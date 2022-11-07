package com.SecondClass.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @title: DateUtils
 * @Author SXSQ
 * @Description //时间判断工具类
 * @Date 2022/11/7 16:17
 **/

public class DateUtils {

    /**
     * 判断当前时间是否在判断时间内
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return
     */
    public static boolean isEffectiveDate(Date startTime, Date endTime) {
        Date nowTime = new Date();
//        System.out.println(nowTime);
//        System.out.println(startTime);
//        System.out.println(endTime);

        if (nowTime.getTime() == startTime.getTime()
                || nowTime.getTime() == endTime.getTime()) {
            System.out.println(1);
            return true;
        }


        Calendar date = Calendar.getInstance();
        date.setTime(nowTime);

        Calendar begin = Calendar.getInstance();
        begin.setTime(startTime);

        Calendar end = Calendar.getInstance();
        end.setTime(endTime);

        if (date.after(begin) && date.before(end)) {
            System.out.println(2);
            return true;
        } else {
            System.out.println(3);
            return false;
        }
    }
}
