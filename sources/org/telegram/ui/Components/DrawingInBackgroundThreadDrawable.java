package org.telegram.ui.Components;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import java.util.ArrayList;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.BuildVars;
import org.telegram.messenger.DispatchQueue;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.SharedConfig;
import org.telegram.ui.ActionBar.Theme;
/* loaded from: classes3.dex */
public class DrawingInBackgroundThreadDrawable implements NotificationCenter.NotificationCenterDelegate {
    private static DispatchQueue backgroundQueue;
    boolean attachedToWindow;
    Bitmap backgroundBitmap;
    Canvas backgroundCanvas;
    Bitmap bitmap;
    Canvas bitmapCanvas;
    private boolean bitmapUpdating;
    private int currentOpenedLayerFlags;
    boolean error;
    int frameGuid;
    int height;
    private int lastFrameId;
    Bitmap nextRenderingBitmap;
    Canvas nextRenderingCanvas;
    int padding;
    protected boolean paused;
    private boolean reset;
    int width;
    public int currentLayerNum = 1;
    private Paint paint = new Paint(1);
    Runnable bitmapCreateTask = new Runnable() { // from class: org.telegram.ui.Components.DrawingInBackgroundThreadDrawable.1
        /* JADX WARN: Code restructure failed: missing block: B:7:0x001b, code lost:
            if (r2.backgroundBitmap.getHeight() == r1) goto L8;
         */
        @Override // java.lang.Runnable
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public void run() {
            try {
                DrawingInBackgroundThreadDrawable drawingInBackgroundThreadDrawable = DrawingInBackgroundThreadDrawable.this;
                int i = drawingInBackgroundThreadDrawable.height + drawingInBackgroundThreadDrawable.padding;
                Bitmap bitmap = drawingInBackgroundThreadDrawable.backgroundBitmap;
                if (bitmap != null) {
                    int width = bitmap.getWidth();
                    DrawingInBackgroundThreadDrawable drawingInBackgroundThreadDrawable2 = DrawingInBackgroundThreadDrawable.this;
                    if (width == drawingInBackgroundThreadDrawable2.width) {
                    }
                }
                Bitmap bitmap2 = DrawingInBackgroundThreadDrawable.this.backgroundBitmap;
                if (bitmap2 != null) {
                    bitmap2.recycle();
                }
                DrawingInBackgroundThreadDrawable drawingInBackgroundThreadDrawable3 = DrawingInBackgroundThreadDrawable.this;
                drawingInBackgroundThreadDrawable3.backgroundBitmap = Bitmap.createBitmap(drawingInBackgroundThreadDrawable3.width, i, Bitmap.Config.ARGB_8888);
                DrawingInBackgroundThreadDrawable.this.backgroundCanvas = new Canvas(DrawingInBackgroundThreadDrawable.this.backgroundBitmap);
                DrawingInBackgroundThreadDrawable.this.backgroundBitmap.eraseColor(0);
                DrawingInBackgroundThreadDrawable.this.backgroundCanvas.save();
                DrawingInBackgroundThreadDrawable drawingInBackgroundThreadDrawable4 = DrawingInBackgroundThreadDrawable.this;
                drawingInBackgroundThreadDrawable4.backgroundCanvas.translate(0.0f, drawingInBackgroundThreadDrawable4.padding);
                DrawingInBackgroundThreadDrawable drawingInBackgroundThreadDrawable5 = DrawingInBackgroundThreadDrawable.this;
                drawingInBackgroundThreadDrawable5.drawInBackground(drawingInBackgroundThreadDrawable5.backgroundCanvas);
                DrawingInBackgroundThreadDrawable.this.backgroundCanvas.restore();
            } catch (Exception e) {
                FileLog.e(e);
                DrawingInBackgroundThreadDrawable.this.error = true;
            }
            AndroidUtilities.runOnUIThread(DrawingInBackgroundThreadDrawable.this.uiFrameRunnable);
        }
    };
    Runnable uiFrameRunnable = new Runnable() { // from class: org.telegram.ui.Components.DrawingInBackgroundThreadDrawable.2
        @Override // java.lang.Runnable
        public void run() {
            DrawingInBackgroundThreadDrawable.this.bitmapUpdating = false;
            DrawingInBackgroundThreadDrawable.this.onFrameReady();
            DrawingInBackgroundThreadDrawable drawingInBackgroundThreadDrawable = DrawingInBackgroundThreadDrawable.this;
            if (drawingInBackgroundThreadDrawable.attachedToWindow) {
                if (drawingInBackgroundThreadDrawable.frameGuid != drawingInBackgroundThreadDrawable.lastFrameId) {
                    return;
                }
                DrawingInBackgroundThreadDrawable drawingInBackgroundThreadDrawable2 = DrawingInBackgroundThreadDrawable.this;
                Bitmap bitmap = drawingInBackgroundThreadDrawable2.bitmap;
                Canvas canvas = drawingInBackgroundThreadDrawable2.bitmapCanvas;
                drawingInBackgroundThreadDrawable2.bitmap = drawingInBackgroundThreadDrawable2.nextRenderingBitmap;
                drawingInBackgroundThreadDrawable2.bitmapCanvas = drawingInBackgroundThreadDrawable2.nextRenderingCanvas;
                drawingInBackgroundThreadDrawable2.nextRenderingBitmap = drawingInBackgroundThreadDrawable2.backgroundBitmap;
                drawingInBackgroundThreadDrawable2.nextRenderingCanvas = drawingInBackgroundThreadDrawable2.backgroundCanvas;
                drawingInBackgroundThreadDrawable2.backgroundBitmap = bitmap;
                drawingInBackgroundThreadDrawable2.backgroundCanvas = canvas;
                return;
            }
            Bitmap bitmap2 = drawingInBackgroundThreadDrawable.backgroundBitmap;
            if (bitmap2 == null) {
                return;
            }
            bitmap2.recycle();
            DrawingInBackgroundThreadDrawable.this.backgroundBitmap = null;
        }
    };

