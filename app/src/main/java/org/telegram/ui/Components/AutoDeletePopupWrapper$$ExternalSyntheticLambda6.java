package org.telegram.ui.Components;
/* loaded from: classes3.dex */
public final /* synthetic */ class AutoDeletePopupWrapper$$ExternalSyntheticLambda6 implements Runnable {
    public final /* synthetic */ AutoDeletePopupWrapper f$0;
    public final /* synthetic */ int f$1;

    public /* synthetic */ AutoDeletePopupWrapper$$ExternalSyntheticLambda6(AutoDeletePopupWrapper autoDeletePopupWrapper, int i) {
        this.f$0 = autoDeletePopupWrapper;
        this.f$1 = i;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$updateItems$7(this.f$1);
    }
}