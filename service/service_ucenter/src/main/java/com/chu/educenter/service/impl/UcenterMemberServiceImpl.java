package com.chu.educenter.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.chu.commonutils.JwtUtils;
import com.chu.commonutils.MD5;
import com.chu.educenter.entity.UcenterMember;
import com.chu.educenter.entity.vo.RegisterVo;
import com.chu.educenter.mapper.UcenterMemberMapper;
import com.chu.educenter.service.UcenterMemberService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chu.servicebase.exceptionhandler.GuliException;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 会员表 服务实现类
 * </p>
 *
 * @author chu
 * @since 2022-12-02
 */
@Service
public class UcenterMemberServiceImpl extends ServiceImpl<UcenterMemberMapper, UcenterMember> implements UcenterMemberService {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public String login(UcenterMember member) {
        String mobile = member.getMobile();
        String password = member.getPassword();
        if (StringUtils.isEmpty(mobile) || StringUtils.isEmpty(password)) {
            throw new GuliException(20001, "登录失败");
        }

        QueryWrapper<UcenterMember> memberQueryWrapper = new QueryWrapper<>();
        memberQueryWrapper.eq("mobile", mobile);
        UcenterMember mobileMember = getOne(memberQueryWrapper);
        if (mobileMember == null) {
            throw new GuliException(20001, "登录失败");
        }

        if (!mobileMember.getPassword().equals(MD5.encrypt(password))) {
            throw new GuliException(20001, "登录失败");
        }

        if (mobileMember.getIsDisabled()) {
            throw new GuliException(20001, "登录失败");
        }

        String jwtToken = JwtUtils.getJwtToken(mobileMember.getId(), mobileMember.getNickname());
        return jwtToken;
    }

    @Override
    public void register(RegisterVo registerVo) {
        String code = registerVo.getCode();
        String mobile = registerVo.getMobile();
        String nickname = registerVo.getNickname();
        String password = registerVo.getPassword();

        if (StringUtils.isEmpty(mobile) || StringUtils.isEmpty(password)
                || StringUtils.isEmpty(code) || StringUtils.isEmpty(nickname)) {
            throw new GuliException(20001, "登录失败");
        }

        String redisCode = stringRedisTemplate.opsForValue().get(mobile);
        if (!code.equals(redisCode)) {
            throw new GuliException(20001, "注册失败");
        }

        QueryWrapper<UcenterMember> memberQueryWrapper = new QueryWrapper<>();
        memberQueryWrapper.eq("mobile", mobile);
        int count = this.count(memberQueryWrapper);
        if (count > 0) {
            throw new GuliException(20001, "注册失败");
        }

        // 添加数据到数据库
        UcenterMember member = new UcenterMember();
        member.setMobile(mobile);
        member.setNickname(nickname);
        member.setPassword(MD5.encrypt(password));
        member.setIsDisabled(false);

        this.save(member);
    }

    @Override
    public UcenterMember getOpenIdMember(String openid) {
        QueryWrapper<UcenterMember> memberQueryWrapper = new QueryWrapper<>();
        memberQueryWrapper.eq("openid", openid);
        UcenterMember member = getOne(memberQueryWrapper);
        return member;
    }
}
