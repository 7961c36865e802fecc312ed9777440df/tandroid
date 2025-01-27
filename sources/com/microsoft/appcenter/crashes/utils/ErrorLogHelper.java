package com.microsoft.appcenter.crashes.utils;

import android.app.ActivityManager;
import android.content.Context;
import android.os.Build;
import android.os.Process;
import com.microsoft.appcenter.Constants;
import com.microsoft.appcenter.crashes.ingestion.models.Exception;
import com.microsoft.appcenter.crashes.ingestion.models.ManagedErrorLog;
import com.microsoft.appcenter.crashes.ingestion.models.StackFrame;
import com.microsoft.appcenter.crashes.ingestion.models.Thread;
import com.microsoft.appcenter.crashes.model.ErrorReport;
import com.microsoft.appcenter.ingestion.models.Device;
import com.microsoft.appcenter.utils.AppCenterLog;
import com.microsoft.appcenter.utils.DeviceInfoHelper;
import com.microsoft.appcenter.utils.context.UserIdContext;
import com.microsoft.appcenter.utils.storage.FileManager;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;

/* loaded from: classes.dex */
public abstract class ErrorLogHelper {
    private static File sErrorLogDirectory;
    private static File sNewMinidumpDirectory;
    private static File sPendingMinidumpDirectory;

    public static void cleanPendingMinidumps() {
        FileManager.cleanDirectory(getPendingMinidumpDirectory());
    }

    public static ManagedErrorLog createErrorLog(Context context, Thread thread, Exception exception, Map map, long j, boolean z) {
        List<ActivityManager.RunningAppProcessInfo> runningAppProcesses;
        ManagedErrorLog managedErrorLog = new ManagedErrorLog();
        managedErrorLog.setId(UUID.randomUUID());
        managedErrorLog.setTimestamp(new Date());
        managedErrorLog.setUserId(UserIdContext.getInstance().getUserId());
        try {
            managedErrorLog.setDevice(DeviceInfoHelper.getDeviceInfo(context));
        } catch (DeviceInfoHelper.DeviceInfoException e) {
            AppCenterLog.error("AppCenterCrashes", "Could not attach device properties snapshot to error log, will attach at sending time", e);
        }
        managedErrorLog.setProcessId(Integer.valueOf(Process.myPid()));
        ActivityManager activityManager = (ActivityManager) context.getSystemService("activity");
        if (activityManager != null && (runningAppProcesses = activityManager.getRunningAppProcesses()) != null) {
            for (ActivityManager.RunningAppProcessInfo runningAppProcessInfo : runningAppProcesses) {
                if (runningAppProcessInfo.pid == Process.myPid()) {
                    managedErrorLog.setProcessName(runningAppProcessInfo.processName);
                }
            }
        }
        if (managedErrorLog.getProcessName() == null) {
            managedErrorLog.setProcessName("");
        }
        managedErrorLog.setArchitecture(getArchitecture());
        managedErrorLog.setErrorThreadId(Long.valueOf(thread.getId()));
        managedErrorLog.setErrorThreadName(thread.getName());
        managedErrorLog.setFatal(Boolean.valueOf(z));
        managedErrorLog.setAppLaunchTimestamp(new Date(j));
        managedErrorLog.setException(exception);
        ArrayList arrayList = new ArrayList(map.size());
        for (Map.Entry entry : map.entrySet()) {
            Thread thread2 = new Thread();
            thread2.setId(((Thread) entry.getKey()).getId());
            thread2.setName(((Thread) entry.getKey()).getName());
            thread2.setFrames(getModelFramesFromStackTrace((StackTraceElement[]) entry.getValue()));
            arrayList.add(thread2);
        }
        managedErrorLog.setThreads(arrayList);
        return managedErrorLog;
    }

    private static String getArchitecture() {
        String[] strArr;
        if (Build.VERSION.SDK_INT < 21) {
            return Build.CPU_ABI;
        }
        strArr = Build.SUPPORTED_ABIS;
        return strArr[0];
    }

