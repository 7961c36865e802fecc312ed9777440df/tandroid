package org.telegram.ui.Components.Crop;

import android.content.DialogInterface;
/* loaded from: classes3.dex */
public final /* synthetic */ class CropView$$ExternalSyntheticLambda3 implements DialogInterface.OnCancelListener {
    public final /* synthetic */ CropView f$0;

    public /* synthetic */ CropView$$ExternalSyntheticLambda3(CropView cropView) {
        this.f$0 = cropView;
    }

    @Override // android.content.DialogInterface.OnCancelListener
    public final void onCancel(DialogInterface dialogInterface) {
        this.f$0.lambda$showAspectRatioDialog$4(dialogInterface);
    }
}