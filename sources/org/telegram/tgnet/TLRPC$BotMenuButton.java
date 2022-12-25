package org.telegram.tgnet;
/* loaded from: classes.dex */
public abstract class TLRPC$BotMenuButton extends TLObject {
    public static TLRPC$BotMenuButton TLdeserialize(AbstractSerializedData abstractSerializedData, int i, boolean z) {
        TLRPC$BotMenuButton tLRPC$TL_botMenuButton;
        if (i == -944407322) {
            tLRPC$TL_botMenuButton = new TLRPC$TL_botMenuButton();
        } else if (i != 1113113093) {
            tLRPC$TL_botMenuButton = i != 1966318984 ? null : new TLRPC$BotMenuButton() { // from class: org.telegram.tgnet.TLRPC$TL_botMenuButtonDefault
                public static int constructor = 1966318984;

                @Override // org.telegram.tgnet.TLObject
                public void serializeToStream(AbstractSerializedData abstractSerializedData2) {
                    abstractSerializedData2.writeInt32(constructor);
                }
            };
        } else {
            tLRPC$TL_botMenuButton = new TLRPC$BotMenuButton() { // from class: org.telegram.tgnet.TLRPC$TL_botMenuButtonCommands
                public static int constructor = 1113113093;

                @Override // org.telegram.tgnet.TLObject
                public void serializeToStream(AbstractSerializedData abstractSerializedData2) {
                    abstractSerializedData2.writeInt32(constructor);
                }
            };
        }
        if (tLRPC$TL_botMenuButton == null && z) {
            throw new RuntimeException(String.format("can't parse magic %x in BotMenuButton", Integer.valueOf(i)));
        }
        if (tLRPC$TL_botMenuButton != null) {
            tLRPC$TL_botMenuButton.readParams(abstractSerializedData, z);
        }
        return tLRPC$TL_botMenuButton;
    }
}
