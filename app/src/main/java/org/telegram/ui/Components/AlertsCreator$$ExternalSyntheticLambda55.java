package org.telegram.ui.Components;

import android.view.View;
import org.telegram.ui.ActionBar.BottomSheet;
import org.telegram.ui.Components.AlertsCreator;
/* loaded from: classes3.dex */
public final /* synthetic */ class AlertsCreator$$ExternalSyntheticLambda55 implements View.OnClickListener {
    public final /* synthetic */ NumberPicker f$0;
    public final /* synthetic */ NumberPicker f$1;
    public final /* synthetic */ AlertsCreator.SoundFrequencyDelegate f$2;
    public final /* synthetic */ BottomSheet.Builder f$3;

    public /* synthetic */ AlertsCreator$$ExternalSyntheticLambda55(NumberPicker numberPicker, NumberPicker numberPicker2, AlertsCreator.SoundFrequencyDelegate soundFrequencyDelegate, BottomSheet.Builder builder) {
        this.f$0 = numberPicker;
        this.f$1 = numberPicker2;
        this.f$2 = soundFrequencyDelegate;
        this.f$3 = builder;
    }

    @Override // android.view.View.OnClickListener
    public final void onClick(View view) {
        AlertsCreator.lambda$createSoundFrequencyPickerDialog$71(this.f$0, this.f$1, this.f$2, this.f$3, view);
    }
}