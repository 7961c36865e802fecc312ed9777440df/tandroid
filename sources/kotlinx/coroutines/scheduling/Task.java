package kotlinx.coroutines.scheduling;

/* loaded from: classes.dex */
public abstract class Task implements Runnable {
    public long submissionTime;
    public TaskContext taskContext;

    public Task(long j, TaskContext taskContext) {
        this.submissionTime = j;
        this.taskContext = taskContext;
    }
}