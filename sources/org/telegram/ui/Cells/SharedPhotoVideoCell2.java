package org.telegram.ui.Cells;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.SparseArray;
import android.view.View;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.ColorUtils;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.DownloadController;
import org.telegram.messenger.FileLoader;
import org.telegram.messenger.ImageLocation;
import org.telegram.messenger.ImageReceiver;
import org.telegram.messenger.MessageObject;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.R;
import org.telegram.tgnet.TLRPC$Document;
import org.telegram.tgnet.TLRPC$PhotoSize;
import org.telegram.tgnet.TLRPC$TL_messageMediaPhoto;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Components.CheckBoxBase;
import org.telegram.ui.Components.FlickerLoadingView;
import org.telegram.ui.PhotoViewer;
/* loaded from: classes3.dex */
public class SharedPhotoVideoCell2 extends View {
    static boolean lastAutoDownload;
    static long lastUpdateDownloadSettingsTime;
    ValueAnimator animator;
    private boolean attached;
    CheckBoxBase checkBoxBase;
    float checkBoxProgress;
    float crossfadeProgress;
    float crossfadeToColumnsCount;
    SharedPhotoVideoCell2 crossfadeView;
    int currentAccount;
    MessageObject currentMessageObject;
    int currentParentColumnsCount;
    FlickerLoadingView globalGradientView;
    float highlightProgress;
    SharedResources sharedResources;
    boolean showVideoLayout;
    StaticLayout videoInfoLayot;
    String videoText;
    public ImageReceiver imageReceiver = new ImageReceiver();
    float imageAlpha = 1.0f;
    float imageScale = 1.0f;

    public SharedPhotoVideoCell2(Context context, SharedResources sharedResources, int i) {
        super(context);
        this.sharedResources = sharedResources;
        this.currentAccount = i;
        setChecked(false, false);
        this.imageReceiver.setParentView(this);
    }

