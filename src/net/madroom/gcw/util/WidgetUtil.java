package net.madroom.gcw.util;

import java.util.ArrayList;
import java.util.Calendar;

import net.madroom.common.CommonUtil;
import net.madroom.gcw.R;
import net.madroom.gcw.conf.PrefConf;
import net.madroom.gcw.conf.WidgetConf;
import net.madroom.gcw.entity.InstancesEntity;
import net.madroom.gcw.value.BoxValue;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.RemoteViews;


public class WidgetUtil {
    private static final int BGCOLOR_RED = 2;
    private static final int BGCOLOR_BLUE = 1;
    private static final int BGCOLOR_DEFAULT = 0;

    public static void removeAllViews(int type, RemoteViews remoteViews) {
        switch (type) {
        case WidgetConf.TYPE_MONTH:
            remoteViews.removeAllViews(R.id.header_row);
            remoteViews.removeAllViews(R.id.box_row_1);
            remoteViews.removeAllViews(R.id.box_row_2);
            remoteViews.removeAllViews(R.id.box_row_3);
            remoteViews.removeAllViews(R.id.box_row_4);
            remoteViews.removeAllViews(R.id.box_row_5);
            remoteViews.removeAllViews(R.id.box_row_6);
            break;
        case WidgetConf.TYPE_WEEK:
            remoteViews.removeAllViews(R.id.header_row);
            remoteViews.removeAllViews(R.id.box_row_1);
            break;
        case WidgetConf.TYPE_DAY:
            remoteViews.removeAllViews(R.id.header_row);
            remoteViews.removeAllViews(R.id.box_row_1);
            break;
        }
    }

    public static BoxValue getBoxValue(Context context, CalendarUtil calUtil, int addDate, int dayOfWeek) {
        final BoxValue boxValue = new BoxValue();
        boxValue.setBegin(calUtil.getUnixTimestampByAddDate(addDate));
        boxValue.setEnd(calUtil.getUnixTimestampByAddDate(addDate + 1));
        boxValue.setDayOfWeek(dayOfWeek);
        boxValue.setYear(calUtil.getYear(addDate));
        boxValue.setMonth(calUtil.getMonth(addDate));
        boxValue.setMonthDisp(calUtil.getMonthDisp(addDate));
        boxValue.setDate(calUtil.getDate(addDate));
        boxValue.setToday(calUtil.mTodayUnixTimestamp == boxValue.getBegin());

        boxValue.setPrevMonth(
                (boxValue.getYear()==calUtil.mTodayYear && boxValue.getMonth()==(calUtil.mTodayMonth-1)) ||
                (boxValue.getYear()==calUtil.mTodayYear-1 && boxValue.getMonth()==11));

        boxValue.setNextMonth(
                (boxValue.getYear()==calUtil.mTodayYear && boxValue.getMonth()==(calUtil.mTodayMonth+1)) ||
                (boxValue.getYear()==calUtil.mTodayYear+1 && boxValue.getMonth()==0));

        boxValue.setCurrentMonth(
                boxValue.getYear()==calUtil.mTodayYear && boxValue.getMonth()==calUtil.mTodayMonth);

        boxValue.setInstancesEntities(
                new InstancesEntity().getInstancesEntities(context,
                        boxValue.getBegin(),
                        (boxValue.getEnd()-1)));

        return boxValue;
    }

    public static int  getNextDayOfWeek(int dayOfWeek) {
        dayOfWeek++;
        if(WidgetConf.DEFAULT_WEEKDAY_NUM < dayOfWeek) {
            dayOfWeek = dayOfWeek - WidgetConf.DEFAULT_WEEKDAY_NUM;
        }
        return dayOfWeek;
    }

    public static int  getRowId(int index) {
        switch (index) {
        case 0: return R.id.box_row_1;
        case 1: return R.id.box_row_2;
        case 2: return R.id.box_row_3;
        case 3: return R.id.box_row_4;
        case 4: return R.id.box_row_5;
        case 5: return R.id.box_row_6;
        default:return -1;
        }
    }

    public static int  getViewLayoutId(int type) {
        switch (type) {
        case WidgetConf.TYPE_MONTH: return R.layout.month_box_layout;
        case WidgetConf.TYPE_WEEK: return R.layout.week_box_layout;
        case WidgetConf.TYPE_DAY: return R.layout.day_box_layout;
        default:return -1;
        }
    }
    public static int  getMaxEventCount(int type) {
        switch (type) {
        case WidgetConf.TYPE_MONTH: return WidgetConf.MONTH_WIDGET_MAX_EVENT_COUNT;
        case WidgetConf.TYPE_WEEK: return WidgetConf.WEEK_WIDGET_MAX_EVENT_COUNT;
        case WidgetConf.TYPE_DAY: return WidgetConf.DAY_WIDGET_MAX_EVENT_COUNT;
        default:return -1;
        }
    }

