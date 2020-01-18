package commandtool.telafilmejson;

import java.io.File;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Optional;
import java.util.function.BiFunction;

import commandtool.comum.ITelas;
import commandtool.comum.dados.Constantes;
import commandtool.comum.utils.CommandToolsUtils;
import commandtool.comum.utils.JavaFxUtils;
import commandtool.telafilmejson.componentes.TableComponent;
import commandtool.telafilmejson.dados.ConstantesTelaFilmeParaJson;
import commandtool.telafilmejson.dados.dto.Estatistica;
import commandtool.telafilmejson.dados.dto.FilmeDTO;
import commandtool.telafilmejson.dados.dto.FiltroDTO;
import commandtool.telafilmejson.dados.enums.PropriedadesFilme;
import commandtool.telafilmejson.eventos.EventoSelecionarPasta;
import commandtool.telafilmejson.eventos.EventosTela;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.concurrent.Service;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Accordion;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import jutil.utils.StringUtils;

public class TelaFilmesParaJson implements ITelas
{
	private BorderPane borderPane 			= new BorderPane();
	private EventosTela eventos				= new EventosTela(this);
	
    private VBox vboxPainelInferior 		= new VBox();
    private TextField txtProcurar 			= new TextField();
    
    private Label lblTotal 					= new Label(CommandToolsUtils.getBoundleValue("lblTotal", ""));
    private File arquivoJsonImportado 		= null;
    
    private Estatistica estatistica 		= new Estatistica();
    private TableComponent tabela			= new TableComponent(this);
    private ArrayList<FiltroDTO> filtros 	= new ArrayList<FiltroDTO>();
    
	public TelaFilmesParaJson() 
	{
		initPrincipalNode();
	}
	
