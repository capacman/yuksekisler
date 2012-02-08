/**
 * Created by JetBrains WebStorm.
 * User: capacman
 * Date: 2/6/12
 * Time: 8:37 PM
 * To change this template use File | Settings | File Templates.
 */
define([
    'jquerybind',
    'underscore',
    'Backbone',
    'equipment',
    'definitions'
], function ($, _, Backbone, equipment, definitions) {

    var eventBus = {};
    _.extend(eventBus, Backbone.Events);

    var Workspace = Backbone.Router.extend({
        routes:{
            "equipments":"equipments", // #items
            "equipment/:equipmentNumber":"equipment", // #item/{itemnumber}
            "newequipment":"newEquipment"
        },
        equipments:function () {
            console.log('router equipments');
            eventBus.trigger('equipments');
        },
        equipment:function (itemNumber) {
            console.log('router equipment number');
            eventBus.trigger('equipment',itemNumber);
        },
        newEquipment:function () {
            console.log('router newequipment');
            eventBus.trigger('newequipment');
        }
    });

    var workSpace=new Workspace();
    return {
        workSpace:workSpace,
        eventBus:eventBus
    };
});