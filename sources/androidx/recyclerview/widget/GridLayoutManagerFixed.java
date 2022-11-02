package androidx.recyclerview.widget;

import android.content.Context;
import android.graphics.Rect;
import android.view.View;
import android.view.ViewGroup;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.Arrays;
/* loaded from: classes.dex */
public class GridLayoutManagerFixed extends GridLayoutManager {
    private ArrayList<View> additionalViews = new ArrayList<>(4);
    private boolean canScrollVertically = true;

    protected boolean hasSiblingChild(int i) {
        throw null;
    }

    public boolean shouldLayoutChildFromOpositeSide(View view) {
        throw null;
    }

    public GridLayoutManagerFixed(Context context, int i, int i2, boolean z) {
        super(context, i, i2, z);
    }

    public void setCanScrollVertically(boolean z) {
        this.canScrollVertically = z;
    }

    @Override // androidx.recyclerview.widget.LinearLayoutManager, androidx.recyclerview.widget.RecyclerView.LayoutManager
    public boolean canScrollVertically() {
        return this.canScrollVertically;
    }

    @Override // androidx.recyclerview.widget.LinearLayoutManager
    protected void recycleViewsFromStart(RecyclerView.Recycler recycler, int i, int i2) {
        if (i < 0) {
            return;
        }
        int childCount = getChildCount();
        if (!this.mShouldReverseLayout) {
            for (int i3 = 0; i3 < childCount; i3++) {
                View childAt = getChildAt(i3);
                if (this.mOrientationHelper.getDecoratedEnd(childAt) > i || this.mOrientationHelper.getTransformedEndWithDecoration(childAt) > i) {
                    recycleChildren(recycler, 0, i3);
                    return;
                }
            }
            return;
        }
        int i4 = childCount - 1;
        for (int i5 = i4; i5 >= 0; i5--) {
            View childAt2 = getChildAt(i5);
            if (childAt2.getBottom() + ((ViewGroup.MarginLayoutParams) ((RecyclerView.LayoutParams) childAt2.getLayoutParams())).bottomMargin > i || childAt2.getTop() + childAt2.getHeight() > i) {
                recycleChildren(recycler, i4, i5);
                return;
            }
        }
    }

