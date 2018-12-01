package test.andy.tubeswisata.util;

import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;


public class DateUtil {

    public static final String getCurrentDateIndex() {
        SimpleDateFormat sdf = new SimpleDateFormat("EEEE", Locale.US);
        String date = sdf.format(new Date());

        switch (date) {
            case "Sunday" :
                return "0";
            case "Monday" :
                return "1";
            case "Tuesday" :
                return "2";
            case "Wednesday" :
                return "3";
            case "Thursday" :
                return "4";
            case "Friday" :
                return "5";
            case "Saturday" :
                return "6";
        }

        return "";
    }

    public static final boolean betweenDate(String startDate, String endDate) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        try {
            Date start = sdf.parse(startDate);
            Date end = sdf.parse(endDate);
            Date current = new Date();

            if (current.getTime() >= start.getTime() && current.getTime() <= end.getTime()) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public static String getCurrentDate(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(new Date());
    }

    public static String getCurrentSimpleDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(new Date());
    }

    public static String getCurrentTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        return sdf.format(new Date());
    }

    public static List<Date> getDaysBetweenDates(Date startdate, Date enddate) {
        List<Date> dates = new ArrayList<>();
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(startdate);

        while (calendar.getTime().before(enddate))
        {
            Date result = calendar.getTime();
            dates.add(result);
            calendar.add(Calendar.DATE, 1);
        }
        return dates;
    }

    public static Date convertStringToDate(String format, String date){
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        Date dateToFormat = null;
        try {
            dateToFormat = sdf.parse(date);
        } catch (Exception e){
            e.printStackTrace();
        }
        return dateToFormat;
    }

    public static String convertDatetoString(String format, Date date){
        return new SimpleDateFormat(format).format(date);
    }

    public static final String changeDateFormat(String date, String format1, String format2){
        try {
            SimpleDateFormat sdf1 = new SimpleDateFormat(format1);
            SimpleDateFormat sdf2 = new SimpleDateFormat(format2);
            Date d = sdf1.parse(date);
            return sdf2.format(d);
        } catch(Exception e){
            Log.e("DateUtil",Log.getStackTraceString(e));
        }
        return "-1";
    }

    public static boolean isSameDay(long time1, long time2){
        return (time1-time2) <= (1000 * 60 * 60 * 24);
    }
}
