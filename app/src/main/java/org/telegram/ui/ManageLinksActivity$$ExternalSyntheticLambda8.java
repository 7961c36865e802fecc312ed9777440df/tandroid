package org.telegram.ui;

import org.telegram.tgnet.TLRPC$TL_chatInviteExported;
import org.telegram.tgnet.TLRPC$TL_error;
/* loaded from: classes3.dex */
public final /* synthetic */ class ManageLinksActivity$$ExternalSyntheticLambda8 implements Runnable {
    public final /* synthetic */ ManageLinksActivity f$0;
    public final /* synthetic */ TLRPC$TL_error f$1;
    public final /* synthetic */ TLRPC$TL_chatInviteExported f$2;

    public /* synthetic */ ManageLinksActivity$$ExternalSyntheticLambda8(ManageLinksActivity manageLinksActivity, TLRPC$TL_error tLRPC$TL_error, TLRPC$TL_chatInviteExported tLRPC$TL_chatInviteExported) {
        this.f$0 = manageLinksActivity;
        this.f$1 = tLRPC$TL_error;
        this.f$2 = tLRPC$TL_chatInviteExported;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$deleteLink$13(this.f$1, this.f$2);
    }
}