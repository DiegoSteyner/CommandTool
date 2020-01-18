package commandtool.telafilmejson;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Properties;
import java.util.SortedSet;
import java.util.TreeSet;

import commandtool.comum.dados.Constantes;
import commandtool.comum.utils.CommandToolsUtils;
import commandtool.comum.utils.JavaFxUtils;
import commandtool.telafilmejson.dados.dto.FilmeDTO;
import commandtool.telafilmejson.dados.enums.PropriedadesFilme;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import jutil.utils.FileUtils;

public class TelaEditarFilme 
{
	private Dialog<FilmeDTO> dialog = new Dialog<>();
	private TextField txtNomeFilme = new TextField();
	private TextField txtIdFilme = new TextField();
	private TextField txtLocalArmazenamento = new TextField();
	private ChoiceBox<String> cbGeneros = new ChoiceBox<>();
	private TextField txtClassificacao = new TextField();
	private TextField txtTamanho = new TextField();
	private TextField txtDuracao = new TextField();
	private ChoiceBox<String> cbIdioma = new ChoiceBox<>();
	private TextField txtAnoLancamento = new TextField();
	private TextField txtExtensao = new TextField();
	private TextField txtFormatoArquivo = new TextField();
	private TextField txtFormatoArquivoAudio = new TextField();
	private ChoiceBox<String> cbIdiomaLegenda = new ChoiceBox<>();
	private TextField txtResolucao = new TextField();
	private TextArea txaSinopseFilme = new TextArea();
	private Spinner<Integer> spnQuantidadeAudios = new Spinner<Integer>();
	private Spinner<Integer> spnQuantidadeLegendas = new Spinner<Integer>();
	private ToggleButton tgbAssistido = new ToggleButton("Não");
	private ToggleButton tgbDualAudio = new ToggleButton("Não");
	private ToggleButton tgbLegendas = new ToggleButton("Não");
	
	private String enderecoCapa = "";
	
	private FilmeDTO filme;
	
	public TelaEditarFilme(FilmeDTO filme) throws Exception 
	{
		this.filme = filme;
		
		GridPane gridPrincipal = new GridPane();
		
		gridPrincipal.setPadding(new Insets(10, 0, 0, 10));
		gridPrincipal.add(criarPainelEsquerdo(), 0, 0);
		gridPrincipal.add(criarPainelCentral(), 1, 0, 2,2);
		
		configurarDialog(gridPrincipal);
		
		dialog.setOnShown(event ->
	    {
	        Platform.runLater(() ->
	        {
	            posicionarBotaoCentro();
	        });
	    });
		
		iniciarValoresComponentes();
	}

	private void posicionarBotaoCentro() 
	{
		Button btnClose = (Button) dialog.getDialogPane().lookupButton(ButtonType.CANCEL);
		HBox hBox = (HBox) btnClose.getParent();
		
		for (int i = 0; i < hBox.getChildren().size(); i++) 
		{
			if(hBox.getChildren().get(i) instanceof Region)
			{
				hBox.getChildren().remove(i);
				break;
			}
		}
		
		hBox.setAlignment(Pos.CENTER);
	}

	private VBox criarPainelEsquerdo() 
	{
		HBox hboxBotaoImagem = new HBox();
        HBox hboxImagem = new HBox();
        
        Button procurarImagem = new Button(CommandToolsUtils.getBoundleValue("alertTitle19"));
        
		procurarImagem.setOnAction(event -> {
			try 
			{
				JavaFxUtils.showFileChooser(procurarImagem, CommandToolsUtils.getBoundleValue("alertTitle20"), CommandToolsUtils.getBoundleValue("alertTitle20"), Constantes.LOCAL_IMAGE_BASE, "*.jpg", "*.png").ifPresent(f -> 
				{
					enderecoCapa = f.getAbsolutePath();
					((ImageView)hboxImagem.getChildren().get(0)).setImage(new Image(Paths.get(enderecoCapa).toAbsolutePath().toUri().toString()));
				});
			}
			catch (Exception e) 
			{
				e.printStackTrace();
			}
		});
		
		enderecoCapa = new File(Constantes.LOCAL_IMAGE_BASE, filme.getNomeCapa()).getAbsolutePath();
		
		hboxBotaoImagem.setPadding(new Insets(5, 0, 0, 0));
		hboxBotaoImagem.setAlignment(Pos.CENTER);
		hboxBotaoImagem.getChildren().add(procurarImagem);
		
		hboxImagem.setStyle(JavaFxUtils.getBorderStyle("black"));
		hboxImagem.getChildren().add(criarVisualizadorImagem(enderecoCapa));
		
		return new VBox(10, hboxImagem, hboxBotaoImagem);
	}

