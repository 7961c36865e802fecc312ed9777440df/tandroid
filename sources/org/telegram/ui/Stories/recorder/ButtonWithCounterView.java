package org.telegram.ui.Stories.recorder;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.accessibility.AccessibilityNodeInfo;
import android.view.animation.OvershootInterpolator;
import android.widget.FrameLayout;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Components.AnimatedFloat;
import org.telegram.ui.Components.AnimatedTextView;
import org.telegram.ui.Components.CircularProgressDrawable;
import org.telegram.ui.Components.CubicBezierInterpolator;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.ui.Components.ScaleStateListAnimator;
/* loaded from: classes4.dex */
public class ButtonWithCounterView extends FrameLayout {
    private float countAlpha;
    private final AnimatedFloat countAlphaAnimated;
    private ValueAnimator countAnimator;
    private float countScale;
    private final AnimatedTextView.AnimatedTextDrawable countText;
    private boolean enabled;
    private ValueAnimator enabledAnimator;
    private float enabledT;
    private int globalAlpha;
    private int lastCount;
    private boolean loading;
    private ValueAnimator loadingAnimator;
    private CircularProgressDrawable loadingDrawable;
    private float loadingT;
    private final Paint paint;
    private Theme.ResourcesProvider resourcesProvider;
    private final View rippleView;
    private boolean showZero;
    private final AnimatedTextView.AnimatedTextDrawable text;

    @Override // android.view.ViewGroup
    protected boolean drawChild(Canvas canvas, View view, long j) {
        return false;
    }

    public ButtonWithCounterView(Context context, Theme.ResourcesProvider resourcesProvider) {
        this(context, true, resourcesProvider);
    }

    public ButtonWithCounterView(Context context, boolean z, Theme.ResourcesProvider resourcesProvider) {
        super(context);
        CubicBezierInterpolator cubicBezierInterpolator = CubicBezierInterpolator.EASE_OUT_QUINT;
        this.countAlphaAnimated = new AnimatedFloat(350L, cubicBezierInterpolator);
        this.loadingT = 0.0f;
        this.countScale = 1.0f;
        this.enabledT = 1.0f;
        this.enabled = true;
        this.globalAlpha = 255;
        this.resourcesProvider = resourcesProvider;
        ScaleStateListAnimator.apply(this, 0.02f, 1.2f);
        View view = new View(context);
        this.rippleView = view;
        view.setBackground(Theme.createRadSelectorDrawable(Theme.getColor(Theme.key_listSelector, resourcesProvider), 8, 8));
        addView(view, LayoutHelper.createFrame(-1, -1.0f));
        if (z) {
            setBackground(Theme.createRoundRectDrawable(AndroidUtilities.dp(8.0f), Theme.getColor(Theme.key_featuredStickers_addButton, resourcesProvider)));
        }
        Paint paint = new Paint(1);
        this.paint = paint;
        int i = Theme.key_featuredStickers_buttonText;
        paint.setColor(Theme.getColor(i, resourcesProvider));
        AnimatedTextView.AnimatedTextDrawable animatedTextDrawable = new AnimatedTextView.AnimatedTextDrawable(true, true, false);
        this.text = animatedTextDrawable;
        animatedTextDrawable.setAnimationProperties(0.3f, 0L, 250L, cubicBezierInterpolator);
        animatedTextDrawable.setCallback(this);
        animatedTextDrawable.setTextSize(AndroidUtilities.dp(14.0f));
        if (z) {
            animatedTextDrawable.setTypeface(AndroidUtilities.getTypeface(AndroidUtilities.TYPEFACE_ROBOTO_MEDIUM));
        }
        animatedTextDrawable.setTextColor(Theme.getColor(z ? i : Theme.key_featuredStickers_addButton, resourcesProvider));
        animatedTextDrawable.setGravity(1);
        AnimatedTextView.AnimatedTextDrawable animatedTextDrawable2 = new AnimatedTextView.AnimatedTextDrawable(false, false, true);
        this.countText = animatedTextDrawable2;
        animatedTextDrawable2.setAnimationProperties(0.3f, 0L, 250L, cubicBezierInterpolator);
        animatedTextDrawable2.setCallback(this);
        animatedTextDrawable2.setTextSize(AndroidUtilities.dp(12.0f));
        animatedTextDrawable2.setTypeface(AndroidUtilities.getTypeface(AndroidUtilities.TYPEFACE_ROBOTO_MEDIUM));
        animatedTextDrawable2.setTextColor(Theme.getColor(Theme.key_featuredStickers_addButton, resourcesProvider));
        animatedTextDrawable2.setText("");
        animatedTextDrawable2.setGravity(1);
        setWillNotDraw(false);
    }

