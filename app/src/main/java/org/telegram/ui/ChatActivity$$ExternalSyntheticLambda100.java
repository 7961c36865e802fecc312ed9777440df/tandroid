package org.telegram.ui;

import android.view.View;
import org.telegram.messenger.MessageObject;
import org.telegram.tgnet.TLRPC$KeyboardButton;
import org.telegram.ui.ChatActivity;
/* loaded from: classes3.dex */
public final /* synthetic */ class ChatActivity$$ExternalSyntheticLambda100 implements View.OnLongClickListener {
    public final /* synthetic */ ChatActivity f$0;
    public final /* synthetic */ TLRPC$KeyboardButton f$1;
    public final /* synthetic */ MessageObject f$2;
    public final /* synthetic */ ChatActivity.PinnedMessageButton f$3;

    public /* synthetic */ ChatActivity$$ExternalSyntheticLambda100(ChatActivity chatActivity, TLRPC$KeyboardButton tLRPC$KeyboardButton, MessageObject messageObject, ChatActivity.PinnedMessageButton pinnedMessageButton) {
        this.f$0 = chatActivity;
        this.f$1 = tLRPC$KeyboardButton;
        this.f$2 = messageObject;
        this.f$3 = pinnedMessageButton;
    }

    @Override // android.view.View.OnLongClickListener
    public final boolean onLongClick(View view) {
        boolean lambda$updatePinnedMessageView$144;
        lambda$updatePinnedMessageView$144 = this.f$0.lambda$updatePinnedMessageView$144(this.f$1, this.f$2, this.f$3, view);
        return lambda$updatePinnedMessageView$144;
    }
}