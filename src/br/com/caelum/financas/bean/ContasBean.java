package br.com.caelum.financas.bean;

import java.io.Serializable;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.persistence.EntityManager;

import br.com.caelum.financas.dao.ContaDAO;
import br.com.caelum.financas.modelo.Conta;
import br.com.caelum.financas.util.JPAUtil;

@ViewScoped
@ManagedBean
public class ContasBean implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Conta conta = new Conta();
	private List<Conta> contas;

	public void gravar() {
		System.out.println("Gravando a conta");

		EntityManager em = new JPAUtil().getEntityManager();
		em.getTransaction().begin();
		ContaDAO dao = new ContaDAO(em);

		if (this.conta.getId() != null) {
			dao.altera(conta);
		} else {
			dao.adiciona(conta);
		}

		contas = dao.lista();
		conta = new Conta();

		em.getTransaction().commit();
		em.close();

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

}
