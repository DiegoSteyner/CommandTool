package commandtool.telaprocessarmkv;

import commandtool.comum.ITelas;
import commandtool.comum.utils.JavaFxUtils;
import commandtool.telaprocessarmkv.componentes.TableComponent;
import commandtool.telaprocessarmkv.eventos.EventoSelecionarPasta;
import commandtool.telaprocessarmkv.eventos.EventosTela;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;

public class TelaProcessarMkv implements ITelas
{
	private BorderPane bPane = new BorderPane();
	
	private EventosTela eventos = new EventosTela(this);
	private TableComponent tabela = new TableComponent(this);
	
	public TelaProcessarMkv() 
	{
		initPrincipalNode();
	}

	@Override
	public void initPrincipalNode() 
	{
		try 
		{
			bPane.setLeft(JavaFxUtils.criarVisualizacaoDiretorios(new EventoSelecionarPasta(this)));
			bPane.setCenter(tabela.criarTabela());
			HBox hbox = new HBox();
			
			Button btnProcessar = new Button("Processar");
			btnProcessar.setOnAction(eventos);
			
			hbox.getChildren().add(btnProcessar);
			hbox.setAlignment(Pos.CENTER);
			hbox.setPadding(new Insets(20, 0, 10, 0));
			
			bPane.setBottom(hbox);
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
	}
	
	@Override
	public Node getPrincipalNode() 
	{
		return bPane;
	}
	
	public TableComponent getTabela() {
		return tabela;
	}

	public void setTabela(TableComponent tabela) {
		this.tabela = tabela;
	}

	public EventosTela getEventos() {
		return eventos;
	}

	public void setEventos(EventosTela eventos) {
		this.eventos = eventos;
	}
}
