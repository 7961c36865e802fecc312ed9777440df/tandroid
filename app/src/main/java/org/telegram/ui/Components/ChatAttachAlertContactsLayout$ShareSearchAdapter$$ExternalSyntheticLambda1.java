package org.telegram.ui.Components;

import org.telegram.ui.Components.ChatAttachAlertContactsLayout;
/* loaded from: classes3.dex */
public final /* synthetic */ class ChatAttachAlertContactsLayout$ShareSearchAdapter$$ExternalSyntheticLambda1 implements Runnable {
    public final /* synthetic */ ChatAttachAlertContactsLayout.ShareSearchAdapter f$0;
    public final /* synthetic */ String f$1;
    public final /* synthetic */ int f$2;

    public /* synthetic */ ChatAttachAlertContactsLayout$ShareSearchAdapter$$ExternalSyntheticLambda1(ChatAttachAlertContactsLayout.ShareSearchAdapter shareSearchAdapter, String str, int i) {
        this.f$0 = shareSearchAdapter;
        this.f$1 = str;
        this.f$2 = i;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$search$0(this.f$1, this.f$2);
    }
}