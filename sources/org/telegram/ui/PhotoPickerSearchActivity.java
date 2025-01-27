package org.telegram.ui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.Property;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.animation.Interpolator;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.HashMap;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.LiteMode;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MediaDataController;
import org.telegram.messenger.R;
import org.telegram.ui.ActionBar.ActionBar;
import org.telegram.ui.ActionBar.ActionBarMenuItem;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.ActionBar.ThemeDescription;
import org.telegram.ui.Components.EditTextBoldCursor;
import org.telegram.ui.Components.EditTextEmoji;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.ui.Components.RecyclerListView;
import org.telegram.ui.Components.ScrollSlidingTextTabStrip;
import org.telegram.ui.Components.SizeNotifierFrameLayout;
import org.telegram.ui.PhotoPickerActivity;

/* loaded from: classes4.dex */
public class PhotoPickerSearchActivity extends BaseFragment {
    private static final Interpolator interpolator = new Interpolator() { // from class: org.telegram.ui.PhotoPickerSearchActivity$$ExternalSyntheticLambda0
        @Override // android.animation.TimeInterpolator
        public final float getInterpolation(float f) {
            float lambda$static$0;
            lambda$static$0 = PhotoPickerSearchActivity.lambda$static$0(f);
            return lambda$static$0;
        }
    };
    private boolean animatingForward;
    private boolean backAnimation;
    private EditTextEmoji commentTextView;
    private PhotoPickerActivity gifsSearch;
    private PhotoPickerActivity imagesSearch;
    private int maximumVelocity;
    private ScrollSlidingTextTabStrip scrollSlidingTextTabStrip;
    private ActionBarMenuItem searchItem;
    private AnimatorSet tabsAnimation;
    private boolean tabsAnimationInProgress;
    private boolean swipeBackEnabled = true;
    private Paint backgroundPaint = new Paint();
    private ViewPage[] viewPages = new ViewPage[2];

    private static class ViewPage extends FrameLayout {
        private ActionBar actionBar;
        private FrameLayout fragmentView;
        private RecyclerListView listView;
        private BaseFragment parentFragment;
        private int selectedType;

        public ViewPage(Context context) {
            super(context);
        }
    }

