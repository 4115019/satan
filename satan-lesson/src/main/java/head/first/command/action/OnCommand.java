package head.first.command.action;

import head.first.command.executor.CommandExecutor;

/**
 * Created by huangpin on 17/7/26.
 */
public class OnCommand<CE extends CommandExecutor> implements Command {

    CE commandExecutor;

    public OnCommand(CE commandExecutor) {
        this.commandExecutor = commandExecutor;
    }

    public void execute() {
        commandExecutor.executor();
    }
}
