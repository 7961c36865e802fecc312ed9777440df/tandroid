package org.telegram.ui.Components;

import java.util.ArrayList;
import org.telegram.ui.Components.ChatAttachAlertContactsLayout;
/* loaded from: classes3.dex */
public final /* synthetic */ class ChatAttachAlertContactsLayout$ShareSearchAdapter$$ExternalSyntheticLambda0 implements Runnable {
    public final /* synthetic */ ChatAttachAlertContactsLayout.ShareSearchAdapter f$0;
    public final /* synthetic */ int f$1;
    public final /* synthetic */ ArrayList f$2;
    public final /* synthetic */ ArrayList f$3;

    public /* synthetic */ ChatAttachAlertContactsLayout$ShareSearchAdapter$$ExternalSyntheticLambda0(ChatAttachAlertContactsLayout.ShareSearchAdapter shareSearchAdapter, int i, ArrayList arrayList, ArrayList arrayList2) {
        this.f$0 = shareSearchAdapter;
        this.f$1 = i;
        this.f$2 = arrayList;
        this.f$3 = arrayList2;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$updateSearchResults$3(this.f$1, this.f$2, this.f$3);
    }
}