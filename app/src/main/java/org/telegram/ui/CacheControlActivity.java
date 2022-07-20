package org.telegram.ui;

import android.animation.TimeInterpolator;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.text.TextUtils;
import android.transition.ChangeBounds;
import android.transition.Fade;
import android.transition.TransitionManager;
import android.transition.TransitionSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.io.File;
import java.util.ArrayList;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.FileLoader;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.FilesMigrationService;
import org.telegram.messenger.ImageLoader;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MediaDataController;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.MessagesStorage;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.SharedConfig;
import org.telegram.messenger.Utilities;
import org.telegram.ui.ActionBar.ActionBar;
import org.telegram.ui.ActionBar.AlertDialog;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.ActionBar.BottomSheet;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.ActionBar.ThemeDescription;
import org.telegram.ui.Cells.CheckBoxCell;
import org.telegram.ui.Cells.HeaderCell;
import org.telegram.ui.Cells.TextCheckBoxCell;
import org.telegram.ui.Cells.TextInfoPrivacyCell;
import org.telegram.ui.Cells.TextSettingsCell;
import org.telegram.ui.Components.CubicBezierInterpolator;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.ui.Components.RecyclerListView;
import org.telegram.ui.Components.SlideChooseView;
import org.telegram.ui.Components.StorageDiagramView;
import org.telegram.ui.Components.StroageUsageView;
import org.telegram.ui.Components.UndoView;
/* loaded from: classes3.dex */
public class CacheControlActivity extends BaseFragment implements NotificationCenter.NotificationCenterDelegate {
    private View actionTextView;
    private BottomSheet bottomSheet;
    private View bottomSheetView;
    private int cacheInfoRow;
    private UndoView cacheRemovedTooltip;
    private int databaseInfoRow;
    private int databaseRow;
    private int deviseStorageHeaderRow;
    long fragmentCreateTime;
    private int keepMediaChooserRow;
    private int keepMediaHeaderRow;
    private int keepMediaInfoRow;
    private LinearLayoutManager layoutManager;
    private ListAdapter listAdapter;
    private RecyclerListView listView;
    AlertDialog progressDialog;
    private int rowCount;
    private int storageUsageRow;
    private long databaseSize = -1;
    private long cacheSize = -1;
    private long documentsSize = -1;
    private long audioSize = -1;
    private long musicSize = -1;
    private long photoSize = -1;
    private long videoSize = -1;
    private long stickersSize = -1;
    private long totalSize = -1;
    private long totalDeviceSize = -1;
    private long totalDeviceFreeSize = -1;
    private long migrateOldFolderRow = -1;
    private StorageDiagramView.ClearViewData[] clearViewData = new StorageDiagramView.ClearViewData[7];
    private boolean calculating = true;
    private volatile boolean canceled = false;

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public boolean onFragmentCreate() {
        super.onFragmentCreate();
        getNotificationCenter().addObserver(this, NotificationCenter.didClearDatabase);
        this.databaseSize = MessagesStorage.getInstance(this.currentAccount).getDatabaseSize();
        Utilities.globalQueue.postRunnable(new CacheControlActivity$$ExternalSyntheticLambda3(this));
        this.fragmentCreateTime = System.currentTimeMillis();
        updateRows();
        return true;
    }

    public /* synthetic */ void lambda$onFragmentCreate$1() {
        File file;
        long j;
        long j2;
        long j3;
        this.cacheSize = getDirectorySize(FileLoader.checkDirectory(4), 0);
        if (this.canceled) {
            return;
        }
        long directorySize = getDirectorySize(FileLoader.checkDirectory(0), 0);
        this.photoSize = directorySize;
        this.photoSize = directorySize + getDirectorySize(FileLoader.checkDirectory(100), 0);
        if (this.canceled) {
            return;
        }
        long directorySize2 = getDirectorySize(FileLoader.checkDirectory(2), 0);
        this.videoSize = directorySize2;
        this.videoSize = directorySize2 + getDirectorySize(FileLoader.checkDirectory(101), 0);
        if (this.canceled) {
            return;
        }
        long directorySize3 = getDirectorySize(FileLoader.checkDirectory(3), 1);
        this.documentsSize = directorySize3;
        this.documentsSize = directorySize3 + getDirectorySize(FileLoader.checkDirectory(5), 1);
        if (this.canceled) {
            return;
        }
        long directorySize4 = getDirectorySize(FileLoader.checkDirectory(3), 2);
        this.musicSize = directorySize4;
        this.musicSize = directorySize4 + getDirectorySize(FileLoader.checkDirectory(5), 2);
        if (this.canceled) {
            return;
        }
        this.stickersSize = getDirectorySize(new File(FileLoader.checkDirectory(4), "acache"), 0);
        if (this.canceled) {
            return;
        }
        long directorySize5 = getDirectorySize(FileLoader.checkDirectory(1), 0);
        this.audioSize = directorySize5;
        this.totalSize = this.cacheSize + this.videoSize + directorySize5 + this.photoSize + this.documentsSize + this.musicSize + this.stickersSize;
        if (Build.VERSION.SDK_INT >= 19) {
            ArrayList<File> rootDirs = AndroidUtilities.getRootDirs();
            file = rootDirs.get(0);
            file.getAbsolutePath();
            if (!TextUtils.isEmpty(SharedConfig.storageCacheDir)) {
                int size = rootDirs.size();
                for (int i = 0; i < size; i++) {
                    File file2 = rootDirs.get(i);
                    if (file2.getAbsolutePath().startsWith(SharedConfig.storageCacheDir)) {
                        file = file2;
                        break;
                    }
                }
            }
        } else {
            file = new File(SharedConfig.storageCacheDir);
        }
        try {
            StatFs statFs = new StatFs(file.getPath());
            int i2 = Build.VERSION.SDK_INT;
            if (i2 >= 18) {
                j = statFs.getBlockSizeLong();
            } else {
                j = statFs.getBlockSize();
            }
            if (i2 >= 18) {
                j2 = statFs.getAvailableBlocksLong();
            } else {
                j2 = statFs.getAvailableBlocks();
            }
            if (i2 >= 18) {
                j3 = statFs.getBlockCountLong();
            } else {
                j3 = statFs.getBlockCount();
            }
            this.totalDeviceSize = j3 * j;
            this.totalDeviceFreeSize = j2 * j;
        } catch (Exception e) {
            FileLog.e(e);
        }
        AndroidUtilities.runOnUIThread(new CacheControlActivity$$ExternalSyntheticLambda4(this));
    }

