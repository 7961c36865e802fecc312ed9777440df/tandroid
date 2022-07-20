package org.telegram.ui.Components;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import java.io.File;
import java.util.HashMap;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.ApplicationLoader;
import org.telegram.messenger.Bitmaps;
import org.telegram.messenger.DocumentObject;
import org.telegram.messenger.SvgHelper;
import org.telegram.ui.ActionBar.Theme;
/* loaded from: classes3.dex */
public class ThemePreviewDrawable extends BitmapDrawable {
    public ThemePreviewDrawable(File file, DocumentObject.ThemeDocument themeDocument) {
        super(createPreview(file, themeDocument));
    }

    private static Bitmap createPreview(File file, DocumentObject.ThemeDocument themeDocument) {
        boolean z;
        int i;
        MotionBackgroundDrawable motionBackgroundDrawable;
        Drawable drawable;
        boolean z2;
        Bitmap bitmap;
        Bitmap decodeFile;
        new RectF();
        Paint paint = new Paint();
        Bitmap createBitmap = Bitmaps.createBitmap(560, 678, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(createBitmap);
        HashMap<String, Integer> themeFileValues = Theme.getThemeFileValues(null, themeDocument.baseTheme.assetName, null);
        HashMap<String, Integer> hashMap = new HashMap<>(themeFileValues);
        themeDocument.accent.fillAccentColors(themeFileValues, hashMap);
        int previewColor = Theme.getPreviewColor(hashMap, "actionBarDefault");
        int previewColor2 = Theme.getPreviewColor(hashMap, "actionBarDefaultIcon");
        int previewColor3 = Theme.getPreviewColor(hashMap, "chat_messagePanelBackground");
        int previewColor4 = Theme.getPreviewColor(hashMap, "chat_messagePanelIcons");
        int previewColor5 = Theme.getPreviewColor(hashMap, "chat_inBubble");
        int previewColor6 = Theme.getPreviewColor(hashMap, "chat_outBubble");
        Integer num = hashMap.get("chat_wallpaper");
        Integer num2 = hashMap.get("chat_wallpaper_gradient_to");
        Integer num3 = hashMap.get("key_chat_wallpaper_gradient_to2");
        Integer num4 = hashMap.get("key_chat_wallpaper_gradient_to3");
        Integer num5 = hashMap.get("chat_wallpaper_gradient_rotation");
        if (num5 == null) {
            num5 = 45;
        }
        Drawable mutate = ApplicationLoader.applicationContext.getResources().getDrawable(2131166089).mutate();
        Theme.setDrawableColor(mutate, previewColor2);
        Drawable mutate2 = ApplicationLoader.applicationContext.getResources().getDrawable(2131166091).mutate();
        Theme.setDrawableColor(mutate2, previewColor2);
        Drawable mutate3 = ApplicationLoader.applicationContext.getResources().getDrawable(2131166093).mutate();
        Theme.setDrawableColor(mutate3, previewColor4);
        Drawable mutate4 = ApplicationLoader.applicationContext.getResources().getDrawable(2131166092).mutate();
        Theme.setDrawableColor(mutate4, previewColor4);
        Theme.MessageDrawable[] messageDrawableArr = new Theme.MessageDrawable[2];
        int i2 = 0;
        while (i2 < 2) {
            Drawable drawable2 = mutate2;
            boolean z3 = true;
            Paint paint2 = paint;
            int i3 = previewColor;
            if (i2 != 1) {
                z3 = false;
            }
            messageDrawableArr[i2] = new AnonymousClass1(2, z3, false, hashMap);
            Theme.setDrawableColor(messageDrawableArr[i2], i2 == 1 ? previewColor6 : previewColor5);
            i2++;
            mutate2 = drawable2;
            paint = paint2;
            previewColor = i3;
        }
        Paint paint3 = paint;
        int i4 = previewColor;
        Drawable drawable3 = mutate2;
        if (num != null) {
            if (num2 == null) {
                drawable = new ColorDrawable(num.intValue());
                i = AndroidUtilities.getPatternColor(num.intValue());
                motionBackgroundDrawable = null;
            } else {
                if (num3.intValue() != 0) {
                    motionBackgroundDrawable = new MotionBackgroundDrawable(num.intValue(), num2.intValue(), num3.intValue(), num4.intValue(), true);
                    drawable = null;
                } else {
                    drawable = BackgroundGradientDrawable.createDitheredGradientBitmapDrawable(num5.intValue(), new int[]{num.intValue(), num2.intValue()}, createBitmap.getWidth(), createBitmap.getHeight() - 120);
                    motionBackgroundDrawable = null;
                }
                i = AndroidUtilities.getPatternColor(AndroidUtilities.getAverageColor(num.intValue(), num2.intValue()));
            }
            if (drawable != null) {
                z2 = false;
                drawable.setBounds(0, 120, createBitmap.getWidth(), createBitmap.getHeight() - 120);
                drawable.draw(canvas);
            } else {
                z2 = false;
            }
            if (file != null) {
                if ("application/x-tgwallpattern".equals(themeDocument.mime_type)) {
                    decodeFile = SvgHelper.getBitmap(file, 560, 678, z2);
                } else {
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inSampleSize = 1;
                    options.inJustDecodeBounds = true;
                    BitmapFactory.decodeFile(file.getAbsolutePath(), options);
                    float f = options.outWidth;
                    float f2 = options.outHeight;
                    float min = Math.min(f / 560, f2 / 678);
                    if (min < 1.2f) {
                        min = 1.0f;
                    }
                    options.inJustDecodeBounds = false;
                    if (min > 1.0f && (f > 560 || f2 > 678)) {
                        int i5 = 1;
                        do {
                            i5 *= 2;
                        } while (i5 * 2 < min);
                        options.inSampleSize = i5;
                    } else {
                        options.inSampleSize = (int) min;
                    }
                    decodeFile = BitmapFactory.decodeFile(file.getAbsolutePath(), options);
                }
                bitmap = decodeFile;
                if (bitmap != null) {
                    if (motionBackgroundDrawable != null) {
                        motionBackgroundDrawable.setPatternBitmap((int) (themeDocument.accent.patternIntensity * 100.0f), bitmap);
                        motionBackgroundDrawable.setBounds(0, 120, createBitmap.getWidth(), createBitmap.getHeight() - 120);
                        motionBackgroundDrawable.draw(canvas);
                    } else {
                        Paint paint4 = new Paint(2);
                        if (themeDocument.accent.patternIntensity >= 0.0f) {
                            paint4.setColorFilter(new PorterDuffColorFilter(i, PorterDuff.Mode.SRC_IN));
                        }
                        paint4.setAlpha(255);
                        float max = Math.max(560 / bitmap.getWidth(), 678 / bitmap.getHeight());
                        canvas.save();
                        canvas.translate((560 - ((int) (bitmap.getWidth() * max))) / 2, (678 - ((int) (bitmap.getHeight() * max))) / 2);
                        canvas.scale(max, max);
                        canvas.drawBitmap(bitmap, 0.0f, 0.0f, paint4);
                        canvas.restore();
                    }
                }
            } else {
                bitmap = null;
            }
            if (bitmap == null && motionBackgroundDrawable != null) {
                motionBackgroundDrawable.setBounds(0, 120, createBitmap.getWidth(), createBitmap.getHeight() - 120);
                motionBackgroundDrawable.draw(canvas);
            }
            z = true;
        } else {
            z = false;
        }
        if (!z) {
            Drawable createDefaultWallpaper = Theme.createDefaultWallpaper(createBitmap.getWidth(), createBitmap.getHeight() - 120);
            createDefaultWallpaper.setBounds(0, 120, createBitmap.getWidth(), createBitmap.getHeight() - 120);
            createDefaultWallpaper.draw(canvas);
        }
        paint3.setColor(i4);
        canvas.drawRect(0.0f, 0.0f, createBitmap.getWidth(), 120.0f, paint3);
        if (mutate != null) {
            int intrinsicHeight = (120 - mutate.getIntrinsicHeight()) / 2;
            mutate.setBounds(13, intrinsicHeight, mutate.getIntrinsicWidth() + 13, mutate.getIntrinsicHeight() + intrinsicHeight);
            mutate.draw(canvas);
        }
        if (drawable3 != null) {
            int width = (createBitmap.getWidth() - drawable3.getIntrinsicWidth()) - 10;
            int intrinsicHeight2 = (120 - drawable3.getIntrinsicHeight()) / 2;
            drawable3.setBounds(width, intrinsicHeight2, drawable3.getIntrinsicWidth() + width, drawable3.getIntrinsicHeight() + intrinsicHeight2);
            drawable3.draw(canvas);
        }
        messageDrawableArr[1].setBounds(161, 216, createBitmap.getWidth() - 20, 308);
        messageDrawableArr[1].setTop(0, 560, 522, false, false);
        messageDrawableArr[1].draw(canvas);
        messageDrawableArr[1].setBounds(161, 430, createBitmap.getWidth() - 20, 522);
        messageDrawableArr[1].setTop(430, 560, 522, false, false);
        messageDrawableArr[1].draw(canvas);
        messageDrawableArr[0].setBounds(20, 323, 399, 415);
        messageDrawableArr[0].setTop(323, 560, 522, false, false);
        messageDrawableArr[0].draw(canvas);
        paint3.setColor(previewColor3);
        canvas.drawRect(0.0f, createBitmap.getHeight() - 120, createBitmap.getWidth(), createBitmap.getHeight(), paint3);
        if (mutate3 != null) {
            int height = (createBitmap.getHeight() - 120) + ((120 - mutate3.getIntrinsicHeight()) / 2);
            mutate3.setBounds(22, height, mutate3.getIntrinsicWidth() + 22, mutate3.getIntrinsicHeight() + height);
            mutate3.draw(canvas);
        }
        if (mutate4 != null) {
            int width2 = (createBitmap.getWidth() - mutate4.getIntrinsicWidth()) - 22;
            int height2 = (createBitmap.getHeight() - 120) + ((120 - mutate4.getIntrinsicHeight()) / 2);
            mutate4.setBounds(width2, height2, mutate4.getIntrinsicWidth() + width2, mutate4.getIntrinsicHeight() + height2);
            mutate4.draw(canvas);
        }
        return createBitmap;
    }

    /* renamed from: org.telegram.ui.Components.ThemePreviewDrawable$1 */
    /* loaded from: classes3.dex */
    public class AnonymousClass1 extends Theme.MessageDrawable {
        final /* synthetic */ HashMap val$colors;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass1(int i, boolean z, boolean z2, HashMap hashMap) {
            super(i, z, z2);
            this.val$colors = hashMap;
        }

        @Override // org.telegram.ui.ActionBar.Theme.MessageDrawable
        protected int getColor(String str) {
            Integer num = (Integer) this.val$colors.get(str);
            if (num == null) {
                return Theme.getColor(str);
            }
            return num.intValue();
        }

        @Override // org.telegram.ui.ActionBar.Theme.MessageDrawable
        protected Integer getCurrentColor(String str) {
            return (Integer) this.val$colors.get(str);
        }
    }
}