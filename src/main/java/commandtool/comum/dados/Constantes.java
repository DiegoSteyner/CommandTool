package commandtool.comum.dados;

import java.io.File;
import java.nio.file.Paths;
import java.util.Locale;
import java.util.ResourceBundle;

public interface Constantes 
{
	String PROPERTIES_GENEROS 	= "Generos.properties";
	String PROPERTIES_GERAL 	= "Geral.properties";
	
	String LOCAL_EXECUTADO		= Paths.get("").toAbsolutePath().toString();
	String LOCAL_PREV_BASE		= LOCAL_EXECUTADO.concat(File.separator).concat("PrevBase");
	String LOCAL_JSON_BASE		= LOCAL_EXECUTADO.concat(File.separator).concat("JsonBase");
	String LOCAL_IMAGE_BASE		= LOCAL_EXECUTADO.concat(File.separator).concat("ImageBase");
	String LOCAL_PROPERTS_BASE	= LOCAL_EXECUTADO.concat(File.separator).concat("PropBase");
	
	String EXTENSAO_PONTO_SRT 		= ".srt";
	String EXTENSAO_PONTO_JSON 		= ".json";
	String EXTENSAO_PONTO_MKV 		= ".mkv";
	String EXTENSAO_PONTO_JPG 		= ".jpg";
	
	String EXTENSAO_ASTERISCO_JSON 	= "*.json";
	
	String TASK_EXTRACT_ALL_INFO 	= "ExtrairInformacoes";
	String TASK_MKV_MERGE_PROCESS 	= "ProcessMkvMerge";
	
	String FILM_LIST_JSON_KEY 		= "filmList";
	String MEU_COMPUTADOR 			= "Meu Computador";

	String TABLE_INDEX				= "TABLE_INDEX";
	
	ResourceBundle pt = ResourceBundle.getBundle("Languages/strings", new Locale("pt", "BR"));
}
