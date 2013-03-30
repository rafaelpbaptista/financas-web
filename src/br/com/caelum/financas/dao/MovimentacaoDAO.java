package br.com.caelum.financas.dao;

import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import br.com.caelum.financas.modelo.Conta;
import br.com.caelum.financas.modelo.Movimentacao;

public class MovimentacaoDAO {

	private final EntityManager manager;

	@Inject
	public MovimentacaoDAO(EntityManager manager) {
		this.manager = manager;
	}

	public void adiciona(Movimentacao t) {
		manager.persist(t);
	}

	public Movimentacao busca(Integer id) {
		return manager.find(Movimentacao.class, id);
	}

	public List<Movimentacao> lista() {
		return manager.createNamedQuery("Movimentacao.todos", Movimentacao.class).getResultList();
	}

	public void remove(Movimentacao t) {
		manager.remove(t);
	}

	public List<Movimentacao> listaTodasMovimentacoes(Conta conta) {

		String jpql = "select m from Movimentacao m "
				+ "where m.conta = :pConta order by m.valor desc";

		TypedQuery<Movimentacao> query = this.manager.createQuery(jpql, Movimentacao.class);
		query.setParameter("pConta", conta);
		return query.getResultList();

	}

}
