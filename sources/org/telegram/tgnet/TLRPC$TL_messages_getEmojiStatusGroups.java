package org.telegram.tgnet;
/* loaded from: classes3.dex */
public class TLRPC$TL_messages_getEmojiStatusGroups extends TLObject {
    public int hash;

    @Override // org.telegram.tgnet.TLObject
    public TLObject deserializeResponse(AbstractSerializedData abstractSerializedData, int i, boolean z) {
        return TLRPC$messages_EmojiGroups.TLdeserialize(abstractSerializedData, i, z);
    }

    @Override // org.telegram.tgnet.TLObject
    public void serializeToStream(AbstractSerializedData abstractSerializedData) {
        abstractSerializedData.writeInt32(785209037);
        abstractSerializedData.writeInt32(this.hash);
    }
}
