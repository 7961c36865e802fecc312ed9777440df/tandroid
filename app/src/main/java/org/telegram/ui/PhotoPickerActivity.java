package org.telegram.ui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Outline;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Property;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewOutlineProvider;
import android.view.accessibility.AccessibilityNodeInfo;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.HashMap;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.ApplicationLoader;
import org.telegram.messenger.ChatObject;
import org.telegram.messenger.DialogObject;
import org.telegram.messenger.FileLoader;
import org.telegram.messenger.ImageReceiver;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MediaController;
import org.telegram.messenger.MessageObject;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.MessagesStorage;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.SharedConfig;
import org.telegram.messenger.UserConfig;
import org.telegram.messenger.UserObject;
import org.telegram.messenger.VideoEditedInfo;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC$BotInlineResult;
import org.telegram.tgnet.TLRPC$Chat;
import org.telegram.tgnet.TLRPC$DocumentAttribute;
import org.telegram.tgnet.TLRPC$FileLocation;
import org.telegram.tgnet.TLRPC$Photo;
import org.telegram.tgnet.TLRPC$PhotoSize;
import org.telegram.tgnet.TLRPC$TL_contacts_resolveUsername;
import org.telegram.tgnet.TLRPC$TL_contacts_resolvedPeer;
import org.telegram.tgnet.TLRPC$TL_documentAttributeImageSize;
import org.telegram.tgnet.TLRPC$TL_documentAttributeVideo;
import org.telegram.tgnet.TLRPC$TL_error;
import org.telegram.tgnet.TLRPC$TL_inputPeerEmpty;
import org.telegram.tgnet.TLRPC$TL_messages_getInlineBotResults;
import org.telegram.tgnet.TLRPC$User;
import org.telegram.tgnet.TLRPC$WebDocument;
import org.telegram.tgnet.TLRPC$messages_BotResults;
import org.telegram.ui.ActionBar.ActionBar;
import org.telegram.ui.ActionBar.ActionBarMenuItem;
import org.telegram.ui.ActionBar.ActionBarMenuSubItem;
import org.telegram.ui.ActionBar.ActionBarPopupWindow;
import org.telegram.ui.ActionBar.AlertDialog;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.ActionBar.ThemeDescription;
import org.telegram.ui.Cells.DividerCell;
import org.telegram.ui.Cells.PhotoAttachPhotoCell;
import org.telegram.ui.Cells.SharedDocumentCell;
import org.telegram.ui.Cells.TextCell;
import org.telegram.ui.Components.AlertsCreator;
import org.telegram.ui.Components.BackupImageView;
import org.telegram.ui.Components.CheckBox2;
import org.telegram.ui.Components.CombinedDrawable;
import org.telegram.ui.Components.EditTextBoldCursor;
import org.telegram.ui.Components.EditTextCaption;
import org.telegram.ui.Components.EditTextEmoji;
import org.telegram.ui.Components.FlickerLoadingView;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.ui.Components.RadialProgressView;
import org.telegram.ui.Components.RecyclerListView;
import org.telegram.ui.Components.RecyclerViewItemRangeSelector;
import org.telegram.ui.Components.SizeNotifierFrameLayout;
import org.telegram.ui.Components.StickerEmptyView;
import org.telegram.ui.PhotoViewer;
/* loaded from: classes3.dex */
public class PhotoPickerActivity extends BaseFragment implements NotificationCenter.NotificationCenterDelegate {
    private int alertOnlyOnce;
    private boolean allowCaption;
    private boolean allowIndices;
    private AnimatorSet animatorSet;
    private CharSequence caption;
    private ChatActivity chatActivity;
    protected EditTextEmoji commentTextView;
    private PhotoPickerActivityDelegate delegate;
    private final String dialogBackgroundKey;
    private StickerEmptyView emptyView;
    private FlickerLoadingView flickerView;
    private final boolean forceDarckTheme;
    protected FrameLayout frameLayout2;
    private int imageReqId;
    private String initialSearchString;
    private boolean isDocumentsPicker;
    private ActionBarMenuSubItem[] itemCells;
    private RecyclerViewItemRangeSelector itemRangeSelector;
    private String lastSearchImageString;
    private String lastSearchString;
    private int lastSearchToken;
    private GridLayoutManager layoutManager;
    private ListAdapter listAdapter;
    private boolean listSort;
    private RecyclerListView listView;
    private int maxSelectedPhotos;
    private String nextImagesSearchOffset;
    private PhotoPickerActivitySearchDelegate searchDelegate;
    private ActionBarMenuItem searchItem;
    private boolean searching;
    private boolean searchingUser;
    private int selectPhotoType;
    private MediaController.AlbumEntry selectedAlbum;
    protected View selectedCountView;
    private HashMap<Object, Object> selectedPhotos;
    private ArrayList<Object> selectedPhotosOrder;
    private final String selectorKey;
    private ActionBarPopupWindow.ActionBarPopupWindowLayout sendPopupLayout;
    private ActionBarPopupWindow sendPopupWindow;
    private boolean sendPressed;
    protected View shadow;
    private boolean shouldSelect;
    private ActionBarMenuSubItem showAsListItem;
    private SizeNotifierFrameLayout sizeNotifierFrameLayout;
    private final String textKey;
    private int type;
    private ImageView writeButton;
    protected FrameLayout writeButtonContainer;
    private Drawable writeButtonDrawable;
    private ArrayList<MediaController.SearchImage> searchResult = new ArrayList<>();
    private HashMap<String, MediaController.SearchImage> searchResultKeys = new HashMap<>();
    private ArrayList<String> recentSearches = new ArrayList<>();
    private boolean imageSearchEndReached = true;
    private boolean allowOrder = true;
    private int itemSize = 100;
    private int itemsPerRow = 3;
    private TextPaint textPaint = new TextPaint(1);
    private RectF rect = new RectF();
    private Paint paint = new Paint(1);
    private boolean needsBottomLayout = true;
    private PhotoViewer.PhotoViewerProvider provider = new AnonymousClass1();

    /* loaded from: classes3.dex */
    public interface PhotoPickerActivityDelegate {

        /* renamed from: org.telegram.ui.PhotoPickerActivity$PhotoPickerActivityDelegate$-CC */
        /* loaded from: classes3.dex */
        public final /* synthetic */ class CC {
            public static void $default$onOpenInPressed(PhotoPickerActivityDelegate photoPickerActivityDelegate) {
            }
        }

        void actionButtonPressed(boolean z, boolean z2, int i);

        void onCaptionChanged(CharSequence charSequence);

        void onOpenInPressed();

        void selectedPhotosChanged();
    }

    /* loaded from: classes3.dex */
    public interface PhotoPickerActivitySearchDelegate {
        void shouldClearRecentSearch();

        void shouldSearchText(String str);
    }

    public static /* synthetic */ boolean lambda$createView$3(View view, MotionEvent motionEvent) {
        return true;
    }

    /* renamed from: org.telegram.ui.PhotoPickerActivity$1 */
    /* loaded from: classes3.dex */
    public class AnonymousClass1 extends PhotoViewer.EmptyPhotoViewerProvider {
        @Override // org.telegram.ui.PhotoViewer.EmptyPhotoViewerProvider, org.telegram.ui.PhotoViewer.PhotoViewerProvider
        public boolean scaleToFill() {
            return false;
        }

        AnonymousClass1() {
            PhotoPickerActivity.this = r1;
        }

        @Override // org.telegram.ui.PhotoViewer.EmptyPhotoViewerProvider, org.telegram.ui.PhotoViewer.PhotoViewerProvider
        public PhotoViewer.PlaceProviderObject getPlaceForPhoto(MessageObject messageObject, TLRPC$FileLocation tLRPC$FileLocation, int i, boolean z) {
            PhotoAttachPhotoCell cellForIndex = PhotoPickerActivity.this.getCellForIndex(i);
            if (cellForIndex != null) {
                BackupImageView imageView = cellForIndex.getImageView();
                int[] iArr = new int[2];
                imageView.getLocationInWindow(iArr);
                PhotoViewer.PlaceProviderObject placeProviderObject = new PhotoViewer.PlaceProviderObject();
                placeProviderObject.viewX = iArr[0];
                placeProviderObject.viewY = iArr[1] - (Build.VERSION.SDK_INT >= 21 ? 0 : AndroidUtilities.statusBarHeight);
                placeProviderObject.parentView = PhotoPickerActivity.this.listView;
                ImageReceiver imageReceiver = imageView.getImageReceiver();
                placeProviderObject.imageReceiver = imageReceiver;
                placeProviderObject.thumb = imageReceiver.getBitmapSafe();
                placeProviderObject.scale = cellForIndex.getScale();
                cellForIndex.showCheck(false);
                return placeProviderObject;
            }
            return null;
        }

        @Override // org.telegram.ui.PhotoViewer.EmptyPhotoViewerProvider, org.telegram.ui.PhotoViewer.PhotoViewerProvider
        public void updatePhotoAtIndex(int i) {
            PhotoAttachPhotoCell cellForIndex = PhotoPickerActivity.this.getCellForIndex(i);
            if (cellForIndex != null) {
                if (PhotoPickerActivity.this.selectedAlbum == null) {
                    cellForIndex.setPhotoEntry((MediaController.SearchImage) PhotoPickerActivity.this.searchResult.get(i), true, false);
                    return;
                }
                BackupImageView imageView = cellForIndex.getImageView();
                imageView.setOrientation(0, true);
                MediaController.PhotoEntry photoEntry = PhotoPickerActivity.this.selectedAlbum.photos.get(i);
                String str = photoEntry.thumbPath;
                if (str != null) {
                    imageView.setImage(str, null, Theme.chat_attachEmptyDrawable);
                } else if (photoEntry.path != null) {
                    imageView.setOrientation(photoEntry.orientation, true);
                    if (photoEntry.isVideo) {
                        imageView.setImage("vthumb://" + photoEntry.imageId + ":" + photoEntry.path, null, Theme.chat_attachEmptyDrawable);
                        return;
                    }
                    imageView.setImage("thumb://" + photoEntry.imageId + ":" + photoEntry.path, null, Theme.chat_attachEmptyDrawable);
                } else {
                    imageView.setImageDrawable(Theme.chat_attachEmptyDrawable);
                }
            }
        }

        @Override // org.telegram.ui.PhotoViewer.EmptyPhotoViewerProvider, org.telegram.ui.PhotoViewer.PhotoViewerProvider
        public boolean allowCaption() {
            return PhotoPickerActivity.this.allowCaption;
        }

        @Override // org.telegram.ui.PhotoViewer.EmptyPhotoViewerProvider, org.telegram.ui.PhotoViewer.PhotoViewerProvider
        public ImageReceiver.BitmapHolder getThumbForPhoto(MessageObject messageObject, TLRPC$FileLocation tLRPC$FileLocation, int i) {
            PhotoAttachPhotoCell cellForIndex = PhotoPickerActivity.this.getCellForIndex(i);
            if (cellForIndex != null) {
                return cellForIndex.getImageView().getImageReceiver().getBitmapSafe();
            }
            return null;
        }

        @Override // org.telegram.ui.PhotoViewer.EmptyPhotoViewerProvider, org.telegram.ui.PhotoViewer.PhotoViewerProvider
        public void willSwitchFromPhoto(MessageObject messageObject, TLRPC$FileLocation tLRPC$FileLocation, int i) {
            int childCount = PhotoPickerActivity.this.listView.getChildCount();
            for (int i2 = 0; i2 < childCount; i2++) {
                View childAt = PhotoPickerActivity.this.listView.getChildAt(i2);
                if (childAt.getTag() != null) {
                    PhotoAttachPhotoCell photoAttachPhotoCell = (PhotoAttachPhotoCell) childAt;
                    int intValue = ((Integer) childAt.getTag()).intValue();
                    if (PhotoPickerActivity.this.selectedAlbum == null ? !(intValue < 0 || intValue >= PhotoPickerActivity.this.searchResult.size()) : !(intValue < 0 || intValue >= PhotoPickerActivity.this.selectedAlbum.photos.size())) {
                        if (intValue == i) {
                            photoAttachPhotoCell.showCheck(true);
                            return;
                        }
                    }
                }
            }
        }

        @Override // org.telegram.ui.PhotoViewer.EmptyPhotoViewerProvider, org.telegram.ui.PhotoViewer.PhotoViewerProvider
        public void willHidePhotoViewer() {
            int childCount = PhotoPickerActivity.this.listView.getChildCount();
            for (int i = 0; i < childCount; i++) {
                View childAt = PhotoPickerActivity.this.listView.getChildAt(i);
                if (childAt instanceof PhotoAttachPhotoCell) {
                    ((PhotoAttachPhotoCell) childAt).showCheck(true);
                }
            }
        }

        @Override // org.telegram.ui.PhotoViewer.EmptyPhotoViewerProvider, org.telegram.ui.PhotoViewer.PhotoViewerProvider
        public boolean isPhotoChecked(int i) {
            return PhotoPickerActivity.this.selectedAlbum != null ? i >= 0 && i < PhotoPickerActivity.this.selectedAlbum.photos.size() && PhotoPickerActivity.this.selectedPhotos.containsKey(Integer.valueOf(PhotoPickerActivity.this.selectedAlbum.photos.get(i).imageId)) : i >= 0 && i < PhotoPickerActivity.this.searchResult.size() && PhotoPickerActivity.this.selectedPhotos.containsKey(((MediaController.SearchImage) PhotoPickerActivity.this.searchResult.get(i)).id);
        }

