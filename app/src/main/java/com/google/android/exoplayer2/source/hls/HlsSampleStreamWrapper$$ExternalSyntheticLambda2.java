package com.google.android.exoplayer2.source.hls;
/* loaded from: classes.dex */
public final /* synthetic */ class HlsSampleStreamWrapper$$ExternalSyntheticLambda2 implements Runnable {
    public final /* synthetic */ HlsSampleStreamWrapper f$0;

    public /* synthetic */ HlsSampleStreamWrapper$$ExternalSyntheticLambda2(HlsSampleStreamWrapper hlsSampleStreamWrapper) {
        this.f$0 = hlsSampleStreamWrapper;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.maybeFinishPrepare();
    }
}