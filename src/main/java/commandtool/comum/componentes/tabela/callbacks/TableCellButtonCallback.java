package commandtool.comum.componentes.tabela.callbacks;

import java.util.function.Supplier;

import commandtool.comum.componentes.tabela.renders.TableCellButtonRender;
import javafx.scene.Node;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.util.Callback;

public class TableCellButtonCallback<T> implements Callback<TableColumn<T, Void>, TableCell<T, Void>>
{
	Supplier<Node> render;

	public TableCellButtonCallback(Supplier<Node> render) 
	{
		this.render = render;
	}

	@Override
	public TableCell<T, Void> call(TableColumn<T, Void> param) 
	{
		return new TableCellButtonRender<T>(render.get());
	}
}