        @Override // org.telegram.ui.PhotoViewer.EmptyPhotoViewerProvider, org.telegram.ui.PhotoViewer.PhotoViewerProvider
        public int setPhotoUnchecked(Object obj) {
            Object obj2;
            if (obj instanceof MediaController.PhotoEntry) {
                obj2 = Integer.valueOf(((MediaController.PhotoEntry) obj).imageId);
            } else {
                obj2 = obj instanceof MediaController.SearchImage ? ((MediaController.SearchImage) obj).id : null;
            }
            if (obj2 != null && PhotoPickerActivity.this.selectedPhotos.containsKey(obj2)) {
                PhotoPickerActivity.this.selectedPhotos.remove(obj2);
                int indexOf = PhotoPickerActivity.this.selectedPhotosOrder.indexOf(obj2);
                if (indexOf >= 0) {
                    PhotoPickerActivity.this.selectedPhotosOrder.remove(indexOf);
                }
                if (PhotoPickerActivity.this.allowIndices) {
                    PhotoPickerActivity.this.updateCheckedPhotoIndices();
                }
                return indexOf;
            }
            return -1;
        }

        @Override // org.telegram.ui.PhotoViewer.EmptyPhotoViewerProvider, org.telegram.ui.PhotoViewer.PhotoViewerProvider
        public int setPhotoChecked(int i, VideoEditedInfo videoEditedInfo) {
            boolean z;
            int i2;
            int i3 = 1;
            int i4 = -1;
            if (PhotoPickerActivity.this.selectedAlbum != null) {
                if (i < 0 || i >= PhotoPickerActivity.this.selectedAlbum.photos.size()) {
                    return -1;
                }
                MediaController.PhotoEntry photoEntry = PhotoPickerActivity.this.selectedAlbum.photos.get(i);
                i2 = PhotoPickerActivity.this.addToSelectedPhotos(photoEntry, -1);
                if (i2 == -1) {
                    photoEntry.editedInfo = videoEditedInfo;
                    i2 = PhotoPickerActivity.this.selectedPhotosOrder.indexOf(Integer.valueOf(photoEntry.imageId));
                    z = true;
                } else {
                    photoEntry.editedInfo = null;
                    z = false;
                }
            } else if (i < 0 || i >= PhotoPickerActivity.this.searchResult.size()) {
                return -1;
            } else {
                MediaController.SearchImage searchImage = (MediaController.SearchImage) PhotoPickerActivity.this.searchResult.get(i);
                i2 = PhotoPickerActivity.this.addToSelectedPhotos(searchImage, -1);
                if (i2 == -1) {
                    searchImage.editedInfo = videoEditedInfo;
                    i2 = PhotoPickerActivity.this.selectedPhotosOrder.indexOf(searchImage.id);
                    z = true;
                } else {
                    searchImage.editedInfo = null;
                    z = false;
                }
            }
            int childCount = PhotoPickerActivity.this.listView.getChildCount();
            int i5 = 0;
            while (true) {
                if (i5 >= childCount) {
                    break;
                }
                View childAt = PhotoPickerActivity.this.listView.getChildAt(i5);
                if (((Integer) childAt.getTag()).intValue() == i) {
                    PhotoAttachPhotoCell photoAttachPhotoCell = (PhotoAttachPhotoCell) childAt;
                    if (PhotoPickerActivity.this.allowIndices) {
                        i4 = i2;
                    }
                    photoAttachPhotoCell.setChecked(i4, z, false);
                } else {
                    i5++;
                }
            }
            PhotoPickerActivity photoPickerActivity = PhotoPickerActivity.this;
            if (!z) {
                i3 = 2;
            }
            photoPickerActivity.updatePhotosButton(i3);
            PhotoPickerActivity.this.delegate.selectedPhotosChanged();
            return i2;
        }

        @Override // org.telegram.ui.PhotoViewer.EmptyPhotoViewerProvider, org.telegram.ui.PhotoViewer.PhotoViewerProvider
        public boolean cancelButtonPressed() {
            PhotoPickerActivity.this.delegate.actionButtonPressed(true, true, 0);
            PhotoPickerActivity.this.finishFragment();
            return true;
        }

        @Override // org.telegram.ui.PhotoViewer.EmptyPhotoViewerProvider, org.telegram.ui.PhotoViewer.PhotoViewerProvider
        public int getSelectedCount() {
            return PhotoPickerActivity.this.selectedPhotos.size();
        }

        @Override // org.telegram.ui.PhotoViewer.EmptyPhotoViewerProvider, org.telegram.ui.PhotoViewer.PhotoViewerProvider
        public void sendButtonPressed(int i, VideoEditedInfo videoEditedInfo, boolean z, int i2, boolean z2) {
            if (PhotoPickerActivity.this.selectedPhotos.isEmpty()) {
                if (PhotoPickerActivity.this.selectedAlbum != null) {
                    if (i < 0 || i >= PhotoPickerActivity.this.selectedAlbum.photos.size()) {
                        return;
                    }
                    MediaController.PhotoEntry photoEntry = PhotoPickerActivity.this.selectedAlbum.photos.get(i);
                    photoEntry.editedInfo = videoEditedInfo;
                    PhotoPickerActivity.this.addToSelectedPhotos(photoEntry, -1);
                } else if (i < 0 || i >= PhotoPickerActivity.this.searchResult.size()) {
                    return;
                } else {
                    MediaController.SearchImage searchImage = (MediaController.SearchImage) PhotoPickerActivity.this.searchResult.get(i);
                    searchImage.editedInfo = videoEditedInfo;
                    PhotoPickerActivity.this.addToSelectedPhotos(searchImage, -1);
                }
            }
            PhotoPickerActivity.this.sendSelectedPhotos(z, i2);
        }

        @Override // org.telegram.ui.PhotoViewer.EmptyPhotoViewerProvider, org.telegram.ui.PhotoViewer.PhotoViewerProvider
        public ArrayList<Object> getSelectedPhotosOrder() {
            return PhotoPickerActivity.this.selectedPhotosOrder;
        }

        @Override // org.telegram.ui.PhotoViewer.EmptyPhotoViewerProvider, org.telegram.ui.PhotoViewer.PhotoViewerProvider
        public HashMap<Object, Object> getSelectedPhotos() {
            return PhotoPickerActivity.this.selectedPhotos;
        }
    }

    public PhotoPickerActivity(int i, MediaController.AlbumEntry albumEntry, HashMap<Object, Object> hashMap, ArrayList<Object> arrayList, int i2, boolean z, ChatActivity chatActivity, boolean z2) {
        new HashMap();
        this.selectedAlbum = albumEntry;
        this.selectedPhotos = hashMap;
        this.selectedPhotosOrder = arrayList;
        this.type = i;
        this.selectPhotoType = i2;
        this.chatActivity = chatActivity;
        this.allowCaption = z;
        this.forceDarckTheme = z2;
        if (albumEntry == null) {
            loadRecentSearch();
        }
        if (z2) {
            this.dialogBackgroundKey = "voipgroup_dialogBackground";
            this.textKey = "voipgroup_actionBarItems";
            this.selectorKey = "voipgroup_actionBarItemsSelector";
            return;
        }
        this.dialogBackgroundKey = "dialogBackground";
        this.textKey = "dialogTextBlack";
        this.selectorKey = "dialogButtonSelector";
    }

