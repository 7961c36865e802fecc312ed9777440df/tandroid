package org.telegram.ui.Components;

import android.animation.ValueAnimator;
import org.telegram.ui.Components.TranslateAlert;
/* loaded from: classes3.dex */
public final /* synthetic */ class TranslateAlert$LoadingTextView2$$ExternalSyntheticLambda1 implements ValueAnimator.AnimatorUpdateListener {
    public final /* synthetic */ TranslateAlert.LoadingTextView2 f$0;
    public final /* synthetic */ boolean f$1;

    public /* synthetic */ TranslateAlert$LoadingTextView2$$ExternalSyntheticLambda1(TranslateAlert.LoadingTextView2 loadingTextView2, boolean z) {
        this.f$0 = loadingTextView2;
        this.f$1 = z;
    }

    @Override // android.animation.ValueAnimator.AnimatorUpdateListener
    public final void onAnimationUpdate(ValueAnimator valueAnimator) {
        this.f$0.lambda$new$0(this.f$1, valueAnimator);
    }
}