/**
 * Created by JetBrains WebStorm.
 * User: capacman
 * Date: 9/30/11
 * Time: 5:04 PM
 * To change this template use File | Settings | File Templates.
 */
dojo.provide('yuksekisler.InspectionReportWidget');

dojo.declare('yuksekisler.InspectionReportWidget', [dijit.TitlePane], {
    report:null,
    equipmentView:null,
    postCreate:function() {
        this.inherited(arguments);
        var title = this.report.inspector.name + ' on ' + new Date(this.report.inspectionDate) + ' as ' + this.report.status;
        this.set('title', title);
        var contentDiv = dojo.create('div');
        if (this.report.images.length > 0) {
            var memoryStore = yuksekisler.app.prepareImageStore(report, 'images');
            var imageGallery = new dojox.image.ThumbnailPicker({
                isClickable:true,
                isScrollable:false
            }).placeAt(contentDiv);
            dojo.subscribe(imageGallery.getClickTopicName(), this.equipmentView, this.equipmentView.lightBoxShow);
            imageGallery.setDataStore(memoryStore, { count:20 }, {
                imageThumbAttr: "thumb",
                imageLargeAttr: "large"
            });
        }

        var reportDiv = dojo.create('div', {innerHTML:this.report.report});
        dojo.place(reportDiv, contentDiv);
        this.set('content', contentDiv);

    }
});