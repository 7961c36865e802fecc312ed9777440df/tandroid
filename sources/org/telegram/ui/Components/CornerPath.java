package org.telegram.ui.Components;

import android.graphics.Path;
import android.graphics.RectF;
import android.os.Build;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes3.dex */
public class CornerPath extends Path {
    private static ArrayList recycled;
    private boolean isPathCreated;
    private int paddingX;
    private int paddingY;
    private final ArrayList rects;
    private float rectsUnionDiffDelta;
    protected boolean useCornerPathImplementation;

    public CornerPath() {
        this.isPathCreated = false;
        this.useCornerPathImplementation = true;
        this.rectsUnionDiffDelta = 0.0f;
        this.rects = new ArrayList(1);
    }

    public CornerPath(int i) {
        this.isPathCreated = false;
        this.useCornerPathImplementation = true;
        this.rectsUnionDiffDelta = 0.0f;
        this.rects = new ArrayList(i);
    }

    private void createClosedPathsFromRects(List list) {
        if (list.isEmpty()) {
            return;
        }
        boolean z = false;
        if (list.size() == 1) {
            super.addRect(((RectF) list.get(0)).left - this.paddingX, ((RectF) list.get(0)).top - this.paddingY, ((RectF) list.get(0)).right + this.paddingX, ((RectF) list.get(0)).bottom + this.paddingY, Path.Direction.CW);
            return;
        }
        RectF rectF = (RectF) list.get(0);
        int size = list.size() - 1;
        super.moveTo(rectF.left - this.paddingX, rectF.top - this.paddingY);
        for (int i = 1; i < list.size(); i++) {
            RectF rectF2 = (RectF) list.get(i);
            if (rectF2.width() != 0.0f) {
                float f = rectF.bottom;
                float f2 = rectF2.top;
                if (f >= f2) {
                    float f3 = rectF.left;
                    if (f3 <= rectF2.right) {
                        float f4 = rectF.right;
                        float f5 = rectF2.left;
                        if (f4 >= f5) {
                            if (f3 != f5) {
                                super.lineTo(f3 - this.paddingX, f2);
                                super.lineTo(rectF2.left - this.paddingX, rectF2.top);
                            }
                            rectF = rectF2;
                        }
                    }
                }
                size = i;
                z = true;
                break;
            }
        }
        super.lineTo(rectF.left - this.paddingX, rectF.bottom + this.paddingY);
        super.lineTo(rectF.right + this.paddingX, rectF.bottom + this.paddingY);
        for (int i2 = size - 1; i2 >= 0; i2--) {
            RectF rectF3 = (RectF) list.get(i2);
            if (rectF3.width() != 0.0f) {
                float f6 = rectF.right;
                if (f6 != rectF3.right) {
                    super.lineTo(f6 + this.paddingX, rectF.top);
                    super.lineTo(rectF3.right + this.paddingX, rectF.top);
                }
                rectF = rectF3;
            }
        }
        super.lineTo(rectF.right + this.paddingX, rectF.top - this.paddingY);
        super.close();
        if (z) {
            createClosedPathsFromRects(list.subList(size, list.size()));
        }
    }

    private void resetRects() {
        if (recycled == null) {
            recycled = new ArrayList(this.rects.size());
        }
        recycled.addAll(this.rects);
        this.rects.clear();
        this.isPathCreated = false;
    }

    @Override // android.graphics.Path
    public void addRect(float f, float f2, float f3, float f4, Path.Direction direction) {
        if (Build.VERSION.SDK_INT < 34 || !this.useCornerPathImplementation) {
            super.addRect(f, f2, f3, f4, direction);
            return;
        }
        if (this.rects.size() > 0) {
            if (((RectF) this.rects.get(r7.size() - 1)).contains(f, f2, f3, f4)) {
                return;
            }
        }
        if (this.rects.size() > 0) {
            if (Math.abs(f2 - ((RectF) this.rects.get(r7.size() - 1)).top) <= this.rectsUnionDiffDelta) {
                if (Math.abs(f4 - ((RectF) this.rects.get(r7.size() - 1)).bottom) <= this.rectsUnionDiffDelta) {
                    ((RectF) this.rects.get(r7.size() - 1)).union(f, f2, f3, f4);
                    this.isPathCreated = false;
                }
            }
        }
        ArrayList arrayList = recycled;
        RectF rectF = (arrayList == null || arrayList.size() <= 0) ? new RectF() : (RectF) recycled.remove(0);
        rectF.set(f, f2, f3, f4);
        this.rects.add(rectF);
        this.isPathCreated = false;
    }

    @Override // android.graphics.Path
    public void addRect(RectF rectF, Path.Direction direction) {
        if (Build.VERSION.SDK_INT < 34 || !this.useCornerPathImplementation) {
            super.addRect(rectF.left, rectF.top, rectF.right, rectF.bottom, direction);
            return;
        }
        if (this.rects.size() > 0) {
            if (((RectF) this.rects.get(r9.size() - 1)).contains(rectF)) {
                return;
            }
        }
        if (this.rects.size() > 0) {
            if (Math.abs(rectF.top - ((RectF) this.rects.get(r1.size() - 1)).top) <= this.rectsUnionDiffDelta) {
                if (Math.abs(rectF.bottom - ((RectF) this.rects.get(r1.size() - 1)).bottom) <= this.rectsUnionDiffDelta) {
                    ((RectF) this.rects.get(r9.size() - 1)).union(rectF);
                    this.isPathCreated = false;
                }
            }
        }
        ArrayList arrayList = recycled;
        RectF rectF2 = (arrayList == null || arrayList.size() <= 0) ? new RectF() : (RectF) recycled.remove(0);
        rectF2.set(rectF);
        this.rects.add(rectF2);
        this.isPathCreated = false;
    }

    public void closeRects() {
        if (Build.VERSION.SDK_INT < 34 || !this.useCornerPathImplementation || this.isPathCreated) {
            return;
        }
        createClosedPathsFromRects(this.rects);
        this.isPathCreated = true;
    }

    @Override // android.graphics.Path
    public void reset() {
        super.reset();
        if (Build.VERSION.SDK_INT < 34 || !this.useCornerPathImplementation) {
            return;
        }
        resetRects();
    }

    @Override // android.graphics.Path
    public void rewind() {
        super.rewind();
        if (Build.VERSION.SDK_INT < 34 || !this.useCornerPathImplementation) {
            return;
        }
        resetRects();
    }

    public void setRectsUnionDiffDelta(float f) {
        this.rectsUnionDiffDelta = f;
    }

    public void setUseCornerPathImplementation(boolean z) {
        this.useCornerPathImplementation = z;
    }
}
