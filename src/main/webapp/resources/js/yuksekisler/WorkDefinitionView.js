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
        this.workDeferred.then(dojo.hitch(this, 'populateWorkForm'));
        this.supervisorSelect.set('store', new dojo.data.ObjectStore({objectStore: this.employeeStore,labelProperty:'name'}));
        this.populateEquipmentDragSource();
        var target = new dojo.dnd.Target(this.dropArea, {accept: ["default"]});
        this.inherited(arguments);
    },
    populateWorkForm:function(work) {
        this.workName.set('value', work.name);
        this.customerName.set('value', work.customer);

        var startDate = new Date(work.startDate);
        this.startDate.set('value', startDate);
        this.startDate.constraints.min = startDate;


        var finishDate = work.endDate ? new Date(work.endDate) : null;
        this.finishDate.set('value', finishDate);
        this.finishDate.constraints.min = startDate;
        return work;
    },
    dateValuesChanged:function() {
        this.finishDate.constraints.min = this.startDate.get('value');
        if (this.finishDate.get('value') < this.startDate.get('value'))
            this.finishDate.set('value', this.startDate.get('value'));
    },
    populateEquipmentDragSource:function() {
        var dndObj = new dojo.dnd.Source(this.equipmentDragSource, {
            copyOnly: false,
            creator: function(item) {
                console.log(item);
                var equipmentWidget = new yuksekisler.EquipmentWidget({
                    equipment:item
                });

                return {node:equipmentWidget.domNode,data:item,type:['default']}
            }
        });
        this.equipmentStore.query({}).then(function(data) {
            dndObj.insertNodes(false, data);
        });
    }
});