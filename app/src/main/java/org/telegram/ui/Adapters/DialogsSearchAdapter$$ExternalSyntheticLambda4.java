package org.telegram.ui.Adapters;

import org.telegram.ui.Adapters.DialogsSearchAdapter;
/* loaded from: classes3.dex */
public final /* synthetic */ class DialogsSearchAdapter$$ExternalSyntheticLambda4 implements Runnable {
    public final /* synthetic */ int f$0;
    public final /* synthetic */ int f$1;
    public final /* synthetic */ DialogsSearchAdapter.OnRecentSearchLoaded f$2;

    public /* synthetic */ DialogsSearchAdapter$$ExternalSyntheticLambda4(int i, int i2, DialogsSearchAdapter.OnRecentSearchLoaded onRecentSearchLoaded) {
        this.f$0 = i;
        this.f$1 = i2;
        this.f$2 = onRecentSearchLoaded;
    }

    @Override // java.lang.Runnable
    public final void run() {
        DialogsSearchAdapter.lambda$loadRecentSearch$5(this.f$0, this.f$1, this.f$2);
    }
}