package java8.optional.dto;

import lombok.Data;

/**
 * @author huangpin
 * @version 创建时间：18/5/23 下午2:47
 */
@Data
public class OptionalDTO {

    private String name;

    private String mobile;

    private InOptionalDTO inOptionalDTO;

    @Data
    public class InOptionalDTO {
        private String name;

        private String mobile;
    }
}
