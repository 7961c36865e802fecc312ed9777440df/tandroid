package j$.time.format;

/* loaded from: classes2.dex */
final class k implements g {
    static final String[] c = {"+HH", "+HHmm", "+HH:mm", "+HHMM", "+HH:MM", "+HHMMss", "+HH:MM:ss", "+HHMMSS", "+HH:MM:SS"};
    static final k d = new k("+HH:MM:ss", "Z");
    private final String a;
    private final int b;

    static {
        new k("+HH:MM:ss", "0");
    }

    k(String str, String str2) {
        int i = 0;
        while (true) {
            String[] strArr = c;
            if (i >= 9) {
                throw new IllegalArgumentException("Invalid zone offset pattern: ".concat(str));
            }
            if (strArr[i].equals(str)) {
                this.b = i;
                this.a = str2;
                return;
            }
            i++;
        }
    }

    @Override // j$.time.format.g
    public final boolean a(s sVar, StringBuilder sb) {
        Long e = sVar.e(j$.time.temporal.a.OFFSET_SECONDS);
        if (e == null) {
            return false;
        }
        long longValue = e.longValue();
        int i = (int) longValue;
        if (longValue != i) {
            throw new ArithmeticException();
        }
        String str = this.a;
        if (i != 0) {
            int abs = Math.abs((i / 3600) % 100);
            int abs2 = Math.abs((i / 60) % 60);
            int abs3 = Math.abs(i % 60);
            int length = sb.length();
            sb.append(i < 0 ? "-" : "+");
            sb.append((char) ((abs / 10) + 48));
            sb.append((char) ((abs % 10) + 48));
            int i2 = this.b;
            if (i2 >= 3 || (i2 >= 1 && abs2 > 0)) {
                int i3 = i2 % 2;
                sb.append(i3 == 0 ? ":" : "");
                sb.append((char) ((abs2 / 10) + 48));
                sb.append((char) ((abs2 % 10) + 48));
                abs += abs2;
                if (i2 >= 7 || (i2 >= 5 && abs3 > 0)) {
                    sb.append(i3 == 0 ? ":" : "");
                    sb.append((char) ((abs3 / 10) + 48));
                    sb.append((char) ((abs3 % 10) + 48));
                    abs += abs3;
                }
            }
            if (abs == 0) {
                sb.setLength(length);
            }
            return true;
        }
        sb.append(str);
        return true;
    }

    public final String toString() {
        return "Offset(" + c[this.b] + ",'" + this.a.replace("'", "''") + "')";
    }
}
