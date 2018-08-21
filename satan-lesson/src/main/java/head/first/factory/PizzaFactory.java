package head.first.factory;

import head.first.factory.pizza.ChessPizza;
import head.first.factory.pizza.CoffeePizza;
import head.first.factory.pizza.Pizza;

/**
 * Created by huangpin on 17/7/20.
 */
public class PizzaFactory {

    public Pizza createPizza(String pizzaName) {

        if (pizzaName.equals("ChessPizza")) {
            return new ChessPizza();
        } else {
            return new CoffeePizza();
        }
    }
}
