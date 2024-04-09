package org.telegram.messenger.video;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.os.Build;
import android.text.Editable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.util.Pair;
import android.view.View;
import java.io.File;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Iterator;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.ApplicationLoader;
import org.telegram.messenger.Bitmaps;
import org.telegram.messenger.Emoji;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.LiteMode;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MediaController;
import org.telegram.messenger.UserConfig;
import org.telegram.messenger.Utilities;
import org.telegram.messenger.VideoEditedInfo;
import org.telegram.messenger.video.MediaCodecVideoConvertor;
import org.telegram.ui.Components.AnimatedEmojiSpan;
import org.telegram.ui.Components.AnimatedFileDrawable;
import org.telegram.ui.Components.Paint.PaintTypeface;
import org.telegram.ui.Components.Paint.Views.EditTextOutline;
import org.telegram.ui.Components.RLottieDrawable;
/* loaded from: classes3.dex */
public class WebmEncoder {
    private static native long createEncoder(String str, int i, int i2, int i3, long j);

    public static native void stop(long j);

    private static native boolean writeFrame(long j, ByteBuffer byteBuffer, int i, int i2);

    /* JADX WARN: Removed duplicated region for block: B:53:0x0141  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public static boolean convert(MediaCodecVideoConvertor.ConvertVideoParams convertVideoParams, int i) {
        boolean z;
        long length;
        MediaController.VideoConvertorListener videoConvertorListener;
        Bitmap bitmap;
        ByteBuffer byteBuffer;
        MediaController.VideoConvertorListener videoConvertorListener2;
        int i2 = convertVideoParams.resultWidth;
        int i3 = convertVideoParams.resultHeight;
        long createEncoder = createEncoder(convertVideoParams.cacheFile.getAbsolutePath(), i2, i3, convertVideoParams.framerate, convertVideoParams.bitrate);
        boolean z2 = true;
        if (createEncoder == 0) {
            return true;
        }
        Bitmap bitmap2 = null;
        try {
            try {
                bitmap2 = Bitmap.createBitmap(i2, i3, Bitmap.Config.ARGB_8888);
                try {
                    ByteBuffer allocateDirect = ByteBuffer.allocateDirect(bitmap2.getByteCount());
                    Canvas canvas = new Canvas(bitmap2);
                    FrameDrawer frameDrawer = new FrameDrawer(convertVideoParams);
                    double d = convertVideoParams.framerate;
                    double d2 = convertVideoParams.duration;
                    Double.isNaN(d2);
                    Double.isNaN(d);
                    int ceil = (int) Math.ceil(d * (d2 / 1000.0d));
                    int i4 = 0;
                    while (i4 < ceil) {
                        frameDrawer.draw(canvas, i4);
                        bitmap2.copyPixelsToBuffer(allocateDirect);
                        allocateDirect.flip();
                        if (!writeFrame(createEncoder, allocateDirect, i2, i3)) {
                            FileLog.d("webm writeFile error at " + i4 + "/" + ceil);
                            stop(createEncoder);
                            bitmap2.recycle();
                            return z2;
                        }
                        MediaController.VideoConvertorListener videoConvertorListener3 = convertVideoParams.callback;
                        if (videoConvertorListener3 != null) {
                            byteBuffer = allocateDirect;
                            bitmap = bitmap2;
                            try {
                                videoConvertorListener3.didWriteData(Math.min(261120L, convertVideoParams.cacheFile.length()), i4 / ceil);
                            } catch (Exception e) {
                                e = e;
                                bitmap2 = bitmap;
                                FileLog.e(e);
                                stop(createEncoder);
                                if (bitmap2 != null) {
                                    bitmap2.recycle();
                                }
                                z = true;
                                length = convertVideoParams.cacheFile.length();
                                if (i > 0) {
                                }
                                videoConvertorListener = convertVideoParams.callback;
                                if (videoConvertorListener != null) {
                                }
                                FileLog.d("webm encoded to " + convertVideoParams.cacheFile + " with size=" + length + " triesLeft=" + i);
                                return z;
                            } catch (Throwable th) {
                                th = th;
                                bitmap2 = bitmap;
                                stop(createEncoder);
                                if (bitmap2 != null) {
                                    bitmap2.recycle();
                                }
                                throw th;
                            }
                        } else {
                            bitmap = bitmap2;
                            byteBuffer = allocateDirect;
                        }
                        if (i4 % 3 == 0 && (videoConvertorListener2 = convertVideoParams.callback) != null) {
                            videoConvertorListener2.checkConversionCanceled();
                        }
                        i4++;
                        allocateDirect = byteBuffer;
                        bitmap2 = bitmap;
                        z2 = true;
                    }
                    stop(createEncoder);
                    bitmap2.recycle();
                    z = false;
                } catch (Exception e2) {
                    e = e2;
                } catch (Throwable th2) {
                    th = th2;
                }
            } catch (Exception e3) {
                e = e3;
            }
            length = convertVideoParams.cacheFile.length();
            if (i > 0 || length <= 261120) {
                videoConvertorListener = convertVideoParams.callback;
                if (videoConvertorListener != null) {
                    videoConvertorListener.didWriteData(length, 1.0f);
                }
                FileLog.d("webm encoded to " + convertVideoParams.cacheFile + " with size=" + length + " triesLeft=" + i);
                return z;
            }
            int i5 = convertVideoParams.bitrate;
            convertVideoParams.bitrate = (int) (i5 * (261120.0f / ((float) length)) * 0.9f);
            convertVideoParams.cacheFile.delete();
            FileLog.d("webm encoded too much, got " + length + ", old bitrate = " + i5 + " new bitrate = " + convertVideoParams.bitrate);
            return convert(convertVideoParams, i - 1);
        } catch (Throwable th3) {
            th = th3;
        }
    }

    /* loaded from: classes3.dex */
    public static class FrameDrawer {
        private final int H;
        private final int W;
        private final Paint bitmapPaint;
        private final Paint clearPaint;
        private final Path clipPath;
        private final int fps;
        private final ArrayList<VideoEditedInfo.MediaEntity> mediaEntities;
        Path path;
        private final Bitmap photo;
        Paint textColorPaint;
        Paint xRefPaint;

