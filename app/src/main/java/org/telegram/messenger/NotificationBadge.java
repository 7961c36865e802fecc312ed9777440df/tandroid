package org.telegram.messenger;

import android.annotation.TargetApi;
import android.content.AsyncQueryHandler;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Message;
import java.io.Closeable;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
/* loaded from: classes.dex */
public class NotificationBadge {
    private static final List<Class<? extends Badger>> BADGERS;
    private static Badger badger;
    private static ComponentName componentName;
    private static boolean initied;

    /* loaded from: classes.dex */
    public interface Badger {
        void executeBadge(int i);

        List<String> getSupportLaunchers();
    }

    static {
        LinkedList linkedList = new LinkedList();
        BADGERS = linkedList;
        linkedList.add(AdwHomeBadger.class);
        linkedList.add(ApexHomeBadger.class);
        linkedList.add(NewHtcHomeBadger.class);
        linkedList.add(NovaHomeBadger.class);
        linkedList.add(SonyHomeBadger.class);
        linkedList.add(XiaomiHomeBadger.class);
        linkedList.add(AsusHomeBadger.class);
        linkedList.add(HuaweiHomeBadger.class);
        linkedList.add(OPPOHomeBader.class);
        linkedList.add(SamsungHomeBadger.class);
        linkedList.add(ZukHomeBadger.class);
        linkedList.add(VivoHomeBadger.class);
    }

    /* loaded from: classes.dex */
    public static class AdwHomeBadger implements Badger {
        public static final String CLASSNAME = "CNAME";
        public static final String COUNT = "COUNT";
        public static final String INTENT_UPDATE_COUNTER = "org.adw.launcher.counter.SEND";
        public static final String PACKAGENAME = "PNAME";

        @Override // org.telegram.messenger.NotificationBadge.Badger
        public void executeBadge(int i) {
            Intent intent = new Intent("org.adw.launcher.counter.SEND");
            intent.putExtra("PNAME", NotificationBadge.componentName.getPackageName());
            intent.putExtra("CNAME", NotificationBadge.componentName.getClassName());
            intent.putExtra("COUNT", i);
            if (NotificationBadge.canResolveBroadcast(intent)) {
                AndroidUtilities.runOnUIThread(new NotificationBadge$AdwHomeBadger$$ExternalSyntheticLambda0(intent));
            }
        }

        public static /* synthetic */ void lambda$executeBadge$0(Intent intent) {
            ApplicationLoader.applicationContext.sendBroadcast(intent);
        }

        @Override // org.telegram.messenger.NotificationBadge.Badger
        public List<String> getSupportLaunchers() {
            return Arrays.asList("org.adw.launcher", "org.adwfreak.launcher");
        }
    }

    /* loaded from: classes.dex */
    public static class ApexHomeBadger implements Badger {
        private static final String CLASS = "class";
        private static final String COUNT = "count";
        private static final String INTENT_UPDATE_COUNTER = "com.anddoes.launcher.COUNTER_CHANGED";
        private static final String PACKAGENAME = "package";

        @Override // org.telegram.messenger.NotificationBadge.Badger
        public void executeBadge(int i) {
            Intent intent = new Intent("com.anddoes.launcher.COUNTER_CHANGED");
            intent.putExtra("package", NotificationBadge.componentName.getPackageName());
            intent.putExtra("count", i);
            intent.putExtra("class", NotificationBadge.componentName.getClassName());
            if (NotificationBadge.canResolveBroadcast(intent)) {
                AndroidUtilities.runOnUIThread(new NotificationBadge$ApexHomeBadger$$ExternalSyntheticLambda0(intent));
            }
        }

        public static /* synthetic */ void lambda$executeBadge$0(Intent intent) {
            ApplicationLoader.applicationContext.sendBroadcast(intent);
        }

        @Override // org.telegram.messenger.NotificationBadge.Badger
        public List<String> getSupportLaunchers() {
            return Arrays.asList("com.anddoes.launcher");
        }
    }

    /* loaded from: classes.dex */
    public static class AsusHomeBadger implements Badger {
        private static final String INTENT_ACTION = "android.intent.action.BADGE_COUNT_UPDATE";
        private static final String INTENT_EXTRA_ACTIVITY_NAME = "badge_count_class_name";
        private static final String INTENT_EXTRA_BADGE_COUNT = "badge_count";
        private static final String INTENT_EXTRA_PACKAGENAME = "badge_count_package_name";

