package org.telegram.ui;

import android.animation.ValueAnimator;
/* loaded from: classes3.dex */
public final /* synthetic */ class CameraScanActivity$$ExternalSyntheticLambda0 implements ValueAnimator.AnimatorUpdateListener {
    public final /* synthetic */ CameraScanActivity f$0;

    public /* synthetic */ CameraScanActivity$$ExternalSyntheticLambda0(CameraScanActivity cameraScanActivity) {
        this.f$0 = cameraScanActivity;
    }

    @Override // android.animation.ValueAnimator.AnimatorUpdateListener
    public final void onAnimationUpdate(ValueAnimator valueAnimator) {
        this.f$0.lambda$updateRecognized$5(valueAnimator);
    }
}