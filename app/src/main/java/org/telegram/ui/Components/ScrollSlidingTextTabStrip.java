package org.telegram.ui.Components;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.SystemClock;
import android.text.Layout;
import android.util.SparseArray;
import android.util.SparseIntArray;
import android.view.View;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.ui.ActionBar.Theme;
/* loaded from: classes5.dex */
public class ScrollSlidingTextTabStrip extends HorizontalScrollView {
    private String activeTextColorKey;
    private int allTextWidth;
    private int animateFromIndicatorWidth;
    private int animateFromIndicaxtorX;
    private int animateIndicatorStartWidth;
    private int animateIndicatorStartX;
    private int animateIndicatorToWidth;
    private int animateIndicatorToX;
    private boolean animatingIndicator;
    private float animationIdicatorProgress;
    private Runnable animationRunnable;
    private boolean animationRunning;
    private float animationTime;
    private int currentPosition;
    private ScrollSlidingTabStripDelegate delegate;
    private SparseIntArray idToPosition;
    private int indicatorWidth;
    private float indicatorWidthAnimationDx;
    private int indicatorX;
    private float indicatorXAnimationDx;
    private CubicBezierInterpolator interpolator;
    private long lastAnimationTime;
    private SparseIntArray positionToId;
    private SparseIntArray positionToWidth;
    private int prevLayoutWidth;
    private int previousPosition;
    private Theme.ResourcesProvider resourcesProvider;
    private int scrollingToChild;
    private int selectedTabId;
    private String selectorColorKey;
    private GradientDrawable selectorDrawable;
    private int tabCount;
    private String tabLineColorKey;
    private LinearLayout tabsContainer;
    private String unactiveTextColorKey;
    private boolean useSameWidth;

    static /* synthetic */ float access$216(ScrollSlidingTextTabStrip x0, float x1) {
        float f = x0.animationTime + x1;
        x0.animationTime = f;
        return f;
    }

    /* loaded from: classes5.dex */
    public interface ScrollSlidingTabStripDelegate {
        void onPageScrolled(float f);

        void onPageSelected(int i, boolean z);

        void onSamePageSelected();

        /* renamed from: org.telegram.ui.Components.ScrollSlidingTextTabStrip$ScrollSlidingTabStripDelegate$-CC */
        /* loaded from: classes5.dex */
        public final /* synthetic */ class CC {
            public static void $default$onSamePageSelected(ScrollSlidingTabStripDelegate _this) {
            }
        }
    }

    public ScrollSlidingTextTabStrip(Context context) {
        this(context, null);
    }

