package head.first.decorator.beverage;

/**
 * Created by huangpin on 17/7/18.
 */
public class Coffee extends Beverage {

    private Beverage beverage;

    public Coffee(Beverage beverage) {
        this.beverage = beverage;
    }

    public String getDescription() {
        return beverage.getDescription() + "-COFFEE";
    }

    public double cost() {
        return beverage.cost() + 5;
    }
}
