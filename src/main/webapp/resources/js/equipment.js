/**
 * Created by JetBrains WebStorm.
 * User: mint
 * Date: 1/31/12
 * Time: 2:40 PM
 * To change this template use File | Settings | File Templates.
 */
define(['jquerybind', 'underscore', 'Backbone', 'eventroute', 'Slick', 'fileupload',
    'text!../templates/equipments.html',
    'text!../templates/equipmentform.html',
    'text!../templates/equipmentview.html',
    'text!../templates/inspectionreportform.html'],
    function ($, _, Backbone, eventroute, Grid, fileupload, equipmentstemplate, equipmentformtemplate, equipmentviewtemplate,inspectionreportformtemplate) {
        function uuid() {
            var uuid = "", i, random;
            for (i = 0; i < 32; i++) {
                random = Math.random() * 16 | 0;

                if (i == 8 || i == 12 || i == 16 || i == 20) {
                    uuid += "-"
                }
                uuid += (i == 12 ? 4 : (i == 16 ? (random & 3 | 8) : random)).toString(16);
            }
            return uuid;
        }

        var Equipment = Backbone.Model.extend({
            urlRoot:'/equipment'
        });
        var EquipmentsCollection = Backbone.Collection.extend({
            model:Equipment,
            url:appconfig.context + '/equipment',
            getLength:function () {
                return this.length;
            },
            getItem:function (i) {
                return this.at(i);
            }
        });
        var equipmentColumns = [
            {id:"productName", name:"Product Name", field:"productName", sortable:'true', width:120},
            {id:"productCode", name:"Product Code", field:"productCode", sortable:'true', width:120},
            {id:"brand", name:"Brand", field:"brand", sortable:'true', width:120},
            {id:"category", name:"Category", field:"category", sortable:'true', width:120}
        ];
        var options = {
            enableCellNavigation:true,
            enableColumnReorder:false,
            dataItemColumnValueExtractor:function (item, columnDef) {
                if (columnDef.field == 'brand' || columnDef.field == 'category')
                    return item.get(columnDef.field).name;
                else
                    return item.get(columnDef.field);
            },
            fullWidthRows:true
        };
        var EquipmentForm = Backbone.View.extend({
            tagName:'div',
            template:_.template(equipmentformtemplate),
            events:{
                'click #submitButton':'submit',
                'click #cancelButton':'cancel'
            },
            render:function () {
                $(this.el).html(this.template({
                    categories:this.options.categories,
                    brands:this.options.brands
                }));

                this.$('#equipmentForm').ajaxForm();
                this.$('#stockEntrance').datepicker();
                this.$('#bestBefore').datepicker();
                this.$('#production').datepicker();
                this.$('#submitButton').button();
                this.$('#cancelButton').button();
                this.$('#equipmentForm').validationEngine('attach');
                return this;
            },
            cancel:function () {
                eventroute.eventBus.trigger('equipments');
            },
            submit:function () {

            }
            //TODO: we should remove some view garbages
        });
        var EquipmentsView = Backbone.View.extend({
            tagName:'div',
            className:'row',
            template:_.template(equipmentstemplate),
            events:{
                'click #newEquipmentButton':'newEquipment'
            },
            initialize:function () {
                this.collection = new EquipmentsCollection();
            },
            render:function () {
                $(this.el).html(this.template({}));
                this.$('#newEquipmentButton').button();
                this.collection.fetch().then(_.bind(function () {
                    this.grid = new Slick.Grid("#equipmentGrid", this.collection, equipmentColumns, options);
                    this.grid.onClick.subscribe(_.bind(function (e) {
                        var cell = this.grid.getCellFromEvent(e);
                        eventroute.eventBus.trigger('equipment', this.collection.getItem(cell.row));
                    }, this));
                }, this));
                return this;
            },
            newEquipment:function () {
                eventroute.eventBus.trigger('newequipment');
            }
        });

        var EquipmentView = Backbone.View.extend({
            tagName:'div',
            template:_.template(equipmentviewtemplate),
            events:{
                'click #addFileButton':'addFiles',
                'click #addInspectionReport':'addInspectionReport'
            },
            render:function () {
                $(this.el).html(this.template({
                    equipment:this.options.equipment
                }));
                this.$('#fileUploadButton').button();
                this.$('#selectFiles').button();
                this.$('#addInspectionReport').button();
                var assignedUuid=uuid();
                this.$('#fileInput').fileupload({
                    dataType:'json',
                    url:'/file/image/upload',
                    formData:function () {
                        return [
                            {
                                name:'uploadId',
                                value:assignedUuid
                            }
                        ];
                    },
                    done:function (e, data) {
                        $("#uploadProgressbar").hide();
                        if(data.result.operationFailed){
                            $('#uploadFailedAlert').show();
                        }else{
                            $('#uploadSuccessAlert').show();
                        }
                    },
                    singleFileUploads:false
                });
                this.$('#fileInput').bind("fileuploadprogressall", function (e, data) {
                    $("#uploadProgressbar").show();
                    $("#uploadProgressbar").progressbar({
                        value:parseInt(data.loaded / data.total * 100, 10)
                    });
                });
                return this;
            },
            addFiles:function () {
                this.$('#fileInput').click();
            },
            addInspectionReport:function(){
                eventroute.eventBus.trigger('addinspection',this.options.equipment);
            }
        });

        var InspectionReportForm=Backbone.View.extend({
            tagName:'div',
            template:_.template(inspectionreportformtemplate),
            events:{

            },
            render:function(){
                $(this.el).html(this.template({
                    equipment:this.options.equipment
                }));
                this.$('#inspectionReportForm').ajaxForm();
                this.$('#inspectionDate').datepicker();
                this.$('#inspectionReportForm').validationEngine('attach');
                return this;
            },
            renderEditor:function(){
                this.$('#textEditor').wysiwyg();
            }
        });

        return {
            EquipmentsView:EquipmentsView,
            EquipmentForm:EquipmentForm,
            EquipmentView:EquipmentView,
            Equipment:Equipment,
            InspectionReportForm:InspectionReportForm
        };
    });