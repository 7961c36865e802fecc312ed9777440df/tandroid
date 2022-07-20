package org.telegram.messenger.voip;

import android.app.Activity;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import org.telegram.messenger.AccountInstance;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.UserConfig;
import org.telegram.tgnet.TLRPC$User;
import org.telegram.tgnet.TLRPC$UserFull;
import org.telegram.ui.Components.voip.VoIPHelper;
/* loaded from: classes.dex */
public final class VoIPPendingCall {
    private AccountInstance accountInstance;
    private final Activity activity;
    private Handler handler;
    private NotificationCenter notificationCenter;
    private final NotificationCenter.NotificationCenterDelegate observer;
    private final Runnable releaseRunnable;
    private boolean released;
    private final long userId;
    private final boolean video;

    public static VoIPPendingCall startOrSchedule(Activity activity, long j, boolean z, AccountInstance accountInstance) {
        return new VoIPPendingCall(activity, j, z, 1000L, accountInstance);
    }

    public /* synthetic */ void lambda$new$0(int i, int i2, Object[] objArr) {
        if (i == NotificationCenter.didUpdateConnectionState) {
            onConnectionStateUpdated(false);
        }
    }

    public /* synthetic */ void lambda$new$1() {
        onConnectionStateUpdated(true);
    }

    private VoIPPendingCall(Activity activity, long j, boolean z, long j2, AccountInstance accountInstance) {
        VoIPPendingCall$$ExternalSyntheticLambda1 voIPPendingCall$$ExternalSyntheticLambda1 = new VoIPPendingCall$$ExternalSyntheticLambda1(this);
        this.observer = voIPPendingCall$$ExternalSyntheticLambda1;
        VoIPPendingCall$$ExternalSyntheticLambda0 voIPPendingCall$$ExternalSyntheticLambda0 = new VoIPPendingCall$$ExternalSyntheticLambda0(this);
        this.releaseRunnable = voIPPendingCall$$ExternalSyntheticLambda0;
        this.activity = activity;
        this.userId = j;
        this.video = z;
        this.accountInstance = accountInstance;
        if (!onConnectionStateUpdated(false)) {
            NotificationCenter notificationCenter = NotificationCenter.getInstance(UserConfig.selectedAccount);
            this.notificationCenter = notificationCenter;
            notificationCenter.addObserver(voIPPendingCall$$ExternalSyntheticLambda1, NotificationCenter.didUpdateConnectionState);
            Handler handler = new Handler(Looper.myLooper());
            this.handler = handler;
            handler.postDelayed(voIPPendingCall$$ExternalSyntheticLambda0, j2);
        }
    }

    private boolean onConnectionStateUpdated(boolean z) {
        if (this.released || (!z && !isConnected(this.accountInstance) && !isAirplaneMode())) {
            return false;
        }
        MessagesController messagesController = this.accountInstance.getMessagesController();
        TLRPC$User user = messagesController.getUser(Long.valueOf(this.userId));
        if (user != null) {
            TLRPC$UserFull userFull = messagesController.getUserFull(user.id);
            VoIPHelper.startCall(user, this.video, userFull != null && userFull.video_calls_available, this.activity, userFull, this.accountInstance);
        } else if (isAirplaneMode()) {
            VoIPHelper.startCall(null, this.video, false, this.activity, null, this.accountInstance);
        }
        release();
        return true;
    }

    private boolean isConnected(AccountInstance accountInstance) {
        return accountInstance.getConnectionsManager().getConnectionState() == 3;
    }

    private boolean isAirplaneMode() {
        return Settings.System.getInt(this.activity.getContentResolver(), "airplane_mode_on", 0) != 0;
    }

    public void release() {
        if (!this.released) {
            NotificationCenter notificationCenter = this.notificationCenter;
            if (notificationCenter != null) {
                notificationCenter.removeObserver(this.observer, NotificationCenter.didUpdateConnectionState);
            }
            Handler handler = this.handler;
            if (handler != null) {
                handler.removeCallbacks(this.releaseRunnable);
            }
            this.released = true;
        }
    }
}