package org.telegram.ui.web;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.os.Build;
import android.text.TextUtils;
import android.util.JsonReader;
import android.util.Pair;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONObject;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.ApplicationLoader;
import org.telegram.messenger.BuildVars;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.ImageReceiver;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.R;
import org.telegram.messenger.SharedConfig;
import org.telegram.messenger.SvgHelper;
import org.telegram.messenger.Timer;
import org.telegram.messenger.Utilities;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC$Page;
import org.telegram.tgnet.TLRPC$PageBlock;
import org.telegram.tgnet.TLRPC$Photo;
import org.telegram.tgnet.TLRPC$RichText;
import org.telegram.tgnet.TLRPC$TL_error;
import org.telegram.tgnet.TLRPC$TL_messages_getWebPage;
import org.telegram.tgnet.TLRPC$TL_messages_webPage;
import org.telegram.tgnet.TLRPC$TL_page;
import org.telegram.tgnet.TLRPC$TL_pageBlockBlockquote;
import org.telegram.tgnet.TLRPC$TL_pageBlockDetails;
import org.telegram.tgnet.TLRPC$TL_pageBlockDivider;
import org.telegram.tgnet.TLRPC$TL_pageBlockHeader;
import org.telegram.tgnet.TLRPC$TL_pageBlockList;
import org.telegram.tgnet.TLRPC$TL_pageBlockOrderedList;
import org.telegram.tgnet.TLRPC$TL_pageBlockParagraph;
import org.telegram.tgnet.TLRPC$TL_pageBlockPhoto;
import org.telegram.tgnet.TLRPC$TL_pageBlockPreformatted;
import org.telegram.tgnet.TLRPC$TL_pageBlockSubheader;
import org.telegram.tgnet.TLRPC$TL_pageBlockTable;
import org.telegram.tgnet.TLRPC$TL_pageBlockTitle;
import org.telegram.tgnet.TLRPC$TL_pageCaption;
import org.telegram.tgnet.TLRPC$TL_pageListItemBlocks;
import org.telegram.tgnet.TLRPC$TL_pageListItemText;
import org.telegram.tgnet.TLRPC$TL_pageListOrderedItemBlocks;
import org.telegram.tgnet.TLRPC$TL_pageListOrderedItemText;
import org.telegram.tgnet.TLRPC$TL_pageTableCell;
import org.telegram.tgnet.TLRPC$TL_pageTableRow;
import org.telegram.tgnet.TLRPC$TL_textAnchor;
import org.telegram.tgnet.TLRPC$TL_textBold;
import org.telegram.tgnet.TLRPC$TL_textConcat;
import org.telegram.tgnet.TLRPC$TL_textEmail;
import org.telegram.tgnet.TLRPC$TL_textEmpty;
import org.telegram.tgnet.TLRPC$TL_textFixed;
import org.telegram.tgnet.TLRPC$TL_textImage;
import org.telegram.tgnet.TLRPC$TL_textItalic;
import org.telegram.tgnet.TLRPC$TL_textMarked;
import org.telegram.tgnet.TLRPC$TL_textPhone;
import org.telegram.tgnet.TLRPC$TL_textPlain;
import org.telegram.tgnet.TLRPC$TL_textStrike;
import org.telegram.tgnet.TLRPC$TL_textSubscript;
import org.telegram.tgnet.TLRPC$TL_textSuperscript;
import org.telegram.tgnet.TLRPC$TL_textUrl;
import org.telegram.tgnet.TLRPC$TL_webPage;
import org.telegram.tgnet.TLRPC$WebPage;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.ui.LaunchActivity;
import org.telegram.ui.web.BotWebViewContainer;
import org.telegram.ui.web.MHTML;
import org.telegram.ui.web.WebInstantView;
/* loaded from: classes.dex */
public class WebInstantView {
    public static final HashMap instants = new HashMap();
    private static HashMap loadingPhotos;
    public final HashMap loadedPhotos = new HashMap();
    public MHTML mhtml;
    public String url;
    public TLRPC$WebPage webpage;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public class 4 {
        final /* synthetic */ boolean[] val$done;
        final /* synthetic */ WebView val$webView;
        final /* synthetic */ FrameLayout val$webViewContainer;
        final /* synthetic */ Utilities.Callback val$whenDone;

        4(boolean[] zArr, WebView webView, FrameLayout frameLayout, Utilities.Callback callback) {
            this.val$done = zArr;
            this.val$webView = webView;
            this.val$webViewContainer = frameLayout;
            this.val$whenDone = callback;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static /* synthetic */ void lambda$done$0(boolean[] zArr, WebView webView, FrameLayout frameLayout, String str, Utilities.Callback callback) {
            JSONObject jSONObject;
            if (zArr[0]) {
                return;
            }
            zArr[0] = true;
            if (!BuildVars.DEBUG_PRIVATE_VERSION) {
                webView.onPause();
                webView.destroy();
                AndroidUtilities.removeFromParent(webView);
                AndroidUtilities.removeFromParent(frameLayout);
            }
            try {
                jSONObject = new JSONObject(str);
            } catch (Exception e) {
                FileLog.e(e);
                jSONObject = null;
            }
            callback.run(jSONObject);
        }

        @JavascriptInterface
        public void done(final String str) {
            final boolean[] zArr = this.val$done;
            final WebView webView = this.val$webView;
            final FrameLayout frameLayout = this.val$webViewContainer;
            final Utilities.Callback callback = this.val$whenDone;
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.web.WebInstantView$4$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    WebInstantView.4.lambda$done$0(zArr, webView, frameLayout, str, callback);
                }
            });
        }
    }

    /* loaded from: classes.dex */
    public static class Loader {
        private Runnable cancelLocal;
        private boolean cancelled;
        private final int currentAccount;
        public boolean currentIsLoaded;
        public float currentProgress;
        public String currentUrl;
        private boolean gotLocal;
        private boolean gotRemote;
        private final ArrayList listeners = new ArrayList();
        private TLRPC$WebPage localPage;
        private TLRPC$WebPage remotePage;
        private int reqId;
        private boolean started;

        public Loader(int i) {
            this.currentAccount = i;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$listen$4(Runnable runnable) {
            this.listeners.remove(runnable);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$retryLocal$0(WebInstantView webInstantView) {
            this.cancelLocal = null;
            this.gotLocal = true;
            TLRPC$WebPage tLRPC$WebPage = this.localPage;
            if (tLRPC$WebPage != null) {
                WebInstantView.recycle(tLRPC$WebPage);
            }
            this.localPage = webInstantView.webpage;
            notifyUpdate();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$start$1(WebInstantView webInstantView) {
            this.cancelLocal = null;
            this.gotLocal = true;
            TLRPC$WebPage tLRPC$WebPage = this.localPage;
            if (tLRPC$WebPage != null) {
                WebInstantView.recycle(tLRPC$WebPage);
            }
            this.localPage = webInstantView.webpage;
            notifyUpdate();
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* JADX WARN: Code restructure failed: missing block: B:9:0x0030, code lost:
            if ((r5.cached_page instanceof org.telegram.tgnet.TLRPC$TL_page) != false) goto L4;
         */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public /* synthetic */ void lambda$start$2(TLObject tLObject) {
            TLRPC$WebPage tLRPC$WebPage;
            TLRPC$WebPage tLRPC$WebPage2;
            Runnable runnable;
            this.gotRemote = true;
            if (!(tLObject instanceof TLRPC$TL_messages_webPage)) {
                if (tLObject instanceof TLRPC$TL_webPage) {
                    tLRPC$WebPage = (TLRPC$TL_webPage) tLObject;
                }
                this.remotePage = null;
                tLRPC$WebPage2 = this.remotePage;
                if (tLRPC$WebPage2 != null && tLRPC$WebPage2.cached_page == null) {
                    this.remotePage = null;
                }
                if (!SharedConfig.onlyLocalInstantView && this.remotePage != null && (runnable = this.cancelLocal) != null) {
                    runnable.run();
                }
                notifyUpdate();
            }
            TLRPC$TL_messages_webPage tLRPC$TL_messages_webPage = (TLRPC$TL_messages_webPage) tLObject;
            MessagesController.getInstance(this.currentAccount).putUsers(tLRPC$TL_messages_webPage.users, false);
            MessagesController.getInstance(this.currentAccount).putChats(tLRPC$TL_messages_webPage.chats, false);
            tLRPC$WebPage = tLRPC$TL_messages_webPage.webpage;
            this.remotePage = tLRPC$WebPage;
            tLRPC$WebPage2 = this.remotePage;
            if (tLRPC$WebPage2 != null) {
                this.remotePage = null;
            }
            if (!SharedConfig.onlyLocalInstantView) {
                runnable.run();
            }
            notifyUpdate();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$start$3(final TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.web.WebInstantView$Loader$$ExternalSyntheticLambda3
                @Override // java.lang.Runnable
                public final void run() {
                    WebInstantView.Loader.this.lambda$start$2(tLObject);
                }
            });
        }

        private void notifyUpdate() {
            Iterator it = this.listeners.iterator();
            while (it.hasNext()) {
                ((Runnable) it.next()).run();
            }
        }

        public void cancel() {
            Runnable runnable;
            if (this.cancelled) {
                return;
            }
            this.cancelled = true;
            if (!this.gotRemote) {
                ConnectionsManager.getInstance(this.currentAccount).cancelRequest(this.reqId, true);
            }
            if (this.gotLocal || (runnable = this.cancelLocal) == null) {
                return;
            }
            runnable.run();
        }

        public TLRPC$WebPage getWebPage() {
            TLRPC$WebPage tLRPC$WebPage;
            if (SharedConfig.onlyLocalInstantView || (tLRPC$WebPage = this.remotePage) == null) {
                TLRPC$WebPage tLRPC$WebPage2 = this.localPage;
                if (tLRPC$WebPage2 != null) {
                    return tLRPC$WebPage2;
                }
                return null;
            }
            return tLRPC$WebPage;
        }

        public boolean isDone() {
            return (this.gotRemote && this.gotLocal) || this.remotePage != null || this.localPage != null || this.cancelled;
        }

        public Runnable listen(final Runnable runnable) {
            this.listeners.add(runnable);
            return new Runnable() { // from class: org.telegram.ui.web.WebInstantView$Loader$$ExternalSyntheticLambda4
                @Override // java.lang.Runnable
                public final void run() {
                    WebInstantView.Loader.this.lambda$listen$4(runnable);
                }
            };
        }

        public void recycle() {
            TLRPC$WebPage tLRPC$WebPage = this.localPage;
            if (tLRPC$WebPage != null) {
                WebInstantView.recycle(tLRPC$WebPage);
                this.localPage = null;
            }
        }

        public void retryLocal(BotWebViewContainer.MyWebView myWebView) {
            if (this.cancelled) {
                return;
            }
            TLRPC$WebPage tLRPC$WebPage = this.localPage;
            if (tLRPC$WebPage != null) {
                WebInstantView.recycle(tLRPC$WebPage);
                this.localPage = null;
            }
            this.gotLocal = false;
            this.currentUrl = myWebView.getUrl();
            this.currentProgress = myWebView.getProgress();
            this.currentIsLoaded = myWebView.isPageLoaded();
            Runnable runnable = this.cancelLocal;
            if (runnable != null) {
                runnable.run();
            }
            this.cancelLocal = WebInstantView.generate(myWebView, false, new Utilities.Callback() { // from class: org.telegram.ui.web.WebInstantView$Loader$$ExternalSyntheticLambda2
                @Override // org.telegram.messenger.Utilities.Callback
                public final void run(Object obj) {
                    WebInstantView.Loader.this.lambda$retryLocal$0((WebInstantView) obj);
                }
            });
        }

        public void start(BotWebViewContainer.MyWebView myWebView) {
            if (this.started) {
                return;
            }
            this.started = true;
            this.currentUrl = myWebView.getUrl();
            this.currentProgress = myWebView.getProgress();
            this.currentIsLoaded = myWebView.isPageLoaded();
            this.cancelLocal = WebInstantView.generate(myWebView, false, new Utilities.Callback() { // from class: org.telegram.ui.web.WebInstantView$Loader$$ExternalSyntheticLambda0
                @Override // org.telegram.messenger.Utilities.Callback
                public final void run(Object obj) {
                    WebInstantView.Loader.this.lambda$start$1((WebInstantView) obj);
                }
            });
            TLRPC$TL_messages_getWebPage tLRPC$TL_messages_getWebPage = new TLRPC$TL_messages_getWebPage();
            tLRPC$TL_messages_getWebPage.url = this.currentUrl;
            tLRPC$TL_messages_getWebPage.hash = 0;
            this.reqId = ConnectionsManager.getInstance(this.currentAccount).sendRequest(tLRPC$TL_messages_getWebPage, new RequestDelegate() { // from class: org.telegram.ui.web.WebInstantView$Loader$$ExternalSyntheticLambda1
                @Override // org.telegram.tgnet.RequestDelegate
                public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
                    WebInstantView.Loader.this.lambda$start$3(tLObject, tLRPC$TL_error);
                }
            });
        }
    }

    /* loaded from: classes.dex */
    public class WebPhoto extends TLRPC$Photo {
        public int h;
        public TLRPC$TL_textImage inlineImage;
        public WebInstantView instantView;
        public String url;
        public HashSet urls = new HashSet();
        public int w;

        public WebPhoto() {
        }
    }

    public static TLRPC$RichText addLastSpace(TLRPC$RichText tLRPC$RichText) {
        TLRPC$TL_textPlain tLRPC$TL_textPlain;
        String str;
        ArrayList arrayList;
        if (tLRPC$RichText == null) {
            return tLRPC$RichText;
        }
        TLRPC$RichText tLRPC$RichText2 = tLRPC$RichText.text;
        if (tLRPC$RichText2 == null) {
            if (tLRPC$RichText.texts.isEmpty()) {
                if ((tLRPC$RichText instanceof TLRPC$TL_textPlain) && (str = (tLRPC$TL_textPlain = (TLRPC$TL_textPlain) tLRPC$RichText).text) != null && !str.endsWith(" ")) {
                    tLRPC$TL_textPlain.text += ' ';
                }
                return tLRPC$RichText;
            }
            tLRPC$RichText2 = (TLRPC$RichText) tLRPC$RichText.texts.get(arrayList.size() - 1);
        }
        addLastSpace(tLRPC$RichText2);
        return tLRPC$RichText;
    }

    public static TLRPC$RichText addNewLine(TLRPC$RichText tLRPC$RichText) {
        if (tLRPC$RichText == null) {
            return tLRPC$RichText;
        }
        TLRPC$RichText tLRPC$RichText2 = tLRPC$RichText.text;
        if (tLRPC$RichText2 == null) {
            if (tLRPC$RichText.texts.isEmpty()) {
                if (tLRPC$RichText instanceof TLRPC$TL_textPlain) {
                    StringBuilder sb = new StringBuilder();
                    TLRPC$TL_textPlain tLRPC$TL_textPlain = (TLRPC$TL_textPlain) tLRPC$RichText;
                    sb.append(tLRPC$TL_textPlain.text);
                    sb.append('\n');
                    tLRPC$TL_textPlain.text = sb.toString();
                }
                return tLRPC$RichText;
            }
            ArrayList arrayList = tLRPC$RichText.texts;
            tLRPC$RichText2 = (TLRPC$RichText) arrayList.get(arrayList.size() - 1);
        }
        addNewLine(tLRPC$RichText2);
        return tLRPC$RichText;
    }

    public static TLRPC$RichText applyAnchor(TLRPC$RichText tLRPC$RichText, JSONObject jSONObject) {
        if (jSONObject == null) {
            return tLRPC$RichText;
        }
        String optString = jSONObject.optString("id");
        if (TextUtils.isEmpty(optString)) {
            return tLRPC$RichText;
        }
        TLRPC$TL_textAnchor tLRPC$TL_textAnchor = new TLRPC$TL_textAnchor();
        tLRPC$TL_textAnchor.text = tLRPC$RichText;
        tLRPC$TL_textAnchor.name = optString;
        return tLRPC$TL_textAnchor;
    }

    public static void cancelLoadPhoto(ImageReceiver imageReceiver) {
        HashMap hashMap = loadingPhotos;
        if (hashMap == null) {
            return;
        }
        for (Map.Entry entry : hashMap.entrySet()) {
            String str = (String) entry.getKey();
            ArrayList arrayList = (ArrayList) entry.getValue();
            int i = 0;
            while (true) {
                if (i >= arrayList.size()) {
                    break;
                } else if (((Pair) arrayList.get(i)).first == imageReceiver) {
                    arrayList.remove(i);
                    break;
                } else {
                    i++;
                }
            }
            if (arrayList.isEmpty()) {
                loadingPhotos.remove(str);
                return;
            }
        }
    }

    public static TLRPC$RichText filterRecursiveAnchorLinks(TLRPC$RichText tLRPC$RichText, String str, String str2) {
        TLRPC$TL_textUrl tLRPC$TL_textUrl;
        String str3;
        if (tLRPC$RichText == null) {
            return tLRPC$RichText;
        }
        if (tLRPC$RichText instanceof TLRPC$TL_textConcat) {
            TLRPC$TL_textConcat tLRPC$TL_textConcat = (TLRPC$TL_textConcat) tLRPC$RichText;
            TLRPC$TL_textConcat tLRPC$TL_textConcat2 = new TLRPC$TL_textConcat();
            for (int i = 0; i < tLRPC$TL_textConcat.texts.size(); i++) {
                TLRPC$RichText filterRecursiveAnchorLinks = filterRecursiveAnchorLinks((TLRPC$RichText) tLRPC$TL_textConcat.texts.get(i), str, str2);
                if (filterRecursiveAnchorLinks != null) {
                    tLRPC$TL_textConcat2.texts.add(filterRecursiveAnchorLinks);
                }
            }
            return tLRPC$TL_textConcat2;
        } else if (!(tLRPC$RichText instanceof TLRPC$TL_textUrl) || (str3 = (tLRPC$TL_textUrl = (TLRPC$TL_textUrl) tLRPC$RichText).url) == null) {
            return tLRPC$RichText;
        } else {
            String lowerCase = str3.toLowerCase();
            if (!lowerCase.equals("#" + str2)) {
                String lowerCase2 = tLRPC$TL_textUrl.url.toLowerCase();
                if (!TextUtils.equals(lowerCase2, str + "#" + str2)) {
                    return tLRPC$RichText;
                }
            }
            return null;
        }
    }

    public static Runnable generate(WebView webView, boolean z, final Utilities.Callback callback) {
        if (callback == null) {
            return null;
        }
        if (webView == null) {
            callback.run(null);
            return null;
        }
        final boolean[] zArr = {false};
        final WebInstantView webInstantView = new WebInstantView();
        webInstantView.url = webView.getUrl();
        final Timer create = Timer.create("WebInstantView");
        final Timer.Task start = Timer.start(create, "getHTML");
        webInstantView.getHTML(webView, z, new Utilities.Callback() { // from class: org.telegram.ui.web.WebInstantView$$ExternalSyntheticLambda0
            @Override // org.telegram.messenger.Utilities.Callback
            public final void run(Object obj) {
                WebInstantView.lambda$generate$1(Timer.Task.this, zArr, create, webInstantView, callback, (InputStream) obj);
            }
        });
        return new Runnable() { // from class: org.telegram.ui.web.WebInstantView$$ExternalSyntheticLambda1
            @Override // java.lang.Runnable
            public final void run() {
                WebInstantView.lambda$generate$2(zArr);
            }
        };
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$generate$0(Timer.Task task, boolean[] zArr, Timer timer, WebInstantView webInstantView, Utilities.Callback callback, JSONObject jSONObject) {
        Timer.done(task);
        if (zArr[0]) {
            return;
        }
        Timer.Task start = Timer.start(timer, "parseJSON");
        try {
            webInstantView.webpage = webInstantView.parseJSON(webInstantView.url, jSONObject);
        } catch (Exception e) {
            Timer.log(timer, "error: " + e);
            FileLog.e(e);
        }
        Timer.done(start);
        callback.run(webInstantView);
        TLRPC$WebPage tLRPC$WebPage = webInstantView.webpage;
        if (tLRPC$WebPage != null) {
            instants.put(tLRPC$WebPage, webInstantView);
        }
        Timer.finish(timer);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$generate$1(Timer.Task task, final boolean[] zArr, final Timer timer, final WebInstantView webInstantView, final Utilities.Callback callback, InputStream inputStream) {
        Timer.done(task);
        if (zArr[0]) {
            return;
        }
        final Timer.Task start = Timer.start(timer, "readHTML");
        webInstantView.readHTML(webInstantView.url, inputStream, new Utilities.Callback() { // from class: org.telegram.ui.web.WebInstantView$$ExternalSyntheticLambda4
            @Override // org.telegram.messenger.Utilities.Callback
            public final void run(Object obj) {
                WebInstantView.lambda$generate$0(Timer.Task.this, zArr, timer, webInstantView, callback, (JSONObject) obj);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$generate$2(boolean[] zArr) {
        zArr[0] = true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$getHTML$5(Utilities.Callback callback, String str) {
        try {
            JsonReader jsonReader = new JsonReader(new StringReader(str));
            jsonReader.setLenient(true);
            String nextString = jsonReader.nextString();
            jsonReader.close();
            callback.run(new ByteArrayInputStream(nextString.getBytes(StandardCharsets.UTF_8)));
        } catch (Exception e) {
            FileLog.e(e);
            callback.run(null);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$getHTML$6(String str) {
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$getHTML$7(WebView webView, File file, Utilities.Callback callback, String str) {
        webView.evaluateJavascript(AndroidUtilities.readRes(R.raw.open_collapsed).replace("$OPEN$", "false"), new ValueCallback() { // from class: org.telegram.ui.web.WebInstantView$$ExternalSyntheticLambda8
            @Override // android.webkit.ValueCallback
            public final void onReceiveValue(Object obj) {
                WebInstantView.lambda$getHTML$6((String) obj);
            }
        });
        try {
            MHTML mhtml = new MHTML(file);
            this.mhtml = mhtml;
            if (!mhtml.entries.isEmpty()) {
                callback.run(((MHTML.Entry) this.mhtml.entries.get(0)).getInputStream());
                return;
            }
        } catch (Exception e) {
            FileLog.e(e);
        }
        callback.run(null);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$getHTML$8(final WebView webView, final File file, final Utilities.Callback callback, String str) {
        webView.saveWebArchive(file.getAbsolutePath(), false, new ValueCallback() { // from class: org.telegram.ui.web.WebInstantView$$ExternalSyntheticLambda6
            @Override // android.webkit.ValueCallback
            public final void onReceiveValue(Object obj) {
                WebInstantView.this.lambda$getHTML$7(webView, file, callback, (String) obj);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Removed duplicated region for block: B:28:0x0062  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public /* synthetic */ void lambda$loadPhotoInternal$3(WebPhoto webPhoto, Bitmap bitmap) {
        Object obj;
        int height;
        TLRPC$TL_textImage tLRPC$TL_textImage;
        if (loadingPhotos == null) {
            return;
        }
        boolean z = (webPhoto.w <= 0 || webPhoto.h <= 0) && bitmap != null;
        if (bitmap != null) {
            this.loadedPhotos.put(webPhoto.url, bitmap);
            if (z) {
                int i = webPhoto.w;
                if (i == 0 && webPhoto.h == 0) {
                    webPhoto.w = bitmap.getWidth();
                    height = bitmap.getHeight();
                } else {
                    if (i == 0) {
                        webPhoto.w = (int) ((bitmap.getWidth() / bitmap.getHeight()) * webPhoto.h);
                    } else if (webPhoto.h == 0) {
                        height = (int) ((bitmap.getHeight() / bitmap.getWidth()) * webPhoto.w);
                    }
                    tLRPC$TL_textImage = webPhoto.inlineImage;
                    if (tLRPC$TL_textImage != null) {
                        tLRPC$TL_textImage.w = webPhoto.w;
                        tLRPC$TL_textImage.h = webPhoto.h;
                    }
                }
                webPhoto.h = height;
                tLRPC$TL_textImage = webPhoto.inlineImage;
                if (tLRPC$TL_textImage != null) {
                }
            }
        }
        ArrayList arrayList = (ArrayList) loadingPhotos.remove(webPhoto.url);
        if (arrayList == null) {
            return;
        }
        Iterator it = arrayList.iterator();
        while (it.hasNext()) {
            Pair pair = (Pair) it.next();
            ((ImageReceiver) pair.first).setImageBitmap(bitmap);
            if (z && (obj = pair.second) != null) {
                ((Runnable) obj).run();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$loadPhotoInternal$4(final WebPhoto webPhoto, final Bitmap bitmap) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.web.WebInstantView$$ExternalSyntheticLambda7
            @Override // java.lang.Runnable
            public final void run() {
                WebInstantView.this.lambda$loadPhotoInternal$3(webPhoto, bitmap);
            }
        });
    }

    public static void loadPhoto(WebPhoto webPhoto, ImageReceiver imageReceiver, Runnable runnable) {
        WebInstantView webInstantView;
        if (webPhoto == null || (webInstantView = webPhoto.instantView) == null) {
            return;
        }
        webInstantView.loadPhotoInternal(webPhoto, imageReceiver, runnable);
    }

    /* JADX WARN: Removed duplicated region for block: B:41:0x00a6 A[Catch: Exception -> 0x0025, TryCatch #0 {Exception -> 0x0025, blocks: (B:2:0x0000, B:4:0x0005, B:5:0x000c, B:7:0x0012, B:15:0x002c, B:17:0x0038, B:19:0x003c, B:22:0x0041, B:45:0x00bb, B:24:0x0059, B:26:0x005d, B:44:0x00b3, B:28:0x0061, B:30:0x0074, B:32:0x0078, B:38:0x00a0, B:39:0x00a2, B:41:0x00a6, B:43:0x00b0, B:34:0x0081, B:35:0x0091, B:37:0x0095, B:47:0x00bf, B:49:0x00c9, B:51:0x00d7, B:53:0x00db, B:54:0x00e2, B:56:0x00ee, B:58:0x00f4, B:61:0x00ff, B:62:0x0102, B:64:0x010b), top: B:68:0x0000 }] */
    /* JADX WARN: Removed duplicated region for block: B:43:0x00b0 A[Catch: Exception -> 0x0025, TryCatch #0 {Exception -> 0x0025, blocks: (B:2:0x0000, B:4:0x0005, B:5:0x000c, B:7:0x0012, B:15:0x002c, B:17:0x0038, B:19:0x003c, B:22:0x0041, B:45:0x00bb, B:24:0x0059, B:26:0x005d, B:44:0x00b3, B:28:0x0061, B:30:0x0074, B:32:0x0078, B:38:0x00a0, B:39:0x00a2, B:41:0x00a6, B:43:0x00b0, B:34:0x0081, B:35:0x0091, B:37:0x0095, B:47:0x00bf, B:49:0x00c9, B:51:0x00d7, B:53:0x00db, B:54:0x00e2, B:56:0x00ee, B:58:0x00f4, B:61:0x00ff, B:62:0x0102, B:64:0x010b), top: B:68:0x0000 }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private void loadPhotoInternal(final WebPhoto webPhoto, ImageReceiver imageReceiver, Runnable runnable) {
        MHTML.Entry entry;
        int i;
        TLRPC$TL_textImage tLRPC$TL_textImage;
        Bitmap decodeStream;
        try {
            if (this.mhtml != null) {
                Iterator it = webPhoto.urls.iterator();
                entry = null;
                while (it.hasNext()) {
                    entry = (MHTML.Entry) this.mhtml.entriesByLocation.get((String) it.next());
                    if (entry != null) {
                        break;
                    }
                }
            } else {
                entry = null;
            }
            if (entry == null) {
                if (this.loadedPhotos.containsKey(webPhoto.url)) {
                    imageReceiver.setImageBitmap((Bitmap) this.loadedPhotos.get(webPhoto.url));
                    return;
                }
                if (loadingPhotos == null) {
                    loadingPhotos = new HashMap();
                }
                ArrayList arrayList = (ArrayList) loadingPhotos.get(webPhoto.url);
                if (arrayList == null) {
                    loadingPhotos.put(webPhoto.url, new ArrayList());
                    new HttpGetBitmapTask(new Utilities.Callback() { // from class: org.telegram.ui.web.WebInstantView$$ExternalSyntheticLambda5
                        @Override // org.telegram.messenger.Utilities.Callback
                        public final void run(Object obj) {
                            WebInstantView.this.lambda$loadPhotoInternal$4(webPhoto, (Bitmap) obj);
                        }
                    }).execute(webPhoto.url);
                    return;
                }
                for (int i2 = 0; i2 < arrayList.size(); i2++) {
                    if (((Pair) arrayList.get(i2)).first == imageReceiver) {
                        return;
                    }
                }
                arrayList.add(new Pair(imageReceiver, runnable));
                return;
            }
            if (entry.getType().contains("svg")) {
                if (webPhoto.w > 0 && webPhoto.h > 0) {
                    decodeStream = SvgHelper.getBitmap(entry.getInputStream(), AndroidUtilities.dp(webPhoto.w), AndroidUtilities.dp(webPhoto.h), false);
                }
                return;
            }
            if (webPhoto.w <= 0 || webPhoto.h <= 0) {
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inJustDecodeBounds = true;
                BitmapFactory.decodeStream(entry.getInputStream(), null, options);
                int i3 = webPhoto.w;
                if (i3 == 0 && webPhoto.h == 0) {
                    webPhoto.w = options.outWidth;
                    i = options.outHeight;
                } else {
                    if (i3 == 0) {
                        webPhoto.w = (int) ((options.outWidth / options.outHeight) * webPhoto.h);
                    } else if (webPhoto.h == 0) {
                        i = (int) ((options.outHeight / options.outWidth) * i3);
                    }
                    tLRPC$TL_textImage = webPhoto.inlineImage;
                    if (tLRPC$TL_textImage != null) {
                        tLRPC$TL_textImage.w = webPhoto.w;
                        tLRPC$TL_textImage.h = webPhoto.h;
                    }
                    if (runnable != null) {
                        runnable.run();
                    }
                }
                webPhoto.h = i;
                tLRPC$TL_textImage = webPhoto.inlineImage;
                if (tLRPC$TL_textImage != null) {
                }
                if (runnable != null) {
                }
            }
            decodeStream = BitmapFactory.decodeStream(entry.getInputStream());
            imageReceiver.setImageBitmap(decodeStream);
        } catch (Exception e) {
            FileLog.e(e);
        }
    }

    public static TLRPC$RichText parseRichText(String str) {
        TLRPC$TL_textPlain tLRPC$TL_textPlain = new TLRPC$TL_textPlain();
        tLRPC$TL_textPlain.text = str;
        return tLRPC$TL_textPlain;
    }

    public static void recycle(TLRPC$WebPage tLRPC$WebPage) {
        WebInstantView webInstantView = (WebInstantView) instants.remove(tLRPC$WebPage);
        if (webInstantView != null) {
            webInstantView.recycle();
        }
    }

    public static TLRPC$RichText trim(TLRPC$RichText tLRPC$RichText) {
        TLRPC$TL_textPlain tLRPC$TL_textPlain;
        String str;
        if (tLRPC$RichText == null) {
            return tLRPC$RichText;
        }
        TLRPC$RichText tLRPC$RichText2 = tLRPC$RichText.text;
        if (tLRPC$RichText2 == null) {
            if (tLRPC$RichText.texts.size() != 1) {
                if (!tLRPC$RichText.texts.isEmpty()) {
                    trimStart((TLRPC$RichText) tLRPC$RichText.texts.get(0));
                    ArrayList arrayList = tLRPC$RichText.texts;
                    trimEnd((TLRPC$RichText) arrayList.get(arrayList.size() - 1));
                } else if ((tLRPC$RichText instanceof TLRPC$TL_textPlain) && (str = (tLRPC$TL_textPlain = (TLRPC$TL_textPlain) tLRPC$RichText).text) != null) {
                    tLRPC$TL_textPlain.text = str.trim();
                }
                return tLRPC$RichText;
            }
            tLRPC$RichText2 = (TLRPC$RichText) tLRPC$RichText.texts.get(0);
        }
        trim(tLRPC$RichText2);
        return tLRPC$RichText;
    }

    public static TLRPC$RichText trimEnd(TLRPC$RichText tLRPC$RichText) {
        TLRPC$TL_textPlain tLRPC$TL_textPlain;
        String str;
        if (tLRPC$RichText == null) {
            return tLRPC$RichText;
        }
        TLRPC$RichText tLRPC$RichText2 = tLRPC$RichText.text;
        if (tLRPC$RichText2 == null) {
            if (tLRPC$RichText.texts.isEmpty()) {
                if ((tLRPC$RichText instanceof TLRPC$TL_textPlain) && (str = (tLRPC$TL_textPlain = (TLRPC$TL_textPlain) tLRPC$RichText).text) != null) {
                    tLRPC$TL_textPlain.text = str.replaceAll("\\s+$", "");
                }
                return tLRPC$RichText;
            }
            ArrayList arrayList = tLRPC$RichText.texts;
            tLRPC$RichText2 = (TLRPC$RichText) arrayList.get(arrayList.size() - 1);
        }
        trimEnd(tLRPC$RichText2);
        return tLRPC$RichText;
    }

    public static TLRPC$RichText trimStart(TLRPC$RichText tLRPC$RichText) {
        TLRPC$TL_textPlain tLRPC$TL_textPlain;
        String str;
        if (tLRPC$RichText == null) {
            return tLRPC$RichText;
        }
        TLRPC$RichText tLRPC$RichText2 = tLRPC$RichText.text;
        if (tLRPC$RichText2 == null) {
            if (tLRPC$RichText.texts.isEmpty()) {
                if ((tLRPC$RichText instanceof TLRPC$TL_textPlain) && (str = (tLRPC$TL_textPlain = (TLRPC$TL_textPlain) tLRPC$RichText).text) != null) {
                    tLRPC$TL_textPlain.text = str.replaceAll("^\\s+", "");
                }
                return tLRPC$RichText;
            }
            tLRPC$RichText2 = (TLRPC$RichText) tLRPC$RichText.texts.get(0);
        }
        trimStart(tLRPC$RichText2);
        return tLRPC$RichText;
    }

    public void getHTML(final WebView webView, boolean z, final Utilities.Callback callback) {
        if (callback == null) {
            return;
        }
        if (webView == null) {
            callback.run(null);
        } else if (z) {
            webView.evaluateJavascript("document.documentElement.outerHTML", new ValueCallback() { // from class: org.telegram.ui.web.WebInstantView$$ExternalSyntheticLambda2
                @Override // android.webkit.ValueCallback
                public final void onReceiveValue(Object obj) {
                    WebInstantView.lambda$getHTML$5(Utilities.Callback.this, (String) obj);
                }
            });
        } else {
            System.currentTimeMillis();
            final File file = new File(AndroidUtilities.getCacheDir(), "archive.mht");
            webView.evaluateJavascript(AndroidUtilities.readRes(R.raw.open_collapsed).replace("$OPEN$", "true"), new ValueCallback() { // from class: org.telegram.ui.web.WebInstantView$$ExternalSyntheticLambda3
                @Override // android.webkit.ValueCallback
                public final void onReceiveValue(Object obj) {
                    WebInstantView.this.lambda$getHTML$8(webView, file, callback, (String) obj);
                }
            });
        }
    }

    public boolean isInline(JSONArray jSONArray) {
        List asList = Arrays.asList("b", "strong", "span", "img", "i", "s", "a", "code", "mark", "sub", "sup");
        for (int i = 0; i < jSONArray.length(); i++) {
            Object obj = jSONArray.get(i);
            if (!(obj instanceof String)) {
                if (obj instanceof JSONObject) {
                    JSONObject jSONObject = (JSONObject) obj;
                    String optString = jSONObject.optString("tag");
                    if (!asList.contains(optString)) {
                        if ("div".equalsIgnoreCase(optString) || "span".equalsIgnoreCase(optString)) {
                            isInline(jSONObject.optJSONArray("content"));
                        }
                    }
                }
                return false;
            }
        }
        return true;
    }

    public TLRPC$TL_pageBlockDetails parseDetails(String str, JSONObject jSONObject, TLRPC$TL_page tLRPC$TL_page) {
        TLRPC$TL_pageBlockDetails tLRPC$TL_pageBlockDetails = new TLRPC$TL_pageBlockDetails();
        JSONArray optJSONArray = jSONObject.optJSONArray("content");
        if (optJSONArray == null) {
            return null;
        }
        int i = 0;
        while (true) {
            if (i >= optJSONArray.length()) {
                break;
            }
            Object obj = optJSONArray.get(i);
            if (obj instanceof JSONObject) {
                JSONObject jSONObject2 = (JSONObject) obj;
                if ("summary".equals(jSONObject2.optString("tag"))) {
                    tLRPC$TL_pageBlockDetails.title = trim(parseRichText(jSONObject2, tLRPC$TL_page));
                    optJSONArray.remove(i);
                    break;
                }
            }
            i++;
        }
        tLRPC$TL_pageBlockDetails.blocks.addAll(parsePageBlocks(str, optJSONArray, tLRPC$TL_page));
        tLRPC$TL_pageBlockDetails.open = jSONObject.has("open");
        return tLRPC$TL_pageBlockDetails;
    }

    public TLRPC$TL_pageBlockPhoto parseFigure(JSONObject jSONObject, TLRPC$TL_page tLRPC$TL_page) {
        JSONArray optJSONArray = jSONObject.optJSONArray("content");
        ArrayList arrayList = new ArrayList();
        WebPhoto webPhoto = null;
        int i = 0;
        TLRPC$TL_pageBlockPhoto tLRPC$TL_pageBlockPhoto = null;
        TLRPC$RichText tLRPC$RichText = null;
        for (int i2 = 0; i2 < optJSONArray.length(); i2++) {
            Object obj = optJSONArray.get(i2);
            if (obj instanceof JSONObject) {
                JSONObject jSONObject2 = (JSONObject) obj;
                String optString = jSONObject2.optString("tag");
                if ("figurecaption".equalsIgnoreCase(optString) || "caption".equalsIgnoreCase(optString)) {
                    tLRPC$RichText = trim(parseRichText(jSONObject2, tLRPC$TL_page));
                } else if ("img".equalsIgnoreCase(optString)) {
                    tLRPC$TL_pageBlockPhoto = parseImage(jSONObject2, tLRPC$TL_page);
                } else if ("source".equalsIgnoreCase(optString)) {
                    String optString2 = jSONObject2.optString("src");
                    if (TextUtils.isEmpty(optString2)) {
                        String optString3 = jSONObject2.optString("srcset");
                        if (!TextUtils.isEmpty(optString3)) {
                            for (String str : optString3.split(",")) {
                                arrayList.add(str.trim().split(" ")[0].trim());
                            }
                        }
                    } else {
                        arrayList.add(optString2);
                    }
                }
            }
        }
        if (tLRPC$TL_pageBlockPhoto == null) {
            return null;
        }
        if (tLRPC$RichText != null) {
            TLRPC$TL_pageCaption tLRPC$TL_pageCaption = new TLRPC$TL_pageCaption();
            tLRPC$TL_pageBlockPhoto.caption = tLRPC$TL_pageCaption;
            tLRPC$TL_pageCaption.text = tLRPC$RichText;
            tLRPC$TL_pageCaption.credit = new TLRPC$TL_textEmpty();
        }
        while (true) {
            if (i >= tLRPC$TL_page.photos.size()) {
                break;
            }
            if ((tLRPC$TL_page.photos.get(i) instanceof WebPhoto) && ((TLRPC$Photo) tLRPC$TL_page.photos.get(i)).id == tLRPC$TL_pageBlockPhoto.photo_id) {
                webPhoto = (WebPhoto) tLRPC$TL_page.photos.get(i);
                break;
            }
            i++;
        }
        if (webPhoto != null) {
            webPhoto.urls.addAll(arrayList);
        }
        return tLRPC$TL_pageBlockPhoto;
    }

    public TLRPC$TL_pageBlockPhoto parseImage(JSONObject jSONObject, TLRPC$TL_page tLRPC$TL_page) {
        TLRPC$TL_pageBlockPhoto tLRPC$TL_pageBlockPhoto = new TLRPC$TL_pageBlockPhoto();
        tLRPC$TL_pageBlockPhoto.caption = new TLRPC$TL_pageCaption();
        String optString = jSONObject.optString("alt");
        if (optString != null) {
            tLRPC$TL_pageBlockPhoto.caption.text = trim(parseRichText(optString));
            tLRPC$TL_pageBlockPhoto.caption.credit = trim(parseRichText(""));
        }
        String optString2 = jSONObject.optString("src");
        if (optString2 == null) {
            return null;
        }
        WebPhoto webPhoto = new WebPhoto();
        webPhoto.instantView = this;
        webPhoto.id = (-1) - tLRPC$TL_page.photos.size();
        webPhoto.url = optString2;
        webPhoto.urls.add(optString2);
        try {
            webPhoto.w = Integer.parseInt(jSONObject.optString("width"));
        } catch (Exception unused) {
        }
        try {
            webPhoto.h = Integer.parseInt(jSONObject.optString("height"));
        } catch (Exception unused2) {
        }
        if (webPhoto.w == 0) {
            webPhoto.w = webPhoto.h;
        }
        if (webPhoto.h == 0) {
            webPhoto.h = webPhoto.w;
        }
        tLRPC$TL_pageBlockPhoto.photo_id = webPhoto.id;
        tLRPC$TL_pageBlockPhoto.url = optString2;
        tLRPC$TL_page.photos.add(webPhoto);
        return tLRPC$TL_pageBlockPhoto;
    }

    public TLRPC$TL_textImage parseInlineImage(JSONObject jSONObject, TLRPC$TL_page tLRPC$TL_page) {
        TLRPC$TL_textImage tLRPC$TL_textImage = new TLRPC$TL_textImage();
        String optString = jSONObject.optString("src");
        if (optString == null) {
            return null;
        }
        WebPhoto webPhoto = new WebPhoto();
        webPhoto.instantView = this;
        webPhoto.id = (-1) - tLRPC$TL_page.photos.size();
        webPhoto.url = optString;
        webPhoto.urls.add(optString);
        try {
            webPhoto.w = Integer.parseInt(jSONObject.optString("width"));
        } catch (Exception unused) {
        }
        try {
            webPhoto.h = Integer.parseInt(jSONObject.optString("height"));
        } catch (Exception unused2) {
        }
        tLRPC$TL_textImage.url = optString;
        tLRPC$TL_page.photos.add(webPhoto);
        if (webPhoto.w == 0) {
            webPhoto.w = webPhoto.h;
        }
        if (webPhoto.h == 0) {
            webPhoto.h = webPhoto.w;
        }
        try {
            tLRPC$TL_textImage.w = Integer.parseInt(jSONObject.optString("width"));
        } catch (Exception unused3) {
        }
        try {
            tLRPC$TL_textImage.h = Integer.parseInt(jSONObject.optString("height"));
        } catch (Exception unused4) {
        }
        if (tLRPC$TL_textImage.w == 0) {
            tLRPC$TL_textImage.w = tLRPC$TL_textImage.h;
        }
        if (tLRPC$TL_textImage.h == 0) {
            tLRPC$TL_textImage.h = tLRPC$TL_textImage.w;
        }
        tLRPC$TL_textImage.photo_id = webPhoto.id;
        return tLRPC$TL_textImage;
    }

    public TLRPC$TL_webPage parseJSON(String str, JSONObject jSONObject) {
        TLRPC$TL_webPage tLRPC$TL_webPage = new TLRPC$TL_webPage();
        tLRPC$TL_webPage.id = 0L;
        tLRPC$TL_webPage.url = str;
        tLRPC$TL_webPage.display_url = str;
        String string = jSONObject.getString("siteName");
        if (string != null && !"null".equals(string)) {
            tLRPC$TL_webPage.flags |= 2;
            tLRPC$TL_webPage.site_name = string;
        }
        String optString = jSONObject.optString("title");
        if (optString != null && !"null".equals(optString)) {
            tLRPC$TL_webPage.flags |= 4;
            tLRPC$TL_webPage.title = optString;
        }
        String optString2 = jSONObject.optString("byline");
        if (optString2 != null && !"null".equals(optString2) && !"by".equalsIgnoreCase(optString2)) {
            tLRPC$TL_webPage.flags |= 256;
            tLRPC$TL_webPage.author = optString2;
        }
        String optString3 = jSONObject.optString("excerpt");
        if (optString3 != null && !"null".equals(optString3)) {
            tLRPC$TL_webPage.flags |= 8;
            tLRPC$TL_webPage.description = optString3;
        }
        JSONArray optJSONArray = jSONObject.optJSONArray("content");
        if (optJSONArray != null && !"null".equals(optJSONArray)) {
            tLRPC$TL_webPage.flags |= 1024;
            tLRPC$TL_webPage.cached_page = parsePage(str, jSONObject);
        }
        return tLRPC$TL_webPage;
    }

    /* JADX WARN: Multi-variable type inference failed */
    public TLRPC$PageBlock parseList(String str, JSONObject jSONObject, TLRPC$TL_page tLRPC$TL_page) {
        TLRPC$TL_pageListItemBlocks tLRPC$TL_pageListItemBlocks;
        TLRPC$TL_pageListOrderedItemBlocks tLRPC$TL_pageListOrderedItemBlocks;
        int i = 0;
        if ("ol".equals(jSONObject.optString("tag"))) {
            TLRPC$TL_pageBlockOrderedList tLRPC$TL_pageBlockOrderedList = new TLRPC$TL_pageBlockOrderedList();
            JSONArray jSONArray = jSONObject.getJSONArray("content");
            while (i < jSONArray.length()) {
                Object obj = jSONArray.get(i);
                if (obj instanceof JSONObject) {
                    JSONObject jSONObject2 = (JSONObject) obj;
                    if ("li".equals(jSONObject2.optString("tag"))) {
                        JSONArray optJSONArray = jSONObject2.optJSONArray("content");
                        if (isInline(optJSONArray)) {
                            TLRPC$TL_pageListOrderedItemText tLRPC$TL_pageListOrderedItemText = new TLRPC$TL_pageListOrderedItemText();
                            tLRPC$TL_pageListOrderedItemText.text = parseRichText(optJSONArray, tLRPC$TL_page);
                            tLRPC$TL_pageListOrderedItemBlocks = tLRPC$TL_pageListOrderedItemText;
                        } else {
                            TLRPC$TL_pageListOrderedItemBlocks tLRPC$TL_pageListOrderedItemBlocks2 = new TLRPC$TL_pageListOrderedItemBlocks();
                            tLRPC$TL_pageListOrderedItemBlocks2.blocks.addAll(parsePageBlocks(str, optJSONArray, tLRPC$TL_page));
                            tLRPC$TL_pageListOrderedItemBlocks = tLRPC$TL_pageListOrderedItemBlocks2;
                        }
                        tLRPC$TL_pageBlockOrderedList.items.add(tLRPC$TL_pageListOrderedItemBlocks);
                    }
                }
                i++;
            }
            return tLRPC$TL_pageBlockOrderedList;
        }
        TLRPC$TL_pageBlockList tLRPC$TL_pageBlockList = new TLRPC$TL_pageBlockList();
        JSONArray jSONArray2 = jSONObject.getJSONArray("content");
        while (i < jSONArray2.length()) {
            Object obj2 = jSONArray2.get(i);
            if (obj2 instanceof JSONObject) {
                JSONObject jSONObject3 = (JSONObject) obj2;
                if ("li".equals(jSONObject3.optString("tag"))) {
                    JSONArray optJSONArray2 = jSONObject3.optJSONArray("content");
                    if (isInline(optJSONArray2)) {
                        TLRPC$TL_pageListItemText tLRPC$TL_pageListItemText = new TLRPC$TL_pageListItemText();
                        tLRPC$TL_pageListItemText.text = parseRichText(optJSONArray2, tLRPC$TL_page);
                        tLRPC$TL_pageListItemBlocks = tLRPC$TL_pageListItemText;
                    } else {
                        TLRPC$TL_pageListItemBlocks tLRPC$TL_pageListItemBlocks2 = new TLRPC$TL_pageListItemBlocks();
                        tLRPC$TL_pageListItemBlocks2.blocks.addAll(parsePageBlocks(str, optJSONArray2, tLRPC$TL_page));
                        tLRPC$TL_pageListItemBlocks = tLRPC$TL_pageListItemBlocks2;
                    }
                    tLRPC$TL_pageBlockList.items.add(tLRPC$TL_pageListItemBlocks);
                }
            }
            i++;
        }
        return tLRPC$TL_pageBlockList;
    }

    public TLRPC$TL_page parsePage(String str, JSONObject jSONObject) {
        String optString = jSONObject.optString("title");
        if ("null".equals(optString)) {
            optString = null;
        }
        "null".equals(jSONObject.optString("publishedTime"));
        JSONArray optJSONArray = jSONObject.optJSONArray("content");
        TLRPC$TL_page tLRPC$TL_page = new TLRPC$TL_page();
        tLRPC$TL_page.web = true;
        tLRPC$TL_page.url = str;
        tLRPC$TL_page.blocks.addAll(parsePageBlocks(str, optJSONArray, tLRPC$TL_page));
        if (tLRPC$TL_page.blocks.isEmpty() || !(tLRPC$TL_page.blocks.get(0) instanceof TLRPC$TL_pageBlockHeader)) {
            TLRPC$TL_pageBlockTitle tLRPC$TL_pageBlockTitle = new TLRPC$TL_pageBlockTitle();
            tLRPC$TL_pageBlockTitle.text = trim(parseRichText(optString));
            tLRPC$TL_page.blocks.add(0, tLRPC$TL_pageBlockTitle);
        }
        return tLRPC$TL_page;
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r3v11, types: [org.telegram.tgnet.TLRPC$TL_pageBlockDivider] */
    /* JADX WARN: Type inference failed for: r3v12, types: [org.telegram.tgnet.TLRPC$PageBlock] */
    /* JADX WARN: Type inference failed for: r3v17, types: [org.telegram.tgnet.TLRPC$TL_pageBlockTable] */
    /* JADX WARN: Type inference failed for: r3v22, types: [org.telegram.tgnet.TLRPC$TL_pageBlockDetails] */
    /* JADX WARN: Type inference failed for: r3v4, types: [org.telegram.tgnet.TLRPC$TL_pageBlockParagraph] */
    /* JADX WARN: Type inference failed for: r4v10, types: [org.telegram.tgnet.TLRPC$TL_pageBlockBlockquote] */
    /* JADX WARN: Type inference failed for: r4v7, types: [org.telegram.tgnet.TLRPC$TL_pageBlockHeader] */
    /* JADX WARN: Type inference failed for: r4v8, types: [org.telegram.tgnet.TLRPC$TL_pageBlockSubheader] */
    /* JADX WARN: Type inference failed for: r4v9, types: [org.telegram.tgnet.TLRPC$TL_pageBlockPreformatted] */
    /* JADX WARN: Type inference failed for: r8v0, types: [org.telegram.ui.web.WebInstantView] */
    public ArrayList parsePageBlocks(String str, JSONArray jSONArray, TLRPC$TL_page tLRPC$TL_page) {
        TLRPC$RichText trim;
        TLRPC$TL_pageBlockPhoto tLRPC$TL_pageBlockPhoto;
        TLRPC$TL_pageBlockParagraph tLRPC$TL_pageBlockParagraph;
        TLRPC$TL_pageBlockParagraph tLRPC$TL_pageBlockParagraph2;
        ArrayList arrayList = new ArrayList();
        for (int i = 0; i < jSONArray.length(); i++) {
            Object obj = jSONArray.get(i);
            if (obj instanceof String) {
                TLRPC$TL_pageBlockParagraph tLRPC$TL_pageBlockParagraph3 = new TLRPC$TL_pageBlockParagraph();
                trim = parseRichText((String) obj);
                tLRPC$TL_pageBlockParagraph2 = tLRPC$TL_pageBlockParagraph3;
            } else {
                if (obj instanceof JSONObject) {
                    JSONObject jSONObject = (JSONObject) obj;
                    String optString = jSONObject.optString("tag");
                    JSONArray optJSONArray = jSONObject.optJSONArray("content");
                    optString.hashCode();
                    char c = 65535;
                    switch (optString.hashCode()) {
                        case -1274639644:
                            if (optString.equals("figure")) {
                                c = 0;
                                break;
                            }
                            break;
                        case -891980137:
                            if (optString.equals("strong")) {
                                c = 1;
                                break;
                            }
                            break;
                        case -577741570:
                            if (optString.equals("picture")) {
                                c = 2;
                                break;
                            }
                            break;
                        case 97:
                            if (optString.equals("a")) {
                                c = 3;
                                break;
                            }
                            break;
                        case 98:
                            if (optString.equals("b")) {
                                c = 4;
                                break;
                            }
                            break;
                        case 105:
                            if (optString.equals("i")) {
                                c = 5;
                                break;
                            }
                            break;
                        case 112:
                            if (optString.equals("p")) {
                                c = 6;
                                break;
                            }
                            break;
                        case 115:
                            if (optString.equals("s")) {
                                c = 7;
                                break;
                            }
                            break;
                        case 3273:
                            if (optString.equals("h1")) {
                                c = '\b';
                                break;
                            }
                            break;
                        case 3274:
                            if (optString.equals("h2")) {
                                c = '\t';
                                break;
                            }
                            break;
                        case 3275:
                            if (optString.equals("h3")) {
                                c = '\n';
                                break;
                            }
                            break;
                        case 3276:
                            if (optString.equals("h4")) {
                                c = 11;
                                break;
                            }
                            break;
                        case 3277:
                            if (optString.equals("h5")) {
                                c = '\f';
                                break;
                            }
                            break;
                        case 3278:
                            if (optString.equals("h6")) {
                                c = '\r';
                                break;
                            }
                            break;
                        case 3338:
                            if (optString.equals("hr")) {
                                c = 14;
                                break;
                            }
                            break;
                        case 3549:
                            if (optString.equals("ol")) {
                                c = 15;
                                break;
                            }
                            break;
                        case 3735:
                            if (optString.equals("ul")) {
                                c = 16;
                                break;
                            }
                            break;
                        case 104387:
                            if (optString.equals("img")) {
                                c = 17;
                                break;
                            }
                            break;
                        case 111267:
                            if (optString.equals("pre")) {
                                c = 18;
                                break;
                            }
                            break;
                        case 114240:
                            if (optString.equals("sub")) {
                                c = 19;
                                break;
                            }
                            break;
                        case 114254:
                            if (optString.equals("sup")) {
                                c = 20;
                                break;
                            }
                            break;
                        case 3059181:
                            if (optString.equals("code")) {
                                c = 21;
                                break;
                            }
                            break;
                        case 3344077:
                            if (optString.equals("mark")) {
                                c = 22;
                                break;
                            }
                            break;
                        case 3536714:
                            if (optString.equals("span")) {
                                c = 23;
                                break;
                            }
                            break;
                        case 110115790:
                            if (optString.equals("table")) {
                                c = 24;
                                break;
                            }
                            break;
                        case 1303202319:
                            if (optString.equals("blockquote")) {
                                c = 25;
                                break;
                            }
                            break;
                        case 1557721666:
                            if (optString.equals("details")) {
                                c = 26;
                                break;
                            }
                            break;
                    }
                    switch (c) {
                        case 0:
                        case 2:
                            TLRPC$TL_pageBlockPhoto parseFigure = parseFigure(jSONObject, tLRPC$TL_page);
                            tLRPC$TL_pageBlockPhoto = parseFigure;
                            if (parseFigure == null) {
                                break;
                            }
                            arrayList.add(tLRPC$TL_pageBlockPhoto);
                            break;
                        case 1:
                        case 3:
                        case 4:
                        case 5:
                        case 7:
                        case 19:
                        case 20:
                        case 21:
                        case 22:
                        case 23:
                            JSONArray jSONArray2 = new JSONArray();
                            jSONArray2.put(jSONObject);
                            ?? tLRPC$TL_pageBlockParagraph4 = new TLRPC$TL_pageBlockParagraph();
                            tLRPC$TL_pageBlockParagraph4.text = parseRichText(jSONArray2, tLRPC$TL_page);
                            tLRPC$TL_pageBlockPhoto = tLRPC$TL_pageBlockParagraph4;
                            arrayList.add(tLRPC$TL_pageBlockPhoto);
                            break;
                        case 6:
                            TLRPC$TL_pageBlockParagraph tLRPC$TL_pageBlockParagraph5 = new TLRPC$TL_pageBlockParagraph();
                            trim = trim(parseRichText(jSONObject, tLRPC$TL_page));
                            tLRPC$TL_pageBlockParagraph2 = tLRPC$TL_pageBlockParagraph5;
                            break;
                        case '\b':
                        case '\t':
                            ?? tLRPC$TL_pageBlockHeader = new TLRPC$TL_pageBlockHeader();
                            tLRPC$TL_pageBlockHeader.text = trim(parseRichText(jSONObject, tLRPC$TL_page));
                            tLRPC$TL_pageBlockParagraph = tLRPC$TL_pageBlockHeader;
                            arrayList.add(tLRPC$TL_pageBlockParagraph);
                            break;
                        case '\n':
                        case 11:
                        case '\f':
                        case '\r':
                            ?? tLRPC$TL_pageBlockSubheader = new TLRPC$TL_pageBlockSubheader();
                            tLRPC$TL_pageBlockSubheader.text = trim(parseRichText(jSONObject, tLRPC$TL_page));
                            tLRPC$TL_pageBlockParagraph = tLRPC$TL_pageBlockSubheader;
                            arrayList.add(tLRPC$TL_pageBlockParagraph);
                            break;
                        case 14:
                            tLRPC$TL_pageBlockPhoto = new TLRPC$TL_pageBlockDivider();
                            arrayList.add(tLRPC$TL_pageBlockPhoto);
                            break;
                        case 15:
                        case 16:
                            tLRPC$TL_pageBlockPhoto = parseList(str, jSONObject, tLRPC$TL_page);
                            arrayList.add(tLRPC$TL_pageBlockPhoto);
                            break;
                        case 17:
                            TLRPC$TL_pageBlockPhoto parseImage = parseImage(jSONObject, tLRPC$TL_page);
                            tLRPC$TL_pageBlockPhoto = parseImage;
                            if (parseImage == null) {
                                break;
                            }
                            arrayList.add(tLRPC$TL_pageBlockPhoto);
                            break;
                        case 18:
                            ?? tLRPC$TL_pageBlockPreformatted = new TLRPC$TL_pageBlockPreformatted();
                            TLRPC$TL_textFixed tLRPC$TL_textFixed = new TLRPC$TL_textFixed();
                            tLRPC$TL_textFixed.text = trim(parseRichText(jSONObject, tLRPC$TL_page));
                            tLRPC$TL_pageBlockPreformatted.text = tLRPC$TL_textFixed;
                            tLRPC$TL_pageBlockPreformatted.language = "";
                            tLRPC$TL_pageBlockParagraph = tLRPC$TL_pageBlockPreformatted;
                            arrayList.add(tLRPC$TL_pageBlockParagraph);
                            break;
                        case 24:
                            tLRPC$TL_pageBlockPhoto = parseTable(str, jSONObject, tLRPC$TL_page);
                            arrayList.add(tLRPC$TL_pageBlockPhoto);
                            break;
                        case 25:
                            ?? tLRPC$TL_pageBlockBlockquote = new TLRPC$TL_pageBlockBlockquote();
                            tLRPC$TL_pageBlockBlockquote.text = trim(parseRichText(jSONObject, tLRPC$TL_page));
                            TLRPC$TL_textItalic tLRPC$TL_textItalic = new TLRPC$TL_textItalic();
                            tLRPC$TL_textItalic.text = tLRPC$TL_pageBlockBlockquote.text;
                            tLRPC$TL_pageBlockBlockquote.text = tLRPC$TL_textItalic;
                            tLRPC$TL_pageBlockParagraph = tLRPC$TL_pageBlockBlockquote;
                            arrayList.add(tLRPC$TL_pageBlockParagraph);
                            break;
                        case 26:
                            ?? parseDetails = parseDetails(str, jSONObject, tLRPC$TL_page);
                            tLRPC$TL_pageBlockPhoto = parseDetails;
                            if (parseDetails == 0) {
                                break;
                            }
                            arrayList.add(tLRPC$TL_pageBlockPhoto);
                            break;
                        default:
                            if (optJSONArray != null) {
                                arrayList.addAll(parsePageBlocks(str, optJSONArray, tLRPC$TL_page));
                                break;
                            } else {
                                break;
                            }
                    }
                }
            }
            tLRPC$TL_pageBlockParagraph2.text = trim;
            tLRPC$TL_pageBlockParagraph = tLRPC$TL_pageBlockParagraph2;
            arrayList.add(tLRPC$TL_pageBlockParagraph);
        }
        return arrayList;
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    public TLRPC$RichText parseRichText(JSONArray jSONArray, TLRPC$TL_page tLRPC$TL_page) {
        TLRPC$RichText parseRichText;
        TLRPC$TL_textPhone tLRPC$TL_textPhone;
        TLRPC$RichText applyAnchor;
        ArrayList arrayList = new ArrayList();
        for (int i = 0; i < jSONArray.length(); i++) {
            Object obj = jSONArray.get(i);
            if (obj instanceof String) {
                applyAnchor = parseRichText((String) obj);
            } else {
                JSONObject jSONObject = (JSONObject) obj;
                String optString = jSONObject.optString("tag");
                optString.hashCode();
                char c = 65535;
                switch (optString.hashCode()) {
                    case -891980137:
                        if (optString.equals("strong")) {
                            c = 0;
                            break;
                        }
                        break;
                    case 97:
                        if (optString.equals("a")) {
                            c = 1;
                            break;
                        }
                        break;
                    case 98:
                        if (optString.equals("b")) {
                            c = 2;
                            break;
                        }
                        break;
                    case 105:
                        if (optString.equals("i")) {
                            c = 3;
                            break;
                        }
                        break;
                    case 112:
                        if (optString.equals("p")) {
                            c = 4;
                            break;
                        }
                        break;
                    case 115:
                        if (optString.equals("s")) {
                            c = 5;
                            break;
                        }
                        break;
                    case 3152:
                        if (optString.equals("br")) {
                            c = 6;
                            break;
                        }
                        break;
                    case 104387:
                        if (optString.equals("img")) {
                            c = 7;
                            break;
                        }
                        break;
                    case 111267:
                        if (optString.equals("pre")) {
                            c = '\b';
                            break;
                        }
                        break;
                    case 114240:
                        if (optString.equals("sub")) {
                            c = '\t';
                            break;
                        }
                        break;
                    case 114254:
                        if (optString.equals("sup")) {
                            c = '\n';
                            break;
                        }
                        break;
                    case 3059181:
                        if (optString.equals("code")) {
                            c = 11;
                            break;
                        }
                        break;
                    case 3344077:
                        if (optString.equals("mark")) {
                            c = '\f';
                            break;
                        }
                        break;
                }
                switch (c) {
                    case 0:
                    case 2:
                        parseRichText = new TLRPC$TL_textBold();
                        parseRichText.text = parseRichText(jSONObject, tLRPC$TL_page);
                        break;
                    case 1:
                        String optString2 = jSONObject.optString("href");
                        if (optString2 != null) {
                            if (optString2.startsWith("tel:")) {
                                TLRPC$TL_textPhone tLRPC$TL_textPhone2 = new TLRPC$TL_textPhone();
                                tLRPC$TL_textPhone2.phone = optString2.substring(4);
                                tLRPC$TL_textPhone = tLRPC$TL_textPhone2;
                            } else if (optString2.startsWith("mailto:")) {
                                TLRPC$TL_textEmail tLRPC$TL_textEmail = new TLRPC$TL_textEmail();
                                tLRPC$TL_textEmail.email = optString2.substring(7);
                                tLRPC$TL_textPhone = tLRPC$TL_textEmail;
                            } else {
                                TLRPC$TL_textUrl tLRPC$TL_textUrl = new TLRPC$TL_textUrl();
                                tLRPC$TL_textUrl.url = optString2;
                                tLRPC$TL_textPhone = tLRPC$TL_textUrl;
                            }
                            tLRPC$TL_textPhone.text = parseRichText(jSONObject, tLRPC$TL_page);
                            parseRichText = tLRPC$TL_textPhone;
                            break;
                        }
                        parseRichText = parseRichText(jSONObject, tLRPC$TL_page);
                        break;
                    case 3:
                        parseRichText = new TLRPC$TL_textItalic();
                        parseRichText.text = parseRichText(jSONObject, tLRPC$TL_page);
                        break;
                    case 4:
                        if (!arrayList.isEmpty()) {
                            addNewLine((TLRPC$RichText) arrayList.get(arrayList.size() - 1));
                        }
                        parseRichText = parseRichText(jSONObject, tLRPC$TL_page);
                        break;
                    case 5:
                        parseRichText = new TLRPC$TL_textStrike();
                        parseRichText.text = parseRichText(jSONObject, tLRPC$TL_page);
                        break;
                    case 6:
                        if (!arrayList.isEmpty()) {
                            addNewLine((TLRPC$RichText) arrayList.get(arrayList.size() - 1));
                        }
                        parseRichText = null;
                        break;
                    case 7:
                        if (!arrayList.isEmpty()) {
                            addLastSpace((TLRPC$RichText) arrayList.get(arrayList.size() - 1));
                        }
                        parseRichText = parseInlineImage(jSONObject, tLRPC$TL_page);
                        break;
                    case '\b':
                    case 11:
                        parseRichText = new TLRPC$TL_textFixed();
                        parseRichText.text = parseRichText(jSONObject, tLRPC$TL_page);
                        break;
                    case '\t':
                        parseRichText = new TLRPC$TL_textSubscript();
                        parseRichText.text = parseRichText(jSONObject, tLRPC$TL_page);
                        break;
                    case '\n':
                        parseRichText = new TLRPC$TL_textSuperscript();
                        parseRichText.text = parseRichText(jSONObject, tLRPC$TL_page);
                        break;
                    case '\f':
                        parseRichText = new TLRPC$TL_textMarked();
                        parseRichText.text = parseRichText(jSONObject, tLRPC$TL_page);
                        break;
                    default:
                        parseRichText = parseRichText(jSONObject, tLRPC$TL_page);
                        break;
                }
                if (parseRichText != null) {
                    applyAnchor = applyAnchor(parseRichText, jSONObject);
                }
            }
            arrayList.add(applyAnchor);
        }
        if (arrayList.isEmpty()) {
            return new TLRPC$TL_textEmpty();
        }
        if (arrayList.size() == 1) {
            return (TLRPC$RichText) arrayList.get(0);
        }
        TLRPC$TL_textConcat tLRPC$TL_textConcat = new TLRPC$TL_textConcat();
        tLRPC$TL_textConcat.texts = arrayList;
        return tLRPC$TL_textConcat;
    }

    public TLRPC$RichText parseRichText(JSONObject jSONObject, TLRPC$TL_page tLRPC$TL_page) {
        TLRPC$RichText applyAnchor = applyAnchor(parseRichText(jSONObject.getJSONArray("content"), tLRPC$TL_page), jSONObject);
        if (jSONObject.has("bold")) {
            TLRPC$TL_textBold tLRPC$TL_textBold = new TLRPC$TL_textBold();
            tLRPC$TL_textBold.text = applyAnchor;
            applyAnchor = tLRPC$TL_textBold;
        }
        if (jSONObject.has("italic")) {
            TLRPC$TL_textItalic tLRPC$TL_textItalic = new TLRPC$TL_textItalic();
            tLRPC$TL_textItalic.text = applyAnchor;
            return tLRPC$TL_textItalic;
        }
        return applyAnchor;
    }

    public TLRPC$TL_pageBlockTable parseTable(String str, JSONObject jSONObject, TLRPC$TL_page tLRPC$TL_page) {
        TLRPC$TL_pageBlockTable tLRPC$TL_pageBlockTable = new TLRPC$TL_pageBlockTable();
        tLRPC$TL_pageBlockTable.bordered = true;
        tLRPC$TL_pageBlockTable.striped = true;
        String optString = jSONObject.optString("title");
        if (optString == null) {
            optString = "";
        }
        tLRPC$TL_pageBlockTable.title = trim(applyAnchor(parseRichText(optString), jSONObject));
        tLRPC$TL_pageBlockTable.rows.addAll(parseTableRows(str, jSONObject.getJSONArray("content"), tLRPC$TL_page));
        return tLRPC$TL_pageBlockTable;
    }

    public TLRPC$TL_pageTableRow parseTableRow(String str, JSONObject jSONObject, TLRPC$TL_page tLRPC$TL_page) {
        JSONObject jSONObject2;
        String optString;
        TLRPC$TL_pageTableRow tLRPC$TL_pageTableRow = new TLRPC$TL_pageTableRow();
        JSONArray jSONArray = jSONObject.getJSONArray("content");
        for (int i = 0; i < jSONArray.length(); i++) {
            Object obj = jSONArray.get(i);
            if ((obj instanceof JSONObject) && (optString = (jSONObject2 = (JSONObject) obj).optString("tag")) != null && ("td".equals(optString) || "th".equals(optString))) {
                TLRPC$TL_pageTableCell tLRPC$TL_pageTableCell = new TLRPC$TL_pageTableCell();
                tLRPC$TL_pageTableCell.header = "th".equals(optString);
                try {
                    tLRPC$TL_pageTableCell.colspan = Integer.parseInt(jSONObject2.optString("colspan"));
                    tLRPC$TL_pageTableCell.flags |= 2;
                } catch (Exception unused) {
                }
                try {
                    tLRPC$TL_pageTableCell.rowspan = Integer.parseInt(jSONObject2.optString("rowspan"));
                    tLRPC$TL_pageTableCell.flags |= 4;
                } catch (Exception unused2) {
                }
                tLRPC$TL_pageTableCell.text = trim(parseRichText(jSONObject2.getJSONArray("content"), tLRPC$TL_page));
                if (jSONObject2.has("bold") || tLRPC$TL_pageTableCell.header) {
                    TLRPC$TL_textBold tLRPC$TL_textBold = new TLRPC$TL_textBold();
                    tLRPC$TL_textBold.text = tLRPC$TL_pageTableCell.text;
                    tLRPC$TL_pageTableCell.text = tLRPC$TL_textBold;
                }
                if (jSONObject2.has("italic")) {
                    TLRPC$TL_textItalic tLRPC$TL_textItalic = new TLRPC$TL_textItalic();
                    tLRPC$TL_textItalic.text = tLRPC$TL_pageTableCell.text;
                    tLRPC$TL_pageTableCell.text = tLRPC$TL_textItalic;
                }
                tLRPC$TL_pageTableCell.align_center = jSONObject2.has("xcenter");
                tLRPC$TL_pageTableRow.cells.add(tLRPC$TL_pageTableCell);
            }
        }
        return tLRPC$TL_pageTableRow;
    }

    public ArrayList parseTableRows(String str, JSONArray jSONArray, TLRPC$TL_page tLRPC$TL_page) {
        ArrayList arrayList = new ArrayList();
        new ArrayList();
        for (int i = 0; i < jSONArray.length(); i++) {
            Object obj = jSONArray.get(i);
            if (obj instanceof JSONObject) {
                JSONObject jSONObject = (JSONObject) obj;
                if ("tr".equals(jSONObject.optString("tag"))) {
                    arrayList.add(parseTableRow(str, jSONObject, tLRPC$TL_page));
                } else {
                    JSONArray optJSONArray = jSONObject.optJSONArray("content");
                    if (optJSONArray != null) {
                        arrayList.addAll(parseTableRows(str, optJSONArray, tLRPC$TL_page));
                    }
                }
            }
        }
        return arrayList;
    }

    public void readHTML(String str, final InputStream inputStream, Utilities.Callback callback) {
        if (callback == null) {
            return;
        }
        if (inputStream == null) {
            callback.run(null);
            return;
        }
        Context context = LaunchActivity.instance;
        if (context == null) {
            context = ApplicationLoader.applicationContext;
        }
        Activity findActivity = AndroidUtilities.findActivity(context);
        if (findActivity == null) {
            callback.run(null);
            return;
        }
        View rootView = findActivity.findViewById(16908290).getRootView();
        if (!(rootView instanceof ViewGroup)) {
            callback.run(null);
            return;
        }
        FrameLayout frameLayout = new FrameLayout(context) { // from class: org.telegram.ui.web.WebInstantView.1
            @Override // android.view.ViewGroup, android.view.View
            public boolean dispatchTouchEvent(MotionEvent motionEvent) {
                return false;
            }

            @Override // android.view.ViewGroup
            protected boolean drawChild(Canvas canvas, View view, long j) {
                return false;
            }

            @Override // android.widget.FrameLayout, android.view.View
            protected void onMeasure(int i, int i2) {
                super.onMeasure(View.MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(500.0f), 1073741824), View.MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(500.0f), 1073741824));
            }

            @Override // android.view.View
            public boolean onTouchEvent(MotionEvent motionEvent) {
                return false;
            }
        };
        ((ViewGroup) rootView).addView(frameLayout);
        WebView webView = new WebView(context);
        WebSettings settings = webView.getSettings();
        settings.setAllowContentAccess(false);
        settings.setDatabaseEnabled(false);
        settings.setAllowFileAccess(false);
        settings.setJavaScriptEnabled(true);
        settings.setSaveFormData(false);
        settings.setGeolocationEnabled(false);
        settings.setDomStorageEnabled(false);
        settings.setAllowFileAccessFromFileURLs(false);
        settings.setAllowUniversalAccessFromFileURLs(false);
        webView.setWebViewClient(new WebViewClient() { // from class: org.telegram.ui.web.WebInstantView.2
            private boolean firstLoad = true;
            private boolean streamLoaded;

            @Override // android.webkit.WebViewClient
            public WebResourceResponse shouldInterceptRequest(WebView webView2, String str2) {
                InputStream inputStream2;
                String str3;
                if (this.firstLoad) {
                    this.firstLoad = false;
                    String readRes = AndroidUtilities.readRes(R.raw.instant);
                    String replace = readRes.replace("$DEBUG$", "" + BuildVars.DEBUG_VERSION);
                    return new WebResourceResponse("text/html", "UTF-8", new ByteArrayInputStream(("<script>\n" + replace + "\n</script>").getBytes(StandardCharsets.UTF_8)));
                }
                if (str2 == null || !str2.endsWith("/index.html")) {
                    MHTML mhtml = WebInstantView.this.mhtml;
                    MHTML.Entry entry = mhtml != null ? (MHTML.Entry) mhtml.entriesByLocation.get(str2) : null;
                    if (entry == null) {
                        return Build.VERSION.SDK_INT < 21 ? new WebResourceResponse("text/html", "UTF-8", null) : new WebResourceResponse("text/plain", "utf-8", 404, "Not Found", null, null);
                    }
                    String type = entry.getType();
                    if (!"text/html".equalsIgnoreCase(type) && !"text/css".equalsIgnoreCase(type)) {
                        return Build.VERSION.SDK_INT < 21 ? new WebResourceResponse(type, "UTF-8", null) : new WebResourceResponse("text/plain", "utf-8", 404, "Not Found", null, null);
                    }
                    try {
                        inputStream2 = entry.getInputStream();
                        str3 = type;
                    } catch (IOException e) {
                        FileLog.e(e);
                        return Build.VERSION.SDK_INT < 21 ? new WebResourceResponse(type, "UTF-8", null) : new WebResourceResponse("text/plain", "utf-8", 503, "Server error", null, null);
                    }
                } else {
                    str3 = "application/octet-stream";
                    if (this.streamLoaded) {
                        MHTML mhtml2 = WebInstantView.this.mhtml;
                        MHTML.Entry entry2 = mhtml2 != null ? (MHTML.Entry) mhtml2.entries.get(0) : null;
                        if (entry2 == null) {
                            return Build.VERSION.SDK_INT < 21 ? new WebResourceResponse("application/octet-stream", "UTF-8", null) : new WebResourceResponse("text/plain", "utf-8", 404, "Not Found", null, null);
                        }
                        try {
                            inputStream2 = entry2.getInputStream();
                        } catch (IOException e2) {
                            FileLog.e(e2);
                            return Build.VERSION.SDK_INT < 21 ? new WebResourceResponse("application/octet-stream", "UTF-8", null) : new WebResourceResponse("text/plain", "utf-8", 503, "Server error", null, null);
                        }
                    } else {
                        inputStream2 = inputStream;
                        this.streamLoaded = true;
                    }
                }
                return new WebResourceResponse(str3, null, inputStream2);
            }
        });
        webView.setWebChromeClient(new WebChromeClient() { // from class: org.telegram.ui.web.WebInstantView.3
        });
        frameLayout.addView(webView, LayoutHelper.createFrame(-1, -1.0f));
        webView.addJavascriptInterface(new 4(new boolean[]{false}, webView, frameLayout, callback), "Instant");
        webView.loadUrl(str);
    }

    public void recycle() {
        TLRPC$Page tLRPC$Page;
        ArrayList arrayList;
        instants.remove(this.webpage);
        for (Map.Entry entry : this.loadedPhotos.entrySet()) {
            AndroidUtilities.recycleBitmap((Bitmap) entry.getValue());
        }
        this.loadedPhotos.clear();
        TLRPC$WebPage tLRPC$WebPage = this.webpage;
        if (tLRPC$WebPage == null || (tLRPC$Page = tLRPC$WebPage.cached_page) == null || (arrayList = tLRPC$Page.photos) == null) {
            return;
        }
        Iterator it = arrayList.iterator();
        while (it.hasNext()) {
            TLRPC$Photo tLRPC$Photo = (TLRPC$Photo) it.next();
            if (tLRPC$Photo instanceof WebPhoto) {
                WebPhoto webPhoto = (WebPhoto) tLRPC$Photo;
                HashMap hashMap = loadingPhotos;
                if (hashMap != null) {
                    hashMap.remove(webPhoto.url);
                }
            }
        }
    }
}