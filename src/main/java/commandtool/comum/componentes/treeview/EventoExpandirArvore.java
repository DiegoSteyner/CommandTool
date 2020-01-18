package commandtool.comum.componentes.treeview;

import java.io.File;

import commandtool.comum.dados.Constantes;
import commandtool.comum.dados.CustomItem;
import commandtool.comum.utils.JavaFxUtils;
import commandtool.comum.utils.TreeItemUtils;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.control.TreeItem;

public class EventoExpandirArvore implements EventHandler<Event>{

	private static final String BRANCH_EXPANDED = "BranchExpandedEvent";
	private static final String BRANCH_COLLAPSED = "BranchCollapsedEvent";
	private static final String EVENT_NOTIFICATION = "ChildrenModificationEvent";

	@Override
	@SuppressWarnings("unchecked")
	public void handle(Event event) 
	{
		TreeItem<CustomItem> source = (TreeItem<CustomItem>)event.getSource();
		
		if (event.getEventType().getName().equals(BRANCH_COLLAPSED))
		{
			int size = source.getChildren().size()-1;
			
			while(size > -1)
			{
				source.getChildren().remove(size);
				size--;
			}
			
			source.getChildren().add(TreeItemUtils.getEmptyCustomItem());
		}
		else if (event.getEventType().getName().equals(BRANCH_EXPANDED) )
		{
			try 
			{
				CustomItem itemSelecionado = source.getValue();
				File[] arquivos = null;
				
				if(itemSelecionado.getName().equals(Constantes.MEU_COMPUTADOR))
				{
					arquivos = File.listRoots();
				}
				else
				{
					source.getChildren().remove(0);
					arquivos = itemSelecionado.getFile().listFiles();
				}
				
				JavaFxUtils.adicioneArquivosNaArvore(arquivos, source);
			}
			catch (Exception e) 
			{
				e.printStackTrace();
			}
			
		}
		else if (event.getEventType().getName().equals(EVENT_NOTIFICATION))
		{
			
		}
	}
}
