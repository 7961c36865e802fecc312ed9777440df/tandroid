package org.telegram.ui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Vibrator;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import java.util.ArrayList;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.ChatObject;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.ImageLocation;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.NotificationCenter;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC$Chat;
import org.telegram.tgnet.TLRPC$ChatFull;
import org.telegram.tgnet.TLRPC$FileLocation;
import org.telegram.tgnet.TLRPC$InputFile;
import org.telegram.tgnet.TLRPC$PhotoSize;
import org.telegram.tgnet.TLRPC$TL_boolTrue;
import org.telegram.tgnet.TLRPC$TL_channels_checkUsername;
import org.telegram.tgnet.TLRPC$TL_channels_getAdminedPublicChannels;
import org.telegram.tgnet.TLRPC$TL_channels_updateUsername;
import org.telegram.tgnet.TLRPC$TL_chatInviteExported;
import org.telegram.tgnet.TLRPC$TL_error;
import org.telegram.tgnet.TLRPC$TL_inputChannelEmpty;
import org.telegram.tgnet.TLRPC$TL_messages_chats;
import org.telegram.tgnet.TLRPC$TL_messages_exportedChatInvites;
import org.telegram.tgnet.TLRPC$TL_messages_getExportedChatInvites;
import org.telegram.ui.ActionBar.ActionBar;
import org.telegram.ui.ActionBar.ActionBarMenu;
import org.telegram.ui.ActionBar.AlertDialog;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.ActionBar.ThemeDescription;
import org.telegram.ui.Cells.AdminedChannelCell;
import org.telegram.ui.Cells.HeaderCell;
import org.telegram.ui.Cells.LoadingCell;
import org.telegram.ui.Cells.RadioButtonCell;
import org.telegram.ui.Cells.ShadowSectionCell;
import org.telegram.ui.Cells.TextBlockCell;
import org.telegram.ui.Cells.TextInfoPrivacyCell;
import org.telegram.ui.Components.AvatarDrawable;
import org.telegram.ui.Components.BackupImageView;
import org.telegram.ui.Components.CircularProgressDrawable;
import org.telegram.ui.Components.CrossfadeDrawable;
import org.telegram.ui.Components.CubicBezierInterpolator;
import org.telegram.ui.Components.EditTextBoldCursor;
import org.telegram.ui.Components.EditTextEmoji;
import org.telegram.ui.Components.ImageUpdater;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.ui.Components.LinkActionView;
import org.telegram.ui.Components.Premium.LimitReachedBottomSheet;
import org.telegram.ui.Components.RLottieDrawable;
import org.telegram.ui.Components.RLottieImageView;
import org.telegram.ui.Components.RadialProgressView;
import org.telegram.ui.Components.SizeNotifierFrameLayout;
/* loaded from: classes3.dex */
public class ChannelCreateActivity extends BaseFragment implements NotificationCenter.NotificationCenterDelegate, ImageUpdater.ImageUpdaterDelegate {
    private ArrayList<AdminedChannelCell> adminedChannelCells = new ArrayList<>();
    private TextInfoPrivacyCell adminedInfoCell;
    private LinearLayout adminnedChannelsLayout;
    private TLRPC$FileLocation avatar;
    private AnimatorSet avatarAnimation;
    private TLRPC$FileLocation avatarBig;
    private AvatarDrawable avatarDrawable;
    private RLottieImageView avatarEditor;
    private BackupImageView avatarImage;
    private View avatarOverlay;
    private RadialProgressView avatarProgressView;
    private RLottieDrawable cameraDrawable;
    private boolean canCreatePublic;
    private AlertDialog cancelDialog;
    private long chatId;
    private int checkReqId;
    private Runnable checkRunnable;
    private TextView checkTextView;
    private boolean createAfterUpload;
    private int currentStep;
    private EditTextBoldCursor descriptionTextView;
    private View doneButton;
    private CrossfadeDrawable doneButtonDrawable;
    private ValueAnimator doneButtonDrawableAnimator;
    private boolean donePressed;
    private Integer doneRequestId;
    private EditTextBoldCursor editText;
    private HeaderCell headerCell;
    private HeaderCell headerCell2;
    private TextView helpTextView;
    private ImageUpdater imageUpdater;
    private TLRPC$InputFile inputPhoto;
    private TLRPC$InputFile inputVideo;
    private String inputVideoPath;
    private TLRPC$TL_chatInviteExported invite;
    private boolean isPrivate;
    private String lastCheckName;
    private boolean lastNameAvailable;
    private LinearLayout linearLayout;
    private LinearLayout linearLayout2;
    private LinearLayout linkContainer;
    private LoadingCell loadingAdminedCell;
    private boolean loadingAdminedChannels;
    private boolean loadingInvite;
    private EditTextEmoji nameTextView;
    private String nameToSet;
    private LinkActionView permanentLinkView;
    private LinearLayout privateContainer;
    private LinearLayout publicContainer;
    private RadioButtonCell radioButtonCell1;
    private RadioButtonCell radioButtonCell2;
    private ShadowSectionCell sectionCell;
    private TextInfoPrivacyCell typeInfoCell;
    private double videoTimestamp;

    public static /* synthetic */ boolean lambda$createView$4(View view, MotionEvent motionEvent) {
        return true;
    }

    public ChannelCreateActivity(Bundle bundle) {
        super(bundle);
        this.canCreatePublic = true;
        int i = bundle.getInt("step", 0);
        this.currentStep = i;
        if (i == 0) {
            this.avatarDrawable = new AvatarDrawable();
            this.imageUpdater = new ImageUpdater(true);
            TLRPC$TL_channels_checkUsername tLRPC$TL_channels_checkUsername = new TLRPC$TL_channels_checkUsername();
            tLRPC$TL_channels_checkUsername.username = "1";
            tLRPC$TL_channels_checkUsername.channel = new TLRPC$TL_inputChannelEmpty();
            ConnectionsManager.getInstance(this.currentAccount).sendRequest(tLRPC$TL_channels_checkUsername, new ChannelCreateActivity$$ExternalSyntheticLambda23(this));
            return;
        }
        if (i == 1) {
            boolean z = bundle.getBoolean("canCreatePublic", true);
            this.canCreatePublic = z;
            this.isPrivate = !z;
            if (!z) {
                loadAdminedChannels();
            }
        }
        this.chatId = bundle.getLong("chat_id", 0L);
    }

    public /* synthetic */ void lambda$new$0(TLRPC$TL_error tLRPC$TL_error) {
        this.canCreatePublic = tLRPC$TL_error == null || !tLRPC$TL_error.text.equals("CHANNELS_ADMIN_PUBLIC_TOO_MUCH");
    }

