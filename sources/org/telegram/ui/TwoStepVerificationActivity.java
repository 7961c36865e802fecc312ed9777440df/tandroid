package org.telegram.ui;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.StateListAnimator;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.graphics.Outline;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.util.Property;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewOutlineProvider;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import androidx.core.graphics.ColorUtils;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.Locale;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.R;
import org.telegram.messenger.SRPHelper;
import org.telegram.messenger.UserConfig;
import org.telegram.messenger.Utilities;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;
import org.telegram.tgnet.tl.TL_account;
import org.telegram.ui.ActionBar.ActionBar;
import org.telegram.ui.ActionBar.AlertDialog;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.ActionBar.SimpleTextView;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.ActionBar.ThemeDescription;
import org.telegram.ui.Cells.EditTextSettingsCell;
import org.telegram.ui.Cells.TextInfoPrivacyCell;
import org.telegram.ui.Cells.TextSettingsCell;
import org.telegram.ui.Components.AlertsCreator;
import org.telegram.ui.Components.CombinedDrawable;
import org.telegram.ui.Components.CubicBezierInterpolator;
import org.telegram.ui.Components.EditTextBoldCursor;
import org.telegram.ui.Components.EmptyTextProgressView;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.ui.Components.OutlineTextContainerView;
import org.telegram.ui.Components.RLottieImageView;
import org.telegram.ui.Components.RadialProgressView;
import org.telegram.ui.Components.RecyclerListView;
import org.telegram.ui.Components.TransformableLoginButtonView;
import org.telegram.ui.Components.VerticalPositionAutoAnimator;

/* loaded from: classes4.dex */
public class TwoStepVerificationActivity extends BaseFragment implements NotificationCenter.NotificationCenterDelegate {
    private SimpleTextView bottomButton;
    private TextView bottomTextView;
    private TextView cancelResetButton;
    private int changePasswordRow;
    private int changeRecoveryEmailRow;
    private TL_account.Password currentPassword;
    private byte[] currentSecret;
    private long currentSecretId;
    private TwoStepVerificationActivityDelegate delegate;
    private int delegateType;
    private String delegateTypeString;
    private boolean destroyed;
    private EmptyTextProgressView emptyView;
    private FrameLayout floatingButtonContainer;
    private TransformableLoginButtonView floatingButtonIcon;
    private boolean forgotPasswordOnShow;
    private ListAdapter listAdapter;
    private RecyclerListView listView;
    private boolean loading;
    private RLottieImageView lockImageView;
    private EditTextBoldCursor passwordEditText;
    private int passwordEnabledDetailRow;
    private OutlineTextContainerView passwordOutlineView;
    private boolean paused;
    private boolean postedErrorColorTimeout;
    public boolean preloaded;
    private AlertDialog progressDialog;
    private RadialProgressView radialProgressView;
    private boolean resetPasswordOnShow;
    private TextView resetWaitView;
    private int rowCount;
    private ScrollView scrollView;
    private int setPasswordDetailRow;
    private int setPasswordRow;
    private int setRecoveryEmailRow;
    private TextView subtitleTextView;
    private TextView titleTextView;
    private int turnPasswordOffRow;
    private boolean passwordEntered = true;
    private byte[] currentPasswordHash = new byte[0];
    private Runnable errorColorTimeout = new Runnable() { // from class: org.telegram.ui.TwoStepVerificationActivity$$ExternalSyntheticLambda11
        @Override // java.lang.Runnable
        public final void run() {
            TwoStepVerificationActivity.this.lambda$new$0();
        }
    };
    int otherwiseReloginDays = -1;
    private Runnable updateTimeRunnable = new Runnable() { // from class: org.telegram.ui.TwoStepVerificationActivity$$ExternalSyntheticLambda12
        @Override // java.lang.Runnable
        public final void run() {
            TwoStepVerificationActivity.this.updateBottomButton();
        }
    };

    private class ListAdapter extends RecyclerListView.SelectionAdapter {
        private Context mContext;

