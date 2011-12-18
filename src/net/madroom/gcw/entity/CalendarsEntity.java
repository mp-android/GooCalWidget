package net.madroom.gcw.entity;

import java.util.ArrayList;

import net.madroom.common.CommonUtil;
import net.madroom.gcw.conf.WidgetConf;
import android.content.Context;
import android.database.Cursor;
import android.provider.Calendar.Calendars;

public class CalendarsEntity {

    /**
    _id
    _sync_account
    _sync_account_type
    _sync_id
    _sync_version
    _sync_time
    _sync_local_id
    _sync_dirty
    _sync_mark
    url
    name
    displayName
    hidden
    color
    access_level
    selected
    sync_events
    location
    timezone
    ownerAccount
    organizerCanRespond
    **/

    private final String[] PROJECTION = new String[] {
            Calendars._ID,
            Calendars.DISPLAY_NAME,
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
        Cursor c = Calendars.query(context.getContentResolver(), PROJECTION,
                Calendars.SELECTED + "!=0", Calendars.DISPLAY_NAME + " asc");

        if(c.moveToFirst()) {
            do {
                CalendarsEntity calendarsEntity = new CalendarsEntity();
                calendarsEntity.setId(c.getLong(c.getColumnIndex(Calendars._ID)));
                calendarsEntity.setDisplayName(c.getString(c.getColumnIndex(Calendars.DISPLAY_NAME)));
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
