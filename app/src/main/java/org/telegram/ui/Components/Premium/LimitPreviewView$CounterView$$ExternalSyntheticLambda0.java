package org.telegram.ui.Components.Premium;

import android.animation.ValueAnimator;
import org.telegram.ui.Components.Premium.LimitPreviewView;
/* loaded from: classes3.dex */
public final /* synthetic */ class LimitPreviewView$CounterView$$ExternalSyntheticLambda0 implements ValueAnimator.AnimatorUpdateListener {
    public final /* synthetic */ LimitPreviewView.CounterView f$0;
    public final /* synthetic */ LimitPreviewView.CounterView.AnimatedLayout f$1;

    public /* synthetic */ LimitPreviewView$CounterView$$ExternalSyntheticLambda0(LimitPreviewView.CounterView counterView, LimitPreviewView.CounterView.AnimatedLayout animatedLayout) {
        this.f$0 = counterView;
        this.f$1 = animatedLayout;
    }

    @Override // android.animation.ValueAnimator.AnimatorUpdateListener
    public final void onAnimationUpdate(ValueAnimator valueAnimator) {
        this.f$0.lambda$createAnimationLayouts$0(this.f$1, valueAnimator);
    }
}