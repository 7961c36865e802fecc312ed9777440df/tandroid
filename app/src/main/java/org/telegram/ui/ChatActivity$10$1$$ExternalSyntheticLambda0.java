package org.telegram.ui;

import org.telegram.messenger.MessagesStorage;
import org.telegram.ui.ChatActivity;
/* loaded from: classes3.dex */
public final /* synthetic */ class ChatActivity$10$1$$ExternalSyntheticLambda0 implements MessagesStorage.BooleanCallback {
    public final /* synthetic */ ChatActivity.AnonymousClass10.AnonymousClass1 f$0;
    public final /* synthetic */ boolean f$1;

    public /* synthetic */ ChatActivity$10$1$$ExternalSyntheticLambda0(ChatActivity.AnonymousClass10.AnonymousClass1 anonymousClass1, boolean z) {
        this.f$0 = anonymousClass1;
        this.f$1 = z;
    }

    @Override // org.telegram.messenger.MessagesStorage.BooleanCallback
    public final void run(boolean z) {
        this.f$0.lambda$run$0(this.f$1, z);
    }
}