package org.telegram.ui.Components;

import android.view.View;
import org.telegram.ui.ActionBar.ActionBarMenuItem;
import org.telegram.ui.Components.AlertsCreator;
/* loaded from: classes3.dex */
public final /* synthetic */ class AlertsCreator$$ExternalSyntheticLambda51 implements View.OnClickListener {
    public final /* synthetic */ ActionBarMenuItem f$0;
    public final /* synthetic */ AlertsCreator.ScheduleDatePickerColors f$1;

    public /* synthetic */ AlertsCreator$$ExternalSyntheticLambda51(ActionBarMenuItem actionBarMenuItem, AlertsCreator.ScheduleDatePickerColors scheduleDatePickerColors) {
        this.f$0 = actionBarMenuItem;
        this.f$1 = scheduleDatePickerColors;
    }

    @Override // android.view.View.OnClickListener
    public final void onClick(View view) {
        AlertsCreator.lambda$createScheduleDatePickerDialog$48(this.f$0, this.f$1, view);
    }
}