package com.satan.common.web.result.plus;

import com.alibaba.fastjson.annotation.JSONField;
import com.satan.common.utils.DateUtil;
import com.satan.common.web.serializer.BigDecimalSerializer;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by huangpin on 17/7/25.
 */
@Data
@Builder
public class TestDTO {

    private String name;
    private String idCard;
    @JSONField(format = DateUtil.DEFAULT_FORMAT)
    private Date time;
    @JSONField(serializeUsing = BigDecimalSerializer.class)
    private BigDecimal one;
    @JSONField(serializeUsing = BigDecimalSerializer.class)
    private BigDecimal two;
    private TestSubDTO subDTO;

    @Data
    public class TestSubDTO {
        private String name;
        private String idCard;
        private Date time;
        private BigDecimal one;
        private BigDecimal two;
    }
}
