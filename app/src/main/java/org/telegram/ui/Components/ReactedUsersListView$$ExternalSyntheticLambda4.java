package org.telegram.ui.Components;

import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC$TL_error;
/* loaded from: classes3.dex */
public final /* synthetic */ class ReactedUsersListView$$ExternalSyntheticLambda4 implements RequestDelegate {
    public final /* synthetic */ ReactedUsersListView f$0;

    public /* synthetic */ ReactedUsersListView$$ExternalSyntheticLambda4(ReactedUsersListView reactedUsersListView) {
        this.f$0 = reactedUsersListView;
    }

    @Override // org.telegram.tgnet.RequestDelegate
    public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        this.f$0.lambda$load$5(tLObject, tLRPC$TL_error);
    }
}