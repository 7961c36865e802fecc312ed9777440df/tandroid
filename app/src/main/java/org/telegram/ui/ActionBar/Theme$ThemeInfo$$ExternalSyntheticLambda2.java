package org.telegram.ui.ActionBar;

import org.telegram.tgnet.TLObject;
import org.telegram.ui.ActionBar.Theme;
/* loaded from: classes3.dex */
public final /* synthetic */ class Theme$ThemeInfo$$ExternalSyntheticLambda2 implements Runnable {
    public final /* synthetic */ Theme.ThemeInfo f$0;
    public final /* synthetic */ TLObject f$1;
    public final /* synthetic */ Theme.ThemeInfo f$2;

    public /* synthetic */ Theme$ThemeInfo$$ExternalSyntheticLambda2(Theme.ThemeInfo themeInfo, TLObject tLObject, Theme.ThemeInfo themeInfo2) {
        this.f$0 = themeInfo;
        this.f$1 = tLObject;
        this.f$2 = themeInfo2;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$didReceivedNotification$1(this.f$1, this.f$2);
    }
}