package org.telegram.ui.ActionBar;

import android.animation.ValueAnimator;
import org.telegram.ui.ActionBar.BottomSheet;
/* loaded from: classes3.dex */
public final /* synthetic */ class BottomSheet$ContainerView$$ExternalSyntheticLambda0 implements ValueAnimator.AnimatorUpdateListener {
    public final /* synthetic */ BottomSheet.ContainerView f$0;

    public /* synthetic */ BottomSheet$ContainerView$$ExternalSyntheticLambda0(BottomSheet.ContainerView containerView) {
        this.f$0 = containerView;
    }

    @Override // android.animation.ValueAnimator.AnimatorUpdateListener
    public final void onAnimationUpdate(ValueAnimator valueAnimator) {
        this.f$0.lambda$onLayout$1(valueAnimator);
    }
}