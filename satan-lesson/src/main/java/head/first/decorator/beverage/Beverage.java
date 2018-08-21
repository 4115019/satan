package head.first.decorator.beverage;

import lombok.Data;

/**
 * Created by huangpin on 17/7/18.
 */
@Data
public abstract class Beverage {

    private String description = "UNKNOW BEVERAGE";

    public abstract double cost();
}
