/**
 * Created with JetBrains WebStorm.
 * User: capacman
 * Date: 10/13/12
 * Time: 2:22 PM
 * To change this template use File | Settings | File Templates.
 */

angular.module('yuksekislerServices', ['ngResource']).
    factory('EquipmentService',function ($resource) {
        return $resource(appconfig.context + '/equipment/:equipmentID', {}, {
            query:{method:'GET', params:{equipmentID:'equipments'}, isArray:true}
        });
    }).
    factory('CategoryService',function ($resource) {
        return $resource(appconfig.context + '/category/:categoryID', {}, {
            query:{method:'GET', params:{categoryID:''}, isArray:true}
        });
    }).
    factory('BrandService',function ($resource) {
        return $resource(appconfig.context + '/brand/:brandID', {}, {
            query:{method:'GET', params:{brandID:''}, isArray:true}
        });
    }).
    factory('ApplicationService', function ($location) {
        return {
            openEquipment:function (equipmentID) {
                $location.path("/equipments/" + equipmentID);
            },
            newEquipment:function(){
                $location.path("/equipments/new");
            }
        };
    });