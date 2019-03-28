app.controller("settingController",function ($scope,$controller, $timeout, baseService) {


    $controller('indexController',{$scope:$scope});
    // 定义json对象
    $scope.user = {};

    $scope.setSafe = function () {
        if ($scope.okPassword && $scope.user.password == $scope.okPassword) {
            baseService.sendPost("/user/setSafe", $scope.user).then(function (response) {

                if (response.data) {

                    $scope.user = {};
                    $scope.okPassword = "";
                    alert("密码设置成功");
                } else {
                    alert("设置失败");
                }
            });
        } else {
            alert("昵称为空或两次密码不一致");
        }
    };


    // 定义显示文本
    $scope.msg = "发送";
    $scope.flag = false;

    // 发送短信验证码
    $scope.sendSms = function () {

        // 判断手机号码的有效性
        if ($scope.phone && /^1[3|4|5|7|8|9]\d{9}$/.test($scope.phone)){
            // 发送异步请求
            baseService.sendGet("/user/sendSmsCode?phone="
                + $scope.phone).then(function(response){
                // 获取响应数据
                if (response.data){
                    // 倒计时 (扩展)
                    $scope.flag = true;
                    // 调用倒计时方法
                    $scope.downcount(89);
                }else{
                    alert("获取短信验证码失败！");
                }
            });
        }else{
            alert("手机号码不正确！");
        }
    };


    // 倒计时方法
    $scope.downcount = function (seconds) {
        if (seconds > 0) {
            seconds--;
            $scope.msg = seconds + "秒，后重新获取";

            // 开启定时器
            $timeout(function () {
                $scope.downcount(seconds);
            }, 1000);
        }else{
            $scope.msg = "发送";
            $scope.flag = false;
        }
    };
    $scope.next01 = function () {

        baseService.sendPost("/user/check?code=" +
            $scope.code + "&smsCode=" + $scope.smsCode + "&phone=" +$scope.phone)
            .then(function (response) {
                if (response.data) {
                    location.href = "/home-setting-address-phone.html";
                }else {
                    alert("验证失败")
                }
            })
    };

    $scope.next02 = function () {

        baseService.sendPost("/user/check?code=" +
            $scope.code + "&smsCode=" + $scope.smsCode + "&phone=" +$scope.phone)
            .then(function (response) {
                if (response.data) {
                    location.href = "/home-setting-address-complete.html";
                }else {
                    alert("验证失败")
                }
            });
    };

    $scope.save = function () {
        baseService.sendPost("/user/savePhone?phone="+ $scope.phone).then(function (response) {
            if (response.data){
                alert("成功");
            }
        })
    };


    $scope.findProvinces = function () {

        baseService.sendGet("/setting/findProvinces").then(function (response) {
            $scope.provincesList = response.data;
        })
    };

    $scope.$watch("provinces.provinceId",function (newVal, oldVal) {

        if (newVal){
           var i = $scope.provincesList[newVal-1].provinceId;
           baseService.sendGet("/setting/findCities?provinceId=" + i).then(function (response) {

               $scope.citiesList = response.data;
           });
        }
    });

    $scope.$watch("cities.cityId",function (newVal, oldVal) {

        if (newVal){
            var c = $scope.citiesList[newVal-1].cityId;
            baseService.sendGet("/setting/findAreas?cityId=" + c).then(function (response) {

                $scope.areasList = response.data;
            });
        }
    });

    $scope.findPhone = function () {

        baseService.sendGet("/setting/findPhone").then(function (response) {
           $scope.phone = response.data;
        });
    };
	
	
	   $scope.add={};
    // 获取登录用户名
    $scope.showName = function () {
        baseService.sendGet("/user/showName").then(function (response) {
            // 获取响应数据
            $scope.loginName = response.data.loginName;
        });
    };


        /** 根据登录用户获取地址 */
        $scope.findAddressByUser = function(){
            baseService.sendGet("/setting/findAddressByUser")
                .then(function(response){
                    $scope.addressList = response.data;
                });
        };


    $scope.findByTableId = function(table,tableId, name){
        baseService.sendGet("/setting/"+table+"?tableId=" + tableId).then(function(response){
            $scope[name] = response.data;
        });
    };


    $scope.$watch('add.provinceId', function(newValue, oldValue){
        if (newValue){
            var table = "city";
            /** 根据选择的值查询二级分类 */
            $scope.findByTableId(table,newValue, "cityList");
        }else{
            $scope.cityList = [];
        }
    });


 $scope.$watch('add.cityId', function(newValue, oldValue){
        if (newValue){
            var table = "area";
            /** 根据选择的值查询二级分类 */
            $scope.findByTableId(table,newValue, "areaList");
        }else{
            $scope.areaList = [];
        }
    });



    /** 保存商品 */
    $scope.saveAddress = function(){
        // 发送异步请求
        baseService.sendPost("/setting/save", $scope.add).then(
            function(response){
                if(response.data){
                    alert("保存成功！");
                    /** 清空表单 */
                    $scope.add = {};

                    $scope.findAddressByUser();
                }else{
                    alert("保存失败！");
                }
            }
        );
    };

   $scope.delete=function (id) {
       baseService.sendGet("/setting/delete?id="+id).then(function (response) {
           if(response.data){
               alert("ok");
               $scope.findAddressByUser();
           }else {
               alert("no");
           }
       });
   };

   var DefaultId = 0;
    $scope.saveDefault=function (id,st) {
        if(st==1){
            DefaultId = id;
        }else {
            baseService.sendGet("/setting/saveDefault?id="+id+"&st="+st).then(function (response) {
                if(response.data){
                    $scope.saveDefault0();
                    $scope.findAddressByUser();
                }else {
                    alert("no");
                }
            })
        }
    };

    $scope.saveDefault0=function () {
        baseService.sendGet("/setting/saveDefault?id="+DefaultId+"&st=1").then(function (response) {
            $scope.findAddressByUser();
        })
    }
});