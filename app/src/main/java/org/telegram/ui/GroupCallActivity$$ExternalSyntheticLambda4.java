package org.telegram.ui;

import android.animation.ValueAnimator;
import android.view.View;
/* loaded from: classes3.dex */
public final /* synthetic */ class GroupCallActivity$$ExternalSyntheticLambda4 implements ValueAnimator.AnimatorUpdateListener {
    public final /* synthetic */ GroupCallActivity f$0;
    public final /* synthetic */ float f$1;
    public final /* synthetic */ View f$2;
    public final /* synthetic */ View f$3;

    public /* synthetic */ GroupCallActivity$$ExternalSyntheticLambda4(GroupCallActivity groupCallActivity, float f, View view, View view2) {
        this.f$0 = groupCallActivity;
        this.f$1 = f;
        this.f$2 = view;
        this.f$3 = view2;
    }

    @Override // android.animation.ValueAnimator.AnimatorUpdateListener
    public final void onAnimationUpdate(ValueAnimator valueAnimator) {
        this.f$0.lambda$updateMuteButton$50(this.f$1, this.f$2, this.f$3, valueAnimator);
    }
}