package commandtool.comum.componentes.tabela.callbacks;

import java.util.function.Supplier;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.util.Callback;
import jutil.utils.StringUtils;

public class TableCellAutoNumbered <T> implements Callback<CellDataFeatures<T, String>, ObservableValue<String>>
{
	private Supplier<TableView<T>> supplier;

	public TableCellAutoNumbered(Supplier<TableView<T>> supplier) 
	{
		this.supplier = supplier;
	}

	@Override
	public ObservableValue<String> call(CellDataFeatures<T, String> data) 
	{
		try 
		{
			return (new ReadOnlyObjectWrapper<String>(StringUtils.addZeroToLeft(4, supplier.get().getItems().indexOf(data.getValue())+1)));
		}
		catch (Exception e) 
		{
			e.printStackTrace();
			return(new ReadOnlyObjectWrapper<String>(""));
		}
	}
}



