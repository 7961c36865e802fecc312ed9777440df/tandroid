package org.telegram.ui.Components;

import android.content.DialogInterface;
import android.view.View;
import org.telegram.ui.ActionBar.AlertDialog;
/* loaded from: classes3.dex */
public final /* synthetic */ class AlertsCreator$$ExternalSyntheticLambda52 implements View.OnClickListener {
    public final /* synthetic */ AlertDialog.Builder f$0;
    public final /* synthetic */ DialogInterface.OnClickListener f$1;

    public /* synthetic */ AlertsCreator$$ExternalSyntheticLambda52(AlertDialog.Builder builder, DialogInterface.OnClickListener onClickListener) {
        this.f$0 = builder;
        this.f$1 = onClickListener;
    }

    @Override // android.view.View.OnClickListener
    public final void onClick(View view) {
        AlertsCreator.lambda$createSingleChoiceDialog$106(this.f$0, this.f$1, view);
    }
}