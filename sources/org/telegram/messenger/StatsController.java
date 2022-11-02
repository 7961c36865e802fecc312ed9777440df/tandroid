package org.telegram.messenger;

import android.content.Context;
import android.content.SharedPreferences;
import java.io.File;
import java.io.RandomAccessFile;
import java.lang.reflect.Array;
/* loaded from: classes.dex */
public class StatsController extends BaseController {
    private static final int TYPES_COUNT = 7;
    public static final int TYPE_AUDIOS = 3;
    public static final int TYPE_CALLS = 0;
    public static final int TYPE_FILES = 5;
    public static final int TYPE_MESSAGES = 1;
    public static final int TYPE_MOBILE = 0;
    public static final int TYPE_PHOTOS = 4;
    public static final int TYPE_ROAMING = 2;
    public static final int TYPE_TOTAL = 6;
    public static final int TYPE_VIDEOS = 2;
    public static final int TYPE_WIFI = 1;
    private long lastInternalStatsSaveTime;
    private RandomAccessFile statsFile;
    private static DispatchQueue statsSaveQueue = new DispatchQueue("statsSaveQueue");
    private static final ThreadLocal<Long> lastStatsSaveTime = new ThreadLocal<Long>() { // from class: org.telegram.messenger.StatsController.1
        /* JADX INFO: Access modifiers changed from: protected */
        @Override // java.lang.ThreadLocal
        public Long initialValue() {
            return Long.valueOf(System.currentTimeMillis() - 1000);
        }
    };
    private static volatile StatsController[] Instance = new StatsController[4];
    private byte[] buffer = new byte[8];
    private long[][] sentBytes = (long[][]) Array.newInstance(long.class, 3, 7);
    private long[][] receivedBytes = (long[][]) Array.newInstance(long.class, 3, 7);
    private int[][] sentItems = (int[][]) Array.newInstance(int.class, 3, 7);
    private int[][] receivedItems = (int[][]) Array.newInstance(int.class, 3, 7);
    private long[] resetStatsDate = new long[3];
    private int[] callsTotalTime = new int[3];
    private Runnable saveRunnable = new Runnable() { // from class: org.telegram.messenger.StatsController.2
        @Override // java.lang.Runnable
        public void run() {
            long currentTimeMillis = System.currentTimeMillis();
            if (Math.abs(currentTimeMillis - StatsController.this.lastInternalStatsSaveTime) < 2000) {
                return;
            }
            StatsController.this.lastInternalStatsSaveTime = currentTimeMillis;
            try {
                StatsController.this.statsFile.seek(0L);
                for (int i = 0; i < 3; i++) {
                    for (int i2 = 0; i2 < 7; i2++) {
                        RandomAccessFile randomAccessFile = StatsController.this.statsFile;
                        StatsController statsController = StatsController.this;
                        randomAccessFile.write(statsController.longToBytes(statsController.sentBytes[i][i2]), 0, 8);
                        RandomAccessFile randomAccessFile2 = StatsController.this.statsFile;
                        StatsController statsController2 = StatsController.this;
                        randomAccessFile2.write(statsController2.longToBytes(statsController2.receivedBytes[i][i2]), 0, 8);
                        RandomAccessFile randomAccessFile3 = StatsController.this.statsFile;
                        StatsController statsController3 = StatsController.this;
                        randomAccessFile3.write(statsController3.intToBytes(statsController3.sentItems[i][i2]), 0, 4);
                        RandomAccessFile randomAccessFile4 = StatsController.this.statsFile;
                        StatsController statsController4 = StatsController.this;
                        randomAccessFile4.write(statsController4.intToBytes(statsController4.receivedItems[i][i2]), 0, 4);
                    }
                    RandomAccessFile randomAccessFile5 = StatsController.this.statsFile;
                    StatsController statsController5 = StatsController.this;
                    randomAccessFile5.write(statsController5.intToBytes(statsController5.callsTotalTime[i]), 0, 4);
                    RandomAccessFile randomAccessFile6 = StatsController.this.statsFile;
                    StatsController statsController6 = StatsController.this;
                    randomAccessFile6.write(statsController6.longToBytes(statsController6.resetStatsDate[i]), 0, 8);
                }
                StatsController.this.statsFile.getFD().sync();
            } catch (Exception unused) {
            }
        }
    };

