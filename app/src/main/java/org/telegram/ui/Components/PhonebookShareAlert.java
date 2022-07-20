package org.telegram.ui.Components;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.RectF;
import android.net.Uri;
import android.os.Build;
import android.os.Parcelable;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.Property;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.core.widget.NestedScrollView;
import java.io.File;
import java.util.ArrayList;
import java.util.Locale;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.ApplicationLoader;
import org.telegram.messenger.ContactsController;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.browser.Browser;
import org.telegram.tgnet.TLRPC$TL_restrictionReason;
import org.telegram.tgnet.TLRPC$TL_userContact_old2;
import org.telegram.tgnet.TLRPC$User;
import org.telegram.ui.ActionBar.ActionBar;
import org.telegram.ui.ActionBar.AlertDialog;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.ActionBar.BottomSheet;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.ChatActivity;
import org.telegram.ui.Components.Bulletin;
import org.telegram.ui.Components.ChatAttachAlertContactsLayout;
/* loaded from: classes3.dex */
public class PhonebookShareAlert extends BottomSheet {
    private ActionBar actionBar;
    private AnimatorSet actionBarAnimation;
    private View actionBarShadow;
    private Paint backgroundPaint;
    private TextView buttonTextView;
    private TLRPC$User currentUser;
    private ChatAttachAlertContactsLayout.PhonebookShareAlertDelegate delegate;
    private boolean inLayout;
    private boolean isImport;
    private LinearLayout linearLayout;
    private ListAdapter listAdapter;
    private ArrayList<AndroidUtilities.VcardItem> other;
    private BaseFragment parentFragment;
    private int phoneEndRow;
    private int phoneStartRow;
    private ArrayList<AndroidUtilities.VcardItem> phones;
    private int rowCount;
    private int scrollOffsetY;
    private NestedScrollView scrollView;
    private View shadow;
    private AnimatorSet shadowAnimation;
    private int userRow;
    private int vcardEndRow;
    private int vcardStartRow;

    @Override // org.telegram.ui.ActionBar.BottomSheet
    protected boolean canDismissWithSwipe() {
        return false;
    }

    /* loaded from: classes3.dex */
    public class UserCell extends LinearLayout {
        public UserCell(PhonebookShareAlert phonebookShareAlert, Context context) {
            super(context);
            boolean z;
            String str;
            setOrientation(1);
            if (phonebookShareAlert.phones.size() != 1 || phonebookShareAlert.other.size() != 0) {
                str = (phonebookShareAlert.currentUser.status == null || phonebookShareAlert.currentUser.status.expires == 0) ? null : LocaleController.formatUserStatus(((BottomSheet) phonebookShareAlert).currentAccount, phonebookShareAlert.currentUser);
                z = true;
            } else {
                str = ((AndroidUtilities.VcardItem) phonebookShareAlert.phones.get(0)).getValue(true);
                z = false;
            }
            AvatarDrawable avatarDrawable = new AvatarDrawable();
            avatarDrawable.setTextSize(AndroidUtilities.dp(30.0f));
            avatarDrawable.setInfo(phonebookShareAlert.currentUser);
            BackupImageView backupImageView = new BackupImageView(context);
            backupImageView.setRoundRadius(AndroidUtilities.dp(40.0f));
            backupImageView.setForUserOrChat(phonebookShareAlert.currentUser, avatarDrawable);
            addView(backupImageView, LayoutHelper.createLinear(80, 80, 49, 0, 32, 0, 0));
            TextView textView = new TextView(context);
            textView.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
            textView.setTextSize(1, 17.0f);
            textView.setTextColor(phonebookShareAlert.getThemedColor("dialogTextBlack"));
            textView.setSingleLine(true);
            textView.setEllipsize(TextUtils.TruncateAt.END);
            textView.setText(ContactsController.formatName(phonebookShareAlert.currentUser.first_name, phonebookShareAlert.currentUser.last_name));
            addView(textView, LayoutHelper.createLinear(-2, -2, 49, 10, 10, 10, str != null ? 0 : 27));
            if (str != null) {
                TextView textView2 = new TextView(context);
                textView2.setTextSize(1, 14.0f);
                textView2.setTextColor(phonebookShareAlert.getThemedColor("dialogTextGray3"));
                textView2.setSingleLine(true);
                textView2.setEllipsize(TextUtils.TruncateAt.END);
                textView2.setText(str);
                addView(textView2, LayoutHelper.createLinear(-2, -2, 49, 10, 3, 10, z ? 27 : 11));
            }
        }
    }

    /* loaded from: classes3.dex */
    public class TextCheckBoxCell extends FrameLayout {
        private Switch checkBox;
        private ImageView imageView;
        private boolean needDivider;
        private TextView textView;
        private TextView valueTextView;

        public TextCheckBoxCell(PhonebookShareAlert phonebookShareAlert, Context context) {
            super(context);
            float f;
            float f2;
            float f3;
            float f4;
            TextView textView = new TextView(context);
            this.textView = textView;
            textView.setTextColor(phonebookShareAlert.getThemedColor("windowBackgroundWhiteBlackText"));
            this.textView.setTextSize(1, 16.0f);
            this.textView.setSingleLine(false);
            int i = 5;
            this.textView.setGravity((LocaleController.isRTL ? 5 : 3) | 48);
            this.textView.setEllipsize(TextUtils.TruncateAt.END);
            TextView textView2 = this.textView;
            boolean z = LocaleController.isRTL;
            int i2 = (z ? 5 : 3) | 48;
            int i3 = 17;
            if (z) {
                f = phonebookShareAlert.isImport ? 17 : 64;
            } else {
                f = 72.0f;
            }
            if (LocaleController.isRTL) {
                f2 = 72.0f;
            } else {
                f2 = phonebookShareAlert.isImport ? 17 : 64;
            }
            addView(textView2, LayoutHelper.createFrame(-1, -1.0f, i2, f, 10.0f, f2, 0.0f));
            TextView textView3 = new TextView(context);
            this.valueTextView = textView3;
            textView3.setTextColor(phonebookShareAlert.getThemedColor("windowBackgroundWhiteGrayText2"));
            this.valueTextView.setTextSize(1, 13.0f);
            this.valueTextView.setLines(1);
            this.valueTextView.setMaxLines(1);
            this.valueTextView.setSingleLine(true);
            this.valueTextView.setGravity(LocaleController.isRTL ? 5 : 3);
            TextView textView4 = this.valueTextView;
            boolean z2 = LocaleController.isRTL;
            int i4 = z2 ? 5 : 3;
            if (z2) {
                f3 = phonebookShareAlert.isImport ? 17 : 64;
            } else {
                f3 = 72.0f;
            }
            if (LocaleController.isRTL) {
                f4 = 72.0f;
            } else {
                f4 = !phonebookShareAlert.isImport ? 64 : i3;
            }
            addView(textView4, LayoutHelper.createFrame(-2, -2.0f, i4, f3, 35.0f, f4, 0.0f));
            ImageView imageView = new ImageView(context);
            this.imageView = imageView;
            imageView.setScaleType(ImageView.ScaleType.CENTER);
            this.imageView.setColorFilter(new PorterDuffColorFilter(phonebookShareAlert.getThemedColor("windowBackgroundWhiteGrayIcon"), PorterDuff.Mode.MULTIPLY));
            ImageView imageView2 = this.imageView;
            boolean z3 = LocaleController.isRTL;
            addView(imageView2, LayoutHelper.createFrame(-2, -2.0f, (z3 ? 5 : 3) | 48, z3 ? 0.0f : 20.0f, 20.0f, z3 ? 20.0f : 0.0f, 0.0f));
            if (!phonebookShareAlert.isImport) {
                Switch r1 = new Switch(context);
                this.checkBox = r1;
                r1.setColors("switchTrack", "switchTrackChecked", "windowBackgroundWhite", "windowBackgroundWhite");
                addView(this.checkBox, LayoutHelper.createFrame(37, 40.0f, (LocaleController.isRTL ? 3 : i) | 16, 22.0f, 0.0f, 22.0f, 0.0f));
            }
        }

