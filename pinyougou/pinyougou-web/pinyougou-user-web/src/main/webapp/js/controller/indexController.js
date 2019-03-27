/** 定义控制器层 */
app.controller('indexController', function($scope, baseService,$http){

    // 获取登录用户名
    $scope.showName = function () {
        baseService.sendGet("/user/showName").then(function (response) {
            // 获取响应数据
            $scope.loginName = response.data.loginName;
        });
    };
    // 定义json对象封装搜索条件
    $scope.searchParam = { page: 1 ,rows: 5 };

    // 商品搜索的方法
    $scope.findByPage = function () {
        // 发送异步请求
        baseService.sendPost("/order/findByPage" , $scope.searchParam).then(function(response){
            // 获取响应数据 response.data: {total: 100, rows:[{},{}]} rows:List<SolrItem>
            $scope.resultMap = response.data;
            $scope.to
            // 调用初始化页码的方法
            $scope.initPageNums();
        });
    };

    /** 添加SKU商品到购物车 */
    $scope.payment = function(id,num){
        $http.get("http://cart.pinyougou.com/cart/addCart?itemId="
            + id + "&num=" + num,{"withCredentials":true})
            .then(function(response){
                if (response.data){
                    /** 跳转到购物车页面 */
                    location.href='http://cart.pinyougou.com/cart.html';
                }else{
                    alert("请求失败！");
                }
            });
    };

    // 定义初始化页码的方法
    $scope.initPageNums = function () {
        // 页码数组
        $scope.pageNums = [];

        // 开始页码
        var firstPage = 1;
        // 结束页码
        var lastPage = $scope.resultMap.totalPages;

        // 前面加点
        $scope.firstDot = true;
        // 后面加点
        $scope.lastDot = true;

        // 判断总页数是不是大于5
        if ($scope.resultMap.totalPages > 5){

            // 当前页码靠首页近些
            if ($scope.searchParam.page <= 3){
                lastPage = 5;
                $scope.firstDot = false;
            }else if($scope.searchParam.page >= $scope.resultMap.totalPages - 3){
                // 当前页码靠尾页近些
                firstPage = $scope.resultMap.totalPages - 4;
                $scope.lastDot = false;
            }else{
                // 在中间
                firstPage = $scope.searchParam.page - 2;
                lastPage = $scope.searchParam.page + 2;
            }
        }else{
            // 前面加点
            $scope.firstDot = false;
            // 后面加点
            $scope.lastDot = false;
        }
        for (var i = firstPage; i <= lastPage; i++){
            $scope.pageNums.push(i);
        }
    };

    // 分页搜索
    $scope.pageSearch = function (page) {

        page = parseInt(page);

        // 判断页码的有效性
        if (page >= 1 && page <= $scope.resultMap.totalPages
            && page != $scope.searchParam.page){
            $scope.searchParam.page = page;
            $scope.jumpPage = page;
            // 执行搜索
            $scope.payment();
        }
    };
});