package commandtool.telaprocessarmkv.dados.dtos;

import java.io.File;

public class TrilhaLegendaDTO extends TrilhasDTO
{
	private static final long serialVersionUID = 1L;
	private File enderecoLegenda = null;
	
	public TrilhaLegendaDTO(String name) 
	{
		setNomeMedia(name);
	}

	public File getEnderecoLegenda() {
		return enderecoLegenda;
	}

	public void setEnderecoLegenda(File enderecoLegenda) {
		this.enderecoLegenda = enderecoLegenda;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((enderecoLegenda == null) ? 0 : enderecoLegenda.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		TrilhaLegendaDTO other = (TrilhaLegendaDTO) obj;
		if (enderecoLegenda == null) {
			if (other.enderecoLegenda != null)
				return false;
		} else if (!enderecoLegenda.equals(other.enderecoLegenda))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return getNomeMedia();
	}
}
