package com.airbnb.lottie.model;
/* loaded from: classes.dex */
public class Marker {
    private final String name;
    public final float startFrame;

    public Marker(String str, float f, float f2) {
        this.name = str;
        this.startFrame = f;
    }

    public boolean matchesName(String str) {
        if (this.name.equalsIgnoreCase(str)) {
            return true;
        }
        if (this.name.endsWith("\r")) {
            String str2 = this.name;
            if (str2.substring(0, str2.length() - 1).equalsIgnoreCase(str)) {
                return true;
            }
        }
        return false;
    }
}