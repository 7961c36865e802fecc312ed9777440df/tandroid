package org.telegram.ui;

import org.telegram.ui.ActionBar.BaseFragment;
/* loaded from: classes3.dex */
public final /* synthetic */ class LaunchActivity$$ExternalSyntheticLambda60 implements Runnable {
    public final /* synthetic */ LaunchActivity f$0;
    public final /* synthetic */ BaseFragment f$1;
    public final /* synthetic */ boolean f$2;

    public /* synthetic */ LaunchActivity$$ExternalSyntheticLambda60(LaunchActivity launchActivity, BaseFragment baseFragment, boolean z) {
        this.f$0 = launchActivity;
        this.f$1 = baseFragment;
        this.f$2 = z;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$handleIntent$16(this.f$1, this.f$2);
    }
}