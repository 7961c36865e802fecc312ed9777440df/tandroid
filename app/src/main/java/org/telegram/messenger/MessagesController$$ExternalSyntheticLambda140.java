package org.telegram.messenger;

import org.telegram.tgnet.TLObject;
/* loaded from: classes.dex */
public final /* synthetic */ class MessagesController$$ExternalSyntheticLambda140 implements Runnable {
    public final /* synthetic */ MessagesController f$0;
    public final /* synthetic */ TLObject f$1;

    public /* synthetic */ MessagesController$$ExternalSyntheticLambda140(MessagesController messagesController, TLObject tLObject) {
        this.f$0 = messagesController;
        this.f$1 = tLObject;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$loadSuggestedFilters$16(this.f$1);
    }
}