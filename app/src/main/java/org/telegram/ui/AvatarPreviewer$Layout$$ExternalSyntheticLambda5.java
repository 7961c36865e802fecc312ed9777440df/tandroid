package org.telegram.ui;

import androidx.core.util.Consumer;
import org.telegram.ui.AvatarPreviewer;
/* loaded from: classes3.dex */
public final /* synthetic */ class AvatarPreviewer$Layout$$ExternalSyntheticLambda5 implements Consumer {
    public final /* synthetic */ AvatarPreviewer.Layout f$0;
    public final /* synthetic */ AvatarPreviewer.Data f$1;

    public /* synthetic */ AvatarPreviewer$Layout$$ExternalSyntheticLambda5(AvatarPreviewer.Layout layout, AvatarPreviewer.Data data) {
        this.f$0 = layout;
        this.f$1 = data;
    }

    @Override // androidx.core.util.Consumer
    public final void accept(Object obj) {
        this.f$0.lambda$setData$5(this.f$1, obj);
    }
}