    public ScrollSlidingTextTabStrip(Context context, Theme.ResourcesProvider resourcesProvider) {
        super(context);
        this.selectedTabId = -1;
        this.scrollingToChild = -1;
        this.tabLineColorKey = Theme.key_actionBarTabLine;
        this.activeTextColorKey = Theme.key_actionBarTabActiveText;
        this.unactiveTextColorKey = Theme.key_actionBarTabUnactiveText;
        this.selectorColorKey = Theme.key_actionBarTabSelector;
        this.interpolator = CubicBezierInterpolator.EASE_OUT_QUINT;
        this.positionToId = new SparseIntArray(5);
        this.idToPosition = new SparseIntArray(5);
        this.positionToWidth = new SparseIntArray(5);
        this.animationRunnable = new Runnable() { // from class: org.telegram.ui.Components.ScrollSlidingTextTabStrip.1
            {
                ScrollSlidingTextTabStrip.this = this;
            }

            @Override // java.lang.Runnable
            public void run() {
                if (!ScrollSlidingTextTabStrip.this.animatingIndicator) {
                    return;
                }
                long newTime = SystemClock.elapsedRealtime();
                long dt = newTime - ScrollSlidingTextTabStrip.this.lastAnimationTime;
                if (dt > 17) {
                    dt = 17;
                }
                ScrollSlidingTextTabStrip.access$216(ScrollSlidingTextTabStrip.this, ((float) dt) / 200.0f);
                ScrollSlidingTextTabStrip scrollSlidingTextTabStrip = ScrollSlidingTextTabStrip.this;
                scrollSlidingTextTabStrip.setAnimationIdicatorProgress(scrollSlidingTextTabStrip.interpolator.getInterpolation(ScrollSlidingTextTabStrip.this.animationTime));
                if (ScrollSlidingTextTabStrip.this.animationTime > 1.0f) {
                    ScrollSlidingTextTabStrip.this.animationTime = 1.0f;
                }
                if (ScrollSlidingTextTabStrip.this.animationTime < 1.0f) {
                    AndroidUtilities.runOnUIThread(ScrollSlidingTextTabStrip.this.animationRunnable);
                    return;
                }
                ScrollSlidingTextTabStrip.this.animatingIndicator = false;
                ScrollSlidingTextTabStrip.this.setEnabled(true);
                if (ScrollSlidingTextTabStrip.this.delegate != null) {
                    ScrollSlidingTextTabStrip.this.delegate.onPageScrolled(1.0f);
                }
            }
        };
        this.resourcesProvider = resourcesProvider;
        this.selectorDrawable = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, null);
        float rad = AndroidUtilities.dpf2(3.0f);
        this.selectorDrawable.setCornerRadii(new float[]{rad, rad, rad, rad, 0.0f, 0.0f, 0.0f, 0.0f});
        this.selectorDrawable.setColor(Theme.getColor(this.tabLineColorKey, resourcesProvider));
        setFillViewport(true);
        setWillNotDraw(false);
        setHorizontalScrollBarEnabled(false);
        LinearLayout linearLayout = new LinearLayout(context) { // from class: org.telegram.ui.Components.ScrollSlidingTextTabStrip.2
            {
                ScrollSlidingTextTabStrip.this = this;
            }

            @Override // android.view.View
            public void setAlpha(float alpha) {
                super.setAlpha(alpha);
                ScrollSlidingTextTabStrip.this.invalidate();
            }
        };
        this.tabsContainer = linearLayout;
        linearLayout.setOrientation(0);
        this.tabsContainer.setPadding(AndroidUtilities.dp(7.0f), 0, AndroidUtilities.dp(7.0f), 0);
        this.tabsContainer.setLayoutParams(new FrameLayout.LayoutParams(-1, -1));
        addView(this.tabsContainer);
    }

    public void setDelegate(ScrollSlidingTabStripDelegate scrollSlidingTabStripDelegate) {
        this.delegate = scrollSlidingTabStripDelegate;
    }

    public boolean isAnimatingIndicator() {
        return this.animatingIndicator;
    }

    private void setAnimationProgressInernal(TextView newTab, TextView prevTab, float value) {
        if (newTab != null && prevTab != null) {
            int newColor = Theme.getColor(this.activeTextColorKey, this.resourcesProvider);
            int prevColor = Theme.getColor(this.unactiveTextColorKey, this.resourcesProvider);
            int r1 = Color.red(newColor);
            int g1 = Color.green(newColor);
            int b1 = Color.blue(newColor);
            int a1 = Color.alpha(newColor);
            int r2 = Color.red(prevColor);
            int g2 = Color.green(prevColor);
            int b2 = Color.blue(prevColor);
            int a2 = Color.alpha(prevColor);
            prevTab.setTextColor(Color.argb((int) (a1 + ((a2 - a1) * value)), (int) (r1 + ((r2 - r1) * value)), (int) (g1 + ((g2 - g1) * value)), (int) (b1 + ((b2 - b1) * value))));
            newTab.setTextColor(Color.argb((int) (a2 + ((a1 - a2) * value)), (int) (r2 + ((r1 - r2) * value)), (int) (g2 + ((g1 - g2) * value)), (int) (b2 + ((b1 - b2) * value))));
            int i = this.animateIndicatorStartX;
            this.indicatorX = (int) (i + ((this.animateIndicatorToX - i) * value));
            int i2 = this.animateIndicatorStartWidth;
            this.indicatorWidth = (int) (i2 + ((this.animateIndicatorToWidth - i2) * value));
            invalidate();
        }
    }

    public void setAnimationIdicatorProgress(float value) {
        this.animationIdicatorProgress = value;
        TextView newTab = (TextView) this.tabsContainer.getChildAt(this.currentPosition);
        TextView prevTab = (TextView) this.tabsContainer.getChildAt(this.previousPosition);
        if (prevTab == null || newTab == null) {
            return;
        }
        setAnimationProgressInernal(newTab, prevTab, value);
        if (value >= 1.0f) {
            prevTab.setTag(this.unactiveTextColorKey);
            newTab.setTag(this.activeTextColorKey);
        }
        ScrollSlidingTabStripDelegate scrollSlidingTabStripDelegate = this.delegate;
        if (scrollSlidingTabStripDelegate != null) {
            scrollSlidingTabStripDelegate.onPageScrolled(value);
        }
    }

    public void setUseSameWidth(boolean value) {
        this.useSameWidth = value;
    }

    public Drawable getSelectorDrawable() {
        return this.selectorDrawable;
    }

    public ViewGroup getTabsContainer() {
        return this.tabsContainer;
    }

    public float getAnimationIdicatorProgress() {
        return this.animationIdicatorProgress;
    }

    public int getNextPageId(boolean forward) {
        return this.positionToId.get(this.currentPosition + (forward ? 1 : -1), -1);
    }

    public SparseArray<View> removeTabs() {
        SparseArray<View> views = new SparseArray<>();
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            views.get(this.positionToId.get(i), child);
        }
        this.positionToId.clear();
        this.idToPosition.clear();
        this.positionToWidth.clear();
        this.tabsContainer.removeAllViews();
        this.allTextWidth = 0;
        this.tabCount = 0;
        return views;
    }

    public int getTabsCount() {
        return this.tabCount;
    }

    public boolean hasTab(int id) {
        return this.idToPosition.get(id, -1) != -1;
    }

    public void addTextTab(int id, CharSequence text) {
        addTextTab(id, text, null);
    }

    public void addTextTab(final int id, CharSequence text, SparseArray<View> viewsCache) {
        int position = this.tabCount;
        this.tabCount = position + 1;
        if (position == 0 && this.selectedTabId == -1) {
            this.selectedTabId = id;
        }
        this.positionToId.put(position, id);
        this.idToPosition.put(id, position);
        int i = this.selectedTabId;
        if (i != -1 && i == id) {
            this.currentPosition = position;
            this.prevLayoutWidth = 0;
        }
        TextView tab = null;
        if (viewsCache != null) {
            tab = (TextView) viewsCache.get(id);
            viewsCache.delete(id);
        }
        if (tab == null) {
            tab = new TextView(getContext()) { // from class: org.telegram.ui.Components.ScrollSlidingTextTabStrip.3
                {
                    ScrollSlidingTextTabStrip.this = this;
                }

                @Override // android.view.View
                public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo info) {
                    super.onInitializeAccessibilityNodeInfo(info);
                    info.setSelected(ScrollSlidingTextTabStrip.this.selectedTabId == id);
                }
            };
            tab.setWillNotDraw(false);
            tab.setGravity(17);
            tab.setBackgroundDrawable(Theme.createSelectorDrawable(Theme.getColor(this.selectorColorKey, this.resourcesProvider), 3));
            tab.setTextSize(1, 15.0f);
            tab.setSingleLine(true);
            tab.setTypeface(AndroidUtilities.getTypeface(AndroidUtilities.TYPEFACE_ROBOTO_MEDIUM));
            tab.setPadding(AndroidUtilities.dp(16.0f), 0, AndroidUtilities.dp(16.0f), 0);
            tab.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Components.ScrollSlidingTextTabStrip$$ExternalSyntheticLambda1
                @Override // android.view.View.OnClickListener
                public final void onClick(View view) {
                    ScrollSlidingTextTabStrip.this.m2970x28370d04(id, view);
                }
            });
        }
        tab.setText(text);
        int tabWidth = ((int) Math.ceil(tab.getPaint().measureText(text, 0, text.length()))) + tab.getPaddingLeft() + tab.getPaddingRight();
        this.tabsContainer.addView(tab, LayoutHelper.createLinear(0, -1));
        this.allTextWidth += tabWidth;
        this.positionToWidth.put(position, tabWidth);
    }

    /* renamed from: lambda$addTextTab$0$org-telegram-ui-Components-ScrollSlidingTextTabStrip */
    public /* synthetic */ void m2970x28370d04(int id, View v) {
        ScrollSlidingTabStripDelegate scrollSlidingTabStripDelegate;
        int position1 = this.tabsContainer.indexOfChild(v);
        if (position1 < 0) {
            return;
        }
        int i = this.currentPosition;
        if (position1 == i && (scrollSlidingTabStripDelegate = this.delegate) != null) {
            scrollSlidingTabStripDelegate.onSamePageSelected();
            return;
        }
        boolean scrollingForward = i < position1;
        this.scrollingToChild = -1;
        this.previousPosition = i;
        this.currentPosition = position1;
        this.selectedTabId = id;
        if (this.animatingIndicator) {
            AndroidUtilities.cancelRunOnUIThread(this.animationRunnable);
            this.animatingIndicator = false;
        }
        this.animationTime = 0.0f;
        this.animatingIndicator = true;
        this.animateIndicatorStartX = this.indicatorX;
        this.animateIndicatorStartWidth = this.indicatorWidth;
        TextView nextChild = (TextView) v;
        this.animateIndicatorToWidth = getChildWidth(nextChild);
        this.animateIndicatorToX = nextChild.getLeft() + ((nextChild.getMeasuredWidth() - this.animateIndicatorToWidth) / 2);
        setEnabled(false);
        AndroidUtilities.runOnUIThread(this.animationRunnable, 16L);
        ScrollSlidingTabStripDelegate scrollSlidingTabStripDelegate2 = this.delegate;
        if (scrollSlidingTabStripDelegate2 != null) {
            scrollSlidingTabStripDelegate2.onPageSelected(id, scrollingForward);
        }
        scrollToChild(position1);
    }

    public void finishAddingTabs() {
        int count = this.tabsContainer.getChildCount();
        int a = 0;
        while (a < count) {
            TextView tab = (TextView) this.tabsContainer.getChildAt(a);
            tab.setTag(this.currentPosition == a ? this.activeTextColorKey : this.unactiveTextColorKey);
            tab.setTextColor(Theme.getColor(this.currentPosition == a ? this.activeTextColorKey : this.unactiveTextColorKey, this.resourcesProvider));
            if (a == 0) {
                tab.getLayoutParams().width = count == 1 ? -2 : 0;
            }
            a++;
        }
    }

    public void setColors(String line, String active, String unactive, String selector) {
        this.tabLineColorKey = line;
        this.activeTextColorKey = active;
        this.unactiveTextColorKey = unactive;
        this.selectorColorKey = selector;
        this.selectorDrawable.setColor(Theme.getColor(line, this.resourcesProvider));
    }

    public int getCurrentTabId() {
        return this.selectedTabId;
    }

    public void setInitialTabId(int id) {
        this.selectedTabId = id;
        int pos = this.idToPosition.get(id);
        TextView child = (TextView) this.tabsContainer.getChildAt(pos);
        if (child != null) {
            this.currentPosition = pos;
            this.prevLayoutWidth = 0;
            finishAddingTabs();
            requestLayout();
        }
    }

    public void resetTab() {
        this.selectedTabId = -1;
    }

    public int getFirstTabId() {
        return this.positionToId.get(0, 0);
    }

    @Override // android.view.ViewGroup
    protected boolean drawChild(Canvas canvas, View child, long drawingTime) {
        boolean result = super.drawChild(canvas, child, drawingTime);
        if (child == this.tabsContainer) {
            int height = getMeasuredHeight();
            this.selectorDrawable.setAlpha((int) (this.tabsContainer.getAlpha() * 255.0f));
            float x = this.indicatorX + this.indicatorXAnimationDx;
            float w = this.indicatorWidth + x + this.indicatorWidthAnimationDx;
            this.selectorDrawable.setBounds((int) x, height - AndroidUtilities.dpr(4.0f), (int) w, height);
            this.selectorDrawable.draw(canvas);
        }
        return result;
    }

    @Override // android.widget.HorizontalScrollView, android.widget.FrameLayout, android.view.View
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = View.MeasureSpec.getSize(widthMeasureSpec) - AndroidUtilities.dp(22.0f);
        int count = this.tabsContainer.getChildCount();
        for (int a = 0; a < count; a++) {
            View child = this.tabsContainer.getChildAt(a);
            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) child.getLayoutParams();
            int i = this.allTextWidth;
            if (i > width) {
                layoutParams.weight = 0.0f;
                layoutParams.width = -2;
            } else if (this.useSameWidth) {
                layoutParams.weight = 1.0f / count;
                layoutParams.width = 0;
            } else if (a == 0 && count == 1) {
                layoutParams.weight = 0.0f;
                layoutParams.width = -2;
            } else {
                layoutParams.weight = (1.0f / i) * this.positionToWidth.get(a);
                layoutParams.width = 0;
            }
        }
        if (count == 1 || this.allTextWidth > width) {
            this.tabsContainer.setWeightSum(0.0f);
        } else {
            this.tabsContainer.setWeightSum(1.0f);
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    private void scrollToChild(int position) {
        if (this.tabCount == 0 || this.scrollingToChild == position) {
            return;
        }
        this.scrollingToChild = position;
        TextView child = (TextView) this.tabsContainer.getChildAt(position);
        if (child == null) {
            return;
        }
        int currentScrollX = getScrollX();
        int left = child.getLeft();
        int width = child.getMeasuredWidth();
        if (left - AndroidUtilities.dp(50.0f) < currentScrollX) {
            smoothScrollTo(left - AndroidUtilities.dp(50.0f), 0);
        } else if (left + width + AndroidUtilities.dp(21.0f) > getWidth() + currentScrollX) {
            smoothScrollTo(left + width, 0);
        }
    }

    @Override // android.widget.HorizontalScrollView, android.widget.FrameLayout, android.view.ViewGroup, android.view.View
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int i;
        super.onLayout(changed, l, t, r, b);
        if (this.prevLayoutWidth != r - l) {
            this.prevLayoutWidth = r - l;
            this.scrollingToChild = -1;
            if (this.animatingIndicator) {
                AndroidUtilities.cancelRunOnUIThread(this.animationRunnable);
                this.animatingIndicator = false;
                setEnabled(true);
                ScrollSlidingTabStripDelegate scrollSlidingTabStripDelegate = this.delegate;
                if (scrollSlidingTabStripDelegate != null) {
                    scrollSlidingTabStripDelegate.onPageScrolled(1.0f);
                }
            }
            TextView child = (TextView) this.tabsContainer.getChildAt(this.currentPosition);
            if (child != null) {
                this.indicatorWidth = getChildWidth(child);
                int left = child.getLeft();
                int measuredWidth = child.getMeasuredWidth();
                int i2 = this.indicatorWidth;
                int i3 = left + ((measuredWidth - i2) / 2);
                this.indicatorX = i3;
                int i4 = this.animateFromIndicaxtorX;
                if (i4 > 0 && (i = this.animateFromIndicatorWidth) > 0) {
                    if (i4 != i3 || i != i2) {
                        final int dX = i4 - i3;
                        final int dW = i - i2;
                        ValueAnimator valueAnimator = ValueAnimator.ofFloat(1.0f, 0.0f);
                        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: org.telegram.ui.Components.ScrollSlidingTextTabStrip$$ExternalSyntheticLambda0
                            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                            public final void onAnimationUpdate(ValueAnimator valueAnimator2) {
                                ScrollSlidingTextTabStrip.this.m2971x57ecd781(dX, dW, valueAnimator2);
                            }
                        });
                        valueAnimator.setDuration(200L);
                        valueAnimator.setInterpolator(CubicBezierInterpolator.DEFAULT);
                        valueAnimator.start();
                    }
                    this.animateFromIndicaxtorX = 0;
                    this.animateFromIndicatorWidth = 0;
                }
            }
        }
    }

    /* renamed from: lambda$onLayout$1$org-telegram-ui-Components-ScrollSlidingTextTabStrip */
    public /* synthetic */ void m2971x57ecd781(int dX, int dW, ValueAnimator valueAnimator1) {
        float v = ((Float) valueAnimator1.getAnimatedValue()).floatValue();
        this.indicatorXAnimationDx = dX * v;
        this.indicatorWidthAnimationDx = dW * v;
        this.tabsContainer.invalidate();
        invalidate();
    }

    public int getCurrentPosition() {
        return this.currentPosition;
    }

    @Override // android.view.View
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        int count = this.tabsContainer.getChildCount();
        for (int a = 0; a < count; a++) {
            View child = this.tabsContainer.getChildAt(a);
            child.setEnabled(enabled);
        }
    }

    public void selectTabWithId(int id, float progress) {
        int position = this.idToPosition.get(id, -1);
        if (position < 0) {
            return;
        }
        if (progress < 0.0f) {
            progress = 0.0f;
        } else if (progress > 1.0f) {
            progress = 1.0f;
        }
        TextView child = (TextView) this.tabsContainer.getChildAt(this.currentPosition);
        TextView nextChild = (TextView) this.tabsContainer.getChildAt(position);
        if (child != null && nextChild != null) {
            this.animateIndicatorStartWidth = getChildWidth(child);
            this.animateIndicatorStartX = child.getLeft() + ((child.getMeasuredWidth() - this.animateIndicatorStartWidth) / 2);
            this.animateIndicatorToWidth = getChildWidth(nextChild);
            this.animateIndicatorToX = nextChild.getLeft() + ((nextChild.getMeasuredWidth() - this.animateIndicatorToWidth) / 2);
            setAnimationProgressInernal(nextChild, child, progress);
            if (progress >= 1.0f) {
                child.setTag(this.unactiveTextColorKey);
                nextChild.setTag(this.activeTextColorKey);
            }
            scrollToChild(this.tabsContainer.indexOfChild(nextChild));
        }
        if (progress >= 1.0f) {
            this.currentPosition = position;
            this.selectedTabId = id;
        }
    }

    private int getChildWidth(TextView child) {
        Layout layout = child.getLayout();
        if (layout != null) {
            return ((int) Math.ceil(layout.getLineWidth(0))) + AndroidUtilities.dp(2.0f);
        }
        return child.getMeasuredWidth();
    }

    public void onPageScrolled(int position, int first) {
        if (this.currentPosition == position) {
            return;
        }
        this.currentPosition = position;
        if (position >= this.tabsContainer.getChildCount()) {
            return;
        }
        int a = 0;
        while (true) {
            boolean z = true;
            if (a >= this.tabsContainer.getChildCount()) {
                break;
            }
            View childAt = this.tabsContainer.getChildAt(a);
            if (a != position) {
                z = false;
            }
            childAt.setSelected(z);
            a++;
        }
        if (first == position && position > 1) {
            scrollToChild(position - 1);
        } else {
            scrollToChild(position);
        }
        invalidate();
    }

    public void recordIndicatorParams() {
        this.animateFromIndicaxtorX = this.indicatorX;
        this.animateFromIndicatorWidth = this.indicatorWidth;
    }
}
