package org.telegram.ui;

import org.telegram.ui.ThemePreviewActivity;
/* loaded from: classes3.dex */
public final /* synthetic */ class WallpapersListActivity$$ExternalSyntheticLambda8 implements ThemePreviewActivity.WallpaperActivityDelegate {
    public final /* synthetic */ WallpapersListActivity f$0;

    public /* synthetic */ WallpapersListActivity$$ExternalSyntheticLambda8(WallpapersListActivity wallpapersListActivity) {
        this.f$0 = wallpapersListActivity;
    }

    @Override // org.telegram.ui.ThemePreviewActivity.WallpaperActivityDelegate
    public final void didSetNewBackground() {
        this.f$0.removeSelfFromStack();
    }
}