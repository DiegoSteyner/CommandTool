package commandtool;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.MessageFormat;
import java.util.LinkedList;
import java.util.stream.Collectors;

public class PrincipalCommandTool {

	public static void main(String[] args) 
	{
		try 
		{
			TelaPrincipal.launch(TelaPrincipal.class);
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
	}
	
	protected static void shutdownHook() 
	{
		Runtime.getRuntime().addShutdownHook(new Thread() {

		    @Override
		    public void run() {
		    	System.out.println("Veio aqui no desligamento");
		    	
		    	try 
		    	{
//						Thread.sleep(5000);
				} 
		    	catch (Exception e) 
		    	{
					e.printStackTrace();
				}
		    }

		});
	}
	
	public static void testePid() 
	{
		try 
		{
			java.lang.management.RuntimeMXBean runtime = java.lang.management.ManagementFactory.getRuntimeMXBean();
			java.lang.reflect.Field jvm = runtime.getClass().getDeclaredField("jvm");
			jvm.setAccessible(true);
			sun.management.VMManagement mgmt = (sun.management.VMManagement) jvm.get(runtime);
			java.lang.reflect.Method pid_method = mgmt.getClass().getDeclaredMethod("getProcessId");
			pid_method.setAccessible(true);

			int pid = (Integer) pid_method.invoke(mgmt);

			System.out.println("PID " + pid);
		} 
		catch (Exception e) 
		{

		}
	}
}
