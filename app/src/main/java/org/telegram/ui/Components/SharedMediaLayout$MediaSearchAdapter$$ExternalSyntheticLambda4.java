package org.telegram.ui.Components;

import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC$TL_error;
import org.telegram.ui.Components.SharedMediaLayout;
/* loaded from: classes3.dex */
public final /* synthetic */ class SharedMediaLayout$MediaSearchAdapter$$ExternalSyntheticLambda4 implements RequestDelegate {
    public final /* synthetic */ SharedMediaLayout.MediaSearchAdapter f$0;
    public final /* synthetic */ int f$1;
    public final /* synthetic */ int f$2;
    public final /* synthetic */ String f$3;

    public /* synthetic */ SharedMediaLayout$MediaSearchAdapter$$ExternalSyntheticLambda4(SharedMediaLayout.MediaSearchAdapter mediaSearchAdapter, int i, int i2, String str) {
        this.f$0 = mediaSearchAdapter;
        this.f$1 = i;
        this.f$2 = i2;
        this.f$3 = str;
    }

    @Override // org.telegram.tgnet.RequestDelegate
    public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        this.f$0.lambda$queryServerSearch$1(this.f$1, this.f$2, this.f$3, tLObject, tLRPC$TL_error);
    }
}