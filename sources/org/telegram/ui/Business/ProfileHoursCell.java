package org.telegram.ui.Business;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewPropertyAnimator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import j$.time.DayOfWeek;
import j$.time.format.TextStyle;
import java.util.ArrayList;
import java.util.Calendar;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.R;
import org.telegram.messenger.UserConfig;
import org.telegram.tgnet.TLRPC$TL_businessWeeklyOpen;
import org.telegram.tgnet.TLRPC$TL_businessWorkHours;
import org.telegram.tgnet.TLRPC$TL_timezone;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Business.OpeningHoursActivity;
import org.telegram.ui.Components.ClickableAnimatedTextView;
import org.telegram.ui.Components.CubicBezierInterpolator;
import org.telegram.ui.Components.LayoutHelper;
/* loaded from: classes.dex */
public class ProfileHoursCell extends LinearLayout {
    private ImageView arrowView;
    private boolean expanded;
    private boolean firstAfterAttach;
    private final TextView[] labelText;
    private TextView[] labelTimeText;
    private final ViewGroup[] lines;
    private boolean needDivider;
    private final Theme.ResourcesProvider resourcesProvider;
    private ClickableAnimatedTextView switchText;
    private TextView textView;
    private final TextView[][] timeText;
    private int todayLinesCount;
    private int todayLinesHeight;
    private FrameLayout todayTimeContainer;
    private FrameLayout todayTimeTextContainer;
    private LinearLayout todayTimeTextContainer2;

