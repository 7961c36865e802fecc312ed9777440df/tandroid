package org.telegram.ui;

import org.telegram.ui.QrActivity;
/* loaded from: classes3.dex */
public final /* synthetic */ class QrActivity$ThemeListViewController$$ExternalSyntheticLambda3 implements Runnable {
    public final /* synthetic */ QrActivity.ThemeListViewController f$0;
    public final /* synthetic */ boolean f$1;

    public /* synthetic */ QrActivity$ThemeListViewController$$ExternalSyntheticLambda3(QrActivity.ThemeListViewController themeListViewController, boolean z) {
        this.f$0 = themeListViewController;
        this.f$1 = z;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$setupLightDarkTheme$3(this.f$1);
    }
}