    public void setDocumentsPicker(boolean z) {
        this.isDocumentsPicker = z;
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public boolean onFragmentCreate() {
        NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.closeChats);
        return super.onFragmentCreate();
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public void onFragmentDestroy() {
        NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.closeChats);
        if (this.imageReqId != 0) {
            ConnectionsManager.getInstance(this.currentAccount).cancelRequest(this.imageReqId, true);
            this.imageReqId = 0;
        }
        EditTextEmoji editTextEmoji = this.commentTextView;
        if (editTextEmoji != null) {
            editTextEmoji.onDestroy();
        }
        super.onFragmentDestroy();
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public View createView(Context context) {
        int i;
        this.listSort = false;
        this.actionBar.setBackgroundColor(Theme.getColor(this.dialogBackgroundKey));
        this.actionBar.setTitleColor(Theme.getColor(this.textKey));
        this.actionBar.setItemsColor(Theme.getColor(this.textKey), false);
        this.actionBar.setItemsBackgroundColor(Theme.getColor(this.selectorKey), false);
        this.actionBar.setBackButtonImage(2131165449);
        MediaController.AlbumEntry albumEntry = this.selectedAlbum;
        if (albumEntry != null) {
            this.actionBar.setTitle(albumEntry.bucketName);
        } else {
            int i2 = this.type;
            if (i2 == 0) {
                this.actionBar.setTitle(LocaleController.getString("SearchImagesTitle", 2131628115));
            } else if (i2 == 1) {
                this.actionBar.setTitle(LocaleController.getString("SearchGifsTitle", 2131628112));
            }
        }
        this.actionBar.setActionBarMenuOnItemClick(new AnonymousClass2());
        if (this.isDocumentsPicker) {
            ActionBarMenuItem addItem = this.actionBar.createMenu().addItem(0, 2131165453);
            addItem.setSubMenuDelegate(new AnonymousClass3());
            this.showAsListItem = addItem.addSubItem(1, 2131165786, LocaleController.getString("ShowAsList", 2131628335));
            addItem.addSubItem(2, 2131165831, LocaleController.getString("OpenInExternalApp", 2131627101));
        }
        if (this.selectedAlbum == null) {
            ActionBarMenuItem actionBarMenuItemSearchListener = this.actionBar.createMenu().addItem(0, 2131165456).setIsSearchField(true).setActionBarMenuItemSearchListener(new AnonymousClass4());
            this.searchItem = actionBarMenuItemSearchListener;
            EditTextBoldCursor searchField = actionBarMenuItemSearchListener.getSearchField();
            searchField.setTextColor(Theme.getColor(this.textKey));
            searchField.setCursorColor(Theme.getColor(this.textKey));
            searchField.setHintTextColor(Theme.getColor("chat_messagePanelHint"));
        }
        if (this.selectedAlbum == null) {
            int i3 = this.type;
            if (i3 == 0) {
                this.searchItem.setSearchFieldHint(LocaleController.getString("SearchImagesTitle", 2131628115));
            } else if (i3 == 1) {
                this.searchItem.setSearchFieldHint(LocaleController.getString("SearchGifsTitle", 2131628112));
            }
        }
        AnonymousClass5 anonymousClass5 = new AnonymousClass5(context);
        this.sizeNotifierFrameLayout = anonymousClass5;
        anonymousClass5.setBackgroundColor(Theme.getColor(this.dialogBackgroundKey));
        this.fragmentView = this.sizeNotifierFrameLayout;
        RecyclerListView recyclerListView = new RecyclerListView(context);
        this.listView = recyclerListView;
        recyclerListView.setPadding(AndroidUtilities.dp(6.0f), AndroidUtilities.dp(8.0f), AndroidUtilities.dp(6.0f), AndroidUtilities.dp(50.0f));
        this.listView.setClipToPadding(false);
        this.listView.setHorizontalScrollBarEnabled(false);
        this.listView.setVerticalScrollBarEnabled(false);
        this.listView.setItemAnimator(null);
        this.listView.setLayoutAnimation(null);
        RecyclerListView recyclerListView2 = this.listView;
        AnonymousClass6 anonymousClass6 = new AnonymousClass6(this, context, 4);
        this.layoutManager = anonymousClass6;
        recyclerListView2.setLayoutManager(anonymousClass6);
        this.layoutManager.setSpanSizeLookup(new AnonymousClass7());
        this.sizeNotifierFrameLayout.addView(this.listView, LayoutHelper.createFrame(-1, -1, 51));
        RecyclerListView recyclerListView3 = this.listView;
        ListAdapter listAdapter = new ListAdapter(context);
        this.listAdapter = listAdapter;
        recyclerListView3.setAdapter(listAdapter);
        this.listView.setGlowColor(Theme.getColor(this.dialogBackgroundKey));
        this.listView.setOnItemClickListener(new PhotoPickerActivity$$ExternalSyntheticLambda11(this));
        if (this.maxSelectedPhotos != 1) {
            this.listView.setOnItemLongClickListener(new PhotoPickerActivity$$ExternalSyntheticLambda12(this));
        }
        RecyclerViewItemRangeSelector recyclerViewItemRangeSelector = new RecyclerViewItemRangeSelector(new AnonymousClass8());
        this.itemRangeSelector = recyclerViewItemRangeSelector;
        if (this.maxSelectedPhotos != 1) {
            this.listView.addOnItemTouchListener(recyclerViewItemRangeSelector);
        }
        AnonymousClass9 anonymousClass9 = new AnonymousClass9(this, context, getResourceProvider());
        this.flickerView = anonymousClass9;
        anonymousClass9.setAlpha(0.0f);
        this.flickerView.setVisibility(8);
        StickerEmptyView stickerEmptyView = new StickerEmptyView(context, this.flickerView, 1, getResourceProvider());
        this.emptyView = stickerEmptyView;
        stickerEmptyView.setAnimateLayoutChange(true);
        this.emptyView.title.setTypeface(Typeface.DEFAULT);
        this.emptyView.title.setTextSize(1, 16.0f);
        this.emptyView.title.setTextColor(getThemedColor("windowBackgroundWhiteGrayText"));
        this.emptyView.addView(this.flickerView, 0);
        if (this.selectedAlbum != null) {
            this.emptyView.title.setText(LocaleController.getString("NoPhotos", 2131626847));
        } else {
            this.emptyView.title.setText(LocaleController.getString("NoRecentSearches", 2131626856));
        }
        this.emptyView.showProgress(false, false);
        this.sizeNotifierFrameLayout.addView(this.emptyView, LayoutHelper.createFrame(-1, -1.0f, 51, 0.0f, 126.0f, 0.0f, 0.0f));
        this.listView.setOnScrollListener(new AnonymousClass10());
        if (this.selectedAlbum == null) {
            updateSearchInterface();
        }
        if (this.needsBottomLayout) {
            View view = new View(context);
            this.shadow = view;
            view.setBackgroundResource(2131165447);
            this.shadow.setTranslationY(AndroidUtilities.dp(48.0f));
            this.sizeNotifierFrameLayout.addView(this.shadow, LayoutHelper.createFrame(-1, 3.0f, 83, 0.0f, 0.0f, 0.0f, 48.0f));
            FrameLayout frameLayout = new FrameLayout(context);
            this.frameLayout2 = frameLayout;
            frameLayout.setBackgroundColor(Theme.getColor(this.dialogBackgroundKey));
            this.frameLayout2.setVisibility(4);
            this.frameLayout2.setTranslationY(AndroidUtilities.dp(48.0f));
            this.sizeNotifierFrameLayout.addView(this.frameLayout2, LayoutHelper.createFrame(-1, 48, 83));
            this.frameLayout2.setOnTouchListener(PhotoPickerActivity$$ExternalSyntheticLambda4.INSTANCE);
            EditTextEmoji editTextEmoji = this.commentTextView;
            if (editTextEmoji != null) {
                editTextEmoji.onDestroy();
            }
            this.commentTextView = new EditTextEmoji(context, this.sizeNotifierFrameLayout, null, 1);
            this.commentTextView.setFilters(new InputFilter[]{new InputFilter.LengthFilter(MessagesController.getInstance(UserConfig.selectedAccount).maxCaptionLength)});
            this.commentTextView.setHint(LocaleController.getString("AddCaption", 2131624258));
            this.commentTextView.onResume();
            EditTextCaption editText = this.commentTextView.getEditText();
            editText.setMaxLines(1);
            editText.setSingleLine(true);
            this.frameLayout2.addView(this.commentTextView, LayoutHelper.createFrame(-1, -1.0f, 51, 0.0f, 0.0f, 84.0f, 0.0f));
            CharSequence charSequence = this.caption;
            if (charSequence != null) {
                this.commentTextView.setText(charSequence);
            }
            this.commentTextView.getEditText().addTextChangedListener(new AnonymousClass11());
            AnonymousClass12 anonymousClass12 = new AnonymousClass12(context);
            this.writeButtonContainer = anonymousClass12;
            anonymousClass12.setFocusable(true);
            this.writeButtonContainer.setFocusableInTouchMode(true);
            this.writeButtonContainer.setVisibility(4);
            this.writeButtonContainer.setScaleX(0.2f);
            this.writeButtonContainer.setScaleY(0.2f);
            this.writeButtonContainer.setAlpha(0.0f);
            this.sizeNotifierFrameLayout.addView(this.writeButtonContainer, LayoutHelper.createFrame(60, 60.0f, 85, 0.0f, 0.0f, 12.0f, 10.0f));
            this.writeButton = new ImageView(context);
            int dp = AndroidUtilities.dp(56.0f);
            String str = "dialogFloatingButton";
            int color = Theme.getColor(str);
            int i4 = Build.VERSION.SDK_INT;
            if (i4 >= 21) {
                str = "dialogFloatingButtonPressed";
            }
            this.writeButtonDrawable = Theme.createSimpleSelectorCircleDrawable(dp, color, Theme.getColor(str));
            if (i4 < 21) {
                Drawable mutate = context.getResources().getDrawable(2131165415).mutate();
                mutate.setColorFilter(new PorterDuffColorFilter(-16777216, PorterDuff.Mode.MULTIPLY));
                CombinedDrawable combinedDrawable = new CombinedDrawable(mutate, this.writeButtonDrawable, 0, 0);
                combinedDrawable.setIconSize(AndroidUtilities.dp(56.0f), AndroidUtilities.dp(56.0f));
                this.writeButtonDrawable = combinedDrawable;
            }
            this.writeButton.setBackgroundDrawable(this.writeButtonDrawable);
            this.writeButton.setImageResource(2131165264);
            this.writeButton.setImportantForAccessibility(2);
            this.writeButton.setColorFilter(new PorterDuffColorFilter(Theme.getColor("dialogFloatingIcon"), PorterDuff.Mode.MULTIPLY));
            this.writeButton.setScaleType(ImageView.ScaleType.CENTER);
            if (i4 >= 21) {
                this.writeButton.setOutlineProvider(new AnonymousClass13(this));
            }
            this.writeButtonContainer.addView(this.writeButton, LayoutHelper.createFrame(i4 >= 21 ? 56 : 60, i4 >= 21 ? 56.0f : 60.0f, 51, i4 >= 21 ? 2.0f : 0.0f, 0.0f, 0.0f, 0.0f));
            this.writeButton.setOnClickListener(new PhotoPickerActivity$$ExternalSyntheticLambda1(this));
            this.writeButton.setOnLongClickListener(new PhotoPickerActivity$$ExternalSyntheticLambda3(this));
            this.textPaint.setTextSize(AndroidUtilities.dp(12.0f));
            this.textPaint.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
            AnonymousClass15 anonymousClass15 = new AnonymousClass15(context);
            this.selectedCountView = anonymousClass15;
            anonymousClass15.setAlpha(0.0f);
            this.selectedCountView.setScaleX(0.2f);
            this.selectedCountView.setScaleY(0.2f);
            this.sizeNotifierFrameLayout.addView(this.selectedCountView, LayoutHelper.createFrame(42, 24.0f, 85, 0.0f, 0.0f, -2.0f, 9.0f));
            if (this.selectPhotoType != PhotoAlbumPickerActivity.SELECT_TYPE_ALL) {
                this.commentTextView.setVisibility(8);
            }
        }
        this.allowIndices = (this.selectedAlbum != null || (i = this.type) == 0 || i == 1) && this.allowOrder;
        this.listView.setEmptyView(this.emptyView);
        this.listView.setAnimateEmptyView(true, 0);
        updatePhotosButton(0);
        return this.fragmentView;
    }

    /* renamed from: org.telegram.ui.PhotoPickerActivity$2 */
    /* loaded from: classes3.dex */
    class AnonymousClass2 extends ActionBar.ActionBarMenuOnItemClick {
        AnonymousClass2() {
            PhotoPickerActivity.this = r1;
        }

        @Override // org.telegram.ui.ActionBar.ActionBar.ActionBarMenuOnItemClick
        public void onItemClick(int i) {
            if (i == -1) {
                PhotoPickerActivity.this.finishFragment();
            } else if (i != 1) {
                if (i != 2) {
                    return;
                }
                if (PhotoPickerActivity.this.delegate != null) {
                    PhotoPickerActivity.this.delegate.onOpenInPressed();
                }
                PhotoPickerActivity.this.finishFragment();
            } else {
                PhotoPickerActivity photoPickerActivity = PhotoPickerActivity.this;
                photoPickerActivity.listSort = true ^ photoPickerActivity.listSort;
                if (PhotoPickerActivity.this.listSort) {
                    PhotoPickerActivity.this.listView.setPadding(0, 0, 0, AndroidUtilities.dp(48.0f));
                } else {
                    PhotoPickerActivity.this.listView.setPadding(AndroidUtilities.dp(6.0f), AndroidUtilities.dp(8.0f), AndroidUtilities.dp(6.0f), AndroidUtilities.dp(50.0f));
                }
                PhotoPickerActivity.this.listView.stopScroll();
                PhotoPickerActivity.this.layoutManager.scrollToPositionWithOffset(0, 0);
                PhotoPickerActivity.this.listAdapter.notifyDataSetChanged();
            }
        }
    }

    /* renamed from: org.telegram.ui.PhotoPickerActivity$3 */
    /* loaded from: classes3.dex */
    class AnonymousClass3 implements ActionBarMenuItem.ActionBarSubMenuItemDelegate {
        @Override // org.telegram.ui.ActionBar.ActionBarMenuItem.ActionBarSubMenuItemDelegate
        public void onHideSubMenu() {
        }

        AnonymousClass3() {
            PhotoPickerActivity.this = r1;
        }

        @Override // org.telegram.ui.ActionBar.ActionBarMenuItem.ActionBarSubMenuItemDelegate
        public void onShowSubMenu() {
            String str;
            int i;
            ActionBarMenuSubItem actionBarMenuSubItem = PhotoPickerActivity.this.showAsListItem;
            if (PhotoPickerActivity.this.listSort) {
                i = 2131628334;
                str = "ShowAsGrid";
            } else {
                i = 2131628335;
                str = "ShowAsList";
            }
            actionBarMenuSubItem.setText(LocaleController.getString(str, i));
            PhotoPickerActivity.this.showAsListItem.setIcon(PhotoPickerActivity.this.listSort ? 2131165798 : 2131165786);
        }
    }

    /* renamed from: org.telegram.ui.PhotoPickerActivity$4 */
    /* loaded from: classes3.dex */
    public class AnonymousClass4 extends ActionBarMenuItem.ActionBarMenuItemSearchListener {
        Runnable updateSearch = new PhotoPickerActivity$4$$ExternalSyntheticLambda0(this);

        @Override // org.telegram.ui.ActionBar.ActionBarMenuItem.ActionBarMenuItemSearchListener
        public void onSearchExpand() {
        }

        AnonymousClass4() {
            PhotoPickerActivity.this = r1;
        }

        @Override // org.telegram.ui.ActionBar.ActionBarMenuItem.ActionBarMenuItemSearchListener
        public boolean canCollapseSearch() {
            PhotoPickerActivity.this.finishFragment();
            return false;
        }

        @Override // org.telegram.ui.ActionBar.ActionBarMenuItem.ActionBarMenuItemSearchListener
        public void onTextChanged(EditText editText) {
            if (editText.getText().length() == 0) {
                PhotoPickerActivity.this.searchResult.clear();
                PhotoPickerActivity.this.searchResultKeys.clear();
                PhotoPickerActivity.this.lastSearchString = null;
                PhotoPickerActivity.this.imageSearchEndReached = true;
                PhotoPickerActivity.this.searching = false;
                if (PhotoPickerActivity.this.imageReqId != 0) {
                    ConnectionsManager.getInstance(((BaseFragment) PhotoPickerActivity.this).currentAccount).cancelRequest(PhotoPickerActivity.this.imageReqId, true);
                    PhotoPickerActivity.this.imageReqId = 0;
                }
                PhotoPickerActivity.this.emptyView.title.setText(LocaleController.getString("NoRecentSearches", 2131626856));
                PhotoPickerActivity.this.emptyView.showProgress(false);
                PhotoPickerActivity.this.updateSearchInterface();
                return;
            }
            AndroidUtilities.cancelRunOnUIThread(this.updateSearch);
            AndroidUtilities.runOnUIThread(this.updateSearch, 1200L);
        }

        public /* synthetic */ void lambda$$0() {
            PhotoPickerActivity photoPickerActivity = PhotoPickerActivity.this;
            photoPickerActivity.processSearch(photoPickerActivity.searchItem.getSearchField());
        }

        @Override // org.telegram.ui.ActionBar.ActionBarMenuItem.ActionBarMenuItemSearchListener
        public void onSearchPressed(EditText editText) {
            PhotoPickerActivity.this.processSearch(editText);
        }
    }

    /* renamed from: org.telegram.ui.PhotoPickerActivity$5 */
    /* loaded from: classes3.dex */
    public class AnonymousClass5 extends SizeNotifierFrameLayout {
        private boolean ignoreLayout;
        private int lastItemSize;
        private int lastNotifyWidth;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass5(Context context) {
            super(context);
            PhotoPickerActivity.this = r1;
        }

        @Override // android.widget.FrameLayout, android.view.View
        protected void onMeasure(int i, int i2) {
            int size = View.MeasureSpec.getSize(i2);
            int size2 = View.MeasureSpec.getSize(i);
            if (AndroidUtilities.isTablet()) {
                PhotoPickerActivity.this.itemsPerRow = 4;
            } else {
                Point point = AndroidUtilities.displaySize;
                if (point.x > point.y) {
                    PhotoPickerActivity.this.itemsPerRow = 4;
                } else {
                    PhotoPickerActivity.this.itemsPerRow = 3;
                }
            }
            this.ignoreLayout = true;
            PhotoPickerActivity.this.itemSize = ((size2 - AndroidUtilities.dp(12.0f)) - AndroidUtilities.dp(10.0f)) / PhotoPickerActivity.this.itemsPerRow;
            if (this.lastItemSize != PhotoPickerActivity.this.itemSize) {
                this.lastItemSize = PhotoPickerActivity.this.itemSize;
                AndroidUtilities.runOnUIThread(new PhotoPickerActivity$5$$ExternalSyntheticLambda0(this));
            }
            if (PhotoPickerActivity.this.listSort) {
                PhotoPickerActivity.this.layoutManager.setSpanCount(1);
            } else {
                PhotoPickerActivity.this.layoutManager.setSpanCount((PhotoPickerActivity.this.itemSize * PhotoPickerActivity.this.itemsPerRow) + (AndroidUtilities.dp(5.0f) * (PhotoPickerActivity.this.itemsPerRow - 1)));
            }
            this.ignoreLayout = false;
            onMeasureInternal(i, View.MeasureSpec.makeMeasureSpec(size, 1073741824));
        }

        public /* synthetic */ void lambda$onMeasure$0() {
            PhotoPickerActivity.this.listAdapter.notifyDataSetChanged();
        }

        /* JADX WARN: Removed duplicated region for block: B:30:0x008c  */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        private void onMeasureInternal(int i, int i2) {
            int i3;
            int i4;
            int childCount;
            int i5;
            EditTextEmoji editTextEmoji;
            EditTextEmoji editTextEmoji2;
            int size = View.MeasureSpec.getSize(i);
            int size2 = View.MeasureSpec.getSize(i2);
            setMeasuredDimension(size, size2);
            int measureKeyboardHeight = measureKeyboardHeight();
            if ((SharedConfig.smoothKeyboard ? 0 : measureKeyboardHeight) <= AndroidUtilities.dp(20.0f) && !AndroidUtilities.isInMultiwindow) {
                PhotoPickerActivity photoPickerActivity = PhotoPickerActivity.this;
                if (photoPickerActivity.commentTextView != null && photoPickerActivity.frameLayout2.getParent() == this) {
                    int emojiPadding = size2 - PhotoPickerActivity.this.commentTextView.getEmojiPadding();
                    i3 = emojiPadding;
                    i4 = View.MeasureSpec.makeMeasureSpec(emojiPadding, 1073741824);
                    if (measureKeyboardHeight > AndroidUtilities.dp(20.0f) && (editTextEmoji2 = PhotoPickerActivity.this.commentTextView) != null) {
                        this.ignoreLayout = true;
                        editTextEmoji2.hideEmojiView();
                        this.ignoreLayout = false;
                    }
                    if (SharedConfig.smoothKeyboard && (editTextEmoji = PhotoPickerActivity.this.commentTextView) != null && editTextEmoji.isPopupShowing()) {
                        ((BaseFragment) PhotoPickerActivity.this).fragmentView.setTranslationY(0.0f);
                        PhotoPickerActivity.this.listView.setTranslationY(0.0f);
                        PhotoPickerActivity.this.emptyView.setTranslationY(0.0f);
                    }
                    childCount = getChildCount();
                    for (i5 = 0; i5 < childCount; i5++) {
                        View childAt = getChildAt(i5);
                        if (childAt != null && childAt.getVisibility() != 8) {
                            EditTextEmoji editTextEmoji3 = PhotoPickerActivity.this.commentTextView;
                            if (editTextEmoji3 != null && editTextEmoji3.isPopupView(childAt)) {
                                if (AndroidUtilities.isInMultiwindow || AndroidUtilities.isTablet()) {
                                    if (AndroidUtilities.isTablet()) {
                                        childAt.measure(View.MeasureSpec.makeMeasureSpec(size, 1073741824), View.MeasureSpec.makeMeasureSpec(Math.min(AndroidUtilities.dp(AndroidUtilities.isTablet() ? 200.0f : 320.0f), (i3 - AndroidUtilities.statusBarHeight) + getPaddingTop()), 1073741824));
                                    } else {
                                        childAt.measure(View.MeasureSpec.makeMeasureSpec(size, 1073741824), View.MeasureSpec.makeMeasureSpec((i3 - AndroidUtilities.statusBarHeight) + getPaddingTop(), 1073741824));
                                    }
                                } else {
                                    childAt.measure(View.MeasureSpec.makeMeasureSpec(size, 1073741824), View.MeasureSpec.makeMeasureSpec(childAt.getLayoutParams().height, 1073741824));
                                }
                            } else {
                                measureChildWithMargins(childAt, i, 0, i4, 0);
                            }
                        }
                    }
                }
            }
            i4 = i2;
            i3 = size2;
            if (measureKeyboardHeight > AndroidUtilities.dp(20.0f)) {
                this.ignoreLayout = true;
                editTextEmoji2.hideEmojiView();
                this.ignoreLayout = false;
            }
            if (SharedConfig.smoothKeyboard) {
                ((BaseFragment) PhotoPickerActivity.this).fragmentView.setTranslationY(0.0f);
                PhotoPickerActivity.this.listView.setTranslationY(0.0f);
                PhotoPickerActivity.this.emptyView.setTranslationY(0.0f);
            }
            childCount = getChildCount();
            while (i5 < childCount) {
            }
        }

        /* JADX WARN: Removed duplicated region for block: B:45:0x00c2  */
        /* JADX WARN: Removed duplicated region for block: B:52:0x00dc  */
        /* JADX WARN: Removed duplicated region for block: B:56:0x00ef  */
        /* JADX WARN: Removed duplicated region for block: B:60:0x00fb  */
        /* JADX WARN: Removed duplicated region for block: B:61:0x0104  */
        @Override // org.telegram.ui.Components.SizeNotifierFrameLayout, android.widget.FrameLayout, android.view.ViewGroup, android.view.View
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public void onLayout(boolean z, int i, int i2, int i3, int i4) {
            int i5;
            int i6;
            EditTextEmoji editTextEmoji;
            int i7;
            int i8;
            int i9;
            int i10;
            int i11;
            int i12;
            int i13 = i3 - i;
            if (this.lastNotifyWidth != i13) {
                this.lastNotifyWidth = i13;
                if (PhotoPickerActivity.this.listAdapter != null) {
                    PhotoPickerActivity.this.listAdapter.notifyDataSetChanged();
                }
                if (PhotoPickerActivity.this.sendPopupWindow != null && PhotoPickerActivity.this.sendPopupWindow.isShowing()) {
                    PhotoPickerActivity.this.sendPopupWindow.dismiss();
                }
            }
            int childCount = getChildCount();
            int measureKeyboardHeight = SharedConfig.smoothKeyboard ? 0 : measureKeyboardHeight();
            PhotoPickerActivity photoPickerActivity = PhotoPickerActivity.this;
            int emojiPadding = (photoPickerActivity.commentTextView == null || photoPickerActivity.frameLayout2.getParent() != this || measureKeyboardHeight > AndroidUtilities.dp(20.0f) || AndroidUtilities.isInMultiwindow || AndroidUtilities.isTablet()) ? 0 : PhotoPickerActivity.this.commentTextView.getEmojiPadding();
            setBottomClip(emojiPadding);
            for (int i14 = 0; i14 < childCount; i14++) {
                View childAt = getChildAt(i14);
                if (childAt.getVisibility() != 8) {
                    FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) childAt.getLayoutParams();
                    int measuredWidth = childAt.getMeasuredWidth();
                    int measuredHeight = childAt.getMeasuredHeight();
                    int i15 = layoutParams.gravity;
                    if (i15 == -1) {
                        i15 = 51;
                    }
                    int i16 = i15 & 7;
                    int i17 = i15 & 112;
                    int i18 = i16 & 7;
                    if (i18 == 1) {
                        i12 = ((i13 - measuredWidth) / 2) + layoutParams.leftMargin;
                        i11 = layoutParams.rightMargin;
                    } else if (i18 == 5) {
                        i12 = (i13 - measuredWidth) - layoutParams.rightMargin;
                        i11 = getPaddingRight();
                    } else {
                        i5 = layoutParams.leftMargin + getPaddingLeft();
                        if (i17 != 16) {
                            i9 = ((((i4 - emojiPadding) - i2) - measuredHeight) / 2) + layoutParams.topMargin;
                            i10 = layoutParams.bottomMargin;
                        } else {
                            if (i17 == 48) {
                                i6 = layoutParams.topMargin + getPaddingTop();
                            } else if (i17 == 80) {
                                i9 = ((i4 - emojiPadding) - i2) - measuredHeight;
                                i10 = layoutParams.bottomMargin;
                            } else {
                                i6 = layoutParams.topMargin;
                            }
                            editTextEmoji = PhotoPickerActivity.this.commentTextView;
                            if (editTextEmoji != null && editTextEmoji.isPopupView(childAt)) {
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
                        }
                        i6 = i9 - i10;
                        editTextEmoji = PhotoPickerActivity.this.commentTextView;
                        if (editTextEmoji != null) {
                            if (!AndroidUtilities.isTablet()) {
                            }
                            i6 = i8 - i7;
                        }
                        childAt.layout(i5, i6, measuredWidth + i5, measuredHeight + i6);
                    }
                    i5 = i12 - i11;
                    if (i17 != 16) {
                    }
                    i6 = i9 - i10;
                    editTextEmoji = PhotoPickerActivity.this.commentTextView;
                    if (editTextEmoji != null) {
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

    /* renamed from: org.telegram.ui.PhotoPickerActivity$6 */
    /* loaded from: classes3.dex */
    class AnonymousClass6 extends GridLayoutManager {
        @Override // androidx.recyclerview.widget.GridLayoutManager, androidx.recyclerview.widget.LinearLayoutManager, androidx.recyclerview.widget.RecyclerView.LayoutManager
        public boolean supportsPredictiveItemAnimations() {
            return false;
        }

        AnonymousClass6(PhotoPickerActivity photoPickerActivity, Context context, int i) {
            super(context, i);
        }
    }

    /* renamed from: org.telegram.ui.PhotoPickerActivity$7 */
    /* loaded from: classes3.dex */
    class AnonymousClass7 extends GridLayoutManager.SpanSizeLookup {
        AnonymousClass7() {
            PhotoPickerActivity.this = r1;
        }

        @Override // androidx.recyclerview.widget.GridLayoutManager.SpanSizeLookup
        public int getSpanSize(int i) {
            if (PhotoPickerActivity.this.listAdapter.getItemViewType(i) == 1 || PhotoPickerActivity.this.listSort || (PhotoPickerActivity.this.selectedAlbum == null && TextUtils.isEmpty(PhotoPickerActivity.this.lastSearchString))) {
                return PhotoPickerActivity.this.layoutManager.getSpanCount();
            }
            return PhotoPickerActivity.this.itemSize + (i % PhotoPickerActivity.this.itemsPerRow != PhotoPickerActivity.this.itemsPerRow - 1 ? AndroidUtilities.dp(5.0f) : 0);
        }
    }

    public /* synthetic */ void lambda$createView$1(View view, int i) {
        ArrayList<Object> arrayList;
        int i2;
        if (this.selectedAlbum == null && this.searchResult.isEmpty()) {
            if (i < this.recentSearches.size()) {
                String str = this.recentSearches.get(i);
                PhotoPickerActivitySearchDelegate photoPickerActivitySearchDelegate = this.searchDelegate;
                if (photoPickerActivitySearchDelegate != null) {
                    photoPickerActivitySearchDelegate.shouldSearchText(str);
                    return;
                }
                this.searchItem.getSearchField().setText(str);
                this.searchItem.getSearchField().setSelection(str.length());
                processSearch(this.searchItem.getSearchField());
                return;
            } else if (i != this.recentSearches.size() + 1) {
                return;
            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(getParentActivity());
                builder.setTitle(LocaleController.getString("ClearSearchAlertTitle", 2131625161));
                builder.setMessage(LocaleController.getString("ClearSearchAlert", 2131625154));
                builder.setPositiveButton(LocaleController.getString("ClearButton", 2131625132).toUpperCase(), new PhotoPickerActivity$$ExternalSyntheticLambda0(this));
                builder.setNegativeButton(LocaleController.getString("Cancel", 2131624819), null);
                AlertDialog create = builder.create();
                showDialog(create);
                TextView textView = (TextView) create.getButton(-1);
                if (textView == null) {
                    return;
                }
                textView.setTextColor(Theme.getColor("dialogTextRed2"));
                return;
            }
        }
        MediaController.AlbumEntry albumEntry = this.selectedAlbum;
        if (albumEntry != null) {
            arrayList = albumEntry.photos;
        } else {
            arrayList = this.searchResult;
        }
        ArrayList<Object> arrayList2 = arrayList;
        if (i < 0 || i >= arrayList2.size()) {
            return;
        }
        ActionBarMenuItem actionBarMenuItem = this.searchItem;
        if (actionBarMenuItem != null) {
            AndroidUtilities.hideKeyboard(actionBarMenuItem.getSearchField());
        }
        if (this.listSort) {
            onListItemClick(view, arrayList2.get(i));
            return;
        }
        int i3 = this.selectPhotoType;
        if (i3 == PhotoAlbumPickerActivity.SELECT_TYPE_AVATAR || i3 == PhotoAlbumPickerActivity.SELECT_TYPE_AVATAR_VIDEO) {
            i2 = 1;
        } else if (i3 == PhotoAlbumPickerActivity.SELECT_TYPE_WALLPAPER) {
            i2 = 3;
        } else if (i3 == PhotoAlbumPickerActivity.SELECT_TYPE_QR) {
            i2 = 10;
        } else {
            i2 = this.chatActivity == null ? 4 : 0;
        }
        PhotoViewer.getInstance().setParentActivity(getParentActivity());
        PhotoViewer.getInstance().setMaxSelectedPhotos(this.maxSelectedPhotos, this.allowOrder);
        PhotoViewer.getInstance().openPhotoForSelect(arrayList2, i, i2, this.isDocumentsPicker, this.provider, this.chatActivity);
    }

    public /* synthetic */ void lambda$createView$0(DialogInterface dialogInterface, int i) {
        PhotoPickerActivitySearchDelegate photoPickerActivitySearchDelegate = this.searchDelegate;
        if (photoPickerActivitySearchDelegate != null) {
            photoPickerActivitySearchDelegate.shouldClearRecentSearch();
        } else {
            clearRecentSearch();
        }
    }

    public /* synthetic */ boolean lambda$createView$2(View view, int i) {
        if (this.listSort) {
            onListItemClick(view, this.selectedAlbum.photos.get(i));
            return true;
        } else if (!(view instanceof PhotoAttachPhotoCell)) {
            return false;
        } else {
            RecyclerViewItemRangeSelector recyclerViewItemRangeSelector = this.itemRangeSelector;
            boolean z = !((PhotoAttachPhotoCell) view).isChecked();
            this.shouldSelect = z;
            recyclerViewItemRangeSelector.setIsActive(view, true, i, z);
            return false;
        }
    }

    /* renamed from: org.telegram.ui.PhotoPickerActivity$8 */
    /* loaded from: classes3.dex */
    class AnonymousClass8 implements RecyclerViewItemRangeSelector.RecyclerViewItemRangeSelectorDelegate {
        AnonymousClass8() {
            PhotoPickerActivity.this = r1;
        }

        @Override // org.telegram.ui.Components.RecyclerViewItemRangeSelector.RecyclerViewItemRangeSelectorDelegate
        public void setSelected(View view, int i, boolean z) {
            if (z != PhotoPickerActivity.this.shouldSelect || !(view instanceof PhotoAttachPhotoCell)) {
                return;
            }
            ((PhotoAttachPhotoCell) view).callDelegate();
        }

        @Override // org.telegram.ui.Components.RecyclerViewItemRangeSelector.RecyclerViewItemRangeSelectorDelegate
        public boolean isSelected(int i) {
            Object obj;
            if (PhotoPickerActivity.this.selectedAlbum != null) {
                obj = Integer.valueOf(PhotoPickerActivity.this.selectedAlbum.photos.get(i).imageId);
            } else {
                obj = ((MediaController.SearchImage) PhotoPickerActivity.this.searchResult.get(i)).id;
            }
            return PhotoPickerActivity.this.selectedPhotos.containsKey(obj);
        }

        @Override // org.telegram.ui.Components.RecyclerViewItemRangeSelector.RecyclerViewItemRangeSelectorDelegate
        public boolean isIndexSelectable(int i) {
            return PhotoPickerActivity.this.listAdapter.getItemViewType(i) == 0;
        }

        @Override // org.telegram.ui.Components.RecyclerViewItemRangeSelector.RecyclerViewItemRangeSelectorDelegate
        public void onStartStopSelection(boolean z) {
            PhotoPickerActivity.this.alertOnlyOnce = z ? 1 : 0;
            if (z) {
                ((BaseFragment) PhotoPickerActivity.this).parentLayout.requestDisallowInterceptTouchEvent(true);
            }
            PhotoPickerActivity.this.listView.hideSelector(true);
        }
    }

    /* renamed from: org.telegram.ui.PhotoPickerActivity$9 */
    /* loaded from: classes3.dex */
    class AnonymousClass9 extends FlickerLoadingView {
        @Override // org.telegram.ui.Components.FlickerLoadingView
        public int getColumnsCount() {
            return 3;
        }

        @Override // org.telegram.ui.Components.FlickerLoadingView
        public int getViewType() {
            return 2;
        }

        AnonymousClass9(PhotoPickerActivity photoPickerActivity, Context context, Theme.ResourcesProvider resourcesProvider) {
            super(context, resourcesProvider);
        }
    }

    /* renamed from: org.telegram.ui.PhotoPickerActivity$10 */
    /* loaded from: classes3.dex */
    class AnonymousClass10 extends RecyclerView.OnScrollListener {
        AnonymousClass10() {
            PhotoPickerActivity.this = r1;
        }

        @Override // androidx.recyclerview.widget.RecyclerView.OnScrollListener
        public void onScrollStateChanged(RecyclerView recyclerView, int i) {
            if (i == 1) {
                AndroidUtilities.hideKeyboard(PhotoPickerActivity.this.getParentActivity().getCurrentFocus());
            }
        }

        @Override // androidx.recyclerview.widget.RecyclerView.OnScrollListener
        public void onScrolled(RecyclerView recyclerView, int i, int i2) {
            if (PhotoPickerActivity.this.selectedAlbum == null) {
                int findFirstVisibleItemPosition = PhotoPickerActivity.this.layoutManager.findFirstVisibleItemPosition();
                boolean z = false;
                int abs = findFirstVisibleItemPosition == -1 ? 0 : Math.abs(PhotoPickerActivity.this.layoutManager.findLastVisibleItemPosition() - findFirstVisibleItemPosition) + 1;
                if (abs <= 0 || findFirstVisibleItemPosition + abs <= PhotoPickerActivity.this.layoutManager.getItemCount() - 2 || PhotoPickerActivity.this.searching || PhotoPickerActivity.this.imageSearchEndReached) {
                    return;
                }
                PhotoPickerActivity photoPickerActivity = PhotoPickerActivity.this;
                if (photoPickerActivity.type == 1) {
                    z = true;
                }
                photoPickerActivity.searchImages(z, PhotoPickerActivity.this.lastSearchString, PhotoPickerActivity.this.nextImagesSearchOffset, true);
            }
        }
    }

    /* renamed from: org.telegram.ui.PhotoPickerActivity$11 */
    /* loaded from: classes3.dex */
    class AnonymousClass11 implements TextWatcher {
        @Override // android.text.TextWatcher
        public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
        }

        @Override // android.text.TextWatcher
        public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
        }

        AnonymousClass11() {
            PhotoPickerActivity.this = r1;
        }

        @Override // android.text.TextWatcher
        public void afterTextChanged(Editable editable) {
            if (PhotoPickerActivity.this.delegate != null) {
                PhotoPickerActivity.this.delegate.onCaptionChanged(editable);
            }
        }
    }

    /* renamed from: org.telegram.ui.PhotoPickerActivity$12 */
    /* loaded from: classes3.dex */
    class AnonymousClass12 extends FrameLayout {
        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass12(Context context) {
            super(context);
            PhotoPickerActivity.this = r1;
        }

        @Override // android.view.View
        public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo accessibilityNodeInfo) {
            super.onInitializeAccessibilityNodeInfo(accessibilityNodeInfo);
            accessibilityNodeInfo.setText(LocaleController.formatPluralString("AccDescrSendPhotos", PhotoPickerActivity.this.selectedPhotos.size(), new Object[0]));
            accessibilityNodeInfo.setClassName(Button.class.getName());
            accessibilityNodeInfo.setLongClickable(true);
            accessibilityNodeInfo.setClickable(true);
        }
    }

    /* renamed from: org.telegram.ui.PhotoPickerActivity$13 */
    /* loaded from: classes3.dex */
    class AnonymousClass13 extends ViewOutlineProvider {
        AnonymousClass13(PhotoPickerActivity photoPickerActivity) {
        }

        @Override // android.view.ViewOutlineProvider
        @SuppressLint({"NewApi"})
        public void getOutline(View view, Outline outline) {
            outline.setOval(0, 0, AndroidUtilities.dp(56.0f), AndroidUtilities.dp(56.0f));
        }
    }

    public /* synthetic */ void lambda$createView$4(View view) {
        ChatActivity chatActivity = this.chatActivity;
        if (chatActivity != null && chatActivity.isInScheduleMode()) {
            AlertsCreator.createScheduleDatePickerDialog(getParentActivity(), this.chatActivity.getDialogId(), new PhotoPickerActivity$$ExternalSyntheticLambda10(this));
        } else {
            sendSelectedPhotos(true, 0);
        }
    }

    public /* synthetic */ boolean lambda$createView$7(View view) {
        ChatActivity chatActivity = this.chatActivity;
        if (chatActivity != null && this.maxSelectedPhotos != 1) {
            chatActivity.getCurrentChat();
            TLRPC$User currentUser = this.chatActivity.getCurrentUser();
            if (this.sendPopupLayout == null) {
                ActionBarPopupWindow.ActionBarPopupWindowLayout actionBarPopupWindowLayout = new ActionBarPopupWindow.ActionBarPopupWindowLayout(getParentActivity());
                this.sendPopupLayout = actionBarPopupWindowLayout;
                actionBarPopupWindowLayout.setAnimationEnabled(false);
                this.sendPopupLayout.setOnTouchListener(new AnonymousClass14());
                this.sendPopupLayout.setDispatchKeyEventListener(new PhotoPickerActivity$$ExternalSyntheticLambda9(this));
                this.sendPopupLayout.setShownFromBottom(false);
                this.itemCells = new ActionBarMenuSubItem[2];
                int i = 0;
                while (i < 2) {
                    if ((i != 0 || this.chatActivity.canScheduleMessage()) && (i != 1 || !UserObject.isUserSelf(currentUser))) {
                        this.itemCells[i] = new ActionBarMenuSubItem(getParentActivity(), i == 0, i == 1);
                        if (i == 0) {
                            if (UserObject.isUserSelf(currentUser)) {
                                this.itemCells[i].setTextAndIcon(LocaleController.getString("SetReminder", 2131628248), 2131165662);
                            } else {
                                this.itemCells[i].setTextAndIcon(LocaleController.getString("ScheduleMessage", 2131628082), 2131165662);
                            }
                        } else {
                            this.itemCells[i].setTextAndIcon(LocaleController.getString("SendWithoutSound", 2131628212), 2131165539);
                        }
                        this.itemCells[i].setMinimumWidth(AndroidUtilities.dp(196.0f));
                        this.sendPopupLayout.addView((View) this.itemCells[i], LayoutHelper.createLinear(-1, 48));
                        this.itemCells[i].setOnClickListener(new PhotoPickerActivity$$ExternalSyntheticLambda2(this, i));
                    }
                    i++;
                }
                this.sendPopupLayout.setupRadialSelectors(Theme.getColor(this.selectorKey));
                ActionBarPopupWindow actionBarPopupWindow = new ActionBarPopupWindow(this.sendPopupLayout, -2, -2);
                this.sendPopupWindow = actionBarPopupWindow;
                actionBarPopupWindow.setAnimationEnabled(false);
                this.sendPopupWindow.setAnimationStyle(2131689481);
                this.sendPopupWindow.setOutsideTouchable(true);
                this.sendPopupWindow.setClippingEnabled(true);
                this.sendPopupWindow.setInputMethodMode(2);
                this.sendPopupWindow.setSoftInputMode(0);
                this.sendPopupWindow.getContentView().setFocusableInTouchMode(true);
            }
            this.sendPopupLayout.measure(View.MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(1000.0f), Integer.MIN_VALUE), View.MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(1000.0f), Integer.MIN_VALUE));
            this.sendPopupWindow.setFocusable(true);
            int[] iArr = new int[2];
            view.getLocationInWindow(iArr);
            this.sendPopupWindow.showAtLocation(view, 51, ((iArr[0] + view.getMeasuredWidth()) - this.sendPopupLayout.getMeasuredWidth()) + AndroidUtilities.dp(8.0f), (iArr[1] - this.sendPopupLayout.getMeasuredHeight()) - AndroidUtilities.dp(2.0f));
            this.sendPopupWindow.dimBehind();
            view.performHapticFeedback(3, 2);
        }
        return false;
    }

    /* renamed from: org.telegram.ui.PhotoPickerActivity$14 */
    /* loaded from: classes3.dex */
    public class AnonymousClass14 implements View.OnTouchListener {
        private Rect popupRect = new Rect();

        AnonymousClass14() {
            PhotoPickerActivity.this = r1;
        }

        @Override // android.view.View.OnTouchListener
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (motionEvent.getActionMasked() != 0 || PhotoPickerActivity.this.sendPopupWindow == null || !PhotoPickerActivity.this.sendPopupWindow.isShowing()) {
                return false;
            }
            view.getHitRect(this.popupRect);
            if (this.popupRect.contains((int) motionEvent.getX(), (int) motionEvent.getY())) {
                return false;
            }
            PhotoPickerActivity.this.sendPopupWindow.dismiss();
            return false;
        }
    }

