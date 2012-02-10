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
    return {
        eventBus:eventBus
    };
});