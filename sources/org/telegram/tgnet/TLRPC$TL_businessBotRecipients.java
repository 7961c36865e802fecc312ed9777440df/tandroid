package org.telegram.tgnet;

import java.util.ArrayList;
/* loaded from: classes3.dex */
public class TLRPC$TL_businessBotRecipients extends TLObject {
    public boolean contacts;
    public boolean exclude_selected;
    public boolean existing_chats;
    public int flags;
    public boolean new_chats;
    public boolean non_contacts;
    public ArrayList<Long> users = new ArrayList<>();
    public ArrayList<Long> exclude_users = new ArrayList<>();

    public static TLRPC$TL_businessBotRecipients TLdeserialize(AbstractSerializedData abstractSerializedData, int i, boolean z) {
        if (i != -1198722189) {
            if (z) {
                throw new RuntimeException(String.format("can't parse magic %x in TL_businessBotRecipients", Integer.valueOf(i)));
            }
            return null;
        }
        TLRPC$TL_businessBotRecipients tLRPC$TL_businessBotRecipients = new TLRPC$TL_businessBotRecipients();
        tLRPC$TL_businessBotRecipients.readParams(abstractSerializedData, z);
        return tLRPC$TL_businessBotRecipients;
    }

    @Override // org.telegram.tgnet.TLObject
    public void readParams(AbstractSerializedData abstractSerializedData, boolean z) {
        int readInt32 = abstractSerializedData.readInt32(z);
        this.flags = readInt32;
        this.existing_chats = (readInt32 & 1) != 0;
        this.new_chats = (readInt32 & 2) != 0;
        this.contacts = (readInt32 & 4) != 0;
        this.non_contacts = (readInt32 & 8) != 0;
        this.exclude_selected = (readInt32 & 32) != 0;
        if ((readInt32 & 16) != 0) {
            int readInt322 = abstractSerializedData.readInt32(z);
            if (readInt322 != 481674261) {
                if (z) {
                    throw new RuntimeException(String.format("wrong Vector magic, got %x", Integer.valueOf(readInt322)));
                }
                return;
            }
            int readInt323 = abstractSerializedData.readInt32(z);
            for (int i = 0; i < readInt323; i++) {
                this.users.add(Long.valueOf(abstractSerializedData.readInt64(z)));
            }
        }
        if ((this.flags & 64) != 0) {
            int readInt324 = abstractSerializedData.readInt32(z);
            if (readInt324 != 481674261) {
                if (z) {
                    throw new RuntimeException(String.format("wrong Vector magic, got %x", Integer.valueOf(readInt324)));
                }
                return;
            }
            int readInt325 = abstractSerializedData.readInt32(z);
            for (int i2 = 0; i2 < readInt325; i2++) {
                this.exclude_users.add(Long.valueOf(abstractSerializedData.readInt64(z)));
            }
        }
    }

    @Override // org.telegram.tgnet.TLObject
    public void serializeToStream(AbstractSerializedData abstractSerializedData) {
        abstractSerializedData.writeInt32(-1198722189);
        int i = this.existing_chats ? this.flags | 1 : this.flags & (-2);
        this.flags = i;
        int i2 = this.new_chats ? i | 2 : i & (-3);
        this.flags = i2;
        int i3 = this.contacts ? i2 | 4 : i2 & (-5);
        this.flags = i3;
        int i4 = this.non_contacts ? i3 | 8 : i3 & (-9);
        this.flags = i4;
        int i5 = this.exclude_selected ? i4 | 32 : i4 & (-33);
        this.flags = i5;
        abstractSerializedData.writeInt32(i5);
        if ((this.flags & 16) != 0) {
            abstractSerializedData.writeInt32(481674261);
            int size = this.users.size();
            abstractSerializedData.writeInt32(size);
            for (int i6 = 0; i6 < size; i6++) {
                abstractSerializedData.writeInt64(this.users.get(i6).longValue());
            }
        }
        if ((this.flags & 64) != 0) {
            abstractSerializedData.writeInt32(481674261);
            int size2 = this.exclude_users.size();
            abstractSerializedData.writeInt32(size2);
            for (int i7 = 0; i7 < size2; i7++) {
                abstractSerializedData.writeInt64(this.exclude_users.get(i7).longValue());
            }
        }
    }
}