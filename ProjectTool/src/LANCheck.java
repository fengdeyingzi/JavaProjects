import java.awt.Desktop;
import java.awt.Desktop.Action;
import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.net.SocketException;
import java.net.URI;
import java.net.UnknownHostException;

public class LANCheck extends Thread
{
  String ip = "";
  int id;
  int port = 2333;

  private static void browse2(String url)
    throws Exception
  {
    Desktop desktop = Desktop.getDesktop();

    if ((Desktop.isDesktopSupported()) && (desktop.isSupported(Desktop.Action.BROWSE)))
    {
      URI uri = new URI(url);

      desktop.browse(uri);
    }
  }
  
  public void checkIP(String headip, int id, int port){
	  try
	    {
		  System.out.println(""+headip+id+":"+port);
	      LANCheck.Client client = new LANCheck.Client(headip + id, port);
	      System.out.println("连接成功 --> "+headip + id + ":" + port);
	      Socket mSocket = client.mSocket;
	      System.out.println("获取out");
	      mSocket.getOutputStream();
	      System.out.println("连接成功:"+headip + id + ":" + port);
	      browse2("http://" + headip + id + ":" + port);
	    }
	    catch (SocketException localSocketException)
	    {
	    }
	    catch (UnknownHostException e)
	    {
	      
	    }
	    catch (IOException e) {
	      
	    }
	    catch (Exception e) {
	      
	    }
  }

  public void run()
  {
    checkIP(ip, id, port);
  }

  public LANCheck(String ip, int id,int port)
  {
    this.id = id;
    this.ip = ip;
    this.port = port;
  }

  public static void main(String[] main)
  {
    for (int id = 0; id < 256; id++) {
      new LANCheck("192.168.43.", id, 2333).start();
      new LANCheck("192.168.0.", id, 2333).start();
      new LANCheck("172.0.0.", id, 7070).start();
    }
  }

  class Client
  {
    Socket mSocket;

    public Client(String host, int port)
      throws SocketException, UnknownHostException, IOException
    {
      this.mSocket = new Socket(host, port);
    }
  }
}