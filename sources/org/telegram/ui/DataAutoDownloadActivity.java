package org.telegram.ui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.Collections;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.DownloadController;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.R;
import org.telegram.ui.ActionBar.ActionBar;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.ActionBar.BottomSheet;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.ActionBar.ThemeDescription;
import org.telegram.ui.Cells.HeaderCell;
import org.telegram.ui.Cells.MaxFileSizeCell;
import org.telegram.ui.Cells.NotificationsCheckCell;
import org.telegram.ui.Cells.ShadowSectionCell;
import org.telegram.ui.Cells.TextCheckBoxCell;
import org.telegram.ui.Cells.TextCheckCell;
import org.telegram.ui.Cells.TextInfoPrivacyCell;
import org.telegram.ui.Components.CombinedDrawable;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.ui.Components.RecyclerListView;
import org.telegram.ui.Components.SlideChooseView;
import org.telegram.ui.DataAutoDownloadActivity;
/* loaded from: classes3.dex */
public class DataAutoDownloadActivity extends BaseFragment {
    private boolean animateChecked;
    private int autoDownloadRow;
    private int autoDownloadSectionRow;
    private int currentPresetNum;
    private int currentType;
    private DownloadController.Preset defaultPreset;
    private int filesRow;
    private String key;
    private String key2;
    private ListAdapter listAdapter;
    private RecyclerListView listView;
    private int photosRow;
    private int rowCount;
    private int typeHeaderRow;
    private DownloadController.Preset typePreset;
    private int typeSectionRow;
    private int usageHeaderRow;
    private int usageProgressRow;
    private int usageSectionRow;
    private int videosRow;
    private boolean wereAnyChanges;
    private ArrayList<DownloadController.Preset> presets = new ArrayList<>();
    private int selectedPreset = 1;
    private DownloadController.Preset lowPreset = DownloadController.getInstance(this.currentAccount).lowPreset;
    private DownloadController.Preset mediumPreset = DownloadController.getInstance(this.currentAccount).mediumPreset;
    private DownloadController.Preset highPreset = DownloadController.getInstance(this.currentAccount).highPreset;