        public FrameDrawer(MediaCodecVideoConvertor.ConvertVideoParams convertVideoParams) {
            ArrayList<VideoEditedInfo.MediaEntity> arrayList = new ArrayList<>();
            this.mediaEntities = arrayList;
            this.clearPaint = new Paint(1);
            this.bitmapPaint = new Paint(5);
            int i = convertVideoParams.resultWidth;
            this.W = i;
            int i2 = convertVideoParams.resultHeight;
            this.H = i2;
            this.fps = convertVideoParams.framerate;
            Path path = new Path();
            this.clipPath = path;
            path.addRoundRect(new RectF(0.0f, 0.0f, i, i2), i * 0.125f, i2 * 0.125f, Path.Direction.CW);
            this.photo = BitmapFactory.decodeFile(convertVideoParams.videoPath);
            arrayList.addAll(convertVideoParams.mediaEntities);
            int size = arrayList.size();
            for (int i3 = 0; i3 < size; i3++) {
                VideoEditedInfo.MediaEntity mediaEntity = this.mediaEntities.get(i3);
                byte b = mediaEntity.type;
                if (b == 0 || b == 2 || b == 5) {
                    initStickerEntity(mediaEntity);
                } else if (b == 1) {
                    initTextEntity(mediaEntity);
                }
            }
            this.clearPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        }

