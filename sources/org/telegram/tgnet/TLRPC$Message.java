package org.telegram.tgnet;

import android.text.TextUtils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import org.telegram.messenger.FileLoaderPriorityQueue;
import org.telegram.messenger.LiteMode;
import org.telegram.messenger.Utilities;
import org.telegram.tgnet.tl.TL_stories$StoryItem;
/* loaded from: classes3.dex */
public class TLRPC$Message extends TLObject {
    public TLRPC$MessageAction action;
    public int date;
    public int destroyTime;
    public long destroyTimeMillis;
    public long dialog_id;
    public int edit_date;
    public boolean edit_hide;
    public int expire_date;
    public int flags;
    public int flags2;
    public int forwards;
    public int from_boosts_applied;
    public TLRPC$Peer from_id;
    public boolean from_scheduled;
    public TLRPC$MessageFwdHeader fwd_from;
    public long grouped_id;
    public int id;
    public boolean invert_media;
    public boolean isThreadMessage;
    public int layer;
    public boolean legacy;
    public TLRPC$MessageMedia media;
    public boolean media_unread;
    public boolean mentioned;
    public String message;
    public boolean noforwards;
    public boolean offline;
    public String originalLanguage;
    public boolean out;
    public HashMap<String, String> params;
    public TLRPC$Peer peer_id;
    public boolean pinned;
    public boolean post;
    public String post_author;
    public boolean premiumEffectWasPlayed;
    public TLRPC$InputQuickReplyShortcut quick_reply_shortcut;
    public int quick_reply_shortcut_id;
    public long random_id;
    public TLRPC$TL_messageReactions reactions;
    public int realId;
    public TLRPC$MessageReplies replies;
    public TLRPC$Message replyMessage;
    public TL_stories$StoryItem replyStory;
    public TLRPC$ReplyMarkup reply_markup;
    public TLRPC$MessageReplyHeader reply_to;
    public int reqId;
    public TLRPC$Peer saved_peer_id;
    public int seq_in;
    public int seq_out;
    public boolean silent;
    public TLRPC$TL_textWithEntities translatedText;
    public String translatedToLanguage;
    public int ttl;
    public int ttl_period;
    public boolean unread;
    public long via_bot_id;
    public String via_bot_name;
    public long via_business_bot_id;
    public int views;
    public String voiceTranscription;
    public boolean voiceTranscriptionFinal;
    public boolean voiceTranscriptionForce;
    public long voiceTranscriptionId;
    public boolean voiceTranscriptionOpen;
    public boolean voiceTranscriptionRated;
    public boolean with_my_score;
    public ArrayList<TLRPC$MessageEntity> entities = new ArrayList<>();
    public ArrayList<TLRPC$TL_restrictionReason> restriction_reason = new ArrayList<>();
    public int send_state = 0;
    public int fwd_msg_id = 0;
    public String attachPath = "";
    public int local_id = 0;
    public int stickerVerified = 1;

