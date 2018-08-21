package head.first.singleton;

/**
 * Created by huangpin on 17/7/25.
 */
public class Singleton {

    private static Singleton singleton;

    private Singleton() {

    }

    public static synchronized Singleton getInstance() {
        if (singleton == null) {
            synchronized (Singleton.class) {
                if (singleton == null) {
                    singleton = new Singleton();
                }
            }
        }
        return singleton;
    }
}
