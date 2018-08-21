package head.first.decorator.condiment;

/**
 * Created by huangpin on 17/7/18.
 */
public class Sugar extends Condimentor {
    public String getDescription() {
        return "SUGAR";
    }

    public double cost() {
        return 0.5;
    }
}
