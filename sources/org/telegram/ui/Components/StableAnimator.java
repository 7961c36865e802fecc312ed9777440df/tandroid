package org.telegram.ui.Components;

import android.animation.TimeAnimator;
import android.animation.ValueAnimator;
import org.telegram.messenger.AndroidUtilities;

/* loaded from: classes3.dex */
public class StableAnimator extends TimeAnimator {
    private Object animatedValue;
    private float[] floatValues;
    private int times = 0;
    private int totalTimes = 0;
    private ValueAnimator.AnimatorUpdateListener updateListener;

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$start$0(TimeAnimator timeAnimator, long j, long j2) {
        int i;
        int i2 = this.times;
        if (i2 > 0 && (i = this.totalTimes) > 0) {
            int i3 = i2 - 1;
            this.times = i3;
            if (this.updateListener == null) {
                return;
            }
            float[] fArr = this.floatValues;
            if (fArr != null && fArr.length == 2) {
                float interpolation = getInterpolator().getInterpolation(1.0f - (i3 / i));
                float[] fArr2 = this.floatValues;
                float f = fArr2[0];
                this.animatedValue = Float.valueOf(f + ((fArr2[1] - f) * interpolation));
                this.updateListener.onAnimationUpdate(this);
                return;
            }
        }
        end();
    }

    public static StableAnimator ofFloat(float... fArr) {
        StableAnimator stableAnimator = new StableAnimator();
        stableAnimator.setFloatValues(fArr);
        return stableAnimator;
    }

    @Override // android.animation.ValueAnimator
    public void addUpdateListener(ValueAnimator.AnimatorUpdateListener animatorUpdateListener) {
        this.updateListener = animatorUpdateListener;
    }

    @Override // android.animation.ValueAnimator, android.animation.Animator
    public void end() {
        this.updateListener = null;
        super.end();
    }

    @Override // android.animation.ValueAnimator
    public Object getAnimatedValue() {
        return this.animatedValue;
    }

    @Override // android.animation.ValueAnimator
    public void setFloatValues(float[] fArr) {
        super.setFloatValues(fArr);
        this.floatValues = fArr;
    }

    @Override // android.animation.TimeAnimator, android.animation.ValueAnimator, android.animation.Animator
    public void start() {
        setTimeListener(new TimeAnimator.TimeListener() { // from class: org.telegram.ui.Components.StableAnimator$$ExternalSyntheticLambda0
            @Override // android.animation.TimeAnimator.TimeListener
            public final void onTimeUpdate(TimeAnimator timeAnimator, long j, long j2) {
                StableAnimator.this.lambda$start$0(timeAnimator, j, j2);
            }
        });
        int duration = (int) (getDuration() / AndroidUtilities.screenRefreshTime);
        this.times = duration;
        this.totalTimes = duration;
        super.start();
    }
}
