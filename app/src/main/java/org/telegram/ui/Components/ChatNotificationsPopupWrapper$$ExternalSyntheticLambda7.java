package org.telegram.ui.Components;

import org.telegram.ui.Components.ChatNotificationsPopupWrapper;
/* loaded from: classes3.dex */
public final /* synthetic */ class ChatNotificationsPopupWrapper$$ExternalSyntheticLambda7 implements Runnable {
    public final /* synthetic */ int f$0;
    public final /* synthetic */ int f$1;
    public final /* synthetic */ ChatNotificationsPopupWrapper.Callback f$2;

    public /* synthetic */ ChatNotificationsPopupWrapper$$ExternalSyntheticLambda7(int i, int i2, ChatNotificationsPopupWrapper.Callback callback) {
        this.f$0 = i;
        this.f$1 = i2;
        this.f$2 = callback;
    }

    @Override // java.lang.Runnable
    public final void run() {
        ChatNotificationsPopupWrapper.lambda$new$4(this.f$0, this.f$1, this.f$2);
    }
}