    /* JADX INFO: Access modifiers changed from: private */
    public byte[] intToBytes(int i) {
        byte[] bArr = this.buffer;
        bArr[0] = (byte) (i >>> 24);
        bArr[1] = (byte) (i >>> 16);
        bArr[2] = (byte) (i >>> 8);
        bArr[3] = (byte) i;
        return bArr;
    }

    private int bytesToInt(byte[] bArr) {
        return (bArr[3] & 255) | (bArr[0] << 24) | ((bArr[1] & 255) << 16) | ((bArr[2] & 255) << 8);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public byte[] longToBytes(long j) {
        byte[] bArr = this.buffer;
        bArr[0] = (byte) (j >>> 56);
        bArr[1] = (byte) (j >>> 48);
        bArr[2] = (byte) (j >>> 40);
        bArr[3] = (byte) (j >>> 32);
        bArr[4] = (byte) (j >>> 24);
        bArr[5] = (byte) (j >>> 16);
        bArr[6] = (byte) (j >>> 8);
        bArr[7] = (byte) j;
        return bArr;
    }

    private long bytesToLong(byte[] bArr) {
        return ((bArr[0] & 255) << 56) | ((bArr[1] & 255) << 48) | ((bArr[2] & 255) << 40) | ((bArr[3] & 255) << 32) | ((bArr[4] & 255) << 24) | ((bArr[5] & 255) << 16) | ((bArr[6] & 255) << 8) | (255 & bArr[7]);
    }

    public static StatsController getInstance(int i) {
        StatsController statsController = Instance[i];
        if (statsController == null) {
            synchronized (StatsController.class) {
                statsController = Instance[i];
                if (statsController == null) {
                    StatsController[] statsControllerArr = Instance;
                    StatsController statsController2 = new StatsController(i);
                    statsControllerArr[i] = statsController2;
                    statsController = statsController2;
                }
            }
        }
        return statsController;
    }

    /* JADX WARN: Removed duplicated region for block: B:27:0x012d  */
    /* JADX WARN: Removed duplicated region for block: B:49:? A[RETURN, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private StatsController(int i) {
        super(i);
        boolean z;
        SharedPreferences sharedPreferences;
        RandomAccessFile randomAccessFile;
        File filesDirFixed = ApplicationLoader.getFilesDirFixed();
        if (i != 0) {
            File filesDirFixed2 = ApplicationLoader.getFilesDirFixed();
            filesDirFixed = new File(filesDirFixed2, "account" + i + "/");
            filesDirFixed.mkdirs();
        }
        try {
            randomAccessFile = new RandomAccessFile(new File(filesDirFixed, "stats2.dat"), "rw");
            this.statsFile = randomAccessFile;
        } catch (Exception unused) {
        }
        if (randomAccessFile.length() > 0) {
            boolean z2 = false;
            for (int i2 = 0; i2 < 3; i2++) {
                for (int i3 = 0; i3 < 7; i3++) {
                    this.statsFile.readFully(this.buffer, 0, 8);
                    this.sentBytes[i2][i3] = bytesToLong(this.buffer);
                    this.statsFile.readFully(this.buffer, 0, 8);
                    this.receivedBytes[i2][i3] = bytesToLong(this.buffer);
                    this.statsFile.readFully(this.buffer, 0, 4);
                    this.sentItems[i2][i3] = bytesToInt(this.buffer);
                    this.statsFile.readFully(this.buffer, 0, 4);
                    this.receivedItems[i2][i3] = bytesToInt(this.buffer);
                }
                this.statsFile.readFully(this.buffer, 0, 4);
                this.callsTotalTime[i2] = bytesToInt(this.buffer);
                this.statsFile.readFully(this.buffer, 0, 8);
                this.resetStatsDate[i2] = bytesToLong(this.buffer);
                long[] jArr = this.resetStatsDate;
                if (jArr[i2] == 0) {
                    jArr[i2] = System.currentTimeMillis();
                    z2 = true;
                }
            }
            if (z2) {
                saveStats();
            }
            z = false;
            if (z) {
                return;
            }
            if (i == 0) {
                sharedPreferences = ApplicationLoader.applicationContext.getSharedPreferences("stats", 0);
            } else {
                Context context = ApplicationLoader.applicationContext;
                sharedPreferences = context.getSharedPreferences("stats" + i, 0);
            }
            boolean z3 = false;
            for (int i4 = 0; i4 < 3; i4++) {
                int[] iArr = this.callsTotalTime;
                iArr[i4] = sharedPreferences.getInt("callsTotalTime" + i4, 0);
                long[] jArr2 = this.resetStatsDate;
                jArr2[i4] = sharedPreferences.getLong("resetStatsDate" + i4, 0L);
                for (int i5 = 0; i5 < 7; i5++) {
                    long[] jArr3 = this.sentBytes[i4];
                    jArr3[i5] = sharedPreferences.getLong("sentBytes" + i4 + "_" + i5, 0L);
                    long[] jArr4 = this.receivedBytes[i4];
                    jArr4[i5] = sharedPreferences.getLong("receivedBytes" + i4 + "_" + i5, 0L);
                    int[] iArr2 = this.sentItems[i4];
                    iArr2[i5] = sharedPreferences.getInt("sentItems" + i4 + "_" + i5, 0);
                    int[] iArr3 = this.receivedItems[i4];
                    iArr3[i5] = sharedPreferences.getInt("receivedItems" + i4 + "_" + i5, 0);
                }
                long[] jArr5 = this.resetStatsDate;
                if (jArr5[i4] == 0) {
                    jArr5[i4] = System.currentTimeMillis();
                    z3 = true;
                }
            }
            if (!z3) {
                return;
            }
            saveStats();
            return;
        }
        z = true;
        if (z) {
        }
    }

    public void incrementReceivedItemsCount(int i, int i2, int i3) {
        int[] iArr = this.receivedItems[i];
        iArr[i2] = iArr[i2] + i3;
        saveStats();
    }

    public void incrementSentItemsCount(int i, int i2, int i3) {
        int[] iArr = this.sentItems[i];
        iArr[i2] = iArr[i2] + i3;
        saveStats();
    }

    public void incrementReceivedBytesCount(int i, int i2, long j) {
        long[] jArr = this.receivedBytes[i];
        jArr[i2] = jArr[i2] + j;
        saveStats();
    }

    public void incrementSentBytesCount(int i, int i2, long j) {
        long[] jArr = this.sentBytes[i];
        jArr[i2] = jArr[i2] + j;
        saveStats();
    }

    public void incrementTotalCallsTime(int i, int i2) {
        int[] iArr = this.callsTotalTime;
        iArr[i] = iArr[i] + i2;
        saveStats();
    }

    public int getRecivedItemsCount(int i, int i2) {
        return this.receivedItems[i][i2];
    }

    public int getSentItemsCount(int i, int i2) {
        return this.sentItems[i][i2];
    }

    public long getSentBytesCount(int i, int i2) {
        if (i2 == 1) {
            long[][] jArr = this.sentBytes;
            return (((jArr[i][6] - jArr[i][5]) - jArr[i][3]) - jArr[i][2]) - jArr[i][4];
        }
        return this.sentBytes[i][i2];
    }

    public long getReceivedBytesCount(int i, int i2) {
        if (i2 == 1) {
            long[][] jArr = this.receivedBytes;
            return (((jArr[i][6] - jArr[i][5]) - jArr[i][3]) - jArr[i][2]) - jArr[i][4];
        }
        return this.receivedBytes[i][i2];
    }

    public int getCallsTotalTime(int i) {
        return this.callsTotalTime[i];
    }

    public long getResetStatsDate(int i) {
        return this.resetStatsDate[i];
    }

    public void resetStats(int i) {
        this.resetStatsDate[i] = System.currentTimeMillis();
        for (int i2 = 0; i2 < 7; i2++) {
            this.sentBytes[i][i2] = 0;
            this.receivedBytes[i][i2] = 0;
            this.sentItems[i][i2] = 0;
            this.receivedItems[i][i2] = 0;
        }
        this.callsTotalTime[i] = 0;
        saveStats();
    }

    private void saveStats() {
        long currentTimeMillis = System.currentTimeMillis();
        ThreadLocal<Long> threadLocal = lastStatsSaveTime;
        if (Math.abs(currentTimeMillis - threadLocal.get().longValue()) >= 2000) {
            threadLocal.set(Long.valueOf(currentTimeMillis));
            statsSaveQueue.postRunnable(this.saveRunnable);
        }
    }
}