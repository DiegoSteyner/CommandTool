package commandtool.telaconfiguracao.persistencia;

import java.io.Serializable;
import java.util.Date;

import org.prevayler.Transaction;

import commandtool.telaconfiguracao.dados.Configuration;

public class ConfigurationPersistence implements Transaction<Configuration>, Serializable
{
	private static final long serialVersionUID = 2L;
	protected Configuration configuration = null;
	
	public ConfigurationPersistence(Configuration configuration) 
	{
		this.configuration = configuration;
		executeOn(this.configuration, new Date());
	}

	@Override
	public void executeOn(Configuration configuration, Date executionTime) 
	{
		configuration.setMediaInfo(this.configuration.getMediaInfo());
		configuration.setMkvToolNix(this.configuration.getMkvToolNix());
		configuration.setHandBrake(this.configuration.getHandBrake());
		configuration.setDirProgramas(this.configuration.getDirProgramas());
	}
}
