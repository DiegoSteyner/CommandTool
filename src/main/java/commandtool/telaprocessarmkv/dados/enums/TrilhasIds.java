package commandtool.telaprocessarmkv.dados.enums;

public enum TrilhasIds 
{
	MANTER_TODAS_TRILHAS("Manter Todas"),
	LEGENDA_FORCADA("Português forçada"),
	REMOVER_TODAS_TRILHAS("Remover Todas");

	String titulo;

	private TrilhasIds(String titulo) {
		this.titulo = titulo;
	}

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}
}