        @Override // android.view.View
        public void invalidate() {
            super.invalidate();
            Switch r0 = this.checkBox;
            if (r0 != null) {
                r0.invalidate();
            }
        }

        @Override // android.widget.FrameLayout, android.view.View
        protected void onMeasure(int i, int i2) {
            measureChildWithMargins(this.textView, i, 0, i2, 0);
            measureChildWithMargins(this.valueTextView, i, 0, i2, 0);
            measureChildWithMargins(this.imageView, i, 0, i2, 0);
            Switch r7 = this.checkBox;
            if (r7 != null) {
                measureChildWithMargins(r7, i, 0, i2, 0);
            }
            setMeasuredDimension(View.MeasureSpec.getSize(i), Math.max(AndroidUtilities.dp(64.0f), this.textView.getMeasuredHeight() + this.valueTextView.getMeasuredHeight() + AndroidUtilities.dp(20.0f)) + (this.needDivider ? 1 : 0));
        }

        @Override // android.widget.FrameLayout, android.view.ViewGroup, android.view.View
        protected void onLayout(boolean z, int i, int i2, int i3, int i4) {
            super.onLayout(z, i, i2, i3, i4);
            int measuredHeight = this.textView.getMeasuredHeight() + AndroidUtilities.dp(13.0f);
            TextView textView = this.valueTextView;
            textView.layout(textView.getLeft(), measuredHeight, this.valueTextView.getRight(), this.valueTextView.getMeasuredHeight() + measuredHeight);
        }

        public void setVCardItem(AndroidUtilities.VcardItem vcardItem, int i, boolean z) {
            this.textView.setText(vcardItem.getValue(true));
            this.valueTextView.setText(vcardItem.getType());
            Switch r0 = this.checkBox;
            if (r0 != null) {
                r0.setChecked(vcardItem.checked, false);
            }
            if (i != 0) {
                this.imageView.setImageResource(i);
            } else {
                this.imageView.setImageDrawable(null);
            }
            this.needDivider = z;
            setWillNotDraw(!z);
        }

        public void setChecked(boolean z) {
            Switch r0 = this.checkBox;
            if (r0 != null) {
                r0.setChecked(z, true);
            }
        }

        @Override // android.view.View
        protected void onDraw(Canvas canvas) {
            if (this.needDivider) {
                canvas.drawLine(LocaleController.isRTL ? 0.0f : AndroidUtilities.dp(70.0f), getMeasuredHeight() - 1, getMeasuredWidth() - (LocaleController.isRTL ? AndroidUtilities.dp(70.0f) : 0), getMeasuredHeight() - 1, Theme.dividerPaint);
            }
        }
    }

    public PhonebookShareAlert(BaseFragment baseFragment, ContactsController.Contact contact, TLRPC$User tLRPC$User, Uri uri, File file, String str, String str2) {
        this(baseFragment, contact, tLRPC$User, uri, file, str, str2, null);
    }