        @Override // org.telegram.messenger.NotificationBadge.Badger
        public void executeBadge(int i) {
            Intent intent = new Intent("android.intent.action.BADGE_COUNT_UPDATE");
            intent.putExtra("badge_count", i);
            intent.putExtra("badge_count_package_name", NotificationBadge.componentName.getPackageName());
            intent.putExtra("badge_count_class_name", NotificationBadge.componentName.getClassName());
            intent.putExtra("badge_vip_count", 0);
            if (NotificationBadge.canResolveBroadcast(intent)) {
                AndroidUtilities.runOnUIThread(new NotificationBadge$AsusHomeBadger$$ExternalSyntheticLambda0(intent));
            }
        }

        public static /* synthetic */ void lambda$executeBadge$0(Intent intent) {
            ApplicationLoader.applicationContext.sendBroadcast(intent);
        }

        @Override // org.telegram.messenger.NotificationBadge.Badger
        public List<String> getSupportLaunchers() {
            return Arrays.asList("com.asus.launcher");
        }
    }

    /* loaded from: classes.dex */
    public static class DefaultBadger implements Badger {
        private static final String INTENT_ACTION = "android.intent.action.BADGE_COUNT_UPDATE";
        private static final String INTENT_EXTRA_ACTIVITY_NAME = "badge_count_class_name";
        private static final String INTENT_EXTRA_BADGE_COUNT = "badge_count";
        private static final String INTENT_EXTRA_PACKAGENAME = "badge_count_package_name";

        @Override // org.telegram.messenger.NotificationBadge.Badger
        public void executeBadge(int i) {
            Intent intent = new Intent("android.intent.action.BADGE_COUNT_UPDATE");
            intent.putExtra("badge_count", i);
            intent.putExtra("badge_count_package_name", NotificationBadge.componentName.getPackageName());
            intent.putExtra("badge_count_class_name", NotificationBadge.componentName.getClassName());
            AndroidUtilities.runOnUIThread(new NotificationBadge$DefaultBadger$$ExternalSyntheticLambda0(intent));
        }

        public static /* synthetic */ void lambda$executeBadge$0(Intent intent) {
            try {
                ApplicationLoader.applicationContext.sendBroadcast(intent);
            } catch (Exception unused) {
            }
        }

        @Override // org.telegram.messenger.NotificationBadge.Badger
        public List<String> getSupportLaunchers() {
            return Arrays.asList("fr.neamar.kiss", "com.quaap.launchtime", "com.quaap.launchtime_official");
        }
    }

    /* loaded from: classes.dex */
    public static class HuaweiHomeBadger implements Badger {
        @Override // org.telegram.messenger.NotificationBadge.Badger
        public void executeBadge(int i) {
            Bundle bundle = new Bundle();
            bundle.putString("package", ApplicationLoader.applicationContext.getPackageName());
            bundle.putString("class", NotificationBadge.componentName.getClassName());
            bundle.putInt("badgenumber", i);
            AndroidUtilities.runOnUIThread(new NotificationBadge$HuaweiHomeBadger$$ExternalSyntheticLambda0(bundle));
        }

        public static /* synthetic */ void lambda$executeBadge$0(Bundle bundle) {
            try {
                ApplicationLoader.applicationContext.getContentResolver().call(Uri.parse("content://com.huawei.android.launcher.settings/badge/"), "change_badge", (String) null, bundle);
            } catch (Exception e) {
                FileLog.e(e);
            }
        }

        @Override // org.telegram.messenger.NotificationBadge.Badger
        public List<String> getSupportLaunchers() {
            return Arrays.asList("com.huawei.android.launcher");
        }
    }

    /* loaded from: classes.dex */
    public static class NewHtcHomeBadger implements Badger {
        public static final String COUNT = "count";
        public static final String EXTRA_COMPONENT = "com.htc.launcher.extra.COMPONENT";
        public static final String EXTRA_COUNT = "com.htc.launcher.extra.COUNT";
        public static final String INTENT_SET_NOTIFICATION = "com.htc.launcher.action.SET_NOTIFICATION";
        public static final String INTENT_UPDATE_SHORTCUT = "com.htc.launcher.action.UPDATE_SHORTCUT";
        public static final String PACKAGENAME = "packagename";

