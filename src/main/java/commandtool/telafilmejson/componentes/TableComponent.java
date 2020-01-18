package commandtool.telafilmejson.componentes;

import java.nio.file.Paths;
import java.util.function.Predicate;
import java.util.function.Supplier;

import com.sun.javafx.scene.control.skin.TableViewSkin;

import commandtool.comum.utils.CommandToolsUtils;
import commandtool.comum.utils.JavaFxUtils;
import commandtool.telafilmejson.TelaFilmesParaJson;
import commandtool.telafilmejson.dados.ConstantesTelaFilmeParaJson;
import commandtool.telafilmejson.dados.dto.FilmeDTO;
import commandtool.telafilmejson.dados.dto.ListaDeFilmesDTO;
import commandtool.telafilmejson.dados.enums.ColunasTabela;
import commandtool.telafilmejson.dados.enums.PropriedadesFilme;
import commandtool.telafilmejson.eventos.EventosTela;
import commandtool.testes.TableCellRowColorRender;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.util.Callback;

public class TableComponent 
{
	private TableView<FilmeDTO> tabela			= new TableView<>();
	private TableViewSkin<FilmeDTO> tableSkin 	= new TableViewSkin<>(tabela);
	private TelaFilmesParaJson tela;

	private ListaDeFilmesDTO listaDeFilmes		= new ListaDeFilmesDTO();
	
	ObservableList<FilmeDTO> dadosTabela 			= FXCollections.observableArrayList(listaDeFilmes.getFilmesNaLista());
	private FilteredList<FilmeDTO> dadosFiltrados 	= new FilteredList<>(dadosTabela, p -> true);
	private SortedList<FilmeDTO> dadosOrganizados 	= new SortedList<>(dadosFiltrados);
	
	public TableComponent(TelaFilmesParaJson tela) 
	{
		this.tela = tela;
		
		tabela.getStylesheets().add("Custom.css");
        tabela.getStyleClass().add("hideEmpty");
        
		tabela.setRowFactory(new Callback<TableView<FilmeDTO>, TableRow<FilmeDTO>>() 
		{
			@Override
			public TableRow<FilmeDTO> call(TableView<FilmeDTO> param) 
			{
				return new TableCellRowColorRender<FilmeDTO>( e -> false, () -> "") ;
			}
		});
	}
	
	public Node criarTabela() 
	{
		tabela.setEditable(true);

		tabela.getColumns().add(JavaFxUtils.<FilmeDTO>criarColunaAutoNumerada(()-> tabela));
        tabela.getColumns().add(JavaFxUtils.<FilmeDTO>getTableColumnBoolean(() -> e -> new EventosTela(tela).selectAllBoxes(e), ConstantesTelaFilmeParaJson.BOOLEAN_BINDING_PROPERTIE));
		tabela.getColumns().add(criarColunaNormal("tblColumnName00", ColunasTabela.ID_FILME, -1, ConstantesTelaFilmeParaJson.FX_ALIGNMENT_CENTER_LEFT));
		tabela.getColumns().add(criarColunaNormal("tblColumnName01", ColunasTabela.NOME_FILME, 150, ConstantesTelaFilmeParaJson.FX_ALIGNMENT_CENTER_LEFT));
		tabela.getColumns().add(criarColunaNormal("tblColumnName02", ColunasTabela.ARMAZENAMENTO, -1, ""));
		tabela.getColumns().add(criarColunaNormal("tblColumnName03", ColunasTabela.QUANTIDADE_AUDIOS, -1, ""));
		tabela.getColumns().add(criarColunaNormal("tblColumnName04", ColunasTabela.DURACAO, -1, ""));
		tabela.getColumns().add(criarColunaNormal("tblColumnName05", ColunasTabela.TAMANHO, -1, ""));
		tabela.getColumns().add(criarColunaNormal("tblColumnName06", ColunasTabela.EXTENSAO, -1, ""));
		tabela.getColumns().add(JavaFxUtils.<FilmeDTO>getTableColumnButton(CommandToolsUtils.getBoundleValue("tblColumnName07"), ConstantesTelaFilmeParaJson.FX_ALIGNMENT_CENTER, criarBotoesAcoes()));
		
		tabela.setRowFactory(r -> 
		{
		    TableRow<FilmeDTO> row = new TableRow<>();
		    
		    row.setOnMouseClicked(event -> 
		    {
		        if (event.getClickCount() == 2 && (! row.isEmpty()) ) 
		        {
		        	tela.abrirEdicaoFilme(row.getItem());
		        }
		    });
		    
		    return row ;
		});
		
		tabela.columnResizePolicyProperty().set(TableView.CONSTRAINED_RESIZE_POLICY);
		tabela.setPrefWidth(600);
		
		dadosOrganizados.comparatorProperty().bind(tabela.comparatorProperty());
		tabela.setItems(dadosOrganizados);
		
		return(tabela);
	}
	
