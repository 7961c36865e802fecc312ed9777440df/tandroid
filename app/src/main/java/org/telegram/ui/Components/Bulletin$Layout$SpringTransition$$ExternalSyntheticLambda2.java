package org.telegram.ui.Components;

import androidx.core.util.Consumer;
import androidx.dynamicanimation.animation.DynamicAnimation;
import org.telegram.ui.Components.Bulletin;
/* loaded from: classes3.dex */
public final /* synthetic */ class Bulletin$Layout$SpringTransition$$ExternalSyntheticLambda2 implements DynamicAnimation.OnAnimationUpdateListener {
    public final /* synthetic */ Consumer f$0;
    public final /* synthetic */ Bulletin.Layout f$1;

    public /* synthetic */ Bulletin$Layout$SpringTransition$$ExternalSyntheticLambda2(Consumer consumer, Bulletin.Layout layout) {
        this.f$0 = consumer;
        this.f$1 = layout;
    }

    @Override // androidx.dynamicanimation.animation.DynamicAnimation.OnAnimationUpdateListener
    public final void onAnimationUpdate(DynamicAnimation dynamicAnimation, float f, float f2) {
        Bulletin.Layout.SpringTransition.lambda$animateExit$3(this.f$0, this.f$1, dynamicAnimation, f, f2);
    }
}