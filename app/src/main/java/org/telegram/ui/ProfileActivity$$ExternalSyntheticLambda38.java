package org.telegram.ui;

import android.content.Context;
import android.view.View;
import org.telegram.ui.Components.RecyclerListView;
/* loaded from: classes3.dex */
public final /* synthetic */ class ProfileActivity$$ExternalSyntheticLambda38 implements RecyclerListView.OnItemClickListenerExtended {
    public final /* synthetic */ ProfileActivity f$0;
    public final /* synthetic */ long f$1;
    public final /* synthetic */ Context f$2;

    public /* synthetic */ ProfileActivity$$ExternalSyntheticLambda38(ProfileActivity profileActivity, long j, Context context) {
        this.f$0 = profileActivity;
        this.f$1 = j;
        this.f$2 = context;
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
        this.f$0.lambda$createView$4(this.f$1, this.f$2, view, i, f, f2);
    }
}