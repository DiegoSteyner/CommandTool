package commandtool.telafilmejson.dados.dto;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Estatistica implements Serializable 
{
	private static final long serialVersionUID = 1L;

	private Map<String, String> espacoHds = new HashMap<>();
	private Map<String, String> quantidadeFilmesHds = new HashMap<>();
	private int quantidadeFilmes;
	
	public Estatistica() 
	{
	}
	
	public void addFilme(int qtd)
	{
		quantidadeFilmes = quantidadeFilmes + qtd;
	}
	
	public void addEspacoHd(String nomeHd, String espaco)
	{
		compute(nomeHd, espaco, espacoHds);
	}

	public void addQuantidadeFilmes(String nomeHd, String espaco)
	{
		compute(nomeHd, espaco, quantidadeFilmesHds);
	}

	private void compute(String nomeHd, String espaco, Map<String, String> map)
	{
		Long total = 0L;
		
		if(map.containsKey(nomeHd))
		{
			total = Long.parseLong(map.get(nomeHd));
		}
		
		map.put(nomeHd, String.valueOf((Long.parseLong(espaco)+total)));
	}

	public Map<String, String> getEspacoHds() {
		return espacoHds;
	}

	public void setEspacoHds(Map<String, String> espacoHds) {
		this.espacoHds = espacoHds;
	}

	public Map<String, String> getQuantidadeFilmesHds() {
		return quantidadeFilmesHds;
	}

	public void setQuantidadeFilmesHds(Map<String, String> quantidadeFilmesHds) {
		this.quantidadeFilmesHds = quantidadeFilmesHds;
	}

	public int getQuantidadeFilmes() {
		return quantidadeFilmes;
	}

	public void setQuantidadeFilmes(int quantidadeFilmes) {
		this.quantidadeFilmes = quantidadeFilmes;
	}
}
