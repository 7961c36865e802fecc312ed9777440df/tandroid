package org.telegram.ui;

import android.animation.ValueAnimator;
import android.widget.HorizontalScrollView;
import org.telegram.ui.Components.ReactionTabHolderView;
/* loaded from: classes3.dex */
public final /* synthetic */ class ChatActivity$$ExternalSyntheticLambda0 implements ValueAnimator.AnimatorUpdateListener {
    public final /* synthetic */ HorizontalScrollView f$0;
    public final /* synthetic */ float f$1;
    public final /* synthetic */ float f$2;
    public final /* synthetic */ ReactionTabHolderView f$3;
    public final /* synthetic */ ReactionTabHolderView f$4;

    public /* synthetic */ ChatActivity$$ExternalSyntheticLambda0(HorizontalScrollView horizontalScrollView, float f, float f2, ReactionTabHolderView reactionTabHolderView, ReactionTabHolderView reactionTabHolderView2) {
        this.f$0 = horizontalScrollView;
        this.f$1 = f;
        this.f$2 = f2;
        this.f$3 = reactionTabHolderView;
        this.f$4 = reactionTabHolderView2;
    }

    @Override // android.animation.ValueAnimator.AnimatorUpdateListener
    public final void onAnimationUpdate(ValueAnimator valueAnimator) {
        ChatActivity.lambda$createMenu$156(this.f$0, this.f$1, this.f$2, this.f$3, this.f$4, valueAnimator);
    }
}