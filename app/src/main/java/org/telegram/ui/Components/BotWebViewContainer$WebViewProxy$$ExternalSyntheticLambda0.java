package org.telegram.ui.Components;

import org.telegram.ui.Components.BotWebViewContainer;
/* loaded from: classes3.dex */
public final /* synthetic */ class BotWebViewContainer$WebViewProxy$$ExternalSyntheticLambda0 implements Runnable {
    public final /* synthetic */ BotWebViewContainer.WebViewProxy f$0;
    public final /* synthetic */ String f$1;
    public final /* synthetic */ String f$2;

    public /* synthetic */ BotWebViewContainer$WebViewProxy$$ExternalSyntheticLambda0(BotWebViewContainer.WebViewProxy webViewProxy, String str, String str2) {
        this.f$0 = webViewProxy;
        this.f$1 = str;
        this.f$2 = str2;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$postEvent$0(this.f$1, this.f$2);
    }
}