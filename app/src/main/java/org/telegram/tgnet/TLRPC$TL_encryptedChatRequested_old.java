package org.telegram.tgnet;
/* loaded from: classes.dex */
public class TLRPC$TL_encryptedChatRequested_old extends TLRPC$TL_encryptedChatRequested {
    public static int constructor = -39213129;

    @Override // org.telegram.tgnet.TLRPC$TL_encryptedChatRequested, org.telegram.tgnet.TLObject
    public void readParams(AbstractSerializedData abstractSerializedData, boolean z) {
        this.id = abstractSerializedData.readInt32(z);
        this.access_hash = abstractSerializedData.readInt64(z);
        this.date = abstractSerializedData.readInt32(z);
        this.admin_id = abstractSerializedData.readInt32(z);
        this.participant_id = abstractSerializedData.readInt32(z);
        this.g_a = abstractSerializedData.readByteArray(z);
        this.nonce = abstractSerializedData.readByteArray(z);
    }

    @Override // org.telegram.tgnet.TLRPC$TL_encryptedChatRequested, org.telegram.tgnet.TLObject
    public void serializeToStream(AbstractSerializedData abstractSerializedData) {
        abstractSerializedData.writeInt32(constructor);
        abstractSerializedData.writeInt32(this.id);
        abstractSerializedData.writeInt64(this.access_hash);
        abstractSerializedData.writeInt32(this.date);
        abstractSerializedData.writeInt32((int) this.admin_id);
        abstractSerializedData.writeInt32((int) this.participant_id);
        abstractSerializedData.writeByteArray(this.g_a);
        abstractSerializedData.writeByteArray(this.nonce);
    }
}