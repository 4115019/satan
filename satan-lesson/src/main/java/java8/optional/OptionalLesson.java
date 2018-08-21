package java8.optional;

import java8.optional.dto.OptionalDTO;

import java.util.Optional;

/**
 * @author huangpin
 * @version 创建时间：18/5/23 下午2:33
 */
public class OptionalLesson {
    public static void main(String[] args) throws Exception {

        OptionalDTO optionalDTO = new OptionalDTO();
//        optionalDTO.setMobile("mobile1");
        optionalDTO.setName("name1");

        OptionalDTO.InOptionalDTO inOptionalDTO = optionalDTO.new InOptionalDTO();
//        inOptionalDTO.setMobile("mobile2");
        inOptionalDTO.setName("name2");

        optionalDTO.setInOptionalDTO(inOptionalDTO);

        Optional<OptionalDTO> optionalModel = Optional.ofNullable(optionalDTO);

        String mobile = optionalModel.map(out -> out.getInOptionalDTO())
                .map(in -> in.getMobile())
                .orElseThrow(() -> new Exception("test"));
        System.out.println(mobile);
    }
}
