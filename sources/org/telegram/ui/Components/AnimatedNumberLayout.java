package org.telegram.ui.Components;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.graphics.Canvas;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.Property;
import android.view.View;
import java.util.ArrayList;
import java.util.Locale;
import org.telegram.ui.Components.AnimationProperties;

/* loaded from: classes3.dex */
public class AnimatedNumberLayout {
    public static final Property PROGRESS = new AnimationProperties.FloatProperty("progress") { // from class: org.telegram.ui.Components.AnimatedNumberLayout.1
        @Override // android.util.Property
        public Float get(AnimatedNumberLayout animatedNumberLayout) {
            return Float.valueOf(animatedNumberLayout.progress);
        }

        @Override // org.telegram.ui.Components.AnimationProperties.FloatProperty
        public void setValue(AnimatedNumberLayout animatedNumberLayout, float f) {
            animatedNumberLayout.setProgress(f);
        }
    };
    private ObjectAnimator animator;
    private final View parentView;
    private final TextPaint textPaint;
    private ArrayList letters = new ArrayList();
    private ArrayList oldLetters = new ArrayList();
    private float progress = 0.0f;
    private int currentNumber = 1;

    public AnimatedNumberLayout(View view, TextPaint textPaint) {
        this.textPaint = textPaint;
        this.parentView = view;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setProgress(float f) {
        if (this.progress == f) {
            return;
        }
        this.progress = f;
        this.parentView.invalidate();
    }

    /* JADX WARN: Code restructure failed: missing block: B:39:0x00c6, code lost:
    
        if (r6 != null) goto L31;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void draw(Canvas canvas) {
        TextPaint textPaint;
        int i;
        if (this.letters.isEmpty()) {
            return;
        }
        float height = ((StaticLayout) this.letters.get(0)).getHeight();
        int max = Math.max(this.letters.size(), this.oldLetters.size());
        canvas.save();
        int alpha = this.textPaint.getAlpha();
        int i2 = 0;
        while (i2 < max) {
            canvas.save();
            StaticLayout staticLayout = i2 < this.oldLetters.size() ? (StaticLayout) this.oldLetters.get(i2) : null;
            StaticLayout staticLayout2 = i2 < this.letters.size() ? (StaticLayout) this.letters.get(i2) : null;
            float f = this.progress;
            if (f > 0.0f) {
                if (staticLayout != null) {
                    float f2 = alpha;
                    this.textPaint.setAlpha((int) (f * f2));
                    canvas.save();
                    canvas.translate(0.0f, (this.progress - 1.0f) * height);
                    staticLayout.draw(canvas);
                    canvas.restore();
                    if (staticLayout2 != null) {
                        textPaint = this.textPaint;
                        i = (int) (f2 * (1.0f - this.progress));
                        textPaint.setAlpha(i);
                        canvas.translate(0.0f, this.progress * height);
                    }
                }
                this.textPaint.setAlpha(alpha);
            } else if (f < 0.0f) {
                if (staticLayout != null) {
                    this.textPaint.setAlpha((int) (alpha * (-f)));
                    canvas.save();
                    canvas.translate(0.0f, (this.progress + 1.0f) * height);
                    staticLayout.draw(canvas);
                    canvas.restore();
                }
                if (staticLayout2 != null) {
                    if (i2 == max - 1 || staticLayout != null) {
                        textPaint = this.textPaint;
                        i = (int) (alpha * (this.progress + 1.0f));
                        textPaint.setAlpha(i);
                        canvas.translate(0.0f, this.progress * height);
                    }
                    this.textPaint.setAlpha(alpha);
                }
            }
            if (staticLayout2 != null) {
                staticLayout2.draw(canvas);
            }
            canvas.restore();
            canvas.translate(staticLayout2 != null ? staticLayout2.getLineWidth(0) : staticLayout.getLineWidth(0), 0.0f);
            i2++;
        }
        canvas.restore();
    }

    public int getWidth() {
        int size = this.letters.size();
        float f = 0.0f;
        for (int i = 0; i < size; i++) {
            f += ((StaticLayout) this.letters.get(i)).getLineWidth(0);
        }
        return (int) Math.ceil(f);
    }

    public void setNumber(int i, boolean z) {
        if (this.currentNumber != i || this.letters.isEmpty()) {
            ObjectAnimator objectAnimator = this.animator;
            if (objectAnimator != null) {
                objectAnimator.cancel();
                this.animator = null;
            }
            this.oldLetters.clear();
            this.oldLetters.addAll(this.letters);
            this.letters.clear();
            Locale locale = Locale.US;
            String format = String.format(locale, "%d", Integer.valueOf(this.currentNumber));
            String format2 = String.format(locale, "%d", Integer.valueOf(i));
            boolean z2 = i > this.currentNumber;
            this.currentNumber = i;
            this.progress = 0.0f;
            int i2 = 0;
            while (i2 < format2.length()) {
                int i3 = i2 + 1;
                String substring = format2.substring(i2, i3);
                String substring2 = (this.oldLetters.isEmpty() || i2 >= format.length()) ? null : format.substring(i2, i3);
                if (substring2 == null || !substring2.equals(substring)) {
                    this.letters.add(new StaticLayout(substring, this.textPaint, (int) Math.ceil(r12.measureText(substring)), Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false));
                } else {
                    this.letters.add((StaticLayout) this.oldLetters.get(i2));
                    this.oldLetters.set(i2, null);
                }
                i2 = i3;
            }
            if (z && !this.oldLetters.isEmpty()) {
                ObjectAnimator ofFloat = ObjectAnimator.ofFloat(this, (Property<AnimatedNumberLayout, Float>) PROGRESS, z2 ? -1.0f : 1.0f, 0.0f);
                this.animator = ofFloat;
                ofFloat.setDuration(150L);
                this.animator.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.Components.AnimatedNumberLayout.2
                    @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                    public void onAnimationEnd(Animator animator) {
                        AnimatedNumberLayout.this.animator = null;
                        AnimatedNumberLayout.this.oldLetters.clear();
                    }
                });
                this.animator.start();
            }
            this.parentView.invalidate();
        }
    }
}
