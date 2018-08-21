package yaml.groovy;

import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by huangpin on 18/1/8.
 */
@Slf4j
public class FieldsConfigLoader {


    private static Map<String, ReportFieldConfig> fieldConfigMap = new HashMap<>();

    static {

        try {

            List<ReportFieldConfig> fieldConfigs = new YamlConfigDirLoader("satan-lesson/src/main/resources/yaml/").loadConfigs();

            fieldConfigs.forEach(

                    fc -> fieldConfigMap.put(fc.getName(), fc)

            );

            log.info("fieldConfigs: {}", fieldConfigs);

        } catch (Exception ex) {

            log.error("failed to load fields conf", ex);

        }


    }


    public static ReportFieldConfig getFieldConfig(String name) {

        return fieldConfigMap.get(name);

    }
}
