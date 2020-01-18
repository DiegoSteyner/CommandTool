package commandtool.telaconfiguracao;

import commandtool.comum.ITelas;
import commandtool.telaconfiguracao.dados.Configuration;
import commandtool.telaconfiguracao.dados.ConstantesTelaConfiguracao;
import commandtool.telaconfiguracao.eventos.EventosTela;
import commandtool.telaconfiguracao.persistencia.PrevaylerUtils;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class TelaConfiguracao implements ITelas
{
	private static final String TEXT_FIELD_CSS 		= "-fx-text-inner-color: blue; -fx-control-inner-background: derive(-fx-base,90%);-fx-opacity: 0.5;";
	
	protected GridPane gridProgramas = new GridPane();
	protected BorderPane pane = new BorderPane();
	protected TextField txtDirProgramas = new TextField();
	protected Configuration conf = null;
	
	public TelaConfiguracao() 
	{
		initPrincipalNode();
	}
	
	@Override
	public void initPrincipalNode() 
	{
		EventosTela event = new EventosTela(this);
		GridPane grid = new GridPane();
		
		grid.setHgap(10);
	    grid.setVgap(10);
	    grid.setPadding(new Insets(35, 0, 0, 0));
	    
	    configureConf();
	    
	    TitledPane dirProgramas = new TitledPane("Diretório programas", null);
	    
	    Label lblDirProgramas = new Label("Diretório programas :");
	    Button btnDirProgramas = new Button("Procurar");
	    Button btnSalvar = new Button("Salvar");
	    lblDirProgramas.setPadding(new Insets(3, 0, 0, 0));
	    
	    txtDirProgramas.setPrefColumnCount(19);
	    
	    HBox hboxProgramas = new HBox(10);
	    HBox hboxTitulo = new HBox(10);
	    VBox vboxProgramas = new VBox(10);
	    
	    gridProgramas.setHgap(10);
	    gridProgramas.setVgap(10);
	    gridProgramas.setPadding(new Insets(10, 0, 0, 0));
	    hboxProgramas.setPadding(new Insets(10, 0, 0, 0));
	    
	    hboxProgramas.getChildren().add(lblDirProgramas);
	    hboxProgramas.getChildren().add(txtDirProgramas);
	    hboxProgramas.getChildren().add(btnDirProgramas);
	    
	    Label titulo = new Label("Programas");
	    titulo.setFont(Font.font("Calibri Light", FontWeight.LIGHT, 15));
	    
		hboxTitulo.setPadding(new Insets(20, 0, 0, 0));
		hboxTitulo.getChildren().add(createLine(270.0));
		hboxTitulo.getChildren().add(titulo);
		hboxTitulo.getChildren().add(createLine(260.0));
	    hboxTitulo.setAlignment(Pos.CENTER_LEFT);
	    
	    vboxProgramas.getChildren().add(hboxProgramas);
	    vboxProgramas.getChildren().add(hboxTitulo);
	    
	    Label lblMediaInfo = new Label("MediaInfor :");
	    TextField txtMediaInfo = new TextField();
	    Button btnConfMediainfo = new Button("Configure");
	    
	    Label lblmkvToolNix = new Label("MkvToolNix :");
	    TextField txtMkvToolNix = new TextField();
	    Button btnConfMkvToolNix = new Button("Configure");
	    
	    Label lbl7zip = new Label("7Zip :");
	    TextField txt7zip = new TextField();
	    Button btnConf7Zip = new Button("Configure");
	    
	    Label lblHandbrake = new Label("Handbrake :");
	    TextField txtHandbrake = new TextField();
	    Button btnConfHandbrake = new Button("Configure");
	    
	    txtMediaInfo.setPrefColumnCount(25);
	    txtMkvToolNix.setPrefColumnCount(25);
	    txt7zip.setPrefColumnCount(25);
	    
	    txtDirProgramas.setId(ConstantesTelaConfiguracao.ID_TXT_DIR_PROGRAMAS);
	    txtMediaInfo.setId(ConstantesTelaConfiguracao.ID_TXT_MEDIA_INFOR);
	    txtMkvToolNix.setId(ConstantesTelaConfiguracao.ID_TXT_MKV_TOOL_NIX);
	    txtHandbrake.setId(ConstantesTelaConfiguracao.ID_TXT_HANDBRAKE);
	    txt7zip.setId(ConstantesTelaConfiguracao.ID_TXT_7ZIP);
	    
	    txtDirProgramas.setText(conf.getDirProgramas() != null ? conf.getDirProgramas().getAbsolutePath() : "");
	    txtHandbrake.setText(conf.getHandBrake() != null ? conf.getHandBrake().getName() : "");
	    txtMediaInfo.setText(conf.getMediaInfo() != null ? conf.getMediaInfo().getName() : "");
	    
	    txtDirProgramas.setEditable(false);
	    txt7zip.setEditable(false);
	    txtMediaInfo.setEditable(false);
	    txtMkvToolNix.setEditable(false);
	    txtHandbrake.setEditable(false);
	    
	    txtDirProgramas.setStyle(TEXT_FIELD_CSS);
	    txt7zip.setStyle(TEXT_FIELD_CSS);
	    txtMediaInfo.setStyle(TEXT_FIELD_CSS);
	    txtMkvToolNix.setStyle(TEXT_FIELD_CSS);
	    txtHandbrake.setStyle(TEXT_FIELD_CSS);
	    
	    dirProgramas.setCollapsible(false);
	    dirProgramas.setAlignment(Pos.CENTER);
	    dirProgramas.setPadding(new Insets(10, 0, 0, 10));
	    
	    gridProgramas.add(lblMediaInfo, 0, 0);
	    gridProgramas.add(txtMediaInfo, 1, 0);
	    gridProgramas.add(btnConfMediainfo, 2, 0);
	    
	    gridProgramas.add(lblmkvToolNix, 0, 1);
	    gridProgramas.add(txtMkvToolNix, 1, 1);
	    gridProgramas.add(btnConfMkvToolNix, 2, 1);
	    
	    gridProgramas.add(lbl7zip, 0, 2);
	    gridProgramas.add(txt7zip, 1, 2);
	    gridProgramas.add(btnConf7Zip, 2, 2);
	    
	    gridProgramas.add(lblHandbrake, 0, 3);
	    gridProgramas.add(txtHandbrake, 1, 3);
	    gridProgramas.add(btnConfHandbrake, 2, 3);
	    
	    vboxProgramas.getChildren().add(gridProgramas);
	    dirProgramas.setContent(vboxProgramas);
	    
	    grid.add(dirProgramas, 0, 0);
	    
	    btnDirProgramas.setId(ConstantesTelaConfiguracao.ID_BTN_DIR_PROGRAMAS);
	    btnSalvar.setId(ConstantesTelaConfiguracao.ID_BTN_SALVAR);
	    
	    btnDirProgramas.setOnAction(event);
	    btnSalvar.setOnAction(event);
	    
	    HBox horizontalPane = new HBox();
	    horizontalPane.setPadding(new Insets(10, 10, 10, 10));
        horizontalPane.setAlignment(Pos.CENTER);
        horizontalPane.getChildren().add(btnSalvar);

	    pane.setCenter(grid);
	    pane.setBottom(horizontalPane);
	}

	private Line createLine(double endX) 
	{
		Line lineR = new Line();
		lineR.setStartX(100.0);
		lineR.setEndX(endX);
		
		lineR.setStartY(120.0);
		lineR.setEndY(120.0);
		lineR.setStroke(Paint.valueOf("#6e6c67"));
		return lineR;
	}

	private void configureConf() {
		try 
	    {
			conf = PrevaylerUtils.recoverConfiguration();
			
			if(conf == null)
			{
				conf = new Configuration();
			}
			else if(conf.getDirProgramas() == null)
			{
				conf = new Configuration();
			}
			else if(!conf.getDirProgramas().exists())
			{
				conf = new Configuration();
			}
		}
	    catch (Exception e) 
	    {
			e.printStackTrace();
		}
	}
	
	@Override
	public Node getPrincipalNode() 
	{
		return pane;
	}

	public void setTextFieldValue(String textId, String value)
	{
		if(textId.equalsIgnoreCase(ConstantesTelaConfiguracao.ID_TXT_DIR_PROGRAMAS))
		{
			txtDirProgramas.setText(value);
			return;
		}
		
		ObservableList<Node> nodes = gridProgramas.getChildren();
		
		for (int i = 0; i < nodes.size(); i++) 
		{
			if(nodes.get(i) instanceof TextField && nodes.get(i).getId() != null && nodes.get(i).getId().equals(textId))
			{
				((TextField)nodes.get(i)).setText(value);
				break;
			}
		}
	}

	public Configuration getConf() {
		return conf;
	}

	public void setConf(Configuration conf) {
		this.conf = conf;
	}
}
