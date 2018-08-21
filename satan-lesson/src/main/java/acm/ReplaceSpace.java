package acm;

/**
 * @author huangpin
 * @version 创建时间：18/5/22 上午11:38
 */
public class ReplaceSpace {
    public String replaceSpace(StringBuffer str) {

        int count = 0;
        for (int i = 0; i < str.length(); i++) {
            if (str.charAt(i) == ' ') {
                count++;
            }
        }

        str.setLength(str.length() + 2 * count);

        for (int i = str.length() - 1 - 2 * count; i >= 0; i--) {
            if (str.charAt(i) == ' ') {
                count--;
                str.setCharAt(i + 2 * count, '%');
                str.setCharAt(i + 2 * count + 1, '2');
                str.setCharAt(i + 2 * count + 2, 'd');
            } else {
                str.setCharAt(i + 2 * count, str.charAt(i));
            }
        }

        return str.toString();
    }

    public static void main(String[] args) {
        System.out.println(new ReplaceSpace().replaceSpace(new StringBuffer("hello world")));
    }
}