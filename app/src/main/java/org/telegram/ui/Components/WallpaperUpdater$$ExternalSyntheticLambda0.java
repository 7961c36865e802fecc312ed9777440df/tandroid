package org.telegram.ui.Components;

import android.content.DialogInterface;
/* loaded from: classes3.dex */
public final /* synthetic */ class WallpaperUpdater$$ExternalSyntheticLambda0 implements DialogInterface.OnClickListener {
    public final /* synthetic */ WallpaperUpdater f$0;
    public final /* synthetic */ boolean f$1;

    public /* synthetic */ WallpaperUpdater$$ExternalSyntheticLambda0(WallpaperUpdater wallpaperUpdater, boolean z) {
        this.f$0 = wallpaperUpdater;
        this.f$1 = z;
    }

    @Override // android.content.DialogInterface.OnClickListener
    public final void onClick(DialogInterface dialogInterface, int i) {
        this.f$0.lambda$showAlert$0(this.f$1, dialogInterface, i);
    }
}