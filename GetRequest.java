import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

public class GetRequest {

    private static HttpURLConnection con;
    private  static String returnContent;

    public static String httpRequest(String urlParameter) throws MalformedURLException,
            ProtocolException, IOException {

        // Inloggen
        // response => 0 of 1
        // 1 => ingelogd
        // String url = "https://projectbank.azurewebsites.net/api/Authentication/[password]/[Nuid]";

        // Get Client saldo
        // api/ClientSaldo/[Iban]
        // -1 => badrequest
        // 0 => notfound
        // [saldo] => OK

        //URL: api/Withdraw/[iban]/[destination iban]/[amount]
        /*
         * -1 => badrequest
         *  0 => succesful
         *  1 => notfound
         */

        String url = "https://project34bank.azurewebsites.net/api/" + urlParameter; // + controller + parameters

        try {

            URL myurl = new URL(url);
            con = (HttpURLConnection) myurl.openConnection();

            con.setRequestMethod("GET");

            StringBuilder content;

            try (BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()))) {

                String line;
                content = new StringBuilder();

                while ((line = in.readLine()) != null) {
                    content.append(line);
                    content.append(System.lineSeparator());
                }
            }

            System.out.println(content.toString());
            returnContent = content.toString();

        } finally {

            con.disconnect();
        }
        return  returnContent;
    }
}