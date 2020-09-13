package installerGov0;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;

import org.eclipse.swt.widgets.Text;
import org.eclipse.wb.swt.SWTResourceManager;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.GridData;

public class MainClass {

	protected static Shell shlInstallergoV;
	private Text text;
	static String staticStr;
	/**
	 * Launch the application.
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			MainClass window = new MainClass();
			window.open();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Open the window.
	 */
	public void open() {
		Display display = Display.getDefault();
		createContents();
		shlInstallergoV.open();
		shlInstallergoV.layout();
		while (!shlInstallergoV.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}

	/**
	 * Create contents of the window.
	 */
	protected void createContents() {
		shlInstallergoV = new Shell();
		shlInstallergoV.setImage(SWTResourceManager.getImage(MainClass.class, "/installerGov0/intgo.ico"));
		shlInstallergoV.setSize(680, 750);
		shlInstallergoV.setText("InstallerGo v0.1");
		GridLayout gl_shlInstallergoV = new GridLayout(5, false);
		gl_shlInstallergoV.marginTop = 100;
		gl_shlInstallergoV.marginWidth = 100;
		gl_shlInstallergoV.marginBottom = 110;
		gl_shlInstallergoV.marginHeight = 110;
		gl_shlInstallergoV.marginLeft = 110;
		gl_shlInstallergoV.marginRight = 100;
		shlInstallergoV.setLayout(gl_shlInstallergoV);
		
		Button btnInstalarApk = new Button(shlInstallergoV, SWT.NONE);
		btnInstalarApk.setText("Instalar apk");
		
		
		btnInstalarApk.addSelectionListener(new SelectionListener() {
			 
			   @Override
			   public void widgetSelected(SelectionEvent arg0) {
				   FileDialog filedialog = new FileDialog(shlInstallergoV);
				   String[] extension = {"*.apk"};
				   filedialog.setFilterExtensions(extension);
				   filedialog.open();
				   String nombreArchivo = filedialog.getFileName();
				   if (nombreArchivo.contentEquals("")) { //NO HAY SELECCION
					   text.append("No file selected\n");
					   return;
				   }
				   if (!nombreArchivo.substring(nombreArchivo.length()-4, nombreArchivo.length()).equals(".apk")) {	//COMPROBAR SI ES .APK
					   MessageBox dialog =
					       new MessageBox(shlInstallergoV, SWT.ICON_ERROR| SWT.OK);
					   dialog.setText("Error");
					   dialog.setMessage("El archivo seleccionado no tiene el formato .apk");
					   dialog.open();
					   return;
				   }
				   CmdController cm = new CmdController();
				   String ordenCMD = cm.ejecutar("platform-tools\\adb install \"" + filedialog.getFilterPath() + "\\" + nombreArchivo + "\"");
				   System.out.println(ordenCMD);
				   if (!ordenCMD.contains("Success")) {
					   text.append("ERROR: ¿La aplicación ya estaba instalada?\n");
					   return;
				   }
				   text.append(ordenCMD.replace("Performing Streamed Install\nSuccess", "Instalación realizada con éxito") + "\n");
				   
			   }
			 
			   @Override
			   public void widgetDefaultSelected(SelectionEvent arg0) {
			   }
			 
			});
		
		Label lblNewLabel = new Label(shlInstallergoV, SWT.CENTER);
		lblNewLabel.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 3));
		lblNewLabel.setImage(SWTResourceManager.getImage(MainClass.class, "/installerGov0/oculus-go-3.ico"));
		
