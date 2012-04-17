import javax.swing.JFrame;
import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;

public class Main{
	public static void main(String args[]){
		Window wnd = new Window();

		ExecutorService e = Executors.newFixedThreadPool(2);
		e.execute(new ThreadSocket(wnd));
		e.execute(new ThreadVerificacao(wnd));

		wnd.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
		wnd.setSize(680,520);
		wnd.setVisible(true);

		e.shutdown();
	}
}
