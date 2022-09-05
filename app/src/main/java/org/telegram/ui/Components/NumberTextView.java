package org.telegram.ui.Components;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Typeface;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.view.View;
import androidx.annotation.Keep;
import java.util.ArrayList;
import java.util.Locale;
import org.telegram.messenger.AndroidUtilities;
/* loaded from: classes3.dex */
public class NumberTextView extends View {
    private boolean addNumber;
    private ObjectAnimator animator;
    private boolean center;
    private float oldTextWidth;
    private OnTextWidthProgressChangedListener onTextWidthProgressChangedListener;
    private float textWidth;
    private ArrayList<StaticLayout> letters = new ArrayList<>();
    private ArrayList<StaticLayout> oldLetters = new ArrayList<>();
    private TextPaint textPaint = new TextPaint(1);
    private float progress = 0.0f;
    private int currentNumber = 1;

    /* loaded from: classes3.dex */
    public interface OnTextWidthProgressChangedListener {
        void onTextWidthProgress(float f, float f2, float f3);
    }

    public NumberTextView(Context context) {
        super(context);
    }

    public void setOnTextWidthProgressChangedListener(OnTextWidthProgressChangedListener onTextWidthProgressChangedListener) {
        this.onTextWidthProgressChangedListener = onTextWidthProgressChangedListener;
    }

    @Keep
    public void setProgress(float f) {
        if (this.progress == f) {
            return;
        }
        this.progress = f;
        OnTextWidthProgressChangedListener onTextWidthProgressChangedListener = this.onTextWidthProgressChangedListener;
        if (onTextWidthProgressChangedListener != null) {
            onTextWidthProgressChangedListener.onTextWidthProgress(this.oldTextWidth, this.textWidth, f);
        }
        invalidate();
    }

    @Keep
    public float getProgress() {
        return this.progress;
    }

    public void setAddNumber() {
        this.addNumber = true;
    }