    public /* synthetic */ void lambda$createView$5(KeyEvent keyEvent) {
        ActionBarPopupWindow actionBarPopupWindow;
        if (keyEvent.getKeyCode() != 4 || keyEvent.getRepeatCount() != 0 || (actionBarPopupWindow = this.sendPopupWindow) == null || !actionBarPopupWindow.isShowing()) {
            return;
        }
        this.sendPopupWindow.dismiss();
    }

    public /* synthetic */ void lambda$createView$6(int i, View view) {
        ActionBarPopupWindow actionBarPopupWindow = this.sendPopupWindow;
        if (actionBarPopupWindow != null && actionBarPopupWindow.isShowing()) {
            this.sendPopupWindow.dismiss();
        }
        if (i == 0) {
            AlertsCreator.createScheduleDatePickerDialog(getParentActivity(), this.chatActivity.getDialogId(), new PhotoPickerActivity$$ExternalSyntheticLambda10(this));
        } else {
            sendSelectedPhotos(true, 0);
        }
    }

    /* renamed from: org.telegram.ui.PhotoPickerActivity$15 */
    /* loaded from: classes3.dex */
    class AnonymousClass15 extends View {
        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass15(Context context) {
            super(context);
            PhotoPickerActivity.this = r1;
        }

        @Override // android.view.View
        protected void onDraw(Canvas canvas) {
            String format = String.format("%d", Integer.valueOf(Math.max(1, PhotoPickerActivity.this.selectedPhotosOrder.size())));
            int ceil = (int) Math.ceil(PhotoPickerActivity.this.textPaint.measureText(format));
            int max = Math.max(AndroidUtilities.dp(16.0f) + ceil, AndroidUtilities.dp(24.0f));
            int measuredWidth = getMeasuredWidth() / 2;
            int measuredHeight = getMeasuredHeight() / 2;
            PhotoPickerActivity.this.textPaint.setColor(Theme.getColor("dialogRoundCheckBoxCheck"));
            PhotoPickerActivity.this.paint.setColor(Theme.getColor(PhotoPickerActivity.this.dialogBackgroundKey));
            int i = max / 2;
            int i2 = measuredWidth - i;
            int i3 = i + measuredWidth;
            PhotoPickerActivity.this.rect.set(i2, 0.0f, i3, getMeasuredHeight());
            canvas.drawRoundRect(PhotoPickerActivity.this.rect, AndroidUtilities.dp(12.0f), AndroidUtilities.dp(12.0f), PhotoPickerActivity.this.paint);
            PhotoPickerActivity.this.paint.setColor(Theme.getColor("dialogRoundCheckBox"));
            PhotoPickerActivity.this.rect.set(i2 + AndroidUtilities.dp(2.0f), AndroidUtilities.dp(2.0f), i3 - AndroidUtilities.dp(2.0f), getMeasuredHeight() - AndroidUtilities.dp(2.0f));
            canvas.drawRoundRect(PhotoPickerActivity.this.rect, AndroidUtilities.dp(10.0f), AndroidUtilities.dp(10.0f), PhotoPickerActivity.this.paint);
            canvas.drawText(format, measuredWidth - (ceil / 2), AndroidUtilities.dp(16.2f), PhotoPickerActivity.this.textPaint);
        }
    }

