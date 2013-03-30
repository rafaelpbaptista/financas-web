package br.com.caelum.financas.bean;

import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.caelum.financas.dao.ContaDAO;
import br.com.caelum.financas.dao.MovimentacaoDAO;
import br.com.caelum.financas.modelo.Conta;
import br.com.caelum.financas.modelo.Movimentacao;
import br.com.caelum.financas.modelo.TipoMovimentacao;
import br.com.caelum.financas.transaction.Transactional;

@Named
@RequestScoped
public class MovimentacaoBean {

	private Movimentacao movimentacao = new Movimentacao();
	private List<Conta> contas;
	private Integer idConta;
	private Conta contaSelecionada;

	@Inject
	MovimentacaoDAO movimentacaoDao;

	@Inject
	ContaDAO contaDao;

	@Transactional
	public void gravar() {

		Conta conta = contaDao.busca(idConta);
		this.movimentacao.setConta(conta);

		movimentacaoDao.adiciona(this.movimentacao);

		criaMensagemDeSucesso();
		limpaFormulario();
	}

	private void limpaFormulario() {
		this.movimentacao = new Movimentacao();
	}

	private void criaMensagemDeSucesso() {
		FacesContext.getCurrentInstance().addMessage("messages",
				new FacesMessage("Movimentação adicionada"));
	}

	public Movimentacao getMovimentacao() {
		return movimentacao;
	}

	public List<Conta> getContas() {

		if (this.contas == null) {
			contas = contaDao.lista();
		}
		return contas;
	}

	public Integer getIdConta() {
		return idConta;
	}

	public void setIdConta(Integer idConta) {
		this.idConta = idConta;
	}

	public Conta getContaSelecionada() {
		return contaSelecionada;
	}

	public void setContaSelecionada(Conta contaSelecionada) {
		this.contaSelecionada = contaSelecionada;
	}

	public SelectItem[] getTipos() {
		SelectItem[] items = new SelectItem[TipoMovimentacao.values().length];
		int i = 0;
		for (TipoMovimentacao tipo : TipoMovimentacao.values()) {
			items[i++] = new SelectItem(tipo, tipo.name());
		}
		return items;
	}

}
