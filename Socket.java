import java.net.*;
import java.io.*;
import sun.security.util.*;
import javax.swing.JOptionPane;

class Socket {
	private DatagramSocket sock;
	private InetAddress bc;
	private DatagramPacket packet;
	private int port = 9874;
	private boolean isConnected;

	public Socket(String broadcast){
		try{
			bc = InetAddress.getByName(broadcast);
			sock = new DatagramSocket(port);
			isConnected = true;
		}
		catch( Exception e ){
			JOptionPane.showMessageDialog(null, "Error: "+e.getMessage(), "Trabalho Sistema Distribuido", JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
			isConnected = false;
		}
	}

	public void close(){
		try{
			if( isConnected )
				sock.close();
		}
		catch( Exception e ){
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Error: "+e.getMessage(), "Trabalho Sistema Distribuido", JOptionPane.ERROR_MESSAGE);
		}
	}

	public String receive(){
		String ret = "";
		try{
			if( isConnected ){
				byte[] recv = new byte[1024];
				packet = new DatagramPacket(recv, recv.length);
				sock.receive(packet);

				ret = new String(packet.getData()).substring(0, packet.getLength());
			}
		}
		catch( Exception e ){
			ret = "NULL";
			e.printStackTrace();
			close();
			JOptionPane.showMessageDialog(null, "Error: "+e.getMessage(), "Trabalho Sistema Distribuido", JOptionPane.ERROR_MESSAGE);
		}
		return ret;
	}

	private boolean protocol(String p, String nick, InetAddress u){
		try{
			if( isConnected ){
				String msg = p+nick;
				byte[] send = new byte[1024];
				send = msg.getBytes();

				packet = new DatagramPacket(send, send.length, u, port);
				sock.send(packet);
			}
		}
		catch( Exception e ){
			e.printStackTrace();
			close();
			JOptionPane.showMessageDialog(null, "Protocol: "+p+"\nError: "+e.getMessage(),
					"Trabalho Sistema Distribuido", JOptionPane.ERROR_MESSAGE);
			return false;
		}
		return true;
	}

	public boolean logon(String nick){
		return protocol("L", nick, bc);
	}

	public boolean logoff(String nick){
		return protocol("F", nick, bc);
	}

	public boolean online(String nick){
		return protocol("O", nick, packet.getAddress());
	}

	public boolean message(String nick, String msg, InetAddress end){
		return protocol("M", nick+": "+msg, end);
	}

	public boolean verifica(InetAddress end){
		return protocol("V", "", end);
	}

	public boolean retorno(InetAddress end){
		return protocol("R", "", end);
	}

	public boolean connected(){
		return isConnected;
	}

	public DatagramPacket getPacket(){
		return packet;
	}

	public InetAddress getBroadCast(){
		return bc;
	}
}
