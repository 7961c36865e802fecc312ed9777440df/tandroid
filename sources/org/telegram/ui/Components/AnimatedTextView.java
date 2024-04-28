package org.telegram.ui.Components;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.TimeInterpolator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.TextUtils;
import android.view.View;
import android.view.accessibility.AccessibilityNodeInfo;
import androidx.core.graphics.ColorUtils;
import j$.wrappers.$r8$wrapper$java$util$stream$IntStream$-V-WRP;
import j$.wrappers.$r8$wrapper$java$util$stream$IntStream$-WRP;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.IntStream;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.CachedStaticLayout;
import org.telegram.ui.Components.AnimatedEmojiSpan;
import org.telegram.ui.Components.AnimatedTextView;
/* loaded from: classes3.dex */
public class AnimatedTextView extends View {
    public boolean adaptWidth;
    private final AnimatedTextDrawable drawable;
    private boolean first;
    private int lastMaxWidth;
    private int maxWidth;
    private boolean toSetMoveDown;
    private CharSequence toSetText;

    /* loaded from: classes3.dex */
    public static class AnimatedTextDrawable extends Drawable {
        private boolean allowCancel;
        private int alpha;
        private long animateDelay;
        private long animateDuration;
        private TimeInterpolator animateInterpolator;
        private ValueAnimator animator;
        private final android.graphics.Rect bounds;
        private ValueAnimator colorAnimator;
        private float currentHeight;
        private Part[] currentParts;
        private CharSequence currentText;
        private float currentWidth;
        private boolean ellipsizeByGradient;
        private LinearGradient ellipsizeGradient;
        private Matrix ellipsizeGradientMatrix;
        private Paint ellipsizePaint;
        private int emojiCacheType;
        private int emojiColor;
        private ColorFilter emojiColorFilter;
        private int gravity;
        public boolean ignoreRTL;
        private boolean includeFontPadding;
        private boolean isRTL;
        private float moveAmplitude;
        private boolean moveDown;
        private float oldHeight;
        private Part[] oldParts;
        private CharSequence oldText;
        private float oldWidth;
        private Runnable onAnimationFinishListener;
        private int overrideFullWidth;
        private boolean preserveIndex;
        private float rightPadding;
        private float scaleAmplitude;
        private int shadowColor;
        private float shadowDx;
        private float shadowDy;
        private float shadowRadius;
        private boolean shadowed;
        private boolean splitByWords;
        private boolean startFromEnd;
        private float t;
        private final TextPaint textPaint;
        private CharSequence toSetText;
        private boolean toSetTextMoveDown;
        public boolean updateAll;
        private Runnable widthUpdatedListener;

        /* JADX INFO: Access modifiers changed from: private */
        /* loaded from: classes3.dex */
        public interface RegionCallback {
            void run(CharSequence charSequence, int i, int i2);
        }

        @Override // android.graphics.drawable.Drawable
        @Deprecated
        public int getOpacity() {
            return -2;
        }