        public void draw(Canvas canvas, int i) {
            canvas.drawPaint(this.clearPaint);
            canvas.save();
            canvas.clipPath(this.clipPath);
            Bitmap bitmap = this.photo;
            if (bitmap != null) {
                canvas.drawBitmap(bitmap, 0.0f, 0.0f, (Paint) null);
            }
            long j = i * (1000000000 / this.fps);
            int size = this.mediaEntities.size();
            for (int i2 = 0; i2 < size; i2++) {
                VideoEditedInfo.MediaEntity mediaEntity = this.mediaEntities.get(i2);
                drawEntity(canvas, mediaEntity, mediaEntity.color, j);
            }
            canvas.restore();
        }

        private void drawEntity(Canvas canvas, VideoEditedInfo.MediaEntity mediaEntity, int i, long j) {
            VideoEditedInfo.MediaEntity mediaEntity2;
            int i2;
            int i3;
            long j2 = mediaEntity.ptr;
            if (j2 != 0) {
                Bitmap bitmap = mediaEntity.bitmap;
                if (bitmap == null || (i2 = mediaEntity.W) <= 0 || (i3 = mediaEntity.H) <= 0) {
                    return;
                }
                RLottieDrawable.getFrame(j2, (int) mediaEntity.currentFrame, bitmap, i2, i3, bitmap.getRowBytes(), true);
                Bitmap bitmap2 = mediaEntity.bitmap;
                if ((mediaEntity.subType & 8) == 0) {
                    i = 0;
                }
                applyRoundRadius(mediaEntity, bitmap2, i);
                canvas.drawBitmap(mediaEntity.bitmap, mediaEntity.matrix, this.bitmapPaint);
                float f = mediaEntity.currentFrame + mediaEntity.framesPerDraw;
                mediaEntity.currentFrame = f;
                if (f >= mediaEntity.metadata[0]) {
                    mediaEntity.currentFrame = 0.0f;
                }
            } else if (mediaEntity.animatedFileDrawable != null) {
                float f2 = mediaEntity.currentFrame;
                int i4 = (int) f2;
                float f3 = f2 + mediaEntity.framesPerDraw;
                mediaEntity.currentFrame = f3;
                for (int i5 = (int) f3; i4 != i5; i5--) {
                    mediaEntity.animatedFileDrawable.getNextFrame(true);
                }
                Bitmap backgroundBitmap = mediaEntity.animatedFileDrawable.getBackgroundBitmap();
                if (backgroundBitmap != null) {
                    canvas.drawBitmap(backgroundBitmap, mediaEntity.matrix, this.bitmapPaint);
                }
            } else {
                canvas.drawBitmap(mediaEntity.bitmap, mediaEntity.matrix, this.bitmapPaint);
                ArrayList<VideoEditedInfo.EmojiEntity> arrayList = mediaEntity.entities;
                if (arrayList == null || arrayList.isEmpty()) {
                    return;
                }
                for (int i6 = 0; i6 < mediaEntity.entities.size(); i6++) {
                    VideoEditedInfo.EmojiEntity emojiEntity = mediaEntity.entities.get(i6);
                    if (emojiEntity != null && (mediaEntity2 = emojiEntity.entity) != null) {
                        drawEntity(canvas, mediaEntity2, mediaEntity.color, j);
                    }
                }
            }
        }

