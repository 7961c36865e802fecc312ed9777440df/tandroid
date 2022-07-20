package org.telegram.ui.Components;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Build;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.webkit.WebView;
import android.widget.FrameLayout;
import androidx.core.math.MathUtils;
import androidx.core.view.GestureDetectorCompat;
import androidx.dynamicanimation.animation.DynamicAnimation;
import androidx.dynamicanimation.animation.FloatValueHolder;
import androidx.dynamicanimation.animation.SpringAnimation;
import androidx.dynamicanimation.animation.SpringForce;
import androidx.recyclerview.widget.ChatListItemAnimator;
import java.util.Iterator;
import org.json.JSONObject;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.GenericProvider;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MediaDataController;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.UserObject;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC$ChatFull;
import org.telegram.tgnet.TLRPC$Peer;
import org.telegram.tgnet.TLRPC$TL_attachMenuBot;
import org.telegram.tgnet.TLRPC$TL_dataJSON;
import org.telegram.tgnet.TLRPC$TL_error;
import org.telegram.tgnet.TLRPC$TL_messages_prolongWebView;
import org.telegram.tgnet.TLRPC$TL_messages_requestWebView;
import org.telegram.tgnet.TLRPC$TL_webViewResultUrl;
import org.telegram.ui.ActionBar.ActionBar;
import org.telegram.ui.ActionBar.ActionBarMenu;
import org.telegram.ui.ActionBar.ActionBarMenuItem;
import org.telegram.ui.ActionBar.ActionBarMenuSubItem;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.ChatActivity;
import org.telegram.ui.Components.BotWebViewContainer;
import org.telegram.ui.Components.ChatAttachAlert;
/* loaded from: classes3.dex */
public class ChatAttachAlertBotWebViewLayout extends ChatAttachAlert.AttachAlertLayout implements NotificationCenter.NotificationCenterDelegate {
    private long botId;
    private int currentAccount;
    private int customBackground;
    private boolean destroyed;
    private boolean hasCustomBackground;
    private boolean ignoreLayout;
    private boolean ignoreMeasure;
    private boolean isBotButtonAvailable;
    private int measureOffsetY;
    private boolean needReload;
    private ActionBarMenuItem otherItem;
    private long peerId;
    private Runnable pollRunnable = new ChatAttachAlertBotWebViewLayout$$ExternalSyntheticLambda4(this);
    private WebProgressView progressView;
    private long queryId;
    private int replyToMsgId;
    private ActionBarMenuSubItem settingsItem;
    private boolean silent;
    private String startCommand;
    private WebViewSwipeContainer swipeContainer;
    private BotWebViewContainer webViewContainer;
    private ValueAnimator webViewScrollAnimator;

    @Override // org.telegram.ui.Components.ChatAttachAlert.AttachAlertLayout
    int needsActionBar() {
        return 1;
    }

    @Override // org.telegram.ui.Components.ChatAttachAlert.AttachAlertLayout
    boolean shouldHideBottomButtons() {
        return false;
    }

    public /* synthetic */ void lambda$new$2() {
        TLRPC$ChatFull chatFull;
        TLRPC$Peer tLRPC$Peer;
        if (!this.destroyed) {
            TLRPC$TL_messages_prolongWebView tLRPC$TL_messages_prolongWebView = new TLRPC$TL_messages_prolongWebView();
            tLRPC$TL_messages_prolongWebView.bot = MessagesController.getInstance(this.currentAccount).getInputUser(this.botId);
            tLRPC$TL_messages_prolongWebView.peer = MessagesController.getInstance(this.currentAccount).getInputPeer(this.peerId);
            tLRPC$TL_messages_prolongWebView.query_id = this.queryId;
            tLRPC$TL_messages_prolongWebView.silent = this.silent;
            int i = this.replyToMsgId;
            if (i != 0) {
                tLRPC$TL_messages_prolongWebView.reply_to_msg_id = i;
                tLRPC$TL_messages_prolongWebView.flags |= 1;
            }
            if (this.peerId < 0 && (chatFull = MessagesController.getInstance(this.currentAccount).getChatFull(-this.peerId)) != null && (tLRPC$Peer = chatFull.default_send_as) != null) {
                tLRPC$TL_messages_prolongWebView.send_as = MessagesController.getInstance(this.currentAccount).getInputPeer(tLRPC$Peer);
                tLRPC$TL_messages_prolongWebView.flags |= 8192;
            }
            ConnectionsManager.getInstance(this.currentAccount).sendRequest(tLRPC$TL_messages_prolongWebView, new ChatAttachAlertBotWebViewLayout$$ExternalSyntheticLambda11(this));
        }
    }

    public /* synthetic */ void lambda$new$1(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        AndroidUtilities.runOnUIThread(new ChatAttachAlertBotWebViewLayout$$ExternalSyntheticLambda9(this, tLRPC$TL_error));
    }

    public /* synthetic */ void lambda$new$0(TLRPC$TL_error tLRPC$TL_error) {
        if (this.destroyed) {
            return;
        }
        if (tLRPC$TL_error != null) {
            this.parentAlert.dismiss();
        } else {
            AndroidUtilities.runOnUIThread(this.pollRunnable, 60000L);
        }
    }

    public ChatAttachAlertBotWebViewLayout(ChatAttachAlert chatAttachAlert, Context context, Theme.ResourcesProvider resourcesProvider) {
        super(chatAttachAlert, context, resourcesProvider);
        ActionBarMenuItem addItem = this.parentAlert.actionBar.createMenu().addItem(0, 2131165453);
        this.otherItem = addItem;
        addItem.addSubItem(2131230861, 2131165657, LocaleController.getString(2131624743));
        this.settingsItem = this.otherItem.addSubItem(2131230864, 2131165931, LocaleController.getString(2131624753));
        this.otherItem.addSubItem(2131230863, 2131165907, LocaleController.getString(2131624744));
        this.otherItem.addSubItem(2131230856, 2131165702, LocaleController.getString(2131624740));
        this.parentAlert.actionBar.setActionBarMenuOnItemClick(new AnonymousClass1());
        this.webViewContainer = new AnonymousClass2(context, resourcesProvider, getThemedColor("dialogBackground"));
        AnonymousClass3 anonymousClass3 = new AnonymousClass3(context);
        this.swipeContainer = anonymousClass3;
        anonymousClass3.addView(this.webViewContainer, LayoutHelper.createFrame(-1, -1.0f));
        this.swipeContainer.setScrollListener(new ChatAttachAlertBotWebViewLayout$$ExternalSyntheticLambda6(this));
        this.swipeContainer.setScrollEndListener(new ChatAttachAlertBotWebViewLayout$$ExternalSyntheticLambda7(this));
        this.swipeContainer.setDelegate(new ChatAttachAlertBotWebViewLayout$$ExternalSyntheticLambda13(this));
        this.swipeContainer.setIsKeyboardVisible(new ChatAttachAlertBotWebViewLayout$$ExternalSyntheticLambda10(this));
        addView(this.swipeContainer, LayoutHelper.createFrame(-1, -1.0f));
        WebProgressView webProgressView = new WebProgressView(context, resourcesProvider);
        this.progressView = webProgressView;
        addView(webProgressView, LayoutHelper.createFrame(-1, -2.0f, 80, 0.0f, 0.0f, 0.0f, 84.0f));
        this.webViewContainer.setWebViewProgressListener(new ChatAttachAlertBotWebViewLayout$$ExternalSyntheticLambda2(this));
        NotificationCenter.getGlobalInstance().addObserver(this, NotificationCenter.didSetNewTheme);
    }

