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
        var employeeDefinitionContainer = dojo.create("div", {class:'employeeDefinitionContainer'}, this.domNode);
        this.employeeDefinition = dojo.create('p', {class:'employeeDefinition'}, employeeDefinitionContainer);
        this.employeeStartDate = dojo.create('p', {class:'employeeStartDate'}, employeeDefinitionContainer);
        var employeeImageContainer = dojo.create("div", {class:'employeeImageContainer'}, this.domNode);
        var employeeImageWrapper = dojo.create("div", {class:'employeeImageWrapper'}, employeeImageContainer);
        this.employeeImage = dojo.create('img', {class:'employeeImage'}, employeeImageWrapper);
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