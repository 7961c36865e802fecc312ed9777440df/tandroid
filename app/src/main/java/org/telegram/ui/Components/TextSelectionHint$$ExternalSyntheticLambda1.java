package org.telegram.ui.Components;

import android.animation.ValueAnimator;
/* loaded from: classes3.dex */
public final /* synthetic */ class TextSelectionHint$$ExternalSyntheticLambda1 implements ValueAnimator.AnimatorUpdateListener {
    public final /* synthetic */ TextSelectionHint f$0;

    public /* synthetic */ TextSelectionHint$$ExternalSyntheticLambda1(TextSelectionHint textSelectionHint) {
        this.f$0 = textSelectionHint;
    }

    @Override // android.animation.ValueAnimator.AnimatorUpdateListener
    public final void onAnimationUpdate(ValueAnimator valueAnimator) {
        this.f$0.lambda$show$2(valueAnimator);
    }
}