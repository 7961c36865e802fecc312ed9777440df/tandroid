package org.telegram.messenger;

import org.telegram.tgnet.TLRPC$TL_channels_createChannel;
import org.telegram.tgnet.TLRPC$TL_error;
import org.telegram.ui.ActionBar.BaseFragment;
/* loaded from: classes.dex */
public final /* synthetic */ class MessagesController$$ExternalSyntheticLambda165 implements Runnable {
    public final /* synthetic */ MessagesController f$0;
    public final /* synthetic */ TLRPC$TL_error f$1;
    public final /* synthetic */ BaseFragment f$2;
    public final /* synthetic */ TLRPC$TL_channels_createChannel f$3;

    public /* synthetic */ MessagesController$$ExternalSyntheticLambda165(MessagesController messagesController, TLRPC$TL_error tLRPC$TL_error, BaseFragment baseFragment, TLRPC$TL_channels_createChannel tLRPC$TL_channels_createChannel) {
        this.f$0 = messagesController;
        this.f$1 = tLRPC$TL_error;
        this.f$2 = baseFragment;
        this.f$3 = tLRPC$TL_channels_createChannel;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$createChat$210(this.f$1, this.f$2, this.f$3);
    }
}