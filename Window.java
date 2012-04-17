import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.FlowLayout;
import java.awt.Container;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.List;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JTextArea;
import javax.swing.JButton;
import javax.swing.SwingConstants;
import java.io.*;
import java.util.*;
import java.net.*;

public class Window extends JFrame implements ActionListener{
	private JTextField nick, msg, bc;
	private JButton logon, logoff, send;
	private JTextArea lMsg;
	private List list;
	private Socket sock;
	private boolean isLogged;
	private HashMap<String, Usuario> user;

	public Window(){
		super("Trablho Sistema Distribuidos");
		sock = null;
		isLogged = false;
		user = new HashMap<String, Usuario>(200);

		setLayout(new BorderLayout(3,3));
		createPart1();
		createPart3();
		createPart2();

		enabled("Logoff");
	}

	public boolean isConnected(){
		return isLogged;
	}

	private void createPart1(){
		JPanel p1 = new JPanel();
		p1.setLayout(new GridLayout(2,1));

		JPanel top = new JPanel();
		
		FlowLayout l1 = new FlowLayout();
		FlowLayout l2 = new FlowLayout();
		l1.setAlignment(FlowLayout.RIGHT);
		l2.setAlignment(FlowLayout.RIGHT);

		top.setLayout(l1);

		top.add(new JLabel("Nick:", SwingConstants.RIGHT));

		nick = new JTextField(20);
		top.add(nick);

		top.add(new JLabel("Broadcast:", SwingConstants.RIGHT));

		bc = new JTextField(20);
		bc.setText("192.168.84.255");
		top.add(bc);

		JPanel btn = new JPanel();
		btn.setLayout(l2);

		logon = new JButton("Logon");
		logon.addActionListener(this);
		btn.add(logon);
		
		logoff = new JButton("Logoff");
		logoff.addActionListener(this);
		btn.add(logoff);

		p1.add(top);
		p1.add(btn);

		add(p1, BorderLayout.NORTH);
	}

	private void createPart2(){
		JPanel p2 = new JPanel();
		p2.setLayout(new BorderLayout(2,1));

		lMsg = new JTextArea("");
		lMsg.setEnabled(false);
		p2.add(lMsg, BorderLayout.CENTER);

		JPanel p32 = new JPanel();
		p32.setLayout(new FlowLayout());

		msg = new JTextField(40);
		p32.add(msg);

		send = new JButton("Enviar");
		send.addActionListener(this);
		p32.add(send);

		p2.add(p32, BorderLayout.SOUTH);
		add(p2, BorderLayout.WEST);
	}

	public void createPart3(){
		JPanel p3 = new JPanel();
		p3.setLayout(new BorderLayout(2,1));

		p3.add(new JLabel("Usuarios:"), BorderLayout.NORTH);
		list = new List();
		p3.add(list, BorderLayout.CENTER);

		add(p3, BorderLayout.EAST);
	}

	private void enabled(String ac){
		if( ac.equals("Logon") ){
			logoff.setEnabled(true);
			send.setEnabled(true);
			logon.setEnabled(false);
			nick.setEnabled(false);
			bc.setEnabled(false);
			msg.setEnabled(true);
			isLogged = true;
			list.add("TODOS");
			user.put("TODOS", new Usuario("TODOS", sock.getBroadCast(), true));
			list.select(0);
		}
		else if( ac.equals("Logoff") ){
			logoff.setEnabled(false);
			msg.setEnabled(false);
			send.setEnabled(false);
			logon.setEnabled(true);
			nick.setEnabled(true);
			bc.setEnabled(true);
			lMsg.setText("");
			isLogged = false;
			list.clear();
			user.clear();
		}
	}

	public void recv(){
		String ret = sock.receive();
		if( ret.equals("NULL") ){
			enabled("Logoff");
			return;
		}
		if( ret.equals("") )
			return;

		String p = ret.substring(0,1);
		String name = ret.substring(1);

		if( p.equals("L") ){
			if( !name.equals(nick.getText()) ){
				list.add(name);
				user.put(name, new Usuario(name, sock.getPacket().getAddress()));
				sock.online(nick.getText());
			}
		}
		else if( p.equals("F") ){
			if( !name.equals(nick.getText()) ){
				list.remove(name);
				user.remove(name);
			}
		}
		else if( p.equals("O") ){
			list.add(name);
			user.put(name, new Usuario(name, sock.getPacket().getAddress()));
		}
		else if( p.equals("M") ){
			String m = lMsg.getText() + name + "\n";
			lMsg.setText(m);
		}
		else if( p.equals("V") ){
			sock.retorno(sock.getPacket().getAddress());
		}
		else if( p.equals("R") ){
			Set<String> keys = user.keySet();
			if( user.size() > 1 ){
				for( String n : keys ){
					Usuario u = user.get(n);
					if( u.getAddress().equals(sock.getPacket().getAddress()) )
						u.zerarContador();
				}
			}
		}
	}

	public void actionPerformed(ActionEvent e){
		String cmd = e.getActionCommand();

		if( e.getSource() == logon ){
			if( !isLogged ){
				if( sock != null )
					sock.close();

				sock = new Socket(bc.getText());
				if( sock.connected() )
					if( sock.logon(nick.getText()) ){
						enabled("Logon");
					}
			}
		}
		else if( e.getSource() == logoff ){
			if( isLogged ){
				if( sock.logoff(nick.getText()) ){
					enabled("Logoff");
				}
			}
		}
		else if( e.getSource() == send ){
			if( isLogged ){
				String u = list.getSelectedItem();
				String n = nick.getText();

				if( !u.equals("TODOS") ){
					n += " para "+u;
					String m = lMsg.getText() + n + ": " + msg.getText() + "\n";
					lMsg.setText(m);
				}

				if( sock.message(n, msg.getText(), user.get(u).getAddress()) ){
					msg.setText("");
				}
			}
		}
	}

	public HashMap<String, Usuario> getUsuarioMap(){
		return user;
	}

	public Socket getSocket(){
		return sock;
	}

	public void removeUser(String name){
		list.remove(name);
		user.remove(name);
	}
}
