package org.telegram.ui.Components;

import android.view.View;
import org.telegram.ui.ChatActivity;
import org.telegram.ui.Components.RecyclerListView;
/* loaded from: classes3.dex */
public final /* synthetic */ class PollVotesAlert$$ExternalSyntheticLambda6 implements RecyclerListView.OnItemClickListener {
    public final /* synthetic */ PollVotesAlert f$0;
    public final /* synthetic */ ChatActivity f$1;

    public /* synthetic */ PollVotesAlert$$ExternalSyntheticLambda6(PollVotesAlert pollVotesAlert, ChatActivity chatActivity) {
        this.f$0 = pollVotesAlert;
        this.f$1 = chatActivity;
    }

    @Override // org.telegram.ui.Components.RecyclerListView.OnItemClickListener
    public final void onItemClick(View view, int i) {
        this.f$0.lambda$new$4(this.f$1, view, i);
    }
}