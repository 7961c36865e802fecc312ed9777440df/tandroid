package org.telegram.messenger;

import java.util.Comparator;
import org.telegram.messenger.SavedMessagesController;
/* loaded from: classes.dex */
public final /* synthetic */ class SavedMessagesController$$ExternalSyntheticLambda14 implements Comparator {
    public static final /* synthetic */ SavedMessagesController$$ExternalSyntheticLambda14 INSTANCE = new SavedMessagesController$$ExternalSyntheticLambda14();

    private /* synthetic */ SavedMessagesController$$ExternalSyntheticLambda14() {
    }

    @Override // java.util.Comparator
    public final int compare(Object obj, Object obj2) {
        int lambda$updateAllDialogs$0;
        lambda$updateAllDialogs$0 = SavedMessagesController.lambda$updateAllDialogs$0((SavedMessagesController.SavedDialog) obj, (SavedMessagesController.SavedDialog) obj2);
        return lambda$updateAllDialogs$0;
    }
}
