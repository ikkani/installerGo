package installerGov0;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Button;

public class SelectForDesinstall {

	protected Shell shlDesinstalar;
	private String[] arrayApps;

	public SelectForDesinstall(String listaApps) {
		arrayApps = listaApps.split("\n");
		
	}

	public void combo() {
		Combo combo = new Combo(shlDesinstalar, SWT.APPLICATION_MODAL);
		combo.setToolTipText("Selecciona el paquete a desinstalar");
		combo.setBounds(37, 37, 210, 23);
		combo.setText("Seleeciona el paquete a desinstalar");
		combo.select(0);
		
		Button btnNewButton = new Button(shlDesinstalar, SWT.NONE);
		btnNewButton.setBounds(97, 74, 90, 25);
		btnNewButton.setText("OK");
		for (int i = 0; i < arrayApps.length; i++)
			combo.add(arrayApps[i]);
		
		btnNewButton.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				// TODO Auto-generated method stub
				try {MainClass.staticStr = combo.getItem(combo.getSelectionIndex());
				}catch(Exception e1) {}
				shlDesinstalar.close();
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
				
				
	}
	
	public SelectForDesinstall() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * Launch the application.
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			SelectForDesinstall window = new SelectForDesinstall();
			window.open();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Open the window.
	 */
	public void open() {
		MainClass.shlInstallergoV.setEnabled(false);
		Display display = Display.getDefault();
		createContents();
		shlDesinstalar.open();
		shlDesinstalar.layout();
		while (!shlDesinstalar.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}

	/**
	 * Create contents of the window.
	 */
	protected void createContents() {
		shlDesinstalar = new Shell();
		shlDesinstalar.setSize(307, 175);
		shlDesinstalar.setText("Desinstalar");
		combo();
		
		shlDesinstalar.addListener(SWT.Close, new Listener()
	    {
	        public void handleEvent(Event event)
	        {
	    		MainClass.shlInstallergoV.setEnabled(true);
	        }
	    });

	}
}
