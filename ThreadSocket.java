import javax.swing.JOptionPane;

public class ThreadSocket implements Runnable {
	private Window wnd;

	public ThreadSocket(Window w){
		wnd = w;
	}

	public void run(){
		try{
			while( true ){
				if( wnd.isConnected() )
					wnd.recv();

				Thread.sleep(10);
			}
		}
		catch( Exception e ){
			JOptionPane.showMessageDialog(null, "Error: "+e.getMessage(), "Trabalho Sistema Distribuido", JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		}
	}
}
