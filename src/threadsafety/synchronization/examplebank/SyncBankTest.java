package threadsafety.synchronization.examplebank;

public class SyncBankTest {

    public static final int N_ACCOUNTS = 10;
    public static final double INITIAL_BALANCE = 1000;
    public static  final double MAX_AMOUNT = 1000;
    public static final int DELAY = 10;

    public static void main(String[] args) {

        BankSynchronizedUsingLock bank = new BankSynchronizedUsingLock(N_ACCOUNTS, INITIAL_BALANCE);

        // Every transaction gets new thread
        for(int i = 0; i < N_ACCOUNTS; i++) {

            int fromAccount = i;            // determine fromAccount using loop counter

            Runnable runnable = () -> {     // new Runnable
                try{
                    while(true) {
                        int toAccount = (int) (bank.size() * Math.random());    // toAccount chosen randomly
                        double amount = MAX_AMOUNT * Math.random();             // random amount for transaction
                        bank.transfer(fromAccount, toAccount, amount);          // perform transfer
                        Thread.sleep((int)(DELAY * Math.random()));             // random thread sleep time
                    }
                } catch (InterruptedException e) {
                    System.out.println(e.getMessage());
                }
            };

            Thread thread = new Thread(runnable);
            thread.start();                         // start new thread
        }
    }
}
