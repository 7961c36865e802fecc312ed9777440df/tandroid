package org.telegram.ui;

import android.view.View;
import org.telegram.ui.FiltersSetupActivity;
/* loaded from: classes3.dex */
public final /* synthetic */ class FiltersSetupActivity$ListAdapter$$ExternalSyntheticLambda3 implements View.OnClickListener {
    public final /* synthetic */ FiltersSetupActivity.ListAdapter f$0;
    public final /* synthetic */ FiltersSetupActivity.SuggestedFilterCell f$1;

    public /* synthetic */ FiltersSetupActivity$ListAdapter$$ExternalSyntheticLambda3(FiltersSetupActivity.ListAdapter listAdapter, FiltersSetupActivity.SuggestedFilterCell suggestedFilterCell) {
        this.f$0 = listAdapter;
        this.f$1 = suggestedFilterCell;
    }

    @Override // android.view.View.OnClickListener
    public final void onClick(View view) {
        this.f$0.lambda$onCreateViewHolder$7(this.f$1, view);
    }
}