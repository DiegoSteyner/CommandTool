package commandtool.telafilmejson;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import commandtool.comum.dados.Constantes;
import commandtool.telafilmejson.dados.dto.FilmeDTO;
import commandtool.telafilmejson.dados.dto.ListaDeFilmesDTO;
import commandtool.telafilmejson.dados.enums.PropriedadesFilme;
import javafx.beans.property.SimpleBooleanProperty;
import jutil.utils.FileUtils;
import mediainfo.data.dto.AudioDTO;
import mediainfo.data.dto.LegendaDTO;
import mediainfo.data.dto.MediaInfoDTO;
import mediainfo.data.dto.VideoDTO;
import mediainfo.utils.MediaInfoUtils;

public final class FilmeUtils 
{
	private static final String PATTERN_INICIO_NOME = "\\d{5}\\.\\d{2}.*?-";
	private static final String PATTERN_ID_FILME 	= "^\\d{5}\\.\\d{2}\\s";
	
	private static final int AUDIO_LANGUAGE 		= 1;
	private static final int AUDIO_DUAL_AUDIO 		= 2;
	private static final int AUDIO_COMERCIAL_FORMAT = 3;
	private static final int LEGENDA_IS_PRESENT 	= 4;
	private static final int LEGENDA_LANGUAGE 		= 5;
	private static final int VIDEO_RESOLUTION 		= 6;
	private static final int AUDIO_QTD 				= 7;
	private static final int LEGENDA_QTD 			= 8;

	private FilmeUtils() 
	{
		throw new UnsupportedOperationException("Esta classe utilitaria não pode ser instanciada");
	}
	
	public static FilmeDTO createFilmDTO(MediaInfoDTO info, String localArmazenamento) throws NumberFormatException, Exception
	{
		FilmeDTO filme = new FilmeDTO();
		
		String nomeFilme = info.getInformacoesGerais().getSimpleFileName();
		
		filme.setNomeFilme(nomeFilme.replaceAll(PATTERN_INICIO_NOME, "").trim());
		filme.setIdFilme(extractFilmId(nomeFilme));
		filme.setAssistido(false);
		filme.setGenero("");
		filme.setSinopse("");
		filme.setNomeCapa(filme.getNomeFilme().concat(Constantes.EXTENSAO_PONTO_JPG));
		filme.setDuracao(MediaInfoUtils.formatDuration(info.getInformacoesGerais().getDuration()));
		filme.setTamanho(FileUtils.sizeToHumanReadable(Long.parseLong(info.getInformacoesGerais().getFileSize()), 2));
		filme.setLocalArmazenamento(localArmazenamento);
		filme.setTemCapa(new File(Constantes.LOCAL_IMAGE_BASE, filme.getNomeCapa()).exists());
		
		filme.getPropriedades().put(PropriedadesFilme.IDIOMA, getValue(AUDIO_LANGUAGE, info));
		filme.getPropriedades().put(PropriedadesFilme.ANO_LANCAMENTO, "");
		filme.getPropriedades().put(PropriedadesFilme.DUAL_AUDIO, getValue(AUDIO_DUAL_AUDIO, info));
		filme.getPropriedades().put(PropriedadesFilme.EXTENSAO, info.getInformacoesGerais().getFileExtension());
		filme.getPropriedades().put(PropriedadesFilme.FORMATO_ARQUIVO, info.getInformacoesGerais().getFormat());
		filme.getPropriedades().put(PropriedadesFilme.FORMATO_AUDIO, getValue(AUDIO_COMERCIAL_FORMAT, info));
		filme.getPropriedades().put(PropriedadesFilme.LEGENDAS, getValue(LEGENDA_IS_PRESENT, info));
		filme.getPropriedades().put(PropriedadesFilme.IDIOMA_LEGENDAS, getValue(LEGENDA_LANGUAGE, info));
		filme.getPropriedades().put(PropriedadesFilme.RESOLUCAO, getValue(VIDEO_RESOLUTION, info));
		filme.getPropriedades().put(PropriedadesFilme.QUANTIDADE_AUDIOS, getValue(AUDIO_QTD, info));
		filme.getPropriedades().put(PropriedadesFilme.QUANTIDADE_LEGENDAS, getValue(LEGENDA_QTD, info));
		filme.getPropriedades().put(PropriedadesFilme.TAMANHO_BYTES, info.getInformacoesGerais().getFileSize());
		
		return(filme);
	}
	
