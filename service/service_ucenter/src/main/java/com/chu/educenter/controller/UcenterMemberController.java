package com.chu.educenter.controller;


import com.chu.commonutils.JwtUtils;
import com.chu.commonutils.R;
import com.chu.commonutils.vo.MemberVo;
import com.chu.educenter.entity.UcenterMember;
import com.chu.educenter.entity.vo.RegisterVo;
import com.chu.educenter.service.UcenterMemberService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * 会员表 前端控制器
 * </p>
 *
 * @author chu
 * @since 2022-12-02
 */
@RestController
@RequestMapping("/educenter/member")
@CrossOrigin
public class UcenterMemberController {

    @Autowired
    private UcenterMemberService memberService;

    @PostMapping("/login")
    public R loginUser(@RequestBody UcenterMember member) {
        String token = memberService.login(member);
        return R.ok().data("token", token);
    }

    @PostMapping("/register")
    public R registerUser(@RequestBody RegisterVo registerVo) {
        memberService.register(registerVo);
        return R.ok();
    }

    @GetMapping("/getMemberInfo")
    public R getMemberInfo(HttpServletRequest request) {
        String memberId = JwtUtils.getMemberIdByJwtToken(request);

        UcenterMember member = memberService.getById(memberId);
        return R.ok().data("userInfo", member);
    }

    @PostMapping("/getInfo/{id}")
    public MemberVo getInfo(@PathVariable String id) {
        UcenterMember member = memberService.getById(id);
        MemberVo memberVo = new MemberVo();
        BeanUtils.copyProperties(member, memberVo);

        return memberVo;
    }
}

