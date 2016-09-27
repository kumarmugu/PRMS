/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sg.edu.nus.iss.phoenix.util;

import java.sql.Time;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;


/**
 *
 * @author Mugunthan
 */
public class DateUtil {
   // private static Timestamp Timestamp;

    public static Date getStartDateOfWeek(String year, String week) {
        Date date = null;
        int y;
        int w;
        Calendar calendar = Calendar.getInstance();
        if (year != null && year.length() == 4 && year.matches("^-?\\d+$") && week != null && week.matches("^-?\\d+$")) {
            y = Integer.parseInt(year);
            w = Integer.parseInt(week);
        } else {
            y = calendar.get(Calendar.YEAR);
            w = calendar.get(Calendar.WEEK_OF_YEAR);
        }
        calendar.clear();
        calendar.set(Calendar.WEEK_OF_YEAR, w);
        calendar.set(Calendar.YEAR, y);
        // Now get the first day of week.
        date = calendar.getTime();
        return date;
    }
    
    public static int getNoOfWeeks(int year) {       
   
        Calendar cal = Calendar.getInstance();

        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, Calendar.DECEMBER);
        cal.set(Calendar.DAY_OF_MONTH, 31);
        int ordinalDay = cal.get(Calendar.DAY_OF_YEAR);
        int weekDay = cal.get(Calendar.DAY_OF_WEEK) - 1; // Sunday = 0
        int numberOfWeeks = (ordinalDay - weekDay + 10) / 7;
            return numberOfWeeks;
    }
    
     public static Date getFirstDayOfYear(int year) {       
   
        Calendar cal = Calendar.getInstance();
         Date date = null;
            cal.set(Calendar.YEAR, year);
            cal.set(Calendar.MONTH, Calendar.JANUARY);
            cal.set(Calendar.DAY_OF_MONTH, 1);
            cal.set(Calendar.HOUR,0);
            cal.set(Calendar.MINUTE,0);
             cal.set(Calendar.SECOND,0);
            date = cal.getTime();
        return date;
    }
     
      public static Time getTimeDiff(String starttime,String endtime) throws ParseException {       
   
        String HrMin = "HH:mm";
         String HrMinSec = "HH:mm:ss";
         SimpleDateFormat hmformatter = new SimpleDateFormat(HrMin);
         SimpleDateFormat hmsformatter = new SimpleDateFormat(HrMinSec);
         
         Date startDt,endDt= null;
         
         startDt= hmformatter.parse(starttime);
         endDt=hmformatter.parse(endtime);
         
         long datediff = endDt.getTime()- startDt.getTime();
         System.out.println(datediff);
         
         String hms = String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(datediff),
         TimeUnit.MILLISECONDS.toMinutes(datediff) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(datediff)),
         TimeUnit.MILLISECONDS.toSeconds(datediff) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(datediff)));
         
         Time timediff = Time.valueOf(hms);
       
        return timediff;
    }
      
       public static Timestamp getTimestamp(Date date) {
        return date == null ? null : new java.sql.Timestamp(date.getTime());
    }
     
     public static void main(String args[]) throws ParseException{
         
         String HrMin = "HH:mm";
         String HrMinSec = "HH:mm:ss";
         SimpleDateFormat hmformatter = new SimpleDateFormat(HrMin);
         SimpleDateFormat hmsformatter = new SimpleDateFormat(HrMinSec);
         
         Date d,d2= null;
         
         d= hmformatter.parse("20:30");
         d2=hmformatter.parse("21:00");
         
         long datediff = d2.getTime() -d.getTime();
         //System.out.println(datediff);
         
         String hms = String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(datediff),
            TimeUnit.MILLISECONDS.toMinutes(datediff) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(datediff)),
            TimeUnit.MILLISECONDS.toSeconds(datediff) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(datediff)));
         
         Time t = Time.valueOf(hms);
         
         Date dd= new Date();
         Timestamp tt =new java.sql.Timestamp(dd.getTime());
    System.out.println("*********************");
    System.out.println("TimeStamp: "+ tt);
         
         
     }
}