    public /* synthetic */ void lambda$new$1(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        AndroidUtilities.runOnUIThread(new ChannelCreateActivity$$ExternalSyntheticLambda18(this, tLRPC$TL_error));
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public boolean onFragmentCreate() {
        NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.chatDidCreated);
        NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.chatDidFailCreate);
        if (this.currentStep == 1) {
            generateLink();
        }
        ImageUpdater imageUpdater = this.imageUpdater;
        if (imageUpdater != null) {
            imageUpdater.parentFragment = this;
            imageUpdater.setDelegate(this);
        }
        return super.onFragmentCreate();
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public void onFragmentDestroy() {
        super.onFragmentDestroy();
        if (this.doneRequestId != null) {
            ConnectionsManager.getInstance(this.currentAccount).cancelRequest(this.doneRequestId.intValue(), true);
            this.doneRequestId = null;
        }
        NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.chatDidCreated);
        NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.chatDidFailCreate);
        ImageUpdater imageUpdater = this.imageUpdater;
        if (imageUpdater != null) {
            imageUpdater.clear();
        }
        AndroidUtilities.removeAdjustResize(getParentActivity(), this.classGuid);
        EditTextEmoji editTextEmoji = this.nameTextView;
        if (editTextEmoji != null) {
            editTextEmoji.onDestroy();
        }
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public void onResume() {
        super.onResume();
        EditTextEmoji editTextEmoji = this.nameTextView;
        if (editTextEmoji != null) {
            editTextEmoji.onResume();
        }
        AndroidUtilities.requestAdjustResize(getParentActivity(), this.classGuid);
        ImageUpdater imageUpdater = this.imageUpdater;
        if (imageUpdater != null) {
            imageUpdater.onResume();
        }
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public void onPause() {
        super.onPause();
        EditTextEmoji editTextEmoji = this.nameTextView;
        if (editTextEmoji != null) {
            editTextEmoji.onPause();
        }
        ImageUpdater imageUpdater = this.imageUpdater;
        if (imageUpdater != null) {
            imageUpdater.onPause();
        }
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public void dismissCurrentDialog() {
        ImageUpdater imageUpdater = this.imageUpdater;
        if (imageUpdater == null || !imageUpdater.dismissCurrentDialog(this.visibleDialog)) {
            super.dismissCurrentDialog();
        }
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public boolean dismissDialogOnPause(Dialog dialog) {
        ImageUpdater imageUpdater = this.imageUpdater;
        return (imageUpdater == null || imageUpdater.dismissDialogOnPause(dialog)) && super.dismissDialogOnPause(dialog);
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public void onRequestPermissionsResultFragment(int i, String[] strArr, int[] iArr) {
        ImageUpdater imageUpdater = this.imageUpdater;
        if (imageUpdater != null) {
            imageUpdater.onRequestPermissionsResultFragment(i, strArr, iArr);
        }
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public boolean onBackPressed() {
        EditTextEmoji editTextEmoji = this.nameTextView;
        if (editTextEmoji == null || !editTextEmoji.isPopupShowing()) {
            return true;
        }
        this.nameTextView.hidePopup(true);
        return false;
    }

    public void showDoneCancelDialog() {
        if (this.cancelDialog != null) {
            return;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(getParentActivity());
        builder.setTitle(LocaleController.getString("AppName", 2131624375));
        builder.setMessage(LocaleController.getString("StopLoading", 2131628471));
        builder.setPositiveButton(LocaleController.getString("WaitMore", 2131629220), null);
        builder.setNegativeButton(LocaleController.getString("Stop", 2131628463), new ChannelCreateActivity$$ExternalSyntheticLambda1(this));
        this.cancelDialog = builder.show();
    }

    public /* synthetic */ void lambda$showDoneCancelDialog$2(DialogInterface dialogInterface, int i) {
        this.donePressed = false;
        this.createAfterUpload = false;
        if (this.doneRequestId != null) {
            ConnectionsManager.getInstance(this.currentAccount).cancelRequest(this.doneRequestId.intValue(), true);
            this.doneRequestId = null;
        }
        updateDoneProgress(false);
        dialogInterface.dismiss();
    }

    public void updateDoneProgress(boolean z) {
        if (this.doneButtonDrawable != null) {
            ValueAnimator valueAnimator = this.doneButtonDrawableAnimator;
            if (valueAnimator != null) {
                valueAnimator.cancel();
            }
            float[] fArr = new float[2];
            fArr[0] = this.doneButtonDrawable.getProgress();
            float f = 1.0f;
            fArr[1] = z ? 1.0f : 0.0f;
            ValueAnimator ofFloat = ValueAnimator.ofFloat(fArr);
            this.doneButtonDrawableAnimator = ofFloat;
            ofFloat.addUpdateListener(new ChannelCreateActivity$$ExternalSyntheticLambda0(this));
            ValueAnimator valueAnimator2 = this.doneButtonDrawableAnimator;
            float progress = this.doneButtonDrawable.getProgress();
            if (!z) {
                f = 0.0f;
            }
            valueAnimator2.setDuration(Math.abs(progress - f) * 200.0f);
            this.doneButtonDrawableAnimator.setInterpolator(CubicBezierInterpolator.DEFAULT);
            this.doneButtonDrawableAnimator.start();
        }
    }

    public /* synthetic */ void lambda$updateDoneProgress$3(ValueAnimator valueAnimator) {
        this.doneButtonDrawable.setProgress(((Float) valueAnimator.getAnimatedValue()).floatValue());
        this.doneButtonDrawable.invalidateSelf();
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public View createView(Context context) {
        EditTextEmoji editTextEmoji = this.nameTextView;
        if (editTextEmoji != null) {
            editTextEmoji.onDestroy();
        }
        this.actionBar.setBackButtonImage(2131165449);
        this.actionBar.setAllowOverlayTitle(true);
        this.actionBar.setActionBarMenuOnItemClick(new AnonymousClass1());
        ActionBarMenu createMenu = this.actionBar.createMenu();
        Drawable mutate = context.getResources().getDrawable(2131165450).mutate();
        mutate.setColorFilter(new PorterDuffColorFilter(Theme.getColor("actionBarDefaultIcon"), PorterDuff.Mode.MULTIPLY));
        CrossfadeDrawable crossfadeDrawable = new CrossfadeDrawable(mutate, new CircularProgressDrawable(Theme.getColor("actionBarDefaultIcon")));
        this.doneButtonDrawable = crossfadeDrawable;
        this.doneButton = createMenu.addItemWithWidth(1, crossfadeDrawable, AndroidUtilities.dp(56.0f), LocaleController.getString("Done", 2131625525));
        int i = this.currentStep;
        if (i == 0) {
            this.actionBar.setTitle(LocaleController.getString("NewChannel", 2131626772));
            AnonymousClass2 anonymousClass2 = new AnonymousClass2(context);
            anonymousClass2.setOnTouchListener(ChannelCreateActivity$$ExternalSyntheticLambda8.INSTANCE);
            this.fragmentView = anonymousClass2;
            anonymousClass2.setTag("windowBackgroundWhite");
            this.fragmentView.setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
            LinearLayout linearLayout = new LinearLayout(context);
            this.linearLayout = linearLayout;
            linearLayout.setOrientation(1);
            anonymousClass2.addView(this.linearLayout, new FrameLayout.LayoutParams(-1, -2));
            FrameLayout frameLayout = new FrameLayout(context);
            this.linearLayout.addView(frameLayout, LayoutHelper.createLinear(-1, -2));
            AnonymousClass3 anonymousClass3 = new AnonymousClass3(context);
            this.avatarImage = anonymousClass3;
            anonymousClass3.setRoundRadius(AndroidUtilities.dp(32.0f));
            this.avatarDrawable.setInfo(5L, null, null);
            this.avatarImage.setImageDrawable(this.avatarDrawable);
            BackupImageView backupImageView = this.avatarImage;
            boolean z = LocaleController.isRTL;
            frameLayout.addView(backupImageView, LayoutHelper.createFrame(64, 64.0f, (z ? 5 : 3) | 48, z ? 0.0f : 16.0f, 12.0f, z ? 16.0f : 0.0f, 12.0f));
            Paint paint = new Paint(1);
            paint.setColor(1426063360);
            AnonymousClass4 anonymousClass4 = new AnonymousClass4(context, paint);
            this.avatarOverlay = anonymousClass4;
            anonymousClass4.setContentDescription(LocaleController.getString("ChatSetPhotoOrVideo", 2131625030));
            View view = this.avatarOverlay;
            boolean z2 = LocaleController.isRTL;
            frameLayout.addView(view, LayoutHelper.createFrame(64, 64.0f, (z2 ? 5 : 3) | 48, z2 ? 0.0f : 16.0f, 12.0f, z2 ? 16.0f : 0.0f, 12.0f));
            this.avatarOverlay.setOnClickListener(new ChannelCreateActivity$$ExternalSyntheticLambda7(this));
            this.cameraDrawable = new RLottieDrawable(2131558413, "2131558413", AndroidUtilities.dp(60.0f), AndroidUtilities.dp(60.0f), false, null);
            AnonymousClass5 anonymousClass5 = new AnonymousClass5(context);
            this.avatarEditor = anonymousClass5;
            anonymousClass5.setScaleType(ImageView.ScaleType.CENTER);
            this.avatarEditor.setAnimation(this.cameraDrawable);
            this.avatarEditor.setEnabled(false);
            this.avatarEditor.setClickable(false);
            this.avatarEditor.setPadding(AndroidUtilities.dp(2.0f), 0, 0, AndroidUtilities.dp(1.0f));
            RLottieImageView rLottieImageView = this.avatarEditor;
            boolean z3 = LocaleController.isRTL;
            frameLayout.addView(rLottieImageView, LayoutHelper.createFrame(64, 64.0f, (z3 ? 5 : 3) | 48, z3 ? 0.0f : 16.0f, 12.0f, z3 ? 16.0f : 0.0f, 12.0f));
            RadialProgressView radialProgressView = new RadialProgressView(context);
            this.avatarProgressView = radialProgressView;
            radialProgressView.setSize(AndroidUtilities.dp(30.0f));
            this.avatarProgressView.setProgressColor(-1);
            this.avatarProgressView.setNoProgress(false);
            RadialProgressView radialProgressView2 = this.avatarProgressView;
            boolean z4 = LocaleController.isRTL;
            frameLayout.addView(radialProgressView2, LayoutHelper.createFrame(64, 64.0f, (z4 ? 5 : 3) | 48, z4 ? 0.0f : 16.0f, 12.0f, z4 ? 16.0f : 0.0f, 12.0f));
            showAvatarProgress(false, false);
            EditTextEmoji editTextEmoji2 = new EditTextEmoji(context, anonymousClass2, this, 0);
            this.nameTextView = editTextEmoji2;
            editTextEmoji2.setHint(LocaleController.getString("EnterChannelName", 2131625644));
            String str = this.nameToSet;
            if (str != null) {
                this.nameTextView.setText(str);
                this.nameToSet = null;
            }
            this.nameTextView.setFilters(new InputFilter[]{new InputFilter.LengthFilter(100)});
            this.nameTextView.getEditText().setSingleLine(true);
            this.nameTextView.getEditText().setImeOptions(5);
            this.nameTextView.getEditText().setOnEditorActionListener(new ChannelCreateActivity$$ExternalSyntheticLambda9(this));
            EditTextEmoji editTextEmoji3 = this.nameTextView;
            boolean z5 = LocaleController.isRTL;
            frameLayout.addView(editTextEmoji3, LayoutHelper.createFrame(-1, -2.0f, 16, z5 ? 5.0f : 96.0f, 0.0f, z5 ? 96.0f : 5.0f, 0.0f));
            EditTextBoldCursor editTextBoldCursor = new EditTextBoldCursor(context);
            this.descriptionTextView = editTextBoldCursor;
            editTextBoldCursor.setTextSize(1, 18.0f);
            this.descriptionTextView.setHintTextColor(Theme.getColor("windowBackgroundWhiteHintText"));
            this.descriptionTextView.setTextColor(Theme.getColor("windowBackgroundWhiteBlackText"));
            this.descriptionTextView.setBackgroundDrawable(null);
            this.descriptionTextView.setLineColors(getThemedColor("windowBackgroundWhiteInputField"), getThemedColor("windowBackgroundWhiteInputFieldActivated"), getThemedColor("windowBackgroundWhiteRedText3"));
            this.descriptionTextView.setPadding(0, 0, 0, AndroidUtilities.dp(6.0f));
            this.descriptionTextView.setGravity(LocaleController.isRTL ? 5 : 3);
            this.descriptionTextView.setInputType(180225);
            this.descriptionTextView.setImeOptions(6);
            this.descriptionTextView.setFilters(new InputFilter[]{new InputFilter.LengthFilter(120)});
            this.descriptionTextView.setHint(LocaleController.getString("DescriptionPlaceholder", 2131625463));
            this.descriptionTextView.setCursorColor(Theme.getColor("windowBackgroundWhiteBlackText"));
            this.descriptionTextView.setCursorSize(AndroidUtilities.dp(20.0f));
            this.descriptionTextView.setCursorWidth(1.5f);
            this.linearLayout.addView(this.descriptionTextView, LayoutHelper.createLinear(-1, -2, 24.0f, 18.0f, 24.0f, 0.0f));
            this.descriptionTextView.setOnEditorActionListener(new ChannelCreateActivity$$ExternalSyntheticLambda10(this));
            this.descriptionTextView.addTextChangedListener(new AnonymousClass6(this));
            TextView textView = new TextView(context);
            this.helpTextView = textView;
            textView.setTextSize(1, 15.0f);
            this.helpTextView.setTextColor(Theme.getColor("windowBackgroundWhiteGrayText8"));
            this.helpTextView.setGravity(LocaleController.isRTL ? 5 : 3);
            this.helpTextView.setText(LocaleController.getString("DescriptionInfo", 2131625460));
            this.linearLayout.addView(this.helpTextView, LayoutHelper.createLinear(-2, -2, LocaleController.isRTL ? 5 : 3, 24, 10, 24, 20));
        } else if (i == 1) {
            ScrollView scrollView = new ScrollView(context);
            this.fragmentView = scrollView;
            ScrollView scrollView2 = scrollView;
            scrollView2.setFillViewport(true);
            LinearLayout linearLayout2 = new LinearLayout(context);
            this.linearLayout = linearLayout2;
            linearLayout2.setOrientation(1);
            scrollView2.addView(this.linearLayout, new FrameLayout.LayoutParams(-1, -2));
            this.actionBar.setTitle(LocaleController.getString("ChannelSettingsTitle", 2131624964));
            this.fragmentView.setTag("windowBackgroundGray");
            this.fragmentView.setBackgroundColor(Theme.getColor("windowBackgroundGray"));
            HeaderCell headerCell = new HeaderCell(context, 23);
            this.headerCell2 = headerCell;
            headerCell.setHeight(46);
            this.headerCell2.setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
            this.headerCell2.setText(LocaleController.getString("ChannelTypeHeader", 2131624974));
            this.linearLayout.addView(this.headerCell2);
            LinearLayout linearLayout3 = new LinearLayout(context);
            this.linearLayout2 = linearLayout3;
            linearLayout3.setOrientation(1);
            this.linearLayout2.setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
            this.linearLayout.addView(this.linearLayout2, LayoutHelper.createLinear(-1, -2));
            RadioButtonCell radioButtonCell = new RadioButtonCell(context);
            this.radioButtonCell1 = radioButtonCell;
            radioButtonCell.setBackgroundDrawable(Theme.getSelectorDrawable(false));
            this.radioButtonCell1.setTextAndValue(LocaleController.getString("ChannelPublic", 2131624949), LocaleController.getString("ChannelPublicInfo", 2131624952), false, !this.isPrivate);
            this.linearLayout2.addView(this.radioButtonCell1, LayoutHelper.createLinear(-1, -2));
            this.radioButtonCell1.setOnClickListener(new ChannelCreateActivity$$ExternalSyntheticLambda4(this));
            RadioButtonCell radioButtonCell2 = new RadioButtonCell(context);
            this.radioButtonCell2 = radioButtonCell2;
            radioButtonCell2.setBackgroundDrawable(Theme.getSelectorDrawable(false));
            this.radioButtonCell2.setTextAndValue(LocaleController.getString("ChannelPrivate", 2131624946), LocaleController.getString("ChannelPrivateInfo", 2131624947), false, this.isPrivate);
            this.linearLayout2.addView(this.radioButtonCell2, LayoutHelper.createLinear(-1, -2));
            this.radioButtonCell2.setOnClickListener(new ChannelCreateActivity$$ExternalSyntheticLambda6(this));
            ShadowSectionCell shadowSectionCell = new ShadowSectionCell(context);
            this.sectionCell = shadowSectionCell;
            this.linearLayout.addView(shadowSectionCell, LayoutHelper.createLinear(-1, -2));
            LinearLayout linearLayout4 = new LinearLayout(context);
            this.linkContainer = linearLayout4;
            linearLayout4.setOrientation(1);
            this.linkContainer.setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
            this.linearLayout.addView(this.linkContainer, LayoutHelper.createLinear(-1, -2));
            HeaderCell headerCell2 = new HeaderCell(context);
            this.headerCell = headerCell2;
            this.linkContainer.addView(headerCell2);
            LinearLayout linearLayout5 = new LinearLayout(context);
            this.publicContainer = linearLayout5;
            linearLayout5.setOrientation(0);
            this.linkContainer.addView(this.publicContainer, LayoutHelper.createLinear(-1, 36, 21.0f, 7.0f, 21.0f, 0.0f));
            EditTextBoldCursor editTextBoldCursor2 = new EditTextBoldCursor(context);
            this.editText = editTextBoldCursor2;
            editTextBoldCursor2.setText(MessagesController.getInstance(this.currentAccount).linkPrefix + "/");
            this.editText.setTextSize(1, 18.0f);
            this.editText.setHintTextColor(Theme.getColor("windowBackgroundWhiteHintText"));
            this.editText.setTextColor(Theme.getColor("windowBackgroundWhiteBlackText"));
            this.editText.setMaxLines(1);
            this.editText.setLines(1);
            this.editText.setEnabled(false);
            this.editText.setBackgroundDrawable(null);
            this.editText.setPadding(0, 0, 0, 0);
            this.editText.setSingleLine(true);
            this.editText.setInputType(163840);
            this.editText.setImeOptions(6);
            this.publicContainer.addView(this.editText, LayoutHelper.createLinear(-2, 36));
            EditTextBoldCursor editTextBoldCursor3 = new EditTextBoldCursor(context);
            this.descriptionTextView = editTextBoldCursor3;
            editTextBoldCursor3.setTextSize(1, 18.0f);
            this.descriptionTextView.setHintTextColor(Theme.getColor("windowBackgroundWhiteHintText"));
            this.descriptionTextView.setTextColor(Theme.getColor("windowBackgroundWhiteBlackText"));
            this.descriptionTextView.setMaxLines(1);
            this.descriptionTextView.setLines(1);
            this.descriptionTextView.setBackgroundDrawable(null);
            this.descriptionTextView.setPadding(0, 0, 0, 0);
            this.descriptionTextView.setSingleLine(true);
            this.descriptionTextView.setInputType(163872);
            this.descriptionTextView.setImeOptions(6);
            this.descriptionTextView.setHint(LocaleController.getString("ChannelUsernamePlaceholder", 2131624982));
            this.descriptionTextView.setCursorColor(Theme.getColor("windowBackgroundWhiteBlackText"));
            this.descriptionTextView.setCursorSize(AndroidUtilities.dp(20.0f));
            this.descriptionTextView.setCursorWidth(1.5f);
            this.publicContainer.addView(this.descriptionTextView, LayoutHelper.createLinear(-1, 36));
            this.descriptionTextView.addTextChangedListener(new AnonymousClass7());
            LinearLayout linearLayout6 = new LinearLayout(context);
            this.privateContainer = linearLayout6;
            linearLayout6.setOrientation(1);
            this.linkContainer.addView(this.privateContainer, LayoutHelper.createLinear(-1, -2));
            LinkActionView linkActionView = new LinkActionView(context, this, null, this.chatId, true, ChatObject.isChannel(getMessagesController().getChat(Long.valueOf(this.chatId))));
            this.permanentLinkView = linkActionView;
            linkActionView.hideRevokeOption(true);
            this.permanentLinkView.setUsers(0, null);
            this.privateContainer.addView(this.permanentLinkView);
            TextView textView2 = new TextView(context);
            this.checkTextView = textView2;
            textView2.setTextSize(1, 15.0f);
            this.checkTextView.setGravity(LocaleController.isRTL ? 5 : 3);
            this.checkTextView.setVisibility(8);
            this.linkContainer.addView(this.checkTextView, LayoutHelper.createLinear(-2, -2, LocaleController.isRTL ? 5 : 3, 17, 3, 17, 7));
            TextInfoPrivacyCell textInfoPrivacyCell = new TextInfoPrivacyCell(context);
            this.typeInfoCell = textInfoPrivacyCell;
            textInfoPrivacyCell.setBackgroundDrawable(Theme.getThemedDrawable(context, 2131165436, "windowBackgroundGrayShadow"));
            this.linearLayout.addView(this.typeInfoCell, LayoutHelper.createLinear(-1, -2));
            LoadingCell loadingCell = new LoadingCell(context);
            this.loadingAdminedCell = loadingCell;
            this.linearLayout.addView(loadingCell, LayoutHelper.createLinear(-1, -2));
            LinearLayout linearLayout7 = new LinearLayout(context);
            this.adminnedChannelsLayout = linearLayout7;
            linearLayout7.setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
            this.adminnedChannelsLayout.setOrientation(1);
            this.linearLayout.addView(this.adminnedChannelsLayout, LayoutHelper.createLinear(-1, -2));
            TextInfoPrivacyCell textInfoPrivacyCell2 = new TextInfoPrivacyCell(context);
            this.adminedInfoCell = textInfoPrivacyCell2;
            textInfoPrivacyCell2.setBackgroundDrawable(Theme.getThemedDrawable(context, 2131165436, "windowBackgroundGrayShadow"));
            this.linearLayout.addView(this.adminedInfoCell, LayoutHelper.createLinear(-1, -2));
            updatePrivatePublic();
        }
        return this.fragmentView;
    }

    /* renamed from: org.telegram.ui.ChannelCreateActivity$1 */
    /* loaded from: classes3.dex */
    class AnonymousClass1 extends ActionBar.ActionBarMenuOnItemClick {
        AnonymousClass1() {
            ChannelCreateActivity.this = r1;
        }

        @Override // org.telegram.ui.ActionBar.ActionBar.ActionBarMenuOnItemClick
        public void onItemClick(int i) {
            if (i == -1) {
                if (ChannelCreateActivity.this.donePressed) {
                    ChannelCreateActivity.this.showDoneCancelDialog();
                } else {
                    ChannelCreateActivity.this.finishFragment();
                }
            } else if (i != 1) {
            } else {
                if (ChannelCreateActivity.this.currentStep != 0) {
                    if (ChannelCreateActivity.this.currentStep != 1) {
                        return;
                    }
                    if (!ChannelCreateActivity.this.isPrivate) {
                        if (ChannelCreateActivity.this.descriptionTextView.length() != 0) {
                            if (ChannelCreateActivity.this.lastNameAvailable) {
                                MessagesController.getInstance(((BaseFragment) ChannelCreateActivity.this).currentAccount).updateChannelUserName(ChannelCreateActivity.this.chatId, ChannelCreateActivity.this.lastCheckName);
                            } else {
                                Vibrator vibrator = (Vibrator) ChannelCreateActivity.this.getParentActivity().getSystemService("vibrator");
                                if (vibrator != null) {
                                    vibrator.vibrate(200L);
                                }
                                AndroidUtilities.shakeView(ChannelCreateActivity.this.checkTextView, 2.0f, 0);
                                return;
                            }
                        } else {
                            AlertDialog.Builder builder = new AlertDialog.Builder(ChannelCreateActivity.this.getParentActivity());
                            builder.setTitle(LocaleController.getString("ChannelPublicEmptyUsernameTitle", 2131624951));
                            builder.setMessage(LocaleController.getString("ChannelPublicEmptyUsername", 2131624950));
                            builder.setPositiveButton(LocaleController.getString("Close", 2131625167), null);
                            ChannelCreateActivity.this.showDialog(builder.create());
                            return;
                        }
                    }
                    Bundle bundle = new Bundle();
                    bundle.putInt("step", 2);
                    bundle.putLong("chatId", ChannelCreateActivity.this.chatId);
                    bundle.putInt("chatType", 2);
                    ChannelCreateActivity.this.presentFragment(new GroupCreateActivity(bundle), true);
                } else if (ChannelCreateActivity.this.getParentActivity() == null) {
                } else {
                    if (ChannelCreateActivity.this.donePressed) {
                        ChannelCreateActivity.this.showDoneCancelDialog();
                    } else if (ChannelCreateActivity.this.nameTextView.length() != 0) {
                        ChannelCreateActivity.this.donePressed = true;
                        ChannelCreateActivity.this.updateDoneProgress(true);
                        if (ChannelCreateActivity.this.imageUpdater.isUploadingImage()) {
                            ChannelCreateActivity.this.createAfterUpload = true;
                            return;
                        }
                        ChannelCreateActivity channelCreateActivity = ChannelCreateActivity.this;
                        channelCreateActivity.doneRequestId = Integer.valueOf(MessagesController.getInstance(((BaseFragment) channelCreateActivity).currentAccount).createChat(ChannelCreateActivity.this.nameTextView.getText().toString(), new ArrayList<>(), ChannelCreateActivity.this.descriptionTextView.getText().toString(), 2, false, null, null, ChannelCreateActivity.this));
                    } else {
                        Vibrator vibrator2 = (Vibrator) ChannelCreateActivity.this.getParentActivity().getSystemService("vibrator");
                        if (vibrator2 != null) {
                            vibrator2.vibrate(200L);
                        }
                        AndroidUtilities.shakeView(ChannelCreateActivity.this.nameTextView, 2.0f, 0);
                    }
                }
            }
        }
    }

    /* renamed from: org.telegram.ui.ChannelCreateActivity$2 */
    /* loaded from: classes3.dex */
    class AnonymousClass2 extends SizeNotifierFrameLayout {
        private boolean ignoreLayout;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass2(Context context) {
            super(context);
            ChannelCreateActivity.this = r1;
        }

        @Override // android.widget.FrameLayout, android.view.View
        protected void onMeasure(int i, int i2) {
            int size = View.MeasureSpec.getSize(i);
            int size2 = View.MeasureSpec.getSize(i2);
            setMeasuredDimension(size, size2);
            int paddingTop = size2 - getPaddingTop();
            measureChildWithMargins(((BaseFragment) ChannelCreateActivity.this).actionBar, i, 0, i2, 0);
            if (measureKeyboardHeight() > AndroidUtilities.dp(20.0f)) {
                this.ignoreLayout = true;
                ChannelCreateActivity.this.nameTextView.hideEmojiView();
                this.ignoreLayout = false;
            }
            int childCount = getChildCount();
            for (int i3 = 0; i3 < childCount; i3++) {
                View childAt = getChildAt(i3);
                if (childAt != null && childAt.getVisibility() != 8 && childAt != ((BaseFragment) ChannelCreateActivity.this).actionBar) {
                    if (ChannelCreateActivity.this.nameTextView != null && ChannelCreateActivity.this.nameTextView.isPopupView(childAt)) {
                        if (AndroidUtilities.isInMultiwindow || AndroidUtilities.isTablet()) {
                            if (AndroidUtilities.isTablet()) {
                                childAt.measure(View.MeasureSpec.makeMeasureSpec(size, 1073741824), View.MeasureSpec.makeMeasureSpec(Math.min(AndroidUtilities.dp(AndroidUtilities.isTablet() ? 200.0f : 320.0f), (paddingTop - AndroidUtilities.statusBarHeight) + getPaddingTop()), 1073741824));
                            } else {
                                childAt.measure(View.MeasureSpec.makeMeasureSpec(size, 1073741824), View.MeasureSpec.makeMeasureSpec((paddingTop - AndroidUtilities.statusBarHeight) + getPaddingTop(), 1073741824));
                            }
                        } else {
                            childAt.measure(View.MeasureSpec.makeMeasureSpec(size, 1073741824), View.MeasureSpec.makeMeasureSpec(childAt.getLayoutParams().height, 1073741824));
                        }
                    } else {
                        measureChildWithMargins(childAt, i, 0, i2, 0);
                    }
                }
            }
        }

        /* JADX WARN: Removed duplicated region for block: B:27:0x0072  */
        /* JADX WARN: Removed duplicated region for block: B:34:0x008c  */
        /* JADX WARN: Removed duplicated region for block: B:38:0x00a1  */
        /* JADX WARN: Removed duplicated region for block: B:42:0x00b3  */
        /* JADX WARN: Removed duplicated region for block: B:43:0x00bc  */
        @Override // org.telegram.ui.Components.SizeNotifierFrameLayout, android.widget.FrameLayout, android.view.ViewGroup, android.view.View
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public void onLayout(boolean z, int i, int i2, int i3, int i4) {
            int i5;
            int i6;
            int i7;
            int i8;
            int i9;
            int i10;
            int i11;
            int i12;
            int childCount = getChildCount();
            int measureKeyboardHeight = measureKeyboardHeight();
            int emojiPadding = (measureKeyboardHeight > AndroidUtilities.dp(20.0f) || AndroidUtilities.isInMultiwindow || AndroidUtilities.isTablet()) ? 0 : ChannelCreateActivity.this.nameTextView.getEmojiPadding();
            setBottomClip(emojiPadding);
            for (int i13 = 0; i13 < childCount; i13++) {
                View childAt = getChildAt(i13);
                if (childAt.getVisibility() != 8) {
                    FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) childAt.getLayoutParams();
                    int measuredWidth = childAt.getMeasuredWidth();
                    int measuredHeight = childAt.getMeasuredHeight();
                    int i14 = layoutParams.gravity;
                    if (i14 == -1) {
                        i14 = 51;
                    }
                    int i15 = i14 & 7;
                    int i16 = i14 & 112;
                    int i17 = i15 & 7;
                    if (i17 == 1) {
                        i12 = (((i3 - i) - measuredWidth) / 2) + layoutParams.leftMargin;
                        i11 = layoutParams.rightMargin;
                    } else if (i17 == 5) {
                        i12 = i3 - measuredWidth;
                        i11 = layoutParams.rightMargin;
                    } else {
                        i5 = layoutParams.leftMargin;
                        if (i16 == 16) {
                            if (i16 == 48) {
                                i6 = layoutParams.topMargin + getPaddingTop();
                            } else if (i16 == 80) {
                                i9 = ((i4 - emojiPadding) - i2) - measuredHeight;
                                i10 = layoutParams.bottomMargin;
                            } else {
                                i6 = layoutParams.topMargin;
                            }
                            if (ChannelCreateActivity.this.nameTextView != null && ChannelCreateActivity.this.nameTextView.isPopupView(childAt)) {
                                if (!AndroidUtilities.isTablet()) {
                                    i8 = getMeasuredHeight();
                                    i7 = childAt.getMeasuredHeight();
                                } else {
                                    i8 = getMeasuredHeight() + measureKeyboardHeight;
                                    i7 = childAt.getMeasuredHeight();
                                }
                                i6 = i8 - i7;
                            }
                            childAt.layout(i5, i6, measuredWidth + i5, measuredHeight + i6);
                        } else {
                            i9 = ((((i4 - emojiPadding) - i2) - measuredHeight) / 2) + layoutParams.topMargin;
                            i10 = layoutParams.bottomMargin;
                        }
                        i6 = i9 - i10;
                        if (ChannelCreateActivity.this.nameTextView != null) {
                            if (!AndroidUtilities.isTablet()) {
                            }
                            i6 = i8 - i7;
                        }
                        childAt.layout(i5, i6, measuredWidth + i5, measuredHeight + i6);
                    }
                    i5 = i12 - i11;
                    if (i16 == 16) {
                    }
                    i6 = i9 - i10;
                    if (ChannelCreateActivity.this.nameTextView != null) {
                    }
                    childAt.layout(i5, i6, measuredWidth + i5, measuredHeight + i6);
                }
            }
            notifyHeightChanged();
        }

        @Override // android.view.View, android.view.ViewParent
        public void requestLayout() {
            if (this.ignoreLayout) {
                return;
            }
            super.requestLayout();
        }
    }

    /* renamed from: org.telegram.ui.ChannelCreateActivity$3 */
    /* loaded from: classes3.dex */
    class AnonymousClass3 extends BackupImageView {
        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass3(Context context) {
            super(context);
            ChannelCreateActivity.this = r1;
        }

        @Override // android.view.View
        public void invalidate() {
            if (ChannelCreateActivity.this.avatarOverlay != null) {
                ChannelCreateActivity.this.avatarOverlay.invalidate();
            }
            super.invalidate();
        }

        @Override // android.view.View
        public void invalidate(int i, int i2, int i3, int i4) {
            if (ChannelCreateActivity.this.avatarOverlay != null) {
                ChannelCreateActivity.this.avatarOverlay.invalidate();
            }
            super.invalidate(i, i2, i3, i4);
        }
    }

    /* renamed from: org.telegram.ui.ChannelCreateActivity$4 */
    /* loaded from: classes3.dex */
    class AnonymousClass4 extends View {
        final /* synthetic */ Paint val$paint;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass4(Context context, Paint paint) {
            super(context);
            ChannelCreateActivity.this = r1;
            this.val$paint = paint;
        }

        @Override // android.view.View
        protected void onDraw(Canvas canvas) {
            if (ChannelCreateActivity.this.avatarImage == null || !ChannelCreateActivity.this.avatarImage.getImageReceiver().hasNotThumb()) {
                return;
            }
            this.val$paint.setAlpha((int) (ChannelCreateActivity.this.avatarImage.getImageReceiver().getCurrentAlpha() * 85.0f));
            canvas.drawCircle(getMeasuredWidth() / 2.0f, getMeasuredHeight() / 2.0f, getMeasuredWidth() / 2.0f, this.val$paint);
        }
    }

    public /* synthetic */ void lambda$createView$7(View view) {
        this.imageUpdater.openMenu(this.avatar != null, new ChannelCreateActivity$$ExternalSyntheticLambda12(this), new ChannelCreateActivity$$ExternalSyntheticLambda3(this));
        this.cameraDrawable.setCurrentFrame(0);
        this.cameraDrawable.setCustomEndFrame(43);
        this.avatarEditor.playAnimation();
    }

    public /* synthetic */ void lambda$createView$5() {
        this.avatar = null;
        this.avatarBig = null;
        this.inputPhoto = null;
        this.inputVideo = null;
        this.inputVideoPath = null;
        this.videoTimestamp = 0.0d;
        showAvatarProgress(false, true);
        this.avatarImage.setImage((ImageLocation) null, (String) null, this.avatarDrawable, (Object) null);
        this.avatarEditor.setAnimation(this.cameraDrawable);
        this.cameraDrawable.setCurrentFrame(0);
    }

    public /* synthetic */ void lambda$createView$6(DialogInterface dialogInterface) {
        if (!this.imageUpdater.isUploadingImage()) {
            this.cameraDrawable.setCustomEndFrame(86);
            this.avatarEditor.playAnimation();
            return;
        }
        this.cameraDrawable.setCurrentFrame(0, false);
    }

    /* renamed from: org.telegram.ui.ChannelCreateActivity$5 */
    /* loaded from: classes3.dex */
    class AnonymousClass5 extends RLottieImageView {
        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass5(Context context) {
            super(context);
            ChannelCreateActivity.this = r1;
        }

        @Override // android.view.View
        public void invalidate(int i, int i2, int i3, int i4) {
            super.invalidate(i, i2, i3, i4);
            ChannelCreateActivity.this.avatarOverlay.invalidate();
        }

        @Override // android.view.View
        public void invalidate() {
            super.invalidate();
            ChannelCreateActivity.this.avatarOverlay.invalidate();
        }
    }

    public /* synthetic */ boolean lambda$createView$8(TextView textView, int i, KeyEvent keyEvent) {
        if (i != 5 || TextUtils.isEmpty(this.nameTextView.getEditText().getText())) {
            return false;
        }
        this.descriptionTextView.requestFocus();
        return true;
    }

    public /* synthetic */ boolean lambda$createView$9(TextView textView, int i, KeyEvent keyEvent) {
        View view;
        if (i != 6 || (view = this.doneButton) == null) {
            return false;
        }
        view.performClick();
        return true;
    }

    /* renamed from: org.telegram.ui.ChannelCreateActivity$6 */
    /* loaded from: classes3.dex */
    class AnonymousClass6 implements TextWatcher {
        @Override // android.text.TextWatcher
        public void afterTextChanged(Editable editable) {
        }

        @Override // android.text.TextWatcher
        public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
        }

        @Override // android.text.TextWatcher
        public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
        }

        AnonymousClass6(ChannelCreateActivity channelCreateActivity) {
        }
    }

    public /* synthetic */ void lambda$createView$10(View view) {
        if (!this.canCreatePublic) {
            showPremiumIncreaseLimitDialog();
        } else if (!this.isPrivate) {
        } else {
            this.isPrivate = false;
            updatePrivatePublic();
        }
    }

    public /* synthetic */ void lambda$createView$11(View view) {
        if (this.isPrivate) {
            return;
        }
        this.isPrivate = true;
        updatePrivatePublic();
    }

    /* renamed from: org.telegram.ui.ChannelCreateActivity$7 */
    /* loaded from: classes3.dex */
    class AnonymousClass7 implements TextWatcher {
        @Override // android.text.TextWatcher
        public void afterTextChanged(Editable editable) {
        }

        @Override // android.text.TextWatcher
        public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
        }

        AnonymousClass7() {
            ChannelCreateActivity.this = r1;
        }

        @Override // android.text.TextWatcher
        public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            ChannelCreateActivity channelCreateActivity = ChannelCreateActivity.this;
            channelCreateActivity.checkUserName(channelCreateActivity.descriptionTextView.getText().toString());
        }
    }

    private void generateLink() {
        if (this.loadingInvite || this.invite != null) {
            return;
        }
        TLRPC$ChatFull chatFull = getMessagesController().getChatFull(this.chatId);
        if (chatFull != null) {
            this.invite = chatFull.exported_invite;
        }
        if (this.invite != null) {
            return;
        }
        this.loadingInvite = true;
        TLRPC$TL_messages_getExportedChatInvites tLRPC$TL_messages_getExportedChatInvites = new TLRPC$TL_messages_getExportedChatInvites();
        tLRPC$TL_messages_getExportedChatInvites.peer = getMessagesController().getInputPeer(-this.chatId);
        tLRPC$TL_messages_getExportedChatInvites.admin_id = getMessagesController().getInputUser(getUserConfig().getCurrentUser());
        tLRPC$TL_messages_getExportedChatInvites.limit = 1;
        ConnectionsManager.getInstance(this.currentAccount).sendRequest(tLRPC$TL_messages_getExportedChatInvites, new ChannelCreateActivity$$ExternalSyntheticLambda20(this));
    }

    public /* synthetic */ void lambda$generateLink$13(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        AndroidUtilities.runOnUIThread(new ChannelCreateActivity$$ExternalSyntheticLambda19(this, tLRPC$TL_error, tLObject));
    }

    public /* synthetic */ void lambda$generateLink$12(TLRPC$TL_error tLRPC$TL_error, TLObject tLObject) {
        if (tLRPC$TL_error == null) {
            this.invite = (TLRPC$TL_chatInviteExported) ((TLRPC$TL_messages_exportedChatInvites) tLObject).invites.get(0);
        }
        this.loadingInvite = false;
        LinkActionView linkActionView = this.permanentLinkView;
        TLRPC$TL_chatInviteExported tLRPC$TL_chatInviteExported = this.invite;
        linkActionView.setLink(tLRPC$TL_chatInviteExported != null ? tLRPC$TL_chatInviteExported.link : null);
    }

    private void updatePrivatePublic() {
        String str;
        int i;
        String str2;
        int i2;
        if (this.sectionCell == null) {
            return;
        }
        int i3 = 8;
        if (!this.isPrivate && !this.canCreatePublic) {
            this.typeInfoCell.setText(LocaleController.getString("ChangePublicLimitReached", 2131624863));
            this.typeInfoCell.setTag("windowBackgroundWhiteRedText4");
            this.typeInfoCell.setTextColor(Theme.getColor("windowBackgroundWhiteRedText4"));
            this.linkContainer.setVisibility(8);
            this.sectionCell.setVisibility(8);
            if (this.loadingAdminedChannels) {
                this.loadingAdminedCell.setVisibility(0);
                this.adminnedChannelsLayout.setVisibility(8);
                TextInfoPrivacyCell textInfoPrivacyCell = this.typeInfoCell;
                textInfoPrivacyCell.setBackgroundDrawable(Theme.getThemedDrawable(textInfoPrivacyCell.getContext(), 2131165436, "windowBackgroundGrayShadow"));
                this.adminedInfoCell.setVisibility(8);
            } else {
                TextInfoPrivacyCell textInfoPrivacyCell2 = this.typeInfoCell;
                textInfoPrivacyCell2.setBackgroundDrawable(Theme.getThemedDrawable(textInfoPrivacyCell2.getContext(), 2131165435, "windowBackgroundGrayShadow"));
                this.loadingAdminedCell.setVisibility(8);
                this.adminnedChannelsLayout.setVisibility(0);
                this.adminedInfoCell.setVisibility(0);
            }
        } else {
            this.typeInfoCell.setTag("windowBackgroundWhiteGrayText4");
            this.typeInfoCell.setTextColor(Theme.getColor("windowBackgroundWhiteGrayText4"));
            this.sectionCell.setVisibility(0);
            this.adminedInfoCell.setVisibility(8);
            this.adminnedChannelsLayout.setVisibility(8);
            TextInfoPrivacyCell textInfoPrivacyCell3 = this.typeInfoCell;
            textInfoPrivacyCell3.setBackgroundDrawable(Theme.getThemedDrawable(textInfoPrivacyCell3.getContext(), 2131165436, "windowBackgroundGrayShadow"));
            this.linkContainer.setVisibility(0);
            this.loadingAdminedCell.setVisibility(8);
            TextInfoPrivacyCell textInfoPrivacyCell4 = this.typeInfoCell;
            if (this.isPrivate) {
                i = 2131624948;
                str = "ChannelPrivateLinkHelp";
            } else {
                i = 2131624981;
                str = "ChannelUsernameHelp";
            }
            textInfoPrivacyCell4.setText(LocaleController.getString(str, i));
            HeaderCell headerCell = this.headerCell;
            if (this.isPrivate) {
                i2 = 2131624905;
                str2 = "ChannelInviteLinkTitle";
            } else {
                i2 = 2131624915;
                str2 = "ChannelLinkTitle";
            }
            headerCell.setText(LocaleController.getString(str2, i2));
            this.publicContainer.setVisibility(this.isPrivate ? 8 : 0);
            this.privateContainer.setVisibility(this.isPrivate ? 0 : 8);
            this.linkContainer.setPadding(0, 0, 0, this.isPrivate ? 0 : AndroidUtilities.dp(7.0f));
            LinkActionView linkActionView = this.permanentLinkView;
            TLRPC$TL_chatInviteExported tLRPC$TL_chatInviteExported = this.invite;
            linkActionView.setLink(tLRPC$TL_chatInviteExported != null ? tLRPC$TL_chatInviteExported.link : null);
            TextView textView = this.checkTextView;
            if (!this.isPrivate && textView.length() != 0) {
                i3 = 0;
            }
            textView.setVisibility(i3);
        }
        this.radioButtonCell1.setChecked(!this.isPrivate, true);
        this.radioButtonCell2.setChecked(this.isPrivate, true);
        this.descriptionTextView.clearFocus();
        AndroidUtilities.hideKeyboard(this.descriptionTextView);
    }

    @Override // org.telegram.ui.Components.ImageUpdater.ImageUpdaterDelegate
    public void onUploadProgressChanged(float f) {
        RadialProgressView radialProgressView = this.avatarProgressView;
        if (radialProgressView == null) {
            return;
        }
        radialProgressView.setProgress(f);
    }

    @Override // org.telegram.ui.Components.ImageUpdater.ImageUpdaterDelegate
    public void didStartUpload(boolean z) {
        RadialProgressView radialProgressView = this.avatarProgressView;
        if (radialProgressView == null) {
            return;
        }
        radialProgressView.setProgress(0.0f);
    }

    @Override // org.telegram.ui.Components.ImageUpdater.ImageUpdaterDelegate
    public void didUploadPhoto(TLRPC$InputFile tLRPC$InputFile, TLRPC$InputFile tLRPC$InputFile2, double d, String str, TLRPC$PhotoSize tLRPC$PhotoSize, TLRPC$PhotoSize tLRPC$PhotoSize2) {
        AndroidUtilities.runOnUIThread(new ChannelCreateActivity$$ExternalSyntheticLambda17(this, tLRPC$InputFile, tLRPC$InputFile2, str, d, tLRPC$PhotoSize2, tLRPC$PhotoSize));
    }

    public /* synthetic */ void lambda$didUploadPhoto$14(TLRPC$InputFile tLRPC$InputFile, TLRPC$InputFile tLRPC$InputFile2, String str, double d, TLRPC$PhotoSize tLRPC$PhotoSize, TLRPC$PhotoSize tLRPC$PhotoSize2) {
        if (tLRPC$InputFile != null || tLRPC$InputFile2 != null) {
            this.inputPhoto = tLRPC$InputFile;
            this.inputVideo = tLRPC$InputFile2;
            this.inputVideoPath = str;
            this.videoTimestamp = d;
            if (this.createAfterUpload) {
                AlertDialog alertDialog = this.cancelDialog;
                if (alertDialog != null) {
                    try {
                        alertDialog.dismiss();
                        this.cancelDialog = null;
                    } catch (Exception e) {
                        FileLog.e(e);
                    }
                }
                updateDoneProgress(false);
                this.donePressed = false;
                this.doneButton.performClick();
            }
            showAvatarProgress(false, true);
            return;
        }
        TLRPC$FileLocation tLRPC$FileLocation = tLRPC$PhotoSize.location;
        this.avatar = tLRPC$FileLocation;
        this.avatarBig = tLRPC$PhotoSize2.location;
        this.avatarImage.setImage(ImageLocation.getForLocal(tLRPC$FileLocation), "50_50", this.avatarDrawable, (Object) null);
        showAvatarProgress(true, false);
    }

    @Override // org.telegram.ui.Components.ImageUpdater.ImageUpdaterDelegate
    public String getInitialSearchString() {
        return this.nameTextView.getText().toString();
    }

    private void showAvatarProgress(boolean z, boolean z2) {
        if (this.avatarEditor == null) {
            return;
        }
        AnimatorSet animatorSet = this.avatarAnimation;
        if (animatorSet != null) {
            animatorSet.removeAllListeners();
            this.avatarAnimation.cancel();
            this.avatarAnimation = null;
        }
        if (!z2) {
            if (z) {
                this.avatarEditor.setAlpha(1.0f);
                this.avatarEditor.setVisibility(4);
                this.avatarProgressView.setAlpha(1.0f);
                this.avatarProgressView.setVisibility(0);
                return;
            }
            this.avatarEditor.setAlpha(1.0f);
            this.avatarEditor.setVisibility(0);
            this.avatarProgressView.setAlpha(0.0f);
            this.avatarProgressView.setVisibility(4);
            return;
        }
        this.avatarAnimation = new AnimatorSet();
        if (z) {
            this.avatarProgressView.setVisibility(0);
            this.avatarAnimation.playTogether(ObjectAnimator.ofFloat(this.avatarEditor, View.ALPHA, 0.0f), ObjectAnimator.ofFloat(this.avatarProgressView, View.ALPHA, 1.0f));
        } else {
            if (this.avatarEditor.getVisibility() != 0) {
                this.avatarEditor.setAlpha(0.0f);
            }
            this.avatarEditor.setVisibility(0);
            this.avatarAnimation.playTogether(ObjectAnimator.ofFloat(this.avatarEditor, View.ALPHA, 1.0f), ObjectAnimator.ofFloat(this.avatarProgressView, View.ALPHA, 0.0f));
        }
        this.avatarAnimation.setDuration(180L);
        this.avatarAnimation.addListener(new AnonymousClass8(z));
        this.avatarAnimation.start();
    }

    /* renamed from: org.telegram.ui.ChannelCreateActivity$8 */
    /* loaded from: classes3.dex */
    public class AnonymousClass8 extends AnimatorListenerAdapter {
        final /* synthetic */ boolean val$show;

        AnonymousClass8(boolean z) {
            ChannelCreateActivity.this = r1;
            this.val$show = z;
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator) {
            if (ChannelCreateActivity.this.avatarAnimation == null || ChannelCreateActivity.this.avatarEditor == null) {
                return;
            }
            if (this.val$show) {
                ChannelCreateActivity.this.avatarEditor.setVisibility(4);
            } else {
                ChannelCreateActivity.this.avatarProgressView.setVisibility(4);
            }
            ChannelCreateActivity.this.avatarAnimation = null;
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationCancel(Animator animator) {
            ChannelCreateActivity.this.avatarAnimation = null;
        }
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public void onActivityResultFragment(int i, int i2, Intent intent) {
        ImageUpdater imageUpdater = this.imageUpdater;
        if (imageUpdater != null) {
            imageUpdater.onActivityResult(i, i2, intent);
        }
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public void saveSelfArgs(Bundle bundle) {
        String str;
        if (this.currentStep == 0) {
            ImageUpdater imageUpdater = this.imageUpdater;
            if (imageUpdater != null && (str = imageUpdater.currentPicturePath) != null) {
                bundle.putString("path", str);
            }
            EditTextEmoji editTextEmoji = this.nameTextView;
            if (editTextEmoji == null) {
                return;
            }
            String obj = editTextEmoji.getText().toString();
            if (obj.length() == 0) {
                return;
            }
            bundle.putString("nameTextView", obj);
        }
    }

    public void restoreSelfArgs(Bundle bundle) {
        if (this.currentStep == 0) {
            ImageUpdater imageUpdater = this.imageUpdater;
            if (imageUpdater != null) {
                imageUpdater.currentPicturePath = bundle.getString("path");
            }
            String string = bundle.getString("nameTextView");
            if (string == null) {
                return;
            }
            EditTextEmoji editTextEmoji = this.nameTextView;
            if (editTextEmoji != null) {
                editTextEmoji.setText(string);
            } else {
                this.nameToSet = string;
            }
        }
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public void onTransitionAnimationEnd(boolean z, boolean z2) {
        if (!z || this.currentStep == 1) {
            return;
        }
        this.nameTextView.requestFocus();
        this.nameTextView.openKeyboard();
    }

    @Override // org.telegram.messenger.NotificationCenter.NotificationCenterDelegate
    public void didReceivedNotification(int i, int i2, Object... objArr) {
        if (i == NotificationCenter.chatDidFailCreate) {
            AlertDialog alertDialog = this.cancelDialog;
            if (alertDialog != null) {
                try {
                    alertDialog.dismiss();
                    this.cancelDialog = null;
                } catch (Exception e) {
                    FileLog.e(e);
                }
            }
            updateDoneProgress(false);
            this.donePressed = false;
        } else if (i != NotificationCenter.chatDidCreated) {
        } else {
            AlertDialog alertDialog2 = this.cancelDialog;
            if (alertDialog2 != null) {
                try {
                    alertDialog2.dismiss();
                    this.cancelDialog = null;
                } catch (Exception e2) {
                    FileLog.e(e2);
                }
            }
            long longValue = ((Long) objArr[0]).longValue();
            Bundle bundle = new Bundle();
            bundle.putInt("step", 1);
            bundle.putLong("chat_id", longValue);
            bundle.putBoolean("canCreatePublic", this.canCreatePublic);
            if (this.inputPhoto != null || this.inputVideo != null) {
                MessagesController.getInstance(this.currentAccount).changeChatAvatar(longValue, null, this.inputPhoto, this.inputVideo, this.videoTimestamp, this.inputVideoPath, this.avatar, this.avatarBig, null);
            }
            presentFragment(new ChannelCreateActivity(bundle), true);
        }
    }

    private void loadAdminedChannels() {
        if (this.loadingAdminedChannels) {
            return;
        }
        this.loadingAdminedChannels = true;
        updatePrivatePublic();
        ConnectionsManager.getInstance(this.currentAccount).sendRequest(new TLRPC$TL_channels_getAdminedPublicChannels(), new ChannelCreateActivity$$ExternalSyntheticLambda22(this));
    }

    public /* synthetic */ void lambda$loadAdminedChannels$20(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        AndroidUtilities.runOnUIThread(new ChannelCreateActivity$$ExternalSyntheticLambda16(this, tLObject));
    }

    public /* synthetic */ void lambda$loadAdminedChannels$19(TLObject tLObject) {
        this.loadingAdminedChannels = false;
        if (tLObject == null || getParentActivity() == null) {
            return;
        }
        for (int i = 0; i < this.adminedChannelCells.size(); i++) {
            this.linearLayout.removeView(this.adminedChannelCells.get(i));
        }
        this.adminedChannelCells.clear();
        TLRPC$TL_messages_chats tLRPC$TL_messages_chats = (TLRPC$TL_messages_chats) tLObject;
        for (int i2 = 0; i2 < tLRPC$TL_messages_chats.chats.size(); i2++) {
            AdminedChannelCell adminedChannelCell = new AdminedChannelCell(getParentActivity(), new ChannelCreateActivity$$ExternalSyntheticLambda5(this), false, 0);
            TLRPC$Chat tLRPC$Chat = tLRPC$TL_messages_chats.chats.get(i2);
            boolean z = true;
            if (i2 != tLRPC$TL_messages_chats.chats.size() - 1) {
                z = false;
            }
            adminedChannelCell.setChannel(tLRPC$Chat, z);
            this.adminedChannelCells.add(adminedChannelCell);
            this.adminnedChannelsLayout.addView(adminedChannelCell, LayoutHelper.createLinear(-1, 72));
        }
        updatePrivatePublic();
    }

    public /* synthetic */ void lambda$loadAdminedChannels$18(View view) {
        TLRPC$Chat currentChannel = ((AdminedChannelCell) view.getParent()).getCurrentChannel();
        AlertDialog.Builder builder = new AlertDialog.Builder(getParentActivity());
        builder.setTitle(LocaleController.getString("AppName", 2131624375));
        if (currentChannel.megagroup) {
            builder.setMessage(AndroidUtilities.replaceTags(LocaleController.formatString("RevokeLinkAlert", 2131628044, MessagesController.getInstance(this.currentAccount).linkPrefix + "/" + currentChannel.username, currentChannel.title)));
        } else {
            builder.setMessage(AndroidUtilities.replaceTags(LocaleController.formatString("RevokeLinkAlertChannel", 2131628045, MessagesController.getInstance(this.currentAccount).linkPrefix + "/" + currentChannel.username, currentChannel.title)));
        }
        builder.setNegativeButton(LocaleController.getString("Cancel", 2131624819), null);
        builder.setPositiveButton(LocaleController.getString("RevokeButton", 2131628042), new ChannelCreateActivity$$ExternalSyntheticLambda2(this, currentChannel));
        showDialog(builder.create());
    }

    public /* synthetic */ void lambda$loadAdminedChannels$17(TLRPC$Chat tLRPC$Chat, DialogInterface dialogInterface, int i) {
        TLRPC$TL_channels_updateUsername tLRPC$TL_channels_updateUsername = new TLRPC$TL_channels_updateUsername();
        tLRPC$TL_channels_updateUsername.channel = MessagesController.getInputChannel(tLRPC$Chat);
        tLRPC$TL_channels_updateUsername.username = "";
        ConnectionsManager.getInstance(this.currentAccount).sendRequest(tLRPC$TL_channels_updateUsername, new ChannelCreateActivity$$ExternalSyntheticLambda21(this), 64);
    }

    public /* synthetic */ void lambda$loadAdminedChannels$16(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        if (tLObject instanceof TLRPC$TL_boolTrue) {
            AndroidUtilities.runOnUIThread(new ChannelCreateActivity$$ExternalSyntheticLambda13(this));
        }
    }

    public /* synthetic */ void lambda$loadAdminedChannels$15() {
        this.canCreatePublic = true;
        if (this.descriptionTextView.length() > 0) {
            checkUserName(this.descriptionTextView.getText().toString());
        }
        updatePrivatePublic();
    }

    public boolean checkUserName(String str) {
        if (str != null && str.length() > 0) {
            this.checkTextView.setVisibility(0);
        } else {
            this.checkTextView.setVisibility(8);
        }
        Runnable runnable = this.checkRunnable;
        if (runnable != null) {
            AndroidUtilities.cancelRunOnUIThread(runnable);
            this.checkRunnable = null;
            this.lastCheckName = null;
            if (this.checkReqId != 0) {
                ConnectionsManager.getInstance(this.currentAccount).cancelRequest(this.checkReqId, true);
            }
        }
        this.lastNameAvailable = false;
        if (str != null) {
            if (str.startsWith("_") || str.endsWith("_")) {
                this.checkTextView.setText(LocaleController.getString("LinkInvalid", 2131626444));
                this.checkTextView.setTag("windowBackgroundWhiteRedText4");
                this.checkTextView.setTextColor(Theme.getColor("windowBackgroundWhiteRedText4"));
                return false;
            }
            for (int i = 0; i < str.length(); i++) {
                char charAt = str.charAt(i);
                if (i == 0 && charAt >= '0' && charAt <= '9') {
                    this.checkTextView.setText(LocaleController.getString("LinkInvalidStartNumber", 2131626448));
                    this.checkTextView.setTag("windowBackgroundWhiteRedText4");
                    this.checkTextView.setTextColor(Theme.getColor("windowBackgroundWhiteRedText4"));
                    return false;
                } else if ((charAt < '0' || charAt > '9') && ((charAt < 'a' || charAt > 'z') && ((charAt < 'A' || charAt > 'Z') && charAt != '_'))) {
                    this.checkTextView.setText(LocaleController.getString("LinkInvalid", 2131626444));
                    this.checkTextView.setTag("windowBackgroundWhiteRedText4");
                    this.checkTextView.setTextColor(Theme.getColor("windowBackgroundWhiteRedText4"));
                    return false;
                }
            }
        }
        if (str == null || str.length() < 5) {
            this.checkTextView.setText(LocaleController.getString("LinkInvalidShort", 2131626446));
            this.checkTextView.setTag("windowBackgroundWhiteRedText4");
            this.checkTextView.setTextColor(Theme.getColor("windowBackgroundWhiteRedText4"));
            return false;
        } else if (str.length() > 32) {
            this.checkTextView.setText(LocaleController.getString("LinkInvalidLong", 2131626445));
            this.checkTextView.setTag("windowBackgroundWhiteRedText4");
            this.checkTextView.setTextColor(Theme.getColor("windowBackgroundWhiteRedText4"));
            return false;
        } else {
            this.checkTextView.setText(LocaleController.getString("LinkChecking", 2131626432));
            this.checkTextView.setTag("windowBackgroundWhiteGrayText8");
            this.checkTextView.setTextColor(Theme.getColor("windowBackgroundWhiteGrayText8"));
            this.lastCheckName = str;
            ChannelCreateActivity$$ExternalSyntheticLambda14 channelCreateActivity$$ExternalSyntheticLambda14 = new ChannelCreateActivity$$ExternalSyntheticLambda14(this, str);
            this.checkRunnable = channelCreateActivity$$ExternalSyntheticLambda14;
            AndroidUtilities.runOnUIThread(channelCreateActivity$$ExternalSyntheticLambda14, 300L);
            return true;
        }
    }

    public /* synthetic */ void lambda$checkUserName$23(String str) {
        TLRPC$TL_channels_checkUsername tLRPC$TL_channels_checkUsername = new TLRPC$TL_channels_checkUsername();
        tLRPC$TL_channels_checkUsername.username = str;
        tLRPC$TL_channels_checkUsername.channel = MessagesController.getInstance(this.currentAccount).getInputChannel(this.chatId);
        this.checkReqId = ConnectionsManager.getInstance(this.currentAccount).sendRequest(tLRPC$TL_channels_checkUsername, new ChannelCreateActivity$$ExternalSyntheticLambda24(this, str), 2);
    }

    public /* synthetic */ void lambda$checkUserName$22(String str, TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        AndroidUtilities.runOnUIThread(new ChannelCreateActivity$$ExternalSyntheticLambda15(this, str, tLRPC$TL_error, tLObject));
    }

    public /* synthetic */ void lambda$checkUserName$21(String str, TLRPC$TL_error tLRPC$TL_error, TLObject tLObject) {
        this.checkReqId = 0;
        String str2 = this.lastCheckName;
        if (str2 == null || !str2.equals(str)) {
            return;
        }
        if (tLRPC$TL_error == null && (tLObject instanceof TLRPC$TL_boolTrue)) {
            this.checkTextView.setText(LocaleController.formatString("LinkAvailable", 2131626431, str));
            this.checkTextView.setTag("windowBackgroundWhiteGreenText");
            this.checkTextView.setTextColor(Theme.getColor("windowBackgroundWhiteGreenText"));
            this.lastNameAvailable = true;
            return;
        }
        if (tLRPC$TL_error != null && tLRPC$TL_error.text.equals("CHANNELS_ADMIN_PUBLIC_TOO_MUCH")) {
            this.canCreatePublic = false;
            showPremiumIncreaseLimitDialog();
        } else {
            this.checkTextView.setText(LocaleController.getString("LinkInUse", 2131626441));
        }
        this.checkTextView.setTag("windowBackgroundWhiteRedText4");
        this.checkTextView.setTextColor(Theme.getColor("windowBackgroundWhiteRedText4"));
        this.lastNameAvailable = false;
    }

    private void showPremiumIncreaseLimitDialog() {
        if (getParentActivity() == null) {
            return;
        }
        LimitReachedBottomSheet limitReachedBottomSheet = new LimitReachedBottomSheet(this, getParentActivity(), 2, this.currentAccount);
        limitReachedBottomSheet.parentIsChannel = true;
        limitReachedBottomSheet.onSuccessRunnable = new ChannelCreateActivity$$ExternalSyntheticLambda11(this);
        showDialog(limitReachedBottomSheet);
    }

    public /* synthetic */ void lambda$showPremiumIncreaseLimitDialog$24() {
        this.canCreatePublic = true;
        updatePrivatePublic();
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public ArrayList<ThemeDescription> getThemeDescriptions() {
        ArrayList<ThemeDescription> arrayList = new ArrayList<>();
        ChannelCreateActivity$$ExternalSyntheticLambda25 channelCreateActivity$$ExternalSyntheticLambda25 = new ChannelCreateActivity$$ExternalSyntheticLambda25(this);
        arrayList.add(new ThemeDescription(this.fragmentView, ThemeDescription.FLAG_CHECKTAG | ThemeDescription.FLAG_BACKGROUND, null, null, null, null, "windowBackgroundWhite"));
        arrayList.add(new ThemeDescription(this.fragmentView, ThemeDescription.FLAG_BACKGROUND | ThemeDescription.FLAG_CHECKTAG, null, null, null, null, "windowBackgroundGray"));
        arrayList.add(new ThemeDescription(this.actionBar, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, "actionBarDefault"));
        arrayList.add(new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_ITEMSCOLOR, null, null, null, null, "actionBarDefaultIcon"));
        arrayList.add(new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_TITLECOLOR, null, null, null, null, "actionBarDefaultTitle"));
        arrayList.add(new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_SELECTORCOLOR, null, null, null, null, "actionBarDefaultSelector"));
        arrayList.add(new ThemeDescription(this.nameTextView, ThemeDescription.FLAG_TEXTCOLOR, null, null, null, null, "windowBackgroundWhiteBlackText"));
        arrayList.add(new ThemeDescription(this.nameTextView, ThemeDescription.FLAG_HINTTEXTCOLOR, null, null, null, null, "windowBackgroundWhiteHintText"));
        arrayList.add(new ThemeDescription(this.nameTextView, ThemeDescription.FLAG_BACKGROUNDFILTER, null, null, null, null, "windowBackgroundWhiteInputField"));
        arrayList.add(new ThemeDescription(this.nameTextView, ThemeDescription.FLAG_BACKGROUNDFILTER | ThemeDescription.FLAG_DRAWABLESELECTEDSTATE, null, null, null, null, "windowBackgroundWhiteInputFieldActivated"));
        arrayList.add(new ThemeDescription(this.descriptionTextView, ThemeDescription.FLAG_TEXTCOLOR, null, null, null, null, "windowBackgroundWhiteBlackText"));
        arrayList.add(new ThemeDescription(this.descriptionTextView, ThemeDescription.FLAG_HINTTEXTCOLOR, null, null, null, null, "windowBackgroundWhiteHintText"));
        arrayList.add(new ThemeDescription(this.descriptionTextView, ThemeDescription.FLAG_BACKGROUNDFILTER, null, null, null, null, "windowBackgroundWhiteInputField"));
        arrayList.add(new ThemeDescription(this.descriptionTextView, ThemeDescription.FLAG_BACKGROUNDFILTER | ThemeDescription.FLAG_DRAWABLESELECTEDSTATE, null, null, null, null, "windowBackgroundWhiteInputFieldActivated"));
        arrayList.add(new ThemeDescription(this.helpTextView, ThemeDescription.FLAG_TEXTCOLOR, null, null, null, null, "windowBackgroundWhiteGrayText8"));
        arrayList.add(new ThemeDescription(this.linearLayout2, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, "windowBackgroundWhite"));
        arrayList.add(new ThemeDescription(this.linkContainer, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, "windowBackgroundWhite"));
        arrayList.add(new ThemeDescription(this.sectionCell, ThemeDescription.FLAG_BACKGROUNDFILTER, null, null, null, null, "windowBackgroundGrayShadow"));
        arrayList.add(new ThemeDescription(this.headerCell, 0, new Class[]{HeaderCell.class}, new String[]{"textView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "windowBackgroundWhiteBlueHeader"));
        arrayList.add(new ThemeDescription(this.headerCell2, 0, new Class[]{HeaderCell.class}, new String[]{"textView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "windowBackgroundWhiteBlueHeader"));
        arrayList.add(new ThemeDescription(this.editText, ThemeDescription.FLAG_TEXTCOLOR, null, null, null, null, "windowBackgroundWhiteBlackText"));
        arrayList.add(new ThemeDescription(this.editText, ThemeDescription.FLAG_HINTTEXTCOLOR, null, null, null, null, "windowBackgroundWhiteHintText"));
        arrayList.add(new ThemeDescription(this.checkTextView, ThemeDescription.FLAG_TEXTCOLOR | ThemeDescription.FLAG_CHECKTAG, null, null, null, null, "windowBackgroundWhiteRedText4"));
        arrayList.add(new ThemeDescription(this.checkTextView, ThemeDescription.FLAG_TEXTCOLOR | ThemeDescription.FLAG_CHECKTAG, null, null, null, null, "windowBackgroundWhiteGrayText8"));
        arrayList.add(new ThemeDescription(this.checkTextView, ThemeDescription.FLAG_TEXTCOLOR | ThemeDescription.FLAG_CHECKTAG, null, null, null, null, "windowBackgroundWhiteGreenText"));
        arrayList.add(new ThemeDescription(this.typeInfoCell, ThemeDescription.FLAG_BACKGROUNDFILTER, new Class[]{TextInfoPrivacyCell.class}, null, null, null, "windowBackgroundGrayShadow"));
        arrayList.add(new ThemeDescription(this.typeInfoCell, ThemeDescription.FLAG_CHECKTAG, new Class[]{TextInfoPrivacyCell.class}, new String[]{"textView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "windowBackgroundWhiteGrayText4"));
        arrayList.add(new ThemeDescription(this.typeInfoCell, ThemeDescription.FLAG_CHECKTAG, new Class[]{TextInfoPrivacyCell.class}, new String[]{"textView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "windowBackgroundWhiteRedText4"));
        arrayList.add(new ThemeDescription(this.adminedInfoCell, ThemeDescription.FLAG_BACKGROUNDFILTER, new Class[]{TextInfoPrivacyCell.class}, null, null, null, "windowBackgroundGrayShadow"));
        arrayList.add(new ThemeDescription(this.adminnedChannelsLayout, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, "windowBackgroundWhite"));
        arrayList.add(new ThemeDescription(this.privateContainer, ThemeDescription.FLAG_SELECTOR, null, null, null, null, "listSelectorSDK21"));
        arrayList.add(new ThemeDescription(this.privateContainer, 0, new Class[]{TextBlockCell.class}, new String[]{"textView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "windowBackgroundWhiteBlackText"));
        arrayList.add(new ThemeDescription(this.loadingAdminedCell, 0, new Class[]{LoadingCell.class}, new String[]{"progressBar"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "progressCircle"));
        arrayList.add(new ThemeDescription(this.radioButtonCell1, ThemeDescription.FLAG_SELECTOR, null, null, null, null, "listSelectorSDK21"));
        arrayList.add(new ThemeDescription(this.radioButtonCell1, ThemeDescription.FLAG_CHECKBOX, new Class[]{RadioButtonCell.class}, new String[]{"radioButton"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "radioBackground"));
        arrayList.add(new ThemeDescription(this.radioButtonCell1, ThemeDescription.FLAG_CHECKBOXCHECK, new Class[]{RadioButtonCell.class}, new String[]{"radioButton"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "radioBackgroundChecked"));
        arrayList.add(new ThemeDescription(this.radioButtonCell1, ThemeDescription.FLAG_TEXTCOLOR, new Class[]{RadioButtonCell.class}, new String[]{"textView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "windowBackgroundWhiteBlackText"));
        arrayList.add(new ThemeDescription(this.radioButtonCell1, ThemeDescription.FLAG_TEXTCOLOR, new Class[]{RadioButtonCell.class}, new String[]{"valueTextView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "windowBackgroundWhiteGrayText2"));
        arrayList.add(new ThemeDescription(this.radioButtonCell2, ThemeDescription.FLAG_SELECTOR, null, null, null, null, "listSelectorSDK21"));
        arrayList.add(new ThemeDescription(this.radioButtonCell2, ThemeDescription.FLAG_CHECKBOX, new Class[]{RadioButtonCell.class}, new String[]{"radioButton"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "radioBackground"));
        arrayList.add(new ThemeDescription(this.radioButtonCell2, ThemeDescription.FLAG_CHECKBOXCHECK, new Class[]{RadioButtonCell.class}, new String[]{"radioButton"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "radioBackgroundChecked"));
        arrayList.add(new ThemeDescription(this.radioButtonCell2, ThemeDescription.FLAG_TEXTCOLOR, new Class[]{RadioButtonCell.class}, new String[]{"textView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "windowBackgroundWhiteBlackText"));
        arrayList.add(new ThemeDescription(this.radioButtonCell2, ThemeDescription.FLAG_TEXTCOLOR, new Class[]{RadioButtonCell.class}, new String[]{"valueTextView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "windowBackgroundWhiteGrayText2"));
        arrayList.add(new ThemeDescription(this.adminnedChannelsLayout, ThemeDescription.FLAG_TEXTCOLOR, new Class[]{AdminedChannelCell.class}, new String[]{"nameTextView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "windowBackgroundWhiteBlackText"));
        arrayList.add(new ThemeDescription(this.adminnedChannelsLayout, ThemeDescription.FLAG_TEXTCOLOR, new Class[]{AdminedChannelCell.class}, new String[]{"statusTextView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "windowBackgroundWhiteGrayText"));
        arrayList.add(new ThemeDescription(this.adminnedChannelsLayout, ThemeDescription.FLAG_LINKCOLOR, new Class[]{AdminedChannelCell.class}, new String[]{"statusTextView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "windowBackgroundWhiteLinkText"));
        arrayList.add(new ThemeDescription(this.adminnedChannelsLayout, ThemeDescription.FLAG_IMAGECOLOR, new Class[]{AdminedChannelCell.class}, new String[]{"deleteButton"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "windowBackgroundWhiteGrayText"));
        arrayList.add(new ThemeDescription(null, 0, null, null, Theme.avatarDrawables, channelCreateActivity$$ExternalSyntheticLambda25, "avatar_text"));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, channelCreateActivity$$ExternalSyntheticLambda25, "avatar_backgroundRed"));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, channelCreateActivity$$ExternalSyntheticLambda25, "avatar_backgroundOrange"));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, channelCreateActivity$$ExternalSyntheticLambda25, "avatar_backgroundViolet"));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, channelCreateActivity$$ExternalSyntheticLambda25, "avatar_backgroundGreen"));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, channelCreateActivity$$ExternalSyntheticLambda25, "avatar_backgroundCyan"));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, channelCreateActivity$$ExternalSyntheticLambda25, "avatar_backgroundBlue"));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, channelCreateActivity$$ExternalSyntheticLambda25, "avatar_backgroundPink"));
        return arrayList;
    }

    public /* synthetic */ void lambda$getThemeDescriptions$25() {
        LinearLayout linearLayout = this.adminnedChannelsLayout;
        if (linearLayout != null) {
            int childCount = linearLayout.getChildCount();
            for (int i = 0; i < childCount; i++) {
                View childAt = this.adminnedChannelsLayout.getChildAt(i);
                if (childAt instanceof AdminedChannelCell) {
                    ((AdminedChannelCell) childAt).update();
                }
            }
        }
    }
}