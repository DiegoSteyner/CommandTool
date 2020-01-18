package commandtool.telaprocessarmkv;

import java.io.File;
import java.util.List;

import org.jutils.jprocesses.JProcesses;
import org.jutils.jprocesses.model.ProcessInfo;

import commandtool.comum.utils.JavaFxUtils;
import commandtool.telaprocessarmkv.dados.dtos.ProcessoMkvDTO;
import commandtool.telaprocessarmkv.dados.dtos.TrilhaLegendaDTO;
import commandtool.telaprocessarmkv.dados.dtos.TrilhasDTO;
import commandtool.telaprocessarmkv.dados.enums.TrilhasIds;
import javafx.application.Platform;
import javafx.scene.control.Alert.AlertType;

public class ProcessarMkvUtils 
{
	public static boolean MkvProcessRunning()
	{
		return(JProcesses.getProcessList().parallelStream().anyMatch(p -> p.getName().toLowerCase().contains("mkvmerge")));
	}
	
	public static void killMkvMergeProcess() 
	{
		List<ProcessInfo> processList = JProcesses.getProcessList();
	    
	    for (final ProcessInfo processInfo : processList) 
	    {
	        if(processInfo.getName().toLowerCase().contains("mkvmerge"))
	        {
	        	 boolean success = JProcesses.killProcess(Integer.parseInt(processInfo.getPid())).isSuccess();
	        	 ProcessoMkvDTO.pararProcesso = false;
	        	 
	        	 if(success)
	        	 {
	        		 Platform.runLater(() -> { 
	        			 JavaFxUtils.showAlert(AlertType.INFORMATION, "Processo finalizado com sucesso!", "O processo "+processInfo.getName()+" foi finalizado com sucesso!");
	        			 ProcessoMkvDTO.bloquearEventoClick = false;
	        		 });
	        	 }
	        	 else
	        	 {
	        		 Platform.runLater(() ->{ 
	        			 JavaFxUtils.showAlert(AlertType.INFORMATION, "Processo não finalizado!", "O processo "+processInfo.getName()+" de PID "+processInfo.getPid()+" não pode ser finalizado.");
	        			 ProcessoMkvDTO.bloquearEventoClick = false;
	        	 	 });
	        	 }
	        	 
	        	 break;
	        }
	    }
	    
	    processList.clear();
	}
	
	public static TrilhasDTO getManterTodasLegendas() 
	{
		TrilhaLegendaDTO legenda = new TrilhaLegendaDTO(TrilhasIds.MANTER_TODAS_TRILHAS.getTitulo());
		legenda.setIdMedia(String.valueOf(TrilhasIds.MANTER_TODAS_TRILHAS.ordinal()));
		legenda.setProcessar(false);
		legenda.setEnderecoLegenda(null);
		
		return legenda;
	}

	public static TrilhasDTO getRemoverTodasLegendas() 
	{
		TrilhaLegendaDTO legenda = new TrilhaLegendaDTO(TrilhasIds.REMOVER_TODAS_TRILHAS.getTitulo());
		legenda.setIdMedia(String.valueOf(TrilhasIds.REMOVER_TODAS_TRILHAS.ordinal()));
		legenda.setProcessar(false);
		legenda.setEnderecoLegenda(null);
		
		return legenda;
	}

	public static TrilhasDTO getLegendaForced(File file) 
	{
		TrilhaLegendaDTO legenda = new TrilhaLegendaDTO(TrilhasIds.LEGENDA_FORCADA.getTitulo());
		legenda.setIdMedia(String.valueOf(TrilhasIds.LEGENDA_FORCADA.ordinal()));
		legenda.setProcessar(false);
		legenda.setEnderecoLegenda(file);
		
		return legenda;
	}

}