        public void setSplitByWords(boolean z) {
            this.splitByWords = z;
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* loaded from: classes3.dex */
        public class Part {
            AnimatedEmojiSpan.EmojiGroupedSpans emoji;
            CachedStaticLayout layout;
            float left;
            float offset;
            int toOppositeIndex;
            float width;

            public Part(CachedStaticLayout cachedStaticLayout, float f, int i) {
                this.layout = cachedStaticLayout;
                this.toOppositeIndex = i;
                layout(f);
                if (AnimatedTextDrawable.this.getCallback() instanceof View) {
                    View view = (View) AnimatedTextDrawable.this.getCallback();
                    int i2 = AnimatedTextDrawable.this.emojiCacheType;
                    AnimatedEmojiSpan.EmojiGroupedSpans emojiGroupedSpans = this.emoji;
                    Layout[] layoutArr = new Layout[1];
                    layoutArr[0] = cachedStaticLayout == null ? null : cachedStaticLayout.layout;
                    this.emoji = AnimatedEmojiSpan.update(i2, view, emojiGroupedSpans, layoutArr);
                }
            }

            public void detach() {
                if (AnimatedTextDrawable.this.getCallback() instanceof View) {
                    AnimatedEmojiSpan.release((View) AnimatedTextDrawable.this.getCallback(), this.emoji);
                }
            }

            public void layout(float f) {
                this.offset = f;
                CachedStaticLayout cachedStaticLayout = this.layout;
                float f2 = 0.0f;
                this.left = (cachedStaticLayout == null || cachedStaticLayout.getLineCount() <= 0) ? 0.0f : this.layout.getLineLeft(0);
                CachedStaticLayout cachedStaticLayout2 = this.layout;
                if (cachedStaticLayout2 != null && cachedStaticLayout2.getLineCount() > 0) {
                    f2 = this.layout.getLineWidth(0);
                }
                this.width = f2;
            }

            public void draw(Canvas canvas, float f) {
                this.layout.draw(canvas);
                AnimatedEmojiSpan.drawAnimatedEmojis(canvas, this.layout.layout, this.emoji, 0.0f, null, 0.0f, 0.0f, 0.0f, f, AnimatedTextDrawable.this.emojiColorFilter);
            }
        }

        public void setEmojiCacheType(int i) {
            this.emojiCacheType = i;
        }

        public void setHacks(boolean z, boolean z2, boolean z3) {
            this.splitByWords = z;
            this.preserveIndex = z2;
            this.startFromEnd = z3;
        }

        public void setOverrideFullWidth(int i) {
            this.overrideFullWidth = i;
        }

        public AnimatedTextDrawable() {
            this(false, false, false);
        }

        public AnimatedTextDrawable(boolean z, boolean z2, boolean z3) {
            this.textPaint = new TextPaint(1);
            this.gravity = 0;
            this.isRTL = false;
            this.emojiCacheType = 0;
            this.t = 0.0f;
            this.moveDown = true;
            this.animateDelay = 0L;
            this.animateDuration = 320L;
            this.animateInterpolator = CubicBezierInterpolator.EASE_OUT_QUINT;
            this.moveAmplitude = 0.3f;
            this.scaleAmplitude = 0.0f;
            this.alpha = 255;
            this.bounds = new android.graphics.Rect();
            this.includeFontPadding = true;
            this.shadowed = false;
            this.splitByWords = z;
            this.preserveIndex = z2;
            this.startFromEnd = z3;
        }

        public void setAllowCancel(boolean z) {
            this.allowCancel = z;
        }

        public void setEllipsizeByGradient(boolean z) {
            this.ellipsizeByGradient = z;
            invalidateSelf();
        }

        public void setOnAnimationFinishListener(Runnable runnable) {
            this.onAnimationFinishListener = runnable;
        }

        private void applyAlphaInternal(float f) {
            this.textPaint.setAlpha((int) (this.alpha * f));
            if (this.shadowed) {
                this.textPaint.setShadowLayer(this.shadowRadius, this.shadowDx, this.shadowDy, Theme.multAlpha(this.shadowColor, f));
            }
        }

        /* JADX WARN: Code restructure failed: missing block: B:46:0x00eb, code lost:
            if (r18.ignoreRTL == false) goto L32;
         */
        /* JADX WARN: Removed duplicated region for block: B:118:0x021b  */
        /* JADX WARN: Removed duplicated region for block: B:134:? A[RETURN, SYNTHETIC] */
        @Override // android.graphics.drawable.Drawable
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public void draw(Canvas canvas) {
            float f;
            float f2;
            float f3;
            android.graphics.Rect rect;
            float f4;
            float f5;
            float f6;
            float f7;
            float f8;
            float f9;
            if (this.ellipsizeByGradient) {
                RectF rectF = AndroidUtilities.rectTmp;
                rectF.set(this.bounds);
                rectF.right -= this.rightPadding;
                canvas.saveLayerAlpha(rectF, 255, 31);
            }
            canvas.save();
            android.graphics.Rect rect2 = this.bounds;
            canvas.translate(rect2.left, rect2.top);
            int width = this.bounds.width();
            int height = this.bounds.height();
            if (this.currentParts != null && this.oldParts != null) {
                float f10 = this.t;
                if (f10 != 1.0f) {
                    float lerp = AndroidUtilities.lerp(this.oldWidth, this.currentWidth, f10);
                    canvas.translate(0.0f, (height - AndroidUtilities.lerp(this.oldHeight, this.currentHeight, this.t)) / 2.0f);
                    int i = 0;
                    while (true) {
                        Part[] partArr = this.currentParts;
                        if (i >= partArr.length) {
                            break;
                        }
                        Part part = partArr[i];
                        int i2 = part.toOppositeIndex;
                        float f11 = part.offset;
                        boolean z = this.isRTL;
                        if (z && !this.ignoreRTL) {
                            f11 = this.currentWidth - (f11 + part.width);
                        }
                        if (i2 >= 0) {
                            Part part2 = this.oldParts[i2];
                            float f12 = part2.offset;
                            if (z && !this.ignoreRTL) {
                                f12 = this.oldWidth - (f12 + part2.width);
                            }
                            f7 = AndroidUtilities.lerp(f12 - part2.left, f11 - part.left, this.t);
                            applyAlphaInternal(1.0f);
                            f8 = 0.0f;
                        } else {
                            f7 = f11 - part.left;
                            float f13 = (-this.textPaint.getTextSize()) * this.moveAmplitude;
                            float f14 = this.t;
                            f8 = f13 * (1.0f - f14) * (this.moveDown ? 1.0f : -1.0f);
                            applyAlphaInternal(f14);
                        }
                        canvas.save();
                        float f15 = i2 >= 0 ? lerp : this.currentWidth;
                        int i3 = this.gravity;
                        if ((i3 | (-4)) != -1) {
                            if ((i3 | (-6)) != -1) {
                                if ((i3 | (-2)) == -1) {
                                    f9 = (width - f15) / 2.0f;
                                    f7 += f9;
                                } else if (this.isRTL) {
                                }
                            }
                            f9 = width - f15;
                            f7 += f9;
                        }
                        canvas.translate(f7, f8);
                        if (i2 < 0) {
                            float f16 = this.scaleAmplitude;
                            if (f16 > 0.0f) {
                                float lerp2 = AndroidUtilities.lerp(1.0f - f16, 1.0f, this.t);
                                canvas.scale(lerp2, lerp2, part.width / 2.0f, part.layout.getHeight() / 2.0f);
                            }
                        }
                        part.draw(canvas, i2 >= 0 ? 1.0f : this.t);
                        canvas.restore();
                        i++;
                    }
                    int i4 = 0;
                    while (true) {
                        Part[] partArr2 = this.oldParts;
                        if (i4 >= partArr2.length) {
                            break;
                        }
                        Part part3 = partArr2[i4];
                        if (part3.toOppositeIndex < 0) {
                            float f17 = part3.offset;
                            float textSize = this.textPaint.getTextSize() * this.moveAmplitude;
                            float f18 = this.t;
                            float f19 = textSize * f18 * (this.moveDown ? 1.0f : -1.0f);
                            applyAlphaInternal(1.0f - f18);
                            canvas.save();
                            boolean z2 = this.isRTL;
                            if (z2 && !this.ignoreRTL) {
                                f17 = this.oldWidth - (f17 + part3.width);
                            }
                            float f20 = f17 - part3.left;
                            int i5 = this.gravity;
                            if ((i5 | (-4)) != -1) {
                                if ((i5 | (-6)) == -1) {
                                    f4 = width;
                                    f5 = this.oldWidth;
                                } else if ((i5 | (-2)) == -1) {
                                    f6 = (width - this.oldWidth) / 2.0f;
                                    f20 += f6;
                                } else if (z2 && !this.ignoreRTL) {
                                    f4 = width;
                                    f5 = this.oldWidth;
                                }
                                f6 = f4 - f5;
                                f20 += f6;
                            }
                            canvas.translate(f20, f19);
                            float f21 = this.scaleAmplitude;
                            if (f21 > 0.0f) {
                                float lerp3 = AndroidUtilities.lerp(1.0f, 1.0f - f21, this.t);
                                canvas.scale(lerp3, lerp3, part3.width / 2.0f, part3.layout.getHeight() / 2.0f);
                            }
                            part3.draw(canvas, 1.0f - this.t);
                            canvas.restore();
                        }
                        i4++;
                    }
                    canvas.restore();
                    if (this.ellipsizeByGradient) {
                        return;
                    }
                    float dp = AndroidUtilities.dp(16.0f);
                    if (this.ellipsizeGradient == null) {
                        this.ellipsizeGradient = new LinearGradient(0.0f, 0.0f, dp, 0.0f, new int[]{16711680, -65536}, new float[]{0.0f, 1.0f}, Shader.TileMode.CLAMP);
                        this.ellipsizeGradientMatrix = new Matrix();
                        Paint paint = new Paint(1);
                        this.ellipsizePaint = paint;
                        paint.setShader(this.ellipsizeGradient);
                        this.ellipsizePaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OUT));
                    }
                    this.ellipsizeGradientMatrix.reset();
                    this.ellipsizeGradientMatrix.postTranslate((this.bounds.right - this.rightPadding) - dp, 0.0f);
                    this.ellipsizeGradient.setLocalMatrix(this.ellipsizeGradientMatrix);
                    canvas.save();
                    int i6 = this.bounds.right;
                    float f22 = this.rightPadding;
                    canvas.drawRect((i6 - f22) - dp, rect.top, (i6 - f22) + AndroidUtilities.dp(1.0f), this.bounds.bottom, this.ellipsizePaint);
                    canvas.restore();
                    canvas.restore();
                    return;
                }
            }
            canvas.translate(0.0f, (height - this.currentHeight) / 2.0f);
            if (this.currentParts != null) {
                applyAlphaInternal(1.0f);
                for (int i7 = 0; i7 < this.currentParts.length; i7++) {
                    canvas.save();
                    Part part4 = this.currentParts[i7];
                    float f23 = part4.offset;
                    boolean z3 = this.isRTL;
                    if (z3 && !this.ignoreRTL) {
                        f23 = this.currentWidth - (f23 + part4.width);
                    }
                    float f24 = f23 - part4.left;
                    int i8 = this.gravity;
                    if ((i8 | (-4)) != -1) {
                        if ((i8 | (-6)) == -1) {
                            f = width;
                            f2 = this.currentWidth;
                        } else if ((i8 | (-2)) == -1) {
                            f3 = (width - this.currentWidth) / 2.0f;
                            f24 += f3;
                        } else if (z3 && !this.ignoreRTL) {
                            f = width;
                            f2 = this.currentWidth;
                        }
                        f3 = f - f2;
                        f24 += f3;
                    }
                    canvas.translate(f24, 0.0f);
                    part4.draw(canvas, 1.0f);
                    canvas.restore();
                }
            }
            canvas.restore();
            if (this.ellipsizeByGradient) {
            }
        }

