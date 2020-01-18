package commandtool;

import commandtool.comum.utils.CommandToolsUtils;
import commandtool.telaconfiguracao.TelaConfiguracao;
import commandtool.telafilmejson.TelaFilmesParaJson;
import commandtool.telaprocessarmkv.TelaProcessarMkv;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TabPane.TabClosingPolicy;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class TelaPrincipal extends Application 
{
	private StackPane stackPane = new StackPane();
	private TelaFilmesParaJson jsonView = new TelaFilmesParaJson();
	private TelaConfiguracao conf = new TelaConfiguracao();
	private TelaProcessarMkv media = new TelaProcessarMkv();
	
	@Override
	public void start(Stage stage) throws Exception 
	{
		setStageData(stage);
		
		TabPane tabpane = new TabPane();
		
		tabpane.setTabClosingPolicy(TabClosingPolicy.UNAVAILABLE);
		tabpane.getTabs().add(createTabExportJson());
		tabpane.getTabs().add(createTabProcessar());
		tabpane.getTabs().add(createTabConfiguration());
		
        tabpane.getSelectionModel().select(0);
        
        stackPane.getChildren().add(tabpane);
        
		stage.setScene(new Scene(stackPane));
		stage.show();
	}
	
	public void setStageData(Stage stage) 
	{
		stage.setTitle(CommandToolsUtils.getBoundleValue("appTitle"));
		stage.setWidth(1300);
	}

	private Tab createTabExportJson() 
	{
		Tab abaExportJson = new Tab(CommandToolsUtils.getBoundleValue("nameTab01"));
		abaExportJson.setContent(jsonView.getPrincipalNode());
		return abaExportJson;
	}
	
	private Tab createTabConfiguration() {
		Tab configuration = new Tab(CommandToolsUtils.getBoundleValue("nameTab02"));
		configuration.setContent(conf.getPrincipalNode());
		return configuration;
	}
	
	protected Tab createTabEstatistica() {
		Tab estatistica = new Tab(CommandToolsUtils.getBoundleValue("nameTab03"));
		return estatistica;
	}

	private Tab createTabProcessar() 
	{
		Tab processar = new Tab(CommandToolsUtils.getBoundleValue("nameTab04"));
		processar.setContent(media.getPrincipalNode());
		return processar;
	}
}
