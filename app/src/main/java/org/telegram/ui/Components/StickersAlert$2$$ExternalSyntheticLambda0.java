package org.telegram.ui.Components;

import android.animation.ValueAnimator;
import org.telegram.ui.Components.StickersAlert;
/* loaded from: classes3.dex */
public final /* synthetic */ class StickersAlert$2$$ExternalSyntheticLambda0 implements ValueAnimator.AnimatorUpdateListener {
    public final /* synthetic */ StickersAlert.AnonymousClass2 f$0;
    public final /* synthetic */ int f$1;
    public final /* synthetic */ int f$2;

    public /* synthetic */ StickersAlert$2$$ExternalSyntheticLambda0(StickersAlert.AnonymousClass2 anonymousClass2, int i, int i2) {
        this.f$0 = anonymousClass2;
        this.f$1 = i;
        this.f$2 = i2;
    }

    @Override // android.animation.ValueAnimator.AnimatorUpdateListener
    public final void onAnimationUpdate(ValueAnimator valueAnimator) {
        this.f$0.lambda$createAnimator$0(this.f$1, this.f$2, valueAnimator);
    }
}