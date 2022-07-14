package org.telegram.ui.Components;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.ui.ActionBar.Theme;
/* loaded from: classes5.dex */
public class RadialProgress {
    private static DecelerateInterpolator decelerateInterpolator;
    private Drawable checkBackgroundDrawable;
    private CheckDrawable checkDrawable;
    private Drawable currentDrawable;
    private Drawable currentMiniDrawable;
    private boolean currentMiniWithRound;
    private boolean currentWithRound;
    private boolean drawMiniProgress;
    private boolean hideCurrentDrawable;
    private Bitmap miniDrawBitmap;
    private Canvas miniDrawCanvas;
    private Paint miniProgressBackgroundPaint;
    private Paint miniProgressPaint;
    private View parent;
    private boolean previousCheckDrawable;
    private Drawable previousDrawable;
    private Drawable previousMiniDrawable;
    private boolean previousMiniWithRound;
    private boolean previousWithRound;
    private Paint progressPaint;
    private long lastUpdateTime = 0;
    private float radOffset = 0.0f;
    private float currentProgress = 0.0f;
    private float animationProgressStart = 0.0f;
    private long currentProgressTime = 0;
    private float animatedProgressValue = 0.0f;
    private RectF progressRect = new RectF();
    private RectF cicleRect = new RectF();
    private float animatedAlphaValue = 1.0f;
    private int progressColor = -1;
    private int diff = AndroidUtilities.dp(4.0f);
    private boolean alphaForPrevious = true;
    private boolean alphaForMiniPrevious = true;
    private float overrideAlpha = 1.0f;

    /* loaded from: classes5.dex */
    public class CheckDrawable extends Drawable {
        private Paint paint;
        private float progress;

        public CheckDrawable() {
            RadialProgress.this = r2;
            Paint paint = new Paint(1);
            this.paint = paint;
            paint.setStyle(Paint.Style.STROKE);
            this.paint.setStrokeWidth(AndroidUtilities.dp(3.0f));
            this.paint.setStrokeCap(Paint.Cap.ROUND);
            this.paint.setColor(-1);
        }

        public void resetProgress(boolean animated) {
            this.progress = animated ? 0.0f : 1.0f;
        }

        public boolean updateAnimation(long dt) {
            float f = this.progress;
            if (f < 1.0f) {
                float f2 = f + (((float) dt) / 700.0f);
                this.progress = f2;
                if (f2 > 1.0f) {
                    this.progress = 1.0f;
                    return true;
                }
                return true;
            }
            return false;
        }

        @Override // android.graphics.drawable.Drawable
        public void draw(Canvas canvas) {
            int x = getBounds().centerX() - AndroidUtilities.dp(12.0f);
            int y = getBounds().centerY() - AndroidUtilities.dp(6.0f);
            float f = 1.0f;
            if (this.progress != 1.0f) {
                f = RadialProgress.decelerateInterpolator.getInterpolation(this.progress);
            }
            float p = f;
            int endX = (int) (AndroidUtilities.dp(7.0f) - (AndroidUtilities.dp(6.0f) * p));
            int endY = (int) (AndroidUtilities.dpf2(13.0f) - (AndroidUtilities.dp(6.0f) * p));
            canvas.drawLine(AndroidUtilities.dp(7.0f) + x, ((int) AndroidUtilities.dpf2(13.0f)) + y, x + endX, y + endY, this.paint);
            int endX2 = (int) (AndroidUtilities.dpf2(7.0f) + (AndroidUtilities.dp(13.0f) * p));
            int endY2 = (int) (AndroidUtilities.dpf2(13.0f) - (AndroidUtilities.dp(13.0f) * p));
            canvas.drawLine(((int) AndroidUtilities.dpf2(7.0f)) + x, ((int) AndroidUtilities.dpf2(13.0f)) + y, x + endX2, y + endY2, this.paint);
        }

        @Override // android.graphics.drawable.Drawable
        public void setAlpha(int alpha) {
            this.paint.setAlpha(alpha);
        }

        @Override // android.graphics.drawable.Drawable
        public void setColorFilter(ColorFilter cf) {
            this.paint.setColorFilter(cf);
        }

        @Override // android.graphics.drawable.Drawable
        public int getOpacity() {
            return -2;
        }

        @Override // android.graphics.drawable.Drawable
        public int getIntrinsicWidth() {
            return AndroidUtilities.dp(48.0f);
        }

