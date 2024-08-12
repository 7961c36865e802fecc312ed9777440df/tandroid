package org.telegram.ui.Components;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.widget.TextView;
import java.util.ArrayList;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.BuildVars;
import org.telegram.messenger.FileLoader;
import org.telegram.messenger.ImageLocation;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MediaDataController;
import org.telegram.messenger.MessageObject;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.R;
import org.telegram.messenger.UserConfig;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC$Document;
import org.telegram.tgnet.TLRPC$PhotoSize;
import org.telegram.tgnet.TLRPC$StickerSet;
import org.telegram.tgnet.TLRPC$StickerSetCovered;
import org.telegram.tgnet.TLRPC$TL_messages_stickerSet;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Components.Bulletin;
import org.telegram.ui.Components.Premium.LimitReachedBottomSheet;
import org.telegram.ui.LaunchActivity;
import org.telegram.ui.PremiumPreviewFragment;
@SuppressLint({"ViewConstructor"})
/* loaded from: classes3.dex */
public class StickerSetBulletinLayout extends Bulletin.TwoLineLayout {
    public StickerSetBulletinLayout(Context context, TLObject tLObject, int i, TLRPC$Document tLRPC$Document, Theme.ResourcesProvider resourcesProvider) {
        this(context, tLObject, 1, i, tLRPC$Document, resourcesProvider);
    }

