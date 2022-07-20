package org.telegram.ui.Adapters;

import android.content.Context;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import androidx.collection.LongSparseArray;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;
import org.telegram.PhoneFormat.PhoneFormat;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.ContactsController;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.UserConfig;
import org.telegram.messenger.UserObject;
import org.telegram.messenger.Utilities;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC$Chat;
import org.telegram.tgnet.TLRPC$TL_contact;
import org.telegram.tgnet.TLRPC$User;
import org.telegram.ui.Adapters.SearchAdapterHelper;
import org.telegram.ui.Cells.GraySectionCell;
import org.telegram.ui.Cells.ProfileSearchCell;
import org.telegram.ui.Cells.TextCell;
import org.telegram.ui.Cells.UserCell;
import org.telegram.ui.Components.ForegroundColorSpanThemable;
import org.telegram.ui.Components.RecyclerListView;
/* loaded from: classes3.dex */
public class SearchAdapter extends RecyclerListView.SelectionAdapter {
    private boolean allowBots;
    private boolean allowChats;
    private boolean allowPhoneNumbers;
    private boolean allowSelf;
    private boolean allowUsernameSearch;
    private long channelId;
    private LongSparseArray<?> checkedMap;
    private LongSparseArray<TLRPC$User> ignoreUsers;
    private Context mContext;
    private boolean onlyMutual;
    private SearchAdapterHelper searchAdapterHelper;
    private boolean searchInProgress;
    private int searchPointer;
    private int searchReqId;
    private ArrayList<Object> searchResult = new ArrayList<>();
    private ArrayList<CharSequence> searchResultNames = new ArrayList<>();
    private Timer searchTimer;
    private boolean useUserCell;

    protected void onSearchProgressChanged() {
        throw null;
    }

    public SearchAdapter(Context context, LongSparseArray<TLRPC$User> longSparseArray, boolean z, boolean z2, boolean z3, boolean z4, boolean z5, boolean z6, int i) {
        this.mContext = context;
        this.ignoreUsers = longSparseArray;
        this.onlyMutual = z2;
        this.allowUsernameSearch = z;
        this.allowChats = z3;
        this.allowBots = z4;
        this.channelId = i;
        this.allowSelf = z5;
        this.allowPhoneNumbers = z6;
        SearchAdapterHelper searchAdapterHelper = new SearchAdapterHelper(true);
        this.searchAdapterHelper = searchAdapterHelper;
        searchAdapterHelper.setDelegate(new AnonymousClass1());
    }

    /* renamed from: org.telegram.ui.Adapters.SearchAdapter$1 */
    /* loaded from: classes3.dex */
    public class AnonymousClass1 implements SearchAdapterHelper.SearchAdapterHelperDelegate {
        @Override // org.telegram.ui.Adapters.SearchAdapterHelper.SearchAdapterHelperDelegate
        public /* synthetic */ boolean canApplySearchResults(int i) {
            return SearchAdapterHelper.SearchAdapterHelperDelegate.CC.$default$canApplySearchResults(this, i);
        }

        @Override // org.telegram.ui.Adapters.SearchAdapterHelper.SearchAdapterHelperDelegate
        public /* synthetic */ LongSparseArray getExcludeCallParticipants() {
            return SearchAdapterHelper.SearchAdapterHelperDelegate.CC.$default$getExcludeCallParticipants(this);
        }

        @Override // org.telegram.ui.Adapters.SearchAdapterHelper.SearchAdapterHelperDelegate
        public /* synthetic */ void onSetHashtags(ArrayList arrayList, HashMap hashMap) {
            SearchAdapterHelper.SearchAdapterHelperDelegate.CC.$default$onSetHashtags(this, arrayList, hashMap);
        }

        AnonymousClass1() {
            SearchAdapter.this = r1;
        }

        @Override // org.telegram.ui.Adapters.SearchAdapterHelper.SearchAdapterHelperDelegate
        public void onDataSetChanged(int i) {
            SearchAdapter.this.notifyDataSetChanged();
            if (i != 0) {
                SearchAdapter.this.onSearchProgressChanged();
            }
        }

        @Override // org.telegram.ui.Adapters.SearchAdapterHelper.SearchAdapterHelperDelegate
        public LongSparseArray<TLRPC$User> getExcludeUsers() {
            return SearchAdapter.this.ignoreUsers;
        }
    }

