package commandtool.telaprocessarmkv.tarefas;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import commandtool.comum.dados.Constantes;
import commandtool.telaconfiguracao.dados.Configuration;
import commandtool.telaprocessarmkv.ProcessarMkvUtils;
import commandtool.telaprocessarmkv.TelaProcessarMkv;
import commandtool.telaprocessarmkv.dados.dtos.ArquivoMkvDTO;
import commandtool.telaprocessarmkv.dados.dtos.TrilhaAudioDTO;
import commandtool.telaprocessarmkv.dados.dtos.TrilhaLegendaDTO;
import commandtool.telaprocessarmkv.dados.dtos.TrilhasDTO;
import javafx.concurrent.Task;
import jutil.data.enums.RegexEnum;
import jutil.utils.FileUtils;
import jutil.utils.StringUtils;
import mediainfo.MediaInfo;
import mediainfo.data.dto.MediaInfoDTO;
import mediainfo.utils.MediaInfoUtils;

public class TarefaAdicionarArquivosTabela extends Task<String>{

	private TelaProcessarMkv tela;
	private List<File> files;
	private Configuration conf;
	
	public TarefaAdicionarArquivosTabela(TelaProcessarMkv tela, List<File> files, Configuration conf) 
	{
		super();
		this.tela = tela;
		this.files = files;
		this.conf = conf;
	}

	@Override
	protected String call() throws Exception 
	{
		files.forEach(f -> {
			
			try 
			{
				updateMessage("Lendo informações de "+f.getName());
				
				MediaInfoDTO info = MediaInfo.getNewInstance(conf.getMediaInfo().getAbsolutePath(), f.getAbsolutePath()).getInfoAsDTO();
				ArquivoMkvDTO arquivo = new ArquivoMkvDTO();
				
				ArrayList<TrilhasDTO> audios = new ArrayList<>();
				ArrayList<TrilhasDTO> legendas = new ArrayList<>();
				
				popularAudios(info, audios);
				popularLegendas(info, legendas);
				popularLegendasAvulsas(f, legendas);
				popularInformacoesArquivo(f, info, arquivo, audios, legendas);
				
				tela.getTabela().adicionarArquivo(arquivo);
			}
			catch (Exception e) 
			{
				e.printStackTrace();
			}
		});
		
		return "";
	}

	private void popularInformacoesArquivo(File f, MediaInfoDTO info, ArquivoMkvDTO file, ArrayList<TrilhasDTO> audios, ArrayList<TrilhasDTO> legendas) throws Exception 
	{
		file.setFile(f);
		file.setNome(f.getName().replaceAll(RegexEnum.FIND_FILE_EXTENSION.getStringValue(), "").trim());
		file.setTamanho(FileUtils.sizeToHumanReadable(Long.parseLong(info.getInformacoesGerais().getFileSize()), 2));
		file.setFormato(info.getInformacoesGerais().getFormat());
		file.setVideoId(info.getVideos().get(0).getStreamOrder());
		file.setAudios(audios);
		file.setLegendas(legendas);
	}

	private void popularLegendasAvulsas(File f, ArrayList<TrilhasDTO> legendas) 
	{
		Stream.of(f.getParentFile().listFiles((n) -> { 
			return (n.getName().toLowerCase().endsWith(Constantes.EXTENSAO_PONTO_SRT) && n.getName().toLowerCase().contains("forced"));
		}))
		.forEach(l -> {
			legendas.add(ProcessarMkvUtils.getLegendaForced(l));
		});
	}

	private void popularLegendas(MediaInfoDTO info, ArrayList<TrilhasDTO> legendas) 
	{
		legendas.add(new TrilhaLegendaDTO("Selecione"));
		legendas.add(ProcessarMkvUtils.getManterTodasLegendas());
		legendas.add(ProcessarMkvUtils.getRemoverTodasLegendas());
		
		info.getLegendas().forEach(l -> {
			
			try 
			{
				String language = MediaInfoUtils.formatLanguage(l);
				String title = l.getTitle();
				
				TrilhaLegendaDTO legenda = null;
				
				if(StringUtils.isNullOrEmptyTrim(title))
				{
					legenda = new TrilhaLegendaDTO(language);
				}
				else
				{
					legenda = new TrilhaLegendaDTO(title + " ["+language+"]");
				}
				
				legenda.setIdMedia(l.getID());
				legenda.setProcessar(false);
				legenda.setIdLinguagem(l.getLanguage());
				
				legendas.add(legenda);
			}
			catch (Exception e) 
			{
				e.printStackTrace();
			}
		});
	}

	private void popularAudios(MediaInfoDTO info, ArrayList<TrilhasDTO> audios) 
	{
		audios.add(new TrilhaAudioDTO("Selecione"));
		
		info.getAudios().forEach(a -> {
			
			try 
			{
				TrilhaAudioDTO audio = new TrilhaAudioDTO("Audio "+MediaInfoUtils.formatLanguage(a) +" ["+MediaInfoUtils.formatAudio(a)+"]");
				audio.setIdMedia(a.getStreamOrder());
				audio.setProcessar(false);
				audio.setDuracao(MediaInfoUtils.formatDuration(info.getInformacoesGerais().getDuration()));
				audio.setIdLinguagem(a.getLanguage());
				
				audios.add(audio);
			}
			catch (Exception e) 
			{
				e.printStackTrace();
			}
		});
	}
}