        public void setRightPadding(float f) {
            this.rightPadding = f;
            invalidateSelf();
        }

        public float getRightPadding() {
            return this.rightPadding;
        }

        public void cancelAnimation() {
            ValueAnimator valueAnimator = this.animator;
            if (valueAnimator != null) {
                valueAnimator.cancel();
            }
        }

        public boolean isAnimating() {
            ValueAnimator valueAnimator = this.animator;
            return valueAnimator != null && valueAnimator.isRunning();
        }

        public void setText(CharSequence charSequence) {
            setText(charSequence, true);
        }

        public void setText(CharSequence charSequence, boolean z) {
            setText(charSequence, z, true);
        }

        public void setText(CharSequence charSequence, boolean z, boolean z2) {
            z = (this.currentText == null || charSequence == null) ? false : false;
            if (charSequence == null) {
                charSequence = "";
            }
            final int i = this.overrideFullWidth;
            if (i <= 0) {
                i = this.bounds.width();
            }
            if (z) {
                if (this.allowCancel) {
                    ValueAnimator valueAnimator = this.animator;
                    if (valueAnimator != null) {
                        valueAnimator.cancel();
                        this.animator = null;
                    }
                } else if (isAnimating()) {
                    this.toSetText = charSequence;
                    this.toSetTextMoveDown = z2;
                    return;
                }
                if (charSequence.equals(this.currentText)) {
                    return;
                }
                this.oldText = this.currentText;
                this.currentText = charSequence;
                final ArrayList arrayList = new ArrayList();
                final ArrayList arrayList2 = new ArrayList();
                this.currentHeight = 0.0f;
                this.currentWidth = 0.0f;
                this.oldHeight = 0.0f;
                this.oldWidth = 0.0f;
                this.isRTL = AndroidUtilities.isRTL(this.currentText);
                diff(this.splitByWords ? new WordSequence(this.oldText) : this.oldText, this.splitByWords ? new WordSequence(this.currentText) : this.currentText, new RegionCallback() { // from class: org.telegram.ui.Components.AnimatedTextView$AnimatedTextDrawable$$ExternalSyntheticLambda4
                    @Override // org.telegram.ui.Components.AnimatedTextView.AnimatedTextDrawable.RegionCallback
                    public final void run(CharSequence charSequence2, int i2, int i3) {
                        AnimatedTextView.AnimatedTextDrawable.this.lambda$setText$0(i, arrayList2, arrayList, charSequence2, i2, i3);
                    }
                }, new RegionCallback() { // from class: org.telegram.ui.Components.AnimatedTextView$AnimatedTextDrawable$$ExternalSyntheticLambda3
                    @Override // org.telegram.ui.Components.AnimatedTextView.AnimatedTextDrawable.RegionCallback
                    public final void run(CharSequence charSequence2, int i2, int i3) {
                        AnimatedTextView.AnimatedTextDrawable.this.lambda$setText$1(i, arrayList, charSequence2, i2, i3);
                    }
                }, new RegionCallback() { // from class: org.telegram.ui.Components.AnimatedTextView$AnimatedTextDrawable$$ExternalSyntheticLambda2
                    @Override // org.telegram.ui.Components.AnimatedTextView.AnimatedTextDrawable.RegionCallback
                    public final void run(CharSequence charSequence2, int i2, int i3) {
                        AnimatedTextView.AnimatedTextDrawable.this.lambda$setText$2(i, arrayList2, charSequence2, i2, i3);
                    }
                });
                clearCurrentParts();
                Part[] partArr = this.currentParts;
                if (partArr == null || partArr.length != arrayList.size()) {
                    this.currentParts = new Part[arrayList.size()];
                }
                arrayList.toArray(this.currentParts);
                clearOldParts();
                Part[] partArr2 = this.oldParts;
                if (partArr2 == null || partArr2.length != arrayList2.size()) {
                    this.oldParts = new Part[arrayList2.size()];
                }
                arrayList2.toArray(this.oldParts);
                ValueAnimator valueAnimator2 = this.animator;
                if (valueAnimator2 != null) {
                    valueAnimator2.cancel();
                }
                this.moveDown = z2;
                this.t = 0.0f;
                this.animator = ValueAnimator.ofFloat(0.0f, 1.0f);
                Runnable runnable = this.widthUpdatedListener;
                if (runnable != null) {
                    runnable.run();
                }
                this.animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: org.telegram.ui.Components.AnimatedTextView$AnimatedTextDrawable$$ExternalSyntheticLambda0
                    @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                    public final void onAnimationUpdate(ValueAnimator valueAnimator3) {
                        AnimatedTextView.AnimatedTextDrawable.this.lambda$setText$3(valueAnimator3);
                    }
                });
                this.animator.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.Components.AnimatedTextView.AnimatedTextDrawable.1
                    @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                    public void onAnimationEnd(Animator animator) {
                        super.onAnimationEnd(animator);
                        AnimatedTextDrawable.this.clearOldParts();
                        AnimatedTextDrawable.this.oldText = null;
                        AnimatedTextDrawable.this.oldWidth = 0.0f;
                        AnimatedTextDrawable.this.t = 0.0f;
                        AnimatedTextDrawable.this.invalidateSelf();
                        if (AnimatedTextDrawable.this.widthUpdatedListener != null) {
                            AnimatedTextDrawable.this.widthUpdatedListener.run();
                        }
                        AnimatedTextDrawable.this.animator = null;
                        if (AnimatedTextDrawable.this.toSetText == null) {
                            if (AnimatedTextDrawable.this.onAnimationFinishListener != null) {
                                AnimatedTextDrawable.this.onAnimationFinishListener.run();
                                return;
                            }
                            return;
                        }
                        AnimatedTextDrawable animatedTextDrawable = AnimatedTextDrawable.this;
                        animatedTextDrawable.setText(animatedTextDrawable.toSetText, true, AnimatedTextDrawable.this.toSetTextMoveDown);
                        AnimatedTextDrawable.this.toSetText = null;
                        AnimatedTextDrawable.this.toSetTextMoveDown = false;
                    }
                });
                this.animator.setStartDelay(this.animateDelay);
                this.animator.setDuration(this.animateDuration);
                this.animator.setInterpolator(this.animateInterpolator);
                this.animator.start();
                return;
            }
            ValueAnimator valueAnimator3 = this.animator;
            if (valueAnimator3 != null) {
                valueAnimator3.cancel();
            }
            this.animator = null;
            this.toSetText = null;
            this.toSetTextMoveDown = false;
            this.t = 0.0f;
            if (!charSequence.equals(this.currentText)) {
                clearCurrentParts();
                this.currentParts = r12;
                this.currentText = charSequence;
                Part[] partArr3 = {new Part(new CachedStaticLayout(makeLayout(charSequence, i)), 0.0f, -1)};
                Part[] partArr4 = this.currentParts;
                this.currentWidth = partArr4[0].width;
                this.currentHeight = partArr4[0].layout.getHeight();
                this.isRTL = AndroidUtilities.isRTL(this.currentText);
            }
            clearOldParts();
            this.oldText = null;
            this.oldWidth = 0.0f;
            this.oldHeight = 0.0f;
            invalidateSelf();
            Runnable runnable2 = this.widthUpdatedListener;
            if (runnable2 != null) {
                runnable2.run();
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$setText$0(int i, ArrayList arrayList, ArrayList arrayList2, CharSequence charSequence, int i2, int i3) {
            CachedStaticLayout cachedStaticLayout = new CachedStaticLayout(makeLayout(charSequence, i - ((int) Math.ceil(Math.min(this.currentWidth, this.oldWidth)))));
            Part part = new Part(cachedStaticLayout, this.currentWidth, arrayList.size());
            Part part2 = new Part(cachedStaticLayout, this.oldWidth, arrayList.size());
            arrayList2.add(part);
            arrayList.add(part2);
            float f = part.width;
            this.currentWidth += f;
            this.oldWidth += f;
            this.currentHeight = Math.max(this.currentHeight, cachedStaticLayout.getHeight());
            this.oldHeight = Math.max(this.oldHeight, cachedStaticLayout.getHeight());
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$setText$1(int i, ArrayList arrayList, CharSequence charSequence, int i2, int i3) {
            CachedStaticLayout cachedStaticLayout;
            Part part = new Part(new CachedStaticLayout(makeLayout(charSequence, i - ((int) Math.ceil(this.currentWidth)))), this.currentWidth, -1);
            arrayList.add(part);
            this.currentWidth += part.width;
            this.currentHeight = Math.max(this.currentHeight, cachedStaticLayout.getHeight());
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$setText$2(int i, ArrayList arrayList, CharSequence charSequence, int i2, int i3) {
            CachedStaticLayout cachedStaticLayout;
            Part part = new Part(new CachedStaticLayout(makeLayout(charSequence, i - ((int) Math.ceil(this.oldWidth)))), this.oldWidth, -1);
            arrayList.add(part);
            this.oldWidth += part.width;
            this.oldHeight = Math.max(this.oldHeight, cachedStaticLayout.getHeight());
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$setText$3(ValueAnimator valueAnimator) {
            this.t = ((Float) valueAnimator.getAnimatedValue()).floatValue();
            invalidateSelf();
            Runnable runnable = this.widthUpdatedListener;
            if (runnable != null) {
                runnable.run();
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void clearOldParts() {
            if (this.oldParts != null) {
                int i = 0;
                while (true) {
                    Part[] partArr = this.oldParts;
                    if (i >= partArr.length) {
                        break;
                    }
                    partArr[i].detach();
                    i++;
                }
            }
            this.oldParts = null;
        }

        private void clearCurrentParts() {
            if (this.oldParts != null) {
                int i = 0;
                while (true) {
                    Part[] partArr = this.oldParts;
                    if (i >= partArr.length) {
                        break;
                    }
                    partArr[i].detach();
                    i++;
                }
            }
            this.oldParts = null;
        }

        public CharSequence getText() {
            return this.currentText;
        }

        public float getWidth() {
            return Math.max(this.currentWidth, this.oldWidth);
        }

        public float getCurrentWidth() {
            if (this.currentParts != null && this.oldParts != null) {
                return AndroidUtilities.lerp(this.oldWidth, this.currentWidth, this.t);
            }
            return this.currentWidth;
        }

        public float getAnimateToWidth() {
            return this.currentWidth;
        }

        public float getHeight() {
            return this.currentHeight;
        }

        private StaticLayout makeLayout(CharSequence charSequence, int i) {
            if (i <= 0) {
                android.graphics.Point point = AndroidUtilities.displaySize;
                i = Math.min(point.x, point.y);
            }
            int i2 = i;
            if (Build.VERSION.SDK_INT >= 23) {
                return StaticLayout.Builder.obtain(charSequence, 0, charSequence.length(), this.textPaint, i2).setMaxLines(1).setLineSpacing(0.0f, 1.0f).setAlignment(Layout.Alignment.ALIGN_NORMAL).setEllipsize(TextUtils.TruncateAt.END).setEllipsizedWidth(i2).setIncludePad(this.includeFontPadding).build();
            }
            return new StaticLayout(charSequence, 0, charSequence.length(), this.textPaint, i2, Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, this.includeFontPadding, TextUtils.TruncateAt.END, i2);
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* loaded from: classes3.dex */
        public static class WordSequence implements CharSequence {
            private final int length;
            private final CharSequence[] words;

            @Override // java.lang.CharSequence
            public /* synthetic */ IntStream chars() {
                return $r8$wrapper$java$util$stream$IntStream$-WRP.convert(chars());
            }

            @Override // java.lang.CharSequence
            public /* synthetic */ IntStream codePoints() {
                return $r8$wrapper$java$util$stream$IntStream$-WRP.convert(codePoints());
            }

            public WordSequence(CharSequence charSequence) {
                if (charSequence == null) {
                    this.words = new CharSequence[0];
                    this.length = 0;
                    return;
                }
                this.length = charSequence.length();
                int i = 0;
                for (int i2 = 0; i2 < this.length; i2++) {
                    if (charSequence.charAt(i2) == ' ') {
                        i++;
                    }
                }
                this.words = new CharSequence[i + 1];
                int i3 = 0;
                int i4 = 0;
                int i5 = 0;
                while (true) {
                    int i6 = this.length;
                    if (i3 > i6) {
                        return;
                    }
                    if (i3 == i6 || charSequence.charAt(i3) == ' ') {
                        int i7 = i4 + 1;
                        this.words[i4] = charSequence.subSequence(i5, (i3 < this.length ? 1 : 0) + i3);
                        i5 = i3 + 1;
                        i4 = i7;
                    }
                    i3++;
                }
            }

            public CharSequence wordAt(int i) {
                if (i >= 0) {
                    CharSequence[] charSequenceArr = this.words;
                    if (i >= charSequenceArr.length) {
                        return null;
                    }
                    return charSequenceArr[i];
                }
                return null;
            }

            @Override // java.lang.CharSequence
            public int length() {
                return this.words.length;
            }

            @Override // java.lang.CharSequence
            public char charAt(int i) {
                int i2 = 0;
                while (true) {
                    CharSequence[] charSequenceArr = this.words;
                    if (i2 >= charSequenceArr.length) {
                        return (char) 0;
                    }
                    if (i < charSequenceArr[i2].length()) {
                        return this.words[i2].charAt(i);
                    }
                    i -= this.words[i2].length();
                    i2++;
                }
            }

            @Override // java.lang.CharSequence
            public CharSequence subSequence(int i, int i2) {
                return TextUtils.concat((CharSequence[]) Arrays.copyOfRange(this.words, i, i2));
            }

            @Override // java.lang.CharSequence
            public String toString() {
                StringBuilder sb = new StringBuilder();
                int i = 0;
                while (true) {
                    CharSequence[] charSequenceArr = this.words;
                    if (i < charSequenceArr.length) {
                        sb.append(charSequenceArr[i]);
                        i++;
                    } else {
                        return sb.toString();
                    }
                }
            }

            public CharSequence toCharSequence() {
                return TextUtils.concat(this.words);
            }

            @Override // java.lang.CharSequence
            public j$.util.stream.IntStream chars() {
                if (Build.VERSION.SDK_INT >= 24) {
                    return $r8$wrapper$java$util$stream$IntStream$-V-WRP.convert(toCharSequence().chars());
                }
                return null;
            }

            @Override // java.lang.CharSequence
            public j$.util.stream.IntStream codePoints() {
                if (Build.VERSION.SDK_INT >= 24) {
                    return $r8$wrapper$java$util$stream$IntStream$-V-WRP.convert(toCharSequence().codePoints());
                }
                return null;
            }
        }

        public static boolean partEquals(CharSequence charSequence, CharSequence charSequence2, int i, int i2) {
            if (!(charSequence instanceof WordSequence) || !(charSequence2 instanceof WordSequence)) {
                if (charSequence == null && charSequence2 == null) {
                    return true;
                }
                return (charSequence == null || charSequence2 == null || charSequence.charAt(i) != charSequence2.charAt(i2)) ? false : true;
            }
            CharSequence wordAt = ((WordSequence) charSequence).wordAt(i);
            CharSequence wordAt2 = ((WordSequence) charSequence2).wordAt(i2);
            if (wordAt == null && wordAt2 == null) {
                return true;
            }
            return wordAt != null && wordAt.equals(wordAt2);
        }

        private void diff(CharSequence charSequence, CharSequence charSequence2, RegionCallback regionCallback, RegionCallback regionCallback2, RegionCallback regionCallback3) {
            if (this.updateAll) {
                regionCallback3.run(charSequence, 0, charSequence.length());
                regionCallback2.run(charSequence2, 0, charSequence2.length());
            } else if (this.preserveIndex) {
                int min = Math.min(charSequence2.length(), charSequence.length());
                if (this.startFromEnd) {
                    ArrayList arrayList = new ArrayList();
                    boolean z = true;
                    int i = 0;
                    boolean z2 = true;
                    for (int i2 = 0; i2 <= min; i2++) {
                        int length = (charSequence2.length() - i2) - 1;
                        int length2 = (charSequence.length() - i2) - 1;
                        boolean z3 = length >= 0 && length2 >= 0 && partEquals(charSequence2, charSequence, length, length2);
                        if (z != z3 || i2 == min) {
                            int i3 = i2 - i;
                            if (i3 > 0) {
                                if (arrayList.size() != 0) {
                                    z = z2;
                                }
                                arrayList.add(Integer.valueOf(i3));
                                z2 = z;
                            }
                            i = i2;
                            z = z3;
                        }
                    }
                    int length3 = charSequence2.length() - min;
                    int length4 = charSequence.length() - min;
                    if (length3 > 0) {
                        regionCallback2.run(charSequence2.subSequence(0, length3), 0, length3);
                    }
                    if (length4 > 0) {
                        regionCallback3.run(charSequence.subSequence(0, length4), 0, length4);
                    }
                    for (int size = arrayList.size() - 1; size >= 0; size--) {
                        int intValue = ((Integer) arrayList.get(size)).intValue();
                        if ((size % 2 == 0) == z2) {
                            if (charSequence2.length() > charSequence.length()) {
                                int i4 = length3 + intValue;
                                regionCallback.run(charSequence2.subSequence(length3, i4), length3, i4);
                            } else {
                                int i5 = length4 + intValue;
                                regionCallback.run(charSequence.subSequence(length4, i5), length4, i5);
                            }
                        } else {
                            int i6 = length3 + intValue;
                            regionCallback2.run(charSequence2.subSequence(length3, i6), length3, i6);
                            int i7 = length4 + intValue;
                            regionCallback3.run(charSequence.subSequence(length4, i7), length4, i7);
                        }
                        length3 += intValue;
                        length4 += intValue;
                    }
                    return;
                }
                int i8 = 0;
                boolean z4 = true;
                int i9 = 0;
                while (i8 <= min) {
                    boolean z5 = i8 < min && partEquals(charSequence2, charSequence, i8, i8);
                    if (z4 != z5 || i8 == min) {
                        if (i8 - i9 > 0) {
                            if (z4) {
                                regionCallback.run(charSequence2.subSequence(i9, i8), i9, i8);
                            } else {
                                regionCallback2.run(charSequence2.subSequence(i9, i8), i9, i8);
                                regionCallback3.run(charSequence.subSequence(i9, i8), i9, i8);
                            }
                        }
                        i9 = i8;
                        z4 = z5;
                    }
                    i8++;
                }
                if (charSequence2.length() - min > 0) {
                    regionCallback2.run(charSequence2.subSequence(min, charSequence2.length()), min, charSequence2.length());
                }
                if (charSequence.length() - min > 0) {
                    regionCallback3.run(charSequence.subSequence(min, charSequence.length()), min, charSequence.length());
                }
            } else {
                int min2 = Math.min(charSequence2.length(), charSequence.length());
                int i10 = 0;
                int i11 = 0;
                boolean z6 = true;
                int i12 = 0;
                int i13 = 0;
                while (i10 <= min2) {
                    boolean z7 = i10 < min2 && partEquals(charSequence2, charSequence, i10, i11);
                    if (z6 != z7 || i10 == min2) {
                        if (i10 == min2) {
                            i10 = charSequence2.length();
                            i11 = charSequence.length();
                        }
                        int i14 = i10 - i12;
                        int i15 = i11 - i13;
                        if (i14 > 0 || i15 > 0) {
                            if (i14 == i15 && z6) {
                                regionCallback.run(charSequence2.subSequence(i12, i10), i12, i10);
                            } else {
                                if (i14 > 0) {
                                    regionCallback2.run(charSequence2.subSequence(i12, i10), i12, i10);
                                }
                                if (i15 > 0) {
                                    regionCallback3.run(charSequence.subSequence(i13, i11), i13, i11);
                                }
                            }
                        }
                        i12 = i10;
                        i13 = i11;
                        z6 = z7;
                    }
                    if (z7) {
                        i11++;
                    }
                    i10++;
                }
            }
        }

        public void setTextSize(float f) {
            float textSize = this.textPaint.getTextSize();
            this.textPaint.setTextSize(f);
            if (Math.abs(textSize - f) > 0.5f) {
                int i = this.overrideFullWidth;
                if (i <= 0) {
                    i = this.bounds.width();
                }
                int i2 = 0;
                if (this.currentParts != null) {
                    this.currentWidth = 0.0f;
                    this.currentHeight = 0.0f;
                    int i3 = 0;
                    while (true) {
                        Part[] partArr = this.currentParts;
                        if (i3 >= partArr.length) {
                            break;
                        }
                        StaticLayout makeLayout = makeLayout(partArr[i3].layout.getText(), i - ((int) Math.ceil(Math.min(this.currentWidth, this.oldWidth))));
                        Part[] partArr2 = this.currentParts;
                        CachedStaticLayout cachedStaticLayout = new CachedStaticLayout(makeLayout);
                        Part[] partArr3 = this.currentParts;
                        partArr2[i3] = new Part(cachedStaticLayout, partArr3[i3].offset, partArr3[i3].toOppositeIndex);
                        float f2 = this.currentWidth;
                        Part[] partArr4 = this.currentParts;
                        this.currentWidth = f2 + partArr4[i3].width;
                        this.currentHeight = Math.max(this.currentHeight, partArr4[i3].layout.getHeight());
                        i3++;
                    }
                }
                if (this.oldParts != null) {
                    this.oldWidth = 0.0f;
                    this.oldHeight = 0.0f;
                    while (true) {
                        Part[] partArr5 = this.oldParts;
                        if (i2 >= partArr5.length) {
                            break;
                        }
                        CachedStaticLayout cachedStaticLayout2 = new CachedStaticLayout(makeLayout(partArr5[i2].layout.getText(), i - ((int) Math.ceil(Math.min(this.currentWidth, this.oldWidth)))));
                        Part[] partArr6 = this.oldParts;
                        partArr6[i2] = new Part(cachedStaticLayout2, partArr6[i2].offset, partArr6[i2].toOppositeIndex);
                        float f3 = this.oldWidth;
                        Part[] partArr7 = this.oldParts;
                        this.oldWidth = f3 + partArr7[i2].width;
                        this.oldHeight = Math.max(this.oldHeight, partArr7[i2].layout.getHeight());
                        i2++;
                    }
                }
                invalidateSelf();
            }
        }

        public float getTextSize() {
            return this.textPaint.getTextSize();
        }

        public void setTextColor(int i) {
            this.textPaint.setColor(i);
            this.alpha = Color.alpha(i);
        }

        public void setShadowLayer(float f, float f2, float f3, int i) {
            this.shadowed = true;
            TextPaint textPaint = this.textPaint;
            this.shadowRadius = f;
            this.shadowDx = f2;
            this.shadowDy = f3;
            this.shadowColor = i;
            textPaint.setShadowLayer(f, f2, f3, i);
        }

        public int getTextColor() {
            return this.textPaint.getColor();
        }

        public void setTextColor(final int i, boolean z) {
            ValueAnimator valueAnimator = this.colorAnimator;
            if (valueAnimator != null) {
                valueAnimator.cancel();
                this.colorAnimator = null;
            }
            if (!z) {
                setTextColor(i);
                return;
            }
            final int textColor = getTextColor();
            ValueAnimator ofFloat = ValueAnimator.ofFloat(0.0f, 1.0f);
            this.colorAnimator = ofFloat;
            ofFloat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: org.telegram.ui.Components.AnimatedTextView$AnimatedTextDrawable$$ExternalSyntheticLambda1
                @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                public final void onAnimationUpdate(ValueAnimator valueAnimator2) {
                    AnimatedTextView.AnimatedTextDrawable.this.lambda$setTextColor$9(textColor, i, valueAnimator2);
                }
            });
            this.colorAnimator.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.Components.AnimatedTextView.AnimatedTextDrawable.2
                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public void onAnimationEnd(Animator animator) {
                    AnimatedTextDrawable.this.setTextColor(i);
                }
            });
            this.colorAnimator.setDuration(240L);
            this.colorAnimator.setInterpolator(CubicBezierInterpolator.EASE_OUT_QUINT);
            this.colorAnimator.start();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$setTextColor$9(int i, int i2, ValueAnimator valueAnimator) {
            setTextColor(ColorUtils.blendARGB(i, i2, ((Float) valueAnimator.getAnimatedValue()).floatValue()));
            invalidateSelf();
        }

        public void setEmojiColorFilter(ColorFilter colorFilter) {
            this.emojiColorFilter = colorFilter;
        }

        public void setEmojiColor(int i) {
            if (this.emojiColor != i) {
                this.emojiColor = i;
                this.emojiColorFilter = new PorterDuffColorFilter(i, PorterDuff.Mode.MULTIPLY);
            }
        }

        public void setTypeface(Typeface typeface) {
            this.textPaint.setTypeface(typeface);
        }

        public void setGravity(int i) {
            this.gravity = i;
        }

        public int getGravity() {
            return this.gravity;
        }

        public void setAnimationProperties(float f, long j, long j2, TimeInterpolator timeInterpolator) {
            this.moveAmplitude = f;
            this.animateDelay = j;
            this.animateDuration = j2;
            this.animateInterpolator = timeInterpolator;
        }

        public void setScaleProperty(float f) {
            this.scaleAmplitude = f;
        }

        public TextPaint getPaint() {
            return this.textPaint;
        }

        @Override // android.graphics.drawable.Drawable
        public void setAlpha(int i) {
            this.alpha = i;
        }

        @Override // android.graphics.drawable.Drawable
        public void setColorFilter(ColorFilter colorFilter) {
            this.textPaint.setColorFilter(colorFilter);
        }

        @Override // android.graphics.drawable.Drawable
        public void setBounds(android.graphics.Rect rect) {
            super.setBounds(rect);
            this.bounds.set(rect);
        }

        @Override // android.graphics.drawable.Drawable
        public void setBounds(int i, int i2, int i3, int i4) {
            super.setBounds(i, i2, i3, i4);
            this.bounds.set(i, i2, i3, i4);
        }

        @Override // android.graphics.drawable.Drawable
        public android.graphics.Rect getDirtyBounds() {
            return this.bounds;
        }

        public float isNotEmpty() {
            CharSequence charSequence = this.oldText;
            float f = 0.0f;
            float f2 = (charSequence == null || charSequence.length() <= 0) ? 0.0f : 1.0f;
            CharSequence charSequence2 = this.currentText;
            if (charSequence2 != null && charSequence2.length() > 0) {
                f = 1.0f;
            }
            return AndroidUtilities.lerp(f2, f, this.oldText != null ? this.t : 1.0f);
        }

        public void setOnWidthUpdatedListener(Runnable runnable) {
            this.widthUpdatedListener = runnable;
        }

        public void setIncludeFontPadding(boolean z) {
            this.includeFontPadding = z;
        }
    }

    public AnimatedTextView(Context context) {
        this(context, false, false, false);
    }

    public AnimatedTextView(Context context, boolean z, boolean z2, boolean z3) {
        super(context);
        this.adaptWidth = true;
        this.first = true;
        AnimatedTextDrawable animatedTextDrawable = new AnimatedTextDrawable(z, z2, z3);
        this.drawable = animatedTextDrawable;
        animatedTextDrawable.setCallback(this);
        animatedTextDrawable.setOnAnimationFinishListener(new Runnable() { // from class: org.telegram.ui.Components.AnimatedTextView$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                AnimatedTextView.this.lambda$new$0();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0() {
        CharSequence charSequence = this.toSetText;
        if (charSequence != null) {
            setText(charSequence, this.toSetMoveDown, true);
            this.toSetText = null;
            this.toSetMoveDown = false;
        }
    }

    public void setMaxWidth(int i) {
        this.maxWidth = i;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.view.View
    public void onMeasure(int i, int i2) {
        int size = View.MeasureSpec.getSize(i);
        int size2 = View.MeasureSpec.getSize(i2);
        int i3 = this.maxWidth;
        if (i3 > 0) {
            size = Math.min(size, i3);
        }
        if (this.lastMaxWidth != size && getLayoutParams().width != 0) {
            this.drawable.setBounds(getPaddingLeft(), getPaddingTop(), size - getPaddingRight(), size2 - getPaddingBottom());
            AnimatedTextDrawable animatedTextDrawable = this.drawable;
            animatedTextDrawable.setText(animatedTextDrawable.getText(), false, true);
        }
        this.lastMaxWidth = size;
        if (this.adaptWidth && View.MeasureSpec.getMode(i) == Integer.MIN_VALUE) {
            size = getPaddingRight() + getPaddingLeft() + ((int) Math.ceil(this.drawable.getWidth()));
        }
        setMeasuredDimension(size, size2);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.view.View
    public void onDraw(Canvas canvas) {
        this.drawable.setBounds(getPaddingLeft(), getPaddingTop(), getMeasuredWidth() - getPaddingRight(), getMeasuredHeight() - getPaddingBottom());
        this.drawable.draw(canvas);
    }

    public void setText(CharSequence charSequence) {
        setText(charSequence, true, true);
    }

    public void setText(CharSequence charSequence, boolean z) {
        setText(charSequence, z, true);
    }

    public void cancelAnimation() {
        this.drawable.cancelAnimation();
    }

    public boolean isAnimating() {
        return this.drawable.isAnimating();
    }

    public void setIgnoreRTL(boolean z) {
        this.drawable.ignoreRTL = z;
    }

    public void setText(CharSequence charSequence, boolean z, boolean z2) {
        boolean z3 = !this.first && z;
        this.first = false;
        if (z3) {
            if (this.drawable.allowCancel) {
                if (this.drawable.animator != null) {
                    this.drawable.animator.cancel();
                    this.drawable.animator = null;
                }
            } else if (this.drawable.isAnimating()) {
                this.toSetText = charSequence;
                this.toSetMoveDown = z2;
                return;
            }
        }
        this.drawable.setBounds(getPaddingLeft(), getPaddingTop(), this.lastMaxWidth - getPaddingRight(), getMeasuredHeight() - getPaddingBottom());
        this.drawable.setText(charSequence, z3, z2);
        float width = (int) this.drawable.getWidth();
        if (width < this.drawable.getWidth() || !(z3 || width == this.drawable.getWidth())) {
            requestLayout();
        }
    }

    public int width() {
        return getPaddingLeft() + ((int) Math.ceil(this.drawable.getCurrentWidth())) + getPaddingRight();
    }

    public CharSequence getText() {
        return this.drawable.getText();
    }

    public int getTextHeight() {
        return getPaint().getFontMetricsInt().descent - getPaint().getFontMetricsInt().ascent;
    }

    public void setTextSize(float f) {
        this.drawable.setTextSize(f);
    }

    public void setTextColor(int i) {
        this.drawable.setTextColor(i);
        invalidate();
    }

    public void setTextColor(int i, boolean z) {
        this.drawable.setTextColor(i, z);
        invalidate();
    }

    public void setEmojiCacheType(int i) {
        this.drawable.setEmojiCacheType(i);
    }

    public void setEmojiColor(int i) {
        this.drawable.setEmojiColor(i);
        invalidate();
    }

    public void setEmojiColorFilter(ColorFilter colorFilter) {
        this.drawable.setEmojiColorFilter(colorFilter);
        invalidate();
    }

    public int getTextColor() {
        return this.drawable.getTextColor();
    }

    public void setTypeface(Typeface typeface) {
        this.drawable.setTypeface(typeface);
    }

    public void setGravity(int i) {
        this.drawable.setGravity(i);
    }

    public void setAnimationProperties(float f, long j, long j2, TimeInterpolator timeInterpolator) {
        this.drawable.setAnimationProperties(f, j, j2, timeInterpolator);
    }

    public void setScaleProperty(float f) {
        this.drawable.setScaleProperty(f);
    }

    public AnimatedTextDrawable getDrawable() {
        return this.drawable;
    }

    public TextPaint getPaint() {
        return this.drawable.getPaint();
    }

    @Override // android.view.View, android.graphics.drawable.Drawable.Callback
    public void invalidateDrawable(Drawable drawable) {
        super.invalidateDrawable(drawable);
        invalidate();
    }

    @Override // android.view.View
    public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo accessibilityNodeInfo) {
        super.onInitializeAccessibilityNodeInfo(accessibilityNodeInfo);
        accessibilityNodeInfo.setClassName("android.widget.TextView");
        accessibilityNodeInfo.setText(getText());
    }

    public void setEllipsizeByGradient(boolean z) {
        this.drawable.setEllipsizeByGradient(z);
    }

    public void setRightPadding(float f) {
        this.drawable.setRightPadding(f);
    }

    public float getRightPadding() {
        return this.drawable.getRightPadding();
    }

    public void setOnWidthUpdatedListener(Runnable runnable) {
        this.drawable.setOnWidthUpdatedListener(runnable);
    }

    public void setIncludeFontPadding(boolean z) {
        this.drawable.setIncludeFontPadding(z);
    }
}
