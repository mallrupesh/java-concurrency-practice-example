package intro.synchronisation;

import locktypes.simplelock.Lock;

import java.time.LocalTime;
import java.util.Arrays;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class BankSynchronizedUsingLock {

    private final double[] accounts;
    private ReentrantLock bankLock;
    private Condition sufficientFunds;      // lock condition

    public BankSynchronizedUsingLock(int n, double initialBalance) {
        this.accounts = new double[n];
        Arrays.fill(accounts, initialBalance);
        this.bankLock = new ReentrantLock();
        this.sufficientFunds = bankLock.newCondition();
    }

    public void transfer(int from, int to, double amount) {

        bankLock.lock();

        try{
            while(accounts[from] < amount){
                sufficientFunds.await();    // if not sufficient amount, current thread goes to waiting state
            }
            System.out.print(Thread.currentThread());
            accounts[from] -= amount;
            System.out.printf(" %10.2f from %d to %d", amount, from, to);
            accounts[to] += amount;
            System.out.printf(" Total balance: %10.2f%n ", getTotalBalance());
            sufficientFunds.signalAll();    // signal all threads waiting on the condition
        } catch (InterruptedException e) {
            System.out.println(e.getMessage());
        }
        finally {
            bankLock.unlock();
        }
    }

    /**
     * Get the sum of all account balances
     * @return the total balance
     */
    private synchronized double getTotalBalance() {

        double sum = 0;
        for(double a : accounts){
            sum += a;
        }
        return sum;
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
