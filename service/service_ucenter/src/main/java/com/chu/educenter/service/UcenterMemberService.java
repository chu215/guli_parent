package com.chu.educenter.service;

import com.chu.educenter.entity.UcenterMember;
import com.baomidou.mybatisplus.extension.service.IService;
import com.chu.educenter.entity.vo.RegisterVo;

/**
 * <p>
 * 会员表 服务类
 * </p>
 *
 * @author chu
 * @since 2022-12-02
 */
public interface UcenterMemberService extends IService<UcenterMember> {

    String login(UcenterMember member);

    void register(RegisterVo registerVo);

    UcenterMember getOpenIdMember(String openid);

    Integer countRegisterDay(String day);
}
