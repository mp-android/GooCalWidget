package net.madroom.gcw.activity;

import java.util.ArrayList;
import java.util.List;

import net.madroom.gcw.R;
import net.madroom.gcw.conf.PrefConf;
import net.madroom.gcw.conf.WidgetConf;
import net.madroom.gcw.entity.CalendarsEntity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.PreferenceActivity;

public class PrefActivity extends PreferenceActivity {
    private Context mContext = this;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.pref);
    }

    @Override
    public void onStart() {
        super.onStart();

        ArrayList<CharSequence> entries;
        ArrayList<CharSequence> entryValues;

        /**
         * launch app
         */
        entries = new ArrayList<CharSequence>();
        entryValues = new ArrayList<CharSequence>();

        Intent i = new Intent(Intent.ACTION_MAIN, null);
        i.addCategory(Intent.CATEGORY_LAUNCHER);

        PackageManager pm = this.getPackageManager();
        List<ResolveInfo> infos = pm.queryIntentActivities(i,0);

        for (ResolveInfo info : infos) {
            entries.add(info.loadLabel(pm));
            entryValues.add(info.activityInfo.packageName + WidgetConf.LAUNCH_APP_INFO_DELIMITER +info.activityInfo.name);
        }

        ListPreference launchAppList = (ListPreference) findPreference(PrefConf.KEY_LAUNCH_APP_INFO);
        launchAppList.setEntries((CharSequence[])entries.toArray(new CharSequence[0]));
        launchAppList.setEntryValues((CharSequence[])entryValues.toArray(new CharSequence[0]));

        /**
         * holiday calendar
         */
        entries = new ArrayList<CharSequence>();
        entryValues = new ArrayList<CharSequence>();

        ArrayList<CalendarsEntity> calendarsEntities = new CalendarsEntity().getCalendarsEntities(mContext);

        entries.add(WidgetConf.NONE_HOLIDAY_CALENDAR);
        entryValues.add(PrefConf.DEF_HOLIDAY_CALENDAR);

        for (CalendarsEntity entity : calendarsEntities) {
            entries.add(entity.getDisplayName());
            entryValues.add(""+entity.getId());
        }

        ListPreference calendarList = (ListPreference) findPreference(PrefConf.KEY_HOLIDAY_CALENDAR);
        calendarList.setEntries((CharSequence[])entries.toArray(new CharSequence[0]));
        calendarList.setEntryValues((CharSequence[])entryValues.toArray(new CharSequence[0]));
    }
}
