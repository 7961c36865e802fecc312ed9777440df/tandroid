package org.telegram.messenger;

import java.util.Comparator;
import org.telegram.tgnet.TLRPC$MessageEntity;
/* loaded from: classes.dex */
public final /* synthetic */ class MediaDataController$$ExternalSyntheticLambda162 implements Comparator {
    public static final /* synthetic */ MediaDataController$$ExternalSyntheticLambda162 INSTANCE = new MediaDataController$$ExternalSyntheticLambda162();

    private /* synthetic */ MediaDataController$$ExternalSyntheticLambda162() {
    }

    @Override // java.util.Comparator
    public final int compare(Object obj, Object obj2) {
        int lambda$getTextStyleRuns$169;
        lambda$getTextStyleRuns$169 = MediaDataController.lambda$getTextStyleRuns$169((TLRPC$MessageEntity) obj, (TLRPC$MessageEntity) obj2);
        return lambda$getTextStyleRuns$169;
    }
}
