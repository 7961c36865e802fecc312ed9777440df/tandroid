package org.telegram.messenger;

import org.telegram.ui.ActionBar.Theme;
/* loaded from: classes.dex */
public final /* synthetic */ class MessagesController$$ExternalSyntheticLambda205 implements Runnable {
    public final /* synthetic */ MessagesController f$0;
    public final /* synthetic */ Theme.ThemeInfo f$1;
    public final /* synthetic */ Theme.ThemeAccent f$2;

    public /* synthetic */ MessagesController$$ExternalSyntheticLambda205(MessagesController messagesController, Theme.ThemeInfo themeInfo, Theme.ThemeAccent themeAccent) {
        this.f$0 = messagesController;
        this.f$1 = themeInfo;
        this.f$2 = themeAccent;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$didReceivedNotification$32(this.f$1, this.f$2);
    }
}