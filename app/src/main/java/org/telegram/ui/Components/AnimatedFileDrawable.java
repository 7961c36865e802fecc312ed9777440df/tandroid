package org.telegram.ui.Components;

import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.view.View;
import java.io.File;
import java.util.ArrayList;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.AnimatedFileDrawableStream;
import org.telegram.messenger.DispatchQueue;
import org.telegram.messenger.FileLoader;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.ImageLocation;
import org.telegram.messenger.ImageReceiver;
import org.telegram.messenger.utils.BitmapsCache;
import org.telegram.tgnet.TLRPC$Document;
import org.telegram.ui.Components.AnimatedFileDrawable;
/* loaded from: classes3.dex */
public class AnimatedFileDrawable extends BitmapDrawable implements Animatable, BitmapsCache.Cacheable {
    private RectF actualDrawRect;
    private boolean applyTransformation;
    private Bitmap backgroundBitmap;
    private int backgroundBitmapTime;
    private Paint backgroundPaint;
    private BitmapShader backgroundShader;
    BitmapsCache bitmapsCache;
    Runnable cacheGenRunnable;
    long cacheGenerateNativePtr;
    long cacheGenerateTimestamp;
    BitmapsCache.Metadata cacheMetadata;
    private Runnable cancelCache;
    private int currentAccount;
    private DispatchQueue decodeQueue;
    private boolean decodeSingleFrame;
    private boolean decoderCreated;
    private boolean destroyWhenDone;
    private final TLRPC$Document document;
    private final RectF dstRect;
    private RectF dstRectBackground;
    private float endTime;
    private boolean forceDecodeAfterNextFrame;
    boolean generatingCache;
    Bitmap generatingCacheBitmap;
    public boolean ignoreNoParent;
    private int invalidateAfter;
    private boolean invalidateParentViewWithSecond;
    private boolean invalidatePath;
    private volatile boolean isRecycled;
    private boolean isRestarted;
    private volatile boolean isRunning;
    public boolean isWebmSticker;
    private long lastFrameDecodeTime;
    private long lastFrameTime;
    int lastMetadata;
    private int lastTimeStamp;
    private boolean limitFps;
    private Runnable loadFrameRunnable;
    private Runnable loadFrameTask;
    private final Runnable mStartTask;
    private final int[] metaData;
    public volatile long nativePtr;
    private Bitmap nextRenderingBitmap;
    private int nextRenderingBitmapTime;
    private BitmapShader nextRenderingShader;
    private View parentView;
    private ArrayList<ImageReceiver> parents;
    private File path;
    private boolean pendingRemoveLoading;
    private int pendingRemoveLoadingFramesReset;
    private volatile long pendingSeekTo;
    private volatile long pendingSeekToUI;
    private boolean precache;
    private boolean recycleWithSecond;
    private Bitmap renderingBitmap;
    private int renderingBitmapTime;
    private int renderingHeight;
    private BitmapShader renderingShader;
    private int renderingWidth;
    public int repeatCount;
    private Path roundPath;
    private int[] roundRadius;
    private int[] roundRadiusBackup;
    private float scaleFactor;
    private float scaleX;
    private float scaleY;
    private ArrayList<View> secondParentViews;
    private Matrix shaderMatrix;
    private boolean singleFrameDecoded;
    public boolean skipFrameUpdate;
    private float startTime;
    private AnimatedFileDrawableStream stream;
    private long streamFileSize;
    private final Object sync;
    int tryCount;
    private Runnable uiRunnable;
    private Runnable uiRunnableGenerateCache;
    private Runnable uiRunnableNoFrame;
    private boolean useSharedQueue;
    private static float[] radii = new float[8];
    private static ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(8, new ThreadPoolExecutor.DiscardPolicy());

    /* JADX INFO: Access modifiers changed from: private */
    public static native long createDecoder(String str, int[] iArr, int i, long j, Object obj, boolean z);

    /* JADX INFO: Access modifiers changed from: private */
    public static native void destroyDecoder(long j);

    private static native int getFrameAtTime(long j, long j2, Bitmap bitmap, int[] iArr, int i);

    /* JADX INFO: Access modifiers changed from: private */
    public static native int getVideoFrame(long j, Bitmap bitmap, int[] iArr, int i, boolean z, float f, float f2);

    private static native void getVideoInfo(int i, String str, int[] iArr);

    private static native void prepareToSeek(long j);

    /* JADX INFO: Access modifiers changed from: private */
    public static native void seekToMs(long j, long j2, boolean z);

    private static native void stopDecoder(long j);

    @Override // android.graphics.drawable.BitmapDrawable, android.graphics.drawable.Drawable
    public int getOpacity() {
        return -2;
    }

