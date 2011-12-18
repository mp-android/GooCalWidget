package net.madroom.gcw.entity;

import java.util.ArrayList;

import net.madroom.common.CommonUtil;
import net.madroom.gcw.conf.PrefConf;
import net.madroom.gcw.conf.WidgetConf;
import net.madroom.gcw.util.CalendarUtil;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.preference.PreferenceManager;
import android.provider.Calendar.Instances;
import android.text.TextUtils;

public class InstancesEntity {

    /**
    originalEvent
    ownerAccount
    endDay
    visibility
    endMinute
    rrule
    event_id
    lastDate
    hasAlarm
    guestsCanModify
    transparency
    rdate
    exrule
    guestsCanSeeGuests
    title
    dtstart
    selected
    timezone
    _id
    hasAttendeeData
    commentsUri
    startDay
    description
    htmlUri
    end
    startMinute
    hasExtendedProperties
    calendar_id
    eventLocation
    dtend
    access_level
    allDay
    organizer
    originalAllDay
    deleted
    url
    begin
    originalInstanceTime
    duration
    color
    selfAttendeeStatus
    guestsCanInviteOthers
    exdate
    eventStatus
    eventTimezone
    **/

    private final String[] PROJECTION = new String[] {
        Instances.BEGIN,
        Instances.END,
        Instances.TITLE,
        Instances.ALL_DAY,
        Instances.COLOR,
        Instances.CALENDAR_ID
    };

    private long begin;
    private long end;
    private String title;
    private boolean isAllDay;

    private int beginYear;
    private int beginMonth;
    private String beginMonthDisp;
    private int beginDate;
    private int beginHourOfDay;
    private int beginMinute;

    private int endYear;
    private int endMonth;
    private String endMonthDisp;
    private int endDate;
    private int endHourOfDay;
    private int endMinute;

    private int color;
    private boolean isHoliday;

    public ArrayList<InstancesEntity> instancesEntities = new ArrayList<InstancesEntity>();

    public ArrayList<InstancesEntity> getInstancesEntities(Context context, long begin, long end) {
        if(CommonUtil.isInstalled(context, WidgetConf.ANDROID_CALENDAR_PACKAGE_NAME)) {
            return getInstancesEntitiesByDB(context, begin, end);
        } else {
            return getInstancesEntitiesByDummy();
        }
    }


