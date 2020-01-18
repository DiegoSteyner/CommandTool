package commandtool.comum.utils;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.attribute.DosFileAttributes;
import java.util.Optional;
import java.util.function.Supplier;

import javax.swing.filechooser.FileSystemView;

import commandtool.comum.componentes.tabela.callbacks.TableCellAutoNumbered;
import commandtool.comum.componentes.tabela.callbacks.TableCellButtonCallback;
import commandtool.comum.componentes.treeview.EventoExpandirArvore;
import commandtool.comum.dados.Constantes;
import commandtool.comum.dados.CustomItem;
import javafx.beans.value.ChangeListener;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.StageStyle;
import javafx.stage.Window;
import jutil.utils.StringUtils;

public class JavaFxUtils 
{
	public static Window getStageFromNode(Node node)
	{
		Node parent = node;
		
		while(parent.getParent() != null)
		{
			parent = parent.getParent();
		}
		
		if(parent instanceof Pane)
		{
			return(((Pane) parent).getScene().getWindow());
		}
		
		return(null);
	}
	
	public static DropShadow getLineShadow() 
	{
		DropShadow drop = new DropShadow();  
        drop.setBlurType(BlurType.GAUSSIAN);  
        drop.setColor(Color.BLACK);  
        drop.setHeight(100);  
        drop.setWidth(150);  
        drop.setOffsetX(-5);  
        drop.setOffsetY(3);  
        drop.setSpread(0.2);  
        drop.setRadius(5);
        
        return(drop);
	}
	
	public static Alert getLoadingAlert(String title, String information)
	{
		Alert alert = new Alert(AlertType.NONE);
		alert.setTitle(title);
		alert.setHeaderText(null);
		Label lblJson = new Label(information);
		ProgressIndicator indicador = new ProgressIndicator();
		indicador.setPrefSize(30, 30);
		lblJson.setPadding(new Insets(0,6,0,0));
		
		alert.initStyle(StageStyle.UNDECORATED);
		alert.getDialogPane().setStyle(JavaFxUtils.getBorderStyle("black"));
		
		HBox content = new HBox(lblJson, indicador);
		content.setAlignment(Pos.CENTER);
		content.setPrefWidth(250);
	
		content.setPadding(new Insets(35, 0, 0, 0));
		alert.getDialogPane().setContent(content);
		
		alert.getDialogPane().getButtonTypes().add(ButtonType.OK);
		Button ok = ( Button ) alert.getDialogPane().lookupButton( ButtonType.OK );
		ok.setVisible(false);
		
		return(alert);
	}
	
	public static void showAlert(AlertType aletType, String alertTitle, String alertMessage)
	{
		Alert alert = new Alert(aletType);
		alert.setHeaderText(null);
		alert.setTitle(alertTitle);
		alert.setContentText(alertMessage);
		
		alert.showAndWait();
	}
	
	public static Alert createTaskAlert(Task<String> task, String title, Node ownerNode)
	{
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setHeaderText(null);
		alert.setTitle(title);
		
		alert.contentTextProperty().bind(task.messageProperty());
		alert.initStyle(StageStyle.UNDECORATED);
		alert.getDialogPane().setStyle(getBorderStyle("black"));
		alert.initOwner(JavaFxUtils.getStageFromNode(ownerNode));
		Button lookupButton = (Button) alert.getDialogPane().lookupButton(ButtonType.OK);
		
		lookupButton.disableProperty().bind(task.runningProperty());
		
		alert.setOnCloseRequest(e -> 
		{
			if(!task.isDone())
			{
				e.consume();
			}
		});
		
		return(alert);
	}

	public static String showTextInputDialog(String title, String header, String content)
	{
		TextInputDialog dialog = new TextInputDialog();
		dialog.setTitle(title);
		dialog.setHeaderText(header);
		dialog.setContentText(content);

		Optional<String> result = dialog.showAndWait();
		return(result.isPresent() ? result.get().trim().isEmpty() ? "" : result.get() : "");
	}
	
	public static void showTextDialog(AlertType alerType, String tituloJanela, String titulo, String labelInfo, String text)
	{
		Alert alert = new Alert(alerType);
        alert.setTitle(tituloJanela);
        alert.setHeaderText(titulo);
 
        VBox dialogPaneContent = new VBox();
 
        TextArea textArea = new TextArea();
        textArea.setText(text);
 
        dialogPaneContent.getChildren().addAll(textArea);
 
        alert.getDialogPane().setContent(dialogPaneContent);
 
        alert.showAndWait();
	}
	
	public static Optional<File> showFileChooser(Node node, String title, String filterName, String initialLocation, String... extensions) throws Exception
	{
		FileChooser fc = new FileChooser();
		fc.getExtensionFilters().add(new ExtensionFilter(filterName, extensions));
		fc.setTitle(title);
		
		if(StringUtils.isNotNullOrEmptyTrim(initialLocation))
		{
			fc.setInitialDirectory(new File(initialLocation));
		}
		
		return(Optional.ofNullable(fc.showOpenDialog(getStageFromNode(node))));
	}
	
