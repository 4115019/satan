import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Created by h
 * on 2018-07-10 18:10.
 *
 * @author h
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TestDTO {

    private String testParams;
}