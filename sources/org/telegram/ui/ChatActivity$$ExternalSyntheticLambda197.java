package org.telegram.ui;

import androidx.core.util.Consumer;
import java.util.List;
import org.telegram.ui.Components.ReactedUsersListView;
/* compiled from: R8$$SyntheticClass */
/* loaded from: classes4.dex */
public final /* synthetic */ class ChatActivity$$ExternalSyntheticLambda197 implements Consumer {
    public final /* synthetic */ ReactedUsersListView f$0;

    public /* synthetic */ ChatActivity$$ExternalSyntheticLambda197(ReactedUsersListView reactedUsersListView) {
        this.f$0 = reactedUsersListView;
    }

    @Override // androidx.core.util.Consumer
    public final void accept(Object obj) {
        this.f$0.setSeenUsers((List) obj);
    }
}