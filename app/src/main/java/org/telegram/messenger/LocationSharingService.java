package org.telegram.messenger;

import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import java.util.ArrayList;
import org.telegram.messenger.LocationController;
import org.telegram.messenger.NotificationCenter;
import org.telegram.tgnet.TLRPC$Chat;
import org.telegram.ui.LaunchActivity;
/* loaded from: classes.dex */
public class LocationSharingService extends Service implements NotificationCenter.NotificationCenterDelegate {
    private NotificationCompat.Builder builder;
    private Handler handler;
    private Runnable runnable;

    @Override // android.app.Service
    public IBinder onBind(Intent intent) {
        return null;
    }

    public LocationSharingService() {
        NotificationCenter.getGlobalInstance().addObserver(this, NotificationCenter.liveLocationsChanged);
    }

    @Override // android.app.Service
    public void onCreate() {
        super.onCreate();
        this.handler = new Handler();
        LocationSharingService$$ExternalSyntheticLambda1 locationSharingService$$ExternalSyntheticLambda1 = new LocationSharingService$$ExternalSyntheticLambda1(this);
        this.runnable = locationSharingService$$ExternalSyntheticLambda1;
        this.handler.postDelayed(locationSharingService$$ExternalSyntheticLambda1, 1000L);
    }

    public /* synthetic */ void lambda$onCreate$1() {
        this.handler.postDelayed(this.runnable, 1000L);
        Utilities.stageQueue.postRunnable(LocationSharingService$$ExternalSyntheticLambda2.INSTANCE);
    }

    public static /* synthetic */ void lambda$onCreate$0() {
        for (int i = 0; i < 4; i++) {
            LocationController.getInstance(i).update();
        }
    }

    @Override // android.app.Service
    public void onDestroy() {
        super.onDestroy();
        Handler handler = this.handler;
        if (handler != null) {
            handler.removeCallbacks(this.runnable);
        }
        stopForeground(true);
        NotificationManagerCompat.from(ApplicationLoader.applicationContext).cancel(6);
        NotificationCenter.getGlobalInstance().removeObserver(this, NotificationCenter.liveLocationsChanged);
    }

    @Override // org.telegram.messenger.NotificationCenter.NotificationCenterDelegate
    public void didReceivedNotification(int i, int i2, Object... objArr) {
        Handler handler;
        if (i != NotificationCenter.liveLocationsChanged || (handler = this.handler) == null) {
            return;
        }
        handler.post(new LocationSharingService$$ExternalSyntheticLambda0(this));
    }

    public /* synthetic */ void lambda$didReceivedNotification$2() {
        if (getInfos().isEmpty()) {
            stopSelf();
        } else {
            updateNotification(true);
        }
    }

    private ArrayList<LocationController.SharingLocationInfo> getInfos() {
        ArrayList<LocationController.SharingLocationInfo> arrayList = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            ArrayList<LocationController.SharingLocationInfo> arrayList2 = LocationController.getInstance(i).sharingLocationsUI;
            if (!arrayList2.isEmpty()) {
                arrayList.addAll(arrayList2);
            }
        }
        return arrayList;
    }

    private void updateNotification(boolean z) {
        String str;
        String str2;
        if (this.builder == null) {
            return;
        }
        ArrayList<LocationController.SharingLocationInfo> infos = getInfos();
        if (infos.size() == 1) {
            LocationController.SharingLocationInfo sharingLocationInfo = infos.get(0);
            long dialogId = sharingLocationInfo.messageObject.getDialogId();
            int i = sharingLocationInfo.messageObject.currentAccount;
            if (DialogObject.isUserDialog(dialogId)) {
                str2 = UserObject.getFirstName(MessagesController.getInstance(i).getUser(Long.valueOf(dialogId)));
                str = LocaleController.getString("AttachLiveLocationIsSharing", 2131624489);
            } else {
                TLRPC$Chat chat = MessagesController.getInstance(i).getChat(Long.valueOf(-dialogId));
                str2 = chat != null ? chat.title : "";
                str = LocaleController.getString("AttachLiveLocationIsSharingChat", 2131624490);
            }
        } else {
            str2 = LocaleController.formatPluralString("Chats", infos.size(), new Object[0]);
            str = LocaleController.getString("AttachLiveLocationIsSharingChats", 2131624491);
        }
        String format = String.format(str, LocaleController.getString("AttachLiveLocation", 2131624488), str2);
        this.builder.setTicker(format);
        this.builder.setContentText(format);
        if (!z) {
            return;
        }
        NotificationManagerCompat.from(ApplicationLoader.applicationContext).notify(6, this.builder.build());
    }

    @Override // android.app.Service
    public int onStartCommand(Intent intent, int i, int i2) {
        if (getInfos().isEmpty()) {
            stopSelf();
        }
        if (this.builder == null) {
            Intent intent2 = new Intent(ApplicationLoader.applicationContext, LaunchActivity.class);
            intent2.setAction("org.tmessages.openlocations");
            intent2.addCategory("android.intent.category.LAUNCHER");
            PendingIntent activity = PendingIntent.getActivity(ApplicationLoader.applicationContext, 0, intent2, 0);
            NotificationCompat.Builder builder = new NotificationCompat.Builder(ApplicationLoader.applicationContext);
            this.builder = builder;
            builder.setWhen(System.currentTimeMillis());
            this.builder.setSmallIcon(2131165581);
            this.builder.setContentIntent(activity);
            NotificationsController.checkOtherNotificationsChannel();
            this.builder.setChannelId(NotificationsController.OTHER_NOTIFICATIONS_CHANNEL);
            this.builder.setContentTitle(LocaleController.getString("AppName", 2131624375));
            this.builder.addAction(0, LocaleController.getString("StopLiveLocation", 2131628466), PendingIntent.getBroadcast(ApplicationLoader.applicationContext, 2, new Intent(ApplicationLoader.applicationContext, StopLiveLocationReceiver.class), 134217728));
        }
        updateNotification(false);
        startForeground(6, this.builder.build());
        return 2;
    }
}