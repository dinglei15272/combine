/** 定义控制器层 */
app.controller('passwordController', function ($scope, $controller, baseService) {
    /** 提交新密码 */
    $scope.saveOrUpdate = function () {
        // 获取密码的内容进行对比
        if ($scope.password != null) {
            if ($scope.password.old != null) {
                if ($scope.password.new01 != null) {
                    if ($scope.password.new01.length <= 20 && $scope.password.new01.length > 5) {
                        var patrn = /^[0-9]{1,20}$/;
                        if (!patrn.exec($scope.password.new01)) {
                            var patrn = /^[a-zA-Z]{1,20}$/;
                            if (!patrn.exec($scope.password.new01)) {
                                if ($scope.password.new01 == $scope.password.new02) {
                                    // $scope.oldpassword = $scope.password.old;
                                    // $scope.newpassword = $scope.password.new01;
                                    baseService.sendPost("/password/update", $scope.password)
                                        .then(function (response) {
                                            if (response.data) {
                                                /** 清空表单数据 */
                                                $scope.password = null;
                                                /** 清空富文本编辑器中的内容 */
                                                alert("修改成功，即将刷新页面！");
                                                location.reload();
                                            } else {
                                                alert("你的密码输入有误！！");
                                            }
                                        });
                                } else {
                                    alert("两次新密码输入不一致！！")
                                }
                            } else {
                                alert("请不要纯英文，输入些别的吧。")
                            }
                        } else {
                            alert("请不要纯数字，输入些别的吧。")
                        }
                    } else {
                        alert("密码长度应该在 6 - 20 位");
                    }
                } else {
                    alert("请输入新密码！")
                }
            } else {
                alert("请输入原密码！")
            }
        } else {
            alert("请输入原密码！")
        }
    };

    $scope.reset = function () {
        $scope.password = null;
    }
});