package org.telegram.ui.Components;

import android.animation.ValueAnimator;
/* loaded from: classes3.dex */
public final /* synthetic */ class JoinToSendSettingsView$$ExternalSyntheticLambda0 implements ValueAnimator.AnimatorUpdateListener {
    public final /* synthetic */ JoinToSendSettingsView f$0;

    public /* synthetic */ JoinToSendSettingsView$$ExternalSyntheticLambda0(JoinToSendSettingsView joinToSendSettingsView) {
        this.f$0 = joinToSendSettingsView;
    }

    @Override // android.animation.ValueAnimator.AnimatorUpdateListener
    public final void onAnimationUpdate(ValueAnimator valueAnimator) {
        this.f$0.lambda$setJoinToSend$6(valueAnimator);
    }
}