package com.iiovecherry.util.dto;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang.StringUtils;

/**
 * 
 * @author D.I.hunter
 * @ClassName: TimeUtil.java
 * @date 2014-12-11
 * @Description:The interface to listener DTO and entity convert
 */
public class TimeUtil {
    /**
     * Model date with date no time
     */
    public static final String DATE_MATCH="yyyy-MM-dd";
    /**
     * Model date with date have time
     */
    public static final String DATE_TIME_MATCH="yyyy-MM-dd HH:mm:ss";
    /**
     * Model time with no date
     */
    public static final String ONLY_TIME_MACTH="HH:mm:ss";
    /**
     * Tools convert date to string  
     * @param date
     * @param pattern "yyyy-MM-dd"  "yyyy-MM-dd HH:mm:ss"
     * @return
     */
    public static String dateToString(Date date,String pattern){
        SimpleDateFormat sdf=new SimpleDateFormat(pattern);
        String str =sdf.format(date);
        return str;
    }
    /**
     * Tools convert string to date 
     * @param dateStr
     * @param pattern  "yyyy-MM-dd"  "yyyy-MM-dd HH:mm:ss"
     * @return
     */
    public static Date strToDate(String dateStr,String pattern){
        SimpleDateFormat sdf=new SimpleDateFormat(pattern);
        try {
            Date date = sdf.parse(dateStr);
            return date;
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }
    /**
     * Tools get date time with String 
     * @param strDate date String
     * @param isMax true: get current date last seconds ,false : get current date first seconds
     * @return
     */
	public static Date strToDateTime(String strDate,boolean isMax){
		if(StringUtils.isBlank(strDate)) return null;		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		if(strDate.length()>=10){
			if(isMax){
				strDate=strDate.substring(0, 10)+" 23:59:59";
			}else{
				strDate=strDate.substring(0, 10)+" 00:00:00";
			}
		}
		try {
			Date date = sdf.parse(strDate);
			return date;
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
	}

}
