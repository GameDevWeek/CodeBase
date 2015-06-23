package de.hochschuletrier.gdw.commons.utils.chainprocessing;

public class ChainWorker<T, C> {

    private ChainJob<T, C> currentJob;
    private T object;
    
    public boolean isDone() {
        return currentJob == null;
    }

    public void reset() {
        currentJob = null;
        object = null;
    }

    public void init(ChainJob<T, C> firstJob, T object, C context) {
        currentJob = firstJob;
        this.object = object;
        currentJob.init(object, context);
    }

    public boolean runFrame(C context) throws Exception {
        if (currentJob != null) {
            ChainJob<T, C> lastJob = currentJob;
            currentJob = currentJob.process(object, context);
            if (currentJob != null) {
                if (currentJob != lastJob) {
                    currentJob.init(object, context);
                }
                return false;
            }
            object = null;
        }
        return true;
    }

    public T getObject() {
        return object;
    }
}
