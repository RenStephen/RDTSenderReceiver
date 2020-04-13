package utils;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import org.apache.log4j.Logger;

public class UDT {

  private static final int RETRIES = 10;

  private static final int TIMEOUT = 10000;

  private static final int BUFFER = 2048;

  private int targetPort;

  private DatagramSocket socket;

  private InetAddress targetAddress;

  private static final Logger LOGGER = Logger.getLogger(UDT.class.getName());

  public UDT(int hostPort, String targetAddress, int targetPort) throws IOException {
    socket = new DatagramSocket(hostPort);
    socket.setSoTimeout(TIMEOUT);
    this.targetAddress = InetAddress.getByName(targetAddress);
    this.targetPort = targetPort;
  }

  public void send(byte[] msg) {
    DatagramPacket sndpkt = new DatagramPacket(msg, msg.length, targetAddress, targetPort);
    try {
      socket.send(sndpkt);
    } catch (IOException e) {
      LOGGER.info("Error while sending");
    }
  }

  public byte[] receive() {
    DatagramPacket rcvpkt = new DatagramPacket(new byte[BUFFER], BUFFER);
    for (int i = 0; i < RETRIES; i++) {
      try {
        socket.receive(rcvpkt);
        break;
      } catch (IOException e) {
        LOGGER.info(e.getMessage());
      }
    }
    return rcvpkt.getData();
  }
}