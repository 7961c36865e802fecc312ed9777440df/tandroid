package org.telegram.tgnet;
/* loaded from: classes3.dex */
public class TLRPC$TL_account_checkUsername extends TLObject {
    public String username;

    @Override // org.telegram.tgnet.TLObject
    public TLObject deserializeResponse(AbstractSerializedData abstractSerializedData, int i, boolean z) {
        return TLRPC$Bool.TLdeserialize(abstractSerializedData, i, z);
    }

    @Override // org.telegram.tgnet.TLObject
    public void serializeToStream(AbstractSerializedData abstractSerializedData) {
        abstractSerializedData.writeInt32(655677548);
        abstractSerializedData.writeString(this.username);
    }
}
