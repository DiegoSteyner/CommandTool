package commandtool.telaprocessarmkv.tarefas;

import java.io.BufferedReader;
import java.io.IOException;

import commandtool.telaprocessarmkv.ProcessarMkvUtils;
import commandtool.telaprocessarmkv.TelaProcessarMkv;
import commandtool.telaprocessarmkv.dados.dtos.ArquivoMkvDTO;
import commandtool.telaprocessarmkv.dados.dtos.ProcessoMkvDTO;
import javafx.concurrent.Task;
import jutil.utils.RuntimeUtils;

public class TarefaProcessarArquivosMkv extends Task<String>
{
	private TelaProcessarMkv tela;
	
	public TarefaProcessarArquivosMkv(TelaProcessarMkv tela) 
	{
		super();
		this.tela = tela;
	}

	@Override
	protected String call() throws Exception 
	{
		try 
		{
			ProcessoMkvDTO.bloquearEventoClick = true;
			
			for(ArquivoMkvDTO mkv : tela.getTabela().getFilesToProcess())
			{
				if(mkv.isToProcess())
				{
					updateTitle("Processando arquivo: "+mkv.getNome());
					imprimirBuffer(mkv.getCommand(), RuntimeUtils.execSystemCommandCmd(mkv.getCommand(), "UTF-8"));
					mkv.setToProcess(false);
				}
				
				if(ProcessoMkvDTO.pararProcesso)
				{
					ProcessarMkvUtils.killMkvMergeProcess();
					break;
				}
			}
			
			ProcessoMkvDTO.bloquearEventoClick = false;
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		
		return "";
	}

	private void imprimirBuffer(String command, BufferedReader buff) throws IOException 
	{
		String line = "";
		StringBuilder str = new StringBuilder();
		str.append(command).append("\n");
		
		while ((line = buff.readLine()) != null)
		{
			if(!line.contains("%"))
			{
				str.append(line).append("\n");
				updateMessage(str.toString());
			}
			else 
			{
				updateMessage(str.toString()+line);
			}
			
			if(ProcessoMkvDTO.pararProcesso)
			{
				break;
			}
		}

		buff.close();
	}
	
	@Override
    protected void succeeded() 
	{
    }

}
