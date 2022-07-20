package org.telegram.ui.ActionBar;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.SystemClock;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.ViewTreeObserver;
import android.view.WindowInsets;
import android.view.WindowInsetsAnimation;
import android.view.animation.Interpolator;
import android.widget.FrameLayout;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.ChatListItemAnimator;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.SharedConfig;
import org.telegram.messenger.UserConfig;
/* loaded from: classes3.dex */
public class AdjustPanLayoutHelper {
    public static boolean USE_ANDROID11_INSET_ANIMATOR = false;
    public static final Interpolator keyboardInterpolator = ChatListItemAnimator.DEFAULT_INTERPOLATOR;
    private boolean animationInProgress;
    ValueAnimator animator;
    boolean checkHierarchyHeight;
    private ViewGroup contentView;
    float from;
    private boolean ignoreOnce;
    boolean inverse;
    boolean isKeyboardVisible;
    protected float keyboardSize;
    private boolean needDelay;
    int notificationsIndex;
    private final View parent;
    View parentForListener;
    private View resizableView;
    private View resizableViewToSet;
    long startAfter;
    float to;
    private boolean usingInsetAnimator = false;
    private Runnable delayedAnimationRunnable = new AnonymousClass1();
    int previousHeight = -1;
    int previousContentHeight = -1;
    int previousStartOffset = -1;
    ArrayList<View> viewsToHeightSet = new ArrayList<>();
    ViewTreeObserver.OnPreDrawListener onPreDrawListener = new AnonymousClass2();
    private boolean enabled = true;

    protected boolean heightAnimationEnabled() {
        throw null;
    }

    public void onPanTranslationUpdate(float f, float f2, boolean z) {
    }

    public void onTransitionEnd() {
    }

    public void onTransitionStart(boolean z, int i) {
    }

    protected int startOffset() {
        return 0;
    }

    /* renamed from: org.telegram.ui.ActionBar.AdjustPanLayoutHelper$1 */
    /* loaded from: classes3.dex */
    public class AnonymousClass1 implements Runnable {
        AnonymousClass1() {
            AdjustPanLayoutHelper.this = r1;
        }

        @Override // java.lang.Runnable
        public void run() {
            ValueAnimator valueAnimator = AdjustPanLayoutHelper.this.animator;
            if (valueAnimator == null || valueAnimator.isRunning()) {
                return;
            }
            AdjustPanLayoutHelper.this.animator.start();
        }
    }

    /* renamed from: org.telegram.ui.ActionBar.AdjustPanLayoutHelper$2 */
    /* loaded from: classes3.dex */
    public class AnonymousClass2 implements ViewTreeObserver.OnPreDrawListener {
        AnonymousClass2() {
            AdjustPanLayoutHelper.this = r1;
        }

