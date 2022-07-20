package org.telegram.ui.ActionBar;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.TimeInterpolator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.os.Build;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.transition.ChangeBounds;
import android.transition.Transition;
import android.transition.TransitionManager;
import android.transition.TransitionSet;
import android.transition.TransitionValues;
import android.transition.Visibility;
import android.view.ActionMode;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.accessibility.AccessibilityNodeInfo;
import android.view.animation.DecelerateInterpolator;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.ColorUtils;
import java.util.ArrayList;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.UserConfig;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC$Chat;
import org.telegram.tgnet.TLRPC$User;
import org.telegram.ui.ActionBar.ActionBarPopupWindow;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Adapters.FiltersView;
import org.telegram.ui.Components.BackupImageView;
import org.telegram.ui.Components.CloseProgressDrawable2;
import org.telegram.ui.Components.CombinedDrawable;
import org.telegram.ui.Components.CubicBezierInterpolator;
import org.telegram.ui.Components.EditTextBoldCursor;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.ui.Components.RLottieDrawable;
import org.telegram.ui.Components.RLottieImageView;
/* loaded from: classes3.dex */
public class ActionBarMenuItem extends FrameLayout {
    private int additionalXOffset;
    private int additionalYOffset;
    private boolean allowCloseAnimation;
    private boolean animateClear;
    private boolean animationEnabled;
    private ImageView clearButton;
    private ArrayList<FiltersView.MediaFilterData> currentSearchFilters;
    private ActionBarMenuItemDelegate delegate;
    private boolean forceSmoothKeyboard;
    protected RLottieImageView iconView;
    private boolean ignoreOnTextChange;
    private boolean isSearchField;
    private boolean layoutInScreen;
    protected ActionBarMenuItemSearchListener listener;
    private int[] location;
    private boolean longClickEnabled;
    private int notificationIndex;
    private View.OnClickListener onClickListener;
    protected boolean overrideMenuClick;
    private ActionBarMenu parentMenu;
    private ActionBarPopupWindow.ActionBarPopupWindowLayout popupLayout;
    private ActionBarPopupWindow popupWindow;
    private boolean processedPopupClick;
    private CloseProgressDrawable2 progressDrawable;
    private Rect rect;
    private final Theme.ResourcesProvider resourcesProvider;
    private FrameLayout searchContainer;
    AnimatorSet searchContainerAnimator;
    private EditTextBoldCursor searchField;
    private TextView searchFieldCaption;
    private LinearLayout searchFilterLayout;
    private int selectedFilterIndex;
    private View selectedMenuView;
    private Runnable showMenuRunnable;
    private View showSubMenuFrom;
    private boolean showSubmenuByMove;
    private ActionBarSubMenuItemDelegate subMenuDelegate;
    private int subMenuOpenSide;
    protected TextView textView;
    private float transitionOffset;
    private FrameLayout wrappedSearchFrameLayout;
    private int yOffset;

    /* loaded from: classes3.dex */
    public interface ActionBarMenuItemDelegate {
        void onItemClick(int i);
    }

    /* loaded from: classes3.dex */
    public static class ActionBarMenuItemSearchListener {
        public boolean canCollapseSearch() {
            return true;
        }

        public boolean canToggleSearch() {
            return true;
        }

        public boolean forceShowClear() {
            return false;
        }

        public Animator getCustomToggleTransition() {
            return null;
        }

        public void onCaptionCleared() {
        }

        public void onLayout(int i, int i2, int i3, int i4) {
        }

        public void onSearchCollapse() {
        }

        public void onSearchExpand() {
        }

        public void onSearchFilterCleared(FiltersView.MediaFilterData mediaFilterData) {
        }

        public void onSearchPressed(EditText editText) {
        }

        public void onTextChanged(EditText editText) {
        }
    }

    /* loaded from: classes3.dex */
    public interface ActionBarSubMenuItemDelegate {
        void onHideSubMenu();

        void onShowSubMenu();
    }

    protected void onDismiss() {
    }

    public ActionBarMenuItem(Context context, ActionBarMenu actionBarMenu, int i, int i2) {
        this(context, actionBarMenu, i, i2, false);
    }

    public ActionBarMenuItem(Context context, ActionBarMenu actionBarMenu, int i, int i2, Theme.ResourcesProvider resourcesProvider) {
        this(context, actionBarMenu, i, i2, false, resourcesProvider);
    }

    public ActionBarMenuItem(Context context, ActionBarMenu actionBarMenu, int i, int i2, boolean z) {
        this(context, actionBarMenu, i, i2, z, null);
    }

    public ActionBarMenuItem(Context context, ActionBarMenu actionBarMenu, int i, int i2, boolean z, Theme.ResourcesProvider resourcesProvider) {
        super(context);
        new ArrayList();
        this.allowCloseAnimation = true;
        this.animationEnabled = true;
        this.animateClear = true;
        this.showSubmenuByMove = true;
        this.currentSearchFilters = new ArrayList<>();
        this.selectedFilterIndex = -1;
        this.notificationIndex = -1;
        this.resourcesProvider = resourcesProvider;
        if (i != 0) {
            setBackgroundDrawable(Theme.createSelectorDrawable(i, z ? 5 : 1));
        }
        this.parentMenu = actionBarMenu;
        if (z) {
            TextView textView = new TextView(context);
            this.textView = textView;
            textView.setTextSize(1, 15.0f);
            this.textView.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
            this.textView.setGravity(17);
            this.textView.setPadding(AndroidUtilities.dp(4.0f), 0, AndroidUtilities.dp(4.0f), 0);
            this.textView.setImportantForAccessibility(2);
            if (i2 != 0) {
                this.textView.setTextColor(i2);
            }
            addView(this.textView, LayoutHelper.createFrame(-2, -1.0f));
            return;
        }
        RLottieImageView rLottieImageView = new RLottieImageView(context);
        this.iconView = rLottieImageView;
        rLottieImageView.setScaleType(ImageView.ScaleType.CENTER);
        this.iconView.setImportantForAccessibility(2);
        addView(this.iconView, LayoutHelper.createFrame(-1, -1.0f));
        if (i2 == 0) {
            return;
        }
        this.iconView.setColorFilter(new PorterDuffColorFilter(i2, PorterDuff.Mode.MULTIPLY));
    }

    @Override // android.view.View
    public void setTranslationX(float f) {
        super.setTranslationX(f + this.transitionOffset);
    }

    public void setLongClickEnabled(boolean z) {
        this.longClickEnabled = z;
    }

    @Override // android.view.View
    public boolean onTouchEvent(MotionEvent motionEvent) {
        ActionBarPopupWindow actionBarPopupWindow;
        ActionBarPopupWindow actionBarPopupWindow2;
        ActionBarPopupWindow actionBarPopupWindow3;
        if (motionEvent.getActionMasked() == 0) {
            if (this.longClickEnabled && hasSubMenu() && ((actionBarPopupWindow3 = this.popupWindow) == null || !actionBarPopupWindow3.isShowing())) {
                ActionBarMenuItem$$ExternalSyntheticLambda11 actionBarMenuItem$$ExternalSyntheticLambda11 = new ActionBarMenuItem$$ExternalSyntheticLambda11(this);
                this.showMenuRunnable = actionBarMenuItem$$ExternalSyntheticLambda11;
                AndroidUtilities.runOnUIThread(actionBarMenuItem$$ExternalSyntheticLambda11, 200L);
            }
        } else if (motionEvent.getActionMasked() == 2) {
            if (this.showSubmenuByMove && hasSubMenu() && ((actionBarPopupWindow2 = this.popupWindow) == null || !actionBarPopupWindow2.isShowing())) {
                if (motionEvent.getY() > getHeight()) {
                    if (getParent() != null) {
                        getParent().requestDisallowInterceptTouchEvent(true);
                    }
                    toggleSubMenu();
                    return true;
                }
            } else if (this.showSubmenuByMove && (actionBarPopupWindow = this.popupWindow) != null && actionBarPopupWindow.isShowing()) {
                getLocationOnScreen(this.location);
                float x = motionEvent.getX() + this.location[0];
                float y = motionEvent.getY();
                int[] iArr = this.location;
                float f = y + iArr[1];
                this.popupLayout.getLocationOnScreen(iArr);
                int[] iArr2 = this.location;
                float f2 = x - iArr2[0];
                float f3 = f - iArr2[1];
                this.selectedMenuView = null;
                for (int i = 0; i < this.popupLayout.getItemsCount(); i++) {
                    View itemAt = this.popupLayout.getItemAt(i);
                    itemAt.getHitRect(this.rect);
                    Object tag = itemAt.getTag();
                    if ((tag instanceof Integer) && ((Integer) tag).intValue() < 100) {
                        if (!this.rect.contains((int) f2, (int) f3)) {
                            itemAt.setPressed(false);
                            itemAt.setSelected(false);
                            if (Build.VERSION.SDK_INT == 21 && itemAt.getBackground() != null) {
                                itemAt.getBackground().setVisible(false, false);
                            }
                        } else {
                            itemAt.setPressed(true);
                            itemAt.setSelected(true);
                            int i2 = Build.VERSION.SDK_INT;
                            if (i2 >= 21) {
                                if (i2 == 21 && itemAt.getBackground() != null) {
                                    itemAt.getBackground().setVisible(true, false);
                                }
                                itemAt.drawableHotspotChanged(f2, f3 - itemAt.getTop());
                            }
                            this.selectedMenuView = itemAt;
                        }
                    }
                }
            }
        } else {
            ActionBarPopupWindow actionBarPopupWindow4 = this.popupWindow;
            if (actionBarPopupWindow4 != null && actionBarPopupWindow4.isShowing() && motionEvent.getActionMasked() == 1) {
                View view = this.selectedMenuView;
                if (view != null) {
                    view.setSelected(false);
                    ActionBarMenu actionBarMenu = this.parentMenu;
                    if (actionBarMenu != null) {
                        actionBarMenu.onItemClick(((Integer) this.selectedMenuView.getTag()).intValue());
                    } else {
                        ActionBarMenuItemDelegate actionBarMenuItemDelegate = this.delegate;
                        if (actionBarMenuItemDelegate != null) {
                            actionBarMenuItemDelegate.onItemClick(((Integer) this.selectedMenuView.getTag()).intValue());
                        }
                    }
                    this.popupWindow.dismiss(this.allowCloseAnimation);
                } else if (this.showSubmenuByMove) {
                    this.popupWindow.dismiss();
                }
            } else {
                View view2 = this.selectedMenuView;
                if (view2 != null) {
                    view2.setSelected(false);
                    this.selectedMenuView = null;
                }
            }
        }
        return super.onTouchEvent(motionEvent);
    }

    public /* synthetic */ void lambda$onTouchEvent$0() {
        if (getParent() != null) {
            getParent().requestDisallowInterceptTouchEvent(true);
        }
        toggleSubMenu();
    }

    public void setDelegate(ActionBarMenuItemDelegate actionBarMenuItemDelegate) {
        this.delegate = actionBarMenuItemDelegate;
    }

    public void setSubMenuDelegate(ActionBarSubMenuItemDelegate actionBarSubMenuItemDelegate) {
        this.subMenuDelegate = actionBarSubMenuItemDelegate;
    }

    public void setShowSubmenuByMove(boolean z) {
        this.showSubmenuByMove = z;
    }

    public void setIconColor(int i) {
        RLottieImageView rLottieImageView = this.iconView;
        if (rLottieImageView != null) {
            rLottieImageView.setColorFilter(new PorterDuffColorFilter(i, PorterDuff.Mode.MULTIPLY));
        }
        TextView textView = this.textView;
        if (textView != null) {
            textView.setTextColor(i);
        }
        ImageView imageView = this.clearButton;
        if (imageView != null) {
            imageView.setColorFilter(new PorterDuffColorFilter(i, PorterDuff.Mode.MULTIPLY));
        }
    }

    public void setSubMenuOpenSide(int i) {
        this.subMenuOpenSide = i;
    }

