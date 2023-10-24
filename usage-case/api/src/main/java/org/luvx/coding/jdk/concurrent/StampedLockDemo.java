package org.luvx.coding.jdk.concurrent;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.StampedLock;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class StampedLockDemo {
    private final StampedLock lock = new StampedLock();
    private double balance;

    public void deposit(double amount) {
        long stamp = lock.writeLock();
        try {
            balance += amount;
        } finally {
            lock.unlockWrite(stamp);
        }
    }

    public double getBalance() {
        long stamp = lock.readLock();
        try {
            return balance;
        } finally {
            lock.unlockRead(stamp);
        }
    }

    public static void main(String[] args) throws InterruptedException {
        StampedLockDemo account = new StampedLockDemo();
        ExecutorService pool = Executors.newCachedThreadPool();

        List<Callable<Double>> callables = IntStream.rangeClosed(1, 5)
                .mapToObj(x -> (Callable<Double>) () -> {
                    account.deposit(x);
                    return account.getBalance();
                })
                .collect(Collectors.toList());

        pool.invokeAll(callables).forEach(x -> {
            try {
                System.out.println(x.get());
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        pool.shutdown();

        System.out.println(account.getBalance());
    }
}
