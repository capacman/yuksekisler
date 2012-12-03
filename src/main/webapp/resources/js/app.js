/**
 * Created with JetBrains WebStorm.
 * User: capacman
 * Date: 10/13/12
 * Time: 2:00 PM
 * To change this template use File | Settings | File Templates.
 */
angular.module('yuksekisler', ['yuksekislerServices','custom.directives']).
    config(['$routeProvider', function ($routeProvider) {
    $routeProvider.
        when('/equipments', {templateUrl:appconfig.context + '/resources/templates/equipments.html', controller:EquipmentListCtrl}).
        when('/equipments/new', {templateUrl:appconfig.context + '/resources/templates/equipmentform.html', controller:EquipmentFormCtrl}).
        when('/equipments/:equipmentID', {templateUrl:appconfig.context + '/resources/templates/equipmentview.html', controller:EquipmentDetailCtrl}).
        otherwise({redirectTo:'/equipments'});
}]).
    filter('paging', function () {
        return function (array, uppercase) {
            console.log(array);
            console.log(uppercase);
            return array;
        }
    });