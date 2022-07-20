package org.telegram.messenger;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.widget.RemoteViews;
import org.telegram.ui.LaunchActivity;
/* loaded from: classes.dex */
public class FeedWidgetProvider extends AppWidgetProvider {
    @Override // android.appwidget.AppWidgetProvider, android.content.BroadcastReceiver
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
    }

    @Override // android.appwidget.AppWidgetProvider
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] iArr) {
        super.onUpdate(context, appWidgetManager, iArr);
        for (int i : iArr) {
            updateWidget(context, appWidgetManager, i);
        }
    }

    @Override // android.appwidget.AppWidgetProvider
    public void onDeleted(Context context, int[] iArr) {
        super.onDeleted(context, iArr);
        for (int i = 0; i < iArr.length; i++) {
            SharedPreferences.Editor edit = context.getSharedPreferences("shortcut_widget", 0).edit();
            SharedPreferences.Editor remove = edit.remove("account" + iArr[i]);
            remove.remove("dialogId" + iArr[i]).commit();
        }
    }

    public static void updateWidget(Context context, AppWidgetManager appWidgetManager, int i) {
        Intent intent = new Intent(context, FeedWidgetService.class);
        intent.putExtra("appWidgetId", i);
        intent.setData(Uri.parse(intent.toUri(1)));
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), 2131427340);
        remoteViews.setRemoteAdapter(i, 2131230850, intent);
        remoteViews.setEmptyView(2131230850, 2131230813);
        Intent intent2 = new Intent(ApplicationLoader.applicationContext, LaunchActivity.class);
        intent2.setAction("com.tmessages.openchat" + Math.random() + Integer.MAX_VALUE);
        intent2.addFlags(67108864);
        intent2.addCategory("android.intent.category.LAUNCHER");
        remoteViews.setPendingIntentTemplate(2131230850, PendingIntent.getActivity(ApplicationLoader.applicationContext, 0, intent2, 134217728));
        appWidgetManager.updateAppWidget(i, remoteViews);
    }
}