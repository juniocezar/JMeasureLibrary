# JMeasureLibrary
Java and C libraries for connecting to the smartpower2 through usb cable and collecting power samples.


## Installation

You just need to download and compile the C++ and/or JAVA libraries. No external libraries are needed.

## Usage

C API

```c
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
```



JAVA API

```java
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
```

## Contributing
Pull requests are welcome. 


## License
Apache2
