package org.telegram.ui;

import android.content.DialogInterface;
import org.telegram.ui.AvatarPreviewer;
/* loaded from: classes3.dex */
public final /* synthetic */ class AvatarPreviewer$Layout$$ExternalSyntheticLambda3 implements DialogInterface.OnClickListener {
    public final /* synthetic */ AvatarPreviewer.Layout f$0;

    public /* synthetic */ AvatarPreviewer$Layout$$ExternalSyntheticLambda3(AvatarPreviewer.Layout layout) {
        this.f$0 = layout;
    }

    @Override // android.content.DialogInterface.OnClickListener
    public final void onClick(DialogInterface dialogInterface, int i) {
        this.f$0.lambda$showBottomSheet$1(dialogInterface, i);
    }
}