package org.telegram.ui.Components;

import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.accessibility.AccessibilityNodeInfo;
import androidx.core.view.ViewCompat;
import java.util.HashMap;
import java.util.Map;

/* loaded from: classes3.dex */
public abstract class SeekBarAccessibilityDelegate extends View.AccessibilityDelegate {
    private static final CharSequence SEEK_BAR_CLASS_NAME = android.widget.SeekBar.class.getName();
    private final Map accessibilityEventRunnables = new HashMap(4);
    private final View.OnAttachStateChangeListener onAttachStateChangeListener = new View.OnAttachStateChangeListener() { // from class: org.telegram.ui.Components.SeekBarAccessibilityDelegate.1
        @Override // android.view.View.OnAttachStateChangeListener
        public void onViewAttachedToWindow(View view) {
        }

        @Override // android.view.View.OnAttachStateChangeListener
        public void onViewDetachedFromWindow(View view) {
            view.removeCallbacks((Runnable) SeekBarAccessibilityDelegate.this.accessibilityEventRunnables.remove(view));
            view.removeOnAttachStateChangeListener(this);
        }
    };

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$postAccessibilityEventRunnable$0(View view) {
        sendAccessibilityEvent(view, 4);
    }

    private void postAccessibilityEventRunnable(final View view) {
        if (ViewCompat.isAttachedToWindow(view)) {
            Runnable runnable = (Runnable) this.accessibilityEventRunnables.get(view);
            if (runnable == null) {
                Map map = this.accessibilityEventRunnables;
                Runnable runnable2 = new Runnable() { // from class: org.telegram.ui.Components.SeekBarAccessibilityDelegate$$ExternalSyntheticLambda2
                    @Override // java.lang.Runnable
                    public final void run() {
                        SeekBarAccessibilityDelegate.this.lambda$postAccessibilityEventRunnable$0(view);
                    }
                };
                map.put(view, runnable2);
                view.addOnAttachStateChangeListener(this.onAttachStateChangeListener);
                runnable = runnable2;
            } else {
                view.removeCallbacks(runnable);
            }
            view.postDelayed(runnable, 400L);
        }
    }

    protected abstract boolean canScrollBackward(View view);

    protected abstract boolean canScrollForward(View view);

    protected abstract void doScroll(View view, boolean z);

    protected CharSequence getContentDescription(View view) {
        return null;
    }

    @Override // android.view.View.AccessibilityDelegate
    public void onInitializeAccessibilityNodeInfo(View view, AccessibilityNodeInfo accessibilityNodeInfo) {
        super.onInitializeAccessibilityNodeInfo(view, accessibilityNodeInfo);
        onInitializeAccessibilityNodeInfoInternal(view, accessibilityNodeInfo);
    }

    public void onInitializeAccessibilityNodeInfoInternal(View view, AccessibilityNodeInfo accessibilityNodeInfo) {
        AccessibilityNodeInfo.AccessibilityAction accessibilityAction;
        AccessibilityNodeInfo.AccessibilityAction accessibilityAction2;
        accessibilityNodeInfo.setClassName(SEEK_BAR_CLASS_NAME);
        CharSequence contentDescription = getContentDescription(view);
        if (!TextUtils.isEmpty(contentDescription)) {
            accessibilityNodeInfo.setText(contentDescription);
        }
        if (Build.VERSION.SDK_INT >= 21) {
            if (canScrollBackward(view)) {
                accessibilityAction2 = AccessibilityNodeInfo.AccessibilityAction.ACTION_SCROLL_BACKWARD;
                accessibilityNodeInfo.addAction(accessibilityAction2);
            }
            if (canScrollForward(view)) {
                accessibilityAction = AccessibilityNodeInfo.AccessibilityAction.ACTION_SCROLL_FORWARD;
                accessibilityNodeInfo.addAction(accessibilityAction);
            }
        }
    }

    public final void onInitializeAccessibilityNodeInfoInternal(AccessibilityNodeInfo accessibilityNodeInfo) {
        onInitializeAccessibilityNodeInfoInternal(null, accessibilityNodeInfo);
    }

    @Override // android.view.View.AccessibilityDelegate
    public boolean performAccessibilityAction(View view, int i, Bundle bundle) {
        if (super.performAccessibilityAction(view, i, bundle)) {
            return true;
        }
        return performAccessibilityActionInternal(view, i, bundle);
    }

    public final boolean performAccessibilityActionInternal(int i, Bundle bundle) {
        return performAccessibilityActionInternal(null, i, bundle);
    }

    public boolean performAccessibilityActionInternal(View view, int i, Bundle bundle) {
        if (i != 4096 && i != 8192) {
            return false;
        }
        doScroll(view, i == 8192);
        if (view != null) {
            postAccessibilityEventRunnable(view);
        }
        return true;
    }
}
