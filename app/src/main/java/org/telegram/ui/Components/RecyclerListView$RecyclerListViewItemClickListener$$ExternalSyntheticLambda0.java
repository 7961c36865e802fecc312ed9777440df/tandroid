package org.telegram.ui.Components;

import org.telegram.ui.Components.RecyclerListView;
/* loaded from: classes3.dex */
public final /* synthetic */ class RecyclerListView$RecyclerListViewItemClickListener$$ExternalSyntheticLambda0 implements Runnable {
    public final /* synthetic */ RecyclerListView.RecyclerListViewItemClickListener f$0;
    public final /* synthetic */ float f$1;
    public final /* synthetic */ float f$2;

    public /* synthetic */ RecyclerListView$RecyclerListViewItemClickListener$$ExternalSyntheticLambda0(RecyclerListView.RecyclerListViewItemClickListener recyclerListViewItemClickListener, float f, float f2) {
        this.f$0 = recyclerListViewItemClickListener;
        this.f$1 = f;
        this.f$2 = f2;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$onInterceptTouchEvent$0(this.f$1, this.f$2);
    }
}