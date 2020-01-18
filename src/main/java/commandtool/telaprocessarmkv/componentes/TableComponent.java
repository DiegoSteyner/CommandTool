package commandtool.telaprocessarmkv.componentes;

import java.util.Collections;
import java.util.LinkedList;

import com.sun.javafx.scene.control.skin.TableViewSkin;

import commandtool.comum.utils.JavaFxUtils;
import commandtool.telaprocessarmkv.TelaProcessarMkv;
import commandtool.telaprocessarmkv.dados.dtos.ArquivoMkvDTO;
import commandtool.telaprocessarmkv.dados.dtos.TrilhaAudioDTO;
import commandtool.telaprocessarmkv.dados.dtos.TrilhaLegendaDTO;
import commandtool.telaprocessarmkv.dados.dtos.TrilhasDTO;
import commandtool.telaprocessarmkv.dados.enums.ColunasTabela;
import commandtool.telaprocessarmkv.dados.enums.MediaType;
import commandtool.telaprocessarmkv.eventos.EventosTela;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;

public class TableComponent 
{
	private TableView<ArquivoMkvDTO> tabela = new TableView<>();
	private TableViewSkin<ArquivoMkvDTO> tableSkin = new TableViewSkin<>(tabela);
	private LinkedList<ArquivoMkvDTO> filesToProcess = new LinkedList<>();

	ObservableList<ArquivoMkvDTO> dadosTabela 			= FXCollections.observableArrayList(filesToProcess);
	private FilteredList<ArquivoMkvDTO> dadosFiltrados 	= new FilteredList<>(dadosTabela, p -> true);
	private SortedList<ArquivoMkvDTO> dadosOrganizados 	= new SortedList<>(dadosFiltrados);
	private TelaProcessarMkv tela;
	
	public TableComponent(TelaProcessarMkv tela) 
	{
		this.tela = tela;
	}
	
	@SuppressWarnings({"unchecked", "rawtypes"})
	public Node criarTabela() 
	{
		TableColumn<ArquivoMkvDTO, String> coluna01 = new TableColumn<ArquivoMkvDTO, String>("Nome filme");
		TableColumn coluna02 = new TableColumn("Audios");
		TableColumn coluna03 = new TableColumn("Legendas");
		TableColumn<ArquivoMkvDTO, String> coluna04 = new TableColumn<ArquivoMkvDTO, String>("Duração");
		TableColumn<ArquivoMkvDTO, String> coluna05 = new TableColumn<ArquivoMkvDTO, String>("Tamanho");
		TableColumn<ArquivoMkvDTO, String> coluna06 = new TableColumn<ArquivoMkvDTO, String>("Formato");
		
		configureColunaNome(coluna01);
		configureColunaAudios(coluna02);
		configureColunaLegenda(coluna03);
		
		coluna01.setMinWidth(200);
		tabela.setEditable(true);
		
        coluna01.setStyle("-fx-alignment: CENTER-LEFT;");
        coluna02.setStyle("-fx-alignment: CENTER;");
        coluna03.setStyle("-fx-alignment: CENTER;");
        coluna04.setStyle("-fx-alignment: CENTER;");
        coluna05.setStyle("-fx-alignment: CENTER;");
        coluna06.setStyle("-fx-alignment: CENTER;");
        
		tabela.getColumns().add(JavaFxUtils.<ArquivoMkvDTO>getTableColumnBoolean(() -> e -> { new EventosTela(this.tela).selectAllBoxes(e);}, "toProcess"));
		tabela.getColumns().add(coluna01);
		tabela.getColumns().add(coluna02);
		tabela.getColumns().add(coluna03);
		tabela.getColumns().add(coluna04);
		tabela.getColumns().add(coluna05);
		tabela.getColumns().add(coluna06);
		
		coluna01.setCellValueFactory((cellData -> new SimpleStringProperty(getValue(cellData, ColunasTabela.NOME_FILME))));
		coluna02.setCellValueFactory((cellData -> new SimpleObjectProperty<TrilhaAudioDTO>((TrilhaAudioDTO) getValue(cellData, true))));
		coluna03.setCellValueFactory((cellData -> new SimpleObjectProperty<TrilhaLegendaDTO>((TrilhaLegendaDTO) getValue(cellData, false))));
		coluna04.setCellValueFactory((cellData -> new SimpleStringProperty(getValue(cellData, ColunasTabela.DURACAO))));
		coluna05.setCellValueFactory((cellData -> new SimpleStringProperty(getValue(cellData, ColunasTabela.TAMANHO))));
		coluna06.setCellValueFactory((cellData -> new SimpleStringProperty(getValue(cellData, ColunasTabela.FORMATO))));
		
		tabela.columnResizePolicyProperty().set(TableView.CONSTRAINED_RESIZE_POLICY);
		tabela.setPrefWidth(600);
		
		dadosOrganizados.comparatorProperty().bind(tabela.comparatorProperty());
		tabela.setItems(dadosOrganizados);
		
		return(tabela);
	}

