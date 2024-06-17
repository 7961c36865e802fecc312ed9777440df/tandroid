package org.telegram.ui.Components.Paint.Views;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.CornerPathEffect;
import android.graphics.DashPathEffect;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Build;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;
import android.widget.TextView;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.mlkit.common.MlKitException;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.label.ImageLabel;
import com.google.mlkit.vision.label.ImageLabeling;
import com.google.mlkit.vision.label.defaults.ImageLabelerOptions;
import com.google.mlkit.vision.segmentation.subject.Subject;
import com.google.mlkit.vision.segmentation.subject.SubjectSegmentation;
import com.google.mlkit.vision.segmentation.subject.SubjectSegmentationResult;
import com.google.mlkit.vision.segmentation.subject.SubjectSegmenter;
import com.google.mlkit.vision.segmentation.subject.SubjectSegmenterOptions;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.Emoji;
import org.telegram.messenger.EmuDetector;
import org.telegram.messenger.FileLoader;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.ImageReceiver;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MediaController;
import org.telegram.messenger.MediaDataController;
import org.telegram.messenger.MessageObject;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.R;
import org.telegram.messenger.SharedConfig;
import org.telegram.messenger.UserConfig;
import org.telegram.messenger.Utilities;
import org.telegram.messenger.VideoEditedInfo;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC$Document;
import org.telegram.tgnet.TLRPC$InputDocument;
import org.telegram.tgnet.TLRPC$InputFile;
import org.telegram.tgnet.TLRPC$Message;
import org.telegram.tgnet.TLRPC$StickerSet;
import org.telegram.tgnet.TLRPC$TL_availableReaction;
import org.telegram.tgnet.TLRPC$TL_documentAttributeSticker;
import org.telegram.tgnet.TLRPC$TL_error;
import org.telegram.tgnet.TLRPC$TL_inputMediaUploadedDocument;
import org.telegram.tgnet.TLRPC$TL_inputPeerSelf;
import org.telegram.tgnet.TLRPC$TL_inputStickerSetEmpty;
import org.telegram.tgnet.TLRPC$TL_inputStickerSetItem;
import org.telegram.tgnet.TLRPC$TL_inputUserSelf;
import org.telegram.tgnet.TLRPC$TL_message;
import org.telegram.tgnet.TLRPC$TL_messageMediaDocument;
import org.telegram.tgnet.TLRPC$TL_messages_stickerSet;
import org.telegram.tgnet.TLRPC$TL_messages_uploadMedia;
import org.telegram.tgnet.TLRPC$TL_stickers_addStickerToSet;
import org.telegram.tgnet.TLRPC$TL_stickers_createStickerSet;
import org.telegram.tgnet.TLRPC$TL_stickers_replaceSticker;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Components.AnimatedFloat;
import org.telegram.ui.Components.BulletinFactory;
import org.telegram.ui.Components.CubicBezierInterpolator;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.ui.Components.Paint.ObjectDetectionEmojis;
import org.telegram.ui.Components.Paint.Views.PaintWeightChooserView;
import org.telegram.ui.Components.Paint.Views.StickerMakerView;
import org.telegram.ui.Components.ThanosEffect;
import org.telegram.ui.Stories.recorder.DownloadButton;
import org.telegram.ui.Stories.recorder.StoryEntry;
@SuppressLint({"ViewConstructor"})
/* loaded from: classes3.dex */
public class StickerMakerView extends FrameLayout implements NotificationCenter.NotificationCenterDelegate {
    private final TextView actionTextView;
    private final Path areaPath;
    private final Paint bgPaint;
    private final Path bgPath;
    private final Paint borderPaint;
    private ValueAnimator bordersAnimator;
    private float bordersAnimatorValue;
    private float bordersAnimatorValueStart;
    private final PathMeasure bordersPathMeasure;
    private int containerHeight;
    private int containerWidth;
    public int currentAccount;
    private final Paint dashPaint;
    private final Path dashPath;
    public String detectedEmoji;
    public boolean empty;
    private Rect exclusionRect;
    private ArrayList<Rect> exclusionRects;
    private Bitmap filteredBitmap;
    private float imageReceiverHeight;
    private final Matrix imageReceiverMatrix;
    private float imageReceiverWidth;
    private boolean isSegmentedState;
    public boolean isThanosInProgress;
    private DownloadButton.PreparingVideoToast loadingToast;
    public SegmentedObject[] objects;
    public int orientation;
    private final AnimatedFloat outlineAlpha;
    private final RectF outlineBounds;
    private Path outlineBoundsInnerPath;
    private Path outlineBoundsPath;
    public final Matrix outlineMatrix;
    public boolean outlineVisible;
    public float outlineWidth;
    private final Theme.ResourcesProvider resourcesProvider;
    private final Path screenPath;
    private final AnimatedFloat segmentBorderAlpha;
    private float segmentBorderImageHeight;
    private float segmentBorderImageWidth;
    private final Paint segmentBorderPaint;
    private volatile boolean segmentingLoaded;
    private volatile boolean segmentingLoading;
    private SegmentedObject selectedObject;
    private volatile Bitmap sourceBitmap;
    private StickerCutOutBtn stickerCutOutBtn;
    private StickerUploader stickerUploader;
    private ThanosEffect thanosEffect;
    float tx;
    float ty;
    public PaintWeightChooserView weightChooserView;

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$segment$11(Exception exc) {
    }

    public StickerMakerView(Context context, Theme.ResourcesProvider resourcesProvider) {
        super(context);
        this.currentAccount = -1;
        CubicBezierInterpolator cubicBezierInterpolator = CubicBezierInterpolator.EASE_OUT_QUINT;
        this.segmentBorderAlpha = new AnimatedFloat(0.0f, (View) null, 0L, 420L, cubicBezierInterpolator);
        this.outlineAlpha = new AnimatedFloat(0.0f, (View) null, 0L, 420L, cubicBezierInterpolator);
        Paint paint = new Paint(1);
        this.dashPaint = paint;
        Paint paint2 = new Paint(1);
        this.bgPaint = paint2;
        Paint paint3 = new Paint(1);
        this.borderPaint = paint3;
        Paint paint4 = new Paint(1);
        this.segmentBorderPaint = paint4;
        this.bordersPathMeasure = new PathMeasure();
        this.bgPath = new Path();
        this.areaPath = new Path();
        this.screenPath = new Path();
        this.dashPath = new Path();
        this.outlineWidth = 2.0f;
        this.imageReceiverMatrix = new Matrix();
        this.outlineMatrix = new Matrix();
        this.outlineBounds = new RectF();
        this.exclusionRects = new ArrayList<>();
        this.exclusionRect = new Rect();
        this.resourcesProvider = resourcesProvider;
        paint.setColor(-1);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(AndroidUtilities.dp(2.0f));
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setPathEffect(new DashPathEffect(new float[]{AndroidUtilities.dp(5.0f), AndroidUtilities.dp(10.0f)}, 0.5f));
        paint.setShadowLayer(AndroidUtilities.dpf2(0.75f), 0.0f, 0.0f, 1342177280);
        paint.setAlpha(140);
        TextView textView = new TextView(context);
        this.actionTextView = textView;
        textView.setTextSize(1, 13.0f);
        textView.setTextColor(-1);
        textView.setAlpha(0.0f);
        textView.setScaleX(0.3f);
        textView.setScaleY(0.3f);
        addView(textView, LayoutHelper.createFrame(-2, -2, 17));
        paint3.setColor(-1);
        paint3.setStrokeWidth(AndroidUtilities.dp(3.0f));
        paint3.setStyle(Paint.Style.STROKE);
        paint3.setStrokeCap(Paint.Cap.ROUND);
        paint3.setPathEffect(new CornerPathEffect(AndroidUtilities.dp(20.0f)));
        paint3.setMaskFilter(new BlurMaskFilter(AndroidUtilities.dp(4.0f), BlurMaskFilter.Blur.NORMAL));
        paint4.setColor(-1);
        paint4.setStrokeWidth(AndroidUtilities.dp(3.0f));
        paint4.setStyle(Paint.Style.STROKE);
        paint4.setStrokeCap(Paint.Cap.ROUND);
        paint4.setPathEffect(new CornerPathEffect(AndroidUtilities.dp(20.0f)));
        paint4.setMaskFilter(new BlurMaskFilter(AndroidUtilities.dp(4.0f), BlurMaskFilter.Blur.NORMAL));
        paint2.setColor(1711276032);
        setLayerType(2, null);
        PaintWeightChooserView paintWeightChooserView = new PaintWeightChooserView(context);
        this.weightChooserView = paintWeightChooserView;
        paintWeightChooserView.setAlpha(0.0f);
        this.weightChooserView.setTranslationX(-AndroidUtilities.dp(18.0f));
        this.weightChooserView.setMinMax(0.33f, 10.0f);
        this.weightChooserView.setBrushWeight(this.outlineWidth);
        this.weightChooserView.setValueOverride(new PaintWeightChooserView.ValueOverride() { // from class: org.telegram.ui.Components.Paint.Views.StickerMakerView.1
            @Override // org.telegram.ui.Components.Paint.Views.PaintWeightChooserView.ValueOverride
            public float get() {
                return StickerMakerView.this.outlineWidth;
            }

            @Override // org.telegram.ui.Components.Paint.Views.PaintWeightChooserView.ValueOverride
            public void set(float f) {
                StickerMakerView.this.setOutlineWidth(f);
            }
        });
        this.weightChooserView.setTranslationX(-AndroidUtilities.dp(18.0f));
        this.weightChooserView.setAlpha(0.0f);
        addView(this.weightChooserView, LayoutHelper.createFrame(-1, -1.0f));
    }

    public void setStickerCutOutBtn(StickerCutOutBtn stickerCutOutBtn) {
        this.stickerCutOutBtn = stickerCutOutBtn;
    }

    public float getSegmentBorderImageHeight() {
        return this.segmentBorderImageHeight;
    }

    public float getSegmentBorderImageWidth() {
        return this.segmentBorderImageWidth;
    }

