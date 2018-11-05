package service;
import java.io.BufferedReader;
import java.io.InputStreamReader;


public class CommandExecutorService {

    public static final String DEPCHECK_COMMAND = "depcheck graph --dot  --workspace PATH --scheme SCHEME";
    public static final String JDEPS_COMMAND = "jdeps -v -dotoutput dot ";

    public String executeCommand(final String command)
    {
        StringBuffer output = new StringBuffer();

        Process process;
        try{
            process = Runtime.getRuntime().exec(command);
            int a = process.waitFor();
            System.out.println(command);
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

            String line = "";
            while ((line = reader.readLine())!= null) {
                output.append(line + "\n");
                System.out.println(line + "\n");
            }
        }catch (Exception ex)
        {
            ex.printStackTrace();
        }
        return output.toString();
    }
}


