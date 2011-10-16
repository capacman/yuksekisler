/**
 * Created by JetBrains WebStorm.
 * User: capacman
 * Date: 9/30/11
 * Time: 5:04 PM
 * To change this template use File | Settings | File Templates.
 */
dojo.provide('yuksekisler.InspectionReportWidget');

dojo.declare('yuksekisler.InspectionReportWidget', [dijit.TitlePane,yuksekisler._ProperDestroyMixin], {
    report:null,
    equipmentView:null,
    postCreate:function() {
        this.inherited(arguments);
        var title = this.report.inspector.name + ' on ' + new Date(this.report.inspectionDate) + ' as ' + this.report.status;
        this.set('title', title);
        var contentDiv = dojo.create('div', {style:'height:100%;width:100%;'});


        var reportDiv = dojo.create('div', {innerHTML:this.report.report});
        if (this.report.images.length > 0) {
            var memoryStore = yuksekisler.app.prepareImageStore(this.report, 'images');
            var imageGallery = new dojox.image.ThumbnailPicker({
                isClickable:true,
                isScrollable:false,
                size:400
            });
            imageGallery.startup();
            imageGallery.placeAt(contentDiv);
            this.addInner(imageGallery);
            dojo.subscribe(imageGallery.getClickTopicName(), this, this.lightboxShow);
            imageGallery.setDataStore(memoryStore, { count:20 }, {
                imageThumbAttr: "thumb",
                imageLargeAttr: "large"
            });
        }
        dojo.place(reportDiv, contentDiv);
        this.set('content', contentDiv);
        dojo.addClass(this.domNode, 'yuksekisler-widget');
    },
    lightboxShow:function(packet) {
        this.equipmentView.lightboxShow(packet);
    }
});