import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

/**
 * JSONtest
 * <p>
 * Created by h
 * on 2018-07-10 18:09.
 *
 * @author h
 */
public class JsonTest {
    public static void main(String[] args) {

        JSONObject params = new JSONObject();
        params.put("test_params","test");


        TestDTO testDTO = JSON.parseObject(params.toJSONString(), TestDTO.class);
        System.out.println(JSON.toJSON(testDTO));
    }

}