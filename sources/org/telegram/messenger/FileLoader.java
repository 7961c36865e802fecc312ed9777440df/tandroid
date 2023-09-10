package org.telegram.messenger;

import android.text.TextUtils;
import android.util.SparseArray;
import j$.util.concurrent.ConcurrentHashMap;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.telegram.messenger.FileLoadOperation;
import org.telegram.messenger.FileLoader;
import org.telegram.messenger.FilePathDatabase;
import org.telegram.messenger.FileUploadOperation;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC$ChatPhoto;
import org.telegram.tgnet.TLRPC$Document;
import org.telegram.tgnet.TLRPC$DocumentAttribute;
import org.telegram.tgnet.TLRPC$FileLocation;
import org.telegram.tgnet.TLRPC$InputEncryptedFile;
import org.telegram.tgnet.TLRPC$InputFile;
import org.telegram.tgnet.TLRPC$Message;
import org.telegram.tgnet.TLRPC$Photo;
import org.telegram.tgnet.TLRPC$PhotoSize;
import org.telegram.tgnet.TLRPC$StoryItem;
import org.telegram.tgnet.TLRPC$TL_documentAttributeFilename;
import org.telegram.tgnet.TLRPC$TL_fileLocationToBeDeprecated;
import org.telegram.tgnet.TLRPC$TL_fileLocationUnavailable;
import org.telegram.tgnet.TLRPC$TL_messageMediaDocument;
import org.telegram.tgnet.TLRPC$TL_messageMediaInvoice;
import org.telegram.tgnet.TLRPC$TL_messageMediaPhoto;
import org.telegram.tgnet.TLRPC$TL_messageMediaWebPage;
import org.telegram.tgnet.TLRPC$TL_messageService;
import org.telegram.tgnet.TLRPC$TL_photo;
import org.telegram.tgnet.TLRPC$TL_photoCachedSize;
import org.telegram.tgnet.TLRPC$TL_photoPathSize;
import org.telegram.tgnet.TLRPC$TL_photoSizeEmpty;
import org.telegram.tgnet.TLRPC$TL_photoStrippedSize;
import org.telegram.tgnet.TLRPC$TL_secureFile;
import org.telegram.tgnet.TLRPC$TL_videoSize;
import org.telegram.tgnet.TLRPC$TL_videoSizeEmojiMarkup;
import org.telegram.tgnet.TLRPC$TL_videoSizeStickerMarkup;
import org.telegram.tgnet.TLRPC$UserProfilePhoto;
import org.telegram.tgnet.TLRPC$VideoSize;
import org.telegram.tgnet.TLRPC$WebDocument;
import org.telegram.ui.LaunchActivity;
/* loaded from: classes.dex */
public class FileLoader extends BaseController {
    public static final long DEFAULT_MAX_FILE_SIZE = 2097152000;
    public static final long DEFAULT_MAX_FILE_SIZE_PREMIUM = 4194304000L;
    public static final int IMAGE_TYPE_ANIMATION = 2;
    public static final int IMAGE_TYPE_LOTTIE = 1;
    public static final int IMAGE_TYPE_SVG = 3;
    public static final int IMAGE_TYPE_SVG_WHITE = 4;
    public static final int IMAGE_TYPE_THEME_PREVIEW = 5;
    public static final int MEDIA_DIR_AUDIO = 1;
    public static final int MEDIA_DIR_CACHE = 4;
    public static final int MEDIA_DIR_DOCUMENT = 3;
    public static final int MEDIA_DIR_FILES = 5;
    public static final int MEDIA_DIR_IMAGE = 0;
    public static final int MEDIA_DIR_IMAGE_PUBLIC = 100;
    public static final int MEDIA_DIR_STORIES = 6;
    public static final int MEDIA_DIR_VIDEO = 2;
    public static final int MEDIA_DIR_VIDEO_PUBLIC = 101;
    public static final int PRELOAD_CACHE_TYPE = 11;
    public static final int PRIORITY_HIGH = 3;
    public static final int PRIORITY_LOW = 0;
    public static final int PRIORITY_NORMAL = 1;
    public static final int PRIORITY_NORMAL_UP = 2;
    private static final int PRIORITY_STREAM = 4;
    private static Pattern sentPattern;
    private int currentUploadOperationsCount;
    private int currentUploadSmallOperationsCount;
    private FileLoaderDelegate delegate;
    Runnable dumpFilesQueueRunnable;
    private final FilePathDatabase filePathDatabase;
    private String forceLoadingFile;
    private final FileLoaderPriorityQueue[] largeFilesQueue;
    private int lastReferenceId;
    private final ConcurrentHashMap<String, FileLoadOperation> loadOperationPaths;
    private final ConcurrentHashMap<String, LoadOperationUIObject> loadOperationPathsUI;
    private HashMap<String, Boolean> loadingVideos;
    private ConcurrentHashMap<Integer, Object> parentObjectReferences;
    private int priorityIncreasePointer;
    private final FileLoaderPriorityQueue[] smallFilesQueue;
    private final ConcurrentHashMap<String, FileUploadOperation> uploadOperationPaths;
    private final ConcurrentHashMap<String, FileUploadOperation> uploadOperationPathsEnc;
    private final LinkedList<FileUploadOperation> uploadOperationQueue;
    private HashMap<String, Long> uploadSizes;
    private final LinkedList<FileUploadOperation> uploadSmallOperationQueue;
    private static volatile DispatchQueue fileLoaderQueue = new DispatchQueue("fileUploadQueue");
    private static SparseArray<File> mediaDirs = null;
    private static final FileLoader[] Instance = new FileLoader[4];

    /* loaded from: classes.dex */
    public interface FileLoaderDelegate {
        void fileDidFailedLoad(String str, int i);

        void fileDidFailedUpload(String str, boolean z);

        void fileDidLoaded(String str, File file, Object obj, int i);

        void fileDidUploaded(String str, TLRPC$InputFile tLRPC$InputFile, TLRPC$InputEncryptedFile tLRPC$InputEncryptedFile, byte[] bArr, byte[] bArr2, long j);

        void fileLoadProgressChanged(FileLoadOperation fileLoadOperation, String str, long j, long j2);

        void fileUploadProgressChanged(FileUploadOperation fileUploadOperation, String str, long j, long j2, boolean z);
    }

    /* loaded from: classes.dex */
    public interface FileResolver {
        File getFile();
    }

    static /* synthetic */ int access$1008(FileLoader fileLoader) {
        int i = fileLoader.currentUploadOperationsCount;
        fileLoader.currentUploadOperationsCount = i + 1;
        return i;
    }

    static /* synthetic */ int access$1010(FileLoader fileLoader) {
        int i = fileLoader.currentUploadOperationsCount;
        fileLoader.currentUploadOperationsCount = i - 1;
        return i;
    }

    static /* synthetic */ int access$808(FileLoader fileLoader) {
        int i = fileLoader.currentUploadSmallOperationsCount;
        fileLoader.currentUploadSmallOperationsCount = i + 1;
        return i;
    }

    static /* synthetic */ int access$810(FileLoader fileLoader) {
        int i = fileLoader.currentUploadSmallOperationsCount;
        fileLoader.currentUploadSmallOperationsCount = i - 1;
        return i;
    }

    public static FilePathDatabase.FileMeta getFileMetadataFromParent(int i, Object obj) {
        if (obj instanceof String) {
            String str = (String) obj;
            if (str.startsWith("sent_")) {
                if (sentPattern == null) {
                    sentPattern = Pattern.compile("sent_.*_([0-9]+)_([0-9]+)_([0-9]+)_([0-9]+)");
                }
                try {
                    Matcher matcher = sentPattern.matcher(str);
                    if (matcher.matches()) {
                        FilePathDatabase.FileMeta fileMeta = new FilePathDatabase.FileMeta();
                        fileMeta.messageId = Integer.parseInt(matcher.group(1));
                        fileMeta.dialogId = Long.parseLong(matcher.group(2));
                        fileMeta.messageType = Integer.parseInt(matcher.group(3));
                        fileMeta.messageSize = Long.parseLong(matcher.group(4));
                        return fileMeta;
                    }
                    return null;
                } catch (Exception e) {
                    FileLog.e(e);
                    return null;
                }
            }
            return null;
        } else if (obj instanceof MessageObject) {
            MessageObject messageObject = (MessageObject) obj;
            FilePathDatabase.FileMeta fileMeta2 = new FilePathDatabase.FileMeta();
            fileMeta2.messageId = messageObject.getId();
            fileMeta2.dialogId = messageObject.getDialogId();
            fileMeta2.messageType = messageObject.type;
            fileMeta2.messageSize = messageObject.getSize();
            return fileMeta2;
        } else if (obj instanceof TLRPC$StoryItem) {
            FilePathDatabase.FileMeta fileMeta3 = new FilePathDatabase.FileMeta();
            fileMeta3.dialogId = ((TLRPC$StoryItem) obj).dialogId;
            fileMeta3.messageType = 23;
            return fileMeta3;
        } else {
            return null;
        }
    }

    public static TLRPC$VideoSize getVectorMarkupVideoSize(TLRPC$Photo tLRPC$Photo) {
        if (tLRPC$Photo != null && tLRPC$Photo.video_sizes != null) {
            for (int i = 0; i < tLRPC$Photo.video_sizes.size(); i++) {
                TLRPC$VideoSize tLRPC$VideoSize = tLRPC$Photo.video_sizes.get(i);
                if ((tLRPC$VideoSize instanceof TLRPC$TL_videoSizeEmojiMarkup) || (tLRPC$VideoSize instanceof TLRPC$TL_videoSizeStickerMarkup)) {
                    return tLRPC$VideoSize;
                }
            }
        }
        return null;
    }

    public static TLRPC$VideoSize getEmojiMarkup(ArrayList<TLRPC$VideoSize> arrayList) {
        for (int i = 0; i < arrayList.size(); i++) {
            if ((arrayList.get(i) instanceof TLRPC$TL_videoSizeEmojiMarkup) || (arrayList.get(i) instanceof TLRPC$TL_videoSizeStickerMarkup)) {
                return arrayList.get(i);
            }
        }
        return null;
    }

    private int getPriorityValue(int i) {
        if (i == 4) {
            return ConnectionsManager.DEFAULT_DATACENTER_ID;
        }
        if (i == 3) {
            int i2 = this.priorityIncreasePointer + 1;
            this.priorityIncreasePointer = i2;
            return i2 + FileLoaderPriorityQueue.PRIORITY_VALUE_MAX;
        } else if (i != 2) {
            return i == 1 ? 65536 : 0;
        } else {
            int i3 = this.priorityIncreasePointer + 1;
            this.priorityIncreasePointer = i3;
            return i3 + 65536;
        }
    }

    public DispatchQueue getFileLoaderQueue() {
        return fileLoaderQueue;
    }

    public void setLocalPathTo(TLObject tLObject, String str) {
        int i;
        if (tLObject instanceof TLRPC$Document) {
            TLRPC$Document tLRPC$Document = (TLRPC$Document) tLObject;
            if (tLRPC$Document.key != null) {
                i = 4;
            } else if (MessageObject.isVoiceDocument(tLRPC$Document)) {
                i = 1;
            } else {
                i = MessageObject.isVideoDocument(tLRPC$Document) ? 2 : 3;
            }
            this.filePathDatabase.putPath(tLRPC$Document.id, tLRPC$Document.dc_id, i, 1, str);
        } else if (tLObject instanceof TLRPC$PhotoSize) {
            TLRPC$PhotoSize tLRPC$PhotoSize = (TLRPC$PhotoSize) tLObject;
            if ((tLRPC$PhotoSize instanceof TLRPC$TL_photoStrippedSize) || (tLRPC$PhotoSize instanceof TLRPC$TL_photoPathSize)) {
                return;
            }
            TLRPC$FileLocation tLRPC$FileLocation = tLRPC$PhotoSize.location;
            this.filePathDatabase.putPath(tLRPC$FileLocation.volume_id, tLRPC$FileLocation.dc_id + (tLRPC$FileLocation.local_id << 16), (tLRPC$FileLocation == null || tLRPC$FileLocation.key != null || (tLRPC$FileLocation.volume_id == -2147483648L && tLRPC$FileLocation.local_id < 0) || tLRPC$PhotoSize.size < 0) ? 4 : 0, 1, str);
        }
    }

