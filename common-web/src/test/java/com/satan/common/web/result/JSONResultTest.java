package com.satan.common.web.result;

import com.alibaba.fastjson.JSON;
import com.satan.common.web.result.dto.TestDTO;
import com.satan.common.web.result.enums.TestEnum;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by huangpin on 17/3/16.
 */
@Slf4j
public class JSONResultTest {

    @Test
    public void testResult(){
        TestDTO testDTO = TestDTO.builder().testName("test").yyyyMMdd(new Date()).commonType(new Date()).intEnum(TestEnum.TEST).sum(new BigDecimal("1.3656")).build();
        System.out.println(JSON.toJSONString(testDTO));
        System.out.println(JSONResult.ok(testDTO).buildJsonString());
    }
}
