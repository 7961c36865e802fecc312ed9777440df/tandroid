package org.telegram.ui;

import org.telegram.tgnet.TLObject;
import org.telegram.ui.WallpapersListActivity;
/* loaded from: classes3.dex */
public final /* synthetic */ class WallpapersListActivity$SearchAdapter$$ExternalSyntheticLambda0 implements Runnable {
    public final /* synthetic */ WallpapersListActivity.SearchAdapter f$0;
    public final /* synthetic */ int f$1;
    public final /* synthetic */ TLObject f$2;

    public /* synthetic */ WallpapersListActivity$SearchAdapter$$ExternalSyntheticLambda0(WallpapersListActivity.SearchAdapter searchAdapter, int i, TLObject tLObject) {
        this.f$0 = searchAdapter;
        this.f$1 = i;
        this.f$2 = tLObject;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$searchImages$3(this.f$1, this.f$2);
    }
}