    /* JADX WARN: Removed duplicated region for block: B:26:0x0238  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void setMessageObject(MessageObject messageObject, int i) {
        int i2 = this.currentParentColumnsCount;
        this.currentParentColumnsCount = i;
        MessageObject messageObject2 = this.currentMessageObject;
        if (messageObject2 == null && messageObject == null) {
            return;
        }
        if (messageObject2 != null && messageObject != null && messageObject2.getId() == messageObject.getId() && i2 == i) {
            return;
        }
        this.currentMessageObject = messageObject;
        boolean z = false;
        TLRPC$PhotoSize tLRPC$PhotoSize = null;
        if (messageObject == null) {
            this.imageReceiver.onDetachedFromWindow();
            this.videoText = null;
            this.videoInfoLayot = null;
            this.showVideoLayout = false;
            return;
        }
        if (this.attached) {
            this.imageReceiver.onAttachedToWindow();
        }
        String restrictionReason = MessagesController.getRestrictionReason(messageObject.messageOwner.restriction_reason);
        String filterString = this.sharedResources.getFilterString((int) ((AndroidUtilities.displaySize.x / i) / AndroidUtilities.density));
        int i3 = 320;
        if (i <= 2) {
            i3 = AndroidUtilities.getPhotoSize();
        }
        this.videoText = null;
        this.videoInfoLayot = null;
        this.showVideoLayout = false;
        if (TextUtils.isEmpty(restrictionReason)) {
            if (messageObject.isVideo()) {
                this.showVideoLayout = true;
                if (i != 9) {
                    this.videoText = AndroidUtilities.formatShortDuration(messageObject.getDuration());
                }
                ImageLocation imageLocation = messageObject.mediaThumb;
                if (imageLocation != null) {
                    BitmapDrawable bitmapDrawable = messageObject.strippedThumb;
                    if (bitmapDrawable != null) {
                        this.imageReceiver.setImage(imageLocation, filterString, bitmapDrawable, null, messageObject, 0);
                    } else {
                        this.imageReceiver.setImage(imageLocation, filterString, messageObject.mediaSmallThumb, filterString + "_b", null, 0L, null, messageObject, 0);
                    }
                } else {
                    TLRPC$Document document = messageObject.getDocument();
                    TLRPC$PhotoSize closestPhotoSizeWithSize = FileLoader.getClosestPhotoSizeWithSize(document.thumbs, 50);
                    TLRPC$PhotoSize closestPhotoSizeWithSize2 = FileLoader.getClosestPhotoSizeWithSize(document.thumbs, i3);
                    if (closestPhotoSizeWithSize != closestPhotoSizeWithSize2) {
                        tLRPC$PhotoSize = closestPhotoSizeWithSize2;
                    }
                    if (closestPhotoSizeWithSize != null) {
                        if (messageObject.strippedThumb != null) {
                            this.imageReceiver.setImage(ImageLocation.getForDocument(tLRPC$PhotoSize, document), filterString, messageObject.strippedThumb, null, messageObject, 0);
                        } else {
                            this.imageReceiver.setImage(ImageLocation.getForDocument(tLRPC$PhotoSize, document), filterString, ImageLocation.getForDocument(closestPhotoSizeWithSize, document), filterString + "_b", null, 0L, null, messageObject, 0);
                        }
                    }
                }
                if (z) {
                    this.imageReceiver.setImageBitmap(ContextCompat.getDrawable(getContext(), R.drawable.photo_placeholder_in));
                }
                invalidate();
            } else if ((MessageObject.getMedia(messageObject.messageOwner) instanceof TLRPC$TL_messageMediaPhoto) && MessageObject.getMedia(messageObject.messageOwner).photo != null && !messageObject.photoThumbs.isEmpty()) {
                if (messageObject.mediaExists || canAutoDownload(messageObject)) {
                    ImageLocation imageLocation2 = messageObject.mediaThumb;
                    if (imageLocation2 != null) {
                        BitmapDrawable bitmapDrawable2 = messageObject.strippedThumb;
                        if (bitmapDrawable2 != null) {
                            this.imageReceiver.setImage(imageLocation2, filterString, bitmapDrawable2, null, messageObject, 0);
                        } else {
                            this.imageReceiver.setImage(imageLocation2, filterString, messageObject.mediaSmallThumb, filterString + "_b", null, 0L, null, messageObject, 0);
                        }
                    } else {
                        TLRPC$PhotoSize closestPhotoSizeWithSize3 = FileLoader.getClosestPhotoSizeWithSize(messageObject.photoThumbs, 50);
                        TLRPC$PhotoSize closestPhotoSizeWithSize4 = FileLoader.getClosestPhotoSizeWithSize(messageObject.photoThumbs, i3, false, closestPhotoSizeWithSize3, false);
                        if (closestPhotoSizeWithSize4 != closestPhotoSizeWithSize3) {
                            tLRPC$PhotoSize = closestPhotoSizeWithSize3;
                        }
                        long j = 0;
                        if (messageObject.strippedThumb != null) {
                            ImageReceiver imageReceiver = this.imageReceiver;
                            ImageLocation forObject = ImageLocation.getForObject(closestPhotoSizeWithSize4, messageObject.photoThumbsObject);
                            BitmapDrawable bitmapDrawable3 = messageObject.strippedThumb;
                            if (closestPhotoSizeWithSize4 != null) {
                                j = closestPhotoSizeWithSize4.size;
                            }
                            imageReceiver.setImage(forObject, filterString, null, null, bitmapDrawable3, j, null, messageObject, messageObject.shouldEncryptPhotoOrVideo() ? 2 : 1);
                        } else {
                            ImageReceiver imageReceiver2 = this.imageReceiver;
                            ImageLocation forObject2 = ImageLocation.getForObject(closestPhotoSizeWithSize4, messageObject.photoThumbsObject);
                            ImageLocation forObject3 = ImageLocation.getForObject(tLRPC$PhotoSize, messageObject.photoThumbsObject);
                            String str = filterString + "_b";
                            if (closestPhotoSizeWithSize4 != null) {
                                j = closestPhotoSizeWithSize4.size;
                            }
                            imageReceiver2.setImage(forObject2, filterString, forObject3, str, j, null, messageObject, messageObject.shouldEncryptPhotoOrVideo() ? 2 : 1);
                        }
                    }
                } else {
                    BitmapDrawable bitmapDrawable4 = messageObject.strippedThumb;
                    if (bitmapDrawable4 != null) {
                        this.imageReceiver.setImage(null, null, null, null, bitmapDrawable4, 0L, null, messageObject, 0);
                    } else {
                        this.imageReceiver.setImage(null, null, ImageLocation.getForObject(FileLoader.getClosestPhotoSizeWithSize(messageObject.photoThumbs, 50), messageObject.photoThumbsObject), "b", null, 0L, null, messageObject, 0);
                    }
                }
                if (z) {
                }
                invalidate();
            }
        }
        z = true;
        if (z) {
        }
        invalidate();
    }

    private boolean canAutoDownload(MessageObject messageObject) {
        if (System.currentTimeMillis() - lastUpdateDownloadSettingsTime > 5000) {
            lastUpdateDownloadSettingsTime = System.currentTimeMillis();
            lastAutoDownload = DownloadController.getInstance(this.currentAccount).canDownloadMedia(messageObject);
        }
        return lastAutoDownload;
    }

    /* JADX WARN: Removed duplicated region for block: B:101:0x00f7  */
    /* JADX WARN: Removed duplicated region for block: B:29:0x0100 A[RETURN] */
    /* JADX WARN: Removed duplicated region for block: B:31:0x0101  */
    /* JADX WARN: Removed duplicated region for block: B:91:0x00a9  */
    @Override // android.view.View
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    protected void onDraw(Canvas canvas) {
        float dpf2;
        float measuredWidth;
        float measuredHeight;
        float f;
        float f2;
        int dp;
        String str;
        float dp2;
        float dpf22;
        float f3;
        super.onDraw(canvas);
        if (this.crossfadeProgress != 0.0f) {
            float f4 = this.crossfadeToColumnsCount;
            if (f4 == 9.0f || this.currentParentColumnsCount == 9) {
                if (f4 == 9.0f) {
                    dp2 = AndroidUtilities.dp(0.5f) * this.crossfadeProgress;
                    dpf22 = AndroidUtilities.dpf2(1.0f);
                    f3 = this.crossfadeProgress;
                } else {
                    dp2 = AndroidUtilities.dp(1.0f) * this.crossfadeProgress;
                    dpf22 = AndroidUtilities.dpf2(0.5f);
                    f3 = this.crossfadeProgress;
                }
                dpf2 = dp2 + (dpf22 * (1.0f - f3));
                float f5 = dpf2;
                float f6 = f5 * 2.0f;
                measuredWidth = (getMeasuredWidth() - f6) * this.imageScale;
                measuredHeight = (getMeasuredHeight() - f6) * this.imageScale;
                if (this.crossfadeProgress > 0.5f && this.crossfadeToColumnsCount != 9.0f && this.currentParentColumnsCount != 9) {
                    measuredWidth -= 2.0f;
                    measuredHeight -= 2.0f;
                }
                float f7 = measuredWidth;
                float f8 = measuredHeight;
                if (this.currentMessageObject == null && this.imageReceiver.hasBitmapImage() && this.imageReceiver.getCurrentAlpha() == 1.0f && this.imageAlpha == 1.0f) {
                    f = f8;
                    f2 = f7;
                } else {
                    if (getParent() == null) {
                        this.globalGradientView.setParentSize(((View) getParent()).getMeasuredWidth(), getMeasuredHeight(), -getX());
                        this.globalGradientView.updateColors();
                        this.globalGradientView.updateGradient();
                        float f9 = (this.crossfadeProgress <= 0.5f || this.crossfadeToColumnsCount == 9.0f || this.currentParentColumnsCount == 9) ? f5 : f5 + 1.0f;
                        f = f8;
                        f2 = f7;
                        canvas.drawRect(f9, f9, f9 + f7, f9 + f8, this.globalGradientView.getPaint());
                    } else {
                        f = f8;
                        f2 = f7;
                    }
                    invalidate();
                }
                if (this.currentMessageObject != null) {
                    return;
                }
                float f10 = this.imageAlpha;
                if (f10 != 1.0f) {
                    canvas.saveLayerAlpha(0.0f, 0.0f, f6 + f2, f6 + f, (int) (f10 * 255.0f), 31);
                } else {
                    canvas.save();
                }
                CheckBoxBase checkBoxBase = this.checkBoxBase;
                if ((checkBoxBase != null && checkBoxBase.isChecked()) || PhotoViewer.isShowingImage(this.currentMessageObject)) {
                    canvas.drawRect(f5, f5, f2, f, this.sharedResources.backgroundPaint);
                }
                if (this.currentMessageObject != null) {
                    if (this.checkBoxProgress > 0.0f) {
                        float dp3 = AndroidUtilities.dp(10.0f) * this.checkBoxProgress;
                        float f11 = f5 + dp3;
                        float f12 = dp3 * 2.0f;
                        this.imageReceiver.setImageCoords(f11, f11, f2 - f12, f - f12);
                    } else {
                        float f13 = (this.crossfadeProgress <= 0.5f || this.crossfadeToColumnsCount == 9.0f || this.currentParentColumnsCount == 9) ? f5 : f5 + 1.0f;
                        this.imageReceiver.setImageCoords(f13, f13, f2, f);
                    }
                    if (!PhotoViewer.isShowingImage(this.currentMessageObject)) {
                        this.imageReceiver.draw(canvas);
                        float f14 = this.highlightProgress;
                        if (f14 > 0.0f) {
                            this.sharedResources.highlightPaint.setColor(ColorUtils.setAlphaComponent(-16777216, (int) (f14 * 0.5f * 255.0f)));
                            canvas.drawRect(this.imageReceiver.getDrawRegion(), this.sharedResources.highlightPaint);
                        }
                    }
                }
                if (this.showVideoLayout) {
                    canvas.save();
                    canvas.clipRect(f5, f5, f5 + f2, f5 + f);
                    if (this.currentParentColumnsCount != 9 && this.videoInfoLayot == null && (str = this.videoText) != null) {
                        this.videoInfoLayot = new StaticLayout(this.videoText, this.sharedResources.textPaint, (int) Math.ceil(this.sharedResources.textPaint.measureText(str)), Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
                    }
                    if (this.videoInfoLayot == null) {
                        dp = AndroidUtilities.dp(17.0f);
                    } else {
                        dp = AndroidUtilities.dp(14.0f) + this.videoInfoLayot.getWidth() + AndroidUtilities.dp(4.0f);
                    }
                    canvas.translate(AndroidUtilities.dp(5.0f), ((AndroidUtilities.dp(1.0f) + f) - AndroidUtilities.dp(17.0f)) - AndroidUtilities.dp(4.0f));
                    RectF rectF = AndroidUtilities.rectTmp;
                    rectF.set(0.0f, 0.0f, dp, AndroidUtilities.dp(17.0f));
                    canvas.drawRoundRect(rectF, AndroidUtilities.dp(4.0f), AndroidUtilities.dp(4.0f), Theme.chat_timeBackgroundPaint);
                    canvas.save();
                    canvas.translate(this.videoInfoLayot == null ? AndroidUtilities.dp(5.0f) : AndroidUtilities.dp(4.0f), (AndroidUtilities.dp(17.0f) - this.sharedResources.playDrawable.getIntrinsicHeight()) / 2.0f);
                    this.sharedResources.playDrawable.setAlpha((int) (this.imageAlpha * 255.0f));
                    this.sharedResources.playDrawable.draw(canvas);
                    canvas.restore();
                    if (this.videoInfoLayot != null) {
                        canvas.translate(AndroidUtilities.dp(14.0f), (AndroidUtilities.dp(17.0f) - this.videoInfoLayot.getHeight()) / 2.0f);
                        this.videoInfoLayot.draw(canvas);
                    }
                    canvas.restore();
                }
                CheckBoxBase checkBoxBase2 = this.checkBoxBase;
                if (checkBoxBase2 != null && checkBoxBase2.getProgress() != 0.0f) {
                    canvas.save();
                    canvas.translate((f2 + AndroidUtilities.dp(2.0f)) - AndroidUtilities.dp(25.0f), 0.0f);
                    this.checkBoxBase.draw(canvas);
                    canvas.restore();
                }
                canvas.restore();
                return;
            }
        }
        dpf2 = this.currentParentColumnsCount == 9 ? AndroidUtilities.dpf2(0.5f) : AndroidUtilities.dpf2(1.0f);
        float f52 = dpf2;
        float f62 = f52 * 2.0f;
        measuredWidth = (getMeasuredWidth() - f62) * this.imageScale;
        measuredHeight = (getMeasuredHeight() - f62) * this.imageScale;
        if (this.crossfadeProgress > 0.5f) {
            measuredWidth -= 2.0f;
            measuredHeight -= 2.0f;
        }
        float f72 = measuredWidth;
        float f82 = measuredHeight;
        if (this.currentMessageObject == null) {
        }
        if (getParent() == null) {
        }
        invalidate();
        if (this.currentMessageObject != null) {
        }
    }

    @Override // android.view.View
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        this.attached = true;
        CheckBoxBase checkBoxBase = this.checkBoxBase;
        if (checkBoxBase != null) {
            checkBoxBase.onAttachedToWindow();
        }
        if (this.currentMessageObject != null) {
            this.imageReceiver.onAttachedToWindow();
        }
    }