        private void initTextEntity(final VideoEditedInfo.MediaEntity mediaEntity) {
            int i;
            Typeface typeface;
            final EditTextOutline editTextOutline = new EditTextOutline(ApplicationLoader.applicationContext);
            editTextOutline.getPaint().setAntiAlias(true);
            editTextOutline.drawAnimatedEmojiDrawables = false;
            editTextOutline.setBackgroundColor(0);
            editTextOutline.setPadding(AndroidUtilities.dp(7.0f), AndroidUtilities.dp(7.0f), AndroidUtilities.dp(7.0f), AndroidUtilities.dp(7.0f));
            PaintTypeface paintTypeface = mediaEntity.textTypeface;
            if (paintTypeface != null && (typeface = paintTypeface.getTypeface()) != null) {
                editTextOutline.setTypeface(typeface);
            }
            editTextOutline.setTextSize(0, mediaEntity.fontSize);
            SpannableString spannableString = new SpannableString(mediaEntity.text);
            Iterator<VideoEditedInfo.EmojiEntity> it = mediaEntity.entities.iterator();
            while (it.hasNext()) {
                final VideoEditedInfo.EmojiEntity next = it.next();
                if (next.documentAbsolutePath != null) {
                    VideoEditedInfo.MediaEntity mediaEntity2 = new VideoEditedInfo.MediaEntity();
                    next.entity = mediaEntity2;
                    mediaEntity2.text = next.documentAbsolutePath;
                    mediaEntity2.subType = next.subType;
                    AnimatedEmojiSpan animatedEmojiSpan = new AnimatedEmojiSpan(0L, 1.0f, editTextOutline.getPaint().getFontMetricsInt()) { // from class: org.telegram.messenger.video.WebmEncoder.FrameDrawer.1
                        @Override // org.telegram.ui.Components.AnimatedEmojiSpan, android.text.style.ReplacementSpan
                        public void draw(Canvas canvas, CharSequence charSequence, int i2, int i3, float f, int i4, int i5, int i6, Paint paint) {
                            super.draw(canvas, charSequence, i2, i3, f, i4, i5, i6, paint);
                            VideoEditedInfo.MediaEntity mediaEntity3 = mediaEntity;
                            float paddingLeft = mediaEntity.x + ((((editTextOutline.getPaddingLeft() + f) + (this.measuredSize / 2.0f)) / mediaEntity3.viewWidth) * mediaEntity3.width);
                            float f2 = mediaEntity3.y;
                            VideoEditedInfo.MediaEntity mediaEntity4 = mediaEntity;
                            float f3 = mediaEntity4.height;
                            float paddingTop = f2 + ((((editTextOutline.getPaddingTop() + i4) + ((i6 - i4) / 2.0f)) / mediaEntity4.viewHeight) * f3);
                            if (mediaEntity4.rotation != 0.0f) {
                                float f4 = mediaEntity4.x + (mediaEntity4.width / 2.0f);
                                float f5 = mediaEntity4.y + (f3 / 2.0f);
                                float f6 = FrameDrawer.this.W / FrameDrawer.this.H;
                                double d = paddingLeft - f4;
                                double cos = Math.cos(-mediaEntity.rotation);
                                Double.isNaN(d);
                                double d2 = (paddingTop - f5) / f6;
                                double sin = Math.sin(-mediaEntity.rotation);
                                Double.isNaN(d2);
                                float f7 = f4 + ((float) ((cos * d) - (sin * d2)));
                                double sin2 = Math.sin(-mediaEntity.rotation);
                                Double.isNaN(d);
                                double d3 = d * sin2;
                                double cos2 = Math.cos(-mediaEntity.rotation);
                                Double.isNaN(d2);
                                paddingTop = (((float) (d3 + (d2 * cos2))) * f6) + f5;
                                paddingLeft = f7;
                            }
                            VideoEditedInfo.MediaEntity mediaEntity5 = next.entity;
                            int i7 = this.measuredSize;
                            VideoEditedInfo.MediaEntity mediaEntity6 = mediaEntity;
                            float f8 = (i7 / mediaEntity6.viewWidth) * mediaEntity6.width;
                            mediaEntity5.width = f8;
                            float f9 = (i7 / mediaEntity6.viewHeight) * mediaEntity6.height;
                            mediaEntity5.height = f9;
                            mediaEntity5.x = paddingLeft - (f8 / 2.0f);
                            mediaEntity5.y = paddingTop - (f9 / 2.0f);
                            mediaEntity5.rotation = mediaEntity6.rotation;
                            if (mediaEntity5.bitmap == null) {
                                FrameDrawer.this.initStickerEntity(mediaEntity5);
                            }
                        }
                    };
                    int i2 = next.offset;
                    spannableString.setSpan(animatedEmojiSpan, i2, next.length + i2, 33);
                }
            }
            editTextOutline.setText(Emoji.replaceEmoji((CharSequence) spannableString, editTextOutline.getPaint().getFontMetricsInt(), (int) (editTextOutline.getTextSize() * 0.8f), false));
            editTextOutline.setTextColor(mediaEntity.color);
            Editable text = editTextOutline.getText();
            if (text instanceof Spanned) {
                for (Emoji.EmojiSpan emojiSpan : (Emoji.EmojiSpan[]) text.getSpans(0, text.length(), Emoji.EmojiSpan.class)) {
                    emojiSpan.scale = 0.85f;
                }
            }
            int i3 = mediaEntity.textAlign;
            editTextOutline.setGravity(i3 != 1 ? i3 != 2 ? 19 : 21 : 17);
            int i4 = Build.VERSION.SDK_INT;
            if (i4 >= 17) {
                int i5 = mediaEntity.textAlign;
                if (i5 != 1) {
                    i = (i5 == 2 ? !LocaleController.isRTL : LocaleController.isRTL) ? 3 : 2;
                } else {
                    i = 4;
                }
                editTextOutline.setTextAlignment(i);
            }
            editTextOutline.setHorizontallyScrolling(false);
            editTextOutline.setImeOptions(268435456);
            editTextOutline.setFocusableInTouchMode(true);
            editTextOutline.setInputType(editTextOutline.getInputType() | LiteMode.FLAG_ANIMATED_EMOJI_KEYBOARD_NOT_PREMIUM);
            if (i4 >= 23) {
                setBreakStrategy(editTextOutline);
            }
            byte b = mediaEntity.subType;
            if (b == 0) {
                editTextOutline.setFrameColor(mediaEntity.color);
                editTextOutline.setTextColor(AndroidUtilities.computePerceivedBrightness(mediaEntity.color) < 0.721f ? -1 : -16777216);
            } else if (b == 1) {
                editTextOutline.setFrameColor(AndroidUtilities.computePerceivedBrightness(mediaEntity.color) >= 0.25f ? -1728053248 : -1711276033);
                editTextOutline.setTextColor(mediaEntity.color);
            } else if (b == 2) {
                editTextOutline.setFrameColor(AndroidUtilities.computePerceivedBrightness(mediaEntity.color) < 0.25f ? -1 : -16777216);
                editTextOutline.setTextColor(mediaEntity.color);
            } else if (b == 3) {
                editTextOutline.setFrameColor(0);
                editTextOutline.setTextColor(mediaEntity.color);
            }
            editTextOutline.measure(View.MeasureSpec.makeMeasureSpec(mediaEntity.viewWidth, 1073741824), View.MeasureSpec.makeMeasureSpec(mediaEntity.viewHeight, 1073741824));
            editTextOutline.layout(0, 0, mediaEntity.viewWidth, mediaEntity.viewHeight);
            mediaEntity.bitmap = Bitmap.createBitmap(mediaEntity.viewWidth, mediaEntity.viewHeight, Bitmap.Config.ARGB_8888);
            editTextOutline.draw(new Canvas(mediaEntity.bitmap));
            setupMatrix(mediaEntity);
        }

