package org.telegram.ui;

import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC$TL_error;
import org.telegram.ui.ArticleViewer;
/* loaded from: classes3.dex */
public final /* synthetic */ class ArticleViewer$$ExternalSyntheticLambda32 implements Runnable {
    public final /* synthetic */ ArticleViewer f$0;
    public final /* synthetic */ ArticleViewer.WebpageAdapter f$1;
    public final /* synthetic */ TLRPC$TL_error f$2;
    public final /* synthetic */ TLObject f$3;
    public final /* synthetic */ int f$4;
    public final /* synthetic */ ArticleViewer.BlockChannelCell f$5;

    public /* synthetic */ ArticleViewer$$ExternalSyntheticLambda32(ArticleViewer articleViewer, ArticleViewer.WebpageAdapter webpageAdapter, TLRPC$TL_error tLRPC$TL_error, TLObject tLObject, int i, ArticleViewer.BlockChannelCell blockChannelCell) {
        this.f$0 = articleViewer;
        this.f$1 = webpageAdapter;
        this.f$2 = tLRPC$TL_error;
        this.f$3 = tLObject;
        this.f$4 = i;
        this.f$5 = blockChannelCell;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$loadChannel$38(this.f$1, this.f$2, this.f$3, this.f$4, this.f$5);
    }
}