    public static ErrorReport getErrorReportFromErrorLog(ManagedErrorLog managedErrorLog, String str) {
        ErrorReport errorReport = new ErrorReport();
        errorReport.setId(managedErrorLog.getId().toString());
        errorReport.setThreadName(managedErrorLog.getErrorThreadName());
        errorReport.setStackTrace(str);
        errorReport.setAppStartTime(managedErrorLog.getAppLaunchTimestamp());
        errorReport.setAppErrorTime(managedErrorLog.getTimestamp());
        errorReport.setDevice(managedErrorLog.getDevice());
        return errorReport;
    }

    public static synchronized File getErrorStorageDirectory() {
        File file;
        synchronized (ErrorLogHelper.class) {
            try {
                if (sErrorLogDirectory == null) {
                    File file2 = new File(Constants.FILES_PATH, "error");
                    sErrorLogDirectory = file2;
                    FileManager.mkdir(file2.getAbsolutePath());
                }
                file = sErrorLogDirectory;
            } catch (Throwable th) {
                throw th;
            }
        }
        return file;
    }

    public static File getLastErrorLogFile() {
        return FileManager.lastModifiedFile(getErrorStorageDirectory(), new FilenameFilter() { // from class: com.microsoft.appcenter.crashes.utils.ErrorLogHelper.4
            @Override // java.io.FilenameFilter
            public boolean accept(File file, String str) {
                return str.endsWith(".json");
            }
        });
    }

    public static Exception getModelExceptionFromThrowable(Throwable th) {
        LinkedList<Throwable> linkedList = new LinkedList();
        while (th != null) {
            linkedList.add(th);
            th = th.getCause();
        }
        if (linkedList.size() > 16) {
            AppCenterLog.warn("AppCenterCrashes", "Crash causes truncated from " + linkedList.size() + " to 16 causes.");
            linkedList.subList(8, linkedList.size() - 8).clear();
        }
        Exception exception = null;
        Exception exception2 = null;
        for (Throwable th2 : linkedList) {
            Exception exception3 = new Exception();
            exception3.setType(th2.getClass().getName());
            exception3.setMessage(th2.getMessage());
            exception3.setFrames(getModelFramesFromStackTrace(th2));
            if (exception == null) {
                exception = exception3;
            } else {
                exception2.setInnerExceptions(Collections.singletonList(exception3));
            }
            exception2 = exception3;
        }
        return exception;
    }

    private static List getModelFramesFromStackTrace(Throwable th) {
        StackTraceElement[] stackTrace = th.getStackTrace();
        if (stackTrace.length > 256) {
            StackTraceElement[] stackTraceElementArr = new StackTraceElement[256];
            System.arraycopy(stackTrace, 0, stackTraceElementArr, 0, 128);
            System.arraycopy(stackTrace, stackTrace.length - 128, stackTraceElementArr, 128, 128);
            th.setStackTrace(stackTraceElementArr);
            AppCenterLog.warn("AppCenterCrashes", "Crash frames truncated from " + stackTrace.length + " to 256 frames.");
            stackTrace = stackTraceElementArr;
        }
        return getModelFramesFromStackTrace(stackTrace);
    }

    private static List getModelFramesFromStackTrace(StackTraceElement[] stackTraceElementArr) {
        ArrayList arrayList = new ArrayList();
        for (StackTraceElement stackTraceElement : stackTraceElementArr) {
            arrayList.add(getModelStackFrame(stackTraceElement));
        }
        return arrayList;
    }

    private static StackFrame getModelStackFrame(StackTraceElement stackTraceElement) {
        StackFrame stackFrame = new StackFrame();
        stackFrame.setClassName(stackTraceElement.getClassName());
        stackFrame.setMethodName(stackTraceElement.getMethodName());
        stackFrame.setLineNumber(Integer.valueOf(stackTraceElement.getLineNumber()));
        stackFrame.setFileName(stackTraceElement.getFileName());
        return stackFrame;
    }

    public static synchronized File getNewMinidumpDirectory() {
        File file;
        synchronized (ErrorLogHelper.class) {
            file = new File(new File(getErrorStorageDirectory().getAbsolutePath(), "minidump"), "new");
        }
        return file;
    }

