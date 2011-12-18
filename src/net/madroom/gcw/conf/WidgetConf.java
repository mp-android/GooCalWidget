package net.madroom.gcw.conf;

import android.graphics.Color;



public class WidgetConf {
    public static final int TYPE_MONTH = 1;
    public static final int TYPE_WEEK = 2;
    public static final int TYPE_DAY = 3;

    public static final int DEFAULT_WEEKDAY_NUM = 7;

    public static final int MONTH_WIDGET_WEEK_NUM = 6;
    public static final int MONTH_WIDGET_WEEKDAY_NUM = DEFAULT_WEEKDAY_NUM;
    public static final int MONTH_WIDGET_DAY_NUM = MONTH_WIDGET_WEEK_NUM * MONTH_WIDGET_WEEKDAY_NUM;
    public static final int MONTH_WIDGET_MAX_EVENT_COUNT = 3;
    public static final boolean MONTH_WIDGET_IS_FIRST_DAY_OF_WEEK_TODAY = false;

    public static final int WEEK_WIDGET_WEEK_NUM = 1;
    public static final int WEEK_WIDGET_WEEKDAY_NUM = DEFAULT_WEEKDAY_NUM;
    public static final int WEEK_WIDGET_DAY_NUM = WEEK_WIDGET_WEEK_NUM * WEEK_WIDGET_WEEKDAY_NUM;
    public static final int WEEK_WIDGET_MAX_EVENT_COUNT = 5;
    public static final boolean WEEK_WIDGET_IS_FIRST_DAY_OF_WEEK_TODAY = true;

    public static final int DAY_WIDGET_WEEK_NUM = 1;
    public static final int DAY_WIDGET_WEEKDAY_NUM = 1;
    public static final int DAY_WIDGET_DAY_NUM = DAY_WIDGET_WEEK_NUM * DAY_WIDGET_WEEKDAY_NUM;
    public static final int DAY_WIDGET_MAX_EVENT_COUNT = 5;
    public static final boolean DAY_WIDGET_IS_FIRST_DAY_OF_WEEK_TODAY = true;

    public static final int COLOR_BOX_BG_PREV_MONTH = Color.rgb(200, 200, 200);
    public static final int COLOR_BOX_BG_NEXT_MONTH = Color.rgb(200, 200, 200);
    public static final int COLOR_BOX_BG_TODAY = Color.rgb(255, 255, 175);
    public static final int COLOR_BOX_BG_RED = Color.rgb(255, 200, 200);
    public static final int COLOR_BOX_BG_BLUE = Color.rgb(200, 200, 255);
    public static final int COLOR_BOX_BG_DEFAULT = Color.rgb(255, 255, 255);

    public static final int COLOR_HEADER_TEXT_DAYOFWEEK_RED = Color.rgb(200, 0, 0);
    public static final int COLOR_HEADER_TEXT_DAYOFWEEK_BLUE = Color.rgb(0, 0, 200);
    public static final int COLOR_HEADER_TEXT_DAYOFWEEK_DEFAULT = Color.rgb(255, 255, 255);

    public static final int COLOR_BOX_TEXT_DATE_PREV_MONTH = Color.rgb(100, 100, 100);
    public static final int COLOR_BOX_TEXT_DATE_NEXT_MONTH = Color.rgb(100, 100, 100);
    public static final int COLOR_BOX_TEXT_DATE_RED = Color.rgb(200, 0, 0);
    public static final int COLOR_BOX_TEXT_DATE_BLUE = Color.rgb(0, 0, 200);
    public static final int COLOR_BOX_TEXT_DATE_DEFAULT = Color.rgb(0, 0, 0);

    public static final String ACTION_DATE_CHANGED = "net.madroom.gcw.action.DATE_CHANGED";
    public static final String ACTION_CLICK_MONTH = "net.madroom.gcw.action.CLICK_MONTH";
    public static final String ACTION_CLICK_WEEK = "net.madroom.gcw.action.CLICK_WEEK";
    public static final String ACTION_CLICK_DAY = "net.madroom.gcw.action.CLICK_DAY";
    public static final String ANDROID_CALENDAR_PACKAGE_NAME = "com.android.calendar";

    public static final String LAUNCH_APP_INFO_DELIMITER = ",";

    public static final String  NONE_HOLIDAY_CALENDAR = "none";
}
