package org.telegram.messenger;

import java.util.Comparator;
import org.telegram.messenger.SharedConfig;
/* loaded from: classes.dex */
public final /* synthetic */ class SharedConfig$$ExternalSyntheticLambda5 implements Comparator {
    public static final /* synthetic */ SharedConfig$$ExternalSyntheticLambda5 INSTANCE = new SharedConfig$$ExternalSyntheticLambda5();

    private /* synthetic */ SharedConfig$$ExternalSyntheticLambda5() {
    }

    @Override // java.util.Comparator
    public final int compare(Object obj, Object obj2) {
        int lambda$checkKeepMedia$1;
        lambda$checkKeepMedia$1 = SharedConfig.lambda$checkKeepMedia$1((SharedConfig.FileInfoInternal) obj, (SharedConfig.FileInfoInternal) obj2);
        return lambda$checkKeepMedia$1;
    }
}