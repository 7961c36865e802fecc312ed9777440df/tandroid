package org.telegram.ui.Components;

import android.animation.ValueAnimator;
/* loaded from: classes3.dex */
public final /* synthetic */ class BotWebViewMenuContainer$$ExternalSyntheticLambda0 implements ValueAnimator.AnimatorUpdateListener {
    public final /* synthetic */ BotWebViewMenuContainer f$0;

    public /* synthetic */ BotWebViewMenuContainer$$ExternalSyntheticLambda0(BotWebViewMenuContainer botWebViewMenuContainer) {
        this.f$0 = botWebViewMenuContainer;
    }

    @Override // android.animation.ValueAnimator.AnimatorUpdateListener
    public final void onAnimationUpdate(ValueAnimator valueAnimator) {
        this.f$0.lambda$onDismiss$17(valueAnimator);
    }
}