    public static TLRPC$Message TLdeserialize(AbstractSerializedData abstractSerializedData, int i, boolean z) {
        TLRPC$Message tLRPC$Message;
        switch (i) {
            case -2082087340:
                tLRPC$Message = new TLRPC$TL_messageEmpty() { // from class: org.telegram.tgnet.TLRPC$TL_messageEmpty_layer122
                    @Override // org.telegram.tgnet.TLRPC$TL_messageEmpty, org.telegram.tgnet.TLObject
                    public void readParams(AbstractSerializedData abstractSerializedData2, boolean z2) {
                        this.id = abstractSerializedData2.readInt32(z2);
                        this.peer_id = new TLRPC$TL_peerUser();
                    }

                    @Override // org.telegram.tgnet.TLRPC$TL_messageEmpty, org.telegram.tgnet.TLObject
                    public void serializeToStream(AbstractSerializedData abstractSerializedData2) {
                        abstractSerializedData2.writeInt32(-2082087340);
                        abstractSerializedData2.writeInt32(this.id);
                    }
                };
                break;
            case -2049520670:
                tLRPC$Message = new TLRPC$TL_message() { // from class: org.telegram.tgnet.TLRPC$TL_message_layer135
                    @Override // org.telegram.tgnet.TLRPC$TL_message, org.telegram.tgnet.TLObject
                    public void readParams(AbstractSerializedData abstractSerializedData2, boolean z2) {
                        int readInt32 = abstractSerializedData2.readInt32(z2);
                        this.flags = readInt32;
                        this.out = (readInt32 & 2) != 0;
                        this.mentioned = (readInt32 & 16) != 0;
                        this.media_unread = (readInt32 & 32) != 0;
                        this.silent = (readInt32 & LiteMode.FLAG_ANIMATED_EMOJI_REACTIONS_NOT_PREMIUM) != 0;
                        this.post = (readInt32 & LiteMode.FLAG_ANIMATED_EMOJI_KEYBOARD_NOT_PREMIUM) != 0;
                        this.from_scheduled = (262144 & readInt32) != 0;
                        this.legacy = (524288 & readInt32) != 0;
                        this.edit_hide = (2097152 & readInt32) != 0;
                        this.pinned = (16777216 & readInt32) != 0;
                        this.noforwards = (readInt32 & ConnectionsManager.FileTypeFile) != 0;
                        this.id = abstractSerializedData2.readInt32(z2);
                        if ((this.flags & LiteMode.FLAG_CHAT_BLUR) != 0) {
                            this.from_id = TLRPC$Peer.TLdeserialize(abstractSerializedData2, abstractSerializedData2.readInt32(z2), z2);
                        }
                        this.peer_id = TLRPC$Peer.TLdeserialize(abstractSerializedData2, abstractSerializedData2.readInt32(z2), z2);
                        if ((this.flags & 4) != 0) {
                            this.fwd_from = TLRPC$MessageFwdHeader.TLdeserialize(abstractSerializedData2, abstractSerializedData2.readInt32(z2), z2);
                        }
                        if ((this.flags & 2048) != 0) {
                            this.via_bot_id = abstractSerializedData2.readInt64(z2);
                        }
                        if ((this.flags & 8) != 0) {
                            this.reply_to = TLRPC$MessageReplyHeader.TLdeserialize(abstractSerializedData2, abstractSerializedData2.readInt32(z2), z2);
                        }
                        this.date = abstractSerializedData2.readInt32(z2);
                        this.message = abstractSerializedData2.readString(z2);
                        if ((this.flags & LiteMode.FLAG_CALLS_ANIMATIONS) != 0) {
                            TLRPC$MessageMedia TLdeserialize = TLRPC$MessageMedia.TLdeserialize(abstractSerializedData2, abstractSerializedData2.readInt32(z2), z2);
                            this.media = TLdeserialize;
                            if (TLdeserialize != null) {
                                this.ttl = TLdeserialize.ttl_seconds;
                            }
                            if (TLdeserialize != null && !TextUtils.isEmpty(TLdeserialize.captionLegacy)) {
                                this.message = this.media.captionLegacy;
                            }
                        }
                        if ((this.flags & 64) != 0) {
                            this.reply_markup = TLRPC$ReplyMarkup.TLdeserialize(abstractSerializedData2, abstractSerializedData2.readInt32(z2), z2);
                        }
                        if ((this.flags & 128) != 0) {
                            int readInt322 = abstractSerializedData2.readInt32(z2);
                            if (readInt322 != 481674261) {
                                if (z2) {
                                    throw new RuntimeException(String.format("wrong Vector magic, got %x", Integer.valueOf(readInt322)));
                                }
                                return;
                            }
                            int readInt323 = abstractSerializedData2.readInt32(z2);
                            for (int i2 = 0; i2 < readInt323; i2++) {
                                TLRPC$MessageEntity TLdeserialize2 = TLRPC$MessageEntity.TLdeserialize(abstractSerializedData2, abstractSerializedData2.readInt32(z2), z2);
                                if (TLdeserialize2 == null) {
                                    return;
                                }
                                this.entities.add(TLdeserialize2);
                            }
                        }
                        if ((this.flags & 1024) != 0) {
                            this.views = abstractSerializedData2.readInt32(z2);
                        }
                        if ((this.flags & 1024) != 0) {
                            this.forwards = abstractSerializedData2.readInt32(z2);
                        }
                        if ((this.flags & 8388608) != 0) {
                            this.replies = TLRPC$MessageReplies.TLdeserialize(abstractSerializedData2, abstractSerializedData2.readInt32(z2), z2);
                        }
                        if ((this.flags & LiteMode.FLAG_CHAT_SCALE) != 0) {
                            this.edit_date = abstractSerializedData2.readInt32(z2);
                        }
                        if ((this.flags & 65536) != 0) {
                            this.post_author = abstractSerializedData2.readString(z2);
                        }
                        if ((this.flags & 131072) != 0) {
                            this.grouped_id = abstractSerializedData2.readInt64(z2);
                        }
                        if ((this.flags & 4194304) != 0) {
                            int readInt324 = abstractSerializedData2.readInt32(z2);
                            if (readInt324 != 481674261) {
                                if (z2) {
                                    throw new RuntimeException(String.format("wrong Vector magic, got %x", Integer.valueOf(readInt324)));
                                }
                                return;
                            }
                            int readInt325 = abstractSerializedData2.readInt32(z2);
                            for (int i3 = 0; i3 < readInt325; i3++) {
                                TLRPC$TL_restrictionReason TLdeserialize3 = TLRPC$TL_restrictionReason.TLdeserialize(abstractSerializedData2, abstractSerializedData2.readInt32(z2), z2);
                                if (TLdeserialize3 == null) {
                                    return;
                                }
                                this.restriction_reason.add(TLdeserialize3);
                            }
                        }
                        if ((this.flags & ConnectionsManager.FileTypeVideo) != 0) {
                            this.ttl_period = abstractSerializedData2.readInt32(z2);
                        }
                    }

                    @Override // org.telegram.tgnet.TLRPC$TL_message, org.telegram.tgnet.TLObject
                    public void serializeToStream(AbstractSerializedData abstractSerializedData2) {
                        abstractSerializedData2.writeInt32(-2049520670);
                        int i2 = this.out ? this.flags | 2 : this.flags & (-3);
                        this.flags = i2;
                        int i3 = this.mentioned ? i2 | 16 : i2 & (-17);
                        this.flags = i3;
                        int i4 = this.media_unread ? i3 | 32 : i3 & (-33);
                        this.flags = i4;
                        int i5 = this.silent ? i4 | LiteMode.FLAG_ANIMATED_EMOJI_REACTIONS_NOT_PREMIUM : i4 & (-8193);
                        this.flags = i5;
                        int i6 = this.post ? i5 | LiteMode.FLAG_ANIMATED_EMOJI_KEYBOARD_NOT_PREMIUM : i5 & (-16385);
                        this.flags = i6;
                        int i7 = this.from_scheduled ? i6 | 262144 : i6 & (-262145);
                        this.flags = i7;
                        int i8 = this.legacy ? i7 | 524288 : i7 & (-524289);
                        this.flags = i8;
                        int i9 = this.edit_hide ? i8 | 2097152 : i8 & (-2097153);
                        this.flags = i9;
                        int i10 = this.pinned ? i9 | ConnectionsManager.FileTypePhoto : i9 & (-16777217);
                        this.flags = i10;
                        int i11 = this.noforwards ? i10 | ConnectionsManager.FileTypeFile : i10 & (-67108865);
                        this.flags = i11;
                        abstractSerializedData2.writeInt32(i11);
                        abstractSerializedData2.writeInt32(this.id);
                        if ((this.flags & LiteMode.FLAG_CHAT_BLUR) != 0) {
                            this.from_id.serializeToStream(abstractSerializedData2);
                        }
                        this.peer_id.serializeToStream(abstractSerializedData2);
                        if ((this.flags & 4) != 0) {
                            this.fwd_from.serializeToStream(abstractSerializedData2);
                        }
                        if ((this.flags & 2048) != 0) {
                            abstractSerializedData2.writeInt64(this.via_bot_id);
                        }
                        if ((this.flags & 8) != 0) {
                            this.reply_to.serializeToStream(abstractSerializedData2);
                        }
                        abstractSerializedData2.writeInt32(this.date);
                        abstractSerializedData2.writeString(this.message);
                        if ((this.flags & LiteMode.FLAG_CALLS_ANIMATIONS) != 0) {
                            this.media.serializeToStream(abstractSerializedData2);
                        }
                        if ((this.flags & 64) != 0) {
                            this.reply_markup.serializeToStream(abstractSerializedData2);
                        }
                        if ((this.flags & 128) != 0) {
                            abstractSerializedData2.writeInt32(481674261);
                            int size = this.entities.size();
                            abstractSerializedData2.writeInt32(size);
                            for (int i12 = 0; i12 < size; i12++) {
                                this.entities.get(i12).serializeToStream(abstractSerializedData2);
                            }
                        }
                        if ((this.flags & 1024) != 0) {
                            abstractSerializedData2.writeInt32(this.views);
                        }
                        if ((this.flags & 1024) != 0) {
                            abstractSerializedData2.writeInt32(this.forwards);
                        }
                        if ((this.flags & 8388608) != 0) {
                            this.replies.serializeToStream(abstractSerializedData2);
                        }
                        if ((this.flags & LiteMode.FLAG_CHAT_SCALE) != 0) {
                            abstractSerializedData2.writeInt32(this.edit_date);
                        }
                        if ((this.flags & 65536) != 0) {
                            abstractSerializedData2.writeString(this.post_author);
                        }
                        if ((this.flags & 131072) != 0) {
                            abstractSerializedData2.writeInt64(this.grouped_id);
                        }
                        if ((this.flags & 4194304) != 0) {
                            abstractSerializedData2.writeInt32(481674261);
                            int size2 = this.restriction_reason.size();
                            abstractSerializedData2.writeInt32(size2);
                            for (int i13 = 0; i13 < size2; i13++) {
                                this.restriction_reason.get(i13).serializeToStream(abstractSerializedData2);
                            }
                        }
                        if ((this.flags & ConnectionsManager.FileTypeVideo) != 0) {
                            abstractSerializedData2.writeInt32(this.ttl_period);
                        }
                        writeAttachPath(abstractSerializedData2);
                    }
                };
                break;
            case -1868117372:
                tLRPC$Message = new TLRPC$TL_messageEmpty();
                break;
            case -1864508399:
                tLRPC$Message = new TLRPC$TL_message() { // from class: org.telegram.tgnet.TLRPC$TL_message_layer72
                    @Override // org.telegram.tgnet.TLRPC$TL_message, org.telegram.tgnet.TLObject
                    public void readParams(AbstractSerializedData abstractSerializedData2, boolean z2) {
                        int readInt32 = abstractSerializedData2.readInt32(z2);
                        this.flags = readInt32;
                        this.out = (readInt32 & 2) != 0;
                        this.mentioned = (readInt32 & 16) != 0;
                        this.media_unread = (readInt32 & 32) != 0;
                        this.silent = (readInt32 & LiteMode.FLAG_ANIMATED_EMOJI_REACTIONS_NOT_PREMIUM) != 0;
                        this.post = (readInt32 & LiteMode.FLAG_ANIMATED_EMOJI_KEYBOARD_NOT_PREMIUM) != 0;
                        this.id = abstractSerializedData2.readInt32(z2);
                        if ((this.flags & LiteMode.FLAG_CHAT_BLUR) != 0) {
                            TLRPC$TL_peerUser tLRPC$TL_peerUser = new TLRPC$TL_peerUser();
                            this.from_id = tLRPC$TL_peerUser;
                            tLRPC$TL_peerUser.user_id = abstractSerializedData2.readInt32(z2);
                        }
                        this.peer_id = TLRPC$Peer.TLdeserialize(abstractSerializedData2, abstractSerializedData2.readInt32(z2), z2);
                        if ((this.flags & 4) != 0) {
                            this.fwd_from = TLRPC$MessageFwdHeader.TLdeserialize(abstractSerializedData2, abstractSerializedData2.readInt32(z2), z2);
                        }
                        if ((this.flags & 2048) != 0) {
                            this.via_bot_id = abstractSerializedData2.readInt32(z2);
                        }
                        if ((this.flags & 8) != 0) {
                            TLRPC$TL_messageReplyHeader tLRPC$TL_messageReplyHeader = new TLRPC$TL_messageReplyHeader();
                            this.reply_to = tLRPC$TL_messageReplyHeader;
                            tLRPC$TL_messageReplyHeader.flags |= 16;
                            tLRPC$TL_messageReplyHeader.reply_to_msg_id = abstractSerializedData2.readInt32(z2);
                        }
                        this.date = abstractSerializedData2.readInt32(z2);
                        this.message = abstractSerializedData2.readString(z2);
                        if ((this.flags & LiteMode.FLAG_CALLS_ANIMATIONS) != 0) {
                            TLRPC$MessageMedia TLdeserialize = TLRPC$MessageMedia.TLdeserialize(abstractSerializedData2, abstractSerializedData2.readInt32(z2), z2);
                            this.media = TLdeserialize;
                            if (TLdeserialize != null) {
                                this.ttl = TLdeserialize.ttl_seconds;
                            }
                            if (TLdeserialize != null && !TextUtils.isEmpty(TLdeserialize.captionLegacy)) {
                                this.message = this.media.captionLegacy;
                            }
                        }
                        if ((this.flags & 64) != 0) {
                            this.reply_markup = TLRPC$ReplyMarkup.TLdeserialize(abstractSerializedData2, abstractSerializedData2.readInt32(z2), z2);
                        }
                        if ((this.flags & 128) != 0) {
                            int readInt322 = abstractSerializedData2.readInt32(z2);
                            if (readInt322 != 481674261) {
                                if (z2) {
                                    throw new RuntimeException(String.format("wrong Vector magic, got %x", Integer.valueOf(readInt322)));
                                }
                                return;
                            }
                            int readInt323 = abstractSerializedData2.readInt32(z2);
                            for (int i2 = 0; i2 < readInt323; i2++) {
                                TLRPC$MessageEntity TLdeserialize2 = TLRPC$MessageEntity.TLdeserialize(abstractSerializedData2, abstractSerializedData2.readInt32(z2), z2);
                                if (TLdeserialize2 == null) {
                                    return;
                                }
                                this.entities.add(TLdeserialize2);
                            }
                        }
                        if ((this.flags & 1024) != 0) {
                            this.views = abstractSerializedData2.readInt32(z2);
                        }
                        if ((this.flags & LiteMode.FLAG_CHAT_SCALE) != 0) {
                            this.edit_date = abstractSerializedData2.readInt32(z2);
                        }
                        if ((this.flags & 65536) != 0) {
                            this.post_author = abstractSerializedData2.readString(z2);
                        }
                    }

                    @Override // org.telegram.tgnet.TLRPC$TL_message, org.telegram.tgnet.TLObject
                    public void serializeToStream(AbstractSerializedData abstractSerializedData2) {
                        abstractSerializedData2.writeInt32(-1864508399);
                        int i2 = this.out ? this.flags | 2 : this.flags & (-3);
                        this.flags = i2;
                        int i3 = this.mentioned ? i2 | 16 : i2 & (-17);
                        this.flags = i3;
                        int i4 = this.media_unread ? i3 | 32 : i3 & (-33);
                        this.flags = i4;
                        int i5 = this.silent ? i4 | LiteMode.FLAG_ANIMATED_EMOJI_REACTIONS_NOT_PREMIUM : i4 & (-8193);
                        this.flags = i5;
                        int i6 = this.post ? i5 | LiteMode.FLAG_ANIMATED_EMOJI_KEYBOARD_NOT_PREMIUM : i5 & (-16385);
                        this.flags = i6;
                        abstractSerializedData2.writeInt32(i6);
                        abstractSerializedData2.writeInt32(this.id);
                        if ((this.flags & LiteMode.FLAG_CHAT_BLUR) != 0) {
                            abstractSerializedData2.writeInt32((int) this.from_id.user_id);
                        }
                        this.peer_id.serializeToStream(abstractSerializedData2);
                        if ((this.flags & 4) != 0) {
                            this.fwd_from.serializeToStream(abstractSerializedData2);
                        }
                        if ((this.flags & 2048) != 0) {
                            abstractSerializedData2.writeInt32((int) this.via_bot_id);
                        }
                        if ((this.flags & 8) != 0) {
                            abstractSerializedData2.writeInt32(this.reply_to.reply_to_msg_id);
                        }
                        abstractSerializedData2.writeInt32(this.date);
                        abstractSerializedData2.writeString(this.message);
                        if ((this.flags & LiteMode.FLAG_CALLS_ANIMATIONS) != 0) {
                            this.media.serializeToStream(abstractSerializedData2);
                        }
                        if ((this.flags & 64) != 0) {
                            this.reply_markup.serializeToStream(abstractSerializedData2);
                        }
                        if ((this.flags & 128) != 0) {
                            abstractSerializedData2.writeInt32(481674261);
                            int size = this.entities.size();
                            abstractSerializedData2.writeInt32(size);
                            for (int i7 = 0; i7 < size; i7++) {
                                this.entities.get(i7).serializeToStream(abstractSerializedData2);
                            }
                        }
                        if ((this.flags & 1024) != 0) {
                            abstractSerializedData2.writeInt32(this.views);
                        }
                        if ((this.flags & LiteMode.FLAG_CHAT_SCALE) != 0) {
                            abstractSerializedData2.writeInt32(this.edit_date);
                        }
                        if ((this.flags & 65536) != 0) {
                            abstractSerializedData2.writeString(this.post_author);
                        }
                        writeAttachPath(abstractSerializedData2);
                    }
                };
                break;
            case -1752573244:
                tLRPC$Message = new TLRPC$TL_message() { // from class: org.telegram.tgnet.TLRPC$TL_message_layer104_3
                    @Override // org.telegram.tgnet.TLRPC$TL_message, org.telegram.tgnet.TLObject
                    public void readParams(AbstractSerializedData abstractSerializedData2, boolean z2) {
                        int readInt32 = abstractSerializedData2.readInt32(z2);
                        this.flags = readInt32;
                        this.out = (readInt32 & 2) != 0;
                        this.mentioned = (readInt32 & 16) != 0;
                        this.media_unread = (readInt32 & 32) != 0;
                        this.silent = (readInt32 & LiteMode.FLAG_ANIMATED_EMOJI_REACTIONS_NOT_PREMIUM) != 0;
                        this.post = (readInt32 & LiteMode.FLAG_ANIMATED_EMOJI_KEYBOARD_NOT_PREMIUM) != 0;
                        this.from_scheduled = (262144 & readInt32) != 0;
                        this.legacy = (524288 & readInt32) != 0;
                        this.edit_hide = (readInt32 & 2097152) != 0;
                        this.id = abstractSerializedData2.readInt32(z2);
                        if ((this.flags & LiteMode.FLAG_CHAT_BLUR) != 0) {
                            TLRPC$TL_peerUser tLRPC$TL_peerUser = new TLRPC$TL_peerUser();
                            this.from_id = tLRPC$TL_peerUser;
                            tLRPC$TL_peerUser.user_id = abstractSerializedData2.readInt32(z2);
                        }
                        this.peer_id = TLRPC$Peer.TLdeserialize(abstractSerializedData2, abstractSerializedData2.readInt32(z2), z2);
                        if ((this.flags & 4) != 0) {
                            this.fwd_from = TLRPC$MessageFwdHeader.TLdeserialize(abstractSerializedData2, abstractSerializedData2.readInt32(z2), z2);
                        }
                        if ((this.flags & 2048) != 0) {
                            this.via_bot_id = abstractSerializedData2.readInt32(z2);
                        }
                        if ((this.flags & 8) != 0) {
                            TLRPC$TL_messageReplyHeader tLRPC$TL_messageReplyHeader = new TLRPC$TL_messageReplyHeader();
                            this.reply_to = tLRPC$TL_messageReplyHeader;
                            tLRPC$TL_messageReplyHeader.flags |= 16;
                            tLRPC$TL_messageReplyHeader.reply_to_msg_id = abstractSerializedData2.readInt32(z2);
                        }
                        this.date = abstractSerializedData2.readInt32(z2);
                        this.message = abstractSerializedData2.readString(z2);
                        if ((this.flags & LiteMode.FLAG_CALLS_ANIMATIONS) != 0) {
                            TLRPC$MessageMedia TLdeserialize = TLRPC$MessageMedia.TLdeserialize(abstractSerializedData2, abstractSerializedData2.readInt32(z2), z2);
                            this.media = TLdeserialize;
                            if (TLdeserialize != null) {
                                this.ttl = TLdeserialize.ttl_seconds;
                            }
                            if (TLdeserialize != null && !TextUtils.isEmpty(TLdeserialize.captionLegacy)) {
                                this.message = this.media.captionLegacy;
                            }
                        }
                        if ((this.flags & 64) != 0) {
                            this.reply_markup = TLRPC$ReplyMarkup.TLdeserialize(abstractSerializedData2, abstractSerializedData2.readInt32(z2), z2);
                        }
                        if ((this.flags & 128) != 0) {
                            int readInt322 = abstractSerializedData2.readInt32(z2);
                            if (readInt322 != 481674261) {
                                if (z2) {
                                    throw new RuntimeException(String.format("wrong Vector magic, got %x", Integer.valueOf(readInt322)));
                                }
                                return;
                            }
                            int readInt323 = abstractSerializedData2.readInt32(z2);
                            for (int i2 = 0; i2 < readInt323; i2++) {
                                TLRPC$MessageEntity TLdeserialize2 = TLRPC$MessageEntity.TLdeserialize(abstractSerializedData2, abstractSerializedData2.readInt32(z2), z2);
                                if (TLdeserialize2 == null) {
                                    return;
                                }
                                this.entities.add(TLdeserialize2);
                            }
                        }
                        if ((this.flags & 1024) != 0) {
                            this.views = abstractSerializedData2.readInt32(z2);
                        }
                        if ((this.flags & LiteMode.FLAG_CHAT_SCALE) != 0) {
                            this.edit_date = abstractSerializedData2.readInt32(z2);
                        }
                        if ((this.flags & 65536) != 0) {
                            this.post_author = abstractSerializedData2.readString(z2);
                        }
                        if ((this.flags & 131072) != 0) {
                            this.grouped_id = abstractSerializedData2.readInt64(z2);
                        }
                        if ((this.flags & FileLoaderPriorityQueue.PRIORITY_VALUE_MAX) != 0) {
                            this.reactions = TLRPC$MessageReactions.TLdeserialize(abstractSerializedData2, abstractSerializedData2.readInt32(z2), z2);
                        }
                        if ((this.flags & 4194304) != 0) {
                            int readInt324 = abstractSerializedData2.readInt32(z2);
                            if (readInt324 != 481674261) {
                                if (z2) {
                                    throw new RuntimeException(String.format("wrong Vector magic, got %x", Integer.valueOf(readInt324)));
                                }
                                return;
                            }
                            int readInt325 = abstractSerializedData2.readInt32(z2);
                            for (int i3 = 0; i3 < readInt325; i3++) {
                                TLRPC$TL_restrictionReason TLdeserialize3 = TLRPC$TL_restrictionReason.TLdeserialize(abstractSerializedData2, abstractSerializedData2.readInt32(z2), z2);
                                if (TLdeserialize3 == null) {
                                    return;
                                }
                                this.restriction_reason.add(TLdeserialize3);
                            }
                        }
                    }

                    @Override // org.telegram.tgnet.TLRPC$TL_message, org.telegram.tgnet.TLObject
                    public void serializeToStream(AbstractSerializedData abstractSerializedData2) {
                        abstractSerializedData2.writeInt32(-1752573244);
                        int i2 = this.out ? this.flags | 2 : this.flags & (-3);
                        this.flags = i2;
                        int i3 = this.mentioned ? i2 | 16 : i2 & (-17);
                        this.flags = i3;
                        int i4 = this.media_unread ? i3 | 32 : i3 & (-33);
                        this.flags = i4;
                        int i5 = this.silent ? i4 | LiteMode.FLAG_ANIMATED_EMOJI_REACTIONS_NOT_PREMIUM : i4 & (-8193);
                        this.flags = i5;
                        int i6 = this.post ? i5 | LiteMode.FLAG_ANIMATED_EMOJI_KEYBOARD_NOT_PREMIUM : i5 & (-16385);
                        this.flags = i6;
                        int i7 = this.from_scheduled ? i6 | 262144 : i6 & (-262145);
                        this.flags = i7;
                        int i8 = this.legacy ? i7 | 524288 : i7 & (-524289);
                        this.flags = i8;
                        int i9 = this.edit_hide ? i8 | 2097152 : i8 & (-2097153);
                        this.flags = i9;
                        abstractSerializedData2.writeInt32(i9);
                        abstractSerializedData2.writeInt32(this.id);
                        if ((this.flags & LiteMode.FLAG_CHAT_BLUR) != 0) {
                            abstractSerializedData2.writeInt32((int) this.from_id.user_id);
                        }
                        this.peer_id.serializeToStream(abstractSerializedData2);
                        if ((this.flags & 4) != 0) {
                            this.fwd_from.serializeToStream(abstractSerializedData2);
                        }
                        if ((this.flags & 2048) != 0) {
                            abstractSerializedData2.writeInt32((int) this.via_bot_id);
                        }
                        if ((this.flags & 8) != 0) {
                            abstractSerializedData2.writeInt32(this.reply_to.reply_to_msg_id);
                        }
                        abstractSerializedData2.writeInt32(this.date);
                        abstractSerializedData2.writeString(this.message);
                        if ((this.flags & LiteMode.FLAG_CALLS_ANIMATIONS) != 0) {
                            this.media.serializeToStream(abstractSerializedData2);
                        }
                        if ((this.flags & 64) != 0) {
                            this.reply_markup.serializeToStream(abstractSerializedData2);
                        }
                        if ((this.flags & 128) != 0) {
                            abstractSerializedData2.writeInt32(481674261);
                            int size = this.entities.size();
                            abstractSerializedData2.writeInt32(size);
                            for (int i10 = 0; i10 < size; i10++) {
                                this.entities.get(i10).serializeToStream(abstractSerializedData2);
                            }
                        }
                        if ((this.flags & 1024) != 0) {
                            abstractSerializedData2.writeInt32(this.views);
                        }
                        if ((this.flags & LiteMode.FLAG_CHAT_SCALE) != 0) {
                            abstractSerializedData2.writeInt32(this.edit_date);
                        }
                        if ((this.flags & 65536) != 0) {
                            abstractSerializedData2.writeString(this.post_author);
                        }
                        if ((this.flags & 131072) != 0) {
                            abstractSerializedData2.writeInt64(this.grouped_id);
                        }
                        if ((this.flags & FileLoaderPriorityQueue.PRIORITY_VALUE_MAX) != 0) {
                            this.reactions.serializeToStream(abstractSerializedData2);
                        }
                        if ((this.flags & 4194304) != 0) {
                            abstractSerializedData2.writeInt32(481674261);
                            int size2 = this.restriction_reason.size();
                            abstractSerializedData2.writeInt32(size2);
                            for (int i11 = 0; i11 < size2; i11++) {
                                this.restriction_reason.get(i11).serializeToStream(abstractSerializedData2);
                            }
                        }
                        writeAttachPath(abstractSerializedData2);
                    }
                };
                break;
            case -1642487306:
                tLRPC$Message = new TLRPC$TL_messageService() { // from class: org.telegram.tgnet.TLRPC$TL_messageService_layer118
                    @Override // org.telegram.tgnet.TLRPC$TL_messageService, org.telegram.tgnet.TLObject
                    public void readParams(AbstractSerializedData abstractSerializedData2, boolean z2) {
                        int readInt32 = abstractSerializedData2.readInt32(z2);
                        this.flags = readInt32;
                        this.unread = (readInt32 & 1) != 0;
                        this.out = (readInt32 & 2) != 0;
                        this.mentioned = (readInt32 & 16) != 0;
                        this.media_unread = (readInt32 & 32) != 0;
                        this.silent = (readInt32 & LiteMode.FLAG_ANIMATED_EMOJI_REACTIONS_NOT_PREMIUM) != 0;
                        this.post = (readInt32 & LiteMode.FLAG_ANIMATED_EMOJI_KEYBOARD_NOT_PREMIUM) != 0;
                        this.legacy = (readInt32 & 524288) != 0;
                        this.id = abstractSerializedData2.readInt32(z2);
                        if ((this.flags & LiteMode.FLAG_CHAT_BLUR) != 0) {
                            TLRPC$TL_peerUser tLRPC$TL_peerUser = new TLRPC$TL_peerUser();
                            this.from_id = tLRPC$TL_peerUser;
                            tLRPC$TL_peerUser.user_id = abstractSerializedData2.readInt32(z2);
                        }
                        this.peer_id = TLRPC$Peer.TLdeserialize(abstractSerializedData2, abstractSerializedData2.readInt32(z2), z2);
                        if ((this.flags & 8) != 0) {
                            TLRPC$TL_messageReplyHeader tLRPC$TL_messageReplyHeader = new TLRPC$TL_messageReplyHeader();
                            this.reply_to = tLRPC$TL_messageReplyHeader;
                            tLRPC$TL_messageReplyHeader.flags |= 16;
                            tLRPC$TL_messageReplyHeader.reply_to_msg_id = abstractSerializedData2.readInt32(z2);
                        }
                        this.date = abstractSerializedData2.readInt32(z2);
                        this.action = TLRPC$MessageAction.TLdeserialize(abstractSerializedData2, abstractSerializedData2.readInt32(z2), z2);
                    }

                    @Override // org.telegram.tgnet.TLRPC$TL_messageService, org.telegram.tgnet.TLObject
                    public void serializeToStream(AbstractSerializedData abstractSerializedData2) {
                        abstractSerializedData2.writeInt32(-1642487306);
                        int i2 = this.unread ? this.flags | 1 : this.flags & (-2);
                        this.flags = i2;
                        int i3 = this.out ? i2 | 2 : i2 & (-3);
                        this.flags = i3;
                        int i4 = this.mentioned ? i3 | 16 : i3 & (-17);
                        this.flags = i4;
                        int i5 = this.media_unread ? i4 | 32 : i4 & (-33);
                        this.flags = i5;
                        int i6 = this.silent ? i5 | LiteMode.FLAG_ANIMATED_EMOJI_REACTIONS_NOT_PREMIUM : i5 & (-8193);
                        this.flags = i6;
                        int i7 = this.post ? i6 | LiteMode.FLAG_ANIMATED_EMOJI_KEYBOARD_NOT_PREMIUM : i6 & (-16385);
                        this.flags = i7;
                        int i8 = this.legacy ? i7 | 524288 : i7 & (-524289);
                        this.flags = i8;
                        abstractSerializedData2.writeInt32(i8);
                        abstractSerializedData2.writeInt32(this.id);
                        if ((this.flags & LiteMode.FLAG_CHAT_BLUR) != 0) {
                            abstractSerializedData2.writeInt32((int) this.from_id.user_id);
                        }
                        this.peer_id.serializeToStream(abstractSerializedData2);
                        if ((this.flags & 8) != 0) {
                            abstractSerializedData2.writeInt32(this.reply_to.reply_to_msg_id);
                        }
                        abstractSerializedData2.writeInt32(this.date);
                        this.action.serializeToStream(abstractSerializedData2);
                        writeAttachPath(abstractSerializedData2);
                    }
                };
                break;
            case -1618124613:
                tLRPC$Message = new TLRPC$TL_messageService() { // from class: org.telegram.tgnet.TLRPC$TL_messageService_old
                    @Override // org.telegram.tgnet.TLRPC$TL_messageService, org.telegram.tgnet.TLObject
                    public void readParams(AbstractSerializedData abstractSerializedData2, boolean z2) {
                        this.id = abstractSerializedData2.readInt32(z2);
                        TLRPC$TL_peerUser tLRPC$TL_peerUser = new TLRPC$TL_peerUser();
                        this.from_id = tLRPC$TL_peerUser;
                        tLRPC$TL_peerUser.user_id = abstractSerializedData2.readInt32(z2);
                        this.peer_id = TLRPC$Peer.TLdeserialize(abstractSerializedData2, abstractSerializedData2.readInt32(z2), z2);
                        this.out = abstractSerializedData2.readBool(z2);
                        this.unread = abstractSerializedData2.readBool(z2);
                        this.flags |= LiteMode.FLAG_CHAT_BLUR;
                        this.date = abstractSerializedData2.readInt32(z2);
                        this.action = TLRPC$MessageAction.TLdeserialize(abstractSerializedData2, abstractSerializedData2.readInt32(z2), z2);
                    }

                    @Override // org.telegram.tgnet.TLRPC$TL_messageService, org.telegram.tgnet.TLObject
                    public void serializeToStream(AbstractSerializedData abstractSerializedData2) {
                        abstractSerializedData2.writeInt32(-1618124613);
                        abstractSerializedData2.writeInt32(this.id);
                        abstractSerializedData2.writeInt32((int) this.from_id.user_id);
                        this.peer_id.serializeToStream(abstractSerializedData2);
                        abstractSerializedData2.writeBool(this.out);
                        abstractSerializedData2.writeBool(this.unread);
                        abstractSerializedData2.writeInt32(this.date);
                        this.action.serializeToStream(abstractSerializedData2);
                    }
                };
                break;
            case -1553471722:
                tLRPC$Message = new TLRPC$TL_messageForwarded_old2();
                break;
            case -1528201417:
            case 592953125:
                tLRPC$Message = new TLRPC$TL_message();
                break;
            case -1502839044:
                tLRPC$Message = new TLRPC$TL_message() { // from class: org.telegram.tgnet.TLRPC$TL_message_layer176
                    @Override // org.telegram.tgnet.TLRPC$TL_message, org.telegram.tgnet.TLObject
                    public void readParams(AbstractSerializedData abstractSerializedData2, boolean z2) {
                        int readInt32 = abstractSerializedData2.readInt32(z2);
                        this.flags = readInt32;
                        this.out = (readInt32 & 2) != 0;
                        this.mentioned = (readInt32 & 16) != 0;
                        this.media_unread = (readInt32 & 32) != 0;
                        this.silent = (readInt32 & LiteMode.FLAG_ANIMATED_EMOJI_REACTIONS_NOT_PREMIUM) != 0;
                        this.post = (readInt32 & LiteMode.FLAG_ANIMATED_EMOJI_KEYBOARD_NOT_PREMIUM) != 0;
                        this.from_scheduled = (262144 & readInt32) != 0;
                        this.legacy = (524288 & readInt32) != 0;
                        this.edit_hide = (2097152 & readInt32) != 0;
                        this.pinned = (16777216 & readInt32) != 0;
                        this.noforwards = (67108864 & readInt32) != 0;
                        this.invert_media = (readInt32 & 134217728) != 0;
                        this.id = abstractSerializedData2.readInt32(z2);
                        if ((this.flags & LiteMode.FLAG_CHAT_BLUR) != 0) {
                            this.from_id = TLRPC$Peer.TLdeserialize(abstractSerializedData2, abstractSerializedData2.readInt32(z2), z2);
                        }
                        if ((this.flags & 536870912) != 0) {
                            this.from_boosts_applied = abstractSerializedData2.readInt32(z2);
                        }
                        this.peer_id = TLRPC$Peer.TLdeserialize(abstractSerializedData2, abstractSerializedData2.readInt32(z2), z2);
                        if ((this.flags & 268435456) != 0) {
                            this.saved_peer_id = TLRPC$Peer.TLdeserialize(abstractSerializedData2, abstractSerializedData2.readInt32(z2), z2);
                        }
                        if ((this.flags & 4) != 0) {
                            this.fwd_from = TLRPC$MessageFwdHeader.TLdeserialize(abstractSerializedData2, abstractSerializedData2.readInt32(z2), z2);
                        }
                        if ((this.flags & 2048) != 0) {
                            this.via_bot_id = abstractSerializedData2.readInt64(z2);
                        }
                        if ((this.flags & 8) != 0) {
                            this.reply_to = TLRPC$MessageReplyHeader.TLdeserialize(abstractSerializedData2, abstractSerializedData2.readInt32(z2), z2);
                        }
                        this.date = abstractSerializedData2.readInt32(z2);
                        this.message = abstractSerializedData2.readString(z2);
                        if ((this.flags & LiteMode.FLAG_CALLS_ANIMATIONS) != 0) {
                            TLRPC$MessageMedia TLdeserialize = TLRPC$MessageMedia.TLdeserialize(abstractSerializedData2, abstractSerializedData2.readInt32(z2), z2);
                            this.media = TLdeserialize;
                            if (TLdeserialize != null) {
                                this.ttl = TLdeserialize.ttl_seconds;
                            }
                            if (TLdeserialize != null && !TextUtils.isEmpty(TLdeserialize.captionLegacy)) {
                                this.message = this.media.captionLegacy;
                            }
                        }
                        if ((this.flags & 64) != 0) {
                            this.reply_markup = TLRPC$ReplyMarkup.TLdeserialize(abstractSerializedData2, abstractSerializedData2.readInt32(z2), z2);
                        }
                        if ((this.flags & 128) != 0) {
                            int readInt322 = abstractSerializedData2.readInt32(z2);
                            if (readInt322 != 481674261) {
                                if (z2) {
                                    throw new RuntimeException(String.format("wrong Vector magic, got %x", Integer.valueOf(readInt322)));
                                }
                                return;
                            }
                            int readInt323 = abstractSerializedData2.readInt32(z2);
                            for (int i2 = 0; i2 < readInt323; i2++) {
                                TLRPC$MessageEntity TLdeserialize2 = TLRPC$MessageEntity.TLdeserialize(abstractSerializedData2, abstractSerializedData2.readInt32(z2), z2);
                                if (TLdeserialize2 == null) {
                                    return;
                                }
                                this.entities.add(TLdeserialize2);
                            }
                        }
                        if ((this.flags & 1024) != 0) {
                            this.views = abstractSerializedData2.readInt32(z2);
                        }
                        if ((this.flags & 1024) != 0) {
                            this.forwards = abstractSerializedData2.readInt32(z2);
                        }
                        if ((this.flags & 8388608) != 0) {
                            this.replies = TLRPC$MessageReplies.TLdeserialize(abstractSerializedData2, abstractSerializedData2.readInt32(z2), z2);
                        }
                        if ((this.flags & LiteMode.FLAG_CHAT_SCALE) != 0) {
                            this.edit_date = abstractSerializedData2.readInt32(z2);
                        }
                        if ((this.flags & 65536) != 0) {
                            this.post_author = abstractSerializedData2.readString(z2);
                        }
                        if ((this.flags & 131072) != 0) {
                            this.grouped_id = abstractSerializedData2.readInt64(z2);
                        }
                        if ((this.flags & FileLoaderPriorityQueue.PRIORITY_VALUE_MAX) != 0) {
                            this.reactions = TLRPC$MessageReactions.TLdeserialize(abstractSerializedData2, abstractSerializedData2.readInt32(z2), z2);
                        }
                        if ((this.flags & 4194304) != 0) {
                            int readInt324 = abstractSerializedData2.readInt32(z2);
                            if (readInt324 != 481674261) {
                                if (z2) {
                                    throw new RuntimeException(String.format("wrong Vector magic, got %x", Integer.valueOf(readInt324)));
                                }
                                return;
                            }
                            int readInt325 = abstractSerializedData2.readInt32(z2);
                            for (int i3 = 0; i3 < readInt325; i3++) {
                                TLRPC$TL_restrictionReason TLdeserialize3 = TLRPC$TL_restrictionReason.TLdeserialize(abstractSerializedData2, abstractSerializedData2.readInt32(z2), z2);
                                if (TLdeserialize3 == null) {
                                    return;
                                }
                                this.restriction_reason.add(TLdeserialize3);
                            }
                        }
                        if ((this.flags & ConnectionsManager.FileTypeVideo) != 0) {
                            this.ttl_period = abstractSerializedData2.readInt32(z2);
                        }
                        if ((this.flags & 1073741824) != 0) {
                            this.quick_reply_shortcut_id = abstractSerializedData2.readInt32(z2);
                        }
                    }

                    @Override // org.telegram.tgnet.TLRPC$TL_message, org.telegram.tgnet.TLObject
                    public void serializeToStream(AbstractSerializedData abstractSerializedData2) {
                        abstractSerializedData2.writeInt32(-1502839044);
                        int i2 = this.out ? this.flags | 2 : this.flags & (-3);
                        this.flags = i2;
                        int i3 = this.mentioned ? i2 | 16 : i2 & (-17);
                        this.flags = i3;
                        int i4 = this.media_unread ? i3 | 32 : i3 & (-33);
                        this.flags = i4;
                        int i5 = this.silent ? i4 | LiteMode.FLAG_ANIMATED_EMOJI_REACTIONS_NOT_PREMIUM : i4 & (-8193);
                        this.flags = i5;
                        int i6 = this.post ? i5 | LiteMode.FLAG_ANIMATED_EMOJI_KEYBOARD_NOT_PREMIUM : i5 & (-16385);
                        this.flags = i6;
                        int i7 = this.from_scheduled ? i6 | 262144 : i6 & (-262145);
                        this.flags = i7;
                        int i8 = this.legacy ? i7 | 524288 : i7 & (-524289);
                        this.flags = i8;
                        int i9 = this.edit_hide ? i8 | 2097152 : i8 & (-2097153);
                        this.flags = i9;
                        int i10 = this.pinned ? i9 | ConnectionsManager.FileTypePhoto : i9 & (-16777217);
                        this.flags = i10;
                        int i11 = this.noforwards ? i10 | ConnectionsManager.FileTypeFile : i10 & (-67108865);
                        this.flags = i11;
                        int i12 = this.invert_media ? i11 | 134217728 : i11 & (-134217729);
                        this.flags = i12;
                        abstractSerializedData2.writeInt32(i12);
                        abstractSerializedData2.writeInt32(this.id);
                        if ((this.flags & LiteMode.FLAG_CHAT_BLUR) != 0) {
                            this.from_id.serializeToStream(abstractSerializedData2);
                        }
                        if ((this.flags & 536870912) != 0) {
                            abstractSerializedData2.writeInt32(this.from_boosts_applied);
                        }
                        this.peer_id.serializeToStream(abstractSerializedData2);
                        if ((this.flags & 268435456) != 0) {
                            this.saved_peer_id.serializeToStream(abstractSerializedData2);
                        }
                        if ((this.flags & 4) != 0) {
                            this.fwd_from.serializeToStream(abstractSerializedData2);
                        }
                        if ((this.flags & 2048) != 0) {
                            abstractSerializedData2.writeInt64(this.via_bot_id);
                        }
                        if ((this.flags & 8) != 0) {
                            this.reply_to.serializeToStream(abstractSerializedData2);
                        }
                        abstractSerializedData2.writeInt32(this.date);
                        abstractSerializedData2.writeString(this.message);
                        if ((this.flags & LiteMode.FLAG_CALLS_ANIMATIONS) != 0) {
                            this.media.serializeToStream(abstractSerializedData2);
                        }
                        if ((this.flags & 64) != 0) {
                            this.reply_markup.serializeToStream(abstractSerializedData2);
                        }
                        if ((this.flags & 128) != 0) {
                            abstractSerializedData2.writeInt32(481674261);
                            int size = this.entities.size();
                            abstractSerializedData2.writeInt32(size);
                            for (int i13 = 0; i13 < size; i13++) {
                                this.entities.get(i13).serializeToStream(abstractSerializedData2);
                            }
                        }
                        if ((this.flags & 1024) != 0) {
                            abstractSerializedData2.writeInt32(this.views);
                        }
                        if ((this.flags & 1024) != 0) {
                            abstractSerializedData2.writeInt32(this.forwards);
                        }
                        if ((this.flags & 8388608) != 0) {
                            this.replies.serializeToStream(abstractSerializedData2);
                        }
                        if ((this.flags & LiteMode.FLAG_CHAT_SCALE) != 0) {
                            abstractSerializedData2.writeInt32(this.edit_date);
                        }
                        if ((this.flags & 65536) != 0) {
                            abstractSerializedData2.writeString(this.post_author);
                        }
                        if ((this.flags & 131072) != 0) {
                            abstractSerializedData2.writeInt64(this.grouped_id);
                        }
                        if ((this.flags & FileLoaderPriorityQueue.PRIORITY_VALUE_MAX) != 0) {
                            this.reactions.serializeToStream(abstractSerializedData2);
                        }
                        if ((this.flags & 4194304) != 0) {
                            abstractSerializedData2.writeInt32(481674261);
                            int size2 = this.restriction_reason.size();
                            abstractSerializedData2.writeInt32(size2);
                            for (int i14 = 0; i14 < size2; i14++) {
                                this.restriction_reason.get(i14).serializeToStream(abstractSerializedData2);
                            }
                        }
                        if ((this.flags & ConnectionsManager.FileTypeVideo) != 0) {
                            abstractSerializedData2.writeInt32(this.ttl_period);
                        }
                        if ((this.flags & 1073741824) != 0) {
                            abstractSerializedData2.writeInt32(this.quick_reply_shortcut_id);
                        }
                        writeAttachPath(abstractSerializedData2);
                    }
                };
                break;
            case -1481959023:
                tLRPC$Message = new TLRPC$TL_message() { // from class: org.telegram.tgnet.TLRPC$TL_message_old3
                    @Override // org.telegram.tgnet.TLRPC$TL_message, org.telegram.tgnet.TLObject
                    public void readParams(AbstractSerializedData abstractSerializedData2, boolean z2) {
                        int readInt32 = abstractSerializedData2.readInt32(z2) | LiteMode.FLAG_CHAT_BLUR | LiteMode.FLAG_CALLS_ANIMATIONS;
                        this.flags = readInt32;
                        this.unread = (readInt32 & 1) != 0;
                        this.out = (readInt32 & 2) != 0;
                        this.mentioned = (readInt32 & 16) != 0;
                        this.media_unread = (readInt32 & 32) != 0;
                        this.id = abstractSerializedData2.readInt32(z2);
                        TLRPC$TL_peerUser tLRPC$TL_peerUser = new TLRPC$TL_peerUser();
                        this.from_id = tLRPC$TL_peerUser;
                        tLRPC$TL_peerUser.user_id = abstractSerializedData2.readInt32(z2);
                        this.peer_id = TLRPC$Peer.TLdeserialize(abstractSerializedData2, abstractSerializedData2.readInt32(z2), z2);
                        if ((this.flags & 4) != 0) {
                            TLRPC$TL_messageFwdHeader tLRPC$TL_messageFwdHeader = new TLRPC$TL_messageFwdHeader();
                            this.fwd_from = tLRPC$TL_messageFwdHeader;
                            tLRPC$TL_messageFwdHeader.from_id = new TLRPC$TL_peerUser();
                            this.fwd_from.from_id.user_id = abstractSerializedData2.readInt32(z2);
                            TLRPC$MessageFwdHeader tLRPC$MessageFwdHeader = this.fwd_from;
                            tLRPC$MessageFwdHeader.flags |= 1;
                            tLRPC$MessageFwdHeader.date = abstractSerializedData2.readInt32(z2);
                        }
                        if ((this.flags & 8) != 0) {
                            TLRPC$TL_messageReplyHeader tLRPC$TL_messageReplyHeader = new TLRPC$TL_messageReplyHeader();
                            this.reply_to = tLRPC$TL_messageReplyHeader;
                            tLRPC$TL_messageReplyHeader.flags |= 16;
                            tLRPC$TL_messageReplyHeader.reply_to_msg_id = abstractSerializedData2.readInt32(z2);
                        }
                        this.date = abstractSerializedData2.readInt32(z2);
                        this.message = abstractSerializedData2.readString(z2);
                        TLRPC$MessageMedia TLdeserialize = TLRPC$MessageMedia.TLdeserialize(abstractSerializedData2, abstractSerializedData2.readInt32(z2), z2);
                        this.media = TLdeserialize;
                        if (TLdeserialize == null || TextUtils.isEmpty(TLdeserialize.captionLegacy)) {
                            return;
                        }
                        this.message = this.media.captionLegacy;
                    }

                    @Override // org.telegram.tgnet.TLRPC$TL_message, org.telegram.tgnet.TLObject
                    public void serializeToStream(AbstractSerializedData abstractSerializedData2) {
                        abstractSerializedData2.writeInt32(-1481959023);
                        int i2 = this.unread ? this.flags | 1 : this.flags & (-2);
                        this.flags = i2;
                        int i3 = this.out ? i2 | 2 : i2 & (-3);
                        this.flags = i3;
                        int i4 = this.mentioned ? i3 | 16 : i3 & (-17);
                        this.flags = i4;
                        int i5 = this.media_unread ? i4 | 32 : i4 & (-33);
                        this.flags = i5;
                        abstractSerializedData2.writeInt32(i5);
                        abstractSerializedData2.writeInt32(this.id);
                        abstractSerializedData2.writeInt32((int) this.from_id.user_id);
                        this.peer_id.serializeToStream(abstractSerializedData2);
                        if ((this.flags & 4) != 0) {
                            abstractSerializedData2.writeInt32((int) this.fwd_from.from_id.user_id);
                            abstractSerializedData2.writeInt32(this.fwd_from.date);
                        }
                        if ((this.flags & 8) != 0) {
                            abstractSerializedData2.writeInt32(this.reply_to.reply_to_msg_id);
                        }
                        abstractSerializedData2.writeInt32(this.date);
                        abstractSerializedData2.writeString(this.message);
                        this.media.serializeToStream(abstractSerializedData2);
                        writeAttachPath(abstractSerializedData2);
                    }
                };
                break;
            case -1125940270:
                tLRPC$Message = new TLRPC$TL_message() { // from class: org.telegram.tgnet.TLRPC$TL_message_layer131
                    @Override // org.telegram.tgnet.TLRPC$TL_message, org.telegram.tgnet.TLObject
                    public void readParams(AbstractSerializedData abstractSerializedData2, boolean z2) {
                        int readInt32 = abstractSerializedData2.readInt32(z2);
                        this.flags = readInt32;
                        this.out = (readInt32 & 2) != 0;
                        this.mentioned = (readInt32 & 16) != 0;
                        this.media_unread = (readInt32 & 32) != 0;
                        this.silent = (readInt32 & LiteMode.FLAG_ANIMATED_EMOJI_REACTIONS_NOT_PREMIUM) != 0;
                        this.post = (readInt32 & LiteMode.FLAG_ANIMATED_EMOJI_KEYBOARD_NOT_PREMIUM) != 0;
                        this.from_scheduled = (262144 & readInt32) != 0;
                        this.legacy = (524288 & readInt32) != 0;
                        this.edit_hide = (2097152 & readInt32) != 0;
                        this.pinned = (readInt32 & ConnectionsManager.FileTypePhoto) != 0;
                        this.id = abstractSerializedData2.readInt32(z2);
                        if ((this.flags & LiteMode.FLAG_CHAT_BLUR) != 0) {
                            this.from_id = TLRPC$Peer.TLdeserialize(abstractSerializedData2, abstractSerializedData2.readInt32(z2), z2);
                        }
                        this.peer_id = TLRPC$Peer.TLdeserialize(abstractSerializedData2, abstractSerializedData2.readInt32(z2), z2);
                        if ((this.flags & 4) != 0) {
                            this.fwd_from = TLRPC$MessageFwdHeader.TLdeserialize(abstractSerializedData2, abstractSerializedData2.readInt32(z2), z2);
                        }
                        if ((this.flags & 2048) != 0) {
                            this.via_bot_id = abstractSerializedData2.readInt32(z2);
                        }
                        if ((this.flags & 8) != 0) {
                            this.reply_to = TLRPC$MessageReplyHeader.TLdeserialize(abstractSerializedData2, abstractSerializedData2.readInt32(z2), z2);
                        }
                        this.date = abstractSerializedData2.readInt32(z2);
                        this.message = abstractSerializedData2.readString(z2);
                        if ((this.flags & LiteMode.FLAG_CALLS_ANIMATIONS) != 0) {
                            TLRPC$MessageMedia TLdeserialize = TLRPC$MessageMedia.TLdeserialize(abstractSerializedData2, abstractSerializedData2.readInt32(z2), z2);
                            this.media = TLdeserialize;
                            if (TLdeserialize != null) {
                                this.ttl = TLdeserialize.ttl_seconds;
                            }
                            if (TLdeserialize != null && !TextUtils.isEmpty(TLdeserialize.captionLegacy)) {
                                this.message = this.media.captionLegacy;
                            }
                        }
                        if ((this.flags & 64) != 0) {
                            this.reply_markup = TLRPC$ReplyMarkup.TLdeserialize(abstractSerializedData2, abstractSerializedData2.readInt32(z2), z2);
                        }
                        if ((this.flags & 128) != 0) {
                            int readInt322 = abstractSerializedData2.readInt32(z2);
                            if (readInt322 != 481674261) {
                                if (z2) {
                                    throw new RuntimeException(String.format("wrong Vector magic, got %x", Integer.valueOf(readInt322)));
                                }
                                return;
                            }
                            int readInt323 = abstractSerializedData2.readInt32(z2);
                            for (int i2 = 0; i2 < readInt323; i2++) {
                                TLRPC$MessageEntity TLdeserialize2 = TLRPC$MessageEntity.TLdeserialize(abstractSerializedData2, abstractSerializedData2.readInt32(z2), z2);
                                if (TLdeserialize2 == null) {
                                    return;
                                }
                                this.entities.add(TLdeserialize2);
                            }
                        }
                        if ((this.flags & 1024) != 0) {
                            this.views = abstractSerializedData2.readInt32(z2);
                        }
                        if ((this.flags & 1024) != 0) {
                            this.forwards = abstractSerializedData2.readInt32(z2);
                        }
                        if ((this.flags & 8388608) != 0) {
                            this.replies = TLRPC$MessageReplies.TLdeserialize(abstractSerializedData2, abstractSerializedData2.readInt32(z2), z2);
                        }
                        if ((this.flags & LiteMode.FLAG_CHAT_SCALE) != 0) {
                            this.edit_date = abstractSerializedData2.readInt32(z2);
                        }
                        if ((this.flags & 65536) != 0) {
                            this.post_author = abstractSerializedData2.readString(z2);
                        }
                        if ((this.flags & 131072) != 0) {
                            this.grouped_id = abstractSerializedData2.readInt64(z2);
                        }
                        if ((this.flags & 4194304) != 0) {
                            int readInt324 = abstractSerializedData2.readInt32(z2);
                            if (readInt324 != 481674261) {
                                if (z2) {
                                    throw new RuntimeException(String.format("wrong Vector magic, got %x", Integer.valueOf(readInt324)));
                                }
                                return;
                            }
                            int readInt325 = abstractSerializedData2.readInt32(z2);
                            for (int i3 = 0; i3 < readInt325; i3++) {
                                TLRPC$TL_restrictionReason TLdeserialize3 = TLRPC$TL_restrictionReason.TLdeserialize(abstractSerializedData2, abstractSerializedData2.readInt32(z2), z2);
                                if (TLdeserialize3 == null) {
                                    return;
                                }
                                this.restriction_reason.add(TLdeserialize3);
                            }
                        }
                        if ((this.flags & ConnectionsManager.FileTypeVideo) != 0) {
                            this.ttl_period = abstractSerializedData2.readInt32(z2);
                        }
                    }

                    @Override // org.telegram.tgnet.TLRPC$TL_message, org.telegram.tgnet.TLObject
                    public void serializeToStream(AbstractSerializedData abstractSerializedData2) {
                        abstractSerializedData2.writeInt32(-1125940270);
                        int i2 = this.out ? this.flags | 2 : this.flags & (-3);
                        this.flags = i2;
                        int i3 = this.mentioned ? i2 | 16 : i2 & (-17);
                        this.flags = i3;
                        int i4 = this.media_unread ? i3 | 32 : i3 & (-33);
                        this.flags = i4;
                        int i5 = this.silent ? i4 | LiteMode.FLAG_ANIMATED_EMOJI_REACTIONS_NOT_PREMIUM : i4 & (-8193);
                        this.flags = i5;
                        int i6 = this.post ? i5 | LiteMode.FLAG_ANIMATED_EMOJI_KEYBOARD_NOT_PREMIUM : i5 & (-16385);
                        this.flags = i6;
                        int i7 = this.from_scheduled ? i6 | 262144 : i6 & (-262145);
                        this.flags = i7;
                        int i8 = this.legacy ? i7 | 524288 : i7 & (-524289);
                        this.flags = i8;
                        int i9 = this.edit_hide ? i8 | 2097152 : i8 & (-2097153);
                        this.flags = i9;
                        int i10 = this.pinned ? i9 | ConnectionsManager.FileTypePhoto : i9 & (-16777217);
                        this.flags = i10;
                        abstractSerializedData2.writeInt32(i10);
                        abstractSerializedData2.writeInt32(this.id);
                        if ((this.flags & LiteMode.FLAG_CHAT_BLUR) != 0) {
                            this.from_id.serializeToStream(abstractSerializedData2);
                        }
                        this.peer_id.serializeToStream(abstractSerializedData2);
                        if ((this.flags & 4) != 0) {
                            this.fwd_from.serializeToStream(abstractSerializedData2);
                        }
                        if ((this.flags & 2048) != 0) {
                            abstractSerializedData2.writeInt32((int) this.via_bot_id);
                        }
                        if ((this.flags & 8) != 0) {
                            this.reply_to.serializeToStream(abstractSerializedData2);
                        }
                        abstractSerializedData2.writeInt32(this.date);
                        abstractSerializedData2.writeString(this.message);
                        if ((this.flags & LiteMode.FLAG_CALLS_ANIMATIONS) != 0) {
                            this.media.serializeToStream(abstractSerializedData2);
                        }
                        if ((this.flags & 64) != 0) {
                            this.reply_markup.serializeToStream(abstractSerializedData2);
                        }
                        if ((this.flags & 128) != 0) {
                            abstractSerializedData2.writeInt32(481674261);
                            int size = this.entities.size();
                            abstractSerializedData2.writeInt32(size);
                            for (int i11 = 0; i11 < size; i11++) {
                                this.entities.get(i11).serializeToStream(abstractSerializedData2);
                            }
                        }
                        if ((this.flags & 1024) != 0) {
                            abstractSerializedData2.writeInt32(this.views);
                        }
                        if ((this.flags & 1024) != 0) {
                            abstractSerializedData2.writeInt32(this.forwards);
                        }
                        if ((this.flags & 8388608) != 0) {
                            this.replies.serializeToStream(abstractSerializedData2);
                        }
                        if ((this.flags & LiteMode.FLAG_CHAT_SCALE) != 0) {
                            abstractSerializedData2.writeInt32(this.edit_date);
                        }
                        if ((this.flags & 65536) != 0) {
                            abstractSerializedData2.writeString(this.post_author);
                        }
                        if ((this.flags & 131072) != 0) {
                            abstractSerializedData2.writeInt64(this.grouped_id);
                        }
                        if ((this.flags & 4194304) != 0) {
                            abstractSerializedData2.writeInt32(481674261);
                            int size2 = this.restriction_reason.size();
                            abstractSerializedData2.writeInt32(size2);
                            for (int i12 = 0; i12 < size2; i12++) {
                                this.restriction_reason.get(i12).serializeToStream(abstractSerializedData2);
                            }
                        }
                        if ((this.flags & ConnectionsManager.FileTypeVideo) != 0) {
                            abstractSerializedData2.writeInt32(this.ttl_period);
                        }
                        writeAttachPath(abstractSerializedData2);
                    }
                };
                break;
            case -1066691065:
                tLRPC$Message = new TLRPC$TL_messageService() { // from class: org.telegram.tgnet.TLRPC$TL_messageService_layer48
                    @Override // org.telegram.tgnet.TLRPC$TL_messageService, org.telegram.tgnet.TLObject
                    public void readParams(AbstractSerializedData abstractSerializedData2, boolean z2) {
                        int readInt32 = abstractSerializedData2.readInt32(z2);
                        this.flags = readInt32;
                        this.unread = (readInt32 & 1) != 0;
                        this.out = (readInt32 & 2) != 0;
                        this.mentioned = (readInt32 & 16) != 0;
                        this.media_unread = (readInt32 & 32) != 0;
                        this.silent = (readInt32 & LiteMode.FLAG_ANIMATED_EMOJI_REACTIONS_NOT_PREMIUM) != 0;
                        this.post = (readInt32 & LiteMode.FLAG_ANIMATED_EMOJI_KEYBOARD_NOT_PREMIUM) != 0;
                        this.id = abstractSerializedData2.readInt32(z2);
                        if ((this.flags & LiteMode.FLAG_CHAT_BLUR) != 0) {
                            TLRPC$TL_peerUser tLRPC$TL_peerUser = new TLRPC$TL_peerUser();
                            this.from_id = tLRPC$TL_peerUser;
                            tLRPC$TL_peerUser.user_id = abstractSerializedData2.readInt32(z2);
                        }
                        TLRPC$Peer TLdeserialize = TLRPC$Peer.TLdeserialize(abstractSerializedData2, abstractSerializedData2.readInt32(z2), z2);
                        this.peer_id = TLdeserialize;
                        if (this.from_id == null) {
                            this.from_id = TLdeserialize;
                        }
                        this.date = abstractSerializedData2.readInt32(z2);
                        this.action = TLRPC$MessageAction.TLdeserialize(abstractSerializedData2, abstractSerializedData2.readInt32(z2), z2);
                    }

                    @Override // org.telegram.tgnet.TLRPC$TL_messageService, org.telegram.tgnet.TLObject
                    public void serializeToStream(AbstractSerializedData abstractSerializedData2) {
                        abstractSerializedData2.writeInt32(-1066691065);
                        int i2 = this.unread ? this.flags | 1 : this.flags & (-2);
                        this.flags = i2;
                        int i3 = this.out ? i2 | 2 : i2 & (-3);
                        this.flags = i3;
                        int i4 = this.mentioned ? i3 | 16 : i3 & (-17);
                        this.flags = i4;
                        int i5 = this.media_unread ? i4 | 32 : i4 & (-33);
                        this.flags = i5;
                        int i6 = this.silent ? i5 | LiteMode.FLAG_ANIMATED_EMOJI_REACTIONS_NOT_PREMIUM : i5 & (-8193);
                        this.flags = i6;
                        int i7 = this.post ? i6 | LiteMode.FLAG_ANIMATED_EMOJI_KEYBOARD_NOT_PREMIUM : i6 & (-16385);
                        this.flags = i7;
                        abstractSerializedData2.writeInt32(i7);
                        abstractSerializedData2.writeInt32(this.id);
                        if ((this.flags & LiteMode.FLAG_CHAT_BLUR) != 0) {
                            abstractSerializedData2.writeInt32((int) this.from_id.user_id);
                        }
                        this.peer_id.serializeToStream(abstractSerializedData2);
                        abstractSerializedData2.writeInt32(this.date);
                        this.action.serializeToStream(abstractSerializedData2);
                    }
                };
                break;
            case -1063525281:
                tLRPC$Message = new TLRPC$TL_message() { // from class: org.telegram.tgnet.TLRPC$TL_message_layer68
                    @Override // org.telegram.tgnet.TLRPC$TL_message, org.telegram.tgnet.TLObject
                    public void readParams(AbstractSerializedData abstractSerializedData2, boolean z2) {
                        int readInt32 = abstractSerializedData2.readInt32(z2);
                        this.flags = readInt32;
                        this.unread = (readInt32 & 1) != 0;
                        this.out = (readInt32 & 2) != 0;
                        this.mentioned = (readInt32 & 16) != 0;
                        this.media_unread = (readInt32 & 32) != 0;
                        this.silent = (readInt32 & LiteMode.FLAG_ANIMATED_EMOJI_REACTIONS_NOT_PREMIUM) != 0;
                        this.post = (readInt32 & LiteMode.FLAG_ANIMATED_EMOJI_KEYBOARD_NOT_PREMIUM) != 0;
                        this.with_my_score = (readInt32 & 1073741824) != 0;
                        this.id = abstractSerializedData2.readInt32(z2);
                        if ((this.flags & LiteMode.FLAG_CHAT_BLUR) != 0) {
                            TLRPC$TL_peerUser tLRPC$TL_peerUser = new TLRPC$TL_peerUser();
                            this.from_id = tLRPC$TL_peerUser;
                            tLRPC$TL_peerUser.user_id = abstractSerializedData2.readInt32(z2);
                        }
                        TLRPC$Peer TLdeserialize = TLRPC$Peer.TLdeserialize(abstractSerializedData2, abstractSerializedData2.readInt32(z2), z2);
                        this.peer_id = TLdeserialize;
                        if (this.from_id == null) {
                            this.from_id = TLdeserialize;
                        }
                        if ((this.flags & 4) != 0) {
                            this.fwd_from = TLRPC$MessageFwdHeader.TLdeserialize(abstractSerializedData2, abstractSerializedData2.readInt32(z2), z2);
                        }
                        if ((this.flags & 2048) != 0) {
                            this.via_bot_id = abstractSerializedData2.readInt32(z2);
                        }
                        if ((this.flags & 8) != 0) {
                            TLRPC$TL_messageReplyHeader tLRPC$TL_messageReplyHeader = new TLRPC$TL_messageReplyHeader();
                            this.reply_to = tLRPC$TL_messageReplyHeader;
                            tLRPC$TL_messageReplyHeader.flags |= 16;
                            tLRPC$TL_messageReplyHeader.reply_to_msg_id = abstractSerializedData2.readInt32(z2);
                        }
                        this.date = abstractSerializedData2.readInt32(z2);
                        this.message = abstractSerializedData2.readString(z2);
                        if ((this.flags & LiteMode.FLAG_CALLS_ANIMATIONS) != 0) {
                            TLRPC$MessageMedia TLdeserialize2 = TLRPC$MessageMedia.TLdeserialize(abstractSerializedData2, abstractSerializedData2.readInt32(z2), z2);
                            this.media = TLdeserialize2;
                            if (TLdeserialize2 != null && !TextUtils.isEmpty(TLdeserialize2.captionLegacy)) {
                                this.message = this.media.captionLegacy;
                            }
                        } else {
                            this.media = new TLRPC$TL_messageMediaEmpty();
                        }
                        if ((this.flags & 64) != 0) {
                            this.reply_markup = TLRPC$ReplyMarkup.TLdeserialize(abstractSerializedData2, abstractSerializedData2.readInt32(z2), z2);
                        }
                        if ((this.flags & 128) != 0) {
                            int readInt322 = abstractSerializedData2.readInt32(z2);
                            if (readInt322 != 481674261) {
                                if (z2) {
                                    throw new RuntimeException(String.format("wrong Vector magic, got %x", Integer.valueOf(readInt322)));
                                }
                                return;
                            }
                            int readInt323 = abstractSerializedData2.readInt32(z2);
                            for (int i2 = 0; i2 < readInt323; i2++) {
                                TLRPC$MessageEntity TLdeserialize3 = TLRPC$MessageEntity.TLdeserialize(abstractSerializedData2, abstractSerializedData2.readInt32(z2), z2);
                                if (TLdeserialize3 == null) {
                                    return;
                                }
                                this.entities.add(TLdeserialize3);
                            }
                        }
                        if ((this.flags & 1024) != 0) {
                            this.views = abstractSerializedData2.readInt32(z2);
                        }
                        if ((this.flags & LiteMode.FLAG_CHAT_SCALE) != 0) {
                            this.edit_date = abstractSerializedData2.readInt32(z2);
                        }
                    }

                    @Override // org.telegram.tgnet.TLRPC$TL_message, org.telegram.tgnet.TLObject
                    public void serializeToStream(AbstractSerializedData abstractSerializedData2) {
                        abstractSerializedData2.writeInt32(-1063525281);
                        int i2 = this.unread ? this.flags | 1 : this.flags & (-2);
                        this.flags = i2;
                        int i3 = this.out ? i2 | 2 : i2 & (-3);
                        this.flags = i3;
                        int i4 = this.mentioned ? i3 | 16 : i3 & (-17);
                        this.flags = i4;
                        int i5 = this.media_unread ? i4 | 32 : i4 & (-33);
                        this.flags = i5;
                        int i6 = this.silent ? i5 | LiteMode.FLAG_ANIMATED_EMOJI_REACTIONS_NOT_PREMIUM : i5 & (-8193);
                        this.flags = i6;
                        int i7 = this.post ? i6 | LiteMode.FLAG_ANIMATED_EMOJI_KEYBOARD_NOT_PREMIUM : i6 & (-16385);
                        this.flags = i7;
                        int i8 = this.with_my_score ? i7 | 1073741824 : i7 & (-1073741825);
                        this.flags = i8;
                        abstractSerializedData2.writeInt32(i8);
                        abstractSerializedData2.writeInt32(this.id);
                        if ((this.flags & LiteMode.FLAG_CHAT_BLUR) != 0) {
                            abstractSerializedData2.writeInt32((int) this.from_id.user_id);
                        }
                        this.peer_id.serializeToStream(abstractSerializedData2);
                        if ((this.flags & 4) != 0) {
                            this.fwd_from.serializeToStream(abstractSerializedData2);
                        }
                        if ((this.flags & 2048) != 0) {
                            abstractSerializedData2.writeInt32((int) this.via_bot_id);
                        }
                        if ((this.flags & 8) != 0) {
                            abstractSerializedData2.writeInt32(this.reply_to.reply_to_msg_id);
                        }
                        abstractSerializedData2.writeInt32(this.date);
                        abstractSerializedData2.writeString(this.message);
                        if ((this.flags & LiteMode.FLAG_CALLS_ANIMATIONS) != 0) {
                            this.media.serializeToStream(abstractSerializedData2);
                        }
                        if ((this.flags & 64) != 0) {
                            this.reply_markup.serializeToStream(abstractSerializedData2);
                        }
                        if ((this.flags & 128) != 0) {
                            abstractSerializedData2.writeInt32(481674261);
                            int size = this.entities.size();
                            abstractSerializedData2.writeInt32(size);
                            for (int i9 = 0; i9 < size; i9++) {
                                this.entities.get(i9).serializeToStream(abstractSerializedData2);
                            }
                        }
                        if ((this.flags & 1024) != 0) {
                            abstractSerializedData2.writeInt32(this.views);
                        }
                        if ((this.flags & LiteMode.FLAG_CHAT_SCALE) != 0) {
                            abstractSerializedData2.writeInt32(this.edit_date);
                        }
                        writeAttachPath(abstractSerializedData2);
                    }
                };
                break;
            case -1023016155:
                tLRPC$Message = new TLRPC$TL_message() { // from class: org.telegram.tgnet.TLRPC$TL_message_old4
                    @Override // org.telegram.tgnet.TLRPC$TL_message, org.telegram.tgnet.TLObject
                    public void readParams(AbstractSerializedData abstractSerializedData2, boolean z2) {
                        int readInt32 = abstractSerializedData2.readInt32(z2) | LiteMode.FLAG_CHAT_BLUR | LiteMode.FLAG_CALLS_ANIMATIONS;
                        this.flags = readInt32;
                        this.unread = (readInt32 & 1) != 0;
                        this.out = (readInt32 & 2) != 0;
                        this.mentioned = (readInt32 & 16) != 0;
                        this.media_unread = (readInt32 & 32) != 0;
                        this.id = abstractSerializedData2.readInt32(z2);
                        TLRPC$TL_peerUser tLRPC$TL_peerUser = new TLRPC$TL_peerUser();
                        this.from_id = tLRPC$TL_peerUser;
                        tLRPC$TL_peerUser.user_id = abstractSerializedData2.readInt32(z2);
                        this.peer_id = TLRPC$Peer.TLdeserialize(abstractSerializedData2, abstractSerializedData2.readInt32(z2), z2);
                        if ((this.flags & 4) != 0) {
                            TLRPC$TL_messageFwdHeader tLRPC$TL_messageFwdHeader = new TLRPC$TL_messageFwdHeader();
                            this.fwd_from = tLRPC$TL_messageFwdHeader;
                            tLRPC$TL_messageFwdHeader.from_id = new TLRPC$TL_peerUser();
                            this.fwd_from.from_id.user_id = abstractSerializedData2.readInt32(z2);
                            TLRPC$MessageFwdHeader tLRPC$MessageFwdHeader = this.fwd_from;
                            tLRPC$MessageFwdHeader.flags |= 1;
                            tLRPC$MessageFwdHeader.date = abstractSerializedData2.readInt32(z2);
                        }
                        if ((this.flags & 8) != 0) {
                            TLRPC$TL_messageReplyHeader tLRPC$TL_messageReplyHeader = new TLRPC$TL_messageReplyHeader();
                            this.reply_to = tLRPC$TL_messageReplyHeader;
                            tLRPC$TL_messageReplyHeader.flags |= 16;
                            tLRPC$TL_messageReplyHeader.reply_to_msg_id = abstractSerializedData2.readInt32(z2);
                        }
                        this.date = abstractSerializedData2.readInt32(z2);
                        this.message = abstractSerializedData2.readString(z2);
                        TLRPC$MessageMedia TLdeserialize = TLRPC$MessageMedia.TLdeserialize(abstractSerializedData2, abstractSerializedData2.readInt32(z2), z2);
                        this.media = TLdeserialize;
                        if (TLdeserialize != null && !TextUtils.isEmpty(TLdeserialize.captionLegacy)) {
                            this.message = this.media.captionLegacy;
                        }
                        if ((this.flags & 64) != 0) {
                            this.reply_markup = TLRPC$ReplyMarkup.TLdeserialize(abstractSerializedData2, abstractSerializedData2.readInt32(z2), z2);
                        }
                    }

                    @Override // org.telegram.tgnet.TLRPC$TL_message, org.telegram.tgnet.TLObject
                    public void serializeToStream(AbstractSerializedData abstractSerializedData2) {
                        abstractSerializedData2.writeInt32(-1023016155);
                        int i2 = this.unread ? this.flags | 1 : this.flags & (-2);
                        this.flags = i2;
                        int i3 = this.out ? i2 | 2 : i2 & (-3);
                        this.flags = i3;
                        int i4 = this.mentioned ? i3 | 16 : i3 & (-17);
                        this.flags = i4;
                        int i5 = this.media_unread ? i4 | 32 : i4 & (-33);
                        this.flags = i5;
                        abstractSerializedData2.writeInt32(i5);
                        abstractSerializedData2.writeInt32(this.id);
                        abstractSerializedData2.writeInt32((int) this.from_id.user_id);
                        this.peer_id.serializeToStream(abstractSerializedData2);
                        if ((this.flags & 4) != 0) {
                            abstractSerializedData2.writeInt32((int) this.fwd_from.from_id.user_id);
                            abstractSerializedData2.writeInt32(this.fwd_from.date);
                        }
                        if ((this.flags & 8) != 0) {
                            abstractSerializedData2.writeInt32(this.reply_to.reply_to_msg_id);
                        }
                        abstractSerializedData2.writeInt32(this.date);
                        abstractSerializedData2.writeString(this.message);
                        this.media.serializeToStream(abstractSerializedData2);
                        if ((this.flags & 64) != 0) {
                            this.reply_markup.serializeToStream(abstractSerializedData2);
                        }
                        writeAttachPath(abstractSerializedData2);
                    }
                };
                break;
            case -913120932:
                tLRPC$Message = new TLRPC$TL_message() { // from class: org.telegram.tgnet.TLRPC$TL_message_layer47
                    @Override // org.telegram.tgnet.TLRPC$TL_message, org.telegram.tgnet.TLObject
                    public void readParams(AbstractSerializedData abstractSerializedData2, boolean z2) {
                        int readInt32 = abstractSerializedData2.readInt32(z2);
                        this.flags = readInt32;
                        this.unread = (readInt32 & 1) != 0;
                        this.out = (readInt32 & 2) != 0;
                        this.mentioned = (readInt32 & 16) != 0;
                        this.media_unread = (readInt32 & 32) != 0;
                        this.id = abstractSerializedData2.readInt32(z2);
                        if ((this.flags & LiteMode.FLAG_CHAT_BLUR) != 0) {
                            TLRPC$TL_peerUser tLRPC$TL_peerUser = new TLRPC$TL_peerUser();
                            this.from_id = tLRPC$TL_peerUser;
                            tLRPC$TL_peerUser.user_id = abstractSerializedData2.readInt32(z2);
                        }
                        TLRPC$Peer TLdeserialize = TLRPC$Peer.TLdeserialize(abstractSerializedData2, abstractSerializedData2.readInt32(z2), z2);
                        this.peer_id = TLdeserialize;
                        if (this.from_id == null) {
                            this.from_id = TLdeserialize;
                        }
                        if ((this.flags & 4) != 0) {
                            this.fwd_from = new TLRPC$TL_messageFwdHeader();
                            TLRPC$Peer TLdeserialize2 = TLRPC$Peer.TLdeserialize(abstractSerializedData2, abstractSerializedData2.readInt32(z2), z2);
                            if (TLdeserialize2 != null) {
                                TLRPC$MessageFwdHeader tLRPC$MessageFwdHeader = this.fwd_from;
                                tLRPC$MessageFwdHeader.from_id = TLdeserialize2;
                                tLRPC$MessageFwdHeader.flags |= 1;
                            }
                            this.fwd_from.date = abstractSerializedData2.readInt32(z2);
                        }
                        if ((this.flags & 2048) != 0) {
                            this.via_bot_id = abstractSerializedData2.readInt32(z2);
                        }
                        if ((this.flags & 8) != 0) {
                            TLRPC$TL_messageReplyHeader tLRPC$TL_messageReplyHeader = new TLRPC$TL_messageReplyHeader();
                            this.reply_to = tLRPC$TL_messageReplyHeader;
                            tLRPC$TL_messageReplyHeader.flags |= 16;
                            tLRPC$TL_messageReplyHeader.reply_to_msg_id = abstractSerializedData2.readInt32(z2);
                        }
                        this.date = abstractSerializedData2.readInt32(z2);
                        this.message = abstractSerializedData2.readString(z2);
                        if ((this.flags & LiteMode.FLAG_CALLS_ANIMATIONS) != 0) {
                            TLRPC$MessageMedia TLdeserialize3 = TLRPC$MessageMedia.TLdeserialize(abstractSerializedData2, abstractSerializedData2.readInt32(z2), z2);
                            this.media = TLdeserialize3;
                            if (TLdeserialize3 != null && !TextUtils.isEmpty(TLdeserialize3.captionLegacy)) {
                                this.message = this.media.captionLegacy;
                            }
                        } else {
                            this.media = new TLRPC$TL_messageMediaEmpty();
                        }
                        if ((this.flags & 64) != 0) {
                            this.reply_markup = TLRPC$ReplyMarkup.TLdeserialize(abstractSerializedData2, abstractSerializedData2.readInt32(z2), z2);
                        }
                        if ((this.flags & 128) != 0) {
                            int readInt322 = abstractSerializedData2.readInt32(z2);
                            if (readInt322 != 481674261) {
                                if (z2) {
                                    throw new RuntimeException(String.format("wrong Vector magic, got %x", Integer.valueOf(readInt322)));
                                }
                                return;
                            }
                            int readInt323 = abstractSerializedData2.readInt32(z2);
                            for (int i2 = 0; i2 < readInt323; i2++) {
                                TLRPC$MessageEntity TLdeserialize4 = TLRPC$MessageEntity.TLdeserialize(abstractSerializedData2, abstractSerializedData2.readInt32(z2), z2);
                                if (TLdeserialize4 == null) {
                                    return;
                                }
                                this.entities.add(TLdeserialize4);
                            }
                        }
                        if ((this.flags & 1024) != 0) {
                            this.views = abstractSerializedData2.readInt32(z2);
                        }
                    }

                    @Override // org.telegram.tgnet.TLRPC$TL_message, org.telegram.tgnet.TLObject
                    public void serializeToStream(AbstractSerializedData abstractSerializedData2) {
                        abstractSerializedData2.writeInt32(-913120932);
                        int i2 = this.unread ? this.flags | 1 : this.flags & (-2);
                        this.flags = i2;
                        int i3 = this.out ? i2 | 2 : i2 & (-3);
                        this.flags = i3;
                        int i4 = this.mentioned ? i3 | 16 : i3 & (-17);
                        this.flags = i4;
                        int i5 = this.media_unread ? i4 | 32 : i4 & (-33);
                        this.flags = i5;
                        abstractSerializedData2.writeInt32(i5);
                        abstractSerializedData2.writeInt32(this.id);
                        if ((this.flags & LiteMode.FLAG_CHAT_BLUR) != 0) {
                            abstractSerializedData2.writeInt32((int) this.from_id.user_id);
                        }
                        this.peer_id.serializeToStream(abstractSerializedData2);
                        if ((this.flags & 4) != 0) {
                            TLRPC$Peer tLRPC$Peer = this.fwd_from.from_id;
                            if (tLRPC$Peer != null) {
                                tLRPC$Peer.serializeToStream(abstractSerializedData2);
                            }
                            abstractSerializedData2.writeInt32(this.fwd_from.date);
                        }
                        if ((this.flags & 2048) != 0) {
                            abstractSerializedData2.writeInt32((int) this.via_bot_id);
                        }
                        if ((this.flags & 8) != 0) {
                            abstractSerializedData2.writeInt32(this.reply_to.reply_to_msg_id);
                        }
                        abstractSerializedData2.writeInt32(this.date);
                        abstractSerializedData2.writeString(this.message);
                        if ((this.flags & LiteMode.FLAG_CALLS_ANIMATIONS) != 0) {
                            this.media.serializeToStream(abstractSerializedData2);
                        }
                        if ((this.flags & 64) != 0) {
                            this.reply_markup.serializeToStream(abstractSerializedData2);
                        }
                        if ((this.flags & 128) != 0) {
                            abstractSerializedData2.writeInt32(481674261);
                            int size = this.entities.size();
                            abstractSerializedData2.writeInt32(size);
                            for (int i6 = 0; i6 < size; i6++) {
                                this.entities.get(i6).serializeToStream(abstractSerializedData2);
                            }
                        }
                        if ((this.flags & 1024) != 0) {
                            abstractSerializedData2.writeInt32(this.views);
                        }
                        writeAttachPath(abstractSerializedData2);
                    }
                };
                break;
            case -260565816:
                tLRPC$Message = new TLRPC$TL_message() { // from class: org.telegram.tgnet.TLRPC$TL_message_old5
                    @Override // org.telegram.tgnet.TLRPC$TL_message, org.telegram.tgnet.TLObject
                    public void readParams(AbstractSerializedData abstractSerializedData2, boolean z2) {
                        int readInt32 = abstractSerializedData2.readInt32(z2) | LiteMode.FLAG_CHAT_BLUR | LiteMode.FLAG_CALLS_ANIMATIONS;
                        this.flags = readInt32;
                        this.unread = (readInt32 & 1) != 0;
                        this.out = (readInt32 & 2) != 0;
                        this.mentioned = (readInt32 & 16) != 0;
                        this.media_unread = (readInt32 & 32) != 0;
                        this.id = abstractSerializedData2.readInt32(z2);
                        TLRPC$TL_peerUser tLRPC$TL_peerUser = new TLRPC$TL_peerUser();
                        this.from_id = tLRPC$TL_peerUser;
                        tLRPC$TL_peerUser.user_id = abstractSerializedData2.readInt32(z2);
                        this.peer_id = TLRPC$Peer.TLdeserialize(abstractSerializedData2, abstractSerializedData2.readInt32(z2), z2);
                        if ((this.flags & 4) != 0) {
                            TLRPC$TL_messageFwdHeader tLRPC$TL_messageFwdHeader = new TLRPC$TL_messageFwdHeader();
                            this.fwd_from = tLRPC$TL_messageFwdHeader;
                            tLRPC$TL_messageFwdHeader.from_id = new TLRPC$TL_peerUser();
                            this.fwd_from.from_id.user_id = abstractSerializedData2.readInt32(z2);
                            TLRPC$MessageFwdHeader tLRPC$MessageFwdHeader = this.fwd_from;
                            tLRPC$MessageFwdHeader.flags |= 1;
                            tLRPC$MessageFwdHeader.date = abstractSerializedData2.readInt32(z2);
                        }
                        if ((this.flags & 8) != 0) {
                            TLRPC$TL_messageReplyHeader tLRPC$TL_messageReplyHeader = new TLRPC$TL_messageReplyHeader();
                            this.reply_to = tLRPC$TL_messageReplyHeader;
                            tLRPC$TL_messageReplyHeader.flags |= 16;
                            tLRPC$TL_messageReplyHeader.reply_to_msg_id = abstractSerializedData2.readInt32(z2);
                        }
                        this.date = abstractSerializedData2.readInt32(z2);
                        this.message = abstractSerializedData2.readString(z2);
                        TLRPC$MessageMedia TLdeserialize = TLRPC$MessageMedia.TLdeserialize(abstractSerializedData2, abstractSerializedData2.readInt32(z2), z2);
                        this.media = TLdeserialize;
                        if (TLdeserialize != null && !TextUtils.isEmpty(TLdeserialize.captionLegacy)) {
                            this.message = this.media.captionLegacy;
                        }
                        if ((this.flags & 64) != 0) {
                            this.reply_markup = TLRPC$ReplyMarkup.TLdeserialize(abstractSerializedData2, abstractSerializedData2.readInt32(z2), z2);
                        }
                        if ((this.flags & 128) != 0) {
                            int readInt322 = abstractSerializedData2.readInt32(z2);
                            if (readInt322 != 481674261) {
                                if (z2) {
                                    throw new RuntimeException(String.format("wrong Vector magic, got %x", Integer.valueOf(readInt322)));
                                }
                                return;
                            }
                            int readInt323 = abstractSerializedData2.readInt32(z2);
                            for (int i2 = 0; i2 < readInt323; i2++) {
                                TLRPC$MessageEntity TLdeserialize2 = TLRPC$MessageEntity.TLdeserialize(abstractSerializedData2, abstractSerializedData2.readInt32(z2), z2);
                                if (TLdeserialize2 == null) {
                                    return;
                                }
                                this.entities.add(TLdeserialize2);
                            }
                        }
                    }

                    @Override // org.telegram.tgnet.TLRPC$TL_message, org.telegram.tgnet.TLObject
                    public void serializeToStream(AbstractSerializedData abstractSerializedData2) {
                        abstractSerializedData2.writeInt32(-260565816);
                        int i2 = this.unread ? this.flags | 1 : this.flags & (-2);
                        this.flags = i2;
                        int i3 = this.out ? i2 | 2 : i2 & (-3);
                        this.flags = i3;
                        int i4 = this.mentioned ? i3 | 16 : i3 & (-17);
                        this.flags = i4;
                        int i5 = this.media_unread ? i4 | 32 : i4 & (-33);
                        this.flags = i5;
                        abstractSerializedData2.writeInt32(i5);
                        abstractSerializedData2.writeInt32(this.id);
                        abstractSerializedData2.writeInt32((int) this.from_id.user_id);
                        this.peer_id.serializeToStream(abstractSerializedData2);
                        if ((this.flags & 4) != 0) {
                            abstractSerializedData2.writeInt32((int) this.fwd_from.from_id.user_id);
                            abstractSerializedData2.writeInt32(this.fwd_from.date);
                        }
                        if ((this.flags & 8) != 0) {
                            abstractSerializedData2.writeInt32(this.reply_to.reply_to_msg_id);
                        }
                        abstractSerializedData2.writeInt32(this.date);
                        abstractSerializedData2.writeString(this.message);
                        this.media.serializeToStream(abstractSerializedData2);
                        if ((this.flags & 64) != 0) {
                            this.reply_markup.serializeToStream(abstractSerializedData2);
                        }
                        if ((this.flags & 128) != 0) {
                            abstractSerializedData2.writeInt32(481674261);
                            int size = this.entities.size();
                            abstractSerializedData2.writeInt32(size);
                            for (int i6 = 0; i6 < size; i6++) {
                                this.entities.get(i6).serializeToStream(abstractSerializedData2);
                            }
                        }
                        writeAttachPath(abstractSerializedData2);
                    }
                };
                break;
            case -181507201:
                tLRPC$Message = new TLRPC$TL_message() { // from class: org.telegram.tgnet.TLRPC$TL_message_layer118
                    @Override // org.telegram.tgnet.TLRPC$TL_message, org.telegram.tgnet.TLObject
                    public void readParams(AbstractSerializedData abstractSerializedData2, boolean z2) {
                        int readInt32 = abstractSerializedData2.readInt32(z2);
                        this.flags = readInt32;
                        this.out = (readInt32 & 2) != 0;
                        this.mentioned = (readInt32 & 16) != 0;
                        this.media_unread = (readInt32 & 32) != 0;
                        this.silent = (readInt32 & LiteMode.FLAG_ANIMATED_EMOJI_REACTIONS_NOT_PREMIUM) != 0;
                        this.post = (readInt32 & LiteMode.FLAG_ANIMATED_EMOJI_KEYBOARD_NOT_PREMIUM) != 0;
                        this.from_scheduled = (262144 & readInt32) != 0;
                        this.legacy = (524288 & readInt32) != 0;
                        this.edit_hide = (readInt32 & 2097152) != 0;
                        this.id = abstractSerializedData2.readInt32(z2);
                        if ((this.flags & LiteMode.FLAG_CHAT_BLUR) != 0) {
                            TLRPC$TL_peerUser tLRPC$TL_peerUser = new TLRPC$TL_peerUser();
                            this.from_id = tLRPC$TL_peerUser;
                            tLRPC$TL_peerUser.user_id = abstractSerializedData2.readInt32(z2);
                        }
                        this.peer_id = TLRPC$Peer.TLdeserialize(abstractSerializedData2, abstractSerializedData2.readInt32(z2), z2);
                        if ((this.flags & 4) != 0) {
                            this.fwd_from = TLRPC$MessageFwdHeader.TLdeserialize(abstractSerializedData2, abstractSerializedData2.readInt32(z2), z2);
                        }
                        if ((this.flags & 2048) != 0) {
                            this.via_bot_id = abstractSerializedData2.readInt32(z2);
                        }
                        if ((this.flags & 8) != 0) {
                            TLRPC$TL_messageReplyHeader tLRPC$TL_messageReplyHeader = new TLRPC$TL_messageReplyHeader();
                            this.reply_to = tLRPC$TL_messageReplyHeader;
                            tLRPC$TL_messageReplyHeader.flags |= 16;
                            tLRPC$TL_messageReplyHeader.reply_to_msg_id = abstractSerializedData2.readInt32(z2);
                        }
                        this.date = abstractSerializedData2.readInt32(z2);
                        this.message = abstractSerializedData2.readString(z2);
                        if ((this.flags & LiteMode.FLAG_CALLS_ANIMATIONS) != 0) {
                            TLRPC$MessageMedia TLdeserialize = TLRPC$MessageMedia.TLdeserialize(abstractSerializedData2, abstractSerializedData2.readInt32(z2), z2);
                            this.media = TLdeserialize;
                            if (TLdeserialize != null) {
                                this.ttl = TLdeserialize.ttl_seconds;
                            }
                            if (TLdeserialize != null && !TextUtils.isEmpty(TLdeserialize.captionLegacy)) {
                                this.message = this.media.captionLegacy;
                            }
                        }
                        if ((this.flags & 64) != 0) {
                            this.reply_markup = TLRPC$ReplyMarkup.TLdeserialize(abstractSerializedData2, abstractSerializedData2.readInt32(z2), z2);
                        }
                        if ((this.flags & 128) != 0) {
                            int readInt322 = abstractSerializedData2.readInt32(z2);
                            if (readInt322 != 481674261) {
                                if (z2) {
                                    throw new RuntimeException(String.format("wrong Vector magic, got %x", Integer.valueOf(readInt322)));
                                }
                                return;
                            }
                            int readInt323 = abstractSerializedData2.readInt32(z2);
                            for (int i2 = 0; i2 < readInt323; i2++) {
                                TLRPC$MessageEntity TLdeserialize2 = TLRPC$MessageEntity.TLdeserialize(abstractSerializedData2, abstractSerializedData2.readInt32(z2), z2);
                                if (TLdeserialize2 == null) {
                                    return;
                                }
                                this.entities.add(TLdeserialize2);
                            }
                        }
                        if ((this.flags & 1024) != 0) {
                            this.views = abstractSerializedData2.readInt32(z2);
                        }
                        if ((this.flags & 1024) != 0) {
                            this.forwards = abstractSerializedData2.readInt32(z2);
                        }
                        if ((this.flags & LiteMode.FLAG_CHAT_SCALE) != 0) {
                            this.edit_date = abstractSerializedData2.readInt32(z2);
                        }
                        if ((this.flags & 65536) != 0) {
                            this.post_author = abstractSerializedData2.readString(z2);
                        }
                        if ((this.flags & 131072) != 0) {
                            this.grouped_id = abstractSerializedData2.readInt64(z2);
                        }
                        if ((this.flags & 4194304) != 0) {
                            int readInt324 = abstractSerializedData2.readInt32(z2);
                            if (readInt324 != 481674261) {
                                if (z2) {
                                    throw new RuntimeException(String.format("wrong Vector magic, got %x", Integer.valueOf(readInt324)));
                                }
                                return;
                            }
                            int readInt325 = abstractSerializedData2.readInt32(z2);
                            for (int i3 = 0; i3 < readInt325; i3++) {
                                TLRPC$TL_restrictionReason TLdeserialize3 = TLRPC$TL_restrictionReason.TLdeserialize(abstractSerializedData2, abstractSerializedData2.readInt32(z2), z2);
                                if (TLdeserialize3 == null) {
                                    return;
                                }
                                this.restriction_reason.add(TLdeserialize3);
                            }
                        }
                    }

                    @Override // org.telegram.tgnet.TLRPC$TL_message, org.telegram.tgnet.TLObject
                    public void serializeToStream(AbstractSerializedData abstractSerializedData2) {
                        abstractSerializedData2.writeInt32(-181507201);
                        int i2 = this.out ? this.flags | 2 : this.flags & (-3);
                        this.flags = i2;
                        int i3 = this.mentioned ? i2 | 16 : i2 & (-17);
                        this.flags = i3;
                        int i4 = this.media_unread ? i3 | 32 : i3 & (-33);
                        this.flags = i4;
                        int i5 = this.silent ? i4 | LiteMode.FLAG_ANIMATED_EMOJI_REACTIONS_NOT_PREMIUM : i4 & (-8193);
                        this.flags = i5;
                        int i6 = this.post ? i5 | LiteMode.FLAG_ANIMATED_EMOJI_KEYBOARD_NOT_PREMIUM : i5 & (-16385);
                        this.flags = i6;
                        int i7 = this.from_scheduled ? i6 | 262144 : i6 & (-262145);
                        this.flags = i7;
                        int i8 = this.legacy ? i7 | 524288 : i7 & (-524289);
                        this.flags = i8;
                        int i9 = this.edit_hide ? i8 | 2097152 : i8 & (-2097153);
                        this.flags = i9;
                        abstractSerializedData2.writeInt32(i9);
                        abstractSerializedData2.writeInt32(this.id);
                        if ((this.flags & LiteMode.FLAG_CHAT_BLUR) != 0) {
                            abstractSerializedData2.writeInt32((int) this.from_id.user_id);
                        }
                        this.peer_id.serializeToStream(abstractSerializedData2);
                        if ((this.flags & 4) != 0) {
                            this.fwd_from.serializeToStream(abstractSerializedData2);
                        }
                        if ((this.flags & 2048) != 0) {
                            abstractSerializedData2.writeInt32((int) this.via_bot_id);
                        }
                        if ((this.flags & 8) != 0) {
                            abstractSerializedData2.writeInt32(this.reply_to.reply_to_msg_id);
                        }
                        abstractSerializedData2.writeInt32(this.date);
                        abstractSerializedData2.writeString(this.message);
                        if ((this.flags & LiteMode.FLAG_CALLS_ANIMATIONS) != 0) {
                            this.media.serializeToStream(abstractSerializedData2);
                        }
                        if ((this.flags & 64) != 0) {
                            this.reply_markup.serializeToStream(abstractSerializedData2);
                        }
                        if ((this.flags & 128) != 0) {
                            abstractSerializedData2.writeInt32(481674261);
                            int size = this.entities.size();
                            abstractSerializedData2.writeInt32(size);
                            for (int i10 = 0; i10 < size; i10++) {
                                this.entities.get(i10).serializeToStream(abstractSerializedData2);
                            }
                        }
                        if ((this.flags & 1024) != 0) {
                            abstractSerializedData2.writeInt32(this.views);
                        }
                        if ((this.flags & 1024) != 0) {
                            abstractSerializedData2.writeInt32(this.forwards);
                        }
                        if ((this.flags & LiteMode.FLAG_CHAT_SCALE) != 0) {
                            abstractSerializedData2.writeInt32(this.edit_date);
                        }
                        if ((this.flags & 65536) != 0) {
                            abstractSerializedData2.writeString(this.post_author);
                        }
                        if ((this.flags & 131072) != 0) {
                            abstractSerializedData2.writeInt64(this.grouped_id);
                        }
                        if ((this.flags & 4194304) != 0) {
                            abstractSerializedData2.writeInt32(481674261);
                            int size2 = this.restriction_reason.size();
                            abstractSerializedData2.writeInt32(size2);
                            for (int i11 = 0; i11 < size2; i11++) {
                                this.restriction_reason.get(i11).serializeToStream(abstractSerializedData2);
                            }
                        }
                        writeAttachPath(abstractSerializedData2);
                    }
                };
                break;
            case 99903492:
                tLRPC$Message = new TLRPC$TL_messageForwarded_old2() { // from class: org.telegram.tgnet.TLRPC$TL_messageForwarded_old
                    @Override // org.telegram.tgnet.TLRPC$TL_messageForwarded_old2, org.telegram.tgnet.TLObject
                    public void readParams(AbstractSerializedData abstractSerializedData2, boolean z2) {
                        this.id = abstractSerializedData2.readInt32(z2);
                        TLRPC$TL_messageFwdHeader tLRPC$TL_messageFwdHeader = new TLRPC$TL_messageFwdHeader();
                        this.fwd_from = tLRPC$TL_messageFwdHeader;
                        tLRPC$TL_messageFwdHeader.from_id = new TLRPC$TL_peerUser();
                        this.fwd_from.from_id.user_id = abstractSerializedData2.readInt32(z2);
                        TLRPC$MessageFwdHeader tLRPC$MessageFwdHeader = this.fwd_from;
                        tLRPC$MessageFwdHeader.flags |= 1;
                        tLRPC$MessageFwdHeader.date = abstractSerializedData2.readInt32(z2);
                        TLRPC$TL_peerUser tLRPC$TL_peerUser = new TLRPC$TL_peerUser();
                        this.from_id = tLRPC$TL_peerUser;
                        tLRPC$TL_peerUser.user_id = abstractSerializedData2.readInt32(z2);
                        this.peer_id = TLRPC$Peer.TLdeserialize(abstractSerializedData2, abstractSerializedData2.readInt32(z2), z2);
                        this.out = abstractSerializedData2.readBool(z2);
                        this.unread = abstractSerializedData2.readBool(z2);
                        this.flags |= 772;
                        this.date = abstractSerializedData2.readInt32(z2);
                        this.message = abstractSerializedData2.readString(z2);
                        TLRPC$MessageMedia TLdeserialize = TLRPC$MessageMedia.TLdeserialize(abstractSerializedData2, abstractSerializedData2.readInt32(z2), z2);
                        this.media = TLdeserialize;
                        if (TLdeserialize == null || TextUtils.isEmpty(TLdeserialize.captionLegacy)) {
                            return;
                        }
                        this.message = this.media.captionLegacy;
                    }

                    @Override // org.telegram.tgnet.TLRPC$TL_messageForwarded_old2, org.telegram.tgnet.TLObject
                    public void serializeToStream(AbstractSerializedData abstractSerializedData2) {
                        abstractSerializedData2.writeInt32(99903492);
                        abstractSerializedData2.writeInt32(this.id);
                        abstractSerializedData2.writeInt32((int) this.fwd_from.from_id.user_id);
                        abstractSerializedData2.writeInt32(this.fwd_from.date);
                        abstractSerializedData2.writeInt32((int) this.from_id.user_id);
                        this.peer_id.serializeToStream(abstractSerializedData2);
                        abstractSerializedData2.writeBool(this.out);
                        abstractSerializedData2.writeBool(this.unread);
                        abstractSerializedData2.writeInt32(this.date);
                        abstractSerializedData2.writeString(this.message);
                        this.media.serializeToStream(abstractSerializedData2);
                        writeAttachPath(abstractSerializedData2);
                    }
                };
                break;
            case 479924263:
                tLRPC$Message = new TLRPC$TL_message() { // from class: org.telegram.tgnet.TLRPC$TL_message_layer104_2
                    @Override // org.telegram.tgnet.TLRPC$TL_message, org.telegram.tgnet.TLObject
                    public void readParams(AbstractSerializedData abstractSerializedData2, boolean z2) {
                        int readInt32 = abstractSerializedData2.readInt32(z2);
                        this.flags = readInt32;
                        this.out = (readInt32 & 2) != 0;
                        this.mentioned = (readInt32 & 16) != 0;
                        this.media_unread = (readInt32 & 32) != 0;
                        this.silent = (readInt32 & LiteMode.FLAG_ANIMATED_EMOJI_REACTIONS_NOT_PREMIUM) != 0;
                        this.post = (readInt32 & LiteMode.FLAG_ANIMATED_EMOJI_KEYBOARD_NOT_PREMIUM) != 0;
                        this.from_scheduled = (262144 & readInt32) != 0;
                        this.legacy = (524288 & readInt32) != 0;
                        this.edit_hide = (readInt32 & 2097152) != 0;
                        this.id = abstractSerializedData2.readInt32(z2);
                        if ((this.flags & LiteMode.FLAG_CHAT_BLUR) != 0) {
                            TLRPC$TL_peerUser tLRPC$TL_peerUser = new TLRPC$TL_peerUser();
                            this.from_id = tLRPC$TL_peerUser;
                            tLRPC$TL_peerUser.user_id = abstractSerializedData2.readInt32(z2);
                        }
                        this.peer_id = TLRPC$Peer.TLdeserialize(abstractSerializedData2, abstractSerializedData2.readInt32(z2), z2);
                        if ((this.flags & 4) != 0) {
                            this.fwd_from = TLRPC$MessageFwdHeader.TLdeserialize(abstractSerializedData2, abstractSerializedData2.readInt32(z2), z2);
                        }
                        if ((this.flags & 2048) != 0) {
                            this.via_bot_id = abstractSerializedData2.readInt32(z2);
                        }
                        if ((this.flags & 8) != 0) {
                            TLRPC$TL_messageReplyHeader tLRPC$TL_messageReplyHeader = new TLRPC$TL_messageReplyHeader();
                            this.reply_to = tLRPC$TL_messageReplyHeader;
                            tLRPC$TL_messageReplyHeader.flags |= 16;
                            tLRPC$TL_messageReplyHeader.reply_to_msg_id = abstractSerializedData2.readInt32(z2);
                        }
                        this.date = abstractSerializedData2.readInt32(z2);
                        this.message = abstractSerializedData2.readString(z2);
                        if ((this.flags & LiteMode.FLAG_CALLS_ANIMATIONS) != 0) {
                            TLRPC$MessageMedia TLdeserialize = TLRPC$MessageMedia.TLdeserialize(abstractSerializedData2, abstractSerializedData2.readInt32(z2), z2);
                            this.media = TLdeserialize;
                            if (TLdeserialize != null) {
                                this.ttl = TLdeserialize.ttl_seconds;
                            }
                            if (TLdeserialize != null && !TextUtils.isEmpty(TLdeserialize.captionLegacy)) {
                                this.message = this.media.captionLegacy;
                            }
                        }
                        if ((this.flags & 64) != 0) {
                            this.reply_markup = TLRPC$ReplyMarkup.TLdeserialize(abstractSerializedData2, abstractSerializedData2.readInt32(z2), z2);
                        }
                        if ((this.flags & 128) != 0) {
                            int readInt322 = abstractSerializedData2.readInt32(z2);
                            if (readInt322 != 481674261) {
                                if (z2) {
                                    throw new RuntimeException(String.format("wrong Vector magic, got %x", Integer.valueOf(readInt322)));
                                }
                                return;
                            }
                            int readInt323 = abstractSerializedData2.readInt32(z2);
                            for (int i2 = 0; i2 < readInt323; i2++) {
                                TLRPC$MessageEntity TLdeserialize2 = TLRPC$MessageEntity.TLdeserialize(abstractSerializedData2, abstractSerializedData2.readInt32(z2), z2);
                                if (TLdeserialize2 == null) {
                                    return;
                                }
                                this.entities.add(TLdeserialize2);
                            }
                        }
                        if ((this.flags & 1024) != 0) {
                            this.views = abstractSerializedData2.readInt32(z2);
                        }
                        if ((this.flags & LiteMode.FLAG_CHAT_SCALE) != 0) {
                            this.edit_date = abstractSerializedData2.readInt32(z2);
                        }
                        if ((this.flags & 65536) != 0) {
                            this.post_author = abstractSerializedData2.readString(z2);
                        }
                        if ((this.flags & 131072) != 0) {
                            this.grouped_id = abstractSerializedData2.readInt64(z2);
                        }
                        if ((this.flags & FileLoaderPriorityQueue.PRIORITY_VALUE_MAX) != 0) {
                            this.reactions = TLRPC$MessageReactions.TLdeserialize(abstractSerializedData2, abstractSerializedData2.readInt32(z2), z2);
                        }
                        if ((this.flags & 4194304) != 0) {
                            abstractSerializedData2.readString(z2);
                        }
                    }

                    @Override // org.telegram.tgnet.TLRPC$TL_message, org.telegram.tgnet.TLObject
                    public void serializeToStream(AbstractSerializedData abstractSerializedData2) {
                        abstractSerializedData2.writeInt32(479924263);
                        int i2 = this.out ? this.flags | 2 : this.flags & (-3);
                        this.flags = i2;
                        int i3 = this.mentioned ? i2 | 16 : i2 & (-17);
                        this.flags = i3;
                        int i4 = this.media_unread ? i3 | 32 : i3 & (-33);
                        this.flags = i4;
                        int i5 = this.silent ? i4 | LiteMode.FLAG_ANIMATED_EMOJI_REACTIONS_NOT_PREMIUM : i4 & (-8193);
                        this.flags = i5;
                        int i6 = this.post ? i5 | LiteMode.FLAG_ANIMATED_EMOJI_KEYBOARD_NOT_PREMIUM : i5 & (-16385);
                        this.flags = i6;
                        int i7 = this.from_scheduled ? i6 | 262144 : i6 & (-262145);
                        this.flags = i7;
                        int i8 = this.legacy ? i7 | 524288 : i7 & (-524289);
                        this.flags = i8;
                        int i9 = this.edit_hide ? i8 | 2097152 : i8 & (-2097153);
                        this.flags = i9;
                        abstractSerializedData2.writeInt32(i9);
                        abstractSerializedData2.writeInt32(this.id);
                        if ((this.flags & LiteMode.FLAG_CHAT_BLUR) != 0) {
                            abstractSerializedData2.writeInt32((int) this.from_id.user_id);
                        }
                        this.peer_id.serializeToStream(abstractSerializedData2);
                        if ((this.flags & 4) != 0) {
                            this.fwd_from.serializeToStream(abstractSerializedData2);
                        }
                        if ((this.flags & 2048) != 0) {
                            abstractSerializedData2.writeInt32((int) this.via_bot_id);
                        }
                        if ((this.flags & 8) != 0) {
                            abstractSerializedData2.writeInt32(this.reply_to.reply_to_msg_id);
                        }
                        abstractSerializedData2.writeInt32(this.date);
                        abstractSerializedData2.writeString(this.message);
                        if ((this.flags & LiteMode.FLAG_CALLS_ANIMATIONS) != 0) {
                            this.media.serializeToStream(abstractSerializedData2);
                        }
                        if ((this.flags & 64) != 0) {
                            this.reply_markup.serializeToStream(abstractSerializedData2);
                        }
                        if ((this.flags & 128) != 0) {
                            abstractSerializedData2.writeInt32(481674261);
                            int size = this.entities.size();
                            abstractSerializedData2.writeInt32(size);
                            for (int i10 = 0; i10 < size; i10++) {
                                this.entities.get(i10).serializeToStream(abstractSerializedData2);
                            }
                        }
                        if ((this.flags & 1024) != 0) {
                            abstractSerializedData2.writeInt32(this.views);
                        }
                        if ((this.flags & LiteMode.FLAG_CHAT_SCALE) != 0) {
                            abstractSerializedData2.writeInt32(this.edit_date);
                        }
                        if ((this.flags & 65536) != 0) {
                            abstractSerializedData2.writeString(this.post_author);
                        }
                        if ((this.flags & 131072) != 0) {
                            abstractSerializedData2.writeInt64(this.grouped_id);
                        }
                        if ((this.flags & FileLoaderPriorityQueue.PRIORITY_VALUE_MAX) != 0) {
                            this.reactions.serializeToStream(abstractSerializedData2);
                        }
                        if ((this.flags & 4194304) != 0) {
                            abstractSerializedData2.writeString("");
                        }
                        writeAttachPath(abstractSerializedData2);
                    }
                };
                break;
            case 495384334:
                tLRPC$Message = new TLRPC$TL_messageService() { // from class: org.telegram.tgnet.TLRPC$TL_messageService_old2
                    @Override // org.telegram.tgnet.TLRPC$TL_messageService, org.telegram.tgnet.TLObject
                    public void readParams(AbstractSerializedData abstractSerializedData2, boolean z2) {
                        int readInt32 = abstractSerializedData2.readInt32(z2);
                        this.flags = readInt32;
                        this.unread = (readInt32 & 1) != 0;
                        this.out = (readInt32 & 2) != 0;
                        this.mentioned = (readInt32 & 16) != 0;
                        this.media_unread = (readInt32 & 32) != 0;
                        this.id = abstractSerializedData2.readInt32(z2);
                        TLRPC$TL_peerUser tLRPC$TL_peerUser = new TLRPC$TL_peerUser();
                        this.from_id = tLRPC$TL_peerUser;
                        tLRPC$TL_peerUser.user_id = abstractSerializedData2.readInt32(z2);
                        this.peer_id = TLRPC$Peer.TLdeserialize(abstractSerializedData2, abstractSerializedData2.readInt32(z2), z2);
                        this.date = abstractSerializedData2.readInt32(z2);
                        this.action = TLRPC$MessageAction.TLdeserialize(abstractSerializedData2, abstractSerializedData2.readInt32(z2), z2);
                        this.flags |= LiteMode.FLAG_CHAT_BLUR;
                    }

                    @Override // org.telegram.tgnet.TLRPC$TL_messageService, org.telegram.tgnet.TLObject
                    public void serializeToStream(AbstractSerializedData abstractSerializedData2) {
                        abstractSerializedData2.writeInt32(495384334);
                        int i2 = this.unread ? this.flags | 1 : this.flags & (-2);
                        this.flags = i2;
                        int i3 = this.out ? i2 | 2 : i2 & (-3);
                        this.flags = i3;
                        int i4 = this.mentioned ? i3 | 16 : i3 & (-17);
                        this.flags = i4;
                        int i5 = this.media_unread ? i4 | 32 : i4 & (-33);
                        this.flags = i5;
                        abstractSerializedData2.writeInt32(i5);
                        abstractSerializedData2.writeInt32(this.id);
                        abstractSerializedData2.writeInt32((int) this.from_id.user_id);
                        this.peer_id.serializeToStream(abstractSerializedData2);
                        abstractSerializedData2.writeInt32(this.date);
                        this.action.serializeToStream(abstractSerializedData2);
                    }
                };
                break;
            case 508332649:
                tLRPC$Message = new TLRPC$TL_message() { // from class: org.telegram.tgnet.TLRPC$TL_message_layer175
                    @Override // org.telegram.tgnet.TLRPC$TL_message, org.telegram.tgnet.TLObject
                    public void readParams(AbstractSerializedData abstractSerializedData2, boolean z2) {
                        int readInt32 = abstractSerializedData2.readInt32(z2);
                        this.flags = readInt32;
                        this.out = (readInt32 & 2) != 0;
                        this.mentioned = (readInt32 & 16) != 0;
                        this.media_unread = (readInt32 & 32) != 0;
                        this.silent = (readInt32 & LiteMode.FLAG_ANIMATED_EMOJI_REACTIONS_NOT_PREMIUM) != 0;
                        this.post = (readInt32 & LiteMode.FLAG_ANIMATED_EMOJI_KEYBOARD_NOT_PREMIUM) != 0;
                        this.from_scheduled = (262144 & readInt32) != 0;
                        this.legacy = (524288 & readInt32) != 0;
                        this.edit_hide = (2097152 & readInt32) != 0;
                        this.pinned = (16777216 & readInt32) != 0;
                        this.noforwards = (67108864 & readInt32) != 0;
                        this.invert_media = (readInt32 & 134217728) != 0;
                        this.id = abstractSerializedData2.readInt32(z2);
                        if ((this.flags & LiteMode.FLAG_CHAT_BLUR) != 0) {
                            this.from_id = TLRPC$Peer.TLdeserialize(abstractSerializedData2, abstractSerializedData2.readInt32(z2), z2);
                        }
                        if ((this.flags & 536870912) != 0) {
                            this.from_boosts_applied = abstractSerializedData2.readInt32(z2);
                        }
                        this.peer_id = TLRPC$Peer.TLdeserialize(abstractSerializedData2, abstractSerializedData2.readInt32(z2), z2);
                        if ((this.flags & 268435456) != 0) {
                            this.saved_peer_id = TLRPC$Peer.TLdeserialize(abstractSerializedData2, abstractSerializedData2.readInt32(z2), z2);
                        }
                        if ((this.flags & 4) != 0) {
                            this.fwd_from = TLRPC$MessageFwdHeader.TLdeserialize(abstractSerializedData2, abstractSerializedData2.readInt32(z2), z2);
                        }
                        if ((this.flags & 2048) != 0) {
                            this.via_bot_id = abstractSerializedData2.readInt64(z2);
                        }
                        if ((this.flags & 8) != 0) {
                            this.reply_to = TLRPC$MessageReplyHeader.TLdeserialize(abstractSerializedData2, abstractSerializedData2.readInt32(z2), z2);
                        }
                        this.date = abstractSerializedData2.readInt32(z2);
                        this.message = abstractSerializedData2.readString(z2);
                        if ((this.flags & LiteMode.FLAG_CALLS_ANIMATIONS) != 0) {
                            TLRPC$MessageMedia TLdeserialize = TLRPC$MessageMedia.TLdeserialize(abstractSerializedData2, abstractSerializedData2.readInt32(z2), z2);
                            this.media = TLdeserialize;
                            if (TLdeserialize != null) {
                                this.ttl = TLdeserialize.ttl_seconds;
                            }
                            if (TLdeserialize != null && !TextUtils.isEmpty(TLdeserialize.captionLegacy)) {
                                this.message = this.media.captionLegacy;
                            }
                        }
                        if ((this.flags & 64) != 0) {
                            this.reply_markup = TLRPC$ReplyMarkup.TLdeserialize(abstractSerializedData2, abstractSerializedData2.readInt32(z2), z2);
                        }
                        if ((this.flags & 128) != 0) {
                            int readInt322 = abstractSerializedData2.readInt32(z2);
                            if (readInt322 != 481674261) {
                                if (z2) {
                                    throw new RuntimeException(String.format("wrong Vector magic, got %x", Integer.valueOf(readInt322)));
                                }
                                return;
                            }
                            int readInt323 = abstractSerializedData2.readInt32(z2);
                            for (int i2 = 0; i2 < readInt323; i2++) {
                                TLRPC$MessageEntity TLdeserialize2 = TLRPC$MessageEntity.TLdeserialize(abstractSerializedData2, abstractSerializedData2.readInt32(z2), z2);
                                if (TLdeserialize2 == null) {
                                    return;
                                }
                                this.entities.add(TLdeserialize2);
                            }
                        }
                        if ((this.flags & 1024) != 0) {
                            this.views = abstractSerializedData2.readInt32(z2);
                        }
                        if ((this.flags & 1024) != 0) {
                            this.forwards = abstractSerializedData2.readInt32(z2);
                        }
                        if ((this.flags & 8388608) != 0) {
                            this.replies = TLRPC$MessageReplies.TLdeserialize(abstractSerializedData2, abstractSerializedData2.readInt32(z2), z2);
                        }
                        if ((this.flags & LiteMode.FLAG_CHAT_SCALE) != 0) {
                            this.edit_date = abstractSerializedData2.readInt32(z2);
                        }
                        if ((this.flags & 65536) != 0) {
                            this.post_author = abstractSerializedData2.readString(z2);
                        }
                        if ((this.flags & 131072) != 0) {
                            this.grouped_id = abstractSerializedData2.readInt64(z2);
                        }
                        if ((this.flags & FileLoaderPriorityQueue.PRIORITY_VALUE_MAX) != 0) {
                            this.reactions = TLRPC$MessageReactions.TLdeserialize(abstractSerializedData2, abstractSerializedData2.readInt32(z2), z2);
                        }
                        if ((this.flags & 4194304) != 0) {
                            int readInt324 = abstractSerializedData2.readInt32(z2);
                            if (readInt324 != 481674261) {
                                if (z2) {
                                    throw new RuntimeException(String.format("wrong Vector magic, got %x", Integer.valueOf(readInt324)));
                                }
                                return;
                            }
                            int readInt325 = abstractSerializedData2.readInt32(z2);
                            for (int i3 = 0; i3 < readInt325; i3++) {
                                TLRPC$TL_restrictionReason TLdeserialize3 = TLRPC$TL_restrictionReason.TLdeserialize(abstractSerializedData2, abstractSerializedData2.readInt32(z2), z2);
                                if (TLdeserialize3 == null) {
                                    return;
                                }
                                this.restriction_reason.add(TLdeserialize3);
                            }
                        }
                        if ((this.flags & ConnectionsManager.FileTypeVideo) != 0) {
                            this.ttl_period = abstractSerializedData2.readInt32(z2);
                        }
                    }

                    @Override // org.telegram.tgnet.TLRPC$TL_message, org.telegram.tgnet.TLObject
                    public void serializeToStream(AbstractSerializedData abstractSerializedData2) {
                        abstractSerializedData2.writeInt32(508332649);
                        int i2 = this.out ? this.flags | 2 : this.flags & (-3);
                        this.flags = i2;
                        int i3 = this.mentioned ? i2 | 16 : i2 & (-17);
                        this.flags = i3;
                        int i4 = this.media_unread ? i3 | 32 : i3 & (-33);
                        this.flags = i4;
                        int i5 = this.silent ? i4 | LiteMode.FLAG_ANIMATED_EMOJI_REACTIONS_NOT_PREMIUM : i4 & (-8193);
                        this.flags = i5;
                        int i6 = this.post ? i5 | LiteMode.FLAG_ANIMATED_EMOJI_KEYBOARD_NOT_PREMIUM : i5 & (-16385);
                        this.flags = i6;
                        int i7 = this.from_scheduled ? i6 | 262144 : i6 & (-262145);
                        this.flags = i7;
                        int i8 = this.legacy ? i7 | 524288 : i7 & (-524289);
                        this.flags = i8;
                        int i9 = this.edit_hide ? i8 | 2097152 : i8 & (-2097153);
                        this.flags = i9;
                        int i10 = this.pinned ? i9 | ConnectionsManager.FileTypePhoto : i9 & (-16777217);
                        this.flags = i10;
                        int i11 = this.noforwards ? i10 | ConnectionsManager.FileTypeFile : i10 & (-67108865);
                        this.flags = i11;
                        int i12 = this.invert_media ? i11 | 134217728 : i11 & (-134217729);
                        this.flags = i12;
                        abstractSerializedData2.writeInt32(i12);
                        abstractSerializedData2.writeInt32(this.id);
                        if ((this.flags & LiteMode.FLAG_CHAT_BLUR) != 0) {
                            this.from_id.serializeToStream(abstractSerializedData2);
                        }
                        if ((this.flags & 536870912) != 0) {
                            abstractSerializedData2.writeInt32(this.from_boosts_applied);
                        }
                        this.peer_id.serializeToStream(abstractSerializedData2);
                        if ((this.flags & 268435456) != 0) {
                            this.saved_peer_id.serializeToStream(abstractSerializedData2);
                        }
                        if ((this.flags & 4) != 0) {
                            this.fwd_from.serializeToStream(abstractSerializedData2);
                        }
                        if ((this.flags & 2048) != 0) {
                            abstractSerializedData2.writeInt64(this.via_bot_id);
                        }
                        if ((this.flags & 8) != 0) {
                            this.reply_to.serializeToStream(abstractSerializedData2);
                        }
                        abstractSerializedData2.writeInt32(this.date);
                        abstractSerializedData2.writeString(this.message);
                        if ((this.flags & LiteMode.FLAG_CALLS_ANIMATIONS) != 0) {
                            this.media.serializeToStream(abstractSerializedData2);
                        }
                        if ((this.flags & 64) != 0) {
                            this.reply_markup.serializeToStream(abstractSerializedData2);
                        }
                        if ((this.flags & 128) != 0) {
                            abstractSerializedData2.writeInt32(481674261);
                            int size = this.entities.size();
                            abstractSerializedData2.writeInt32(size);
                            for (int i13 = 0; i13 < size; i13++) {
                                this.entities.get(i13).serializeToStream(abstractSerializedData2);
                            }
                        }
                        if ((this.flags & 1024) != 0) {
                            abstractSerializedData2.writeInt32(this.views);
                        }
                        if ((this.flags & 1024) != 0) {
                            abstractSerializedData2.writeInt32(this.forwards);
                        }
                        if ((this.flags & 8388608) != 0) {
                            this.replies.serializeToStream(abstractSerializedData2);
                        }
                        if ((this.flags & LiteMode.FLAG_CHAT_SCALE) != 0) {
                            abstractSerializedData2.writeInt32(this.edit_date);
                        }
                        if ((this.flags & 65536) != 0) {
                            abstractSerializedData2.writeString(this.post_author);
                        }
                        if ((this.flags & 131072) != 0) {
                            abstractSerializedData2.writeInt64(this.grouped_id);
                        }
                        if ((this.flags & FileLoaderPriorityQueue.PRIORITY_VALUE_MAX) != 0) {
                            this.reactions.serializeToStream(abstractSerializedData2);
                        }
                        if ((this.flags & 4194304) != 0) {
                            abstractSerializedData2.writeInt32(481674261);
                            int size2 = this.restriction_reason.size();
                            abstractSerializedData2.writeInt32(size2);
                            for (int i14 = 0; i14 < size2; i14++) {
                                this.restriction_reason.get(i14).serializeToStream(abstractSerializedData2);
                            }
                        }
                        if ((this.flags & ConnectionsManager.FileTypeVideo) != 0) {
                            abstractSerializedData2.writeInt32(this.ttl_period);
                        }
                        writeAttachPath(abstractSerializedData2);
                    }
                };
                break;
            case 585853626:
                tLRPC$Message = new TLRPC$TL_message() { // from class: org.telegram.tgnet.TLRPC$TL_message_old
                    @Override // org.telegram.tgnet.TLRPC$TL_message, org.telegram.tgnet.TLObject
                    public void readParams(AbstractSerializedData abstractSerializedData2, boolean z2) {
                        this.id = abstractSerializedData2.readInt32(z2);
                        TLRPC$TL_peerUser tLRPC$TL_peerUser = new TLRPC$TL_peerUser();
                        this.from_id = tLRPC$TL_peerUser;
                        tLRPC$TL_peerUser.user_id = abstractSerializedData2.readInt32(z2);
                        this.peer_id = TLRPC$Peer.TLdeserialize(abstractSerializedData2, abstractSerializedData2.readInt32(z2), z2);
                        this.out = abstractSerializedData2.readBool(z2);
                        this.unread = abstractSerializedData2.readBool(z2);
                        this.flags |= 768;
                        this.date = abstractSerializedData2.readInt32(z2);
                        this.message = abstractSerializedData2.readString(z2);
                        TLRPC$MessageMedia TLdeserialize = TLRPC$MessageMedia.TLdeserialize(abstractSerializedData2, abstractSerializedData2.readInt32(z2), z2);
                        this.media = TLdeserialize;
                        if (TLdeserialize == null || TextUtils.isEmpty(TLdeserialize.captionLegacy)) {
                            return;
                        }
                        this.message = this.media.captionLegacy;
                    }

                    @Override // org.telegram.tgnet.TLRPC$TL_message, org.telegram.tgnet.TLObject
                    public void serializeToStream(AbstractSerializedData abstractSerializedData2) {
                        abstractSerializedData2.writeInt32(585853626);
                        abstractSerializedData2.writeInt32(this.id);
                        abstractSerializedData2.writeInt32((int) this.from_id.user_id);
                        this.peer_id.serializeToStream(abstractSerializedData2);
                        abstractSerializedData2.writeBool(this.out);
                        abstractSerializedData2.writeBool(this.unread);
                        abstractSerializedData2.writeInt32(this.date);
                        abstractSerializedData2.writeString(this.message);
                        this.media.serializeToStream(abstractSerializedData2);
                        writeAttachPath(abstractSerializedData2);
                    }
                };
                break;
            case 678405636:
                tLRPC$Message = new TLRPC$TL_messageService() { // from class: org.telegram.tgnet.TLRPC$TL_messageService_layer123
                    @Override // org.telegram.tgnet.TLRPC$TL_messageService, org.telegram.tgnet.TLObject
                    public void readParams(AbstractSerializedData abstractSerializedData2, boolean z2) {
                        int readInt32 = abstractSerializedData2.readInt32(z2);
                        this.flags = readInt32;
                        this.out = (readInt32 & 2) != 0;
                        this.mentioned = (readInt32 & 16) != 0;
                        this.media_unread = (readInt32 & 32) != 0;
                        this.silent = (readInt32 & LiteMode.FLAG_ANIMATED_EMOJI_REACTIONS_NOT_PREMIUM) != 0;
                        this.post = (readInt32 & LiteMode.FLAG_ANIMATED_EMOJI_KEYBOARD_NOT_PREMIUM) != 0;
                        this.legacy = (readInt32 & 524288) != 0;
                        this.id = abstractSerializedData2.readInt32(z2);
                        if ((this.flags & LiteMode.FLAG_CHAT_BLUR) != 0) {
                            this.from_id = TLRPC$Peer.TLdeserialize(abstractSerializedData2, abstractSerializedData2.readInt32(z2), z2);
                        }
                        this.peer_id = TLRPC$Peer.TLdeserialize(abstractSerializedData2, abstractSerializedData2.readInt32(z2), z2);
                        if ((this.flags & 8) != 0) {
                            this.reply_to = TLRPC$MessageReplyHeader.TLdeserialize(abstractSerializedData2, abstractSerializedData2.readInt32(z2), z2);
                        }
                        this.date = abstractSerializedData2.readInt32(z2);
                        this.action = TLRPC$MessageAction.TLdeserialize(abstractSerializedData2, abstractSerializedData2.readInt32(z2), z2);
                    }

                    @Override // org.telegram.tgnet.TLRPC$TL_messageService, org.telegram.tgnet.TLObject
                    public void serializeToStream(AbstractSerializedData abstractSerializedData2) {
                        abstractSerializedData2.writeInt32(678405636);
                        int i2 = this.out ? this.flags | 2 : this.flags & (-3);
                        this.flags = i2;
                        int i3 = this.mentioned ? i2 | 16 : i2 & (-17);
                        this.flags = i3;
                        int i4 = this.media_unread ? i3 | 32 : i3 & (-33);
                        this.flags = i4;
                        int i5 = this.silent ? i4 | LiteMode.FLAG_ANIMATED_EMOJI_REACTIONS_NOT_PREMIUM : i4 & (-8193);
                        this.flags = i5;
                        int i6 = this.post ? i5 | LiteMode.FLAG_ANIMATED_EMOJI_KEYBOARD_NOT_PREMIUM : i5 & (-16385);
                        this.flags = i6;
                        int i7 = this.legacy ? i6 | 524288 : i6 & (-524289);
                        this.flags = i7;
                        abstractSerializedData2.writeInt32(i7);
                        abstractSerializedData2.writeInt32(this.id);
                        if ((this.flags & LiteMode.FLAG_CHAT_BLUR) != 0) {
                            this.from_id.serializeToStream(abstractSerializedData2);
                        }
                        this.peer_id.serializeToStream(abstractSerializedData2);
                        if ((this.flags & 8) != 0) {
                            this.reply_to.serializeToStream(abstractSerializedData2);
                        }
                        abstractSerializedData2.writeInt32(this.date);
                        this.action.serializeToStream(abstractSerializedData2);
                        writeAttachPath(abstractSerializedData2);
                    }
                };
                break;
            case 721967202:
                tLRPC$Message = new TLRPC$TL_messageService();
                break;
            case 736885382:
                tLRPC$Message = new TLRPC$TL_message() { // from class: org.telegram.tgnet.TLRPC$TL_message_old6
                    @Override // org.telegram.tgnet.TLRPC$TL_message, org.telegram.tgnet.TLObject
                    public void readParams(AbstractSerializedData abstractSerializedData2, boolean z2) {
                        int readInt32 = abstractSerializedData2.readInt32(z2) | LiteMode.FLAG_CHAT_BLUR;
                        this.flags = readInt32;
                        this.unread = (readInt32 & 1) != 0;
                        this.out = (readInt32 & 2) != 0;
                        this.mentioned = (readInt32 & 16) != 0;
                        this.media_unread = (readInt32 & 32) != 0;
                        this.id = abstractSerializedData2.readInt32(z2);
                        TLRPC$TL_peerUser tLRPC$TL_peerUser = new TLRPC$TL_peerUser();
                        this.from_id = tLRPC$TL_peerUser;
                        tLRPC$TL_peerUser.user_id = abstractSerializedData2.readInt32(z2);
                        this.peer_id = TLRPC$Peer.TLdeserialize(abstractSerializedData2, abstractSerializedData2.readInt32(z2), z2);
                        if ((this.flags & 4) != 0) {
                            TLRPC$TL_messageFwdHeader tLRPC$TL_messageFwdHeader = new TLRPC$TL_messageFwdHeader();
                            this.fwd_from = tLRPC$TL_messageFwdHeader;
                            tLRPC$TL_messageFwdHeader.from_id = new TLRPC$TL_peerUser();
                            this.fwd_from.from_id.user_id = abstractSerializedData2.readInt32(z2);
                            TLRPC$MessageFwdHeader tLRPC$MessageFwdHeader = this.fwd_from;
                            tLRPC$MessageFwdHeader.flags |= 1;
                            tLRPC$MessageFwdHeader.date = abstractSerializedData2.readInt32(z2);
                        }
                        if ((this.flags & 8) != 0) {
                            TLRPC$TL_messageReplyHeader tLRPC$TL_messageReplyHeader = new TLRPC$TL_messageReplyHeader();
                            this.reply_to = tLRPC$TL_messageReplyHeader;
                            tLRPC$TL_messageReplyHeader.flags |= 16;
                            tLRPC$TL_messageReplyHeader.reply_to_msg_id = abstractSerializedData2.readInt32(z2);
                        }
                        this.date = abstractSerializedData2.readInt32(z2);
                        this.message = abstractSerializedData2.readString(z2);
                        if ((this.flags & LiteMode.FLAG_CALLS_ANIMATIONS) != 0) {
                            TLRPC$MessageMedia TLdeserialize = TLRPC$MessageMedia.TLdeserialize(abstractSerializedData2, abstractSerializedData2.readInt32(z2), z2);
                            this.media = TLdeserialize;
                            if (TLdeserialize != null && !TextUtils.isEmpty(TLdeserialize.captionLegacy)) {
                                this.message = this.media.captionLegacy;
                            }
                        } else {
                            this.media = new TLRPC$TL_messageMediaEmpty();
                        }
                        if ((this.flags & 64) != 0) {
                            this.reply_markup = TLRPC$ReplyMarkup.TLdeserialize(abstractSerializedData2, abstractSerializedData2.readInt32(z2), z2);
                        }
                        if ((this.flags & 128) != 0) {
                            int readInt322 = abstractSerializedData2.readInt32(z2);
                            if (readInt322 != 481674261) {
                                if (z2) {
                                    throw new RuntimeException(String.format("wrong Vector magic, got %x", Integer.valueOf(readInt322)));
                                }
                                return;
                            }
                            int readInt323 = abstractSerializedData2.readInt32(z2);
                            for (int i2 = 0; i2 < readInt323; i2++) {
                                TLRPC$MessageEntity TLdeserialize2 = TLRPC$MessageEntity.TLdeserialize(abstractSerializedData2, abstractSerializedData2.readInt32(z2), z2);
                                if (TLdeserialize2 == null) {
                                    return;
                                }
                                this.entities.add(TLdeserialize2);
                            }
                        }
                    }

                    @Override // org.telegram.tgnet.TLRPC$TL_message, org.telegram.tgnet.TLObject
                    public void serializeToStream(AbstractSerializedData abstractSerializedData2) {
                        abstractSerializedData2.writeInt32(736885382);
                        int i2 = this.unread ? this.flags | 1 : this.flags & (-2);
                        this.flags = i2;
                        int i3 = this.out ? i2 | 2 : i2 & (-3);
                        this.flags = i3;
                        int i4 = this.mentioned ? i3 | 16 : i3 & (-17);
                        this.flags = i4;
                        int i5 = this.media_unread ? i4 | 32 : i4 & (-33);
                        this.flags = i5;
                        abstractSerializedData2.writeInt32(i5);
                        abstractSerializedData2.writeInt32(this.id);
                        abstractSerializedData2.writeInt32((int) this.from_id.user_id);
                        this.peer_id.serializeToStream(abstractSerializedData2);
                        if ((this.flags & 4) != 0) {
                            abstractSerializedData2.writeInt32((int) this.fwd_from.from_id.user_id);
                            abstractSerializedData2.writeInt32(this.fwd_from.date);
                        }
                        if ((this.flags & 8) != 0) {
                            abstractSerializedData2.writeInt32(this.reply_to.reply_to_msg_id);
                        }
                        abstractSerializedData2.writeInt32(this.date);
                        abstractSerializedData2.writeString(this.message);
                        if ((this.flags & LiteMode.FLAG_CALLS_ANIMATIONS) != 0) {
                            this.media.serializeToStream(abstractSerializedData2);
                        }
                        if ((this.flags & 64) != 0) {
                            this.reply_markup.serializeToStream(abstractSerializedData2);
                        }
                        if ((this.flags & 128) != 0) {
                            abstractSerializedData2.writeInt32(481674261);
                            int size = this.entities.size();
                            abstractSerializedData2.writeInt32(size);
                            for (int i6 = 0; i6 < size; i6++) {
                                this.entities.get(i6).serializeToStream(abstractSerializedData2);
                            }
                        }
                        writeAttachPath(abstractSerializedData2);
                    }
                };
                break;
            case 940666592:
                tLRPC$Message = new TLRPC$TL_message() { // from class: org.telegram.tgnet.TLRPC$TL_message_layer169
                    @Override // org.telegram.tgnet.TLRPC$TL_message, org.telegram.tgnet.TLObject
                    public void readParams(AbstractSerializedData abstractSerializedData2, boolean z2) {
                        int readInt32 = abstractSerializedData2.readInt32(z2);
                        this.flags = readInt32;
                        this.out = (readInt32 & 2) != 0;
                        this.mentioned = (readInt32 & 16) != 0;
                        this.media_unread = (readInt32 & 32) != 0;
                        this.silent = (readInt32 & LiteMode.FLAG_ANIMATED_EMOJI_REACTIONS_NOT_PREMIUM) != 0;
                        this.post = (readInt32 & LiteMode.FLAG_ANIMATED_EMOJI_KEYBOARD_NOT_PREMIUM) != 0;
                        this.from_scheduled = (262144 & readInt32) != 0;
                        this.legacy = (524288 & readInt32) != 0;
                        this.edit_hide = (2097152 & readInt32) != 0;
                        this.pinned = (16777216 & readInt32) != 0;
                        this.noforwards = (67108864 & readInt32) != 0;
                        this.invert_media = (readInt32 & 134217728) != 0;
                        this.id = abstractSerializedData2.readInt32(z2);
                        if ((this.flags & LiteMode.FLAG_CHAT_BLUR) != 0) {
                            this.from_id = TLRPC$Peer.TLdeserialize(abstractSerializedData2, abstractSerializedData2.readInt32(z2), z2);
                        }
                        this.peer_id = TLRPC$Peer.TLdeserialize(abstractSerializedData2, abstractSerializedData2.readInt32(z2), z2);
                        if ((this.flags & 4) != 0) {
                            this.fwd_from = TLRPC$MessageFwdHeader.TLdeserialize(abstractSerializedData2, abstractSerializedData2.readInt32(z2), z2);
                        }
                        if ((this.flags & 2048) != 0) {
                            this.via_bot_id = abstractSerializedData2.readInt64(z2);
                        }
                        if ((this.flags & 8) != 0) {
                            this.reply_to = TLRPC$MessageReplyHeader.TLdeserialize(abstractSerializedData2, abstractSerializedData2.readInt32(z2), z2);
                        }
                        this.date = abstractSerializedData2.readInt32(z2);
                        this.message = abstractSerializedData2.readString(z2);
                        if ((this.flags & LiteMode.FLAG_CALLS_ANIMATIONS) != 0) {
                            TLRPC$MessageMedia TLdeserialize = TLRPC$MessageMedia.TLdeserialize(abstractSerializedData2, abstractSerializedData2.readInt32(z2), z2);
                            this.media = TLdeserialize;
                            if (TLdeserialize != null) {
                                this.ttl = TLdeserialize.ttl_seconds;
                            }
                            if (TLdeserialize != null && !TextUtils.isEmpty(TLdeserialize.captionLegacy)) {
                                this.message = this.media.captionLegacy;
                            }
                        }
                        if ((this.flags & 64) != 0) {
                            this.reply_markup = TLRPC$ReplyMarkup.TLdeserialize(abstractSerializedData2, abstractSerializedData2.readInt32(z2), z2);
                        }
                        if ((this.flags & 128) != 0) {
                            int readInt322 = abstractSerializedData2.readInt32(z2);
                            if (readInt322 != 481674261) {
                                if (z2) {
                                    throw new RuntimeException(String.format("wrong Vector magic, got %x", Integer.valueOf(readInt322)));
                                }
                                return;
                            }
                            int readInt323 = abstractSerializedData2.readInt32(z2);
                            for (int i2 = 0; i2 < readInt323; i2++) {
                                TLRPC$MessageEntity TLdeserialize2 = TLRPC$MessageEntity.TLdeserialize(abstractSerializedData2, abstractSerializedData2.readInt32(z2), z2);
                                if (TLdeserialize2 == null) {
                                    return;
                                }
                                this.entities.add(TLdeserialize2);
                            }
                        }
                        if ((this.flags & 1024) != 0) {
                            this.views = abstractSerializedData2.readInt32(z2);
                        }
                        if ((this.flags & 1024) != 0) {
                            this.forwards = abstractSerializedData2.readInt32(z2);
                        }
                        if ((this.flags & 8388608) != 0) {
                            this.replies = TLRPC$MessageReplies.TLdeserialize(abstractSerializedData2, abstractSerializedData2.readInt32(z2), z2);
                        }
                        if ((this.flags & LiteMode.FLAG_CHAT_SCALE) != 0) {
                            this.edit_date = abstractSerializedData2.readInt32(z2);
                        }
                        if ((this.flags & 65536) != 0) {
                            this.post_author = abstractSerializedData2.readString(z2);
                        }
                        if ((this.flags & 131072) != 0) {
                            this.grouped_id = abstractSerializedData2.readInt64(z2);
                        }
                        if ((this.flags & FileLoaderPriorityQueue.PRIORITY_VALUE_MAX) != 0) {
                            this.reactions = TLRPC$MessageReactions.TLdeserialize(abstractSerializedData2, abstractSerializedData2.readInt32(z2), z2);
                        }
                        if ((this.flags & 4194304) != 0) {
                            int readInt324 = abstractSerializedData2.readInt32(z2);
                            if (readInt324 != 481674261) {
                                if (z2) {
                                    throw new RuntimeException(String.format("wrong Vector magic, got %x", Integer.valueOf(readInt324)));
                                }
                                return;
                            }
                            int readInt325 = abstractSerializedData2.readInt32(z2);
                            for (int i3 = 0; i3 < readInt325; i3++) {
                                TLRPC$TL_restrictionReason TLdeserialize3 = TLRPC$TL_restrictionReason.TLdeserialize(abstractSerializedData2, abstractSerializedData2.readInt32(z2), z2);
                                if (TLdeserialize3 == null) {
                                    return;
                                }
                                this.restriction_reason.add(TLdeserialize3);
                            }
                        }
                        if ((this.flags & ConnectionsManager.FileTypeVideo) != 0) {
                            this.ttl_period = abstractSerializedData2.readInt32(z2);
                        }
                    }

                    @Override // org.telegram.tgnet.TLRPC$TL_message, org.telegram.tgnet.TLObject
                    public void serializeToStream(AbstractSerializedData abstractSerializedData2) {
                        abstractSerializedData2.writeInt32(940666592);
                        int i2 = this.out ? this.flags | 2 : this.flags & (-3);
                        this.flags = i2;
                        int i3 = this.mentioned ? i2 | 16 : i2 & (-17);
                        this.flags = i3;
                        int i4 = this.media_unread ? i3 | 32 : i3 & (-33);
                        this.flags = i4;
                        int i5 = this.silent ? i4 | LiteMode.FLAG_ANIMATED_EMOJI_REACTIONS_NOT_PREMIUM : i4 & (-8193);
                        this.flags = i5;
                        int i6 = this.post ? i5 | LiteMode.FLAG_ANIMATED_EMOJI_KEYBOARD_NOT_PREMIUM : i5 & (-16385);
                        this.flags = i6;
                        int i7 = this.from_scheduled ? i6 | 262144 : i6 & (-262145);
                        this.flags = i7;
                        int i8 = this.legacy ? i7 | 524288 : i7 & (-524289);
                        this.flags = i8;
                        int i9 = this.edit_hide ? i8 | 2097152 : i8 & (-2097153);
                        this.flags = i9;
                        int i10 = this.pinned ? i9 | ConnectionsManager.FileTypePhoto : i9 & (-16777217);
                        this.flags = i10;
                        int i11 = this.noforwards ? i10 | ConnectionsManager.FileTypeFile : i10 & (-67108865);
                        this.flags = i11;
                        int i12 = this.invert_media ? i11 | 134217728 : i11 & (-134217729);
                        this.flags = i12;
                        abstractSerializedData2.writeInt32(i12);
                        abstractSerializedData2.writeInt32(this.id);
                        if ((this.flags & LiteMode.FLAG_CHAT_BLUR) != 0) {
                            this.from_id.serializeToStream(abstractSerializedData2);
                        }
                        this.peer_id.serializeToStream(abstractSerializedData2);
                        if ((this.flags & 4) != 0) {
                            this.fwd_from.serializeToStream(abstractSerializedData2);
                        }
                        if ((this.flags & 2048) != 0) {
                            abstractSerializedData2.writeInt64(this.via_bot_id);
                        }
                        if ((this.flags & 8) != 0) {
                            this.reply_to.serializeToStream(abstractSerializedData2);
                        }
                        abstractSerializedData2.writeInt32(this.date);
                        abstractSerializedData2.writeString(this.message);
                        if ((this.flags & LiteMode.FLAG_CALLS_ANIMATIONS) != 0) {
                            this.media.serializeToStream(abstractSerializedData2);
                        }
                        if ((this.flags & 64) != 0) {
                            this.reply_markup.serializeToStream(abstractSerializedData2);
                        }
                        if ((this.flags & 128) != 0) {
                            abstractSerializedData2.writeInt32(481674261);
                            int size = this.entities.size();
                            abstractSerializedData2.writeInt32(size);
                            for (int i13 = 0; i13 < size; i13++) {
                                this.entities.get(i13).serializeToStream(abstractSerializedData2);
                            }
                        }
                        if ((this.flags & 1024) != 0) {
                            abstractSerializedData2.writeInt32(this.views);
                        }
                        if ((this.flags & 1024) != 0) {
                            abstractSerializedData2.writeInt32(this.forwards);
                        }
                        if ((this.flags & 8388608) != 0) {
                            this.replies.serializeToStream(abstractSerializedData2);
                        }
                        if ((this.flags & LiteMode.FLAG_CHAT_SCALE) != 0) {
                            abstractSerializedData2.writeInt32(this.edit_date);
                        }
                        if ((this.flags & 65536) != 0) {
                            abstractSerializedData2.writeString(this.post_author);
                        }
                        if ((this.flags & 131072) != 0) {
                            abstractSerializedData2.writeInt64(this.grouped_id);
                        }
                        if ((this.flags & FileLoaderPriorityQueue.PRIORITY_VALUE_MAX) != 0) {
                            this.reactions.serializeToStream(abstractSerializedData2);
                        }
                        if ((this.flags & 4194304) != 0) {
                            abstractSerializedData2.writeInt32(481674261);
                            int size2 = this.restriction_reason.size();
                            abstractSerializedData2.writeInt32(size2);
                            for (int i14 = 0; i14 < size2; i14++) {
                                this.restriction_reason.get(i14).serializeToStream(abstractSerializedData2);
                            }
                        }
                        if ((this.flags & ConnectionsManager.FileTypeVideo) != 0) {
                            abstractSerializedData2.writeInt32(this.ttl_period);
                        }
                        writeAttachPath(abstractSerializedData2);
                    }
                };
                break;
            case 1157215293:
                tLRPC$Message = new TLRPC$TL_message() { // from class: org.telegram.tgnet.TLRPC$TL_message_layer104
                    @Override // org.telegram.tgnet.TLRPC$TL_message, org.telegram.tgnet.TLObject
                    public void readParams(AbstractSerializedData abstractSerializedData2, boolean z2) {
                        int readInt32 = abstractSerializedData2.readInt32(z2);
                        this.flags = readInt32;
                        this.out = (readInt32 & 2) != 0;
                        this.mentioned = (readInt32 & 16) != 0;
                        this.media_unread = (readInt32 & 32) != 0;
                        this.silent = (readInt32 & LiteMode.FLAG_ANIMATED_EMOJI_REACTIONS_NOT_PREMIUM) != 0;
                        this.post = (readInt32 & LiteMode.FLAG_ANIMATED_EMOJI_KEYBOARD_NOT_PREMIUM) != 0;
                        this.from_scheduled = (262144 & readInt32) != 0;
                        this.legacy = (readInt32 & 524288) != 0;
                        this.id = abstractSerializedData2.readInt32(z2);
                        if ((this.flags & LiteMode.FLAG_CHAT_BLUR) != 0) {
                            TLRPC$TL_peerUser tLRPC$TL_peerUser = new TLRPC$TL_peerUser();
                            this.from_id = tLRPC$TL_peerUser;
                            tLRPC$TL_peerUser.user_id = abstractSerializedData2.readInt32(z2);
                        }
                        this.peer_id = TLRPC$Peer.TLdeserialize(abstractSerializedData2, abstractSerializedData2.readInt32(z2), z2);
                        if ((this.flags & 4) != 0) {
                            this.fwd_from = TLRPC$MessageFwdHeader.TLdeserialize(abstractSerializedData2, abstractSerializedData2.readInt32(z2), z2);
                        }
                        if ((this.flags & 2048) != 0) {
                            this.via_bot_id = abstractSerializedData2.readInt32(z2);
                        }
                        if ((this.flags & 8) != 0) {
                            TLRPC$TL_messageReplyHeader tLRPC$TL_messageReplyHeader = new TLRPC$TL_messageReplyHeader();
                            this.reply_to = tLRPC$TL_messageReplyHeader;
                            tLRPC$TL_messageReplyHeader.flags |= 16;
                            tLRPC$TL_messageReplyHeader.reply_to_msg_id = abstractSerializedData2.readInt32(z2);
                        }
                        this.date = abstractSerializedData2.readInt32(z2);
                        this.message = abstractSerializedData2.readString(z2);
                        if ((this.flags & LiteMode.FLAG_CALLS_ANIMATIONS) != 0) {
                            TLRPC$MessageMedia TLdeserialize = TLRPC$MessageMedia.TLdeserialize(abstractSerializedData2, abstractSerializedData2.readInt32(z2), z2);
                            this.media = TLdeserialize;
                            if (TLdeserialize != null) {
                                this.ttl = TLdeserialize.ttl_seconds;
                            }
                            if (TLdeserialize != null && !TextUtils.isEmpty(TLdeserialize.captionLegacy)) {
                                this.message = this.media.captionLegacy;
                            }
                        }
                        if ((this.flags & 64) != 0) {
                            this.reply_markup = TLRPC$ReplyMarkup.TLdeserialize(abstractSerializedData2, abstractSerializedData2.readInt32(z2), z2);
                        }
                        if ((this.flags & 128) != 0) {
                            int readInt322 = abstractSerializedData2.readInt32(z2);
                            if (readInt322 != 481674261) {
                                if (z2) {
                                    throw new RuntimeException(String.format("wrong Vector magic, got %x", Integer.valueOf(readInt322)));
                                }
                                return;
                            }
                            int readInt323 = abstractSerializedData2.readInt32(z2);
                            for (int i2 = 0; i2 < readInt323; i2++) {
                                TLRPC$MessageEntity TLdeserialize2 = TLRPC$MessageEntity.TLdeserialize(abstractSerializedData2, abstractSerializedData2.readInt32(z2), z2);
                                if (TLdeserialize2 == null) {
                                    return;
                                }
                                this.entities.add(TLdeserialize2);
                            }
                        }
                        if ((this.flags & 1024) != 0) {
                            this.views = abstractSerializedData2.readInt32(z2);
                        }
                        if ((this.flags & LiteMode.FLAG_CHAT_SCALE) != 0) {
                            this.edit_date = abstractSerializedData2.readInt32(z2);
                        }
                        if ((this.flags & 65536) != 0) {
                            this.post_author = abstractSerializedData2.readString(z2);
                        }
                        if ((this.flags & 131072) != 0) {
                            this.grouped_id = abstractSerializedData2.readInt64(z2);
                        }
                    }

                    @Override // org.telegram.tgnet.TLRPC$TL_message, org.telegram.tgnet.TLObject
                    public void serializeToStream(AbstractSerializedData abstractSerializedData2) {
                        abstractSerializedData2.writeInt32(1157215293);
                        int i2 = this.out ? this.flags | 2 : this.flags & (-3);
                        this.flags = i2;
                        int i3 = this.mentioned ? i2 | 16 : i2 & (-17);
                        this.flags = i3;
                        int i4 = this.media_unread ? i3 | 32 : i3 & (-33);
                        this.flags = i4;
                        int i5 = this.silent ? i4 | LiteMode.FLAG_ANIMATED_EMOJI_REACTIONS_NOT_PREMIUM : i4 & (-8193);
                        this.flags = i5;
                        int i6 = this.post ? i5 | LiteMode.FLAG_ANIMATED_EMOJI_KEYBOARD_NOT_PREMIUM : i5 & (-16385);
                        this.flags = i6;
                        int i7 = this.from_scheduled ? i6 | 262144 : i6 & (-262145);
                        this.flags = i7;
                        int i8 = this.legacy ? i7 | 524288 : i7 & (-524289);
                        this.flags = i8;
                        abstractSerializedData2.writeInt32(i8);
                        abstractSerializedData2.writeInt32(this.id);
                        if ((this.flags & LiteMode.FLAG_CHAT_BLUR) != 0) {
                            abstractSerializedData2.writeInt32((int) this.from_id.user_id);
                        }
                        this.peer_id.serializeToStream(abstractSerializedData2);
                        if ((this.flags & 4) != 0) {
                            this.fwd_from.serializeToStream(abstractSerializedData2);
                        }
                        if ((this.flags & 2048) != 0) {
                            abstractSerializedData2.writeInt32((int) this.via_bot_id);
                        }
                        if ((this.flags & 8) != 0) {
                            abstractSerializedData2.writeInt32(this.reply_to.reply_to_msg_id);
                        }
                        abstractSerializedData2.writeInt32(this.date);
                        abstractSerializedData2.writeString(this.message);
                        if ((this.flags & LiteMode.FLAG_CALLS_ANIMATIONS) != 0) {
                            this.media.serializeToStream(abstractSerializedData2);
                        }
                        if ((this.flags & 64) != 0) {
                            this.reply_markup.serializeToStream(abstractSerializedData2);
                        }
                        if ((this.flags & 128) != 0) {
                            abstractSerializedData2.writeInt32(481674261);
                            int size = this.entities.size();
                            abstractSerializedData2.writeInt32(size);
                            for (int i9 = 0; i9 < size; i9++) {
                                this.entities.get(i9).serializeToStream(abstractSerializedData2);
                            }
                        }
                        if ((this.flags & 1024) != 0) {
                            abstractSerializedData2.writeInt32(this.views);
                        }
                        if ((this.flags & LiteMode.FLAG_CHAT_SCALE) != 0) {
                            abstractSerializedData2.writeInt32(this.edit_date);
                        }
                        if ((this.flags & 65536) != 0) {
                            abstractSerializedData2.writeString(this.post_author);
                        }
                        if ((this.flags & 131072) != 0) {
                            abstractSerializedData2.writeInt64(this.grouped_id);
                        }
                        writeAttachPath(abstractSerializedData2);
                    }
                };
                break;
            case 1160515173:
                tLRPC$Message = new TLRPC$TL_message() { // from class: org.telegram.tgnet.TLRPC$TL_message_layer117
                    @Override // org.telegram.tgnet.TLRPC$TL_message, org.telegram.tgnet.TLObject
                    public void readParams(AbstractSerializedData abstractSerializedData2, boolean z2) {
                        int readInt32 = abstractSerializedData2.readInt32(z2);
                        this.flags = readInt32;
                        this.out = (readInt32 & 2) != 0;
                        this.mentioned = (readInt32 & 16) != 0;
                        this.media_unread = (readInt32 & 32) != 0;
                        this.silent = (readInt32 & LiteMode.FLAG_ANIMATED_EMOJI_REACTIONS_NOT_PREMIUM) != 0;
                        this.post = (readInt32 & LiteMode.FLAG_ANIMATED_EMOJI_KEYBOARD_NOT_PREMIUM) != 0;
                        this.from_scheduled = (262144 & readInt32) != 0;
                        this.legacy = (524288 & readInt32) != 0;
                        this.edit_hide = (readInt32 & 2097152) != 0;
                        this.id = abstractSerializedData2.readInt32(z2);
                        if ((this.flags & LiteMode.FLAG_CHAT_BLUR) != 0) {
                            TLRPC$TL_peerUser tLRPC$TL_peerUser = new TLRPC$TL_peerUser();
                            this.from_id = tLRPC$TL_peerUser;
                            tLRPC$TL_peerUser.user_id = abstractSerializedData2.readInt32(z2);
                        }
                        this.peer_id = TLRPC$Peer.TLdeserialize(abstractSerializedData2, abstractSerializedData2.readInt32(z2), z2);
                        if ((this.flags & 4) != 0) {
                            this.fwd_from = TLRPC$MessageFwdHeader.TLdeserialize(abstractSerializedData2, abstractSerializedData2.readInt32(z2), z2);
                        }
                        if ((this.flags & 2048) != 0) {
                            this.via_bot_id = abstractSerializedData2.readInt32(z2);
                        }
                        if ((this.flags & 8) != 0) {
                            TLRPC$TL_messageReplyHeader tLRPC$TL_messageReplyHeader = new TLRPC$TL_messageReplyHeader();
                            this.reply_to = tLRPC$TL_messageReplyHeader;
                            tLRPC$TL_messageReplyHeader.flags |= 16;
                            tLRPC$TL_messageReplyHeader.reply_to_msg_id = abstractSerializedData2.readInt32(z2);
                        }
                        this.date = abstractSerializedData2.readInt32(z2);
                        this.message = abstractSerializedData2.readString(z2);
                        if ((this.flags & LiteMode.FLAG_CALLS_ANIMATIONS) != 0) {
                            TLRPC$MessageMedia TLdeserialize = TLRPC$MessageMedia.TLdeserialize(abstractSerializedData2, abstractSerializedData2.readInt32(z2), z2);
                            this.media = TLdeserialize;
                            if (TLdeserialize != null) {
                                this.ttl = TLdeserialize.ttl_seconds;
                            }
                            if (TLdeserialize != null && !TextUtils.isEmpty(TLdeserialize.captionLegacy)) {
                                this.message = this.media.captionLegacy;
                            }
                        }
                        if ((this.flags & 64) != 0) {
                            this.reply_markup = TLRPC$ReplyMarkup.TLdeserialize(abstractSerializedData2, abstractSerializedData2.readInt32(z2), z2);
                        }
                        if ((this.flags & 128) != 0) {
                            int readInt322 = abstractSerializedData2.readInt32(z2);
                            if (readInt322 != 481674261) {
                                if (z2) {
                                    throw new RuntimeException(String.format("wrong Vector magic, got %x", Integer.valueOf(readInt322)));
                                }
                                return;
                            }
                            int readInt323 = abstractSerializedData2.readInt32(z2);
                            for (int i2 = 0; i2 < readInt323; i2++) {
                                TLRPC$MessageEntity TLdeserialize2 = TLRPC$MessageEntity.TLdeserialize(abstractSerializedData2, abstractSerializedData2.readInt32(z2), z2);
                                if (TLdeserialize2 == null) {
                                    return;
                                }
                                this.entities.add(TLdeserialize2);
                            }
                        }
                        if ((this.flags & 1024) != 0) {
                            this.views = abstractSerializedData2.readInt32(z2);
                        }
                        if ((this.flags & LiteMode.FLAG_CHAT_SCALE) != 0) {
                            this.edit_date = abstractSerializedData2.readInt32(z2);
                        }
                        if ((this.flags & 65536) != 0) {
                            this.post_author = abstractSerializedData2.readString(z2);
                        }
                        if ((this.flags & 131072) != 0) {
                            this.grouped_id = abstractSerializedData2.readInt64(z2);
                        }
                        if ((this.flags & 4194304) != 0) {
                            int readInt324 = abstractSerializedData2.readInt32(z2);
                            if (readInt324 != 481674261) {
                                if (z2) {
                                    throw new RuntimeException(String.format("wrong Vector magic, got %x", Integer.valueOf(readInt324)));
                                }
                                return;
                            }
                            int readInt325 = abstractSerializedData2.readInt32(z2);
                            for (int i3 = 0; i3 < readInt325; i3++) {
                                TLRPC$TL_restrictionReason TLdeserialize3 = TLRPC$TL_restrictionReason.TLdeserialize(abstractSerializedData2, abstractSerializedData2.readInt32(z2), z2);
                                if (TLdeserialize3 == null) {
                                    return;
                                }
                                this.restriction_reason.add(TLdeserialize3);
                            }
                        }
                    }

                    @Override // org.telegram.tgnet.TLRPC$TL_message, org.telegram.tgnet.TLObject
                    public void serializeToStream(AbstractSerializedData abstractSerializedData2) {
                        abstractSerializedData2.writeInt32(1160515173);
                        int i2 = this.out ? this.flags | 2 : this.flags & (-3);
                        this.flags = i2;
                        int i3 = this.mentioned ? i2 | 16 : i2 & (-17);
                        this.flags = i3;
                        int i4 = this.media_unread ? i3 | 32 : i3 & (-33);
                        this.flags = i4;
                        int i5 = this.silent ? i4 | LiteMode.FLAG_ANIMATED_EMOJI_REACTIONS_NOT_PREMIUM : i4 & (-8193);
                        this.flags = i5;
                        int i6 = this.post ? i5 | LiteMode.FLAG_ANIMATED_EMOJI_KEYBOARD_NOT_PREMIUM : i5 & (-16385);
                        this.flags = i6;
                        int i7 = this.from_scheduled ? i6 | 262144 : i6 & (-262145);
                        this.flags = i7;
                        int i8 = this.legacy ? i7 | 524288 : i7 & (-524289);
                        this.flags = i8;
                        int i9 = this.edit_hide ? i8 | 2097152 : i8 & (-2097153);
                        this.flags = i9;
                        abstractSerializedData2.writeInt32(i9);
                        abstractSerializedData2.writeInt32(this.id);
                        if ((this.flags & LiteMode.FLAG_CHAT_BLUR) != 0) {
                            abstractSerializedData2.writeInt32((int) this.from_id.user_id);
                        }
                        this.peer_id.serializeToStream(abstractSerializedData2);
                        if ((this.flags & 4) != 0) {
                            this.fwd_from.serializeToStream(abstractSerializedData2);
                        }
                        if ((this.flags & 2048) != 0) {
                            abstractSerializedData2.writeInt32((int) this.via_bot_id);
                        }
                        if ((this.flags & 8) != 0) {
                            abstractSerializedData2.writeInt32(this.reply_to.reply_to_msg_id);
                        }
                        abstractSerializedData2.writeInt32(this.date);
                        abstractSerializedData2.writeString(this.message);
                        if ((this.flags & LiteMode.FLAG_CALLS_ANIMATIONS) != 0) {
                            this.media.serializeToStream(abstractSerializedData2);
                        }
                        if ((this.flags & 64) != 0) {
                            this.reply_markup.serializeToStream(abstractSerializedData2);
                        }
                        if ((this.flags & 128) != 0) {
                            abstractSerializedData2.writeInt32(481674261);
                            int size = this.entities.size();
                            abstractSerializedData2.writeInt32(size);
                            for (int i10 = 0; i10 < size; i10++) {
                                this.entities.get(i10).serializeToStream(abstractSerializedData2);
                            }
                        }
                        if ((this.flags & 1024) != 0) {
                            abstractSerializedData2.writeInt32(this.views);
                        }
                        if ((this.flags & LiteMode.FLAG_CHAT_SCALE) != 0) {
                            abstractSerializedData2.writeInt32(this.edit_date);
                        }
                        if ((this.flags & 65536) != 0) {
                            abstractSerializedData2.writeString(this.post_author);
                        }
                        if ((this.flags & 131072) != 0) {
                            abstractSerializedData2.writeInt64(this.grouped_id);
                        }
                        if ((this.flags & 4194304) != 0) {
                            abstractSerializedData2.writeInt32(481674261);
                            int size2 = this.restriction_reason.size();
                            abstractSerializedData2.writeInt32(size2);
                            for (int i11 = 0; i11 < size2; i11++) {
                                this.restriction_reason.get(i11).serializeToStream(abstractSerializedData2);
                            }
                        }
                        writeAttachPath(abstractSerializedData2);
                    }
                };
                break;
            case 1431655928:
                tLRPC$Message = new TLRPC$TL_message_secret() { // from class: org.telegram.tgnet.TLRPC$TL_message_secret_old
                    @Override // org.telegram.tgnet.TLRPC$TL_message_secret, org.telegram.tgnet.TLRPC$TL_message, org.telegram.tgnet.TLObject
                    public void readParams(AbstractSerializedData abstractSerializedData2, boolean z2) {
                        int readInt32 = abstractSerializedData2.readInt32(z2) | LiteMode.FLAG_CHAT_BLUR | LiteMode.FLAG_CALLS_ANIMATIONS;
                        this.flags = readInt32;
                        this.unread = (readInt32 & 1) != 0;
                        this.out = (readInt32 & 2) != 0;
                        this.mentioned = (readInt32 & 16) != 0;
                        this.media_unread = (readInt32 & 32) != 0;
                        this.id = abstractSerializedData2.readInt32(z2);
                        this.ttl = abstractSerializedData2.readInt32(z2);
                        TLRPC$TL_peerUser tLRPC$TL_peerUser = new TLRPC$TL_peerUser();
                        this.from_id = tLRPC$TL_peerUser;
                        tLRPC$TL_peerUser.user_id = abstractSerializedData2.readInt32(z2);
                        this.peer_id = TLRPC$Peer.TLdeserialize(abstractSerializedData2, abstractSerializedData2.readInt32(z2), z2);
                        this.date = abstractSerializedData2.readInt32(z2);
                        this.message = abstractSerializedData2.readString(z2);
                        TLRPC$MessageMedia TLdeserialize = TLRPC$MessageMedia.TLdeserialize(abstractSerializedData2, abstractSerializedData2.readInt32(z2), z2);
                        this.media = TLdeserialize;
                        if (TLdeserialize == null || TextUtils.isEmpty(TLdeserialize.captionLegacy)) {
                            return;
                        }
                        this.message = this.media.captionLegacy;
                    }

                    @Override // org.telegram.tgnet.TLRPC$TL_message_secret, org.telegram.tgnet.TLRPC$TL_message, org.telegram.tgnet.TLObject
                    public void serializeToStream(AbstractSerializedData abstractSerializedData2) {
                        abstractSerializedData2.writeInt32(1431655928);
                        int i2 = this.unread ? this.flags | 1 : this.flags & (-2);
                        this.flags = i2;
                        int i3 = this.out ? i2 | 2 : i2 & (-3);
                        this.flags = i3;
                        int i4 = this.mentioned ? i3 | 16 : i3 & (-17);
                        this.flags = i4;
                        int i5 = this.media_unread ? i4 | 32 : i4 & (-33);
                        this.flags = i5;
                        abstractSerializedData2.writeInt32(i5);
                        abstractSerializedData2.writeInt32(this.id);
                        abstractSerializedData2.writeInt32(this.ttl);
                        abstractSerializedData2.writeInt32((int) this.from_id.user_id);
                        this.peer_id.serializeToStream(abstractSerializedData2);
                        abstractSerializedData2.writeInt32(this.date);
                        abstractSerializedData2.writeString(this.message);
                        this.media.serializeToStream(abstractSerializedData2);
                        writeAttachPath(abstractSerializedData2);
                    }
                };
                break;
            case 1431655929:
                tLRPC$Message = new TLRPC$TL_message() { // from class: org.telegram.tgnet.TLRPC$TL_message_secret_layer72
                    @Override // org.telegram.tgnet.TLRPC$TL_message, org.telegram.tgnet.TLObject
                    public void readParams(AbstractSerializedData abstractSerializedData2, boolean z2) {
                        int readInt32 = abstractSerializedData2.readInt32(z2);
                        this.flags = readInt32;
                        this.unread = (readInt32 & 1) != 0;
                        this.out = (readInt32 & 2) != 0;
                        this.mentioned = (readInt32 & 16) != 0;
                        this.media_unread = (readInt32 & 32) != 0;
                        this.id = abstractSerializedData2.readInt32(z2);
                        this.ttl = abstractSerializedData2.readInt32(z2);
                        TLRPC$TL_peerUser tLRPC$TL_peerUser = new TLRPC$TL_peerUser();
                        this.from_id = tLRPC$TL_peerUser;
                        tLRPC$TL_peerUser.user_id = abstractSerializedData2.readInt32(z2);
                        this.peer_id = TLRPC$Peer.TLdeserialize(abstractSerializedData2, abstractSerializedData2.readInt32(z2), z2);
                        this.date = abstractSerializedData2.readInt32(z2);
                        this.message = abstractSerializedData2.readString(z2);
                        TLRPC$MessageMedia TLdeserialize = TLRPC$MessageMedia.TLdeserialize(abstractSerializedData2, abstractSerializedData2.readInt32(z2), z2);
                        this.media = TLdeserialize;
                        if (TLdeserialize != null && !TextUtils.isEmpty(TLdeserialize.captionLegacy)) {
                            this.message = this.media.captionLegacy;
                        }
                        int readInt322 = abstractSerializedData2.readInt32(z2);
                        if (readInt322 != 481674261) {
                            if (z2) {
                                throw new RuntimeException(String.format("wrong Vector magic, got %x", Integer.valueOf(readInt322)));
                            }
                            return;
                        }
                        int readInt323 = abstractSerializedData2.readInt32(z2);
                        for (int i2 = 0; i2 < readInt323; i2++) {
                            TLRPC$MessageEntity TLdeserialize2 = TLRPC$MessageEntity.TLdeserialize(abstractSerializedData2, abstractSerializedData2.readInt32(z2), z2);
                            if (TLdeserialize2 == null) {
                                return;
                            }
                            this.entities.add(TLdeserialize2);
                        }
                        if ((this.flags & 2048) != 0) {
                            this.via_bot_name = abstractSerializedData2.readString(z2);
                        }
                        if ((this.flags & 8) != 0) {
                            TLRPC$TL_messageReplyHeader tLRPC$TL_messageReplyHeader = new TLRPC$TL_messageReplyHeader();
                            this.reply_to = tLRPC$TL_messageReplyHeader;
                            tLRPC$TL_messageReplyHeader.reply_to_random_id = abstractSerializedData2.readInt64(z2);
                        }
                    }

                    @Override // org.telegram.tgnet.TLRPC$TL_message, org.telegram.tgnet.TLObject
                    public void serializeToStream(AbstractSerializedData abstractSerializedData2) {
                        abstractSerializedData2.writeInt32(1431655929);
                        int i2 = this.unread ? this.flags | 1 : this.flags & (-2);
                        this.flags = i2;
                        int i3 = this.out ? i2 | 2 : i2 & (-3);
                        this.flags = i3;
                        int i4 = this.mentioned ? i3 | 16 : i3 & (-17);
                        this.flags = i4;
                        int i5 = this.media_unread ? i4 | 32 : i4 & (-33);
                        this.flags = i5;
                        abstractSerializedData2.writeInt32(i5);
                        abstractSerializedData2.writeInt32(this.id);
                        abstractSerializedData2.writeInt32(this.ttl);
                        abstractSerializedData2.writeInt32((int) this.from_id.user_id);
                        this.peer_id.serializeToStream(abstractSerializedData2);
                        abstractSerializedData2.writeInt32(this.date);
                        abstractSerializedData2.writeString(this.message);
                        this.media.serializeToStream(abstractSerializedData2);
                        abstractSerializedData2.writeInt32(481674261);
                        int size = this.entities.size();
                        abstractSerializedData2.writeInt32(size);
                        for (int i6 = 0; i6 < size; i6++) {
                            this.entities.get(i6).serializeToStream(abstractSerializedData2);
                        }
                        if ((this.flags & 2048) != 0) {
                            abstractSerializedData2.writeString(this.via_bot_name);
                        }
                        if ((this.flags & 8) != 0) {
                            abstractSerializedData2.writeInt64(this.reply_to.reply_to_random_id);
                        }
                        writeAttachPath(abstractSerializedData2);
                    }
                };
                break;
            case 1431655930:
                tLRPC$Message = new TLRPC$TL_message_secret();
                break;
            case 1450613171:
                tLRPC$Message = new TLRPC$TL_message() { // from class: org.telegram.tgnet.TLRPC$TL_message_old2
                    @Override // org.telegram.tgnet.TLRPC$TL_message, org.telegram.tgnet.TLObject
                    public void readParams(AbstractSerializedData abstractSerializedData2, boolean z2) {
                        int readInt32 = abstractSerializedData2.readInt32(z2) | LiteMode.FLAG_CHAT_BLUR | LiteMode.FLAG_CALLS_ANIMATIONS;
                        this.flags = readInt32;
                        this.unread = (readInt32 & 1) != 0;
                        this.out = (readInt32 & 2) != 0;
                        this.mentioned = (readInt32 & 16) != 0;
                        this.media_unread = (readInt32 & 32) != 0;
                        this.id = abstractSerializedData2.readInt32(z2);
                        TLRPC$TL_peerUser tLRPC$TL_peerUser = new TLRPC$TL_peerUser();
                        this.from_id = tLRPC$TL_peerUser;
                        tLRPC$TL_peerUser.user_id = abstractSerializedData2.readInt32(z2);
                        this.peer_id = TLRPC$Peer.TLdeserialize(abstractSerializedData2, abstractSerializedData2.readInt32(z2), z2);
                        this.date = abstractSerializedData2.readInt32(z2);
                        this.message = abstractSerializedData2.readString(z2);
                        TLRPC$MessageMedia TLdeserialize = TLRPC$MessageMedia.TLdeserialize(abstractSerializedData2, abstractSerializedData2.readInt32(z2), z2);
                        this.media = TLdeserialize;
                        if (TLdeserialize == null || TextUtils.isEmpty(TLdeserialize.captionLegacy)) {
                            return;
                        }
                        this.message = this.media.captionLegacy;
                    }

                    @Override // org.telegram.tgnet.TLRPC$TL_message, org.telegram.tgnet.TLObject
                    public void serializeToStream(AbstractSerializedData abstractSerializedData2) {
                        abstractSerializedData2.writeInt32(1450613171);
                        int i2 = this.unread ? this.flags | 1 : this.flags & (-2);
                        this.flags = i2;
                        int i3 = this.out ? i2 | 2 : i2 & (-3);
                        this.flags = i3;
                        int i4 = this.mentioned ? i3 | 16 : i3 & (-17);
                        this.flags = i4;
                        int i5 = this.media_unread ? i4 | 32 : i4 & (-33);
                        this.flags = i5;
                        abstractSerializedData2.writeInt32(i5);
                        abstractSerializedData2.writeInt32(this.id);
                        abstractSerializedData2.writeInt32((int) this.from_id.user_id);
                        this.peer_id.serializeToStream(abstractSerializedData2);
                        abstractSerializedData2.writeInt32(this.date);
                        abstractSerializedData2.writeString(this.message);
                        this.media.serializeToStream(abstractSerializedData2);
                        writeAttachPath(abstractSerializedData2);
                    }
                };
                break;
            case 1487813065:
                tLRPC$Message = new TLRPC$TL_message() { // from class: org.telegram.tgnet.TLRPC$TL_message_layer123
                    @Override // org.telegram.tgnet.TLRPC$TL_message, org.telegram.tgnet.TLObject
                    public void readParams(AbstractSerializedData abstractSerializedData2, boolean z2) {
                        int readInt32 = abstractSerializedData2.readInt32(z2);
                        this.flags = readInt32;
                        this.out = (readInt32 & 2) != 0;
                        this.mentioned = (readInt32 & 16) != 0;
                        this.media_unread = (readInt32 & 32) != 0;
                        this.silent = (readInt32 & LiteMode.FLAG_ANIMATED_EMOJI_REACTIONS_NOT_PREMIUM) != 0;
                        this.post = (readInt32 & LiteMode.FLAG_ANIMATED_EMOJI_KEYBOARD_NOT_PREMIUM) != 0;
                        this.from_scheduled = (262144 & readInt32) != 0;
                        this.legacy = (524288 & readInt32) != 0;
                        this.edit_hide = (2097152 & readInt32) != 0;
                        this.pinned = (readInt32 & ConnectionsManager.FileTypePhoto) != 0;
                        this.id = abstractSerializedData2.readInt32(z2);
                        if ((this.flags & LiteMode.FLAG_CHAT_BLUR) != 0) {
                            this.from_id = TLRPC$Peer.TLdeserialize(abstractSerializedData2, abstractSerializedData2.readInt32(z2), z2);
                        }
                        this.peer_id = TLRPC$Peer.TLdeserialize(abstractSerializedData2, abstractSerializedData2.readInt32(z2), z2);
                        if ((this.flags & 4) != 0) {
                            this.fwd_from = TLRPC$MessageFwdHeader.TLdeserialize(abstractSerializedData2, abstractSerializedData2.readInt32(z2), z2);
                        }
                        if ((this.flags & 2048) != 0) {
                            this.via_bot_id = abstractSerializedData2.readInt32(z2);
                        }
                        if ((this.flags & 8) != 0) {
                            this.reply_to = TLRPC$MessageReplyHeader.TLdeserialize(abstractSerializedData2, abstractSerializedData2.readInt32(z2), z2);
                        }
                        this.date = abstractSerializedData2.readInt32(z2);
                        this.message = abstractSerializedData2.readString(z2);
                        if ((this.flags & LiteMode.FLAG_CALLS_ANIMATIONS) != 0) {
                            TLRPC$MessageMedia TLdeserialize = TLRPC$MessageMedia.TLdeserialize(abstractSerializedData2, abstractSerializedData2.readInt32(z2), z2);
                            this.media = TLdeserialize;
                            if (TLdeserialize != null) {
                                this.ttl = TLdeserialize.ttl_seconds;
                            }
                            if (TLdeserialize != null && !TextUtils.isEmpty(TLdeserialize.captionLegacy)) {
                                this.message = this.media.captionLegacy;
                            }
                        }
                        if ((this.flags & 64) != 0) {
                            this.reply_markup = TLRPC$ReplyMarkup.TLdeserialize(abstractSerializedData2, abstractSerializedData2.readInt32(z2), z2);
                        }
                        if ((this.flags & 128) != 0) {
                            int readInt322 = abstractSerializedData2.readInt32(z2);
                            if (readInt322 != 481674261) {
                                if (z2) {
                                    throw new RuntimeException(String.format("wrong Vector magic, got %x", Integer.valueOf(readInt322)));
                                }
                                return;
                            }
                            int readInt323 = abstractSerializedData2.readInt32(z2);
                            for (int i2 = 0; i2 < readInt323; i2++) {
                                TLRPC$MessageEntity TLdeserialize2 = TLRPC$MessageEntity.TLdeserialize(abstractSerializedData2, abstractSerializedData2.readInt32(z2), z2);
                                if (TLdeserialize2 == null) {
                                    return;
                                }
                                this.entities.add(TLdeserialize2);
                            }
                        }
                        if ((this.flags & 1024) != 0) {
                            this.views = abstractSerializedData2.readInt32(z2);
                        }
                        if ((this.flags & 1024) != 0) {
                            this.forwards = abstractSerializedData2.readInt32(z2);
                        }
                        if ((this.flags & 8388608) != 0) {
                            this.replies = TLRPC$MessageReplies.TLdeserialize(abstractSerializedData2, abstractSerializedData2.readInt32(z2), z2);
                        }
                        if ((this.flags & LiteMode.FLAG_CHAT_SCALE) != 0) {
                            this.edit_date = abstractSerializedData2.readInt32(z2);
                        }
                        if ((this.flags & 65536) != 0) {
                            this.post_author = abstractSerializedData2.readString(z2);
                        }
                        if ((this.flags & 131072) != 0) {
                            this.grouped_id = abstractSerializedData2.readInt64(z2);
                        }
                        if ((this.flags & 4194304) != 0) {
                            int readInt324 = abstractSerializedData2.readInt32(z2);
                            if (readInt324 != 481674261) {
                                if (z2) {
                                    throw new RuntimeException(String.format("wrong Vector magic, got %x", Integer.valueOf(readInt324)));
                                }
                                return;
                            }
                            int readInt325 = abstractSerializedData2.readInt32(z2);
                            for (int i3 = 0; i3 < readInt325; i3++) {
                                TLRPC$TL_restrictionReason TLdeserialize3 = TLRPC$TL_restrictionReason.TLdeserialize(abstractSerializedData2, abstractSerializedData2.readInt32(z2), z2);
                                if (TLdeserialize3 == null) {
                                    return;
                                }
                                this.restriction_reason.add(TLdeserialize3);
                            }
                        }
                    }

                    @Override // org.telegram.tgnet.TLRPC$TL_message, org.telegram.tgnet.TLObject
                    public void serializeToStream(AbstractSerializedData abstractSerializedData2) {
                        abstractSerializedData2.writeInt32(1487813065);
                        int i2 = this.out ? this.flags | 2 : this.flags & (-3);
                        this.flags = i2;
                        int i3 = this.mentioned ? i2 | 16 : i2 & (-17);
                        this.flags = i3;
                        int i4 = this.media_unread ? i3 | 32 : i3 & (-33);
                        this.flags = i4;
                        int i5 = this.silent ? i4 | LiteMode.FLAG_ANIMATED_EMOJI_REACTIONS_NOT_PREMIUM : i4 & (-8193);
                        this.flags = i5;
                        int i6 = this.post ? i5 | LiteMode.FLAG_ANIMATED_EMOJI_KEYBOARD_NOT_PREMIUM : i5 & (-16385);
                        this.flags = i6;
                        int i7 = this.from_scheduled ? i6 | 262144 : i6 & (-262145);
                        this.flags = i7;
                        int i8 = this.legacy ? i7 | 524288 : i7 & (-524289);
                        this.flags = i8;
                        int i9 = this.edit_hide ? i8 | 2097152 : i8 & (-2097153);
                        this.flags = i9;
                        int i10 = this.pinned ? i9 | ConnectionsManager.FileTypePhoto : i9 & (-16777217);
                        this.flags = i10;
                        abstractSerializedData2.writeInt32(i10);
                        abstractSerializedData2.writeInt32(this.id);
                        if ((this.flags & LiteMode.FLAG_CHAT_BLUR) != 0) {
                            this.from_id.serializeToStream(abstractSerializedData2);
                        }
                        this.peer_id.serializeToStream(abstractSerializedData2);
                        if ((this.flags & 4) != 0) {
                            this.fwd_from.serializeToStream(abstractSerializedData2);
                        }
                        if ((this.flags & 2048) != 0) {
                            abstractSerializedData2.writeInt32((int) this.via_bot_id);
                        }
                        if ((this.flags & 8) != 0) {
                            this.reply_to.serializeToStream(abstractSerializedData2);
                        }
                        abstractSerializedData2.writeInt32(this.date);
                        abstractSerializedData2.writeString(this.message);
                        if ((this.flags & LiteMode.FLAG_CALLS_ANIMATIONS) != 0) {
                            this.media.serializeToStream(abstractSerializedData2);
                        }
                        if ((this.flags & 64) != 0) {
                            this.reply_markup.serializeToStream(abstractSerializedData2);
                        }
                        if ((this.flags & 128) != 0) {
                            abstractSerializedData2.writeInt32(481674261);
                            int size = this.entities.size();
                            abstractSerializedData2.writeInt32(size);
                            for (int i11 = 0; i11 < size; i11++) {
                                this.entities.get(i11).serializeToStream(abstractSerializedData2);
                            }
                        }
                        if ((this.flags & 1024) != 0) {
                            abstractSerializedData2.writeInt32(this.views);
                        }
                        if ((this.flags & 1024) != 0) {
                            abstractSerializedData2.writeInt32(this.forwards);
                        }
                        if ((this.flags & 8388608) != 0) {
                            this.replies.serializeToStream(abstractSerializedData2);
                        }
                        if ((this.flags & LiteMode.FLAG_CHAT_SCALE) != 0) {
                            abstractSerializedData2.writeInt32(this.edit_date);
                        }
                        if ((this.flags & 65536) != 0) {
                            abstractSerializedData2.writeString(this.post_author);
                        }
                        if ((this.flags & 131072) != 0) {
                            abstractSerializedData2.writeInt64(this.grouped_id);
                        }
                        if ((this.flags & 4194304) != 0) {
                            abstractSerializedData2.writeInt32(481674261);
                            int size2 = this.restriction_reason.size();
                            abstractSerializedData2.writeInt32(size2);
                            for (int i12 = 0; i12 < size2; i12++) {
                                this.restriction_reason.get(i12).serializeToStream(abstractSerializedData2);
                            }
                        }
                        writeAttachPath(abstractSerializedData2);
                    }
                };
                break;
            case 1537633299:
                tLRPC$Message = new TLRPC$TL_message() { // from class: org.telegram.tgnet.TLRPC$TL_message_old7
                    @Override // org.telegram.tgnet.TLRPC$TL_message, org.telegram.tgnet.TLObject
                    public void readParams(AbstractSerializedData abstractSerializedData2, boolean z2) {
                        int readInt32 = abstractSerializedData2.readInt32(z2);
                        this.flags = readInt32;
                        this.unread = (readInt32 & 1) != 0;
                        this.out = (readInt32 & 2) != 0;
                        this.mentioned = (readInt32 & 16) != 0;
                        this.media_unread = (readInt32 & 32) != 0;
                        this.id = abstractSerializedData2.readInt32(z2);
                        if ((this.flags & LiteMode.FLAG_CHAT_BLUR) != 0) {
                            TLRPC$TL_peerUser tLRPC$TL_peerUser = new TLRPC$TL_peerUser();
                            this.from_id = tLRPC$TL_peerUser;
                            tLRPC$TL_peerUser.user_id = abstractSerializedData2.readInt32(z2);
                        }
                        TLRPC$Peer TLdeserialize = TLRPC$Peer.TLdeserialize(abstractSerializedData2, abstractSerializedData2.readInt32(z2), z2);
                        this.peer_id = TLdeserialize;
                        if (this.from_id == null) {
                            this.from_id = TLdeserialize;
                        }
                        if ((this.flags & 4) != 0) {
                            this.fwd_from = new TLRPC$TL_messageFwdHeader();
                            TLRPC$Peer TLdeserialize2 = TLRPC$Peer.TLdeserialize(abstractSerializedData2, abstractSerializedData2.readInt32(z2), z2);
                            if (TLdeserialize2 != null) {
                                TLRPC$MessageFwdHeader tLRPC$MessageFwdHeader = this.fwd_from;
                                tLRPC$MessageFwdHeader.from_id = TLdeserialize2;
                                tLRPC$MessageFwdHeader.flags |= 1;
                            }
                            this.fwd_from.date = abstractSerializedData2.readInt32(z2);
                        }
                        if ((this.flags & 8) != 0) {
                            TLRPC$TL_messageReplyHeader tLRPC$TL_messageReplyHeader = new TLRPC$TL_messageReplyHeader();
                            this.reply_to = tLRPC$TL_messageReplyHeader;
                            tLRPC$TL_messageReplyHeader.flags |= 16;
                            tLRPC$TL_messageReplyHeader.reply_to_msg_id = abstractSerializedData2.readInt32(z2);
                        }
                        this.date = abstractSerializedData2.readInt32(z2);
                        this.message = abstractSerializedData2.readString(z2);
                        if ((this.flags & LiteMode.FLAG_CALLS_ANIMATIONS) != 0) {
                            TLRPC$MessageMedia TLdeserialize3 = TLRPC$MessageMedia.TLdeserialize(abstractSerializedData2, abstractSerializedData2.readInt32(z2), z2);
                            this.media = TLdeserialize3;
                            if (TLdeserialize3 != null && !TextUtils.isEmpty(TLdeserialize3.captionLegacy)) {
                                this.message = this.media.captionLegacy;
                            }
                        } else {
                            this.media = new TLRPC$TL_messageMediaEmpty();
                        }
                        if ((this.flags & 64) != 0) {
                            this.reply_markup = TLRPC$ReplyMarkup.TLdeserialize(abstractSerializedData2, abstractSerializedData2.readInt32(z2), z2);
                        }
                        if ((this.flags & 128) != 0) {
                            int readInt322 = abstractSerializedData2.readInt32(z2);
                            if (readInt322 != 481674261) {
                                if (z2) {
                                    throw new RuntimeException(String.format("wrong Vector magic, got %x", Integer.valueOf(readInt322)));
                                }
                                return;
                            }
                            int readInt323 = abstractSerializedData2.readInt32(z2);
                            for (int i2 = 0; i2 < readInt323; i2++) {
                                TLRPC$MessageEntity TLdeserialize4 = TLRPC$MessageEntity.TLdeserialize(abstractSerializedData2, abstractSerializedData2.readInt32(z2), z2);
                                if (TLdeserialize4 == null) {
                                    return;
                                }
                                this.entities.add(TLdeserialize4);
                            }
                        }
                        if ((this.flags & 1024) != 0) {
                            this.views = abstractSerializedData2.readInt32(z2);
                        }
                    }

                    @Override // org.telegram.tgnet.TLRPC$TL_message, org.telegram.tgnet.TLObject
                    public void serializeToStream(AbstractSerializedData abstractSerializedData2) {
                        abstractSerializedData2.writeInt32(1537633299);
                        int i2 = this.unread ? this.flags | 1 : this.flags & (-2);
                        this.flags = i2;
                        int i3 = this.out ? i2 | 2 : i2 & (-3);
                        this.flags = i3;
                        int i4 = this.mentioned ? i3 | 16 : i3 & (-17);
                        this.flags = i4;
                        int i5 = this.media_unread ? i4 | 32 : i4 & (-33);
                        this.flags = i5;
                        abstractSerializedData2.writeInt32(i5);
                        abstractSerializedData2.writeInt32(this.id);
                        if ((this.flags & LiteMode.FLAG_CHAT_BLUR) != 0) {
                            abstractSerializedData2.writeInt32((int) this.from_id.user_id);
                        }
                        this.peer_id.serializeToStream(abstractSerializedData2);
                        if ((this.flags & 4) != 0) {
                            TLRPC$Peer tLRPC$Peer = this.fwd_from.from_id;
                            if (tLRPC$Peer != null) {
                                tLRPC$Peer.serializeToStream(abstractSerializedData2);
                            }
                            abstractSerializedData2.writeInt32(this.fwd_from.date);
                        }
                        if ((this.flags & 8) != 0) {
                            abstractSerializedData2.writeInt32(this.reply_to.reply_to_msg_id);
                        }
                        abstractSerializedData2.writeInt32(this.date);
                        abstractSerializedData2.writeString(this.message);
                        if ((this.flags & LiteMode.FLAG_CALLS_ANIMATIONS) != 0) {
                            this.media.serializeToStream(abstractSerializedData2);
                        }
                        if ((this.flags & 64) != 0) {
                            this.reply_markup.serializeToStream(abstractSerializedData2);
                        }
                        if ((this.flags & 128) != 0) {
                            abstractSerializedData2.writeInt32(481674261);
                            int size = this.entities.size();
                            abstractSerializedData2.writeInt32(size);
                            for (int i6 = 0; i6 < size; i6++) {
                                this.entities.get(i6).serializeToStream(abstractSerializedData2);
                            }
                        }
                        if ((this.flags & 1024) != 0) {
                            abstractSerializedData2.writeInt32(this.views);
                        }
                        writeAttachPath(abstractSerializedData2);
                    }
                };
                break;
            case 1992213009:
                tLRPC$Message = new TLRPC$TL_message() { // from class: org.telegram.tgnet.TLRPC$TL_message_layer173
                    @Override // org.telegram.tgnet.TLRPC$TL_message, org.telegram.tgnet.TLObject
                    public void readParams(AbstractSerializedData abstractSerializedData2, boolean z2) {
                        int readInt32 = abstractSerializedData2.readInt32(z2);
                        this.flags = readInt32;
                        this.out = (readInt32 & 2) != 0;
                        this.mentioned = (readInt32 & 16) != 0;
                        this.media_unread = (readInt32 & 32) != 0;
                        this.silent = (readInt32 & LiteMode.FLAG_ANIMATED_EMOJI_REACTIONS_NOT_PREMIUM) != 0;
                        this.post = (readInt32 & LiteMode.FLAG_ANIMATED_EMOJI_KEYBOARD_NOT_PREMIUM) != 0;
                        this.from_scheduled = (262144 & readInt32) != 0;
                        this.legacy = (524288 & readInt32) != 0;
                        this.edit_hide = (2097152 & readInt32) != 0;
                        this.pinned = (16777216 & readInt32) != 0;
                        this.noforwards = (67108864 & readInt32) != 0;
                        this.invert_media = (readInt32 & 134217728) != 0;
                        this.id = abstractSerializedData2.readInt32(z2);
                        if ((this.flags & LiteMode.FLAG_CHAT_BLUR) != 0) {
                            this.from_id = TLRPC$Peer.TLdeserialize(abstractSerializedData2, abstractSerializedData2.readInt32(z2), z2);
                        }
                        this.peer_id = TLRPC$Peer.TLdeserialize(abstractSerializedData2, abstractSerializedData2.readInt32(z2), z2);
                        if ((this.flags & 268435456) != 0) {
                            this.saved_peer_id = TLRPC$Peer.TLdeserialize(abstractSerializedData2, abstractSerializedData2.readInt32(z2), z2);
                        }
                        if ((this.flags & 4) != 0) {
                            this.fwd_from = TLRPC$MessageFwdHeader.TLdeserialize(abstractSerializedData2, abstractSerializedData2.readInt32(z2), z2);
                        }
                        if ((this.flags & 2048) != 0) {
                            this.via_bot_id = abstractSerializedData2.readInt64(z2);
                        }
                        if ((this.flags & 8) != 0) {
                            this.reply_to = TLRPC$MessageReplyHeader.TLdeserialize(abstractSerializedData2, abstractSerializedData2.readInt32(z2), z2);
                        }
                        this.date = abstractSerializedData2.readInt32(z2);
                        this.message = abstractSerializedData2.readString(z2);
                        if ((this.flags & LiteMode.FLAG_CALLS_ANIMATIONS) != 0) {
                            TLRPC$MessageMedia TLdeserialize = TLRPC$MessageMedia.TLdeserialize(abstractSerializedData2, abstractSerializedData2.readInt32(z2), z2);
                            this.media = TLdeserialize;
                            if (TLdeserialize != null) {
                                this.ttl = TLdeserialize.ttl_seconds;
                            }
                            if (TLdeserialize != null && !TextUtils.isEmpty(TLdeserialize.captionLegacy)) {
                                this.message = this.media.captionLegacy;
                            }
                        }
                        if ((this.flags & 64) != 0) {
                            this.reply_markup = TLRPC$ReplyMarkup.TLdeserialize(abstractSerializedData2, abstractSerializedData2.readInt32(z2), z2);
                        }
                        if ((this.flags & 128) != 0) {
                            int readInt322 = abstractSerializedData2.readInt32(z2);
                            if (readInt322 != 481674261) {
                                if (z2) {
                                    throw new RuntimeException(String.format("wrong Vector magic, got %x", Integer.valueOf(readInt322)));
                                }
                                return;
                            }
                            int readInt323 = abstractSerializedData2.readInt32(z2);
                            for (int i2 = 0; i2 < readInt323; i2++) {
                                TLRPC$MessageEntity TLdeserialize2 = TLRPC$MessageEntity.TLdeserialize(abstractSerializedData2, abstractSerializedData2.readInt32(z2), z2);
                                if (TLdeserialize2 == null) {
                                    return;
                                }
                                this.entities.add(TLdeserialize2);
                            }
                        }
                        if ((this.flags & 1024) != 0) {
                            this.views = abstractSerializedData2.readInt32(z2);
                        }
                        if ((this.flags & 1024) != 0) {
                            this.forwards = abstractSerializedData2.readInt32(z2);
                        }
                        if ((this.flags & 8388608) != 0) {
                            this.replies = TLRPC$MessageReplies.TLdeserialize(abstractSerializedData2, abstractSerializedData2.readInt32(z2), z2);
                        }
                        if ((this.flags & LiteMode.FLAG_CHAT_SCALE) != 0) {
                            this.edit_date = abstractSerializedData2.readInt32(z2);
                        }
                        if ((this.flags & 65536) != 0) {
                            this.post_author = abstractSerializedData2.readString(z2);
                        }
                        if ((this.flags & 131072) != 0) {
                            this.grouped_id = abstractSerializedData2.readInt64(z2);
                        }
                        if ((this.flags & FileLoaderPriorityQueue.PRIORITY_VALUE_MAX) != 0) {
                            this.reactions = TLRPC$MessageReactions.TLdeserialize(abstractSerializedData2, abstractSerializedData2.readInt32(z2), z2);
                        }
                        if ((this.flags & 4194304) != 0) {
                            int readInt324 = abstractSerializedData2.readInt32(z2);
                            if (readInt324 != 481674261) {
                                if (z2) {
                                    throw new RuntimeException(String.format("wrong Vector magic, got %x", Integer.valueOf(readInt324)));
                                }
                                return;
                            }
                            int readInt325 = abstractSerializedData2.readInt32(z2);
                            for (int i3 = 0; i3 < readInt325; i3++) {
                                TLRPC$TL_restrictionReason TLdeserialize3 = TLRPC$TL_restrictionReason.TLdeserialize(abstractSerializedData2, abstractSerializedData2.readInt32(z2), z2);
                                if (TLdeserialize3 == null) {
                                    return;
                                }
                                this.restriction_reason.add(TLdeserialize3);
                            }
                        }
                        if ((this.flags & ConnectionsManager.FileTypeVideo) != 0) {
                            this.ttl_period = abstractSerializedData2.readInt32(z2);
                        }
                    }

                    @Override // org.telegram.tgnet.TLRPC$TL_message, org.telegram.tgnet.TLObject
                    public void serializeToStream(AbstractSerializedData abstractSerializedData2) {
                        abstractSerializedData2.writeInt32(1992213009);
                        int i2 = this.out ? this.flags | 2 : this.flags & (-3);
                        this.flags = i2;
                        int i3 = this.mentioned ? i2 | 16 : i2 & (-17);
                        this.flags = i3;
                        int i4 = this.media_unread ? i3 | 32 : i3 & (-33);
                        this.flags = i4;
                        int i5 = this.silent ? i4 | LiteMode.FLAG_ANIMATED_EMOJI_REACTIONS_NOT_PREMIUM : i4 & (-8193);
                        this.flags = i5;
                        int i6 = this.post ? i5 | LiteMode.FLAG_ANIMATED_EMOJI_KEYBOARD_NOT_PREMIUM : i5 & (-16385);
                        this.flags = i6;
                        int i7 = this.from_scheduled ? i6 | 262144 : i6 & (-262145);
                        this.flags = i7;
                        int i8 = this.legacy ? i7 | 524288 : i7 & (-524289);
                        this.flags = i8;
                        int i9 = this.edit_hide ? i8 | 2097152 : i8 & (-2097153);
                        this.flags = i9;
                        int i10 = this.pinned ? i9 | ConnectionsManager.FileTypePhoto : i9 & (-16777217);
                        this.flags = i10;
                        int i11 = this.noforwards ? i10 | ConnectionsManager.FileTypeFile : i10 & (-67108865);
                        this.flags = i11;
                        int i12 = this.invert_media ? i11 | 134217728 : i11 & (-134217729);
                        this.flags = i12;
                        abstractSerializedData2.writeInt32(i12);
                        abstractSerializedData2.writeInt32(this.id);
                        if ((this.flags & LiteMode.FLAG_CHAT_BLUR) != 0) {
                            this.from_id.serializeToStream(abstractSerializedData2);
                        }
                        this.peer_id.serializeToStream(abstractSerializedData2);
                        if ((this.flags & 268435456) != 0) {
                            this.saved_peer_id.serializeToStream(abstractSerializedData2);
                        }
                        if ((this.flags & 4) != 0) {
                            this.fwd_from.serializeToStream(abstractSerializedData2);
                        }
                        if ((this.flags & 2048) != 0) {
                            abstractSerializedData2.writeInt64(this.via_bot_id);
                        }
                        if ((this.flags & 8) != 0) {
                            this.reply_to.serializeToStream(abstractSerializedData2);
                        }
                        abstractSerializedData2.writeInt32(this.date);
                        abstractSerializedData2.writeString(this.message);
                        if ((this.flags & LiteMode.FLAG_CALLS_ANIMATIONS) != 0) {
                            this.media.serializeToStream(abstractSerializedData2);
                        }
                        if ((this.flags & 64) != 0) {
                            this.reply_markup.serializeToStream(abstractSerializedData2);
                        }
                        if ((this.flags & 128) != 0) {
                            abstractSerializedData2.writeInt32(481674261);
                            int size = this.entities.size();
                            abstractSerializedData2.writeInt32(size);
                            for (int i13 = 0; i13 < size; i13++) {
                                this.entities.get(i13).serializeToStream(abstractSerializedData2);
                            }
                        }
                        if ((this.flags & 1024) != 0) {
                            abstractSerializedData2.writeInt32(this.views);
                        }
                        if ((this.flags & 1024) != 0) {
                            abstractSerializedData2.writeInt32(this.forwards);
                        }
                        if ((this.flags & 8388608) != 0) {
                            this.replies.serializeToStream(abstractSerializedData2);
                        }
                        if ((this.flags & LiteMode.FLAG_CHAT_SCALE) != 0) {
                            abstractSerializedData2.writeInt32(this.edit_date);
                        }
                        if ((this.flags & 65536) != 0) {
                            abstractSerializedData2.writeString(this.post_author);
                        }
                        if ((this.flags & 131072) != 0) {
                            abstractSerializedData2.writeInt64(this.grouped_id);
                        }
                        if ((this.flags & FileLoaderPriorityQueue.PRIORITY_VALUE_MAX) != 0) {
                            this.reactions.serializeToStream(abstractSerializedData2);
                        }
                        if ((this.flags & 4194304) != 0) {
                            abstractSerializedData2.writeInt32(481674261);
                            int size2 = this.restriction_reason.size();
                            abstractSerializedData2.writeInt32(size2);
                            for (int i14 = 0; i14 < size2; i14++) {
                                this.restriction_reason.get(i14).serializeToStream(abstractSerializedData2);
                            }
                        }
                        if ((this.flags & ConnectionsManager.FileTypeVideo) != 0) {
                            abstractSerializedData2.writeInt32(this.ttl_period);
                        }
                        writeAttachPath(abstractSerializedData2);
                    }
                };
                break;
            default:
                tLRPC$Message = null;
                break;
        }
        if (tLRPC$Message == null && z) {
            throw new RuntimeException(String.format("can't parse magic %x in Message", Integer.valueOf(i)));
        }
        if (tLRPC$Message != null) {
            tLRPC$Message.readParams(abstractSerializedData, z);
            if (tLRPC$Message.from_id == null) {
                tLRPC$Message.from_id = (tLRPC$Message.id >= 0 || tLRPC$Message.random_id != 0) ? tLRPC$Message.peer_id : new TLRPC$TL_peerUser();
            }
        }
        return tLRPC$Message;
    }

