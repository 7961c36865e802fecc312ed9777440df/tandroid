package org.telegram.ui;

import org.telegram.messenger.MessagesStorage;
import org.telegram.ui.ActionBar.BaseFragment;
/* loaded from: classes3.dex */
public final /* synthetic */ class ChatLinkActivity$$ExternalSyntheticLambda13 implements MessagesStorage.LongCallback {
    public final /* synthetic */ ChatLinkActivity f$0;
    public final /* synthetic */ BaseFragment f$1;

    public /* synthetic */ ChatLinkActivity$$ExternalSyntheticLambda13(ChatLinkActivity chatLinkActivity, BaseFragment baseFragment) {
        this.f$0 = chatLinkActivity;
        this.f$1 = baseFragment;
    }

    @Override // org.telegram.messenger.MessagesStorage.LongCallback
    public final void run(long j) {
        this.f$0.lambda$linkChat$10(this.f$1, j);
    }
}