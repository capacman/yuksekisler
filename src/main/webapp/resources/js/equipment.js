/**
 * Created by JetBrains WebStorm.
 * User: mint
 * Date: 1/31/12
 * Time: 2:40 PM
 * To change this template use File | Settings | File Templates.
 */
define(['jquerybind', 'underscore', 'Backbone', 'eventroute', 'Slick',
    'text!../templates/equipments.html',
    'text!../templates/equipmentform.html'],
    function ($, _, Backbone, eventroute, Grid, equipmentstemplate, equipmentformtemplate) {
        var Equipment = Backbone.Model.extend({

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
                        console.log(cell);
                    }, this));
                }, this));
                return this;
            },
            newEquipment:function () {
                eventroute.eventBus.trigger('newequipment');
            }
        });


        return {
            EquipmentsView:EquipmentsView,
            EquipmentForm:EquipmentForm
        };
    });