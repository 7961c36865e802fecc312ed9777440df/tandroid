package org.telegram.messenger;

import java.util.ArrayList;
/* loaded from: classes.dex */
public final /* synthetic */ class MessagesStorage$$ExternalSyntheticLambda147 implements Runnable {
    public final /* synthetic */ MessagesStorage f$0;
    public final /* synthetic */ ArrayList f$1;
    public final /* synthetic */ boolean f$2;
    public final /* synthetic */ boolean f$3;

    public /* synthetic */ MessagesStorage$$ExternalSyntheticLambda147(MessagesStorage messagesStorage, ArrayList arrayList, boolean z, boolean z2) {
        this.f$0 = messagesStorage;
        this.f$1 = arrayList;
        this.f$2 = z;
        this.f$3 = z2;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$updateUsers$164(this.f$1, this.f$2, this.f$3);
    }
}