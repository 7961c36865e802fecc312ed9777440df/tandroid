package org.telegram.ui.Components;

import android.view.View;
import org.telegram.ui.ActionBar.AlertDialog;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.ActionBar.Theme;
/* loaded from: classes3.dex */
public final /* synthetic */ class AlertsCreator$$ExternalSyntheticLambda53 implements View.OnClickListener {
    public final /* synthetic */ BaseFragment f$0;
    public final /* synthetic */ EditTextBoldCursor f$1;
    public final /* synthetic */ Theme.ThemeAccent f$2;
    public final /* synthetic */ Theme.ThemeInfo f$3;
    public final /* synthetic */ AlertDialog f$4;

    public /* synthetic */ AlertsCreator$$ExternalSyntheticLambda53(BaseFragment baseFragment, EditTextBoldCursor editTextBoldCursor, Theme.ThemeAccent themeAccent, Theme.ThemeInfo themeInfo, AlertDialog alertDialog) {
        this.f$0 = baseFragment;
        this.f$1 = editTextBoldCursor;
        this.f$2 = themeAccent;
        this.f$3 = themeInfo;
        this.f$4 = alertDialog;
    }

    @Override // android.view.View.OnClickListener
    public final void onClick(View view) {
        AlertsCreator.lambda$createThemeCreateDialog$126(this.f$0, this.f$1, this.f$2, this.f$3, this.f$4, view);
    }
}