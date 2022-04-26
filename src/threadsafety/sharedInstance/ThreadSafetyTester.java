package threadsafety.sharedInstance;

public class ThreadSafetyTester {

    public static void main(String[] args) {

        /*Thread t1 = new Thread(() -> {
            methodOne("Jim", 12);  // t1 has its own local object reference not shared to other threads
        });                                   // hence, calling method with local object reference is thread safe
        t1.start();


        Thread t2 = new Thread(() -> {
            methodOne("Gary", 30);  // t2 has its own local object reference not shared to other threads
        });                                    // hence, calling method with local object reference is thread safe
        t2.start();*/


        //---------------------Shared instance----------------------//
        /*NotThreadSafe sharedInstance = new NotThreadSafe();

        new Thread(new MyRunnable(sharedInstance)).start();     // sharedInstance, not thread safe

        new Thread(new MyRunnable(sharedInstance)).start();     // sharedInstance, not thread safe*/



        //------------------Different instance-----------------------//
        new Thread(new MyRunnable(new NotThreadSafe())).start();    // different instance, thread safe
        new Thread(new MyRunnable(new NotThreadSafe())).start();    // different instance, thread safe
    }



    // Method with local object reference
    public static void methodOne(String name, int age){
        Person p = new Person(name, age);        // local Object reference
        System.out.println(p.getName());
    }

    public static void methodTwo(Person p){
        p.setName("Unknown");
        p.setAge(0);
        System.out.println(p.getName());
    }
}


class NotThreadSafe{

    StringBuilder sb = new StringBuilder();

    public void add(String val){
        this.sb.append(val);
    }
}


class MyRunnable implements Runnable{

    private NotThreadSafe instance;             // member variable

    public MyRunnable(NotThreadSafe instance) {
        this.instance = instance;
    }

    @Override
    public void run() {
        instance.add("some text");
    }
}


class Person{

    private String name;
    private int age;

    public Person(String name, int age){
        this.name = name;
        this.age = age;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }
}