    public void setLayoutViews(FrameLayout frameLayout, FrameLayout frameLayout2, View view, View view2, EditTextEmoji editTextEmoji) {
        this.frameLayout2 = frameLayout;
        this.writeButtonContainer = frameLayout2;
        this.commentTextView = editTextEmoji;
        this.selectedCountView = view;
        this.shadow = view2;
        this.needsBottomLayout = false;
    }

    private void applyCaption() {
        EditTextEmoji editTextEmoji = this.commentTextView;
        if (editTextEmoji == null || editTextEmoji.length() <= 0) {
            return;
        }
        Object obj = this.selectedPhotos.get(this.selectedPhotosOrder.get(0));
        if (obj instanceof MediaController.PhotoEntry) {
            ((MediaController.PhotoEntry) obj).caption = this.commentTextView.getText().toString();
        } else if (!(obj instanceof MediaController.SearchImage)) {
        } else {
            ((MediaController.SearchImage) obj).caption = this.commentTextView.getText().toString();
        }
    }

    private void onListItemClick(View view, Object obj) {
        int i = 1;
        boolean z = addToSelectedPhotos(obj, -1) == -1;
        if (view instanceof SharedDocumentCell) {
            ((SharedDocumentCell) view).setChecked(this.selectedPhotosOrder.contains(Integer.valueOf(this.selectedAlbum.photos.get(((Integer) view.getTag()).intValue()).imageId)), true);
        }
        if (!z) {
            i = 2;
        }
        updatePhotosButton(i);
        this.delegate.selectedPhotosChanged();
    }