    public void drawInBackground(Canvas canvas) {
        throw null;
    }

    protected void drawInUiThread(Canvas canvas, float f) {
        throw null;
    }

    public void onFrameReady() {
    }

    public void onPaused() {
    }

    public void onResume() {
    }

    public void prepareDraw(long j) {
        throw null;
    }

    public DrawingInBackgroundThreadDrawable() {
        if (backgroundQueue == null) {
            backgroundQueue = new DispatchQueue("draw_background_queue");
        }
    }

    public void draw(Canvas canvas, long j, int i, int i2, float f) {
        if (this.error) {
            if (!BuildVars.DEBUG_PRIVATE_VERSION) {
                return;
            }
            canvas.drawRect(0.0f, 0.0f, i, i2, Theme.DEBUG_RED);
            return;
        }
        this.height = i2;
        this.width = i;
        Bitmap bitmap = this.bitmap;
        if ((bitmap == null && this.nextRenderingBitmap == null) || this.reset) {
            this.reset = false;
            if (bitmap != null) {
                ArrayList arrayList = new ArrayList();
                arrayList.add(this.bitmap);
                AndroidUtilities.recycleBitmaps(arrayList);
                this.bitmap = null;
            }
            int i3 = this.height + this.padding;
            Bitmap bitmap2 = this.nextRenderingBitmap;
            if (bitmap2 == null || bitmap2.getHeight() != i3 || this.nextRenderingBitmap.getWidth() != this.width) {
                this.nextRenderingBitmap = Bitmap.createBitmap(this.width, i3, Bitmap.Config.ARGB_8888);
                this.nextRenderingCanvas = new Canvas(this.nextRenderingBitmap);
            } else {
                this.nextRenderingBitmap.eraseColor(0);
            }
            this.nextRenderingCanvas.save();
            this.nextRenderingCanvas.translate(0.0f, this.padding);
            drawInUiThread(this.nextRenderingCanvas, 1.0f);
            this.nextRenderingCanvas.restore();
        }
        if (!this.bitmapUpdating && !this.paused) {
            this.bitmapUpdating = true;
            prepareDraw(j);
            this.lastFrameId = this.frameGuid;
            backgroundQueue.postRunnable(this.bitmapCreateTask);
        }
        Bitmap bitmap3 = this.bitmap;
        if (bitmap3 == null && this.nextRenderingBitmap == null) {
            return;
        }
        if (bitmap3 == null) {
            bitmap3 = this.nextRenderingBitmap;
        }
        this.paint.setAlpha((int) (f * 255.0f));
        canvas.save();
        canvas.translate(0.0f, -this.padding);
        drawBitmap(canvas, bitmap3, this.paint);
        canvas.restore();
    }