    @Override // android.view.View
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        this.attached = false;
        CheckBoxBase checkBoxBase = this.checkBoxBase;
        if (checkBoxBase != null) {
            checkBoxBase.onDetachedFromWindow();
        }
        if (this.currentMessageObject != null) {
            this.imageReceiver.onDetachedFromWindow();
        }
    }

    public void setGradientView(FlickerLoadingView flickerLoadingView) {
        this.globalGradientView = flickerLoadingView;
    }

    @Override // android.view.View
    protected void onMeasure(int i, int i2) {
        super.onMeasure(i, View.MeasureSpec.makeMeasureSpec(View.MeasureSpec.getSize(i), 1073741824));
    }

    public int getMessageId() {
        MessageObject messageObject = this.currentMessageObject;
        if (messageObject != null) {
            return messageObject.getId();
        }
        return 0;
    }

    public MessageObject getMessageObject() {
        return this.currentMessageObject;
    }

    public void setImageAlpha(float f, boolean z) {
        if (this.imageAlpha != f) {
            this.imageAlpha = f;
            if (!z) {
                return;
            }
            invalidate();
        }
    }

    public void setImageScale(float f, boolean z) {
        if (this.imageScale != f) {
            this.imageScale = f;
            if (!z) {
                return;
            }
            invalidate();
        }
    }

    public void setCrossfadeView(SharedPhotoVideoCell2 sharedPhotoVideoCell2, float f, int i) {
        this.crossfadeView = sharedPhotoVideoCell2;
        this.crossfadeProgress = f;
        this.crossfadeToColumnsCount = i;
    }

    public void drawCrossafadeImage(Canvas canvas) {
        if (this.crossfadeView != null) {
            canvas.save();
            canvas.translate(getX(), getY());
            this.crossfadeView.setImageScale(((getMeasuredWidth() - AndroidUtilities.dp(2.0f)) * this.imageScale) / (this.crossfadeView.getMeasuredWidth() - AndroidUtilities.dp(2.0f)), false);
            this.crossfadeView.draw(canvas);
            canvas.restore();
        }
    }

    public View getCrossfadeView() {
        return this.crossfadeView;
    }

    public void setChecked(final boolean z, boolean z2) {
        CheckBoxBase checkBoxBase = this.checkBoxBase;
        if ((checkBoxBase != null && checkBoxBase.isChecked()) == z) {
            return;
        }
        if (this.checkBoxBase == null) {
            CheckBoxBase checkBoxBase2 = new CheckBoxBase(this, 21, null);
            this.checkBoxBase = checkBoxBase2;
            checkBoxBase2.setColor(null, "sharedMedia_photoPlaceholder", "checkboxCheck");
            this.checkBoxBase.setDrawUnchecked(false);
            this.checkBoxBase.setBackgroundType(1);
            this.checkBoxBase.setBounds(0, 0, AndroidUtilities.dp(24.0f), AndroidUtilities.dp(24.0f));
            if (this.attached) {
                this.checkBoxBase.onAttachedToWindow();
            }
        }
        this.checkBoxBase.setChecked(z, z2);
        ValueAnimator valueAnimator = this.animator;
        if (valueAnimator != null) {
            this.animator = null;
            valueAnimator.cancel();
        }
        float f = 1.0f;
        if (z2) {
            float[] fArr = new float[2];
            fArr[0] = this.checkBoxProgress;
            if (!z) {
                f = 0.0f;
            }
            fArr[1] = f;
            ValueAnimator ofFloat = ValueAnimator.ofFloat(fArr);
            this.animator = ofFloat;
            ofFloat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: org.telegram.ui.Cells.SharedPhotoVideoCell2.1
                @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                public void onAnimationUpdate(ValueAnimator valueAnimator2) {
                    SharedPhotoVideoCell2.this.checkBoxProgress = ((Float) valueAnimator2.getAnimatedValue()).floatValue();
                    SharedPhotoVideoCell2.this.invalidate();
                }
            });
            this.animator.setDuration(200L);
            this.animator.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.Cells.SharedPhotoVideoCell2.2
                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public void onAnimationEnd(Animator animator) {
                    ValueAnimator valueAnimator2 = SharedPhotoVideoCell2.this.animator;
                    if (valueAnimator2 == null || !valueAnimator2.equals(animator)) {
                        return;
                    }
                    SharedPhotoVideoCell2 sharedPhotoVideoCell2 = SharedPhotoVideoCell2.this;
                    sharedPhotoVideoCell2.checkBoxProgress = z ? 1.0f : 0.0f;
                    sharedPhotoVideoCell2.animator = null;
                }
            });
            this.animator.start();
        } else {
            if (!z) {
                f = 0.0f;
            }
            this.checkBoxProgress = f;
        }
        invalidate();
    }

    public void setHighlightProgress(float f) {
        if (this.highlightProgress != f) {
            this.highlightProgress = f;
            invalidate();
        }
    }

    /* loaded from: classes3.dex */
    public static class SharedResources {
        Drawable playDrawable;
        TextPaint textPaint = new TextPaint(1);
        private Paint backgroundPaint = new Paint();
        Paint highlightPaint = new Paint();
        SparseArray<String> imageFilters = new SparseArray<>();

        public SharedResources(Context context, Theme.ResourcesProvider resourcesProvider) {
            this.textPaint.setTextSize(AndroidUtilities.dp(12.0f));
            this.textPaint.setColor(-1);
            this.textPaint.setTypeface(AndroidUtilities.getTypeface(AndroidUtilities.TYPEFACE_ROBOTO_MEDIUM));
            Drawable drawable = ContextCompat.getDrawable(context, R.drawable.play_mini_video);
            this.playDrawable = drawable;
            drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), this.playDrawable.getIntrinsicHeight());
            this.backgroundPaint.setColor(Theme.getColor("sharedMedia_photoPlaceholder", resourcesProvider));
        }

        public String getFilterString(int i) {
            String str = this.imageFilters.get(i);
            if (str == null) {
                String str2 = i + "_" + i + "_isc";
                this.imageFilters.put(i, str2);
                return str2;
            }
            return str;
        }
    }
}