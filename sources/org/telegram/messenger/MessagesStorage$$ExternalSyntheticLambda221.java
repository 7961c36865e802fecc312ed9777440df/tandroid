package org.telegram.messenger;

import java.util.Comparator;
import org.telegram.tgnet.TLRPC$Message;
/* loaded from: classes.dex */
public final /* synthetic */ class MessagesStorage$$ExternalSyntheticLambda221 implements Comparator {
    public static final /* synthetic */ MessagesStorage$$ExternalSyntheticLambda221 INSTANCE = new MessagesStorage$$ExternalSyntheticLambda221();

    private /* synthetic */ MessagesStorage$$ExternalSyntheticLambda221() {
    }

    @Override // java.util.Comparator
    public final int compare(Object obj, Object obj2) {
        int lambda$getMessagesInternal$141;
        lambda$getMessagesInternal$141 = MessagesStorage.lambda$getMessagesInternal$141((TLRPC$Message) obj, (TLRPC$Message) obj2);
        return lambda$getMessagesInternal$141;
    }
}