    public void setLayoutInScreen(boolean z) {
        this.layoutInScreen = z;
    }

    public void setForceSmoothKeyboard(boolean z) {
        this.forceSmoothKeyboard = z;
    }

    private void createPopupLayout() {
        if (this.popupLayout != null) {
            return;
        }
        this.rect = new Rect();
        this.location = new int[2];
        ActionBarPopupWindow.ActionBarPopupWindowLayout actionBarPopupWindowLayout = new ActionBarPopupWindow.ActionBarPopupWindowLayout(getContext(), 2131166086, this.resourcesProvider, 1);
        this.popupLayout = actionBarPopupWindowLayout;
        actionBarPopupWindowLayout.setOnTouchListener(new ActionBarMenuItem$$ExternalSyntheticLambda8(this));
        this.popupLayout.setDispatchKeyEventListener(new ActionBarMenuItem$$ExternalSyntheticLambda14(this));
        if (this.popupLayout.getSwipeBack() == null) {
            return;
        }
        this.popupLayout.getSwipeBack().setOnClickListener(new ActionBarMenuItem$$ExternalSyntheticLambda1(this));
    }

    public /* synthetic */ boolean lambda$createPopupLayout$1(View view, MotionEvent motionEvent) {
        ActionBarPopupWindow actionBarPopupWindow;
        if (motionEvent.getActionMasked() != 0 || (actionBarPopupWindow = this.popupWindow) == null || !actionBarPopupWindow.isShowing()) {
            return false;
        }
        view.getHitRect(this.rect);
        if (this.rect.contains((int) motionEvent.getX(), (int) motionEvent.getY())) {
            return false;
        }
        this.popupWindow.dismiss();
        return false;
    }

    public /* synthetic */ void lambda$createPopupLayout$2(KeyEvent keyEvent) {
        ActionBarPopupWindow actionBarPopupWindow;
        if (keyEvent.getKeyCode() != 4 || keyEvent.getRepeatCount() != 0 || (actionBarPopupWindow = this.popupWindow) == null || !actionBarPopupWindow.isShowing()) {
            return;
        }
        this.popupWindow.dismiss();
    }

    public /* synthetic */ void lambda$createPopupLayout$3(View view) {
        ActionBarPopupWindow actionBarPopupWindow = this.popupWindow;
        if (actionBarPopupWindow != null) {
            actionBarPopupWindow.dismiss();
        }
    }

    public void removeAllSubItems() {
        ActionBarPopupWindow.ActionBarPopupWindowLayout actionBarPopupWindowLayout = this.popupLayout;
        if (actionBarPopupWindowLayout == null) {
            return;
        }
        actionBarPopupWindowLayout.removeInnerViews();
    }

    public void setShowedFromBottom(boolean z) {
        ActionBarPopupWindow.ActionBarPopupWindowLayout actionBarPopupWindowLayout = this.popupLayout;
        if (actionBarPopupWindowLayout == null) {
            return;
        }
        actionBarPopupWindowLayout.setShownFromBottom(z);
    }

    public void addSubItem(int i, View view, int i2, int i3) {
        createPopupLayout();
        view.setLayoutParams(new LinearLayout.LayoutParams(i2, i3));
        this.popupLayout.addView(view);
        view.setTag(Integer.valueOf(i));
        view.setOnClickListener(new ActionBarMenuItem$$ExternalSyntheticLambda3(this));
        view.setBackgroundDrawable(Theme.getSelectorDrawable(false));
    }

    public /* synthetic */ void lambda$addSubItem$4(View view) {
        ActionBarPopupWindow actionBarPopupWindow = this.popupWindow;
        if (actionBarPopupWindow != null && actionBarPopupWindow.isShowing()) {
            if (this.processedPopupClick) {
                return;
            }
            this.processedPopupClick = true;
            this.popupWindow.dismiss(this.allowCloseAnimation);
        }
        ActionBarMenu actionBarMenu = this.parentMenu;
        if (actionBarMenu != null) {
            actionBarMenu.onItemClick(((Integer) view.getTag()).intValue());
            return;
        }
        ActionBarMenuItemDelegate actionBarMenuItemDelegate = this.delegate;
        if (actionBarMenuItemDelegate == null) {
            return;
        }
        actionBarMenuItemDelegate.onItemClick(((Integer) view.getTag()).intValue());
    }

