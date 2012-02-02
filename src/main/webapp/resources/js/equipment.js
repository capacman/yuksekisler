/**
 * Created by JetBrains WebStorm.
 * User: mint
 * Date: 1/31/12
 * Time: 2:40 PM
 * To change this template use File | Settings | File Templates.
 */
define(['jquerybind', 'underscore', 'Backbone',
    'text!../templates/equipments.html',
    'text!../templates/equipmentform.html'],
    function ($, _, Backbone, equipmentstemplate, equipmentformtemplate) {
        var EquipmentForm = Backbone.View.extend({
            tagName:'div',
            template:_.template(equipmentformtemplate),
            render:function () {
                $(this.el).html(this.template({
                    categories:this.options.categories,
                    brands:this.options.brands
                }));
                this.$('#stockEntrance').datepicker();
                this.$('#bestBefore').datepicker();
                this.$('#production').datepicker();
                return this;
            }
        });
        var EquipmentsView = Backbone.View.extend({
            tagName:'div',
            className:'row',
            template:_.template(equipmentstemplate),
            events:{
                'click #newEquipmentButton':'newEquipment'
            },
            initialize:function () {

            },
            render:function () {
                $(this.el).html(this.template({}));
                var eqForm = new EquipmentForm({
                    categories:this.options.categories,
                    brands:this.options.brands
                }).render();
                this.$("#dialog-form").append(eqForm.el).dialog({
                    autoOpen:false,
                    height:420,
                    width:420,
                    modal:true,
                    buttons:{
                        "Create an equipment":function () {
                            console.log('create clicked');
                        },
                        Cancel:function () {
                            $(this).dialog("close");
                        }
                    }
                });
                this.$('#newEquipmentButton').button();
                return this;
            },
            newEquipment:function () {
                $("#dialog-form").dialog("open");
            }
        });


        return {
            EquipmentsView:EquipmentsView
        }
    });