package org.telegram.ui.Adapters;

import androidx.collection.LongSparseArray;
import java.util.ArrayList;
/* loaded from: classes3.dex */
public final /* synthetic */ class MentionsAdapter$$ExternalSyntheticLambda5 implements Runnable {
    public final /* synthetic */ MentionsAdapter f$0;
    public final /* synthetic */ ArrayList f$1;
    public final /* synthetic */ LongSparseArray f$2;

    public /* synthetic */ MentionsAdapter$$ExternalSyntheticLambda5(MentionsAdapter mentionsAdapter, ArrayList arrayList, LongSparseArray longSparseArray) {
        this.f$0 = mentionsAdapter;
        this.f$1 = arrayList;
        this.f$2 = longSparseArray;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$searchUsernameOrHashtag$7(this.f$1, this.f$2);
    }
}