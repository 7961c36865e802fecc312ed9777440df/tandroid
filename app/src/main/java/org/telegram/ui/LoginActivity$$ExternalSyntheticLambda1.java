package org.telegram.ui;

import android.animation.ValueAnimator;
/* loaded from: classes3.dex */
public final /* synthetic */ class LoginActivity$$ExternalSyntheticLambda1 implements ValueAnimator.AnimatorUpdateListener {
    public final /* synthetic */ LoginActivity f$0;

    public /* synthetic */ LoginActivity$$ExternalSyntheticLambda1(LoginActivity loginActivity) {
        this.f$0 = loginActivity;
    }

    @Override // android.animation.ValueAnimator.AnimatorUpdateListener
    public final void onAnimationUpdate(ValueAnimator valueAnimator) {
        this.f$0.lambda$setCustomKeyboardVisible$4(valueAnimator);
    }
}