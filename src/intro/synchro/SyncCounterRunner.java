package intro.synchro;

public class SyncCounterRunner {

    public static void main(String[] args) {

    }
}

// This class contains two synchronized methods
// The synchronized methods are synchronized on SynCounter object (owns the methods)
// Only one thread per instance of this class can execute either of the two synchronized methods
class SyncCounter{

    private int count = 0;

    public synchronized void add(int val){
        this.count += val;
    }

    public synchronized void subtract(int val){
        this.count -= val;
    }
}
