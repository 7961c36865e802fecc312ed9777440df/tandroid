package org.telegram.messenger;

import android.content.SharedPreferences;
import android.net.Uri;
import android.util.SparseArray;
import android.util.SparseIntArray;
import java.io.File;
import java.io.FileDescriptor;
import java.io.RandomAccessFile;
import java.security.MessageDigest;
import java.util.ArrayList;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.tgnet.NativeByteBuffer;
import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC$InputEncryptedFile;
import org.telegram.tgnet.TLRPC$InputFile;
import org.telegram.tgnet.TLRPC$TL_boolTrue;
import org.telegram.tgnet.TLRPC$TL_error;
import org.telegram.tgnet.TLRPC$TL_inputFile;
import org.telegram.tgnet.TLRPC$TL_inputFileBig;
import org.telegram.tgnet.TLRPC$TL_upload_saveBigFilePart;
import org.telegram.tgnet.TLRPC$TL_upload_saveFilePart;
import org.telegram.tgnet.WriteToSocketDelegate;
/* loaded from: classes.dex */
public class FileUploadOperation {
    private static final int initialRequestsCount = 8;
    private static final int initialRequestsSlowNetworkCount = 1;
    private static final int maxUploadingKBytes = 2048;
    private static final int maxUploadingSlowNetworkKBytes = 32;
    private static final int minUploadChunkSize = 128;
    private static final int minUploadChunkSlowNetworkSize = 32;
    private long availableSize;
    private int currentAccount;
    private long currentFileId;
    private int currentPartNum;
    private int currentType;
    private int currentUploadRequetsCount;
    private FileUploadOperationDelegate delegate;
    private long estimatedSize;
    private String fileKey;
    private int fingerprint;
    private boolean forceSmallFile;
    private ArrayList<byte[]> freeRequestIvs;
    private boolean isBigFile;
    private boolean isEncrypted;
    private boolean isLastPart;
    private byte[] iv;
    private byte[] ivChange;
    private byte[] key;
    protected long lastProgressUpdateTime;
    private int lastSavedPartNum;
    private int maxRequestsCount;
    private boolean nextPartFirst;
    private int operationGuid;
    private SharedPreferences preferences;
    private byte[] readBuffer;
    private long readBytesCount;
    private int requestNum;
    private int saveInfoTimes;
    private boolean slowNetwork;
    private boolean started;
    private int state;
    private RandomAccessFile stream;
    private long totalFileSize;
    private int totalPartsCount;
    private boolean uploadFirstPartLater;
    private int uploadStartTime;
    private long uploadedBytesCount;
    private String uploadingFilePath;
    private int uploadChunkSize = CharacterCompat.MIN_SUPPLEMENTARY_CODE_POINT;
    private SparseIntArray requestTokens = new SparseIntArray();
    private SparseArray<UploadCachedResult> cachedResults = new SparseArray<>();

    /* loaded from: classes.dex */
    public interface FileUploadOperationDelegate {
        void didChangedUploadProgress(FileUploadOperation fileUploadOperation, long j, long j2);

        void didFailedUploadingFile(FileUploadOperation fileUploadOperation);

        void didFinishUploadingFile(FileUploadOperation fileUploadOperation, TLRPC$InputFile tLRPC$InputFile, TLRPC$InputEncryptedFile tLRPC$InputEncryptedFile, byte[] bArr, byte[] bArr2);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static class UploadCachedResult {
        private long bytesOffset;
        private byte[] iv;

        private UploadCachedResult() {
        }
    }

    public FileUploadOperation(int i, String str, boolean z, long j, int i2) {
        this.currentAccount = i;
        this.uploadingFilePath = str;
        this.isEncrypted = z;
        this.estimatedSize = j;
        this.currentType = i2;
        this.uploadFirstPartLater = j != 0 && !z;
    }

    public long getTotalFileSize() {
        return this.totalFileSize;
    }

    public void setDelegate(FileUploadOperationDelegate fileUploadOperationDelegate) {
        this.delegate = fileUploadOperationDelegate;
    }

