package org.telegram.ui.Components;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.Property;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import androidx.recyclerview.widget.LinearSmoothScroller;
import androidx.recyclerview.widget.RecyclerView;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Objects;
import org.telegram.PhoneFormat.PhoneFormat;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.ContactsController;
import org.telegram.messenger.DispatchQueue;
import org.telegram.messenger.Emoji;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.R;
import org.telegram.messenger.UserConfig;
import org.telegram.messenger.UserObject;
import org.telegram.messenger.Utilities;
import org.telegram.messenger.support.LongSparseIntArray;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.tgnet.TLRPC$FileLocation;
import org.telegram.tgnet.TLRPC$RestrictionReason;
import org.telegram.tgnet.TLRPC$TL_contact;
import org.telegram.tgnet.TLRPC$TL_userContact_old2;
import org.telegram.tgnet.TLRPC$User;
import org.telegram.tgnet.TLRPC$UserProfilePhoto;
import org.telegram.tgnet.TLRPC$UserStatus;
import org.telegram.ui.ActionBar.AlertDialog;
import org.telegram.ui.ActionBar.SimpleTextView;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.ActionBar.ThemeDescription;
import org.telegram.ui.Components.ChatAttachAlert;
import org.telegram.ui.Components.ChatAttachAlertContactsLayout;
import org.telegram.ui.Components.RecyclerListView;
/* loaded from: classes3.dex */
public class ChatAttachAlertContactsLayout extends ChatAttachAlert.AttachAlertLayout implements NotificationCenter.NotificationCenterDelegate {
    private PhonebookShareAlertDelegate delegate;
    private EmptyTextProgressView emptyView;
    private FrameLayout frameLayout;
    private boolean ignoreLayout;
    private FillLastLinearLayoutManager layoutManager;
    private ShareAdapter listAdapter;
    private RecyclerListView listView;
    private boolean multipleSelectionAllowed;
    private ShareSearchAdapter searchAdapter;
    private SearchField searchField;
    private HashMap<ListItemID, Object> selectedContacts;
    private ArrayList<ListItemID> selectedContactsOrder;
    private boolean sendPressed;
    private View shadow;
    private AnimatorSet shadowAnimation;

    /* loaded from: classes3.dex */
    public interface PhonebookShareAlertDelegate {

        /* loaded from: classes3.dex */
        public final /* synthetic */ class -CC {
            public static void $default$didSelectContacts(PhonebookShareAlertDelegate phonebookShareAlertDelegate, ArrayList arrayList, String str, boolean z, int i, long j, boolean z2) {
            }
        }

        void didSelectContact(TLRPC$User tLRPC$User, boolean z, int i, long j, boolean z2);

        void didSelectContacts(ArrayList<TLRPC$User> arrayList, String str, boolean z, int i, long j, boolean z2);
    }

    /* loaded from: classes3.dex */
    public static class UserCell extends FrameLayout {
        private AvatarDrawable avatarDrawable;
        private BackupImageView avatarImageView;
        private CheckBox2 checkBox;
        private int currentAccount;
        private int currentId;
        private CharSequence currentName;
        private CharSequence currentStatus;
        private TLRPC$User currentUser;
        private CharSequence formattedPhoneNumber;
        private TLRPC$User formattedPhoneNumberUser;
        private TLRPC$FileLocation lastAvatar;
        private String lastName;
        private int lastStatus;
        private SimpleTextView nameTextView;
        private boolean needDivider;
        private final Theme.ResourcesProvider resourcesProvider;
        private SimpleTextView statusTextView;

        /* loaded from: classes3.dex */
        public interface CharSequenceCallback {
            CharSequence run();
        }

        @Override // android.view.View
        public boolean hasOverlappingRendering() {
            return false;
        }

        public UserCell(Context context, Theme.ResourcesProvider resourcesProvider) {
            super(context);
            this.currentAccount = UserConfig.selectedAccount;
            this.resourcesProvider = resourcesProvider;
            this.avatarDrawable = new AvatarDrawable(resourcesProvider);
            BackupImageView backupImageView = new BackupImageView(context);
            this.avatarImageView = backupImageView;
            backupImageView.setRoundRadius(AndroidUtilities.dp(23.0f));
            BackupImageView backupImageView2 = this.avatarImageView;
            boolean z = LocaleController.isRTL;
            addView(backupImageView2, LayoutHelper.createFrame(46, 46.0f, (z ? 5 : 3) | 48, z ? 0.0f : 14.0f, 9.0f, z ? 14.0f : 0.0f, 0.0f));
            SimpleTextView simpleTextView = new SimpleTextView(this, context) { // from class: org.telegram.ui.Components.ChatAttachAlertContactsLayout.UserCell.1
                @Override // org.telegram.ui.ActionBar.SimpleTextView
                public boolean setText(CharSequence charSequence, boolean z2) {
                    return super.setText(Emoji.replaceEmoji(charSequence, getPaint().getFontMetricsInt(), AndroidUtilities.dp(14.0f), false), z2);
                }
            };
            this.nameTextView = simpleTextView;
            NotificationCenter.listenEmojiLoading(simpleTextView);
            this.nameTextView.setTextColor(getThemedColor(Theme.key_dialogTextBlack));
            this.nameTextView.setTypeface(AndroidUtilities.bold());
            this.nameTextView.setTextSize(16);
            this.nameTextView.setGravity((LocaleController.isRTL ? 5 : 3) | 48);
            SimpleTextView simpleTextView2 = this.nameTextView;
            boolean z2 = LocaleController.isRTL;
            addView(simpleTextView2, LayoutHelper.createFrame(-1, 20.0f, (z2 ? 5 : 3) | 48, z2 ? 28.0f : 72.0f, 12.0f, z2 ? 72.0f : 28.0f, 0.0f));
            SimpleTextView simpleTextView3 = new SimpleTextView(context);
            this.statusTextView = simpleTextView3;
            simpleTextView3.setTextSize(13);
            this.statusTextView.setTextColor(getThemedColor(Theme.key_dialogTextGray2));
            this.statusTextView.setGravity((LocaleController.isRTL ? 5 : 3) | 48);
            SimpleTextView simpleTextView4 = this.statusTextView;
            boolean z3 = LocaleController.isRTL;
            addView(simpleTextView4, LayoutHelper.createFrame(-1, 20.0f, (z3 ? 5 : 3) | 48, z3 ? 28.0f : 72.0f, 36.0f, z3 ? 72.0f : 28.0f, 0.0f));
            CheckBox2 checkBox2 = new CheckBox2(context, 21, resourcesProvider);
            this.checkBox = checkBox2;
            checkBox2.setColor(-1, Theme.key_windowBackgroundWhite, Theme.key_checkboxCheck);
            this.checkBox.setDrawUnchecked(false);
            this.checkBox.setDrawBackgroundAsArc(3);
            CheckBox2 checkBox22 = this.checkBox;
            boolean z4 = LocaleController.isRTL;
            addView(checkBox22, LayoutHelper.createFrame(24, 24.0f, (z4 ? 5 : 3) | 48, z4 ? 0.0f : 44.0f, 37.0f, z4 ? 44.0f : 0.0f, 0.0f));
        }

        public void setCurrentId(int i) {
            this.currentId = i;
        }

        public void setData(TLRPC$User tLRPC$User, CharSequence charSequence, CharSequence charSequence2, boolean z) {
            if (tLRPC$User == null && charSequence == null && charSequence2 == null) {
                this.currentStatus = null;
                this.currentName = null;
                this.nameTextView.setText("");
                this.statusTextView.setText("");
                this.avatarImageView.setImageDrawable(null);
                return;
            }
            this.currentStatus = charSequence2;
            this.currentName = charSequence;
            this.currentUser = tLRPC$User;
            this.needDivider = z;
            setWillNotDraw(!z);
            update(0);
        }

