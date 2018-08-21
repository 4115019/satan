package com.satan.web.dto;

import com.satan.common.web.validator.Name;
import lombok.Data;

/**
 * Created by huangpin on 17/3/16.
 */
@Data
public class PostDTO {

    @Name(message = "姓名格式错误")
    private String name;
}
