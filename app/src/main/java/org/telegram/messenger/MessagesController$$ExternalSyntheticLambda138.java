package org.telegram.messenger;

import java.util.Comparator;
import org.telegram.messenger.MessagesController;
/* loaded from: classes4.dex */
public final /* synthetic */ class MessagesController$$ExternalSyntheticLambda138 implements Comparator {
    public static final /* synthetic */ MessagesController$$ExternalSyntheticLambda138 INSTANCE = new MessagesController$$ExternalSyntheticLambda138();

    private /* synthetic */ MessagesController$$ExternalSyntheticLambda138() {
    }

    @Override // java.util.Comparator
    public final int compare(Object obj, Object obj2) {
        return MessagesController.lambda$processLoadedDialogFilters$13((MessagesController.DialogFilter) obj, (MessagesController.DialogFilter) obj2);
    }
}
