package org.telegram.messenger;

import java.util.ArrayList;
/* loaded from: classes.dex */
public final /* synthetic */ class MediaDataController$$ExternalSyntheticLambda113 implements Runnable {
    public final /* synthetic */ MediaDataController f$0;
    public final /* synthetic */ boolean f$1;
    public final /* synthetic */ ArrayList f$2;
    public final /* synthetic */ int f$3;

    public /* synthetic */ MediaDataController$$ExternalSyntheticLambda113(MediaDataController mediaDataController, boolean z, ArrayList arrayList, int i) {
        this.f$0 = mediaDataController;
        this.f$1 = z;
        this.f$2 = arrayList;
        this.f$3 = i;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$loadRecents$31(this.f$1, this.f$2, this.f$3);
    }
}