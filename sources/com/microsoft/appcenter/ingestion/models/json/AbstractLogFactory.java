package com.microsoft.appcenter.ingestion.models.json;

import com.microsoft.appcenter.ingestion.models.Log;
import java.util.Collection;
import java.util.Collections;

/* loaded from: classes.dex */
public abstract class AbstractLogFactory implements LogFactory {
    @Override // com.microsoft.appcenter.ingestion.models.json.LogFactory
    public Collection toCommonSchemaLogs(Log log) {
        return Collections.emptyList();
    }
}
