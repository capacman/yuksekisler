/**
 * Created by JetBrains WebStorm.
 * User: mint
 * Date: 1/26/12
 * Time: 7:10 PM
 * To change this template use File | Settings | File Templates.
 */
define([
    'jquerybind',
    'underscore',
    'Backbone',
    'equipment',
    'definitions',
    'eventroute'
], function ($, _, Backbone, equipment, definitions, eventroute) {
    var lastView = undefined;
    var clearLastView = function () {
        if (lastView) {
            lastView.remove();
            lastView = undefined;
        }
    };
    var applicationController = {
        newEquipment:function () {
            clearLastView();
            var e = new equipment.EquipmentForm({
                categories:definitions.Categories,
                brands:definitions.Brands
            }).render();
            $('#content').append(e.el);
            lastView = e;
            eventroute.workSpace.navigate('newequipment');
        },
        equipments:function () {
            clearLastView();
            var e = new equipment.EquipmentsView().render();
            $('#content').append(e.el);
            lastView = e;
            eventroute.workSpace.navigate('equipments');
        },
        equipment:function () {

        }
    };
    _.bindAll(applicationController);
    eventroute.eventBus.on('equipments', applicationController.equipments);
    eventroute.eventBus.on('equipment', applicationController.equipment);
    eventroute.eventBus.on('newequipment', applicationController.newEquipment);

    var Menu = Backbone.View.extend({
        events:{
            "click .nav li":"change"
        },
        change:function (e) {
            console.log(e);
            this.$('.nav li.active').toggleClass('active');
            $(e.currentTarget).toggleClass('active');
        }
    });
    var menu = new Menu();

    var initialize = function () {
        // Pass in our Router module and call it's initialize function
        console.log('initialized');
        $ ? console.log($().jquery) : console.log('jquery not loaded');
        _ ? console.log(_.VERSION) : console.log('underscore not loaded');
        Backbone ? console.log(Backbone.VERSION) : console.log('backbone not loaded');

        $.when(definitions.Brands.fetch(), definitions.Categories.fetch()).then(function () {

            var currentState = Backbone.history.start({pushState:true});
            if (!currentState)
                eventroute.eventBus.trigger('equipments');
            else {
                console.log(currentState);
            }
            menu.setElement($('body .navbar-inner'));
        });
    };

    return {
        initialize:initialize
    };
});
