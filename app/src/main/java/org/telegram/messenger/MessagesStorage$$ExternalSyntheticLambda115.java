package org.telegram.messenger;

import java.util.concurrent.CountDownLatch;
/* loaded from: classes.dex */
public final /* synthetic */ class MessagesStorage$$ExternalSyntheticLambda115 implements Runnable {
    public final /* synthetic */ MessagesStorage f$0;
    public final /* synthetic */ long f$1;
    public final /* synthetic */ boolean[] f$2;
    public final /* synthetic */ CountDownLatch f$3;

    public /* synthetic */ MessagesStorage$$ExternalSyntheticLambda115(MessagesStorage messagesStorage, long j, boolean[] zArr, CountDownLatch countDownLatch) {
        this.f$0 = messagesStorage;
        this.f$1 = j;
        this.f$2 = zArr;
        this.f$3 = countDownLatch;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$isMigratedChat$108(this.f$1, this.f$2, this.f$3);
    }
}