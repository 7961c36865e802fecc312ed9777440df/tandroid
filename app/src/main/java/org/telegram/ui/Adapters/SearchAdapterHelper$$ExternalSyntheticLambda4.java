package org.telegram.ui.Adapters;

import java.util.ArrayList;
import java.util.HashMap;
/* loaded from: classes3.dex */
public final /* synthetic */ class SearchAdapterHelper$$ExternalSyntheticLambda4 implements Runnable {
    public final /* synthetic */ SearchAdapterHelper f$0;
    public final /* synthetic */ ArrayList f$1;
    public final /* synthetic */ HashMap f$2;

    public /* synthetic */ SearchAdapterHelper$$ExternalSyntheticLambda4(SearchAdapterHelper searchAdapterHelper, ArrayList arrayList, HashMap hashMap) {
        this.f$0 = searchAdapterHelper;
        this.f$1 = arrayList;
        this.f$2 = hashMap;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$loadRecentHashtags$5(this.f$1, this.f$2);
    }
}