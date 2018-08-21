package head.first.decorator.beverage;

/**
 * Created by huangpin on 17/7/18.
 */
public class Moka extends Beverage {

    private Beverage beverage;

    public Moka(Beverage beverage) {
        this.beverage = beverage;
    }

    public String getDescription() {
        return beverage.getDescription() + "-MOKA";
    }

    public double cost() {
        return beverage.cost() + 5;
    }
}