	private ScrollPane criarPainelCentral() 
	{
		HBox hboxTituloFilme = new HBox();
		Label lblTituloFilme = new Label("nome filme");
		VBox vboxTituloFilme = new VBox(lblTituloFilme,txtNomeFilme);
		
		hboxTituloFilme.getChildren().add(vboxTituloFilme);
		hboxTituloFilme.setAlignment(Pos.CENTER);
		vboxTituloFilme.setAlignment(Pos.CENTER);

		HBox hboxSinopseFilme = new HBox();
		Label lblSinopseFilme = new Label("Sinopse");
		VBox vboxSinopseFilme = new VBox(lblSinopseFilme,txaSinopseFilme);
		
		lblSinopseFilme.setPadding(new Insets(15, 0, 15, 0));
		lblTituloFilme.setPadding(new Insets(0, 0, 10, 0));
		vboxTituloFilme.setPadding(new Insets(0, 0, 10, 0));
		txaSinopseFilme.setPrefColumnCount(45);
		txtNomeFilme.setPrefColumnCount(30);
		
		hboxSinopseFilme.getChildren().add(vboxSinopseFilme);
		hboxSinopseFilme.setAlignment(Pos.CENTER);
		vboxSinopseFilme.setAlignment(Pos.CENTER);
		
		hboxSinopseFilme.setPrefWidth(600);
		
		ScrollPane scrollPane = new ScrollPane();
		
		VBox vboxCentro = new VBox(10, hboxTituloFilme ,criarPainelSuperior(), criarPainelInferior(), hboxSinopseFilme);
		vboxCentro.setPrefWidth(700);
		
		scrollPane.setContent(vboxCentro);
		scrollPane.setStyle(" -fx-background-color:transparent;");
        scrollPane.setFitToWidth(true);
        scrollPane.setMaxHeight(420);
        scrollPane.setPadding(new Insets(-50, 0, 0, 0));
        
		return scrollPane;
	}

	private TitledPane criarPainelInferior() {
		TitledPane tlpEspecificas = new TitledPane();
		GridPane gridPropriedadesEspecificas = new GridPane();
		tlpEspecificas.setText("Propriedades especificas");
		tlpEspecificas.setAlignment(Pos.CENTER);
		tlpEspecificas.setCollapsible(false);
		tlpEspecificas.setPadding(new Insets(0, 0, 0, 15));
		addPropriedadesEspecificas(gridPropriedadesEspecificas);
		gridPropriedadesEspecificas.setHgap(10);
		gridPropriedadesEspecificas.setVgap(10);
		
		tlpEspecificas.setContent(gridPropriedadesEspecificas);
		return tlpEspecificas;
	}

	private TitledPane criarPainelSuperior() 
	{
		TitledPane tlpGeral = new TitledPane();
		GridPane gridPropriedades = new GridPane();
		
		tlpGeral.setText("Propriedades gerais");
		tlpGeral.setAlignment(Pos.CENTER);
		tlpGeral.setCollapsible(false);
		tlpGeral.setPadding(new Insets(0, 0, 0, 15));

		addPropertiesGerais(gridPropriedades);
		
		gridPropriedades.setHgap(10);
		gridPropriedades.setVgap(10);
		tlpGeral.setContent(gridPropriedades);
		return tlpGeral;
	}
	
