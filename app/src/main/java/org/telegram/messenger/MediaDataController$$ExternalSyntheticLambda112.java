package org.telegram.messenger;

import java.util.ArrayList;
/* loaded from: classes.dex */
public final /* synthetic */ class MediaDataController$$ExternalSyntheticLambda112 implements Runnable {
    public final /* synthetic */ MediaDataController f$0;
    public final /* synthetic */ boolean f$1;
    public final /* synthetic */ int f$2;
    public final /* synthetic */ ArrayList f$3;
    public final /* synthetic */ boolean f$4;
    public final /* synthetic */ int f$5;

    public /* synthetic */ MediaDataController$$ExternalSyntheticLambda112(MediaDataController mediaDataController, boolean z, int i, ArrayList arrayList, boolean z2, int i2) {
        this.f$0 = mediaDataController;
        this.f$1 = z;
        this.f$2 = i;
        this.f$3 = arrayList;
        this.f$4 = z2;
        this.f$5 = i2;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$processLoadedRecentDocuments$35(this.f$1, this.f$2, this.f$3, this.f$4, this.f$5);
    }
}