    /* JADX WARN: Removed duplicated region for block: B:43:0x0105  */
    /* JADX WARN: Removed duplicated region for block: B:46:0x0126  */
    /* JADX WARN: Removed duplicated region for block: B:49:0x01a7  */
    /* JADX WARN: Removed duplicated region for block: B:61:0x0231  */
    /* JADX WARN: Removed duplicated region for block: B:62:0x023b  */
    /* JADX WARN: Removed duplicated region for block: B:65:0x02e6  */
    /* JADX WARN: Removed duplicated region for block: B:66:0x02f0  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public PhonebookShareAlert(BaseFragment baseFragment, ContactsController.Contact contact, TLRPC$User tLRPC$User, Uri uri, File file, String str, String str2, Theme.ResourcesProvider resourcesProvider) {
        super(baseFragment.getParentActivity(), false, resourcesProvider);
        ArrayList<TLRPC$User> arrayList;
        String str3;
        ArrayList<TLRPC$TL_restrictionReason> arrayList2;
        String str4;
        int itemCount;
        int i;
        boolean z;
        this.backgroundPaint = new Paint(1);
        this.other = new ArrayList<>();
        this.phones = new ArrayList<>();
        String formatName = ContactsController.formatName(str, str2);
        ArrayList arrayList3 = new ArrayList();
        if (uri != null) {
            arrayList = AndroidUtilities.loadVCardFromStream(uri, this.currentAccount, false, arrayList3, formatName);
        } else if (file != null) {
            arrayList = AndroidUtilities.loadVCardFromStream(Uri.fromFile(file), this.currentAccount, false, arrayList3, formatName);
            file.delete();
            this.isImport = true;
        } else {
            String str5 = contact.key;
            if (str5 != null) {
                arrayList = AndroidUtilities.loadVCardFromStream(Uri.withAppendedPath(ContactsContract.Contacts.CONTENT_VCARD_URI, str5), this.currentAccount, true, arrayList3, formatName);
            } else {
                AndroidUtilities.VcardItem vcardItem = new AndroidUtilities.VcardItem();
                vcardItem.type = 0;
                ArrayList<String> arrayList4 = vcardItem.vcardData;
                String str6 = "TEL;MOBILE:+" + contact.user.phone;
                vcardItem.fullData = str6;
                arrayList4.add(str6);
                this.phones.add(vcardItem);
                arrayList = null;
            }
        }
        TLRPC$User tLRPC$User2 = (tLRPC$User != null || contact == null) ? tLRPC$User : contact.user;
        if (arrayList != null) {
            for (int i2 = 0; i2 < arrayList3.size(); i2++) {
                AndroidUtilities.VcardItem vcardItem2 = (AndroidUtilities.VcardItem) arrayList3.get(i2);
                if (vcardItem2.type == 0) {
                    int i3 = 0;
                    while (true) {
                        if (i3 >= this.phones.size()) {
                            z = false;
                            break;
                        } else if (this.phones.get(i3).getValue(false).equals(vcardItem2.getValue(false))) {
                            z = true;
                            break;
                        } else {
                            i3++;
                        }
                    }
                    if (z) {
                        vcardItem2.checked = false;
                    } else {
                        this.phones.add(vcardItem2);
                    }
                } else {
                    this.other.add(vcardItem2);
                }
            }
            if (!arrayList.isEmpty()) {
                TLRPC$User tLRPC$User3 = arrayList.get(0);
                arrayList2 = tLRPC$User3.restriction_reason;
                if (TextUtils.isEmpty(str)) {
                    str3 = tLRPC$User3.first_name;
                    str4 = tLRPC$User3.last_name;
                } else {
                    str3 = str;
                    str4 = str2;
                }
                TLRPC$TL_userContact_old2 tLRPC$TL_userContact_old2 = new TLRPC$TL_userContact_old2();
                this.currentUser = tLRPC$TL_userContact_old2;
                if (tLRPC$User2 == null) {
                    tLRPC$TL_userContact_old2.id = tLRPC$User2.id;
                    tLRPC$TL_userContact_old2.access_hash = tLRPC$User2.access_hash;
                    tLRPC$TL_userContact_old2.photo = tLRPC$User2.photo;
                    tLRPC$TL_userContact_old2.status = tLRPC$User2.status;
                    tLRPC$TL_userContact_old2.first_name = tLRPC$User2.first_name;
                    tLRPC$TL_userContact_old2.last_name = tLRPC$User2.last_name;
                    tLRPC$TL_userContact_old2.phone = tLRPC$User2.phone;
                    if (arrayList2 != null) {
                        tLRPC$TL_userContact_old2.restriction_reason = arrayList2;
                    }
                } else {
                    tLRPC$TL_userContact_old2.first_name = str3;
                    tLRPC$TL_userContact_old2.last_name = str4;
                }
                this.parentFragment = baseFragment;
                Activity parentActivity = baseFragment.getParentActivity();
                updateRows();
                AnonymousClass1 anonymousClass1 = new AnonymousClass1(parentActivity, parentActivity);
                anonymousClass1.setWillNotDraw(false);
                this.containerView = anonymousClass1;
                setApplyTopPadding(false);
                setApplyBottomPadding(false);
                this.listAdapter = new ListAdapter(this, null);
                AnonymousClass2 anonymousClass2 = new AnonymousClass2(parentActivity);
                this.scrollView = anonymousClass2;
                anonymousClass2.setClipToPadding(false);
                this.scrollView.setVerticalScrollBarEnabled(false);
                anonymousClass1.addView(this.scrollView, LayoutHelper.createFrame(-1, -1.0f, 51, 0.0f, 0.0f, 0.0f, 77.0f));
                LinearLayout linearLayout = new LinearLayout(parentActivity);
                this.linearLayout = linearLayout;
                linearLayout.setOrientation(1);
                this.scrollView.addView(this.linearLayout, LayoutHelper.createScroll(-1, -1, 51));
                this.scrollView.setOnScrollChangeListener(new PhonebookShareAlert$$ExternalSyntheticLambda4(this));
                itemCount = this.listAdapter.getItemCount();
                for (i = 0; i < itemCount; i++) {
                    View createView = this.listAdapter.createView(parentActivity, i);
                    this.linearLayout.addView(createView, LayoutHelper.createLinear(-1, -2));
                    if ((i >= this.phoneStartRow && i < this.phoneEndRow) || (i >= this.vcardStartRow && i < this.vcardEndRow)) {
                        createView.setBackgroundDrawable(Theme.getSelectorDrawable(false));
                        createView.setOnClickListener(new PhonebookShareAlert$$ExternalSyntheticLambda1(this, i, createView));
                        createView.setOnLongClickListener(new PhonebookShareAlert$$ExternalSyntheticLambda3(this, i, resourcesProvider, parentActivity));
                    }
                }
                AnonymousClass3 anonymousClass3 = new AnonymousClass3(parentActivity);
                this.actionBar = anonymousClass3;
                anonymousClass3.setBackgroundColor(getThemedColor("dialogBackground"));
                this.actionBar.setBackButtonImage(2131165449);
                this.actionBar.setItemsColor(getThemedColor("dialogTextBlack"), false);
                this.actionBar.setItemsBackgroundColor(getThemedColor("dialogButtonSelector"), false);
                this.actionBar.setTitleColor(getThemedColor("dialogTextBlack"));
                this.actionBar.setOccupyStatusBar(false);
                this.actionBar.setAlpha(0.0f);
                if (!this.isImport) {
                    this.actionBar.setTitle(LocaleController.getString("AddContactPhonebookTitle", 2131624263));
                } else {
                    this.actionBar.setTitle(LocaleController.getString("ShareContactTitle", 2131628273));
                }
                this.containerView.addView(this.actionBar, LayoutHelper.createFrame(-1, -2.0f));
                this.actionBar.setActionBarMenuOnItemClick(new AnonymousClass4());
                View view = new View(parentActivity);
                this.actionBarShadow = view;
                view.setAlpha(0.0f);
                this.actionBarShadow.setBackgroundColor(getThemedColor("dialogShadowLine"));
                this.containerView.addView(this.actionBarShadow, LayoutHelper.createFrame(-1, 1.0f));
                View view2 = new View(parentActivity);
                this.shadow = view2;
                view2.setBackgroundColor(getThemedColor("dialogShadowLine"));
                this.shadow.setAlpha(0.0f);
                this.containerView.addView(this.shadow, LayoutHelper.createFrame(-1, 1.0f, 83, 0.0f, 0.0f, 0.0f, 77.0f));
                TextView textView = new TextView(parentActivity);
                this.buttonTextView = textView;
                textView.setPadding(AndroidUtilities.dp(34.0f), 0, AndroidUtilities.dp(34.0f), 0);
                this.buttonTextView.setGravity(17);
                this.buttonTextView.setTextColor(getThemedColor("featuredStickers_buttonText"));
                this.buttonTextView.setTextSize(1, 14.0f);
                if (!this.isImport) {
                    this.buttonTextView.setText(LocaleController.getString("AddContactPhonebookTitle", 2131624263));
                } else {
                    this.buttonTextView.setText(LocaleController.getString("ShareContactTitle", 2131628273));
                }
                this.buttonTextView.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
                this.buttonTextView.setBackground(Theme.createSimpleSelectorRoundRectDrawable(AndroidUtilities.dp(4.0f), getThemedColor("featuredStickers_addButton"), getThemedColor("featuredStickers_addButtonPressed")));
                anonymousClass1.addView(this.buttonTextView, LayoutHelper.createFrame(-1, 42.0f, 83, 16.0f, 16.0f, 16.0f, 16.0f));
                this.buttonTextView.setOnClickListener(new PhonebookShareAlert$$ExternalSyntheticLambda2(this, resourcesProvider));
            }
        }
        str3 = str;
        str4 = str2;
        arrayList2 = null;
        TLRPC$TL_userContact_old2 tLRPC$TL_userContact_old22 = new TLRPC$TL_userContact_old2();
        this.currentUser = tLRPC$TL_userContact_old22;
        if (tLRPC$User2 == null) {
        }
        this.parentFragment = baseFragment;
        Activity parentActivity2 = baseFragment.getParentActivity();
        updateRows();
        AnonymousClass1 anonymousClass12 = new AnonymousClass1(parentActivity2, parentActivity2);
        anonymousClass12.setWillNotDraw(false);
        this.containerView = anonymousClass12;
        setApplyTopPadding(false);
        setApplyBottomPadding(false);
        this.listAdapter = new ListAdapter(this, null);
        AnonymousClass2 anonymousClass22 = new AnonymousClass2(parentActivity2);
        this.scrollView = anonymousClass22;
        anonymousClass22.setClipToPadding(false);
        this.scrollView.setVerticalScrollBarEnabled(false);
        anonymousClass12.addView(this.scrollView, LayoutHelper.createFrame(-1, -1.0f, 51, 0.0f, 0.0f, 0.0f, 77.0f));
        LinearLayout linearLayout2 = new LinearLayout(parentActivity2);
        this.linearLayout = linearLayout2;
        linearLayout2.setOrientation(1);
        this.scrollView.addView(this.linearLayout, LayoutHelper.createScroll(-1, -1, 51));
        this.scrollView.setOnScrollChangeListener(new PhonebookShareAlert$$ExternalSyntheticLambda4(this));
        itemCount = this.listAdapter.getItemCount();
        while (i < itemCount) {
        }
        AnonymousClass3 anonymousClass32 = new AnonymousClass3(parentActivity2);
        this.actionBar = anonymousClass32;
        anonymousClass32.setBackgroundColor(getThemedColor("dialogBackground"));
        this.actionBar.setBackButtonImage(2131165449);
        this.actionBar.setItemsColor(getThemedColor("dialogTextBlack"), false);
        this.actionBar.setItemsBackgroundColor(getThemedColor("dialogButtonSelector"), false);
        this.actionBar.setTitleColor(getThemedColor("dialogTextBlack"));
        this.actionBar.setOccupyStatusBar(false);
        this.actionBar.setAlpha(0.0f);
        if (!this.isImport) {
        }
        this.containerView.addView(this.actionBar, LayoutHelper.createFrame(-1, -2.0f));
        this.actionBar.setActionBarMenuOnItemClick(new AnonymousClass4());
        View view3 = new View(parentActivity2);
        this.actionBarShadow = view3;
        view3.setAlpha(0.0f);
        this.actionBarShadow.setBackgroundColor(getThemedColor("dialogShadowLine"));
        this.containerView.addView(this.actionBarShadow, LayoutHelper.createFrame(-1, 1.0f));
        View view22 = new View(parentActivity2);
        this.shadow = view22;
        view22.setBackgroundColor(getThemedColor("dialogShadowLine"));
        this.shadow.setAlpha(0.0f);
        this.containerView.addView(this.shadow, LayoutHelper.createFrame(-1, 1.0f, 83, 0.0f, 0.0f, 0.0f, 77.0f));
        TextView textView2 = new TextView(parentActivity2);
        this.buttonTextView = textView2;
        textView2.setPadding(AndroidUtilities.dp(34.0f), 0, AndroidUtilities.dp(34.0f), 0);
        this.buttonTextView.setGravity(17);
        this.buttonTextView.setTextColor(getThemedColor("featuredStickers_buttonText"));
        this.buttonTextView.setTextSize(1, 14.0f);
        if (!this.isImport) {
        }
        this.buttonTextView.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
        this.buttonTextView.setBackground(Theme.createSimpleSelectorRoundRectDrawable(AndroidUtilities.dp(4.0f), getThemedColor("featuredStickers_addButton"), getThemedColor("featuredStickers_addButtonPressed")));
        anonymousClass12.addView(this.buttonTextView, LayoutHelper.createFrame(-1, 42.0f, 83, 16.0f, 16.0f, 16.0f, 16.0f));
        this.buttonTextView.setOnClickListener(new PhonebookShareAlert$$ExternalSyntheticLambda2(this, resourcesProvider));
    }

    /* renamed from: org.telegram.ui.Components.PhonebookShareAlert$1 */
    /* loaded from: classes3.dex */
    public class AnonymousClass1 extends FrameLayout {
        private boolean ignoreLayout;
        private RectF rect = new RectF();
        final /* synthetic */ Context val$context;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass1(Context context, Context context2) {
            super(context);
            PhonebookShareAlert.this = r1;
            this.val$context = context2;
        }

