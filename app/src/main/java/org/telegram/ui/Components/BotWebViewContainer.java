package org.telegram.ui.Components;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.webkit.GeolocationPermissions;
import android.webkit.JavascriptInterface;
import android.webkit.PermissionRequest;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.TextView;
import androidx.core.graphics.ColorUtils;
import androidx.core.util.Consumer;
import androidx.core.util.ObjectsCompat$$ExternalSyntheticBackport0;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import org.json.JSONException;
import org.json.JSONObject;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.ApplicationLoader;
import org.telegram.messenger.BotWebViewVibrationEffect;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.ImageLocation;
import org.telegram.messenger.ImageReceiver;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MediaDataController;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.SvgHelper;
import org.telegram.messenger.UserObject;
import org.telegram.messenger.browser.Browser;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC$TL_attachMenuBot;
import org.telegram.tgnet.TLRPC$TL_attachMenuBotIcon;
import org.telegram.tgnet.TLRPC$TL_attachMenuBotsBot;
import org.telegram.tgnet.TLRPC$TL_error;
import org.telegram.tgnet.TLRPC$TL_inputInvoiceSlug;
import org.telegram.tgnet.TLRPC$TL_messages_getAttachMenuBot;
import org.telegram.tgnet.TLRPC$TL_payments_getPaymentForm;
import org.telegram.tgnet.TLRPC$User;
import org.telegram.ui.ActionBar.ActionBar;
import org.telegram.ui.ActionBar.ActionBarMenuSubItem;
import org.telegram.ui.ActionBar.AlertDialog;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Components.ChatAttachAlertBotWebViewLayout;
import org.telegram.ui.Components.voip.CellFlickerDrawable;
/* loaded from: classes3.dex */
public class BotWebViewContainer extends FrameLayout implements NotificationCenter.NotificationCenterDelegate {
    private static final List<String> WHITELISTED_SCHEMES = Arrays.asList("http", "https");
    private TLRPC$User botUser;
    private String buttonData;
    private int currentAccount;
    private String currentPaymentSlug;
    private Delegate delegate;
    private BackupImageView flickerView;
    private boolean hasUserPermissions;
    private boolean isBackButtonVisible;
    private boolean isFlickeringCenter;
    private boolean isPageLoaded;
    private boolean isRequestingPageOpen;
    private boolean isViewPortByMeasureSuppressed;
    private long lastClickMs;
    private boolean lastExpanded;
    private ValueCallback<Uri[]> mFilePathCallback;
    private String mUrl;
    private Runnable onPermissionsRequestResultCallback;
    private Activity parentActivity;
    private Theme.ResourcesProvider resourcesProvider;
    private WebView webView;
    private boolean webViewNotAvailable;
    private TextView webViewNotAvailableText;
    private Consumer<Float> webViewProgressListener;
    private WebViewScrollListener webViewScrollListener;
    private CellFlickerDrawable flickerDrawable = new CellFlickerDrawable();
    private int lastButtonColor = getColor("featuredStickers_addButton");
    private int lastButtonTextColor = getColor("featuredStickers_buttonText");
    private String lastButtonText = "";

    /* loaded from: classes3.dex */
    public interface Delegate {

        /* renamed from: org.telegram.ui.Components.BotWebViewContainer$Delegate$-CC */
        /* loaded from: classes3.dex */
        public final /* synthetic */ class CC {
            public static void $default$onSendWebViewData(Delegate delegate, String str) {
            }

            public static void $default$onWebAppReady(Delegate delegate) {
            }
        }

        void onCloseRequested(Runnable runnable);

        void onSendWebViewData(String str);

        void onSetBackButtonVisible(boolean z);

        void onSetupMainButton(boolean z, boolean z2, String str, int i, int i2, boolean z3);

        void onWebAppExpand();

        void onWebAppOpenInvoice(String str, TLObject tLObject);

        void onWebAppReady();

        void onWebAppSetActionBarColor(String str);

        void onWebAppSetBackgroundColor(int i);
    }

    /* loaded from: classes3.dex */
    public interface WebViewScrollListener {
        void onWebViewScrolled(WebView webView, int i, int i2);
    }

    public static /* synthetic */ void lambda$evaluateJs$6(String str) {
    }

    public BotWebViewContainer(Context context, Theme.ResourcesProvider resourcesProvider, int i) {
        super(context);
        this.resourcesProvider = resourcesProvider;
        if (context instanceof Activity) {
            this.parentActivity = (Activity) context;
        }
        CellFlickerDrawable cellFlickerDrawable = this.flickerDrawable;
        cellFlickerDrawable.drawFrame = false;
        cellFlickerDrawable.setColors(i, 153, 204);
        AnonymousClass1 anonymousClass1 = new AnonymousClass1(context);
        this.flickerView = anonymousClass1;
        anonymousClass1.setColorFilter(new PorterDuffColorFilter(getColor("dialogSearchHint"), PorterDuff.Mode.SRC_IN));
        this.flickerView.getImageReceiver().setAspectFit(true);
        addView(this.flickerView, LayoutHelper.createFrame(-1, -2, 48));
        TextView textView = new TextView(context);
        this.webViewNotAvailableText = textView;
        textView.setText(LocaleController.getString(2131624742));
        this.webViewNotAvailableText.setTextColor(getColor("windowBackgroundWhiteGrayText"));
        this.webViewNotAvailableText.setTextSize(1, 15.0f);
        this.webViewNotAvailableText.setGravity(17);
        this.webViewNotAvailableText.setVisibility(8);
        int dp = AndroidUtilities.dp(16.0f);
        this.webViewNotAvailableText.setPadding(dp, dp, dp, dp);
        addView(this.webViewNotAvailableText, LayoutHelper.createFrame(-1, -2, 17));
        setFocusable(false);
    }

    /* renamed from: org.telegram.ui.Components.BotWebViewContainer$1 */
    /* loaded from: classes3.dex */
    public class AnonymousClass1 extends BackupImageView {
        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass1(Context context) {
            super(context);
            BotWebViewContainer.this = r1;
            this.imageReceiver = new C00161(this);
        }

        /* renamed from: org.telegram.ui.Components.BotWebViewContainer$1$1 */
        /* loaded from: classes3.dex */
        public class C00161 extends ImageReceiver {
            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            C00161(View view) {
                super(view);
                AnonymousClass1.this = r1;
            }

            @Override // org.telegram.messenger.ImageReceiver
            public boolean setImageBitmapByKey(Drawable drawable, String str, int i, boolean z, int i2) {
                boolean imageBitmapByKey = super.setImageBitmapByKey(drawable, str, i, z, i2);
                ValueAnimator duration = ValueAnimator.ofFloat(0.0f, 1.0f).setDuration(300L);
                duration.addUpdateListener(new BotWebViewContainer$1$1$$ExternalSyntheticLambda0(this));
                duration.start();
                return imageBitmapByKey;
            }

            public /* synthetic */ void lambda$setImageBitmapByKey$0(ValueAnimator valueAnimator) {
                AnonymousClass1.this.imageReceiver.setAlpha(((Float) valueAnimator.getAnimatedValue()).floatValue());
                invalidate();
            }
        }

