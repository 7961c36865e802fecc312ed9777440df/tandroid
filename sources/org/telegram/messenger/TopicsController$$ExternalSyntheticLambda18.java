package org.telegram.messenger;

import java.util.Comparator;
import org.telegram.tgnet.TLRPC$TL_forumTopic;
/* loaded from: classes.dex */
public final /* synthetic */ class TopicsController$$ExternalSyntheticLambda18 implements Comparator {
    public static final /* synthetic */ TopicsController$$ExternalSyntheticLambda18 INSTANCE = new TopicsController$$ExternalSyntheticLambda18();

    private /* synthetic */ TopicsController$$ExternalSyntheticLambda18() {
    }

    @Override // java.util.Comparator
    public final int compare(Object obj, Object obj2) {
        int lambda$sortTopics$6;
        lambda$sortTopics$6 = TopicsController.lambda$sortTopics$6((TLRPC$TL_forumTopic) obj, (TLRPC$TL_forumTopic) obj2);
        return lambda$sortTopics$6;
    }
}