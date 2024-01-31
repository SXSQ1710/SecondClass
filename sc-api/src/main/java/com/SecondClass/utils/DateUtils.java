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

        return isEffectiveDate(nowTime, startTime, endTime);
    }

    public static boolean isEffectiveDate(Date checkTime, Date startTime, Date endTime) {

        if (checkTime.getTime() == startTime.getTime()
                || checkTime.getTime() == endTime.getTime()) {
            return true;
        }


        Calendar date = Calendar.getInstance();
        date.setTime(checkTime);

        Calendar begin = Calendar.getInstance();
        begin.setTime(startTime);

        Calendar end = Calendar.getInstance();
        end.setTime(endTime);

        return date.after(begin) && date.before(end);
    }
}