    protected void drawBitmap(Canvas canvas, Bitmap bitmap, Paint paint) {
        canvas.drawBitmap(bitmap, 0.0f, 0.0f, paint);
    }

    public void onAttachToWindow() {
        this.attachedToWindow = true;
        int currentHeavyOperationFlags = NotificationCenter.getGlobalInstance().getCurrentHeavyOperationFlags();
        this.currentOpenedLayerFlags = currentHeavyOperationFlags;
        int i = currentHeavyOperationFlags & (this.currentLayerNum ^ (-1));
        this.currentOpenedLayerFlags = i;
        if (i == 0 && this.paused) {
            this.paused = false;
            onResume();
        }
        NotificationCenter.getGlobalInstance().addObserver(this, NotificationCenter.stopAllHeavyOperations);
        NotificationCenter.getGlobalInstance().addObserver(this, NotificationCenter.startAllHeavyOperations);
    }

    public void onDetachFromWindow() {
        ArrayList arrayList = new ArrayList();
        Bitmap bitmap = this.bitmap;
        if (bitmap != null) {
            arrayList.add(bitmap);
        }
        Bitmap bitmap2 = this.nextRenderingBitmap;
        if (bitmap2 != null) {
            arrayList.add(bitmap2);
        }
        this.bitmap = null;
        this.nextRenderingBitmap = null;
        AndroidUtilities.recycleBitmaps(arrayList);
        this.attachedToWindow = false;
        NotificationCenter.getGlobalInstance().removeObserver(this, NotificationCenter.stopAllHeavyOperations);
        NotificationCenter.getGlobalInstance().removeObserver(this, NotificationCenter.startAllHeavyOperations);
    }

    @Override // org.telegram.messenger.NotificationCenter.NotificationCenterDelegate
    public void didReceivedNotification(int i, int i2, Object... objArr) {
        int i3;
        if (i == NotificationCenter.stopAllHeavyOperations) {
            Integer num = (Integer) objArr[0];
            if (this.currentLayerNum >= num.intValue()) {
                return;
            }
            if (num.intValue() == 512 && SharedConfig.getDevicePerformanceClass() >= 2) {
                return;
            }
            int intValue = num.intValue() | this.currentOpenedLayerFlags;
            this.currentOpenedLayerFlags = intValue;
            if (intValue == 0 || this.paused) {
                return;
            }
            this.paused = true;
            onPaused();
        } else if (i != NotificationCenter.startAllHeavyOperations) {
        } else {
            Integer num2 = (Integer) objArr[0];
            if (this.currentLayerNum >= num2.intValue() || (i3 = this.currentOpenedLayerFlags) == 0) {
                return;
            }
            int intValue2 = (num2.intValue() ^ (-1)) & i3;
            this.currentOpenedLayerFlags = intValue2;
            if (intValue2 != 0 || !this.paused) {
                return;
            }
            this.paused = false;
            onResume();
        }
    }

    public void reset() {
        this.reset = true;
        this.frameGuid++;
        if (this.bitmap != null) {
            ArrayList arrayList = new ArrayList();
            arrayList.add(this.bitmap);
            this.bitmap = null;
            AndroidUtilities.recycleBitmaps(arrayList);
        }
    }
}