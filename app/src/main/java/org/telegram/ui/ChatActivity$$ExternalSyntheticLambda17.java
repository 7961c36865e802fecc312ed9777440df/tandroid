package org.telegram.ui;

import android.animation.ValueAnimator;
/* loaded from: classes3.dex */
public final /* synthetic */ class ChatActivity$$ExternalSyntheticLambda17 implements ValueAnimator.AnimatorUpdateListener {
    public final /* synthetic */ ChatActivity f$0;
    public final /* synthetic */ ChatActivity f$1;
    public final /* synthetic */ boolean f$2;

    public /* synthetic */ ChatActivity$$ExternalSyntheticLambda17(ChatActivity chatActivity, ChatActivity chatActivity2, boolean z) {
        this.f$0 = chatActivity;
        this.f$1 = chatActivity2;
        this.f$2 = z;
    }

    @Override // android.animation.ValueAnimator.AnimatorUpdateListener
    public final void onAnimationUpdate(ValueAnimator valueAnimator) {
        this.f$0.lambda$onCustomTransitionAnimation$247(this.f$1, this.f$2, valueAnimator);
    }
}