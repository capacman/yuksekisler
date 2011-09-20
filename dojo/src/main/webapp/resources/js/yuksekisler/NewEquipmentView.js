/**
 * Created by JetBrains WebStorm.
 * User: capacman
 * Date: 9/18/11
 * Time: 6:55 PM
 * To change this template use File | Settings | File Templates.
 */
dojo.provide('yuksekisler.NewEquipmentView');


dojo.require("dijit._Widget");
dojo.require('dijit._Templated');


dojo.declare('yuksekisler.NewEquipmentView', [dijit._Widget,dijit._Templated], {
    templateString:dojo.cache('yuksekisler.NewEquipmentView', dojo.moduleUrl('yuksekisler', '../../templates/new_equipment_template.html')),
    widgetsInTemplate:true,
    categoryStore:null,
    brandStore:null,
    equipmentStore:null,
    postCreate:function() {
        this.categorySelect.set('store', new dojo.data.ObjectStore({objectStore: this.categoryStore,labelProperty:'name'}));
        this.brandSelect.set('store', new dojo.data.ObjectStore({objectStore: this.brandStore,labelProperty:'name'}));
        this.inherited(arguments);
    },
    onSave:function() {
        var newObject = this.form.get('value');
        var brandDef = this.brandStore.get(newObject.brand).then(function(object) {
            newObject.brand = object;
        });
        var categoryDef = this.categoryStore.get(newObject.category).then(function(object) {
            newObject.category = object;
        });
        var defs = new dojo.DeferredList([brandDef, categoryDef]);
        defs.then(function(results) {
            console.log(results);
            this.equipmentStore.add(newObject);
        });
    }
});