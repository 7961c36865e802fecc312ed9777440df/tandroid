package org.telegram.ui.ActionBar;

import android.animation.ValueAnimator;
/* loaded from: classes3.dex */
public final /* synthetic */ class BottomSheet$$ExternalSyntheticLambda0 implements ValueAnimator.AnimatorUpdateListener {
    public final /* synthetic */ BottomSheet f$0;

    public /* synthetic */ BottomSheet$$ExternalSyntheticLambda0(BottomSheet bottomSheet) {
        this.f$0 = bottomSheet;
    }

    @Override // android.animation.ValueAnimator.AnimatorUpdateListener
    public final void onAnimationUpdate(ValueAnimator valueAnimator) {
        this.f$0.lambda$startOpenAnimation$4(valueAnimator);
    }
}