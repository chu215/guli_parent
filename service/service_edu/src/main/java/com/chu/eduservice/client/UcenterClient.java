package com.chu.eduservice.client;

import com.chu.commonutils.R;
import com.chu.commonutils.vo.MemberVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;

@Component
@FeignClient(value = "service-ucenter", fallback = UcenterClientImpl.class)
public interface UcenterClient {

    @GetMapping("/getMemberInfo")
    public R getMemberInfo(HttpServletRequest request);

    @PostMapping("/getInfo/{id}")
    public MemberVo getInfo(@PathVariable String id);
}
