package commandtool.telafilmejson.eventos;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import commandtool.comum.CommandToolFileVisitor;
import commandtool.comum.conditions.FileIsMedia;
import commandtool.comum.dados.Constantes;
import commandtool.comum.dados.CustomItem;
import commandtool.comum.utils.ComponentEventsUtils;
import commandtool.telaconfiguracao.dados.enums.AppsEnum;
import commandtool.telaconfiguracao.persistencia.PrevaylerUtils;
import commandtool.telafilmejson.TelaFilmesParaJson;
import commandtool.telafilmejson.tarefas.TarefaAdicionarFilmesNaLista;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.TreeItem;

public class EventoSelecionarPasta implements EventHandler<ActionEvent>, ChangeListener<TreeItem<CustomItem>>
{
	private TelaFilmesParaJson tela;
	
	public EventoSelecionarPasta(TelaFilmesParaJson tela) 
	{
		this.tela = tela;
	}

	@Override
	public void changed(ObservableValue<? extends TreeItem<CustomItem>> observable, TreeItem<CustomItem> oldValue, TreeItem<CustomItem> newValue) 
	{
		try 
		{
			if(!newValue.getValue().toString().equals(Constantes.MEU_COMPUTADOR))
			{
				List<File> files = new ArrayList<File>();
				Task<String> task = new TarefaAdicionarFilmesNaLista(files, tela, PrevaylerUtils.recoverConfiguration());
				
				new ComponentEventsUtils().findFilesInFolder(newValue, tela.getPrincipalNode(), task, new CommandToolFileVisitor(files, new FileIsMedia()), AppsEnum.MEDIA_INFO);
			}
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
	}
	
	@Override
	public void handle(ActionEvent event) 
	{
	}
}
