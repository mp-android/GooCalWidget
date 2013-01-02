package net.madroom.gcw.widget;

import net.madroom.common.CommonUtil;
import net.madroom.gcw.R;
import net.madroom.gcw.activity.PrefActivity;
import net.madroom.gcw.conf.PrefConf;
import net.madroom.gcw.conf.WidgetConf;
import net.madroom.gcw.util.CalendarUtil;
import net.madroom.gcw.util.WidgetUtil;
import net.madroom.gcw.value.BoxValue;
import android.app.PendingIntent;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.ContentObserver;
import android.os.Handler;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.provider.CalendarContract.Instances;
import android.widget.RemoteViews;

public class WeekWidget extends AppWidgetProvider {
    @Override
    public void onEnabled(Context context) {
        super.onEnabled(context);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        context.startService(new Intent(context, MyService.class));
        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        super.onDeleted(context, appWidgetIds);
    }
    
    @Override
    public void onDisabled(Context context) {
        super.onDisabled(context);
    }
    
    @Override
    public void onReceive(Context context, Intent intent) {
        /**
         * TODO:
         * when Fast Reboot run. received action "mobi.intuitit.android.hpp.ACTION_READY" by Xperia arc.
         * https://market.android.com/details?id=com.greatbytes.fastreboot
         */
//        if(intent.getAction().equals(WidgetConf.ACTION_DATE_CHANGED)) {
//            context.startService(new Intent(context, MyService.class));
//        }
        context.startService(new Intent(context, MyService.class));
        super.onReceive(context, intent);
    }

    public static class MyService extends Service {
        Context mContext;
        SharedPreferences mPref;
        ComponentName mComponentName;
        int mType;
        AppWidgetManager mManager;
        RemoteViews mRemoteViews;

        @Override
        public void onCreate() {
            /**
             * initialize.
             */
            mContext = this;
            mPref = PreferenceManager.getDefaultSharedPreferences(mContext);
            mComponentName = new ComponentName(mContext, WeekWidget.class);
            mType = WidgetConf.TYPE_WEEK;
            mManager = AppWidgetManager.getInstance(mContext);
            mRemoteViews = new RemoteViews(mContext.getPackageName(), WidgetUtil.getWidgetLayout(mType));

            ContentObserver contentObserver = new ContentObserver(new Handler()) {
                @Override
                public void onChange(boolean selfChange) {
                    startService(new Intent(mContext, MyService.class));
                    super.onChange(selfChange);
                }
            };
            getContentResolver().registerContentObserver(Instances.CONTENT_URI, true, contentObserver);
        }

        @Override
        public void onStart(Intent intent, int startId) {

            int widgetCount = AppWidgetManager.getInstance(mContext).getAppWidgetIds(new ComponentName(mContext, WeekWidget.class)).length;
            if(widgetCount==0) return;

            if(intent!=null && intent.getAction()!=null) {
                if(intent.getAction().equals(WidgetConf.ACTION_CLICK_WEEK)) {

                    String appInfo = mPref.getString(PrefConf.KEY_LAUNCH_APP_INFO, PrefConf.DEF_LAUNCH_APP_INFO);
                    String[] appInfos = appInfo.split(WidgetConf.LAUNCH_APP_INFO_DELIMITER);

                    boolean isInstalled = CommonUtil.isInstalled(mContext, appInfos[0]);

                    if(isInstalled) {
                        Intent i = new Intent();
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        i.setAction(Intent.ACTION_MAIN);
                        i.setClassName(appInfos[0], appInfos[1]);
                        startActivity(i);
                        
                    } else {
                        Intent i = new Intent(mContext, PrefActivity.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(i);
                    }
                    return;
                }
            }

            int firstDayOfWeek;
            CalendarUtil calUtil;
            if(WidgetUtil.isFirstDayOfWeekToday(mType)) {
                calUtil = new CalendarUtil(true, mType);
                firstDayOfWeek = calUtil.mDayOfWeek;
            } else {
                firstDayOfWeek =
                    Integer.valueOf(mPref.getString(PrefConf.KEY_FIRST_DAY_OF_WEEK, PrefConf.DEF_FIRST_DAY_OF_WEEK));
                calUtil = new CalendarUtil(true, mType, firstDayOfWeek);
            }

            WidgetUtil.removeAllViews(mType, mRemoteViews);

            Intent clickIntent = new Intent();
            clickIntent.setAction(WidgetConf.ACTION_CLICK_WEEK);
            PendingIntent pendingIntent = PendingIntent.getService(mContext, 0, clickIntent, 0);
            mRemoteViews.setOnClickPendingIntent(R.id.week_widget_layout, pendingIntent);

            /**
             * add header layout.
             */
            int dayOfWeek = firstDayOfWeek;
            final int weekDayNum = WidgetUtil.getWeekdayNum(mType);
            for(int i=0; i<weekDayNum; i++) {
                WidgetUtil.addHeader2RemoteViews(mContext, mType, mRemoteViews, dayOfWeek, (i+1)==weekDayNum);
                dayOfWeek = WidgetUtil.getNextDayOfWeek(dayOfWeek);
            }

            /**
             * add box layout.
             */
            final int weekNum = WidgetUtil.getWeekNum(mType);
            dayOfWeek = firstDayOfWeek;
            for(int i=0; i<weekNum; i++) {
                final int rowId = WidgetUtil.getRowId(i);

                for(int j=0; j<weekDayNum; j++) {
                    BoxValue boxValue = WidgetUtil.getBoxValue(mContext, calUtil, (i*weekDayNum)+j, dayOfWeek);
                    dayOfWeek = WidgetUtil.getNextDayOfWeek(dayOfWeek);
                    WidgetUtil.addBox2RemoteViews(mContext, mType, mRemoteViews, boxValue,
                            rowId, j==0, (j+1)==weekDayNum, (i+1)==weekNum);
                }
            }

            mManager.updateAppWidget(mComponentName, mRemoteViews);

            Intent alarmIntent = new Intent(mContext, WeekWidget.class);
            calUtil = new CalendarUtil(true, WidgetConf.TYPE_DAY);
            CommonUtil.setAlarm(mContext, alarmIntent,
                    WidgetConf.ACTION_DATE_CHANGED, calUtil.getUnixTimestampByAddDate(1));

            // ContentObserver#onChange doesn't react when Service#stopSelf
//          stopSelf();
        }

        @Override
        public IBinder onBind(Intent intent) {
            return null;
        }
    }
}
