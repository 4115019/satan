package basic;

import lombok.AllArgsConstructor;

/**
 * Created by huangpin on 18/1/8.
 */
@AllArgsConstructor
public class Student implements Person {

    private String studentId;
    private String name;
    private String able;

    @Override
    public String toString() {
        return "Student{" +
                "studentId='" + studentId + '\'' +
                ", name='" + name + '\'' +
                ", able='" + able + '\'' +
                '}';
    }

    @Override
    public String getId() {
        return studentId;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String able() {
        return able;
    }
}
