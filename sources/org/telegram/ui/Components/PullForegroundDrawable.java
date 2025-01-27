package org.telegram.ui.Components;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.animation.LinearInterpolator;
import androidx.core.graphics.ColorUtils;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.ApplicationLoader;
import org.telegram.messenger.R;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Stories.recorder.HintView2;
import org.telegram.ui.TopicsFragment;

/* loaded from: classes3.dex */
public abstract class PullForegroundDrawable {
    private ValueAnimator accentRevalAnimatorIn;
    private ValueAnimator accentRevalAnimatorOut;
    private float accentRevalProgress;
    private float accentRevalProgressOut;
    private boolean animateOut;
    private boolean animateToColorize;
    private boolean animateToEndText;
    private boolean animateToTextIn;
    private boolean arrowAnimateTo;
    private final ArrowDrawable arrowDrawable;
    private ValueAnimator arrowRotateAnimator;
    private float arrowRotateProgress;
    private boolean bounceIn;
    private float bounceProgress;
    private View cell;
    private final Path circleClipPath;
    private Drawable generalTopicDrawable;
    private boolean isOut;
    private int lastWidth;
    private RecyclerListView listView;
    private AnimatorSet outAnimator;
    public float outCx;
    public float outCy;
    public float outImageSize;
    public float outOverScroll;
    public float outProgress;
    public float outRadius;
    public float pullProgress;
    private StaticLayout pullTooltipLayout;
    private float pullTooltipLayoutLeft;
    private float pullTooltipLayoutScale;
    private float pullTooltipLayoutWidth;
    private final CharSequence pullTooltipText;
    private StaticLayout releaseTooltipLayout;
    private float releaseTooltipLayoutLeft;
    private float releaseTooltipLayoutScale;
    private float releaseTooltipLayoutWidth;
    private final CharSequence releaseTooltipText;
    public int scrollDy;
    private float textInProgress;
    Runnable textInRunnable;
    private ValueAnimator.AnimatorUpdateListener textInUpdateListener;
    private ValueAnimator textIntAnimator;
    private float textSwappingProgress;
    private ValueAnimator.AnimatorUpdateListener textSwappingUpdateListener;
    private ValueAnimator textSwipingAnimator;
    private final TextPaint tooltipTextPaint;
    private float touchSlop;
    boolean wasSendCallback;
    private boolean willDraw;
    private int backgroundColorKey = Theme.key_chats_archivePullDownBackground;
    private int backgroundActiveColorKey = Theme.key_chats_archivePullDownBackgroundActive;
    private int avatarBackgroundColorKey = Theme.key_avatar_backgroundArchivedHidden;
    private boolean changeAvatarColor = true;
    private final Paint paintSecondary = new Paint(1);
    private final Paint paintWhite = new Paint(1);
    private final Paint paintBackgroundAccent = new Paint(1);
    private final Paint backgroundPaint = new Paint();
    private final RectF rectF = new RectF();

    private class ArrowDrawable extends Drawable {
        private float lastDensity;
        private Path path = new Path();
        private Paint paint = new Paint(1);

        public ArrowDrawable() {
            updatePath();
        }

        private void updatePath() {
            int dp = AndroidUtilities.dp(18.0f);
            this.path.reset();
            float f = dp >> 1;
            this.path.moveTo(f, AndroidUtilities.dpf2(4.98f));
            this.path.lineTo(AndroidUtilities.dpf2(4.95f), AndroidUtilities.dpf2(9.0f));
            this.path.lineTo(dp - AndroidUtilities.dpf2(4.95f), AndroidUtilities.dpf2(9.0f));
            this.path.lineTo(f, AndroidUtilities.dpf2(4.98f));
            this.paint.setStyle(Paint.Style.FILL_AND_STROKE);
            this.paint.setStrokeJoin(Paint.Join.ROUND);
            this.paint.setStrokeWidth(AndroidUtilities.dpf2(1.0f));
            this.lastDensity = AndroidUtilities.density;
        }

        @Override // android.graphics.drawable.Drawable
        public void draw(Canvas canvas) {
            if (this.lastDensity != AndroidUtilities.density) {
                updatePath();
            }
            canvas.save();
            canvas.translate(getBounds().left, getBounds().top);
            canvas.drawPath(this.path, this.paint);
            canvas.drawRect(AndroidUtilities.dpf2(7.56f), AndroidUtilities.dpf2(8.0f), AndroidUtilities.dp(18.0f) - AndroidUtilities.dpf2(7.56f), AndroidUtilities.dpf2(11.1f), this.paint);
            canvas.restore();
        }

        @Override // android.graphics.drawable.Drawable
        public int getIntrinsicHeight() {
            return AndroidUtilities.dp(18.0f);
        }

