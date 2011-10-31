/**
 * Created by JetBrains WebStorm.
 * User: capacman
 * Date: 9/21/11
 * Time: 4:40 PM
 * To change this template use File | Settings | File Templates.
 */
dojo.provide('yuksekisler.EquipmentView');

dojo.declare('yuksekisler.EquipmentView', [dijit._Widget,dijit._Templated,yuksekisler._ProperDestroyMixin], {
    templateString:dojo["cache"]('yuksekisler.templates', 'equipment_view_template.html', {sanitize: true}),
    widgetsInTemplate:true,
    categoryStore:null,
    brandStore:null,
    equipmentStore:null,
    equipment:null,
    gutters:false,
    design:'headline',
    postCreate:function() {
        this.inherited(arguments);

        var equipmentChain = dojo.when(this.equipment, dojo.hitch(this, this.prepareImageContent));
        equipmentChain = dojo.when(equipmentChain, dojo.hitch(this, this.prepareInspectionReports));

        this.editEquipment = new yuksekisler.EquipmentFormView({
            categoryStore:this.categoryStore,
            brandStore:this.brandStore,
            equipmentStore:this.equipmentStore,
            equipment:equipmentChain,
            noHashChange:true,
            onSubmit:dojo.hitch(this, this.equipmentChanged),
            onImageUpload:dojo.hitch(this, this.onImageUpload)
        });
        this.editEquipment.startup();

        this.editEquipmentContent.set('content', this.editEquipment);
        dojo.addClass(this.domNode, 'yuksekisler-widget');
    },
    prepareImageContent:function(value) {
        if (value.images.length > 0) {
            /*var imageGallery = new dojox.image.Gallery({
             imageWidth:'500',
             imageHeight:'300'
             });*/
            this.prepareImageGalery();
            this.initImageStore(value);
        } else {
            var img = dojo.create('img', {"class":'no-image',src:'/yuksekisler/resources/images/no-image.jpg'});
            this.imageContent.set('content', img);
        }

        //this.addInner(this.lightbox);
        return value;
    },
    prepareInspectionReports:function(value) {
        var container = dojo.create('div');
        var group = new dojox.widget.TitleGroup({id:'reportsGroup'}).placeAt(container);
        for (var x in value.inspectionReports) {
            var inspectionReport = value.inspectionReports[x];
            var inspectionReportWidget = new yuksekisler.InspectionReportWidget({
                report:inspectionReport,
                equipmentView:this,
                open:x == (value.inspectionReports.length - 1)
            }).placeAt(group);
        }
        this.addInner(group);

        var placeHolderTextBox = new dijit.form.TextBox({
            id:'inspectionReportPlaceHolder',
            placeHolder:'new inspection report',
            "class":'newPlaceHolder',
            onFocus:dojo.hitch(this, function() {
                dojo.fadeOut({
                    node:dojo.byId('inspectionReportView'),
                    duration:500,
                    onEnd: dojo.hitch(this, function() {
                        dojo.style(dojo.byId('inspectionReportView'), "display", "none");
                        dojo.style('inspectionReportFormView', 'opacity', 0);
                        var reportForm = new yuksekisler.InspectionReportFormWidget({
                            id:'reportForm',
                            equipmentId:value.id,
                            afterSave:dojo.hitch(this, this.reportSaved),
                            afterCancel:dojo.hitch(this, this.reportCanceled)
                        }).placeAt(dojo.byId('inspectionReportFormView'));
                        dojo.fadeIn({
                            node:'inspectionReportFormView',
                            duration:500
                        }).play();
                    })
                }).play();
            })
        }).placeAt(container);
        this.addInner(placeHolderTextBox);
        this.inspectionReportContent.set('content', container);
        return value;
    },
    lightboxShow:function (packet) {

        // you can just "show" this image

        dijit.byId('imageLightBox').show({
            href: packet.largeUrl,
            title: packet.title
        });

    },
    reportSaved:function(newReport) {
        dojo.fadeOut({
            node:'inspectionReportFormView',
            duration:500,
            onEnd:function() {
                dijit.byId('reportForm').destroy();
                var inspectionReportView = dojo.byId('inspectionReportView');
                dojo.style(inspectionReportView, "display", "block");
                dojo.fadeIn({
                    node:inspectionReportView,
                    duration:500
                }).play();
                var group = dijit.byId('reportsGroup');
                var inspectionReportWidget = new yuksekisler.InspectionReportWidget({
                    report:newReport,
                    equipmentView:this
                }).placeAt(group);
                group.selectChild(inspectionReportWidget);
            }
        }).play();
    },
    reportCanceled:function() {
        var reportForm = dijit.byId('reportForm');
        dojo.fadeOut({
            node:'inspectionReportFormView',
            duration:500,
            onEnd:function() {
                dijit.byId('reportForm').destroy();
                var inspectionReportView = dojo.byId('inspectionReportView');
                dojo.style(inspectionReportView, "display", "block");
                dojo.fadeIn({
                    node:inspectionReportView,
                    duration:500
                }).play();
            }
        }).play();
    },
    equipmentChanged:function(equipmentID) {
        dojo.xhrGet({
            url:dojo.config.applicationBase + '/equipment/' + equipmentID,
            handleAs:'json',
            load:dojo.hitch(this, function(value) {
                if (!this.imageGallery)
                    this.prepareImageGalery();
                this.initImageStore(value);
            })
        });
    },
    initImageStore:function (value) {
        var memoryStore = yuksekisler.app.prepareImageStore(value, 'images');
        this.imageGallery.setDataStore(memoryStore, { count:20 }, {
            imageThumbAttr: "thumb",
            imageLargeAttr: "large"
        });
    },
    prepareImageGalery:function() {
        this.imageGallery = new dojox.image.ThumbnailPicker({
            id:'imagePicker',
            size:530,
            isClickable:true,
            isScrollable:false
        });
        this.imageGallery.startup();
        this.addInner(this.imageGallery);
        this.imageContent.set('content', this.imageGallery);
        dojo.subscribe(this.imageGallery.getClickTopicName(), this, this.lightboxShow);
    },
    onImageUpload:function(uploadInfo) {
        var infoWidget = new yuksekisler.UploaderSuccessWidget({
            uploadInfo:uploadInfo
        }).placeAt(this.domNode, 'first');
        dojo.style(infoWidget.domNode, 'opacity', '0');
        dojo.fadeIn({
            node:infoWidget.domNode,
            duration:900
        }).play();
        setTimeout(function() {
            dojo.fadeOut({
                node:infoWidget.domNode,
                duration:900,
                onEnd:function() {
                    dojo.style(infoWidget.domNode, 'display', 'none');
                    infoWidget.destroyRecursive(false);
                }
            }).play();
        }, 6900);
    }
});