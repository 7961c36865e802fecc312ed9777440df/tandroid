package org.telegram.ui.Components;

import java.util.ArrayList;
/* loaded from: classes3.dex */
public final /* synthetic */ class SearchDownloadsContainer$$ExternalSyntheticLambda3 implements Runnable {
    public final /* synthetic */ SearchDownloadsContainer f$0;
    public final /* synthetic */ String f$1;
    public final /* synthetic */ ArrayList f$2;
    public final /* synthetic */ ArrayList f$3;

    public /* synthetic */ SearchDownloadsContainer$$ExternalSyntheticLambda3(SearchDownloadsContainer searchDownloadsContainer, String str, ArrayList arrayList, ArrayList arrayList2) {
        this.f$0 = searchDownloadsContainer;
        this.f$1 = str;
        this.f$2 = arrayList;
        this.f$3 = arrayList2;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$update$4(this.f$1, this.f$2, this.f$3);
    }
}