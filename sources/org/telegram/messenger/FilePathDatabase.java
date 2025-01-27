package org.telegram.messenger;

import android.os.Looper;
import android.util.LongSparseArray;
import j$.util.concurrent.ConcurrentHashMap;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import org.telegram.SQLite.SQLiteCursor;
import org.telegram.SQLite.SQLiteDatabase;
import org.telegram.SQLite.SQLiteException;
import org.telegram.SQLite.SQLitePreparedStatement;
import org.telegram.messenger.CacheByChatsController;
import org.telegram.ui.Storage.CacheModel;

/* loaded from: classes3.dex */
public class FilePathDatabase {
    private static final String DATABASE_BACKUP_NAME = "file_to_path_backup";
    private static final String DATABASE_NAME = "file_to_path";
    public static final int FLAG_LOCALLY_CREATED = 1;
    private static final int LAST_DB_VERSION = 7;
    public static final int MESSAGE_TYPE_VIDEO_MESSAGE = 0;
    private File cacheFile;
    private final int currentAccount;
    private SQLiteDatabase database;
    boolean databaseCreated;
    private DispatchQueue dispatchQueue;
    private File shmCacheFile;
    private final ConcurrentHashMap<String, String> cache = new ConcurrentHashMap<>();
    private final FileMeta metaTmp = new FileMeta();

    public static class FileMeta {
        public long dialogId;
        public int messageId;
        public long messageSize;
        public int messageType;
    }

    public static class PathData {
        public final int dc;
        public final long id;
        public final int type;

        public PathData(long j, int i, int i2) {
            this.id = j;
            this.dc = i;
            this.type = i2;
        }
    }

    public FilePathDatabase(int i) {
        this.currentAccount = i;
    }

