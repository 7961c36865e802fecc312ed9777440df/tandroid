package org.telegram.ui;

import org.telegram.messenger.UserConfig;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC$Photo;
import org.telegram.ui.ProfileActivity;
/* loaded from: classes3.dex */
public final /* synthetic */ class ProfileActivity$5$$ExternalSyntheticLambda6 implements Runnable {
    public final /* synthetic */ ProfileActivity.AnonymousClass5 f$0;
    public final /* synthetic */ TLObject f$1;
    public final /* synthetic */ UserConfig f$2;
    public final /* synthetic */ TLRPC$Photo f$3;

    public /* synthetic */ ProfileActivity$5$$ExternalSyntheticLambda6(ProfileActivity.AnonymousClass5 anonymousClass5, TLObject tLObject, UserConfig userConfig, TLRPC$Photo tLRPC$Photo) {
        this.f$0 = anonymousClass5;
        this.f$1 = tLObject;
        this.f$2 = userConfig;
        this.f$3 = tLRPC$Photo;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$onItemClick$9(this.f$1, this.f$2, this.f$3);
    }
}