package org.telegram.messenger;

import java.util.ArrayList;
/* loaded from: classes.dex */
public final /* synthetic */ class MessagesStorage$$ExternalSyntheticLambda140 implements Runnable {
    public final /* synthetic */ MessagesStorage f$0;
    public final /* synthetic */ ArrayList f$1;
    public final /* synthetic */ long f$2;

    public /* synthetic */ MessagesStorage$$ExternalSyntheticLambda140(MessagesStorage messagesStorage, ArrayList arrayList, long j) {
        this.f$0 = messagesStorage;
        this.f$1 = arrayList;
        this.f$2 = j;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$emptyMessagesMedia$73(this.f$1, this.f$2);
    }
}