package commandtool.telaprocessarmkv.dados.dtos;

import java.io.Serializable;

public class TrilhasDTO implements Serializable 
{
	private static final long serialVersionUID = 1L;
	
	private String nomeMedia;
	private String idMedia;
	private boolean isProcessar;
	private String idLinguagem;
	
	public String getNomeMedia() 
	{
		return nomeMedia;
	}
	
	public void setNomeMedia(String nomeMedia) 
	{
		this.nomeMedia = nomeMedia;
	}
	
	public String getIdMedia() 
	{
		return idMedia;
	}
	
	public void setIdMedia(String idMedia) 
	{
		this.idMedia = idMedia;
	}

	public boolean isProcessar() {
		return isProcessar;
	}

	public void setProcessar(boolean isProcessar) {
		this.isProcessar = isProcessar;
	}

	@Override
	public String toString() 
	{
		return nomeMedia;
	}

	public String getIdLinguagem() {
		return idLinguagem;
	}

	public void setIdLinguagem(String idLinguagem) {
		this.idLinguagem = idLinguagem;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((idLinguagem == null) ? 0 : idLinguagem.hashCode());
		result = prime * result + ((idMedia == null) ? 0 : idMedia.hashCode());
		result = prime * result + (isProcessar ? 1231 : 1237);
		result = prime * result + ((nomeMedia == null) ? 0 : nomeMedia.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TrilhasDTO other = (TrilhasDTO) obj;
		if (idLinguagem == null) {
			if (other.idLinguagem != null)
				return false;
		} else if (!idLinguagem.equals(other.idLinguagem))
			return false;
		if (idMedia == null) {
			if (other.idMedia != null)
				return false;
		} else if (!idMedia.equals(other.idMedia))
			return false;
		if (isProcessar != other.isProcessar)
			return false;
		if (nomeMedia == null) {
			if (other.nomeMedia != null)
				return false;
		} else if (!nomeMedia.equals(other.nomeMedia))
			return false;
		return true;
	}
}
