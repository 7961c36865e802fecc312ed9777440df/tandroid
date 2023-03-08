package org.telegram.ui.Components;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Parcelable;
import android.view.View;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityEvent;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.core.graphics.ColorUtils;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.ChatObject;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.R;
import org.telegram.messenger.SvgHelper;
import org.telegram.tgnet.TLRPC$Chat;
import org.telegram.ui.ActionBar.BottomSheet;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Components.GroupCallRecordAlert;
/* loaded from: classes3.dex */
public class GroupCallRecordAlert extends BottomSheet {
    private int currentPage;
    private float pageOffset;
    private TextView positiveButton;
    private TextView[] titles;
    private LinearLayout titlesLayout;
    private ViewPager viewPager;

    public void onStartRecord(int i) {
        throw null;
    }

    public GroupCallRecordAlert(Context context, TLRPC$Chat tLRPC$Chat, boolean z) {
        super(context, false);
        int color = Theme.getColor("voipgroup_inviteMembersBackground");
        this.shadowDrawable.setColorFilter(new PorterDuffColorFilter(color, PorterDuff.Mode.MULTIPLY));
        FrameLayout frameLayout = new FrameLayout(context) { // from class: org.telegram.ui.Components.GroupCallRecordAlert.1
            boolean ignoreLayout;

            @Override // android.widget.FrameLayout, android.view.View
            protected void onMeasure(int i, int i2) {
                boolean z2 = View.MeasureSpec.getSize(i) > View.MeasureSpec.getSize(i2);
                ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) GroupCallRecordAlert.this.positiveButton.getLayoutParams();
                if (z2) {
                    int dp = AndroidUtilities.dp(80.0f);
                    marginLayoutParams.leftMargin = dp;
                    marginLayoutParams.rightMargin = dp;
                } else {
                    int dp2 = AndroidUtilities.dp(16.0f);
                    marginLayoutParams.leftMargin = dp2;
                    marginLayoutParams.rightMargin = dp2;
                }
                int size = (View.MeasureSpec.getSize(i) - AndroidUtilities.dp(200.0f)) / 2;
                GroupCallRecordAlert.this.viewPager.setPadding(size, 0, size, 0);
                super.onMeasure(i, View.MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(370.0f), 1073741824));
                measureChildWithMargins(GroupCallRecordAlert.this.titlesLayout, View.MeasureSpec.makeMeasureSpec(0, 0), 0, View.MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(64.0f), 1073741824), 0);
            }

            @Override // android.widget.FrameLayout, android.view.ViewGroup, android.view.View
            protected void onLayout(boolean z2, int i, int i2, int i3, int i4) {
                super.onLayout(z2, i, i2, i3, i4);
                GroupCallRecordAlert.this.updateTitlesLayout();
            }

            @Override // android.view.View, android.view.ViewParent
            public void requestLayout() {
                if (this.ignoreLayout) {
                    return;
                }
                super.requestLayout();
            }
        };
        this.containerView = frameLayout;
        frameLayout.setWillNotDraw(false);
        this.containerView.setClipChildren(false);
        this.containerView.setBackgroundDrawable(this.shadowDrawable);
        ViewGroup viewGroup = this.containerView;
        int i = this.backgroundPaddingLeft;
        viewGroup.setPadding(i, 0, i, 0);
        TextView textView = new TextView(getContext());
        if (ChatObject.isChannelOrGiga(tLRPC$Chat)) {
            textView.setText(LocaleController.getString("VoipChannelRecordVoiceChat", R.string.VoipChannelRecordVoiceChat));
        } else {
            textView.setText(LocaleController.getString("VoipRecordVoiceChat", R.string.VoipRecordVoiceChat));
        }
        textView.setTextColor(-1);
        textView.setTextSize(1, 20.0f);
        textView.setTypeface(AndroidUtilities.getTypeface(AndroidUtilities.TYPEFACE_ROBOTO_MEDIUM));
        textView.setGravity((LocaleController.isRTL ? 5 : 3) | 48);
        this.containerView.addView(textView, LayoutHelper.createFrame(-2, -2.0f, (LocaleController.isRTL ? 5 : 3) | 48, 24.0f, 29.0f, 24.0f, 0.0f));
        TextView textView2 = new TextView(getContext());
        textView2.setText(LocaleController.getString("VoipRecordVoiceChatInfo", R.string.VoipRecordVoiceChatInfo));
        textView2.setTextColor(-1);
        textView2.setTextSize(1, 14.0f);
        textView2.setGravity((LocaleController.isRTL ? 5 : 3) | 48);
        this.containerView.addView(textView2, LayoutHelper.createFrame(-2, -2.0f, (LocaleController.isRTL ? 5 : 3) | 48, 24.0f, 62.0f, 24.0f, 0.0f));
        this.titles = new TextView[3];
        ViewPager viewPager = new ViewPager(context);
        this.viewPager = viewPager;
        viewPager.setClipChildren(false);
        this.viewPager.setOffscreenPageLimit(4);
        this.viewPager.setClipToPadding(false);
        AndroidUtilities.setViewPagerEdgeEffectColor(this.viewPager, 2130706432);
        this.viewPager.setAdapter(new Adapter());
        this.viewPager.setPageMargin(0);
        this.containerView.addView(this.viewPager, LayoutHelper.createFrame(-1, -1.0f, 1, 0.0f, 100.0f, 0.0f, 130.0f));
        this.viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() { // from class: org.telegram.ui.Components.GroupCallRecordAlert.2
            @Override // androidx.viewpager.widget.ViewPager.OnPageChangeListener
            public void onPageScrollStateChanged(int i2) {
            }

            @Override // androidx.viewpager.widget.ViewPager.OnPageChangeListener
            public void onPageSelected(int i2) {
            }

            @Override // androidx.viewpager.widget.ViewPager.OnPageChangeListener
            public void onPageScrolled(int i2, float f, int i3) {
                GroupCallRecordAlert.this.currentPage = i2;
                GroupCallRecordAlert.this.pageOffset = f;
                GroupCallRecordAlert.this.updateTitlesLayout();
            }
        });
        View view = new View(getContext());
        view.setBackground(new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, new int[]{color, 0}));
        this.containerView.addView(view, LayoutHelper.createFrame(120, -1.0f, 51, 0.0f, 100.0f, 0.0f, 130.0f));
        View view2 = new View(getContext());
        view2.setBackground(new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, new int[]{0, color}));
        this.containerView.addView(view2, LayoutHelper.createFrame(120, -1.0f, 53, 0.0f, 100.0f, 0.0f, 130.0f));
        TextView textView3 = new TextView(getContext()) { // from class: org.telegram.ui.Components.GroupCallRecordAlert.3
            private Paint[] gradientPaint;

            {
                this.gradientPaint = new Paint[GroupCallRecordAlert.this.titles.length];
                int i2 = 0;
                while (true) {
                    Paint[] paintArr = this.gradientPaint;
                    if (i2 >= paintArr.length) {
                        return;
                    }
                    paintArr[i2] = new Paint(1);
                    i2++;
                }
            }

            /* JADX WARN: Removed duplicated region for block: B:14:0x002f  */
            /* JADX WARN: Removed duplicated region for block: B:15:0x004b  */
            @Override // android.view.View
            /*
                Code decompiled incorrectly, please refer to instructions dump.
            */
            protected void onSizeChanged(int i2, int i3, int i4, int i5) {
                int i6;
                int i7;
                LinearGradient linearGradient;
                super.onSizeChanged(i2, i3, i4, i5);
                for (int i8 = 0; i8 < this.gradientPaint.length; i8++) {
                    int i9 = -9015575;
                    if (i8 == 0) {
                        i9 = -11033346;
                        i6 = -9015575;
                    } else if (i8 == 1) {
                        i9 = -8919716;
                        i6 = -11089922;
                    } else {
                        i6 = -1026983;
                        i7 = -1792170;
                        if (i7 == 0) {
                            linearGradient = new LinearGradient(0.0f, 0.0f, getMeasuredWidth(), 0.0f, new int[]{i9, i6, i7}, (float[]) null, Shader.TileMode.CLAMP);
                        } else {
                            linearGradient = new LinearGradient(0.0f, 0.0f, getMeasuredWidth(), 0.0f, new int[]{i9, i6}, (float[]) null, Shader.TileMode.CLAMP);
                        }
                        this.gradientPaint[i8].setShader(linearGradient);
                    }
                    i7 = 0;
                    if (i7 == 0) {
                    }
                    this.gradientPaint[i8].setShader(linearGradient);
                }
            }

            @Override // android.widget.TextView, android.view.View
            protected void onDraw(Canvas canvas) {
                RectF rectF = AndroidUtilities.rectTmp;
                rectF.set(0.0f, 0.0f, getMeasuredWidth(), getMeasuredHeight());
                this.gradientPaint[GroupCallRecordAlert.this.currentPage].setAlpha(255);
                canvas.drawRoundRect(rectF, AndroidUtilities.dp(6.0f), AndroidUtilities.dp(6.0f), this.gradientPaint[GroupCallRecordAlert.this.currentPage]);
                if (GroupCallRecordAlert.this.pageOffset > 0.0f) {
                    int i2 = GroupCallRecordAlert.this.currentPage + 1;
                    Paint[] paintArr = this.gradientPaint;
                    if (i2 < paintArr.length) {
                        paintArr[GroupCallRecordAlert.this.currentPage + 1].setAlpha((int) (GroupCallRecordAlert.this.pageOffset * 255.0f));
                        canvas.drawRoundRect(rectF, AndroidUtilities.dp(6.0f), AndroidUtilities.dp(6.0f), this.gradientPaint[GroupCallRecordAlert.this.currentPage + 1]);
                    }
                }
                super.onDraw(canvas);
            }
        };
        this.positiveButton = textView3;
        textView3.setMinWidth(AndroidUtilities.dp(64.0f));
        this.positiveButton.setTag(-1);
        this.positiveButton.setTextSize(1, 14.0f);
        this.positiveButton.setTextColor(Theme.getColor("voipgroup_nameText"));
        this.positiveButton.setGravity(17);
        this.positiveButton.setTypeface(AndroidUtilities.getTypeface(AndroidUtilities.TYPEFACE_ROBOTO_MEDIUM));
        this.positiveButton.setText(LocaleController.getString("VoipRecordStart", R.string.VoipRecordStart));
        if (Build.VERSION.SDK_INT >= 23) {
            this.positiveButton.setForeground(Theme.createSimpleSelectorRoundRectDrawable(AndroidUtilities.dp(6.0f), 0, ColorUtils.setAlphaComponent(Theme.getColor("voipgroup_nameText"), 76)));
        }
        this.positiveButton.setPadding(0, AndroidUtilities.dp(12.0f), 0, AndroidUtilities.dp(12.0f));
        this.positiveButton.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Components.GroupCallRecordAlert$$ExternalSyntheticLambda0
            @Override // android.view.View.OnClickListener
            public final void onClick(View view3) {
                GroupCallRecordAlert.this.lambda$new$0(view3);
            }
        });
        this.containerView.addView(this.positiveButton, LayoutHelper.createFrame(-1, 48.0f, 80, 0.0f, 0.0f, 0.0f, 64.0f));
        LinearLayout linearLayout = new LinearLayout(context);
        this.titlesLayout = linearLayout;
        this.containerView.addView(linearLayout, LayoutHelper.createFrame(-2, 64, 80));
        final int i2 = 0;
        while (true) {
            TextView[] textViewArr = this.titles;
            if (i2 >= textViewArr.length) {
                break;
            }
            textViewArr[i2] = new TextView(context);
            this.titles[i2].setTextSize(1, 12.0f);
            this.titles[i2].setTextColor(-1);
            this.titles[i2].setTypeface(AndroidUtilities.getTypeface(AndroidUtilities.TYPEFACE_ROBOTO_MEDIUM));
            this.titles[i2].setPadding(AndroidUtilities.dp(10.0f), 0, AndroidUtilities.dp(10.0f), 0);
            this.titles[i2].setGravity(16);
            this.titles[i2].setSingleLine(true);
            this.titlesLayout.addView(this.titles[i2], LayoutHelper.createLinear(-2, -1));
            if (i2 == 0) {
                this.titles[i2].setText(LocaleController.getString("VoipRecordAudio", R.string.VoipRecordAudio));
            } else if (i2 == 1) {
                this.titles[i2].setText(LocaleController.getString("VoipRecordPortrait", R.string.VoipRecordPortrait));
            } else {
                this.titles[i2].setText(LocaleController.getString("VoipRecordLandscape", R.string.VoipRecordLandscape));
            }
            this.titles[i2].setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Components.GroupCallRecordAlert$$ExternalSyntheticLambda1
                @Override // android.view.View.OnClickListener
                public final void onClick(View view3) {
                    GroupCallRecordAlert.this.lambda$new$1(i2, view3);
                }
            });
            i2++;
        }
        if (z) {
            this.viewPager.setCurrentItem(1);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0(View view) {
        onStartRecord(this.currentPage);
        dismiss();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$1(int i, View view) {
        this.viewPager.setCurrentItem(i, true);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateTitlesLayout() {
        TextView[] textViewArr = this.titles;
        int i = this.currentPage;
        TextView textView = textViewArr[i];
        TextView textView2 = i < textViewArr.length + (-1) ? textViewArr[i + 1] : null;
        int measuredWidth = this.containerView.getMeasuredWidth() / 2;
        float left = textView.getLeft() + (textView.getMeasuredWidth() / 2);
        float measuredWidth2 = (this.containerView.getMeasuredWidth() / 2) - left;
        if (textView2 != null) {
            measuredWidth2 -= ((textView2.getLeft() + (textView2.getMeasuredWidth() / 2)) - left) * this.pageOffset;
        }
        int i2 = 0;
        while (true) {
            TextView[] textViewArr2 = this.titles;
            if (i2 < textViewArr2.length) {
                int i3 = this.currentPage;
                float f = 0.9f;
                float f2 = 0.7f;
                if (i2 >= i3 && i2 <= i3 + 1) {
                    if (i2 == i3) {
                        float f3 = this.pageOffset;
                        f2 = 1.0f - (0.3f * f3);
                        f = 1.0f - (f3 * 0.1f);
                    } else {
                        float f4 = this.pageOffset;
                        f2 = 0.7f + (0.3f * f4);
                        f = 0.9f + (f4 * 0.1f);
                    }
                }
                textViewArr2[i2].setAlpha(f2);
                this.titles[i2].setScaleX(f);
                this.titles[i2].setScaleY(f);
                i2++;
            } else {
                this.titlesLayout.setTranslationX(measuredWidth2);
                this.positiveButton.invalidate();
                return;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes3.dex */
    public class Adapter extends PagerAdapter {
        @Override // androidx.viewpager.widget.PagerAdapter
        public void restoreState(Parcelable parcelable, ClassLoader classLoader) {
        }

        @Override // androidx.viewpager.widget.PagerAdapter
        public Parcelable saveState() {
            return null;
        }

        private Adapter() {
        }

        @Override // androidx.viewpager.widget.PagerAdapter
        public int getCount() {
            return GroupCallRecordAlert.this.titles.length;
        }

        @Override // androidx.viewpager.widget.PagerAdapter
        public Object instantiateItem(ViewGroup viewGroup, final int i) {
            int i2;
            ImageView imageView = new ImageView(GroupCallRecordAlert.this.getContext()) { // from class: org.telegram.ui.Components.GroupCallRecordAlert.Adapter.1
                @Override // android.view.View
                public void onInitializeAccessibilityEvent(AccessibilityEvent accessibilityEvent) {
                    super.onInitializeAccessibilityEvent(accessibilityEvent);
                    if (accessibilityEvent.getEventType() == 32768) {
                        GroupCallRecordAlert.this.viewPager.setCurrentItem(i, true);
                    }
                }
            };
            imageView.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Components.GroupCallRecordAlert$Adapter$$ExternalSyntheticLambda0
                @Override // android.view.View.OnClickListener
                public final void onClick(View view) {
                    GroupCallRecordAlert.Adapter.this.lambda$instantiateItem$0(i, view);
                }
            });
            imageView.setFocusable(true);
            imageView.setTag(Integer.valueOf(i));
            imageView.setPadding(AndroidUtilities.dp(18.0f), 0, AndroidUtilities.dp(18.0f), 0);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            imageView.setLayoutParams(new ViewGroup.LayoutParams(AndroidUtilities.dp(200.0f), -1));
            if (i == 0) {
                imageView.setContentDescription(LocaleController.getString("VoipRecordAudio", R.string.VoipRecordAudio));
            } else if (i == 1) {
                imageView.setContentDescription(LocaleController.getString("VoipRecordPortrait", R.string.VoipRecordPortrait));
            } else {
                imageView.setContentDescription(LocaleController.getString("VoipRecordLandscape", R.string.VoipRecordLandscape));
            }
            if (i == 0) {
                i2 = R.raw.record_audio;
            } else if (i == 1) {
                i2 = R.raw.record_video_p;
            } else {
                i2 = R.raw.record_video_l;
            }
            SvgHelper.SvgDrawable drawable = SvgHelper.getDrawable(RLottieDrawable.readRes(null, i2));
            drawable.setAspectFill(false);
            imageView.setImageDrawable(drawable);
            if (imageView.getParent() != null) {
                ((ViewGroup) imageView.getParent()).removeView(imageView);
            }
            viewGroup.addView(imageView, 0);
            return imageView;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$instantiateItem$0(int i, View view) {
            GroupCallRecordAlert.this.onStartRecord(i);
            GroupCallRecordAlert.this.dismiss();
        }

        @Override // androidx.viewpager.widget.PagerAdapter
        public void destroyItem(ViewGroup viewGroup, int i, Object obj) {
            viewGroup.removeView((View) obj);
        }

        @Override // androidx.viewpager.widget.PagerAdapter
        public void setPrimaryItem(ViewGroup viewGroup, int i, Object obj) {
            super.setPrimaryItem(viewGroup, i, obj);
        }

        @Override // androidx.viewpager.widget.PagerAdapter
        public boolean isViewFromObject(View view, Object obj) {
            return view.equals(obj);
        }
    }
}