    public static FileLoader getInstance(int i) {
        FileLoader[] fileLoaderArr = Instance;
        FileLoader fileLoader = fileLoaderArr[i];
        if (fileLoader == null) {
            synchronized (FileLoader.class) {
                fileLoader = fileLoaderArr[i];
                if (fileLoader == null) {
                    fileLoader = new FileLoader(i);
                    fileLoaderArr[i] = fileLoader;
                }
            }
        }
        return fileLoader;
    }

    public FileLoader(int i) {
        super(i);
        this.smallFilesQueue = new FileLoaderPriorityQueue[5];
        this.largeFilesQueue = new FileLoaderPriorityQueue[5];
        this.uploadOperationQueue = new LinkedList<>();
        this.uploadSmallOperationQueue = new LinkedList<>();
        this.uploadOperationPaths = new ConcurrentHashMap<>();
        this.uploadOperationPathsEnc = new ConcurrentHashMap<>();
        this.currentUploadOperationsCount = 0;
        this.currentUploadSmallOperationsCount = 0;
        this.loadOperationPaths = new ConcurrentHashMap<>();
        this.loadOperationPathsUI = new ConcurrentHashMap<>(10, 1.0f, 2);
        this.uploadSizes = new HashMap<>();
        this.loadingVideos = new HashMap<>();
        this.delegate = null;
        this.parentObjectReferences = new ConcurrentHashMap<>();
        this.dumpFilesQueueRunnable = new Runnable() { // from class: org.telegram.messenger.FileLoader$$ExternalSyntheticLambda2
            @Override // java.lang.Runnable
            public final void run() {
                FileLoader.this.lambda$new$16();
            }
        };
        this.filePathDatabase = new FilePathDatabase(i);
        int i2 = 0;
        while (true) {
            FileLoaderPriorityQueue[] fileLoaderPriorityQueueArr = this.smallFilesQueue;
            if (i2 < fileLoaderPriorityQueueArr.length) {
                StringBuilder sb = new StringBuilder();
                sb.append("smallFilesQueue dc");
                int i3 = i2 + 1;
                sb.append(i3);
                fileLoaderPriorityQueueArr[i2] = new FileLoaderPriorityQueue(i, sb.toString(), 0, fileLoaderQueue);
                FileLoaderPriorityQueue[] fileLoaderPriorityQueueArr2 = this.largeFilesQueue;
                fileLoaderPriorityQueueArr2[i2] = new FileLoaderPriorityQueue(i, "largeFilesQueue dc" + i3, 1, fileLoaderQueue);
                i2 = i3;
            } else {
                dumpFilesQueue();
                return;
            }
        }
    }

    public static void setMediaDirs(SparseArray<File> sparseArray) {
        mediaDirs = sparseArray;
    }

    public static File checkDirectory(int i) {
        return mediaDirs.get(i);
    }

    public static File getDirectory(int i) {
        File file = mediaDirs.get(i);
        if (file == null && i != 4) {
            file = mediaDirs.get(4);
        }
        if (BuildVars.NO_SCOPED_STORAGE && file != null) {
            try {
                if (!file.isDirectory()) {
                    file.mkdirs();
                }
            } catch (Exception unused) {
            }
        }
        return file;
    }

    public int getFileReference(Object obj) {
        int i = this.lastReferenceId;
        this.lastReferenceId = i + 1;
        this.parentObjectReferences.put(Integer.valueOf(i), obj);
        return i;
    }

    public Object getParentObject(int i) {
        return this.parentObjectReferences.get(Integer.valueOf(i));
    }

    /* renamed from: setLoadingVideoInternal */
    public void lambda$setLoadingVideo$0(TLRPC$Document tLRPC$Document, boolean z) {
        String attachFileName = getAttachFileName(tLRPC$Document);
        StringBuilder sb = new StringBuilder();
        sb.append(attachFileName);
        sb.append(z ? "p" : "");
        this.loadingVideos.put(sb.toString(), Boolean.TRUE);
        getNotificationCenter().lambda$postNotificationNameOnUIThread$1(NotificationCenter.videoLoadingStateChanged, attachFileName);
    }

