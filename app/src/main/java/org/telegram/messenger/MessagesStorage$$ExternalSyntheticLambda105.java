package org.telegram.messenger;

import org.telegram.tgnet.NativeByteBuffer;
/* loaded from: classes.dex */
public final /* synthetic */ class MessagesStorage$$ExternalSyntheticLambda105 implements Runnable {
    public final /* synthetic */ MessagesStorage f$0;
    public final /* synthetic */ long f$1;
    public final /* synthetic */ NativeByteBuffer f$2;

    public /* synthetic */ MessagesStorage$$ExternalSyntheticLambda105(MessagesStorage messagesStorage, long j, NativeByteBuffer nativeByteBuffer) {
        this.f$0 = messagesStorage;
        this.f$1 = j;
        this.f$2 = nativeByteBuffer;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$createPendingTask$9(this.f$1, this.f$2);
    }
}