    /* renamed from: org.telegram.ui.Components.ChatAttachAlertBotWebViewLayout$1 */
    /* loaded from: classes3.dex */
    public class AnonymousClass1 extends ActionBar.ActionBarMenuOnItemClick {
        AnonymousClass1() {
            ChatAttachAlertBotWebViewLayout.this = r1;
        }

        @Override // org.telegram.ui.ActionBar.ActionBar.ActionBarMenuOnItemClick
        public void onItemClick(int i) {
            if (i == -1) {
                if (ChatAttachAlertBotWebViewLayout.this.onBackPressed()) {
                    return;
                }
                ChatAttachAlertBotWebViewLayout.this.parentAlert.dismiss();
            } else if (i == 2131230861) {
                Bundle bundle = new Bundle();
                bundle.putLong("user_id", ChatAttachAlertBotWebViewLayout.this.botId);
                ChatAttachAlertBotWebViewLayout.this.parentAlert.baseFragment.presentFragment(new ChatActivity(bundle));
                ChatAttachAlertBotWebViewLayout.this.parentAlert.dismiss();
            } else if (i == 2131230863) {
                if (ChatAttachAlertBotWebViewLayout.this.webViewContainer.getWebView() != null) {
                    ChatAttachAlertBotWebViewLayout.this.webViewContainer.getWebView().animate().cancel();
                    ChatAttachAlertBotWebViewLayout.this.webViewContainer.getWebView().animate().alpha(0.0f).start();
                }
                ChatAttachAlertBotWebViewLayout.this.progressView.setLoadProgress(0.0f);
                ChatAttachAlertBotWebViewLayout.this.progressView.setAlpha(1.0f);
                ChatAttachAlertBotWebViewLayout.this.progressView.setVisibility(0);
                ChatAttachAlertBotWebViewLayout.this.webViewContainer.setBotUser(MessagesController.getInstance(ChatAttachAlertBotWebViewLayout.this.currentAccount).getUser(Long.valueOf(ChatAttachAlertBotWebViewLayout.this.botId)));
                ChatAttachAlertBotWebViewLayout.this.webViewContainer.loadFlickerAndSettingsItem(ChatAttachAlertBotWebViewLayout.this.currentAccount, ChatAttachAlertBotWebViewLayout.this.botId, ChatAttachAlertBotWebViewLayout.this.settingsItem);
                ChatAttachAlertBotWebViewLayout.this.webViewContainer.reload();
            } else if (i != 2131230856) {
                if (i != 2131230864) {
                    return;
                }
                ChatAttachAlertBotWebViewLayout.this.webViewContainer.onSettingsButtonPressed();
            } else {
                Iterator<TLRPC$TL_attachMenuBot> it = MediaDataController.getInstance(ChatAttachAlertBotWebViewLayout.this.currentAccount).getAttachMenuBots().bots.iterator();
                while (it.hasNext()) {
                    TLRPC$TL_attachMenuBot next = it.next();
                    if (next.bot_id == ChatAttachAlertBotWebViewLayout.this.botId) {
                        ChatAttachAlertBotWebViewLayout chatAttachAlertBotWebViewLayout = ChatAttachAlertBotWebViewLayout.this;
                        chatAttachAlertBotWebViewLayout.parentAlert.onLongClickBotButton(next, MessagesController.getInstance(chatAttachAlertBotWebViewLayout.currentAccount).getUser(Long.valueOf(ChatAttachAlertBotWebViewLayout.this.botId)));
                        return;
                    }
                }
            }
        }
    }

    /* renamed from: org.telegram.ui.Components.ChatAttachAlertBotWebViewLayout$2 */
    /* loaded from: classes3.dex */
    public class AnonymousClass2 extends BotWebViewContainer {
        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass2(Context context, Theme.ResourcesProvider resourcesProvider, int i) {
            super(context, resourcesProvider, i);
            ChatAttachAlertBotWebViewLayout.this = r1;
        }

        @Override // android.view.ViewGroup, android.view.View
        public boolean dispatchTouchEvent(MotionEvent motionEvent) {
            if (motionEvent.getAction() == 0 && !ChatAttachAlertBotWebViewLayout.this.isBotButtonAvailable) {
                ChatAttachAlertBotWebViewLayout.this.isBotButtonAvailable = true;
                ChatAttachAlertBotWebViewLayout.this.webViewContainer.restoreButtonData();
            }
            return super.dispatchTouchEvent(motionEvent);
        }
    }

    /* renamed from: org.telegram.ui.Components.ChatAttachAlertBotWebViewLayout$3 */
    /* loaded from: classes3.dex */
    public class AnonymousClass3 extends WebViewSwipeContainer {
        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass3(Context context) {
            super(context);
            ChatAttachAlertBotWebViewLayout.this = r1;
        }

        @Override // android.widget.FrameLayout, android.view.View
        protected void onMeasure(int i, int i2) {
            super.onMeasure(i, View.MeasureSpec.makeMeasureSpec(((View.MeasureSpec.getSize(i2) - ActionBar.getCurrentActionBarHeight()) - AndroidUtilities.dp(84.0f)) + ChatAttachAlertBotWebViewLayout.this.measureOffsetY, 1073741824));
        }
    }

