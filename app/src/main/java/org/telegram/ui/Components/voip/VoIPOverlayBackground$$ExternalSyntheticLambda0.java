package org.telegram.ui.Components.voip;

import android.animation.ValueAnimator;
/* loaded from: classes3.dex */
public final /* synthetic */ class VoIPOverlayBackground$$ExternalSyntheticLambda0 implements ValueAnimator.AnimatorUpdateListener {
    public final /* synthetic */ VoIPOverlayBackground f$0;

    public /* synthetic */ VoIPOverlayBackground$$ExternalSyntheticLambda0(VoIPOverlayBackground voIPOverlayBackground) {
        this.f$0 = voIPOverlayBackground;
    }

    @Override // android.animation.ValueAnimator.AnimatorUpdateListener
    public final void onAnimationUpdate(ValueAnimator valueAnimator) {
        this.f$0.lambda$setShowBlackout$2(valueAnimator);
    }
}