/**
 * Created with JetBrains WebStorm.
 * User: capacman
 * Date: 10/13/12
 * Time: 2:21 PM
 * To change this template use File | Settings | File Templates.
 */
/*jslint browser:true */

function EquipmentListCtrl($scope, EquipmentService, ApplicationService) {
    $scope.reverse = false;
    $scope.itemsPerPage = 5;
    $scope.maxPages = 0;
    $scope.currentPage = 0;
    $scope.equipments = EquipmentService.query(function () {
        $scope.maxPages = Math.ceil($scope.equipments.length / $scope.itemsPerPage);
    });
    $scope.openEquipment = function (equipmentID) {
        ApplicationService.openEquipment(equipmentID);
    };
    $scope.newEquipment = function () {
        ApplicationService.newEquipment();
    };
    $scope.prevPage = function () {
        if ($scope.currentPage > 0) {
            $scope.currentPage--;
        }
    };
    $scope.nextPage = function () {
        if ($scope.currentPage < $scope.maxPages - 1) {
            $scope.currentPage++;
        }
    };
    $scope.setPage = function () {
        $scope.currentPage = this.n;
    };

    $scope.getPage = function (pageNumber) {
        return $scope.equipments.slice($scope.itemsPerPage * pageNumber, $scope.itemsPerPage * pageNumber + $scope.itemsPerPage);
    };
    $scope.range = function (start, end) {
        var ret = [];
        if (!end) {
            end = start;
            start = 0;
        }
        for (var i = start; i < end; i++) {
            ret.push(i);
        }
        return ret;
    };

}
function EquipmentDetailCtrl($scope, EquipmentService, $routeParams) {

    $scope.equipment = EquipmentService.get($routeParams);
    $scope.fileName = "";
    $scope.fileSize = 0;
    $scope.fileType = "";
    $scope.fileSelected = function () {
        $scope.$apply(function () {
            console.log("selected");
            var file = document.getElementById('fileInput').files[0];
            console.log(file);
            if (file) {
                var fileSize = 0;
                if (file.size > 1024 * 1024)
                    fileSize = (Math.round(file.size * 100 / (1024 * 1024)) / 100).toString() + 'MB';
                else
                    fileSize = (Math.round(file.size * 100 / 1024) / 100).toString() + 'KB';
                console.log(file.name);
                console.log(fileSize);
                console.log(file.type);
                $scope.fileName = file.name;
                $scope.fileSize = fileSize;
                $scope.fileType = file.type;
            }
        });
    }
}

function EquipmentFormCtrl($scope, EquipmentService, CategoryService, BrandService,$filter) {
    $scope.categories = CategoryService.query();
    $scope.brands = BrandService.query();
    /*$('.date').datepicker({
        autoclose:true
    }).on('changeDate',function(e){
        $scope.$apply(function(){
            $scope.equipment[e.target.name]= $filter('date')(e.date,'yy-mm-dd');
        });
    });*/
    $scope.equipment = {
        productName:"",
        productCode:"",
        category:undefined,
        brand:undefined,
        stockEntranceDate:undefined,
        bestBeforeDate:undefined,
        productionDate:undefined
    };
    function isValidPristine(formName){
        return !$scope.equipmentForm[formName].$pristine && !$scope.equipmentForm[formName].$valid;
    }
    $scope.addClass=function(formName,className){
        return isValidPristine(formName) ? className:'';
    };
    $scope.isValidPristine=function(formName){
        return isValidPristine(formName);
    };
    $scope.submitEnabled=function(){
        return $scope.equipmentForm.$valid ? "" : "disabled";
    };

}