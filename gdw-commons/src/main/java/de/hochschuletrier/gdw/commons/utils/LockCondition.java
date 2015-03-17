package de.hochschuletrier.gdw.commons.utils;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class LockCondition {

    private final ReentrantLock lock = new ReentrantLock();
    private final Condition condition = lock.newCondition();

    public void signal() {
        lock.lock();
        try {
            condition.signal();
        } finally {
            lock.unlock();
        }
    }

    public void await(Check check) {
        lock.lock();
        try {
            while (!check.isDone()) {
                try {
                    condition.await();
                } catch (InterruptedException e) {
                }
            }
        } finally {
            lock.unlock();
        }
    }

    public void await(long ms) {
        lock.lock();
        try {
            try {
                condition.await(ms, TimeUnit.MILLISECONDS);
            } catch (InterruptedException e) {
            }
        } finally {
            lock.unlock();
        }
    }

    public void await(long ms, Check check) {
        lock.lock();
        try {
            while (!check.isDone()) {
                try {
                    condition.await(ms, TimeUnit.MILLISECONDS);
                } catch (InterruptedException e) {
                }
            }
        } finally {
            lock.unlock();
        }
    }

    public static interface Check {

        boolean isDone();
    }
}
