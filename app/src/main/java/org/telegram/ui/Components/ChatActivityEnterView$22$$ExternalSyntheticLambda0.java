package org.telegram.ui.Components;

import org.telegram.ui.Components.ChatActivityEnterView;
import org.telegram.ui.Components.SenderSelectPopup;
/* loaded from: classes3.dex */
public final /* synthetic */ class ChatActivityEnterView$22$$ExternalSyntheticLambda0 implements Runnable {
    public final /* synthetic */ ChatActivityEnterView.AnonymousClass22 f$0;
    public final /* synthetic */ SimpleAvatarView f$1;
    public final /* synthetic */ SenderSelectPopup.SenderView f$2;

    public /* synthetic */ ChatActivityEnterView$22$$ExternalSyntheticLambda0(ChatActivityEnterView.AnonymousClass22 anonymousClass22, SimpleAvatarView simpleAvatarView, SenderSelectPopup.SenderView senderView) {
        this.f$0 = anonymousClass22;
        this.f$1 = simpleAvatarView;
        this.f$2 = senderView;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$onDraw$0(this.f$1, this.f$2);
    }
}