package org.telegram.messenger;

import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC$TL_error;
/* loaded from: classes.dex */
public final /* synthetic */ class MediaDataController$$ExternalSyntheticLambda99 implements Runnable {
    public final /* synthetic */ MediaDataController f$0;
    public final /* synthetic */ TLRPC$TL_error f$1;
    public final /* synthetic */ TLObject f$2;
    public final /* synthetic */ int f$3;

    public /* synthetic */ MediaDataController$$ExternalSyntheticLambda99(MediaDataController mediaDataController, TLRPC$TL_error tLRPC$TL_error, TLObject tLObject, int i) {
        this.f$0 = mediaDataController;
        this.f$1 = tLRPC$TL_error;
        this.f$2 = tLObject;
        this.f$3 = i;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$loadArchivedStickersCount$53(this.f$1, this.f$2, this.f$3);
    }
}