    public void setLoadingVideo(final TLRPC$Document tLRPC$Document, final boolean z, boolean z2) {
        if (tLRPC$Document == null) {
            return;
        }
        if (z2) {
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.FileLoader$$ExternalSyntheticLambda9
                @Override // java.lang.Runnable
                public final void run() {
                    FileLoader.this.lambda$setLoadingVideo$0(tLRPC$Document, z);
                }
            });
        } else {
            lambda$setLoadingVideo$0(tLRPC$Document, z);
        }
    }

    public void setLoadingVideoForPlayer(TLRPC$Document tLRPC$Document, boolean z) {
        if (tLRPC$Document == null) {
            return;
        }
        String attachFileName = getAttachFileName(tLRPC$Document);
        HashMap<String, Boolean> hashMap = this.loadingVideos;
        StringBuilder sb = new StringBuilder();
        sb.append(attachFileName);
        sb.append(z ? "" : "p");
        if (hashMap.containsKey(sb.toString())) {
            HashMap<String, Boolean> hashMap2 = this.loadingVideos;
            StringBuilder sb2 = new StringBuilder();
            sb2.append(attachFileName);
            sb2.append(z ? "p" : "");
            hashMap2.put(sb2.toString(), Boolean.TRUE);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* renamed from: removeLoadingVideoInternal */
    public void lambda$removeLoadingVideo$1(TLRPC$Document tLRPC$Document, boolean z) {
        String attachFileName = getAttachFileName(tLRPC$Document);
        StringBuilder sb = new StringBuilder();
        sb.append(attachFileName);
        sb.append(z ? "p" : "");
        if (this.loadingVideos.remove(sb.toString()) != null) {
            getNotificationCenter().lambda$postNotificationNameOnUIThread$1(NotificationCenter.videoLoadingStateChanged, attachFileName);
        }
    }

    public void removeLoadingVideo(final TLRPC$Document tLRPC$Document, final boolean z, boolean z2) {
        if (tLRPC$Document == null) {
            return;
        }
        if (z2) {
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.FileLoader$$ExternalSyntheticLambda10
                @Override // java.lang.Runnable
                public final void run() {
                    FileLoader.this.lambda$removeLoadingVideo$1(tLRPC$Document, z);
                }
            });
        } else {
            lambda$removeLoadingVideo$1(tLRPC$Document, z);
        }
    }

    public boolean isLoadingVideo(TLRPC$Document tLRPC$Document, boolean z) {
        if (tLRPC$Document != null) {
            HashMap<String, Boolean> hashMap = this.loadingVideos;
            StringBuilder sb = new StringBuilder();
            sb.append(getAttachFileName(tLRPC$Document));
            sb.append(z ? "p" : "");
            if (hashMap.containsKey(sb.toString())) {
                return true;
            }
        }
        return false;
    }

    public boolean isLoadingVideoAny(TLRPC$Document tLRPC$Document) {
        return isLoadingVideo(tLRPC$Document, false) || isLoadingVideo(tLRPC$Document, true);
    }

    public void cancelFileUpload(final String str, final boolean z) {
        if (str == null) {
            return;
        }
        fileLoaderQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.FileLoader$$ExternalSyntheticLambda13
            @Override // java.lang.Runnable
            public final void run() {
                FileLoader.this.lambda$cancelFileUpload$2(z, str);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$cancelFileUpload$2(boolean z, String str) {
        FileUploadOperation fileUploadOperation;
        if (!z) {
            fileUploadOperation = this.uploadOperationPaths.get(str);
        } else {
            fileUploadOperation = this.uploadOperationPathsEnc.get(str);
        }
        this.uploadSizes.remove(str);
        if (fileUploadOperation != null) {
            this.uploadOperationPathsEnc.remove(str);
            this.uploadOperationQueue.remove(fileUploadOperation);
            this.uploadSmallOperationQueue.remove(fileUploadOperation);
            fileUploadOperation.cancel();
        }
    }

    public void checkUploadNewDataAvailable(String str, boolean z, long j, long j2) {
        checkUploadNewDataAvailable(str, z, j, j2, null);
    }

    public void checkUploadNewDataAvailable(final String str, final boolean z, final long j, final long j2, final Float f) {
        fileLoaderQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.FileLoader$$ExternalSyntheticLambda15
            @Override // java.lang.Runnable
            public final void run() {
                FileLoader.this.lambda$checkUploadNewDataAvailable$3(z, str, j, j2, f);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$checkUploadNewDataAvailable$3(boolean z, String str, long j, long j2, Float f) {
        FileUploadOperation fileUploadOperation;
        if (z) {
            fileUploadOperation = this.uploadOperationPathsEnc.get(str);
        } else {
            fileUploadOperation = this.uploadOperationPaths.get(str);
        }
        FileUploadOperation fileUploadOperation2 = fileUploadOperation;
        if (fileUploadOperation2 != null) {
            fileUploadOperation2.checkNewDataAvailable(j, j2, f);
        } else if (j2 != 0) {
            this.uploadSizes.put(str, Long.valueOf(j2));
        }
    }

    public void onNetworkChanged(final boolean z) {
        fileLoaderQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.FileLoader$$ExternalSyntheticLambda12
            @Override // java.lang.Runnable
            public final void run() {
                FileLoader.this.lambda$onNetworkChanged$4(z);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onNetworkChanged$4(boolean z) {
        for (Map.Entry<String, FileUploadOperation> entry : this.uploadOperationPaths.entrySet()) {
            entry.getValue().onNetworkChanged(z);
        }
        for (Map.Entry<String, FileUploadOperation> entry2 : this.uploadOperationPathsEnc.entrySet()) {
            entry2.getValue().onNetworkChanged(z);
        }
    }

    public void uploadFile(String str, boolean z, boolean z2, int i) {
        uploadFile(str, z, z2, 0L, i, false);
    }

    public void uploadFile(final String str, final boolean z, final boolean z2, final long j, final int i, final boolean z3) {
        if (str == null) {
            return;
        }
        fileLoaderQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.FileLoader$$ExternalSyntheticLambda14
            @Override // java.lang.Runnable
            public final void run() {
                FileLoader.this.lambda$uploadFile$5(z, str, j, i, z3, z2);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$uploadFile$5(boolean z, String str, long j, int i, boolean z2, boolean z3) {
        long j2;
        if (z) {
            if (this.uploadOperationPathsEnc.containsKey(str)) {
                return;
            }
        } else if (this.uploadOperationPaths.containsKey(str)) {
            return;
        }
        if (j == 0 || this.uploadSizes.get(str) == null) {
            j2 = j;
        } else {
            this.uploadSizes.remove(str);
            j2 = 0;
        }
        FileUploadOperation fileUploadOperation = new FileUploadOperation(this.currentAccount, str, z, j2, i);
        FileLoaderDelegate fileLoaderDelegate = this.delegate;
        if (fileLoaderDelegate != null && j != 0) {
            fileLoaderDelegate.fileUploadProgressChanged(fileUploadOperation, str, 0L, j, z);
        }
        if (z) {
            this.uploadOperationPathsEnc.put(str, fileUploadOperation);
        } else {
            this.uploadOperationPaths.put(str, fileUploadOperation);
        }
        if (z2) {
            fileUploadOperation.setForceSmallFile();
        }
        fileUploadOperation.setDelegate(new 1(z, str, z3));
        if (z3) {
            int i2 = this.currentUploadSmallOperationsCount;
            if (i2 < 1) {
                this.currentUploadSmallOperationsCount = i2 + 1;
                fileUploadOperation.start();
                return;
            }
            this.uploadSmallOperationQueue.add(fileUploadOperation);
            return;
        }
        int i3 = this.currentUploadOperationsCount;
        if (i3 < 1) {
            this.currentUploadOperationsCount = i3 + 1;
            fileUploadOperation.start();
            return;
        }
        this.uploadOperationQueue.add(fileUploadOperation);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public class 1 implements FileUploadOperation.FileUploadOperationDelegate {
        final /* synthetic */ boolean val$encrypted;
        final /* synthetic */ String val$location;
        final /* synthetic */ boolean val$small;

        1(boolean z, String str, boolean z2) {
            this.val$encrypted = z;
            this.val$location = str;
            this.val$small = z2;
        }

        @Override // org.telegram.messenger.FileUploadOperation.FileUploadOperationDelegate
        public void didFinishUploadingFile(final FileUploadOperation fileUploadOperation, final TLRPC$InputFile tLRPC$InputFile, final TLRPC$InputEncryptedFile tLRPC$InputEncryptedFile, final byte[] bArr, final byte[] bArr2) {
            DispatchQueue dispatchQueue = FileLoader.fileLoaderQueue;
            final boolean z = this.val$encrypted;
            final String str = this.val$location;
            final boolean z2 = this.val$small;
            dispatchQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.FileLoader$1$$ExternalSyntheticLambda1
                @Override // java.lang.Runnable
                public final void run() {
                    FileLoader.1.this.lambda$didFinishUploadingFile$0(z, str, z2, tLRPC$InputFile, tLRPC$InputEncryptedFile, bArr, bArr2, fileUploadOperation);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$didFinishUploadingFile$0(boolean z, String str, boolean z2, TLRPC$InputFile tLRPC$InputFile, TLRPC$InputEncryptedFile tLRPC$InputEncryptedFile, byte[] bArr, byte[] bArr2, FileUploadOperation fileUploadOperation) {
            FileUploadOperation fileUploadOperation2;
            FileUploadOperation fileUploadOperation3;
            if (z) {
                FileLoader.this.uploadOperationPathsEnc.remove(str);
            } else {
                FileLoader.this.uploadOperationPaths.remove(str);
            }
            if (z2) {
                FileLoader.access$810(FileLoader.this);
                if (FileLoader.this.currentUploadSmallOperationsCount < 1 && (fileUploadOperation3 = (FileUploadOperation) FileLoader.this.uploadSmallOperationQueue.poll()) != null) {
                    FileLoader.access$808(FileLoader.this);
                    fileUploadOperation3.start();
                }
            } else {
                FileLoader.access$1010(FileLoader.this);
                if (FileLoader.this.currentUploadOperationsCount < 1 && (fileUploadOperation2 = (FileUploadOperation) FileLoader.this.uploadOperationQueue.poll()) != null) {
                    FileLoader.access$1008(FileLoader.this);
                    fileUploadOperation2.start();
                }
            }
            if (FileLoader.this.delegate != null) {
                FileLoader.this.delegate.fileDidUploaded(str, tLRPC$InputFile, tLRPC$InputEncryptedFile, bArr, bArr2, fileUploadOperation.getTotalFileSize());
            }
        }

        @Override // org.telegram.messenger.FileUploadOperation.FileUploadOperationDelegate
        public void didFailedUploadingFile(FileUploadOperation fileUploadOperation) {
            DispatchQueue dispatchQueue = FileLoader.fileLoaderQueue;
            final boolean z = this.val$encrypted;
            final String str = this.val$location;
            final boolean z2 = this.val$small;
            dispatchQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.FileLoader$1$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    FileLoader.1.this.lambda$didFailedUploadingFile$1(z, str, z2);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$didFailedUploadingFile$1(boolean z, String str, boolean z2) {
            FileUploadOperation fileUploadOperation;
            FileUploadOperation fileUploadOperation2;
            if (z) {
                FileLoader.this.uploadOperationPathsEnc.remove(str);
            } else {
                FileLoader.this.uploadOperationPaths.remove(str);
            }
            if (FileLoader.this.delegate != null) {
                FileLoader.this.delegate.fileDidFailedUpload(str, z);
            }
            if (z2) {
                FileLoader.access$810(FileLoader.this);
                if (FileLoader.this.currentUploadSmallOperationsCount >= 1 || (fileUploadOperation2 = (FileUploadOperation) FileLoader.this.uploadSmallOperationQueue.poll()) == null) {
                    return;
                }
                FileLoader.access$808(FileLoader.this);
                fileUploadOperation2.start();
                return;
            }
            FileLoader.access$1010(FileLoader.this);
            if (FileLoader.this.currentUploadOperationsCount >= 1 || (fileUploadOperation = (FileUploadOperation) FileLoader.this.uploadOperationQueue.poll()) == null) {
                return;
            }
            FileLoader.access$1008(FileLoader.this);
            fileUploadOperation.start();
        }

        @Override // org.telegram.messenger.FileUploadOperation.FileUploadOperationDelegate
        public void didChangedUploadProgress(FileUploadOperation fileUploadOperation, long j, long j2) {
            if (FileLoader.this.delegate != null) {
                FileLoader.this.delegate.fileUploadProgressChanged(fileUploadOperation, this.val$location, j, j2, this.val$encrypted);
            }
        }
    }

    public void setForceStreamLoadingFile(final TLRPC$FileLocation tLRPC$FileLocation, final String str) {
        if (tLRPC$FileLocation == null) {
            return;
        }
        fileLoaderQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.FileLoader$$ExternalSyntheticLambda11
            @Override // java.lang.Runnable
            public final void run() {
                FileLoader.this.lambda$setForceStreamLoadingFile$6(tLRPC$FileLocation, str);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$setForceStreamLoadingFile$6(TLRPC$FileLocation tLRPC$FileLocation, String str) {
        String attachFileName = getAttachFileName(tLRPC$FileLocation, str);
        this.forceLoadingFile = attachFileName;
        FileLoadOperation fileLoadOperation = this.loadOperationPaths.get(attachFileName);
        if (fileLoadOperation != null) {
            if (fileLoadOperation.isPreloadVideoOperation()) {
                fileLoadOperation.setIsPreloadVideoOperation(false);
            }
            fileLoadOperation.setForceRequest(true);
            fileLoadOperation.setPriority(getPriorityValue(4));
            fileLoadOperation.getQueue().remove(fileLoadOperation);
            fileLoadOperation.getQueue().add(fileLoadOperation);
            fileLoadOperation.getQueue().checkLoadingOperations();
        }
    }

    public void cancelLoadFile(TLRPC$Document tLRPC$Document) {
        cancelLoadFile(tLRPC$Document, false);
    }

    public void cancelLoadFile(TLRPC$Document tLRPC$Document, boolean z) {
        cancelLoadFile(tLRPC$Document, null, null, null, null, null, z);
    }

    public void cancelLoadFile(SecureDocument secureDocument) {
        cancelLoadFile(null, secureDocument, null, null, null, null, false);
    }

    public void cancelLoadFile(WebFile webFile) {
        cancelLoadFile(null, null, webFile, null, null, null, false);
    }

    public void cancelLoadFile(TLRPC$PhotoSize tLRPC$PhotoSize) {
        cancelLoadFile(tLRPC$PhotoSize, false);
    }

    public void cancelLoadFile(TLRPC$PhotoSize tLRPC$PhotoSize, boolean z) {
        cancelLoadFile(null, null, null, tLRPC$PhotoSize.location, null, null, z);
    }

    public void cancelLoadFile(TLRPC$FileLocation tLRPC$FileLocation, String str) {
        cancelLoadFile(tLRPC$FileLocation, str, false);
    }

    public void cancelLoadFile(TLRPC$FileLocation tLRPC$FileLocation, String str, boolean z) {
        cancelLoadFile(null, null, null, tLRPC$FileLocation, str, null, z);
    }

    public void cancelLoadFile(String str) {
        cancelLoadFile(null, null, null, null, null, str, true);
    }

    public void cancelLoadFiles(ArrayList<String> arrayList) {
        int size = arrayList.size();
        for (int i = 0; i < size; i++) {
            cancelLoadFile(null, null, null, null, null, arrayList.get(i), true);
        }
    }

    private void cancelLoadFile(TLRPC$Document tLRPC$Document, SecureDocument secureDocument, WebFile webFile, TLRPC$FileLocation tLRPC$FileLocation, String str, final String str2, boolean z) {
        if (tLRPC$FileLocation == null && tLRPC$Document == null && webFile == null && secureDocument == null && TextUtils.isEmpty(str2)) {
            return;
        }
        if (tLRPC$FileLocation != null) {
            str2 = getAttachFileName(tLRPC$FileLocation, str);
        } else if (tLRPC$Document != null) {
            str2 = getAttachFileName(tLRPC$Document);
        } else if (secureDocument != null) {
            str2 = getAttachFileName(secureDocument);
        } else if (webFile != null) {
            str2 = getAttachFileName(webFile);
        }
        LoadOperationUIObject remove = this.loadOperationPathsUI.remove(str2);
        Runnable runnable = remove != null ? remove.loadInternalRunnable : null;
        boolean z2 = remove != null;
        if (runnable != null) {
            fileLoaderQueue.cancelRunnable(runnable);
        }
        fileLoaderQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.FileLoader$$ExternalSyntheticLambda4
            @Override // java.lang.Runnable
            public final void run() {
                FileLoader.this.lambda$cancelLoadFile$7(str2);
            }
        });
        if (!z2 || tLRPC$Document == null) {
            return;
        }
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.FileLoader$$ExternalSyntheticLambda1
            @Override // java.lang.Runnable
            public final void run() {
                FileLoader.this.lambda$cancelLoadFile$8();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$cancelLoadFile$7(String str) {
        FileLoadOperation remove = this.loadOperationPaths.remove(str);
        if (remove != null) {
            remove.getQueue().cancel(remove);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$cancelLoadFile$8() {
        getNotificationCenter().lambda$postNotificationNameOnUIThread$1(NotificationCenter.onDownloadingFilesChanged, new Object[0]);
    }

    public void changePriority(final int i, TLRPC$Document tLRPC$Document, SecureDocument secureDocument, WebFile webFile, TLRPC$FileLocation tLRPC$FileLocation, String str, final String str2) {
        if (tLRPC$FileLocation == null && tLRPC$Document == null && webFile == null && secureDocument == null && TextUtils.isEmpty(str2)) {
            return;
        }
        if (tLRPC$FileLocation != null) {
            str2 = getAttachFileName(tLRPC$FileLocation, str);
        } else if (tLRPC$Document != null) {
            str2 = getAttachFileName(tLRPC$Document);
        } else if (secureDocument != null) {
            str2 = getAttachFileName(secureDocument);
        } else if (webFile != null) {
            str2 = getAttachFileName(webFile);
        }
        fileLoaderQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.FileLoader$$ExternalSyntheticLambda5
            @Override // java.lang.Runnable
            public final void run() {
                FileLoader.this.lambda$changePriority$9(str2, i);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$changePriority$9(String str, int i) {
        int priorityValue;
        FileLoadOperation fileLoadOperation = this.loadOperationPaths.get(str);
        if (fileLoadOperation == null || fileLoadOperation.getPriority() == (priorityValue = getPriorityValue(i))) {
            return;
        }
        fileLoadOperation.setPriority(priorityValue);
        FileLoaderPriorityQueue queue = fileLoadOperation.getQueue();
        queue.remove(fileLoadOperation);
        queue.add(fileLoadOperation);
        queue.checkLoadingOperations();
        FileLog.d("update priority " + str + " position in queue " + fileLoadOperation.getPositionInQueue() + " account=" + this.currentAccount);
    }

    public void cancelLoadAllFiles() {
        for (final String str : this.loadOperationPathsUI.keySet()) {
            LoadOperationUIObject loadOperationUIObject = this.loadOperationPathsUI.get(str);
            Runnable runnable = loadOperationUIObject != null ? loadOperationUIObject.loadInternalRunnable : null;
            if (runnable != null) {
                fileLoaderQueue.cancelRunnable(runnable);
            }
            fileLoaderQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.FileLoader$$ExternalSyntheticLambda3
                @Override // java.lang.Runnable
                public final void run() {
                    FileLoader.this.lambda$cancelLoadAllFiles$10(str);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$cancelLoadAllFiles$10(String str) {
        FileLoadOperation remove = this.loadOperationPaths.remove(str);
        if (remove != null) {
            remove.getQueue().cancel(remove);
        }
    }

    public boolean isLoadingFile(String str) {
        return str != null && this.loadOperationPathsUI.containsKey(str);
    }

    public float getBufferedProgressFromPosition(float f, String str) {
        FileLoadOperation fileLoadOperation;
        if (TextUtils.isEmpty(str) || (fileLoadOperation = this.loadOperationPaths.get(str)) == null) {
            return 0.0f;
        }
        return fileLoadOperation.getDownloadedLengthFromOffset(f);
    }

    public void loadFile(ImageLocation imageLocation, Object obj, String str, int i, int i2) {
        if (imageLocation == null) {
            return;
        }
        loadFile(imageLocation.document, imageLocation.secureDocument, imageLocation.webFile, imageLocation.location, imageLocation, obj, str, imageLocation.getSize(), i, (i2 != 0 || (!imageLocation.isEncrypted() && (imageLocation.photoSize == null || imageLocation.getSize() != 0))) ? i2 : 1);
    }

    public void loadFile(SecureDocument secureDocument, int i) {
        if (secureDocument == null) {
            return;
        }
        loadFile(null, secureDocument, null, null, null, null, null, 0L, i, 1);
    }

    public void loadFile(TLRPC$Document tLRPC$Document, Object obj, int i, int i2) {
        if (tLRPC$Document == null) {
            return;
        }
        loadFile(tLRPC$Document, null, null, null, null, obj, null, 0L, i, (i2 != 0 || tLRPC$Document.key == null) ? i2 : 1);
    }

    public void loadFile(WebFile webFile, int i, int i2) {
        loadFile(null, null, webFile, null, null, null, null, 0L, i, i2);
    }

    /* JADX WARN: Code restructure failed: missing block: B:119:0x024e, code lost:
        if (r7 != null) goto L92;
     */
    /* JADX WARN: Code restructure failed: missing block: B:120:0x0250, code lost:
        r5 = r7;
        r7 = true;
     */
    /* JADX WARN: Code restructure failed: missing block: B:121:0x0253, code lost:
        r7 = false;
     */
    /* JADX WARN: Code restructure failed: missing block: B:131:0x0272, code lost:
        if (r7 != null) goto L92;
     */
    /* JADX WARN: Removed duplicated region for block: B:116:0x0243  */
    /* JADX WARN: Removed duplicated region for block: B:144:0x029f  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private FileLoadOperation loadFileInternal(TLRPC$Document tLRPC$Document, SecureDocument secureDocument, WebFile webFile, TLRPC$TL_fileLocationToBeDeprecated tLRPC$TL_fileLocationToBeDeprecated, ImageLocation imageLocation, Object obj, String str, long j, int i, FileLoadOperationStream fileLoadOperationStream, long j2, boolean z, int i2) {
        String str2;
        String attachFileName;
        String str3;
        int i3;
        FileLoader fileLoader;
        long j3;
        int i4;
        int i5;
        int i6;
        int i7;
        FileLoaderPriorityQueue fileLoaderPriorityQueue;
        int i8;
        File directory;
        String str4;
        String str5;
        boolean z2;
        File directory2;
        boolean z3;
        FileLoader fileLoader2;
        boolean z4;
        if (tLRPC$TL_fileLocationToBeDeprecated != null) {
            attachFileName = getAttachFileName(tLRPC$TL_fileLocationToBeDeprecated, str);
        } else if (secureDocument != null) {
            attachFileName = getAttachFileName(secureDocument);
        } else if (tLRPC$Document != null) {
            attachFileName = getAttachFileName(tLRPC$Document);
        } else if (webFile != null) {
            attachFileName = getAttachFileName(webFile);
        } else {
            str2 = null;
            if (str2 != null || str2.contains("-2147483648")) {
                return null;
            }
            if (str2.startsWith("0_0")) {
                FileLog.e(new RuntimeException("cant get hash from " + tLRPC$Document));
                return null;
            }
            if (i2 != 10 && !TextUtils.isEmpty(str2) && !str2.contains("-2147483648")) {
                this.loadOperationPathsUI.put(str2, new LoadOperationUIObject(null));
            }
            if (tLRPC$Document != null && (obj instanceof MessageObject)) {
                MessageObject messageObject = (MessageObject) obj;
                if (messageObject.putInDownloadsStore && !messageObject.isAnyKindOfSticker()) {
                    getDownloadController().startDownloadFile(tLRPC$Document, messageObject);
                }
            }
            FileLoadOperation fileLoadOperation = this.loadOperationPaths.get(str2);
            int priorityValue = getPriorityValue(i);
            if (fileLoadOperation != null) {
                if (i2 == 10 || !fileLoadOperation.isPreloadVideoOperation()) {
                    z4 = false;
                } else {
                    z4 = false;
                    fileLoadOperation.setIsPreloadVideoOperation(false);
                }
                fileLoadOperation.setForceRequest(priorityValue > 0);
                fileLoadOperation.setStream(fileLoadOperationStream, z, j2);
                if (fileLoadOperation.getPriority() != priorityValue) {
                    fileLoadOperation.setPriority(priorityValue);
                    z4 = true;
                }
                fileLoadOperation.getQueue().add(fileLoadOperation);
                fileLoadOperation.updateProgress();
                if (z4) {
                    fileLoadOperation.getQueue().checkLoadingOperations();
                }
                FileLog.d("load operation update position fileName=" + str2 + " position in queue " + fileLoadOperation.getPositionInQueue() + " preloadFinish " + fileLoadOperation.isPreloadFinished());
                return fileLoadOperation;
            }
            File directory3 = getDirectory(4);
            if (secureDocument != null) {
                fileLoadOperation = new FileLoadOperation(secureDocument);
                i4 = 0;
                i5 = 3;
                fileLoader = this;
                str3 = " position in queue ";
                j3 = 0;
                i3 = 0;
            } else if (tLRPC$TL_fileLocationToBeDeprecated != null) {
                long j4 = tLRPC$TL_fileLocationToBeDeprecated.volume_id;
                str3 = " position in queue ";
                i5 = 0;
                i3 = 0;
                fileLoader = this;
                i4 = tLRPC$TL_fileLocationToBeDeprecated.dc_id + (tLRPC$TL_fileLocationToBeDeprecated.local_id << 16);
                fileLoadOperation = new FileLoadOperation(imageLocation, obj, str, j);
                j3 = j4;
            } else {
                str3 = " position in queue ";
                if (tLRPC$Document != null) {
                    fileLoadOperation = new FileLoadOperation(tLRPC$Document, obj);
                    if (MessageObject.isVoiceDocument(tLRPC$Document)) {
                        j3 = 0;
                        i6 = 0;
                        i7 = 1;
                    } else if (MessageObject.isVideoDocument(tLRPC$Document)) {
                        j3 = tLRPC$Document.id;
                        i6 = tLRPC$Document.dc_id;
                        i7 = 2;
                    } else {
                        j3 = tLRPC$Document.id;
                        i6 = tLRPC$Document.dc_id;
                        i7 = 3;
                    }
                    if (MessageObject.isRoundVideoDocument(tLRPC$Document)) {
                        i4 = 0;
                        i3 = 0;
                        i5 = i7;
                        j3 = 0;
                    } else {
                        i3 = 0;
                        i4 = i6;
                        i5 = i7;
                    }
                    fileLoader = this;
                } else {
                    if (webFile != null) {
                        i3 = 0;
                        fileLoader = this;
                        fileLoadOperation = new FileLoadOperation(fileLoader.currentAccount, webFile);
                        if (webFile.location == null) {
                            if (MessageObject.isVoiceWebDocument(webFile)) {
                                j3 = 0;
                                i4 = 0;
                                i5 = 1;
                            } else if (MessageObject.isVideoWebDocument(webFile)) {
                                j3 = 0;
                                i4 = 0;
                                i5 = 2;
                            } else if (MessageObject.isImageWebDocument(webFile)) {
                                j3 = 0;
                                i4 = 0;
                                i5 = 0;
                            } else {
                                j3 = 0;
                                i4 = 0;
                                i5 = 3;
                            }
                        }
                    } else {
                        i3 = 0;
                        fileLoader = this;
                    }
                    j3 = 0;
                    i4 = 0;
                    i5 = 4;
                }
            }
            int clamp = Utilities.clamp(fileLoadOperation.getDatacenterId() - 1, 4, i3);
            boolean z5 = obj instanceof TLRPC$StoryItem;
            if (fileLoadOperation.totalBytesCount > 20971520 || z5) {
                fileLoaderPriorityQueue = fileLoader.largeFilesQueue[clamp];
            } else {
                fileLoaderPriorityQueue = fileLoader.smallFilesQueue[clamp];
            }
            FileLoaderPriorityQueue fileLoaderPriorityQueue2 = fileLoaderPriorityQueue;
            if (i2 != 0 && i2 != 10 && !z5) {
                if (i2 == 2) {
                    fileLoadOperation.setEncryptFile(true);
                }
                i8 = priorityValue;
                str4 = str2;
                directory = directory3;
            } else if (j3 != 0) {
                String path = getFileDatabase().getPath(j3, i4, i5, true);
                i8 = priorityValue;
                if (path != null) {
                    File file = new File(path);
                    if (file.exists()) {
                        str5 = file.getName();
                        directory = file.getParentFile();
                        z2 = true;
                        if (!z2) {
                            File directory4 = getDirectory(i5);
                            if (z5) {
                                directory2 = getDirectory(6);
                            } else if ((i5 != 0 && i5 != 2) || !fileLoader.canSaveToPublicStorage(obj)) {
                                if (TextUtils.isEmpty(getDocumentFileName(tLRPC$Document)) || !fileLoader.canSaveAsFile(obj)) {
                                    directory = directory4;
                                    str5 = str2;
                                } else {
                                    String documentFileName = getDocumentFileName(tLRPC$Document);
                                    File directory5 = getDirectory(5);
                                    if (directory5 != null) {
                                        str5 = documentFileName;
                                        directory = directory5;
                                        z3 = true;
                                        if (z3) {
                                            fileLoadOperation.pathSaveData = new FilePathDatabase.PathData(j3, i4, i5);
                                        }
                                    } else {
                                        directory = directory4;
                                        str5 = documentFileName;
                                    }
                                }
                                z3 = false;
                                if (z3) {
                                }
                            } else if (i5 == 0) {
                                directory2 = getDirectory(100);
                            } else {
                                directory2 = getDirectory(MEDIA_DIR_VIDEO_PUBLIC);
                            }
                            directory = directory4;
                            str5 = str2;
                            if (z3) {
                            }
                        }
                        str4 = str5;
                    }
                }
                str5 = str2;
                directory = directory3;
                z2 = false;
                if (!z2) {
                }
                str4 = str5;
            } else {
                i8 = priorityValue;
                directory = getDirectory(i5);
                str4 = str2;
            }
            int i9 = i8;
            String str6 = str2;
            fileLoadOperation.setPaths(fileLoader.currentAccount, str6, fileLoaderPriorityQueue2, directory, directory3, str4);
            if (i2 == 10) {
                fileLoadOperation.setIsPreloadVideoOperation(true);
            }
            FileLoadOperation fileLoadOperation2 = fileLoadOperation;
            fileLoadOperation2.setDelegate(new 2(obj, tLRPC$Document, str6, i5));
            fileLoader.loadOperationPaths.put(str6, fileLoadOperation2);
            fileLoadOperation2.setPriority(i9);
            if (fileLoadOperationStream != null) {
                fileLoadOperation2.setStream(fileLoadOperationStream, z, j2);
            }
            fileLoaderPriorityQueue2.add(fileLoadOperation2);
            fileLoaderPriorityQueue2.checkLoadingOperations(fileLoadOperation2.isStory && i9 >= 3);
            if (BuildVars.LOGS_ENABLED) {
                FileLog.d("create load operation fileName=" + str6 + " documentName=" + getDocumentFileName(tLRPC$Document) + "size=" + AndroidUtilities.formatFileSize(fileLoadOperation2.totalBytesCount) + str3 + fileLoadOperation2.getPositionInQueue() + " account=" + fileLoader2.currentAccount);
            }
            return fileLoadOperation2;
        }
        str2 = attachFileName;
        if (str2 != null) {
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public class 2 implements FileLoadOperation.FileLoadOperationDelegate {
        final /* synthetic */ TLRPC$Document val$document;
        final /* synthetic */ String val$fileName;
        final /* synthetic */ int val$finalType;
        final /* synthetic */ Object val$parentObject;

        2(Object obj, TLRPC$Document tLRPC$Document, String str, int i) {
            this.val$parentObject = obj;
            this.val$document = tLRPC$Document;
            this.val$fileName = str;
            this.val$finalType = i;
        }

        @Override // org.telegram.messenger.FileLoadOperation.FileLoadOperationDelegate
        public void didPreFinishLoading(final FileLoadOperation fileLoadOperation, File file) {
            final FileLoaderPriorityQueue queue = fileLoadOperation.getQueue();
            FileLoader.fileLoaderQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.FileLoader$2$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    FileLoader.2.lambda$didPreFinishLoading$0(FileLoadOperation.this, queue);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static /* synthetic */ void lambda$didPreFinishLoading$0(FileLoadOperation fileLoadOperation, FileLoaderPriorityQueue fileLoaderPriorityQueue) {
            fileLoadOperation.preFinished = true;
            fileLoaderPriorityQueue.checkLoadingOperations();
        }

        @Override // org.telegram.messenger.FileLoadOperation.FileLoadOperationDelegate
        public void didFinishLoadingFile(FileLoadOperation fileLoadOperation, File file) {
            if (!fileLoadOperation.isPreloadVideoOperation() && fileLoadOperation.isPreloadFinished()) {
                FileLoader.this.checkDownloadQueue(fileLoadOperation, fileLoadOperation.getQueue(), 0L);
                return;
            }
            FilePathDatabase.FileMeta fileMetadataFromParent = FileLoader.getFileMetadataFromParent(FileLoader.this.currentAccount, this.val$parentObject);
            if (fileMetadataFromParent != null) {
                FileLoader.this.getFileLoader().getFileDatabase().saveFileDialogId(file, fileMetadataFromParent);
            }
            Object obj = this.val$parentObject;
            if (obj instanceof MessageObject) {
                MessageObject messageObject = (MessageObject) obj;
                if (this.val$document != null && messageObject.putInDownloadsStore) {
                    FileLoader.this.getDownloadController().onDownloadComplete(messageObject);
                }
            }
            if (!fileLoadOperation.isPreloadVideoOperation()) {
                FileLoader.this.loadOperationPathsUI.remove(this.val$fileName);
                if (FileLoader.this.delegate != null) {
                    FileLoader.this.delegate.fileDidLoaded(this.val$fileName, file, this.val$parentObject, this.val$finalType);
                }
            }
            FileLoader.this.checkDownloadQueue(fileLoadOperation, fileLoadOperation.getQueue(), 0L);
        }

        @Override // org.telegram.messenger.FileLoadOperation.FileLoadOperationDelegate
        public void didFailedLoadingFile(FileLoadOperation fileLoadOperation, int i) {
            FileLoader.this.loadOperationPathsUI.remove(this.val$fileName);
            FileLoader.this.checkDownloadQueue(fileLoadOperation, fileLoadOperation.getQueue());
            if (FileLoader.this.delegate != null) {
                FileLoader.this.delegate.fileDidFailedLoad(this.val$fileName, i);
            }
            if (this.val$document != null && (this.val$parentObject instanceof MessageObject) && i == 0) {
                FileLoader.this.getDownloadController().onDownloadFail((MessageObject) this.val$parentObject, i);
            } else if (i == -1) {
                LaunchActivity.checkFreeDiscSpaceStatic(2);
            }
        }

        @Override // org.telegram.messenger.FileLoadOperation.FileLoadOperationDelegate
        public void didChangedLoadProgress(FileLoadOperation fileLoadOperation, long j, long j2) {
            if (FileLoader.this.delegate != null) {
                FileLoader.this.delegate.fileLoadProgressChanged(fileLoadOperation, this.val$fileName, j, j2);
            }
        }

        @Override // org.telegram.messenger.FileLoadOperation.FileLoadOperationDelegate
        public void saveFilePath(FilePathDatabase.PathData pathData, File file) {
            FileLoader.this.getFileDatabase().putPath(pathData.id, pathData.dc, pathData.type, 0, file != null ? file.toString() : null);
        }

        @Override // org.telegram.messenger.FileLoadOperation.FileLoadOperationDelegate
        public boolean hasAnotherRefOnFile(String str) {
            return FileLoader.this.getFileDatabase().hasAnotherRefOnFile(str);
        }

        @Override // org.telegram.messenger.FileLoadOperation.FileLoadOperationDelegate
        public boolean isLocallyCreatedFile(String str) {
            return FileLoader.this.getFileDatabase().isLocallyCreated(str);
        }
    }

    private boolean canSaveAsFile(Object obj) {
        return (obj instanceof MessageObject) && ((MessageObject) obj).isDocument();
    }

    /* JADX WARN: Code restructure failed: missing block: B:27:0x0059, code lost:
        if (r12 != 2) goto L21;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private boolean canSaveToPublicStorage(Object obj) {
        if (BuildVars.NO_SCOPED_STORAGE) {
            return false;
        }
        FilePathDatabase.FileMeta fileMetadataFromParent = getFileMetadataFromParent(this.currentAccount, obj);
        MessageObject messageObject = null;
        if (fileMetadataFromParent != null) {
            long j = fileMetadataFromParent.dialogId;
            long j2 = -j;
            if (!getMessagesController().isChatNoForwards(getMessagesController().getChat(Long.valueOf(j2))) && !DialogObject.isEncryptedDialog(j)) {
                int i = 2;
                if (obj instanceof MessageObject) {
                    messageObject = (MessageObject) obj;
                    if (messageObject.isRoundVideo() || messageObject.isVoice() || messageObject.isAnyKindOfSticker() || messageObject.messageOwner.noforwards) {
                        return false;
                    }
                } else {
                    int i2 = fileMetadataFromParent.messageType;
                    if (i2 != 5) {
                        if (i2 != 13) {
                        }
                    }
                }
                if (j >= 0) {
                    i = 1;
                } else if (ChatObject.isChannelAndNotMegaGroup(getMessagesController().getChat(Long.valueOf(j2)))) {
                    i = 4;
                }
                if (SaveToGallerySettingsHelper.needSave(i, fileMetadataFromParent, messageObject, this.currentAccount)) {
                    return true;
                }
            }
        }
        return false;
    }

    private void addOperationToQueue(FileLoadOperation fileLoadOperation, LinkedList<FileLoadOperation> linkedList) {
        int priority = fileLoadOperation.getPriority();
        if (priority > 0) {
            int size = linkedList.size();
            int i = 0;
            int size2 = linkedList.size();
            while (true) {
                if (i >= size2) {
                    break;
                } else if (linkedList.get(i).getPriority() < priority) {
                    size = i;
                    break;
                } else {
                    i++;
                }
            }
            linkedList.add(size, fileLoadOperation);
            return;
        }
        linkedList.add(fileLoadOperation);
    }

    private void loadFile(final TLRPC$Document tLRPC$Document, final SecureDocument secureDocument, final WebFile webFile, final TLRPC$TL_fileLocationToBeDeprecated tLRPC$TL_fileLocationToBeDeprecated, final ImageLocation imageLocation, final Object obj, final String str, final long j, final int i, final int i2) {
        String str2;
        String attachFileName;
        if (tLRPC$TL_fileLocationToBeDeprecated != null) {
            attachFileName = getAttachFileName(tLRPC$TL_fileLocationToBeDeprecated, str);
        } else if (tLRPC$Document != null) {
            attachFileName = getAttachFileName(tLRPC$Document);
        } else if (webFile != null) {
            attachFileName = getAttachFileName(webFile);
        } else {
            str2 = null;
            Runnable runnable = new Runnable() { // from class: org.telegram.messenger.FileLoader$$ExternalSyntheticLambda8
                @Override // java.lang.Runnable
                public final void run() {
                    FileLoader.this.lambda$loadFile$11(tLRPC$Document, secureDocument, webFile, tLRPC$TL_fileLocationToBeDeprecated, imageLocation, obj, str, j, i, i2);
                }
            };
            if (i2 == 10 && !TextUtils.isEmpty(str2) && !str2.contains("-2147483648")) {
                LoadOperationUIObject loadOperationUIObject = new LoadOperationUIObject(null);
                loadOperationUIObject.loadInternalRunnable = runnable;
                this.loadOperationPathsUI.put(str2, loadOperationUIObject);
            }
            fileLoaderQueue.postRunnable(runnable);
        }
        str2 = attachFileName;
        Runnable runnable2 = new Runnable() { // from class: org.telegram.messenger.FileLoader$$ExternalSyntheticLambda8
            @Override // java.lang.Runnable
            public final void run() {
                FileLoader.this.lambda$loadFile$11(tLRPC$Document, secureDocument, webFile, tLRPC$TL_fileLocationToBeDeprecated, imageLocation, obj, str, j, i, i2);
            }
        };
        if (i2 == 10) {
        }
        fileLoaderQueue.postRunnable(runnable2);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$loadFile$11(TLRPC$Document tLRPC$Document, SecureDocument secureDocument, WebFile webFile, TLRPC$TL_fileLocationToBeDeprecated tLRPC$TL_fileLocationToBeDeprecated, ImageLocation imageLocation, Object obj, String str, long j, int i, int i2) {
        loadFileInternal(tLRPC$Document, secureDocument, webFile, tLRPC$TL_fileLocationToBeDeprecated, imageLocation, obj, str, j, i, null, 0L, false, i2);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public FileLoadOperation loadStreamFile(final FileLoadOperationStream fileLoadOperationStream, final TLRPC$Document tLRPC$Document, final ImageLocation imageLocation, final Object obj, final long j, final boolean z, final int i) {
        final CountDownLatch countDownLatch = new CountDownLatch(1);
        final FileLoadOperation[] fileLoadOperationArr = new FileLoadOperation[1];
        fileLoaderQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.FileLoader$$ExternalSyntheticLambda16
            @Override // java.lang.Runnable
            public final void run() {
                FileLoader.this.lambda$loadStreamFile$12(fileLoadOperationArr, tLRPC$Document, imageLocation, obj, i, fileLoadOperationStream, j, z, countDownLatch);
            }
        });
        awaitFileLoadOperation(countDownLatch, true);
        return fileLoadOperationArr[0];
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$loadStreamFile$12(FileLoadOperation[] fileLoadOperationArr, TLRPC$Document tLRPC$Document, ImageLocation imageLocation, Object obj, int i, FileLoadOperationStream fileLoadOperationStream, long j, boolean z, CountDownLatch countDownLatch) {
        String str = null;
        TLRPC$TL_fileLocationToBeDeprecated tLRPC$TL_fileLocationToBeDeprecated = (tLRPC$Document != null || imageLocation == null) ? null : imageLocation.location;
        if (tLRPC$Document == null && imageLocation != null) {
            str = "mp4";
        }
        fileLoadOperationArr[0] = loadFileInternal(tLRPC$Document, null, null, tLRPC$TL_fileLocationToBeDeprecated, imageLocation, obj, str, (tLRPC$Document != null || imageLocation == null) ? 0L : imageLocation.currentSize, i, fileLoadOperationStream, j, z, tLRPC$Document == null ? 1 : 0);
        countDownLatch.countDown();
    }

    private void awaitFileLoadOperation(CountDownLatch countDownLatch, boolean z) {
        try {
            countDownLatch.await();
        } catch (Exception e) {
            FileLog.e((Throwable) e, false);
            if (z) {
                awaitFileLoadOperation(countDownLatch, false);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void checkDownloadQueue(FileLoadOperation fileLoadOperation, FileLoaderPriorityQueue fileLoaderPriorityQueue) {
        checkDownloadQueue(fileLoadOperation, fileLoaderPriorityQueue, 0L);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void checkDownloadQueue(final FileLoadOperation fileLoadOperation, final FileLoaderPriorityQueue fileLoaderPriorityQueue, long j) {
        fileLoaderQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.FileLoader$$ExternalSyntheticLambda7
            @Override // java.lang.Runnable
            public final void run() {
                FileLoader.this.lambda$checkDownloadQueue$13(fileLoaderPriorityQueue, fileLoadOperation);
            }
        }, j);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$checkDownloadQueue$13(FileLoaderPriorityQueue fileLoaderPriorityQueue, FileLoadOperation fileLoadOperation) {
        if (fileLoaderPriorityQueue.remove(fileLoadOperation)) {
            this.loadOperationPaths.remove(fileLoadOperation.getFileName());
            fileLoaderPriorityQueue.checkLoadingOperations();
        }
    }

    public void setDelegate(FileLoaderDelegate fileLoaderDelegate) {
        this.delegate = fileLoaderDelegate;
    }

    public static String getMessageFileName(TLRPC$Message tLRPC$Message) {
        TLRPC$WebDocument tLRPC$WebDocument;
        TLRPC$PhotoSize closestPhotoSizeWithSize;
        TLRPC$PhotoSize closestPhotoSizeWithSize2;
        TLRPC$PhotoSize closestPhotoSizeWithSize3;
        if (tLRPC$Message == null) {
            return "";
        }
        if (tLRPC$Message instanceof TLRPC$TL_messageService) {
            TLRPC$Photo tLRPC$Photo = tLRPC$Message.action.photo;
            if (tLRPC$Photo != null) {
                ArrayList<TLRPC$PhotoSize> arrayList = tLRPC$Photo.sizes;
                if (arrayList.size() > 0 && (closestPhotoSizeWithSize3 = getClosestPhotoSizeWithSize(arrayList, AndroidUtilities.getPhotoSize())) != null) {
                    return getAttachFileName(closestPhotoSizeWithSize3);
                }
            }
        } else if (MessageObject.getMedia(tLRPC$Message) instanceof TLRPC$TL_messageMediaDocument) {
            return getAttachFileName(MessageObject.getMedia(tLRPC$Message).document);
        } else {
            if (MessageObject.getMedia(tLRPC$Message) instanceof TLRPC$TL_messageMediaPhoto) {
                ArrayList<TLRPC$PhotoSize> arrayList2 = MessageObject.getMedia(tLRPC$Message).photo.sizes;
                if (arrayList2.size() > 0 && (closestPhotoSizeWithSize2 = getClosestPhotoSizeWithSize(arrayList2, AndroidUtilities.getPhotoSize(), false, null, true)) != null) {
                    return getAttachFileName(closestPhotoSizeWithSize2);
                }
            } else if (MessageObject.getMedia(tLRPC$Message) instanceof TLRPC$TL_messageMediaWebPage) {
                if (MessageObject.getMedia(tLRPC$Message).webpage.document != null) {
                    return getAttachFileName(MessageObject.getMedia(tLRPC$Message).webpage.document);
                }
                if (MessageObject.getMedia(tLRPC$Message).webpage.photo != null) {
                    ArrayList<TLRPC$PhotoSize> arrayList3 = MessageObject.getMedia(tLRPC$Message).webpage.photo.sizes;
                    if (arrayList3.size() > 0 && (closestPhotoSizeWithSize = getClosestPhotoSizeWithSize(arrayList3, AndroidUtilities.getPhotoSize())) != null) {
                        return getAttachFileName(closestPhotoSizeWithSize);
                    }
                }
            } else if ((MessageObject.getMedia(tLRPC$Message) instanceof TLRPC$TL_messageMediaInvoice) && (tLRPC$WebDocument = ((TLRPC$TL_messageMediaInvoice) MessageObject.getMedia(tLRPC$Message)).webPhoto) != null) {
                return Utilities.MD5(tLRPC$WebDocument.url) + "." + ImageLoader.getHttpUrlExtension(tLRPC$WebDocument.url, getMimeTypePart(tLRPC$WebDocument.mime_type));
            }
        }
        return "";
    }

    public File getPathToMessage(TLRPC$Message tLRPC$Message) {
        return getPathToMessage(tLRPC$Message, true);
    }

    public File getPathToMessage(TLRPC$Message tLRPC$Message, boolean z) {
        TLRPC$PhotoSize closestPhotoSizeWithSize;
        TLRPC$PhotoSize closestPhotoSizeWithSize2;
        TLRPC$PhotoSize closestPhotoSizeWithSize3;
        if (tLRPC$Message == null) {
            return new File("");
        }
        if (tLRPC$Message instanceof TLRPC$TL_messageService) {
            TLRPC$Photo tLRPC$Photo = tLRPC$Message.action.photo;
            if (tLRPC$Photo != null) {
                ArrayList<TLRPC$PhotoSize> arrayList = tLRPC$Photo.sizes;
                if (arrayList.size() > 0 && (closestPhotoSizeWithSize3 = getClosestPhotoSizeWithSize(arrayList, AndroidUtilities.getPhotoSize())) != null) {
                    return getPathToAttach(closestPhotoSizeWithSize3, null, false, z);
                }
            }
        } else if (MessageObject.getMedia(tLRPC$Message) instanceof TLRPC$TL_messageMediaDocument) {
            return getPathToAttach(MessageObject.getMedia(tLRPC$Message).document, null, MessageObject.getMedia(tLRPC$Message).ttl_seconds != 0, z);
        } else if (MessageObject.getMedia(tLRPC$Message) instanceof TLRPC$TL_messageMediaPhoto) {
            ArrayList<TLRPC$PhotoSize> arrayList2 = MessageObject.getMedia(tLRPC$Message).photo.sizes;
            if (arrayList2.size() > 0 && (closestPhotoSizeWithSize2 = getClosestPhotoSizeWithSize(arrayList2, AndroidUtilities.getPhotoSize(), false, null, true)) != null) {
                return getPathToAttach(closestPhotoSizeWithSize2, null, MessageObject.getMedia(tLRPC$Message).ttl_seconds != 0, z);
            }
        } else if (MessageObject.getMedia(tLRPC$Message) instanceof TLRPC$TL_messageMediaWebPage) {
            if (MessageObject.getMedia(tLRPC$Message).webpage.document != null) {
                return getPathToAttach(MessageObject.getMedia(tLRPC$Message).webpage.document, null, false, z);
            }
            if (MessageObject.getMedia(tLRPC$Message).webpage.photo != null) {
                ArrayList<TLRPC$PhotoSize> arrayList3 = MessageObject.getMedia(tLRPC$Message).webpage.photo.sizes;
                if (arrayList3.size() > 0 && (closestPhotoSizeWithSize = getClosestPhotoSizeWithSize(arrayList3, AndroidUtilities.getPhotoSize())) != null) {
                    return getPathToAttach(closestPhotoSizeWithSize, null, false, z);
                }
            }
        } else if (MessageObject.getMedia(tLRPC$Message) instanceof TLRPC$TL_messageMediaInvoice) {
            return getPathToAttach(((TLRPC$TL_messageMediaInvoice) MessageObject.getMedia(tLRPC$Message)).photo, null, true, z);
        }
        return new File("");
    }

    public File getPathToAttach(TLObject tLObject) {
        return getPathToAttach(tLObject, null, false);
    }

    public File getPathToAttach(TLObject tLObject, boolean z) {
        return getPathToAttach(tLObject, null, z);
    }

    public File getPathToAttach(TLObject tLObject, String str, boolean z) {
        return getPathToAttach(tLObject, null, str, z, true);
    }

    public File getPathToAttach(TLObject tLObject, String str, boolean z, boolean z2) {
        return getPathToAttach(tLObject, null, str, z, z2);
    }

    /* JADX WARN: Removed duplicated region for block: B:110:0x0173  */
    /* JADX WARN: Removed duplicated region for block: B:112:0x017b  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public File getPathToAttach(TLObject tLObject, String str, String str2, boolean z, boolean z2) {
        File directory;
        long j;
        int i;
        long j2;
        int i2;
        int i3;
        int i4;
        int i5;
        int i6;
        String path;
        File file = null;
        int i7 = 4;
        if (z) {
            file = getDirectory(4);
        } else {
            if (tLObject instanceof TLRPC$Document) {
                TLRPC$Document tLRPC$Document = (TLRPC$Document) tLObject;
                if (!TextUtils.isEmpty(tLRPC$Document.localPath)) {
                    return new File(tLRPC$Document.localPath);
                }
                if (tLRPC$Document.key == null) {
                    if (MessageObject.isVoiceDocument(tLRPC$Document)) {
                        i7 = 1;
                    } else {
                        i7 = MessageObject.isVideoDocument(tLRPC$Document) ? 2 : 3;
                    }
                }
                j2 = tLRPC$Document.id;
                i6 = tLRPC$Document.dc_id;
                file = getDirectory(i7);
            } else if (tLObject instanceof TLRPC$Photo) {
                return getPathToAttach(getClosestPhotoSizeWithSize(((TLRPC$Photo) tLObject).sizes, AndroidUtilities.getPhotoSize()), str2, false, z2);
            } else {
                if (tLObject instanceof TLRPC$PhotoSize) {
                    TLRPC$PhotoSize tLRPC$PhotoSize = (TLRPC$PhotoSize) tLObject;
                    if (!(tLRPC$PhotoSize instanceof TLRPC$TL_photoStrippedSize) && !(tLRPC$PhotoSize instanceof TLRPC$TL_photoPathSize)) {
                        TLRPC$FileLocation tLRPC$FileLocation = tLRPC$PhotoSize.location;
                        if (tLRPC$FileLocation == null || tLRPC$FileLocation.key != null || ((tLRPC$FileLocation.volume_id == -2147483648L && tLRPC$FileLocation.local_id < 0) || tLRPC$PhotoSize.size < 0)) {
                            file = getDirectory(4);
                            TLRPC$FileLocation tLRPC$FileLocation2 = tLRPC$PhotoSize.location;
                            j2 = tLRPC$FileLocation2.volume_id;
                            i4 = tLRPC$FileLocation2.dc_id;
                            i5 = tLRPC$FileLocation2.local_id;
                        } else {
                            file = getDirectory(0);
                        }
                    }
                    i7 = 0;
                    TLRPC$FileLocation tLRPC$FileLocation22 = tLRPC$PhotoSize.location;
                    j2 = tLRPC$FileLocation22.volume_id;
                    i4 = tLRPC$FileLocation22.dc_id;
                    i5 = tLRPC$FileLocation22.local_id;
                } else if (tLObject instanceof TLRPC$TL_videoSize) {
                    TLRPC$TL_videoSize tLRPC$TL_videoSize = (TLRPC$TL_videoSize) tLObject;
                    TLRPC$FileLocation tLRPC$FileLocation3 = tLRPC$TL_videoSize.location;
                    if (tLRPC$FileLocation3 == null || tLRPC$FileLocation3.key != null || ((tLRPC$FileLocation3.volume_id == -2147483648L && tLRPC$FileLocation3.local_id < 0) || tLRPC$TL_videoSize.size < 0)) {
                        file = getDirectory(4);
                    } else {
                        file = getDirectory(0);
                        i7 = 0;
                    }
                    TLRPC$FileLocation tLRPC$FileLocation4 = tLRPC$TL_videoSize.location;
                    j2 = tLRPC$FileLocation4.volume_id;
                    i4 = tLRPC$FileLocation4.dc_id;
                    i5 = tLRPC$FileLocation4.local_id;
                } else if (tLObject instanceof TLRPC$FileLocation) {
                    TLRPC$FileLocation tLRPC$FileLocation5 = (TLRPC$FileLocation) tLObject;
                    if (tLRPC$FileLocation5.key == null) {
                        j = tLRPC$FileLocation5.volume_id;
                        if (j != -2147483648L || tLRPC$FileLocation5.local_id >= 0) {
                            i = tLRPC$FileLocation5.dc_id + (tLRPC$FileLocation5.local_id << 16);
                            file = getDirectory(0);
                            j2 = j;
                            i2 = 0;
                            i3 = i;
                            if (file != null) {
                                return new File("");
                            }
                            if (j2 != 0 && (path = getInstance(UserConfig.selectedAccount).getFileDatabase().getPath(j2, i3, i2, z2)) != null) {
                                return new File(path);
                            }
                            return new File(file, getAttachFileName(tLObject, str2));
                        }
                    }
                    file = getDirectory(4);
                    j = 0;
                    i = 0;
                    j2 = j;
                    i2 = 0;
                    i3 = i;
                    if (file != null) {
                    }
                } else if ((tLObject instanceof TLRPC$UserProfilePhoto) || (tLObject instanceof TLRPC$ChatPhoto)) {
                    if (str == null) {
                        str = "s";
                    }
                    if ("s".equals(str)) {
                        file = getDirectory(4);
                    } else {
                        file = getDirectory(0);
                    }
                } else if (tLObject instanceof WebFile) {
                    WebFile webFile = (WebFile) tLObject;
                    if (webFile.mime_type.startsWith("image/")) {
                        directory = getDirectory(0);
                    } else if (webFile.mime_type.startsWith("audio/")) {
                        directory = getDirectory(1);
                    } else if (webFile.mime_type.startsWith("video/")) {
                        directory = getDirectory(2);
                    } else {
                        directory = getDirectory(3);
                    }
                    file = directory;
                } else if ((tLObject instanceof TLRPC$TL_secureFile) || (tLObject instanceof SecureDocument)) {
                    file = getDirectory(4);
                }
                i6 = (i5 << 16) + i4;
            }
            i3 = i6;
            i2 = i7;
            if (file != null) {
            }
        }
        j2 = 0;
        i3 = 0;
        i2 = 0;
        if (file != null) {
        }
    }

    public FilePathDatabase getFileDatabase() {
        return this.filePathDatabase;
    }

    public static TLRPC$PhotoSize getClosestPhotoSizeWithSize(ArrayList<TLRPC$PhotoSize> arrayList, int i) {
        return getClosestPhotoSizeWithSize(arrayList, i, false);
    }

    public static TLRPC$PhotoSize getClosestPhotoSizeWithSize(ArrayList<TLRPC$PhotoSize> arrayList, int i, boolean z) {
        return getClosestPhotoSizeWithSize(arrayList, i, z, null, false);
    }

    /* JADX WARN: Code restructure failed: missing block: B:29:0x0044, code lost:
        if (r5.dc_id != Integer.MIN_VALUE) goto L29;
     */
    /* JADX WARN: Code restructure failed: missing block: B:41:0x0061, code lost:
        if (r5.dc_id != Integer.MIN_VALUE) goto L44;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public static TLRPC$PhotoSize getClosestPhotoSizeWithSize(ArrayList<TLRPC$PhotoSize> arrayList, int i, boolean z, TLRPC$PhotoSize tLRPC$PhotoSize, boolean z2) {
        int max;
        TLRPC$PhotoSize tLRPC$PhotoSize2 = null;
        if (arrayList != null && !arrayList.isEmpty()) {
            int i2 = 0;
            for (int i3 = 0; i3 < arrayList.size(); i3++) {
                TLRPC$PhotoSize tLRPC$PhotoSize3 = arrayList.get(i3);
                if (tLRPC$PhotoSize3 != null && tLRPC$PhotoSize3 != tLRPC$PhotoSize && !(tLRPC$PhotoSize3 instanceof TLRPC$TL_photoSizeEmpty) && !(tLRPC$PhotoSize3 instanceof TLRPC$TL_photoPathSize) && (!z2 || !(tLRPC$PhotoSize3 instanceof TLRPC$TL_photoStrippedSize))) {
                    if (z) {
                        max = Math.min(tLRPC$PhotoSize3.h, tLRPC$PhotoSize3.w);
                        if (tLRPC$PhotoSize2 != null) {
                            if (i > 100) {
                                TLRPC$FileLocation tLRPC$FileLocation = tLRPC$PhotoSize2.location;
                                if (tLRPC$FileLocation != null) {
                                }
                            }
                            if (!(tLRPC$PhotoSize3 instanceof TLRPC$TL_photoCachedSize)) {
                                if (i > i2) {
                                    if (i2 >= max) {
                                    }
                                }
                            }
                        }
                        tLRPC$PhotoSize2 = tLRPC$PhotoSize3;
                        i2 = max;
                    } else {
                        max = Math.max(tLRPC$PhotoSize3.w, tLRPC$PhotoSize3.h);
                        if (tLRPC$PhotoSize2 != null) {
                            if (i > 100) {
                                TLRPC$FileLocation tLRPC$FileLocation2 = tLRPC$PhotoSize2.location;
                                if (tLRPC$FileLocation2 != null) {
                                }
                            }
                            if (!(tLRPC$PhotoSize3 instanceof TLRPC$TL_photoCachedSize)) {
                                if (max <= i) {
                                    if (i2 >= max) {
                                    }
                                }
                            }
                        }
                        tLRPC$PhotoSize2 = tLRPC$PhotoSize3;
                        i2 = max;
                    }
                }
            }
        }
        return tLRPC$PhotoSize2;
    }

    public static TLRPC$VideoSize getClosestVideoSizeWithSize(ArrayList<TLRPC$VideoSize> arrayList, int i) {
        return getClosestVideoSizeWithSize(arrayList, i, false);
    }

    public static TLRPC$VideoSize getClosestVideoSizeWithSize(ArrayList<TLRPC$VideoSize> arrayList, int i, boolean z) {
        return getClosestVideoSizeWithSize(arrayList, i, z, false);
    }

    /* JADX WARN: Code restructure failed: missing block: B:25:0x003b, code lost:
        if (r4.dc_id != Integer.MIN_VALUE) goto L23;
     */
    /* JADX WARN: Code restructure failed: missing block: B:35:0x0054, code lost:
        if (r4.dc_id != Integer.MIN_VALUE) goto L37;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public static TLRPC$VideoSize getClosestVideoSizeWithSize(ArrayList<TLRPC$VideoSize> arrayList, int i, boolean z, boolean z2) {
        int max;
        TLRPC$VideoSize tLRPC$VideoSize = null;
        if (arrayList != null && !arrayList.isEmpty()) {
            int i2 = 0;
            for (int i3 = 0; i3 < arrayList.size(); i3++) {
                TLRPC$VideoSize tLRPC$VideoSize2 = arrayList.get(i3);
                if (tLRPC$VideoSize2 != null && !(tLRPC$VideoSize2 instanceof TLRPC$TL_videoSizeEmojiMarkup) && !(tLRPC$VideoSize2 instanceof TLRPC$TL_videoSizeStickerMarkup)) {
                    if (z) {
                        max = Math.min(tLRPC$VideoSize2.h, tLRPC$VideoSize2.w);
                        if (tLRPC$VideoSize != null) {
                            if (i > 100) {
                                TLRPC$FileLocation tLRPC$FileLocation = tLRPC$VideoSize.location;
                                if (tLRPC$FileLocation != null) {
                                }
                            }
                            if (i > i2) {
                                if (i2 >= max) {
                                }
                            }
                        }
                        tLRPC$VideoSize = tLRPC$VideoSize2;
                        i2 = max;
                    } else {
                        max = Math.max(tLRPC$VideoSize2.w, tLRPC$VideoSize2.h);
                        if (tLRPC$VideoSize != null) {
                            if (i > 100) {
                                TLRPC$FileLocation tLRPC$FileLocation2 = tLRPC$VideoSize.location;
                                if (tLRPC$FileLocation2 != null) {
                                }
                            }
                            if (max <= i) {
                                if (i2 >= max) {
                                }
                            }
                        }
                        tLRPC$VideoSize = tLRPC$VideoSize2;
                        i2 = max;
                    }
                }
            }
        }
        return tLRPC$VideoSize;
    }

    public static TLRPC$TL_photoPathSize getPathPhotoSize(ArrayList<TLRPC$PhotoSize> arrayList) {
        if (arrayList != null && !arrayList.isEmpty()) {
            for (int i = 0; i < arrayList.size(); i++) {
                TLRPC$PhotoSize tLRPC$PhotoSize = arrayList.get(i);
                if (!(tLRPC$PhotoSize instanceof TLRPC$TL_photoPathSize)) {
                    return (TLRPC$TL_photoPathSize) tLRPC$PhotoSize;
                }
            }
        }
        return null;
    }

    public static String getFileExtension(File file) {
        String name = file.getName();
        try {
            return name.substring(name.lastIndexOf(46) + 1);
        } catch (Exception unused) {
            return "";
        }
    }

    public static String fixFileName(String str) {
        return str != null ? str.replaceAll("[\u0001-\u001f<>\u202e:\"/\\\\|?*\u007f]+", "").trim() : str;
    }

    public static String getDocumentFileName(TLRPC$Document tLRPC$Document) {
        String str = null;
        if (tLRPC$Document == null) {
            return null;
        }
        String str2 = tLRPC$Document.file_name_fixed;
        if (str2 != null) {
            return str2;
        }
        String str3 = tLRPC$Document.file_name;
        if (str3 == null) {
            for (int i = 0; i < tLRPC$Document.attributes.size(); i++) {
                TLRPC$DocumentAttribute tLRPC$DocumentAttribute = tLRPC$Document.attributes.get(i);
                if (tLRPC$DocumentAttribute instanceof TLRPC$TL_documentAttributeFilename) {
                    str = tLRPC$DocumentAttribute.file_name;
                }
            }
            str3 = str;
        }
        String fixFileName = fixFileName(str3);
        return fixFileName != null ? fixFileName : "";
    }

    public static String getMimeTypePart(String str) {
        int lastIndexOf = str.lastIndexOf(47);
        return lastIndexOf != -1 ? str.substring(lastIndexOf + 1) : "";
    }

    public static String getExtensionByMimeType(String str) {
        if (str != null) {
            char c = 65535;
            switch (str.hashCode()) {
                case 187091926:
                    if (str.equals("audio/ogg")) {
                        c = 0;
                        break;
                    }
                    break;
                case 1331848029:
                    if (str.equals("video/mp4")) {
                        c = 1;
                        break;
                    }
                    break;
                case 2039520277:
                    if (str.equals("video/x-matroska")) {
                        c = 2;
                        break;
                    }
                    break;
            }
            switch (c) {
                case 0:
                    return ".ogg";
                case 1:
                    return ".mp4";
                case 2:
                    return ".mkv";
                default:
                    return "";
            }
        }
        return "";
    }

    public static File getInternalCacheDir() {
        return ApplicationLoader.applicationContext.getCacheDir();
    }

    public static String getDocumentExtension(TLRPC$Document tLRPC$Document) {
        String documentFileName = getDocumentFileName(tLRPC$Document);
        int lastIndexOf = documentFileName.lastIndexOf(46);
        String substring = lastIndexOf != -1 ? documentFileName.substring(lastIndexOf + 1) : null;
        if (substring == null || substring.length() == 0) {
            substring = tLRPC$Document.mime_type;
        }
        if (substring == null) {
            substring = "";
        }
        return substring.toUpperCase();
    }

    public static String getAttachFileName(TLObject tLObject) {
        return getAttachFileName(tLObject, null);
    }

    public static String getAttachFileName(TLObject tLObject, String str) {
        return getAttachFileName(tLObject, null, str);
    }

    public static String getAttachFileName(TLObject tLObject, String str, String str2) {
        if (tLObject instanceof TLRPC$Document) {
            TLRPC$Document tLRPC$Document = (TLRPC$Document) tLObject;
            String documentFileName = getDocumentFileName(tLRPC$Document);
            int lastIndexOf = documentFileName.lastIndexOf(46);
            String substring = lastIndexOf != -1 ? documentFileName.substring(lastIndexOf) : "";
            if (substring.length() <= 1) {
                substring = getExtensionByMimeType(tLRPC$Document.mime_type);
            }
            if (substring.length() > 1) {
                return tLRPC$Document.dc_id + "_" + tLRPC$Document.id + substring;
            }
            return tLRPC$Document.dc_id + "_" + tLRPC$Document.id;
        } else if (tLObject instanceof SecureDocument) {
            SecureDocument secureDocument = (SecureDocument) tLObject;
            return secureDocument.secureFile.dc_id + "_" + secureDocument.secureFile.id + ".jpg";
        } else if (tLObject instanceof TLRPC$TL_secureFile) {
            TLRPC$TL_secureFile tLRPC$TL_secureFile = (TLRPC$TL_secureFile) tLObject;
            return tLRPC$TL_secureFile.dc_id + "_" + tLRPC$TL_secureFile.id + ".jpg";
        } else if (tLObject instanceof WebFile) {
            WebFile webFile = (WebFile) tLObject;
            return Utilities.MD5(webFile.url) + "." + ImageLoader.getHttpUrlExtension(webFile.url, getMimeTypePart(webFile.mime_type));
        } else if (tLObject instanceof TLRPC$PhotoSize) {
            TLRPC$PhotoSize tLRPC$PhotoSize = (TLRPC$PhotoSize) tLObject;
            TLRPC$FileLocation tLRPC$FileLocation = tLRPC$PhotoSize.location;
            if (tLRPC$FileLocation == null || (tLRPC$FileLocation instanceof TLRPC$TL_fileLocationUnavailable)) {
                return "";
            }
            StringBuilder sb = new StringBuilder();
            sb.append(tLRPC$PhotoSize.location.volume_id);
            sb.append("_");
            sb.append(tLRPC$PhotoSize.location.local_id);
            sb.append(".");
            if (str2 == null) {
                str2 = "jpg";
            }
            sb.append(str2);
            return sb.toString();
        } else if (tLObject instanceof TLRPC$TL_videoSize) {
            TLRPC$TL_videoSize tLRPC$TL_videoSize = (TLRPC$TL_videoSize) tLObject;
            TLRPC$FileLocation tLRPC$FileLocation2 = tLRPC$TL_videoSize.location;
            if (tLRPC$FileLocation2 == null || (tLRPC$FileLocation2 instanceof TLRPC$TL_fileLocationUnavailable)) {
                return "";
            }
            StringBuilder sb2 = new StringBuilder();
            sb2.append(tLRPC$TL_videoSize.location.volume_id);
            sb2.append("_");
            sb2.append(tLRPC$TL_videoSize.location.local_id);
            sb2.append(".");
            if (str2 == null) {
                str2 = "mp4";
            }
            sb2.append(str2);
            return sb2.toString();
        } else if (tLObject instanceof TLRPC$FileLocation) {
            if (tLObject instanceof TLRPC$TL_fileLocationUnavailable) {
                return "";
            }
            TLRPC$FileLocation tLRPC$FileLocation3 = (TLRPC$FileLocation) tLObject;
            StringBuilder sb3 = new StringBuilder();
            sb3.append(tLRPC$FileLocation3.volume_id);
            sb3.append("_");
            sb3.append(tLRPC$FileLocation3.local_id);
            sb3.append(".");
            if (str2 == null) {
                str2 = "jpg";
            }
            sb3.append(str2);
            return sb3.toString();
        } else if (tLObject instanceof TLRPC$UserProfilePhoto) {
            if (str == null) {
                str = "s";
            }
            TLRPC$UserProfilePhoto tLRPC$UserProfilePhoto = (TLRPC$UserProfilePhoto) tLObject;
            if (tLRPC$UserProfilePhoto.photo_small != null) {
                if ("s".equals(str)) {
                    return getAttachFileName(tLRPC$UserProfilePhoto.photo_small, str2);
                }
                return getAttachFileName(tLRPC$UserProfilePhoto.photo_big, str2);
            }
            StringBuilder sb4 = new StringBuilder();
            sb4.append(tLRPC$UserProfilePhoto.photo_id);
            sb4.append("_");
            sb4.append(str);
            sb4.append(".");
            if (str2 == null) {
                str2 = "jpg";
            }
            sb4.append(str2);
            return sb4.toString();
        } else if (tLObject instanceof TLRPC$ChatPhoto) {
            TLRPC$ChatPhoto tLRPC$ChatPhoto = (TLRPC$ChatPhoto) tLObject;
            if (tLRPC$ChatPhoto.photo_small != null) {
                if ("s".equals(str)) {
                    return getAttachFileName(tLRPC$ChatPhoto.photo_small, str2);
                }
                return getAttachFileName(tLRPC$ChatPhoto.photo_big, str2);
            }
            StringBuilder sb5 = new StringBuilder();
            sb5.append(tLRPC$ChatPhoto.photo_id);
            sb5.append("_");
            sb5.append(str);
            sb5.append(".");
            if (str2 == null) {
                str2 = "jpg";
            }
            sb5.append(str2);
            return sb5.toString();
        } else {
            return "";
        }
    }

    public void deleteFiles(final ArrayList<File> arrayList, final int i) {
        if (arrayList == null || arrayList.isEmpty()) {
            return;
        }
        fileLoaderQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.FileLoader$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                FileLoader.lambda$deleteFiles$14(arrayList, i);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$deleteFiles$14(ArrayList arrayList, int i) {
        for (int i2 = 0; i2 < arrayList.size(); i2++) {
            File file = (File) arrayList.get(i2);
            File file2 = new File(file.getAbsolutePath() + ".enc");
            if (file2.exists()) {
                try {
                    if (!file2.delete()) {
                        file2.deleteOnExit();
                    }
                } catch (Exception e) {
                    FileLog.e(e);
                }
                try {
                    File internalCacheDir = getInternalCacheDir();
                    File file3 = new File(internalCacheDir, file.getName() + ".enc.key");
                    if (!file3.delete()) {
                        file3.deleteOnExit();
                    }
                } catch (Exception e2) {
                    FileLog.e(e2);
                }
            } else if (file.exists()) {
                try {
                    if (!file.delete()) {
                        file.deleteOnExit();
                    }
                } catch (Exception e3) {
                    FileLog.e(e3);
                }
            }
            try {
                File parentFile = file.getParentFile();
                File file4 = new File(parentFile, "q_" + file.getName());
                if (file4.exists() && !file4.delete()) {
                    file4.deleteOnExit();
                }
            } catch (Exception e4) {
                FileLog.e(e4);
            }
        }
        if (i == 2) {
            ImageLoader.getInstance().clearMemory();
        }
    }

    public static boolean isVideoMimeType(String str) {
        return "video/mp4".equals(str) || (SharedConfig.streamMkv && "video/x-matroska".equals(str));
    }

    public static boolean copyFile(InputStream inputStream, File file) throws IOException {
        return copyFile(inputStream, file, -1);
    }

    public static boolean copyFile(InputStream inputStream, File file, int i) throws IOException {
        FileOutputStream fileOutputStream = new FileOutputStream(file);
        byte[] bArr = new byte[LiteMode.FLAG_ANIMATED_EMOJI_CHAT_NOT_PREMIUM];
        int i2 = 0;
        while (true) {
            int read = inputStream.read(bArr);
            if (read <= 0) {
                break;
            }
            Thread.yield();
            fileOutputStream.write(bArr, 0, read);
            i2 += read;
            if (i > 0 && i2 >= i) {
                break;
            }
        }
        fileOutputStream.getFD().sync();
        fileOutputStream.close();
        return true;
    }

    public static boolean isSamePhoto(TLObject tLObject, TLObject tLObject2) {
        if ((tLObject != null || tLObject2 == null) && (tLObject == null || tLObject2 != null)) {
            if (tLObject == null && tLObject2 == null) {
                return true;
            }
            if (tLObject.getClass() != tLObject2.getClass()) {
                return false;
            }
            return tLObject instanceof TLRPC$UserProfilePhoto ? ((TLRPC$UserProfilePhoto) tLObject).photo_id == ((TLRPC$UserProfilePhoto) tLObject2).photo_id : (tLObject instanceof TLRPC$ChatPhoto) && ((TLRPC$ChatPhoto) tLObject).photo_id == ((TLRPC$ChatPhoto) tLObject2).photo_id;
        }
        return false;
    }

    public static boolean isSamePhoto(TLRPC$FileLocation tLRPC$FileLocation, TLRPC$Photo tLRPC$Photo) {
        if (tLRPC$FileLocation != null && (tLRPC$Photo instanceof TLRPC$TL_photo)) {
            int size = tLRPC$Photo.sizes.size();
            for (int i = 0; i < size; i++) {
                TLRPC$FileLocation tLRPC$FileLocation2 = tLRPC$Photo.sizes.get(i).location;
                if (tLRPC$FileLocation2 != null && tLRPC$FileLocation2.local_id == tLRPC$FileLocation.local_id && tLRPC$FileLocation2.volume_id == tLRPC$FileLocation.volume_id) {
                    return true;
                }
            }
            if ((-tLRPC$FileLocation.volume_id) == tLRPC$Photo.id) {
                return true;
            }
        }
        return false;
    }

    public static long getPhotoId(TLObject tLObject) {
        if (tLObject instanceof TLRPC$Photo) {
            return ((TLRPC$Photo) tLObject).id;
        }
        if (tLObject instanceof TLRPC$ChatPhoto) {
            return ((TLRPC$ChatPhoto) tLObject).photo_id;
        }
        if (tLObject instanceof TLRPC$UserProfilePhoto) {
            return ((TLRPC$UserProfilePhoto) tLObject).photo_id;
        }
        return 0L;
    }

    public void getCurrentLoadingFiles(ArrayList<MessageObject> arrayList) {
        arrayList.clear();
        arrayList.addAll(getDownloadController().downloadingFiles);
        for (int i = 0; i < arrayList.size(); i++) {
            arrayList.get(i).isDownloadingFile = true;
        }
    }

    public void getRecentLoadingFiles(ArrayList<MessageObject> arrayList) {
        arrayList.clear();
        arrayList.addAll(getDownloadController().recentDownloadingFiles);
        for (int i = 0; i < arrayList.size(); i++) {
            arrayList.get(i).isDownloadingFile = true;
        }
    }

    public void checkCurrentDownloadsFiles() {
        final ArrayList arrayList = new ArrayList();
        ArrayList arrayList2 = new ArrayList(getDownloadController().recentDownloadingFiles);
        for (int i = 0; i < arrayList2.size(); i++) {
            ((MessageObject) arrayList2.get(i)).checkMediaExistance();
            if (((MessageObject) arrayList2.get(i)).mediaExists) {
                arrayList.add((MessageObject) arrayList2.get(i));
            }
        }
        if (arrayList.isEmpty()) {
            return;
        }
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.FileLoader$$ExternalSyntheticLambda6
            @Override // java.lang.Runnable
            public final void run() {
                FileLoader.this.lambda$checkCurrentDownloadsFiles$15(arrayList);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$checkCurrentDownloadsFiles$15(ArrayList arrayList) {
        getDownloadController().recentDownloadingFiles.removeAll(arrayList);
        getNotificationCenter().lambda$postNotificationNameOnUIThread$1(NotificationCenter.onDownloadingFilesChanged, new Object[0]);
    }

    public void checkMediaExistance(ArrayList<MessageObject> arrayList) {
        getFileDatabase().checkMediaExistance(arrayList);
    }

    public void clearRecentDownloadedFiles() {
        getDownloadController().clearRecentDownloadedFiles();
    }

    public void clearFilePaths() {
        this.filePathDatabase.clear();
    }

    public static boolean checkUploadFileSize(int i, long j) {
        boolean isPremium = AccountInstance.getInstance(i).getUserConfig().isPremium();
        if (j >= DEFAULT_MAX_FILE_SIZE) {
            return j < 4194304000L && isPremium;
        }
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static class LoadOperationUIObject {
        Runnable loadInternalRunnable;

        private LoadOperationUIObject() {
        }

        /* synthetic */ LoadOperationUIObject(1 r1) {
            this();
        }
    }

    public static byte[] longToBytes(long j) {
        ByteBuffer allocate = ByteBuffer.allocate(8);
        allocate.putLong(j);
        return allocate.array();
    }

    public static long bytesToLong(byte[] bArr) {
        long j = 0;
        for (int i = 0; i < 8; i++) {
            j = (j << 8) ^ (bArr[i] & 255);
        }
        return j;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$16() {
        int i = 0;
        while (true) {
            FileLoaderPriorityQueue[] fileLoaderPriorityQueueArr = this.smallFilesQueue;
            if (i < fileLoaderPriorityQueueArr.length) {
                if (fileLoaderPriorityQueueArr[i].getCount() > 0 || this.largeFilesQueue[i].getCount() > 0) {
                    FileLog.d("download queue: dc" + (i + 1) + " account=" + this.currentAccount + " small_operations=" + this.smallFilesQueue[i].getCount() + " large_operations=" + this.largeFilesQueue[i].getCount());
                }
                i++;
            } else {
                dumpFilesQueue();
                return;
            }
        }
    }

    public void dumpFilesQueue() {
        if (BuildVars.LOGS_ENABLED) {
            fileLoaderQueue.cancelRunnable(this.dumpFilesQueueRunnable);
            fileLoaderQueue.postRunnable(this.dumpFilesQueueRunnable, 10000L);
        }
    }
}
