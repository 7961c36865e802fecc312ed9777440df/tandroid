package org.telegram.ui;

import android.animation.ValueAnimator;
/* loaded from: classes3.dex */
public final /* synthetic */ class ProfileActivity$$ExternalSyntheticLambda3 implements ValueAnimator.AnimatorUpdateListener {
    public final /* synthetic */ ProfileActivity f$0;
    public final /* synthetic */ ValueAnimator f$1;
    public final /* synthetic */ float f$2;
    public final /* synthetic */ boolean f$3;

    public /* synthetic */ ProfileActivity$$ExternalSyntheticLambda3(ProfileActivity profileActivity, ValueAnimator valueAnimator, float f, boolean z) {
        this.f$0 = profileActivity;
        this.f$1 = valueAnimator;
        this.f$2 = f;
        this.f$3 = z;
    }

    @Override // android.animation.ValueAnimator.AnimatorUpdateListener
    public final void onAnimationUpdate(ValueAnimator valueAnimator) {
        this.f$0.lambda$searchExpandTransition$33(this.f$1, this.f$2, this.f$3, valueAnimator);
    }
}