        @Override // org.telegram.messenger.NotificationBadge.Badger
        public void executeBadge(int i) {
            Intent intent = new Intent("com.htc.launcher.action.SET_NOTIFICATION");
            intent.putExtra("com.htc.launcher.extra.COMPONENT", NotificationBadge.componentName.flattenToShortString());
            intent.putExtra("com.htc.launcher.extra.COUNT", i);
            Intent intent2 = new Intent("com.htc.launcher.action.UPDATE_SHORTCUT");
            intent2.putExtra("packagename", NotificationBadge.componentName.getPackageName());
            intent2.putExtra("count", i);
            if (NotificationBadge.canResolveBroadcast(intent) || NotificationBadge.canResolveBroadcast(intent2)) {
                AndroidUtilities.runOnUIThread(new NotificationBadge$NewHtcHomeBadger$$ExternalSyntheticLambda0(intent, intent2));
            }
        }

        public static /* synthetic */ void lambda$executeBadge$0(Intent intent, Intent intent2) {
            ApplicationLoader.applicationContext.sendBroadcast(intent);
            ApplicationLoader.applicationContext.sendBroadcast(intent2);
        }

        @Override // org.telegram.messenger.NotificationBadge.Badger
        public List<String> getSupportLaunchers() {
            return Arrays.asList("com.htc.launcher");
        }
    }

    /* loaded from: classes.dex */
    public static class NovaHomeBadger implements Badger {
        private static final String CONTENT_URI = "content://com.teslacoilsw.notifier/unread_count";
        private static final String COUNT = "count";
        private static final String TAG = "tag";

        @Override // org.telegram.messenger.NotificationBadge.Badger
        public void executeBadge(int i) {
            ContentValues contentValues = new ContentValues();
            contentValues.put("tag", NotificationBadge.componentName.getPackageName() + "/" + NotificationBadge.componentName.getClassName());
            contentValues.put("count", Integer.valueOf(i));
            ApplicationLoader.applicationContext.getContentResolver().insert(Uri.parse("content://com.teslacoilsw.notifier/unread_count"), contentValues);
        }

        @Override // org.telegram.messenger.NotificationBadge.Badger
        public List<String> getSupportLaunchers() {
            return Arrays.asList("com.teslacoilsw.launcher");
        }
    }

    /* loaded from: classes.dex */
    public static class OPPOHomeBader implements Badger {
        private static final String INTENT_ACTION = "com.oppo.unsettledevent";
        private static final String INTENT_EXTRA_BADGEUPGRADE_COUNT = "app_badge_count";
        private static final String INTENT_EXTRA_BADGE_COUNT = "number";
        private static final String INTENT_EXTRA_BADGE_UPGRADENUMBER = "upgradeNumber";
        private static final String INTENT_EXTRA_PACKAGENAME = "pakeageName";
        private static final String PROVIDER_CONTENT_URI = "content://com.android.badge/badge";
        private int mCurrentTotalCount = -1;

        @Override // org.telegram.messenger.NotificationBadge.Badger
        public void executeBadge(int i) {
            if (this.mCurrentTotalCount == i) {
                return;
            }
            this.mCurrentTotalCount = i;
            executeBadgeByContentProvider(i);
        }

        @Override // org.telegram.messenger.NotificationBadge.Badger
        public List<String> getSupportLaunchers() {
            return Collections.singletonList("com.oppo.launcher");
        }

        @TargetApi(11)
        private void executeBadgeByContentProvider(int i) {
            try {
                Bundle bundle = new Bundle();
                bundle.putInt("app_badge_count", i);
                ApplicationLoader.applicationContext.getContentResolver().call(Uri.parse("content://com.android.badge/badge"), "setAppBadgeCount", (String) null, bundle);
            } catch (Throwable unused) {
            }
        }
    }

    /* loaded from: classes.dex */
    public static class SamsungHomeBadger implements Badger {
        private static final String[] CONTENT_PROJECTION = {"_id", "class"};
        private static final String CONTENT_URI = "content://com.sec.badge/apps?notify=true";
        private static DefaultBadger defaultBadger;

