package commandtool.telaprocessarmkv.componentes;

import java.util.ArrayList;

import commandtool.telaprocessarmkv.dados.dtos.ArquivoMkvDTO;
import commandtool.telaprocessarmkv.dados.dtos.TrilhasDTO;
import commandtool.telaprocessarmkv.dados.enums.MediaType;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableCell;

public class TableCellRenderComboBox extends TableCell<ArquivoMkvDTO, TrilhasDTO> 
{
	private ComboBox<TrilhasDTO> comboBox;
	private MediaType type;

	public TableCellRenderComboBox(MediaType type) 
	{
		comboBox = new ComboBox<TrilhasDTO>();
		this.type = type;
	}

	@Override
	public void startEdit() 
	{
		if (!isEmpty()) 
		{
			super.startEdit();
			ArrayList<TrilhasDTO> media = null;

			switch (type) 
			{
				case AUDIO: 
				{
					media = getTableView().getItems().get(getIndex()).getAudios();
					break;
				}
				case LEGENDA: 
				{
					media = getTableView().getItems().get(getIndex()).getLegendas();
					break;
				}
	
				default:
					break;
			}

			comboBox.setItems(FXCollections.<TrilhasDTO>observableArrayList(media));
			comboBox.getSelectionModel().select(getItem());

			comboBox.focusedProperty().addListener(new ChangeListener<Boolean>() 
			{
				@Override
				public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) 
				{
					if (!newValue) 
					{
						commitEdit(comboBox.getSelectionModel().getSelectedItem());
					}
				}
			});

			setText(null);
			setGraphic(comboBox);
		}
	}

	@Override
	public void cancelEdit() 
	{
		super.cancelEdit();

		setText(getItem().toString());
		setGraphic(null);
	}

	@Override
	public void updateItem(TrilhasDTO item, boolean empty) 
	{
		super.updateItem(item, empty);
		
		if (empty) 
		{
			setText(null);
			setGraphic(null);
		} 
		else 
		{
			if (isEditing()) 
			{
				setText(null);
				setGraphic(comboBox);
			}
			else 
			{
				setText(getItem().toString());
				setGraphic(null);
			}
		}
	}
}
