package org.telegram.messenger;
/* loaded from: classes.dex */
public final /* synthetic */ class MediaDataController$$ExternalSyntheticLambda47 implements Runnable {
    public final /* synthetic */ MediaDataController f$0;
    public final /* synthetic */ long f$1;
    public final /* synthetic */ boolean f$2;
    public final /* synthetic */ int f$3;
    public final /* synthetic */ int f$4;
    public final /* synthetic */ int f$5;
    public final /* synthetic */ int f$6;

    public /* synthetic */ MediaDataController$$ExternalSyntheticLambda47(MediaDataController mediaDataController, long j, boolean z, int i, int i2, int i3, int i4) {
        this.f$0 = mediaDataController;
        this.f$1 = j;
        this.f$2 = z;
        this.f$3 = i;
        this.f$4 = i2;
        this.f$5 = i3;
        this.f$6 = i4;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$processLoadedMediaCount$97(this.f$1, this.f$2, this.f$3, this.f$4, this.f$5, this.f$6);
    }
}