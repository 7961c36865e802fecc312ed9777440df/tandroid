package org.telegram.ui.Adapters;

import androidx.collection.LongSparseArray;
import java.util.ArrayList;
import org.telegram.ui.Adapters.DialogsSearchAdapter;
/* loaded from: classes3.dex */
public final /* synthetic */ class DialogsSearchAdapter$$ExternalSyntheticLambda5 implements Runnable {
    public final /* synthetic */ DialogsSearchAdapter.OnRecentSearchLoaded f$0;
    public final /* synthetic */ ArrayList f$1;
    public final /* synthetic */ LongSparseArray f$2;

    public /* synthetic */ DialogsSearchAdapter$$ExternalSyntheticLambda5(DialogsSearchAdapter.OnRecentSearchLoaded onRecentSearchLoaded, ArrayList arrayList, LongSparseArray longSparseArray) {
        this.f$0 = onRecentSearchLoaded;
        this.f$1 = arrayList;
        this.f$2 = longSparseArray;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.setRecentSearch(this.f$1, this.f$2);
    }
}