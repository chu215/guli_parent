package com.chu.msmservice.controller;


import com.chu.commonutils.R;
import com.chu.msmservice.service.MsmService;
import com.chu.msmservice.utils.RandomUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/edumsm/msm")
@CrossOrigin
public class MsmController {

    @Autowired
    private MsmService msmService;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @GetMapping("/send/{phone}")
    public R sendMsm(@PathVariable String phone) {

        String code = stringRedisTemplate.opsForValue().get(phone);
        if (!StringUtils.isEmpty(code)) {
            return R.ok();
        }

        code = RandomUtil.getFourBitRandom();
        Map<String, Object> param = new HashMap<>();
        param.put("code", code);

        Boolean isSend = msmService.send(param, phone);
        if (isSend) {

            stringRedisTemplate.opsForValue().set(phone, code, 5, TimeUnit.MINUTES);

            return R.ok();
        } else {
            return R.error().message("短信发送失败");
        }
    }
}
