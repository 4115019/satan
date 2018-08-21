package acm;

/**
 * @author huangpin
 * @version 创建时间：18/5/18 下午4:00
 */
public class ArrayFind {
    public boolean Find(int target, int[][] array) {
        if (array == null || array.length <= 0 || array[0].length <= 0) {
            return false;
        }
        return find(target, 0, 0, array);
    }

    public boolean find(int target, int row, int column, int[][] array) {

        if (array.length <= row || array.length <= column) {
            return false;
        }

        if (target < array[row][column]) {
            return false;
        }

        if (target == array[row][column]) {
            return true;
        }

        return find(target, row + 1, column, array) || find(target, row, column + 1, array);
    }

    public static void main(String[] args) {
        new ArrayFind().Find(16, new int[][]{});
    }
}