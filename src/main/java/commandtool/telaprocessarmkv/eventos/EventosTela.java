package commandtool.telaprocessarmkv.eventos;

import java.io.File;
import java.util.Optional;

import commandtool.PrincipalCommandTool;
import commandtool.comum.dados.Constantes;
import commandtool.comum.utils.CommandToolsUtils;
import commandtool.comum.utils.JavaFxUtils;
import commandtool.mkvtoolnix.MkvToolnix;
import commandtool.mkvtoolnix.MkvTrack;
import commandtool.telaconfiguracao.dados.enums.AppsEnum;
import commandtool.telaconfiguracao.persistencia.PrevaylerUtils;
import commandtool.telaprocessarmkv.TelaProcessarMkv;
import commandtool.telaprocessarmkv.dados.dtos.ArquivoMkvDTO;
import commandtool.telaprocessarmkv.dados.dtos.ProcessoMkvDTO;
import commandtool.telaprocessarmkv.dados.dtos.TrilhaAudioDTO;
import commandtool.telaprocessarmkv.dados.dtos.TrilhaLegendaDTO;
import commandtool.telaprocessarmkv.dados.enums.TrilhasIds;
import commandtool.telaprocessarmkv.tarefas.TarefaProcessarArquivosMkv;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import javafx.stage.StageStyle;

public class EventosTela  implements EventHandler<ActionEvent>{

	private static final int NENHUMA_LEGENDA_SELECIONADA = 2;
	private static final int NENHUM_AUDIO_SELECIONADO = 1;
	private static final int NENHUM_ARQUIVO_SELECIONADO = 0;
	private static final int TODOS_ARQUIVOS_CONFIGURADOS = -1;
	private TelaProcessarMkv tela;

	private String arquivoProblema = "";
	
	public EventosTela(TelaProcessarMkv tela) 
	{
		this.tela = tela;
	}

