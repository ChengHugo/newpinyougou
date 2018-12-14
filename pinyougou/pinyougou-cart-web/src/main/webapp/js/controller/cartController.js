app.controller("cartController", function ($scope, cartService) {

    //获取用户信息
    $scope.getUsername = function () {
        cartService.getUsername().success(function (response) {
            $scope.username = response.username;

        });

    };

    //获取购物车列表数据
    $scope.findCartList = function () {
        cartService.findCartList().success(function (response) {
            $scope.cartList = response;

        });

    };
});