        @Override // org.telegram.ui.Components.BackupImageView, android.view.View
        public void onDraw(Canvas canvas) {
            if (BotWebViewContainer.this.isFlickeringCenter) {
                super.onDraw(canvas);
                return;
            }
            Drawable drawable = this.imageReceiver.getDrawable();
            if (drawable == null) {
                return;
            }
            this.imageReceiver.setImageCoords(0.0f, 0.0f, getWidth(), drawable.getIntrinsicHeight() * (getWidth() / drawable.getIntrinsicWidth()));
            this.imageReceiver.draw(canvas);
        }
    }

    public void setViewPortByMeasureSuppressed(boolean z) {
        this.isViewPortByMeasureSuppressed = z;
    }

    private void checkCreateWebView() {
        if (this.webView != null || this.webViewNotAvailable) {
            return;
        }
        try {
            setupWebView();
        } catch (Throwable th) {
            FileLog.e(th);
            this.flickerView.setVisibility(8);
            this.webViewNotAvailable = true;
            this.webViewNotAvailableText.setVisibility(0);
            if (this.webView == null) {
                return;
            }
            removeView(this.webView);
        }
    }

    @SuppressLint({"SetJavaScriptEnabled", "AddJavascriptInterface"})
    private void setupWebView() {
        WebView webView = this.webView;
        if (webView != null) {
            webView.destroy();
            removeView(this.webView);
        }
        AnonymousClass2 anonymousClass2 = new AnonymousClass2(getContext());
        this.webView = anonymousClass2;
        anonymousClass2.setBackgroundColor(getColor("windowBackgroundWhite"));
        WebSettings settings = this.webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setGeolocationEnabled(true);
        settings.setDomStorageEnabled(true);
        settings.setDatabaseEnabled(true);
        File file = new File(ApplicationLoader.getFilesDirFixed(), "webview_database");
        if ((file.exists() && file.isDirectory()) || file.mkdirs()) {
            settings.setDatabasePath(file.getAbsolutePath());
        }
        GeolocationPermissions.getInstance().clearAll();
        this.webView.setVerticalScrollBarEnabled(false);
        this.webView.setWebViewClient(new AnonymousClass3());
        this.webView.setWebChromeClient(new AnonymousClass4());
        this.webView.setAlpha(0.0f);
        addView(this.webView);
        if (Build.VERSION.SDK_INT >= 17) {
            this.webView.addJavascriptInterface(new WebViewProxy(this, null), "TelegramWebviewProxy");
        }
    }

    /* renamed from: org.telegram.ui.Components.BotWebViewContainer$2 */
    /* loaded from: classes3.dex */
    public class AnonymousClass2 extends WebView {
        private int prevScrollX;
        private int prevScrollY;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass2(Context context) {
            super(context);
            BotWebViewContainer.this = r1;
        }

        @Override // android.webkit.WebView, android.view.View
        protected void onScrollChanged(int i, int i2, int i3, int i4) {
            super.onScrollChanged(i, i2, i3, i4);
            if (BotWebViewContainer.this.webViewScrollListener != null) {
                BotWebViewContainer.this.webViewScrollListener.onWebViewScrolled(this, getScrollX() - this.prevScrollX, getScrollY() - this.prevScrollY);
            }
            this.prevScrollX = getScrollX();
            this.prevScrollY = getScrollY();
        }

        @Override // android.view.View
        public void setScrollX(int i) {
            super.setScrollX(i);
            this.prevScrollX = i;
        }

        @Override // android.view.View
        public void setScrollY(int i) {
            super.setScrollY(i);
            this.prevScrollY = i;
        }

        @Override // android.webkit.WebView, android.view.View
        public boolean onCheckIsTextEditor() {
            return BotWebViewContainer.this.isFocusable();
        }

        @Override // android.webkit.WebView, android.widget.AbsoluteLayout, android.view.View
        protected void onMeasure(int i, int i2) {
            super.onMeasure(i, View.MeasureSpec.makeMeasureSpec(View.MeasureSpec.getSize(i2), 1073741824));
        }

        @Override // android.webkit.WebView, android.view.View
        @SuppressLint({"ClickableViewAccessibility"})
        public boolean onTouchEvent(MotionEvent motionEvent) {
            if (motionEvent.getAction() == 0) {
                BotWebViewContainer.this.lastClickMs = System.currentTimeMillis();
            }
            return super.onTouchEvent(motionEvent);
        }
    }

    /* renamed from: org.telegram.ui.Components.BotWebViewContainer$3 */
    /* loaded from: classes3.dex */
    public class AnonymousClass3 extends WebViewClient {
        AnonymousClass3() {
            BotWebViewContainer.this = r1;
        }

        @Override // android.webkit.WebViewClient
        public boolean shouldOverrideUrlLoading(WebView webView, String str) {
            Uri parse = Uri.parse(BotWebViewContainer.this.mUrl);
            Uri parse2 = Uri.parse(str);
            if (!BotWebViewContainer.this.isPageLoaded || (ObjectsCompat$$ExternalSyntheticBackport0.m(parse.getHost(), parse2.getHost()) && ObjectsCompat$$ExternalSyntheticBackport0.m(parse.getPath(), parse2.getPath()))) {
                return false;
            }
            if (!BotWebViewContainer.WHITELISTED_SCHEMES.contains(parse2.getScheme())) {
                return true;
            }
            BotWebViewContainer.this.onOpenUri(parse2);
            return true;
        }

        @Override // android.webkit.WebViewClient
        public void onPageFinished(WebView webView, String str) {
            BotWebViewContainer.this.setPageLoaded(str);
        }
    }

    /* renamed from: org.telegram.ui.Components.BotWebViewContainer$4 */
    /* loaded from: classes3.dex */
    public class AnonymousClass4 extends WebChromeClient {
        private Dialog lastPermissionsDialog;

        AnonymousClass4() {
            BotWebViewContainer.this = r1;
        }

        @Override // android.webkit.WebChromeClient
        public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> valueCallback, WebChromeClient.FileChooserParams fileChooserParams) {
            Context context = BotWebViewContainer.this.getContext();
            if (!(context instanceof Activity)) {
                return false;
            }
            Activity activity = (Activity) context;
            if (BotWebViewContainer.this.mFilePathCallback != null) {
                BotWebViewContainer.this.mFilePathCallback.onReceiveValue(null);
            }
            BotWebViewContainer.this.mFilePathCallback = valueCallback;
            if (Build.VERSION.SDK_INT >= 21) {
                activity.startActivityForResult(fileChooserParams.createIntent(), 3000);
                return true;
            }
            Intent intent = new Intent("android.intent.action.GET_CONTENT");
            intent.addCategory("android.intent.category.OPENABLE");
            intent.setType("*/*");
            activity.startActivityForResult(Intent.createChooser(intent, LocaleController.getString(2131624741)), 3000);
            return true;
        }

        @Override // android.webkit.WebChromeClient
        public void onProgressChanged(WebView webView, int i) {
            if (BotWebViewContainer.this.webViewProgressListener != null) {
                BotWebViewContainer.this.webViewProgressListener.accept(Float.valueOf(i / 100.0f));
            }
        }

