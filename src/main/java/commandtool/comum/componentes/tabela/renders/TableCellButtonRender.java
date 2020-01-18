package commandtool.comum.componentes.tabela.renders;

import commandtool.comum.dados.Constantes;
import javafx.scene.Node;
import javafx.scene.control.TableCell;

public class TableCellButtonRender<T> extends TableCell<T, Void> 
{
	private Node node;
	
	public TableCellButtonRender(Node btn) 
	{
		this.node = btn;
	}

	@Override
	public void updateItem(Void item, boolean empty) 
	{
		super.updateItem(item, empty);
		if (empty) 
		{
			setGraphic(null);
		}
		else 
		{
			node.getProperties().put(Constantes.TABLE_INDEX, getIndex());
			setGraphic(node);
		}
	}
}
