package org.telegram.ui;

import java.util.ArrayList;
import java.util.HashMap;
import org.telegram.tgnet.TLObject;
/* loaded from: classes3.dex */
public final /* synthetic */ class MessageSeenView$$ExternalSyntheticLambda0 implements Runnable {
    public final /* synthetic */ MessageSeenView f$0;
    public final /* synthetic */ TLObject f$1;
    public final /* synthetic */ int f$2;
    public final /* synthetic */ HashMap f$3;
    public final /* synthetic */ ArrayList f$4;

    public /* synthetic */ MessageSeenView$$ExternalSyntheticLambda0(MessageSeenView messageSeenView, TLObject tLObject, int i, HashMap hashMap, ArrayList arrayList) {
        this.f$0 = messageSeenView;
        this.f$1 = tLObject;
        this.f$2 = i;
        this.f$3 = hashMap;
        this.f$4 = arrayList;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$new$2(this.f$1, this.f$2, this.f$3, this.f$4);
    }
}