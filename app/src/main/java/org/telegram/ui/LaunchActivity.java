package org.telegram.ui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Shader;
import android.location.LocationManager;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.os.StatFs;
import android.os.SystemClock;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Base64;
import android.view.ActionMode;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.arch.core.util.Function;
import androidx.core.content.pm.ShortcutInfoCompat;
import androidx.core.content.pm.ShortcutManagerCompat;
import androidx.core.graphics.ColorUtils;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.gms.common.api.Status;
import com.google.firebase.appindexing.FirebaseUserActions;
import com.google.firebase.appindexing.builders.AssistActionBuilder;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;
import org.telegram.PhoneFormat.PhoneFormat;
import org.telegram.messenger.AccountInstance;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.ApplicationLoader;
import org.telegram.messenger.BuildVars;
import org.telegram.messenger.ChatObject;
import org.telegram.messenger.ContactsController;
import org.telegram.messenger.ContactsLoadingObserver;
import org.telegram.messenger.DialogObject;
import org.telegram.messenger.FileLoader;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.FingerprintController;
import org.telegram.messenger.GenericProvider;
import org.telegram.messenger.ImageLoader;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.LocationController;
import org.telegram.messenger.MediaController;
import org.telegram.messenger.MediaDataController;
import org.telegram.messenger.MessageObject;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.MessagesStorage;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.SendMessagesHelper;
import org.telegram.messenger.SharedConfig;
import org.telegram.messenger.UserConfig;
import org.telegram.messenger.UserObject;
import org.telegram.messenger.Utilities;
import org.telegram.messenger.browser.Browser;
import org.telegram.messenger.voip.VideoCapturerDevice;
import org.telegram.messenger.voip.VoIPPendingCall;
import org.telegram.messenger.voip.VoIPService;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC$Chat;
import org.telegram.tgnet.TLRPC$ChatFull;
import org.telegram.tgnet.TLRPC$ChatInvite;
import org.telegram.tgnet.TLRPC$Document;
import org.telegram.tgnet.TLRPC$InputPeer;
import org.telegram.tgnet.TLRPC$LangPackString;
import org.telegram.tgnet.TLRPC$MessageMedia;
import org.telegram.tgnet.TLRPC$ReplyMarkup;
import org.telegram.tgnet.TLRPC$TL_account_authorizationForm;
import org.telegram.tgnet.TLRPC$TL_account_getAuthorizationForm;
import org.telegram.tgnet.TLRPC$TL_account_getPassword;
import org.telegram.tgnet.TLRPC$TL_account_getTheme;
import org.telegram.tgnet.TLRPC$TL_account_getWallPaper;
import org.telegram.tgnet.TLRPC$TL_account_password;
import org.telegram.tgnet.TLRPC$TL_account_sendConfirmPhoneCode;
import org.telegram.tgnet.TLRPC$TL_attachMenuBot;
import org.telegram.tgnet.TLRPC$TL_attachMenuBotsBot;
import org.telegram.tgnet.TLRPC$TL_auth_acceptLoginToken;
import org.telegram.tgnet.TLRPC$TL_auth_sentCode;
import org.telegram.tgnet.TLRPC$TL_authorization;
import org.telegram.tgnet.TLRPC$TL_boolTrue;
import org.telegram.tgnet.TLRPC$TL_channels_getChannels;
import org.telegram.tgnet.TLRPC$TL_chatAdminRights;
import org.telegram.tgnet.TLRPC$TL_chatBannedRights;
import org.telegram.tgnet.TLRPC$TL_chatInvitePeek;
import org.telegram.tgnet.TLRPC$TL_codeSettings;
import org.telegram.tgnet.TLRPC$TL_contact;
import org.telegram.tgnet.TLRPC$TL_contacts_resolvePhone;
import org.telegram.tgnet.TLRPC$TL_contacts_resolveUsername;
import org.telegram.tgnet.TLRPC$TL_contacts_resolvedPeer;
import org.telegram.tgnet.TLRPC$TL_error;
import org.telegram.tgnet.TLRPC$TL_groupCallParticipant;
import org.telegram.tgnet.TLRPC$TL_help_appUpdate;
import org.telegram.tgnet.TLRPC$TL_help_deepLinkInfo;
import org.telegram.tgnet.TLRPC$TL_help_getAppUpdate;
import org.telegram.tgnet.TLRPC$TL_help_getDeepLinkInfo;
import org.telegram.tgnet.TLRPC$TL_help_termsOfService;
import org.telegram.tgnet.TLRPC$TL_inputChannel;
import org.telegram.tgnet.TLRPC$TL_inputGameShortName;
import org.telegram.tgnet.TLRPC$TL_inputInvoiceSlug;
import org.telegram.tgnet.TLRPC$TL_inputMediaGame;
import org.telegram.tgnet.TLRPC$TL_inputStickerSetShortName;
import org.telegram.tgnet.TLRPC$TL_inputThemeSlug;
import org.telegram.tgnet.TLRPC$TL_inputWallPaperSlug;
import org.telegram.tgnet.TLRPC$TL_langPackLanguage;
import org.telegram.tgnet.TLRPC$TL_langpack_getLanguage;
import org.telegram.tgnet.TLRPC$TL_langpack_getStrings;
import org.telegram.tgnet.TLRPC$TL_messages_chats;
import org.telegram.tgnet.TLRPC$TL_messages_checkChatInvite;
import org.telegram.tgnet.TLRPC$TL_messages_checkHistoryImport;
import org.telegram.tgnet.TLRPC$TL_messages_discussionMessage;
import org.telegram.tgnet.TLRPC$TL_messages_getAttachMenuBot;
import org.telegram.tgnet.TLRPC$TL_messages_getDiscussionMessage;
import org.telegram.tgnet.TLRPC$TL_messages_historyImportParsed;
import org.telegram.tgnet.TLRPC$TL_messages_importChatInvite;
import org.telegram.tgnet.TLRPC$TL_messages_stickerSet;
import org.telegram.tgnet.TLRPC$TL_messages_toggleBotInAttachMenu;
import org.telegram.tgnet.TLRPC$TL_payments_getPaymentForm;
import org.telegram.tgnet.TLRPC$TL_payments_paymentForm;
import org.telegram.tgnet.TLRPC$TL_payments_paymentReceipt;
import org.telegram.tgnet.TLRPC$TL_theme;
import org.telegram.tgnet.TLRPC$TL_wallPaper;
import org.telegram.tgnet.TLRPC$TL_wallPaperSettings;
import org.telegram.tgnet.TLRPC$TL_webPage;
import org.telegram.tgnet.TLRPC$ThemeSettings;
import org.telegram.tgnet.TLRPC$Updates;
import org.telegram.tgnet.TLRPC$User;
import org.telegram.tgnet.TLRPC$UserFull;
import org.telegram.tgnet.TLRPC$Vector;
import org.telegram.tgnet.TLRPC$WallPaper;
import org.telegram.tgnet.TLRPC$WallPaperSettings;
import org.telegram.ui.ActionBar.ActionBarLayout;
import org.telegram.ui.ActionBar.AlertDialog;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.ActionBar.DrawerLayoutContainer;
import org.telegram.ui.ActionBar.SimpleTextView;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Adapters.DrawerLayoutAdapter;
import org.telegram.ui.Cells.DrawerAddCell;
import org.telegram.ui.Cells.DrawerProfileCell;
import org.telegram.ui.Cells.DrawerUserCell;
import org.telegram.ui.Cells.LanguageCell;
import org.telegram.ui.ChatActivity;
import org.telegram.ui.ChatRightsEditActivity;
import org.telegram.ui.Components.AlertsCreator;
import org.telegram.ui.Components.AppIconBulletinLayout;
import org.telegram.ui.Components.AttachBotIntroTopView;
import org.telegram.ui.Components.AudioPlayerAlert;
import org.telegram.ui.Components.BlockingUpdateView;
import org.telegram.ui.Components.Bulletin;
import org.telegram.ui.Components.BulletinFactory;
import org.telegram.ui.Components.CubicBezierInterpolator;
import org.telegram.ui.Components.Easings;
import org.telegram.ui.Components.EmbedBottomSheet;
import org.telegram.ui.Components.FireworksOverlay;
import org.telegram.ui.Components.GroupCallPip;
import org.telegram.ui.Components.JoinGroupAlert;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.ui.Components.PasscodeView;
import org.telegram.ui.Components.PhonebookShareAlert;
import org.telegram.ui.Components.PipRoundVideoView;
import org.telegram.ui.Components.Premium.LimitReachedBottomSheet;
import org.telegram.ui.Components.RLottieDrawable;
import org.telegram.ui.Components.RLottieImageView;
import org.telegram.ui.Components.RadialProgress2;
import org.telegram.ui.Components.RecyclerListView;
import org.telegram.ui.Components.SharingLocationsAlert;
import org.telegram.ui.Components.SideMenultItemAnimator;
import org.telegram.ui.Components.SizeNotifierFrameLayout;
import org.telegram.ui.Components.StickerSetBulletinLayout;
import org.telegram.ui.Components.StickersAlert;
import org.telegram.ui.Components.TermsOfServiceView;
import org.telegram.ui.Components.ThemeEditorView;
import org.telegram.ui.Components.UpdateAppAlertDialog;
import org.telegram.ui.Components.VerticalPositionAutoAnimator;
import org.telegram.ui.Components.voip.VoIPHelper;
import org.telegram.ui.DialogsActivity;
import org.telegram.ui.LauncherIconController;
import org.telegram.ui.WallpapersListActivity;
import org.webrtc.voiceengine.WebRtcAudioTrack;
/* loaded from: classes3.dex */
public class LaunchActivity extends BasePermissionsActivity implements ActionBarLayout.ActionBarLayoutDelegate, NotificationCenter.NotificationCenterDelegate, DialogsActivity.DialogsActivityDelegate {
    public static boolean isResumed;
    public static Runnable onResumeStaticCallback;
    private ActionBarLayout actionBarLayout;
    private SizeNotifierFrameLayout backgroundTablet;
    private BlockingUpdateView blockingUpdateView;
    private ArrayList<TLRPC$User> contactsToSend;
    private Uri contactsToSendUri;
    private int currentConnectionState;
    private String documentsMimeType;
    private ArrayList<String> documentsOriginalPathsArray;
    private ArrayList<String> documentsPathsArray;
    private ArrayList<Uri> documentsUrisArray;
    private DrawerLayoutAdapter drawerLayoutAdapter;
    public DrawerLayoutContainer drawerLayoutContainer;
    private HashMap<String, String> englishLocaleStrings;
    private Uri exportingChatUri;
    private boolean finished;
    private FireworksOverlay fireworksOverlay;
    private FrameLayout frameLayout;
    private ArrayList<Parcelable> importingStickers;
    private ArrayList<String> importingStickersEmoji;
    private String importingStickersSoftware;
    private SideMenultItemAnimator itemAnimator;
    private ActionBarLayout layersActionBarLayout;
    private boolean loadingLocaleDialog;
    private TLRPC$TL_theme loadingTheme;
    private boolean loadingThemeAccent;
    private String loadingThemeFileName;
    private Theme.ThemeInfo loadingThemeInfo;
    private AlertDialog loadingThemeProgressDialog;
    private TLRPC$TL_wallPaper loadingThemeWallpaper;
    private String loadingThemeWallpaperName;
    private AlertDialog localeDialog;
    private Runnable lockRunnable;
    private boolean navigateToPremiumBot;
    private ViewTreeObserver.OnGlobalLayoutListener onGlobalLayoutListener;
    private Intent passcodeSaveIntent;
    private boolean passcodeSaveIntentIsNew;
    private boolean passcodeSaveIntentIsRestore;
    private PasscodeView passcodeView;
    private ArrayList<SendMessagesHelper.SendingMediaInfo> photoPathsArray;
    private AlertDialog proxyErrorDialog;
    private ActionBarLayout rightActionBarLayout;
    private View rippleAbove;
    private String sendingText;
    private FrameLayout shadowTablet;
    private FrameLayout shadowTabletSide;
    private RecyclerListView sideMenu;
    private FrameLayout sideMenuContainer;
    private HashMap<String, String> systemLocaleStrings;
    private boolean tabletFullSize;
    private int[] tempLocation;
    private TermsOfServiceView termsOfServiceView;
    private ImageView themeSwitchImageView;
    private RLottieDrawable themeSwitchSunDrawable;
    private View themeSwitchSunView;
    private FrameLayout updateLayout;
    private RadialProgress2 updateLayoutIcon;
    private TextView updateSizeTextView;
    private SimpleTextView updateTextView;
    private String videoPath;
    private ActionMode visibleActionMode;
    private AlertDialog visibleDialog;
    private boolean wasMutedByAdminRaisedHand;
    private static ArrayList<BaseFragment> mainFragmentsStack = new ArrayList<>();
    private static ArrayList<BaseFragment> layerFragmentsStack = new ArrayList<>();
    private static ArrayList<BaseFragment> rightFragmentsStack = new ArrayList<>();
    private boolean isNavigationBarColorFrozen = false;
    private List<Runnable> onUserLeaveHintListeners = new ArrayList();

    public static /* synthetic */ void lambda$onCreate$1(View view) {
    }

    @Override // android.app.Activity
    protected void onCreate(Bundle bundle) {
        boolean z;
        Intent intent;
        Uri data;
        ApplicationLoader.postInitApplication();
        AndroidUtilities.checkDisplaySize(this, getResources().getConfiguration());
        int i = UserConfig.selectedAccount;
        this.currentAccount = i;
        if (!UserConfig.getInstance(i).isClientActivated() && (intent = getIntent()) != null && intent.getAction() != null) {
            if ("android.intent.action.SEND".equals(intent.getAction()) || "android.intent.action.SEND_MULTIPLE".equals(intent.getAction())) {
                super.onCreate(bundle);
                finish();
                return;
            } else if ("android.intent.action.VIEW".equals(intent.getAction()) && (data = intent.getData()) != null) {
                String lowerCase = data.toString().toLowerCase();
                if (!lowerCase.startsWith("tg:proxy") && !lowerCase.startsWith("tg://proxy") && !lowerCase.startsWith("tg:socks")) {
                    lowerCase.startsWith("tg://socks");
                }
            }
        }
        requestWindowFeature(1);
        setTheme(2131689489);
        if (Build.VERSION.SDK_INT >= 21) {
            try {
                setTaskDescription(new ActivityManager.TaskDescription((String) null, (Bitmap) null, Theme.getColor("actionBarDefault") | (-16777216)));
            } catch (Throwable unused) {
            }
            try {
                getWindow().setNavigationBarColor(-16777216);
            } catch (Throwable unused2) {
            }
        }
        getWindow().setBackgroundDrawableResource(2131166187);
        if (SharedConfig.passcodeHash.length() > 0 && !SharedConfig.allowScreenCapture) {
            try {
                getWindow().setFlags(8192, 8192);
            } catch (Exception e) {
                FileLog.e(e);
            }
        }
        super.onCreate(bundle);
        int i2 = Build.VERSION.SDK_INT;
        if (i2 >= 24) {
            AndroidUtilities.isInMultiwindow = isInMultiWindowMode();
        }
        Theme.createCommonChatResources();
        Theme.createDialogsResources(this);
        if (SharedConfig.passcodeHash.length() != 0 && SharedConfig.appLocked) {
            SharedConfig.lastPauseTime = (int) (SystemClock.elapsedRealtime() / 1000);
        }
        AndroidUtilities.fillStatusBarHeight(this);
        this.actionBarLayout = new AnonymousClass1(this);
        AnonymousClass2 anonymousClass2 = new AnonymousClass2(this);
        this.frameLayout = anonymousClass2;
        char c = 65535;
        setContentView(anonymousClass2, new ViewGroup.LayoutParams(-1, -1));
        int i3 = 8;
        if (i2 >= 21) {
            ImageView imageView = new ImageView(this);
            this.themeSwitchImageView = imageView;
            imageView.setVisibility(8);
        }
        AnonymousClass3 anonymousClass3 = new AnonymousClass3(this, this);
        this.drawerLayoutContainer = anonymousClass3;
        anonymousClass3.setBehindKeyboardColor(Theme.getColor("windowBackgroundWhite"));
        this.frameLayout.addView(this.drawerLayoutContainer, LayoutHelper.createFrame(-1, -1.0f));
        if (i2 >= 21) {
            AnonymousClass4 anonymousClass4 = new AnonymousClass4(this);
            this.themeSwitchSunView = anonymousClass4;
            this.frameLayout.addView(anonymousClass4, LayoutHelper.createFrame(48, 48.0f));
            this.themeSwitchSunView.setVisibility(8);
        }
        FrameLayout frameLayout = this.frameLayout;
        FireworksOverlay fireworksOverlay = new FireworksOverlay(this);
        this.fireworksOverlay = fireworksOverlay;
        frameLayout.addView(fireworksOverlay);
        if (AndroidUtilities.isTablet()) {
            getWindow().setSoftInputMode(16);
            AnonymousClass5 anonymousClass5 = new AnonymousClass5(this);
            this.drawerLayoutContainer.addView(anonymousClass5, LayoutHelper.createFrame(-1, -1.0f));
            AnonymousClass6 anonymousClass6 = new AnonymousClass6(this, this);
            this.backgroundTablet = anonymousClass6;
            anonymousClass6.setOccupyStatusBar(false);
            this.backgroundTablet.setBackgroundImage(Theme.getCachedWallpaper(), Theme.isWallpaperMotion());
            anonymousClass5.addView(this.backgroundTablet, LayoutHelper.createRelative(-1, -1));
            anonymousClass5.addView(this.actionBarLayout);
            ActionBarLayout actionBarLayout = new ActionBarLayout(this);
            this.rightActionBarLayout = actionBarLayout;
            actionBarLayout.init(rightFragmentsStack);
            this.rightActionBarLayout.setDelegate(this);
            anonymousClass5.addView(this.rightActionBarLayout);
            FrameLayout frameLayout2 = new FrameLayout(this);
            this.shadowTabletSide = frameLayout2;
            frameLayout2.setBackgroundColor(1076449908);
            anonymousClass5.addView(this.shadowTabletSide);
            FrameLayout frameLayout3 = new FrameLayout(this);
            this.shadowTablet = frameLayout3;
            frameLayout3.setVisibility(layerFragmentsStack.isEmpty() ? 8 : 0);
            this.shadowTablet.setBackgroundColor(2130706432);
            anonymousClass5.addView(this.shadowTablet);
            this.shadowTablet.setOnTouchListener(new LaunchActivity$$ExternalSyntheticLambda19(this));
            this.shadowTablet.setOnClickListener(LaunchActivity$$ExternalSyntheticLambda18.INSTANCE);
            ActionBarLayout actionBarLayout2 = new ActionBarLayout(this);
            this.layersActionBarLayout = actionBarLayout2;
            actionBarLayout2.setRemoveActionBarExtraHeight(true);
            this.layersActionBarLayout.setBackgroundView(this.shadowTablet);
            this.layersActionBarLayout.setUseAlphaAnimations(true);
            this.layersActionBarLayout.setBackgroundResource(2131165291);
            this.layersActionBarLayout.init(layerFragmentsStack);
            this.layersActionBarLayout.setDelegate(this);
            this.layersActionBarLayout.setDrawerLayoutContainer(this.drawerLayoutContainer);
            ActionBarLayout actionBarLayout3 = this.layersActionBarLayout;
            if (!layerFragmentsStack.isEmpty()) {
                i3 = 0;
            }
            actionBarLayout3.setVisibility(i3);
            VerticalPositionAutoAnimator.attach(this.layersActionBarLayout);
            anonymousClass5.addView(this.layersActionBarLayout);
        } else {
            this.drawerLayoutContainer.addView(this.actionBarLayout, new ViewGroup.LayoutParams(-1, -1));
        }
        this.sideMenuContainer = new FrameLayout(this);
        AnonymousClass7 anonymousClass7 = new AnonymousClass7(this);
        this.sideMenu = anonymousClass7;
        SideMenultItemAnimator sideMenultItemAnimator = new SideMenultItemAnimator(anonymousClass7);
        this.itemAnimator = sideMenultItemAnimator;
        this.sideMenu.setItemAnimator(sideMenultItemAnimator);
        this.sideMenu.setBackgroundColor(Theme.getColor("chats_menuBackground"));
        this.sideMenu.setLayoutManager(new LinearLayoutManager(this, 1, false));
        this.sideMenu.setAllowItemsInteractionDuringAnimation(false);
        RecyclerListView recyclerListView = this.sideMenu;
        DrawerLayoutAdapter drawerLayoutAdapter = new DrawerLayoutAdapter(this, this.itemAnimator, this.drawerLayoutContainer);
        this.drawerLayoutAdapter = drawerLayoutAdapter;
        recyclerListView.setAdapter(drawerLayoutAdapter);
        this.sideMenuContainer.addView(this.sideMenu, LayoutHelper.createFrame(-1, -1.0f));
        this.drawerLayoutContainer.setDrawerLayout(this.sideMenuContainer);
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) this.sideMenuContainer.getLayoutParams();
        Point realScreenSize = AndroidUtilities.getRealScreenSize();
        layoutParams.width = AndroidUtilities.isTablet() ? AndroidUtilities.dp(320.0f) : Math.min(AndroidUtilities.dp(320.0f), Math.min(realScreenSize.x, realScreenSize.y) - AndroidUtilities.dp(56.0f));
        layoutParams.height = -1;
        this.sideMenuContainer.setLayoutParams(layoutParams);
        this.sideMenu.setOnItemClickListener(new LaunchActivity$$ExternalSyntheticLambda94(this));
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new AnonymousClass8(3, 0));
        itemTouchHelper.attachToRecyclerView(this.sideMenu);
        this.sideMenu.setOnItemLongClickListener(new LaunchActivity$$ExternalSyntheticLambda95(this, itemTouchHelper));
        this.drawerLayoutContainer.setParentActionBarLayout(this.actionBarLayout);
        this.actionBarLayout.setDrawerLayoutContainer(this.drawerLayoutContainer);
        this.actionBarLayout.init(mainFragmentsStack);
        this.actionBarLayout.setFragmentStackChangedListener(new LaunchActivity$$ExternalSyntheticLambda31(this));
        this.actionBarLayout.setDelegate(this);
        Theme.loadWallpaper();
        checkCurrentAccount();
        updateCurrentConnectionState(this.currentAccount);
        NotificationCenter globalInstance = NotificationCenter.getGlobalInstance();
        int i4 = NotificationCenter.closeOtherAppActivities;
        globalInstance.postNotificationName(i4, this);
        this.currentConnectionState = ConnectionsManager.getInstance(this.currentAccount).getConnectionState();
        NotificationCenter.getGlobalInstance().addObserver(this, NotificationCenter.needShowAlert);
        NotificationCenter.getGlobalInstance().addObserver(this, NotificationCenter.reloadInterface);
        NotificationCenter.getGlobalInstance().addObserver(this, NotificationCenter.suggestedLangpack);
        NotificationCenter.getGlobalInstance().addObserver(this, NotificationCenter.didSetNewTheme);
        NotificationCenter.getGlobalInstance().addObserver(this, NotificationCenter.needSetDayNightTheme);
        NotificationCenter.getGlobalInstance().addObserver(this, NotificationCenter.needCheckSystemBarColors);
        NotificationCenter.getGlobalInstance().addObserver(this, i4);
        NotificationCenter.getGlobalInstance().addObserver(this, NotificationCenter.didSetPasscode);
        NotificationCenter.getGlobalInstance().addObserver(this, NotificationCenter.didSetNewWallpapper);
        NotificationCenter.getGlobalInstance().addObserver(this, NotificationCenter.notificationsCountUpdated);
        NotificationCenter.getGlobalInstance().addObserver(this, NotificationCenter.screenStateChanged);
        NotificationCenter.getGlobalInstance().addObserver(this, NotificationCenter.showBulletin);
        NotificationCenter.getGlobalInstance().addObserver(this, NotificationCenter.appUpdateAvailable);
        if (this.actionBarLayout.fragmentsStack.isEmpty()) {
            if (!UserConfig.getInstance(this.currentAccount).isClientActivated()) {
                this.actionBarLayout.addFragmentToStack(getClientNotActivatedFragment());
                this.drawerLayoutContainer.setAllowOpenDrawer(false, false);
            } else {
                DialogsActivity dialogsActivity = new DialogsActivity(null);
                dialogsActivity.setSideMenu(this.sideMenu);
                this.actionBarLayout.addFragmentToStack(dialogsActivity);
                this.drawerLayoutContainer.setAllowOpenDrawer(true, false);
            }
            if (bundle != null) {
                try {
                    String string = bundle.getString("fragment");
                    if (string != null) {
                        Bundle bundle2 = bundle.getBundle("args");
                        switch (string.hashCode()) {
                            case -1529105743:
                                if (string.equals("wallpapers")) {
                                    c = 5;
                                    break;
                                }
                                break;
                            case -1349522494:
                                if (string.equals("chat_profile")) {
                                    c = 4;
                                    break;
                                }
                                break;
                            case 3052376:
                                if (string.equals("chat")) {
                                    c = 0;
                                    break;
                                }
                                break;
                            case 98629247:
                                if (string.equals("group")) {
                                    c = 2;
                                    break;
                                }
                                break;
                            case 738950403:
                                if (string.equals("channel")) {
                                    c = 3;
                                    break;
                                }
                                break;
                            case 1434631203:
                                if (string.equals("settings")) {
                                    c = 1;
                                    break;
                                }
                                break;
                        }
                        if (c != 0) {
                            if (c == 1) {
                                bundle2.putLong("user_id", UserConfig.getInstance(this.currentAccount).clientUserId);
                                ProfileActivity profileActivity = new ProfileActivity(bundle2);
                                this.actionBarLayout.addFragmentToStack(profileActivity);
                                profileActivity.restoreSelfArgs(bundle);
                            } else if (c != 2) {
                                if (c != 3) {
                                    if (c != 4) {
                                        if (c == 5) {
                                            WallpapersListActivity wallpapersListActivity = new WallpapersListActivity(0);
                                            this.actionBarLayout.addFragmentToStack(wallpapersListActivity);
                                            wallpapersListActivity.restoreSelfArgs(bundle);
                                        }
                                    } else if (bundle2 != null) {
                                        ProfileActivity profileActivity2 = new ProfileActivity(bundle2);
                                        if (this.actionBarLayout.addFragmentToStack(profileActivity2)) {
                                            profileActivity2.restoreSelfArgs(bundle);
                                        }
                                    }
                                } else if (bundle2 != null) {
                                    ChannelCreateActivity channelCreateActivity = new ChannelCreateActivity(bundle2);
                                    if (this.actionBarLayout.addFragmentToStack(channelCreateActivity)) {
                                        channelCreateActivity.restoreSelfArgs(bundle);
                                    }
                                }
                            } else if (bundle2 != null) {
                                GroupCreateFinalActivity groupCreateFinalActivity = new GroupCreateFinalActivity(bundle2);
                                if (this.actionBarLayout.addFragmentToStack(groupCreateFinalActivity)) {
                                    groupCreateFinalActivity.restoreSelfArgs(bundle);
                                }
                            }
                        } else if (bundle2 != null) {
                            ChatActivity chatActivity = new ChatActivity(bundle2);
                            if (this.actionBarLayout.addFragmentToStack(chatActivity)) {
                                chatActivity.restoreSelfArgs(bundle);
                            }
                        }
                    }
                } catch (Exception e2) {
                    FileLog.e(e2);
                }
            }
        } else {
            BaseFragment baseFragment = this.actionBarLayout.fragmentsStack.get(0);
            if (baseFragment instanceof DialogsActivity) {
                ((DialogsActivity) baseFragment).setSideMenu(this.sideMenu);
            }
            if (AndroidUtilities.isTablet()) {
                z = this.actionBarLayout.fragmentsStack.size() <= 1 && this.layersActionBarLayout.fragmentsStack.isEmpty();
                if (this.layersActionBarLayout.fragmentsStack.size() == 1 && ((this.layersActionBarLayout.fragmentsStack.get(0) instanceof LoginActivity) || (this.layersActionBarLayout.fragmentsStack.get(0) instanceof IntroActivity))) {
                    z = false;
                }
            } else {
                z = true;
            }
            if (this.actionBarLayout.fragmentsStack.size() == 1 && ((this.actionBarLayout.fragmentsStack.get(0) instanceof LoginActivity) || (this.actionBarLayout.fragmentsStack.get(0) instanceof IntroActivity))) {
                z = false;
            }
            this.drawerLayoutContainer.setAllowOpenDrawer(z, false);
        }
        checkLayout();
        checkSystemBarColors();
        handleIntent(getIntent(), false, bundle != null, false);
        try {
            String str = Build.DISPLAY;
            String str2 = Build.USER;
            String str3 = "";
            String lowerCase2 = str != null ? str.toLowerCase() : str3;
            if (str2 != null) {
                str3 = lowerCase2.toLowerCase();
            }
            if (BuildVars.LOGS_ENABLED) {
                FileLog.d("OS name " + lowerCase2 + " " + str3);
            }
            if ((lowerCase2.contains("flyme") || str3.contains("flyme")) && Build.VERSION.SDK_INT <= 24) {
                AndroidUtilities.incorrectDisplaySizeFix = true;
                View rootView = getWindow().getDecorView().getRootView();
                ViewTreeObserver viewTreeObserver = rootView.getViewTreeObserver();
                LaunchActivity$$ExternalSyntheticLambda20 launchActivity$$ExternalSyntheticLambda20 = new LaunchActivity$$ExternalSyntheticLambda20(rootView);
                this.onGlobalLayoutListener = launchActivity$$ExternalSyntheticLambda20;
                viewTreeObserver.addOnGlobalLayoutListener(launchActivity$$ExternalSyntheticLambda20);
            }
        } catch (Exception e3) {
            FileLog.e(e3);
        }
        MediaController.getInstance().setBaseActivity(this, true);
        AndroidUtilities.startAppCenter(this);
        updateAppUpdateViews(false);
        int i5 = Build.VERSION.SDK_INT;
        if (i5 >= 23) {
            FingerprintController.checkKeyReady();
        }
        if (i5 < 28 || !((ActivityManager) getSystemService("activity")).isBackgroundRestricted() || System.currentTimeMillis() - SharedConfig.BackgroundActivityPrefs.getLastCheckedBackgroundActivity() < 86400000) {
            return;
        }
        AlertsCreator.createBackgroundActivityDialog(this).show();
        SharedConfig.BackgroundActivityPrefs.setLastCheckedBackgroundActivity(System.currentTimeMillis());
    }

    /* renamed from: org.telegram.ui.LaunchActivity$1 */
    /* loaded from: classes3.dex */
    class AnonymousClass1 extends ActionBarLayout {
        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass1(Context context) {
            super(context);
            LaunchActivity.this = r1;
        }

        @Override // org.telegram.ui.ActionBar.ActionBarLayout
        public void setThemeAnimationValue(float f) {
            super.setThemeAnimationValue(f);
            if (ArticleViewer.hasInstance() && ArticleViewer.getInstance().isVisible()) {
                ArticleViewer.getInstance().updateThemeColors(f);
            }
            LaunchActivity.this.drawerLayoutContainer.setBehindKeyboardColor(Theme.getColor("windowBackgroundWhite"));
            if (PhotoViewer.hasInstance()) {
                PhotoViewer.getInstance().updateColors();
            }
        }
    }

    /* renamed from: org.telegram.ui.LaunchActivity$2 */
    /* loaded from: classes3.dex */
    class AnonymousClass2 extends FrameLayout {
        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass2(Context context) {
            super(context);
            LaunchActivity.this = r1;
        }

        @Override // android.view.ViewGroup, android.view.View
        protected void dispatchDraw(Canvas canvas) {
            super.dispatchDraw(canvas);
            LaunchActivity.this.drawRippleAbove(canvas, this);
        }
    }

    /* renamed from: org.telegram.ui.LaunchActivity$3 */
    /* loaded from: classes3.dex */
    class AnonymousClass3 extends DrawerLayoutContainer {
        AnonymousClass3(LaunchActivity launchActivity, Context context) {
            super(context);
        }

        @Override // org.telegram.ui.ActionBar.DrawerLayoutContainer, android.widget.FrameLayout, android.view.ViewGroup, android.view.View
        public void onLayout(boolean z, int i, int i2, int i3, int i4) {
            super.onLayout(z, i, i2, i3, i4);
            setDrawerPosition(getDrawerPosition());
        }
    }

    /* renamed from: org.telegram.ui.LaunchActivity$4 */
    /* loaded from: classes3.dex */
    class AnonymousClass4 extends View {
        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass4(Context context) {
            super(context);
            LaunchActivity.this = r1;
        }

        @Override // android.view.View
        protected void onDraw(Canvas canvas) {
            if (LaunchActivity.this.themeSwitchSunDrawable != null) {
                LaunchActivity.this.themeSwitchSunDrawable.draw(canvas);
                invalidate();
            }
        }
    }

    /* renamed from: org.telegram.ui.LaunchActivity$5 */
    /* loaded from: classes3.dex */
    class AnonymousClass5 extends RelativeLayout {
        private boolean inLayout;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass5(Context context) {
            super(context);
            LaunchActivity.this = r1;
        }

        @Override // android.widget.RelativeLayout, android.view.View, android.view.ViewParent
        public void requestLayout() {
            if (this.inLayout) {
                return;
            }
            super.requestLayout();
        }

        @Override // android.widget.RelativeLayout, android.view.View
        protected void onMeasure(int i, int i2) {
            this.inLayout = true;
            int size = View.MeasureSpec.getSize(i);
            int size2 = View.MeasureSpec.getSize(i2);
            setMeasuredDimension(size, size2);
            if (AndroidUtilities.isInMultiwindow || (AndroidUtilities.isSmallTablet() && getResources().getConfiguration().orientation != 2)) {
                LaunchActivity.this.tabletFullSize = true;
                LaunchActivity.this.actionBarLayout.measure(View.MeasureSpec.makeMeasureSpec(size, 1073741824), View.MeasureSpec.makeMeasureSpec(size2, 1073741824));
            } else {
                LaunchActivity.this.tabletFullSize = false;
                int i3 = (size / 100) * 35;
                if (i3 < AndroidUtilities.dp(320.0f)) {
                    i3 = AndroidUtilities.dp(320.0f);
                }
                LaunchActivity.this.actionBarLayout.measure(View.MeasureSpec.makeMeasureSpec(i3, 1073741824), View.MeasureSpec.makeMeasureSpec(size2, 1073741824));
                LaunchActivity.this.shadowTabletSide.measure(View.MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(1.0f), 1073741824), View.MeasureSpec.makeMeasureSpec(size2, 1073741824));
                LaunchActivity.this.rightActionBarLayout.measure(View.MeasureSpec.makeMeasureSpec(size - i3, 1073741824), View.MeasureSpec.makeMeasureSpec(size2, 1073741824));
            }
            LaunchActivity.this.backgroundTablet.measure(View.MeasureSpec.makeMeasureSpec(size, 1073741824), View.MeasureSpec.makeMeasureSpec(size2, 1073741824));
            LaunchActivity.this.shadowTablet.measure(View.MeasureSpec.makeMeasureSpec(size, 1073741824), View.MeasureSpec.makeMeasureSpec(size2, 1073741824));
            LaunchActivity.this.layersActionBarLayout.measure(View.MeasureSpec.makeMeasureSpec(Math.min(AndroidUtilities.dp(530.0f), size), 1073741824), View.MeasureSpec.makeMeasureSpec(Math.min(AndroidUtilities.dp(528.0f), size2), 1073741824));
            this.inLayout = false;
        }

        @Override // android.widget.RelativeLayout, android.view.ViewGroup, android.view.View
        protected void onLayout(boolean z, int i, int i2, int i3, int i4) {
            int i5 = i3 - i;
            int i6 = i4 - i2;
            if (AndroidUtilities.isInMultiwindow || (AndroidUtilities.isSmallTablet() && getResources().getConfiguration().orientation != 2)) {
                LaunchActivity.this.actionBarLayout.layout(0, 0, LaunchActivity.this.actionBarLayout.getMeasuredWidth(), LaunchActivity.this.actionBarLayout.getMeasuredHeight());
            } else {
                int i7 = (i5 / 100) * 35;
                if (i7 < AndroidUtilities.dp(320.0f)) {
                    i7 = AndroidUtilities.dp(320.0f);
                }
                LaunchActivity.this.shadowTabletSide.layout(i7, 0, LaunchActivity.this.shadowTabletSide.getMeasuredWidth() + i7, LaunchActivity.this.shadowTabletSide.getMeasuredHeight());
                LaunchActivity.this.actionBarLayout.layout(0, 0, LaunchActivity.this.actionBarLayout.getMeasuredWidth(), LaunchActivity.this.actionBarLayout.getMeasuredHeight());
                LaunchActivity.this.rightActionBarLayout.layout(i7, 0, LaunchActivity.this.rightActionBarLayout.getMeasuredWidth() + i7, LaunchActivity.this.rightActionBarLayout.getMeasuredHeight());
            }
            int measuredWidth = (i5 - LaunchActivity.this.layersActionBarLayout.getMeasuredWidth()) / 2;
            int measuredHeight = (i6 - LaunchActivity.this.layersActionBarLayout.getMeasuredHeight()) / 2;
            LaunchActivity.this.layersActionBarLayout.layout(measuredWidth, measuredHeight, LaunchActivity.this.layersActionBarLayout.getMeasuredWidth() + measuredWidth, LaunchActivity.this.layersActionBarLayout.getMeasuredHeight() + measuredHeight);
            LaunchActivity.this.backgroundTablet.layout(0, 0, LaunchActivity.this.backgroundTablet.getMeasuredWidth(), LaunchActivity.this.backgroundTablet.getMeasuredHeight());
            LaunchActivity.this.shadowTablet.layout(0, 0, LaunchActivity.this.shadowTablet.getMeasuredWidth(), LaunchActivity.this.shadowTablet.getMeasuredHeight());
        }
    }

    /* renamed from: org.telegram.ui.LaunchActivity$6 */
    /* loaded from: classes3.dex */
    class AnonymousClass6 extends SizeNotifierFrameLayout {
        @Override // org.telegram.ui.Components.SizeNotifierFrameLayout
        protected boolean isActionBarVisible() {
            return false;
        }

        AnonymousClass6(LaunchActivity launchActivity, Context context) {
            super(context);
        }
    }

    public /* synthetic */ boolean lambda$onCreate$0(View view, MotionEvent motionEvent) {
        if (!this.actionBarLayout.fragmentsStack.isEmpty() && motionEvent.getAction() == 1) {
            float x = motionEvent.getX();
            float y = motionEvent.getY();
            int[] iArr = new int[2];
            this.layersActionBarLayout.getLocationOnScreen(iArr);
            int i = iArr[0];
            int i2 = iArr[1];
            if (!this.layersActionBarLayout.checkTransitionAnimation() && (x <= i || x >= i + this.layersActionBarLayout.getWidth() || y <= i2 || y >= i2 + this.layersActionBarLayout.getHeight())) {
                if (!this.layersActionBarLayout.fragmentsStack.isEmpty()) {
                    while (this.layersActionBarLayout.fragmentsStack.size() - 1 > 0) {
                        ActionBarLayout actionBarLayout = this.layersActionBarLayout;
                        actionBarLayout.removeFragmentFromStack(actionBarLayout.fragmentsStack.get(0));
                    }
                    this.layersActionBarLayout.closeLastFragment(true);
                }
                return true;
            }
        }
        return false;
    }

    /* renamed from: org.telegram.ui.LaunchActivity$7 */
    /* loaded from: classes3.dex */
    class AnonymousClass7 extends RecyclerListView {
        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass7(Context context) {
            super(context);
            LaunchActivity.this = r1;
        }

        @Override // androidx.recyclerview.widget.RecyclerView, android.view.ViewGroup
        public boolean drawChild(Canvas canvas, View view, long j) {
            int i;
            if (LaunchActivity.this.itemAnimator == null || !LaunchActivity.this.itemAnimator.isRunning() || !LaunchActivity.this.itemAnimator.isAnimatingChild(view)) {
                i = -1;
            } else {
                i = canvas.save();
                canvas.clipRect(0, LaunchActivity.this.itemAnimator.getAnimationClipTop(), getMeasuredWidth(), getMeasuredHeight());
            }
            boolean drawChild = super.drawChild(canvas, view, j);
            if (i >= 0) {
                canvas.restoreToCount(i);
                invalidate();
                invalidateViews();
            }
            return drawChild;
        }
    }

    public /* synthetic */ void lambda$onCreate$3(View view, int i, float f, float f2) {
        DrawerLayoutAdapter drawerLayoutAdapter;
        boolean z = true;
        if (i == 0) {
            DrawerProfileCell drawerProfileCell = (DrawerProfileCell) view;
            if (drawerProfileCell.isInAvatar(f, f2)) {
                openSettings(drawerProfileCell.hasAvatar());
                return;
            }
            this.drawerLayoutAdapter.setAccountsShown(!drawerLayoutAdapter.isAccountsShown(), true);
        } else if (view instanceof DrawerUserCell) {
            switchToAccount(((DrawerUserCell) view).getAccountNumber(), true);
            this.drawerLayoutContainer.closeDrawer(false);
        } else {
            Integer num = null;
            if (view instanceof DrawerAddCell) {
                int i2 = 0;
                for (int i3 = 3; i3 >= 0; i3--) {
                    if (!UserConfig.getInstance(i3).isClientActivated()) {
                        i2++;
                        if (num == null) {
                            num = Integer.valueOf(i3);
                        }
                    }
                }
                if (!UserConfig.hasPremiumOnAccounts()) {
                    i2--;
                }
                if (i2 > 0 && num != null) {
                    lambda$runLinkRequest$59(new LoginActivity(num.intValue()));
                    this.drawerLayoutContainer.closeDrawer(false);
                    return;
                } else if (UserConfig.hasPremiumOnAccounts() || this.actionBarLayout.fragmentsStack.size() <= 0) {
                    return;
                } else {
                    BaseFragment baseFragment = this.actionBarLayout.fragmentsStack.get(0);
                    LimitReachedBottomSheet limitReachedBottomSheet = new LimitReachedBottomSheet(baseFragment, this, 7, this.currentAccount);
                    baseFragment.showDialog(limitReachedBottomSheet);
                    limitReachedBottomSheet.onShowPremiumScreenRunnable = new LaunchActivity$$ExternalSyntheticLambda33(this);
                    return;
                }
            }
            int id = this.drawerLayoutAdapter.getId(i);
            if (id == 2) {
                lambda$runLinkRequest$59(new GroupCreateActivity(new Bundle()));
                this.drawerLayoutContainer.closeDrawer(false);
            } else if (id == 3) {
                Bundle bundle = new Bundle();
                bundle.putBoolean("onlyUsers", true);
                bundle.putBoolean("destroyAfterSelect", true);
                bundle.putBoolean("createSecretChat", true);
                bundle.putBoolean("allowBots", false);
                bundle.putBoolean("allowSelf", false);
                lambda$runLinkRequest$59(new ContactsActivity(bundle));
                this.drawerLayoutContainer.closeDrawer(false);
            } else if (id == 4) {
                SharedPreferences globalMainSettings = MessagesController.getGlobalMainSettings();
                if (!BuildVars.DEBUG_VERSION && globalMainSettings.getBoolean("channel_intro", false)) {
                    Bundle bundle2 = new Bundle();
                    bundle2.putInt("step", 0);
                    lambda$runLinkRequest$59(new ChannelCreateActivity(bundle2));
                } else {
                    lambda$runLinkRequest$59(new ActionIntroActivity(0));
                    globalMainSettings.edit().putBoolean("channel_intro", true).commit();
                }
                this.drawerLayoutContainer.closeDrawer(false);
            } else if (id == 6) {
                lambda$runLinkRequest$59(new ContactsActivity(null));
                this.drawerLayoutContainer.closeDrawer(false);
            } else if (id == 7) {
                lambda$runLinkRequest$59(new InviteContactsActivity());
                this.drawerLayoutContainer.closeDrawer(false);
            } else if (id == 8) {
                openSettings(false);
            } else if (id == 9) {
                Browser.openUrl(this, LocaleController.getString("TelegramFaqUrl", 2131628563));
                this.drawerLayoutContainer.closeDrawer(false);
            } else if (id == 10) {
                lambda$runLinkRequest$59(new CallLogActivity());
                this.drawerLayoutContainer.closeDrawer(false);
            } else if (id == 11) {
                Bundle bundle3 = new Bundle();
                bundle3.putLong("user_id", UserConfig.getInstance(this.currentAccount).getClientUserId());
                lambda$runLinkRequest$59(new ChatActivity(bundle3));
                this.drawerLayoutContainer.closeDrawer(false);
            } else if (id == 12) {
                int i4 = Build.VERSION.SDK_INT;
                if (i4 >= 23 && checkSelfPermission("android.permission.ACCESS_COARSE_LOCATION") != 0) {
                    lambda$runLinkRequest$59(new ActionIntroActivity(1));
                    this.drawerLayoutContainer.closeDrawer(false);
                    return;
                }
                if (i4 >= 28) {
                    z = ((LocationManager) ApplicationLoader.applicationContext.getSystemService("location")).isLocationEnabled();
                } else if (i4 >= 19) {
                    try {
                        if (Settings.Secure.getInt(ApplicationLoader.applicationContext.getContentResolver(), "location_mode", 0) == 0) {
                            z = false;
                        }
                    } catch (Throwable th) {
                        FileLog.e(th);
                    }
                }
                if (z) {
                    lambda$runLinkRequest$59(new PeopleNearbyActivity());
                } else {
                    lambda$runLinkRequest$59(new ActionIntroActivity(4));
                }
                this.drawerLayoutContainer.closeDrawer(false);
            } else {
                if (id != 13) {
                    return;
                }
                Browser.openUrl(this, LocaleController.getString("TelegramFeaturesUrl", 2131628565));
                this.drawerLayoutContainer.closeDrawer(false);
            }
        }
    }

    public /* synthetic */ void lambda$onCreate$2() {
        this.drawerLayoutContainer.closeDrawer(false);
    }

    /* renamed from: org.telegram.ui.LaunchActivity$8 */
    /* loaded from: classes3.dex */
    class AnonymousClass8 extends ItemTouchHelper.SimpleCallback {
        private RecyclerView.ViewHolder selectedViewHolder;

        @Override // androidx.recyclerview.widget.ItemTouchHelper.Callback
        public boolean isLongPressDragEnabled() {
            return false;
        }

        @Override // androidx.recyclerview.widget.ItemTouchHelper.Callback
        public void onSwiped(RecyclerView.ViewHolder viewHolder, int i) {
        }

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass8(int i, int i2) {
            super(i, i2);
            LaunchActivity.this = r1;
        }

        @Override // androidx.recyclerview.widget.ItemTouchHelper.Callback
        public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder viewHolder2) {
            if (viewHolder.getItemViewType() != viewHolder2.getItemViewType()) {
                return false;
            }
            LaunchActivity.this.drawerLayoutAdapter.swapElements(viewHolder.getAdapterPosition(), viewHolder2.getAdapterPosition());
            return true;
        }

        @Override // androidx.recyclerview.widget.ItemTouchHelper.Callback
        public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int i) {
            clearSelectedViewHolder();
            if (i != 0) {
                this.selectedViewHolder = viewHolder;
                View view = viewHolder.itemView;
                LaunchActivity.this.sideMenu.cancelClickRunnables(false);
                view.setBackgroundColor(Theme.getColor("dialogBackground"));
                if (Build.VERSION.SDK_INT < 21) {
                    return;
                }
                ObjectAnimator.ofFloat(view, "elevation", AndroidUtilities.dp(1.0f)).setDuration(150L).start();
            }
        }

        @Override // androidx.recyclerview.widget.ItemTouchHelper.Callback
        public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
            clearSelectedViewHolder();
        }

        private void clearSelectedViewHolder() {
            RecyclerView.ViewHolder viewHolder = this.selectedViewHolder;
            if (viewHolder != null) {
                View view = viewHolder.itemView;
                this.selectedViewHolder = null;
                view.setTranslationX(0.0f);
                view.setTranslationY(0.0f);
                if (Build.VERSION.SDK_INT < 21) {
                    return;
                }
                ObjectAnimator ofFloat = ObjectAnimator.ofFloat(view, "elevation", 0.0f);
                ofFloat.addListener(new AnonymousClass1(this, view));
                ofFloat.setDuration(150L).start();
            }
        }

        /* renamed from: org.telegram.ui.LaunchActivity$8$1 */
        /* loaded from: classes3.dex */
        public class AnonymousClass1 extends AnimatorListenerAdapter {
            final /* synthetic */ View val$view;

            AnonymousClass1(AnonymousClass8 anonymousClass8, View view) {
                this.val$view = view;
            }

            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationEnd(Animator animator) {
                this.val$view.setBackground(null);
            }
        }

        @Override // androidx.recyclerview.widget.ItemTouchHelper.Callback
        public void onChildDraw(Canvas canvas, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float f, float f2, int i, boolean z) {
            View view;
            View view2;
            View view3 = viewHolder.itemView;
            if (LaunchActivity.this.drawerLayoutAdapter.isAccountsShown()) {
                RecyclerView.ViewHolder findViewHolderForAdapterPosition = recyclerView.findViewHolderForAdapterPosition(LaunchActivity.this.drawerLayoutAdapter.getFirstAccountPosition() - 1);
                RecyclerView.ViewHolder findViewHolderForAdapterPosition2 = recyclerView.findViewHolderForAdapterPosition(LaunchActivity.this.drawerLayoutAdapter.getLastAccountPosition() + 1);
                if ((findViewHolderForAdapterPosition != null && (view2 = findViewHolderForAdapterPosition.itemView) != null && view2.getBottom() == view3.getTop() && f2 < 0.0f) || (findViewHolderForAdapterPosition2 != null && (view = findViewHolderForAdapterPosition2.itemView) != null && view.getTop() == view3.getBottom() && f2 > 0.0f)) {
                    f2 = 0.0f;
                }
            }
            view3.setTranslationX(f);
            view3.setTranslationY(f2);
        }
    }

    public /* synthetic */ boolean lambda$onCreate$4(ItemTouchHelper itemTouchHelper, View view, int i) {
        if (view instanceof DrawerUserCell) {
            int accountNumber = ((DrawerUserCell) view).getAccountNumber();
            if (accountNumber == this.currentAccount || AndroidUtilities.isTablet()) {
                itemTouchHelper.startDrag(this.sideMenu.getChildViewHolder(view));
                return false;
            }
            AnonymousClass9 anonymousClass9 = new AnonymousClass9(null, accountNumber);
            anonymousClass9.setCurrentAccount(accountNumber);
            this.actionBarLayout.presentFragmentAsPreview(anonymousClass9);
            this.drawerLayoutContainer.setDrawCurrentPreviewFragmentAbove(true);
            return true;
        }
        return false;
    }

    /* renamed from: org.telegram.ui.LaunchActivity$9 */
    /* loaded from: classes3.dex */
    public class AnonymousClass9 extends DialogsActivity {
        final /* synthetic */ int val$accountNumber;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass9(Bundle bundle, int i) {
            super(bundle);
            LaunchActivity.this = r1;
            this.val$accountNumber = i;
        }

        @Override // org.telegram.ui.DialogsActivity, org.telegram.ui.ActionBar.BaseFragment
        public void onTransitionAnimationEnd(boolean z, boolean z2) {
            super.onTransitionAnimationEnd(z, z2);
            if (z || !z2) {
                return;
            }
            LaunchActivity.this.drawerLayoutContainer.setDrawCurrentPreviewFragmentAbove(false);
        }

        @Override // org.telegram.ui.ActionBar.BaseFragment
        public void onPreviewOpenAnimationEnd() {
            super.onPreviewOpenAnimationEnd();
            LaunchActivity.this.drawerLayoutContainer.setAllowOpenDrawer(false, false);
            LaunchActivity.this.drawerLayoutContainer.setDrawCurrentPreviewFragmentAbove(false);
            LaunchActivity.this.switchToAccount(this.val$accountNumber, true);
        }
    }

    public /* synthetic */ void lambda$onCreate$5() {
        checkSystemBarColors(true, false);
    }

    public static /* synthetic */ void lambda$onCreate$6(View view) {
        int measuredHeight = view.getMeasuredHeight();
        FileLog.d("height = " + measuredHeight + " displayHeight = " + AndroidUtilities.displaySize.y);
        if (Build.VERSION.SDK_INT >= 21) {
            measuredHeight -= AndroidUtilities.statusBarHeight;
        }
        if (measuredHeight <= AndroidUtilities.dp(100.0f) || measuredHeight >= AndroidUtilities.displaySize.y) {
            return;
        }
        int dp = AndroidUtilities.dp(100.0f) + measuredHeight;
        Point point = AndroidUtilities.displaySize;
        if (dp <= point.y) {
            return;
        }
        point.y = measuredHeight;
        if (!BuildVars.LOGS_ENABLED) {
            return;
        }
        FileLog.d("fix display size y to " + AndroidUtilities.displaySize.y);
    }

    public void addOnUserLeaveHintListener(Runnable runnable) {
        this.onUserLeaveHintListeners.add(runnable);
    }

    public void removeOnUserLeaveHintListener(Runnable runnable) {
        this.onUserLeaveHintListeners.remove(runnable);
    }

    private BaseFragment getClientNotActivatedFragment() {
        if (LoginActivity.loadCurrentState(false).getInt("currentViewNum", 0) != 0) {
            return new LoginActivity();
        }
        return new IntroActivity();
    }

    public FireworksOverlay getFireworksOverlay() {
        return this.fireworksOverlay;
    }

    private void openSettings(boolean z) {
        Bundle bundle = new Bundle();
        bundle.putLong("user_id", UserConfig.getInstance(this.currentAccount).clientUserId);
        if (z) {
            bundle.putBoolean("expandPhoto", true);
        }
        lambda$runLinkRequest$59(new ProfileActivity(bundle));
        this.drawerLayoutContainer.closeDrawer(false);
    }

    private void checkSystemBarColors() {
        checkSystemBarColors(false, true, !this.isNavigationBarColorFrozen);
    }

    private void checkSystemBarColors(boolean z) {
        checkSystemBarColors(z, true, !this.isNavigationBarColorFrozen);
    }

    private void checkSystemBarColors(boolean z, boolean z2) {
        checkSystemBarColors(false, z, z2);
    }

    private void checkSystemBarColors(boolean z, boolean z2, boolean z3) {
        BaseFragment baseFragment;
        boolean z4;
        ArrayList<BaseFragment> arrayList;
        boolean z5 = true;
        if (!mainFragmentsStack.isEmpty()) {
            ArrayList<BaseFragment> arrayList2 = mainFragmentsStack;
            baseFragment = arrayList2.get(arrayList2.size() - 1);
        } else {
            baseFragment = null;
        }
        if (baseFragment != null && (baseFragment.isRemovingFromStack() || baseFragment.isInPreviewMode())) {
            if (mainFragmentsStack.size() > 1) {
                baseFragment = mainFragmentsStack.get(arrayList.size() - 2);
            } else {
                baseFragment = null;
            }
        }
        boolean z6 = baseFragment != null && baseFragment.hasForceLightStatusBar();
        int i = Build.VERSION.SDK_INT;
        if (i >= 23) {
            if (z2) {
                if (baseFragment != null) {
                    z4 = baseFragment.isLightStatusBar();
                } else {
                    z4 = ColorUtils.calculateLuminance(Theme.getColor("actionBarDefault", null, true)) > 0.699999988079071d;
                }
                AndroidUtilities.setLightStatusBar(getWindow(), z4, z6);
            }
            if (i >= 26 && z3 && (!z || baseFragment == null || !baseFragment.isInPreviewMode())) {
                Window window = getWindow();
                int color = (baseFragment == null || !z) ? Theme.getColor("windowBackgroundGray", null, true) : baseFragment.getNavigationBarColor();
                if (window.getNavigationBarColor() != color) {
                    window.setNavigationBarColor(color);
                    float computePerceivedBrightness = AndroidUtilities.computePerceivedBrightness(color);
                    Window window2 = getWindow();
                    if (computePerceivedBrightness < 0.721f) {
                        z5 = false;
                    }
                    AndroidUtilities.setLightNavigationBar(window2, z5);
                }
            }
        }
        if ((SharedConfig.noStatusBar || z6) && i >= 21 && z2) {
            getWindow().setStatusBarColor(0);
        }
    }

    public static /* synthetic */ DialogsActivity lambda$switchToAccount$7(Void r1) {
        return new DialogsActivity(null);
    }

    public void switchToAccount(int i, boolean z) {
        switchToAccount(i, z, LaunchActivity$$ExternalSyntheticLambda65.INSTANCE);
    }

    public void switchToAccount(int i, boolean z, GenericProvider<Void, DialogsActivity> genericProvider) {
        if (i == UserConfig.selectedAccount || !UserConfig.isValidAccount(i)) {
            return;
        }
        ConnectionsManager.getInstance(this.currentAccount).setAppPaused(true, false);
        UserConfig.selectedAccount = i;
        UserConfig.getInstance(0).saveConfig(false);
        checkCurrentAccount();
        if (AndroidUtilities.isTablet()) {
            this.layersActionBarLayout.removeAllFragments();
            this.rightActionBarLayout.removeAllFragments();
            if (!this.tabletFullSize) {
                this.shadowTabletSide.setVisibility(0);
                if (this.rightActionBarLayout.fragmentsStack.isEmpty()) {
                    this.backgroundTablet.setVisibility(0);
                }
                this.rightActionBarLayout.setVisibility(8);
            }
            this.layersActionBarLayout.setVisibility(8);
        }
        if (z) {
            this.actionBarLayout.removeAllFragments();
        } else {
            this.actionBarLayout.removeFragmentFromStack(0);
        }
        DialogsActivity provide = genericProvider.provide(null);
        provide.setSideMenu(this.sideMenu);
        this.actionBarLayout.addFragmentToStack(provide, 0);
        this.drawerLayoutContainer.setAllowOpenDrawer(true, false);
        this.actionBarLayout.showLastFragment();
        if (AndroidUtilities.isTablet()) {
            this.layersActionBarLayout.showLastFragment();
            this.rightActionBarLayout.showLastFragment();
        }
        if (!ApplicationLoader.mainInterfacePaused) {
            ConnectionsManager.getInstance(this.currentAccount).setAppPaused(false, false);
        }
        if (UserConfig.getInstance(i).unacceptedTermsOfService != null) {
            showTosActivity(i, UserConfig.getInstance(i).unacceptedTermsOfService);
        }
        updateCurrentConnectionState(this.currentAccount);
    }

    private void switchToAvailableAccountOrLogout() {
        int i = 0;
        while (true) {
            if (i >= 4) {
                i = -1;
                break;
            } else if (UserConfig.getInstance(i).isClientActivated()) {
                break;
            } else {
                i++;
            }
        }
        TermsOfServiceView termsOfServiceView = this.termsOfServiceView;
        if (termsOfServiceView != null) {
            termsOfServiceView.setVisibility(8);
        }
        if (i != -1) {
            switchToAccount(i, true);
            return;
        }
        DrawerLayoutAdapter drawerLayoutAdapter = this.drawerLayoutAdapter;
        if (drawerLayoutAdapter != null) {
            drawerLayoutAdapter.notifyDataSetChanged();
        }
        clearFragments();
        this.actionBarLayout.rebuildLogout();
        if (AndroidUtilities.isTablet()) {
            this.layersActionBarLayout.rebuildLogout();
            this.rightActionBarLayout.rebuildLogout();
        }
        lambda$runLinkRequest$59(new IntroActivity().setOnLogout());
    }

    public static void clearFragments() {
        Iterator<BaseFragment> it = mainFragmentsStack.iterator();
        while (it.hasNext()) {
            it.next().onFragmentDestroy();
        }
        mainFragmentsStack.clear();
        if (AndroidUtilities.isTablet()) {
            Iterator<BaseFragment> it2 = layerFragmentsStack.iterator();
            while (it2.hasNext()) {
                it2.next().onFragmentDestroy();
            }
            layerFragmentsStack.clear();
            Iterator<BaseFragment> it3 = rightFragmentsStack.iterator();
            while (it3.hasNext()) {
                it3.next().onFragmentDestroy();
            }
            rightFragmentsStack.clear();
        }
    }

    public int getMainFragmentsCount() {
        return mainFragmentsStack.size();
    }

    private void checkCurrentAccount() {
        int i = this.currentAccount;
        if (i != UserConfig.selectedAccount) {
            NotificationCenter.getInstance(i).removeObserver(this, NotificationCenter.appDidLogout);
            NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.mainUserInfoChanged);
            NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.didUpdateConnectionState);
            NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.needShowAlert);
            NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.wasUnableToFindCurrentLocation);
            NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.openArticle);
            NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.hasNewContactsToImport);
            NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.needShowPlayServicesAlert);
            NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.fileLoaded);
            NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.fileLoadProgressChanged);
            NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.fileLoadFailed);
            NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.historyImportProgressChanged);
            NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.groupCallUpdated);
            NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.stickersImportComplete);
            NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.newSuggestionsAvailable);
        }
        int i2 = UserConfig.selectedAccount;
        this.currentAccount = i2;
        NotificationCenter.getInstance(i2).addObserver(this, NotificationCenter.appDidLogout);
        NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.mainUserInfoChanged);
        NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.didUpdateConnectionState);
        NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.needShowAlert);
        NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.wasUnableToFindCurrentLocation);
        NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.openArticle);
        NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.hasNewContactsToImport);
        NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.needShowPlayServicesAlert);
        NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.fileLoaded);
        NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.fileLoadProgressChanged);
        NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.fileLoadFailed);
        NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.historyImportProgressChanged);
        NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.groupCallUpdated);
        NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.stickersImportComplete);
        NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.newSuggestionsAvailable);
        NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.currentUserShowLimitReachedDialog);
    }

    private void checkLayout() {
        if (!AndroidUtilities.isTablet() || this.rightActionBarLayout == null) {
            return;
        }
        int i = 0;
        if (!AndroidUtilities.isInMultiwindow && (!AndroidUtilities.isSmallTablet() || getResources().getConfiguration().orientation == 2)) {
            this.tabletFullSize = false;
            if (this.actionBarLayout.fragmentsStack.size() >= 2) {
                while (1 < this.actionBarLayout.fragmentsStack.size()) {
                    BaseFragment baseFragment = this.actionBarLayout.fragmentsStack.get(1);
                    if (baseFragment instanceof ChatActivity) {
                        ((ChatActivity) baseFragment).setIgnoreAttachOnPause(true);
                    }
                    baseFragment.onPause();
                    this.actionBarLayout.fragmentsStack.remove(1);
                    this.rightActionBarLayout.fragmentsStack.add(baseFragment);
                }
                PasscodeView passcodeView = this.passcodeView;
                if (passcodeView == null || passcodeView.getVisibility() != 0) {
                    this.actionBarLayout.showLastFragment();
                    this.rightActionBarLayout.showLastFragment();
                }
            }
            ActionBarLayout actionBarLayout = this.rightActionBarLayout;
            actionBarLayout.setVisibility(actionBarLayout.fragmentsStack.isEmpty() ? 8 : 0);
            this.backgroundTablet.setVisibility(this.rightActionBarLayout.fragmentsStack.isEmpty() ? 0 : 8);
            FrameLayout frameLayout = this.shadowTabletSide;
            if (this.actionBarLayout.fragmentsStack.isEmpty()) {
                i = 8;
            }
            frameLayout.setVisibility(i);
            return;
        }
        this.tabletFullSize = true;
        if (!this.rightActionBarLayout.fragmentsStack.isEmpty()) {
            while (this.rightActionBarLayout.fragmentsStack.size() > 0) {
                BaseFragment baseFragment2 = this.rightActionBarLayout.fragmentsStack.get(0);
                if (baseFragment2 instanceof ChatActivity) {
                    ((ChatActivity) baseFragment2).setIgnoreAttachOnPause(true);
                }
                baseFragment2.onPause();
                this.rightActionBarLayout.fragmentsStack.remove(0);
                this.actionBarLayout.fragmentsStack.add(baseFragment2);
            }
            PasscodeView passcodeView2 = this.passcodeView;
            if (passcodeView2 == null || passcodeView2.getVisibility() != 0) {
                this.actionBarLayout.showLastFragment();
            }
        }
        this.shadowTabletSide.setVisibility(8);
        this.rightActionBarLayout.setVisibility(8);
        SizeNotifierFrameLayout sizeNotifierFrameLayout = this.backgroundTablet;
        if (!this.actionBarLayout.fragmentsStack.isEmpty()) {
            i = 8;
        }
        sizeNotifierFrameLayout.setVisibility(i);
    }

    /* renamed from: org.telegram.ui.LaunchActivity$10 */
    /* loaded from: classes3.dex */
    public class AnonymousClass10 extends BlockingUpdateView {
        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass10(Context context) {
            super(context);
            LaunchActivity.this = r1;
        }

        @Override // org.telegram.ui.Components.BlockingUpdateView, android.view.View
        public void setVisibility(int i) {
            super.setVisibility(i);
            if (i == 8) {
                LaunchActivity.this.drawerLayoutContainer.setAllowOpenDrawer(true, false);
            }
        }
    }

    private void showUpdateActivity(int i, TLRPC$TL_help_appUpdate tLRPC$TL_help_appUpdate, boolean z) {
        if (this.blockingUpdateView == null) {
            AnonymousClass10 anonymousClass10 = new AnonymousClass10(this);
            this.blockingUpdateView = anonymousClass10;
            this.drawerLayoutContainer.addView(anonymousClass10, LayoutHelper.createFrame(-1, -1.0f));
        }
        this.blockingUpdateView.show(i, tLRPC$TL_help_appUpdate, z);
        this.drawerLayoutContainer.setAllowOpenDrawer(false, false);
    }

    private void showTosActivity(int i, TLRPC$TL_help_termsOfService tLRPC$TL_help_termsOfService) {
        if (this.termsOfServiceView == null) {
            TermsOfServiceView termsOfServiceView = new TermsOfServiceView(this);
            this.termsOfServiceView = termsOfServiceView;
            termsOfServiceView.setAlpha(0.0f);
            this.drawerLayoutContainer.addView(this.termsOfServiceView, LayoutHelper.createFrame(-1, -1.0f));
            this.termsOfServiceView.setDelegate(new AnonymousClass11());
        }
        TLRPC$TL_help_termsOfService tLRPC$TL_help_termsOfService2 = UserConfig.getInstance(i).unacceptedTermsOfService;
        if (tLRPC$TL_help_termsOfService2 != tLRPC$TL_help_termsOfService && (tLRPC$TL_help_termsOfService2 == null || !tLRPC$TL_help_termsOfService2.id.data.equals(tLRPC$TL_help_termsOfService.id.data))) {
            UserConfig.getInstance(i).unacceptedTermsOfService = tLRPC$TL_help_termsOfService;
            UserConfig.getInstance(i).saveConfig(false);
        }
        this.termsOfServiceView.show(i, tLRPC$TL_help_termsOfService);
        this.drawerLayoutContainer.setAllowOpenDrawer(false, false);
        this.termsOfServiceView.animate().alpha(1.0f).setDuration(150L).setInterpolator(AndroidUtilities.decelerateInterpolator).setListener(null).start();
    }

    /* renamed from: org.telegram.ui.LaunchActivity$11 */
    /* loaded from: classes3.dex */
    public class AnonymousClass11 implements TermsOfServiceView.TermsOfServiceViewDelegate {
        AnonymousClass11() {
            LaunchActivity.this = r1;
        }

        @Override // org.telegram.ui.Components.TermsOfServiceView.TermsOfServiceViewDelegate
        public void onAcceptTerms(int i) {
            UserConfig.getInstance(i).unacceptedTermsOfService = null;
            UserConfig.getInstance(i).saveConfig(false);
            LaunchActivity.this.drawerLayoutContainer.setAllowOpenDrawer(true, false);
            if (LaunchActivity.mainFragmentsStack.size() > 0) {
                ((BaseFragment) LaunchActivity.mainFragmentsStack.get(LaunchActivity.mainFragmentsStack.size() - 1)).onResume();
            }
            LaunchActivity.this.termsOfServiceView.animate().alpha(0.0f).setDuration(150L).setInterpolator(AndroidUtilities.accelerateInterpolator).withEndAction(new LaunchActivity$11$$ExternalSyntheticLambda0(this)).start();
        }

        public /* synthetic */ void lambda$onAcceptTerms$0() {
            LaunchActivity.this.termsOfServiceView.setVisibility(8);
        }
    }

    public void showPasscodeActivity(boolean z, boolean z2, int i, int i2, Runnable runnable, Runnable runnable2) {
        if (this.drawerLayoutContainer == null) {
            return;
        }
        if (this.passcodeView == null) {
            PasscodeView passcodeView = new PasscodeView(this);
            this.passcodeView = passcodeView;
            this.drawerLayoutContainer.addView(passcodeView, LayoutHelper.createFrame(-1, -1.0f));
        }
        SharedConfig.appLocked = true;
        if (SecretMediaViewer.hasInstance() && SecretMediaViewer.getInstance().isVisible()) {
            SecretMediaViewer.getInstance().closePhoto(false, false);
        } else if (PhotoViewer.hasInstance() && PhotoViewer.getInstance().isVisible()) {
            PhotoViewer.getInstance().closePhoto(false, true);
        } else if (ArticleViewer.hasInstance() && ArticleViewer.getInstance().isVisible()) {
            ArticleViewer.getInstance().close(false, true);
        }
        MessageObject playingMessageObject = MediaController.getInstance().getPlayingMessageObject();
        if (playingMessageObject != null && playingMessageObject.isRoundVideo()) {
            MediaController.getInstance().cleanupPlayer(true, true);
        }
        this.passcodeView.onShow(z, z2, i, i2, new LaunchActivity$$ExternalSyntheticLambda37(this, runnable), runnable2);
        SharedConfig.isWaitingForPasscodeEnter = true;
        this.drawerLayoutContainer.setAllowOpenDrawer(false, false);
        this.passcodeView.setDelegate(new LaunchActivity$$ExternalSyntheticLambda93(this));
    }

    public /* synthetic */ void lambda$showPasscodeActivity$8(Runnable runnable) {
        this.actionBarLayout.setVisibility(4);
        if (AndroidUtilities.isTablet()) {
            if (this.layersActionBarLayout.getVisibility() == 0) {
                this.layersActionBarLayout.setVisibility(4);
            }
            this.rightActionBarLayout.setVisibility(4);
        }
        if (runnable != null) {
            runnable.run();
        }
    }

    public /* synthetic */ void lambda$showPasscodeActivity$9() {
        SharedConfig.isWaitingForPasscodeEnter = false;
        Intent intent = this.passcodeSaveIntent;
        if (intent != null) {
            handleIntent(intent, this.passcodeSaveIntentIsNew, this.passcodeSaveIntentIsRestore, true);
            this.passcodeSaveIntent = null;
        }
        this.drawerLayoutContainer.setAllowOpenDrawer(true, false);
        this.actionBarLayout.setVisibility(0);
        this.actionBarLayout.showLastFragment();
        if (AndroidUtilities.isTablet()) {
            this.layersActionBarLayout.showLastFragment();
            this.rightActionBarLayout.showLastFragment();
            if (this.layersActionBarLayout.getVisibility() == 4) {
                this.layersActionBarLayout.setVisibility(0);
            }
            this.rightActionBarLayout.setVisibility(0);
        }
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Code restructure failed: missing block: B:166:0x0326, code lost:
        if (r75.sendingText == null) goto L78;
     */
    /* JADX WARN: Code restructure failed: missing block: B:417:0x0938, code lost:
        if (r7 == 0) goto L418;
     */
    /* JADX WARN: Code restructure failed: missing block: B:437:0x09c8, code lost:
        if (r1.intValue() == 0) goto L438;
     */
    /* JADX WARN: Code restructure failed: missing block: B:64:0x0153, code lost:
        if (r1.equals(r0) != false) goto L66;
     */
    /* JADX WARN: Code restructure failed: missing block: B:785:0x1844, code lost:
        if (r3 == 0) goto L787;
     */
    /* JADX WARN: Code restructure failed: missing block: B:957:0x1dfa, code lost:
        if (r1.checkCanOpenChat(r0, r2.get(r2.size() - r5)) != false) goto L958;
     */
    /* JADX WARN: Code restructure failed: missing block: B:974:0x1e71, code lost:
        if (r1.checkCanOpenChat(r0, r2.get(r2.size() - r5)) != false) goto L975;
     */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:1050:0x1fd1  */
    /* JADX WARN: Removed duplicated region for block: B:1051:0x1fde  */
    /* JADX WARN: Removed duplicated region for block: B:1054:0x1fec  */
    /* JADX WARN: Removed duplicated region for block: B:1055:0x1ffd  */
    /* JADX WARN: Removed duplicated region for block: B:1120:0x2236 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:1123:0x223e  */
    /* JADX WARN: Removed duplicated region for block: B:1134:0x2289  */
    /* JADX WARN: Removed duplicated region for block: B:1145:0x22d4  */
    /* JADX WARN: Removed duplicated region for block: B:1147:0x22e0  */
    /* JADX WARN: Removed duplicated region for block: B:1149:0x22e8  */
    /* JADX WARN: Removed duplicated region for block: B:1173:0x137a A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:1182:0x1a95 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:165:0x0324  */
    /* JADX WARN: Removed duplicated region for block: B:169:0x032d  */
    /* JADX WARN: Removed duplicated region for block: B:197:0x03c6  */
    /* JADX WARN: Removed duplicated region for block: B:264:0x04f3  */
    /* JADX WARN: Removed duplicated region for block: B:334:0x06c1  */
    /* JADX WARN: Removed duplicated region for block: B:374:0x07b3 A[Catch: Exception -> 0x07c1, TRY_LEAVE, TryCatch #26 {Exception -> 0x07c1, blocks: (B:372:0x07a7, B:374:0x07b3), top: B:1218:0x07a7 }] */
    /* JADX WARN: Removed duplicated region for block: B:375:0x07c0  */
    /* JADX WARN: Removed duplicated region for block: B:63:0x014f  */
    /* JADX WARN: Removed duplicated region for block: B:779:0x17b5 A[Catch: Exception -> 0x17c3, TRY_LEAVE, TryCatch #25 {Exception -> 0x17c3, blocks: (B:777:0x17a9, B:779:0x17b5), top: B:1216:0x17a9 }] */
    /* JADX WARN: Removed duplicated region for block: B:803:0x1973  */
    /* JADX WARN: Removed duplicated region for block: B:86:0x01bd  */
    /* JADX WARN: Removed duplicated region for block: B:885:0x1b63  */
    /* JADX WARN: Removed duplicated region for block: B:886:0x1b73  */
    /* JADX WARN: Removed duplicated region for block: B:935:0x1d93  */
    /* JADX WARN: Removed duplicated region for block: B:949:0x1dc6  */
    /* JADX WARN: Removed duplicated region for block: B:94:0x01e8  */
    /* JADX WARN: Removed duplicated region for block: B:966:0x1e3e  */
    /* JADX WARN: Removed duplicated region for block: B:99:0x01f6  */
    /* JADX WARN: Type inference failed for: r0v55, types: [org.telegram.ui.ActionBar.DrawerLayoutContainer] */
    /* JADX WARN: Type inference failed for: r0v68, types: [org.telegram.ui.ActionBar.DrawerLayoutContainer] */
    /* JADX WARN: Type inference failed for: r0v72, types: [android.os.Bundle] */
    /* JADX WARN: Type inference failed for: r0v77, types: [org.telegram.ui.ActionBar.DrawerLayoutContainer] */
    /* JADX WARN: Type inference failed for: r0v81, types: [android.os.Bundle] */
    /* JADX WARN: Type inference failed for: r0v83, types: [org.telegram.ui.ActionBar.DrawerLayoutContainer] */
    /* JADX WARN: Type inference failed for: r0v88, types: [org.telegram.ui.ActionBar.DrawerLayoutContainer] */
    /* JADX WARN: Type inference failed for: r10v2, types: [android.os.Bundle, java.lang.String] */
    /* JADX WARN: Type inference failed for: r10v6 */
    /* JADX WARN: Type inference failed for: r10v7 */
    /* JADX WARN: Type inference failed for: r10v71 */
    /* JADX WARN: Type inference failed for: r10v80 */
    /* JADX WARN: Type inference failed for: r1v157, types: [org.telegram.tgnet.TLRPC$TL_wallPaper, org.telegram.tgnet.TLRPC$WallPaper] */
    /* JADX WARN: Type inference failed for: r1v377, types: [org.telegram.tgnet.TLRPC$TL_wallPaper, org.telegram.tgnet.TLRPC$WallPaper] */
    /* JADX WARN: Type inference failed for: r2v108, types: [java.lang.Long] */
    /* JADX WARN: Type inference failed for: r3v198, types: [java.lang.Long] */
    /* JADX WARN: Type inference failed for: r5v11 */
    /* JADX WARN: Type inference failed for: r5v14 */
    /* JADX WARN: Type inference failed for: r5v15 */
    /* JADX WARN: Type inference failed for: r5v2, types: [boolean, int] */
    /* JADX WARN: Type inference failed for: r5v9 */
    /* JADX WARN: Type inference failed for: r76v0, types: [android.content.Intent] */
    @SuppressLint({"Range"})
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private boolean handleIntent(Intent intent, boolean z, boolean z2, boolean z3) {
        String str;
        String str2;
        long j;
        boolean z4;
        boolean z5;
        boolean z6;
        boolean z7;
        boolean z8;
        boolean z9;
        boolean z10;
        boolean z11;
        boolean z12;
        long j2;
        boolean z13;
        boolean z14;
        long j3;
        int[] iArr;
        char c;
        int i;
        String str3;
        boolean z15;
        LaunchActivity launchActivity;
        String str4;
        String str5;
        int i2;
        long j4;
        int i3;
        int i4;
        boolean z16;
        ?? r10;
        boolean z17;
        boolean z18;
        GroupCallActivity groupCallActivity;
        boolean z19;
        boolean z20;
        String str6;
        ?? r5;
        String str7;
        ActionIntroActivity actionIntroActivity;
        boolean z21;
        char c2;
        BaseFragment filtersSetupActivity;
        ProfileActivity profileActivity;
        boolean z22;
        boolean z23;
        ArrayList parcelableArrayListExtra;
        String type;
        ArrayList arrayList;
        boolean z24;
        Pattern compile;
        char c3;
        int i5;
        boolean z25;
        String str8;
        int[] iArr2;
        long j5;
        String str9;
        String str10;
        String str11;
        boolean z26;
        String str12;
        char c4;
        int i6;
        long j6;
        String str13;
        String str14;
        String str15;
        int i7;
        String str16;
        String str17;
        String str18;
        String str19;
        String str20;
        String str21;
        String str22;
        long j7;
        String str23;
        String str24;
        HashMap<String, String> hashMap;
        String str25;
        char c5;
        int i8;
        String str26;
        String str27;
        String str28;
        int[] iArr3;
        Integer num;
        Integer num2;
        String str29;
        String str30;
        long j8;
        Integer num3;
        boolean z27;
        String str31;
        String str32;
        String str33;
        String str34;
        String str35;
        String str36;
        String str37;
        TLRPC$TL_account_sendConfirmPhoneCode tLRPC$TL_account_sendConfirmPhoneCode;
        Exception e;
        Cursor query;
        Throwable th;
        boolean z28;
        char c6;
        String str38;
        HashMap<String, String> hashMap2;
        Integer num4;
        Integer num5;
        String str39;
        String str40;
        String str41;
        String str42;
        String str43;
        String str44;
        Integer num6;
        Uri uri;
        String str45;
        String str46;
        String str47;
        boolean z29;
        String queryParameter;
        String queryParameter2;
        String[] split;
        boolean z30;
        int i9;
        String str48;
        long j9;
        long j10;
        int parseInt;
        String str49;
        long j11;
        String str50;
        boolean z31;
        boolean z32;
        boolean z33;
        boolean z34;
        String str51;
        String str52;
        String str53;
        String str54;
        Integer num7;
        Integer num8;
        String str55;
        Integer num9;
        String str56;
        String str57;
        String str58;
        String str59;
        String str60;
        String str61;
        String str62;
        String str63;
        String str64;
        String str65;
        String str66;
        String str67;
        String str68;
        String str69;
        String str70;
        int i10;
        boolean z35;
        String str71;
        int i11;
        String str72;
        int i12;
        Integer num10;
        String str73;
        String str74;
        Integer num11;
        Integer num12;
        String str75;
        boolean z36;
        String queryParameter3;
        String queryParameter4;
        String[] split2;
        String substring;
        String type2;
        boolean z37;
        String stringExtra;
        Parcelable parcelableExtra;
        Pattern compile2;
        CharSequence charSequenceExtra;
        String str76;
        Throwable th2;
        String str77;
        if (AndroidUtilities.handleProxyIntent(this, intent)) {
            this.actionBarLayout.showLastFragment();
            if (AndroidUtilities.isTablet()) {
                this.layersActionBarLayout.showLastFragment();
                this.rightActionBarLayout.showLastFragment();
            }
            return true;
        }
        if (PhotoViewer.hasInstance() && PhotoViewer.getInstance().isVisible() && (intent == 0 || !"android.intent.action.MAIN".equals(intent.getAction()))) {
            PhotoViewer.getInstance().closePhoto(false, true);
        }
        int flags = intent.getFlags();
        String action = intent.getAction();
        int[] iArr4 = {intent.getIntExtra("currentAccount", UserConfig.selectedAccount)};
        switchToAccount(iArr4[0], true);
        boolean z38 = action != null && action.equals("voip");
        if (!z3 && (AndroidUtilities.needShowPasscode(true) || SharedConfig.isWaitingForPasscodeEnter)) {
            showPasscodeActivity(true, false, -1, -1, null, null);
            UserConfig.getInstance(this.currentAccount).saveConfig(false);
            if (!z38) {
                this.passcodeSaveIntent = intent;
                this.passcodeSaveIntentIsNew = z;
                this.passcodeSaveIntentIsRestore = z2;
                return false;
            }
        }
        this.photoPathsArray = null;
        this.videoPath = null;
        this.sendingText = null;
        this.documentsPathsArray = null;
        this.documentsOriginalPathsArray = null;
        this.documentsMimeType = null;
        this.documentsUrisArray = null;
        this.exportingChatUri = null;
        this.contactsToSend = null;
        this.contactsToSendUri = null;
        this.importingStickers = null;
        this.importingStickersEmoji = null;
        this.importingStickersSoftware = null;
        if ((1048576 & flags) == 0 && intent.getAction() != null && !z2) {
            String str78 = "";
            if ("android.intent.action.SEND".equals(intent.getAction())) {
                if (SharedConfig.directShare && intent.getExtras() != null) {
                    j2 = intent.getExtras().getLong("dialogId", 0L);
                    if (j2 == 0) {
                        try {
                            String string = intent.getExtras().getString("android.intent.extra.shortcut.ID");
                            if (string != null) {
                                List<ShortcutInfoCompat> dynamicShortcuts = ShortcutManagerCompat.getDynamicShortcuts(ApplicationLoader.applicationContext);
                                int size = dynamicShortcuts.size();
                                for (int i13 = 0; i13 < size; i13++) {
                                    ShortcutInfoCompat shortcutInfoCompat = dynamicShortcuts.get(i13);
                                    if (string.equals(shortcutInfoCompat.getId())) {
                                        Bundle extras = shortcutInfoCompat.getIntent().getExtras();
                                        long j12 = extras.getLong("dialogId", 0L);
                                        try {
                                            str76 = extras.getString("hash", null);
                                            j2 = j12;
                                            break;
                                        } catch (Throwable th3) {
                                            th2 = th3;
                                            j2 = j12;
                                            FileLog.e(th2);
                                            str76 = null;
                                            str77 = SharedConfig.directShareHash;
                                            if (str77 != null) {
                                            }
                                            j2 = 0;
                                            type2 = intent.getType();
                                            if (type2 == null) {
                                            }
                                            stringExtra = intent.getStringExtra("android.intent.extra.TEXT");
                                            if (stringExtra == null) {
                                                stringExtra = charSequenceExtra.toString();
                                            }
                                            String stringExtra2 = intent.getStringExtra("android.intent.extra.SUBJECT");
                                            if (TextUtils.isEmpty(stringExtra)) {
                                            }
                                            parcelableExtra = intent.getParcelableExtra("android.intent.extra.STREAM");
                                            if (parcelableExtra == null) {
                                            }
                                        }
                                    }
                                }
                            }
                        } catch (Throwable th4) {
                            th2 = th4;
                        }
                        str76 = null;
                    } else {
                        str76 = intent.getExtras().getString("hash", null);
                    }
                    str77 = SharedConfig.directShareHash;
                    if (str77 != null) {
                    }
                }
                j2 = 0;
                type2 = intent.getType();
                if (type2 == null && type2.equals("text/x-vcard")) {
                    try {
                        Uri uri2 = (Uri) intent.getExtras().get("android.intent.extra.STREAM");
                        if (uri2 != null) {
                            ArrayList<TLRPC$User> loadVCardFromStream = AndroidUtilities.loadVCardFromStream(uri2, this.currentAccount, false, null, null);
                            this.contactsToSend = loadVCardFromStream;
                            if (loadVCardFromStream.size() > 5) {
                                this.contactsToSend = null;
                                ArrayList<Uri> arrayList2 = new ArrayList<>();
                                this.documentsUrisArray = arrayList2;
                                arrayList2.add(uri2);
                                this.documentsMimeType = type2;
                            } else {
                                this.contactsToSendUri = uri2;
                            }
                        }
                    } catch (Exception e2) {
                        FileLog.e(e2);
                    }
                    z37 = true;
                    if (z37) {
                    }
                    j4 = 0;
                    j3 = 0;
                    j = 0;
                    str4 = "message_id";
                    iArr = iArr4;
                    launchActivity = this;
                    str = null;
                    i4 = -1;
                    i3 = 0;
                    i2 = -1;
                    str5 = null;
                    z15 = false;
                    str3 = null;
                    i = 0;
                    c = 0;
                    z14 = false;
                    z13 = false;
                    z12 = false;
                    z11 = false;
                    z10 = false;
                    z9 = false;
                    z8 = false;
                    z7 = false;
                    z6 = false;
                    z5 = false;
                    z4 = false;
                    str2 = null;
                    if (UserConfig.getInstance(launchActivity.currentAccount).isClientActivated()) {
                    }
                    z17 = true;
                    z20 = false;
                    z18 = z;
                    z19 = z20;
                    z16 = false;
                    r10 = z19;
                    if (!z16) {
                    }
                    if (z38) {
                    }
                    if (!z10) {
                    }
                    intent.setAction(r10);
                    return z16;
                }
                stringExtra = intent.getStringExtra("android.intent.extra.TEXT");
                if (stringExtra == null && (charSequenceExtra = intent.getCharSequenceExtra("android.intent.extra.TEXT")) != null) {
                    stringExtra = charSequenceExtra.toString();
                }
                String stringExtra22 = intent.getStringExtra("android.intent.extra.SUBJECT");
                if (TextUtils.isEmpty(stringExtra)) {
                    if ((stringExtra.startsWith("http://") || stringExtra.startsWith("https://")) && !TextUtils.isEmpty(stringExtra22)) {
                        stringExtra = stringExtra22 + "\n" + stringExtra;
                    }
                    this.sendingText = stringExtra;
                } else if (!TextUtils.isEmpty(stringExtra22)) {
                    this.sendingText = stringExtra22;
                }
                parcelableExtra = intent.getParcelableExtra("android.intent.extra.STREAM");
                if (parcelableExtra == null) {
                    boolean z39 = parcelableExtra instanceof Uri;
                    Uri uri3 = parcelableExtra;
                    if (!z39) {
                        uri3 = Uri.parse(parcelableExtra.toString());
                    }
                    Uri uri4 = (Uri) uri3;
                    boolean z40 = uri4 != null && AndroidUtilities.isInternalUri(uri4);
                    if (!z40 && uri4 != null) {
                        if ((type2 != null && type2.startsWith("image/")) || uri4.toString().toLowerCase().endsWith(".jpg")) {
                            if (this.photoPathsArray == null) {
                                this.photoPathsArray = new ArrayList<>();
                            }
                            SendMessagesHelper.SendingMediaInfo sendingMediaInfo = new SendMessagesHelper.SendingMediaInfo();
                            sendingMediaInfo.uri = uri4;
                            this.photoPathsArray.add(sendingMediaInfo);
                        } else {
                            String uri5 = uri4.toString();
                            if (j2 == 0 && uri5 != null) {
                                if (BuildVars.LOGS_ENABLED) {
                                    FileLog.d("export path = " + uri5);
                                }
                                Set<String> set = MessagesController.getInstance(iArr4[0]).exportUri;
                                String fixFileName = FileLoader.fixFileName(MediaController.getFileName(uri4));
                                for (String str79 : set) {
                                    try {
                                        compile2 = Pattern.compile(str79);
                                    } catch (Exception e3) {
                                        FileLog.e(e3);
                                    }
                                    if (compile2.matcher(uri5).find() || compile2.matcher(fixFileName).find()) {
                                        this.exportingChatUri = uri4;
                                        break;
                                    }
                                }
                                if (this.exportingChatUri == null && uri5.startsWith("content://com.kakao.talk") && uri5.endsWith("KakaoTalkChats.txt")) {
                                    this.exportingChatUri = uri4;
                                }
                            }
                            if (this.exportingChatUri == null) {
                                String path = AndroidUtilities.getPath(uri4);
                                if (!BuildVars.NO_SCOPED_STORAGE) {
                                    path = MediaController.copyFileToCache(uri4, "file");
                                }
                                if (path != null) {
                                    if (path.startsWith("file:")) {
                                        path = path.replace("file://", str78);
                                    }
                                    if (type2 != null && type2.startsWith("video/")) {
                                        this.videoPath = path;
                                    } else {
                                        if (this.documentsPathsArray == null) {
                                            this.documentsPathsArray = new ArrayList<>();
                                            this.documentsOriginalPathsArray = new ArrayList<>();
                                        }
                                        this.documentsPathsArray.add(path);
                                        this.documentsOriginalPathsArray.add(uri4.toString());
                                    }
                                } else {
                                    if (this.documentsUrisArray == null) {
                                        this.documentsUrisArray = new ArrayList<>();
                                    }
                                    this.documentsUrisArray.add(uri4);
                                    this.documentsMimeType = type2;
                                }
                            }
                        }
                    }
                    z37 = z40;
                    if (z37) {
                        Toast.makeText(this, "Unsupported content", 0).show();
                    }
                    j4 = 0;
                    j3 = 0;
                    j = 0;
                    str4 = "message_id";
                    iArr = iArr4;
                    launchActivity = this;
                    str = null;
                    i4 = -1;
                    i3 = 0;
                    i2 = -1;
                    str5 = null;
                    z15 = false;
                    str3 = null;
                    i = 0;
                    c = 0;
                    z14 = false;
                    z13 = false;
                    z12 = false;
                    z11 = false;
                    z10 = false;
                    z9 = false;
                    z8 = false;
                    z7 = false;
                    z6 = false;
                    z5 = false;
                    z4 = false;
                    str2 = null;
                    if (UserConfig.getInstance(launchActivity.currentAccount).isClientActivated()) {
                        if (str3 != null) {
                            BaseFragment lastFragment = launchActivity.actionBarLayout.getLastFragment();
                            str6 = str5;
                            if (lastFragment instanceof DialogsActivity) {
                                DialogsActivity dialogsActivity = (DialogsActivity) lastFragment;
                                if (dialogsActivity.isMainDialogList()) {
                                    if (dialogsActivity.getFragmentView() != null) {
                                        r5 = 1;
                                        dialogsActivity.search(str3, true);
                                    } else {
                                        r5 = 1;
                                        dialogsActivity.setInitialSearchString(str3);
                                    }
                                }
                            } else {
                                r5 = 1;
                                z14 = true;
                            }
                            if (j4 == j) {
                                if (!z8 && !z15) {
                                    Bundle bundle = new Bundle();
                                    bundle.putLong("user_id", j4);
                                    if (i != 0) {
                                        bundle.putInt(str4, i);
                                    }
                                    if (!mainFragmentsStack.isEmpty()) {
                                        MessagesController messagesController = MessagesController.getInstance(iArr[0]);
                                        ArrayList<BaseFragment> arrayList3 = mainFragmentsStack;
                                    }
                                    if (launchActivity.actionBarLayout.presentFragment(new ChatActivity(bundle), false, true, true, false)) {
                                        launchActivity.drawerLayoutContainer.closeDrawer();
                                        z16 = true;
                                    }
                                    z16 = false;
                                } else if (z7) {
                                    BaseFragment lastFragment2 = launchActivity.actionBarLayout.getLastFragment();
                                    if (lastFragment2 != null) {
                                        AlertsCreator.createCallDialogAlert(lastFragment2, lastFragment2.getMessagesController().getUser(Long.valueOf(j4)), z15);
                                    }
                                } else {
                                    VoIPPendingCall.startOrSchedule(launchActivity, j4, z15, AccountInstance.getInstance(iArr[0]));
                                }
                            } else if (j3 != j) {
                                Bundle bundle2 = new Bundle();
                                bundle2.putLong("chat_id", j3);
                                if (i != 0) {
                                    bundle2.putInt(str4, i);
                                }
                                if (!mainFragmentsStack.isEmpty()) {
                                    MessagesController messagesController2 = MessagesController.getInstance(iArr[0]);
                                    ArrayList<BaseFragment> arrayList4 = mainFragmentsStack;
                                }
                                if (launchActivity.actionBarLayout.presentFragment(new ChatActivity(bundle2), false, true, true, false)) {
                                    launchActivity.drawerLayoutContainer.closeDrawer();
                                    z16 = true;
                                }
                                z16 = false;
                            } else if (i3 != 0) {
                                Bundle bundle3 = new Bundle();
                                bundle3.putInt("enc_id", i3);
                                if (launchActivity.actionBarLayout.presentFragment(new ChatActivity(bundle3), false, true, true, false)) {
                                    launchActivity.drawerLayoutContainer.closeDrawer();
                                    z16 = true;
                                }
                                z16 = false;
                            } else {
                                if (z14) {
                                    if (!AndroidUtilities.isTablet()) {
                                        launchActivity.actionBarLayout.removeAllFragments();
                                    } else if (!launchActivity.layersActionBarLayout.fragmentsStack.isEmpty()) {
                                        while (true) {
                                            int size2 = launchActivity.layersActionBarLayout.fragmentsStack.size();
                                            int i14 = r5 == true ? 1 : 0;
                                            int i15 = r5 == true ? 1 : 0;
                                            int i16 = r5 == true ? 1 : 0;
                                            int i17 = r5 == true ? 1 : 0;
                                            if (size2 - i14 > 0) {
                                                ActionBarLayout actionBarLayout = launchActivity.layersActionBarLayout;
                                                actionBarLayout.removeFragmentFromStack(actionBarLayout.fragmentsStack.get(0));
                                            } else {
                                                z18 = false;
                                                launchActivity.layersActionBarLayout.closeLastFragment(false);
                                                z17 = true;
                                                z19 = false;
                                            }
                                        }
                                    }
                                    z18 = false;
                                    z17 = true;
                                    z19 = false;
                                } else {
                                    if (!z13) {
                                        r10 = 0;
                                        z22 = false;
                                        z22 = false;
                                        z20 = false;
                                        z22 = false;
                                        z20 = false;
                                        z20 = false;
                                        r10 = 0;
                                        z20 = false;
                                        z20 = false;
                                        if (z12) {
                                            if (!launchActivity.actionBarLayout.fragmentsStack.isEmpty()) {
                                                launchActivity.actionBarLayout.fragmentsStack.get(0).showDialog(new SharingLocationsAlert(launchActivity, new LaunchActivity$$ExternalSyntheticLambda96(launchActivity, iArr), null));
                                            }
                                        } else {
                                            Uri uri6 = launchActivity.exportingChatUri;
                                            if (uri6 != null) {
                                                launchActivity.runImportRequest(uri6, launchActivity.documentsUrisArray);
                                                z17 = true;
                                            } else if (launchActivity.importingStickers != null) {
                                                AndroidUtilities.runOnUIThread(new LaunchActivity$$ExternalSyntheticLambda25(launchActivity));
                                            } else {
                                                if (launchActivity.videoPath == null && launchActivity.photoPathsArray == null && launchActivity.sendingText == null && launchActivity.documentsPathsArray == null && launchActivity.contactsToSend == null && launchActivity.documentsUrisArray == null) {
                                                    if (c != 0) {
                                                        if (c == r5) {
                                                            Bundle bundle4 = new Bundle();
                                                            bundle4.putLong("user_id", UserConfig.getInstance(launchActivity.currentAccount).clientUserId);
                                                            profileActivity = new ProfileActivity(bundle4);
                                                        } else {
                                                            if (c == 2) {
                                                                filtersSetupActivity = new ThemeActivity(0);
                                                            } else if (c == 3) {
                                                                filtersSetupActivity = new SessionsActivity(0);
                                                            } else if (c == 4) {
                                                                filtersSetupActivity = new FiltersSetupActivity();
                                                            } else if (c == 5) {
                                                                actionIntroActivity = new ActionIntroActivity(3);
                                                                c2 = 6;
                                                                z21 = true;
                                                                if (c == c2) {
                                                                    launchActivity.actionBarLayout.presentFragment(actionIntroActivity, false, true, true, false);
                                                                } else {
                                                                    AndroidUtilities.runOnUIThread(new LaunchActivity$$ExternalSyntheticLambda60(launchActivity, actionIntroActivity, z21));
                                                                }
                                                                if (AndroidUtilities.isTablet()) {
                                                                    launchActivity.actionBarLayout.showLastFragment();
                                                                    launchActivity.rightActionBarLayout.showLastFragment();
                                                                    launchActivity.drawerLayoutContainer.setAllowOpenDrawer(false, false);
                                                                } else {
                                                                    launchActivity.drawerLayoutContainer.setAllowOpenDrawer(r5, false);
                                                                }
                                                            } else {
                                                                c2 = 6;
                                                                actionIntroActivity = c == 6 ? new EditWidgetActivity(i2, i4) : null;
                                                                z21 = false;
                                                                if (c == c2) {
                                                                }
                                                                if (AndroidUtilities.isTablet()) {
                                                                }
                                                            }
                                                            profileActivity = filtersSetupActivity;
                                                        }
                                                        c2 = 6;
                                                        actionIntroActivity = profileActivity;
                                                        z21 = false;
                                                        if (c == c2) {
                                                        }
                                                        if (AndroidUtilities.isTablet()) {
                                                        }
                                                    } else if (z11) {
                                                        ?? bundle5 = new Bundle();
                                                        bundle5.putBoolean("destroyAfterSelect", r5);
                                                        launchActivity.actionBarLayout.presentFragment(new ContactsActivity(bundle5), false, true, true, false);
                                                        if (AndroidUtilities.isTablet()) {
                                                            launchActivity.actionBarLayout.showLastFragment();
                                                            launchActivity.rightActionBarLayout.showLastFragment();
                                                            launchActivity.drawerLayoutContainer.setAllowOpenDrawer(false, false);
                                                        } else {
                                                            launchActivity.drawerLayoutContainer.setAllowOpenDrawer(r5, false);
                                                        }
                                                    } else if (str6 != null) {
                                                        ?? bundle6 = new Bundle();
                                                        bundle6.putBoolean("destroyAfterSelect", r5);
                                                        bundle6.putBoolean("returnAsResult", r5);
                                                        bundle6.putBoolean("onlyUsers", r5);
                                                        bundle6.putBoolean("allowSelf", false);
                                                        ContactsActivity contactsActivity = new ContactsActivity(bundle6);
                                                        contactsActivity.setInitialSearchString(str6);
                                                        contactsActivity.setDelegate(new LaunchActivity$$ExternalSyntheticLambda97(launchActivity, z15, iArr));
                                                        ActionBarLayout actionBarLayout2 = launchActivity.actionBarLayout;
                                                        actionBarLayout2.presentFragment(contactsActivity, actionBarLayout2.getLastFragment() instanceof ContactsActivity, true, true, false);
                                                        if (AndroidUtilities.isTablet()) {
                                                            launchActivity.actionBarLayout.showLastFragment();
                                                            launchActivity.rightActionBarLayout.showLastFragment();
                                                            launchActivity.drawerLayoutContainer.setAllowOpenDrawer(false, false);
                                                        } else {
                                                            launchActivity.drawerLayoutContainer.setAllowOpenDrawer(r5, false);
                                                        }
                                                    } else if (z4) {
                                                        ActionIntroActivity actionIntroActivity2 = new ActionIntroActivity(5);
                                                        actionIntroActivity2.setQrLoginDelegate(new LaunchActivity$$ExternalSyntheticLambda90(launchActivity, actionIntroActivity2));
                                                        launchActivity.actionBarLayout.presentFragment(actionIntroActivity2, false, true, true, false);
                                                        if (AndroidUtilities.isTablet()) {
                                                            launchActivity.actionBarLayout.showLastFragment();
                                                            launchActivity.rightActionBarLayout.showLastFragment();
                                                            launchActivity.drawerLayoutContainer.setAllowOpenDrawer(false, false);
                                                        } else {
                                                            launchActivity.drawerLayoutContainer.setAllowOpenDrawer(r5, false);
                                                        }
                                                    } else if (z6) {
                                                        NewContactActivity newContactActivity = new NewContactActivity();
                                                        if (str != null) {
                                                            String[] split3 = str.split(" ", 2);
                                                            String str80 = split3[0];
                                                            if (split3.length > r5) {
                                                                char c7 = r5 == true ? 1 : 0;
                                                                char c8 = r5 == true ? 1 : 0;
                                                                char c9 = r5 == true ? 1 : 0;
                                                                char c10 = r5 == true ? 1 : 0;
                                                                str7 = split3[c7];
                                                            } else {
                                                                str7 = null;
                                                            }
                                                            newContactActivity.setInitialName(str80, str7);
                                                        }
                                                        String str81 = str2;
                                                        if (str81 != null) {
                                                            newContactActivity.setInitialPhoneNumber(PhoneFormat.stripExceptNumbers(str81, r5), false);
                                                        }
                                                        launchActivity.actionBarLayout.presentFragment(newContactActivity, false, true, true, false);
                                                        if (AndroidUtilities.isTablet()) {
                                                            launchActivity.actionBarLayout.showLastFragment();
                                                            launchActivity.rightActionBarLayout.showLastFragment();
                                                            launchActivity.drawerLayoutContainer.setAllowOpenDrawer(false, false);
                                                        } else {
                                                            launchActivity.drawerLayoutContainer.setAllowOpenDrawer(r5, false);
                                                        }
                                                    } else {
                                                        String str82 = str;
                                                        String str83 = str2;
                                                        if (z10) {
                                                            z17 = true;
                                                            GroupCallActivity.create(this, AccountInstance.getInstance(launchActivity.currentAccount), null, null, false, null);
                                                            if (GroupCallActivity.groupCallInstance != null) {
                                                                GroupCallActivity.groupCallUiVisible = true;
                                                            }
                                                        } else {
                                                            z17 = true;
                                                            if (z5) {
                                                                BaseFragment lastFragment3 = launchActivity.actionBarLayout.getLastFragment();
                                                                if (lastFragment3 == null || lastFragment3.getParentActivity() == null) {
                                                                    z16 = false;
                                                                } else {
                                                                    String phoneNumber = NewContactActivity.getPhoneNumber(launchActivity, UserConfig.getInstance(launchActivity.currentAccount).getCurrentUser(), str83, false);
                                                                    lastFragment3.showDialog(new AlertDialog.Builder(lastFragment3.getParentActivity()).setTitle(LocaleController.getString("NewContactAlertTitle", 2131626777)).setMessage(AndroidUtilities.replaceTags(LocaleController.formatString("NewContactAlertMessage", 2131626776, PhoneFormat.getInstance().format(phoneNumber)))).setPositiveButton(LocaleController.getString("NewContactAlertButton", 2131626775), new LaunchActivity$$ExternalSyntheticLambda8(phoneNumber, str82, lastFragment3)).setNegativeButton(LocaleController.getString("Cancel", 2131624819), null).create());
                                                                    z16 = true;
                                                                }
                                                                z18 = z;
                                                            } else if (z9) {
                                                                launchActivity.actionBarLayout.presentFragment(new CallLogActivity(), false, true, true, false);
                                                                if (AndroidUtilities.isTablet()) {
                                                                    launchActivity.actionBarLayout.showLastFragment();
                                                                    launchActivity.rightActionBarLayout.showLastFragment();
                                                                    launchActivity.drawerLayoutContainer.setAllowOpenDrawer(false, false);
                                                                } else {
                                                                    launchActivity.drawerLayoutContainer.setAllowOpenDrawer(true, false);
                                                                }
                                                                z18 = z;
                                                                z16 = true;
                                                            }
                                                        }
                                                    }
                                                    z18 = z;
                                                    z17 = true;
                                                    z16 = true;
                                                } else {
                                                    z17 = true;
                                                    if (!AndroidUtilities.isTablet()) {
                                                        NotificationCenter.getInstance(iArr[0]).postNotificationName(NotificationCenter.closeChats, new Object[0]);
                                                    }
                                                    if (j2 == j) {
                                                        launchActivity.openDialogsToSend(false);
                                                        z18 = z;
                                                        z16 = true;
                                                    } else {
                                                        ArrayList<Long> arrayList5 = new ArrayList<>();
                                                        arrayList5.add(Long.valueOf(j2));
                                                        launchActivity.didSelectDialogs(null, arrayList5, null, false);
                                                    }
                                                }
                                                if (!z16 && !z18) {
                                                    if (!AndroidUtilities.isTablet()) {
                                                        if (!UserConfig.getInstance(launchActivity.currentAccount).isClientActivated()) {
                                                            if (launchActivity.layersActionBarLayout.fragmentsStack.isEmpty()) {
                                                                launchActivity.layersActionBarLayout.addFragmentToStack(getClientNotActivatedFragment());
                                                                launchActivity.drawerLayoutContainer.setAllowOpenDrawer(false, false);
                                                            }
                                                        } else if (launchActivity.actionBarLayout.fragmentsStack.isEmpty()) {
                                                            DialogsActivity dialogsActivity2 = new DialogsActivity(r10);
                                                            dialogsActivity2.setSideMenu(launchActivity.sideMenu);
                                                            if (str3 != null) {
                                                                dialogsActivity2.setInitialSearchString(str3);
                                                            }
                                                            launchActivity.actionBarLayout.addFragmentToStack(dialogsActivity2);
                                                            launchActivity.drawerLayoutContainer.setAllowOpenDrawer(z17, false);
                                                        }
                                                    } else if (launchActivity.actionBarLayout.fragmentsStack.isEmpty()) {
                                                        if (!UserConfig.getInstance(launchActivity.currentAccount).isClientActivated()) {
                                                            launchActivity.actionBarLayout.addFragmentToStack(getClientNotActivatedFragment());
                                                            launchActivity.drawerLayoutContainer.setAllowOpenDrawer(false, false);
                                                        } else {
                                                            DialogsActivity dialogsActivity3 = new DialogsActivity(r10);
                                                            dialogsActivity3.setSideMenu(launchActivity.sideMenu);
                                                            if (str3 != null) {
                                                                dialogsActivity3.setInitialSearchString(str3);
                                                            }
                                                            launchActivity.actionBarLayout.addFragmentToStack(dialogsActivity3);
                                                            launchActivity.drawerLayoutContainer.setAllowOpenDrawer(z17, false);
                                                        }
                                                    }
                                                    launchActivity.actionBarLayout.showLastFragment();
                                                    if (AndroidUtilities.isTablet()) {
                                                        launchActivity.layersActionBarLayout.showLastFragment();
                                                        launchActivity.rightActionBarLayout.showLastFragment();
                                                    }
                                                }
                                                if (z38) {
                                                    VoIPFragment.show(launchActivity, iArr[0]);
                                                }
                                                if (!z10 && !"android.intent.action.MAIN".equals(intent.getAction()) && (groupCallActivity = GroupCallActivity.groupCallInstance) != null) {
                                                    groupCallActivity.dismiss();
                                                }
                                                intent.setAction(r10);
                                                return z16;
                                            }
                                            z18 = z;
                                            z19 = z20;
                                        }
                                    } else if (!launchActivity.actionBarLayout.fragmentsStack.isEmpty()) {
                                        z22 = false;
                                        launchActivity.actionBarLayout.fragmentsStack.get(0).showDialog(new AudioPlayerAlert(launchActivity, null));
                                    } else {
                                        z22 = false;
                                    }
                                    z18 = z;
                                    z17 = true;
                                    z19 = z22;
                                }
                                z16 = false;
                                r10 = z19;
                                if (!z16) {
                                    if (!AndroidUtilities.isTablet()) {
                                    }
                                    launchActivity.actionBarLayout.showLastFragment();
                                    if (AndroidUtilities.isTablet()) {
                                    }
                                }
                                if (z38) {
                                }
                                if (!z10) {
                                    groupCallActivity.dismiss();
                                }
                                intent.setAction(r10);
                                return z16;
                            }
                            z18 = z;
                            z17 = true;
                            r10 = 0;
                            if (!z16) {
                            }
                            if (z38) {
                            }
                            if (!z10) {
                            }
                            intent.setAction(r10);
                            return z16;
                        }
                        str6 = str5;
                        r5 = 1;
                        if (j4 == j) {
                        }
                        z18 = z;
                        z17 = true;
                        r10 = 0;
                        if (!z16) {
                        }
                        if (z38) {
                        }
                        if (!z10) {
                        }
                        intent.setAction(r10);
                        return z16;
                    }
                    z17 = true;
                    z20 = false;
                    z18 = z;
                    z19 = z20;
                    z16 = false;
                    r10 = z19;
                    if (!z16) {
                    }
                    if (z38) {
                    }
                    if (!z10) {
                    }
                    intent.setAction(r10);
                    return z16;
                }
                z37 = false;
                if (z37) {
                }
                j4 = 0;
                j3 = 0;
                j = 0;
                str4 = "message_id";
                iArr = iArr4;
                launchActivity = this;
                str = null;
                i4 = -1;
                i3 = 0;
                i2 = -1;
                str5 = null;
                z15 = false;
                str3 = null;
                i = 0;
                c = 0;
                z14 = false;
                z13 = false;
                z12 = false;
                z11 = false;
                z10 = false;
                z9 = false;
                z8 = false;
                z7 = false;
                z6 = false;
                z5 = false;
                z4 = false;
                str2 = null;
                if (UserConfig.getInstance(launchActivity.currentAccount).isClientActivated()) {
                }
                z17 = true;
                z20 = false;
                z18 = z;
                z19 = z20;
                z16 = false;
                r10 = z19;
                if (!z16) {
                }
                if (z38) {
                }
                if (!z10) {
                }
                intent.setAction(r10);
                return z16;
            } else if ("org.telegram.messenger.CREATE_STICKER_PACK".equals(intent.getAction())) {
                try {
                    this.importingStickers = intent.getParcelableArrayListExtra("android.intent.extra.STREAM");
                    this.importingStickersEmoji = intent.getStringArrayListExtra("STICKER_EMOJIS");
                    this.importingStickersSoftware = intent.getStringExtra("IMPORTER");
                } catch (Throwable th5) {
                    FileLog.e(th5);
                    this.importingStickers = null;
                    this.importingStickersEmoji = null;
                    this.importingStickersSoftware = null;
                }
            } else {
                if ("android.intent.action.SEND_MULTIPLE".equals(intent.getAction())) {
                    try {
                        parcelableArrayListExtra = intent.getParcelableArrayListExtra("android.intent.extra.STREAM");
                        type = intent.getType();
                    } catch (Exception e4) {
                        FileLog.e(e4);
                    }
                    if (parcelableArrayListExtra != null) {
                        int i18 = 0;
                        while (i18 < parcelableArrayListExtra.size()) {
                            Parcelable parcelable = (Parcelable) parcelableArrayListExtra.get(i18);
                            boolean z41 = parcelable instanceof Uri;
                            Uri uri7 = parcelable;
                            if (!z41) {
                                uri7 = Uri.parse(parcelable.toString());
                            }
                            Uri uri8 = (Uri) uri7;
                            if (uri8 != null && AndroidUtilities.isInternalUri(uri8)) {
                                parcelableArrayListExtra.remove(i18);
                                i18--;
                            }
                            i18++;
                        }
                        if (parcelableArrayListExtra.isEmpty()) {
                            arrayList = null;
                            if (arrayList != null) {
                                if (type != null && type.startsWith("image/")) {
                                    for (int i19 = 0; i19 < arrayList.size(); i19++) {
                                        Parcelable parcelable2 = (Parcelable) arrayList.get(i19);
                                        boolean z42 = parcelable2 instanceof Uri;
                                        Uri uri9 = parcelable2;
                                        if (!z42) {
                                            uri9 = Uri.parse(parcelable2.toString());
                                        }
                                        Uri uri10 = (Uri) uri9;
                                        if (this.photoPathsArray == null) {
                                            this.photoPathsArray = new ArrayList<>();
                                        }
                                        SendMessagesHelper.SendingMediaInfo sendingMediaInfo2 = new SendMessagesHelper.SendingMediaInfo();
                                        sendingMediaInfo2.uri = uri10;
                                        this.photoPathsArray.add(sendingMediaInfo2);
                                    }
                                } else {
                                    Set<String> set2 = MessagesController.getInstance(iArr4[0]).exportUri;
                                    for (int i20 = 0; i20 < arrayList.size(); i20++) {
                                        Object obj = (Parcelable) arrayList.get(i20);
                                        if (!(obj instanceof Uri)) {
                                            obj = Uri.parse(obj.toString());
                                        }
                                        Uri uri11 = (Uri) obj;
                                        String path2 = AndroidUtilities.getPath(uri11);
                                        String obj2 = obj.toString();
                                        String str84 = obj2 == null ? path2 : obj2;
                                        if (BuildVars.LOGS_ENABLED) {
                                            FileLog.d("export path = " + str84);
                                        }
                                        if (str84 != null && this.exportingChatUri == null) {
                                            String fixFileName2 = FileLoader.fixFileName(MediaController.getFileName(uri11));
                                            for (String str85 : set2) {
                                                try {
                                                    compile = Pattern.compile(str85);
                                                } catch (Exception e5) {
                                                    FileLog.e(e5);
                                                }
                                                if (compile.matcher(str84).find() || compile.matcher(fixFileName2).find()) {
                                                    this.exportingChatUri = uri11;
                                                    z24 = true;
                                                    break;
                                                }
                                            }
                                            z24 = false;
                                            if (!z24) {
                                                if (str84.startsWith("content://com.kakao.talk") && str84.endsWith("KakaoTalkChats.txt")) {
                                                    this.exportingChatUri = uri11;
                                                }
                                            }
                                        }
                                        if (path2 != null) {
                                            if (path2.startsWith("file:")) {
                                                path2 = path2.replace("file://", str78);
                                            }
                                            if (this.documentsPathsArray == null) {
                                                this.documentsPathsArray = new ArrayList<>();
                                                this.documentsOriginalPathsArray = new ArrayList<>();
                                            }
                                            this.documentsPathsArray.add(path2);
                                            this.documentsOriginalPathsArray.add(str84);
                                        } else {
                                            if (this.documentsUrisArray == null) {
                                                this.documentsUrisArray = new ArrayList<>();
                                            }
                                            this.documentsUrisArray.add(uri11);
                                            this.documentsMimeType = type;
                                        }
                                    }
                                }
                                z23 = false;
                                if (z23) {
                                    Toast.makeText(this, "Unsupported content", 0).show();
                                }
                                str4 = "message_id";
                                iArr = iArr4;
                                launchActivity = this;
                                i = 0;
                                j = 0;
                            }
                            z23 = true;
                            if (z23) {
                            }
                            str4 = "message_id";
                            iArr = iArr4;
                            launchActivity = this;
                            i = 0;
                            j = 0;
                        }
                    }
                    arrayList = parcelableArrayListExtra;
                    if (arrayList != null) {
                    }
                    z23 = true;
                    if (z23) {
                    }
                    str4 = "message_id";
                    iArr = iArr4;
                    launchActivity = this;
                    i = 0;
                    j = 0;
                } else if ("android.intent.action.VIEW".equals(intent.getAction())) {
                    Uri data = intent.getData();
                    if (data != null) {
                        String scheme = data.getScheme();
                        if (scheme != null) {
                            switch (scheme.hashCode()) {
                                case 3699:
                                    if (scheme.equals("tg")) {
                                        c6 = 0;
                                        break;
                                    }
                                    c6 = 65535;
                                    break;
                                case 3213448:
                                    if (scheme.equals("http")) {
                                        c6 = 1;
                                        break;
                                    }
                                    c6 = 65535;
                                    break;
                                case 99617003:
                                    if (scheme.equals("https")) {
                                        c6 = 2;
                                        break;
                                    }
                                    c6 = 65535;
                                    break;
                                default:
                                    c6 = 65535;
                                    break;
                            }
                            switch (c6) {
                                case 0:
                                    String uri12 = data.toString();
                                    if (uri12.startsWith("tg:resolve") || uri12.startsWith("tg://resolve")) {
                                        String replace = uri12.replace("tg:resolve", "tg://telegram.org").replace("tg://resolve", "tg://telegram.org");
                                        uri = Uri.parse(replace);
                                        String queryParameter5 = uri.getQueryParameter("domain");
                                        if (queryParameter5 != null || (queryParameter5 = uri.getQueryParameter("phone")) == null) {
                                            str51 = replace;
                                        } else {
                                            str51 = replace;
                                            if (queryParameter5.startsWith("+")) {
                                                queryParameter5 = queryParameter5.substring(1);
                                            }
                                        }
                                        if ("telegrampassport".equals(queryParameter5)) {
                                            HashMap<String, String> hashMap3 = new HashMap<>();
                                            String queryParameter6 = uri.getQueryParameter("scope");
                                            if (!TextUtils.isEmpty(queryParameter6)) {
                                                iArr3 = iArr4;
                                                if (!queryParameter6.startsWith("{") || !queryParameter6.endsWith("}")) {
                                                    str28 = scheme;
                                                } else {
                                                    str28 = scheme;
                                                    hashMap3.put("nonce", uri.getQueryParameter("nonce"));
                                                    hashMap3.put("bot_id", uri.getQueryParameter("bot_id"));
                                                    hashMap3.put("scope", queryParameter6);
                                                    hashMap3.put("public_key", uri.getQueryParameter("public_key"));
                                                    hashMap3.put("callback_url", uri.getQueryParameter("callback_url"));
                                                    hashMap2 = hashMap3;
                                                    str45 = str51;
                                                    str32 = null;
                                                    str31 = null;
                                                    num6 = null;
                                                    str27 = null;
                                                    str26 = null;
                                                    str44 = null;
                                                    str43 = null;
                                                    str42 = null;
                                                    str41 = null;
                                                    str40 = null;
                                                    str39 = null;
                                                    num5 = null;
                                                    num4 = null;
                                                }
                                            } else {
                                                str28 = scheme;
                                                iArr3 = iArr4;
                                            }
                                            hashMap3.put("payload", uri.getQueryParameter("payload"));
                                            hashMap3.put("bot_id", uri.getQueryParameter("bot_id"));
                                            hashMap3.put("scope", queryParameter6);
                                            hashMap3.put("public_key", uri.getQueryParameter("public_key"));
                                            hashMap3.put("callback_url", uri.getQueryParameter("callback_url"));
                                            hashMap2 = hashMap3;
                                            str45 = str51;
                                            str32 = null;
                                            str31 = null;
                                            num6 = null;
                                            str27 = null;
                                            str26 = null;
                                            str44 = null;
                                            str43 = null;
                                            str42 = null;
                                            str41 = null;
                                            str40 = null;
                                            str39 = null;
                                            num5 = null;
                                            num4 = null;
                                        } else {
                                            str28 = scheme;
                                            iArr3 = iArr4;
                                            String queryParameter7 = uri.getQueryParameter("start");
                                            str32 = uri.getQueryParameter("startgroup");
                                            str31 = uri.getQueryParameter("startchannel");
                                            str27 = queryParameter7;
                                            str26 = uri.getQueryParameter("admin");
                                            str44 = uri.getQueryParameter("game");
                                            str43 = uri.getQueryParameter("voicechat");
                                            str42 = uri.getQueryParameter("livestream");
                                            str41 = uri.getQueryParameter("startattach");
                                            str40 = uri.getQueryParameter("choose");
                                            str39 = uri.getQueryParameter("attach");
                                            Integer parseInt2 = Utilities.parseInt((CharSequence) uri.getQueryParameter("post"));
                                            if (parseInt2.intValue() == 0) {
                                                parseInt2 = null;
                                            }
                                            num5 = Utilities.parseInt((CharSequence) uri.getQueryParameter("thread"));
                                            if (num5.intValue() == 0) {
                                                num4 = parseInt2;
                                                num5 = null;
                                            } else {
                                                num4 = parseInt2;
                                            }
                                            Integer parseInt3 = Utilities.parseInt((CharSequence) uri.getQueryParameter("comment"));
                                            str38 = queryParameter5;
                                            if (parseInt3.intValue() == 0) {
                                                str45 = str51;
                                                num6 = null;
                                                hashMap2 = null;
                                            } else {
                                                hashMap2 = null;
                                                String str86 = str51;
                                                num6 = parseInt3;
                                                str45 = str86;
                                            }
                                            if (!str45.startsWith("tg:invoice") || str45.startsWith("tg://invoice")) {
                                                j8 = 0;
                                                str19 = Uri.parse(str45.replace("tg:invoice", "tg://invoice")).getQueryParameter("slug");
                                                str37 = null;
                                                str36 = null;
                                                str35 = null;
                                                str33 = null;
                                                str30 = null;
                                                str29 = null;
                                                str11 = null;
                                                str10 = null;
                                                str9 = null;
                                                str24 = null;
                                                str23 = null;
                                                str22 = null;
                                                str21 = null;
                                                str20 = null;
                                                str18 = null;
                                                j7 = 0;
                                                j5 = 0;
                                                num = num6;
                                                str25 = str44;
                                                str17 = str43;
                                                str16 = str42;
                                                str15 = str41;
                                                str13 = str40;
                                                str14 = str39;
                                                num2 = num5;
                                                num3 = num4;
                                            } else if (str45.startsWith("tg:privatepost") || str45.startsWith("tg://privatepost")) {
                                                Uri parse = Uri.parse(str45.replace("tg:privatepost", "tg://telegram.org").replace("tg://privatepost", "tg://telegram.org"));
                                                Integer parseInt4 = Utilities.parseInt((CharSequence) parse.getQueryParameter("post"));
                                                ?? parseLong = Utilities.parseLong(parse.getQueryParameter("channel"));
                                                if (parseInt4.intValue() != 0) {
                                                    j8 = 0;
                                                    int i21 = (parseLong.longValue() > 0L ? 1 : (parseLong.longValue() == 0L ? 0 : -1));
                                                    str46 = parseLong;
                                                    break;
                                                } else {
                                                    j8 = 0;
                                                }
                                                parseInt4 = null;
                                                str46 = null;
                                                Integer parseInt5 = Utilities.parseInt((CharSequence) parse.getQueryParameter("thread"));
                                                if (parseInt5.intValue() == 0) {
                                                    parseInt5 = null;
                                                }
                                                Integer parseInt6 = Utilities.parseInt((CharSequence) parse.getQueryParameter("comment"));
                                                if (parseInt6.intValue() == 0) {
                                                    num3 = parseInt4;
                                                    str30 = str46;
                                                    num2 = parseInt5;
                                                    str37 = null;
                                                    str36 = null;
                                                    str35 = null;
                                                    str33 = null;
                                                    str29 = null;
                                                    num = null;
                                                    str11 = null;
                                                } else {
                                                    num = parseInt6;
                                                    num3 = parseInt4;
                                                    str30 = str46;
                                                    num2 = parseInt5;
                                                    str37 = null;
                                                    str36 = null;
                                                    str35 = null;
                                                    str33 = null;
                                                    str29 = null;
                                                    str11 = null;
                                                }
                                                str10 = str11;
                                                str9 = str10;
                                                str24 = str9;
                                                str23 = str24;
                                                str22 = str23;
                                                str21 = str22;
                                                str20 = str21;
                                                str19 = str20;
                                                str18 = str19;
                                                j7 = j8;
                                                j5 = j7;
                                                str25 = str44;
                                                str17 = str43;
                                                str16 = str42;
                                                str15 = str41;
                                                str13 = str40;
                                                str14 = str39;
                                            } else if (str45.startsWith("tg:bg") || str45.startsWith("tg://bg")) {
                                                Uri parse2 = Uri.parse(str45.replace("tg:bg", "tg://telegram.org").replace("tg://bg", "tg://telegram.org"));
                                                ?? tLRPC$TL_wallPaper = new TLRPC$TL_wallPaper();
                                                tLRPC$TL_wallPaper.settings = new TLRPC$TL_wallPaperSettings();
                                                String queryParameter8 = parse2.getQueryParameter("slug");
                                                tLRPC$TL_wallPaper.slug = queryParameter8;
                                                if (queryParameter8 == null) {
                                                    tLRPC$TL_wallPaper.slug = parse2.getQueryParameter("color");
                                                }
                                                String str87 = tLRPC$TL_wallPaper.slug;
                                                if (str87 != null && str87.length() == 6) {
                                                    tLRPC$TL_wallPaper.settings.background_color = Integer.parseInt(tLRPC$TL_wallPaper.slug, 16) | (-16777216);
                                                    tLRPC$TL_wallPaper.slug = null;
                                                    z29 = true;
                                                    str47 = null;
                                                } else {
                                                    String str88 = tLRPC$TL_wallPaper.slug;
                                                    if (str88 != null && str88.length() >= 13 && AndroidUtilities.isValidWallChar(tLRPC$TL_wallPaper.slug.charAt(6))) {
                                                        tLRPC$TL_wallPaper.settings.background_color = Integer.parseInt(tLRPC$TL_wallPaper.slug.substring(0, 6), 16) | (-16777216);
                                                        tLRPC$TL_wallPaper.settings.second_background_color = Integer.parseInt(tLRPC$TL_wallPaper.slug.substring(7, 13), 16) | (-16777216);
                                                        if (tLRPC$TL_wallPaper.slug.length() >= 20 && AndroidUtilities.isValidWallChar(tLRPC$TL_wallPaper.slug.charAt(13))) {
                                                            tLRPC$TL_wallPaper.settings.third_background_color = Integer.parseInt(tLRPC$TL_wallPaper.slug.substring(14, 20), 16) | (-16777216);
                                                        }
                                                        if (tLRPC$TL_wallPaper.slug.length() == 27 && AndroidUtilities.isValidWallChar(tLRPC$TL_wallPaper.slug.charAt(20))) {
                                                            tLRPC$TL_wallPaper.settings.fourth_background_color = Integer.parseInt(tLRPC$TL_wallPaper.slug.substring(21), 16) | (-16777216);
                                                        }
                                                        try {
                                                            String queryParameter9 = parse2.getQueryParameter("rotation");
                                                            if (!TextUtils.isEmpty(queryParameter9)) {
                                                                tLRPC$TL_wallPaper.settings.rotation = Utilities.parseInt((CharSequence) queryParameter9).intValue();
                                                            }
                                                        } catch (Exception unused) {
                                                        }
                                                        str47 = null;
                                                        try {
                                                            tLRPC$TL_wallPaper.slug = null;
                                                            z29 = true;
                                                        } catch (Exception unused2) {
                                                        }
                                                    }
                                                    str47 = null;
                                                    z29 = false;
                                                }
                                                if (!z29) {
                                                    String queryParameter10 = parse2.getQueryParameter("mode");
                                                    if (queryParameter10 != null && (split = queryParameter10.toLowerCase().split(" ")) != null && split.length > 0) {
                                                        for (int i22 = 0; i22 < split.length; i22++) {
                                                            if ("blur".equals(split[i22])) {
                                                                tLRPC$TL_wallPaper.settings.blur = true;
                                                            } else if ("motion".equals(split[i22])) {
                                                                tLRPC$TL_wallPaper.settings.motion = true;
                                                            }
                                                        }
                                                    }
                                                    tLRPC$TL_wallPaper.settings.intensity = Utilities.parseInt((CharSequence) parse2.getQueryParameter("intensity")).intValue();
                                                    try {
                                                        queryParameter2 = parse2.getQueryParameter("bg_color");
                                                    } catch (Exception unused3) {
                                                    }
                                                    try {
                                                        if (!TextUtils.isEmpty(queryParameter2)) {
                                                            try {
                                                                tLRPC$TL_wallPaper.settings.background_color = Integer.parseInt(queryParameter2.substring(0, 6), 16) | (-16777216);
                                                                if (queryParameter2.length() >= 13) {
                                                                    tLRPC$TL_wallPaper.settings.second_background_color = Integer.parseInt(queryParameter2.substring(8, 13), 16) | (-16777216);
                                                                    if (queryParameter2.length() >= 20 && AndroidUtilities.isValidWallChar(queryParameter2.charAt(13))) {
                                                                        tLRPC$TL_wallPaper.settings.third_background_color = Integer.parseInt(queryParameter2.substring(14, 20), 16) | (-16777216);
                                                                    }
                                                                    if (queryParameter2.length() == 27 && AndroidUtilities.isValidWallChar(queryParameter2.charAt(20))) {
                                                                        tLRPC$TL_wallPaper.settings.fourth_background_color = Integer.parseInt(queryParameter2.substring(21), 16) | (-16777216);
                                                                    }
                                                                }
                                                            } catch (Exception unused4) {
                                                            }
                                                            queryParameter = parse2.getQueryParameter("rotation");
                                                            if (!TextUtils.isEmpty(queryParameter)) {
                                                                tLRPC$TL_wallPaper.settings.rotation = Utilities.parseInt((CharSequence) queryParameter).intValue();
                                                            }
                                                        }
                                                        queryParameter = parse2.getQueryParameter("rotation");
                                                        if (!TextUtils.isEmpty(queryParameter)) {
                                                        }
                                                    } catch (Exception unused5) {
                                                    }
                                                }
                                                str20 = tLRPC$TL_wallPaper;
                                                str37 = str47;
                                                str36 = str37;
                                                str35 = str36;
                                                str33 = str35;
                                                str30 = str33;
                                                str29 = str30;
                                                str11 = str29;
                                                str10 = str11;
                                                str9 = str10;
                                                str24 = str9;
                                                str23 = str24;
                                                str22 = str23;
                                                str21 = str22;
                                                str19 = str21;
                                                str18 = str19;
                                                num = num6;
                                                str25 = str44;
                                                str17 = str43;
                                                str16 = str42;
                                                str15 = str41;
                                                str13 = str40;
                                                str14 = str39;
                                                num2 = num5;
                                                num3 = num4;
                                                hashMap = hashMap2;
                                                str34 = str38;
                                                z27 = false;
                                                j8 = 0;
                                                i8 = 0;
                                                c5 = 0;
                                                z9 = false;
                                                z8 = false;
                                                z26 = false;
                                                z7 = false;
                                                z6 = false;
                                                z5 = false;
                                                z4 = false;
                                                j7 = 0;
                                                j5 = 0;
                                                i7 = -1;
                                                str12 = str18;
                                                break;
                                            } else {
                                                if (str45.startsWith("tg:join") || str45.startsWith("tg://join")) {
                                                    str29 = Uri.parse(str45.replace("tg:join", "tg://telegram.org").replace("tg://join", "tg://telegram.org")).getQueryParameter("invite");
                                                    num = num6;
                                                    str25 = str44;
                                                    str17 = str43;
                                                    str16 = str42;
                                                    str15 = str41;
                                                    str13 = str40;
                                                    str14 = str39;
                                                    num2 = num5;
                                                    num3 = num4;
                                                    hashMap = hashMap2;
                                                    str34 = str38;
                                                    str37 = null;
                                                    str36 = null;
                                                    str35 = null;
                                                    str33 = null;
                                                    z27 = false;
                                                    j8 = 0;
                                                    str30 = null;
                                                } else {
                                                    if (str45.startsWith("tg:addstickers") || str45.startsWith("tg://addstickers")) {
                                                        str33 = Uri.parse(str45.replace("tg:addstickers", "tg://telegram.org").replace("tg://addstickers", "tg://telegram.org")).getQueryParameter("set");
                                                        num = num6;
                                                        str25 = str44;
                                                        str17 = str43;
                                                        str16 = str42;
                                                        str15 = str41;
                                                        str13 = str40;
                                                        str14 = str39;
                                                        num2 = num5;
                                                        num3 = num4;
                                                        hashMap = hashMap2;
                                                        str34 = str38;
                                                        str37 = null;
                                                        str36 = null;
                                                        str35 = null;
                                                    } else if (str45.startsWith("tg:msg") || str45.startsWith("tg://msg") || str45.startsWith("tg://share") || str45.startsWith("tg:share")) {
                                                        Uri parse3 = Uri.parse(str45.replace("tg:msg", "tg://telegram.org").replace("tg://msg", "tg://telegram.org").replace("tg://share", "tg://telegram.org").replace("tg:share", "tg://telegram.org"));
                                                        String queryParameter11 = parse3.getQueryParameter("url");
                                                        if (queryParameter11 != null) {
                                                            str78 = queryParameter11;
                                                        }
                                                        if (parse3.getQueryParameter("text") != null) {
                                                            if (str78.length() > 0) {
                                                                str78 = str78 + "\n";
                                                                z30 = true;
                                                            } else {
                                                                z30 = false;
                                                            }
                                                            str78 = str78 + parse3.getQueryParameter("text");
                                                        } else {
                                                            z30 = false;
                                                        }
                                                        if (str78.length() > 16384) {
                                                            i9 = 0;
                                                            str48 = str78.substring(0, 16384);
                                                        } else {
                                                            i9 = 0;
                                                            str48 = str78;
                                                        }
                                                        while (str48.endsWith("\n")) {
                                                            str48 = str48.substring(i9, str48.length() - 1);
                                                        }
                                                        str35 = str48;
                                                        z27 = z30;
                                                        num = num6;
                                                        str25 = str44;
                                                        str17 = str43;
                                                        str16 = str42;
                                                        str15 = str41;
                                                        str13 = str40;
                                                        str14 = str39;
                                                        num2 = num5;
                                                        num3 = num4;
                                                        hashMap = hashMap2;
                                                        str34 = str38;
                                                        str37 = null;
                                                        str36 = null;
                                                        str33 = null;
                                                        j8 = 0;
                                                        str30 = null;
                                                        str29 = null;
                                                    } else {
                                                        if (str45.startsWith("tg:confirmphone") || str45.startsWith("tg://confirmphone")) {
                                                            Uri parse4 = Uri.parse(str45.replace("tg:confirmphone", "tg://telegram.org").replace("tg://confirmphone", "tg://telegram.org"));
                                                            String queryParameter12 = parse4.getQueryParameter("phone");
                                                            str36 = parse4.getQueryParameter("hash");
                                                            str37 = queryParameter12;
                                                            num = num6;
                                                            str25 = str44;
                                                            str17 = str43;
                                                            str16 = str42;
                                                            str15 = str41;
                                                            str13 = str40;
                                                            str14 = str39;
                                                            num2 = num5;
                                                            num3 = num4;
                                                            hashMap = hashMap2;
                                                            str34 = str38;
                                                        } else if (str45.startsWith("tg:login") || str45.startsWith("tg://login")) {
                                                            Uri parse5 = Uri.parse(str45.replace("tg:login", "tg://telegram.org").replace("tg://login", "tg://telegram.org"));
                                                            String queryParameter13 = parse5.getQueryParameter("token");
                                                            int intValue = Utilities.parseInt((CharSequence) parse5.getQueryParameter("code")).intValue();
                                                            str22 = intValue != 0 ? str78 + intValue : null;
                                                            str21 = queryParameter13;
                                                            num = num6;
                                                            str25 = str44;
                                                            str17 = str43;
                                                            str16 = str42;
                                                            str15 = str41;
                                                            str13 = str40;
                                                            str14 = str39;
                                                            num2 = num5;
                                                            num3 = num4;
                                                            hashMap = hashMap2;
                                                            str34 = str38;
                                                            str37 = null;
                                                            str36 = null;
                                                            str35 = null;
                                                            str33 = null;
                                                            z27 = false;
                                                            j8 = 0;
                                                            str30 = null;
                                                            str29 = null;
                                                            str12 = null;
                                                            i8 = 0;
                                                            c5 = 0;
                                                            z9 = false;
                                                            z8 = false;
                                                            z26 = false;
                                                            z7 = false;
                                                            z6 = false;
                                                            z5 = false;
                                                            z4 = false;
                                                            str11 = null;
                                                            str10 = null;
                                                            str9 = null;
                                                            str24 = null;
                                                            str23 = null;
                                                            j7 = 0;
                                                            j5 = 0;
                                                            str20 = null;
                                                            str19 = null;
                                                            str18 = null;
                                                            i7 = -1;
                                                            break;
                                                        } else if (str45.startsWith("tg:openmessage") || str45.startsWith("tg://openmessage")) {
                                                            Uri parse6 = Uri.parse(str45.replace("tg:openmessage", "tg://telegram.org").replace("tg://openmessage", "tg://telegram.org"));
                                                            String queryParameter14 = parse6.getQueryParameter("user_id");
                                                            String queryParameter15 = parse6.getQueryParameter("chat_id");
                                                            String queryParameter16 = parse6.getQueryParameter("message_id");
                                                            if (queryParameter14 != null) {
                                                                j10 = Long.parseLong(queryParameter14);
                                                                j9 = 0;
                                                                if (queryParameter16 != null) {
                                                                    try {
                                                                        parseInt = Integer.parseInt(queryParameter16);
                                                                    } catch (NumberFormatException unused6) {
                                                                    }
                                                                    j7 = j10;
                                                                    j5 = j9;
                                                                    num = num6;
                                                                    str25 = str44;
                                                                    str17 = str43;
                                                                    str16 = str42;
                                                                    str15 = str41;
                                                                    str13 = str40;
                                                                    str14 = str39;
                                                                    num2 = num5;
                                                                    num3 = num4;
                                                                    hashMap = hashMap2;
                                                                    str34 = str38;
                                                                    str36 = null;
                                                                    str35 = null;
                                                                    str33 = null;
                                                                    z27 = false;
                                                                    j8 = 0;
                                                                    str30 = null;
                                                                    str29 = null;
                                                                    str12 = null;
                                                                    c5 = 0;
                                                                    z9 = false;
                                                                    z8 = false;
                                                                    z26 = false;
                                                                    z7 = false;
                                                                    z6 = false;
                                                                    z5 = false;
                                                                    z4 = false;
                                                                    str11 = null;
                                                                    str10 = null;
                                                                    str9 = null;
                                                                    str24 = null;
                                                                    str23 = null;
                                                                    str22 = null;
                                                                    str21 = null;
                                                                    str20 = null;
                                                                    str19 = null;
                                                                    str18 = null;
                                                                    i7 = -1;
                                                                    i8 = parseInt;
                                                                    str37 = null;
                                                                    break;
                                                                }
                                                                parseInt = 0;
                                                                j7 = j10;
                                                                j5 = j9;
                                                                num = num6;
                                                                str25 = str44;
                                                                str17 = str43;
                                                                str16 = str42;
                                                                str15 = str41;
                                                                str13 = str40;
                                                                str14 = str39;
                                                                num2 = num5;
                                                                num3 = num4;
                                                                hashMap = hashMap2;
                                                                str34 = str38;
                                                                str36 = null;
                                                                str35 = null;
                                                                str33 = null;
                                                                z27 = false;
                                                                j8 = 0;
                                                                str30 = null;
                                                                str29 = null;
                                                                str12 = null;
                                                                c5 = 0;
                                                                z9 = false;
                                                                z8 = false;
                                                                z26 = false;
                                                                z7 = false;
                                                                z6 = false;
                                                                z5 = false;
                                                                z4 = false;
                                                                str11 = null;
                                                                str10 = null;
                                                                str9 = null;
                                                                str24 = null;
                                                                str23 = null;
                                                                str22 = null;
                                                                str21 = null;
                                                                str20 = null;
                                                                str19 = null;
                                                                str18 = null;
                                                                i7 = -1;
                                                                i8 = parseInt;
                                                                str37 = null;
                                                            } else {
                                                                if (queryParameter15 != null) {
                                                                    j9 = Long.parseLong(queryParameter15);
                                                                    j10 = 0;
                                                                    if (queryParameter16 != null) {
                                                                    }
                                                                    parseInt = 0;
                                                                    j7 = j10;
                                                                    j5 = j9;
                                                                    num = num6;
                                                                    str25 = str44;
                                                                    str17 = str43;
                                                                    str16 = str42;
                                                                    str15 = str41;
                                                                    str13 = str40;
                                                                    str14 = str39;
                                                                    num2 = num5;
                                                                    num3 = num4;
                                                                    hashMap = hashMap2;
                                                                    str34 = str38;
                                                                    str36 = null;
                                                                    str35 = null;
                                                                    str33 = null;
                                                                    z27 = false;
                                                                    j8 = 0;
                                                                    str30 = null;
                                                                    str29 = null;
                                                                    str12 = null;
                                                                    c5 = 0;
                                                                    z9 = false;
                                                                    z8 = false;
                                                                    z26 = false;
                                                                    z7 = false;
                                                                    z6 = false;
                                                                    z5 = false;
                                                                    z4 = false;
                                                                    str11 = null;
                                                                    str10 = null;
                                                                    str9 = null;
                                                                    str24 = null;
                                                                    str23 = null;
                                                                    str22 = null;
                                                                    str21 = null;
                                                                    str20 = null;
                                                                    str19 = null;
                                                                    str18 = null;
                                                                    i7 = -1;
                                                                    i8 = parseInt;
                                                                    str37 = null;
                                                                }
                                                                j10 = 0;
                                                                j9 = 0;
                                                                if (queryParameter16 != null) {
                                                                }
                                                                parseInt = 0;
                                                                j7 = j10;
                                                                j5 = j9;
                                                                num = num6;
                                                                str25 = str44;
                                                                str17 = str43;
                                                                str16 = str42;
                                                                str15 = str41;
                                                                str13 = str40;
                                                                str14 = str39;
                                                                num2 = num5;
                                                                num3 = num4;
                                                                hashMap = hashMap2;
                                                                str34 = str38;
                                                                str36 = null;
                                                                str35 = null;
                                                                str33 = null;
                                                                z27 = false;
                                                                j8 = 0;
                                                                str30 = null;
                                                                str29 = null;
                                                                str12 = null;
                                                                c5 = 0;
                                                                z9 = false;
                                                                z8 = false;
                                                                z26 = false;
                                                                z7 = false;
                                                                z6 = false;
                                                                z5 = false;
                                                                z4 = false;
                                                                str11 = null;
                                                                str10 = null;
                                                                str9 = null;
                                                                str24 = null;
                                                                str23 = null;
                                                                str22 = null;
                                                                str21 = null;
                                                                str20 = null;
                                                                str19 = null;
                                                                str18 = null;
                                                                i7 = -1;
                                                                i8 = parseInt;
                                                                str37 = null;
                                                            }
                                                        } else {
                                                            if (str45.startsWith("tg:passport") || str45.startsWith("tg://passport") || str45.startsWith("tg:secureid")) {
                                                                Uri parse7 = Uri.parse(str45.replace("tg:passport", "tg://telegram.org").replace("tg://passport", "tg://telegram.org").replace("tg:secureid", "tg://telegram.org"));
                                                                HashMap<String, String> hashMap4 = new HashMap<>();
                                                                String queryParameter17 = parse7.getQueryParameter("scope");
                                                                if (!TextUtils.isEmpty(queryParameter17) && queryParameter17.startsWith("{") && queryParameter17.endsWith("}")) {
                                                                    hashMap4.put("nonce", parse7.getQueryParameter("nonce"));
                                                                } else {
                                                                    hashMap4.put("payload", parse7.getQueryParameter("payload"));
                                                                }
                                                                hashMap4.put("bot_id", parse7.getQueryParameter("bot_id"));
                                                                hashMap4.put("scope", queryParameter17);
                                                                hashMap4.put("public_key", parse7.getQueryParameter("public_key"));
                                                                hashMap4.put("callback_url", parse7.getQueryParameter("callback_url"));
                                                                hashMap = hashMap4;
                                                                num = num6;
                                                                str25 = str44;
                                                                str17 = str43;
                                                                str16 = str42;
                                                                str15 = str41;
                                                                str13 = str40;
                                                                str14 = str39;
                                                                num2 = num5;
                                                                num3 = num4;
                                                            } else if (str45.startsWith("tg:setlanguage") || str45.startsWith("tg://setlanguage")) {
                                                                str24 = Uri.parse(str45.replace("tg:setlanguage", "tg://telegram.org").replace("tg://setlanguage", "tg://telegram.org")).getQueryParameter("lang");
                                                                num = num6;
                                                                str25 = str44;
                                                                str17 = str43;
                                                                str16 = str42;
                                                                str15 = str41;
                                                                str13 = str40;
                                                                str14 = str39;
                                                                num2 = num5;
                                                                num3 = num4;
                                                                hashMap = hashMap2;
                                                                str34 = str38;
                                                                str37 = null;
                                                                str36 = null;
                                                                str35 = null;
                                                                str33 = null;
                                                                z27 = false;
                                                                j8 = 0;
                                                                str30 = null;
                                                                str29 = null;
                                                                str12 = null;
                                                                i8 = 0;
                                                                c5 = 0;
                                                                z9 = false;
                                                                z8 = false;
                                                                z26 = false;
                                                                z7 = false;
                                                                z6 = false;
                                                                z5 = false;
                                                                z4 = false;
                                                                str11 = null;
                                                                str10 = null;
                                                                str9 = null;
                                                                str23 = null;
                                                                j7 = 0;
                                                                j5 = 0;
                                                                str22 = null;
                                                                str21 = null;
                                                                str20 = null;
                                                                str19 = null;
                                                                str18 = null;
                                                                i7 = -1;
                                                            } else if (str45.startsWith("tg:addtheme") || str45.startsWith("tg://addtheme")) {
                                                                str18 = Uri.parse(str45.replace("tg:addtheme", "tg://telegram.org").replace("tg://addtheme", "tg://telegram.org")).getQueryParameter("slug");
                                                                num = num6;
                                                                str25 = str44;
                                                                str17 = str43;
                                                                str16 = str42;
                                                                str15 = str41;
                                                                str13 = str40;
                                                                str14 = str39;
                                                                num2 = num5;
                                                                num3 = num4;
                                                                hashMap = hashMap2;
                                                                str34 = str38;
                                                                str37 = null;
                                                                str36 = null;
                                                                str35 = null;
                                                                str33 = null;
                                                                z27 = false;
                                                                j8 = 0;
                                                                str30 = null;
                                                                str29 = null;
                                                                str12 = null;
                                                                i8 = 0;
                                                                c5 = 0;
                                                                z9 = false;
                                                                z8 = false;
                                                                z26 = false;
                                                                z7 = false;
                                                                z6 = false;
                                                                z5 = false;
                                                                z4 = false;
                                                                str11 = null;
                                                                str10 = null;
                                                                str9 = null;
                                                                str24 = null;
                                                                str23 = null;
                                                                j7 = 0;
                                                                j5 = 0;
                                                                str22 = null;
                                                                str21 = null;
                                                                str20 = null;
                                                                str19 = null;
                                                                i7 = -1;
                                                            } else if (str45.startsWith("tg:settings") || str45.startsWith("tg://settings")) {
                                                                if (str45.contains("themes")) {
                                                                    num = num6;
                                                                    str25 = str44;
                                                                    str17 = str43;
                                                                    str16 = str42;
                                                                    str15 = str41;
                                                                    str13 = str40;
                                                                    str14 = str39;
                                                                    num2 = num5;
                                                                    num3 = num4;
                                                                    hashMap = hashMap2;
                                                                    str34 = str38;
                                                                    str37 = null;
                                                                    str36 = null;
                                                                    str35 = null;
                                                                    str33 = null;
                                                                    z27 = false;
                                                                    j8 = 0;
                                                                    str30 = null;
                                                                    str29 = null;
                                                                    str12 = null;
                                                                    i8 = 0;
                                                                    c5 = 2;
                                                                } else if (str45.contains("devices")) {
                                                                    num = num6;
                                                                    str25 = str44;
                                                                    str17 = str43;
                                                                    str16 = str42;
                                                                    str15 = str41;
                                                                    str13 = str40;
                                                                    str14 = str39;
                                                                    num2 = num5;
                                                                    num3 = num4;
                                                                    hashMap = hashMap2;
                                                                    str34 = str38;
                                                                    str37 = null;
                                                                    str36 = null;
                                                                    str35 = null;
                                                                    str33 = null;
                                                                    z27 = false;
                                                                    j8 = 0;
                                                                    str30 = null;
                                                                    str29 = null;
                                                                    str12 = null;
                                                                    i8 = 0;
                                                                    c5 = 3;
                                                                } else if (str45.contains("folders")) {
                                                                    num = num6;
                                                                    str25 = str44;
                                                                    str17 = str43;
                                                                    str16 = str42;
                                                                    str15 = str41;
                                                                    str13 = str40;
                                                                    str14 = str39;
                                                                    num2 = num5;
                                                                    num3 = num4;
                                                                    hashMap = hashMap2;
                                                                    str34 = str38;
                                                                    str37 = null;
                                                                    str36 = null;
                                                                    str35 = null;
                                                                    str33 = null;
                                                                    z27 = false;
                                                                    j8 = 0;
                                                                    str30 = null;
                                                                    str29 = null;
                                                                    str12 = null;
                                                                    i8 = 0;
                                                                    c5 = 4;
                                                                } else if (str45.contains("change_number")) {
                                                                    num = num6;
                                                                    str25 = str44;
                                                                    str17 = str43;
                                                                    str16 = str42;
                                                                    str15 = str41;
                                                                    str13 = str40;
                                                                    str14 = str39;
                                                                    num2 = num5;
                                                                    num3 = num4;
                                                                    hashMap = hashMap2;
                                                                    str34 = str38;
                                                                    str37 = null;
                                                                    str36 = null;
                                                                    str35 = null;
                                                                    str33 = null;
                                                                    z27 = false;
                                                                    j8 = 0;
                                                                    str30 = null;
                                                                    str29 = null;
                                                                    str12 = null;
                                                                    i8 = 0;
                                                                    c5 = 5;
                                                                } else {
                                                                    num = num6;
                                                                    str25 = str44;
                                                                    str17 = str43;
                                                                    str16 = str42;
                                                                    str15 = str41;
                                                                    str13 = str40;
                                                                    str14 = str39;
                                                                    num2 = num5;
                                                                    num3 = num4;
                                                                    hashMap = hashMap2;
                                                                    str34 = str38;
                                                                    str37 = null;
                                                                    str36 = null;
                                                                    str35 = null;
                                                                    str33 = null;
                                                                    z27 = false;
                                                                    j8 = 0;
                                                                    str30 = null;
                                                                    str29 = null;
                                                                    str12 = null;
                                                                    i8 = 0;
                                                                    c5 = 1;
                                                                }
                                                                z9 = false;
                                                                z8 = false;
                                                                z26 = false;
                                                                z7 = false;
                                                                z6 = false;
                                                                z5 = false;
                                                                z4 = false;
                                                                str11 = null;
                                                                str10 = null;
                                                                str9 = null;
                                                                str24 = null;
                                                                str23 = null;
                                                                j7 = 0;
                                                                j5 = 0;
                                                                str22 = null;
                                                                str21 = null;
                                                                str20 = null;
                                                                str19 = null;
                                                                str18 = null;
                                                                i7 = -1;
                                                            } else if (str45.startsWith("tg:search") || str45.startsWith("tg://search")) {
                                                                String queryParameter18 = Uri.parse(str45.replace("tg:search", "tg://telegram.org").replace("tg://search", "tg://telegram.org")).getQueryParameter("query");
                                                                if (queryParameter18 != null) {
                                                                    str78 = queryParameter18.trim();
                                                                }
                                                                num = num6;
                                                                str25 = str44;
                                                                str17 = str43;
                                                                str16 = str42;
                                                                str15 = str41;
                                                                str13 = str40;
                                                                str14 = str39;
                                                                num2 = num5;
                                                                num3 = num4;
                                                                hashMap = hashMap2;
                                                                str37 = null;
                                                                str36 = null;
                                                                str35 = null;
                                                                str33 = null;
                                                                z27 = false;
                                                                j8 = 0;
                                                                str30 = null;
                                                                str29 = null;
                                                                i8 = 0;
                                                                c5 = 0;
                                                                z9 = false;
                                                                z8 = false;
                                                                z26 = false;
                                                                z7 = false;
                                                                z6 = false;
                                                                z5 = false;
                                                                str11 = null;
                                                                str10 = null;
                                                                str9 = null;
                                                                str24 = null;
                                                                str23 = null;
                                                                j7 = 0;
                                                                j5 = 0;
                                                                str22 = null;
                                                                str21 = null;
                                                                str20 = null;
                                                                str19 = null;
                                                                str18 = null;
                                                                i7 = -1;
                                                                str12 = str78;
                                                                str34 = str38;
                                                                z4 = false;
                                                                break;
                                                            } else if (str45.startsWith("tg:calllog") || str45.startsWith("tg://calllog")) {
                                                                num = num6;
                                                                str25 = str44;
                                                                str17 = str43;
                                                                str16 = str42;
                                                                str15 = str41;
                                                                str13 = str40;
                                                                str14 = str39;
                                                                num2 = num5;
                                                                num3 = num4;
                                                                hashMap = hashMap2;
                                                                str34 = str38;
                                                                str37 = null;
                                                                str36 = null;
                                                                str35 = null;
                                                                str33 = null;
                                                                z27 = false;
                                                                j8 = 0;
                                                                str30 = null;
                                                                str29 = null;
                                                                str12 = null;
                                                                i8 = 0;
                                                                c5 = 0;
                                                                z9 = true;
                                                                z8 = false;
                                                                z26 = false;
                                                                z7 = false;
                                                                z6 = false;
                                                                z5 = false;
                                                                z4 = false;
                                                                str11 = null;
                                                                str10 = null;
                                                                str9 = null;
                                                                str24 = null;
                                                                str23 = null;
                                                                j7 = 0;
                                                                j5 = 0;
                                                                str22 = null;
                                                                str21 = null;
                                                                str20 = null;
                                                                str19 = null;
                                                                str18 = null;
                                                                i7 = -1;
                                                            } else if (str45.startsWith("tg:call") || str45.startsWith("tg://call")) {
                                                                if (UserConfig.getInstance(this.currentAccount).isClientActivated()) {
                                                                    if (ContactsController.getInstance(this.currentAccount).contactsLoaded || intent.hasExtra("extra_force_call")) {
                                                                        String queryParameter19 = uri.getQueryParameter("format");
                                                                        String queryParameter20 = uri.getQueryParameter("name");
                                                                        String queryParameter21 = uri.getQueryParameter("phone");
                                                                        z31 = false;
                                                                        List<TLRPC$TL_contact> findContacts = findContacts(queryParameter20, queryParameter21, false);
                                                                        if (findContacts.isEmpty() && queryParameter21 != null) {
                                                                            str50 = queryParameter20;
                                                                            str49 = queryParameter21;
                                                                            z34 = true;
                                                                            z33 = false;
                                                                            z32 = false;
                                                                            str78 = null;
                                                                            j11 = 0;
                                                                            str11 = str78;
                                                                            str10 = str50;
                                                                            j7 = j11;
                                                                            str9 = str49;
                                                                            num = num6;
                                                                            str25 = str44;
                                                                            str17 = str43;
                                                                            str16 = str42;
                                                                            str15 = str41;
                                                                            str13 = str40;
                                                                            str14 = str39;
                                                                            num2 = num5;
                                                                            num3 = num4;
                                                                            hashMap = hashMap2;
                                                                            str34 = str38;
                                                                            str33 = null;
                                                                            z27 = false;
                                                                            j8 = 0;
                                                                            str30 = null;
                                                                            str29 = null;
                                                                            str12 = null;
                                                                            i8 = 0;
                                                                            c5 = 0;
                                                                            z9 = false;
                                                                            z6 = false;
                                                                            z4 = false;
                                                                            str24 = null;
                                                                            str23 = null;
                                                                            j5 = 0;
                                                                            str22 = null;
                                                                            str21 = null;
                                                                            str20 = null;
                                                                            str19 = null;
                                                                            str18 = null;
                                                                            i7 = -1;
                                                                            z5 = z34;
                                                                            z8 = z33;
                                                                            z26 = z32;
                                                                            z7 = z31;
                                                                            str37 = null;
                                                                            str36 = null;
                                                                            str35 = null;
                                                                            break;
                                                                        } else {
                                                                            j11 = findContacts.size() == 1 ? findContacts.get(0).user_id : 0L;
                                                                            if (j11 != 0) {
                                                                                str78 = null;
                                                                            } else if (queryParameter20 != null) {
                                                                                str78 = queryParameter20;
                                                                            }
                                                                            boolean equalsIgnoreCase = "video".equalsIgnoreCase(queryParameter19);
                                                                            z33 = !equalsIgnoreCase;
                                                                            z32 = equalsIgnoreCase;
                                                                            z34 = false;
                                                                            z31 = true;
                                                                            str50 = null;
                                                                        }
                                                                    } else {
                                                                        Intent intent2 = new Intent((Intent) intent);
                                                                        intent2.removeExtra("actions.fulfillment.extra.ACTION_TOKEN");
                                                                        intent2.putExtra("extra_force_call", true);
                                                                        ContactsLoadingObserver.observe(new LaunchActivity$$ExternalSyntheticLambda64(this, intent2), 1000L);
                                                                        z34 = false;
                                                                        z33 = false;
                                                                        z32 = false;
                                                                        str78 = null;
                                                                        z31 = false;
                                                                        str50 = null;
                                                                        j11 = 0;
                                                                    }
                                                                    str49 = null;
                                                                    str11 = str78;
                                                                    str10 = str50;
                                                                    j7 = j11;
                                                                    str9 = str49;
                                                                    num = num6;
                                                                    str25 = str44;
                                                                    str17 = str43;
                                                                    str16 = str42;
                                                                    str15 = str41;
                                                                    str13 = str40;
                                                                    str14 = str39;
                                                                    num2 = num5;
                                                                    num3 = num4;
                                                                    hashMap = hashMap2;
                                                                    str34 = str38;
                                                                    str33 = null;
                                                                    z27 = false;
                                                                    j8 = 0;
                                                                    str30 = null;
                                                                    str29 = null;
                                                                    str12 = null;
                                                                    i8 = 0;
                                                                    c5 = 0;
                                                                    z9 = false;
                                                                    z6 = false;
                                                                    z4 = false;
                                                                    str24 = null;
                                                                    str23 = null;
                                                                    j5 = 0;
                                                                    str22 = null;
                                                                    str21 = null;
                                                                    str20 = null;
                                                                    str19 = null;
                                                                    str18 = null;
                                                                    i7 = -1;
                                                                    z5 = z34;
                                                                    z8 = z33;
                                                                    z26 = z32;
                                                                    z7 = z31;
                                                                    str37 = null;
                                                                    str36 = null;
                                                                    str35 = null;
                                                                } else {
                                                                    num = num6;
                                                                    str25 = str44;
                                                                    str17 = str43;
                                                                    str16 = str42;
                                                                    str15 = str41;
                                                                    str13 = str40;
                                                                    str14 = str39;
                                                                    num2 = num5;
                                                                    num3 = num4;
                                                                    hashMap = hashMap2;
                                                                }
                                                            } else if (str45.startsWith("tg:scanqr") || str45.startsWith("tg://scanqr")) {
                                                                num = num6;
                                                                str25 = str44;
                                                                str17 = str43;
                                                                str16 = str42;
                                                                str15 = str41;
                                                                str13 = str40;
                                                                str14 = str39;
                                                                num2 = num5;
                                                                num3 = num4;
                                                                hashMap = hashMap2;
                                                                str34 = str38;
                                                                str37 = null;
                                                                str36 = null;
                                                                str35 = null;
                                                                str33 = null;
                                                                z27 = false;
                                                                j8 = 0;
                                                                str30 = null;
                                                                str29 = null;
                                                                str12 = null;
                                                                i8 = 0;
                                                                c5 = 0;
                                                                z9 = false;
                                                                z8 = false;
                                                                z26 = false;
                                                                z7 = false;
                                                                z6 = false;
                                                                z5 = false;
                                                                z4 = true;
                                                                str11 = null;
                                                                str10 = null;
                                                                str9 = null;
                                                                str24 = null;
                                                                str23 = null;
                                                                j7 = 0;
                                                                j5 = 0;
                                                                str22 = null;
                                                                str21 = null;
                                                                str20 = null;
                                                                str19 = null;
                                                                str18 = null;
                                                                i7 = -1;
                                                            } else if (str45.startsWith("tg:addcontact") || str45.startsWith("tg://addcontact")) {
                                                                Uri parse8 = Uri.parse(str45.replace("tg:addcontact", "tg://telegram.org").replace("tg://addcontact", "tg://telegram.org"));
                                                                String queryParameter22 = parse8.getQueryParameter("name");
                                                                str9 = parse8.getQueryParameter("phone");
                                                                str10 = queryParameter22;
                                                                num = num6;
                                                                str25 = str44;
                                                                str17 = str43;
                                                                str16 = str42;
                                                                str15 = str41;
                                                                str13 = str40;
                                                                str14 = str39;
                                                                num2 = num5;
                                                                num3 = num4;
                                                                hashMap = hashMap2;
                                                                str34 = str38;
                                                                str37 = null;
                                                                str36 = null;
                                                                str35 = null;
                                                                str33 = null;
                                                                z27 = false;
                                                                j8 = 0;
                                                                str30 = null;
                                                                str29 = null;
                                                                str12 = null;
                                                                i8 = 0;
                                                                c5 = 0;
                                                                z9 = false;
                                                                z8 = false;
                                                                z26 = false;
                                                                z7 = false;
                                                                z6 = true;
                                                                z5 = false;
                                                                z4 = false;
                                                                str11 = null;
                                                                str24 = null;
                                                                str23 = null;
                                                                j7 = 0;
                                                                j5 = 0;
                                                                str22 = null;
                                                                str21 = null;
                                                                str20 = null;
                                                                str19 = null;
                                                                str18 = null;
                                                                i7 = -1;
                                                            } else {
                                                                String replace2 = str45.replace("tg://", str78).replace("tg:", str78);
                                                                int indexOf = replace2.indexOf(63);
                                                                if (indexOf >= 0) {
                                                                    replace2 = replace2.substring(0, indexOf);
                                                                }
                                                                str23 = replace2;
                                                                num = num6;
                                                                str25 = str44;
                                                                str17 = str43;
                                                                str16 = str42;
                                                                str15 = str41;
                                                                str13 = str40;
                                                                str14 = str39;
                                                                num2 = num5;
                                                                num3 = num4;
                                                                hashMap = hashMap2;
                                                                str34 = str38;
                                                                str37 = null;
                                                                str36 = null;
                                                                str35 = null;
                                                                str33 = null;
                                                                z27 = false;
                                                                j8 = 0;
                                                                str30 = null;
                                                                str29 = null;
                                                                str12 = null;
                                                                i8 = 0;
                                                                c5 = 0;
                                                                z9 = false;
                                                                z8 = false;
                                                                z26 = false;
                                                                z7 = false;
                                                                z6 = false;
                                                                z5 = false;
                                                                z4 = false;
                                                                str11 = null;
                                                                str10 = null;
                                                                str9 = null;
                                                                str24 = null;
                                                                j7 = 0;
                                                                j5 = 0;
                                                                str22 = null;
                                                                str21 = null;
                                                                str20 = null;
                                                                str19 = null;
                                                                str18 = null;
                                                                i7 = -1;
                                                            }
                                                            str34 = str38;
                                                            str37 = null;
                                                            str36 = null;
                                                        }
                                                        str35 = null;
                                                        str33 = null;
                                                    }
                                                    z27 = false;
                                                    j8 = 0;
                                                    str30 = null;
                                                    str29 = null;
                                                }
                                                str12 = null;
                                                i8 = 0;
                                                c5 = 0;
                                                z9 = false;
                                                z8 = false;
                                                z26 = false;
                                                z7 = false;
                                                z6 = false;
                                                z5 = false;
                                                z4 = false;
                                                str11 = null;
                                                str10 = null;
                                                str9 = null;
                                                str24 = null;
                                                str23 = null;
                                                j7 = 0;
                                                j5 = 0;
                                                str22 = null;
                                                str21 = null;
                                                str20 = null;
                                                str19 = null;
                                                str18 = null;
                                                i7 = -1;
                                            }
                                            hashMap = hashMap2;
                                            str34 = str38;
                                            z27 = false;
                                            i8 = 0;
                                            c5 = 0;
                                            z9 = false;
                                            z8 = false;
                                            z26 = false;
                                            z7 = false;
                                            z6 = false;
                                            z5 = false;
                                            z4 = false;
                                            i7 = -1;
                                            str12 = str18;
                                        }
                                    } else {
                                        str28 = scheme;
                                        iArr3 = iArr4;
                                        str45 = uri12;
                                        uri = data;
                                        str32 = null;
                                        str31 = null;
                                        num6 = null;
                                        str27 = null;
                                        str26 = null;
                                        str44 = null;
                                        str43 = null;
                                        str42 = null;
                                        str41 = null;
                                        str40 = null;
                                        str39 = null;
                                        num5 = null;
                                        num4 = null;
                                        hashMap2 = null;
                                    }
                                    str38 = null;
                                    if (!str45.startsWith("tg:invoice")) {
                                    }
                                    j8 = 0;
                                    str19 = Uri.parse(str45.replace("tg:invoice", "tg://invoice")).getQueryParameter("slug");
                                    str37 = null;
                                    str36 = null;
                                    str35 = null;
                                    str33 = null;
                                    str30 = null;
                                    str29 = null;
                                    str11 = null;
                                    str10 = null;
                                    str9 = null;
                                    str24 = null;
                                    str23 = null;
                                    str22 = null;
                                    str21 = null;
                                    str20 = null;
                                    str18 = null;
                                    j7 = 0;
                                    j5 = 0;
                                    num = num6;
                                    str25 = str44;
                                    str17 = str43;
                                    str16 = str42;
                                    str15 = str41;
                                    str13 = str40;
                                    str14 = str39;
                                    num2 = num5;
                                    num3 = num4;
                                    hashMap = hashMap2;
                                    str34 = str38;
                                    z27 = false;
                                    i8 = 0;
                                    c5 = 0;
                                    z9 = false;
                                    z8 = false;
                                    z26 = false;
                                    z7 = false;
                                    z6 = false;
                                    z5 = false;
                                    z4 = false;
                                    i7 = -1;
                                    str12 = str18;
                                    break;
                                case 1:
                                case 2:
                                    String lowerCase = data.getHost().toLowerCase();
                                    if (lowerCase.equals("telegram.me") || lowerCase.equals("t.me") || lowerCase.equals("telegram.dog")) {
                                        String path3 = data.getPath();
                                        if (path3 != null && path3.length() > 1) {
                                            String substring2 = path3.substring(1);
                                            if (substring2.startsWith("$")) {
                                                substring = substring2.substring(1);
                                            } else if (substring2.startsWith("invoice/")) {
                                                substring = substring2.substring(substring2.indexOf(47) + 1);
                                            } else if (substring2.startsWith("bg/")) {
                                                ?? tLRPC$TL_wallPaper2 = new TLRPC$TL_wallPaper();
                                                tLRPC$TL_wallPaper2.settings = new TLRPC$TL_wallPaperSettings();
                                                String replace3 = substring2.replace("bg/", str78);
                                                tLRPC$TL_wallPaper2.slug = replace3;
                                                if (replace3 != null && replace3.length() == 6) {
                                                    tLRPC$TL_wallPaper2.settings.background_color = Integer.parseInt(tLRPC$TL_wallPaper2.slug, 16) | (-16777216);
                                                    tLRPC$TL_wallPaper2.slug = null;
                                                } else {
                                                    String str89 = tLRPC$TL_wallPaper2.slug;
                                                    if (str89 != null && str89.length() >= 13 && AndroidUtilities.isValidWallChar(tLRPC$TL_wallPaper2.slug.charAt(6))) {
                                                        tLRPC$TL_wallPaper2.settings.background_color = Integer.parseInt(tLRPC$TL_wallPaper2.slug.substring(0, 6), 16) | (-16777216);
                                                        tLRPC$TL_wallPaper2.settings.second_background_color = Integer.parseInt(tLRPC$TL_wallPaper2.slug.substring(7, 13), 16) | (-16777216);
                                                        if (tLRPC$TL_wallPaper2.slug.length() >= 20 && AndroidUtilities.isValidWallChar(tLRPC$TL_wallPaper2.slug.charAt(13))) {
                                                            tLRPC$TL_wallPaper2.settings.third_background_color = Integer.parseInt(tLRPC$TL_wallPaper2.slug.substring(14, 20), 16) | (-16777216);
                                                        }
                                                        if (tLRPC$TL_wallPaper2.slug.length() == 27 && AndroidUtilities.isValidWallChar(tLRPC$TL_wallPaper2.slug.charAt(20))) {
                                                            tLRPC$TL_wallPaper2.settings.fourth_background_color = Integer.parseInt(tLRPC$TL_wallPaper2.slug.substring(21), 16) | (-16777216);
                                                        }
                                                        try {
                                                            String queryParameter23 = data.getQueryParameter("rotation");
                                                            if (!TextUtils.isEmpty(queryParameter23)) {
                                                                tLRPC$TL_wallPaper2.settings.rotation = Utilities.parseInt((CharSequence) queryParameter23).intValue();
                                                            }
                                                        } catch (Exception unused7) {
                                                        }
                                                        tLRPC$TL_wallPaper2.slug = null;
                                                    }
                                                    z36 = false;
                                                    if (!z36) {
                                                        String queryParameter24 = data.getQueryParameter("mode");
                                                        if (queryParameter24 != null && (split2 = queryParameter24.toLowerCase().split(" ")) != null && split2.length > 0) {
                                                            for (int i23 = 0; i23 < split2.length; i23++) {
                                                                if ("blur".equals(split2[i23])) {
                                                                    tLRPC$TL_wallPaper2.settings.blur = true;
                                                                } else if ("motion".equals(split2[i23])) {
                                                                    tLRPC$TL_wallPaper2.settings.motion = true;
                                                                }
                                                            }
                                                        }
                                                        String queryParameter25 = data.getQueryParameter("intensity");
                                                        if (!TextUtils.isEmpty(queryParameter25)) {
                                                            tLRPC$TL_wallPaper2.settings.intensity = Utilities.parseInt((CharSequence) queryParameter25).intValue();
                                                        } else {
                                                            tLRPC$TL_wallPaper2.settings.intensity = 50;
                                                        }
                                                        try {
                                                            queryParameter4 = data.getQueryParameter("bg_color");
                                                        } catch (Exception unused8) {
                                                        }
                                                        if (!TextUtils.isEmpty(queryParameter4)) {
                                                            tLRPC$TL_wallPaper2.settings.background_color = Integer.parseInt(queryParameter4.substring(0, 6), 16) | (-16777216);
                                                            if (queryParameter4.length() >= 13) {
                                                                tLRPC$TL_wallPaper2.settings.second_background_color = Integer.parseInt(queryParameter4.substring(7, 13), 16) | (-16777216);
                                                                if (queryParameter4.length() >= 20 && AndroidUtilities.isValidWallChar(queryParameter4.charAt(13))) {
                                                                    tLRPC$TL_wallPaper2.settings.third_background_color = Integer.parseInt(queryParameter4.substring(14, 20), 16) | (-16777216);
                                                                }
                                                                if (queryParameter4.length() == 27 && AndroidUtilities.isValidWallChar(queryParameter4.charAt(20))) {
                                                                    tLRPC$TL_wallPaper2.settings.fourth_background_color = Integer.parseInt(queryParameter4.substring(21), 16) | (-16777216);
                                                                }
                                                            }
                                                            try {
                                                                queryParameter3 = data.getQueryParameter("rotation");
                                                                if (!TextUtils.isEmpty(queryParameter3)) {
                                                                    tLRPC$TL_wallPaper2.settings.rotation = Utilities.parseInt((CharSequence) queryParameter3).intValue();
                                                                }
                                                            } catch (Exception unused9) {
                                                            }
                                                        } else {
                                                            try {
                                                                tLRPC$TL_wallPaper2.settings.background_color = -1;
                                                            } catch (Exception unused10) {
                                                            }
                                                            queryParameter3 = data.getQueryParameter("rotation");
                                                            if (!TextUtils.isEmpty(queryParameter3)) {
                                                            }
                                                        }
                                                    }
                                                    str57 = tLRPC$TL_wallPaper2;
                                                    str71 = null;
                                                    z35 = false;
                                                    str34 = null;
                                                    i10 = -1;
                                                    str33 = null;
                                                    str70 = null;
                                                    str69 = null;
                                                    str68 = null;
                                                    str67 = null;
                                                    str66 = null;
                                                    str65 = null;
                                                    str64 = null;
                                                    str63 = null;
                                                    str62 = null;
                                                    str61 = null;
                                                    str60 = null;
                                                    str59 = null;
                                                    str58 = null;
                                                    str56 = null;
                                                    num9 = null;
                                                    str55 = null;
                                                    num8 = null;
                                                    num7 = null;
                                                    str54 = null;
                                                    str53 = null;
                                                    str52 = null;
                                                    str29 = str71;
                                                    i7 = i10;
                                                    str37 = str65;
                                                    str25 = str64;
                                                    str17 = str63;
                                                    str16 = str62;
                                                    str24 = str60;
                                                    str18 = str59;
                                                    str22 = str58;
                                                    str20 = str57;
                                                    str19 = str56;
                                                    num3 = num9;
                                                    num = num7;
                                                    str15 = str54;
                                                    str14 = str53;
                                                    str13 = str52;
                                                    j8 = 0;
                                                    str12 = null;
                                                    i8 = 0;
                                                    c5 = 0;
                                                    z9 = false;
                                                    z8 = false;
                                                    z6 = false;
                                                    z5 = false;
                                                    z4 = false;
                                                    str11 = null;
                                                    str10 = null;
                                                    str9 = null;
                                                    hashMap = null;
                                                    str23 = null;
                                                    j7 = 0;
                                                    j5 = 0;
                                                    str21 = null;
                                                    str28 = scheme;
                                                    iArr3 = iArr4;
                                                    str32 = str69;
                                                    str26 = str67;
                                                    str35 = str66;
                                                    str30 = str55;
                                                    num2 = num8;
                                                    z26 = false;
                                                    z7 = false;
                                                    z27 = z35;
                                                    str36 = str61;
                                                    str27 = str70;
                                                    str31 = str68;
                                                    break;
                                                }
                                                z36 = true;
                                                if (!z36) {
                                                }
                                                str57 = tLRPC$TL_wallPaper2;
                                                str71 = null;
                                                z35 = false;
                                                str34 = null;
                                                i10 = -1;
                                                str33 = null;
                                                str70 = null;
                                                str69 = null;
                                                str68 = null;
                                                str67 = null;
                                                str66 = null;
                                                str65 = null;
                                                str64 = null;
                                                str63 = null;
                                                str62 = null;
                                                str61 = null;
                                                str60 = null;
                                                str59 = null;
                                                str58 = null;
                                                str56 = null;
                                                num9 = null;
                                                str55 = null;
                                                num8 = null;
                                                num7 = null;
                                                str54 = null;
                                                str53 = null;
                                                str52 = null;
                                                str29 = str71;
                                                i7 = i10;
                                                str37 = str65;
                                                str25 = str64;
                                                str17 = str63;
                                                str16 = str62;
                                                str24 = str60;
                                                str18 = str59;
                                                str22 = str58;
                                                str20 = str57;
                                                str19 = str56;
                                                num3 = num9;
                                                num = num7;
                                                str15 = str54;
                                                str14 = str53;
                                                str13 = str52;
                                                j8 = 0;
                                                str12 = null;
                                                i8 = 0;
                                                c5 = 0;
                                                z9 = false;
                                                z8 = false;
                                                z6 = false;
                                                z5 = false;
                                                z4 = false;
                                                str11 = null;
                                                str10 = null;
                                                str9 = null;
                                                hashMap = null;
                                                str23 = null;
                                                j7 = 0;
                                                j5 = 0;
                                                str21 = null;
                                                str28 = scheme;
                                                iArr3 = iArr4;
                                                str32 = str69;
                                                str26 = str67;
                                                str35 = str66;
                                                str30 = str55;
                                                num2 = num8;
                                                z26 = false;
                                                z7 = false;
                                                z27 = z35;
                                                str36 = str61;
                                                str27 = str70;
                                                str31 = str68;
                                            } else if (substring2.startsWith("login/")) {
                                                int intValue2 = Utilities.parseInt((CharSequence) substring2.replace("login/", str78)).intValue();
                                                str58 = intValue2 != 0 ? str78 + intValue2 : null;
                                                str71 = null;
                                                z35 = false;
                                                str34 = null;
                                                i10 = -1;
                                                str33 = null;
                                                str70 = null;
                                                str69 = null;
                                                str68 = null;
                                                str67 = null;
                                                str66 = null;
                                                str65 = null;
                                                str64 = null;
                                                str63 = null;
                                                str62 = null;
                                                str61 = null;
                                                str60 = null;
                                                str59 = null;
                                                str57 = null;
                                                str56 = null;
                                                num9 = null;
                                                str55 = null;
                                                num8 = null;
                                                num7 = null;
                                                str54 = null;
                                                str53 = null;
                                                str52 = null;
                                                str29 = str71;
                                                i7 = i10;
                                                str37 = str65;
                                                str25 = str64;
                                                str17 = str63;
                                                str16 = str62;
                                                str24 = str60;
                                                str18 = str59;
                                                str22 = str58;
                                                str20 = str57;
                                                str19 = str56;
                                                num3 = num9;
                                                num = num7;
                                                str15 = str54;
                                                str14 = str53;
                                                str13 = str52;
                                                j8 = 0;
                                                str12 = null;
                                                i8 = 0;
                                                c5 = 0;
                                                z9 = false;
                                                z8 = false;
                                                z6 = false;
                                                z5 = false;
                                                z4 = false;
                                                str11 = null;
                                                str10 = null;
                                                str9 = null;
                                                hashMap = null;
                                                str23 = null;
                                                j7 = 0;
                                                j5 = 0;
                                                str21 = null;
                                                str28 = scheme;
                                                iArr3 = iArr4;
                                                str32 = str69;
                                                str26 = str67;
                                                str35 = str66;
                                                str30 = str55;
                                                num2 = num8;
                                                z26 = false;
                                                z7 = false;
                                                z27 = z35;
                                                str36 = str61;
                                                str27 = str70;
                                                str31 = str68;
                                            } else {
                                                if (substring2.startsWith("joinchat/")) {
                                                    str71 = substring2.replace("joinchat/", str78);
                                                } else if (substring2.startsWith("+")) {
                                                    str71 = substring2.replace("+", str78);
                                                    if (AndroidUtilities.isNumeric(str71)) {
                                                        str34 = str71;
                                                        str71 = null;
                                                        z35 = false;
                                                        i10 = -1;
                                                        str33 = null;
                                                        str70 = null;
                                                        str69 = null;
                                                        str68 = null;
                                                        str67 = null;
                                                        str66 = null;
                                                        str65 = null;
                                                        str64 = null;
                                                        str63 = null;
                                                        str62 = null;
                                                        str61 = null;
                                                        str60 = null;
                                                        str59 = null;
                                                        str58 = null;
                                                        str57 = null;
                                                        str56 = null;
                                                        num9 = null;
                                                        str55 = null;
                                                        num8 = null;
                                                        num7 = null;
                                                        str54 = null;
                                                        str53 = null;
                                                        str52 = null;
                                                        str29 = str71;
                                                        i7 = i10;
                                                        str37 = str65;
                                                        str25 = str64;
                                                        str17 = str63;
                                                        str16 = str62;
                                                        str24 = str60;
                                                        str18 = str59;
                                                        str22 = str58;
                                                        str20 = str57;
                                                        str19 = str56;
                                                        num3 = num9;
                                                        num = num7;
                                                        str15 = str54;
                                                        str14 = str53;
                                                        str13 = str52;
                                                        j8 = 0;
                                                        str12 = null;
                                                        i8 = 0;
                                                        c5 = 0;
                                                        z9 = false;
                                                        z8 = false;
                                                        z6 = false;
                                                        z5 = false;
                                                        z4 = false;
                                                        str11 = null;
                                                        str10 = null;
                                                        str9 = null;
                                                        hashMap = null;
                                                        str23 = null;
                                                        j7 = 0;
                                                        j5 = 0;
                                                        str21 = null;
                                                        str28 = scheme;
                                                        iArr3 = iArr4;
                                                        str32 = str69;
                                                        str26 = str67;
                                                        str35 = str66;
                                                        str30 = str55;
                                                        num2 = num8;
                                                        z26 = false;
                                                        z7 = false;
                                                        z27 = z35;
                                                        str36 = str61;
                                                        str27 = str70;
                                                        str31 = str68;
                                                    }
                                                } else if (substring2.startsWith("addstickers/")) {
                                                    str33 = substring2.replace("addstickers/", str78);
                                                    str71 = null;
                                                    z35 = false;
                                                    str34 = null;
                                                    i10 = -1;
                                                    str70 = null;
                                                    str69 = null;
                                                    str68 = null;
                                                    str67 = null;
                                                    str66 = null;
                                                    str65 = null;
                                                    str64 = null;
                                                    str63 = null;
                                                    str62 = null;
                                                    str61 = null;
                                                    str60 = null;
                                                    str59 = null;
                                                    str58 = null;
                                                    str57 = null;
                                                    str56 = null;
                                                    num9 = null;
                                                    str55 = null;
                                                    num8 = null;
                                                    num7 = null;
                                                    str54 = null;
                                                    str53 = null;
                                                    str52 = null;
                                                    str29 = str71;
                                                    i7 = i10;
                                                    str37 = str65;
                                                    str25 = str64;
                                                    str17 = str63;
                                                    str16 = str62;
                                                    str24 = str60;
                                                    str18 = str59;
                                                    str22 = str58;
                                                    str20 = str57;
                                                    str19 = str56;
                                                    num3 = num9;
                                                    num = num7;
                                                    str15 = str54;
                                                    str14 = str53;
                                                    str13 = str52;
                                                    j8 = 0;
                                                    str12 = null;
                                                    i8 = 0;
                                                    c5 = 0;
                                                    z9 = false;
                                                    z8 = false;
                                                    z6 = false;
                                                    z5 = false;
                                                    z4 = false;
                                                    str11 = null;
                                                    str10 = null;
                                                    str9 = null;
                                                    hashMap = null;
                                                    str23 = null;
                                                    j7 = 0;
                                                    j5 = 0;
                                                    str21 = null;
                                                    str28 = scheme;
                                                    iArr3 = iArr4;
                                                    str32 = str69;
                                                    str26 = str67;
                                                    str35 = str66;
                                                    str30 = str55;
                                                    num2 = num8;
                                                    z26 = false;
                                                    z7 = false;
                                                    z27 = z35;
                                                    str36 = str61;
                                                    str27 = str70;
                                                    str31 = str68;
                                                } else if (substring2.startsWith("msg/") || substring2.startsWith("share/")) {
                                                    String queryParameter26 = data.getQueryParameter("url");
                                                    if (queryParameter26 != null) {
                                                        str78 = queryParameter26;
                                                    }
                                                    if (data.getQueryParameter("text") != null) {
                                                        if (str78.length() > 0) {
                                                            str78 = str78 + "\n";
                                                            z35 = true;
                                                        } else {
                                                            z35 = false;
                                                        }
                                                        str78 = str78 + data.getQueryParameter("text");
                                                    } else {
                                                        z35 = false;
                                                    }
                                                    if (str78.length() > 16384) {
                                                        i11 = 0;
                                                        str72 = str78.substring(0, 16384);
                                                    } else {
                                                        i11 = 0;
                                                        str72 = str78;
                                                    }
                                                    while (str72.endsWith("\n")) {
                                                        str72 = str72.substring(i11, str72.length() - 1);
                                                    }
                                                    str66 = str72;
                                                    str71 = null;
                                                    str34 = null;
                                                    i10 = -1;
                                                    str33 = null;
                                                    str70 = null;
                                                    str69 = null;
                                                    str68 = null;
                                                    str67 = null;
                                                    str65 = null;
                                                    str64 = null;
                                                    str63 = null;
                                                    str62 = null;
                                                    str61 = null;
                                                    str60 = null;
                                                    str59 = null;
                                                    str58 = null;
                                                    str57 = null;
                                                    str56 = null;
                                                    num9 = null;
                                                    str55 = null;
                                                    num8 = null;
                                                    num7 = null;
                                                    str54 = null;
                                                    str53 = null;
                                                    str52 = null;
                                                    str29 = str71;
                                                    i7 = i10;
                                                    str37 = str65;
                                                    str25 = str64;
                                                    str17 = str63;
                                                    str16 = str62;
                                                    str24 = str60;
                                                    str18 = str59;
                                                    str22 = str58;
                                                    str20 = str57;
                                                    str19 = str56;
                                                    num3 = num9;
                                                    num = num7;
                                                    str15 = str54;
                                                    str14 = str53;
                                                    str13 = str52;
                                                    j8 = 0;
                                                    str12 = null;
                                                    i8 = 0;
                                                    c5 = 0;
                                                    z9 = false;
                                                    z8 = false;
                                                    z6 = false;
                                                    z5 = false;
                                                    z4 = false;
                                                    str11 = null;
                                                    str10 = null;
                                                    str9 = null;
                                                    hashMap = null;
                                                    str23 = null;
                                                    j7 = 0;
                                                    j5 = 0;
                                                    str21 = null;
                                                    str28 = scheme;
                                                    iArr3 = iArr4;
                                                    str32 = str69;
                                                    str26 = str67;
                                                    str35 = str66;
                                                    str30 = str55;
                                                    num2 = num8;
                                                    z26 = false;
                                                    z7 = false;
                                                    z27 = z35;
                                                    str36 = str61;
                                                    str27 = str70;
                                                    str31 = str68;
                                                } else if (substring2.startsWith("confirmphone")) {
                                                    String queryParameter27 = data.getQueryParameter("phone");
                                                    str61 = data.getQueryParameter("hash");
                                                    str65 = queryParameter27;
                                                    str71 = null;
                                                    z35 = false;
                                                    str34 = null;
                                                    i10 = -1;
                                                    str33 = null;
                                                    str70 = null;
                                                    str69 = null;
                                                    str68 = null;
                                                    str67 = null;
                                                    str66 = null;
                                                    str64 = null;
                                                    str63 = null;
                                                    str62 = null;
                                                    str60 = null;
                                                    str59 = null;
                                                    str58 = null;
                                                    str57 = null;
                                                    str56 = null;
                                                    num9 = null;
                                                    str55 = null;
                                                    num8 = null;
                                                    num7 = null;
                                                    str54 = null;
                                                    str53 = null;
                                                    str52 = null;
                                                    str29 = str71;
                                                    i7 = i10;
                                                    str37 = str65;
                                                    str25 = str64;
                                                    str17 = str63;
                                                    str16 = str62;
                                                    str24 = str60;
                                                    str18 = str59;
                                                    str22 = str58;
                                                    str20 = str57;
                                                    str19 = str56;
                                                    num3 = num9;
                                                    num = num7;
                                                    str15 = str54;
                                                    str14 = str53;
                                                    str13 = str52;
                                                    j8 = 0;
                                                    str12 = null;
                                                    i8 = 0;
                                                    c5 = 0;
                                                    z9 = false;
                                                    z8 = false;
                                                    z6 = false;
                                                    z5 = false;
                                                    z4 = false;
                                                    str11 = null;
                                                    str10 = null;
                                                    str9 = null;
                                                    hashMap = null;
                                                    str23 = null;
                                                    j7 = 0;
                                                    j5 = 0;
                                                    str21 = null;
                                                    str28 = scheme;
                                                    iArr3 = iArr4;
                                                    str32 = str69;
                                                    str26 = str67;
                                                    str35 = str66;
                                                    str30 = str55;
                                                    num2 = num8;
                                                    z26 = false;
                                                    z7 = false;
                                                    z27 = z35;
                                                    str36 = str61;
                                                    str27 = str70;
                                                    str31 = str68;
                                                } else if (substring2.startsWith("setlanguage/")) {
                                                    str60 = substring2.substring(12);
                                                    str71 = null;
                                                    z35 = false;
                                                    str34 = null;
                                                    i10 = -1;
                                                    str33 = null;
                                                    str70 = null;
                                                    str69 = null;
                                                    str68 = null;
                                                    str67 = null;
                                                    str66 = null;
                                                    str65 = null;
                                                    str64 = null;
                                                    str63 = null;
                                                    str62 = null;
                                                    str61 = null;
                                                    str59 = null;
                                                    str58 = null;
                                                    str57 = null;
                                                    str56 = null;
                                                    num9 = null;
                                                    str55 = null;
                                                    num8 = null;
                                                    num7 = null;
                                                    str54 = null;
                                                    str53 = null;
                                                    str52 = null;
                                                    str29 = str71;
                                                    i7 = i10;
                                                    str37 = str65;
                                                    str25 = str64;
                                                    str17 = str63;
                                                    str16 = str62;
                                                    str24 = str60;
                                                    str18 = str59;
                                                    str22 = str58;
                                                    str20 = str57;
                                                    str19 = str56;
                                                    num3 = num9;
                                                    num = num7;
                                                    str15 = str54;
                                                    str14 = str53;
                                                    str13 = str52;
                                                    j8 = 0;
                                                    str12 = null;
                                                    i8 = 0;
                                                    c5 = 0;
                                                    z9 = false;
                                                    z8 = false;
                                                    z6 = false;
                                                    z5 = false;
                                                    z4 = false;
                                                    str11 = null;
                                                    str10 = null;
                                                    str9 = null;
                                                    hashMap = null;
                                                    str23 = null;
                                                    j7 = 0;
                                                    j5 = 0;
                                                    str21 = null;
                                                    str28 = scheme;
                                                    iArr3 = iArr4;
                                                    str32 = str69;
                                                    str26 = str67;
                                                    str35 = str66;
                                                    str30 = str55;
                                                    num2 = num8;
                                                    z26 = false;
                                                    z7 = false;
                                                    z27 = z35;
                                                    str36 = str61;
                                                    str27 = str70;
                                                    str31 = str68;
                                                } else if (substring2.startsWith("addtheme/")) {
                                                    str59 = substring2.substring(9);
                                                    str71 = null;
                                                    z35 = false;
                                                    str34 = null;
                                                    i10 = -1;
                                                    str33 = null;
                                                    str70 = null;
                                                    str69 = null;
                                                    str68 = null;
                                                    str67 = null;
                                                    str66 = null;
                                                    str65 = null;
                                                    str64 = null;
                                                    str63 = null;
                                                    str62 = null;
                                                    str61 = null;
                                                    str60 = null;
                                                    str58 = null;
                                                    str57 = null;
                                                    str56 = null;
                                                    num9 = null;
                                                    str55 = null;
                                                    num8 = null;
                                                    num7 = null;
                                                    str54 = null;
                                                    str53 = null;
                                                    str52 = null;
                                                    str29 = str71;
                                                    i7 = i10;
                                                    str37 = str65;
                                                    str25 = str64;
                                                    str17 = str63;
                                                    str16 = str62;
                                                    str24 = str60;
                                                    str18 = str59;
                                                    str22 = str58;
                                                    str20 = str57;
                                                    str19 = str56;
                                                    num3 = num9;
                                                    num = num7;
                                                    str15 = str54;
                                                    str14 = str53;
                                                    str13 = str52;
                                                    j8 = 0;
                                                    str12 = null;
                                                    i8 = 0;
                                                    c5 = 0;
                                                    z9 = false;
                                                    z8 = false;
                                                    z6 = false;
                                                    z5 = false;
                                                    z4 = false;
                                                    str11 = null;
                                                    str10 = null;
                                                    str9 = null;
                                                    hashMap = null;
                                                    str23 = null;
                                                    j7 = 0;
                                                    j5 = 0;
                                                    str21 = null;
                                                    str28 = scheme;
                                                    iArr3 = iArr4;
                                                    str32 = str69;
                                                    str26 = str67;
                                                    str35 = str66;
                                                    str30 = str55;
                                                    num2 = num8;
                                                    z26 = false;
                                                    z7 = false;
                                                    z27 = z35;
                                                    str36 = str61;
                                                    str27 = str70;
                                                    str31 = str68;
                                                } else if (substring2.startsWith("c/")) {
                                                    List<String> pathSegments = data.getPathSegments();
                                                    if (pathSegments.size() == 3) {
                                                        ?? parseLong2 = Utilities.parseLong(pathSegments.get(1));
                                                        num11 = Utilities.parseInt((CharSequence) pathSegments.get(2));
                                                        if (num11.intValue() != 0) {
                                                            int i24 = (parseLong2.longValue() > 0L ? 1 : (parseLong2.longValue() == 0L ? 0 : -1));
                                                            str75 = parseLong2;
                                                            break;
                                                        }
                                                        num11 = null;
                                                        str75 = null;
                                                        num12 = Utilities.parseInt((CharSequence) data.getQueryParameter("thread"));
                                                        str74 = str75;
                                                        if (num12.intValue() == 0) {
                                                            num12 = null;
                                                            str74 = str75;
                                                        }
                                                    } else {
                                                        num12 = null;
                                                        num11 = null;
                                                        str74 = null;
                                                    }
                                                    num8 = num12;
                                                    num9 = num11;
                                                    str55 = str74;
                                                    str71 = null;
                                                    z35 = false;
                                                    str34 = null;
                                                    i10 = -1;
                                                    str33 = null;
                                                    str70 = null;
                                                    str69 = null;
                                                    str68 = null;
                                                    str67 = null;
                                                    str66 = null;
                                                    str65 = null;
                                                    str64 = null;
                                                    str63 = null;
                                                    str62 = null;
                                                    str61 = null;
                                                    str60 = null;
                                                    str59 = null;
                                                    str58 = null;
                                                    str57 = null;
                                                    str56 = null;
                                                    num7 = null;
                                                    str54 = null;
                                                    str53 = null;
                                                    str52 = null;
                                                    str29 = str71;
                                                    i7 = i10;
                                                    str37 = str65;
                                                    str25 = str64;
                                                    str17 = str63;
                                                    str16 = str62;
                                                    str24 = str60;
                                                    str18 = str59;
                                                    str22 = str58;
                                                    str20 = str57;
                                                    str19 = str56;
                                                    num3 = num9;
                                                    num = num7;
                                                    str15 = str54;
                                                    str14 = str53;
                                                    str13 = str52;
                                                    j8 = 0;
                                                    str12 = null;
                                                    i8 = 0;
                                                    c5 = 0;
                                                    z9 = false;
                                                    z8 = false;
                                                    z6 = false;
                                                    z5 = false;
                                                    z4 = false;
                                                    str11 = null;
                                                    str10 = null;
                                                    str9 = null;
                                                    hashMap = null;
                                                    str23 = null;
                                                    j7 = 0;
                                                    j5 = 0;
                                                    str21 = null;
                                                    str28 = scheme;
                                                    iArr3 = iArr4;
                                                    str32 = str69;
                                                    str26 = str67;
                                                    str35 = str66;
                                                    str30 = str55;
                                                    num2 = num8;
                                                    z26 = false;
                                                    z7 = false;
                                                    z27 = z35;
                                                    str36 = str61;
                                                    str27 = str70;
                                                    str31 = str68;
                                                } else if (substring2.length() >= 1) {
                                                    ArrayList arrayList6 = new ArrayList(data.getPathSegments());
                                                    if (arrayList6.size() > 0) {
                                                        i12 = 0;
                                                        if (((String) arrayList6.get(0)).equals("s")) {
                                                            arrayList6.remove(0);
                                                        }
                                                    } else {
                                                        i12 = 0;
                                                    }
                                                    if (arrayList6.size() > 0) {
                                                        str34 = (String) arrayList6.get(i12);
                                                        if (arrayList6.size() > 1) {
                                                            num10 = Utilities.parseInt((CharSequence) arrayList6.get(1));
                                                            break;
                                                        }
                                                        num10 = null;
                                                    } else {
                                                        num10 = null;
                                                        str34 = null;
                                                    }
                                                    i10 = num10 != null ? getTimestampFromLink(data) : -1;
                                                    String queryParameter28 = data.getQueryParameter("start");
                                                    String queryParameter29 = data.getQueryParameter("startgroup");
                                                    String queryParameter30 = data.getQueryParameter("startchannel");
                                                    Integer num13 = num10;
                                                    str67 = data.getQueryParameter("admin");
                                                    String queryParameter31 = data.getQueryParameter("game");
                                                    String queryParameter32 = data.getQueryParameter("voicechat");
                                                    String queryParameter33 = data.getQueryParameter("livestream");
                                                    String queryParameter34 = data.getQueryParameter("startattach");
                                                    String queryParameter35 = data.getQueryParameter("choose");
                                                    String queryParameter36 = data.getQueryParameter("attach");
                                                    Integer parseInt7 = Utilities.parseInt((CharSequence) data.getQueryParameter("thread"));
                                                    if (parseInt7.intValue() == 0) {
                                                        str73 = queryParameter36;
                                                        parseInt7 = null;
                                                    } else {
                                                        str73 = queryParameter36;
                                                    }
                                                    Integer parseInt8 = Utilities.parseInt((CharSequence) data.getQueryParameter("comment"));
                                                    num9 = num13;
                                                    if (parseInt8.intValue() == 0) {
                                                        num8 = parseInt7;
                                                        str68 = queryParameter30;
                                                        str54 = queryParameter34;
                                                        str52 = queryParameter35;
                                                        str53 = str73;
                                                        str71 = null;
                                                        z35 = false;
                                                        str33 = null;
                                                        str61 = null;
                                                        str60 = null;
                                                        str59 = null;
                                                        str58 = null;
                                                        str57 = null;
                                                        str56 = null;
                                                        str55 = null;
                                                        num7 = null;
                                                    } else {
                                                        num7 = parseInt8;
                                                        num8 = parseInt7;
                                                        str68 = queryParameter30;
                                                        str54 = queryParameter34;
                                                        str52 = queryParameter35;
                                                        str53 = str73;
                                                        str71 = null;
                                                        z35 = false;
                                                        str33 = null;
                                                        str61 = null;
                                                        str60 = null;
                                                        str59 = null;
                                                        str58 = null;
                                                        str57 = null;
                                                        str56 = null;
                                                        str55 = null;
                                                    }
                                                    str69 = queryParameter29;
                                                    str63 = queryParameter32;
                                                    str62 = queryParameter33;
                                                    str65 = null;
                                                    str70 = queryParameter28;
                                                    str64 = queryParameter31;
                                                    str66 = null;
                                                    str29 = str71;
                                                    i7 = i10;
                                                    str37 = str65;
                                                    str25 = str64;
                                                    str17 = str63;
                                                    str16 = str62;
                                                    str24 = str60;
                                                    str18 = str59;
                                                    str22 = str58;
                                                    str20 = str57;
                                                    str19 = str56;
                                                    num3 = num9;
                                                    num = num7;
                                                    str15 = str54;
                                                    str14 = str53;
                                                    str13 = str52;
                                                    j8 = 0;
                                                    str12 = null;
                                                    i8 = 0;
                                                    c5 = 0;
                                                    z9 = false;
                                                    z8 = false;
                                                    z6 = false;
                                                    z5 = false;
                                                    z4 = false;
                                                    str11 = null;
                                                    str10 = null;
                                                    str9 = null;
                                                    hashMap = null;
                                                    str23 = null;
                                                    j7 = 0;
                                                    j5 = 0;
                                                    str21 = null;
                                                    str28 = scheme;
                                                    iArr3 = iArr4;
                                                    str32 = str69;
                                                    str26 = str67;
                                                    str35 = str66;
                                                    str30 = str55;
                                                    num2 = num8;
                                                    z26 = false;
                                                    z7 = false;
                                                    z27 = z35;
                                                    str36 = str61;
                                                    str27 = str70;
                                                    str31 = str68;
                                                }
                                                z35 = false;
                                                str34 = null;
                                                i10 = -1;
                                                str33 = null;
                                                str70 = null;
                                                str69 = null;
                                                str68 = null;
                                                str67 = null;
                                                str66 = null;
                                                str65 = null;
                                                str64 = null;
                                                str63 = null;
                                                str62 = null;
                                                str61 = null;
                                                str60 = null;
                                                str59 = null;
                                                str58 = null;
                                                str57 = null;
                                                str56 = null;
                                                num9 = null;
                                                str55 = null;
                                                num8 = null;
                                                num7 = null;
                                                str54 = null;
                                                str53 = null;
                                                str52 = null;
                                                str29 = str71;
                                                i7 = i10;
                                                str37 = str65;
                                                str25 = str64;
                                                str17 = str63;
                                                str16 = str62;
                                                str24 = str60;
                                                str18 = str59;
                                                str22 = str58;
                                                str20 = str57;
                                                str19 = str56;
                                                num3 = num9;
                                                num = num7;
                                                str15 = str54;
                                                str14 = str53;
                                                str13 = str52;
                                                j8 = 0;
                                                str12 = null;
                                                i8 = 0;
                                                c5 = 0;
                                                z9 = false;
                                                z8 = false;
                                                z6 = false;
                                                z5 = false;
                                                z4 = false;
                                                str11 = null;
                                                str10 = null;
                                                str9 = null;
                                                hashMap = null;
                                                str23 = null;
                                                j7 = 0;
                                                j5 = 0;
                                                str21 = null;
                                                str28 = scheme;
                                                iArr3 = iArr4;
                                                str32 = str69;
                                                str26 = str67;
                                                str35 = str66;
                                                str30 = str55;
                                                num2 = num8;
                                                z26 = false;
                                                z7 = false;
                                                z27 = z35;
                                                str36 = str61;
                                                str27 = str70;
                                                str31 = str68;
                                            }
                                            str56 = substring;
                                            str71 = null;
                                            z35 = false;
                                            str34 = null;
                                            i10 = -1;
                                            str33 = null;
                                            str70 = null;
                                            str69 = null;
                                            str68 = null;
                                            str67 = null;
                                            str66 = null;
                                            str65 = null;
                                            str64 = null;
                                            str63 = null;
                                            str62 = null;
                                            str61 = null;
                                            str60 = null;
                                            str59 = null;
                                            str58 = null;
                                            str57 = null;
                                            num9 = null;
                                            str55 = null;
                                            num8 = null;
                                            num7 = null;
                                            str54 = null;
                                            str53 = null;
                                            str52 = null;
                                            str29 = str71;
                                            i7 = i10;
                                            str37 = str65;
                                            str25 = str64;
                                            str17 = str63;
                                            str16 = str62;
                                            str24 = str60;
                                            str18 = str59;
                                            str22 = str58;
                                            str20 = str57;
                                            str19 = str56;
                                            num3 = num9;
                                            num = num7;
                                            str15 = str54;
                                            str14 = str53;
                                            str13 = str52;
                                            j8 = 0;
                                            str12 = null;
                                            i8 = 0;
                                            c5 = 0;
                                            z9 = false;
                                            z8 = false;
                                            z6 = false;
                                            z5 = false;
                                            z4 = false;
                                            str11 = null;
                                            str10 = null;
                                            str9 = null;
                                            hashMap = null;
                                            str23 = null;
                                            j7 = 0;
                                            j5 = 0;
                                            str21 = null;
                                            str28 = scheme;
                                            iArr3 = iArr4;
                                            str32 = str69;
                                            str26 = str67;
                                            str35 = str66;
                                            str30 = str55;
                                            num2 = num8;
                                            z26 = false;
                                            z7 = false;
                                            z27 = z35;
                                            str36 = str61;
                                            str27 = str70;
                                            str31 = str68;
                                        }
                                        str71 = null;
                                        z35 = false;
                                        str34 = null;
                                        i10 = -1;
                                        str33 = null;
                                        str70 = null;
                                        str69 = null;
                                        str68 = null;
                                        str67 = null;
                                        str66 = null;
                                        str65 = null;
                                        str64 = null;
                                        str63 = null;
                                        str62 = null;
                                        str61 = null;
                                        str60 = null;
                                        str59 = null;
                                        str58 = null;
                                        str57 = null;
                                        str56 = null;
                                        num9 = null;
                                        str55 = null;
                                        num8 = null;
                                        num7 = null;
                                        str54 = null;
                                        str53 = null;
                                        str52 = null;
                                        str29 = str71;
                                        i7 = i10;
                                        str37 = str65;
                                        str25 = str64;
                                        str17 = str63;
                                        str16 = str62;
                                        str24 = str60;
                                        str18 = str59;
                                        str22 = str58;
                                        str20 = str57;
                                        str19 = str56;
                                        num3 = num9;
                                        num = num7;
                                        str15 = str54;
                                        str14 = str53;
                                        str13 = str52;
                                        j8 = 0;
                                        str12 = null;
                                        i8 = 0;
                                        c5 = 0;
                                        z9 = false;
                                        z8 = false;
                                        z6 = false;
                                        z5 = false;
                                        z4 = false;
                                        str11 = null;
                                        str10 = null;
                                        str9 = null;
                                        hashMap = null;
                                        str23 = null;
                                        j7 = 0;
                                        j5 = 0;
                                        str21 = null;
                                        str28 = scheme;
                                        iArr3 = iArr4;
                                        str32 = str69;
                                        str26 = str67;
                                        str35 = str66;
                                        str30 = str55;
                                        num2 = num8;
                                        z26 = false;
                                        z7 = false;
                                        z27 = z35;
                                        str36 = str61;
                                        str27 = str70;
                                        str31 = str68;
                                    }
                                    break;
                                default:
                                    str28 = scheme;
                                    iArr3 = iArr4;
                                    j8 = 0;
                                    str37 = null;
                                    str36 = null;
                                    str35 = null;
                                    str34 = null;
                                    str33 = null;
                                    str32 = null;
                                    str31 = null;
                                    num3 = null;
                                    str30 = null;
                                    str29 = null;
                                    num2 = null;
                                    num = null;
                                    str27 = null;
                                    str26 = null;
                                    str12 = null;
                                    str11 = null;
                                    str10 = null;
                                    str9 = null;
                                    str25 = null;
                                    hashMap = null;
                                    str24 = null;
                                    str23 = null;
                                    str22 = null;
                                    str21 = null;
                                    str20 = null;
                                    str19 = null;
                                    str18 = null;
                                    str17 = null;
                                    str16 = null;
                                    str15 = null;
                                    str14 = null;
                                    str13 = null;
                                    j7 = 0;
                                    j5 = 0;
                                    z27 = false;
                                    i8 = 0;
                                    c5 = 0;
                                    z9 = false;
                                    z8 = false;
                                    z26 = false;
                                    z7 = false;
                                    z6 = false;
                                    z5 = false;
                                    z4 = false;
                                    i7 = -1;
                                    break;
                            }
                            if (intent.hasExtra("actions.fulfillment.extra.ACTION_TOKEN")) {
                                FirebaseUserActions.getInstance(this).end(new AssistActionBuilder().setActionToken(intent.getStringExtra("actions.fulfillment.extra.ACTION_TOKEN")).setActionStatus(UserConfig.getInstance(this.currentAccount).isClientActivated() && "tg".equals(str28) && str23 == null ? "http://schema.org/CompletedActionStatus" : "http://schema.org/FailedActionStatus").build());
                                intent.removeExtra("actions.fulfillment.extra.ACTION_TOKEN");
                            }
                            if (str22 != null && !UserConfig.getInstance(this.currentAccount).isClientActivated()) {
                                str8 = "message_id";
                                launchActivity = this;
                                j = j8;
                                iArr2 = iArr3;
                            } else if (str37 == null || str36 != null) {
                                str8 = "message_id";
                                j = j8;
                                iArr2 = iArr3;
                                launchActivity = this;
                                AlertDialog alertDialog = new AlertDialog(launchActivity, 3);
                                alertDialog.setCanCancel(false);
                                alertDialog.show();
                                tLRPC$TL_account_sendConfirmPhoneCode = new TLRPC$TL_account_sendConfirmPhoneCode();
                                tLRPC$TL_account_sendConfirmPhoneCode.hash = str36;
                                TLRPC$TL_codeSettings tLRPC$TL_codeSettings = new TLRPC$TL_codeSettings();
                                tLRPC$TL_account_sendConfirmPhoneCode.settings = tLRPC$TL_codeSettings;
                                tLRPC$TL_codeSettings.allow_flashcall = false;
                                tLRPC$TL_codeSettings.allow_app_hash = ApplicationLoader.hasPlayServices;
                                SharedPreferences sharedPreferences = ApplicationLoader.applicationContext.getSharedPreferences("mainconfig", 0);
                                if (!tLRPC$TL_account_sendConfirmPhoneCode.settings.allow_app_hash) {
                                    sharedPreferences.edit().putString("sms_hash", BuildVars.SMS_HASH).apply();
                                } else {
                                    sharedPreferences.edit().remove("sms_hash").apply();
                                }
                                Bundle bundle7 = new Bundle();
                                bundle7.putString("phone", str37);
                                ConnectionsManager.getInstance(launchActivity.currentAccount).sendRequest(tLRPC$TL_account_sendConfirmPhoneCode, new LaunchActivity$$ExternalSyntheticLambda82(this, alertDialog, str37, bundle7, tLRPC$TL_account_sendConfirmPhoneCode), 2);
                            } else if (str34 != null || str29 != null || str33 != null || str35 != null || str25 != null || str17 != null || hashMap != null || str23 != null || str24 != null || str22 != null || str20 != null || str19 != null || str30 != null || str18 != null || str21 != null) {
                                j = j8;
                                str8 = "message_id";
                                iArr2 = iArr3;
                                runLinkRequest(iArr3[0], str34, str29, str33, str27, str32, str31, str26, (str35 == null || !str35.startsWith("@")) ? str35 : " " + str35, z27, num3, str30, num2, num, str25, hashMap, str24, str23, str22, str21, str20, str19, str18, str17, str16, 0, i7, str15, str14, str13);
                                launchActivity = this;
                            } else {
                                try {
                                    query = getContentResolver().query(intent.getData(), null, null, null, null);
                                } catch (Exception e6) {
                                    e = e6;
                                }
                                if (query != null) {
                                    try {
                                        if (query.moveToFirst()) {
                                            int intValue3 = Utilities.parseInt((CharSequence) query.getString(query.getColumnIndex("account_name"))).intValue();
                                            int i25 = 0;
                                            while (true) {
                                                if (i25 < 4) {
                                                    if (UserConfig.getInstance(i25).getClientUserId() != intValue3) {
                                                        try {
                                                            i25++;
                                                        } catch (Throwable th6) {
                                                            th = th6;
                                                            try {
                                                                query.close();
                                                            } catch (Throwable unused11) {
                                                            }
                                                            try {
                                                                throw th;
                                                            } catch (Exception e7) {
                                                                e = e7;
                                                                FileLog.e(e);
                                                                str8 = "message_id";
                                                                launchActivity = this;
                                                                j = j8;
                                                                iArr2 = iArr3;
                                                                i6 = i8;
                                                                c4 = c5;
                                                                j6 = j7;
                                                                j4 = j6;
                                                                i = i6;
                                                                c = c4;
                                                                str3 = str12;
                                                                z15 = z26;
                                                                str5 = str11;
                                                                str = str10;
                                                                str2 = str9;
                                                                j3 = j5;
                                                                j2 = j;
                                                                iArr = iArr2;
                                                                str4 = str8;
                                                                i4 = -1;
                                                                i3 = 0;
                                                                i2 = -1;
                                                                z14 = false;
                                                                z13 = false;
                                                                z12 = false;
                                                                z11 = false;
                                                                z10 = false;
                                                                if (UserConfig.getInstance(launchActivity.currentAccount).isClientActivated()) {
                                                                }
                                                                z17 = true;
                                                                z20 = false;
                                                                z18 = z;
                                                                z19 = z20;
                                                                z16 = false;
                                                                r10 = z19;
                                                                if (!z16) {
                                                                }
                                                                if (z38) {
                                                                }
                                                                if (!z10) {
                                                                }
                                                                intent.setAction(r10);
                                                                return z16;
                                                            }
                                                        }
                                                    } else {
                                                        iArr3[0] = i25;
                                                        switchToAccount(iArr3[0], true);
                                                    }
                                                }
                                            }
                                            long j13 = query.getLong(query.getColumnIndex("data4"));
                                            NotificationCenter.getInstance(iArr3[0]).postNotificationName(NotificationCenter.closeChats, new Object[0]);
                                            try {
                                                String string2 = query.getString(query.getColumnIndex("mimetype"));
                                                if (TextUtils.equals(string2, "vnd.android.cursor.item/vnd.org.telegram.messenger.android.call")) {
                                                    j7 = j13;
                                                    z28 = true;
                                                } else {
                                                    j7 = j13;
                                                    z28 = z8;
                                                    if (TextUtils.equals(string2, "vnd.android.cursor.item/vnd.org.telegram.messenger.android.call.video")) {
                                                        z26 = true;
                                                    }
                                                }
                                                if (query != null) {
                                                    try {
                                                        query.close();
                                                    } catch (Exception e8) {
                                                        e = e8;
                                                        z8 = z28;
                                                        FileLog.e(e);
                                                        str8 = "message_id";
                                                        launchActivity = this;
                                                        j = j8;
                                                        iArr2 = iArr3;
                                                        i6 = i8;
                                                        c4 = c5;
                                                        j6 = j7;
                                                        j4 = j6;
                                                        i = i6;
                                                        c = c4;
                                                        str3 = str12;
                                                        z15 = z26;
                                                        str5 = str11;
                                                        str = str10;
                                                        str2 = str9;
                                                        j3 = j5;
                                                        j2 = j;
                                                        iArr = iArr2;
                                                        str4 = str8;
                                                        i4 = -1;
                                                        i3 = 0;
                                                        i2 = -1;
                                                        z14 = false;
                                                        z13 = false;
                                                        z12 = false;
                                                        z11 = false;
                                                        z10 = false;
                                                        if (UserConfig.getInstance(launchActivity.currentAccount).isClientActivated()) {
                                                        }
                                                        z17 = true;
                                                        z20 = false;
                                                        z18 = z;
                                                        z19 = z20;
                                                        z16 = false;
                                                        r10 = z19;
                                                        if (!z16) {
                                                        }
                                                        if (z38) {
                                                        }
                                                        if (!z10) {
                                                        }
                                                        intent.setAction(r10);
                                                        return z16;
                                                    }
                                                }
                                                z8 = z28;
                                                str8 = "message_id";
                                                launchActivity = this;
                                                j = j8;
                                                iArr2 = iArr3;
                                                i6 = i8;
                                                c4 = c5;
                                                j6 = j7;
                                            } catch (Throwable th7) {
                                                th = th7;
                                                j7 = j13;
                                                query.close();
                                                throw th;
                                            }
                                        }
                                    } catch (Throwable th8) {
                                        th = th8;
                                    }
                                }
                                z28 = z8;
                                if (query != null) {
                                }
                                z8 = z28;
                                str8 = "message_id";
                                launchActivity = this;
                                j = j8;
                                iArr2 = iArr3;
                                i6 = i8;
                                c4 = c5;
                                j6 = j7;
                            }
                            i6 = i8;
                            c4 = c5;
                            j6 = j7;
                        }
                        str28 = scheme;
                        iArr3 = iArr4;
                        j8 = 0;
                        str37 = null;
                        str36 = null;
                        str35 = null;
                        str34 = null;
                        str33 = null;
                        str32 = null;
                        str31 = null;
                        num3 = null;
                        str30 = null;
                        str29 = null;
                        num2 = null;
                        num = null;
                        str27 = null;
                        str26 = null;
                        str12 = null;
                        str11 = null;
                        str10 = null;
                        str9 = null;
                        str25 = null;
                        hashMap = null;
                        str24 = null;
                        str23 = null;
                        str22 = null;
                        str21 = null;
                        str20 = null;
                        str19 = null;
                        str18 = null;
                        str17 = null;
                        str16 = null;
                        str15 = null;
                        str14 = null;
                        str13 = null;
                        j7 = 0;
                        j5 = 0;
                        z27 = false;
                        i8 = 0;
                        c5 = 0;
                        z9 = false;
                        z8 = false;
                        z26 = false;
                        z7 = false;
                        z6 = false;
                        z5 = false;
                        z4 = false;
                        i7 = -1;
                        if (intent.hasExtra("actions.fulfillment.extra.ACTION_TOKEN")) {
                        }
                        if (str22 != null) {
                        }
                        if (str37 == null) {
                        }
                        str8 = "message_id";
                        j = j8;
                        iArr2 = iArr3;
                        launchActivity = this;
                        AlertDialog alertDialog2 = new AlertDialog(launchActivity, 3);
                        alertDialog2.setCanCancel(false);
                        alertDialog2.show();
                        tLRPC$TL_account_sendConfirmPhoneCode = new TLRPC$TL_account_sendConfirmPhoneCode();
                        tLRPC$TL_account_sendConfirmPhoneCode.hash = str36;
                        TLRPC$TL_codeSettings tLRPC$TL_codeSettings2 = new TLRPC$TL_codeSettings();
                        tLRPC$TL_account_sendConfirmPhoneCode.settings = tLRPC$TL_codeSettings2;
                        tLRPC$TL_codeSettings2.allow_flashcall = false;
                        tLRPC$TL_codeSettings2.allow_app_hash = ApplicationLoader.hasPlayServices;
                        SharedPreferences sharedPreferences2 = ApplicationLoader.applicationContext.getSharedPreferences("mainconfig", 0);
                        if (!tLRPC$TL_account_sendConfirmPhoneCode.settings.allow_app_hash) {
                        }
                        Bundle bundle72 = new Bundle();
                        bundle72.putString("phone", str37);
                        ConnectionsManager.getInstance(launchActivity.currentAccount).sendRequest(tLRPC$TL_account_sendConfirmPhoneCode, new LaunchActivity$$ExternalSyntheticLambda82(this, alertDialog2, str37, bundle72, tLRPC$TL_account_sendConfirmPhoneCode), 2);
                        i6 = i8;
                        c4 = c5;
                        j6 = j7;
                    } else {
                        str8 = "message_id";
                        iArr2 = iArr4;
                        launchActivity = this;
                        j = 0;
                        j6 = 0;
                        j5 = 0;
                        i6 = 0;
                        c4 = 0;
                        str12 = null;
                        z9 = false;
                        z8 = false;
                        z26 = false;
                        z7 = false;
                        z6 = false;
                        z5 = false;
                        z4 = false;
                        str11 = null;
                        str10 = null;
                        str9 = null;
                    }
                    j4 = j6;
                    i = i6;
                    c = c4;
                    str3 = str12;
                    z15 = z26;
                    str5 = str11;
                    str = str10;
                    str2 = str9;
                    j3 = j5;
                    j2 = j;
                    iArr = iArr2;
                    str4 = str8;
                    i4 = -1;
                    i3 = 0;
                    i2 = -1;
                    z14 = false;
                    z13 = false;
                    z12 = false;
                    z11 = false;
                    z10 = false;
                    if (UserConfig.getInstance(launchActivity.currentAccount).isClientActivated()) {
                    }
                    z17 = true;
                    z20 = false;
                    z18 = z;
                    z19 = z20;
                    z16 = false;
                    r10 = z19;
                    if (!z16) {
                    }
                    if (z38) {
                    }
                    if (!z10) {
                    }
                    intent.setAction(r10);
                    return z16;
                } else {
                    launchActivity = this;
                    i = 0;
                    int i26 = -1;
                    j = 0;
                    if (!intent.getAction().equals("org.telegram.messenger.OPEN_ACCOUNT")) {
                        if (!intent.getAction().equals("new_dialog")) {
                            if (intent.getAction().startsWith("com.tmessages.openchat")) {
                                long longExtra = intent.getLongExtra("chatId", intent.getIntExtra("chatId", 0));
                                long longExtra2 = intent.getLongExtra("userId", intent.getIntExtra("userId", 0));
                                int intExtra = intent.getIntExtra("encId", 0);
                                int intExtra2 = intent.getIntExtra("appWidgetId", 0);
                                if (intExtra2 != 0) {
                                    i2 = intent.getIntExtra("appWidgetType", 0);
                                    i26 = intExtra2;
                                    j4 = 0;
                                    j3 = 0;
                                    iArr = iArr4;
                                    str4 = "message_id";
                                    intExtra = 0;
                                    z25 = false;
                                    i5 = 0;
                                    c3 = 6;
                                } else {
                                    str4 = "message_id";
                                    i5 = intent.getIntExtra(str4, 0);
                                    if (longExtra != 0) {
                                        iArr = iArr4;
                                        NotificationCenter.getInstance(iArr[0]).postNotificationName(NotificationCenter.closeChats, new Object[0]);
                                        j3 = longExtra;
                                        j4 = 0;
                                    } else {
                                        iArr = iArr4;
                                        if (longExtra2 != 0) {
                                            NotificationCenter.getInstance(iArr[0]).postNotificationName(NotificationCenter.closeChats, new Object[0]);
                                            j4 = longExtra2;
                                            j3 = 0;
                                        } else if (intExtra != 0) {
                                            NotificationCenter.getInstance(iArr[0]).postNotificationName(NotificationCenter.closeChats, new Object[0]);
                                            j4 = 0;
                                            j3 = 0;
                                            i2 = -1;
                                            z25 = false;
                                            c3 = 0;
                                        } else {
                                            j4 = 0;
                                            j3 = 0;
                                            intExtra = 0;
                                            i2 = -1;
                                            z25 = true;
                                            c3 = 0;
                                        }
                                    }
                                    intExtra = 0;
                                    i2 = -1;
                                    z25 = false;
                                    c3 = 0;
                                }
                                i3 = intExtra;
                                z14 = z25;
                                i = i5;
                                c = c3;
                                i4 = i26;
                                j2 = 0;
                                str = null;
                                str5 = null;
                                z15 = false;
                                str3 = null;
                                z13 = false;
                                z12 = false;
                                z11 = false;
                                z10 = false;
                                z9 = false;
                                z8 = false;
                                z7 = false;
                                z6 = false;
                                z5 = false;
                                z4 = false;
                                str2 = null;
                                if (UserConfig.getInstance(launchActivity.currentAccount).isClientActivated()) {
                                }
                                z17 = true;
                                z20 = false;
                                z18 = z;
                                z19 = z20;
                                z16 = false;
                                r10 = z19;
                                if (!z16) {
                                }
                                if (z38) {
                                }
                                if (!z10) {
                                }
                                intent.setAction(r10);
                                return z16;
                            }
                            iArr = iArr4;
                            str4 = "message_id";
                            if (!intent.getAction().equals("com.tmessages.openplayer")) {
                                if (!intent.getAction().equals("org.tmessages.openlocations")) {
                                    if (action.equals("voip_chat")) {
                                        j4 = 0;
                                        j3 = 0;
                                        j2 = 0;
                                        str = null;
                                        i4 = -1;
                                        i3 = 0;
                                        i2 = -1;
                                        str5 = null;
                                        z15 = false;
                                        str3 = null;
                                        c = 0;
                                        z14 = false;
                                        z13 = false;
                                        z12 = false;
                                        z11 = false;
                                        z10 = true;
                                        z9 = false;
                                        z8 = false;
                                        z7 = false;
                                        z6 = false;
                                        z5 = false;
                                        z4 = false;
                                        str2 = null;
                                        if (UserConfig.getInstance(launchActivity.currentAccount).isClientActivated()) {
                                        }
                                        z17 = true;
                                        z20 = false;
                                        z18 = z;
                                        z19 = z20;
                                        z16 = false;
                                        r10 = z19;
                                        if (!z16) {
                                        }
                                        if (z38) {
                                        }
                                        if (!z10) {
                                        }
                                        intent.setAction(r10);
                                        return z16;
                                    }
                                } else {
                                    j4 = 0;
                                    j3 = 0;
                                    j2 = 0;
                                    str = null;
                                    i4 = -1;
                                    i3 = 0;
                                    i2 = -1;
                                    str5 = null;
                                    z15 = false;
                                    str3 = null;
                                    c = 0;
                                    z14 = false;
                                    z13 = false;
                                    z12 = true;
                                    z11 = false;
                                    z10 = false;
                                    z9 = false;
                                    z8 = false;
                                    z7 = false;
                                    z6 = false;
                                    z5 = false;
                                    z4 = false;
                                    str2 = null;
                                    if (UserConfig.getInstance(launchActivity.currentAccount).isClientActivated()) {
                                    }
                                    z17 = true;
                                    z20 = false;
                                    z18 = z;
                                    z19 = z20;
                                    z16 = false;
                                    r10 = z19;
                                    if (!z16) {
                                    }
                                    if (z38) {
                                    }
                                    if (!z10) {
                                    }
                                    intent.setAction(r10);
                                    return z16;
                                }
                            } else {
                                j4 = 0;
                                j3 = 0;
                                j2 = 0;
                                str = null;
                                i4 = -1;
                                i3 = 0;
                                i2 = -1;
                                str5 = null;
                                z15 = false;
                                str3 = null;
                                c = 0;
                                z14 = false;
                                z13 = true;
                                z12 = false;
                                z11 = false;
                                z10 = false;
                                z9 = false;
                                z8 = false;
                                z7 = false;
                                z6 = false;
                                z5 = false;
                                z4 = false;
                                str2 = null;
                                if (UserConfig.getInstance(launchActivity.currentAccount).isClientActivated()) {
                                }
                                z17 = true;
                                z20 = false;
                                z18 = z;
                                z19 = z20;
                                z16 = false;
                                r10 = z19;
                                if (!z16) {
                                }
                                if (z38) {
                                }
                                if (!z10) {
                                }
                                intent.setAction(r10);
                                return z16;
                            }
                        } else {
                            j4 = 0;
                            j3 = 0;
                            j2 = 0;
                            iArr = iArr4;
                            str4 = "message_id";
                            str = null;
                            i4 = -1;
                            i3 = 0;
                            i2 = -1;
                            str5 = null;
                            z15 = false;
                            str3 = null;
                            c = 0;
                            z14 = false;
                            z13 = false;
                            z12 = false;
                            z11 = true;
                            z10 = false;
                            z9 = false;
                            z8 = false;
                            z7 = false;
                            z6 = false;
                            z5 = false;
                            z4 = false;
                            str2 = null;
                            if (UserConfig.getInstance(launchActivity.currentAccount).isClientActivated()) {
                            }
                            z17 = true;
                            z20 = false;
                            z18 = z;
                            z19 = z20;
                            z16 = false;
                            r10 = z19;
                            if (!z16) {
                            }
                            if (z38) {
                            }
                            if (!z10) {
                            }
                            intent.setAction(r10);
                            return z16;
                        }
                    } else {
                        j4 = 0;
                        j3 = 0;
                        j2 = 0;
                        iArr = iArr4;
                        str4 = "message_id";
                        str = null;
                        i4 = -1;
                        i3 = 0;
                        i2 = -1;
                        str5 = null;
                        z15 = false;
                        str3 = null;
                        c = 1;
                        z14 = false;
                        z13 = false;
                        z12 = false;
                        z11 = false;
                        z10 = false;
                        z9 = false;
                        z8 = false;
                        z7 = false;
                        z6 = false;
                        z5 = false;
                        z4 = false;
                        str2 = null;
                        if (UserConfig.getInstance(launchActivity.currentAccount).isClientActivated()) {
                        }
                        z17 = true;
                        z20 = false;
                        z18 = z;
                        z19 = z20;
                        z16 = false;
                        r10 = z19;
                        if (!z16) {
                        }
                        if (z38) {
                        }
                        if (!z10) {
                        }
                        intent.setAction(r10);
                        return z16;
                    }
                }
                j4 = j;
                j3 = j4;
                j2 = j3;
                str = null;
                i4 = -1;
                i3 = 0;
                i2 = -1;
                str5 = null;
                z15 = false;
                str3 = null;
                c = 0;
                z14 = false;
                z13 = false;
                z12 = false;
                z11 = false;
                z10 = false;
                z9 = false;
                z8 = false;
                z7 = false;
                z6 = false;
                z5 = false;
                z4 = false;
                str2 = null;
                if (UserConfig.getInstance(launchActivity.currentAccount).isClientActivated()) {
                }
                z17 = true;
                z20 = false;
                z18 = z;
                z19 = z20;
                z16 = false;
                r10 = z19;
                if (!z16) {
                }
                if (z38) {
                }
                if (!z10) {
                }
                intent.setAction(r10);
                return z16;
            }
        }
        j = 0;
        str4 = "message_id";
        iArr = iArr4;
        launchActivity = this;
        i = 0;
        j4 = j;
        j3 = j4;
        j2 = j3;
        str = null;
        i4 = -1;
        i3 = 0;
        i2 = -1;
        str5 = null;
        z15 = false;
        str3 = null;
        c = 0;
        z14 = false;
        z13 = false;
        z12 = false;
        z11 = false;
        z10 = false;
        z9 = false;
        z8 = false;
        z7 = false;
        z6 = false;
        z5 = false;
        z4 = false;
        str2 = null;
        if (UserConfig.getInstance(launchActivity.currentAccount).isClientActivated()) {
        }
        z17 = true;
        z20 = false;
        z18 = z;
        z19 = z20;
        z16 = false;
        r10 = z19;
        if (!z16) {
        }
        if (z38) {
        }
        if (!z10) {
        }
        intent.setAction(r10);
        return z16;
    }

    public /* synthetic */ void lambda$handleIntent$10(Intent intent, boolean z) {
        handleIntent(intent, true, false, false);
    }

    public /* synthetic */ void lambda$handleIntent$12(AlertDialog alertDialog, String str, Bundle bundle, TLRPC$TL_account_sendConfirmPhoneCode tLRPC$TL_account_sendConfirmPhoneCode, TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        AndroidUtilities.runOnUIThread(new LaunchActivity$$ExternalSyntheticLambda58(this, alertDialog, tLRPC$TL_error, str, bundle, tLObject, tLRPC$TL_account_sendConfirmPhoneCode));
    }

    public /* synthetic */ void lambda$handleIntent$11(AlertDialog alertDialog, TLRPC$TL_error tLRPC$TL_error, String str, Bundle bundle, TLObject tLObject, TLRPC$TL_account_sendConfirmPhoneCode tLRPC$TL_account_sendConfirmPhoneCode) {
        alertDialog.dismiss();
        if (tLRPC$TL_error == null) {
            lambda$runLinkRequest$59(new LoginActivity().cancelAccountDeletion(str, bundle, (TLRPC$TL_auth_sentCode) tLObject));
        } else {
            AlertsCreator.processError(this.currentAccount, tLRPC$TL_error, getActionBarLayout().getLastFragment(), tLRPC$TL_account_sendConfirmPhoneCode, new Object[0]);
        }
    }

    public /* synthetic */ void lambda$handleIntent$14(int[] iArr, LocationController.SharingLocationInfo sharingLocationInfo) {
        iArr[0] = sharingLocationInfo.messageObject.currentAccount;
        switchToAccount(iArr[0], true);
        LocationActivity locationActivity = new LocationActivity(2);
        locationActivity.setMessageObject(sharingLocationInfo.messageObject);
        locationActivity.setDelegate(new LaunchActivity$$ExternalSyntheticLambda103(iArr, sharingLocationInfo.messageObject.getDialogId()));
        lambda$runLinkRequest$59(locationActivity);
    }

    public static /* synthetic */ void lambda$handleIntent$13(int[] iArr, long j, TLRPC$MessageMedia tLRPC$MessageMedia, int i, boolean z, int i2) {
        SendMessagesHelper.getInstance(iArr[0]).sendMessage(tLRPC$MessageMedia, j, (MessageObject) null, (MessageObject) null, (TLRPC$ReplyMarkup) null, (HashMap<String, String>) null, z, i2);
    }

    public /* synthetic */ void lambda$handleIntent$15() {
        if (!this.actionBarLayout.fragmentsStack.isEmpty()) {
            this.actionBarLayout.fragmentsStack.get(0).showDialog(new StickersAlert(this, this.importingStickersSoftware, this.importingStickers, this.importingStickersEmoji, (Theme.ResourcesProvider) null));
        }
    }

    public /* synthetic */ void lambda$handleIntent$16(BaseFragment baseFragment, boolean z) {
        presentFragment(baseFragment, z, false);
    }

    public /* synthetic */ void lambda$handleIntent$17(boolean z, int[] iArr, TLRPC$User tLRPC$User, String str, ContactsActivity contactsActivity) {
        TLRPC$UserFull userFull = MessagesController.getInstance(this.currentAccount).getUserFull(tLRPC$User.id);
        VoIPHelper.startCall(tLRPC$User, z, userFull != null && userFull.video_calls_available, this, userFull, AccountInstance.getInstance(iArr[0]));
    }

    public /* synthetic */ void lambda$handleIntent$21(ActionIntroActivity actionIntroActivity, String str) {
        AlertDialog alertDialog = new AlertDialog(this, 3);
        alertDialog.setCanCancel(false);
        alertDialog.show();
        byte[] decode = Base64.decode(str.substring(17), 8);
        TLRPC$TL_auth_acceptLoginToken tLRPC$TL_auth_acceptLoginToken = new TLRPC$TL_auth_acceptLoginToken();
        tLRPC$TL_auth_acceptLoginToken.token = decode;
        ConnectionsManager.getInstance(this.currentAccount).sendRequest(tLRPC$TL_auth_acceptLoginToken, new LaunchActivity$$ExternalSyntheticLambda68(alertDialog, actionIntroActivity));
    }

    public static /* synthetic */ void lambda$handleIntent$20(AlertDialog alertDialog, ActionIntroActivity actionIntroActivity, TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        AndroidUtilities.runOnUIThread(new LaunchActivity$$ExternalSyntheticLambda23(alertDialog, tLObject, actionIntroActivity, tLRPC$TL_error));
    }

    public static /* synthetic */ void lambda$handleIntent$19(AlertDialog alertDialog, TLObject tLObject, ActionIntroActivity actionIntroActivity, TLRPC$TL_error tLRPC$TL_error) {
        try {
            alertDialog.dismiss();
        } catch (Exception unused) {
        }
        if (!(tLObject instanceof TLRPC$TL_authorization)) {
            AndroidUtilities.runOnUIThread(new LaunchActivity$$ExternalSyntheticLambda24(actionIntroActivity, tLRPC$TL_error));
        }
    }

    public static /* synthetic */ void lambda$handleIntent$18(ActionIntroActivity actionIntroActivity, TLRPC$TL_error tLRPC$TL_error) {
        String string = LocaleController.getString("AuthAnotherClient", 2131624524);
        AlertsCreator.showSimpleAlert(actionIntroActivity, string, LocaleController.getString("ErrorOccurred", 2131625657) + "\n" + tLRPC$TL_error.text);
    }

    public static /* synthetic */ void lambda$handleIntent$22(String str, String str2, BaseFragment baseFragment, DialogInterface dialogInterface, int i) {
        NewContactActivity newContactActivity = new NewContactActivity();
        newContactActivity.setInitialPhoneNumber(str, false);
        if (str2 != null) {
            String[] split = str2.split(" ", 2);
            newContactActivity.setInitialName(split[0], split.length > 1 ? split[1] : null);
        }
        baseFragment.presentFragment(newContactActivity);
    }

    public static int getTimestampFromLink(Uri uri) {
        String str;
        int i;
        if (uri.getPathSegments().contains("video")) {
            str = uri.getQuery();
        } else {
            str = uri.getQueryParameter("t") != null ? uri.getQueryParameter("t") : null;
        }
        if (str != null) {
            try {
                i = Integer.parseInt(str);
            } catch (Throwable unused) {
                i = -1;
            }
            if (i == -1) {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("mm:ss");
                try {
                    return (int) ((simpleDateFormat.parse(str).getTime() - simpleDateFormat.parse("00:00").getTime()) / 1000);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            return i;
        }
        return -1;
    }

    /* JADX WARN: Code restructure failed: missing block: B:13:0x007a, code lost:
        if ((r0.get(r0.size() - 1) instanceof org.telegram.ui.DialogsActivity) != false) goto L18;
     */
    /* JADX WARN: Code restructure failed: missing block: B:17:0x0096, code lost:
        if ((r0.get(r0.size() - 1) instanceof org.telegram.ui.DialogsActivity) != false) goto L18;
     */
    /* JADX WARN: Code restructure failed: missing block: B:18:0x0098, code lost:
        r0 = true;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private void openDialogsToSend(boolean z) {
        boolean z2;
        Bundle bundle = new Bundle();
        bundle.putBoolean("onlySelect", true);
        bundle.putInt("dialogsType", 3);
        bundle.putBoolean("allowSwitchAccount", true);
        ArrayList<TLRPC$User> arrayList = this.contactsToSend;
        if (arrayList != null) {
            if (arrayList.size() != 1) {
                bundle.putString("selectAlertString", LocaleController.getString("SendContactToText", 2131628205));
                bundle.putString("selectAlertStringGroup", LocaleController.getString("SendContactToGroupText", 2131628181));
            }
        } else {
            bundle.putString("selectAlertString", LocaleController.getString("SendMessagesToText", 2131628205));
            bundle.putString("selectAlertStringGroup", LocaleController.getString("SendMessagesToGroupText", 2131628204));
        }
        AnonymousClass12 anonymousClass12 = new AnonymousClass12(bundle);
        anonymousClass12.setDelegate(this);
        if (AndroidUtilities.isTablet()) {
            if (this.layersActionBarLayout.fragmentsStack.size() > 0) {
                ArrayList<BaseFragment> arrayList2 = this.layersActionBarLayout.fragmentsStack;
            }
            z2 = false;
        } else {
            if (this.actionBarLayout.fragmentsStack.size() > 1) {
                ArrayList<BaseFragment> arrayList3 = this.actionBarLayout.fragmentsStack;
            }
            z2 = false;
        }
        this.actionBarLayout.presentFragment(anonymousClass12, z2, !z, true, false);
        if (SecretMediaViewer.hasInstance() && SecretMediaViewer.getInstance().isVisible()) {
            SecretMediaViewer.getInstance().closePhoto(false, false);
        } else if (PhotoViewer.hasInstance() && PhotoViewer.getInstance().isVisible()) {
            PhotoViewer.getInstance().closePhoto(false, true);
        } else if (ArticleViewer.hasInstance() && ArticleViewer.getInstance().isVisible()) {
            ArticleViewer.getInstance().close(false, true);
        }
        GroupCallActivity groupCallActivity = GroupCallActivity.groupCallInstance;
        if (groupCallActivity != null) {
            groupCallActivity.dismiss();
        }
        if (!z) {
            this.drawerLayoutContainer.setAllowOpenDrawer(false, false);
            if (AndroidUtilities.isTablet()) {
                this.actionBarLayout.showLastFragment();
                this.rightActionBarLayout.showLastFragment();
                return;
            }
            this.drawerLayoutContainer.setAllowOpenDrawer(true, false);
        }
    }

    /* renamed from: org.telegram.ui.LaunchActivity$12 */
    /* loaded from: classes3.dex */
    public class AnonymousClass12 extends DialogsActivity {
        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass12(Bundle bundle) {
            super(bundle);
            LaunchActivity.this = r1;
        }

        @Override // org.telegram.ui.DialogsActivity
        public boolean shouldShowNextButton(DialogsActivity dialogsActivity, ArrayList<Long> arrayList, CharSequence charSequence, boolean z) {
            if (LaunchActivity.this.exportingChatUri != null) {
                return false;
            }
            if (LaunchActivity.this.contactsToSend != null && LaunchActivity.this.contactsToSend.size() == 1 && !LaunchActivity.mainFragmentsStack.isEmpty()) {
                return true;
            }
            if (arrayList.size() <= 1) {
                if (LaunchActivity.this.videoPath != null) {
                    return true;
                }
                if (LaunchActivity.this.photoPathsArray != null && LaunchActivity.this.photoPathsArray.size() > 0) {
                    return true;
                }
            }
            return false;
        }
    }

    private int runCommentRequest(int i, AlertDialog alertDialog, Integer num, Integer num2, Integer num3, TLRPC$Chat tLRPC$Chat) {
        if (tLRPC$Chat == null) {
            return 0;
        }
        TLRPC$TL_messages_getDiscussionMessage tLRPC$TL_messages_getDiscussionMessage = new TLRPC$TL_messages_getDiscussionMessage();
        tLRPC$TL_messages_getDiscussionMessage.peer = MessagesController.getInputPeer(tLRPC$Chat);
        tLRPC$TL_messages_getDiscussionMessage.msg_id = (num2 != null ? num : num3).intValue();
        return ConnectionsManager.getInstance(i).sendRequest(tLRPC$TL_messages_getDiscussionMessage, new LaunchActivity$$ExternalSyntheticLambda70(this, i, num, tLRPC$Chat, tLRPC$TL_messages_getDiscussionMessage, num2, num3, alertDialog));
    }

    public /* synthetic */ void lambda$runCommentRequest$24(int i, Integer num, TLRPC$Chat tLRPC$Chat, TLRPC$TL_messages_getDiscussionMessage tLRPC$TL_messages_getDiscussionMessage, Integer num2, Integer num3, AlertDialog alertDialog, TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        AndroidUtilities.runOnUIThread(new LaunchActivity$$ExternalSyntheticLambda41(this, tLObject, i, num, tLRPC$Chat, tLRPC$TL_messages_getDiscussionMessage, num2, num3, alertDialog));
    }

    /* JADX WARN: Removed duplicated region for block: B:26:0x0094 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public /* synthetic */ void lambda$runCommentRequest$23(TLObject tLObject, int i, Integer num, TLRPC$Chat tLRPC$Chat, TLRPC$TL_messages_getDiscussionMessage tLRPC$TL_messages_getDiscussionMessage, Integer num2, Integer num3, AlertDialog alertDialog) {
        boolean z = false;
        try {
            if (tLObject instanceof TLRPC$TL_messages_discussionMessage) {
                TLRPC$TL_messages_discussionMessage tLRPC$TL_messages_discussionMessage = (TLRPC$TL_messages_discussionMessage) tLObject;
                MessagesController.getInstance(i).putUsers(tLRPC$TL_messages_discussionMessage.users, false);
                MessagesController.getInstance(i).putChats(tLRPC$TL_messages_discussionMessage.chats, false);
                ArrayList<MessageObject> arrayList = new ArrayList<>();
                int size = tLRPC$TL_messages_discussionMessage.messages.size();
                for (int i2 = 0; i2 < size; i2++) {
                    arrayList.add(new MessageObject(UserConfig.selectedAccount, tLRPC$TL_messages_discussionMessage.messages.get(i2), true, true));
                }
                if (!arrayList.isEmpty()) {
                    Bundle bundle = new Bundle();
                    bundle.putLong("chat_id", -arrayList.get(0).getDialogId());
                    bundle.putInt("message_id", Math.max(1, num.intValue()));
                    ChatActivity chatActivity = new ChatActivity(bundle);
                    chatActivity.setThreadMessages(arrayList, tLRPC$Chat, tLRPC$TL_messages_getDiscussionMessage.msg_id, tLRPC$TL_messages_discussionMessage.read_inbox_max_id, tLRPC$TL_messages_discussionMessage.read_outbox_max_id);
                    if (num2 != null) {
                        chatActivity.setHighlightMessageId(num2.intValue());
                    } else if (num3 != null) {
                        chatActivity.setHighlightMessageId(num.intValue());
                    }
                    lambda$runLinkRequest$59(chatActivity);
                    z = true;
                    if (!z) {
                        try {
                            if (!mainFragmentsStack.isEmpty()) {
                                ArrayList<BaseFragment> arrayList2 = mainFragmentsStack;
                                BulletinFactory.of(arrayList2.get(arrayList2.size() - 1)).createErrorBulletin(LocaleController.getString("ChannelPostDeleted", 2131624945)).show();
                            }
                        } catch (Exception e) {
                            FileLog.e(e);
                        }
                    }
                    alertDialog.dismiss();
                    return;
                }
            }
            alertDialog.dismiss();
            return;
        } catch (Exception e2) {
            FileLog.e(e2);
            return;
        }
        if (!z) {
        }
    }

    private void runImportRequest(Uri uri, ArrayList<Uri> arrayList) {
        Throwable th;
        Exception e;
        InputStream openInputStream;
        int i = UserConfig.selectedAccount;
        AlertDialog alertDialog = new AlertDialog(this, 3);
        int[] iArr = {0};
        InputStream inputStream = null;
        try {
            try {
                openInputStream = getContentResolver().openInputStream(uri);
            } catch (Throwable th2) {
                th = th2;
            }
        } catch (Exception e2) {
            e = e2;
        }
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(openInputStream));
            StringBuilder sb = new StringBuilder();
            int i2 = 0;
            while (true) {
                String readLine = bufferedReader.readLine();
                if (readLine == null || i2 >= 100) {
                    break;
                }
                sb.append(readLine);
                sb.append('\n');
                i2++;
            }
            String sb2 = sb.toString();
            if (openInputStream != null) {
                try {
                    openInputStream.close();
                } catch (Exception e3) {
                    FileLog.e(e3);
                }
            }
            TLRPC$TL_messages_checkHistoryImport tLRPC$TL_messages_checkHistoryImport = new TLRPC$TL_messages_checkHistoryImport();
            tLRPC$TL_messages_checkHistoryImport.import_head = sb2;
            iArr[0] = ConnectionsManager.getInstance(i).sendRequest(tLRPC$TL_messages_checkHistoryImport, new LaunchActivity$$ExternalSyntheticLambda76(this, uri, i, alertDialog));
            alertDialog.setOnCancelListener(new LaunchActivity$$ExternalSyntheticLambda2(i, iArr, null));
            try {
                alertDialog.showDelayed(300L);
            } catch (Exception unused) {
            }
        } catch (Exception e4) {
            e = e4;
            inputStream = openInputStream;
            FileLog.e(e);
            if (inputStream == null) {
                return;
            }
            try {
                inputStream.close();
            } catch (Exception e5) {
                FileLog.e(e5);
            }
        } catch (Throwable th3) {
            th = th3;
            inputStream = openInputStream;
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (Exception e6) {
                    FileLog.e(e6);
                }
            }
            throw th;
        }
    }

    public /* synthetic */ void lambda$runImportRequest$26(Uri uri, int i, AlertDialog alertDialog, TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        AndroidUtilities.runOnUIThread(new LaunchActivity$$ExternalSyntheticLambda44(this, tLObject, uri, i, alertDialog), 2L);
    }

    /* JADX WARN: Code restructure failed: missing block: B:57:0x011b, code lost:
        if ((r10.get(r10.size() - 1) instanceof org.telegram.ui.DialogsActivity) != false) goto L62;
     */
    /* JADX WARN: Code restructure failed: missing block: B:61:0x0137, code lost:
        if ((r10.get(r10.size() - 1) instanceof org.telegram.ui.DialogsActivity) != false) goto L62;
     */
    /* JADX WARN: Code restructure failed: missing block: B:62:0x0139, code lost:
        r0 = true;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public /* synthetic */ void lambda$runImportRequest$25(TLObject tLObject, Uri uri, int i, AlertDialog alertDialog) {
        boolean z;
        if (!isFinishing()) {
            boolean z2 = false;
            if (tLObject != null && this.actionBarLayout != null) {
                TLRPC$TL_messages_historyImportParsed tLRPC$TL_messages_historyImportParsed = (TLRPC$TL_messages_historyImportParsed) tLObject;
                Bundle bundle = new Bundle();
                bundle.putBoolean("onlySelect", true);
                bundle.putString("importTitle", tLRPC$TL_messages_historyImportParsed.title);
                bundle.putBoolean("allowSwitchAccount", true);
                if (tLRPC$TL_messages_historyImportParsed.pm) {
                    bundle.putInt("dialogsType", 12);
                } else if (tLRPC$TL_messages_historyImportParsed.group) {
                    bundle.putInt("dialogsType", 11);
                } else {
                    String uri2 = uri.toString();
                    Iterator<String> it = MessagesController.getInstance(i).exportPrivateUri.iterator();
                    while (true) {
                        if (!it.hasNext()) {
                            z = false;
                            break;
                        } else if (uri2.contains(it.next())) {
                            bundle.putInt("dialogsType", 12);
                            z = true;
                            break;
                        }
                    }
                    if (!z) {
                        Iterator<String> it2 = MessagesController.getInstance(i).exportGroupUri.iterator();
                        while (true) {
                            if (it2.hasNext()) {
                                if (uri2.contains(it2.next())) {
                                    bundle.putInt("dialogsType", 11);
                                    z = true;
                                    break;
                                }
                            } else {
                                break;
                            }
                        }
                        if (!z) {
                            bundle.putInt("dialogsType", 13);
                        }
                    }
                }
                if (SecretMediaViewer.hasInstance() && SecretMediaViewer.getInstance().isVisible()) {
                    SecretMediaViewer.getInstance().closePhoto(false, false);
                } else if (PhotoViewer.hasInstance() && PhotoViewer.getInstance().isVisible()) {
                    PhotoViewer.getInstance().closePhoto(false, true);
                } else if (ArticleViewer.hasInstance() && ArticleViewer.getInstance().isVisible()) {
                    ArticleViewer.getInstance().close(false, true);
                }
                GroupCallActivity groupCallActivity = GroupCallActivity.groupCallInstance;
                if (groupCallActivity != null) {
                    groupCallActivity.dismiss();
                }
                this.drawerLayoutContainer.setAllowOpenDrawer(false, false);
                if (AndroidUtilities.isTablet()) {
                    this.actionBarLayout.showLastFragment();
                    this.rightActionBarLayout.showLastFragment();
                } else {
                    this.drawerLayoutContainer.setAllowOpenDrawer(true, false);
                }
                DialogsActivity dialogsActivity = new DialogsActivity(bundle);
                dialogsActivity.setDelegate(this);
                if (AndroidUtilities.isTablet()) {
                    if (this.layersActionBarLayout.fragmentsStack.size() > 0) {
                        ArrayList<BaseFragment> arrayList = this.layersActionBarLayout.fragmentsStack;
                    }
                    this.actionBarLayout.presentFragment(dialogsActivity, z2, false, true, false);
                } else {
                    if (this.actionBarLayout.fragmentsStack.size() > 1) {
                        ArrayList<BaseFragment> arrayList2 = this.actionBarLayout.fragmentsStack;
                    }
                    this.actionBarLayout.presentFragment(dialogsActivity, z2, false, true, false);
                }
            } else {
                if (this.documentsUrisArray == null) {
                    this.documentsUrisArray = new ArrayList<>();
                }
                this.documentsUrisArray.add(0, this.exportingChatUri);
                this.exportingChatUri = null;
                openDialogsToSend(true);
            }
            try {
                alertDialog.dismiss();
            } catch (Exception e) {
                FileLog.e(e);
            }
        }
    }

    public static /* synthetic */ void lambda$runImportRequest$27(int i, int[] iArr, Runnable runnable, DialogInterface dialogInterface) {
        ConnectionsManager.getInstance(i).cancelRequest(iArr[0], true);
        if (runnable != null) {
            runnable.run();
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:101:0x0469  */
    /* JADX WARN: Removed duplicated region for block: B:113:? A[RETURN, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:79:0x0365  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private void runLinkRequest(int i, String str, String str2, String str3, String str4, String str5, String str6, String str7, String str8, boolean z, Integer num, Long l, Integer num2, Integer num3, String str9, HashMap<String, String> hashMap, String str10, String str11, String str12, String str13, TLRPC$TL_wallPaper tLRPC$TL_wallPaper, String str14, String str15, String str16, String str17, int i2, int i3, String str18, String str19, String str20) {
        LaunchActivity$$ExternalSyntheticLambda29 launchActivity$$ExternalSyntheticLambda29;
        AlertDialog alertDialog;
        int i4;
        char c;
        int[] iArr;
        LaunchActivity$$ExternalSyntheticLambda29 launchActivity$$ExternalSyntheticLambda292;
        BaseFragment baseFragment;
        Exception e;
        WallpapersListActivity.ColorWallpaper colorWallpaper;
        StickersAlert stickersAlert;
        TLRPC$TL_contacts_resolveUsername tLRPC$TL_contacts_resolveUsername;
        if (i2 == 0 && UserConfig.getActivatedAccountsCount() >= 2 && hashMap != null) {
            AlertsCreator.createAccountSelectDialog(this, new LaunchActivity$$ExternalSyntheticLambda91(this, i, str, str2, str3, str4, str5, str6, str7, str8, z, num, l, num2, num3, str9, hashMap, str10, str11, str12, str13, tLRPC$TL_wallPaper, str14, str15, str16, str17, i3, str18, str19, str20)).show();
            return;
        }
        boolean z2 = true;
        if (str12 != null) {
            NotificationCenter globalInstance = NotificationCenter.getGlobalInstance();
            int i5 = NotificationCenter.didReceiveSmsCode;
            if (globalInstance.hasObservers(i5)) {
                NotificationCenter.getGlobalInstance().postNotificationName(i5, str12);
                return;
            }
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(LocaleController.getString("AppName", 2131624375));
            builder.setMessage(AndroidUtilities.replaceTags(LocaleController.formatString("OtherLoginCode", 2131627120, str12)));
            builder.setPositiveButton(LocaleController.getString("OK", 2131627075), null);
            showAlertDialog(builder);
        } else if (str13 != null) {
            AlertDialog.Builder builder2 = new AlertDialog.Builder(this);
            builder2.setTitle(LocaleController.getString("AuthAnotherClient", 2131624524));
            builder2.setMessage(LocaleController.getString("AuthAnotherClientUrl", 2131624535));
            builder2.setPositiveButton(LocaleController.getString("OK", 2131627075), null);
            showAlertDialog(builder2);
        } else {
            AlertDialog alertDialog2 = new AlertDialog(this, 3);
            int[] iArr2 = {0};
            if (str14 != null) {
                TLRPC$TL_payments_getPaymentForm tLRPC$TL_payments_getPaymentForm = new TLRPC$TL_payments_getPaymentForm();
                TLRPC$TL_inputInvoiceSlug tLRPC$TL_inputInvoiceSlug = new TLRPC$TL_inputInvoiceSlug();
                tLRPC$TL_inputInvoiceSlug.slug = str14;
                tLRPC$TL_payments_getPaymentForm.invoice = tLRPC$TL_inputInvoiceSlug;
                iArr2[0] = ConnectionsManager.getInstance(i).sendRequest(tLRPC$TL_payments_getPaymentForm, new LaunchActivity$$ExternalSyntheticLambda72(this, i, str14, alertDialog2));
                iArr = iArr2;
                alertDialog = alertDialog2;
                launchActivity$$ExternalSyntheticLambda292 = null;
                i4 = i;
                c = 0;
            } else {
                if (str != null) {
                    if (AndroidUtilities.isNumeric(str)) {
                        TLRPC$TL_contacts_resolvePhone tLRPC$TL_contacts_resolvePhone = new TLRPC$TL_contacts_resolvePhone();
                        tLRPC$TL_contacts_resolvePhone.phone = str;
                        tLRPC$TL_contacts_resolveUsername = tLRPC$TL_contacts_resolvePhone;
                    } else {
                        TLRPC$TL_contacts_resolveUsername tLRPC$TL_contacts_resolveUsername2 = new TLRPC$TL_contacts_resolveUsername();
                        tLRPC$TL_contacts_resolveUsername2.username = str;
                        tLRPC$TL_contacts_resolveUsername = tLRPC$TL_contacts_resolveUsername2;
                    }
                    iArr = iArr2;
                    c = 0;
                    iArr[0] = ConnectionsManager.getInstance(i).sendRequest(tLRPC$TL_contacts_resolveUsername, new LaunchActivity$$ExternalSyntheticLambda77(this, str9, str16, str17, i, str18, str19, str20, num, num3, num2, iArr2, alertDialog2, str5, str6, str7, str4, i3, str));
                    i4 = i;
                    alertDialog = alertDialog2;
                } else {
                    iArr = iArr2;
                    c = 0;
                    if (str2 == null) {
                        i4 = i;
                        alertDialog = alertDialog2;
                        if (str3 != null) {
                            if (mainFragmentsStack.isEmpty()) {
                                return;
                            }
                            TLRPC$TL_inputStickerSetShortName tLRPC$TL_inputStickerSetShortName = new TLRPC$TL_inputStickerSetShortName();
                            tLRPC$TL_inputStickerSetShortName.short_name = str3;
                            ArrayList<BaseFragment> arrayList = mainFragmentsStack;
                            BaseFragment baseFragment2 = arrayList.get(arrayList.size() - 1);
                            if (baseFragment2 instanceof ChatActivity) {
                                ChatActivity chatActivity = (ChatActivity) baseFragment2;
                                stickersAlert = new StickersAlert(this, baseFragment2, tLRPC$TL_inputStickerSetShortName, null, chatActivity.getChatActivityEnterViewForStickers(), chatActivity.getResourceProvider());
                                stickersAlert.setCalcMandatoryInsets(chatActivity.isKeyboardVisible());
                            } else {
                                stickersAlert = new StickersAlert(this, baseFragment2, tLRPC$TL_inputStickerSetShortName, (TLRPC$TL_messages_stickerSet) null, (StickersAlert.StickersAlertDelegate) null);
                            }
                            baseFragment2.showDialog(stickersAlert);
                            return;
                        } else if (str8 != null) {
                            Bundle bundle = new Bundle();
                            bundle.putBoolean("onlySelect", true);
                            bundle.putInt("dialogsType", 3);
                            DialogsActivity dialogsActivity = new DialogsActivity(bundle);
                            dialogsActivity.setDelegate(new LaunchActivity$$ExternalSyntheticLambda101(this, z, i4, str8));
                            presentFragment(dialogsActivity, false, true);
                        } else if (hashMap != null) {
                            int intValue = Utilities.parseInt((CharSequence) hashMap.get("bot_id")).intValue();
                            if (intValue == 0) {
                                return;
                            }
                            TLRPC$TL_account_getAuthorizationForm tLRPC$TL_account_getAuthorizationForm = new TLRPC$TL_account_getAuthorizationForm();
                            tLRPC$TL_account_getAuthorizationForm.bot_id = intValue;
                            tLRPC$TL_account_getAuthorizationForm.scope = hashMap.get("scope");
                            tLRPC$TL_account_getAuthorizationForm.public_key = hashMap.get("public_key");
                            iArr[0] = ConnectionsManager.getInstance(i).sendRequest(tLRPC$TL_account_getAuthorizationForm, new LaunchActivity$$ExternalSyntheticLambda87(this, iArr, i, alertDialog, tLRPC$TL_account_getAuthorizationForm, hashMap.get("payload"), hashMap.get("nonce"), hashMap.get("callback_url")));
                        } else if (str11 != null) {
                            TLRPC$TL_help_getDeepLinkInfo tLRPC$TL_help_getDeepLinkInfo = new TLRPC$TL_help_getDeepLinkInfo();
                            tLRPC$TL_help_getDeepLinkInfo.path = str11;
                            iArr[0] = ConnectionsManager.getInstance(this.currentAccount).sendRequest(tLRPC$TL_help_getDeepLinkInfo, new LaunchActivity$$ExternalSyntheticLambda78(this, alertDialog));
                        } else if (str10 != null) {
                            TLRPC$TL_langpack_getLanguage tLRPC$TL_langpack_getLanguage = new TLRPC$TL_langpack_getLanguage();
                            tLRPC$TL_langpack_getLanguage.lang_code = str10;
                            tLRPC$TL_langpack_getLanguage.lang_pack = "android";
                            iArr[0] = ConnectionsManager.getInstance(this.currentAccount).sendRequest(tLRPC$TL_langpack_getLanguage, new LaunchActivity$$ExternalSyntheticLambda80(this, alertDialog));
                        } else if (tLRPC$TL_wallPaper != null) {
                            if (TextUtils.isEmpty(tLRPC$TL_wallPaper.slug)) {
                                try {
                                    TLRPC$WallPaperSettings tLRPC$WallPaperSettings = tLRPC$TL_wallPaper.settings;
                                    int i6 = tLRPC$WallPaperSettings.third_background_color;
                                    if (i6 != 0) {
                                        colorWallpaper = new WallpapersListActivity.ColorWallpaper("c", tLRPC$WallPaperSettings.background_color, tLRPC$WallPaperSettings.second_background_color, i6, tLRPC$WallPaperSettings.fourth_background_color);
                                    } else {
                                        colorWallpaper = new WallpapersListActivity.ColorWallpaper("c", tLRPC$WallPaperSettings.background_color, tLRPC$WallPaperSettings.second_background_color, AndroidUtilities.getWallpaperRotation(tLRPC$WallPaperSettings.rotation, false));
                                    }
                                    launchActivity$$ExternalSyntheticLambda292 = null;
                                } catch (Exception e2) {
                                    e = e2;
                                    launchActivity$$ExternalSyntheticLambda292 = null;
                                }
                                try {
                                    AndroidUtilities.runOnUIThread(new LaunchActivity$$ExternalSyntheticLambda62(this, new ThemePreviewActivity(colorWallpaper, null, true, false)));
                                } catch (Exception e3) {
                                    e = e3;
                                    FileLog.e(e);
                                    z2 = false;
                                    if (!z2) {
                                    }
                                    launchActivity$$ExternalSyntheticLambda29 = launchActivity$$ExternalSyntheticLambda292;
                                    if (iArr[c] == 0) {
                                    }
                                }
                                if (!z2) {
                                    TLRPC$TL_account_getWallPaper tLRPC$TL_account_getWallPaper = new TLRPC$TL_account_getWallPaper();
                                    TLRPC$TL_inputWallPaperSlug tLRPC$TL_inputWallPaperSlug = new TLRPC$TL_inputWallPaperSlug();
                                    tLRPC$TL_inputWallPaperSlug.slug = tLRPC$TL_wallPaper.slug;
                                    tLRPC$TL_account_getWallPaper.wallpaper = tLRPC$TL_inputWallPaperSlug;
                                    iArr[0] = ConnectionsManager.getInstance(this.currentAccount).sendRequest(tLRPC$TL_account_getWallPaper, new LaunchActivity$$ExternalSyntheticLambda83(this, alertDialog, tLRPC$TL_wallPaper));
                                }
                            } else {
                                launchActivity$$ExternalSyntheticLambda292 = null;
                            }
                            z2 = false;
                            if (!z2) {
                            }
                        } else {
                            launchActivity$$ExternalSyntheticLambda292 = null;
                            if (str15 != null) {
                                launchActivity$$ExternalSyntheticLambda29 = new LaunchActivity$$ExternalSyntheticLambda29(this);
                                TLRPC$TL_account_getTheme tLRPC$TL_account_getTheme = new TLRPC$TL_account_getTheme();
                                tLRPC$TL_account_getTheme.format = "android";
                                TLRPC$TL_inputThemeSlug tLRPC$TL_inputThemeSlug = new TLRPC$TL_inputThemeSlug();
                                tLRPC$TL_inputThemeSlug.slug = str15;
                                tLRPC$TL_account_getTheme.theme = tLRPC$TL_inputThemeSlug;
                                iArr[0] = ConnectionsManager.getInstance(this.currentAccount).sendRequest(tLRPC$TL_account_getTheme, new LaunchActivity$$ExternalSyntheticLambda79(this, alertDialog));
                                if (iArr[c] == 0) {
                                    return;
                                }
                                alertDialog.setOnCancelListener(new LaunchActivity$$ExternalSyntheticLambda1(i4, iArr, launchActivity$$ExternalSyntheticLambda29));
                                try {
                                    alertDialog.showDelayed(300L);
                                    return;
                                } catch (Exception unused) {
                                    return;
                                }
                            } else if (l != null && num != null) {
                                if (num2 != null) {
                                    TLRPC$Chat chat = MessagesController.getInstance(i).getChat(l);
                                    if (chat != null) {
                                        iArr[0] = runCommentRequest(i, alertDialog, num, num3, num2, chat);
                                    } else {
                                        TLRPC$TL_channels_getChannels tLRPC$TL_channels_getChannels = new TLRPC$TL_channels_getChannels();
                                        TLRPC$TL_inputChannel tLRPC$TL_inputChannel = new TLRPC$TL_inputChannel();
                                        tLRPC$TL_inputChannel.channel_id = l.longValue();
                                        tLRPC$TL_channels_getChannels.id.add(tLRPC$TL_inputChannel);
                                        iArr[0] = ConnectionsManager.getInstance(this.currentAccount).sendRequest(tLRPC$TL_channels_getChannels, new LaunchActivity$$ExternalSyntheticLambda86(this, iArr, i, alertDialog, num, num3, num2));
                                    }
                                } else {
                                    Bundle bundle2 = new Bundle();
                                    bundle2.putLong("chat_id", l.longValue());
                                    bundle2.putInt("message_id", num.intValue());
                                    if (!mainFragmentsStack.isEmpty()) {
                                        ArrayList<BaseFragment> arrayList2 = mainFragmentsStack;
                                        baseFragment = arrayList2.get(arrayList2.size() - 1);
                                    } else {
                                        baseFragment = null;
                                    }
                                    if (baseFragment == null || MessagesController.getInstance(i).checkCanOpenChat(bundle2, baseFragment)) {
                                        AndroidUtilities.runOnUIThread(new LaunchActivity$$ExternalSyntheticLambda36(this, bundle2, l, iArr, alertDialog, baseFragment, i));
                                    }
                                }
                            }
                        }
                    } else if (i2 == 0) {
                        TLRPC$TL_messages_checkChatInvite tLRPC$TL_messages_checkChatInvite = new TLRPC$TL_messages_checkChatInvite();
                        tLRPC$TL_messages_checkChatInvite.hash = str2;
                        i4 = i;
                        alertDialog = alertDialog2;
                        iArr[0] = ConnectionsManager.getInstance(i).sendRequest(tLRPC$TL_messages_checkChatInvite, new LaunchActivity$$ExternalSyntheticLambda74(this, i4, alertDialog, str2), 2);
                    } else {
                        i4 = i;
                        alertDialog = alertDialog2;
                        if (i2 == 1) {
                            TLRPC$TL_messages_importChatInvite tLRPC$TL_messages_importChatInvite = new TLRPC$TL_messages_importChatInvite();
                            tLRPC$TL_messages_importChatInvite.hash = str2;
                            ConnectionsManager.getInstance(i).sendRequest(tLRPC$TL_messages_importChatInvite, new LaunchActivity$$ExternalSyntheticLambda73(this, i4, alertDialog), 2);
                        }
                    }
                }
                launchActivity$$ExternalSyntheticLambda292 = null;
            }
            launchActivity$$ExternalSyntheticLambda29 = launchActivity$$ExternalSyntheticLambda292;
            if (iArr[c] == 0) {
            }
        }
    }

    public /* synthetic */ void lambda$runLinkRequest$28(int i, String str, String str2, String str3, String str4, String str5, String str6, String str7, String str8, boolean z, Integer num, Long l, Integer num2, Integer num3, String str9, HashMap hashMap, String str10, String str11, String str12, String str13, TLRPC$TL_wallPaper tLRPC$TL_wallPaper, String str14, String str15, String str16, String str17, int i2, String str18, String str19, String str20, int i3) {
        if (i3 != i) {
            switchToAccount(i3, true);
        }
        runLinkRequest(i3, str, str2, str3, str4, str5, str6, str7, str8, z, num, l, num2, num3, str9, hashMap, str10, str11, str12, str13, tLRPC$TL_wallPaper, str14, str15, str16, str17, 1, i2, str18, str19, str20);
    }

    public /* synthetic */ void lambda$runLinkRequest$30(int i, String str, AlertDialog alertDialog, TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        AndroidUtilities.runOnUIThread(new LaunchActivity$$ExternalSyntheticLambda49(this, tLRPC$TL_error, tLObject, i, str, alertDialog));
    }

    public /* synthetic */ void lambda$runLinkRequest$29(TLRPC$TL_error tLRPC$TL_error, TLObject tLObject, int i, String str, AlertDialog alertDialog) {
        if (tLRPC$TL_error != null) {
            ArrayList<BaseFragment> arrayList = mainFragmentsStack;
            BulletinFactory.of(arrayList.get(arrayList.size() - 1)).createErrorBulletin(LocaleController.getString(2131627394)).show();
        } else if (!isFinishing()) {
            if (tLObject instanceof TLRPC$TL_payments_paymentForm) {
                TLRPC$TL_payments_paymentForm tLRPC$TL_payments_paymentForm = (TLRPC$TL_payments_paymentForm) tLObject;
                MessagesController.getInstance(i).putUsers(tLRPC$TL_payments_paymentForm.users, false);
                lambda$runLinkRequest$59(new PaymentFormActivity(tLRPC$TL_payments_paymentForm, str, getActionBarLayout().getLastFragment()));
            } else if (tLObject instanceof TLRPC$TL_payments_paymentReceipt) {
                lambda$runLinkRequest$59(new PaymentFormActivity((TLRPC$TL_payments_paymentReceipt) tLObject));
            }
        }
        try {
            alertDialog.dismiss();
        } catch (Exception e) {
            FileLog.e(e);
        }
    }

    public /* synthetic */ void lambda$runLinkRequest$44(String str, String str2, String str3, int i, String str4, String str5, String str6, Integer num, Integer num2, Integer num3, int[] iArr, AlertDialog alertDialog, String str7, String str8, String str9, String str10, int i2, String str11, TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        AndroidUtilities.runOnUIThread(new LaunchActivity$$ExternalSyntheticLambda45(this, tLObject, tLRPC$TL_error, str, str2, str3, i, str4, str5, str6, num, num2, num3, iArr, alertDialog, str7, str8, str9, str10, i2, str11), 2L);
    }

    /* JADX WARN: Code restructure failed: missing block: B:37:0x0119, code lost:
        if (r29[0] != 0) goto L38;
     */
    /* JADX WARN: Code restructure failed: missing block: B:46:0x017e, code lost:
        if ((r1.get(r1.size() - 1) instanceof org.telegram.ui.DialogsActivity) != false) goto L47;
     */
    /* JADX WARN: Code restructure failed: missing block: B:47:0x0180, code lost:
        r1 = true;
     */
    /* JADX WARN: Code restructure failed: missing block: B:52:0x019d, code lost:
        if ((r1.get(r1.size() - 1) instanceof org.telegram.ui.DialogsActivity) != false) goto L47;
     */
    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:167:0x042f -> B:173:0x0432). Please submit an issue!!! */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public /* synthetic */ void lambda$runLinkRequest$43(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error, String str, String str2, String str3, int i, String str4, String str5, String str6, Integer num, Integer num2, Integer num3, int[] iArr, AlertDialog alertDialog, String str7, String str8, String str9, String str10, int i2, String str11) {
        String str12;
        long j;
        boolean z;
        BaseFragment baseFragment;
        boolean z2;
        if (!isFinishing()) {
            TLRPC$TL_contacts_resolvedPeer tLRPC$TL_contacts_resolvedPeer = (TLRPC$TL_contacts_resolvedPeer) tLObject;
            boolean z3 = true;
            boolean z4 = false;
            if (tLRPC$TL_error == null && this.actionBarLayout != null && ((str == null && str2 == null) || ((str != null && !tLRPC$TL_contacts_resolvedPeer.users.isEmpty()) || ((str2 != null && !tLRPC$TL_contacts_resolvedPeer.chats.isEmpty()) || (str3 != null && !tLRPC$TL_contacts_resolvedPeer.chats.isEmpty()))))) {
                MessagesController.getInstance(i).putUsers(tLRPC$TL_contacts_resolvedPeer.users, false);
                MessagesController.getInstance(i).putChats(tLRPC$TL_contacts_resolvedPeer.chats, false);
                MessagesStorage.getInstance(i).putUsersAndChats(tLRPC$TL_contacts_resolvedPeer.users, tLRPC$TL_contacts_resolvedPeer.chats, false, true);
                if (str4 != null && str5 == null) {
                    TLRPC$User user = MessagesController.getInstance(i).getUser(Long.valueOf(tLRPC$TL_contacts_resolvedPeer.peer.user_id));
                    if (user != null && user.bot) {
                        if (user.bot_attach_menu) {
                            TLRPC$TL_messages_getAttachMenuBot tLRPC$TL_messages_getAttachMenuBot = new TLRPC$TL_messages_getAttachMenuBot();
                            tLRPC$TL_messages_getAttachMenuBot.bot = MessagesController.getInstance(i).getInputUser(tLRPC$TL_contacts_resolvedPeer.peer.user_id);
                            ConnectionsManager.getInstance(i).sendRequest(tLRPC$TL_messages_getAttachMenuBot, new LaunchActivity$$ExternalSyntheticLambda71(this, i, str6, user, str4, tLRPC$TL_contacts_resolvedPeer));
                        } else {
                            ArrayList<BaseFragment> arrayList = mainFragmentsStack;
                            BulletinFactory.of(arrayList.get(arrayList.size() - 1)).createErrorBulletin(LocaleController.getString(2131624708)).show();
                        }
                    } else {
                        ArrayList<BaseFragment> arrayList2 = mainFragmentsStack;
                        BulletinFactory.of(arrayList2.get(arrayList2.size() - 1)).createErrorBulletin(LocaleController.getString(2131624732)).show();
                    }
                } else if (num != null && ((num2 != null || num3 != null) && !tLRPC$TL_contacts_resolvedPeer.chats.isEmpty())) {
                    iArr[0] = runCommentRequest(i, alertDialog, num, num2, num3, tLRPC$TL_contacts_resolvedPeer.chats.get(0));
                } else if (str != null) {
                    Bundle bundle = new Bundle();
                    bundle.putBoolean("onlySelect", true);
                    bundle.putBoolean("cantSendToChannels", true);
                    bundle.putInt("dialogsType", 1);
                    bundle.putString("selectAlertString", LocaleController.getString("SendGameToText", 2131628187));
                    bundle.putString("selectAlertStringGroup", LocaleController.getString("SendGameToGroupText", 2131628186));
                    DialogsActivity dialogsActivity = new DialogsActivity(bundle);
                    dialogsActivity.setDelegate(new LaunchActivity$$ExternalSyntheticLambda99(this, str, i, tLRPC$TL_contacts_resolvedPeer));
                    if (AndroidUtilities.isTablet()) {
                        if (this.layersActionBarLayout.fragmentsStack.size() > 0) {
                            ArrayList<BaseFragment> arrayList3 = this.layersActionBarLayout.fragmentsStack;
                        }
                        z2 = false;
                    } else {
                        if (this.actionBarLayout.fragmentsStack.size() > 1) {
                            ArrayList<BaseFragment> arrayList4 = this.actionBarLayout.fragmentsStack;
                        }
                        z2 = false;
                    }
                    this.actionBarLayout.presentFragment(dialogsActivity, z2, true, true, false);
                    if (SecretMediaViewer.hasInstance() && SecretMediaViewer.getInstance().isVisible()) {
                        SecretMediaViewer.getInstance().closePhoto(false, false);
                    } else if (PhotoViewer.hasInstance() && PhotoViewer.getInstance().isVisible()) {
                        PhotoViewer.getInstance().closePhoto(false, true);
                    } else if (ArticleViewer.hasInstance() && ArticleViewer.getInstance().isVisible()) {
                        ArticleViewer.getInstance().close(false, true);
                    }
                    GroupCallActivity groupCallActivity = GroupCallActivity.groupCallInstance;
                    if (groupCallActivity != null) {
                        groupCallActivity.dismiss();
                    }
                    this.drawerLayoutContainer.setAllowOpenDrawer(false, false);
                    if (AndroidUtilities.isTablet()) {
                        this.actionBarLayout.showLastFragment();
                        this.rightActionBarLayout.showLastFragment();
                    } else {
                        this.drawerLayoutContainer.setAllowOpenDrawer(true, false);
                    }
                } else if (str7 != null || str8 != null) {
                    TLRPC$User tLRPC$User = !tLRPC$TL_contacts_resolvedPeer.users.isEmpty() ? tLRPC$TL_contacts_resolvedPeer.users.get(0) : null;
                    if (tLRPC$User == null || (tLRPC$User.bot && tLRPC$User.bot_nochats)) {
                        try {
                            if (mainFragmentsStack.isEmpty()) {
                                return;
                            }
                            ArrayList<BaseFragment> arrayList5 = mainFragmentsStack;
                            BulletinFactory.of(arrayList5.get(arrayList5.size() - 1)).createErrorBulletin(LocaleController.getString("BotCantJoinGroups", 2131624709)).show();
                            return;
                        } catch (Exception e) {
                            FileLog.e(e);
                            return;
                        }
                    }
                    Bundle bundle2 = new Bundle();
                    bundle2.putBoolean("onlySelect", true);
                    bundle2.putInt("dialogsType", 2);
                    bundle2.putBoolean("resetDelegate", false);
                    bundle2.putBoolean("closeFragment", false);
                    bundle2.putBoolean("allowGroups", str7 != null);
                    if (str8 != null) {
                        z4 = true;
                    }
                    bundle2.putBoolean("allowChannels", z4);
                    String str13 = TextUtils.isEmpty(str7) ? TextUtils.isEmpty(str8) ? null : str8 : str7;
                    DialogsActivity dialogsActivity2 = new DialogsActivity(bundle2);
                    dialogsActivity2.setDelegate(new LaunchActivity$$ExternalSyntheticLambda98(this, i, tLRPC$User, str9, str13, dialogsActivity2));
                    lambda$runLinkRequest$59(dialogsActivity2);
                } else {
                    Bundle bundle3 = new Bundle();
                    if (!tLRPC$TL_contacts_resolvedPeer.chats.isEmpty()) {
                        bundle3.putLong("chat_id", tLRPC$TL_contacts_resolvedPeer.chats.get(0).id);
                        j = -tLRPC$TL_contacts_resolvedPeer.chats.get(0).id;
                    } else {
                        bundle3.putLong("user_id", tLRPC$TL_contacts_resolvedPeer.users.get(0).id);
                        j = tLRPC$TL_contacts_resolvedPeer.users.get(0).id;
                    }
                    if (str10 == null || tLRPC$TL_contacts_resolvedPeer.users.size() <= 0 || !tLRPC$TL_contacts_resolvedPeer.users.get(0).bot) {
                        z = false;
                    } else {
                        bundle3.putString("botUser", str10);
                        z = true;
                    }
                    if (this.navigateToPremiumBot) {
                        this.navigateToPremiumBot = false;
                        bundle3.putBoolean("premium_bot", true);
                    }
                    if (num != null) {
                        bundle3.putInt("message_id", num.intValue());
                    }
                    if (str2 != null) {
                        bundle3.putString("voicechat", str2);
                    }
                    if (str3 != null) {
                        bundle3.putString("livestream", str3);
                    }
                    if (i2 >= 0) {
                        bundle3.putInt("video_timestamp", i2);
                    }
                    if (str5 != null) {
                        bundle3.putString("attach_bot", str5);
                    }
                    if (str4 != null) {
                        bundle3.putString("attach_bot_start_command", str4);
                    }
                    if (mainFragmentsStack.isEmpty() || str2 != null) {
                        baseFragment = null;
                    } else {
                        ArrayList<BaseFragment> arrayList6 = mainFragmentsStack;
                        baseFragment = arrayList6.get(arrayList6.size() - 1);
                    }
                    if (baseFragment == null || MessagesController.getInstance(i).checkCanOpenChat(bundle3, baseFragment)) {
                        if (z && (baseFragment instanceof ChatActivity)) {
                            ChatActivity chatActivity = (ChatActivity) baseFragment;
                            if (chatActivity.getDialogId() == j) {
                                chatActivity.setBotUser(str10);
                            }
                        }
                        MessagesController.getInstance(i).ensureMessagesLoaded(j, num == null ? 0 : num.intValue(), new AnonymousClass14(alertDialog, str3, baseFragment, j, bundle3));
                        z3 = false;
                    }
                }
            } else {
                try {
                    if (!mainFragmentsStack.isEmpty()) {
                        ArrayList<BaseFragment> arrayList7 = mainFragmentsStack;
                        BaseFragment baseFragment2 = arrayList7.get(arrayList7.size() - 1);
                        if (tLRPC$TL_error != null && (str12 = tLRPC$TL_error.text) != null && str12.startsWith("FLOOD_WAIT")) {
                            BulletinFactory.of(baseFragment2).createErrorBulletin(LocaleController.getString("FloodWait", 2131625908)).show();
                        } else if (AndroidUtilities.isNumeric(str11)) {
                            BulletinFactory.of(baseFragment2).createErrorBulletin(LocaleController.getString("NoPhoneFound", 2131626846)).show();
                        } else {
                            BulletinFactory.of(baseFragment2).createErrorBulletin(LocaleController.getString("NoUsernameFound", 2131626877)).show();
                        }
                    }
                } catch (Exception e2) {
                    FileLog.e(e2);
                }
            }
            if (!z3) {
                return;
            }
            try {
                alertDialog.dismiss();
            } catch (Exception e3) {
                FileLog.e(e3);
            }
        }
    }

    public /* synthetic */ void lambda$runLinkRequest$36(int i, String str, TLRPC$User tLRPC$User, String str2, TLRPC$TL_contacts_resolvedPeer tLRPC$TL_contacts_resolvedPeer, TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        AndroidUtilities.runOnUIThread(new LaunchActivity$$ExternalSyntheticLambda42(this, tLObject, i, str, tLRPC$User, str2, tLRPC$TL_contacts_resolvedPeer));
    }

    public /* synthetic */ void lambda$runLinkRequest$35(TLObject tLObject, int i, String str, TLRPC$User tLRPC$User, String str2, TLRPC$TL_contacts_resolvedPeer tLRPC$TL_contacts_resolvedPeer) {
        DialogsActivity dialogsActivity;
        String[] split;
        if (tLObject instanceof TLRPC$TL_attachMenuBotsBot) {
            TLRPC$TL_attachMenuBotsBot tLRPC$TL_attachMenuBotsBot = (TLRPC$TL_attachMenuBotsBot) tLObject;
            MessagesController.getInstance(i).putUsers(tLRPC$TL_attachMenuBotsBot.users, false);
            TLRPC$TL_attachMenuBot tLRPC$TL_attachMenuBot = tLRPC$TL_attachMenuBotsBot.bot;
            ArrayList<BaseFragment> arrayList = mainFragmentsStack;
            BaseFragment baseFragment = arrayList.get(arrayList.size() - 1);
            ArrayList arrayList2 = new ArrayList();
            if (!TextUtils.isEmpty(str)) {
                for (String str3 : str.split(" ")) {
                    if (MediaDataController.canShowAttachMenuBotForTarget(tLRPC$TL_attachMenuBot, str3)) {
                        arrayList2.add(str3);
                    }
                }
            }
            if (!arrayList2.isEmpty()) {
                Bundle bundle = new Bundle();
                bundle.putInt("dialogsType", 14);
                bundle.putBoolean("onlySelect", true);
                bundle.putBoolean("allowGroups", arrayList2.contains("groups"));
                bundle.putBoolean("allowUsers", arrayList2.contains("users"));
                bundle.putBoolean("allowChannels", arrayList2.contains("channels"));
                bundle.putBoolean("allowBots", arrayList2.contains("bots"));
                DialogsActivity dialogsActivity2 = new DialogsActivity(bundle);
                dialogsActivity2.setDelegate(new LaunchActivity$$ExternalSyntheticLambda100(this, tLRPC$User, str2, i));
                dialogsActivity = dialogsActivity2;
            } else {
                dialogsActivity = null;
            }
            if (tLRPC$TL_attachMenuBot.inactive) {
                AttachBotIntroTopView attachBotIntroTopView = new AttachBotIntroTopView(this);
                attachBotIntroTopView.setColor(Theme.getColor("chat_attachContactIcon"));
                attachBotIntroTopView.setBackgroundColor(Theme.getColor("dialogTopBackground"));
                attachBotIntroTopView.setAttachBot(tLRPC$TL_attachMenuBot);
                new AlertDialog.Builder(this).setTopView(attachBotIntroTopView).setMessage(AndroidUtilities.replaceTags(LocaleController.formatString("BotRequestAttachPermission", 2131624729, UserObject.getUserName(tLRPC$User)))).setPositiveButton(LocaleController.getString(2131624706), new LaunchActivity$$ExternalSyntheticLambda10(this, i, tLRPC$TL_contacts_resolvedPeer, dialogsActivity, baseFragment, tLRPC$User, str2)).setNegativeButton(LocaleController.getString(2131624819), null).show();
                return;
            } else if (dialogsActivity != null) {
                lambda$runLinkRequest$59(dialogsActivity);
                return;
            } else if (baseFragment instanceof ChatActivity) {
                ((ChatActivity) baseFragment).openAttachBotLayout(tLRPC$User.id, str2);
                return;
            } else {
                BulletinFactory.of(baseFragment).createErrorBulletin(LocaleController.getString(2131624707)).show();
                return;
            }
        }
        ArrayList<BaseFragment> arrayList3 = mainFragmentsStack;
        BulletinFactory.of(arrayList3.get(arrayList3.size() - 1)).createErrorBulletin(LocaleController.getString(2131624708)).show();
    }

    public /* synthetic */ void lambda$runLinkRequest$31(TLRPC$User tLRPC$User, String str, int i, DialogsActivity dialogsActivity, ArrayList arrayList, CharSequence charSequence, boolean z) {
        long longValue = ((Long) arrayList.get(0)).longValue();
        Bundle bundle = new Bundle();
        bundle.putBoolean("scrollToTopOnResume", true);
        if (DialogObject.isEncryptedDialog(longValue)) {
            bundle.putInt("enc_id", DialogObject.getEncryptedChatId(longValue));
        } else if (DialogObject.isUserDialog(longValue)) {
            bundle.putLong("user_id", longValue);
        } else {
            bundle.putLong("chat_id", -longValue);
        }
        bundle.putString("attach_bot", tLRPC$User.username);
        if (str != null) {
            bundle.putString("attach_bot_start_command", str);
        }
        if (MessagesController.getInstance(i).checkCanOpenChat(bundle, dialogsActivity)) {
            NotificationCenter.getInstance(i).postNotificationName(NotificationCenter.closeChats, new Object[0]);
            this.actionBarLayout.presentFragment(new ChatActivity(bundle), true, false, true, false);
        }
    }

    public /* synthetic */ void lambda$runLinkRequest$34(int i, TLRPC$TL_contacts_resolvedPeer tLRPC$TL_contacts_resolvedPeer, DialogsActivity dialogsActivity, BaseFragment baseFragment, TLRPC$User tLRPC$User, String str, DialogInterface dialogInterface, int i2) {
        TLRPC$TL_messages_toggleBotInAttachMenu tLRPC$TL_messages_toggleBotInAttachMenu = new TLRPC$TL_messages_toggleBotInAttachMenu();
        tLRPC$TL_messages_toggleBotInAttachMenu.bot = MessagesController.getInstance(i).getInputUser(tLRPC$TL_contacts_resolvedPeer.peer.user_id);
        tLRPC$TL_messages_toggleBotInAttachMenu.enabled = true;
        ConnectionsManager.getInstance(i).sendRequest(tLRPC$TL_messages_toggleBotInAttachMenu, new LaunchActivity$$ExternalSyntheticLambda75(this, i, dialogsActivity, baseFragment, tLRPC$User, str), 66);
    }

    public /* synthetic */ void lambda$runLinkRequest$33(int i, DialogsActivity dialogsActivity, BaseFragment baseFragment, TLRPC$User tLRPC$User, String str, TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        AndroidUtilities.runOnUIThread(new LaunchActivity$$ExternalSyntheticLambda43(this, tLObject, i, dialogsActivity, baseFragment, tLRPC$User, str));
    }

    public /* synthetic */ void lambda$runLinkRequest$32(TLObject tLObject, int i, DialogsActivity dialogsActivity, BaseFragment baseFragment, TLRPC$User tLRPC$User, String str) {
        if (tLObject instanceof TLRPC$TL_boolTrue) {
            MediaDataController.getInstance(i).loadAttachMenuBots(false, true);
            if (dialogsActivity != null) {
                lambda$runLinkRequest$59(dialogsActivity);
            } else if (!(baseFragment instanceof ChatActivity)) {
            } else {
                ((ChatActivity) baseFragment).openAttachBotLayout(tLRPC$User.id, str);
            }
        }
    }

    public /* synthetic */ void lambda$runLinkRequest$37(String str, int i, TLRPC$TL_contacts_resolvedPeer tLRPC$TL_contacts_resolvedPeer, DialogsActivity dialogsActivity, ArrayList arrayList, CharSequence charSequence, boolean z) {
        long longValue = ((Long) arrayList.get(0)).longValue();
        TLRPC$TL_inputMediaGame tLRPC$TL_inputMediaGame = new TLRPC$TL_inputMediaGame();
        TLRPC$TL_inputGameShortName tLRPC$TL_inputGameShortName = new TLRPC$TL_inputGameShortName();
        tLRPC$TL_inputMediaGame.id = tLRPC$TL_inputGameShortName;
        tLRPC$TL_inputGameShortName.short_name = str;
        tLRPC$TL_inputGameShortName.bot_id = MessagesController.getInstance(i).getInputUser(tLRPC$TL_contacts_resolvedPeer.users.get(0));
        SendMessagesHelper.getInstance(i).sendGame(MessagesController.getInstance(i).getInputPeer(longValue), tLRPC$TL_inputMediaGame, 0L, 0L);
        Bundle bundle = new Bundle();
        bundle.putBoolean("scrollToTopOnResume", true);
        if (DialogObject.isEncryptedDialog(longValue)) {
            bundle.putInt("enc_id", DialogObject.getEncryptedChatId(longValue));
        } else if (DialogObject.isUserDialog(longValue)) {
            bundle.putLong("user_id", longValue);
        } else {
            bundle.putLong("chat_id", -longValue);
        }
        if (MessagesController.getInstance(i).checkCanOpenChat(bundle, dialogsActivity)) {
            NotificationCenter.getInstance(i).postNotificationName(NotificationCenter.closeChats, new Object[0]);
            this.actionBarLayout.presentFragment(new ChatActivity(bundle), true, false, true, false);
        }
    }

    public /* synthetic */ void lambda$runLinkRequest$42(int i, TLRPC$User tLRPC$User, String str, String str2, DialogsActivity dialogsActivity, DialogsActivity dialogsActivity2, ArrayList arrayList, CharSequence charSequence, boolean z) {
        TLRPC$TL_chatAdminRights tLRPC$TL_chatAdminRights;
        long longValue = ((Long) arrayList.get(0)).longValue();
        TLRPC$Chat chat = MessagesController.getInstance(this.currentAccount).getChat(Long.valueOf(-longValue));
        if (chat != null && (chat.creator || ((tLRPC$TL_chatAdminRights = chat.admin_rights) != null && tLRPC$TL_chatAdminRights.add_admins))) {
            MessagesController.getInstance(i).checkIsInChat(chat, tLRPC$User, new LaunchActivity$$ExternalSyntheticLambda66(this, str, str2, i, chat, dialogsActivity, tLRPC$User, longValue));
            return;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(LocaleController.getString("AddBot", 2131624250));
        builder.setMessage(AndroidUtilities.replaceTags(LocaleController.formatString("AddMembersAlertNamesText", 2131624271, UserObject.getUserName(tLRPC$User), chat == null ? "" : chat.title)));
        builder.setNegativeButton(LocaleController.getString("Cancel", 2131624819), null);
        builder.setPositiveButton(LocaleController.getString("AddBot", 2131624250), new LaunchActivity$$ExternalSyntheticLambda11(this, longValue, i, tLRPC$User, str2));
        builder.show();
    }

    public /* synthetic */ void lambda$runLinkRequest$40(String str, String str2, int i, TLRPC$Chat tLRPC$Chat, DialogsActivity dialogsActivity, TLRPC$User tLRPC$User, long j, boolean z, TLRPC$TL_chatAdminRights tLRPC$TL_chatAdminRights, String str3) {
        AndroidUtilities.runOnUIThread(new LaunchActivity$$ExternalSyntheticLambda38(this, str, tLRPC$TL_chatAdminRights, z, str2, i, tLRPC$Chat, dialogsActivity, tLRPC$User, j, str3));
    }

    public /* synthetic */ void lambda$runLinkRequest$39(String str, TLRPC$TL_chatAdminRights tLRPC$TL_chatAdminRights, boolean z, String str2, int i, TLRPC$Chat tLRPC$Chat, DialogsActivity dialogsActivity, TLRPC$User tLRPC$User, long j, String str3) {
        TLRPC$TL_chatAdminRights tLRPC$TL_chatAdminRights2;
        TLRPC$TL_chatAdminRights tLRPC$TL_chatAdminRights3;
        if (str != null) {
            String[] split = str.split("\\+| ");
            tLRPC$TL_chatAdminRights2 = new TLRPC$TL_chatAdminRights();
            for (String str4 : split) {
                str4.hashCode();
                char c = 65535;
                switch (str4.hashCode()) {
                    case -2110462504:
                        if (str4.equals("ban_users")) {
                            c = 0;
                            break;
                        }
                        break;
                    case -2095811475:
                        if (str4.equals("anonymous")) {
                            c = 1;
                            break;
                        }
                        break;
                    case -1654794275:
                        if (str4.equals("change_info")) {
                            c = 2;
                            break;
                        }
                        break;
                    case -1593320096:
                        if (str4.equals("delete_messages")) {
                            c = 3;
                            break;
                        }
                        break;
                    case -939200543:
                        if (str4.equals("edit_messages")) {
                            c = 4;
                            break;
                        }
                        break;
                    case 22162680:
                        if (str4.equals("manage_call")) {
                            c = 5;
                            break;
                        }
                        break;
                    case 22169074:
                        if (str4.equals("manage_chat")) {
                            c = 6;
                            break;
                        }
                        break;
                    case 106069776:
                        if (str4.equals("other")) {
                            c = 7;
                            break;
                        }
                        break;
                    case 449085338:
                        if (str4.equals("promote_members")) {
                            c = '\b';
                            break;
                        }
                        break;
                    case 632157522:
                        if (str4.equals("invite_users")) {
                            c = '\t';
                            break;
                        }
                        break;
                    case 758599179:
                        if (str4.equals("post_messages")) {
                            c = '\n';
                            break;
                        }
                        break;
                    case 1357805750:
                        if (str4.equals("pin_messages")) {
                            c = 11;
                            break;
                        }
                        break;
                    case 1529816162:
                        if (str4.equals("add_admins")) {
                            c = '\f';
                            break;
                        }
                        break;
                    case 1542893206:
                        if (str4.equals("restrict_members")) {
                            c = '\r';
                            break;
                        }
                        break;
                    case 1641337725:
                        if (str4.equals("manage_video_chats")) {
                            c = 14;
                            break;
                        }
                        break;
                }
                switch (c) {
                    case 0:
                    case '\r':
                        tLRPC$TL_chatAdminRights2.ban_users = true;
                        break;
                    case 1:
                        tLRPC$TL_chatAdminRights2.anonymous = true;
                        break;
                    case 2:
                        tLRPC$TL_chatAdminRights2.change_info = true;
                        break;
                    case 3:
                        tLRPC$TL_chatAdminRights2.delete_messages = true;
                        break;
                    case 4:
                        tLRPC$TL_chatAdminRights2.edit_messages = true;
                        break;
                    case 5:
                    case 14:
                        tLRPC$TL_chatAdminRights2.manage_call = true;
                        break;
                    case 6:
                    case 7:
                        tLRPC$TL_chatAdminRights2.other = true;
                        break;
                    case '\b':
                    case '\f':
                        tLRPC$TL_chatAdminRights2.add_admins = true;
                        break;
                    case '\t':
                        tLRPC$TL_chatAdminRights2.invite_users = true;
                        break;
                    case '\n':
                        tLRPC$TL_chatAdminRights2.post_messages = true;
                        break;
                    case 11:
                        tLRPC$TL_chatAdminRights2.pin_messages = true;
                        break;
                }
            }
        } else {
            tLRPC$TL_chatAdminRights2 = null;
        }
        if (tLRPC$TL_chatAdminRights2 == null && tLRPC$TL_chatAdminRights == null) {
            tLRPC$TL_chatAdminRights3 = null;
        } else {
            if (tLRPC$TL_chatAdminRights2 != null) {
                if (tLRPC$TL_chatAdminRights == null) {
                    tLRPC$TL_chatAdminRights3 = tLRPC$TL_chatAdminRights2;
                } else {
                    tLRPC$TL_chatAdminRights.change_info = tLRPC$TL_chatAdminRights2.change_info || tLRPC$TL_chatAdminRights.change_info;
                    tLRPC$TL_chatAdminRights.post_messages = tLRPC$TL_chatAdminRights2.post_messages || tLRPC$TL_chatAdminRights.post_messages;
                    tLRPC$TL_chatAdminRights.edit_messages = tLRPC$TL_chatAdminRights2.edit_messages || tLRPC$TL_chatAdminRights.edit_messages;
                    tLRPC$TL_chatAdminRights.add_admins = tLRPC$TL_chatAdminRights2.add_admins || tLRPC$TL_chatAdminRights.add_admins;
                    tLRPC$TL_chatAdminRights.delete_messages = tLRPC$TL_chatAdminRights2.delete_messages || tLRPC$TL_chatAdminRights.delete_messages;
                    tLRPC$TL_chatAdminRights.ban_users = tLRPC$TL_chatAdminRights2.ban_users || tLRPC$TL_chatAdminRights.ban_users;
                    tLRPC$TL_chatAdminRights.invite_users = tLRPC$TL_chatAdminRights2.invite_users || tLRPC$TL_chatAdminRights.invite_users;
                    tLRPC$TL_chatAdminRights.pin_messages = tLRPC$TL_chatAdminRights2.pin_messages || tLRPC$TL_chatAdminRights.pin_messages;
                    tLRPC$TL_chatAdminRights.manage_call = tLRPC$TL_chatAdminRights2.manage_call || tLRPC$TL_chatAdminRights.manage_call;
                    tLRPC$TL_chatAdminRights.anonymous = tLRPC$TL_chatAdminRights2.anonymous || tLRPC$TL_chatAdminRights.anonymous;
                    tLRPC$TL_chatAdminRights.other = tLRPC$TL_chatAdminRights2.other || tLRPC$TL_chatAdminRights.other;
                }
            }
            tLRPC$TL_chatAdminRights3 = tLRPC$TL_chatAdminRights;
        }
        if (z && tLRPC$TL_chatAdminRights2 == null && !TextUtils.isEmpty(str2)) {
            MessagesController.getInstance(this.currentAccount).addUserToChat(tLRPC$Chat.id, tLRPC$User, 0, str2, dialogsActivity, true, new LaunchActivity$$ExternalSyntheticLambda35(this, i, tLRPC$Chat, dialogsActivity), null);
            return;
        }
        ChatRightsEditActivity chatRightsEditActivity = new ChatRightsEditActivity(tLRPC$User.id, -j, tLRPC$TL_chatAdminRights3, null, null, str3, 2, true, !z, str2);
        chatRightsEditActivity.setDelegate(new AnonymousClass13(this, dialogsActivity, i));
        this.actionBarLayout.presentFragment(chatRightsEditActivity, false);
    }

    public /* synthetic */ void lambda$runLinkRequest$38(int i, TLRPC$Chat tLRPC$Chat, DialogsActivity dialogsActivity) {
        NotificationCenter.getInstance(i).postNotificationName(NotificationCenter.closeChats, new Object[0]);
        Bundle bundle = new Bundle();
        bundle.putBoolean("scrollToTopOnResume", true);
        bundle.putLong("chat_id", tLRPC$Chat.id);
        if (!MessagesController.getInstance(this.currentAccount).checkCanOpenChat(bundle, dialogsActivity)) {
            return;
        }
        presentFragment(new ChatActivity(bundle), true, false);
    }

    /* renamed from: org.telegram.ui.LaunchActivity$13 */
    /* loaded from: classes3.dex */
    public class AnonymousClass13 implements ChatRightsEditActivity.ChatRightsEditActivityDelegate {
        final /* synthetic */ DialogsActivity val$fragment;
        final /* synthetic */ int val$intentAccount;

        @Override // org.telegram.ui.ChatRightsEditActivity.ChatRightsEditActivityDelegate
        public void didChangeOwner(TLRPC$User tLRPC$User) {
        }

        AnonymousClass13(LaunchActivity launchActivity, DialogsActivity dialogsActivity, int i) {
            this.val$fragment = dialogsActivity;
            this.val$intentAccount = i;
        }

        @Override // org.telegram.ui.ChatRightsEditActivity.ChatRightsEditActivityDelegate
        public void didSetRights(int i, TLRPC$TL_chatAdminRights tLRPC$TL_chatAdminRights, TLRPC$TL_chatBannedRights tLRPC$TL_chatBannedRights, String str) {
            this.val$fragment.removeSelfFromStack();
            NotificationCenter.getInstance(this.val$intentAccount).postNotificationName(NotificationCenter.closeChats, new Object[0]);
        }
    }

    public /* synthetic */ void lambda$runLinkRequest$41(long j, int i, TLRPC$User tLRPC$User, String str, DialogInterface dialogInterface, int i2) {
        Bundle bundle = new Bundle();
        bundle.putBoolean("scrollToTopOnResume", true);
        long j2 = -j;
        bundle.putLong("chat_id", j2);
        ChatActivity chatActivity = new ChatActivity(bundle);
        NotificationCenter.getInstance(i).postNotificationName(NotificationCenter.closeChats, new Object[0]);
        MessagesController.getInstance(i).addUserToChat(j2, tLRPC$User, 0, str, chatActivity, null);
        this.actionBarLayout.presentFragment(chatActivity, true, false, true, false);
    }

    /* renamed from: org.telegram.ui.LaunchActivity$14 */
    /* loaded from: classes3.dex */
    public class AnonymousClass14 implements MessagesController.MessagesLoadedCallback {
        final /* synthetic */ Bundle val$args;
        final /* synthetic */ long val$dialog_id;
        final /* synthetic */ BaseFragment val$lastFragment;
        final /* synthetic */ String val$livestream;
        final /* synthetic */ AlertDialog val$progressDialog;

        AnonymousClass14(AlertDialog alertDialog, String str, BaseFragment baseFragment, long j, Bundle bundle) {
            LaunchActivity.this = r1;
            this.val$progressDialog = alertDialog;
            this.val$livestream = str;
            this.val$lastFragment = baseFragment;
            this.val$dialog_id = j;
            this.val$args = bundle;
        }

        @Override // org.telegram.messenger.MessagesController.MessagesLoadedCallback
        public void onMessagesLoaded(boolean z) {
            BaseFragment baseFragment;
            try {
                this.val$progressDialog.dismiss();
            } catch (Exception e) {
                FileLog.e(e);
            }
            if (!LaunchActivity.this.isFinishing()) {
                if (this.val$livestream != null) {
                    BaseFragment baseFragment2 = this.val$lastFragment;
                    if ((baseFragment2 instanceof ChatActivity) && ((ChatActivity) baseFragment2).getDialogId() == this.val$dialog_id) {
                        baseFragment = this.val$lastFragment;
                        AndroidUtilities.runOnUIThread(new LaunchActivity$14$$ExternalSyntheticLambda0(this, this.val$livestream, this.val$dialog_id, baseFragment), 150L);
                    }
                }
                baseFragment = new ChatActivity(this.val$args);
                LaunchActivity.this.actionBarLayout.presentFragment(baseFragment);
                AndroidUtilities.runOnUIThread(new LaunchActivity$14$$ExternalSyntheticLambda0(this, this.val$livestream, this.val$dialog_id, baseFragment), 150L);
            }
        }

        public /* synthetic */ void lambda$onMessagesLoaded$2(String str, long j, BaseFragment baseFragment) {
            if (str != null) {
                AccountInstance accountInstance = AccountInstance.getInstance(LaunchActivity.this.currentAccount);
                long j2 = -j;
                boolean z = false;
                ChatObject.Call groupCall = accountInstance.getMessagesController().getGroupCall(j2, false);
                if (groupCall != null) {
                    TLRPC$Chat chat = accountInstance.getMessagesController().getChat(Long.valueOf(j2));
                    TLRPC$InputPeer inputPeer = accountInstance.getMessagesController().getInputPeer(j);
                    if (!groupCall.call.rtmp_stream) {
                        z = true;
                    }
                    VoIPHelper.startCall(chat, inputPeer, null, false, Boolean.valueOf(z), LaunchActivity.this, baseFragment, accountInstance);
                    return;
                }
                TLRPC$ChatFull chatFull = accountInstance.getMessagesController().getChatFull(j2);
                if (chatFull == null) {
                    return;
                }
                if (chatFull.call == null) {
                    if (baseFragment.getParentActivity() == null) {
                        return;
                    }
                    BulletinFactory.of(baseFragment).createSimpleBulletin(2131558480, LocaleController.getString("InviteExpired", 2131626260)).show();
                    return;
                }
                accountInstance.getMessagesController().getGroupCall(j2, true, new LaunchActivity$14$$ExternalSyntheticLambda1(this, accountInstance, j, baseFragment));
            }
        }

        public /* synthetic */ void lambda$onMessagesLoaded$1(AccountInstance accountInstance, long j, BaseFragment baseFragment) {
            AndroidUtilities.runOnUIThread(new LaunchActivity$14$$ExternalSyntheticLambda2(this, accountInstance, j, baseFragment));
        }

        public /* synthetic */ void lambda$onMessagesLoaded$0(AccountInstance accountInstance, long j, BaseFragment baseFragment) {
            long j2 = -j;
            boolean z = false;
            ChatObject.Call groupCall = accountInstance.getMessagesController().getGroupCall(j2, false);
            TLRPC$Chat chat = accountInstance.getMessagesController().getChat(Long.valueOf(j2));
            TLRPC$InputPeer inputPeer = accountInstance.getMessagesController().getInputPeer(j);
            if (groupCall == null || !groupCall.call.rtmp_stream) {
                z = true;
            }
            VoIPHelper.startCall(chat, inputPeer, null, false, Boolean.valueOf(z), LaunchActivity.this, baseFragment, accountInstance);
        }

        @Override // org.telegram.messenger.MessagesController.MessagesLoadedCallback
        public void onError() {
            if (!LaunchActivity.this.isFinishing()) {
                AlertsCreator.showSimpleAlert((BaseFragment) LaunchActivity.mainFragmentsStack.get(LaunchActivity.mainFragmentsStack.size() - 1), LocaleController.getString("JoinToGroupErrorNotExist", 2131626325));
            }
            try {
                this.val$progressDialog.dismiss();
            } catch (Exception e) {
                FileLog.e(e);
            }
        }
    }

    public /* synthetic */ void lambda$runLinkRequest$47(int i, AlertDialog alertDialog, String str, TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        AndroidUtilities.runOnUIThread(new LaunchActivity$$ExternalSyntheticLambda50(this, tLRPC$TL_error, tLObject, i, alertDialog, str));
    }

    /* JADX WARN: Code restructure failed: missing block: B:19:0x0031, code lost:
        if (r10.chat.has_geo != false) goto L20;
     */
    /* JADX WARN: Code restructure failed: missing block: B:23:0x0079, code lost:
        if (r11.checkCanOpenChat(r7, r14.get(r14.size() - 1)) != false) goto L24;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public /* synthetic */ void lambda$runLinkRequest$46(TLRPC$TL_error tLRPC$TL_error, TLObject tLObject, int i, AlertDialog alertDialog, String str) {
        if (!isFinishing()) {
            ChatActivity.ThemeDelegate themeDelegate = null;
            boolean z = true;
            if (tLRPC$TL_error == null && this.actionBarLayout != null) {
                TLRPC$ChatInvite tLRPC$ChatInvite = (TLRPC$ChatInvite) tLObject;
                TLRPC$Chat tLRPC$Chat = tLRPC$ChatInvite.chat;
                if (tLRPC$Chat != null) {
                    if (ChatObject.isLeftFromChat(tLRPC$Chat)) {
                        TLRPC$Chat tLRPC$Chat2 = tLRPC$ChatInvite.chat;
                        if (!tLRPC$Chat2.kicked) {
                            if (TextUtils.isEmpty(tLRPC$Chat2.username)) {
                                if (!(tLRPC$ChatInvite instanceof TLRPC$TL_chatInvitePeek)) {
                                }
                            }
                        }
                    }
                    MessagesController.getInstance(i).putChat(tLRPC$ChatInvite.chat, false);
                    ArrayList<TLRPC$Chat> arrayList = new ArrayList<>();
                    arrayList.add(tLRPC$ChatInvite.chat);
                    MessagesStorage.getInstance(i).putUsersAndChats(null, arrayList, false, true);
                    Bundle bundle = new Bundle();
                    bundle.putLong("chat_id", tLRPC$ChatInvite.chat.id);
                    if (!mainFragmentsStack.isEmpty()) {
                        MessagesController messagesController = MessagesController.getInstance(i);
                        ArrayList<BaseFragment> arrayList2 = mainFragmentsStack;
                    }
                    boolean[] zArr = new boolean[1];
                    alertDialog.setOnCancelListener(new LaunchActivity$$ExternalSyntheticLambda3(zArr));
                    MessagesController.getInstance(i).ensureMessagesLoaded(-tLRPC$ChatInvite.chat.id, 0, new AnonymousClass15(alertDialog, zArr, bundle, tLRPC$ChatInvite));
                    z = false;
                }
                ArrayList<BaseFragment> arrayList3 = mainFragmentsStack;
                BaseFragment baseFragment = arrayList3.get(arrayList3.size() - 1);
                if (baseFragment instanceof ChatActivity) {
                    themeDelegate = ((ChatActivity) baseFragment).themeDelegate;
                }
                baseFragment.showDialog(new JoinGroupAlert(this, tLRPC$ChatInvite, str, baseFragment, themeDelegate));
            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle(LocaleController.getString("AppName", 2131624375));
                if (tLRPC$TL_error.text.startsWith("FLOOD_WAIT")) {
                    builder.setMessage(LocaleController.getString("FloodWait", 2131625908));
                } else if (tLRPC$TL_error.text.startsWith("INVITE_HASH_EXPIRED")) {
                    builder.setTitle(LocaleController.getString("ExpiredLink", 2131625789));
                    builder.setMessage(LocaleController.getString("InviteExpired", 2131626260));
                } else {
                    builder.setMessage(LocaleController.getString("JoinToGroupErrorNotExist", 2131626325));
                }
                builder.setPositiveButton(LocaleController.getString("OK", 2131627075), null);
                showAlertDialog(builder);
            }
            if (!z) {
                return;
            }
            try {
                alertDialog.dismiss();
            } catch (Exception e) {
                FileLog.e(e);
            }
        }
    }

    public static /* synthetic */ void lambda$runLinkRequest$45(boolean[] zArr, DialogInterface dialogInterface) {
        zArr[0] = true;
    }

    /* renamed from: org.telegram.ui.LaunchActivity$15 */
    /* loaded from: classes3.dex */
    public class AnonymousClass15 implements MessagesController.MessagesLoadedCallback {
        final /* synthetic */ Bundle val$args;
        final /* synthetic */ boolean[] val$canceled;
        final /* synthetic */ TLRPC$ChatInvite val$invite;
        final /* synthetic */ AlertDialog val$progressDialog;

        AnonymousClass15(AlertDialog alertDialog, boolean[] zArr, Bundle bundle, TLRPC$ChatInvite tLRPC$ChatInvite) {
            LaunchActivity.this = r1;
            this.val$progressDialog = alertDialog;
            this.val$canceled = zArr;
            this.val$args = bundle;
            this.val$invite = tLRPC$ChatInvite;
        }

        @Override // org.telegram.messenger.MessagesController.MessagesLoadedCallback
        public void onMessagesLoaded(boolean z) {
            try {
                this.val$progressDialog.dismiss();
            } catch (Exception e) {
                FileLog.e(e);
            }
            if (this.val$canceled[0]) {
                return;
            }
            ChatActivity chatActivity = new ChatActivity(this.val$args);
            TLRPC$ChatInvite tLRPC$ChatInvite = this.val$invite;
            if (tLRPC$ChatInvite instanceof TLRPC$TL_chatInvitePeek) {
                chatActivity.setChatInvite(tLRPC$ChatInvite);
            }
            LaunchActivity.this.actionBarLayout.presentFragment(chatActivity);
        }

        @Override // org.telegram.messenger.MessagesController.MessagesLoadedCallback
        public void onError() {
            if (!LaunchActivity.this.isFinishing()) {
                AlertsCreator.showSimpleAlert((BaseFragment) LaunchActivity.mainFragmentsStack.get(LaunchActivity.mainFragmentsStack.size() - 1), LocaleController.getString("JoinToGroupErrorNotExist", 2131626325));
            }
            try {
                this.val$progressDialog.dismiss();
            } catch (Exception e) {
                FileLog.e(e);
            }
        }
    }

    public /* synthetic */ void lambda$runLinkRequest$49(int i, AlertDialog alertDialog, TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        if (tLRPC$TL_error == null) {
            MessagesController.getInstance(i).processUpdates((TLRPC$Updates) tLObject, false);
        }
        AndroidUtilities.runOnUIThread(new LaunchActivity$$ExternalSyntheticLambda59(this, alertDialog, tLRPC$TL_error, tLObject, i));
    }

    public /* synthetic */ void lambda$runLinkRequest$48(AlertDialog alertDialog, TLRPC$TL_error tLRPC$TL_error, TLObject tLObject, int i) {
        if (!isFinishing()) {
            try {
                alertDialog.dismiss();
            } catch (Exception e) {
                FileLog.e(e);
            }
            if (tLRPC$TL_error == null) {
                if (this.actionBarLayout == null) {
                    return;
                }
                TLRPC$Updates tLRPC$Updates = (TLRPC$Updates) tLObject;
                if (tLRPC$Updates.chats.isEmpty()) {
                    return;
                }
                TLRPC$Chat tLRPC$Chat = tLRPC$Updates.chats.get(0);
                tLRPC$Chat.left = false;
                tLRPC$Chat.kicked = false;
                MessagesController.getInstance(i).putUsers(tLRPC$Updates.users, false);
                MessagesController.getInstance(i).putChats(tLRPC$Updates.chats, false);
                Bundle bundle = new Bundle();
                bundle.putLong("chat_id", tLRPC$Chat.id);
                if (!mainFragmentsStack.isEmpty()) {
                    MessagesController messagesController = MessagesController.getInstance(i);
                    ArrayList<BaseFragment> arrayList = mainFragmentsStack;
                    if (!messagesController.checkCanOpenChat(bundle, arrayList.get(arrayList.size() - 1))) {
                        return;
                    }
                }
                ChatActivity chatActivity = new ChatActivity(bundle);
                NotificationCenter.getInstance(i).postNotificationName(NotificationCenter.closeChats, new Object[0]);
                this.actionBarLayout.presentFragment(chatActivity, false, true, true, false);
                return;
            }
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(LocaleController.getString("AppName", 2131624375));
            if (tLRPC$TL_error.text.startsWith("FLOOD_WAIT")) {
                builder.setMessage(LocaleController.getString("FloodWait", 2131625908));
            } else if (tLRPC$TL_error.text.equals("USERS_TOO_MUCH")) {
                builder.setMessage(LocaleController.getString("JoinToGroupErrorFull", 2131626324));
            } else {
                builder.setMessage(LocaleController.getString("JoinToGroupErrorNotExist", 2131626325));
            }
            builder.setPositiveButton(LocaleController.getString("OK", 2131627075), null);
            showAlertDialog(builder);
        }
    }

    public /* synthetic */ void lambda$runLinkRequest$50(boolean z, int i, String str, DialogsActivity dialogsActivity, ArrayList arrayList, CharSequence charSequence, boolean z2) {
        long longValue = ((Long) arrayList.get(0)).longValue();
        Bundle bundle = new Bundle();
        bundle.putBoolean("scrollToTopOnResume", true);
        bundle.putBoolean("hasUrl", z);
        if (DialogObject.isEncryptedDialog(longValue)) {
            bundle.putInt("enc_id", DialogObject.getEncryptedChatId(longValue));
        } else if (DialogObject.isUserDialog(longValue)) {
            bundle.putLong("user_id", longValue);
        } else {
            bundle.putLong("chat_id", -longValue);
        }
        if (MessagesController.getInstance(i).checkCanOpenChat(bundle, dialogsActivity)) {
            NotificationCenter.getInstance(i).postNotificationName(NotificationCenter.closeChats, new Object[0]);
            MediaDataController.getInstance(i).saveDraft(longValue, 0, str, null, null, false);
            this.actionBarLayout.presentFragment(new ChatActivity(bundle), true, false, true, false);
        }
    }

    public /* synthetic */ void lambda$runLinkRequest$54(int[] iArr, int i, AlertDialog alertDialog, TLRPC$TL_account_getAuthorizationForm tLRPC$TL_account_getAuthorizationForm, String str, String str2, String str3, TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        TLRPC$TL_account_authorizationForm tLRPC$TL_account_authorizationForm = (TLRPC$TL_account_authorizationForm) tLObject;
        if (tLRPC$TL_account_authorizationForm != null) {
            iArr[0] = ConnectionsManager.getInstance(i).sendRequest(new TLRPC$TL_account_getPassword(), new LaunchActivity$$ExternalSyntheticLambda81(this, alertDialog, i, tLRPC$TL_account_authorizationForm, tLRPC$TL_account_getAuthorizationForm, str, str2, str3));
            return;
        }
        AndroidUtilities.runOnUIThread(new LaunchActivity$$ExternalSyntheticLambda57(this, alertDialog, tLRPC$TL_error));
    }

    public /* synthetic */ void lambda$runLinkRequest$52(AlertDialog alertDialog, int i, TLRPC$TL_account_authorizationForm tLRPC$TL_account_authorizationForm, TLRPC$TL_account_getAuthorizationForm tLRPC$TL_account_getAuthorizationForm, String str, String str2, String str3, TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        AndroidUtilities.runOnUIThread(new LaunchActivity$$ExternalSyntheticLambda53(this, alertDialog, tLObject, i, tLRPC$TL_account_authorizationForm, tLRPC$TL_account_getAuthorizationForm, str, str2, str3));
    }

    public /* synthetic */ void lambda$runLinkRequest$51(AlertDialog alertDialog, TLObject tLObject, int i, TLRPC$TL_account_authorizationForm tLRPC$TL_account_authorizationForm, TLRPC$TL_account_getAuthorizationForm tLRPC$TL_account_getAuthorizationForm, String str, String str2, String str3) {
        try {
            alertDialog.dismiss();
        } catch (Exception e) {
            FileLog.e(e);
        }
        if (tLObject != null) {
            MessagesController.getInstance(i).putUsers(tLRPC$TL_account_authorizationForm.users, false);
            lambda$runLinkRequest$59(new PassportActivity(5, tLRPC$TL_account_getAuthorizationForm.bot_id, tLRPC$TL_account_getAuthorizationForm.scope, tLRPC$TL_account_getAuthorizationForm.public_key, str, str2, str3, tLRPC$TL_account_authorizationForm, (TLRPC$TL_account_password) tLObject));
        }
    }

    public /* synthetic */ void lambda$runLinkRequest$53(AlertDialog alertDialog, TLRPC$TL_error tLRPC$TL_error) {
        try {
            alertDialog.dismiss();
            if ("APP_VERSION_OUTDATED".equals(tLRPC$TL_error.text)) {
                AlertsCreator.showUpdateAppAlert(this, LocaleController.getString("UpdateAppAlert", 2131628758), true);
            } else {
                showAlertDialog(AlertsCreator.createSimpleAlert(this, LocaleController.getString("ErrorOccurred", 2131625657) + "\n" + tLRPC$TL_error.text));
            }
        } catch (Exception e) {
            FileLog.e(e);
        }
    }

    public /* synthetic */ void lambda$runLinkRequest$56(AlertDialog alertDialog, TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        AndroidUtilities.runOnUIThread(new LaunchActivity$$ExternalSyntheticLambda52(this, alertDialog, tLObject));
    }

    public /* synthetic */ void lambda$runLinkRequest$55(AlertDialog alertDialog, TLObject tLObject) {
        try {
            alertDialog.dismiss();
        } catch (Exception e) {
            FileLog.e(e);
        }
        if (tLObject instanceof TLRPC$TL_help_deepLinkInfo) {
            TLRPC$TL_help_deepLinkInfo tLRPC$TL_help_deepLinkInfo = (TLRPC$TL_help_deepLinkInfo) tLObject;
            AlertsCreator.showUpdateAppAlert(this, tLRPC$TL_help_deepLinkInfo.message, tLRPC$TL_help_deepLinkInfo.update_app);
        }
    }

    public /* synthetic */ void lambda$runLinkRequest$58(AlertDialog alertDialog, TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        AndroidUtilities.runOnUIThread(new LaunchActivity$$ExternalSyntheticLambda54(this, alertDialog, tLObject, tLRPC$TL_error));
    }

    public /* synthetic */ void lambda$runLinkRequest$57(AlertDialog alertDialog, TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        try {
            alertDialog.dismiss();
        } catch (Exception e) {
            FileLog.e(e);
        }
        if (tLObject instanceof TLRPC$TL_langPackLanguage) {
            showAlertDialog(AlertsCreator.createLanguageAlert(this, (TLRPC$TL_langPackLanguage) tLObject));
        } else if (tLRPC$TL_error == null) {
        } else {
            if ("LANG_CODE_NOT_SUPPORTED".equals(tLRPC$TL_error.text)) {
                showAlertDialog(AlertsCreator.createSimpleAlert(this, LocaleController.getString("LanguageUnsupportedError", 2131626358)));
                return;
            }
            showAlertDialog(AlertsCreator.createSimpleAlert(this, LocaleController.getString("ErrorOccurred", 2131625657) + "\n" + tLRPC$TL_error.text));
        }
    }

    public /* synthetic */ void lambda$runLinkRequest$61(AlertDialog alertDialog, TLRPC$TL_wallPaper tLRPC$TL_wallPaper, TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        AndroidUtilities.runOnUIThread(new LaunchActivity$$ExternalSyntheticLambda55(this, alertDialog, tLObject, tLRPC$TL_wallPaper, tLRPC$TL_error));
    }

    /* JADX WARN: Multi-variable type inference failed */
    public /* synthetic */ void lambda$runLinkRequest$60(AlertDialog alertDialog, TLObject tLObject, TLRPC$TL_wallPaper tLRPC$TL_wallPaper, TLRPC$TL_error tLRPC$TL_error) {
        try {
            alertDialog.dismiss();
        } catch (Exception e) {
            FileLog.e(e);
        }
        if (tLObject instanceof TLRPC$TL_wallPaper) {
            TLRPC$TL_wallPaper tLRPC$TL_wallPaper2 = (TLRPC$TL_wallPaper) tLObject;
            if (tLRPC$TL_wallPaper2.pattern) {
                String str = tLRPC$TL_wallPaper2.slug;
                TLRPC$WallPaperSettings tLRPC$WallPaperSettings = tLRPC$TL_wallPaper.settings;
                int i = tLRPC$WallPaperSettings.background_color;
                int i2 = tLRPC$WallPaperSettings.second_background_color;
                int i3 = tLRPC$WallPaperSettings.third_background_color;
                int i4 = tLRPC$WallPaperSettings.fourth_background_color;
                int wallpaperRotation = AndroidUtilities.getWallpaperRotation(tLRPC$WallPaperSettings.rotation, false);
                TLRPC$WallPaperSettings tLRPC$WallPaperSettings2 = tLRPC$TL_wallPaper.settings;
                WallpapersListActivity.ColorWallpaper colorWallpaper = new WallpapersListActivity.ColorWallpaper(str, i, i2, i3, i4, wallpaperRotation, tLRPC$WallPaperSettings2.intensity / 100.0f, tLRPC$WallPaperSettings2.motion, null);
                colorWallpaper.pattern = tLRPC$TL_wallPaper2;
                tLRPC$TL_wallPaper2 = colorWallpaper;
            }
            ThemePreviewActivity themePreviewActivity = new ThemePreviewActivity(tLRPC$TL_wallPaper2, null, true, false);
            TLRPC$WallPaperSettings tLRPC$WallPaperSettings3 = tLRPC$TL_wallPaper.settings;
            themePreviewActivity.setInitialModes(tLRPC$WallPaperSettings3.blur, tLRPC$WallPaperSettings3.motion);
            lambda$runLinkRequest$59(themePreviewActivity);
            return;
        }
        showAlertDialog(AlertsCreator.createSimpleAlert(this, LocaleController.getString("ErrorOccurred", 2131625657) + "\n" + tLRPC$TL_error.text));
    }

    public /* synthetic */ void lambda$runLinkRequest$62() {
        this.loadingThemeFileName = null;
        this.loadingThemeWallpaperName = null;
        this.loadingThemeWallpaper = null;
        this.loadingThemeInfo = null;
        this.loadingThemeProgressDialog = null;
        this.loadingTheme = null;
    }

    public /* synthetic */ void lambda$runLinkRequest$64(AlertDialog alertDialog, TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        AndroidUtilities.runOnUIThread(new LaunchActivity$$ExternalSyntheticLambda46(this, tLObject, alertDialog, tLRPC$TL_error));
    }

    /* JADX WARN: Removed duplicated region for block: B:38:0x009b A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:42:? A[RETURN, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public /* synthetic */ void lambda$runLinkRequest$63(TLObject tLObject, AlertDialog alertDialog, TLRPC$TL_error tLRPC$TL_error) {
        char c;
        if (tLObject instanceof TLRPC$TL_theme) {
            TLRPC$TL_theme tLRPC$TL_theme = (TLRPC$TL_theme) tLObject;
            TLRPC$TL_wallPaper tLRPC$TL_wallPaper = null;
            c = 0;
            TLRPC$ThemeSettings tLRPC$ThemeSettings = tLRPC$TL_theme.settings.size() > 0 ? tLRPC$TL_theme.settings.get(0) : null;
            if (tLRPC$ThemeSettings != null) {
                Theme.ThemeInfo theme = Theme.getTheme(Theme.getBaseThemeKey(tLRPC$ThemeSettings));
                if (theme != null) {
                    TLRPC$WallPaper tLRPC$WallPaper = tLRPC$ThemeSettings.wallpaper;
                    if (tLRPC$WallPaper instanceof TLRPC$TL_wallPaper) {
                        tLRPC$TL_wallPaper = (TLRPC$TL_wallPaper) tLRPC$WallPaper;
                        if (!FileLoader.getInstance(this.currentAccount).getPathToAttach(tLRPC$TL_wallPaper.document, true).exists()) {
                            this.loadingThemeProgressDialog = alertDialog;
                            this.loadingThemeAccent = true;
                            this.loadingThemeInfo = theme;
                            this.loadingTheme = tLRPC$TL_theme;
                            this.loadingThemeWallpaper = tLRPC$TL_wallPaper;
                            this.loadingThemeWallpaperName = FileLoader.getAttachFileName(tLRPC$TL_wallPaper.document);
                            FileLoader.getInstance(this.currentAccount).loadFile(tLRPC$TL_wallPaper.document, tLRPC$TL_wallPaper, 1, 1);
                            return;
                        }
                    }
                    try {
                        alertDialog.dismiss();
                    } catch (Exception e) {
                        FileLog.e(e);
                    }
                    openThemeAccentPreview(tLRPC$TL_theme, tLRPC$TL_wallPaper, theme);
                    if (c != 0) {
                        return;
                    }
                    try {
                        alertDialog.dismiss();
                    } catch (Exception e2) {
                        FileLog.e(e2);
                    }
                    if (c == 1) {
                        showAlertDialog(AlertsCreator.createSimpleAlert(this, LocaleController.getString("Theme", 2131628598), LocaleController.getString("ThemeNotSupported", 2131628620)));
                        return;
                    } else {
                        showAlertDialog(AlertsCreator.createSimpleAlert(this, LocaleController.getString("Theme", 2131628598), LocaleController.getString("ThemeNotFound", 2131628619)));
                        return;
                    }
                }
                c = 1;
                if (c != 0) {
                }
            } else {
                TLRPC$Document tLRPC$Document = tLRPC$TL_theme.document;
                if (tLRPC$Document != null) {
                    this.loadingThemeAccent = false;
                    this.loadingTheme = tLRPC$TL_theme;
                    this.loadingThemeFileName = FileLoader.getAttachFileName(tLRPC$Document);
                    this.loadingThemeProgressDialog = alertDialog;
                    FileLoader.getInstance(this.currentAccount).loadFile(this.loadingTheme.document, tLRPC$TL_theme, 1, 1);
                    if (c != 0) {
                    }
                }
                c = 1;
                if (c != 0) {
                }
            }
        } else {
            if (tLRPC$TL_error == null || !"THEME_FORMAT_INVALID".equals(tLRPC$TL_error.text)) {
                c = 2;
                if (c != 0) {
                }
            }
            c = 1;
            if (c != 0) {
            }
        }
    }

    public /* synthetic */ void lambda$runLinkRequest$66(int[] iArr, int i, AlertDialog alertDialog, Integer num, Integer num2, Integer num3, TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        AndroidUtilities.runOnUIThread(new LaunchActivity$$ExternalSyntheticLambda48(this, tLObject, iArr, i, alertDialog, num, num2, num3));
    }

    /* JADX WARN: Removed duplicated region for block: B:14:0x0037 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:16:? A[RETURN, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public /* synthetic */ void lambda$runLinkRequest$65(TLObject tLObject, int[] iArr, int i, AlertDialog alertDialog, Integer num, Integer num2, Integer num3) {
        boolean z = false;
        if (tLObject instanceof TLRPC$TL_messages_chats) {
            TLRPC$TL_messages_chats tLRPC$TL_messages_chats = (TLRPC$TL_messages_chats) tLObject;
            if (!tLRPC$TL_messages_chats.chats.isEmpty()) {
                MessagesController.getInstance(this.currentAccount).putChats(tLRPC$TL_messages_chats.chats, false);
                iArr[0] = runCommentRequest(i, alertDialog, num, num2, num3, tLRPC$TL_messages_chats.chats.get(0));
                if (z) {
                    return;
                }
                try {
                    alertDialog.dismiss();
                } catch (Exception e) {
                    FileLog.e(e);
                }
                showAlertDialog(AlertsCreator.createSimpleAlert(this, LocaleController.getString("LinkNotFound", 2131626456)));
                return;
            }
        }
        z = true;
        if (z) {
        }
    }

    public /* synthetic */ void lambda$runLinkRequest$69(Bundle bundle, Long l, int[] iArr, AlertDialog alertDialog, BaseFragment baseFragment, int i) {
        if (!this.actionBarLayout.presentFragment(new ChatActivity(bundle))) {
            TLRPC$TL_channels_getChannels tLRPC$TL_channels_getChannels = new TLRPC$TL_channels_getChannels();
            TLRPC$TL_inputChannel tLRPC$TL_inputChannel = new TLRPC$TL_inputChannel();
            tLRPC$TL_inputChannel.channel_id = l.longValue();
            tLRPC$TL_channels_getChannels.id.add(tLRPC$TL_inputChannel);
            iArr[0] = ConnectionsManager.getInstance(this.currentAccount).sendRequest(tLRPC$TL_channels_getChannels, new LaunchActivity$$ExternalSyntheticLambda84(this, alertDialog, baseFragment, i, bundle));
        }
    }

    public /* synthetic */ void lambda$runLinkRequest$68(AlertDialog alertDialog, BaseFragment baseFragment, int i, Bundle bundle, TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        AndroidUtilities.runOnUIThread(new LaunchActivity$$ExternalSyntheticLambda56(this, alertDialog, tLObject, baseFragment, i, bundle));
    }

    public /* synthetic */ void lambda$runLinkRequest$67(AlertDialog alertDialog, TLObject tLObject, BaseFragment baseFragment, int i, Bundle bundle) {
        try {
            alertDialog.dismiss();
        } catch (Exception e) {
            FileLog.e(e);
        }
        boolean z = true;
        if (tLObject instanceof TLRPC$TL_messages_chats) {
            TLRPC$TL_messages_chats tLRPC$TL_messages_chats = (TLRPC$TL_messages_chats) tLObject;
            if (!tLRPC$TL_messages_chats.chats.isEmpty()) {
                MessagesController.getInstance(this.currentAccount).putChats(tLRPC$TL_messages_chats.chats, false);
                tLRPC$TL_messages_chats.chats.get(0);
                if (baseFragment == null || MessagesController.getInstance(i).checkCanOpenChat(bundle, baseFragment)) {
                    this.actionBarLayout.presentFragment(new ChatActivity(bundle));
                }
                z = false;
            }
        }
        if (z) {
            showAlertDialog(AlertsCreator.createSimpleAlert(this, LocaleController.getString("LinkNotFound", 2131626456)));
        }
    }

    public static /* synthetic */ void lambda$runLinkRequest$70(int i, int[] iArr, Runnable runnable, DialogInterface dialogInterface) {
        ConnectionsManager.getInstance(i).cancelRequest(iArr[0], true);
        if (runnable != null) {
            runnable.run();
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:21:0x0075  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private List<TLRPC$TL_contact> findContacts(String str, String str2, boolean z) {
        String str3;
        String lowerCase;
        TLRPC$User user;
        String str4;
        MessagesController messagesController = MessagesController.getInstance(this.currentAccount);
        ContactsController contactsController = ContactsController.getInstance(this.currentAccount);
        ArrayList arrayList = new ArrayList(contactsController.contacts);
        ArrayList arrayList2 = new ArrayList();
        String str5 = null;
        int i = 0;
        if (str2 != null) {
            String stripExceptNumbers = PhoneFormat.stripExceptNumbers(str2);
            TLRPC$TL_contact tLRPC$TL_contact = contactsController.contactsByPhone.get(stripExceptNumbers);
            if (tLRPC$TL_contact == null) {
                tLRPC$TL_contact = contactsController.contactsByShortPhone.get(stripExceptNumbers.substring(Math.max(0, stripExceptNumbers.length() - 7)));
            }
            if (tLRPC$TL_contact != null) {
                TLRPC$User user2 = messagesController.getUser(Long.valueOf(tLRPC$TL_contact.user_id));
                if (user2 != null && (!user2.self || z)) {
                    arrayList2.add(tLRPC$TL_contact);
                } else {
                    str3 = null;
                    if (arrayList2.isEmpty() && str3 != null) {
                        lowerCase = str3.trim().toLowerCase();
                        if (!TextUtils.isEmpty(lowerCase)) {
                            String translitString = LocaleController.getInstance().getTranslitString(lowerCase);
                            if (lowerCase.equals(translitString) || translitString.length() == 0) {
                                translitString = null;
                            }
                            int i2 = 2;
                            char c = 1;
                            String[] strArr = {lowerCase, translitString};
                            int size = arrayList.size();
                            int i3 = 0;
                            while (i3 < size) {
                                TLRPC$TL_contact tLRPC$TL_contact2 = (TLRPC$TL_contact) arrayList.get(i3);
                                if (tLRPC$TL_contact2 != null && (user = messagesController.getUser(Long.valueOf(tLRPC$TL_contact2.user_id))) != null && (!user.self || z)) {
                                    String[] strArr2 = new String[3];
                                    strArr2[i] = ContactsController.formatName(user.first_name, user.last_name).toLowerCase();
                                    strArr2[c] = LocaleController.getInstance().getTranslitString(strArr2[i]);
                                    if (strArr2[i].equals(strArr2[c])) {
                                        strArr2[c] = str5;
                                    }
                                    if (UserObject.isReplyUser(user)) {
                                        strArr2[i2] = LocaleController.getString("RepliesTitle", 2131627920).toLowerCase();
                                    } else if (user.self) {
                                        strArr2[i2] = LocaleController.getString("SavedMessages", 2131628077).toLowerCase();
                                    }
                                    int i4 = 0;
                                    boolean z2 = false;
                                    while (true) {
                                        if (i4 >= i2) {
                                            break;
                                        }
                                        String str6 = strArr[i4];
                                        if (str6 != null) {
                                            for (int i5 = 3; i < i5; i5 = 3) {
                                                String str7 = strArr2[i];
                                                if (str7 != null) {
                                                    if (!str7.startsWith(str6)) {
                                                        if (str7.contains(" " + str6)) {
                                                        }
                                                    }
                                                    z2 = true;
                                                    break;
                                                }
                                                i++;
                                            }
                                            if (!z2 && (str4 = user.username) != null && str4.startsWith(str6)) {
                                                z2 = true;
                                            }
                                            if (z2) {
                                                arrayList2.add(tLRPC$TL_contact2);
                                                break;
                                            }
                                        }
                                        i4++;
                                        i = 0;
                                        i2 = 2;
                                    }
                                }
                                i3++;
                                c = 1;
                                str5 = null;
                                i = 0;
                                i2 = 2;
                            }
                        }
                    }
                    return arrayList2;
                }
            }
        }
        str3 = str;
        if (arrayList2.isEmpty()) {
            lowerCase = str3.trim().toLowerCase();
            if (!TextUtils.isEmpty(lowerCase)) {
            }
        }
        return arrayList2;
    }

    private void createUpdateUI() {
        if (this.sideMenuContainer == null) {
            return;
        }
        AnonymousClass16 anonymousClass16 = new AnonymousClass16(this);
        this.updateLayout = anonymousClass16;
        anonymousClass16.setWillNotDraw(false);
        this.updateLayout.setVisibility(4);
        this.updateLayout.setTranslationY(AndroidUtilities.dp(44.0f));
        if (Build.VERSION.SDK_INT >= 21) {
            this.updateLayout.setBackground(Theme.getSelectorDrawable(1090519039, false));
        }
        this.sideMenuContainer.addView(this.updateLayout, LayoutHelper.createFrame(-1, 44, 83));
        this.updateLayout.setOnClickListener(new LaunchActivity$$ExternalSyntheticLambda15(this));
        RadialProgress2 radialProgress2 = new RadialProgress2(this.updateLayout);
        this.updateLayoutIcon = radialProgress2;
        radialProgress2.setColors(-1, -1, -1, -1);
        this.updateLayoutIcon.setProgressRect(AndroidUtilities.dp(22.0f), AndroidUtilities.dp(11.0f), AndroidUtilities.dp(44.0f), AndroidUtilities.dp(33.0f));
        this.updateLayoutIcon.setCircleRadius(AndroidUtilities.dp(11.0f));
        this.updateLayoutIcon.setAsMini();
        SimpleTextView simpleTextView = new SimpleTextView(this);
        this.updateTextView = simpleTextView;
        simpleTextView.setTextSize(15);
        this.updateTextView.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
        this.updateTextView.setText(LocaleController.getString("AppUpdate", 2131624377));
        this.updateTextView.setTextColor(-1);
        this.updateTextView.setGravity(3);
        this.updateLayout.addView(this.updateTextView, LayoutHelper.createFrame(-2, -2.0f, 16, 74.0f, 0.0f, 0.0f, 0.0f));
        TextView textView = new TextView(this);
        this.updateSizeTextView = textView;
        textView.setTextSize(1, 15.0f);
        this.updateSizeTextView.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
        this.updateSizeTextView.setGravity(5);
        this.updateSizeTextView.setTextColor(-1);
        this.updateLayout.addView(this.updateSizeTextView, LayoutHelper.createFrame(-2, -2.0f, 21, 0.0f, 0.0f, 17.0f, 0.0f));
    }

    /* renamed from: org.telegram.ui.LaunchActivity$16 */
    /* loaded from: classes3.dex */
    public class AnonymousClass16 extends FrameLayout {
        private int lastGradientWidth;
        private LinearGradient updateGradient;
        private Paint paint = new Paint();
        private Matrix matrix = new Matrix();

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass16(Context context) {
            super(context);
            LaunchActivity.this = r1;
        }

        @Override // android.view.View
        public void draw(Canvas canvas) {
            if (this.updateGradient != null) {
                this.paint.setColor(-1);
                this.paint.setShader(this.updateGradient);
                this.updateGradient.setLocalMatrix(this.matrix);
                canvas.drawRect(0.0f, 0.0f, getMeasuredWidth(), getMeasuredHeight(), this.paint);
                LaunchActivity.this.updateLayoutIcon.setBackgroundGradientDrawable(this.updateGradient);
                LaunchActivity.this.updateLayoutIcon.draw(canvas);
            }
            super.draw(canvas);
        }

        @Override // android.widget.FrameLayout, android.view.View
        protected void onMeasure(int i, int i2) {
            super.onMeasure(i, i2);
            int size = View.MeasureSpec.getSize(i);
            if (this.lastGradientWidth != size) {
                this.updateGradient = new LinearGradient(0.0f, 0.0f, size, 0.0f, new int[]{-9846926, -11291731}, new float[]{0.0f, 1.0f}, Shader.TileMode.CLAMP);
                this.lastGradientWidth = size;
            }
        }
    }

    public /* synthetic */ void lambda$createUpdateUI$71(View view) {
        if (!SharedConfig.isAppUpdateAvailable()) {
            return;
        }
        if (this.updateLayoutIcon.getIcon() == 2) {
            FileLoader.getInstance(this.currentAccount).loadFile(SharedConfig.pendingAppUpdate.document, "update", 1, 1);
            updateAppUpdateViews(true);
        } else if (this.updateLayoutIcon.getIcon() == 3) {
            FileLoader.getInstance(this.currentAccount).cancelLoadFile(SharedConfig.pendingAppUpdate.document);
            updateAppUpdateViews(true);
        } else {
            AndroidUtilities.openForView(SharedConfig.pendingAppUpdate.document, true, (Activity) this);
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:20:0x00b4  */
    /* JADX WARN: Removed duplicated region for block: B:26:0x00ef  */
    /* JADX WARN: Removed duplicated region for block: B:33:0x0133 A[RETURN] */
    /* JADX WARN: Removed duplicated region for block: B:34:0x0134  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private void updateAppUpdateViews(boolean z) {
        boolean z2;
        if (this.sideMenuContainer == null) {
            return;
        }
        if (SharedConfig.isAppUpdateAvailable()) {
            FrameLayout frameLayout = this.updateLayout;
            createUpdateUI();
            this.updateSizeTextView.setText(AndroidUtilities.formatFileSize(SharedConfig.pendingAppUpdate.document.size));
            String attachFileName = FileLoader.getAttachFileName(SharedConfig.pendingAppUpdate.document);
            if (FileLoader.getInstance(this.currentAccount).getPathToAttach(SharedConfig.pendingAppUpdate.document, true).exists()) {
                this.updateLayoutIcon.setIcon(15, true, false);
                this.updateTextView.setText(LocaleController.getString("AppUpdateNow", 2131624381));
            } else if (FileLoader.getInstance(this.currentAccount).isLoadingFile(attachFileName)) {
                this.updateLayoutIcon.setIcon(3, true, false);
                this.updateLayoutIcon.setProgress(0.0f, false);
                Float fileProgress = ImageLoader.getInstance().getFileProgress(attachFileName);
                SimpleTextView simpleTextView = this.updateTextView;
                Object[] objArr = new Object[1];
                objArr[0] = Integer.valueOf((int) ((fileProgress != null ? fileProgress.floatValue() : 0.0f) * 100.0f));
                simpleTextView.setText(LocaleController.formatString("AppUpdateDownloading", 2131624380, objArr));
            } else {
                this.updateLayoutIcon.setIcon(2, true, false);
                this.updateTextView.setText(LocaleController.getString("AppUpdate", 2131624377));
                z2 = true;
                if (!z2) {
                    if (this.updateSizeTextView.getTag() != null) {
                        if (z) {
                            this.updateSizeTextView.setTag(null);
                            this.updateSizeTextView.animate().alpha(1.0f).scaleX(1.0f).scaleY(1.0f).setDuration(180L).start();
                        } else {
                            this.updateSizeTextView.setAlpha(1.0f);
                            this.updateSizeTextView.setScaleX(1.0f);
                            this.updateSizeTextView.setScaleY(1.0f);
                        }
                    }
                } else if (this.updateSizeTextView.getTag() == null) {
                    if (z) {
                        this.updateSizeTextView.setTag(1);
                        this.updateSizeTextView.animate().alpha(0.0f).scaleX(0.0f).scaleY(0.0f).setDuration(180L).start();
                    } else {
                        this.updateSizeTextView.setAlpha(0.0f);
                        this.updateSizeTextView.setScaleX(0.0f);
                        this.updateSizeTextView.setScaleY(0.0f);
                    }
                }
                if (this.updateLayout.getTag() == null) {
                    return;
                }
                this.updateLayout.setVisibility(0);
                this.updateLayout.setTag(1);
                if (z) {
                    this.updateLayout.animate().translationY(0.0f).setInterpolator(CubicBezierInterpolator.EASE_OUT).setListener(null).setDuration(180L).withEndAction(new LaunchActivity$$ExternalSyntheticLambda22(frameLayout)).start();
                } else {
                    this.updateLayout.setTranslationY(0.0f);
                    if (frameLayout != null) {
                        ((ViewGroup) frameLayout.getParent()).removeView(frameLayout);
                    }
                }
                this.sideMenu.setPadding(0, 0, 0, AndroidUtilities.dp(44.0f));
                return;
            }
            z2 = false;
            if (!z2) {
            }
            if (this.updateLayout.getTag() == null) {
            }
        } else {
            FrameLayout frameLayout2 = this.updateLayout;
            if (frameLayout2 == null || frameLayout2.getTag() == null) {
                return;
            }
            this.updateLayout.setTag(null);
            if (z) {
                this.updateLayout.animate().translationY(AndroidUtilities.dp(44.0f)).setInterpolator(CubicBezierInterpolator.EASE_OUT).setListener(new AnonymousClass17()).setDuration(180L).start();
            } else {
                this.updateLayout.setTranslationY(AndroidUtilities.dp(44.0f));
                this.updateLayout.setVisibility(4);
            }
            this.sideMenu.setPadding(0, 0, 0, 0);
        }
    }

    public static /* synthetic */ void lambda$updateAppUpdateViews$72(View view) {
        if (view != null) {
            ((ViewGroup) view.getParent()).removeView(view);
        }
    }

    /* renamed from: org.telegram.ui.LaunchActivity$17 */
    /* loaded from: classes3.dex */
    public class AnonymousClass17 extends AnimatorListenerAdapter {
        AnonymousClass17() {
            LaunchActivity.this = r1;
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator) {
            if (LaunchActivity.this.updateLayout.getTag() == null) {
                LaunchActivity.this.updateLayout.setVisibility(4);
            }
        }
    }

    public void checkAppUpdate(boolean z) {
        if (z || !BuildVars.DEBUG_VERSION) {
            if (!z && !BuildVars.CHECK_UPDATES) {
                return;
            }
            if (!z && Math.abs(System.currentTimeMillis() - SharedConfig.lastUpdateCheckTime) < MessagesController.getInstance(0).updateCheckDelay * 1000) {
                return;
            }
            TLRPC$TL_help_getAppUpdate tLRPC$TL_help_getAppUpdate = new TLRPC$TL_help_getAppUpdate();
            try {
                tLRPC$TL_help_getAppUpdate.source = ApplicationLoader.applicationContext.getPackageManager().getInstallerPackageName(ApplicationLoader.applicationContext.getPackageName());
            } catch (Exception unused) {
            }
            if (tLRPC$TL_help_getAppUpdate.source == null) {
                tLRPC$TL_help_getAppUpdate.source = "";
            }
            int i = this.currentAccount;
            ConnectionsManager.getInstance(i).sendRequest(tLRPC$TL_help_getAppUpdate, new LaunchActivity$$ExternalSyntheticLambda69(this, i));
        }
    }

    public /* synthetic */ void lambda$checkAppUpdate$74(int i, TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        SharedConfig.lastUpdateCheckTime = System.currentTimeMillis();
        SharedConfig.saveConfig();
        if (tLObject instanceof TLRPC$TL_help_appUpdate) {
            AndroidUtilities.runOnUIThread(new LaunchActivity$$ExternalSyntheticLambda51(this, (TLRPC$TL_help_appUpdate) tLObject, i));
        }
    }

    public /* synthetic */ void lambda$checkAppUpdate$73(TLRPC$TL_help_appUpdate tLRPC$TL_help_appUpdate, int i) {
        TLRPC$TL_help_appUpdate tLRPC$TL_help_appUpdate2 = SharedConfig.pendingAppUpdate;
        if ((tLRPC$TL_help_appUpdate2 == null || !tLRPC$TL_help_appUpdate2.version.equals(tLRPC$TL_help_appUpdate.version)) && SharedConfig.setNewAppVersionAvailable(tLRPC$TL_help_appUpdate)) {
            if (tLRPC$TL_help_appUpdate.can_not_skip) {
                showUpdateActivity(i, tLRPC$TL_help_appUpdate, false);
            } else {
                this.drawerLayoutAdapter.notifyDataSetChanged();
                try {
                    new UpdateAppAlertDialog(this, tLRPC$TL_help_appUpdate, i).show();
                } catch (Exception e) {
                    FileLog.e(e);
                }
            }
            NotificationCenter.getGlobalInstance().postNotificationName(NotificationCenter.appUpdateAvailable, new Object[0]);
        }
    }

    public AlertDialog showAlertDialog(AlertDialog.Builder builder) {
        try {
            AlertDialog alertDialog = this.visibleDialog;
            if (alertDialog != null) {
                alertDialog.dismiss();
                this.visibleDialog = null;
            }
        } catch (Exception e) {
            FileLog.e(e);
        }
        try {
            AlertDialog show = builder.show();
            this.visibleDialog = show;
            show.setCanceledOnTouchOutside(true);
            this.visibleDialog.setOnDismissListener(new LaunchActivity$$ExternalSyntheticLambda14(this));
            return this.visibleDialog;
        } catch (Exception e2) {
            FileLog.e(e2);
            return null;
        }
    }

    public /* synthetic */ void lambda$showAlertDialog$75(DialogInterface dialogInterface) {
        AlertDialog alertDialog = this.visibleDialog;
        if (alertDialog != null) {
            if (alertDialog == this.localeDialog) {
                try {
                    Toast.makeText(this, getStringForLanguageAlert(LocaleController.getInstance().getCurrentLocaleInfo().shortName.equals("en") ? this.englishLocaleStrings : this.systemLocaleStrings, "ChangeLanguageLater", 2131624851), 1).show();
                } catch (Exception e) {
                    FileLog.e(e);
                }
                this.localeDialog = null;
            } else if (alertDialog == this.proxyErrorDialog) {
                MessagesController.getGlobalMainSettings();
                SharedPreferences.Editor edit = MessagesController.getGlobalMainSettings().edit();
                edit.putBoolean("proxy_enabled", false);
                edit.putBoolean("proxy_enabled_calls", false);
                edit.commit();
                ConnectionsManager.setProxySettings(false, "", 1080, "", "", "");
                NotificationCenter.getGlobalInstance().postNotificationName(NotificationCenter.proxySettingsChanged, new Object[0]);
                this.proxyErrorDialog = null;
            }
        }
        this.visibleDialog = null;
    }

    public void showBulletin(Function<BulletinFactory, Bulletin> function) {
        BaseFragment baseFragment;
        if (!layerFragmentsStack.isEmpty()) {
            ArrayList<BaseFragment> arrayList = layerFragmentsStack;
            baseFragment = arrayList.get(arrayList.size() - 1);
        } else if (!rightFragmentsStack.isEmpty()) {
            ArrayList<BaseFragment> arrayList2 = rightFragmentsStack;
            baseFragment = arrayList2.get(arrayList2.size() - 1);
        } else if (!mainFragmentsStack.isEmpty()) {
            ArrayList<BaseFragment> arrayList3 = mainFragmentsStack;
            baseFragment = arrayList3.get(arrayList3.size() - 1);
        } else {
            baseFragment = null;
        }
        if (BulletinFactory.canShowBulletin(baseFragment)) {
            function.apply(BulletinFactory.of(baseFragment)).show();
        }
    }

    public void setNavigateToPremiumBot(boolean z) {
        this.navigateToPremiumBot = z;
    }

    @Override // android.app.Activity
    public void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        handleIntent(intent, true, false, false);
    }

    @Override // org.telegram.ui.DialogsActivity.DialogsActivityDelegate
    public void didSelectDialogs(DialogsActivity dialogsActivity, ArrayList<Long> arrayList, CharSequence charSequence, boolean z) {
        ChatActivity chatActivity;
        boolean z2;
        boolean z3;
        ChatActivity chatActivity2;
        int i;
        boolean z4;
        boolean z5;
        ArrayList<SendMessagesHelper.SendingMediaInfo> arrayList2;
        int currentAccount = dialogsActivity != null ? dialogsActivity.getCurrentAccount() : this.currentAccount;
        Uri uri = this.exportingChatUri;
        if (uri != null) {
            ArrayList arrayList3 = this.documentsUrisArray != null ? new ArrayList(this.documentsUrisArray) : null;
            AlertDialog alertDialog = new AlertDialog(this, 3);
            SendMessagesHelper.getInstance(currentAccount).prepareImportHistory(arrayList.get(0).longValue(), this.exportingChatUri, this.documentsUrisArray, new LaunchActivity$$ExternalSyntheticLambda67(this, currentAccount, dialogsActivity, z, arrayList3, uri, alertDialog));
            try {
                alertDialog.showDelayed(300L);
            } catch (Exception unused) {
            }
        } else {
            boolean z6 = dialogsActivity == null || dialogsActivity.notify;
            if (arrayList.size() <= 1) {
                long longValue = arrayList.get(0).longValue();
                Bundle bundle = new Bundle();
                bundle.putBoolean("scrollToTopOnResume", true);
                if (!AndroidUtilities.isTablet()) {
                    NotificationCenter.getInstance(currentAccount).postNotificationName(NotificationCenter.closeChats, new Object[0]);
                }
                if (DialogObject.isEncryptedDialog(longValue)) {
                    bundle.putInt("enc_id", DialogObject.getEncryptedChatId(longValue));
                } else if (DialogObject.isUserDialog(longValue)) {
                    bundle.putLong("user_id", longValue);
                } else {
                    bundle.putLong("chat_id", -longValue);
                }
                if (!MessagesController.getInstance(currentAccount).checkCanOpenChat(bundle, dialogsActivity)) {
                    return;
                }
                chatActivity = new ChatActivity(bundle);
            } else {
                chatActivity = null;
            }
            ArrayList<TLRPC$User> arrayList4 = this.contactsToSend;
            int size = arrayList4 != null ? arrayList4.size() + 0 : 0;
            if (this.videoPath != null) {
                size++;
            }
            ArrayList<SendMessagesHelper.SendingMediaInfo> arrayList5 = this.photoPathsArray;
            if (arrayList5 != null) {
                size += arrayList5.size();
            }
            ArrayList<String> arrayList6 = this.documentsPathsArray;
            if (arrayList6 != null) {
                size += arrayList6.size();
            }
            ArrayList<Uri> arrayList7 = this.documentsUrisArray;
            if (arrayList7 != null) {
                size += arrayList7.size();
            }
            if (this.videoPath == null && this.photoPathsArray == null && this.documentsPathsArray == null && this.documentsUrisArray == null && this.sendingText != null) {
                size++;
            }
            for (int i2 = 0; i2 < arrayList.size(); i2++) {
                if (AlertsCreator.checkSlowMode(this, this.currentAccount, arrayList.get(i2).longValue(), size > 1)) {
                    return;
                }
            }
            ArrayList<TLRPC$User> arrayList8 = this.contactsToSend;
            if (arrayList8 != null && arrayList8.size() == 1 && !mainFragmentsStack.isEmpty()) {
                ArrayList<BaseFragment> arrayList9 = mainFragmentsStack;
                PhonebookShareAlert phonebookShareAlert = new PhonebookShareAlert(arrayList9.get(arrayList9.size() - 1), null, null, this.contactsToSendUri, null, null, null);
                phonebookShareAlert.setDelegate(new LaunchActivity$$ExternalSyntheticLambda92(this, chatActivity, arrayList, currentAccount, charSequence, z6));
                ArrayList<BaseFragment> arrayList10 = mainFragmentsStack;
                arrayList10.get(arrayList10.size() - 1).showDialog(phonebookShareAlert);
            } else {
                String str = null;
                int i3 = 0;
                while (i3 < arrayList.size()) {
                    long longValue2 = arrayList.get(i3).longValue();
                    AccountInstance accountInstance = AccountInstance.getInstance(UserConfig.selectedAccount);
                    if (chatActivity != null) {
                        i = 1024;
                        chatActivity2 = chatActivity;
                        this.actionBarLayout.presentFragment(chatActivity, dialogsActivity != null, dialogsActivity == null || this.videoPath != null || ((arrayList2 = this.photoPathsArray) != null && arrayList2.size() > 0), true, false);
                        String str2 = this.videoPath;
                        if (str2 != null) {
                            chatActivity2.openVideoEditor(str2, this.sendingText);
                            this.sendingText = null;
                            z5 = true;
                        } else {
                            ArrayList<SendMessagesHelper.SendingMediaInfo> arrayList11 = this.photoPathsArray;
                            if (arrayList11 == null || arrayList11.size() <= 0) {
                                z5 = false;
                            } else {
                                boolean openPhotosEditor = chatActivity2.openPhotosEditor(this.photoPathsArray, (charSequence == null || charSequence.length() == 0) ? this.sendingText : charSequence);
                                if (openPhotosEditor) {
                                    this.sendingText = null;
                                }
                                z4 = openPhotosEditor;
                                z5 = false;
                                z3 = z5;
                                z2 = z4;
                            }
                        }
                        z4 = false;
                        z3 = z5;
                        z2 = z4;
                    } else {
                        chatActivity2 = chatActivity;
                        i = 1024;
                        if (this.videoPath != null) {
                            String str3 = this.sendingText;
                            if (str3 != null && str3.length() <= 1024) {
                                str = this.sendingText;
                                this.sendingText = null;
                            }
                            ArrayList arrayList12 = new ArrayList();
                            arrayList12.add(this.videoPath);
                            SendMessagesHelper.prepareSendingDocuments(accountInstance, arrayList12, arrayList12, null, str, null, longValue2, null, null, null, null, z6, 0);
                        }
                        z3 = false;
                        z2 = false;
                    }
                    if (this.photoPathsArray != null && !z2) {
                        String str4 = this.sendingText;
                        if (str4 != null && str4.length() <= i && this.photoPathsArray.size() == 1) {
                            this.photoPathsArray.get(0).caption = this.sendingText;
                            this.sendingText = null;
                        }
                        SendMessagesHelper.prepareSendingMedia(accountInstance, this.photoPathsArray, longValue2, null, null, null, false, false, null, z6, 0);
                    }
                    if (this.documentsPathsArray != null || this.documentsUrisArray != null) {
                        String str5 = this.sendingText;
                        if (str5 != null && str5.length() <= i) {
                            ArrayList<String> arrayList13 = this.documentsPathsArray;
                            int size2 = arrayList13 != null ? arrayList13.size() : 0;
                            ArrayList<Uri> arrayList14 = this.documentsUrisArray;
                            if (size2 + (arrayList14 != null ? arrayList14.size() : 0) == 1) {
                                str = this.sendingText;
                                this.sendingText = null;
                            }
                        }
                        SendMessagesHelper.prepareSendingDocuments(accountInstance, this.documentsPathsArray, this.documentsOriginalPathsArray, this.documentsUrisArray, str, this.documentsMimeType, longValue2, null, null, null, null, z6, 0);
                    }
                    String str6 = this.sendingText;
                    if (str6 != null) {
                        SendMessagesHelper.prepareSendingText(accountInstance, str6, longValue2, true, 0);
                    }
                    ArrayList<TLRPC$User> arrayList15 = this.contactsToSend;
                    if (arrayList15 != null && !arrayList15.isEmpty()) {
                        for (int i4 = 0; i4 < this.contactsToSend.size(); i4++) {
                            SendMessagesHelper.getInstance(currentAccount).sendMessage(this.contactsToSend.get(i4), longValue2, (MessageObject) null, (MessageObject) null, (TLRPC$ReplyMarkup) null, (HashMap<String, String>) null, z6, 0);
                        }
                    }
                    if (!TextUtils.isEmpty(charSequence) && !z3 && !z2) {
                        SendMessagesHelper.prepareSendingText(accountInstance, charSequence.toString(), longValue2, z6, 0);
                    }
                    i3++;
                    chatActivity = chatActivity2;
                }
            }
            ChatActivity chatActivity3 = chatActivity;
            if (dialogsActivity != null && chatActivity3 == null) {
                dialogsActivity.finishFragment();
            }
        }
        this.photoPathsArray = null;
        this.videoPath = null;
        this.sendingText = null;
        this.documentsPathsArray = null;
        this.documentsOriginalPathsArray = null;
        this.contactsToSend = null;
        this.contactsToSendUri = null;
        this.exportingChatUri = null;
    }

    public /* synthetic */ void lambda$didSelectDialogs$76(int i, DialogsActivity dialogsActivity, boolean z, ArrayList arrayList, Uri uri, AlertDialog alertDialog, long j) {
        if (j != 0) {
            Bundle bundle = new Bundle();
            bundle.putBoolean("scrollToTopOnResume", true);
            if (!AndroidUtilities.isTablet()) {
                NotificationCenter.getInstance(i).postNotificationName(NotificationCenter.closeChats, new Object[0]);
            }
            if (DialogObject.isUserDialog(j)) {
                bundle.putLong("user_id", j);
            } else {
                bundle.putLong("chat_id", -j);
            }
            ChatActivity chatActivity = new ChatActivity(bundle);
            chatActivity.setOpenImport();
            this.actionBarLayout.presentFragment(chatActivity, dialogsActivity != null || z, dialogsActivity == null, true, false);
        } else {
            this.documentsUrisArray = arrayList;
            if (arrayList == null) {
                this.documentsUrisArray = new ArrayList<>();
            }
            this.documentsUrisArray.add(0, uri);
            openDialogsToSend(true);
        }
        try {
            alertDialog.dismiss();
        } catch (Exception e) {
            FileLog.e(e);
        }
    }

    public /* synthetic */ void lambda$didSelectDialogs$77(ChatActivity chatActivity, ArrayList arrayList, int i, CharSequence charSequence, boolean z, TLRPC$User tLRPC$User, boolean z2, int i2) {
        if (chatActivity != null) {
            this.actionBarLayout.presentFragment(chatActivity, true, false, true, false);
        }
        AccountInstance accountInstance = AccountInstance.getInstance(UserConfig.selectedAccount);
        for (int i3 = 0; i3 < arrayList.size(); i3++) {
            long longValue = ((Long) arrayList.get(i3)).longValue();
            SendMessagesHelper.getInstance(i).sendMessage(tLRPC$User, longValue, (MessageObject) null, (MessageObject) null, (TLRPC$ReplyMarkup) null, (HashMap<String, String>) null, z2, i2);
            if (!TextUtils.isEmpty(charSequence)) {
                SendMessagesHelper.prepareSendingText(accountInstance, charSequence.toString(), longValue, z, 0);
            }
        }
    }

    private void onFinish() {
        Runnable runnable = this.lockRunnable;
        if (runnable != null) {
            AndroidUtilities.cancelRunOnUIThread(runnable);
            this.lockRunnable = null;
        }
        if (this.finished) {
            return;
        }
        this.finished = true;
        int i = this.currentAccount;
        if (i != -1) {
            NotificationCenter.getInstance(i).removeObserver(this, NotificationCenter.appDidLogout);
            NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.mainUserInfoChanged);
            NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.didUpdateConnectionState);
            NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.needShowAlert);
            NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.wasUnableToFindCurrentLocation);
            NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.openArticle);
            NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.hasNewContactsToImport);
            NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.needShowPlayServicesAlert);
            NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.fileLoaded);
            NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.fileLoadProgressChanged);
            NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.fileLoadFailed);
            NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.historyImportProgressChanged);
            NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.groupCallUpdated);
            NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.stickersImportComplete);
            NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.newSuggestionsAvailable);
            NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.currentUserShowLimitReachedDialog);
        }
        NotificationCenter.getGlobalInstance().removeObserver(this, NotificationCenter.needShowAlert);
        NotificationCenter.getGlobalInstance().removeObserver(this, NotificationCenter.didSetNewWallpapper);
        NotificationCenter.getGlobalInstance().removeObserver(this, NotificationCenter.suggestedLangpack);
        NotificationCenter.getGlobalInstance().removeObserver(this, NotificationCenter.reloadInterface);
        NotificationCenter.getGlobalInstance().removeObserver(this, NotificationCenter.didSetNewTheme);
        NotificationCenter.getGlobalInstance().removeObserver(this, NotificationCenter.needSetDayNightTheme);
        NotificationCenter.getGlobalInstance().removeObserver(this, NotificationCenter.needCheckSystemBarColors);
        NotificationCenter.getGlobalInstance().removeObserver(this, NotificationCenter.closeOtherAppActivities);
        NotificationCenter.getGlobalInstance().removeObserver(this, NotificationCenter.didSetPasscode);
        NotificationCenter.getGlobalInstance().removeObserver(this, NotificationCenter.notificationsCountUpdated);
        NotificationCenter.getGlobalInstance().removeObserver(this, NotificationCenter.screenStateChanged);
        NotificationCenter.getGlobalInstance().removeObserver(this, NotificationCenter.showBulletin);
        NotificationCenter.getGlobalInstance().removeObserver(this, NotificationCenter.appUpdateAvailable);
    }

    /* renamed from: presentFragment */
    public void lambda$runLinkRequest$59(BaseFragment baseFragment) {
        this.actionBarLayout.presentFragment(baseFragment);
    }

    public boolean presentFragment(BaseFragment baseFragment, boolean z, boolean z2) {
        return this.actionBarLayout.presentFragment(baseFragment, z, z2, true, false);
    }

    public ActionBarLayout getActionBarLayout() {
        return this.actionBarLayout;
    }

    public ActionBarLayout getLayersActionBarLayout() {
        return this.layersActionBarLayout;
    }

    public ActionBarLayout getRightActionBarLayout() {
        return this.rightActionBarLayout;
    }

    @Override // android.app.Activity
    protected void onActivityResult(int i, int i2, Intent intent) {
        VoIPService sharedInstance;
        boolean z = false;
        if (SharedConfig.passcodeHash.length() != 0 && SharedConfig.lastPauseTime != 0) {
            SharedConfig.lastPauseTime = 0;
            if (BuildVars.LOGS_ENABLED) {
                FileLog.d("reset lastPauseTime onActivityResult");
            }
            UserConfig.getInstance(this.currentAccount).saveConfig(false);
        }
        if (i == 105) {
            if (Build.VERSION.SDK_INT < 23) {
                return;
            }
            boolean canDrawOverlays = Settings.canDrawOverlays(this);
            ApplicationLoader.canDrawOverlays = canDrawOverlays;
            if (!canDrawOverlays) {
                return;
            }
            GroupCallActivity groupCallActivity = GroupCallActivity.groupCallInstance;
            if (groupCallActivity != null) {
                groupCallActivity.dismissInternal();
            }
            AndroidUtilities.runOnUIThread(new LaunchActivity$$ExternalSyntheticLambda32(this), 200L);
            return;
        }
        super.onActivityResult(i, i2, intent);
        if (i == 520) {
            if (i2 != -1 || (sharedInstance = VoIPService.getSharedInstance()) == null) {
                return;
            }
            VideoCapturerDevice.mediaProjectionPermissionResultData = intent;
            sharedInstance.createCaptureDevice(true);
        } else if (i == 140) {
            LocationController locationController = LocationController.getInstance(this.currentAccount);
            if (i2 == -1) {
                z = true;
            }
            locationController.startFusedLocationRequest(z);
        } else {
            ThemeEditorView themeEditorView = ThemeEditorView.getInstance();
            if (themeEditorView != null) {
                themeEditorView.onActivityResult(i, i2, intent);
            }
            if (this.actionBarLayout.fragmentsStack.size() != 0) {
                ArrayList<BaseFragment> arrayList = this.actionBarLayout.fragmentsStack;
                arrayList.get(arrayList.size() - 1).onActivityResultFragment(i, i2, intent);
            }
            if (AndroidUtilities.isTablet()) {
                if (this.rightActionBarLayout.fragmentsStack.size() != 0) {
                    ArrayList<BaseFragment> arrayList2 = this.rightActionBarLayout.fragmentsStack;
                    arrayList2.get(arrayList2.size() - 1).onActivityResultFragment(i, i2, intent);
                }
                if (this.layersActionBarLayout.fragmentsStack.size() != 0) {
                    ArrayList<BaseFragment> arrayList3 = this.layersActionBarLayout.fragmentsStack;
                    arrayList3.get(arrayList3.size() - 1).onActivityResultFragment(i, i2, intent);
                }
            }
            NotificationCenter.getGlobalInstance().postNotificationName(NotificationCenter.onActivityResultReceived, Integer.valueOf(i), Integer.valueOf(i2), intent);
        }
    }

    public /* synthetic */ void lambda$onActivityResult$78() {
        GroupCallPip.clearForce();
        GroupCallPip.updateVisibility(this);
    }

    @Override // android.app.Activity
    public void onRequestPermissionsResult(int i, String[] strArr, int[] iArr) {
        super.onRequestPermissionsResult(i, strArr, iArr);
        if (!checkPermissionsResult(i, strArr, iArr)) {
            return;
        }
        if (this.actionBarLayout.fragmentsStack.size() != 0) {
            ArrayList<BaseFragment> arrayList = this.actionBarLayout.fragmentsStack;
            arrayList.get(arrayList.size() - 1).onRequestPermissionsResultFragment(i, strArr, iArr);
        }
        if (AndroidUtilities.isTablet()) {
            if (this.rightActionBarLayout.fragmentsStack.size() != 0) {
                ArrayList<BaseFragment> arrayList2 = this.rightActionBarLayout.fragmentsStack;
                arrayList2.get(arrayList2.size() - 1).onRequestPermissionsResultFragment(i, strArr, iArr);
            }
            if (this.layersActionBarLayout.fragmentsStack.size() != 0) {
                ArrayList<BaseFragment> arrayList3 = this.layersActionBarLayout.fragmentsStack;
                arrayList3.get(arrayList3.size() - 1).onRequestPermissionsResultFragment(i, strArr, iArr);
            }
        }
        VoIPFragment.onRequestPermissionsResult(i, strArr, iArr);
        NotificationCenter.getGlobalInstance().postNotificationName(NotificationCenter.onRequestPermissionResultReceived, Integer.valueOf(i), strArr, iArr);
    }

    @Override // android.app.Activity
    protected void onPause() {
        super.onPause();
        isResumed = false;
        NotificationCenter.getGlobalInstance().postNotificationName(NotificationCenter.stopAllHeavyOperations, 4096);
        ApplicationLoader.mainInterfacePaused = true;
        Utilities.stageQueue.postRunnable(new LaunchActivity$$ExternalSyntheticLambda21(this.currentAccount));
        onPasscodePause();
        this.actionBarLayout.onPause();
        if (AndroidUtilities.isTablet()) {
            this.rightActionBarLayout.onPause();
            this.layersActionBarLayout.onPause();
        }
        PasscodeView passcodeView = this.passcodeView;
        if (passcodeView != null) {
            passcodeView.onPause();
        }
        ConnectionsManager.getInstance(this.currentAccount).setAppPaused(true, false);
        if (PhotoViewer.hasInstance() && PhotoViewer.getInstance().isVisible()) {
            PhotoViewer.getInstance().onPause();
        }
        if (VoIPFragment.getInstance() != null) {
            VoIPFragment.onPause();
        }
    }

    public static /* synthetic */ void lambda$onPause$79(int i) {
        ApplicationLoader.mainInterfacePausedStageQueue = true;
        ApplicationLoader.mainInterfacePausedStageQueueTime = 0L;
        if (VoIPService.getSharedInstance() == null) {
            MessagesController.getInstance(i).ignoreSetOnline = false;
        }
    }

    @Override // android.app.Activity
    protected void onStart() {
        super.onStart();
        Browser.bindCustomTabsService(this);
        ApplicationLoader.mainInterfaceStopped = false;
        GroupCallPip.updateVisibility(this);
        GroupCallActivity groupCallActivity = GroupCallActivity.groupCallInstance;
        if (groupCallActivity != null) {
            groupCallActivity.onResume();
        }
    }

    @Override // android.app.Activity
    protected void onStop() {
        super.onStop();
        Browser.unbindCustomTabsService(this);
        ApplicationLoader.mainInterfaceStopped = true;
        GroupCallPip.updateVisibility(this);
        GroupCallActivity groupCallActivity = GroupCallActivity.groupCallInstance;
        if (groupCallActivity != null) {
            groupCallActivity.onPause();
        }
    }

    @Override // android.app.Activity
    protected void onDestroy() {
        if (PhotoViewer.getPipInstance() != null) {
            PhotoViewer.getPipInstance().destroyPhotoViewer();
        }
        if (PhotoViewer.hasInstance()) {
            PhotoViewer.getInstance().destroyPhotoViewer();
        }
        if (SecretMediaViewer.hasInstance()) {
            SecretMediaViewer.getInstance().destroyPhotoViewer();
        }
        if (ArticleViewer.hasInstance()) {
            ArticleViewer.getInstance().destroyArticleViewer();
        }
        if (ContentPreviewViewer.hasInstance()) {
            ContentPreviewViewer.getInstance().destroy();
        }
        GroupCallActivity groupCallActivity = GroupCallActivity.groupCallInstance;
        if (groupCallActivity != null) {
            groupCallActivity.dismissInternal();
        }
        PipRoundVideoView pipRoundVideoView = PipRoundVideoView.getInstance();
        MediaController.getInstance().setBaseActivity(this, false);
        MediaController.getInstance().setFeedbackView(this.actionBarLayout, false);
        if (pipRoundVideoView != null) {
            pipRoundVideoView.close(false);
        }
        Theme.destroyResources();
        EmbedBottomSheet embedBottomSheet = EmbedBottomSheet.getInstance();
        if (embedBottomSheet != null) {
            embedBottomSheet.destroy();
        }
        ThemeEditorView themeEditorView = ThemeEditorView.getInstance();
        if (themeEditorView != null) {
            themeEditorView.destroy();
        }
        try {
            AlertDialog alertDialog = this.visibleDialog;
            if (alertDialog != null) {
                alertDialog.dismiss();
                this.visibleDialog = null;
            }
        } catch (Exception e) {
            FileLog.e(e);
        }
        try {
            if (this.onGlobalLayoutListener != null) {
                getWindow().getDecorView().getRootView().getViewTreeObserver().removeOnGlobalLayoutListener(this.onGlobalLayoutListener);
            }
        } catch (Exception e2) {
            FileLog.e(e2);
        }
        super.onDestroy();
        onFinish();
    }

    @Override // android.app.Activity
    protected void onUserLeaveHint() {
        for (Runnable runnable : this.onUserLeaveHintListeners) {
            runnable.run();
        }
        this.actionBarLayout.onUserLeaveHint();
    }

    @Override // android.app.Activity
    protected void onResume() {
        MessageObject playingMessageObject;
        super.onResume();
        isResumed = true;
        Runnable runnable = onResumeStaticCallback;
        if (runnable != null) {
            runnable.run();
            onResumeStaticCallback = null;
        }
        if (Theme.selectedAutoNightType == 3) {
            Theme.checkAutoNightThemeConditions();
        }
        checkWasMutedByAdmin(true);
        NotificationCenter.getGlobalInstance().postNotificationName(NotificationCenter.startAllHeavyOperations, 4096);
        MediaController.getInstance().setFeedbackView(this.actionBarLayout, true);
        ApplicationLoader.mainInterfacePaused = false;
        showLanguageAlert(false);
        Utilities.stageQueue.postRunnable(LaunchActivity$$ExternalSyntheticLambda63.INSTANCE);
        checkFreeDiscSpace();
        MediaController.checkGallery();
        onPasscodeResume();
        PasscodeView passcodeView = this.passcodeView;
        if (passcodeView == null || passcodeView.getVisibility() != 0) {
            this.actionBarLayout.onResume();
            if (AndroidUtilities.isTablet()) {
                this.rightActionBarLayout.onResume();
                this.layersActionBarLayout.onResume();
            }
        } else {
            this.actionBarLayout.dismissDialogs();
            if (AndroidUtilities.isTablet()) {
                this.rightActionBarLayout.dismissDialogs();
                this.layersActionBarLayout.dismissDialogs();
            }
            this.passcodeView.onResume();
        }
        ConnectionsManager.getInstance(this.currentAccount).setAppPaused(false, false);
        updateCurrentConnectionState(this.currentAccount);
        if (PhotoViewer.hasInstance() && PhotoViewer.getInstance().isVisible()) {
            PhotoViewer.getInstance().onResume();
        }
        if (PipRoundVideoView.getInstance() != null && MediaController.getInstance().isMessagePaused() && (playingMessageObject = MediaController.getInstance().getPlayingMessageObject()) != null) {
            MediaController.getInstance().seekToProgress(playingMessageObject, playingMessageObject.audioProgress);
        }
        if (UserConfig.getInstance(UserConfig.selectedAccount).unacceptedTermsOfService != null) {
            int i = UserConfig.selectedAccount;
            showTosActivity(i, UserConfig.getInstance(i).unacceptedTermsOfService);
        } else {
            TLRPC$TL_help_appUpdate tLRPC$TL_help_appUpdate = SharedConfig.pendingAppUpdate;
            if (tLRPC$TL_help_appUpdate != null && tLRPC$TL_help_appUpdate.can_not_skip) {
                showUpdateActivity(UserConfig.selectedAccount, SharedConfig.pendingAppUpdate, true);
            }
        }
        checkAppUpdate(false);
        if (Build.VERSION.SDK_INT >= 23) {
            ApplicationLoader.canDrawOverlays = Settings.canDrawOverlays(this);
        }
        if (VoIPFragment.getInstance() != null) {
            VoIPFragment.onResume();
        }
    }

    public static /* synthetic */ void lambda$onResume$80() {
        ApplicationLoader.mainInterfacePausedStageQueue = false;
        ApplicationLoader.mainInterfacePausedStageQueueTime = System.currentTimeMillis();
    }

    @Override // android.app.Activity, android.content.ComponentCallbacks
    public void onConfigurationChanged(Configuration configuration) {
        AndroidUtilities.checkDisplaySize(this, configuration);
        super.onConfigurationChanged(configuration);
        checkLayout();
        PipRoundVideoView pipRoundVideoView = PipRoundVideoView.getInstance();
        if (pipRoundVideoView != null) {
            pipRoundVideoView.onConfigurationChanged();
        }
        EmbedBottomSheet embedBottomSheet = EmbedBottomSheet.getInstance();
        if (embedBottomSheet != null) {
            embedBottomSheet.onConfigurationChanged(configuration);
        }
        PhotoViewer pipInstance = PhotoViewer.getPipInstance();
        if (pipInstance != null) {
            pipInstance.onConfigurationChanged(configuration);
        }
        ThemeEditorView themeEditorView = ThemeEditorView.getInstance();
        if (themeEditorView != null) {
            themeEditorView.onConfigurationChanged();
        }
        if (Theme.selectedAutoNightType == 3) {
            Theme.checkAutoNightThemeConditions();
        }
    }

    @Override // android.app.Activity
    public void onMultiWindowModeChanged(boolean z) {
        AndroidUtilities.isInMultiwindow = z;
        checkLayout();
    }

    /* JADX WARN: Code restructure failed: missing block: B:100:0x0288, code lost:
        if (((org.telegram.ui.ProfileActivity) r1.get(r1.size() - 1)).isSettings() == false) goto L102;
     */
    /* JADX WARN: Removed duplicated region for block: B:206:0x05d8  */
    /* JADX WARN: Removed duplicated region for block: B:424:? A[RETURN, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:99:0x0277  */
    @Override // org.telegram.messenger.NotificationCenter.NotificationCenterDelegate
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void didReceivedNotification(int i, int i2, Object... objArr) {
        BaseFragment baseFragment;
        String str;
        int i3;
        String str2;
        int i4;
        GroupCallActivity groupCallActivity;
        BaseFragment baseFragment2;
        boolean z;
        boolean z2;
        View childAt;
        if (i == NotificationCenter.appDidLogout) {
            switchToAvailableAccountOrLogout();
            return;
        }
        boolean z3 = false;
        boolean z4 = false;
        r4 = false;
        r4 = false;
        boolean z5 = false;
        r4 = false;
        boolean z6 = false;
        if (i == NotificationCenter.closeOtherAppActivities) {
            if (objArr[0] == this) {
                return;
            }
            onFinish();
            finish();
        } else if (i == NotificationCenter.didUpdateConnectionState) {
            int connectionState = ConnectionsManager.getInstance(i2).getConnectionState();
            if (this.currentConnectionState == connectionState) {
                return;
            }
            if (BuildVars.LOGS_ENABLED) {
                FileLog.d("switch to state " + connectionState);
            }
            this.currentConnectionState = connectionState;
            updateCurrentConnectionState(i2);
        } else if (i == NotificationCenter.mainUserInfoChanged) {
            this.drawerLayoutAdapter.notifyDataSetChanged();
        } else if (i == NotificationCenter.needShowAlert) {
            Integer num = (Integer) objArr[0];
            if (num.intValue() == 6) {
                return;
            }
            if (num.intValue() == 3 && this.proxyErrorDialog != null) {
                return;
            }
            if (num.intValue() == 4) {
                showTosActivity(i2, (TLRPC$TL_help_termsOfService) objArr[1]);
                return;
            }
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(LocaleController.getString("AppName", 2131624375));
            if (num.intValue() != 2 && num.intValue() != 3) {
                builder.setNegativeButton(LocaleController.getString("MoreInfo", 2131626737), new LaunchActivity$$ExternalSyntheticLambda4(i2));
            }
            if (num.intValue() == 5) {
                builder.setMessage(LocaleController.getString("NobodyLikesSpam3", 2131626883));
                builder.setPositiveButton(LocaleController.getString("OK", 2131627075), null);
            } else if (num.intValue() == 0) {
                builder.setMessage(LocaleController.getString("NobodyLikesSpam1", 2131626881));
                builder.setPositiveButton(LocaleController.getString("OK", 2131627075), null);
            } else if (num.intValue() == 1) {
                builder.setMessage(LocaleController.getString("NobodyLikesSpam2", 2131626882));
                builder.setPositiveButton(LocaleController.getString("OK", 2131627075), null);
            } else if (num.intValue() == 2) {
                builder.setMessage((String) objArr[1]);
                if (((String) objArr[2]).startsWith("AUTH_KEY_DROP_")) {
                    builder.setPositiveButton(LocaleController.getString("Cancel", 2131624819), null);
                    builder.setNegativeButton(LocaleController.getString("LogOut", 2131626498), new LaunchActivity$$ExternalSyntheticLambda9(this));
                } else {
                    builder.setPositiveButton(LocaleController.getString("OK", 2131627075), null);
                }
            } else if (num.intValue() == 3) {
                builder.setTitle(LocaleController.getString("Proxy", 2131627749));
                builder.setMessage(LocaleController.getString("UseProxyTelegramError", 2131628794));
                builder.setPositiveButton(LocaleController.getString("OK", 2131627075), null);
                this.proxyErrorDialog = showAlertDialog(builder);
                return;
            }
            if (mainFragmentsStack.isEmpty()) {
                return;
            }
            ArrayList<BaseFragment> arrayList = mainFragmentsStack;
            arrayList.get(arrayList.size() - 1).showDialog(builder.create());
        } else if (i == NotificationCenter.wasUnableToFindCurrentLocation) {
            AlertDialog.Builder builder2 = new AlertDialog.Builder(this);
            builder2.setTitle(LocaleController.getString("AppName", 2131624375));
            builder2.setPositiveButton(LocaleController.getString("OK", 2131627075), null);
            builder2.setNegativeButton(LocaleController.getString("ShareYouLocationUnableManually", 2131628292), new LaunchActivity$$ExternalSyntheticLambda12(this, (HashMap) objArr[0], i2));
            builder2.setMessage(LocaleController.getString("ShareYouLocationUnable", 2131628291));
            if (mainFragmentsStack.isEmpty()) {
                return;
            }
            ArrayList<BaseFragment> arrayList2 = mainFragmentsStack;
            arrayList2.get(arrayList2.size() - 1).showDialog(builder2.create());
        } else if (i == NotificationCenter.didSetNewWallpapper) {
            RecyclerListView recyclerListView = this.sideMenu;
            if (recyclerListView != null && (childAt = recyclerListView.getChildAt(0)) != null) {
                childAt.invalidate();
            }
            SizeNotifierFrameLayout sizeNotifierFrameLayout = this.backgroundTablet;
            if (sizeNotifierFrameLayout == null) {
                return;
            }
            sizeNotifierFrameLayout.setBackgroundImage(Theme.getCachedWallpaper(), Theme.isWallpaperMotion());
        } else if (i == NotificationCenter.didSetPasscode) {
            if (SharedConfig.passcodeHash.length() > 0 && !SharedConfig.allowScreenCapture) {
                try {
                    getWindow().setFlags(8192, 8192);
                } catch (Exception e) {
                    FileLog.e(e);
                }
            } else if (!AndroidUtilities.hasFlagSecureFragment()) {
                try {
                    getWindow().clearFlags(8192);
                } catch (Exception e2) {
                    FileLog.e(e2);
                }
            }
        } else if (i == NotificationCenter.reloadInterface) {
            if (mainFragmentsStack.size() > 1) {
                ArrayList<BaseFragment> arrayList3 = mainFragmentsStack;
                if (arrayList3.get(arrayList3.size() - 1) instanceof ProfileActivity) {
                    z2 = true;
                    if (z2) {
                        ArrayList<BaseFragment> arrayList4 = mainFragmentsStack;
                    }
                    z4 = z2;
                    rebuildAllFragments(z4);
                }
            }
            z2 = false;
            if (z2) {
            }
            z4 = z2;
            rebuildAllFragments(z4);
        } else if (i == NotificationCenter.suggestedLangpack) {
            showLanguageAlert(false);
        } else if (i == NotificationCenter.openArticle) {
            if (mainFragmentsStack.isEmpty()) {
                return;
            }
            ArticleViewer articleViewer = ArticleViewer.getInstance();
            ArrayList<BaseFragment> arrayList5 = mainFragmentsStack;
            articleViewer.setParentActivity(this, arrayList5.get(arrayList5.size() - 1));
            ArticleViewer.getInstance().open((TLRPC$TL_webPage) objArr[0], (String) objArr[1]);
        } else if (i == NotificationCenter.hasNewContactsToImport) {
            ActionBarLayout actionBarLayout = this.actionBarLayout;
            if (actionBarLayout == null || actionBarLayout.fragmentsStack.isEmpty()) {
                return;
            }
            ((Integer) objArr[0]).intValue();
            HashMap hashMap = (HashMap) objArr[1];
            boolean booleanValue = ((Boolean) objArr[2]).booleanValue();
            boolean booleanValue2 = ((Boolean) objArr[3]).booleanValue();
            ArrayList<BaseFragment> arrayList6 = this.actionBarLayout.fragmentsStack;
            AlertDialog.Builder builder3 = new AlertDialog.Builder(this);
            builder3.setTopAnimation(2131558494, 72, false, Theme.getColor("dialogTopBackground"));
            builder3.setTitle(LocaleController.getString("UpdateContactsTitle", 2131628760));
            builder3.setMessage(LocaleController.getString("UpdateContactsMessage", 2131628759));
            builder3.setPositiveButton(LocaleController.getString("OK", 2131627075), new LaunchActivity$$ExternalSyntheticLambda7(i2, hashMap, booleanValue, booleanValue2));
            builder3.setNegativeButton(LocaleController.getString("Cancel", 2131624819), new LaunchActivity$$ExternalSyntheticLambda6(i2, hashMap, booleanValue, booleanValue2));
            builder3.setOnBackButtonListener(new LaunchActivity$$ExternalSyntheticLambda5(i2, hashMap, booleanValue, booleanValue2));
            AlertDialog create = builder3.create();
            arrayList6.get(arrayList6.size() - 1).showDialog(create);
            create.setCanceledOnTouchOutside(false);
        } else if (i == NotificationCenter.didSetNewTheme) {
            if (!((Boolean) objArr[0]).booleanValue()) {
                RecyclerListView recyclerListView2 = this.sideMenu;
                if (recyclerListView2 != null) {
                    recyclerListView2.setBackgroundColor(Theme.getColor("chats_menuBackground"));
                    this.sideMenu.setGlowColor(Theme.getColor("chats_menuBackground"));
                    this.sideMenu.setListSelectorColor(Theme.getColor("listSelectorSDK21"));
                    this.sideMenu.getAdapter().notifyDataSetChanged();
                }
                if (Build.VERSION.SDK_INT >= 21) {
                    try {
                        setTaskDescription(new ActivityManager.TaskDescription((String) null, (Bitmap) null, Theme.getColor("actionBarDefault") | (-16777216)));
                    } catch (Exception unused) {
                    }
                }
            }
            this.drawerLayoutContainer.setBehindKeyboardColor(Theme.getColor("windowBackgroundWhite"));
            boolean booleanValue3 = objArr.length > 1 ? ((Boolean) objArr[1]).booleanValue() : true;
            boolean z7 = objArr.length > 2 && ((Boolean) objArr[2]).booleanValue();
            if (booleanValue3 && !this.isNavigationBarColorFrozen && !this.actionBarLayout.isTransitionAnimationInProgress()) {
                z5 = true;
            }
            checkSystemBarColors(z7, true, z5);
        } else if (i == NotificationCenter.needSetDayNightTheme) {
            if (Build.VERSION.SDK_INT >= 21 && objArr[2] != null) {
                if (this.themeSwitchImageView.getVisibility() == 0) {
                    return;
                }
                try {
                    int[] iArr = (int[]) objArr[2];
                    boolean booleanValue4 = ((Boolean) objArr[4]).booleanValue();
                    RLottieImageView rLottieImageView = (RLottieImageView) objArr[5];
                    int measuredWidth = this.drawerLayoutContainer.getMeasuredWidth();
                    int measuredHeight = this.drawerLayoutContainer.getMeasuredHeight();
                    if (!booleanValue4) {
                        rLottieImageView.setVisibility(4);
                    }
                    this.rippleAbove = null;
                    if (objArr.length > 6) {
                        this.rippleAbove = (View) objArr[6];
                    }
                    this.isNavigationBarColorFrozen = true;
                    invalidateCachedViews(this.drawerLayoutContainer);
                    View view = this.rippleAbove;
                    if (view != null && view.getBackground() != null) {
                        this.rippleAbove.getBackground().setAlpha(0);
                    }
                    Bitmap snapshotView = AndroidUtilities.snapshotView(this.drawerLayoutContainer);
                    View view2 = this.rippleAbove;
                    if (view2 != null && view2.getBackground() != null) {
                        this.rippleAbove.getBackground().setAlpha(255);
                    }
                    this.frameLayout.removeView(this.themeSwitchImageView);
                    if (booleanValue4) {
                        this.frameLayout.addView(this.themeSwitchImageView, 0, LayoutHelper.createFrame(-1, -1.0f));
                        this.themeSwitchSunView.setVisibility(8);
                    } else {
                        this.frameLayout.addView(this.themeSwitchImageView, 1, LayoutHelper.createFrame(-1, -1.0f));
                        this.themeSwitchSunView.setTranslationX(iArr[0] - AndroidUtilities.dp(14.0f));
                        this.themeSwitchSunView.setTranslationY(iArr[1] - AndroidUtilities.dp(14.0f));
                        this.themeSwitchSunView.setVisibility(0);
                        this.themeSwitchSunView.invalidate();
                    }
                    this.themeSwitchImageView.setImageBitmap(snapshotView);
                    this.themeSwitchImageView.setVisibility(0);
                    this.themeSwitchSunDrawable = rLottieImageView.getAnimatedDrawable();
                    float max = Math.max((float) Math.max(Math.sqrt(((measuredWidth - iArr[0]) * (measuredWidth - iArr[0])) + ((measuredHeight - iArr[1]) * (measuredHeight - iArr[1]))), Math.sqrt((iArr[0] * iArr[0]) + ((measuredHeight - iArr[1]) * (measuredHeight - iArr[1])))), (float) Math.max(Math.sqrt(((measuredWidth - iArr[0]) * (measuredWidth - iArr[0])) + (iArr[1] * iArr[1])), Math.sqrt((iArr[0] * iArr[0]) + (iArr[1] * iArr[1]))));
                    View view3 = booleanValue4 ? this.drawerLayoutContainer : this.themeSwitchImageView;
                    int i5 = iArr[0];
                    int i6 = iArr[1];
                    float f = booleanValue4 ? 0.0f : max;
                    if (!booleanValue4) {
                        max = 0.0f;
                    }
                    Animator createCircularReveal = ViewAnimationUtils.createCircularReveal(view3, i5, i6, f, max);
                    createCircularReveal.setDuration(400L);
                    createCircularReveal.setInterpolator(Easings.easeInOutQuad);
                    createCircularReveal.addListener(new AnonymousClass18(booleanValue4, rLottieImageView));
                    if (this.rippleAbove != null) {
                        ValueAnimator ofFloat = ValueAnimator.ofFloat(0.0f, 1.0f);
                        ofFloat.addUpdateListener(new LaunchActivity$$ExternalSyntheticLambda0(this));
                        ofFloat.setDuration(createCircularReveal.getDuration());
                        ofFloat.start();
                    }
                    AndroidUtilities.runOnUIThread(new LaunchActivity$$ExternalSyntheticLambda30(this), booleanValue4 ? (measuredHeight - iArr[1]) / AndroidUtilities.dp(2.25f) : 50L);
                    createCircularReveal.start();
                    z = true;
                } catch (Throwable th) {
                    FileLog.e(th);
                    try {
                        this.themeSwitchImageView.setImageDrawable(null);
                        this.frameLayout.removeView(this.themeSwitchImageView);
                        DrawerProfileCell.switchingTheme = false;
                    } catch (Exception e3) {
                        FileLog.e(e3);
                    }
                }
                Theme.ThemeInfo themeInfo = (Theme.ThemeInfo) objArr[0];
                boolean booleanValue5 = ((Boolean) objArr[1]).booleanValue();
                int intValue = ((Integer) objArr[3]).intValue();
                this.actionBarLayout.animateThemedValues(themeInfo, intValue, booleanValue5, z);
                if (AndroidUtilities.isTablet()) {
                    return;
                }
                this.layersActionBarLayout.animateThemedValues(themeInfo, intValue, booleanValue5, z);
                this.rightActionBarLayout.animateThemedValues(themeInfo, intValue, booleanValue5, z);
                return;
            }
            DrawerProfileCell.switchingTheme = false;
            z = false;
            Theme.ThemeInfo themeInfo2 = (Theme.ThemeInfo) objArr[0];
            boolean booleanValue52 = ((Boolean) objArr[1]).booleanValue();
            int intValue2 = ((Integer) objArr[3]).intValue();
            this.actionBarLayout.animateThemedValues(themeInfo2, intValue2, booleanValue52, z);
            if (AndroidUtilities.isTablet()) {
            }
        } else if (i == NotificationCenter.notificationsCountUpdated) {
            RecyclerListView recyclerListView3 = this.sideMenu;
            if (recyclerListView3 == null) {
                return;
            }
            Integer num2 = (Integer) objArr[0];
            int childCount = recyclerListView3.getChildCount();
            for (int i7 = 0; i7 < childCount; i7++) {
                View childAt2 = this.sideMenu.getChildAt(i7);
                if ((childAt2 instanceof DrawerUserCell) && ((DrawerUserCell) childAt2).getAccountNumber() == num2.intValue()) {
                    childAt2.invalidate();
                    return;
                }
            }
        } else if (i == NotificationCenter.needShowPlayServicesAlert) {
            try {
                ((Status) objArr[0]).startResolutionForResult(this, 140);
            } catch (Throwable unused2) {
            }
        } else if (i == NotificationCenter.fileLoaded) {
            String str3 = (String) objArr[0];
            if (SharedConfig.isAppUpdateAvailable() && FileLoader.getAttachFileName(SharedConfig.pendingAppUpdate.document).equals(str3)) {
                updateAppUpdateViews(true);
            }
            String str4 = this.loadingThemeFileName;
            if (str4 != null) {
                if (!str4.equals(str3)) {
                    return;
                }
                this.loadingThemeFileName = null;
                File filesDirFixed = ApplicationLoader.getFilesDirFixed();
                File file = new File(filesDirFixed, "remote" + this.loadingTheme.id + ".attheme");
                TLRPC$TL_theme tLRPC$TL_theme = this.loadingTheme;
                Theme.ThemeInfo fillThemeValues = Theme.fillThemeValues(file, tLRPC$TL_theme.title, tLRPC$TL_theme);
                if (fillThemeValues != null) {
                    if (fillThemeValues.pathToWallpaper != null && !new File(fillThemeValues.pathToWallpaper).exists()) {
                        TLRPC$TL_account_getWallPaper tLRPC$TL_account_getWallPaper = new TLRPC$TL_account_getWallPaper();
                        TLRPC$TL_inputWallPaperSlug tLRPC$TL_inputWallPaperSlug = new TLRPC$TL_inputWallPaperSlug();
                        tLRPC$TL_inputWallPaperSlug.slug = fillThemeValues.slug;
                        tLRPC$TL_account_getWallPaper.wallpaper = tLRPC$TL_inputWallPaperSlug;
                        ConnectionsManager.getInstance(fillThemeValues.account).sendRequest(tLRPC$TL_account_getWallPaper, new LaunchActivity$$ExternalSyntheticLambda85(this, fillThemeValues));
                        return;
                    }
                    TLRPC$TL_theme tLRPC$TL_theme2 = this.loadingTheme;
                    Theme.ThemeInfo applyThemeFile = Theme.applyThemeFile(file, tLRPC$TL_theme2.title, tLRPC$TL_theme2, true);
                    if (applyThemeFile != null) {
                        lambda$runLinkRequest$59(new ThemePreviewActivity(applyThemeFile, true, 0, false, false));
                    }
                }
                onThemeLoadFinish();
                return;
            }
            String str5 = this.loadingThemeWallpaperName;
            if (str5 == null || !str5.equals(str3)) {
                return;
            }
            this.loadingThemeWallpaperName = null;
            File file2 = (File) objArr[1];
            if (this.loadingThemeAccent) {
                openThemeAccentPreview(this.loadingTheme, this.loadingThemeWallpaper, this.loadingThemeInfo);
                onThemeLoadFinish();
                return;
            }
            Utilities.globalQueue.postRunnable(new LaunchActivity$$ExternalSyntheticLambda61(this, this.loadingThemeInfo, file2));
        } else if (i == NotificationCenter.fileLoadFailed) {
            String str6 = (String) objArr[0];
            if (str6.equals(this.loadingThemeFileName) || str6.equals(this.loadingThemeWallpaperName)) {
                onThemeLoadFinish();
            }
            if (!SharedConfig.isAppUpdateAvailable() || !FileLoader.getAttachFileName(SharedConfig.pendingAppUpdate.document).equals(str6)) {
                return;
            }
            updateAppUpdateViews(true);
        } else if (i == NotificationCenter.screenStateChanged) {
            if (ApplicationLoader.mainInterfacePaused) {
                return;
            }
            if (ApplicationLoader.isScreenOn) {
                onPasscodeResume();
            } else {
                onPasscodePause();
            }
        } else if (i == NotificationCenter.needCheckSystemBarColors) {
            if (objArr.length > 0 && ((Boolean) objArr[0]).booleanValue()) {
                z6 = true;
            }
            checkSystemBarColors(z6);
        } else if (i == NotificationCenter.historyImportProgressChanged) {
            if (objArr.length <= 1 || mainFragmentsStack.isEmpty()) {
                return;
            }
            ArrayList<BaseFragment> arrayList7 = mainFragmentsStack;
            AlertsCreator.processError(this.currentAccount, (TLRPC$TL_error) objArr[2], arrayList7.get(arrayList7.size() - 1), (TLObject) objArr[1], new Object[0]);
        } else if (i == NotificationCenter.stickersImportComplete) {
            MediaDataController mediaDataController = MediaDataController.getInstance(i2);
            TLObject tLObject = (TLObject) objArr[0];
            if (!mainFragmentsStack.isEmpty()) {
                ArrayList<BaseFragment> arrayList8 = mainFragmentsStack;
                baseFragment2 = arrayList8.get(arrayList8.size() - 1);
            } else {
                baseFragment2 = null;
            }
            mediaDataController.toggleStickerSet(this, tLObject, 2, baseFragment2, false, true);
        } else if (i == NotificationCenter.newSuggestionsAvailable) {
            this.sideMenu.invalidateViews();
        } else if (i == NotificationCenter.showBulletin) {
            if (mainFragmentsStack.isEmpty()) {
                return;
            }
            int intValue3 = ((Integer) objArr[0]).intValue();
            FrameLayout container = (!GroupCallActivity.groupCallUiVisible || (groupCallActivity = GroupCallActivity.groupCallInstance) == null) ? null : groupCallActivity.getContainer();
            if (container == null) {
                ArrayList<BaseFragment> arrayList9 = mainFragmentsStack;
                baseFragment = arrayList9.get(arrayList9.size() - 1);
            } else {
                baseFragment = null;
            }
            if (intValue3 == 0) {
                TLRPC$Document tLRPC$Document = (TLRPC$Document) objArr[1];
                int intValue4 = ((Integer) objArr[2]).intValue();
                StickerSetBulletinLayout stickerSetBulletinLayout = new StickerSetBulletinLayout(this, null, intValue4, tLRPC$Document, null);
                int i8 = (intValue4 == 6 || intValue4 == 7) ? 3500 : 1500;
                if (baseFragment != null) {
                    Bulletin.make(baseFragment, stickerSetBulletinLayout, i8).show();
                } else {
                    Bulletin.make(container, stickerSetBulletinLayout, i8).show();
                }
            } else if (intValue3 == 1) {
                if (baseFragment != null) {
                    BulletinFactory.of(baseFragment).createErrorBulletin((String) objArr[1]).show();
                } else {
                    BulletinFactory.of(container, null).createErrorBulletin((String) objArr[1]).show();
                }
            } else if (intValue3 == 2) {
                if (((Long) objArr[1]).longValue() > 0) {
                    i3 = 2131629274;
                    str = "YourBioChanged";
                } else {
                    i3 = 2131624901;
                    str = "CannelDescriptionChanged";
                }
                (container != null ? BulletinFactory.of(container, null) : BulletinFactory.of(baseFragment)).createErrorBulletin(LocaleController.getString(str, i3)).show();
            } else if (intValue3 == 3) {
                if (((Long) objArr[1]).longValue() > 0) {
                    i4 = 2131629291;
                    str2 = "YourNameChanged";
                } else {
                    i4 = 2131624969;
                    str2 = "CannelTitleChanged";
                }
                (container != null ? BulletinFactory.of(container, null) : BulletinFactory.of(baseFragment)).createErrorBulletin(LocaleController.getString(str2, i4)).show();
            } else if (intValue3 == 4) {
                if (baseFragment != null) {
                    BulletinFactory.of(baseFragment).createErrorBulletinSubtitle((String) objArr[1], (String) objArr[2], baseFragment.getResourceProvider()).show();
                } else {
                    BulletinFactory.of(container, null).createErrorBulletinSubtitle((String) objArr[1], (String) objArr[2], null).show();
                }
            } else if (intValue3 == 5) {
                AppIconBulletinLayout appIconBulletinLayout = new AppIconBulletinLayout(this, (LauncherIconController.LauncherIcon) objArr[1], null);
                if (baseFragment != null) {
                    Bulletin.make(baseFragment, appIconBulletinLayout, 1500).show();
                } else {
                    Bulletin.make(container, appIconBulletinLayout, 1500).show();
                }
            }
        } else if (i == NotificationCenter.groupCallUpdated) {
            checkWasMutedByAdmin(false);
        } else if (i == NotificationCenter.fileLoadProgressChanged) {
            if (this.updateTextView == null || !SharedConfig.isAppUpdateAvailable()) {
                return;
            }
            String str7 = (String) objArr[0];
            String attachFileName = FileLoader.getAttachFileName(SharedConfig.pendingAppUpdate.document);
            if (attachFileName == null || !attachFileName.equals(str7)) {
                return;
            }
            float longValue = ((float) ((Long) objArr[1]).longValue()) / ((float) ((Long) objArr[2]).longValue());
            this.updateLayoutIcon.setProgress(longValue, true);
            this.updateTextView.setText(LocaleController.formatString("AppUpdateDownloading", 2131624380, Integer.valueOf((int) (longValue * 100.0f))));
        } else if (i == NotificationCenter.appUpdateAvailable) {
            if (mainFragmentsStack.size() == 1) {
                z3 = true;
            }
            updateAppUpdateViews(z3);
        } else if (i == NotificationCenter.currentUserShowLimitReachedDialog && !mainFragmentsStack.isEmpty()) {
            ArrayList<BaseFragment> arrayList10 = mainFragmentsStack;
            BaseFragment baseFragment3 = arrayList10.get(arrayList10.size() - 1);
            if (baseFragment3.getParentActivity() == null) {
                return;
            }
            baseFragment3.showDialog(new LimitReachedBottomSheet(baseFragment3, baseFragment3.getParentActivity(), ((Integer) objArr[0]).intValue(), this.currentAccount));
        }
    }

    public static /* synthetic */ void lambda$didReceivedNotification$81(int i, DialogInterface dialogInterface, int i2) {
        if (!mainFragmentsStack.isEmpty()) {
            MessagesController messagesController = MessagesController.getInstance(i);
            ArrayList<BaseFragment> arrayList = mainFragmentsStack;
            messagesController.openByUserName("spambot", arrayList.get(arrayList.size() - 1), 1);
        }
    }

    public /* synthetic */ void lambda$didReceivedNotification$82(DialogInterface dialogInterface, int i) {
        MessagesController.getInstance(this.currentAccount).performLogout(2);
    }

    public /* synthetic */ void lambda$didReceivedNotification$84(HashMap hashMap, int i, DialogInterface dialogInterface, int i2) {
        if (mainFragmentsStack.isEmpty()) {
            return;
        }
        ArrayList<BaseFragment> arrayList = mainFragmentsStack;
        if (!AndroidUtilities.isGoogleMapsInstalled(arrayList.get(arrayList.size() - 1))) {
            return;
        }
        LocationActivity locationActivity = new LocationActivity(0);
        locationActivity.setDelegate(new LaunchActivity$$ExternalSyntheticLambda102(hashMap, i));
        lambda$runLinkRequest$59(locationActivity);
    }

    public static /* synthetic */ void lambda$didReceivedNotification$83(HashMap hashMap, int i, TLRPC$MessageMedia tLRPC$MessageMedia, int i2, boolean z, int i3) {
        for (Map.Entry entry : hashMap.entrySet()) {
            MessageObject messageObject = (MessageObject) entry.getValue();
            SendMessagesHelper.getInstance(i).sendMessage(tLRPC$MessageMedia, messageObject.getDialogId(), messageObject, (MessageObject) null, (TLRPC$ReplyMarkup) null, (HashMap<String, String>) null, z, i3);
        }
    }

    public static /* synthetic */ void lambda$didReceivedNotification$85(int i, HashMap hashMap, boolean z, boolean z2, DialogInterface dialogInterface, int i2) {
        ContactsController.getInstance(i).syncPhoneBookByAlert(hashMap, z, z2, false);
    }

    public static /* synthetic */ void lambda$didReceivedNotification$86(int i, HashMap hashMap, boolean z, boolean z2, DialogInterface dialogInterface, int i2) {
        ContactsController.getInstance(i).syncPhoneBookByAlert(hashMap, z, z2, true);
    }

    public static /* synthetic */ void lambda$didReceivedNotification$87(int i, HashMap hashMap, boolean z, boolean z2, DialogInterface dialogInterface, int i2) {
        ContactsController.getInstance(i).syncPhoneBookByAlert(hashMap, z, z2, true);
    }

    /* renamed from: org.telegram.ui.LaunchActivity$18 */
    /* loaded from: classes3.dex */
    class AnonymousClass18 extends AnimatorListenerAdapter {
        final /* synthetic */ RLottieImageView val$darkThemeView;
        final /* synthetic */ boolean val$toDark;

        AnonymousClass18(boolean z, RLottieImageView rLottieImageView) {
            LaunchActivity.this = r1;
            this.val$toDark = z;
            this.val$darkThemeView = rLottieImageView;
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator) {
            LaunchActivity.this.rippleAbove = null;
            LaunchActivity.this.drawerLayoutContainer.invalidate();
            LaunchActivity.this.themeSwitchImageView.invalidate();
            LaunchActivity.this.themeSwitchImageView.setImageDrawable(null);
            LaunchActivity.this.themeSwitchImageView.setVisibility(8);
            LaunchActivity.this.themeSwitchSunView.setVisibility(8);
            NotificationCenter.getGlobalInstance().postNotificationName(NotificationCenter.themeAccentListUpdated, new Object[0]);
            if (!this.val$toDark) {
                this.val$darkThemeView.setVisibility(0);
            }
            DrawerProfileCell.switchingTheme = false;
        }
    }

    public /* synthetic */ void lambda$didReceivedNotification$88(ValueAnimator valueAnimator) {
        this.frameLayout.invalidate();
    }

    public /* synthetic */ void lambda$didReceivedNotification$89() {
        if (this.isNavigationBarColorFrozen) {
            this.isNavigationBarColorFrozen = false;
            checkSystemBarColors(false, true);
        }
    }

    public /* synthetic */ void lambda$didReceivedNotification$91(Theme.ThemeInfo themeInfo, TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        AndroidUtilities.runOnUIThread(new LaunchActivity$$ExternalSyntheticLambda47(this, tLObject, themeInfo));
    }

    public /* synthetic */ void lambda$didReceivedNotification$90(TLObject tLObject, Theme.ThemeInfo themeInfo) {
        if (tLObject instanceof TLRPC$TL_wallPaper) {
            TLRPC$TL_wallPaper tLRPC$TL_wallPaper = (TLRPC$TL_wallPaper) tLObject;
            this.loadingThemeInfo = themeInfo;
            this.loadingThemeWallpaperName = FileLoader.getAttachFileName(tLRPC$TL_wallPaper.document);
            this.loadingThemeWallpaper = tLRPC$TL_wallPaper;
            FileLoader.getInstance(themeInfo.account).loadFile(tLRPC$TL_wallPaper.document, tLRPC$TL_wallPaper, 1, 1);
            return;
        }
        onThemeLoadFinish();
    }

    public /* synthetic */ void lambda$didReceivedNotification$93(Theme.ThemeInfo themeInfo, File file) {
        themeInfo.createBackground(file, themeInfo.pathToWallpaper);
        AndroidUtilities.runOnUIThread(new LaunchActivity$$ExternalSyntheticLambda27(this));
    }

    public /* synthetic */ void lambda$didReceivedNotification$92() {
        if (this.loadingTheme == null) {
            return;
        }
        File filesDirFixed = ApplicationLoader.getFilesDirFixed();
        File file = new File(filesDirFixed, "remote" + this.loadingTheme.id + ".attheme");
        TLRPC$TL_theme tLRPC$TL_theme = this.loadingTheme;
        Theme.ThemeInfo applyThemeFile = Theme.applyThemeFile(file, tLRPC$TL_theme.title, tLRPC$TL_theme, true);
        if (applyThemeFile != null) {
            lambda$runLinkRequest$59(new ThemePreviewActivity(applyThemeFile, true, 0, false, false));
        }
        onThemeLoadFinish();
    }

    private void invalidateCachedViews(View view) {
        if (view.getLayerType() != 0) {
            view.invalidate();
        }
        if (view instanceof ViewGroup) {
            ViewGroup viewGroup = (ViewGroup) view;
            for (int i = 0; i < viewGroup.getChildCount(); i++) {
                invalidateCachedViews(viewGroup.getChildAt(i));
            }
        }
    }

    private void checkWasMutedByAdmin(boolean z) {
        ChatObject.Call call;
        long j;
        VoIPService sharedInstance = VoIPService.getSharedInstance();
        boolean z2 = false;
        if (sharedInstance != null && (call = sharedInstance.groupCall) != null) {
            boolean z3 = this.wasMutedByAdminRaisedHand;
            TLRPC$InputPeer groupCallPeer = sharedInstance.getGroupCallPeer();
            if (groupCallPeer != null) {
                j = groupCallPeer.user_id;
                if (j == 0) {
                    long j2 = groupCallPeer.chat_id;
                    if (j2 == 0) {
                        j2 = groupCallPeer.channel_id;
                    }
                    j = -j2;
                }
            } else {
                j = UserConfig.getInstance(this.currentAccount).clientUserId;
            }
            TLRPC$TL_groupCallParticipant tLRPC$TL_groupCallParticipant = call.participants.get(j);
            boolean z4 = tLRPC$TL_groupCallParticipant != null && !tLRPC$TL_groupCallParticipant.can_self_unmute && tLRPC$TL_groupCallParticipant.muted;
            if (z4 && tLRPC$TL_groupCallParticipant.raise_hand_rating != 0) {
                z2 = true;
            }
            this.wasMutedByAdminRaisedHand = z2;
            if (z || !z3 || z2 || z4 || GroupCallActivity.groupCallInstance != null) {
                return;
            }
            showVoiceChatTooltip(38);
            return;
        }
        this.wasMutedByAdminRaisedHand = false;
    }

    private void showVoiceChatTooltip(int i) {
        VoIPService sharedInstance = VoIPService.getSharedInstance();
        if (sharedInstance == null || mainFragmentsStack.isEmpty() || sharedInstance.groupCall == null || mainFragmentsStack.isEmpty()) {
            return;
        }
        TLRPC$Chat chat = sharedInstance.getChat();
        ArrayList<BaseFragment> arrayList = this.actionBarLayout.fragmentsStack;
        BaseFragment baseFragment = arrayList.get(arrayList.size() - 1);
        if (baseFragment instanceof ChatActivity) {
            ChatActivity chatActivity = (ChatActivity) baseFragment;
            if (chatActivity.getDialogId() == (-chat.id)) {
                chat = null;
            }
            chatActivity.getUndoView().showWithAction(0L, i, chat);
        } else if (baseFragment instanceof DialogsActivity) {
            ((DialogsActivity) baseFragment).getUndoView().showWithAction(0L, i, chat);
        } else if (baseFragment instanceof ProfileActivity) {
            ((ProfileActivity) baseFragment).getUndoView().showWithAction(0L, i, chat);
        }
        if (i != 38 || VoIPService.getSharedInstance() == null) {
            return;
        }
        VoIPService.getSharedInstance().playAllowTalkSound();
    }

    private String getStringForLanguageAlert(HashMap<String, String> hashMap, String str, int i) {
        String str2 = hashMap.get(str);
        return str2 == null ? LocaleController.getString(str, i) : str2;
    }

    private void openThemeAccentPreview(TLRPC$TL_theme tLRPC$TL_theme, TLRPC$TL_wallPaper tLRPC$TL_wallPaper, Theme.ThemeInfo themeInfo) {
        int i = themeInfo.lastAccentId;
        Theme.ThemeAccent createNewAccent = themeInfo.createNewAccent(tLRPC$TL_theme, this.currentAccount);
        themeInfo.prevAccentId = themeInfo.currentAccentId;
        themeInfo.setCurrentAccentId(createNewAccent.id);
        createNewAccent.pattern = tLRPC$TL_wallPaper;
        lambda$runLinkRequest$59(new ThemePreviewActivity(themeInfo, i != themeInfo.lastAccentId, 0, false, false));
    }

    private void onThemeLoadFinish() {
        AlertDialog alertDialog = this.loadingThemeProgressDialog;
        if (alertDialog != null) {
            try {
                alertDialog.dismiss();
            } finally {
                this.loadingThemeProgressDialog = null;
            }
        }
        this.loadingThemeWallpaperName = null;
        this.loadingThemeWallpaper = null;
        this.loadingThemeInfo = null;
        this.loadingThemeFileName = null;
        this.loadingTheme = null;
    }

    private void checkFreeDiscSpace() {
        SharedConfig.checkKeepMedia();
        SharedConfig.checkLogsToDelete();
        if (Build.VERSION.SDK_INT >= 26) {
            return;
        }
        Utilities.globalQueue.postRunnable(new LaunchActivity$$ExternalSyntheticLambda28(this), 2000L);
    }

    public /* synthetic */ void lambda$checkFreeDiscSpace$95() {
        File directory;
        long j;
        if (!UserConfig.getInstance(this.currentAccount).isClientActivated()) {
            return;
        }
        try {
            SharedPreferences globalMainSettings = MessagesController.getGlobalMainSettings();
            if (Math.abs(globalMainSettings.getLong("last_space_check", 0L) - System.currentTimeMillis()) < 259200000 || (directory = FileLoader.getDirectory(4)) == null) {
                return;
            }
            StatFs statFs = new StatFs(directory.getAbsolutePath());
            if (Build.VERSION.SDK_INT < 18) {
                j = Math.abs(statFs.getAvailableBlocks() * statFs.getBlockSize());
            } else {
                j = statFs.getBlockSizeLong() * statFs.getAvailableBlocksLong();
            }
            if (j >= 104857600) {
                return;
            }
            globalMainSettings.edit().putLong("last_space_check", System.currentTimeMillis()).commit();
            AndroidUtilities.runOnUIThread(new LaunchActivity$$ExternalSyntheticLambda26(this));
        } catch (Throwable unused) {
        }
    }

    public /* synthetic */ void lambda$checkFreeDiscSpace$94() {
        try {
            AlertsCreator.createFreeSpaceDialog(this).show();
        } catch (Throwable unused) {
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:12:0x0054  */
    /* JADX WARN: Removed duplicated region for block: B:13:0x0056  */
    /* JADX WARN: Removed duplicated region for block: B:16:0x005c  */
    /* JADX WARN: Removed duplicated region for block: B:17:0x005f  */
    /* JADX WARN: Removed duplicated region for block: B:20:0x0064  */
    /* JADX WARN: Removed duplicated region for block: B:21:0x0065  */
    /* JADX WARN: Removed duplicated region for block: B:26:0x006e A[Catch: Exception -> 0x0122, TRY_ENTER, TryCatch #0 {Exception -> 0x0122, blocks: (B:3:0x0007, B:5:0x0010, B:10:0x001e, B:14:0x0058, B:18:0x0060, B:22:0x0067, B:26:0x006e, B:30:0x0082, B:34:0x00a2, B:35:0x00c0), top: B:39:0x0007 }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private void showLanguageAlertInternal(LocaleController.LocaleInfo localeInfo, LocaleController.LocaleInfo localeInfo2, String str) {
        boolean z;
        int i;
        try {
            this.loadingLocaleDialog = false;
            LocaleController.LocaleInfo localeInfo3 = localeInfo;
            if (!localeInfo3.builtIn && !LocaleController.getInstance().isCurrentLocalLocale()) {
                z = false;
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle(getStringForLanguageAlert(this.systemLocaleStrings, "ChooseYourLanguage", 2131625126));
                builder.setSubtitle(getStringForLanguageAlert(this.englishLocaleStrings, "ChooseYourLanguage", 2131625126));
                LinearLayout linearLayout = new LinearLayout(this);
                linearLayout.setOrientation(1);
                LanguageCell[] languageCellArr = new LanguageCell[2];
                LocaleController.LocaleInfo[] localeInfoArr = new LocaleController.LocaleInfo[1];
                LocaleController.LocaleInfo[] localeInfoArr2 = new LocaleController.LocaleInfo[2];
                String stringForLanguageAlert = getStringForLanguageAlert(this.systemLocaleStrings, "English", 2131625641);
                localeInfoArr2[0] = !z ? localeInfo3 : localeInfo2;
                localeInfoArr2[1] = !z ? localeInfo2 : localeInfo3;
                if (z) {
                    localeInfo3 = localeInfo2;
                }
                localeInfoArr[0] = localeInfo3;
                i = 0;
                while (i < 2) {
                    languageCellArr[i] = new LanguageCell(this);
                    languageCellArr[i].setLanguage(localeInfoArr2[i], localeInfoArr2[i] == localeInfo2 ? stringForLanguageAlert : null, true);
                    languageCellArr[i].setTag(Integer.valueOf(i));
                    languageCellArr[i].setBackground(Theme.createSelectorDrawable(Theme.getColor("dialogButtonSelector"), 2));
                    languageCellArr[i].setLanguageSelected(i == 0, false);
                    linearLayout.addView(languageCellArr[i], LayoutHelper.createLinear(-1, 50));
                    languageCellArr[i].setOnClickListener(new LaunchActivity$$ExternalSyntheticLambda17(localeInfoArr, languageCellArr));
                    i++;
                }
                LanguageCell languageCell = new LanguageCell(this);
                languageCell.setValue(getStringForLanguageAlert(this.systemLocaleStrings, "ChooseYourLanguageOther", 2131625127), getStringForLanguageAlert(this.englishLocaleStrings, "ChooseYourLanguageOther", 2131625127));
                languageCell.setBackground(Theme.createSelectorDrawable(Theme.getColor("dialogButtonSelector"), 2));
                languageCell.setOnClickListener(new LaunchActivity$$ExternalSyntheticLambda16(this));
                linearLayout.addView(languageCell, LayoutHelper.createLinear(-1, 50));
                builder.setView(linearLayout);
                builder.setNegativeButton(LocaleController.getString("OK", 2131627075), new LaunchActivity$$ExternalSyntheticLambda13(this, localeInfoArr));
                this.localeDialog = showAlertDialog(builder);
                MessagesController.getGlobalMainSettings().edit().putString("language_showed2", str).commit();
            }
            z = true;
            AlertDialog.Builder builder2 = new AlertDialog.Builder(this);
            builder2.setTitle(getStringForLanguageAlert(this.systemLocaleStrings, "ChooseYourLanguage", 2131625126));
            builder2.setSubtitle(getStringForLanguageAlert(this.englishLocaleStrings, "ChooseYourLanguage", 2131625126));
            LinearLayout linearLayout2 = new LinearLayout(this);
            linearLayout2.setOrientation(1);
            LanguageCell[] languageCellArr2 = new LanguageCell[2];
            LocaleController.LocaleInfo[] localeInfoArr3 = new LocaleController.LocaleInfo[1];
            LocaleController.LocaleInfo[] localeInfoArr22 = new LocaleController.LocaleInfo[2];
            String stringForLanguageAlert2 = getStringForLanguageAlert(this.systemLocaleStrings, "English", 2131625641);
            localeInfoArr22[0] = !z ? localeInfo3 : localeInfo2;
            localeInfoArr22[1] = !z ? localeInfo2 : localeInfo3;
            if (z) {
            }
            localeInfoArr3[0] = localeInfo3;
            i = 0;
            while (i < 2) {
            }
            LanguageCell languageCell2 = new LanguageCell(this);
            languageCell2.setValue(getStringForLanguageAlert(this.systemLocaleStrings, "ChooseYourLanguageOther", 2131625127), getStringForLanguageAlert(this.englishLocaleStrings, "ChooseYourLanguageOther", 2131625127));
            languageCell2.setBackground(Theme.createSelectorDrawable(Theme.getColor("dialogButtonSelector"), 2));
            languageCell2.setOnClickListener(new LaunchActivity$$ExternalSyntheticLambda16(this));
            linearLayout2.addView(languageCell2, LayoutHelper.createLinear(-1, 50));
            builder2.setView(linearLayout2);
            builder2.setNegativeButton(LocaleController.getString("OK", 2131627075), new LaunchActivity$$ExternalSyntheticLambda13(this, localeInfoArr3));
            this.localeDialog = showAlertDialog(builder2);
            MessagesController.getGlobalMainSettings().edit().putString("language_showed2", str).commit();
        } catch (Exception e) {
            FileLog.e(e);
        }
    }

    public static /* synthetic */ void lambda$showLanguageAlertInternal$96(LocaleController.LocaleInfo[] localeInfoArr, LanguageCell[] languageCellArr, View view) {
        Integer num = (Integer) view.getTag();
        localeInfoArr[0] = ((LanguageCell) view).getCurrentLocale();
        int i = 0;
        while (i < languageCellArr.length) {
            languageCellArr[i].setLanguageSelected(i == num.intValue(), true);
            i++;
        }
    }

    public /* synthetic */ void lambda$showLanguageAlertInternal$97(View view) {
        this.localeDialog = null;
        this.drawerLayoutContainer.closeDrawer(true);
        lambda$runLinkRequest$59(new LanguageSelectActivity());
        AlertDialog alertDialog = this.visibleDialog;
        if (alertDialog != null) {
            alertDialog.dismiss();
            this.visibleDialog = null;
        }
    }

    public /* synthetic */ void lambda$showLanguageAlertInternal$98(LocaleController.LocaleInfo[] localeInfoArr, DialogInterface dialogInterface, int i) {
        LocaleController.getInstance().applyLanguage(localeInfoArr[0], true, false, this.currentAccount);
        rebuildAllFragments(true);
    }

    public void drawRippleAbove(Canvas canvas, View view) {
        View view2;
        if (view == null || (view2 = this.rippleAbove) == null || view2.getBackground() == null) {
            return;
        }
        if (this.tempLocation == null) {
            this.tempLocation = new int[2];
        }
        this.rippleAbove.getLocationInWindow(this.tempLocation);
        int[] iArr = this.tempLocation;
        int i = iArr[0];
        int i2 = iArr[1];
        view.getLocationInWindow(iArr);
        int[] iArr2 = this.tempLocation;
        int i3 = i2 - iArr2[1];
        canvas.save();
        canvas.translate(i - iArr2[0], i3);
        this.rippleAbove.getBackground().draw(canvas);
        canvas.restore();
    }

    private void showLanguageAlert(boolean z) {
        String str;
        if (!UserConfig.getInstance(this.currentAccount).isClientActivated()) {
            return;
        }
        try {
            if (!this.loadingLocaleDialog && !ApplicationLoader.mainInterfacePaused) {
                String string = MessagesController.getGlobalMainSettings().getString("language_showed2", "");
                String str2 = MessagesController.getInstance(this.currentAccount).suggestedLangCode;
                if (!z && string.equals(str2)) {
                    if (!BuildVars.LOGS_ENABLED) {
                        return;
                    }
                    FileLog.d("alert already showed for " + string);
                    return;
                }
                LocaleController.LocaleInfo[] localeInfoArr = new LocaleController.LocaleInfo[2];
                String str3 = str2.contains("-") ? str2.split("-")[0] : str2;
                if ("in".equals(str3)) {
                    str = "id";
                } else if ("iw".equals(str3)) {
                    str = "he";
                } else {
                    str = "jw".equals(str3) ? "jv" : null;
                }
                for (int i = 0; i < LocaleController.getInstance().languages.size(); i++) {
                    LocaleController.LocaleInfo localeInfo = LocaleController.getInstance().languages.get(i);
                    if (localeInfo.shortName.equals("en")) {
                        localeInfoArr[0] = localeInfo;
                    }
                    if (localeInfo.shortName.replace("_", "-").equals(str2) || localeInfo.shortName.equals(str3) || localeInfo.shortName.equals(str)) {
                        localeInfoArr[1] = localeInfo;
                    }
                    if (localeInfoArr[0] != null && localeInfoArr[1] != null) {
                        break;
                    }
                }
                if (localeInfoArr[0] != null && localeInfoArr[1] != null && localeInfoArr[0] != localeInfoArr[1]) {
                    if (BuildVars.LOGS_ENABLED) {
                        FileLog.d("show lang alert for " + localeInfoArr[0].getKey() + " and " + localeInfoArr[1].getKey());
                    }
                    this.systemLocaleStrings = null;
                    this.englishLocaleStrings = null;
                    this.loadingLocaleDialog = true;
                    TLRPC$TL_langpack_getStrings tLRPC$TL_langpack_getStrings = new TLRPC$TL_langpack_getStrings();
                    tLRPC$TL_langpack_getStrings.lang_code = localeInfoArr[1].getLangCode();
                    tLRPC$TL_langpack_getStrings.keys.add("English");
                    tLRPC$TL_langpack_getStrings.keys.add("ChooseYourLanguage");
                    tLRPC$TL_langpack_getStrings.keys.add("ChooseYourLanguageOther");
                    tLRPC$TL_langpack_getStrings.keys.add("ChangeLanguageLater");
                    ConnectionsManager.getInstance(this.currentAccount).sendRequest(tLRPC$TL_langpack_getStrings, new LaunchActivity$$ExternalSyntheticLambda89(this, localeInfoArr, str2), 8);
                    TLRPC$TL_langpack_getStrings tLRPC$TL_langpack_getStrings2 = new TLRPC$TL_langpack_getStrings();
                    tLRPC$TL_langpack_getStrings2.lang_code = localeInfoArr[0].getLangCode();
                    tLRPC$TL_langpack_getStrings2.keys.add("English");
                    tLRPC$TL_langpack_getStrings2.keys.add("ChooseYourLanguage");
                    tLRPC$TL_langpack_getStrings2.keys.add("ChooseYourLanguageOther");
                    tLRPC$TL_langpack_getStrings2.keys.add("ChangeLanguageLater");
                    ConnectionsManager.getInstance(this.currentAccount).sendRequest(tLRPC$TL_langpack_getStrings2, new LaunchActivity$$ExternalSyntheticLambda88(this, localeInfoArr, str2), 8);
                }
            }
        } catch (Exception e) {
            FileLog.e(e);
        }
    }

    public /* synthetic */ void lambda$showLanguageAlert$100(LocaleController.LocaleInfo[] localeInfoArr, String str, TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        HashMap hashMap = new HashMap();
        if (tLObject != null) {
            TLRPC$Vector tLRPC$Vector = (TLRPC$Vector) tLObject;
            for (int i = 0; i < tLRPC$Vector.objects.size(); i++) {
                TLRPC$LangPackString tLRPC$LangPackString = (TLRPC$LangPackString) tLRPC$Vector.objects.get(i);
                hashMap.put(tLRPC$LangPackString.key, tLRPC$LangPackString.value);
            }
        }
        AndroidUtilities.runOnUIThread(new LaunchActivity$$ExternalSyntheticLambda40(this, hashMap, localeInfoArr, str));
    }

    public /* synthetic */ void lambda$showLanguageAlert$99(HashMap hashMap, LocaleController.LocaleInfo[] localeInfoArr, String str) {
        this.systemLocaleStrings = hashMap;
        if (this.englishLocaleStrings == null || hashMap == null) {
            return;
        }
        showLanguageAlertInternal(localeInfoArr[1], localeInfoArr[0], str);
    }

    public /* synthetic */ void lambda$showLanguageAlert$102(LocaleController.LocaleInfo[] localeInfoArr, String str, TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        HashMap hashMap = new HashMap();
        if (tLObject != null) {
            TLRPC$Vector tLRPC$Vector = (TLRPC$Vector) tLObject;
            for (int i = 0; i < tLRPC$Vector.objects.size(); i++) {
                TLRPC$LangPackString tLRPC$LangPackString = (TLRPC$LangPackString) tLRPC$Vector.objects.get(i);
                hashMap.put(tLRPC$LangPackString.key, tLRPC$LangPackString.value);
            }
        }
        AndroidUtilities.runOnUIThread(new LaunchActivity$$ExternalSyntheticLambda39(this, hashMap, localeInfoArr, str));
    }

    public /* synthetic */ void lambda$showLanguageAlert$101(HashMap hashMap, LocaleController.LocaleInfo[] localeInfoArr, String str) {
        this.englishLocaleStrings = hashMap;
        if (hashMap == null || this.systemLocaleStrings == null) {
            return;
        }
        showLanguageAlertInternal(localeInfoArr[1], localeInfoArr[0], str);
    }

    private void onPasscodePause() {
        if (this.lockRunnable != null) {
            if (BuildVars.LOGS_ENABLED) {
                FileLog.d("cancel lockRunnable onPasscodePause");
            }
            AndroidUtilities.cancelRunOnUIThread(this.lockRunnable);
            this.lockRunnable = null;
        }
        if (SharedConfig.passcodeHash.length() != 0) {
            SharedConfig.lastPauseTime = (int) (SystemClock.elapsedRealtime() / 1000);
            AnonymousClass19 anonymousClass19 = new AnonymousClass19();
            this.lockRunnable = anonymousClass19;
            if (SharedConfig.appLocked) {
                AndroidUtilities.runOnUIThread(anonymousClass19, 1000L);
                if (BuildVars.LOGS_ENABLED) {
                    FileLog.d("schedule app lock in 1000");
                }
            } else if (SharedConfig.autoLockIn != 0) {
                if (BuildVars.LOGS_ENABLED) {
                    FileLog.d("schedule app lock in " + ((SharedConfig.autoLockIn * 1000) + 1000));
                }
                AndroidUtilities.runOnUIThread(this.lockRunnable, (SharedConfig.autoLockIn * 1000) + 1000);
            }
        } else {
            SharedConfig.lastPauseTime = 0;
        }
        SharedConfig.saveConfig();
    }

    /* renamed from: org.telegram.ui.LaunchActivity$19 */
    /* loaded from: classes3.dex */
    public class AnonymousClass19 implements Runnable {
        AnonymousClass19() {
            LaunchActivity.this = r1;
        }

        @Override // java.lang.Runnable
        public void run() {
            if (LaunchActivity.this.lockRunnable == this) {
                if (AndroidUtilities.needShowPasscode(true)) {
                    if (BuildVars.LOGS_ENABLED) {
                        FileLog.d("lock app");
                    }
                    LaunchActivity.this.showPasscodeActivity(true, false, -1, -1, null, null);
                } else if (BuildVars.LOGS_ENABLED) {
                    FileLog.d("didn't pass lock check");
                }
                LaunchActivity.this.lockRunnable = null;
            }
        }
    }

    private void onPasscodeResume() {
        if (this.lockRunnable != null) {
            if (BuildVars.LOGS_ENABLED) {
                FileLog.d("cancel lockRunnable onPasscodeResume");
            }
            AndroidUtilities.cancelRunOnUIThread(this.lockRunnable);
            this.lockRunnable = null;
        }
        if (AndroidUtilities.needShowPasscode(true)) {
            showPasscodeActivity(true, false, -1, -1, null, null);
        }
        if (SharedConfig.lastPauseTime != 0) {
            SharedConfig.lastPauseTime = 0;
            SharedConfig.saveConfig();
            if (!BuildVars.LOGS_ENABLED) {
                return;
            }
            FileLog.d("reset lastPauseTime onPasscodeResume");
        }
    }

    private void updateCurrentConnectionState(int i) {
        String str;
        if (this.actionBarLayout == null) {
            return;
        }
        int i2 = 0;
        int connectionState = ConnectionsManager.getInstance(this.currentAccount).getConnectionState();
        this.currentConnectionState = connectionState;
        LaunchActivity$$ExternalSyntheticLambda34 launchActivity$$ExternalSyntheticLambda34 = null;
        if (connectionState == 2) {
            i2 = 2131629221;
            str = "WaitingForNetwork";
        } else if (connectionState == 5) {
            i2 = 2131628769;
            str = "Updating";
        } else if (connectionState == 4) {
            i2 = 2131625227;
            str = "ConnectingToProxy";
        } else if (connectionState == 1) {
            i2 = 2131625225;
            str = "Connecting";
        } else {
            str = null;
        }
        if (connectionState == 1 || connectionState == 4) {
            launchActivity$$ExternalSyntheticLambda34 = new LaunchActivity$$ExternalSyntheticLambda34(this);
        }
        this.actionBarLayout.setTitleOverlayText(str, i2, launchActivity$$ExternalSyntheticLambda34);
    }

    public /* synthetic */ void lambda$updateCurrentConnectionState$103() {
        BaseFragment baseFragment;
        if (AndroidUtilities.isTablet()) {
            if (!layerFragmentsStack.isEmpty()) {
                ArrayList<BaseFragment> arrayList = layerFragmentsStack;
                baseFragment = arrayList.get(arrayList.size() - 1);
            }
            baseFragment = null;
        } else {
            if (!mainFragmentsStack.isEmpty()) {
                ArrayList<BaseFragment> arrayList2 = mainFragmentsStack;
                baseFragment = arrayList2.get(arrayList2.size() - 1);
            }
            baseFragment = null;
        }
        if ((baseFragment instanceof ProxyListActivity) || (baseFragment instanceof ProxySettingsActivity)) {
            return;
        }
        lambda$runLinkRequest$59(new ProxyListActivity());
    }

    public void hideVisibleActionMode() {
        ActionMode actionMode = this.visibleActionMode;
        if (actionMode == null) {
            return;
        }
        actionMode.finish();
    }

    @Override // android.app.Activity
    protected void onSaveInstanceState(Bundle bundle) {
        try {
            super.onSaveInstanceState(bundle);
            BaseFragment baseFragment = null;
            if (AndroidUtilities.isTablet()) {
                if (!this.layersActionBarLayout.fragmentsStack.isEmpty()) {
                    ArrayList<BaseFragment> arrayList = this.layersActionBarLayout.fragmentsStack;
                    baseFragment = arrayList.get(arrayList.size() - 1);
                } else if (!this.rightActionBarLayout.fragmentsStack.isEmpty()) {
                    ArrayList<BaseFragment> arrayList2 = this.rightActionBarLayout.fragmentsStack;
                    baseFragment = arrayList2.get(arrayList2.size() - 1);
                } else if (!this.actionBarLayout.fragmentsStack.isEmpty()) {
                    ArrayList<BaseFragment> arrayList3 = this.actionBarLayout.fragmentsStack;
                    baseFragment = arrayList3.get(arrayList3.size() - 1);
                }
            } else if (!this.actionBarLayout.fragmentsStack.isEmpty()) {
                ArrayList<BaseFragment> arrayList4 = this.actionBarLayout.fragmentsStack;
                baseFragment = arrayList4.get(arrayList4.size() - 1);
            }
            if (baseFragment == null) {
                return;
            }
            Bundle arguments = baseFragment.getArguments();
            if ((baseFragment instanceof ChatActivity) && arguments != null) {
                bundle.putBundle("args", arguments);
                bundle.putString("fragment", "chat");
            } else if ((baseFragment instanceof GroupCreateFinalActivity) && arguments != null) {
                bundle.putBundle("args", arguments);
                bundle.putString("fragment", "group");
            } else if (baseFragment instanceof WallpapersListActivity) {
                bundle.putString("fragment", "wallpapers");
            } else if (baseFragment instanceof ProfileActivity) {
                ProfileActivity profileActivity = (ProfileActivity) baseFragment;
                if (profileActivity.isSettings()) {
                    bundle.putString("fragment", "settings");
                } else if (profileActivity.isChat() && arguments != null) {
                    bundle.putBundle("args", arguments);
                    bundle.putString("fragment", "chat_profile");
                }
            } else if ((baseFragment instanceof ChannelCreateActivity) && arguments != null && arguments.getInt("step") == 0) {
                bundle.putBundle("args", arguments);
                bundle.putString("fragment", "channel");
            }
            baseFragment.saveSelfArgs(bundle);
        } catch (Exception e) {
            FileLog.e(e);
        }
    }

    @Override // android.app.Activity
    public void onBackPressed() {
        PasscodeView passcodeView = this.passcodeView;
        if (passcodeView != null && passcodeView.getVisibility() == 0) {
            finish();
            return;
        }
        if (ContentPreviewViewer.hasInstance() && ContentPreviewViewer.getInstance().isVisible()) {
            ContentPreviewViewer.getInstance().closeWithMenu();
        }
        boolean z = false;
        if (SecretMediaViewer.hasInstance() && SecretMediaViewer.getInstance().isVisible()) {
            SecretMediaViewer.getInstance().closePhoto(true, false);
        } else if (PhotoViewer.hasInstance() && PhotoViewer.getInstance().isVisible()) {
            PhotoViewer.getInstance().closePhoto(true, false);
        } else if (ArticleViewer.hasInstance() && ArticleViewer.getInstance().isVisible()) {
            ArticleViewer.getInstance().close(true, false);
        } else if (this.drawerLayoutContainer.isDrawerOpened()) {
            this.drawerLayoutContainer.closeDrawer(false);
        } else if (AndroidUtilities.isTablet()) {
            if (this.layersActionBarLayout.getVisibility() == 0) {
                this.layersActionBarLayout.onBackPressed();
                return;
            }
            if (this.rightActionBarLayout.getVisibility() == 0 && !this.rightActionBarLayout.fragmentsStack.isEmpty()) {
                ArrayList<BaseFragment> arrayList = this.rightActionBarLayout.fragmentsStack;
                z = !arrayList.get(arrayList.size() - 1).onBackPressed();
            }
            if (z) {
                return;
            }
            this.actionBarLayout.onBackPressed();
        } else {
            this.actionBarLayout.onBackPressed();
        }
    }

    @Override // android.app.Activity, android.content.ComponentCallbacks
    public void onLowMemory() {
        super.onLowMemory();
        ActionBarLayout actionBarLayout = this.actionBarLayout;
        if (actionBarLayout != null) {
            actionBarLayout.onLowMemory();
            if (!AndroidUtilities.isTablet()) {
                return;
            }
            this.rightActionBarLayout.onLowMemory();
            this.layersActionBarLayout.onLowMemory();
        }
    }

    @Override // android.app.Activity, android.view.Window.Callback
    public void onActionModeStarted(ActionMode actionMode) {
        super.onActionModeStarted(actionMode);
        this.visibleActionMode = actionMode;
        try {
            Menu menu = actionMode.getMenu();
            if (menu != null && !this.actionBarLayout.extendActionMode(menu) && AndroidUtilities.isTablet() && !this.rightActionBarLayout.extendActionMode(menu)) {
                this.layersActionBarLayout.extendActionMode(menu);
            }
        } catch (Exception e) {
            FileLog.e(e);
        }
        if (Build.VERSION.SDK_INT < 23 || actionMode.getType() != 1) {
            this.actionBarLayout.onActionModeStarted(actionMode);
            if (!AndroidUtilities.isTablet()) {
                return;
            }
            this.rightActionBarLayout.onActionModeStarted(actionMode);
            this.layersActionBarLayout.onActionModeStarted(actionMode);
        }
    }

    @Override // android.app.Activity, android.view.Window.Callback
    public void onActionModeFinished(ActionMode actionMode) {
        super.onActionModeFinished(actionMode);
        if (this.visibleActionMode == actionMode) {
            this.visibleActionMode = null;
        }
        if (Build.VERSION.SDK_INT < 23 || actionMode.getType() != 1) {
            this.actionBarLayout.onActionModeFinished(actionMode);
            if (!AndroidUtilities.isTablet()) {
                return;
            }
            this.rightActionBarLayout.onActionModeFinished(actionMode);
            this.layersActionBarLayout.onActionModeFinished(actionMode);
        }
    }

    @Override // org.telegram.ui.ActionBar.ActionBarLayout.ActionBarLayoutDelegate
    public boolean onPreIme() {
        if (SecretMediaViewer.hasInstance() && SecretMediaViewer.getInstance().isVisible()) {
            SecretMediaViewer.getInstance().closePhoto(true, false);
            return true;
        } else if (PhotoViewer.hasInstance() && PhotoViewer.getInstance().isVisible()) {
            PhotoViewer.getInstance().closePhoto(true, false);
            return true;
        } else if (!ArticleViewer.hasInstance() || !ArticleViewer.getInstance().isVisible()) {
            return false;
        } else {
            ArticleViewer.getInstance().close(true, false);
            return true;
        }
    }

    @Override // android.app.Activity, android.view.Window.Callback
    public boolean dispatchKeyEvent(KeyEvent keyEvent) {
        keyEvent.getKeyCode();
        if (keyEvent.getAction() == 0 && (keyEvent.getKeyCode() == 24 || keyEvent.getKeyCode() == 25)) {
            boolean z = true;
            if (VoIPService.getSharedInstance() != null) {
                if (Build.VERSION.SDK_INT >= 32) {
                    boolean isSpeakerMuted = WebRtcAudioTrack.isSpeakerMuted();
                    AudioManager audioManager = (AudioManager) getSystemService("audio");
                    if (audioManager.getStreamVolume(0) != audioManager.getStreamMinVolume(0) || keyEvent.getKeyCode() != 25) {
                        z = false;
                    }
                    WebRtcAudioTrack.setSpeakerMute(z);
                    if (isSpeakerMuted != WebRtcAudioTrack.isSpeakerMuted()) {
                        showVoiceChatTooltip(z ? 42 : 43);
                    }
                }
            } else if (!mainFragmentsStack.isEmpty() && ((!PhotoViewer.hasInstance() || !PhotoViewer.getInstance().isVisible()) && keyEvent.getRepeatCount() == 0)) {
                ArrayList<BaseFragment> arrayList = mainFragmentsStack;
                BaseFragment baseFragment = arrayList.get(arrayList.size() - 1);
                if ((baseFragment instanceof ChatActivity) && ((ChatActivity) baseFragment).maybePlayVisibleVideo()) {
                    return true;
                }
                if (AndroidUtilities.isTablet() && !rightFragmentsStack.isEmpty()) {
                    ArrayList<BaseFragment> arrayList2 = rightFragmentsStack;
                    BaseFragment baseFragment2 = arrayList2.get(arrayList2.size() - 1);
                    if ((baseFragment2 instanceof ChatActivity) && ((ChatActivity) baseFragment2).maybePlayVisibleVideo()) {
                        return true;
                    }
                }
            }
        }
        try {
            super.dispatchKeyEvent(keyEvent);
        } catch (Exception e) {
            FileLog.e(e);
        }
        return false;
    }

    @Override // android.app.Activity, android.view.KeyEvent.Callback
    public boolean onKeyUp(int i, KeyEvent keyEvent) {
        if (i == 82 && !SharedConfig.isWaitingForPasscodeEnter) {
            if (PhotoViewer.hasInstance() && PhotoViewer.getInstance().isVisible()) {
                return super.onKeyUp(i, keyEvent);
            }
            if (ArticleViewer.hasInstance() && ArticleViewer.getInstance().isVisible()) {
                return super.onKeyUp(i, keyEvent);
            }
            if (AndroidUtilities.isTablet()) {
                if (this.layersActionBarLayout.getVisibility() == 0 && !this.layersActionBarLayout.fragmentsStack.isEmpty()) {
                    this.layersActionBarLayout.onKeyUp(i, keyEvent);
                } else if (this.rightActionBarLayout.getVisibility() == 0 && !this.rightActionBarLayout.fragmentsStack.isEmpty()) {
                    this.rightActionBarLayout.onKeyUp(i, keyEvent);
                } else {
                    this.actionBarLayout.onKeyUp(i, keyEvent);
                }
            } else if (this.actionBarLayout.fragmentsStack.size() == 1) {
                if (!this.drawerLayoutContainer.isDrawerOpened()) {
                    if (getCurrentFocus() != null) {
                        AndroidUtilities.hideKeyboard(getCurrentFocus());
                    }
                    this.drawerLayoutContainer.openDrawer(false);
                } else {
                    this.drawerLayoutContainer.closeDrawer(false);
                }
            } else {
                this.actionBarLayout.onKeyUp(i, keyEvent);
            }
        }
        return super.onKeyUp(i, keyEvent);
    }

    @Override // org.telegram.ui.ActionBar.ActionBarLayout.ActionBarLayoutDelegate
    public boolean needPresentFragment(BaseFragment baseFragment, boolean z, boolean z2, ActionBarLayout actionBarLayout) {
        ActionBarLayout actionBarLayout2;
        ActionBarLayout actionBarLayout3;
        ActionBarLayout actionBarLayout4;
        ActionBarLayout actionBarLayout5;
        if (ArticleViewer.hasInstance() && ArticleViewer.getInstance().isVisible()) {
            ArticleViewer.getInstance().close(false, true);
        }
        if (AndroidUtilities.isTablet()) {
            boolean z3 = baseFragment instanceof LoginActivity;
            this.drawerLayoutContainer.setAllowOpenDrawer(!z3 && !(baseFragment instanceof IntroActivity) && !(baseFragment instanceof CountrySelectActivity) && this.layersActionBarLayout.getVisibility() != 0, true);
            if ((baseFragment instanceof DialogsActivity) && ((DialogsActivity) baseFragment).isMainDialogList() && actionBarLayout != (actionBarLayout5 = this.actionBarLayout)) {
                actionBarLayout5.removeAllFragments();
                this.actionBarLayout.presentFragment(baseFragment, z, z2, false, false);
                this.layersActionBarLayout.removeAllFragments();
                this.layersActionBarLayout.setVisibility(8);
                this.drawerLayoutContainer.setAllowOpenDrawer(true, false);
                if (!this.tabletFullSize) {
                    this.shadowTabletSide.setVisibility(0);
                    if (this.rightActionBarLayout.fragmentsStack.isEmpty()) {
                        this.backgroundTablet.setVisibility(0);
                    }
                }
                return false;
            } else if ((baseFragment instanceof ChatActivity) && !((ChatActivity) baseFragment).isInScheduleMode()) {
                boolean z4 = this.tabletFullSize;
                if ((!z4 && actionBarLayout == this.rightActionBarLayout) || (z4 && actionBarLayout == this.actionBarLayout)) {
                    boolean z5 = (z4 && actionBarLayout == (actionBarLayout2 = this.actionBarLayout) && actionBarLayout2.fragmentsStack.size() == 1) ? false : true;
                    if (!this.layersActionBarLayout.fragmentsStack.isEmpty()) {
                        while (this.layersActionBarLayout.fragmentsStack.size() - 1 > 0) {
                            ActionBarLayout actionBarLayout6 = this.layersActionBarLayout;
                            actionBarLayout6.removeFragmentFromStack(actionBarLayout6.fragmentsStack.get(0));
                        }
                        this.layersActionBarLayout.closeLastFragment(!z2);
                    }
                    if (!z5) {
                        this.actionBarLayout.presentFragment(baseFragment, false, z2, false, false);
                    }
                    return z5;
                } else if (!z4 && actionBarLayout != (actionBarLayout4 = this.rightActionBarLayout)) {
                    actionBarLayout4.setVisibility(0);
                    this.backgroundTablet.setVisibility(8);
                    this.rightActionBarLayout.removeAllFragments();
                    this.rightActionBarLayout.presentFragment(baseFragment, z, true, false, false);
                    if (!this.layersActionBarLayout.fragmentsStack.isEmpty()) {
                        while (this.layersActionBarLayout.fragmentsStack.size() - 1 > 0) {
                            ActionBarLayout actionBarLayout7 = this.layersActionBarLayout;
                            actionBarLayout7.removeFragmentFromStack(actionBarLayout7.fragmentsStack.get(0));
                        }
                        this.layersActionBarLayout.closeLastFragment(!z2);
                    }
                    return false;
                } else if (z4 && actionBarLayout != (actionBarLayout3 = this.actionBarLayout)) {
                    actionBarLayout3.presentFragment(baseFragment, actionBarLayout3.fragmentsStack.size() > 1, z2, false, false);
                    if (!this.layersActionBarLayout.fragmentsStack.isEmpty()) {
                        while (this.layersActionBarLayout.fragmentsStack.size() - 1 > 0) {
                            ActionBarLayout actionBarLayout8 = this.layersActionBarLayout;
                            actionBarLayout8.removeFragmentFromStack(actionBarLayout8.fragmentsStack.get(0));
                        }
                        this.layersActionBarLayout.closeLastFragment(!z2);
                    }
                    return false;
                } else {
                    if (!this.layersActionBarLayout.fragmentsStack.isEmpty()) {
                        while (this.layersActionBarLayout.fragmentsStack.size() - 1 > 0) {
                            ActionBarLayout actionBarLayout9 = this.layersActionBarLayout;
                            actionBarLayout9.removeFragmentFromStack(actionBarLayout9.fragmentsStack.get(0));
                        }
                        this.layersActionBarLayout.closeLastFragment(!z2);
                    }
                    ActionBarLayout actionBarLayout10 = this.actionBarLayout;
                    actionBarLayout10.presentFragment(baseFragment, actionBarLayout10.fragmentsStack.size() > 1, z2, false, false);
                    return false;
                }
            } else {
                ActionBarLayout actionBarLayout11 = this.layersActionBarLayout;
                if (actionBarLayout != actionBarLayout11) {
                    actionBarLayout11.setVisibility(0);
                    this.drawerLayoutContainer.setAllowOpenDrawer(false, true);
                    if (z3) {
                        this.backgroundTablet.setVisibility(0);
                        this.shadowTabletSide.setVisibility(8);
                        this.shadowTablet.setBackgroundColor(0);
                    } else {
                        this.shadowTablet.setBackgroundColor(2130706432);
                    }
                    this.layersActionBarLayout.presentFragment(baseFragment, z, z2, false, false);
                    return false;
                }
            }
        } else {
            this.drawerLayoutContainer.setAllowOpenDrawer((baseFragment instanceof LoginActivity) || (baseFragment instanceof IntroActivity) ? !(mainFragmentsStack.size() == 0 || (mainFragmentsStack.get(0) instanceof IntroActivity)) : !((baseFragment instanceof CountrySelectActivity) && mainFragmentsStack.size() == 1), false);
        }
        return true;
    }

    @Override // org.telegram.ui.ActionBar.ActionBarLayout.ActionBarLayoutDelegate
    public boolean needAddFragmentToStack(BaseFragment baseFragment, ActionBarLayout actionBarLayout) {
        ActionBarLayout actionBarLayout2;
        ActionBarLayout actionBarLayout3;
        ActionBarLayout actionBarLayout4;
        if (AndroidUtilities.isTablet()) {
            boolean z = baseFragment instanceof LoginActivity;
            this.drawerLayoutContainer.setAllowOpenDrawer(!z && !(baseFragment instanceof IntroActivity) && !(baseFragment instanceof CountrySelectActivity) && this.layersActionBarLayout.getVisibility() != 0, true);
            if (baseFragment instanceof DialogsActivity) {
                if (((DialogsActivity) baseFragment).isMainDialogList() && actionBarLayout != (actionBarLayout4 = this.actionBarLayout)) {
                    actionBarLayout4.removeAllFragments();
                    this.actionBarLayout.addFragmentToStack(baseFragment);
                    this.layersActionBarLayout.removeAllFragments();
                    this.layersActionBarLayout.setVisibility(8);
                    this.drawerLayoutContainer.setAllowOpenDrawer(true, false);
                    if (!this.tabletFullSize) {
                        this.shadowTabletSide.setVisibility(0);
                        if (this.rightActionBarLayout.fragmentsStack.isEmpty()) {
                            this.backgroundTablet.setVisibility(0);
                        }
                    }
                    return false;
                }
            } else if ((baseFragment instanceof ChatActivity) && !((ChatActivity) baseFragment).isInScheduleMode()) {
                boolean z2 = this.tabletFullSize;
                if (!z2 && actionBarLayout != (actionBarLayout3 = this.rightActionBarLayout)) {
                    actionBarLayout3.setVisibility(0);
                    this.backgroundTablet.setVisibility(8);
                    this.rightActionBarLayout.removeAllFragments();
                    this.rightActionBarLayout.addFragmentToStack(baseFragment);
                    if (!this.layersActionBarLayout.fragmentsStack.isEmpty()) {
                        while (this.layersActionBarLayout.fragmentsStack.size() - 1 > 0) {
                            ActionBarLayout actionBarLayout5 = this.layersActionBarLayout;
                            actionBarLayout5.removeFragmentFromStack(actionBarLayout5.fragmentsStack.get(0));
                        }
                        this.layersActionBarLayout.closeLastFragment(true);
                    }
                    return false;
                } else if (z2 && actionBarLayout != (actionBarLayout2 = this.actionBarLayout)) {
                    actionBarLayout2.addFragmentToStack(baseFragment);
                    if (!this.layersActionBarLayout.fragmentsStack.isEmpty()) {
                        while (this.layersActionBarLayout.fragmentsStack.size() - 1 > 0) {
                            ActionBarLayout actionBarLayout6 = this.layersActionBarLayout;
                            actionBarLayout6.removeFragmentFromStack(actionBarLayout6.fragmentsStack.get(0));
                        }
                        this.layersActionBarLayout.closeLastFragment(true);
                    }
                    return false;
                }
            } else {
                ActionBarLayout actionBarLayout7 = this.layersActionBarLayout;
                if (actionBarLayout != actionBarLayout7) {
                    actionBarLayout7.setVisibility(0);
                    this.drawerLayoutContainer.setAllowOpenDrawer(false, true);
                    if (z) {
                        this.backgroundTablet.setVisibility(0);
                        this.shadowTabletSide.setVisibility(8);
                        this.shadowTablet.setBackgroundColor(0);
                    } else {
                        this.shadowTablet.setBackgroundColor(2130706432);
                    }
                    this.layersActionBarLayout.addFragmentToStack(baseFragment);
                    return false;
                }
            }
        } else {
            this.drawerLayoutContainer.setAllowOpenDrawer((baseFragment instanceof LoginActivity) || (baseFragment instanceof IntroActivity) ? !(mainFragmentsStack.size() == 0 || (mainFragmentsStack.get(0) instanceof IntroActivity)) : !((baseFragment instanceof CountrySelectActivity) && mainFragmentsStack.size() == 1), false);
        }
        return true;
    }

    @Override // org.telegram.ui.ActionBar.ActionBarLayout.ActionBarLayoutDelegate
    public boolean needCloseLastFragment(ActionBarLayout actionBarLayout) {
        if (AndroidUtilities.isTablet()) {
            if (actionBarLayout == this.actionBarLayout && actionBarLayout.fragmentsStack.size() <= 1) {
                onFinish();
                finish();
                return false;
            } else if (actionBarLayout == this.rightActionBarLayout) {
                if (!this.tabletFullSize) {
                    this.backgroundTablet.setVisibility(0);
                }
            } else if (actionBarLayout == this.layersActionBarLayout && this.actionBarLayout.fragmentsStack.isEmpty() && this.layersActionBarLayout.fragmentsStack.size() == 1) {
                onFinish();
                finish();
                return false;
            }
        } else if (actionBarLayout.fragmentsStack.size() <= 1) {
            onFinish();
            finish();
            return false;
        } else if (actionBarLayout.fragmentsStack.size() >= 2 && !(actionBarLayout.fragmentsStack.get(0) instanceof LoginActivity)) {
            this.drawerLayoutContainer.setAllowOpenDrawer(true, false);
        }
        return true;
    }

    public void rebuildAllFragments(boolean z) {
        ActionBarLayout actionBarLayout = this.layersActionBarLayout;
        if (actionBarLayout != null) {
            actionBarLayout.rebuildAllFragmentViews(z, z);
        } else {
            this.actionBarLayout.rebuildAllFragmentViews(z, z);
        }
    }

    @Override // org.telegram.ui.ActionBar.ActionBarLayout.ActionBarLayoutDelegate
    public void onRebuildAllFragments(ActionBarLayout actionBarLayout, boolean z) {
        if (AndroidUtilities.isTablet() && actionBarLayout == this.layersActionBarLayout) {
            this.rightActionBarLayout.rebuildAllFragmentViews(z, z);
            this.actionBarLayout.rebuildAllFragmentViews(z, z);
        }
        this.drawerLayoutAdapter.notifyDataSetChanged();
    }
}