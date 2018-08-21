package head.first.factory.pizza;

/**
 * Created by huangpin on 17/7/20.
 */
public class ChessPizza extends Pizza {

    public ChessPizza() {
        this.pizzaName = "ChessPizza";
    }

    public void prepare() {
        System.out.println("prepare " + this.pizzaName);
    }

    public void box() {
        System.out.println("box " + this.pizzaName);
    }
}
