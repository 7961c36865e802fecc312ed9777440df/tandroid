package org.telegram.messenger;

import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;
/* loaded from: classes4.dex */
public final /* synthetic */ class FileRefController$$ExternalSyntheticLambda29 implements RequestDelegate {
    public static final /* synthetic */ FileRefController$$ExternalSyntheticLambda29 INSTANCE = new FileRefController$$ExternalSyntheticLambda29();

    private /* synthetic */ FileRefController$$ExternalSyntheticLambda29() {
    }

    @Override // org.telegram.tgnet.RequestDelegate
    public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
        FileRefController.lambda$onUpdateObjectReference$29(tLObject, tL_error);
    }
}