	private void configureColunaNome(TableColumn<ArquivoMkvDTO, String> coluna01) {
		coluna01.setCellValueFactory(new PropertyValueFactory<>("nome"));
		coluna01.setCellFactory(TextFieldTableCell.<ArquivoMkvDTO>forTableColumn());
		
		coluna01.setOnEditCommit((CellEditEvent<ArquivoMkvDTO, String> event) -> {
	            TablePosition<ArquivoMkvDTO, String> pos = event.getTablePosition();
	 
	            String newFullName = event.getNewValue();
	 
	            int row = pos.getRow();
	            ArquivoMkvDTO person = event.getTableView().getItems().get(row);
	 
	            person.setNome(newFullName);
	        });
	}

	@SuppressWarnings({"unchecked", "rawtypes"})
	private void configureColunaAudios(TableColumn coluna02) 
	{
		coluna02.setCellFactory( cell -> new TableCellRenderComboBox(MediaType.AUDIO));
		coluna02.setOnEditCommit(
                new EventHandler<TableColumn.CellEditEvent<ArquivoMkvDTO, TrilhasDTO>>()
                {
                    @Override
                    public void handle( TableColumn.CellEditEvent<ArquivoMkvDTO, TrilhasDTO> t )
                    {
                    	ArquivoMkvDTO filep = t.getTableView().getItems().get(t.getTablePosition().getRow());
                    	t.getNewValue().getNomeMedia();
                    	
                    	
                    	filep.getAudios().forEach(m -> {
                    		m.setProcessar(m.getNomeMedia().equals(t.getNewValue().getNomeMedia()));
                    		if(m.isProcessar())
                    		{
                    			System.out.println(filep.getNome());
                    			System.out.println(t.getNewValue().getNomeMedia());
                    		}
                    	});
                    }
                }
        );
	}

	@SuppressWarnings({"unchecked", "rawtypes"})
	private void configureColunaLegenda(TableColumn coluna03) 
	{
		coluna03.setCellFactory( cell -> new TableCellRenderComboBox(MediaType.LEGENDA));
		coluna03.setOnEditCommit(
                new EventHandler<TableColumn.CellEditEvent<ArquivoMkvDTO, TrilhasDTO>>()
                {
                    @Override
                    public void handle( TableColumn.CellEditEvent<ArquivoMkvDTO, TrilhasDTO> t )
                    {
                    	ArquivoMkvDTO filep = t.getTableView().getItems().get(t.getTablePosition().getRow());
                    	t.getNewValue().getNomeMedia();
                    	
                    	filep.getLegendas().forEach(m -> {
                    		m.setProcessar(m.getNomeMedia().equalsIgnoreCase(t.getNewValue().getNomeMedia()));
                    	});
                    }
                }
        );
	}
	
	private String getValue(CellDataFeatures<ArquivoMkvDTO, String> cellData, ColunasTabela colunas) 
	{
		switch (colunas) 
		{
			case NOME_FILME:
			{
				return(cellData.getValue().getNome());
			}
			case DURACAO:
			{
				return(((TrilhaAudioDTO)cellData.getValue().getAudios().get(1)).getDuracao());
			}
			case TAMANHO:
			{
				return(cellData.getValue().getTamanho());
			}
			case FORMATO:
			{
				return(cellData.getValue().getFormato());
			}

			default:
				break;
		}
		
		return "Nome filme";
	}

	@SuppressWarnings("rawtypes")
	private TrilhasDTO getValue(Object cellData, boolean isAudio) 
	{
		CellDataFeatures cellDataFeatures = (CellDataFeatures)cellData;
		ArquivoMkvDTO fileProcessorDTO = (ArquivoMkvDTO)cellDataFeatures.getValue();
		
		if(isAudio)
		{
			swapAudio(fileProcessorDTO);
			return (fileProcessorDTO.getAudios().get(0));
		}
		else
		{
			swapLegenda(fileProcessorDTO);
			return(fileProcessorDTO.getLegendas().get(0));
		}
	}

	private void swapAudio(ArquivoMkvDTO fileProcessorDTO) 
	{
		for (int i = 0; i < fileProcessorDTO.getAudios().size(); i++) 
		{
			if(fileProcessorDTO.getAudios().get(i).isProcessar())
			{
				Collections.swap(fileProcessorDTO.getAudios(), 0, i);
				break;
			}
		}
	}

	private void swapLegenda(ArquivoMkvDTO fileProcessorDTO) 
	{
		for (int i = 0; i < fileProcessorDTO.getLegendas().size(); i++) 
		{
			if(fileProcessorDTO.getLegendas().get(i).isProcessar())
			{
				Collections.swap(fileProcessorDTO.getLegendas(), 0, i);
				break;
			}
		}
	}

	public void adicionarArquivo(ArquivoMkvDTO arquivo)
	{
		this.filesToProcess.add(arquivo);
		dadosTabela.add(filesToProcess.getLast());
	}
	
	public TableView<ArquivoMkvDTO> getTabela() {
		return tabela;
	}

	public void setTabela(TableView<ArquivoMkvDTO> tabela) {
		this.tabela = tabela;
	}

	public LinkedList<ArquivoMkvDTO> getFilesToProcess() {
		return filesToProcess;
	}

	public void setFilesToProcess(LinkedList<ArquivoMkvDTO> filesToProcess) {
		this.filesToProcess = filesToProcess;
	}

	public TableViewSkin<ArquivoMkvDTO> getTableSkin() {
		return tableSkin;
	}

	public void setTableSkin(TableViewSkin<ArquivoMkvDTO> tableSkin) {
		this.tableSkin = tableSkin;
	}
}
