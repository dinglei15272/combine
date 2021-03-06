package com.pinyougou.order.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.ISelect;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.pinyougou.cart.Cart;
import com.pinyougou.common.pojo.PageResult;
import com.pinyougou.common.util.IdWorker;
import com.pinyougou.mapper.*;
import com.pinyougou.pojo.*;
import com.pinyougou.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 订单服务接口实现类
 *
 * @author lee.siu.wah
 * @version 1.0
 * <p>File Created at 2019-03-20<p>
 */
@Service(interfaceName = "com.pinyougou.service.OrderService")
@Transactional
public class OrderServiceImpl implements OrderService {

    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private OrderItemMapper orderItemMapper;
    @Autowired
    private PayLogMapper payLogMapper;
    @Autowired
    private IdWorker idWorker;
    @Autowired
    private SellerMapper sellerMapper;
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private ItemMapper itemMapper;
    @Override
    public void save(Order order) {
        try {

            // 1. 从Redis数据库获取该用户的购物车
            List<Cart> carts = (List<Cart>) redisTemplate
                    .boundValueOps("cart_" + order.getUserId()).get();

            // 定义支付总金额
            double totalMoney = 0;
            // 封装多个订单id
            String orderIds = "";
            
            // 2. 往tb_order表插入数据(一个商家的购物车产生一个订单)
            for (Cart cart : carts) {
                // 一个cart代表一个商家的购物车(产生一个订单)
                Order order1 = new Order();
                // 生成主键id
                long orderId = idWorker.nextId();
                // 设置订单id
                order1.setOrderId(orderId);
                // 设置支付方式
                order1.setPaymentType(order.getPaymentType());
                // 设置状态码: 1 未付款
                order1.setStatus("1");
                // 设置订单创建时间
                order1.setCreateTime(new Date());
                // 设置订单修改时间
                order1.setUpdateTime(order1.getCreateTime());
                // 设置用户id
                order1.setUserId(order.getUserId());
                // 设置收件地址
                order1.setReceiverAreaName(order.getReceiverAreaName());
                // 设置收件人手机号码
                order1.setReceiverMobile(order.getReceiverMobile());
                // 设置收件人姓名
                order1.setReceiver(order.getReceiver());
                // 设置订单来源
                order1.setSourceType(order.getSourceType());
                // 设置商家id
                order1.setSellerId(cart.getSellerId());

                // 订单总金额
                double money = 0;

                // 3. 往tb_order_item表插入数据
                for (OrderItem orderItem : cart.getOrderItems()) {

                    // 生成主键id
                    orderItem.setId(idWorker.nextId());
                    // 设置关联的订单id
                    orderItem.setOrderId(orderId);

                    // 累计订单的金额
                    money += orderItem.getTotalFee().doubleValue();
                    // 保存订单明细
                    orderItemMapper.insertSelective(orderItem);
                }

                // 累计订单总金额
                totalMoney += money;
                // 拼接多个订单id
                orderIds += orderId + ",";
                // 设置订单的总金额
                order1.setPayment(new BigDecimal(money));
                // 保存订单
                orderMapper.insertSelective(order1);
            }


            // 往支付日志表中插入数据
            if ("1".equals(order.getPaymentType())){
                // 在线支付
                PayLog payLog = new PayLog();
                // 交易订单号
                payLog.setOutTradeNo(String.valueOf(idWorker.nextId()));
                // 创建时间
                payLog.setCreateTime(new Date());
                // 支付的总金额(分)
                payLog.setTotalFee((long)(totalMoney * 100));
                // 用户id
                payLog.setUserId(order.getUserId());
                // 交易状态 (未支付)
                payLog.setTradeState("0");
                // 多个订单id，组成一次支付
                payLog.setOrderList(orderIds.substring(0, orderIds.length() - 1));
                // 支付类型
                payLog.setPayType(order.getPaymentType());

                // 插入数据
                payLogMapper.insertSelective(payLog);

                // 把PayLog对象存储到Redis数据库
                redisTemplate.boundValueOps("payLog_"
                        + order.getUserId()).set(payLog);
            }

            // 4. 从Redis数据库中删除用户的购物车
            redisTemplate.delete("cart_" + order.getUserId());

        }catch (Exception ex){
            throw new RuntimeException(ex);
        }
    }

