package org.telegram.ui.ActionBar;

import android.animation.ValueAnimator;
/* loaded from: classes3.dex */
public final /* synthetic */ class AdjustPanLayoutHelper$$ExternalSyntheticLambda0 implements ValueAnimator.AnimatorUpdateListener {
    public final /* synthetic */ AdjustPanLayoutHelper f$0;

    public /* synthetic */ AdjustPanLayoutHelper$$ExternalSyntheticLambda0(AdjustPanLayoutHelper adjustPanLayoutHelper) {
        this.f$0 = adjustPanLayoutHelper;
    }

    @Override // android.animation.ValueAnimator.AnimatorUpdateListener
    public final void onAnimationUpdate(ValueAnimator valueAnimator) {
        this.f$0.lambda$animateHeight$0(valueAnimator);
    }
}