    /* JADX WARN: Removed duplicated region for block: B:101:0x030e  */
    /* JADX WARN: Removed duplicated region for block: B:109:0x037e  */
    /* JADX WARN: Removed duplicated region for block: B:141:? A[RETURN, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:29:0x0075 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:35:0x008c  */
    /* JADX WARN: Removed duplicated region for block: B:65:0x0115  */
    /* JADX WARN: Removed duplicated region for block: B:68:0x0128  */
    /* JADX WARN: Removed duplicated region for block: B:72:0x013c  */
    /* JADX WARN: Removed duplicated region for block: B:81:0x01c8  */
    /* JADX WARN: Removed duplicated region for block: B:87:0x0251  */
    /* JADX WARN: Removed duplicated region for block: B:88:0x0265  */
    /* JADX WARN: Removed duplicated region for block: B:89:0x0279  */
    /* JADX WARN: Removed duplicated region for block: B:90:0x028d  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public StickerSetBulletinLayout(final Context context, TLObject tLObject, int i, int i2, TLRPC$Document tLRPC$Document, Theme.ResourcesProvider resourcesProvider) {
        super(context, resourcesProvider);
        TLRPC$Document tLRPC$Document2;
        TLRPC$StickerSet tLRPC$StickerSet;
        TLRPC$Document tLRPC$Document3;
        int i3;
        ImageLocation forSticker;
        TLRPC$TL_messages_stickerSet stickerSet;
        boolean z = tLObject instanceof TLRPC$TL_messages_stickerSet;
        if (z) {
            TLRPC$TL_messages_stickerSet tLRPC$TL_messages_stickerSet = (TLRPC$TL_messages_stickerSet) tLObject;
            tLRPC$StickerSet = tLRPC$TL_messages_stickerSet.set;
            ArrayList<TLRPC$Document> arrayList = tLRPC$TL_messages_stickerSet.documents;
            if (arrayList != null && !arrayList.isEmpty()) {
                tLRPC$Document3 = arrayList.get(0);
                tLRPC$Document2 = tLRPC$Document3;
                if (tLRPC$StickerSet == null && tLRPC$Document2 != null && (stickerSet = MediaDataController.getInstance(UserConfig.selectedAccount).getStickerSet(MessageObject.getInputStickerSet(tLRPC$Document2), true)) != null) {
                    tLRPC$StickerSet = stickerSet.set;
                }
                TLRPC$StickerSet tLRPC$StickerSet2 = tLRPC$StickerSet;
                if (tLRPC$Document2 == null) {
                    TLRPC$PhotoSize closestPhotoSizeWithSize = tLRPC$StickerSet2 != null ? FileLoader.getClosestPhotoSizeWithSize(tLRPC$StickerSet2.thumbs, 90) : null;
                    closestPhotoSizeWithSize = closestPhotoSizeWithSize == null ? tLRPC$Document2 : closestPhotoSizeWithSize;
                    boolean z2 = closestPhotoSizeWithSize instanceof TLRPC$Document;
                    if (z2) {
                        forSticker = ImageLocation.getForDocument(FileLoader.getClosestPhotoSizeWithSize(tLRPC$Document2.thumbs, 90), tLRPC$Document2);
                    } else {
                        TLRPC$PhotoSize tLRPC$PhotoSize = closestPhotoSizeWithSize;
                        if (tLObject instanceof TLRPC$StickerSetCovered) {
                            i3 = ((TLRPC$StickerSetCovered) tLObject).set.thumb_version;
                        } else {
                            i3 = z ? ((TLRPC$TL_messages_stickerSet) tLObject).set.thumb_version : 0;
                        }
                        forSticker = ImageLocation.getForSticker(tLRPC$PhotoSize, tLRPC$Document2, i3);
                    }
                    ImageLocation imageLocation = forSticker;
                    if (z2 && (MessageObject.isAnimatedStickerDocument(tLRPC$Document2, true) || MessageObject.isVideoSticker(tLRPC$Document2) || MessageObject.isGifDocument(tLRPC$Document2))) {
                        this.imageView.setImage(ImageLocation.getForDocument(tLRPC$Document2), "50_50", imageLocation, (String) null, 0, tLObject);
                    } else if (imageLocation != null && imageLocation.imageType == 1) {
                        this.imageView.setImage(imageLocation, "50_50", "tgs", (Drawable) null, tLObject);
                    } else {
                        this.imageView.setImage(imageLocation, "50_50", "webp", (Drawable) null, tLObject);
                    }
                } else {
                    this.imageView.setImage((ImageLocation) null, (String) null, "webp", (Drawable) null, tLObject);
                }
                if (MessageObject.isTextColorEmoji(tLRPC$Document2)) {
                    this.imageView.setColorFilter(new PorterDuffColorFilter(-1, PorterDuff.Mode.SRC_IN));
                }
                switch (i2) {
                    case 0:
                        if (tLRPC$StickerSet2 != null) {
                            if (tLRPC$StickerSet2.masks) {
                                this.titleTextView.setText(LocaleController.getString("MasksRemoved", R.string.MasksRemoved));
                                this.subtitleTextView.setText(LocaleController.formatString("MasksRemovedInfo", R.string.MasksRemovedInfo, tLRPC$StickerSet2.title));
                                return;
                            } else if (tLRPC$StickerSet2.emojis) {
                                this.titleTextView.setText(LocaleController.getString("EmojiRemoved", R.string.EmojiRemoved));
                                if (i > 1) {
                                    this.subtitleTextView.setText(LocaleController.formatPluralString("EmojiRemovedMultipleInfo", i, new Object[0]));
                                    return;
                                } else {
                                    this.subtitleTextView.setText(LocaleController.formatString("EmojiRemovedInfo", R.string.EmojiRemovedInfo, tLRPC$StickerSet2.title));
                                    return;
                                }
                            } else {
                                this.titleTextView.setText(LocaleController.getString("StickersRemoved", R.string.StickersRemoved));
                                this.subtitleTextView.setText(LocaleController.formatString("StickersRemovedInfo", R.string.StickersRemovedInfo, tLRPC$StickerSet2.title));
                                return;
                            }
                        }
                        return;
                    case 1:
                        if (tLRPC$StickerSet2 != null) {
                            if (tLRPC$StickerSet2.masks) {
                                this.titleTextView.setText(LocaleController.getString("MasksArchived", R.string.MasksArchived));
                                this.subtitleTextView.setText(LocaleController.formatString("MasksArchivedInfo", R.string.MasksArchivedInfo, tLRPC$StickerSet2.title));
                                return;
                            } else if (tLRPC$StickerSet2.emojis) {
                                this.titleTextView.setText(LocaleController.getString("EmojiArchived", R.string.EmojiArchived));
                                this.subtitleTextView.setText(LocaleController.formatString("EmojiArchivedInfo", R.string.EmojiArchivedInfo, tLRPC$StickerSet2.title));
                                return;
                            } else {
                                this.titleTextView.setText(LocaleController.getString("StickersArchived", R.string.StickersArchived));
                                this.subtitleTextView.setText(LocaleController.formatString("StickersArchivedInfo", R.string.StickersArchivedInfo, tLRPC$StickerSet2.title));
                                return;
                            }
                        }
                        return;
                    case 2:
                        if (tLRPC$StickerSet2 != null) {
                            if (tLRPC$StickerSet2.masks) {
                                this.titleTextView.setText(LocaleController.getString("AddMasksInstalled", R.string.AddMasksInstalled));
                                this.subtitleTextView.setText(LocaleController.formatString("AddMasksInstalledInfo", R.string.AddMasksInstalledInfo, tLRPC$StickerSet2.title));
                                return;
                            } else if (tLRPC$StickerSet2.emojis) {
                                this.titleTextView.setText(LocaleController.getString("AddEmojiInstalled", R.string.AddEmojiInstalled));
                                if (i > 1) {
                                    this.subtitleTextView.setText(LocaleController.formatPluralString("AddEmojiMultipleInstalledInfo", i, new Object[0]));
                                    return;
                                } else {
                                    this.subtitleTextView.setText(LocaleController.formatString("AddEmojiInstalledInfo", R.string.AddEmojiInstalledInfo, tLRPC$StickerSet2.title));
                                    return;
                                }
                            } else {
                                this.titleTextView.setText(LocaleController.getString("AddStickersInstalled", R.string.AddStickersInstalled));
                                this.subtitleTextView.setText(LocaleController.formatString("AddStickersInstalledInfo", R.string.AddStickersInstalledInfo, tLRPC$StickerSet2.title));
                                return;
                            }
                        }
                        return;
                    case 3:
                        this.titleTextView.setText(LocaleController.getString("RemovedFromRecent", R.string.RemovedFromRecent));
                        this.subtitleTextView.setVisibility(8);
                        return;
                    case 4:
                        this.titleTextView.setText(LocaleController.getString("RemovedFromFavorites", R.string.RemovedFromFavorites));
                        this.subtitleTextView.setVisibility(8);
                        return;
                    case 5:
                        this.titleTextView.setText(LocaleController.getString("AddedToFavorites", R.string.AddedToFavorites));
                        this.subtitleTextView.setVisibility(8);
                        return;
                    case 6:
                        if (!UserConfig.getInstance(UserConfig.selectedAccount).isPremium() && !MessagesController.getInstance(UserConfig.selectedAccount).premiumFeaturesBlocked()) {
                            this.titleTextView.setText(LocaleController.formatString("LimitReachedFavoriteStickers", R.string.LimitReachedFavoriteStickers, Integer.valueOf(MessagesController.getInstance(UserConfig.selectedAccount).stickersFavedLimitDefault)));
                            this.subtitleTextView.setText(AndroidUtilities.premiumText(LocaleController.formatString("LimitReachedFavoriteStickersSubtitle", R.string.LimitReachedFavoriteStickersSubtitle, Integer.valueOf(MessagesController.getInstance(UserConfig.selectedAccount).stickersFavedLimitPremium)), new Runnable() { // from class: org.telegram.ui.Components.StickerSetBulletinLayout$$ExternalSyntheticLambda0
                                @Override // java.lang.Runnable
                                public final void run() {
                                    StickerSetBulletinLayout.lambda$new$0(context);
                                }
                            }));
                            return;
                        }
                        this.titleTextView.setText(LocaleController.formatString("LimitReachedFavoriteStickers", R.string.LimitReachedFavoriteStickers, Integer.valueOf(MessagesController.getInstance(UserConfig.selectedAccount).stickersFavedLimitPremium)));
                        this.subtitleTextView.setText(LocaleController.formatString("LimitReachedFavoriteStickersSubtitlePremium", R.string.LimitReachedFavoriteStickersSubtitlePremium, new Object[0]));
                        return;
                    case 7:
                        boolean isPremium = UserConfig.getInstance(UserConfig.selectedAccount).isPremium();
                        if (!MessagesController.getInstance(UserConfig.selectedAccount).premiumFeaturesBlocked() && !isPremium) {
                            this.titleTextView.setText(LocaleController.formatString(R.string.LimitReachedFavoriteGifs, Integer.valueOf(MessagesController.getInstance(UserConfig.selectedAccount).savedGifsLimitDefault)));
                            this.subtitleTextView.setText(AndroidUtilities.premiumText(LocaleController.formatString(R.string.LimitReachedFavoriteGifsSubtitle, Integer.valueOf(MessagesController.getInstance(UserConfig.selectedAccount).savedGifsLimitPremium)), new Runnable() { // from class: org.telegram.ui.Components.StickerSetBulletinLayout$$ExternalSyntheticLambda1
                                @Override // java.lang.Runnable
                                public final void run() {
                                    StickerSetBulletinLayout.lambda$new$1(context);
                                }
                            }));
                            return;
                        }
                        TextView textView = this.titleTextView;
                        int i4 = R.string.LimitReachedFavoriteGifs;
                        Object[] objArr = new Object[1];
                        objArr[0] = Integer.valueOf(isPremium ? MessagesController.getInstance(UserConfig.selectedAccount).savedGifsLimitPremium : MessagesController.getInstance(UserConfig.selectedAccount).savedGifsLimitDefault);
                        textView.setText(LocaleController.formatString(i4, objArr));
                        this.subtitleTextView.setText(LocaleController.getString(R.string.LimitReachedFavoriteGifsSubtitlePremium));
                        return;
                    default:
                        return;
                }
            }
            tLRPC$Document3 = null;
            tLRPC$Document2 = tLRPC$Document3;
            if (tLRPC$StickerSet == null) {
                tLRPC$StickerSet = stickerSet.set;
            }
            TLRPC$StickerSet tLRPC$StickerSet22 = tLRPC$StickerSet;
            if (tLRPC$Document2 == null) {
            }
            if (MessageObject.isTextColorEmoji(tLRPC$Document2)) {
            }
            switch (i2) {
            }
        } else if (tLObject instanceof TLRPC$StickerSetCovered) {
            TLRPC$StickerSetCovered tLRPC$StickerSetCovered = (TLRPC$StickerSetCovered) tLObject;
            tLRPC$StickerSet = tLRPC$StickerSetCovered.set;
            TLRPC$Document tLRPC$Document4 = tLRPC$StickerSetCovered.cover;
            if (tLRPC$Document4 != null) {
                tLRPC$Document3 = tLRPC$Document4;
            } else {
                if (!tLRPC$StickerSetCovered.covers.isEmpty()) {
                    tLRPC$Document3 = tLRPC$StickerSetCovered.covers.get(0);
                }
                tLRPC$Document3 = null;
            }
            tLRPC$Document2 = tLRPC$Document3;
            if (tLRPC$StickerSet == null) {
            }
            TLRPC$StickerSet tLRPC$StickerSet222 = tLRPC$StickerSet;
            if (tLRPC$Document2 == null) {
            }
            if (MessageObject.isTextColorEmoji(tLRPC$Document2)) {
            }
            switch (i2) {
            }
        } else if (tLRPC$Document == null && tLObject != null && BuildVars.DEBUG_VERSION) {
            throw new IllegalArgumentException("Invalid type of the given setObject: " + tLObject.getClass());
        } else {
            tLRPC$Document2 = tLRPC$Document;
            tLRPC$StickerSet = null;
            if (tLRPC$StickerSet == null) {
            }
            TLRPC$StickerSet tLRPC$StickerSet2222 = tLRPC$StickerSet;
            if (tLRPC$Document2 == null) {
            }
            if (MessageObject.isTextColorEmoji(tLRPC$Document2)) {
            }
            switch (i2) {
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$new$0(Context context) {
        Activity findActivity = AndroidUtilities.findActivity(context);
        if (findActivity instanceof LaunchActivity) {
            ((LaunchActivity) findActivity).lambda$runLinkRequest$91(new PremiumPreviewFragment(LimitReachedBottomSheet.limitTypeToServerString(10)));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$new$1(Context context) {
        Activity findActivity = AndroidUtilities.findActivity(context);
        if (findActivity instanceof LaunchActivity) {
            ((LaunchActivity) findActivity).lambda$runLinkRequest$91(new PremiumPreviewFragment(LimitReachedBottomSheet.limitTypeToServerString(9)));
        }
    }
}