	@Override
	public void initPrincipalNode() 
	{
		try 
		{
			configureFiltros();
			
			borderPane.setTop(createPainelSuperior());
	        borderPane.setLeft(criarPainelEsquerdo());
	        borderPane.setCenter(criarPainelCentro());
	        borderPane.setBottom(createPainelInferior());
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
	}

	private void configureFiltros() 
	{
		try 
		{
			BiFunction<FilmeDTO, String, Boolean> nomeFilme = (f, s) -> { return(f.getNomeFilme().toLowerCase().contains(Optional.ofNullable(s).orElse("").toLowerCase())); };
			BiFunction<FilmeDTO, String, Boolean> tamanho = (f, s) -> { return(f.getTamanho().toLowerCase().contains(Optional.ofNullable(s).orElse("").toLowerCase())); };
			BiFunction<FilmeDTO, String, Boolean> duracao = (f, s) -> { return(f.getDuracao().toLowerCase().contains(Optional.ofNullable(s).orElse("").toLowerCase())); };
			BiFunction<FilmeDTO, String, Boolean> semCapa = (f, s) -> { return(!new File(Constantes.LOCAL_IMAGE_BASE, f.getNomeCapa()).exists()); };
			BiFunction<FilmeDTO, String, Boolean> duplicados = (f, s) -> { return( f.isDuplicated()); };
			BiFunction<FilmeDTO, String, Boolean> idsDuplicados = (f, s) -> { return(tabela.getListaDeFilmes().getFilmesNaLista().parallelStream().anyMatch(m -> m.getIdFilme().equals(f.getIdFilme()) && !m.getNomeFilme().equals(f.getNomeFilme()))); };
			
			filtros.add(new FiltroDTO("Nome Filme", null, nomeFilme));
			filtros.add(new FiltroDTO("Tamanho Filme", null, tamanho));
			filtros.add(new FiltroDTO("Duração", null, duracao));
			filtros.add(new FiltroDTO("Filmes sem Capa", null, semCapa));
			filtros.add(new FiltroDTO("Filmes duplicados", null, duplicados));
			filtros.add(new FiltroDTO("Ids duplicados", null, idsDuplicados));
			
			configureFiltrosPropriedades();
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
	}

	private void configureFiltrosPropriedades() throws Exception 
	{
		FiltroDTO filtro = null;
		BiFunction<FilmeDTO, String, Boolean> condicao = null;
		
		for (int i = 0; i < PropriedadesFilme.values().length; i++) 
		{
			condicao = (f, s) -> { return(f.getPropriedades().get(getFiltroSelecionado().getPropriedade()).contains(Optional.ofNullable(s).orElse("").toLowerCase())); };
			filtro = new FiltroDTO(StringUtils.replaceUpperBySpace(PropriedadesFilme.values()[i].getNomePropriedade()), PropriedadesFilme.values()[i], condicao);
			filtros.add(filtro);
		}
		
		getTxtProcurar().textProperty().addListener((observable, oldValue, newValue) -> 
		{
			aplicarFiltroTabela(newValue);
			
		});
	}

	private Node criarPainelCentro() 
	{
		return tabela.criarTabela();
	}
	
	@Override
	public Node getPrincipalNode() 
	{
		return (borderPane);
	}

	private Node criarPainelEsquerdo() throws Exception
	{
		ScrollPane scrollPane = new ScrollPane();
		scrollPane.setContent(JavaFxUtils.criarVisualizacaoDiretorios(new EventoSelecionarPasta(this)));
        scrollPane.setFitToHeight(true);
        
        TitledPane escolherDiretorio = new TitledPane(CommandToolsUtils.getBoundleValue("title01"), scrollPane);
		
		Accordion acPainelEsquerdo = new Accordion ();
		
		acPainelEsquerdo.setExpandedPane(escolherDiretorio);
        acPainelEsquerdo.getPanes().add(escolherDiretorio);
        
        return(acPainelEsquerdo);
	}
	
	private Node createPainelSuperior() 
	{
		Label lblArquivoJson = new Label(CommandToolsUtils.getBoundleValue("lblFiltrar"));
		
		ComboBox<FiltroDTO> cbFiltrar = new ComboBox<>();
		configurarFiltroCombobox(cbFiltrar);
		
		HBox hboxProcurarJson = new HBox(cbFiltrar, txtProcurar);
        
        lblArquivoJson.setPadding(new Insets(3, 0, 0, 5));
        txtProcurar.setPrefColumnCount(25);
		
        hboxProcurarJson.setMaxWidth(500);
        hboxProcurarJson.setSpacing(10);
        hboxProcurarJson.setPadding(new Insets(20, 10, 20, 0));
        
        VBox vboxPainelSuperior = new VBox();
        
        StackPane stack = new StackPane();
		stack.getChildren().add(hboxProcurarJson);
        stack.setAlignment(Pos.CENTER);
        
        vboxPainelSuperior.getChildren().add(stack);
        
		return (vboxPainelSuperior);
	}

	private Button criarBotaoCarregarJson() 
	{
		Button btnCarregarJson = new Button("Importar Json");
		setIconeBotao(btnCarregarJson, "upload.png");
		
		btnCarregarJson.setId(ConstantesTelaFilmeParaJson.ID_BTN_IMPORTAR_JSON);
		btnCarregarJson.setOnAction(getEventos());
		return(btnCarregarJson);
	}

	private Button criarBotaoSalvarJson() 
	{
		Button btnCarregarJson = new Button("Salvar Json");
		setIconeBotao(btnCarregarJson, "salvar.png");
		
		btnCarregarJson.setId(ConstantesTelaFilmeParaJson.ID_BTN_SALVAR_JSON);
		btnCarregarJson.setOnAction(getEventos());
		return(btnCarregarJson);
	}

	private Button criarBotaoIdsAusentes() 
	{
		Button btnIdsAusentes = new Button("Ids Ausentes");
		setIconeBotao(btnIdsAusentes, "ids-ausentes.png");
		
		btnIdsAusentes.setId(ConstantesTelaFilmeParaJson.ID_BTN_IDS_AUSENTES);
		btnIdsAusentes.setOnAction(getEventos());
		return(btnIdsAusentes);
	}

	private Button criarBotaoExportJson() 
	{
		Button btnExportarJson = new Button("Exportar Json");
		setIconeBotao(btnExportarJson, "export-json.png");

		btnExportarJson.setId(ConstantesTelaFilmeParaJson.ID_BTN_EXPORTAR_JSON);
		btnExportarJson.setOnAction(getEventos());
		return(btnExportarJson);
	}
	
	private void setIconeBotao(Button btn, String nomeIcone) 
	{
		ImageView value = new ImageView(new Image(Paths.get(".").relativize(Paths.get("./Icons/"+nomeIcone)).toAbsolutePath().toUri().toString()));
		value.setFitWidth(20);
		value.setFitHeight(20);

		btn.setStyle(ConstantesTelaFilmeParaJson.FX_BACKGROUND_TRANSPARENT);
		btn.setGraphic(value);
		btn.setContentDisplay(ContentDisplay.TOP);
		btn.setPadding(new Insets(0,0,5,0));
	}

	private void configurarFiltroCombobox(ComboBox<FiltroDTO> cbFiltrar) 
	{
		cbFiltrar.setPromptText(CommandToolsUtils.getBoundleValue("lblFiltrar"));
		cbFiltrar.setItems(FXCollections.observableArrayList(filtros));
		
		cbFiltrar.valueProperty().addListener(new ChangeListener<FiltroDTO>() 
		{
	        @SuppressWarnings("rawtypes")
			@Override public void changed(ObservableValue ov, FiltroDTO valorAntigo, FiltroDTO valorNovo) 
	        {
	        	Collections.swap(filtros, 0, filtros.indexOf(valorNovo));
	        	txtProcurar.setDisable(StringUtils.containsAny(valorNovo.getNomeFiltro().toLowerCase(), "capa", "dupli"));
	        	
	        	aplicarFiltroTabela("");
	        }    
	    });
	}
	
	private void aplicarFiltroTabela(String newValue)
	{
		tabela.filtrarDados(e -> getFiltroSelecionado().getCondicao().apply(e, newValue));
	}

	public void abrirEdicaoFilme(FilmeDTO filme) 
	{
		try 
		{
			new TelaEditarFilme(filme).getDialog().showAndWait().ifPresent((e) -> 
			{
				getTabela().refresh();
			});
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
	}

	private Node createPainelInferior() 
	{
		HBox hboxPainelInferior = new HBox();
		HBox hboxLabelTotal = new HBox();
		HBox hboxBotoesInferiores = new HBox();
		
		hboxPainelInferior.setPadding(new Insets(10, 10, 10, 10));
        hboxPainelInferior.setAlignment(Pos.CENTER);
        
		hboxLabelTotal.getChildren().add(lblTotal);
		hboxLabelTotal.setAlignment(Pos.TOP_RIGHT);
		hboxLabelTotal.setPadding(new Insets(-5, 40, 0, 0));
		
		hboxBotoesInferiores.getChildren().add(criarBotaoSalvarJson());
		hboxBotoesInferiores.getChildren().add(criarBotaoExportJson());
		hboxBotoesInferiores.getChildren().add(criarBotaoIdsAusentes());
		hboxBotoesInferiores.getChildren().add(criarBotaoCarregarJson());
		hboxBotoesInferiores.setSpacing(10);
		hboxBotoesInferiores.setAlignment(Pos.CENTER);
		
		vboxPainelInferior.getChildren().add(hboxLabelTotal);
		
		vboxPainelInferior.setMaxWidth(Double.MAX_VALUE);
		vboxPainelInferior.setAlignment(Pos.CENTER);
		vboxPainelInferior.getChildren().add(hboxBotoesInferiores);
		vboxPainelInferior.setPrefWidth(2000);
		
		hboxPainelInferior.getChildren().add(vboxPainelInferior);
		
		return(hboxPainelInferior);
	}
	
	public void enableProgress(Service<Void> progressTask)
	{
		ProgressBar barraProgresso = new ProgressBar();
	    ProgressIndicator indicadorProgresso = new ProgressIndicator(0);
	    Label lblProcessando = new Label(CommandToolsUtils.getBoundleValue("lblProcessando"));
	    HBox hboxBarra = new HBox(new StackPane(barraProgresso, lblProcessando), indicadorProgresso);
	    Insets insets = new Insets(-15, 0, 0, 0);
	    
		barraProgresso.setPadding(insets);
		lblProcessando.setPadding(insets);
		lblProcessando.setTextFill(Color.DARKBLUE);
		
		barraProgresso.prefWidthProperty().bind(vboxPainelInferior.widthProperty().subtract(20));
		
		barraProgresso.progressProperty().bind(progressTask.progressProperty());
        indicadorProgresso.progressProperty().bind(progressTask.progressProperty());
        lblProcessando.textProperty().bind(progressTask.messageProperty());
        
        vboxPainelInferior.getChildren().add(1, hboxBarra);
	}
	
	public FiltroDTO getFiltroSelecionado()
	{
		return(filtros.get(0));
	}
	
	public void setTotalFilmes(int total)
	{
		lblTotal.setText(CommandToolsUtils.getBoundleValue("lblTotal", String.valueOf(total)));
	}
	
	public void disableProgress()
	{
		vboxPainelInferior.getChildren().remove(1);
	}
	
	protected MenuBar createMenuBarView() 
	{
		MenuBar menu = new MenuBar();
		
		Menu arquivo = new Menu("");
		Menu configuracao = new Menu("");
		
		arquivo.setDisable(true);
		configuracao.setDisable(true);
		
		menu.getMenus().add(arquivo);
		menu.getMenus().add(configuracao);
		
		return(menu);
	}
	
	public TextField getTxtProcurar() {
		return txtProcurar;
	}

	public void setTxtProcurar(TextField txtProcurar) {
		this.txtProcurar = txtProcurar;
	}

	public Estatistica getEstatistica() {
		return estatistica;
	}

	public void setEstatistica(Estatistica estatistica) {
		this.estatistica = estatistica;
	}

	public EventosTela getEventos() {
		return eventos;
	}

	public void setEventos(EventosTela eventos) {
		this.eventos = eventos;
	}

	public TableComponent getTabela() {
		return tabela;
	}

	public void setTabela(TableComponent tabela) {
		this.tabela = tabela;
	}

	public File getArquivoJsonImportado() {
		return arquivoJsonImportado;
	}

	public void setArquivoJsonImportado(File arquivoJsonImportado) {
		this.arquivoJsonImportado = arquivoJsonImportado;
	}
}
