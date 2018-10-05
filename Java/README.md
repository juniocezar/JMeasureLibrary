# JMeasure
Simple Tool to collect power samples from the smartpower2 device (with adapted firmware)

Usage:
Include the compile lib to the classpath

include in your sources:

import lac.JMeasureLibrary;


...
JMeasureLibrary jm = new JMeasureLibrary();

jm.enableMonitor();
jm.startMeasurement();


// code to be monitored

jm.finishMeasurement() or jm.stopMeasurement()
jm.disableMonitor();
