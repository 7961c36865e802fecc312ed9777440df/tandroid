package org.telegram.ui.Components;

import android.content.Context;
import android.view.View;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.ActionBar.BottomSheet;
/* loaded from: classes3.dex */
public final /* synthetic */ class LinkActionView$$ExternalSyntheticLambda6 implements View.OnClickListener {
    public final /* synthetic */ LinkActionView f$0;
    public final /* synthetic */ Context f$1;
    public final /* synthetic */ BottomSheet f$2;
    public final /* synthetic */ BaseFragment f$3;

    public /* synthetic */ LinkActionView$$ExternalSyntheticLambda6(LinkActionView linkActionView, Context context, BottomSheet bottomSheet, BaseFragment baseFragment) {
        this.f$0 = linkActionView;
        this.f$1 = context;
        this.f$2 = bottomSheet;
        this.f$3 = baseFragment;
    }

    @Override // android.view.View.OnClickListener
    public final void onClick(View view) {
        this.f$0.lambda$new$9(this.f$1, this.f$2, this.f$3, view);
    }
}