package org.telegram.ui.ActionBar;

import android.view.View;
import android.widget.ImageButton;
import org.telegram.ui.ActionBar.FloatingToolbar;
/* loaded from: classes3.dex */
public final /* synthetic */ class FloatingToolbar$FloatingToolbarPopup$$ExternalSyntheticLambda0 implements View.OnClickListener {
    public final /* synthetic */ FloatingToolbar.FloatingToolbarPopup f$0;
    public final /* synthetic */ ImageButton f$1;

    public /* synthetic */ FloatingToolbar$FloatingToolbarPopup$$ExternalSyntheticLambda0(FloatingToolbar.FloatingToolbarPopup floatingToolbarPopup, ImageButton imageButton) {
        this.f$0 = floatingToolbarPopup;
        this.f$1 = imageButton;
    }

    @Override // android.view.View.OnClickListener
    public final void onClick(View view) {
        this.f$0.lambda$createOverflowButton$0(this.f$1, view);
    }
}