    public static void addHeader2RemoteViews(Context context, int type, RemoteViews remoteViews, int dayOfWeek, boolean isLast) {
        final RemoteViews addView = new RemoteViews(context.getPackageName(), getHeaderLayout(type));
        addView.setTextViewText(R.id.header_day_of_week, DateUtils.getDayOfWeekString(dayOfWeek, DateUtils.LENGTH_MEDIUM));
        addView.setTextColor(R.id.header_day_of_week, getHeaderDayOfWeekColorByDayOfWeek(context, dayOfWeek));

        if(isLast) {
            addView.setViewVisibility(R.id.spacer_x_short, View.GONE);
        }

        remoteViews.addView(R.id.header_row, addView);
    }

    public static void addBox2RemoteViews(Context context, int type, RemoteViews remoteViews, BoxValue boxValue, int rowId, boolean isFirstBox, boolean isLastBox, boolean isLastRow) {
        final RemoteViews addView = new RemoteViews(context.getPackageName(), getViewLayoutId(type));
        final int maxEventCount = getMaxEventCount(type);
        final ArrayList<InstancesEntity> instancesEntities = boxValue.getInstancesEntities();

        if(maxEventCount < instancesEntities.size()) {
            addView.setTextViewText(R.id.box_event_more_count, "+" + (instancesEntities.size() - maxEventCount));
        }

        int boxBgColor = 0;

        for(int i = 0; i < maxEventCount; i++) {
            final boolean hasEvent  = i<instancesEntities.size();

            if(i!=0 && !hasEvent) {
                break;
            }

            final boolean isAllday = hasEvent?instancesEntities.get(i).isAllDay():false;

            String title;
            if(type == WidgetConf.TYPE_DAY) {
                if(hasEvent) {
                    if(isAllday) {
                        title =
                            instancesEntities.get(i).getTitle();
                    } else {
                        int num = 2;

                        String hh =
                            CommonUtil.zeroPadding(num, "" + instancesEntities.get(i).getBeginHourOfDay());

                        String mm =
                            CommonUtil.zeroPadding(num, "" + instancesEntities.get(i).getBeginMinute());

                        title = hh + ":" + mm + " " + instancesEntities.get(i).getTitle();
                    }
                } else {
                    title = "";
                }
            } else {
                title = hasEvent?instancesEntities.get(i).getTitle():"";
            }

            final int boxEventTextColor = hasEvent?instancesEntities.get(i).getColor():0;
            final boolean isHoliday = hasEvent?instancesEntities.get(i).isHoliday():false;

            switch (i) {
            case 0:
                /**
                 * box_layout / box_date / box_event_more_count
                 */
                boxBgColor = (isHoliday && !boxValue.isToday())?
                        getBoxBgColorByDayOfWeek(context, Calendar.SUNDAY):getBoxBgColor(context, type, boxValue);
                addView.setInt(R.id.box_layout, "setBackgroundColor", boxBgColor);
                addView.setInt(R.id.box_date, "setBackgroundColor", boxBgColor);
                addView.setInt(R.id.box_event_more_count, "setBackgroundColor", boxBgColor);

                final String boxDate = getBoxDate(type, boxValue, isFirstBox);
                addView.setTextViewText(R.id.box_date, "" + boxDate);

                final int boxDateTextColor = isHoliday?
                        getBoxDateColorByDayOfWeek(context, Calendar.SUNDAY):getBoxDateColor(context, type, boxValue);
                addView.setTextColor(R.id.box_date, boxDateTextColor);

                if(!hasEvent) {
                    break;
                }

                /**
                 * box_event_1
                 */
                addView.setTextViewText(R.id.box_event_1, title);
                if(isAllday) {
                    addView.setTextColor(R.id.box_event_1, Color.WHITE);
                    addView.setInt(R.id.box_event_1, "setBackgroundColor", boxEventTextColor);
                } else {
                    addView.setTextColor(R.id.box_event_1, boxEventTextColor);
                    addView.setInt(R.id.box_event_1, "setBackgroundColor", boxBgColor);
                }
                break;
            case 1:
                /**
                 * box_event_2
                 */
                addView.setTextViewText(R.id.box_event_2, title);
                if(isAllday) {
                    addView.setTextColor(R.id.box_event_2, Color.WHITE);
                    addView.setInt(R.id.box_event_2, "setBackgroundColor", boxEventTextColor);
                } else {
                    addView.setTextColor(R.id.box_event_2, boxEventTextColor);
                    addView.setInt(R.id.box_event_2, "setBackgroundColor", boxBgColor);
                }
                break;
            case 2:
                /**
                 * box_event_3
                 */
                addView.setTextViewText(R.id.box_event_3, title);
                if(isAllday) {
                    addView.setTextColor(R.id.box_event_3, Color.WHITE);
                    addView.setInt(R.id.box_event_3, "setBackgroundColor", boxEventTextColor);
                } else {
                    addView.setTextColor(R.id.box_event_3, boxEventTextColor);
                    addView.setInt(R.id.box_event_3, "setBackgroundColor", boxBgColor);
                }
                break;
            case 3:
                /**
                 * box_event_4
                 */
                addView.setTextViewText(R.id.box_event_4, title);
                if(isAllday) {
                    addView.setTextColor(R.id.box_event_4, Color.WHITE);
                    addView.setInt(R.id.box_event_4, "setBackgroundColor", boxEventTextColor);
                } else {
                    addView.setTextColor(R.id.box_event_4, boxEventTextColor);
                    addView.setInt(R.id.box_event_4, "setBackgroundColor", boxBgColor);
                }
                break;
            case 4:
                /**
                 * box_event_5
                 */
                addView.setTextViewText(R.id.box_event_5, title);
                if(isAllday) {
                    addView.setTextColor(R.id.box_event_5, Color.WHITE);
                    addView.setInt(R.id.box_event_5, "setBackgroundColor", boxEventTextColor);
                } else {
                    addView.setTextColor(R.id.box_event_5, boxEventTextColor);
                    addView.setInt(R.id.box_event_5, "setBackgroundColor", boxBgColor);
                }
                break;
            }
        }
        if(isLastBox) {
            addView.setViewVisibility(R.id.spacer_x, View.GONE);
        }
        if(isLastRow) {
            addView.setViewVisibility(R.id.spacer_y, View.GONE);
        }
        remoteViews.addView(rowId, addView);
    }

