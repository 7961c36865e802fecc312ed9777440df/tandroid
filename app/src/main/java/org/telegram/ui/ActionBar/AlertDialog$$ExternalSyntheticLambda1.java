package org.telegram.ui.ActionBar;

import android.content.DialogInterface;
/* loaded from: classes3.dex */
public final /* synthetic */ class AlertDialog$$ExternalSyntheticLambda1 implements DialogInterface.OnDismissListener {
    public final /* synthetic */ AlertDialog f$0;

    public /* synthetic */ AlertDialog$$ExternalSyntheticLambda1(AlertDialog alertDialog) {
        this.f$0 = alertDialog;
    }

    @Override // android.content.DialogInterface.OnDismissListener
    public final void onDismiss(DialogInterface dialogInterface) {
        this.f$0.lambda$showCancelAlert$6(dialogInterface);
    }
}