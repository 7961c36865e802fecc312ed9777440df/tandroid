package org.telegram.ui;

import android.animation.ValueAnimator;
/* loaded from: classes3.dex */
public final /* synthetic */ class DialogsActivity$$ExternalSyntheticLambda6 implements ValueAnimator.AnimatorUpdateListener {
    public final /* synthetic */ DialogsActivity f$0;
    public final /* synthetic */ boolean f$1;
    public final /* synthetic */ float f$2;

    public /* synthetic */ DialogsActivity$$ExternalSyntheticLambda6(DialogsActivity dialogsActivity, boolean z, float f) {
        this.f$0 = dialogsActivity;
        this.f$1 = z;
        this.f$2 = f;
    }

    @Override // android.animation.ValueAnimator.AnimatorUpdateListener
    public final void onAnimationUpdate(ValueAnimator valueAnimator) {
        this.f$0.lambda$updateFilterTabsVisibility$21(this.f$1, this.f$2, valueAnimator);
    }
}