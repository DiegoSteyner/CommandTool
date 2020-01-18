package commandtool.telafilmejson.dados.dto;

import java.io.Serializable;
import java.util.Map;
import java.util.TreeMap;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import commandtool.telafilmejson.dados.enums.PropriedadesFilme;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

@JsonIgnoreProperties(value = {"isDuplicated", "toExport", "loadedFromJson"})
public class FilmeDTO implements Serializable{

	private static final long serialVersionUID = 1L;

	private String idFilme;
	private String localArmazenamento;
	private String nomeFilme;
	private String genero;
	private String sinopse;
	private String classificacao;
	private String tamanho;
	private String duracao;
	private boolean assistido;
	private String nomeCapa;
	private Map<PropriedadesFilme, String> propriedades = new TreeMap<>();
	
	private boolean isDuplicated;
	private boolean temCapa;
	
	public BooleanProperty toExport = new SimpleBooleanProperty(false);
	public BooleanProperty loadedFromJson = new SimpleBooleanProperty(false);
	
	public BooleanProperty toExportProperty() {
		return toExport;
	}

	public boolean isToExport() {
		return toExport.get();
	}

	public BooleanProperty getToExport() {
		return toExport;
	}

	@JsonIgnore
	public void setToExport(BooleanProperty toProcess) {
		this.toExport = toProcess;
	}
	
	public void setToExport(boolean toProcess) {
		this.toExport.set(toProcess);
	}
	
	public boolean isAssistido() {
		return assistido;
	}
	
	public void setAssistido(boolean assistido) {
		this.assistido = assistido;
	}

	public String getIdFilme() {
		return idFilme;
	}

	public void setIdFilme(String idFilme) {
		this.idFilme = idFilme;
	}

	public String getLocalArmazenamento() {
		return localArmazenamento;
	}

	public void setLocalArmazenamento(String localArmazenamento) {
		this.localArmazenamento = localArmazenamento;
	}

	public String getNomeFilme() {
		return nomeFilme;
	}

	public void setNomeFilme(String nomeFilme) {
		this.nomeFilme = nomeFilme;
	}

	public String getGenero() {
		return genero;
	}

	public void setGenero(String genero) {
		this.genero = genero;
	}

	public String getSinopse() {
		return sinopse;
	}

	public void setSinopse(String sinopse) {
		this.sinopse = sinopse;
	}

	public String getClassificacao() {
		return classificacao;
	}

	public void setClassificacao(String classificacao) {
		this.classificacao = classificacao;
	}

	public String getTamanho() {
		return tamanho;
	}

	public void setTamanho(String tamanho) {
		this.tamanho = tamanho;
	}

	public String getDuracao() {
		return duracao;
	}

	public void setDuracao(String duracao) {
		this.duracao = duracao;
	}

	public Map<PropriedadesFilme, String> getPropriedades() {
		return propriedades;
	}

	public void setPropriedades(Map<PropriedadesFilme, String> propriedades) {
		this.propriedades = propriedades;
	}

	public boolean isDuplicated() {
		return isDuplicated;
	}

	public void setDuplicated(boolean isDuplicated) {
		this.isDuplicated = isDuplicated;
	}

	public boolean isTemCapa() {
		return temCapa;
	}

	public void setTemCapa(boolean temCapa) {
		this.temCapa = temCapa;
	}

	public BooleanProperty getLoadedFromJson() {
		return loadedFromJson;
	}

	public void setLoadedFromJson(BooleanProperty loadedFromJson) {
		this.loadedFromJson = loadedFromJson;
	}

	public String getNomeCapa() {
		return nomeCapa;
	}

	public void setNomeCapa(String nomeCapa) {
		this.nomeCapa = nomeCapa;
	}
}
