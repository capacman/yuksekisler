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
    postCreate:function() {
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
            id:'dijitEditor',
            extraPlugins:['foreColor','hiliteColor','|','createLink','fullscreen']
        }).placeAt(this.domNode);
        editor.startup();
        this.addInner(editor);

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
            value:new Date()
        }).placeAt(div);

        dojo.place(div, this.domNode);


        dojo.connect(save, 'onClick', this, this.onSubmit);
        dojo.connect(cancel, 'onClick', this.afterCancel);
        this.inherited(arguments);
    },
    onSubmit:function() {
        dojo.xhrPost({
            url: dojo.config.applicationBase + "/equipment",
            handleAs: "json",
            load: function(data) {
                this.afterSave(data);
            },
            error: function(error) {
                //show nice error function
                alert("save failed" + error);
            },
            content:this.get('value')
        });
    }
})
    ;