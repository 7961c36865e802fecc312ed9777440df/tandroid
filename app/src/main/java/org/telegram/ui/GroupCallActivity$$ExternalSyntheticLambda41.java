package org.telegram.ui;

import org.telegram.tgnet.TLObject;
/* loaded from: classes3.dex */
public final /* synthetic */ class GroupCallActivity$$ExternalSyntheticLambda41 implements Runnable {
    public final /* synthetic */ GroupCallActivity f$0;
    public final /* synthetic */ TLObject f$1;
    public final /* synthetic */ int f$2;
    public final /* synthetic */ boolean f$3;

    public /* synthetic */ GroupCallActivity$$ExternalSyntheticLambda41(GroupCallActivity groupCallActivity, TLObject tLObject, int i, boolean z) {
        this.f$0 = groupCallActivity;
        this.f$1 = tLObject;
        this.f$2 = i;
        this.f$3 = z;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$getLink$40(this.f$1, this.f$2, this.f$3);
    }
}