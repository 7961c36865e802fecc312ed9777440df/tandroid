package org.telegram.ui.Components;

import android.view.View;
import org.telegram.ui.ActionBar.BottomSheet;
/* loaded from: classes3.dex */
public final /* synthetic */ class AlertsCreator$$ExternalSyntheticLambda54 implements View.OnClickListener {
    public final /* synthetic */ BottomSheet f$0;

    public /* synthetic */ AlertsCreator$$ExternalSyntheticLambda54(BottomSheet bottomSheet) {
        this.f$0 = bottomSheet;
    }

    @Override // android.view.View.OnClickListener
    public final void onClick(View view) {
        this.f$0.dismiss();
    }
}