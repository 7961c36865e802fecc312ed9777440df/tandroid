package org.telegram.ui;

import org.telegram.ui.ArticleViewer;
/* loaded from: classes3.dex */
public final /* synthetic */ class ArticleViewer$$ExternalSyntheticLambda18 implements Runnable {
    public final /* synthetic */ ArticleViewer.BlockChannelCell f$0;

    public /* synthetic */ ArticleViewer$$ExternalSyntheticLambda18(ArticleViewer.BlockChannelCell blockChannelCell) {
        this.f$0 = blockChannelCell;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.setState(2, false);
    }
}