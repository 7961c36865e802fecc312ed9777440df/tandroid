package org.telegram.ui.Components.voip;

import android.content.Context;
import android.graphics.Canvas;
import android.view.View;
import org.telegram.messenger.LiteMode;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Components.SnowflakesEffect;
/* loaded from: classes3.dex */
public class VoIpSnowView extends View {
    private boolean isPaused;
    private SnowflakesEffect snowflakesEffect;

    public VoIpSnowView(Context context) {
        super(context);
        if (LiteMode.isEnabled(LiteMode.FLAG_CALLS_ANIMATIONS) && Theme.getEventType() == 0) {
            this.snowflakesEffect = new SnowflakesEffect(0);
        }
    }

    @Override // android.view.View
    protected void onDraw(Canvas canvas) {
        SnowflakesEffect snowflakesEffect;
        if (this.isPaused || (snowflakesEffect = this.snowflakesEffect) == null) {
            return;
        }
        snowflakesEffect.onDraw(this, canvas);
    }

    public void setState(boolean z) {
        this.isPaused = z;
        invalidate();
    }
}