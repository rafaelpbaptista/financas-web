package br.com.caelum.financas.modelo;

public enum TipoMovimentacao {

	ENTRADA, SAIDA;

	public String getNome() {
		return this.name();
	}

}
