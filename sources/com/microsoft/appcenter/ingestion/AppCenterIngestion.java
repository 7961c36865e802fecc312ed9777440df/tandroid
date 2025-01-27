package com.microsoft.appcenter.ingestion;

import com.microsoft.appcenter.http.AbstractAppCallTemplate;
import com.microsoft.appcenter.http.HttpClient;
import com.microsoft.appcenter.http.ServiceCall;
import com.microsoft.appcenter.http.ServiceCallback;
import com.microsoft.appcenter.ingestion.models.LogContainer;
import com.microsoft.appcenter.ingestion.models.json.LogSerializer;
import java.util.HashMap;
import java.util.UUID;

/* loaded from: classes.dex */
public class AppCenterIngestion implements Ingestion {
    private final HttpClient mHttpClient;
    private final LogSerializer mLogSerializer;
    private String mLogUrl = "https://in.appcenter.ms";

    private static class IngestionCallTemplate extends AbstractAppCallTemplate {
        private final LogContainer mLogContainer;
        private final LogSerializer mLogSerializer;

        IngestionCallTemplate(LogSerializer logSerializer, LogContainer logContainer) {
            this.mLogSerializer = logSerializer;
            this.mLogContainer = logContainer;
        }

        @Override // com.microsoft.appcenter.http.HttpClient.CallTemplate
        public String buildRequestBody() {
            return this.mLogSerializer.serializeContainer(this.mLogContainer);
        }
    }

    public AppCenterIngestion(HttpClient httpClient, LogSerializer logSerializer) {
        this.mLogSerializer = logSerializer;
        this.mHttpClient = httpClient;
    }

    @Override // java.io.Closeable, java.lang.AutoCloseable
    public void close() {
        this.mHttpClient.close();
    }

    @Override // com.microsoft.appcenter.ingestion.Ingestion
    public void reopen() {
        this.mHttpClient.reopen();
    }

    @Override // com.microsoft.appcenter.ingestion.Ingestion
    public ServiceCall sendAsync(String str, UUID uuid, LogContainer logContainer, ServiceCallback serviceCallback) {
        HashMap hashMap = new HashMap();
        hashMap.put("Install-ID", uuid.toString());
        hashMap.put("App-Secret", str);
        IngestionCallTemplate ingestionCallTemplate = new IngestionCallTemplate(this.mLogSerializer, logContainer);
        return this.mHttpClient.callAsync(this.mLogUrl + "/logs?api-version=1.0.0", "POST", hashMap, ingestionCallTemplate, serviceCallback);
    }

    @Override // com.microsoft.appcenter.ingestion.Ingestion
    public void setLogUrl(String str) {
        this.mLogUrl = str;
    }
}
