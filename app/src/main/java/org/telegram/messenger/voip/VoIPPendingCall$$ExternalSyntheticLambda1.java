package org.telegram.messenger.voip;

import org.telegram.messenger.NotificationCenter;
/* loaded from: classes.dex */
public final /* synthetic */ class VoIPPendingCall$$ExternalSyntheticLambda1 implements NotificationCenter.NotificationCenterDelegate {
    public final /* synthetic */ VoIPPendingCall f$0;

    public /* synthetic */ VoIPPendingCall$$ExternalSyntheticLambda1(VoIPPendingCall voIPPendingCall) {
        this.f$0 = voIPPendingCall;
    }

    @Override // org.telegram.messenger.NotificationCenter.NotificationCenterDelegate
    public final void didReceivedNotification(int i, int i2, Object[] objArr) {
        this.f$0.lambda$new$0(i, i2, objArr);
    }
}