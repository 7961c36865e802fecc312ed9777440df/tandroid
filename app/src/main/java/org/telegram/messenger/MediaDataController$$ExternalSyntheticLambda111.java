package org.telegram.messenger;

import java.util.ArrayList;
/* loaded from: classes.dex */
public final /* synthetic */ class MediaDataController$$ExternalSyntheticLambda111 implements Runnable {
    public final /* synthetic */ MediaDataController f$0;
    public final /* synthetic */ boolean f$1;
    public final /* synthetic */ int f$2;
    public final /* synthetic */ ArrayList f$3;

    public /* synthetic */ MediaDataController$$ExternalSyntheticLambda111(MediaDataController mediaDataController, boolean z, int i, ArrayList arrayList) {
        this.f$0 = mediaDataController;
        this.f$1 = z;
        this.f$2 = i;
        this.f$3 = arrayList;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$processLoadedRecentDocuments$36(this.f$1, this.f$2, this.f$3);
    }
}