package org.telegram.messenger;

import androidx.collection.LongSparseArray;
import java.util.ArrayList;
/* loaded from: classes.dex */
public final /* synthetic */ class MediaDataController$$ExternalSyntheticLambda73 implements Runnable {
    public final /* synthetic */ MediaDataController f$0;
    public final /* synthetic */ ArrayList f$1;
    public final /* synthetic */ LongSparseArray f$2;
    public final /* synthetic */ ArrayList f$3;
    public final /* synthetic */ long f$4;
    public final /* synthetic */ int f$5;
    public final /* synthetic */ boolean f$6;

    public /* synthetic */ MediaDataController$$ExternalSyntheticLambda73(MediaDataController mediaDataController, ArrayList arrayList, LongSparseArray longSparseArray, ArrayList arrayList2, long j, int i, boolean z) {
        this.f$0 = mediaDataController;
        this.f$1 = arrayList;
        this.f$2 = longSparseArray;
        this.f$3 = arrayList2;
        this.f$4 = j;
        this.f$5 = i;
        this.f$6 = z;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$processLoadedFeaturedStickers$43(this.f$1, this.f$2, this.f$3, this.f$4, this.f$5, this.f$6);
    }
}