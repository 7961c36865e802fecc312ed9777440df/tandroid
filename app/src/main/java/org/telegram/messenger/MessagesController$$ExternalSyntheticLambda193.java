package org.telegram.messenger;

import androidx.collection.LongSparseArray;
import org.telegram.messenger.support.LongSparseIntArray;
import org.telegram.tgnet.TLRPC$messages_Dialogs;
/* loaded from: classes.dex */
public final /* synthetic */ class MessagesController$$ExternalSyntheticLambda193 implements Runnable {
    public final /* synthetic */ MessagesController f$0;
    public final /* synthetic */ TLRPC$messages_Dialogs f$1;
    public final /* synthetic */ LongSparseArray f$2;
    public final /* synthetic */ LongSparseArray f$3;
    public final /* synthetic */ boolean f$4;
    public final /* synthetic */ LongSparseIntArray f$5;

    public /* synthetic */ MessagesController$$ExternalSyntheticLambda193(MessagesController messagesController, TLRPC$messages_Dialogs tLRPC$messages_Dialogs, LongSparseArray longSparseArray, LongSparseArray longSparseArray2, boolean z, LongSparseIntArray longSparseIntArray) {
        this.f$0 = messagesController;
        this.f$1 = tLRPC$messages_Dialogs;
        this.f$2 = longSparseArray;
        this.f$3 = longSparseArray2;
        this.f$4 = z;
        this.f$5 = longSparseIntArray;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$processDialogsUpdate$187(this.f$1, this.f$2, this.f$3, this.f$4, this.f$5);
    }
}