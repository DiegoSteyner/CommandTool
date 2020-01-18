package commandtool.telafilmejson.eventos;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Optional;

import commandtool.comum.dados.Constantes;
import commandtool.comum.utils.CommandToolsUtils;
import commandtool.comum.utils.JavaFxUtils;
import commandtool.telaconfiguracao.dados.enums.AppsEnum;
import commandtool.telafilmejson.FilmeUtils;
import commandtool.telafilmejson.TelaFilmesParaJson;
import commandtool.telafilmejson.dados.ConstantesTelaFilmeParaJson;
import commandtool.telafilmejson.dados.dto.FilmeDTO;
import commandtool.telafilmejson.tarefas.TarefaExportarListaParaJson;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;

public class EventosTela implements EventHandler<ActionEvent>
{
	private TelaFilmesParaJson tela;
	
	public EventosTela(TelaFilmesParaJson tela) 
	{
		this.tela = tela;
	}

	@Override
	public void handle(ActionEvent event) 
	{
		try 
		{
			if(event.getSource() instanceof Button)
			{
				Button botao = (Button)event.getSource();
				
				switch (botao.getId()) 
				{
					case ConstantesTelaFilmeParaJson.ID_BTN_IMPORTAR_JSON:
					{
						importarJson();
						break;
					}
					case ConstantesTelaFilmeParaJson.ID_BTN_EXPORTAR_JSON:
					{
						exportarListaParaJson();
						break;
					}
					case ConstantesTelaFilmeParaJson.ID_BTN_IDS_AUSENTES:
					{
						mostrarIdsAusentes();
						break;
					}
					case ConstantesTelaFilmeParaJson.ID_BTN_EDITAR_FILME:
					{
						editarFilme(botao);
						break;
					}
					case ConstantesTelaFilmeParaJson.ID_BTN_DELETAR_FILME:
					{
						deletarFilme(botao);
						break;
					}
					case ConstantesTelaFilmeParaJson.ID_BTN_SALVAR_JSON:
					{
						FilmeUtils.listaDeFilmesParaJson(tela.getTabela().getListaDeFilmes().cloneFilmesNaLista(), tela.getArquivoJsonImportado());
						JavaFxUtils.showAlert(AlertType.INFORMATION, CommandToolsUtils.getBoundleValue("alertTitle13") , CommandToolsUtils.getBoundleValue("alertMessage09"));
						break;
					}
					default:
					{
						break;
					}
				}
			}
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
	}

	private void mostrarIdsAusentes() 
	{
		if(tela.getTabela().getListaDeFilmes().getFilmesNaLista().isEmpty())
		{
			JavaFxUtils.showAlert(AlertType.INFORMATION, CommandToolsUtils.getBoundleValue("alertTitle06"), CommandToolsUtils.getBoundleValue("alertMessage05"));
			return;
		}
		
		DecimalFormat format = new DecimalFormat("00000.00");
		
		Optional<String> opt = null;
		StringBuilder str = new StringBuilder();
		double idFilme = 0.0;
		double contador = 1.0;
		
		for(FilmeDTO f : this.tela.getTabela().getListaDeFilmes().getFilmesNaLista())
		{
			opt = Optional.ofNullable(f.getIdFilme());
			
			if(opt.isPresent())
			{
				idFilme = Double.parseDouble(opt.get());
				
				if(idFilme % 1 == 0)
				{
					
					if(contador > idFilme)
					{
						str.append("ID Duplicado "+format.format(idFilme)).append("\n");
						contador = idFilme;
					}
					
					if(contador != idFilme)
					{
						while(contador != idFilme)
						{
							str.append(format.format(contador)).append("\n");
							contador++;
						}
					}
					
					contador++;
				}
			}
		}
		
		if(str.toString().isEmpty())
		{
			JavaFxUtils.showAlert(AlertType.INFORMATION, CommandToolsUtils.getBoundleValue("alertTitle14", "Duplicados"), CommandToolsUtils.getBoundleValue("alertMessage10"));
		}
		else
		{
			JavaFxUtils.showTextDialog(AlertType.INFORMATION, CommandToolsUtils.getBoundleValue("alertTitle14", "Ausentes"), CommandToolsUtils.getBoundleValue("alertMessage11"), CommandToolsUtils.getBoundleValue("alertMessage11"), str.toString());
		}
	}

	private void importarJson() throws Exception 
	{
		JavaFxUtils.showFileChooser(tela.getPrincipalNode(), CommandToolsUtils.getBoundleValue("alertTitle15"), CommandToolsUtils.getBoundleValue("alertMessage12"), Constantes.LOCAL_JSON_BASE, Constantes.EXTENSAO_ASTERISCO_JSON).ifPresent(f -> {
			try 
			{
				tela.getTabela().setListaDeFilmes(FilmeUtils.jsonParaListaDe(f, true));
				FilmeUtils.verificarDuplicados(tela.getTabela().getListaDeFilmes().getFilmesNaLista());
				tela.setArquivoJsonImportado(f);
			}
			catch (Exception e) 
			{
				e.printStackTrace();
			}
		});
	}
	
	public void selectAllBoxes(ActionEvent e)
	{
		ArrayList<FilmeDTO> list = tela.getTabela().getListaDeFilmes().getFilmesNaLista();
		
		for (FilmeDTO film : list) 
		{
			film.setToExport(((CheckBox)e.getSource()).isSelected());
		}

		tela.getTabela().refresh();
	}
	
	private void exportarListaParaJson()
	{
		if(!CommandToolsUtils.configurationIsValid(AppsEnum.MEDIA_INFO))
		{
			JavaFxUtils.showAlert(AlertType.INFORMATION, CommandToolsUtils.getBoundleValue("alertTitle02"), CommandToolsUtils.getBoundleValue("alertMessage02"));
			return;
		}
		
		try 
		{
			if(tela.getTabela().getListaDeFilmes().getFilmesNaLista().stream().anyMatch(f -> f.toExport.get()))
			{
				String str = CommandToolsUtils.getBoundleValue("lblLocalArmazenamento");
				String localArmazenamento = JavaFxUtils.showTextInputDialog(str, null, str);
				
				if(localArmazenamento.isEmpty())
				{
					JavaFxUtils.showAlert(AlertType.INFORMATION, CommandToolsUtils.getBoundleValue("lblLocalArmazenamento"), CommandToolsUtils.getBoundleValue("alertMessage06"));
				}
				else
				{
					Service<Void> progressTask = new Service<Void>() 
					{
						@Override
						protected Task<Void> createTask() 
						{
							return new TarefaExportarListaParaJson(tela, localArmazenamento);
						}
					};
					
					tela.enableProgress(progressTask);
					progressTask.restart();
				}
			}
			else
			{
				JavaFxUtils.showAlert(AlertType.INFORMATION, CommandToolsUtils.getBoundleValue("alertTitle03"), CommandToolsUtils.getBoundleValue("alertMessage03"));
			}
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
	}
	
	private void editarFilme(Node botao)
	{
		try 
		{
			int indexTabela = Integer.parseInt(botao.getParent().getProperties().get(Constantes.TABLE_INDEX).toString());
			FilmeDTO filme = tela.getTabela().getTabela().getItems().get(indexTabela);
			
			tela.abrirEdicaoFilme(filme);
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
	}
	
	private void deletarFilme(Node botao)
	{
		int indexTabela = Integer.parseInt(botao.getParent().getProperties().get(Constantes.TABLE_INDEX).toString());
		FilmeDTO filme = tela.getTabela().getTabela().getItems().get(indexTabela);
		
		tela.getTabela().removerFilme(filme);
	}
}
