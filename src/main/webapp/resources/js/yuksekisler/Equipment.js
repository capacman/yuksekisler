/**
 * Created by JetBrains WebStorm.
 * User: mint
 * Date: 1/3/12
 * Time: 2:49 PM
 * To change this template use File | Settings | File Templates.
 */
define(["dojo/_base/declare", "dijit/_Widget", "dijit/_Templated", "dojo", "dojo/data/ObjectStore",
    "dojox/grid/DataGrid", "dojox/uuid/generateRandomUuid", "yuksekisler/_ProperDestroyMixin",
    "dijit/form/Button", "dojox/widget/TitleGroup", "dijit/form/TextBox", "dijit/_base/manager",
    "dojox/image/ThumbnailPicker", "dijit/form/Form", "dijit/form/Select", "dijit/Editor",
    "dijit/form/DateTextBox", "dojox/form/Uploader", "dojox/form/uploader/FileList", "dijit/TitlePane", "yuksekisler/misc", "dojox/layout/TableContainer", "dojox/form/uploader/plugins/HTML5"],
    function (declare, _Widget, _Templated, dojo, ObjectStore, DataGrid, randomUuid, _ProperDestroyMixin, Button, TitleGroup, TextBox, dijit, ThumbnailPicker, Form, Select, Editor, DateTextBox, Uploader, FileList, TitlePane, misc) {

        var InspectionReportWidget = declare('yuksekisler.InspectionReportWidget', [TitlePane, _ProperDestroyMixin], {
            report:null,
            equipmentView:null,
            postCreate:function () {
                this.inherited(arguments);
                var title = this.report.inspector.name + ' on ' + new Date(this.report.inspectionDate) + ' as ' + this.report.status;
                this.set('title', title);
                var contentDiv = dojo.create('div', {style:'height:100%;width:100%;'});


                var reportDiv = dojo.create('div', {innerHTML:this.report.report});
                if (this.report.images.length > 0) {
                    var memoryStore = misc.prepareImageStore(this.report, 'images');
                    var imageGallery = new ThumbnailPicker({
                        isClickable:true,
                        isScrollable:false,
                        size:400
                    });
                    imageGallery.startup();
                    imageGallery.placeAt(contentDiv);
                    this.addInner(imageGallery);
                    dojo.subscribe(imageGallery.getClickTopicName(), this, this.lightboxShow);
                    imageGallery.setDataStore(memoryStore, { count:20 }, {
                        imageThumbAttr:"thumb",
                        imageLargeAttr:"large"
                    });
                }
                dojo.place(reportDiv, contentDiv);
                this.set('content', contentDiv);
                dojo.addClass(this.domNode, 'yuksekisler-widget');
            },
            lightboxShow:function (packet) {
                this.equipmentView.lightboxShow(packet);
            }
        });

        var InspectionReportFormWidget = declare([Form, _ProperDestroyMixin], {
            afterCancel:null,
            afterSave:null,
            equipmentId:null,
            postCreate:function () {
                this.enctype = 'multipart/form-data';
                this.method = 'post';
                var div = dojo.create('div');
                var select = new Select({
                    id:'stateSelect',
                    name:'stateSelect',
                    placeHolder:'select state',
                    options:[
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

                var editor = new Editor({
                    id:'report',
                    name:'report',
                    extraPlugins:['foreColor', 'hiliteColor', '|', 'createLink', 'fullscreen']
                }).placeAt(this.domNode);
                editor.startup();
                //this.addInner(editor);

                var save = new Button({
                    id:'save',
                    label:'Save'
                }).placeAt(div);
                save.startup();
                this.addInner(save);

                var cancel = new Button({
                    id:'cancel',
                    label:'Cancel'
                }).placeAt(div);
                cancel.startup();
                this.addInner(cancel);

                var inspectionDate = new DateTextBox({
                    id:'inspectionDate',
                    name:'inspectionDate',
                    value:new Date()
                }).placeAt(div);
                inspectionDate.startup();
                this.addInner(inspectionDate);


                this.uploader = new Uploader({
                    label:'Upload Images',
                    name:'uploadedfile',
                    id:'inspectionReportUploader',
                    "class":'fileUploader',
                    multiple:true
                }).placeAt(div);
                this.uploader.startup();
                this.addInner(this.uploader);

                var fileList = new FileList({
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
            onSubmit:function () {
                this.validate();
                if (this.uploader.getFileList().length > 0) {
                    this.uuid = dojox.uuid.generateRandomUuid();
                    this.uploader.upload({
                        uploadId:this.uuid
                    });
                } else
                    this.formCompleted();
            },
            formCompleted:function (e) {
                var data = dojo.formToObject(this.domNode);
                data.report = this.get('value').report;
                if (this.uuid)
                    data.filesUUID = this.uuid;
                dojo.xhrPost({
                    url:dojo.config.applicationBase + "/equipment/" + this.equipmentId + "/inspectionReport",
                    handleAs:"json",
                    load:dojo.hitch(this, function (data) {
                        misc.equipmentStore.evict(this.equipmentId);
                        this.afterSave(data)
                    }),
                    error:function (error) {
                        //show nice error function
                        alert("login failed" + error);
                    },
                    content:data
                });
            }
        });

        var EquipmentFormView = declare([_Widget, _Templated], {
            templateString:dojo["cache"]('yuksekisler.templates', 'equipment_form_template.html', {sanitize:true}),
            widgetsInTemplate:true,
            categoryStore:null,
            brandStore:null,
            equipmentStore:null,
            equipment:null,
            onSubmit:null,
            noHashChange:false,
            postCreate:function () {
                //this.uploader.onComplete = dojo.hitch(this, this.formCompleted);
                dojo.connect(this.uploader, 'onComplete', this, this.formCompleted);

                if (this.equipment) {
                    dojo.when(this.equipment, dojo.hitch(this, function (value) {

                        //stores does not work with integer id when used with select boxes add options individually
                        this.categoryStore.query({}).then(dojo.hitch(this, function (results) {
                            dojo.forEach(results, function (value) {
                                this.categorySelect.addOption({
                                    label:value.name,
                                    value:'' + value.id
                                });
                            }, this);
                            this.categorySelect.set('value', value.category.id);
                        }));

                        this.brandStore.query({}).then(dojo.hitch(this, function (results) {
                            dojo.forEach(results, function (value) {
                                this.brandSelect.addOption({
                                    label:value.name,
                                    value:'' + value.id
                                });
                            }, this);
                            this.brandSelect.set('value', value.category.id);
                        }));

                        this.productName.set('value', value.productName);
                        this.productCode.set('value', value.productCode);

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
            onSave:function () {
                if (this.form.validate()) {
                    //check whether we have files and upload them
                    if (this.uploader.getFileList().length > 0) {
                        this.uuid = randomUuid();
                        misc.loadingDialog.show();
                        this.uploader.upload({
                            uploadId:this.uuid
                        });
                    } else
                        this.formCompleted();
                }
            },
            formCompleted:function (uploadInfo) {
                //yuksekisler.app.loadingDialog.hide();
                if (uploadInfo && this.onImageUpload && !uploadInfo.operationFailed)
                    this.onImageUpload(uploadInfo);
                if (!uploadInfo || ( uploadInfo && !uploadInfo.operationFailed)) {
                    var postContent = dojo.formToObject(this.form.domNode);
                    if (this.uuid)
                        postContent.filesUUID = this.uuid;
                    var baseUrl = dojo.config.applicationBase + "/equipment/";
                    dojo.xhrPost({
                        url:this.equipmentID ? baseUrl + this.equipmentID : baseUrl,
                        handleAs:"json",
                        load:dojo.hitch(this, function (data) {
                            this.equipmentStore.evict(this.equipmentID);
                            if (this.onSubmit)
                                this.onSubmit(this.equipmentID);
                            if (!this.noHashChange)
                                dojo.hash('equipments');
                            misc.loadingDialog.hide();
                        }),
                        content:postContent
                    });
                }
            }
        });

        var EquipmentListView = declare([_Widget, _Templated, _ProperDestroyMixin], {
            templateString:dojo["cache"]('yuksekisler.templates', 'equipment_list_view_template.html', {sanitize:true}),
            widgetsInTemplate:true,
            dataStore:null,
            gutters:false,
            postCreate:function () {
                this.grid = new DataGrid({
                    query:{},
                    selectionMode:'single',
                    id:'equipmentListGrid',
                    rowCount:10,
                    store:new ObjectStore({objectStore:this.dataStore, labelProperty:'productName'}),
                    structure:[
                        {name:'Product Name', field:'productName', width:'auto'},
                        {name:'Product Code', field:'productCode', width:'auto'},
                        {name:'Image', get:this.getImagePath, width:'48px', height:'auto', formatter:this.formatImage},
                        {name:'Brand', get:this.getBrandName, width:'auto'},
                        {name:'Category', get:this.getCategoryName, width:'auto'},
                        {name:'Inspection Date', get:this.stockEntrance, formatter:this.formatDate, width:'auto'}
                    ],
                    onRowContextMenu:dojo.hitch(this, misc.onRowContextMenu)
                });
                this.gridContainer.set('content', this.grid);
                this.grid.startup();
                dojo.connect(this.searchBox, 'onKeyUp', this, this.textBoxChanged);
                dojo.connect(this.grid, 'onRowClick', this, this.onRowClick);
                this.newEquipment = new Button({
                    label:'New Equipment',
                    onClick:function () {
                        dojo.hash('newequipment');
                    }
                }).placeAt(this.subContainer);
                this.addInner(this.newEquipment);
                this.newEquipment.startup();
                this.inherited(arguments);
                dojo.addClass(this.domNode, 'yuksekisler-widget');
            },
            getBrandName:function (colIndex, item) {
                if (item)
                    return item.brand.name;
            },
            getImagePath:function (colIndex, item) {
                if (item) {
                    return item.images[0];
                }
            },
            getCategoryName:function (colIndex, item) {
                if (item)
                    return item.category.name;
            },
            stockEntrance:function (colIndex, item) {
                if (item && item.inspectionReports.length > 0)
                    return item.inspectionReports[item.inspectionReports.length - 1].inspectionDate;
            },
            formatDate:function (value) {
                if (value)
                    return dojo.date.locale.format(new Date(value), {
                        datePattern:'dd/MM/yyyy',
                        formatLength:'short',
                        selector:'date'
                    });
                return "";
            },
            formatImage:function (value) {
                if (value)
                    return "<img width='48px' height='48px' src='" + (dojo.config.applicationBase + '/file/image/' + value.id) + "/thumbnail'/>";
                else
                    return "<img width='48px' height='48px' src='/yuksekisler/resources/images/no-image.jpg'/>";
            },
            textBoxChanged:function (e) {
                if (this.timerId) {
                    clearTimeout(this.timerId);
                }
                this.timerId = setTimeout(dojo.hitch(this, 'updateGrid'), 400);
            },
            onRowClick:function (e) {
                var item = this.grid.getItem(e.rowIndex);
                dojo.hash('equipment/' + item.id);
            },
            updateGrid:function () {
                if (this.searchBox.get('value') == '')
                    this.grid.setQuery({});
                else
                    this.grid.setQuery({'searchString':this.searchBox.get('value')});
            }
        });

        var EquipmentView = declare([_Widget, _Templated, _ProperDestroyMixin], {
            templateString:dojo["cache"]('yuksekisler.templates', 'equipment_view_template.html', {sanitize:true}),
            widgetsInTemplate:true,
            categoryStore:null,
            brandStore:null,
            equipmentStore:null,
            equipment:null,
            gutters:false,
            design:'headline',
            postCreate:function () {
                this.inherited(arguments);

                var equipmentChain = dojo.when(this.equipment, dojo.hitch(this, this.prepareImageContent));
                equipmentChain = dojo.when(equipmentChain, dojo.hitch(this, this.prepareInspectionReports));

                this.editEquipment = new EquipmentFormView({
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
            prepareImageContent:function (value) {
                if (value.images.length > 0) {
                    /*var imageGallery = new dojox.image.Gallery({
                     imageWidth:'500',
                     imageHeight:'300'
                     });*/
                    this.prepareImageGalery();
                    this.initImageStore(value);
                } else {
                    var img = dojo.create('img', {"class":'no-image', src:'/yuksekisler/resources/images/no-image.jpg'});
                    this.imageContent.set('content', img);
                }

                //this.addInner(this.lightbox);
                return value;
            },
            prepareInspectionReports:function (value) {
                var container = dojo.create('div');
                var group = new TitleGroup({id:'reportsGroup'}).placeAt(container);
                for (var x in value.inspectionReports) {
                    var inspectionReport = value.inspectionReports[x];
                    var inspectionReportWidget = new InspectionReportWidget({
                        report:inspectionReport,
                        equipmentView:this,
                        open:x == (value.inspectionReports.length - 1)
                    }).placeAt(group);
                }
                this.addInner(group);

                var placeHolderTextBox = new TextBox({
                    id:'inspectionReportPlaceHolder',
                    placeHolder:'new inspection report',
                    "class":'newPlaceHolder',
                    onFocus:dojo.hitch(this, function () {
                        dojo.fadeOut({
                            node:dojo.byId('inspectionReportView'),
                            duration:500,
                            onEnd:dojo.hitch(this, function () {
                                dojo.style(dojo.byId('inspectionReportView'), "display", "none");
                                dojo.style('inspectionReportFormView', 'opacity', 0);
                                var reportForm = new InspectionReportFormWidget({
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
                    href:packet.largeUrl,
                    title:packet.title
                });

            },
            reportSaved:function (newReport) {
                dojo.fadeOut({
                    node:'inspectionReportFormView',
                    duration:500,
                    onEnd:function () {
                        dijit.byId('reportForm').destroy();
                        var inspectionReportView = dojo.byId('inspectionReportView');
                        dojo.style(inspectionReportView, "display", "block");
                        dojo.fadeIn({
                            node:inspectionReportView,
                            duration:500
                        }).play();
                        var group = dijit.byId('reportsGroup');
                        var inspectionReportWidget = new InspectionReportWidget({
                            report:newReport,
                            equipmentView:this
                        }).placeAt(group);
                        group.selectChild(inspectionReportWidget);
                    }
                }).play();
            },
            reportCanceled:function () {
                var reportForm = dijit.byId('reportForm');
                dojo.fadeOut({
                    node:'inspectionReportFormView',
                    duration:500,
                    onEnd:function () {
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
            equipmentChanged:function (equipmentID) {
                dojo.xhrGet({
                    url:dojo.config.applicationBase + '/equipment/' + equipmentID,
                    handleAs:'json',
                    load:dojo.hitch(this, function (value) {
                        if (!this.imageGallery)
                            this.prepareImageGalery();
                        this.initImageStore(value);
                    })
                });
            },
            initImageStore:function (value) {
                var memoryStore = misc.prepareImageStore(value, 'images');
                this.imageGallery.setDataStore(memoryStore, { count:20 }, {
                    imageThumbAttr:"thumb",
                    imageLargeAttr:"large"
                });
            },
            prepareImageGalery:function () {
                this.imageGallery = new ThumbnailPicker({
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
            onImageUpload:function (uploadInfo) {
                //var infoWidget = new yuksekisler.UploaderSuccessWidget({
                //    uploadInfo:uploadInfo
                //}).placeAt(this.domNode, 'first');
                if (uploadInfo.length == 1 && uploadInfo.operationFailed)
                    dojo.publish("globalMessageTopic", [
                        {
                            message:'File upload failed',
                            type:"warning"
                        }
                    ]);
                else {
                    var domNode = this.domNode;
                    dojo.forEach(uploadInfo, function (file) {
                        var message = file.file + (file.result == 'success' ? ' saved' : ' failed');
                        dojo.publish("globalMessageTopic", [
                            {
                                message:message,
                                type:file.result != 'success' ? "warning" : "message"
                            }
                        ]);
                    });
                }
            }
        });

        return {
            EquipmentView:EquipmentView,
            EquipmentListView:EquipmentListView,
            EquipmentFormView:EquipmentFormView
        };
    });