package com.chu.eduservice.client;

import com.chu.commonutils.R;
import com.chu.commonutils.vo.MemberVo;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

@Component
public class UcenterClientImpl implements UcenterClient {
    @Override
    public R getMemberInfo(HttpServletRequest request) {
        return null;
    }

    @Override
    public MemberVo getInfo(String id) {
        return null;
    }
}
