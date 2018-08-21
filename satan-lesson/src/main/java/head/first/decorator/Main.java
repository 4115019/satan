package head.first.decorator;

import head.first.decorator.beverage.Beverage;
import head.first.decorator.beverage.Coffee;
import head.first.decorator.beverage.Moka;
import head.first.decorator.condiment.Milk;
import head.first.decorator.condiment.Sugar;

/**
 * Created by huangpin on 17/7/18.
 */
public class Main {
    public static void main(String[] args) {
        Beverage beverage = new Moka(new Milk());
        beverage = new Coffee(beverage);
        System.out.println(beverage.getDescription());
        System.out.println(beverage.cost());

        Beverage beverage1 = new Coffee(new Sugar());
        beverage1 = new Moka(beverage1);
        System.out.println(beverage1.getDescription());
        System.out.println(beverage1.cost());
    }
}