        @Override // org.telegram.messenger.NotificationBadge.Badger
        public void executeBadge(int i) {
            try {
                if (defaultBadger == null) {
                    defaultBadger = new DefaultBadger();
                }
                defaultBadger.executeBadge(i);
            } catch (Exception unused) {
            }
            Uri parse = Uri.parse("content://com.sec.badge/apps?notify=true");
            ContentResolver contentResolver = ApplicationLoader.applicationContext.getContentResolver();
            Cursor cursor = null;
            try {
                cursor = contentResolver.query(parse, CONTENT_PROJECTION, "package=?", new String[]{NotificationBadge.componentName.getPackageName()}, null);
                if (cursor != null) {
                    String className = NotificationBadge.componentName.getClassName();
                    boolean z = false;
                    while (cursor.moveToNext()) {
                        contentResolver.update(parse, getContentValues(NotificationBadge.componentName, i, false), "_id=?", new String[]{String.valueOf(cursor.getInt(0))});
                        if (className.equals(cursor.getString(cursor.getColumnIndex("class")))) {
                            z = true;
                        }
                    }
                    if (!z) {
                        contentResolver.insert(parse, getContentValues(NotificationBadge.componentName, i, true));
                    }
                }
            } finally {
                NotificationBadge.close(cursor);
            }
        }

        private ContentValues getContentValues(ComponentName componentName, int i, boolean z) {
            ContentValues contentValues = new ContentValues();
            if (z) {
                contentValues.put("package", componentName.getPackageName());
                contentValues.put("class", componentName.getClassName());
            }
            contentValues.put("badgecount", Integer.valueOf(i));
            return contentValues;
        }

        @Override // org.telegram.messenger.NotificationBadge.Badger
        public List<String> getSupportLaunchers() {
            return Arrays.asList("com.sec.android.app.launcher", "com.sec.android.app.twlauncher");
        }
    }

    /* loaded from: classes.dex */
    public static class SonyHomeBadger implements Badger {
        private static final String INTENT_ACTION = "com.sonyericsson.home.action.UPDATE_BADGE";
        private static final String INTENT_EXTRA_ACTIVITY_NAME = "com.sonyericsson.home.intent.extra.badge.ACTIVITY_NAME";
        private static final String INTENT_EXTRA_MESSAGE = "com.sonyericsson.home.intent.extra.badge.MESSAGE";
        private static final String INTENT_EXTRA_PACKAGE_NAME = "com.sonyericsson.home.intent.extra.badge.PACKAGE_NAME";
        private static final String INTENT_EXTRA_SHOW_MESSAGE = "com.sonyericsson.home.intent.extra.badge.SHOW_MESSAGE";
        private static final String PROVIDER_COLUMNS_ACTIVITY_NAME = "activity_name";
        private static final String PROVIDER_COLUMNS_BADGE_COUNT = "badge_count";
        private static final String PROVIDER_COLUMNS_PACKAGE_NAME = "package_name";
        private static final String PROVIDER_CONTENT_URI = "content://com.sonymobile.home.resourceprovider/badge";
        private static final String SONY_HOME_PROVIDER_NAME = "com.sonymobile.home.resourceprovider";
        private static AsyncQueryHandler mQueryHandler;
        private final Uri BADGE_CONTENT_URI = Uri.parse("content://com.sonymobile.home.resourceprovider/badge");

        @Override // org.telegram.messenger.NotificationBadge.Badger
        public void executeBadge(int i) {
            if (sonyBadgeContentProviderExists()) {
                executeBadgeByContentProvider(i);
            } else {
                executeBadgeByBroadcast(i);
            }
        }

        @Override // org.telegram.messenger.NotificationBadge.Badger
        public List<String> getSupportLaunchers() {
            return Arrays.asList("com.sonyericsson.home", "com.sonymobile.home");
        }

        private static void executeBadgeByBroadcast(int i) {
            Intent intent = new Intent("com.sonyericsson.home.action.UPDATE_BADGE");
            intent.putExtra("com.sonyericsson.home.intent.extra.badge.PACKAGE_NAME", NotificationBadge.componentName.getPackageName());
            intent.putExtra("com.sonyericsson.home.intent.extra.badge.ACTIVITY_NAME", NotificationBadge.componentName.getClassName());
            intent.putExtra("com.sonyericsson.home.intent.extra.badge.MESSAGE", String.valueOf(i));
            intent.putExtra("com.sonyericsson.home.intent.extra.badge.SHOW_MESSAGE", i > 0);
            AndroidUtilities.runOnUIThread(new NotificationBadge$SonyHomeBadger$$ExternalSyntheticLambda0(intent));
        }

        public static /* synthetic */ void lambda$executeBadgeByBroadcast$0(Intent intent) {
            ApplicationLoader.applicationContext.sendBroadcast(intent);
        }

        /* renamed from: org.telegram.messenger.NotificationBadge$SonyHomeBadger$1 */
        /* loaded from: classes.dex */
        public class AnonymousClass1 extends AsyncQueryHandler {
            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            AnonymousClass1(ContentResolver contentResolver) {
                super(contentResolver);
                SonyHomeBadger.this = r1;
            }

