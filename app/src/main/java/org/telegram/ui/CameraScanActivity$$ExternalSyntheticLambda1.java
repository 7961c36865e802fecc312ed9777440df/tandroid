package org.telegram.ui;

import android.animation.ValueAnimator;
/* loaded from: classes3.dex */
public final /* synthetic */ class CameraScanActivity$$ExternalSyntheticLambda1 implements ValueAnimator.AnimatorUpdateListener {
    public final /* synthetic */ CameraScanActivity f$0;

    public /* synthetic */ CameraScanActivity$$ExternalSyntheticLambda1(CameraScanActivity cameraScanActivity) {
        this.f$0 = cameraScanActivity;
    }

    @Override // android.animation.ValueAnimator.AnimatorUpdateListener
    public final void onAnimationUpdate(ValueAnimator valueAnimator) {
        this.f$0.lambda$createView$3(valueAnimator);
    }
}