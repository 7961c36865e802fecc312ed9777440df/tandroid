package org.telegram.messenger;

import androidx.core.util.Consumer;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC$TL_error;
/* loaded from: classes.dex */
public final /* synthetic */ class MessagesController$$ExternalSyntheticLambda218 implements Runnable {
    public final /* synthetic */ TLObject f$0;
    public final /* synthetic */ TLRPC$TL_error f$1;
    public final /* synthetic */ Consumer f$2;

    public /* synthetic */ MessagesController$$ExternalSyntheticLambda218(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error, Consumer consumer) {
        this.f$0 = tLObject;
        this.f$1 = tLRPC$TL_error;
        this.f$2 = consumer;
    }

    @Override // java.lang.Runnable
    public final void run() {
        MessagesController.lambda$getNextReactionMention$3(this.f$0, this.f$1, this.f$2);
    }
}