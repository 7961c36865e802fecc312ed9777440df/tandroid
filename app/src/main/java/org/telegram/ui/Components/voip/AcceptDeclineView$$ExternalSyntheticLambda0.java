package org.telegram.ui.Components.voip;

import android.animation.ValueAnimator;
/* loaded from: classes3.dex */
public final /* synthetic */ class AcceptDeclineView$$ExternalSyntheticLambda0 implements ValueAnimator.AnimatorUpdateListener {
    public final /* synthetic */ AcceptDeclineView f$0;

    public /* synthetic */ AcceptDeclineView$$ExternalSyntheticLambda0(AcceptDeclineView acceptDeclineView) {
        this.f$0 = acceptDeclineView;
    }

    @Override // android.animation.ValueAnimator.AnimatorUpdateListener
    public final void onAnimationUpdate(ValueAnimator valueAnimator) {
        this.f$0.lambda$onTouchEvent$0(valueAnimator);
    }
}