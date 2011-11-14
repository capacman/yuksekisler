/**
 * Created by JetBrains WebStorm.
 * User: capacman
 * Date: 10/23/11
 * Time: 3:53 PM
 * To change this template use File | Settings | File Templates.
 */
dojo.provide('yuksekisler.WorkDefinitionView');

dojo.declare('yuksekisler.WorkDefinitionView', [dijit._Widget,dijit._Templated], {
    templateString:dojo["cache"]('yuksekisler.templates', 'workdefinition_view_template.html', {sanitize: true}),
    widgetsInTemplate:true,
    employeeStore:null,
    equipmentStore:null,
    workDeferred:null,
    categoryStore:null,
    postCreate:function() {
        this.inherited(arguments);
        console.log('in postcreate');
        var deferreds = []
        var categorySelectDeferred = new dojo.Deferred();
        deferreds.push(categorySelectDeferred);

        this.categorySelection.set('store', new dojo.data.ObjectStore({objectStore: this.categoryStore,labelProperty:'name'}));

        dojo.connect(this.categorySelection, 'onSetStore', function() {
            categorySelectDeferred.resolve();
        });
        var employeeStoreDeferred = yuksekisler.app.turnToPromise(dojo.when(this.employeeStore.query({}), dojo.hitch(this, function(results) {
            for (var i in results)
                results[i] = {value: results[i].id.toString(),label:results[i].name};
            this.supervisorSelect.addOption(results);

        })));
        deferreds.push(employeeStoreDeferred);
        deferreds.push(dojo.when(yuksekisler.app.turnToPromise(this.workDeferred), dojo.hitch(this, function(val) {
            this.workDefinition = val;
        })));
        var deferredList = new dojo.DeferredList(deferreds);
        deferredList.addCallback(dojo.hitch(this, function() {
            if (this.workDefinition) {
                this.populateWorkForm(this.workDefinition);
                this.populateEquipmentDragSource(this.workDefinition);
            }
            else {
                this.populateWorkForm();
                this.populateEquipmentDragSource();
            }
            dojo.connect(this.startDate, 'onChange', this, this.dateValuesChanged);
            dojo.connect(this.finishDate, 'onChange', this, this.dateValuesChanged);
            dojo.connect(this.categorySelection, 'onChange', this, this.onCategoryChange);
        }));

    },
    startup:function() {
        console.log('in startup');
        this.inherited(arguments);

    },
    populateWorkForm:function(work) {
        var startDate = work ? new Date(work.lifeTime.startDate) : new Date();
        var finishDate = work && work.lifeTime.endDate ? new Date(work.lifeTime.endDate) : null;
        if (work) {
            this.workName.set('value', work.name);
            this.customerName.set('value', work.customer);
            var supervisorIDX = [];
            dojo.forEach(work.supervisors, function(supervisor) {
                supervisorIDX.push(supervisor.id.toString());
            });
            this.supervisorSelect.set('value', supervisorIDX);
        }

        this.startDate.set('value', startDate, false);
        this.startDate.constraints.min = startDate;

        this.finishDate.set('value', finishDate, false);
        this.finishDate.constraints.min = startDate;


    },
    dateValuesChanged:function() {
        console.log("date value changed");
        this.finishDate.constraints.min = this.startDate.get('value');
        if (this.finishDate.get('value') < this.startDate.get('value'))
            this.finishDate.set('value', this.startDate.get('value'), false);
        this.loadDragSource();
    },
    onCategoryChange:function() {
        this.loadDragSource();
    },
    populateEquipmentDragSource:function(work) {
        this.dndObj = new dojo.dnd.Source(this.equipmentDragSource, {
            copyOnly: false,
            creator: this.equipmentNodeCreator,
            accept: ["available","notavailable"]
        });
        this.dndObj.startup();
        if (work) {
            this.target = new dojo.dnd.Source(this.dropArea, {
                copyOnly: false,
                creator: this.equipmentNodeCreator,
                accept: ["available"]
            });
            this.target.startup();
            this.target.insertNodes(false, work.equipments);
        } else {
            this.target = new dojo.dnd.Source(this.dropArea, {accept: ["available"]});
            this.target.startup();
        }

        dojo.connect(this.dndObj, 'onMouseDown', this, function() {
            this.target.selectNone();
        });
        dojo.connect(this.target, 'onMouseDown', this, function() {
            this.dndObj.selectNone();
        });
        //dojo.connect(this.target, "onDrop", this.onDropTarget);
    },
    onDropTarget:function(source, nodes, copy) {
        if (this != source) {
            dojo.forEach(nodes, dojo.hitch(this, function(node) {
                console.log(this.getItem(node.id));
            }));
        }
    },
    equipmentNodeCreator:function(item, hint) {
        var equipmentWidget = new yuksekisler.EquipmentWidget({
            equipment:item
        });
        if (item.type && item.type === ['notavailable'])
            dojo.addClass(equipmentWidget.domNode, 'unavailableNode');
        return {node:equipmentWidget.domNode,data:item,type:item.type ? item.type : ['available']}
    },
    onSave:function() {
        if (this.workDefinitionViewForm.validate()) {
            var object = dojo.mixin(this.workDefinitionViewForm.get('value'), dojo.formToObject(this.workDefinitionViewForm.domNode));

            object.equipments = [];
            this.target.forInItems(function(obj, id, map) {
                object.equipments.push(obj.data.id);
            }, this);
            object.workers = [];
            if (this.workDefinition)
                object.id = this.workDefinition.id;
            yuksekisler.app.loadingDialog.show();
            dojo.xhrPost({
                url:dojo.config.applicationBase + '/work/',
                handleAs:'json',
                load:function(data) {
                    yuksekisler.app.workStore.evict(data.id);
                    yuksekisler.app.loadingDialog.hide();
                    dojo.publish("globalMessageTopic", [
                        {
                            message: 'WorkDefinition ' + data.name + ' saved'
                        }
                    ]);
                },
                content:object,
                headers:{
                    JsonStore:'false'
                }
            });
        }
    },
    onCancel:function() {
        dojo.hash('works');
    },
    loadDragSource:function () {
        dojo.xhrGet({
            url:dojo.config.applicationBase + '/equipment/available',
            handleAs:'json',
            load:dojo.hitch(this, function(data) {
                for (var i in this.target.map) {
                    var found = false;
                    for (var j in data) {
                        if (this.target.map[i].data.id == data[j].id) {
                            found = true;
                            break;
                        }
                    }
                    if (!found) {
                        this.target.map[i].type = ['notavailable'];
                        this.target.map[i].data.type = ['notavailable'];
                    } else {
                        this.target.map[i].type = ['available'];
                        this.target.map[i].data.type = ['available'];
                    }
                }

                this.target.getAllNodes().forEach(dojo.hitch(this, function(node) {
                    if (this.target.map[node.id].type[0] === 'notavailable') {
                        dojo.addClass(node, 'unavailableNode');
                    } else
                        dojo.removeClass(node, 'unavailableNode');
                }));
                var results = dojo.filter(data, dojo.hitch(this, function(item) {
                    for (var i in this.target.map) {
                        if (this.target.map[i].data.id == item.id)
                            return false;
                    }
                    return true;
                }));
                this.dndObj.selectAll().deleteSelectedNodes();
                this.dndObj.insertNodes(false, results);
            }),
            content:{
                startDate: dojo.date.stamp.toISOString(this.startDate.get('value'), {selector: 'date'}),
                endDate:this.finishDate.get('value') ? dojo.date.stamp.toISOString(this.finishDate.get('value'), {selector: 'date'}) : null,
                categoryID:this.categorySelection.get('value'),
                workID:this.workDefinition ? this.workDefinition.id : null
            }
        });

    }
});