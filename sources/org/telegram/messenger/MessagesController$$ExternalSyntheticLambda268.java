package org.telegram.messenger;

import java.util.Comparator;
import org.telegram.tgnet.TLRPC$Updates;
/* loaded from: classes.dex */
public final /* synthetic */ class MessagesController$$ExternalSyntheticLambda268 implements Comparator {
    public static final /* synthetic */ MessagesController$$ExternalSyntheticLambda268 INSTANCE = new MessagesController$$ExternalSyntheticLambda268();

    private /* synthetic */ MessagesController$$ExternalSyntheticLambda268() {
    }

    @Override // java.util.Comparator
    public final int compare(Object obj, Object obj2) {
        int lambda$processUpdatesQueue$284;
        lambda$processUpdatesQueue$284 = MessagesController.lambda$processUpdatesQueue$284((TLRPC$Updates) obj, (TLRPC$Updates) obj2);
        return lambda$processUpdatesQueue$284;
    }
}