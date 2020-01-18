package commandtool.mkvtoolnix;

import java.io.File;

public class MkvTrack 
{
	int id;
	String nome;
	String linguagemId;
	boolean padrao;
	boolean forcada;
	private File file; 
	
	public MkvTrack(int id, String nome) 
	{
		this.id = id;
		this.nome = nome;
	}

	public MkvTrack(int id, String nome, String linguagemId, boolean padrao, boolean forcada) {
		super();
		this.id = id;
		this.nome = nome;
		this.linguagemId = linguagemId;
		this.padrao = padrao;
		this.forcada = forcada;
	}

	public MkvTrack(int id, String nome, String linguagemId, boolean padrao, boolean forcada, File file) 
	{
		super();
		this.id = id;
		this.nome = nome;
		this.linguagemId = linguagemId;
		this.padrao = padrao;
		this.forcada = forcada;
		this.file = file;
	}

	public String getId() {
		return String.valueOf(id);
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public boolean isPadrao() {
		return padrao;
	}

	public void setPadrao(boolean padrao) {
		this.padrao = padrao;
	}

	public boolean isForcada() {
		return forcada;
	}

	public void setForcada(boolean forcada) {
		this.forcada = forcada;
	}

	public String getLinguagemId() {
		return linguagemId;
	}

	public void setLinguagemId(String linguagemId) {
		this.linguagemId = linguagemId;
	}

	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
	}
}
