package commandtool.comum.utils;

import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;

import commandtool.comum.dados.Constantes;
import commandtool.comum.dados.CustomItem;
import commandtool.telaconfiguracao.dados.enums.AppsEnum;
import javafx.concurrent.Task;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TreeItem;

public class ComponentEventsUtils 
{
	public void findFilesInFolder(TreeItem<CustomItem> item, Node ownerNode, Task<String> task, FileVisitor<? super Path> processador, AppsEnum executavel)
	{
		if(!CommandToolsUtils.configurationIsValid(executavel))
		{
			JavaFxUtils.showAlert(AlertType.INFORMATION, CommandToolsUtils.getBoundleValue("alertTitle01"), CommandToolsUtils.getBoundleValue("alertMessage01", executavel.getAppName()));
			return;
		}
		
		try 
		{
			Files.walkFileTree(item.getValue().getFile().toPath(), processador);
			
			Alert alert = JavaFxUtils.createTaskAlert(task, "", ownerNode);
			
			Thread t = new Thread(task, Constantes.TASK_EXTRACT_ALL_INFO);
			t.setDaemon(true);
			t.start();
			
			alert.show();
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
	}

}
