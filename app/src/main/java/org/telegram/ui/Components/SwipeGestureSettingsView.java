package org.telegram.ui.Components;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.RectF;
import android.os.Build;
import android.view.View;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.FrameLayout;
import androidx.core.graphics.ColorUtils;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.SharedConfig;
import org.telegram.ui.ActionBar.Theme;
/* loaded from: classes3.dex */
public class SwipeGestureSettingsView extends FrameLayout {
    String currentColorKey;
    int currentIconIndex;
    int currentIconValue;
    int fromColor;
    boolean hasTabs;
    private NumberPicker picker;
    float progressToSwipeFolders;
    String[] strings;
    Runnable swapIconRunnable;
    Paint outlinePaint = new Paint(1);
    Paint filledPaint = new Paint(1);
    Paint linePaint = new Paint(1);
    Paint pickerDividersPaint = new Paint(1);
    RectF rect = new RectF();
    String[] backgroundKeys = new String[6];
    RLottieDrawable[] icons = new RLottieDrawable[6];
    RLottieImageView[] iconViews = new RLottieImageView[2];
    float colorProgress = 1.0f;

    public SwipeGestureSettingsView(Context context, int i) {
        super(context);
        String[] strArr = new String[6];
        this.strings = strArr;
        float f = 1.0f;
        strArr[0] = LocaleController.getString("SwipeSettingsPin", 2131628517);
        this.strings[1] = LocaleController.getString("SwipeSettingsRead", 2131628518);
        this.strings[2] = LocaleController.getString("SwipeSettingsArchive", 2131628513);
        this.strings[3] = LocaleController.getString("SwipeSettingsMute", 2131628516);
        this.strings[4] = LocaleController.getString("SwipeSettingsDelete", 2131628514);
        this.strings[5] = LocaleController.getString("SwipeSettingsFolders", 2131628515);
        String[] strArr2 = this.backgroundKeys;
        strArr2[0] = "chats_archiveBackground";
        strArr2[1] = "chats_archiveBackground";
        strArr2[2] = "chats_archiveBackground";
        strArr2[3] = "chats_archiveBackground";
        strArr2[4] = "dialogSwipeRemove";
        strArr2[5] = "chats_archivePinBackground";
        this.outlinePaint.setStyle(Paint.Style.STROKE);
        this.outlinePaint.setStrokeWidth(AndroidUtilities.dp(1.0f));
        this.linePaint.setStyle(Paint.Style.STROKE);
        this.linePaint.setStrokeCap(Paint.Cap.ROUND);
        this.linePaint.setStrokeWidth(AndroidUtilities.dp(5.0f));
        this.pickerDividersPaint.setStyle(Paint.Style.STROKE);
        this.pickerDividersPaint.setStrokeCap(Paint.Cap.ROUND);
        this.pickerDividersPaint.setStrokeWidth(AndroidUtilities.dp(2.0f));
        AnonymousClass1 anonymousClass1 = new AnonymousClass1(context, 13);
        this.picker = anonymousClass1;
        anonymousClass1.setMinValue(0);
        this.picker.setDrawDividers(false);
        boolean z = !MessagesController.getInstance(i).dialogFilters.isEmpty();
        this.hasTabs = z;
        this.picker.setMaxValue(z ? this.strings.length - 1 : this.strings.length - 2);
        this.picker.setFormatter(new SwipeGestureSettingsView$$ExternalSyntheticLambda1(this));
        this.picker.setOnValueChangedListener(new SwipeGestureSettingsView$$ExternalSyntheticLambda2(this));
        this.picker.setImportantForAccessibility(2);
        this.picker.setValue(SharedConfig.getChatSwipeAction(i));
        addView(this.picker, LayoutHelper.createFrame(132, -1.0f, 5, 21.0f, 0.0f, 21.0f, 0.0f));
        setWillNotDraw(false);
        this.currentIconIndex = 0;
        for (int i2 = 0; i2 < 2; i2++) {
            this.iconViews[i2] = new RLottieImageView(context);
            addView(this.iconViews[i2], LayoutHelper.createFrame(28, 28.0f, 21, 0.0f, 0.0f, 184.0f, 0.0f));
        }
        RLottieDrawable icon = getIcon(this.picker.getValue());
        if (icon != null) {
            this.iconViews[0].setImageDrawable(icon);
            icon.setCurrentFrame(icon.getFramesCount() - 1);
        }
        AndroidUtilities.updateViewVisibilityAnimated(this.iconViews[0], true, 0.5f, false);
        AndroidUtilities.updateViewVisibilityAnimated(this.iconViews[1], false, 0.5f, false);
        this.progressToSwipeFolders = this.picker.getValue() != 5 ? 0.0f : f;
        this.currentIconValue = this.picker.getValue();
    }

