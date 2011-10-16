/**
 * Created by JetBrains WebStorm.
 * User: capacman
 * Date: 10/15/11
 * Time: 6:22 PM
 * To change this template use File | Settings | File Templates.
 */
dojo.provide('yuksekisler.EmployeeWidget');

dojo.declare('yuksekisler.EmployeeWidget', [dijit._Widget], {
    employee:null,
    buildRendering: function() {
        // create the DOM for this widget
        this.domNode = dojo.create("div", {
            class:'employeeContainer'
        });
        this.employeeImage = dojo.create('img', {class:'employeeImage',align:'right'}, this.domNode);
        this.employeeDefinition = dojo.create('div', {class:'employeeDefinition'}, this.domNode);
        this.employeeStartDate = dojo.create('div', {class:'employeeStartDate'}, this.domNode);

        this.inherited(arguments);
    },
    postCreate:function() {
        dojo.attr(this.employeeDefinition, {
            innerHTML:'<b>' + this.employee.title.name + '</b>' + ' ' + '<i>' + this.employee.name + '</i>'
        });

        dojo.attr(this.employeeStartDate, {
            innerHTML:'started on ' + dojo.date.locale.format(new Date(this.employee.startDate), {selector: "date", formatLength: "short"})
        });

        dojo.attr(this.employeeImage, {
            src:this.employee.image ? dojo.config.applicationBase + '/file/image/' + this.employee.image.id + '/thumbnail' : dojo.config.applicationBase + '/resources/images/noperson.jpg'
        });
        this.inherited(arguments);
    }
});