package org.telegram.messenger;

import java.util.concurrent.CountDownLatch;
/* loaded from: classes.dex */
public final /* synthetic */ class MessagesStorage$$ExternalSyntheticLambda113 implements Runnable {
    public final /* synthetic */ MessagesStorage f$0;
    public final /* synthetic */ long f$1;
    public final /* synthetic */ Integer[] f$2;
    public final /* synthetic */ CountDownLatch f$3;

    public /* synthetic */ MessagesStorage$$ExternalSyntheticLambda113(MessagesStorage messagesStorage, long j, Integer[] numArr, CountDownLatch countDownLatch) {
        this.f$0 = messagesStorage;
        this.f$1 = j;
        this.f$2 = numArr;
        this.f$3 = countDownLatch;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$getChannelPtsSync$195(this.f$1, this.f$2, this.f$3);
    }
}