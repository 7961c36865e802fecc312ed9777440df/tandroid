package org.telegram.ui;

import android.animation.ValueAnimator;
/* loaded from: classes3.dex */
public final /* synthetic */ class PasscodeActivity$$ExternalSyntheticLambda0 implements ValueAnimator.AnimatorUpdateListener {
    public final /* synthetic */ PasscodeActivity f$0;

    public /* synthetic */ PasscodeActivity$$ExternalSyntheticLambda0(PasscodeActivity passcodeActivity) {
        this.f$0 = passcodeActivity;
    }

    @Override // android.animation.ValueAnimator.AnimatorUpdateListener
    public final void onAnimationUpdate(ValueAnimator valueAnimator) {
        this.f$0.lambda$setCustomKeyboardVisible$13(valueAnimator);
    }
}