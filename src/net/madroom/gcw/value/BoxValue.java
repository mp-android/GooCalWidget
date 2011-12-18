package net.madroom.gcw.value;

import java.util.ArrayList;

import net.madroom.gcw.entity.InstancesEntity;

public class BoxValue {

    private ArrayList<InstancesEntity> instancesEntities = new ArrayList<InstancesEntity>();
    private long begin;
    private long end;
    private int dayOfWeek;
    private int year;
    private int month;
    private String monthDisp;
    private int date;

    private boolean isToday;
    private boolean isNextMonth;
    private boolean isPrevMonth;
    private boolean isCurrentMonth;
    public ArrayList<InstancesEntity> getInstancesEntities() {
        return instancesEntities;
    }
    public void setInstancesEntities(ArrayList<InstancesEntity> instancesEntities) {
        this.instancesEntities = instancesEntities;
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
    public int getDayOfWeek() {
        return dayOfWeek;
    }
    public void setDayOfWeek(int dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }
    public int getYear() {
        return year;
    }
    public void setYear(int year) {
        this.year = year;
    }
    public int getMonth() {
        return month;
    }
    public void setMonth(int month) {
        this.month = month;
    }
    public String getMonthDisp() {
        return monthDisp;
    }
    public void setMonthDisp(String monthDisp) {
        this.monthDisp = monthDisp;
    }
    public int getDate() {
        return date;
    }
    public void setDate(int date) {
        this.date = date;
    }
    public boolean isToday() {
        return isToday;
    }
    public void setToday(boolean isToday) {
        this.isToday = isToday;
    }
    public boolean isNextMonth() {
        return isNextMonth;
    }
    public void setNextMonth(boolean isNextMonth) {
        this.isNextMonth = isNextMonth;
    }
    public boolean isPrevMonth() {
        return isPrevMonth;
    }
    public void setPrevMonth(boolean isPrevMonth) {
        this.isPrevMonth = isPrevMonth;
    }
    public boolean isCurrentMonth() {
        return isCurrentMonth;
    }
    public void setCurrentMonth(boolean isCurrentMonth) {
        this.isCurrentMonth = isCurrentMonth;
    }

}