    public ProfileHoursCell(Context context, Theme.ResourcesProvider resourcesProvider) {
        super(context);
        this.labelTimeText = new TextView[2];
        this.lines = new ViewGroup[7];
        this.labelText = new TextView[7];
        this.timeText = new TextView[7];
        this.todayLinesCount = 1;
        this.todayLinesHeight = 0;
        this.firstAfterAttach = true;
        this.resourcesProvider = resourcesProvider;
        setOrientation(1);
        setClipChildren(false);
        int i = 0;
        for (int i2 = 7; i < i2; i2 = 7) {
            if (i == 0) {
                FrameLayout frameLayout = new FrameLayout(this, context) { // from class: org.telegram.ui.Business.ProfileHoursCell.1
                    @Override // android.widget.FrameLayout, android.view.View
                    protected void onMeasure(int i3, int i4) {
                        super.onMeasure(i3, View.MeasureSpec.makeMeasureSpec(Math.max(View.MeasureSpec.getSize(i4), AndroidUtilities.dp(60.0f)), View.MeasureSpec.getMode(i4)));
                    }
                };
                frameLayout.setMinimumHeight(AndroidUtilities.dp(60.0f));
                TextView textView = new TextView(context);
                this.textView = textView;
                textView.setGravity(LocaleController.isRTL ? 5 : 3);
                this.textView.setTextSize(1, 16.0f);
                frameLayout.addView(this.textView, LayoutHelper.createFrameRelatively(-1.0f, -2.0f, 8388659, 0.0f, 9.33f, 0.0f, 0.0f));
                this.labelText[i] = new TextView(context);
                this.labelText[i].setGravity(LocaleController.isRTL ? 5 : 3);
                this.labelText[i].setTextSize(1, 13.0f);
                this.labelText[i].setTextColor(Theme.getColor(Theme.key_windowBackgroundWhiteGrayText2, resourcesProvider));
                frameLayout.addView(this.labelText[i], LayoutHelper.createFrameRelatively(-2.0f, -2.0f, 8388659, 0.0f, 33.0f, 0.0f, 10.0f));
                LinearLayout linearLayout = new LinearLayout(context);
                this.todayTimeTextContainer2 = linearLayout;
                linearLayout.setOrientation(1);
                this.todayTimeTextContainer = new FrameLayout(context);
                this.timeText[i] = new TextView[2];
                for (int i3 = 0; i3 < 2; i3++) {
                    this.timeText[i][i3] = new TextView(context);
                    this.timeText[i][i3].setTextSize(1, 14.0f);
                    this.timeText[i][i3].setTextColor(Theme.getColor(Theme.key_windowBackgroundWhiteGrayText2, resourcesProvider));
                    this.timeText[i][i3].setGravity(LocaleController.isRTL ? 3 : 5);
                    this.todayTimeTextContainer.addView(this.timeText[i][i3], LayoutHelper.createFrameRelatively(-1.0f, -1.0f, 119, 0.0f, 0.0f, 20.0f, 0.0f));
                }
                for (int i4 = 0; i4 < 2; i4++) {
                    this.labelTimeText[i4] = new TextView(context);
                    this.labelTimeText[i4].setTextSize(1, 14.0f);
                    this.labelTimeText[i4].setTextColor(Theme.getColor(Theme.key_windowBackgroundWhiteGrayText2, resourcesProvider));
                    this.labelTimeText[i4].setGravity(LocaleController.isRTL ? 3 : 5);
                    this.todayTimeTextContainer.addView(this.labelTimeText[i4], LayoutHelper.createFrameRelatively(-1.0f, -1.0f, 119, 0.0f, 0.0f, 20.0f, 0.0f));
                }
                ImageView imageView = new ImageView(context);
                this.arrowView = imageView;
                imageView.setScaleType(ImageView.ScaleType.CENTER);
                this.arrowView.setScaleX(0.6f);
                this.arrowView.setScaleY(0.6f);
                this.arrowView.setImageResource(R.drawable.arrow_more);
                this.arrowView.setColorFilter(new PorterDuffColorFilter(Theme.getColor(Theme.key_windowBackgroundWhiteGrayText2, resourcesProvider), PorterDuff.Mode.SRC_IN));
                this.todayTimeTextContainer.addView(this.arrowView, LayoutHelper.createFrameRelatively(20.0f, 20.0f, 8388629));
                this.todayTimeTextContainer2.addView(this.todayTimeTextContainer, LayoutHelper.createLinearRelatively(-1.0f, -1.0f, 119));
                ClickableAnimatedTextView clickableAnimatedTextView = new ClickableAnimatedTextView(context);
                this.switchText = clickableAnimatedTextView;
                clickableAnimatedTextView.getDrawable().updateAll = true;
                this.switchText.setTextSize(AndroidUtilities.dp(13.0f));
                ClickableAnimatedTextView clickableAnimatedTextView2 = this.switchText;
                int i5 = Theme.key_windowBackgroundWhiteBlueText2;
                clickableAnimatedTextView2.setTextColor(Theme.getColor(i5, resourcesProvider));
                this.switchText.setPadding(AndroidUtilities.dp(6.0f), 0, AndroidUtilities.dp(6.0f), 0);
                this.switchText.setGravity(LocaleController.isRTL ? 3 : 5);
                this.switchText.setBackground(Theme.createSimpleSelectorRoundRectDrawable(AndroidUtilities.dp(8.0f), Theme.multAlpha(Theme.getColor(i5, resourcesProvider), 0.1f), Theme.multAlpha(Theme.getColor(i5, resourcesProvider), 0.22f)));
                this.switchText.getDrawable().setScaleProperty(0.6f);
                this.switchText.setVisibility(8);
                this.todayTimeTextContainer2.addView(this.switchText, LayoutHelper.createLinearRelatively(-1.0f, 17.0f, 8388613, 0.0f, 4.0f, 18.0f, 0.0f));
                FrameLayout frameLayout2 = new FrameLayout(context);
                this.todayTimeContainer = frameLayout2;
                frameLayout2.addView(this.todayTimeTextContainer2, LayoutHelper.createFrameRelatively(-1.0f, -2.0f, 8388693, 0.0f, 0.0f, 0.0f, 0.0f));
                frameLayout.addView(this.todayTimeContainer, LayoutHelper.createFrameRelatively(-1.0f, -2.0f, 8388693, 0.0f, 0.0f, 0.0f, 12.0f));
                this.lines[i] = frameLayout;
                addView(frameLayout, LayoutHelper.createFrameRelatively(-1.0f, -2.0f, 51, 22.0f, 0.0f, 13.0f, 0.0f));
            } else {
                LinearLayout linearLayout2 = new LinearLayout(context);
                linearLayout2.setOrientation(0);
                this.labelText[i] = new TextView(context);
                this.labelText[i].setTextSize(1, 14.0f);
                this.labelText[i].setTextColor(Theme.getColor(Theme.key_windowBackgroundWhiteBlackText, resourcesProvider));
                this.labelText[i].setGravity(LocaleController.isRTL ? 5 : 3);
                FrameLayout frameLayout3 = new FrameLayout(context);
                this.timeText[i] = new TextView[2];
                for (int i6 = 0; i6 < 2; i6++) {
                    this.timeText[i][i6] = new TextView(context);
                    this.timeText[i][i6].setTextSize(1, 14.0f);
                    this.timeText[i][i6].setTextColor(Theme.getColor(Theme.key_windowBackgroundWhiteGrayText2, resourcesProvider));
                    this.timeText[i][i6].setGravity(LocaleController.isRTL ? 3 : 5);
                    frameLayout3.addView(this.timeText[i][i6], LayoutHelper.createFrame(-1, -1, 119));
                }
                if (LocaleController.isRTL) {
                    linearLayout2.addView(frameLayout3, LayoutHelper.createLinear(-2, -1, 51));
                    linearLayout2.addView(this.labelText[i], LayoutHelper.createLinear(-1, -1, 53));
                } else {
                    linearLayout2.addView(this.labelText[i], LayoutHelper.createLinear(-2, -1, 51));
                    linearLayout2.addView(frameLayout3, LayoutHelper.createLinear(-1, -1, 53));
                }
                this.lines[i] = linearLayout2;
                addView(linearLayout2, LayoutHelper.createLinearRelatively(-1.0f, -2.0f, 51, 22.0f, i == 1 ? 1.0f : 11.66f, 33.0f, i == 6 ? 16.66f : 0.0f));
            }
            i++;
        }
        setWillNotDraw(false);
    }

