package org.telegram.messenger;

import java.util.Comparator;
import org.telegram.tgnet.TLRPC$Updates;
/* loaded from: classes.dex */
public final /* synthetic */ class MessagesController$$ExternalSyntheticLambda244 implements Comparator {
    public static final /* synthetic */ MessagesController$$ExternalSyntheticLambda244 INSTANCE = new MessagesController$$ExternalSyntheticLambda244();

    private /* synthetic */ MessagesController$$ExternalSyntheticLambda244() {
    }

    @Override // java.util.Comparator
    public final int compare(Object obj, Object obj2) {
        int lambda$processUpdatesQueue$278;
        lambda$processUpdatesQueue$278 = MessagesController.lambda$processUpdatesQueue$278((TLRPC$Updates) obj, (TLRPC$Updates) obj2);
        return lambda$processUpdatesQueue$278;
    }
}