	private static String getValue(int part, MediaInfoDTO info) throws Exception
	{
		List<AudioDTO> audios 		= info.getAudios();
		List<LegendaDTO> legendas 	= info.getLegendas();
		List<VideoDTO> videos 		= info.getVideos();
		
		switch (part) 
		{
			case AUDIO_LANGUAGE:
			{
				if(audios != null && !audios.isEmpty())
				{
					return(MediaInfoUtils.formatLanguage(audios.get(0)));
				}
				else 
				{
					return("");
				}
			}
			case AUDIO_DUAL_AUDIO:
			{
				if(audios != null && !audios.isEmpty())
				{
					return(String.valueOf(audios.size() > 1));
				}
				else 
				{
					return("false");
				}
			}
			case AUDIO_COMERCIAL_FORMAT:
			{
				if(audios != null && !audios.isEmpty())
				{
					return(MediaInfoUtils.formatAudio(audios.get(0)));
				}
				else 
				{
					return("");
				}
			}
			case LEGENDA_IS_PRESENT:
			{
				if(legendas != null && !legendas.isEmpty())
				{
					return(String.valueOf(!legendas.isEmpty()));
				}
				else 
				{
					return("");
				}
			}
			case LEGENDA_LANGUAGE:
			{
				if(legendas != null && !legendas.isEmpty())
				{
					return(MediaInfoUtils.formatLanguage(legendas.get(0)));
				}
				else 
				{
					return("");
				}
			}
			case VIDEO_RESOLUTION:
			{
				if(videos != null && !videos.isEmpty())
				{
					return(MediaInfoUtils.formatResolution(videos.get(0)));
				}
				else 
				{
					return("");
				}
			}
			case AUDIO_QTD:
			{
				if(audios != null && !audios.isEmpty())
				{
					return(String.valueOf(audios.size()));
				}
				else 
				{
					return("0");
				}
			}
			case LEGENDA_QTD:
			{
				if(legendas != null && !legendas.isEmpty())
				{
					return(String.valueOf(legendas.size()));
				}
				else 
				{
					return("0");
				}
			}

			default:
				return("");
		}
	}
	
	public static String extractFilmId(String filmName)
	{
		Matcher matcher = Pattern.compile(PATTERN_ID_FILME, Pattern.CASE_INSENSITIVE).matcher(filmName);
		
		if(matcher.find())
		{
			return(matcher.group().trim());
		}
		
		return(null);
	}
	
	public static void verificarDuplicados(ArrayList<FilmeDTO> filmesNaLista) 
	{
		filmesNaLista.forEach(e -> { 
			e.setDuplicated(filmesNaLista.parallelStream().filter(f -> !f.equals(e)).anyMatch(f -> f.getNomeFilme().equalsIgnoreCase(e.getNomeFilme())));
		});
	}
	
	public static void listaDeFilmesParaJson(Map<String, ArrayList<FilmeDTO>> listaDeFilmes, File jsonFile) throws JsonGenerationException, JsonMappingException, IOException
	{
		ObjectMapper mapper = new ObjectMapper();
		mapper.setVisibility(PropertyAccessor.FIELD, Visibility.ANY);
		mapper.writerWithDefaultPrettyPrinter().writeValue(jsonFile, listaDeFilmes);
	}
	
	public static ListaDeFilmesDTO jsonParaListaDe(File file, boolean setLoaded) throws IOException, JsonParseException, JsonMappingException 
	{
		ListaDeFilmesDTO filmList = new ListaDeFilmesDTO();
		
		ObjectMapper mapper = new ObjectMapper();
		mapper.setVisibility(PropertyAccessor.FIELD, Visibility.ANY);
		
		TypeReference<Map<String, ArrayList<FilmeDTO>>> typeReference = new TypeReference<Map<String, ArrayList<FilmeDTO>>>() {};
		filmList.setNovaLista(mapper.readValue(file, typeReference));
		
		if(setLoaded)
		{
			filmList.getFilmesNaLista().forEach(f -> f.setLoadedFromJson(new SimpleBooleanProperty(true)));
		}
		
		return filmList;
	}
}
