package org.telegram.ui;

import java.util.Calendar;
import org.telegram.ui.Components.NumberPicker;
/* loaded from: classes3.dex */
public final /* synthetic */ class GroupCallActivity$$ExternalSyntheticLambda57 implements NumberPicker.Formatter {
    public final /* synthetic */ long f$0;
    public final /* synthetic */ Calendar f$1;
    public final /* synthetic */ int f$2;

    public /* synthetic */ GroupCallActivity$$ExternalSyntheticLambda57(long j, Calendar calendar, int i) {
        this.f$0 = j;
        this.f$1 = calendar;
        this.f$2 = i;
    }

    @Override // org.telegram.ui.Components.NumberPicker.Formatter
    public final String format(int i) {
        String lambda$new$30;
        lambda$new$30 = GroupCallActivity.lambda$new$30(this.f$0, this.f$1, this.f$2, i);
        return lambda$new$30;
    }
}