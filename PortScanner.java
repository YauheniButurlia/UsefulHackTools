import java.net.*;
import java.util.concurrent.*;
import java.util.*;

public class PortScanner {
  public static void main(final String... args) throws InterruptedException,ExecutionException {
  final ExecutorService es = Executors.newFixedThreadPool(20);
  final String ip = args.length == 0 ? "127.0.0.1": args[0];
  final int timeout = args.length == 0 ? 200: Integer.parseInt(args[1]);
  int startPort = args.length == 4 ? Integer.parseInt(args[2]): 1 ;
  int endPort = args.length == 4 ? Integer.parseInt(args[3]): 65535 ;
  final List<Future<Boolean>> futures = new ArrayList<>();
  long start = System.currentTimeMillis();
  for (int port = startPort; port <= endPort; port++) { //65535
    futures.add(portIsOpen(es, ip, port, timeout));
  }
  es.shutdown();
  int openPorts = 0;
  for (final Future<Boolean> f : futures) {
    if (f.get()) {
      openPorts++;
    }
  }
  long end = System.currentTimeMillis();
  System.out.println("There are " + openPorts + " open ports on host " + ip + " (probed with a timeout of " + timeout + "ms)");
  long timeElapsed = end - start; //milliseconds

  long hours = (timeElapsed/1000) / 3600;
  long minutes = (timeElapsed / 1000) % 3600 / 60;
  long seconds = (timeElapsed / 1000) % 60;
  long milliseconds = timeElapsed % 1000;

  System.out.println("Port range: " + startPort + " - " + endPort);
  System.out.println("Time consumed: " + String.valueOf(hours) + " h " + String.valueOf(minutes) + " m " +  String.valueOf(seconds) + " s "  +  String.valueOf(milliseconds) + " ms");
}
  public static Future<Boolean> portIsOpen(final ExecutorService es, final String ip, final int port, final int timeout) {
  return es.submit(new Callable<Boolean>() {
      @Override public Boolean call() {
        try {
          Socket socket = new Socket();
          socket.connect(new InetSocketAddress(ip, port), timeout);
          socket.close();
          System.out.println(port + " is open;");
          return true;
        } catch (Exception ex) {
          return false;
        }
      }
   });
}
}
