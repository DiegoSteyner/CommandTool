package commandtool.telaprocessarmkv.dados.dtos;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

public class ArquivoMkvDTO implements Serializable 
{
	private static final long serialVersionUID = 1L;
	
	private File file;
	private String videoId;
	private String nome;
	private String tamanho;
	private String formato;
	private String command;
	private String diretorioDestino;
	private ArrayList<TrilhasDTO> audios;
	private ArrayList<TrilhasDTO> legendas;
	public BooleanProperty toProcess = new SimpleBooleanProperty(false);
	
	public File getFile() {
		return file;
	}
	
	public void setFile(File file) {
		this.file = file;
	}
	
	public String getNome() {
		return nome;
	}
	
	public void setNome(String nome) {
		this.nome = nome;
	}
	
	public ArrayList<TrilhasDTO> getAudios() {
		return audios;
	}

	public void setAudios(ArrayList<TrilhasDTO> audios) {
		this.audios = audios;
	}

	public ArrayList<TrilhasDTO> getLegendas() {
		return legendas;
	}

	public void setLegendas(ArrayList<TrilhasDTO> legendas) {
		this.legendas = legendas;
	}
	
	public BooleanProperty toProcessProperty() {
		return toProcess;
	}

	public boolean isToProcess() {
		return toProcess.get();
	}

	public void setToProcess(boolean toProcess) {
		this.toProcess.set(toProcess);
	}

	public BooleanProperty getToProcess() {
		return toProcess;
	}

	public void setToProcess(BooleanProperty toProcess) {
		this.toProcess = toProcess;
	}

	public String getTamanho() {
		return tamanho;
	}

	public void setTamanho(String tamanho) {
		this.tamanho = tamanho;
	}

	public String getFormato() {
		return formato;
	}

	public void setFormato(String formato) {
		this.formato = formato;
	}

	public String getCommand() {
		return command;
	}

	public void setCommand(String command) {
		this.command = command;
	}

	public String getVideoId() {
		return videoId;
	}

	public void setVideoId(String videoId) {
		this.videoId = videoId;
	}

	public String getDiretorioDestino() {
		return diretorioDestino;
	}

	public void setDiretorioDestino(String diretorioDestino) {
		this.diretorioDestino = diretorioDestino;
	}
}
