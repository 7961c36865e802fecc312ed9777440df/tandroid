package org.telegram.messenger;

import java.util.ArrayList;
/* loaded from: classes.dex */
public final /* synthetic */ class MessagesController$$ExternalSyntheticLambda85 implements Runnable {
    public final /* synthetic */ MessagesController f$0;
    public final /* synthetic */ long f$1;
    public final /* synthetic */ ArrayList f$2;
    public final /* synthetic */ ArrayList f$3;

    public /* synthetic */ MessagesController$$ExternalSyntheticLambda85(MessagesController messagesController, long j, ArrayList arrayList, ArrayList arrayList2) {
        this.f$0 = messagesController;
        this.f$1 = j;
        this.f$2 = arrayList;
        this.f$3 = arrayList2;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$reloadMessages$52(this.f$1, this.f$2, this.f$3);
    }
}