    public void setOnTimezoneSwitchClick(View.OnClickListener onClickListener) {
        ClickableAnimatedTextView clickableAnimatedTextView = this.switchText;
        if (clickableAnimatedTextView != null) {
            clickableAnimatedTextView.setOnClickListener(onClickListener);
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:73:0x0134  */
    /* JADX WARN: Removed duplicated region for block: B:74:0x013c  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void set(TLRPC$TL_businessWorkHours tLRPC$TL_businessWorkHours, boolean z, boolean z2, boolean z3) {
        boolean z4;
        ArrayList<OpeningHoursActivity.Period>[] arrayListArr;
        boolean z5;
        int i;
        int i2;
        int i3;
        float f;
        boolean z6 = z;
        this.expanded = z6;
        this.needDivider = z3;
        if (tLRPC$TL_businessWorkHours == null) {
            return;
        }
        TLRPC$TL_timezone findTimezone = TimezonesController.getInstance(UserConfig.selectedAccount).findTimezone(tLRPC$TL_businessWorkHours.timezone_id);
        Calendar calendar = Calendar.getInstance();
        int rawOffset = ((calendar.getTimeZone().getRawOffset() / 1000) - (findTimezone == null ? 0 : findTimezone.utc_offset)) / 60;
        this.switchText.setVisibility(rawOffset != 0 ? 0 : 8);
        boolean z7 = rawOffset == 0 ? false : z2;
        invalidate();
        if (this.firstAfterAttach) {
            this.labelTimeText[0].setAlpha((z6 || z7) ? 0.0f : 1.0f);
            this.labelTimeText[1].setAlpha((z6 || !z7) ? 0.0f : 1.0f);
            this.arrowView.setRotation(z6 ? 180.0f : 0.0f);
        } else {
            ViewPropertyAnimator duration = this.labelTimeText[0].animate().alpha((z6 || z7) ? 0.0f : 1.0f).setDuration(320L);
            CubicBezierInterpolator cubicBezierInterpolator = CubicBezierInterpolator.EASE_OUT_QUINT;
            duration.setInterpolator(cubicBezierInterpolator).start();
            this.labelTimeText[1].animate().alpha((z6 || !z7) ? 0.0f : 1.0f).setDuration(320L).setInterpolator(cubicBezierInterpolator).start();
            this.timeText[0][0].animate().alpha(z6 ? 1.0f : 0.0f).setDuration(320L).setInterpolator(cubicBezierInterpolator).start();
            this.timeText[0][1].animate().alpha(z6 ? 1.0f : 0.0f).setDuration(320L).setInterpolator(cubicBezierInterpolator).start();
            this.arrowView.animate().rotation(z6 ? 180.0f : 0.0f).setDuration(320L).setInterpolator(cubicBezierInterpolator).start();
        }
        for (int i4 = 0; i4 < this.timeText.length; i4++) {
            int i5 = 0;
            while (true) {
                TextView[][] textViewArr = this.timeText;
                if (i5 < textViewArr[i4].length) {
                    if (i4 != 0 || z6) {
                        if ((i5 == 1) == z7) {
                            f = 1.0f;
                            if (!this.firstAfterAttach) {
                                textViewArr[i4][i5].setAlpha(f);
                            } else {
                                textViewArr[i4][i5].animate().alpha(f).setDuration(320L).setInterpolator(CubicBezierInterpolator.EASE_OUT_QUINT).start();
                            }
                            i5++;
                        }
                    }
                    f = 0.0f;
                    if (!this.firstAfterAttach) {
                    }
                    i5++;
                }
            }
        }
        ClickableAnimatedTextView clickableAnimatedTextView = this.switchText;
        if (clickableAnimatedTextView != null) {
            clickableAnimatedTextView.setText(LocaleController.getString(z7 ? R.string.BusinessHoursProfileSwitchMy : R.string.BusinessHoursProfileSwitchLocal), (LocaleController.isRTL || this.firstAfterAttach) ? false : true);
        }
        this.firstAfterAttach = false;
        ArrayList arrayList = new ArrayList(tLRPC$TL_businessWorkHours.weekly_open);
        ArrayList<OpeningHoursActivity.Period>[] daysHours = OpeningHoursActivity.getDaysHours(arrayList);
        int i6 = 7;
        int i7 = ((calendar.get(7) + 7) - 2) % 7;
        int i8 = calendar.get(11);
        int i9 = calendar.get(12);
        ArrayList<TLRPC$TL_businessWeeklyOpen> adaptWeeklyOpen = OpeningHoursActivity.adaptWeeklyOpen(tLRPC$TL_businessWorkHours.weekly_open, rawOffset);
        int i10 = i9 + (i8 * 60) + (i7 * 1440);
        for (int i11 = 0; i11 < adaptWeeklyOpen.size(); i11++) {
            TLRPC$TL_businessWeeklyOpen tLRPC$TL_businessWeeklyOpen = adaptWeeklyOpen.get(i11);
            int i12 = tLRPC$TL_businessWeeklyOpen.start_minute;
            if ((i10 >= i12 && i10 <= tLRPC$TL_businessWeeklyOpen.end_minute) || (((i2 = i10 + 10080) >= i12 && i2 <= tLRPC$TL_businessWeeklyOpen.end_minute) || (i10 - 10080 >= i12 && i3 <= tLRPC$TL_businessWeeklyOpen.end_minute))) {
                z4 = true;
                break;
            }
        }
        z4 = false;
        ArrayList<OpeningHoursActivity.Period>[] daysHours2 = OpeningHoursActivity.getDaysHours(adaptWeeklyOpen);
        this.textView.setText(LocaleController.getString(z4 ? R.string.BusinessHoursProfileNowOpen : R.string.BusinessHoursProfileNowClosed));
        this.textView.setTextColor(Theme.getColor(z4 ? Theme.key_avatar_nameInMessageGreen : Theme.key_text_RedRegular, this.resourcesProvider));
        int i13 = this.todayLinesHeight;
        int i14 = this.todayLinesCount;
        this.todayLinesCount = 1;
        this.todayLinesHeight = 0;
        int i15 = 0;
        for (int i16 = 2; i15 < i16; i16 = 2) {
            ArrayList<OpeningHoursActivity.Period>[] arrayListArr2 = i15 == 0 ? daysHours : daysHours2;
            int i17 = 0;
            while (i17 < i6) {
                int i18 = (i7 + i17) % 7;
                if (i17 == 0) {
                    this.labelText[i17].setText(LocaleController.getString(R.string.BusinessHoursProfile));
                    arrayListArr = daysHours2;
                } else {
                    String displayName = DayOfWeek.values()[i18].getDisplayName(TextStyle.FULL, LocaleController.getInstance().getCurrentLocale());
                    StringBuilder sb = new StringBuilder();
                    arrayListArr = daysHours2;
                    sb.append(displayName.substring(0, 1).toUpperCase());
                    sb.append(displayName.substring(1));
                    this.labelText[i17].setText(sb.toString());
                    this.timeText[i17][0].setVisibility(z6 ? 0 : 4);
                    this.timeText[i17][1].setVisibility(z6 ? 0 : 4);
                    this.labelText[i17].setVisibility(z6 ? 0 : 4);
                }
                int i19 = 0;
                while (true) {
                    if (i19 < (i17 == 0 ? 2 : 1)) {
                        TextView textView = i19 == 0 ? this.timeText[i17][i15] : this.labelTimeText[i15];
                        if (i17 == 0 && !z4 && i19 == 1) {
                            int i20 = 0;
                            while (true) {
                                if (i20 >= arrayList.size()) {
                                    i = -1;
                                    break;
                                }
                                i = ((TLRPC$TL_businessWeeklyOpen) arrayList.get(i20)).start_minute;
                                if (i10 < i) {
                                    break;
                                }
                                i20++;
                            }
                            if (i == -1 && !arrayList.isEmpty()) {
                                i = ((TLRPC$TL_businessWeeklyOpen) arrayList.get(0)).start_minute;
                            }
                            if (i == -1) {
                                textView.setText(LocaleController.getString(R.string.BusinessHoursProfileClose));
                            } else {
                                int i21 = i < i10 ? i + (10080 - i10) : i - i10;
                                if (i21 < 60) {
                                    textView.setText(LocaleController.formatPluralString("BusinessHoursProfileOpensInMinutes", i21, new Object[0]));
                                } else if (i21 < 1440) {
                                    z5 = z4;
                                    textView.setText(LocaleController.formatPluralString("BusinessHoursProfileOpensInHours", (int) Math.ceil(i21 / 60.0f), new Object[0]));
                                } else {
                                    z5 = z4;
                                    textView.setText(LocaleController.formatPluralString("BusinessHoursProfileOpensInDays", (int) Math.ceil((i21 / 60.0f) / 24.0f), new Object[0]));
                                }
                            }
                            z5 = z4;
                        } else {
                            z5 = z4;
                            if (arrayListArr2[i18].isEmpty()) {
                                textView.setText(LocaleController.getString(R.string.BusinessHoursProfileClose));
                            } else if (OpeningHoursActivity.isFull(arrayListArr2[i18])) {
                                textView.setText(LocaleController.getString(R.string.BusinessHoursProfileOpen));
                            } else {
                                StringBuilder sb2 = new StringBuilder();
                                for (int i22 = 0; i22 < arrayListArr2[i18].size(); i22++) {
                                    if (i22 > 0) {
                                        sb2.append("\n");
                                    }
                                    sb2.append(arrayListArr2[i18].get(i22));
                                }
                                int size = arrayListArr2[i18].size();
                                textView.setText(sb2);
                                if (i17 == 0) {
                                    this.todayLinesCount = Math.max(this.todayLinesCount, size);
                                    this.todayLinesHeight = Math.max(this.todayLinesHeight, textView.getLineHeight() * size);
                                }
                            }
                        }
                        i19++;
                        z4 = z5;
                    }
                }
                i17++;
                daysHours2 = arrayListArr;
                z6 = z;
                i6 = 7;
            }
            i15++;
            z6 = z;
            i6 = 7;
        }
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) this.todayTimeContainer.getLayoutParams();
        float f2 = 12.0f;
        layoutParams.topMargin = AndroidUtilities.dp((this.todayLinesCount > 2 || this.switchText.getVisibility() == 0) ? 6.0f : 12.0f);
        layoutParams.bottomMargin = AndroidUtilities.dp((this.todayLinesCount > 2 || this.switchText.getVisibility() == 0) ? 6.0f : 6.0f);
        layoutParams.gravity = ((this.todayLinesCount > 2 || this.switchText.getVisibility() == 0) ? 16 : 80) | (LocaleController.isRTL ? 3 : 5);
        if (i14 == this.todayLinesCount && i13 == this.todayLinesHeight) {
            return;
        }
        requestLayout();
    }

    @Override // android.widget.LinearLayout, android.view.View
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (this.needDivider) {
            Paint themePaint = Theme.getThemePaint("paintDivider", this.resourcesProvider);
            if (themePaint == null) {
                themePaint = Theme.dividerPaint;
            }
            canvas.drawRect(AndroidUtilities.dp(LocaleController.isRTL ? 0.0f : 21.33f), getMeasuredHeight() - 1, getWidth() - AndroidUtilities.dp(LocaleController.isRTL ? 21.33f : 0.0f), getMeasuredHeight(), themePaint);
        }
    }

    @Override // android.widget.LinearLayout, android.view.View
    protected void onMeasure(int i, int i2) {
        int dp;
        int makeMeasureSpec = View.MeasureSpec.makeMeasureSpec(View.MeasureSpec.getSize(i), 1073741824);
        if (!this.expanded) {
            int dp2 = AndroidUtilities.dp(60.0f);
            if (this.todayLinesCount > 2 || this.switchText.getVisibility() == 0) {
                dp = this.todayLinesHeight + AndroidUtilities.dp(15.0f) + AndroidUtilities.dp(this.switchText.getVisibility() == 0 ? 21.0f : 0.0f);
            } else {
                dp = 0;
            }
            i2 = View.MeasureSpec.makeMeasureSpec(Math.max(dp2, dp) + (this.needDivider ? 1 : 0), 1073741824);
        }
        super.onMeasure(makeMeasureSpec, i2);
    }

    @Override // android.view.View
    public boolean onTouchEvent(MotionEvent motionEvent) {
        ClickableAnimatedTextView clickableAnimatedTextView = this.switchText;
        if (clickableAnimatedTextView != null && clickableAnimatedTextView.getVisibility() == 0) {
            return this.switchText.getClickBounds().contains((int) ((((motionEvent.getX() - this.lines[0].getX()) - this.todayTimeContainer.getX()) - this.todayTimeTextContainer.getX()) - this.switchText.getX()), (int) ((((motionEvent.getY() - this.lines[0].getY()) - this.todayTimeContainer.getY()) - this.todayTimeTextContainer.getY()) - this.switchText.getY()));
        }
        return super.onTouchEvent(motionEvent);
    }

    @Override // android.view.ViewGroup, android.view.View
    public boolean dispatchTouchEvent(MotionEvent motionEvent) {
        return super.dispatchTouchEvent(motionEvent);
    }
}