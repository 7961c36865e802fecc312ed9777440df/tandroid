package org.telegram.ui;

import android.view.View;
import org.telegram.ui.Cells.AboutLinkCell;
import org.telegram.ui.ProfileActivity;
/* loaded from: classes3.dex */
public final /* synthetic */ class ProfileActivity$ListAdapter$$ExternalSyntheticLambda1 implements View.OnClickListener {
    public final /* synthetic */ ProfileActivity.ListAdapter f$0;
    public final /* synthetic */ int f$1;
    public final /* synthetic */ AboutLinkCell f$2;

    public /* synthetic */ ProfileActivity$ListAdapter$$ExternalSyntheticLambda1(ProfileActivity.ListAdapter listAdapter, int i, AboutLinkCell aboutLinkCell) {
        this.f$0 = listAdapter;
        this.f$1 = i;
        this.f$2 = aboutLinkCell;
    }

    @Override // android.view.View.OnClickListener
    public final void onClick(View view) {
        this.f$0.lambda$onBindViewHolder$2(this.f$1, this.f$2, view);
    }
}