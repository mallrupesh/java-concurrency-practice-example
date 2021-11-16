package threadlocal;

import java.text.SimpleDateFormat;

public class MyEvent {

    private String name;
    public static final ThreadLocal<SimpleDateFormat> dateFormat =
            ThreadLocal.withInitial(() -> new SimpleDateFormat("yyyy-MM-dd"));

    public MyEvent(String name) {
        this.name = name;
    }
}
