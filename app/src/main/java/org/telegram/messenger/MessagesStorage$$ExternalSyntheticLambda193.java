package org.telegram.messenger;

import java.util.concurrent.CountDownLatch;
import org.telegram.tgnet.TLRPC$Chat;
/* loaded from: classes.dex */
public final /* synthetic */ class MessagesStorage$$ExternalSyntheticLambda193 implements Runnable {
    public final /* synthetic */ MessagesStorage f$0;
    public final /* synthetic */ TLRPC$Chat[] f$1;
    public final /* synthetic */ long f$2;
    public final /* synthetic */ CountDownLatch f$3;

    public /* synthetic */ MessagesStorage$$ExternalSyntheticLambda193(MessagesStorage messagesStorage, TLRPC$Chat[] tLRPC$ChatArr, long j, CountDownLatch countDownLatch) {
        this.f$0 = messagesStorage;
        this.f$1 = tLRPC$ChatArr;
        this.f$2 = j;
        this.f$3 = countDownLatch;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$getChatSync$197(this.f$1, this.f$2, this.f$3);
    }
}