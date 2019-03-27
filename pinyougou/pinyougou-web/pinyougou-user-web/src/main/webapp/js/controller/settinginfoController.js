/** 定义控制器层 */
app.controller('settinginfoController', function ($scope, $controller, baseService, time) {
    $scope.entity = {};


    //保存用户信息
    $scope.saveOrUpdate = function () {
        baseService.sendPost("/info/update", $scope.entity)
            .then(function (response) {
                if (response.data) {
                    alert("修改成功，即将刷新页面！");
                    location.reload();
                } else {
                    alert("修改失败！！");
                }
            });
    };

    // //异步请求获得地址数据
    // $scope.search = function(){
    //     baseService.sendPost("/info/site")
    //         .then(function(response){
    //             //获得数据
    //             $scope.entity = response.data.rows;
    //             /** 更新分页总记录数 */
    //             $scope.paginationConf.totalItems = response.data.total;
    //         });
    // };
    // 根据父级id查询商品分类
    $scope.site = function (parentId, table, name) {
        baseService.sendGet("/info/" + table + "?parentId="
            + parentId).then(function (response) {
            // 获取响应数据 List<ItemCat> [{},{}]
            $scope[name] = response.data;
        });
    };


    // $scope.$watch(): 监控"entity.category1Id"发生改变，查询二级分类
    $scope.$watch("entity.address.province", function (newVal, oldVal) {//newVal 传入是 ID
        //alert(newVal + "==" + oldVal);
        if (newVal) { // 不是undefined、null
            // 发送异步请求查询二级分类
            var table = "city";
            $scope.site(newVal, table, 'itemCatList2');
        } else {
            $scope.itemCatList2 = [];
        }
    });

    // $scope.$watch(): 监控"entity.category2Id"发生改变，查询三级分类
    $scope.$watch("entity.address.city", function (newVal, oldVal) {//newVal 传入是 ID
        if (newVal) { // 不是undefined、null
            // 发送异步请求查询三级分类
            var table = "area";
            $scope.site(newVal, table, 'itemCatList3');
        } else {
            $scope.itemCatList3 = [];
        }
    });

    $scope.cook = function () {
        alert($scope.entity.address);
    };
    // 图片上传
    $scope.upload = function () {
        baseService.uploadFile().then(function (response) {
            // 获取响应数据: {status : 200|500, url : ''}
            if (response.data.status == 200) {
                $scope.entity.headPic = response.data.url;
                alert("上传成功")
            }
        });
    };

    // 获取登录用户的相关信息
    $scope.showUser = function () {
        baseService.sendGet("/user/showUser").then(function (response) {
            // 获取响应数据
            $scope.loginName = response.data.loginName;
            $scope.User = response.data.User;
            $scope.entity.headPic = $scope.User.headPic;
            // alert($scope.User.birthday);
            $scope.entity.birthday = time.timeDate($scope.User.birthday);
            $scope.entity.sex = $scope.User.sex;
            $scope.entity.nickName = $scope.User.nickName;
            // alert(response.data.province);
            // alert(response.data.city);
            // alert(response.data.area);
            if (response.data.province) {
                var table = "city";
                $scope.site(response.data.province, table, 'itemCatList2');
                var table = "area";
                $scope.site(response.data.city, table, 'itemCatList3');
                $scope.entity.address = {};
                $scope.entity.address.province = response.data.province;
                $scope.entity.address.city = response.data.city;
                $scope.entity.address.area = response.data.area;
            }
        });
    };
});