package org.telegram.messenger;

import java.util.Comparator;
import org.telegram.ui.Adapters.DialogsSearchAdapter;
/* loaded from: classes.dex */
public final /* synthetic */ class MessagesStorage$$ExternalSyntheticLambda213 implements Comparator {
    public static final /* synthetic */ MessagesStorage$$ExternalSyntheticLambda213 INSTANCE = new MessagesStorage$$ExternalSyntheticLambda213();

    private /* synthetic */ MessagesStorage$$ExternalSyntheticLambda213() {
    }

    @Override // java.util.Comparator
    public final int compare(Object obj, Object obj2) {
        int lambda$localSearch$210;
        lambda$localSearch$210 = MessagesStorage.lambda$localSearch$210((DialogsSearchAdapter.DialogSearchResult) obj, (DialogsSearchAdapter.DialogSearchResult) obj2);
        return lambda$localSearch$210;
    }
}