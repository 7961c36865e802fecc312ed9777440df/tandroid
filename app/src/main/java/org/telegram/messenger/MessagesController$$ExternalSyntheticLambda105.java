package org.telegram.messenger;

import java.util.ArrayList;
/* loaded from: classes.dex */
public final /* synthetic */ class MessagesController$$ExternalSyntheticLambda105 implements Runnable {
    public final /* synthetic */ MessagesController f$0;
    public final /* synthetic */ ArrayList f$1;

    public /* synthetic */ MessagesController$$ExternalSyntheticLambda105(MessagesController messagesController, ArrayList arrayList) {
        this.f$0 = messagesController;
        this.f$1 = arrayList;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$processUpdateArray$316(this.f$1);
    }
}