    public static File[] getNewMinidumpFiles() {
        File[] listFiles = getNewMinidumpDirectory().listFiles();
        return listFiles != null ? listFiles : new File[0];
    }

    public static synchronized File getNewMinidumpSubfolder() {
        File file;
        synchronized (ErrorLogHelper.class) {
            try {
                if (sNewMinidumpDirectory == null) {
                    File file2 = new File(getNewMinidumpDirectory(), UUID.randomUUID().toString());
                    sNewMinidumpDirectory = file2;
                    FileManager.mkdir(file2.getPath());
                }
                file = sNewMinidumpDirectory;
            } catch (Throwable th) {
                throw th;
            }
        }
        return file;
    }

    public static synchronized File getNewMinidumpSubfolderWithContextData(Context context) {
        File newMinidumpSubfolder;
        synchronized (ErrorLogHelper.class) {
            newMinidumpSubfolder = getNewMinidumpSubfolder();
            File file = new File(newMinidumpSubfolder, "deviceInfo");
            try {
                Device deviceInfo = DeviceInfoHelper.getDeviceInfo(context);
                deviceInfo.setWrapperSdkName("appcenter.ndk");
                JSONStringer jSONStringer = new JSONStringer();
                jSONStringer.object();
                deviceInfo.write(jSONStringer);
                jSONStringer.endObject();
                FileManager.write(file, jSONStringer.toString());
            } catch (DeviceInfoHelper.DeviceInfoException | IOException | JSONException e) {
                AppCenterLog.error("AppCenterCrashes", "Failed to store device info in a minidump folder.", e);
                file.delete();
            }
        }
        return newMinidumpSubfolder;
    }

    public static synchronized File getPendingMinidumpDirectory() {
        File file;
        synchronized (ErrorLogHelper.class) {
            try {
                if (sPendingMinidumpDirectory == null) {
                    File file2 = new File(new File(getErrorStorageDirectory().getAbsolutePath(), "minidump"), "pending");
                    sPendingMinidumpDirectory = file2;
                    FileManager.mkdir(file2.getPath());
                }
                file = sPendingMinidumpDirectory;
            } catch (Throwable th) {
                throw th;
            }
        }
        return file;
    }

    public static Device getStoredDeviceInfo(File file) {
        File[] listFiles = file.listFiles(new FilenameFilter() { // from class: com.microsoft.appcenter.crashes.utils.ErrorLogHelper.2
            @Override // java.io.FilenameFilter
            public boolean accept(File file2, String str) {
                return str.equals("deviceInfo");
            }
        });
        if (listFiles == null || listFiles.length == 0) {
            AppCenterLog.warn("AppCenterCrashes", "No stored deviceinfo file found in a minidump folder.");
            return null;
        }
        String read = FileManager.read(listFiles[0]);
        if (read != null) {
            return parseDevice(read);
        }
        AppCenterLog.error("AppCenterCrashes", "Failed to read stored device info.");
        return null;
    }

    static File getStoredErrorLogFile(UUID uuid) {
        return getStoredFile(uuid, ".json");
    }

    public static File[] getStoredErrorLogFiles() {
        File[] listFiles = getErrorStorageDirectory().listFiles(new FilenameFilter() { // from class: com.microsoft.appcenter.crashes.utils.ErrorLogHelper.1
            @Override // java.io.FilenameFilter
            public boolean accept(File file, String str) {
                return str.endsWith(".json");
            }
        });
        return listFiles != null ? listFiles : new File[0];
    }

    private static File getStoredFile(final UUID uuid, final String str) {
        File[] listFiles = getErrorStorageDirectory().listFiles(new FilenameFilter() { // from class: com.microsoft.appcenter.crashes.utils.ErrorLogHelper.5
            @Override // java.io.FilenameFilter
            public boolean accept(File file, String str2) {
                return str2.startsWith(uuid.toString()) && str2.endsWith(str);
            }
        });
        if (listFiles == null || listFiles.length <= 0) {
            return null;
        }
        return listFiles[0];
    }

    public static File getStoredThrowableFile(UUID uuid) {
        return getStoredFile(uuid, ".throwable");
    }