    @Override // androidx.recyclerview.widget.GridLayoutManager
    protected int[] calculateItemBorders(int[] iArr, int i, int i2) {
        if (iArr == null || iArr.length != i + 1 || iArr[iArr.length - 1] != i2) {
            iArr = new int[i + 1];
        }
        iArr[0] = 0;
        for (int i3 = 1; i3 <= i; i3++) {
            iArr[i3] = (int) Math.ceil((i3 / i) * i2);
        }
        return iArr;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // androidx.recyclerview.widget.GridLayoutManager
    public void measureChild(View view, int i, boolean z) {
        GridLayoutManager.LayoutParams layoutParams = (GridLayoutManager.LayoutParams) view.getLayoutParams();
        Rect rect = layoutParams.mDecorInsets;
        int i2 = rect.top + rect.bottom + ((ViewGroup.MarginLayoutParams) layoutParams).topMargin + ((ViewGroup.MarginLayoutParams) layoutParams).bottomMargin;
        measureChildWithDecorationsAndMargin(view, RecyclerView.LayoutManager.getChildMeasureSpec(this.mCachedBorders[layoutParams.mSpanSize], i, rect.left + rect.right + ((ViewGroup.MarginLayoutParams) layoutParams).leftMargin + ((ViewGroup.MarginLayoutParams) layoutParams).rightMargin, ((ViewGroup.MarginLayoutParams) layoutParams).width, false), RecyclerView.LayoutManager.getChildMeasureSpec(this.mOrientationHelper.getTotalSpace(), getHeightMode(), i2, ((ViewGroup.MarginLayoutParams) layoutParams).height, true), z);
    }

    /* JADX WARN: Code restructure failed: missing block: B:144:0x00d5, code lost:
        r28.mFinished = r13;
     */
    /* JADX WARN: Code restructure failed: missing block: B:145:0x00d7, code lost:
        return;
     */
    /* JADX WARN: Code restructure failed: missing block: B:90:0x0187, code lost:
        if (r27.mLayoutDirection != (-1)) goto L116;
     */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r12v0 */
    /* JADX WARN: Type inference failed for: r12v1, types: [boolean, int] */
    /* JADX WARN: Type inference failed for: r12v5 */
    @Override // androidx.recyclerview.widget.GridLayoutManager, androidx.recyclerview.widget.LinearLayoutManager
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    void layoutChunk(RecyclerView.Recycler recycler, RecyclerView.State state, LinearLayoutManager.LayoutState layoutState, LinearLayoutManager.LayoutChunkResult layoutChunkResult) {
        int i;
        int i2;
        int i3;
        int i4;
        int i5;
        int width;
        int i6;
        int i7;
        View next;
        RecyclerView.Recycler recycler2 = recycler;
        int modeInOther = this.mOrientationHelper.getModeInOther();
        ?? r12 = 0;
        boolean z = true;
        boolean z2 = layoutState.mItemDirection == 1;
        layoutChunkResult.mConsumed = 0;
        int i8 = layoutState.mCurrentPosition;
        int i9 = -1;
        if (layoutState.mLayoutDirection != -1 && hasSiblingChild(i8) && findViewByPosition(layoutState.mCurrentPosition + 1) == null) {
            if (hasSiblingChild(layoutState.mCurrentPosition + 1)) {
                layoutState.mCurrentPosition += 3;
            } else {
                layoutState.mCurrentPosition += 2;
            }
            int i10 = layoutState.mCurrentPosition;
            for (int i11 = i10; i11 > i8; i11--) {
                View next2 = layoutState.next(recycler2);
                if (next2 != null) {
                    this.additionalViews.add(next2);
                    if (i11 != i10) {
                        calculateItemDecorationsForChild(next2, this.mDecorInsets);
                        measureChild(next2, modeInOther, false);
                        int decoratedMeasurement = this.mOrientationHelper.getDecoratedMeasurement(next2);
                        layoutState.mOffset -= decoratedMeasurement;
                        layoutState.mAvailable += decoratedMeasurement;
                    }
                }
            }
            layoutState.mCurrentPosition = i10;
        }
        boolean z3 = true;
        while (z3) {
            int i12 = this.mSpanCount;
            boolean isEmpty = this.additionalViews.isEmpty();
            boolean z4 = z ? 1 : 0;
            char c = z ? 1 : 0;
            boolean z5 = isEmpty ^ z4;
            int i13 = 0;
            while (i13 < this.mSpanCount && layoutState.hasMore(state) && i12 > 0) {
                int i14 = layoutState.mCurrentPosition;
                i12 -= getSpanSize(recycler2, state, i14);
                if (i12 < 0) {
                    break;
                }
                if (!this.additionalViews.isEmpty()) {
                    next = this.additionalViews.get(r12);
                    this.additionalViews.remove((int) r12);
                    int i15 = layoutState.mCurrentPosition;
                    int i16 = z ? 1 : 0;
                    int i17 = z ? 1 : 0;
                    layoutState.mCurrentPosition = i15 - i16;
                } else {
                    next = layoutState.next(recycler2);
                }
                if (next == null) {
                    break;
                }
                this.mSet[i13] = next;
                i13++;
                if (layoutState.mLayoutDirection == i9 && i12 <= 0 && hasSiblingChild(i14)) {
                    z5 = true;
                }
            }
            float f = 0.0f;
            assignSpans(recycler2, state, i13, z2);
            int i18 = 0;
            for (int i19 = 0; i19 < i13; i19++) {
                View view = this.mSet[i19];
                if (layoutState.mScrapList == null) {
                    if (z2) {
                        addView(view);
                    } else {
                        addView(view, r12);
                    }
                } else if (z2) {
                    addDisappearingView(view);
                } else {
                    int i20 = r12 == true ? 1 : 0;
                    int i21 = r12 == true ? 1 : 0;
                    addDisappearingView(view, i20);
                }
                calculateItemDecorationsForChild(view, this.mDecorInsets);
                measureChild(view, modeInOther, r12);
                int decoratedMeasurement2 = this.mOrientationHelper.getDecoratedMeasurement(view);
                if (decoratedMeasurement2 > i18) {
                    i18 = decoratedMeasurement2;
                }
                float decoratedMeasurementInOther = (this.mOrientationHelper.getDecoratedMeasurementInOther(view) * 1.0f) / ((GridLayoutManager.LayoutParams) view.getLayoutParams()).mSpanSize;
                if (decoratedMeasurementInOther > f) {
                    f = decoratedMeasurementInOther;
                }
            }
            for (int i22 = 0; i22 < i13; i22++) {
                View view2 = this.mSet[i22];
                if (this.mOrientationHelper.getDecoratedMeasurement(view2) != i18) {
                    GridLayoutManager.LayoutParams layoutParams = (GridLayoutManager.LayoutParams) view2.getLayoutParams();
                    Rect rect = layoutParams.mDecorInsets;
                    measureChildWithDecorationsAndMargin(view2, RecyclerView.LayoutManager.getChildMeasureSpec(this.mCachedBorders[layoutParams.mSpanSize], 1073741824, rect.left + rect.right + ((ViewGroup.MarginLayoutParams) layoutParams).leftMargin + ((ViewGroup.MarginLayoutParams) layoutParams).rightMargin, ((ViewGroup.MarginLayoutParams) layoutParams).width, false), View.MeasureSpec.makeMeasureSpec(i18 - (((rect.top + rect.bottom) + ((ViewGroup.MarginLayoutParams) layoutParams).topMargin) + ((ViewGroup.MarginLayoutParams) layoutParams).bottomMargin), 1073741824), true);
                }
            }
            boolean shouldLayoutChildFromOpositeSide = shouldLayoutChildFromOpositeSide(this.mSet[0]);
            if (shouldLayoutChildFromOpositeSide) {
                i = -1;
            } else {
                i = -1;
            }
            if (shouldLayoutChildFromOpositeSide || layoutState.mLayoutDirection != 1) {
                i2 = i18;
                if (layoutState.mLayoutDirection == -1) {
                    int i23 = layoutState.mOffset - layoutChunkResult.mConsumed;
                    i5 = getWidth();
                    i3 = i23;
                    i4 = i23 - i2;
                } else {
                    int i24 = layoutChunkResult.mConsumed + layoutState.mOffset;
                    i3 = i24 + i2;
                    i4 = i24;
                    i5 = 0;
                }
                int i25 = 0;
                while (i25 < i13) {
                    View view3 = this.mSet[i25];
                    GridLayoutManager.LayoutParams layoutParams2 = (GridLayoutManager.LayoutParams) view3.getLayoutParams();
                    int decoratedMeasurementInOther2 = this.mOrientationHelper.getDecoratedMeasurementInOther(view3);
                    if (layoutState.mLayoutDirection == -1) {
                        i5 -= decoratedMeasurementInOther2;
                    }
                    int i26 = i5;
                    int i27 = i26 + decoratedMeasurementInOther2;
                    int i28 = i13;
                    layoutDecoratedWithMargins(view3, i26, i4, i27, i3);
                    i5 = layoutState.mLayoutDirection != -1 ? i27 : i26;
                    if (layoutParams2.isItemRemoved() || layoutParams2.isItemChanged()) {
                        layoutChunkResult.mIgnoreConsumed = true;
                    }
                    layoutChunkResult.mFocusable |= view3.hasFocusable();
                    i25++;
                    i13 = i28;
                }
                layoutChunkResult.mConsumed += i2;
                Arrays.fill(this.mSet, (Object) null);
                recycler2 = recycler;
                z3 = z5;
                r12 = 0;
                z = true;
                i9 = -1;
            }
            if (layoutState.mLayoutDirection == i) {
                int i29 = layoutState.mOffset - layoutChunkResult.mConsumed;
                i6 = i29;
                i7 = i29 - i18;
                width = 0;
            } else {
                int i30 = layoutChunkResult.mConsumed + layoutState.mOffset;
                width = getWidth();
                i6 = i30 + i18;
                i7 = i30;
            }
            int i31 = i13 - 1;
            while (i31 >= 0) {
                View view4 = this.mSet[i31];
                GridLayoutManager.LayoutParams layoutParams3 = (GridLayoutManager.LayoutParams) view4.getLayoutParams();
                int decoratedMeasurementInOther3 = this.mOrientationHelper.getDecoratedMeasurementInOther(view4);
                if (layoutState.mLayoutDirection == 1) {
                    width -= decoratedMeasurementInOther3;
                }
                int i32 = width;
                int i33 = i32 + decoratedMeasurementInOther3;
                int i34 = i18;
                layoutDecoratedWithMargins(view4, i32, i7, i33, i6);
                width = layoutState.mLayoutDirection == -1 ? i33 : i32;
                if (layoutParams3.isItemRemoved() || layoutParams3.isItemChanged()) {
                    layoutChunkResult.mIgnoreConsumed = true;
                }
                layoutChunkResult.mFocusable |= view4.hasFocusable();
                i31--;
                i18 = i34;
            }
            i2 = i18;
            layoutChunkResult.mConsumed += i2;
            Arrays.fill(this.mSet, (Object) null);
            recycler2 = recycler;
            z3 = z5;
            r12 = 0;
            z = true;
            i9 = -1;
        }
    }
}