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
    equipment:null,
    onSubmit:null,
    postCreate:function() {
        this.categorySelect.set('store', new dojo.data.ObjectStore({objectStore: this.categoryStore,labelProperty:'name'}));
        this.brandSelect.set('store', new dojo.data.ObjectStore({objectStore: this.brandStore,labelProperty:'name'}));

        if (this.equipment) {
            this.equipment.then(dojo.hitch(this, function(value) {

                this.productName.set('value', value.productName);
                this.productCode.set('value', value.productCode);
                this.categorySelect.set('value', value.category);
                this.brandSelect.set('value', value.brand);

                var stockEntranceDate = new Date(value.stockEntrance);
                this.stockEntrance.constraints.max = stockEntranceDate;
                this.stockEntrance.set('value', stockEntranceDate);

                var bestBeforeDate = new Date(value.bestBeforeDate);
                this.bestBeforeDate.constraints.min = bestBeforeDate;
                this.bestBeforeDate.set('value', bestBeforeDate);

                this.productionDate.set('value', new Date(value.productionDate));
            }));
        } else {
            var currentVal = new Date();
            this.stockEntrance.constraints.max = currentVal;
            this.bestBeforeDate.constraints.min = new Date();
            this.stockEntrance.set('value', currentVal);
        }
        this.inherited(arguments);
    },
    onSave:function() {
        this.form.validate();
        this.uploader.submit();
        dojo.hash('equipments');
        if (this.onSubmit)
            this.onSubmit();
    }
});