package org.telegram.ui;

import org.telegram.ui.DialogsActivity;
/* loaded from: classes3.dex */
public final /* synthetic */ class DialogsActivity$$ExternalSyntheticLambda45 implements Runnable {
    public final /* synthetic */ DialogsActivity f$0;
    public final /* synthetic */ DialogsActivity.ViewPage f$1;
    public final /* synthetic */ Object[] f$2;

    public /* synthetic */ DialogsActivity$$ExternalSyntheticLambda45(DialogsActivity dialogsActivity, DialogsActivity.ViewPage viewPage, Object[] objArr) {
        this.f$0 = dialogsActivity;
        this.f$1 = viewPage;
        this.f$2 = objArr;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$didReceivedNotification$44(this.f$1, this.f$2);
    }
}