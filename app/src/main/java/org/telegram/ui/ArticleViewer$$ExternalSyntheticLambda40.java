package org.telegram.ui;

import org.telegram.ui.Cells.TextSelectionHelper;
/* loaded from: classes3.dex */
public final /* synthetic */ class ArticleViewer$$ExternalSyntheticLambda40 implements TextSelectionHelper.OnTranslateListener {
    public final /* synthetic */ ArticleViewer f$0;

    public /* synthetic */ ArticleViewer$$ExternalSyntheticLambda40(ArticleViewer articleViewer) {
        this.f$0 = articleViewer;
    }

    @Override // org.telegram.ui.Cells.TextSelectionHelper.OnTranslateListener
    public final void run(CharSequence charSequence, String str, String str2, Runnable runnable) {
        this.f$0.lambda$setParentActivity$24(charSequence, str, str2, runnable);
    }
}