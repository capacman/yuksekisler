/**
 * Created with JetBrains WebStorm.
 * User: capacman
 * Date: 11/3/12
 * Time: 2:17 PM
 * To change this template use File | Settings | File Templates.
 */
/*global angular */
/*
 jQuery UI Datepicker plugin wrapper

 @param [ui-date] {object} Options to pass to $.fn.datepicker() merged onto ui.config
 */

angular.module('custom.directives',[])
    .directive('uiDate',function () {
    'use strict';
    var options;
    options = {};
    return {
        require:'?ngModel',
        link:function (scope, element, attrs, controller) {
            var getOptions = function () {
                return angular.extend({}, scope.$eval(attrs.uiDate));
            };
            var initDateWidget = function () {
                var opts = getOptions();
                var jqElement=$(element);
                // If we have a controller (i.e. ngModelController) then wire it up
                if (controller) {
                    var updateModel = function () {
                        scope.$apply(function () {
                            var date = jqElement.datepicker("getDate");
                            jqElement.datepicker("setDate", element.val());
                            controller.$setViewValue(date);
                        });
                    };
                    if (opts.changeDate) {
                        // Caller has specified onSelect, so call this as well as updating the model
                        var userHandler = opts.changeDate;
                        opts.changeDate = function (value, picker) {
                            updateModel();
                            return userHandler(value, picker);
                        };
                    } else {
                        // No onSelect already specified so just update the model
                        opts.changeDate = updateModel;
                    }
                    // In case the user changes the text directly in the input box
                    element.bind('change', updateModel);

                    // Update the date picker when the model changes
                    controller.$render = function () {
                        var date = controller.$viewValue;
                        if ( angular.isDefined(date) && date !== null && !angular.isDate(date) ) {
                            throw new Error('ng-Model value must be a Date object - currently it is a ' + typeof date + ' - use ui-date-format to convert it from a string');
                        }
                        if(date)
                            jqElement.datepicker("setDate", date);
                    };
                }
                // If we don't destroy the old one it doesn't update properly when the config changes
                //$(element).datepicker();
                // Create the new datepicker widget
                jqElement.datepicker(opts).on('changeDate',opts.changeDate);
                // Force a render to override whatever is in the input text box
                console.log("osman");
                controller.$render();
            };
            // Watch for changes to the directives options
            scope.$watch(getOptions, initDateWidget, true);
        }
    };
})

    .directive('uiDateFormat', [function() {
    var directive = {
        require:'ngModel',
        link: function(scope, element, attrs, modelCtrl) {
            if ( attrs.uiDateFormat === '' ) {
                // Default to ISO formatting
                modelCtrl.$formatters.push(function(value) {
                    if (angular.isString(value) ) {
                        return new Date(value);
                    }
                    return null;
                });
                modelCtrl.$parsers.push(function(value){
                    if (angular.isString(value) ) {
                        return value.toISOString();
                    }
                    return null;
                });
            } else {
                var format = attrs.uiDateFormat;
                // Use the datepicker with the attribute value as the format string to convert to and from a string
                modelCtrl.$formatters.push(function(value) {
                    if (angular.isString(value) ) {
                        return $.datepicker.parseDate(format, value);
                    }
                    return null;
                });
                modelCtrl.$parsers.push(function(value){
                    if (angular.isString(value) ) {
                        return $.datepicker.formatDate(format, value);
                    }
                    return null;
                });
            }
        }
    };
    return directive;
}]);