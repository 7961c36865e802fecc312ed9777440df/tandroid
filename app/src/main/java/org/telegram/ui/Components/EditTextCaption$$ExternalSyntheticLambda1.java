package org.telegram.ui.Components;

import android.content.DialogInterface;
/* loaded from: classes3.dex */
public final /* synthetic */ class EditTextCaption$$ExternalSyntheticLambda1 implements DialogInterface.OnShowListener {
    public final /* synthetic */ EditTextBoldCursor f$0;

    public /* synthetic */ EditTextCaption$$ExternalSyntheticLambda1(EditTextBoldCursor editTextBoldCursor) {
        this.f$0 = editTextBoldCursor;
    }

    @Override // android.content.DialogInterface.OnShowListener
    public final void onShow(DialogInterface dialogInterface) {
        EditTextCaption.lambda$makeSelectedUrl$1(this.f$0, dialogInterface);
    }
}