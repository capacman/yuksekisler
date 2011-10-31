/**
 * Created by JetBrains WebStorm.
 * User: capacman
 * Date: 10/22/11
 * Time: 10:18 PM
 * To change this template use File | Settings | File Templates.
 */
dojo.provide('yuksekisler.UploaderSuccessWidget');

dojo.declare('yuksekisler.UploaderSuccessWidget', [dijit._Widget], {
    uploadInfo:null,
    buildRendering: function() {
        // create the DOM for this widget
        this.domNode = dojo.create("div", {
            "class":'fileUploadResultWidget'
        });
        this.inherited(arguments);
    },
    postCreate:function() {
        if (this.uploadInfo.length == 1 && this.uploadInfo.operationFailed)
            dojo.create('div', {
                "class":'fileUploadFileInfo',
                innerHtml:'<b>File upload failed</b>',
                style:{'color':'red'}
            }, this.domNode);
        else {
            var domNode = this.domNode;
            dojo.forEach(this.uploadInfo, function(file) {
                var fileInfo = dojo.create("div", {
                    "class":'fileUploadFileInfo',
                    innerHTML:'<i>' + file.file + '</i>' + (file.result == 'success' ? ' saved' : ' failed')
                }, domNode);
                if (file.result != 'success')
                    fileInfo.style('color', 'red');
            });
        }
        this.inherited(arguments);
    }
});