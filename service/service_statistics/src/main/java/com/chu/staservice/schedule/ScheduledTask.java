package com.chu.staservice.schedule;

import com.chu.staservice.service.StatisticsDailyService;
import com.chu.staservice.utils.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class ScheduledTask {

    @Autowired
    private StatisticsDailyService staService;

    // 在每天凌晨一点，把前一天数据进行查询添加
    @Scheduled(cron = "0 0 1 * * ?")
    public void task() {
        String day = DateUtil.formatDate(DateUtil.addDays(new Date(), -1));
        staService.registerCount(day);
    }
}
