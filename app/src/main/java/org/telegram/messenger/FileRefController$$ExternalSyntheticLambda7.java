package org.telegram.messenger;

import org.telegram.tgnet.TLRPC$User;
/* loaded from: classes.dex */
public final /* synthetic */ class FileRefController$$ExternalSyntheticLambda7 implements Runnable {
    public final /* synthetic */ FileRefController f$0;
    public final /* synthetic */ TLRPC$User f$1;

    public /* synthetic */ FileRefController$$ExternalSyntheticLambda7(FileRefController fileRefController, TLRPC$User tLRPC$User) {
        this.f$0 = fileRefController;
        this.f$1 = tLRPC$User;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$onRequestComplete$33(this.f$1);
    }
}