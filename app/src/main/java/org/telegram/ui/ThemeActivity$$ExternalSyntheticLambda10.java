package org.telegram.ui;

import android.view.View;
import org.telegram.ui.Components.RecyclerListView;
/* loaded from: classes3.dex */
public final /* synthetic */ class ThemeActivity$$ExternalSyntheticLambda10 implements RecyclerListView.OnItemClickListenerExtended {
    public final /* synthetic */ ThemeActivity f$0;

    public /* synthetic */ ThemeActivity$$ExternalSyntheticLambda10(ThemeActivity themeActivity) {
        this.f$0 = themeActivity;
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
        this.f$0.lambda$createView$5(view, i, f, f2);
    }
}