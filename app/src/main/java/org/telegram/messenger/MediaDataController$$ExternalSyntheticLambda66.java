package org.telegram.messenger;

import java.util.ArrayList;
/* loaded from: classes.dex */
public final /* synthetic */ class MediaDataController$$ExternalSyntheticLambda66 implements Runnable {
    public final /* synthetic */ MediaDataController f$0;
    public final /* synthetic */ ArrayList f$1;
    public final /* synthetic */ int f$2;
    public final /* synthetic */ int f$3;

    public /* synthetic */ MediaDataController$$ExternalSyntheticLambda66(MediaDataController mediaDataController, ArrayList arrayList, int i, int i2) {
        this.f$0 = mediaDataController;
        this.f$1 = arrayList;
        this.f$2 = i;
        this.f$3 = i2;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$putReactionsToCache$13(this.f$1, this.f$2, this.f$3);
    }
}