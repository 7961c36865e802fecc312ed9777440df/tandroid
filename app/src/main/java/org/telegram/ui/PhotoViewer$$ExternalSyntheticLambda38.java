package org.telegram.ui;

import android.view.View;
/* loaded from: classes3.dex */
public final /* synthetic */ class PhotoViewer$$ExternalSyntheticLambda38 implements View.OnClickListener {
    public final /* synthetic */ PhotoViewer f$0;
    public final /* synthetic */ int f$1;

    public /* synthetic */ PhotoViewer$$ExternalSyntheticLambda38(PhotoViewer photoViewer, int i) {
        this.f$0 = photoViewer;
        this.f$1 = i;
    }

    @Override // android.view.View.OnClickListener
    public final void onClick(View view) {
        this.f$0.lambda$setParentActivity$14(this.f$1, view);
    }
}