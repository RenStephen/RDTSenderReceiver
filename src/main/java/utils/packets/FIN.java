package utils.packets;

import utils.Packet;

public class FIN extends Packet {

  private static final String ACK_PRINT_FORMAT = ""
      + "Sequence Number: %d, Length: %d\n"
      + "Header Length: %d\n"
      + "FIN: %b\n"
      + "Check Sum: %s\n";

  public FIN(int sequenceNumber) {
    super(sequenceNumber, false, false, true, EMPTY);
  }

  @Override
  public String toString() {
    return String.format(ACK_PRINT_FORMAT,
        sequenceNumber, length,
        HEADER_LENGTH, FIN,
        Integer.toBinaryString(checksum));
  }
}
