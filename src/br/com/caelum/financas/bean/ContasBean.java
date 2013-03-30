package br.com.caelum.financas.bean;

import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.caelum.financas.dao.ContaDAO;
import br.com.caelum.financas.modelo.Conta;
import br.com.caelum.financas.transaction.Transactional;

@Named
@RequestScoped
public class ContasBean {

	private Conta conta = new Conta();
	private List<Conta> contas;

	@Inject
	ContaDAO contaDao;

	@Transactional
	public void gravar() {

		if (this.conta.getId() != null) {
			contaDao.altera(conta);
		} else {
			contaDao.adiciona(conta);
		}

		contas = contaDao.lista();
		conta = new Conta();
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
			contas = contaDao.lista();
		}

		return contas;
	}

}
