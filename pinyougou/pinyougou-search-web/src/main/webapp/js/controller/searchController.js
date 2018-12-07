app.controller("searchController", function ($scope, searchService) {

    //定义提交到后台的对象
    $scope.searchMap = {"keywords":"","brand":"","category":"","price":"","spec":{}};

    //搜索
    $scope.search = function () {

        searchService.search($scope.searchMap).success(function (reponse) {
            $scope.resultMap = reponse;
        });

    };

    //添加过滤条件
    $scope.addSearchItem = function (key, value) {
        if ("brand" == key || "category" == key || "price" == key) {
            $scope.searchMap[key] = value;
        } else {
            //规格
            $scope.searchMap.spec[key] = value;
        }

        //查询
        $scope.search();

    };

    //删除过滤条件
    $scope.removeSearchItem = function (key) {
        if ("brand" == key || "category" == key || "price" == key) {
            $scope.searchMap[key] = "";
        } else {
            //删除规格对象中的属性
            delete $scope.searchMap.spec[key];
        }

        //查询
        $scope.search();

    };

});