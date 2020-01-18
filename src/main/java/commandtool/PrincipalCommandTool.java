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

//			extrairArquivos();
			
			//https://code.makery.ch/blog/javafx-dialogs-official/
			
			// dialog, muita boa explicaÃ§Ã£o.
			//https://stackoverflow.com/questions/51387453/javafx-how-to-implement-a-busy-and-wait-dialog-routine
			
			// outros legais
			//https://www.dummies.com/programming/java/javafx-binding-properties/
			
			// muito legal sobre os componentes
			//https://o7planning.org/en/11009/javafx
			
			// interessante sobre thread
			//http://aprendendo-javafx.blogspot.com/2014/08/threads-e-javafx.html
			//https://docs.oracle.com/javase/8/javafx/interoperability-tutorial/concurrency.htm
			
			// criar código quebrar os nomes
			// Criar lógica para setar valores no json em lote
			
//			testeMkvToolNix();
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
	
	public static void extrairArquivos()
	{
		//"C:\Program Files\7-Zip\7z.exe" x "E:\Filmes\filme - a mafia no divã\MNODIV.BLU.1080p.part1.rar" -y -o"E:\Filmes\filme - a mafia no divã"
		final String sevenZip = "C:\\Program Files\\7-Zip\\7z.exe";
		final String comandoExtrairAqui = " x \"{0}\"";
		final String comandoParaDiretorio = " -o\"{0}\"";
		final String comandoSimParaTodos = " -y";
		final String diretorioInicial = "E:\\Filmes";
		
		try 
		{
			LinkedList<Path> arquivos = Files.walk(Paths.get(diretorioInicial)).parallel().filter(p -> p.toString().contains("part1")).collect(Collectors.toCollection(LinkedList::new));
			
			arquivos.forEach(p -> {
				
				StringBuilder str = new StringBuilder();
				
				str.append("\"").append(sevenZip).append("\"");
				str.append(MessageFormat.format(comandoExtrairAqui, p.toString()));
				str.append(MessageFormat.format(comandoParaDiretorio, p.getParent().toString()));
				str.append(comandoSimParaTodos);
				
				System.out.println(str.toString());
				
				try 
				{
//					RuntimeUtils.execSystemCommand(str.toString(), new PrintOutputDTO(System.out), "UTF-8");
//					RuntimeUtils.execProgramByCommand("\""+sevenZip+"\"", str.toString(), "", new PrintOutputDTO(System.out), "UTF-8");
				}
				catch (Exception e) 
				{
					e.printStackTrace();
				}
				
//				try(BufferedReader buff = RuntimeUtils.execSystemCommandCmd(str.toString(), "UTF-8"))
//				{
//					String line = "";
//					
//					while ((line = buff.readLine()) != null)
//					{
//						System.out.println(line);
//					}
//					
//					System.out.println("saiu");
//				} 
//				catch (Exception e) 
//				{
//					e.printStackTrace();
//				}
				
			}) ;
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
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
