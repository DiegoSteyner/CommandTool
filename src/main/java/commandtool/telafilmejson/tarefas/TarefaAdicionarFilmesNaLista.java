package commandtool.telafilmejson.tarefas;

import java.io.File;
import java.util.List;

import commandtool.comum.utils.CommandToolsUtils;
import commandtool.telaconfiguracao.dados.Configuration;
import commandtool.telafilmejson.FilmeUtils;
import commandtool.telafilmejson.TelaFilmesParaJson;
import commandtool.telafilmejson.dados.dto.FilmeDTO;
import javafx.application.Platform;
import javafx.concurrent.Task;
import mediainfo.MediaInfo;

public class TarefaAdicionarFilmesNaLista extends Task<String>{

	private List<File> files;
	private TelaFilmesParaJson tela;
	private Configuration conf;
	
	public TarefaAdicionarFilmesNaLista(List<File> files, TelaFilmesParaJson tela, Configuration conf) 
	{
		super();
		this.files = files;
		this.tela = tela;
		this.conf = conf;
	}

	@Override
	protected String call() throws Exception 
	{
		try 
		{
			if(files.size() > 0)
			{
				for(File file : files)
				{
					updateMessage(CommandToolsUtils.getBoundleValue("lblProcessando", file.getName()));
					
					FilmeDTO filmeDTO = FilmeUtils.createFilmDTO(MediaInfo.getNewInstance(conf.getMediaInfo().getAbsolutePath(), file.getAbsolutePath()).getInfoAsDTO(), "");
					tela.getTabela().getListaDeFilmes().adicioneFilmeNaLista(filmeDTO);
					tela.getTabela().refreshFilmList();
					
					Platform.runLater(() -> {tela.setTotalFilmes(tela.getTabela().getListaDeFilmes().getFilmesNaLista().size());});
				}
				
				FilmeUtils.verificarDuplicados(tela.getTabela().getListaDeFilmes().getFilmesNaLista());
				updateProgress(1, 1);
			}
			else
			{
				updateMessage(CommandToolsUtils.getBoundleValue("alertMessage14"));
			}
			
			return "";
		}
		catch (Exception e) 
		{
			e.printStackTrace();
			return "";
		}
	}
	
	@Override
    protected void succeeded() 
	{
    }

}