    public void searchDialogs(String str) {
        try {
            Timer timer = this.searchTimer;
            if (timer != null) {
                timer.cancel();
            }
        } catch (Exception e) {
            FileLog.e(e);
        }
        this.searchResult.clear();
        this.searchResultNames.clear();
        if (this.allowUsernameSearch) {
            this.searchAdapterHelper.queryServerSearch(null, true, this.allowChats, this.allowBots, this.allowSelf, false, this.channelId, this.allowPhoneNumbers, 0, 0);
        }
        notifyDataSetChanged();
        if (!TextUtils.isEmpty(str)) {
            Timer timer2 = new Timer();
            this.searchTimer = timer2;
            timer2.schedule(new AnonymousClass2(str), 200L, 300L);
        }
    }

    /* renamed from: org.telegram.ui.Adapters.SearchAdapter$2 */
    /* loaded from: classes3.dex */
    public class AnonymousClass2 extends TimerTask {
        final /* synthetic */ String val$query;

        AnonymousClass2(String str) {
            SearchAdapter.this = r1;
            this.val$query = str;
        }

        @Override // java.util.TimerTask, java.lang.Runnable
        public void run() {
            try {
                SearchAdapter.this.searchTimer.cancel();
                SearchAdapter.this.searchTimer = null;
            } catch (Exception e) {
                FileLog.e(e);
            }
            SearchAdapter.this.processSearch(this.val$query);
        }
    }

    public void processSearch(String str) {
        AndroidUtilities.runOnUIThread(new SearchAdapter$$ExternalSyntheticLambda1(this, str));
    }

    public /* synthetic */ void lambda$processSearch$1(String str) {
        if (this.allowUsernameSearch) {
            this.searchAdapterHelper.queryServerSearch(str, true, this.allowChats, this.allowBots, this.allowSelf, false, this.channelId, this.allowPhoneNumbers, -1, 1);
        }
        int i = UserConfig.selectedAccount;
        ArrayList arrayList = new ArrayList(ContactsController.getInstance(i).contacts);
        this.searchInProgress = true;
        int i2 = this.searchPointer;
        this.searchPointer = i2 + 1;
        this.searchReqId = i2;
        Utilities.searchQueue.postRunnable(new SearchAdapter$$ExternalSyntheticLambda2(this, str, i2, arrayList, i));
    }

    public /* synthetic */ void lambda$processSearch$0(String str, int i, ArrayList arrayList, int i2) {
        int i3;
        String str2;
        LongSparseArray<TLRPC$User> longSparseArray;
        String str3;
        String lowerCase = str.trim().toLowerCase();
        if (lowerCase.length() == 0) {
            updateSearchResults(i, new ArrayList<>(), new ArrayList<>());
            return;
        }
        String translitString = LocaleController.getInstance().getTranslitString(lowerCase);
        String str4 = null;
        if (lowerCase.equals(translitString) || translitString.length() == 0) {
            translitString = null;
        }
        char c = 0;
        char c2 = 1;
        int i4 = (translitString != null ? 1 : 0) + 1;
        String[] strArr = new String[i4];
        strArr[0] = lowerCase;
        if (translitString != null) {
            strArr[1] = translitString;
        }
        ArrayList<Object> arrayList2 = new ArrayList<>();
        ArrayList<CharSequence> arrayList3 = new ArrayList<>();
        int i5 = 0;
        while (i5 < arrayList.size()) {
            TLRPC$TL_contact tLRPC$TL_contact = (TLRPC$TL_contact) arrayList.get(i5);
            TLRPC$User user = MessagesController.getInstance(i2).getUser(Long.valueOf(tLRPC$TL_contact.user_id));
            if ((this.allowSelf || !user.self) && ((!this.onlyMutual || user.mutual_contact) && ((longSparseArray = this.ignoreUsers) == null || longSparseArray.indexOfKey(tLRPC$TL_contact.user_id) < 0))) {
                int i6 = 3;
                String[] strArr2 = new String[3];
                strArr2[c] = ContactsController.formatName(user.first_name, user.last_name).toLowerCase();
                strArr2[c2] = LocaleController.getInstance().getTranslitString(strArr2[c]);
                if (strArr2[c].equals(strArr2[c2])) {
                    strArr2[c2] = str4;
                }
                if (UserObject.isReplyUser(user)) {
                    strArr2[2] = LocaleController.getString("RepliesTitle", 2131627920).toLowerCase();
                } else if (user.self) {
                    strArr2[2] = LocaleController.getString("SavedMessages", 2131628077).toLowerCase();
                }
                int i7 = 0;
                char c3 = 0;
                while (i7 < i4) {
                    String str5 = strArr[i7];
                    int i8 = 0;
                    while (i8 < i6) {
                        String str6 = strArr2[i8];
                        if (str6 != null) {
                            if (!str6.startsWith(str5)) {
                                StringBuilder sb = new StringBuilder();
                                i3 = i4;
                                sb.append(" ");
                                sb.append(str5);
                                if (str6.contains(sb.toString())) {
                                }
                            } else {
                                i3 = i4;
                            }
                            c3 = 1;
                            break;
                        }
                        i3 = i4;
                        i8++;
                        i4 = i3;
                        i6 = 3;
                    }
                    i3 = i4;
                    if (c3 == 0 && (str3 = user.username) != null && str3.startsWith(str5)) {
                        c3 = 2;
                    }
                    if (c3 != 0) {
                        if (c3 == 1) {
                            arrayList3.add(AndroidUtilities.generateSearchName(user.first_name, user.last_name, str5));
                            str2 = null;
                        } else {
                            str2 = null;
                            arrayList3.add(AndroidUtilities.generateSearchName("@" + user.username, null, "@" + str5));
                        }
                        arrayList2.add(user);
                        i5++;
                        str4 = str2;
                        i4 = i3;
                        c = 0;
                        c2 = 1;
                    } else {
                        i7++;
                        str4 = null;
                        i4 = i3;
                        i6 = 3;
                    }
                }
            }
            i3 = i4;
            str2 = str4;
            i5++;
            str4 = str2;
            i4 = i3;
            c = 0;
            c2 = 1;
        }
        updateSearchResults(i, arrayList2, arrayList3);
    }

