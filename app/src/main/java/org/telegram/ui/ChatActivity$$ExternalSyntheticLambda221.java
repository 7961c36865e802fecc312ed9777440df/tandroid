package org.telegram.ui;

import org.telegram.messenger.MessageObject;
import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC$Chat;
import org.telegram.tgnet.TLRPC$TL_error;
import org.telegram.tgnet.TLRPC$TL_messages_getDiscussionMessage;
/* loaded from: classes3.dex */
public final /* synthetic */ class ChatActivity$$ExternalSyntheticLambda221 implements RequestDelegate {
    public final /* synthetic */ ChatActivity f$0;
    public final /* synthetic */ int f$1;
    public final /* synthetic */ int f$2;
    public final /* synthetic */ long f$3;
    public final /* synthetic */ int f$4;
    public final /* synthetic */ MessageObject f$5;
    public final /* synthetic */ TLRPC$TL_messages_getDiscussionMessage f$6;
    public final /* synthetic */ TLRPC$Chat f$7;
    public final /* synthetic */ MessageObject f$8;

    public /* synthetic */ ChatActivity$$ExternalSyntheticLambda221(ChatActivity chatActivity, int i, int i2, long j, int i3, MessageObject messageObject, TLRPC$TL_messages_getDiscussionMessage tLRPC$TL_messages_getDiscussionMessage, TLRPC$Chat tLRPC$Chat, MessageObject messageObject2) {
        this.f$0 = chatActivity;
        this.f$1 = i;
        this.f$2 = i2;
        this.f$3 = j;
        this.f$4 = i3;
        this.f$5 = messageObject;
        this.f$6 = tLRPC$TL_messages_getDiscussionMessage;
        this.f$7 = tLRPC$Chat;
        this.f$8 = messageObject2;
    }

    @Override // org.telegram.tgnet.RequestDelegate
    public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        this.f$0.lambda$openDiscussionMessageChat$228(this.f$1, this.f$2, this.f$3, this.f$4, this.f$5, this.f$6, this.f$7, this.f$8, tLObject, tLRPC$TL_error);
    }
}