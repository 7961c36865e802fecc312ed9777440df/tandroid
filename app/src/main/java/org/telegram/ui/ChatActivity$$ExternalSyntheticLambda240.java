package org.telegram.ui;

import org.telegram.tgnet.TLRPC$TL_document;
import org.telegram.ui.Components.AlertsCreator;
/* loaded from: classes3.dex */
public final /* synthetic */ class ChatActivity$$ExternalSyntheticLambda240 implements AlertsCreator.ScheduleDatePickerDelegate {
    public final /* synthetic */ ChatActivity f$0;
    public final /* synthetic */ TLRPC$TL_document f$1;
    public final /* synthetic */ String f$2;
    public final /* synthetic */ Object f$3;

    public /* synthetic */ ChatActivity$$ExternalSyntheticLambda240(ChatActivity chatActivity, TLRPC$TL_document tLRPC$TL_document, String str, Object obj) {
        this.f$0 = chatActivity;
        this.f$1 = tLRPC$TL_document;
        this.f$2 = str;
        this.f$3 = obj;
    }

    @Override // org.telegram.ui.Components.AlertsCreator.ScheduleDatePickerDelegate
    public final void didSelectDate(boolean z, int i) {
        this.f$0.lambda$createView$43(this.f$1, this.f$2, this.f$3, z, i);
    }
}