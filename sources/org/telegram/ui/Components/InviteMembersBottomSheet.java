package org.telegram.ui.Components;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.StateListAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Outline;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.Property;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.ViewOutlineProvider;
import android.widget.ImageView;
import android.widget.ScrollView;
import androidx.collection.LongSparseArray;
import androidx.core.graphics.ColorUtils;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.HashMap;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.ApplicationLoader;
import org.telegram.messenger.ChatObject;
import org.telegram.messenger.ContactsController;
import org.telegram.messenger.DialogObject;
import org.telegram.messenger.DispatchQueue;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.R;
import org.telegram.messenger.UserObject;
import org.telegram.messenger.Utilities;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;
import org.telegram.ui.ActionBar.AlertDialog;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.ActionBar.BottomSheet;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Adapters.SearchAdapterHelper;
import org.telegram.ui.Cells.GroupCreateSectionCell;
import org.telegram.ui.Cells.GroupCreateUserCell;
import org.telegram.ui.Cells.ManageChatTextCell;
import org.telegram.ui.ChatActivity;
import org.telegram.ui.Components.InviteMembersBottomSheet;
import org.telegram.ui.Components.RecyclerListView;
import org.telegram.ui.Components.UsersAlertBase;
import org.telegram.ui.Components.spoilers.SpoilersTextView;
import org.telegram.ui.GroupCreateActivity;
import org.telegram.ui.LaunchActivity;

/* loaded from: classes3.dex */
public class InviteMembersBottomSheet extends UsersAlertBase implements NotificationCenter.NotificationCenterDelegate {
    private int additionalHeight;
    private long chatId;
    private ArrayList contacts;
    private int contactsEndRow;
    private int contactsStartRow;
    private int copyLinkRow;
    private AnimatorSet currentAnimation;
    private GroupCreateSpan currentDeletingSpan;
    private AnimatorSet currentDoneButtonAnimation;
    private GroupCreateActivity.ContactsAddActivityDelegate delegate;
    private InviteMembersBottomSheetDelegate dialogsDelegate;
    private ArrayList dialogsServerOnly;
    private int emptyRow;
    boolean enterEventSent;
    private final ImageView floatingButton;
    private LongSparseArray ignoreUsers;
    TLRPC.TL_chatInviteExported invite;
    private int lastRow;
    boolean linkGenerating;
    private int maxSize;
    private int noContactsStubRow;
    private BaseFragment parentFragment;
    private int rowCount;
    private int scrollViewH;
    private SearchAdapter searchAdapter;
    private int searchAdditionalHeight;
    private LongSparseArray selectedContacts;
    private View.OnClickListener spanClickListener;
    private boolean spanEnter;
    private final SpansContainer spansContainer;
    private ValueAnimator spansEnterAnimator;
    private float spansEnterProgress;
    private final ScrollView spansScrollView;
    private float touchSlop;
    float y;

    public interface InviteMembersBottomSheetDelegate {
        void didSelectDialogs(ArrayList arrayList);
    }

    private class ItemAnimator extends DefaultItemAnimator {
        public ItemAnimator() {
            this.translationInterpolator = CubicBezierInterpolator.DEFAULT;
            setMoveDuration(150L);
            setAddDuration(150L);
            setRemoveDuration(150L);
            InviteMembersBottomSheet.this.setShowWithoutAnimation(false);
        }
    }

