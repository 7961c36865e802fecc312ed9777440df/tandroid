package org.telegram.ui;

import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC$TL_error;
/* loaded from: classes3.dex */
public final /* synthetic */ class ProfileActivity$$ExternalSyntheticLambda34 implements RequestDelegate {
    public final /* synthetic */ ProfileActivity f$0;
    public final /* synthetic */ String f$1;

    public /* synthetic */ ProfileActivity$$ExternalSyntheticLambda34(ProfileActivity profileActivity, String str) {
        this.f$0 = profileActivity;
        this.f$1 = str;
    }

    @Override // org.telegram.tgnet.RequestDelegate
    public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        this.f$0.lambda$didUploadPhoto$35(this.f$1, tLObject, tLRPC$TL_error);
    }
}