    /* JADX WARN: Code restructure failed: missing block: B:12:0x004c, code lost:
        if (r22 < r21.currentNumber) goto L13;
     */
    /* JADX WARN: Code restructure failed: missing block: B:13:0x004e, code lost:
        r7 = true;
     */
    /* JADX WARN: Code restructure failed: missing block: B:59:0x0050, code lost:
        r7 = false;
     */
    /* JADX WARN: Code restructure failed: missing block: B:61:0x0072, code lost:
        if (r22 > r21.currentNumber) goto L13;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void setNumber(int i, boolean z) {
        String format;
        String format2;
        boolean z2;
        if (this.currentNumber != i || !z) {
            ObjectAnimator objectAnimator = this.animator;
            if (objectAnimator != null) {
                objectAnimator.cancel();
                this.animator = null;
            }
            this.oldLetters.clear();
            this.oldLetters.addAll(this.letters);
            this.letters.clear();
            if (this.addNumber) {
                Locale locale = Locale.US;
                format = String.format(locale, "#%d", Integer.valueOf(this.currentNumber));
                format2 = String.format(locale, "#%d", Integer.valueOf(i));
            } else {
                Locale locale2 = Locale.US;
                format = String.format(locale2, "%d", Integer.valueOf(this.currentNumber));
                format2 = String.format(locale2, "%d", Integer.valueOf(i));
            }
            this.textWidth = this.textPaint.measureText(format2);
            float measureText = this.textPaint.measureText(format);
            this.oldTextWidth = measureText;
            boolean z3 = this.center && this.textWidth != measureText;
            this.currentNumber = i;
            this.progress = 0.0f;
            int i2 = 0;
            while (i2 < format2.length()) {
                int i3 = i2 + 1;
                String substring = format2.substring(i2, i3);
                String substring2 = (this.oldLetters.isEmpty() || i2 >= format.length()) ? null : format.substring(i2, i3);
                if (!z3 && substring2 != null && substring2.equals(substring)) {
                    this.letters.add(this.oldLetters.get(i2));
                    this.oldLetters.set(i2, null);
                } else {
                    if (z3 && substring2 == null) {
                        this.oldLetters.add(new StaticLayout("", this.textPaint, 0, Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false));
                    }
                    TextPaint textPaint = this.textPaint;
                    this.letters.add(new StaticLayout(substring, textPaint, (int) Math.ceil(textPaint.measureText(substring)), Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false));
                }
                i2 = i3;
            }
            if (z && !this.oldLetters.isEmpty()) {
                float[] fArr = new float[2];
                fArr[0] = z2 ? -1.0f : 1.0f;
                fArr[1] = 0.0f;
                ObjectAnimator ofFloat = ObjectAnimator.ofFloat(this, "progress", fArr);
                this.animator = ofFloat;
                ofFloat.setDuration(this.addNumber ? 180L : 150L);
                this.animator.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.Components.NumberTextView.1
                    @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                    public void onAnimationEnd(Animator animator) {
                        NumberTextView.this.animator = null;
                        NumberTextView.this.oldLetters.clear();
                    }
                });
                this.animator.start();
            } else {
                OnTextWidthProgressChangedListener onTextWidthProgressChangedListener = this.onTextWidthProgressChangedListener;
                if (onTextWidthProgressChangedListener != null) {
                    onTextWidthProgressChangedListener.onTextWidthProgress(this.oldTextWidth, this.textWidth, this.progress);
                }
            }
            invalidate();
        }
    }

    public void setTextSize(int i) {
        this.textPaint.setTextSize(AndroidUtilities.dp(i));
        this.oldLetters.clear();
        this.letters.clear();
        setNumber(this.currentNumber, false);
    }

    public void setTextColor(int i) {
        this.textPaint.setColor(i);
        invalidate();
    }

    public void setTypeface(Typeface typeface) {
        this.textPaint.setTypeface(typeface);
        this.oldLetters.clear();
        this.letters.clear();
        setNumber(this.currentNumber, false);
    }

    public void setCenterAlign(boolean z) {
        this.center = z;
    }

    @Override // android.view.View
    protected void onDraw(Canvas canvas) {
        float f;
        float f2;
        if (this.letters.isEmpty()) {
            return;
        }
        float height = this.letters.get(0).getHeight();
        float dp = this.addNumber ? AndroidUtilities.dp(4.0f) : height;
        if (this.center) {
            f = (getMeasuredWidth() - this.textWidth) / 2.0f;
            f2 = ((getMeasuredWidth() - this.oldTextWidth) / 2.0f) - f;
        } else {
            f = 0.0f;
            f2 = 0.0f;
        }
        canvas.save();
        canvas.translate(getPaddingLeft() + f, (getMeasuredHeight() - height) / 2.0f);
        int max = Math.max(this.letters.size(), this.oldLetters.size());
        int i = 0;
        while (i < max) {
            canvas.save();
            StaticLayout staticLayout = null;
            StaticLayout staticLayout2 = i < this.oldLetters.size() ? this.oldLetters.get(i) : null;
            if (i < this.letters.size()) {
                staticLayout = this.letters.get(i);
            }
            float f3 = this.progress;
            if (f3 > 0.0f) {
                if (staticLayout2 != null) {
                    this.textPaint.setAlpha((int) (f3 * 255.0f));
                    canvas.save();
                    canvas.translate(f2, (this.progress - 1.0f) * dp);
                    staticLayout2.draw(canvas);
                    canvas.restore();
                    if (staticLayout != null) {
                        this.textPaint.setAlpha((int) ((1.0f - this.progress) * 255.0f));
                        canvas.translate(0.0f, this.progress * dp);
                    }
                } else {
                    this.textPaint.setAlpha(255);
                }
            } else if (f3 < 0.0f) {
                if (staticLayout2 != null) {
                    this.textPaint.setAlpha((int) ((-f3) * 255.0f));
                    canvas.save();
                    canvas.translate(f2, (this.progress + 1.0f) * dp);
                    staticLayout2.draw(canvas);
                    canvas.restore();
                }
                if (staticLayout != null) {
                    if (i == max - 1 || staticLayout2 != null) {
                        this.textPaint.setAlpha((int) ((this.progress + 1.0f) * 255.0f));
                        canvas.translate(0.0f, this.progress * dp);
                    } else {
                        this.textPaint.setAlpha(255);
                    }
                }
            } else if (staticLayout != null) {
                this.textPaint.setAlpha(255);
            }
            if (staticLayout != null) {
                staticLayout.draw(canvas);
            }
            canvas.restore();
            canvas.translate(staticLayout != null ? staticLayout.getLineWidth(0) : staticLayout2.getLineWidth(0) + AndroidUtilities.dp(1.0f), 0.0f);
            if (staticLayout != null && staticLayout2 != null) {
                f2 += staticLayout2.getLineWidth(0) - staticLayout.getLineWidth(0);
            }
            i++;
        }
        canvas.restore();
    }

    public float getOldTextWidth() {
        return this.oldTextWidth;
    }

    public float getTextWidth() {
        return this.textWidth;
    }
}