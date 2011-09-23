/**
 * Created by JetBrains WebStorm.
 * User: capacman
 * Date: 9/21/11
 * Time: 4:40 PM
 * To change this template use File | Settings | File Templates.
 */
dojo.provide('yuksekisler.EquipmentView');

dojo.require('dijit.layout.BorderContainer');
dojo.require('dijit._Templated');

dojo.declare('yuksekisler.EquipmentView', [dijit.layout.BorderContainer,dijit._Templated], {
    templateString:dojo.cache('yuksekisler.EquipmentView', dojo.moduleUrl('yuksekisler', '../../templates/equipment_view_template.html')),
    widgetsInTemplate:true,
    categoryStore:null,
    brandStore:null,
    equipmentStore:null,
    equipment:null,
    gutters:false,
    postCreate:function() {
        this.inherited(arguments);
        this.editEquipment = new yuksekisler.NewEquipmentView({
            categoryStore:this.categoryStore,
            brandStore:this.brandStore,
            equipmentStore:this.equipmentStore,
            equipment:this.equipment
        });
        this.editEquipmentContent.set('content',this.editEquipment);
        this.editEquipment.startup();
    }
});