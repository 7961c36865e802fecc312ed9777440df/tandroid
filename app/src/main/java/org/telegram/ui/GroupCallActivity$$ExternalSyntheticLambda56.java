package org.telegram.ui;

import android.view.KeyEvent;
import org.telegram.ui.ActionBar.ActionBarPopupWindow;
/* loaded from: classes3.dex */
public final /* synthetic */ class GroupCallActivity$$ExternalSyntheticLambda56 implements ActionBarPopupWindow.OnDispatchKeyEventListener {
    public final /* synthetic */ GroupCallActivity f$0;

    public /* synthetic */ GroupCallActivity$$ExternalSyntheticLambda56(GroupCallActivity groupCallActivity) {
        this.f$0 = groupCallActivity;
    }

    @Override // org.telegram.ui.ActionBar.ActionBarPopupWindow.OnDispatchKeyEventListener
    public final void onDispatchKeyEvent(KeyEvent keyEvent) {
        this.f$0.lambda$showMenuForCell$59(keyEvent);
    }
}