package org.telegram.messenger;

import java.util.Comparator;
import org.telegram.tgnet.TLRPC$Updates;
/* loaded from: classes.dex */
public final /* synthetic */ class MessagesController$$ExternalSyntheticLambda267 implements Comparator {
    public static final /* synthetic */ MessagesController$$ExternalSyntheticLambda267 INSTANCE = new MessagesController$$ExternalSyntheticLambda267();

    private /* synthetic */ MessagesController$$ExternalSyntheticLambda267() {
    }

    @Override // java.util.Comparator
    public final int compare(Object obj, Object obj2) {
        int lambda$processUpdatesQueue$283;
        lambda$processUpdatesQueue$283 = MessagesController.lambda$processUpdatesQueue$283((TLRPC$Updates) obj, (TLRPC$Updates) obj2);
        return lambda$processUpdatesQueue$283;
    }
}