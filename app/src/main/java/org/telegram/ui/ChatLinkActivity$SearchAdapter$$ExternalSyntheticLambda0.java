package org.telegram.ui;

import org.telegram.ui.ChatLinkActivity;
/* loaded from: classes3.dex */
public final /* synthetic */ class ChatLinkActivity$SearchAdapter$$ExternalSyntheticLambda0 implements Runnable {
    public final /* synthetic */ ChatLinkActivity.SearchAdapter f$0;
    public final /* synthetic */ String f$1;

    public /* synthetic */ ChatLinkActivity$SearchAdapter$$ExternalSyntheticLambda0(ChatLinkActivity.SearchAdapter searchAdapter, String str) {
        this.f$0 = searchAdapter;
        this.f$1 = str;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$searchDialogs$0(this.f$1);
    }
}