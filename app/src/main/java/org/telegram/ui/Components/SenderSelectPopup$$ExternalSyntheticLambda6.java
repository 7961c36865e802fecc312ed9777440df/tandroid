package org.telegram.ui.Components;

import androidx.dynamicanimation.animation.DynamicAnimation;
/* loaded from: classes3.dex */
public final /* synthetic */ class SenderSelectPopup$$ExternalSyntheticLambda6 implements DynamicAnimation.OnAnimationUpdateListener {
    public final /* synthetic */ SenderSelectPopup f$0;

    public /* synthetic */ SenderSelectPopup$$ExternalSyntheticLambda6(SenderSelectPopup senderSelectPopup) {
        this.f$0 = senderSelectPopup;
    }

    @Override // androidx.dynamicanimation.animation.DynamicAnimation.OnAnimationUpdateListener
    public final void onAnimationUpdate(DynamicAnimation dynamicAnimation, float f, float f2) {
        this.f$0.lambda$startShowAnimation$1(dynamicAnimation, f, f2);
    }
}