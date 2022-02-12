package com.example.countdayoflove.logic;

import java.util.Calendar;

public class GetDay implements GetDayInterface {
    //TODO SETUP INITIAL VARIABLE
    Calendar cal = Calendar.getInstance();
    private final int current_year = cal.get(Calendar.YEAR);
    private final int current_month = (cal.get(Calendar.MONTH)) + 1;
    private final int current_day = cal.get(Calendar.DAY_OF_MONTH);


    private boolean checkYear(int year) {
        // false = nhuan
        if (year % 100 == 0) {
            return year % 400 != 0;
        } else {
            return year % 4 != 0;
        }
    }
    //TODO GET DAY FROM YEAR
    @Override
    public int getdayfromyear(int year) {
        int temp = 0;
        for (int i = year; i < current_year; i++) {
            if (i % 100 == 0) {
                if (i % 400 == 0) {
                    temp += 366;
                } else {
                    temp += 365;
                }
            } else {
                if (i % 4 == 0) {
                    temp += 366;
                } else {
                    temp += 365;
                }
            }
        }
        return temp;
    }

    public int checkMonth(int month, boolean year) {
        // year = false (nhuan)
        int day = 0;
        if (!year) {
            if (month == 2) {
                day = 29;
            }
        } else {
            if (month == 2) {
                day = 28;
            }
        }
        if (month == 1) {
            day = 31;
        } else if (month == 3) {
            day = 31;
        } else if (month == 4) {
            day = 30;
        } else if (month == 5) {
            day = 31;
        } else if (month == 6) {
            day = 30;
        } else if (month == 7) {
            day = 31;
        } else if (month == 8) {
            day = 31;
        } else if (month == 9) {
            day = 30;
        } else if (month == 10) {
            day = 31;
        } else if (month == 11) {
            day = 30;
        } else if (month == 12) {
            day = 31;
        }
        return day;

    }
    //TODO GET DAY FROM MONTH
    @Override
    public int getdayfrommonth(int day, int month, int year) {
        // current year
        int day_temp = 0;
        int day_temp_current = 0;
        for (int i = 1; i < month; i++) {
            day_temp +=checkMonth(i, checkYear(year));
        }
        day_temp += day;
        for (int i = 1; i < current_month; i++) {
            day_temp_current += checkMonth(i, checkYear(current_year));
        }
        day_temp_current += current_day;
        return day_temp_current - day_temp;
    }
    //TODO GET DAY OF LOE FROM DATE
    @Override
    public int finalyday(int day, int month, int year) {
        int real_day = getdayfrommonth(day, month, year);
        int day_from_year = getdayfromyear(year);
        int finally_day;
        if(real_day < 0){
            finally_day = day_from_year - (- real_day);
        }else{
            finally_day = day_from_year + real_day;
        }
        return finally_day;
    }

}
