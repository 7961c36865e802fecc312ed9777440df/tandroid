package org.telegram.ui.Components;

import java.util.Comparator;
import org.telegram.ui.Components.ChatAttachAlertDocumentLayout;
/* loaded from: classes3.dex */
public final /* synthetic */ class ChatAttachAlertDocumentLayout$$ExternalSyntheticLambda2 implements Comparator {
    public final /* synthetic */ ChatAttachAlertDocumentLayout f$0;

    public /* synthetic */ ChatAttachAlertDocumentLayout$$ExternalSyntheticLambda2(ChatAttachAlertDocumentLayout chatAttachAlertDocumentLayout) {
        this.f$0 = chatAttachAlertDocumentLayout;
    }

    @Override // java.util.Comparator
    public final int compare(Object obj, Object obj2) {
        int lambda$sortFileItems$6;
        lambda$sortFileItems$6 = this.f$0.lambda$sortFileItems$6((ChatAttachAlertDocumentLayout.ListItem) obj, (ChatAttachAlertDocumentLayout.ListItem) obj2);
        return lambda$sortFileItems$6;
    }
}