    private class ListAdapter extends RecyclerListView.SelectionAdapter {
        private ListAdapter() {
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public int getItemCount() {
            return InviteMembersBottomSheet.this.rowCount;
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public int getItemViewType(int i) {
            if (i == InviteMembersBottomSheet.this.copyLinkRow) {
                return 1;
            }
            if (i == InviteMembersBottomSheet.this.emptyRow) {
                return 2;
            }
            if (i >= InviteMembersBottomSheet.this.contactsStartRow && i < InviteMembersBottomSheet.this.contactsEndRow) {
                return 3;
            }
            if (i == InviteMembersBottomSheet.this.lastRow) {
                return 4;
            }
            return i == InviteMembersBottomSheet.this.noContactsStubRow ? 5 : 0;
        }

        public TLObject getObject(int i) {
            if (InviteMembersBottomSheet.this.dialogsDelegate == null) {
                return (TLObject) InviteMembersBottomSheet.this.contacts.get(i - InviteMembersBottomSheet.this.contactsStartRow);
            }
            TLRPC.Dialog dialog = (TLRPC.Dialog) InviteMembersBottomSheet.this.dialogsServerOnly.get(i - InviteMembersBottomSheet.this.contactsStartRow);
            return DialogObject.isUserDialog(dialog.id) ? MessagesController.getInstance(((BottomSheet) InviteMembersBottomSheet.this).currentAccount).getUser(Long.valueOf(dialog.id)) : MessagesController.getInstance(((BottomSheet) InviteMembersBottomSheet.this).currentAccount).getChat(Long.valueOf(-dialog.id));
        }

        @Override // org.telegram.ui.Components.RecyclerListView.SelectionAdapter
        public boolean isEnabled(RecyclerView.ViewHolder viewHolder) {
            return viewHolder.getItemViewType() == 3 || viewHolder.getItemViewType() == 1;
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
            int itemViewType = viewHolder.getItemViewType();
            if (itemViewType == 2) {
                viewHolder.itemView.requestLayout();
                return;
            }
            if (itemViewType != 3) {
                return;
            }
            GroupCreateUserCell groupCreateUserCell = (GroupCreateUserCell) viewHolder.itemView;
            TLObject object = getObject(i);
            Object object2 = groupCreateUserCell.getObject();
            long j = object2 instanceof TLRPC.User ? ((TLRPC.User) object2).id : object2 instanceof TLRPC.Chat ? -((TLRPC.Chat) object2).id : 0L;
            groupCreateUserCell.setObject(object, null, null, i != InviteMembersBottomSheet.this.contactsEndRow);
            long j2 = object instanceof TLRPC.User ? ((TLRPC.User) object).id : object instanceof TLRPC.Chat ? -((TLRPC.Chat) object).id : 0L;
            if (j2 != 0) {
                if (InviteMembersBottomSheet.this.ignoreUsers == null || InviteMembersBottomSheet.this.ignoreUsers.indexOfKey(j2) < 0) {
                    groupCreateUserCell.setChecked(InviteMembersBottomSheet.this.selectedContacts.indexOfKey(j2) >= 0, j == j2);
                    groupCreateUserCell.setCheckBoxEnabled(true);
                } else {
                    groupCreateUserCell.setChecked(true, false);
                    groupCreateUserCell.setCheckBoxEnabled(false);
                }
            }
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View view;
            SpoilersTextView spoilersTextView;
            int i2;
            Context context = viewGroup.getContext();
            if (i != 2) {
                int i3 = 0;
                if (i == 3) {
                    view = new GroupCreateUserCell(context, 1, 0, InviteMembersBottomSheet.this.dialogsDelegate != null);
                } else if (i == 4) {
                    view = new View(context);
                } else if (i != 5) {
                    ManageChatTextCell manageChatTextCell = new ManageChatTextCell(context);
                    manageChatTextCell.setText(LocaleController.getString(R.string.VoipGroupCopyInviteLink), null, R.drawable.msg_link, 7, true);
                    int i4 = Theme.key_dialogTextBlue2;
                    manageChatTextCell.setColors(i4, i4);
                    view = manageChatTextCell;
                } else {
                    StickerEmptyView stickerEmptyView = new StickerEmptyView(context, null, i3) { // from class: org.telegram.ui.Components.InviteMembersBottomSheet.ListAdapter.2
                        @Override // org.telegram.ui.Components.StickerEmptyView, android.view.ViewGroup, android.view.View
                        protected void onAttachedToWindow() {
                            super.onAttachedToWindow();
                            this.stickerView.getImageReceiver().startAnimation();
                        }
                    };
                    stickerEmptyView.setLayoutParams(new RecyclerView.LayoutParams(-1, -1));
                    stickerEmptyView.subtitle.setVisibility(8);
                    if (InviteMembersBottomSheet.this.dialogsDelegate != null) {
                        spoilersTextView = stickerEmptyView.title;
                        i2 = R.string.FilterNoChats;
                    } else {
                        spoilersTextView = stickerEmptyView.title;
                        i2 = R.string.NoContacts;
                    }
                    spoilersTextView.setText(LocaleController.getString(i2));
                    stickerEmptyView.setAnimateLayoutChange(true);
                    view = stickerEmptyView;
                }
            } else {
                view = new View(context) { // from class: org.telegram.ui.Components.InviteMembersBottomSheet.ListAdapter.1
                    @Override // android.view.View
                    protected void onMeasure(int i5, int i6) {
                        super.onMeasure(i5, View.MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(48.0f) + InviteMembersBottomSheet.this.additionalHeight, 1073741824));
                    }
                };
            }
            return new RecyclerListView.Holder(view);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    class SearchAdapter extends RecyclerListView.SelectionAdapter {
        private int currentItemsCount;
        private final SearchAdapterHelper searchAdapterHelper;
        private ArrayList searchResult = new ArrayList();
        private ArrayList searchResultNames = new ArrayList();
        private Runnable searchRunnable;

        public SearchAdapter() {
            SearchAdapterHelper searchAdapterHelper = new SearchAdapterHelper(false);
            this.searchAdapterHelper = searchAdapterHelper;
            searchAdapterHelper.setDelegate(new SearchAdapterHelper.SearchAdapterHelperDelegate() { // from class: org.telegram.ui.Components.InviteMembersBottomSheet$SearchAdapter$$ExternalSyntheticLambda0
                @Override // org.telegram.ui.Adapters.SearchAdapterHelper.SearchAdapterHelperDelegate
                public /* synthetic */ boolean canApplySearchResults(int i) {
                    return SearchAdapterHelper.SearchAdapterHelperDelegate.-CC.$default$canApplySearchResults(this, i);
                }

                @Override // org.telegram.ui.Adapters.SearchAdapterHelper.SearchAdapterHelperDelegate
                public /* synthetic */ LongSparseArray getExcludeCallParticipants() {
                    return SearchAdapterHelper.SearchAdapterHelperDelegate.-CC.$default$getExcludeCallParticipants(this);
                }

                @Override // org.telegram.ui.Adapters.SearchAdapterHelper.SearchAdapterHelperDelegate
                public /* synthetic */ LongSparseArray getExcludeUsers() {
                    return SearchAdapterHelper.SearchAdapterHelperDelegate.-CC.$default$getExcludeUsers(this);
                }

                @Override // org.telegram.ui.Adapters.SearchAdapterHelper.SearchAdapterHelperDelegate
                public final void onDataSetChanged(int i) {
                    InviteMembersBottomSheet.SearchAdapter.this.lambda$new$0(i);
                }

                @Override // org.telegram.ui.Adapters.SearchAdapterHelper.SearchAdapterHelperDelegate
                public /* synthetic */ void onSetHashtags(ArrayList arrayList, HashMap hashMap) {
                    SearchAdapterHelper.SearchAdapterHelperDelegate.-CC.$default$onSetHashtags(this, arrayList, hashMap);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$new$0(int i) {
            InviteMembersBottomSheet.this.showItemsAnimated(this.currentItemsCount - 1);
            if (this.searchRunnable == null && !this.searchAdapterHelper.isSearchInProgress() && getItemCount() <= 2) {
                InviteMembersBottomSheet.this.emptyView.showProgress(false, true);
            }
            notifyDataSetChanged();
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* JADX WARN: Code restructure failed: missing block: B:35:0x00d4, code lost:
        
            if (r13.contains(" " + r3) != false) goto L44;
         */
        /* JADX WARN: Removed duplicated region for block: B:41:0x0131 A[LOOP:1: B:26:0x0098->B:41:0x0131, LOOP_END] */
        /* JADX WARN: Removed duplicated region for block: B:42:0x00e4 A[SYNTHETIC] */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public /* synthetic */ void lambda$searchDialogs$2(String str) {
            String str2;
            String publicUsername;
            Object obj;
            CharSequence generateSearchName;
            String lowerCase = str.trim().toLowerCase();
            if (lowerCase.length() == 0) {
                updateSearchResults(new ArrayList(), new ArrayList());
                return;
            }
            String translitString = LocaleController.getInstance().getTranslitString(lowerCase);
            if (lowerCase.equals(translitString) || translitString.length() == 0) {
                translitString = null;
            }
            int i = (translitString != null ? 1 : 0) + 1;
            String[] strArr = new String[i];
            strArr[0] = lowerCase;
            if (translitString != null) {
                strArr[1] = translitString;
            }
            ArrayList arrayList = new ArrayList();
            ArrayList arrayList2 = new ArrayList();
            for (int i2 = 0; i2 < InviteMembersBottomSheet.this.contacts.size(); i2++) {
                TLObject tLObject = (TLObject) InviteMembersBottomSheet.this.contacts.get(i2);
                boolean z = tLObject instanceof TLRPC.User;
                if (z) {
                    TLRPC.User user = (TLRPC.User) tLObject;
                    str2 = ContactsController.formatName(user.first_name, user.last_name).toLowerCase();
                    publicUsername = UserObject.getPublicUsername(user);
                } else {
                    TLRPC.Chat chat = (TLRPC.Chat) tLObject;
                    str2 = chat.title;
                    publicUsername = ChatObject.getPublicUsername(chat);
                }
                String translitString2 = LocaleController.getInstance().getTranslitString(str2);
                if (str2.equals(translitString2)) {
                    translitString2 = null;
                }
                int i3 = 0;
                char c = 0;
                while (true) {
                    if (i3 < i) {
                        String str3 = strArr[i3];
                        if (!str2.startsWith(str3)) {
                            if (!str2.contains(" " + str3)) {
                                if (translitString2 != null) {
                                    if (!translitString2.startsWith(str3)) {
                                    }
                                }
                                if (publicUsername != null && publicUsername.startsWith(str3)) {
                                    c = 2;
                                }
                                if (c == 0) {
                                    if (c == 1) {
                                        if (z) {
                                            TLRPC.User user2 = (TLRPC.User) tLObject;
                                            generateSearchName = AndroidUtilities.generateSearchName(user2.first_name, user2.last_name, str3);
                                        } else {
                                            generateSearchName = AndroidUtilities.generateSearchName(((TLRPC.Chat) tLObject).title, null, str3);
                                        }
                                        arrayList2.add(generateSearchName);
                                        obj = null;
                                    } else {
                                        obj = null;
                                        arrayList2.add(AndroidUtilities.generateSearchName("@" + publicUsername, null, "@" + str3));
                                    }
                                    arrayList.add(tLObject);
                                } else {
                                    i3++;
                                }
                            }
                        }
                        c = 1;
                        if (c == 0) {
                        }
                    }
                }
            }
            updateSearchResults(arrayList, arrayList2);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$searchDialogs$3(final String str) {
            this.searchAdapterHelper.queryServerSearch(str, true, InviteMembersBottomSheet.this.dialogsDelegate != null, true, InviteMembersBottomSheet.this.dialogsDelegate != null, false, 0L, false, 0, 0);
            DispatchQueue dispatchQueue = Utilities.searchQueue;
            Runnable runnable = new Runnable() { // from class: org.telegram.ui.Components.InviteMembersBottomSheet$SearchAdapter$$ExternalSyntheticLambda3
                @Override // java.lang.Runnable
                public final void run() {
                    InviteMembersBottomSheet.SearchAdapter.this.lambda$searchDialogs$2(str);
                }
            };
            this.searchRunnable = runnable;
            dispatchQueue.postRunnable(runnable);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$searchDialogs$4(final String str) {
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.Components.InviteMembersBottomSheet$SearchAdapter$$ExternalSyntheticLambda2
                @Override // java.lang.Runnable
                public final void run() {
                    InviteMembersBottomSheet.SearchAdapter.this.lambda$searchDialogs$3(str);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$updateSearchResults$1(ArrayList arrayList, ArrayList arrayList2) {
            this.searchRunnable = null;
            this.searchResult = arrayList;
            this.searchResultNames = arrayList2;
            this.searchAdapterHelper.mergeResults(arrayList);
            InviteMembersBottomSheet.this.showItemsAnimated(this.currentItemsCount - 1);
            notifyDataSetChanged();
            if (this.searchAdapterHelper.isSearchInProgress() || getItemCount() > 2) {
                return;
            }
            InviteMembersBottomSheet.this.emptyView.showProgress(false, true);
        }

        private void updateSearchResults(final ArrayList arrayList, final ArrayList arrayList2) {
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.Components.InviteMembersBottomSheet$SearchAdapter$$ExternalSyntheticLambda4
                @Override // java.lang.Runnable
                public final void run() {
                    InviteMembersBottomSheet.SearchAdapter.this.lambda$updateSearchResults$1(arrayList, arrayList2);
                }
            });
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public int getItemCount() {
            int size = this.searchResult.size();
            int size2 = this.searchAdapterHelper.getLocalServerSearch().size();
            int size3 = this.searchAdapterHelper.getGlobalSearch().size();
            int i = size + size2;
            if (size3 != 0) {
                i += size3 + 1;
            }
            int i2 = i + 2;
            this.currentItemsCount = i2;
            return i2;
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public int getItemViewType(int i) {
            if (i == 0) {
                return 2;
            }
            if (i == this.currentItemsCount - 1) {
                return 4;
            }
            return i + (-1) == this.searchResult.size() + this.searchAdapterHelper.getLocalServerSearch().size() ? 0 : 1;
        }

        @Override // org.telegram.ui.Components.RecyclerListView.SelectionAdapter
        public boolean isEnabled(RecyclerView.ViewHolder viewHolder) {
            return viewHolder.getItemViewType() == 1;
        }

        /* JADX WARN: Code restructure failed: missing block: B:27:0x00ac, code lost:
        
            if (r13.toString().startsWith("@" + r3) != false) goto L58;
         */
        /* JADX WARN: Removed duplicated region for block: B:17:0x0070  */
        /* JADX WARN: Removed duplicated region for block: B:30:0x010a  */
        /* JADX WARN: Removed duplicated region for block: B:33:0x0121  */
        /* JADX WARN: Removed duplicated region for block: B:36:0x0135  */
        /* JADX WARN: Removed duplicated region for block: B:55:? A[RETURN, SYNTHETIC] */
        /* JADX WARN: Removed duplicated region for block: B:56:0x0126  */
        /* JADX WARN: Removed duplicated region for block: B:60:0x010f  */
        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
            TLObject tLObject;
            Object obj;
            CharSequence charSequence;
            long j;
            int itemViewType = viewHolder.getItemViewType();
            if (itemViewType == 0) {
                ((GroupCreateSectionCell) viewHolder.itemView).setText(LocaleController.getString(R.string.GlobalSearch));
                return;
            }
            if (itemViewType != 1) {
                if (itemViewType != 2) {
                    return;
                }
                viewHolder.itemView.requestLayout();
                return;
            }
            GroupCreateUserCell groupCreateUserCell = (GroupCreateUserCell) viewHolder.itemView;
            int size = this.searchResult.size();
            int size2 = this.searchAdapterHelper.getGlobalSearch().size();
            int size3 = this.searchAdapterHelper.getLocalServerSearch().size();
            int i2 = i - 1;
            CharSequence charSequence2 = null;
            if (i2 >= 0 && i2 < size) {
                obj = this.searchResult.get(i2);
            } else if (i2 >= size && i2 < size3 + size) {
                obj = this.searchAdapterHelper.getLocalServerSearch().get(i2 - size);
            } else if (i2 <= size + size3 || i2 > size2 + size + size3) {
                tLObject = null;
                if (tLObject != null) {
                    String publicUsername = tLObject instanceof TLRPC.User ? ((TLRPC.User) tLObject).username : ChatObject.getPublicUsername((TLRPC.Chat) tLObject);
                    if (i2 < size) {
                        charSequence = (CharSequence) this.searchResultNames.get(i2);
                        if (charSequence != null && !TextUtils.isEmpty(publicUsername)) {
                        }
                        charSequence2 = charSequence;
                        charSequence = null;
                    } else if (i2 > size && !TextUtils.isEmpty(publicUsername)) {
                        String lastFoundUsername = this.searchAdapterHelper.getLastFoundUsername();
                        if (lastFoundUsername.startsWith("@")) {
                            lastFoundUsername = lastFoundUsername.substring(1);
                        }
                        try {
                            SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder();
                            spannableStringBuilder.append((CharSequence) "@");
                            spannableStringBuilder.append((CharSequence) publicUsername);
                            int indexOfIgnoreCase = AndroidUtilities.indexOfIgnoreCase(publicUsername, lastFoundUsername);
                            if (indexOfIgnoreCase != -1) {
                                int length = lastFoundUsername.length();
                                if (indexOfIgnoreCase == 0) {
                                    length++;
                                } else {
                                    indexOfIgnoreCase++;
                                }
                                spannableStringBuilder.setSpan(new ForegroundColorSpan(Theme.getColor(Theme.key_windowBackgroundWhiteBlueText4)), indexOfIgnoreCase, length + indexOfIgnoreCase, 33);
                            }
                            charSequence = spannableStringBuilder;
                        } catch (Exception unused) {
                            charSequence = publicUsername;
                        }
                    }
                    Object object = groupCreateUserCell.getObject();
                    long j2 = object instanceof TLRPC.User ? ((TLRPC.User) object).id : object instanceof TLRPC.Chat ? -((TLRPC.Chat) object).id : 0L;
                    groupCreateUserCell.setObject(tLObject, charSequence2, charSequence);
                    j = tLObject instanceof TLRPC.User ? ((TLRPC.User) tLObject).id : tLObject instanceof TLRPC.Chat ? -((TLRPC.Chat) tLObject).id : 0L;
                    if (j != 0) {
                        if (InviteMembersBottomSheet.this.ignoreUsers == null || InviteMembersBottomSheet.this.ignoreUsers.indexOfKey(j) < 0) {
                            groupCreateUserCell.setChecked(InviteMembersBottomSheet.this.selectedContacts.indexOfKey(j) >= 0, j2 == j);
                            groupCreateUserCell.setCheckBoxEnabled(true);
                            return;
                        } else {
                            groupCreateUserCell.setChecked(true, j2 == j);
                            groupCreateUserCell.setCheckBoxEnabled(false);
                            return;
                        }
                    }
                    return;
                }
                charSequence = null;
                Object object2 = groupCreateUserCell.getObject();
                if (object2 instanceof TLRPC.User) {
                }
                groupCreateUserCell.setObject(tLObject, charSequence2, charSequence);
                if (tLObject instanceof TLRPC.User) {
                }
                if (j != 0) {
                }
            } else {
                obj = this.searchAdapterHelper.getGlobalSearch().get(((i2 - size) - size3) - 1);
            }
            tLObject = (TLObject) obj;
            if (tLObject != null) {
            }
            charSequence = null;
            Object object22 = groupCreateUserCell.getObject();
            if (object22 instanceof TLRPC.User) {
            }
            groupCreateUserCell.setObject(tLObject, charSequence2, charSequence);
            if (tLObject instanceof TLRPC.User) {
            }
            if (j != 0) {
            }
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            Context context = viewGroup.getContext();
            return new RecyclerListView.Holder(i != 1 ? i != 2 ? i != 4 ? new GroupCreateSectionCell(context) : new View(context) : new View(context) { // from class: org.telegram.ui.Components.InviteMembersBottomSheet.SearchAdapter.1
                @Override // android.view.View
                protected void onMeasure(int i2, int i3) {
                    super.onMeasure(i2, View.MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(48.0f) + InviteMembersBottomSheet.this.additionalHeight + InviteMembersBottomSheet.this.searchAdditionalHeight, 1073741824));
                }
            } : new GroupCreateUserCell(context, 1, 0, false));
        }

        public void searchDialogs(final String str) {
            if (this.searchRunnable != null) {
                Utilities.searchQueue.cancelRunnable(this.searchRunnable);
                this.searchRunnable = null;
            }
            this.searchResult.clear();
            this.searchResultNames.clear();
            this.searchAdapterHelper.mergeResults(null);
            this.searchAdapterHelper.queryServerSearch(null, true, false, false, false, false, 0L, false, 0, 0);
            notifyDataSetChanged();
            if (TextUtils.isEmpty(str)) {
                RecyclerView.Adapter adapter = InviteMembersBottomSheet.this.listView.getAdapter();
                InviteMembersBottomSheet inviteMembersBottomSheet = InviteMembersBottomSheet.this;
                RecyclerView.Adapter adapter2 = inviteMembersBottomSheet.listViewAdapter;
                if (adapter != adapter2) {
                    inviteMembersBottomSheet.listView.setAdapter(adapter2);
                    return;
                }
                return;
            }
            RecyclerView.Adapter adapter3 = InviteMembersBottomSheet.this.listView.getAdapter();
            InviteMembersBottomSheet inviteMembersBottomSheet2 = InviteMembersBottomSheet.this;
            RecyclerView.Adapter adapter4 = inviteMembersBottomSheet2.searchListViewAdapter;
            if (adapter3 != adapter4) {
                inviteMembersBottomSheet2.listView.setAdapter(adapter4);
            }
            InviteMembersBottomSheet.this.emptyView.showProgress(true, false);
            DispatchQueue dispatchQueue = Utilities.searchQueue;
            Runnable runnable = new Runnable() { // from class: org.telegram.ui.Components.InviteMembersBottomSheet$SearchAdapter$$ExternalSyntheticLambda1
                @Override // java.lang.Runnable
                public final void run() {
                    InviteMembersBottomSheet.SearchAdapter.this.lambda$searchDialogs$4(str);
                }
            };
            this.searchRunnable = runnable;
            dispatchQueue.postRunnable(runnable, 300L);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    class SpansContainer extends ViewGroup {
        boolean addAnimation;
        private int animationIndex;
        private boolean animationStarted;
        private ArrayList animators;
        private View removingSpan;

        public SpansContainer(Context context) {
            super(context);
            this.animators = new ArrayList();
            this.animationIndex = -1;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onMeasure$0(ValueAnimator valueAnimator) {
            InviteMembersBottomSheet.this.scrollViewH = ((Integer) valueAnimator.getAnimatedValue()).intValue();
            ((BottomSheet) InviteMembersBottomSheet.this).containerView.invalidate();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onMeasure$1(int i) {
            InviteMembersBottomSheet.this.spansScrollView.smoothScrollTo(0, i - InviteMembersBottomSheet.this.maxSize);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onMeasure$2(int i) {
            InviteMembersBottomSheet.this.spansScrollView.smoothScrollTo(0, i - InviteMembersBottomSheet.this.maxSize);
        }

        public void addSpan(GroupCreateSpan groupCreateSpan, boolean z) {
            this.addAnimation = true;
            InviteMembersBottomSheet.this.selectedContacts.put(groupCreateSpan.getUid(), groupCreateSpan);
            if (InviteMembersBottomSheet.this.currentAnimation != null) {
                InviteMembersBottomSheet.this.currentAnimation.setupEndValues();
                InviteMembersBottomSheet.this.currentAnimation.cancel();
            }
            this.animationStarted = false;
            if (z) {
                InviteMembersBottomSheet.this.currentAnimation = new AnimatorSet();
                InviteMembersBottomSheet.this.currentAnimation.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.Components.InviteMembersBottomSheet.SpansContainer.3
                    @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                    public void onAnimationEnd(Animator animator) {
                        InviteMembersBottomSheet.this.currentAnimation = null;
                        SpansContainer.this.animationStarted = false;
                    }
                });
                InviteMembersBottomSheet.this.currentAnimation.setDuration(150L);
                InviteMembersBottomSheet.this.currentAnimation.setInterpolator(CubicBezierInterpolator.DEFAULT);
                this.animators.clear();
                this.animators.add(ObjectAnimator.ofFloat(groupCreateSpan, (Property<GroupCreateSpan, Float>) View.SCALE_X, 0.01f, 1.0f));
                this.animators.add(ObjectAnimator.ofFloat(groupCreateSpan, (Property<GroupCreateSpan, Float>) View.SCALE_Y, 0.01f, 1.0f));
                this.animators.add(ObjectAnimator.ofFloat(groupCreateSpan, (Property<GroupCreateSpan, Float>) View.ALPHA, 0.0f, 1.0f));
            }
            addView(groupCreateSpan);
        }

        @Override // android.view.ViewGroup, android.view.View
        protected void onLayout(boolean z, int i, int i2, int i3, int i4) {
            int childCount = getChildCount();
            for (int i5 = 0; i5 < childCount; i5++) {
                View childAt = getChildAt(i5);
                childAt.layout(0, 0, childAt.getMeasuredWidth(), childAt.getMeasuredHeight());
            }
        }

        /* JADX WARN: Removed duplicated region for block: B:65:0x0263  */
        @Override // android.view.View
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        protected void onMeasure(int i, int i2) {
            RecyclerView.ViewHolder findViewHolderForAdapterPosition;
            Runnable runnable;
            float f;
            int childCount = getChildCount();
            int size = View.MeasureSpec.getSize(i);
            int dp = size - AndroidUtilities.dp(26.0f);
            int dp2 = AndroidUtilities.dp(10.0f);
            int dp3 = AndroidUtilities.dp(10.0f);
            int i3 = 0;
            int i4 = 0;
            for (int i5 = 0; i5 < childCount; i5++) {
                View childAt = getChildAt(i5);
                if (childAt instanceof GroupCreateSpan) {
                    childAt.measure(View.MeasureSpec.makeMeasureSpec(size, Integer.MIN_VALUE), View.MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(32.0f), 1073741824));
                    if (childAt != this.removingSpan && childAt.getMeasuredWidth() + i3 > dp) {
                        dp2 += childAt.getMeasuredHeight() + AndroidUtilities.dp(8.0f);
                        i3 = 0;
                    }
                    if (childAt.getMeasuredWidth() + i4 > dp) {
                        dp3 += childAt.getMeasuredHeight() + AndroidUtilities.dp(8.0f);
                        i4 = 0;
                    }
                    int dp4 = AndroidUtilities.dp(13.0f) + i3;
                    if (!this.animationStarted) {
                        View view = this.removingSpan;
                        if (childAt == view) {
                            childAt.setTranslationX(AndroidUtilities.dp(13.0f) + i4);
                            f = dp3;
                        } else if (view != null) {
                            float f2 = dp4;
                            if (childAt.getTranslationX() != f2) {
                                this.animators.add(ObjectAnimator.ofFloat(childAt, (Property<View, Float>) View.TRANSLATION_X, f2));
                            }
                            float f3 = dp2;
                            if (childAt.getTranslationY() != f3) {
                                this.animators.add(ObjectAnimator.ofFloat(childAt, (Property<View, Float>) View.TRANSLATION_Y, f3));
                            }
                        } else {
                            childAt.setTranslationX(dp4);
                            f = dp2;
                        }
                        childAt.setTranslationY(f);
                    }
                    if (childAt != this.removingSpan) {
                        i3 += childAt.getMeasuredWidth() + AndroidUtilities.dp(9.0f);
                    }
                    i4 += childAt.getMeasuredWidth() + AndroidUtilities.dp(9.0f);
                }
            }
            int dp5 = dp3 + AndroidUtilities.dp(42.0f);
            final int dp6 = dp2 + AndroidUtilities.dp(42.0f);
            int min = InviteMembersBottomSheet.this.dialogsDelegate != null ? InviteMembersBottomSheet.this.spanEnter ? Math.min(InviteMembersBottomSheet.this.maxSize, dp6) : 0 : Math.max(0, Math.min(InviteMembersBottomSheet.this.maxSize, dp6) - AndroidUtilities.dp(52.0f));
            int i6 = InviteMembersBottomSheet.this.searchAdditionalHeight;
            InviteMembersBottomSheet inviteMembersBottomSheet = InviteMembersBottomSheet.this;
            inviteMembersBottomSheet.searchAdditionalHeight = (inviteMembersBottomSheet.dialogsDelegate != null || InviteMembersBottomSheet.this.selectedContacts.size() <= 0) ? 0 : AndroidUtilities.dp(56.0f);
            if (min != InviteMembersBottomSheet.this.additionalHeight || i6 != InviteMembersBottomSheet.this.searchAdditionalHeight) {
                InviteMembersBottomSheet.this.additionalHeight = min;
                if (InviteMembersBottomSheet.this.listView.getAdapter() != null && InviteMembersBottomSheet.this.listView.getAdapter().getItemCount() > 0 && (findViewHolderForAdapterPosition = InviteMembersBottomSheet.this.listView.findViewHolderForAdapterPosition(0)) != null) {
                    InviteMembersBottomSheet.this.listView.getAdapter().notifyItemChanged(0);
                    InviteMembersBottomSheet.this.layoutManager.scrollToPositionWithOffset(0, findViewHolderForAdapterPosition.itemView.getTop() - InviteMembersBottomSheet.this.listView.getPaddingTop());
                    if (InviteMembersBottomSheet.this.listView.getItemAnimator() != null) {
                        ValueAnimator ofFloat = ValueAnimator.ofFloat(0.0f, 1.0f);
                        ofFloat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: org.telegram.ui.Components.InviteMembersBottomSheet.SpansContainer.1
                            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                                InviteMembersBottomSheet.this.listView.updateSelector();
                            }
                        });
                        ofFloat.setDuration(InviteMembersBottomSheet.this.listView.getItemAnimator().getChangeDuration()).start();
                    }
                }
            }
            int min2 = Math.min(InviteMembersBottomSheet.this.maxSize, dp6);
            if (InviteMembersBottomSheet.this.scrollViewH != min2) {
                ValueAnimator ofInt = ValueAnimator.ofInt(InviteMembersBottomSheet.this.scrollViewH, min2);
                ofInt.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: org.telegram.ui.Components.InviteMembersBottomSheet$SpansContainer$$ExternalSyntheticLambda0
                    @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                    public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                        InviteMembersBottomSheet.SpansContainer.this.lambda$onMeasure$0(valueAnimator);
                    }
                });
                this.animators.add(ofInt);
            }
            if (!this.addAnimation || dp6 <= InviteMembersBottomSheet.this.maxSize) {
                if (!this.addAnimation && InviteMembersBottomSheet.this.spansScrollView.getScrollY() + InviteMembersBottomSheet.this.spansScrollView.getMeasuredHeight() > dp6) {
                    runnable = new Runnable() { // from class: org.telegram.ui.Components.InviteMembersBottomSheet$SpansContainer$$ExternalSyntheticLambda2
                        @Override // java.lang.Runnable
                        public final void run() {
                            InviteMembersBottomSheet.SpansContainer.this.lambda$onMeasure$2(dp6);
                        }
                    };
                }
                if (!this.animationStarted && InviteMembersBottomSheet.this.currentAnimation != null) {
                    InviteMembersBottomSheet.this.currentAnimation.playTogether(this.animators);
                    InviteMembersBottomSheet.this.currentAnimation.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.Components.InviteMembersBottomSheet.SpansContainer.2
                        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                        public void onAnimationEnd(Animator animator) {
                            InviteMembersBottomSheet.this.currentAnimation = null;
                            SpansContainer.this.requestLayout();
                        }
                    });
                    InviteMembersBottomSheet.this.currentAnimation.start();
                    this.animationStarted = true;
                }
                if (InviteMembersBottomSheet.this.currentAnimation == null) {
                    InviteMembersBottomSheet.this.scrollViewH = min2;
                    ((BottomSheet) InviteMembersBottomSheet.this).containerView.invalidate();
                }
                setMeasuredDimension(size, Math.max(dp6, dp5));
                InviteMembersBottomSheet.this.listView.setTranslationY(0.0f);
            }
            runnable = new Runnable() { // from class: org.telegram.ui.Components.InviteMembersBottomSheet$SpansContainer$$ExternalSyntheticLambda1
                @Override // java.lang.Runnable
                public final void run() {
                    InviteMembersBottomSheet.SpansContainer.this.lambda$onMeasure$1(dp6);
                }
            };
            AndroidUtilities.runOnUIThread(runnable);
            if (!this.animationStarted) {
                InviteMembersBottomSheet.this.currentAnimation.playTogether(this.animators);
                InviteMembersBottomSheet.this.currentAnimation.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.Components.InviteMembersBottomSheet.SpansContainer.2
                    @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                    public void onAnimationEnd(Animator animator) {
                        InviteMembersBottomSheet.this.currentAnimation = null;
                        SpansContainer.this.requestLayout();
                    }
                });
                InviteMembersBottomSheet.this.currentAnimation.start();
                this.animationStarted = true;
            }
            if (InviteMembersBottomSheet.this.currentAnimation == null) {
            }
            setMeasuredDimension(size, Math.max(dp6, dp5));
            InviteMembersBottomSheet.this.listView.setTranslationY(0.0f);
        }

        public void removeSpan(final GroupCreateSpan groupCreateSpan) {
            this.addAnimation = false;
            InviteMembersBottomSheet.this.selectedContacts.remove(groupCreateSpan.getUid());
            groupCreateSpan.setOnClickListener(null);
            if (InviteMembersBottomSheet.this.currentAnimation != null) {
                InviteMembersBottomSheet.this.currentAnimation.setupEndValues();
                InviteMembersBottomSheet.this.currentAnimation.cancel();
            }
            this.animationStarted = false;
            InviteMembersBottomSheet.this.currentAnimation = new AnimatorSet();
            InviteMembersBottomSheet.this.currentAnimation.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.Components.InviteMembersBottomSheet.SpansContainer.4
                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public void onAnimationEnd(Animator animator) {
                    SpansContainer.this.removeView(groupCreateSpan);
                    SpansContainer.this.removingSpan = null;
                    InviteMembersBottomSheet.this.currentAnimation = null;
                    SpansContainer.this.animationStarted = false;
                }
            });
            InviteMembersBottomSheet.this.currentAnimation.setDuration(150L);
            this.removingSpan = groupCreateSpan;
            this.animators.clear();
            this.animators.add(ObjectAnimator.ofFloat(this.removingSpan, (Property<View, Float>) View.SCALE_X, 1.0f, 0.01f));
            this.animators.add(ObjectAnimator.ofFloat(this.removingSpan, (Property<View, Float>) View.SCALE_Y, 1.0f, 0.01f));
            this.animators.add(ObjectAnimator.ofFloat(this.removingSpan, (Property<View, Float>) View.ALPHA, 1.0f, 0.0f));
            requestLayout();
        }
    }

