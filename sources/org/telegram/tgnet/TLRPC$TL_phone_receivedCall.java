package org.telegram.tgnet;
/* loaded from: classes3.dex */
public class TLRPC$TL_phone_receivedCall extends TLObject {
    public TLRPC$TL_inputPhoneCall peer;

    @Override // org.telegram.tgnet.TLObject
    public TLObject deserializeResponse(AbstractSerializedData abstractSerializedData, int i, boolean z) {
        return TLRPC$Bool.TLdeserialize(abstractSerializedData, i, z);
    }

    @Override // org.telegram.tgnet.TLObject
    public void serializeToStream(AbstractSerializedData abstractSerializedData) {
        abstractSerializedData.writeInt32(399855457);
        this.peer.serializeToStream(abstractSerializedData);
    }
}