    public ArrayList<InstancesEntity> getInstancesEntitiesByDB(Context context, long begin, long end) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);

        String where = "(" + Instances.ALL_DAY + "=0" +")or" + "(("+ Instances.ALL_DAY + "=1)and(" +
            "(" + begin + "<=" + Instances.BEGIN +" and " + Instances.BEGIN + "<" + end + ") or " +
            "(" + Instances.BEGIN + "<" + begin +" and " + end + "<=" + Instances.END + ")))";

        String orderby =
            "case " + Instances.ALL_DAY + " when 1 then 1 else 2 end asc," + Instances.SORT_CALENDAR_VIEW;

        String holidayCalendarId =
            pref.getString(PrefConf.KEY_HOLIDAY_CALENDAR, PrefConf.DEF_HOLIDAY_CALENDAR);

        if(!TextUtils.isEmpty(holidayCalendarId))
            orderby =
                "case " + Instances.CALENDAR_ID + " when " + holidayCalendarId + " then 1 else 2 end asc," + orderby;

        Cursor c = Instances.query(context.getContentResolver(), PROJECTION,
                begin, end, where, orderby);

        if(c.moveToFirst()) {
            do {
                InstancesEntity instancesEntity = new InstancesEntity();
                instancesEntity.setBegin(c.getLong(c.getColumnIndex(Instances.BEGIN)));
                instancesEntity.setEnd(c.getLong(c.getColumnIndex(Instances.END)));
                instancesEntity.setTitle(c.getString(c.getColumnIndex(Instances.TITLE)));
                instancesEntity.setAllDay(c.getInt(c.getColumnIndex(Instances.ALL_DAY))!=0);
                instancesEntity.setColor(c.getInt(c.getColumnIndex(Instances.COLOR)));
                instancesEntity.setHoliday(c.getString(c.getColumnIndex(Instances.CALENDAR_ID)).equals(holidayCalendarId));

                CalendarUtil calUtil = new CalendarUtil(
                        false, instancesEntity.getBegin());
                instancesEntity.setBeginYear(calUtil.mYear);
                instancesEntity.setBeginMonth(calUtil.mMonth);
                instancesEntity.setBeginMonthDisp((calUtil.mMonth + 1) +"");
                instancesEntity.setBeginDate(calUtil.mDate);
                instancesEntity.setBeginHourOfDay(calUtil.mHourOfDay);
                instancesEntity.setBeginMinute(calUtil.mMinute);

                calUtil = new CalendarUtil(false, instancesEntity.getEnd());
                instancesEntity.setEndYear(calUtil.mYear);
                instancesEntity.setEndMonth(calUtil.mMonth);
                instancesEntity.setEndMonthDisp((calUtil.mMonth + 1) +"");
                instancesEntity.setEndDate(calUtil.mDate);
                instancesEntity.setEndHourOfDay(calUtil.mHourOfDay);
                instancesEntity.setEndMinute(calUtil.mMinute);

                instancesEntities.add(instancesEntity);

            } while (c.moveToNext());
        }
        c.close();
        return instancesEntities;
    }

    public ArrayList<InstancesEntity> getInstancesEntitiesByDummy() {
        return instancesEntities;
    }


    public long getBegin() {
        return begin;
    }


    public void setBegin(long begin) {
        this.begin = begin;
    }


    public long getEnd() {
        return end;
    }


    public void setEnd(long end) {
        this.end = end;
    }


    public String getTitle() {
        return title;
    }


    public void setTitle(String title) {
        this.title = title;
    }


    public boolean isAllDay() {
        return isAllDay;
    }


    public void setAllDay(boolean isAllDay) {
        this.isAllDay = isAllDay;
    }


    public int getBeginYear() {
        return beginYear;
    }


    public void setBeginYear(int beginYear) {
        this.beginYear = beginYear;
    }


    public int getBeginMonth() {
        return beginMonth;
    }


    public void setBeginMonth(int beginMonth) {
        this.beginMonth = beginMonth;
    }


    public String getBeginMonthDisp() {
        return beginMonthDisp;
    }


    public void setBeginMonthDisp(String beginMonthDisp) {
        this.beginMonthDisp = beginMonthDisp;
    }


    public int getBeginDate() {
        return beginDate;
    }


    public void setBeginDate(int beginDate) {
        this.beginDate = beginDate;
    }


    public int getBeginHourOfDay() {
        return beginHourOfDay;
    }


    public void setBeginHourOfDay(int beginHourOfDay) {
        this.beginHourOfDay = beginHourOfDay;
    }


    public int getBeginMinute() {
        return beginMinute;
    }


    public void setBeginMinute(int beginMinute) {
        this.beginMinute = beginMinute;
    }


    public int getEndYear() {
        return endYear;
    }


    public void setEndYear(int endYear) {
        this.endYear = endYear;
    }


    public int getEndMonth() {
        return endMonth;
    }


    public void setEndMonth(int endMonth) {
        this.endMonth = endMonth;
    }


    public String getEndMonthDisp() {
        return endMonthDisp;
    }


    public void setEndMonthDisp(String endMonthDisp) {
        this.endMonthDisp = endMonthDisp;
    }


    public int getEndDate() {
        return endDate;
    }


    public void setEndDate(int endDate) {
        this.endDate = endDate;
    }


    public int getEndHourOfDay() {
        return endHourOfDay;
    }


    public void setEndHourOfDay(int endHourOfDay) {
        this.endHourOfDay = endHourOfDay;
    }


    public int getEndMinute() {
        return endMinute;
    }


    public void setEndMinute(int endMinute) {
        this.endMinute = endMinute;
    }


    public int getColor() {
        return color;
    }


    public void setColor(int color) {
        this.color = color;
    }


    public boolean isHoliday() {
        return isHoliday;
    }


    public void setHoliday(boolean isHoliday) {
        this.isHoliday = isHoliday;
    }


    public ArrayList<InstancesEntity> getInstancesEntities() {
        return instancesEntities;
    }


    public void setInstancesEntities(ArrayList<InstancesEntity> instancesEntities) {
        this.instancesEntities = instancesEntities;
    }


    public String[] getPROJECTION() {
        return PROJECTION;
    }
}
