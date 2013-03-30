package br.com.caelum.financas.dao;

import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import br.com.caelum.financas.modelo.Conta;

public class ContaDAO {

	private EntityManager manager;

	@Inject
	public ContaDAO(EntityManager manager) {
		this.manager = manager;
	}

	public void adiciona(Conta t) {
		manager.persist(t);
	}

	public Conta busca(Integer id) {
		return manager.find(Conta.class, id);
	}

	public List<Conta> lista() {
		return manager.createNamedQuery("Conta.todas", Conta.class).getResultList();
	}

	public void remove(Conta t) {
		manager.remove(t);
	}

	public void altera(Conta t) {
		manager.merge(t);
	}

}
