package org.telegram.messenger;

import androidx.collection.LongSparseArray;
/* loaded from: classes.dex */
public final /* synthetic */ class MessagesStorage$$ExternalSyntheticLambda118 implements Runnable {
    public final /* synthetic */ MessagesStorage f$0;
    public final /* synthetic */ LongSparseArray f$1;

    public /* synthetic */ MessagesStorage$$ExternalSyntheticLambda118(MessagesStorage messagesStorage, LongSparseArray longSparseArray) {
        this.f$0 = messagesStorage;
        this.f$1 = longSparseArray;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$putWebPages$151(this.f$1);
    }
}