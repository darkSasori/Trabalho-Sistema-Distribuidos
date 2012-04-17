import java.net.*;
import java.io.*;

class Usuario {
	private String name;
	private int contador;
	private InetAddress address;
	private boolean broadcast;

	public Usuario(String n, InetAddress a){
		name = n;
		address = a;
		contador = 0;
		broadcast = false;
	}

	public Usuario(String n, InetAddress a, boolean bc){
		name = n;
		address = a;
		contador = 0;
		broadcast = true;
	}

	public int getContador(){
		return contador;
	}

	public void zerarContador(){
		contador = 0;
	}

	public void incContador(){
		contador++;
	}

	public String getName(){
		return name;
	}

	public InetAddress getAddress(){
		return address;
	}

	public boolean getBroadCast(){
		return broadcast;
	}
}
