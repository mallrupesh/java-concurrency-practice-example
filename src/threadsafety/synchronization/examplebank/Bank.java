package threadsafety.synchronization.examplebank;

import java.util.Arrays;

/**
 * A bank with a number of bank accounts
 */
public class Bank {

    private final double[] accounts;

    /**
     * Construct the bank
     * @param n the number of accounts
     * @param initialBalance the initial balance for each account
     */
    public Bank(int n, double initialBalance) {
        accounts = new double[n];               // create n number of accounts
        Arrays.fill(accounts, initialBalance);  // fill each account with initial balance
    }

    /**
     * Transfer money from one account ro another
     * @param from the account to transfer from
     * @param to the account to transfer to
     * @param amount the amount to transfer
     */
    public void transfer(int from, int to, double amount) {
        if(accounts[from] < amount) return;
        System.out.print(Thread.currentThread().getName());
        accounts[from] -= amount;
        System.out.printf(" %10.2f from %d to %d", amount, from, to);
        accounts[to] += amount;
        System.out.printf(" Total balance: %10.2f%n ", getTotalBalance());
    }

    /**
     * Get the sum of all account balances
     * @return the total balance
     */
    private double getTotalBalance() {
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