        @Override // android.view.ViewGroup
        public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
            if (motionEvent.getAction() == 0 && PhonebookShareAlert.this.scrollOffsetY != 0 && motionEvent.getY() < PhonebookShareAlert.this.scrollOffsetY && PhonebookShareAlert.this.actionBar.getAlpha() == 0.0f) {
                PhonebookShareAlert.this.dismiss();
                return true;
            }
            return super.onInterceptTouchEvent(motionEvent);
        }

        @Override // android.view.View
        public boolean onTouchEvent(MotionEvent motionEvent) {
            return !PhonebookShareAlert.this.isDismissed() && super.onTouchEvent(motionEvent);
        }

        @Override // android.widget.FrameLayout, android.view.View
        protected void onMeasure(int i, int i2) {
            int size = View.MeasureSpec.getSize(i2);
            if (Build.VERSION.SDK_INT >= 21) {
                this.ignoreLayout = true;
                setPadding(((BottomSheet) PhonebookShareAlert.this).backgroundPaddingLeft, AndroidUtilities.statusBarHeight, ((BottomSheet) PhonebookShareAlert.this).backgroundPaddingLeft, 0);
                this.ignoreLayout = false;
            }
            int paddingTop = size - getPaddingTop();
            View.MeasureSpec.getSize(i);
            int unused = ((BottomSheet) PhonebookShareAlert.this).backgroundPaddingLeft;
            ((FrameLayout.LayoutParams) PhonebookShareAlert.this.actionBarShadow.getLayoutParams()).topMargin = ActionBar.getCurrentActionBarHeight();
            this.ignoreLayout = true;
            int dp = AndroidUtilities.dp(80.0f);
            int itemCount = PhonebookShareAlert.this.listAdapter.getItemCount();
            for (int i3 = 0; i3 < itemCount; i3++) {
                View createView = PhonebookShareAlert.this.listAdapter.createView(this.val$context, i3);
                createView.measure(i, View.MeasureSpec.makeMeasureSpec(0, 0));
                dp += createView.getMeasuredHeight();
            }
            int i4 = dp < paddingTop ? paddingTop - dp : paddingTop / 5;
            if (PhonebookShareAlert.this.scrollView.getPaddingTop() != i4) {
                PhonebookShareAlert.this.scrollView.getPaddingTop();
                PhonebookShareAlert.this.scrollView.setPadding(0, i4, 0, 0);
            }
            this.ignoreLayout = false;
            super.onMeasure(i, View.MeasureSpec.makeMeasureSpec(size, 1073741824));
        }

        @Override // android.widget.FrameLayout, android.view.ViewGroup, android.view.View
        protected void onLayout(boolean z, int i, int i2, int i3, int i4) {
            PhonebookShareAlert.this.inLayout = true;
            super.onLayout(z, i, i2, i3, i4);
            PhonebookShareAlert.this.inLayout = false;
            PhonebookShareAlert.this.updateLayout(false);
        }

        @Override // android.view.View, android.view.ViewParent
        public void requestLayout() {
            if (this.ignoreLayout) {
                return;
            }
            super.requestLayout();
        }

