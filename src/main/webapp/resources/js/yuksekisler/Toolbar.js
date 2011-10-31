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
        templateString: dojo["cache"]('yuksekisler.templates', "toolbar_template.html", {sanitize: true}),
        widgetsInTemplate: true,
        postCreate:function() {
            this.inherited(arguments);
            dojo.connect(this.equipments, 'onclick', this, this.onEquipments);
            dojo.connect(this.works, 'click', this, this.onWorks);
            dojo.connect(this.employees, 'onclick', this, this.onEmployees);
            dojo.connect(this.definitions, 'onclick', this, this.onDefinitions);
            dojo.connect(this.logout, 'onclick', this, this.onLogout);
        },
        onLogout:function(e) {
            this.logoutForm.submit();
        },
        onEquipments:function(e) {
            this.makeActive(this.equipments);
            dojo.hash('equipments');
        },
        onWorks:function(e) {
            this.makeActive(this.works);
            dojo.hash('works');
        },
        onDefinitions:function(e) {
            this.makeActive(this.definitions);
            dojo.hash('definitions');
        },
        onEmployees:function(e) {
            this.makeActive(this.employees);
            dojo.hash('employees');
        },
        makeActive:function(newActive) {
            if (this.selected)
                dojo.removeClass(this.selected, 'active');
            this.selected = newActive;
            dojo.addClass(this.selected, 'active');
        }
    }
);