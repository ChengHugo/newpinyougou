package com.pinyougou.task;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * 定时更新redis中的秒杀商品
 */
@Component
public class SeckillTask {

    /**
     * 更新秒杀商品数据
     *
     */
    @Scheduled(cron = "0/2 * * * * ?")
    public void refreshSeckillGoods(){
        System.out.println("-------------" + new Date());
    }
}
