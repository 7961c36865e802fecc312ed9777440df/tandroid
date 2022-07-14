package com.google.zxing.common.detector;

import com.google.zxing.NotFoundException;
import com.google.zxing.ResultPoint;
import com.google.zxing.common.BitMatrix;
/* loaded from: classes3.dex */
public final class WhiteRectangleDetector {
    private static final int CORR = 1;
    private static final int INIT_SIZE = 10;
    private final int downInit;
    private final int height;
    private final BitMatrix image;
    private final int leftInit;
    private final int rightInit;
    private final int upInit;
    private final int width;

    public WhiteRectangleDetector(BitMatrix image) throws NotFoundException {
        this(image, 10, image.getWidth() / 2, image.getHeight() / 2);
    }

    public WhiteRectangleDetector(BitMatrix image, int initSize, int x, int y) throws NotFoundException {
        this.image = image;
        int height = image.getHeight();
        this.height = height;
        int width = image.getWidth();
        this.width = width;
        int halfsize = initSize / 2;
        int i = x - halfsize;
        this.leftInit = i;
        int i2 = x + halfsize;
        this.rightInit = i2;
        int i3 = y - halfsize;
        this.upInit = i3;
        int i4 = y + halfsize;
        this.downInit = i4;
        if (i3 < 0 || i < 0 || i4 >= height || i2 >= width) {
            throw NotFoundException.getNotFoundInstance();
        }
    }

    public ResultPoint[] detect() throws NotFoundException {
        int left = this.leftInit;
        int right = this.rightInit;
        int up = this.upInit;
        int down = this.downInit;
        boolean sizeExceeded = false;
        boolean aBlackPointFoundOnBorder = true;
        boolean atLeastOneBlackPointFoundOnRight = false;
        boolean atLeastOneBlackPointFoundOnBottom = false;
        boolean atLeastOneBlackPointFoundOnLeft = false;
        boolean atLeastOneBlackPointFoundOnTop = false;
        while (true) {
            if (!aBlackPointFoundOnBorder) {
                break;
            }
            aBlackPointFoundOnBorder = false;
            boolean rightBorderNotWhite = true;
            while (true) {
                if ((rightBorderNotWhite || !atLeastOneBlackPointFoundOnRight) && right < this.width) {
                    rightBorderNotWhite = containsBlackPoint(up, down, right, false);
                    if (rightBorderNotWhite) {
                        right++;
                        aBlackPointFoundOnBorder = true;
                        atLeastOneBlackPointFoundOnRight = true;
                    } else if (!atLeastOneBlackPointFoundOnRight) {
                        right++;
                    }
                }
            }
            if (right >= this.width) {
                sizeExceeded = true;
                break;
            }
            boolean bottomBorderNotWhite = true;
            while (true) {
                if ((bottomBorderNotWhite || !atLeastOneBlackPointFoundOnBottom) && down < this.height) {
                    bottomBorderNotWhite = containsBlackPoint(left, right, down, true);
                    if (bottomBorderNotWhite) {
                        down++;
                        aBlackPointFoundOnBorder = true;
                        atLeastOneBlackPointFoundOnBottom = true;
                    } else if (!atLeastOneBlackPointFoundOnBottom) {
                        down++;
                    }
                }
            }
            if (down >= this.height) {
                sizeExceeded = true;
                break;
            }
            boolean leftBorderNotWhite = true;
            while (true) {
                if ((leftBorderNotWhite || !atLeastOneBlackPointFoundOnLeft) && left >= 0) {
                    leftBorderNotWhite = containsBlackPoint(up, down, left, false);
                    if (leftBorderNotWhite) {
                        left--;
                        aBlackPointFoundOnBorder = true;
                        atLeastOneBlackPointFoundOnLeft = true;
                    } else if (!atLeastOneBlackPointFoundOnLeft) {
                        left--;
                    }
                }
            }
            if (left < 0) {
                sizeExceeded = true;
                break;
            }
            boolean topBorderNotWhite = true;
            while (true) {
                if ((topBorderNotWhite || !atLeastOneBlackPointFoundOnTop) && up >= 0) {
                    topBorderNotWhite = containsBlackPoint(left, right, up, true);
                    if (topBorderNotWhite) {
                        up--;
                        aBlackPointFoundOnBorder = true;
                        atLeastOneBlackPointFoundOnTop = true;
                    } else if (!atLeastOneBlackPointFoundOnTop) {
                        up--;
                    }
                }
            }
            if (up < 0) {
                sizeExceeded = true;
                break;
            }
        }
        if (!sizeExceeded) {
            int maxSize = right - left;
            ResultPoint z = null;
            int i = 1;
            while (z == null && i < maxSize) {
                z = getBlackPointOnSegment(left, down - i, left + i, down);
                i++;
                sizeExceeded = sizeExceeded;
                aBlackPointFoundOnBorder = aBlackPointFoundOnBorder;
            }
            if (z == null) {
                throw NotFoundException.getNotFoundInstance();
            }
            ResultPoint t = null;
            int i2 = 1;
            while (t == null && i2 < maxSize) {
                t = getBlackPointOnSegment(left, up + i2, left + i2, up);
                i2++;
                left = left;
            }
            if (t == null) {
                throw NotFoundException.getNotFoundInstance();
            }
            ResultPoint x = null;
            int i3 = 1;
            while (x == null && i3 < maxSize) {
                x = getBlackPointOnSegment(right, up + i3, right - i3, up);
                i3++;
                atLeastOneBlackPointFoundOnRight = atLeastOneBlackPointFoundOnRight;
            }
            if (x == null) {
                throw NotFoundException.getNotFoundInstance();
            }
            ResultPoint y = null;
            int i4 = 1;
            while (y == null && i4 < maxSize) {
                y = getBlackPointOnSegment(right, down - i4, right - i4, down);
                i4++;
                right = right;
            }
            if (y == null) {
                throw NotFoundException.getNotFoundInstance();
            }
            return centerEdges(y, z, x, t);
        }
        throw NotFoundException.getNotFoundInstance();
    }

