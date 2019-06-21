

import java.io.PrintStream;

import com.fazecast.jSerialComm.*;


public class Serial2 {
    public static String data;
   private static PrintStream out;

    public static void listenSerial() {
        /* Change "COM4" to your USB port connected to the Arduino
         * You can find the right port using the ArduinIDE
                */
        SerialPort comPort = SerialPort.getCommPort("/dev/cu.wchusbserial1410");

        //set the baud rate to 9600 (same as the Arduino)
        comPort.setBaudRate(9600);

        //open the port
        comPort.openPort();


//        comPort.setComPortTimeouts(SerialPort.TIMEOUT_READ_SEMI_BLOCKING, 0, 0);
//        out = new PrintStream(comPort.getOutputStream());

        //create a listener and start listening
        comPort.addDataListener(new SerialPortDataListener() {
            public int getListeningEvents() {
                return SerialPort.LISTENING_EVENT_DATA_AVAILABLE;
            }

            public void serialEvent(SerialPortEvent event)
            {
                if (event.getEventType() != SerialPort.LISTENING_EVENT_DATA_AVAILABLE) {
                    Cardreader.setInput(null);
                    return; //wait until we receive data
                }

                byte[] newData = new byte[comPort.bytesAvailable()]; //receive incoming bytes
                comPort.readBytes(newData, newData.length); //read incoming bytes
                //convert bytes to string
                String serialData = new String(newData);
                System.out.println(serialData);
                serialData = serialData.replaceAll("\\s+",""); //Removes all whitespaces and non-visible characters
                if(serialData.length() == 14){//8 length of nuid
                    Cardreader.setInput(serialData);
                }else if(serialData.length() == 1){
                    Keypad.setInput(serialData);
                }
            }
        });


    }
//   static void write(char a){
//
//        out.println(a);
//
//    }
}