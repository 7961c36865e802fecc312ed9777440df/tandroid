package org.telegram.ui;

import org.telegram.tgnet.TLRPC$Chat;
import org.telegram.tgnet.TLRPC$User;
/* loaded from: classes3.dex */
public final /* synthetic */ class DialogsActivity$$ExternalSyntheticLambda43 implements Runnable {
    public final /* synthetic */ DialogsActivity f$0;
    public final /* synthetic */ TLRPC$Chat f$1;
    public final /* synthetic */ long f$2;
    public final /* synthetic */ boolean f$3;
    public final /* synthetic */ TLRPC$User f$4;

    public /* synthetic */ DialogsActivity$$ExternalSyntheticLambda43(DialogsActivity dialogsActivity, TLRPC$Chat tLRPC$Chat, long j, boolean z, TLRPC$User tLRPC$User) {
        this.f$0 = dialogsActivity;
        this.f$1 = tLRPC$Chat;
        this.f$2 = j;
        this.f$3 = z;
        this.f$4 = tLRPC$User;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$didReceivedNotification$45(this.f$1, this.f$2, this.f$3, this.f$4);
    }
}