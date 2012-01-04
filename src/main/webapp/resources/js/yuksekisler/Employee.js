/**
 * Created by JetBrains WebStorm.
 * User: capacman
 * Date: 10/15/11
 * Time: 8:19 PM
 * To change this template use File | Settings | File Templates.
 */

define(["dojo/_base/declare", "dijit/_Widget", "dijit/_Templated", "dojo"], function (declare, _Widget, _Templated, dojo) {
    var EmployeeWidget = declare([_Widget], {
        employee:null,
        buildRendering:function () {
            // create the DOM for this widget
            this.domNode = dojo.create("div", {
                "class":'simpleWidgetContainer employeeWidget'
            });
            this.employeeImage = dojo.create('img', {"class":'simpleWidgetImage', align:'right'}, this.domNode);
            this.employeeDefinition = dojo.create('div', {"class":'simpleWidgetDefinition'}, this.domNode);
            this.employeeStartDate = dojo.create('div', {"class":'simpleWidgetDefinition'}, this.domNode);

            this.inherited(arguments);
        },
        postCreate:function () {
            dojo.attr(this.employeeDefinition, {
                innerHTML:'<b>' + this.employee.title.name + '</b>' + ' ' + '<i>' + this.employee.name + '</i>'
            });

            dojo.attr(this.employeeStartDate, {
                innerHTML:'started on ' + dojo.date.locale.format(new Date(this.employee.startDate), {selector:"date", formatLength:"short"})
            });

            dojo.attr(this.employeeImage, {
                src:this.employee.image ? dojo.config.applicationBase + '/file/image/' + this.employee.image.id + '/thumbnail' : dojo.config.applicationBase + '/resources/images/noperson.jpg'
            });
            this.inherited(arguments);
        }
    });
    var EmployeeListWidget = declare([_Widget], {
        employeeStore:null,
        buildRendering:function () {
            this.domNode = dojo.create("div", {
                "class":'employeeListContainer'
            });
            this.inherited(arguments);
        },
        postCreate:function () {
            this.employeeStore.query({}).then(dojo.hitch(this, function (results) {
                for (var x in results) {
                    this.addEmployee(results[x]);
                }
            }));
            dojo.addClass(this.domNode, 'yuksekisler-widget');
            this.inherited(arguments);
        },
        addEmployee:function (employee) {
            new EmployeeWidget({
                employee:employee
            }).placeAt(this.domNode);
        }
    });
    return {
        EmployeeListWidget:EmployeeListWidget
    }
});