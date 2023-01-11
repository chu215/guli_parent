package com.chu.eduorder.client;

import com.chu.commonutils.vo.MemberVo;
import com.chu.commonutils.vo.UcenterMemberOrder;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Component
@FeignClient("service-ucenter")
public interface UcenterClient {

    @PostMapping("/educenter/member/getInfo/{id}")
    public UcenterMemberOrder getInfo(@PathVariable("id") String id);

}
