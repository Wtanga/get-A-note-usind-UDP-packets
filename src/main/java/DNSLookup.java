import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class DNSLookup {
    private static final String DNS_SERVER_ADDRESS = "8.8.8.8";
    private static final int DNS_SERVER_PORT = 53;

    public static void main(String[] args) throws IOException {
        String domain = "www.nalog.gov.ru";
        InetAddress ipAddress = InetAddress.getByName(DNS_SERVER_ADDRESS);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);

        dos.writeShort(0x1234);
        dos.writeShort(0x0100);
        dos.writeShort(0x0001);
        dos.writeShort(0x0000);
        dos.writeShort(0x0000);
        dos.writeShort(0x0000);

        String[] domainParts = domain.split("\\.");
        for (int i = 0; i < domainParts.length; i++) {
            byte[] domainBytes = domainParts[i].getBytes("UTF-8");
            dos.writeByte(domainBytes.length);
            dos.write(domainBytes);
        }

        dos.writeByte(0x00);
        dos.writeShort(0x0001);
        dos.writeShort(0x0001);

        byte[] dnsFrame = baos.toByteArray();

        DatagramSocket socket = new DatagramSocket();
        DatagramPacket dnsReqPacket = new DatagramPacket(dnsFrame, dnsFrame.length, ipAddress, DNS_SERVER_PORT);
        socket.send(dnsReqPacket);

        byte[] buf = new byte[1024];
        DatagramPacket packet = new DatagramPacket(buf, buf.length);
        socket.receive(packet);

        DataInputStream din = new DataInputStream(new ByteArrayInputStream(buf));

        din.readLong();
        din.readInt();

        int recLen = 0;
        while ((recLen = din.readByte()) > 0) {
            byte[] record = new byte[recLen];

            for (int i = 0; i < recLen; i++) {
                record[i] = din.readByte();
            }
        }

        din.readLong();
        din.readShort();
        din.readInt();
        short addrLen = din.readShort();

        System.out.println("Domain name: " + domain);
        System.out.print("Address: ");
        for (int i = 0; i < addrLen; i++) {
            System.out.print(String.format("%d", (din.readByte() & 0xFF)) + ".");
        }
    }

}