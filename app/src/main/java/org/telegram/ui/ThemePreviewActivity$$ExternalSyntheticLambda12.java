package org.telegram.ui;

import android.view.View;
import org.telegram.ui.Components.WallpaperCheckBoxView;
/* loaded from: classes3.dex */
public final /* synthetic */ class ThemePreviewActivity$$ExternalSyntheticLambda12 implements View.OnClickListener {
    public final /* synthetic */ ThemePreviewActivity f$0;
    public final /* synthetic */ int f$1;
    public final /* synthetic */ WallpaperCheckBoxView f$2;

    public /* synthetic */ ThemePreviewActivity$$ExternalSyntheticLambda12(ThemePreviewActivity themePreviewActivity, int i, WallpaperCheckBoxView wallpaperCheckBoxView) {
        this.f$0 = themePreviewActivity;
        this.f$1 = i;
        this.f$2 = wallpaperCheckBoxView;
    }

    @Override // android.view.View.OnClickListener
    public final void onClick(View view) {
        this.f$0.lambda$createView$8(this.f$1, this.f$2, view);
    }
}