        @Override // android.graphics.drawable.Drawable
        public int getIntrinsicWidth() {
            return getIntrinsicHeight();
        }

        @Override // android.graphics.drawable.Drawable
        public int getOpacity() {
            return 0;
        }

        @Override // android.graphics.drawable.Drawable
        public void setAlpha(int i) {
        }

        public void setColor(int i) {
            this.paint.setColor(i);
        }

        @Override // android.graphics.drawable.Drawable
        public void setColorFilter(ColorFilter colorFilter) {
        }
    }

    public PullForegroundDrawable(CharSequence charSequence, CharSequence charSequence2) {
        TextPaint textPaint = new TextPaint(1);
        this.tooltipTextPaint = textPaint;
        this.arrowDrawable = new ArrowDrawable();
        this.circleClipPath = new Path();
        this.textSwappingProgress = 1.0f;
        this.arrowRotateProgress = 1.0f;
        this.accentRevalProgress = 1.0f;
        this.accentRevalProgressOut = 1.0f;
        this.pullTooltipLayoutScale = 1.0f;
        this.releaseTooltipLayoutScale = 1.0f;
        this.textSwappingUpdateListener = new ValueAnimator.AnimatorUpdateListener() { // from class: org.telegram.ui.Components.PullForegroundDrawable$$ExternalSyntheticLambda3
            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                PullForegroundDrawable.this.lambda$new$0(valueAnimator);
            }
        };
        this.textInUpdateListener = new ValueAnimator.AnimatorUpdateListener() { // from class: org.telegram.ui.Components.PullForegroundDrawable$$ExternalSyntheticLambda4
            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                PullForegroundDrawable.this.lambda$new$1(valueAnimator);
            }
        };
        this.textInRunnable = new Runnable() { // from class: org.telegram.ui.Components.PullForegroundDrawable.1
            @Override // java.lang.Runnable
            public void run() {
                PullForegroundDrawable.this.animateToTextIn = true;
                if (PullForegroundDrawable.this.textIntAnimator != null) {
                    PullForegroundDrawable.this.textIntAnimator.cancel();
                }
                PullForegroundDrawable.this.textInProgress = 0.0f;
                PullForegroundDrawable.this.textIntAnimator = ValueAnimator.ofFloat(0.0f, 1.0f);
                PullForegroundDrawable.this.textIntAnimator.addUpdateListener(PullForegroundDrawable.this.textInUpdateListener);
                PullForegroundDrawable.this.textIntAnimator.setInterpolator(new LinearInterpolator());
                PullForegroundDrawable.this.textIntAnimator.setDuration(150L);
                PullForegroundDrawable.this.textIntAnimator.start();
            }
        };
        this.wasSendCallback = false;
        textPaint.setTypeface(AndroidUtilities.bold());
        textPaint.setTextSize(AndroidUtilities.dp(16.0f));
        this.touchSlop = ViewConfiguration.get(ApplicationLoader.applicationContext).getScaledTouchSlop();
        this.pullTooltipText = charSequence;
        this.releaseTooltipText = charSequence2;
        try {
            this.generalTopicDrawable = ApplicationLoader.applicationContext.getResources().getDrawable(R.drawable.msg_filled_general).mutate();
        } catch (Exception unused) {
        }
    }

    private void checkTextLayouts(int i) {
        if (i != this.lastWidth) {
            this.pullTooltipLayout = new StaticLayout(this.pullTooltipText, this.tooltipTextPaint, AndroidUtilities.displaySize.x, Layout.Alignment.ALIGN_CENTER, 1.0f, 0.0f, false);
            float f = 0.0f;
            for (int i2 = 0; i2 < this.pullTooltipLayout.getLineCount(); i2++) {
                f = Math.max(f, this.pullTooltipLayout.getLineWidth(i2));
            }
            float f2 = i;
            this.pullTooltipLayoutScale = Math.min(1.0f, f2 / f);
            int ceil = (int) Math.ceil(f);
            if (this.pullTooltipLayoutScale < 0.8f) {
                this.pullTooltipLayoutScale = 0.8f;
                ceil = HintView2.cutInFancyHalf(this.pullTooltipText, this.tooltipTextPaint);
            }
            this.pullTooltipLayout = new StaticLayout(this.pullTooltipText, this.tooltipTextPaint, ceil, Layout.Alignment.ALIGN_CENTER, 1.0f, 0.0f, false);
            this.pullTooltipLayoutLeft = ceil;
            this.pullTooltipLayoutWidth = 0.0f;
            for (int i3 = 0; i3 < this.pullTooltipLayout.getLineCount(); i3++) {
                this.pullTooltipLayoutLeft = Math.min(this.pullTooltipLayoutLeft, this.pullTooltipLayout.getLineLeft(i3));
                this.pullTooltipLayoutWidth = Math.max(this.pullTooltipLayoutWidth, this.pullTooltipLayout.getLineWidth(i3));
            }
            this.releaseTooltipLayout = new StaticLayout(this.releaseTooltipText, this.tooltipTextPaint, AndroidUtilities.displaySize.x, Layout.Alignment.ALIGN_CENTER, 1.0f, 0.0f, false);
            float f3 = 0.0f;
            for (int i4 = 0; i4 < this.releaseTooltipLayout.getLineCount(); i4++) {
                f3 = Math.max(f3, this.releaseTooltipLayout.getLineWidth(i4));
            }
            this.releaseTooltipLayoutScale = Math.min(1.0f, f2 / f3);
            int ceil2 = (int) Math.ceil(f3);
            if (this.releaseTooltipLayoutScale < 0.8f) {
                this.releaseTooltipLayoutScale = 0.8f;
                ceil2 = HintView2.cutInFancyHalf(this.releaseTooltipText, this.tooltipTextPaint);
            }
            this.releaseTooltipLayout = new StaticLayout(this.releaseTooltipText, this.tooltipTextPaint, ceil2, Layout.Alignment.ALIGN_CENTER, 1.0f, 0.0f, false);
            this.releaseTooltipLayoutLeft = ceil2;
            this.releaseTooltipLayoutWidth = 0.0f;
            for (int i5 = 0; i5 < this.releaseTooltipLayout.getLineCount(); i5++) {
                this.releaseTooltipLayoutLeft = Math.min(this.releaseTooltipLayoutLeft, this.releaseTooltipLayout.getLineLeft(i5));
                this.releaseTooltipLayoutWidth = Math.max(this.releaseTooltipLayoutWidth, this.releaseTooltipLayout.getLineWidth(i5));
            }
            this.lastWidth = i;
        }
    }

    public static int getMaxOverscroll() {
        return AndroidUtilities.dp(72.0f);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$colorize$3(ValueAnimator valueAnimator) {
        this.accentRevalProgress = ((Float) valueAnimator.getAnimatedValue()).floatValue();
        View view = this.cell;
        if (view != null) {
            view.invalidate();
        }
        RecyclerListView recyclerListView = this.listView;
        if (recyclerListView != null) {
            recyclerListView.invalidate();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$colorize$4(ValueAnimator valueAnimator) {
        this.accentRevalProgressOut = ((Float) valueAnimator.getAnimatedValue()).floatValue();
        View view = this.cell;
        if (view != null) {
            view.invalidate();
        }
        RecyclerListView recyclerListView = this.listView;
        if (recyclerListView != null) {
            recyclerListView.invalidate();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0(ValueAnimator valueAnimator) {
        this.textSwappingProgress = ((Float) valueAnimator.getAnimatedValue()).floatValue();
        View view = this.cell;
        if (view != null) {
            view.invalidate();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$1(ValueAnimator valueAnimator) {
        this.textInProgress = ((Float) valueAnimator.getAnimatedValue()).floatValue();
        View view = this.cell;
        if (view != null) {
            view.invalidate();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$startOutAnimation$5(ValueAnimator valueAnimator) {
        setOutProgress(((Float) valueAnimator.getAnimatedValue()).floatValue());
        View view = this.cell;
        if (view != null) {
            view.invalidate();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$startOutAnimation$6(ValueAnimator valueAnimator) {
        this.bounceProgress = ((Float) valueAnimator.getAnimatedValue()).floatValue();
        this.bounceIn = true;
        View view = this.cell;
        if (view != null) {
            view.invalidate();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$startOutAnimation$7(ValueAnimator valueAnimator) {
        this.bounceProgress = ((Float) valueAnimator.getAnimatedValue()).floatValue();
        this.bounceIn = false;
        View view = this.cell;
        if (view != null) {
            view.invalidate();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$updateTextProgress$2(ValueAnimator valueAnimator) {
        this.arrowRotateProgress = ((Float) valueAnimator.getAnimatedValue()).floatValue();
        View view = this.cell;
        if (view != null) {
            view.invalidate();
        }
    }

    private void setOutProgress(float f) {
        this.outProgress = f;
        int blendARGB = ColorUtils.blendARGB(Theme.getNonAnimatedColor(this.avatarBackgroundColorKey), Theme.getNonAnimatedColor(this.backgroundActiveColorKey), 1.0f - this.outProgress);
        this.paintBackgroundAccent.setColor(blendARGB);
        if (this.changeAvatarColor && isDraw()) {
            Theme.dialogs_archiveAvatarDrawable.beginApplyLayerColors();
            Theme.dialogs_archiveAvatarDrawable.setLayerColor("Arrow1.**", blendARGB);
            Theme.dialogs_archiveAvatarDrawable.setLayerColor("Arrow2.**", blendARGB);
            Theme.dialogs_archiveAvatarDrawable.commitApplyLayerColors();
            Theme.dialogs_archiveAvatarDrawableRecolored = true;
        }
    }

    private void textIn() {
        if (this.animateToTextIn) {
            return;
        }
        if (Math.abs(this.scrollDy) >= this.touchSlop * 0.5f) {
            this.wasSendCallback = true;
            this.cell.removeCallbacks(this.textInRunnable);
            this.cell.postDelayed(this.textInRunnable, 200L);
        } else {
            if (this.wasSendCallback) {
                return;
            }
            this.textInProgress = 1.0f;
            this.animateToTextIn = true;
        }
    }

    private void updateTextProgress(float f) {
        boolean z = f > 0.85f;
        if (this.animateToEndText != z) {
            this.animateToEndText = z;
            if (this.textInProgress == 0.0f) {
                ValueAnimator valueAnimator = this.textSwipingAnimator;
                if (valueAnimator != null) {
                    valueAnimator.cancel();
                }
                this.textSwappingProgress = z ? 0.0f : 1.0f;
            } else {
                ValueAnimator valueAnimator2 = this.textSwipingAnimator;
                if (valueAnimator2 != null) {
                    valueAnimator2.cancel();
                }
                ValueAnimator ofFloat = ValueAnimator.ofFloat(this.textSwappingProgress, z ? 0.0f : 1.0f);
                this.textSwipingAnimator = ofFloat;
                ofFloat.addUpdateListener(this.textSwappingUpdateListener);
                this.textSwipingAnimator.setInterpolator(new LinearInterpolator());
                this.textSwipingAnimator.setDuration(170L);
                this.textSwipingAnimator.start();
            }
        }
        if (z != this.arrowAnimateTo) {
            this.arrowAnimateTo = z;
            ValueAnimator valueAnimator3 = this.arrowRotateAnimator;
            if (valueAnimator3 != null) {
                valueAnimator3.cancel();
            }
            ValueAnimator ofFloat2 = ValueAnimator.ofFloat(this.arrowRotateProgress, this.arrowAnimateTo ? 0.0f : 1.0f);
            this.arrowRotateAnimator = ofFloat2;
            ofFloat2.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: org.telegram.ui.Components.PullForegroundDrawable$$ExternalSyntheticLambda2
                @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                public final void onAnimationUpdate(ValueAnimator valueAnimator4) {
                    PullForegroundDrawable.this.lambda$updateTextProgress$2(valueAnimator4);
                }
            });
            this.arrowRotateAnimator.setInterpolator(CubicBezierInterpolator.EASE_BOTH);
            this.arrowRotateAnimator.setDuration(250L);
            this.arrowRotateAnimator.start();
        }
    }

    public void colorize(boolean z) {
        ValueAnimator valueAnimator;
        if (this.animateToColorize != z) {
            this.animateToColorize = z;
            if (z) {
                ValueAnimator valueAnimator2 = this.accentRevalAnimatorIn;
                if (valueAnimator2 != null) {
                    valueAnimator2.cancel();
                    this.accentRevalAnimatorIn = null;
                }
                this.accentRevalProgress = 0.0f;
                ValueAnimator ofFloat = ValueAnimator.ofFloat(0.0f, 1.0f);
                this.accentRevalAnimatorIn = ofFloat;
                ofFloat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: org.telegram.ui.Components.PullForegroundDrawable$$ExternalSyntheticLambda0
                    @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                    public final void onAnimationUpdate(ValueAnimator valueAnimator3) {
                        PullForegroundDrawable.this.lambda$colorize$3(valueAnimator3);
                    }
                });
                this.accentRevalAnimatorIn.setInterpolator(AndroidUtilities.accelerateInterpolator);
                this.accentRevalAnimatorIn.setDuration(230L);
                valueAnimator = this.accentRevalAnimatorIn;
            } else {
                ValueAnimator valueAnimator3 = this.accentRevalAnimatorOut;
                if (valueAnimator3 != null) {
                    valueAnimator3.cancel();
                    this.accentRevalAnimatorOut = null;
                }
                this.accentRevalProgressOut = 0.0f;
                ValueAnimator ofFloat2 = ValueAnimator.ofFloat(0.0f, 1.0f);
                this.accentRevalAnimatorOut = ofFloat2;
                ofFloat2.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: org.telegram.ui.Components.PullForegroundDrawable$$ExternalSyntheticLambda1
                    @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                    public final void onAnimationUpdate(ValueAnimator valueAnimator4) {
                        PullForegroundDrawable.this.lambda$colorize$4(valueAnimator4);
                    }
                });
                this.accentRevalAnimatorOut.setInterpolator(AndroidUtilities.accelerateInterpolator);
                this.accentRevalAnimatorOut.setDuration(230L);
                valueAnimator = this.accentRevalAnimatorOut;
            }
            valueAnimator.start();
        }
    }

    public void doNotShow() {
        ValueAnimator valueAnimator = this.textSwipingAnimator;
        if (valueAnimator != null) {
            valueAnimator.cancel();
        }
        ValueAnimator valueAnimator2 = this.textIntAnimator;
        if (valueAnimator2 != null) {
            valueAnimator2.cancel();
        }
        View view = this.cell;
        if (view != null) {
            view.removeCallbacks(this.textInRunnable);
        }
        ValueAnimator valueAnimator3 = this.accentRevalAnimatorIn;
        if (valueAnimator3 != null) {
            valueAnimator3.cancel();
        }
        this.textSwappingProgress = 1.0f;
        this.arrowRotateProgress = 1.0f;
        this.animateToEndText = false;
        this.arrowAnimateTo = false;
        this.animateToTextIn = false;
        this.wasSendCallback = false;
        this.textInProgress = 0.0f;
        this.isOut = true;
        setOutProgress(1.0f);
        this.animateToColorize = false;
        this.accentRevalProgress = 0.0f;
    }

    public void draw(Canvas canvas) {
        draw(canvas, false);
    }

    /* JADX WARN: Removed duplicated region for block: B:120:0x023c  */
    /* JADX WARN: Removed duplicated region for block: B:50:0x01ef  */
    /* JADX WARN: Removed duplicated region for block: B:56:0x0242  */
    /* JADX WARN: Removed duplicated region for block: B:59:0x0246  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void draw(Canvas canvas, boolean z) {
        View view;
        int i;
        float f;
        int i2;
        int i3;
        int i4;
        float f2;
        float f3;
        float f4;
        float width;
        Paint paint;
        int i5;
        int i6;
        float f5;
        float f6;
        int i7;
        float f7;
        RectF rectF;
        float f8;
        float height;
        float f9;
        int height2;
        if (!this.willDraw || this.isOut || (view = this.cell) == null || this.listView == null) {
            return;
        }
        boolean z2 = view instanceof TopicsFragment.TopicDialogCell;
        int dp = AndroidUtilities.dp(z2 ? 15.0f : 28.0f);
        int dp2 = AndroidUtilities.dp(8.0f);
        int dp3 = AndroidUtilities.dp(9.0f);
        int dp4 = AndroidUtilities.dp(18.0f);
        int viewOffset = (int) getViewOffset();
        int height3 = (int) (this.cell.getHeight() * this.pullProgress);
        float f10 = this.bounceIn ? (this.bounceProgress * 0.07f) - 0.05f : this.bounceProgress * 0.02f;
        checkTextLayouts((this.cell.getWidth() - (dp * 4)) - AndroidUtilities.dp(16.0f));
        updateTextProgress(this.pullProgress);
        float f11 = this.outProgress * 2.0f;
        if (f11 > 1.0f) {
            f11 = 1.0f;
        }
        float f12 = this.outCx;
        float f13 = this.outCy;
        if (z) {
            f13 += viewOffset;
        }
        int i8 = dp + dp3;
        int measuredHeight = (this.cell.getMeasuredHeight() - dp2) - dp3;
        if (z) {
            measuredHeight += viewOffset;
        }
        int i9 = dp4 + (dp2 * 2);
        if (height3 > i9) {
            i = dp3;
            f = 1.0f;
        } else {
            i = dp3;
            f = height3 / i9;
        }
        canvas.save();
        if (z) {
            i2 = dp4;
            i4 = dp2;
            i3 = viewOffset;
            canvas.clipRect(0, -AndroidUtilities.dp(4.0f), this.listView.getMeasuredWidth(), viewOffset + 1);
        } else {
            i2 = dp4;
            i3 = viewOffset;
            i4 = dp2;
        }
        if (this.outProgress == 0.0f) {
            if (this.accentRevalProgress != 1.0f && this.accentRevalProgressOut != 1.0f) {
                canvas.drawPaint(this.backgroundPaint);
            }
            f2 = f10;
        } else {
            float f14 = this.outRadius;
            float width2 = f14 + (f14 * f10) + ((this.cell.getWidth() - this.outRadius) * (1.0f - this.outProgress));
            if (this.accentRevalProgress != 1.0f && this.accentRevalProgressOut != 1.0f) {
                canvas.drawCircle(f12, f13, width2, this.backgroundPaint);
            }
            this.circleClipPath.reset();
            f2 = f10;
            this.rectF.set(f12 - width2, f13 - width2, f12 + width2, width2 + f13);
            this.circleClipPath.addOval(this.rectF, Path.Direction.CW);
            canvas.clipPath(this.circleClipPath);
        }
        if (this.animateToColorize) {
            if (this.accentRevalProgressOut > this.accentRevalProgress) {
                canvas.save();
                float f15 = i8;
                float f16 = this.outProgress;
                float f17 = measuredHeight;
                canvas.translate((f12 - f15) * f16, (f13 - f17) * f16);
                canvas.drawCircle(f15, f17, this.cell.getWidth() * this.accentRevalProgressOut, this.backgroundPaint);
                canvas.restore();
            }
            if (this.accentRevalProgress > 0.0f) {
                canvas.save();
                f3 = i8;
                float f18 = this.outProgress;
                f4 = measuredHeight;
                canvas.translate((f12 - f3) * f18, (f13 - f4) * f18);
                width = this.cell.getWidth() * this.accentRevalProgress;
                paint = this.paintBackgroundAccent;
                canvas.drawCircle(f3, f4, width, paint);
                canvas.restore();
            }
            if (height3 <= i9) {
                this.paintSecondary.setAlpha((int) ((1.0f - f11) * 0.4f * f * 255.0f));
                if (z) {
                    rectF = this.rectF;
                    f8 = dp;
                    i6 = i4;
                    height = i6;
                    f9 = dp + i2;
                    height2 = i6 + i3 + i;
                } else {
                    i6 = i4;
                    rectF = this.rectF;
                    f8 = dp;
                    height = ((this.cell.getHeight() - height3) + i6) - i3;
                    f9 = dp + i2;
                    height2 = this.cell.getHeight() - i6;
                }
                rectF.set(f8, height, f9, height2);
                i5 = i;
                float f19 = i5;
                canvas.drawRoundRect(this.rectF, f19, f19, this.paintSecondary);
            } else {
                i5 = i;
                i6 = i4;
            }
            if (!z) {
                canvas.restore();
                return;
            }
            if (z2) {
                measuredHeight = (int) (measuredHeight - ((this.cell.getMeasuredHeight() - AndroidUtilities.dp(41.0f)) * this.outProgress));
            }
            float f20 = this.outProgress;
            if (f20 == 0.0f || z2) {
                this.paintWhite.setAlpha((int) (f * 255.0f * (1.0f - f20)));
                float f21 = i8;
                float f22 = measuredHeight;
                canvas.drawCircle(f21, f22, i5, this.paintWhite);
                int intrinsicHeight = this.arrowDrawable.getIntrinsicHeight();
                int intrinsicWidth = this.arrowDrawable.getIntrinsicWidth() >> 1;
                f5 = f12;
                int i10 = intrinsicHeight >> 1;
                f6 = f13;
                this.arrowDrawable.setBounds(i8 - intrinsicWidth, measuredHeight - i10, intrinsicWidth + i8, measuredHeight + i10);
                float f23 = 1.0f - this.arrowRotateProgress;
                if (f23 < 0.0f) {
                    f23 = 0.0f;
                }
                float f24 = 1.0f - f23;
                canvas.save();
                canvas.rotate(180.0f * f24, f21, f22);
                canvas.translate(0.0f, (AndroidUtilities.dpf2(1.0f) * 1.0f) - f24);
                this.arrowDrawable.setColor(this.animateToColorize ? this.paintBackgroundAccent.getColor() : Theme.getColor(this.backgroundColorKey));
                this.arrowDrawable.setAlpha((int) ((1.0f - this.outProgress) * 255.0f));
                this.arrowDrawable.draw(canvas);
                canvas.restore();
            } else {
                f5 = f12;
                f6 = f13;
            }
            if (this.pullProgress > 0.0f) {
                textIn();
            }
            float height4 = (this.cell.getHeight() - (i9 / 2.0f)) + AndroidUtilities.dp(6.0f);
            float width3 = (this.cell.getWidth() + (z2 ? dp * 2 : 0)) / 2.0f;
            if (this.pullTooltipLayout != null) {
                float f25 = this.textSwappingProgress;
                if (f25 > 0.0f && f25 < 1.0f) {
                    canvas.save();
                    float f26 = (this.textSwappingProgress * 0.2f) + 0.8f;
                    canvas.scale(f26, f26, width3, (AndroidUtilities.dp(16.0f) * (1.0f - this.textSwappingProgress)) + height4);
                }
                i7 = i8;
                f7 = f5;
                canvas.saveLayerAlpha(0.0f, 0.0f, this.cell.getMeasuredWidth(), this.cell.getMeasuredHeight(), (int) (this.textSwappingProgress * 255.0f * f * this.textInProgress), 31);
                canvas.translate((width3 - this.pullTooltipLayoutLeft) - (this.pullTooltipLayoutWidth / 2.0f), ((AndroidUtilities.dp(8.0f) * (1.0f - this.textSwappingProgress)) + height4) - this.pullTooltipLayout.getHeight());
                float f27 = this.pullTooltipLayoutScale;
                canvas.scale(f27, f27, this.pullTooltipLayoutLeft + (this.pullTooltipLayoutWidth / 2.0f), this.pullTooltipLayout.getHeight());
                this.pullTooltipLayout.draw(canvas);
                canvas.restore();
                float f28 = this.textSwappingProgress;
                if (f28 > 0.0f && f28 < 1.0f) {
                    canvas.restore();
                }
            } else {
                i7 = i8;
                f7 = f5;
            }
            if (this.releaseTooltipLayout != null) {
                float f29 = this.textSwappingProgress;
                if (f29 > 0.0f && f29 < 1.0f) {
                    canvas.save();
                    float f30 = ((1.0f - this.textSwappingProgress) * 0.1f) + 0.9f;
                    canvas.scale(f30, f30, width3, height4 - (AndroidUtilities.dp(8.0f) * this.textSwappingProgress));
                }
                canvas.saveLayerAlpha(0.0f, 0.0f, this.cell.getMeasuredWidth(), this.cell.getMeasuredHeight(), (int) ((1.0f - this.textSwappingProgress) * 255.0f * f * this.textInProgress), 31);
                canvas.translate((width3 - this.releaseTooltipLayoutLeft) - (this.releaseTooltipLayoutWidth / 2.0f), (height4 + (AndroidUtilities.dp(8.0f) * this.textSwappingProgress)) - this.releaseTooltipLayout.getHeight());
                float f31 = this.releaseTooltipLayoutScale;
                canvas.scale(f31, f31, this.releaseTooltipLayoutLeft + (this.releaseTooltipLayoutWidth / 2.0f), this.releaseTooltipLayout.getHeight());
                this.releaseTooltipLayout.draw(canvas);
                canvas.restore();
                float f32 = this.textSwappingProgress;
                if (f32 > 0.0f && f32 < 1.0f) {
                    canvas.restore();
                }
            }
            canvas.restore();
            if (z2 || !this.changeAvatarColor || this.outProgress <= 0.0f) {
                return;
            }
            canvas.save();
            int intrinsicWidth2 = Theme.dialogs_archiveAvatarDrawable.getIntrinsicWidth();
            int height5 = (this.cell.getHeight() - i6) - i5;
            float f33 = intrinsicWidth2;
            float dp5 = AndroidUtilities.dp(24.0f) / f33;
            float f34 = this.outProgress;
            float f35 = dp5 + ((1.0f - dp5) * f34) + f2;
            float f36 = f7;
            float f37 = i7 - f36;
            float f38 = 1.0f - f34;
            canvas.translate(f37 * f38, (height5 - f6) * f38);
            float f39 = f6;
            canvas.scale(f35, f35, f36, f39);
            Theme.dialogs_archiveAvatarDrawable.setProgress(0.0f);
            if (!Theme.dialogs_archiveAvatarDrawableRecolored) {
                Theme.dialogs_archiveAvatarDrawable.beginApplyLayerColors();
                Theme.dialogs_archiveAvatarDrawable.setLayerColor("Arrow1.**", Theme.getNonAnimatedColor(this.avatarBackgroundColorKey));
                Theme.dialogs_archiveAvatarDrawable.setLayerColor("Arrow2.**", Theme.getNonAnimatedColor(this.avatarBackgroundColorKey));
                Theme.dialogs_archiveAvatarDrawable.commitApplyLayerColors();
                Theme.dialogs_archiveAvatarDrawableRecolored = true;
            }
            float f40 = f33 / 2.0f;
            Theme.dialogs_archiveAvatarDrawable.setBounds((int) (f36 - f40), (int) (f39 - f40), (int) (f36 + f40), (int) (f39 + f40));
            Theme.dialogs_archiveAvatarDrawable.draw(canvas);
            canvas.restore();
            return;
        }
        if (this.accentRevalProgress > this.accentRevalProgressOut) {
            canvas.save();
            float f41 = i8;
            float f42 = this.outProgress;
            float f43 = measuredHeight;
            canvas.translate((f12 - f41) * f42, (f13 - f43) * f42);
            canvas.drawCircle(f41, f43, this.cell.getWidth() * this.accentRevalProgress, this.paintBackgroundAccent);
            canvas.restore();
        }
        if (this.accentRevalProgressOut > 0.0f) {
            canvas.save();
            f3 = i8;
            float f44 = this.outProgress;
            f4 = measuredHeight;
            canvas.translate((f12 - f3) * f44, (f13 - f4) * f44);
            width = this.cell.getWidth() * this.accentRevalProgressOut;
            paint = this.backgroundPaint;
            canvas.drawCircle(f3, f4, width, paint);
            canvas.restore();
        }
        if (height3 <= i9) {
        }
        if (!z) {
        }
    }

    public void drawOverScroll(Canvas canvas) {
        draw(canvas, true);
    }

    protected abstract float getViewOffset();

    public boolean isDraw() {
        return this.willDraw && !this.isOut;
    }

    public void resetText() {
        ValueAnimator valueAnimator = this.textIntAnimator;
        if (valueAnimator != null) {
            valueAnimator.cancel();
        }
        View view = this.cell;
        if (view != null) {
            view.removeCallbacks(this.textInRunnable);
        }
        this.textInProgress = 0.0f;
        this.animateToTextIn = false;
        this.wasSendCallback = false;
    }

    public void setCell(View view) {
        this.cell = view;
        updateColors();
    }

    public void setListView(RecyclerListView recyclerListView) {
        this.listView = recyclerListView;
    }

    public void setWillDraw(boolean z) {
        this.willDraw = z;
    }

    public void showHidden() {
        AnimatorSet animatorSet = this.outAnimator;
        if (animatorSet != null) {
            animatorSet.removeAllListeners();
            this.outAnimator.cancel();
        }
        setOutProgress(0.0f);
        this.isOut = false;
        this.animateOut = false;
    }

    public void startOutAnimation() {
        if (this.animateOut || this.listView == null) {
            return;
        }
        AnimatorSet animatorSet = this.outAnimator;
        if (animatorSet != null) {
            animatorSet.removeAllListeners();
            this.outAnimator.cancel();
        }
        this.animateOut = true;
        this.bounceIn = true;
        this.bounceProgress = 0.0f;
        this.outOverScroll = this.listView.getTranslationY() / AndroidUtilities.dp(100.0f);
        ValueAnimator ofFloat = ValueAnimator.ofFloat(0.0f, 1.0f);
        ofFloat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: org.telegram.ui.Components.PullForegroundDrawable$$ExternalSyntheticLambda5
            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                PullForegroundDrawable.this.lambda$startOutAnimation$5(valueAnimator);
            }
        });
        ofFloat.setInterpolator(CubicBezierInterpolator.EASE_OUT_QUINT);
        ofFloat.setDuration(250L);
        ValueAnimator ofFloat2 = ValueAnimator.ofFloat(0.0f, 1.0f);
        ofFloat2.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: org.telegram.ui.Components.PullForegroundDrawable$$ExternalSyntheticLambda6
            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                PullForegroundDrawable.this.lambda$startOutAnimation$6(valueAnimator);
            }
        });
        CubicBezierInterpolator cubicBezierInterpolator = CubicBezierInterpolator.EASE_BOTH;
        ofFloat2.setInterpolator(cubicBezierInterpolator);
        ofFloat2.setDuration(150L);
        ValueAnimator ofFloat3 = ValueAnimator.ofFloat(1.0f, 0.0f);
        ofFloat3.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: org.telegram.ui.Components.PullForegroundDrawable$$ExternalSyntheticLambda7
            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                PullForegroundDrawable.this.lambda$startOutAnimation$7(valueAnimator);
            }
        });
        ofFloat3.setInterpolator(cubicBezierInterpolator);
        ofFloat3.setDuration(135L);
        AnimatorSet animatorSet2 = new AnimatorSet();
        this.outAnimator = animatorSet2;
        animatorSet2.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.Components.PullForegroundDrawable.2
            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationEnd(Animator animator) {
                PullForegroundDrawable.this.doNotShow();
            }
        });
        AnimatorSet animatorSet3 = new AnimatorSet();
        animatorSet3.playSequentially(ofFloat2, ofFloat3);
        animatorSet3.setStartDelay(180L);
        this.outAnimator.playTogether(ofFloat, animatorSet3);
        this.outAnimator.start();
    }

    public void updateColors() {
        int color = Theme.getColor(this.backgroundColorKey);
        this.tooltipTextPaint.setColor(-1);
        this.paintWhite.setColor(-1);
        this.paintSecondary.setColor(ColorUtils.setAlphaComponent(-1, 100));
        this.backgroundPaint.setColor(color);
        this.arrowDrawable.setColor(color);
        this.paintBackgroundAccent.setColor(Theme.getColor(this.avatarBackgroundColorKey));
    }
}