    private void updateSearchResults(int i, ArrayList<Object> arrayList, ArrayList<CharSequence> arrayList2) {
        AndroidUtilities.runOnUIThread(new SearchAdapter$$ExternalSyntheticLambda0(this, i, arrayList, arrayList2));
    }

    public /* synthetic */ void lambda$updateSearchResults$2(int i, ArrayList arrayList, ArrayList arrayList2) {
        if (i == this.searchReqId) {
            this.searchResult = arrayList;
            this.searchResultNames = arrayList2;
            this.searchAdapterHelper.mergeResults(arrayList);
            this.searchInProgress = false;
            notifyDataSetChanged();
            onSearchProgressChanged();
        }
    }

    public boolean searchInProgress() {
        return this.searchInProgress || this.searchAdapterHelper.isSearchInProgress();
    }

    @Override // org.telegram.ui.Components.RecyclerListView.SelectionAdapter
    public boolean isEnabled(RecyclerView.ViewHolder viewHolder) {
        int itemViewType = viewHolder.getItemViewType();
        return itemViewType == 0 || itemViewType == 2;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public int getItemCount() {
        int size = this.searchResult.size();
        int size2 = this.searchAdapterHelper.getGlobalSearch().size();
        if (size2 != 0) {
            size += size2 + 1;
        }
        int size3 = this.searchAdapterHelper.getPhoneSearch().size();
        return size3 != 0 ? size + size3 : size;
    }

    public boolean isGlobalSearch(int i) {
        int size = this.searchResult.size();
        int size2 = this.searchAdapterHelper.getGlobalSearch().size();
        int size3 = this.searchAdapterHelper.getPhoneSearch().size();
        if (i < 0 || i >= size) {
            return (i <= size || i >= size + size3) && i > size + size3 && i <= (size2 + size3) + size;
        }
        return false;
    }

    public Object getItem(int i) {
        int size = this.searchResult.size();
        int size2 = this.searchAdapterHelper.getGlobalSearch().size();
        int size3 = this.searchAdapterHelper.getPhoneSearch().size();
        if (i < 0 || i >= size) {
            int i2 = i - size;
            if (i2 >= 0 && i2 < size3) {
                return this.searchAdapterHelper.getPhoneSearch().get(i2);
            }
            int i3 = i2 - size3;
            if (i3 > 0 && i3 <= size2) {
                return this.searchAdapterHelper.getGlobalSearch().get(i3 - 1);
            }
            return null;
        }
        return this.searchResult.get(i);
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view;
        if (i != 0) {
            if (i == 1) {
                view = new GraySectionCell(this.mContext);
            } else {
                view = new TextCell(this.mContext, 16, false);
            }
        } else if (this.useUserCell) {
            UserCell userCell = new UserCell(this.mContext, 1, 1, false);
            if (this.checkedMap != null) {
                userCell.setChecked(false, false);
            }
            view = userCell;
        } else {
            view = new ProfileSearchCell(this.mContext);
        }
        return new RecyclerListView.Holder(view);
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
        boolean z;
        String str;
        SpannableStringBuilder spannableStringBuilder;
        int indexOfIgnoreCase;
        int itemViewType = viewHolder.getItemViewType();
        String str2 = null;
        boolean z2 = false;
        boolean z3 = true;
        if (itemViewType != 0) {
            if (itemViewType == 1) {
                GraySectionCell graySectionCell = (GraySectionCell) viewHolder.itemView;
                if (getItem(i) == null) {
                    graySectionCell.setText(LocaleController.getString("GlobalSearch", 2131626079));
                    return;
                } else {
                    graySectionCell.setText(LocaleController.getString("PhoneNumberSearch", 2131627500));
                    return;
                }
            } else if (itemViewType != 2) {
                return;
            } else {
                TextCell textCell = (TextCell) viewHolder.itemView;
                textCell.setColors(null, "windowBackgroundWhiteBlueText2");
                textCell.setText(LocaleController.formatString("AddContactByPhone", 2131624260, PhoneFormat.getInstance().format("+" + ((String) getItem(i)))), false);
                return;
            }
        }
        TLObject tLObject = (TLObject) getItem(i);
        if (tLObject == null) {
            return;
        }
        long j = 0;
        if (tLObject instanceof TLRPC$User) {
            TLRPC$User tLRPC$User = (TLRPC$User) tLObject;
            str = tLRPC$User.username;
            j = tLRPC$User.id;
            z = tLRPC$User.self;
        } else {
            if (tLObject instanceof TLRPC$Chat) {
                TLRPC$Chat tLRPC$Chat = (TLRPC$Chat) tLObject;
                str = tLRPC$Chat.username;
                j = tLRPC$Chat.id;
            } else {
                str = null;
            }
            z = false;
        }
        if (i < this.searchResult.size()) {
            CharSequence charSequence = this.searchResultNames.get(i);
            if (charSequence != null && str != null && str.length() > 0) {
                if (charSequence.toString().startsWith("@" + str)) {
                    spannableStringBuilder = charSequence;
                }
            }
            spannableStringBuilder = null;
            str2 = charSequence;
        } else if (i <= this.searchResult.size() || str == null) {
            spannableStringBuilder = null;
        } else {
            String lastFoundUsername = this.searchAdapterHelper.getLastFoundUsername();
            if (lastFoundUsername != null && lastFoundUsername.startsWith("@")) {
                lastFoundUsername = lastFoundUsername.substring(1);
            }
            try {
                SpannableStringBuilder spannableStringBuilder2 = new SpannableStringBuilder();
                spannableStringBuilder2.append((CharSequence) "@");
                spannableStringBuilder2.append((CharSequence) str);
                if (lastFoundUsername != null && (indexOfIgnoreCase = AndroidUtilities.indexOfIgnoreCase(str, lastFoundUsername)) != -1) {
                    int length = lastFoundUsername.length();
                    if (indexOfIgnoreCase == 0) {
                        length++;
                    } else {
                        indexOfIgnoreCase++;
                    }
                    spannableStringBuilder2.setSpan(new ForegroundColorSpanThemable("windowBackgroundWhiteBlueText4"), indexOfIgnoreCase, length + indexOfIgnoreCase, 33);
                }
                spannableStringBuilder = spannableStringBuilder2;
            } catch (Exception e) {
                FileLog.e(e);
                spannableStringBuilder = str;
            }
        }
        if (this.useUserCell) {
            UserCell userCell = (UserCell) viewHolder.itemView;
            userCell.setData(tLObject, str2, spannableStringBuilder, 0);
            LongSparseArray<?> longSparseArray = this.checkedMap;
            if (longSparseArray == null) {
                return;
            }
            if (longSparseArray.indexOfKey(j) < 0) {
                z3 = false;
            }
            userCell.setChecked(z3, false);
            return;
        }
        ProfileSearchCell profileSearchCell = (ProfileSearchCell) viewHolder.itemView;
        profileSearchCell.setData(tLObject, null, z ? LocaleController.getString("SavedMessages", 2131628077) : str2, spannableStringBuilder, false, z);
        if (i != getItemCount() - 1 && i != this.searchResult.size() - 1) {
            z2 = true;
        }
        profileSearchCell.useSeparator = z2;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public int getItemViewType(int i) {
        Object item = getItem(i);
        if (item == null) {
            return 1;
        }
        if (!(item instanceof String)) {
            return 0;
        }
        return "section".equals((String) item) ? 1 : 2;
    }
}