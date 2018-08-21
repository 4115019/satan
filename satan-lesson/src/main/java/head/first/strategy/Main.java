package head.first.strategy;

import head.first.strategy.animal.duct.Duck;
import head.first.strategy.behavior.FlyNormalBehavior;
import head.first.strategy.behavior.FlyWithRocketBehavior;
import head.first.strategy.behavior.SwimNormalBehavior;
import head.first.strategy.behavior.SwimWithRocketBehavior;

/**
 * Created by huangpin on 17/7/14.
 */
public class Main {
    public static void main(String[] args) {
        Duck normalDuck = new Duck(new FlyNormalBehavior(), new SwimNormalBehavior());
        normalDuck.introduce();
        normalDuck.fly();
        normalDuck.swim();

        Duck rocketDuck = new Duck(new FlyWithRocketBehavior(), new SwimWithRocketBehavior());
        rocketDuck.introduce();
        rocketDuck.fly();
        rocketDuck.swim();
    }
}