    public /* synthetic */ void lambda$new$3() {
        this.parentAlert.updateLayout(this, true, 0);
        this.webViewContainer.invalidateViewPortHeight();
        System.currentTimeMillis();
    }

    public /* synthetic */ void lambda$new$4() {
        this.webViewContainer.invalidateViewPortHeight(true);
    }

    public /* synthetic */ void lambda$new$5() {
        this.parentAlert.dismiss();
    }

    public /* synthetic */ Boolean lambda$new$6(Void r2) {
        return Boolean.valueOf(this.parentAlert.sizeNotifierFrameLayout.getKeyboardHeight() >= AndroidUtilities.dp(20.0f));
    }

    public /* synthetic */ void lambda$new$8(Float f) {
        this.progressView.setLoadProgressAnimated(f.floatValue());
        if (f.floatValue() == 1.0f) {
            ValueAnimator duration = ValueAnimator.ofFloat(1.0f, 0.0f).setDuration(200L);
            duration.setInterpolator(CubicBezierInterpolator.DEFAULT);
            duration.addUpdateListener(new ChatAttachAlertBotWebViewLayout$$ExternalSyntheticLambda1(this));
            duration.addListener(new AnonymousClass4());
            duration.start();
            requestEnableKeyboard();
        }
    }

    /* renamed from: org.telegram.ui.Components.ChatAttachAlertBotWebViewLayout$4 */
    /* loaded from: classes3.dex */
    public class AnonymousClass4 extends AnimatorListenerAdapter {
        AnonymousClass4() {
            ChatAttachAlertBotWebViewLayout.this = r1;
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator) {
            ChatAttachAlertBotWebViewLayout.this.progressView.setVisibility(8);
        }
    }

    public /* synthetic */ void lambda$new$7(ValueAnimator valueAnimator) {
        this.progressView.setAlpha(((Float) valueAnimator.getAnimatedValue()).floatValue());
    }

    public void setCustomBackground(int i) {
        this.customBackground = i;
        this.hasCustomBackground = true;
    }

    @Override // org.telegram.ui.Components.ChatAttachAlert.AttachAlertLayout
    boolean hasCustomBackground() {
        return this.hasCustomBackground;
    }

    @Override // org.telegram.ui.Components.ChatAttachAlert.AttachAlertLayout
    public int getCustomBackground() {
        return this.customBackground;
    }

    public boolean canExpandByRequest() {
        return !this.swipeContainer.isSwipeInProgress();
    }

    public void setMeasureOffsetY(int i) {
        this.measureOffsetY = i;
        this.swipeContainer.requestLayout();
    }

    public void disallowSwipeOffsetAnimation() {
        this.swipeContainer.setSwipeOffsetAnimationDisallowed(true);
    }

    @Override // android.widget.FrameLayout, android.view.View
    protected void onMeasure(int i, int i2) {
        if (this.ignoreMeasure) {
            setMeasuredDimension(getMeasuredWidth(), getMeasuredHeight());
        } else {
            super.onMeasure(i, i2);
        }
    }

    @Override // org.telegram.ui.Components.ChatAttachAlert.AttachAlertLayout
    public void onPanTransitionStart(boolean z, int i) {
        boolean z2;
        if (!z) {
            return;
        }
        this.webViewContainer.setViewPortByMeasureSuppressed(true);
        float topActionBarOffsetY = (-this.swipeContainer.getOffsetY()) + this.swipeContainer.getTopActionBarOffsetY();
        if (this.swipeContainer.getSwipeOffsetY() != topActionBarOffsetY) {
            this.swipeContainer.stickTo(topActionBarOffsetY);
            z2 = true;
        } else {
            z2 = false;
        }
        int measureKeyboardHeight = this.parentAlert.sizeNotifierFrameLayout.measureKeyboardHeight() + i;
        setMeasuredDimension(getMeasuredWidth(), i);
        this.ignoreMeasure = true;
        this.swipeContainer.setSwipeOffsetAnimationDisallowed(true);
        if (z2) {
            return;
        }
        ValueAnimator valueAnimator = this.webViewScrollAnimator;
        if (valueAnimator != null) {
            valueAnimator.cancel();
            this.webViewScrollAnimator = null;
        }
        if (this.webViewContainer.getWebView() == null) {
            return;
        }
        int scrollY = this.webViewContainer.getWebView().getScrollY();
        int i2 = (measureKeyboardHeight - i) + scrollY;
        ValueAnimator duration = ValueAnimator.ofInt(scrollY, i2).setDuration(250L);
        this.webViewScrollAnimator = duration;
        duration.setInterpolator(ChatListItemAnimator.DEFAULT_INTERPOLATOR);
        this.webViewScrollAnimator.addUpdateListener(new ChatAttachAlertBotWebViewLayout$$ExternalSyntheticLambda0(this));
        this.webViewScrollAnimator.addListener(new AnonymousClass5(i2));
        this.webViewScrollAnimator.start();
    }

    public /* synthetic */ void lambda$onPanTransitionStart$9(ValueAnimator valueAnimator) {
        int intValue = ((Integer) valueAnimator.getAnimatedValue()).intValue();
        if (this.webViewContainer.getWebView() != null) {
            this.webViewContainer.getWebView().setScrollY(intValue);
        }
    }

    /* renamed from: org.telegram.ui.Components.ChatAttachAlertBotWebViewLayout$5 */
    /* loaded from: classes3.dex */
    class AnonymousClass5 extends AnimatorListenerAdapter {
        final /* synthetic */ int val$toY;

        AnonymousClass5(int i) {
            ChatAttachAlertBotWebViewLayout.this = r1;
            this.val$toY = i;
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator) {
            if (ChatAttachAlertBotWebViewLayout.this.webViewContainer.getWebView() != null) {
                ChatAttachAlertBotWebViewLayout.this.webViewContainer.getWebView().setScrollY(this.val$toY);
            }
            if (animator == ChatAttachAlertBotWebViewLayout.this.webViewScrollAnimator) {
                ChatAttachAlertBotWebViewLayout.this.webViewScrollAnimator = null;
            }
        }
    }

    @Override // org.telegram.ui.Components.ChatAttachAlert.AttachAlertLayout
    public void onPanTransitionEnd() {
        this.ignoreMeasure = false;
        this.swipeContainer.setSwipeOffsetAnimationDisallowed(false);
        this.webViewContainer.setViewPortByMeasureSuppressed(false);
        requestLayout();
    }