    private void createBackup() {
        File filesDirFixed = ApplicationLoader.getFilesDirFixed();
        if (this.currentAccount != 0) {
            File file = new File(filesDirFixed, "account" + this.currentAccount + "/");
            file.mkdirs();
            filesDirFixed = file;
        }
        File file2 = new File(filesDirFixed, "file_to_path_backup.db");
        try {
            AndroidUtilities.copyFile(this.cacheFile, file2);
            FileLog.d("file db backup created " + file2.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void ensureQueueExist() {
        if (this.dispatchQueue == null) {
            synchronized (this) {
                try {
                    if (this.dispatchQueue == null) {
                        DispatchQueue dispatchQueue = new DispatchQueue("files_database_queue_" + this.currentAccount);
                        this.dispatchQueue = dispatchQueue;
                        dispatchQueue.setPriority(10);
                    }
                } finally {
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$checkMediaExistance$2(ArrayList arrayList, long[] jArr, CountDownLatch countDownLatch) {
        long currentTimeMillis = System.currentTimeMillis();
        ensureDatabaseCreated();
        for (int i = 0; i < arrayList.size(); i++) {
            try {
                ((MessageObject) arrayList.get(i)).checkMediaExistance(false);
            } finally {
                try {
                } finally {
                }
            }
        }
        jArr[0] = System.currentTimeMillis() - currentTimeMillis;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$clear$3() {
        ensureDatabaseCreated();
        try {
            this.database.executeFast("DELETE FROM paths WHERE 1").stepThis().dispose();
            this.database.executeFast("DELETE FROM paths_by_dialog_id WHERE 1").stepThis().dispose();
        } catch (Exception e) {
            FileLog.e(e);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$getPath$0(long j, int i, int i2, String[] strArr, long j2, CountDownLatch countDownLatch) {
        ensureDatabaseCreated();
        SQLiteDatabase sQLiteDatabase = this.database;
        if (sQLiteDatabase != null) {
            SQLiteCursor sQLiteCursor = null;
            try {
                sQLiteCursor = sQLiteDatabase.queryFinalized("SELECT path FROM paths WHERE document_id = " + j + " AND dc_id = " + i + " AND type = " + i2, new Object[0]);
                if (sQLiteCursor.next()) {
                    strArr[0] = sQLiteCursor.stringValue(0);
                    if (BuildVars.DEBUG_VERSION) {
                        FileLog.d("get file path id=" + j + " dc=" + i + " type=" + i2 + " path=" + strArr[0] + " in " + (System.currentTimeMillis() - j2) + "ms");
                    }
                }
            } catch (Throwable th) {
                try {
                    FileLog.e(th);
                } finally {
                    if (sQLiteCursor != null) {
                        sQLiteCursor.dispose();
                    }
                }
            }
        }
        countDownLatch.countDown();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$hasAnotherRefOnFile$4(String str, boolean[] zArr, CountDownLatch countDownLatch) {
        ensureDatabaseCreated();
        try {
            try {
                if (this.database.queryFinalized("SELECT document_id FROM paths WHERE path = '" + str + "'", new Object[0]).next()) {
                    zArr[0] = true;
                }
            } catch (Exception e) {
                FileLog.e(e);
            }
        } finally {
            countDownLatch.countDown();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$isLocallyCreated$8(String str, boolean[] zArr, CountDownLatch countDownLatch) {
        ensureDatabaseCreated();
        try {
            try {
                SQLiteCursor queryFinalized = this.database.queryFinalized("SELECT flags FROM paths WHERE path = '" + str + "'", new Object[0]);
                if (queryFinalized.next()) {
                    boolean z = true;
                    if ((queryFinalized.intValue(0) & 1) == 0) {
                        z = false;
                    }
                    zArr[0] = z;
                }
            } catch (Exception e) {
                FileLog.e(e);
            }
        } finally {
            countDownLatch.countDown();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$lookupFiles$7(ArrayList arrayList, LongSparseArray longSparseArray, CountDownLatch countDownLatch) {
        try {
            ensureDatabaseCreated();
            FileMeta fileMeta = new FileMeta();
            for (int i = 0; i < arrayList.size(); i++) {
                FileMeta fileDialogId = getFileDialogId(((CacheByChatsController.KeepMediaFile) arrayList.get(i)).file, fileMeta);
                if (fileDialogId != null) {
                    long j = fileDialogId.dialogId;
                    if (j != 0) {
                        ArrayList arrayList2 = (ArrayList) longSparseArray.get(j);
                        if (arrayList2 == null) {
                            arrayList2 = new ArrayList();
                            longSparseArray.put(fileDialogId.dialogId, arrayList2);
                        }
                        ((CacheByChatsController.KeepMediaFile) arrayList.get(i)).isStory = fileDialogId.messageType == 23;
                        arrayList2.add((CacheByChatsController.KeepMediaFile) arrayList.get(i));
                    }
                }
            }
        } finally {
            try {
            } finally {
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Removed duplicated region for block: B:36:0x0101  */
    /* JADX WARN: Removed duplicated region for block: B:38:0x0106  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public /* synthetic */ void lambda$putPath$1(long j, int i, int i2, String str, int i3) {
        SQLitePreparedStatement sQLitePreparedStatement;
        SQLitePreparedStatement sQLitePreparedStatement2;
        if (BuildVars.DEBUG_VERSION) {
            FileLog.d("put file path id=" + j + " dc=" + i + " type=" + i2 + " path=" + str);
        }
        ensureDatabaseCreated();
        SQLiteDatabase sQLiteDatabase = this.database;
        if (sQLiteDatabase == null) {
            return;
        }
        SQLitePreparedStatement sQLitePreparedStatement3 = null;
        try {
            if (str != null) {
                SQLitePreparedStatement executeFast = sQLiteDatabase.executeFast("DELETE FROM paths WHERE path = ?");
                try {
                    executeFast.bindString(1, str);
                    executeFast.step();
                    sQLitePreparedStatement3 = this.database.executeFast("REPLACE INTO paths VALUES(?, ?, ?, ?, ?)");
                    sQLitePreparedStatement3.requery();
                    sQLitePreparedStatement3.bindLong(1, j);
                    sQLitePreparedStatement3.bindInteger(2, i);
                    sQLitePreparedStatement3.bindInteger(3, i2);
                    sQLitePreparedStatement3.bindString(4, str);
                    sQLitePreparedStatement3.bindInteger(5, i3);
                    sQLitePreparedStatement3.step();
                    sQLitePreparedStatement3.dispose();
                    this.cache.put(j + "_" + i + "_" + i2, str);
                    sQLitePreparedStatement2 = sQLitePreparedStatement3;
                    sQLitePreparedStatement3 = executeFast;
                } catch (SQLiteException e) {
                    e = e;
                    sQLitePreparedStatement = sQLitePreparedStatement3;
                    sQLitePreparedStatement3 = executeFast;
                    try {
                        FileLog.e(e);
                        if (sQLitePreparedStatement3 != null) {
                            sQLitePreparedStatement3.dispose();
                        }
                        if (sQLitePreparedStatement != null) {
                            sQLitePreparedStatement2 = sQLitePreparedStatement;
                            sQLitePreparedStatement2.dispose();
                        }
                        return;
                    } catch (Throwable th) {
                        th = th;
                        if (sQLitePreparedStatement3 != null) {
                            sQLitePreparedStatement3.dispose();
                        }
                        if (sQLitePreparedStatement != null) {
                            sQLitePreparedStatement.dispose();
                        }
                        throw th;
                    }
                } catch (Throwable th2) {
                    th = th2;
                    sQLitePreparedStatement = sQLitePreparedStatement3;
                    sQLitePreparedStatement3 = executeFast;
                    if (sQLitePreparedStatement3 != null) {
                    }
                    if (sQLitePreparedStatement != null) {
                    }
                    throw th;
                }
            } else {
                sQLiteDatabase.executeFast("DELETE FROM paths WHERE document_id = " + j + " AND dc_id = " + i + " AND type = " + i2).stepThis().dispose();
                this.cache.remove(j + "_" + i + "_" + i2);
                sQLitePreparedStatement2 = null;
            }
            if (sQLitePreparedStatement3 != null) {
                sQLitePreparedStatement3.dispose();
            }
            if (sQLitePreparedStatement2 == null) {
                return;
            }
        } catch (SQLiteException e2) {
            e = e2;
            sQLitePreparedStatement = sQLitePreparedStatement3;
        } catch (Throwable th3) {
            th = th3;
            sQLitePreparedStatement = sQLitePreparedStatement3;
        }
        sQLitePreparedStatement2.dispose();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$removeFiles$6(List list) {
        try {
            ensureDatabaseCreated();
            this.database.beginTransaction();
            for (int i = 0; i < list.size(); i++) {
                this.database.executeFast("DELETE FROM paths_by_dialog_id WHERE path = '" + shield(((CacheModel.FileInfo) list.get(i)).file.getPath()) + "'").stepThis().dispose();
            }
        } finally {
            try {
            } finally {
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$saveFileDialogId$5(File file, FileMeta fileMeta) {
        ensureDatabaseCreated();
        SQLitePreparedStatement sQLitePreparedStatement = null;
        try {
            try {
                sQLitePreparedStatement = this.database.executeFast("REPLACE INTO paths_by_dialog_id VALUES(?, ?, ?, ?)");
                sQLitePreparedStatement.requery();
                sQLitePreparedStatement.bindString(1, shield(file.getPath()));
                sQLitePreparedStatement.bindLong(2, fileMeta.dialogId);
                sQLitePreparedStatement.bindInteger(3, fileMeta.messageId);
                sQLitePreparedStatement.bindInteger(4, fileMeta.messageType);
                sQLitePreparedStatement.step();
            } catch (Exception e) {
                FileLog.e(e);
                if (sQLitePreparedStatement == null) {
                    return;
                }
            }
            sQLitePreparedStatement.dispose();
        } catch (Throwable th) {
            if (sQLitePreparedStatement != null) {
                sQLitePreparedStatement.dispose();
            }
            throw th;
        }
    }

    private void migrateDatabase(int i) {
        if (i == 1) {
            this.database.executeFast("CREATE INDEX IF NOT EXISTS path_in_paths ON paths(path);").stepThis().dispose();
            this.database.executeFast("PRAGMA user_version = 2").stepThis().dispose();
            i = 2;
        }
        if (i == 2) {
            this.database.executeFast("CREATE TABLE paths_by_dialog_id(path TEXT PRIMARY KEY, dialog_id INTEGER);").stepThis().dispose();
            this.database.executeFast("PRAGMA user_version = 3").stepThis().dispose();
            i = 3;
        }
        if (i == 3) {
            this.database.executeFast("ALTER TABLE paths_by_dialog_id ADD COLUMN message_id INTEGER default 0").stepThis().dispose();
            this.database.executeFast("ALTER TABLE paths_by_dialog_id ADD COLUMN message_type INTEGER default 0").stepThis().dispose();
            this.database.executeFast("PRAGMA user_version = 4").stepThis().dispose();
            i = 4;
        }
        if (i == 4 || i == 5 || i == 6) {
            try {
                this.database.executeFast("ALTER TABLE paths ADD COLUMN flags INTEGER default 0").stepThis().dispose();
            } catch (Throwable th) {
                FileLog.e(th);
            }
            this.database.executeFast("PRAGMA user_version = 7").stepThis().dispose();
        }
    }

    private void postRunnable(Runnable runnable) {
        ensureQueueExist();
        this.dispatchQueue.postRunnable(runnable);
    }

    private void postToFrontRunnable(Runnable runnable) {
        ensureQueueExist();
        this.dispatchQueue.postToFrontRunnable(runnable);
    }

    private boolean restoreBackup() {
        File filesDirFixed = ApplicationLoader.getFilesDirFixed();
        if (this.currentAccount != 0) {
            File file = new File(filesDirFixed, "account" + this.currentAccount + "/");
            file.mkdirs();
            filesDirFixed = file;
        }
        File file2 = new File(filesDirFixed, "file_to_path_backup.db");
        if (!file2.exists()) {
            return false;
        }
        try {
            return AndroidUtilities.copyFile(file2, this.cacheFile);
        } catch (IOException e) {
            FileLog.e(e);
            return false;
        }
    }

    private String shield(String str) {
        return str.replace("'", "").replace("\"", "");
    }

    public void checkMediaExistance(ArrayList<MessageObject> arrayList) {
        if (arrayList.isEmpty()) {
            return;
        }
        final ArrayList arrayList2 = new ArrayList(arrayList);
        final CountDownLatch countDownLatch = new CountDownLatch(1);
        long currentTimeMillis = System.currentTimeMillis();
        final long[] jArr = new long[1];
        postToFrontRunnable(new Runnable() { // from class: org.telegram.messenger.FilePathDatabase$$ExternalSyntheticLambda8
            @Override // java.lang.Runnable
            public final void run() {
                FilePathDatabase.this.lambda$checkMediaExistance$2(arrayList2, jArr, countDownLatch);
            }
        });
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            FileLog.e(e);
        }
        FileLog.d("checkMediaExistance size=" + arrayList.size() + " time=" + (System.currentTimeMillis() - currentTimeMillis) + " thread_time=" + jArr[0]);
        if (BuildVars.DEBUG_VERSION && Thread.currentThread() == Looper.getMainLooper().getThread()) {
            FileLog.e(new Exception("warning, not allowed in main thread"));
        }
    }

    public void clear() {
        this.cache.clear();
        postRunnable(new Runnable() { // from class: org.telegram.messenger.FilePathDatabase$$ExternalSyntheticLambda6
            @Override // java.lang.Runnable
            public final void run() {
                FilePathDatabase.this.lambda$clear$3();
            }
        });
    }

    public void createDatabase(int i, boolean z) {
        File filesDirFixed = ApplicationLoader.getFilesDirFixed();
        if (this.currentAccount != 0) {
            File file = new File(filesDirFixed, "account" + this.currentAccount + "/");
            file.mkdirs();
            filesDirFixed = file;
        }
        this.cacheFile = new File(filesDirFixed, "file_to_path.db");
        this.shmCacheFile = new File(filesDirFixed, "file_to_path.db-shm");
        boolean z2 = !this.cacheFile.exists();
        try {
            SQLiteDatabase sQLiteDatabase = new SQLiteDatabase(this.cacheFile.getPath());
            this.database = sQLiteDatabase;
            sQLiteDatabase.executeFast("PRAGMA secure_delete = ON").stepThis().dispose();
            this.database.executeFast("PRAGMA temp_store = MEMORY").stepThis().dispose();
            if (z2) {
                this.database.executeFast("CREATE TABLE paths(document_id INTEGER, dc_id INTEGER, type INTEGER, path TEXT, flags INTEGER, PRIMARY KEY(document_id, dc_id, type));").stepThis().dispose();
                this.database.executeFast("CREATE INDEX IF NOT EXISTS path_in_paths ON paths(path);").stepThis().dispose();
                this.database.executeFast("CREATE TABLE paths_by_dialog_id(path TEXT PRIMARY KEY, dialog_id INTEGER, message_id INTEGER, message_type INTEGER);").stepThis().dispose();
                this.database.executeFast("PRAGMA user_version = 7").stepThis().dispose();
            } else {
                int intValue = this.database.executeInt("PRAGMA user_version", new Object[0]).intValue();
                if (BuildVars.LOGS_ENABLED) {
                    FileLog.d("current files db version = " + intValue);
                }
                if (intValue == 0) {
                    throw new Exception("malformed");
                }
                migrateDatabase(intValue);
            }
            if (!z) {
                createBackup();
            }
            FileLog.d("files db created from_backup= " + z);
        } catch (Exception e) {
            if (i < 4) {
                if (!z && restoreBackup()) {
                    createDatabase(i + 1, true);
                    return;
                } else {
                    this.cacheFile.delete();
                    this.shmCacheFile.delete();
                    createDatabase(i + 1, false);
                }
            }
            if (BuildVars.DEBUG_VERSION) {
                FileLog.e(e);
            }
        }
    }

    public void ensureDatabaseCreated() {
        if (this.databaseCreated) {
            return;
        }
        if (!NativeLoader.loaded()) {
            int i = 0;
            while (!NativeLoader.loaded()) {
                try {
                    Thread.sleep(1000L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                i++;
                if (i > 5) {
                    break;
                }
            }
        }
        createDatabase(0, false);
        this.databaseCreated = true;
    }

    public FileMeta getFileDialogId(File file, FileMeta fileMeta) {
        int i;
        int i2;
        SQLiteCursor sQLiteCursor = null;
        if (file == null) {
            return null;
        }
        if (fileMeta == null) {
            fileMeta = this.metaTmp;
        }
        int i3 = 0;
        long j = 0;
        try {
            try {
                sQLiteCursor = this.database.queryFinalized("SELECT dialog_id, message_id, message_type FROM paths_by_dialog_id WHERE path = '" + shield(file.getPath()) + "'", new Object[0]);
            } catch (Exception e) {
                e = e;
                i = 0;
            }
            if (sQLiteCursor.next()) {
                j = sQLiteCursor.longValue(0);
                i = sQLiteCursor.intValue(1);
                try {
                    i3 = i;
                    i2 = sQLiteCursor.intValue(2);
                } catch (Exception e2) {
                    e = e2;
                    FileLog.e(e);
                    if (sQLiteCursor != null) {
                        i3 = i;
                        i2 = 0;
                        sQLiteCursor.dispose();
                        int i4 = i3;
                        i3 = i2;
                        i = i4;
                    }
                    fileMeta.dialogId = j;
                    fileMeta.messageId = i;
                    fileMeta.messageType = i3;
                    return fileMeta;
                }
                sQLiteCursor.dispose();
                int i42 = i3;
                i3 = i2;
                i = i42;
                fileMeta.dialogId = j;
                fileMeta.messageId = i;
                fileMeta.messageType = i3;
                return fileMeta;
            }
            i2 = 0;
            sQLiteCursor.dispose();
            int i422 = i3;
            i3 = i2;
            i = i422;
            fileMeta.dialogId = j;
            fileMeta.messageId = i;
            fileMeta.messageType = i3;
            return fileMeta;
        } catch (Throwable th) {
            if (sQLiteCursor != null) {
                sQLiteCursor.dispose();
            }
            throw th;
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:46:0x0163  */
    /* JADX WARN: Removed duplicated region for block: B:53:0x015b  */
    /* JADX WARN: Removed duplicated region for block: B:57:0x016e  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public String getPath(final long j, final int i, final int i2, boolean z) {
        SQLiteCursor sQLiteCursor;
        String str;
        SQLiteException sQLiteException;
        String str2;
        SQLiteCursor sQLiteCursor2;
        String str3;
        final long currentTimeMillis = System.currentTimeMillis();
        String str4 = j + "_" + i + "_" + i2;
        String str5 = this.cache.get(str4);
        if (str5 != null) {
            if (BuildVars.DEBUG_VERSION) {
                FileLog.d("get file path cached id=" + j + " dc=" + i + " type=" + i2 + " path=" + str5 + " in " + (System.currentTimeMillis() - currentTimeMillis) + "ms");
            }
            return str5;
        }
        DispatchQueue dispatchQueue = this.dispatchQueue;
        if ((dispatchQueue == null || dispatchQueue.getHandler() == null || Thread.currentThread() != this.dispatchQueue.getHandler().getLooper().getThread()) ? z : false) {
            final CountDownLatch countDownLatch = new CountDownLatch(1);
            final String[] strArr = new String[1];
            postRunnable(new Runnable() { // from class: org.telegram.messenger.FilePathDatabase$$ExternalSyntheticLambda4
                @Override // java.lang.Runnable
                public final void run() {
                    FilePathDatabase.this.lambda$getPath$0(j, i, i2, strArr, currentTimeMillis, countDownLatch);
                }
            });
            try {
                countDownLatch.await();
            } catch (Exception unused) {
            }
            String str6 = strArr[0];
            if (str6 != null) {
                this.cache.put(str4, str6);
            }
            return strArr[0];
        }
        SQLiteDatabase sQLiteDatabase = this.database;
        if (sQLiteDatabase == null) {
            return null;
        }
        try {
            try {
                str = str4;
                try {
                    sQLiteCursor2 = sQLiteDatabase.queryFinalized("SELECT path FROM paths WHERE document_id = " + j + " AND dc_id = " + i + " AND type = " + i2, new Object[0]);
                    try {
                        try {
                            if (sQLiteCursor2.next()) {
                                str3 = sQLiteCursor2.stringValue(0);
                                try {
                                    if (BuildVars.DEBUG_VERSION) {
                                        FileLog.d("get file path id=" + j + " dc=" + i + " type=" + i2 + " path=" + str3 + " in " + (System.currentTimeMillis() - currentTimeMillis) + "ms");
                                    }
                                } catch (SQLiteException e) {
                                    sQLiteException = e;
                                    str2 = str3;
                                    sQLiteCursor = sQLiteCursor2;
                                    try {
                                        FileLog.e(sQLiteException);
                                        if (sQLiteCursor != null) {
                                        }
                                        if (str2 != null) {
                                        }
                                        return str2;
                                    } catch (Throwable th) {
                                        th = th;
                                        if (sQLiteCursor != null) {
                                        }
                                        throw th;
                                    }
                                }
                            } else {
                                str3 = null;
                            }
                        } catch (Throwable th2) {
                            th = th2;
                            sQLiteCursor = sQLiteCursor2;
                            if (sQLiteCursor != null) {
                                sQLiteCursor.dispose();
                            }
                            throw th;
                        }
                    } catch (SQLiteException e2) {
                        sQLiteCursor = sQLiteCursor2;
                        sQLiteException = e2;
                        str2 = null;
                    }
                } catch (SQLiteException e3) {
                    e = e3;
                    sQLiteException = e;
                    str2 = null;
                    sQLiteCursor = null;
                    FileLog.e(sQLiteException);
                    if (sQLiteCursor != null) {
                        sQLiteCursor2 = sQLiteCursor;
                        str3 = str2;
                        sQLiteCursor2.dispose();
                        str2 = str3;
                    }
                    if (str2 != null) {
                    }
                    return str2;
                }
            } catch (SQLiteException e4) {
                e = e4;
                str = str4;
            }
            sQLiteCursor2.dispose();
            str2 = str3;
            if (str2 != null) {
                this.cache.put(str, str2);
            }
            return str2;
        } catch (Throwable th3) {
            th = th3;
            sQLiteCursor = null;
        }
    }

    public DispatchQueue getQueue() {
        ensureQueueExist();
        return this.dispatchQueue;
    }

    public boolean hasAnotherRefOnFile(final String str) {
        final CountDownLatch countDownLatch = new CountDownLatch(1);
        final boolean[] zArr = {false};
        postRunnable(new Runnable() { // from class: org.telegram.messenger.FilePathDatabase$$ExternalSyntheticLambda3
            @Override // java.lang.Runnable
            public final void run() {
                FilePathDatabase.this.lambda$hasAnotherRefOnFile$4(str, zArr, countDownLatch);
            }
        });
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            FileLog.e(e);
        }
        return zArr[0];
    }

    public boolean isLocallyCreated(final String str) {
        final CountDownLatch countDownLatch = new CountDownLatch(1);
        final boolean[] zArr = {false};
        postRunnable(new Runnable() { // from class: org.telegram.messenger.FilePathDatabase$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                FilePathDatabase.this.lambda$isLocallyCreated$8(str, zArr, countDownLatch);
            }
        });
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            FileLog.e(e);
        }
        return zArr[0];
    }

    public LongSparseArray<ArrayList<CacheByChatsController.KeepMediaFile>> lookupFiles(final ArrayList<? extends CacheByChatsController.KeepMediaFile> arrayList) {
        final CountDownLatch countDownLatch = new CountDownLatch(1);
        final LongSparseArray<ArrayList<CacheByChatsController.KeepMediaFile>> longSparseArray = new LongSparseArray<>();
        postRunnable(new Runnable() { // from class: org.telegram.messenger.FilePathDatabase$$ExternalSyntheticLambda1
            @Override // java.lang.Runnable
            public final void run() {
                FilePathDatabase.this.lambda$lookupFiles$7(arrayList, longSparseArray, countDownLatch);
            }
        });
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            FileLog.e(e);
        }
        return longSparseArray;
    }

    public void putPath(final long j, final int i, final int i2, final int i3, final String str) {
        postRunnable(new Runnable() { // from class: org.telegram.messenger.FilePathDatabase$$ExternalSyntheticLambda2
            @Override // java.lang.Runnable
            public final void run() {
                FilePathDatabase.this.lambda$putPath$1(j, i, i2, str, i3);
            }
        });
    }

    public void removeFiles(final List<CacheModel.FileInfo> list) {
        postRunnable(new Runnable() { // from class: org.telegram.messenger.FilePathDatabase$$ExternalSyntheticLambda5
            @Override // java.lang.Runnable
            public final void run() {
                FilePathDatabase.this.lambda$removeFiles$6(list);
            }
        });
    }

    public void saveFileDialogId(final File file, final FileMeta fileMeta) {
        if (file == null || fileMeta == null) {
            return;
        }
        postRunnable(new Runnable() { // from class: org.telegram.messenger.FilePathDatabase$$ExternalSyntheticLambda7
            @Override // java.lang.Runnable
            public final void run() {
                FilePathDatabase.this.lambda$saveFileDialogId$5(file, fileMeta);
            }
        });
    }
}
