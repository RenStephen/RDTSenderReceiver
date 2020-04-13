package receiver;

import java.util.Optional;
import utils.Packet;
import utils.RDT;
import utils.UDT;
import utils.packets.ACK;

public class ReceiverRDT extends RDT {

  private UDT socket;

  private int sequenceNumber;

  public ReceiverRDT(UDT socket) {
    this.socket = socket;
    this.sequenceNumber = 0;
  }

  @Override
  public void send(Packet sndpkt) {
    socket.send(sndpkt.getBytes());
  }

  @Override
  public Optional<Packet> receive() {
    while (true) {
      Optional<Packet> o = extract(socket.receive());
      if (o.isPresent()) {
        Packet rcvpkt = o.get();
        if (!corrupt(rcvpkt) && rcvpkt.sequenceNumber == sequenceNumber) {
          Packet ack = new ACK(rcvpkt.sequenceNumber);
          this.sequenceNumber += 1;
          send(ack);
          return Optional.of(rcvpkt);
        } else {
          Packet ack = new ACK(sequenceNumber);
          send(ack);
        }
      }
    }
  }
}
