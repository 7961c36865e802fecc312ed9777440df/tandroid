package org.telegram.ui;

import android.view.View;
import org.telegram.messenger.LocaleController;
import org.telegram.ui.Cells.LanguageCell;
/* loaded from: classes3.dex */
public final /* synthetic */ class LaunchActivity$$ExternalSyntheticLambda17 implements View.OnClickListener {
    public final /* synthetic */ LocaleController.LocaleInfo[] f$0;
    public final /* synthetic */ LanguageCell[] f$1;

    public /* synthetic */ LaunchActivity$$ExternalSyntheticLambda17(LocaleController.LocaleInfo[] localeInfoArr, LanguageCell[] languageCellArr) {
        this.f$0 = localeInfoArr;
        this.f$1 = languageCellArr;
    }

    @Override // android.view.View.OnClickListener
    public final void onClick(View view) {
        LaunchActivity.lambda$showLanguageAlertInternal$96(this.f$0, this.f$1, view);
    }
}