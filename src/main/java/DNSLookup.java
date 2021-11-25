import java.net.InetAddress;
import java.net.UnknownHostException;

public class DNSLookup {
    public static void main(String args[]) {
        try {
            InetAddress host;
            host = InetAddress.getByName("www.nalog.gov.ru");
            displayRequest(host);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    private static void displayRequest(InetAddress host) {
        System.out.println("Host name:'" + host.getHostName()
                + "' has address: " + host.getHostAddress());
    }
}  