        public ListAdapter(Context context) {
            this.mContext = context;
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public int getItemCount() {
            if (TwoStepVerificationActivity.this.loading || TwoStepVerificationActivity.this.currentPassword == null) {
                return 0;
            }
            return TwoStepVerificationActivity.this.rowCount;
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public int getItemViewType(int i) {
            return (i == TwoStepVerificationActivity.this.setPasswordDetailRow || i == TwoStepVerificationActivity.this.passwordEnabledDetailRow) ? 1 : 0;
        }

        @Override // org.telegram.ui.Components.RecyclerListView.SelectionAdapter
        public boolean isEnabled(RecyclerView.ViewHolder viewHolder) {
            return viewHolder.getItemViewType() == 0;
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
            int i2;
            int i3;
            int itemViewType = viewHolder.getItemViewType();
            boolean z = true;
            if (itemViewType != 0) {
                if (itemViewType != 1) {
                    return;
                }
                TextInfoPrivacyCell textInfoPrivacyCell = (TextInfoPrivacyCell) viewHolder.itemView;
                if (i == TwoStepVerificationActivity.this.setPasswordDetailRow) {
                    i3 = R.string.SetAdditionalPasswordInfo;
                } else if (i != TwoStepVerificationActivity.this.passwordEnabledDetailRow) {
                    return;
                } else {
                    i3 = R.string.EnabledPasswordText;
                }
                textInfoPrivacyCell.setText(LocaleController.getString(i3));
                textInfoPrivacyCell.setBackgroundDrawable(Theme.getThemedDrawableByKey(this.mContext, R.drawable.greydivider_bottom, Theme.key_windowBackgroundGrayShadow));
                return;
            }
            TextSettingsCell textSettingsCell = (TextSettingsCell) viewHolder.itemView;
            int i4 = Theme.key_windowBackgroundWhiteBlackText;
            textSettingsCell.setTag(Integer.valueOf(i4));
            textSettingsCell.setTextColor(Theme.getColor(i4));
            if (i == TwoStepVerificationActivity.this.changePasswordRow) {
                i2 = R.string.ChangePassword;
            } else if (i == TwoStepVerificationActivity.this.setPasswordRow) {
                i2 = R.string.SetAdditionalPassword;
            } else if (i == TwoStepVerificationActivity.this.turnPasswordOffRow) {
                i2 = R.string.TurnPasswordOff;
            } else {
                z = false;
                if (i == TwoStepVerificationActivity.this.changeRecoveryEmailRow) {
                    i2 = R.string.ChangeRecoveryEmail;
                } else if (i != TwoStepVerificationActivity.this.setRecoveryEmailRow) {
                    return;
                } else {
                    i2 = R.string.SetRecoveryEmail;
                }
            }
            textSettingsCell.setText(LocaleController.getString(i2), z);
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View textSettingsCell;
            if (i != 0) {
                textSettingsCell = new TextInfoPrivacyCell(this.mContext);
            } else {
                textSettingsCell = new TextSettingsCell(this.mContext);
                textSettingsCell.setBackgroundColor(Theme.getColor(Theme.key_windowBackgroundWhite));
            }
            return new RecyclerListView.Holder(textSettingsCell);
        }
    }

    public interface TwoStepVerificationActivityDelegate {
        void didEnterPassword(TLRPC.InputCheckPasswordSRP inputCheckPasswordSRP);
    }

    public static boolean canHandleCurrentPassword(TL_account.Password password, boolean z) {
        return z ? !(password.current_algo instanceof TLRPC.TL_passwordKdfAlgoUnknown) : ((password.new_algo instanceof TLRPC.TL_passwordKdfAlgoUnknown) || (password.current_algo instanceof TLRPC.TL_passwordKdfAlgoUnknown) || (password.new_secure_algo instanceof TLRPC.TL_securePasswordKdfAlgoUnknown)) ? false : true;
    }

    private void cancelPasswordReset() {
        if (getParentActivity() == null) {
            return;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(getParentActivity());
        builder.setPositiveButton(LocaleController.getString(R.string.CancelPasswordResetYes), new AlertDialog.OnButtonClickListener() { // from class: org.telegram.ui.TwoStepVerificationActivity$$ExternalSyntheticLambda17
            @Override // org.telegram.ui.ActionBar.AlertDialog.OnButtonClickListener
            public final void onClick(AlertDialog alertDialog, int i) {
                TwoStepVerificationActivity.this.lambda$cancelPasswordReset$10(alertDialog, i);
            }
        });
        builder.setNegativeButton(LocaleController.getString(R.string.CancelPasswordResetNo), null);
        builder.setTitle(LocaleController.getString(R.string.CancelReset));
        builder.setMessage(LocaleController.getString(R.string.CancelPasswordReset));
        showDialog(builder.create());
    }

    private boolean checkSecretValues(byte[] bArr, TL_account.passwordSettings passwordsettings) {
        byte[] computeSHA512;
        TLRPC.TL_secureSecretSettings tL_secureSecretSettings = passwordsettings.secure_settings;
        if (tL_secureSecretSettings != null) {
            this.currentSecret = tL_secureSecretSettings.secure_secret;
            TLRPC.SecurePasswordKdfAlgo securePasswordKdfAlgo = tL_secureSecretSettings.secure_algo;
            if (securePasswordKdfAlgo instanceof TLRPC.TL_securePasswordKdfAlgoPBKDF2HMACSHA512iter100000) {
                computeSHA512 = Utilities.computePBKDF2(bArr, ((TLRPC.TL_securePasswordKdfAlgoPBKDF2HMACSHA512iter100000) securePasswordKdfAlgo).salt);
            } else {
                if (!(securePasswordKdfAlgo instanceof TLRPC.TL_securePasswordKdfAlgoSHA512)) {
                    return false;
                }
                byte[] bArr2 = ((TLRPC.TL_securePasswordKdfAlgoSHA512) securePasswordKdfAlgo).salt;
                computeSHA512 = Utilities.computeSHA512(bArr2, bArr, bArr2);
            }
            this.currentSecretId = passwordsettings.secure_settings.secure_secret_id;
            byte[] bArr3 = new byte[32];
            System.arraycopy(computeSHA512, 0, bArr3, 0, 32);
            byte[] bArr4 = new byte[16];
            System.arraycopy(computeSHA512, 32, bArr4, 0, 16);
            byte[] bArr5 = this.currentSecret;
            Utilities.aesCbcEncryptionByteArraySafe(bArr5, bArr3, bArr4, 0, bArr5.length, 0, 0);
            TLRPC.TL_secureSecretSettings tL_secureSecretSettings2 = passwordsettings.secure_settings;
            if (PassportActivity.checkSecret(tL_secureSecretSettings2.secure_secret, Long.valueOf(tL_secureSecretSettings2.secure_secret_id))) {
                return true;
            }
            TL_account.updatePasswordSettings updatepasswordsettings = new TL_account.updatePasswordSettings();
            updatepasswordsettings.password = getNewSrpPassword();
            TL_account.passwordInputSettings passwordinputsettings = new TL_account.passwordInputSettings();
            updatepasswordsettings.new_settings = passwordinputsettings;
            passwordinputsettings.new_secure_settings = new TLRPC.TL_secureSecretSettings();
            TLRPC.TL_secureSecretSettings tL_secureSecretSettings3 = updatepasswordsettings.new_settings.new_secure_settings;
            tL_secureSecretSettings3.secure_secret = new byte[0];
            tL_secureSecretSettings3.secure_algo = new TLRPC.TL_securePasswordKdfAlgoUnknown();
            TL_account.passwordInputSettings passwordinputsettings2 = updatepasswordsettings.new_settings;
            passwordinputsettings2.new_secure_settings.secure_secret_id = 0L;
            passwordinputsettings2.flags |= 4;
            ConnectionsManager.getInstance(this.currentAccount).sendRequest(updatepasswordsettings, new RequestDelegate() { // from class: org.telegram.ui.TwoStepVerificationActivity$$ExternalSyntheticLambda37
                @Override // org.telegram.tgnet.RequestDelegate
                public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                    TwoStepVerificationActivity.lambda$checkSecretValues$28(tLObject, tL_error);
                }
            });
        }
        this.currentSecret = null;
        this.currentSecretId = 0L;
        return true;
    }

    private void clearPassword() {
        final TL_account.updatePasswordSettings updatepasswordsettings = new TL_account.updatePasswordSettings();
        byte[] bArr = this.currentPasswordHash;
        if (bArr == null || bArr.length == 0) {
            updatepasswordsettings.password = new TLRPC.TL_inputCheckPasswordEmpty();
        }
        updatepasswordsettings.new_settings = new TL_account.passwordInputSettings();
        UserConfig.getInstance(this.currentAccount).resetSavedPassword();
        this.currentSecret = null;
        TL_account.passwordInputSettings passwordinputsettings = updatepasswordsettings.new_settings;
        passwordinputsettings.flags = 3;
        passwordinputsettings.hint = "";
        passwordinputsettings.new_password_hash = new byte[0];
        passwordinputsettings.new_algo = new TLRPC.TL_passwordKdfAlgoUnknown();
        updatepasswordsettings.new_settings.email = "";
        needShowProgress();
        Utilities.globalQueue.postRunnable(new Runnable() { // from class: org.telegram.ui.TwoStepVerificationActivity$$ExternalSyntheticLambda22
            @Override // java.lang.Runnable
            public final void run() {
                TwoStepVerificationActivity.this.lambda$clearPassword$27(updatepasswordsettings);
            }
        });
    }

    public static void initPasswordNewAlgo(TL_account.Password password) {
        TLRPC.PasswordKdfAlgo passwordKdfAlgo = password.new_algo;
        if (passwordKdfAlgo instanceof TLRPC.TL_passwordKdfAlgoSHA256SHA256PBKDF2HMACSHA512iter100000SHA256ModPow) {
            TLRPC.TL_passwordKdfAlgoSHA256SHA256PBKDF2HMACSHA512iter100000SHA256ModPow tL_passwordKdfAlgoSHA256SHA256PBKDF2HMACSHA512iter100000SHA256ModPow = (TLRPC.TL_passwordKdfAlgoSHA256SHA256PBKDF2HMACSHA512iter100000SHA256ModPow) passwordKdfAlgo;
            byte[] bArr = new byte[tL_passwordKdfAlgoSHA256SHA256PBKDF2HMACSHA512iter100000SHA256ModPow.salt1.length + 32];
            Utilities.random.nextBytes(bArr);
            byte[] bArr2 = tL_passwordKdfAlgoSHA256SHA256PBKDF2HMACSHA512iter100000SHA256ModPow.salt1;
            System.arraycopy(bArr2, 0, bArr, 0, bArr2.length);
            tL_passwordKdfAlgoSHA256SHA256PBKDF2HMACSHA512iter100000SHA256ModPow.salt1 = bArr;
        }
        TLRPC.SecurePasswordKdfAlgo securePasswordKdfAlgo = password.new_secure_algo;
        if (securePasswordKdfAlgo instanceof TLRPC.TL_securePasswordKdfAlgoPBKDF2HMACSHA512iter100000) {
            TLRPC.TL_securePasswordKdfAlgoPBKDF2HMACSHA512iter100000 tL_securePasswordKdfAlgoPBKDF2HMACSHA512iter100000 = (TLRPC.TL_securePasswordKdfAlgoPBKDF2HMACSHA512iter100000) securePasswordKdfAlgo;
            byte[] bArr3 = new byte[tL_securePasswordKdfAlgoPBKDF2HMACSHA512iter100000.salt.length + 32];
            Utilities.random.nextBytes(bArr3);
            byte[] bArr4 = tL_securePasswordKdfAlgoPBKDF2HMACSHA512iter100000.salt;
            System.arraycopy(bArr4, 0, bArr3, 0, bArr4.length);
            tL_securePasswordKdfAlgoPBKDF2HMACSHA512iter100000.salt = bArr3;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$cancelPasswordReset$10(AlertDialog alertDialog, int i) {
        getConnectionsManager().sendRequest(new TL_account.declinePasswordReset(), new RequestDelegate() { // from class: org.telegram.ui.TwoStepVerificationActivity$$ExternalSyntheticLambda25
            @Override // org.telegram.tgnet.RequestDelegate
            public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                TwoStepVerificationActivity.this.lambda$cancelPasswordReset$9(tLObject, tL_error);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$cancelPasswordReset$8(TLObject tLObject) {
        if (tLObject instanceof TLRPC.TL_boolTrue) {
            this.currentPassword.pending_reset_date = 0;
            updateBottomButton();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$cancelPasswordReset$9(final TLObject tLObject, TLRPC.TL_error tL_error) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.TwoStepVerificationActivity$$ExternalSyntheticLambda30
            @Override // java.lang.Runnable
            public final void run() {
                TwoStepVerificationActivity.this.lambda$cancelPasswordReset$8(tLObject);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$checkSecretValues$28(TLObject tLObject, TLRPC.TL_error tL_error) {
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$clearPassword$21(TLRPC.TL_error tL_error, TLObject tLObject) {
        if (tL_error == null) {
            TL_account.Password password = (TL_account.Password) tLObject;
            this.currentPassword = password;
            initPasswordNewAlgo(password);
            NotificationCenter.getInstance(this.currentAccount).lambda$postNotificationNameOnUIThread$1(NotificationCenter.didSetOrRemoveTwoStepPassword, this.currentPassword);
            clearPassword();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$clearPassword$22(final TLObject tLObject, final TLRPC.TL_error tL_error) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.TwoStepVerificationActivity$$ExternalSyntheticLambda31
            @Override // java.lang.Runnable
            public final void run() {
                TwoStepVerificationActivity.this.lambda$clearPassword$21(tL_error, tLObject);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$clearPassword$23(TLRPC.TL_error tL_error, TLObject tLObject) {
        if (tL_error == null) {
            TL_account.Password password = (TL_account.Password) tLObject;
            this.currentPassword = password;
            initPasswordNewAlgo(password);
            NotificationCenter.getInstance(this.currentAccount).lambda$postNotificationNameOnUIThread$1(NotificationCenter.didSetOrRemoveTwoStepPassword, this.currentPassword);
            clearPassword();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$clearPassword$24(final TLObject tLObject, final TLRPC.TL_error tL_error) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.TwoStepVerificationActivity$$ExternalSyntheticLambda38
            @Override // java.lang.Runnable
            public final void run() {
                TwoStepVerificationActivity.this.lambda$clearPassword$23(tL_error, tLObject);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$clearPassword$25(TLRPC.TL_error tL_error, TLObject tLObject) {
        String string;
        String str;
        if (tL_error != null && "SRP_ID_INVALID".equals(tL_error.text)) {
            ConnectionsManager.getInstance(this.currentAccount).sendRequest(new TL_account.getPassword(), new RequestDelegate() { // from class: org.telegram.ui.TwoStepVerificationActivity$$ExternalSyntheticLambda35
                @Override // org.telegram.tgnet.RequestDelegate
                public final void run(TLObject tLObject2, TLRPC.TL_error tL_error2) {
                    TwoStepVerificationActivity.this.lambda$clearPassword$24(tLObject2, tL_error2);
                }
            }, 8);
            return;
        }
        needHideProgress();
        if (tL_error == null && (tLObject instanceof TLRPC.TL_boolTrue)) {
            this.currentPassword = null;
            this.currentPasswordHash = new byte[0];
            NotificationCenter.getInstance(this.currentAccount).lambda$postNotificationNameOnUIThread$1(NotificationCenter.didRemoveTwoStepPassword, new Object[0]);
            NotificationCenter.getInstance(this.currentAccount).lambda$postNotificationNameOnUIThread$1(NotificationCenter.didSetOrRemoveTwoStepPassword, new Object[0]);
            lambda$onBackPressed$323();
            return;
        }
        if (tL_error != null) {
            if (tL_error.text.startsWith("FLOOD_WAIT")) {
                int intValue = Utilities.parseInt((CharSequence) tL_error.text).intValue();
                String formatPluralString = intValue < 60 ? LocaleController.formatPluralString("Seconds", intValue, new Object[0]) : LocaleController.formatPluralString("Minutes", intValue / 60, new Object[0]);
                string = LocaleController.getString(R.string.AppName);
                str = LocaleController.formatString("FloodWaitTime", R.string.FloodWaitTime, formatPluralString);
            } else {
                string = LocaleController.getString(R.string.AppName);
                str = tL_error.text;
            }
            showAlertWithText(string, str);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$clearPassword$26(final TLObject tLObject, final TLRPC.TL_error tL_error) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.TwoStepVerificationActivity$$ExternalSyntheticLambda34
            @Override // java.lang.Runnable
            public final void run() {
                TwoStepVerificationActivity.this.lambda$clearPassword$25(tL_error, tLObject);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$clearPassword$27(TL_account.updatePasswordSettings updatepasswordsettings) {
        if (updatepasswordsettings.password == null) {
            if (this.currentPassword.current_algo == null) {
                ConnectionsManager.getInstance(this.currentAccount).sendRequest(new TL_account.getPassword(), new RequestDelegate() { // from class: org.telegram.ui.TwoStepVerificationActivity$$ExternalSyntheticLambda28
                    @Override // org.telegram.tgnet.RequestDelegate
                    public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                        TwoStepVerificationActivity.this.lambda$clearPassword$22(tLObject, tL_error);
                    }
                }, 8);
                return;
            }
            updatepasswordsettings.password = getNewSrpPassword();
        }
        ConnectionsManager.getInstance(this.currentAccount).sendRequest(updatepasswordsettings, new RequestDelegate() { // from class: org.telegram.ui.TwoStepVerificationActivity$$ExternalSyntheticLambda29
            @Override // org.telegram.tgnet.RequestDelegate
            public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                TwoStepVerificationActivity.this.lambda$clearPassword$26(tLObject, tL_error);
            }
        }, 10);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createView$1(View view, boolean z) {
        this.passwordOutlineView.animateSelection(z ? 1.0f : 0.0f);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ boolean lambda$createView$2(TextView textView, int i, KeyEvent keyEvent) {
        if (i != 5 && i != 6) {
            return false;
        }
        processDone();
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createView$3(View view) {
        onPasswordForgot();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createView$4(View view) {
        cancelPasswordReset();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createView$5(View view) {
        processDone();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createView$6(AlertDialog alertDialog, int i) {
        clearPassword();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createView$7(View view, int i) {
        TwoStepVerificationSetupActivity twoStepVerificationSetupActivity;
        if (i == this.setPasswordRow || i == this.changePasswordRow) {
            twoStepVerificationSetupActivity = new TwoStepVerificationSetupActivity(this.currentAccount, 0, this.currentPassword);
            twoStepVerificationSetupActivity.addFragmentToClose(this);
            twoStepVerificationSetupActivity.setCurrentPasswordParams(this.currentPasswordHash, this.currentSecretId, this.currentSecret, false);
        } else {
            if (i != this.setRecoveryEmailRow && i != this.changeRecoveryEmailRow) {
                if (i == this.turnPasswordOffRow) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getParentActivity());
                    String string = LocaleController.getString(R.string.TurnPasswordOffQuestion);
                    if (this.currentPassword.has_secure_values) {
                        string = string + "\n\n" + LocaleController.getString(R.string.TurnPasswordOffPassport);
                    }
                    String string2 = LocaleController.getString(R.string.TurnPasswordOffQuestionTitle);
                    String string3 = LocaleController.getString(R.string.Disable);
                    builder.setMessage(string);
                    builder.setTitle(string2);
                    builder.setPositiveButton(string3, new AlertDialog.OnButtonClickListener() { // from class: org.telegram.ui.TwoStepVerificationActivity$$ExternalSyntheticLambda18
                        @Override // org.telegram.ui.ActionBar.AlertDialog.OnButtonClickListener
                        public final void onClick(AlertDialog alertDialog, int i2) {
                            TwoStepVerificationActivity.this.lambda$createView$6(alertDialog, i2);
                        }
                    });
                    builder.setNegativeButton(LocaleController.getString(R.string.Cancel), null);
                    AlertDialog create = builder.create();
                    showDialog(create);
                    TextView textView = (TextView) create.getButton(-1);
                    if (textView != null) {
                        textView.setTextColor(Theme.getColor(Theme.key_text_RedBold));
                        return;
                    }
                    return;
                }
                return;
            }
            twoStepVerificationSetupActivity = new TwoStepVerificationSetupActivity(this.currentAccount, 3, this.currentPassword);
            twoStepVerificationSetupActivity.addFragmentToClose(this);
            twoStepVerificationSetupActivity.setCurrentPasswordParams(this.currentPasswordHash, this.currentSecretId, this.currentSecret, true);
        }
        presentFragment(twoStepVerificationSetupActivity);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$loadPasswordInfo$18(TLRPC.TL_error tL_error, TLObject tLObject, boolean z, boolean z2, Runnable runnable) {
        if (tL_error == null) {
            this.loading = false;
            TL_account.Password password = (TL_account.Password) tLObject;
            this.currentPassword = password;
            if (!canHandleCurrentPassword(password, false)) {
                AlertsCreator.showUpdateAppAlert(getParentActivity(), LocaleController.getString(R.string.UpdateAppAlert), true);
                return;
            }
            if (!z || z2) {
                byte[] bArr = this.currentPasswordHash;
                this.passwordEntered = (bArr != null && bArr.length > 0) || !this.currentPassword.has_password;
            }
            initPasswordNewAlgo(this.currentPassword);
            NotificationCenter.getInstance(this.currentAccount).lambda$postNotificationNameOnUIThread$1(NotificationCenter.didSetOrRemoveTwoStepPassword, this.currentPassword);
        }
        if (runnable != null) {
            runnable.run();
        }
        updateRows();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$loadPasswordInfo$19(final boolean z, final boolean z2, final Runnable runnable, final TLObject tLObject, final TLRPC.TL_error tL_error) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.TwoStepVerificationActivity$$ExternalSyntheticLambda20
            @Override // java.lang.Runnable
            public final void run() {
                TwoStepVerificationActivity.this.lambda$loadPasswordInfo$18(tL_error, tLObject, z, z2, runnable);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0() {
        this.postedErrorColorTimeout = false;
        this.passwordOutlineView.animateError(0.0f);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onFieldError$36() {
        AndroidUtilities.cancelRunOnUIThread(this.errorColorTimeout);
        AndroidUtilities.runOnUIThread(this.errorColorTimeout, 1500L);
        this.postedErrorColorTimeout = true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onPasswordForgot$14(TLRPC.TL_error tL_error, TLObject tLObject) {
        String string;
        String str;
        needHideProgress();
        if (tL_error == null) {
            TL_account.Password password = this.currentPassword;
            password.email_unconfirmed_pattern = ((TLRPC.TL_auth_passwordRecovery) tLObject).email_pattern;
            TwoStepVerificationSetupActivity twoStepVerificationSetupActivity = new TwoStepVerificationSetupActivity(this.currentAccount, 4, password) { // from class: org.telegram.ui.TwoStepVerificationActivity.5
                @Override // org.telegram.ui.TwoStepVerificationSetupActivity
                protected void onReset() {
                    TwoStepVerificationActivity.this.resetPasswordOnShow = true;
                }
            };
            twoStepVerificationSetupActivity.addFragmentToClose(this);
            twoStepVerificationSetupActivity.setCurrentPasswordParams(this.currentPasswordHash, this.currentSecretId, this.currentSecret, false);
            presentFragment(twoStepVerificationSetupActivity);
            return;
        }
        if (tL_error.text.startsWith("FLOOD_WAIT")) {
            int intValue = Utilities.parseInt((CharSequence) tL_error.text).intValue();
            String formatPluralString = intValue < 60 ? LocaleController.formatPluralString("Seconds", intValue, new Object[0]) : LocaleController.formatPluralString("Minutes", intValue / 60, new Object[0]);
            string = LocaleController.getString(R.string.AppName);
            str = LocaleController.formatString("FloodWaitTime", R.string.FloodWaitTime, formatPluralString);
        } else {
            string = LocaleController.getString(R.string.AppName);
            str = tL_error.text;
        }
        showAlertWithText(string, str);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onPasswordForgot$15(final TLObject tLObject, final TLRPC.TL_error tL_error) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.TwoStepVerificationActivity$$ExternalSyntheticLambda19
            @Override // java.lang.Runnable
            public final void run() {
                TwoStepVerificationActivity.this.lambda$onPasswordForgot$14(tL_error, tLObject);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onPasswordForgot$16(AlertDialog alertDialog, int i) {
        resetPassword();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onPasswordForgot$17(AlertDialog alertDialog, int i) {
        resetPassword();
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Multi-variable type inference failed */
    public /* synthetic */ void lambda$processDone$29(boolean z, byte[] bArr) {
        TwoStepVerificationActivity twoStepVerificationActivity;
        if (this.delegate == null || !z) {
            needHideProgress();
        }
        if (!z) {
            AlertsCreator.showUpdateAppAlert(getParentActivity(), LocaleController.getString(R.string.UpdateAppAlert), true);
            return;
        }
        this.currentPasswordHash = bArr;
        this.passwordEntered = true;
        if (this.delegate != null) {
            AndroidUtilities.hideKeyboard(this.passwordEditText);
            this.delegate.didEnterPassword(getNewSrpPassword());
            return;
        }
        if (TextUtils.isEmpty(this.currentPassword.email_unconfirmed_pattern)) {
            AndroidUtilities.hideKeyboard(this.passwordEditText);
            TwoStepVerificationActivity twoStepVerificationActivity2 = new TwoStepVerificationActivity();
            twoStepVerificationActivity2.passwordEntered = true;
            twoStepVerificationActivity2.currentPasswordHash = this.currentPasswordHash;
            twoStepVerificationActivity2.currentPassword = this.currentPassword;
            twoStepVerificationActivity2.currentSecret = this.currentSecret;
            twoStepVerificationActivity2.currentSecretId = this.currentSecretId;
            twoStepVerificationActivity = twoStepVerificationActivity2;
        } else {
            TwoStepVerificationSetupActivity twoStepVerificationSetupActivity = new TwoStepVerificationSetupActivity(this.currentAccount, 5, this.currentPassword);
            twoStepVerificationSetupActivity.setCurrentPasswordParams(this.currentPasswordHash, this.currentSecretId, this.currentSecret, true);
            twoStepVerificationActivity = twoStepVerificationSetupActivity;
        }
        presentFragment(twoStepVerificationActivity, true);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$processDone$30(byte[] bArr, TLObject tLObject, final byte[] bArr2) {
        final boolean checkSecretValues = checkSecretValues(bArr, (TL_account.passwordSettings) tLObject);
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.TwoStepVerificationActivity$$ExternalSyntheticLambda32
            @Override // java.lang.Runnable
            public final void run() {
                TwoStepVerificationActivity.this.lambda$processDone$29(checkSecretValues, bArr2);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$processDone$31(TLRPC.TL_error tL_error, TLObject tLObject) {
        if (tL_error == null) {
            TL_account.Password password = (TL_account.Password) tLObject;
            this.currentPassword = password;
            initPasswordNewAlgo(password);
            NotificationCenter.getInstance(this.currentAccount).lambda$postNotificationNameOnUIThread$1(NotificationCenter.didSetOrRemoveTwoStepPassword, this.currentPassword);
            processDone();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$processDone$32(final TLObject tLObject, final TLRPC.TL_error tL_error) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.TwoStepVerificationActivity$$ExternalSyntheticLambda36
            @Override // java.lang.Runnable
            public final void run() {
                TwoStepVerificationActivity.this.lambda$processDone$31(tL_error, tLObject);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$processDone$33(TLRPC.TL_error tL_error) {
        if ("SRP_ID_INVALID".equals(tL_error.text)) {
            ConnectionsManager.getInstance(this.currentAccount).sendRequest(new TL_account.getPassword(), new RequestDelegate() { // from class: org.telegram.ui.TwoStepVerificationActivity$$ExternalSyntheticLambda33
                @Override // org.telegram.tgnet.RequestDelegate
                public final void run(TLObject tLObject, TLRPC.TL_error tL_error2) {
                    TwoStepVerificationActivity.this.lambda$processDone$32(tLObject, tL_error2);
                }
            }, 8);
            return;
        }
        needHideProgress();
        if ("PASSWORD_HASH_INVALID".equals(tL_error.text)) {
            onFieldError(this.passwordOutlineView, this.passwordEditText, true);
        } else if (!tL_error.text.startsWith("FLOOD_WAIT")) {
            showAlertWithText(LocaleController.getString(R.string.AppName), tL_error.text);
        } else {
            int intValue = Utilities.parseInt((CharSequence) tL_error.text).intValue();
            showAlertWithText(LocaleController.getString(R.string.AppName), LocaleController.formatString("FloodWaitTime", R.string.FloodWaitTime, intValue < 60 ? LocaleController.formatPluralString("Seconds", intValue, new Object[0]) : LocaleController.formatPluralString("Minutes", intValue / 60, new Object[0])));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$processDone$34(final byte[] bArr, final byte[] bArr2, final TLObject tLObject, final TLRPC.TL_error tL_error) {
        if (tL_error == null) {
            Utilities.globalQueue.postRunnable(new Runnable() { // from class: org.telegram.ui.TwoStepVerificationActivity$$ExternalSyntheticLambda26
                @Override // java.lang.Runnable
                public final void run() {
                    TwoStepVerificationActivity.this.lambda$processDone$30(bArr, tLObject, bArr2);
                }
            });
        } else {
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.TwoStepVerificationActivity$$ExternalSyntheticLambda27
                @Override // java.lang.Runnable
                public final void run() {
                    TwoStepVerificationActivity.this.lambda$processDone$33(tL_error);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$processDone$35(final byte[] bArr) {
        TL_account.getPasswordSettings getpasswordsettings = new TL_account.getPasswordSettings();
        TLRPC.PasswordKdfAlgo passwordKdfAlgo = this.currentPassword.current_algo;
        final byte[] x = passwordKdfAlgo instanceof TLRPC.TL_passwordKdfAlgoSHA256SHA256PBKDF2HMACSHA512iter100000SHA256ModPow ? SRPHelper.getX(bArr, (TLRPC.TL_passwordKdfAlgoSHA256SHA256PBKDF2HMACSHA512iter100000SHA256ModPow) passwordKdfAlgo) : null;
        RequestDelegate requestDelegate = new RequestDelegate() { // from class: org.telegram.ui.TwoStepVerificationActivity$$ExternalSyntheticLambda21
            @Override // org.telegram.tgnet.RequestDelegate
            public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                TwoStepVerificationActivity.this.lambda$processDone$34(bArr, x, tLObject, tL_error);
            }
        };
        TL_account.Password password = this.currentPassword;
        TLRPC.PasswordKdfAlgo passwordKdfAlgo2 = password.current_algo;
        if (!(passwordKdfAlgo2 instanceof TLRPC.TL_passwordKdfAlgoSHA256SHA256PBKDF2HMACSHA512iter100000SHA256ModPow)) {
            TLRPC.TL_error tL_error = new TLRPC.TL_error();
            tL_error.text = "PASSWORD_HASH_INVALID";
            requestDelegate.run(null, tL_error);
            return;
        }
        TLRPC.TL_inputCheckPasswordSRP startCheck = SRPHelper.startCheck(x, password.srp_id, password.srp_B, (TLRPC.TL_passwordKdfAlgoSHA256SHA256PBKDF2HMACSHA512iter100000SHA256ModPow) passwordKdfAlgo2);
        getpasswordsettings.password = startCheck;
        if (startCheck != null) {
            ConnectionsManager.getInstance(this.currentAccount).sendRequest(getpasswordsettings, requestDelegate, 10);
            return;
        }
        TLRPC.TL_error tL_error2 = new TLRPC.TL_error();
        tL_error2.text = "ALGO_INVALID";
        requestDelegate.run(null, tL_error2);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$resetPassword$11(DialogInterface dialogInterface) {
        getNotificationCenter().lambda$postNotificationNameOnUIThread$1(NotificationCenter.didSetOrRemoveTwoStepPassword, new Object[0]);
        lambda$onBackPressed$323();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$resetPassword$12(TLObject tLObject) {
        needHideProgress();
        if (tLObject instanceof TL_account.resetPasswordOk) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getParentActivity());
            builder.setNegativeButton(LocaleController.getString(R.string.OK), null);
            builder.setTitle(LocaleController.getString(R.string.ResetPassword));
            builder.setMessage(LocaleController.getString(R.string.RestorePasswordResetPasswordOk));
            showDialog(builder.create(), new DialogInterface.OnDismissListener() { // from class: org.telegram.ui.TwoStepVerificationActivity$$ExternalSyntheticLambda24
                @Override // android.content.DialogInterface.OnDismissListener
                public final void onDismiss(DialogInterface dialogInterface) {
                    TwoStepVerificationActivity.this.lambda$resetPassword$11(dialogInterface);
                }
            });
            return;
        }
        if (tLObject instanceof TL_account.resetPasswordRequestedWait) {
            this.currentPassword.pending_reset_date = ((TL_account.resetPasswordRequestedWait) tLObject).until_date;
            updateBottomButton();
        } else if (tLObject instanceof TL_account.resetPasswordFailedWait) {
            int currentTime = ((TL_account.resetPasswordFailedWait) tLObject).retry_date - getConnectionsManager().getCurrentTime();
            showAlertWithText(LocaleController.getString(R.string.ResetPassword), LocaleController.formatString("ResetPasswordWait", R.string.ResetPasswordWait, currentTime > 86400 ? LocaleController.formatPluralString("Days", currentTime / 86400, new Object[0]) : currentTime > 3600 ? LocaleController.formatPluralString("Hours", currentTime / 86400, new Object[0]) : currentTime > 60 ? LocaleController.formatPluralString("Minutes", currentTime / 60, new Object[0]) : LocaleController.formatPluralString("Seconds", Math.max(1, currentTime), new Object[0])));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$resetPassword$13(final TLObject tLObject, TLRPC.TL_error tL_error) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.TwoStepVerificationActivity$$ExternalSyntheticLambda16
            @Override // java.lang.Runnable
            public final void run() {
                TwoStepVerificationActivity.this.lambda$resetPassword$12(tLObject);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$showSetForcePasswordAlert$37(AlertDialog alertDialog, int i) {
        lambda$onBackPressed$323();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$updateRows$20() {
        EditTextBoldCursor editTextBoldCursor;
        if (isFinishing() || this.destroyed || (editTextBoldCursor = this.passwordEditText) == null) {
            return;
        }
        editTextBoldCursor.requestFocus();
        AndroidUtilities.showKeyboard(this.passwordEditText);
    }

    private void loadPasswordInfo(final boolean z, final boolean z2, final Runnable runnable) {
        if (!z2) {
            this.loading = true;
            ListAdapter listAdapter = this.listAdapter;
            if (listAdapter != null) {
                listAdapter.notifyDataSetChanged();
            }
        }
        ConnectionsManager.getInstance(this.currentAccount).sendRequest(new TL_account.getPassword(), new RequestDelegate() { // from class: org.telegram.ui.TwoStepVerificationActivity$$ExternalSyntheticLambda14
            @Override // org.telegram.tgnet.RequestDelegate
            public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                TwoStepVerificationActivity.this.lambda$loadPasswordInfo$19(z2, z, runnable, tLObject, tL_error);
            }
        }, 10);
    }

    private void needShowProgress() {
        needShowProgress(false);
    }

    private void needShowProgress(boolean z) {
        if (getParentActivity() == null || getParentActivity().isFinishing() || this.progressDialog != null) {
            return;
        }
        if (!this.passwordEntered) {
            AnimatorSet animatorSet = new AnimatorSet();
            animatorSet.playTogether(ObjectAnimator.ofFloat(this.radialProgressView, (Property<RadialProgressView, Float>) View.ALPHA, 1.0f), ObjectAnimator.ofFloat(this.radialProgressView, (Property<RadialProgressView, Float>) View.SCALE_X, 1.0f), ObjectAnimator.ofFloat(this.radialProgressView, (Property<RadialProgressView, Float>) View.SCALE_Y, 1.0f));
            animatorSet.setInterpolator(CubicBezierInterpolator.DEFAULT);
            animatorSet.start();
            return;
        }
        AlertDialog alertDialog = new AlertDialog(getParentActivity(), 3);
        this.progressDialog = alertDialog;
        alertDialog.setCanCancel(false);
        if (z) {
            this.progressDialog.showDelayed(300L);
        } else {
            this.progressDialog.show();
        }
    }

    private void onFieldError(OutlineTextContainerView outlineTextContainerView, TextView textView, boolean z) {
        if (getParentActivity() == null) {
            return;
        }
        try {
            textView.performHapticFeedback(3, 2);
        } catch (Exception unused) {
        }
        if (z) {
            textView.setText("");
        }
        outlineTextContainerView.animateError(1.0f);
        AndroidUtilities.shakeViewSpring(outlineTextContainerView, 5.0f, new Runnable() { // from class: org.telegram.ui.TwoStepVerificationActivity$$ExternalSyntheticLambda23
            @Override // java.lang.Runnable
            public final void run() {
                TwoStepVerificationActivity.this.lambda$onFieldError$36();
            }
        });
    }

    private void onPasswordForgot() {
        TL_account.Password password = this.currentPassword;
        if (password.pending_reset_date == 0 && password.has_recovery) {
            needShowProgress(true);
            ConnectionsManager.getInstance(this.currentAccount).sendRequest(new TLRPC.TL_auth_requestPasswordRecovery(), new RequestDelegate() { // from class: org.telegram.ui.TwoStepVerificationActivity$$ExternalSyntheticLambda1
                @Override // org.telegram.tgnet.RequestDelegate
                public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                    TwoStepVerificationActivity.this.lambda$onPasswordForgot$15(tLObject, tL_error);
                }
            }, 10);
            return;
        }
        if (getParentActivity() == null) {
            return;
        }
        if (this.currentPassword.pending_reset_date == 0) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getParentActivity());
            builder.setPositiveButton(LocaleController.getString(R.string.Reset), new AlertDialog.OnButtonClickListener() { // from class: org.telegram.ui.TwoStepVerificationActivity$$ExternalSyntheticLambda3
                @Override // org.telegram.ui.ActionBar.AlertDialog.OnButtonClickListener
                public final void onClick(AlertDialog alertDialog, int i) {
                    TwoStepVerificationActivity.this.lambda$onPasswordForgot$17(alertDialog, i);
                }
            });
            builder.setNegativeButton(LocaleController.getString(R.string.Cancel), null);
            builder.setTitle(LocaleController.getString(R.string.ResetPassword));
            builder.setMessage(LocaleController.getString(R.string.RestorePasswordNoEmailText2));
            showDialog(builder.create());
            return;
        }
        if (getConnectionsManager().getCurrentTime() <= this.currentPassword.pending_reset_date) {
            cancelPasswordReset();
            return;
        }
        AlertDialog.Builder builder2 = new AlertDialog.Builder(getParentActivity());
        builder2.setPositiveButton(LocaleController.getString(R.string.Reset), new AlertDialog.OnButtonClickListener() { // from class: org.telegram.ui.TwoStepVerificationActivity$$ExternalSyntheticLambda2
            @Override // org.telegram.ui.ActionBar.AlertDialog.OnButtonClickListener
            public final void onClick(AlertDialog alertDialog, int i) {
                TwoStepVerificationActivity.this.lambda$onPasswordForgot$16(alertDialog, i);
            }
        });
        builder2.setNegativeButton(LocaleController.getString(R.string.Cancel), null);
        builder2.setTitle(LocaleController.getString(R.string.ResetPassword));
        builder2.setMessage(LocaleController.getString(R.string.RestorePasswordResetPasswordText));
        AlertDialog create = builder2.create();
        showDialog(create);
        TextView textView = (TextView) create.getButton(-1);
        if (textView != null) {
            textView.setTextColor(Theme.getColor(Theme.key_text_RedBold));
        }
    }

    private void processDone() {
        if (this.passwordEntered) {
            return;
        }
        String obj = this.passwordEditText.getText().toString();
        if (obj.length() == 0) {
            onFieldError(this.passwordOutlineView, this.passwordEditText, false);
            return;
        }
        final byte[] stringBytes = AndroidUtilities.getStringBytes(obj);
        needShowProgress();
        Utilities.globalQueue.postRunnable(new Runnable() { // from class: org.telegram.ui.TwoStepVerificationActivity$$ExternalSyntheticLambda15
            @Override // java.lang.Runnable
            public final void run() {
                TwoStepVerificationActivity.this.lambda$processDone$35(stringBytes);
            }
        });
    }

    private void resetPassword() {
        needShowProgress(true);
        getConnectionsManager().sendRequest(new TL_account.resetPassword(), new RequestDelegate() { // from class: org.telegram.ui.TwoStepVerificationActivity$$ExternalSyntheticLambda10
            @Override // org.telegram.tgnet.RequestDelegate
            public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                TwoStepVerificationActivity.this.lambda$resetPassword$13(tLObject, tL_error);
            }
        });
    }

    private void showAlertWithText(String str, String str2) {
        if (getParentActivity() == null) {
            return;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(getParentActivity());
        builder.setPositiveButton(LocaleController.getString(R.string.OK), null);
        builder.setTitle(str);
        builder.setMessage(str2);
        showDialog(builder.create());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void showSetForcePasswordAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getParentActivity());
        builder.setTitle(LocaleController.getString(R.string.Warning));
        builder.setMessage(LocaleController.formatPluralString("ForceSetPasswordAlertMessageShort", this.otherwiseReloginDays, new Object[0]));
        builder.setPositiveButton(LocaleController.getString(R.string.TwoStepVerificationSetPassword), null);
        builder.setNegativeButton(LocaleController.getString(R.string.ForceSetPasswordCancel), new AlertDialog.OnButtonClickListener() { // from class: org.telegram.ui.TwoStepVerificationActivity$$ExternalSyntheticLambda0
            @Override // org.telegram.ui.ActionBar.AlertDialog.OnButtonClickListener
            public final void onClick(AlertDialog alertDialog, int i) {
                TwoStepVerificationActivity.this.lambda$showSetForcePasswordAlert$37(alertDialog, i);
            }
        });
        ((TextView) builder.show().getButton(-2)).setTextColor(Theme.getColor(Theme.key_text_RedBold));
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Removed duplicated region for block: B:28:0x010f  */
    /* JADX WARN: Removed duplicated region for block: B:30:? A[RETURN, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void updateBottomButton() {
        TextView textView;
        if (this.passwordEntered) {
            return;
        }
        if (this.currentPassword.pending_reset_date != 0) {
            int currentTime = getConnectionsManager().getCurrentTime();
            int i = this.currentPassword.pending_reset_date;
            if (currentTime <= i) {
                int max = Math.max(1, i - getConnectionsManager().getCurrentTime());
                this.resetWaitView.setText(LocaleController.formatString("RestorePasswordResetIn", R.string.RestorePasswordResetIn, max > 86400 ? LocaleController.formatPluralString("Days", max / 86400, new Object[0]) : max >= 3600 ? LocaleController.formatPluralString("Hours", max / 3600, new Object[0]) : String.format(Locale.US, "%02d:%02d", Integer.valueOf(max / 60), Integer.valueOf(max % 60))));
                this.resetWaitView.setTextColor(Theme.getColor(Theme.key_windowBackgroundWhiteGrayText6));
                if (this.bottomButton.getVisibility() != 8) {
                    this.bottomButton.setVisibility(8);
                }
                if (this.resetWaitView.getVisibility() != 0) {
                    this.resetWaitView.setVisibility(0);
                }
                this.cancelResetButton.setVisibility(0);
                AndroidUtilities.cancelRunOnUIThread(this.updateTimeRunnable);
                AndroidUtilities.runOnUIThread(this.updateTimeRunnable, 1000L);
                if (this.currentPassword == null && this.bottomButton != null && this.resetWaitView.getVisibility() == 0) {
                    return;
                }
                AndroidUtilities.cancelRunOnUIThread(this.updateTimeRunnable);
                textView = this.cancelResetButton;
                if (textView == null) {
                    textView.setVisibility(8);
                    return;
                }
                return;
            }
        }
        if (this.resetWaitView.getVisibility() != 8) {
            this.resetWaitView.setVisibility(8);
        }
        if (this.currentPassword.pending_reset_date == 0) {
            this.bottomButton.setText(LocaleController.getString(R.string.ForgotPassword));
            this.cancelResetButton.setVisibility(8);
        } else {
            this.bottomButton.setText(LocaleController.getString(R.string.ResetPassword));
            this.cancelResetButton.setVisibility(0);
        }
        this.bottomButton.setVisibility(0);
        this.bottomButton.setTextColor(Theme.getColor(Theme.key_windowBackgroundWhiteBlueText4));
        AndroidUtilities.cancelRunOnUIThread(this.updateTimeRunnable);
        if (this.currentPassword == null) {
        }
        AndroidUtilities.cancelRunOnUIThread(this.updateTimeRunnable);
        textView = this.cancelResetButton;
        if (textView == null) {
        }
    }

    private void updateRows() {
        TL_account.Password password;
        StringBuilder sb = new StringBuilder();
        sb.append(this.setPasswordRow);
        sb.append(this.setPasswordDetailRow);
        sb.append(this.changePasswordRow);
        sb.append(this.turnPasswordOffRow);
        sb.append(this.setRecoveryEmailRow);
        sb.append(this.changeRecoveryEmailRow);
        sb.append(this.passwordEnabledDetailRow);
        sb.append(this.rowCount);
        this.rowCount = 0;
        this.setPasswordRow = -1;
        this.setPasswordDetailRow = -1;
        this.changePasswordRow = -1;
        this.turnPasswordOffRow = -1;
        this.setRecoveryEmailRow = -1;
        this.changeRecoveryEmailRow = -1;
        this.passwordEnabledDetailRow = -1;
        if (!this.loading && (password = this.currentPassword) != null && this.passwordEntered) {
            if (password.has_password) {
                this.changePasswordRow = 0;
                this.rowCount = 2;
                this.turnPasswordOffRow = 1;
                boolean z = password.has_recovery;
                this.rowCount = 3;
                if (z) {
                    this.changeRecoveryEmailRow = 2;
                } else {
                    this.setRecoveryEmailRow = 2;
                }
                int i = this.rowCount;
                this.rowCount = i + 1;
                this.passwordEnabledDetailRow = i;
            } else {
                this.setPasswordRow = 0;
                this.rowCount = 2;
                this.setPasswordDetailRow = 1;
            }
        }
        StringBuilder sb2 = new StringBuilder();
        sb2.append(this.setPasswordRow);
        sb2.append(this.setPasswordDetailRow);
        sb2.append(this.changePasswordRow);
        sb2.append(this.turnPasswordOffRow);
        sb2.append(this.setRecoveryEmailRow);
        sb2.append(this.changeRecoveryEmailRow);
        sb2.append(this.passwordEnabledDetailRow);
        sb2.append(this.rowCount);
        if (this.listAdapter != null && !sb.toString().equals(sb2.toString())) {
            this.listAdapter.notifyDataSetChanged();
        }
        if (this.fragmentView != null) {
            if (this.loading || this.passwordEntered) {
                RecyclerListView recyclerListView = this.listView;
                if (recyclerListView != null) {
                    recyclerListView.setVisibility(0);
                    this.scrollView.setVisibility(4);
                    this.listView.setEmptyView(this.emptyView);
                }
                if (this.passwordEditText != null) {
                    this.floatingButtonContainer.setVisibility(8);
                    this.passwordEditText.setVisibility(4);
                    this.titleTextView.setVisibility(4);
                    this.bottomTextView.setVisibility(8);
                    this.bottomButton.setVisibility(4);
                    updateBottomButton();
                }
                View view = this.fragmentView;
                int i2 = Theme.key_windowBackgroundGray;
                view.setBackgroundColor(Theme.getColor(i2));
                this.fragmentView.setTag(Integer.valueOf(i2));
                return;
            }
            RecyclerListView recyclerListView2 = this.listView;
            if (recyclerListView2 != null) {
                recyclerListView2.setEmptyView(null);
                this.listView.setVisibility(4);
                this.scrollView.setVisibility(0);
                this.emptyView.setVisibility(4);
            }
            if (this.passwordEditText != null) {
                this.floatingButtonContainer.setVisibility(0);
                this.passwordEditText.setVisibility(0);
                View view2 = this.fragmentView;
                int i3 = Theme.key_windowBackgroundWhite;
                view2.setBackgroundColor(Theme.getColor(i3));
                this.fragmentView.setTag(Integer.valueOf(i3));
                this.titleTextView.setVisibility(0);
                this.bottomButton.setVisibility(0);
                updateBottomButton();
                this.bottomTextView.setVisibility(8);
                if (TextUtils.isEmpty(this.currentPassword.hint)) {
                    this.passwordEditText.setHint((CharSequence) null);
                } else {
                    this.passwordEditText.setHint(this.currentPassword.hint);
                }
                AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.TwoStepVerificationActivity$$ExternalSyntheticLambda13
                    @Override // java.lang.Runnable
                    public final void run() {
                        TwoStepVerificationActivity.this.lambda$updateRows$20();
                    }
                }, 200L);
            }
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:14:0x0222  */
    /* JADX WARN: Removed duplicated region for block: B:17:0x023b  */
    /* JADX WARN: Removed duplicated region for block: B:20:0x026e  */
    /* JADX WARN: Removed duplicated region for block: B:23:0x02ca  */
    /* JADX WARN: Removed duplicated region for block: B:26:0x032e  */
    /* JADX WARN: Removed duplicated region for block: B:29:0x035e  */
    /* JADX WARN: Removed duplicated region for block: B:32:0x0403  */
    /* JADX WARN: Removed duplicated region for block: B:34:0x040a  */
    /* JADX WARN: Removed duplicated region for block: B:37:0x042e  */
    /* JADX WARN: Removed duplicated region for block: B:40:0x0469  */
    /* JADX WARN: Removed duplicated region for block: B:42:0x0470  */
    /* JADX WARN: Removed duplicated region for block: B:51:0x053b  */
    /* JADX WARN: Removed duplicated region for block: B:59:0x0596  */
    /* JADX WARN: Removed duplicated region for block: B:64:0x05ab  */
    /* JADX WARN: Removed duplicated region for block: B:69:0x0577  */
    /* JADX WARN: Removed duplicated region for block: B:71:0x0473  */
    /* JADX WARN: Removed duplicated region for block: B:72:0x046c  */
    /* JADX WARN: Removed duplicated region for block: B:73:0x045c  */
    /* JADX WARN: Removed duplicated region for block: B:74:0x040d  */
    /* JADX WARN: Removed duplicated region for block: B:75:0x0406  */
    /* JADX WARN: Removed duplicated region for block: B:76:0x0331  */
    /* JADX WARN: Removed duplicated region for block: B:77:0x02cd  */
    /* JADX WARN: Removed duplicated region for block: B:78:0x023d  */
    /* JADX WARN: Removed duplicated region for block: B:79:0x0224  */
    @Override // org.telegram.ui.ActionBar.BaseFragment
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public View createView(Context context) {
        int i;
        int i2;
        ActionBar actionBar;
        String str;
        View view;
        Integer valueOf;
        TextView textView;
        int i3;
        CharSequence replaceTags;
        this.actionBar.setBackButtonImage(R.drawable.ic_ab_back);
        this.actionBar.setAllowOverlayTitle(false);
        if (!this.passwordEntered || this.delegate != null) {
            this.actionBar.setBackgroundColor(Theme.getColor(Theme.key_windowBackgroundWhite));
            ActionBar actionBar2 = this.actionBar;
            int i4 = Theme.key_windowBackgroundWhiteBlackText;
            actionBar2.setTitleColor(Theme.getColor(i4));
            this.actionBar.setItemsColor(Theme.getColor(i4), false);
            this.actionBar.setItemsBackgroundColor(Theme.getColor(Theme.key_actionBarWhiteSelector), false);
            this.actionBar.setCastShadows(false);
        }
        this.actionBar.setActionBarMenuOnItemClick(new ActionBar.ActionBarMenuOnItemClick() { // from class: org.telegram.ui.TwoStepVerificationActivity.1
            @Override // org.telegram.ui.ActionBar.ActionBar.ActionBarMenuOnItemClick
            public void onItemClick(int i5) {
                if (i5 == -1) {
                    TwoStepVerificationActivity twoStepVerificationActivity = TwoStepVerificationActivity.this;
                    if (twoStepVerificationActivity.otherwiseReloginDays >= 0) {
                        twoStepVerificationActivity.showSetForcePasswordAlert();
                    } else {
                        twoStepVerificationActivity.lambda$onBackPressed$323();
                    }
                }
            }
        });
        FrameLayout frameLayout = new FrameLayout(context);
        this.fragmentView = frameLayout;
        int i5 = Theme.key_windowBackgroundWhite;
        frameLayout.setBackgroundColor(Theme.getColor(i5));
        ScrollView scrollView = new ScrollView(context);
        this.scrollView = scrollView;
        scrollView.setFillViewport(true);
        frameLayout.addView(this.scrollView, LayoutHelper.createFrame(-1, -1.0f));
        LinearLayout linearLayout = new LinearLayout(context);
        linearLayout.setOrientation(1);
        linearLayout.setGravity(1);
        this.scrollView.addView(linearLayout, LayoutHelper.createScroll(-1, -2, 51));
        RLottieImageView rLottieImageView = new RLottieImageView(context);
        this.lockImageView = rLottieImageView;
        rLottieImageView.setAnimation(R.raw.tsv_setup_intro, 100, 100);
        this.lockImageView.playAnimation();
        RLottieImageView rLottieImageView2 = this.lockImageView;
        if (!AndroidUtilities.isSmallScreen()) {
            Point point = AndroidUtilities.displaySize;
            if (point.x <= point.y || AndroidUtilities.isTablet()) {
                i = 0;
                rLottieImageView2.setVisibility(i);
                linearLayout.addView(this.lockImageView, LayoutHelper.createLinear(100, 100, 1));
                TextView textView2 = new TextView(context);
                this.titleTextView = textView2;
                int i6 = Theme.key_windowBackgroundWhiteBlackText;
                textView2.setTextColor(Theme.getColor(i6));
                this.titleTextView.setTextSize(1, 18.0f);
                this.titleTextView.setGravity(1);
                this.titleTextView.setTypeface(AndroidUtilities.bold());
                linearLayout.addView(this.titleTextView, LayoutHelper.createLinear(-2, -2, 1, 24, 8, 24, 0));
                TextView textView3 = new TextView(context);
                this.subtitleTextView = textView3;
                int i7 = Theme.key_windowBackgroundWhiteGrayText6;
                textView3.setTextColor(Theme.getColor(i7));
                this.subtitleTextView.setTextSize(1, 15.0f);
                this.subtitleTextView.setGravity(1);
                this.subtitleTextView.setVisibility(8);
                linearLayout.addView(this.subtitleTextView, LayoutHelper.createLinear(-2, -2, 1, 42, 8, 42, 0));
                OutlineTextContainerView outlineTextContainerView = new OutlineTextContainerView(context);
                this.passwordOutlineView = outlineTextContainerView;
                int i8 = R.string.EnterPassword;
                outlineTextContainerView.setText(LocaleController.getString(i8));
                this.passwordOutlineView.animateSelection(1.0f, false);
                linearLayout.addView(this.passwordOutlineView, LayoutHelper.createLinear(-1, -2, 1, 24, 24, 24, 0));
                EditTextBoldCursor editTextBoldCursor = new EditTextBoldCursor(context);
                this.passwordEditText = editTextBoldCursor;
                editTextBoldCursor.setTextSize(1, 18.0f);
                this.passwordEditText.setTextColor(Theme.getColor(i6));
                this.passwordEditText.setHintTextColor(Theme.getColor(Theme.key_windowBackgroundWhiteHintText));
                this.passwordEditText.setBackground(null);
                this.passwordEditText.setSingleLine(true);
                this.passwordEditText.setInputType(NotificationCenter.walletPendingTransactionsChanged);
                this.passwordEditText.setTransformationMethod(PasswordTransformationMethod.getInstance());
                this.passwordEditText.setTypeface(Typeface.DEFAULT);
                EditTextBoldCursor editTextBoldCursor2 = this.passwordEditText;
                int i9 = Theme.key_windowBackgroundWhiteInputFieldActivated;
                editTextBoldCursor2.setCursorColor(Theme.getColor(i9));
                this.passwordEditText.setCursorWidth(1.5f);
                this.passwordEditText.setContentDescription(LocaleController.getString(i8));
                int dp = AndroidUtilities.dp(16.0f);
                this.passwordEditText.setPadding(dp, dp, dp, dp);
                this.passwordOutlineView.addView(this.passwordEditText, LayoutHelper.createFrame(-1, -2.0f));
                this.passwordOutlineView.attachEditText(this.passwordEditText);
                this.passwordEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() { // from class: org.telegram.ui.TwoStepVerificationActivity$$ExternalSyntheticLambda4
                    @Override // android.view.View.OnFocusChangeListener
                    public final void onFocusChange(View view2, boolean z) {
                        TwoStepVerificationActivity.this.lambda$createView$1(view2, z);
                    }
                });
                this.passwordEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() { // from class: org.telegram.ui.TwoStepVerificationActivity$$ExternalSyntheticLambda5
                    @Override // android.widget.TextView.OnEditorActionListener
                    public final boolean onEditorAction(TextView textView4, int i10, KeyEvent keyEvent) {
                        boolean lambda$createView$2;
                        lambda$createView$2 = TwoStepVerificationActivity.this.lambda$createView$2(textView4, i10, keyEvent);
                        return lambda$createView$2;
                    }
                });
                this.passwordEditText.addTextChangedListener(new TextWatcher() { // from class: org.telegram.ui.TwoStepVerificationActivity.2
                    @Override // android.text.TextWatcher
                    public void afterTextChanged(Editable editable) {
                        if (TwoStepVerificationActivity.this.postedErrorColorTimeout) {
                            AndroidUtilities.cancelRunOnUIThread(TwoStepVerificationActivity.this.errorColorTimeout);
                            TwoStepVerificationActivity.this.errorColorTimeout.run();
                        }
                    }

                    @Override // android.text.TextWatcher
                    public void beforeTextChanged(CharSequence charSequence, int i10, int i11, int i12) {
                    }

                    @Override // android.text.TextWatcher
                    public void onTextChanged(CharSequence charSequence, int i10, int i11, int i12) {
                    }
                });
                TextView textView4 = new TextView(context);
                this.bottomTextView = textView4;
                textView4.setTextColor(Theme.getColor(i7));
                this.bottomTextView.setTextSize(1, 14.0f);
                this.bottomTextView.setGravity((!LocaleController.isRTL ? 5 : 3) | 48);
                this.bottomTextView.setText(LocaleController.getString(R.string.YourEmailInfo));
                linearLayout.addView(this.bottomTextView, LayoutHelper.createLinear(-2, -2, (!LocaleController.isRTL ? 5 : 3) | 48, 40, 30, 40, 0));
                TextView textView5 = new TextView(context);
                this.resetWaitView = textView5;
                textView5.setTextColor(Theme.getColor(i7));
                this.resetWaitView.setTextSize(1, 12.0f);
                this.resetWaitView.setGravity((LocaleController.isRTL ? 5 : 3) | 48);
                linearLayout.addView(this.resetWaitView, LayoutHelper.createLinear(-1, -2, 40.0f, 8.0f, 40.0f, 0.0f));
                LinearLayout linearLayout2 = new LinearLayout(context);
                linearLayout2.setOrientation(1);
                linearLayout2.setGravity(80);
                linearLayout2.setClipChildren(false);
                linearLayout.addView(linearLayout2, LayoutHelper.createLinear(-1, 0, 1.0f));
                SimpleTextView simpleTextView = new SimpleTextView(context);
                this.bottomButton = simpleTextView;
                simpleTextView.setTextSize(15);
                this.bottomButton.setGravity(19);
                this.bottomButton.setPadding(AndroidUtilities.dp(32.0f), 0, AndroidUtilities.dp(32.0f), 0);
                SimpleTextView simpleTextView2 = this.bottomButton;
                i2 = Build.VERSION.SDK_INT;
                frameLayout.addView(simpleTextView2, LayoutHelper.createFrame(-1, i2 < 21 ? 56.0f : 60.0f, 80, 0.0f, 0.0f, 0.0f, 16.0f));
                this.bottomButton.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.TwoStepVerificationActivity$$ExternalSyntheticLambda6
                    @Override // android.view.View.OnClickListener
                    public final void onClick(View view2) {
                        TwoStepVerificationActivity.this.lambda$createView$3(view2);
                    }
                });
                VerticalPositionAutoAnimator.attach(this.bottomButton);
                TextView textView6 = new TextView(context);
                this.cancelResetButton = textView6;
                textView6.setTextSize(1, 15.0f);
                this.cancelResetButton.setGravity(19);
                this.cancelResetButton.setPadding(AndroidUtilities.dp(32.0f), 0, AndroidUtilities.dp(32.0f), 0);
                this.cancelResetButton.setText(LocaleController.getString(R.string.CancelReset));
                this.cancelResetButton.setTextColor(Theme.getColor(Theme.key_windowBackgroundWhiteBlueText4));
                this.cancelResetButton.setVisibility(8);
                frameLayout.addView(this.cancelResetButton, LayoutHelper.createFrame(-1, i2 < 21 ? 56.0f : 60.0f, 80, 0.0f, 0.0f, 0.0f, 16.0f));
                this.cancelResetButton.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.TwoStepVerificationActivity$$ExternalSyntheticLambda7
                    @Override // android.view.View.OnClickListener
                    public final void onClick(View view2) {
                        TwoStepVerificationActivity.this.lambda$createView$4(view2);
                    }
                });
                VerticalPositionAutoAnimator.attach(this.cancelResetButton);
                this.floatingButtonContainer = new FrameLayout(context);
                if (i2 >= 21) {
                    StateListAnimator stateListAnimator = new StateListAnimator();
                    stateListAnimator.addState(new int[]{android.R.attr.state_pressed}, ObjectAnimator.ofFloat(this.floatingButtonIcon, "translationZ", AndroidUtilities.dp(2.0f), AndroidUtilities.dp(4.0f)).setDuration(200L));
                    stateListAnimator.addState(new int[0], ObjectAnimator.ofFloat(this.floatingButtonIcon, "translationZ", AndroidUtilities.dp(4.0f), AndroidUtilities.dp(2.0f)).setDuration(200L));
                    this.floatingButtonContainer.setStateListAnimator(stateListAnimator);
                    this.floatingButtonContainer.setOutlineProvider(new ViewOutlineProvider() { // from class: org.telegram.ui.TwoStepVerificationActivity.3
                        @Override // android.view.ViewOutlineProvider
                        public void getOutline(View view2, Outline outline) {
                            outline.setOval(0, 0, AndroidUtilities.dp(56.0f), AndroidUtilities.dp(56.0f));
                        }
                    });
                }
                VerticalPositionAutoAnimator.attach(this.floatingButtonContainer);
                this.floatingButtonContainer.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.TwoStepVerificationActivity$$ExternalSyntheticLambda8
                    @Override // android.view.View.OnClickListener
                    public final void onClick(View view2) {
                        TwoStepVerificationActivity.this.lambda$createView$5(view2);
                    }
                });
                TransformableLoginButtonView transformableLoginButtonView = new TransformableLoginButtonView(context);
                this.floatingButtonIcon = transformableLoginButtonView;
                transformableLoginButtonView.setTransformType(1);
                this.floatingButtonIcon.setProgress(0.0f);
                this.floatingButtonIcon.setColor(Theme.getColor(Theme.key_chats_actionIcon));
                this.floatingButtonIcon.setDrawBackground(false);
                this.floatingButtonContainer.setContentDescription(LocaleController.getString(R.string.Next));
                this.floatingButtonContainer.addView(this.floatingButtonIcon, LayoutHelper.createFrame(i2 < 21 ? 56 : 60, i2 < 21 ? 56.0f : 60.0f));
                Drawable createSimpleSelectorCircleDrawable = Theme.createSimpleSelectorCircleDrawable(AndroidUtilities.dp(56.0f), Theme.getColor(Theme.key_chats_actionBackground), Theme.getColor(Theme.key_chats_actionPressedBackground));
                if (i2 >= 21) {
                    Drawable mutate = context.getResources().getDrawable(R.drawable.floating_shadow).mutate();
                    mutate.setColorFilter(new PorterDuffColorFilter(-16777216, PorterDuff.Mode.MULTIPLY));
                    CombinedDrawable combinedDrawable = new CombinedDrawable(mutate, createSimpleSelectorCircleDrawable, 0, 0);
                    combinedDrawable.setIconSize(AndroidUtilities.dp(56.0f), AndroidUtilities.dp(56.0f));
                    createSimpleSelectorCircleDrawable = combinedDrawable;
                }
                this.floatingButtonContainer.setBackground(createSimpleSelectorCircleDrawable);
                frameLayout.addView(this.floatingButtonContainer, LayoutHelper.createFrame(i2 < 21 ? 56 : 60, i2 < 21 ? 56.0f : 60.0f, 85, 0.0f, 0.0f, 24.0f, 16.0f));
                EmptyTextProgressView emptyTextProgressView = new EmptyTextProgressView(context);
                this.emptyView = emptyTextProgressView;
                emptyTextProgressView.showProgress();
                frameLayout.addView(this.emptyView, LayoutHelper.createFrame(-1, -1.0f));
                RecyclerListView recyclerListView = new RecyclerListView(context);
                this.listView = recyclerListView;
                recyclerListView.setLayoutManager(new LinearLayoutManager(context, 1, false));
                this.listView.setEmptyView(this.emptyView);
                this.listView.setVerticalScrollBarEnabled(false);
                frameLayout.addView(this.listView, LayoutHelper.createFrame(-1, -1.0f));
                RecyclerListView recyclerListView2 = this.listView;
                ListAdapter listAdapter = new ListAdapter(context);
                this.listAdapter = listAdapter;
                recyclerListView2.setAdapter(listAdapter);
                this.listView.setOnItemClickListener(new RecyclerListView.OnItemClickListener() { // from class: org.telegram.ui.TwoStepVerificationActivity$$ExternalSyntheticLambda9
                    @Override // org.telegram.ui.Components.RecyclerListView.OnItemClickListener
                    public final void onItemClick(View view2, int i10) {
                        TwoStepVerificationActivity.this.lambda$createView$7(view2, i10);
                    }
                });
                RadialProgressView radialProgressView = new RadialProgressView(context) { // from class: org.telegram.ui.TwoStepVerificationActivity.4
                    @Override // android.view.View
                    protected void onMeasure(int i10, int i11) {
                        super.onMeasure(i10, i11);
                        ((ViewGroup.MarginLayoutParams) getLayoutParams()).topMargin = AndroidUtilities.statusBarHeight / 2;
                    }
                };
                this.radialProgressView = radialProgressView;
                radialProgressView.setSize(AndroidUtilities.dp(20.0f));
                this.radialProgressView.setAlpha(0.0f);
                this.radialProgressView.setScaleX(0.1f);
                this.radialProgressView.setScaleY(0.1f);
                this.radialProgressView.setProgressColor(Theme.getColor(i9));
                this.actionBar.addView(this.radialProgressView, LayoutHelper.createFrame(32, 32.0f, 21, 0.0f, 0.0f, 12.0f, 0.0f));
                updateRows();
                if (this.passwordEntered || this.delegate != null) {
                    actionBar = this.actionBar;
                    str = null;
                } else {
                    actionBar = this.actionBar;
                    str = LocaleController.getString(R.string.TwoStepVerificationTitle);
                }
                actionBar.setTitle(str);
                if (this.delegate == null) {
                    this.titleTextView.setText(LocaleController.getString(R.string.YourPassword));
                    int i10 = this.delegateType;
                    if (i10 == 1) {
                        textView = this.subtitleTextView;
                        i3 = R.string.PleaseEnterCurrentPasswordWithdraw;
                    } else if (i10 == 2) {
                        textView = this.subtitleTextView;
                        replaceTags = AndroidUtilities.replaceTags(LocaleController.formatString(R.string.PleaseEnterCurrentPasswordGiftTransfer, this.delegateTypeString));
                        textView.setText(replaceTags);
                        this.subtitleTextView.setVisibility(0);
                    } else {
                        textView = this.subtitleTextView;
                        i3 = R.string.PleaseEnterCurrentPasswordTransfer;
                    }
                    replaceTags = LocaleController.getString(i3);
                    textView.setText(replaceTags);
                    this.subtitleTextView.setVisibility(0);
                } else {
                    this.titleTextView.setText(LocaleController.getString(R.string.YourPassword));
                    this.subtitleTextView.setVisibility(0);
                    this.subtitleTextView.setText(LocaleController.getString(R.string.LoginPasswordTextShort));
                }
                if (this.passwordEntered) {
                    this.fragmentView.setBackgroundColor(Theme.getColor(i5));
                    view = this.fragmentView;
                    valueOf = Integer.valueOf(i5);
                } else {
                    View view2 = this.fragmentView;
                    int i11 = Theme.key_windowBackgroundGray;
                    view2.setBackgroundColor(Theme.getColor(i11));
                    view = this.fragmentView;
                    valueOf = Integer.valueOf(i11);
                }
                view.setTag(valueOf);
                return this.fragmentView;
            }
        }
        i = 8;
        rLottieImageView2.setVisibility(i);
        linearLayout.addView(this.lockImageView, LayoutHelper.createLinear(100, 100, 1));
        TextView textView22 = new TextView(context);
        this.titleTextView = textView22;
        int i62 = Theme.key_windowBackgroundWhiteBlackText;
        textView22.setTextColor(Theme.getColor(i62));
        this.titleTextView.setTextSize(1, 18.0f);
        this.titleTextView.setGravity(1);
        this.titleTextView.setTypeface(AndroidUtilities.bold());
        linearLayout.addView(this.titleTextView, LayoutHelper.createLinear(-2, -2, 1, 24, 8, 24, 0));
        TextView textView32 = new TextView(context);
        this.subtitleTextView = textView32;
        int i72 = Theme.key_windowBackgroundWhiteGrayText6;
        textView32.setTextColor(Theme.getColor(i72));
        this.subtitleTextView.setTextSize(1, 15.0f);
        this.subtitleTextView.setGravity(1);
        this.subtitleTextView.setVisibility(8);
        linearLayout.addView(this.subtitleTextView, LayoutHelper.createLinear(-2, -2, 1, 42, 8, 42, 0));
        OutlineTextContainerView outlineTextContainerView2 = new OutlineTextContainerView(context);
        this.passwordOutlineView = outlineTextContainerView2;
        int i82 = R.string.EnterPassword;
        outlineTextContainerView2.setText(LocaleController.getString(i82));
        this.passwordOutlineView.animateSelection(1.0f, false);
        linearLayout.addView(this.passwordOutlineView, LayoutHelper.createLinear(-1, -2, 1, 24, 24, 24, 0));
        EditTextBoldCursor editTextBoldCursor3 = new EditTextBoldCursor(context);
        this.passwordEditText = editTextBoldCursor3;
        editTextBoldCursor3.setTextSize(1, 18.0f);
        this.passwordEditText.setTextColor(Theme.getColor(i62));
        this.passwordEditText.setHintTextColor(Theme.getColor(Theme.key_windowBackgroundWhiteHintText));
        this.passwordEditText.setBackground(null);
        this.passwordEditText.setSingleLine(true);
        this.passwordEditText.setInputType(NotificationCenter.walletPendingTransactionsChanged);
        this.passwordEditText.setTransformationMethod(PasswordTransformationMethod.getInstance());
        this.passwordEditText.setTypeface(Typeface.DEFAULT);
        EditTextBoldCursor editTextBoldCursor22 = this.passwordEditText;
        int i92 = Theme.key_windowBackgroundWhiteInputFieldActivated;
        editTextBoldCursor22.setCursorColor(Theme.getColor(i92));
        this.passwordEditText.setCursorWidth(1.5f);
        this.passwordEditText.setContentDescription(LocaleController.getString(i82));
        int dp2 = AndroidUtilities.dp(16.0f);
        this.passwordEditText.setPadding(dp2, dp2, dp2, dp2);
        this.passwordOutlineView.addView(this.passwordEditText, LayoutHelper.createFrame(-1, -2.0f));
        this.passwordOutlineView.attachEditText(this.passwordEditText);
        this.passwordEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() { // from class: org.telegram.ui.TwoStepVerificationActivity$$ExternalSyntheticLambda4
            @Override // android.view.View.OnFocusChangeListener
            public final void onFocusChange(View view22, boolean z) {
                TwoStepVerificationActivity.this.lambda$createView$1(view22, z);
            }
        });
        this.passwordEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() { // from class: org.telegram.ui.TwoStepVerificationActivity$$ExternalSyntheticLambda5
            @Override // android.widget.TextView.OnEditorActionListener
            public final boolean onEditorAction(TextView textView42, int i102, KeyEvent keyEvent) {
                boolean lambda$createView$2;
                lambda$createView$2 = TwoStepVerificationActivity.this.lambda$createView$2(textView42, i102, keyEvent);
                return lambda$createView$2;
            }
        });
        this.passwordEditText.addTextChangedListener(new TextWatcher() { // from class: org.telegram.ui.TwoStepVerificationActivity.2
            @Override // android.text.TextWatcher
            public void afterTextChanged(Editable editable) {
                if (TwoStepVerificationActivity.this.postedErrorColorTimeout) {
                    AndroidUtilities.cancelRunOnUIThread(TwoStepVerificationActivity.this.errorColorTimeout);
                    TwoStepVerificationActivity.this.errorColorTimeout.run();
                }
            }

            @Override // android.text.TextWatcher
            public void beforeTextChanged(CharSequence charSequence, int i102, int i112, int i12) {
            }

            @Override // android.text.TextWatcher
            public void onTextChanged(CharSequence charSequence, int i102, int i112, int i12) {
            }
        });
        TextView textView42 = new TextView(context);
        this.bottomTextView = textView42;
        textView42.setTextColor(Theme.getColor(i72));
        this.bottomTextView.setTextSize(1, 14.0f);
        this.bottomTextView.setGravity((!LocaleController.isRTL ? 5 : 3) | 48);
        this.bottomTextView.setText(LocaleController.getString(R.string.YourEmailInfo));
        linearLayout.addView(this.bottomTextView, LayoutHelper.createLinear(-2, -2, (!LocaleController.isRTL ? 5 : 3) | 48, 40, 30, 40, 0));
        TextView textView52 = new TextView(context);
        this.resetWaitView = textView52;
        textView52.setTextColor(Theme.getColor(i72));
        this.resetWaitView.setTextSize(1, 12.0f);
        this.resetWaitView.setGravity((LocaleController.isRTL ? 5 : 3) | 48);
        linearLayout.addView(this.resetWaitView, LayoutHelper.createLinear(-1, -2, 40.0f, 8.0f, 40.0f, 0.0f));
        LinearLayout linearLayout22 = new LinearLayout(context);
        linearLayout22.setOrientation(1);
        linearLayout22.setGravity(80);
        linearLayout22.setClipChildren(false);
        linearLayout.addView(linearLayout22, LayoutHelper.createLinear(-1, 0, 1.0f));
        SimpleTextView simpleTextView3 = new SimpleTextView(context);
        this.bottomButton = simpleTextView3;
        simpleTextView3.setTextSize(15);
        this.bottomButton.setGravity(19);
        this.bottomButton.setPadding(AndroidUtilities.dp(32.0f), 0, AndroidUtilities.dp(32.0f), 0);
        SimpleTextView simpleTextView22 = this.bottomButton;
        i2 = Build.VERSION.SDK_INT;
        frameLayout.addView(simpleTextView22, LayoutHelper.createFrame(-1, i2 < 21 ? 56.0f : 60.0f, 80, 0.0f, 0.0f, 0.0f, 16.0f));
        this.bottomButton.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.TwoStepVerificationActivity$$ExternalSyntheticLambda6
            @Override // android.view.View.OnClickListener
            public final void onClick(View view22) {
                TwoStepVerificationActivity.this.lambda$createView$3(view22);
            }
        });
        VerticalPositionAutoAnimator.attach(this.bottomButton);
        TextView textView62 = new TextView(context);
        this.cancelResetButton = textView62;
        textView62.setTextSize(1, 15.0f);
        this.cancelResetButton.setGravity(19);
        this.cancelResetButton.setPadding(AndroidUtilities.dp(32.0f), 0, AndroidUtilities.dp(32.0f), 0);
        this.cancelResetButton.setText(LocaleController.getString(R.string.CancelReset));
        this.cancelResetButton.setTextColor(Theme.getColor(Theme.key_windowBackgroundWhiteBlueText4));
        this.cancelResetButton.setVisibility(8);
        frameLayout.addView(this.cancelResetButton, LayoutHelper.createFrame(-1, i2 < 21 ? 56.0f : 60.0f, 80, 0.0f, 0.0f, 0.0f, 16.0f));
        this.cancelResetButton.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.TwoStepVerificationActivity$$ExternalSyntheticLambda7
            @Override // android.view.View.OnClickListener
            public final void onClick(View view22) {
                TwoStepVerificationActivity.this.lambda$createView$4(view22);
            }
        });
        VerticalPositionAutoAnimator.attach(this.cancelResetButton);
        this.floatingButtonContainer = new FrameLayout(context);
        if (i2 >= 21) {
        }
        VerticalPositionAutoAnimator.attach(this.floatingButtonContainer);
        this.floatingButtonContainer.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.TwoStepVerificationActivity$$ExternalSyntheticLambda8
            @Override // android.view.View.OnClickListener
            public final void onClick(View view22) {
                TwoStepVerificationActivity.this.lambda$createView$5(view22);
            }
        });
        TransformableLoginButtonView transformableLoginButtonView2 = new TransformableLoginButtonView(context);
        this.floatingButtonIcon = transformableLoginButtonView2;
        transformableLoginButtonView2.setTransformType(1);
        this.floatingButtonIcon.setProgress(0.0f);
        this.floatingButtonIcon.setColor(Theme.getColor(Theme.key_chats_actionIcon));
        this.floatingButtonIcon.setDrawBackground(false);
        this.floatingButtonContainer.setContentDescription(LocaleController.getString(R.string.Next));
        this.floatingButtonContainer.addView(this.floatingButtonIcon, LayoutHelper.createFrame(i2 < 21 ? 56 : 60, i2 < 21 ? 56.0f : 60.0f));
        Drawable createSimpleSelectorCircleDrawable2 = Theme.createSimpleSelectorCircleDrawable(AndroidUtilities.dp(56.0f), Theme.getColor(Theme.key_chats_actionBackground), Theme.getColor(Theme.key_chats_actionPressedBackground));
        if (i2 >= 21) {
        }
        this.floatingButtonContainer.setBackground(createSimpleSelectorCircleDrawable2);
        frameLayout.addView(this.floatingButtonContainer, LayoutHelper.createFrame(i2 < 21 ? 56 : 60, i2 < 21 ? 56.0f : 60.0f, 85, 0.0f, 0.0f, 24.0f, 16.0f));
        EmptyTextProgressView emptyTextProgressView2 = new EmptyTextProgressView(context);
        this.emptyView = emptyTextProgressView2;
        emptyTextProgressView2.showProgress();
        frameLayout.addView(this.emptyView, LayoutHelper.createFrame(-1, -1.0f));
        RecyclerListView recyclerListView3 = new RecyclerListView(context);
        this.listView = recyclerListView3;
        recyclerListView3.setLayoutManager(new LinearLayoutManager(context, 1, false));
        this.listView.setEmptyView(this.emptyView);
        this.listView.setVerticalScrollBarEnabled(false);
        frameLayout.addView(this.listView, LayoutHelper.createFrame(-1, -1.0f));
        RecyclerListView recyclerListView22 = this.listView;
        ListAdapter listAdapter2 = new ListAdapter(context);
        this.listAdapter = listAdapter2;
        recyclerListView22.setAdapter(listAdapter2);
        this.listView.setOnItemClickListener(new RecyclerListView.OnItemClickListener() { // from class: org.telegram.ui.TwoStepVerificationActivity$$ExternalSyntheticLambda9
            @Override // org.telegram.ui.Components.RecyclerListView.OnItemClickListener
            public final void onItemClick(View view22, int i102) {
                TwoStepVerificationActivity.this.lambda$createView$7(view22, i102);
            }
        });
        RadialProgressView radialProgressView2 = new RadialProgressView(context) { // from class: org.telegram.ui.TwoStepVerificationActivity.4
            @Override // android.view.View
            protected void onMeasure(int i102, int i112) {
                super.onMeasure(i102, i112);
                ((ViewGroup.MarginLayoutParams) getLayoutParams()).topMargin = AndroidUtilities.statusBarHeight / 2;
            }
        };
        this.radialProgressView = radialProgressView2;
        radialProgressView2.setSize(AndroidUtilities.dp(20.0f));
        this.radialProgressView.setAlpha(0.0f);
        this.radialProgressView.setScaleX(0.1f);
        this.radialProgressView.setScaleY(0.1f);
        this.radialProgressView.setProgressColor(Theme.getColor(i92));
        this.actionBar.addView(this.radialProgressView, LayoutHelper.createFrame(32, 32.0f, 21, 0.0f, 0.0f, 12.0f, 0.0f));
        updateRows();
        if (this.passwordEntered) {
        }
        actionBar = this.actionBar;
        str = null;
        actionBar.setTitle(str);
        if (this.delegate == null) {
        }
        if (this.passwordEntered) {
        }
        view.setTag(valueOf);
        return this.fragmentView;
    }

    @Override // org.telegram.messenger.NotificationCenter.NotificationCenterDelegate
    public void didReceivedNotification(int i, int i2, Object... objArr) {
        Object obj;
        if (i == NotificationCenter.twoStepPasswordChanged) {
            if (objArr != null && objArr.length > 0 && (obj = objArr[0]) != null) {
                this.currentPasswordHash = (byte[]) obj;
            }
            loadPasswordInfo(false, false, null);
            updateRows();
        }
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    /* renamed from: finishFragment */
    public void lambda$onBackPressed$323() {
        if (this.otherwiseReloginDays < 0) {
            super.lambda$onBackPressed$323();
            return;
        }
        Bundle bundle = new Bundle();
        bundle.putBoolean("afterSignup", true);
        presentFragment(new DialogsActivity(bundle), true);
    }

    public TLRPC.TL_inputCheckPasswordSRP getNewSrpPassword() {
        TL_account.Password password = this.currentPassword;
        TLRPC.PasswordKdfAlgo passwordKdfAlgo = password.current_algo;
        if (!(passwordKdfAlgo instanceof TLRPC.TL_passwordKdfAlgoSHA256SHA256PBKDF2HMACSHA512iter100000SHA256ModPow)) {
            return null;
        }
        return SRPHelper.startCheck(this.currentPasswordHash, password.srp_id, password.srp_B, (TLRPC.TL_passwordKdfAlgoSHA256SHA256PBKDF2HMACSHA512iter100000SHA256ModPow) passwordKdfAlgo);
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public ArrayList getThemeDescriptions() {
        ArrayList arrayList = new ArrayList();
        int i = Theme.key_windowBackgroundWhite;
        arrayList.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_CELLBACKGROUNDCOLOR, new Class[]{TextSettingsCell.class, EditTextSettingsCell.class}, null, null, null, i));
        arrayList.add(new ThemeDescription(this.fragmentView, ThemeDescription.FLAG_BACKGROUND | ThemeDescription.FLAG_CHECKTAG, null, null, null, null, i));
        arrayList.add(new ThemeDescription(this.fragmentView, ThemeDescription.FLAG_BACKGROUND | ThemeDescription.FLAG_CHECKTAG, null, null, null, null, Theme.key_windowBackgroundGray));
        ActionBar actionBar = this.actionBar;
        int i2 = ThemeDescription.FLAG_BACKGROUND;
        int i3 = Theme.key_actionBarDefault;
        arrayList.add(new ThemeDescription(actionBar, i2, null, null, null, null, i3));
        arrayList.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_LISTGLOWCOLOR, null, null, null, null, i3));
        arrayList.add(new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_ITEMSCOLOR, null, null, null, null, Theme.key_actionBarDefaultIcon));
        arrayList.add(new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_TITLECOLOR, null, null, null, null, Theme.key_actionBarDefaultTitle));
        arrayList.add(new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_SELECTORCOLOR, null, null, null, null, Theme.key_actionBarDefaultSelector));
        arrayList.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_SELECTOR, null, null, null, null, Theme.key_listSelector));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{View.class}, Theme.dividerPaint, null, null, Theme.key_divider));
        arrayList.add(new ThemeDescription(this.emptyView, ThemeDescription.FLAG_PROGRESSBAR, null, null, null, null, Theme.key_progressCircle));
        int i4 = Theme.key_windowBackgroundWhiteBlackText;
        arrayList.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_CHECKTAG, new Class[]{TextSettingsCell.class}, new String[]{"textView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, i4));
        arrayList.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_CHECKTAG, new Class[]{TextSettingsCell.class}, new String[]{"textView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, Theme.key_text_RedRegular));
        arrayList.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_TEXTCOLOR, new Class[]{EditTextSettingsCell.class}, new String[]{"textView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, i4));
        int i5 = Theme.key_windowBackgroundWhiteHintText;
        arrayList.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_HINTTEXTCOLOR, new Class[]{EditTextSettingsCell.class}, new String[]{"textView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, i5));
        arrayList.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_BACKGROUNDFILTER, new Class[]{TextInfoPrivacyCell.class}, null, null, null, Theme.key_windowBackgroundGrayShadow));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{TextInfoPrivacyCell.class}, new String[]{"textView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, Theme.key_windowBackgroundWhiteGrayText4));
        TextView textView = this.titleTextView;
        int i6 = ThemeDescription.FLAG_TEXTCOLOR;
        int i7 = Theme.key_windowBackgroundWhiteGrayText6;
        arrayList.add(new ThemeDescription(textView, i6, null, null, null, null, i7));
        arrayList.add(new ThemeDescription(this.bottomTextView, ThemeDescription.FLAG_TEXTCOLOR, null, null, null, null, i7));
        arrayList.add(new ThemeDescription(this.bottomButton, ThemeDescription.FLAG_TEXTCOLOR, null, null, null, null, Theme.key_windowBackgroundWhiteBlueText4));
        arrayList.add(new ThemeDescription(this.passwordEditText, ThemeDescription.FLAG_TEXTCOLOR, null, null, null, null, i4));
        arrayList.add(new ThemeDescription(this.passwordEditText, ThemeDescription.FLAG_HINTTEXTCOLOR, null, null, null, null, i5));
        arrayList.add(new ThemeDescription(this.passwordEditText, ThemeDescription.FLAG_BACKGROUNDFILTER, null, null, null, null, Theme.key_windowBackgroundWhiteInputField));
        arrayList.add(new ThemeDescription(this.passwordEditText, ThemeDescription.FLAG_BACKGROUNDFILTER | ThemeDescription.FLAG_DRAWABLESELECTEDSTATE, null, null, null, null, Theme.key_windowBackgroundWhiteInputFieldActivated));
        return arrayList;
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public boolean isLightStatusBar() {
        return ColorUtils.calculateLuminance(Theme.getColor(Theme.key_windowBackgroundWhite, null, true)) > 0.699999988079071d;
    }

    public void needHideProgress() {
        if (!this.passwordEntered) {
            AnimatorSet animatorSet = new AnimatorSet();
            animatorSet.playTogether(ObjectAnimator.ofFloat(this.radialProgressView, (Property<RadialProgressView, Float>) View.ALPHA, 0.0f), ObjectAnimator.ofFloat(this.radialProgressView, (Property<RadialProgressView, Float>) View.SCALE_X, 0.1f), ObjectAnimator.ofFloat(this.radialProgressView, (Property<RadialProgressView, Float>) View.SCALE_Y, 0.1f));
            animatorSet.setInterpolator(CubicBezierInterpolator.DEFAULT);
            animatorSet.start();
            return;
        }
        AlertDialog alertDialog = this.progressDialog;
        if (alertDialog == null) {
            return;
        }
        try {
            alertDialog.dismiss();
        } catch (Exception e) {
            FileLog.e(e);
        }
        this.progressDialog = null;
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public boolean onBackPressed() {
        if (this.otherwiseReloginDays < 0) {
            return super.onBackPressed();
        }
        showSetForcePasswordAlert();
        return false;
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public void onConfigurationChanged(Configuration configuration) {
        int i;
        super.onConfigurationChanged(configuration);
        RLottieImageView rLottieImageView = this.lockImageView;
        if (!AndroidUtilities.isSmallScreen()) {
            Point point = AndroidUtilities.displaySize;
            if (point.x <= point.y || AndroidUtilities.isTablet()) {
                i = 0;
                rLottieImageView.setVisibility(i);
            }
        }
        i = 8;
        rLottieImageView.setVisibility(i);
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public boolean onFragmentCreate() {
        super.onFragmentCreate();
        if (!this.preloaded) {
            preload(null);
        }
        updateRows();
        NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.twoStepPasswordChanged);
        return true;
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public void onFragmentDestroy() {
        super.onFragmentDestroy();
        AndroidUtilities.cancelRunOnUIThread(this.updateTimeRunnable);
        NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.twoStepPasswordChanged);
        this.destroyed = true;
        AlertDialog alertDialog = this.progressDialog;
        if (alertDialog != null) {
            try {
                alertDialog.dismiss();
            } catch (Exception e) {
                FileLog.e(e);
            }
            this.progressDialog = null;
        }
        AndroidUtilities.removeAdjustResize(getParentActivity(), this.classGuid);
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public void onPause() {
        super.onPause();
        this.paused = true;
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public void onResume() {
        super.onResume();
        this.paused = false;
        AndroidUtilities.requestAdjustResize(getParentActivity(), this.classGuid);
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public void onTransitionAnimationEnd(boolean z, boolean z2) {
        super.onTransitionAnimationEnd(z, z2);
        if (z) {
            if (this.forgotPasswordOnShow) {
                onPasswordForgot();
                this.forgotPasswordOnShow = false;
            } else if (this.resetPasswordOnShow) {
                resetPassword();
                this.resetPasswordOnShow = false;
            }
        }
    }

    public void preload(Runnable runnable) {
        byte[] bArr;
        this.preloaded = false;
        TL_account.Password password = this.currentPassword;
        if (password == null || password.current_algo == null || (bArr = this.currentPasswordHash) == null || bArr.length <= 0) {
            loadPasswordInfo(true, password != null, runnable);
        } else if (runnable != null) {
            runnable.run();
        }
    }

    public void setBlockingAlert(int i) {
        this.otherwiseReloginDays = i;
    }

    public void setCurrentPasswordInfo(byte[] bArr, TL_account.Password password) {
        if (bArr != null) {
            this.currentPasswordHash = bArr;
        }
        this.currentPassword = password;
    }

    public void setCurrentPasswordParams(TL_account.Password password, byte[] bArr, long j, byte[] bArr2) {
        this.currentPassword = password;
        this.currentPasswordHash = bArr;
        this.currentSecret = bArr2;
        this.currentSecretId = j;
        this.passwordEntered = (bArr != null && bArr.length > 0) || !password.has_password;
    }

    public void setDelegate(int i, TwoStepVerificationActivityDelegate twoStepVerificationActivityDelegate) {
        this.delegateType = i;
        this.delegate = twoStepVerificationActivityDelegate;
    }

    public void setDelegateString(String str) {
        this.delegateTypeString = str;
    }

    public void setForgotPasswordOnShow() {
        this.forgotPasswordOnShow = true;
    }

    public void setPassword(TL_account.Password password) {
        this.currentPassword = password;
        this.passwordEntered = false;
    }
}
