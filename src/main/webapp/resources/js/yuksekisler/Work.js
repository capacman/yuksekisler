/**
 * Created by JetBrains WebStorm.
 * User: capacman
 * Date: 10/23/11
 * Time: 3:53 PM
 * To change this template use File | Settings | File Templates.
 */
define(["dojo/_base/declare", "dijit/_Widget", "dijit/_Templated", "dojo", "dojo/data/ObjectStore", "dojo/dnd/Source", "dijit/Tooltip", "dojox/grid/DataGrid", "yuksekisler/_ProperDestroyMixin", "yuksekisler/misc", "dojo/DeferredList" , "dojox/form/CheckedMultiSelect"], function (declare, _Widget, _Templated, dojo, ObjectStore, Source, Tooltip, DataGrid, _ProperDestroyMixin, misc, DeferredList) {
    var EquipmentWidget = declare(_Widget, {
        equipment:null,
        buildRendering:function () {
            // create the DOM for this widget
            this.domNode = dojo.create("div", {
                "class":'simpleWidgetContainer equipmentWidget'
            });
            this.equipmentImage = dojo.create('img', {"class":'simpleWidgetImage', align:'right'}, this.domNode);
            this.equipmentProductName = dojo.create('div', {"class":'simpleWidgetDefinition'}, this.domNode);
            this.equipmentProductCode = dojo.create('div', {"class":'simpleWidgetDefinition'}, this.domNode);

            this.inherited(arguments);
        },
        postCreate:function () {

            dojo.attr(this.equipmentProductName, {
                innerHTML:'<b>' + this.equipment.productName + '</b>'
            });

            dojo.attr(this.equipmentProductCode, {
                innerHTML:'ProductCode ' + this.equipment.productCode
            });

            dojo.attr(this.equipmentImage, {
                src:this.equipment.images.length > 0 ? dojo.config.applicationBase + '/file/image/' + this.equipment.images[0].id + '/thumbnail' : dojo.config.applicationBase + '/resources/images/no-image.jpg'
            });

            this.inherited(arguments);
        }
    });
    var WorkDefinitionView = declare([_Widget, _Templated], {
        templateString:dojo["cache"]('yuksekisler.templates', 'workdefinition_view_template.html', {sanitize:true}),
        widgetsInTemplate:true,
        employeeStore:null,
        equipmentStore:null,
        workDeferred:null,
        categoryStore:null,
        postCreate:function () {
            this.inherited(arguments);
            console.log('in postcreate');
            var deferreds = []
            var categorySelectDeferred = new dojo.Deferred();
            deferreds.push(categorySelectDeferred);

            this.categorySelection.set('store', new ObjectStore({objectStore:this.categoryStore, labelProperty:'name'}));

            dojo.connect(this.categorySelection, 'onSetStore', function () {
                categorySelectDeferred.resolve();
            });
            var employeeStoreDeferred = misc.turnToPromise(dojo.when(this.employeeStore.query({}), dojo.hitch(this, function (results) {
                for (var i in results)
                    results[i] = {value:results[i].id.toString(), label:results[i].name};
                this.supervisorSelect.addOption(results);

            })));
            deferreds.push(employeeStoreDeferred);
            deferreds.push(dojo.when(misc.turnToPromise(this.workDeferred), dojo.hitch(this, function (val) {
                this.workDefinition = val;
            })));
            var deferredList = new DeferredList(deferreds);
            deferredList.addCallback(dojo.hitch(this, function () {
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
        startup:function () {
            console.log('in startup');
            this.inherited(arguments);

        },
        populateWorkForm:function (work) {
            var startDate = work ? new Date(work.lifeTime.startDate) : new Date();
            var finishDate = work && work.lifeTime.endDate ? new Date(work.lifeTime.endDate) : null;
            if (work) {
                this.workName.set('value', work.name);
                this.customerName.set('value', work.customer);
                var supervisorIDX = [];
                dojo.forEach(work.supervisors, function (supervisor) {
                    supervisorIDX.push(supervisor.id.toString());
                });
                this.supervisorSelect.set('value', supervisorIDX);
            }

            this.startDate.set('value', startDate, false);
            this.startDate.constraints.min = startDate;

            this.finishDate.set('value', finishDate, false);
            this.finishDate.constraints.min = startDate;


        },
        dateValuesChanged:function () {
            console.log("date value changed");
            this.finishDate.constraints.min = this.startDate.get('value');
            if (this.finishDate.get('value') < this.startDate.get('value'))
                this.finishDate.set('value', this.startDate.get('value'), false);
            this.loadDragSource();
        },
        onCategoryChange:function () {
            this.loadDragSource();
        },
        populateEquipmentDragSource:function (work) {
            this.dndObj = new Source(this.equipmentDragSource, {
                copyOnly:false,
                creator:this.equipmentNodeCreator,
                accept:["available", "notavailable"]
            });
            this.dndObj.startup();
            if (work) {
                this.target = new Source(this.dropArea, {
                    copyOnly:false,
                    creator:this.equipmentNodeCreator,
                    accept:["available"]
                });
                this.target.startup();
                this.target.insertNodes(false, work.equipments);
            } else {
                this.target = new Source(this.dropArea, {accept:["available"]});
                this.target.startup();
            }

            dojo.connect(this.dndObj, 'onMouseDown', this, function () {
                this.target.selectNone();
            });
            dojo.connect(this.target, 'onMouseDown', this, function () {
                this.dndObj.selectNone();
            });
            //dojo.connect(this.dndObj, "onDrop", this.onDropTarget);
        },
        onDropTarget:function (source, nodes, copy) {
            if (this != source) {
                dojo.forEach(nodes, dojo.hitch(this, function (node) {
                    console.log(this.getItem(node.id));
                }));
            }
        },
        equipmentNodeCreator:function (item, hint) {
            var equipmentWidget = new EquipmentWidget({
                equipment:item
            });
            if (item.type && item.type[0] === 'notavailable') {
                dojo.addClass(equipmentWidget.domNode, 'unavailableNode');
                new Tooltip({
                    connectId:[equipmentWidget.domNode],
                    label:"This equipment used in another work!"
                });
            }
            return {node:equipmentWidget.domNode, data:item, type:item.type ? item.type : ['available']}
        },
        onSave:function () {
            if (this.workDefinitionViewForm.validate()) {
                var object = dojo.mixin(this.workDefinitionViewForm.get('value'), dojo.formToObject(this.workDefinitionViewForm.domNode));

                object.equipments = [];
                this.target.forInItems(function (obj, id, map) {
                    object.equipments.push(obj.data.id);
                }, this);
                object.supervisors = this.supervisorSelect.get('value');
                object.workers = [];
                if (this.workDefinition)
                    object.id = this.workDefinition.id;
                misc.loadingDialog.show();
                dojo.xhrPost({
                    url:dojo.config.applicationBase + '/work/',
                    handleAs:'json',
                    load:function (data) {
                        misc.workStore.evict(data.id);
                        misc.loadingDialog.hide();
                        dojo.publish("globalMessageTopic", [
                            {
                                message:'WorkDefinition ' + data.name + ' saved'
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
        onCancel:function () {
            dojo.hash('works');
        },
        loadDragSource:function () {
            dojo.xhrGet({
                url:dojo.config.applicationBase + '/equipment/available',
                handleAs:'json',
                load:dojo.hitch(this, function (data) {
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
                    var tooltip = new Tooltip({
                        label:"This equipment used in another work!"
                    });
                    this.target.getAllNodes().forEach(dojo.hitch(this, function (node) {
                        if (this.target.map[node.id].type[0] === 'notavailable') {
                            dojo.addClass(node, 'unavailableNode');
                            tooltip.addTarget(node);
                        } else {
                            dojo.removeClass(node, 'unavailableNode');
                        }
                    }));
                    var results = dojo.filter(data, dojo.hitch(this, function (item) {
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
                    startDate:dojo.date.stamp.toISOString(this.startDate.get('value'), {selector:'date'}),
                    endDate:this.finishDate.get('value') ? dojo.date.stamp.toISOString(this.finishDate.get('value'), {selector:'date'}) : null,
                    categoryID:this.categorySelection.get('value'),
                    workID:this.workDefinition ? this.workDefinition.id : null
                }
            });
        }
    });
    var WorkListView = declare([_Widget, _Templated, _ProperDestroyMixin], {
        templateString:dojo["cache"]('yuksekisler.templates', 'worklist_view_template.html', {sanitize:true}),
        widgetsInTemplate:true,
        workStore:null,
        postCreate:function () {
            this.grid = new DataGrid({
                query:{},
                selectionMode:'single',
                id:'workListGrid',
                rowCount:10,
                store:new ObjectStore({objectStore:this.workStore, labelProperty:'name'}),
                structure:[
                    {name:'Name', field:'name', width:'auto'},
                    {name:'Customer', field:'customer', width:'auto'},
                    {name:'Start Date', get:this.getStartDate, formatter:this.formatDate, width:'auto'},
                    {name:'End Date', get:this.getEndDate, formatter:this.formatDate, width:'auto'}
                ],
                onRowContextMenu:dojo.hitch(this, misc.onRowContextMenu)
            });
            this.gridContainer.set('content', this.grid);
            this.grid.startup();
            dojo.connect(this.searchBox, 'onKeyUp', this, this.textBoxChanged);
            dojo.connect(this.grid, 'onRowClick', this, this.onRowClick);
            this.newWork = new dijit.form.Button({
                label:'New Work',
                onClick:function () {
                    dojo.hash('newwork');
                }
            }).placeAt(this.subContainer);
            this.addInner(this.newWork);
            this.newWork.startup();
            this.inherited(arguments);
            dojo.addClass(this.domNode, 'yuksekisler-widget');
            this.inherited(arguments);
        },
        formatDate:function (value) {
            if (value)
                return dojo.date.locale.format(new Date(value), {
                    datePattern:'dd/MM/yyyy',
                    formatLength:'short',
                    selector:'date'
                });
            return "";
        },
        getStartDate:function (colIndex, item) {
            if (item)
                return item.lifeTime.startDate;
        },
        getEndDate:function (colIndex, item) {
            if (item)
                return item.lifeTime.endDate;
        },
        textBoxChanged:function (e) {
            if (this.timerId) {
                clearTimeout(this.timerId);
            }
            this.timerId = setTimeout(dojo.hitch(this, 'updateGrid'), 400);
        },
        onRowClick:function (e) {
            var item = this.grid.getItem(e.rowIndex);
            dojo.hash('work/' + item.id);
        },
        updateGrid:function () {
            if (this.searchBox.get('value') == '')
                this.grid.setQuery({});
            else
                this.grid.setQuery({'searchString':this.searchBox.get('value')});
        }
    });
    return {
        WorkDefinitionView:WorkDefinitionView,
        WorkListView:WorkListView
    };
});
