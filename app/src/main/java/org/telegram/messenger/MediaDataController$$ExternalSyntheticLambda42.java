package org.telegram.messenger;

import java.util.ArrayList;
/* loaded from: classes.dex */
public final /* synthetic */ class MediaDataController$$ExternalSyntheticLambda42 implements Runnable {
    public final /* synthetic */ MediaDataController f$0;
    public final /* synthetic */ long f$1;
    public final /* synthetic */ long f$2;
    public final /* synthetic */ ArrayList f$3;

    public /* synthetic */ MediaDataController$$ExternalSyntheticLambda42(MediaDataController mediaDataController, long j, long j2, ArrayList arrayList) {
        this.f$0 = mediaDataController;
        this.f$1 = j;
        this.f$2 = j2;
        this.f$3 = arrayList;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$loadPinnedMessages$123(this.f$1, this.f$2, this.f$3);
    }
}