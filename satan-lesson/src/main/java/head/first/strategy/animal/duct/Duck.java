package head.first.strategy.animal.duct;

import head.first.strategy.animal.Animal;
import head.first.strategy.behavior.Behavior;
import lombok.AllArgsConstructor;

/**
 * Created by huangpin on 17/7/14.
 */
@AllArgsConstructor
public class Duck implements Animal {

    private Behavior flyBehavior;

    private Behavior swimBehavior;

    public void fly() {
        flyBehavior.perform();
    }

    public void swim() {
        swimBehavior.perform();
    }

    public void introduce() {
        System.out.println("i am a duck");
    }
}
