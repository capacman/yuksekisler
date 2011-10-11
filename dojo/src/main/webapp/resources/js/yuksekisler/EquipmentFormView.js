/**
 * Created by JetBrains WebStorm.
 * User: capacman
 * Date: 9/18/11
 * Time: 6:55 PM
 * To change this template use File | Settings | File Templates.
 */
dojo.provide('yuksekisler.EquipmentFormView');


dojo.require("dijit._Widget");
dojo.require('dijit._Templated');


dojo.declare('yuksekisler.EquipmentFormView', [dijit._Widget,dijit._Templated], {
    templateString:dojo.cache('yuksekisler.EquipmentFormView', '../../../templates/equipment_form_template.html'),
    widgetsInTemplate:true,
    categoryStore:null,
    brandStore:null,
    equipmentStore:null,
    equipment:null,
    onSubmit:null,
    postCreate:function() {
        //this.uploader.onComplete = dojo.hitch(this, this.formCompleted);
        dojo.connect(this.uploader, 'onComplete', this, this.formCompleted);
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
                //indicate that we are doing update
                //also uploader calculating form url once so changing form url doesnt do anything

            }));
        } else {
            var currentVal = new Date();
            this.stockEntrance.constraints.max = currentVal;
            this.bestBeforeDate.constraints.min = new Date();
            this.stockEntrance.set('value', currentVal);
        }
         this.uploader.url = dojo.config.applicationBase + '/file/image/upload'
        this.inherited(arguments);
    },
    onSave:function() {
        this.form.validate();
        //check whether we have files and upload them
        if (this.uploader.getFileList().length > 0) {
            this.uuid = dojox.uuid.generateRandomUuid();
            this.uploader.upload({
                uploadId:this.uuid
            });
        } else
            this.formCompleted();
    },
    formCompleted:function(e) {
        var postContent = dojo.formToObject(this.form.domNode);
        if (this.uuid)
            postContent.filesUUID = this.uuid;

        dojo.xhrPost({
            url: dojo.config.applicationBase + "/equipment/",
            handleAs: "json",
            load: dojo.hitch(this,function(data) {
                if (this.onSubmit)
                    this.onSubmit();
                dojo.hash('equipments');
            }),
            error: function(error) {
                //show nice error function
                alert("login failed" + error);
            },
            content:postContent
        });

    }
});