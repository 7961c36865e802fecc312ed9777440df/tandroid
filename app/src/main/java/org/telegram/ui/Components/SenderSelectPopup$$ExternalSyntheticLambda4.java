package org.telegram.ui.Components;

import androidx.dynamicanimation.animation.DynamicAnimation;
/* loaded from: classes3.dex */
public final /* synthetic */ class SenderSelectPopup$$ExternalSyntheticLambda4 implements DynamicAnimation.OnAnimationUpdateListener {
    public final /* synthetic */ SenderSelectPopup f$0;

    public /* synthetic */ SenderSelectPopup$$ExternalSyntheticLambda4(SenderSelectPopup senderSelectPopup) {
        this.f$0 = senderSelectPopup;
    }

    @Override // androidx.dynamicanimation.animation.DynamicAnimation.OnAnimationUpdateListener
    public final void onAnimationUpdate(DynamicAnimation dynamicAnimation, float f, float f2) {
        this.f$0.lambda$startShowAnimation$2(dynamicAnimation, f, f2);
    }
}