package org.telegram.messenger;

import java.util.ArrayList;
/* loaded from: classes.dex */
public final /* synthetic */ class MessagesController$$ExternalSyntheticLambda120 implements Runnable {
    public final /* synthetic */ MessagesController f$0;
    public final /* synthetic */ ArrayList f$1;
    public final /* synthetic */ ArrayList f$2;

    public /* synthetic */ MessagesController$$ExternalSyntheticLambda120(MessagesController messagesController, ArrayList arrayList, ArrayList arrayList2) {
        this.f$0 = messagesController;
        this.f$1 = arrayList;
        this.f$2 = arrayList2;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$processUpdateArray$309(this.f$1, this.f$2);
    }
}