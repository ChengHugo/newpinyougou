app.controller("orderInfoController", function ($scope, addressService, cartService) {

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

            //计算总数量和价格
            $scope.totalValue = cartService.subTotalValue(response);
        });

    };

    //加载地址列表
    $scope.findAddressList = function () {
        addressService.findAddressList().success(function (response) {
            $scope.addressList = response;

            //获取默认的地址
            for (var i = 0; i < response.length; i++) {
                var address = response[i];
                if("1"==address.isDefault){
                    $scope.address = address;
                    break;
                }
            }

        });

    };

    //当前选择的地址
    $scope.selectAddress = function (address) {
        $scope.address = address;

    };

    //判断是否选中的地址
    $scope.isSelectedAddress = function (address) {
        return $scope.address==address;
    };

    //初始化支付方式；微信支付为1，货到付款为2
    $scope.order = {"paymentType":"1"};

    //选择支付类型
    $scope.selectPaymentType = function (type) {
        $scope.order.paymentType = type;

    };

});