    public TextView addSubItem(int i, CharSequence charSequence) {
        createPopupLayout();
        TextView textView = new TextView(getContext());
        textView.setTextColor(getThemedColor("actionBarDefaultSubmenuItem"));
        textView.setBackgroundDrawable(Theme.getSelectorDrawable(false));
        if (!LocaleController.isRTL) {
            textView.setGravity(16);
        } else {
            textView.setGravity(21);
        }
        textView.setPadding(AndroidUtilities.dp(16.0f), 0, AndroidUtilities.dp(16.0f), 0);
        textView.setTextSize(1, 16.0f);
        textView.setMinWidth(AndroidUtilities.dp(196.0f));
        textView.setSingleLine(true);
        textView.setEllipsize(TextUtils.TruncateAt.END);
        textView.setTag(Integer.valueOf(i));
        textView.setText(charSequence);
        this.popupLayout.addView(textView);
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) textView.getLayoutParams();
        if (LocaleController.isRTL) {
            layoutParams.gravity = 5;
        }
        layoutParams.width = -1;
        layoutParams.height = AndroidUtilities.dp(48.0f);
        textView.setLayoutParams(layoutParams);
        textView.setOnClickListener(new ActionBarMenuItem$$ExternalSyntheticLambda0(this));
        return textView;
    }

    public /* synthetic */ void lambda$addSubItem$5(View view) {
        ActionBarPopupWindow actionBarPopupWindow = this.popupWindow;
        if (actionBarPopupWindow != null && actionBarPopupWindow.isShowing()) {
            if (this.processedPopupClick) {
                return;
            }
            this.processedPopupClick = true;
            if (!this.allowCloseAnimation) {
                this.popupWindow.setAnimationStyle(2131689479);
            }
            this.popupWindow.dismiss(this.allowCloseAnimation);
        }
        ActionBarMenu actionBarMenu = this.parentMenu;
        if (actionBarMenu != null) {
            actionBarMenu.onItemClick(((Integer) view.getTag()).intValue());
            return;
        }
        ActionBarMenuItemDelegate actionBarMenuItemDelegate = this.delegate;
        if (actionBarMenuItemDelegate == null) {
            return;
        }
        actionBarMenuItemDelegate.onItemClick(((Integer) view.getTag()).intValue());
    }

    public ActionBarMenuSubItem addSubItem(int i, int i2, CharSequence charSequence) {
        return addSubItem(i, i2, null, charSequence, true, false);
    }

    public ActionBarMenuSubItem addSubItem(int i, int i2, CharSequence charSequence, Theme.ResourcesProvider resourcesProvider) {
        return addSubItem(i, i2, null, charSequence, true, false, resourcesProvider);
    }

    public ActionBarMenuSubItem addSubItem(int i, int i2, CharSequence charSequence, boolean z) {
        return addSubItem(i, i2, null, charSequence, true, z);
    }

    public View addGap(int i) {
        createPopupLayout();
        View view = new View(getContext());
        view.setMinimumWidth(AndroidUtilities.dp(196.0f));
        view.setTag(Integer.valueOf(i));
        view.setTag(2131230875, 1);
        this.popupLayout.addView(view);
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) view.getLayoutParams();
        if (LocaleController.isRTL) {
            layoutParams.gravity = 5;
        }
        layoutParams.width = -1;
        layoutParams.height = AndroidUtilities.dp(6.0f);
        view.setLayoutParams(layoutParams);
        return view;
    }

    public ActionBarMenuSubItem addSubItem(int i, int i2, Drawable drawable, CharSequence charSequence, boolean z, boolean z2) {
        return addSubItem(i, i2, drawable, charSequence, z, z2, null);
    }

    public ActionBarMenuSubItem addSubItem(int i, int i2, Drawable drawable, CharSequence charSequence, boolean z, boolean z2, Theme.ResourcesProvider resourcesProvider) {
        createPopupLayout();
        ActionBarMenuSubItem actionBarMenuSubItem = new ActionBarMenuSubItem(getContext(), z2, false, false, resourcesProvider);
        actionBarMenuSubItem.setTextAndIcon(charSequence, i2, drawable);
        actionBarMenuSubItem.setMinimumWidth(AndroidUtilities.dp(196.0f));
        actionBarMenuSubItem.setTag(Integer.valueOf(i));
        this.popupLayout.addView(actionBarMenuSubItem);
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) actionBarMenuSubItem.getLayoutParams();
        if (LocaleController.isRTL) {
            layoutParams.gravity = 5;
        }
        layoutParams.width = -1;
        layoutParams.height = AndroidUtilities.dp(48.0f);
        actionBarMenuSubItem.setLayoutParams(layoutParams);
        actionBarMenuSubItem.setOnClickListener(new ActionBarMenuItem$$ExternalSyntheticLambda5(this, z));
        return actionBarMenuSubItem;
    }

    public /* synthetic */ void lambda$addSubItem$6(boolean z, View view) {
        ActionBarPopupWindow actionBarPopupWindow = this.popupWindow;
        if (actionBarPopupWindow != null && actionBarPopupWindow.isShowing() && z) {
            if (this.processedPopupClick) {
                return;
            }
            this.processedPopupClick = true;
            this.popupWindow.dismiss(this.allowCloseAnimation);
        }
        ActionBarMenu actionBarMenu = this.parentMenu;
        if (actionBarMenu != null) {
            actionBarMenu.onItemClick(((Integer) view.getTag()).intValue());
            return;
        }
        ActionBarMenuItemDelegate actionBarMenuItemDelegate = this.delegate;
        if (actionBarMenuItemDelegate == null) {
            return;
        }
        actionBarMenuItemDelegate.onItemClick(((Integer) view.getTag()).intValue());
    }

    public ActionBarMenuSubItem addSwipeBackItem(int i, Drawable drawable, String str, View view) {
        createPopupLayout();
        ActionBarMenuSubItem actionBarMenuSubItem = new ActionBarMenuSubItem(getContext(), false, false, false, this.resourcesProvider);
        actionBarMenuSubItem.setTextAndIcon(str, i, drawable);
        actionBarMenuSubItem.setMinimumWidth(AndroidUtilities.dp(196.0f));
        actionBarMenuSubItem.setRightIcon(2131165641);
        this.popupLayout.addView(actionBarMenuSubItem);
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) actionBarMenuSubItem.getLayoutParams();
        if (LocaleController.isRTL) {
            layoutParams.gravity = 5;
        }
        layoutParams.width = -1;
        layoutParams.height = AndroidUtilities.dp(48.0f);
        actionBarMenuSubItem.setLayoutParams(layoutParams);
        actionBarMenuSubItem.openSwipeBackLayout = new ActionBarMenuItem$$ExternalSyntheticLambda13(this, this.popupLayout.addViewToSwipeBack(view));
        actionBarMenuSubItem.setOnClickListener(new ActionBarMenuItem$$ExternalSyntheticLambda6(actionBarMenuSubItem));
        this.popupLayout.swipeBackGravityRight = true;
        return actionBarMenuSubItem;
    }

    public /* synthetic */ void lambda$addSwipeBackItem$7(int i) {
        if (this.popupLayout.getSwipeBack() != null) {
            this.popupLayout.getSwipeBack().openForeground(i);
        }
    }

    public View addDivider(int i) {
        createPopupLayout();
        TextView textView = new TextView(getContext());
        textView.setBackgroundColor(i);
        textView.setMinimumWidth(AndroidUtilities.dp(196.0f));
        this.popupLayout.addView(textView);
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) textView.getLayoutParams();
        layoutParams.width = -1;
        layoutParams.height = 1;
        int dp = AndroidUtilities.dp(3.0f);
        layoutParams.bottomMargin = dp;
        layoutParams.topMargin = dp;
        textView.setLayoutParams(layoutParams);
        return textView;
    }

    public void redrawPopup(int i) {
        ActionBarPopupWindow.ActionBarPopupWindowLayout actionBarPopupWindowLayout = this.popupLayout;
        if (actionBarPopupWindowLayout == null || actionBarPopupWindowLayout.getBackgroundColor() == i) {
            return;
        }
        this.popupLayout.setBackgroundColor(i);
        ActionBarPopupWindow actionBarPopupWindow = this.popupWindow;
        if (actionBarPopupWindow == null || !actionBarPopupWindow.isShowing()) {
            return;
        }
        this.popupLayout.invalidate();
    }

    public void setPopupItemsColor(int i, boolean z) {
        ActionBarPopupWindow.ActionBarPopupWindowLayout actionBarPopupWindowLayout = this.popupLayout;
        if (actionBarPopupWindowLayout == null) {
            return;
        }
        LinearLayout linearLayout = actionBarPopupWindowLayout.linearLayout;
        int childCount = linearLayout.getChildCount();
        for (int i2 = 0; i2 < childCount; i2++) {
            View childAt = linearLayout.getChildAt(i2);
            if (childAt instanceof TextView) {
                ((TextView) childAt).setTextColor(i);
            } else if (childAt instanceof ActionBarMenuSubItem) {
                if (z) {
                    ((ActionBarMenuSubItem) childAt).setIconColor(i);
                } else {
                    ((ActionBarMenuSubItem) childAt).setTextColor(i);
                }
            }
        }
    }

    public void setPopupItemsSelectorColor(int i) {
        ActionBarPopupWindow.ActionBarPopupWindowLayout actionBarPopupWindowLayout = this.popupLayout;
        if (actionBarPopupWindowLayout == null) {
            return;
        }
        LinearLayout linearLayout = actionBarPopupWindowLayout.linearLayout;
        int childCount = linearLayout.getChildCount();
        for (int i2 = 0; i2 < childCount; i2++) {
            View childAt = linearLayout.getChildAt(i2);
            if (childAt instanceof ActionBarMenuSubItem) {
                ((ActionBarMenuSubItem) childAt).setSelectorColor(i);
            }
        }
    }

    public void setupPopupRadialSelectors(int i) {
        ActionBarPopupWindow.ActionBarPopupWindowLayout actionBarPopupWindowLayout = this.popupLayout;
        if (actionBarPopupWindowLayout != null) {
            actionBarPopupWindowLayout.setupRadialSelectors(i);
        }
    }

    public boolean hasSubMenu() {
        return this.popupLayout != null;
    }

    public ActionBarPopupWindow.ActionBarPopupWindowLayout getPopupLayout() {
        if (this.popupLayout == null) {
            createPopupLayout();
        }
        return this.popupLayout;
    }

    public void setMenuYOffset(int i) {
        this.yOffset = i;
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r12v6, types: [android.widget.LinearLayout, org.telegram.ui.ActionBar.ActionBarMenuItem$1] */
    public void toggleSubMenu(View view, View view2) {
        ActionBar actionBar;
        if (this.popupLayout != null) {
            ActionBarMenu actionBarMenu = this.parentMenu;
            if (actionBarMenu != null && actionBarMenu.isActionMode && (actionBar = actionBarMenu.parentActionBar) != null && !actionBar.isActionModeShowed()) {
                return;
            }
            Runnable runnable = this.showMenuRunnable;
            if (runnable != null) {
                AndroidUtilities.cancelRunOnUIThread(runnable);
                this.showMenuRunnable = null;
            }
            ActionBarPopupWindow actionBarPopupWindow = this.popupWindow;
            if (actionBarPopupWindow != null && actionBarPopupWindow.isShowing()) {
                this.popupWindow.dismiss();
                return;
            }
            this.showSubMenuFrom = view2;
            ActionBarSubMenuItemDelegate actionBarSubMenuItemDelegate = this.subMenuDelegate;
            if (actionBarSubMenuItemDelegate != null) {
                actionBarSubMenuItemDelegate.onShowSubMenu();
            }
            if (this.popupLayout.getParent() != null) {
                ((ViewGroup) this.popupLayout.getParent()).removeView(this.popupLayout);
            }
            ActionBarPopupWindow.ActionBarPopupWindowLayout actionBarPopupWindowLayout = this.popupLayout;
            if (view != null) {
                ?? anonymousClass1 = new AnonymousClass1(getContext(), view);
                anonymousClass1.setOrientation(1);
                FrameLayout frameLayout = new FrameLayout(getContext());
                frameLayout.setAlpha(0.0f);
                frameLayout.animate().alpha(1.0f).setDuration(100L).start();
                Drawable mutate = ContextCompat.getDrawable(getContext(), 2131166086).mutate();
                mutate.setColorFilter(new PorterDuffColorFilter(this.popupLayout.getBackgroundColor(), PorterDuff.Mode.MULTIPLY));
                frameLayout.setBackground(mutate);
                frameLayout.addView(view);
                anonymousClass1.addView(frameLayout, LayoutHelper.createLinear(-2, -2));
                anonymousClass1.addView(this.popupLayout, LayoutHelper.createLinear(-2, -2, 0, 0, -AndroidUtilities.dp(4.0f), 0, 0));
                this.popupLayout.setTopView(frameLayout);
                actionBarPopupWindowLayout = anonymousClass1;
            }
            ActionBarPopupWindow actionBarPopupWindow2 = new ActionBarPopupWindow(actionBarPopupWindowLayout, -2, -2);
            this.popupWindow = actionBarPopupWindow2;
            if (this.animationEnabled && Build.VERSION.SDK_INT >= 19) {
                actionBarPopupWindow2.setAnimationStyle(0);
            } else {
                actionBarPopupWindow2.setAnimationStyle(2131689479);
            }
            boolean z = this.animationEnabled;
            if (!z) {
                this.popupWindow.setAnimationEnabled(z);
            }
            this.popupWindow.setOutsideTouchable(true);
            this.popupWindow.setClippingEnabled(true);
            if (this.layoutInScreen) {
                this.popupWindow.setLayoutInScreen(true);
            }
            this.popupWindow.setInputMethodMode(2);
            this.popupWindow.setSoftInputMode(0);
            actionBarPopupWindowLayout.setFocusableInTouchMode(true);
            actionBarPopupWindowLayout.setOnKeyListener(new ActionBarMenuItem$$ExternalSyntheticLambda7(this));
            this.popupWindow.setOnDismissListener(new ActionBarMenuItem$$ExternalSyntheticLambda9(this));
            actionBarPopupWindowLayout.measure(View.MeasureSpec.makeMeasureSpec(AndroidUtilities.displaySize.x - AndroidUtilities.dp(40.0f), Integer.MIN_VALUE), View.MeasureSpec.makeMeasureSpec(AndroidUtilities.displaySize.y, Integer.MIN_VALUE));
            this.processedPopupClick = false;
            this.popupWindow.setFocusable(true);
            if (actionBarPopupWindowLayout.getMeasuredWidth() == 0) {
                updateOrShowPopup(true, true);
            } else {
                updateOrShowPopup(true, false);
            }
            this.popupLayout.updateRadialSelectors();
            if (this.popupLayout.getSwipeBack() != null) {
                this.popupLayout.getSwipeBack().closeForeground(false);
            }
            this.popupWindow.startAnimation();
        }
    }

    /* renamed from: org.telegram.ui.ActionBar.ActionBarMenuItem$1 */
    /* loaded from: classes3.dex */
    public class AnonymousClass1 extends LinearLayout {
        final /* synthetic */ View val$topView;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass1(Context context, View view) {
            super(context);
            ActionBarMenuItem.this = r1;
            this.val$topView = view;
        }

        @Override // android.widget.LinearLayout, android.view.View
        protected void onMeasure(int i, int i2) {
            ActionBarMenuItem.this.popupLayout.measure(i, i2);
            if (ActionBarMenuItem.this.popupLayout.getSwipeBack() != null) {
                this.val$topView.getLayoutParams().width = ActionBarMenuItem.this.popupLayout.getSwipeBack().getChildAt(0).getMeasuredWidth();
            } else {
                this.val$topView.getLayoutParams().width = ActionBarMenuItem.this.popupLayout.getMeasuredWidth() - AndroidUtilities.dp(16.0f);
            }
            super.onMeasure(i, i2);
        }
    }

    public /* synthetic */ boolean lambda$toggleSubMenu$9(View view, int i, KeyEvent keyEvent) {
        ActionBarPopupWindow actionBarPopupWindow;
        if (i == 82 && keyEvent.getRepeatCount() == 0 && keyEvent.getAction() == 1 && (actionBarPopupWindow = this.popupWindow) != null && actionBarPopupWindow.isShowing()) {
            this.popupWindow.dismiss();
            return true;
        }
        return false;
    }

    public /* synthetic */ void lambda$toggleSubMenu$10() {
        onDismiss();
        ActionBarSubMenuItemDelegate actionBarSubMenuItemDelegate = this.subMenuDelegate;
        if (actionBarSubMenuItemDelegate != null) {
            actionBarSubMenuItemDelegate.onHideSubMenu();
        }
    }

    public void toggleSubMenu() {
        toggleSubMenu(null, null);
    }

    public void openSearch(boolean z) {
        ActionBarMenu actionBarMenu;
        FrameLayout frameLayout = this.searchContainer;
        if (frameLayout == null || frameLayout.getVisibility() == 0 || (actionBarMenu = this.parentMenu) == null) {
            return;
        }
        actionBarMenu.parentActionBar.onSearchFieldVisibilityChanged(toggleSearch(z));
    }

    public boolean isSearchFieldVisible() {
        return this.searchContainer.getVisibility() == 0;
    }

    public boolean toggleSearch(boolean z) {
        ActionBarMenuItemSearchListener actionBarMenuItemSearchListener;
        RLottieImageView iconView;
        Animator customToggleTransition;
        if (this.searchContainer == null || ((actionBarMenuItemSearchListener = this.listener) != null && !actionBarMenuItemSearchListener.canToggleSearch())) {
            return false;
        }
        ActionBarMenuItemSearchListener actionBarMenuItemSearchListener2 = this.listener;
        if (actionBarMenuItemSearchListener2 != null && (customToggleTransition = actionBarMenuItemSearchListener2.getCustomToggleTransition()) != null) {
            customToggleTransition.start();
            return true;
        }
        ArrayList arrayList = new ArrayList();
        for (int i = 0; i < this.parentMenu.getChildCount(); i++) {
            View childAt = this.parentMenu.getChildAt(i);
            if ((childAt instanceof ActionBarMenuItem) && (iconView = ((ActionBarMenuItem) childAt).getIconView()) != null) {
                arrayList.add(iconView);
            }
        }
        if (this.searchContainer.getTag() != null) {
            this.searchContainer.setTag(null);
            AnimatorSet animatorSet = this.searchContainerAnimator;
            if (animatorSet != null) {
                animatorSet.removeAllListeners();
                this.searchContainerAnimator.cancel();
            }
            AnimatorSet animatorSet2 = new AnimatorSet();
            this.searchContainerAnimator = animatorSet2;
            FrameLayout frameLayout = this.searchContainer;
            animatorSet2.playTogether(ObjectAnimator.ofFloat(frameLayout, View.ALPHA, frameLayout.getAlpha(), 0.0f));
            for (int i2 = 0; i2 < arrayList.size(); i2++) {
                ((View) arrayList.get(i2)).setAlpha(0.0f);
                this.searchContainerAnimator.playTogether(ObjectAnimator.ofFloat((View) arrayList.get(i2), View.ALPHA, ((View) arrayList.get(i2)).getAlpha(), 1.0f));
            }
            this.searchContainerAnimator.setDuration(150L);
            this.searchContainerAnimator.addListener(new AnonymousClass2(arrayList));
            this.searchContainerAnimator.start();
            this.searchField.clearFocus();
            setVisibility(0);
            if (!this.currentSearchFilters.isEmpty() && this.listener != null) {
                for (int i3 = 0; i3 < this.currentSearchFilters.size(); i3++) {
                    if (this.currentSearchFilters.get(i3).removable) {
                        this.listener.onSearchFilterCleared(this.currentSearchFilters.get(i3));
                    }
                }
            }
            ActionBarMenuItemSearchListener actionBarMenuItemSearchListener3 = this.listener;
            if (actionBarMenuItemSearchListener3 != null) {
                actionBarMenuItemSearchListener3.onSearchCollapse();
            }
            if (z) {
                AndroidUtilities.hideKeyboard(this.searchField);
            }
            this.parentMenu.requestLayout();
            requestLayout();
            return false;
        }
        this.searchContainer.setVisibility(0);
        this.searchContainer.setAlpha(0.0f);
        AnimatorSet animatorSet3 = this.searchContainerAnimator;
        if (animatorSet3 != null) {
            animatorSet3.removeAllListeners();
            this.searchContainerAnimator.cancel();
        }
        AnimatorSet animatorSet4 = new AnimatorSet();
        this.searchContainerAnimator = animatorSet4;
        FrameLayout frameLayout2 = this.searchContainer;
        animatorSet4.playTogether(ObjectAnimator.ofFloat(frameLayout2, View.ALPHA, frameLayout2.getAlpha(), 1.0f));
        for (int i4 = 0; i4 < arrayList.size(); i4++) {
            this.searchContainerAnimator.playTogether(ObjectAnimator.ofFloat((View) arrayList.get(i4), View.ALPHA, ((View) arrayList.get(i4)).getAlpha(), 0.0f));
        }
        this.searchContainerAnimator.setDuration(150L);
        this.searchContainerAnimator.addListener(new AnonymousClass3(arrayList));
        this.searchContainerAnimator.start();
        setVisibility(8);
        clearSearchFilters();
        this.searchField.setText("");
        this.searchField.requestFocus();
        if (z) {
            AndroidUtilities.showKeyboard(this.searchField);
        }
        ActionBarMenuItemSearchListener actionBarMenuItemSearchListener4 = this.listener;
        if (actionBarMenuItemSearchListener4 != null) {
            actionBarMenuItemSearchListener4.onSearchExpand();
        }
        this.searchContainer.setTag(1);
        return true;
    }

    /* renamed from: org.telegram.ui.ActionBar.ActionBarMenuItem$2 */
    /* loaded from: classes3.dex */
    public class AnonymousClass2 extends AnimatorListenerAdapter {
        final /* synthetic */ ArrayList val$menuIcons;

        AnonymousClass2(ArrayList arrayList) {
            ActionBarMenuItem.this = r1;
            this.val$menuIcons = arrayList;
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator) {
            ActionBarMenuItem.this.searchContainer.setAlpha(0.0f);
            for (int i = 0; i < this.val$menuIcons.size(); i++) {
                ((View) this.val$menuIcons.get(i)).setAlpha(1.0f);
            }
            ActionBarMenuItem.this.searchContainer.setVisibility(8);
        }
    }

    /* renamed from: org.telegram.ui.ActionBar.ActionBarMenuItem$3 */
    /* loaded from: classes3.dex */
    public class AnonymousClass3 extends AnimatorListenerAdapter {
        final /* synthetic */ ArrayList val$menuIcons;

        AnonymousClass3(ArrayList arrayList) {
            ActionBarMenuItem.this = r1;
            this.val$menuIcons = arrayList;
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator) {
            ActionBarMenuItem.this.searchContainer.setAlpha(1.0f);
            for (int i = 0; i < this.val$menuIcons.size(); i++) {
                ((View) this.val$menuIcons.get(i)).setAlpha(0.0f);
            }
        }
    }

    public void removeSearchFilter(FiltersView.MediaFilterData mediaFilterData) {
        if (!mediaFilterData.removable) {
            return;
        }
        this.currentSearchFilters.remove(mediaFilterData);
        int i = this.selectedFilterIndex;
        if (i < 0 || i > this.currentSearchFilters.size() - 1) {
            this.selectedFilterIndex = this.currentSearchFilters.size() - 1;
        }
        onFiltersChanged();
        this.searchField.hideActionMode();
    }

    public void addSearchFilter(FiltersView.MediaFilterData mediaFilterData) {
        this.currentSearchFilters.add(mediaFilterData);
        if (this.searchContainer.getTag() != null) {
            this.selectedFilterIndex = this.currentSearchFilters.size() - 1;
        }
        onFiltersChanged();
    }

    public void clearSearchFilters() {
        int i = 0;
        while (i < this.currentSearchFilters.size()) {
            if (this.currentSearchFilters.get(i).removable) {
                this.currentSearchFilters.remove(i);
                i--;
            }
            i++;
        }
        onFiltersChanged();
    }

    public void onFiltersChanged() {
        boolean z = !this.currentSearchFilters.isEmpty();
        ArrayList arrayList = new ArrayList(this.currentSearchFilters);
        if (Build.VERSION.SDK_INT >= 19 && this.searchContainer.getTag() != null) {
            TransitionSet transitionSet = new TransitionSet();
            ChangeBounds changeBounds = new ChangeBounds();
            changeBounds.setDuration(150L);
            transitionSet.addTransition(new AnonymousClass4(this).setDuration(150L)).addTransition(changeBounds);
            transitionSet.setOrdering(0);
            transitionSet.setInterpolator((TimeInterpolator) CubicBezierInterpolator.EASE_OUT);
            transitionSet.addListener((Transition.TransitionListener) new AnonymousClass5(UserConfig.selectedAccount));
            TransitionManager.beginDelayedTransition(this.searchFilterLayout, transitionSet);
        }
        int i = 0;
        while (i < this.searchFilterLayout.getChildCount()) {
            if (!arrayList.remove(((SearchFilterView) this.searchFilterLayout.getChildAt(i)).getFilter())) {
                this.searchFilterLayout.removeViewAt(i);
                i--;
            }
            i++;
        }
        for (int i2 = 0; i2 < arrayList.size(); i2++) {
            SearchFilterView searchFilterView = new SearchFilterView(getContext(), this.resourcesProvider);
            searchFilterView.setData((FiltersView.MediaFilterData) arrayList.get(i2));
            searchFilterView.setOnClickListener(new ActionBarMenuItem$$ExternalSyntheticLambda4(this, searchFilterView));
            this.searchFilterLayout.addView(searchFilterView, LayoutHelper.createLinear(-2, -1, 0, 0, 0, 6, 0));
        }
        int i3 = 0;
        while (i3 < this.searchFilterLayout.getChildCount()) {
            ((SearchFilterView) this.searchFilterLayout.getChildAt(i3)).setExpanded(i3 == this.selectedFilterIndex);
            i3++;
        }
        this.searchFilterLayout.setTag(z ? 1 : null);
        float x = this.searchField.getX();
        if (this.searchContainer.getTag() != null) {
            this.searchField.getViewTreeObserver().addOnPreDrawListener(new AnonymousClass6(x));
        }
        checkClearButton();
    }

    /* renamed from: org.telegram.ui.ActionBar.ActionBarMenuItem$4 */
    /* loaded from: classes3.dex */
    public class AnonymousClass4 extends Visibility {
        AnonymousClass4(ActionBarMenuItem actionBarMenuItem) {
        }

        @Override // android.transition.Visibility
        public Animator onAppear(ViewGroup viewGroup, View view, TransitionValues transitionValues, TransitionValues transitionValues2) {
            if (view instanceof SearchFilterView) {
                AnimatorSet animatorSet = new AnimatorSet();
                animatorSet.playTogether(ObjectAnimator.ofFloat(view, View.ALPHA, 0.0f, 1.0f), ObjectAnimator.ofFloat(view, View.SCALE_X, 0.5f, 1.0f), ObjectAnimator.ofFloat(view, View.SCALE_Y, 0.5f, 1.0f));
                animatorSet.setInterpolator(CubicBezierInterpolator.DEFAULT);
                return animatorSet;
            }
            return ObjectAnimator.ofFloat(view, View.ALPHA, 0.0f, 1.0f);
        }

        @Override // android.transition.Visibility
        public Animator onDisappear(ViewGroup viewGroup, View view, TransitionValues transitionValues, TransitionValues transitionValues2) {
            if (view instanceof SearchFilterView) {
                AnimatorSet animatorSet = new AnimatorSet();
                animatorSet.playTogether(ObjectAnimator.ofFloat(view, View.ALPHA, view.getAlpha(), 0.0f), ObjectAnimator.ofFloat(view, View.SCALE_X, view.getScaleX(), 0.5f), ObjectAnimator.ofFloat(view, View.SCALE_Y, view.getScaleX(), 0.5f));
                animatorSet.setInterpolator(CubicBezierInterpolator.DEFAULT);
                return animatorSet;
            }
            return ObjectAnimator.ofFloat(view, View.ALPHA, 1.0f, 0.0f);
        }
    }

    /* renamed from: org.telegram.ui.ActionBar.ActionBarMenuItem$5 */
    /* loaded from: classes3.dex */
    public class AnonymousClass5 implements Transition.TransitionListener {
        final /* synthetic */ int val$selectedAccount;

        @Override // android.transition.Transition.TransitionListener
        public void onTransitionPause(Transition transition) {
        }

        @Override // android.transition.Transition.TransitionListener
        public void onTransitionResume(Transition transition) {
        }

        AnonymousClass5(int i) {
            ActionBarMenuItem.this = r1;
            this.val$selectedAccount = i;
        }

        @Override // android.transition.Transition.TransitionListener
        public void onTransitionStart(Transition transition) {
            ActionBarMenuItem.this.notificationIndex = NotificationCenter.getInstance(this.val$selectedAccount).setAnimationInProgress(ActionBarMenuItem.this.notificationIndex, null);
        }

        @Override // android.transition.Transition.TransitionListener
        public void onTransitionEnd(Transition transition) {
            NotificationCenter.getInstance(this.val$selectedAccount).onAnimationFinish(ActionBarMenuItem.this.notificationIndex);
        }

        @Override // android.transition.Transition.TransitionListener
        public void onTransitionCancel(Transition transition) {
            NotificationCenter.getInstance(this.val$selectedAccount).onAnimationFinish(ActionBarMenuItem.this.notificationIndex);
        }
    }

    public /* synthetic */ void lambda$onFiltersChanged$11(SearchFilterView searchFilterView, View view) {
        int indexOf = this.currentSearchFilters.indexOf(searchFilterView.getFilter());
        if (this.selectedFilterIndex != indexOf) {
            this.selectedFilterIndex = indexOf;
            onFiltersChanged();
        } else if (!searchFilterView.getFilter().removable) {
        } else {
            if (!searchFilterView.selectedForDelete) {
                searchFilterView.setSelectedForDelete(true);
                return;
            }
            FiltersView.MediaFilterData filter = searchFilterView.getFilter();
            removeSearchFilter(filter);
            ActionBarMenuItemSearchListener actionBarMenuItemSearchListener = this.listener;
            if (actionBarMenuItemSearchListener == null) {
                return;
            }
            actionBarMenuItemSearchListener.onSearchFilterCleared(filter);
            this.listener.onTextChanged(this.searchField);
        }
    }

    /* renamed from: org.telegram.ui.ActionBar.ActionBarMenuItem$6 */
    /* loaded from: classes3.dex */
    public class AnonymousClass6 implements ViewTreeObserver.OnPreDrawListener {
        final /* synthetic */ float val$oldX;

        AnonymousClass6(float f) {
            ActionBarMenuItem.this = r1;
            this.val$oldX = f;
        }

        @Override // android.view.ViewTreeObserver.OnPreDrawListener
        public boolean onPreDraw() {
            ActionBarMenuItem.this.searchField.getViewTreeObserver().removeOnPreDrawListener(this);
            if (ActionBarMenuItem.this.searchField.getX() != this.val$oldX) {
                ActionBarMenuItem.this.searchField.setTranslationX(this.val$oldX - ActionBarMenuItem.this.searchField.getX());
            }
            ActionBarMenuItem.this.searchField.animate().translationX(0.0f).setDuration(250L).setStartDelay(0L).setInterpolator(CubicBezierInterpolator.DEFAULT).start();
            return true;
        }
    }

    public boolean isSubMenuShowing() {
        ActionBarPopupWindow actionBarPopupWindow = this.popupWindow;
        return actionBarPopupWindow != null && actionBarPopupWindow.isShowing();
    }

    public void closeSubMenu() {
        ActionBarPopupWindow actionBarPopupWindow = this.popupWindow;
        if (actionBarPopupWindow == null || !actionBarPopupWindow.isShowing()) {
            return;
        }
        this.popupWindow.dismiss();
    }

    public void setIcon(Drawable drawable) {
        RLottieImageView rLottieImageView = this.iconView;
        if (rLottieImageView == null) {
            return;
        }
        if (drawable instanceof RLottieDrawable) {
            rLottieImageView.setAnimation((RLottieDrawable) drawable);
        } else {
            rLottieImageView.setImageDrawable(drawable);
        }
    }

    public RLottieImageView getIconView() {
        return this.iconView;
    }

    public TextView getTextView() {
        return this.textView;
    }

    public void setIcon(int i) {
        RLottieImageView rLottieImageView = this.iconView;
        if (rLottieImageView == null) {
            return;
        }
        rLottieImageView.setImageResource(i);
    }

    public void setText(CharSequence charSequence) {
        TextView textView = this.textView;
        if (textView == null) {
            return;
        }
        textView.setText(charSequence);
    }

    public View getContentView() {
        RLottieImageView rLottieImageView = this.iconView;
        return rLottieImageView != null ? rLottieImageView : this.textView;
    }

    public void setSearchFieldHint(CharSequence charSequence) {
        if (this.searchFieldCaption == null) {
            return;
        }
        this.searchField.setHint(charSequence);
        setContentDescription(charSequence);
    }

    public void setSearchFieldText(CharSequence charSequence, boolean z) {
        if (this.searchFieldCaption == null) {
            return;
        }
        this.animateClear = z;
        this.searchField.setText(charSequence);
        if (TextUtils.isEmpty(charSequence)) {
            return;
        }
        this.searchField.setSelection(charSequence.length());
    }

    public void onSearchPressed() {
        ActionBarMenuItemSearchListener actionBarMenuItemSearchListener = this.listener;
        if (actionBarMenuItemSearchListener != null) {
            actionBarMenuItemSearchListener.onSearchPressed(this.searchField);
        }
    }

    public EditTextBoldCursor getSearchField() {
        return this.searchField;
    }

    public ActionBarMenuItem setOverrideMenuClick(boolean z) {
        this.overrideMenuClick = z;
        return this;
    }

    public ActionBarMenuItem setIsSearchField(boolean z) {
        return setIsSearchField(z, false);
    }

    public ActionBarMenuItem setIsSearchField(boolean z, boolean z2) {
        if (this.parentMenu == null) {
            return this;
        }
        if (z && this.searchContainer == null) {
            AnonymousClass7 anonymousClass7 = new AnonymousClass7(getContext(), z2);
            this.searchContainer = anonymousClass7;
            anonymousClass7.setClipChildren(false);
            this.wrappedSearchFrameLayout = null;
            if (z2) {
                this.wrappedSearchFrameLayout = new FrameLayout(getContext());
                AnonymousClass8 anonymousClass8 = new AnonymousClass8(this, getContext());
                anonymousClass8.addView(this.searchContainer, LayoutHelper.createScroll(-2, -1, 0));
                anonymousClass8.setHorizontalScrollBarEnabled(false);
                anonymousClass8.setClipChildren(false);
                this.wrappedSearchFrameLayout.addView(anonymousClass8, LayoutHelper.createFrame(-1, -1.0f, 0, 0.0f, 0.0f, 48.0f, 0.0f));
                this.parentMenu.addView(this.wrappedSearchFrameLayout, 0, LayoutHelper.createLinear(0, -1, 1.0f, 0, 0, 0, 0));
            } else {
                this.parentMenu.addView(this.searchContainer, 0, LayoutHelper.createLinear(0, -1, 1.0f, 6, 0, 0, 0));
            }
            this.searchContainer.setVisibility(8);
            TextView textView = new TextView(getContext());
            this.searchFieldCaption = textView;
            textView.setTextSize(1, 18.0f);
            this.searchFieldCaption.setTextColor(getThemedColor("actionBarDefaultSearch"));
            this.searchFieldCaption.setSingleLine(true);
            this.searchFieldCaption.setEllipsize(TextUtils.TruncateAt.END);
            this.searchFieldCaption.setVisibility(8);
            this.searchFieldCaption.setGravity(LocaleController.isRTL ? 5 : 3);
            AnonymousClass9 anonymousClass9 = new AnonymousClass9(getContext());
            this.searchField = anonymousClass9;
            anonymousClass9.setScrollContainer(false);
            this.searchField.setCursorWidth(1.5f);
            this.searchField.setCursorColor(getThemedColor("actionBarDefaultSearch"));
            this.searchField.setTextSize(1, 18.0f);
            this.searchField.setHintTextColor(getThemedColor("actionBarDefaultSearchPlaceholder"));
            this.searchField.setTextColor(getThemedColor("actionBarDefaultSearch"));
            this.searchField.setSingleLine(true);
            this.searchField.setBackgroundResource(0);
            this.searchField.setPadding(0, 0, 0, 0);
            this.searchField.setInputType(this.searchField.getInputType() | 524288);
            if (Build.VERSION.SDK_INT < 23) {
                this.searchField.setCustomSelectionActionModeCallback(new AnonymousClass10(this));
            }
            this.searchField.setOnEditorActionListener(new ActionBarMenuItem$$ExternalSyntheticLambda10(this));
            this.searchField.addTextChangedListener(new AnonymousClass11());
            this.searchField.setImeOptions(33554435);
            this.searchField.setTextIsSelectable(false);
            this.searchField.setHighlightColor(getThemedColor("chat_inTextSelectionHighlight"));
            this.searchField.setHandlesColor(getThemedColor("chat_TextSelectionCursor"));
            LinearLayout linearLayout = new LinearLayout(getContext());
            this.searchFilterLayout = linearLayout;
            linearLayout.setOrientation(0);
            this.searchFilterLayout.setVisibility(0);
            if (!LocaleController.isRTL) {
                this.searchContainer.addView(this.searchFieldCaption, LayoutHelper.createFrame(-2, 36.0f, 19, 0.0f, 5.5f, 0.0f, 0.0f));
                this.searchContainer.addView(this.searchField, LayoutHelper.createFrame(-2, 36.0f, 16, 6.0f, 0.0f, 48.0f, 0.0f));
                this.searchContainer.addView(this.searchFilterLayout, LayoutHelper.createFrame(-2, 32.0f, 16, 0.0f, 0.0f, 48.0f, 0.0f));
            } else {
                this.searchContainer.addView(this.searchFilterLayout, LayoutHelper.createFrame(-2, 32.0f, 16, 0.0f, 0.0f, 48.0f, 0.0f));
                this.searchContainer.addView(this.searchField, LayoutHelper.createFrame(-2, 36.0f, 16, 0.0f, 0.0f, z2 ? 0.0f : 48.0f, 0.0f));
                this.searchContainer.addView(this.searchFieldCaption, LayoutHelper.createFrame(-2, 36.0f, 21, 0.0f, 5.5f, 48.0f, 0.0f));
            }
            this.searchFilterLayout.setClipChildren(false);
            AnonymousClass12 anonymousClass12 = new AnonymousClass12(getContext());
            this.clearButton = anonymousClass12;
            AnonymousClass13 anonymousClass13 = new AnonymousClass13();
            this.progressDrawable = anonymousClass13;
            anonymousClass12.setImageDrawable(anonymousClass13);
            this.clearButton.setScaleType(ImageView.ScaleType.CENTER);
            this.clearButton.setAlpha(0.0f);
            this.clearButton.setRotation(45.0f);
            this.clearButton.setScaleX(0.0f);
            this.clearButton.setScaleY(0.0f);
            this.clearButton.setOnClickListener(new ActionBarMenuItem$$ExternalSyntheticLambda2(this));
            this.clearButton.setContentDescription(LocaleController.getString("ClearButton", 2131625132));
            if (z2) {
                this.wrappedSearchFrameLayout.addView(this.clearButton, LayoutHelper.createFrame(48, -1, 21));
            } else {
                this.searchContainer.addView(this.clearButton, LayoutHelper.createFrame(48, -1, 21));
            }
        }
        this.isSearchField = z;
        return this;
    }

    /* renamed from: org.telegram.ui.ActionBar.ActionBarMenuItem$7 */
    /* loaded from: classes3.dex */
    public class AnonymousClass7 extends FrameLayout {
        private boolean ignoreRequestLayout;
        final /* synthetic */ boolean val$wrapInScrollView;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass7(Context context, boolean z) {
            super(context);
            ActionBarMenuItem.this = r1;
            this.val$wrapInScrollView = z;
        }

        @Override // android.view.View
        public void setVisibility(int i) {
            super.setVisibility(i);
            if (ActionBarMenuItem.this.clearButton != null) {
                ActionBarMenuItem.this.clearButton.setVisibility(i);
            }
            if (ActionBarMenuItem.this.wrappedSearchFrameLayout != null) {
                ActionBarMenuItem.this.wrappedSearchFrameLayout.setVisibility(i);
            }
        }

        @Override // android.view.View
        public void setAlpha(float f) {
            super.setAlpha(f);
            if (ActionBarMenuItem.this.clearButton == null || ActionBarMenuItem.this.clearButton.getTag() == null) {
                return;
            }
            ActionBarMenuItem.this.clearButton.setAlpha(f);
            ActionBarMenuItem.this.clearButton.setScaleX(f);
            ActionBarMenuItem.this.clearButton.setScaleY(f);
        }

        @Override // android.widget.FrameLayout, android.view.View
        protected void onMeasure(int i, int i2) {
            int i3;
            int i4;
            if (!this.val$wrapInScrollView) {
                measureChildWithMargins(ActionBarMenuItem.this.clearButton, i, 0, i2, 0);
            }
            if (!LocaleController.isRTL) {
                if (ActionBarMenuItem.this.searchFieldCaption.getVisibility() == 0) {
                    measureChildWithMargins(ActionBarMenuItem.this.searchFieldCaption, i, View.MeasureSpec.getSize(i) / 2, i2, 0);
                    i4 = ActionBarMenuItem.this.searchFieldCaption.getMeasuredWidth() + AndroidUtilities.dp(4.0f);
                } else {
                    i4 = 0;
                }
                int size = View.MeasureSpec.getSize(i);
                this.ignoreRequestLayout = true;
                measureChildWithMargins(ActionBarMenuItem.this.searchFilterLayout, i, i4, i2, 0);
                int measuredWidth = ActionBarMenuItem.this.searchFilterLayout.getVisibility() == 0 ? ActionBarMenuItem.this.searchFilterLayout.getMeasuredWidth() : 0;
                measureChildWithMargins(ActionBarMenuItem.this.searchField, i, i4 + measuredWidth, i2, 0);
                this.ignoreRequestLayout = false;
                setMeasuredDimension(Math.max(measuredWidth + ActionBarMenuItem.this.searchField.getMeasuredWidth(), size), View.MeasureSpec.getSize(i2));
                return;
            }
            if (ActionBarMenuItem.this.searchFieldCaption.getVisibility() == 0) {
                measureChildWithMargins(ActionBarMenuItem.this.searchFieldCaption, i, View.MeasureSpec.getSize(i) / 2, i2, 0);
                i3 = ActionBarMenuItem.this.searchFieldCaption.getMeasuredWidth() + AndroidUtilities.dp(4.0f);
            } else {
                i3 = 0;
            }
            int size2 = View.MeasureSpec.getSize(i);
            this.ignoreRequestLayout = true;
            measureChildWithMargins(ActionBarMenuItem.this.searchFilterLayout, i, i3, i2, 0);
            int measuredWidth2 = ActionBarMenuItem.this.searchFilterLayout.getVisibility() == 0 ? ActionBarMenuItem.this.searchFilterLayout.getMeasuredWidth() : 0;
            measureChildWithMargins(ActionBarMenuItem.this.searchField, View.MeasureSpec.makeMeasureSpec(size2 - AndroidUtilities.dp(12.0f), 0), i3 + measuredWidth2, i2, 0);
            this.ignoreRequestLayout = false;
            setMeasuredDimension(Math.max(measuredWidth2 + ActionBarMenuItem.this.searchField.getMeasuredWidth(), size2), View.MeasureSpec.getSize(i2));
        }

        @Override // android.view.View, android.view.ViewParent
        public void requestLayout() {
            if (this.ignoreRequestLayout) {
                return;
            }
            super.requestLayout();
        }

        @Override // android.widget.FrameLayout, android.view.ViewGroup, android.view.View
        protected void onLayout(boolean z, int i, int i2, int i3, int i4) {
            super.onLayout(z, i, i2, i3, i4);
            int i5 = 0;
            if (!LocaleController.isRTL && ActionBarMenuItem.this.searchFieldCaption.getVisibility() == 0) {
                i5 = AndroidUtilities.dp(4.0f) + ActionBarMenuItem.this.searchFieldCaption.getMeasuredWidth();
            }
            if (ActionBarMenuItem.this.searchFilterLayout.getVisibility() == 0) {
                i5 += ActionBarMenuItem.this.searchFilterLayout.getMeasuredWidth();
            }
            ActionBarMenuItem.this.searchField.layout(i5, ActionBarMenuItem.this.searchField.getTop(), ActionBarMenuItem.this.searchField.getMeasuredWidth() + i5, ActionBarMenuItem.this.searchField.getBottom());
        }
    }

    /* renamed from: org.telegram.ui.ActionBar.ActionBarMenuItem$8 */
    /* loaded from: classes3.dex */
    public class AnonymousClass8 extends HorizontalScrollView {
        boolean isDragging;

        AnonymousClass8(ActionBarMenuItem actionBarMenuItem, Context context) {
            super(context);
        }

        @Override // android.widget.HorizontalScrollView, android.view.ViewGroup
        public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
            checkDragg(motionEvent);
            return super.onInterceptTouchEvent(motionEvent);
        }

        @Override // android.widget.HorizontalScrollView, android.view.View
        public boolean onTouchEvent(MotionEvent motionEvent) {
            checkDragg(motionEvent);
            return super.onTouchEvent(motionEvent);
        }

        private void checkDragg(MotionEvent motionEvent) {
            if (motionEvent.getAction() == 0) {
                this.isDragging = true;
            } else if (motionEvent.getAction() != 1 && motionEvent.getAction() != 3) {
            } else {
                this.isDragging = false;
            }
        }

        @Override // android.widget.HorizontalScrollView, android.view.View
        protected void onOverScrolled(int i, int i2, boolean z, boolean z2) {
            if (!this.isDragging) {
                return;
            }
            super.onOverScrolled(i, i2, z, z2);
        }
    }

    /* renamed from: org.telegram.ui.ActionBar.ActionBarMenuItem$9 */
    /* loaded from: classes3.dex */
    public class AnonymousClass9 extends EditTextBoldCursor {
        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass9(Context context) {
            super(context);
            ActionBarMenuItem.this = r1;
        }

        @Override // org.telegram.ui.Components.EditTextBoldCursor, android.widget.TextView, android.view.View
        public void onMeasure(int i, int i2) {
            super.onMeasure(i, i2);
            setMeasuredDimension(Math.max(View.MeasureSpec.getSize(i), getMeasuredWidth()) + AndroidUtilities.dp(3.0f), getMeasuredHeight());
        }

        @Override // org.telegram.ui.Components.EditTextEffects, android.widget.TextView
        public void onSelectionChanged(int i, int i2) {
            super.onSelectionChanged(i, i2);
        }

        @Override // android.widget.TextView, android.view.View, android.view.KeyEvent.Callback
        public boolean onKeyDown(int i, KeyEvent keyEvent) {
            if (i == 67 && ActionBarMenuItem.this.searchField.length() == 0 && ((ActionBarMenuItem.this.searchFieldCaption.getVisibility() == 0 && ActionBarMenuItem.this.searchFieldCaption.length() > 0) || ActionBarMenuItem.this.hasRemovableFilters())) {
                if (ActionBarMenuItem.this.hasRemovableFilters()) {
                    FiltersView.MediaFilterData mediaFilterData = (FiltersView.MediaFilterData) ActionBarMenuItem.this.currentSearchFilters.get(ActionBarMenuItem.this.currentSearchFilters.size() - 1);
                    ActionBarMenuItemSearchListener actionBarMenuItemSearchListener = ActionBarMenuItem.this.listener;
                    if (actionBarMenuItemSearchListener != null) {
                        actionBarMenuItemSearchListener.onSearchFilterCleared(mediaFilterData);
                    }
                    ActionBarMenuItem.this.removeSearchFilter(mediaFilterData);
                } else {
                    ActionBarMenuItem.this.clearButton.callOnClick();
                }
                return true;
            }
            return super.onKeyDown(i, keyEvent);
        }

        @Override // org.telegram.ui.Components.EditTextBoldCursor, android.widget.TextView, android.view.View
        public boolean onTouchEvent(MotionEvent motionEvent) {
            boolean onTouchEvent = super.onTouchEvent(motionEvent);
            if (motionEvent.getAction() == 1 && !AndroidUtilities.showKeyboard(this)) {
                clearFocus();
                requestFocus();
            }
            return onTouchEvent;
        }
    }

    /* renamed from: org.telegram.ui.ActionBar.ActionBarMenuItem$10 */
    /* loaded from: classes3.dex */
    public class AnonymousClass10 implements ActionMode.Callback {
        @Override // android.view.ActionMode.Callback
        public boolean onActionItemClicked(ActionMode actionMode, MenuItem menuItem) {
            return false;
        }

        @Override // android.view.ActionMode.Callback
        public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
            return false;
        }

        @Override // android.view.ActionMode.Callback
        public void onDestroyActionMode(ActionMode actionMode) {
        }

        @Override // android.view.ActionMode.Callback
        public boolean onPrepareActionMode(ActionMode actionMode, Menu menu) {
            return false;
        }

        AnonymousClass10(ActionBarMenuItem actionBarMenuItem) {
        }
    }

    public /* synthetic */ boolean lambda$setIsSearchField$12(TextView textView, int i, KeyEvent keyEvent) {
        if (keyEvent != null) {
            if ((keyEvent.getAction() != 1 || keyEvent.getKeyCode() != 84) && (keyEvent.getAction() != 0 || keyEvent.getKeyCode() != 66)) {
                return false;
            }
            AndroidUtilities.hideKeyboard(this.searchField);
            ActionBarMenuItemSearchListener actionBarMenuItemSearchListener = this.listener;
            if (actionBarMenuItemSearchListener == null) {
                return false;
            }
            actionBarMenuItemSearchListener.onSearchPressed(this.searchField);
            return false;
        }
        return false;
    }

    /* renamed from: org.telegram.ui.ActionBar.ActionBarMenuItem$11 */
    /* loaded from: classes3.dex */
    public class AnonymousClass11 implements TextWatcher {
        @Override // android.text.TextWatcher
        public void afterTextChanged(Editable editable) {
        }

        @Override // android.text.TextWatcher
        public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
        }

        AnonymousClass11() {
            ActionBarMenuItem.this = r1;
        }

        @Override // android.text.TextWatcher
        public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            if (ActionBarMenuItem.this.ignoreOnTextChange) {
                ActionBarMenuItem.this.ignoreOnTextChange = false;
                return;
            }
            ActionBarMenuItem actionBarMenuItem = ActionBarMenuItem.this;
            ActionBarMenuItemSearchListener actionBarMenuItemSearchListener = actionBarMenuItem.listener;
            if (actionBarMenuItemSearchListener != null) {
                actionBarMenuItemSearchListener.onTextChanged(actionBarMenuItem.searchField);
            }
            ActionBarMenuItem.this.checkClearButton();
            if (ActionBarMenuItem.this.currentSearchFilters.isEmpty() || TextUtils.isEmpty(ActionBarMenuItem.this.searchField.getText()) || ActionBarMenuItem.this.selectedFilterIndex < 0) {
                return;
            }
            ActionBarMenuItem.this.selectedFilterIndex = -1;
            ActionBarMenuItem.this.onFiltersChanged();
        }
    }

    /* renamed from: org.telegram.ui.ActionBar.ActionBarMenuItem$12 */
    /* loaded from: classes3.dex */
    public class AnonymousClass12 extends ImageView {
        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass12(Context context) {
            super(context);
            ActionBarMenuItem.this = r1;
        }

        @Override // android.widget.ImageView, android.view.View
        protected void onDetachedFromWindow() {
            super.onDetachedFromWindow();
            clearAnimation();
            if (getTag() == null) {
                ActionBarMenuItem.this.clearButton.setVisibility(4);
                ActionBarMenuItem.this.clearButton.setAlpha(0.0f);
                ActionBarMenuItem.this.clearButton.setRotation(45.0f);
                ActionBarMenuItem.this.clearButton.setScaleX(0.0f);
                ActionBarMenuItem.this.clearButton.setScaleY(0.0f);
                return;
            }
            ActionBarMenuItem.this.clearButton.setAlpha(1.0f);
            ActionBarMenuItem.this.clearButton.setRotation(0.0f);
            ActionBarMenuItem.this.clearButton.setScaleX(1.0f);
            ActionBarMenuItem.this.clearButton.setScaleY(1.0f);
        }
    }

    /* renamed from: org.telegram.ui.ActionBar.ActionBarMenuItem$13 */
    /* loaded from: classes3.dex */
    public class AnonymousClass13 extends CloseProgressDrawable2 {
        AnonymousClass13() {
            ActionBarMenuItem.this = r1;
        }

        @Override // org.telegram.ui.Components.CloseProgressDrawable2
        public int getCurrentColor() {
            return ActionBarMenuItem.this.parentMenu.parentActionBar.itemsColor;
        }
    }

    public /* synthetic */ void lambda$setIsSearchField$13(View view) {
        if (this.searchField.length() != 0) {
            this.searchField.setText("");
        } else if (hasRemovableFilters()) {
            this.searchField.hideActionMode();
            for (int i = 0; i < this.currentSearchFilters.size(); i++) {
                if (this.listener != null && this.currentSearchFilters.get(i).removable) {
                    this.listener.onSearchFilterCleared(this.currentSearchFilters.get(i));
                }
            }
            clearSearchFilters();
        } else {
            TextView textView = this.searchFieldCaption;
            if (textView != null && textView.getVisibility() == 0) {
                this.searchFieldCaption.setVisibility(8);
                ActionBarMenuItemSearchListener actionBarMenuItemSearchListener = this.listener;
                if (actionBarMenuItemSearchListener != null) {
                    actionBarMenuItemSearchListener.onCaptionCleared();
                }
            }
        }
        this.searchField.requestFocus();
        AndroidUtilities.showKeyboard(this.searchField);
    }

    public View.OnClickListener getOnClickListener() {
        return this.onClickListener;
    }

    @Override // android.view.View
    public void setOnClickListener(View.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
        super.setOnClickListener(onClickListener);
    }

    public void checkClearButton() {
        ActionBarMenuItemSearchListener actionBarMenuItemSearchListener;
        TextView textView;
        if (this.clearButton != null) {
            if (!hasRemovableFilters() && TextUtils.isEmpty(this.searchField.getText()) && (((actionBarMenuItemSearchListener = this.listener) == null || !actionBarMenuItemSearchListener.forceShowClear()) && ((textView = this.searchFieldCaption) == null || textView.getVisibility() != 0))) {
                if (this.clearButton.getTag() == null) {
                    return;
                }
                this.clearButton.setTag(null);
                this.clearButton.clearAnimation();
                if (this.animateClear) {
                    this.clearButton.animate().setInterpolator(new DecelerateInterpolator()).alpha(0.0f).setDuration(180L).scaleY(0.0f).scaleX(0.0f).rotation(45.0f).withEndAction(new ActionBarMenuItem$$ExternalSyntheticLambda12(this)).start();
                    return;
                }
                this.clearButton.setAlpha(0.0f);
                this.clearButton.setRotation(45.0f);
                this.clearButton.setScaleX(0.0f);
                this.clearButton.setScaleY(0.0f);
                this.clearButton.setVisibility(4);
                this.animateClear = true;
            } else if (this.clearButton.getTag() != null) {
            } else {
                this.clearButton.setTag(1);
                this.clearButton.clearAnimation();
                this.clearButton.setVisibility(0);
                if (this.animateClear) {
                    this.clearButton.animate().setInterpolator(new DecelerateInterpolator()).alpha(1.0f).setDuration(180L).scaleY(1.0f).scaleX(1.0f).rotation(0.0f).start();
                    return;
                }
                this.clearButton.setAlpha(1.0f);
                this.clearButton.setRotation(0.0f);
                this.clearButton.setScaleX(1.0f);
                this.clearButton.setScaleY(1.0f);
                this.animateClear = true;
            }
        }
    }

    public /* synthetic */ void lambda$checkClearButton$14() {
        this.clearButton.setVisibility(4);
    }

    public boolean hasRemovableFilters() {
        if (this.currentSearchFilters.isEmpty()) {
            return false;
        }
        for (int i = 0; i < this.currentSearchFilters.size(); i++) {
            if (this.currentSearchFilters.get(i).removable) {
                return true;
            }
        }
        return false;
    }

    public void setShowSearchProgress(boolean z) {
        CloseProgressDrawable2 closeProgressDrawable2 = this.progressDrawable;
        if (closeProgressDrawable2 == null) {
            return;
        }
        if (z) {
            closeProgressDrawable2.startAnimation();
        } else {
            closeProgressDrawable2.stopAnimation();
        }
    }

    public void setSearchFieldCaption(CharSequence charSequence) {
        if (this.searchFieldCaption == null) {
            return;
        }
        if (TextUtils.isEmpty(charSequence)) {
            this.searchFieldCaption.setVisibility(8);
            return;
        }
        this.searchFieldCaption.setVisibility(0);
        this.searchFieldCaption.setText(charSequence);
    }

    public boolean isSearchField() {
        return this.isSearchField;
    }

    public void clearSearchText() {
        EditTextBoldCursor editTextBoldCursor = this.searchField;
        if (editTextBoldCursor == null) {
            return;
        }
        editTextBoldCursor.setText("");
    }

    public ActionBarMenuItem setActionBarMenuItemSearchListener(ActionBarMenuItemSearchListener actionBarMenuItemSearchListener) {
        this.listener = actionBarMenuItemSearchListener;
        return this;
    }

    public ActionBarMenuItem setAllowCloseAnimation(boolean z) {
        this.allowCloseAnimation = z;
        return this;
    }

    public void setPopupAnimationEnabled(boolean z) {
        ActionBarPopupWindow actionBarPopupWindow = this.popupWindow;
        if (actionBarPopupWindow != null) {
            actionBarPopupWindow.setAnimationEnabled(z);
        }
        this.animationEnabled = z;
    }

    @Override // android.widget.FrameLayout, android.view.ViewGroup, android.view.View
    protected void onLayout(boolean z, int i, int i2, int i3, int i4) {
        super.onLayout(z, i, i2, i3, i4);
        ActionBarPopupWindow actionBarPopupWindow = this.popupWindow;
        if (actionBarPopupWindow != null && actionBarPopupWindow.isShowing()) {
            updateOrShowPopup(false, true);
        }
        ActionBarMenuItemSearchListener actionBarMenuItemSearchListener = this.listener;
        if (actionBarMenuItemSearchListener != null) {
            actionBarMenuItemSearchListener.onLayout(i, i2, i3, i4);
        }
    }

    public void setAdditionalYOffset(int i) {
        this.additionalYOffset = i;
    }

    public void setAdditionalXOffset(int i) {
        this.additionalXOffset = i;
    }

    public void forceUpdatePopupPosition() {
        ActionBarPopupWindow actionBarPopupWindow = this.popupWindow;
        if (actionBarPopupWindow == null || !actionBarPopupWindow.isShowing()) {
            return;
        }
        this.popupLayout.measure(View.MeasureSpec.makeMeasureSpec(AndroidUtilities.displaySize.x - AndroidUtilities.dp(40.0f), Integer.MIN_VALUE), View.MeasureSpec.makeMeasureSpec(AndroidUtilities.displaySize.y, Integer.MIN_VALUE));
        updateOrShowPopup(true, true);
    }

    private void updateOrShowPopup(boolean z, boolean z2) {
        int i;
        int i2;
        ActionBarMenu actionBarMenu = this.parentMenu;
        if (actionBarMenu != null) {
            i = (-actionBarMenu.parentActionBar.getMeasuredHeight()) + this.parentMenu.getTop();
            i2 = this.parentMenu.getPaddingTop();
        } else {
            float scaleY = getScaleY();
            i = -((int) ((getMeasuredHeight() * scaleY) - ((this.subMenuOpenSide != 2 ? getTranslationY() : 0.0f) / scaleY)));
            i2 = this.additionalYOffset;
        }
        int i3 = i + i2 + this.yOffset;
        if (z) {
            this.popupLayout.scrollToTop();
        }
        View view = this.showSubMenuFrom;
        if (view == null) {
            view = this;
        }
        ActionBarMenu actionBarMenu2 = this.parentMenu;
        if (actionBarMenu2 != null) {
            ActionBar actionBar = actionBarMenu2.parentActionBar;
            if (this.subMenuOpenSide == 0) {
                if (z) {
                    this.popupWindow.showAsDropDown(actionBar, (((view.getLeft() + this.parentMenu.getLeft()) + view.getMeasuredWidth()) - this.popupWindow.getContentView().getMeasuredWidth()) + ((int) getTranslationX()), i3);
                }
                if (!z2) {
                    return;
                }
                this.popupWindow.update(actionBar, (((view.getLeft() + this.parentMenu.getLeft()) + view.getMeasuredWidth()) - this.popupWindow.getContentView().getMeasuredWidth()) + ((int) getTranslationX()), i3, -1, -1);
                return;
            }
            if (z) {
                if (this.forceSmoothKeyboard) {
                    this.popupWindow.showAtLocation(actionBar, 51, (getLeft() - AndroidUtilities.dp(8.0f)) + ((int) getTranslationX()), i3);
                } else {
                    this.popupWindow.showAsDropDown(actionBar, (getLeft() - AndroidUtilities.dp(8.0f)) + ((int) getTranslationX()), i3);
                }
            }
            if (!z2) {
                return;
            }
            this.popupWindow.update(actionBar, (getLeft() - AndroidUtilities.dp(8.0f)) + ((int) getTranslationX()), i3, -1, -1);
            return;
        }
        int i4 = this.subMenuOpenSide;
        if (i4 == 0) {
            if (getParent() == null) {
                return;
            }
            View view2 = (View) getParent();
            if (z) {
                this.popupWindow.showAsDropDown(view2, ((getLeft() + getMeasuredWidth()) - this.popupWindow.getContentView().getMeasuredWidth()) + this.additionalXOffset, i3);
            }
            if (!z2) {
                return;
            }
            this.popupWindow.update(view2, ((getLeft() + getMeasuredWidth()) - this.popupWindow.getContentView().getMeasuredWidth()) + this.additionalXOffset, i3, -1, -1);
        } else if (i4 == 1) {
            if (z) {
                this.popupWindow.showAsDropDown(this, (-AndroidUtilities.dp(8.0f)) + this.additionalXOffset, i3);
            }
            if (!z2) {
                return;
            }
            this.popupWindow.update(this, (-AndroidUtilities.dp(8.0f)) + this.additionalXOffset, i3, -1, -1);
        } else {
            if (z) {
                this.popupWindow.showAsDropDown(this, (getMeasuredWidth() - this.popupWindow.getContentView().getMeasuredWidth()) + this.additionalXOffset, i3);
            }
            if (!z2) {
                return;
            }
            this.popupWindow.update(this, (getMeasuredWidth() - this.popupWindow.getContentView().getMeasuredWidth()) + this.additionalXOffset, i3, -1, -1);
        }
    }

    public void hideSubItem(int i) {
        View findViewWithTag;
        ActionBarPopupWindow.ActionBarPopupWindowLayout actionBarPopupWindowLayout = this.popupLayout;
        if (actionBarPopupWindowLayout == null || (findViewWithTag = actionBarPopupWindowLayout.findViewWithTag(Integer.valueOf(i))) == null || findViewWithTag.getVisibility() == 8) {
            return;
        }
        findViewWithTag.setVisibility(8);
    }

    public void checkHideMenuItem() {
        boolean z;
        int i = 0;
        int i2 = 0;
        while (true) {
            if (i2 >= this.popupLayout.getItemsCount()) {
                z = false;
                break;
            } else if (this.popupLayout.getItemAt(i2).getVisibility() == 0) {
                z = true;
                break;
            } else {
                i2++;
            }
        }
        if (!z) {
            i = 8;
        }
        if (i != getVisibility()) {
            setVisibility(i);
        }
    }

    public boolean isSubItemVisible(int i) {
        View findViewWithTag;
        ActionBarPopupWindow.ActionBarPopupWindowLayout actionBarPopupWindowLayout = this.popupLayout;
        return (actionBarPopupWindowLayout == null || (findViewWithTag = actionBarPopupWindowLayout.findViewWithTag(Integer.valueOf(i))) == null || findViewWithTag.getVisibility() != 0) ? false : true;
    }

    public void showSubItem(int i) {
        showSubItem(i, false);
    }

    public void showSubItem(int i, boolean z) {
        View findViewWithTag;
        ActionBarPopupWindow.ActionBarPopupWindowLayout actionBarPopupWindowLayout = this.popupLayout;
        if (actionBarPopupWindowLayout == null || (findViewWithTag = actionBarPopupWindowLayout.findViewWithTag(Integer.valueOf(i))) == null || findViewWithTag.getVisibility() == 0) {
            return;
        }
        findViewWithTag.setAlpha(0.0f);
        findViewWithTag.animate().alpha(1.0f).setInterpolator(CubicBezierInterpolator.DEFAULT).setDuration(150L).start();
        findViewWithTag.setVisibility(0);
    }

    public void requestFocusOnSearchView() {
        if (this.searchContainer.getWidth() == 0 || this.searchField.isFocused()) {
            return;
        }
        this.searchField.requestFocus();
        AndroidUtilities.showKeyboard(this.searchField);
    }

    public void clearFocusOnSearchView() {
        this.searchField.clearFocus();
        AndroidUtilities.hideKeyboard(this.searchField);
    }

    public FrameLayout getSearchContainer() {
        return this.searchContainer;
    }

    @Override // android.view.View
    public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo accessibilityNodeInfo) {
        super.onInitializeAccessibilityNodeInfo(accessibilityNodeInfo);
        if (this.iconView != null) {
            accessibilityNodeInfo.setClassName("android.widget.ImageButton");
        } else if (this.textView == null) {
        } else {
            accessibilityNodeInfo.setClassName("android.widget.Button");
            if (!TextUtils.isEmpty(accessibilityNodeInfo.getText())) {
                return;
            }
            accessibilityNodeInfo.setText(this.textView.getText());
        }
    }

    public void updateColor() {
        if (this.searchFilterLayout != null) {
            for (int i = 0; i < this.searchFilterLayout.getChildCount(); i++) {
                if (this.searchFilterLayout.getChildAt(i) instanceof SearchFilterView) {
                    ((SearchFilterView) this.searchFilterLayout.getChildAt(i)).updateColors();
                }
            }
        }
        if (this.popupLayout != null) {
            for (int i2 = 0; i2 < this.popupLayout.getItemsCount(); i2++) {
                if (this.popupLayout.getItemAt(i2) instanceof ActionBarMenuSubItem) {
                    ((ActionBarMenuSubItem) this.popupLayout.getItemAt(i2)).setSelectorColor(getThemedColor("dialogButtonSelector"));
                }
            }
        }
        EditTextBoldCursor editTextBoldCursor = this.searchField;
        if (editTextBoldCursor != null) {
            editTextBoldCursor.setCursorColor(getThemedColor("actionBarDefaultSearch"));
            this.searchField.setHintTextColor(getThemedColor("actionBarDefaultSearchPlaceholder"));
            this.searchField.setTextColor(getThemedColor("actionBarDefaultSearch"));
            this.searchField.setHighlightColor(getThemedColor("chat_inTextSelectionHighlight"));
            this.searchField.setHandlesColor(getThemedColor("chat_TextSelectionCursor"));
        }
    }

    public void collapseSearchFilters() {
        this.selectedFilterIndex = -1;
        onFiltersChanged();
    }

    public void setTransitionOffset(float f) {
        this.transitionOffset = f;
        setTranslationX(0.0f);
    }

    private int getThemedColor(String str) {
        Theme.ResourcesProvider resourcesProvider = this.resourcesProvider;
        Integer color = resourcesProvider != null ? resourcesProvider.getColor(str) : null;
        return color != null ? color.intValue() : Theme.getColor(str);
    }

    /* loaded from: classes3.dex */
    public static class SearchFilterView extends FrameLayout {
        BackupImageView avatarImageView;
        ImageView closeIconView;
        FiltersView.MediaFilterData data;
        Runnable removeSelectionRunnable = new AnonymousClass1();
        private final Theme.ResourcesProvider resourcesProvider;
        ValueAnimator selectAnimator;
        private boolean selectedForDelete;
        private float selectedProgress;
        ShapeDrawable shapeDrawable;
        Drawable thumbDrawable;
        TextView titleView;

        /* renamed from: org.telegram.ui.ActionBar.ActionBarMenuItem$SearchFilterView$1 */
        /* loaded from: classes3.dex */
        public class AnonymousClass1 implements Runnable {
            AnonymousClass1() {
                SearchFilterView.this = r1;
            }

            @Override // java.lang.Runnable
            public void run() {
                if (SearchFilterView.this.selectedForDelete) {
                    SearchFilterView.this.setSelectedForDelete(false);
                }
            }
        }

        public SearchFilterView(Context context, Theme.ResourcesProvider resourcesProvider) {
            super(context);
            this.resourcesProvider = resourcesProvider;
            BackupImageView backupImageView = new BackupImageView(context);
            this.avatarImageView = backupImageView;
            addView(backupImageView, LayoutHelper.createFrame(32, 32.0f));
            ImageView imageView = new ImageView(context);
            this.closeIconView = imageView;
            imageView.setImageResource(2131165473);
            addView(this.closeIconView, LayoutHelper.createFrame(24, 24.0f, 16, 8.0f, 0.0f, 0.0f, 0.0f));
            TextView textView = new TextView(context);
            this.titleView = textView;
            textView.setTextSize(1, 14.0f);
            addView(this.titleView, LayoutHelper.createFrame(-2, -2.0f, 16, 38.0f, 0.0f, 16.0f, 0.0f));
            ShapeDrawable shapeDrawable = (ShapeDrawable) Theme.createRoundRectDrawable(AndroidUtilities.dp(28.0f), -12292204);
            this.shapeDrawable = shapeDrawable;
            setBackground(shapeDrawable);
            updateColors();
        }

        public void updateColors() {
            int themedColor = getThemedColor("groupcreate_spanBackground");
            int themedColor2 = getThemedColor("avatar_backgroundBlue");
            int themedColor3 = getThemedColor("windowBackgroundWhiteBlackText");
            int themedColor4 = getThemedColor("avatar_actionBarIconBlue");
            this.shapeDrawable.getPaint().setColor(ColorUtils.blendARGB(themedColor, themedColor2, this.selectedProgress));
            this.titleView.setTextColor(ColorUtils.blendARGB(themedColor3, themedColor4, this.selectedProgress));
            this.closeIconView.setColorFilter(themedColor4);
            this.closeIconView.setAlpha(this.selectedProgress);
            this.closeIconView.setScaleX(this.selectedProgress * 0.82f);
            this.closeIconView.setScaleY(this.selectedProgress * 0.82f);
            Drawable drawable = this.thumbDrawable;
            if (drawable != null) {
                Theme.setCombinedDrawableColor(drawable, getThemedColor("avatar_backgroundBlue"), false);
                Theme.setCombinedDrawableColor(this.thumbDrawable, getThemedColor("avatar_actionBarIconBlue"), true);
            }
            this.avatarImageView.setAlpha(1.0f - this.selectedProgress);
            FiltersView.MediaFilterData mediaFilterData = this.data;
            if (mediaFilterData != null && mediaFilterData.filterType == 7) {
                setData(mediaFilterData);
            }
            invalidate();
        }

        public void setData(FiltersView.MediaFilterData mediaFilterData) {
            this.data = mediaFilterData;
            this.titleView.setText(mediaFilterData.title);
            CombinedDrawable createCircleDrawableWithIcon = Theme.createCircleDrawableWithIcon(AndroidUtilities.dp(32.0f), mediaFilterData.iconResFilled);
            this.thumbDrawable = createCircleDrawableWithIcon;
            Theme.setCombinedDrawableColor(createCircleDrawableWithIcon, getThemedColor("avatar_backgroundBlue"), false);
            Theme.setCombinedDrawableColor(this.thumbDrawable, getThemedColor("avatar_actionBarIconBlue"), true);
            int i = mediaFilterData.filterType;
            if (i != 4) {
                if (i == 7) {
                    CombinedDrawable createCircleDrawableWithIcon2 = Theme.createCircleDrawableWithIcon(AndroidUtilities.dp(32.0f), 2131165331);
                    createCircleDrawableWithIcon2.setIconSize(AndroidUtilities.dp(16.0f), AndroidUtilities.dp(16.0f));
                    Theme.setCombinedDrawableColor(createCircleDrawableWithIcon2, getThemedColor("avatar_backgroundArchived"), false);
                    Theme.setCombinedDrawableColor(createCircleDrawableWithIcon2, getThemedColor("avatar_actionBarIconBlue"), true);
                    this.avatarImageView.setImageDrawable(createCircleDrawableWithIcon2);
                    return;
                }
                this.avatarImageView.setImageDrawable(this.thumbDrawable);
                return;
            }
            TLObject tLObject = mediaFilterData.chat;
            if (tLObject instanceof TLRPC$User) {
                TLRPC$User tLRPC$User = (TLRPC$User) tLObject;
                if (UserConfig.getInstance(UserConfig.selectedAccount).getCurrentUser().id == tLRPC$User.id) {
                    CombinedDrawable createCircleDrawableWithIcon3 = Theme.createCircleDrawableWithIcon(AndroidUtilities.dp(32.0f), 2131165340);
                    createCircleDrawableWithIcon3.setIconSize(AndroidUtilities.dp(16.0f), AndroidUtilities.dp(16.0f));
                    Theme.setCombinedDrawableColor(createCircleDrawableWithIcon3, getThemedColor("avatar_backgroundSaved"), false);
                    Theme.setCombinedDrawableColor(createCircleDrawableWithIcon3, getThemedColor("avatar_actionBarIconBlue"), true);
                    this.avatarImageView.setImageDrawable(createCircleDrawableWithIcon3);
                    return;
                }
                this.avatarImageView.getImageReceiver().setRoundRadius(AndroidUtilities.dp(16.0f));
                this.avatarImageView.getImageReceiver().setForUserOrChat(tLRPC$User, this.thumbDrawable);
            } else if (!(tLObject instanceof TLRPC$Chat)) {
            } else {
                this.avatarImageView.getImageReceiver().setRoundRadius(AndroidUtilities.dp(16.0f));
                this.avatarImageView.getImageReceiver().setForUserOrChat((TLRPC$Chat) tLObject, this.thumbDrawable);
            }
        }

        public void setExpanded(boolean z) {
            if (z) {
                this.titleView.setVisibility(0);
                return;
            }
            this.titleView.setVisibility(8);
            setSelectedForDelete(false);
        }

        public void setSelectedForDelete(boolean z) {
            if (this.selectedForDelete == z) {
                return;
            }
            AndroidUtilities.cancelRunOnUIThread(this.removeSelectionRunnable);
            this.selectedForDelete = z;
            ValueAnimator valueAnimator = this.selectAnimator;
            if (valueAnimator != null) {
                valueAnimator.removeAllListeners();
                this.selectAnimator.cancel();
            }
            float[] fArr = new float[2];
            fArr[0] = this.selectedProgress;
            fArr[1] = z ? 1.0f : 0.0f;
            ValueAnimator ofFloat = ValueAnimator.ofFloat(fArr);
            this.selectAnimator = ofFloat;
            ofFloat.addUpdateListener(new ActionBarMenuItem$SearchFilterView$$ExternalSyntheticLambda0(this));
            this.selectAnimator.addListener(new AnonymousClass2(z));
            this.selectAnimator.setDuration(150L).start();
            if (!this.selectedForDelete) {
                return;
            }
            AndroidUtilities.runOnUIThread(this.removeSelectionRunnable, 2000L);
        }

        public /* synthetic */ void lambda$setSelectedForDelete$0(ValueAnimator valueAnimator) {
            this.selectedProgress = ((Float) valueAnimator.getAnimatedValue()).floatValue();
            updateColors();
        }

        /* renamed from: org.telegram.ui.ActionBar.ActionBarMenuItem$SearchFilterView$2 */
        /* loaded from: classes3.dex */
        public class AnonymousClass2 extends AnimatorListenerAdapter {
            final /* synthetic */ boolean val$select;

            AnonymousClass2(boolean z) {
                SearchFilterView.this = r1;
                this.val$select = z;
            }

            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationEnd(Animator animator) {
                SearchFilterView.this.selectedProgress = this.val$select ? 1.0f : 0.0f;
                SearchFilterView.this.updateColors();
            }
        }

        public FiltersView.MediaFilterData getFilter() {
            return this.data;
        }

        private int getThemedColor(String str) {
            Theme.ResourcesProvider resourcesProvider = this.resourcesProvider;
            Integer color = resourcesProvider != null ? resourcesProvider.getColor(str) : null;
            return color != null ? color.intValue() : Theme.getColor(str);
        }
    }

    public ActionBarPopupWindow.GapView addColoredGap() {
        createPopupLayout();
        ActionBarPopupWindow.GapView gapView = new ActionBarPopupWindow.GapView(getContext(), this.resourcesProvider, "actionBarDefaultSubmenuSeparator");
        gapView.setTag(2131230820, 1);
        this.popupLayout.addView((View) gapView, LayoutHelper.createLinear(-1, 8));
        return gapView;
    }

    public static ActionBarMenuSubItem addItem(ActionBarPopupWindow.ActionBarPopupWindowLayout actionBarPopupWindowLayout, int i, CharSequence charSequence, boolean z, Theme.ResourcesProvider resourcesProvider) {
        ActionBarMenuSubItem actionBarMenuSubItem = new ActionBarMenuSubItem(actionBarPopupWindowLayout.getContext(), z, false, false, resourcesProvider);
        actionBarMenuSubItem.setTextAndIcon(charSequence, i);
        actionBarMenuSubItem.setMinimumWidth(AndroidUtilities.dp(196.0f));
        actionBarPopupWindowLayout.addView(actionBarMenuSubItem);
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) actionBarMenuSubItem.getLayoutParams();
        if (LocaleController.isRTL) {
            layoutParams.gravity = 5;
        }
        layoutParams.width = -1;
        layoutParams.height = AndroidUtilities.dp(48.0f);
        actionBarMenuSubItem.setLayoutParams(layoutParams);
        return actionBarMenuSubItem;
    }
}