    static Device parseDevice(String str) {
        try {
            Device device = new Device();
            device.read(new JSONObject(str));
            return device;
        } catch (JSONException e) {
            AppCenterLog.error("AppCenterCrashes", "Failed to deserialize device info.", e);
            return null;
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:5:0x001a  */
    /* JADX WARN: Removed duplicated region for block: B:8:? A[RETURN, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public static UUID parseLogFolderUuid(File file) {
        UUID fromString;
        if (file.isDirectory()) {
            try {
                fromString = UUID.fromString(file.getName());
            } catch (IllegalArgumentException e) {
                AppCenterLog.warn("AppCenterCrashes", "Cannot parse minidump folder name to UUID.", e);
            }
            return fromString != null ? UUID.randomUUID() : fromString;
        }
        fromString = null;
        if (fromString != null) {
        }
    }

    public static void removeMinidumpFolder() {
        FileManager.deleteDirectory(new File(getErrorStorageDirectory().getAbsolutePath(), "minidump"));
    }

    public static void removeStaleMinidumpSubfolders() {
        File[] listFiles = getNewMinidumpDirectory().listFiles(new FilenameFilter() { // from class: com.microsoft.appcenter.crashes.utils.ErrorLogHelper.3
            @Override // java.io.FilenameFilter
            public boolean accept(File file, String str) {
                if (ErrorLogHelper.sNewMinidumpDirectory != null) {
                    return !str.equals(ErrorLogHelper.sNewMinidumpDirectory.getName());
                }
                return true;
            }
        });
        if (listFiles == null || listFiles.length == 0) {
            AppCenterLog.debug("AppCenterCrashes", "No previous minidump sub-folders.");
            return;
        }
        for (File file : listFiles) {
            FileManager.deleteDirectory(file);
        }
    }

    public static void removeStoredErrorLogFile(UUID uuid) {
        File storedErrorLogFile = getStoredErrorLogFile(uuid);
        if (storedErrorLogFile != null) {
            AppCenterLog.info("AppCenterCrashes", "Deleting error log file " + storedErrorLogFile.getName());
            FileManager.delete(storedErrorLogFile);
        }
    }

    public static void removeStoredThrowableFile(UUID uuid) {
        File storedThrowableFile = getStoredThrowableFile(uuid);
        if (storedThrowableFile != null) {
            AppCenterLog.info("AppCenterCrashes", "Deleting throwable file " + storedThrowableFile.getName());
            FileManager.delete(storedThrowableFile);
        }
    }

    public static Map validateProperties(Map map, String str) {
        String format;
        if (map == null) {
            return null;
        }
        HashMap hashMap = new HashMap();
        Iterator it = map.entrySet().iterator();
        while (true) {
            if (!it.hasNext()) {
                break;
            }
            Map.Entry entry = (Map.Entry) it.next();
            String str2 = (String) entry.getKey();
            String str3 = (String) entry.getValue();
            if (hashMap.size() >= 20) {
                AppCenterLog.warn("AppCenterCrashes", String.format("%s : properties cannot contain more than %s items. Skipping other properties.", str, 20));
                break;
            }
            if (str2 == null || str2.isEmpty()) {
                format = String.format("%s : a property key cannot be null or empty. Property will be skipped.", str);
            } else if (str3 == null) {
                format = String.format("%s : property '%s' : property value cannot be null. Property '%s' will be skipped.", str, str2, str2);
            } else {
                if (str2.length() > 125) {
                    AppCenterLog.warn("AppCenterCrashes", String.format("%s : property '%s' : property key length cannot be longer than %s characters. Property key will be truncated.", str, str2, 125));
                    str2 = str2.substring(0, 125);
                }
                if (str3.length() > 125) {
                    AppCenterLog.warn("AppCenterCrashes", String.format("%s : property '%s' : property value cannot be longer than %s characters. Property value will be truncated.", str, str2, 125));
                    str3 = str3.substring(0, 125);
                }
                hashMap.put(str2, str3);
            }
            AppCenterLog.warn("AppCenterCrashes", format);
        }
        return hashMap;
    }
}