            @Override // android.content.AsyncQueryHandler, android.os.Handler
            public void handleMessage(Message message) {
                try {
                    super.handleMessage(message);
                } catch (Throwable unused) {
                }
            }
        }

        private void executeBadgeByContentProvider(int i) {
            if (i < 0) {
                return;
            }
            if (mQueryHandler == null) {
                mQueryHandler = new AnonymousClass1(ApplicationLoader.applicationContext.getApplicationContext().getContentResolver());
            }
            insertBadgeAsync(i, NotificationBadge.componentName.getPackageName(), NotificationBadge.componentName.getClassName());
        }

        private void insertBadgeAsync(int i, String str, String str2) {
            ContentValues contentValues = new ContentValues();
            contentValues.put("badge_count", Integer.valueOf(i));
            contentValues.put("package_name", str);
            contentValues.put("activity_name", str2);
            mQueryHandler.startInsert(0, null, this.BADGE_CONTENT_URI, contentValues);
        }

        private static boolean sonyBadgeContentProviderExists() {
            return ApplicationLoader.applicationContext.getPackageManager().resolveContentProvider("com.sonymobile.home.resourceprovider", 0) != null;
        }
    }

    /* loaded from: classes.dex */
    public static class XiaomiHomeBadger implements Badger {
        public static final String EXTRA_UPDATE_APP_COMPONENT_NAME = "android.intent.extra.update_application_component_name";
        public static final String EXTRA_UPDATE_APP_MSG_TEXT = "android.intent.extra.update_application_message_text";
        public static final String INTENT_ACTION = "android.intent.action.APPLICATION_MESSAGE_UPDATE";

        @Override // org.telegram.messenger.NotificationBadge.Badger
        public void executeBadge(int i) {
            Object obj = "";
            try {
                Object newInstance = Class.forName("android.app.MiuiNotification").newInstance();
                Field declaredField = newInstance.getClass().getDeclaredField("messageCount");
                declaredField.setAccessible(true);
                declaredField.set(newInstance, String.valueOf(i == 0 ? obj : Integer.valueOf(i)));
            } catch (Throwable unused) {
                Intent intent = new Intent("android.intent.action.APPLICATION_MESSAGE_UPDATE");
                intent.putExtra("android.intent.extra.update_application_component_name", NotificationBadge.componentName.getPackageName() + "/" + NotificationBadge.componentName.getClassName());
                if (i != 0) {
                    obj = Integer.valueOf(i);
                }
                intent.putExtra("android.intent.extra.update_application_message_text", String.valueOf(obj));
                if (!NotificationBadge.canResolveBroadcast(intent)) {
                    return;
                }
                AndroidUtilities.runOnUIThread(new AnonymousClass1(intent));
            }
        }

        /* renamed from: org.telegram.messenger.NotificationBadge$XiaomiHomeBadger$1 */
        /* loaded from: classes.dex */
        class AnonymousClass1 implements Runnable {
            final /* synthetic */ Intent val$localIntent;

            AnonymousClass1(Intent intent) {
                XiaomiHomeBadger.this = r1;
                this.val$localIntent = intent;
            }

            @Override // java.lang.Runnable
            public void run() {
                ApplicationLoader.applicationContext.sendBroadcast(this.val$localIntent);
            }
        }

        @Override // org.telegram.messenger.NotificationBadge.Badger
        public List<String> getSupportLaunchers() {
            return Arrays.asList("com.miui.miuilite", "com.miui.home", "com.miui.miuihome", "com.miui.miuihome2", "com.miui.mihome", "com.miui.mihome2");
        }
    }

    /* loaded from: classes.dex */
    public static class ZukHomeBadger implements Badger {
        private final Uri CONTENT_URI = Uri.parse("content://com.android.badge/badge");

        @Override // org.telegram.messenger.NotificationBadge.Badger
        @TargetApi(11)
        public void executeBadge(int i) {
            Bundle bundle = new Bundle();
            bundle.putInt("app_badge_count", i);
            AndroidUtilities.runOnUIThread(new NotificationBadge$ZukHomeBadger$$ExternalSyntheticLambda0(this, bundle));
        }

        public /* synthetic */ void lambda$executeBadge$0(Bundle bundle) {
            try {
                ApplicationLoader.applicationContext.getContentResolver().call(this.CONTENT_URI, "setAppBadgeCount", (String) null, bundle);
            } catch (Exception e) {
                FileLog.e(e);
            }
        }

