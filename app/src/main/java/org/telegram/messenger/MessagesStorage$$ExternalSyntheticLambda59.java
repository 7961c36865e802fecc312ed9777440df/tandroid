package org.telegram.messenger;

import java.util.ArrayList;
/* loaded from: classes.dex */
public final /* synthetic */ class MessagesStorage$$ExternalSyntheticLambda59 implements Runnable {
    public final /* synthetic */ MessagesStorage f$0;
    public final /* synthetic */ int f$1;
    public final /* synthetic */ ArrayList f$2;
    public final /* synthetic */ long f$3;

    public /* synthetic */ MessagesStorage$$ExternalSyntheticLambda59(MessagesStorage messagesStorage, int i, ArrayList arrayList, long j) {
        this.f$0 = messagesStorage;
        this.f$1 = i;
        this.f$2 = arrayList;
        this.f$3 = j;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$loadPendingTasks$25(this.f$1, this.f$2, this.f$3);
    }
}