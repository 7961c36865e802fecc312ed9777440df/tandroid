package org.telegram.ui;

import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC$TL_error;
/* loaded from: classes3.dex */
public final /* synthetic */ class PeopleNearbyActivity$$ExternalSyntheticLambda8 implements RequestDelegate {
    public final /* synthetic */ PeopleNearbyActivity f$0;
    public final /* synthetic */ int f$1;

    public /* synthetic */ PeopleNearbyActivity$$ExternalSyntheticLambda8(PeopleNearbyActivity peopleNearbyActivity, int i) {
        this.f$0 = peopleNearbyActivity;
        this.f$1 = i;
    }

    @Override // org.telegram.tgnet.RequestDelegate
    public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        this.f$0.lambda$sendRequest$7(this.f$1, tLObject, tLRPC$TL_error);
    }
}