package org.telegram.ui;

import android.animation.ValueAnimator;
import org.telegram.ui.PhotoViewer;
/* loaded from: classes3.dex */
public final /* synthetic */ class PhotoViewer$FirstFrameView$$ExternalSyntheticLambda0 implements ValueAnimator.AnimatorUpdateListener {
    public final /* synthetic */ PhotoViewer.FirstFrameView f$0;

    public /* synthetic */ PhotoViewer$FirstFrameView$$ExternalSyntheticLambda0(PhotoViewer.FirstFrameView firstFrameView) {
        this.f$0 = firstFrameView;
    }

    @Override // android.animation.ValueAnimator.AnimatorUpdateListener
    public final void onAnimationUpdate(ValueAnimator valueAnimator) {
        this.f$0.lambda$updateAlpha$3(valueAnimator);
    }
}