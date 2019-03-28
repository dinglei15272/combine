// 购物车控制器
app.controller('cartController', function ($scope, $controller, baseService) {
    // 继承baseController
    $controller('baseController', {$scope:$scope});




    // 查询用户的购物车
    $scope.findCart = function () {
        baseService.sendGet("/cart/findCart").then(function(response){
            // 获取响应数据
            $scope.carts = response.data;


            $scope.updateTotalMoney();

            // 循环用户的购物车数组

        });
    };

    // 购买数量增减与删除
    $scope.addCart = function (itemId, num) {


        baseService.sendGet("/cart/addCart?itemId="
            + itemId + "&num=" + num).then(function(response){
            // 获取响应数据
            if (response.data){
                // 重新加载购物车数据
                $scope.findCart();

            }
        });
    };

    $scope.totalFees = [];
    $scope.arr=[];

    $scope.cartList = [];

     //商品复选框情况数组
    $scope.itemCheckbox =[[],[],[],[],[]];
    //商家复选框情况数组
    $scope.sellerCheckbox = [];
   //商品itemId
    $scope.itemId = [[],[],[],[],[]];



    $scope.selectOrderItem = function (parentIndex,index,$event) {

        $scope.itemCheckbox[parentIndex][index] = $event.target.checked;
         if($event.target.checked){
             $scope.itemId[parentIndex].push($scope.carts[parentIndex].orderItems[index].itemId);
         }else{
             $scope.itemId[parentIndex].splice( $scope.itemId[parentIndex].indexOf($scope.carts[parentIndex].orderItems[index].itemId),1);
         }

         $scope.sellerCheckbox[parentIndex] = $scope.carts[parentIndex].orderItems.length == $scope.itemId[parentIndex].length;

        $scope.findAll();
        $scope.updateTotalMoney();
    };


    $scope.selectCart = function (index,$event) {

        $scope.itemId[index] = [];
       for(var i=0;i< $scope.carts[index].orderItems.length;i++){
           $scope.itemCheckbox[index][i] = $event.target.checked;
           if($event.target.checked){
               $scope.itemId[index].push($scope.carts[index].orderItems[i].itemId);
           }else{
               $scope.itemId[index].splice( $scope.itemId[index].indexOf($scope.carts[index].orderItems[index].itemId),1);
           }

       }

        $scope.sellerCheckbox[index] = $scope.carts[index].orderItems.length == $scope.itemId[index].length;
        $scope.findAll();
        $scope.updateTotalMoney();
    };

   $scope.selectAll = function ($event) {
       for(var i=0;i<$scope.carts.length;i++){
          $scope.selectCart(i,$event);
       }

   };

    $scope.findAll = function () {
        $scope.cAll = true;
        for (var i = 0; i < $scope.carts.length; i++) {
            if (!$scope.sellerCheckbox[i]) {
                $scope.cAll = false;
            }
        }
    };
    $scope.updateTotalMoney = function () {

        $scope.totalEntity = {totalNum : 0, totalMoney : 0};
        for (var i = 0; i < $scope.itemId.length; i++) {
            var cart = $scope.itemId[i];
            // i 就是对应cartList中的购物车的索引
            for (var j = 0; j < cart.length; j++) {
                // 得到ids中该位置的itemId
                var id = cart[j];
                for (var k = 0; k < $scope.carts[i].orderItems.length; k++) {
                    var orderItem = $scope.carts[i].orderItems[k];
                    if (orderItem.itemId == id) {
                        $scope.totalEntity.totalNum += orderItem.num;
                        $scope.totalEntity.totalMoney += orderItem.totalFee;
                    }
                }
            }
        }

    };

    $scope.topay = function () {

      if($scope.totalEntity.totalMoney == 0){
          alert("请先选择商品")
      }else{
          location.href = "/order/getOrderInfo.html";
      }


    };


});