/**
 * Created by JetBrains WebStorm.
 * User: capacman
 * Date: 10/23/11
 * Time: 3:53 PM
 * To change this template use File | Settings | File Templates.
 */
dojo.provide('yuksekisler.WorkDefinitionView');

dojo.declare('yuksekisler.WorkDefinitionView', [dijit._Widget,dijit._Templated], {
    templateString:dojo.cache('yuksekisler.WorkDefinitionView', '../../../templates/workdefinition_view_template.html'),
    widgetsInTemplate:true,
    employeeStore:null,
    equipmentStore:null,
    workDeferred:null,
    postCreate:function() {
        if (this.workDeferred)
            this.workDeferred.then(dojo.hitch(this, 'populateWorkForm')).then(dojo.hitch(this, function(value) {
                this.populateEquipmentDragSource(value);
            }));
        else {
            this.populateWorkForm();
            this.populateEquipmentDragSource();
        }
        this.supervisorSelect.set('store', new dojo.data.ObjectStore({objectStore: this.employeeStore,labelProperty:'name'}));


        this.inherited(arguments);
    },
    populateWorkForm:function(work) {
        var startDate = work ? new Date(work.startDate) : null;
        var finishDate = work || work.endDate ? new Date(work.endDate) : null;
        if (work) {
            this.workName.set('value', work.name);
            this.customerName.set('value', work.customer);
        }

        this.startDate.set('value', startDate);
        this.startDate.constraints.min = startDate;

        this.finishDate.set('value', finishDate);
        this.finishDate.constraints.min = startDate;
        return work;
    },
    dateValuesChanged:function() {
        this.finishDate.constraints.min = this.startDate.get('value');
        if (this.finishDate.get('value') < this.startDate.get('value'))
            this.finishDate.set('value', this.startDate.get('value'));
    },
    populateEquipmentDragSource:function(workDefinition) {
        var dndObj = new dojo.dnd.Target(this.equipmentDragSource, {
            copyOnly: false,
            creator: this.equipmentNodeCreator,
            accept: ["default"]
        });
        dojo.xhrGet({
            url:dojo.config.applicationBase + '/equipment/available',
            handleAs:'json',
            load:function(data) {
                dndObj.insertNodes(false, data);
            },
            content:{
                startDate:this.startDate.get('value'),
                endDate:this.finishDate.get('value')
            }
        });

        
        if (workDefinition) {
            var target = new dojo.dnd.Target(this.dropArea, {
                copyOnly: false,
                creator: this.equipmentNodeCreator,
                accept: ["default"]
            });
            target.insertNodes(false, workDefinition.equipments);
        } else
            var target = new dojo.dnd.Target(this.dropArea, {accept: ["default"]});
    },
    equipmentNodeCreator:function(item) {
        console.log(item);
        var equipmentWidget = new yuksekisler.EquipmentWidget({
            equipment:item
        });

        return {node:equipmentWidget.domNode,data:item,type:['default']}
    }
});