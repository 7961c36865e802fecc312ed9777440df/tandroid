package j$.time.format;

import j$.util.concurrent.ConcurrentHashMap;
import java.util.Objects;
/* loaded from: classes2.dex */
public final class r {
    public static final r a = new r('0', '+', '-', '.');

    static {
        new ConcurrentHashMap(16, 0.75f, 2);
    }

    private r(char c, char c2, char c3, char c4) {
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof r) {
            Objects.requireNonNull((r) obj);
            return true;
        }
        return false;
    }

    public int hashCode() {
        return 182;
    }

    public String toString() {
        return "DecimalStyle[0+-.]";
    }
}
