package org.telegram.ui.Adapters;

import java.util.ArrayList;
import org.telegram.messenger.MediaDataController;
/* loaded from: classes3.dex */
public final /* synthetic */ class MentionsAdapter$$ExternalSyntheticLambda6 implements MediaDataController.KeywordResultCallback {
    public final /* synthetic */ MentionsAdapter f$0;

    public /* synthetic */ MentionsAdapter$$ExternalSyntheticLambda6(MentionsAdapter mentionsAdapter) {
        this.f$0 = mentionsAdapter;
    }

    @Override // org.telegram.messenger.MediaDataController.KeywordResultCallback
    public final void run(ArrayList arrayList, String str) {
        this.f$0.lambda$searchUsernameOrHashtag$8(arrayList, str);
    }
}