    public void setText(CharSequence charSequence, boolean z) {
        if (z) {
            this.text.cancelAnimation();
        }
        this.text.setText(charSequence, z);
        setContentDescription(charSequence);
        invalidate();
    }

    public void setLoading(final boolean z) {
        if (this.loading != z) {
            ValueAnimator valueAnimator = this.loadingAnimator;
            if (valueAnimator != null) {
                valueAnimator.cancel();
                this.loadingAnimator = null;
            }
            float[] fArr = new float[2];
            fArr[0] = this.loadingT;
            this.loading = z;
            fArr[1] = z ? 1.0f : 0.0f;
            ValueAnimator ofFloat = ValueAnimator.ofFloat(fArr);
            this.loadingAnimator = ofFloat;
            ofFloat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: org.telegram.ui.Stories.recorder.ButtonWithCounterView$$ExternalSyntheticLambda2
                @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                public final void onAnimationUpdate(ValueAnimator valueAnimator2) {
                    ButtonWithCounterView.this.lambda$setLoading$0(valueAnimator2);
                }
            });
            this.loadingAnimator.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.Stories.recorder.ButtonWithCounterView.1
                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public void onAnimationEnd(Animator animator) {
                    ButtonWithCounterView.this.loadingT = z ? 1.0f : 0.0f;
                    ButtonWithCounterView.this.invalidate();
                }
            });
            this.loadingAnimator.setDuration(320L);
            this.loadingAnimator.setInterpolator(CubicBezierInterpolator.EASE_OUT_QUINT);
            this.loadingAnimator.start();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$setLoading$0(ValueAnimator valueAnimator) {
        this.loadingT = ((Float) valueAnimator.getAnimatedValue()).floatValue();
        invalidate();
    }

    public boolean isLoading() {
        return this.loading;
    }

    private void animateCount() {
        ValueAnimator valueAnimator = this.countAnimator;
        if (valueAnimator != null) {
            valueAnimator.cancel();
            this.countAnimator = null;
        }
        ValueAnimator ofFloat = ValueAnimator.ofFloat(0.0f, 1.0f);
        this.countAnimator = ofFloat;
        ofFloat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: org.telegram.ui.Stories.recorder.ButtonWithCounterView$$ExternalSyntheticLambda0
            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public final void onAnimationUpdate(ValueAnimator valueAnimator2) {
                ButtonWithCounterView.this.lambda$animateCount$1(valueAnimator2);
            }
        });
        this.countAnimator.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.Stories.recorder.ButtonWithCounterView.2
            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationEnd(Animator animator) {
                ButtonWithCounterView.this.countScale = 1.0f;
                ButtonWithCounterView.this.invalidate();
            }
        });
        this.countAnimator.setInterpolator(new OvershootInterpolator(2.0f));
        this.countAnimator.setDuration(200L);
        this.countAnimator.start();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$animateCount$1(ValueAnimator valueAnimator) {
        this.countScale = Math.max(1.0f, ((Float) valueAnimator.getAnimatedValue()).floatValue());
        invalidate();
    }

    public void setShowZero(boolean z) {
        this.showZero = z;
    }

    public void setCount(int i, boolean z) {
        int i2;
        if (z) {
            this.countText.cancelAnimation();
        }
        if (z && i != (i2 = this.lastCount) && i > 0 && i2 > 0) {
            animateCount();
        }
        this.lastCount = i;
        this.countAlpha = (i != 0 || this.showZero) ? 1.0f : 0.0f;
        AnimatedTextView.AnimatedTextDrawable animatedTextDrawable = this.countText;
        animatedTextDrawable.setText("" + i, z);
        invalidate();
    }

    @Override // android.view.View
    public void setEnabled(boolean z) {
        if (this.enabled != z) {
            ValueAnimator valueAnimator = this.enabledAnimator;
            if (valueAnimator != null) {
                valueAnimator.cancel();
                this.enabledAnimator = null;
            }
            float[] fArr = new float[2];
            fArr[0] = this.enabledT;
            this.enabled = z;
            fArr[1] = z ? 1.0f : 0.0f;
            ValueAnimator ofFloat = ValueAnimator.ofFloat(fArr);
            this.enabledAnimator = ofFloat;
            ofFloat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: org.telegram.ui.Stories.recorder.ButtonWithCounterView$$ExternalSyntheticLambda1
                @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                public final void onAnimationUpdate(ValueAnimator valueAnimator2) {
                    ButtonWithCounterView.this.lambda$setEnabled$2(valueAnimator2);
                }
            });
            this.enabledAnimator.start();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$setEnabled$2(ValueAnimator valueAnimator) {
        this.enabledT = ((Float) valueAnimator.getAnimatedValue()).floatValue();
        invalidate();
    }

    @Override // android.view.View
    protected boolean verifyDrawable(Drawable drawable) {
        return this.text == drawable || this.countText == drawable || super.verifyDrawable(drawable);
    }

    @Override // android.view.View
    protected void onDraw(Canvas canvas) {
        this.rippleView.draw(canvas);
        boolean z = false;
        if (this.loadingT > 0.0f) {
            if (this.loadingDrawable == null) {
                this.loadingDrawable = new CircularProgressDrawable(this.text.getTextColor());
            }
            int dp = (int) ((1.0f - this.loadingT) * AndroidUtilities.dp(24.0f));
            this.loadingDrawable.setBounds(0, dp, getWidth(), getHeight() + dp);
            this.loadingDrawable.setAlpha((int) (this.loadingT * 255.0f));
            this.loadingDrawable.draw(canvas);
            invalidate();
        }
        float f = this.loadingT;
        if (f < 1.0f) {
            if (f != 0.0f) {
                canvas.save();
                canvas.translate(0.0f, (int) (this.loadingT * AndroidUtilities.dp(-24.0f)));
                canvas.scale(1.0f, 1.0f - (this.loadingT * 0.4f));
                z = true;
            }
            float currentWidth = this.text.getCurrentWidth();
            float f2 = this.countAlphaAnimated.set(this.countAlpha);
            float dp2 = ((AndroidUtilities.dp(15.66f) + this.countText.getCurrentWidth()) * f2) + currentWidth;
            Rect rect = AndroidUtilities.rectTmp2;
            rect.set((int) (((getMeasuredWidth() - dp2) - getWidth()) / 2.0f), (int) (((getMeasuredHeight() - this.text.getHeight()) / 2.0f) - AndroidUtilities.dp(1.0f)), (int) ((((getMeasuredWidth() - dp2) + getWidth()) / 2.0f) + currentWidth), (int) (((getMeasuredHeight() + this.text.getHeight()) / 2.0f) - AndroidUtilities.dp(1.0f)));
            this.text.setAlpha((int) (this.globalAlpha * (1.0f - this.loadingT) * AndroidUtilities.lerp(0.5f, 1.0f, this.enabledT)));
            this.text.setBounds(rect);
            this.text.draw(canvas);
            rect.set((int) (((getMeasuredWidth() - dp2) / 2.0f) + currentWidth + AndroidUtilities.dp(5.0f)), (int) ((getMeasuredHeight() - AndroidUtilities.dp(18.0f)) / 2.0f), (int) (((getMeasuredWidth() - dp2) / 2.0f) + currentWidth + AndroidUtilities.dp(13.0f) + Math.max(AndroidUtilities.dp(9.0f), this.countText.getCurrentWidth())), (int) ((getMeasuredHeight() + AndroidUtilities.dp(18.0f)) / 2.0f));
            RectF rectF = AndroidUtilities.rectTmp;
            rectF.set(rect);
            if (this.countScale != 1.0f) {
                canvas.save();
                float f3 = this.countScale;
                canvas.scale(f3, f3, rect.centerX(), rect.centerY());
            }
            this.paint.setAlpha((int) (this.globalAlpha * (1.0f - this.loadingT) * f2 * f2 * AndroidUtilities.lerp(0.5f, 1.0f, this.enabledT)));
            canvas.drawRoundRect(rectF, AndroidUtilities.dp(10.0f), AndroidUtilities.dp(10.0f), this.paint);
            rect.offset(-AndroidUtilities.dp(0.3f), -AndroidUtilities.dp(0.4f));
            this.countText.setAlpha((int) (this.globalAlpha * (1.0f - this.loadingT) * f2));
            this.countText.setBounds(rect);
            this.countText.draw(canvas);
            if (this.countScale != 1.0f) {
                canvas.restore();
            }
            if (z) {
                canvas.restore();
            }
        }
    }

    @Override // android.view.View
    public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo accessibilityNodeInfo) {
        super.onInitializeAccessibilityNodeInfo(accessibilityNodeInfo);
        accessibilityNodeInfo.setClassName("android.widget.Button");
    }

    public void setTextAlpha(float f) {
        this.text.setAlpha((int) (f * 255.0f));
    }

    public void setGlobalAlpha(float f) {
        this.globalAlpha = (int) (f * 255.0f);
    }
}