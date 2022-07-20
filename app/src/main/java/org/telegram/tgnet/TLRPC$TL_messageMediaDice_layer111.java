package org.telegram.tgnet;
/* loaded from: classes.dex */
public class TLRPC$TL_messageMediaDice_layer111 extends TLRPC$TL_messageMediaDice {
    public static int constructor = 1670374507;

    @Override // org.telegram.tgnet.TLRPC$TL_messageMediaDice, org.telegram.tgnet.TLObject
    public void readParams(AbstractSerializedData abstractSerializedData, boolean z) {
        this.value = abstractSerializedData.readInt32(z);
    }

    @Override // org.telegram.tgnet.TLRPC$TL_messageMediaDice, org.telegram.tgnet.TLObject
    public void serializeToStream(AbstractSerializedData abstractSerializedData) {
        abstractSerializedData.writeInt32(constructor);
        abstractSerializedData.writeInt32(this.value);
    }
}