        @Override // android.webkit.WebChromeClient
        public void onGeolocationPermissionsShowPrompt(String str, GeolocationPermissions.Callback callback) {
            if (BotWebViewContainer.this.parentActivity != null) {
                Dialog createWebViewPermissionsRequestDialog = AlertsCreator.createWebViewPermissionsRequestDialog(BotWebViewContainer.this.parentActivity, BotWebViewContainer.this.resourcesProvider, new String[]{"android.permission.ACCESS_COARSE_LOCATION", "android.permission.ACCESS_FINE_LOCATION"}, 2131558496, LocaleController.formatString(2131624749, UserObject.getUserName(BotWebViewContainer.this.botUser)), LocaleController.formatString(2131624750, UserObject.getUserName(BotWebViewContainer.this.botUser)), new BotWebViewContainer$4$$ExternalSyntheticLambda1(this, callback, str));
                this.lastPermissionsDialog = createWebViewPermissionsRequestDialog;
                createWebViewPermissionsRequestDialog.show();
                return;
            }
            callback.invoke(str, false, false);
        }

        public /* synthetic */ void lambda$onGeolocationPermissionsShowPrompt$1(GeolocationPermissions.Callback callback, String str, Boolean bool) {
            if (this.lastPermissionsDialog != null) {
                this.lastPermissionsDialog = null;
                if (bool.booleanValue()) {
                    BotWebViewContainer.this.runWithPermissions(new String[]{"android.permission.ACCESS_COARSE_LOCATION", "android.permission.ACCESS_FINE_LOCATION"}, new BotWebViewContainer$4$$ExternalSyntheticLambda0(this, callback, str));
                } else {
                    callback.invoke(str, false, false);
                }
            }
        }

        public /* synthetic */ void lambda$onGeolocationPermissionsShowPrompt$0(GeolocationPermissions.Callback callback, String str, Boolean bool) {
            callback.invoke(str, bool.booleanValue(), false);
            if (bool.booleanValue()) {
                BotWebViewContainer.this.hasUserPermissions = true;
            }
        }

        @Override // android.webkit.WebChromeClient
        public void onGeolocationPermissionsHidePrompt() {
            Dialog dialog = this.lastPermissionsDialog;
            if (dialog != null) {
                dialog.dismiss();
                this.lastPermissionsDialog = null;
            }
        }

        @Override // android.webkit.WebChromeClient
        public void onPermissionRequest(PermissionRequest permissionRequest) {
            Dialog dialog = this.lastPermissionsDialog;
            if (dialog != null) {
                dialog.dismiss();
                this.lastPermissionsDialog = null;
            }
            String[] resources = permissionRequest.getResources();
            if (resources.length == 1) {
                String str = resources[0];
                if (BotWebViewContainer.this.parentActivity == null) {
                    permissionRequest.deny();
                    return;
                }
                str.hashCode();
                if (str.equals("android.webkit.resource.VIDEO_CAPTURE")) {
                    Dialog createWebViewPermissionsRequestDialog = AlertsCreator.createWebViewPermissionsRequestDialog(BotWebViewContainer.this.parentActivity, BotWebViewContainer.this.resourcesProvider, new String[]{"android.permission.CAMERA"}, 2131558493, LocaleController.formatString(2131624746, UserObject.getUserName(BotWebViewContainer.this.botUser)), LocaleController.formatString(2131624747, UserObject.getUserName(BotWebViewContainer.this.botUser)), new BotWebViewContainer$4$$ExternalSyntheticLambda2(this, permissionRequest, str));
                    this.lastPermissionsDialog = createWebViewPermissionsRequestDialog;
                    createWebViewPermissionsRequestDialog.show();
                } else if (!str.equals("android.webkit.resource.AUDIO_CAPTURE")) {
                } else {
                    Dialog createWebViewPermissionsRequestDialog2 = AlertsCreator.createWebViewPermissionsRequestDialog(BotWebViewContainer.this.parentActivity, BotWebViewContainer.this.resourcesProvider, new String[]{"android.permission.RECORD_AUDIO"}, 2131558497, LocaleController.formatString(2131624751, UserObject.getUserName(BotWebViewContainer.this.botUser)), LocaleController.formatString(2131624752, UserObject.getUserName(BotWebViewContainer.this.botUser)), new BotWebViewContainer$4$$ExternalSyntheticLambda4(this, permissionRequest, str));
                    this.lastPermissionsDialog = createWebViewPermissionsRequestDialog2;
                    createWebViewPermissionsRequestDialog2.show();
                }
            }
        }

        public /* synthetic */ void lambda$onPermissionRequest$3(PermissionRequest permissionRequest, String str, Boolean bool) {
            if (this.lastPermissionsDialog != null) {
                this.lastPermissionsDialog = null;
                if (bool.booleanValue()) {
                    BotWebViewContainer.this.runWithPermissions(new String[]{"android.permission.RECORD_AUDIO"}, new BotWebViewContainer$4$$ExternalSyntheticLambda5(this, permissionRequest, str));
                } else {
                    permissionRequest.deny();
                }
            }
        }

        public /* synthetic */ void lambda$onPermissionRequest$2(PermissionRequest permissionRequest, String str, Boolean bool) {
            if (bool.booleanValue()) {
                permissionRequest.grant(new String[]{str});
                BotWebViewContainer.this.hasUserPermissions = true;
                return;
            }
            permissionRequest.deny();
        }

        public /* synthetic */ void lambda$onPermissionRequest$5(PermissionRequest permissionRequest, String str, Boolean bool) {
            if (this.lastPermissionsDialog != null) {
                this.lastPermissionsDialog = null;
                if (bool.booleanValue()) {
                    BotWebViewContainer.this.runWithPermissions(new String[]{"android.permission.CAMERA"}, new BotWebViewContainer$4$$ExternalSyntheticLambda3(this, permissionRequest, str));
                } else {
                    permissionRequest.deny();
                }
            }
        }

        public /* synthetic */ void lambda$onPermissionRequest$4(PermissionRequest permissionRequest, String str, Boolean bool) {
            if (bool.booleanValue()) {
                permissionRequest.grant(new String[]{str});
                BotWebViewContainer.this.hasUserPermissions = true;
                return;
            }
            permissionRequest.deny();
        }

