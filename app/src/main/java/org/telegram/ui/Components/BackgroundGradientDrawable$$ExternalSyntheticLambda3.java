package org.telegram.ui.Components;

import org.telegram.ui.Components.BackgroundGradientDrawable;
/* loaded from: classes3.dex */
public final /* synthetic */ class BackgroundGradientDrawable$$ExternalSyntheticLambda3 implements BackgroundGradientDrawable.Disposable {
    public final /* synthetic */ BackgroundGradientDrawable f$0;
    public final /* synthetic */ BackgroundGradientDrawable.Listener[] f$1;
    public final /* synthetic */ Runnable[] f$2;
    public final /* synthetic */ IntSize[] f$3;

    public /* synthetic */ BackgroundGradientDrawable$$ExternalSyntheticLambda3(BackgroundGradientDrawable backgroundGradientDrawable, BackgroundGradientDrawable.Listener[] listenerArr, Runnable[] runnableArr, IntSize[] intSizeArr) {
        this.f$0 = backgroundGradientDrawable;
        this.f$1 = listenerArr;
        this.f$2 = runnableArr;
        this.f$3 = intSizeArr;
    }

    @Override // org.telegram.ui.Components.BackgroundGradientDrawable.Disposable
    public final void dispose() {
        this.f$0.lambda$startDitheringInternal$3(this.f$1, this.f$2, this.f$3);
    }
}