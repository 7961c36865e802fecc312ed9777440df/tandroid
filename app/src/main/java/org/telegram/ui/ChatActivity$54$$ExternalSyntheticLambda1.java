package org.telegram.ui;

import java.util.ArrayList;
import org.telegram.ui.ChatActivity;
/* loaded from: classes3.dex */
public final /* synthetic */ class ChatActivity$54$$ExternalSyntheticLambda1 implements Runnable {
    public final /* synthetic */ ChatActivity.AnonymousClass54 f$0;
    public final /* synthetic */ boolean f$1;
    public final /* synthetic */ ArrayList f$2;
    public final /* synthetic */ ArrayList f$3;
    public final /* synthetic */ int f$4;
    public final /* synthetic */ int f$5;

    public /* synthetic */ ChatActivity$54$$ExternalSyntheticLambda1(ChatActivity.AnonymousClass54 anonymousClass54, boolean z, ArrayList arrayList, ArrayList arrayList2, int i, int i2) {
        this.f$0 = anonymousClass54;
        this.f$1 = z;
        this.f$2 = arrayList;
        this.f$3 = arrayList2;
        this.f$4 = i;
        this.f$5 = i2;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$onUnpin$0(this.f$1, this.f$2, this.f$3, this.f$4, this.f$5);
    }
}