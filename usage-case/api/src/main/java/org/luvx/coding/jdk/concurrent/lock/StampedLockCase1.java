package org.luvx.coding.jdk.concurrent.lock;

import org.luvx.coding.common.concurrent.Threads;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.StampedLock;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class StampedLockCase1 {
    private final StampedLock lock = new StampedLock();
    private       int         balance;

    public void deposit(int amount) {
        long stamp = lock.writeLock();
        try {
            balance += amount;
        } finally {
            lock.unlockWrite(stamp);
        }
    }

    public int getBalance() {
        long stamp = lock.readLock();
        try {
            return balance;
        } finally {
            lock.unlockRead(stamp);
        }
    }

    public static void main(String[] args) throws InterruptedException {
        StampedLockCase1 exec = new StampedLockCase1();
        ExecutorService pool = Executors.newCachedThreadPool();

        // 1+2+3+4+5
        List<Callable<Integer>> callables = IntStream.rangeClosed(1, 5)
                .mapToObj(x -> (Callable<Integer>) () -> {
                    exec.deposit(x);
                    return exec.getBalance();
                })
                .collect(Collectors.toList());

        pool.invokeAll(callables).forEach(x -> {
            try {
                Threads.info(x.get() + "");
            } catch (Exception _) {
            }
        });

        pool.shutdown();

        System.out.println("总:" + exec.getBalance());
    }
}
