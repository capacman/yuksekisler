/**
 * Created by JetBrains WebStorm.
 * User: capacman
 * Date: 10/15/11
 * Time: 8:19 PM
 * To change this template use File | Settings | File Templates.
 */
dojo.provide('yuksekisler.EmployeeListWidget');

dojo.declare('yuksekisler.EmployeeListWidget', [dijit._Widget], {
    employeeStore:null,
    buildRendering:function() {
        this.domNode = dojo.create("div", {
            class:'employeeListContainer'
        });
        this.inherited(arguments);
    },
    postCreate:function() {
        this.employeeStore.query({}).then(dojo.hitch(this, function(results) {
            for (var x in results) {
                this.addEmployee(results[x]);
            }
        }));
        dojo.addClass(this.domNode, 'yuksekisler-widget');
        this.inherited(arguments);
    },
    addEmployee:function(employee) {
        new yuksekisler.EmployeeWidget({
            employee:employee
        }).placeAt(this.domNode);
    }
});