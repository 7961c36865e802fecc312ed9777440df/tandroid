package org.telegram.ui.Components;

import android.view.View;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.ActionBar.BottomSheet;
/* loaded from: classes3.dex */
public final /* synthetic */ class LinkActionView$$ExternalSyntheticLambda9 implements View.OnClickListener {
    public final /* synthetic */ LinkActionView f$0;
    public final /* synthetic */ BottomSheet f$1;
    public final /* synthetic */ BaseFragment f$2;

    public /* synthetic */ LinkActionView$$ExternalSyntheticLambda9(LinkActionView linkActionView, BottomSheet bottomSheet, BaseFragment baseFragment) {
        this.f$0 = linkActionView;
        this.f$1 = bottomSheet;
        this.f$2 = baseFragment;
    }

    @Override // android.view.View.OnClickListener
    public final void onClick(View view) {
        this.f$0.lambda$new$0(this.f$1, this.f$2, view);
    }
}