        @Override // android.graphics.drawable.Drawable
        public int getIntrinsicHeight() {
            return AndroidUtilities.dp(48.0f);
        }
    }

    public RadialProgress(View parentView) {
        if (decelerateInterpolator == null) {
            decelerateInterpolator = new DecelerateInterpolator();
        }
        Paint paint = new Paint(1);
        this.progressPaint = paint;
        paint.setStyle(Paint.Style.STROKE);
        this.progressPaint.setStrokeCap(Paint.Cap.ROUND);
        this.progressPaint.setStrokeWidth(AndroidUtilities.dp(3.0f));
        Paint paint2 = new Paint(1);
        this.miniProgressPaint = paint2;
        paint2.setStyle(Paint.Style.STROKE);
        this.miniProgressPaint.setStrokeCap(Paint.Cap.ROUND);
        this.miniProgressPaint.setStrokeWidth(AndroidUtilities.dp(2.0f));
        this.miniProgressBackgroundPaint = new Paint(1);
        this.parent = parentView;
    }

    public void setStrokeWidth(int width) {
        this.progressPaint.setStrokeWidth(width);
    }

    public void setProgressRect(int left, int top, int right, int bottom) {
        this.progressRect.set(left, top, right, bottom);
    }

    public RectF getProgressRect() {
        return this.progressRect;
    }

    public void setAlphaForPrevious(boolean value) {
        this.alphaForPrevious = value;
    }

    public void setAlphaForMiniPrevious(boolean value) {
        this.alphaForMiniPrevious = value;
    }

    private void updateAnimation(boolean progress) {
        long newTime = System.currentTimeMillis();
        long dt = newTime - this.lastUpdateTime;
        this.lastUpdateTime = newTime;
        Drawable drawable = this.checkBackgroundDrawable;
        if (drawable != null && ((this.currentDrawable == drawable || this.previousDrawable == drawable) && this.checkDrawable.updateAnimation(dt))) {
            invalidateParent();
        }
        boolean z = true;
        if (progress) {
            if (this.animatedProgressValue != 1.0f) {
                this.radOffset += ((float) (360 * dt)) / 3000.0f;
                float f = this.currentProgress;
                float f2 = this.animationProgressStart;
                float progressDiff = f - f2;
                if (progressDiff > 0.0f) {
                    long j = this.currentProgressTime + dt;
                    this.currentProgressTime = j;
                    if (j < 300) {
                        this.animatedProgressValue = f2 + (decelerateInterpolator.getInterpolation(((float) j) / 300.0f) * progressDiff);
                    } else {
                        this.animatedProgressValue = f;
                        this.animationProgressStart = f;
                        this.currentProgressTime = 0L;
                    }
                }
                invalidateParent();
            }
            if (this.drawMiniProgress) {
                if (this.animatedProgressValue >= 1.0f && this.previousMiniDrawable != null) {
                    float f3 = this.animatedAlphaValue - (((float) dt) / 200.0f);
                    this.animatedAlphaValue = f3;
                    if (f3 <= 0.0f) {
                        this.animatedAlphaValue = 0.0f;
                        this.previousMiniDrawable = null;
                        if (this.currentMiniDrawable == null) {
                            z = false;
                        }
                        this.drawMiniProgress = z;
                    }
                    invalidateParent();
                }
            } else if (this.animatedProgressValue >= 1.0f && this.previousDrawable != null) {
                float f4 = this.animatedAlphaValue - (((float) dt) / 200.0f);
                this.animatedAlphaValue = f4;
                if (f4 <= 0.0f) {
                    this.animatedAlphaValue = 0.0f;
                    this.previousDrawable = null;
                }
                invalidateParent();
            }
        } else if (this.drawMiniProgress) {
            if (this.previousMiniDrawable != null) {
                float f5 = this.animatedAlphaValue - (((float) dt) / 200.0f);
                this.animatedAlphaValue = f5;
                if (f5 <= 0.0f) {
                    this.animatedAlphaValue = 0.0f;
                    this.previousMiniDrawable = null;
                    if (this.currentMiniDrawable == null) {
                        z = false;
                    }
                    this.drawMiniProgress = z;
                }
                invalidateParent();
            }
        } else if (this.previousDrawable != null) {
            float f6 = this.animatedAlphaValue - (((float) dt) / 200.0f);
            this.animatedAlphaValue = f6;
            if (f6 <= 0.0f) {
                this.animatedAlphaValue = 0.0f;
                this.previousDrawable = null;
            }
            invalidateParent();
        }
    }

