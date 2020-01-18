package commandtool.telafilmejson.dados.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import commandtool.comum.dados.Constantes;
import jutil.utils.StringUtils;

public class ListaDeFilmesDTO implements Serializable{

	private static final long serialVersionUID = 1L;

	private Map<String, ArrayList<FilmeDTO>> lista = new HashMap<String, ArrayList<FilmeDTO>>();
	
	public ListaDeFilmesDTO() 
	{
		lista.put(Constantes.FILM_LIST_JSON_KEY, new ArrayList<FilmeDTO>());
	}
	
	public void adicioneFilmeNaLista(FilmeDTO filme)
	{
		this.lista.get(Constantes.FILM_LIST_JSON_KEY).add(filme);
	}
	
	public Map<String, ArrayList<FilmeDTO>> cloneFilmesNaLista()
	{
		Map<String, ArrayList<FilmeDTO>> r = new HashMap<>();
		r.put(Constantes.FILM_LIST_JSON_KEY, new ArrayList<FilmeDTO>(lista.get(Constantes.FILM_LIST_JSON_KEY)));
		
		Collections.sort(r.get(Constantes.FILM_LIST_JSON_KEY), (a,b) -> {
			
			try 
			{
				if(StringUtils.isNotNullOrEmptyTrim(a.getIdFilme(), b.getIdFilme()))
				{
					return(a.getIdFilme().compareTo(b.getIdFilme()));
				}
			}
			catch (Exception e) 
			{
				e.printStackTrace();
			}
			
			return(0);
		});
		
		return(r);
	}
	
	public ArrayList<FilmeDTO> getFilmesNaLista()
	{
		return(lista.get(Constantes.FILM_LIST_JSON_KEY));
	}
	
	public void setNovaLista(Map<String, ArrayList<FilmeDTO>> list)
	{
		this.lista = new HashMap<>(list);
	}
}
