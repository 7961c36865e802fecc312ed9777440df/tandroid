package org.telegram.ui;

import org.telegram.tgnet.TLRPC$TL_channels_joinChannel;
import org.telegram.tgnet.TLRPC$TL_error;
import org.telegram.ui.ArticleViewer;
/* loaded from: classes3.dex */
public final /* synthetic */ class ArticleViewer$$ExternalSyntheticLambda31 implements Runnable {
    public final /* synthetic */ ArticleViewer f$0;
    public final /* synthetic */ ArticleViewer.BlockChannelCell f$1;
    public final /* synthetic */ int f$2;
    public final /* synthetic */ TLRPC$TL_error f$3;
    public final /* synthetic */ TLRPC$TL_channels_joinChannel f$4;

    public /* synthetic */ ArticleViewer$$ExternalSyntheticLambda31(ArticleViewer articleViewer, ArticleViewer.BlockChannelCell blockChannelCell, int i, TLRPC$TL_error tLRPC$TL_error, TLRPC$TL_channels_joinChannel tLRPC$TL_channels_joinChannel) {
        this.f$0 = articleViewer;
        this.f$1 = blockChannelCell;
        this.f$2 = i;
        this.f$3 = tLRPC$TL_error;
        this.f$4 = tLRPC$TL_channels_joinChannel;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$joinChannel$40(this.f$1, this.f$2, this.f$3, this.f$4);
    }
}