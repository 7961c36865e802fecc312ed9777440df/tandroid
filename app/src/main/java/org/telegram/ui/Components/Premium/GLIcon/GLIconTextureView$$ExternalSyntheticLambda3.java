package org.telegram.ui.Components.Premium.GLIcon;

import android.animation.ValueAnimator;
/* loaded from: classes3.dex */
public final /* synthetic */ class GLIconTextureView$$ExternalSyntheticLambda3 implements ValueAnimator.AnimatorUpdateListener {
    public final /* synthetic */ GLIconTextureView f$0;
    public final /* synthetic */ float f$1;
    public final /* synthetic */ float f$2;
    public final /* synthetic */ float f$3;

    public /* synthetic */ GLIconTextureView$$ExternalSyntheticLambda3(GLIconTextureView gLIconTextureView, float f, float f2, float f3) {
        this.f$0 = gLIconTextureView;
        this.f$1 = f;
        this.f$2 = f2;
        this.f$3 = f3;
    }

    @Override // android.animation.ValueAnimator.AnimatorUpdateListener
    public final void onAnimationUpdate(ValueAnimator valueAnimator) {
        this.f$0.lambda$startBackAnimation$0(this.f$1, this.f$2, this.f$3, valueAnimator);
    }
}