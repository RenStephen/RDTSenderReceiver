package sender;

import java.io.IOException;
import utils.Packet;
import utils.UDT;
import utils.packets.FIN;

public class Sender {

  public static void main(String[] args) throws IOException {

    UDT server = new UDT(8080, "localhost", 8081);

    SenderRDT sender = new SenderRDT(server);

    FileReader reader = new FileReader("Beethoven.png", sender);

    int sequenceNumber = reader.sendFile();
    Packet fin = new FIN(sequenceNumber);
    sender.send(fin);

  }
}

