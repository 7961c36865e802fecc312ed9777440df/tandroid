package org.telegram.ui;

import android.animation.AnimatorSet;
import org.telegram.ui.PhotoViewer;
/* loaded from: classes3.dex */
public final /* synthetic */ class PhotoViewer$69$$ExternalSyntheticLambda0 implements Runnable {
    public final /* synthetic */ PhotoViewer.AnonymousClass69 f$0;
    public final /* synthetic */ int f$1;
    public final /* synthetic */ AnimatorSet f$2;

    public /* synthetic */ PhotoViewer$69$$ExternalSyntheticLambda0(PhotoViewer.AnonymousClass69 anonymousClass69, int i, AnimatorSet animatorSet) {
        this.f$0 = anonymousClass69;
        this.f$1 = i;
        this.f$2 = animatorSet;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$onPreDraw$1(this.f$1, this.f$2);
    }
}