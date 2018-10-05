/**
 * Library for enabling power measurement and collect samples from the 
 * smartpower 2 device
 * @author juniocezar
 */

#include <stdio.h>
#include <errno.h>

unsigned int jmeasureMonitorEnabled = 0;
FILE *UsbSerialPort = NULL;

    
/**
 * Constructor for the Library. Instantiates the writer, pointing it to the 
 * usb device.
 * @param usb The location for the usb device.
 */
unsigned int JMeasureInitUSB(char* usb) {
    UsbSerialPort = fopen(usb, "w");
    if (UsbSerialPort == NULL) {
        printf ("JMeasureLibrary is killing the application because of the "
            "following problem:\n\n, errno = %d\n", errno);
        return 1;
    }    
    return 0;
}
    
/**
 * Calls the main constructor with the usb device pointed to "/dev/ttyUSB0".
 */
unsigned int JMeasureInit() {
    // USB0 is the default first serial usb port to be used
    return JMeasureInitUSB("/dev/ttyUSB0");
}


/**
 * Closes the output UsbSerialPort // file
 */
void JMeasureFinish() {
    fclose(UsbSerialPort);
}
      
/**
 * Sends the new state to the SmartPower2 device.
 * @param state New state to be sent to the measurement device. Possible 
 * values are: "A", "B", "C", "D".
 * @return Success of operation.
 */
void JMeasureChangeMeterState(char* state) {     
    fprintf(UsbSerialPort, "%s", state);            
}

    
/**
 * Sends an 'enable state' to the measurement device. The device will then
 * echos this state to the telnet port, which can be captured by the JMeasure
 * Software.
 * @return Success of operation.
 */
void JMeasureEnableMonitor() {
   jmeasureMonitorEnabled = 1;
   JMeasureChangeMeterState("C");
}
    
/**
 * Sends a 'disable state' to the measurement device, which will also be
 * re-sent to the telnet end.
 * @return Success of operation.
 */
void JMeasureDisableMonitor() {
    jmeasureMonitorEnabled = 0;        
    JMeasureChangeMeterState("D");        
    JMeasureFinish();
}
    
/**
 * Sends a 'start measurement' signal to the measurement device. This way,
 * the device will report real measured power values.
 * @return Success of operation.
 */
void JMeasureStartMeasurement() {
    if (!jmeasureMonitorEnabled)
        JMeasureEnableMonitor();
    JMeasureChangeMeterState("A");
}
    
/**
 * Sends a 'stop measurement' signal to the measurement device. This way,
 * the device will report the value 0 (ZERO) as power value.
 * @return Success of operation.
 */
void JMeasureStopMeasurement() {
    JMeasureChangeMeterState("B");
}
   

/**
* Sample usage for the library
*/
void sample(){
    printf("Sample started");
    JMeasureInit();
    JMeasureEnableMonitor();               
    JMeasureStartMeasurement();
    
    int i = 100000000;
    while (i > 0) i--;
                
    printf("Sample restored");
    JMeasureStopMeasurement();
    JMeasureDisableMonitor();        
}

