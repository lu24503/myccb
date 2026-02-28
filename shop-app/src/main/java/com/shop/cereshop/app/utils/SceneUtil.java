/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.utils;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import com.shop.cereshop.commons.constant.IntegerEnum;
import com.shop.cereshop.commons.utils.TimeUtils;
import org.apache.commons.lang.StringUtils;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * 场景营销工具类
 */
public class SceneUtil {

    public static boolean matchScene(Integer sceneType, Integer sceneTimeType, String sceneTime, String birthday, boolean matchTimeAllDay) {
        Calendar cal = Calendar.getInstance();
        boolean matched = false;
        if (IntegerEnum.SCENE_TYPE_FESTIVAL.getCode().equals(sceneType)) {
            return true;
        } else if (IntegerEnum.SCENE_TYPE_MEMBER.getCode().equals(sceneType)) {
            int dayOfMonth = cal.get(Calendar.DAY_OF_MONTH);
            int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);

            String[] timeArr = sceneTime.split("-");
            if (IntegerEnum.SCENE_TIME_MONTH.getCode().equals(sceneTimeType)) {
                int startDate = Integer.parseInt(timeArr[0]);
                int endDate = Integer.parseInt(timeArr[1]);
                if (startDate <= dayOfMonth && dayOfMonth <= endDate) {
                    matched = true;
                }
            } else if (IntegerEnum.SCENE_TIME_WEEK.getCode().equals(sceneTimeType)) {
                List<String> weekDayList = CollectionUtil.toList(timeArr);
                if ((dayOfWeek == 1 && weekDayList.contains("7")) || weekDayList.contains(String.valueOf(dayOfWeek - 1))) {
                    matched = true;
                }
            } else if (IntegerEnum.SCENE_TIME_DAY.getCode().equals(sceneTimeType)) {
                String hms = TimeUtils.hhmmss();
                if (matchTimeAllDay || (timeArr[0].compareTo(hms) <= 0) && hms.compareTo(timeArr[1]) <= 0) {
                    matched = true;
                }
            }
        } else if (StringUtils.isNotBlank(birthday)) {
            if (IntegerEnum.SCENE_TIME_BIRTHDAY.getCode().equals(sceneTimeType)
                    && matchToday(birthday, cal)) {
                matched = true;
            }
            if (IntegerEnum.SCENE_TIME_BIRTHDAY_WEEK.getCode().equals(sceneTimeType)
                    && matchThisWeek(birthday, cal)) {
                matched = true;
            }
            if (IntegerEnum.SCENE_TIME_BIRTHDAY_MONTH.getCode().equals(sceneTimeType)
                    && matchThisMonth(birthday, cal)) {
                matched = true;
            }
        }
        return matched;
    }

    private static boolean matchToday(String birthday, Calendar cal) {
        return birthday.substring(5).equals(DateUtil.format(cal.getTime(), "MM-dd"));
    }

    private static boolean matchThisWeek(String birthday, Calendar cal) {
        //做好备份，接口返回前重新设置回去
        Date timeBackUp = cal.getTime();
        int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
        if (dayOfWeek == 1) {
            dayOfWeek = 8;
        }
        cal.add(Calendar.DATE, -1 * (dayOfWeek - 2));
        // 当前时间对应的周的周一
        String startDateStr = DateUtil.format(cal.getTime(), "yyyy-MM-dd");
        cal.add(Calendar.DATE, 6);
        // 当前时间对应的周的周日
        String endDateStr = DateUtil.format(cal.getTime(), "yyyy-MM-dd");
        cal.setTime(timeBackUp);
        //把birthday的年份改为startDateStr同一年
        birthday = startDateStr.substring(0,4) + birthday.substring(4);
        if (birthday.compareTo(startDateStr) >= 0 && birthday.compareTo(endDateStr) <= 0) {
            return true;
        }
        //startDateStr 和 endDateStr 可能是跨年的，所以birthday需要再改成 endDateStr 同一个年再判断一次
        if (!startDateStr.substring(0,4).equals(endDateStr.substring(0,4))) {
            birthday = endDateStr.substring(0,4) + birthday.substring(4);
            if (birthday.compareTo(startDateStr) >= 0 && birthday.compareTo(endDateStr) <= 0) {
                return true;
            }
        }
        return false;
    }

    private static boolean matchThisMonth(String birthday, Calendar cal) {
        return birthday.substring(5,7).equals(DateUtil.format(cal.getTime(), "MM"));
    }

    public static void main(String[] args) {
        String birthday = "1998-09-12";
        Calendar cal = Calendar.getInstance();
        birthday =  cal.get(Calendar.YEAR) + birthday.substring(4);
        System.out.println(birthday);
    }

}