	private void addPropertiesGerais(GridPane gridPropriedades) 
	{
		int row = 0;
		int col = 0;
		
		EventHandler<ActionEvent> eventHandler = new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent event) 
			{
				ToggleButton tgbButton = (ToggleButton) event.getSource();
				tgbButton.setText(tgbButton.isSelected() ? "Sim" : "Não");
			}
		};
		
		gridPropriedades.add(new Label("Id Filme"), col++, row);
		gridPropriedades.add(txtIdFilme, col++, row);
		gridPropriedades.add(new Label("Local Armazenamento"), col++, row);
		gridPropriedades.add(txtLocalArmazenamento, col++, row);
		
		row++;
		col = 0;

		gridPropriedades.add(new Label("Gênero"), col++, row);
		gridPropriedades.add(cbGeneros, col++, row);
		gridPropriedades.add(new Label("Classificação"), col++, row);
		gridPropriedades.add(txtClassificacao, col++, row);
		
		row++;
		col = 0;

		gridPropriedades.add(new Label("Tamanho"), col++, row);
		gridPropriedades.add(txtTamanho, col++, row);
		gridPropriedades.add(new Label("Duração"), col++, row);
		gridPropriedades.add(txtDuracao, col++, row);
		
		row++;
		col = 0;

		gridPropriedades.add(new Label("Assistido"), col++, row);
		gridPropriedades.add(tgbAssistido, col++, row);
		
		tgbAssistido.setSelected(true);
		tgbAssistido.setText(tgbAssistido.isSelected() ? "Sim" : "Não");
		tgbDualAudio.setText(tgbAssistido.isSelected() ? "Sim" : "Não");
		tgbLegendas.setText(tgbAssistido.isSelected() ? "Sim" : "Não");
		
		tgbAssistido.getStyleClass().add("toggleSelectedGreen");
		tgbDualAudio.getStyleClass().add("toggleSelectedGreen");
		tgbLegendas.getStyleClass().add("toggleSelectedGreen");
		
		tgbAssistido.setOnAction(eventHandler);
		tgbDualAudio.setOnAction(eventHandler);
		tgbLegendas.setOnAction(eventHandler);
		
		row++;
		col = 0;
	}

	private void configurarDialog(GridPane grid) 
	{
		ButtonType btnSalvar = new ButtonType("Salvar", ButtonData.OK_DONE);
		
		dialog.getDialogPane().setPrefWidth(1050);
		dialog.getDialogPane().setContent(grid);
		dialog.getDialogPane().getStylesheets().add("Custom.css");
		dialog.getDialogPane().getButtonTypes().addAll(btnSalvar, ButtonType.CANCEL);
		dialog.setTitle("Editar filme - "+filme.getNomeFilme());
		
		dialog.setResultConverter(dialogButton -> 
		{
		    if (dialogButton == btnSalvar) 
		    {
		    	try 
		    	{
		    		setPropriedadesFilme();
			        return this.filme;
				}
		    	catch (Exception e) 
		    	{
					e.printStackTrace();
				}
		    }
		    
		    return null;
		});
	}
	
	private SortedSet<String> getGeneros() throws IOException, FileNotFoundException 
	{
		SortedSet<String> generos = new TreeSet<String>();
        Properties pgeneros = new Properties();
        pgeneros.load(new FileInputStream(new File(Constantes.LOCAL_PROPERTS_BASE, Constantes.PROPERTIES_GENEROS)));
        
        for(Object key : pgeneros.keySet())
        {
        	generos.add(pgeneros.getProperty(key.toString()));
        }
		return generos;
	}

	private ImageView criarVisualizadorImagem(String imagem) 
	{
		ImageView visualizadorImagem = new ImageView();
		
		visualizadorImagem.setFitWidth(380);
		visualizadorImagem.setFitHeight(500);
		visualizadorImagem.setSmooth(true);
		visualizadorImagem.setCache(true);
		
		if(imagem != null)
		{
			visualizadorImagem.setImage(new Image(Paths.get(imagem).toAbsolutePath().toUri().toString()));
		}
		
		return(visualizadorImagem);
	}

	private void setPropriedadesFilme() throws Exception
	{
		filme.setNomeCapa(copiarImagemSelecionada(new File(enderecoCapa)).getName());
		filme.setAssistido(tgbAssistido.isSelected());
		filme.setClassificacao(txtClassificacao.getText());
		filme.setDuracao(txtDuracao.getText());
		filme.setGenero(cbGeneros.getSelectionModel().getSelectedItem());
		filme.setIdFilme(txtIdFilme.getText());
		filme.setLocalArmazenamento(txtLocalArmazenamento.getText());
		filme.setNomeFilme(txtNomeFilme.getText());
		filme.setSinopse(txaSinopseFilme.getText());
		filme.setTamanho(txtTamanho.getText());
		
		filme.getPropriedades().put(PropriedadesFilme.ANO_LANCAMENTO, txtAnoLancamento.getText());
		filme.getPropriedades().put(PropriedadesFilme.DUAL_AUDIO, String.valueOf(tgbDualAudio.isSelected()));
		filme.getPropriedades().put(PropriedadesFilme.EXTENSAO, txtExtensao.getText());
		filme.getPropriedades().put(PropriedadesFilme.FORMATO_ARQUIVO, txtFormatoArquivo.getText());
		filme.getPropriedades().put(PropriedadesFilme.FORMATO_AUDIO, txtFormatoArquivoAudio.getText());
		filme.getPropriedades().put(PropriedadesFilme.IDIOMA, cbIdioma.getSelectionModel().getSelectedItem());
		filme.getPropriedades().put(PropriedadesFilme.IDIOMA_LEGENDAS, cbIdiomaLegenda.getSelectionModel().getSelectedItem());
		filme.getPropriedades().put(PropriedadesFilme.LEGENDAS, String.valueOf(tgbLegendas.isSelected()));
		filme.getPropriedades().put(PropriedadesFilme.QUANTIDADE_AUDIOS, spnQuantidadeAudios.getValue().toString());
		filme.getPropriedades().put(PropriedadesFilme.QUANTIDADE_LEGENDAS, spnQuantidadeLegendas.getValue().toString());
		filme.getPropriedades().put(PropriedadesFilme.RESOLUCAO, txtResolucao.getText());
	}

	private File copiarImagemSelecionada(File fileImageSource) throws Exception 
	{
		File fileImageTarget = new File(Constantes.LOCAL_IMAGE_BASE, filme.getNomeFilme()+fileImageSource.getName().substring(fileImageSource.getName().lastIndexOf(".")));
		
		if(!fileImageSource.getAbsolutePath().equalsIgnoreCase(fileImageTarget.getAbsolutePath()))
		{
			if(fileImageTarget.exists())
			{
				if(fileImageTarget.delete())
				{
					FileUtils.copyFileByFileChannel(fileImageSource, fileImageTarget);
				}
				else
				{
					throw new Exception("Não foi possível deletar a imagem no destino");
				}
			}
			else
			{
				FileUtils.copyFileByFileChannel(fileImageSource, fileImageTarget);
			}
		}
		
		return fileImageTarget;
	}
	
	private void iniciarValoresComponentes() throws Exception 
	{
		SortedSet<String> idiomas = CommandToolsUtils.getIdiomarDisponiveis();
		
		txtNomeFilme.setText(filme.getNomeFilme());
		txtClassificacao.setText(filme.getClassificacao());
		txtDuracao.setText(filme.getDuracao());
		cbGeneros.getSelectionModel().select(filme.getGenero());
		txtIdFilme.setText(filme.getIdFilme());
		txtLocalArmazenamento.setText(filme.getLocalArmazenamento());
		txaSinopseFilme.setText(filme.getSinopse());
		txtTamanho.setText(filme.getTamanho());
		
		txtAnoLancamento.setText(filme.getPropriedades().get(PropriedadesFilme.ANO_LANCAMENTO));
		txtExtensao.setText(filme.getPropriedades().get(PropriedadesFilme.EXTENSAO));
		txtFormatoArquivo.setText(filme.getPropriedades().get(PropriedadesFilme.FORMATO_ARQUIVO));
		txtFormatoArquivoAudio.setText(filme.getPropriedades().get(PropriedadesFilme.FORMATO_AUDIO));
		cbIdioma.getSelectionModel().select(filme.getPropriedades().get(PropriedadesFilme.IDIOMA));
		
		cbIdiomaLegenda.getSelectionModel().select(filme.getPropriedades().get(PropriedadesFilme.IDIOMA_LEGENDAS));
		
		spnQuantidadeAudios.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 5, Integer.parseInt(filme.getPropriedades().get(PropriedadesFilme.QUANTIDADE_AUDIOS))));
		spnQuantidadeAudios.getStyleClass().add(Spinner.STYLE_CLASS_SPLIT_ARROWS_HORIZONTAL);
		spnQuantidadeLegendas.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 5, Integer.parseInt(filme.getPropriedades().get(PropriedadesFilme.QUANTIDADE_LEGENDAS))));
		spnQuantidadeLegendas.getStyleClass().add(Spinner.STYLE_CLASS_SPLIT_ARROWS_HORIZONTAL);
		
		txtResolucao.setText(filme.getPropriedades().get(PropriedadesFilme.RESOLUCAO));
		
		tgbDualAudio.setSelected(Boolean.parseBoolean(filme.getPropriedades().get(PropriedadesFilme.DUAL_AUDIO)));
		tgbLegendas.setSelected(Boolean.parseBoolean(filme.getPropriedades().get(PropriedadesFilme.LEGENDAS)));
		tgbAssistido.setSelected(filme.isAssistido());
        
        cbIdioma.setItems(FXCollections.observableArrayList(idiomas));
        cbIdiomaLegenda.setItems(FXCollections.observableArrayList(idiomas));
        cbGeneros.setItems(FXCollections.observableArrayList(getGeneros()));
	}
	
	private void addPropriedadesEspecificas(GridPane gridPropriedadesEspecificas)
	{
		int row = 0;
		int col = 0;
		
		gridPropriedadesEspecificas.add(new Label("Idioma"), col++, row);
		gridPropriedadesEspecificas.add(cbIdioma, col++, row);
		gridPropriedadesEspecificas.add(new Label("Ano lançamento"), col++, row);
		gridPropriedadesEspecificas.add(txtAnoLancamento, col++, row);
		
		row++;
		col = 0;

		gridPropriedadesEspecificas.add(new Label("Dual Audio"), col++, row);
		gridPropriedadesEspecificas.add(tgbDualAudio, col++, row);
		gridPropriedadesEspecificas.add(new Label("Extensão"), col++, row);
		gridPropriedadesEspecificas.add(txtExtensao, col++, row);
		
		row++;
		col = 0;

		gridPropriedadesEspecificas.add(new Label("Formato arquivo"), col++, row);
		gridPropriedadesEspecificas.add(txtFormatoArquivo, col++, row);
		gridPropriedadesEspecificas.add(new Label("Formato arquivo áudio"), col++, row);
		gridPropriedadesEspecificas.add(txtFormatoArquivoAudio, col++, row);
		
		row++;
		col = 0;

		gridPropriedadesEspecificas.add(new Label("Legenda"), col++, row);
		gridPropriedadesEspecificas.add(tgbLegendas, col++, row);
		gridPropriedadesEspecificas.add(new Label("Idioma da legenda"), col++, row);
		gridPropriedadesEspecificas.add(cbIdiomaLegenda, col++, row);
		
		row++;
		col = 0;

		gridPropriedadesEspecificas.add(new Label("Resolução"), col++, row);
		gridPropriedadesEspecificas.add(txtResolucao, col++, row);
		gridPropriedadesEspecificas.add(new Label("Quantidade de áudio"), col++, row);
		gridPropriedadesEspecificas.add(spnQuantidadeAudios, col++, row);
		
		row++;
		col = 0;

		gridPropriedadesEspecificas.add(new Label("Quantidade de legendas"), col++, row);
		gridPropriedadesEspecificas.add(spnQuantidadeLegendas, col++, row);
		
		row++;
		col = 0;
	}

	public Dialog<FilmeDTO> getDialog()
	{
		return(dialog);
	}
}
