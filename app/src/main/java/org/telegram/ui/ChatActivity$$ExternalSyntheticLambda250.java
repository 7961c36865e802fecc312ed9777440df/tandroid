package org.telegram.ui;

import android.view.View;
import org.telegram.ui.Components.RecyclerListView;
/* loaded from: classes3.dex */
public final /* synthetic */ class ChatActivity$$ExternalSyntheticLambda250 implements RecyclerListView.OnItemClickListener {
    public final /* synthetic */ ChatActivity f$0;
    public final /* synthetic */ MessageSeenView f$1;

    public /* synthetic */ ChatActivity$$ExternalSyntheticLambda250(ChatActivity chatActivity, MessageSeenView messageSeenView) {
        this.f$0 = chatActivity;
        this.f$1 = messageSeenView;
    }

    @Override // org.telegram.ui.Components.RecyclerListView.OnItemClickListener
    public final void onItemClick(View view, int i) {
        this.f$0.lambda$createMenu$161(this.f$1, view, i);
    }
}