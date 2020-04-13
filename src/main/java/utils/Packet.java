package utils;

import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;

public class Packet {

  protected static final byte[] EMPTY = new byte[0];

  private static final String PACKET_PRINT_FORMAT = ""
      + "Sequence Number: %d, Length: %d\n"
      + "Header Length: %d\n"
      + "ACK: %b, SYN: %b, FIN: %b\n"
      + "Check Sum: %s\n"
      + "\n"
      + "Body: %s\n";

  public final int sequenceNumber;
  public final int length;
  public static final int HEADER_LENGTH = 11;
  public final boolean ACK;
  public final boolean SYN;
  public final boolean FIN;
  public final int checksum;
  public final byte[] body;

  public Packet(int sequenceNumber, boolean ACK, boolean SYN, boolean FIN, byte[] body) {
    this.sequenceNumber = sequenceNumber;
    this.length = body.length;
    this.ACK = ACK;
    this.SYN = SYN;
    this.FIN = FIN;
    this.body = body;
    this.checksum = ~RDT.checksum(this.getPseudoPacket()) & 0xFFFF;
  }

  protected Packet(byte[] pkt) {
    ByteBuffer buffer = ByteBuffer.wrap(pkt);

    this.sequenceNumber = buffer.getInt();
    length = buffer.getInt();

    byte options = buffer.get();
    ACK = (options >> 2 & 0x1) == 1;
    SYN = (options >> 1 & 0x1) == 1;
    FIN = (options >> 0 & 0x1) == 1;

    checksum = (((buffer.get() << 8) & 0xFF00) + (buffer.get() & 0xFF));

    body = new byte[length];
    buffer.get(body);
  }

  public byte[] getBytes() {
    ByteArrayOutputStream output = new ByteArrayOutputStream();
    output.writeBytes(getHeader());
    output.write((checksum >> 8) & 0xFF);
    output.write((checksum >> 0) & 0xFF);
    output.writeBytes(body);

    return output.toByteArray();
  }

  protected byte[] getPseudoPacket() {
    ByteArrayOutputStream output = new ByteArrayOutputStream();
    output.writeBytes(getHeader());
    output.writeBytes(body);
    return output.toByteArray();
  }

  private byte[] getHeader() {
    ByteArrayOutputStream output = new ByteArrayOutputStream();

    output.writeBytes(RDT.intToBytes(sequenceNumber));
    output.writeBytes(RDT.intToBytes(length));

    byte b = (byte) ((HEADER_LENGTH << 4) & 0xFF);
    b += (byte) ((ACK ? 1 : 0) << 2 & 0xFF);
    b += (byte) ((SYN ? 1 : 0) << 1 & 0xFF);
    b += (byte) ((FIN ? 1 : 0) << 0 & 0xFF);

    output.write(b);

    return output.toByteArray();
  }

  public String toString() {
    return String.format(PACKET_PRINT_FORMAT,
        sequenceNumber, length, HEADER_LENGTH,
        ACK, SYN, FIN,
        Integer.toBinaryString(checksum),
        new String(body));
  }
}