        @Override // android.view.ViewTreeObserver.OnPreDrawListener
        public boolean onPreDraw() {
            boolean z = true;
            if (SharedConfig.smoothKeyboard) {
                int height = AdjustPanLayoutHelper.this.parent.getHeight();
                int startOffset = height - AdjustPanLayoutHelper.this.startOffset();
                AdjustPanLayoutHelper adjustPanLayoutHelper = AdjustPanLayoutHelper.this;
                int i = adjustPanLayoutHelper.previousHeight;
                if (startOffset == i - adjustPanLayoutHelper.previousStartOffset || height == i || adjustPanLayoutHelper.animator != null) {
                    if (adjustPanLayoutHelper.animator == null) {
                        adjustPanLayoutHelper.previousHeight = height;
                        adjustPanLayoutHelper.previousContentHeight = adjustPanLayoutHelper.contentView.getHeight();
                        AdjustPanLayoutHelper adjustPanLayoutHelper2 = AdjustPanLayoutHelper.this;
                        adjustPanLayoutHelper2.previousStartOffset = adjustPanLayoutHelper2.startOffset();
                        AdjustPanLayoutHelper.this.usingInsetAnimator = false;
                    }
                    return true;
                } else if (!adjustPanLayoutHelper.heightAnimationEnabled() || Math.abs(AdjustPanLayoutHelper.this.previousHeight - height) < AndroidUtilities.dp(20.0f)) {
                    AdjustPanLayoutHelper adjustPanLayoutHelper3 = AdjustPanLayoutHelper.this;
                    adjustPanLayoutHelper3.previousHeight = height;
                    adjustPanLayoutHelper3.previousContentHeight = adjustPanLayoutHelper3.contentView.getHeight();
                    AdjustPanLayoutHelper adjustPanLayoutHelper4 = AdjustPanLayoutHelper.this;
                    adjustPanLayoutHelper4.previousStartOffset = adjustPanLayoutHelper4.startOffset();
                    AdjustPanLayoutHelper.this.usingInsetAnimator = false;
                    return true;
                } else {
                    AdjustPanLayoutHelper adjustPanLayoutHelper5 = AdjustPanLayoutHelper.this;
                    if (adjustPanLayoutHelper5.previousHeight != -1 && adjustPanLayoutHelper5.previousContentHeight == adjustPanLayoutHelper5.contentView.getHeight()) {
                        AdjustPanLayoutHelper adjustPanLayoutHelper6 = AdjustPanLayoutHelper.this;
                        if (height >= adjustPanLayoutHelper6.contentView.getBottom()) {
                            z = false;
                        }
                        adjustPanLayoutHelper6.isKeyboardVisible = z;
                        AdjustPanLayoutHelper adjustPanLayoutHelper7 = AdjustPanLayoutHelper.this;
                        adjustPanLayoutHelper7.animateHeight(adjustPanLayoutHelper7.previousHeight, height, adjustPanLayoutHelper7.isKeyboardVisible);
                        AdjustPanLayoutHelper adjustPanLayoutHelper8 = AdjustPanLayoutHelper.this;
                        adjustPanLayoutHelper8.previousHeight = height;
                        adjustPanLayoutHelper8.previousContentHeight = adjustPanLayoutHelper8.contentView.getHeight();
                        AdjustPanLayoutHelper adjustPanLayoutHelper9 = AdjustPanLayoutHelper.this;
                        adjustPanLayoutHelper9.previousStartOffset = adjustPanLayoutHelper9.startOffset();
                        return false;
                    }
                    AdjustPanLayoutHelper adjustPanLayoutHelper10 = AdjustPanLayoutHelper.this;
                    adjustPanLayoutHelper10.previousHeight = height;
                    adjustPanLayoutHelper10.previousContentHeight = adjustPanLayoutHelper10.contentView.getHeight();
                    AdjustPanLayoutHelper adjustPanLayoutHelper11 = AdjustPanLayoutHelper.this;
                    adjustPanLayoutHelper11.previousStartOffset = adjustPanLayoutHelper11.startOffset();
                    return false;
                }
            }
            AdjustPanLayoutHelper.this.onDetach();
            return true;
        }
    }

    public void animateHeight(int i, int i2, boolean z) {
        if (this.ignoreOnce) {
            this.ignoreOnce = false;
        } else if (!this.enabled) {
        } else {
            startTransition(i, i2, z);
            this.animator.addUpdateListener(new AdjustPanLayoutHelper$$ExternalSyntheticLambda0(this));
            int i3 = UserConfig.selectedAccount;
            this.animator.addListener(new AnonymousClass3());
            this.animator.setDuration(250L);
            this.animator.setInterpolator(keyboardInterpolator);
            this.notificationsIndex = NotificationCenter.getInstance(i3).setAnimationInProgress(this.notificationsIndex, null);
            if (this.needDelay) {
                this.needDelay = false;
                this.startAfter = SystemClock.elapsedRealtime() + 100;
                AndroidUtilities.runOnUIThread(this.delayedAnimationRunnable, 100L);
                return;
            }
            this.animator.start();
            this.startAfter = -1L;
        }
    }

    public /* synthetic */ void lambda$animateHeight$0(ValueAnimator valueAnimator) {
        if (!this.usingInsetAnimator) {
            updateTransition(((Float) valueAnimator.getAnimatedValue()).floatValue());
        }
    }

