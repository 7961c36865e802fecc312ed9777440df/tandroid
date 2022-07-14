package com.googlecode.mp4parser.util;
/* loaded from: classes3.dex */
public class Math {
    public static long gcd(long a, long b) {
        while (b > 0) {
            long temp = b;
            b = a % b;
            a = temp;
        }
        return a;
    }

    public static int gcd(int a, int b) {
        while (b > 0) {
            int temp = b;
            b = a % b;
            a = temp;
        }
        return a;
    }

    public static long lcm(long a, long b) {
        return (b / gcd(a, b)) * a;
    }

    public static int lcm(int a, int b) {
        return (b / gcd(a, b)) * a;
    }
}
