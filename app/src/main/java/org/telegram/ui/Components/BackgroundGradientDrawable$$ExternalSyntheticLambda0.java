package org.telegram.ui.Components;

import org.telegram.ui.Components.BackgroundGradientDrawable;
/* loaded from: classes3.dex */
public final /* synthetic */ class BackgroundGradientDrawable$$ExternalSyntheticLambda0 implements Runnable {
    public final /* synthetic */ BackgroundGradientDrawable f$0;
    public final /* synthetic */ IntSize f$1;
    public final /* synthetic */ Runnable[] f$2;
    public final /* synthetic */ int f$3;
    public final /* synthetic */ BackgroundGradientDrawable.Listener[] f$4;

    public /* synthetic */ BackgroundGradientDrawable$$ExternalSyntheticLambda0(BackgroundGradientDrawable backgroundGradientDrawable, IntSize intSize, Runnable[] runnableArr, int i, BackgroundGradientDrawable.Listener[] listenerArr) {
        this.f$0 = backgroundGradientDrawable;
        this.f$1 = intSize;
        this.f$2 = runnableArr;
        this.f$3 = i;
        this.f$4 = listenerArr;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$startDitheringInternal$2(this.f$1, this.f$2, this.f$3, this.f$4);
    }
}