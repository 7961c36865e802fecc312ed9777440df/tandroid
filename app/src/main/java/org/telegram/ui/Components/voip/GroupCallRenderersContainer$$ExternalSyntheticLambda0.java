package org.telegram.ui.Components.voip;

import android.animation.ValueAnimator;
/* loaded from: classes3.dex */
public final /* synthetic */ class GroupCallRenderersContainer$$ExternalSyntheticLambda0 implements ValueAnimator.AnimatorUpdateListener {
    public final /* synthetic */ GroupCallRenderersContainer f$0;

    public /* synthetic */ GroupCallRenderersContainer$$ExternalSyntheticLambda0(GroupCallRenderersContainer groupCallRenderersContainer) {
        this.f$0 = groupCallRenderersContainer;
    }

    @Override // android.animation.ValueAnimator.AnimatorUpdateListener
    public final void onAnimationUpdate(ValueAnimator valueAnimator) {
        this.f$0.lambda$requestFullscreen$6(valueAnimator);
    }
}