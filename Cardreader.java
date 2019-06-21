import java.io.BufferedReader;
import  java.io.InputStreamReader;
import java.util.Scanner;

public class Cardreader extends HardwareElement implements InputDevice{

    private BufferedReader bufferreader;
    private InputStreamReader streamreader;
    private static String input = "";

    Cardreader(String naam){

        super(naam); //superclass aanroepen

        streamreader = new InputStreamReader(System.in);
        bufferreader = new BufferedReader(streamreader);

    }

    @Override
    public String getInput() {
        input = null; //clear input
        Serial2.listenSerial(); //listen for input from the arduino
        return input; //return the input from setInput
    }

    public static void setInput(String data){

        input = data; //store the value in the field input
    }
}