    public void clearRecentSearch() {
        this.recentSearches.clear();
        ListAdapter listAdapter = this.listAdapter;
        if (listAdapter != null) {
            listAdapter.notifyDataSetChanged();
        }
        this.emptyView.showProgress(false);
        saveRecentSearch();
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public void onResume() {
        super.onResume();
        ListAdapter listAdapter = this.listAdapter;
        if (listAdapter != null) {
            listAdapter.notifyDataSetChanged();
        }
        EditTextEmoji editTextEmoji = this.commentTextView;
        if (editTextEmoji != null) {
            editTextEmoji.onResume();
        }
        ActionBarMenuItem actionBarMenuItem = this.searchItem;
        if (actionBarMenuItem != null) {
            actionBarMenuItem.openSearch(true);
            if (!TextUtils.isEmpty(this.initialSearchString)) {
                this.searchItem.setSearchFieldText(this.initialSearchString, false);
                this.initialSearchString = null;
                processSearch(this.searchItem.getSearchField());
            }
            getParentActivity().getWindow().setSoftInputMode(SharedConfig.smoothKeyboard ? 32 : 16);
        }
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public void onPause() {
        super.onPause();
    }

    @Override // org.telegram.messenger.NotificationCenter.NotificationCenterDelegate
    public void didReceivedNotification(int i, int i2, Object... objArr) {
        if (i == NotificationCenter.closeChats) {
            removeSelfFromStack();
        }
    }

    public RecyclerListView getListView() {
        return this.listView;
    }

    public void setCaption(CharSequence charSequence) {
        this.caption = charSequence;
        EditTextEmoji editTextEmoji = this.commentTextView;
        if (editTextEmoji != null) {
            editTextEmoji.setText(charSequence);
        }
    }

    public void setInitialSearchString(String str) {
        this.initialSearchString = str;
    }

    private void saveRecentSearch() {
        SharedPreferences.Editor edit = ApplicationLoader.applicationContext.getSharedPreferences("web_recent_search", 0).edit();
        edit.clear();
        edit.putInt("count", this.recentSearches.size());
        int size = this.recentSearches.size();
        for (int i = 0; i < size; i++) {
            edit.putString("recent" + i, this.recentSearches.get(i));
        }
        edit.commit();
    }

    private void loadRecentSearch() {
        SharedPreferences sharedPreferences = ApplicationLoader.applicationContext.getSharedPreferences("web_recent_search", 0);
        int i = sharedPreferences.getInt("count", 0);
        for (int i2 = 0; i2 < i; i2++) {
            String string = sharedPreferences.getString("recent" + i2, null);
            if (string == null) {
                return;
            }
            this.recentSearches.add(string);
        }
    }

    private void addToRecentSearches(String str) {
        int size = this.recentSearches.size();
        int i = 0;
        while (true) {
            if (i >= size) {
                break;
            } else if (this.recentSearches.get(i).equalsIgnoreCase(str)) {
                this.recentSearches.remove(i);
                break;
            } else {
                i++;
            }
        }
        this.recentSearches.add(0, str);
        while (this.recentSearches.size() > 20) {
            ArrayList<String> arrayList = this.recentSearches;
            arrayList.remove(arrayList.size() - 1);
        }
        saveRecentSearch();
    }

    public void processSearch(EditText editText) {
        if (editText.getText().length() == 0) {
            return;
        }
        String obj = editText.getText().toString();
        this.searchResult.clear();
        this.searchResultKeys.clear();
        this.imageSearchEndReached = true;
        searchImages(this.type == 1, obj, "", true);
        this.lastSearchString = obj;
        if (obj.length() == 0) {
            this.lastSearchString = null;
            this.emptyView.title.setText(LocaleController.getString("NoRecentSearches", 2131626856));
        } else {
            this.emptyView.title.setText(LocaleController.formatString("NoResultFoundFor", 2131626859, this.lastSearchString));
        }
        updateSearchInterface();
    }

    private boolean showCommentTextView(boolean z, boolean z2) {
        if (this.commentTextView == null) {
            return false;
        }
        if (z == (this.frameLayout2.getTag() != null)) {
            return false;
        }
        AnimatorSet animatorSet = this.animatorSet;
        if (animatorSet != null) {
            animatorSet.cancel();
        }
        this.frameLayout2.setTag(z ? 1 : null);
        if (this.commentTextView.getEditText().isFocused()) {
            AndroidUtilities.hideKeyboard(this.commentTextView.getEditText());
        }
        this.commentTextView.hidePopup(true);
        if (z) {
            this.frameLayout2.setVisibility(0);
            this.writeButtonContainer.setVisibility(0);
        }
        float f = 0.0f;
        float f2 = 0.2f;
        float f3 = 1.0f;
        if (z2) {
            this.animatorSet = new AnimatorSet();
            ArrayList arrayList = new ArrayList();
            FrameLayout frameLayout = this.writeButtonContainer;
            Property property = View.SCALE_X;
            float[] fArr = new float[1];
            fArr[0] = z ? 1.0f : 0.2f;
            arrayList.add(ObjectAnimator.ofFloat(frameLayout, property, fArr));
            FrameLayout frameLayout2 = this.writeButtonContainer;
            Property property2 = View.SCALE_Y;
            float[] fArr2 = new float[1];
            fArr2[0] = z ? 1.0f : 0.2f;
            arrayList.add(ObjectAnimator.ofFloat(frameLayout2, property2, fArr2));
            FrameLayout frameLayout3 = this.writeButtonContainer;
            Property property3 = View.ALPHA;
            float[] fArr3 = new float[1];
            fArr3[0] = z ? 1.0f : 0.0f;
            arrayList.add(ObjectAnimator.ofFloat(frameLayout3, property3, fArr3));
            View view = this.selectedCountView;
            Property property4 = View.SCALE_X;
            float[] fArr4 = new float[1];
            fArr4[0] = z ? 1.0f : 0.2f;
            arrayList.add(ObjectAnimator.ofFloat(view, property4, fArr4));
            View view2 = this.selectedCountView;
            Property property5 = View.SCALE_Y;
            float[] fArr5 = new float[1];
            if (z) {
                f2 = 1.0f;
            }
            fArr5[0] = f2;
            arrayList.add(ObjectAnimator.ofFloat(view2, property5, fArr5));
            View view3 = this.selectedCountView;
            Property property6 = View.ALPHA;
            float[] fArr6 = new float[1];
            if (!z) {
                f3 = 0.0f;
            }
            fArr6[0] = f3;
            arrayList.add(ObjectAnimator.ofFloat(view3, property6, fArr6));
            FrameLayout frameLayout4 = this.frameLayout2;
            Property property7 = View.TRANSLATION_Y;
            float[] fArr7 = new float[1];
            fArr7[0] = z ? 0.0f : AndroidUtilities.dp(48.0f);
            arrayList.add(ObjectAnimator.ofFloat(frameLayout4, property7, fArr7));
            View view4 = this.shadow;
            Property property8 = View.TRANSLATION_Y;
            float[] fArr8 = new float[1];
            if (!z) {
                f = AndroidUtilities.dp(48.0f);
            }
            fArr8[0] = f;
            arrayList.add(ObjectAnimator.ofFloat(view4, property8, fArr8));
            this.animatorSet.playTogether(arrayList);
            this.animatorSet.setInterpolator(new DecelerateInterpolator());
            this.animatorSet.setDuration(180L);
            this.animatorSet.addListener(new AnonymousClass16(z));
            this.animatorSet.start();
        } else {
            this.writeButtonContainer.setScaleX(z ? 1.0f : 0.2f);
            this.writeButtonContainer.setScaleY(z ? 1.0f : 0.2f);
            this.writeButtonContainer.setAlpha(z ? 1.0f : 0.0f);
            this.selectedCountView.setScaleX(z ? 1.0f : 0.2f);
            View view5 = this.selectedCountView;
            if (z) {
                f2 = 1.0f;
            }
            view5.setScaleY(f2);
            View view6 = this.selectedCountView;
            if (!z) {
                f3 = 0.0f;
            }
            view6.setAlpha(f3);
            this.frameLayout2.setTranslationY(z ? 0.0f : AndroidUtilities.dp(48.0f));
            View view7 = this.shadow;
            if (!z) {
                f = AndroidUtilities.dp(48.0f);
            }
            view7.setTranslationY(f);
            if (!z) {
                this.frameLayout2.setVisibility(4);
                this.writeButtonContainer.setVisibility(4);
            }
        }
        return true;
    }

    /* renamed from: org.telegram.ui.PhotoPickerActivity$16 */
    /* loaded from: classes3.dex */
    public class AnonymousClass16 extends AnimatorListenerAdapter {
        final /* synthetic */ boolean val$show;

        AnonymousClass16(boolean z) {
            PhotoPickerActivity.this = r1;
            this.val$show = z;
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator) {
            if (animator.equals(PhotoPickerActivity.this.animatorSet)) {
                if (!this.val$show) {
                    PhotoPickerActivity.this.frameLayout2.setVisibility(4);
                    PhotoPickerActivity.this.writeButtonContainer.setVisibility(4);
                }
                PhotoPickerActivity.this.animatorSet = null;
            }
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationCancel(Animator animator) {
            if (animator.equals(PhotoPickerActivity.this.animatorSet)) {
                PhotoPickerActivity.this.animatorSet = null;
            }
        }
    }

    public void setMaxSelectedPhotos(int i, boolean z) {
        this.maxSelectedPhotos = i;
        this.allowOrder = z;
        if (i <= 0 || this.type != 1) {
            return;
        }
        this.maxSelectedPhotos = 1;
    }

    public void updateCheckedPhotoIndices() {
        if (!this.allowIndices) {
            return;
        }
        int childCount = this.listView.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View childAt = this.listView.getChildAt(i);
            if (childAt instanceof PhotoAttachPhotoCell) {
                PhotoAttachPhotoCell photoAttachPhotoCell = (PhotoAttachPhotoCell) childAt;
                Integer num = (Integer) childAt.getTag();
                MediaController.AlbumEntry albumEntry = this.selectedAlbum;
                int i2 = -1;
                if (albumEntry != null) {
                    MediaController.PhotoEntry photoEntry = albumEntry.photos.get(num.intValue());
                    if (this.allowIndices) {
                        i2 = this.selectedPhotosOrder.indexOf(Integer.valueOf(photoEntry.imageId));
                    }
                    photoAttachPhotoCell.setNum(i2);
                } else {
                    MediaController.SearchImage searchImage = this.searchResult.get(num.intValue());
                    if (this.allowIndices) {
                        i2 = this.selectedPhotosOrder.indexOf(searchImage.id);
                    }
                    photoAttachPhotoCell.setNum(i2);
                }
            } else if (childAt instanceof SharedDocumentCell) {
                ((SharedDocumentCell) childAt).setChecked(this.selectedPhotosOrder.indexOf(Integer.valueOf(this.selectedAlbum.photos.get(((Integer) childAt.getTag()).intValue()).imageId)) != 0, false);
            }
        }
    }

    public PhotoAttachPhotoCell getCellForIndex(int i) {
        int childCount = this.listView.getChildCount();
        for (int i2 = 0; i2 < childCount; i2++) {
            View childAt = this.listView.getChildAt(i2);
            if (childAt instanceof PhotoAttachPhotoCell) {
                PhotoAttachPhotoCell photoAttachPhotoCell = (PhotoAttachPhotoCell) childAt;
                int intValue = ((Integer) photoAttachPhotoCell.getTag()).intValue();
                MediaController.AlbumEntry albumEntry = this.selectedAlbum;
                if (albumEntry == null ? !(intValue < 0 || intValue >= this.searchResult.size()) : !(intValue < 0 || intValue >= albumEntry.photos.size())) {
                    if (intValue == i) {
                        return photoAttachPhotoCell;
                    }
                }
            }
        }
        return null;
    }

    public int addToSelectedPhotos(Object obj, int i) {
        Object obj2;
        boolean z = obj instanceof MediaController.PhotoEntry;
        if (z) {
            obj2 = Integer.valueOf(((MediaController.PhotoEntry) obj).imageId);
        } else {
            obj2 = obj instanceof MediaController.SearchImage ? ((MediaController.SearchImage) obj).id : null;
        }
        if (obj2 == null) {
            return -1;
        }
        if (this.selectedPhotos.containsKey(obj2)) {
            this.selectedPhotos.remove(obj2);
            int indexOf = this.selectedPhotosOrder.indexOf(obj2);
            if (indexOf >= 0) {
                this.selectedPhotosOrder.remove(indexOf);
            }
            if (this.allowIndices) {
                updateCheckedPhotoIndices();
            }
            if (i >= 0) {
                if (z) {
                    ((MediaController.PhotoEntry) obj).reset();
                } else if (obj instanceof MediaController.SearchImage) {
                    ((MediaController.SearchImage) obj).reset();
                }
                this.provider.updatePhotoAtIndex(i);
            }
            return indexOf;
        }
        this.selectedPhotos.put(obj2, obj);
        this.selectedPhotosOrder.add(obj2);
        return -1;
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public void onTransitionAnimationEnd(boolean z, boolean z2) {
        ActionBarMenuItem actionBarMenuItem;
        if (!z || (actionBarMenuItem = this.searchItem) == null) {
            return;
        }
        AndroidUtilities.showKeyboard(actionBarMenuItem.getSearchField());
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public boolean onBackPressed() {
        EditTextEmoji editTextEmoji = this.commentTextView;
        if (editTextEmoji != null && editTextEmoji.isPopupShowing()) {
            this.commentTextView.hidePopup(true);
            return false;
        }
        return super.onBackPressed();
    }

    public void updatePhotosButton(int i) {
        boolean z = true;
        if (this.selectedPhotos.size() == 0) {
            this.selectedCountView.setPivotX(0.0f);
            this.selectedCountView.setPivotY(0.0f);
            if (i == 0) {
                z = false;
            }
            showCommentTextView(false, z);
            return;
        }
        this.selectedCountView.invalidate();
        if (!showCommentTextView(true, i != 0) && i != 0) {
            this.selectedCountView.setPivotX(AndroidUtilities.dp(21.0f));
            this.selectedCountView.setPivotY(AndroidUtilities.dp(12.0f));
            AnimatorSet animatorSet = new AnimatorSet();
            Animator[] animatorArr = new Animator[2];
            View view = this.selectedCountView;
            Property property = View.SCALE_X;
            float[] fArr = new float[2];
            float f = 1.1f;
            fArr[0] = i == 1 ? 1.1f : 0.9f;
            fArr[1] = 1.0f;
            animatorArr[0] = ObjectAnimator.ofFloat(view, property, fArr);
            View view2 = this.selectedCountView;
            Property property2 = View.SCALE_Y;
            float[] fArr2 = new float[2];
            if (i != 1) {
                f = 0.9f;
            }
            fArr2[0] = f;
            fArr2[1] = 1.0f;
            animatorArr[1] = ObjectAnimator.ofFloat(view2, property2, fArr2);
            animatorSet.playTogether(animatorArr);
            animatorSet.setInterpolator(new OvershootInterpolator());
            animatorSet.setDuration(180L);
            animatorSet.start();
            return;
        }
        this.selectedCountView.setPivotX(0.0f);
        this.selectedCountView.setPivotY(0.0f);
    }

    public void updateSearchInterface() {
        String str;
        ListAdapter listAdapter = this.listAdapter;
        if (listAdapter != null) {
            listAdapter.notifyDataSetChanged();
        }
        if (this.searching || (this.recentSearches.size() > 0 && ((str = this.lastSearchString) == null || TextUtils.isEmpty(str)))) {
            this.emptyView.showProgress(true);
        } else {
            this.emptyView.showProgress(false);
        }
    }

    private void searchBotUser(boolean z) {
        if (this.searchingUser) {
            return;
        }
        this.searchingUser = true;
        TLRPC$TL_contacts_resolveUsername tLRPC$TL_contacts_resolveUsername = new TLRPC$TL_contacts_resolveUsername();
        MessagesController messagesController = MessagesController.getInstance(this.currentAccount);
        tLRPC$TL_contacts_resolveUsername.username = z ? messagesController.gifSearchBot : messagesController.imageSearchBot;
        ConnectionsManager.getInstance(this.currentAccount).sendRequest(tLRPC$TL_contacts_resolveUsername, new PhotoPickerActivity$$ExternalSyntheticLambda8(this, z));
    }

    public /* synthetic */ void lambda$searchBotUser$9(boolean z, TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        if (tLObject != null) {
            AndroidUtilities.runOnUIThread(new PhotoPickerActivity$$ExternalSyntheticLambda6(this, tLObject, z));
        }
    }

    public /* synthetic */ void lambda$searchBotUser$8(TLObject tLObject, boolean z) {
        TLRPC$TL_contacts_resolvedPeer tLRPC$TL_contacts_resolvedPeer = (TLRPC$TL_contacts_resolvedPeer) tLObject;
        MessagesController.getInstance(this.currentAccount).putUsers(tLRPC$TL_contacts_resolvedPeer.users, false);
        MessagesController.getInstance(this.currentAccount).putChats(tLRPC$TL_contacts_resolvedPeer.chats, false);
        MessagesStorage.getInstance(this.currentAccount).putUsersAndChats(tLRPC$TL_contacts_resolvedPeer.users, tLRPC$TL_contacts_resolvedPeer.chats, true, true);
        String str = this.lastSearchImageString;
        this.lastSearchImageString = null;
        searchImages(z, str, "", false);
    }

    public void searchImages(boolean z, String str, String str2, boolean z2) {
        if (this.searching) {
            this.searching = false;
            if (this.imageReqId != 0) {
                ConnectionsManager.getInstance(this.currentAccount).cancelRequest(this.imageReqId, true);
                this.imageReqId = 0;
            }
        }
        this.lastSearchImageString = str;
        this.searching = true;
        MessagesController messagesController = MessagesController.getInstance(this.currentAccount);
        MessagesController messagesController2 = MessagesController.getInstance(this.currentAccount);
        TLObject userOrChat = messagesController.getUserOrChat(z ? messagesController2.gifSearchBot : messagesController2.imageSearchBot);
        if (!(userOrChat instanceof TLRPC$User)) {
            if (!z2) {
                return;
            }
            searchBotUser(z);
            return;
        }
        TLRPC$User tLRPC$User = (TLRPC$User) userOrChat;
        TLRPC$TL_messages_getInlineBotResults tLRPC$TL_messages_getInlineBotResults = new TLRPC$TL_messages_getInlineBotResults();
        tLRPC$TL_messages_getInlineBotResults.query = str == null ? "" : str;
        tLRPC$TL_messages_getInlineBotResults.bot = MessagesController.getInstance(this.currentAccount).getInputUser(tLRPC$User);
        tLRPC$TL_messages_getInlineBotResults.offset = str2;
        ChatActivity chatActivity = this.chatActivity;
        if (chatActivity != null) {
            long dialogId = chatActivity.getDialogId();
            if (DialogObject.isEncryptedDialog(dialogId)) {
                tLRPC$TL_messages_getInlineBotResults.peer = new TLRPC$TL_inputPeerEmpty();
            } else {
                tLRPC$TL_messages_getInlineBotResults.peer = getMessagesController().getInputPeer(dialogId);
            }
        } else {
            tLRPC$TL_messages_getInlineBotResults.peer = new TLRPC$TL_inputPeerEmpty();
        }
        int i = this.lastSearchToken + 1;
        this.lastSearchToken = i;
        this.imageReqId = ConnectionsManager.getInstance(this.currentAccount).sendRequest(tLRPC$TL_messages_getInlineBotResults, new PhotoPickerActivity$$ExternalSyntheticLambda7(this, str, i, z, tLRPC$User));
        ConnectionsManager.getInstance(this.currentAccount).bindRequestToGuid(this.imageReqId, this.classGuid);
    }

    public /* synthetic */ void lambda$searchImages$11(String str, int i, boolean z, TLRPC$User tLRPC$User, TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        AndroidUtilities.runOnUIThread(new PhotoPickerActivity$$ExternalSyntheticLambda5(this, str, i, tLObject, z, tLRPC$User));
    }

    public /* synthetic */ void lambda$searchImages$10(String str, int i, TLObject tLObject, boolean z, TLRPC$User tLRPC$User) {
        int i2;
        TLRPC$Photo tLRPC$Photo;
        TLRPC$PhotoSize closestPhotoSizeWithSize;
        addToRecentSearches(str);
        if (i != this.lastSearchToken) {
            return;
        }
        int size = this.searchResult.size();
        if (tLObject != null) {
            TLRPC$messages_BotResults tLRPC$messages_BotResults = (TLRPC$messages_BotResults) tLObject;
            this.nextImagesSearchOffset = tLRPC$messages_BotResults.next_offset;
            int size2 = tLRPC$messages_BotResults.results.size();
            i2 = 0;
            for (int i3 = 0; i3 < size2; i3++) {
                TLRPC$BotInlineResult tLRPC$BotInlineResult = tLRPC$messages_BotResults.results.get(i3);
                if ((z || "photo".equals(tLRPC$BotInlineResult.type)) && ((!z || "gif".equals(tLRPC$BotInlineResult.type)) && !this.searchResultKeys.containsKey(tLRPC$BotInlineResult.id))) {
                    MediaController.SearchImage searchImage = new MediaController.SearchImage();
                    if (z && tLRPC$BotInlineResult.document != null) {
                        for (int i4 = 0; i4 < tLRPC$BotInlineResult.document.attributes.size(); i4++) {
                            TLRPC$DocumentAttribute tLRPC$DocumentAttribute = tLRPC$BotInlineResult.document.attributes.get(i4);
                            if ((tLRPC$DocumentAttribute instanceof TLRPC$TL_documentAttributeImageSize) || (tLRPC$DocumentAttribute instanceof TLRPC$TL_documentAttributeVideo)) {
                                searchImage.width = tLRPC$DocumentAttribute.w;
                                searchImage.height = tLRPC$DocumentAttribute.h;
                                break;
                            }
                        }
                        searchImage.document = tLRPC$BotInlineResult.document;
                        searchImage.size = 0;
                        TLRPC$Photo tLRPC$Photo2 = tLRPC$BotInlineResult.photo;
                        if (tLRPC$Photo2 != null && (closestPhotoSizeWithSize = FileLoader.getClosestPhotoSizeWithSize(tLRPC$Photo2.sizes, this.itemSize, true)) != null) {
                            tLRPC$BotInlineResult.document.thumbs.add(closestPhotoSizeWithSize);
                            tLRPC$BotInlineResult.document.flags |= 1;
                        }
                    } else if (!z && (tLRPC$Photo = tLRPC$BotInlineResult.photo) != null) {
                        TLRPC$PhotoSize closestPhotoSizeWithSize2 = FileLoader.getClosestPhotoSizeWithSize(tLRPC$Photo.sizes, AndroidUtilities.getPhotoSize());
                        TLRPC$PhotoSize closestPhotoSizeWithSize3 = FileLoader.getClosestPhotoSizeWithSize(tLRPC$BotInlineResult.photo.sizes, 320);
                        if (closestPhotoSizeWithSize2 != null) {
                            searchImage.width = closestPhotoSizeWithSize2.w;
                            searchImage.height = closestPhotoSizeWithSize2.h;
                            searchImage.photoSize = closestPhotoSizeWithSize2;
                            searchImage.photo = tLRPC$BotInlineResult.photo;
                            searchImage.size = closestPhotoSizeWithSize2.size;
                            searchImage.thumbPhotoSize = closestPhotoSizeWithSize3;
                        }
                    } else if (tLRPC$BotInlineResult.content != null) {
                        int i5 = 0;
                        while (true) {
                            if (i5 >= tLRPC$BotInlineResult.content.attributes.size()) {
                                break;
                            }
                            TLRPC$DocumentAttribute tLRPC$DocumentAttribute2 = tLRPC$BotInlineResult.content.attributes.get(i5);
                            if (tLRPC$DocumentAttribute2 instanceof TLRPC$TL_documentAttributeImageSize) {
                                searchImage.width = tLRPC$DocumentAttribute2.w;
                                searchImage.height = tLRPC$DocumentAttribute2.h;
                                break;
                            }
                            i5++;
                        }
                        TLRPC$WebDocument tLRPC$WebDocument = tLRPC$BotInlineResult.thumb;
                        if (tLRPC$WebDocument != null) {
                            searchImage.thumbUrl = tLRPC$WebDocument.url;
                        } else {
                            searchImage.thumbUrl = null;
                        }
                        TLRPC$WebDocument tLRPC$WebDocument2 = tLRPC$BotInlineResult.content;
                        searchImage.imageUrl = tLRPC$WebDocument2.url;
                        searchImage.size = z ? 0 : tLRPC$WebDocument2.size;
                    }
                    searchImage.id = tLRPC$BotInlineResult.id;
                    searchImage.type = z ? 1 : 0;
                    searchImage.inlineResult = tLRPC$BotInlineResult;
                    HashMap<String, String> hashMap = new HashMap<>();
                    searchImage.params = hashMap;
                    hashMap.put("id", tLRPC$BotInlineResult.id);
                    searchImage.params.put("query_id", "" + tLRPC$messages_BotResults.query_id);
                    searchImage.params.put("bot_name", tLRPC$User.username);
                    this.searchResult.add(searchImage);
                    this.searchResultKeys.put(searchImage.id, searchImage);
                    i2++;
                }
            }
            this.imageSearchEndReached = size == this.searchResult.size() || this.nextImagesSearchOffset == null;
        } else {
            i2 = 0;
        }
        this.searching = false;
        if (i2 != 0) {
            this.listAdapter.notifyItemRangeInserted(size, i2);
        } else if (this.imageSearchEndReached) {
            this.listAdapter.notifyItemRemoved(this.searchResult.size() - 1);
        }
        if (this.searchResult.size() > 0) {
            return;
        }
        this.emptyView.showProgress(false);
    }

    public void setDelegate(PhotoPickerActivityDelegate photoPickerActivityDelegate) {
        this.delegate = photoPickerActivityDelegate;
    }

    public void setSearchDelegate(PhotoPickerActivitySearchDelegate photoPickerActivitySearchDelegate) {
        this.searchDelegate = photoPickerActivitySearchDelegate;
    }

    public void sendSelectedPhotos(boolean z, int i) {
        if (this.selectedPhotos.isEmpty() || this.delegate == null || this.sendPressed) {
            return;
        }
        applyCaption();
        this.sendPressed = true;
        this.delegate.actionButtonPressed(false, z, i);
        if (this.selectPhotoType == PhotoAlbumPickerActivity.SELECT_TYPE_WALLPAPER) {
            return;
        }
        finishFragment();
    }

    /* loaded from: classes3.dex */
    public class ListAdapter extends RecyclerListView.SelectionAdapter {
        private Context mContext;

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public long getItemId(int i) {
            return i;
        }

        public ListAdapter(Context context) {
            PhotoPickerActivity.this = r1;
            this.mContext = context;
        }

        @Override // org.telegram.ui.Components.RecyclerListView.SelectionAdapter
        public boolean isEnabled(RecyclerView.ViewHolder viewHolder) {
            if (PhotoPickerActivity.this.selectedAlbum == null) {
                return TextUtils.isEmpty(PhotoPickerActivity.this.lastSearchString) ? viewHolder.getItemViewType() == 3 : viewHolder.getAdapterPosition() < PhotoPickerActivity.this.searchResult.size();
            }
            return true;
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public int getItemCount() {
            if (PhotoPickerActivity.this.selectedAlbum == null) {
                if (PhotoPickerActivity.this.searchResult.isEmpty()) {
                    if (TextUtils.isEmpty(PhotoPickerActivity.this.lastSearchString) && !PhotoPickerActivity.this.recentSearches.isEmpty()) {
                        return PhotoPickerActivity.this.recentSearches.size() + 2;
                    }
                    return 0;
                }
                return PhotoPickerActivity.this.searchResult.size() + (!PhotoPickerActivity.this.imageSearchEndReached ? 1 : 0);
            }
            return PhotoPickerActivity.this.selectedAlbum.photos.size();
        }

        /* renamed from: org.telegram.ui.PhotoPickerActivity$ListAdapter$1 */
        /* loaded from: classes3.dex */
        class AnonymousClass1 implements PhotoAttachPhotoCell.PhotoAttachPhotoCellDelegate {
            AnonymousClass1() {
                ListAdapter.this = r1;
            }

            private void checkSlowMode() {
                TLRPC$Chat currentChat;
                if (!PhotoPickerActivity.this.allowOrder || PhotoPickerActivity.this.chatActivity == null || (currentChat = PhotoPickerActivity.this.chatActivity.getCurrentChat()) == null || ChatObject.hasAdminRights(currentChat) || !currentChat.slowmode_enabled || PhotoPickerActivity.this.alertOnlyOnce == 2) {
                    return;
                }
                AlertsCreator.showSimpleAlert(PhotoPickerActivity.this, LocaleController.getString("Slowmode", 2131628360), LocaleController.getString("SlowmodeSelectSendError", 2131628367));
                if (PhotoPickerActivity.this.alertOnlyOnce != 1) {
                    return;
                }
                PhotoPickerActivity.this.alertOnlyOnce = 2;
            }

            @Override // org.telegram.ui.Cells.PhotoAttachPhotoCell.PhotoAttachPhotoCellDelegate
            public void onCheckClick(PhotoAttachPhotoCell photoAttachPhotoCell) {
                boolean z;
                int intValue = ((Integer) photoAttachPhotoCell.getTag()).intValue();
                int i = -1;
                int i2 = 1;
                if (PhotoPickerActivity.this.selectedAlbum != null) {
                    MediaController.PhotoEntry photoEntry = PhotoPickerActivity.this.selectedAlbum.photos.get(intValue);
                    z = !PhotoPickerActivity.this.selectedPhotos.containsKey(Integer.valueOf(photoEntry.imageId));
                    if (!z || PhotoPickerActivity.this.maxSelectedPhotos <= 0 || PhotoPickerActivity.this.selectedPhotos.size() < PhotoPickerActivity.this.maxSelectedPhotos) {
                        if (PhotoPickerActivity.this.allowIndices && z) {
                            i = PhotoPickerActivity.this.selectedPhotosOrder.size();
                        }
                        photoAttachPhotoCell.setChecked(i, z, true);
                        PhotoPickerActivity.this.addToSelectedPhotos(photoEntry, intValue);
                    } else {
                        checkSlowMode();
                        return;
                    }
                } else {
                    AndroidUtilities.hideKeyboard(PhotoPickerActivity.this.getParentActivity().getCurrentFocus());
                    MediaController.SearchImage searchImage = (MediaController.SearchImage) PhotoPickerActivity.this.searchResult.get(intValue);
                    z = !PhotoPickerActivity.this.selectedPhotos.containsKey(searchImage.id);
                    if (!z || PhotoPickerActivity.this.maxSelectedPhotos <= 0 || PhotoPickerActivity.this.selectedPhotos.size() < PhotoPickerActivity.this.maxSelectedPhotos) {
                        if (PhotoPickerActivity.this.allowIndices && z) {
                            i = PhotoPickerActivity.this.selectedPhotosOrder.size();
                        }
                        photoAttachPhotoCell.setChecked(i, z, true);
                        PhotoPickerActivity.this.addToSelectedPhotos(searchImage, intValue);
                    } else {
                        checkSlowMode();
                        return;
                    }
                }
                PhotoPickerActivity photoPickerActivity = PhotoPickerActivity.this;
                if (!z) {
                    i2 = 2;
                }
                photoPickerActivity.updatePhotosButton(i2);
                PhotoPickerActivity.this.delegate.selectedPhotosChanged();
            }
        }

        /* JADX WARN: Multi-variable type inference failed */
        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            DividerCell dividerCell;
            PhotoAttachPhotoCell photoAttachPhotoCell;
            if (i != 0) {
                if (i == 1) {
                    FrameLayout frameLayout = new FrameLayout(this.mContext);
                    frameLayout.setLayoutParams(new RecyclerView.LayoutParams(-1, -2));
                    RadialProgressView radialProgressView = new RadialProgressView(this.mContext);
                    radialProgressView.setProgressColor(-11371101);
                    frameLayout.addView(radialProgressView, LayoutHelper.createFrame(-1, -1.0f));
                    photoAttachPhotoCell = frameLayout;
                } else if (i == 2) {
                    dividerCell = new SharedDocumentCell(this.mContext, 1);
                } else if (i == 3) {
                    TextCell textCell = new TextCell(this.mContext, 23, true);
                    textCell.setLayoutParams(new RecyclerView.LayoutParams(-1, -2));
                    photoAttachPhotoCell = textCell;
                    if (PhotoPickerActivity.this.forceDarckTheme) {
                        textCell.textView.setTextColor(Theme.getColor(PhotoPickerActivity.this.textKey));
                        textCell.imageView.setColorFilter(new PorterDuffColorFilter(Theme.getColor("voipgroup_mutedIcon"), PorterDuff.Mode.MULTIPLY));
                        photoAttachPhotoCell = textCell;
                    }
                } else {
                    DividerCell dividerCell2 = new DividerCell(this.mContext);
                    dividerCell2.setForceDarkTheme(PhotoPickerActivity.this.forceDarckTheme);
                    dividerCell = dividerCell2;
                }
                dividerCell = photoAttachPhotoCell;
            } else {
                PhotoAttachPhotoCell photoAttachPhotoCell2 = new PhotoAttachPhotoCell(this.mContext, null);
                photoAttachPhotoCell2.setDelegate(new AnonymousClass1());
                photoAttachPhotoCell2.getCheckFrame().setVisibility(PhotoPickerActivity.this.selectPhotoType != PhotoAlbumPickerActivity.SELECT_TYPE_ALL ? 8 : 0);
                dividerCell = photoAttachPhotoCell2;
            }
            return new RecyclerListView.Holder(dividerCell);
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
            boolean z;
            int itemViewType = viewHolder.getItemViewType();
            int i2 = -1;
            int i3 = 0;
            if (itemViewType != 0) {
                if (itemViewType == 1) {
                    ViewGroup.LayoutParams layoutParams = viewHolder.itemView.getLayoutParams();
                    if (layoutParams == null) {
                        return;
                    }
                    layoutParams.width = -1;
                    layoutParams.height = PhotoPickerActivity.this.itemSize;
                    viewHolder.itemView.setLayoutParams(layoutParams);
                    return;
                } else if (itemViewType == 2) {
                    MediaController.PhotoEntry photoEntry = PhotoPickerActivity.this.selectedAlbum.photos.get(i);
                    SharedDocumentCell sharedDocumentCell = (SharedDocumentCell) viewHolder.itemView;
                    sharedDocumentCell.setPhotoEntry(photoEntry);
                    sharedDocumentCell.setChecked(PhotoPickerActivity.this.selectedPhotos.containsKey(Integer.valueOf(photoEntry.imageId)), false);
                    sharedDocumentCell.setTag(Integer.valueOf(i));
                    return;
                } else if (itemViewType != 3) {
                    return;
                } else {
                    TextCell textCell = (TextCell) viewHolder.itemView;
                    if (i < PhotoPickerActivity.this.recentSearches.size()) {
                        textCell.setTextAndIcon((String) PhotoPickerActivity.this.recentSearches.get(i), 2131165891, false);
                        return;
                    } else {
                        textCell.setTextAndIcon(LocaleController.getString("ClearRecentHistory", 2131625150), 2131165684, false);
                        return;
                    }
                }
            }
            PhotoAttachPhotoCell photoAttachPhotoCell = (PhotoAttachPhotoCell) viewHolder.itemView;
            photoAttachPhotoCell.setItemSize(PhotoPickerActivity.this.itemSize);
            BackupImageView imageView = photoAttachPhotoCell.getImageView();
            photoAttachPhotoCell.setTag(Integer.valueOf(i));
            imageView.setOrientation(0, true);
            if (PhotoPickerActivity.this.selectedAlbum != null) {
                MediaController.PhotoEntry photoEntry2 = PhotoPickerActivity.this.selectedAlbum.photos.get(i);
                photoAttachPhotoCell.setPhotoEntry(photoEntry2, true, false);
                if (PhotoPickerActivity.this.allowIndices) {
                    i2 = PhotoPickerActivity.this.selectedPhotosOrder.indexOf(Integer.valueOf(photoEntry2.imageId));
                }
                photoAttachPhotoCell.setChecked(i2, PhotoPickerActivity.this.selectedPhotos.containsKey(Integer.valueOf(photoEntry2.imageId)), false);
                z = PhotoViewer.isShowingImage(photoEntry2.path);
            } else {
                MediaController.SearchImage searchImage = (MediaController.SearchImage) PhotoPickerActivity.this.searchResult.get(i);
                photoAttachPhotoCell.setPhotoEntry(searchImage, true, false);
                photoAttachPhotoCell.getVideoInfoContainer().setVisibility(4);
                if (PhotoPickerActivity.this.allowIndices) {
                    i2 = PhotoPickerActivity.this.selectedPhotosOrder.indexOf(searchImage.id);
                }
                photoAttachPhotoCell.setChecked(i2, PhotoPickerActivity.this.selectedPhotos.containsKey(searchImage.id), false);
                z = PhotoViewer.isShowingImage(searchImage.getPathToAttach());
            }
            imageView.getImageReceiver().setVisible(!z, true);
            CheckBox2 checkBox = photoAttachPhotoCell.getCheckBox();
            if (PhotoPickerActivity.this.selectPhotoType != PhotoAlbumPickerActivity.SELECT_TYPE_ALL || z) {
                i3 = 8;
            }
            checkBox.setVisibility(i3);
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public int getItemViewType(int i) {
            if (PhotoPickerActivity.this.listSort) {
                return 2;
            }
            if (PhotoPickerActivity.this.selectedAlbum != null) {
                return 0;
            }
            return PhotoPickerActivity.this.searchResult.isEmpty() ? i == PhotoPickerActivity.this.recentSearches.size() ? 4 : 3 : i < PhotoPickerActivity.this.searchResult.size() ? 0 : 1;
        }
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public ArrayList<ThemeDescription> getThemeDescriptions() {
        ArrayList<ThemeDescription> arrayList = new ArrayList<>();
        arrayList.add(new ThemeDescription(this.sizeNotifierFrameLayout, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, this.dialogBackgroundKey));
        arrayList.add(new ThemeDescription(this.actionBar, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, this.dialogBackgroundKey));
        arrayList.add(new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_ITEMSCOLOR, null, null, null, null, this.textKey));
        arrayList.add(new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_TITLECOLOR, null, null, null, null, this.textKey));
        arrayList.add(new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_SELECTORCOLOR, null, null, null, null, this.selectorKey));
        arrayList.add(new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_SEARCH, null, null, null, null, this.textKey));
        arrayList.add(new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_SEARCHPLACEHOLDER, null, null, null, null, "chat_messagePanelHint"));
        ActionBarMenuItem actionBarMenuItem = this.searchItem;
        arrayList.add(new ThemeDescription(actionBarMenuItem != null ? actionBarMenuItem.getSearchField() : null, ThemeDescription.FLAG_CURSORCOLOR, null, null, null, null, this.textKey));
        arrayList.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_LISTGLOWCOLOR, null, null, null, null, this.dialogBackgroundKey));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{View.class}, null, new Drawable[]{Theme.chat_attachEmptyDrawable}, null, "chat_attachEmptyImage"));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{View.class}, null, null, null, "chat_attachPhotoBackground"));
        return arrayList;
    }
}