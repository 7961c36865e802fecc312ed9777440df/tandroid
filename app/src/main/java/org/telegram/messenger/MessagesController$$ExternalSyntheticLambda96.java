package org.telegram.messenger;

import androidx.collection.LongSparseArray;
/* loaded from: classes.dex */
public final /* synthetic */ class MessagesController$$ExternalSyntheticLambda96 implements Runnable {
    public final /* synthetic */ MessagesController f$0;
    public final /* synthetic */ LongSparseArray f$1;

    public /* synthetic */ MessagesController$$ExternalSyntheticLambda96(MessagesController messagesController, LongSparseArray longSparseArray) {
        this.f$0 = messagesController;
        this.f$1 = longSparseArray;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$processUpdateArray$318(this.f$1);
    }
}