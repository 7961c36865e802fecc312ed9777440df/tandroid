package org.telegram.ui.bots;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.text.TextPaint;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowInsets;
import android.view.WindowManager;
import android.widget.TextView;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.ColorUtils;
import androidx.core.math.MathUtils;
import androidx.core.util.Consumer;
import androidx.dynamicanimation.animation.DynamicAnimation;
import androidx.dynamicanimation.animation.SpringAnimation;
import androidx.dynamicanimation.animation.SpringForce;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import org.json.JSONObject;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.AnimationNotificationsLocker;
import org.telegram.messenger.ContactsController;
import org.telegram.messenger.DialogObject;
import org.telegram.messenger.Emoji;
import org.telegram.messenger.FileLoader;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.GenericProvider;
import org.telegram.messenger.ImageLocation;
import org.telegram.messenger.LiteMode;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MediaDataController;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.MessagesStorage;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.R;
import org.telegram.messenger.SendMessagesHelper;
import org.telegram.messenger.UserObject;
import org.telegram.messenger.Utilities;
import org.telegram.messenger.browser.Browser;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC$BotApp;
import org.telegram.tgnet.TLRPC$InputPeer;
import org.telegram.tgnet.TLRPC$TL_appWebViewResultUrl;
import org.telegram.tgnet.TLRPC$TL_attachMenuBot;
import org.telegram.tgnet.TLRPC$TL_dataJSON;
import org.telegram.tgnet.TLRPC$TL_error;
import org.telegram.tgnet.TLRPC$TL_inputBotAppID;
import org.telegram.tgnet.TLRPC$TL_messages_prolongWebView;
import org.telegram.tgnet.TLRPC$TL_messages_requestAppWebView;
import org.telegram.tgnet.TLRPC$TL_messages_requestSimpleWebView;
import org.telegram.tgnet.TLRPC$TL_messages_requestWebView;
import org.telegram.tgnet.TLRPC$TL_messages_sendWebViewData;
import org.telegram.tgnet.TLRPC$TL_messages_toggleBotInAttachMenu;
import org.telegram.tgnet.TLRPC$TL_payments_paymentForm;
import org.telegram.tgnet.TLRPC$TL_payments_paymentReceipt;
import org.telegram.tgnet.TLRPC$TL_simpleWebViewResultUrl;
import org.telegram.tgnet.TLRPC$TL_updates;
import org.telegram.tgnet.TLRPC$TL_webViewResultUrl;
import org.telegram.tgnet.TLRPC$User;
import org.telegram.ui.ActionBar.ActionBar;
import org.telegram.ui.ActionBar.ActionBarMenu;
import org.telegram.ui.ActionBar.ActionBarMenuItem;
import org.telegram.ui.ActionBar.ActionBarMenuSubItem;
import org.telegram.ui.ActionBar.AlertDialog;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.ActionBar.INavigationLayout;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.ChatActivity;
import org.telegram.ui.Components.Bulletin;
import org.telegram.ui.Components.BulletinFactory;
import org.telegram.ui.Components.ChatActivityEnterView;
import org.telegram.ui.Components.CubicBezierInterpolator;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.ui.Components.OverlayActionBarLayoutDialog;
import org.telegram.ui.Components.PasscodeView;
import org.telegram.ui.Components.RadialProgressView;
import org.telegram.ui.Components.SimpleFloatPropertyCompat;
import org.telegram.ui.Components.SizeNotifierFrameLayout;
import org.telegram.ui.Components.VerticalPositionAutoAnimator;
import org.telegram.ui.DialogsActivity;
import org.telegram.ui.LaunchActivity;
import org.telegram.ui.PaymentFormActivity;
import org.telegram.ui.TopicsFragment;
import org.telegram.ui.bots.BotWebViewContainer;
import org.telegram.ui.bots.BotWebViewMenuContainer;
import org.telegram.ui.bots.BotWebViewSheet;
import org.telegram.ui.bots.ChatAttachAlertBotWebViewLayout;
/* loaded from: classes4.dex */
public class BotWebViewSheet extends Dialog implements NotificationCenter.NotificationCenterDelegate {
    private static final SimpleFloatPropertyCompat<BotWebViewSheet> ACTION_BAR_TRANSITION_PROGRESS_VALUE = new SimpleFloatPropertyCompat("actionBarTransitionProgress", new SimpleFloatPropertyCompat.Getter() { // from class: org.telegram.ui.bots.BotWebViewSheet$$ExternalSyntheticLambda24
        @Override // org.telegram.ui.Components.SimpleFloatPropertyCompat.Getter
        public final float get(Object obj) {
            float f;
            f = ((BotWebViewSheet) obj).actionBarTransitionProgress;
            return f;
        }
    }, new SimpleFloatPropertyCompat.Setter() { // from class: org.telegram.ui.bots.BotWebViewSheet$$ExternalSyntheticLambda25
        @Override // org.telegram.ui.Components.SimpleFloatPropertyCompat.Setter
        public final void set(Object obj, float f) {
            BotWebViewSheet.lambda$static$2((BotWebViewSheet) obj, f);
        }
    }).setMultiplier(100.0f);
    private ActionBar actionBar;
    private int actionBarColor;
    private boolean actionBarIsLight;
    private Paint actionBarPaint;
    private Drawable actionBarShadow;
    private float actionBarTransitionProgress;
    private Paint backgroundPaint;
    private long botId;
    private String buttonText;
    private int currentAccount;
    private Paint dimPaint;
    private boolean dismissed;
    private SizeNotifierFrameLayout frameLayout;
    private boolean ignoreLayout;
    private int lineColor;
    private Paint linePaint;
    private TextView mainButton;
    private VerticalPositionAutoAnimator mainButtonAutoAnimator;
    private boolean mainButtonProgressWasVisible;
    private boolean mainButtonWasVisible;
    private boolean needCloseConfirmation;
    private boolean overrideBackgroundColor;
    private Activity parentActivity;
    private PasscodeView passcodeView;
    private long peerId;
    private Runnable pollRunnable;
    private ChatAttachAlertBotWebViewLayout.WebProgressView progressView;
    private long queryId;
    private VerticalPositionAutoAnimator radialProgressAutoAnimator;
    private RadialProgressView radialProgressView;
    private int replyToMsgId;
    private Theme.ResourcesProvider resourcesProvider;
    private ActionBarMenuSubItem settingsItem;
    private boolean silent;
    private SpringAnimation springAnimation;
    private ChatAttachAlertBotWebViewLayout.WebViewSwipeContainer swipeContainer;
    private Boolean wasLightStatusBar;
    private BotWebViewContainer webViewContainer;