    public static int getHeaderDayOfWeekColorByDayOfWeek(Context context, int dayOfWeek) {
        switch (getBoxBgColorKey(context, dayOfWeek)) {
        case BGCOLOR_RED:
            return WidgetConf.COLOR_HEADER_TEXT_DAYOFWEEK_RED;
        case BGCOLOR_BLUE:
            return WidgetConf.COLOR_HEADER_TEXT_DAYOFWEEK_BLUE;
        default:
            return WidgetConf.COLOR_HEADER_TEXT_DAYOFWEEK_DEFAULT;
        }
    }

    public static int getBoxBgColor(Context context, int type, BoxValue boxValue) {
        if(boxValue.isToday()) return WidgetConf.COLOR_BOX_BG_TODAY;

        switch (type) {
        case WidgetConf.TYPE_MONTH:
            if(boxValue.isPrevMonth()) return WidgetConf.COLOR_BOX_BG_PREV_MONTH;
            if(boxValue.isNextMonth()) return WidgetConf.COLOR_BOX_BG_NEXT_MONTH;
        }

        return getBoxBgColorByDayOfWeek(context, boxValue.getDayOfWeek());
    }

    public static int getBoxBgColorByDayOfWeek(Context context, int dayOfWeek) {
        switch (getBoxBgColorKey(context, dayOfWeek)) {
        case BGCOLOR_RED:
            return WidgetConf.COLOR_BOX_BG_RED;
        case BGCOLOR_BLUE:
            return WidgetConf.COLOR_BOX_BG_BLUE;
        default:
            return WidgetConf.COLOR_BOX_BG_DEFAULT;
        }
    }

