package org.telegram.ui.Components;

import android.animation.ValueAnimator;
import org.telegram.ui.Components.BotWebViewMenuContainer;
/* loaded from: classes3.dex */
public final /* synthetic */ class BotWebViewMenuContainer$1$$ExternalSyntheticLambda0 implements ValueAnimator.AnimatorUpdateListener {
    public final /* synthetic */ BotWebViewMenuContainer.AnonymousClass1 f$0;
    public final /* synthetic */ int f$1;
    public final /* synthetic */ int f$2;

    public /* synthetic */ BotWebViewMenuContainer$1$$ExternalSyntheticLambda0(BotWebViewMenuContainer.AnonymousClass1 anonymousClass1, int i, int i2) {
        this.f$0 = anonymousClass1;
        this.f$1 = i;
        this.f$2 = i2;
    }

    @Override // android.animation.ValueAnimator.AnimatorUpdateListener
    public final void onAnimationUpdate(ValueAnimator valueAnimator) {
        this.f$0.lambda$onWebAppSetActionBarColor$0(this.f$1, this.f$2, valueAnimator);
    }
}