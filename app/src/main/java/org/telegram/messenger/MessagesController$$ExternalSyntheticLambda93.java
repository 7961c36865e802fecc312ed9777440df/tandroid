package org.telegram.messenger;

import android.util.SparseBooleanArray;
/* loaded from: classes.dex */
public final /* synthetic */ class MessagesController$$ExternalSyntheticLambda93 implements Runnable {
    public final /* synthetic */ MessagesController f$0;
    public final /* synthetic */ SparseBooleanArray f$1;
    public final /* synthetic */ long f$2;

    public /* synthetic */ MessagesController$$ExternalSyntheticLambda93(MessagesController messagesController, SparseBooleanArray sparseBooleanArray, long j) {
        this.f$0 = messagesController;
        this.f$1 = sparseBooleanArray;
        this.f$2 = j;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$checkUnreadReactions$335(this.f$1, this.f$2);
    }
}