    public static int getBoxBgColorKey(Context context, int dayOfWeek) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);

        switch (dayOfWeek) {
        case Calendar.SUNDAY:
            return Integer.valueOf(
                    pref.getString(PrefConf.KEY_BOX_BGCOLOR_SUNDAY, PrefConf.DEF_BOX_BGCOLOR_SUNDAY));
        case Calendar.MONDAY:
            return Integer.valueOf(
                    pref.getString(PrefConf.KEY_BOX_BGCOLOR_MONDAY, PrefConf.DEF_BOX_BGCOLOR_MONDAY));
        case Calendar.TUESDAY:
            return Integer.valueOf(
                    pref.getString(PrefConf.KEY_BOX_BGCOLOR_TUESDAY, PrefConf.DEF_BOX_BGCOLOR_TUESDAY));
        case Calendar.WEDNESDAY:
            return Integer.valueOf(
                    pref.getString(PrefConf.KEY_BOX_BGCOLOR_WEDNESDAY, PrefConf.DEF_BOX_BGCOLOR_WEDNESDAY));
        case Calendar.THURSDAY:
            return Integer.valueOf(
                    pref.getString(PrefConf.KEY_BOX_BGCOLOR_THURSDAY, PrefConf.DEF_BOX_BGCOLOR_THURSDAY));
        case Calendar.FRIDAY:
            return Integer.valueOf(
                    pref.getString(PrefConf.KEY_BOX_BGCOLOR_FRIDAY, PrefConf.DEF_BOX_BGCOLOR_FRIDAY));
        case Calendar.SATURDAY:
            return Integer.valueOf(
                    pref.getString(PrefConf.KEY_BOX_BGCOLOR_SATURDAY, PrefConf.DEF_BOX_BGCOLOR_SATURDAY));
        default:
            return BGCOLOR_DEFAULT;
        }
    }

    public static int getBoxDateColor(Context context, int type, BoxValue boxValue) {
        switch (type) {
        case WidgetConf.TYPE_MONTH:
            if(boxValue.isPrevMonth()) return WidgetConf.COLOR_BOX_TEXT_DATE_PREV_MONTH;
            if(boxValue.isNextMonth()) return WidgetConf.COLOR_BOX_TEXT_DATE_NEXT_MONTH;
        }

        return getBoxDateColorByDayOfWeek(context, boxValue.getDayOfWeek());
    }

    public static int getBoxDateColorByDayOfWeek(Context context, int dayOfWeek) {
        switch (getBoxBgColorKey(context, dayOfWeek)) {
        case BGCOLOR_RED:
            return WidgetConf.COLOR_BOX_TEXT_DATE_RED;
        case BGCOLOR_BLUE:
            return WidgetConf.COLOR_BOX_TEXT_DATE_BLUE;
        default:
            return WidgetConf.COLOR_BOX_TEXT_DATE_DEFAULT;
        }
    }

    public static String getBoxDate(int type, BoxValue boxValue, boolean isFirstBox) {
        String boxDateWithoutPrefix = "" + boxValue.getDate();
        String boxDateWithPrefix = boxValue.getMonthDisp() + "/" + boxDateWithoutPrefix;

        if(!boxValue.isCurrentMonth()) return boxDateWithPrefix;

        switch (type) {
        case WidgetConf.TYPE_MONTH:
            if(boxValue.getDate()==1) return boxDateWithPrefix;

        case WidgetConf.TYPE_WEEK:
        case WidgetConf.TYPE_DAY:
            if(isFirstBox) return boxDateWithPrefix;

        }
        return boxDateWithoutPrefix;
    }

    public static boolean isFirstDayOfWeekToday(int type) {
        switch (type) {
        case WidgetConf.TYPE_MONTH: return WidgetConf.MONTH_WIDGET_IS_FIRST_DAY_OF_WEEK_TODAY;
        case WidgetConf.TYPE_WEEK: return WidgetConf.WEEK_WIDGET_IS_FIRST_DAY_OF_WEEK_TODAY;
        case WidgetConf.TYPE_DAY: return WidgetConf.DAY_WIDGET_IS_FIRST_DAY_OF_WEEK_TODAY;
        default: return false;
        }
    }

    public static int getWeekdayNum(int type) {
        switch (type) {
        case WidgetConf.TYPE_MONTH: return WidgetConf.MONTH_WIDGET_WEEKDAY_NUM;
        case WidgetConf.TYPE_WEEK: return WidgetConf.WEEK_WIDGET_WEEKDAY_NUM;
        case WidgetConf.TYPE_DAY: return WidgetConf.DAY_WIDGET_WEEKDAY_NUM;
        default: return -1;
        }
    }

    public static int getWeekNum(int type) {
        switch (type) {
        case WidgetConf.TYPE_MONTH: return WidgetConf.MONTH_WIDGET_WEEK_NUM;
        case WidgetConf.TYPE_WEEK: return WidgetConf.WEEK_WIDGET_WEEK_NUM;
        case WidgetConf.TYPE_DAY: return WidgetConf.DAY_WIDGET_WEEK_NUM;
        default: return -1;
        }
    }

    public static int getWidgetLayout(int type) {
        switch (type) {
        case WidgetConf.TYPE_MONTH: return R.layout.month_widget_layout;
        case WidgetConf.TYPE_WEEK: return R.layout.week_widget_layout;
        case WidgetConf.TYPE_DAY: return R.layout.day_widget_layout;
        default: return -1;
        }
    }

    public static int getHeaderLayout(int type) {
        switch (type) {
        case WidgetConf.TYPE_MONTH: return R.layout.month_header_layout;
        case WidgetConf.TYPE_WEEK: return R.layout.week_header_layout;
        case WidgetConf.TYPE_DAY: return R.layout.day_header_layout;
        default: return -1;
        }
    }
}
