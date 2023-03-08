package j$.util;

import j$.util.function.Consumer;
import java.util.Comparator;
import java.util.Objects;
import org.telegram.messenger.LiteMode;
/* loaded from: classes2.dex */
final class B implements u {
    private final Object[] a;
    private int b;
    private final int c;
    private final int d;

    public B(Object[] objArr, int i, int i2, int i3) {
        this.a = objArr;
        this.b = i;
        this.c = i2;
        this.d = i3 | 64 | LiteMode.FLAG_ANIMATED_EMOJI_KEYBOARD_NOT_PREMIUM;
    }

    @Override // j$.util.u
    public boolean b(Consumer consumer) {
        Objects.requireNonNull(consumer);
        int i = this.b;
        if (i < 0 || i >= this.c) {
            return false;
        }
        Object[] objArr = this.a;
        this.b = i + 1;
        consumer.accept(objArr[i]);
        return true;
    }

    @Override // j$.util.u
    public int characteristics() {
        return this.d;
    }

    @Override // j$.util.u
    public long estimateSize() {
        return this.c - this.b;
    }

    @Override // j$.util.u
    public void forEachRemaining(Consumer consumer) {
        int i;
        Objects.requireNonNull(consumer);
        Object[] objArr = this.a;
        int length = objArr.length;
        int i2 = this.c;
        if (length < i2 || (i = this.b) < 0) {
            return;
        }
        this.b = i2;
        if (i < i2) {
            do {
                consumer.accept(objArr[i]);
                i++;
            } while (i < i2);
        }
    }

    @Override // j$.util.u
    public Comparator getComparator() {
        if (a.f(this, 4)) {
            return null;
        }
        throw new IllegalStateException();
    }

    @Override // j$.util.u
    public /* synthetic */ long getExactSizeIfKnown() {
        return a.e(this);
    }

    @Override // j$.util.u
    public /* synthetic */ boolean hasCharacteristics(int i) {
        return a.f(this, i);
    }

    @Override // j$.util.u
    public u trySplit() {
        int i = this.b;
        int i2 = (this.c + i) >>> 1;
        if (i >= i2) {
            return null;
        }
        Object[] objArr = this.a;
        this.b = i2;
        return new B(objArr, i, i2, this.d);
    }
}