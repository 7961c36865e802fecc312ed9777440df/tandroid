package org.telegram.ui.Components;

import android.view.View;
/* loaded from: classes3.dex */
public final /* synthetic */ class ScrollSlidingTextTabStrip$$ExternalSyntheticLambda1 implements View.OnClickListener {
    public final /* synthetic */ ScrollSlidingTextTabStrip f$0;
    public final /* synthetic */ int f$1;

    public /* synthetic */ ScrollSlidingTextTabStrip$$ExternalSyntheticLambda1(ScrollSlidingTextTabStrip scrollSlidingTextTabStrip, int i) {
        this.f$0 = scrollSlidingTextTabStrip;
        this.f$1 = i;
    }

    @Override // android.view.View.OnClickListener
    public final void onClick(View view) {
        this.f$0.lambda$addTextTab$0(this.f$1, view);
    }
}