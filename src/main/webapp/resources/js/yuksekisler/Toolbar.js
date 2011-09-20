/**
 * Created by JetBrains WebStorm.
 * User: capacman
 * Date: 9/14/11
 * Time: 9:51 PM
 * To change this template use File | Settings | File Templates.
 */
dojo.provide("yuksekisler.Toolbar");

// Bring in what we need
dojo.require("dijit._Widget");
dojo.require("dijit._Templated");


dojo.declare("yuksekisler.Toolbar", [dijit._Widget, dijit._Templated], {
        templateString:
            dojo.cache("yuksekisler.Toolbar", dojo.moduleUrl("yuksekisler", "../../templates/toolbar_template.html")),
        widgetsInTemplate: true,
        hovered:function(e){
            this.popupMenu.focusFirstChild();
        }
    }
);