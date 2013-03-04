package br.com.caelum.financas.bean;

import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.persistence.EntityManager;

import br.com.caelum.financas.dao.ContaDAO;
import br.com.caelum.financas.modelo.Conta;
import br.com.caelum.financas.util.JPAUtil;

@ViewScoped
@ManagedBean
public class ContasBean {
	private Conta conta = new Conta();
	private List<Conta> contas;

	public void gravar() {
		System.out.println("Gravando a conta");

		EntityManager em = new JPAUtil().getEntityManager();
		em.getTransaction().begin();
		ContaDAO dao = new ContaDAO(em);

		// Bastaria usar o metodo merge no lugar do persist
		if (this.conta.getId() != null) {
			dao.altera(conta);
		} else {
			dao.adiciona(conta);
		}
		contas = dao.lista();

		em.getTransaction().commit();
		em.close();

		conta = new Conta();

	}

	public void remove() {
		System.out.println("Removendo a conta");

		EntityManager em = new JPAUtil().getEntityManager();
		em.getTransaction().begin();

		ContaDAO dao = new ContaDAO(em);
		Conta contaParaRemover = dao.busca(this.conta.getId());

		dao.remove(contaParaRemover);
		contas = dao.lista();

		em.getTransaction().commit();
		em.close();

		limpaFormularioDoJSF();

	}

	public Conta getConta() {
		return conta;
	}

	public void setConta(Conta conta) {
		this.conta = conta;
	}

	public List<Conta> getContas() {
		System.out.println("Listando as contas");

		if (contas == null) {
			EntityManager em = new JPAUtil().getEntityManager();
			ContaDAO dao = new ContaDAO(em);
			contas = dao.lista();
			em.close();
		}

		return contas;
	}

	/**
	 * Esse m??todo apenas limpa o formul??rio da forma com que o JSF espera.
	 * Invoque-o no momento em que precisar do formul??rio vazio.
	 */
	private void limpaFormularioDoJSF() {
		this.conta = new Conta();
	}
}
