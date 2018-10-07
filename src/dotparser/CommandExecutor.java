package dotparser;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by Nils-Pc on 06.08.2018.
 */
public class CommandExecutor {
    public String executeCommand(final String command)
    {

        File file = new File(".");
        try {
            System.out.println(file.getCanonicalPath());
        } catch (IOException e) {
            e.printStackTrace();
        }
        StringBuffer output = new StringBuffer();

        Process process;
        long starttime = System.currentTimeMillis();
        try{
            process = Runtime.getRuntime().exec(command);
            int a = process.waitFor();
            System.out.println(a);
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

            String line = "";
            while ((line = reader.readLine())!= null) {
                output.append(line + "\n");
            }
            System.out.println("Zeit:" + (System.currentTimeMillis() - starttime));
        }catch (Exception ex)
        {
            ex.printStackTrace();
        }
        return output.toString();
    }
}