    /* renamed from: org.telegram.ui.ActionBar.AdjustPanLayoutHelper$3 */
    /* loaded from: classes3.dex */
    public class AnonymousClass3 extends AnimatorListenerAdapter {
        AnonymousClass3() {
            AdjustPanLayoutHelper.this = r1;
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator) {
            if (!AdjustPanLayoutHelper.this.usingInsetAnimator) {
                AdjustPanLayoutHelper.this.stopTransition();
            }
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:13:0x0047  */
    /* JADX WARN: Removed duplicated region for block: B:14:0x005b  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void startTransition(int i, int i2, boolean z) {
        int i3;
        ValueAnimator valueAnimator = this.animator;
        if (valueAnimator != null) {
            valueAnimator.cancel();
        }
        int startOffset = startOffset();
        getViewsToSetHeight(this.parent);
        if (this.checkHierarchyHeight) {
            ViewParent parent = this.parent.getParent();
            if (parent instanceof View) {
                i3 = ((View) parent).getHeight() - i2;
                setViewHeight(Math.max(i, i3 + i2));
                this.resizableView.requestLayout();
                onTransitionStart(z, i2);
                float f = i2 - i;
                this.keyboardSize = Math.abs(f);
                this.animationInProgress = true;
                if (i2 <= i) {
                    float f2 = f - startOffset;
                    float f3 = -f2;
                    this.parent.setTranslationY(f3);
                    onPanTranslationUpdate(f2, 1.0f, z);
                    this.from = f3;
                    this.to = 0.0f;
                    this.inverse = true;
                } else {
                    this.parent.setTranslationY(this.previousStartOffset);
                    onPanTranslationUpdate(-this.previousStartOffset, 0.0f, z);
                    this.to = -this.previousStartOffset;
                    this.from = f;
                    this.inverse = false;
                }
                this.animator = ValueAnimator.ofFloat(0.0f, 1.0f);
                this.usingInsetAnimator = false;
            }
        }
        i3 = 0;
        setViewHeight(Math.max(i, i3 + i2));
        this.resizableView.requestLayout();
        onTransitionStart(z, i2);
        float f4 = i2 - i;
        this.keyboardSize = Math.abs(f4);
        this.animationInProgress = true;
        if (i2 <= i) {
        }
        this.animator = ValueAnimator.ofFloat(0.0f, 1.0f);
        this.usingInsetAnimator = false;
    }

    public void updateTransition(float f) {
        if (this.inverse) {
            f = 1.0f - f;
        }
        float f2 = (int) ((this.from * f) + (this.to * (1.0f - f)));
        this.parent.setTranslationY(f2);
        onPanTranslationUpdate(-f2, f, this.isKeyboardVisible);
    }

    public void stopTransition() {
        ValueAnimator valueAnimator = this.animator;
        if (valueAnimator != null) {
            valueAnimator.cancel();
        }
        this.animationInProgress = false;
        this.usingInsetAnimator = false;
        NotificationCenter.getInstance(UserConfig.selectedAccount).onAnimationFinish(this.notificationsIndex);
        this.animator = null;
        setViewHeight(-1);
        this.viewsToHeightSet.clear();
        this.resizableView.requestLayout();
        boolean z = this.isKeyboardVisible;
        onPanTranslationUpdate(0.0f, z ? 1.0f : 0.0f, z);
        this.parent.setTranslationY(0.0f);
        onTransitionEnd();
    }

    public void setViewHeight(int i) {
        for (int i2 = 0; i2 < this.viewsToHeightSet.size(); i2++) {
            this.viewsToHeightSet.get(i2).getLayoutParams().height = i;
            this.viewsToHeightSet.get(i2).requestLayout();
        }
    }

    public void getViewsToSetHeight(View view) {
        this.viewsToHeightSet.clear();
        while (view != null) {
            this.viewsToHeightSet.add(view);
            if (view == this.resizableView) {
                return;
            }
            view = view.getParent() instanceof View ? (View) view.getParent() : null;
        }
    }

    public AdjustPanLayoutHelper(View view) {
        this.parent = view;
        onAttach();
    }

    public AdjustPanLayoutHelper(View view, boolean z) {
        boolean z2 = false;
        if (USE_ANDROID11_INSET_ANIMATOR && z) {
            z2 = true;
        }
        USE_ANDROID11_INSET_ANIMATOR = z2;
        this.parent = view;
        onAttach();
    }

    public void onAttach() {
        if (!SharedConfig.smoothKeyboard) {
            return;
        }
        onDetach();
        Activity activity = getActivity(this.parent.getContext());
        if (activity != null) {
            this.contentView = (ViewGroup) ((ViewGroup) activity.getWindow().getDecorView()).findViewById(16908290);
        }
        View findResizableView = findResizableView(this.parent);
        this.resizableView = findResizableView;
        if (findResizableView != null) {
            this.parentForListener = findResizableView;
            findResizableView.getViewTreeObserver().addOnPreDrawListener(this.onPreDrawListener);
        }
        if (!USE_ANDROID11_INSET_ANIMATOR || Build.VERSION.SDK_INT < 30) {
            return;
        }
        setupNewCallback();
    }

    private Activity getActivity(Context context) {
        if (context instanceof Activity) {
            return (Activity) context;
        }
        if (!(context instanceof ContextThemeWrapper)) {
            return null;
        }
        return getActivity(((ContextThemeWrapper) context).getBaseContext());
    }

    private View findResizableView(View view) {
        View view2 = this.resizableViewToSet;
        if (view2 != null) {
            return view2;
        }
        while (view != null) {
            if (!(view.getParent() instanceof DrawerLayoutContainer)) {
                if (!(view.getParent() instanceof View)) {
                    break;
                }
                view = (View) view.getParent();
            } else {
                return view;
            }
        }
        return null;
    }

    public void onDetach() {
        ValueAnimator valueAnimator = this.animator;
        if (valueAnimator != null) {
            valueAnimator.cancel();
        }
        View view = this.parentForListener;
        if (view != null) {
            view.getViewTreeObserver().removeOnPreDrawListener(this.onPreDrawListener);
            this.parentForListener = null;
        }
        View view2 = this.parent;
        if (view2 == null || !USE_ANDROID11_INSET_ANIMATOR || Build.VERSION.SDK_INT < 30) {
            return;
        }
        view2.setWindowInsetsAnimationCallback(null);
    }

    public void setEnabled(boolean z) {
        this.enabled = z;
    }

    public void ignoreOnce() {
        this.ignoreOnce = true;
    }

    public void OnPanTranslationUpdate(float f, float f2, boolean z) {
        onPanTranslationUpdate(f, f2, z);
    }

    public void OnTransitionStart(boolean z, int i) {
        onTransitionStart(z, i);
    }

    public void OnTransitionEnd() {
        onTransitionEnd();
    }

    public void setResizableView(FrameLayout frameLayout) {
        this.resizableViewToSet = frameLayout;
    }

    public boolean animationInProgress() {
        return this.animationInProgress;
    }

    public void setCheckHierarchyHeight(boolean z) {
        this.checkHierarchyHeight = z;
    }

    public void delayAnimation() {
        this.needDelay = true;
    }

    public void runDelayedAnimation() {
        AndroidUtilities.cancelRunOnUIThread(this.delayedAnimationRunnable);
        this.delayedAnimationRunnable.run();
    }

    private void setupNewCallback() {
        View view = this.resizableView;
        if (view == null) {
            return;
        }
        view.setWindowInsetsAnimationCallback(new AnonymousClass4(1));
    }

    /* renamed from: org.telegram.ui.ActionBar.AdjustPanLayoutHelper$4 */
    /* loaded from: classes3.dex */
    public class AnonymousClass4 extends WindowInsetsAnimation.Callback {
        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass4(int i) {
            super(i);
            AdjustPanLayoutHelper.this = r1;
        }

        @Override // android.view.WindowInsetsAnimation.Callback
        public WindowInsets onProgress(WindowInsets windowInsets, List<WindowInsetsAnimation> list) {
            if (AdjustPanLayoutHelper.this.animationInProgress && AndroidUtilities.screenRefreshRate >= 90.0f) {
                WindowInsetsAnimation windowInsetsAnimation = null;
                Iterator<WindowInsetsAnimation> it = list.iterator();
                while (true) {
                    if (!it.hasNext()) {
                        break;
                    }
                    WindowInsetsAnimation next = it.next();
                    if ((next.getTypeMask() & WindowInsetsCompat.Type.ime()) != 0) {
                        windowInsetsAnimation = next;
                        break;
                    }
                }
                if (windowInsetsAnimation != null) {
                    long elapsedRealtime = SystemClock.elapsedRealtime();
                    AdjustPanLayoutHelper adjustPanLayoutHelper = AdjustPanLayoutHelper.this;
                    if (elapsedRealtime >= adjustPanLayoutHelper.startAfter) {
                        adjustPanLayoutHelper.usingInsetAnimator = true;
                        AdjustPanLayoutHelper.this.updateTransition(windowInsetsAnimation.getInterpolatedFraction());
                    }
                }
            }
            return windowInsets;
        }

        @Override // android.view.WindowInsetsAnimation.Callback
        public void onEnd(WindowInsetsAnimation windowInsetsAnimation) {
            if (!AdjustPanLayoutHelper.this.animationInProgress || AndroidUtilities.screenRefreshRate < 90.0f) {
                return;
            }
            AdjustPanLayoutHelper.this.stopTransition();
        }
    }
}