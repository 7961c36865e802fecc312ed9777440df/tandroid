package org.telegram.messenger;

import java.util.ArrayList;
/* loaded from: classes.dex */
public final /* synthetic */ class MediaDataController$$ExternalSyntheticLambda71 implements Runnable {
    public final /* synthetic */ MediaDataController f$0;
    public final /* synthetic */ ArrayList f$1;
    public final /* synthetic */ long f$2;
    public final /* synthetic */ int f$3;

    public /* synthetic */ MediaDataController$$ExternalSyntheticLambda71(MediaDataController mediaDataController, ArrayList arrayList, long j, int i) {
        this.f$0 = mediaDataController;
        this.f$1 = arrayList;
        this.f$2 = j;
        this.f$3 = i;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$processLoadedStickers$72(this.f$1, this.f$2, this.f$3);
    }
}