	private TableColumn<FilmeDTO, String> criarColunaNormal(String nomeColuna, ColunasTabela index, int tamanho, String estilo)
	{
		TableColumn<FilmeDTO, String> coluna = new TableColumn<FilmeDTO, String>(CommandToolsUtils.getBoundleValue(nomeColuna));
		coluna.setMinWidth(tamanho > 0 ? 200 : coluna.getWidth());
        coluna.setStyle(estilo.isEmpty() ? ConstantesTelaFilmeParaJson.FX_ALIGNMENT_CENTER : estilo);
        coluna.setCellValueFactory((cellData -> new SimpleStringProperty(recuperarValor(cellData, index))));
        
        return(coluna);
	}
	
	private String recuperarValor(CellDataFeatures<FilmeDTO, String> cellData, ColunasTabela data) 
	{
		try 
		{
			switch (data) 
			{
				case ID_FILME: 
				{
					return (cellData.getValue().getIdFilme());
				}
				case NOME_FILME: 
				{
					return (cellData.getValue().getNomeFilme());
				}
				case QUANTIDADE_LEGENDAS: 
				{
					return (cellData.getValue().getPropriedades().get(PropriedadesFilme.QUANTIDADE_LEGENDAS));
				}
				case ARMAZENAMENTO: 
				{
					return (cellData.getValue().getLocalArmazenamento());
				}
				case QUANTIDADE_AUDIOS: 
				{
					return (cellData.getValue().getPropriedades().get(PropriedadesFilme.QUANTIDADE_AUDIOS));
				}
				case DURACAO: 
				{
					return (cellData.getValue().getDuracao());
				}
				case TAMANHO: 
				{
					return (cellData.getValue().getTamanho());
				}
				case EXTENSAO: 
				{
					return (cellData.getValue().getPropriedades().get(PropriedadesFilme.EXTENSAO));
				}
		
				default:
					break;
			}
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		
		return "";
	}

	private Supplier<Node> criarBotoesAcoes() 
	{
		Supplier<Node> sup = () -> 
		{
			Button btnEditar = new Button();
			{
				btnEditar.setStyle(ConstantesTelaFilmeParaJson.FX_BACKGROUND_TRANSPARENT);
				btnEditar.setContentDisplay(ContentDisplay.CENTER);
				ImageView value = new ImageView(new Image(Paths.get(".").relativize(Paths.get("./Icons/editar.png")).toAbsolutePath().toUri().toString()));
				value.setFitWidth(15);
				value.setFitHeight(15);

				btnEditar.setGraphic(value);
				btnEditar.setId(ConstantesTelaFilmeParaJson.ID_BTN_EDITAR_FILME);
				btnEditar.setOnAction(tela.getEventos());
			}

			Button btnDeletar = new Button();
			{
				btnDeletar.setStyle(ConstantesTelaFilmeParaJson.FX_BACKGROUND_TRANSPARENT);
				btnDeletar.setContentDisplay(ContentDisplay.CENTER);
				ImageView value = new ImageView(new Image(Paths.get(".").relativize(Paths.get("./Icons/delete.png")).toAbsolutePath().toUri().toString()));
				value.setFitWidth(15);
				value.setFitHeight(15);

				btnDeletar.setGraphic(value);
				btnDeletar.setId(ConstantesTelaFilmeParaJson.ID_BTN_DELETAR_FILME);
				btnDeletar.setOnAction(tela.getEventos());
			}

			HBox hbox = new HBox();

			hbox.getChildren().add(btnEditar);
			hbox.getChildren().add(btnDeletar);

			return (hbox);
		};

		return (sup);
	}
	
	public void filtrarDados(Predicate<? super FilmeDTO> filtro)
	{
		dadosFiltrados.setPredicate(filtro);
	}
	
	public void refresh()
	{
		tabela.refresh();
	}
	
	public void refreshFilmList()
	{
		dadosTabela.add(listaDeFilmes.getFilmesNaLista().get(listaDeFilmes.getFilmesNaLista().size()-1));
	}
	
	public void removerFilme(FilmeDTO filme)
	{
		listaDeFilmes.getFilmesNaLista().remove(filme);
		dadosTabela.remove(filme);
	}
	
	public TableViewSkin<FilmeDTO> getTableSkin() {
		return tableSkin;
	}

	public void setTableSkin(TableViewSkin<FilmeDTO> tableSkin) {
		this.tableSkin = tableSkin;
	}

	public ListaDeFilmesDTO getListaDeFilmes() {
		return listaDeFilmes;
	}

	public void setListaDeFilmes(ListaDeFilmesDTO listaDeFilmes) 
	{
		this.listaDeFilmes = listaDeFilmes;
		this.dadosTabela.clear();
		this.dadosTabela.addAll(this.listaDeFilmes.getFilmesNaLista());
		
		this.tela.setTotalFilmes(this.dadosTabela.size());
	}

	public TableView<FilmeDTO> getTabela() {
		return tabela;
	}

	public void setTabela(TableView<FilmeDTO> tabela) {
		this.tabela = tabela;
	}
}

