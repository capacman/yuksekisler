/**
 * Created by JetBrains WebStorm.
 * User: capacman
 * Date: 9/14/11
 * Time: 1:11 PM
 * To change this template use File | Settings | File Templates.
 */
// custom.AuthorWidget
dojo.provide("yuksekisler.Login");

dojo.require("dijit.form.Form");


dojo.declare("yuksekisler.Login", [dijit.form.Form], {
    postCreate:function() {
        var layout = new dojox.layout.TableContainer({cols:1});
        var username = new dijit.form.ValidationTextBox({
            id:'j_username',
            name:'j_username',
            label:'Username',
            autoComplete:'on'
        });
        layout.addChild(username);
        var password = new dijit.form.ValidationTextBox({
            id:'j_password',
            name:'j_password',
            type:'password',
            label:'Password',
            autoComplete:'on'
        });
        layout.addChild(password);

        var submit = new dijit.form.Button({
            label:'Login'
        });

        dojo.connect(submit, 'onClick', this, this.onSubmit);
        this.domNode.appendChild(layout.domNode);
        layout.startup();
        this.domNode.appendChild(submit.domNode);
        submit.startup();
        dojo.attr(dijit.byId('j_username').textbox, "autocomplete", "on");
        dojo.attr(dijit.byId('j_password').textbox, "autocomplete", "on");
        this.inherited(arguments);
    },
    onSubmit:function() {
        dojo.xhrPost({
            url: dojo.config.applicationBase + "/j_spring_security_check",
            handleAs: "text",
            load: function(data) {
                dojo.publish(yuksekisler.app.events.loginsuccsess);
            },
            error: function(error) {
                //show nice error function
                alert("login failed" + error);
            },
            content:this.get('value')
        });
    }
});