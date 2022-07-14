package org.telegram.messenger;
/* loaded from: classes4.dex */
public class SegmentTree {
    private int[] array;
    private Node[] heap;

    public SegmentTree(int[] array) {
        this.array = array;
        if (array.length < 30) {
            return;
        }
        int size = (int) (Math.pow(2.0d, Math.floor((Math.log(array.length) / Math.log(2.0d)) + 1.0d)) * 2.0d);
        this.heap = new Node[size];
        build(1, 0, array.length);
    }

    private void build(int v, int from, int size) {
        this.heap[v] = new Node();
        this.heap[v].from = from;
        this.heap[v].to = (from + size) - 1;
        if (size == 1) {
            this.heap[v].sum = this.array[from];
            this.heap[v].max = this.array[from];
            this.heap[v].min = this.array[from];
            return;
        }
        build(v * 2, from, size / 2);
        build((v * 2) + 1, (size / 2) + from, size - (size / 2));
        Node[] nodeArr = this.heap;
        nodeArr[v].sum = nodeArr[v * 2].sum + this.heap[(v * 2) + 1].sum;
        Node[] nodeArr2 = this.heap;
        nodeArr2[v].max = Math.max(nodeArr2[v * 2].max, this.heap[(v * 2) + 1].max);
        Node[] nodeArr3 = this.heap;
        nodeArr3[v].min = Math.min(nodeArr3[v * 2].min, this.heap[(v * 2) + 1].min);
    }

    public int rMaxQ(int from, int to) {
        int[] iArr = this.array;
        if (iArr.length < 30) {
            int max = Integer.MIN_VALUE;
            if (from < 0) {
                from = 0;
            }
            if (to > iArr.length - 1) {
                to = iArr.length - 1;
            }
            for (int i = from; i <= to; i++) {
                int[] iArr2 = this.array;
                if (iArr2[i] > max) {
                    max = iArr2[i];
                }
            }
            return max;
        }
        return rMaxQ(1, from, to);
    }

    private int rMaxQ(int v, int from, int to) {
        Node n = this.heap[v];
        if (n.pendingVal != null && contains(n.from, n.to, from, to)) {
            return n.pendingVal.intValue();
        }
        if (contains(from, to, n.from, n.to)) {
            return this.heap[v].max;
        }
        if (intersects(from, to, n.from, n.to)) {
            propagate(v);
            int leftMin = rMaxQ(v * 2, from, to);
            int rightMin = rMaxQ((v * 2) + 1, from, to);
            return Math.max(leftMin, rightMin);
        }
        return 0;
    }

    public int rMinQ(int from, int to) {
        int[] iArr = this.array;
        if (iArr.length < 30) {
            int min = Integer.MAX_VALUE;
            if (from < 0) {
                from = 0;
            }
            if (to > iArr.length - 1) {
                to = iArr.length - 1;
            }
            for (int i = from; i <= to; i++) {
                int[] iArr2 = this.array;
                if (iArr2[i] < min) {
                    min = iArr2[i];
                }
            }
            return min;
        }
        return rMinQ(1, from, to);
    }

    private int rMinQ(int v, int from, int to) {
        Node n = this.heap[v];
        if (n.pendingVal != null && contains(n.from, n.to, from, to)) {
            return n.pendingVal.intValue();
        }
        if (contains(from, to, n.from, n.to)) {
            return this.heap[v].min;
        }
        if (intersects(from, to, n.from, n.to)) {
            propagate(v);
            int leftMin = rMinQ(v * 2, from, to);
            int rightMin = rMinQ((v * 2) + 1, from, to);
            return Math.min(leftMin, rightMin);
        }
        return Integer.MAX_VALUE;
    }

    private void propagate(int v) {
        Node n = this.heap[v];
        if (n.pendingVal != null) {
            change(this.heap[v * 2], n.pendingVal.intValue());
            change(this.heap[(v * 2) + 1], n.pendingVal.intValue());
            n.pendingVal = null;
        }
    }

    private void change(Node n, int value) {
        n.pendingVal = Integer.valueOf(value);
        n.sum = n.size() * value;
        n.max = value;
        n.min = value;
        this.array[n.from] = value;
    }

    private boolean contains(int from1, int to1, int from2, int to2) {
        return from2 >= from1 && to2 <= to1;
    }

    private boolean intersects(int from1, int to1, int from2, int to2) {
        return (from1 <= from2 && to1 >= from2) || (from1 >= from2 && from1 <= to2);
    }

    /* loaded from: classes4.dex */
    public static class Node {
        int from;
        int max;
        int min;
        Integer pendingVal = null;
        int sum;
        int to;

        Node() {
        }

        int size() {
            return (this.to - this.from) + 1;
        }
    }
}
