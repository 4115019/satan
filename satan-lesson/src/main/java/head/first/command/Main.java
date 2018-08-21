package head.first.command;

import head.first.command.action.OnCommand;
import head.first.command.executor.Light;

/**
 * Created by huangpin on 17/7/26.
 */
public class Main {
    public static void main(String[] args) {
        OnCommand<Light> lightOnCommand = new OnCommand<Light>(new Light());
        lightOnCommand.execute();
        new Light().lightOn();
    }
}
