package com.chu.eduorder.service.impl;

import com.chu.commonutils.vo.CourseWebVoOrder;
import com.chu.commonutils.vo.MemberVo;
import com.chu.commonutils.vo.UcenterMemberOrder;
import com.chu.eduorder.client.EduClient;
import com.chu.eduorder.client.UcenterClient;
import com.chu.eduorder.entity.Order;
import com.chu.eduorder.mapper.OrderMapper;
import com.chu.eduorder.service.OrderService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chu.eduorder.utils.OrderNoUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 订单 服务实现类
 * </p>
 *
 * @author chu
 * @since 2022-12-26
 */
@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements OrderService {

    @Autowired
    private EduClient eduClient;

    @Autowired
    private UcenterClient ucenterClient;

    @Override
    public String createOrders(String courseId, String memberId) {
        // 通过远程调用根据用户id获取用户信息
        UcenterMemberOrder ucenterMemberOrder = ucenterClient.getInfo(memberId);

        // 通过远程调用根据课程id获取课程信息
        CourseWebVoOrder courseInfoOrder = eduClient.getCourseInfoOrder(courseId);

        Order order = new Order();
        order.setOrderNo(OrderNoUtil.getOrderNo());
        order.setCourseId(courseId);
        order.setCourseTitle(courseInfoOrder.getTitle());
        order.setCourseCover(courseInfoOrder.getCover());
        order.setTeacherName(courseInfoOrder.getTeacherName());
        order.setTotalFee(courseInfoOrder.getPrice());
        order.setMemberId(memberId);
        order.setMobile(ucenterMemberOrder.getMobile());
        order.setNickname(ucenterMemberOrder.getNickname());
        order.setStatus(0);
        order.setPayType(1);

        this.save(order);
        return order.getOrderNo();
    }
}
