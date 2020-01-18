package commandtool.comum.utils;

import java.io.File;

import commandtool.comum.dados.CustomItem;
import javafx.scene.control.TreeItem;

public class TreeItemUtils 
{
	public static TreeItem<CustomItem> getEmptyCustomItem()
	{
		return(new TreeItem<CustomItem>(new CustomItem(new File(""), "")));
	}

}
