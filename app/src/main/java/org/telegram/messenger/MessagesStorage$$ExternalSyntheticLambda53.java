package org.telegram.messenger;

import java.util.ArrayList;
/* loaded from: classes.dex */
public final /* synthetic */ class MessagesStorage$$ExternalSyntheticLambda53 implements Runnable {
    public final /* synthetic */ MessagesStorage f$0;
    public final /* synthetic */ int f$1;
    public final /* synthetic */ ArrayList f$2;

    public /* synthetic */ MessagesStorage$$ExternalSyntheticLambda53(MessagesStorage messagesStorage, int i, ArrayList arrayList) {
        this.f$0 = messagesStorage;
        this.f$1 = i;
        this.f$2 = arrayList;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$getDownloadQueue$148(this.f$1, this.f$2);
    }
}