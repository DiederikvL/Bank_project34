import com.fazecast.jSerialComm.SerialPort;

import java.io.IOException;


class Output {


    private static SerialPort serialPort = SerialPort.getCommPort("/dev/cu.wchusbserial1420");




    static void open(){

        serialPort.setBaudRate(9600);

        //open the port
        serialPort.openPort();
        serialPort.setComPortTimeouts(SerialPort.TIMEOUT_READ_SEMI_BLOCKING, 0, 0);
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("port opened succesfully");


    }

    static void sendToArduino(String i) {


        try {
            serialPort.getOutputStream().write(i.getBytes());
            serialPort.getOutputStream().flush();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }












}