package commandtool.telaconfiguracao.eventos;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

import commandtool.comum.utils.JavaFxUtils;
import commandtool.telaconfiguracao.TelaConfiguracao;
import commandtool.telaconfiguracao.dados.ConstantesTelaConfiguracao;
import commandtool.telaconfiguracao.dados.enums.AppsEnum;
import commandtool.telaconfiguracao.persistencia.PrevaylerUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import jutil.utils.StringUtils;

public class EventosTela implements EventHandler<ActionEvent>
{
	private TelaConfiguracao tela = null;
	
	public EventosTela(TelaConfiguracao tela) 
	{
		this.tela = tela;
	}

	@Override
	public void handle(ActionEvent event) 
	{
		if(event.getSource() instanceof Button && ((Button)event.getSource()).getId().equalsIgnoreCase(ConstantesTelaConfiguracao.ID_BTN_DIR_PROGRAMAS))
		{
			JavaFxUtils.showDirectoryChooser("Diretório Programas", tela.getPrincipalNode()).ifPresent(folder -> {
				
				try 
				{
					List<Path> files = Files.walk(folder.toPath()).filter(f -> StringUtils.containsAny(f.toFile().getName(), AppsEnum.getAllExeName())).collect(Collectors.toList());
					files.forEach(f -> {
						
						try 
						{
							if(f.toFile().getName().equalsIgnoreCase(AppsEnum.MEDIA_INFO.getExeName()))
							{
								tela.setTextFieldValue(ConstantesTelaConfiguracao.ID_TXT_MEDIA_INFOR, f.toFile().getName());
								tela.getConf().setMediaInfo(f.toFile());
							}
							else if(f.toFile().getName().equalsIgnoreCase(AppsEnum.MKV_TOOLNIX.getExeName()))
							{
								tela.setTextFieldValue(ConstantesTelaConfiguracao.ID_TXT_MKV_TOOL_NIX, f.toFile().getName());
								tela.getConf().setMkvToolNix(f.toFile());
							}
							else if(f.toFile().getName().equalsIgnoreCase(AppsEnum.HANDBRAKE.getExeName()))
							{
								tela.setTextFieldValue(ConstantesTelaConfiguracao.ID_TXT_HANDBRAKE, f.toFile().getName());
								tela.getConf().setHandBrake(f.toFile());
							}
							else if(f.toFile().getName().equalsIgnoreCase(AppsEnum.ZIP.getExeName()))
							{
								tela.setTextFieldValue(ConstantesTelaConfiguracao.ID_TXT_7ZIP, f.toFile().getName());
								tela.getConf().setHandBrake(f.toFile());
							}
						}
						catch (Exception e) 
						{
							e.printStackTrace();
						}
					});
					
					files.clear();
					tela.setTextFieldValue(ConstantesTelaConfiguracao.ID_TXT_DIR_PROGRAMAS, folder.getAbsolutePath());
					tela.getConf().setDirProgramas(folder);
				}
				catch (Exception e) 
				{
					e.printStackTrace();
				}
			});
		}
		else if(event.getSource() instanceof Button && ((Button)event.getSource()).getId().equalsIgnoreCase(ConstantesTelaConfiguracao.ID_BTN_SALVAR))
		{
			try 
			{
				PrevaylerUtils.persistCofiguration(tela.getConf());
				JavaFxUtils.showAlert(AlertType.INFORMATION,"Configuração salva", "Configuração salva com sucesso!");
			}
			catch (Exception e) 
			{
				JavaFxUtils.showAlert(AlertType.ERROR, "Configuração não salva", "Ocorreu um erro ao salvar a configuração\n"+e.getMessage());
				e.printStackTrace();
			}
		}
	}
}
