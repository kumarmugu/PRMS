/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sg.edu.nus.iss.phoenix.util;

import java.sql.Time;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author Mugunthan
 */
public class DateUtil {
        private static final Map<Integer, String> myDayMap = new HashMap<Integer, String>() {
        {
            put(0, "Unknown");
            put(1, "Sunday");
            put(2, "Monday");
            put(3, "Tuesday");
            put(4, "Wednesday");
            put(5, "Thursday");
            put(6, "Friday");
            put(7, "Saturday");
        }
    };
    // private static Timestamp Timestamp;

    public static Date getStartDateOfWeek(String year, String week) {
        int y;
        int w;
        if (year != null && year.length() == 4 && year.matches("^-?\\d+$") && week != null && week.matches("^-?\\d+$")) {
            y = Integer.parseInt(year);
            w = Integer.parseInt(week);
        } else {
            Calendar calendar = Calendar.getInstance();

            y = calendar.get(Calendar.YEAR);
            w = calendar.get(Calendar.WEEK_OF_YEAR);
        }
        return getStartDateOfWeek(y, w);
    }

    public static Date getStartDateOfWeek(int y, int w) {
        Calendar calendar = Calendar.getInstance();

        calendar.clear();
        calendar.set(Calendar.WEEK_OF_YEAR, w);
        calendar.set(Calendar.YEAR, y);
        // Now get the first day of week.
        Date date = calendar.getTime();
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
        cal.set(Calendar.HOUR, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        date = cal.getTime();
        return date;
    }

    public static Time getTimeDiff(String starttime, String endtime) throws ParseException {

        String HrMin = "HH:mm";
        String HrMinSec = "HH:mm:ss";
        SimpleDateFormat hmformatter = new SimpleDateFormat(HrMin);
        SimpleDateFormat hmsformatter = new SimpleDateFormat(HrMinSec);

        Date startDt, endDt = null;

        startDt = hmformatter.parse(starttime);
        endDt = hmformatter.parse(endtime);

        long datediff = endDt.getTime() - startDt.getTime();
        System.out.println(datediff);

        String hms = String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(datediff),
                TimeUnit.MILLISECONDS.toMinutes(datediff) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(datediff)),
                TimeUnit.MILLISECONDS.toSeconds(datediff) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(datediff)));

        Time timediff = Time.valueOf(hms);

        return timediff;
    }
    public static Date getDateDiff(Date date1, Date date2) {
        long diffInMillies = date2.getTime() - date1.getTime();
        long timeDif = TimeUnit.SECONDS.convert(diffInMillies, TimeUnit.MILLISECONDS);
        Calendar cal = Calendar.getInstance();
        cal.set(0, 0, 0, (int) timeDif / 3600, (int) timeDif % 3600 / 60, (int) timeDif % 60);
        return cal.getTime();        
    }
    
    public static Timestamp getTimestamp(Date date) {
        return date == null ? null : new java.sql.Timestamp(date.getTime());
    }

    public static Date getDate(String dateString, String format) {
        DateFormat df = new SimpleDateFormat(format); //"yyyy-MM-dd"
        Date date = null;
        try {
            date = df.parse(dateString);
        } catch (ParseException e) {
            //e.printStackTrace();
        }
        return date;
    }

    public static Date getTime(String timeString, String format) {
        DateFormat df = new SimpleDateFormat(format); //"HH:mm"
        Date time = null;
        try {
            time = df.parse(timeString);
        } catch (ParseException e) {
            //e.printStackTrace();
        }
        return time;
    }

    public static Date AddDateTime(Date d1, Date durationLessThan1Day) {
        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(d1);
        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(durationLessThan1Day);
        //cal1.add(Calendar.YEAR, cal2.get(Calendar.YEAR) - 1970);
        // cal1.add(Calendar.MONTH, cal2.get(Calendar.MONTH));
        // cal1.add(Calendar.DAY_OF_MONTH, cal2.get(Calendar.DAY_OF_MONTH) );
        cal1.add(Calendar.HOUR_OF_DAY, cal2.get(Calendar.HOUR_OF_DAY));
        cal1.add(Calendar.MINUTE, cal2.get(Calendar.MINUTE));
        cal1.add(Calendar.SECOND, cal2.get(Calendar.SECOND));
        cal1.add(Calendar.MILLISECOND, cal2.get(Calendar.MILLISECOND));
        return cal1.getTime();
    }
    
    public static String getDayOfWeek(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return myDayMap.get(cal.get(Calendar.DAY_OF_WEEK));
    }
    
    public static int getWeekOfYear(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(Calendar.WEEK_OF_YEAR);
    }
    
    public static int getYear(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(Calendar.YEAR);
    }

    public static void main(String args[]) throws ParseException {

        String HrMin = "HH:mm";
        String HrMinSec = "HH:mm:ss";
        SimpleDateFormat hmformatter = new SimpleDateFormat(HrMin);
        SimpleDateFormat hmsformatter = new SimpleDateFormat(HrMinSec);

        Date d, d2 = null;

        d = hmformatter.parse("20:30");
        d2 = hmformatter.parse("21:00");

        long datediff = d2.getTime() - d.getTime();
        //System.out.println(datediff);

        String hms = String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(datediff),
                TimeUnit.MILLISECONDS.toMinutes(datediff) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(datediff)),
                TimeUnit.MILLISECONDS.toSeconds(datediff) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(datediff)));

        Time t = Time.valueOf(hms);

        Date dd = new Date();
        Timestamp tt = new java.sql.Timestamp(dd.getTime());
        System.out.println("*********************");
        System.out.println("TimeStamp: " + tt);

    }
}