    public void showJustAddedBulletin() {
        TLRPC$TL_attachMenuBot tLRPC$TL_attachMenuBot;
        final String formatString;
        TLRPC$User user = MessagesController.getInstance(this.currentAccount).getUser(Long.valueOf(this.botId));
        Iterator<TLRPC$TL_attachMenuBot> it = MediaDataController.getInstance(this.currentAccount).getAttachMenuBots().bots.iterator();
        while (true) {
            if (!it.hasNext()) {
                tLRPC$TL_attachMenuBot = null;
                break;
            }
            tLRPC$TL_attachMenuBot = it.next();
            if (tLRPC$TL_attachMenuBot.bot_id == this.botId) {
                break;
            }
        }
        if (tLRPC$TL_attachMenuBot == null) {
            return;
        }
        boolean z = tLRPC$TL_attachMenuBot.show_in_side_menu;
        if (z && tLRPC$TL_attachMenuBot.show_in_attach_menu) {
            formatString = LocaleController.formatString("BotAttachMenuShortcatAddedAttachAndSide", R.string.BotAttachMenuShortcatAddedAttachAndSide, user.first_name);
        } else if (z) {
            formatString = LocaleController.formatString("BotAttachMenuShortcatAddedSide", R.string.BotAttachMenuShortcatAddedSide, user.first_name);
        } else {
            formatString = LocaleController.formatString("BotAttachMenuShortcatAddedAttach", R.string.BotAttachMenuShortcatAddedAttach, user.first_name);
        }
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.bots.BotWebViewSheet$$ExternalSyntheticLambda11
            @Override // java.lang.Runnable
            public final void run() {
                BotWebViewSheet.this.lambda$showJustAddedBulletin$0(formatString);
            }
        }, 200L);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$showJustAddedBulletin$0(String str) {
        BulletinFactory.of(this.frameLayout, this.resourcesProvider).createSimpleBulletin(R.raw.contact_check, AndroidUtilities.replaceTags(str)).setDuration(5000).show(true);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$static$2(BotWebViewSheet botWebViewSheet, float f) {
        botWebViewSheet.actionBarTransitionProgress = f;
        botWebViewSheet.frameLayout.invalidate();
        botWebViewSheet.actionBar.setAlpha(f);
        botWebViewSheet.updateLightStatusBar();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$5() {
        if (this.dismissed) {
            return;
        }
        TLRPC$TL_messages_prolongWebView tLRPC$TL_messages_prolongWebView = new TLRPC$TL_messages_prolongWebView();
        tLRPC$TL_messages_prolongWebView.bot = MessagesController.getInstance(this.currentAccount).getInputUser(this.botId);
        tLRPC$TL_messages_prolongWebView.peer = MessagesController.getInstance(this.currentAccount).getInputPeer(this.peerId);
        tLRPC$TL_messages_prolongWebView.query_id = this.queryId;
        tLRPC$TL_messages_prolongWebView.silent = this.silent;
        if (this.replyToMsgId != 0) {
            tLRPC$TL_messages_prolongWebView.reply_to = SendMessagesHelper.getInstance(this.currentAccount).createReplyInput(this.replyToMsgId);
            tLRPC$TL_messages_prolongWebView.flags |= 1;
        }
        ConnectionsManager.getInstance(this.currentAccount).sendRequest(tLRPC$TL_messages_prolongWebView, new RequestDelegate() { // from class: org.telegram.ui.bots.BotWebViewSheet$$ExternalSyntheticLambda19
            @Override // org.telegram.tgnet.RequestDelegate
            public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
                BotWebViewSheet.this.lambda$new$4(tLObject, tLRPC$TL_error);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$4(TLObject tLObject, final TLRPC$TL_error tLRPC$TL_error) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.bots.BotWebViewSheet$$ExternalSyntheticLambda15
            @Override // java.lang.Runnable
            public final void run() {
                BotWebViewSheet.this.lambda$new$3(tLRPC$TL_error);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$3(TLRPC$TL_error tLRPC$TL_error) {
        if (this.dismissed) {
            return;
        }
        if (tLRPC$TL_error != null) {
            dismiss();
        } else {
            AndroidUtilities.runOnUIThread(this.pollRunnable, 60000L);
        }
    }

    public BotWebViewSheet(Context context, Theme.ResourcesProvider resourcesProvider) {
        super(context, R.style.TransparentDialog);
        this.actionBarTransitionProgress = 0.0f;
        this.linePaint = new Paint(1);
        this.dimPaint = new Paint();
        this.backgroundPaint = new Paint(1);
        this.actionBarPaint = new Paint(1);
        this.pollRunnable = new Runnable() { // from class: org.telegram.ui.bots.BotWebViewSheet$$ExternalSyntheticLambda9
            @Override // java.lang.Runnable
            public final void run() {
                BotWebViewSheet.this.lambda$new$5();
            }
        };
        this.resourcesProvider = resourcesProvider;
        this.lineColor = Theme.getColor(Theme.key_sheet_scrollUp);
        this.swipeContainer = new ChatAttachAlertBotWebViewLayout.WebViewSwipeContainer(context) { // from class: org.telegram.ui.bots.BotWebViewSheet.1
            /* JADX WARN: Removed duplicated region for block: B:10:0x001f  */
            /* JADX WARN: Removed duplicated region for block: B:25:0x0081  */
            @Override // android.widget.FrameLayout, android.view.View
            /*
                Code decompiled incorrectly, please refer to instructions dump.
            */
            protected void onMeasure(int i, int i2) {
                int i3;
                float f;
                int size = View.MeasureSpec.getSize(i2);
                if (!AndroidUtilities.isTablet()) {
                    Point point = AndroidUtilities.displaySize;
                    if (point.x > point.y) {
                        i3 = (int) (size / 3.5f);
                        if (i3 < 0) {
                            i3 = 0;
                        }
                        f = i3;
                        if (getOffsetY() != f && !BotWebViewSheet.this.dismissed) {
                            BotWebViewSheet.this.ignoreLayout = true;
                            setOffsetY(f);
                            BotWebViewSheet.this.ignoreLayout = false;
                        }
                        if (AndroidUtilities.isTablet() && !AndroidUtilities.isInMultiwindow && !AndroidUtilities.isSmallTablet()) {
                            Point point2 = AndroidUtilities.displaySize;
                            i = View.MeasureSpec.makeMeasureSpec((int) (Math.min(point2.x, point2.y) * 0.8f), 1073741824);
                        }
                        super.onMeasure(i, View.MeasureSpec.makeMeasureSpec((((View.MeasureSpec.getSize(i2) - ActionBar.getCurrentActionBarHeight()) - AndroidUtilities.statusBarHeight) + AndroidUtilities.dp(24.0f)) - (BotWebViewSheet.this.mainButtonWasVisible ? BotWebViewSheet.this.mainButton.getLayoutParams().height : 0), 1073741824));
                    }
                }
                i3 = (size / 5) * 2;
                if (i3 < 0) {
                }
                f = i3;
                if (getOffsetY() != f) {
                    BotWebViewSheet.this.ignoreLayout = true;
                    setOffsetY(f);
                    BotWebViewSheet.this.ignoreLayout = false;
                }
                if (AndroidUtilities.isTablet()) {
                    Point point22 = AndroidUtilities.displaySize;
                    i = View.MeasureSpec.makeMeasureSpec((int) (Math.min(point22.x, point22.y) * 0.8f), 1073741824);
                }
                super.onMeasure(i, View.MeasureSpec.makeMeasureSpec((((View.MeasureSpec.getSize(i2) - ActionBar.getCurrentActionBarHeight()) - AndroidUtilities.statusBarHeight) + AndroidUtilities.dp(24.0f)) - (BotWebViewSheet.this.mainButtonWasVisible ? BotWebViewSheet.this.mainButton.getLayoutParams().height : 0), 1073741824));
            }

            @Override // android.view.View, android.view.ViewParent
            public void requestLayout() {
                if (BotWebViewSheet.this.ignoreLayout) {
                    return;
                }
                super.requestLayout();
            }
        };
        int i = Theme.key_windowBackgroundWhite;
        BotWebViewContainer botWebViewContainer = new BotWebViewContainer(context, resourcesProvider, getColor(i)) { // from class: org.telegram.ui.bots.BotWebViewSheet.2
            @Override // org.telegram.ui.bots.BotWebViewContainer
            public void onWebViewCreated() {
                super.onWebViewCreated();
                BotWebViewSheet.this.swipeContainer.setWebView(BotWebViewSheet.this.webViewContainer.getWebView());
            }
        };
        this.webViewContainer = botWebViewContainer;
        botWebViewContainer.setDelegate(new 3(resourcesProvider, context));
        this.linePaint.setStyle(Paint.Style.FILL_AND_STROKE);
        this.linePaint.setStrokeWidth(AndroidUtilities.dp(4.0f));
        this.linePaint.setStrokeCap(Paint.Cap.ROUND);
        this.dimPaint.setColor(1073741824);
        this.actionBarColor = getColor(i);
        SizeNotifierFrameLayout sizeNotifierFrameLayout = new SizeNotifierFrameLayout(context) { // from class: org.telegram.ui.bots.BotWebViewSheet.4
            {
                setWillNotDraw(false);
            }

            @Override // android.view.View
            protected void onDraw(Canvas canvas) {
                super.onDraw(canvas);
                if (BotWebViewSheet.this.passcodeView.getVisibility() != 0) {
                    if (!BotWebViewSheet.this.overrideBackgroundColor) {
                        BotWebViewSheet.this.backgroundPaint.setColor(BotWebViewSheet.this.getColor(Theme.key_windowBackgroundWhite));
                    }
                    RectF rectF = AndroidUtilities.rectTmp;
                    rectF.set(0.0f, 0.0f, getWidth(), getHeight());
                    canvas.drawRect(rectF, BotWebViewSheet.this.dimPaint);
                    BotWebViewSheet.this.actionBarPaint.setColor(BotWebViewSheet.this.actionBarColor);
                    float dp = AndroidUtilities.dp(16.0f) * (AndroidUtilities.isTablet() ? 1.0f : 1.0f - BotWebViewSheet.this.actionBarTransitionProgress);
                    rectF.set(BotWebViewSheet.this.swipeContainer.getLeft(), AndroidUtilities.lerp(BotWebViewSheet.this.swipeContainer.getTranslationY(), 0.0f, BotWebViewSheet.this.actionBarTransitionProgress), BotWebViewSheet.this.swipeContainer.getRight(), BotWebViewSheet.this.swipeContainer.getTranslationY() + AndroidUtilities.dp(24.0f) + dp);
                    canvas.drawRoundRect(rectF, dp, dp, BotWebViewSheet.this.actionBarPaint);
                    rectF.set(BotWebViewSheet.this.swipeContainer.getLeft(), BotWebViewSheet.this.swipeContainer.getTranslationY() + AndroidUtilities.dp(24.0f), BotWebViewSheet.this.swipeContainer.getRight(), getHeight());
                    canvas.drawRect(rectF, BotWebViewSheet.this.backgroundPaint);
                }
            }

            @Override // android.view.View
            public void draw(Canvas canvas) {
                super.draw(canvas);
                float f = AndroidUtilities.isTablet() ? 0.0f : BotWebViewSheet.this.actionBarTransitionProgress;
                BotWebViewSheet.this.linePaint.setColor(BotWebViewSheet.this.lineColor);
                BotWebViewSheet.this.linePaint.setAlpha((int) (BotWebViewSheet.this.linePaint.getAlpha() * (1.0f - (Math.min(0.5f, f) / 0.5f))));
                canvas.save();
                float f2 = 1.0f - f;
                float lerp = AndroidUtilities.isTablet() ? AndroidUtilities.lerp(BotWebViewSheet.this.swipeContainer.getTranslationY() + AndroidUtilities.dp(12.0f), AndroidUtilities.statusBarHeight / 2.0f, BotWebViewSheet.this.actionBarTransitionProgress) : AndroidUtilities.lerp(BotWebViewSheet.this.swipeContainer.getTranslationY(), AndroidUtilities.statusBarHeight + (ActionBar.getCurrentActionBarHeight() / 2.0f), f) + AndroidUtilities.dp(12.0f);
                canvas.scale(f2, f2, getWidth() / 2.0f, lerp);
                canvas.drawLine((getWidth() / 2.0f) - AndroidUtilities.dp(16.0f), lerp, (getWidth() / 2.0f) + AndroidUtilities.dp(16.0f), lerp, BotWebViewSheet.this.linePaint);
                canvas.restore();
                BotWebViewSheet.this.actionBarShadow.setAlpha((int) (BotWebViewSheet.this.actionBar.getAlpha() * 255.0f));
                float y = BotWebViewSheet.this.actionBar.getY() + BotWebViewSheet.this.actionBar.getTranslationY() + BotWebViewSheet.this.actionBar.getHeight();
                BotWebViewSheet.this.actionBarShadow.setBounds(0, (int) y, getWidth(), (int) (y + BotWebViewSheet.this.actionBarShadow.getIntrinsicHeight()));
                BotWebViewSheet.this.actionBarShadow.draw(canvas);
            }

            @Override // android.view.View
            @SuppressLint({"ClickableViewAccessibility"})
            public boolean onTouchEvent(MotionEvent motionEvent) {
                if (motionEvent.getAction() == 0 && (motionEvent.getY() <= AndroidUtilities.lerp(BotWebViewSheet.this.swipeContainer.getTranslationY() + AndroidUtilities.dp(24.0f), 0.0f, BotWebViewSheet.this.actionBarTransitionProgress) || motionEvent.getX() > BotWebViewSheet.this.swipeContainer.getRight() || motionEvent.getX() < BotWebViewSheet.this.swipeContainer.getLeft())) {
                    BotWebViewSheet.this.onCheckDismissByUser();
                    return true;
                }
                return super.onTouchEvent(motionEvent);
            }

            /* JADX INFO: Access modifiers changed from: protected */
            @Override // org.telegram.ui.Components.SizeNotifierFrameLayout, android.view.ViewGroup, android.view.View
            public void onAttachedToWindow() {
                super.onAttachedToWindow();
                Bulletin.addDelegate(this, new Bulletin.Delegate(this) { // from class: org.telegram.ui.bots.BotWebViewSheet.4.1
                    @Override // org.telegram.ui.Components.Bulletin.Delegate
                    public /* synthetic */ boolean allowLayoutChanges() {
                        return Bulletin.Delegate.-CC.$default$allowLayoutChanges(this);
                    }

                    @Override // org.telegram.ui.Components.Bulletin.Delegate
                    public /* synthetic */ boolean clipWithGradient(int i2) {
                        return Bulletin.Delegate.-CC.$default$clipWithGradient(this, i2);
                    }

                    @Override // org.telegram.ui.Components.Bulletin.Delegate
                    public /* synthetic */ int getBottomOffset(int i2) {
                        return Bulletin.Delegate.-CC.$default$getBottomOffset(this, i2);
                    }

                    @Override // org.telegram.ui.Components.Bulletin.Delegate
                    public /* synthetic */ void onBottomOffsetChange(float f) {
                        Bulletin.Delegate.-CC.$default$onBottomOffsetChange(this, f);
                    }

                    @Override // org.telegram.ui.Components.Bulletin.Delegate
                    public /* synthetic */ void onHide(Bulletin bulletin) {
                        Bulletin.Delegate.-CC.$default$onHide(this, bulletin);
                    }

                    @Override // org.telegram.ui.Components.Bulletin.Delegate
                    public /* synthetic */ void onShow(Bulletin bulletin) {
                        Bulletin.Delegate.-CC.$default$onShow(this, bulletin);
                    }

                    @Override // org.telegram.ui.Components.Bulletin.Delegate
                    public int getTopOffset(int i2) {
                        return AndroidUtilities.statusBarHeight;
                    }
                });
            }

            /* JADX INFO: Access modifiers changed from: protected */
            @Override // org.telegram.ui.Components.SizeNotifierFrameLayout, android.view.ViewGroup, android.view.View
            public void onDetachedFromWindow() {
                super.onDetachedFromWindow();
                Bulletin.removeDelegate(this);
            }
        };
        this.frameLayout = sizeNotifierFrameLayout;
        sizeNotifierFrameLayout.setDelegate(new SizeNotifierFrameLayout.SizeNotifierFrameLayoutDelegate() { // from class: org.telegram.ui.bots.BotWebViewSheet$$ExternalSyntheticLambda26
            @Override // org.telegram.ui.Components.SizeNotifierFrameLayout.SizeNotifierFrameLayoutDelegate
            public final void onSizeChanged(int i2, boolean z) {
                BotWebViewSheet.this.lambda$new$6(i2, z);
            }
        });
        this.frameLayout.addView(this.swipeContainer, LayoutHelper.createFrame(-1, -1.0f, 49, 0.0f, 24.0f, 0.0f, 0.0f));
        TextView textView = new TextView(this, context) { // from class: org.telegram.ui.bots.BotWebViewSheet.5
            @Override // android.widget.TextView, android.view.View
            protected void onMeasure(int i2, int i3) {
                if (AndroidUtilities.isTablet() && !AndroidUtilities.isInMultiwindow && !AndroidUtilities.isSmallTablet()) {
                    Point point = AndroidUtilities.displaySize;
                    i2 = View.MeasureSpec.makeMeasureSpec((int) (Math.min(point.x, point.y) * 0.8f), 1073741824);
                }
                super.onMeasure(i2, i3);
            }
        };
        this.mainButton = textView;
        textView.setVisibility(8);
        this.mainButton.setAlpha(0.0f);
        this.mainButton.setSingleLine();
        this.mainButton.setGravity(17);
        this.mainButton.setTypeface(AndroidUtilities.getTypeface(AndroidUtilities.TYPEFACE_ROBOTO_MEDIUM));
        int dp = AndroidUtilities.dp(16.0f);
        this.mainButton.setPadding(dp, 0, dp, 0);
        this.mainButton.setTextSize(1, 14.0f);
        this.mainButton.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.bots.BotWebViewSheet$$ExternalSyntheticLambda4
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                BotWebViewSheet.this.lambda$new$7(view);
            }
        });
        this.frameLayout.addView(this.mainButton, LayoutHelper.createFrame(-1, 48, 81));
        this.mainButtonAutoAnimator = VerticalPositionAutoAnimator.attach(this.mainButton);
        RadialProgressView radialProgressView = new RadialProgressView(this, context) { // from class: org.telegram.ui.bots.BotWebViewSheet.6
            @Override // android.view.View
            protected void onMeasure(int i2, int i3) {
                super.onMeasure(i2, i3);
                ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) getLayoutParams();
                if (AndroidUtilities.isTablet() && !AndroidUtilities.isInMultiwindow && !AndroidUtilities.isSmallTablet()) {
                    Point point = AndroidUtilities.displaySize;
                    marginLayoutParams.rightMargin = (int) (AndroidUtilities.dp(10.0f) + (Math.min(point.x, point.y) * 0.1f));
                    return;
                }
                marginLayoutParams.rightMargin = AndroidUtilities.dp(10.0f);
            }
        };
        this.radialProgressView = radialProgressView;
        radialProgressView.setSize(AndroidUtilities.dp(18.0f));
        this.radialProgressView.setAlpha(0.0f);
        this.radialProgressView.setScaleX(0.1f);
        this.radialProgressView.setScaleY(0.1f);
        this.radialProgressView.setVisibility(8);
        this.frameLayout.addView(this.radialProgressView, LayoutHelper.createFrame(28, 28.0f, 85, 0.0f, 0.0f, 10.0f, 10.0f));
        this.radialProgressAutoAnimator = VerticalPositionAutoAnimator.attach(this.radialProgressView);
        this.actionBarShadow = ContextCompat.getDrawable(getContext(), R.drawable.header_shadow).mutate();
        ActionBar actionBar = new ActionBar(this, context, resourcesProvider) { // from class: org.telegram.ui.bots.BotWebViewSheet.7
            /* JADX INFO: Access modifiers changed from: protected */
            @Override // org.telegram.ui.ActionBar.ActionBar, android.widget.FrameLayout, android.view.View
            public void onMeasure(int i2, int i3) {
                if (AndroidUtilities.isTablet() && !AndroidUtilities.isInMultiwindow && !AndroidUtilities.isSmallTablet()) {
                    Point point = AndroidUtilities.displaySize;
                    i2 = View.MeasureSpec.makeMeasureSpec((int) (Math.min(point.x, point.y) * 0.8f), 1073741824);
                }
                super.onMeasure(i2, i3);
            }
        };
        this.actionBar = actionBar;
        actionBar.setBackgroundColor(0);
        this.actionBar.setBackButtonImage(R.drawable.ic_close_white);
        updateActionBarColors();
        this.actionBar.setActionBarMenuOnItemClick(new ActionBar.ActionBarMenuOnItemClick() { // from class: org.telegram.ui.bots.BotWebViewSheet.8
            @Override // org.telegram.ui.ActionBar.ActionBar.ActionBarMenuOnItemClick
            public void onItemClick(int i2) {
                if (i2 == -1) {
                    BotWebViewSheet.this.onCheckDismissByUser();
                }
            }
        });
        this.actionBar.setAlpha(0.0f);
        this.frameLayout.addView(this.actionBar, LayoutHelper.createFrame(-1, -2, 49));
        SizeNotifierFrameLayout sizeNotifierFrameLayout2 = this.frameLayout;
        ChatAttachAlertBotWebViewLayout.WebProgressView webProgressView = new ChatAttachAlertBotWebViewLayout.WebProgressView(this, context, resourcesProvider) { // from class: org.telegram.ui.bots.BotWebViewSheet.9
            @Override // android.view.View
            protected void onMeasure(int i2, int i3) {
                if (AndroidUtilities.isTablet() && !AndroidUtilities.isInMultiwindow && !AndroidUtilities.isSmallTablet()) {
                    Point point = AndroidUtilities.displaySize;
                    i2 = View.MeasureSpec.makeMeasureSpec((int) (Math.min(point.x, point.y) * 0.8f), 1073741824);
                }
                super.onMeasure(i2, i3);
            }
        };
        this.progressView = webProgressView;
        sizeNotifierFrameLayout2.addView(webProgressView, LayoutHelper.createFrame(-1, -2.0f, 81, 0.0f, 0.0f, 0.0f, 0.0f));
        this.webViewContainer.setWebViewProgressListener(new Consumer() { // from class: org.telegram.ui.bots.BotWebViewSheet$$ExternalSyntheticLambda5
            @Override // androidx.core.util.Consumer
            public final void accept(Object obj) {
                BotWebViewSheet.this.lambda$new$9((Float) obj);
            }
        });
        this.swipeContainer.addView(this.webViewContainer, LayoutHelper.createFrame(-1, -1.0f));
        this.swipeContainer.setScrollListener(new Runnable() { // from class: org.telegram.ui.bots.BotWebViewSheet$$ExternalSyntheticLambda8
            @Override // java.lang.Runnable
            public final void run() {
                BotWebViewSheet.this.lambda$new$10();
            }
        });
        this.swipeContainer.setScrollEndListener(new Runnable() { // from class: org.telegram.ui.bots.BotWebViewSheet$$ExternalSyntheticLambda7
            @Override // java.lang.Runnable
            public final void run() {
                BotWebViewSheet.this.lambda$new$11();
            }
        });
        this.swipeContainer.setDelegate(new ChatAttachAlertBotWebViewLayout.WebViewSwipeContainer.Delegate() { // from class: org.telegram.ui.bots.BotWebViewSheet$$ExternalSyntheticLambda27
            @Override // org.telegram.ui.bots.ChatAttachAlertBotWebViewLayout.WebViewSwipeContainer.Delegate
            public final void onDismiss() {
                BotWebViewSheet.this.lambda$new$12();
            }
        });
        this.swipeContainer.setTopActionBarOffsetY((ActionBar.getCurrentActionBarHeight() + AndroidUtilities.statusBarHeight) - AndroidUtilities.dp(24.0f));
        this.swipeContainer.setIsKeyboardVisible(new GenericProvider() { // from class: org.telegram.ui.bots.BotWebViewSheet$$ExternalSyntheticLambda17
            @Override // org.telegram.messenger.GenericProvider
            public final Object provide(Object obj) {
                Boolean lambda$new$13;
                lambda$new$13 = BotWebViewSheet.this.lambda$new$13((Void) obj);
                return lambda$new$13;
            }
        });
        PasscodeView passcodeView = new PasscodeView(context);
        this.passcodeView = passcodeView;
        this.frameLayout.addView(passcodeView, LayoutHelper.createFrame(-1, -1.0f));
        setContentView(this.frameLayout, new ViewGroup.LayoutParams(-1, -1));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes4.dex */
    public class 3 implements BotWebViewContainer.Delegate {
        private boolean sentWebViewData;
        final /* synthetic */ Context val$context;
        final /* synthetic */ Theme.ResourcesProvider val$resourcesProvider;

        @Override // org.telegram.ui.bots.BotWebViewContainer.Delegate
        public /* synthetic */ void onWebAppReady() {
            BotWebViewContainer.Delegate.-CC.$default$onWebAppReady(this);
        }

        3(Theme.ResourcesProvider resourcesProvider, Context context) {
            this.val$resourcesProvider = resourcesProvider;
            this.val$context = context;
        }

        @Override // org.telegram.ui.bots.BotWebViewContainer.Delegate
        public void onCloseRequested(Runnable runnable) {
            BotWebViewSheet.this.dismiss(runnable);
        }

        @Override // org.telegram.ui.bots.BotWebViewContainer.Delegate
        public void onWebAppSetupClosingBehavior(boolean z) {
            BotWebViewSheet.this.needCloseConfirmation = z;
        }

        @Override // org.telegram.ui.bots.BotWebViewContainer.Delegate
        public void onSendWebViewData(String str) {
            if (BotWebViewSheet.this.queryId != 0 || this.sentWebViewData) {
                return;
            }
            this.sentWebViewData = true;
            TLRPC$TL_messages_sendWebViewData tLRPC$TL_messages_sendWebViewData = new TLRPC$TL_messages_sendWebViewData();
            tLRPC$TL_messages_sendWebViewData.bot = MessagesController.getInstance(BotWebViewSheet.this.currentAccount).getInputUser(BotWebViewSheet.this.botId);
            tLRPC$TL_messages_sendWebViewData.random_id = Utilities.random.nextLong();
            tLRPC$TL_messages_sendWebViewData.button_text = BotWebViewSheet.this.buttonText;
            tLRPC$TL_messages_sendWebViewData.data = str;
            ConnectionsManager.getInstance(BotWebViewSheet.this.currentAccount).sendRequest(tLRPC$TL_messages_sendWebViewData, new RequestDelegate() { // from class: org.telegram.ui.bots.BotWebViewSheet$3$$ExternalSyntheticLambda3
                @Override // org.telegram.tgnet.RequestDelegate
                public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
                    BotWebViewSheet.3.this.lambda$onSendWebViewData$0(tLObject, tLRPC$TL_error);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onSendWebViewData$0(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
            if (tLObject instanceof TLRPC$TL_updates) {
                MessagesController.getInstance(BotWebViewSheet.this.currentAccount).processUpdates((TLRPC$TL_updates) tLObject, false);
            }
            final BotWebViewSheet botWebViewSheet = BotWebViewSheet.this;
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.bots.BotWebViewSheet$3$$ExternalSyntheticLambda2
                @Override // java.lang.Runnable
                public final void run() {
                    BotWebViewSheet.this.dismiss();
                }
            });
        }

        @Override // org.telegram.ui.bots.BotWebViewContainer.Delegate
        public void onWebAppSetActionBarColor(final int i, boolean z) {
            final int i2 = BotWebViewSheet.this.actionBarColor;
            final BotWebViewMenuContainer.ActionBarColorsAnimating actionBarColorsAnimating = new BotWebViewMenuContainer.ActionBarColorsAnimating();
            actionBarColorsAnimating.setFrom(BotWebViewSheet.this.overrideBackgroundColor ? BotWebViewSheet.this.actionBarColor : 0, this.val$resourcesProvider);
            BotWebViewSheet.this.overrideBackgroundColor = z;
            BotWebViewSheet.this.actionBarIsLight = ColorUtils.calculateLuminance(i) < 0.5d;
            actionBarColorsAnimating.setTo(BotWebViewSheet.this.overrideBackgroundColor ? i : 0, this.val$resourcesProvider);
            ValueAnimator duration = ValueAnimator.ofFloat(0.0f, 1.0f).setDuration(200L);
            duration.setInterpolator(CubicBezierInterpolator.DEFAULT);
            duration.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: org.telegram.ui.bots.BotWebViewSheet$3$$ExternalSyntheticLambda1
                @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                    BotWebViewSheet.3.this.lambda$onWebAppSetActionBarColor$1(i2, i, actionBarColorsAnimating, valueAnimator);
                }
            });
            duration.start();
            BotWebViewSheet.this.updateLightStatusBar();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onWebAppSetActionBarColor$1(int i, int i2, BotWebViewMenuContainer.ActionBarColorsAnimating actionBarColorsAnimating, ValueAnimator valueAnimator) {
            float floatValue = ((Float) valueAnimator.getAnimatedValue()).floatValue();
            BotWebViewSheet.this.actionBarColor = ColorUtils.blendARGB(i, i2, floatValue);
            BotWebViewSheet.this.actionBar.setBackgroundColor(BotWebViewSheet.this.actionBarColor);
            actionBarColorsAnimating.updateActionBar(BotWebViewSheet.this.actionBar, floatValue);
            BotWebViewSheet.this.lineColor = actionBarColorsAnimating.getColor(Theme.key_sheet_scrollUp);
            BotWebViewSheet.this.frameLayout.invalidate();
        }

        @Override // org.telegram.ui.bots.BotWebViewContainer.Delegate
        public void onWebAppSetBackgroundColor(final int i) {
            final int color = BotWebViewSheet.this.backgroundPaint.getColor();
            ValueAnimator duration = ValueAnimator.ofFloat(0.0f, 1.0f).setDuration(200L);
            duration.setInterpolator(CubicBezierInterpolator.DEFAULT);
            duration.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: org.telegram.ui.bots.BotWebViewSheet$3$$ExternalSyntheticLambda0
                @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                    BotWebViewSheet.3.this.lambda$onWebAppSetBackgroundColor$2(color, i, valueAnimator);
                }
            });
            duration.start();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onWebAppSetBackgroundColor$2(int i, int i2, ValueAnimator valueAnimator) {
            BotWebViewSheet.this.backgroundPaint.setColor(ColorUtils.blendARGB(i, i2, ((Float) valueAnimator.getAnimatedValue()).floatValue()));
            BotWebViewSheet.this.updateActionBarColors();
            BotWebViewSheet.this.frameLayout.invalidate();
        }

        @Override // org.telegram.ui.bots.BotWebViewContainer.Delegate
        public void onSetBackButtonVisible(boolean z) {
            AndroidUtilities.updateImageViewImageAnimated(BotWebViewSheet.this.actionBar.getBackButton(), z ? R.drawable.ic_ab_back : R.drawable.ic_close_white);
        }

        @Override // org.telegram.ui.bots.BotWebViewContainer.Delegate
        public void onSetSettingsButtonVisible(boolean z) {
            if (BotWebViewSheet.this.settingsItem != null) {
                BotWebViewSheet.this.settingsItem.setVisibility(z ? 0 : 8);
            }
        }

        @Override // org.telegram.ui.bots.BotWebViewContainer.Delegate
        public void onWebAppOpenInvoice(final String str, TLObject tLObject) {
            PaymentFormActivity paymentFormActivity;
            BaseFragment lastFragment = ((LaunchActivity) BotWebViewSheet.this.parentActivity).getActionBarLayout().getLastFragment();
            if (tLObject instanceof TLRPC$TL_payments_paymentForm) {
                TLRPC$TL_payments_paymentForm tLRPC$TL_payments_paymentForm = (TLRPC$TL_payments_paymentForm) tLObject;
                MessagesController.getInstance(BotWebViewSheet.this.currentAccount).putUsers(tLRPC$TL_payments_paymentForm.users, false);
                paymentFormActivity = new PaymentFormActivity(tLRPC$TL_payments_paymentForm, str, lastFragment);
            } else {
                paymentFormActivity = tLObject instanceof TLRPC$TL_payments_paymentReceipt ? new PaymentFormActivity((TLRPC$TL_payments_paymentReceipt) tLObject) : null;
            }
            if (paymentFormActivity != null) {
                BotWebViewSheet.this.swipeContainer.stickTo((-BotWebViewSheet.this.swipeContainer.getOffsetY()) + BotWebViewSheet.this.swipeContainer.getTopActionBarOffsetY());
                AndroidUtilities.hideKeyboard(BotWebViewSheet.this.frameLayout);
                final OverlayActionBarLayoutDialog overlayActionBarLayoutDialog = new OverlayActionBarLayoutDialog(this.val$context, this.val$resourcesProvider);
                overlayActionBarLayoutDialog.show();
                paymentFormActivity.setPaymentFormCallback(new PaymentFormActivity.PaymentFormCallback() { // from class: org.telegram.ui.bots.BotWebViewSheet$3$$ExternalSyntheticLambda5
                    @Override // org.telegram.ui.PaymentFormActivity.PaymentFormCallback
                    public final void onInvoiceStatusChanged(PaymentFormActivity.InvoiceStatus invoiceStatus) {
                        BotWebViewSheet.3.this.lambda$onWebAppOpenInvoice$3(overlayActionBarLayoutDialog, str, invoiceStatus);
                    }
                });
                paymentFormActivity.setResourcesProvider(this.val$resourcesProvider);
                overlayActionBarLayoutDialog.addFragment(paymentFormActivity);
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onWebAppOpenInvoice$3(OverlayActionBarLayoutDialog overlayActionBarLayoutDialog, String str, PaymentFormActivity.InvoiceStatus invoiceStatus) {
            if (invoiceStatus != PaymentFormActivity.InvoiceStatus.PENDING) {
                overlayActionBarLayoutDialog.dismiss();
            }
            BotWebViewSheet.this.webViewContainer.onInvoiceStatusUpdate(str, invoiceStatus.name().toLowerCase(Locale.ROOT));
        }

        @Override // org.telegram.ui.bots.BotWebViewContainer.Delegate
        public void onWebAppExpand() {
            if (BotWebViewSheet.this.swipeContainer.isSwipeInProgress()) {
                return;
            }
            BotWebViewSheet.this.swipeContainer.stickTo((-BotWebViewSheet.this.swipeContainer.getOffsetY()) + BotWebViewSheet.this.swipeContainer.getTopActionBarOffsetY());
        }

        @Override // org.telegram.ui.bots.BotWebViewContainer.Delegate
        public void onWebAppSwitchInlineQuery(final TLRPC$User tLRPC$User, final String str, List<String> list) {
            if (list.isEmpty()) {
                if (BotWebViewSheet.this.parentActivity instanceof LaunchActivity) {
                    BaseFragment lastFragment = ((LaunchActivity) BotWebViewSheet.this.parentActivity).getActionBarLayout().getLastFragment();
                    if (lastFragment instanceof ChatActivity) {
                        ChatActivityEnterView chatActivityEnterView = ((ChatActivity) lastFragment).getChatActivityEnterView();
                        chatActivityEnterView.setFieldText("@" + UserObject.getPublicUsername(tLRPC$User) + " " + str);
                        BotWebViewSheet.this.dismiss();
                        return;
                    }
                    return;
                }
                return;
            }
            Bundle bundle = new Bundle();
            bundle.putInt("dialogsType", 14);
            bundle.putBoolean("onlySelect", true);
            bundle.putBoolean("allowGroups", list.contains("groups"));
            bundle.putBoolean("allowMegagroups", list.contains("groups"));
            bundle.putBoolean("allowLegacyGroups", list.contains("groups"));
            bundle.putBoolean("allowUsers", list.contains("users"));
            bundle.putBoolean("allowChannels", list.contains("channels"));
            bundle.putBoolean("allowBots", list.contains("bots"));
            DialogsActivity dialogsActivity = new DialogsActivity(bundle);
            AndroidUtilities.hideKeyboard(BotWebViewSheet.this.frameLayout);
            final OverlayActionBarLayoutDialog overlayActionBarLayoutDialog = new OverlayActionBarLayoutDialog(this.val$context, this.val$resourcesProvider);
            dialogsActivity.setDelegate(new DialogsActivity.DialogsActivityDelegate() { // from class: org.telegram.ui.bots.BotWebViewSheet$3$$ExternalSyntheticLambda4
                @Override // org.telegram.ui.DialogsActivity.DialogsActivityDelegate
                public final boolean didSelectDialogs(DialogsActivity dialogsActivity2, ArrayList arrayList, CharSequence charSequence, boolean z, TopicsFragment topicsFragment) {
                    boolean lambda$onWebAppSwitchInlineQuery$4;
                    lambda$onWebAppSwitchInlineQuery$4 = BotWebViewSheet.3.this.lambda$onWebAppSwitchInlineQuery$4(tLRPC$User, str, overlayActionBarLayoutDialog, dialogsActivity2, arrayList, charSequence, z, topicsFragment);
                    return lambda$onWebAppSwitchInlineQuery$4;
                }
            });
            overlayActionBarLayoutDialog.show();
            overlayActionBarLayoutDialog.addFragment(dialogsActivity);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ boolean lambda$onWebAppSwitchInlineQuery$4(TLRPC$User tLRPC$User, String str, OverlayActionBarLayoutDialog overlayActionBarLayoutDialog, DialogsActivity dialogsActivity, ArrayList arrayList, CharSequence charSequence, boolean z, TopicsFragment topicsFragment) {
            long j = ((MessagesStorage.TopicKey) arrayList.get(0)).dialogId;
            Bundle bundle = new Bundle();
            bundle.putBoolean("scrollToTopOnResume", true);
            if (DialogObject.isEncryptedDialog(j)) {
                bundle.putInt("enc_id", DialogObject.getEncryptedChatId(j));
            } else if (DialogObject.isUserDialog(j)) {
                bundle.putLong("user_id", j);
            } else {
                bundle.putLong("chat_id", -j);
            }
            bundle.putString("start_text", "@" + UserObject.getPublicUsername(tLRPC$User) + " " + str);
            if (BotWebViewSheet.this.parentActivity instanceof LaunchActivity) {
                BaseFragment lastFragment = ((LaunchActivity) BotWebViewSheet.this.parentActivity).getActionBarLayout().getLastFragment();
                if (MessagesController.getInstance(BotWebViewSheet.this.currentAccount).checkCanOpenChat(bundle, lastFragment)) {
                    overlayActionBarLayoutDialog.dismiss();
                    BotWebViewSheet.this.dismissed = true;
                    AndroidUtilities.cancelRunOnUIThread(BotWebViewSheet.this.pollRunnable);
                    BotWebViewSheet.this.webViewContainer.destroyWebView();
                    NotificationCenter.getInstance(BotWebViewSheet.this.currentAccount).removeObserver(BotWebViewSheet.this, NotificationCenter.webViewResultSent);
                    NotificationCenter.getGlobalInstance().removeObserver(BotWebViewSheet.this, NotificationCenter.didSetNewTheme);
                    BotWebViewSheet.super.dismiss();
                    lastFragment.presentFragment(new INavigationLayout.NavigationParams(new ChatActivity(bundle)).setRemoveLast(true));
                }
            }
            return true;
        }

        @Override // org.telegram.ui.bots.BotWebViewContainer.Delegate
        public void onSetupMainButton(final boolean z, boolean z2, String str, int i, int i2, final boolean z3) {
            BotWebViewSheet.this.mainButton.setClickable(z2);
            BotWebViewSheet.this.mainButton.setText(str);
            BotWebViewSheet.this.mainButton.setTextColor(i2);
            BotWebViewSheet.this.mainButton.setBackground(BotWebViewContainer.getMainButtonRippleDrawable(i));
            if (z != BotWebViewSheet.this.mainButtonWasVisible) {
                BotWebViewSheet.this.mainButtonWasVisible = z;
                BotWebViewSheet.this.mainButton.animate().cancel();
                if (z) {
                    BotWebViewSheet.this.mainButton.setAlpha(0.0f);
                    BotWebViewSheet.this.mainButton.setVisibility(0);
                }
                BotWebViewSheet.this.mainButton.animate().alpha(z ? 1.0f : 0.0f).setDuration(150L).setListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.bots.BotWebViewSheet.3.1
                    @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                    public void onAnimationEnd(Animator animator) {
                        if (!z) {
                            BotWebViewSheet.this.mainButton.setVisibility(8);
                        }
                        BotWebViewSheet.this.swipeContainer.requestLayout();
                    }
                }).start();
            }
            BotWebViewSheet.this.radialProgressView.setProgressColor(i2);
            if (z3 != BotWebViewSheet.this.mainButtonProgressWasVisible) {
                BotWebViewSheet.this.mainButtonProgressWasVisible = z3;
                BotWebViewSheet.this.radialProgressView.animate().cancel();
                if (z3) {
                    BotWebViewSheet.this.radialProgressView.setAlpha(0.0f);
                    BotWebViewSheet.this.radialProgressView.setVisibility(0);
                }
                BotWebViewSheet.this.radialProgressView.animate().alpha(z3 ? 1.0f : 0.0f).scaleX(z3 ? 1.0f : 0.1f).scaleY(z3 ? 1.0f : 0.1f).setDuration(250L).setListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.bots.BotWebViewSheet.3.2
                    @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                    public void onAnimationEnd(Animator animator) {
                        if (z3) {
                            return;
                        }
                        BotWebViewSheet.this.radialProgressView.setVisibility(8);
                    }
                }).start();
            }
        }

        @Override // org.telegram.ui.bots.BotWebViewContainer.Delegate
        public boolean isClipboardAvailable() {
            return MediaDataController.getInstance(BotWebViewSheet.this.currentAccount).botInAttachMenu(BotWebViewSheet.this.botId);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$6(int i, boolean z) {
        if (i > AndroidUtilities.dp(20.0f)) {
            ChatAttachAlertBotWebViewLayout.WebViewSwipeContainer webViewSwipeContainer = this.swipeContainer;
            webViewSwipeContainer.stickTo((-webViewSwipeContainer.getOffsetY()) + this.swipeContainer.getTopActionBarOffsetY());
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$7(View view) {
        this.webViewContainer.onMainButtonPressed();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$9(Float f) {
        this.progressView.setLoadProgressAnimated(f.floatValue());
        if (f.floatValue() == 1.0f) {
            ValueAnimator duration = ValueAnimator.ofFloat(1.0f, 0.0f).setDuration(200L);
            duration.setInterpolator(CubicBezierInterpolator.DEFAULT);
            duration.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: org.telegram.ui.bots.BotWebViewSheet$$ExternalSyntheticLambda0
                @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                    BotWebViewSheet.this.lambda$new$8(valueAnimator);
                }
            });
            duration.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.bots.BotWebViewSheet.10
                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public void onAnimationEnd(Animator animator) {
                    BotWebViewSheet.this.progressView.setVisibility(8);
                }
            });
            duration.start();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$8(ValueAnimator valueAnimator) {
        this.progressView.setAlpha(((Float) valueAnimator.getAnimatedValue()).floatValue());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$10() {
        if (this.swipeContainer.getSwipeOffsetY() > 0.0f) {
            this.dimPaint.setAlpha((int) ((1.0f - MathUtils.clamp(this.swipeContainer.getSwipeOffsetY() / this.swipeContainer.getHeight(), 0.0f, 1.0f)) * 64.0f));
        } else {
            this.dimPaint.setAlpha(64);
        }
        this.frameLayout.invalidate();
        this.webViewContainer.invalidateViewPortHeight();
        if (this.springAnimation != null) {
            float f = (1.0f - (Math.min(this.swipeContainer.getTopActionBarOffsetY(), this.swipeContainer.getTranslationY() - this.swipeContainer.getTopActionBarOffsetY()) / this.swipeContainer.getTopActionBarOffsetY()) > 0.5f ? 1 : 0) * 100.0f;
            if (this.springAnimation.getSpring().getFinalPosition() != f) {
                this.springAnimation.getSpring().setFinalPosition(f);
                this.springAnimation.start();
            }
        }
        float max = Math.max(0.0f, this.swipeContainer.getSwipeOffsetY());
        this.mainButtonAutoAnimator.setOffsetY(max);
        this.radialProgressAutoAnimator.setOffsetY(max);
        System.currentTimeMillis();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$11() {
        this.webViewContainer.invalidateViewPortHeight(true);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$12() {
        if (onCheckDismissByUser()) {
            return;
        }
        this.swipeContainer.stickTo(0.0f);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ Boolean lambda$new$13(Void r2) {
        return Boolean.valueOf(this.frameLayout.getKeyboardHeight() >= AndroidUtilities.dp(20.0f));
    }

    @Override // android.app.Dialog
    protected void onStart() {
        super.onStart();
        Context context = getContext();
        if ((context instanceof ContextWrapper) && !(context instanceof LaunchActivity)) {
            context = ((ContextWrapper) context).getBaseContext();
        }
        if (context instanceof LaunchActivity) {
            ((LaunchActivity) context).addOverlayPasscodeView(this.passcodeView);
        }
    }

    @Override // android.app.Dialog
    protected void onStop() {
        super.onStop();
        Context context = getContext();
        if ((context instanceof ContextWrapper) && !(context instanceof LaunchActivity)) {
            context = ((ContextWrapper) context).getBaseContext();
        }
        if (context instanceof LaunchActivity) {
            ((LaunchActivity) context).removeOverlayPasscodeView(this.passcodeView);
        }
    }

    public void setParentActivity(Activity activity) {
        this.parentActivity = activity;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateActionBarColors() {
        if (this.overrideBackgroundColor) {
            return;
        }
        ActionBar actionBar = this.actionBar;
        int i = Theme.key_windowBackgroundWhiteBlackText;
        actionBar.setTitleColor(getColor(i));
        this.actionBar.setItemsColor(getColor(i), false);
        this.actionBar.setItemsBackgroundColor(getColor(Theme.key_actionBarWhiteSelector), false);
        this.actionBar.setPopupBackgroundColor(getColor(Theme.key_actionBarDefaultSubmenuBackground), false);
        this.actionBar.setPopupItemsColor(getColor(Theme.key_actionBarDefaultSubmenuItem), false, false);
        this.actionBar.setPopupItemsColor(getColor(Theme.key_actionBarDefaultSubmenuItemIcon), true, false);
        this.actionBar.setPopupItemsSelectorColor(getColor(Theme.key_dialogButtonSelector), false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateLightStatusBar() {
        boolean z;
        boolean z2 = true;
        if (this.overrideBackgroundColor) {
            z = !this.actionBarIsLight;
        } else {
            z = (AndroidUtilities.isTablet() || ColorUtils.calculateLuminance(Theme.getColor(Theme.key_windowBackgroundWhite, null, true)) < 0.9d || this.actionBarTransitionProgress < 0.85f) ? false : false;
        }
        Boolean bool = this.wasLightStatusBar;
        if (bool == null || bool.booleanValue() != z) {
            this.wasLightStatusBar = Boolean.valueOf(z);
            if (Build.VERSION.SDK_INT >= 23) {
                int systemUiVisibility = this.frameLayout.getSystemUiVisibility();
                this.frameLayout.setSystemUiVisibility(z ? systemUiVisibility | LiteMode.FLAG_ANIMATED_EMOJI_REACTIONS_NOT_PREMIUM : systemUiVisibility & (-8193));
            }
        }
    }

    @Override // android.app.Dialog
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        Window window = getWindow();
        int i = Build.VERSION.SDK_INT;
        if (i >= 30) {
            window.addFlags(-2147483392);
        } else if (i >= 21) {
            window.addFlags(-2147417856);
        }
        window.setWindowAnimations(R.style.DialogNoAnimation);
        WindowManager.LayoutParams attributes = window.getAttributes();
        attributes.width = -1;
        attributes.gravity = 51;
        attributes.dimAmount = 0.0f;
        attributes.flags &= -3;
        attributes.softInputMode = 16;
        attributes.height = -1;
        if (i >= 28) {
            attributes.layoutInDisplayCutoutMode = 1;
        }
        window.setAttributes(attributes);
        if (i >= 23) {
            window.setStatusBarColor(0);
        }
        this.frameLayout.setSystemUiVisibility(1280);
        if (i >= 21) {
            this.frameLayout.setOnApplyWindowInsetsListener(new View.OnApplyWindowInsetsListener() { // from class: org.telegram.ui.bots.BotWebViewSheet$$ExternalSyntheticLambda3
                @Override // android.view.View.OnApplyWindowInsetsListener
                public final WindowInsets onApplyWindowInsets(View view, WindowInsets windowInsets) {
                    WindowInsets lambda$onCreate$14;
                    lambda$onCreate$14 = BotWebViewSheet.lambda$onCreate$14(view, windowInsets);
                    return lambda$onCreate$14;
                }
            });
        }
        if (i >= 26) {
            AndroidUtilities.setLightNavigationBar(window, ColorUtils.calculateLuminance(Theme.getColor(Theme.key_windowBackgroundWhite, null, true)) >= 0.9d);
        }
        NotificationCenter.getGlobalInstance().addObserver(this, NotificationCenter.didSetNewTheme);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ WindowInsets lambda$onCreate$14(View view, WindowInsets windowInsets) {
        view.setPadding(0, 0, 0, windowInsets.getSystemWindowInsetBottom());
        return windowInsets;
    }

    @Override // android.app.Dialog, android.view.Window.Callback
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (this.springAnimation == null) {
            this.springAnimation = new SpringAnimation(this, ACTION_BAR_TRANSITION_PROGRESS_VALUE).setSpring(new SpringForce().setStiffness(1200.0f).setDampingRatio(1.0f));
        }
    }

    @Override // android.app.Dialog, android.view.Window.Callback
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        SpringAnimation springAnimation = this.springAnimation;
        if (springAnimation != null) {
            springAnimation.cancel();
            this.springAnimation = null;
        }
    }

    public static JSONObject makeThemeParams(Theme.ResourcesProvider resourcesProvider) {
        try {
            JSONObject jSONObject = new JSONObject();
            int color = Theme.getColor(Theme.key_dialogBackground, resourcesProvider);
            jSONObject.put("bg_color", color);
            jSONObject.put("section_bg_color", Theme.getColor(Theme.key_windowBackgroundWhite, resourcesProvider));
            jSONObject.put("secondary_bg_color", Theme.getColor(Theme.key_windowBackgroundGray, resourcesProvider));
            jSONObject.put("text_color", Theme.getColor(Theme.key_windowBackgroundWhiteBlackText, resourcesProvider));
            jSONObject.put("hint_color", Theme.getColor(Theme.key_windowBackgroundWhiteHintText, resourcesProvider));
            jSONObject.put("link_color", Theme.getColor(Theme.key_windowBackgroundWhiteLinkText, resourcesProvider));
            jSONObject.put("button_color", Theme.getColor(Theme.key_featuredStickers_addButton, resourcesProvider));
            jSONObject.put("button_text_color", Theme.getColor(Theme.key_featuredStickers_buttonText, resourcesProvider));
            jSONObject.put("header_bg_color", Theme.getColor(Theme.key_actionBarDefault, resourcesProvider));
            jSONObject.put("accent_text_color", Theme.blendOver(color, Theme.getColor(Theme.key_windowBackgroundWhiteBlueText4, resourcesProvider)));
            jSONObject.put("section_header_text_color", Theme.blendOver(color, Theme.getColor(Theme.key_windowBackgroundWhiteBlueHeader, resourcesProvider)));
            jSONObject.put("subtitle_text_color", Theme.blendOver(color, Theme.getColor(Theme.key_windowBackgroundWhiteGrayText2, resourcesProvider)));
            jSONObject.put("destructive_text_color", Theme.blendOver(color, Theme.getColor(Theme.key_text_RedRegular, resourcesProvider)));
            return jSONObject;
        } catch (Exception e) {
            FileLog.e(e);
            return null;
        }
    }

    public void requestWebView(int i, long j, long j2, String str, String str2, int i2, int i3, boolean z, int i4) {
        requestWebView(i, j, j2, str, str2, i2, i3, z, null, null, false, null, null, i4);
    }

    public void requestWebView(int i, long j, long j2, String str, String str2, int i2, int i3, boolean z) {
        requestWebView(i, j, j2, str, str2, i2, i3, z, null, null, false, null, null, 0);
    }

    public void requestWebView(int i, long j, long j2, String str, String str2, int i2, int i3, boolean z, BaseFragment baseFragment, TLRPC$BotApp tLRPC$BotApp, boolean z2, String str3, TLRPC$User tLRPC$User) {
        requestWebView(i, j, j2, str, str2, i2, i3, z, baseFragment, tLRPC$BotApp, z2, str3, tLRPC$User, 0);
    }

    public void requestWebView(final int i, long j, long j2, String str, String str2, int i2, int i3, boolean z, BaseFragment baseFragment, TLRPC$BotApp tLRPC$BotApp, boolean z2, String str3, TLRPC$User tLRPC$User, int i4) {
        TLRPC$TL_attachMenuBot tLRPC$TL_attachMenuBot;
        TLRPC$InputPeer inputPeer;
        this.currentAccount = i;
        this.peerId = j;
        this.botId = j2;
        this.replyToMsgId = i3;
        this.silent = z;
        this.buttonText = str;
        CharSequence userName = UserObject.getUserName(MessagesController.getInstance(i).getUser(Long.valueOf(j2)));
        try {
            TextPaint textPaint = new TextPaint();
            textPaint.setTextSize(AndroidUtilities.dp(20.0f));
            userName = Emoji.replaceEmoji(userName, textPaint.getFontMetricsInt(), false);
        } catch (Exception unused) {
        }
        this.actionBar.setTitle(userName);
        ActionBarMenu createMenu = this.actionBar.createMenu();
        createMenu.removeAllViews();
        Iterator<TLRPC$TL_attachMenuBot> it = MediaDataController.getInstance(i).getAttachMenuBots().bots.iterator();
        while (true) {
            if (!it.hasNext()) {
                tLRPC$TL_attachMenuBot = null;
                break;
            }
            tLRPC$TL_attachMenuBot = it.next();
            if (tLRPC$TL_attachMenuBot.bot_id == j2) {
                break;
            }
        }
        ActionBarMenuItem addItem = createMenu.addItem(0, R.drawable.ic_ab_other);
        addItem.addSubItem(R.id.menu_open_bot, R.drawable.msg_bot, LocaleController.getString(R.string.BotWebViewOpenBot));
        ActionBarMenuSubItem addSubItem = addItem.addSubItem(R.id.menu_settings, R.drawable.msg_settings, LocaleController.getString(R.string.BotWebViewSettings));
        this.settingsItem = addSubItem;
        addSubItem.setVisibility(8);
        addItem.addSubItem(R.id.menu_reload_page, R.drawable.msg_retry, LocaleController.getString(R.string.BotWebViewReloadPage));
        if (tLRPC$TL_attachMenuBot != null && MediaDataController.getInstance(i).canCreateAttachedMenuBotShortcut(tLRPC$TL_attachMenuBot.bot_id)) {
            addItem.addSubItem(R.id.menu_add_to_home_screen_bot, R.drawable.msg_home, LocaleController.getString(R.string.AddShortcut));
        }
        addItem.addSubItem(R.id.menu_tos_bot, R.drawable.menu_intro, LocaleController.getString(R.string.BotWebViewToS));
        if (tLRPC$TL_attachMenuBot != null && (tLRPC$TL_attachMenuBot.show_in_side_menu || tLRPC$TL_attachMenuBot.show_in_attach_menu)) {
            addItem.addSubItem(R.id.menu_delete_bot, R.drawable.msg_delete, LocaleController.getString(R.string.BotWebViewDeleteBot));
        }
        this.actionBar.setActionBarMenuOnItemClick(new 11(j2, i));
        JSONObject makeThemeParams = makeThemeParams(this.resourcesProvider);
        this.webViewContainer.setBotUser(MessagesController.getInstance(i).getUser(Long.valueOf(j2)));
        this.webViewContainer.loadFlickerAndSettingsItem(i, j2, this.settingsItem);
        preloadShortcutBotIcon(tLRPC$User, tLRPC$TL_attachMenuBot);
        if (i2 == 0) {
            TLRPC$TL_messages_requestWebView tLRPC$TL_messages_requestWebView = new TLRPC$TL_messages_requestWebView();
            tLRPC$TL_messages_requestWebView.peer = MessagesController.getInstance(i).getInputPeer(j);
            tLRPC$TL_messages_requestWebView.bot = MessagesController.getInstance(i).getInputUser(j2);
            tLRPC$TL_messages_requestWebView.platform = "android";
            if (str2 != null) {
                tLRPC$TL_messages_requestWebView.url = str2;
                tLRPC$TL_messages_requestWebView.flags |= 2;
            }
            if (i3 != 0) {
                tLRPC$TL_messages_requestWebView.reply_to = SendMessagesHelper.getInstance(i).createReplyInput(i3);
                tLRPC$TL_messages_requestWebView.flags |= 1;
            }
            if (makeThemeParams != null) {
                TLRPC$TL_dataJSON tLRPC$TL_dataJSON = new TLRPC$TL_dataJSON();
                tLRPC$TL_messages_requestWebView.theme_params = tLRPC$TL_dataJSON;
                tLRPC$TL_dataJSON.data = makeThemeParams.toString();
                tLRPC$TL_messages_requestWebView.flags |= 4;
            }
            ConnectionsManager.getInstance(i).sendRequest(tLRPC$TL_messages_requestWebView, new RequestDelegate() { // from class: org.telegram.ui.bots.BotWebViewSheet$$ExternalSyntheticLambda20
                @Override // org.telegram.tgnet.RequestDelegate
                public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
                    BotWebViewSheet.this.lambda$requestWebView$20(i, tLObject, tLRPC$TL_error);
                }
            });
            NotificationCenter.getInstance(i).addObserver(this, NotificationCenter.webViewResultSent);
        } else if (i2 == 1) {
            TLRPC$TL_messages_requestSimpleWebView tLRPC$TL_messages_requestSimpleWebView = new TLRPC$TL_messages_requestSimpleWebView();
            tLRPC$TL_messages_requestSimpleWebView.from_switch_webview = (i4 & 1) != 0;
            tLRPC$TL_messages_requestSimpleWebView.bot = MessagesController.getInstance(i).getInputUser(j2);
            tLRPC$TL_messages_requestSimpleWebView.platform = "android";
            tLRPC$TL_messages_requestSimpleWebView.from_side_menu = (i4 & 2) != 0;
            if (makeThemeParams != null) {
                TLRPC$TL_dataJSON tLRPC$TL_dataJSON2 = new TLRPC$TL_dataJSON();
                tLRPC$TL_messages_requestSimpleWebView.theme_params = tLRPC$TL_dataJSON2;
                tLRPC$TL_dataJSON2.data = makeThemeParams.toString();
                tLRPC$TL_messages_requestSimpleWebView.flags |= 1;
            }
            if (!TextUtils.isEmpty(str2)) {
                tLRPC$TL_messages_requestSimpleWebView.flags |= 8;
                tLRPC$TL_messages_requestSimpleWebView.url = str2;
            }
            if (!TextUtils.isEmpty(str3)) {
                tLRPC$TL_messages_requestSimpleWebView.start_param = str3;
                tLRPC$TL_messages_requestSimpleWebView.flags |= 16;
            }
            ConnectionsManager.getInstance(i).sendRequest(tLRPC$TL_messages_requestSimpleWebView, new RequestDelegate() { // from class: org.telegram.ui.bots.BotWebViewSheet$$ExternalSyntheticLambda22
                @Override // org.telegram.tgnet.RequestDelegate
                public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
                    BotWebViewSheet.this.lambda$requestWebView$18(i, tLObject, tLRPC$TL_error);
                }
            });
        } else if (i2 == 2) {
            TLRPC$TL_messages_requestWebView tLRPC$TL_messages_requestWebView2 = new TLRPC$TL_messages_requestWebView();
            tLRPC$TL_messages_requestWebView2.bot = MessagesController.getInstance(i).getInputUser(j2);
            tLRPC$TL_messages_requestWebView2.peer = MessagesController.getInstance(i).getInputPeer(j2);
            tLRPC$TL_messages_requestWebView2.platform = "android";
            tLRPC$TL_messages_requestWebView2.url = str2;
            tLRPC$TL_messages_requestWebView2.flags |= 2;
            if (makeThemeParams != null) {
                TLRPC$TL_dataJSON tLRPC$TL_dataJSON3 = new TLRPC$TL_dataJSON();
                tLRPC$TL_messages_requestWebView2.theme_params = tLRPC$TL_dataJSON3;
                tLRPC$TL_dataJSON3.data = makeThemeParams.toString();
                tLRPC$TL_messages_requestWebView2.flags |= 4;
            }
            ConnectionsManager.getInstance(i).sendRequest(tLRPC$TL_messages_requestWebView2, new RequestDelegate() { // from class: org.telegram.ui.bots.BotWebViewSheet$$ExternalSyntheticLambda23
                @Override // org.telegram.tgnet.RequestDelegate
                public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
                    BotWebViewSheet.this.lambda$requestWebView$16(i, tLObject, tLRPC$TL_error);
                }
            });
            NotificationCenter.getInstance(i).addObserver(this, NotificationCenter.webViewResultSent);
        } else if (i2 != 3) {
        } else {
            TLRPC$TL_messages_requestAppWebView tLRPC$TL_messages_requestAppWebView = new TLRPC$TL_messages_requestAppWebView();
            TLRPC$TL_inputBotAppID tLRPC$TL_inputBotAppID = new TLRPC$TL_inputBotAppID();
            tLRPC$TL_inputBotAppID.id = tLRPC$BotApp.id;
            tLRPC$TL_inputBotAppID.access_hash = tLRPC$BotApp.access_hash;
            tLRPC$TL_messages_requestAppWebView.app = tLRPC$TL_inputBotAppID;
            tLRPC$TL_messages_requestAppWebView.write_allowed = z2;
            tLRPC$TL_messages_requestAppWebView.platform = "android";
            if (baseFragment instanceof ChatActivity) {
                ChatActivity chatActivity = (ChatActivity) baseFragment;
                inputPeer = chatActivity.getCurrentUser() != null ? MessagesController.getInputPeer(chatActivity.getCurrentUser()) : MessagesController.getInputPeer(chatActivity.getCurrentChat());
            } else {
                inputPeer = MessagesController.getInputPeer(tLRPC$User);
            }
            tLRPC$TL_messages_requestAppWebView.peer = inputPeer;
            if (!TextUtils.isEmpty(str3)) {
                tLRPC$TL_messages_requestAppWebView.start_param = str3;
                tLRPC$TL_messages_requestAppWebView.flags |= 2;
            }
            if (makeThemeParams != null) {
                TLRPC$TL_dataJSON tLRPC$TL_dataJSON4 = new TLRPC$TL_dataJSON();
                tLRPC$TL_messages_requestAppWebView.theme_params = tLRPC$TL_dataJSON4;
                tLRPC$TL_dataJSON4.data = makeThemeParams.toString();
                tLRPC$TL_messages_requestAppWebView.flags |= 4;
            }
            ConnectionsManager.getInstance(i).sendRequest(tLRPC$TL_messages_requestAppWebView, new RequestDelegate() { // from class: org.telegram.ui.bots.BotWebViewSheet$$ExternalSyntheticLambda21
                @Override // org.telegram.tgnet.RequestDelegate
                public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
                    BotWebViewSheet.this.lambda$requestWebView$22(i, tLObject, tLRPC$TL_error);
                }
            }, 66);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes4.dex */
    public class 11 extends ActionBar.ActionBarMenuOnItemClick {
        final /* synthetic */ long val$botId;
        final /* synthetic */ int val$currentAccount;

        11(long j, int i) {
            this.val$botId = j;
            this.val$currentAccount = i;
        }

        @Override // org.telegram.ui.ActionBar.ActionBar.ActionBarMenuOnItemClick
        public void onItemClick(int i) {
            if (i == -1) {
                if (BotWebViewSheet.this.webViewContainer.onBackPressed()) {
                    return;
                }
                BotWebViewSheet.this.onCheckDismissByUser();
            } else if (i == R.id.menu_open_bot) {
                Bundle bundle = new Bundle();
                bundle.putLong("user_id", this.val$botId);
                if (BotWebViewSheet.this.parentActivity instanceof LaunchActivity) {
                    ((LaunchActivity) BotWebViewSheet.this.parentActivity).lambda$runLinkRequest$86(new ChatActivity(bundle));
                }
                BotWebViewSheet.this.dismiss();
            } else if (i == R.id.menu_tos_bot) {
                Browser.openUrl(BotWebViewSheet.this.getContext(), LocaleController.getString(R.string.BotWebViewToSLink));
            } else if (i == R.id.menu_reload_page) {
                if (BotWebViewSheet.this.webViewContainer.getWebView() != null) {
                    BotWebViewSheet.this.webViewContainer.getWebView().animate().cancel();
                    BotWebViewSheet.this.webViewContainer.getWebView().animate().alpha(0.0f).start();
                }
                BotWebViewSheet.this.progressView.setLoadProgress(0.0f);
                BotWebViewSheet.this.progressView.setAlpha(1.0f);
                BotWebViewSheet.this.progressView.setVisibility(0);
                BotWebViewSheet.this.webViewContainer.setBotUser(MessagesController.getInstance(this.val$currentAccount).getUser(Long.valueOf(this.val$botId)));
                BotWebViewSheet.this.webViewContainer.loadFlickerAndSettingsItem(this.val$currentAccount, this.val$botId, BotWebViewSheet.this.settingsItem);
                BotWebViewSheet.this.webViewContainer.reload();
            } else if (i == R.id.menu_settings) {
                BotWebViewSheet.this.webViewContainer.onSettingsButtonPressed();
            } else if (i == R.id.menu_delete_bot) {
                BotWebViewSheet.deleteBot(this.val$currentAccount, this.val$botId, new Runnable() { // from class: org.telegram.ui.bots.BotWebViewSheet$11$$ExternalSyntheticLambda0
                    @Override // java.lang.Runnable
                    public final void run() {
                        BotWebViewSheet.11.this.lambda$onItemClick$0();
                    }
                });
            } else if (i == R.id.menu_add_to_home_screen_bot) {
                MediaDataController.getInstance(this.val$currentAccount).installShortcut(this.val$botId, MediaDataController.SHORTCUT_TYPE_ATTACHED_BOT);
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onItemClick$0() {
            BotWebViewSheet.this.dismiss();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$requestWebView$16(final int i, final TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.bots.BotWebViewSheet$$ExternalSyntheticLambda13
            @Override // java.lang.Runnable
            public final void run() {
                BotWebViewSheet.this.lambda$requestWebView$15(tLObject, i);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$requestWebView$15(TLObject tLObject, int i) {
        if (tLObject instanceof TLRPC$TL_webViewResultUrl) {
            TLRPC$TL_webViewResultUrl tLRPC$TL_webViewResultUrl = (TLRPC$TL_webViewResultUrl) tLObject;
            this.queryId = tLRPC$TL_webViewResultUrl.query_id;
            this.webViewContainer.loadUrl(i, tLRPC$TL_webViewResultUrl.url);
            AndroidUtilities.runOnUIThread(this.pollRunnable, 60000L);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$requestWebView$18(final int i, final TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.bots.BotWebViewSheet$$ExternalSyntheticLambda12
            @Override // java.lang.Runnable
            public final void run() {
                BotWebViewSheet.this.lambda$requestWebView$17(tLObject, i);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$requestWebView$17(TLObject tLObject, int i) {
        if (tLObject instanceof TLRPC$TL_simpleWebViewResultUrl) {
            this.queryId = 0L;
            this.webViewContainer.loadUrl(i, ((TLRPC$TL_simpleWebViewResultUrl) tLObject).url);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$requestWebView$20(final int i, final TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.bots.BotWebViewSheet$$ExternalSyntheticLambda14
            @Override // java.lang.Runnable
            public final void run() {
                BotWebViewSheet.this.lambda$requestWebView$19(tLObject, i);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$requestWebView$19(TLObject tLObject, int i) {
        if (tLObject instanceof TLRPC$TL_webViewResultUrl) {
            TLRPC$TL_webViewResultUrl tLRPC$TL_webViewResultUrl = (TLRPC$TL_webViewResultUrl) tLObject;
            this.queryId = tLRPC$TL_webViewResultUrl.query_id;
            this.webViewContainer.loadUrl(i, tLRPC$TL_webViewResultUrl.url);
            AndroidUtilities.runOnUIThread(this.pollRunnable, 60000L);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$requestWebView$22(final int i, final TLObject tLObject, final TLRPC$TL_error tLRPC$TL_error) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.bots.BotWebViewSheet$$ExternalSyntheticLambda16
            @Override // java.lang.Runnable
            public final void run() {
                BotWebViewSheet.this.lambda$requestWebView$21(tLRPC$TL_error, tLObject, i);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$requestWebView$21(TLRPC$TL_error tLRPC$TL_error, TLObject tLObject, int i) {
        if (tLRPC$TL_error == null) {
            this.queryId = 0L;
            this.webViewContainer.loadUrl(i, ((TLRPC$TL_appWebViewResultUrl) tLObject).url);
            AndroidUtilities.runOnUIThread(this.pollRunnable, 60000L);
        }
    }

    private void preloadShortcutBotIcon(TLRPC$User tLRPC$User, TLRPC$TL_attachMenuBot tLRPC$TL_attachMenuBot) {
        if (tLRPC$TL_attachMenuBot == null || !tLRPC$TL_attachMenuBot.show_in_side_menu || MediaDataController.getInstance(this.currentAccount).isShortcutAdded(this.botId, MediaDataController.SHORTCUT_TYPE_ATTACHED_BOT)) {
            return;
        }
        if (tLRPC$User == null) {
            tLRPC$User = MessagesController.getInstance(this.currentAccount).getUser(Long.valueOf(this.botId));
        }
        if (tLRPC$User == null || tLRPC$User.photo == null || FileLoader.getInstance(this.currentAccount).getPathToAttach(tLRPC$User.photo.photo_small, true).exists()) {
            return;
        }
        MediaDataController.getInstance(this.currentAccount).preloadImage(ImageLocation.getForUser(tLRPC$User, 1), 0);
    }

    public static void deleteBot(final int i, final long j, final Runnable runnable) {
        final TLRPC$TL_attachMenuBot tLRPC$TL_attachMenuBot;
        Iterator<TLRPC$TL_attachMenuBot> it = MediaDataController.getInstance(i).getAttachMenuBots().bots.iterator();
        while (true) {
            if (!it.hasNext()) {
                tLRPC$TL_attachMenuBot = null;
                break;
            }
            TLRPC$TL_attachMenuBot next = it.next();
            if (next.bot_id == j) {
                tLRPC$TL_attachMenuBot = next;
                break;
            }
        }
        if (tLRPC$TL_attachMenuBot == null) {
            return;
        }
        new AlertDialog.Builder(LaunchActivity.getLastFragment().getContext()).setTitle(LocaleController.getString(R.string.BotRemoveFromMenuTitle)).setMessage(AndroidUtilities.replaceTags(LocaleController.formatString("BotRemoveFromMenu", R.string.BotRemoveFromMenu, tLRPC$TL_attachMenuBot.short_name))).setPositiveButton(LocaleController.getString("OK", R.string.OK), new DialogInterface.OnClickListener() { // from class: org.telegram.ui.bots.BotWebViewSheet$$ExternalSyntheticLambda1
            @Override // android.content.DialogInterface.OnClickListener
            public final void onClick(DialogInterface dialogInterface, int i2) {
                BotWebViewSheet.lambda$deleteBot$25(i, j, tLRPC$TL_attachMenuBot, runnable, dialogInterface, i2);
            }
        }).setNegativeButton(LocaleController.getString("Cancel", R.string.Cancel), null).show();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$deleteBot$25(final int i, long j, TLRPC$TL_attachMenuBot tLRPC$TL_attachMenuBot, Runnable runnable, DialogInterface dialogInterface, int i2) {
        TLRPC$TL_messages_toggleBotInAttachMenu tLRPC$TL_messages_toggleBotInAttachMenu = new TLRPC$TL_messages_toggleBotInAttachMenu();
        tLRPC$TL_messages_toggleBotInAttachMenu.bot = MessagesController.getInstance(i).getInputUser(j);
        tLRPC$TL_messages_toggleBotInAttachMenu.enabled = false;
        ConnectionsManager.getInstance(i).sendRequest(tLRPC$TL_messages_toggleBotInAttachMenu, new RequestDelegate() { // from class: org.telegram.ui.bots.BotWebViewSheet$$ExternalSyntheticLambda18
            @Override // org.telegram.tgnet.RequestDelegate
            public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
                BotWebViewSheet.lambda$deleteBot$24(i, tLObject, tLRPC$TL_error);
            }
        }, 66);
        tLRPC$TL_attachMenuBot.show_in_side_menu = false;
        NotificationCenter.getInstance(i).lambda$postNotificationNameOnUIThread$1(NotificationCenter.attachMenuBotsDidLoad, new Object[0]);
        MediaDataController.getInstance(i).uninstallShortcut(j, MediaDataController.SHORTCUT_TYPE_ATTACHED_BOT);
        if (runnable != null) {
            runnable.run();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$deleteBot$24(final int i, TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.bots.BotWebViewSheet$$ExternalSyntheticLambda6
            @Override // java.lang.Runnable
            public final void run() {
                BotWebViewSheet.lambda$deleteBot$23(i);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$deleteBot$23(int i) {
        MediaDataController.getInstance(i).loadAttachMenuBots(false, true);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int getColor(int i) {
        return Theme.getColor(i, this.resourcesProvider);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes4.dex */
    public class 12 implements View.OnLayoutChangeListener {
        12() {
        }

        @Override // android.view.View.OnLayoutChangeListener
        public void onLayoutChange(View view, int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8) {
            view.removeOnLayoutChangeListener(this);
            BotWebViewSheet.this.swipeContainer.setSwipeOffsetY(BotWebViewSheet.this.swipeContainer.getHeight());
            BotWebViewSheet.this.frameLayout.setAlpha(1.0f);
            final AnimationNotificationsLocker animationNotificationsLocker = new AnimationNotificationsLocker();
            animationNotificationsLocker.lock();
            new SpringAnimation(BotWebViewSheet.this.swipeContainer, ChatAttachAlertBotWebViewLayout.WebViewSwipeContainer.SWIPE_OFFSET_Y, 0.0f).setSpring(new SpringForce(0.0f).setDampingRatio(0.75f).setStiffness(500.0f)).addEndListener(new DynamicAnimation.OnAnimationEndListener() { // from class: org.telegram.ui.bots.BotWebViewSheet$12$$ExternalSyntheticLambda0
                @Override // androidx.dynamicanimation.animation.DynamicAnimation.OnAnimationEndListener
                public final void onAnimationEnd(DynamicAnimation dynamicAnimation, boolean z, float f, float f2) {
                    AnimationNotificationsLocker.this.unlock();
                }
            }).start();
        }
    }

    @Override // android.app.Dialog
    public void show() {
        this.frameLayout.setAlpha(0.0f);
        this.frameLayout.addOnLayoutChangeListener(new 12());
        super.show();
    }

    public long getBotId() {
        return this.botId;
    }

    @Override // android.app.Dialog
    public void onBackPressed() {
        if (this.passcodeView.getVisibility() == 0) {
            if (getOwnerActivity() != null) {
                getOwnerActivity().finish();
            }
        } else if (this.webViewContainer.onBackPressed()) {
        } else {
            onCheckDismissByUser();
        }
    }

    @Override // android.app.Dialog, android.content.DialogInterface
    public void dismiss() {
        dismiss(null);
    }

    public boolean onCheckDismissByUser() {
        if (this.needCloseConfirmation) {
            TLRPC$User user = MessagesController.getInstance(this.currentAccount).getUser(Long.valueOf(this.botId));
            AlertDialog create = new AlertDialog.Builder(getContext()).setTitle(user != null ? ContactsController.formatName(user.first_name, user.last_name) : null).setMessage(LocaleController.getString(R.string.BotWebViewChangesMayNotBeSaved)).setPositiveButton(LocaleController.getString(R.string.BotWebViewCloseAnyway), new DialogInterface.OnClickListener() { // from class: org.telegram.ui.bots.BotWebViewSheet$$ExternalSyntheticLambda2
                @Override // android.content.DialogInterface.OnClickListener
                public final void onClick(DialogInterface dialogInterface, int i) {
                    BotWebViewSheet.this.lambda$onCheckDismissByUser$26(dialogInterface, i);
                }
            }).setNegativeButton(LocaleController.getString(R.string.Cancel), null).create();
            create.show();
            ((TextView) create.getButton(-1)).setTextColor(getColor(Theme.key_text_RedBold));
            return false;
        }
        dismiss();
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onCheckDismissByUser$26(DialogInterface dialogInterface, int i) {
        dismiss();
    }

    public void dismiss(final Runnable runnable) {
        if (this.dismissed) {
            return;
        }
        this.dismissed = true;
        AndroidUtilities.cancelRunOnUIThread(this.pollRunnable);
        this.webViewContainer.destroyWebView();
        NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.webViewResultSent);
        NotificationCenter.getGlobalInstance().removeObserver(this, NotificationCenter.didSetNewTheme);
        ChatAttachAlertBotWebViewLayout.WebViewSwipeContainer webViewSwipeContainer = this.swipeContainer;
        webViewSwipeContainer.stickTo(webViewSwipeContainer.getHeight() + this.frameLayout.measureKeyboardHeight(), new Runnable() { // from class: org.telegram.ui.bots.BotWebViewSheet$$ExternalSyntheticLambda10
            @Override // java.lang.Runnable
            public final void run() {
                BotWebViewSheet.this.lambda$dismiss$27(runnable);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$dismiss$27(Runnable runnable) {
        try {
            super.dismiss();
        } catch (Exception e) {
            FileLog.e(e);
        }
        if (runnable != null) {
            runnable.run();
        }
    }

    @Override // org.telegram.messenger.NotificationCenter.NotificationCenterDelegate
    public void didReceivedNotification(int i, int i2, Object... objArr) {
        if (i == NotificationCenter.webViewResultSent) {
            if (this.queryId == ((Long) objArr[0]).longValue()) {
                dismiss();
            }
        } else if (i == NotificationCenter.didSetNewTheme) {
            this.frameLayout.invalidate();
            this.webViewContainer.updateFlickerBackgroundColor(getColor(Theme.key_windowBackgroundWhite));
            updateActionBarColors();
            updateLightStatusBar();
        }
    }
}