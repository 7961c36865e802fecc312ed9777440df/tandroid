package org.telegram.ui.Components;

import android.animation.ValueAnimator;
import org.telegram.ui.Components.FilterTabsView;
/* loaded from: classes3.dex */
public final /* synthetic */ class FilterTabsView$TabView$$ExternalSyntheticLambda0 implements ValueAnimator.AnimatorUpdateListener {
    public final /* synthetic */ FilterTabsView.TabView f$0;

    public /* synthetic */ FilterTabsView$TabView$$ExternalSyntheticLambda0(FilterTabsView.TabView tabView) {
        this.f$0 = tabView;
    }

    @Override // android.animation.ValueAnimator.AnimatorUpdateListener
    public final void onAnimationUpdate(ValueAnimator valueAnimator) {
        this.f$0.lambda$shakeLockIcon$0(valueAnimator);
    }
}