package org.telegram.ui;

import android.content.Context;
import android.view.View;
import org.telegram.ui.Components.RecyclerListView;
/* loaded from: classes3.dex */
public final /* synthetic */ class GroupCreateActivity$$ExternalSyntheticLambda7 implements RecyclerListView.OnItemClickListener {
    public final /* synthetic */ GroupCreateActivity f$0;
    public final /* synthetic */ Context f$1;

    public /* synthetic */ GroupCreateActivity$$ExternalSyntheticLambda7(GroupCreateActivity groupCreateActivity, Context context) {
        this.f$0 = groupCreateActivity;
        this.f$1 = context;
    }

    @Override // org.telegram.ui.Components.RecyclerListView.OnItemClickListener
    public final void onItemClick(View view, int i) {
        this.f$0.lambda$createView$3(this.f$1, view, i);
    }
}