package org.telegram.ui;

import org.telegram.ui.ActionBar.AlertDialog;
/* loaded from: classes3.dex */
public final /* synthetic */ class CacheControlActivity$$ExternalSyntheticLambda5 implements Runnable {
    public final /* synthetic */ CacheControlActivity f$0;
    public final /* synthetic */ AlertDialog f$1;

    public /* synthetic */ CacheControlActivity$$ExternalSyntheticLambda5(CacheControlActivity cacheControlActivity, AlertDialog alertDialog) {
        this.f$0 = cacheControlActivity;
        this.f$1 = alertDialog;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$cleanupFolders$3(this.f$1);
    }
}