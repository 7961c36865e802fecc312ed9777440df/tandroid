package org.telegram.messenger;

import org.telegram.tgnet.TLRPC$Chat;
/* loaded from: classes.dex */
public final /* synthetic */ class FileRefController$$ExternalSyntheticLambda2 implements Runnable {
    public final /* synthetic */ FileRefController f$0;
    public final /* synthetic */ TLRPC$Chat f$1;

    public /* synthetic */ FileRefController$$ExternalSyntheticLambda2(FileRefController fileRefController, TLRPC$Chat tLRPC$Chat) {
        this.f$0 = fileRefController;
        this.f$1 = tLRPC$Chat;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$onRequestComplete$34(this.f$1);
    }
}