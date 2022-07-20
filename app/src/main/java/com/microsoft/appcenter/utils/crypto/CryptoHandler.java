package com.microsoft.appcenter.utils.crypto;

import android.content.Context;
import com.microsoft.appcenter.utils.crypto.CryptoUtils;
import java.security.KeyStore;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public interface CryptoHandler {
    byte[] decrypt(CryptoUtils.ICryptoFactory iCryptoFactory, int i, KeyStore.Entry entry, byte[] bArr) throws Exception;

    byte[] encrypt(CryptoUtils.ICryptoFactory iCryptoFactory, int i, KeyStore.Entry entry, byte[] bArr) throws Exception;

    void generateKey(CryptoUtils.ICryptoFactory iCryptoFactory, String str, Context context) throws Exception;

    String getAlgorithm();
}