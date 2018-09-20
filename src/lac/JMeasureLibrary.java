package lac;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Library for enabling and collecting power measurement on the 
 * smartpower 2 device
 * @author juniocezar
 */
public class JMeasureLibrary {
    private BufferedWriter writer;
    private boolean monitorEnabled = false;
    
    /**
     * Constructor for the Library. Instantiates the writer, pointing it to the 
     * usb device.
     * @param usb The location for the usb device.
     */
    public JMeasureLibrary(String usb) {
        try {
            writer = new BufferedWriter(new FileWriter(usb));
        } catch (IOException ex) {
            Logger.getLogger(JMeasureLibrary.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * Calls the main constructor with the usb device pointed to "/dev/ttyUSB0".
     */
    public JMeasureLibrary() {
        // USB0 is the default first serial usb port to be used
        this("/dev/ttyUSB0");
    }
    
    
    /**
     * Sends the new state to the SmartPower2 device.
     * @param state New state to be sent to the measurement device. Possible 
     * values are: "A", "B", "C", "D".
     * @return Success of operation.
     */
    private boolean changeMeterState(String state) {
        boolean success = true;
        try {                        
            writer.write(state);
            writer.close();
        } catch (IOException ex) {
            success = false;
            Logger.getLogger(JMeasureLibrary.class.getName()).log(Level.SEVERE, null, ex);
        }
        return success;
    }
    
    /**
     * Sends an 'enable state' to the measurement device. The device will then
     * echos this state to the telnet port, which can be captured by the JMeasure
     * Software.
     * @return Success of operation.
     */
    public boolean enableMonitor() {
        monitorEnabled = true;
        return changeMeterState("C");
    }
    
    /**
     * Sends a 'disable state' to the measurement device, which will also be
     * re-sent to the telnet end.
     * @return Success of operation.
     */
    public boolean disableMonitor() {
        monitorEnabled = false;
        return changeMeterState("D");
    }
    
    /**
     * Sends a 'start measurement' signal to the measurement device. This way,
     * the device will report real measured power values.
     * @return Success of operation.
     */
    public boolean startMeasurement() {
        if (!monitorEnabled)
            enableMonitor();
        return changeMeterState("A");
    }
    
    /**
     * Sends a 'finish measurement' signal to the measurement device. This way,
     * the device will report the value 0 (ZERO) as power value.
     * @return Success of operation.
     */
    public boolean finishMeasurement() {
        return changeMeterState("B");
    }
    
    
    /**
     * Sample
     */
    public static void sample(){
        JMeasureLibrary SL = new JMeasureLibrary();
        SL.enableMonitor();
        
        SL.startMeasurement();
        try {
            Thread.sleep(1);
        } catch (InterruptedException ex) {
            //
        }
        
        int a =0;
        for(int i = 0 ; i < 100000000; i--)
            a++;
        
        SL.finishMeasurement();
        SL.disableMonitor();        
    }
}
