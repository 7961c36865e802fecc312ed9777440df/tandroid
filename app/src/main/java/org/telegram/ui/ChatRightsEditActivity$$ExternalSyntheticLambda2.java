package org.telegram.ui;

import android.app.DatePickerDialog;
import android.widget.DatePicker;
/* loaded from: classes3.dex */
public final /* synthetic */ class ChatRightsEditActivity$$ExternalSyntheticLambda2 implements DatePickerDialog.OnDateSetListener {
    public final /* synthetic */ ChatRightsEditActivity f$0;

    public /* synthetic */ ChatRightsEditActivity$$ExternalSyntheticLambda2(ChatRightsEditActivity chatRightsEditActivity) {
        this.f$0 = chatRightsEditActivity;
    }

    @Override // android.app.DatePickerDialog.OnDateSetListener
    public final void onDateSet(DatePicker datePicker, int i, int i2, int i3) {
        this.f$0.lambda$createView$2(datePicker, i, i2, i3);
    }
}