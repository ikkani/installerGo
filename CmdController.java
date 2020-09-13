package installerGov0;

import java.io.*;

public class CmdController {
 
		public CmdController() {}
	
		String ejecutar(String comando){
			
			String line="";
			String total = "";
			
            try {
                Runtime rt = Runtime.getRuntime();
                Process pr = rt.exec(comando);
 
                BufferedReader input = new BufferedReader(new InputStreamReader(pr.getInputStream()));
 
                while((line = input.readLine()) != null) {
                    total += (line + "\n");
                }
 
                pr.waitFor();

            } catch(Exception e) {
                System.out.println(e.toString());
                e.printStackTrace();
            }
            return total;
        }
}