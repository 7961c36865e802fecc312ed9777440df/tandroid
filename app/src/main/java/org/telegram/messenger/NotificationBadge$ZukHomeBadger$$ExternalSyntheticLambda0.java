package org.telegram.messenger;

import android.os.Bundle;
import org.telegram.messenger.NotificationBadge;
/* loaded from: classes.dex */
public final /* synthetic */ class NotificationBadge$ZukHomeBadger$$ExternalSyntheticLambda0 implements Runnable {
    public final /* synthetic */ NotificationBadge.ZukHomeBadger f$0;
    public final /* synthetic */ Bundle f$1;

    public /* synthetic */ NotificationBadge$ZukHomeBadger$$ExternalSyntheticLambda0(NotificationBadge.ZukHomeBadger zukHomeBadger, Bundle bundle) {
        this.f$0 = zukHomeBadger;
        this.f$1 = bundle;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$executeBadge$0(this.f$1);
    }
}