package com.google.gson;

import java.lang.reflect.Type;

/* loaded from: classes.dex */
public interface JsonSerializer {
    JsonElement serialize(Object obj, Type type, JsonSerializationContext jsonSerializationContext);
}