    @Override
    public void update(Order order) {

    }

    @Override
    public void delete(Serializable id) {

    }

    @Override
    public void deleteAll(Serializable[] ids) {

    }

    @Override
    public Order findOne(Serializable id) {
        return orderMapper.selectByPrimaryKey(id);
    }

    @Override
    public List<Order> selectAll() {
        return orderMapper.selectAll();
    }

    @Override
    public   Map findByPage(Map<String, Object> params,String userName) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
//            String userName = (String) params.get("userName");
            Integer page = (Integer) params.get("page");
            Integer rows = (Integer) params.get("rows");
            page = (page - 1) * rows;
            Map<Object,Object> data = new HashMap<>();
            List<Object> list = new ArrayList<>();
            List<Order> orderList  = orderMapper.findAll(page,rows,userName);
            if (orderList !=null && orderList.size() > 0){
                for (Order order : orderList) {
                    OrderItem orderItem = orderItemMapper.findAll(order.getOrderId());
                    if (orderItem != null){
                        Map<String,Object> map = new HashMap<>();
                        map.put("goodsId",order.getSellerId());
                        String status = order.getStatus();
                        if (status.equals("1")){
                            status = "未付款";
                        }else if (status.equals("2")){
                            status = "已付款";
                        }else if (status.equals("3")){
                            status = "未发货";
                        }else if (status.equals("4")){
                            status = "已发货";
                        }else if (status.equals("5")){
                            status = "交易成功";
                        }else if (status.equals("6")){
                            status = "交易关闭";
                        }else if (status.equals("7")){
                            status = "待评价";
                        }
                        map.put("status",status);
                        map.put("orderId",order.getOrderId());
                        map.put("sellerId",sellerMapper.selectById(order.getSellerId()));
                        map.put("picPath",orderItem.getPicPath());
                        map.put("num",orderItem.getNum());
                        map.put("price",orderItem.getPrice());
                        map.put("totalFee",orderItem.getTotalFee());
                        map.put("title",orderItem.getTitle());
                        Date time = order.getUpdateTime();
                        System.out.println(sdf.format(time));
                        map.put("updateTime",sdf.format(time));
                        map.put("id",orderItem.getId());
                        list.add(map);
                    }else {
                        data.put("commodityIsNull",true);
                    }
                }
                // 总记录数
                data.put("total", list.size());
                double content =(double)  list.size() / rows;
                // 总页数
                data.put("totalPages",Math.ceil(content));
                data.put("orders",list);

            }else {
                data.put("mistake",true);
            }
            return data;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }



    /** 根据登录用户名，从Redis数据库获取支付日志对象 */
    public PayLog findPayLogFromRedis(String userId){
        try{
            return (PayLog)redisTemplate.boundValueOps("payLog_" + userId).get();
        }catch (Exception ex){
            throw new RuntimeException(ex);
        }
    }

    /** 支付成功，业务处理 */
    public void updateStatus(String outTradeNo, String transactionId){
        try{
            // 1. 修改支付日志表
            PayLog payLog = payLogMapper.selectByPrimaryKey(outTradeNo);
            // 支付时间
            payLog.setPayTime(new Date());
            // 交易状态
            payLog.setTradeState("1");
            // 微信支付订单号
            payLog.setTransactionId(transactionId);
            // 修改
            payLogMapper.updateByPrimaryKeySelective(payLog);

            // 2. 修改订单表
            String[] orderIds = payLog.getOrderList().split(",");
            // 循环修改多个订单的付款状态
            for (String orderId : orderIds) {
                Order order = new Order();
                order.setOrderId(Long.valueOf(orderId));
                // 付款时间
                order.setPaymentTime(new Date());
                // 已付款
                order.setStatus("2");
                // 修改
                orderMapper.updateByPrimaryKeySelective(order);
            }
            // 3. 删除支付日志
            redisTemplate.delete("payLog_" + payLog.getUserId());
        }catch (Exception ex){
            throw new RuntimeException(ex);
        }
    }
}
