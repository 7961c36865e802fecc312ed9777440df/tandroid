package org.telegram.messenger;

import java.util.ArrayList;
/* loaded from: classes.dex */
public final /* synthetic */ class MessagesStorage$$ExternalSyntheticLambda151 implements Runnable {
    public final /* synthetic */ MessagesStorage f$0;
    public final /* synthetic */ MessageObject f$1;
    public final /* synthetic */ ArrayList f$2;

    public /* synthetic */ MessagesStorage$$ExternalSyntheticLambda151(MessagesStorage messagesStorage, MessageObject messageObject, ArrayList arrayList) {
        this.f$0 = messagesStorage;
        this.f$1 = messageObject;
        this.f$2 = arrayList;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$replaceMessageIfExists$175(this.f$1, this.f$2);
    }
}