        public void setData(TLRPC$User tLRPC$User, CharSequence charSequence, final CharSequenceCallback charSequenceCallback, boolean z) {
            setData(tLRPC$User, charSequence, (CharSequence) null, z);
            Utilities.globalQueue.postRunnable(new Runnable() { // from class: org.telegram.ui.Components.ChatAttachAlertContactsLayout$UserCell$$ExternalSyntheticLambda3
                @Override // java.lang.Runnable
                public final void run() {
                    ChatAttachAlertContactsLayout.UserCell.this.lambda$setData$1(charSequenceCallback);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$setData$1(CharSequenceCallback charSequenceCallback) {
            final CharSequence run = charSequenceCallback.run();
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.Components.ChatAttachAlertContactsLayout$UserCell$$ExternalSyntheticLambda2
                @Override // java.lang.Runnable
                public final void run() {
                    ChatAttachAlertContactsLayout.UserCell.this.lambda$setData$0(run);
                }
            });
        }

        public void setChecked(boolean z, boolean z2) {
            if (this.checkBox.getVisibility() != 0) {
                this.checkBox.setVisibility(0);
            }
            this.checkBox.setChecked(z, z2);
        }

        /* renamed from: setStatus */
        public void lambda$setData$0(CharSequence charSequence) {
            CharSequence charSequence2;
            this.currentStatus = charSequence;
            if (charSequence != null) {
                this.statusTextView.setText(charSequence);
                return;
            }
            TLRPC$User tLRPC$User = this.currentUser;
            if (tLRPC$User != null) {
                if (TextUtils.isEmpty(tLRPC$User.phone)) {
                    this.statusTextView.setText(LocaleController.getString("NumberUnknown", R.string.NumberUnknown));
                } else if (this.formattedPhoneNumberUser != this.currentUser && (charSequence2 = this.formattedPhoneNumber) != null) {
                    this.statusTextView.setText(charSequence2);
                } else {
                    this.statusTextView.setText("");
                    Utilities.globalQueue.postRunnable(new Runnable() { // from class: org.telegram.ui.Components.ChatAttachAlertContactsLayout$UserCell$$ExternalSyntheticLambda0
                        @Override // java.lang.Runnable
                        public final void run() {
                            ChatAttachAlertContactsLayout.UserCell.this.lambda$setStatus$3();
                        }
                    });
                }
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$setStatus$3() {
            if (this.currentUser != null) {
                PhoneFormat phoneFormat = PhoneFormat.getInstance();
                this.formattedPhoneNumber = phoneFormat.format("+" + this.currentUser.phone);
                this.formattedPhoneNumberUser = this.currentUser;
                AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.Components.ChatAttachAlertContactsLayout$UserCell$$ExternalSyntheticLambda1
                    @Override // java.lang.Runnable
                    public final void run() {
                        ChatAttachAlertContactsLayout.UserCell.this.lambda$setStatus$2();
                    }
                });
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$setStatus$2() {
            this.statusTextView.setText(this.formattedPhoneNumber);
        }

        @Override // android.widget.FrameLayout, android.view.View
        protected void onMeasure(int i, int i2) {
            super.onMeasure(View.MeasureSpec.makeMeasureSpec(View.MeasureSpec.getSize(i), 1073741824), View.MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(64.0f) + (this.needDivider ? 1 : 0), 1073741824));
        }

        /* JADX WARN: Code restructure failed: missing block: B:47:0x0068, code lost:
            if (r12.equals(r11.lastName) == false) goto L37;
         */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public void update(int i) {
            String str;
            TLRPC$FileLocation tLRPC$FileLocation;
            TLRPC$UserProfilePhoto tLRPC$UserProfilePhoto;
            TLRPC$User tLRPC$User = this.currentUser;
            TLRPC$FileLocation tLRPC$FileLocation2 = (tLRPC$User == null || (tLRPC$UserProfilePhoto = tLRPC$User.photo) == null) ? null : tLRPC$UserProfilePhoto.photo_small;
            if (i != 0) {
                boolean z = true;
                boolean z2 = (MessagesController.UPDATE_MASK_AVATAR & i) != 0 && (((tLRPC$FileLocation = this.lastAvatar) != null && tLRPC$FileLocation2 == null) || ((tLRPC$FileLocation == null && tLRPC$FileLocation2 != null) || !(tLRPC$FileLocation == null || tLRPC$FileLocation2 == null || (tLRPC$FileLocation.volume_id == tLRPC$FileLocation2.volume_id && tLRPC$FileLocation.local_id == tLRPC$FileLocation2.local_id))));
                if (tLRPC$User != null && !z2 && (MessagesController.UPDATE_MASK_STATUS & i) != 0) {
                    TLRPC$UserStatus tLRPC$UserStatus = tLRPC$User.status;
                    if ((tLRPC$UserStatus != null ? tLRPC$UserStatus.expires : 0) != this.lastStatus) {
                        z2 = true;
                    }
                }
                if (z2 || this.currentName != null || this.lastName == null || (i & MessagesController.UPDATE_MASK_NAME) == 0) {
                    str = null;
                } else {
                    str = tLRPC$User != null ? UserObject.getUserName(tLRPC$User) : null;
                }
                z = z2;
                if (!z) {
                    return;
                }
            } else {
                str = null;
            }
            TLRPC$User tLRPC$User2 = this.currentUser;
            if (tLRPC$User2 != null) {
                this.avatarDrawable.setInfo(this.currentAccount, tLRPC$User2);
                TLRPC$UserStatus tLRPC$UserStatus2 = this.currentUser.status;
                if (tLRPC$UserStatus2 != null) {
                    this.lastStatus = tLRPC$UserStatus2.expires;
                } else {
                    this.lastStatus = 0;
                }
            } else {
                CharSequence charSequence = this.currentName;
                if (charSequence != null) {
                    this.avatarDrawable.setInfo(this.currentId, charSequence.toString(), null);
                } else {
                    this.avatarDrawable.setInfo(this.currentId, "#", null);
                }
            }
            CharSequence charSequence2 = this.currentName;
            if (charSequence2 != null) {
                this.lastName = null;
                this.nameTextView.setText(charSequence2);
            } else {
                TLRPC$User tLRPC$User3 = this.currentUser;
                if (tLRPC$User3 != null) {
                    if (str == null) {
                        str = UserObject.getUserName(tLRPC$User3);
                    }
                    this.lastName = str;
                } else {
                    this.lastName = "";
                }
                this.nameTextView.setText(this.lastName);
            }
            lambda$setData$0(this.currentStatus);
            this.lastAvatar = tLRPC$FileLocation2;
            TLRPC$User tLRPC$User4 = this.currentUser;
            if (tLRPC$User4 != null) {
                this.avatarImageView.setForUserOrChat(tLRPC$User4, this.avatarDrawable);
            } else {
                this.avatarImageView.setImageDrawable(this.avatarDrawable);
            }
        }

        @Override // android.view.View
        protected void onDraw(Canvas canvas) {
            if (this.needDivider) {
                canvas.drawLine(LocaleController.isRTL ? 0.0f : AndroidUtilities.dp(70.0f), getMeasuredHeight() - 1, getMeasuredWidth() - (LocaleController.isRTL ? AndroidUtilities.dp(70.0f) : 0), getMeasuredHeight() - 1, Theme.dividerPaint);
            }
        }

        protected int getThemedColor(int i) {
            return Theme.getColor(i, this.resourcesProvider);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes3.dex */
    public static class ListItemID {
        private final long id;
        private final Type type;

        /* loaded from: classes3.dex */
        public enum Type {
            USER,
            CONTACT
        }

        public static ListItemID of(Object obj) {
            if (obj instanceof ContactsController.Contact) {
                return new ListItemID(Type.CONTACT, ((ContactsController.Contact) obj).contact_id);
            }
            if (obj instanceof TLRPC$User) {
                return new ListItemID(Type.USER, ((TLRPC$User) obj).id);
            }
            return null;
        }

        public ListItemID(Type type, long j) {
            this.type = type;
            this.id = j;
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null || ListItemID.class != obj.getClass()) {
                return false;
            }
            ListItemID listItemID = (ListItemID) obj;
            return this.id == listItemID.id && this.type == listItemID.type;
        }

        public int hashCode() {
            return Objects.hash(this.type, Long.valueOf(this.id));
        }
    }

    public ChatAttachAlertContactsLayout(ChatAttachAlert chatAttachAlert, Context context, final Theme.ResourcesProvider resourcesProvider) {
        super(chatAttachAlert, context, resourcesProvider);
        this.selectedContacts = new HashMap<>();
        this.selectedContactsOrder = new ArrayList<>();
        this.sendPressed = false;
        this.searchAdapter = new ShareSearchAdapter(context);
        FrameLayout frameLayout = new FrameLayout(context);
        this.frameLayout = frameLayout;
        frameLayout.setBackgroundColor(getThemedColor(Theme.key_dialogBackground));
        SearchField searchField = new SearchField(context, false, resourcesProvider) { // from class: org.telegram.ui.Components.ChatAttachAlertContactsLayout.1
            @Override // org.telegram.ui.Components.SearchField
            public void onTextChange(String str) {
                if (str.length() != 0) {
                    if (ChatAttachAlertContactsLayout.this.emptyView != null) {
                        ChatAttachAlertContactsLayout.this.emptyView.setText(LocaleController.getString("NoResult", R.string.NoResult));
                    }
                } else if (ChatAttachAlertContactsLayout.this.listView.getAdapter() != ChatAttachAlertContactsLayout.this.listAdapter) {
                    int currentTop = ChatAttachAlertContactsLayout.this.getCurrentTop();
                    ChatAttachAlertContactsLayout.this.emptyView.setText(LocaleController.getString("NoContacts", R.string.NoContacts));
                    ChatAttachAlertContactsLayout.this.emptyView.showTextView();
                    ChatAttachAlertContactsLayout.this.listView.setAdapter(ChatAttachAlertContactsLayout.this.listAdapter);
                    ChatAttachAlertContactsLayout.this.listAdapter.notifyDataSetChanged();
                    if (currentTop > 0) {
                        ChatAttachAlertContactsLayout.this.layoutManager.scrollToPositionWithOffset(0, -currentTop);
                    }
                }
                if (ChatAttachAlertContactsLayout.this.searchAdapter != null) {
                    ChatAttachAlertContactsLayout.this.searchAdapter.search(str);
                }
            }

            @Override // android.view.ViewGroup
            public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
                ChatAttachAlertContactsLayout.this.parentAlert.makeFocusable(getSearchEditText(), true);
                return super.onInterceptTouchEvent(motionEvent);
            }

            @Override // org.telegram.ui.Components.SearchField
            public void processTouchEvent(MotionEvent motionEvent) {
                MotionEvent obtain = MotionEvent.obtain(motionEvent);
                obtain.setLocation(obtain.getRawX(), (obtain.getRawY() - ChatAttachAlertContactsLayout.this.parentAlert.getSheetContainer().getTranslationY()) - AndroidUtilities.dp(58.0f));
                ChatAttachAlertContactsLayout.this.listView.dispatchTouchEvent(obtain);
                obtain.recycle();
            }

            @Override // org.telegram.ui.Components.SearchField
            protected void onFieldTouchUp(EditTextBoldCursor editTextBoldCursor) {
                ChatAttachAlertContactsLayout.this.parentAlert.makeFocusable(editTextBoldCursor, true);
            }
        };
        this.searchField = searchField;
        searchField.setHint(LocaleController.getString("SearchFriends", R.string.SearchFriends));
        this.frameLayout.addView(this.searchField, LayoutHelper.createFrame(-1, -1, 51));
        EmptyTextProgressView emptyTextProgressView = new EmptyTextProgressView(context, null, resourcesProvider);
        this.emptyView = emptyTextProgressView;
        emptyTextProgressView.showTextView();
        this.emptyView.setText(LocaleController.getString("NoContacts", R.string.NoContacts));
        addView(this.emptyView, LayoutHelper.createFrame(-1, -1.0f, 51, 0.0f, 52.0f, 0.0f, 0.0f));
        RecyclerListView recyclerListView = new RecyclerListView(context, resourcesProvider) { // from class: org.telegram.ui.Components.ChatAttachAlertContactsLayout.2
            @Override // org.telegram.ui.Components.RecyclerListView
            protected boolean allowSelectChildAtPosition(float f, float f2) {
                return f2 >= ((float) ((ChatAttachAlertContactsLayout.this.parentAlert.scrollOffsetY[0] + AndroidUtilities.dp(30.0f)) + ((Build.VERSION.SDK_INT < 21 || ChatAttachAlertContactsLayout.this.parentAlert.inBubbleMode) ? 0 : AndroidUtilities.statusBarHeight)));
            }
        };
        this.listView = recyclerListView;
        recyclerListView.setClipToPadding(false);
        RecyclerListView recyclerListView2 = this.listView;
        FillLastLinearLayoutManager fillLastLinearLayoutManager = new FillLastLinearLayoutManager(getContext(), 1, false, AndroidUtilities.dp(9.0f), this.listView) { // from class: org.telegram.ui.Components.ChatAttachAlertContactsLayout.3
            @Override // androidx.recyclerview.widget.LinearLayoutManager, androidx.recyclerview.widget.RecyclerView.LayoutManager
            public void smoothScrollToPosition(RecyclerView recyclerView, RecyclerView.State state, int i) {
                LinearSmoothScroller linearSmoothScroller = new LinearSmoothScroller(recyclerView.getContext()) { // from class: org.telegram.ui.Components.ChatAttachAlertContactsLayout.3.1
                    @Override // androidx.recyclerview.widget.LinearSmoothScroller
                    public int calculateDyToMakeVisible(View view, int i2) {
                        return super.calculateDyToMakeVisible(view, i2) - (ChatAttachAlertContactsLayout.this.listView.getPaddingTop() - AndroidUtilities.dp(8.0f));
                    }

                    /* JADX INFO: Access modifiers changed from: protected */
                    @Override // androidx.recyclerview.widget.LinearSmoothScroller
                    public int calculateTimeForDeceleration(int i2) {
                        return super.calculateTimeForDeceleration(i2) * 2;
                    }
                };
                linearSmoothScroller.setTargetPosition(i);
                startSmoothScroll(linearSmoothScroller);
            }
        };
        this.layoutManager = fillLastLinearLayoutManager;
        recyclerListView2.setLayoutManager(fillLastLinearLayoutManager);
        this.layoutManager.setBind(false);
        this.listView.setHorizontalScrollBarEnabled(false);
        this.listView.setVerticalScrollBarEnabled(false);
        this.listView.setClipToPadding(false);
        this.listView.setPadding(0, 0, 0, AndroidUtilities.dp(48.0f));
        addView(this.listView, LayoutHelper.createFrame(-1, -1.0f, 51, 0.0f, 0.0f, 0.0f, 0.0f));
        RecyclerListView recyclerListView3 = this.listView;
        ShareAdapter shareAdapter = new ShareAdapter(context);
        this.listAdapter = shareAdapter;
        recyclerListView3.setAdapter(shareAdapter);
        this.listView.setGlowColor(getThemedColor(Theme.key_dialogScrollGlow));
        this.listView.setOnItemClickListener(new RecyclerListView.OnItemClickListener() { // from class: org.telegram.ui.Components.ChatAttachAlertContactsLayout$$ExternalSyntheticLambda2
            @Override // org.telegram.ui.Components.RecyclerListView.OnItemClickListener
            public final void onItemClick(View view, int i) {
                ChatAttachAlertContactsLayout.this.lambda$new$1(resourcesProvider, view, i);
            }
        });
        this.listView.setOnScrollListener(new RecyclerView.OnScrollListener() { // from class: org.telegram.ui.Components.ChatAttachAlertContactsLayout.4
            @Override // androidx.recyclerview.widget.RecyclerView.OnScrollListener
            public void onScrolled(RecyclerView recyclerView, int i, int i2) {
                ChatAttachAlertContactsLayout chatAttachAlertContactsLayout = ChatAttachAlertContactsLayout.this;
                chatAttachAlertContactsLayout.parentAlert.updateLayout(chatAttachAlertContactsLayout, true, i2);
                ChatAttachAlertContactsLayout.this.updateEmptyViewPosition();
            }
        });
        this.listView.setOnItemLongClickListener(new RecyclerListView.OnItemLongClickListener() { // from class: org.telegram.ui.Components.ChatAttachAlertContactsLayout$$ExternalSyntheticLambda3
            @Override // org.telegram.ui.Components.RecyclerListView.OnItemLongClickListener
            public final boolean onItemClick(View view, int i) {
                boolean lambda$new$2;
                lambda$new$2 = ChatAttachAlertContactsLayout.this.lambda$new$2(view, i);
                return lambda$new$2;
            }
        });
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(-1, AndroidUtilities.getShadowHeight(), 51);
        layoutParams.topMargin = AndroidUtilities.dp(58.0f);
        View view = new View(context);
        this.shadow = view;
        view.setBackgroundColor(getThemedColor(Theme.key_dialogShadowLine));
        this.shadow.setAlpha(0.0f);
        this.shadow.setTag(1);
        addView(this.shadow, layoutParams);
        addView(this.frameLayout, LayoutHelper.createFrame(-1, 58, 51));
        NotificationCenter.getInstance(this.parentAlert.currentAccount).addObserver(this, NotificationCenter.contactsDidLoad);
        updateEmptyView();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$1(Theme.ResourcesProvider resourcesProvider, View view, int i) {
        Object item;
        ContactsController.Contact contact;
        String str;
        String str2;
        String str3;
        String str4;
        RecyclerView.Adapter adapter = this.listView.getAdapter();
        ShareSearchAdapter shareSearchAdapter = this.searchAdapter;
        if (adapter == shareSearchAdapter) {
            item = shareSearchAdapter.getItem(i);
        } else {
            int sectionForPosition = this.listAdapter.getSectionForPosition(i);
            int positionInSectionForPosition = this.listAdapter.getPositionInSectionForPosition(i);
            if (positionInSectionForPosition < 0 || sectionForPosition < 0) {
                return;
            }
            item = this.listAdapter.getItem(sectionForPosition, positionInSectionForPosition);
        }
        if (item != null) {
            if (!this.selectedContacts.isEmpty()) {
                addOrRemoveSelectedContact((UserCell) view, item);
                return;
            }
            if (item instanceof ContactsController.Contact) {
                ContactsController.Contact contact2 = (ContactsController.Contact) item;
                TLRPC$User tLRPC$User = contact2.user;
                if (tLRPC$User != null) {
                    str3 = tLRPC$User.first_name;
                    str4 = tLRPC$User.last_name;
                } else {
                    str3 = contact2.first_name;
                    str4 = contact2.last_name;
                }
                contact = contact2;
                str2 = str4;
                str = str3;
            } else {
                TLRPC$User tLRPC$User2 = (TLRPC$User) item;
                ContactsController.Contact contact3 = new ContactsController.Contact();
                String str5 = tLRPC$User2.first_name;
                contact3.first_name = str5;
                String str6 = tLRPC$User2.last_name;
                contact3.last_name = str6;
                contact3.phones.add(tLRPC$User2.phone);
                contact3.user = tLRPC$User2;
                contact = contact3;
                str = str5;
                str2 = str6;
            }
            PhonebookShareAlert phonebookShareAlert = new PhonebookShareAlert(this.parentAlert.baseFragment, contact, (TLRPC$User) null, (Uri) null, (File) null, str, str2, resourcesProvider);
            phonebookShareAlert.setDelegate(new PhonebookShareAlertDelegate() { // from class: org.telegram.ui.Components.ChatAttachAlertContactsLayout$$ExternalSyntheticLambda1
                @Override // org.telegram.ui.Components.ChatAttachAlertContactsLayout.PhonebookShareAlertDelegate
                public final void didSelectContact(TLRPC$User tLRPC$User3, boolean z, int i2, long j, boolean z2) {
                    ChatAttachAlertContactsLayout.this.lambda$new$0(tLRPC$User3, z, i2, j, z2);
                }

                @Override // org.telegram.ui.Components.ChatAttachAlertContactsLayout.PhonebookShareAlertDelegate
                public /* synthetic */ void didSelectContacts(ArrayList arrayList, String str7, boolean z, int i2, long j, boolean z2) {
                    ChatAttachAlertContactsLayout.PhonebookShareAlertDelegate.-CC.$default$didSelectContacts(this, arrayList, str7, z, i2, j, z2);
                }
            });
            phonebookShareAlert.show();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0(TLRPC$User tLRPC$User, boolean z, int i, long j, boolean z2) {
        this.parentAlert.dismiss(true);
        this.delegate.didSelectContact(tLRPC$User, z, i, j, z2);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ boolean lambda$new$2(View view, int i) {
        Object item;
        RecyclerView.Adapter adapter = this.listView.getAdapter();
        ShareSearchAdapter shareSearchAdapter = this.searchAdapter;
        if (adapter == shareSearchAdapter) {
            item = shareSearchAdapter.getItem(i);
        } else {
            item = this.listAdapter.getItem(i);
        }
        if (item != null) {
            addOrRemoveSelectedContact((UserCell) view, item);
            return true;
        }
        return false;
    }

    public void addOrRemoveSelectedContact(UserCell userCell, Object obj) {
        boolean z = false;
        if (this.selectedContacts.isEmpty() && !this.multipleSelectionAllowed) {
            showErrorBox(LocaleController.formatString("AttachContactsSlowMode", R.string.AttachContactsSlowMode, new Object[0]));
            return;
        }
        ListItemID of = ListItemID.of(obj);
        if (this.selectedContacts.containsKey(of)) {
            this.selectedContacts.remove(of);
            this.selectedContactsOrder.remove(of);
        } else {
            this.selectedContacts.put(of, obj);
            this.selectedContactsOrder.add(of);
            z = true;
        }
        userCell.setChecked(z, true);
        this.parentAlert.updateCountButton(z ? 1 : 2);
    }

    public void setMultipleSelectionAllowed(boolean z) {
        this.multipleSelectionAllowed = z;
    }

    @Override // org.telegram.ui.Components.ChatAttachAlert.AttachAlertLayout
    public int getSelectedItemsCount() {
        return this.selectedContacts.size();
    }

    private void showErrorBox(String str) {
        new AlertDialog.Builder(getContext(), this.resourcesProvider).setTitle(LocaleController.getString("AppName", R.string.AppName)).setMessage(str).setPositiveButton(LocaleController.getString("OK", R.string.OK), null).show();
    }

    /* JADX WARN: Removed duplicated region for block: B:41:0x00e4  */
    /* JADX WARN: Removed duplicated region for block: B:44:0x0105  */
    /* JADX WARN: Removed duplicated region for block: B:47:0x0111  */
    /* JADX WARN: Removed duplicated region for block: B:48:0x0121  */
    /* JADX WARN: Removed duplicated region for block: B:51:0x0142  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private TLRPC$User prepareContact(Object obj) {
        String str;
        ContactsController.Contact contact;
        String str2;
        ArrayList<TLRPC$User> arrayList;
        ArrayList<TLRPC$RestrictionReason> arrayList2;
        TLRPC$TL_userContact_old2 tLRPC$TL_userContact_old2;
        StringBuilder sb;
        int lastIndexOf;
        boolean z;
        if (obj instanceof ContactsController.Contact) {
            contact = (ContactsController.Contact) obj;
            TLRPC$User tLRPC$User = contact.user;
            if (tLRPC$User != null) {
                str = tLRPC$User.first_name;
                str2 = tLRPC$User.last_name;
            } else {
                str = contact.first_name;
                str2 = contact.last_name;
            }
        } else {
            TLRPC$User tLRPC$User2 = (TLRPC$User) obj;
            ContactsController.Contact contact2 = new ContactsController.Contact();
            str = tLRPC$User2.first_name;
            contact2.first_name = str;
            String str3 = tLRPC$User2.last_name;
            contact2.last_name = str3;
            contact2.phones.add(tLRPC$User2.phone);
            contact2.user = tLRPC$User2;
            contact = contact2;
            str2 = str3;
        }
        String formatName = ContactsController.formatName(str, str2);
        ArrayList arrayList3 = new ArrayList();
        ArrayList arrayList4 = new ArrayList();
        ArrayList arrayList5 = new ArrayList();
        String str4 = contact.key;
        if (str4 != null) {
            arrayList = AndroidUtilities.loadVCardFromStream(Uri.withAppendedPath(ContactsContract.Contacts.CONTENT_VCARD_URI, str4), this.parentAlert.currentAccount, true, arrayList3, formatName);
        } else {
            AndroidUtilities.VcardItem vcardItem = new AndroidUtilities.VcardItem();
            vcardItem.type = 0;
            ArrayList<String> arrayList6 = vcardItem.vcardData;
            String str5 = "TEL;MOBILE:+" + contact.user.phone;
            vcardItem.fullData = str5;
            arrayList6.add(str5);
            arrayList4.add(vcardItem);
            arrayList = null;
        }
        TLRPC$User tLRPC$User3 = contact.user;
        if (arrayList != null) {
            for (int i = 0; i < arrayList3.size(); i++) {
                AndroidUtilities.VcardItem vcardItem2 = (AndroidUtilities.VcardItem) arrayList3.get(i);
                if (vcardItem2.type == 0) {
                    int i2 = 0;
                    while (true) {
                        if (i2 >= arrayList4.size()) {
                            z = false;
                            break;
                        } else if (((AndroidUtilities.VcardItem) arrayList4.get(i2)).getValue(false).equals(vcardItem2.getValue(false))) {
                            z = true;
                            break;
                        } else {
                            i2++;
                        }
                    }
                    if (z) {
                        vcardItem2.checked = false;
                    } else {
                        arrayList4.add(vcardItem2);
                    }
                } else {
                    arrayList5.add(vcardItem2);
                }
            }
            if (!arrayList.isEmpty()) {
                TLRPC$User tLRPC$User4 = arrayList.get(0);
                arrayList2 = tLRPC$User4.restriction_reason;
                if (TextUtils.isEmpty(str)) {
                    str = tLRPC$User4.first_name;
                    str2 = tLRPC$User4.last_name;
                }
                tLRPC$TL_userContact_old2 = new TLRPC$TL_userContact_old2();
                if (tLRPC$User3 == null) {
                    tLRPC$TL_userContact_old2.id = tLRPC$User3.id;
                    tLRPC$TL_userContact_old2.access_hash = tLRPC$User3.access_hash;
                    tLRPC$TL_userContact_old2.photo = tLRPC$User3.photo;
                    tLRPC$TL_userContact_old2.status = tLRPC$User3.status;
                    tLRPC$TL_userContact_old2.first_name = tLRPC$User3.first_name;
                    tLRPC$TL_userContact_old2.last_name = tLRPC$User3.last_name;
                    tLRPC$TL_userContact_old2.phone = tLRPC$User3.phone;
                    if (arrayList2 != null) {
                        tLRPC$TL_userContact_old2.restriction_reason = arrayList2;
                    }
                } else {
                    tLRPC$TL_userContact_old2.first_name = str;
                    tLRPC$TL_userContact_old2.last_name = str2;
                }
                if (tLRPC$TL_userContact_old2.restriction_reason.isEmpty()) {
                    sb = new StringBuilder(tLRPC$TL_userContact_old2.restriction_reason.get(0).text);
                } else {
                    sb = new StringBuilder(String.format(Locale.US, "BEGIN:VCARD\nVERSION:3.0\nFN:%1$s\nEND:VCARD", ContactsController.formatName(tLRPC$TL_userContact_old2.first_name, tLRPC$TL_userContact_old2.last_name)));
                }
                lastIndexOf = sb.lastIndexOf("END:VCARD");
                if (lastIndexOf >= 0) {
                    tLRPC$TL_userContact_old2.phone = null;
                    for (int size = arrayList4.size() - 1; size >= 0; size--) {
                        AndroidUtilities.VcardItem vcardItem3 = (AndroidUtilities.VcardItem) arrayList4.get(size);
                        if (vcardItem3.checked) {
                            if (tLRPC$TL_userContact_old2.phone == null) {
                                tLRPC$TL_userContact_old2.phone = vcardItem3.getValue(false);
                            }
                            for (int i3 = 0; i3 < vcardItem3.vcardData.size(); i3++) {
                                sb.insert(lastIndexOf, vcardItem3.vcardData.get(i3) + "\n");
                            }
                        }
                    }
                    for (int size2 = arrayList5.size() - 1; size2 >= 0; size2--) {
                        AndroidUtilities.VcardItem vcardItem4 = (AndroidUtilities.VcardItem) arrayList5.get(size2);
                        if (vcardItem4.checked) {
                            for (int size3 = vcardItem4.vcardData.size() - 1; size3 >= 0; size3 += -1) {
                                sb.insert(lastIndexOf, vcardItem4.vcardData.get(size3) + "\n");
                            }
                        }
                    }
                    tLRPC$TL_userContact_old2.restriction_reason.clear();
                    TLRPC$RestrictionReason tLRPC$RestrictionReason = new TLRPC$RestrictionReason();
                    tLRPC$RestrictionReason.text = sb.toString();
                    tLRPC$RestrictionReason.reason = "";
                    tLRPC$RestrictionReason.platform = "";
                    tLRPC$TL_userContact_old2.restriction_reason.add(tLRPC$RestrictionReason);
                }
                return tLRPC$TL_userContact_old2;
            }
        }
        arrayList2 = null;
        tLRPC$TL_userContact_old2 = new TLRPC$TL_userContact_old2();
        if (tLRPC$User3 == null) {
        }
        if (tLRPC$TL_userContact_old2.restriction_reason.isEmpty()) {
        }
        lastIndexOf = sb.lastIndexOf("END:VCARD");
        if (lastIndexOf >= 0) {
        }
        return tLRPC$TL_userContact_old2;
    }

    @Override // org.telegram.ui.Components.ChatAttachAlert.AttachAlertLayout
    public void sendSelectedItems(boolean z, int i, long j, boolean z2) {
        if ((this.selectedContacts.size() == 0 && this.delegate == null) || this.sendPressed) {
            return;
        }
        this.sendPressed = true;
        ArrayList<TLRPC$User> arrayList = new ArrayList<>(this.selectedContacts.size());
        Iterator<ListItemID> it = this.selectedContactsOrder.iterator();
        while (it.hasNext()) {
            arrayList.add(prepareContact(this.selectedContacts.get(it.next())));
        }
        this.delegate.didSelectContacts(arrayList, this.parentAlert.commentTextView.getText().toString(), z, i, j, z2);
    }

    public ArrayList<TLRPC$User> getSelected() {
        ArrayList<TLRPC$User> arrayList = new ArrayList<>(this.selectedContacts.size());
        Iterator<ListItemID> it = this.selectedContactsOrder.iterator();
        while (it.hasNext()) {
            arrayList.add(prepareContact(this.selectedContacts.get(it.next())));
        }
        return arrayList;
    }

    @Override // org.telegram.ui.Components.ChatAttachAlert.AttachAlertLayout
    public void scrollToTop() {
        this.listView.smoothScrollToPosition(0);
    }

    @Override // org.telegram.ui.Components.ChatAttachAlert.AttachAlertLayout
    public int getCurrentItemTop() {
        if (this.listView.getChildCount() <= 0) {
            return ConnectionsManager.DEFAULT_DATACENTER_ID;
        }
        View childAt = this.listView.getChildAt(0);
        RecyclerListView.Holder holder = (RecyclerListView.Holder) this.listView.findContainingViewHolder(childAt);
        int top = childAt.getTop() - AndroidUtilities.dp(8.0f);
        int i = (top <= 0 || holder == null || holder.getAdapterPosition() != 0) ? 0 : top;
        if (top >= 0 && holder != null && holder.getAdapterPosition() == 0) {
            runShadowAnimation(false);
        } else {
            runShadowAnimation(true);
            top = i;
        }
        this.frameLayout.setTranslationY(top);
        return top + AndroidUtilities.dp(12.0f);
    }

    @Override // org.telegram.ui.Components.ChatAttachAlert.AttachAlertLayout
    public int getFirstOffset() {
        return getListTopPadding() + AndroidUtilities.dp(4.0f);
    }

    @Override // android.view.View
    public void setTranslationY(float f) {
        super.setTranslationY(f);
        this.parentAlert.getSheetContainer().invalidate();
    }

    @Override // org.telegram.ui.Components.ChatAttachAlert.AttachAlertLayout
    public int getListTopPadding() {
        return this.listView.getPaddingTop();
    }

    @Override // org.telegram.ui.Components.ChatAttachAlert.AttachAlertLayout
    public void onPreMeasure(int i, int i2) {
        int i3;
        if (this.parentAlert.sizeNotifierFrameLayout.measureKeyboardHeight() > AndroidUtilities.dp(20.0f)) {
            i3 = AndroidUtilities.dp(8.0f);
            this.parentAlert.setAllowNestedScroll(false);
        } else {
            if (!AndroidUtilities.isTablet()) {
                android.graphics.Point point = AndroidUtilities.displaySize;
                if (point.x > point.y) {
                    i3 = (int) (i2 / 3.5f);
                    this.parentAlert.setAllowNestedScroll(true);
                }
            }
            i3 = (i2 / 5) * 2;
            this.parentAlert.setAllowNestedScroll(true);
        }
        if (this.listView.getPaddingTop() != i3) {
            this.ignoreLayout = true;
            this.listView.setPadding(0, i3, 0, AndroidUtilities.dp(48.0f));
            this.ignoreLayout = false;
        }
    }

    @Override // android.view.View, android.view.ViewParent
    public void requestLayout() {
        if (this.ignoreLayout) {
            return;
        }
        super.requestLayout();
    }

    private void runShadowAnimation(final boolean z) {
        if ((!z || this.shadow.getTag() == null) && (z || this.shadow.getTag() != null)) {
            return;
        }
        this.shadow.setTag(z ? null : 1);
        if (z) {
            this.shadow.setVisibility(0);
        }
        AnimatorSet animatorSet = this.shadowAnimation;
        if (animatorSet != null) {
            animatorSet.cancel();
        }
        AnimatorSet animatorSet2 = new AnimatorSet();
        this.shadowAnimation = animatorSet2;
        Animator[] animatorArr = new Animator[1];
        View view = this.shadow;
        Property property = View.ALPHA;
        float[] fArr = new float[1];
        fArr[0] = z ? 1.0f : 0.0f;
        animatorArr[0] = ObjectAnimator.ofFloat(view, property, fArr);
        animatorSet2.playTogether(animatorArr);
        this.shadowAnimation.setDuration(150L);
        this.shadowAnimation.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.Components.ChatAttachAlertContactsLayout.5
            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationEnd(Animator animator) {
                if (ChatAttachAlertContactsLayout.this.shadowAnimation == null || !ChatAttachAlertContactsLayout.this.shadowAnimation.equals(animator)) {
                    return;
                }
                if (!z) {
                    ChatAttachAlertContactsLayout.this.shadow.setVisibility(4);
                }
                ChatAttachAlertContactsLayout.this.shadowAnimation = null;
            }

            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationCancel(Animator animator) {
                if (ChatAttachAlertContactsLayout.this.shadowAnimation == null || !ChatAttachAlertContactsLayout.this.shadowAnimation.equals(animator)) {
                    return;
                }
                ChatAttachAlertContactsLayout.this.shadowAnimation = null;
            }
        });
        this.shadowAnimation.start();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int getCurrentTop() {
        if (this.listView.getChildCount() != 0) {
            int i = 0;
            View childAt = this.listView.getChildAt(0);
            RecyclerListView.Holder holder = (RecyclerListView.Holder) this.listView.findContainingViewHolder(childAt);
            if (holder != null) {
                int paddingTop = this.listView.getPaddingTop();
                if (holder.getAdapterPosition() == 0 && childAt.getTop() >= 0) {
                    i = childAt.getTop();
                }
                return paddingTop - i;
            }
            return -1000;
        }
        return -1000;
    }

    public void setDelegate(PhonebookShareAlertDelegate phonebookShareAlertDelegate) {
        this.delegate = phonebookShareAlertDelegate;
    }

    @Override // org.telegram.messenger.NotificationCenter.NotificationCenterDelegate
    public void didReceivedNotification(int i, int i2, Object... objArr) {
        ShareAdapter shareAdapter;
        if (i != NotificationCenter.contactsDidLoad || (shareAdapter = this.listAdapter) == null) {
            return;
        }
        shareAdapter.notifyDataSetChanged();
    }

    @Override // org.telegram.ui.Components.ChatAttachAlert.AttachAlertLayout
    public void onDestroy() {
        NotificationCenter.getInstance(this.parentAlert.currentAccount).removeObserver(this, NotificationCenter.contactsDidLoad);
    }

    @Override // org.telegram.ui.Components.ChatAttachAlert.AttachAlertLayout
    public void onShow(ChatAttachAlert.AttachAlertLayout attachAlertLayout) {
        this.layoutManager.scrollToPositionWithOffset(0, 0);
    }

    @Override // android.widget.FrameLayout, android.view.ViewGroup, android.view.View
    protected void onLayout(boolean z, int i, int i2, int i3, int i4) {
        super.onLayout(z, i, i2, i3, i4);
        updateEmptyViewPosition();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateEmptyViewPosition() {
        View childAt;
        if (this.emptyView.getVisibility() == 0 && (childAt = this.listView.getChildAt(0)) != null) {
            EmptyTextProgressView emptyTextProgressView = this.emptyView;
            emptyTextProgressView.setTranslationY(((emptyTextProgressView.getMeasuredHeight() - getMeasuredHeight()) + childAt.getTop()) / 2);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateEmptyView() {
        this.emptyView.setVisibility(this.listView.getAdapter().getItemCount() == 2 ? 0 : 8);
        updateEmptyViewPosition();
    }

    /* loaded from: classes3.dex */
    public class ShareAdapter extends RecyclerListView.SectionsAdapter {
        private int currentAccount = UserConfig.selectedAccount;
        private Context mContext;

        @Override // org.telegram.ui.Components.RecyclerListView.FastScrollAdapter
        public String getLetter(int i) {
            return null;
        }

        @Override // org.telegram.ui.Components.RecyclerListView.SectionsAdapter
        public View getSectionHeaderView(int i, View view) {
            return null;
        }

        public ShareAdapter(Context context) {
            this.mContext = context;
        }

        @Override // org.telegram.ui.Components.RecyclerListView.SectionsAdapter
        public Object getItem(int i, int i2) {
            if (i == 0) {
                return null;
            }
            int i3 = i - 1;
            HashMap<String, ArrayList<Object>> hashMap = ContactsController.getInstance(this.currentAccount).phoneBookSectionsDict;
            ArrayList<String> arrayList = ContactsController.getInstance(this.currentAccount).phoneBookSectionsArray;
            if (i3 < arrayList.size()) {
                ArrayList<Object> arrayList2 = hashMap.get(arrayList.get(i3));
                if (i2 < arrayList2.size()) {
                    return arrayList2.get(i2);
                }
            }
            return null;
        }

        @Override // org.telegram.ui.Components.RecyclerListView.SectionsAdapter
        public boolean isEnabled(RecyclerView.ViewHolder viewHolder, int i, int i2) {
            if (i == 0 || i == getSectionCount() - 1) {
                return false;
            }
            return i2 < ContactsController.getInstance(this.currentAccount).phoneBookSectionsDict.get(ContactsController.getInstance(this.currentAccount).phoneBookSectionsArray.get(i + (-1))).size();
        }

        @Override // org.telegram.ui.Components.RecyclerListView.SectionsAdapter
        public int getSectionCount() {
            return ContactsController.getInstance(this.currentAccount).phoneBookSectionsArray.size() + 2;
        }

        @Override // org.telegram.ui.Components.RecyclerListView.SectionsAdapter
        public int getCountForSection(int i) {
            if (i == 0 || i == getSectionCount() - 1) {
                return 1;
            }
            int i2 = i - 1;
            HashMap<String, ArrayList<Object>> hashMap = ContactsController.getInstance(this.currentAccount).phoneBookSectionsDict;
            ArrayList<String> arrayList = ContactsController.getInstance(this.currentAccount).phoneBookSectionsArray;
            if (i2 < arrayList.size()) {
                return hashMap.get(arrayList.get(i2)).size();
            }
            return 0;
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View userCell;
            if (i == 0) {
                userCell = new UserCell(this.mContext, ChatAttachAlertContactsLayout.this.resourcesProvider);
            } else if (i == 1) {
                userCell = new View(this.mContext);
                userCell.setLayoutParams(new RecyclerView.LayoutParams(-1, AndroidUtilities.dp(56.0f)));
            } else {
                userCell = new View(this.mContext);
            }
            return new RecyclerListView.Holder(userCell);
        }

        @Override // org.telegram.ui.Components.RecyclerListView.SectionsAdapter
        public void onBindViewHolder(int i, int i2, RecyclerView.ViewHolder viewHolder) {
            final TLRPC$User tLRPC$User;
            if (viewHolder.getItemViewType() == 0) {
                UserCell userCell = (UserCell) viewHolder.itemView;
                Object item = getItem(i, i2);
                boolean z = true;
                if (i == getSectionCount() - 2 && i2 == getCountForSection(i) - 1) {
                    z = false;
                }
                if (item instanceof ContactsController.Contact) {
                    final ContactsController.Contact contact = (ContactsController.Contact) item;
                    tLRPC$User = contact.user;
                    if (tLRPC$User == null) {
                        userCell.setCurrentId(contact.contact_id);
                        userCell.setData((TLRPC$User) null, ContactsController.formatName(contact.first_name, contact.last_name), new UserCell.CharSequenceCallback() { // from class: org.telegram.ui.Components.ChatAttachAlertContactsLayout$ShareAdapter$$ExternalSyntheticLambda0
                            @Override // org.telegram.ui.Components.ChatAttachAlertContactsLayout.UserCell.CharSequenceCallback
                            public final CharSequence run() {
                                CharSequence lambda$onBindViewHolder$0;
                                lambda$onBindViewHolder$0 = ChatAttachAlertContactsLayout.ShareAdapter.lambda$onBindViewHolder$0(ContactsController.Contact.this);
                                return lambda$onBindViewHolder$0;
                            }
                        }, z);
                        tLRPC$User = null;
                    }
                } else {
                    tLRPC$User = (TLRPC$User) item;
                }
                if (tLRPC$User != null) {
                    userCell.setData(tLRPC$User, (CharSequence) null, new UserCell.CharSequenceCallback() { // from class: org.telegram.ui.Components.ChatAttachAlertContactsLayout$ShareAdapter$$ExternalSyntheticLambda1
                        @Override // org.telegram.ui.Components.ChatAttachAlertContactsLayout.UserCell.CharSequenceCallback
                        public final CharSequence run() {
                            CharSequence lambda$onBindViewHolder$1;
                            lambda$onBindViewHolder$1 = ChatAttachAlertContactsLayout.ShareAdapter.lambda$onBindViewHolder$1(TLRPC$User.this);
                            return lambda$onBindViewHolder$1;
                        }
                    }, z);
                }
                userCell.setChecked(ChatAttachAlertContactsLayout.this.selectedContacts.containsKey(ListItemID.of(item)), false);
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static /* synthetic */ CharSequence lambda$onBindViewHolder$0(ContactsController.Contact contact) {
            return contact.phones.isEmpty() ? "" : PhoneFormat.getInstance().format(contact.phones.get(0));
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static /* synthetic */ CharSequence lambda$onBindViewHolder$1(TLRPC$User tLRPC$User) {
            PhoneFormat phoneFormat = PhoneFormat.getInstance();
            return phoneFormat.format("+" + tLRPC$User.phone);
        }

        @Override // org.telegram.ui.Components.RecyclerListView.SectionsAdapter
        public int getItemViewType(int i, int i2) {
            if (i == 0) {
                return 1;
            }
            return i == getSectionCount() - 1 ? 2 : 0;
        }

        @Override // org.telegram.ui.Components.RecyclerListView.FastScrollAdapter
        public void getPositionForScrollProgress(RecyclerListView recyclerListView, float f, int[] iArr) {
            iArr[0] = 0;
            iArr[1] = 0;
        }

        @Override // org.telegram.ui.Components.RecyclerListView.SectionsAdapter, androidx.recyclerview.widget.RecyclerView.Adapter
        public void notifyDataSetChanged() {
            super.notifyDataSetChanged();
            ChatAttachAlertContactsLayout.this.updateEmptyView();
        }
    }

    /* loaded from: classes3.dex */
    public class ShareSearchAdapter extends RecyclerListView.SelectionAdapter {
        private int lastSearchId;
        private Context mContext;
        private ArrayList<Object> searchResult = new ArrayList<>();
        private ArrayList<CharSequence> searchResultNames = new ArrayList<>();
        private Runnable searchRunnable;

        public ShareSearchAdapter(Context context) {
            this.mContext = context;
        }

        public void search(final String str) {
            if (this.searchRunnable != null) {
                Utilities.searchQueue.cancelRunnable(this.searchRunnable);
                this.searchRunnable = null;
            }
            if (str == null) {
                this.searchResult.clear();
                this.searchResultNames.clear();
                notifyDataSetChanged();
                return;
            }
            final int i = this.lastSearchId + 1;
            this.lastSearchId = i;
            DispatchQueue dispatchQueue = Utilities.searchQueue;
            Runnable runnable = new Runnable() { // from class: org.telegram.ui.Components.ChatAttachAlertContactsLayout$ShareSearchAdapter$$ExternalSyntheticLambda1
                @Override // java.lang.Runnable
                public final void run() {
                    ChatAttachAlertContactsLayout.ShareSearchAdapter.this.lambda$search$0(str, i);
                }
            };
            this.searchRunnable = runnable;
            dispatchQueue.postRunnable(runnable, 300L);
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* renamed from: processSearch */
        public void lambda$search$0(final String str, final int i) {
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.Components.ChatAttachAlertContactsLayout$ShareSearchAdapter$$ExternalSyntheticLambda2
                @Override // java.lang.Runnable
                public final void run() {
                    ChatAttachAlertContactsLayout.ShareSearchAdapter.this.lambda$processSearch$2(str, i);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$processSearch$2(final String str, final int i) {
            final int i2 = UserConfig.selectedAccount;
            final ArrayList arrayList = new ArrayList(ContactsController.getInstance(i2).contactsBook.values());
            final ArrayList arrayList2 = new ArrayList(ContactsController.getInstance(i2).contacts);
            Utilities.searchQueue.postRunnable(new Runnable() { // from class: org.telegram.ui.Components.ChatAttachAlertContactsLayout$ShareSearchAdapter$$ExternalSyntheticLambda3
                @Override // java.lang.Runnable
                public final void run() {
                    ChatAttachAlertContactsLayout.ShareSearchAdapter.this.lambda$processSearch$1(str, arrayList, arrayList2, i2, i);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* JADX WARN: Code restructure failed: missing block: B:35:0x00c4, code lost:
            if (r5.contains(" " + r0) == false) goto L58;
         */
        /* JADX WARN: Code restructure failed: missing block: B:40:0x00e1, code lost:
            if (r6.contains(" " + r0) != false) goto L33;
         */
        /* JADX WARN: Code restructure failed: missing block: B:57:0x012d, code lost:
            if (r12.contains(" " + r0) != false) goto L80;
         */
        /* JADX WARN: Code restructure failed: missing block: B:99:0x0238, code lost:
            if (r6.contains(" " + r12) != false) goto L124;
         */
        /* JADX WARN: Removed duplicated region for block: B:124:0x01a2 A[ADDED_TO_REGION, SYNTHETIC] */
        /* JADX WARN: Removed duplicated region for block: B:63:0x0138  */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public /* synthetic */ void lambda$processSearch$1(String str, ArrayList arrayList, ArrayList arrayList2, int i, int i2) {
            String str2;
            String str3;
            char c;
            String publicUsername;
            String lowerCase = str.trim().toLowerCase();
            if (lowerCase.length() == 0) {
                this.lastSearchId = -1;
                updateSearchResults(str, new ArrayList<>(), new ArrayList<>(), this.lastSearchId);
                return;
            }
            String translitString = LocaleController.getInstance().getTranslitString(lowerCase);
            translitString = (lowerCase.equals(translitString) || translitString.length() == 0) ? null : null;
            int i3 = (translitString != null ? 1 : 0) + 1;
            String[] strArr = new String[i3];
            strArr[0] = lowerCase;
            if (translitString != null) {
                strArr[1] = translitString;
            }
            ArrayList<Object> arrayList3 = new ArrayList<>();
            ArrayList<CharSequence> arrayList4 = new ArrayList<>();
            LongSparseIntArray longSparseIntArray = new LongSparseIntArray();
            for (int i4 = 0; i4 < arrayList.size(); i4++) {
                ContactsController.Contact contact = (ContactsController.Contact) arrayList.get(i4);
                String lowerCase2 = ContactsController.formatName(contact.first_name, contact.last_name).toLowerCase();
                String translitString2 = LocaleController.getInstance().getTranslitString(lowerCase2);
                TLRPC$User tLRPC$User = contact.user;
                if (tLRPC$User != null) {
                    str2 = ContactsController.formatName(tLRPC$User.first_name, tLRPC$User.last_name).toLowerCase();
                    str3 = LocaleController.getInstance().getTranslitString(lowerCase2);
                } else {
                    str2 = null;
                    str3 = null;
                }
                if (lowerCase2.equals(translitString2)) {
                    translitString2 = null;
                }
                int i5 = 0;
                char c2 = 0;
                while (i5 < i3) {
                    String str4 = strArr[i5];
                    if (str2 != null) {
                        if (!str2.startsWith(str4)) {
                        }
                        c = 1;
                        String str5 = lowerCase2;
                        if (c == 0 || (contact.phones.isEmpty() && contact.shortPhones.isEmpty())) {
                            i5++;
                            lowerCase2 = str5;
                            c2 = c;
                        } else {
                            if (c == 3) {
                                arrayList4.add(AndroidUtilities.generateSearchName(contact.first_name, contact.last_name, str4));
                            } else if (c == 1) {
                                TLRPC$User tLRPC$User2 = contact.user;
                                arrayList4.add(AndroidUtilities.generateSearchName(tLRPC$User2.first_name, tLRPC$User2.last_name, str4));
                            } else {
                                arrayList4.add(AndroidUtilities.generateSearchName("@" + UserObject.getPublicUsername(contact.user), null, "@" + str4));
                            }
                            TLRPC$User tLRPC$User3 = contact.user;
                            if (tLRPC$User3 != null) {
                                longSparseIntArray.put(tLRPC$User3.id, 1);
                            }
                            arrayList3.add(contact);
                        }
                    }
                    if (str3 != null) {
                        if (!str3.startsWith(str4)) {
                        }
                        c = 1;
                        String str52 = lowerCase2;
                        if (c == 0) {
                        }
                        i5++;
                        lowerCase2 = str52;
                        c2 = c;
                    }
                    TLRPC$User tLRPC$User4 = contact.user;
                    if (tLRPC$User4 == null || (publicUsername = UserObject.getPublicUsername(tLRPC$User4)) == null || !publicUsername.startsWith(str4)) {
                        if (!lowerCase2.startsWith(str4)) {
                            if (!lowerCase2.contains(" " + str4)) {
                                if (translitString2 != null) {
                                    if (!translitString2.startsWith(str4)) {
                                    }
                                }
                                c = c2;
                            }
                        }
                        c = 3;
                    } else {
                        c = 2;
                    }
                    String str522 = lowerCase2;
                    if (c == 0) {
                    }
                    i5++;
                    lowerCase2 = str522;
                    c2 = c;
                }
            }
            for (int i6 = 0; i6 < arrayList2.size(); i6++) {
                TLRPC$TL_contact tLRPC$TL_contact = (TLRPC$TL_contact) arrayList2.get(i6);
                if (longSparseIntArray.indexOfKey(tLRPC$TL_contact.user_id) < 0) {
                    TLRPC$User user = MessagesController.getInstance(i).getUser(Long.valueOf(tLRPC$TL_contact.user_id));
                    String lowerCase3 = ContactsController.formatName(user.first_name, user.last_name).toLowerCase();
                    String translitString3 = LocaleController.getInstance().getTranslitString(lowerCase3);
                    if (lowerCase3.equals(translitString3)) {
                        translitString3 = null;
                    }
                    char c3 = 0;
                    for (int i7 = 0; i7 < i3; i7++) {
                        String str6 = strArr[i7];
                        if (!lowerCase3.startsWith(str6)) {
                            if (!lowerCase3.contains(" " + str6)) {
                                if (translitString3 != null) {
                                    if (!translitString3.startsWith(str6)) {
                                    }
                                }
                                String publicUsername2 = UserObject.getPublicUsername(user);
                                if (publicUsername2 != null && publicUsername2.startsWith(str6)) {
                                    c3 = 2;
                                }
                                if (c3 == 0 && user.phone != null) {
                                    if (c3 == 1) {
                                        arrayList4.add(AndroidUtilities.generateSearchName(user.first_name, user.last_name, str6));
                                    } else {
                                        arrayList4.add(AndroidUtilities.generateSearchName("@" + UserObject.getPublicUsername(user), null, "@" + str6));
                                    }
                                    arrayList3.add(user);
                                }
                            }
                        }
                        c3 = 1;
                        if (c3 == 0) {
                        }
                    }
                }
            }
            updateSearchResults(str, arrayList3, arrayList4, i2);
        }

        private void updateSearchResults(String str, final ArrayList<Object> arrayList, final ArrayList<CharSequence> arrayList2, final int i) {
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.Components.ChatAttachAlertContactsLayout$ShareSearchAdapter$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    ChatAttachAlertContactsLayout.ShareSearchAdapter.this.lambda$updateSearchResults$3(i, arrayList, arrayList2);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$updateSearchResults$3(int i, ArrayList arrayList, ArrayList arrayList2) {
            if (i != this.lastSearchId) {
                return;
            }
            if (i != -1 && ChatAttachAlertContactsLayout.this.listView.getAdapter() != ChatAttachAlertContactsLayout.this.searchAdapter) {
                ChatAttachAlertContactsLayout.this.listView.setAdapter(ChatAttachAlertContactsLayout.this.searchAdapter);
            }
            this.searchResult = arrayList;
            this.searchResultNames = arrayList2;
            notifyDataSetChanged();
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public int getItemCount() {
            return this.searchResult.size() + 2;
        }

        public Object getItem(int i) {
            int i2 = i - 1;
            if (i2 < 0 || i2 >= this.searchResult.size()) {
                return null;
            }
            return this.searchResult.get(i2);
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View userCell;
            if (i == 0) {
                userCell = new UserCell(this.mContext, ChatAttachAlertContactsLayout.this.resourcesProvider);
            } else if (i == 1) {
                userCell = new View(this.mContext);
                userCell.setLayoutParams(new RecyclerView.LayoutParams(-1, AndroidUtilities.dp(56.0f)));
            } else {
                userCell = new View(this.mContext);
            }
            return new RecyclerListView.Holder(userCell);
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
            final TLRPC$User tLRPC$User;
            if (viewHolder.getItemViewType() == 0) {
                UserCell userCell = (UserCell) viewHolder.itemView;
                boolean z = i != getItemCount() + (-2);
                Object item = getItem(i);
                if (item instanceof ContactsController.Contact) {
                    final ContactsController.Contact contact = (ContactsController.Contact) item;
                    tLRPC$User = contact.user;
                    if (tLRPC$User == null) {
                        userCell.setCurrentId(contact.contact_id);
                        userCell.setData((TLRPC$User) null, this.searchResultNames.get(i - 1), new UserCell.CharSequenceCallback() { // from class: org.telegram.ui.Components.ChatAttachAlertContactsLayout$ShareSearchAdapter$$ExternalSyntheticLambda4
                            @Override // org.telegram.ui.Components.ChatAttachAlertContactsLayout.UserCell.CharSequenceCallback
                            public final CharSequence run() {
                                CharSequence lambda$onBindViewHolder$4;
                                lambda$onBindViewHolder$4 = ChatAttachAlertContactsLayout.ShareSearchAdapter.lambda$onBindViewHolder$4(ContactsController.Contact.this);
                                return lambda$onBindViewHolder$4;
                            }
                        }, z);
                        tLRPC$User = null;
                    }
                } else {
                    tLRPC$User = (TLRPC$User) item;
                }
                if (tLRPC$User != null) {
                    userCell.setData(tLRPC$User, this.searchResultNames.get(i - 1), new UserCell.CharSequenceCallback() { // from class: org.telegram.ui.Components.ChatAttachAlertContactsLayout$ShareSearchAdapter$$ExternalSyntheticLambda5
                        @Override // org.telegram.ui.Components.ChatAttachAlertContactsLayout.UserCell.CharSequenceCallback
                        public final CharSequence run() {
                            CharSequence lambda$onBindViewHolder$5;
                            lambda$onBindViewHolder$5 = ChatAttachAlertContactsLayout.ShareSearchAdapter.lambda$onBindViewHolder$5(TLRPC$User.this);
                            return lambda$onBindViewHolder$5;
                        }
                    }, z);
                }
                userCell.setChecked(ChatAttachAlertContactsLayout.this.selectedContacts.containsKey(ListItemID.of(item)), false);
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static /* synthetic */ CharSequence lambda$onBindViewHolder$4(ContactsController.Contact contact) {
            return contact.phones.isEmpty() ? "" : PhoneFormat.getInstance().format(contact.phones.get(0));
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static /* synthetic */ CharSequence lambda$onBindViewHolder$5(TLRPC$User tLRPC$User) {
            PhoneFormat phoneFormat = PhoneFormat.getInstance();
            return phoneFormat.format("+" + tLRPC$User.phone);
        }

        @Override // org.telegram.ui.Components.RecyclerListView.SelectionAdapter
        public boolean isEnabled(RecyclerView.ViewHolder viewHolder) {
            return viewHolder.getItemViewType() == 0;
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public int getItemViewType(int i) {
            if (i == 0) {
                return 1;
            }
            return i == getItemCount() - 1 ? 2 : 0;
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public void notifyDataSetChanged() {
            super.notifyDataSetChanged();
            ChatAttachAlertContactsLayout.this.updateEmptyView();
        }
    }

    @Override // org.telegram.ui.Components.ChatAttachAlert.AttachAlertLayout
    public ArrayList<ThemeDescription> getThemeDescriptions() {
        ThemeDescription.ThemeDescriptionDelegate themeDescriptionDelegate = new ThemeDescription.ThemeDescriptionDelegate() { // from class: org.telegram.ui.Components.ChatAttachAlertContactsLayout$$ExternalSyntheticLambda0
            @Override // org.telegram.ui.ActionBar.ThemeDescription.ThemeDescriptionDelegate
            public final void didSetColor() {
                ChatAttachAlertContactsLayout.this.lambda$getThemeDescriptions$3();
            }

            @Override // org.telegram.ui.ActionBar.ThemeDescription.ThemeDescriptionDelegate
            public /* synthetic */ void onAnimationProgress(float f) {
                ThemeDescription.ThemeDescriptionDelegate.-CC.$default$onAnimationProgress(this, f);
            }
        };
        ArrayList<ThemeDescription> arrayList = new ArrayList<>();
        arrayList.add(new ThemeDescription(this.frameLayout, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, Theme.key_dialogBackground));
        arrayList.add(new ThemeDescription(this.shadow, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, Theme.key_dialogShadowLine));
        arrayList.add(new ThemeDescription(this.searchField.getSearchBackground(), ThemeDescription.FLAG_BACKGROUNDFILTER, null, null, null, null, Theme.key_dialogSearchBackground));
        int i = Theme.key_dialogSearchIcon;
        arrayList.add(new ThemeDescription(this.searchField, ThemeDescription.FLAG_IMAGECOLOR, new Class[]{SearchField.class}, new String[]{"searchIconImageView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, i));
        arrayList.add(new ThemeDescription(this.searchField, ThemeDescription.FLAG_IMAGECOLOR, new Class[]{SearchField.class}, new String[]{"clearSearchImageView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, i));
        arrayList.add(new ThemeDescription(this.searchField.getSearchEditText(), ThemeDescription.FLAG_TEXTCOLOR, null, null, null, null, Theme.key_dialogSearchText));
        arrayList.add(new ThemeDescription(this.searchField.getSearchEditText(), ThemeDescription.FLAG_HINTTEXTCOLOR, null, null, null, null, Theme.key_dialogSearchHint));
        arrayList.add(new ThemeDescription(this.searchField.getSearchEditText(), ThemeDescription.FLAG_CURSORCOLOR, null, null, null, null, Theme.key_featuredStickers_addedIcon));
        arrayList.add(new ThemeDescription(this.emptyView, ThemeDescription.FLAG_TEXTCOLOR, null, null, null, null, Theme.key_emptyListPlaceholder));
        arrayList.add(new ThemeDescription(this.emptyView, ThemeDescription.FLAG_PROGRESSBAR, null, null, null, null, Theme.key_progressCircle));
        arrayList.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_LISTGLOWCOLOR, null, null, null, null, Theme.key_dialogScrollGlow));
        arrayList.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_SELECTOR, null, null, null, null, Theme.key_listSelector));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{View.class}, Theme.dividerPaint, null, null, Theme.key_divider));
        int i2 = Theme.key_dialogTextGray2;
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{UserCell.class}, new String[]{"nameTextView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, i2));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{UserCell.class}, new String[]{"statusTextView"}, (Paint[]) null, (Drawable[]) null, themeDescriptionDelegate, i2));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{UserCell.class}, null, Theme.avatarDrawables, null, Theme.key_avatar_text));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, themeDescriptionDelegate, Theme.key_avatar_backgroundRed));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, themeDescriptionDelegate, Theme.key_avatar_backgroundOrange));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, themeDescriptionDelegate, Theme.key_avatar_backgroundViolet));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, themeDescriptionDelegate, Theme.key_avatar_backgroundGreen));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, themeDescriptionDelegate, Theme.key_avatar_backgroundCyan));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, themeDescriptionDelegate, Theme.key_avatar_backgroundBlue));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, themeDescriptionDelegate, Theme.key_avatar_backgroundPink));
        return arrayList;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$getThemeDescriptions$3() {
        RecyclerListView recyclerListView = this.listView;
        if (recyclerListView != null) {
            int childCount = recyclerListView.getChildCount();
            for (int i = 0; i < childCount; i++) {
                View childAt = this.listView.getChildAt(i);
                if (childAt instanceof UserCell) {
                    ((UserCell) childAt).update(0);
                }
            }
        }
    }
}
