package org.telegram.messenger;

import java.util.Comparator;
import org.telegram.tgnet.TLRPC$TL_topPeer;
/* loaded from: classes.dex */
public final /* synthetic */ class MediaDataController$$ExternalSyntheticLambda161 implements Comparator {
    public static final /* synthetic */ MediaDataController$$ExternalSyntheticLambda161 INSTANCE = new MediaDataController$$ExternalSyntheticLambda161();

    private /* synthetic */ MediaDataController$$ExternalSyntheticLambda161() {
    }

    @Override // java.util.Comparator
    public final int compare(Object obj, Object obj2) {
        int lambda$increaseInlineRaiting$141;
        lambda$increaseInlineRaiting$141 = MediaDataController.lambda$increaseInlineRaiting$141((TLRPC$TL_topPeer) obj, (TLRPC$TL_topPeer) obj2);
        return lambda$increaseInlineRaiting$141;
    }
}