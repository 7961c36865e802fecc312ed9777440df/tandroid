package org.telegram.ui;

import android.view.View;
import org.telegram.messenger.MessageObject;
import org.telegram.tgnet.TLRPC$KeyboardButton;
/* loaded from: classes3.dex */
public final /* synthetic */ class ChatActivity$$ExternalSyntheticLambda89 implements View.OnClickListener {
    public final /* synthetic */ ChatActivity f$0;
    public final /* synthetic */ TLRPC$KeyboardButton f$1;
    public final /* synthetic */ MessageObject f$2;

    public /* synthetic */ ChatActivity$$ExternalSyntheticLambda89(ChatActivity chatActivity, TLRPC$KeyboardButton tLRPC$KeyboardButton, MessageObject messageObject) {
        this.f$0 = chatActivity;
        this.f$1 = tLRPC$KeyboardButton;
        this.f$2 = messageObject;
    }

    @Override // android.view.View.OnClickListener
    public final void onClick(View view) {
        this.f$0.lambda$updatePinnedMessageView$143(this.f$1, this.f$2, view);
    }
}