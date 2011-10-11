/**
 * Created by JetBrains WebStorm.
 * User: capacman
 * Date: 9/21/11
 * Time: 4:40 PM
 * To change this template use File | Settings | File Templates.
 */
dojo.provide('yuksekisler.EquipmentView');

dojo.declare('yuksekisler.EquipmentView', [dijit._Widget,dijit._Templated,yuksekisler._ProperDestroyMixin], {
    templateString:dojo.cache('yuksekisler.EquipmentView', '../../../templates/equipment_view_template.html'),
    widgetsInTemplate:true,
    categoryStore:null,
    brandStore:null,
    equipmentStore:null,
    equipment:null,
    gutters:false,
    design:'headline',
    postCreate:function() {
        this.inherited(arguments);

        var equipmentChain = this.equipment.then(dojo.hitch(this, this.prepareImageContent));
        equipmentChain = equipmentChain.then(dojo.hitch(this, this.prepareInspectionReports));

        this.editEquipment = new yuksekisler.EquipmentFormView({
            categoryStore:this.categoryStore,
            brandStore:this.brandStore,
            equipmentStore:this.equipmentStore,
            equipment:equipmentChain
        });
        this.editEquipment.startup();

        var container = dojo.create('div', {class:'equipmentFormContainer gradient'});
        this.editEquipmentContent.set('content', this.editEquipment);
    },
    prepareImageContent:function(value) {
        if (value.images.length > 0) {
            /*var imageGallery = new dojox.image.Gallery({
             imageWidth:'500',
             imageHeight:'300'
             });*/
            var imageGallery = new dojox.image.ThumbnailPicker({
                id:'imagePicker',
                size:530,
                isClickable:true,
                isScrollable:false
            });

            imageGallery.startup();
            this.addInner(imageGallery);

            this.imageContent.set('content', imageGallery);
            dojo.subscribe(imageGallery.getClickTopicName(), this, this.lightboxShow);

            var memoryStore = yuksekisler.app.prepareImageStore(value, 'images');

            imageGallery.setDataStore(memoryStore, { count:20 }, {
                imageThumbAttr: "thumb",
                imageLargeAttr: "large"
            });
        } else {
            var img = dojo.create('img', {class:'no-image',src:'/yuksekisler/resources/images/no-image.jpg'});
            this.imageContent.set('content', img);
        }
        this.lightbox = new dojox.image.LightboxDialog().startup();
        this.addInner(this.lightbox);
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
            class:'newPlaceHolder',
            onFocus:dojo.hitch(this, function() {
                dojo.fadeOut({
                    node:dijit.byId('inspectionReportView').domNode,
                    duration:500,
                    onEnd: dojo.hitch(this, function() {
                        dojo.style(dijit.byId('inspectionReportView').domNode, "display", "none");
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
        this.lightbox.show({
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
                var inspectionReportView = dijit.byId('inspectionReportView');
                dojo.style(inspectionReportView.domNode, "display", "block");
                dojo.fadeIn({
                    node:inspectionReportView.domNode,
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
                var inspectionReportView = dijit.byId('inspectionReportView');
                dojo.style(inspectionReportView.domNode, "display", "block");
                dojo.fadeIn({
                    node:inspectionReportView.domNode,
                    duration:500
                }).play();
            }
        }).play();
    }
});