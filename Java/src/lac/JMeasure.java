package lac;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Library for enabling power measurement and collect samples from the 
 * smartpower 2 device
 * @author juniocezar
 */
public class JMeasure {
    private boolean monitorEnabled = false;
    File file=null;
    FileOutputStream fileOutputStream=null;
    PrintStream printStream=null;
    
    /**
     * Constructor for the Library. Instantiates the writer, pointing it to the 
     * usb device.
     * @param usb The location for the usb device.
     */
    public JMeasure(String usb) {
        try {
            File file=new File(usb);
            fileOutputStream=new FileOutputStream(file);
            printStream=new PrintStream(fileOutputStream);
        } catch (IOException ex) {
            System.err.println("JMeasureLibrary is killing the application because" + 
                "of the following problem:\n\n");
            Logger.getLogger(JMeasure.class.getName()).log(Level.SEVERE, null, ex);
            System.exit(1);
        }
    }
    
    /**
     * Calls the main constructor with the usb device pointed to "/dev/ttyUSB0".
     */
    public JMeasure() {
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
            //writer.write(state);            
            printStream.print(state);
        } catch (Exception ex) {
            success = false;
            Logger.getLogger(JMeasure.class.getName()).log(Level.SEVERE, null, ex);
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
        boolean state = changeMeterState("D");
        
        // move this writer close to somewhere else
        try {
            if(fileOutputStream!=null){
		fileOutputStream.close();
            }
            if(printStream!=null){
		printStream.close();
            }
	} catch (Exception e) {
            e.printStackTrace();
	}
        
        return state;
        
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
     * Sends a 'stop measurement' signal to the measurement device. This way,
     * the device will report the value 0 (ZERO) as power value.
     * @return Success of operation.
     */
    public boolean stopMeasurement() {
        return changeMeterState("B");
    }
    
    
    /**
     * Sample usage for the library
     */
    public static void sample(){
        System.out.println("Sample started");
        
        JMeasure JM = new JMeasure();
        JM.enableMonitor();       
        
        JM.startMeasurement();
        try {
            System.out.println("Sample paused");
            Thread.sleep(3000);
        } catch (InterruptedException ex) {
            //
        }
                
        System.out.println("Sample restored");
        JM.stopMeasurement();
        JM.disableMonitor();        
        
    }
    
    public static void main(String[] args){
        if (args.length < 1)
            return;
        
        JMeasure jm = new JMeasure();
        
        if(args[0].equals("enable"))
            jm.enableMonitor();
        else if(args[0].equals("disable"))
            jm.disableMonitor();
        else if(args[0].equals("start"))
            jm.startMeasurement();
        else if(args[0].equals("stop"))
            jm.stopMeasurement();
        else
            System.err.println("Command: " + args[0] + " is not valid");
    }
}
