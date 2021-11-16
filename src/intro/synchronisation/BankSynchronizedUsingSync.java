package intro.synchronisation;

import java.util.Arrays;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class BankSynchronizedUsingSync {

    private final double[] accounts;
    private ReentrantLock bankLock;
    private Condition sufficientFunds;      // lock condition

    public BankSynchronizedUsingSync(int n, double initialBalance) {
        this.accounts = new double[n];
        Arrays.fill(accounts, initialBalance);
        this.bankLock = new ReentrantLock();
        this.sufficientFunds = bankLock.newCondition();
    }

    public synchronized void transfer(int from, int to, double amount) throws InterruptedException {

        while(accounts[from] < amount){
            wait();     // wait on intrinsic object lock's single ocndition
        }
        System.out.print(Thread.currentThread());
        accounts[from] -= amount;
        System.out.printf(" %10.2f from %d to %d", amount, from, to);
        accounts[to] += amount;
        System.out.printf(" Total balance: %10.2f%n ", getTotalBalance());
        notifyAll();    // notify all threads waiting on the condition
    }

    public void transferWithBlock(int from, int to, double amount) throws InterruptedException {

        while(accounts[from] < amount){
            wait();     // wait on intrinsic object lock's single ocndition
        }
        System.out.print(Thread.currentThread());
        accounts[from] -= amount;
        System.out.printf(" %10.2f from %d to %d", amount, from, to);
        accounts[to] += amount;
        System.out.printf(" Total balance: %10.2f%n ", getTotalBalance());
        notifyAll();    // notify all threads waiting on the condition
    }



    /**
     * Get the sum of all account balances
     * @return the total balance
     */
    private double getTotalBalance() {
        bankLock.lock();

        try {
            double sum = 0;
            for(double a : accounts){
                sum += a;
            }
            return sum;
        }finally {
            bankLock.unlock();
        }
    }

    /**
     * Get the number of accounts in the bank
     * @return
     */
    public int size() {
        return accounts.length;
    }

    public double[] getAccounts() {
        return accounts;
    }
}