    /* renamed from: org.telegram.ui.Components.SwipeGestureSettingsView$1 */
    /* loaded from: classes3.dex */
    public class AnonymousClass1 extends NumberPicker {
        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass1(Context context, int i) {
            super(context, i);
            SwipeGestureSettingsView.this = r1;
        }

        @Override // org.telegram.ui.Components.NumberPicker, android.widget.LinearLayout, android.view.View
        public void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            float dp = AndroidUtilities.dp(31.0f);
            SwipeGestureSettingsView.this.pickerDividersPaint.setColor(Theme.getColor("radioBackgroundChecked"));
            canvas.drawLine(AndroidUtilities.dp(2.0f), dp, getMeasuredWidth() - AndroidUtilities.dp(2.0f), dp, SwipeGestureSettingsView.this.pickerDividersPaint);
            float measuredHeight = getMeasuredHeight() - AndroidUtilities.dp(31.0f);
            canvas.drawLine(AndroidUtilities.dp(2.0f), measuredHeight, getMeasuredWidth() - AndroidUtilities.dp(2.0f), measuredHeight, SwipeGestureSettingsView.this.pickerDividersPaint);
        }
    }

    public /* synthetic */ String lambda$new$0(int i) {
        return this.strings[i];
    }

    public /* synthetic */ void lambda$new$1(NumberPicker numberPicker, int i, int i2) {
        swapIcons();
        SharedConfig.updateChatListSwipeSetting(i2);
        invalidate();
        numberPicker.performHapticFeedback(3, 2);
    }

    private void swapIcons() {
        int value;
        if (this.swapIconRunnable == null && this.currentIconValue != (value = this.picker.getValue())) {
            this.currentIconValue = value;
            int i = (this.currentIconIndex + 1) % 2;
            RLottieDrawable icon = getIcon(value);
            if (icon != null) {
                if (this.iconViews[i].getVisibility() != 0) {
                    icon.setCurrentFrame(0, false);
                }
                this.iconViews[i].setAnimation(icon);
                this.iconViews[i].playAnimation();
            } else {
                this.iconViews[i].clearAnimationDrawable();
            }
            AndroidUtilities.updateViewVisibilityAnimated(this.iconViews[this.currentIconIndex], false, 0.5f, true);
            AndroidUtilities.updateViewVisibilityAnimated(this.iconViews[i], true, 0.5f, true);
            this.currentIconIndex = i;
            SwipeGestureSettingsView$$ExternalSyntheticLambda0 swipeGestureSettingsView$$ExternalSyntheticLambda0 = new SwipeGestureSettingsView$$ExternalSyntheticLambda0(this);
            this.swapIconRunnable = swipeGestureSettingsView$$ExternalSyntheticLambda0;
            AndroidUtilities.runOnUIThread(swipeGestureSettingsView$$ExternalSyntheticLambda0, 150L);
        }
    }

    public /* synthetic */ void lambda$swapIcons$2() {
        this.swapIconRunnable = null;
        swapIcons();
    }

    @Override // android.widget.FrameLayout, android.view.View
    protected void onMeasure(int i, int i2) {
        super.onMeasure(View.MeasureSpec.makeMeasureSpec(View.MeasureSpec.getSize(i), 1073741824), View.MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(102.0f), 1073741824));
    }

    /* JADX WARN: Removed duplicated region for block: B:23:0x00b6  */
    /* JADX WARN: Removed duplicated region for block: B:24:0x00d5  */
    /* JADX WARN: Removed duplicated region for block: B:29:0x0113  */
    @Override // android.view.View
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    protected void onDraw(Canvas canvas) {
        int measuredHeight;
        float f;
        super.onDraw(canvas);
        boolean z = this.picker.getValue() == 5;
        if (z) {
            float f2 = this.progressToSwipeFolders;
            if (f2 != 1.0f) {
                float f3 = f2 + 0.053333335f;
                this.progressToSwipeFolders = f3;
                if (f3 > 1.0f) {
                    this.progressToSwipeFolders = 1.0f;
                } else {
                    this.iconViews[0].invalidate();
                    this.iconViews[1].invalidate();
                    invalidate();
                }
                this.outlinePaint.setColor(Theme.getColor("switchTrack"));
                this.linePaint.setColor(Theme.getColor("switchTrack"));
                int measuredWidth = getMeasuredWidth() - ((AndroidUtilities.dp(132.0f) + AndroidUtilities.dp(21.0f)) + AndroidUtilities.dp(16.0f));
                int dp = AndroidUtilities.dp(21.0f);
                float f4 = dp;
                float measuredHeight2 = (getMeasuredHeight() - AndroidUtilities.dp(48.0f)) / 2;
                this.rect.set(f4, measuredHeight2, measuredWidth, getMeasuredHeight() - measuredHeight);
                if (this.currentColorKey != null) {
                    this.currentColorKey = this.backgroundKeys[this.picker.getValue()];
                    this.colorProgress = 1.0f;
                    this.fromColor = ColorUtils.blendARGB(Theme.getColor("windowBackgroundWhite"), Theme.getColor(this.currentColorKey), 0.9f);
                } else if (!this.backgroundKeys[this.picker.getValue()].equals(this.currentColorKey)) {
                    this.fromColor = ColorUtils.blendARGB(this.fromColor, ColorUtils.blendARGB(Theme.getColor("windowBackgroundWhite"), Theme.getColor(this.currentColorKey), 0.9f), this.colorProgress);
                    this.colorProgress = 0.0f;
                    this.currentColorKey = this.backgroundKeys[this.picker.getValue()];
                }
                f = this.colorProgress;
                if (f != 1.0f) {
                    float f5 = f + 0.16f;
                    this.colorProgress = f5;
                    if (f5 > 1.0f) {
                        this.colorProgress = 1.0f;
                    } else {
                        invalidate();
                    }
                }
                this.filledPaint.setColor(ColorUtils.blendARGB(this.fromColor, ColorUtils.blendARGB(Theme.getColor("windowBackgroundWhite"), Theme.getColor(this.currentColorKey), 0.9f), this.colorProgress));
                canvas.drawRoundRect(this.rect, AndroidUtilities.dp(6.0f), AndroidUtilities.dp(6.0f), this.filledPaint);
                this.filledPaint.setColor(Theme.getColor("windowBackgroundWhite"));
                this.filledPaint.setAlpha(255);
                this.rect.set(f4, measuredHeight2, measuredWidth - AndroidUtilities.dp(58.0f), getMeasuredHeight() - measuredHeight);
                this.rect.inset(-AndroidUtilities.dp(1.0f), -AndroidUtilities.dp(1.0f));
                canvas.drawRoundRect(this.rect, AndroidUtilities.dp(6.0f), AndroidUtilities.dp(6.0f), this.filledPaint);
                this.outlinePaint.setAlpha(31);
                canvas.drawRoundRect(this.rect, AndroidUtilities.dp(6.0f), AndroidUtilities.dp(6.0f), this.outlinePaint);
                canvas.save();
                canvas.clipRect(this.rect);
                this.filledPaint.setColor(Theme.getColor("switchTrack"));
                this.filledPaint.setAlpha(60);
                RectF rectF = this.rect;
                canvas.drawCircle(rectF.left + 0.0f, rectF.centerY(), AndroidUtilities.dp(15.0f), this.filledPaint);
                float centerY = this.rect.centerY() - AndroidUtilities.dp(6.0f);
                this.linePaint.setAlpha(57);
                canvas.drawLine(this.rect.left + AndroidUtilities.dp(23.0f) + 0.0f, centerY, this.rect.right - AndroidUtilities.dp(68.0f), centerY, this.linePaint);
                float centerY2 = this.rect.centerY() + AndroidUtilities.dp(6.0f);
                canvas.drawLine(this.rect.left + AndroidUtilities.dp(23.0f) + 0.0f, centerY2, this.rect.right - AndroidUtilities.dp(23.0f), centerY2, this.linePaint);
                canvas.restore();
            }
        }
        if (!z) {
            float f6 = this.progressToSwipeFolders;
            if (f6 != 0.0f) {
                float f7 = f6 - 0.053333335f;
                this.progressToSwipeFolders = f7;
                if (f7 < 0.0f) {
                    this.progressToSwipeFolders = 0.0f;
                } else {
                    this.iconViews[0].invalidate();
                    this.iconViews[1].invalidate();
                    invalidate();
                }
            }
        }
        this.outlinePaint.setColor(Theme.getColor("switchTrack"));
        this.linePaint.setColor(Theme.getColor("switchTrack"));
        int measuredWidth2 = getMeasuredWidth() - ((AndroidUtilities.dp(132.0f) + AndroidUtilities.dp(21.0f)) + AndroidUtilities.dp(16.0f));
        int dp2 = AndroidUtilities.dp(21.0f);
        float f42 = dp2;
        float measuredHeight22 = (getMeasuredHeight() - AndroidUtilities.dp(48.0f)) / 2;
        this.rect.set(f42, measuredHeight22, measuredWidth2, getMeasuredHeight() - measuredHeight);
        if (this.currentColorKey != null) {
        }
        f = this.colorProgress;
        if (f != 1.0f) {
        }
        this.filledPaint.setColor(ColorUtils.blendARGB(this.fromColor, ColorUtils.blendARGB(Theme.getColor("windowBackgroundWhite"), Theme.getColor(this.currentColorKey), 0.9f), this.colorProgress));
        canvas.drawRoundRect(this.rect, AndroidUtilities.dp(6.0f), AndroidUtilities.dp(6.0f), this.filledPaint);
        this.filledPaint.setColor(Theme.getColor("windowBackgroundWhite"));
        this.filledPaint.setAlpha(255);
        this.rect.set(f42, measuredHeight22, measuredWidth2 - AndroidUtilities.dp(58.0f), getMeasuredHeight() - measuredHeight);
        this.rect.inset(-AndroidUtilities.dp(1.0f), -AndroidUtilities.dp(1.0f));
        canvas.drawRoundRect(this.rect, AndroidUtilities.dp(6.0f), AndroidUtilities.dp(6.0f), this.filledPaint);
        this.outlinePaint.setAlpha(31);
        canvas.drawRoundRect(this.rect, AndroidUtilities.dp(6.0f), AndroidUtilities.dp(6.0f), this.outlinePaint);
        canvas.save();
        canvas.clipRect(this.rect);
        this.filledPaint.setColor(Theme.getColor("switchTrack"));
        this.filledPaint.setAlpha(60);
        RectF rectF2 = this.rect;
        canvas.drawCircle(rectF2.left + 0.0f, rectF2.centerY(), AndroidUtilities.dp(15.0f), this.filledPaint);
        float centerY3 = this.rect.centerY() - AndroidUtilities.dp(6.0f);
        this.linePaint.setAlpha(57);
        canvas.drawLine(this.rect.left + AndroidUtilities.dp(23.0f) + 0.0f, centerY3, this.rect.right - AndroidUtilities.dp(68.0f), centerY3, this.linePaint);
        float centerY22 = this.rect.centerY() + AndroidUtilities.dp(6.0f);
        canvas.drawLine(this.rect.left + AndroidUtilities.dp(23.0f) + 0.0f, centerY22, this.rect.right - AndroidUtilities.dp(23.0f), centerY22, this.linePaint);
        canvas.restore();
    }

    public RLottieDrawable getIcon(int i) {
        RLottieDrawable[] rLottieDrawableArr = this.icons;
        if (rLottieDrawableArr[i] == null) {
            int i2 = i != 1 ? i != 2 ? i != 3 ? i != 4 ? i != 5 ? 2131558545 : 2131558543 : 2131558542 : 2131558544 : 2131558420 : 2131558546;
            rLottieDrawableArr[i] = new RLottieDrawable(i2, "" + i2, AndroidUtilities.dp(28.0f), AndroidUtilities.dp(28.0f), true, null);
            updateIconColor(i);
        }
        return this.icons[i];
    }

    public void updateIconColor(int i) {
        if (this.icons[i] != null) {
            int blendARGB = ColorUtils.blendARGB(Theme.getColor("windowBackgroundWhite"), Theme.getColor("chats_archiveBackground"), 0.9f);
            int color = Theme.getColor("chats_archiveIcon");
            if (i == 2) {
                this.icons[i].setLayerColor("Arrow.**", blendARGB);
                this.icons[i].setLayerColor("Box2.**", color);
                this.icons[i].setLayerColor("Box1.**", color);
                return;
            }
            this.icons[i].setColorFilter(new PorterDuffColorFilter(color, PorterDuff.Mode.MULTIPLY));
        }
    }

    public void updateColors() {
        for (int i = 0; i < this.icons.length; i++) {
            updateIconColor(i);
        }
    }

    @Override // android.view.View
    public void setBackgroundColor(int i) {
        super.setBackgroundColor(i);
        updateColors();
        this.picker.setTextColor(Theme.getColor("dialogTextBlack"));
        this.picker.invalidate();
    }

    @Override // android.view.View
    public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo accessibilityNodeInfo) {
        super.onInitializeAccessibilityNodeInfo(accessibilityNodeInfo);
        accessibilityNodeInfo.setEnabled(true);
        accessibilityNodeInfo.setContentDescription(this.strings[this.picker.getValue()]);
        if (Build.VERSION.SDK_INT >= 21) {
            accessibilityNodeInfo.addAction(new AccessibilityNodeInfo.AccessibilityAction(16, null));
        }
    }

    @Override // android.view.View
    public void onInitializeAccessibilityEvent(AccessibilityEvent accessibilityEvent) {
        super.onInitializeAccessibilityEvent(accessibilityEvent);
        if (accessibilityEvent.getEventType() == 1) {
            int value = this.picker.getValue() + 1;
            if (value > this.picker.getMaxValue() || value < 0) {
                value = 0;
            }
            setContentDescription(this.strings[value]);
            this.picker.changeValueByOne(true);
        }
    }
}