    @Override // org.telegram.ui.Components.ChatAttachAlert.AttachAlertLayout
    void onShow(ChatAttachAlert.AttachAlertLayout attachAlertLayout) {
        this.parentAlert.actionBar.setTitle(UserObject.getUserName(MessagesController.getInstance(this.currentAccount).getUser(Long.valueOf(this.botId))));
        this.swipeContainer.setSwipeOffsetY(0.0f);
        if (this.webViewContainer.getWebView() != null) {
            this.webViewContainer.getWebView().scrollTo(0, 0);
        }
        if (this.parentAlert.getBaseFragment() != null) {
            this.webViewContainer.setParentActivity(this.parentAlert.getBaseFragment().getParentActivity());
        }
        this.otherItem.setVisibility(0);
        if (!this.webViewContainer.isBackButtonVisible()) {
            AndroidUtilities.updateImageViewImageAnimated(this.parentAlert.actionBar.getBackButton(), 2131165473);
        }
    }

    @Override // org.telegram.ui.Components.ChatAttachAlert.AttachAlertLayout
    void onShown() {
        if (this.webViewContainer.isPageLoaded()) {
            requestEnableKeyboard();
        }
        this.swipeContainer.setSwipeOffsetAnimationDisallowed(false);
        AndroidUtilities.runOnUIThread(new ChatAttachAlertBotWebViewLayout$$ExternalSyntheticLambda5(this));
    }

    public /* synthetic */ void lambda$onShown$10() {
        this.webViewContainer.restoreButtonData();
    }

    public void requestEnableKeyboard() {
        BaseFragment baseFragment = this.parentAlert.getBaseFragment();
        if ((baseFragment instanceof ChatActivity) && ((ChatActivity) baseFragment).contentView.measureKeyboardHeight() > AndroidUtilities.dp(20.0f)) {
            AndroidUtilities.hideKeyboard(this.parentAlert.baseFragment.getFragmentView());
            AndroidUtilities.runOnUIThread(new ChatAttachAlertBotWebViewLayout$$ExternalSyntheticLambda3(this), 250L);
            return;
        }
        this.parentAlert.getWindow().setSoftInputMode(20);
        setFocusable(true);
        this.parentAlert.setFocusable(true);
    }

    @Override // org.telegram.ui.Components.ChatAttachAlert.AttachAlertLayout
    public void onHidden() {
        super.onHidden();
        this.parentAlert.setFocusable(false);
        this.parentAlert.getWindow().setSoftInputMode(48);
    }

    @Override // org.telegram.ui.Components.ChatAttachAlert.AttachAlertLayout
    public int getCurrentItemTop() {
        return (int) (this.swipeContainer.getSwipeOffsetY() + this.swipeContainer.getOffsetY());
    }

    @Override // android.view.View
    public void setTranslationY(float f) {
        super.setTranslationY(f);
        this.parentAlert.getSheetContainer().invalidate();
    }

    public String getStartCommand() {
        return this.startCommand;
    }

    public void requestWebView(int i, long j, long j2, boolean z, int i2, String str) {
        TLRPC$ChatFull chatFull;
        TLRPC$Peer tLRPC$Peer;
        this.currentAccount = i;
        this.peerId = j;
        this.botId = j2;
        this.silent = z;
        this.replyToMsgId = i2;
        this.startCommand = str;
        this.webViewContainer.setBotUser(MessagesController.getInstance(i).getUser(Long.valueOf(j2)));
        this.webViewContainer.loadFlickerAndSettingsItem(i, j2, this.settingsItem);
        TLRPC$TL_messages_requestWebView tLRPC$TL_messages_requestWebView = new TLRPC$TL_messages_requestWebView();
        tLRPC$TL_messages_requestWebView.peer = MessagesController.getInstance(i).getInputPeer(j);
        tLRPC$TL_messages_requestWebView.bot = MessagesController.getInstance(i).getInputUser(j2);
        tLRPC$TL_messages_requestWebView.silent = z;
        if (j < 0 && (chatFull = MessagesController.getInstance(i).getChatFull(-j)) != null && (tLRPC$Peer = chatFull.default_send_as) != null) {
            tLRPC$TL_messages_requestWebView.send_as = MessagesController.getInstance(i).getInputPeer(tLRPC$Peer);
            tLRPC$TL_messages_requestWebView.flags |= 8192;
        }
        if (str != null) {
            tLRPC$TL_messages_requestWebView.start_param = str;
            tLRPC$TL_messages_requestWebView.flags |= 8;
        }
        if (i2 != 0) {
            tLRPC$TL_messages_requestWebView.reply_to_msg_id = i2;
            tLRPC$TL_messages_requestWebView.flags |= 1;
        }
        try {
            JSONObject jSONObject = new JSONObject();
            jSONObject.put("bg_color", getThemedColor("dialogBackground"));
            jSONObject.put("secondary_bg_color", getThemedColor("windowBackgroundGray"));
            jSONObject.put("text_color", getThemedColor("windowBackgroundWhiteBlackText"));
            jSONObject.put("hint_color", getThemedColor("windowBackgroundWhiteHintText"));
            jSONObject.put("link_color", getThemedColor("windowBackgroundWhiteLinkText"));
            jSONObject.put("button_color", getThemedColor("featuredStickers_addButton"));
            jSONObject.put("button_text_color", getThemedColor("featuredStickers_buttonText"));
            TLRPC$TL_dataJSON tLRPC$TL_dataJSON = new TLRPC$TL_dataJSON();
            tLRPC$TL_messages_requestWebView.theme_params = tLRPC$TL_dataJSON;
            tLRPC$TL_dataJSON.data = jSONObject.toString();
            tLRPC$TL_messages_requestWebView.flags |= 4;
        } catch (Exception e) {
            FileLog.e(e);
        }
        ConnectionsManager.getInstance(i).sendRequest(tLRPC$TL_messages_requestWebView, new ChatAttachAlertBotWebViewLayout$$ExternalSyntheticLambda12(this, i));
        NotificationCenter.getInstance(i).addObserver(this, NotificationCenter.webViewResultSent);
    }

    public /* synthetic */ void lambda$requestWebView$12(int i, TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        AndroidUtilities.runOnUIThread(new ChatAttachAlertBotWebViewLayout$$ExternalSyntheticLambda8(this, tLObject, i));
    }

