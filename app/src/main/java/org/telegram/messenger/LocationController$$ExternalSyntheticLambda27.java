package org.telegram.messenger;

import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC$TL_error;
/* loaded from: classes.dex */
public final /* synthetic */ class LocationController$$ExternalSyntheticLambda27 implements RequestDelegate {
    public final /* synthetic */ LocationController f$0;

    public /* synthetic */ LocationController$$ExternalSyntheticLambda27(LocationController locationController) {
        this.f$0 = locationController;
    }

    @Override // org.telegram.tgnet.RequestDelegate
    public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        this.f$0.lambda$removeSharingLocation$20(tLObject, tLRPC$TL_error);
    }
}