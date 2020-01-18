package commandtool.telaconfiguracao.persistencia;

import java.nio.file.Files;
import java.nio.file.Paths;

import org.prevayler.Prevayler;
import org.prevayler.PrevaylerFactory;

import commandtool.comum.dados.Constantes;
import commandtool.telaconfiguracao.dados.Configuration;

public class PrevaylerUtils 
{
	public static void persistCofiguration(Configuration conf) throws Exception
	{
		createConfigurationPersistence().execute(new ConfigurationPersistence(conf));
	}
	
	public static Configuration recoverConfiguration() throws Exception
	{
		return(createConfigurationPersistence().prevalentSystem());
	}
	
	private static Prevayler<Configuration> createConfigurationPersistence() throws Exception
	{
		if(!Files.exists(Paths.get(Constantes.LOCAL_PREV_BASE)))
		{
			Files.createDirectory(Paths.get(Constantes.LOCAL_PREV_BASE));
		}
		
		PrevaylerFactory<Configuration> factory = new PrevaylerFactory<Configuration>();
		factory.configurePrevalenceDirectory(Constantes.LOCAL_PREV_BASE);
		factory.configurePrevalentSystem(new Configuration());
		
		return(factory.create());
	}

}
