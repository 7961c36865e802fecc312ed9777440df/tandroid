package org.telegram.messenger;

import org.telegram.messenger.MessagesController;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC$TL_error;
import org.telegram.ui.ActionBar.BaseFragment;
/* loaded from: classes.dex */
public final /* synthetic */ class MessagesController$$ExternalSyntheticLambda128 implements Runnable {
    public final /* synthetic */ MessagesController f$0;
    public final /* synthetic */ MessagesController.ErrorDelegate f$1;
    public final /* synthetic */ TLRPC$TL_error f$2;
    public final /* synthetic */ BaseFragment f$3;
    public final /* synthetic */ TLObject f$4;
    public final /* synthetic */ boolean f$5;
    public final /* synthetic */ boolean f$6;

    public /* synthetic */ MessagesController$$ExternalSyntheticLambda128(MessagesController messagesController, MessagesController.ErrorDelegate errorDelegate, TLRPC$TL_error tLRPC$TL_error, BaseFragment baseFragment, TLObject tLObject, boolean z, boolean z2) {
        this.f$0 = messagesController;
        this.f$1 = errorDelegate;
        this.f$2 = tLRPC$TL_error;
        this.f$3 = baseFragment;
        this.f$4 = tLObject;
        this.f$5 = z;
        this.f$6 = z2;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$addUserToChat$242(this.f$1, this.f$2, this.f$3, this.f$4, this.f$5, this.f$6);
    }
}