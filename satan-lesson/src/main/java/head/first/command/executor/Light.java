package head.first.command.executor;

/**
 * Created by huangpin on 17/7/26.
 */
public class Light implements CommandExecutor {
    public void executor() {
        System.out.println("Light on !!!");
    }

    public void lightOn() {
        executor();
    }
}
