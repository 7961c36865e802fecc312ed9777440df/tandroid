package org.telegram.ui;

import org.telegram.messenger.MessagesController;
import org.telegram.ui.ActionBar.AlertDialog;
import org.telegram.ui.FiltersSetupActivity;
/* loaded from: classes3.dex */
public final /* synthetic */ class FiltersSetupActivity$ListAdapter$$ExternalSyntheticLambda6 implements Runnable {
    public final /* synthetic */ FiltersSetupActivity.ListAdapter f$0;
    public final /* synthetic */ AlertDialog f$1;
    public final /* synthetic */ MessagesController.DialogFilter f$2;

    public /* synthetic */ FiltersSetupActivity$ListAdapter$$ExternalSyntheticLambda6(FiltersSetupActivity.ListAdapter listAdapter, AlertDialog alertDialog, MessagesController.DialogFilter dialogFilter) {
        this.f$0 = listAdapter;
        this.f$1 = alertDialog;
        this.f$2 = dialogFilter;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$onCreateViewHolder$1(this.f$1, this.f$2);
    }
}