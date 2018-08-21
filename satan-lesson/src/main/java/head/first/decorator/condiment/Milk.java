package head.first.decorator.condiment;

/**
 * Created by huangpin on 17/7/18.
 */
public class Milk extends Condimentor {

    public String getDescription() {
        return "MILK";
    }

    public double cost() {
        return 1.5;
    }
}
