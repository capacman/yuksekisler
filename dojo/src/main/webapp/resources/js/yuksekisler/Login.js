/**
 * Created by JetBrains WebStorm.
 * User: capacman
 * Date: 9/14/11
 * Time: 1:11 PM
 * To change this template use File | Settings | File Templates.
 */
// custom.AuthorWidget
dojo.provide("yuksekisler.Login");

// Bring in what we need
dojo.require("dijit._Widget");
dojo.require("dijit._Templated");


dojo.declare("yuksekisler.Login", [dijit._Widget, dijit._Templated], {
    templateString:
        dojo.cache("yuksekisler.Login", dojo.moduleUrl("yuksekisler", "../../templates/login_template.html")),
    widgetsInTemplate: true,
    login:function() {
        dojo.xhrPost({
            url: dojo.config.applicationBase+"/j_spring_security_check",
            handleAs: "text",
            load: function(data) {
                dojo.publish(yuksekisler.app.events.loginsuccsess);
            },
            error: function(error) {
                //show nice error function
                alert("login failed" + error);
            },
            content:this.dojoForm.get('value')
        });
    }
});