package org.telegram.messenger;

import java.util.concurrent.CountDownLatch;
/* loaded from: classes.dex */
public final /* synthetic */ class MessagesStorage$$ExternalSyntheticLambda123 implements Runnable {
    public final /* synthetic */ MessagesStorage f$0;
    public final /* synthetic */ String f$1;
    public final /* synthetic */ int f$2;
    public final /* synthetic */ Object[] f$3;
    public final /* synthetic */ CountDownLatch f$4;

    public /* synthetic */ MessagesStorage$$ExternalSyntheticLambda123(MessagesStorage messagesStorage, String str, int i, Object[] objArr, CountDownLatch countDownLatch) {
        this.f$0 = messagesStorage;
        this.f$1 = str;
        this.f$2 = i;
        this.f$3 = objArr;
        this.f$4 = countDownLatch;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$getSentFile$129(this.f$1, this.f$2, this.f$3, this.f$4);
    }
}