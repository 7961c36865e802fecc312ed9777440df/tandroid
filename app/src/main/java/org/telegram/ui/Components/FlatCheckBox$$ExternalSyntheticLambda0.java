package org.telegram.ui.Components;

import android.animation.ValueAnimator;
/* loaded from: classes3.dex */
public final /* synthetic */ class FlatCheckBox$$ExternalSyntheticLambda0 implements ValueAnimator.AnimatorUpdateListener {
    public final /* synthetic */ FlatCheckBox f$0;

    public /* synthetic */ FlatCheckBox$$ExternalSyntheticLambda0(FlatCheckBox flatCheckBox) {
        this.f$0 = flatCheckBox;
    }

    @Override // android.animation.ValueAnimator.AnimatorUpdateListener
    public final void onAnimationUpdate(ValueAnimator valueAnimator) {
        this.f$0.lambda$setChecked$0(valueAnimator);
    }
}