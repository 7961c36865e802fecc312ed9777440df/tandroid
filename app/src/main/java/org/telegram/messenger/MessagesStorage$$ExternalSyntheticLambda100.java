package org.telegram.messenger;

import java.util.ArrayList;
/* loaded from: classes.dex */
public final /* synthetic */ class MessagesStorage$$ExternalSyntheticLambda100 implements Runnable {
    public final /* synthetic */ MessagesStorage f$0;
    public final /* synthetic */ long f$1;
    public final /* synthetic */ ArrayList f$2;
    public final /* synthetic */ boolean f$3;
    public final /* synthetic */ boolean f$4;

    public /* synthetic */ MessagesStorage$$ExternalSyntheticLambda100(MessagesStorage messagesStorage, long j, ArrayList arrayList, boolean z, boolean z2) {
        this.f$0 = messagesStorage;
        this.f$1 = j;
        this.f$2 = arrayList;
        this.f$3 = z;
        this.f$4 = z2;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$markMessagesAsDeleted$172(this.f$1, this.f$2, this.f$3, this.f$4);
    }
}