    public InviteMembersBottomSheet(final Context context, int i, final LongSparseArray longSparseArray, final long j, final BaseFragment baseFragment, Theme.ResourcesProvider resourcesProvider) {
        super(context, false, i, resourcesProvider);
        this.contacts = new ArrayList();
        this.selectedContacts = new LongSparseArray();
        this.spansEnterProgress = 0.0f;
        this.spanClickListener = new View.OnClickListener() { // from class: org.telegram.ui.Components.InviteMembersBottomSheet.1
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                GroupCreateSpan groupCreateSpan = (GroupCreateSpan) view;
                if (!groupCreateSpan.isDeleting()) {
                    if (InviteMembersBottomSheet.this.currentDeletingSpan != null) {
                        InviteMembersBottomSheet.this.currentDeletingSpan.cancelDeleteAnimation();
                    }
                    InviteMembersBottomSheet.this.currentDeletingSpan = groupCreateSpan;
                    groupCreateSpan.startDeleteAnimation();
                    return;
                }
                InviteMembersBottomSheet.this.currentDeletingSpan = null;
                InviteMembersBottomSheet.this.selectedContacts.remove(groupCreateSpan.getUid());
                InviteMembersBottomSheet.this.spansContainer.removeSpan(groupCreateSpan);
                InviteMembersBottomSheet.this.spansCountChanged(true);
                AndroidUtilities.updateVisibleRows(InviteMembersBottomSheet.this.listView);
            }
        };
        this.ignoreUsers = longSparseArray;
        this.needSnapToTop = false;
        this.parentFragment = baseFragment;
        this.chatId = j;
        fixNavigationBar();
        this.searchView.searchEditText.setHint(LocaleController.getString(R.string.SearchForChats));
        this.touchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        SearchAdapter searchAdapter = new SearchAdapter();
        this.searchAdapter = searchAdapter;
        this.searchListViewAdapter = searchAdapter;
        RecyclerListView recyclerListView = this.listView;
        ListAdapter listAdapter = new ListAdapter();
        this.listViewAdapter = listAdapter;
        recyclerListView.setAdapter(listAdapter);
        this.emptyView.showProgress(false, false);
        this.emptyView.setVisibility(8);
        ArrayList<TLRPC.TL_contact> arrayList = ContactsController.getInstance(i).contacts;
        for (int i2 = 0; i2 < arrayList.size(); i2++) {
            TLRPC.User user = MessagesController.getInstance(this.currentAccount).getUser(Long.valueOf(arrayList.get(i2).user_id));
            if (user != null && !user.self && !user.deleted) {
                this.contacts.add(user);
            }
        }
        SpansContainer spansContainer = new SpansContainer(context);
        this.spansContainer = spansContainer;
        this.listView.setOnItemClickListener(new RecyclerListView.OnItemClickListener() { // from class: org.telegram.ui.Components.InviteMembersBottomSheet$$ExternalSyntheticLambda3
            @Override // org.telegram.ui.Components.RecyclerListView.OnItemClickListener
            public final void onItemClick(View view, int i3) {
                InviteMembersBottomSheet.this.lambda$new$0(j, baseFragment, longSparseArray, context, view, i3);
            }
        });
        this.listView.setItemAnimator(new ItemAnimator());
        updateRows();
        ScrollView scrollView = new ScrollView(context) { // from class: org.telegram.ui.Components.InviteMembersBottomSheet.2
            @Override // android.widget.ScrollView, android.widget.FrameLayout, android.view.View
            protected void onMeasure(int i3, int i4) {
                InviteMembersBottomSheet inviteMembersBottomSheet;
                float f;
                int size = View.MeasureSpec.getSize(i3);
                int size2 = View.MeasureSpec.getSize(i4);
                if (AndroidUtilities.isTablet() || size2 > size) {
                    inviteMembersBottomSheet = InviteMembersBottomSheet.this;
                    f = 144.0f;
                } else {
                    inviteMembersBottomSheet = InviteMembersBottomSheet.this;
                    f = 56.0f;
                }
                inviteMembersBottomSheet.maxSize = AndroidUtilities.dp(f);
                super.onMeasure(i3, View.MeasureSpec.makeMeasureSpec(InviteMembersBottomSheet.this.maxSize, Integer.MIN_VALUE));
            }
        };
        this.spansScrollView = scrollView;
        scrollView.setVisibility(8);
        scrollView.setClipChildren(false);
        scrollView.addView(spansContainer);
        this.containerView.addView(scrollView);
        ImageView imageView = new ImageView(context);
        this.floatingButton = imageView;
        imageView.setScaleType(ImageView.ScaleType.CENTER);
        Drawable createSimpleSelectorCircleDrawable = Theme.createSimpleSelectorCircleDrawable(AndroidUtilities.dp(56.0f), Theme.getColor(Theme.key_chats_actionBackground), Theme.getColor(Theme.key_chats_actionPressedBackground));
        int i3 = Build.VERSION.SDK_INT;
        if (i3 < 21) {
            Drawable mutate = context.getResources().getDrawable(R.drawable.floating_shadow).mutate();
            mutate.setColorFilter(new PorterDuffColorFilter(-16777216, PorterDuff.Mode.MULTIPLY));
            CombinedDrawable combinedDrawable = new CombinedDrawable(mutate, createSimpleSelectorCircleDrawable, 0, 0);
            combinedDrawable.setIconSize(AndroidUtilities.dp(56.0f), AndroidUtilities.dp(56.0f));
            createSimpleSelectorCircleDrawable = combinedDrawable;
        }
        imageView.setBackgroundDrawable(createSimpleSelectorCircleDrawable);
        imageView.setColorFilter(new PorterDuffColorFilter(Theme.getColor(Theme.key_chats_actionIcon), PorterDuff.Mode.MULTIPLY));
        imageView.setImageResource(R.drawable.floating_check);
        if (i3 >= 21) {
            StateListAnimator stateListAnimator = new StateListAnimator();
            stateListAnimator.addState(new int[]{android.R.attr.state_pressed}, ObjectAnimator.ofFloat(imageView, "translationZ", AndroidUtilities.dp(2.0f), AndroidUtilities.dp(4.0f)).setDuration(200L));
            stateListAnimator.addState(new int[0], ObjectAnimator.ofFloat(imageView, "translationZ", AndroidUtilities.dp(4.0f), AndroidUtilities.dp(2.0f)).setDuration(200L));
            imageView.setStateListAnimator(stateListAnimator);
            imageView.setOutlineProvider(new ViewOutlineProvider() { // from class: org.telegram.ui.Components.InviteMembersBottomSheet.3
                @Override // android.view.ViewOutlineProvider
                public void getOutline(View view, Outline outline) {
                    outline.setOval(0, 0, AndroidUtilities.dp(56.0f), AndroidUtilities.dp(56.0f));
                }
            });
        }
        imageView.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Components.InviteMembersBottomSheet$$ExternalSyntheticLambda4
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                InviteMembersBottomSheet.this.lambda$new$2(context, j, view);
            }
        });
        imageView.setVisibility(4);
        imageView.setScaleX(0.0f);
        imageView.setScaleY(0.0f);
        imageView.setAlpha(0.0f);
        imageView.setContentDescription(LocaleController.getString(R.string.Next));
        this.containerView.addView(imageView, LayoutHelper.createFrame(i3 >= 21 ? 56 : 60, i3 >= 21 ? 56 : 60, 85, 14.0f, 14.0f, 14.0f, 14.0f));
        ((ViewGroup.MarginLayoutParams) this.emptyView.getLayoutParams()).topMargin = AndroidUtilities.dp(20.0f);
        ((ViewGroup.MarginLayoutParams) this.emptyView.getLayoutParams()).leftMargin = AndroidUtilities.dp(4.0f);
        ((ViewGroup.MarginLayoutParams) this.emptyView.getLayoutParams()).rightMargin = AndroidUtilities.dp(4.0f);
    }

    private void generateLink() {
        if (this.linkGenerating) {
            return;
        }
        this.linkGenerating = true;
        TLRPC.TL_messages_exportChatInvite tL_messages_exportChatInvite = new TLRPC.TL_messages_exportChatInvite();
        tL_messages_exportChatInvite.legacy_revoke_permanent = true;
        tL_messages_exportChatInvite.peer = MessagesController.getInstance(this.currentAccount).getInputPeer(-this.chatId);
        ConnectionsManager.getInstance(this.currentAccount).sendRequest(tL_messages_exportChatInvite, new RequestDelegate() { // from class: org.telegram.ui.Components.InviteMembersBottomSheet$$ExternalSyntheticLambda7
            @Override // org.telegram.tgnet.RequestDelegate
            public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                InviteMembersBottomSheet.this.lambda$generateLink$8(tLObject, tL_error);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$generateLink$7(TLRPC.TL_error tL_error, TLObject tLObject) {
        if (tL_error == null) {
            this.invite = (TLRPC.TL_chatInviteExported) tLObject;
            TLRPC.ChatFull chatFull = MessagesController.getInstance(this.currentAccount).getChatFull(this.chatId);
            if (chatFull != null) {
                chatFull.exported_invite = this.invite;
            }
            if (this.invite.link == null) {
                return;
            }
            ((ClipboardManager) ApplicationLoader.applicationContext.getSystemService("clipboard")).setPrimaryClip(ClipData.newPlainText("label", this.invite.link));
            BulletinFactory.createCopyLinkBulletin(this.parentFragment).show();
            lambda$new$0();
        }
        this.linkGenerating = false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$generateLink$8(final TLObject tLObject, final TLRPC.TL_error tL_error) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.Components.InviteMembersBottomSheet$$ExternalSyntheticLambda8
            @Override // java.lang.Runnable
            public final void run() {
                InviteMembersBottomSheet.this.lambda$generateLink$7(tL_error, tLObject);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Removed duplicated region for block: B:10:0x0074  */
    /* JADX WARN: Removed duplicated region for block: B:51:0x00d4 A[RETURN] */
    /* JADX WARN: Removed duplicated region for block: B:52:0x00d5  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public /* synthetic */ void lambda$new$0(long j, BaseFragment baseFragment, LongSparseArray longSparseArray, Context context, View view, int i) {
        String str;
        TLRPC.TL_chatInviteExported tL_chatInviteExported;
        ArrayList globalSearch;
        int i2;
        Object obj;
        RecyclerView.Adapter adapter = this.listView.getAdapter();
        SearchAdapter searchAdapter = this.searchAdapter;
        TLObject tLObject = null;
        if (adapter == searchAdapter) {
            int size = searchAdapter.searchResult.size();
            int size2 = this.searchAdapter.searchAdapterHelper.getGlobalSearch().size();
            int size3 = this.searchAdapter.searchAdapterHelper.getLocalServerSearch().size();
            int i3 = i - 1;
            if (i3 < 0 || i3 >= size) {
                if (i3 < size || i3 >= size3 + size) {
                    if (i3 > size + size3 && i3 <= size2 + size + size3) {
                        globalSearch = this.searchAdapter.searchAdapterHelper.getGlobalSearch();
                        i2 = ((i3 - size) - size3) - 1;
                    }
                    if (this.dialogsDelegate != null) {
                        this.searchView.closeSearch();
                    }
                } else {
                    globalSearch = this.searchAdapter.searchAdapterHelper.getLocalServerSearch();
                    i2 = i3 - size;
                }
                obj = globalSearch.get(i2);
            } else {
                obj = this.searchAdapter.searchResult.get(i3);
            }
            tLObject = (TLObject) obj;
            if (this.dialogsDelegate != null) {
            }
        } else if (i == this.copyLinkRow) {
            TLRPC.Chat chat = MessagesController.getInstance(this.currentAccount).getChat(Long.valueOf(j));
            TLRPC.ChatFull chatFull = MessagesController.getInstance(this.currentAccount).getChatFull(j);
            if (chat != null) {
                String publicUsername = ChatObject.getPublicUsername(chat);
                if (!TextUtils.isEmpty(publicUsername)) {
                    str = "https://" + MessagesController.getInstance(this.currentAccount).linkPrefix + "/" + publicUsername;
                    if (str != null) {
                        return;
                    }
                    ((ClipboardManager) ApplicationLoader.applicationContext.getSystemService("clipboard")).setPrimaryClip(ClipData.newPlainText("label", str));
                    lambda$new$0();
                    BulletinFactory.createCopyLinkBulletin(baseFragment).show();
                }
            }
            if (chatFull == null || (tL_chatInviteExported = chatFull.exported_invite) == null) {
                generateLink();
                str = null;
            } else {
                str = tL_chatInviteExported.link;
            }
            if (str != null) {
            }
        } else if (i >= this.contactsStartRow && i < this.contactsEndRow) {
            tLObject = ((ListAdapter) this.listViewAdapter).getObject(i);
        }
        if (tLObject != null) {
            long j2 = tLObject instanceof TLRPC.User ? ((TLRPC.User) tLObject).id : tLObject instanceof TLRPC.Chat ? -((TLRPC.Chat) tLObject).id : 0L;
            if (longSparseArray == null || longSparseArray.indexOfKey(j2) < 0) {
                if (j2 != 0) {
                    if (this.selectedContacts.indexOfKey(j2) >= 0) {
                        GroupCreateSpan groupCreateSpan = (GroupCreateSpan) this.selectedContacts.get(j2);
                        this.selectedContacts.remove(j2);
                        this.spansContainer.removeSpan(groupCreateSpan);
                    } else {
                        GroupCreateSpan groupCreateSpan2 = new GroupCreateSpan(context, tLObject);
                        groupCreateSpan2.setOnClickListener(this.spanClickListener);
                        this.selectedContacts.put(j2, groupCreateSpan2);
                        this.spansContainer.addSpan(groupCreateSpan2, true);
                    }
                }
                spansCountChanged(true);
                AndroidUtilities.updateVisibleRows(this.listView);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$1(AlertDialog alertDialog, int i) {
        onAddToGroupDone(0);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$2(Context context, long j, View view) {
        Activity findActivity;
        SpannableStringBuilder replaceTags;
        if ((this.dialogsDelegate == null && this.selectedContacts.size() == 0) || (findActivity = AndroidUtilities.findActivity(context)) == null) {
            return;
        }
        if (this.dialogsDelegate != null) {
            ArrayList arrayList = new ArrayList();
            for (int i = 0; i < this.selectedContacts.size(); i++) {
                arrayList.add(Long.valueOf(this.selectedContacts.keyAt(i)));
            }
            this.dialogsDelegate.didSelectDialogs(arrayList);
            lambda$new$0();
            return;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(findActivity);
        builder.setTitle(LocaleController.formatPluralString("AddManyMembersAlertTitle", this.selectedContacts.size(), new Object[0]));
        StringBuilder sb = new StringBuilder();
        for (int i2 = 0; i2 < this.selectedContacts.size(); i2++) {
            TLRPC.User user = MessagesController.getInstance(this.currentAccount).getUser(Long.valueOf(this.selectedContacts.keyAt(i2)));
            if (user != null) {
                if (sb.length() > 0) {
                    sb.append(", ");
                }
                sb.append("**");
                sb.append(ContactsController.formatName(user.first_name, user.last_name));
                sb.append("**");
            }
        }
        TLRPC.Chat chat = MessagesController.getInstance(this.currentAccount).getChat(Long.valueOf(j));
        if (this.selectedContacts.size() > 5) {
            replaceTags = new SpannableStringBuilder(AndroidUtilities.replaceTags(LocaleController.formatPluralString("AddManyMembersAlertNamesText", this.selectedContacts.size(), chat.title)));
            String format = String.format("%d", Integer.valueOf(this.selectedContacts.size()));
            int indexOf = TextUtils.indexOf(replaceTags, format);
            if (indexOf >= 0) {
                replaceTags.setSpan(new TypefaceSpan(AndroidUtilities.bold()), indexOf, format.length() + indexOf, 33);
            }
        } else {
            replaceTags = AndroidUtilities.replaceTags(LocaleController.formatString("AddMembersAlertNamesText", R.string.AddMembersAlertNamesText, sb, chat.title));
        }
        builder.setMessage(replaceTags);
        builder.setPositiveButton(LocaleController.getString(R.string.Add), new AlertDialog.OnButtonClickListener() { // from class: org.telegram.ui.Components.InviteMembersBottomSheet$$ExternalSyntheticLambda5
            @Override // org.telegram.ui.ActionBar.AlertDialog.OnButtonClickListener
            public final void onClick(AlertDialog alertDialog, int i3) {
                InviteMembersBottomSheet.this.lambda$new$1(alertDialog, i3);
            }
        });
        builder.setNegativeButton(LocaleController.getString(R.string.Cancel), null);
        builder.create();
        builder.show();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onSearchViewTouched$5(final EditTextBoldCursor editTextBoldCursor) {
        setFocusable(true);
        editTextBoldCursor.requestFocus();
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.Components.InviteMembersBottomSheet$$ExternalSyntheticLambda6
            @Override // java.lang.Runnable
            public final void run() {
                AndroidUtilities.showKeyboard(EditTextBoldCursor.this);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$spansCountChanged$3(ValueAnimator valueAnimator) {
        this.spansEnterProgress = ((Float) valueAnimator.getAnimatedValue()).floatValue();
        this.containerView.invalidate();
    }

    private void onAddToGroupDone(int i) {
        ArrayList arrayList = new ArrayList();
        for (int i2 = 0; i2 < this.selectedContacts.size(); i2++) {
            arrayList.add(MessagesController.getInstance(this.currentAccount).getUser(Long.valueOf(this.selectedContacts.keyAt(i2))));
        }
        GroupCreateActivity.ContactsAddActivityDelegate contactsAddActivityDelegate = this.delegate;
        if (contactsAddActivityDelegate != null) {
            contactsAddActivityDelegate.didSelectUsers(arrayList, i);
        }
        lambda$new$0();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void spansCountChanged(boolean z) {
        final boolean z2 = this.selectedContacts.size() > 0;
        if (this.spanEnter != z2) {
            ValueAnimator valueAnimator = this.spansEnterAnimator;
            if (valueAnimator != null) {
                valueAnimator.removeAllListeners();
                this.spansEnterAnimator.cancel();
            }
            this.spanEnter = z2;
            if (z2) {
                this.spansScrollView.setVisibility(0);
            }
            if (!z) {
                this.spansEnterProgress = z2 ? 1.0f : 0.0f;
                this.containerView.invalidate();
                if (!z2) {
                    this.spansScrollView.setVisibility(8);
                }
                AnimatorSet animatorSet = this.currentDoneButtonAnimation;
                if (animatorSet != null) {
                    animatorSet.cancel();
                }
                if (this.spanEnter || this.dialogsDelegate != null) {
                    this.floatingButton.setScaleY(1.0f);
                    this.floatingButton.setScaleX(1.0f);
                    this.floatingButton.setAlpha(1.0f);
                    this.floatingButton.setVisibility(0);
                    return;
                }
                this.floatingButton.setScaleY(0.0f);
                this.floatingButton.setScaleX(0.0f);
                this.floatingButton.setAlpha(0.0f);
                this.floatingButton.setVisibility(4);
                return;
            }
            ValueAnimator ofFloat = ValueAnimator.ofFloat(this.spansEnterProgress, z2 ? 1.0f : 0.0f);
            this.spansEnterAnimator = ofFloat;
            ofFloat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: org.telegram.ui.Components.InviteMembersBottomSheet$$ExternalSyntheticLambda2
                @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                public final void onAnimationUpdate(ValueAnimator valueAnimator2) {
                    InviteMembersBottomSheet.this.lambda$spansCountChanged$3(valueAnimator2);
                }
            });
            this.spansEnterAnimator.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.Components.InviteMembersBottomSheet.4
                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public void onAnimationEnd(Animator animator) {
                    InviteMembersBottomSheet.this.spansEnterProgress = z2 ? 1.0f : 0.0f;
                    ((BottomSheet) InviteMembersBottomSheet.this).containerView.invalidate();
                    if (z2) {
                        return;
                    }
                    InviteMembersBottomSheet.this.spansScrollView.setVisibility(8);
                }
            });
            this.spansEnterAnimator.setDuration(150L);
            this.spansEnterAnimator.start();
            if (this.spanEnter || this.dialogsDelegate != null) {
                AnimatorSet animatorSet2 = this.currentDoneButtonAnimation;
                if (animatorSet2 != null) {
                    animatorSet2.cancel();
                }
                this.currentDoneButtonAnimation = new AnimatorSet();
                this.floatingButton.setVisibility(0);
                this.currentDoneButtonAnimation.playTogether(ObjectAnimator.ofFloat(this.floatingButton, (Property<ImageView, Float>) View.SCALE_X, 1.0f), ObjectAnimator.ofFloat(this.floatingButton, (Property<ImageView, Float>) View.SCALE_Y, 1.0f), ObjectAnimator.ofFloat(this.floatingButton, (Property<ImageView, Float>) View.ALPHA, 1.0f));
            } else {
                AnimatorSet animatorSet3 = this.currentDoneButtonAnimation;
                if (animatorSet3 != null) {
                    animatorSet3.cancel();
                }
                AnimatorSet animatorSet4 = new AnimatorSet();
                this.currentDoneButtonAnimation = animatorSet4;
                animatorSet4.playTogether(ObjectAnimator.ofFloat(this.floatingButton, (Property<ImageView, Float>) View.SCALE_X, 0.0f), ObjectAnimator.ofFloat(this.floatingButton, (Property<ImageView, Float>) View.SCALE_Y, 0.0f), ObjectAnimator.ofFloat(this.floatingButton, (Property<ImageView, Float>) View.ALPHA, 0.0f));
                this.currentDoneButtonAnimation.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.Components.InviteMembersBottomSheet.5
                    @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                    public void onAnimationEnd(Animator animator) {
                        InviteMembersBottomSheet.this.floatingButton.setVisibility(4);
                    }
                });
            }
            this.currentDoneButtonAnimation.setDuration(180L);
            this.currentDoneButtonAnimation.start();
        }
    }

    private void updateRows() {
        int i;
        ArrayList arrayList;
        this.contactsStartRow = -1;
        this.contactsEndRow = -1;
        this.copyLinkRow = -1;
        this.noContactsStubRow = -1;
        this.rowCount = 1;
        this.emptyRow = 0;
        if (this.dialogsDelegate == null) {
            if (hasLink()) {
                int i2 = this.rowCount;
                this.rowCount = i2 + 1;
                this.copyLinkRow = i2;
            }
            if (this.contacts.size() != 0) {
                i = this.rowCount;
                this.contactsStartRow = i;
                arrayList = this.contacts;
                int size = i + arrayList.size();
                this.rowCount = size;
                this.contactsEndRow = size;
            }
            int i3 = this.rowCount;
            this.rowCount = i3 + 1;
            this.noContactsStubRow = i3;
        } else {
            if (this.dialogsServerOnly.size() != 0) {
                i = this.rowCount;
                this.contactsStartRow = i;
                arrayList = this.dialogsServerOnly;
                int size2 = i + arrayList.size();
                this.rowCount = size2;
                this.contactsEndRow = size2;
            }
            int i32 = this.rowCount;
            this.rowCount = i32 + 1;
            this.noContactsStubRow = i32;
        }
        int i4 = this.rowCount;
        this.rowCount = i4 + 1;
        this.lastRow = i4;
    }

    protected boolean canGenerateLink() {
        return true;
    }

    @Override // org.telegram.ui.Components.UsersAlertBase
    protected UsersAlertBase.ContainerView createContainerView(Context context) {
        return new UsersAlertBase.ContainerView(context) { // from class: org.telegram.ui.Components.InviteMembersBottomSheet.6
            float animateToEmptyViewOffset;
            float deltaOffset;
            float emptyViewOffset;
            Paint paint = new Paint();
            private VerticalPositionAutoAnimator verticalPositionAutoAnimator;

            @Override // org.telegram.ui.Components.UsersAlertBase.ContainerView, android.view.ViewGroup, android.view.View
            protected void dispatchDraw(Canvas canvas) {
                InviteMembersBottomSheet inviteMembersBottomSheet = InviteMembersBottomSheet.this;
                InviteMembersBottomSheet.this.spansScrollView.setTranslationY((inviteMembersBottomSheet.scrollOffsetY - ((BottomSheet) inviteMembersBottomSheet).backgroundPaddingTop) + AndroidUtilities.dp(6.0f) + AndroidUtilities.dp(64.0f));
                float f = InviteMembersBottomSheet.this.additionalHeight + InviteMembersBottomSheet.this.searchAdditionalHeight;
                if (InviteMembersBottomSheet.this.emptyView.getVisibility() != 0) {
                    this.emptyViewOffset = f;
                    this.animateToEmptyViewOffset = f;
                } else if (this.animateToEmptyViewOffset != f) {
                    this.animateToEmptyViewOffset = f;
                    this.deltaOffset = (f - this.emptyViewOffset) * 0.10666667f;
                }
                float f2 = this.emptyViewOffset;
                float f3 = this.animateToEmptyViewOffset;
                if (f2 != f3) {
                    float f4 = this.deltaOffset;
                    float f5 = f2 + f4;
                    this.emptyViewOffset = f5;
                    if ((f4 <= 0.0f || f5 <= f3) && (f4 >= 0.0f || f5 >= f3)) {
                        invalidate();
                    } else {
                        this.emptyViewOffset = f3;
                    }
                }
                InviteMembersBottomSheet.this.emptyView.setTranslationY(r0.scrollOffsetY + this.emptyViewOffset);
                super.dispatchDraw(canvas);
            }

            @Override // android.view.ViewGroup
            protected boolean drawChild(Canvas canvas, View view, long j) {
                if (view != InviteMembersBottomSheet.this.spansScrollView) {
                    return super.drawChild(canvas, view, j);
                }
                canvas.save();
                canvas.clipRect(0.0f, view.getY() - AndroidUtilities.dp(4.0f), getMeasuredWidth(), view.getY() + InviteMembersBottomSheet.this.scrollViewH + 1.0f);
                canvas.drawColor(ColorUtils.setAlphaComponent(Theme.getColor(Theme.key_windowBackgroundWhite), (int) (InviteMembersBottomSheet.this.spansEnterProgress * 255.0f)));
                this.paint.setColor(ColorUtils.setAlphaComponent(Theme.getColor(Theme.key_divider), (int) (InviteMembersBottomSheet.this.spansEnterProgress * 255.0f)));
                canvas.drawRect(0.0f, view.getY() + InviteMembersBottomSheet.this.scrollViewH, getMeasuredWidth(), view.getY() + InviteMembersBottomSheet.this.scrollViewH + 1.0f, this.paint);
                boolean drawChild = super.drawChild(canvas, view, j);
                canvas.restore();
                return drawChild;
            }

            @Override // android.view.ViewGroup, android.view.View
            protected void onAttachedToWindow() {
                super.onAttachedToWindow();
                VerticalPositionAutoAnimator verticalPositionAutoAnimator = this.verticalPositionAutoAnimator;
                if (verticalPositionAutoAnimator != null) {
                    verticalPositionAutoAnimator.ignoreNextLayout();
                }
            }

            @Override // android.view.ViewGroup
            public void onViewAdded(View view) {
                if (view == InviteMembersBottomSheet.this.floatingButton && this.verticalPositionAutoAnimator == null) {
                    this.verticalPositionAutoAnimator = VerticalPositionAutoAnimator.attach(view);
                }
            }
        };
    }

    @Override // org.telegram.messenger.NotificationCenter.NotificationCenterDelegate
    public void didReceivedNotification(int i, int i2, Object... objArr) {
        if (i == NotificationCenter.dialogsNeedReload && this.dialogsDelegate != null && this.dialogsServerOnly.isEmpty()) {
            this.dialogsServerOnly = new ArrayList(MessagesController.getInstance(this.currentAccount).dialogsServerOnly);
            this.listViewAdapter.notifyDataSetChanged();
        }
    }

    @Override // org.telegram.ui.Components.UsersAlertBase, org.telegram.ui.ActionBar.BottomSheet, android.app.Dialog, android.content.DialogInterface, org.telegram.ui.ActionBar.BaseFragment.AttachedSheet
    /* renamed from: dismiss */
    public void lambda$new$0() {
        super.lambda$new$0();
        NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.dialogsNeedReload);
    }

    @Override // org.telegram.ui.ActionBar.BottomSheet
    public void dismissInternal() {
        super.dismissInternal();
        if (this.enterEventSent) {
            Activity findActivity = AndroidUtilities.findActivity(getContext());
            if (findActivity instanceof LaunchActivity) {
                LaunchActivity launchActivity = (LaunchActivity) findActivity;
                BaseFragment baseFragment = (BaseFragment) launchActivity.getActionBarLayout().getFragmentStack().get(launchActivity.getActionBarLayout().getFragmentStack().size() - 1);
                if (baseFragment instanceof ChatActivity) {
                    ((ChatActivity) baseFragment).onEditTextDialogClose(true, true);
                }
            }
        }
    }

    protected boolean hasLink() {
        TLRPC.Chat chat = MessagesController.getInstance(this.currentAccount).getChat(Long.valueOf(this.chatId));
        TLRPC.ChatFull chatFull = MessagesController.getInstance(this.currentAccount).getChatFull(this.chatId);
        if (chat != null && !TextUtils.isEmpty(ChatObject.getPublicUsername(chat))) {
            return true;
        }
        if (chatFull == null || chatFull.exported_invite == null) {
            return canGenerateLink();
        }
        return true;
    }

    @Override // org.telegram.ui.Components.UsersAlertBase
    protected void onSearchViewTouched(MotionEvent motionEvent, final EditTextBoldCursor editTextBoldCursor) {
        BaseFragment baseFragment;
        if (motionEvent.getAction() == 0) {
            this.y = this.scrollOffsetY;
            return;
        }
        if (motionEvent.getAction() != 1 || Math.abs(this.scrollOffsetY - this.y) >= this.touchSlop || this.enterEventSent) {
            return;
        }
        Activity findActivity = AndroidUtilities.findActivity(getContext());
        if (findActivity instanceof LaunchActivity) {
            LaunchActivity launchActivity = (LaunchActivity) findActivity;
            baseFragment = (BaseFragment) launchActivity.getActionBarLayout().getFragmentStack().get(launchActivity.getActionBarLayout().getFragmentStack().size() - 1);
        } else {
            baseFragment = null;
        }
        if (baseFragment instanceof ChatActivity) {
            boolean needEnterText = ((ChatActivity) baseFragment).needEnterText();
            this.enterEventSent = true;
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.Components.InviteMembersBottomSheet$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    InviteMembersBottomSheet.this.lambda$onSearchViewTouched$5(editTextBoldCursor);
                }
            }, needEnterText ? 200L : 0L);
        } else {
            this.enterEventSent = true;
            setFocusable(true);
            editTextBoldCursor.requestFocus();
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.Components.InviteMembersBottomSheet$$ExternalSyntheticLambda1
                @Override // java.lang.Runnable
                public final void run() {
                    AndroidUtilities.showKeyboard(EditTextBoldCursor.this);
                }
            });
        }
    }

    @Override // org.telegram.ui.Components.UsersAlertBase
    protected void search(String str) {
        this.searchAdapter.searchDialogs(str);
    }

    public void setDelegate(InviteMembersBottomSheetDelegate inviteMembersBottomSheetDelegate, ArrayList arrayList) {
        this.dialogsDelegate = inviteMembersBottomSheetDelegate;
        NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.dialogsNeedReload);
        this.dialogsServerOnly = new ArrayList(MessagesController.getInstance(this.currentAccount).dialogsServerOnly);
        updateRows();
    }

    public void setDelegate(GroupCreateActivity.ContactsAddActivityDelegate contactsAddActivityDelegate) {
        this.delegate = contactsAddActivityDelegate;
    }

    /* JADX WARN: Removed duplicated region for block: B:25:0x00c3  */
    /* JADX WARN: Removed duplicated region for block: B:37:0x0111  */
    /* JADX WARN: Removed duplicated region for block: B:42:0x0139  */
    /* JADX WARN: Removed duplicated region for block: B:51:0x011e  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void setSelectedContacts(ArrayList arrayList) {
        float max;
        int i;
        int i2;
        int min;
        int size = arrayList.size();
        for (int i3 = 0; i3 < size; i3++) {
            Long l = (Long) arrayList.get(i3);
            long longValue = l.longValue();
            GroupCreateSpan groupCreateSpan = new GroupCreateSpan(this.spansContainer.getContext(), DialogObject.isChatDialog(longValue) ? MessagesController.getInstance(this.currentAccount).getChat(Long.valueOf(-longValue)) : MessagesController.getInstance(this.currentAccount).getUser(l));
            this.spansContainer.addSpan(groupCreateSpan, false);
            groupCreateSpan.setOnClickListener(this.spanClickListener);
        }
        spansCountChanged(false);
        int childCount = this.spansContainer.getChildCount();
        android.graphics.Point point = AndroidUtilities.displaySize;
        boolean z = point.x < point.y;
        this.maxSize = (AndroidUtilities.isTablet() || z) ? AndroidUtilities.dp(144.0f) : AndroidUtilities.dp(56.0f);
        if (AndroidUtilities.isTablet()) {
            android.graphics.Point point2 = AndroidUtilities.displaySize;
            max = Math.min(point2.x, point2.y) * 0.8f;
        } else {
            if (z) {
                i = AndroidUtilities.displaySize.x;
                int dp = i - AndroidUtilities.dp(26.0f);
                int dp2 = AndroidUtilities.dp(10.0f);
                int i4 = 0;
                for (i2 = 0; i2 < childCount; i2++) {
                    View childAt = this.spansContainer.getChildAt(i2);
                    if (childAt instanceof GroupCreateSpan) {
                        childAt.measure(View.MeasureSpec.makeMeasureSpec(i, Integer.MIN_VALUE), View.MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(32.0f), 1073741824));
                        if (childAt.getMeasuredWidth() + i4 > dp) {
                            dp2 += childAt.getMeasuredHeight() + AndroidUtilities.dp(8.0f);
                            i4 = 0;
                        }
                        i4 += childAt.getMeasuredWidth() + AndroidUtilities.dp(9.0f);
                    }
                }
                int dp3 = dp2 + AndroidUtilities.dp(42.0f);
                min = this.dialogsDelegate == null ? this.spanEnter ? Math.min(this.maxSize, dp3) : 0 : Math.max(0, Math.min(this.maxSize, dp3) - AndroidUtilities.dp(52.0f));
                int i5 = this.searchAdditionalHeight;
                int dp4 = this.selectedContacts.size() > 0 ? AndroidUtilities.dp(56.0f) : 0;
                this.searchAdditionalHeight = dp4;
                if (min == this.additionalHeight || i5 != dp4) {
                    this.additionalHeight = min;
                }
                return;
            }
            max = Math.max(AndroidUtilities.displaySize.x * 0.8f, Math.min(AndroidUtilities.dp(480.0f), AndroidUtilities.displaySize.x));
        }
        i = (int) max;
        int dp5 = i - AndroidUtilities.dp(26.0f);
        int dp22 = AndroidUtilities.dp(10.0f);
        int i42 = 0;
        while (i2 < childCount) {
        }
        int dp32 = dp22 + AndroidUtilities.dp(42.0f);
        if (this.dialogsDelegate == null) {
        }
        int i52 = this.searchAdditionalHeight;
        if (this.selectedContacts.size() > 0) {
        }
        this.searchAdditionalHeight = dp4;
        if (min == this.additionalHeight) {
        }
        this.additionalHeight = min;
    }
}
