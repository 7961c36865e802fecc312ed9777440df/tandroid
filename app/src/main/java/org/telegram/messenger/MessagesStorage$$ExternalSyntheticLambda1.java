package org.telegram.messenger;

import org.telegram.messenger.MessagesStorage;
/* loaded from: classes.dex */
public final /* synthetic */ class MessagesStorage$$ExternalSyntheticLambda1 implements Runnable {
    public final /* synthetic */ MessagesStorage.IntCallback f$0;
    public final /* synthetic */ int f$1;

    public /* synthetic */ MessagesStorage$$ExternalSyntheticLambda1(MessagesStorage.IntCallback intCallback, int i) {
        this.f$0 = intCallback;
        this.f$1 = i;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.run(this.f$1);
    }
}