    /* JADX WARN: Code restructure failed: missing block: B:39:0x005d, code lost:
        if (r9 == r13) goto L92;
     */
    /* JADX WARN: Code restructure failed: missing block: B:44:0x0067, code lost:
        if (r11.send_state != 3) goto L37;
     */
    /* JADX WARN: Code restructure failed: missing block: B:46:0x006b, code lost:
        if (r11.legacy != false) goto L39;
     */
    /* JADX WARN: Removed duplicated region for block: B:30:0x0045  */
    /* JADX WARN: Removed duplicated region for block: B:42:0x0063  */
    /* JADX WARN: Removed duplicated region for block: B:62:0x00aa  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void readAttachPath(AbstractSerializedData abstractSerializedData, long j) {
        boolean z;
        TLRPC$Peer tLRPC$Peer;
        TLRPC$MessageMedia tLRPC$MessageMedia = this.media;
        boolean z2 = (tLRPC$MessageMedia == null || (tLRPC$MessageMedia instanceof TLRPC$TL_messageMediaEmpty) || (tLRPC$MessageMedia instanceof TLRPC$TL_messageMediaWebPage)) ? false : true;
        if (!TextUtils.isEmpty(this.message)) {
            TLRPC$MessageMedia tLRPC$MessageMedia2 = this.media;
            if (((tLRPC$MessageMedia2 instanceof TLRPC$TL_messageMediaPhoto_old) || (tLRPC$MessageMedia2 instanceof TLRPC$TL_messageMediaPhoto_layer68) || (tLRPC$MessageMedia2 instanceof TLRPC$TL_messageMediaPhoto_layer74) || (tLRPC$MessageMedia2 instanceof TLRPC$TL_messageMediaDocument_old) || (tLRPC$MessageMedia2 instanceof TLRPC$TL_messageMediaDocument_layer68) || (tLRPC$MessageMedia2 instanceof TLRPC$TL_messageMediaDocument_layer74)) && this.message.startsWith("-1")) {
                z = true;
                if (!this.out) {
                    TLRPC$Peer tLRPC$Peer2 = this.peer_id;
                    if (tLRPC$Peer2 != null && (tLRPC$Peer = this.from_id) != null) {
                        long j2 = tLRPC$Peer2.user_id;
                        if (j2 != 0) {
                            long j3 = tLRPC$Peer.user_id;
                            if (j2 == j3) {
                            }
                        }
                    }
                }
                if (this.id >= 0) {
                    if (!z2) {
                    }
                }
                if (z2 && z) {
                    if (this.message.length() > 6 && this.message.charAt(2) == '_') {
                        HashMap<String, String> hashMap = new HashMap<>();
                        this.params = hashMap;
                        hashMap.put("ve", this.message);
                    }
                    if (this.params == null || this.message.length() == 2) {
                        this.message = "";
                    }
                }
                if (abstractSerializedData.remaining() > 0) {
                    String readString = abstractSerializedData.readString(false);
                    this.attachPath = readString;
                    if (readString != null) {
                        if ((this.id < 0 || this.send_state == 3 || this.legacy) && readString.startsWith("||")) {
                            String[] split = this.attachPath.split("\\|\\|");
                            if (split.length > 0) {
                                if (this.params == null) {
                                    this.params = new HashMap<>();
                                }
                                for (int i = 1; i < split.length - 1; i++) {
                                    String[] split2 = split[i].split("\\|=\\|");
                                    if (split2.length == 2) {
                                        this.params.put(split2[0], split2[1]);
                                    }
                                }
                                this.attachPath = split[split.length - 1].trim();
                                if (this.legacy) {
                                    this.layer = Utilities.parseInt((CharSequence) this.params.get("legacy_layer")).intValue();
                                }
                            }
                        } else {
                            this.attachPath = this.attachPath.trim();
                        }
                    }
                }
                if ((this.flags & 4) != 0 || this.id >= 0) {
                }
                this.fwd_msg_id = abstractSerializedData.readInt32(false);
                return;
            }
        }
        z = false;
        if (!this.out) {
        }
        if (this.id >= 0) {
        }
        if (z2) {
            if (this.message.length() > 6) {
                HashMap<String, String> hashMap2 = new HashMap<>();
                this.params = hashMap2;
                hashMap2.put("ve", this.message);
            }
            if (this.params == null) {
            }
            this.message = "";
        }
        if (abstractSerializedData.remaining() > 0) {
        }
        if ((this.flags & 4) != 0) {
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void writeAttachPath(AbstractSerializedData abstractSerializedData) {
        HashMap<String, String> hashMap;
        HashMap<String, String> hashMap2;
        if ((this instanceof TLRPC$TL_message_secret) || (this instanceof TLRPC$TL_message_secret_layer72)) {
            String str = this.attachPath;
            if (str == null) {
                str = "";
            }
            if (this.send_state == 1 && (hashMap = this.params) != null && hashMap.size() > 0) {
                for (Map.Entry<String, String> entry : this.params.entrySet()) {
                    str = entry.getKey() + "|=|" + entry.getValue() + "||" + str;
                }
                str = "||" + str;
            }
            abstractSerializedData.writeString(str);
            return;
        }
        String str2 = !TextUtils.isEmpty(this.attachPath) ? this.attachPath : " ";
        if (this.legacy) {
            if (this.params == null) {
                this.params = new HashMap<>();
            }
            this.layer = 178;
            this.params.put("legacy_layer", "178");
        }
        if ((this.id < 0 || this.send_state == 3 || this.legacy) && (hashMap2 = this.params) != null && hashMap2.size() > 0) {
            for (Map.Entry<String, String> entry2 : this.params.entrySet()) {
                str2 = entry2.getKey() + "|=|" + entry2.getValue() + "||" + str2;
            }
            str2 = "||" + str2;
        }
        abstractSerializedData.writeString(str2);
        if ((this.flags & 4) == 0 || this.id >= 0) {
            return;
        }
        abstractSerializedData.writeInt32(this.fwd_msg_id);
    }
}
