package org.telegram.messenger;

import android.util.SparseIntArray;
import org.telegram.messenger.support.LongSparseIntArray;
/* loaded from: classes.dex */
public final /* synthetic */ class MessagesStorage$$ExternalSyntheticLambda154 implements Runnable {
    public final /* synthetic */ MessagesStorage f$0;
    public final /* synthetic */ LongSparseIntArray f$1;
    public final /* synthetic */ LongSparseIntArray f$2;
    public final /* synthetic */ SparseIntArray f$3;

    public /* synthetic */ MessagesStorage$$ExternalSyntheticLambda154(MessagesStorage messagesStorage, LongSparseIntArray longSparseIntArray, LongSparseIntArray longSparseIntArray2, SparseIntArray sparseIntArray) {
        this.f$0 = messagesStorage;
        this.f$1 = longSparseIntArray;
        this.f$2 = longSparseIntArray2;
        this.f$3 = sparseIntArray;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$markMessagesAsRead$166(this.f$1, this.f$2, this.f$3);
    }
}