    public DataAutoDownloadActivity(int i) {
        this.currentType = i;
        int i2 = this.currentType;
        if (i2 == 0) {
            this.currentPresetNum = DownloadController.getInstance(this.currentAccount).currentMobilePreset;
            this.typePreset = DownloadController.getInstance(this.currentAccount).mobilePreset;
            this.defaultPreset = this.mediumPreset;
            this.key = "mobilePreset";
            this.key2 = "currentMobilePreset";
        } else if (i2 == 1) {
            this.currentPresetNum = DownloadController.getInstance(this.currentAccount).currentWifiPreset;
            this.typePreset = DownloadController.getInstance(this.currentAccount).wifiPreset;
            this.defaultPreset = this.highPreset;
            this.key = "wifiPreset";
            this.key2 = "currentWifiPreset";
        } else {
            this.currentPresetNum = DownloadController.getInstance(this.currentAccount).currentRoamingPreset;
            this.typePreset = DownloadController.getInstance(this.currentAccount).roamingPreset;
            this.defaultPreset = this.lowPreset;
            this.key = "roamingPreset";
            this.key2 = "currentRoamingPreset";
        }
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public boolean onFragmentCreate() {
        super.onFragmentCreate();
        fillPresets();
        updateRows();
        return true;
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public View createView(Context context) {
        this.actionBar.setBackButtonImage(R.drawable.ic_ab_back);
        int i = this.currentType;
        if (i == 0) {
            this.actionBar.setTitle(LocaleController.getString("AutoDownloadOnMobileData", R.string.AutoDownloadOnMobileData));
        } else if (i == 1) {
            this.actionBar.setTitle(LocaleController.getString("AutoDownloadOnWiFiData", R.string.AutoDownloadOnWiFiData));
        } else if (i == 2) {
            this.actionBar.setTitle(LocaleController.getString("AutoDownloadOnRoamingData", R.string.AutoDownloadOnRoamingData));
        }
        if (AndroidUtilities.isTablet()) {
            this.actionBar.setOccupyStatusBar(false);
        }
        this.actionBar.setAllowOverlayTitle(true);
        this.actionBar.setActionBarMenuOnItemClick(new ActionBar.ActionBarMenuOnItemClick() { // from class: org.telegram.ui.DataAutoDownloadActivity.1
            @Override // org.telegram.ui.ActionBar.ActionBar.ActionBarMenuOnItemClick
            public void onItemClick(int i2) {
                if (i2 == -1) {
                    DataAutoDownloadActivity.this.finishFragment();
                }
            }
        });
        this.listAdapter = new ListAdapter(context);
        FrameLayout frameLayout = new FrameLayout(context);
        this.fragmentView = frameLayout;
        frameLayout.setBackgroundColor(Theme.getColor(Theme.key_windowBackgroundGray));
        RecyclerListView recyclerListView = new RecyclerListView(context);
        this.listView = recyclerListView;
        recyclerListView.setVerticalScrollBarEnabled(false);
        ((DefaultItemAnimator) this.listView.getItemAnimator()).setDelayAnimations(false);
        this.listView.setLayoutManager(new LinearLayoutManager(context, 1, false));
        ((FrameLayout) this.fragmentView).addView(this.listView, LayoutHelper.createFrame(-1, -1, 51));
        this.listView.setAdapter(this.listAdapter);
        this.listView.setOnItemClickListener(new RecyclerListView.OnItemClickListenerExtended() { // from class: org.telegram.ui.DataAutoDownloadActivity$$ExternalSyntheticLambda5
            @Override // org.telegram.ui.Components.RecyclerListView.OnItemClickListenerExtended
            public /* synthetic */ boolean hasDoubleTap(View view, int i2) {
                return RecyclerListView.OnItemClickListenerExtended.-CC.$default$hasDoubleTap(this, view, i2);
            }

            @Override // org.telegram.ui.Components.RecyclerListView.OnItemClickListenerExtended
            public /* synthetic */ void onDoubleTap(View view, int i2, float f, float f2) {
                RecyclerListView.OnItemClickListenerExtended.-CC.$default$onDoubleTap(this, view, i2, f, f2);
            }

            @Override // org.telegram.ui.Components.RecyclerListView.OnItemClickListenerExtended
            public final void onItemClick(View view, int i2, float f, float f2) {
                DataAutoDownloadActivity.this.lambda$createView$4(view, i2, f, f2);
            }
        });
        return this.fragmentView;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:20:0x0058  */
    /* JADX WARN: Removed duplicated region for block: B:21:0x005b  */
    /* JADX WARN: Removed duplicated region for block: B:24:0x006c  */
    /* JADX WARN: Removed duplicated region for block: B:25:0x006f  */
    /* JADX WARN: Removed duplicated region for block: B:28:0x0081  */
    /* JADX WARN: Removed duplicated region for block: B:29:0x008a  */
    /* JADX WARN: Removed duplicated region for block: B:32:0x00b9  */
    /* JADX WARN: Removed duplicated region for block: B:33:0x00c4  */
    /* JADX WARN: Type inference failed for: r0v32 */
    /* JADX WARN: Type inference failed for: r0v33, types: [boolean] */
    /* JADX WARN: Type inference failed for: r0v34 */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public /* synthetic */ void lambda$createView$4(final View view, final int i, float f, float f2) {
        int i2;
        DownloadController.Preset currentRoamingPreset;
        String str;
        String str2;
        final TextCheckCell[] textCheckCellArr;
        DownloadController.Preset preset;
        boolean z;
        ?? r0;
        MaxFileSizeCell[] maxFileSizeCellArr;
        boolean z2;
        int i3;
        int i4 = 4;
        boolean z3 = false;
        int i5 = 0;
        if (i == this.autoDownloadRow) {
            int i6 = this.currentPresetNum;
            if (i6 != 3) {
                if (i6 == 0) {
                    this.typePreset.set(this.lowPreset);
                } else if (i6 == 1) {
                    this.typePreset.set(this.mediumPreset);
                } else if (i6 == 2) {
                    this.typePreset.set(this.highPreset);
                }
            }
            TextCheckCell textCheckCell = (TextCheckCell) view;
            boolean isChecked = textCheckCell.isChecked();
            if (!isChecked) {
                DownloadController.Preset preset2 = this.typePreset;
                if (preset2.enabled) {
                    System.arraycopy(this.defaultPreset.mask, 0, preset2.mask, 0, 4);
                    view.setTag(Integer.valueOf(!this.typePreset.enabled ? Theme.key_windowBackgroundChecked : Theme.key_windowBackgroundUnchecked));
                    textCheckCell.setBackgroundColorAnimated(!isChecked, Theme.getColor(!this.typePreset.enabled ? Theme.key_windowBackgroundChecked : Theme.key_windowBackgroundUnchecked));
                    updateRows();
                    if (!this.typePreset.enabled) {
                        this.listAdapter.notifyItemRangeInserted(this.autoDownloadSectionRow + 1, 8);
                    } else {
                        this.listAdapter.notifyItemRangeRemoved(this.autoDownloadSectionRow + 1, 8);
                    }
                    this.listAdapter.notifyItemChanged(this.autoDownloadSectionRow);
                    SharedPreferences.Editor edit = MessagesController.getMainSettings(this.currentAccount).edit();
                    edit.putString(this.key, this.typePreset.toString());
                    String str3 = this.key2;
                    this.currentPresetNum = 3;
                    edit.putInt(str3, 3);
                    i3 = this.currentType;
                    if (i3 != 0) {
                        DownloadController.getInstance(this.currentAccount).currentMobilePreset = this.currentPresetNum;
                    } else if (i3 == 1) {
                        DownloadController.getInstance(this.currentAccount).currentWifiPreset = this.currentPresetNum;
                    } else {
                        DownloadController.getInstance(this.currentAccount).currentRoamingPreset = this.currentPresetNum;
                    }
                    edit.commit();
                    textCheckCell.setChecked(!isChecked);
                    DownloadController.getInstance(this.currentAccount).checkAutodownloadSettings();
                    this.wereAnyChanges = true;
                }
            }
            DownloadController.Preset preset3 = this.typePreset;
            preset3.enabled = !preset3.enabled;
            view.setTag(Integer.valueOf(!this.typePreset.enabled ? Theme.key_windowBackgroundChecked : Theme.key_windowBackgroundUnchecked));
            textCheckCell.setBackgroundColorAnimated(!isChecked, Theme.getColor(!this.typePreset.enabled ? Theme.key_windowBackgroundChecked : Theme.key_windowBackgroundUnchecked));
            updateRows();
            if (!this.typePreset.enabled) {
            }
            this.listAdapter.notifyItemChanged(this.autoDownloadSectionRow);
            SharedPreferences.Editor edit2 = MessagesController.getMainSettings(this.currentAccount).edit();
            edit2.putString(this.key, this.typePreset.toString());
            String str32 = this.key2;
            this.currentPresetNum = 3;
            edit2.putInt(str32, 3);
            i3 = this.currentType;
            if (i3 != 0) {
            }
            edit2.commit();
            textCheckCell.setChecked(!isChecked);
            DownloadController.getInstance(this.currentAccount).checkAutodownloadSettings();
            this.wereAnyChanges = true;
        } else if ((i == this.photosRow || i == this.videosRow || i == this.filesRow) && view.isEnabled()) {
            if (i == this.photosRow) {
                i2 = 1;
            } else {
                i2 = i == this.videosRow ? 4 : 8;
            }
            final int typeToIndex = DownloadController.typeToIndex(i2);
            int i7 = this.currentType;
            if (i7 == 0) {
                currentRoamingPreset = DownloadController.getInstance(this.currentAccount).getCurrentMobilePreset();
                str = "mobilePreset";
                str2 = "currentMobilePreset";
            } else if (i7 == 1) {
                currentRoamingPreset = DownloadController.getInstance(this.currentAccount).getCurrentWiFiPreset();
                str = "wifiPreset";
                str2 = "currentWifiPreset";
            } else {
                currentRoamingPreset = DownloadController.getInstance(this.currentAccount).getCurrentRoamingPreset();
                str = "roamingPreset";
                str2 = "currentRoamingPreset";
            }
            DownloadController.Preset preset4 = currentRoamingPreset;
            String str4 = str;
            String str5 = str2;
            NotificationsCheckCell notificationsCheckCell = (NotificationsCheckCell) view;
            boolean isChecked2 = notificationsCheckCell.isChecked();
            if ((LocaleController.isRTL && f <= AndroidUtilities.dp(76.0f)) || (!LocaleController.isRTL && f >= view.getMeasuredWidth() - AndroidUtilities.dp(76.0f))) {
                int i8 = this.currentPresetNum;
                if (i8 != 3) {
                    if (i8 == 0) {
                        this.typePreset.set(this.lowPreset);
                    } else if (i8 == 1) {
                        this.typePreset.set(this.mediumPreset);
                    } else if (i8 == 2) {
                        this.typePreset.set(this.highPreset);
                    }
                }
                int i9 = 0;
                while (true) {
                    if (i9 >= this.typePreset.mask.length) {
                        z2 = false;
                        break;
                    } else if ((preset4.mask[i9] & i2) != 0) {
                        z2 = true;
                        break;
                    } else {
                        i9++;
                    }
                }
                while (true) {
                    int[] iArr = this.typePreset.mask;
                    if (i5 >= iArr.length) {
                        break;
                    }
                    if (isChecked2) {
                        iArr[i5] = iArr[i5] & (i2 ^ (-1));
                    } else if (!z2) {
                        iArr[i5] = iArr[i5] | i2;
                    }
                    i5++;
                }
                SharedPreferences.Editor edit3 = MessagesController.getMainSettings(this.currentAccount).edit();
                edit3.putString(str4, this.typePreset.toString());
                this.currentPresetNum = 3;
                edit3.putInt(str5, 3);
                int i10 = this.currentType;
                if (i10 == 0) {
                    DownloadController.getInstance(this.currentAccount).currentMobilePreset = this.currentPresetNum;
                } else if (i10 == 1) {
                    DownloadController.getInstance(this.currentAccount).currentWifiPreset = this.currentPresetNum;
                } else {
                    DownloadController.getInstance(this.currentAccount).currentRoamingPreset = this.currentPresetNum;
                }
                edit3.commit();
                notificationsCheckCell.setChecked(!isChecked2);
                RecyclerView.ViewHolder findContainingViewHolder = this.listView.findContainingViewHolder(view);
                if (findContainingViewHolder != null) {
                    this.listAdapter.onBindViewHolder(findContainingViewHolder, i);
                }
                DownloadController.getInstance(this.currentAccount).checkAutodownloadSettings();
                this.wereAnyChanges = true;
                fillPresets();
            } else if (getParentActivity() == null) {
            } else {
                BottomSheet.Builder builder = new BottomSheet.Builder(getParentActivity());
                builder.setApplyTopPadding(false);
                builder.setApplyBottomPadding(false);
                LinearLayout linearLayout = new LinearLayout(getParentActivity());
                linearLayout.setOrientation(1);
                builder.setCustomView(linearLayout);
                HeaderCell headerCell = new HeaderCell(getParentActivity(), Theme.key_dialogTextBlue2, 21, 15, false);
                if (i == this.photosRow) {
                    headerCell.setText(LocaleController.getString("AutoDownloadPhotosTitle", R.string.AutoDownloadPhotosTitle));
                } else if (i == this.videosRow) {
                    headerCell.setText(LocaleController.getString("AutoDownloadVideosTitle", R.string.AutoDownloadVideosTitle));
                } else {
                    headerCell.setText(LocaleController.getString("AutoDownloadFilesTitle", R.string.AutoDownloadFilesTitle));
                }
                linearLayout.addView(headerCell, LayoutHelper.createFrame(-1, -2.0f));
                MaxFileSizeCell[] maxFileSizeCellArr2 = new MaxFileSizeCell[1];
                TextCheckCell[] textCheckCellArr2 = new TextCheckCell[1];
                final AnimatorSet[] animatorSetArr = new AnimatorSet[1];
                final TextCheckBoxCell[] textCheckBoxCellArr = new TextCheckBoxCell[4];
                int i11 = 0;
                while (i11 < i4) {
                    final TextCheckCell[] textCheckCellArr3 = textCheckCellArr2;
                    final TextCheckBoxCell textCheckBoxCell = new TextCheckBoxCell(getParentActivity(), true, z3);
                    textCheckBoxCellArr[i11] = textCheckBoxCell;
                    if (i11 == 0) {
                        maxFileSizeCellArr = maxFileSizeCellArr2;
                        textCheckBoxCellArr[i11].setTextAndCheck(LocaleController.getString("AutodownloadContacts", R.string.AutodownloadContacts), (preset4.mask[0] & i2) != 0, true);
                    } else {
                        maxFileSizeCellArr = maxFileSizeCellArr2;
                        if (i11 == 1) {
                            textCheckBoxCellArr[i11].setTextAndCheck(LocaleController.getString("AutodownloadPrivateChats", R.string.AutodownloadPrivateChats), (preset4.mask[1] & i2) != 0, true);
                        } else if (i11 == 2) {
                            textCheckBoxCellArr[i11].setTextAndCheck(LocaleController.getString("AutodownloadGroupChats", R.string.AutodownloadGroupChats), (preset4.mask[2] & i2) != 0, true);
                        } else {
                            textCheckBoxCellArr[i11].setTextAndCheck(LocaleController.getString("AutodownloadChannels", R.string.AutodownloadChannels), (preset4.mask[3] & i2) != 0, i != this.photosRow);
                        }
                    }
                    textCheckBoxCellArr[i11].setBackgroundDrawable(Theme.getSelectorDrawable(false));
                    final MaxFileSizeCell[] maxFileSizeCellArr3 = maxFileSizeCellArr;
                    LinearLayout linearLayout2 = linearLayout;
                    textCheckBoxCellArr[i11].setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.DataAutoDownloadActivity$$ExternalSyntheticLambda1
                        @Override // android.view.View.OnClickListener
                        public final void onClick(View view2) {
                            DataAutoDownloadActivity.this.lambda$createView$0(textCheckBoxCell, textCheckBoxCellArr, i, maxFileSizeCellArr3, textCheckCellArr3, animatorSetArr, view2);
                        }
                    });
                    linearLayout2.addView(textCheckBoxCellArr[i11], LayoutHelper.createFrame(-1, 50.0f));
                    i11++;
                    linearLayout = linearLayout2;
                    maxFileSizeCellArr2 = maxFileSizeCellArr3;
                    textCheckCellArr2 = textCheckCellArr3;
                    builder = builder;
                    str5 = str5;
                    str4 = str4;
                    preset4 = preset4;
                    i4 = 4;
                    z3 = false;
                }
                final TextCheckCell[] textCheckCellArr4 = textCheckCellArr2;
                final MaxFileSizeCell[] maxFileSizeCellArr4 = maxFileSizeCellArr2;
                final BottomSheet.Builder builder2 = builder;
                final String str6 = str5;
                final String str7 = str4;
                DownloadController.Preset preset5 = preset4;
                LinearLayout linearLayout3 = linearLayout;
                if (i != this.photosRow) {
                    final TextInfoPrivacyCell textInfoPrivacyCell = new TextInfoPrivacyCell(getParentActivity());
                    maxFileSizeCellArr4[0] = new MaxFileSizeCell(getParentActivity()) { // from class: org.telegram.ui.DataAutoDownloadActivity.3
                        @Override // org.telegram.ui.Cells.MaxFileSizeCell
                        protected void didChangedSizeValue(int i12) {
                            if (i == DataAutoDownloadActivity.this.videosRow) {
                                textInfoPrivacyCell.setText(LocaleController.formatString("AutoDownloadPreloadVideoInfo", R.string.AutoDownloadPreloadVideoInfo, AndroidUtilities.formatFileSize(i12)));
                                boolean z4 = i12 > 2097152;
                                if (z4 != textCheckCellArr4[0].isEnabled()) {
                                    ArrayList<Animator> arrayList = new ArrayList<>();
                                    textCheckCellArr4[0].setEnabled(z4, arrayList);
                                    AnimatorSet[] animatorSetArr2 = animatorSetArr;
                                    if (animatorSetArr2[0] != null) {
                                        animatorSetArr2[0].cancel();
                                        animatorSetArr[0] = null;
                                    }
                                    animatorSetArr[0] = new AnimatorSet();
                                    animatorSetArr[0].playTogether(arrayList);
                                    animatorSetArr[0].addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.DataAutoDownloadActivity.3.1
                                        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                                        public void onAnimationEnd(Animator animator) {
                                            if (animator.equals(animatorSetArr[0])) {
                                                animatorSetArr[0] = null;
                                            }
                                        }
                                    });
                                    animatorSetArr[0].setDuration(150L);
                                    animatorSetArr[0].start();
                                }
                            }
                        }
                    };
                    preset = preset5;
                    maxFileSizeCellArr4[0].setSize(preset.sizes[typeToIndex]);
                    linearLayout3.addView(maxFileSizeCellArr4[0], LayoutHelper.createLinear(-1, 50));
                    textCheckCellArr = textCheckCellArr4;
                    textCheckCellArr[0] = new TextCheckCell(getParentActivity(), 21, true);
                    linearLayout3.addView(textCheckCellArr[0], LayoutHelper.createLinear(-1, 48));
                    textCheckCellArr[0].setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.DataAutoDownloadActivity$$ExternalSyntheticLambda3
                        @Override // android.view.View.OnClickListener
                        public final void onClick(View view2) {
                            DataAutoDownloadActivity.lambda$createView$1(textCheckCellArr, view2);
                        }
                    });
                    CombinedDrawable combinedDrawable = new CombinedDrawable(new ColorDrawable(Theme.getColor(Theme.key_windowBackgroundGray)), Theme.getThemedDrawableByKey(getParentActivity(), R.drawable.greydivider, Theme.key_windowBackgroundGrayShadow));
                    combinedDrawable.setFullsize(true);
                    textInfoPrivacyCell.setBackgroundDrawable(combinedDrawable);
                    linearLayout3.addView(textInfoPrivacyCell, LayoutHelper.createLinear(-1, -2));
                    if (i == this.videosRow) {
                        maxFileSizeCellArr4[0].setText(LocaleController.getString("AutoDownloadMaxVideoSize", R.string.AutoDownloadMaxVideoSize));
                        textCheckCellArr[0].setTextAndCheck(LocaleController.getString("AutoDownloadPreloadVideo", R.string.AutoDownloadPreloadVideo), preset.preloadVideo, false);
                        textInfoPrivacyCell.setText(LocaleController.formatString("AutoDownloadPreloadVideoInfo", R.string.AutoDownloadPreloadVideoInfo, AndroidUtilities.formatFileSize(preset.sizes[typeToIndex])));
                    } else {
                        maxFileSizeCellArr4[0].setText(LocaleController.getString("AutoDownloadMaxFileSize", R.string.AutoDownloadMaxFileSize));
                        textCheckCellArr[0].setTextAndCheck(LocaleController.getString("AutoDownloadPreloadMusic", R.string.AutoDownloadPreloadMusic), preset.preloadMusic, false);
                        textInfoPrivacyCell.setText(LocaleController.getString("AutoDownloadPreloadMusicInfo", R.string.AutoDownloadPreloadMusicInfo));
                    }
                } else {
                    textCheckCellArr = textCheckCellArr4;
                    preset = preset5;
                    maxFileSizeCellArr4[0] = null;
                    textCheckCellArr[0] = null;
                    View view2 = new View(getParentActivity());
                    view2.setBackgroundColor(Theme.getColor(Theme.key_divider));
                    linearLayout3.addView(view2, new LinearLayout.LayoutParams(-1, 1));
                }
                if (i == this.videosRow) {
                    int i12 = 0;
                    while (true) {
                        if (i12 >= 4) {
                            z = false;
                            break;
                        } else if (textCheckBoxCellArr[i12].isChecked()) {
                            z = true;
                            break;
                        } else {
                            i12++;
                        }
                    }
                    if (z) {
                        r0 = 0;
                    } else {
                        r0 = 0;
                        maxFileSizeCellArr4[0].setEnabled(false, null);
                        textCheckCellArr[0].setEnabled(false, null);
                    }
                    if (preset.sizes[typeToIndex] <= 2097152) {
                        textCheckCellArr[r0].setEnabled(r0, null);
                    }
                }
                FrameLayout frameLayout = new FrameLayout(getParentActivity());
                frameLayout.setPadding(AndroidUtilities.dp(8.0f), AndroidUtilities.dp(8.0f), AndroidUtilities.dp(8.0f), AndroidUtilities.dp(8.0f));
                linearLayout3.addView(frameLayout, LayoutHelper.createLinear(-1, 52));
                TextView textView = new TextView(getParentActivity());
                textView.setTextSize(1, 14.0f);
                int i13 = Theme.key_dialogTextBlue2;
                textView.setTextColor(Theme.getColor(i13));
                textView.setGravity(17);
                textView.setTypeface(AndroidUtilities.getTypeface(AndroidUtilities.TYPEFACE_ROBOTO_MEDIUM));
                textView.setText(LocaleController.getString("Cancel", R.string.Cancel).toUpperCase());
                textView.setPadding(AndroidUtilities.dp(10.0f), 0, AndroidUtilities.dp(10.0f), 0);
                frameLayout.addView(textView, LayoutHelper.createFrame(-2, 36, 51));
                textView.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.DataAutoDownloadActivity$$ExternalSyntheticLambda0
                    @Override // android.view.View.OnClickListener
                    public final void onClick(View view3) {
                        DataAutoDownloadActivity.lambda$createView$2(BottomSheet.Builder.this, view3);
                    }
                });
                TextView textView2 = new TextView(getParentActivity());
                textView2.setTextSize(1, 14.0f);
                textView2.setTextColor(Theme.getColor(i13));
                textView2.setGravity(17);
                textView2.setTypeface(AndroidUtilities.getTypeface(AndroidUtilities.TYPEFACE_ROBOTO_MEDIUM));
                textView2.setText(LocaleController.getString("Save", R.string.Save).toUpperCase());
                textView2.setPadding(AndroidUtilities.dp(10.0f), 0, AndroidUtilities.dp(10.0f), 0);
                frameLayout.addView(textView2, LayoutHelper.createFrame(-2, 36, 53));
                final int i14 = i2;
                textView2.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.DataAutoDownloadActivity$$ExternalSyntheticLambda2
                    @Override // android.view.View.OnClickListener
                    public final void onClick(View view3) {
                        DataAutoDownloadActivity.this.lambda$createView$3(textCheckBoxCellArr, i14, maxFileSizeCellArr4, typeToIndex, textCheckCellArr, i, str7, str6, builder2, view, view3);
                    }
                });
                showDialog(builder2.create());
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createView$0(TextCheckBoxCell textCheckBoxCell, TextCheckBoxCell[] textCheckBoxCellArr, int i, MaxFileSizeCell[] maxFileSizeCellArr, TextCheckCell[] textCheckCellArr, final AnimatorSet[] animatorSetArr, View view) {
        if (view.isEnabled()) {
            boolean z = true;
            textCheckBoxCell.setChecked(!textCheckBoxCell.isChecked());
            int i2 = 0;
            while (true) {
                if (i2 >= textCheckBoxCellArr.length) {
                    z = false;
                    break;
                } else if (textCheckBoxCellArr[i2].isChecked()) {
                    break;
                } else {
                    i2++;
                }
            }
            if (i != this.videosRow || maxFileSizeCellArr[0].isEnabled() == z) {
                return;
            }
            ArrayList<Animator> arrayList = new ArrayList<>();
            maxFileSizeCellArr[0].setEnabled(z, arrayList);
            if (maxFileSizeCellArr[0].getSize() > 2097152) {
                textCheckCellArr[0].setEnabled(z, arrayList);
            }
            if (animatorSetArr[0] != null) {
                animatorSetArr[0].cancel();
                animatorSetArr[0] = null;
            }
            animatorSetArr[0] = new AnimatorSet();
            animatorSetArr[0].playTogether(arrayList);
            animatorSetArr[0].addListener(new AnimatorListenerAdapter(this) { // from class: org.telegram.ui.DataAutoDownloadActivity.2
                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public void onAnimationEnd(Animator animator) {
                    if (animator.equals(animatorSetArr[0])) {
                        animatorSetArr[0] = null;
                    }
                }
            });
            animatorSetArr[0].setDuration(150L);
            animatorSetArr[0].start();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$createView$1(TextCheckCell[] textCheckCellArr, View view) {
        textCheckCellArr[0].setChecked(!textCheckCellArr[0].isChecked());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$createView$2(BottomSheet.Builder builder, View view) {
        builder.getDismissRunnable().run();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createView$3(TextCheckBoxCell[] textCheckBoxCellArr, int i, MaxFileSizeCell[] maxFileSizeCellArr, int i2, TextCheckCell[] textCheckCellArr, int i3, String str, String str2, BottomSheet.Builder builder, View view, View view2) {
        int i4 = this.currentPresetNum;
        if (i4 != 3) {
            if (i4 == 0) {
                this.typePreset.set(this.lowPreset);
            } else if (i4 == 1) {
                this.typePreset.set(this.mediumPreset);
            } else if (i4 == 2) {
                this.typePreset.set(this.highPreset);
            }
        }
        for (int i5 = 0; i5 < 4; i5++) {
            if (textCheckBoxCellArr[i5].isChecked()) {
                int[] iArr = this.typePreset.mask;
                iArr[i5] = iArr[i5] | i;
            } else {
                int[] iArr2 = this.typePreset.mask;
                iArr2[i5] = iArr2[i5] & (i ^ (-1));
            }
        }
        if (maxFileSizeCellArr[0] != null) {
            maxFileSizeCellArr[0].getSize();
            this.typePreset.sizes[i2] = (int) maxFileSizeCellArr[0].getSize();
        }
        if (textCheckCellArr[0] != null) {
            if (i3 == this.videosRow) {
                this.typePreset.preloadVideo = textCheckCellArr[0].isChecked();
            } else {
                this.typePreset.preloadMusic = textCheckCellArr[0].isChecked();
            }
        }
        SharedPreferences.Editor edit = MessagesController.getMainSettings(this.currentAccount).edit();
        edit.putString(str, this.typePreset.toString());
        this.currentPresetNum = 3;
        edit.putInt(str2, 3);
        int i6 = this.currentType;
        if (i6 == 0) {
            DownloadController.getInstance(this.currentAccount).currentMobilePreset = this.currentPresetNum;
        } else if (i6 == 1) {
            DownloadController.getInstance(this.currentAccount).currentWifiPreset = this.currentPresetNum;
        } else {
            DownloadController.getInstance(this.currentAccount).currentRoamingPreset = this.currentPresetNum;
        }
        edit.commit();
        builder.getDismissRunnable().run();
        RecyclerView.ViewHolder findContainingViewHolder = this.listView.findContainingViewHolder(view);
        if (findContainingViewHolder != null) {
            this.animateChecked = true;
            this.listAdapter.onBindViewHolder(findContainingViewHolder, i3);
            this.animateChecked = false;
        }
        DownloadController.getInstance(this.currentAccount).checkAutodownloadSettings();
        this.wereAnyChanges = true;
        fillPresets();
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public void onResume() {
        super.onResume();
        ListAdapter listAdapter = this.listAdapter;
        if (listAdapter != null) {
            listAdapter.notifyDataSetChanged();
        }
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public void onPause() {
        super.onPause();
        if (this.wereAnyChanges) {
            DownloadController.getInstance(this.currentAccount).savePresetToServer(this.currentType);
            this.wereAnyChanges = false;
        }
    }

    private void fillPresets() {
        this.presets.clear();
        this.presets.add(this.lowPreset);
        this.presets.add(this.mediumPreset);
        this.presets.add(this.highPreset);
        if (!this.typePreset.equals(this.lowPreset) && !this.typePreset.equals(this.mediumPreset) && !this.typePreset.equals(this.highPreset)) {
            this.presets.add(this.typePreset);
        }
        Collections.sort(this.presets, DataAutoDownloadActivity$$ExternalSyntheticLambda4.INSTANCE);
        int i = this.currentPresetNum;
        if (i == 0 || (i == 3 && this.typePreset.equals(this.lowPreset))) {
            this.selectedPreset = this.presets.indexOf(this.lowPreset);
        } else {
            int i2 = this.currentPresetNum;
            if (i2 == 1 || (i2 == 3 && this.typePreset.equals(this.mediumPreset))) {
                this.selectedPreset = this.presets.indexOf(this.mediumPreset);
            } else {
                int i3 = this.currentPresetNum;
                if (i3 == 2 || (i3 == 3 && this.typePreset.equals(this.highPreset))) {
                    this.selectedPreset = this.presets.indexOf(this.highPreset);
                } else {
                    this.selectedPreset = this.presets.indexOf(this.typePreset);
                }
            }
        }
        RecyclerListView recyclerListView = this.listView;
        if (recyclerListView != null) {
            RecyclerView.ViewHolder findViewHolderForAdapterPosition = recyclerListView.findViewHolderForAdapterPosition(this.usageProgressRow);
            if (findViewHolderForAdapterPosition != null) {
                View view = findViewHolderForAdapterPosition.itemView;
                if (view instanceof SlideChooseView) {
                    updatePresetChoseView((SlideChooseView) view);
                    return;
                }
            }
            this.listAdapter.notifyItemChanged(this.usageProgressRow);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ int lambda$fillPresets$5(DownloadController.Preset preset, DownloadController.Preset preset2) {
        int typeToIndex = DownloadController.typeToIndex(4);
        int typeToIndex2 = DownloadController.typeToIndex(8);
        int i = 0;
        boolean z = false;
        boolean z2 = false;
        while (true) {
            int[] iArr = preset.mask;
            if (i < iArr.length) {
                if ((iArr[i] & 4) != 0) {
                    z = true;
                }
                if ((iArr[i] & 8) != 0) {
                    z2 = true;
                }
                if (z && z2) {
                    break;
                }
                i++;
            } else {
                break;
            }
        }
        int i2 = 0;
        boolean z3 = false;
        boolean z4 = false;
        while (true) {
            int[] iArr2 = preset2.mask;
            if (i2 < iArr2.length) {
                if ((iArr2[i2] & 4) != 0) {
                    z3 = true;
                }
                if ((iArr2[i2] & 8) != 0) {
                    z4 = true;
                }
                if (z3 && z4) {
                    break;
                }
                i2++;
            } else {
                break;
            }
        }
        long j = (z ? preset.sizes[typeToIndex] : 0L) + (z2 ? preset.sizes[typeToIndex2] : 0L);
        long j2 = (z3 ? preset2.sizes[typeToIndex] : 0L) + (z4 ? preset2.sizes[typeToIndex2] : 0L);
        if (j > j2) {
            return 1;
        }
        return j < j2 ? -1 : 0;
    }

    private void updateRows() {
        this.rowCount = 0;
        int i = 0 + 1;
        this.rowCount = i;
        this.autoDownloadRow = 0;
        int i2 = i + 1;
        this.rowCount = i2;
        this.autoDownloadSectionRow = i;
        if (this.typePreset.enabled) {
            int i3 = i2 + 1;
            this.rowCount = i3;
            this.usageHeaderRow = i2;
            int i4 = i3 + 1;
            this.rowCount = i4;
            this.usageProgressRow = i3;
            int i5 = i4 + 1;
            this.rowCount = i5;
            this.usageSectionRow = i4;
            int i6 = i5 + 1;
            this.rowCount = i6;
            this.typeHeaderRow = i5;
            int i7 = i6 + 1;
            this.rowCount = i7;
            this.photosRow = i6;
            int i8 = i7 + 1;
            this.rowCount = i8;
            this.videosRow = i7;
            int i9 = i8 + 1;
            this.rowCount = i9;
            this.filesRow = i8;
            this.rowCount = i9 + 1;
            this.typeSectionRow = i9;
            return;
        }
        this.usageHeaderRow = -1;
        this.usageProgressRow = -1;
        this.usageSectionRow = -1;
        this.typeHeaderRow = -1;
        this.photosRow = -1;
        this.videosRow = -1;
        this.filesRow = -1;
        this.typeSectionRow = -1;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes3.dex */
    public class ListAdapter extends RecyclerListView.SelectionAdapter {
        private Context mContext;

        public ListAdapter(Context context) {
            this.mContext = context;
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public int getItemCount() {
            return DataAutoDownloadActivity.this.rowCount;
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
            int i2;
            String string;
            StringBuilder sb;
            int itemViewType = viewHolder.getItemViewType();
            if (itemViewType == 0) {
                TextCheckCell textCheckCell = (TextCheckCell) viewHolder.itemView;
                if (i == DataAutoDownloadActivity.this.autoDownloadRow) {
                    textCheckCell.setDrawCheckRipple(true);
                    textCheckCell.setTextAndCheck(LocaleController.getString("AutoDownloadMedia", R.string.AutoDownloadMedia), DataAutoDownloadActivity.this.typePreset.enabled, false);
                    textCheckCell.setTag(Integer.valueOf(DataAutoDownloadActivity.this.typePreset.enabled ? Theme.key_windowBackgroundChecked : Theme.key_windowBackgroundUnchecked));
                    textCheckCell.setBackgroundColor(Theme.getColor(DataAutoDownloadActivity.this.typePreset.enabled ? Theme.key_windowBackgroundChecked : Theme.key_windowBackgroundUnchecked));
                }
            } else if (itemViewType == 2) {
                HeaderCell headerCell = (HeaderCell) viewHolder.itemView;
                if (i != DataAutoDownloadActivity.this.usageHeaderRow) {
                    if (i == DataAutoDownloadActivity.this.typeHeaderRow) {
                        headerCell.setText(LocaleController.getString("AutoDownloadTypes", R.string.AutoDownloadTypes));
                        return;
                    }
                    return;
                }
                headerCell.setText(LocaleController.getString("AutoDownloadDataUsage", R.string.AutoDownloadDataUsage));
            } else if (itemViewType == 3) {
                DataAutoDownloadActivity.this.updatePresetChoseView((SlideChooseView) viewHolder.itemView);
            } else if (itemViewType != 4) {
                if (itemViewType != 5) {
                    return;
                }
                TextInfoPrivacyCell textInfoPrivacyCell = (TextInfoPrivacyCell) viewHolder.itemView;
                if (i != DataAutoDownloadActivity.this.typeSectionRow) {
                    if (i == DataAutoDownloadActivity.this.autoDownloadSectionRow) {
                        if (DataAutoDownloadActivity.this.usageHeaderRow == -1) {
                            textInfoPrivacyCell.setBackgroundDrawable(Theme.getThemedDrawableByKey(this.mContext, R.drawable.greydivider_bottom, Theme.key_windowBackgroundGrayShadow));
                            if (DataAutoDownloadActivity.this.currentType != 0) {
                                if (DataAutoDownloadActivity.this.currentType != 1) {
                                    if (DataAutoDownloadActivity.this.currentType == 2) {
                                        textInfoPrivacyCell.setText(LocaleController.getString("AutoDownloadOnRoamingDataInfo", R.string.AutoDownloadOnRoamingDataInfo));
                                    }
                                } else {
                                    textInfoPrivacyCell.setText(LocaleController.getString("AutoDownloadOnWiFiDataInfo", R.string.AutoDownloadOnWiFiDataInfo));
                                }
                            } else {
                                textInfoPrivacyCell.setText(LocaleController.getString("AutoDownloadOnMobileDataInfo", R.string.AutoDownloadOnMobileDataInfo));
                            }
                            textInfoPrivacyCell.setImportantForAccessibility(1);
                            return;
                        }
                        textInfoPrivacyCell.setBackgroundDrawable(Theme.getThemedDrawableByKey(this.mContext, R.drawable.greydivider, Theme.key_windowBackgroundGrayShadow));
                        textInfoPrivacyCell.setText(null);
                        textInfoPrivacyCell.setFixedSize(12);
                        if (Build.VERSION.SDK_INT >= 19) {
                            textInfoPrivacyCell.setImportantForAccessibility(4);
                            return;
                        } else {
                            textInfoPrivacyCell.setImportantForAccessibility(2);
                            return;
                        }
                    }
                    return;
                }
                textInfoPrivacyCell.setText(LocaleController.getString("AutoDownloadAudioInfo", R.string.AutoDownloadAudioInfo));
                textInfoPrivacyCell.setBackgroundDrawable(Theme.getThemedDrawableByKey(this.mContext, R.drawable.greydivider, Theme.key_windowBackgroundGrayShadow));
                textInfoPrivacyCell.setFixedSize(0);
                textInfoPrivacyCell.setImportantForAccessibility(1);
            } else {
                NotificationsCheckCell notificationsCheckCell = (NotificationsCheckCell) viewHolder.itemView;
                if (i != DataAutoDownloadActivity.this.photosRow) {
                    if (i == DataAutoDownloadActivity.this.videosRow) {
                        string = LocaleController.getString("AutoDownloadVideos", R.string.AutoDownloadVideos);
                        i2 = 4;
                    } else {
                        i2 = 8;
                        string = LocaleController.getString("AutoDownloadFiles", R.string.AutoDownloadFiles);
                    }
                } else {
                    string = LocaleController.getString("AutoDownloadPhotos", R.string.AutoDownloadPhotos);
                    i2 = 1;
                }
                DownloadController.Preset currentMobilePreset = DataAutoDownloadActivity.this.currentType == 0 ? DownloadController.getInstance(((BaseFragment) DataAutoDownloadActivity.this).currentAccount).getCurrentMobilePreset() : DataAutoDownloadActivity.this.currentType == 1 ? DownloadController.getInstance(((BaseFragment) DataAutoDownloadActivity.this).currentAccount).getCurrentWiFiPreset() : DownloadController.getInstance(((BaseFragment) DataAutoDownloadActivity.this).currentAccount).getCurrentRoamingPreset();
                long j = currentMobilePreset.sizes[DownloadController.typeToIndex(i2)];
                StringBuilder sb2 = new StringBuilder();
                int i3 = 0;
                int i4 = 0;
                while (true) {
                    int[] iArr = currentMobilePreset.mask;
                    if (i3 >= iArr.length) {
                        break;
                    }
                    if ((iArr[i3] & i2) != 0) {
                        if (sb2.length() != 0) {
                            sb2.append(", ");
                        }
                        if (i3 == 0) {
                            sb2.append(LocaleController.getString("AutoDownloadContacts", R.string.AutoDownloadContacts));
                        } else if (i3 == 1) {
                            sb2.append(LocaleController.getString("AutoDownloadPm", R.string.AutoDownloadPm));
                        } else if (i3 == 2) {
                            sb2.append(LocaleController.getString("AutoDownloadGroups", R.string.AutoDownloadGroups));
                        } else if (i3 == 3) {
                            sb2.append(LocaleController.getString("AutoDownloadChannels", R.string.AutoDownloadChannels));
                        }
                        i4++;
                    }
                    i3++;
                }
                if (i4 == 4) {
                    sb2.setLength(0);
                    if (i == DataAutoDownloadActivity.this.photosRow) {
                        sb2.append(LocaleController.getString("AutoDownloadOnAllChats", R.string.AutoDownloadOnAllChats));
                    } else {
                        sb2.append(LocaleController.formatString("AutoDownloadUpToOnAllChats", R.string.AutoDownloadUpToOnAllChats, AndroidUtilities.formatFileSize(j)));
                    }
                } else if (i4 != 0) {
                    if (i == DataAutoDownloadActivity.this.photosRow) {
                        sb = new StringBuilder(LocaleController.formatString("AutoDownloadOnFor", R.string.AutoDownloadOnFor, sb2.toString()));
                    } else {
                        sb = new StringBuilder(LocaleController.formatString("AutoDownloadOnUpToFor", R.string.AutoDownloadOnUpToFor, AndroidUtilities.formatFileSize(j), sb2.toString()));
                    }
                    sb2 = sb;
                } else {
                    sb2.append(LocaleController.getString("AutoDownloadOff", R.string.AutoDownloadOff));
                }
                if (DataAutoDownloadActivity.this.animateChecked) {
                    notificationsCheckCell.setChecked(i4 != 0);
                }
                notificationsCheckCell.setTextAndValueAndCheck(string, sb2, i4 != 0, 0, true, i != DataAutoDownloadActivity.this.filesRow);
            }
        }

        @Override // org.telegram.ui.Components.RecyclerListView.SelectionAdapter
        public boolean isEnabled(RecyclerView.ViewHolder viewHolder) {
            int adapterPosition = viewHolder.getAdapterPosition();
            return adapterPosition == DataAutoDownloadActivity.this.photosRow || adapterPosition == DataAutoDownloadActivity.this.videosRow || adapterPosition == DataAutoDownloadActivity.this.filesRow;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onCreateViewHolder$0(int i) {
            DownloadController.Preset preset = (DownloadController.Preset) DataAutoDownloadActivity.this.presets.get(i);
            if (preset == DataAutoDownloadActivity.this.lowPreset) {
                DataAutoDownloadActivity.this.currentPresetNum = 0;
            } else if (preset == DataAutoDownloadActivity.this.mediumPreset) {
                DataAutoDownloadActivity.this.currentPresetNum = 1;
            } else if (preset == DataAutoDownloadActivity.this.highPreset) {
                DataAutoDownloadActivity.this.currentPresetNum = 2;
            } else {
                DataAutoDownloadActivity.this.currentPresetNum = 3;
            }
            if (DataAutoDownloadActivity.this.currentType == 0) {
                DownloadController.getInstance(((BaseFragment) DataAutoDownloadActivity.this).currentAccount).currentMobilePreset = DataAutoDownloadActivity.this.currentPresetNum;
            } else if (DataAutoDownloadActivity.this.currentType == 1) {
                DownloadController.getInstance(((BaseFragment) DataAutoDownloadActivity.this).currentAccount).currentWifiPreset = DataAutoDownloadActivity.this.currentPresetNum;
            } else {
                DownloadController.getInstance(((BaseFragment) DataAutoDownloadActivity.this).currentAccount).currentRoamingPreset = DataAutoDownloadActivity.this.currentPresetNum;
            }
            SharedPreferences.Editor edit = MessagesController.getMainSettings(((BaseFragment) DataAutoDownloadActivity.this).currentAccount).edit();
            edit.putInt(DataAutoDownloadActivity.this.key2, DataAutoDownloadActivity.this.currentPresetNum);
            edit.commit();
            DownloadController.getInstance(((BaseFragment) DataAutoDownloadActivity.this).currentAccount).checkAutodownloadSettings();
            for (int i2 = 0; i2 < 3; i2++) {
                RecyclerView.ViewHolder findViewHolderForAdapterPosition = DataAutoDownloadActivity.this.listView.findViewHolderForAdapterPosition(DataAutoDownloadActivity.this.photosRow + i2);
                if (findViewHolderForAdapterPosition != null) {
                    DataAutoDownloadActivity.this.listAdapter.onBindViewHolder(findViewHolderForAdapterPosition, DataAutoDownloadActivity.this.photosRow + i2);
                }
            }
            DataAutoDownloadActivity.this.wereAnyChanges = true;
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            SlideChooseView slideChooseView;
            if (i == 0) {
                TextCheckCell textCheckCell = new TextCheckCell(this.mContext);
                textCheckCell.setColors(Theme.key_windowBackgroundCheckText, Theme.key_switchTrackBlue, Theme.key_switchTrackBlueChecked, Theme.key_switchTrackBlueThumb, Theme.key_switchTrackBlueThumbChecked);
                textCheckCell.setTypeface(AndroidUtilities.getTypeface(AndroidUtilities.TYPEFACE_ROBOTO_MEDIUM));
                textCheckCell.setHeight(56);
                slideChooseView = textCheckCell;
            } else if (i == 1) {
                slideChooseView = new ShadowSectionCell(this.mContext);
            } else if (i == 2) {
                View headerCell = new HeaderCell(this.mContext);
                headerCell.setBackgroundColor(Theme.getColor(Theme.key_windowBackgroundWhite));
                slideChooseView = headerCell;
            } else if (i == 3) {
                SlideChooseView slideChooseView2 = new SlideChooseView(this.mContext);
                slideChooseView2.setCallback(new SlideChooseView.Callback() { // from class: org.telegram.ui.DataAutoDownloadActivity$ListAdapter$$ExternalSyntheticLambda0
                    @Override // org.telegram.ui.Components.SlideChooseView.Callback
                    public final void onOptionSelected(int i2) {
                        DataAutoDownloadActivity.ListAdapter.this.lambda$onCreateViewHolder$0(i2);
                    }

                    @Override // org.telegram.ui.Components.SlideChooseView.Callback
                    public /* synthetic */ void onTouchEnd() {
                        SlideChooseView.Callback.-CC.$default$onTouchEnd(this);
                    }
                });
                slideChooseView2.setBackgroundColor(Theme.getColor(Theme.key_windowBackgroundWhite));
                slideChooseView = slideChooseView2;
            } else if (i == 4) {
                View notificationsCheckCell = new NotificationsCheckCell(this.mContext);
                notificationsCheckCell.setBackgroundColor(Theme.getColor(Theme.key_windowBackgroundWhite));
                slideChooseView = notificationsCheckCell;
            } else {
                View textInfoPrivacyCell = new TextInfoPrivacyCell(this.mContext);
                textInfoPrivacyCell.setBackgroundDrawable(Theme.getThemedDrawableByKey(this.mContext, R.drawable.greydivider_bottom, Theme.key_windowBackgroundGrayShadow));
                slideChooseView = textInfoPrivacyCell;
            }
            slideChooseView.setLayoutParams(new RecyclerView.LayoutParams(-1, -2));
            return new RecyclerListView.Holder(slideChooseView);
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public int getItemViewType(int i) {
            if (i == DataAutoDownloadActivity.this.autoDownloadRow) {
                return 0;
            }
            if (i == DataAutoDownloadActivity.this.usageSectionRow) {
                return 1;
            }
            if (i == DataAutoDownloadActivity.this.usageHeaderRow || i == DataAutoDownloadActivity.this.typeHeaderRow) {
                return 2;
            }
            if (i == DataAutoDownloadActivity.this.usageProgressRow) {
                return 3;
            }
            return (i == DataAutoDownloadActivity.this.photosRow || i == DataAutoDownloadActivity.this.videosRow || i == DataAutoDownloadActivity.this.filesRow) ? 4 : 5;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updatePresetChoseView(SlideChooseView slideChooseView) {
        String[] strArr = new String[this.presets.size()];
        for (int i = 0; i < this.presets.size(); i++) {
            DownloadController.Preset preset = this.presets.get(i);
            if (preset == this.lowPreset) {
                strArr[i] = LocaleController.getString("AutoDownloadLow", R.string.AutoDownloadLow);
            } else if (preset == this.mediumPreset) {
                strArr[i] = LocaleController.getString("AutoDownloadMedium", R.string.AutoDownloadMedium);
            } else if (preset == this.highPreset) {
                strArr[i] = LocaleController.getString("AutoDownloadHigh", R.string.AutoDownloadHigh);
            } else {
                strArr[i] = LocaleController.getString("AutoDownloadCustom", R.string.AutoDownloadCustom);
            }
        }
        slideChooseView.setOptions(this.selectedPreset, strArr);
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public ArrayList<ThemeDescription> getThemeDescriptions() {
        ArrayList<ThemeDescription> arrayList = new ArrayList<>();
        arrayList.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_CELLBACKGROUNDCOLOR, new Class[]{HeaderCell.class, NotificationsCheckCell.class, SlideChooseView.class}, null, null, null, Theme.key_windowBackgroundWhite));
        arrayList.add(new ThemeDescription(this.fragmentView, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, Theme.key_windowBackgroundGray));
        ActionBar actionBar = this.actionBar;
        int i = ThemeDescription.FLAG_BACKGROUND;
        int i2 = Theme.key_actionBarDefault;
        arrayList.add(new ThemeDescription(actionBar, i, null, null, null, null, i2));
        arrayList.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_LISTGLOWCOLOR, null, null, null, null, i2));
        arrayList.add(new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_ITEMSCOLOR, null, null, null, null, Theme.key_actionBarDefaultIcon));
        arrayList.add(new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_TITLECOLOR, null, null, null, null, Theme.key_actionBarDefaultTitle));
        arrayList.add(new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_SELECTORCOLOR, null, null, null, null, Theme.key_actionBarDefaultSelector));
        arrayList.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_SELECTOR, null, null, null, null, Theme.key_listSelector));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{View.class}, Theme.dividerPaint, null, null, Theme.key_divider));
        int i3 = Theme.key_windowBackgroundGrayShadow;
        arrayList.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_BACKGROUNDFILTER, new Class[]{ShadowSectionCell.class}, null, null, null, i3));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{HeaderCell.class}, new String[]{"textView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, Theme.key_windowBackgroundWhiteBlueHeader));
        arrayList.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_CELLBACKGROUNDCOLOR | ThemeDescription.FLAG_CHECKTAG, new Class[]{TextCheckCell.class}, null, null, null, Theme.key_windowBackgroundChecked));
        arrayList.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_CELLBACKGROUNDCOLOR | ThemeDescription.FLAG_CHECKTAG, new Class[]{TextCheckCell.class}, null, null, null, Theme.key_windowBackgroundUnchecked));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{TextCheckCell.class}, new String[]{"textView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, Theme.key_windowBackgroundCheckText));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{TextCheckCell.class}, new String[]{"checkBox"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, Theme.key_switchTrackBlue));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{TextCheckCell.class}, new String[]{"checkBox"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, Theme.key_switchTrackBlueChecked));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{TextCheckCell.class}, new String[]{"checkBox"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, Theme.key_switchTrackBlueThumb));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{TextCheckCell.class}, new String[]{"checkBox"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, Theme.key_switchTrackBlueThumbChecked));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{TextCheckCell.class}, new String[]{"checkBox"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, Theme.key_switchTrackBlueSelector));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{TextCheckCell.class}, new String[]{"checkBox"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, Theme.key_switchTrackBlueSelectorChecked));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{NotificationsCheckCell.class}, new String[]{"textView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, Theme.key_windowBackgroundWhiteBlackText));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{NotificationsCheckCell.class}, new String[]{"valueTextView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, Theme.key_windowBackgroundWhiteGrayText2));
        int i4 = Theme.key_switchTrack;
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{NotificationsCheckCell.class}, new String[]{"checkBox"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, i4));
        int i5 = Theme.key_switchTrackChecked;
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{NotificationsCheckCell.class}, new String[]{"checkBox"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, i5));
        arrayList.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_BACKGROUNDFILTER, new Class[]{TextInfoPrivacyCell.class}, null, null, null, i3));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{TextInfoPrivacyCell.class}, new String[]{"textView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, Theme.key_windowBackgroundWhiteGrayText4));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{SlideChooseView.class}, null, null, null, i4));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{SlideChooseView.class}, null, null, null, i5));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{SlideChooseView.class}, null, null, null, Theme.key_windowBackgroundWhiteGrayText));
        return arrayList;
    }
}
