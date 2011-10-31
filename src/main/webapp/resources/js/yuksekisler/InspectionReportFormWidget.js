/**
 * Created by JetBrains WebStorm.
 * User: capacman
 * Date: 10/2/11
 * Time: 7:16 PM
 * To change this template use File | Settings | File Templates.
 */

dojo.provide('yuksekisler.InspectionReportFormWidget');

dojo.declare("yuksekisler.InspectionReportFormWidget", [dijit.form.Form,yuksekisler._ProperDestroyMixin], {
    afterCancel:null,
    afterSave:null,
    equipmentId:null,
    postCreate:function() {
        this.enctype = 'multipart/form-data';
        this.method = 'post';
        var div = dojo.create('div');
        var select = new dijit.form.Select({
            id:'stateSelect',
            name:'stateSelect',
            options:[
                {
                    value:"",
                    label:"select state",
                    selected:true
                },
                {
                    value:"USABLE",
                    label:"USABLE"
                },
                {
                    value:"NOTUSABLE",
                    label:"NOTUSABLE"
                },
                {
                    value:"FIXED",
                    label:"FIXED"
                }
            ]
        }).placeAt(div);
        select.startup();
        this.addInner(select);

        var editor = new dijit.Editor({
            id:'report',
            name:'report',
            extraPlugins:['foreColor','hiliteColor','|','createLink','fullscreen']
        }).placeAt(this.domNode);
        editor.startup();
        //this.addInner(editor);

        var save = new dijit.form.Button({
            id:'save',
            label:'Save'
        }).placeAt(div);
        save.startup();
        this.addInner(save);

        var cancel = new dijit.form.Button({
            id:'cancel',
            label:'Cancel'
        }).placeAt(div);
        cancel.startup();
        this.addInner(cancel);

        var inspectionDate = new dijit.form.DateTextBox({
            id:'inspectionDate',
            name:'inspectionDate',
            value:new Date()
        }).placeAt(div);
        inspectionDate.startup();
        this.addInner(inspectionDate);


        this.uploader = new dojox.form.Uploader({
            label:'Upload Images',
            name:'uploadedfile',
            id:'inspectionReportUploader',
            "class":'fileUploader',
            multiple:true
        }).placeAt(div);
        this.uploader.startup();
        this.addInner(this.uploader);

        var fileList = new dojox.form.uploader.FileList({
            uploaderId:'inspectionReportUploader',
            "class":'uploaderFileList'
        }).placeAt(div);
        fileList.startup();
        this.addInner(fileList);

        dojo.connect(this.uploader, 'onComplete', this, this.formCompleted);
        this.uploader.url = dojo.config.applicationBase + "/file/image/upload";
        dojo.place(div, this.domNode);
        dojo.connect(save, 'onClick', this, this.onSubmit);
        dojo.connect(cancel, 'onClick', this.afterCancel);
        dojo.addClass(this.domNode, 'yuksekisler-widget');
        this.inherited(arguments);
    },
    onSubmit:function() {
        this.validate();
        if (this.uploader.getFileList().length > 0) {
            this.uuid = dojox.uuid.generateRandomUuid();
            this.uploader.upload({
                uploadId:this.uuid
            });
        } else
            this.formCompleted();
    },
    formCompleted:function(e) {
        var data = dojo.formToObject(this.domNode);
        data.report = this.get('value').report;
        if (this.uuid)
            data.filesUUID = this.uuid;
        dojo.xhrPost({
            url: dojo.config.applicationBase + "/equipment/" + this.equipmentId + "/inspectionReport",
            handleAs: "json",
            load: dojo.hitch(this, function(data) {
                yuksekisler.app.equipmentStore.evict(this.equipmentId);
                this.afterSave(data)
            }),
            error: function(error) {
                //show nice error function
                alert("login failed" + error);
            },
            content:data
        });
    }
});