    public ThanosEffect getThanosEffect() {
        if (ThanosEffect.supports()) {
            if (this.thanosEffect == null) {
                ThanosEffect thanosEffect = new ThanosEffect(getContext(), new Runnable() { // from class: org.telegram.ui.Components.Paint.Views.StickerMakerView$$ExternalSyntheticLambda8
                    @Override // java.lang.Runnable
                    public final void run() {
                        StickerMakerView.this.lambda$getThanosEffect$0();
                    }
                });
                this.thanosEffect = thanosEffect;
                addView(thanosEffect, LayoutHelper.createFrame(-1, -1.0f));
            }
            return this.thanosEffect;
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$getThanosEffect$0() {
        ThanosEffect thanosEffect = this.thanosEffect;
        if (thanosEffect != null) {
            this.thanosEffect = null;
            removeView(thanosEffect);
        }
    }

    /* loaded from: classes3.dex */
    public class SegmentedObject {
        private float borderImageHeight;
        private float borderImageWidth;
        public Bitmap darkMaskImage;
        public boolean hover;
        public Bitmap image;
        public int orientation;
        public Bitmap overrideDarkMaskImage;
        public Bitmap overrideImage;
        private float[] points;
        private int pointsCount;
        private final Paint pointsHighlightPaint;
        private final Paint pointsPaint;
        public AnimatedFloat select = new AnimatedFloat(0.0f, (View) null, 0, 320, CubicBezierInterpolator.EASE_OUT_QUINT);
        public RectF bounds = new RectF();
        public RectF rotatedBounds = new RectF();
        private final Path segmentBorderPath = new Path();
        private final Path partSegmentBorderPath = new Path();
        private final Paint bordersFillPaint = new Paint(1);
        private final Paint bordersStrokePaint = new Paint(1);

        public SegmentedObject() {
            new Paint(1);
            this.pointsPaint = new Paint(1);
            this.pointsHighlightPaint = new Paint(1);
        }

        public Bitmap getImage() {
            Bitmap bitmap = this.overrideImage;
            return bitmap != null ? bitmap : this.image;
        }

        public Bitmap getDarkMaskImage() {
            Bitmap bitmap = this.overrideDarkMaskImage;
            return bitmap != null ? bitmap : this.darkMaskImage;
        }

        public Bitmap makeDarkMaskImage() {
            Bitmap createBitmap = Bitmap.createBitmap(getImage().getWidth(), getImage().getHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(createBitmap);
            canvas.drawColor(-16777216);
            Paint paint = new Paint(3);
            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OUT));
            canvas.drawBitmap(getImage(), 0.0f, 0.0f, paint);
            return createBitmap;
        }

        public void initPoints() {
            PathMeasure pathMeasure = new PathMeasure();
            pathMeasure.setPath(this.segmentBorderPath, true);
            float length = pathMeasure.getLength();
            int ceil = (int) Math.ceil(length / AndroidUtilities.dp(2.0f));
            this.pointsCount = ceil;
            this.points = new float[ceil * 2];
            float[] fArr = new float[2];
            int i = 0;
            while (true) {
                int i2 = this.pointsCount;
                if (i < i2) {
                    pathMeasure.getPosTan(((i / i2) * length) % length, fArr, null);
                    float[] fArr2 = this.points;
                    int i3 = i * 2;
                    fArr2[i3] = fArr[0];
                    fArr2[i3 + 1] = fArr[1];
                    i++;
                } else {
                    this.bordersFillPaint.setStyle(Paint.Style.FILL);
                    this.bordersFillPaint.setColor(-1);
                    this.bordersFillPaint.setStrokeJoin(Paint.Join.ROUND);
                    this.bordersFillPaint.setStrokeCap(Paint.Cap.ROUND);
                    this.bordersFillPaint.setPathEffect(new CornerPathEffect(AndroidUtilities.dp(10.0f)));
                    this.bordersStrokePaint.setStyle(Paint.Style.STROKE);
                    this.bordersStrokePaint.setColor(-1);
                    this.bordersStrokePaint.setStrokeJoin(Paint.Join.ROUND);
                    this.bordersStrokePaint.setStrokeCap(Paint.Cap.ROUND);
                    this.bordersStrokePaint.setPathEffect(new CornerPathEffect(AndroidUtilities.dp(10.0f)));
                    this.pointsPaint.setStyle(Paint.Style.STROKE);
                    this.pointsPaint.setStrokeWidth(AndroidUtilities.dp(4.0f));
                    this.pointsPaint.setColor(-1);
                    this.pointsPaint.setStrokeCap(Paint.Cap.ROUND);
                    this.pointsPaint.setMaskFilter(new BlurMaskFilter(AndroidUtilities.dp(0.33f), BlurMaskFilter.Blur.NORMAL));
                    this.pointsHighlightPaint.setStyle(Paint.Style.STROKE);
                    this.pointsHighlightPaint.setColor(Theme.multAlpha(-1, 0.04f));
                    this.pointsHighlightPaint.setStrokeCap(Paint.Cap.ROUND);
                    this.pointsHighlightPaint.setStrokeWidth(AndroidUtilities.dp(20.0f));
                    this.pointsHighlightPaint.setColor(Theme.multAlpha(-1, 0.04f));
                    this.pointsHighlightPaint.setMaskFilter(new BlurMaskFilter(AndroidUtilities.dp(60.0f), BlurMaskFilter.Blur.NORMAL));
                    return;
                }
            }
        }

        public void drawOutline(Canvas canvas, boolean z, float f, float f2) {
            if (StickerMakerView.this.outlineBoundsPath == null) {
                return;
            }
            canvas.save();
            canvas.clipPath(StickerMakerView.this.outlineBoundsPath);
            if (StickerMakerView.this.sourceBitmap != null) {
                Paint paint = z ? this.bordersStrokePaint : this.bordersFillPaint;
                paint.setAlpha((int) (f2 * 255.0f));
                paint.setStrokeWidth(AndroidUtilities.dp(f));
                canvas.drawPath(this.segmentBorderPath, paint);
                if (StickerMakerView.this.outlineBoundsPath != null && z) {
                    canvas.clipPath(this.segmentBorderPath);
                    paint.setStrokeWidth(AndroidUtilities.dp(f * 2.0f));
                    canvas.drawPath(StickerMakerView.this.outlineBoundsPath, paint);
                }
            }
            canvas.restore();
        }

        public void drawAnimationBorders(Canvas canvas, float f, float f2, View view) {
            int width;
            int height;
            this.select.setParent(view);
            if (StickerMakerView.this.sourceBitmap == null || f2 <= 0.0f) {
                return;
            }
            float lerp = AndroidUtilities.lerp(1.0f, 1.065f, f2) * AndroidUtilities.lerp(1.0f, 1.05f, this.select.set(this.hover));
            if ((this.orientation / 90) % 2 != 0) {
                width = StickerMakerView.this.sourceBitmap.getHeight();
                height = StickerMakerView.this.sourceBitmap.getWidth();
            } else {
                width = StickerMakerView.this.sourceBitmap.getWidth();
                height = StickerMakerView.this.sourceBitmap.getHeight();
            }
            canvas.save();
            float f3 = width;
            float f4 = this.borderImageWidth;
            float f5 = height;
            float f6 = this.borderImageHeight;
            canvas.scale(lerp, lerp, ((this.rotatedBounds.centerX() / f3) * f4) - (f4 / 2.0f), ((this.rotatedBounds.centerY() / f5) * f6) - (f6 / 2.0f));
            if (this.points != null) {
                int i = this.pointsCount;
                int i2 = (int) (i * f);
                int min = Math.min(500, (int) (i * 0.6f)) + i2;
                if (this.pointsCount > 0) {
                    while (i2 <= min) {
                        float f7 = 1.0f - ((min - i2) / this.pointsCount);
                        if (f7 > 0.0f) {
                            this.pointsHighlightPaint.setAlpha((int) (f7 * 10.2f * f2));
                            canvas.drawPoints(this.points, (i2 % this.pointsCount) * 2, 2, this.pointsHighlightPaint);
                        }
                        i2++;
                    }
                }
            }
            if (getImage() != null) {
                canvas.save();
                canvas.rotate(this.orientation);
                canvas.scale((1.0f / f3) * this.borderImageWidth, (1.0f / f5) * this.borderImageHeight);
                canvas.drawBitmap(getImage(), (-StickerMakerView.this.sourceBitmap.getWidth()) / 2.0f, (-StickerMakerView.this.sourceBitmap.getHeight()) / 2.0f, (Paint) null);
                canvas.restore();
            }
            if (this.points == null) {
                StickerMakerView.this.bordersPathMeasure.setPath(this.segmentBorderPath, false);
                this.partSegmentBorderPath.reset();
                float length = StickerMakerView.this.bordersPathMeasure.getLength();
                if (length == 0.0f) {
                    return;
                }
                StickerMakerView.this.segmentBorderPaint.setAlpha((int) (255.0f * f2));
                StickerMakerView.this.borderPaint.setAlpha((int) (f2 * 64.0f));
                canvas.drawPath(this.partSegmentBorderPath, StickerMakerView.this.borderPaint);
                float f8 = f + 0.2f;
                StickerMakerView.this.bordersPathMeasure.getSegment(length * f, length * f8, this.partSegmentBorderPath, true);
                canvas.drawPath(this.partSegmentBorderPath, StickerMakerView.this.segmentBorderPaint);
                canvas.drawPath(this.partSegmentBorderPath, StickerMakerView.this.segmentBorderPaint);
                if (f8 > 1.0f) {
                    this.partSegmentBorderPath.reset();
                    StickerMakerView.this.bordersPathMeasure.setPath(this.segmentBorderPath, false);
                    StickerMakerView.this.bordersPathMeasure.getSegment(0.0f, (f8 - 1.0f) * length, this.partSegmentBorderPath, true);
                    canvas.drawPath(this.partSegmentBorderPath, StickerMakerView.this.segmentBorderPaint);
                    canvas.drawPath(this.partSegmentBorderPath, StickerMakerView.this.segmentBorderPaint);
                }
            } else {
                int i3 = this.pointsCount;
                int i4 = (int) (i3 * f);
                int min2 = Math.min(500, (int) (i3 * 0.6f)) + i4;
                if (this.pointsCount > 0) {
                    for (int i5 = i4; i5 <= min2; i5++) {
                        float f9 = (i5 - i4) / (min2 - i4);
                        this.pointsPaint.setAlpha((int) (Math.min(1.0f, Math.min(f9, 1.0f - f9) * 4.0f) * 255.0f * f2));
                        canvas.drawPoints(this.points, (i5 % this.pointsCount) * 2, 2, this.pointsPaint);
                    }
                }
            }
            canvas.restore();
        }

        public void recycle() {
            this.segmentBorderPath.reset();
            Bitmap bitmap = this.overrideImage;
            if (bitmap != null) {
                bitmap.recycle();
                this.overrideImage = null;
            }
            Bitmap bitmap2 = this.image;
            if (bitmap2 != null) {
                bitmap2.recycle();
                this.image = null;
            }
            Bitmap bitmap3 = this.overrideDarkMaskImage;
            if (bitmap3 != null) {
                bitmap3.recycle();
                this.overrideDarkMaskImage = null;
            }
            Bitmap bitmap4 = this.darkMaskImage;
            if (bitmap4 != null) {
                bitmap4.recycle();
                this.darkMaskImage = null;
            }
        }
    }

    public void setOutlineWidth(float f) {
        this.outlineWidth = f;
        if (getParent() instanceof View) {
            ((View) getParent()).invalidate();
        }
    }

    public void updateOutlinePath(Bitmap bitmap) {
        SegmentedObject segmentedObject = this.selectedObject;
        if (segmentedObject == null) {
            return;
        }
        segmentedObject.overrideImage = createSmoothEdgesSegmentedImage(0, 0, bitmap, true);
        SegmentedObject segmentedObject2 = this.selectedObject;
        segmentedObject2.overrideDarkMaskImage = segmentedObject2.makeDarkMaskImage();
        createSegmentImagePath(this.selectedObject, this.containerWidth, this.containerHeight);
    }

    public boolean overriddenPaths() {
        SegmentedObject[] segmentedObjectArr = this.objects;
        if (segmentedObjectArr != null) {
            for (SegmentedObject segmentedObject : segmentedObjectArr) {
                if (segmentedObject != null && segmentedObject.overrideImage != null) {
                    return true;
                }
            }
        }
        return false;
    }

    public void resetPaths() {
        Bitmap bitmap;
        SegmentedObject[] segmentedObjectArr = this.objects;
        if (segmentedObjectArr != null) {
            for (SegmentedObject segmentedObject : segmentedObjectArr) {
                if (segmentedObject != null && (bitmap = segmentedObject.overrideImage) != null) {
                    bitmap.recycle();
                    segmentedObject.overrideImage = null;
                    Bitmap bitmap2 = segmentedObject.overrideDarkMaskImage;
                    if (bitmap2 != null) {
                        bitmap2.recycle();
                        segmentedObject.overrideDarkMaskImage = null;
                    }
                    createSegmentImagePath(segmentedObject, this.containerWidth, this.containerHeight);
                }
            }
        }
    }

    public void setOutlineVisible(boolean z) {
        if (this.outlineVisible == z) {
            return;
        }
        this.outlineVisible = z;
        this.weightChooserView.animate().alpha(z ? 1.0f : 0.0f).translationX(z ? 0.0f : AndroidUtilities.dp(-18.0f)).setInterpolator(CubicBezierInterpolator.EASE_OUT_QUINT).setDuration(320L).start();
        if (getParent() instanceof View) {
            ((View) getParent()).invalidate();
        }
        if (Build.VERSION.SDK_INT >= 29) {
            this.exclusionRects.clear();
            if (this.outlineVisible) {
                this.exclusionRects.add(this.exclusionRect);
                int measuredHeight = (int) (getMeasuredHeight() * 0.3f);
                this.exclusionRect.set(0, (getMeasuredHeight() - measuredHeight) / 2, AndroidUtilities.dp(20.0f), (getMeasuredHeight() + measuredHeight) / 2);
            }
            setSystemGestureExclusionRects(this.exclusionRects);
        }
    }

    public void drawOutline(Canvas canvas, boolean z, ViewGroup viewGroup, boolean z2) {
        float f;
        this.outlineAlpha.setParent(viewGroup);
        if (this.outlineVisible || this.outlineAlpha.get() > 0.0f) {
            if (viewGroup == null) {
                f = 1.0f;
            } else {
                f = this.outlineAlpha.set(this.outlineVisible && !z2);
            }
            SegmentedObject[] segmentedObjectArr = this.objects;
            if (segmentedObjectArr != null) {
                for (SegmentedObject segmentedObject : segmentedObjectArr) {
                    if (segmentedObject != null && segmentedObject == this.selectedObject) {
                        float f2 = this.outlineWidth;
                        if (f2 > 0.0f) {
                            segmentedObject.drawOutline(canvas, z, f2, f);
                            return;
                        }
                    }
                }
            }
        }
    }

    public void updateOutlineBounds(boolean z) {
        if (z) {
            Path path = this.outlineBoundsPath;
            if (path == null) {
                this.outlineBoundsPath = new Path();
            } else {
                path.rewind();
            }
            if (this.outlineBoundsInnerPath == null) {
                this.outlineBoundsInnerPath = new Path();
                RectF rectF = AndroidUtilities.rectTmp;
                rectF.set(0.0f, 0.0f, 1.0f, 1.0f);
                this.outlineBoundsInnerPath.addRoundRect(rectF, rectF.width() * 0.12f, rectF.height() * 0.12f, Path.Direction.CW);
            }
            this.outlineBoundsPath.addPath(this.outlineBoundsInnerPath, this.outlineMatrix);
            this.outlineBoundsPath.computeBounds(this.outlineBounds, true);
        }
    }

    public void drawSegmentBorderPath(Canvas canvas, ImageReceiver imageReceiver, Matrix matrix, ViewGroup viewGroup) {
        this.segmentBorderAlpha.setParent(viewGroup);
        if ((this.bordersAnimator != null || this.segmentBorderAlpha.get() > 0.0f) && viewGroup != null) {
            this.imageReceiverWidth = imageReceiver.getImageWidth();
            this.imageReceiverHeight = imageReceiver.getImageHeight();
            this.imageReceiverMatrix.set(matrix);
            float f = (this.bordersAnimatorValueStart + this.bordersAnimatorValue) % 1.0f;
            float f2 = this.segmentBorderAlpha.set(this.bordersAnimator != null);
            canvas.drawColor(Theme.multAlpha(1342177280, f2));
            SegmentedObject[] segmentedObjectArr = this.objects;
            if (segmentedObjectArr != null) {
                for (SegmentedObject segmentedObject : segmentedObjectArr) {
                    if (segmentedObject != null) {
                        segmentedObject.drawAnimationBorders(canvas, f, f2, viewGroup);
                    }
                }
            }
            viewGroup.invalidate();
        }
    }

    public void enableClippingMode(final Utilities.Callback<SegmentedObject> callback) {
        setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Components.Paint.Views.StickerMakerView$$ExternalSyntheticLambda1
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                StickerMakerView.this.lambda$enableClippingMode$1(callback, view);
            }
        });
        this.actionTextView.setText(LocaleController.getString(R.string.SegmentationTabToCrop));
        this.actionTextView.animate().cancel();
        this.actionTextView.animate().alpha(1.0f).scaleX(1.0f).scaleY(1.0f).setDuration(240L).setInterpolator(CubicBezierInterpolator.EASE_OUT_QUINT).start();
        ValueAnimator valueAnimator = this.bordersAnimator;
        if (valueAnimator != null) {
            valueAnimator.cancel();
        }
        this.bordersAnimatorValueStart = this.bordersAnimatorValue;
        ValueAnimator ofFloat = ValueAnimator.ofFloat(0.0f, 1.0f);
        this.bordersAnimator = ofFloat;
        ofFloat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: org.telegram.ui.Components.Paint.Views.StickerMakerView$$ExternalSyntheticLambda0
            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public final void onAnimationUpdate(ValueAnimator valueAnimator2) {
                StickerMakerView.this.lambda$enableClippingMode$2(valueAnimator2);
            }
        });
        this.bordersAnimator.setRepeatCount(-1);
        this.bordersAnimator.setRepeatMode(1);
        this.bordersAnimator.setDuration(2400L);
        this.bordersAnimator.setInterpolator(new LinearInterpolator());
        this.bordersAnimator.start();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$enableClippingMode$1(Utilities.Callback callback, View view) {
        SegmentedObject objectBehind;
        SegmentedObject[] segmentedObjectArr = this.objects;
        if (segmentedObjectArr == null || segmentedObjectArr.length == 0 || this.sourceBitmap == null || (objectBehind = objectBehind(this.tx, this.ty)) == null) {
            return;
        }
        callback.run(objectBehind);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$enableClippingMode$2(ValueAnimator valueAnimator) {
        this.bordersAnimatorValue = ((Float) valueAnimator.getAnimatedValue()).floatValue();
    }

    @Override // android.view.ViewGroup, android.view.View
    public boolean dispatchTouchEvent(MotionEvent motionEvent) {
        this.tx = motionEvent.getX();
        float y = motionEvent.getY();
        this.ty = y;
        if (this.objects != null && this.bordersAnimator != null) {
            SegmentedObject objectBehind = objectBehind(this.tx, y);
            int i = 0;
            while (true) {
                SegmentedObject[] segmentedObjectArr = this.objects;
                if (i >= segmentedObjectArr.length) {
                    break;
                }
                boolean z = true;
                z = (segmentedObjectArr[i] != objectBehind || motionEvent.getAction() == 3 || motionEvent.getAction() == 1) ? false : false;
                if (z && !this.objects[i].hover) {
                    AndroidUtilities.vibrateCursor(this);
                }
                this.objects[i].hover = z;
                i++;
            }
            if (getParent() instanceof View) {
                ((View) getParent()).invalidate();
            }
        }
        return super.dispatchTouchEvent(motionEvent);
    }

    public SegmentedObject objectBehind(float f, float f2) {
        int width;
        int height;
        if (this.sourceBitmap == null) {
            return null;
        }
        int i = 0;
        while (true) {
            SegmentedObject[] segmentedObjectArr = this.objects;
            if (i >= segmentedObjectArr.length) {
                return null;
            }
            SegmentedObject segmentedObject = segmentedObjectArr[i];
            if (segmentedObject != null) {
                if ((segmentedObjectArr[i].orientation / 90) % 2 != 0) {
                    width = this.sourceBitmap.getHeight();
                    height = this.sourceBitmap.getWidth();
                } else {
                    width = this.sourceBitmap.getWidth();
                    height = this.sourceBitmap.getHeight();
                }
                RectF rectF = AndroidUtilities.rectTmp;
                SegmentedObject[] segmentedObjectArr2 = this.objects;
                float f3 = width;
                float f4 = this.imageReceiverWidth;
                float f5 = height;
                float f6 = this.imageReceiverHeight;
                rectF.set((segmentedObjectArr2[i].rotatedBounds.left / f3) * f4, (segmentedObjectArr2[i].rotatedBounds.top / f5) * f6, (segmentedObjectArr2[i].rotatedBounds.right / f3) * f4, (segmentedObjectArr2[i].rotatedBounds.bottom / f5) * f6);
                this.imageReceiverMatrix.mapRect(rectF);
                if (rectF.contains(f, f2)) {
                    return segmentedObject;
                }
            }
            i++;
        }
    }

    public void disableClippingMode() {
        this.segmentBorderAlpha.set(0.0f);
        ValueAnimator valueAnimator = this.bordersAnimator;
        if (valueAnimator != null) {
            valueAnimator.cancel();
            this.bordersAnimator = null;
        }
        setOnClickListener(null);
        setClickable(false);
        this.actionTextView.animate().cancel();
        this.actionTextView.animate().alpha(0.0f).scaleX(0.7f).scaleY(0.7f).setDuration(240L).setInterpolator(CubicBezierInterpolator.EASE_OUT_QUINT).start();
    }

    @Override // android.widget.FrameLayout, android.view.View
    protected void onMeasure(int i, int i2) {
        super.onMeasure(i, i2);
        this.actionTextView.setTranslationY(-((getMeasuredWidth() / 2.0f) + AndroidUtilities.dp(10.0f)));
        if (Build.VERSION.SDK_INT >= 29) {
            this.exclusionRects.clear();
            if (this.outlineVisible) {
                this.exclusionRects.add(this.exclusionRect);
                int measuredHeight = (int) (getMeasuredHeight() * 0.3f);
                this.exclusionRect.set(0, (getMeasuredHeight() - measuredHeight) / 2, AndroidUtilities.dp(20.0f), (getMeasuredHeight() + measuredHeight) / 2);
            }
            setSystemGestureExclusionRects(this.exclusionRects);
        }
    }

    public boolean isSegmentedState() {
        return this.isSegmentedState;
    }

    public void setSegmentedState(boolean z, SegmentedObject segmentedObject) {
        this.isSegmentedState = z;
        this.selectedObject = segmentedObject;
    }

    public Bitmap getSegmentedDarkMaskImage() {
        SegmentedObject segmentedObject;
        if (!this.isSegmentedState || (segmentedObject = this.selectedObject) == null) {
            return null;
        }
        return segmentedObject.getDarkMaskImage();
    }

    public boolean hasSegmentedBitmap() {
        SegmentedObject[] segmentedObjectArr;
        return this.segmentingLoaded && (segmentedObjectArr = this.objects) != null && segmentedObjectArr.length > 0;
    }

    public Bitmap getSourceBitmap() {
        return this.sourceBitmap;
    }

    public Bitmap getSourceBitmap(boolean z) {
        Bitmap bitmap;
        return (!z || (bitmap = this.filteredBitmap) == null) ? this.sourceBitmap : bitmap;
    }

    public Bitmap getSegmentedImage(Bitmap bitmap, boolean z, int i) {
        SegmentedObject segmentedObject = this.selectedObject;
        if (segmentedObject == null) {
            return this.sourceBitmap;
        }
        if (z && bitmap != null) {
            return cutSegmentInFilteredBitmap(bitmap, i);
        }
        return segmentedObject.getImage();
    }

    public Bitmap getThanosImage(MediaController.PhotoEntry photoEntry, int i) {
        String str = photoEntry.filterPath;
        Bitmap decodeFile = str != null ? BitmapFactory.decodeFile(str) : getSourceBitmap();
        Bitmap decodeFile2 = BitmapFactory.decodeFile(photoEntry.paintPath);
        Bitmap createBitmap = Bitmap.createBitmap(decodeFile.getWidth(), decodeFile.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(createBitmap);
        Paint paint = new Paint(3);
        Paint paint2 = new Paint(3);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
        canvas.drawBitmap(decodeFile, 0.0f, 0.0f, paint2);
        Rect rect = new Rect();
        rect.set(0, 0, decodeFile.getWidth(), decodeFile.getHeight());
        SegmentedObject segmentedObject = this.selectedObject;
        if (segmentedObject == null) {
            SegmentedObject[] segmentedObjectArr = this.objects;
            if (segmentedObjectArr.length > 0) {
                segmentedObject = segmentedObjectArr[0];
            }
        }
        if (segmentedObject == null) {
            return null;
        }
        if (segmentedObject.orientation != 0 && photoEntry.isFiltered) {
            Matrix matrix = new Matrix();
            matrix.postRotate(segmentedObject.orientation, segmentedObject.getDarkMaskImage().getWidth() / 2.0f, segmentedObject.getDarkMaskImage().getHeight() / 2.0f);
            if ((segmentedObject.orientation / 90) % 2 != 0) {
                float height = (segmentedObject.getDarkMaskImage().getHeight() - segmentedObject.getDarkMaskImage().getWidth()) / 2.0f;
                matrix.postTranslate(height, -height);
            }
            matrix.postScale(decodeFile.getWidth() / segmentedObject.getDarkMaskImage().getHeight(), decodeFile.getHeight() / segmentedObject.getDarkMaskImage().getWidth());
            canvas.drawBitmap(segmentedObject.getDarkMaskImage(), matrix, paint);
        } else {
            canvas.drawBitmap(segmentedObject.getDarkMaskImage(), (Rect) null, rect, paint);
        }
        if (decodeFile2 != null) {
            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OUT));
            if (segmentedObject.orientation != 0 && !photoEntry.isFiltered) {
                Matrix matrix2 = new Matrix();
                matrix2.postRotate(-segmentedObject.orientation, decodeFile2.getWidth() / 2.0f, decodeFile2.getHeight() / 2.0f);
                if ((segmentedObject.orientation / 90) % 2 != 0) {
                    float height2 = (decodeFile2.getHeight() - decodeFile2.getWidth()) / 2.0f;
                    matrix2.postTranslate(height2, -height2);
                }
                matrix2.postScale(decodeFile.getWidth() / decodeFile2.getHeight(), decodeFile.getHeight() / decodeFile2.getWidth());
                canvas.drawBitmap(decodeFile2, matrix2, paint);
            } else {
                canvas.drawBitmap(decodeFile2, (Rect) null, rect, paint);
            }
        }
        return createBitmap;
    }

    @Override // android.widget.FrameLayout, android.view.ViewGroup, android.view.View
    protected void onLayout(boolean z, int i, int i2, int i3, int i4) {
        super.onLayout(z, i, i2, i3, i4);
        float dp = AndroidUtilities.dp(10.0f);
        float f = dp * 2.0f;
        float measuredWidth = getMeasuredWidth() - f;
        float measuredHeight = getMeasuredHeight() - f;
        float f2 = measuredWidth / 8.0f;
        RectF rectF = AndroidUtilities.rectTmp;
        float f3 = measuredWidth + dp;
        rectF.set(dp, dp, f3, f3);
        rectF.offset(0.0f, (measuredHeight - rectF.height()) / 2.0f);
        this.areaPath.rewind();
        this.areaPath.addRoundRect(rectF, f2, f2, Path.Direction.CW);
        this.bgPath.rewind();
        this.bgPath.addRect(0.0f, 0.0f, getMeasuredWidth(), getMeasuredHeight(), Path.Direction.CW);
        this.screenPath.reset();
        this.screenPath.op(this.bgPath, this.areaPath, Path.Op.DIFFERENCE);
        this.dashPath.rewind();
        rectF.inset(AndroidUtilities.dp(-1.0f), AndroidUtilities.dp(-1.0f));
        this.dashPath.addRoundRect(rectF, f2, f2, Path.Direction.CW);
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        canvas.drawPath(this.screenPath, this.bgPaint);
        canvas.drawPath(this.dashPath, this.dashPaint);
    }

    private Bitmap createSmoothEdgesSegmentedImage(int i, int i2, Bitmap bitmap, boolean z) {
        Bitmap sourceBitmap = getSourceBitmap();
        if (bitmap == null || bitmap.isRecycled() || sourceBitmap == null) {
            return null;
        }
        Paint paint = new Paint(3);
        Bitmap createBitmap = Bitmap.createBitmap(sourceBitmap.getWidth(), sourceBitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(createBitmap);
        if (z) {
            canvas.scale(createBitmap.getWidth() / bitmap.getWidth(), createBitmap.getHeight() / bitmap.getHeight());
            canvas.drawBitmap(bitmap, i, i2, paint);
        } else {
            canvas.drawBitmap(bitmap, i, i2, paint);
        }
        Utilities.stackBlurBitmap(createBitmap, 5);
        Bitmap createBitmap2 = Bitmap.createBitmap(sourceBitmap.getWidth(), sourceBitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas2 = new Canvas(createBitmap2);
        canvas2.drawBitmap(sourceBitmap, 0.0f, 0.0f, paint);
        Paint paint2 = new Paint(3);
        paint2.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
        canvas2.drawBitmap(createBitmap, 0.0f, 0.0f, paint2);
        createBitmap.recycle();
        return createBitmap2;
    }

    public void segmentImage(Bitmap bitmap, final int i, int i2, int i3, final Utilities.Callback<SegmentedObject> callback) {
        if (i2 <= 0) {
            i2 = AndroidUtilities.displaySize.x;
        }
        if (i3 <= 0) {
            i3 = AndroidUtilities.displaySize.y;
        }
        this.containerWidth = i2;
        this.containerHeight = i3;
        if (this.segmentingLoaded || this.segmentingLoading || bitmap == null || Build.VERSION.SDK_INT < 24) {
            return;
        }
        this.sourceBitmap = bitmap;
        this.orientation = i;
        this.detectedEmoji = null;
        segment(bitmap, i, new Utilities.Callback() { // from class: org.telegram.ui.Components.Paint.Views.StickerMakerView$$ExternalSyntheticLambda22
            @Override // org.telegram.messenger.Utilities.Callback
            public final void run(Object obj) {
                StickerMakerView.this.lambda$segmentImage$6(i, callback, (List) obj);
            }
        }, callback);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$segmentImage$6(final int i, final Utilities.Callback callback, final List list) {
        final ArrayList arrayList = new ArrayList();
        Utilities.themeQueue.postRunnable(new Runnable() { // from class: org.telegram.ui.Components.Paint.Views.StickerMakerView$$ExternalSyntheticLambda9
            @Override // java.lang.Runnable
            public final void run() {
                StickerMakerView.this.lambda$segmentImage$5(i, list, arrayList, callback);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$segmentImage$5(int i, List list, final ArrayList arrayList, final Utilities.Callback callback) {
        if (this.sourceBitmap == null || this.segmentingLoaded) {
            return;
        }
        Matrix matrix = new Matrix();
        matrix.postScale(1.0f / this.sourceBitmap.getWidth(), 1.0f / this.sourceBitmap.getHeight());
        matrix.postTranslate(-0.5f, -0.5f);
        matrix.postRotate(i);
        matrix.postTranslate(0.5f, 0.5f);
        if ((i / 90) % 2 != 0) {
            matrix.postScale(this.sourceBitmap.getHeight(), this.sourceBitmap.getWidth());
        } else {
            matrix.postScale(this.sourceBitmap.getWidth(), this.sourceBitmap.getHeight());
        }
        if (list.isEmpty()) {
            final SegmentedObject segmentedObject = new SegmentedObject();
            segmentedObject.bounds.set(0.0f, 0.0f, this.sourceBitmap.getWidth(), this.sourceBitmap.getHeight());
            segmentedObject.rotatedBounds.set(segmentedObject.bounds);
            matrix.mapRect(segmentedObject.rotatedBounds);
            segmentedObject.orientation = i;
            Bitmap createSmoothEdgesSegmentedImage = createSmoothEdgesSegmentedImage(0, 0, this.sourceBitmap, false);
            segmentedObject.image = createSmoothEdgesSegmentedImage;
            if (createSmoothEdgesSegmentedImage == null) {
                FileLog.e(new RuntimeException("createSmoothEdgesSegmentedImage failed on empty image"));
                return;
            }
            segmentedObject.darkMaskImage = segmentedObject.makeDarkMaskImage();
            createSegmentImagePath(segmentedObject, this.containerWidth, this.containerHeight);
            this.segmentBorderImageWidth = segmentedObject.borderImageWidth;
            this.segmentBorderImageHeight = segmentedObject.borderImageHeight;
            arrayList.add(segmentedObject);
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.Components.Paint.Views.StickerMakerView$$ExternalSyntheticLambda12
                @Override // java.lang.Runnable
                public final void run() {
                    StickerMakerView.this.lambda$segmentImage$3(arrayList, callback, segmentedObject);
                }
            });
            this.selectedObject = segmentedObject;
            this.segmentingLoaded = true;
            this.segmentingLoading = false;
            return;
        }
        for (int i2 = 0; i2 < list.size(); i2++) {
            SubjectMock subjectMock = (SubjectMock) list.get(i2);
            SegmentedObject segmentedObject2 = new SegmentedObject();
            RectF rectF = segmentedObject2.bounds;
            int i3 = subjectMock.startX;
            int i4 = subjectMock.startY;
            rectF.set(i3, i4, i3 + subjectMock.width, i4 + subjectMock.height);
            segmentedObject2.rotatedBounds.set(segmentedObject2.bounds);
            matrix.mapRect(segmentedObject2.rotatedBounds);
            segmentedObject2.orientation = i;
            Bitmap createSmoothEdgesSegmentedImage2 = createSmoothEdgesSegmentedImage(subjectMock.startX, subjectMock.startY, subjectMock.bitmap, false);
            segmentedObject2.image = createSmoothEdgesSegmentedImage2;
            if (createSmoothEdgesSegmentedImage2 != null) {
                segmentedObject2.darkMaskImage = segmentedObject2.makeDarkMaskImage();
                createSegmentImagePath(segmentedObject2, this.containerWidth, this.containerHeight);
                this.segmentBorderImageWidth = segmentedObject2.borderImageWidth;
                this.segmentBorderImageHeight = segmentedObject2.borderImageHeight;
                arrayList.add(segmentedObject2);
            }
        }
        this.selectedObject = null;
        this.segmentingLoaded = true;
        this.segmentingLoading = false;
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.Components.Paint.Views.StickerMakerView$$ExternalSyntheticLambda11
            @Override // java.lang.Runnable
            public final void run() {
                StickerMakerView.this.lambda$segmentImage$4(arrayList);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$segmentImage$3(ArrayList arrayList, Utilities.Callback callback, SegmentedObject segmentedObject) {
        this.empty = true;
        this.objects = (SegmentedObject[]) arrayList.toArray(new SegmentedObject[0]);
        callback.run(segmentedObject);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$segmentImage$4(ArrayList arrayList) {
        this.empty = false;
        SegmentedObject[] segmentedObjectArr = (SegmentedObject[]) arrayList.toArray(new SegmentedObject[0]);
        this.objects = segmentedObjectArr;
        if (segmentedObjectArr.length > 0) {
            this.stickerCutOutBtn.setScaleX(0.3f);
            this.stickerCutOutBtn.setScaleY(0.3f);
            this.stickerCutOutBtn.setAlpha(0.0f);
            this.stickerCutOutBtn.animate().alpha(1.0f).scaleX(1.0f).scaleY(1.0f).setDuration(250L).setInterpolator(CubicBezierInterpolator.DEFAULT).start();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes3.dex */
    public static class SubjectMock {
        public Bitmap bitmap;
        public int height;
        public int startX;
        public int startY;
        public int width;

        private SubjectMock() {
        }

        public static SubjectMock of(Subject subject) {
            SubjectMock subjectMock = new SubjectMock();
            subjectMock.bitmap = subject.getBitmap();
            subjectMock.startX = subject.getStartX();
            subjectMock.startY = subject.getStartY();
            subjectMock.width = subject.getWidth();
            subjectMock.height = subject.getHeight();
            return subjectMock;
        }

        public static SubjectMock mock(Bitmap bitmap) {
            SubjectMock subjectMock = new SubjectMock();
            int min = (int) (Math.min(bitmap.getWidth(), bitmap.getHeight()) * 0.4f);
            subjectMock.height = min;
            subjectMock.width = min;
            subjectMock.bitmap = Bitmap.createBitmap(min, min, Bitmap.Config.ARGB_8888);
            new Canvas(subjectMock.bitmap).drawRect(0.0f, 0.0f, subjectMock.width, subjectMock.height, Theme.DEBUG_RED);
            subjectMock.startX = (bitmap.getWidth() - subjectMock.width) / 2;
            subjectMock.startY = (bitmap.getHeight() - subjectMock.height) / 2;
            return subjectMock;
        }
    }

    private void segment(final Bitmap bitmap, final int i, final Utilities.Callback<List<SubjectMock>> callback, final Utilities.Callback<SegmentedObject> callback2) {
        this.segmentingLoading = true;
        SubjectSegmenter client = SubjectSegmentation.getClient(new SubjectSegmenterOptions.Builder().enableMultipleSubjects(new SubjectSegmenterOptions.SubjectResultOptions.Builder().enableSubjectBitmap().build()).build());
        if (EmuDetector.with(getContext()).detect()) {
            ArrayList arrayList = new ArrayList();
            arrayList.add(SubjectMock.mock(this.sourceBitmap));
            callback.run(arrayList);
            return;
        }
        InputImage fromBitmap = InputImage.fromBitmap(bitmap, i);
        client.process(fromBitmap).addOnSuccessListener(new OnSuccessListener() { // from class: org.telegram.ui.Components.Paint.Views.StickerMakerView$$ExternalSyntheticLambda4
            @Override // com.google.android.gms.tasks.OnSuccessListener
            public final void onSuccess(Object obj) {
                StickerMakerView.lambda$segment$7(Utilities.Callback.this, (SubjectSegmentationResult) obj);
            }
        }).addOnFailureListener(new OnFailureListener() { // from class: org.telegram.ui.Components.Paint.Views.StickerMakerView$$ExternalSyntheticLambda2
            @Override // com.google.android.gms.tasks.OnFailureListener
            public final void onFailure(Exception exc) {
                StickerMakerView.this.lambda$segment$9(bitmap, i, callback2, callback, exc);
            }
        });
        if (this.detectedEmoji == null) {
            ImageLabeling.getClient(ImageLabelerOptions.DEFAULT_OPTIONS).process(fromBitmap).addOnSuccessListener(new OnSuccessListener() { // from class: org.telegram.ui.Components.Paint.Views.StickerMakerView$$ExternalSyntheticLambda5
                @Override // com.google.android.gms.tasks.OnSuccessListener
                public final void onSuccess(Object obj) {
                    StickerMakerView.this.lambda$segment$10((List) obj);
                }
            }).addOnFailureListener(new OnFailureListener() { // from class: org.telegram.ui.Components.Paint.Views.StickerMakerView$$ExternalSyntheticLambda3
                @Override // com.google.android.gms.tasks.OnFailureListener
                public final void onFailure(Exception exc) {
                    StickerMakerView.lambda$segment$11(exc);
                }
            });
        }
        List<TLRPC$TL_availableReaction> enabledReactionsList = MediaDataController.getInstance(this.currentAccount).getEnabledReactionsList();
        for (int i2 = 0; i2 < Math.min(enabledReactionsList.size(), 9); i2++) {
            Emoji.getEmojiDrawable(enabledReactionsList.get(i2).reaction);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$segment$7(Utilities.Callback callback, SubjectSegmentationResult subjectSegmentationResult) {
        ArrayList arrayList = new ArrayList();
        for (int i = 0; i < subjectSegmentationResult.getSubjects().size(); i++) {
            arrayList.add(SubjectMock.of(subjectSegmentationResult.getSubjects().get(i)));
        }
        callback.run(arrayList);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$segment$9(final Bitmap bitmap, final int i, final Utilities.Callback callback, Utilities.Callback callback2, Exception exc) {
        this.segmentingLoading = false;
        FileLog.e(exc);
        if (isWaitingMlKitError(exc) && isAttachedToWindow()) {
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.Components.Paint.Views.StickerMakerView$$ExternalSyntheticLambda10
                @Override // java.lang.Runnable
                public final void run() {
                    StickerMakerView.this.lambda$segment$8(bitmap, i, callback);
                }
            }, 2000L);
        } else {
            callback2.run(new ArrayList());
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$segment$8(Bitmap bitmap, int i, Utilities.Callback callback) {
        segmentImage(bitmap, i, this.containerWidth, this.containerHeight, callback);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$segment$10(List list) {
        if (list.size() <= 0) {
            FileLog.d("objimg: no objects");
            return;
        }
        this.detectedEmoji = ObjectDetectionEmojis.labelToEmoji(((ImageLabel) list.get(0)).getIndex());
        FileLog.d("objimg: detected #" + ((ImageLabel) list.get(0)).getIndex() + " " + this.detectedEmoji + " " + ((ImageLabel) list.get(0)).getText());
        Emoji.getEmojiDrawable(this.detectedEmoji);
    }

    private void createSegmentImagePath(SegmentedObject segmentedObject, int i, int i2) {
        float f;
        int i3;
        Point point;
        int width = segmentedObject.getImage().getWidth();
        int height = segmentedObject.getImage().getHeight();
        float max = Math.max(width, height) / (SharedConfig.getDevicePerformanceClass() == 2 ? 512.0f : 384.0f);
        if ((segmentedObject.orientation / 90) % 2 != 0) {
            width = segmentedObject.getImage().getHeight();
            height = segmentedObject.getImage().getWidth();
        }
        float f2 = width;
        float f3 = height;
        Bitmap createBitmap = Bitmap.createBitmap((int) (f2 / max), (int) (f3 / max), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(createBitmap);
        RectF rectF = new RectF();
        rectF.set(0.0f, 0.0f, createBitmap.getWidth(), createBitmap.getHeight());
        if (segmentedObject.orientation != 0) {
            Matrix matrix = new Matrix();
            matrix.postRotate(segmentedObject.orientation, segmentedObject.getImage().getWidth() / 2.0f, segmentedObject.image.getHeight() / 2.0f);
            if ((segmentedObject.orientation / 90) % 2 != 0) {
                float height2 = (segmentedObject.getImage().getHeight() - segmentedObject.getImage().getWidth()) / 2.0f;
                matrix.postTranslate(height2, -height2);
            }
            matrix.postScale(rectF.width() / f2, rectF.height() / f3);
            canvas.drawBitmap(segmentedObject.getImage(), matrix, new Paint(3));
        } else {
            canvas.drawBitmap(segmentedObject.getImage(), (Rect) null, rectF, new Paint(3));
        }
        int width2 = createBitmap.getWidth() * createBitmap.getHeight();
        int[] iArr = new int[width2];
        createBitmap.getPixels(iArr, 0, createBitmap.getWidth(), 0, 0, createBitmap.getWidth(), createBitmap.getHeight());
        ArrayList arrayList = new ArrayList();
        ArrayList arrayList2 = new ArrayList();
        float f4 = i;
        float f5 = i2;
        float min = Math.min(f4 / createBitmap.getWidth(), f5 / createBitmap.getHeight());
        Point point2 = null;
        Point point3 = null;
        int i4 = 0;
        while (true) {
            if (i4 >= width2) {
                break;
            }
            int width3 = i4 / createBitmap.getWidth();
            int width4 = i4 - (createBitmap.getWidth() * width3);
            boolean z = iArr[i4] != 0;
            if (iArr[i4] == 0) {
                int i5 = i4 - 1;
                boolean z2 = i5 >= 0;
                Point point4 = point2;
                int i6 = i4 + 1;
                boolean z3 = i6 < width2;
                Point point5 = (!z2 || iArr[i5] == 0) ? point4 : new Point(width4, width3, min);
                if (point3 == null && z3 && iArr[i6] != 0) {
                    point3 = new Point(width4, width3, min);
                }
                point2 = point5;
            }
            boolean z4 = width4 == createBitmap.getWidth() + (-1);
            boolean z5 = width4 == 0;
            if (z4) {
                if (z) {
                    point2 = new Point(width4, width3, min);
                }
                if (point3 != null) {
                    arrayList.add(point3);
                }
                if (point2 != null) {
                    arrayList2.add(point2);
                }
                point2 = null;
                point3 = null;
            }
            if (z5 && z) {
                point3 = new Point(width4, width3, min);
            }
            i4++;
        }
        ArrayList arrayList3 = new ArrayList();
        ArrayList arrayList4 = new ArrayList();
        Point point6 = null;
        Point point7 = null;
        int i7 = 0;
        while (i7 < width2) {
            int height3 = i7 / createBitmap.getHeight();
            Point point8 = point6;
            int height4 = i7 - (createBitmap.getHeight() * height3);
            boolean z6 = iArr[height3 + (createBitmap.getWidth() * height4)] != 0;
            if (z6) {
                f = f3;
                i3 = width2;
                point = point8;
            } else {
                int width5 = height3 + ((height4 - 1) * createBitmap.getWidth());
                f = f3;
                int width6 = height3 + ((height4 + 1) * createBitmap.getWidth());
                boolean z7 = width5 >= 0;
                boolean z8 = width6 < width2;
                if (!z7 || iArr[width5] == 0) {
                    i3 = width2;
                    point = point8;
                } else {
                    i3 = width2;
                    point = new Point(height3, height4, min);
                }
                if (point7 == null && z8 && iArr[width6] != 0) {
                    point7 = new Point(height3, height4, min);
                }
            }
            boolean z9 = height4 == createBitmap.getHeight() + (-1);
            boolean z10 = height4 == 0;
            if (z9) {
                if (z6) {
                    point = new Point(height3, height4, min);
                }
                if (point7 != null) {
                    arrayList3.add(point7);
                }
                if (point != null) {
                    arrayList4.add(point);
                }
                point = null;
                point7 = null;
            }
            if (z10 && z6) {
                point7 = new Point(height3, height4, min);
            }
            i7++;
            point6 = point;
            width2 = i3;
            f3 = f;
        }
        float f6 = f3;
        LinkedHashSet linkedHashSet = new LinkedHashSet();
        LinkedHashSet linkedHashSet2 = new LinkedHashSet();
        Collections.reverse(arrayList2);
        Collections.reverse(arrayList3);
        linkedHashSet2.addAll(arrayList);
        linkedHashSet2.addAll(arrayList2);
        linkedHashSet.addAll(arrayList4);
        linkedHashSet.addAll(arrayList3);
        List<Point> removeUnnecessaryPoints = removeUnnecessaryPoints(new ArrayList(linkedHashSet));
        List<Point> removeUnnecessaryPoints2 = removeUnnecessaryPoints(new ArrayList(linkedHashSet2));
        Path path = new Path();
        for (int i8 = 0; i8 < removeUnnecessaryPoints2.size(); i8 += 2) {
            Point point9 = removeUnnecessaryPoints2.get(i8);
            if (path.isEmpty()) {
                path.moveTo(((android.graphics.Point) point9).x, ((android.graphics.Point) point9).y);
            } else {
                path.lineTo(((android.graphics.Point) point9).x, ((android.graphics.Point) point9).y);
            }
        }
        Path path2 = new Path();
        for (int i9 = 0; i9 < removeUnnecessaryPoints.size(); i9 += 2) {
            Point point10 = removeUnnecessaryPoints.get(i9);
            if (path2.isEmpty()) {
                path2.moveTo(((android.graphics.Point) point10).x, ((android.graphics.Point) point10).y);
            } else {
                path2.lineTo(((android.graphics.Point) point10).x, ((android.graphics.Point) point10).y);
            }
        }
        segmentedObject.segmentBorderPath.reset();
        segmentedObject.segmentBorderPath.op(path, path2, Path.Op.INTERSECT);
        float min2 = Math.min(f4 / f2, f5 / f6);
        segmentedObject.borderImageWidth = f2 * min2;
        segmentedObject.borderImageHeight = min2 * f6;
        segmentedObject.segmentBorderPath.offset((-segmentedObject.borderImageWidth) / 2.0f, (-segmentedObject.borderImageHeight) / 2.0f);
        segmentedObject.initPoints();
    }

    public static List<Point> removeUnnecessaryPoints(List<Point> list) {
        if (list.size() < 3) {
            return list;
        }
        ArrayList arrayList = new ArrayList();
        arrayList.add(list.get(0));
        int i = 1;
        while (i < list.size() - 1) {
            Point point = list.get(i);
            i++;
            if (!isPointOnLine(list.get(i - 1), point, list.get(i))) {
                arrayList.add(point);
            }
        }
        arrayList.add(list.get(list.size() - 1));
        return arrayList;
    }

    private static boolean isPointOnLine(Point point, Point point2, Point point3) {
        int i = ((android.graphics.Point) point2).x;
        int i2 = ((android.graphics.Point) point).x;
        int i3 = ((android.graphics.Point) point3).y;
        int i4 = ((android.graphics.Point) point).y;
        return Math.abs(((float) (((i - i2) * (i3 - i4)) - ((((android.graphics.Point) point2).y - i4) * (((android.graphics.Point) point3).x - i2)))) - (-1.0f)) < 0.15f;
    }

    public Bitmap cutSegmentInFilteredBitmap(Bitmap bitmap, int i) {
        if (bitmap == null) {
            return null;
        }
        SegmentedObject segmentedObject = this.selectedObject;
        if (segmentedObject == null) {
            return bitmap;
        }
        this.filteredBitmap = bitmap;
        if (segmentedObject.darkMaskImage == null || !this.isSegmentedState) {
            return bitmap;
        }
        Bitmap createBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(createBitmap);
        Paint paint = new Paint(3);
        Paint paint2 = new Paint(3);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OUT));
        canvas.drawBitmap(bitmap, 0.0f, 0.0f, paint2);
        Rect rect = new Rect();
        rect.set(0, 0, bitmap.getWidth(), bitmap.getHeight());
        SegmentedObject segmentedObject2 = this.selectedObject;
        if (segmentedObject2.orientation != 0) {
            Matrix matrix = new Matrix();
            SegmentedObject segmentedObject3 = this.selectedObject;
            matrix.postRotate(segmentedObject3.orientation, segmentedObject3.getDarkMaskImage().getWidth() / 2.0f, this.selectedObject.getDarkMaskImage().getHeight() / 2.0f);
            SegmentedObject segmentedObject4 = this.selectedObject;
            if ((segmentedObject4.orientation / 90) % 2 != 0) {
                float height = (segmentedObject4.getImage().getHeight() - this.selectedObject.getImage().getWidth()) / 2.0f;
                matrix.postTranslate(height, -height);
            }
            matrix.postScale(bitmap.getWidth() / this.selectedObject.getDarkMaskImage().getHeight(), bitmap.getHeight() / this.selectedObject.getDarkMaskImage().getWidth());
            canvas.drawBitmap(this.selectedObject.getDarkMaskImage(), matrix, paint);
        } else {
            canvas.drawBitmap(segmentedObject2.getDarkMaskImage(), (Rect) null, rect, paint);
        }
        return createBitmap;
    }

    public void clean() {
        ValueAnimator valueAnimator = this.bordersAnimator;
        if (valueAnimator != null) {
            valueAnimator.cancel();
            this.bordersAnimator = null;
        }
        this.sourceBitmap = null;
        if (this.objects != null) {
            int i = 0;
            while (true) {
                SegmentedObject[] segmentedObjectArr = this.objects;
                if (i >= segmentedObjectArr.length) {
                    break;
                }
                if (segmentedObjectArr[i] != null) {
                    segmentedObjectArr[i].recycle();
                }
                i++;
            }
            this.objects = null;
        }
        this.segmentingLoaded = false;
        this.segmentingLoading = false;
        this.isSegmentedState = false;
        this.actionTextView.setAlpha(0.0f);
        this.actionTextView.setScaleX(0.3f);
        this.actionTextView.setScaleY(0.3f);
        StickerUploader stickerUploader = this.stickerUploader;
        if (stickerUploader != null) {
            if (!stickerUploader.uploaded) {
                stickerUploader.destroy(true);
            }
            this.stickerUploader = null;
        }
        hideLoadingDialog();
        this.isThanosInProgress = false;
    }

    public static boolean isWaitingMlKitError(Exception exc) {
        return (exc instanceof MlKitException) && exc.getMessage() != null && exc.getMessage().contains("segmentation optional module to be downloaded");
    }

    public void setCurrentAccount(int i) {
        int i2 = this.currentAccount;
        if (i2 != i) {
            if (i2 >= 0 && isAttachedToWindow()) {
                NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.fileUploaded);
                NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.fileUploadProgressChanged);
                NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.fileUploadFailed);
                NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.filePreparingFailed);
                NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.filePreparingStarted);
                NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.fileNewChunkAvailable);
            }
            this.currentAccount = i;
            if (i < 0 || !isAttachedToWindow()) {
                return;
            }
            NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.fileUploaded);
            NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.fileUploadProgressChanged);
            NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.fileUploadFailed);
            NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.filePreparingFailed);
            NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.filePreparingStarted);
            NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.fileNewChunkAvailable);
        }
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        int i = this.currentAccount;
        if (i >= 0) {
            NotificationCenter.getInstance(i).removeObserver(this, NotificationCenter.fileUploaded);
            NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.fileUploadProgressChanged);
            NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.fileUploadFailed);
            NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.filePreparingFailed);
            NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.filePreparingStarted);
            NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.fileNewChunkAvailable);
        }
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.fileUploaded);
        NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.fileUploadProgressChanged);
        NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.fileUploadFailed);
        NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.filePreparingFailed);
        NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.filePreparingStarted);
        NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.fileNewChunkAvailable);
    }

    @Override // org.telegram.messenger.NotificationCenter.NotificationCenterDelegate
    public void didReceivedNotification(int i, int i2, Object... objArr) {
        StickerUploader stickerUploader;
        if (i == NotificationCenter.fileUploaded) {
            String str = (String) objArr[0];
            TLRPC$InputFile tLRPC$InputFile = (TLRPC$InputFile) objArr[1];
            StickerUploader stickerUploader2 = this.stickerUploader;
            if (stickerUploader2 == null || !str.equalsIgnoreCase(stickerUploader2.finalPath)) {
                return;
            }
            this.stickerUploader.file = tLRPC$InputFile;
            uploadMedia();
        } else if (i == NotificationCenter.fileUploadProgressChanged) {
            String str2 = (String) objArr[0];
            StickerUploader stickerUploader3 = this.stickerUploader;
            if (stickerUploader3 == null || !str2.equalsIgnoreCase(stickerUploader3.finalPath)) {
                return;
            }
            long longValue = ((Long) objArr[1]).longValue();
            long longValue2 = ((Long) objArr[2]).longValue();
            if (longValue2 > 0) {
                StickerUploader stickerUploader4 = this.stickerUploader;
                stickerUploader4.uploadProgress = Utilities.clamp(((float) longValue) / ((float) longValue2), 1.0f, stickerUploader4.uploadProgress);
                DownloadButton.PreparingVideoToast preparingVideoToast = this.loadingToast;
                if (preparingVideoToast != null) {
                    preparingVideoToast.setProgress(this.stickerUploader.getProgress());
                }
            }
        } else if (i == NotificationCenter.fileUploadFailed) {
            String str3 = (String) objArr[0];
            StickerUploader stickerUploader5 = this.stickerUploader;
            if (stickerUploader5 == null || !str3.equalsIgnoreCase(stickerUploader5.finalPath)) {
                return;
            }
            hideLoadingDialog();
        } else if (i == NotificationCenter.filePreparingStarted) {
            StickerUploader stickerUploader6 = this.stickerUploader;
            if (stickerUploader6 != null && objArr[0] == stickerUploader6.messageObject) {
                FileLoader.getInstance(UserConfig.selectedAccount).uploadFile(this.stickerUploader.finalPath, false, true, ConnectionsManager.FileTypeFile);
            }
        } else if (i == NotificationCenter.fileNewChunkAvailable) {
            StickerUploader stickerUploader7 = this.stickerUploader;
            if (stickerUploader7 != null && objArr[0] == stickerUploader7.messageObject) {
                String str4 = (String) objArr[1];
                long longValue3 = ((Long) objArr[2]).longValue();
                long longValue4 = ((Long) objArr[3]).longValue();
                float floatValue = ((Float) objArr[4]).floatValue();
                this.stickerUploader.messageObject.videoEditedInfo.needUpdateProgress = true;
                FileLoader.getInstance(this.currentAccount).checkUploadNewDataAvailable(str4, false, Math.max(1L, longValue3), longValue4, Float.valueOf(floatValue));
                StickerUploader stickerUploader8 = this.stickerUploader;
                stickerUploader8.convertingProgress = Math.max(stickerUploader8.convertingProgress, floatValue);
                DownloadButton.PreparingVideoToast preparingVideoToast2 = this.loadingToast;
                if (preparingVideoToast2 != null) {
                    preparingVideoToast2.setProgress(this.stickerUploader.getProgress());
                }
            }
        } else if (i == NotificationCenter.filePreparingFailed && (stickerUploader = this.stickerUploader) != null && objArr[0] == stickerUploader.messageObject) {
            hideLoadingDialog();
        }
    }

    public void uploadStickerFile(final String str, final VideoEditedInfo videoEditedInfo, final String str2, final CharSequence charSequence, final boolean z, final TLRPC$StickerSet tLRPC$StickerSet, final TLRPC$Document tLRPC$Document, final String str3, final Utilities.Callback<Boolean> callback, final Utilities.Callback2<String, TLRPC$InputDocument> callback2) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.Components.Paint.Views.StickerMakerView$$ExternalSyntheticLambda13
            @Override // java.lang.Runnable
            public final void run() {
                StickerMakerView.this.lambda$uploadStickerFile$12(callback, str2, str, charSequence, z, tLRPC$StickerSet, tLRPC$Document, videoEditedInfo, str3, callback2);
            }
        }, 300L);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$uploadStickerFile$12(Utilities.Callback callback, String str, String str2, CharSequence charSequence, boolean z, TLRPC$StickerSet tLRPC$StickerSet, TLRPC$Document tLRPC$Document, VideoEditedInfo videoEditedInfo, String str3, Utilities.Callback2 callback2) {
        StickerUploader stickerUploader;
        boolean z2 = callback == null || (stickerUploader = this.stickerUploader) == null || !stickerUploader.uploaded;
        if (z2) {
            StickerUploader stickerUploader2 = this.stickerUploader;
            if (stickerUploader2 != null) {
                stickerUploader2.destroy(true);
            }
            this.stickerUploader = new StickerUploader();
        }
        StickerUploader stickerUploader3 = this.stickerUploader;
        stickerUploader3.emoji = str;
        stickerUploader3.finalPath = str2;
        stickerUploader3.path = str2;
        stickerUploader3.stickerPackName = charSequence;
        stickerUploader3.addToFavorite = z;
        stickerUploader3.stickerSet = tLRPC$StickerSet;
        stickerUploader3.replacedSticker = tLRPC$Document;
        stickerUploader3.videoEditedInfo = videoEditedInfo;
        stickerUploader3.thumbPath = str3;
        stickerUploader3.whenDone = callback;
        stickerUploader3.customHandler = callback2;
        stickerUploader3.setupFiles();
        if (!z2) {
            afterUploadingMedia();
        } else if (videoEditedInfo != null) {
            TLRPC$TL_message tLRPC$TL_message = new TLRPC$TL_message();
            tLRPC$TL_message.id = 1;
            StickerUploader stickerUploader4 = this.stickerUploader;
            String absolutePath = StoryEntry.makeCacheFile(UserConfig.selectedAccount, "webm").getAbsolutePath();
            tLRPC$TL_message.attachPath = absolutePath;
            stickerUploader4.finalPath = absolutePath;
            this.stickerUploader.messageObject = new MessageObject(UserConfig.selectedAccount, (TLRPC$Message) tLRPC$TL_message, (MessageObject) null, false, false);
            this.stickerUploader.messageObject.videoEditedInfo = videoEditedInfo;
            MediaController.getInstance().scheduleVideoConvert(this.stickerUploader.messageObject, false, false);
        } else {
            FileLoader.getInstance(this.currentAccount).uploadFile(str2, false, true, ConnectionsManager.FileTypeFile);
        }
        if (callback == null) {
            showLoadingDialog();
        }
    }

    private void showLoadingDialog() {
        if (this.loadingToast == null) {
            this.loadingToast = new DownloadButton.PreparingVideoToast(getContext());
        }
        this.loadingToast.setOnCancelListener(new Runnable() { // from class: org.telegram.ui.Components.Paint.Views.StickerMakerView$$ExternalSyntheticLambda7
            @Override // java.lang.Runnable
            public final void run() {
                StickerMakerView.this.lambda$showLoadingDialog$13();
            }
        });
        if (this.loadingToast.getParent() == null) {
            addView(this.loadingToast, LayoutHelper.createFrame(-1, -1, 17));
        }
        this.loadingToast.show();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$showLoadingDialog$13() {
        StickerUploader stickerUploader = this.stickerUploader;
        if (stickerUploader != null) {
            if (stickerUploader.messageObject != null) {
                MediaController.getInstance().cancelVideoConvert(this.stickerUploader.messageObject);
                FileLoader.getInstance(this.currentAccount).cancelFileUpload(this.stickerUploader.finalPath, false);
                if (this.stickerUploader.reqId != 0) {
                    ConnectionsManager.getInstance(this.currentAccount).cancelRequest(this.stickerUploader.reqId, true);
                }
            }
            this.stickerUploader.destroy(true);
            this.stickerUploader = null;
        }
        this.loadingToast.hide();
        this.loadingToast = null;
    }

    private void hideLoadingDialog() {
        DownloadButton.PreparingVideoToast preparingVideoToast = this.loadingToast;
        if (preparingVideoToast != null) {
            preparingVideoToast.hide();
            this.loadingToast = null;
        }
    }

    private void uploadMedia() {
        final StickerUploader stickerUploader = this.stickerUploader;
        if (stickerUploader == null) {
            return;
        }
        TLRPC$TL_messages_uploadMedia tLRPC$TL_messages_uploadMedia = new TLRPC$TL_messages_uploadMedia();
        tLRPC$TL_messages_uploadMedia.peer = new TLRPC$TL_inputPeerSelf();
        TLRPC$TL_inputMediaUploadedDocument tLRPC$TL_inputMediaUploadedDocument = new TLRPC$TL_inputMediaUploadedDocument();
        tLRPC$TL_messages_uploadMedia.media = tLRPC$TL_inputMediaUploadedDocument;
        tLRPC$TL_inputMediaUploadedDocument.file = stickerUploader.file;
        if (stickerUploader.videoEditedInfo != null) {
            tLRPC$TL_inputMediaUploadedDocument.mime_type = "video/webm";
        } else {
            tLRPC$TL_inputMediaUploadedDocument.mime_type = "image/webp";
        }
        TLRPC$TL_documentAttributeSticker tLRPC$TL_documentAttributeSticker = new TLRPC$TL_documentAttributeSticker();
        tLRPC$TL_documentAttributeSticker.alt = stickerUploader.emoji;
        tLRPC$TL_documentAttributeSticker.stickerset = new TLRPC$TL_inputStickerSetEmpty();
        tLRPC$TL_messages_uploadMedia.media.attributes.add(tLRPC$TL_documentAttributeSticker);
        ConnectionsManager.getInstance(UserConfig.selectedAccount).sendRequest(tLRPC$TL_messages_uploadMedia, new RequestDelegate() { // from class: org.telegram.ui.Components.Paint.Views.StickerMakerView$$ExternalSyntheticLambda26
            @Override // org.telegram.tgnet.RequestDelegate
            public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
                StickerMakerView.this.lambda$uploadMedia$15(stickerUploader, tLObject, tLRPC$TL_error);
            }
        }, 2);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$uploadMedia$15(final StickerUploader stickerUploader, final TLObject tLObject, final TLRPC$TL_error tLRPC$TL_error) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.Components.Paint.Views.StickerMakerView$$ExternalSyntheticLambda20
            @Override // java.lang.Runnable
            public final void run() {
                StickerMakerView.this.lambda$uploadMedia$14(tLObject, stickerUploader, tLRPC$TL_error);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$uploadMedia$14(TLObject tLObject, StickerUploader stickerUploader, TLRPC$TL_error tLRPC$TL_error) {
        if (tLObject instanceof TLRPC$TL_messageMediaDocument) {
            TLRPC$TL_messageMediaDocument tLRPC$TL_messageMediaDocument = (TLRPC$TL_messageMediaDocument) tLObject;
            stickerUploader.tlInputStickerSetItem = MediaDataController.getInputStickerSetItem(tLRPC$TL_messageMediaDocument.document, stickerUploader.emoji);
            stickerUploader.mediaDocument = tLRPC$TL_messageMediaDocument;
            afterUploadingMedia();
            return;
        }
        hideLoadingDialog();
        showError(tLRPC$TL_error);
    }

    private void showError(TLRPC$TL_error tLRPC$TL_error) {
        if (tLRPC$TL_error == null || "PACK_TITLE_INVALID".equals(tLRPC$TL_error.text)) {
            return;
        }
        BulletinFactory.of((FrameLayout) getParent(), this.resourcesProvider).createErrorBulletin(tLRPC$TL_error.text).show();
    }

    private void afterUploadingMedia() {
        final StickerUploader stickerUploader = this.stickerUploader;
        if (stickerUploader == null) {
            return;
        }
        final int i = UserConfig.selectedAccount;
        stickerUploader.uploaded = true;
        if (stickerUploader.customHandler != null) {
            hideLoadingDialog();
            stickerUploader.customHandler.run(stickerUploader.finalPath, stickerUploader.tlInputStickerSetItem.document);
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.Components.Paint.Views.StickerMakerView$$ExternalSyntheticLambda21
                @Override // java.lang.Runnable
                public final void run() {
                    StickerMakerView.lambda$afterUploadingMedia$16();
                }
            }, 250L);
        } else if (stickerUploader.replacedSticker != null) {
            TLRPC$TL_stickers_replaceSticker tLRPC$TL_stickers_replaceSticker = new TLRPC$TL_stickers_replaceSticker();
            tLRPC$TL_stickers_replaceSticker.sticker = MediaDataController.getInputStickerSetItem(stickerUploader.replacedSticker, stickerUploader.emoji).document;
            tLRPC$TL_stickers_replaceSticker.new_sticker = stickerUploader.tlInputStickerSetItem;
            ConnectionsManager.getInstance(i).sendRequest(tLRPC$TL_stickers_replaceSticker, new RequestDelegate() { // from class: org.telegram.ui.Components.Paint.Views.StickerMakerView$$ExternalSyntheticLambda23
                @Override // org.telegram.tgnet.RequestDelegate
                public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
                    StickerMakerView.this.lambda$afterUploadingMedia$19(i, stickerUploader, tLObject, tLRPC$TL_error);
                }
            });
        } else if (stickerUploader.stickerPackName != null) {
            TLRPC$TL_stickers_createStickerSet tLRPC$TL_stickers_createStickerSet = new TLRPC$TL_stickers_createStickerSet();
            tLRPC$TL_stickers_createStickerSet.user_id = new TLRPC$TL_inputUserSelf();
            tLRPC$TL_stickers_createStickerSet.title = stickerUploader.stickerPackName.toString();
            tLRPC$TL_stickers_createStickerSet.short_name = "";
            tLRPC$TL_stickers_createStickerSet.stickers.add(stickerUploader.tlInputStickerSetItem);
            ConnectionsManager.getInstance(i).sendRequest(tLRPC$TL_stickers_createStickerSet, new RequestDelegate() { // from class: org.telegram.ui.Components.Paint.Views.StickerMakerView$$ExternalSyntheticLambda25
                @Override // org.telegram.tgnet.RequestDelegate
                public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
                    StickerMakerView.this.lambda$afterUploadingMedia$22(i, stickerUploader, tLObject, tLRPC$TL_error);
                }
            });
        } else if (stickerUploader.addToFavorite) {
            hideLoadingDialog();
            NotificationCenter.getInstance(i).postNotificationNameOnUIThread(NotificationCenter.customStickerCreated, Boolean.FALSE);
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.Components.Paint.Views.StickerMakerView$$ExternalSyntheticLambda6
                @Override // java.lang.Runnable
                public final void run() {
                    StickerMakerView.lambda$afterUploadingMedia$23(StickerMakerView.StickerUploader.this);
                }
            }, 350L);
            Utilities.Callback<Boolean> callback = stickerUploader.whenDone;
            if (callback != null) {
                callback.run(Boolean.TRUE);
            }
        } else if (stickerUploader.stickerSet != null) {
            TLRPC$TL_stickers_addStickerToSet tLRPC$TL_stickers_addStickerToSet = new TLRPC$TL_stickers_addStickerToSet();
            tLRPC$TL_stickers_addStickerToSet.stickerset = MediaDataController.getInputStickerSet(stickerUploader.stickerSet);
            tLRPC$TL_stickers_addStickerToSet.sticker = stickerUploader.tlInputStickerSetItem;
            ConnectionsManager.getInstance(i).sendRequest(tLRPC$TL_stickers_addStickerToSet, new RequestDelegate() { // from class: org.telegram.ui.Components.Paint.Views.StickerMakerView$$ExternalSyntheticLambda24
                @Override // org.telegram.tgnet.RequestDelegate
                public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
                    StickerMakerView.this.lambda$afterUploadingMedia$26(i, stickerUploader, tLObject, tLRPC$TL_error);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$afterUploadingMedia$16() {
        NotificationCenter.getInstance(UserConfig.selectedAccount).postNotificationNameOnUIThread(NotificationCenter.customStickerCreated, new Object[0]);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$afterUploadingMedia$19(final int i, final StickerUploader stickerUploader, final TLObject tLObject, final TLRPC$TL_error tLRPC$TL_error) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.Components.Paint.Views.StickerMakerView$$ExternalSyntheticLambda15
            @Override // java.lang.Runnable
            public final void run() {
                StickerMakerView.this.lambda$afterUploadingMedia$18(tLObject, i, stickerUploader, tLRPC$TL_error);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$afterUploadingMedia$18(final TLObject tLObject, int i, final StickerUploader stickerUploader, TLRPC$TL_error tLRPC$TL_error) {
        boolean z;
        if (tLObject instanceof TLRPC$TL_messages_stickerSet) {
            TLRPC$TL_messages_stickerSet tLRPC$TL_messages_stickerSet = (TLRPC$TL_messages_stickerSet) tLObject;
            MediaDataController.getInstance(i).putStickerSet(tLRPC$TL_messages_stickerSet);
            if (!MediaDataController.getInstance(i).isStickerPackInstalled(tLRPC$TL_messages_stickerSet.set.id)) {
                MediaDataController.getInstance(i).toggleStickerSet(null, tLObject, 2, null, false, false);
            }
            DownloadButton.PreparingVideoToast preparingVideoToast = this.loadingToast;
            if (preparingVideoToast != null) {
                preparingVideoToast.setProgress(1.0f);
            }
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.Components.Paint.Views.StickerMakerView$$ExternalSyntheticLambda19
                @Override // java.lang.Runnable
                public final void run() {
                    StickerMakerView.this.lambda$afterUploadingMedia$17(tLObject, stickerUploader);
                }
            }, 450L);
            z = true;
        } else {
            showError(tLRPC$TL_error);
            hideLoadingDialog();
            z = false;
        }
        Utilities.Callback<Boolean> callback = stickerUploader.whenDone;
        if (callback != null) {
            callback.run(Boolean.valueOf(z));
            stickerUploader.whenDone = null;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$afterUploadingMedia$17(TLObject tLObject, StickerUploader stickerUploader) {
        NotificationCenter.getInstance(UserConfig.selectedAccount).postNotificationNameOnUIThread(NotificationCenter.customStickerCreated, Boolean.FALSE, tLObject, stickerUploader.mediaDocument.document, stickerUploader.thumbPath, Boolean.TRUE);
        hideLoadingDialog();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$afterUploadingMedia$22(final int i, final StickerUploader stickerUploader, final TLObject tLObject, final TLRPC$TL_error tLRPC$TL_error) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.Components.Paint.Views.StickerMakerView$$ExternalSyntheticLambda14
            @Override // java.lang.Runnable
            public final void run() {
                StickerMakerView.this.lambda$afterUploadingMedia$21(tLObject, i, stickerUploader, tLRPC$TL_error);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$afterUploadingMedia$21(final TLObject tLObject, int i, final StickerUploader stickerUploader, TLRPC$TL_error tLRPC$TL_error) {
        boolean z;
        if (tLObject instanceof TLRPC$TL_messages_stickerSet) {
            MediaDataController.getInstance(i).putStickerSet((TLRPC$TL_messages_stickerSet) tLObject);
            MediaDataController.getInstance(i).toggleStickerSet(null, tLObject, 2, null, false, false);
            DownloadButton.PreparingVideoToast preparingVideoToast = this.loadingToast;
            if (preparingVideoToast != null) {
                preparingVideoToast.setProgress(1.0f);
            }
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.Components.Paint.Views.StickerMakerView$$ExternalSyntheticLambda17
                @Override // java.lang.Runnable
                public final void run() {
                    StickerMakerView.this.lambda$afterUploadingMedia$20(tLObject, stickerUploader);
                }
            }, 250L);
            z = true;
        } else {
            showError(tLRPC$TL_error);
            hideLoadingDialog();
            z = false;
        }
        Utilities.Callback<Boolean> callback = stickerUploader.whenDone;
        if (callback != null) {
            callback.run(Boolean.valueOf(z));
            stickerUploader.whenDone = null;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$afterUploadingMedia$20(TLObject tLObject, StickerUploader stickerUploader) {
        NotificationCenter notificationCenter = NotificationCenter.getInstance(UserConfig.selectedAccount);
        int i = NotificationCenter.customStickerCreated;
        Boolean bool = Boolean.FALSE;
        notificationCenter.postNotificationNameOnUIThread(i, bool, tLObject, stickerUploader.mediaDocument.document, stickerUploader.thumbPath, bool);
        hideLoadingDialog();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$afterUploadingMedia$23(StickerUploader stickerUploader) {
        MediaDataController.getInstance(UserConfig.selectedAccount).addRecentSticker(2, null, stickerUploader.mediaDocument.document, (int) (System.currentTimeMillis() / 1000), false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$afterUploadingMedia$26(final int i, final StickerUploader stickerUploader, final TLObject tLObject, final TLRPC$TL_error tLRPC$TL_error) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.Components.Paint.Views.StickerMakerView$$ExternalSyntheticLambda16
            @Override // java.lang.Runnable
            public final void run() {
                StickerMakerView.this.lambda$afterUploadingMedia$25(tLObject, i, stickerUploader, tLRPC$TL_error);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$afterUploadingMedia$25(final TLObject tLObject, int i, final StickerUploader stickerUploader, TLRPC$TL_error tLRPC$TL_error) {
        boolean z;
        if (tLObject instanceof TLRPC$TL_messages_stickerSet) {
            TLRPC$TL_messages_stickerSet tLRPC$TL_messages_stickerSet = (TLRPC$TL_messages_stickerSet) tLObject;
            MediaDataController.getInstance(i).putStickerSet(tLRPC$TL_messages_stickerSet);
            if (!MediaDataController.getInstance(i).isStickerPackInstalled(tLRPC$TL_messages_stickerSet.set.id)) {
                MediaDataController.getInstance(i).toggleStickerSet(null, tLObject, 2, null, false, false);
            }
            DownloadButton.PreparingVideoToast preparingVideoToast = this.loadingToast;
            if (preparingVideoToast != null) {
                preparingVideoToast.setProgress(1.0f);
            }
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.Components.Paint.Views.StickerMakerView$$ExternalSyntheticLambda18
                @Override // java.lang.Runnable
                public final void run() {
                    StickerMakerView.this.lambda$afterUploadingMedia$24(tLObject, stickerUploader);
                }
            }, 450L);
            z = true;
        } else {
            showError(tLRPC$TL_error);
            hideLoadingDialog();
            z = false;
        }
        Utilities.Callback<Boolean> callback = stickerUploader.whenDone;
        if (callback != null) {
            callback.run(Boolean.valueOf(z));
            stickerUploader.whenDone = null;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$afterUploadingMedia$24(TLObject tLObject, StickerUploader stickerUploader) {
        NotificationCenter notificationCenter = NotificationCenter.getInstance(UserConfig.selectedAccount);
        int i = NotificationCenter.customStickerCreated;
        Boolean bool = Boolean.FALSE;
        notificationCenter.postNotificationNameOnUIThread(i, bool, tLObject, stickerUploader.mediaDocument.document, stickerUploader.thumbPath, bool);
        hideLoadingDialog();
    }

    /* loaded from: classes3.dex */
    public static class StickerUploader {
        public boolean addToFavorite;
        public Utilities.Callback2<String, TLRPC$InputDocument> customHandler;
        public String emoji;
        public TLRPC$InputFile file;
        public String finalPath;
        public TLRPC$TL_messageMediaDocument mediaDocument;
        public MessageObject messageObject;
        public String path;
        public TLRPC$Document replacedSticker;
        public int reqId;
        public CharSequence stickerPackName;
        public TLRPC$StickerSet stickerSet;
        public String thumbPath;
        public TLRPC$TL_inputStickerSetItem tlInputStickerSetItem;
        public boolean uploaded;
        public VideoEditedInfo videoEditedInfo;
        public Utilities.Callback<Boolean> whenDone;
        public ArrayList<File> finalFiles = new ArrayList<>();
        public ArrayList<File> files = new ArrayList<>();
        private float convertingProgress = 0.0f;
        private float uploadProgress = 0.0f;

        public float getProgress() {
            float f;
            float f2 = this.customHandler == null ? 0.9f : 1.0f;
            if (this.videoEditedInfo == null) {
                f = this.uploadProgress;
            } else {
                f = (this.convertingProgress * 0.5f) + (this.uploadProgress * 0.5f);
            }
            return f2 * f;
        }

        public void setupFiles() {
            if (!TextUtils.isEmpty(this.finalPath)) {
                this.finalFiles.add(new File(this.finalPath));
            }
            if (!TextUtils.isEmpty(this.path) && !TextUtils.equals(this.path, this.finalPath)) {
                this.files.add(new File(this.path));
            }
            if (TextUtils.isEmpty(this.thumbPath)) {
                return;
            }
            this.files.add(new File(this.thumbPath));
        }

        public void destroy(boolean z) {
            if (z) {
                Iterator<File> it = this.finalFiles.iterator();
                while (it.hasNext()) {
                    try {
                        it.next().delete();
                    } catch (Exception e) {
                        FileLog.e(e);
                    }
                }
            }
            this.finalFiles.clear();
            Iterator<File> it2 = this.files.iterator();
            while (it2.hasNext()) {
                try {
                    it2.next().delete();
                } catch (Exception e2) {
                    FileLog.e(e2);
                }
            }
            this.files.clear();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes3.dex */
    public static class Point extends android.graphics.Point {
        public Point(int i, int i2, float f) {
            super((int) (i * f), (int) (i2 * f));
        }
    }
}