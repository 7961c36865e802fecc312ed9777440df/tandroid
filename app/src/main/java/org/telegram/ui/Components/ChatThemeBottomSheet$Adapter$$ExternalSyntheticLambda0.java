package org.telegram.ui.Components;

import org.telegram.tgnet.TLObject;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Components.ChatThemeBottomSheet;
/* loaded from: classes3.dex */
public final /* synthetic */ class ChatThemeBottomSheet$Adapter$$ExternalSyntheticLambda0 implements Runnable {
    public final /* synthetic */ ChatThemeBottomSheet.Adapter f$0;
    public final /* synthetic */ TLObject f$1;
    public final /* synthetic */ Theme.ThemeInfo f$2;

    public /* synthetic */ ChatThemeBottomSheet$Adapter$$ExternalSyntheticLambda0(ChatThemeBottomSheet.Adapter adapter, TLObject tLObject, Theme.ThemeInfo themeInfo) {
        this.f$0 = adapter;
        this.f$1 = tLObject;
        this.f$2 = themeInfo;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$parseTheme$0(this.f$1, this.f$2);
    }
}