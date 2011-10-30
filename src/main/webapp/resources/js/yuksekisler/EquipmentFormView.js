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
    noHashChange:false,
    postCreate:function() {
        //this.uploader.onComplete = dojo.hitch(this, this.formCompleted);
        dojo.connect(this.uploader, 'onComplete', this, this.formCompleted);
        this.categorySelect.set('store', new dojo.data.ObjectStore({objectStore: this.categoryStore,labelProperty:'name'}));
        this.brandSelect.set('store', new dojo.data.ObjectStore({objectStore: this.brandStore,labelProperty:'name'}));

        if (this.equipment) {
            dojo.when(this.equipment, dojo.hitch(this, function(value) {

                this.productName.set('value', value.productName);
                this.productCode.set('value', value.productCode);
                this.categorySelect.set('value', value.category);
                this.brandSelect.set('value', value.brand);

                var stockEntranceDate = new Date(value.stockEntrance);
                this.stockEntrance.constraints.max = stockEntranceDate;
                this.stockEntrance.set('value', stockEntranceDate);

                var bestBeforeDate = new Date(value.bestBeforeDate);
                this.bestBeforeDate.set('value', bestBeforeDate);

                var productionDate = new Date(value.productionDate);
                this.productionDate.constraints.max = productionDate;
                this.productionDate.set('value', productionDate);
                //indicate that we are doing update
                //also uploader calculating form url once so changing form url doesnt do anything
                this.equipmentID = value.id;
            }));
        } else {
            var dateValue = new Date();
            this.stockEntrance.constraints.max = dateValue;
            this.stockEntrance.set('value', dateValue);

            this.bestBeforeDate.set('value', dateValue);

            this.productionDate.constraints.max = dateValue;
            this.productionDate.set('value', dateValue);
        }
        this.uploader.url = dojo.config.applicationBase + '/file/image/upload'
        dojo.addClass(this.domNode, 'yuksekisler-widget');
        this.inherited(arguments);
    },
    onSave:function() {
        if (this.form.validate()) {
            //check whether we have files and upload them
            if (this.uploader.getFileList().length > 0) {
                this.uuid = dojox.uuid.generateRandomUuid();
                yuksekisler.app.loadingDialog.show();
                this.uploader.upload({
                    uploadId:this.uuid
                });
            } else
                this.formCompleted();
        }
    },
    formCompleted:function(uploadInfo) {
        //yuksekisler.app.loadingDialog.hide();
        if (uploadInfo && this.onImageUpload && !uploadInfo.operationFailed)
            this.onImageUpload(uploadInfo);
        if (!uploadInfo || ( uploadInfo && !uploadInfo.operationFailed)) {
            var postContent = dojo.formToObject(this.form.domNode);
            if (this.uuid)
                postContent.filesUUID = this.uuid;
            var baseUrl = dojo.config.applicationBase + "/equipment/";
            dojo.xhrPost({
                url: this.equipmentID ? baseUrl + this.equipmentID : baseUrl,
                handleAs: "json",
                load: dojo.hitch(this, function(data) {
                    this.equipmentStore.evict(this.equipmentID);
                    if (this.onSubmit)
                        this.onSubmit(this.equipmentID);
                    if (!this.noHashChange)
                        dojo.hash('equipments');
                }),
                content:postContent
            });
        }
    }
});
