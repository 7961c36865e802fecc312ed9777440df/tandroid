package org.telegram.ui.Components;

import org.telegram.ui.Components.ShareAlert;
/* loaded from: classes3.dex */
public final /* synthetic */ class ShareAlert$ShareSearchAdapter$$ExternalSyntheticLambda2 implements Runnable {
    public final /* synthetic */ ShareAlert.ShareSearchAdapter f$0;
    public final /* synthetic */ String f$1;
    public final /* synthetic */ int f$2;

    public /* synthetic */ ShareAlert$ShareSearchAdapter$$ExternalSyntheticLambda2(ShareAlert.ShareSearchAdapter shareSearchAdapter, String str, int i) {
        this.f$0 = shareSearchAdapter;
        this.f$1 = str;
        this.f$2 = i;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$searchDialogsInternal$1(this.f$1, this.f$2);
    }
}