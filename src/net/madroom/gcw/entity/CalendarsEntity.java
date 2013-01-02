package net.madroom.gcw.entity;

import java.util.ArrayList;

import net.madroom.common.CommonUtil;
import net.madroom.gcw.conf.WidgetConf;
import android.content.Context;
import android.database.Cursor;
import android.provider.CalendarContract.Calendars;

public class CalendarsEntity {

    private final String[] PROJECTION = new String[] {
            Calendars._ID,
            Calendars.CALENDAR_DISPLAY_NAME,
            Calendars.VISIBLE
    };

    private long id;
    private String displayName;
    
    ArrayList<CalendarsEntity> calendarsEntities = new ArrayList<CalendarsEntity>();

    public ArrayList<CalendarsEntity> getCalendarsEntities(Context context) {
        if(CommonUtil.isInstalled(context, WidgetConf.ANDROID_CALENDAR_PACKAGE_NAME)) {
            return getCalendarsEntitiesByDB(context);
        } else {
            return getCalendarsEntitiesByDummy();
        }
    }

    public ArrayList<CalendarsEntity> getCalendarsEntitiesByDB(Context context) {
    	Cursor c = context.getContentResolver().query(
    			Calendars.CONTENT_URI, PROJECTION,
    			Calendars.VISIBLE + "!=0",
    			null,
    			Calendars.CALENDAR_DISPLAY_NAME + " asc");

        if(c.moveToFirst()) {
            do {
                CalendarsEntity calendarsEntity = new CalendarsEntity();
                calendarsEntity.setId(c.getLong(c.getColumnIndex(Calendars._ID)));
                calendarsEntity.setDisplayName(c.getString(c.getColumnIndex(Calendars.CALENDAR_DISPLAY_NAME)));
                calendarsEntities.add(calendarsEntity);
            } while (c.moveToNext());
        }
        c.close();
        return calendarsEntities;
    }

    public ArrayList<CalendarsEntity> getCalendarsEntitiesByDummy() {
        return calendarsEntities;
    }


    public long getId() {
        return id;
    }


    public void setId(long id) {
        this.id = id;
    }


    public String getDisplayName() {
        return displayName;
    }


    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }


    public ArrayList<CalendarsEntity> getCalendarsEntities() {
        return calendarsEntities;
    }


    public void setCalendarsEntities(ArrayList<CalendarsEntity> calendarsEntities) {
        this.calendarsEntities = calendarsEntities;
    }


    public String[] getPROJECTION() {
        return PROJECTION;
    }


}