        @Override // org.telegram.messenger.NotificationBadge.Badger
        public List<String> getSupportLaunchers() {
            return Collections.singletonList("com.zui.launcher");
        }
    }

    /* loaded from: classes.dex */
    public static class VivoHomeBadger implements Badger {
        @Override // org.telegram.messenger.NotificationBadge.Badger
        public void executeBadge(int i) {
            Intent intent = new Intent("launcher.action.CHANGE_APPLICATION_NOTIFICATION_NUM");
            intent.putExtra("packageName", ApplicationLoader.applicationContext.getPackageName());
            intent.putExtra("className", NotificationBadge.componentName.getClassName());
            intent.putExtra("notificationNum", i);
            ApplicationLoader.applicationContext.sendBroadcast(intent);
        }

        @Override // org.telegram.messenger.NotificationBadge.Badger
        public List<String> getSupportLaunchers() {
            return Arrays.asList("com.vivo.launcher");
        }
    }

    public static boolean applyCount(int i) {
        try {
            if (badger == null && !initied) {
                initBadger();
                initied = true;
            }
            Badger badger2 = badger;
            if (badger2 == null) {
                return false;
            }
            badger2.executeBadge(i);
            return true;
        } catch (Throwable unused) {
            return false;
        }
    }

    private static boolean initBadger() {
        Badger badger2;
        Badger badger3;
        Context context = ApplicationLoader.applicationContext;
        Intent launchIntentForPackage = context.getPackageManager().getLaunchIntentForPackage(context.getPackageName());
        if (launchIntentForPackage == null) {
            return false;
        }
        componentName = launchIntentForPackage.getComponent();
        Intent intent = new Intent("android.intent.action.MAIN");
        intent.addCategory("android.intent.category.HOME");
        ResolveInfo resolveActivity = context.getPackageManager().resolveActivity(intent, 65536);
        if (resolveActivity != null) {
            String str = resolveActivity.activityInfo.packageName;
            Iterator<Class<? extends Badger>> it = BADGERS.iterator();
            while (true) {
                if (!it.hasNext()) {
                    break;
                }
                try {
                    badger3 = it.next().newInstance();
                } catch (Exception unused) {
                    badger3 = null;
                }
                if (badger3 != null && badger3.getSupportLaunchers().contains(str)) {
                    badger = badger3;
                    break;
                }
            }
            if (badger != null) {
                return true;
            }
        }
        List<ResolveInfo> queryIntentActivities = context.getPackageManager().queryIntentActivities(intent, 65536);
        if (queryIntentActivities != null) {
            for (int i = 0; i < queryIntentActivities.size(); i++) {
                String str2 = queryIntentActivities.get(i).activityInfo.packageName;
                Iterator<Class<? extends Badger>> it2 = BADGERS.iterator();
                while (true) {
                    if (!it2.hasNext()) {
                        break;
                    }
                    try {
                        badger2 = it2.next().newInstance();
                    } catch (Exception unused2) {
                        badger2 = null;
                    }
                    if (badger2 != null && badger2.getSupportLaunchers().contains(str2)) {
                        badger = badger2;
                        break;
                    }
                }
                if (badger != null) {
                    break;
                }
            }
        }
        if (badger == null) {
            String str3 = Build.MANUFACTURER;
            if (str3.equalsIgnoreCase("Xiaomi")) {
                badger = new XiaomiHomeBadger();
            } else if (str3.equalsIgnoreCase("ZUK")) {
                badger = new ZukHomeBadger();
            } else if (str3.equalsIgnoreCase("OPPO")) {
                badger = new OPPOHomeBader();
            } else if (str3.equalsIgnoreCase("VIVO")) {
                badger = new VivoHomeBadger();
            } else {
                badger = new DefaultBadger();
            }
        }
        return true;
    }

    public static boolean canResolveBroadcast(Intent intent) {
        List<ResolveInfo> queryBroadcastReceivers = ApplicationLoader.applicationContext.getPackageManager().queryBroadcastReceivers(intent, 0);
        return queryBroadcastReceivers != null && queryBroadcastReceivers.size() > 0;
    }

    public static void close(Cursor cursor) {
        if (cursor == null || cursor.isClosed()) {
            return;
        }
        cursor.close();
    }

    public static void closeQuietly(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (Throwable unused) {
            }
        }
    }
}