	@Override
	public void handle(ActionEvent event) 
	{
		try 
		{
			PrincipalCommandTool.testePid();
			
			if(!CommandToolsUtils.configurationIsValid(AppsEnum.MKV_TOOLNIX))
			{
				JavaFxUtils.showAlert(AlertType.INFORMATION, CommandToolsUtils.getBoundleValue("alertTitle08"), CommandToolsUtils.getBoundleValue("alertMessage01", "Mkv Toolnix"));
				return;
			}
			
			if(event.getSource() instanceof Button)
			{
				switch (arquivosConfigurados()) 
				{
					case NENHUM_ARQUIVO_SELECIONADO:
					{
						JavaFxUtils.showAlert(AlertType.INFORMATION, CommandToolsUtils.getBoundleValue("alertTitle09"), CommandToolsUtils.getBoundleValue("alertMessage03"));
						break;
					}
					case NENHUM_AUDIO_SELECIONADO:
					{
						JavaFxUtils.showAlert(AlertType.INFORMATION, CommandToolsUtils.getBoundleValue("alertTitle07"), CommandToolsUtils.getBoundleValue("alertMessage07", "Audio", arquivoProblema));
						break;
					}
					case NENHUMA_LEGENDA_SELECIONADA:
					{
						JavaFxUtils.showAlert(AlertType.INFORMATION, CommandToolsUtils.getBoundleValue("alertTitle07"), CommandToolsUtils.getBoundleValue("alertMessage07", "Legenda", arquivoProblema));
						break;
					}
					case TODOS_ARQUIVOS_CONFIGURADOS:
					{
						Optional<File> diretorio = JavaFxUtils.showDirectoryChooser(CommandToolsUtils.getBoundleValue("alertTitle10"), tela.getPrincipalNode());
						
						if(diretorio.isPresent())
						{
							configureComandoProcessamento(diretorio);
							processarArquivos();
						}
						else 
						{
							JavaFxUtils.showAlert(AlertType.INFORMATION, CommandToolsUtils.getBoundleValue("alertTitle11"), CommandToolsUtils.getBoundleValue("alertMessage08"));
						}
						
						break;
					}
		
					default:
						break;
				}
			}
			else
			{
				selectAllBoxes(event);
			}
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
	}

	private int arquivosConfigurados() 
	{
		if(!tela.getTabela().getFilesToProcess().parallelStream().filter(f -> f.isToProcess()).findFirst().isPresent())
		{
			return NENHUM_ARQUIVO_SELECIONADO;
		}

		for(ArquivoMkvDTO mkv : tela.getTabela().getFilesToProcess())
		{
			if(mkv.isToProcess())
			{
				arquivoProblema = mkv.getNome();
				
				if(!mkv.getAudios().parallelStream().filter(a -> a.isProcessar()).findFirst().isPresent())
				{
					return NENHUM_AUDIO_SELECIONADO;
				}
				
				if(!mkv.getLegendas().parallelStream().filter(a -> a.isProcessar()).findFirst().isPresent())
				{
					return NENHUMA_LEGENDA_SELECIONADA;
				}
			}
			
			arquivoProblema = "";
		}
		
		return TODOS_ARQUIVOS_CONFIGURADOS;
	}
	
	private void configureComandoProcessamento(Optional<File> diretorio) throws Exception 
	{
		MkvToolnix mkvToolnix = new MkvToolnix(PrevaylerUtils.recoverConfiguration().getMkvToolNix().getAbsolutePath());
		
		tela.getTabela().getFilesToProcess().parallelStream().filter(f -> f.isToProcess()).forEach(mkv -> 
		{
			TrilhaAudioDTO audio = (TrilhaAudioDTO) mkv.getAudios().parallelStream().filter(a -> a.isProcessar()).findFirst().get();
			TrilhaLegendaDTO legenda = (TrilhaLegendaDTO) mkv.getLegendas().parallelStream().filter(a -> a.isProcessar()).findFirst().get();
			
			configureComandoMkvToolnix(mkvToolnix, mkv, audio, legenda, diretorio.get().getAbsolutePath());
		});
		
		tela.getTabela().getFilesToProcess().parallelStream().filter(f -> !f.isToProcess()).forEach(e -> e.setCommand(null));
	}
	
	private void configureComandoMkvToolnix(MkvToolnix mkv, ArquivoMkvDTO arquivo, TrilhaAudioDTO audio, TrilhaLegendaDTO legenda, String diretorioDestino) 
	{
		try 
		{
			mkv.setNoAttachaments()
			   .setNoChapters()
			   .selectAudioTrack(Integer.parseInt(audio.getIdMedia()))
			   .configureTrack(new MkvTrack(Integer.parseInt(arquivo.getVideoId()), arquivo.getNome(), audio.getIdLinguagem(), true, false))
			   .configureTrack(new MkvTrack(Integer.parseInt(audio.getIdMedia()), audio.getNomeMedia(), audio.getIdLinguagem(), true, true));
			
			if(legenda.getNomeMedia().equals(TrilhasIds.LEGENDA_FORCADA.getTitulo()))
			{
				mkv.addTrilhaExterna(new MkvTrack(0, legenda.getNomeMedia(), "por", true, true, legenda.getEnderecoLegenda()));
			}
			else if(legenda.getNomeMedia().equals(TrilhasIds.REMOVER_TODAS_TRILHAS.getTitulo()))
			{
				mkv.setNoSubtitles();
			}
			else if(!legenda.getNomeMedia().equals(TrilhasIds.MANTER_TODAS_TRILHAS.getTitulo()))
			{
				mkv.selectSubtitleTrack(Integer.parseInt(legenda.getIdMedia()));
				mkv.configureTrack(new MkvTrack(Integer.parseInt(legenda.getIdMedia()), legenda.getNomeMedia(), legenda.getIdLinguagem(), true, true));
			}
			else if(legenda.getNomeMedia().equals(TrilhasIds.MANTER_TODAS_TRILHAS.getTitulo()))
			{
				verificarLegendasExternas(mkv, arquivo);
			}
			
			arquivo.setCommand(mkv.buildCommand(arquivo.getFile().getAbsolutePath(), new File(diretorioDestino, arquivo.getNome().concat(Constantes.EXTENSAO_PONTO_MKV)).getAbsolutePath()));
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
	}

	private void verificarLegendasExternas(MkvToolnix mkv, ArquivoMkvDTO arquivo) 
	{
		arquivo.getLegendas().parallelStream().filter(a -> a.getNomeMedia().equals(TrilhasIds.LEGENDA_FORCADA.getTitulo())).forEach(l ->{
			try 
			{
				mkv.addTrilhaExterna(new MkvTrack(0, l.getNomeMedia(), "por", true, true, ((TrilhaLegendaDTO)l).getEnderecoLegenda()));
			}
			catch (Exception e) 
			{
				e.printStackTrace();
			}
		});
	}
	
	private void processarArquivos() 
	{
		TarefaProcessarArquivosMkv task = new TarefaProcessarArquivosMkv(tela);
		Alert alert = createTaskAlertText(task, "", tela.getPrincipalNode());
		
		Thread t = new Thread(task, Constantes.TASK_EXTRACT_ALL_INFO);
		t.setDaemon(true);
		t.start();
		
		alert.show();
	}
	
	private Alert createTaskAlertText(Task<String> task, String title, Node ownerNode)
	{
		Alert alert = new Alert(AlertType.NONE);
		
		alert.setTitle(CommandToolsUtils.getBoundleValue("alertTitle12"));
		alert.setHeaderText(null);
 
		VBox vbox = new VBox();
		vbox.setSpacing(15);

		alert.getDialogPane().getButtonTypes().add(ButtonType.OK);
		alert.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);
		
		Label label = new Label();
		TextArea textArea = new TextArea();
		
		label.textProperty().bind(task.titleProperty());
		textArea.textProperty().bind(task.messageProperty());
		
		Button okButton = (Button) alert.getDialogPane().lookupButton(ButtonType.OK);
		Button cancelButton = (Button) alert.getDialogPane().lookupButton(ButtonType.CANCEL);
		
		cancelButton.setOnAction(e ->
		{
			cancelButton.setDisable(true);
			ProcessoMkvDTO.pararProcesso = true;
			alert.close();
		});
		
		alert.setOnCloseRequest(e ->
		{
			if(ProcessoMkvDTO.bloquearEventoClick)
			{
				e.consume();
			}
	    });
		
		okButton.disableProperty().bind(task.runningProperty());
		
		alert.initStyle(StageStyle.UNDECORATED);
		alert.getDialogPane().setStyle(JavaFxUtils.getBorderStyle("black"));
		
		vbox.getChildren().addAll(label, textArea);
 
		alert.getDialogPane().setContent(vbox);
		
		return(alert);
	}

	public void selectAllBoxes(ActionEvent e) 
	{
		ObservableList<ArquivoMkvDTO> items = tela.getTabela().getTabela().getItems();
		
		for (int i = 0; i < items.size(); i++) 
		{
			items.get(i).setToProcess(((CheckBox)e.getSource()).isSelected());
		}
    }
}