    public void setDiff(int value) {
        this.diff = value;
    }

    public void setProgressColor(int color) {
        this.progressColor = color;
    }

    public void setMiniProgressBackgroundColor(int color) {
        this.miniProgressBackgroundPaint.setColor(color);
    }

    public void setHideCurrentDrawable(boolean value) {
        this.hideCurrentDrawable = value;
    }

    public void setProgress(float value, boolean animated) {
        if (this.drawMiniProgress) {
            if (value != 1.0f && this.animatedAlphaValue != 0.0f && this.previousMiniDrawable != null) {
                this.animatedAlphaValue = 0.0f;
                this.previousMiniDrawable = null;
                this.drawMiniProgress = this.currentMiniDrawable != null;
            }
        } else if (value != 1.0f && this.animatedAlphaValue != 0.0f && this.previousDrawable != null) {
            this.animatedAlphaValue = 0.0f;
            this.previousDrawable = null;
        }
        if (!animated) {
            this.animatedProgressValue = value;
            this.animationProgressStart = value;
        } else {
            if (this.animatedProgressValue > value) {
                this.animatedProgressValue = value;
            }
            this.animationProgressStart = this.animatedProgressValue;
        }
        this.currentProgress = value;
        this.currentProgressTime = 0L;
        invalidateParent();
    }

    private void invalidateParent() {
        int offset = AndroidUtilities.dp(2.0f);
        this.parent.invalidate(((int) this.progressRect.left) - offset, ((int) this.progressRect.top) - offset, ((int) this.progressRect.right) + (offset * 2), ((int) this.progressRect.bottom) + (offset * 2));
    }

    public void setCheckBackground(boolean withRound, boolean animated) {
        if (this.checkDrawable == null) {
            this.checkDrawable = new CheckDrawable();
            this.checkBackgroundDrawable = Theme.createCircleDrawableWithIcon(AndroidUtilities.dp(48.0f), this.checkDrawable, 0);
        }
        Theme.setCombinedDrawableColor(this.checkBackgroundDrawable, Theme.getColor(Theme.key_chat_mediaLoaderPhoto), false);
        Theme.setCombinedDrawableColor(this.checkBackgroundDrawable, Theme.getColor(Theme.key_chat_mediaLoaderPhotoIcon), true);
        Drawable drawable = this.currentDrawable;
        Drawable drawable2 = this.checkBackgroundDrawable;
        if (drawable != drawable2) {
            setBackground(drawable2, withRound, animated);
            this.checkDrawable.resetProgress(animated);
        }
    }

    public boolean isDrawCheckDrawable() {
        return this.currentDrawable == this.checkBackgroundDrawable;
    }

    public void setBackground(Drawable drawable, boolean withRound, boolean animated) {
        Drawable drawable2;
        this.lastUpdateTime = System.currentTimeMillis();
        if (animated && (drawable2 = this.currentDrawable) != drawable) {
            this.previousDrawable = drawable2;
            this.previousWithRound = this.currentWithRound;
            this.animatedAlphaValue = 1.0f;
            setProgress(1.0f, animated);
        } else {
            this.previousDrawable = null;
            this.previousWithRound = false;
        }
        this.currentWithRound = withRound;
        this.currentDrawable = drawable;
        if (!animated) {
            this.parent.invalidate();
        } else {
            invalidateParent();
        }
    }

    public void setMiniBackground(Drawable drawable, boolean withRound, boolean animated) {
        Drawable drawable2;
        this.lastUpdateTime = System.currentTimeMillis();
        boolean z = false;
        if (animated && (drawable2 = this.currentMiniDrawable) != drawable) {
            this.previousMiniDrawable = drawable2;
            this.previousMiniWithRound = this.currentMiniWithRound;
            this.animatedAlphaValue = 1.0f;
            setProgress(1.0f, animated);
        } else {
            this.previousMiniDrawable = null;
            this.previousMiniWithRound = false;
        }
        this.currentMiniWithRound = withRound;
        this.currentMiniDrawable = drawable;
        if (this.previousMiniDrawable != null || drawable != null) {
            z = true;
        }
        this.drawMiniProgress = z;
        if (z && this.miniDrawBitmap == null) {
            try {
                this.miniDrawBitmap = Bitmap.createBitmap(AndroidUtilities.dp(48.0f), AndroidUtilities.dp(48.0f), Bitmap.Config.ARGB_8888);
                this.miniDrawCanvas = new Canvas(this.miniDrawBitmap);
            } catch (Throwable th) {
            }
        }
        if (!animated) {
            this.parent.invalidate();
        } else {
            invalidateParent();
        }
    }

