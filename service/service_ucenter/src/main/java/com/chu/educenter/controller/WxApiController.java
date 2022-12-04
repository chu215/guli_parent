package com.chu.educenter.controller;

import com.chu.commonutils.JwtUtils;
import com.chu.educenter.entity.UcenterMember;
import com.chu.educenter.service.UcenterMemberService;
import com.chu.educenter.utils.HttpClientUtils;
import com.chu.servicebase.exceptionhandler.GuliException;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;

import static com.chu.educenter.utils.ConstantWxUtils.*;

@Controller
@RequestMapping("/api/ucenter/wx")
@CrossOrigin
public class WxApiController {

    @Autowired
    private UcenterMemberService memberService;

    // 获取扫码人信息
    @GetMapping("/callback")
    public String callback(String code, String state) {
        System.out.println("code = " + code + ", state = " + state);

        try {
            // 向认证服务器发送请求换取access_token,openid
            String baseAccessTokenUrl = "https://api.weixin.qq.com/sns/oauth2/access_token" +
                    "?appid=%s" +
                    "&secret=%s" +
                    "&code=%s" +
                    "&grant_type=authorization_code";
            String accessTokenUrl = String.format(baseAccessTokenUrl,
                    WX_OPEN_APP_ID,
                    WX_OPEN_APP_SECRET,
                    code);

            // 使用httpclient发送请求
            String accessTokenInfo = HttpClientUtils.get(accessTokenUrl);

            // 从accessTokenInfo中获取两个值，access_token，openid
            // 把accessTokenInfo字符串转换成map，根据key获取
            // 使用转换工具Gson
            Gson gson = new Gson();
            HashMap mapAccessToken = gson.fromJson(accessTokenInfo, HashMap.class);
            String access_token = (String)mapAccessToken.get("access_token");
            String openid = (String) mapAccessToken.get("openid");

            // 把扫描人添加进数据库
            // 根据openid判断有无相同数据
            UcenterMember member = memberService.getOpenIdMember(openid);
            if (member == null) {

                // 拿着access_token，openid，再去请求固定地址，得到扫码人信息
                String baseUserInfoUrl = "https://api.weixin.qq.com/sns/userinfo" +
                        "?access_token=%s" +
                        "&openid=%s";
                String userInfoUrl = String.format(baseUserInfoUrl, access_token, openid);
                String userInfo = HttpClientUtils.get(userInfoUrl);
                HashMap userInfoMap = gson.fromJson(userInfo, HashMap.class);
                String nickname = (String) userInfoMap.get("nickname");
                String headimgurl = (String) userInfoMap.get("headimgurl");

                member = new UcenterMember();
                member.setNickname(nickname);
                member.setAvatar(headimgurl);
                member.setOpenid(openid);
                memberService.save(member);
            }

            // 根据jwt生成token字符串
            String jwtToken = JwtUtils.getJwtToken(member.getId(), member.getNickname());

            return "redirect:http://localhost:3000?token=" + jwtToken;

        } catch (Exception e) {
            throw new GuliException(20001, "登录失败");
        }
    }

    @GetMapping("/login")
    public String getWxCode() {
        // 微信开放平台授权baseUrl
        String baseUrl = "https://open.weixin.qq.com/connect/qrconnect" +
                "?appid=%s" +
                "&redirect_uri=%s" +
                "&response_type=code" +
                "&scope=snsapi_login" +
                "&state=%s" +
                "#wechat_redirect";

        // 对redirect_url进行URLEncoder编码
        String redirectUrl = WX_OPEN_REDIRECT_URL;
        try {
            redirectUrl = URLEncoder.encode(redirectUrl, "utf-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }

        String url = String.format(
                baseUrl,
                WX_OPEN_APP_ID,
                redirectUrl,
                "atguigu"
        );

        return "redirect:" + url;
    }
}