		Button btnResetearAdb = new Button(shlInstallergoV, SWT.NONE);
		btnResetearAdb.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				CmdController cm = new CmdController();
				cm.ejecutar("platform-tools\\adb kill-server");
				cm.ejecutar("platform-tools\\adb start-server");
				text.append("Reset completado\n");
			}
		});
		btnResetearAdb.setText("Resetear ADB");
		
		Button btnDesinstalarApk = new Button(shlInstallergoV, SWT.NONE);
		btnDesinstalarApk.setText("Desinstalar apk");
		
		btnDesinstalarApk.addSelectionListener(new SelectionListener() {
			 
			   @Override
			   public void widgetSelected(SelectionEvent arg0) {
				   CmdController cm = new CmdController();
				   String listaApps = cm.ejecutar("platform-tools\\adb shell pm list packages -3");
				   if (listaApps == null)
					   return;
			   	   //listaApps = "package:com.oculus.rooms\npackage:com.oculus.cinema";
				   SelectForDesinstall s = new SelectForDesinstall(listaApps);
				   s.open();
				   if (staticStr == null)
					   return;
				   MessageBox dialog =
					       new MessageBox(shlInstallergoV, SWT.ICON_WARNING| SWT.YES | SWT.NO);
					   dialog.setText("Desinstalar");
					   String sinPaquete = staticStr.replace("package:", "");
					   dialog.setMessage("¿Estás seguro de que quieres desinstalar el paquete " + sinPaquete);
					   int respuesta = dialog.open();
					   if (respuesta == SWT.YES) {
						   String traduccion = cm.ejecutar("platform-tools\\adb uninstall " + sinPaquete);
						   traduccion = traduccion.replace("Success", "Exito en la desinstalación");
						   text.append(traduccion + "\n");
					   }else return;
			   }
			 
			   @Override
			   public void widgetDefaultSelected(SelectionEvent arg0) {
			   }
			 
			});
		
		Button btnListarDispConectados = new Button(shlInstallergoV, SWT.NONE);
		btnListarDispConectados.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				CmdController cm = new CmdController();
				String textoConsola = cm.ejecutar("platform-tools\\adb devices");
				textoConsola = textoConsola.replace("List of devices attached", "Lista de dispositivos conectados:");
				text.append(textoConsola);
			}
		});
		btnListarDispConectados.setText("Listar disp conectados");
		
		Button btnVerAplicacionesInstaladas = new Button(shlInstallergoV, SWT.NONE);
		btnVerAplicacionesInstaladas.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				CmdController cm = new CmdController();
				String listaApps = cm.ejecutar("platform-tools\\adb shell pm list packages -3");
				text.append("Los siguientes paquetes están instalados en el dispositivo:\n\n" + listaApps + "\n");
			}
		});
		btnVerAplicacionesInstaladas.setText("Ver aplicaciones instaladas");
		
		Button btnAcercaDe = new Button(shlInstallergoV, SWT.NONE);
		btnAcercaDe.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				MessageBox dialog =
					       new MessageBox(shlInstallergoV, SWT.ICON_INFORMATION| SWT.OK | SWT.CENTER);
					   dialog.setText("Acerca de");
					   dialog.setMessage("InstallerGo v0.1\nDesarrollado por Iñaki Urrutia\nhttps://github.com/ikkani");
					   // open dialog and await user selection
					   dialog.open();
					   return;
			}
		});
		btnAcercaDe.setText("Acerca de...");
		new Label(shlInstallergoV, SWT.NONE);
		new Label(shlInstallergoV, SWT.NONE);
		new Label(shlInstallergoV, SWT.NONE);
		
		Label lblSalida = new Label(shlInstallergoV, SWT.NONE);
		lblSalida.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 3, 1));
		lblSalida.setFont(SWTResourceManager.getFont("Segoe UI", 12, SWT.BOLD));
		lblSalida.setAlignment(SWT.CENTER);
		lblSalida.setText("Salida:");
		new Label(shlInstallergoV, SWT.NONE);
		
		text = new Text(shlInstallergoV, SWT.BORDER | SWT.READ_ONLY | SWT.WRAP | SWT.H_SCROLL | SWT.V_SCROLL | SWT.CANCEL);
		text.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 3, 1));
		text.setForeground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
		new Label(shlInstallergoV, SWT.NONE);
		
		

	}
}