    public boolean swapBackground(Drawable drawable) {
        if (this.currentDrawable != drawable) {
            this.currentDrawable = drawable;
            return true;
        }
        return false;
    }

    public boolean swapMiniBackground(Drawable drawable) {
        boolean z = false;
        if (this.currentMiniDrawable != drawable) {
            this.currentMiniDrawable = drawable;
            if (this.previousMiniDrawable != null || drawable != null) {
                z = true;
            }
            this.drawMiniProgress = z;
            return true;
        }
        return false;
    }

    public float getAlpha() {
        if (this.previousDrawable == null && this.currentDrawable == null) {
            return 0.0f;
        }
        return this.animatedAlphaValue;
    }

    public void setOverrideAlpha(float alpha) {
        this.overrideAlpha = alpha;
    }

    public void draw(Canvas canvas) {
        Drawable drawable;
        float cy;
        float cx;
        int size;
        int size2;
        float alpha;
        Drawable drawable2;
        if (this.drawMiniProgress && this.currentDrawable != null) {
            if (this.miniDrawCanvas != null) {
                this.miniDrawBitmap.eraseColor(0);
            }
            this.currentDrawable.setAlpha((int) (this.overrideAlpha * 255.0f));
            if (this.miniDrawCanvas != null) {
                this.currentDrawable.setBounds(0, 0, (int) this.progressRect.width(), (int) this.progressRect.height());
                this.currentDrawable.draw(this.miniDrawCanvas);
            } else {
                this.currentDrawable.setBounds((int) this.progressRect.left, (int) this.progressRect.top, (int) this.progressRect.right, (int) this.progressRect.bottom);
                this.currentDrawable.draw(canvas);
            }
            if (Math.abs(this.progressRect.width() - AndroidUtilities.dp(44.0f)) < AndroidUtilities.density) {
                float cx2 = this.progressRect.centerX() + AndroidUtilities.dp(0 + 16);
                cy = this.progressRect.centerY() + AndroidUtilities.dp(0 + 16);
                cx = cx2;
                size = 20;
                size2 = 0;
            } else {
                float cx3 = this.progressRect.centerX() + AndroidUtilities.dp(18.0f);
                cy = AndroidUtilities.dp(18.0f) + this.progressRect.centerY();
                cx = cx3;
                size = 22;
                size2 = 2;
            }
            int halfSize = size / 2;
            if (this.previousMiniDrawable != null && this.alphaForMiniPrevious) {
                alpha = this.animatedAlphaValue * this.overrideAlpha;
            } else {
                alpha = 1.0f;
            }
            Canvas canvas2 = this.miniDrawCanvas;
            if (canvas2 == null) {
                this.miniProgressBackgroundPaint.setColor(this.progressColor);
                if (this.previousMiniDrawable != null && this.currentMiniDrawable == null) {
                    this.miniProgressBackgroundPaint.setAlpha((int) (this.animatedAlphaValue * 255.0f * this.overrideAlpha));
                } else {
                    this.miniProgressBackgroundPaint.setAlpha(255);
                }
                canvas.drawCircle(cx, cy, AndroidUtilities.dp(12.0f), this.miniProgressBackgroundPaint);
            } else {
                canvas2.drawCircle(AndroidUtilities.dp(size + 18 + size2), AndroidUtilities.dp(size + 18 + size2), AndroidUtilities.dp(halfSize + 1) * alpha, Theme.checkboxSquare_eraserPaint);
            }
            if (this.miniDrawCanvas != null) {
                canvas.drawBitmap(this.miniDrawBitmap, (int) this.progressRect.left, (int) this.progressRect.top, (Paint) null);
            }
            Drawable drawable3 = this.previousMiniDrawable;
            if (drawable3 != null) {
                if (this.alphaForMiniPrevious) {
                    drawable3.setAlpha((int) (this.animatedAlphaValue * 255.0f * this.overrideAlpha));
                } else {
                    drawable3.setAlpha((int) (this.overrideAlpha * 255.0f));
                }
                this.previousMiniDrawable.setBounds((int) (cx - (AndroidUtilities.dp(halfSize) * alpha)), (int) (cy - (AndroidUtilities.dp(halfSize) * alpha)), (int) ((AndroidUtilities.dp(halfSize) * alpha) + cx), (int) ((AndroidUtilities.dp(halfSize) * alpha) + cy));
                this.previousMiniDrawable.draw(canvas);
            }
            if (!this.hideCurrentDrawable && (drawable2 = this.currentMiniDrawable) != null) {
                if (this.previousMiniDrawable != null) {
                    drawable2.setAlpha((int) ((1.0f - this.animatedAlphaValue) * 255.0f * this.overrideAlpha));
                } else {
                    drawable2.setAlpha((int) (this.overrideAlpha * 255.0f));
                }
                this.currentMiniDrawable.setBounds((int) (cx - AndroidUtilities.dp(halfSize)), (int) (cy - AndroidUtilities.dp(halfSize)), (int) (AndroidUtilities.dp(halfSize) + cx), (int) (AndroidUtilities.dp(halfSize) + cy));
                this.currentMiniDrawable.draw(canvas);
            }
            if (this.currentMiniWithRound || this.previousMiniWithRound) {
                this.miniProgressPaint.setColor(this.progressColor);
                if (this.previousMiniWithRound) {
                    this.miniProgressPaint.setAlpha((int) (this.animatedAlphaValue * 255.0f * this.overrideAlpha));
                } else {
                    this.miniProgressPaint.setAlpha((int) (this.overrideAlpha * 255.0f));
                }
                this.cicleRect.set(cx - (AndroidUtilities.dp(halfSize - 2) * alpha), cy - (AndroidUtilities.dp(halfSize - 2) * alpha), (AndroidUtilities.dp(halfSize - 2) * alpha) + cx, (AndroidUtilities.dp(halfSize - 2) * alpha) + cy);
                canvas.drawArc(this.cicleRect, (-90.0f) + this.radOffset, Math.max(4.0f, this.animatedProgressValue * 360.0f), false, this.miniProgressPaint);
                updateAnimation(true);
                return;
            }
            updateAnimation(false);
            return;
        }
        Drawable drawable4 = this.previousDrawable;
        if (drawable4 != null) {
            if (this.alphaForPrevious) {
                drawable4.setAlpha((int) (this.animatedAlphaValue * 255.0f * this.overrideAlpha));
            } else {
                drawable4.setAlpha((int) (this.overrideAlpha * 255.0f));
            }
            this.previousDrawable.setBounds((int) this.progressRect.left, (int) this.progressRect.top, (int) this.progressRect.right, (int) this.progressRect.bottom);
            this.previousDrawable.draw(canvas);
        }
        if (!this.hideCurrentDrawable && (drawable = this.currentDrawable) != null) {
            if (this.previousDrawable != null) {
                drawable.setAlpha((int) ((1.0f - this.animatedAlphaValue) * 255.0f * this.overrideAlpha));
            } else {
                drawable.setAlpha((int) (this.overrideAlpha * 255.0f));
            }
            this.currentDrawable.setBounds((int) this.progressRect.left, (int) this.progressRect.top, (int) this.progressRect.right, (int) this.progressRect.bottom);
            this.currentDrawable.draw(canvas);
        }
        if (this.currentWithRound || this.previousWithRound) {
            this.progressPaint.setColor(this.progressColor);
            if (this.previousWithRound) {
                this.progressPaint.setAlpha((int) (this.animatedAlphaValue * 255.0f * this.overrideAlpha));
            } else {
                this.progressPaint.setAlpha((int) (this.overrideAlpha * 255.0f));
            }
            this.cicleRect.set(this.progressRect.left + this.diff, this.progressRect.top + this.diff, this.progressRect.right - this.diff, this.progressRect.bottom - this.diff);
            canvas.drawArc(this.cicleRect, (-90.0f) + this.radOffset, Math.max(4.0f, this.animatedProgressValue * 360.0f), false, this.progressPaint);
            updateAnimation(true);
            return;
        }
        updateAnimation(false);
    }
}
