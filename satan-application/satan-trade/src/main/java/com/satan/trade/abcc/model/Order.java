package com.satan.trade.abcc.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.util.Random;

/**
 * Created by h
 * on 2018-09-10 12:05.
 *
 * @author h
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Order {
    private BigDecimal volume;

    private BigDecimal originVolume;

    private BigDecimal avgPrice;

    private String kind;


    public Boolean isAsk() {
        if (StringUtils.isEmpty(this.kind)) {
            return null;
        }

        if ("ask".equals(this.kind)) {
            return true;
        } else {
            return false;
        }
    }
}