    private ResultPoint getBlackPointOnSegment(float aX, float aY, float bX, float bY) {
        int dist = MathUtils.round(MathUtils.distance(aX, aY, bX, bY));
        float xStep = (bX - aX) / dist;
        float yStep = (bY - aY) / dist;
        for (int i = 0; i < dist; i++) {
            int x = MathUtils.round((i * xStep) + aX);
            int y = MathUtils.round((i * yStep) + aY);
            if (this.image.get(x, y)) {
                return new ResultPoint(x, y);
            }
        }
        return null;
    }

    private ResultPoint[] centerEdges(ResultPoint y, ResultPoint z, ResultPoint x, ResultPoint t) {
        float yi = y.getX();
        float yj = y.getY();
        float zi = z.getX();
        float zj = z.getY();
        float xi = x.getX();
        float xj = x.getY();
        float ti = t.getX();
        float tj = t.getY();
        return yi < ((float) this.width) / 2.0f ? new ResultPoint[]{new ResultPoint(ti - 1.0f, tj + 1.0f), new ResultPoint(zi + 1.0f, zj + 1.0f), new ResultPoint(xi - 1.0f, xj - 1.0f), new ResultPoint(yi + 1.0f, yj - 1.0f)} : new ResultPoint[]{new ResultPoint(ti + 1.0f, tj + 1.0f), new ResultPoint(zi + 1.0f, zj - 1.0f), new ResultPoint(xi - 1.0f, xj + 1.0f), new ResultPoint(yi - 1.0f, yj - 1.0f)};
    }

    private boolean containsBlackPoint(int a, int b, int fixed, boolean horizontal) {
        if (horizontal) {
            for (int x = a; x <= b; x++) {
                if (this.image.get(x, fixed)) {
                    return true;
                }
            }
            return false;
        }
        for (int y = a; y <= b; y++) {
            if (this.image.get(fixed, y)) {
                return true;
            }
        }
        return false;
    }
}