        @Override // android.webkit.WebChromeClient
        public void onPermissionRequestCanceled(PermissionRequest permissionRequest) {
            Dialog dialog = this.lastPermissionsDialog;
            if (dialog != null) {
                dialog.dismiss();
                this.lastPermissionsDialog = null;
            }
        }
    }

    public void onOpenUri(Uri uri) {
        onOpenUri(uri, false);
    }

    private void onOpenUri(Uri uri, boolean z) {
        if (!this.isRequestingPageOpen) {
            if (System.currentTimeMillis() - this.lastClickMs > 10000 && z) {
                return;
            }
            this.lastClickMs = 0L;
            boolean[] zArr = {false};
            if (!Browser.isInternalUri(uri, zArr) || zArr[0]) {
                if (z) {
                    Browser.openUrl(getContext(), uri, true, false);
                    return;
                }
                this.isRequestingPageOpen = true;
                new AlertDialog.Builder(getContext(), this.resourcesProvider).setTitle(LocaleController.getString(2131627108)).setMessage(LocaleController.formatString(2131627105, uri.toString())).setPositiveButton(LocaleController.getString(2131627090), new BotWebViewContainer$$ExternalSyntheticLambda0(this, uri)).setNegativeButton(LocaleController.getString(2131624819), null).setOnDismissListener(new BotWebViewContainer$$ExternalSyntheticLambda1(this)).show();
            } else if (this.delegate != null) {
                setDescendantFocusability(393216);
                setFocusable(false);
                this.webView.setFocusable(false);
                this.webView.setDescendantFocusability(393216);
                this.webView.clearFocus();
                ((InputMethodManager) getContext().getSystemService("input_method")).hideSoftInputFromWindow(getWindowToken(), 2);
                this.delegate.onCloseRequested(new BotWebViewContainer$$ExternalSyntheticLambda3(this, uri));
            } else {
                Browser.openUrl(getContext(), uri, true, false);
            }
        }
    }

    public /* synthetic */ void lambda$onOpenUri$0(Uri uri) {
        Browser.openUrl(getContext(), uri, true, false);
    }

    public /* synthetic */ void lambda$onOpenUri$1(Uri uri, DialogInterface dialogInterface, int i) {
        Browser.openUrl(getContext(), uri, true, false);
    }

    public /* synthetic */ void lambda$onOpenUri$2(DialogInterface dialogInterface) {
        this.isRequestingPageOpen = false;
    }

    public static int getMainButtonRippleColor(int i) {
        return ColorUtils.calculateLuminance(i) >= 0.30000001192092896d ? 301989888 : 385875967;
    }

    public static Drawable getMainButtonRippleDrawable(int i) {
        return Theme.createSelectorWithBackgroundDrawable(i, getMainButtonRippleColor(i));
    }

    public void updateFlickerBackgroundColor(int i) {
        this.flickerDrawable.setColors(i, 153, 204);
    }

    public boolean onBackPressed() {
        if (this.webView != null && this.isBackButtonVisible) {
            notifyEvent("back_button_pressed", null);
            return true;
        }
        return false;
    }

    public void setPageLoaded(String str) {
        if (this.isPageLoaded) {
            return;
        }
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(ObjectAnimator.ofFloat(this.webView, View.ALPHA, 1.0f), ObjectAnimator.ofFloat(this.flickerView, View.ALPHA, 0.0f));
        animatorSet.addListener(new AnonymousClass5());
        animatorSet.start();
        this.mUrl = str;
        this.isPageLoaded = true;
        setFocusable(true);
        this.delegate.onWebAppReady();
    }

    /* renamed from: org.telegram.ui.Components.BotWebViewContainer$5 */
    /* loaded from: classes3.dex */
    public class AnonymousClass5 extends AnimatorListenerAdapter {
        AnonymousClass5() {
            BotWebViewContainer.this = r1;
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator) {
            BotWebViewContainer.this.flickerView.setVisibility(8);
        }
    }

    public boolean hasUserPermissions() {
        return this.hasUserPermissions;
    }

    public void setBotUser(TLRPC$User tLRPC$User) {
        this.botUser = tLRPC$User;
    }

    public void runWithPermissions(String[] strArr, Consumer<Boolean> consumer) {
        if (Build.VERSION.SDK_INT < 23) {
            consumer.accept(Boolean.TRUE);
        } else if (checkPermissions(strArr)) {
            consumer.accept(Boolean.TRUE);
        } else {
            this.onPermissionsRequestResultCallback = new BotWebViewContainer$$ExternalSyntheticLambda4(this, consumer, strArr);
            Activity activity = this.parentActivity;
            if (activity == null) {
                return;
            }
            activity.requestPermissions(strArr, 4000);
        }
    }

    public /* synthetic */ void lambda$runWithPermissions$3(Consumer consumer, String[] strArr) {
        consumer.accept(Boolean.valueOf(checkPermissions(strArr)));
    }

    public boolean isPageLoaded() {
        return this.isPageLoaded;
    }

    public void setParentActivity(Activity activity) {
        this.parentActivity = activity;
    }

    private boolean checkPermissions(String[] strArr) {
        for (String str : strArr) {
            if (getContext().checkSelfPermission(str) != 0) {
                return false;
            }
        }
        return true;
    }

    public void restoreButtonData() {
        String str = this.buttonData;
        if (str != null) {
            onEventReceived("web_app_setup_main_button", str);
        }
    }

    public void onInvoiceStatusUpdate(String str, String str2) {
        onInvoiceStatusUpdate(str, str2, false);
    }

    public void onInvoiceStatusUpdate(String str, String str2, boolean z) {
        try {
            JSONObject jSONObject = new JSONObject();
            jSONObject.put("slug", str);
            jSONObject.put("status", str2);
            notifyEvent("invoice_closed", jSONObject);
            if (z || !ObjectsCompat$$ExternalSyntheticBackport0.m(this.currentPaymentSlug, str)) {
                return;
            }
            this.currentPaymentSlug = null;
        } catch (JSONException e) {
            FileLog.e(e);
        }
    }

    public void onSettingsButtonPressed() {
        this.lastClickMs = System.currentTimeMillis();
        notifyEvent("settings_button_pressed", null);
    }

    public void onMainButtonPressed() {
        this.lastClickMs = System.currentTimeMillis();
        notifyEvent("main_button_pressed", null);
    }

    public void onRequestPermissionsResult(int i, String[] strArr, int[] iArr) {
        Runnable runnable;
        if (i != 4000 || (runnable = this.onPermissionsRequestResultCallback) == null) {
            return;
        }
        runnable.run();
        this.onPermissionsRequestResultCallback = null;
    }

    public void onActivityResult(int i, int i2, Intent intent) {
        if (i != 3000 || this.mFilePathCallback == null) {
            return;
        }
        this.mFilePathCallback.onReceiveValue((i2 != -1 || intent == null || intent.getDataString() == null) ? null : new Uri[]{Uri.parse(intent.getDataString())});
        this.mFilePathCallback = null;
    }

    @Override // android.view.View
    protected void onSizeChanged(int i, int i2, int i3, int i4) {
        super.onSizeChanged(i, i2, i3, i4);
        if (!this.isViewPortByMeasureSuppressed) {
            invalidateViewPortHeight(true);
        }
    }

    public void invalidateViewPortHeight() {
        invalidateViewPortHeight(false);
    }

    public void invalidateViewPortHeight(boolean z) {
        invalidateViewPortHeight(z, false);
    }

    public void invalidateViewPortHeight(boolean z, boolean z2) {
        invalidate();
        if ((this.isPageLoaded || z2) && (getParent() instanceof ChatAttachAlertBotWebViewLayout.WebViewSwipeContainer)) {
            ChatAttachAlertBotWebViewLayout.WebViewSwipeContainer webViewSwipeContainer = (ChatAttachAlertBotWebViewLayout.WebViewSwipeContainer) getParent();
            if (z) {
                this.lastExpanded = webViewSwipeContainer.getSwipeOffsetY() == (-webViewSwipeContainer.getOffsetY()) + webViewSwipeContainer.getTopActionBarOffsetY();
            }
            int measuredHeight = (int) (((webViewSwipeContainer.getMeasuredHeight() - webViewSwipeContainer.getOffsetY()) - webViewSwipeContainer.getSwipeOffsetY()) + webViewSwipeContainer.getTopActionBarOffsetY());
            try {
                JSONObject jSONObject = new JSONObject();
                jSONObject.put("height", measuredHeight / AndroidUtilities.density);
                jSONObject.put("is_state_stable", z);
                jSONObject.put("is_expanded", this.lastExpanded);
                notifyEvent("viewport_changed", jSONObject);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override // android.view.ViewGroup
    protected boolean drawChild(Canvas canvas, View view, long j) {
        if (view == this.flickerView) {
            if (this.isFlickeringCenter) {
                canvas.save();
                canvas.translate(0.0f, (ActionBar.getCurrentActionBarHeight() - ((View) getParent()).getTranslationY()) / 2.0f);
            }
            boolean drawChild = super.drawChild(canvas, view, j);
            if (this.isFlickeringCenter) {
                canvas.restore();
            }
            RectF rectF = AndroidUtilities.rectTmp;
            rectF.set(0.0f, 0.0f, getWidth(), getHeight());
            this.flickerDrawable.draw(canvas, rectF, 0.0f, this);
            invalidate();
            return drawChild;
        } else if (view == this.webViewNotAvailableText) {
            canvas.save();
            canvas.translate(0.0f, (ActionBar.getCurrentActionBarHeight() - ((View) getParent()).getTranslationY()) / 2.0f);
            boolean drawChild2 = super.drawChild(canvas, view, j);
            canvas.restore();
            return drawChild2;
        } else {
            return super.drawChild(canvas, view, j);
        }
    }

    @Override // android.widget.FrameLayout, android.view.View
    protected void onMeasure(int i, int i2) {
        super.onMeasure(i, i2);
        this.flickerDrawable.setParentWidth(getMeasuredWidth());
    }

    public void setWebViewProgressListener(Consumer<Float> consumer) {
        this.webViewProgressListener = consumer;
    }

    public WebView getWebView() {
        return this.webView;
    }

    public void loadFlickerAndSettingsItem(int i, long j, ActionBarMenuSubItem actionBarMenuSubItem) {
        TLRPC$TL_attachMenuBot tLRPC$TL_attachMenuBot;
        boolean z;
        String str = MessagesController.getInstance(i).getUser(Long.valueOf(j)).username;
        int i2 = 0;
        if (str != null && ObjectsCompat$$ExternalSyntheticBackport0.m(str, "DurgerKingBot")) {
            this.flickerView.setVisibility(0);
            this.flickerView.setAlpha(1.0f);
            this.flickerView.setImageDrawable(SvgHelper.getDrawable(2131558443, getColor("windowBackgroundGray")));
            setupFlickerParams(false);
            return;
        }
        Iterator<TLRPC$TL_attachMenuBot> it = MediaDataController.getInstance(i).getAttachMenuBots().bots.iterator();
        while (true) {
            if (!it.hasNext()) {
                tLRPC$TL_attachMenuBot = null;
                break;
            }
            tLRPC$TL_attachMenuBot = it.next();
            if (tLRPC$TL_attachMenuBot.bot_id == j) {
                break;
            }
        }
        if (tLRPC$TL_attachMenuBot != null) {
            TLRPC$TL_attachMenuBotIcon placeholderStaticAttachMenuBotIcon = MediaDataController.getPlaceholderStaticAttachMenuBotIcon(tLRPC$TL_attachMenuBot);
            if (placeholderStaticAttachMenuBotIcon == null) {
                placeholderStaticAttachMenuBotIcon = MediaDataController.getStaticAttachMenuBotIcon(tLRPC$TL_attachMenuBot);
                z = true;
            } else {
                z = false;
            }
            if (placeholderStaticAttachMenuBotIcon != null) {
                this.flickerView.setVisibility(0);
                this.flickerView.setAlpha(1.0f);
                this.flickerView.setImage(ImageLocation.getForDocument(placeholderStaticAttachMenuBotIcon.icon), (String) null, (Drawable) null, tLRPC$TL_attachMenuBot);
                setupFlickerParams(z);
            }
            if (actionBarMenuSubItem == null) {
                return;
            }
            if (!tLRPC$TL_attachMenuBot.has_settings) {
                i2 = 8;
            }
            actionBarMenuSubItem.setVisibility(i2);
            return;
        }
        TLRPC$TL_messages_getAttachMenuBot tLRPC$TL_messages_getAttachMenuBot = new TLRPC$TL_messages_getAttachMenuBot();
        tLRPC$TL_messages_getAttachMenuBot.bot = MessagesController.getInstance(i).getInputUser(j);
        ConnectionsManager.getInstance(i).sendRequest(tLRPC$TL_messages_getAttachMenuBot, new BotWebViewContainer$$ExternalSyntheticLambda8(this, actionBarMenuSubItem));
    }

    public /* synthetic */ void lambda$loadFlickerAndSettingsItem$5(ActionBarMenuSubItem actionBarMenuSubItem, TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        AndroidUtilities.runOnUIThread(new BotWebViewContainer$$ExternalSyntheticLambda5(this, tLObject, actionBarMenuSubItem));
    }

    public /* synthetic */ void lambda$loadFlickerAndSettingsItem$4(TLObject tLObject, ActionBarMenuSubItem actionBarMenuSubItem) {
        boolean z;
        int i = 8;
        if (!(tLObject instanceof TLRPC$TL_attachMenuBotsBot)) {
            if (actionBarMenuSubItem == null) {
                return;
            }
            actionBarMenuSubItem.setVisibility(8);
            return;
        }
        TLRPC$TL_attachMenuBot tLRPC$TL_attachMenuBot = ((TLRPC$TL_attachMenuBotsBot) tLObject).bot;
        TLRPC$TL_attachMenuBotIcon placeholderStaticAttachMenuBotIcon = MediaDataController.getPlaceholderStaticAttachMenuBotIcon(tLRPC$TL_attachMenuBot);
        if (placeholderStaticAttachMenuBotIcon == null) {
            placeholderStaticAttachMenuBotIcon = MediaDataController.getStaticAttachMenuBotIcon(tLRPC$TL_attachMenuBot);
            z = true;
        } else {
            z = false;
        }
        if (placeholderStaticAttachMenuBotIcon != null) {
            this.flickerView.setVisibility(0);
            this.flickerView.setAlpha(1.0f);
            this.flickerView.setImage(ImageLocation.getForDocument(placeholderStaticAttachMenuBotIcon.icon), (String) null, (Drawable) null, tLRPC$TL_attachMenuBot);
            setupFlickerParams(z);
        }
        if (actionBarMenuSubItem == null) {
            return;
        }
        if (tLRPC$TL_attachMenuBot.has_settings) {
            i = 0;
        }
        actionBarMenuSubItem.setVisibility(i);
    }

    private void setupFlickerParams(boolean z) {
        this.isFlickeringCenter = z;
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) this.flickerView.getLayoutParams();
        layoutParams.gravity = z ? 17 : 48;
        if (z) {
            int dp = AndroidUtilities.dp(64.0f);
            layoutParams.height = dp;
            layoutParams.width = dp;
        } else {
            layoutParams.width = -1;
            layoutParams.height = -2;
        }
        this.flickerView.requestLayout();
    }

    public void reload() {
        checkCreateWebView();
        this.isPageLoaded = false;
        this.lastClickMs = 0L;
        this.hasUserPermissions = false;
        WebView webView = this.webView;
        if (webView != null) {
            webView.reload();
        }
    }

    public void loadUrl(int i, String str) {
        checkCreateWebView();
        this.currentAccount = i;
        this.isPageLoaded = false;
        this.lastClickMs = 0L;
        this.hasUserPermissions = false;
        this.mUrl = str;
        WebView webView = this.webView;
        if (webView != null) {
            webView.loadUrl(str);
        }
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        NotificationCenter.getGlobalInstance().addObserver(this, NotificationCenter.didSetNewTheme);
        NotificationCenter.getGlobalInstance().addObserver(this, NotificationCenter.onActivityResultReceived);
        NotificationCenter.getGlobalInstance().addObserver(this, NotificationCenter.onRequestPermissionResultReceived);
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        NotificationCenter.getGlobalInstance().removeObserver(this, NotificationCenter.didSetNewTheme);
        NotificationCenter.getGlobalInstance().removeObserver(this, NotificationCenter.onActivityResultReceived);
        NotificationCenter.getGlobalInstance().removeObserver(this, NotificationCenter.onRequestPermissionResultReceived);
    }

    public void destroyWebView() {
        WebView webView = this.webView;
        if (webView != null) {
            if (webView.getParent() != null) {
                removeView(this.webView);
            }
            this.webView.destroy();
        }
    }

    public boolean isBackButtonVisible() {
        return this.isBackButtonVisible;
    }

    public void evaluateJs(String str) {
        checkCreateWebView();
        WebView webView = this.webView;
        if (webView == null) {
            return;
        }
        if (Build.VERSION.SDK_INT >= 19) {
            webView.evaluateJavascript(str, BotWebViewContainer$$ExternalSyntheticLambda2.INSTANCE);
            return;
        }
        try {
            webView.loadUrl("javascript:" + URLEncoder.encode(str, "UTF-8"));
        } catch (UnsupportedEncodingException unused) {
            WebView webView2 = this.webView;
            webView2.loadUrl("javascript:" + URLEncoder.encode(str));
        }
    }

    @Override // org.telegram.messenger.NotificationCenter.NotificationCenterDelegate
    public void didReceivedNotification(int i, int i2, Object... objArr) {
        if (i == NotificationCenter.didSetNewTheme) {
            WebView webView = this.webView;
            if (webView != null) {
                webView.setBackgroundColor(getColor("windowBackgroundWhite"));
            }
            this.flickerView.setColorFilter(new PorterDuffColorFilter(getColor("dialogSearchHint"), PorterDuff.Mode.SRC_IN));
            notifyThemeChanged();
        } else if (i == NotificationCenter.onActivityResultReceived) {
            onActivityResult(((Integer) objArr[0]).intValue(), ((Integer) objArr[1]).intValue(), (Intent) objArr[2]);
        } else if (i != NotificationCenter.onRequestPermissionResultReceived) {
        } else {
            onRequestPermissionsResult(((Integer) objArr[0]).intValue(), (String[]) objArr[1], (int[]) objArr[2]);
        }
    }

    private void notifyThemeChanged() {
        notifyEvent("theme_changed", buildThemeParams());
    }

    private void notifyEvent(String str, JSONObject jSONObject) {
        evaluateJs("window.Telegram.WebView.receiveEvent('" + str + "', " + jSONObject + ");");
    }

    public void setWebViewScrollListener(WebViewScrollListener webViewScrollListener) {
        this.webViewScrollListener = webViewScrollListener;
    }

    public void setDelegate(Delegate delegate) {
        this.delegate = delegate;
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Code restructure failed: missing block: B:69:0x012b, code lost:
        if (r7 == 1) goto L71;
     */
    /* JADX WARN: Code restructure failed: missing block: B:71:0x012e, code lost:
        r12 = "windowBackgroundGray";
     */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:117:0x01f4  */
    /* JADX WARN: Removed duplicated region for block: B:139:0x0238  */
    /* JADX WARN: Removed duplicated region for block: B:144:0x0244 A[Catch: Exception -> 0x02c2, TryCatch #9 {Exception -> 0x02c2, blocks: (B:99:0x01b4, B:106:0x01d3, B:109:0x01dd, B:112:0x01e7, B:120:0x01fa, B:121:0x01fe, B:128:0x0218, B:131:0x0222, B:134:0x022b, B:142:0x023e, B:143:0x0241, B:144:0x0244, B:146:0x0248, B:147:0x0252, B:149:0x0256, B:152:0x0260, B:155:0x0269, B:158:0x0273, B:161:0x027d, B:171:0x0293, B:172:0x0296, B:173:0x0299, B:174:0x029c, B:175:0x029f, B:177:0x02a4, B:179:0x02aa, B:180:0x02b7), top: B:233:0x01b4 }] */
    /* JADX WARN: Removed duplicated region for block: B:146:0x0248 A[Catch: Exception -> 0x02c2, TryCatch #9 {Exception -> 0x02c2, blocks: (B:99:0x01b4, B:106:0x01d3, B:109:0x01dd, B:112:0x01e7, B:120:0x01fa, B:121:0x01fe, B:128:0x0218, B:131:0x0222, B:134:0x022b, B:142:0x023e, B:143:0x0241, B:144:0x0244, B:146:0x0248, B:147:0x0252, B:149:0x0256, B:152:0x0260, B:155:0x0269, B:158:0x0273, B:161:0x027d, B:171:0x0293, B:172:0x0296, B:173:0x0299, B:174:0x029c, B:175:0x029f, B:177:0x02a4, B:179:0x02aa, B:180:0x02b7), top: B:233:0x01b4 }] */
    /* JADX WARN: Removed duplicated region for block: B:177:0x02a4 A[Catch: Exception -> 0x02c2, TryCatch #9 {Exception -> 0x02c2, blocks: (B:99:0x01b4, B:106:0x01d3, B:109:0x01dd, B:112:0x01e7, B:120:0x01fa, B:121:0x01fe, B:128:0x0218, B:131:0x0222, B:134:0x022b, B:142:0x023e, B:143:0x0241, B:144:0x0244, B:146:0x0248, B:147:0x0252, B:149:0x0256, B:152:0x0260, B:155:0x0269, B:158:0x0273, B:161:0x027d, B:171:0x0293, B:172:0x0296, B:173:0x0299, B:174:0x029c, B:175:0x029f, B:177:0x02a4, B:179:0x02aa, B:180:0x02b7), top: B:233:0x01b4 }] */
    /* JADX WARN: Removed duplicated region for block: B:243:? A[RETURN, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void onEventReceived(String str, String str2) {
        char c;
        char c2;
        BotWebViewVibrationEffect botWebViewVibrationEffect;
        if (this.webView == null || this.delegate == null) {
            return;
        }
        str.hashCode();
        char c3 = 65535;
        char c4 = 0;
        switch (str.hashCode()) {
            case -1717314938:
                if (str.equals("web_app_open_link")) {
                    c = 0;
                    break;
                }
                c = 65535;
                break;
            case -1390641887:
                if (str.equals("web_app_open_invoice")) {
                    c = 1;
                    break;
                }
                c = 65535;
                break;
            case -1259935152:
                if (str.equals("web_app_request_theme")) {
                    c = 2;
                    break;
                }
                c = 65535;
                break;
            case -921083201:
                if (str.equals("web_app_request_viewport")) {
                    c = 3;
                    break;
                }
                c = 65535;
                break;
            case -439770054:
                if (str.equals("web_app_open_tg_link")) {
                    c = 4;
                    break;
                }
                c = 65535;
                break;
            case -71726289:
                if (str.equals("web_app_close")) {
                    c = 5;
                    break;
                }
                c = 65535;
                break;
            case -58095910:
                if (str.equals("web_app_ready")) {
                    c = 6;
                    break;
                }
                c = 65535;
                break;
            case 668142772:
                if (str.equals("web_app_data_send")) {
                    c = 7;
                    break;
                }
                c = 65535;
                break;
            case 1011447167:
                if (str.equals("web_app_setup_back_button")) {
                    c = '\b';
                    break;
                }
                c = 65535;
                break;
            case 1273834781:
                if (str.equals("web_app_trigger_haptic_feedback")) {
                    c = '\t';
                    break;
                }
                c = 65535;
                break;
            case 1398490221:
                if (str.equals("web_app_setup_main_button")) {
                    c = '\n';
                    break;
                }
                c = 65535;
                break;
            case 1917103703:
                if (str.equals("web_app_set_header_color")) {
                    c = 11;
                    break;
                }
                c = 65535;
                break;
            case 2001330488:
                if (str.equals("web_app_set_background_color")) {
                    c = '\f';
                    break;
                }
                c = 65535;
                break;
            case 2139805763:
                if (str.equals("web_app_expand")) {
                    c = '\r';
                    break;
                }
                c = 65535;
                break;
            default:
                c = 65535;
                break;
        }
        BotWebViewVibrationEffect botWebViewVibrationEffect2 = null;
        String str3 = null;
        botWebViewVibrationEffect2 = null;
        botWebViewVibrationEffect2 = null;
        switch (c) {
            case 0:
                try {
                    Uri parse = Uri.parse(new JSONObject(str2).optString("url"));
                    if (!WHITELISTED_SCHEMES.contains(parse.getScheme())) {
                        return;
                    }
                    onOpenUri(parse, true);
                    return;
                } catch (Exception e) {
                    FileLog.e(e);
                    return;
                }
            case 1:
                try {
                    String optString = new JSONObject(str2).optString("slug");
                    if (this.currentPaymentSlug != null) {
                        onInvoiceStatusUpdate(optString, "cancelled", true);
                    } else {
                        this.currentPaymentSlug = optString;
                        TLRPC$TL_payments_getPaymentForm tLRPC$TL_payments_getPaymentForm = new TLRPC$TL_payments_getPaymentForm();
                        TLRPC$TL_inputInvoiceSlug tLRPC$TL_inputInvoiceSlug = new TLRPC$TL_inputInvoiceSlug();
                        tLRPC$TL_inputInvoiceSlug.slug = optString;
                        tLRPC$TL_payments_getPaymentForm.invoice = tLRPC$TL_inputInvoiceSlug;
                        ConnectionsManager.getInstance(this.currentAccount).sendRequest(tLRPC$TL_payments_getPaymentForm, new BotWebViewContainer$$ExternalSyntheticLambda7(this, optString));
                    }
                    return;
                } catch (JSONException e2) {
                    FileLog.e(e2);
                    return;
                }
            case 2:
                notifyThemeChanged();
                return;
            case 3:
                if ((getParent() instanceof ChatAttachAlertBotWebViewLayout.WebViewSwipeContainer) && ((ChatAttachAlertBotWebViewLayout.WebViewSwipeContainer) getParent()).isSwipeInProgress()) {
                    c4 = 1;
                }
                invalidateViewPortHeight(c4 ^ 1, true);
                return;
            case 4:
                try {
                    String optString2 = new JSONObject(str2).optString("path_full");
                    if (optString2.startsWith("/")) {
                        optString2 = optString2.substring(1);
                    }
                    onOpenUri(Uri.parse("https://t.me/" + optString2));
                    return;
                } catch (JSONException e3) {
                    FileLog.e(e3);
                    return;
                }
            case 5:
                this.delegate.onCloseRequested(null);
                return;
            case 6:
                setPageLoaded(this.webView.getUrl());
                return;
            case 7:
                try {
                    this.delegate.onSendWebViewData(new JSONObject(str2).optString("data"));
                    return;
                } catch (JSONException e4) {
                    FileLog.e(e4);
                    return;
                }
            case '\b':
                try {
                    boolean optBoolean = new JSONObject(str2).optBoolean("is_visible");
                    if (optBoolean == this.isBackButtonVisible) {
                        return;
                    }
                    this.isBackButtonVisible = optBoolean;
                    this.delegate.onSetBackButtonVisible(optBoolean);
                    return;
                } catch (JSONException e5) {
                    FileLog.e(e5);
                    return;
                }
            case '\t':
                try {
                    JSONObject jSONObject = new JSONObject(str2);
                    String optString3 = jSONObject.optString("type");
                    int hashCode = optString3.hashCode();
                    if (hashCode == -1184809658) {
                        if (optString3.equals("impact")) {
                            c2 = 0;
                            if (c2 == 0) {
                            }
                        }
                        c2 = 65535;
                        if (c2 == 0) {
                        }
                    } else if (hashCode != 193071555) {
                        if (hashCode == 595233003 && optString3.equals("notification")) {
                            c2 = 1;
                            if (c2 == 0) {
                                if (c2 == 1) {
                                    String optString4 = jSONObject.optString("notification_type");
                                    int hashCode2 = optString4.hashCode();
                                    if (hashCode2 == -1867169789) {
                                        if (optString4.equals("success")) {
                                            c4 = 1;
                                            if (c4 != 0) {
                                            }
                                            botWebViewVibrationEffect2 = botWebViewVibrationEffect;
                                        }
                                        c4 = 65535;
                                        if (c4 != 0) {
                                        }
                                        botWebViewVibrationEffect2 = botWebViewVibrationEffect;
                                    } else if (hashCode2 != 96784904) {
                                        if (hashCode2 == 1124446108 && optString4.equals("warning")) {
                                            c4 = 2;
                                            if (c4 != 0) {
                                                botWebViewVibrationEffect = BotWebViewVibrationEffect.NOTIFICATION_ERROR;
                                            } else if (c4 == 1) {
                                                botWebViewVibrationEffect = BotWebViewVibrationEffect.NOTIFICATION_SUCCESS;
                                            } else if (c4 == 2) {
                                                botWebViewVibrationEffect = BotWebViewVibrationEffect.NOTIFICATION_WARNING;
                                            }
                                            botWebViewVibrationEffect2 = botWebViewVibrationEffect;
                                        }
                                        c4 = 65535;
                                        if (c4 != 0) {
                                        }
                                        botWebViewVibrationEffect2 = botWebViewVibrationEffect;
                                    } else {
                                        if (optString4.equals("error")) {
                                            if (c4 != 0) {
                                            }
                                            botWebViewVibrationEffect2 = botWebViewVibrationEffect;
                                        }
                                        c4 = 65535;
                                        if (c4 != 0) {
                                        }
                                        botWebViewVibrationEffect2 = botWebViewVibrationEffect;
                                    }
                                } else if (c2 == 2) {
                                    botWebViewVibrationEffect2 = BotWebViewVibrationEffect.SELECTION_CHANGE;
                                }
                                if (botWebViewVibrationEffect2 == null) {
                                    return;
                                }
                                if (Build.VERSION.SDK_INT >= 26) {
                                    AndroidUtilities.getVibrator().vibrate(botWebViewVibrationEffect2.getVibrationEffectForOreo());
                                    return;
                                } else {
                                    AndroidUtilities.getVibrator().vibrate(botWebViewVibrationEffect2.fallbackTimings, -1);
                                    return;
                                }
                            }
                            String optString5 = jSONObject.optString("impact_style");
                            switch (optString5.hashCode()) {
                                case -1078030475:
                                    if (optString5.equals("medium")) {
                                        c4 = 1;
                                        break;
                                    }
                                    c4 = 65535;
                                    break;
                                case 3535914:
                                    if (optString5.equals("soft")) {
                                        c4 = 4;
                                        break;
                                    }
                                    c4 = 65535;
                                    break;
                                case 99152071:
                                    if (optString5.equals("heavy")) {
                                        c4 = 2;
                                        break;
                                    }
                                    c4 = 65535;
                                    break;
                                case 102970646:
                                    if (optString5.equals("light")) {
                                        break;
                                    }
                                    c4 = 65535;
                                    break;
                                case 108511787:
                                    if (optString5.equals("rigid")) {
                                        c4 = 3;
                                        break;
                                    }
                                    c4 = 65535;
                                    break;
                                default:
                                    c4 = 65535;
                                    break;
                            }
                            if (c4 == 0) {
                                botWebViewVibrationEffect = BotWebViewVibrationEffect.IMPACT_LIGHT;
                            } else if (c4 == 1) {
                                botWebViewVibrationEffect = BotWebViewVibrationEffect.IMPACT_MEDIUM;
                            } else if (c4 == 2) {
                                botWebViewVibrationEffect = BotWebViewVibrationEffect.IMPACT_HEAVY;
                            } else if (c4 == 3) {
                                botWebViewVibrationEffect = BotWebViewVibrationEffect.IMPACT_RIGID;
                            } else if (c4 == 4) {
                                botWebViewVibrationEffect = BotWebViewVibrationEffect.IMPACT_SOFT;
                            } else if (botWebViewVibrationEffect2 == null) {
                            }
                            botWebViewVibrationEffect2 = botWebViewVibrationEffect;
                            if (botWebViewVibrationEffect2 == null) {
                            }
                        }
                        c2 = 65535;
                        if (c2 == 0) {
                        }
                    } else {
                        if (optString3.equals("selection_change")) {
                            c2 = 2;
                            if (c2 == 0) {
                            }
                        }
                        c2 = 65535;
                        if (c2 == 0) {
                        }
                    }
                } catch (Exception e6) {
                    FileLog.e(e6);
                    return;
                }
                break;
            case '\n':
                try {
                    JSONObject jSONObject2 = new JSONObject(str2);
                    boolean optBoolean2 = jSONObject2.optBoolean("is_active", false);
                    String trim = jSONObject2.optString("text", this.lastButtonText).trim();
                    boolean z = jSONObject2.optBoolean("is_visible", false) && !TextUtils.isEmpty(trim);
                    int parseColor = jSONObject2.has("color") ? Color.parseColor(jSONObject2.optString("color")) : this.lastButtonColor;
                    int parseColor2 = jSONObject2.has("text_color") ? Color.parseColor(jSONObject2.optString("text_color")) : this.lastButtonTextColor;
                    boolean z2 = jSONObject2.optBoolean("is_progress_visible", false) && z;
                    this.lastButtonColor = parseColor;
                    this.lastButtonTextColor = parseColor2;
                    this.lastButtonText = trim;
                    this.buttonData = str2;
                    this.delegate.onSetupMainButton(z, optBoolean2, trim, parseColor, parseColor2, z2);
                    return;
                } catch (IllegalArgumentException | JSONException e7) {
                    FileLog.e(e7);
                    return;
                }
            case 11:
                try {
                    String string = new JSONObject(str2).getString("color_key");
                    int hashCode3 = string.hashCode();
                    if (hashCode3 == -1265068311) {
                        if (string.equals("bg_color")) {
                            c3 = 0;
                            break;
                        }
                    } else if (hashCode3 == -210781868 && string.equals("secondary_bg_color")) {
                        c3 = 1;
                        break;
                    }
                    str3 = "windowBackgroundWhite";
                    if (str3 == null) {
                        return;
                    }
                    this.delegate.onWebAppSetActionBarColor(str3);
                    return;
                } catch (JSONException e8) {
                    FileLog.e(e8);
                    return;
                }
            case '\f':
                try {
                    this.delegate.onWebAppSetBackgroundColor(Color.parseColor(new JSONObject(str2).optString("color")) | (-16777216));
                    return;
                } catch (JSONException e9) {
                    FileLog.e(e9);
                    return;
                }
            case '\r':
                this.delegate.onWebAppExpand();
                return;
            default:
                return;
        }
    }

    public /* synthetic */ void lambda$onEventReceived$8(String str, TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        AndroidUtilities.runOnUIThread(new BotWebViewContainer$$ExternalSyntheticLambda6(this, tLRPC$TL_error, str, tLObject));
    }

    public /* synthetic */ void lambda$onEventReceived$7(TLRPC$TL_error tLRPC$TL_error, String str, TLObject tLObject) {
        if (tLRPC$TL_error != null) {
            onInvoiceStatusUpdate(str, "failed");
        } else {
            this.delegate.onWebAppOpenInvoice(str, tLObject);
        }
    }

    private JSONObject buildThemeParams() {
        try {
            JSONObject jSONObject = new JSONObject();
            jSONObject.put("bg_color", formatColor("windowBackgroundWhite"));
            jSONObject.put("secondary_bg_color", formatColor("windowBackgroundGray"));
            jSONObject.put("text_color", formatColor("windowBackgroundWhiteBlackText"));
            jSONObject.put("hint_color", formatColor("windowBackgroundWhiteHintText"));
            jSONObject.put("link_color", formatColor("windowBackgroundWhiteLinkText"));
            jSONObject.put("button_color", formatColor("featuredStickers_addButton"));
            jSONObject.put("button_text_color", formatColor("featuredStickers_buttonText"));
            return new JSONObject().put("theme_params", jSONObject);
        } catch (Exception e) {
            FileLog.e(e);
            return new JSONObject();
        }
    }

    private int getColor(String str) {
        Theme.ResourcesProvider resourcesProvider = this.resourcesProvider;
        Integer valueOf = Integer.valueOf(resourcesProvider != null ? resourcesProvider.getColor(str).intValue() : Theme.getColor(str));
        if (valueOf == null) {
            valueOf = Integer.valueOf(Theme.getColor(str));
        }
        return valueOf.intValue();
    }

    private String formatColor(String str) {
        int color = getColor(str);
        return "#" + hexFixed(Color.red(color)) + hexFixed(Color.green(color)) + hexFixed(Color.blue(color));
    }

    private String hexFixed(int i) {
        String hexString = Integer.toHexString(i);
        if (hexString.length() < 2) {
            return "0" + hexString;
        }
        return hexString;
    }

    /* loaded from: classes3.dex */
    public class WebViewProxy {
        private WebViewProxy() {
            BotWebViewContainer.this = r1;
        }

        /* synthetic */ WebViewProxy(BotWebViewContainer botWebViewContainer, AnonymousClass1 anonymousClass1) {
            this();
        }

        public /* synthetic */ void lambda$postEvent$0(String str, String str2) {
            BotWebViewContainer.this.onEventReceived(str, str2);
        }

        @JavascriptInterface
        public void postEvent(String str, String str2) {
            AndroidUtilities.runOnUIThread(new BotWebViewContainer$WebViewProxy$$ExternalSyntheticLambda0(this, str, str2));
        }
    }
}