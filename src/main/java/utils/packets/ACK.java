package utils.packets;

import utils.Packet;

public class ACK extends Packet {

  private static final String ACK_PRINT_FORMAT = ""
      + "Sequence Number: %d, Length: %d\n"
      + "Header Length: %d\n"
      + "ACK: %b\n"
      + "Check Sum: %s\n";

  public ACK(int sequenceNumber) {
    super(sequenceNumber, true, false, false, EMPTY);
  }

  @Override
  public String toString() {
    return String.format(ACK_PRINT_FORMAT,
        sequenceNumber, length,
        HEADER_LENGTH, ACK,
        Integer.toBinaryString(checksum));
  }
}
