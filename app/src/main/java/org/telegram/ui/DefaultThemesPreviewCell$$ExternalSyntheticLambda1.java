package org.telegram.ui;

import android.view.View;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.Components.RecyclerListView;
/* loaded from: classes3.dex */
public final /* synthetic */ class DefaultThemesPreviewCell$$ExternalSyntheticLambda1 implements RecyclerListView.OnItemClickListener {
    public final /* synthetic */ DefaultThemesPreviewCell f$0;
    public final /* synthetic */ BaseFragment f$1;

    public /* synthetic */ DefaultThemesPreviewCell$$ExternalSyntheticLambda1(DefaultThemesPreviewCell defaultThemesPreviewCell, BaseFragment baseFragment) {
        this.f$0 = defaultThemesPreviewCell;
        this.f$1 = baseFragment;
    }

    @Override // org.telegram.ui.Components.RecyclerListView.OnItemClickListener
    public final void onItemClick(View view, int i) {
        this.f$0.lambda$new$0(this.f$1, view, i);
    }
}