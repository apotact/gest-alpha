import motion.ISensorArraySpecification;
import motion.SensorArraySpecification;
import motion.shims.LibgdxExpectedMappings;
import motion.shims.SensorSpecificationBuilder;
import utility.Context;
import utility.ILoader;

/**
 * Created by mik on 9/2/15.
 */
public class MotionConfig {
        public ISensorArraySpecification getSpecification(Context context, ILoader loader) throws Exception {
            LibgdxExpectedMappings mappings = new LibgdxExpectedMappings();
            SensorSpecificationBuilder builder = new SensorSpecificationBuilder(context, "motion-shim", loader, mappings.gyroscopeMapping(), mappings.accelerometerMapping());
            for(int s = 0; s < 1; s++) {
                for(int i = 0; i < 3; i++) {
                    builder.addIndex().setSensorIdentifier(s).setType(SensorSpecificationBuilder.Type.gyroscope);
                }
                for(int i = 0; i < 3; i++) {
                    builder.addIndex().setSensorIdentifier(s).setType(SensorSpecificationBuilder.Type.accelerometer);
                }
            }
//            for(int i = 0; i < 3; i++) {
//                builder.addIndex().setSensorIdentifier(4).setType(SensorSpecificationBuilder.Type.magnetometer);
//            }
//            builder.addIndex().setSensorIdentifier(0,1,2,3,4).setType(SensorSpecificationBuilder.Type.time);
            builder.addIndex().setSensorIdentifier(0).setType(SensorSpecificationBuilder.Type.time);
            return new SensorArraySpecification(builder.build());
        }
}