    public PhotoPickerSearchActivity(HashMap hashMap, ArrayList arrayList, int i, boolean z, ChatActivity chatActivity) {
        this.imagesSearch = new PhotoPickerActivity(0, null, hashMap, arrayList, i, z, chatActivity, false);
        this.gifsSearch = new PhotoPickerActivity(1, null, hashMap, arrayList, i, z, chatActivity, false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ float lambda$static$0(float f) {
        float f2 = f - 1.0f;
        return (f2 * f2 * f2 * f2 * f2) + 1.0f;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void searchText(String str) {
        this.searchItem.getSearchField().setText(str);
        this.searchItem.getSearchField().setSelection(str.length());
        this.actionBar.onSearchPressed();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setScrollY(float f) {
        this.actionBar.setTranslationY(f);
        int i = 0;
        while (true) {
            ViewPage[] viewPageArr = this.viewPages;
            if (i >= viewPageArr.length) {
                this.fragmentView.invalidate();
                return;
            } else {
                viewPageArr[i].listView.setPinnedSectionOffsetY((int) f);
                i++;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void switchToCurrentSelectedMode(boolean z) {
        ViewPage[] viewPageArr;
        int i = 0;
        while (true) {
            viewPageArr = this.viewPages;
            if (i >= viewPageArr.length) {
                break;
            }
            viewPageArr[i].listView.stopScroll();
            i++;
        }
        viewPageArr[z ? 1 : 0].listView.getAdapter();
        this.viewPages[z ? 1 : 0].listView.setPinnedHeaderShadowDrawable(null);
        if (this.actionBar.getTranslationY() != 0.0f) {
            ((LinearLayoutManager) this.viewPages[z ? 1 : 0].listView.getLayoutManager()).scrollToPositionWithOffset(0, (int) this.actionBar.getTranslationY());
        }
    }

    private void updateTabs() {
        ScrollSlidingTextTabStrip scrollSlidingTextTabStrip = this.scrollSlidingTextTabStrip;
        if (scrollSlidingTextTabStrip == null) {
            return;
        }
        scrollSlidingTextTabStrip.addTextTab(0, LocaleController.getString(R.string.ImagesTab2));
        this.scrollSlidingTextTabStrip.addTextTab(1, LocaleController.getString(R.string.GifsTab2));
        this.scrollSlidingTextTabStrip.setVisibility(0);
        this.actionBar.setExtraHeight(AndroidUtilities.dp(44.0f));
        int currentTabId = this.scrollSlidingTextTabStrip.getCurrentTabId();
        if (currentTabId >= 0) {
            this.viewPages[0].selectedType = currentTabId;
        }
        this.scrollSlidingTextTabStrip.finishAddingTabs();
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public View createView(Context context) {
        this.actionBar.setBackgroundColor(Theme.getColor(Theme.key_dialogBackground));
        ActionBar actionBar = this.actionBar;
        int i = Theme.key_dialogTextBlack;
        actionBar.setTitleColor(Theme.getColor(i));
        this.actionBar.setItemsColor(Theme.getColor(i), false);
        ActionBar actionBar2 = this.actionBar;
        int i2 = Theme.key_dialogButtonSelector;
        actionBar2.setItemsBackgroundColor(Theme.getColor(i2), false);
        this.actionBar.setBackButtonImage(R.drawable.ic_ab_back);
        if (AndroidUtilities.isTablet()) {
            this.actionBar.setOccupyStatusBar(false);
        }
        this.actionBar.setExtraHeight(AndroidUtilities.dp(44.0f));
        this.actionBar.setAllowOverlayTitle(false);
        this.actionBar.setAddToContainer(false);
        this.actionBar.setClipContent(true);
        this.actionBar.setActionBarMenuOnItemClick(new ActionBar.ActionBarMenuOnItemClick() { // from class: org.telegram.ui.PhotoPickerSearchActivity.1
            @Override // org.telegram.ui.ActionBar.ActionBar.ActionBarMenuOnItemClick
            public void onItemClick(int i3) {
                if (i3 == -1) {
                    PhotoPickerSearchActivity.this.lambda$onBackPressed$323();
                }
            }
        });
        this.hasOwnBackground = true;
        ActionBarMenuItem actionBarMenuItemSearchListener = this.actionBar.createMenu().addItem(0, R.drawable.ic_ab_search).setIsSearchField(true).setActionBarMenuItemSearchListener(new ActionBarMenuItem.ActionBarMenuItemSearchListener() { // from class: org.telegram.ui.PhotoPickerSearchActivity.2
            @Override // org.telegram.ui.ActionBar.ActionBarMenuItem.ActionBarMenuItemSearchListener
            public boolean canCollapseSearch() {
                PhotoPickerSearchActivity.this.lambda$onBackPressed$323();
                return false;
            }

            @Override // org.telegram.ui.ActionBar.ActionBarMenuItem.ActionBarMenuItemSearchListener
            public void onSearchExpand() {
                PhotoPickerSearchActivity.this.imagesSearch.getActionBar().openSearchField("", false);
                PhotoPickerSearchActivity.this.gifsSearch.getActionBar().openSearchField("", false);
                PhotoPickerSearchActivity.this.searchItem.getSearchField().requestFocus();
            }

            @Override // org.telegram.ui.ActionBar.ActionBarMenuItem.ActionBarMenuItemSearchListener
            public void onSearchPressed(EditText editText) {
                PhotoPickerSearchActivity.this.imagesSearch.getActionBar().onSearchPressed();
                PhotoPickerSearchActivity.this.gifsSearch.getActionBar().onSearchPressed();
            }

            @Override // org.telegram.ui.ActionBar.ActionBarMenuItem.ActionBarMenuItemSearchListener
            public void onTextChanged(EditText editText) {
                PhotoPickerSearchActivity.this.imagesSearch.getActionBar().setSearchFieldText(editText.getText().toString());
                PhotoPickerSearchActivity.this.gifsSearch.getActionBar().setSearchFieldText(editText.getText().toString());
            }
        });
        this.searchItem = actionBarMenuItemSearchListener;
        actionBarMenuItemSearchListener.setSearchFieldHint(LocaleController.getString(R.string.SearchImagesTitle));
        EditTextBoldCursor searchField = this.searchItem.getSearchField();
        searchField.setTextColor(Theme.getColor(i));
        searchField.setCursorColor(Theme.getColor(i));
        searchField.setHintTextColor(Theme.getColor(Theme.key_chat_messagePanelHint));
        ScrollSlidingTextTabStrip scrollSlidingTextTabStrip = new ScrollSlidingTextTabStrip(context);
        this.scrollSlidingTextTabStrip = scrollSlidingTextTabStrip;
        scrollSlidingTextTabStrip.setUseSameWidth(true);
        ScrollSlidingTextTabStrip scrollSlidingTextTabStrip2 = this.scrollSlidingTextTabStrip;
        int i3 = Theme.key_chat_attachActiveTab;
        scrollSlidingTextTabStrip2.setColors(i3, i3, Theme.key_chat_attachUnactiveTab, i2);
        this.actionBar.addView(this.scrollSlidingTextTabStrip, LayoutHelper.createFrame(-1, 44, 83));
        this.scrollSlidingTextTabStrip.setDelegate(new ScrollSlidingTextTabStrip.ScrollSlidingTabStripDelegate() { // from class: org.telegram.ui.PhotoPickerSearchActivity.3
            @Override // org.telegram.ui.Components.ScrollSlidingTextTabStrip.ScrollSlidingTabStripDelegate
            public void onPageScrolled(float f) {
                ViewPage viewPage;
                float measuredWidth;
                float measuredWidth2;
                if (f != 1.0f || PhotoPickerSearchActivity.this.viewPages[1].getVisibility() == 0) {
                    if (PhotoPickerSearchActivity.this.animatingForward) {
                        PhotoPickerSearchActivity.this.viewPages[0].setTranslationX((-f) * PhotoPickerSearchActivity.this.viewPages[0].getMeasuredWidth());
                        viewPage = PhotoPickerSearchActivity.this.viewPages[1];
                        measuredWidth = PhotoPickerSearchActivity.this.viewPages[0].getMeasuredWidth();
                        measuredWidth2 = PhotoPickerSearchActivity.this.viewPages[0].getMeasuredWidth() * f;
                    } else {
                        PhotoPickerSearchActivity.this.viewPages[0].setTranslationX(PhotoPickerSearchActivity.this.viewPages[0].getMeasuredWidth() * f);
                        viewPage = PhotoPickerSearchActivity.this.viewPages[1];
                        measuredWidth = PhotoPickerSearchActivity.this.viewPages[0].getMeasuredWidth() * f;
                        measuredWidth2 = PhotoPickerSearchActivity.this.viewPages[0].getMeasuredWidth();
                    }
                    viewPage.setTranslationX(measuredWidth - measuredWidth2);
                    if (f == 1.0f) {
                        ViewPage viewPage2 = PhotoPickerSearchActivity.this.viewPages[0];
                        PhotoPickerSearchActivity.this.viewPages[0] = PhotoPickerSearchActivity.this.viewPages[1];
                        PhotoPickerSearchActivity.this.viewPages[1] = viewPage2;
                        PhotoPickerSearchActivity.this.viewPages[1].setVisibility(8);
                    }
                }
            }

            @Override // org.telegram.ui.Components.ScrollSlidingTextTabStrip.ScrollSlidingTabStripDelegate
            public void onPageSelected(int i4, boolean z) {
                ActionBarMenuItem actionBarMenuItem;
                int i5;
                if (PhotoPickerSearchActivity.this.viewPages[0].selectedType == i4) {
                    return;
                }
                PhotoPickerSearchActivity photoPickerSearchActivity = PhotoPickerSearchActivity.this;
                photoPickerSearchActivity.swipeBackEnabled = i4 == photoPickerSearchActivity.scrollSlidingTextTabStrip.getFirstTabId();
                PhotoPickerSearchActivity.this.viewPages[1].selectedType = i4;
                PhotoPickerSearchActivity.this.viewPages[1].setVisibility(0);
                PhotoPickerSearchActivity.this.switchToCurrentSelectedMode(true);
                PhotoPickerSearchActivity.this.animatingForward = z;
                if (i4 == 0) {
                    actionBarMenuItem = PhotoPickerSearchActivity.this.searchItem;
                    i5 = R.string.SearchImagesTitle;
                } else {
                    actionBarMenuItem = PhotoPickerSearchActivity.this.searchItem;
                    i5 = R.string.SearchGifsTitle;
                }
                actionBarMenuItem.setSearchFieldHint(LocaleController.getString(i5));
            }

            @Override // org.telegram.ui.Components.ScrollSlidingTextTabStrip.ScrollSlidingTabStripDelegate
            public /* synthetic */ void onSamePageSelected() {
                ScrollSlidingTextTabStrip.ScrollSlidingTabStripDelegate.-CC.$default$onSamePageSelected(this);
            }
        });
        this.maximumVelocity = ViewConfiguration.get(context).getScaledMaximumFlingVelocity();
        SizeNotifierFrameLayout sizeNotifierFrameLayout = new SizeNotifierFrameLayout(context) { // from class: org.telegram.ui.PhotoPickerSearchActivity.4
            private boolean globalIgnoreLayout;
            private boolean maybeStartTracking;
            private boolean startedTracking;
            private int startedTrackingPointerId;
            private int startedTrackingX;
            private int startedTrackingY;
            private VelocityTracker velocityTracker;

            private boolean prepareForMoving(MotionEvent motionEvent, boolean z) {
                ViewPage viewPage;
                int i4;
                int nextPageId = PhotoPickerSearchActivity.this.scrollSlidingTextTabStrip.getNextPageId(z);
                if (nextPageId < 0) {
                    return false;
                }
                getParent().requestDisallowInterceptTouchEvent(true);
                this.maybeStartTracking = false;
                this.startedTracking = true;
                this.startedTrackingX = (int) motionEvent.getX();
                ((BaseFragment) PhotoPickerSearchActivity.this).actionBar.setEnabled(false);
                PhotoPickerSearchActivity.this.scrollSlidingTextTabStrip.setEnabled(false);
                PhotoPickerSearchActivity.this.viewPages[1].selectedType = nextPageId;
                PhotoPickerSearchActivity.this.viewPages[1].setVisibility(0);
                PhotoPickerSearchActivity.this.animatingForward = z;
                PhotoPickerSearchActivity.this.switchToCurrentSelectedMode(true);
                ViewPage[] viewPageArr = PhotoPickerSearchActivity.this.viewPages;
                if (z) {
                    viewPage = viewPageArr[1];
                    i4 = PhotoPickerSearchActivity.this.viewPages[0].getMeasuredWidth();
                } else {
                    viewPage = viewPageArr[1];
                    i4 = -PhotoPickerSearchActivity.this.viewPages[0].getMeasuredWidth();
                }
                viewPage.setTranslationX(i4);
                return true;
            }

            /* JADX WARN: Removed duplicated region for block: B:13:0x00a4  */
            /*
                Code decompiled incorrectly, please refer to instructions dump.
            */
            public boolean checkTabsAnimationInProgress() {
                if (!PhotoPickerSearchActivity.this.tabsAnimationInProgress) {
                    return false;
                }
                if (!PhotoPickerSearchActivity.this.backAnimation) {
                    if (Math.abs(PhotoPickerSearchActivity.this.viewPages[1].getTranslationX()) < 1.0f) {
                        PhotoPickerSearchActivity.this.viewPages[0].setTranslationX(PhotoPickerSearchActivity.this.viewPages[0].getMeasuredWidth() * (PhotoPickerSearchActivity.this.animatingForward ? -1 : 1));
                        PhotoPickerSearchActivity.this.viewPages[1].setTranslationX(0.0f);
                        if (PhotoPickerSearchActivity.this.tabsAnimation != null) {
                        }
                        PhotoPickerSearchActivity.this.tabsAnimationInProgress = false;
                    }
                    return PhotoPickerSearchActivity.this.tabsAnimationInProgress;
                }
                if (Math.abs(PhotoPickerSearchActivity.this.viewPages[0].getTranslationX()) < 1.0f) {
                    PhotoPickerSearchActivity.this.viewPages[0].setTranslationX(0.0f);
                    PhotoPickerSearchActivity.this.viewPages[1].setTranslationX(PhotoPickerSearchActivity.this.viewPages[0].getMeasuredWidth() * (PhotoPickerSearchActivity.this.animatingForward ? 1 : -1));
                    if (PhotoPickerSearchActivity.this.tabsAnimation != null) {
                        PhotoPickerSearchActivity.this.tabsAnimation.cancel();
                        PhotoPickerSearchActivity.this.tabsAnimation = null;
                    }
                    PhotoPickerSearchActivity.this.tabsAnimationInProgress = false;
                }
                return PhotoPickerSearchActivity.this.tabsAnimationInProgress;
            }

            @Override // org.telegram.ui.Components.SizeNotifierFrameLayout, android.view.ViewGroup, android.view.View
            protected void dispatchDraw(Canvas canvas) {
                super.dispatchDraw(canvas);
                float measuredHeight = ((BaseFragment) PhotoPickerSearchActivity.this).actionBar.getMeasuredHeight() + ((int) ((BaseFragment) PhotoPickerSearchActivity.this).actionBar.getTranslationY());
                canvas.drawLine(0.0f, measuredHeight, getWidth(), measuredHeight, Theme.dividerPaint);
            }

            @Override // android.view.View
            public void forceHasOverlappingRendering(boolean z) {
                super.forceHasOverlappingRendering(z);
            }

            @Override // android.view.View
            protected void onDraw(Canvas canvas) {
                PhotoPickerSearchActivity.this.backgroundPaint.setColor(Theme.getColor(Theme.key_windowBackgroundGray));
                canvas.drawRect(0.0f, ((BaseFragment) PhotoPickerSearchActivity.this).actionBar.getMeasuredHeight() + ((BaseFragment) PhotoPickerSearchActivity.this).actionBar.getTranslationY(), getMeasuredWidth(), getMeasuredHeight(), PhotoPickerSearchActivity.this.backgroundPaint);
            }

            @Override // android.view.ViewGroup
            public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
                return checkTabsAnimationInProgress() || PhotoPickerSearchActivity.this.scrollSlidingTextTabStrip.isAnimatingIndicator() || onTouchEvent(motionEvent);
            }

            /* JADX WARN: Removed duplicated region for block: B:22:0x0078  */
            /* JADX WARN: Removed duplicated region for block: B:29:0x00a6  */
            /* JADX WARN: Removed duplicated region for block: B:38:0x0094  */
            @Override // org.telegram.ui.Components.SizeNotifierFrameLayout, android.widget.FrameLayout, android.view.ViewGroup, android.view.View
            /*
                Code decompiled incorrectly, please refer to instructions dump.
            */
            protected void onLayout(boolean z, int i4, int i5, int i6, int i7) {
                int i8;
                int i9;
                int i10;
                int i11;
                int i12;
                int childCount = getChildCount();
                int emojiPadding = (AndroidUtilities.dp(20.0f) < 0 || AndroidUtilities.isInMultiwindow || AndroidUtilities.isTablet()) ? 0 : PhotoPickerSearchActivity.this.commentTextView.getEmojiPadding();
                setBottomClip(emojiPadding);
                for (int i13 = 0; i13 < childCount; i13++) {
                    View childAt = getChildAt(i13);
                    if (childAt.getVisibility() != 8) {
                        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) childAt.getLayoutParams();
                        int measuredWidth = childAt.getMeasuredWidth();
                        int measuredHeight = childAt.getMeasuredHeight();
                        int i14 = layoutParams.gravity;
                        if (i14 == -1) {
                            i14 = 51;
                        }
                        int i15 = i14 & 112;
                        int i16 = i14 & 7;
                        if (i16 == 1) {
                            i8 = (((i6 - i4) - measuredWidth) / 2) + layoutParams.leftMargin;
                            i9 = layoutParams.rightMargin;
                        } else if (i16 != 5) {
                            i10 = layoutParams.leftMargin + getPaddingLeft();
                            if (i15 == 16) {
                                if (i15 == 48) {
                                    i12 = layoutParams.topMargin + getPaddingTop();
                                } else if (i15 != 80) {
                                    i12 = layoutParams.topMargin;
                                } else {
                                    i11 = ((i7 - emojiPadding) - i5) - measuredHeight;
                                }
                                if (PhotoPickerSearchActivity.this.commentTextView != null && PhotoPickerSearchActivity.this.commentTextView.isPopupView(childAt)) {
                                    AndroidUtilities.isTablet();
                                    i12 = getMeasuredHeight() - childAt.getMeasuredHeight();
                                }
                                childAt.layout(i10, i12, measuredWidth + i10, measuredHeight + i12);
                            } else {
                                i11 = ((((i7 - emojiPadding) - i5) - measuredHeight) / 2) + layoutParams.topMargin;
                            }
                            i12 = i11 - layoutParams.bottomMargin;
                            if (PhotoPickerSearchActivity.this.commentTextView != null) {
                                AndroidUtilities.isTablet();
                                i12 = getMeasuredHeight() - childAt.getMeasuredHeight();
                            }
                            childAt.layout(i10, i12, measuredWidth + i10, measuredHeight + i12);
                        } else {
                            i8 = ((i6 - i4) - measuredWidth) - layoutParams.rightMargin;
                            i9 = getPaddingRight();
                        }
                        i10 = i8 - i9;
                        if (i15 == 16) {
                        }
                        i12 = i11 - layoutParams.bottomMargin;
                        if (PhotoPickerSearchActivity.this.commentTextView != null) {
                        }
                        childAt.layout(i10, i12, measuredWidth + i10, measuredHeight + i12);
                    }
                }
                notifyHeightChanged();
            }

            @Override // android.widget.FrameLayout, android.view.View
            protected void onMeasure(int i4, int i5) {
                int makeMeasureSpec;
                int paddingTop;
                int size = View.MeasureSpec.getSize(i4);
                int size2 = View.MeasureSpec.getSize(i5);
                setMeasuredDimension(size, size2);
                measureChildWithMargins(((BaseFragment) PhotoPickerSearchActivity.this).actionBar, i4, 0, i5, 0);
                if (AndroidUtilities.dp(20.0f) < 0) {
                    this.globalIgnoreLayout = true;
                    PhotoPickerSearchActivity.this.commentTextView.hideEmojiView();
                    this.globalIgnoreLayout = false;
                } else if (!AndroidUtilities.isInMultiwindow) {
                    size2 -= PhotoPickerSearchActivity.this.commentTextView.getEmojiPadding();
                    i5 = View.MeasureSpec.makeMeasureSpec(size2, 1073741824);
                }
                int measuredHeight = ((BaseFragment) PhotoPickerSearchActivity.this).actionBar.getMeasuredHeight();
                this.globalIgnoreLayout = true;
                for (int i6 = 0; i6 < PhotoPickerSearchActivity.this.viewPages.length; i6++) {
                    if (PhotoPickerSearchActivity.this.viewPages[i6] != null && PhotoPickerSearchActivity.this.viewPages[i6].listView != null) {
                        PhotoPickerSearchActivity.this.viewPages[i6].listView.setPadding(AndroidUtilities.dp(4.0f), AndroidUtilities.dp(4.0f) + measuredHeight, AndroidUtilities.dp(4.0f), AndroidUtilities.dp(4.0f));
                    }
                }
                this.globalIgnoreLayout = false;
                int childCount = getChildCount();
                for (int i7 = 0; i7 < childCount; i7++) {
                    View childAt = getChildAt(i7);
                    if (childAt != null && childAt.getVisibility() != 8 && childAt != ((BaseFragment) PhotoPickerSearchActivity.this).actionBar) {
                        if (PhotoPickerSearchActivity.this.commentTextView == null || !PhotoPickerSearchActivity.this.commentTextView.isPopupView(childAt)) {
                            measureChildWithMargins(childAt, i4, 0, i5, 0);
                        } else {
                            if (!AndroidUtilities.isInMultiwindow && !AndroidUtilities.isTablet()) {
                                makeMeasureSpec = View.MeasureSpec.makeMeasureSpec(size, 1073741824);
                                paddingTop = childAt.getLayoutParams().height;
                            } else if (AndroidUtilities.isTablet()) {
                                makeMeasureSpec = View.MeasureSpec.makeMeasureSpec(size, 1073741824);
                                paddingTop = Math.min(AndroidUtilities.dp(AndroidUtilities.isTablet() ? 200.0f : 320.0f), (size2 - AndroidUtilities.statusBarHeight) + getPaddingTop());
                            } else {
                                makeMeasureSpec = View.MeasureSpec.makeMeasureSpec(size, 1073741824);
                                paddingTop = (size2 - AndroidUtilities.statusBarHeight) + getPaddingTop();
                            }
                            childAt.measure(makeMeasureSpec, View.MeasureSpec.makeMeasureSpec(paddingTop, 1073741824));
                        }
                    }
                }
            }

            @Override // android.view.View
            public boolean onTouchEvent(MotionEvent motionEvent) {
                float f;
                float f2;
                float measuredWidth;
                ViewPage viewPage;
                int measuredWidth2;
                if (((BaseFragment) PhotoPickerSearchActivity.this).parentLayout.checkTransitionAnimation() || checkTabsAnimationInProgress()) {
                    return false;
                }
                if (motionEvent != null) {
                    if (this.velocityTracker == null) {
                        this.velocityTracker = VelocityTracker.obtain();
                    }
                    this.velocityTracker.addMovement(motionEvent);
                }
                if (motionEvent != null && motionEvent.getAction() == 0 && !this.startedTracking && !this.maybeStartTracking) {
                    this.startedTrackingPointerId = motionEvent.getPointerId(0);
                    this.maybeStartTracking = true;
                    this.startedTrackingX = (int) motionEvent.getX();
                    this.startedTrackingY = (int) motionEvent.getY();
                    this.velocityTracker.clear();
                } else if (motionEvent != null && motionEvent.getAction() == 2 && motionEvent.getPointerId(0) == this.startedTrackingPointerId) {
                    int x = (int) (motionEvent.getX() - this.startedTrackingX);
                    int abs = Math.abs(((int) motionEvent.getY()) - this.startedTrackingY);
                    if (this.startedTracking && ((PhotoPickerSearchActivity.this.animatingForward && x > 0) || (!PhotoPickerSearchActivity.this.animatingForward && x < 0))) {
                        if (!prepareForMoving(motionEvent, x < 0)) {
                            this.maybeStartTracking = true;
                            this.startedTracking = false;
                            PhotoPickerSearchActivity.this.viewPages[0].setTranslationX(0.0f);
                            PhotoPickerSearchActivity.this.viewPages[1].setTranslationX(PhotoPickerSearchActivity.this.animatingForward ? PhotoPickerSearchActivity.this.viewPages[0].getMeasuredWidth() : -PhotoPickerSearchActivity.this.viewPages[0].getMeasuredWidth());
                            PhotoPickerSearchActivity.this.scrollSlidingTextTabStrip.selectTabWithId(PhotoPickerSearchActivity.this.viewPages[1].selectedType, 0.0f);
                        }
                    }
                    if (!this.maybeStartTracking || this.startedTracking) {
                        if (this.startedTracking) {
                            PhotoPickerSearchActivity.this.viewPages[0].setTranslationX(x);
                            if (PhotoPickerSearchActivity.this.animatingForward) {
                                viewPage = PhotoPickerSearchActivity.this.viewPages[1];
                                measuredWidth2 = PhotoPickerSearchActivity.this.viewPages[0].getMeasuredWidth() + x;
                            } else {
                                viewPage = PhotoPickerSearchActivity.this.viewPages[1];
                                measuredWidth2 = x - PhotoPickerSearchActivity.this.viewPages[0].getMeasuredWidth();
                            }
                            viewPage.setTranslationX(measuredWidth2);
                            PhotoPickerSearchActivity.this.scrollSlidingTextTabStrip.selectTabWithId(PhotoPickerSearchActivity.this.viewPages[1].selectedType, Math.abs(x) / PhotoPickerSearchActivity.this.viewPages[0].getMeasuredWidth());
                        }
                    } else if (Math.abs(x) >= AndroidUtilities.getPixelsInCM(0.3f, true) && Math.abs(x) > abs) {
                        prepareForMoving(motionEvent, x < 0);
                    }
                } else if (motionEvent == null || (motionEvent.getPointerId(0) == this.startedTrackingPointerId && (motionEvent.getAction() == 3 || motionEvent.getAction() == 1 || motionEvent.getAction() == 6))) {
                    this.velocityTracker.computeCurrentVelocity(MediaDataController.MAX_STYLE_RUNS_COUNT, PhotoPickerSearchActivity.this.maximumVelocity);
                    if (motionEvent == null || motionEvent.getAction() == 3) {
                        f = 0.0f;
                        f2 = 0.0f;
                    } else {
                        f = this.velocityTracker.getXVelocity();
                        f2 = this.velocityTracker.getYVelocity();
                        if (!this.startedTracking && Math.abs(f) >= 3000.0f && Math.abs(f) > Math.abs(f2)) {
                            prepareForMoving(motionEvent, f < 0.0f);
                        }
                    }
                    if (this.startedTracking) {
                        float x2 = PhotoPickerSearchActivity.this.viewPages[0].getX();
                        PhotoPickerSearchActivity.this.tabsAnimation = new AnimatorSet();
                        PhotoPickerSearchActivity.this.backAnimation = Math.abs(x2) < ((float) PhotoPickerSearchActivity.this.viewPages[0].getMeasuredWidth()) / 3.0f && (Math.abs(f) < 3500.0f || Math.abs(f) < Math.abs(f2));
                        if (PhotoPickerSearchActivity.this.backAnimation) {
                            measuredWidth = Math.abs(x2);
                            if (PhotoPickerSearchActivity.this.animatingForward) {
                                AnimatorSet animatorSet = PhotoPickerSearchActivity.this.tabsAnimation;
                                ViewPage viewPage2 = PhotoPickerSearchActivity.this.viewPages[0];
                                Property property = View.TRANSLATION_X;
                                animatorSet.playTogether(ObjectAnimator.ofFloat(viewPage2, (Property<ViewPage, Float>) property, 0.0f), ObjectAnimator.ofFloat(PhotoPickerSearchActivity.this.viewPages[1], (Property<ViewPage, Float>) property, PhotoPickerSearchActivity.this.viewPages[1].getMeasuredWidth()));
                            } else {
                                AnimatorSet animatorSet2 = PhotoPickerSearchActivity.this.tabsAnimation;
                                ViewPage viewPage3 = PhotoPickerSearchActivity.this.viewPages[0];
                                Property property2 = View.TRANSLATION_X;
                                animatorSet2.playTogether(ObjectAnimator.ofFloat(viewPage3, (Property<ViewPage, Float>) property2, 0.0f), ObjectAnimator.ofFloat(PhotoPickerSearchActivity.this.viewPages[1], (Property<ViewPage, Float>) property2, -PhotoPickerSearchActivity.this.viewPages[1].getMeasuredWidth()));
                            }
                        } else {
                            measuredWidth = PhotoPickerSearchActivity.this.viewPages[0].getMeasuredWidth() - Math.abs(x2);
                            if (PhotoPickerSearchActivity.this.animatingForward) {
                                AnimatorSet animatorSet3 = PhotoPickerSearchActivity.this.tabsAnimation;
                                ViewPage viewPage4 = PhotoPickerSearchActivity.this.viewPages[0];
                                Property property3 = View.TRANSLATION_X;
                                animatorSet3.playTogether(ObjectAnimator.ofFloat(viewPage4, (Property<ViewPage, Float>) property3, -PhotoPickerSearchActivity.this.viewPages[0].getMeasuredWidth()), ObjectAnimator.ofFloat(PhotoPickerSearchActivity.this.viewPages[1], (Property<ViewPage, Float>) property3, 0.0f));
                            } else {
                                AnimatorSet animatorSet4 = PhotoPickerSearchActivity.this.tabsAnimation;
                                ViewPage viewPage5 = PhotoPickerSearchActivity.this.viewPages[0];
                                Property property4 = View.TRANSLATION_X;
                                animatorSet4.playTogether(ObjectAnimator.ofFloat(viewPage5, (Property<ViewPage, Float>) property4, PhotoPickerSearchActivity.this.viewPages[0].getMeasuredWidth()), ObjectAnimator.ofFloat(PhotoPickerSearchActivity.this.viewPages[1], (Property<ViewPage, Float>) property4, 0.0f));
                            }
                        }
                        PhotoPickerSearchActivity.this.tabsAnimation.setInterpolator(PhotoPickerSearchActivity.interpolator);
                        int measuredWidth3 = getMeasuredWidth();
                        float f3 = measuredWidth3 / 2;
                        float distanceInfluenceForSnapDuration = f3 + (AndroidUtilities.distanceInfluenceForSnapDuration(Math.min(1.0f, (measuredWidth * 1.0f) / measuredWidth3)) * f3);
                        PhotoPickerSearchActivity.this.tabsAnimation.setDuration(Math.max(150, Math.min(Math.abs(f) > 0.0f ? Math.round(Math.abs(distanceInfluenceForSnapDuration / r4) * 1000.0f) * 4 : (int) (((measuredWidth / getMeasuredWidth()) + 1.0f) * 100.0f), 600)));
                        PhotoPickerSearchActivity.this.tabsAnimation.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.PhotoPickerSearchActivity.4.1
                            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                            public void onAnimationEnd(Animator animator) {
                                PhotoPickerSearchActivity.this.tabsAnimation = null;
                                if (PhotoPickerSearchActivity.this.backAnimation) {
                                    PhotoPickerSearchActivity.this.viewPages[1].setVisibility(8);
                                } else {
                                    ViewPage viewPage6 = PhotoPickerSearchActivity.this.viewPages[0];
                                    PhotoPickerSearchActivity.this.viewPages[0] = PhotoPickerSearchActivity.this.viewPages[1];
                                    PhotoPickerSearchActivity.this.viewPages[1] = viewPage6;
                                    PhotoPickerSearchActivity.this.viewPages[1].setVisibility(8);
                                    PhotoPickerSearchActivity photoPickerSearchActivity = PhotoPickerSearchActivity.this;
                                    photoPickerSearchActivity.swipeBackEnabled = photoPickerSearchActivity.viewPages[0].selectedType == PhotoPickerSearchActivity.this.scrollSlidingTextTabStrip.getFirstTabId();
                                    PhotoPickerSearchActivity.this.scrollSlidingTextTabStrip.selectTabWithId(PhotoPickerSearchActivity.this.viewPages[0].selectedType, 1.0f);
                                }
                                PhotoPickerSearchActivity.this.tabsAnimationInProgress = false;
                                4.this.maybeStartTracking = false;
                                4.this.startedTracking = false;
                                ((BaseFragment) PhotoPickerSearchActivity.this).actionBar.setEnabled(true);
                                PhotoPickerSearchActivity.this.scrollSlidingTextTabStrip.setEnabled(true);
                            }
                        });
                        PhotoPickerSearchActivity.this.tabsAnimation.start();
                        PhotoPickerSearchActivity.this.tabsAnimationInProgress = true;
                        this.startedTracking = false;
                    } else {
                        this.maybeStartTracking = false;
                        ((BaseFragment) PhotoPickerSearchActivity.this).actionBar.setEnabled(true);
                        PhotoPickerSearchActivity.this.scrollSlidingTextTabStrip.setEnabled(true);
                    }
                    VelocityTracker velocityTracker = this.velocityTracker;
                    if (velocityTracker != null) {
                        velocityTracker.recycle();
                        this.velocityTracker = null;
                    }
                }
                return this.startedTracking;
            }

            @Override // android.view.View, android.view.ViewParent
            public void requestLayout() {
                if (this.globalIgnoreLayout) {
                    return;
                }
                super.requestLayout();
            }
        };
        this.fragmentView = sizeNotifierFrameLayout;
        sizeNotifierFrameLayout.setWillNotDraw(false);
        this.imagesSearch.setParentFragment(this);
        EditTextEmoji editTextEmoji = this.imagesSearch.commentTextView;
        this.commentTextView = editTextEmoji;
        editTextEmoji.setSizeNotifierLayout(sizeNotifierFrameLayout);
        int i4 = 0;
        while (i4 < 4) {
            View view = i4 != 0 ? i4 != 1 ? i4 != 2 ? this.imagesSearch.shadow : this.imagesSearch.selectedCountView : this.imagesSearch.writeButtonContainer : this.imagesSearch.frameLayout2;
            ((ViewGroup) view.getParent()).removeView(view);
            i4++;
        }
        PhotoPickerActivity photoPickerActivity = this.gifsSearch;
        PhotoPickerActivity photoPickerActivity2 = this.imagesSearch;
        photoPickerActivity.setLayoutViews(photoPickerActivity2.frameLayout2, photoPickerActivity2.writeButtonContainer, photoPickerActivity2.selectedCountView, photoPickerActivity2.shadow, photoPickerActivity2.commentTextView);
        this.gifsSearch.setParentFragment(this);
        int i5 = 0;
        while (true) {
            ViewPage[] viewPageArr = this.viewPages;
            if (i5 >= viewPageArr.length) {
                break;
            }
            viewPageArr[i5] = new ViewPage(context) { // from class: org.telegram.ui.PhotoPickerSearchActivity.5
                @Override // android.view.View
                public void setTranslationX(float f) {
                    super.setTranslationX(f);
                    if (PhotoPickerSearchActivity.this.tabsAnimationInProgress && PhotoPickerSearchActivity.this.viewPages[0] == this) {
                        PhotoPickerSearchActivity.this.scrollSlidingTextTabStrip.selectTabWithId(PhotoPickerSearchActivity.this.viewPages[1].selectedType, Math.abs(PhotoPickerSearchActivity.this.viewPages[0].getTranslationX()) / PhotoPickerSearchActivity.this.viewPages[0].getMeasuredWidth());
                    }
                }
            };
            sizeNotifierFrameLayout.addView(this.viewPages[i5], LayoutHelper.createFrame(-1, -1.0f));
            if (i5 == 0) {
                this.viewPages[i5].parentFragment = this.imagesSearch;
                this.viewPages[i5].listView = this.imagesSearch.getListView();
            } else if (i5 == 1) {
                this.viewPages[i5].parentFragment = this.gifsSearch;
                this.viewPages[i5].listView = this.gifsSearch.getListView();
                this.viewPages[i5].setVisibility(8);
            }
            this.viewPages[i5].listView.setScrollingTouchSlop(1);
            ViewPage viewPage = this.viewPages[i5];
            viewPage.fragmentView = (FrameLayout) viewPage.parentFragment.getFragmentView();
            this.viewPages[i5].listView.setClipToPadding(false);
            ViewPage viewPage2 = this.viewPages[i5];
            viewPage2.actionBar = viewPage2.parentFragment.getActionBar();
            ViewPage viewPage3 = this.viewPages[i5];
            viewPage3.addView(viewPage3.fragmentView, LayoutHelper.createFrame(-1, -1.0f));
            ViewPage viewPage4 = this.viewPages[i5];
            viewPage4.addView(viewPage4.actionBar, LayoutHelper.createFrame(-1, -2.0f));
            this.viewPages[i5].actionBar.setVisibility(8);
            final RecyclerView.OnScrollListener onScrollListener = this.viewPages[i5].listView.getOnScrollListener();
            this.viewPages[i5].listView.setOnScrollListener(new RecyclerView.OnScrollListener() { // from class: org.telegram.ui.PhotoPickerSearchActivity.6
                @Override // androidx.recyclerview.widget.RecyclerView.OnScrollListener
                public void onScrollStateChanged(RecyclerView recyclerView, int i6) {
                    onScrollListener.onScrollStateChanged(recyclerView, i6);
                    if (i6 != 1) {
                        int i7 = (int) (-((BaseFragment) PhotoPickerSearchActivity.this).actionBar.getTranslationY());
                        int currentActionBarHeight = ActionBar.getCurrentActionBarHeight();
                        if (i7 == 0 || i7 == currentActionBarHeight) {
                            return;
                        }
                        if (i7 < currentActionBarHeight / 2) {
                            PhotoPickerSearchActivity.this.viewPages[0].listView.smoothScrollBy(0, -i7);
                        } else {
                            PhotoPickerSearchActivity.this.viewPages[0].listView.smoothScrollBy(0, currentActionBarHeight - i7);
                        }
                    }
                }

                @Override // androidx.recyclerview.widget.RecyclerView.OnScrollListener
                public void onScrolled(RecyclerView recyclerView, int i6, int i7) {
                    onScrollListener.onScrolled(recyclerView, i6, i7);
                    if (recyclerView == PhotoPickerSearchActivity.this.viewPages[0].listView) {
                        float translationY = ((BaseFragment) PhotoPickerSearchActivity.this).actionBar.getTranslationY();
                        float f = translationY - i7;
                        if (f < (-ActionBar.getCurrentActionBarHeight())) {
                            f = -ActionBar.getCurrentActionBarHeight();
                        } else if (f > 0.0f) {
                            f = 0.0f;
                        }
                        if (f != translationY) {
                            PhotoPickerSearchActivity.this.setScrollY(f);
                        }
                    }
                }
            });
            i5++;
        }
        sizeNotifierFrameLayout.addView(this.actionBar, LayoutHelper.createFrame(-1, -2.0f));
        sizeNotifierFrameLayout.addView(this.imagesSearch.frameLayout2, LayoutHelper.createFrame(-1, 48, 83));
        sizeNotifierFrameLayout.addView(this.imagesSearch.writeButtonContainer, LayoutHelper.createFrame(60, 60.0f, 85, 0.0f, 0.0f, 12.0f, 10.0f));
        sizeNotifierFrameLayout.addView(this.imagesSearch.selectedCountView, LayoutHelper.createFrame(42, 24.0f, 85, 0.0f, 0.0f, -2.0f, 9.0f));
        updateTabs();
        switchToCurrentSelectedMode(false);
        this.swipeBackEnabled = this.scrollSlidingTextTabStrip.getCurrentTabId() == this.scrollSlidingTextTabStrip.getFirstTabId();
        int color = Theme.getColor(Theme.key_dialogBackground);
        if (Build.VERSION.SDK_INT >= 23 && AndroidUtilities.computePerceivedBrightness(color) >= 0.721f) {
            View view2 = this.fragmentView;
            view2.setSystemUiVisibility(view2.getSystemUiVisibility() | LiteMode.FLAG_ANIMATED_EMOJI_REACTIONS_NOT_PREMIUM);
        }
        return this.fragmentView;
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public ArrayList getThemeDescriptions() {
        ArrayList arrayList = new ArrayList();
        View view = this.fragmentView;
        int i = ThemeDescription.FLAG_BACKGROUND;
        int i2 = Theme.key_dialogBackground;
        arrayList.add(new ThemeDescription(view, i, null, null, null, null, i2));
        arrayList.add(new ThemeDescription(this.actionBar, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, i2));
        ActionBar actionBar = this.actionBar;
        int i3 = ThemeDescription.FLAG_AB_ITEMSCOLOR;
        int i4 = Theme.key_dialogTextBlack;
        arrayList.add(new ThemeDescription(actionBar, i3, null, null, null, null, i4));
        arrayList.add(new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_TITLECOLOR, null, null, null, null, i4));
        ActionBar actionBar2 = this.actionBar;
        int i5 = ThemeDescription.FLAG_AB_SELECTORCOLOR;
        int i6 = Theme.key_dialogButtonSelector;
        arrayList.add(new ThemeDescription(actionBar2, i5, null, null, null, null, i6));
        arrayList.add(new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_SEARCH, null, null, null, null, i4));
        arrayList.add(new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_SEARCHPLACEHOLDER, null, null, null, null, Theme.key_chat_messagePanelHint));
        arrayList.add(new ThemeDescription(this.searchItem.getSearchField(), ThemeDescription.FLAG_CURSORCOLOR, null, null, null, null, i4));
        int i7 = Theme.key_chat_attachActiveTab;
        arrayList.add(new ThemeDescription(this.scrollSlidingTextTabStrip.getTabsContainer(), ThemeDescription.FLAG_TEXTCOLOR | ThemeDescription.FLAG_CHECKTAG, new Class[]{TextView.class}, null, null, null, i7));
        arrayList.add(new ThemeDescription(this.scrollSlidingTextTabStrip.getTabsContainer(), ThemeDescription.FLAG_TEXTCOLOR | ThemeDescription.FLAG_CHECKTAG, new Class[]{TextView.class}, null, null, null, Theme.key_chat_attachUnactiveTab));
        arrayList.add(new ThemeDescription(this.scrollSlidingTextTabStrip.getTabsContainer(), ThemeDescription.FLAG_DRAWABLESELECTEDSTATE | ThemeDescription.FLAG_BACKGROUNDFILTER, new Class[]{TextView.class}, null, null, null, i6));
        arrayList.add(new ThemeDescription(null, 0, null, null, new Drawable[]{this.scrollSlidingTextTabStrip.getSelectorDrawable()}, null, i7));
        arrayList.addAll(this.imagesSearch.getThemeDescriptions());
        arrayList.addAll(this.gifsSearch.getThemeDescriptions());
        return arrayList;
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public boolean isSwipeBackEnabled(MotionEvent motionEvent) {
        return this.swipeBackEnabled;
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        PhotoPickerActivity photoPickerActivity = this.imagesSearch;
        if (photoPickerActivity != null) {
            photoPickerActivity.onConfigurationChanged(configuration);
        }
        PhotoPickerActivity photoPickerActivity2 = this.gifsSearch;
        if (photoPickerActivity2 != null) {
            photoPickerActivity2.onConfigurationChanged(configuration);
        }
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public void onFragmentDestroy() {
        PhotoPickerActivity photoPickerActivity = this.imagesSearch;
        if (photoPickerActivity != null) {
            photoPickerActivity.onFragmentDestroy();
        }
        PhotoPickerActivity photoPickerActivity2 = this.gifsSearch;
        if (photoPickerActivity2 != null) {
            photoPickerActivity2.onFragmentDestroy();
        }
        super.onFragmentDestroy();
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public void onPause() {
        super.onPause();
        PhotoPickerActivity photoPickerActivity = this.imagesSearch;
        if (photoPickerActivity != null) {
            photoPickerActivity.onPause();
        }
        PhotoPickerActivity photoPickerActivity2 = this.gifsSearch;
        if (photoPickerActivity2 != null) {
            photoPickerActivity2.onPause();
        }
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public void onResume() {
        super.onResume();
        ActionBarMenuItem actionBarMenuItem = this.searchItem;
        if (actionBarMenuItem != null) {
            actionBarMenuItem.openSearch(true);
            getParentActivity().getWindow().setSoftInputMode(32);
        }
        PhotoPickerActivity photoPickerActivity = this.imagesSearch;
        if (photoPickerActivity != null) {
            photoPickerActivity.onResume();
        }
        PhotoPickerActivity photoPickerActivity2 = this.gifsSearch;
        if (photoPickerActivity2 != null) {
            photoPickerActivity2.onResume();
        }
    }

    public void setCaption(CharSequence charSequence) {
        PhotoPickerActivity photoPickerActivity = this.imagesSearch;
        if (photoPickerActivity != null) {
            photoPickerActivity.setCaption(charSequence);
        }
    }

    public void setDelegate(PhotoPickerActivity.PhotoPickerActivityDelegate photoPickerActivityDelegate) {
        this.imagesSearch.setDelegate(photoPickerActivityDelegate);
        this.gifsSearch.setDelegate(photoPickerActivityDelegate);
        this.imagesSearch.setSearchDelegate(new PhotoPickerActivity.PhotoPickerActivitySearchDelegate() { // from class: org.telegram.ui.PhotoPickerSearchActivity.7
            @Override // org.telegram.ui.PhotoPickerActivity.PhotoPickerActivitySearchDelegate
            public void shouldClearRecentSearch() {
                PhotoPickerSearchActivity.this.imagesSearch.clearRecentSearch();
                PhotoPickerSearchActivity.this.gifsSearch.clearRecentSearch();
            }

            @Override // org.telegram.ui.PhotoPickerActivity.PhotoPickerActivitySearchDelegate
            public void shouldSearchText(String str) {
                PhotoPickerSearchActivity.this.searchText(str);
            }
        });
        this.gifsSearch.setSearchDelegate(new PhotoPickerActivity.PhotoPickerActivitySearchDelegate() { // from class: org.telegram.ui.PhotoPickerSearchActivity.8
            @Override // org.telegram.ui.PhotoPickerActivity.PhotoPickerActivitySearchDelegate
            public void shouldClearRecentSearch() {
                PhotoPickerSearchActivity.this.imagesSearch.clearRecentSearch();
                PhotoPickerSearchActivity.this.gifsSearch.clearRecentSearch();
            }

            @Override // org.telegram.ui.PhotoPickerActivity.PhotoPickerActivitySearchDelegate
            public void shouldSearchText(String str) {
                PhotoPickerSearchActivity.this.searchText(str);
            }
        });
    }

    public void setMaxSelectedPhotos(int i, boolean z) {
        this.imagesSearch.setMaxSelectedPhotos(i, z);
        this.gifsSearch.setMaxSelectedPhotos(i, z);
    }
}