    public /* synthetic */ void lambda$onFragmentCreate$0() {
        this.calculating = false;
        updateStorageUsageRow();
    }

    private void updateRows() {
        this.rowCount = 0;
        int i = 0 + 1;
        this.rowCount = i;
        this.keepMediaHeaderRow = 0;
        int i2 = i + 1;
        this.rowCount = i2;
        this.keepMediaChooserRow = i;
        int i3 = i2 + 1;
        this.rowCount = i3;
        this.keepMediaInfoRow = i2;
        int i4 = i3 + 1;
        this.rowCount = i4;
        this.deviseStorageHeaderRow = i3;
        int i5 = i4 + 1;
        this.rowCount = i5;
        this.storageUsageRow = i4;
        int i6 = i5 + 1;
        this.rowCount = i6;
        this.cacheInfoRow = i5;
        int i7 = i6 + 1;
        this.rowCount = i7;
        this.databaseRow = i6;
        this.rowCount = i7 + 1;
        this.databaseInfoRow = i7;
    }

    private void updateStorageUsageRow() {
        View findViewByPosition = this.layoutManager.findViewByPosition(this.storageUsageRow);
        if (findViewByPosition instanceof StroageUsageView) {
            StroageUsageView stroageUsageView = (StroageUsageView) findViewByPosition;
            long currentTimeMillis = System.currentTimeMillis();
            if (Build.VERSION.SDK_INT >= 19 && currentTimeMillis - this.fragmentCreateTime > 250) {
                TransitionSet transitionSet = new TransitionSet();
                ChangeBounds changeBounds = new ChangeBounds();
                changeBounds.setDuration(250L);
                changeBounds.excludeTarget((View) stroageUsageView.legendLayout, true);
                Fade fade = new Fade(1);
                fade.setDuration(290L);
                transitionSet.addTransition(new Fade(2).setDuration(250L)).addTransition(changeBounds).addTransition(fade);
                transitionSet.setOrdering(0);
                transitionSet.setInterpolator((TimeInterpolator) CubicBezierInterpolator.EASE_OUT);
                TransitionManager.beginDelayedTransition(this.listView, transitionSet);
            }
            stroageUsageView.setStorageUsage(this.calculating, this.databaseSize, this.totalSize, this.totalDeviceFreeSize, this.totalDeviceSize);
            RecyclerView.ViewHolder findViewHolderForAdapterPosition = this.listView.findViewHolderForAdapterPosition(this.storageUsageRow);
            if (findViewHolderForAdapterPosition == null) {
                return;
            }
            stroageUsageView.setEnabled(this.listAdapter.isEnabled(findViewHolderForAdapterPosition));
            return;
        }
        this.listAdapter.notifyDataSetChanged();
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public void onFragmentDestroy() {
        super.onFragmentDestroy();
        getNotificationCenter().removeObserver(this, NotificationCenter.didClearDatabase);
        try {
            AlertDialog alertDialog = this.progressDialog;
            if (alertDialog != null) {
                alertDialog.dismiss();
            }
        } catch (Exception unused) {
        }
        this.progressDialog = null;
        this.canceled = true;
    }

    private long getDirectorySize(File file, int i) {
        if (file == null || this.canceled) {
            return 0L;
        }
        if (file.isDirectory()) {
            return Utilities.getDirSize(file.getAbsolutePath(), i, false);
        }
        if (!file.isFile()) {
            return 0L;
        }
        return 0 + file.length();
    }

    private void cleanupFolders() {
        AlertDialog alertDialog = new AlertDialog(getParentActivity(), 3);
        alertDialog.setCanCancel(false);
        alertDialog.showDelayed(500L);
        Utilities.globalQueue.postRunnable(new CacheControlActivity$$ExternalSyntheticLambda5(this, alertDialog));
    }

    /* JADX WARN: Removed duplicated region for block: B:28:0x005d  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public /* synthetic */ void lambda$cleanupFolders$3(AlertDialog alertDialog) {
        long j;
        long j2;
        long j3;
        int i;
        int i2;
        File file;
        File checkDirectory;
        int i3 = 1;
        long j4 = 0;
        boolean z = false;
        int i4 = 0;
        boolean z2 = true;
        while (i4 < 7) {
            StorageDiagramView.ClearViewData[] clearViewDataArr = this.clearViewData;
            if (clearViewDataArr[i4] != null && clearViewDataArr[i4].clear) {
                if (i4 == 0) {
                    j4 += this.photoSize;
                    i2 = 0;
                } else if (i4 == i3) {
                    j4 += this.videoSize;
                    i2 = 2;
                } else {
                    if (i4 == 2) {
                        j4 += this.documentsSize;
                        i2 = 3;
                        i = 1;
                    } else if (i4 == 3) {
                        j4 += this.musicSize;
                        i2 = 3;
                        i = 2;
                    } else if (i4 == 4) {
                        j4 += this.audioSize;
                        i2 = 1;
                    } else if (i4 == 5) {
                        j4 += this.stickersSize;
                        i2 = 100;
                    } else if (i4 == 6) {
                        j4 += this.cacheSize;
                        i2 = 4;
                    } else {
                        i2 = -1;
                    }
                    if (i2 != -1) {
                        if (i2 == 100) {
                            file = new File(FileLoader.checkDirectory(4), "acache");
                        } else {
                            file = FileLoader.checkDirectory(i2);
                        }
                        if (file != null) {
                            Utilities.clearDir(file.getAbsolutePath(), i, Long.MAX_VALUE, false);
                        }
                        if (i2 == 0 || i2 == 2) {
                            File checkDirectory2 = FileLoader.checkDirectory(i2 == 0 ? 100 : 101);
                            if (checkDirectory2 != null) {
                                Utilities.clearDir(checkDirectory2.getAbsolutePath(), i, Long.MAX_VALUE, false);
                            }
                        }
                        if (i2 == 3 && (checkDirectory = FileLoader.checkDirectory(5)) != null) {
                            Utilities.clearDir(checkDirectory.getAbsolutePath(), i, Long.MAX_VALUE, false);
                        }
                        if (i2 == 4) {
                            this.cacheSize = getDirectorySize(FileLoader.checkDirectory(4), i);
                            z = true;
                        } else if (i2 == 1) {
                            this.audioSize = getDirectorySize(FileLoader.checkDirectory(1), i);
                        } else if (i2 != 3) {
                            if (i2 == 0) {
                                long directorySize = getDirectorySize(FileLoader.checkDirectory(0), i);
                                this.photoSize = directorySize;
                                this.photoSize = directorySize + getDirectorySize(FileLoader.checkDirectory(100), i);
                            } else if (i2 == 2) {
                                long directorySize2 = getDirectorySize(FileLoader.checkDirectory(2), i);
                                this.videoSize = directorySize2;
                                this.videoSize = directorySize2 + getDirectorySize(FileLoader.checkDirectory(101), i);
                            } else if (i2 == 100) {
                                this.stickersSize = getDirectorySize(new File(FileLoader.checkDirectory(4), "acache"), i);
                            }
                            z = true;
                        } else if (i == 1) {
                            long directorySize3 = getDirectorySize(FileLoader.checkDirectory(3), i);
                            this.documentsSize = directorySize3;
                            this.documentsSize = directorySize3 + getDirectorySize(FileLoader.checkDirectory(5), i);
                        } else {
                            long directorySize4 = getDirectorySize(FileLoader.checkDirectory(3), i);
                            this.musicSize = directorySize4;
                            this.musicSize = directorySize4 + getDirectorySize(FileLoader.checkDirectory(5), i);
                        }
                    }
                }
                i = 0;
                if (i2 != -1) {
                }
            } else if (clearViewDataArr[i4] != null) {
                z2 = false;
            }
            i4++;
            i3 = 1;
        }
        this.totalSize = this.cacheSize + this.videoSize + this.audioSize + this.photoSize + this.documentsSize + this.musicSize + this.stickersSize;
        StatFs statFs = new StatFs(Environment.getDataDirectory().getPath());
        int i5 = Build.VERSION.SDK_INT;
        if (i5 >= 18) {
            j = statFs.getBlockSizeLong();
        } else {
            j = statFs.getBlockSize();
        }
        if (i5 >= 18) {
            j2 = statFs.getAvailableBlocksLong();
        } else {
            j2 = statFs.getAvailableBlocks();
        }
        if (i5 >= 18) {
            j3 = statFs.getBlockCountLong();
        } else {
            j3 = statFs.getBlockCount();
        }
        this.totalDeviceSize = j3 * j;
        this.totalDeviceFreeSize = j2 * j;
        if (z2) {
            FileLoader.getInstance(this.currentAccount).clearFilePaths();
        }
        FileLoader.getInstance(this.currentAccount).checkCurrentDownloadsFiles();
        AndroidUtilities.runOnUIThread(new CacheControlActivity$$ExternalSyntheticLambda6(this, z, alertDialog, j4));
    }

    public /* synthetic */ void lambda$cleanupFolders$2(boolean z, AlertDialog alertDialog, long j) {
        if (z) {
            ImageLoader.getInstance().clearMemory();
        }
        if (this.listAdapter != null) {
            updateStorageUsageRow();
        }
        try {
            alertDialog.dismiss();
        } catch (Exception e) {
            FileLog.e(e);
        }
        getMediaDataController().ringtoneDataStore.checkRingtoneSoundsLoaded();
        this.cacheRemovedTooltip.setInfoText(LocaleController.formatString("CacheWasCleared", 2131624769, AndroidUtilities.formatFileSize(j)));
        this.cacheRemovedTooltip.showWithAction(0L, 19, null, null);
        MediaDataController.getInstance(this.currentAccount).chekAllMedia(true);
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public View createView(Context context) {
        this.actionBar.setBackButtonImage(2131165449);
        this.actionBar.setAllowOverlayTitle(true);
        this.actionBar.setTitle(LocaleController.getString("StorageUsage", 2131628481));
        this.actionBar.setActionBarMenuOnItemClick(new AnonymousClass1());
        this.listAdapter = new ListAdapter(context);
        FrameLayout frameLayout = new FrameLayout(context);
        this.fragmentView = frameLayout;
        FrameLayout frameLayout2 = frameLayout;
        frameLayout2.setBackgroundColor(Theme.getColor("windowBackgroundGray"));
        RecyclerListView recyclerListView = new RecyclerListView(context);
        this.listView = recyclerListView;
        recyclerListView.setVerticalScrollBarEnabled(false);
        RecyclerListView recyclerListView2 = this.listView;
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, 1, false);
        this.layoutManager = linearLayoutManager;
        recyclerListView2.setLayoutManager(linearLayoutManager);
        frameLayout2.addView(this.listView, LayoutHelper.createFrame(-1, -1.0f));
        this.listView.setAdapter(this.listAdapter);
        this.listView.setOnItemClickListener(new CacheControlActivity$$ExternalSyntheticLambda8(this, context));
        UndoView undoView = new UndoView(context);
        this.cacheRemovedTooltip = undoView;
        frameLayout2.addView(undoView, LayoutHelper.createFrame(-1, -2.0f, 83, 8.0f, 0.0f, 8.0f, 8.0f));
        return this.fragmentView;
    }

    /* renamed from: org.telegram.ui.CacheControlActivity$1 */
    /* loaded from: classes3.dex */
    class AnonymousClass1 extends ActionBar.ActionBarMenuOnItemClick {
        AnonymousClass1() {
            CacheControlActivity.this = r1;
        }

        @Override // org.telegram.ui.ActionBar.ActionBar.ActionBarMenuOnItemClick
        public void onItemClick(int i) {
            if (i == -1) {
                CacheControlActivity.this.finishFragment();
            }
        }
    }

    public /* synthetic */ void lambda$createView$6(Context context, View view, int i) {
        long j;
        String str;
        String str2;
        if (getParentActivity() == null) {
            return;
        }
        if (i == this.migrateOldFolderRow) {
            if (Build.VERSION.SDK_INT < 30) {
                return;
            }
            migrateOldFolder();
        } else if (i == this.databaseRow) {
            clearDatabase();
        } else if (i != this.storageUsageRow) {
        } else {
            long j2 = 0;
            if (this.totalSize <= 0 || getParentActivity() == null) {
                return;
            }
            AnonymousClass2 anonymousClass2 = new AnonymousClass2(this, getParentActivity(), false);
            this.bottomSheet = anonymousClass2;
            anonymousClass2.fixNavigationBar();
            this.bottomSheet.setAllowNestedScroll(true);
            this.bottomSheet.setApplyBottomPadding(false);
            LinearLayout linearLayout = new LinearLayout(getParentActivity());
            this.bottomSheetView = linearLayout;
            linearLayout.setOrientation(1);
            StorageDiagramView storageDiagramView = new StorageDiagramView(context);
            linearLayout.addView(storageDiagramView, LayoutHelper.createLinear(-2, -2, 1, 0, 16, 0, 16));
            CheckBoxCell checkBoxCell = null;
            int i2 = 0;
            while (i2 < 7) {
                if (i2 == 0) {
                    j = this.photoSize;
                    str2 = LocaleController.getString("LocalPhotoCache", 2131626487);
                    str = "statisticChartLine_blue";
                } else if (i2 == 1) {
                    j = this.videoSize;
                    str2 = LocaleController.getString("LocalVideoCache", 2131626488);
                    str = "statisticChartLine_golden";
                } else if (i2 == 2) {
                    j = this.documentsSize;
                    str2 = LocaleController.getString("LocalDocumentCache", 2131626484);
                    str = "statisticChartLine_green";
                } else if (i2 == 3) {
                    j = this.musicSize;
                    str2 = LocaleController.getString("LocalMusicCache", 2131626486);
                    str = "statisticChartLine_indigo";
                } else if (i2 == 4) {
                    j = this.audioSize;
                    str2 = LocaleController.getString("LocalAudioCache", 2131626477);
                    str = "statisticChartLine_red";
                } else if (i2 == 5) {
                    j = this.stickersSize;
                    str2 = LocaleController.getString("AnimatedStickers", 2131624355);
                    str = "statisticChartLine_lightgreen";
                } else {
                    j = this.cacheSize;
                    str2 = LocaleController.getString("LocalCache", 2131626478);
                    str = "statisticChartLine_lightblue";
                }
                if (j > j2) {
                    this.clearViewData[i2] = new StorageDiagramView.ClearViewData(storageDiagramView);
                    StorageDiagramView.ClearViewData[] clearViewDataArr = this.clearViewData;
                    clearViewDataArr[i2].size = j;
                    clearViewDataArr[i2].color = str;
                    checkBoxCell = new CheckBoxCell(getParentActivity(), 4, 21, null);
                    checkBoxCell.setTag(Integer.valueOf(i2));
                    checkBoxCell.setBackgroundDrawable(Theme.getSelectorDrawable(false));
                    linearLayout.addView(checkBoxCell, LayoutHelper.createLinear(-1, 50));
                    checkBoxCell.setText(str2, AndroidUtilities.formatFileSize(j), true, true);
                    checkBoxCell.setTextColor(Theme.getColor("dialogTextBlack"));
                    checkBoxCell.setCheckBoxColor(str, "windowBackgroundWhiteGrayIcon", "checkboxCheck");
                    checkBoxCell.setOnClickListener(new CacheControlActivity$$ExternalSyntheticLambda1(this));
                } else {
                    this.clearViewData[i2] = null;
                }
                i2++;
                j2 = 0;
            }
            if (checkBoxCell != null) {
                checkBoxCell.setNeedDivider(false);
            }
            storageDiagramView.setData(this.clearViewData);
            BottomSheet.BottomSheetCell bottomSheetCell = new BottomSheet.BottomSheetCell(getParentActivity(), 2);
            bottomSheetCell.setTextAndIcon(LocaleController.getString("ClearMediaCache", 2131625146), 0);
            this.actionTextView = bottomSheetCell.getTextView();
            bottomSheetCell.getTextView().setOnClickListener(new CacheControlActivity$$ExternalSyntheticLambda2(this));
            linearLayout.addView(bottomSheetCell, LayoutHelper.createLinear(-1, 50));
            NestedScrollView nestedScrollView = new NestedScrollView(context);
            nestedScrollView.setVerticalScrollBarEnabled(false);
            nestedScrollView.addView(linearLayout);
            this.bottomSheet.setCustomView(nestedScrollView);
            showDialog(this.bottomSheet);
        }
    }

    /* renamed from: org.telegram.ui.CacheControlActivity$2 */
    /* loaded from: classes3.dex */
    public class AnonymousClass2 extends BottomSheet {
        @Override // org.telegram.ui.ActionBar.BottomSheet
        protected boolean canDismissWithSwipe() {
            return false;
        }

        AnonymousClass2(CacheControlActivity cacheControlActivity, Context context, boolean z) {
            super(context, z);
        }
    }

    public /* synthetic */ void lambda$createView$4(View view) {
        int i = 0;
        int i2 = 0;
        while (true) {
            StorageDiagramView.ClearViewData[] clearViewDataArr = this.clearViewData;
            if (i >= clearViewDataArr.length) {
                break;
            }
            if (clearViewDataArr[i] != null && clearViewDataArr[i].clear) {
                i2++;
            }
            i++;
        }
        CheckBoxCell checkBoxCell = (CheckBoxCell) view;
        int intValue = ((Integer) checkBoxCell.getTag()).intValue();
        if (i2 == 1 && this.clearViewData[intValue].clear) {
            AndroidUtilities.shakeView(checkBoxCell.getCheckBoxView(), 2.0f, 0);
            return;
        }
        StorageDiagramView.ClearViewData[] clearViewDataArr2 = this.clearViewData;
        clearViewDataArr2[intValue].setClear(!clearViewDataArr2[intValue].clear);
        checkBoxCell.setChecked(this.clearViewData[intValue].clear, true);
    }

    public /* synthetic */ void lambda$createView$5(View view) {
        try {
            Dialog dialog = this.visibleDialog;
            if (dialog != null) {
                dialog.dismiss();
            }
        } catch (Exception e) {
            FileLog.e(e);
        }
        cleanupFolders();
    }

    private void migrateOldFolder() {
        FilesMigrationService.checkBottomSheet(this);
    }

    private void clearDatabase() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getParentActivity());
        builder.setTitle(LocaleController.getString("LocalDatabaseClearTextTitle", 2131626481));
        builder.setMessage(LocaleController.getString("LocalDatabaseClearText", 2131626480));
        builder.setNegativeButton(LocaleController.getString("Cancel", 2131624819), null);
        builder.setPositiveButton(LocaleController.getString("CacheClear", 2131624767), new CacheControlActivity$$ExternalSyntheticLambda0(this));
        AlertDialog create = builder.create();
        showDialog(create);
        TextView textView = (TextView) create.getButton(-1);
        if (textView != null) {
            textView.setTextColor(Theme.getColor("dialogTextRed2"));
        }
    }

    public /* synthetic */ void lambda$clearDatabase$7(DialogInterface dialogInterface, int i) {
        if (getParentActivity() == null) {
            return;
        }
        AlertDialog alertDialog = new AlertDialog(getParentActivity(), 3);
        this.progressDialog = alertDialog;
        alertDialog.setCanCancel(false);
        this.progressDialog.showDelayed(500L);
        MessagesController.getInstance(this.currentAccount).clearQueryTime();
        getMessagesStorage().clearLocalDatabase();
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public void onResume() {
        super.onResume();
        ListAdapter listAdapter = this.listAdapter;
        if (listAdapter != null) {
            listAdapter.notifyDataSetChanged();
        }
    }

    @Override // org.telegram.messenger.NotificationCenter.NotificationCenterDelegate
    public void didReceivedNotification(int i, int i2, Object... objArr) {
        if (i == NotificationCenter.didClearDatabase) {
            try {
                AlertDialog alertDialog = this.progressDialog;
                if (alertDialog != null) {
                    alertDialog.dismiss();
                }
            } catch (Exception e) {
                FileLog.e(e);
            }
            this.progressDialog = null;
            if (this.listAdapter == null) {
                return;
            }
            this.databaseSize = MessagesStorage.getInstance(this.currentAccount).getDatabaseSize();
            this.listAdapter.notifyDataSetChanged();
        }
    }

    /* loaded from: classes3.dex */
    public class ListAdapter extends RecyclerListView.SelectionAdapter {
        private Context mContext;

        public ListAdapter(Context context) {
            CacheControlActivity.this = r1;
            this.mContext = context;
        }

        @Override // org.telegram.ui.Components.RecyclerListView.SelectionAdapter
        public boolean isEnabled(RecyclerView.ViewHolder viewHolder) {
            int adapterPosition = viewHolder.getAdapterPosition();
            return ((long) adapterPosition) == CacheControlActivity.this.migrateOldFolderRow || adapterPosition == CacheControlActivity.this.databaseRow || (adapterPosition == CacheControlActivity.this.storageUsageRow && CacheControlActivity.this.totalSize > 0 && !CacheControlActivity.this.calculating);
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public int getItemCount() {
            return CacheControlActivity.this.rowCount;
        }

        public static /* synthetic */ void lambda$onCreateViewHolder$0(int i) {
            if (i == 0) {
                SharedConfig.setKeepMedia(3);
            } else if (i == 1) {
                SharedConfig.setKeepMedia(0);
            } else if (i == 2) {
                SharedConfig.setKeepMedia(1);
            } else if (i != 3) {
            } else {
                SharedConfig.setKeepMedia(2);
            }
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View view;
            SlideChooseView slideChooseView;
            if (i == 0) {
                TextSettingsCell textSettingsCell = new TextSettingsCell(this.mContext);
                textSettingsCell.setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
                slideChooseView = textSettingsCell;
            } else if (i == 2) {
                StroageUsageView stroageUsageView = new StroageUsageView(this.mContext);
                stroageUsageView.setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
                slideChooseView = stroageUsageView;
            } else if (i == 3) {
                HeaderCell headerCell = new HeaderCell(this.mContext);
                headerCell.setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
                slideChooseView = headerCell;
            } else if (i == 4) {
                SlideChooseView slideChooseView2 = new SlideChooseView(this.mContext);
                slideChooseView2.setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
                MessagesController.getGlobalMainSettings();
                slideChooseView2.setCallback(CacheControlActivity$ListAdapter$$ExternalSyntheticLambda0.INSTANCE);
                int i2 = SharedConfig.keepMedia;
                slideChooseView2.setOptions(i2 == 3 ? 0 : i2 + 1, LocaleController.formatPluralString("Days", 3, new Object[0]), LocaleController.formatPluralString("Weeks", 1, new Object[0]), LocaleController.formatPluralString("Months", 1, new Object[0]), LocaleController.getString("KeepMediaForever", 2131626341));
                slideChooseView = slideChooseView2;
            } else {
                view = new TextInfoPrivacyCell(this.mContext);
                return new RecyclerListView.Holder(view);
            }
            view = slideChooseView;
            return new RecyclerListView.Holder(view);
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
            int itemViewType = viewHolder.getItemViewType();
            if (itemViewType == 0) {
                TextSettingsCell textSettingsCell = (TextSettingsCell) viewHolder.itemView;
                if (i == CacheControlActivity.this.databaseRow) {
                    textSettingsCell.setTextAndValue(LocaleController.getString("ClearLocalDatabase", 2131625145), AndroidUtilities.formatFileSize(CacheControlActivity.this.databaseSize), false);
                } else if (i != CacheControlActivity.this.migrateOldFolderRow) {
                } else {
                    textSettingsCell.setTextAndValue(LocaleController.getString("MigrateOldFolder", 2131626697), null, false);
                }
            } else if (itemViewType != 1) {
                if (itemViewType == 2) {
                    ((StroageUsageView) viewHolder.itemView).setStorageUsage(CacheControlActivity.this.calculating, CacheControlActivity.this.databaseSize, CacheControlActivity.this.totalSize, CacheControlActivity.this.totalDeviceFreeSize, CacheControlActivity.this.totalDeviceSize);
                } else if (itemViewType != 3) {
                } else {
                    HeaderCell headerCell = (HeaderCell) viewHolder.itemView;
                    if (i != CacheControlActivity.this.keepMediaHeaderRow) {
                        if (i != CacheControlActivity.this.deviseStorageHeaderRow) {
                            return;
                        }
                        headerCell.setText(LocaleController.getString("DeviceStorage", 2131625464));
                        return;
                    }
                    headerCell.setText(LocaleController.getString("KeepMedia", 2131626340));
                }
            } else {
                TextInfoPrivacyCell textInfoPrivacyCell = (TextInfoPrivacyCell) viewHolder.itemView;
                if (i != CacheControlActivity.this.databaseInfoRow) {
                    if (i != CacheControlActivity.this.cacheInfoRow) {
                        if (i != CacheControlActivity.this.keepMediaInfoRow) {
                            return;
                        }
                        textInfoPrivacyCell.setText(AndroidUtilities.replaceTags(LocaleController.getString("KeepMediaInfo", 2131626342)));
                        textInfoPrivacyCell.setBackgroundDrawable(Theme.getThemedDrawable(this.mContext, 2131165435, "windowBackgroundGrayShadow"));
                        return;
                    }
                    textInfoPrivacyCell.setText("");
                    textInfoPrivacyCell.setBackgroundDrawable(Theme.getThemedDrawable(this.mContext, 2131165435, "windowBackgroundGrayShadow"));
                    return;
                }
                textInfoPrivacyCell.setText(LocaleController.getString("LocalDatabaseInfo", 2131626482));
                textInfoPrivacyCell.setBackgroundDrawable(Theme.getThemedDrawable(this.mContext, 2131165436, "windowBackgroundGrayShadow"));
            }
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public int getItemViewType(int i) {
            if (i == CacheControlActivity.this.databaseInfoRow || i == CacheControlActivity.this.cacheInfoRow || i == CacheControlActivity.this.keepMediaInfoRow) {
                return 1;
            }
            if (i == CacheControlActivity.this.storageUsageRow) {
                return 2;
            }
            if (i == CacheControlActivity.this.keepMediaHeaderRow || i == CacheControlActivity.this.deviseStorageHeaderRow) {
                return 3;
            }
            return i == CacheControlActivity.this.keepMediaChooserRow ? 4 : 0;
        }
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public ArrayList<ThemeDescription> getThemeDescriptions() {
        CacheControlActivity$$ExternalSyntheticLambda7 cacheControlActivity$$ExternalSyntheticLambda7 = new CacheControlActivity$$ExternalSyntheticLambda7(this);
        ArrayList<ThemeDescription> arrayList = new ArrayList<>();
        arrayList.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_CELLBACKGROUNDCOLOR, new Class[]{TextSettingsCell.class, SlideChooseView.class, StroageUsageView.class, HeaderCell.class}, null, null, null, "windowBackgroundWhite"));
        arrayList.add(new ThemeDescription(this.fragmentView, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, "windowBackgroundGray"));
        arrayList.add(new ThemeDescription(this.actionBar, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, "actionBarDefault"));
        arrayList.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_LISTGLOWCOLOR, null, null, null, null, "actionBarDefault"));
        arrayList.add(new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_ITEMSCOLOR, null, null, null, null, "actionBarDefaultIcon"));
        arrayList.add(new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_TITLECOLOR, null, null, null, null, "actionBarDefaultTitle"));
        arrayList.add(new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_SELECTORCOLOR, null, null, null, null, "actionBarDefaultSelector"));
        arrayList.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_SELECTOR, null, null, null, null, "listSelectorSDK21"));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{TextSettingsCell.class}, new String[]{"textView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "windowBackgroundWhiteBlackText"));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{TextSettingsCell.class}, new String[]{"valueTextView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "windowBackgroundWhiteValueText"));
        arrayList.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_BACKGROUNDFILTER, new Class[]{TextInfoPrivacyCell.class}, null, null, null, "windowBackgroundGrayShadow"));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{TextInfoPrivacyCell.class}, new String[]{"textView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "windowBackgroundWhiteGrayText4"));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{HeaderCell.class}, new String[]{"textView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "windowBackgroundWhiteBlueHeader"));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{StroageUsageView.class}, new String[]{"paintFill"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "player_progressBackground"));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{StroageUsageView.class}, new String[]{"paintProgress"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "player_progress"));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{StroageUsageView.class}, new String[]{"telegramCacheTextView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "windowBackgroundWhiteGrayText"));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{StroageUsageView.class}, new String[]{"freeSizeTextView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "windowBackgroundWhiteGrayText"));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{StroageUsageView.class}, new String[]{"calculationgTextView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "windowBackgroundWhiteGrayText"));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{StroageUsageView.class}, new String[]{"paintProgress2"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "player_progressBackground2"));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{SlideChooseView.class}, null, null, null, "switchTrack"));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{SlideChooseView.class}, null, null, null, "switchTrackChecked"));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{SlideChooseView.class}, null, null, null, "windowBackgroundWhiteGrayText"));
        arrayList.add(new ThemeDescription(this.bottomSheetView, 0, null, null, null, null, "windowBackgroundWhiteGrayText"));
        arrayList.add(new ThemeDescription(this.bottomSheetView, 0, new Class[]{CheckBoxCell.class}, new String[]{"textView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "windowBackgroundWhiteBlackText"));
        arrayList.add(new ThemeDescription(this.bottomSheetView, 0, new Class[]{CheckBoxCell.class}, new String[]{"valueTextView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "windowBackgroundWhiteValueText"));
        arrayList.add(new ThemeDescription(this.bottomSheetView, 0, new Class[]{CheckBoxCell.class}, Theme.dividerPaint, null, null, "divider"));
        arrayList.add(new ThemeDescription(this.bottomSheetView, 0, new Class[]{StorageDiagramView.class}, null, null, null, "windowBackgroundWhiteBlackText"));
        arrayList.add(new ThemeDescription((View) null, 0, new Class[]{TextCheckBoxCell.class}, new String[]{"textView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "windowBackgroundWhiteBlackText"));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, cacheControlActivity$$ExternalSyntheticLambda7, "dialogBackground"));
        arrayList.add(new ThemeDescription(this.bottomSheetView, 0, null, null, null, null, "statisticChartLine_blue"));
        arrayList.add(new ThemeDescription(this.bottomSheetView, 0, null, null, null, null, "statisticChartLine_green"));
        arrayList.add(new ThemeDescription(this.bottomSheetView, 0, null, null, null, null, "statisticChartLine_red"));
        arrayList.add(new ThemeDescription(this.bottomSheetView, 0, null, null, null, null, "statisticChartLine_golden"));
        arrayList.add(new ThemeDescription(this.bottomSheetView, 0, null, null, null, null, "statisticChartLine_lightblue"));
        arrayList.add(new ThemeDescription(this.bottomSheetView, 0, null, null, null, null, "statisticChartLine_lightgreen"));
        arrayList.add(new ThemeDescription(this.bottomSheetView, 0, null, null, null, null, "statisticChartLine_orange"));
        arrayList.add(new ThemeDescription(this.bottomSheetView, 0, null, null, null, null, "statisticChartLine_indigo"));
        return arrayList;
    }

    public /* synthetic */ void lambda$getThemeDescriptions$8() {
        BottomSheet bottomSheet = this.bottomSheet;
        if (bottomSheet != null) {
            bottomSheet.setBackgroundColor(Theme.getColor("dialogBackground"));
        }
        View view = this.actionTextView;
        if (view != null) {
            view.setBackground(Theme.AdaptiveRipple.filledRect("featuredStickers_addButton", 4.0f));
        }
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public void onRequestPermissionsResultFragment(int i, String[] strArr, int[] iArr) {
        FilesMigrationService.FilesMigrationBottomSheet filesMigrationBottomSheet;
        if (i == 4) {
            boolean z = false;
            int i2 = 0;
            while (true) {
                if (i2 >= iArr.length) {
                    z = true;
                    break;
                } else if (iArr[i2] != 0) {
                    break;
                } else {
                    i2++;
                }
            }
            if (!z || Build.VERSION.SDK_INT < 30 || (filesMigrationBottomSheet = FilesMigrationService.filesMigrationBottomSheet) == null) {
                return;
            }
            filesMigrationBottomSheet.migrateOldFolder();
        }
    }
}