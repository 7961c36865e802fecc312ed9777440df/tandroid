package org.telegram.messenger;

import java.util.ArrayList;
/* loaded from: classes.dex */
public final /* synthetic */ class MediaDataController$$ExternalSyntheticLambda64 implements Runnable {
    public final /* synthetic */ MediaDataController f$0;
    public final /* synthetic */ ArrayList f$1;

    public /* synthetic */ MediaDataController$$ExternalSyntheticLambda64(MediaDataController mediaDataController, ArrayList arrayList) {
        this.f$0 = mediaDataController;
        this.f$1 = arrayList;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$broadcastPinnedMessage$128(this.f$1);
    }
}