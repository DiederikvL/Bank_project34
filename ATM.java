import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;

import java.time.*;


import static java.lang.Thread.sleep;


public class ATM{

    private ATMScreen as;
    private Point pos;
    private DisplayText display1;
    private DisplayText display2;
    private DisplayText InputX;




    private boolean login = false;
    private String ShowX = "";
    public String balance;
    private boolean gekozen = false;
    public String amount;
    public String input;
    private String response;
    String cardnumber;



    GetRequest http = new GetRequest();

    public ATM(){

        as = new ATMScreen();
        Frame f = new Frame("My ATM");
        f.setBounds(200, 200, 800, 600);
        f.setBackground(Color.RED);
        f.addWindowListener(new MyWindowAdapter(f));
        f.add(as);
        f.setVisible(true);


        Output.open();

        try {
            sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        while (true) {           // na elke transactie wordt deze weer opnieuw aangeroepen zodat er transacties achter elkaar kunnen gedaan worden
            doTransaction();
        }

    }

    public void doTransaction() {



        String Input;
        String pin = "";        // pin leegmaken
        String consoleInput;
        String amount = "";     // amount leegmaken
        int bedragint = 0;      // bedrag op 0 zetten
        login = false;
        gekozen = false;


        pos = new Point(250, 30);
        as.clear();
        display1 = new DisplayText("text", pos);
        pos.y += 50;
        display2 = new DisplayText("text2", pos);
        pos.x += 50;
        InputX = new DisplayText("XXXX", pos);
        pos.y -= 50;
        as.add(InputX);
        as.add(display1);
        display1.giveOutput("Insert your card");
        Cardreader cardreader = new Cardreader("cardreader1");
        Keypad keypad1 = new Keypad("keypad1");
        pos.x = 600;
        pos.y = 350;
        ScreenButton exit = new ScreenButton("Exit", pos);
        ArrayList<ScreenButton> knoplijst = new ArrayList<ScreenButton>();
        as.add(exit);
        knoplijst.add(exit);


        //Bank bank1 = new Bank();
        do {
            cardnumber = cardreader.getInput();
            System.out.println(cardnumber);

        } while (cardnumber == null);

        System.out.println("Enter pin");
        display1.giveOutput("Enter pin.");


        // Knoppen worden toegevoegd aan een Arraylist om de knoppen te kunnen checken


        // pin wordt door keypad en knoppen ingelezen.
        while (!login) {

            for (ScreenButton button : knoplijst) {
                input = button.getInput();
                consoleInput = keypad1.getInput();

                if (consoleInput != null) {          // Input via console wordt ingelezen
                    System.out.println(consoleInput);
                    pin += consoleInput;
                    ShowX += "* ";
                    InputX.giveOutput(ShowX);


                }
                if (input == "Exit") {
                    pin = "";
                    ShowX = "";
                    doTransaction();

                }

                    /* Als er 4 cijfers zijn ingevoerd zal deze vergeleken worden met de pin van de gebruiker
                        Als deze gelijk zijn zal er ingelogd worden.
                    */


                if (pin.length() == 4) {

                    try {
                        response = http.httpRequest("Authentication/" + pin + "/" + cardnumber);
                        response = response.replaceAll("\n", "");
                        System.out.println(response);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    /* Als er 1 wordt terug gegeven betekend dat, dat de pincode correct is*/
                    if (response.equals("1")) {
                        login = true;

                        ShowX = "";
                        System.out.println("Pin correct");
                        as.clear();
                        Menu(pin);

                    }

                    /* Als er 2 wordt terug gegeven betekend dat, dat de pas geblokkeerd is */

                    else if (response.equals("2")) {
                        as.clear();
                        pin = "";
                        display1.giveOutput("Card is blocked");
                        display2.giveOutput("Contact bank");
                        System.out.println("Card blocked");
                        ShowX = "";
                        as.add(display1);
                        as.add(display2);

                        try {
                            sleep(2000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        doTransaction();

                    }
                    /* Als er iets anders terug gegeven wordt is de pin fout*/
                    else {
                        pin = "";
                        ShowX += "X";
                        display1.giveOutput("Wrong password");
                        as.add(display1);
                        System.out.println("Wrong password");
                        ShowX = "";


                        try {
                            sleep(2000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        doTransaction();


                    }
                    as.clear();

                }

            }
        }
    }

    public void Menu(String pin){

    /** In het menu is er keuze om je saldo te checken, een bepaald bedrag op te nemen of om snel R 50 op te nemen. Ook kan je vanuit het menu uitloggen.*/
        as.clear();

        String KeuzeMenu = "";
        int Balance;


        ArrayList<ScreenButton> knoplijst = new ArrayList<ScreenButton>();

        display1.giveOutput("Menu");
        pos.x = 30;
        pos.y = 100;
        ScreenButton Checkbalance = new ScreenButton("Check Balance", pos);
        pos.y += 100;
        ScreenButton Withdraw = new ScreenButton("Withdraw", pos);
        pos.y += 100;
        ScreenButton snelcash = new ScreenButton("Snel R50", pos);
        pos.x = 600;
        pos.y = 350;
        ScreenButton exit = new ScreenButton("Exit", pos);
        knoplijst.add(exit);
        knoplijst.add(snelcash);

        knoplijst.add(Checkbalance);
        knoplijst.add(Withdraw);

        as.add(display1);
        as.add(Checkbalance);
        as.add(snelcash);
        as.add(Withdraw);
        as.add(exit);

        while (!gekozen) {
            for (ScreenButton button : knoplijst) {
                input = button.getInput();

                if (input != null && input == "Exit") {
                    as.clear();
                    doTransaction();
                    gekozen = true;
                } else if (input != null) {
                    KeuzeMenu = input;
                    gekozen = true;
                }


            }
        }

        gekozen = false;

        as.clear();

        if (KeuzeMenu == "Check Balance") {
            CheckBalance(pin);
        }


        if (KeuzeMenu == "Withdraw") {
            Withdraw(pin);
        }

        if(KeuzeMenu == "Snel R50"){
            Snel50(pin);
        }

    }

    public void Withdraw(String pin){

        /** In withdraw kan je kiezen welk bedrag je wilt hebben en ik welke biljetten je dit bedrag wilt hebben
         * ook heb je de mogelijkheid om uit te loggen of om terug naar het menu te gaan*/
        as.clear();

        int bedragint = 0;
        int aantal10 = 0;
        int aantal20 = 0;
        int aantal50 = 0;

        String consoleInput;

        int hoeveel10;
        int hoeveel20;
        int hoeveel50;



        try {
            response = http.httpRequest("billItems/" + 10);
            response = response.replaceAll("\n", "");
        } catch (IOException e) {
            e.printStackTrace();
        }

        hoeveel10 = Integer.parseInt(response);

        try {
            response = http.httpRequest("billItems/" + 20);
            response = response.replaceAll("\n", "");
        } catch (IOException e) {
            e.printStackTrace();
        }

        hoeveel20 = Integer.parseInt(response);

        try {
            response = http.httpRequest("billItems/" + 50);
            response = response.replaceAll("\n", "");
        } catch (IOException e) {
            e.printStackTrace();
        }

        hoeveel50 = Integer.parseInt(response);



        //Keypad keypad2 = new Keypad("keypad2");
        ArrayList<ScreenButton> knoplijst = new ArrayList<ScreenButton>();

        pos.x = 150;
        pos.y = 150;
        DisplayText x10 = new DisplayText("x10",pos );

        pos.y = 250;
        DisplayText x20 = new DisplayText("x20",pos );

        pos.y = 350;
        DisplayText x50 = new DisplayText("x50",pos );
        pos.y += 50;
        DisplayText line = new DisplayText("line", pos);
        pos.y += 50;
        pos.x = 10;
        DisplayText totaal = new DisplayText("totaal", pos);


        display1.giveOutput("Choose amount");


        as.add(display1);
        as.add(x10);
        as.add(x20);
        as.add(x50);
        as.add(line);
        as.add(totaal);


        pos.y = 150;
        pos.x = 10;
        ScreenButton mintien = new ScreenButton("-R10", pos);
        pos.x += 60;
        ScreenButton plustien = new ScreenButton("+R10", pos);
        pos.y += 100;
        ScreenButton plustwintig = new ScreenButton("+R20", pos);
        pos.x -= 60;
        ScreenButton mintwintig = new ScreenButton("-R20", pos);
        pos.y += 100;
        ScreenButton minvijftig = new ScreenButton("-R50", pos);
        pos.x += 60;
        ScreenButton plusvijftig = new ScreenButton("+R50", pos);

        pos.x = 600;
        pos.y = 450;
        ScreenButton verder = new ScreenButton("Verder", pos);


        pos.y = 400;
        ScreenButton terug = new ScreenButton("Back", pos);

        pos.y = 350;
        ScreenButton exit = new ScreenButton("Exit", pos);


        as.add(plustien);
        as.add(mintien);
        as.add(mintwintig);
        as.add(minvijftig);
        as.add(plustwintig);
        as.add(plusvijftig);
        as.add(verder);
        as.add(terug);
        as.add(exit);


        knoplijst.add(verder);
        knoplijst.add(terug);
        knoplijst.add(exit);
        knoplijst.add(mintien);
        knoplijst.add(mintwintig);
        knoplijst.add(minvijftig);
        knoplijst.add(plustien);
        knoplijst.add(plustwintig);
        knoplijst.add(plusvijftig);



        while (!gekozen) {

            x10.giveOutput(aantal10 + " x  R 10");
            x20.giveOutput(aantal20 + " x  R 20");
            x50.giveOutput(aantal50 + " x  R 50");
            line.giveOutput("___________ +");
            totaal.giveOutput("Totaal   " + Integer.toString(bedragint));




            for (ScreenButton button : knoplijst) {
                input = button.getInput();

                // Als er voldoende biljetten van 10 in de automaat zitten kan er op +10 worden geklikt
                if (input != null && input == "+R10" && aantal10 < hoeveel10) {
                        bedragint += 10;
                        System.out.println("+R10");
                        aantal10 += 1;

                }
                // Als er +10 is ingedrukt kan deze weggehaald worden met -10
                if (input != null && input == "-R10") {
                    if(aantal10 < 1){
                        bedragint = bedragint;

                    }
                    else {
                        bedragint -= 10;
                        System.out.println("-R10");
                        aantal10 -= 1;
                    }

                }

                // Als er voldoende biljetten van 20 in de automaat zitten kan er op +20 worden geklikt
                else if (input != null && input == "+R20" && aantal20 < hoeveel20) {

                        bedragint += 20;
                        System.out.println("+R20");
                        aantal20 += 1;


                }

                // Als er +20 is ingedrukt kan deze weggehaald worden met -20
                else if (input != null && input == "-R20") {
                    if(aantal20 < 1){
                        bedragint = bedragint;

                    }
                    else{
                        bedragint -= 20;
                        System.out.println("-R20");
                        aantal20 -= 1;
                    }
                }

                // Als er voldoende biljetten van 50 in de automaat zitten kan er op +50 worden geklikt
                else if (input != null && input == "+R50" && aantal50 < hoeveel50) {
                        bedragint += 50;
                        System.out.println("+R50");
                        aantal50 += 1;


                }
                // Als er +50 is ingedrukt kan deze weggehaald worden met -50
                else if (input != null && input == "-R50") {
                    if(aantal50 < 1){
                        bedragint = bedragint;

                    }
                    else {
                        bedragint -= 50;
                        System.out.println("-R50");
                        aantal50 -= 1;
                    }
                }

                //Verder naar geld uitwerpen
                else if(input != null && input == "Verder") {

                    Takecash(pin, bedragint, aantal10, aantal20, aantal50);
                    gekozen = true;
                }

                //Terug naar het menu
                else if(input != null && input == "Back"){
                    Menu(pin);
                }

                // Uitloggen
                else if(input != null && input == "Exit"){
                    doTransaction();
                }

            }

        }

    }

    public void Pinbon(String pin, int bedragint, int aantal10, int aantal20, int aantal50){
        LocalTime time = LocalTime.now();
        LocalDate date = LocalDate.now();




        amount = Integer.toString(bedragint);


        ArrayList<ScreenButton> knoplijst = new ArrayList<ScreenButton>();

        as.clear();

        display1.giveOutput("Do you want a receipt?");
        pos.x = 300;
        pos.y = 200;
        ScreenButton yes = new ScreenButton("Yes", pos);
        pos.x += 100;
        ScreenButton no = new ScreenButton("No  ", pos);
        pos.x = 600;
        pos.y = 400;
        ScreenButton terug = new ScreenButton("Back", pos);

        pos.y = 350;
        ScreenButton exit = new ScreenButton("Exit", pos);

        as.add(yes);
        as.add(no);
        as.add(terug);
        as.add(exit);
        as.add(display1);

        knoplijst.add(yes);
        knoplijst.add(no);
        knoplijst.add(terug);
        knoplijst.add(exit);



        while (!gekozen) {
            for (ScreenButton button : knoplijst) {  //loopen langs alle knoppen om te checken of er een wordt ingedrukt

                input = button.getInput();

                // wanneer YES wordt aangeklikt zal er een bon worden geprint
                if (input != null && input.equals("Yes")) {
                    // print bon

                    String hashiban = "";


                    hashiban = cardnumber.substring(11);

                    as.clear();




                    Output.sendToArduino( amount + ">" +hashiban + ">" + date + ">" + time.toString().substring(0,8));

                    try {
                        Thread.sleep(10000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }


                    gekozen = true;
                }

                // wanneer NO wordt aangeklikt zal er geen bon worden geprint
                else if (input != null && input.equals("No  ")) {
                    System.out.println("No receipt");
                    gekozen = true;
                }

                else if(input != null && input == "Back"){
                    Withdraw(pin);
                }
                else if(input != null && input == "Exit"){
                    doTransaction();
                }


            }
        }
        doTransaction();

        //Takecash(pin, bedragint, aantal10, aantal20, aantal50);

    }
    public void Takecash(String pin, int bedragint, int aantal10, int aantal20, int aantal50){

        amount = Integer.toString(bedragint);

       try {
            response = http.httpRequest("Withdraw/" + cardnumber + "/ATM/" + amount + "/" + pin);
            response  =response.replaceAll("\n","");
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (!response.equals("0")) {

            as.clear();
            display1.giveOutput("You don't have enough credit");
            display2.giveOutput("Try other amount");

            as.add(display1);
            as.add(display2);

            try {
                sleep(2000);    //wacht 1 seconde
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            as.clear();

            Withdraw(pin);




        }
     else {
            as.clear();

            display1.giveOutput("Now dispensing €" + bedragint);
            as.add(display1);


            if (aantal50 > 0) {
                Output.sendToArduino("&" + aantal50);
                try {
                    Thread.sleep(4000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                try {
                    response = http.httpRequest("billItems/" + 50 + "/" + aantal50);
                    response = response.replaceAll("\n", "");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (aantal20 > 0) {
                Output.sendToArduino("%" + aantal20);
                try {
                    Thread.sleep(4000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                try {
                    response = http.httpRequest("billItems/" + 20 + "/" + aantal20);
                    response = response.replaceAll("\n", "");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (aantal10 > 0) {
                Output.sendToArduino("$" + aantal10);
                try {
                    Thread.sleep(4000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                try {
                    response = http.httpRequest("billItems/" + 10 + "/" + aantal10);
                    response = response.replaceAll("\n", "");
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }


            try {                           // wacht 3 seconden
                sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            as.clear();

            display1.giveOutput("Please take your");
            display2.giveOutput("card and cash");

            as.add(display1);
            as.add(display2);

            try {
                sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            as.clear();


        }

     Pinbon(pin, bedragint, aantal10, aantal20, aantal50);
    }

    public void CheckBalance(String pin){
        ArrayList<ScreenButton> knoplijst = new ArrayList<ScreenButton>();

       balance = getBalance(pin);

        display1.giveOutput("Balance:");
        display2.giveOutput(balance);
        pos.x = 600;
        pos.y = 400;
        ScreenButton terug = new ScreenButton("Back", pos);
        pos.x = 600;
        pos.y = 350;
        ScreenButton exit = new ScreenButton("Exit", pos);
        knoplijst.add(terug);
        knoplijst.add(exit);

        as.add(display1);
        as.add(display2);
        as.add(terug);
        as.add(exit);

        while (!gekozen) {
            for (ScreenButton button : knoplijst) {
                input = button.getInput();

                if (input == "Back") {
                    as.clear();
                    Menu(pin);
                    gekozen = true;
                }
                if(input == "Exit"){
                    as.clear();
                    doTransaction();
                    gekozen = true;
                }


            }
        }
        gekozen = false;


        try {                           // wacht 3 seconden
            sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public String getBalance(String pin){
        GetRequest get = new GetRequest();
        try {
            balance = get.httpRequest("ClientSaldo/" + cardnumber);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return balance;
    }

    public void Snel50(String pin){


        try {
            response = http.httpRequest("Withdraw/" + cardnumber + "/ATM/" + 50 + "/" + pin);
            response  =response.replaceAll("\n","");
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (!response.equals("0")) {

            as.clear();
            display1.giveOutput("You don't have enough credit");
            display2.giveOutput("Try other amount");

            as.add(display1);
            as.add(display2);

            try {
                sleep(2000);    //wacht 1 seconde
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            as.clear();

            Withdraw(pin);




        }
        else {
            as.clear();

            display1.giveOutput("Now dispensing €" + 50);
            as.add(display1);



                Output.sendToArduino("&" + 1);
                try {
                    Thread.sleep(4000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                try {
                    response = http.httpRequest("billItems/" + 50 + "/" + 1);
                    response = response.replaceAll("\n", "");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }


            try {                           // wacht 3 seconden
                sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            as.clear();

            display1.giveOutput("Please take your");
            display2.giveOutput("card and cash");

            as.add(display1);
            as.add(display2);

            try {
                sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            as.clear();

            doTransaction();

    }







}