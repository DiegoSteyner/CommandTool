package commandtool.telafilmejson.dados.dto;

import java.util.function.BiFunction;

import commandtool.telafilmejson.dados.enums.PropriedadesFilme;

public class FiltroDTO 
{
	private String nomeFiltro;
	private PropriedadesFilme propriedade;
	private BiFunction<FilmeDTO, String, Boolean> condicao;
	
	public FiltroDTO(String nomeFiltro, PropriedadesFilme propriedade, BiFunction<FilmeDTO, String, Boolean> condicao) 
	{
		super();
		this.nomeFiltro = nomeFiltro;
		this.propriedade = propriedade;
		this.condicao = condicao;
	}

	public String getNomeFiltro() {
		return nomeFiltro;
	}

	public void setNomeFiltro(String nomeFiltro) {
		this.nomeFiltro = nomeFiltro;
	}

	public BiFunction<FilmeDTO, String, Boolean> getCondicao() {
		return condicao;
	}

	public void setCondicao(BiFunction<FilmeDTO, String, Boolean> condicao) {
		this.condicao = condicao;
	}
	
	public PropriedadesFilme getPropriedade() {
		return propriedade;
	}

	public void setPropriedade(PropriedadesFilme propriedade) {
		this.propriedade = propriedade;
	}
	
	@Override
	public String toString() 
	{
		return nomeFiltro;
	}
}