    public /* synthetic */ void lambda$requestWebView$11(TLObject tLObject, int i) {
        if (tLObject instanceof TLRPC$TL_webViewResultUrl) {
            TLRPC$TL_webViewResultUrl tLRPC$TL_webViewResultUrl = (TLRPC$TL_webViewResultUrl) tLObject;
            this.queryId = tLRPC$TL_webViewResultUrl.query_id;
            this.webViewContainer.loadUrl(i, tLRPC$TL_webViewResultUrl.url);
            this.swipeContainer.setWebView(this.webViewContainer.getWebView());
            AndroidUtilities.runOnUIThread(this.pollRunnable);
        }
    }

    @Override // org.telegram.ui.Components.ChatAttachAlert.AttachAlertLayout
    void onDestroy() {
        NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.webViewResultSent);
        NotificationCenter.getGlobalInstance().removeObserver(this, NotificationCenter.didSetNewTheme);
        ActionBarMenu createMenu = this.parentAlert.actionBar.createMenu();
        this.otherItem.removeAllSubItems();
        createMenu.removeView(this.otherItem);
        this.webViewContainer.destroyWebView();
        this.destroyed = true;
        AndroidUtilities.cancelRunOnUIThread(this.pollRunnable);
    }

    @Override // org.telegram.ui.Components.ChatAttachAlert.AttachAlertLayout
    public void onHide() {
        super.onHide();
        this.otherItem.setVisibility(8);
        this.isBotButtonAvailable = false;
        if (!this.webViewContainer.isBackButtonVisible()) {
            AndroidUtilities.updateImageViewImageAnimated(this.parentAlert.actionBar.getBackButton(), 2131165449);
        }
        this.parentAlert.actionBar.setBackgroundColor(getThemedColor("windowBackgroundWhite"));
        if (this.webViewContainer.hasUserPermissions()) {
            this.webViewContainer.destroyWebView();
            this.needReload = true;
        }
    }

    public boolean needReload() {
        if (this.needReload) {
            this.needReload = false;
            return true;
        }
        return false;
    }

    @Override // org.telegram.ui.Components.ChatAttachAlert.AttachAlertLayout
    public int getListTopPadding() {
        return (int) this.swipeContainer.getOffsetY();
    }

    @Override // org.telegram.ui.Components.ChatAttachAlert.AttachAlertLayout
    int getFirstOffset() {
        return getListTopPadding() + AndroidUtilities.dp(56.0f);
    }

    /* JADX WARN: Removed duplicated region for block: B:10:0x0021  */
    /* JADX WARN: Removed duplicated region for block: B:13:0x002d  */
    /* JADX WARN: Removed duplicated region for block: B:15:? A[RETURN, SYNTHETIC] */
    @Override // org.telegram.ui.Components.ChatAttachAlert.AttachAlertLayout
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    void onPreMeasure(int i, int i2) {
        int i3;
        float f;
        if (!AndroidUtilities.isTablet()) {
            android.graphics.Point point = AndroidUtilities.displaySize;
            if (point.x > point.y) {
                i3 = (int) (i2 / 3.5f);
                this.parentAlert.setAllowNestedScroll(true);
                if (i3 < 0) {
                    i3 = 0;
                }
                f = i3;
                if (this.swipeContainer.getOffsetY() != f) {
                    return;
                }
                this.ignoreLayout = true;
                this.swipeContainer.setOffsetY(f);
                this.ignoreLayout = false;
                return;
            }
        }
        i3 = (i2 / 5) * 2;
        this.parentAlert.setAllowNestedScroll(true);
        if (i3 < 0) {
        }
        f = i3;
        if (this.swipeContainer.getOffsetY() != f) {
        }
    }

    @Override // org.telegram.ui.Components.ChatAttachAlert.AttachAlertLayout
    int getButtonsHideOffset() {
        return ((int) this.swipeContainer.getTopActionBarOffsetY()) + AndroidUtilities.dp(12.0f);
    }

    @Override // org.telegram.ui.Components.ChatAttachAlert.AttachAlertLayout
    public boolean onBackPressed() {
        return this.webViewContainer.onBackPressed();
    }

    @Override // android.view.View, android.view.ViewParent
    public void requestLayout() {
        if (this.ignoreLayout) {
            return;
        }
        super.requestLayout();
    }

    @Override // org.telegram.ui.Components.ChatAttachAlert.AttachAlertLayout
    public void scrollToTop() {
        WebViewSwipeContainer webViewSwipeContainer = this.swipeContainer;
        webViewSwipeContainer.stickTo((-webViewSwipeContainer.getOffsetY()) + this.swipeContainer.getTopActionBarOffsetY());
    }

    public BotWebViewContainer getWebViewContainer() {
        return this.webViewContainer;
    }

    public void setDelegate(BotWebViewContainer.Delegate delegate) {
        this.webViewContainer.setDelegate(delegate);
    }

    public boolean isBotButtonAvailable() {
        return this.isBotButtonAvailable;
    }

    @Override // org.telegram.messenger.NotificationCenter.NotificationCenterDelegate
    public void didReceivedNotification(int i, int i2, Object... objArr) {
        if (i == NotificationCenter.webViewResultSent) {
            if (this.queryId != ((Long) objArr[0]).longValue()) {
                return;
            }
            this.webViewContainer.destroyWebView();
            this.needReload = true;
            this.parentAlert.dismiss();
        } else if (i != NotificationCenter.didSetNewTheme) {
        } else {
            this.webViewContainer.updateFlickerBackgroundColor(getThemedColor("dialogBackground"));
        }
    }

    /* loaded from: classes3.dex */
    public static class WebViewSwipeContainer extends FrameLayout {
        public static final SimpleFloatPropertyCompat<WebViewSwipeContainer> SWIPE_OFFSET_Y = new SimpleFloatPropertyCompat<>("swipeOffsetY", ChatAttachAlertBotWebViewLayout$WebViewSwipeContainer$$ExternalSyntheticLambda4.INSTANCE, ChatAttachAlertBotWebViewLayout$WebViewSwipeContainer$$ExternalSyntheticLambda5.INSTANCE);
        private Delegate delegate;
        private boolean flingInProgress;
        private GestureDetectorCompat gestureDetector;
        private boolean isScrolling;
        private boolean isSwipeDisallowed;
        private boolean isSwipeOffsetAnimationDisallowed;
        private SpringAnimation offsetYAnimator;
        private SpringAnimation scrollAnimator;
        private Runnable scrollEndListener;
        private Runnable scrollListener;
        private float swipeOffsetY;
        private int swipeStickyRange;
        private WebView webView;
        private float topActionBarOffsetY = ActionBar.getCurrentActionBarHeight();
        private float offsetY = 0.0f;
        private float pendingOffsetY = -1.0f;
        private float pendingSwipeOffsetY = -2.14748365E9f;
        private GenericProvider<Void, Boolean> isKeyboardVisible = ChatAttachAlertBotWebViewLayout$WebViewSwipeContainer$$ExternalSyntheticLambda3.INSTANCE;

        /* loaded from: classes3.dex */
        public interface Delegate {
            void onDismiss();
        }

        static /* synthetic */ float access$1124(WebViewSwipeContainer webViewSwipeContainer, float f) {
            float f2 = webViewSwipeContainer.swipeOffsetY - f;
            webViewSwipeContainer.swipeOffsetY = f2;
            return f2;
        }

        public WebViewSwipeContainer(Context context) {
            super(context);
            this.gestureDetector = new GestureDetectorCompat(context, new AnonymousClass1(ViewConfiguration.get(context).getScaledTouchSlop()));
            updateStickyRange();
        }

        /* renamed from: org.telegram.ui.Components.ChatAttachAlertBotWebViewLayout$WebViewSwipeContainer$1 */
        /* loaded from: classes3.dex */
        public class AnonymousClass1 extends GestureDetector.SimpleOnGestureListener {
            final /* synthetic */ int val$touchSlop;

            AnonymousClass1(int i) {
                WebViewSwipeContainer.this = r1;
                this.val$touchSlop = i;
            }

            @Override // android.view.GestureDetector.SimpleOnGestureListener, android.view.GestureDetector.OnGestureListener
            public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent2, float f, float f2) {
                if (WebViewSwipeContainer.this.isSwipeDisallowed) {
                    return false;
                }
                if (f2 < 700.0f || (WebViewSwipeContainer.this.webView != null && WebViewSwipeContainer.this.webView.getScrollY() != 0)) {
                    if (f2 <= -700.0f && WebViewSwipeContainer.this.swipeOffsetY > (-WebViewSwipeContainer.this.offsetY) + WebViewSwipeContainer.this.topActionBarOffsetY) {
                        WebViewSwipeContainer.this.flingInProgress = true;
                        WebViewSwipeContainer webViewSwipeContainer = WebViewSwipeContainer.this;
                        webViewSwipeContainer.stickTo((-webViewSwipeContainer.offsetY) + WebViewSwipeContainer.this.topActionBarOffsetY);
                    }
                    return true;
                }
                WebViewSwipeContainer.this.flingInProgress = true;
                if (WebViewSwipeContainer.this.swipeOffsetY >= WebViewSwipeContainer.this.swipeStickyRange) {
                    if (WebViewSwipeContainer.this.delegate != null) {
                        WebViewSwipeContainer.this.delegate.onDismiss();
                    }
                } else {
                    WebViewSwipeContainer.this.stickTo(0.0f);
                }
                return true;
            }

            @Override // android.view.GestureDetector.SimpleOnGestureListener, android.view.GestureDetector.OnGestureListener
            public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent2, float f, float f2) {
                if (!WebViewSwipeContainer.this.isScrolling && !WebViewSwipeContainer.this.isSwipeDisallowed) {
                    if (((Boolean) WebViewSwipeContainer.this.isKeyboardVisible.provide(null)).booleanValue() && WebViewSwipeContainer.this.swipeOffsetY == (-WebViewSwipeContainer.this.offsetY) + WebViewSwipeContainer.this.topActionBarOffsetY) {
                        WebViewSwipeContainer.this.isSwipeDisallowed = true;
                    } else if (Math.abs(f2) < this.val$touchSlop || Math.abs(f2) * 1.5f < Math.abs(f) || (WebViewSwipeContainer.this.swipeOffsetY == (-WebViewSwipeContainer.this.offsetY) + WebViewSwipeContainer.this.topActionBarOffsetY && WebViewSwipeContainer.this.webView != null && (f2 >= 0.0f || WebViewSwipeContainer.this.webView.getScrollY() != 0))) {
                        if (WebViewSwipeContainer.this.webView != null) {
                            if (WebViewSwipeContainer.this.webView.canScrollHorizontally(f >= 0.0f ? 1 : -1)) {
                                WebViewSwipeContainer.this.isSwipeDisallowed = true;
                            }
                        }
                    } else {
                        WebViewSwipeContainer.this.isScrolling = true;
                        MotionEvent obtain = MotionEvent.obtain(0L, 0L, 3, 0.0f, 0.0f, 0);
                        for (int i = 0; i < WebViewSwipeContainer.this.getChildCount(); i++) {
                            WebViewSwipeContainer.this.getChildAt(i).dispatchTouchEvent(obtain);
                        }
                        obtain.recycle();
                        return true;
                    }
                }
                if (WebViewSwipeContainer.this.isScrolling) {
                    if (f2 < 0.0f) {
                        if (WebViewSwipeContainer.this.swipeOffsetY <= (-WebViewSwipeContainer.this.offsetY) + WebViewSwipeContainer.this.topActionBarOffsetY) {
                            if (WebViewSwipeContainer.this.webView != null) {
                                float scrollY = WebViewSwipeContainer.this.webView.getScrollY() + f2;
                                WebViewSwipeContainer.this.webView.setScrollY((int) MathUtils.clamp(scrollY, 0.0f, Math.max(WebViewSwipeContainer.this.webView.getContentHeight(), WebViewSwipeContainer.this.webView.getHeight()) - WebViewSwipeContainer.this.topActionBarOffsetY));
                                if (scrollY < 0.0f) {
                                    WebViewSwipeContainer.access$1124(WebViewSwipeContainer.this, scrollY);
                                }
                            } else {
                                WebViewSwipeContainer.access$1124(WebViewSwipeContainer.this, f2);
                            }
                        } else {
                            WebViewSwipeContainer.access$1124(WebViewSwipeContainer.this, f2);
                        }
                    } else {
                        WebViewSwipeContainer.access$1124(WebViewSwipeContainer.this, f2);
                        if (WebViewSwipeContainer.this.webView != null && WebViewSwipeContainer.this.swipeOffsetY < (-WebViewSwipeContainer.this.offsetY) + WebViewSwipeContainer.this.topActionBarOffsetY) {
                            WebViewSwipeContainer.this.webView.setScrollY((int) MathUtils.clamp(WebViewSwipeContainer.this.webView.getScrollY() - ((WebViewSwipeContainer.this.swipeOffsetY + WebViewSwipeContainer.this.offsetY) - WebViewSwipeContainer.this.topActionBarOffsetY), 0.0f, Math.max(WebViewSwipeContainer.this.webView.getContentHeight(), WebViewSwipeContainer.this.webView.getHeight()) - WebViewSwipeContainer.this.topActionBarOffsetY));
                        }
                    }
                    WebViewSwipeContainer webViewSwipeContainer = WebViewSwipeContainer.this;
                    webViewSwipeContainer.swipeOffsetY = MathUtils.clamp(webViewSwipeContainer.swipeOffsetY, (-WebViewSwipeContainer.this.offsetY) + WebViewSwipeContainer.this.topActionBarOffsetY, (WebViewSwipeContainer.this.getHeight() - WebViewSwipeContainer.this.offsetY) + WebViewSwipeContainer.this.topActionBarOffsetY);
                    WebViewSwipeContainer.this.invalidateTranslation();
                }
                return true;
            }
        }

        public void setIsKeyboardVisible(GenericProvider<Void, Boolean> genericProvider) {
            this.isKeyboardVisible = genericProvider;
        }

        @Override // android.view.View
        protected void onConfigurationChanged(Configuration configuration) {
            super.onConfigurationChanged(configuration);
            updateStickyRange();
        }

        private void updateStickyRange() {
            android.graphics.Point point = AndroidUtilities.displaySize;
            this.swipeStickyRange = AndroidUtilities.dp(point.x > point.y ? 8.0f : 64.0f);
        }

        @Override // android.view.ViewGroup, android.view.ViewParent
        public void requestDisallowInterceptTouchEvent(boolean z) {
            super.requestDisallowInterceptTouchEvent(z);
            if (z) {
                this.isSwipeDisallowed = true;
                this.isScrolling = false;
            }
        }

        public void setSwipeOffsetAnimationDisallowed(boolean z) {
            this.isSwipeOffsetAnimationDisallowed = z;
        }

        public void setScrollListener(Runnable runnable) {
            this.scrollListener = runnable;
        }

        public void setScrollEndListener(Runnable runnable) {
            this.scrollEndListener = runnable;
        }

        public void setWebView(WebView webView) {
            this.webView = webView;
        }

        public void setTopActionBarOffsetY(float f) {
            this.topActionBarOffsetY = f;
            invalidateTranslation();
        }

        public void setSwipeOffsetY(float f) {
            this.swipeOffsetY = f;
            invalidateTranslation();
        }

        public void setOffsetY(float f) {
            if (this.pendingSwipeOffsetY != -2.14748365E9f) {
                this.pendingOffsetY = f;
                return;
            }
            SpringAnimation springAnimation = this.offsetYAnimator;
            if (springAnimation != null) {
                springAnimation.cancel();
            }
            float f2 = this.offsetY;
            float f3 = f - f2;
            boolean z = Math.abs((this.swipeOffsetY + f2) - this.topActionBarOffsetY) <= ((float) AndroidUtilities.dp(1.0f));
            if (!this.isSwipeOffsetAnimationDisallowed) {
                SpringAnimation springAnimation2 = this.offsetYAnimator;
                if (springAnimation2 != null) {
                    springAnimation2.cancel();
                }
                SpringAnimation addEndListener = new SpringAnimation(new FloatValueHolder(f2)).setSpring(new SpringForce(f).setStiffness(1400.0f).setDampingRatio(1.0f)).addUpdateListener(new ChatAttachAlertBotWebViewLayout$WebViewSwipeContainer$$ExternalSyntheticLambda2(this, f2, f3, z, f)).addEndListener(new ChatAttachAlertBotWebViewLayout$WebViewSwipeContainer$$ExternalSyntheticLambda0(this, f));
                this.offsetYAnimator = addEndListener;
                addEndListener.start();
                return;
            }
            this.offsetY = f;
            if (z) {
                this.swipeOffsetY = MathUtils.clamp(this.swipeOffsetY - Math.max(0.0f, f3), (-this.offsetY) + this.topActionBarOffsetY, (getHeight() - this.offsetY) + this.topActionBarOffsetY);
            }
            invalidateTranslation();
        }

        public /* synthetic */ void lambda$setOffsetY$1(float f, float f2, boolean z, float f3, DynamicAnimation dynamicAnimation, float f4, float f5) {
            this.offsetY = f4;
            float f6 = (f4 - f) / f2;
            if (z) {
                this.swipeOffsetY = MathUtils.clamp(this.swipeOffsetY - (f6 * Math.max(0.0f, f2)), (-this.offsetY) + this.topActionBarOffsetY, (getHeight() - this.offsetY) + this.topActionBarOffsetY);
            }
            SpringAnimation springAnimation = this.scrollAnimator;
            if (springAnimation != null && springAnimation.getSpring().getFinalPosition() == (-f) + this.topActionBarOffsetY) {
                this.scrollAnimator.getSpring().setFinalPosition((-f3) + this.topActionBarOffsetY);
            }
            invalidateTranslation();
        }

        public /* synthetic */ void lambda$setOffsetY$2(float f, DynamicAnimation dynamicAnimation, boolean z, float f2, float f3) {
            this.offsetYAnimator = null;
            if (!z) {
                this.offsetY = f;
                invalidateTranslation();
                return;
            }
            this.pendingOffsetY = f;
        }

        public void invalidateTranslation() {
            setTranslationY(Math.max(this.topActionBarOffsetY, this.offsetY + this.swipeOffsetY));
            Runnable runnable = this.scrollListener;
            if (runnable != null) {
                runnable.run();
            }
        }

        public float getTopActionBarOffsetY() {
            return this.topActionBarOffsetY;
        }

        public float getOffsetY() {
            return this.offsetY;
        }

        public float getSwipeOffsetY() {
            return this.swipeOffsetY;
        }

        public void setDelegate(Delegate delegate) {
            this.delegate = delegate;
        }

        @Override // android.view.ViewGroup, android.view.View
        public boolean dispatchTouchEvent(MotionEvent motionEvent) {
            if (!this.isScrolling || motionEvent.getActionIndex() == 0) {
                MotionEvent obtain = MotionEvent.obtain(motionEvent);
                int actionIndex = motionEvent.getActionIndex();
                if (Build.VERSION.SDK_INT >= 29) {
                    obtain.setLocation(motionEvent.getRawX(actionIndex), motionEvent.getRawY(actionIndex));
                } else {
                    obtain.setLocation(motionEvent.getX(actionIndex) + (motionEvent.getRawX() - motionEvent.getX()), motionEvent.getY(actionIndex) + (motionEvent.getRawY() - motionEvent.getY()));
                }
                boolean onTouchEvent = this.gestureDetector.onTouchEvent(obtain);
                obtain.recycle();
                if (motionEvent.getAction() == 1 || motionEvent.getAction() == 3) {
                    this.isSwipeDisallowed = false;
                    this.isScrolling = false;
                    if (this.flingInProgress) {
                        this.flingInProgress = false;
                    } else {
                        float f = this.swipeOffsetY;
                        int i = this.swipeStickyRange;
                        if (f <= (-i)) {
                            stickTo((-this.offsetY) + this.topActionBarOffsetY);
                        } else if (f > (-i) && f <= i) {
                            stickTo(0.0f);
                        } else {
                            Delegate delegate = this.delegate;
                            if (delegate != null) {
                                delegate.onDismiss();
                            }
                        }
                    }
                }
                boolean dispatchTouchEvent = super.dispatchTouchEvent(motionEvent);
                return (!dispatchTouchEvent && !onTouchEvent && motionEvent.getAction() == 0) || dispatchTouchEvent || onTouchEvent;
            }
            return false;
        }

        public void stickTo(float f) {
            stickTo(f, null);
        }

        public void stickTo(float f, Runnable runnable) {
            SpringAnimation springAnimation;
            if (this.swipeOffsetY == f || ((springAnimation = this.scrollAnimator) != null && springAnimation.getSpring().getFinalPosition() == f)) {
                if (runnable != null) {
                    runnable.run();
                }
                Runnable runnable2 = this.scrollEndListener;
                if (runnable2 == null) {
                    return;
                }
                runnable2.run();
                return;
            }
            this.pendingSwipeOffsetY = f;
            SpringAnimation springAnimation2 = this.offsetYAnimator;
            if (springAnimation2 != null) {
                springAnimation2.cancel();
            }
            SpringAnimation springAnimation3 = this.scrollAnimator;
            if (springAnimation3 != null) {
                springAnimation3.cancel();
            }
            SpringAnimation addEndListener = new SpringAnimation(this, SWIPE_OFFSET_Y, f).setSpring(new SpringForce(f).setStiffness(1400.0f).setDampingRatio(1.0f)).addEndListener(new ChatAttachAlertBotWebViewLayout$WebViewSwipeContainer$$ExternalSyntheticLambda1(this, runnable));
            this.scrollAnimator = addEndListener;
            addEndListener.start();
        }

        public /* synthetic */ void lambda$stickTo$3(Runnable runnable, DynamicAnimation dynamicAnimation, boolean z, float f, float f2) {
            if (dynamicAnimation == this.scrollAnimator) {
                this.scrollAnimator = null;
                if (runnable != null) {
                    runnable.run();
                }
                Runnable runnable2 = this.scrollEndListener;
                if (runnable2 != null) {
                    runnable2.run();
                }
                float f3 = this.pendingOffsetY;
                if (f3 != -1.0f) {
                    boolean z2 = this.isSwipeOffsetAnimationDisallowed;
                    this.isSwipeOffsetAnimationDisallowed = true;
                    setOffsetY(f3);
                    this.pendingOffsetY = -1.0f;
                    this.isSwipeOffsetAnimationDisallowed = z2;
                }
                this.pendingSwipeOffsetY = -2.14748365E9f;
            }
        }

        public boolean isSwipeInProgress() {
            return this.isScrolling;
        }
    }

    /* loaded from: classes3.dex */
    public static class WebProgressView extends View {
        private final SimpleFloatPropertyCompat<WebProgressView> LOAD_PROGRESS_PROPERTY = new SimpleFloatPropertyCompat("loadProgress", ChatAttachAlertBotWebViewLayout$WebProgressView$$ExternalSyntheticLambda0.INSTANCE, ChatAttachAlertBotWebViewLayout$WebProgressView$$ExternalSyntheticLambda1.INSTANCE).setMultiplier(100.0f);
        private Paint bluePaint;
        private float loadProgress;
        private Theme.ResourcesProvider resourcesProvider;
        private SpringAnimation springAnimation;

        public WebProgressView(Context context, Theme.ResourcesProvider resourcesProvider) {
            super(context);
            Paint paint = new Paint(1);
            this.bluePaint = paint;
            this.resourcesProvider = resourcesProvider;
            paint.setColor(getThemedColor("featuredStickers_addButton"));
            this.bluePaint.setStyle(Paint.Style.STROKE);
            this.bluePaint.setStrokeWidth(AndroidUtilities.dp(2.0f));
            this.bluePaint.setStrokeCap(Paint.Cap.ROUND);
        }

        protected int getThemedColor(String str) {
            Theme.ResourcesProvider resourcesProvider = this.resourcesProvider;
            Integer color = resourcesProvider != null ? resourcesProvider.getColor(str) : null;
            return color != null ? color.intValue() : Theme.getColor(str);
        }

        @Override // android.view.View
        protected void onAttachedToWindow() {
            super.onAttachedToWindow();
            this.springAnimation = new SpringAnimation(this, this.LOAD_PROGRESS_PROPERTY).setSpring(new SpringForce().setStiffness(400.0f).setDampingRatio(1.0f));
        }

        @Override // android.view.View
        protected void onDetachedFromWindow() {
            super.onDetachedFromWindow();
            this.springAnimation.cancel();
            this.springAnimation = null;
        }

        public void setLoadProgressAnimated(float f) {
            SpringAnimation springAnimation = this.springAnimation;
            if (springAnimation == null) {
                setLoadProgress(f);
                return;
            }
            springAnimation.getSpring().setFinalPosition(f * 100.0f);
            this.springAnimation.start();
        }

        public void setLoadProgress(float f) {
            this.loadProgress = f;
            invalidate();
        }

        @Override // android.view.View
        public void draw(Canvas canvas) {
            super.draw(canvas);
            float height = getHeight() - (this.bluePaint.getStrokeWidth() / 2.0f);
            canvas.drawLine(0.0f, height, getWidth() * this.loadProgress, height, this.bluePaint);
        }
    }
}