package org.telegram.ui.Components;

import android.animation.ValueAnimator;
/* loaded from: classes3.dex */
public final /* synthetic */ class SimpleAvatarView$$ExternalSyntheticLambda0 implements ValueAnimator.AnimatorUpdateListener {
    public final /* synthetic */ SimpleAvatarView f$0;

    public /* synthetic */ SimpleAvatarView$$ExternalSyntheticLambda0(SimpleAvatarView simpleAvatarView) {
        this.f$0 = simpleAvatarView;
    }

    @Override // android.animation.ValueAnimator.AnimatorUpdateListener
    public final void onAnimationUpdate(ValueAnimator valueAnimator) {
        this.f$0.lambda$setSelected$0(valueAnimator);
    }
}