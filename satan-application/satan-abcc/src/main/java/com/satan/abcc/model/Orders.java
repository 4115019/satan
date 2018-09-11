package com.satan.abcc.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Created by h
 * on 2018-09-10 12:13.
 *
 * @author h
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Orders {

    private List<Order> orders;
}