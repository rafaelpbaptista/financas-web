package br.com.caelum.financas.bean;

import java.io.Serializable;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.persistence.EntityManager;

import br.com.caelum.financas.dao.ContaDAO;
import br.com.caelum.financas.dao.MovimentacaoDAO;
import br.com.caelum.financas.modelo.Conta;
import br.com.caelum.financas.modelo.Movimentacao;
import br.com.caelum.financas.util.JPAUtil;

@ViewScoped
@ManagedBean
public class MovimentacaoBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private Movimentacao movimentacao = new Movimentacao();
	private List<Movimentacao> movimentacoes;
	private List<Conta> contas;
	private Integer idConta;
	private Conta contaSelecionada;

	public Movimentacao getMovimentacao() {
		return movimentacao;
	}

	public void setMovimentacao(Movimentacao movimentacao) {
		this.movimentacao = movimentacao;
	}

	public List<Movimentacao> getMovimentacoes() {
		EntityManager em = new JPAUtil().getEntityManager();
		this.movimentacoes = new MovimentacaoDAO(em)
				.listaTodasMovimentacoes(contaSelecionada);
		return movimentacoes;
	}

	public List<Conta> getContas() {

		if (contas == null) {
			EntityManager em = new JPAUtil().getEntityManager();
			ContaDAO dao = new ContaDAO(em);
			contas = dao.lista();
			em.close();
		}

		return contas;
	}

	public Integer getIdConta() {
		return idConta;
	}

	public void setIdConta(Integer idConta) {
		this.idConta = idConta;
	}

	public void gravar() {
		System.out.println("Salvando movimentacao.");
	}

	public Conta getContaSelecionada() {
		return contaSelecionada;
	}

	public void setContaSelecionada(Conta contaSelecionada) {
		this.contaSelecionada = contaSelecionada;
	}

}