    static /* synthetic */ int access$1010(AnimatedFileDrawable animatedFileDrawable) {
        int i = animatedFileDrawable.pendingRemoveLoadingFramesReset;
        animatedFileDrawable.pendingRemoveLoadingFramesReset = i - 1;
        return i;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes3.dex */
    public class 2 implements Runnable {
        2() {
        }

        @Override // java.lang.Runnable
        public void run() {
            if (AnimatedFileDrawable.this.isRecycled || AnimatedFileDrawable.this.destroyWhenDone) {
                return;
            }
            AnimatedFileDrawable animatedFileDrawable = AnimatedFileDrawable.this;
            if (animatedFileDrawable.generatingCache) {
                return;
            }
            animatedFileDrawable.startTime = (float) System.currentTimeMillis();
            if (RLottieDrawable.lottieCacheGenerateQueue == null) {
                RLottieDrawable.createCacheGenQueue();
            }
            AnimatedFileDrawable animatedFileDrawable2 = AnimatedFileDrawable.this;
            animatedFileDrawable2.generatingCache = true;
            animatedFileDrawable2.loadFrameTask = null;
            DispatchQueue dispatchQueue = RLottieDrawable.lottieCacheGenerateQueue;
            AnimatedFileDrawable animatedFileDrawable3 = AnimatedFileDrawable.this;
            Runnable runnable = new Runnable() { // from class: org.telegram.ui.Components.AnimatedFileDrawable$2$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    AnimatedFileDrawable.2.this.lambda$run$1();
                }
            };
            animatedFileDrawable3.cacheGenRunnable = runnable;
            dispatchQueue.postRunnable(runnable);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$run$1() {
            AnimatedFileDrawable.this.bitmapsCache.createCache();
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.Components.AnimatedFileDrawable$2$$ExternalSyntheticLambda1
                @Override // java.lang.Runnable
                public final void run() {
                    AnimatedFileDrawable.2.this.lambda$run$0();
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$run$0() {
            AnimatedFileDrawable animatedFileDrawable = AnimatedFileDrawable.this;
            animatedFileDrawable.generatingCache = false;
            animatedFileDrawable.scheduleNextGetFrame();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void chekDestroyDecoder() {
        if (this.loadFrameRunnable == null && this.destroyWhenDone && this.nativePtr != 0 && !this.generatingCache) {
            destroyDecoder(this.nativePtr);
            this.nativePtr = 0L;
        }
        if (!canLoadFrames()) {
            Bitmap bitmap = this.renderingBitmap;
            if (bitmap != null) {
                bitmap.recycle();
                this.renderingBitmap = null;
            }
            Bitmap bitmap2 = this.backgroundBitmap;
            if (bitmap2 != null) {
                bitmap2.recycle();
                this.backgroundBitmap = null;
            }
            DispatchQueue dispatchQueue = this.decodeQueue;
            if (dispatchQueue != null) {
                dispatchQueue.recycle();
                this.decodeQueue = null;
            }
            invalidateInternal();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void invalidateInternal() {
        for (int i = 0; i < this.parents.size(); i++) {
            this.parents.get(i).invalidate();
        }
    }

    public void checkRepeat() {
        if (this.ignoreNoParent) {
            start();
            return;
        }
        int i = 0;
        int i2 = 0;
        while (i < this.parents.size()) {
            ImageReceiver imageReceiver = this.parents.get(i);
            if (!imageReceiver.isAttachedToWindow()) {
                this.parents.remove(i);
                i--;
            }
            int i3 = imageReceiver.animatedFileDrawableRepeatMaxCount;
            if (i3 > 0 && this.repeatCount >= i3) {
                i2++;
            }
            i++;
        }
        if (this.parents.size() == i2) {
            stop();
        } else {
            start();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateScaleFactor() {
        int i;
        int i2;
        if (!this.isWebmSticker && (i = this.renderingHeight) > 0 && (i2 = this.renderingWidth) > 0) {
            int[] iArr = this.metaData;
            if (iArr[0] > 0 && iArr[1] > 0) {
                float max = Math.max(i2 / iArr[0], i / iArr[1]);
                this.scaleFactor = max;
                if (max > 0.0f && max <= 0.7d) {
                    return;
                }
                this.scaleFactor = 1.0f;
                return;
            }
        }
        this.scaleFactor = 1.0f;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0() {
        View view;
        if (!this.secondParentViews.isEmpty()) {
            int size = this.secondParentViews.size();
            for (int i = 0; i < size; i++) {
                this.secondParentViews.get(i).invalidate();
            }
        }
        if ((this.secondParentViews.isEmpty() || this.invalidateParentViewWithSecond) && (view = this.parentView) != null) {
            view.invalidate();
        }
    }

    public AnimatedFileDrawable(File file, boolean z, long j, TLRPC$Document tLRPC$Document, ImageLocation imageLocation, Object obj, long j2, int i, boolean z2, BitmapsCache.CacheOptions cacheOptions) {
        this(file, z, j, tLRPC$Document, imageLocation, obj, j2, i, z2, 0, 0, cacheOptions);
    }

    /* JADX WARN: Removed duplicated region for block: B:34:0x0154  */
    /* JADX WARN: Removed duplicated region for block: B:37:? A[RETURN, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public AnimatedFileDrawable(File file, boolean z, long j, TLRPC$Document tLRPC$Document, ImageLocation imageLocation, Object obj, long j2, int i, boolean z2, int i2, int i3, BitmapsCache.CacheOptions cacheOptions) {
        long j3;
        char c;
        char c2;
        boolean z3;
        long j4;
        long j5;
        this.invalidateAfter = 50;
        int[] iArr = new int[5];
        this.metaData = iArr;
        this.pendingSeekTo = -1L;
        this.pendingSeekToUI = -1L;
        this.sync = new Object();
        this.actualDrawRect = new RectF();
        this.roundRadius = new int[4];
        this.shaderMatrix = new Matrix();
        this.roundPath = new Path();
        this.scaleX = 1.0f;
        this.scaleY = 1.0f;
        this.dstRect = new RectF();
        this.scaleFactor = 1.0f;
        this.secondParentViews = new ArrayList<>();
        this.parents = new ArrayList<>();
        this.invalidatePath = true;
        this.uiRunnableNoFrame = new Runnable() { // from class: org.telegram.ui.Components.AnimatedFileDrawable.1
            @Override // java.lang.Runnable
            public void run() {
                AnimatedFileDrawable.this.chekDestroyDecoder();
                AnimatedFileDrawable.this.loadFrameTask = null;
                AnimatedFileDrawable.this.scheduleNextGetFrame();
                AnimatedFileDrawable.this.invalidateInternal();
            }
        };
        this.uiRunnableGenerateCache = new 2();
        this.uiRunnable = new Runnable() { // from class: org.telegram.ui.Components.AnimatedFileDrawable.3
            @Override // java.lang.Runnable
            public void run() {
                AnimatedFileDrawable.this.chekDestroyDecoder();
                if (AnimatedFileDrawable.this.stream != null && AnimatedFileDrawable.this.pendingRemoveLoading) {
                    FileLoader.getInstance(AnimatedFileDrawable.this.currentAccount).removeLoadingVideo(AnimatedFileDrawable.this.stream.getDocument(), false, false);
                }
                if (AnimatedFileDrawable.this.pendingRemoveLoadingFramesReset <= 0) {
                    AnimatedFileDrawable.this.pendingRemoveLoading = true;
                } else {
                    AnimatedFileDrawable.access$1010(AnimatedFileDrawable.this);
                }
                if (!AnimatedFileDrawable.this.forceDecodeAfterNextFrame) {
                    AnimatedFileDrawable.this.singleFrameDecoded = true;
                } else {
                    AnimatedFileDrawable.this.forceDecodeAfterNextFrame = false;
                }
                AnimatedFileDrawable.this.loadFrameTask = null;
                AnimatedFileDrawable animatedFileDrawable = AnimatedFileDrawable.this;
                animatedFileDrawable.nextRenderingBitmap = animatedFileDrawable.backgroundBitmap;
                AnimatedFileDrawable animatedFileDrawable2 = AnimatedFileDrawable.this;
                animatedFileDrawable2.nextRenderingBitmapTime = animatedFileDrawable2.backgroundBitmapTime;
                AnimatedFileDrawable animatedFileDrawable3 = AnimatedFileDrawable.this;
                animatedFileDrawable3.nextRenderingShader = animatedFileDrawable3.backgroundShader;
                if (AnimatedFileDrawable.this.isRestarted) {
                    AnimatedFileDrawable.this.isRestarted = false;
                    AnimatedFileDrawable animatedFileDrawable4 = AnimatedFileDrawable.this;
                    animatedFileDrawable4.repeatCount++;
                    animatedFileDrawable4.checkRepeat();
                }
                if (AnimatedFileDrawable.this.metaData[3] < AnimatedFileDrawable.this.lastTimeStamp) {
                    AnimatedFileDrawable animatedFileDrawable5 = AnimatedFileDrawable.this;
                    animatedFileDrawable5.lastTimeStamp = animatedFileDrawable5.startTime > 0.0f ? (int) (AnimatedFileDrawable.this.startTime * 1000.0f) : 0;
                }
                if (AnimatedFileDrawable.this.metaData[3] - AnimatedFileDrawable.this.lastTimeStamp != 0) {
                    AnimatedFileDrawable animatedFileDrawable6 = AnimatedFileDrawable.this;
                    animatedFileDrawable6.invalidateAfter = animatedFileDrawable6.metaData[3] - AnimatedFileDrawable.this.lastTimeStamp;
                    if (AnimatedFileDrawable.this.limitFps && AnimatedFileDrawable.this.invalidateAfter < 32) {
                        AnimatedFileDrawable.this.invalidateAfter = 32;
                    }
                }
                if (AnimatedFileDrawable.this.pendingSeekToUI >= 0 && AnimatedFileDrawable.this.pendingSeekTo == -1) {
                    AnimatedFileDrawable.this.pendingSeekToUI = -1L;
                    AnimatedFileDrawable.this.invalidateAfter = 0;
                }
                AnimatedFileDrawable animatedFileDrawable7 = AnimatedFileDrawable.this;
                animatedFileDrawable7.lastTimeStamp = animatedFileDrawable7.metaData[3];
                if (!AnimatedFileDrawable.this.secondParentViews.isEmpty()) {
                    int size = AnimatedFileDrawable.this.secondParentViews.size();
                    for (int i4 = 0; i4 < size; i4++) {
                        ((View) AnimatedFileDrawable.this.secondParentViews.get(i4)).invalidate();
                    }
                }
                AnimatedFileDrawable.this.invalidateInternal();
                AnimatedFileDrawable.this.scheduleNextGetFrame();
            }
        };
        this.loadFrameRunnable = new Runnable() { // from class: org.telegram.ui.Components.AnimatedFileDrawable.4
            @Override // java.lang.Runnable
            public void run() {
                if (!AnimatedFileDrawable.this.isRecycled) {
                    boolean z4 = false;
                    if (!AnimatedFileDrawable.this.decoderCreated && AnimatedFileDrawable.this.nativePtr == 0) {
                        AnimatedFileDrawable animatedFileDrawable = AnimatedFileDrawable.this;
                        animatedFileDrawable.nativePtr = AnimatedFileDrawable.createDecoder(animatedFileDrawable.path.getAbsolutePath(), AnimatedFileDrawable.this.metaData, AnimatedFileDrawable.this.currentAccount, AnimatedFileDrawable.this.streamFileSize, AnimatedFileDrawable.this.stream, false);
                        if (AnimatedFileDrawable.this.nativePtr != 0 && (AnimatedFileDrawable.this.metaData[0] > 3840 || AnimatedFileDrawable.this.metaData[1] > 3840)) {
                            AnimatedFileDrawable.destroyDecoder(AnimatedFileDrawable.this.nativePtr);
                            AnimatedFileDrawable.this.nativePtr = 0L;
                        }
                        AnimatedFileDrawable.this.updateScaleFactor();
                        AnimatedFileDrawable.this.decoderCreated = true;
                    }
                    try {
                        AnimatedFileDrawable animatedFileDrawable2 = AnimatedFileDrawable.this;
                        if (animatedFileDrawable2.bitmapsCache != null) {
                            if (animatedFileDrawable2.backgroundBitmap == null) {
                                AnimatedFileDrawable animatedFileDrawable3 = AnimatedFileDrawable.this;
                                animatedFileDrawable3.backgroundBitmap = Bitmap.createBitmap(animatedFileDrawable3.renderingWidth, AnimatedFileDrawable.this.renderingHeight, Bitmap.Config.ARGB_8888);
                            }
                            AnimatedFileDrawable animatedFileDrawable4 = AnimatedFileDrawable.this;
                            if (animatedFileDrawable4.cacheMetadata == null) {
                                animatedFileDrawable4.cacheMetadata = new BitmapsCache.Metadata();
                            }
                            AnimatedFileDrawable.this.lastFrameDecodeTime = System.currentTimeMillis();
                            AnimatedFileDrawable animatedFileDrawable5 = AnimatedFileDrawable.this;
                            int i4 = animatedFileDrawable5.cacheMetadata.frame;
                            int frame = animatedFileDrawable5.bitmapsCache.getFrame(animatedFileDrawable5.backgroundBitmap, AnimatedFileDrawable.this.cacheMetadata);
                            if (frame != -1) {
                                AnimatedFileDrawable animatedFileDrawable6 = AnimatedFileDrawable.this;
                                if (animatedFileDrawable6.cacheMetadata.frame < i4) {
                                    animatedFileDrawable6.isRestarted = true;
                                }
                            }
                            int[] iArr2 = AnimatedFileDrawable.this.metaData;
                            AnimatedFileDrawable animatedFileDrawable7 = AnimatedFileDrawable.this;
                            iArr2[3] = animatedFileDrawable7.backgroundBitmapTime = animatedFileDrawable7.cacheMetadata.frame * Math.max(16, animatedFileDrawable7.metaData[4] / Math.max(1, AnimatedFileDrawable.this.bitmapsCache.getFrameCount()));
                            if (AnimatedFileDrawable.this.bitmapsCache.needGenCache()) {
                                AndroidUtilities.runOnUIThread(AnimatedFileDrawable.this.uiRunnableGenerateCache);
                            }
                            if (frame == -1) {
                                AndroidUtilities.runOnUIThread(AnimatedFileDrawable.this.uiRunnableNoFrame);
                                return;
                            } else {
                                AndroidUtilities.runOnUIThread(AnimatedFileDrawable.this.uiRunnable);
                                return;
                            }
                        }
                        if (animatedFileDrawable2.nativePtr == 0 && AnimatedFileDrawable.this.metaData[0] != 0 && AnimatedFileDrawable.this.metaData[1] != 0) {
                            AndroidUtilities.runOnUIThread(AnimatedFileDrawable.this.uiRunnableNoFrame);
                            return;
                        }
                        if (AnimatedFileDrawable.this.backgroundBitmap == null && AnimatedFileDrawable.this.metaData[0] > 0 && AnimatedFileDrawable.this.metaData[1] > 0) {
                            AnimatedFileDrawable animatedFileDrawable8 = AnimatedFileDrawable.this;
                            animatedFileDrawable8.backgroundBitmap = Bitmap.createBitmap((int) (animatedFileDrawable8.metaData[0] * AnimatedFileDrawable.this.scaleFactor), (int) (AnimatedFileDrawable.this.metaData[1] * AnimatedFileDrawable.this.scaleFactor), Bitmap.Config.ARGB_8888);
                            if (AnimatedFileDrawable.this.backgroundShader == null && AnimatedFileDrawable.this.backgroundBitmap != null && AnimatedFileDrawable.this.hasRoundRadius()) {
                                AnimatedFileDrawable animatedFileDrawable9 = AnimatedFileDrawable.this;
                                Bitmap bitmap = AnimatedFileDrawable.this.backgroundBitmap;
                                Shader.TileMode tileMode = Shader.TileMode.CLAMP;
                                animatedFileDrawable9.backgroundShader = new BitmapShader(bitmap, tileMode, tileMode);
                            }
                        }
                        if (AnimatedFileDrawable.this.pendingSeekTo >= 0) {
                            AnimatedFileDrawable.this.metaData[3] = (int) AnimatedFileDrawable.this.pendingSeekTo;
                            long j6 = AnimatedFileDrawable.this.pendingSeekTo;
                            synchronized (AnimatedFileDrawable.this.sync) {
                                AnimatedFileDrawable.this.pendingSeekTo = -1L;
                            }
                            if (AnimatedFileDrawable.this.stream != null) {
                                AnimatedFileDrawable.this.stream.reset();
                            }
                            AnimatedFileDrawable.seekToMs(AnimatedFileDrawable.this.nativePtr, j6, true);
                            z4 = true;
                        }
                        if (AnimatedFileDrawable.this.backgroundBitmap != null) {
                            AnimatedFileDrawable.this.lastFrameDecodeTime = System.currentTimeMillis();
                            if (AnimatedFileDrawable.getVideoFrame(AnimatedFileDrawable.this.nativePtr, AnimatedFileDrawable.this.backgroundBitmap, AnimatedFileDrawable.this.metaData, AnimatedFileDrawable.this.backgroundBitmap.getRowBytes(), false, AnimatedFileDrawable.this.startTime, AnimatedFileDrawable.this.endTime) == 0) {
                                AndroidUtilities.runOnUIThread(AnimatedFileDrawable.this.uiRunnableNoFrame);
                                return;
                            }
                            if (AnimatedFileDrawable.this.metaData[3] < AnimatedFileDrawable.this.lastTimeStamp) {
                                AnimatedFileDrawable.this.isRestarted = true;
                            }
                            if (z4) {
                                AnimatedFileDrawable animatedFileDrawable10 = AnimatedFileDrawable.this;
                                animatedFileDrawable10.lastTimeStamp = animatedFileDrawable10.metaData[3];
                            }
                            AnimatedFileDrawable animatedFileDrawable11 = AnimatedFileDrawable.this;
                            animatedFileDrawable11.backgroundBitmapTime = animatedFileDrawable11.metaData[3];
                        }
                    } catch (Throwable th) {
                        FileLog.e(th);
                    }
                }
                AndroidUtilities.runOnUIThread(AnimatedFileDrawable.this.uiRunnable);
            }
        };
        this.mStartTask = new Runnable() { // from class: org.telegram.ui.Components.AnimatedFileDrawable$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                AnimatedFileDrawable.this.lambda$new$0();
            }
        };
        this.path = file;
        this.streamFileSize = j;
        this.currentAccount = i;
        this.renderingHeight = i3;
        this.renderingWidth = i2;
        this.precache = cacheOptions != null && i2 > 0 && i3 > 0;
        this.document = tLRPC$Document;
        getPaint().setFlags(3);
        if (j != 0 && (tLRPC$Document != null || imageLocation != null)) {
            this.stream = new AnimatedFileDrawableStream(tLRPC$Document, imageLocation, obj, i, z2);
        }
        if (!z || this.precache) {
            j3 = 0;
            c = 1;
            c2 = 0;
        } else {
            j3 = 0;
            c2 = 0;
            c = 1;
            this.nativePtr = createDecoder(file.getAbsolutePath(), iArr, this.currentAccount, this.streamFileSize, this.stream, z2);
            if (this.nativePtr != 0 && (iArr[0] > 3840 || iArr[1] > 3840)) {
                destroyDecoder(this.nativePtr);
                this.nativePtr = 0L;
            }
            updateScaleFactor();
            this.decoderCreated = true;
        }
        if (this.precache) {
            this.nativePtr = createDecoder(file.getAbsolutePath(), iArr, this.currentAccount, this.streamFileSize, this.stream, z2);
            if (this.nativePtr != j3 && (iArr[c2] > 3840 || iArr[c] > 3840)) {
                destroyDecoder(this.nativePtr);
                this.nativePtr = j3;
                j4 = j2;
                j5 = j3;
                z3 = false;
                if (j4 != j5) {
                    return;
                }
                seekTo(j4, z3);
                return;
            }
            z3 = false;
            this.bitmapsCache = new BitmapsCache(file, this, cacheOptions, this.renderingWidth, this.renderingHeight, !this.limitFps);
        } else {
            z3 = false;
        }
        j4 = j2;
        j5 = j3;
        if (j4 != j5) {
        }
    }

    public void setIsWebmSticker(boolean z) {
        this.isWebmSticker = z;
        if (z) {
            this.useSharedQueue = true;
        }
    }

    public Bitmap getFrameAtTime(long j) {
        return getFrameAtTime(j, false);
    }

    public Bitmap getFrameAtTime(long j, boolean z) {
        int videoFrame;
        if (!this.decoderCreated || this.nativePtr == 0) {
            return null;
        }
        AnimatedFileDrawableStream animatedFileDrawableStream = this.stream;
        if (animatedFileDrawableStream != null) {
            animatedFileDrawableStream.cancel(false);
            this.stream.reset();
        }
        if (!z) {
            seekToMs(this.nativePtr, j, z);
        }
        if (this.backgroundBitmap == null) {
            int[] iArr = this.metaData;
            float f = this.scaleFactor;
            this.backgroundBitmap = Bitmap.createBitmap((int) (iArr[0] * f), (int) (iArr[1] * f), Bitmap.Config.ARGB_8888);
        }
        if (z) {
            long j2 = this.nativePtr;
            Bitmap bitmap = this.backgroundBitmap;
            videoFrame = getFrameAtTime(j2, j, bitmap, this.metaData, bitmap.getRowBytes());
        } else {
            long j3 = this.nativePtr;
            Bitmap bitmap2 = this.backgroundBitmap;
            videoFrame = getVideoFrame(j3, bitmap2, this.metaData, bitmap2.getRowBytes(), true, 0.0f, 0.0f);
        }
        if (videoFrame == 0) {
            return null;
        }
        return this.backgroundBitmap;
    }

    public void setParentView(View view) {
        if (this.parentView != null) {
            return;
        }
        this.parentView = view;
    }

    public void addParent(ImageReceiver imageReceiver) {
        if (imageReceiver != null && !this.parents.contains(imageReceiver)) {
            this.parents.add(imageReceiver);
            if (this.isRunning) {
                scheduleNextGetFrame();
            }
        }
        checkCacheCancel();
    }

    public void removeParent(ImageReceiver imageReceiver) {
        this.parents.remove(imageReceiver);
        if (this.parents.size() == 0) {
            this.repeatCount = 0;
        }
        checkCacheCancel();
    }

    public void checkCacheCancel() {
        Runnable runnable;
        if (this.bitmapsCache == null) {
            return;
        }
        boolean isEmpty = this.parents.isEmpty();
        if (isEmpty && this.cancelCache == null) {
            Runnable runnable2 = new Runnable() { // from class: org.telegram.ui.Components.AnimatedFileDrawable$$ExternalSyntheticLambda1
                @Override // java.lang.Runnable
                public final void run() {
                    AnimatedFileDrawable.this.lambda$checkCacheCancel$1();
                }
            };
            this.cancelCache = runnable2;
            AndroidUtilities.runOnUIThread(runnable2, 600L);
        } else if (isEmpty || (runnable = this.cancelCache) == null) {
        } else {
            AndroidUtilities.cancelRunOnUIThread(runnable);
            this.cancelCache = null;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$checkCacheCancel$1() {
        BitmapsCache bitmapsCache = this.bitmapsCache;
        if (bitmapsCache != null) {
            bitmapsCache.cancelCreate();
        }
    }

    public void setInvalidateParentViewWithSecond(boolean z) {
        this.invalidateParentViewWithSecond = z;
    }

    public void addSecondParentView(View view) {
        if (view == null || this.secondParentViews.contains(view)) {
            return;
        }
        this.secondParentViews.add(view);
    }

    public void removeSecondParentView(View view) {
        this.secondParentViews.remove(view);
        if (this.secondParentViews.isEmpty()) {
            if (this.recycleWithSecond) {
                recycle();
                return;
            }
            int[] iArr = this.roundRadiusBackup;
            if (iArr == null) {
                return;
            }
            setRoundRadius(iArr);
        }
    }

    public void setAllowDecodeSingleFrame(boolean z) {
        this.decodeSingleFrame = z;
        if (z) {
            scheduleNextGetFrame();
        }
    }

    public void seekTo(long j, boolean z) {
        seekTo(j, z, false);
    }

    public void seekTo(long j, boolean z, boolean z2) {
        AnimatedFileDrawableStream animatedFileDrawableStream;
        synchronized (this.sync) {
            this.pendingSeekTo = j;
            this.pendingSeekToUI = j;
            if (this.nativePtr != 0) {
                prepareToSeek(this.nativePtr);
            }
            if (this.decoderCreated && (animatedFileDrawableStream = this.stream) != null) {
                animatedFileDrawableStream.cancel(z);
                this.pendingRemoveLoading = z;
                this.pendingRemoveLoadingFramesReset = z ? 0 : 10;
            }
            if (z2 && this.decodeSingleFrame) {
                this.singleFrameDecoded = false;
                if (this.loadFrameTask == null) {
                    scheduleNextGetFrame();
                } else {
                    this.forceDecodeAfterNextFrame = true;
                }
            }
        }
    }

    public void recycle() {
        if (!this.secondParentViews.isEmpty()) {
            this.recycleWithSecond = true;
            return;
        }
        this.isRunning = false;
        this.isRecycled = true;
        Runnable runnable = this.cacheGenRunnable;
        if (runnable != null) {
            RLottieDrawable.lottieCacheGenerateQueue.cancelRunnable(runnable);
        }
        if (this.loadFrameTask == null) {
            if (this.nativePtr != 0) {
                destroyDecoder(this.nativePtr);
                this.nativePtr = 0L;
            }
            ArrayList arrayList = new ArrayList();
            arrayList.add(this.renderingBitmap);
            arrayList.add(this.nextRenderingBitmap);
            if (this.renderingBitmap != null) {
                this.renderingBitmap = null;
            }
            if (this.nextRenderingBitmap != null) {
                this.nextRenderingBitmap = null;
            }
            DispatchQueue dispatchQueue = this.decodeQueue;
            if (dispatchQueue != null) {
                dispatchQueue.recycle();
                this.decodeQueue = null;
            }
            getPaint().setShader(null);
            AndroidUtilities.recycleBitmaps(arrayList);
        } else {
            this.destroyWhenDone = true;
        }
        AnimatedFileDrawableStream animatedFileDrawableStream = this.stream;
        if (animatedFileDrawableStream != null) {
            animatedFileDrawableStream.cancel(true);
        }
        invalidateInternal();
    }

    public void resetStream(boolean z) {
        AnimatedFileDrawableStream animatedFileDrawableStream = this.stream;
        if (animatedFileDrawableStream != null) {
            animatedFileDrawableStream.cancel(true);
        }
        if (this.nativePtr != 0) {
            if (z) {
                stopDecoder(this.nativePtr);
            } else {
                prepareToSeek(this.nativePtr);
            }
        }
    }

    public void setUseSharedQueue(boolean z) {
        if (this.isWebmSticker) {
            return;
        }
        this.useSharedQueue = z;
    }

    protected void finalize() throws Throwable {
        try {
            recycle();
        } finally {
            super.finalize();
        }
    }

    @Override // android.graphics.drawable.Animatable
    public void start() {
        if (!this.isRunning) {
            if (this.parents.size() == 0 && !this.ignoreNoParent) {
                return;
            }
            this.isRunning = true;
            scheduleNextGetFrame();
            AndroidUtilities.runOnUIThread(this.mStartTask);
        }
    }

    public float getCurrentProgress() {
        if (this.metaData[4] == 0) {
            return 0.0f;
        }
        if (this.pendingSeekToUI >= 0) {
            return ((float) this.pendingSeekToUI) / this.metaData[4];
        }
        int[] iArr = this.metaData;
        return iArr[3] / iArr[4];
    }

    public int getCurrentProgressMs() {
        if (this.pendingSeekToUI >= 0) {
            return (int) this.pendingSeekToUI;
        }
        int i = this.nextRenderingBitmapTime;
        return i != 0 ? i : this.renderingBitmapTime;
    }

    public int getDurationMs() {
        return this.metaData[4];
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void scheduleNextGetFrame() {
        if (this.loadFrameTask != null || !canLoadFrames() || this.destroyWhenDone) {
            return;
        }
        if (!this.isRunning) {
            boolean z = this.decodeSingleFrame;
            if (!z) {
                return;
            }
            if (z && this.singleFrameDecoded) {
                return;
            }
        }
        if ((this.parents.size() == 0 && !this.ignoreNoParent) || this.generatingCache) {
            return;
        }
        long j = 0;
        if (this.lastFrameDecodeTime != 0) {
            int i = this.invalidateAfter;
            j = Math.min(i, Math.max(0L, i - (System.currentTimeMillis() - this.lastFrameDecodeTime)));
        }
        if (this.useSharedQueue) {
            ScheduledThreadPoolExecutor scheduledThreadPoolExecutor = executor;
            Runnable runnable = this.loadFrameRunnable;
            this.loadFrameTask = runnable;
            scheduledThreadPoolExecutor.schedule(runnable, j, TimeUnit.MILLISECONDS);
            return;
        }
        if (this.decodeQueue == null) {
            this.decodeQueue = new DispatchQueue("decodeQueue" + this);
        }
        DispatchQueue dispatchQueue = this.decodeQueue;
        Runnable runnable2 = this.loadFrameRunnable;
        this.loadFrameTask = runnable2;
        dispatchQueue.postRunnable(runnable2, j);
    }

    public boolean isLoadingStream() {
        AnimatedFileDrawableStream animatedFileDrawableStream = this.stream;
        return animatedFileDrawableStream != null && animatedFileDrawableStream.isWaitingForLoad();
    }

    @Override // android.graphics.drawable.Animatable
    public void stop() {
        this.isRunning = false;
    }

    @Override // android.graphics.drawable.Animatable
    public boolean isRunning() {
        return this.isRunning;
    }

    @Override // android.graphics.drawable.BitmapDrawable, android.graphics.drawable.Drawable
    public int getIntrinsicHeight() {
        int i = 0;
        if (this.decoderCreated) {
            int[] iArr = this.metaData;
            i = (iArr[2] == 90 || iArr[2] == 270) ? iArr[0] : iArr[1];
        }
        if (i == 0) {
            return AndroidUtilities.dp(100.0f);
        }
        return (int) (i * this.scaleFactor);
    }

    @Override // android.graphics.drawable.BitmapDrawable, android.graphics.drawable.Drawable
    public int getIntrinsicWidth() {
        int i = 0;
        if (this.decoderCreated) {
            int[] iArr = this.metaData;
            i = (iArr[2] == 90 || iArr[2] == 270) ? iArr[1] : iArr[0];
        }
        if (i == 0) {
            return AndroidUtilities.dp(100.0f);
        }
        return (int) (i * this.scaleFactor);
    }

    @Override // android.graphics.drawable.BitmapDrawable, android.graphics.drawable.Drawable
    protected void onBoundsChange(android.graphics.Rect rect) {
        super.onBoundsChange(rect);
        this.applyTransformation = true;
    }

    @Override // android.graphics.drawable.BitmapDrawable, android.graphics.drawable.Drawable
    public void draw(Canvas canvas) {
        drawInternal(canvas, false, System.currentTimeMillis());
    }

    public void drawInBackground(Canvas canvas, float f, float f2, float f3, float f4, int i, ColorFilter colorFilter) {
        if (this.dstRectBackground == null) {
            this.dstRectBackground = new RectF();
            Paint paint = new Paint();
            this.backgroundPaint = paint;
            paint.setFilterBitmap(true);
        }
        this.backgroundPaint.setAlpha(i);
        this.backgroundPaint.setColorFilter(colorFilter);
        this.dstRectBackground.set(f, f2, f3 + f, f4 + f2);
        drawInternal(canvas, true, 0L);
    }

    /* JADX WARN: Removed duplicated region for block: B:27:0x00af  */
    /* JADX WARN: Removed duplicated region for block: B:50:0x015a  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void drawInternal(Canvas canvas, boolean z, long j) {
        float width;
        if (!canLoadFrames() || this.destroyWhenDone) {
            return;
        }
        long currentTimeMillis = j == 0 ? System.currentTimeMillis() : j;
        RectF rectF = z ? this.dstRectBackground : this.dstRect;
        Paint paint = z ? this.backgroundPaint : getPaint();
        if (!z) {
            updateCurrentFrame(currentTimeMillis, false);
        }
        Bitmap bitmap = this.renderingBitmap;
        if (bitmap == null) {
            return;
        }
        float f = this.scaleX;
        float f2 = this.scaleY;
        if (z) {
            int width2 = bitmap.getWidth();
            int height = this.renderingBitmap.getHeight();
            int[] iArr = this.metaData;
            if (iArr[2] == 90 || iArr[2] == 270) {
                height = width2;
                width2 = height;
            }
            width = rectF.width() / width2;
            f2 = rectF.height() / height;
        } else {
            if (this.applyTransformation) {
                int width3 = bitmap.getWidth();
                int height2 = this.renderingBitmap.getHeight();
                int[] iArr2 = this.metaData;
                if (iArr2[2] == 90 || iArr2[2] == 270) {
                    height2 = width3;
                    width3 = height2;
                }
                rectF.set(getBounds());
                width = rectF.width() / width3;
                this.scaleX = width;
                f2 = rectF.height() / height2;
                this.scaleY = f2;
                this.applyTransformation = false;
            }
            if (!hasRoundRadius()) {
                if (this.renderingShader == null) {
                    Bitmap bitmap2 = this.renderingBitmap;
                    Shader.TileMode tileMode = Shader.TileMode.CLAMP;
                    this.renderingShader = new BitmapShader(bitmap2, tileMode, tileMode);
                }
                paint.setShader(this.renderingShader);
                this.shaderMatrix.reset();
                this.shaderMatrix.setTranslate(rectF.left, rectF.top);
                int[] iArr3 = this.metaData;
                if (iArr3[2] == 90) {
                    this.shaderMatrix.preRotate(90.0f);
                    this.shaderMatrix.preTranslate(0.0f, -rectF.width());
                } else if (iArr3[2] == 180) {
                    this.shaderMatrix.preRotate(180.0f);
                    this.shaderMatrix.preTranslate(-rectF.width(), -rectF.height());
                } else if (iArr3[2] == 270) {
                    this.shaderMatrix.preRotate(270.0f);
                    this.shaderMatrix.preTranslate(-rectF.height(), 0.0f);
                }
                this.shaderMatrix.preScale(f, f2);
                this.renderingShader.setLocalMatrix(this.shaderMatrix);
                if (this.invalidatePath) {
                    this.invalidatePath = false;
                    int i = 0;
                    while (true) {
                        int[] iArr4 = this.roundRadius;
                        if (i >= iArr4.length) {
                            break;
                        }
                        float[] fArr = radii;
                        int i2 = i * 2;
                        fArr[i2] = iArr4[i];
                        fArr[i2 + 1] = iArr4[i];
                        i++;
                    }
                    this.roundPath.reset();
                    this.roundPath.addRoundRect(rectF, radii, Path.Direction.CW);
                    this.roundPath.close();
                }
                canvas.drawPath(this.roundPath, paint);
                return;
            }
            canvas.translate(rectF.left, rectF.top);
            int[] iArr5 = this.metaData;
            if (iArr5[2] == 90) {
                canvas.rotate(90.0f);
                canvas.translate(0.0f, -rectF.width());
            } else if (iArr5[2] == 180) {
                canvas.rotate(180.0f);
                canvas.translate(-rectF.width(), -rectF.height());
            } else if (iArr5[2] == 270) {
                canvas.rotate(270.0f);
                canvas.translate(-rectF.height(), 0.0f);
            }
            canvas.scale(f, f2);
            canvas.drawBitmap(this.renderingBitmap, 0.0f, 0.0f, paint);
            return;
        }
        f = width;
        if (!hasRoundRadius()) {
        }
    }

    public long getLastFrameTimestamp() {
        return this.lastTimeStamp;
    }

    @Override // android.graphics.drawable.Drawable
    public int getMinimumHeight() {
        int i = 0;
        if (this.decoderCreated) {
            int[] iArr = this.metaData;
            i = (iArr[2] == 90 || iArr[2] == 270) ? iArr[0] : iArr[1];
        }
        return i == 0 ? AndroidUtilities.dp(100.0f) : i;
    }

    @Override // android.graphics.drawable.Drawable
    public int getMinimumWidth() {
        int i = 0;
        if (this.decoderCreated) {
            int[] iArr = this.metaData;
            i = (iArr[2] == 90 || iArr[2] == 270) ? iArr[1] : iArr[0];
        }
        return i == 0 ? AndroidUtilities.dp(100.0f) : i;
    }

    public Bitmap getBackgroundBitmap() {
        return this.backgroundBitmap;
    }

    public Bitmap getAnimatedBitmap() {
        Bitmap bitmap = this.renderingBitmap;
        if (bitmap != null) {
            return bitmap;
        }
        Bitmap bitmap2 = this.nextRenderingBitmap;
        if (bitmap2 == null) {
            return null;
        }
        return bitmap2;
    }

    public void setActualDrawRect(float f, float f2, float f3, float f4) {
        float f5 = f4 + f2;
        float f6 = f3 + f;
        RectF rectF = this.actualDrawRect;
        if (rectF.left == f && rectF.top == f2 && rectF.right == f6 && rectF.bottom == f5) {
            return;
        }
        rectF.set(f, f2, f6, f5);
        this.invalidatePath = true;
    }

    public void setRoundRadius(int[] iArr) {
        if (!this.secondParentViews.isEmpty()) {
            if (this.roundRadiusBackup == null) {
                this.roundRadiusBackup = new int[4];
            }
            int[] iArr2 = this.roundRadius;
            int[] iArr3 = this.roundRadiusBackup;
            System.arraycopy(iArr2, 0, iArr3, 0, iArr3.length);
        }
        for (int i = 0; i < 4; i++) {
            if (!this.invalidatePath && iArr[i] != this.roundRadius[i]) {
                this.invalidatePath = true;
            }
            this.roundRadius[i] = iArr[i];
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean hasRoundRadius() {
        int i = 0;
        while (true) {
            int[] iArr = this.roundRadius;
            if (i < iArr.length) {
                if (iArr[i] != 0) {
                    return true;
                }
                i++;
            } else {
                return false;
            }
        }
    }

    public boolean hasBitmap() {
        return canLoadFrames() && !(this.renderingBitmap == null && this.nextRenderingBitmap == null);
    }

    public int getOrientation() {
        return this.metaData[2];
    }

    public AnimatedFileDrawable makeCopy() {
        AnimatedFileDrawable animatedFileDrawable;
        AnimatedFileDrawableStream animatedFileDrawableStream = this.stream;
        if (animatedFileDrawableStream != null) {
            File file = this.path;
            long j = this.streamFileSize;
            TLRPC$Document document = animatedFileDrawableStream.getDocument();
            ImageLocation location = this.stream.getLocation();
            Object parentObject = this.stream.getParentObject();
            long j2 = this.pendingSeekToUI;
            int i = this.currentAccount;
            AnimatedFileDrawableStream animatedFileDrawableStream2 = this.stream;
            animatedFileDrawable = new AnimatedFileDrawable(file, false, j, document, location, parentObject, j2, i, animatedFileDrawableStream2 != null && animatedFileDrawableStream2.isPreview(), null);
        } else {
            File file2 = this.path;
            long j3 = this.streamFileSize;
            TLRPC$Document tLRPC$Document = this.document;
            long j4 = this.pendingSeekToUI;
            int i2 = this.currentAccount;
            AnimatedFileDrawableStream animatedFileDrawableStream3 = this.stream;
            animatedFileDrawable = new AnimatedFileDrawable(file2, false, j3, tLRPC$Document, null, null, j4, i2, animatedFileDrawableStream3 != null && animatedFileDrawableStream3.isPreview(), null);
        }
        AnimatedFileDrawable animatedFileDrawable2 = animatedFileDrawable;
        int[] iArr = animatedFileDrawable2.metaData;
        int[] iArr2 = this.metaData;
        iArr[0] = iArr2[0];
        iArr[1] = iArr2[1];
        return animatedFileDrawable2;
    }

    public static void getVideoInfo(String str, int[] iArr) {
        getVideoInfo(Build.VERSION.SDK_INT, str, iArr);
    }

    public void setStartEndTime(long j, long j2) {
        this.startTime = ((float) j) / 1000.0f;
        this.endTime = ((float) j2) / 1000.0f;
        if (getCurrentProgressMs() < j) {
            seekTo(j, true);
        }
    }

    public long getStartTime() {
        return this.startTime * 1000.0f;
    }

    public boolean isRecycled() {
        return this.isRecycled;
    }

    public Bitmap getNextFrame() {
        if (this.nativePtr == 0) {
            return this.backgroundBitmap;
        }
        if (this.backgroundBitmap == null) {
            int[] iArr = this.metaData;
            float f = this.scaleFactor;
            this.backgroundBitmap = Bitmap.createBitmap((int) (iArr[0] * f), (int) (iArr[1] * f), Bitmap.Config.ARGB_8888);
        }
        long j = this.nativePtr;
        Bitmap bitmap = this.backgroundBitmap;
        getVideoFrame(j, bitmap, this.metaData, bitmap.getRowBytes(), false, this.startTime, this.endTime);
        return this.backgroundBitmap;
    }

    public void setLimitFps(boolean z) {
        this.limitFps = z;
    }

    public ArrayList<ImageReceiver> getParents() {
        return this.parents;
    }

    @Override // org.telegram.messenger.utils.BitmapsCache.Cacheable
    public void prepareForGenerateCache() {
        this.cacheGenerateNativePtr = createDecoder(this.path.getAbsolutePath(), this.metaData, this.currentAccount, this.streamFileSize, this.stream, false);
    }

    @Override // org.telegram.messenger.utils.BitmapsCache.Cacheable
    public void releaseForGenerateCache() {
        long j = this.cacheGenerateNativePtr;
        if (j != 0) {
            destroyDecoder(j);
        }
    }

    @Override // org.telegram.messenger.utils.BitmapsCache.Cacheable
    public int getNextFrame(Bitmap bitmap) {
        if (this.cacheGenerateNativePtr == 0) {
            return -1;
        }
        Canvas canvas = new Canvas(bitmap);
        if (this.generatingCacheBitmap == null) {
            int[] iArr = this.metaData;
            this.generatingCacheBitmap = Bitmap.createBitmap(iArr[0], iArr[1], Bitmap.Config.ARGB_8888);
        }
        long j = this.cacheGenerateNativePtr;
        Bitmap bitmap2 = this.generatingCacheBitmap;
        getVideoFrame(j, bitmap2, this.metaData, bitmap2.getRowBytes(), false, this.startTime, this.endTime);
        long j2 = this.cacheGenerateTimestamp;
        if (j2 != 0) {
            int[] iArr2 = this.metaData;
            if (iArr2[3] == 0 || j2 > iArr2[3]) {
                return 0;
            }
        }
        int i = this.lastMetadata;
        int[] iArr3 = this.metaData;
        if (i == iArr3[3]) {
            int i2 = this.tryCount + 1;
            this.tryCount = i2;
            if (i2 > 5) {
                return 0;
            }
        }
        this.lastMetadata = iArr3[3];
        bitmap.eraseColor(0);
        canvas.save();
        float width = this.renderingWidth / this.generatingCacheBitmap.getWidth();
        canvas.scale(width, width);
        canvas.drawBitmap(this.generatingCacheBitmap, 0.0f, 0.0f, (Paint) null);
        canvas.restore();
        this.cacheGenerateTimestamp = this.metaData[3];
        return 1;
    }

    @Override // org.telegram.messenger.utils.BitmapsCache.Cacheable
    public Bitmap getFirstFrame(Bitmap bitmap) {
        Canvas canvas = new Canvas(bitmap);
        if (this.generatingCacheBitmap == null) {
            int[] iArr = this.metaData;
            this.generatingCacheBitmap = Bitmap.createBitmap(iArr[0], iArr[1], Bitmap.Config.ARGB_8888);
        }
        long createDecoder = createDecoder(this.path.getAbsolutePath(), this.metaData, this.currentAccount, this.streamFileSize, this.stream, false);
        if (createDecoder == 0) {
            return bitmap;
        }
        Bitmap bitmap2 = this.generatingCacheBitmap;
        getVideoFrame(createDecoder, bitmap2, this.metaData, bitmap2.getRowBytes(), false, this.startTime, this.endTime);
        destroyDecoder(createDecoder);
        bitmap.eraseColor(0);
        canvas.save();
        float width = this.renderingWidth / this.generatingCacheBitmap.getWidth();
        canvas.scale(width, width);
        canvas.drawBitmap(this.generatingCacheBitmap, 0.0f, 0.0f, (Paint) null);
        canvas.restore();
        return bitmap;
    }

    public boolean canLoadFrames() {
        return this.precache ? this.bitmapsCache != null : this.nativePtr != 0 || !this.decoderCreated;
    }

    public void updateCurrentFrame(long j, boolean z) {
        Bitmap bitmap;
        if (this.isRunning) {
            Bitmap bitmap2 = this.renderingBitmap;
            if (bitmap2 == null && this.nextRenderingBitmap == null) {
                scheduleNextGetFrame();
            } else if (this.nextRenderingBitmap == null) {
            } else {
                if (bitmap2 != null && (Math.abs(j - this.lastFrameTime) < this.invalidateAfter || this.skipFrameUpdate)) {
                    return;
                }
                if (this.precache) {
                    this.backgroundBitmap = this.renderingBitmap;
                }
                this.renderingBitmap = this.nextRenderingBitmap;
                this.renderingBitmapTime = this.nextRenderingBitmapTime;
                this.renderingShader = this.nextRenderingShader;
                this.nextRenderingBitmap = null;
                this.nextRenderingBitmapTime = 0;
                this.nextRenderingShader = null;
                this.lastFrameTime = j;
            }
        } else if (this.isRunning || !this.decodeSingleFrame || Math.abs(j - this.lastFrameTime) < this.invalidateAfter || (bitmap = this.nextRenderingBitmap) == null) {
        } else {
            if (this.precache) {
                this.backgroundBitmap = this.renderingBitmap;
            }
            this.renderingBitmap = bitmap;
            this.renderingBitmapTime = this.nextRenderingBitmapTime;
            this.renderingShader = this.nextRenderingShader;
            this.nextRenderingBitmap = null;
            this.nextRenderingBitmapTime = 0;
            this.nextRenderingShader = null;
            this.lastFrameTime = j;
        }
    }
}