package org.telegram.ui.Adapters;

import java.util.ArrayList;
/* loaded from: classes3.dex */
public final /* synthetic */ class SearchAdapterHelper$$ExternalSyntheticLambda2 implements Runnable {
    public final /* synthetic */ SearchAdapterHelper f$0;
    public final /* synthetic */ ArrayList f$1;

    public /* synthetic */ SearchAdapterHelper$$ExternalSyntheticLambda2(SearchAdapterHelper searchAdapterHelper, ArrayList arrayList) {
        this.f$0 = searchAdapterHelper;
        this.f$1 = arrayList;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$putRecentHashtags$7(this.f$1);
    }
}