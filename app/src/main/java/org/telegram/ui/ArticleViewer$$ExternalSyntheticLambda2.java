package org.telegram.ui;

import android.content.DialogInterface;
/* loaded from: classes3.dex */
public final /* synthetic */ class ArticleViewer$$ExternalSyntheticLambda2 implements DialogInterface.OnDismissListener {
    public final /* synthetic */ ArticleViewer f$0;

    public /* synthetic */ ArticleViewer$$ExternalSyntheticLambda2(ArticleViewer articleViewer) {
        this.f$0 = articleViewer;
    }

    @Override // android.content.DialogInterface.OnDismissListener
    public final void onDismiss(DialogInterface dialogInterface) {
        this.f$0.lambda$showCopyPopup$1(dialogInterface);
    }
}