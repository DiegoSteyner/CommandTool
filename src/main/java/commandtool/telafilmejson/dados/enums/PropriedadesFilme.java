package commandtool.telafilmejson.dados.enums;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;
import com.fasterxml.jackson.annotation.JsonValue;

@JsonFormat(shape = Shape.OBJECT)
public enum PropriedadesFilme 
{
	IDIOMA("Idioma"),
	ANO_LANCAMENTO("AnoLancamento"),
	DUAL_AUDIO("DualAudio"),
	EXTENSAO("Extensao"),
	FORMATO_ARQUIVO("FormatoArquivo"),
	FORMATO_AUDIO("FormatoAudio"),
	LEGENDAS("Legendas"),
	IDIOMA_LEGENDAS("IdiomaLegendas"),
	RESOLUCAO("Resolucao"),
	QUANTIDADE_AUDIOS("QuantidadeAudios"),
	QUANTIDADE_LEGENDAS("QuantidadeLegendas"),
	TAMANHO_BYTES("TamanhoByte"),
	;

	private String nomePropriedade;

	private PropriedadesFilme(String nomePropriedade) 
	{
		this.nomePropriedade = nomePropriedade;
	}

	@JsonValue
	public String getNomePropriedade() {
		return nomePropriedade;
	}

	public void setNomePropriedade(String nomePropriedade) {
		this.nomePropriedade = nomePropriedade;
	}
	
}
