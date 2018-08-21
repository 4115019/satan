package head.first.factory;

import head.first.factory.pizza.Pizza;

/**
 * Created by huangpin on 17/7/20.
 */
public class Main {
    public static void main(String[] args) {
        PizzaFactory pizzaFactory = new PizzaFactory();
        Pizza pizza = pizzaFactory.createPizza("ChessPizza");
        pizza.prepare();
        pizza.box();
    }
}
