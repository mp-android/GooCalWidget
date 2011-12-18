package net.madroom.gcw.util;

import java.util.Calendar;

import net.madroom.gcw.conf.WidgetConf;

public class CalendarUtil {

    public final Calendar mCal;
    public int mType;

    public int mDayOfWeek;

    public long mUnixTimestamp;
    public int mYear;
    public int mMonth;
    public String mMonthDisp;
    public int mDate;
    public int mHourOfDay;
    public int mMinute;
    public int mSecond;
    public int mMillisecond;


    public long mTodayUnixTimestamp;
    public int mTodayYear;
    public int mTodayMonth;
    public String mTodayMonthDisp;

    public CalendarUtil(boolean isZero, int type) {
        mCal = Calendar.getInstance();
        initCommon(isZero);
        initValue(type);
    }

    public CalendarUtil(boolean isZero, int type, int firstDayOfWeek) {
        mCal = Calendar.getInstance();
        initCommon(isZero);

        switch (type) {
        case WidgetConf.TYPE_MONTH:
            initValue4Month(firstDayOfWeek);
            break;
        case WidgetConf.TYPE_WEEK:
            initValue4Week(firstDayOfWeek);
            break;
        case WidgetConf.TYPE_DAY:
            break;
        }

        initValue(type);
    }

    public CalendarUtil(boolean isZero, long unixTimeStamp) {
        mCal = Calendar.getInstance();
        mCal.setTimeInMillis(unixTimeStamp);
        initCommon(isZero);

        initValue(WidgetConf.TYPE_DAY);
    }

    private void initCommon(boolean isZero) {
        if(isZero) {
            mCal.set(Calendar.HOUR_OF_DAY, 0);
            mCal.set(Calendar.MINUTE, 0);
            mCal.set(Calendar.SECOND, 0);
            mCal.set(Calendar.MILLISECOND, 0);
        }
        mTodayYear = mCal.get(Calendar.YEAR);
        mTodayMonth = mCal.get(Calendar.MONTH);
        mTodayMonthDisp = (mTodayMonth + 1) + "";
        mTodayUnixTimestamp  = mCal.getTimeInMillis();
    }

    private void initValue4Month(int firstDayOfWeek) {
        mCal.set(Calendar.DATE, 1);
        final int addDate = mCal.get(Calendar.DAY_OF_WEEK) - firstDayOfWeek;
        if(0<=addDate) {
            mCal.add(Calendar.DATE, -addDate);
        } else {
            mCal.add(Calendar.DATE, -(addDate + WidgetConf.DEFAULT_WEEKDAY_NUM));
        }
    }

    private void initValue4Week(int firstDayOfWeek) {
        final int addDate = mCal.get(Calendar.DAY_OF_WEEK) - firstDayOfWeek;
        if(0<=addDate) {
            mCal.add(Calendar.DATE, -addDate);
        } else {
            mCal.add(Calendar.DATE, -(addDate + WidgetConf.DEFAULT_WEEKDAY_NUM));
        }
    }

    private void initValue(int type) {
        mType = type;
        mYear = mCal.get(Calendar.YEAR);
        mMonth = mCal.get(Calendar.MONTH);
        mMonthDisp = (mMonth + 1) + "";
        mDate = mCal.get(Calendar.DATE);
        mHourOfDay = mCal.get(Calendar.HOUR_OF_DAY);
        mMinute = mCal.get(Calendar.MINUTE);
        mSecond = mCal.get(Calendar.SECOND);
        mMillisecond = mCal.get(Calendar.MILLISECOND);
        mDayOfWeek = mCal.get(Calendar.DAY_OF_WEEK);
        mUnixTimestamp = mCal.getTimeInMillis();
    }


    public void initCalendar() {
        mCal.set(Calendar.YEAR, mYear);
        mCal.set(Calendar.MONTH, mMonth);
        mCal.set(Calendar.DATE, mDate);
        mCal.set(Calendar.HOUR_OF_DAY, mHourOfDay);
        mCal.set(Calendar.MINUTE, mMinute);
        mCal.set(Calendar.SECOND, mSecond);
        mCal.set(Calendar.MILLISECOND, mMillisecond);
    }

    public long getUnixTimestamp() {
        initCalendar();
        return mCal.getTimeInMillis();
    }

    public long getUnixTimestampByAddDate(int addDate) {
        initCalendar();
        mCal.add(Calendar.DATE, addDate);
        return mCal.getTimeInMillis();
    }

    public long getUnixTimestampByAddSecond(int addSecond) {
        initCalendar();
        mCal.add(Calendar.SECOND, addSecond);
        return mCal.getTimeInMillis();
    }

    public int getYear(int addDate) {
        initCalendar();
        mCal.add(Calendar.DATE, addDate);
        return mCal.get(Calendar.YEAR);
    }

    public int getMonth(int addDate) {
        initCalendar();
        mCal.add(Calendar.DATE, addDate);
        return mCal.get(Calendar.MONTH);
    }

    public String getMonthDisp(int addDate) {
        return (getMonth(addDate) + 1) + "";
    }

    public int getDate(int addDate) {
        initCalendar();
        mCal.add(Calendar.DATE, addDate);
        return mCal.get(Calendar.DATE);
    }

}
