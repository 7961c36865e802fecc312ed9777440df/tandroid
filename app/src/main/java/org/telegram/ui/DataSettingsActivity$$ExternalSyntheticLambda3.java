package org.telegram.ui;

import android.view.View;
import org.telegram.ui.ActionBar.AlertDialog;
/* loaded from: classes3.dex */
public final /* synthetic */ class DataSettingsActivity$$ExternalSyntheticLambda3 implements View.OnClickListener {
    public final /* synthetic */ DataSettingsActivity f$0;
    public final /* synthetic */ String f$1;
    public final /* synthetic */ AlertDialog.Builder f$2;

    public /* synthetic */ DataSettingsActivity$$ExternalSyntheticLambda3(DataSettingsActivity dataSettingsActivity, String str, AlertDialog.Builder builder) {
        this.f$0 = dataSettingsActivity;
        this.f$1 = str;
        this.f$2 = builder;
    }

    @Override // android.view.View.OnClickListener
    public final void onClick(View view) {
        this.f$0.lambda$createView$2(this.f$1, this.f$2, view);
    }
}