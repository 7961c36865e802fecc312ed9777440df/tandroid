package org.telegram.tgnet;

import android.text.TextUtils;
/* loaded from: classes.dex */
public class TLRPC$TL_message_old extends TLRPC$TL_message {
    public static int constructor = 585853626;

    @Override // org.telegram.tgnet.TLRPC$TL_message, org.telegram.tgnet.TLObject
    public void readParams(AbstractSerializedData abstractSerializedData, boolean z) {
        this.id = abstractSerializedData.readInt32(z);
        TLRPC$TL_peerUser tLRPC$TL_peerUser = new TLRPC$TL_peerUser();
        this.from_id = tLRPC$TL_peerUser;
        tLRPC$TL_peerUser.user_id = abstractSerializedData.readInt32(z);
        this.peer_id = TLRPC$Peer.TLdeserialize(abstractSerializedData, abstractSerializedData.readInt32(z), z);
        this.out = abstractSerializedData.readBool(z);
        this.unread = abstractSerializedData.readBool(z);
        this.flags |= 768;
        this.date = abstractSerializedData.readInt32(z);
        this.message = abstractSerializedData.readString(z);
        TLRPC$MessageMedia TLdeserialize = TLRPC$MessageMedia.TLdeserialize(abstractSerializedData, abstractSerializedData.readInt32(z), z);
        this.media = TLdeserialize;
        if (TLdeserialize == null || TextUtils.isEmpty(TLdeserialize.captionLegacy)) {
            return;
        }
        this.message = this.media.captionLegacy;
    }

    @Override // org.telegram.tgnet.TLRPC$TL_message, org.telegram.tgnet.TLObject
    public void serializeToStream(AbstractSerializedData abstractSerializedData) {
        abstractSerializedData.writeInt32(constructor);
        abstractSerializedData.writeInt32(this.id);
        abstractSerializedData.writeInt32((int) this.from_id.user_id);
        this.peer_id.serializeToStream(abstractSerializedData);
        abstractSerializedData.writeBool(this.out);
        abstractSerializedData.writeBool(this.unread);
        abstractSerializedData.writeInt32(this.date);
        abstractSerializedData.writeString(this.message);
        this.media.serializeToStream(abstractSerializedData);
        writeAttachPath(abstractSerializedData);
    }
}