package org.telegram.messenger;

import java.util.ArrayList;
/* loaded from: classes.dex */
public final /* synthetic */ class MediaDataController$$ExternalSyntheticLambda115 implements Runnable {
    public final /* synthetic */ MediaDataController f$0;
    public final /* synthetic */ boolean f$1;
    public final /* synthetic */ ArrayList f$2;
    public final /* synthetic */ int f$3;
    public final /* synthetic */ long f$4;
    public final /* synthetic */ ArrayList f$5;
    public final /* synthetic */ boolean f$6;

    public /* synthetic */ MediaDataController$$ExternalSyntheticLambda115(MediaDataController mediaDataController, boolean z, ArrayList arrayList, int i, long j, ArrayList arrayList2, boolean z2) {
        this.f$0 = mediaDataController;
        this.f$1 = z;
        this.f$2 = arrayList;
        this.f$3 = i;
        this.f$4 = j;
        this.f$5 = arrayList2;
        this.f$6 = z2;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$processLoadedFeaturedStickers$45(this.f$1, this.f$2, this.f$3, this.f$4, this.f$5, this.f$6);
    }
}