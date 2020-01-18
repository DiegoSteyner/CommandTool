package commandtool.telaprocessarmkv.dados.dtos;

public class TrilhaAudioDTO extends TrilhasDTO{

	private static final long serialVersionUID = 1L;
	
	private String duracao;
	
	public TrilhaAudioDTO(String nome) 
	{
		setNomeMedia(nome);
	}

	public String getDuracao() {
		return duracao;
	}

	public void setDuracao(String duracao) {
		this.duracao = duracao;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((duracao == null) ? 0 : duracao.hashCode());
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
		TrilhaAudioDTO other = (TrilhaAudioDTO) obj;
		if (duracao == null) {
			if (other.duracao != null)
				return false;
		} else if (!duracao.equals(other.duracao))
			return false;
		return true;
	}
}
