import javax.swing.JOptionPane;
import java.util.*;

public class ThreadVerificacao implements Runnable {
	private Window wnd;

	public ThreadVerificacao(Window w){
		wnd = w;
	}

	public void run(){
		try{
			while( true ){
				if( wnd.isConnected() ){
					HashMap<String, Usuario> users = wnd.getUsuarioMap();
					Set<String> keys = users.keySet();
					if( users.size() > 1 ){
						for( String n : keys ){
							Usuario u = users.get(n);
							if( !u.getBroadCast() ){
								wnd.getSocket().verifica(u.getAddress());
								u.incContador();

								if( u.getContador() > 5 ){
									wnd.removeUser(u.getName());
								}
							}

						}
					}
				}

				Thread.sleep(50);
			}
		}
		catch( Exception e ){
			JOptionPane.showMessageDialog(null, "Error: "+e.getMessage(), "Trabalho Sistema Distribuido", JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		}
	}
}
