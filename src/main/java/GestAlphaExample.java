
import development.DevelopmentLoader;
import development.display.HandVisualizer;
import development.display.OrientationVisualizer;
import development.display.framework.DevelopmentInterfaceDisplay;
import development.display.framework.LibgdxDisplay;
import handvis.HandVisMotionProcessor;
import handvis.ListCoordinatesShim;
import hardware.INotifiedReceiver;
import hardware.NotifiedHardware;
import hardware.parsers.ByteParser;
import hardware.receivers.SerialReceiver;
import hypegoatV_0_1_1.HypegoatV_0_1_2_Parser;
import motion.*;
import motion.coordinates.ICoordinates;
import utility.Context;
import utility.ICallback;
import utility.ILoader;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * Created by mik on 9/2/15.
 */
public class GestAlphaExample {

    public static void main (String[] args) throws Exception {
        final Context context = new Context();


        INotifiedReceiver<List<Double>> hardware = new NotifiedHardware<>(new SerialReceiver(HypegoatV_0_1_2_Parser.NUM_BYTES), new HypegoatV_0_1_2_Parser());
        hardware.onUpdate(new ICallback<List<Double>>() {
            @Override
            public void call(List<Double> input) {
                List subL = new ArrayList();
                subL.addAll(input.subList(0,6));
                subL.add((input.get(input.size()-1))/1000000);
                context.writeCallbacks("data", subL);
            }
        });

        context.addCallback("data", "motion-shim", new ICallback<List<Double>>() {
            @Override
            public void call(List<Double> input) {
                context.writeCallbacks("motion-shim", new ListCoordinatesShim(input));
            }
        });

        ILoader loader = new DevelopmentLoader("./src/main/assets");

        ISensorArray sensorArray = new SensorArray(new MotionConfig().getSpecification(context, loader));
        final IMotionProcessing motionProcessing = new MotionProcessing(sensorArray);

        context.addCallback("motion-shim", "motion-processing", Context.buildAsyncSequential(new ICallback<ICoordinates>() {
            @Override
            public void call(ICoordinates input) {
                context.writeCallbacks("motion-processing", motionProcessing.process(input));
            }
        }));

        DevelopmentInterfaceDisplay dd = new DevelopmentInterfaceDisplay();
        dd.setSize(800,500);
        dd.setVisible(true);
        final OrientationVisualizer ov = new OrientationVisualizer();
        dd.addDisplay("ov", new LibgdxDisplay(ov));
        context.addCallback("motion-processing","display",Context.buildIgnorer(new ICallback<IMotion>() {
            @Override
            public void call(final IMotion input) {
                ov.update(input.getMotion(0));
                System.out.println(input.getMotion(0));
            }
        }));



    }
}
