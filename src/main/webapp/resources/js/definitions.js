/**
 * Created by JetBrains WebStorm.
 * User: mint
 * Date: 1/31/12
 * Time: 10:09 PM
 * To change this template use File | Settings | File Templates.
 */
define(['jquerybind', 'underscore', 'Backbone'], function ($, _, Backbone) {
    var Brand = Backbone.Model.extend({
    });

    var Category = Backbone.Model.extend({

    });

    var Brands = Backbone.Collection.extend({
        model:Brand,
        url:appconfig.context+'/brand'
    });

    var Categories = Backbone.Collection.extend({
        model:Category,
        url:appconfig.context+'/category'
    });

    return {
        Brands:new Brands(),
        Categories:new Categories()
    };
});