        public void setBreakStrategy(EditTextOutline editTextOutline) {
            editTextOutline.setBreakStrategy(0);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void initStickerEntity(VideoEditedInfo.MediaEntity mediaEntity) {
            Bitmap bitmap;
            AnimatedFileDrawable animatedFileDrawable;
            int i;
            int i2 = (int) (mediaEntity.width * this.W);
            mediaEntity.W = i2;
            int i3 = (int) (mediaEntity.height * this.H);
            mediaEntity.H = i3;
            if (i2 > 512) {
                mediaEntity.H = (int) ((i3 / i2) * 512.0f);
                mediaEntity.W = LiteMode.FLAG_CALLS_ANIMATIONS;
            }
            int i4 = mediaEntity.H;
            if (i4 > 512) {
                mediaEntity.W = (int) ((mediaEntity.W / i4) * 512.0f);
                mediaEntity.H = LiteMode.FLAG_CALLS_ANIMATIONS;
            }
            byte b = mediaEntity.subType;
            if ((b & 1) != 0) {
                int i5 = mediaEntity.W;
                if (i5 <= 0 || (i = mediaEntity.H) <= 0) {
                    return;
                }
                mediaEntity.bitmap = Bitmap.createBitmap(i5, i, Bitmap.Config.ARGB_8888);
                int[] iArr = new int[3];
                mediaEntity.metadata = iArr;
                mediaEntity.ptr = RLottieDrawable.create(mediaEntity.text, null, mediaEntity.W, mediaEntity.H, iArr, false, null, false, 0);
                mediaEntity.framesPerDraw = mediaEntity.metadata[1] / this.fps;
            } else if ((b & 4) != 0) {
                mediaEntity.looped = false;
                mediaEntity.animatedFileDrawable = new AnimatedFileDrawable(new File(mediaEntity.text), true, 0L, 0, null, null, null, 0L, UserConfig.selectedAccount, true, LiteMode.FLAG_CALLS_ANIMATIONS, LiteMode.FLAG_CALLS_ANIMATIONS, null);
                mediaEntity.framesPerDraw = animatedFileDrawable.getFps() / this.fps;
                mediaEntity.currentFrame = 1.0f;
                mediaEntity.animatedFileDrawable.getNextFrame(true);
                if (mediaEntity.type == 5) {
                    mediaEntity.firstSeek = true;
                }
            } else {
                String str = mediaEntity.text;
                if (!TextUtils.isEmpty(mediaEntity.segmentedPath) && (mediaEntity.subType & 16) != 0) {
                    str = mediaEntity.segmentedPath;
                }
                if (Build.VERSION.SDK_INT >= 19) {
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    if (mediaEntity.type == 2) {
                        options.inMutable = true;
                    }
                    mediaEntity.bitmap = BitmapFactory.decodeFile(str, options);
                } else {
                    try {
                        File file = new File(str);
                        RandomAccessFile randomAccessFile = new RandomAccessFile(file, "r");
                        MappedByteBuffer map = randomAccessFile.getChannel().map(FileChannel.MapMode.READ_ONLY, 0L, file.length());
                        BitmapFactory.Options options2 = new BitmapFactory.Options();
                        options2.inJustDecodeBounds = true;
                        Utilities.loadWebpImage(null, map, map.limit(), options2, true);
                        if (mediaEntity.type == 2) {
                            options2.inMutable = true;
                        }
                        Bitmap createBitmap = Bitmaps.createBitmap(options2.outWidth, options2.outHeight, Bitmap.Config.ARGB_8888);
                        mediaEntity.bitmap = createBitmap;
                        Utilities.loadWebpImage(createBitmap, map, map.limit(), null, true);
                        randomAccessFile.close();
                    } catch (Throwable th) {
                        FileLog.e(th);
                    }
                }
                if (mediaEntity.type == 2 && mediaEntity.bitmap != null) {
                    mediaEntity.roundRadius = AndroidUtilities.dp(12.0f) / Math.min(mediaEntity.viewWidth, mediaEntity.viewHeight);
                    Pair<Integer, Integer> imageOrientation = AndroidUtilities.getImageOrientation(mediaEntity.text);
                    double d = mediaEntity.rotation;
                    double radians = Math.toRadians(((Integer) imageOrientation.first).intValue());
                    Double.isNaN(d);
                    mediaEntity.rotation = (float) (d - radians);
                    if ((((Integer) imageOrientation.first).intValue() / 90) % 2 == 1) {
                        float f = mediaEntity.x;
                        float f2 = mediaEntity.width;
                        float f3 = f + (f2 / 2.0f);
                        float f4 = mediaEntity.y;
                        float f5 = mediaEntity.height;
                        float f6 = f4 + (f5 / 2.0f);
                        int i6 = this.W;
                        int i7 = this.H;
                        float f7 = (f2 * i6) / i7;
                        float f8 = (f5 * i7) / i6;
                        mediaEntity.width = f8;
                        mediaEntity.height = f7;
                        mediaEntity.x = f3 - (f8 / 2.0f);
                        mediaEntity.y = f6 - (f7 / 2.0f);
                    }
                    applyRoundRadius(mediaEntity, mediaEntity.bitmap, 0);
                } else {
                    if (mediaEntity.bitmap != null) {
                        float width = bitmap.getWidth() / mediaEntity.bitmap.getHeight();
                        if (width > 1.0f) {
                            float f9 = mediaEntity.height;
                            float f10 = f9 / width;
                            mediaEntity.y += (f9 - f10) / 2.0f;
                            mediaEntity.height = f10;
                        } else if (width < 1.0f) {
                            float f11 = mediaEntity.width;
                            float f12 = width * f11;
                            mediaEntity.x += (f11 - f12) / 2.0f;
                            mediaEntity.width = f12;
                        }
                    }
                }
            }
            setupMatrix(mediaEntity);
        }

        private void setupMatrix(VideoEditedInfo.MediaEntity mediaEntity) {
            AnimatedFileDrawable animatedFileDrawable;
            mediaEntity.matrix = new Matrix();
            Bitmap bitmap = mediaEntity.bitmap;
            if (bitmap == null && (animatedFileDrawable = mediaEntity.animatedFileDrawable) != null) {
                bitmap = animatedFileDrawable.getBackgroundBitmap();
            }
            if (bitmap != null) {
                mediaEntity.matrix.postScale(1.0f / bitmap.getWidth(), 1.0f / bitmap.getHeight());
            }
            if ((mediaEntity.subType & 2) != 0) {
                mediaEntity.matrix.postScale(-1.0f, 1.0f, 0.5f, 0.5f);
            }
            mediaEntity.matrix.postScale(mediaEntity.width * this.W, mediaEntity.height * this.H);
            mediaEntity.matrix.postTranslate(mediaEntity.x * this.W, mediaEntity.y * this.H);
            Matrix matrix = mediaEntity.matrix;
            double d = -mediaEntity.rotation;
            Double.isNaN(d);
            float f = mediaEntity.x;
            matrix.postRotate((float) ((d / 3.141592653589793d) * 180.0d), (mediaEntity.width + f) * this.W, (f + mediaEntity.height) * this.H);
        }

        private void applyRoundRadius(VideoEditedInfo.MediaEntity mediaEntity, Bitmap bitmap, int i) {
            if (bitmap == null || mediaEntity == null) {
                return;
            }
            if (mediaEntity.roundRadius == 0.0f && i == 0) {
                return;
            }
            if (mediaEntity.roundRadiusCanvas == null) {
                mediaEntity.roundRadiusCanvas = new Canvas(bitmap);
            }
            if (mediaEntity.roundRadius != 0.0f) {
                if (this.path == null) {
                    this.path = new Path();
                }
                if (this.xRefPaint == null) {
                    Paint paint = new Paint(1);
                    this.xRefPaint = paint;
                    paint.setColor(-16777216);
                    this.xRefPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
                }
                float min = Math.min(bitmap.getWidth(), bitmap.getHeight()) * mediaEntity.roundRadius;
                this.path.rewind();
                this.path.addRoundRect(new RectF(0.0f, 0.0f, bitmap.getWidth(), bitmap.getHeight()), min, min, Path.Direction.CCW);
                this.path.toggleInverseFillType();
                mediaEntity.roundRadiusCanvas.drawPath(this.path, this.xRefPaint);
            }
            if (i != 0) {
                if (this.textColorPaint == null) {
                    Paint paint2 = new Paint(1);
                    this.textColorPaint = paint2;
                    paint2.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
                }
                this.textColorPaint.setColor(i);
                mediaEntity.roundRadiusCanvas.drawRect(0.0f, 0.0f, bitmap.getWidth(), bitmap.getHeight(), this.textColorPaint);
            }
        }
    }
}
