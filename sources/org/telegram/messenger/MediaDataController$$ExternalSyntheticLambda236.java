package org.telegram.messenger;

import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC$TL_error;
/* loaded from: classes3.dex */
public final /* synthetic */ class MediaDataController$$ExternalSyntheticLambda236 implements RequestDelegate {
    public static final /* synthetic */ MediaDataController$$ExternalSyntheticLambda236 INSTANCE = new MediaDataController$$ExternalSyntheticLambda236();

    private /* synthetic */ MediaDataController$$ExternalSyntheticLambda236() {
    }

    @Override // org.telegram.tgnet.RequestDelegate
    public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        MediaDataController.lambda$removePeer$146(tLObject, tLRPC$TL_error);
    }
}