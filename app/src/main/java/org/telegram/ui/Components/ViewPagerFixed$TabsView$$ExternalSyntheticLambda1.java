package org.telegram.ui.Components;

import android.view.View;
import org.telegram.ui.Components.RecyclerListView;
import org.telegram.ui.Components.ViewPagerFixed;
/* loaded from: classes3.dex */
public final /* synthetic */ class ViewPagerFixed$TabsView$$ExternalSyntheticLambda1 implements RecyclerListView.OnItemClickListenerExtended {
    public final /* synthetic */ ViewPagerFixed.TabsView f$0;

    public /* synthetic */ ViewPagerFixed$TabsView$$ExternalSyntheticLambda1(ViewPagerFixed.TabsView tabsView) {
        this.f$0 = tabsView;
    }

    @Override // org.telegram.ui.Components.RecyclerListView.OnItemClickListenerExtended
    public /* synthetic */ boolean hasDoubleTap(View view, int i) {
        return RecyclerListView.OnItemClickListenerExtended.CC.$default$hasDoubleTap(this, view, i);
    }

    @Override // org.telegram.ui.Components.RecyclerListView.OnItemClickListenerExtended
    public /* synthetic */ void onDoubleTap(View view, int i, float f, float f2) {
        RecyclerListView.OnItemClickListenerExtended.CC.$default$onDoubleTap(this, view, i, f, f2);
    }

    @Override // org.telegram.ui.Components.RecyclerListView.OnItemClickListenerExtended
    public final void onItemClick(View view, int i, float f, float f2) {
        this.f$0.lambda$new$0(view, i, f, f2);
    }
}