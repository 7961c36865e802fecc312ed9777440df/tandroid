package org.telegram.ui;

import android.view.View;
import org.telegram.ui.Components.RecyclerListView;
/* loaded from: classes3.dex */
public final /* synthetic */ class DataAutoDownloadActivity$$ExternalSyntheticLambda5 implements RecyclerListView.OnItemClickListenerExtended {
    public final /* synthetic */ DataAutoDownloadActivity f$0;

    public /* synthetic */ DataAutoDownloadActivity$$ExternalSyntheticLambda5(DataAutoDownloadActivity dataAutoDownloadActivity) {
        this.f$0 = dataAutoDownloadActivity;
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
        this.f$0.lambda$createView$4(view, i, f, f2);
    }
}