    public void start() {
        if (this.state != 0) {
            return;
        }
        this.state = 1;
        Utilities.stageQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.FileUploadOperation$$ExternalSyntheticLambda2
            @Override // java.lang.Runnable
            public final void run() {
                FileUploadOperation.this.lambda$start$0();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$start$0() {
        this.preferences = ApplicationLoader.applicationContext.getSharedPreferences("uploadinfo", 0);
        this.slowNetwork = ApplicationLoader.isConnectionSlow();
        if (BuildVars.LOGS_ENABLED) {
            FileLog.d("start upload on slow network = " + this.slowNetwork);
        }
        int i = this.slowNetwork ? 1 : 8;
        for (int i2 = 0; i2 < i; i2++) {
            startUploadRequest();
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void onNetworkChanged(final boolean z) {
        if (this.state != 1) {
            return;
        }
        Utilities.stageQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.FileUploadOperation$$ExternalSyntheticLambda4
            @Override // java.lang.Runnable
            public final void run() {
                FileUploadOperation.this.lambda$onNetworkChanged$1(z);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onNetworkChanged$1(boolean z) {
        int i;
        if (this.slowNetwork != z) {
            this.slowNetwork = z;
            if (BuildVars.LOGS_ENABLED) {
                FileLog.d("network changed to slow = " + this.slowNetwork);
            }
            int i2 = 0;
            while (true) {
                i = 1;
                if (i2 >= this.requestTokens.size()) {
                    break;
                }
                ConnectionsManager.getInstance(this.currentAccount).cancelRequest(this.requestTokens.valueAt(i2), true);
                i2++;
            }
            this.requestTokens.clear();
            cleanup();
            this.isLastPart = false;
            this.nextPartFirst = false;
            this.requestNum = 0;
            this.currentPartNum = 0;
            this.readBytesCount = 0L;
            this.uploadedBytesCount = 0L;
            this.saveInfoTimes = 0;
            this.key = null;
            this.iv = null;
            this.ivChange = null;
            this.currentUploadRequetsCount = 0;
            this.lastSavedPartNum = 0;
            this.uploadFirstPartLater = false;
            this.cachedResults.clear();
            this.operationGuid++;
            if (!this.slowNetwork) {
                i = 8;
            }
            for (int i3 = 0; i3 < i; i3++) {
                startUploadRequest();
            }
        }
    }

    public void cancel() {
        if (this.state == 3) {
            return;
        }
        this.state = 2;
        Utilities.stageQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.FileUploadOperation$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                FileUploadOperation.this.lambda$cancel$2();
            }
        });
        this.delegate.didFailedUploadingFile(this);
        cleanup();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$cancel$2() {
        for (int i = 0; i < this.requestTokens.size(); i++) {
            ConnectionsManager.getInstance(this.currentAccount).cancelRequest(this.requestTokens.valueAt(i), true);
        }
    }

    private void cleanup() {
        if (this.preferences == null) {
            this.preferences = ApplicationLoader.applicationContext.getSharedPreferences("uploadinfo", 0);
        }
        SharedPreferences.Editor edit = this.preferences.edit();
        SharedPreferences.Editor remove = edit.remove(this.fileKey + "_time");
        SharedPreferences.Editor remove2 = remove.remove(this.fileKey + "_size");
        SharedPreferences.Editor remove3 = remove2.remove(this.fileKey + "_uploaded");
        SharedPreferences.Editor remove4 = remove3.remove(this.fileKey + "_id");
        SharedPreferences.Editor remove5 = remove4.remove(this.fileKey + "_iv");
        SharedPreferences.Editor remove6 = remove5.remove(this.fileKey + "_key");
        remove6.remove(this.fileKey + "_ivc").commit();
        try {
            RandomAccessFile randomAccessFile = this.stream;
            if (randomAccessFile == null) {
                return;
            }
            randomAccessFile.close();
            this.stream = null;
        } catch (Exception e) {
            FileLog.e(e);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void checkNewDataAvailable(final long j, final long j2) {
        Utilities.stageQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.FileUploadOperation$$ExternalSyntheticLambda3
            @Override // java.lang.Runnable
            public final void run() {
                FileUploadOperation.this.lambda$checkNewDataAvailable$3(j2, j);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$checkNewDataAvailable$3(long j, long j2) {
        if (this.estimatedSize != 0 && j != 0) {
            this.estimatedSize = 0L;
            this.totalFileSize = j;
            calcTotalPartsCount();
            if (!this.uploadFirstPartLater && this.started) {
                storeFileUploadInfo();
            }
        }
        if (j <= 0) {
            j = j2;
        }
        this.availableSize = j;
        if (this.currentUploadRequetsCount < this.maxRequestsCount) {
            startUploadRequest();
        }
    }

    private void storeFileUploadInfo() {
        SharedPreferences.Editor edit = this.preferences.edit();
        edit.putInt(this.fileKey + "_time", this.uploadStartTime);
        edit.putLong(this.fileKey + "_size", this.totalFileSize);
        edit.putLong(this.fileKey + "_id", this.currentFileId);
        edit.remove(this.fileKey + "_uploaded");
        if (this.isEncrypted) {
            edit.putString(this.fileKey + "_iv", Utilities.bytesToHex(this.iv));
            edit.putString(this.fileKey + "_ivc", Utilities.bytesToHex(this.ivChange));
            edit.putString(this.fileKey + "_key", Utilities.bytesToHex(this.key));
        }
        edit.commit();
    }

    private void calcTotalPartsCount() {
        if (this.uploadFirstPartLater) {
            if (this.isBigFile) {
                long j = this.totalFileSize;
                int i = this.uploadChunkSize;
                this.totalPartsCount = ((int) ((((j - i) + i) - 1) / i)) + 1;
                return;
            }
            int i2 = this.uploadChunkSize;
            this.totalPartsCount = ((int) ((((this.totalFileSize - 1024) + i2) - 1) / i2)) + 1;
            return;
        }
        long j2 = this.totalFileSize;
        int i3 = this.uploadChunkSize;
        this.totalPartsCount = (int) (((j2 + i3) - 1) / i3);
    }

    public void setForceSmallFile() {
        this.forceSmallFile = true;
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:121:0x02ed A[Catch: Exception -> 0x0508, TryCatch #2 {Exception -> 0x0508, blocks: (B:6:0x0008, B:8:0x0016, B:11:0x0029, B:14:0x005a, B:16:0x0060, B:17:0x0069, B:19:0x006d, B:21:0x0076, B:22:0x0078, B:24:0x0091, B:26:0x009a, B:27:0x00a3, B:30:0x00ac, B:33:0x00c7, B:35:0x00cb, B:37:0x00ce, B:38:0x00d0, B:41:0x00d9, B:43:0x00e6, B:44:0x00f0, B:46:0x00f4, B:48:0x00fe, B:51:0x0120, B:53:0x0155, B:55:0x0159, B:57:0x015f, B:59:0x0165, B:61:0x01b7, B:64:0x01ed, B:67:0x01ff, B:69:0x0202, B:71:0x0205, B:75:0x0215, B:77:0x0219, B:83:0x0239, B:86:0x0246, B:88:0x0251, B:90:0x025d, B:92:0x0261, B:93:0x0269, B:95:0x0274, B:97:0x027d, B:101:0x028a, B:103:0x0291, B:105:0x02a8, B:107:0x027b, B:110:0x02b0, B:112:0x02b9, B:114:0x02d4, B:116:0x02dc, B:118:0x02df, B:119:0x02e5, B:121:0x02ed, B:123:0x02f1, B:124:0x0311, B:126:0x031d, B:128:0x0321, B:130:0x0327, B:131:0x032a, B:133:0x0361, B:135:0x036d, B:137:0x0371, B:138:0x0388, B:139:0x037f, B:149:0x035e, B:151:0x0225, B:159:0x0063, B:160:0x038b, B:161:0x0390, B:162:0x0391, B:163:0x0396, B:164:0x0397, B:166:0x039d, B:169:0x03aa, B:171:0x03ae, B:173:0x03b7, B:174:0x03cc, B:178:0x03dc, B:180:0x03e0, B:182:0x03e4, B:183:0x03ec, B:185:0x03f7, B:187:0x03fb, B:189:0x0401, B:191:0x0414, B:195:0x0421, B:197:0x0428, B:198:0x0455, B:200:0x0459, B:202:0x046c, B:203:0x0473, B:204:0x048b, B:206:0x048f, B:208:0x0493, B:209:0x04a4, B:221:0x046f, B:222:0x0479, B:224:0x0408, B:226:0x040c, B:227:0x0412, B:229:0x03c1, B:230:0x03cf, B:141:0x032e, B:144:0x0349, B:12:0x0032), top: B:5:0x0008, inners: #0 }] */
    /* JADX WARN: Removed duplicated region for block: B:135:0x036d A[Catch: Exception -> 0x0508, TryCatch #2 {Exception -> 0x0508, blocks: (B:6:0x0008, B:8:0x0016, B:11:0x0029, B:14:0x005a, B:16:0x0060, B:17:0x0069, B:19:0x006d, B:21:0x0076, B:22:0x0078, B:24:0x0091, B:26:0x009a, B:27:0x00a3, B:30:0x00ac, B:33:0x00c7, B:35:0x00cb, B:37:0x00ce, B:38:0x00d0, B:41:0x00d9, B:43:0x00e6, B:44:0x00f0, B:46:0x00f4, B:48:0x00fe, B:51:0x0120, B:53:0x0155, B:55:0x0159, B:57:0x015f, B:59:0x0165, B:61:0x01b7, B:64:0x01ed, B:67:0x01ff, B:69:0x0202, B:71:0x0205, B:75:0x0215, B:77:0x0219, B:83:0x0239, B:86:0x0246, B:88:0x0251, B:90:0x025d, B:92:0x0261, B:93:0x0269, B:95:0x0274, B:97:0x027d, B:101:0x028a, B:103:0x0291, B:105:0x02a8, B:107:0x027b, B:110:0x02b0, B:112:0x02b9, B:114:0x02d4, B:116:0x02dc, B:118:0x02df, B:119:0x02e5, B:121:0x02ed, B:123:0x02f1, B:124:0x0311, B:126:0x031d, B:128:0x0321, B:130:0x0327, B:131:0x032a, B:133:0x0361, B:135:0x036d, B:137:0x0371, B:138:0x0388, B:139:0x037f, B:149:0x035e, B:151:0x0225, B:159:0x0063, B:160:0x038b, B:161:0x0390, B:162:0x0391, B:163:0x0396, B:164:0x0397, B:166:0x039d, B:169:0x03aa, B:171:0x03ae, B:173:0x03b7, B:174:0x03cc, B:178:0x03dc, B:180:0x03e0, B:182:0x03e4, B:183:0x03ec, B:185:0x03f7, B:187:0x03fb, B:189:0x0401, B:191:0x0414, B:195:0x0421, B:197:0x0428, B:198:0x0455, B:200:0x0459, B:202:0x046c, B:203:0x0473, B:204:0x048b, B:206:0x048f, B:208:0x0493, B:209:0x04a4, B:221:0x046f, B:222:0x0479, B:224:0x0408, B:226:0x040c, B:227:0x0412, B:229:0x03c1, B:230:0x03cf, B:141:0x032e, B:144:0x0349, B:12:0x0032), top: B:5:0x0008, inners: #0 }] */
    /* JADX WARN: Removed duplicated region for block: B:140:0x032e A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:81:0x0235  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private void startUploadRequest() {
        int read;
        byte[] bArr;
        final int i;
        TLRPC$TL_upload_saveFilePart tLRPC$TL_upload_saveFilePart;
        int i2;
        boolean z;
        boolean z2;
        if (this.state != 1) {
            return;
        }
        try {
            this.started = true;
            if (this.stream == null) {
                File file = new File(this.uploadingFilePath);
                if (AndroidUtilities.isInternalUri(Uri.fromFile(file))) {
                    throw new Exception("trying to upload internal file");
                }
                this.stream = new RandomAccessFile(file, "r");
                if (AndroidUtilities.isInternalUri(((Integer) FileDescriptor.class.getDeclaredMethod("getInt$", new Class[0]).invoke(this.stream.getFD(), new Object[0])).intValue())) {
                    throw new Exception("trying to upload internal file");
                }
                long j = this.estimatedSize;
                if (j != 0) {
                    this.totalFileSize = j;
                } else {
                    this.totalFileSize = file.length();
                }
                if (!this.forceSmallFile && this.totalFileSize > 10485760) {
                    this.isBigFile = true;
                }
                long j2 = MessagesController.getInstance(this.currentAccount).uploadMaxFileParts;
                if (AccountInstance.getInstance(this.currentAccount).getUserConfig().isPremium() && this.totalFileSize > FileLoader.DEFAULT_MAX_FILE_SIZE) {
                    j2 = MessagesController.getInstance(this.currentAccount).uploadMaxFilePartsPremium;
                }
                long j3 = j2 * 1024;
                int max = (int) Math.max(this.slowNetwork ? 32L : 128L, ((this.totalFileSize + j3) - 1) / j3);
                this.uploadChunkSize = max;
                if (ConnectionsManager.RequestFlagDoNotWaitFloodWait % max != 0) {
                    int i3 = 64;
                    while (this.uploadChunkSize > i3) {
                        i3 *= 2;
                    }
                    this.uploadChunkSize = i3;
                }
                this.maxRequestsCount = Math.max(1, (this.slowNetwork ? 32 : maxUploadingKBytes) / this.uploadChunkSize);
                if (this.isEncrypted) {
                    this.freeRequestIvs = new ArrayList<>(this.maxRequestsCount);
                    for (int i4 = 0; i4 < this.maxRequestsCount; i4++) {
                        this.freeRequestIvs.add(new byte[32]);
                    }
                }
                this.uploadChunkSize *= ConnectionsManager.RequestFlagDoNotWaitFloodWait;
                calcTotalPartsCount();
                this.readBuffer = new byte[this.uploadChunkSize];
                StringBuilder sb = new StringBuilder();
                sb.append(this.uploadingFilePath);
                sb.append(this.isEncrypted ? "enc" : "");
                this.fileKey = Utilities.MD5(sb.toString());
                long j4 = this.preferences.getLong(this.fileKey + "_size", 0L);
                this.uploadStartTime = (int) (System.currentTimeMillis() / 1000);
                if (!this.uploadFirstPartLater && !this.nextPartFirst && this.estimatedSize == 0 && j4 == this.totalFileSize) {
                    this.currentFileId = this.preferences.getLong(this.fileKey + "_id", 0L);
                    int i5 = this.preferences.getInt(this.fileKey + "_time", 0);
                    long j5 = this.preferences.getLong(this.fileKey + "_uploaded", 0L);
                    if (this.isEncrypted) {
                        String string = this.preferences.getString(this.fileKey + "_iv", null);
                        String string2 = this.preferences.getString(this.fileKey + "_key", null);
                        if (string != null && string2 != null) {
                            this.key = Utilities.hexToBytes(string2);
                            byte[] hexToBytes = Utilities.hexToBytes(string);
                            this.iv = hexToBytes;
                            byte[] bArr2 = this.key;
                            if (bArr2 != null && hexToBytes != null && bArr2.length == 32 && hexToBytes.length == 32) {
                                byte[] bArr3 = new byte[32];
                                this.ivChange = bArr3;
                                System.arraycopy(hexToBytes, 0, bArr3, 0, 32);
                            }
                        }
                        z = true;
                        if (!z && i5 != 0) {
                            z2 = this.isBigFile;
                            if (!z2) {
                            }
                            if (!z2 && i5 < this.uploadStartTime - 5400.0f) {
                                i5 = 0;
                            }
                            if (i5 != 0) {
                                if (j5 > 0) {
                                    this.readBytesCount = j5;
                                    this.currentPartNum = (int) (j5 / this.uploadChunkSize);
                                    if (!z2) {
                                        for (int i6 = 0; i6 < this.readBytesCount / this.uploadChunkSize; i6++) {
                                            int read2 = this.stream.read(this.readBuffer);
                                            int i7 = (!this.isEncrypted || read2 % 16 == 0) ? 0 : (16 - (read2 % 16)) + 0;
                                            int i8 = read2 + i7;
                                            NativeByteBuffer nativeByteBuffer = new NativeByteBuffer(i8);
                                            if (read2 != this.uploadChunkSize || this.totalPartsCount == this.currentPartNum + 1) {
                                                this.isLastPart = true;
                                            }
                                            int i9 = 0;
                                            nativeByteBuffer.writeBytes(this.readBuffer, 0, read2);
                                            if (this.isEncrypted) {
                                                int i10 = 0;
                                                while (i10 < i7) {
                                                    nativeByteBuffer.writeByte(i9);
                                                    i10++;
                                                    i9 = 0;
                                                }
                                                Utilities.aesIgeEncryption(nativeByteBuffer.buffer, this.key, this.ivChange, true, true, 0, i8);
                                            }
                                            nativeByteBuffer.reuse();
                                        }
                                    } else {
                                        this.stream.seek(j5);
                                        if (this.isEncrypted) {
                                            String string3 = this.preferences.getString(this.fileKey + "_ivc", null);
                                            if (string3 != null) {
                                                byte[] hexToBytes2 = Utilities.hexToBytes(string3);
                                                this.ivChange = hexToBytes2;
                                                if (hexToBytes2 == null || hexToBytes2.length != 32) {
                                                    this.readBytesCount = 0L;
                                                    this.currentPartNum = 0;
                                                }
                                            } else {
                                                this.readBytesCount = 0L;
                                                this.currentPartNum = 0;
                                            }
                                        }
                                    }
                                }
                            }
                            if (z) {
                                if (this.isEncrypted) {
                                    byte[] bArr4 = new byte[32];
                                    this.iv = bArr4;
                                    this.key = new byte[32];
                                    this.ivChange = new byte[32];
                                    Utilities.random.nextBytes(bArr4);
                                    Utilities.random.nextBytes(this.key);
                                    System.arraycopy(this.iv, 0, this.ivChange, 0, 32);
                                }
                                this.currentFileId = Utilities.random.nextLong();
                                if (!this.nextPartFirst && !this.uploadFirstPartLater && this.estimatedSize == 0) {
                                    storeFileUploadInfo();
                                }
                            }
                            if (this.isEncrypted) {
                                try {
                                    MessageDigest messageDigest = MessageDigest.getInstance("MD5");
                                    byte[] bArr5 = new byte[64];
                                    System.arraycopy(this.key, 0, bArr5, 0, 32);
                                    System.arraycopy(this.iv, 0, bArr5, 32, 32);
                                    byte[] digest = messageDigest.digest(bArr5);
                                    for (int i11 = 0; i11 < 4; i11++) {
                                        this.fingerprint |= ((digest[i11] ^ digest[i11 + 4]) & 255) << (i11 * 8);
                                    }
                                } catch (Exception e) {
                                    FileLog.e(e);
                                }
                            }
                            this.uploadedBytesCount = this.readBytesCount;
                            this.lastSavedPartNum = this.currentPartNum;
                            if (this.uploadFirstPartLater) {
                                if (this.isBigFile) {
                                    this.stream.seek(this.uploadChunkSize);
                                    this.readBytesCount = this.uploadChunkSize;
                                } else {
                                    this.stream.seek(1024L);
                                    this.readBytesCount = 1024L;
                                }
                                this.currentPartNum = 1;
                            }
                        }
                    }
                    z = false;
                    if (!z) {
                        z2 = this.isBigFile;
                        if (!z2 || i5 >= this.uploadStartTime - 86400) {
                            if (!z2) {
                            }
                            if (i5 != 0) {
                            }
                            if (z) {
                            }
                            if (this.isEncrypted) {
                            }
                            this.uploadedBytesCount = this.readBytesCount;
                            this.lastSavedPartNum = this.currentPartNum;
                            if (this.uploadFirstPartLater) {
                            }
                        }
                        i5 = 0;
                        if (i5 != 0) {
                        }
                        if (z) {
                        }
                        if (this.isEncrypted) {
                        }
                        this.uploadedBytesCount = this.readBytesCount;
                        this.lastSavedPartNum = this.currentPartNum;
                        if (this.uploadFirstPartLater) {
                        }
                    }
                }
                z = true;
                if (z) {
                }
                if (this.isEncrypted) {
                }
                this.uploadedBytesCount = this.readBytesCount;
                this.lastSavedPartNum = this.currentPartNum;
                if (this.uploadFirstPartLater) {
                }
            }
            if (this.estimatedSize != 0 && this.readBytesCount + this.uploadChunkSize > this.availableSize) {
                return;
            }
            if (this.nextPartFirst) {
                this.stream.seek(0L);
                if (this.isBigFile) {
                    read = this.stream.read(this.readBuffer);
                    i2 = 0;
                } else {
                    i2 = 0;
                    read = this.stream.read(this.readBuffer, 0, ConnectionsManager.RequestFlagDoNotWaitFloodWait);
                }
                this.currentPartNum = i2;
            } else {
                read = this.stream.read(this.readBuffer);
            }
            final int i12 = read;
            if (i12 == -1) {
                return;
            }
            int i13 = (!this.isEncrypted || i12 % 16 == 0) ? 0 : (16 - (i12 % 16)) + 0;
            int i14 = i12 + i13;
            NativeByteBuffer nativeByteBuffer2 = new NativeByteBuffer(i14);
            if (this.nextPartFirst || i12 != this.uploadChunkSize || (this.estimatedSize == 0 && this.totalPartsCount == this.currentPartNum + 1)) {
                if (this.uploadFirstPartLater) {
                    this.nextPartFirst = true;
                    this.uploadFirstPartLater = false;
                } else {
                    this.isLastPart = true;
                }
            }
            int i15 = 0;
            nativeByteBuffer2.writeBytes(this.readBuffer, 0, i12);
            if (this.isEncrypted) {
                int i16 = 0;
                while (i16 < i13) {
                    nativeByteBuffer2.writeByte(i15);
                    i16++;
                    i15 = 0;
                }
                Utilities.aesIgeEncryption(nativeByteBuffer2.buffer, this.key, this.ivChange, true, true, 0, i14);
                byte[] bArr6 = this.freeRequestIvs.get(0);
                System.arraycopy(this.ivChange, 0, bArr6, 0, 32);
                this.freeRequestIvs.remove(0);
                bArr = bArr6;
            } else {
                bArr = null;
            }
            if (this.isBigFile) {
                TLRPC$TL_upload_saveBigFilePart tLRPC$TL_upload_saveBigFilePart = new TLRPC$TL_upload_saveBigFilePart();
                int i17 = this.currentPartNum;
                tLRPC$TL_upload_saveBigFilePart.file_part = i17;
                tLRPC$TL_upload_saveBigFilePart.file_id = this.currentFileId;
                if (this.estimatedSize != 0) {
                    tLRPC$TL_upload_saveBigFilePart.file_total_parts = -1;
                } else {
                    tLRPC$TL_upload_saveBigFilePart.file_total_parts = this.totalPartsCount;
                }
                tLRPC$TL_upload_saveBigFilePart.bytes = nativeByteBuffer2;
                tLRPC$TL_upload_saveFilePart = tLRPC$TL_upload_saveBigFilePart;
                i = i17;
            } else {
                TLRPC$TL_upload_saveFilePart tLRPC$TL_upload_saveFilePart2 = new TLRPC$TL_upload_saveFilePart();
                int i18 = this.currentPartNum;
                tLRPC$TL_upload_saveFilePart2.file_part = i18;
                tLRPC$TL_upload_saveFilePart2.file_id = this.currentFileId;
                tLRPC$TL_upload_saveFilePart2.bytes = nativeByteBuffer2;
                i = i18;
                tLRPC$TL_upload_saveFilePart = tLRPC$TL_upload_saveFilePart2;
            }
            if (this.isLastPart && this.nextPartFirst) {
                this.nextPartFirst = false;
                this.currentPartNum = this.totalPartsCount - 1;
                this.stream.seek(this.totalFileSize);
            }
            this.readBytesCount += i12;
            this.currentPartNum++;
            this.currentUploadRequetsCount++;
            final int i19 = this.requestNum;
            this.requestNum = i19 + 1;
            final long j6 = i + i12;
            final int objectSize = tLRPC$TL_upload_saveFilePart.getObjectSize() + 4;
            final int i20 = this.operationGuid;
            final byte[] bArr7 = bArr;
            this.requestTokens.put(i19, ConnectionsManager.getInstance(this.currentAccount).sendRequest(tLRPC$TL_upload_saveFilePart, new RequestDelegate() { // from class: org.telegram.messenger.FileUploadOperation$$ExternalSyntheticLambda5
                @Override // org.telegram.tgnet.RequestDelegate
                public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
                    FileUploadOperation.this.lambda$startUploadRequest$4(i20, objectSize, bArr7, i19, i12, i, j6, tLObject, tLRPC$TL_error);
                }
            }, null, new WriteToSocketDelegate() { // from class: org.telegram.messenger.FileUploadOperation$$ExternalSyntheticLambda6
                @Override // org.telegram.tgnet.WriteToSocketDelegate
                public final void run() {
                    FileUploadOperation.this.lambda$startUploadRequest$6();
                }
            }, this.forceSmallFile ? 4 : 0, ConnectionsManager.DEFAULT_DATACENTER_ID, this.slowNetwork ? 4 : ((i19 % 4) << 16) | 4, true));
        } catch (Exception e2) {
            FileLog.e(e2);
            this.state = 4;
            this.delegate.didFailedUploadingFile(this);
            cleanup();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$startUploadRequest$4(int i, int i2, byte[] bArr, int i3, int i4, int i5, long j, TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        long j2;
        TLRPC$InputEncryptedFile tLRPC$InputEncryptedFile;
        TLRPC$InputFile tLRPC$TL_inputFile;
        byte[] bArr2 = bArr;
        if (i != this.operationGuid) {
            return;
        }
        int currentNetworkType = tLObject != null ? tLObject.networkType : ApplicationLoader.getCurrentNetworkType();
        int i6 = this.currentType;
        if (i6 == 50331648) {
            StatsController.getInstance(this.currentAccount).incrementSentBytesCount(currentNetworkType, 3, i2);
        } else if (i6 == 33554432) {
            StatsController.getInstance(this.currentAccount).incrementSentBytesCount(currentNetworkType, 2, i2);
        } else if (i6 == 16777216) {
            StatsController.getInstance(this.currentAccount).incrementSentBytesCount(currentNetworkType, 4, i2);
        } else if (i6 == 67108864) {
            StatsController.getInstance(this.currentAccount).incrementSentBytesCount(currentNetworkType, 5, i2);
        }
        if (bArr2 != null) {
            this.freeRequestIvs.add(bArr2);
        }
        this.requestTokens.delete(i3);
        if (tLObject instanceof TLRPC$TL_boolTrue) {
            if (this.state != 1) {
                return;
            }
            this.uploadedBytesCount += i4;
            long j3 = this.estimatedSize;
            if (j3 != 0) {
                j2 = Math.max(this.availableSize, j3);
            } else {
                j2 = this.totalFileSize;
            }
            this.delegate.didChangedUploadProgress(this, this.uploadedBytesCount, j2);
            int i7 = this.currentUploadRequetsCount - 1;
            this.currentUploadRequetsCount = i7;
            if (this.isLastPart && i7 == 0 && this.state == 1) {
                this.state = 3;
                if (this.key == null) {
                    if (this.isBigFile) {
                        tLRPC$TL_inputFile = new TLRPC$TL_inputFileBig();
                    } else {
                        tLRPC$TL_inputFile = new TLRPC$TL_inputFile();
                        tLRPC$TL_inputFile.md5_checksum = "";
                    }
                    tLRPC$TL_inputFile.parts = this.currentPartNum;
                    tLRPC$TL_inputFile.id = this.currentFileId;
                    String str = this.uploadingFilePath;
                    tLRPC$TL_inputFile.name = str.substring(str.lastIndexOf("/") + 1);
                    this.delegate.didFinishUploadingFile(this, tLRPC$TL_inputFile, null, null, null);
                    cleanup();
                } else {
                    if (this.isBigFile) {
                        tLRPC$InputEncryptedFile = new TLRPC$InputEncryptedFile() { // from class: org.telegram.tgnet.TLRPC$TL_inputEncryptedFileBigUploaded
                            public static int constructor = 767652808;

                            @Override // org.telegram.tgnet.TLObject
                            public void readParams(AbstractSerializedData abstractSerializedData, boolean z) {
                                this.id = abstractSerializedData.readInt64(z);
                                this.parts = abstractSerializedData.readInt32(z);
                                this.key_fingerprint = abstractSerializedData.readInt32(z);
                            }

                            @Override // org.telegram.tgnet.TLObject
                            public void serializeToStream(AbstractSerializedData abstractSerializedData) {
                                abstractSerializedData.writeInt32(constructor);
                                abstractSerializedData.writeInt64(this.id);
                                abstractSerializedData.writeInt32(this.parts);
                                abstractSerializedData.writeInt32(this.key_fingerprint);
                            }
                        };
                    } else {
                        tLRPC$InputEncryptedFile = new TLRPC$InputEncryptedFile() { // from class: org.telegram.tgnet.TLRPC$TL_inputEncryptedFileUploaded
                            public static int constructor = 1690108678;

                            @Override // org.telegram.tgnet.TLObject
                            public void readParams(AbstractSerializedData abstractSerializedData, boolean z) {
                                this.id = abstractSerializedData.readInt64(z);
                                this.parts = abstractSerializedData.readInt32(z);
                                this.md5_checksum = abstractSerializedData.readString(z);
                                this.key_fingerprint = abstractSerializedData.readInt32(z);
                            }

                            @Override // org.telegram.tgnet.TLObject
                            public void serializeToStream(AbstractSerializedData abstractSerializedData) {
                                abstractSerializedData.writeInt32(constructor);
                                abstractSerializedData.writeInt64(this.id);
                                abstractSerializedData.writeInt32(this.parts);
                                abstractSerializedData.writeString(this.md5_checksum);
                                abstractSerializedData.writeInt32(this.key_fingerprint);
                            }
                        };
                        tLRPC$InputEncryptedFile.md5_checksum = "";
                    }
                    tLRPC$InputEncryptedFile.parts = this.currentPartNum;
                    tLRPC$InputEncryptedFile.id = this.currentFileId;
                    tLRPC$InputEncryptedFile.key_fingerprint = this.fingerprint;
                    this.delegate.didFinishUploadingFile(this, null, tLRPC$InputEncryptedFile, this.key, this.iv);
                    cleanup();
                }
                int i8 = this.currentType;
                if (i8 == 50331648) {
                    StatsController.getInstance(this.currentAccount).incrementSentItemsCount(ApplicationLoader.getCurrentNetworkType(), 3, 1);
                    return;
                } else if (i8 == 33554432) {
                    StatsController.getInstance(this.currentAccount).incrementSentItemsCount(ApplicationLoader.getCurrentNetworkType(), 2, 1);
                    return;
                } else if (i8 == 16777216) {
                    StatsController.getInstance(this.currentAccount).incrementSentItemsCount(ApplicationLoader.getCurrentNetworkType(), 4, 1);
                    return;
                } else if (i8 != 67108864) {
                    return;
                } else {
                    StatsController.getInstance(this.currentAccount).incrementSentItemsCount(ApplicationLoader.getCurrentNetworkType(), 5, 1);
                    return;
                }
            } else if (i7 >= this.maxRequestsCount) {
                return;
            } else {
                if (this.estimatedSize == 0 && !this.uploadFirstPartLater && !this.nextPartFirst) {
                    if (this.saveInfoTimes >= 4) {
                        this.saveInfoTimes = 0;
                    }
                    int i9 = this.lastSavedPartNum;
                    if (i5 == i9) {
                        this.lastSavedPartNum = i9 + 1;
                        long j4 = j;
                        while (true) {
                            UploadCachedResult uploadCachedResult = this.cachedResults.get(this.lastSavedPartNum);
                            if (uploadCachedResult == null) {
                                break;
                            }
                            j4 = uploadCachedResult.bytesOffset;
                            bArr2 = uploadCachedResult.iv;
                            this.cachedResults.remove(this.lastSavedPartNum);
                            this.lastSavedPartNum++;
                        }
                        boolean z = this.isBigFile;
                        if ((z && j4 % 1048576 == 0) || (!z && this.saveInfoTimes == 0)) {
                            SharedPreferences.Editor edit = this.preferences.edit();
                            edit.putLong(this.fileKey + "_uploaded", j4);
                            if (this.isEncrypted) {
                                edit.putString(this.fileKey + "_ivc", Utilities.bytesToHex(bArr2));
                            }
                            edit.commit();
                        }
                    } else {
                        UploadCachedResult uploadCachedResult2 = new UploadCachedResult();
                        uploadCachedResult2.bytesOffset = j;
                        if (bArr2 != null) {
                            uploadCachedResult2.iv = new byte[32];
                            System.arraycopy(bArr2, 0, uploadCachedResult2.iv, 0, 32);
                        }
                        this.cachedResults.put(i5, uploadCachedResult2);
                    }
                    this.saveInfoTimes++;
                }
                startUploadRequest();
                return;
            }
        }
        this.state = 4;
        this.delegate.didFailedUploadingFile(this);
        cleanup();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$startUploadRequest$6() {
        Utilities.stageQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.FileUploadOperation$$ExternalSyntheticLambda1
            @Override // java.lang.Runnable
            public final void run() {
                FileUploadOperation.this.lambda$startUploadRequest$5();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$startUploadRequest$5() {
        if (this.currentUploadRequetsCount < this.maxRequestsCount) {
            startUploadRequest();
        }
    }
}