package lac;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Library for enabling and collecting power measurement on the 
 * smartpower 2 device
 * @author juniocezar
 */
public class JMeasureLibrary {
    private boolean monitorEnabled = false;
    File file=null;
    FileOutputStream fileOutputStream=null;
    PrintStream printStream=null;
    
    /**
     * Constructor for the Library. Instantiates the writer, pointing it to the 
     * usb device.
     * @param usb The location for the usb device.
     */
    public JMeasureLibrary(String usb) {
        try {
            File file=new File(usb);
            fileOutputStream=new FileOutputStream(file);
            printStream=new PrintStream(fileOutputStream);
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
            //writer.write(state);            
            printStream.print(state);
        } catch (Exception ex) {
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
     * Sends a 'finish measurement' signal to the measurement device. This way,
     * the device will report the value 0 (ZERO) as power value.
     * @return Success of operation.
     */
    public boolean finishMeasurement() {
        return changeMeterState("B");
    }
    
    
    /**
     * Sample usage for the library
     */
    public static void sample(){
        System.out.println("Sample started");
        
        JMeasureLibrary JM = new JMeasureLibrary();
        JM.enableMonitor();       
        
        JM.startMeasurement();
        try {
            System.out.println("Sample paused");
            Thread.sleep(3000);
        } catch (InterruptedException ex) {
            //
        }
                
        System.out.println("Sample restored");
        JM.finishMeasurement();
        JM.disableMonitor();        
        
    }
    
    public static void main(String[] args){
        sample();
    }
}
