package commandtool.comum.utils;

import java.io.File;
import java.io.FileInputStream;
import java.text.MessageFormat;
import java.util.Locale;
import java.util.Properties;
import java.util.SortedSet;
import java.util.TreeSet;

import commandtool.comum.dados.Constantes;
import commandtool.telaconfiguracao.dados.Configuration;
import commandtool.telaconfiguracao.dados.enums.AppsEnum;
import commandtool.telaconfiguracao.persistencia.PrevaylerUtils;
import jutil.utils.StringUtils;

public class CommandToolsUtils 
{
	public static String getBoundleValue(String str, Object... replace)
	{
		return(MessageFormat.format(Constantes.pt.getString(str), replace));
	}
	
	public static String[] getExtensoes()
	{
		try 
		{
			Properties pgeneros = new Properties();
	        pgeneros.load(new FileInputStream(new File(Constantes.LOCAL_PROPERTS_BASE, Constantes.PROPERTIES_GERAL)));
	        
	        return(pgeneros.get("ExtensoesAceitas").toString().split(","));
		} 
		catch (Exception e) 
		{
			return(null);
		}
	}
	
	public static boolean configurationIsValid(AppsEnum executavel)
	{
		try 
		{
			Configuration conf = PrevaylerUtils.recoverConfiguration();
			
			if(conf.getDirProgramas() != null && conf.getDirProgramas().exists())
			{
				switch (executavel) 
				{
					case MEDIA_INFO:
					{
						return(conf.getMediaInfo() != null && conf.getMediaInfo().exists());
					}
					case MKV_TOOLNIX:
					{
						return(conf.getMkvToolNix() != null && conf.getMkvToolNix().exists());
					}
	
					default:
						return(false);
				}
			}
			else if(conf.getDirProgramas() == null)
			{
				return(false);
			}
			else if(!conf.getDirProgramas().exists())
			{
				return(false);
			}
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		
		return(false);
	}
	
	public static SortedSet<String> getIdiomarDisponiveis() throws Exception 
	{
		SortedSet<String> linguagens = new TreeSet<String>();
		
        String[] languages = Locale.getISOLanguages();
        
        for (int i = 0; i < languages.length; i++)
        {
            Locale loc = new Locale(languages[i]);
            linguagens.add(StringUtils.capitalizeToCamelCase(loc.getDisplayLanguage()));
        }
		return linguagens;
	}
}
