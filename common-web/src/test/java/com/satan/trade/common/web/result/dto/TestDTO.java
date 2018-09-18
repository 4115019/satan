package com.satan.trade.common.web.result.dto;

import com.alibaba.fastjson.annotation.JSONField;
import com.satan.trade.common.enums.BaseIntEnum;
import com.satan.trade.common.web.serializer.BaseIntSerializer;
import com.satan.trade.common.web.serializer.BigDecimalSerializer;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by huangpin on 17/3/14.
 */
@Data
@Builder
public class TestDTO {
    private String testName;

    @JSONField(format = "yyyy-MM-dd")
    private Date yyyyMMdd;

    private Date commonType;

    @JSONField(serializeUsing = BigDecimalSerializer.class,deserialize = false)
    private BigDecimal sum;

    @JSONField(serializeUsing = BaseIntSerializer.class,deserialize = false)
    private BaseIntEnum intEnum;
}