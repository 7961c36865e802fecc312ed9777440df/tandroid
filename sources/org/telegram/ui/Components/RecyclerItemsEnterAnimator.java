package org.telegram.ui.Components;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.util.Property;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewTreeObserver;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.HashSet;
import org.telegram.ui.Components.RecyclerItemsEnterAnimator;

/* loaded from: classes3.dex */
public class RecyclerItemsEnterAnimator {
    boolean alwaysCheckItemsAlpha;
    boolean invalidateAlpha;
    private final RecyclerListView listView;
    private final SparseArray listAlphaItems = new SparseArray();
    HashSet ignoreView = new HashSet();
    public boolean animateAlphaProgressView = true;
    ArrayList currentAnimations = new ArrayList();
    ArrayList preDrawListeners = new ArrayList();

    class 2 implements ViewTreeObserver.OnPreDrawListener {
        final /* synthetic */ int val$finalFrom;
        final /* synthetic */ View val$finalProgressView;

        2(View view, int i) {
            this.val$finalProgressView = view;
            this.val$finalFrom = i;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onPreDraw$0(int i, ValueAnimator valueAnimator) {
            RecyclerItemsEnterAnimator.this.listAlphaItems.put(i, (Float) valueAnimator.getAnimatedValue());
            RecyclerItemsEnterAnimator recyclerItemsEnterAnimator = RecyclerItemsEnterAnimator.this;
            recyclerItemsEnterAnimator.invalidateAlpha = true;
            recyclerItemsEnterAnimator.listView.invalidate();
        }

        @Override // android.view.ViewTreeObserver.OnPreDrawListener
        public boolean onPreDraw() {
            RecyclerItemsEnterAnimator.this.listView.getViewTreeObserver().removeOnPreDrawListener(this);
            RecyclerItemsEnterAnimator.this.preDrawListeners.remove(this);
            int childCount = RecyclerItemsEnterAnimator.this.listView.getChildCount();
            final AnimatorSet animatorSet = new AnimatorSet();
            for (int i = 0; i < childCount; i++) {
                View childAt = RecyclerItemsEnterAnimator.this.listView.getChildAt(i);
                final int childAdapterPosition = RecyclerItemsEnterAnimator.this.listView.getChildAdapterPosition(childAt);
                if (childAt != this.val$finalProgressView && childAdapterPosition >= this.val$finalFrom - 1 && RecyclerItemsEnterAnimator.this.listAlphaItems.get(childAdapterPosition, null) == null) {
                    RecyclerItemsEnterAnimator.this.listAlphaItems.put(childAdapterPosition, Float.valueOf(0.0f));
                    RecyclerItemsEnterAnimator recyclerItemsEnterAnimator = RecyclerItemsEnterAnimator.this;
                    recyclerItemsEnterAnimator.invalidateAlpha = true;
                    recyclerItemsEnterAnimator.listView.invalidate();
                    ValueAnimator ofFloat = ValueAnimator.ofFloat(0.0f, 1.0f);
                    ofFloat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: org.telegram.ui.Components.RecyclerItemsEnterAnimator$2$$ExternalSyntheticLambda0
                        @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                        public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                            RecyclerItemsEnterAnimator.2.this.lambda$onPreDraw$0(childAdapterPosition, valueAnimator);
                        }
                    });
                    ofFloat.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.Components.RecyclerItemsEnterAnimator.2.1
                        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                        public void onAnimationEnd(Animator animator) {
                            RecyclerItemsEnterAnimator.this.listAlphaItems.remove(childAdapterPosition);
                            RecyclerItemsEnterAnimator recyclerItemsEnterAnimator2 = RecyclerItemsEnterAnimator.this;
                            recyclerItemsEnterAnimator2.invalidateAlpha = true;
                            recyclerItemsEnterAnimator2.listView.invalidate();
                        }
                    });
                    ofFloat.setStartDelay((int) ((Math.min(RecyclerItemsEnterAnimator.this.listView.getMeasuredHeight(), Math.max(0, childAt.getTop())) / RecyclerItemsEnterAnimator.this.listView.getMeasuredHeight()) * 100.0f));
                    ofFloat.setDuration(200L);
                    animatorSet.playTogether(ofFloat);
                }
            }
            RecyclerItemsEnterAnimator.this.currentAnimations.add(animatorSet);
            animatorSet.start();
            animatorSet.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.Components.RecyclerItemsEnterAnimator.2.2
                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public void onAnimationEnd(Animator animator) {
                    super.onAnimationEnd(animator);
                    RecyclerItemsEnterAnimator.this.currentAnimations.remove(animatorSet);
                    if (RecyclerItemsEnterAnimator.this.currentAnimations.isEmpty()) {
                        RecyclerItemsEnterAnimator.this.listAlphaItems.clear();
                        RecyclerItemsEnterAnimator recyclerItemsEnterAnimator2 = RecyclerItemsEnterAnimator.this;
                        recyclerItemsEnterAnimator2.invalidateAlpha = true;
                        recyclerItemsEnterAnimator2.listView.invalidate();
                    }
                }
            });
            return false;
        }
    }

    public RecyclerItemsEnterAnimator(RecyclerListView recyclerListView, boolean z) {
        this.listView = recyclerListView;
        this.alwaysCheckItemsAlpha = z;
        recyclerListView.setItemsEnterAnimator(this);
    }

    public void cancel() {
        if (!this.currentAnimations.isEmpty()) {
            ArrayList arrayList = new ArrayList(this.currentAnimations);
            for (int i = 0; i < arrayList.size(); i++) {
                ((AnimatorSet) arrayList.get(i)).end();
                ((AnimatorSet) arrayList.get(i)).cancel();
            }
        }
        this.currentAnimations.clear();
        for (int i2 = 0; i2 < this.preDrawListeners.size(); i2++) {
            this.listView.getViewTreeObserver().removeOnPreDrawListener((ViewTreeObserver.OnPreDrawListener) this.preDrawListeners.get(i2));
        }
        this.preDrawListeners.clear();
        this.listAlphaItems.clear();
        this.listView.invalidate();
        this.invalidateAlpha = true;
    }

    public void dispatchDraw() {
        if (this.invalidateAlpha || this.alwaysCheckItemsAlpha) {
            for (int i = 0; i < this.listView.getChildCount(); i++) {
                View childAt = this.listView.getChildAt(i);
                int childAdapterPosition = this.listView.getChildAdapterPosition(childAt);
                if (childAdapterPosition >= 0 && !this.ignoreView.contains(childAt)) {
                    Float f = (Float) this.listAlphaItems.get(childAdapterPosition, null);
                    childAt.setAlpha(f == null ? 1.0f : f.floatValue());
                }
            }
            this.invalidateAlpha = false;
        }
    }

    public View getProgressView() {
        int childCount = this.listView.getChildCount();
        View view = null;
        for (int i = 0; i < childCount; i++) {
            View childAt = this.listView.getChildAt(i);
            if (this.listView.getChildAdapterPosition(childAt) >= 0 && (childAt instanceof FlickerLoadingView)) {
                view = childAt;
            }
        }
        return view;
    }

    public void onDetached() {
        cancel();
    }

    public void showItemsAnimated(int i) {
        final View progressView = getProgressView();
        final RecyclerView.LayoutManager layoutManager = this.listView.getLayoutManager();
        if (progressView != null && layoutManager != null) {
            this.listView.removeView(progressView);
            this.ignoreView.add(progressView);
            this.listView.addView(progressView);
            layoutManager.ignoreView(progressView);
            Animator ofFloat = this.animateAlphaProgressView ? ObjectAnimator.ofFloat(progressView, (Property<View, Float>) View.ALPHA, progressView.getAlpha(), 0.0f) : ValueAnimator.ofFloat(0.0f, 1.0f);
            ofFloat.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.Components.RecyclerItemsEnterAnimator.1
                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public void onAnimationEnd(Animator animator) {
                    progressView.setAlpha(1.0f);
                    layoutManager.stopIgnoringView(progressView);
                    RecyclerItemsEnterAnimator.this.ignoreView.remove(progressView);
                    RecyclerItemsEnterAnimator.this.listView.removeView(progressView);
                }
            });
            ofFloat.start();
            i--;
        }
        2 r0 = new 2(progressView, i);
        this.preDrawListeners.add(r0);
        this.listView.getViewTreeObserver().addOnPreDrawListener(r0);
    }
}
