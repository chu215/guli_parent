package com.chu.eduorder.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.chu.commonutils.JwtUtils;
import com.chu.commonutils.R;
import com.chu.eduorder.entity.Order;
import com.chu.eduorder.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * 订单 前端控制器
 * </p>
 *
 * @author chu
 * @since 2022-12-26
 */
@RestController
@RequestMapping("/eduorder/order")
@CrossOrigin
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping("/createOrder/{courseId}")
    public R saveOrder(@PathVariable String courseId, HttpServletRequest request) {
        String orderNo = orderService.createOrders(courseId, JwtUtils.getMemberIdByJwtToken(request));

        return R.ok().data("orderId", orderNo);
    }

    @GetMapping("/getOrderInfo/{orderId}")
    public R getOrderInfo(@PathVariable String orderId) {
        QueryWrapper<Order> orderQueryWrapper = new QueryWrapper<>();
        orderQueryWrapper.eq("order_no", orderId);
        Order order = orderService.getOne(orderQueryWrapper);
        return R.ok().data("item", order);
    }

    // 根据课程id和用户id查询订单表订单状态
    @GetMapping("/isBuyCourse/{courseId}/{memberId}")
    public boolean isBuyCourse(@PathVariable String courseId, @PathVariable String memberId) {
        QueryWrapper<Order> orderQueryWrapper = new QueryWrapper<>();
        orderQueryWrapper.eq("course_id", courseId);
        orderQueryWrapper.eq("member_id", memberId);
        orderQueryWrapper.eq("status", 1);

        int count = orderService.count(orderQueryWrapper);
        if (count > 0) {
            return true;
        } else {
            return false;
        }
    }
}

