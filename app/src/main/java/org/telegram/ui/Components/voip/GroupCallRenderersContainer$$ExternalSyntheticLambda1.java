package org.telegram.ui.Components.voip;

import android.animation.ValueAnimator;
/* loaded from: classes3.dex */
public final /* synthetic */ class GroupCallRenderersContainer$$ExternalSyntheticLambda1 implements ValueAnimator.AnimatorUpdateListener {
    public final /* synthetic */ GroupCallRenderersContainer f$0;

    public /* synthetic */ GroupCallRenderersContainer$$ExternalSyntheticLambda1(GroupCallRenderersContainer groupCallRenderersContainer) {
        this.f$0 = groupCallRenderersContainer;
    }

    @Override // android.animation.ValueAnimator.AnimatorUpdateListener
    public final void onAnimationUpdate(ValueAnimator valueAnimator) {
        this.f$0.lambda$animateSwipeToBack$7(valueAnimator);
    }
}