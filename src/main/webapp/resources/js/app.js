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
    'definitions'
], function ($, _, Backbone,equipment,definitions) {
    var Workspace = Backbone.Router.extend({

        routes: {
            "equipments":                 "equipments",    // #items
            "equipment/:equipmentNumber":        "equipment"  // #item/{itemnumber}
        },
        equipments:function(){
            console.log('items');
            var e = new equipment.EquipmentsView({
                categories:definitions.Categories,
                brands:definitions.Brands
            }).render();
            $('#content').append(e.el);
        },
        equipment:function(itemNumber){
            console.log(itemNumber);
        }
    });

    var Menu = Backbone.View.extend({
        events:{
            "click .nav li" : "change"
        },
        change:function(e){
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

        $.when(definitions.Brands.fetch(),definitions.Categories.fetch()).then(function(){
            var workSpace=new Workspace();
            var currentState=Backbone.history.start({pushState: true, root: "/yuksekisler/"});
            if(!currentState)
                workSpace.navigate("equipments",true);
            else{
                console.log(currentState);
            }
            menu.setElement($('body .navbar-inner'));
        });
    }

    return {
        initialize:initialize
    };
});
