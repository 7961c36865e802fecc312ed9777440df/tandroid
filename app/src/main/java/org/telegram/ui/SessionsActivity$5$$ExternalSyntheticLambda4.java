package org.telegram.ui;

import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC$TL_error;
import org.telegram.ui.SessionsActivity;
/* loaded from: classes3.dex */
public final /* synthetic */ class SessionsActivity$5$$ExternalSyntheticLambda4 implements RequestDelegate {
    public final /* synthetic */ SessionsActivity.AnonymousClass5 f$0;
    public final /* synthetic */ Runnable f$1;

    public /* synthetic */ SessionsActivity$5$$ExternalSyntheticLambda4(SessionsActivity.AnonymousClass5 anonymousClass5, Runnable runnable) {
        this.f$0 = anonymousClass5;
        this.f$1 = runnable;
    }

    @Override // org.telegram.tgnet.RequestDelegate
    public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        this.f$0.lambda$processQr$2(this.f$1, tLObject, tLRPC$TL_error);
    }
}