        @Override // android.view.View
        protected void onDraw(Canvas canvas) {
            int i = PhonebookShareAlert.this.scrollOffsetY - ((BottomSheet) PhonebookShareAlert.this).backgroundPaddingTop;
            int measuredHeight = getMeasuredHeight() + AndroidUtilities.dp(30.0f) + ((BottomSheet) PhonebookShareAlert.this).backgroundPaddingTop;
            float dp = AndroidUtilities.dp(12.0f);
            float min = ((float) (((BottomSheet) PhonebookShareAlert.this).backgroundPaddingTop + i)) < dp ? 1.0f - Math.min(1.0f, ((dp - i) - ((BottomSheet) PhonebookShareAlert.this).backgroundPaddingTop) / dp) : 1.0f;
            if (Build.VERSION.SDK_INT >= 21) {
                int i2 = AndroidUtilities.statusBarHeight;
                i += i2;
                measuredHeight -= i2;
            }
            ((BottomSheet) PhonebookShareAlert.this).shadowDrawable.setBounds(0, i, getMeasuredWidth(), measuredHeight);
            ((BottomSheet) PhonebookShareAlert.this).shadowDrawable.draw(canvas);
            if (min != 1.0f) {
                PhonebookShareAlert.this.backgroundPaint.setColor(PhonebookShareAlert.this.getThemedColor("dialogBackground"));
                this.rect.set(((BottomSheet) PhonebookShareAlert.this).backgroundPaddingLeft, ((BottomSheet) PhonebookShareAlert.this).backgroundPaddingTop + i, getMeasuredWidth() - ((BottomSheet) PhonebookShareAlert.this).backgroundPaddingLeft, ((BottomSheet) PhonebookShareAlert.this).backgroundPaddingTop + i + AndroidUtilities.dp(24.0f));
                float f = dp * min;
                canvas.drawRoundRect(this.rect, f, f, PhonebookShareAlert.this.backgroundPaint);
            }
            int themedColor = PhonebookShareAlert.this.getThemedColor("dialogBackground");
            PhonebookShareAlert.this.backgroundPaint.setColor(Color.argb((int) (PhonebookShareAlert.this.actionBar.getAlpha() * 255.0f), (int) (Color.red(themedColor) * 0.8f), (int) (Color.green(themedColor) * 0.8f), (int) (Color.blue(themedColor) * 0.8f)));
            canvas.drawRect(((BottomSheet) PhonebookShareAlert.this).backgroundPaddingLeft, 0.0f, getMeasuredWidth() - ((BottomSheet) PhonebookShareAlert.this).backgroundPaddingLeft, AndroidUtilities.statusBarHeight, PhonebookShareAlert.this.backgroundPaint);
        }
    }

    /* renamed from: org.telegram.ui.Components.PhonebookShareAlert$2 */
    /* loaded from: classes3.dex */
    public class AnonymousClass2 extends NestedScrollView {
        private View focusingView;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass2(Context context) {
            super(context);
            PhonebookShareAlert.this = r1;
        }

        @Override // androidx.core.widget.NestedScrollView, android.view.ViewGroup, android.view.ViewParent
        public void requestChildFocus(View view, View view2) {
            this.focusingView = view2;
            super.requestChildFocus(view, view2);
        }

        @Override // androidx.core.widget.NestedScrollView
        public int computeScrollDeltaToGetChildRectOnScreen(android.graphics.Rect rect) {
            if (this.focusingView == null || PhonebookShareAlert.this.linearLayout.getTop() != getPaddingTop()) {
                return 0;
            }
            int computeScrollDeltaToGetChildRectOnScreen = super.computeScrollDeltaToGetChildRectOnScreen(rect);
            int currentActionBarHeight = ActionBar.getCurrentActionBarHeight() - (((this.focusingView.getTop() - getScrollY()) + rect.top) + computeScrollDeltaToGetChildRectOnScreen);
            return currentActionBarHeight > 0 ? computeScrollDeltaToGetChildRectOnScreen - (currentActionBarHeight + AndroidUtilities.dp(10.0f)) : computeScrollDeltaToGetChildRectOnScreen;
        }
    }

    public /* synthetic */ void lambda$new$0(NestedScrollView nestedScrollView, int i, int i2, int i3, int i4) {
        updateLayout(!this.inLayout);
    }

    public /* synthetic */ void lambda$new$2(int i, View view, View view2) {
        AndroidUtilities.VcardItem vcardItem;
        int i2 = this.phoneStartRow;
        if (i >= i2 && i < this.phoneEndRow) {
            vcardItem = this.phones.get(i - i2);
        } else {
            int i3 = this.vcardStartRow;
            vcardItem = (i < i3 || i >= this.vcardEndRow) ? null : this.other.get(i - i3);
        }
        if (vcardItem == null) {
            return;
        }
        boolean z = true;
        if (this.isImport) {
            int i4 = vcardItem.type;
            if (i4 == 0) {
                try {
                    Intent intent = new Intent("android.intent.action.DIAL", Uri.parse("tel:" + vcardItem.getValue(false)));
                    intent.addFlags(268435456);
                    this.parentFragment.getParentActivity().startActivityForResult(intent, 500);
                    return;
                } catch (Exception e) {
                    FileLog.e(e);
                    return;
                }
            } else if (i4 == 1) {
                Browser.openUrl(this.parentFragment.getParentActivity(), "mailto:" + vcardItem.getValue(false));
                return;
            } else if (i4 == 3) {
                String value = vcardItem.getValue(false);
                if (!value.startsWith("http")) {
                    value = "http://" + value;
                }
                Browser.openUrl(this.parentFragment.getParentActivity(), value);
                return;
            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(this.parentFragment.getParentActivity());
                builder.setItems(new CharSequence[]{LocaleController.getString("Copy", 2131625256)}, new PhonebookShareAlert$$ExternalSyntheticLambda0(this, vcardItem));
                builder.show();
                return;
            }
        }
        vcardItem.checked = !vcardItem.checked;
        if (i >= this.phoneStartRow && i < this.phoneEndRow) {
            int i5 = 0;
            while (true) {
                if (i5 >= this.phones.size()) {
                    z = false;
                    break;
                } else if (this.phones.get(i5).checked) {
                    break;
                } else {
                    i5++;
                }
            }
            int themedColor = getThemedColor("featuredStickers_buttonText");
            this.buttonTextView.setEnabled(z);
            TextView textView = this.buttonTextView;
            if (!z) {
                themedColor &= Integer.MAX_VALUE;
            }
            textView.setTextColor(themedColor);
        }
        ((TextCheckBoxCell) view).setChecked(vcardItem.checked);
    }

    public /* synthetic */ void lambda$new$1(AndroidUtilities.VcardItem vcardItem, DialogInterface dialogInterface, int i) {
        if (i == 0) {
            try {
                ((ClipboardManager) ApplicationLoader.applicationContext.getSystemService("clipboard")).setPrimaryClip(ClipData.newPlainText("label", vcardItem.getValue(false)));
                if (Build.VERSION.SDK_INT >= 31) {
                    return;
                }
                Toast.makeText(this.parentFragment.getParentActivity(), LocaleController.getString("TextCopied", 2131628595), 0).show();
            } catch (Exception e) {
                FileLog.e(e);
            }
        }
    }

    public /* synthetic */ boolean lambda$new$3(int i, Theme.ResourcesProvider resourcesProvider, Context context, View view) {
        AndroidUtilities.VcardItem vcardItem;
        int i2 = this.phoneStartRow;
        if (i >= i2 && i < this.phoneEndRow) {
            vcardItem = this.phones.get(i - i2);
        } else {
            int i3 = this.vcardStartRow;
            vcardItem = (i < i3 || i >= this.vcardEndRow) ? null : this.other.get(i - i3);
        }
        if (vcardItem == null) {
            return false;
        }
        ((ClipboardManager) ApplicationLoader.applicationContext.getSystemService("clipboard")).setPrimaryClip(ClipData.newPlainText("label", vcardItem.getValue(false)));
        if (BulletinFactory.canShowBulletin(this.parentFragment)) {
            if (vcardItem.type == 3) {
                BulletinFactory.of((FrameLayout) this.containerView, resourcesProvider).createCopyLinkBulletin().show();
            } else {
                Bulletin.SimpleLayout simpleLayout = new Bulletin.SimpleLayout(context, resourcesProvider);
                int i4 = vcardItem.type;
                if (i4 == 0) {
                    simpleLayout.textView.setText(LocaleController.getString("PhoneCopied", 2131627488));
                    simpleLayout.imageView.setImageResource(2131165664);
                } else if (i4 == 1) {
                    simpleLayout.textView.setText(LocaleController.getString("EmailCopied", 2131625597));
                    simpleLayout.imageView.setImageResource(2131165799);
                } else {
                    simpleLayout.textView.setText(LocaleController.getString("TextCopied", 2131628595));
                    simpleLayout.imageView.setImageResource(2131165763);
                }
                if (Build.VERSION.SDK_INT < 31) {
                    Bulletin.make((FrameLayout) this.containerView, simpleLayout, 1500).show();
                }
            }
        }
        return true;
    }

    /* renamed from: org.telegram.ui.Components.PhonebookShareAlert$3 */
    /* loaded from: classes3.dex */
    public class AnonymousClass3 extends ActionBar {
        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass3(Context context) {
            super(context);
            PhonebookShareAlert.this = r1;
        }

        @Override // android.view.View
        public void setAlpha(float f) {
            super.setAlpha(f);
            ((BottomSheet) PhonebookShareAlert.this).containerView.invalidate();
        }
    }

    /* renamed from: org.telegram.ui.Components.PhonebookShareAlert$4 */
    /* loaded from: classes3.dex */
    public class AnonymousClass4 extends ActionBar.ActionBarMenuOnItemClick {
        AnonymousClass4() {
            PhonebookShareAlert.this = r1;
        }

        @Override // org.telegram.ui.ActionBar.ActionBar.ActionBarMenuOnItemClick
        public void onItemClick(int i) {
            if (i == -1) {
                PhonebookShareAlert.this.dismiss();
            }
        }
    }

    public /* synthetic */ void lambda$new$5(Theme.ResourcesProvider resourcesProvider, View view) {
        StringBuilder sb;
        if (this.isImport) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setTitle(LocaleController.getString("AddContactTitle", 2131624264));
            builder.setNegativeButton(LocaleController.getString("Cancel", 2131624819), null);
            builder.setItems(new CharSequence[]{LocaleController.getString("CreateNewContact", 2131625274), LocaleController.getString("AddToExistingContact", 2131624291)}, new AnonymousClass5());
            builder.show();
            return;
        }
        if (!this.currentUser.restriction_reason.isEmpty()) {
            sb = new StringBuilder(this.currentUser.restriction_reason.get(0).text);
        } else {
            Locale locale = Locale.US;
            TLRPC$User tLRPC$User = this.currentUser;
            sb = new StringBuilder(String.format(locale, "BEGIN:VCARD\nVERSION:3.0\nFN:%1$s\nEND:VCARD", ContactsController.formatName(tLRPC$User.first_name, tLRPC$User.last_name)));
        }
        int lastIndexOf = sb.lastIndexOf("END:VCARD");
        if (lastIndexOf >= 0) {
            this.currentUser.phone = null;
            for (int size = this.phones.size() - 1; size >= 0; size--) {
                AndroidUtilities.VcardItem vcardItem = this.phones.get(size);
                if (vcardItem.checked) {
                    TLRPC$User tLRPC$User2 = this.currentUser;
                    if (tLRPC$User2.phone == null) {
                        tLRPC$User2.phone = vcardItem.getValue(false);
                    }
                    for (int i = 0; i < vcardItem.vcardData.size(); i++) {
                        sb.insert(lastIndexOf, vcardItem.vcardData.get(i) + "\n");
                    }
                }
            }
            for (int size2 = this.other.size() - 1; size2 >= 0; size2--) {
                AndroidUtilities.VcardItem vcardItem2 = this.other.get(size2);
                if (vcardItem2.checked) {
                    for (int size3 = vcardItem2.vcardData.size() - 1; size3 >= 0; size3 += -1) {
                        sb.insert(lastIndexOf, vcardItem2.vcardData.get(size3) + "\n");
                    }
                }
            }
            this.currentUser.restriction_reason.clear();
            TLRPC$TL_restrictionReason tLRPC$TL_restrictionReason = new TLRPC$TL_restrictionReason();
            tLRPC$TL_restrictionReason.text = sb.toString();
            tLRPC$TL_restrictionReason.reason = "";
            tLRPC$TL_restrictionReason.platform = "";
            this.currentUser.restriction_reason.add(tLRPC$TL_restrictionReason);
        }
        BaseFragment baseFragment = this.parentFragment;
        if ((baseFragment instanceof ChatActivity) && ((ChatActivity) baseFragment).isInScheduleMode()) {
            AlertsCreator.createScheduleDatePickerDialog(getContext(), ((ChatActivity) this.parentFragment).getDialogId(), new PhonebookShareAlert$$ExternalSyntheticLambda5(this), resourcesProvider);
            return;
        }
        this.delegate.didSelectContact(this.currentUser, true, 0);
        dismiss();
    }

    /* renamed from: org.telegram.ui.Components.PhonebookShareAlert$5 */
    /* loaded from: classes3.dex */
    public class AnonymousClass5 implements DialogInterface.OnClickListener {
        AnonymousClass5() {
            PhonebookShareAlert.this = r1;
        }

        private void fillRowWithType(String str, ContentValues contentValues) {
            if (str.startsWith("X-")) {
                contentValues.put("data2", (Integer) 0);
                contentValues.put("data3", str.substring(2));
            } else if ("PREF".equalsIgnoreCase(str)) {
                contentValues.put("data2", (Integer) 12);
            } else if ("HOME".equalsIgnoreCase(str)) {
                contentValues.put("data2", (Integer) 1);
            } else if ("MOBILE".equalsIgnoreCase(str) || "CELL".equalsIgnoreCase(str)) {
                contentValues.put("data2", (Integer) 2);
            } else if ("OTHER".equalsIgnoreCase(str)) {
                contentValues.put("data2", (Integer) 7);
            } else if ("WORK".equalsIgnoreCase(str)) {
                contentValues.put("data2", (Integer) 3);
            } else if ("RADIO".equalsIgnoreCase(str) || "VOICE".equalsIgnoreCase(str)) {
                contentValues.put("data2", (Integer) 14);
            } else if ("PAGER".equalsIgnoreCase(str)) {
                contentValues.put("data2", (Integer) 6);
            } else if ("CALLBACK".equalsIgnoreCase(str)) {
                contentValues.put("data2", (Integer) 8);
            } else if ("CAR".equalsIgnoreCase(str)) {
                contentValues.put("data2", (Integer) 9);
            } else if ("ASSISTANT".equalsIgnoreCase(str)) {
                contentValues.put("data2", (Integer) 19);
            } else if ("MMS".equalsIgnoreCase(str)) {
                contentValues.put("data2", (Integer) 20);
            } else if (str.startsWith("FAX")) {
                contentValues.put("data2", (Integer) 4);
            } else {
                contentValues.put("data2", (Integer) 0);
                contentValues.put("data3", str);
            }
        }

        private void fillUrlRowWithType(String str, ContentValues contentValues) {
            if (str.startsWith("X-")) {
                contentValues.put("data2", (Integer) 0);
                contentValues.put("data3", str.substring(2));
            } else if ("HOMEPAGE".equalsIgnoreCase(str)) {
                contentValues.put("data2", (Integer) 1);
            } else if ("BLOG".equalsIgnoreCase(str)) {
                contentValues.put("data2", (Integer) 2);
            } else if ("PROFILE".equalsIgnoreCase(str)) {
                contentValues.put("data2", (Integer) 3);
            } else if ("HOME".equalsIgnoreCase(str)) {
                contentValues.put("data2", (Integer) 4);
            } else if ("WORK".equalsIgnoreCase(str)) {
                contentValues.put("data2", (Integer) 5);
            } else if ("FTP".equalsIgnoreCase(str)) {
                contentValues.put("data2", (Integer) 6);
            } else if ("OTHER".equalsIgnoreCase(str)) {
                contentValues.put("data2", (Integer) 7);
            } else {
                contentValues.put("data2", (Integer) 0);
                contentValues.put("data3", str);
            }
        }

        @Override // android.content.DialogInterface.OnClickListener
        public void onClick(DialogInterface dialogInterface, int i) {
            Intent intent;
            int i2;
            Intent intent2;
            boolean z;
            AnonymousClass5 anonymousClass5 = this;
            int i3 = 1;
            if (i == 0) {
                intent = new Intent("android.intent.action.INSERT");
                intent.setType("vnd.android.cursor.dir/raw_contact");
            } else if (i == 1) {
                intent = new Intent("android.intent.action.INSERT_OR_EDIT");
                intent.setType("vnd.android.cursor.item/contact");
            } else {
                intent = null;
            }
            intent.putExtra("name", ContactsController.formatName(PhonebookShareAlert.this.currentUser.first_name, PhonebookShareAlert.this.currentUser.last_name));
            ArrayList<? extends Parcelable> arrayList = new ArrayList<>();
            boolean z2 = false;
            for (int i4 = 0; i4 < PhonebookShareAlert.this.phones.size(); i4++) {
                AndroidUtilities.VcardItem vcardItem = (AndroidUtilities.VcardItem) PhonebookShareAlert.this.phones.get(i4);
                ContentValues contentValues = new ContentValues();
                contentValues.put("mimetype", "vnd.android.cursor.item/phone_v2");
                contentValues.put("data1", vcardItem.getValue(false));
                anonymousClass5.fillRowWithType(vcardItem.getRawType(false), contentValues);
                arrayList.add(contentValues);
            }
            int i5 = 0;
            boolean z3 = false;
            while (i5 < PhonebookShareAlert.this.other.size()) {
                AndroidUtilities.VcardItem vcardItem2 = (AndroidUtilities.VcardItem) PhonebookShareAlert.this.other.get(i5);
                int i6 = vcardItem2.type;
                if (i6 == i3) {
                    ContentValues contentValues2 = new ContentValues();
                    contentValues2.put("mimetype", "vnd.android.cursor.item/email_v2");
                    contentValues2.put("data1", vcardItem2.getValue(z2));
                    anonymousClass5.fillRowWithType(vcardItem2.getRawType(z2), contentValues2);
                    arrayList.add(contentValues2);
                } else if (i6 == 3) {
                    ContentValues contentValues3 = new ContentValues();
                    contentValues3.put("mimetype", "vnd.android.cursor.item/website");
                    contentValues3.put("data1", vcardItem2.getValue(z2));
                    anonymousClass5.fillUrlRowWithType(vcardItem2.getRawType(z2), contentValues3);
                    arrayList.add(contentValues3);
                } else if (i6 == 4) {
                    ContentValues contentValues4 = new ContentValues();
                    contentValues4.put("mimetype", "vnd.android.cursor.item/note");
                    contentValues4.put("data1", vcardItem2.getValue(z2));
                    arrayList.add(contentValues4);
                } else if (i6 == 5) {
                    ContentValues contentValues5 = new ContentValues();
                    contentValues5.put("mimetype", "vnd.android.cursor.item/contact_event");
                    contentValues5.put("data1", vcardItem2.getValue(z2));
                    contentValues5.put("data2", (Integer) 3);
                    arrayList.add(contentValues5);
                } else {
                    intent2 = intent;
                    i2 = i5;
                    if (i6 == 2) {
                        ContentValues contentValues6 = new ContentValues();
                        contentValues6.put("mimetype", "vnd.android.cursor.item/postal-address_v2");
                        String[] rawValue = vcardItem2.getRawValue();
                        z = z3;
                        if (rawValue.length > 0) {
                            contentValues6.put("data5", rawValue[0]);
                        }
                        if (rawValue.length > 1) {
                            contentValues6.put("data6", rawValue[1]);
                        }
                        if (rawValue.length > 2) {
                            contentValues6.put("data4", rawValue[2]);
                        }
                        if (rawValue.length > 3) {
                            contentValues6.put("data7", rawValue[3]);
                        }
                        if (rawValue.length > 4) {
                            contentValues6.put("data8", rawValue[4]);
                        }
                        if (rawValue.length > 5) {
                            contentValues6.put("data9", rawValue[5]);
                        }
                        if (rawValue.length > 6) {
                            contentValues6.put("data10", rawValue[6]);
                        }
                        String rawType = vcardItem2.getRawType(false);
                        if ("HOME".equalsIgnoreCase(rawType)) {
                            contentValues6.put("data2", (Integer) 1);
                        } else if ("WORK".equalsIgnoreCase(rawType)) {
                            contentValues6.put("data2", (Integer) 2);
                        } else if ("OTHER".equalsIgnoreCase(rawType)) {
                            contentValues6.put("data2", (Integer) 3);
                        }
                        arrayList.add(contentValues6);
                    } else {
                        z = z3;
                        if (i6 == 20) {
                            ContentValues contentValues7 = new ContentValues();
                            contentValues7.put("mimetype", "vnd.android.cursor.item/im");
                            String rawType2 = vcardItem2.getRawType(true);
                            String rawType3 = vcardItem2.getRawType(false);
                            contentValues7.put("data1", vcardItem2.getValue(false));
                            if ("AIM".equalsIgnoreCase(rawType2)) {
                                contentValues7.put("data5", (Integer) 0);
                            } else if ("MSN".equalsIgnoreCase(rawType2)) {
                                contentValues7.put("data5", (Integer) 1);
                            } else if ("YAHOO".equalsIgnoreCase(rawType2)) {
                                contentValues7.put("data5", (Integer) 2);
                            } else if ("SKYPE".equalsIgnoreCase(rawType2)) {
                                contentValues7.put("data5", (Integer) 3);
                            } else if ("QQ".equalsIgnoreCase(rawType2)) {
                                contentValues7.put("data5", (Integer) 4);
                            } else if ("GOOGLE-TALK".equalsIgnoreCase(rawType2)) {
                                contentValues7.put("data5", (Integer) 5);
                            } else if ("ICQ".equalsIgnoreCase(rawType2)) {
                                contentValues7.put("data5", (Integer) 6);
                            } else if ("JABBER".equalsIgnoreCase(rawType2)) {
                                contentValues7.put("data5", (Integer) 7);
                            } else if ("NETMEETING".equalsIgnoreCase(rawType2)) {
                                contentValues7.put("data5", (Integer) 8);
                            } else {
                                contentValues7.put("data5", (Integer) (-1));
                                contentValues7.put("data6", vcardItem2.getRawType(true));
                            }
                            if ("HOME".equalsIgnoreCase(rawType3)) {
                                contentValues7.put("data2", (Integer) 1);
                            } else if ("WORK".equalsIgnoreCase(rawType3)) {
                                contentValues7.put("data2", (Integer) 2);
                            } else if ("OTHER".equalsIgnoreCase(rawType3)) {
                                contentValues7.put("data2", (Integer) 3);
                            }
                            arrayList.add(contentValues7);
                        } else if (i6 == 6 && !z) {
                            ContentValues contentValues8 = new ContentValues();
                            contentValues8.put("mimetype", "vnd.android.cursor.item/organization");
                            anonymousClass5 = this;
                            for (int i7 = i2; i7 < PhonebookShareAlert.this.other.size(); i7++) {
                                AndroidUtilities.VcardItem vcardItem3 = (AndroidUtilities.VcardItem) PhonebookShareAlert.this.other.get(i7);
                                if (vcardItem3.type == 6) {
                                    String rawType4 = vcardItem3.getRawType(true);
                                    if ("ORG".equalsIgnoreCase(rawType4)) {
                                        String[] rawValue2 = vcardItem3.getRawValue();
                                        if (rawValue2.length != 0) {
                                            if (rawValue2.length >= 1) {
                                                contentValues8.put("data1", rawValue2[0]);
                                            }
                                            if (rawValue2.length >= 2) {
                                                contentValues8.put("data5", rawValue2[1]);
                                            }
                                        }
                                    } else if ("TITLE".equalsIgnoreCase(rawType4)) {
                                        contentValues8.put("data4", vcardItem3.getValue(false));
                                    } else if ("ROLE".equalsIgnoreCase(rawType4)) {
                                        contentValues8.put("data4", vcardItem3.getValue(false));
                                    }
                                    String rawType5 = vcardItem3.getRawType(true);
                                    if ("WORK".equalsIgnoreCase(rawType5)) {
                                        contentValues8.put("data2", (Integer) 1);
                                    } else if ("OTHER".equalsIgnoreCase(rawType5)) {
                                        contentValues8.put("data2", (Integer) 2);
                                    }
                                }
                            }
                            arrayList.add(contentValues8);
                            z3 = true;
                            i5 = i2 + 1;
                            intent = intent2;
                            i3 = 1;
                            z2 = false;
                        }
                    }
                    anonymousClass5 = this;
                    z3 = z;
                    i5 = i2 + 1;
                    intent = intent2;
                    i3 = 1;
                    z2 = false;
                }
                intent2 = intent;
                i2 = i5;
                z = z3;
                z3 = z;
                i5 = i2 + 1;
                intent = intent2;
                i3 = 1;
                z2 = false;
            }
            Intent intent3 = intent;
            intent3.putExtra("finishActivityOnSaveCompleted", true);
            intent3.putParcelableArrayListExtra("data", arrayList);
            try {
                PhonebookShareAlert.this.parentFragment.getParentActivity().startActivity(intent3);
                PhonebookShareAlert.this.dismiss();
            } catch (Exception e) {
                FileLog.e(e);
            }
        }
    }

    public /* synthetic */ void lambda$new$4(boolean z, int i) {
        this.delegate.didSelectContact(this.currentUser, z, i);
        dismiss();
    }

    /* renamed from: org.telegram.ui.Components.PhonebookShareAlert$6 */
    /* loaded from: classes3.dex */
    class AnonymousClass6 implements Bulletin.Delegate {
        @Override // org.telegram.ui.Components.Bulletin.Delegate
        public /* synthetic */ void onHide(Bulletin bulletin) {
            Bulletin.Delegate.CC.$default$onHide(this, bulletin);
        }

        @Override // org.telegram.ui.Components.Bulletin.Delegate
        public /* synthetic */ void onOffsetChange(float f) {
            Bulletin.Delegate.CC.$default$onOffsetChange(this, f);
        }

        @Override // org.telegram.ui.Components.Bulletin.Delegate
        public /* synthetic */ void onShow(Bulletin bulletin) {
            Bulletin.Delegate.CC.$default$onShow(this, bulletin);
        }

        AnonymousClass6(PhonebookShareAlert phonebookShareAlert) {
        }

        @Override // org.telegram.ui.Components.Bulletin.Delegate
        public int getBottomOffset(int i) {
            return AndroidUtilities.dp(74.0f);
        }
    }

    @Override // org.telegram.ui.ActionBar.BottomSheet, android.app.Dialog
    public void onStart() {
        super.onStart();
        Bulletin.addDelegate((FrameLayout) this.containerView, new AnonymousClass6(this));
    }

    @Override // android.app.Dialog
    protected void onStop() {
        super.onStop();
        Bulletin.removeDelegate((FrameLayout) this.containerView);
    }

    public void setDelegate(ChatAttachAlertContactsLayout.PhonebookShareAlertDelegate phonebookShareAlertDelegate) {
        this.delegate = phonebookShareAlertDelegate;
    }

    public void updateLayout(boolean z) {
        View childAt = this.scrollView.getChildAt(0);
        int top = childAt.getTop() - this.scrollView.getScrollY();
        if (top < 0) {
            top = 0;
        }
        boolean z2 = top <= 0;
        float f = 1.0f;
        if ((z2 && this.actionBar.getTag() == null) || (!z2 && this.actionBar.getTag() != null)) {
            this.actionBar.setTag(z2 ? 1 : null);
            AnimatorSet animatorSet = this.actionBarAnimation;
            if (animatorSet != null) {
                animatorSet.cancel();
                this.actionBarAnimation = null;
            }
            if (z) {
                AnimatorSet animatorSet2 = new AnimatorSet();
                this.actionBarAnimation = animatorSet2;
                animatorSet2.setDuration(180L);
                AnimatorSet animatorSet3 = this.actionBarAnimation;
                Animator[] animatorArr = new Animator[2];
                ActionBar actionBar = this.actionBar;
                Property property = View.ALPHA;
                float[] fArr = new float[1];
                fArr[0] = z2 ? 1.0f : 0.0f;
                animatorArr[0] = ObjectAnimator.ofFloat(actionBar, property, fArr);
                View view = this.actionBarShadow;
                Property property2 = View.ALPHA;
                float[] fArr2 = new float[1];
                fArr2[0] = z2 ? 1.0f : 0.0f;
                animatorArr[1] = ObjectAnimator.ofFloat(view, property2, fArr2);
                animatorSet3.playTogether(animatorArr);
                this.actionBarAnimation.addListener(new AnonymousClass7());
                this.actionBarAnimation.start();
            } else {
                this.actionBar.setAlpha(z2 ? 1.0f : 0.0f);
                this.actionBarShadow.setAlpha(z2 ? 1.0f : 0.0f);
            }
        }
        if (this.scrollOffsetY != top) {
            this.scrollOffsetY = top;
            this.containerView.invalidate();
        }
        childAt.getBottom();
        this.scrollView.getMeasuredHeight();
        boolean z3 = childAt.getBottom() - this.scrollView.getScrollY() > this.scrollView.getMeasuredHeight();
        if ((!z3 || this.shadow.getTag() != null) && (z3 || this.shadow.getTag() == null)) {
            return;
        }
        this.shadow.setTag(z3 ? 1 : null);
        AnimatorSet animatorSet4 = this.shadowAnimation;
        if (animatorSet4 != null) {
            animatorSet4.cancel();
            this.shadowAnimation = null;
        }
        if (z) {
            AnimatorSet animatorSet5 = new AnimatorSet();
            this.shadowAnimation = animatorSet5;
            animatorSet5.setDuration(180L);
            AnimatorSet animatorSet6 = this.shadowAnimation;
            Animator[] animatorArr2 = new Animator[1];
            View view2 = this.shadow;
            Property property3 = View.ALPHA;
            float[] fArr3 = new float[1];
            if (!z3) {
                f = 0.0f;
            }
            fArr3[0] = f;
            animatorArr2[0] = ObjectAnimator.ofFloat(view2, property3, fArr3);
            animatorSet6.playTogether(animatorArr2);
            this.shadowAnimation.addListener(new AnonymousClass8());
            this.shadowAnimation.start();
            return;
        }
        View view3 = this.shadow;
        if (!z3) {
            f = 0.0f;
        }
        view3.setAlpha(f);
    }

    /* renamed from: org.telegram.ui.Components.PhonebookShareAlert$7 */
    /* loaded from: classes3.dex */
    public class AnonymousClass7 extends AnimatorListenerAdapter {
        AnonymousClass7() {
            PhonebookShareAlert.this = r1;
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator) {
            PhonebookShareAlert.this.actionBarAnimation = null;
        }
    }

    /* renamed from: org.telegram.ui.Components.PhonebookShareAlert$8 */
    /* loaded from: classes3.dex */
    public class AnonymousClass8 extends AnimatorListenerAdapter {
        AnonymousClass8() {
            PhonebookShareAlert.this = r1;
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator) {
            PhonebookShareAlert.this.shadowAnimation = null;
        }
    }

    private void updateRows() {
        this.rowCount = 0;
        this.rowCount = 0 + 1;
        this.userRow = 0;
        if (this.phones.size() <= 1 && this.other.isEmpty()) {
            this.phoneStartRow = -1;
            this.phoneEndRow = -1;
            this.vcardStartRow = -1;
            this.vcardEndRow = -1;
            return;
        }
        if (this.phones.isEmpty()) {
            this.phoneStartRow = -1;
            this.phoneEndRow = -1;
        } else {
            int i = this.rowCount;
            this.phoneStartRow = i;
            int size = i + this.phones.size();
            this.rowCount = size;
            this.phoneEndRow = size;
        }
        if (this.other.isEmpty()) {
            this.vcardStartRow = -1;
            this.vcardEndRow = -1;
            return;
        }
        int i2 = this.rowCount;
        this.vcardStartRow = i2;
        int size2 = i2 + this.other.size();
        this.rowCount = size2;
        this.vcardEndRow = size2;
    }

    /* loaded from: classes3.dex */
    public class ListAdapter {
        private ListAdapter() {
            PhonebookShareAlert.this = r1;
        }

        /* synthetic */ ListAdapter(PhonebookShareAlert phonebookShareAlert, AnonymousClass1 anonymousClass1) {
            this();
        }

        public int getItemCount() {
            return PhonebookShareAlert.this.rowCount;
        }

        public void onBindViewHolder(View view, int i, int i2) {
            AndroidUtilities.VcardItem vcardItem;
            boolean z = true;
            if (i2 == 1) {
                TextCheckBoxCell textCheckBoxCell = (TextCheckBoxCell) view;
                int i3 = 2131165763;
                if (i < PhonebookShareAlert.this.phoneStartRow || i >= PhonebookShareAlert.this.phoneEndRow) {
                    vcardItem = (AndroidUtilities.VcardItem) PhonebookShareAlert.this.other.get(i - PhonebookShareAlert.this.vcardStartRow);
                    int i4 = vcardItem.type;
                    if (i4 == 1) {
                        i3 = 2131165799;
                    } else if (i4 == 2) {
                        i3 = 2131165788;
                    } else if (i4 == 3) {
                        i3 = 2131165782;
                    } else if (i4 != 4) {
                        if (i4 == 5) {
                            i3 = 2131165662;
                        } else if (i4 == 6) {
                            i3 = "ORG".equalsIgnoreCase(vcardItem.getRawType(true)) ? 2131165989 : 2131165772;
                        }
                    }
                } else {
                    vcardItem = (AndroidUtilities.VcardItem) PhonebookShareAlert.this.phones.get(i - PhonebookShareAlert.this.phoneStartRow);
                    i3 = 2131165664;
                }
                if (i == getItemCount() - 1) {
                    z = false;
                }
                textCheckBoxCell.setVCardItem(vcardItem, i3, z);
            }
        }

        public View createView(Context context, int i) {
            View view;
            int itemViewType = getItemViewType(i);
            if (itemViewType == 0) {
                view = new UserCell(PhonebookShareAlert.this, context);
            } else {
                view = new TextCheckBoxCell(PhonebookShareAlert.this, context);
            }
            onBindViewHolder(view, i, itemViewType);
            return view;
        }

        public int getItemViewType(int i) {
            return i == PhonebookShareAlert.this.userRow ? 0 : 1;
        }
    }
}