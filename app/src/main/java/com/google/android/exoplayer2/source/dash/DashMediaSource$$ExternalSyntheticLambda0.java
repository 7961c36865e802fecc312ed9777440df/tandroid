package com.google.android.exoplayer2.source.dash;
/* loaded from: classes.dex */
public final /* synthetic */ class DashMediaSource$$ExternalSyntheticLambda0 implements Runnable {
    public final /* synthetic */ DashMediaSource f$0;

    public /* synthetic */ DashMediaSource$$ExternalSyntheticLambda0(DashMediaSource dashMediaSource) {
        this.f$0 = dashMediaSource;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.startLoadingManifest();
    }
}