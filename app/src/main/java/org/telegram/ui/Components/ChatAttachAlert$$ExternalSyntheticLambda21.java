package org.telegram.ui.Components;

import org.telegram.ui.ActionBar.BottomSheet;
/* loaded from: classes3.dex */
public final /* synthetic */ class ChatAttachAlert$$ExternalSyntheticLambda21 implements Runnable {
    public final /* synthetic */ ChatAttachAlert f$0;
    public final /* synthetic */ BottomSheet.BottomSheetDelegateInterface f$1;

    public /* synthetic */ ChatAttachAlert$$ExternalSyntheticLambda21(ChatAttachAlert chatAttachAlert, BottomSheet.BottomSheetDelegateInterface bottomSheetDelegateInterface) {
        this.f$0 = chatAttachAlert;
        this.f$1 = bottomSheetDelegateInterface;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$onCustomOpenAnimation$27(this.f$1);
    }
}