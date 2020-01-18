package commandtool.telafilmejson.tarefas;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Map;
import java.util.Map.Entry;

import commandtool.comum.dados.Constantes;
import commandtool.comum.utils.CommandToolsUtils;
import commandtool.comum.utils.JavaFxUtils;
import commandtool.telafilmejson.FilmeUtils;
import commandtool.telafilmejson.TelaFilmesParaJson;
import commandtool.telafilmejson.dados.dto.FilmeDTO;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import jutil.data.enums.RegexEnum;

public class TarefaExportarListaParaJson extends Task<Void>
{
	private TelaFilmesParaJson tela;
	private String localArmazenamento;
	private String nomeArquivo = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH_mm_ss"));
	private String localSalvamento = Constantes.LOCAL_JSON_BASE;
	
	public TarefaExportarListaParaJson(TelaFilmesParaJson tela, String localArmazenamento) 
	{
		this.tela = tela;
		this.localArmazenamento = localArmazenamento;
	}

	@Override
	protected Void call() throws Exception 
	{
		return null;
	}

	@Override
	protected void succeeded() 
	{
    	try 
    	{
    		try 
    		{
    			Map<String, ArrayList<FilmeDTO>> lista = tela.getTabela().getListaDeFilmes().cloneFilmesNaLista();
    			
    			for (Entry<String, ArrayList<FilmeDTO>> item : lista.entrySet()) 
    			{
    				item.getValue().removeIf(f -> !f.toExport.get());
    				item.getValue().forEach(f -> {
    					
    					if(!f.getLoadedFromJson().get())
    					{
    						f.setLocalArmazenamento(this.localArmazenamento);
    					}
    				});
    			}
    			
    			criarArquivoJson(lista);
    		}
    		catch (Exception e) 
    		{
    			e.printStackTrace();
    		}
    		
    		tela.disableProgress();
    		super.succeeded();
		}
    	catch (Exception e) 
    	{
			e.printStackTrace();
		}
	}
	
	private void criarArquivoJson(Map<String, ArrayList<FilmeDTO>> lista) 
	{
		Alert alert = JavaFxUtils.getLoadingAlert(CommandToolsUtils.getBoundleValue("alertTitle16"), CommandToolsUtils.getBoundleValue("alertMessage13"));
		
		Platform.runLater(() -> {
			
			alert.show();
			File localSalvamentoJson = getLocalSalvamentoJson();
			
			try 
			{
				FilmeUtils.listaDeFilmesParaJson(lista, localSalvamentoJson);
				Platform.runLater(() -> alert.close());
				Platform.runLater(() -> JavaFxUtils.showAlert(AlertType.INFORMATION, CommandToolsUtils.getBoundleValue("alertTitle17"), CommandToolsUtils.getBoundleValue("alertMessage15", nomeArquivo, localSalvamentoJson.getParent())));
			}
			catch (Exception e) 
			{
				e.printStackTrace();
			}
		});
	}
	
	private File getLocalSalvamentoJson() 
	{
		JavaFxUtils.showDirectoryChooser("Diretório", tela.getPrincipalNode()).ifPresent(f -> {localSalvamento = f.getAbsolutePath();});
		
		String value = JavaFxUtils.showTextInputDialog(CommandToolsUtils.getBoundleValue("alertTitle18"), null, CommandToolsUtils.getBoundleValue("alertMessage16", nomeArquivo));
		nomeArquivo = value.isEmpty() ? nomeArquivo : value;
		nomeArquivo = nomeArquivo.replaceAll(RegexEnum.FIND_FILE_EXTENSION.getStringValue(), "");
		
		return(new File(localSalvamento, nomeArquivo.concat(Constantes.EXTENSAO_PONTO_JSON)));
	}
}
