package org.telegram.ui.Components;

import android.view.View;
import org.telegram.ui.ActionBar.Theme;
/* loaded from: classes3.dex */
public final /* synthetic */ class ChatAvatarContainer$$ExternalSyntheticLambda2 implements View.OnClickListener {
    public final /* synthetic */ ChatAvatarContainer f$0;
    public final /* synthetic */ Theme.ResourcesProvider f$1;

    public /* synthetic */ ChatAvatarContainer$$ExternalSyntheticLambda2(ChatAvatarContainer chatAvatarContainer, Theme.ResourcesProvider resourcesProvider) {
        this.f$0 = chatAvatarContainer;
        this.f$1 = resourcesProvider;
    }

    @Override // android.view.View.OnClickListener
    public final void onClick(View view) {
        this.f$0.lambda$new$1(this.f$1, view);
    }
}