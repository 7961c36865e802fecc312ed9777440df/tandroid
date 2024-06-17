package org.telegram.tgnet;
/* loaded from: classes3.dex */
public class TLRPC$TL_inputStorePaymentStars extends TLRPC$InputStorePaymentPurpose {
    public long amount;
    public String currency;
    public int flags;
    public long stars;

    @Override // org.telegram.tgnet.TLObject
    public void readParams(AbstractSerializedData abstractSerializedData, boolean z) {
        this.flags = abstractSerializedData.readInt32(z);
        this.stars = abstractSerializedData.readInt64(z);
        this.currency = abstractSerializedData.readString(z);
        this.amount = abstractSerializedData.readInt64(z);
    }

    @Override // org.telegram.tgnet.TLObject
    public void serializeToStream(AbstractSerializedData abstractSerializedData) {
        abstractSerializedData.writeInt32(1326377183);
        abstractSerializedData.writeInt32(this.flags);
        abstractSerializedData.writeInt64(this.stars);
        abstractSerializedData.writeString(this.currency);
        abstractSerializedData.writeInt64(this.amount);
    }
}