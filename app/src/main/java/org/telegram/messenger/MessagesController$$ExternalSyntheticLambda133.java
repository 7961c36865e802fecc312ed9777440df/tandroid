package org.telegram.messenger;

import org.telegram.messenger.support.LongSparseIntArray;
/* loaded from: classes.dex */
public final /* synthetic */ class MessagesController$$ExternalSyntheticLambda133 implements Runnable {
    public final /* synthetic */ MessagesController f$0;
    public final /* synthetic */ LongSparseIntArray f$1;
    public final /* synthetic */ LongSparseIntArray f$2;

    public /* synthetic */ MessagesController$$ExternalSyntheticLambda133(MessagesController messagesController, LongSparseIntArray longSparseIntArray, LongSparseIntArray longSparseIntArray2) {
        this.f$0 = messagesController;
        this.f$1 = longSparseIntArray;
        this.f$2 = longSparseIntArray2;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$processDialogsUpdateRead$182(this.f$1, this.f$2);
    }
}