	public static Optional<File> showDirectoryChooser(String title, Node node)
	{
		DirectoryChooser dir = new DirectoryChooser();
		dir.setTitle(title);
		
		return(Optional.ofNullable(dir.showDialog(JavaFxUtils.getStageFromNode(node))));
	}
	
	public static TreeView<CustomItem> criarVisualizacaoDiretorios(ChangeListener<? super TreeItem<CustomItem>> eventoSelecao) throws Exception
	{
		TreeView<CustomItem> arvore = new TreeView<CustomItem>();
		
		File[] unidades = File.listRoots();
		EventoExpandirArvore eventoGeral = new EventoExpandirArvore();
		TreeItem<CustomItem> raiz = new TreeItem<CustomItem>(new CustomItem(new File(""), Constantes.MEU_COMPUTADOR));
		
        raiz.setExpanded(true);
        raiz.addEventHandler(EventType.ROOT, eventoGeral);
        
        adicioneArquivosNaArvore(unidades, raiz);
        
        arvore.setRoot(raiz);
        arvore.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        
        if(eventoSelecao != null)
        {
        	arvore.getSelectionModel().selectedItemProperty().addListener(eventoSelecao);
        }
        
        return(arvore);
	}

	public static void adicioneArquivosNaArvore(File[] arquivos, TreeItem<CustomItem> raiz) throws Exception 
	{
		boolean isMeuComputador = raiz.getValue().getName().equals(Constantes.MEU_COMPUTADOR);
		
		if(isMeuComputador)
		{
			raiz.getChildren().clear();
		}
		
		for(int i = 0; i < arquivos.length ; i++)
        {
			if((arquivos[i].isDirectory() && !Files.readAttributes(arquivos[i].toPath(), DosFileAttributes.class, LinkOption.NOFOLLOW_LINKS).isSystem()) || isMeuComputador)
			{
				String nomeArquivo = FileSystemView.getFileSystemView().getSystemDisplayName (arquivos[i]);
				
				if(StringUtils.isNotNullOrEmptyTrim(nomeArquivo))
				{
					TreeItem<CustomItem> itemArvore = new TreeItem<CustomItem>(new CustomItem(arquivos[i], nomeArquivo));
					itemArvore.getChildren().add(new TreeItem<CustomItem>(new CustomItem(new File(""), "")));
					itemArvore.setExpanded(false);
					
					raiz.getChildren().add(itemArvore);
				}
			}
        }
	}
	
	public static <T> TableColumn<T, Boolean> getTableColumnBoolean(Supplier<EventHandler<ActionEvent>> e, String bindingPropertie)
	{
		CheckBox selectAll = new CheckBox();
		TableColumn<T, Boolean> column = new TableColumn<T, Boolean>();
		
		column.setMinWidth(40);
		column.setMaxWidth(40);
		column.setResizable(false);
		column.setSortable(false);
		column.setGraphic(selectAll);
		column.setCellFactory(CheckBoxTableCell.forTableColumn(column));
		column.setCellValueFactory(new PropertyValueFactory<>(bindingPropertie));
		selectAll.setOnAction(e.get());
		
		return(column);
	}
	
	public static <T> TableColumn<T, String> criarColunaAutoNumerada(Supplier<TableView<T>> supplier)
	{
		TableColumn<T, String> numberCol = new TableColumn<T, String>("#");

		numberCol.setMaxWidth(3500);
		numberCol.setSortable(false);
		numberCol.setCellValueFactory(new TableCellAutoNumbered<T>(supplier));
		numberCol.setSortable(false);
		
		return(numberCol);
	}
	
	public static <T> TableColumn<T, Void> getTableColumnButton(String columnName, String style, Supplier<Node> supplier) 
	{
        TableColumn<T, Void> colBtn = new TableColumn<>(columnName);

        colBtn.setStyle(style);
        colBtn.setCellFactory(new TableCellButtonCallback<T>(supplier));

        return(colBtn);
    }
	
	public static Border getBorder(Color color, BorderStrokeStyle type, int radius, int borderWidth)
	{
		return(new Border(new BorderStroke(color, type, new CornerRadii(radius), new BorderWidths(borderWidth))));
	}
	
	public static String getBorderStyleWidth(int top, int rigth, int bottom, int left)
	{
		return("-fx-border-width: "+top+" "+rigth+" "+bottom+" "+left+";");
	}

	public static String getBorderStyle(String color)
	{
		return("-fx-border-color: "+color+";");
	}

	public static String getBorderStyleColor(String colorTop, String colorRigth, String colorBottom, String colorLeft)
	{
		return("-fx-border-color: "+colorTop+" "+colorRigth+" "+colorBottom+" "+colorLeft+";");
	}

	public static String getBorderStyleDotted()
	{
		return("-fx-border-style: dotted;");
	}
}
