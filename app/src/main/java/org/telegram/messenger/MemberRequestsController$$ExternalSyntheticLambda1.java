package org.telegram.messenger;

import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC$TL_error;
/* loaded from: classes.dex */
public final /* synthetic */ class MemberRequestsController$$ExternalSyntheticLambda1 implements RequestDelegate {
    public final /* synthetic */ MemberRequestsController f$0;
    public final /* synthetic */ long f$1;
    public final /* synthetic */ RequestDelegate f$2;

    public /* synthetic */ MemberRequestsController$$ExternalSyntheticLambda1(MemberRequestsController memberRequestsController, long j, RequestDelegate requestDelegate) {
        this.f$0 = memberRequestsController;
        this.f$1 = j;
        this.f$2 = requestDelegate;
    }

    @Override // org.telegram.tgnet.RequestDelegate
    public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        this.f$0.lambda$getImporters$1(this.f$1, this.f$2, tLObject, tLRPC$TL_error);
    }
}