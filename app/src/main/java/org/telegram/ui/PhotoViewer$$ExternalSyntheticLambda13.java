package org.telegram.ui;

import android.view.View;
import org.telegram.ui.ActionBar.BottomSheet;
/* loaded from: classes3.dex */
public final /* synthetic */ class PhotoViewer$$ExternalSyntheticLambda13 implements View.OnClickListener {
    public final /* synthetic */ BottomSheet f$0;

    public /* synthetic */ PhotoViewer$$ExternalSyntheticLambda13(BottomSheet bottomSheet) {
        this.f$0 = bottomSheet;
    }

    @Override // android.view.View.OnClickListener
    public final void onClick(View view) {
        this.f$0.dismiss();
    }
}