package org.telegram.ui;

import java.util.ArrayList;
import org.telegram.tgnet.TLRPC$User;
import org.telegram.ui.ChatUsersActivity;
/* loaded from: classes3.dex */
public final /* synthetic */ class ChatUsersActivity$9$$ExternalSyntheticLambda1 implements Runnable {
    public final /* synthetic */ ChatUsersActivity.AnonymousClass9 f$0;
    public final /* synthetic */ int[] f$1;
    public final /* synthetic */ int f$2;
    public final /* synthetic */ ArrayList f$3;
    public final /* synthetic */ Runnable f$4;
    public final /* synthetic */ TLRPC$User f$5;

    public /* synthetic */ ChatUsersActivity$9$$ExternalSyntheticLambda1(ChatUsersActivity.AnonymousClass9 anonymousClass9, int[] iArr, int i, ArrayList arrayList, Runnable runnable, TLRPC$User tLRPC$User) {
        this.f$0 = anonymousClass9;
        this.f$1 = iArr;
        this.f$2 = i;
        this.f$3 = arrayList;
        this.f$4 = runnable;
        this.f$5 = tLRPC$User;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$didSelectUsers$1(this.f$1, this.f$2, this.f$3, this.f$4, this.f$5);
    }
}