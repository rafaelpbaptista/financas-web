package br.com.caelum.financas.dao;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import br.com.caelum.financas.modelo.Conta;
import br.com.caelum.financas.modelo.Movimentacao;
import br.com.caelum.financas.modelo.TipoMovimentacao;

public class MovimentacaoDAO {

	private final DAO<Movimentacao> dao;
	private final EntityManager em;

	public MovimentacaoDAO(EntityManager em) {
		this.em = em;
		dao = new DAO<Movimentacao>(em, Movimentacao.class);
	}

	public void adiciona(Movimentacao t) {
		dao.adiciona(t);
	}

	public Movimentacao busca(Integer id) {
		return dao.busca(id);
	}

	public List<Movimentacao> lista() {
		return dao.lista();
	}

	public void remove(Movimentacao t) {
		dao.remove(t);
	}

	public List<Movimentacao> listaTodasMovimentacoes(Conta conta) {

		String jpql = "select m from Movimentacao m "
				+ "where m.conta = :pConta order by m.valor desc";

		Query query = this.em.createQuery(jpql);
		query.setParameter("pConta", conta);
		return query.getResultList();

	}

	public List<Movimentacao> listaPorValorETipo(BigDecimal valor, TipoMovimentacao tipo) {

		String jpql = "select m from Movimentacao m "
				+ "where m.valor <= :pValor and m.tipoMovimentacao = :pTipoMovimentacao";

		Query query = this.em.createQuery(jpql);
		query.setParameter("pValor", valor);
		query.setParameter("pTipoMovimentacao", tipo);
		
		query.setHint("org.hibernate.cacheable", "true");

		return query.getResultList();

	}

	public BigDecimal calculaTotalMovimentado(Conta conta, TipoMovimentacao tipo) {
		String jpql = "select sum(m.valor) from Movimentacao m "
				+ "where m.conta=:pConta and m.tipoMovimentacao=:pTipoMovimentacao";

		TypedQuery<BigDecimal> query = this.em.createQuery(jpql,
				BigDecimal.class);
		query.setParameter("pConta", conta);
		query.setParameter("pTipoMovimentacao", tipo);
		
		return query.getSingleResult();

	}

	public List<Movimentacao> buscaTodasMovimentacoesDaConta(String titular) {

		String jpql = "select m from Movimentacao m where m.conta.titular like :pTitular";
		TypedQuery<Movimentacao> query = this.em.createQuery(jpql,
				Movimentacao.class);
		query.setParameter("pTitular", "%" + titular + "%");

		return query.getResultList();
	}

	public List<Movimentacao> todasComCriteria() {

		CriteriaBuilder builder = em.getCriteriaBuilder();

		CriteriaQuery<Movimentacao> cq = builder
				.createQuery(Movimentacao.class);
		cq.from(Movimentacao.class);

		return em.createQuery(cq).getResultList();

	}
	
	public BigDecimal somaMovimentacoesDoTitular(String titular){
		
		CriteriaBuilder builder = em.getCriteriaBuilder();
		CriteriaQuery<BigDecimal> cq = builder.createQuery(BigDecimal.class);
		
		Root<Movimentacao> root = cq.from(Movimentacao.class);
		
		cq.select(
				builder.sum(
						root.<BigDecimal>get("valor")
				)
		);
		
		cq.where(
				builder.like(
						root.<Conta>get("conta").<String>get("titular"),
						titular
				)
		);
		
		return this.em.createQuery(cq).getSingleResult();

	}
	
	public List<Movimentacao> pesquisa(Conta conta, TipoMovimentacao tipoMovimentacao, Integer mes) {

		CriteriaBuilder cb = this.em.getCriteriaBuilder();
		CriteriaQuery<Movimentacao> criterio = cb.createQuery(Movimentacao.class);
		Root<Movimentacao> movimentacao = criterio.from(Movimentacao.class);
		Predicate conjunction = cb.conjunction();
		
		if (conta.getId() != null) {
			conjunction = cb.and(conjunction, cb.equal(movimentacao.<Conta>get("conta"), conta));
		}
		
		if (mes != null && mes != 0) {
			Expression<Integer> expression = cb.function("month", Integer.class, movimentacao.<Calendar>get("data"));
			conjunction = cb.and(conjunction, cb.equal(expression, mes));
		}
		
		if(tipoMovimentacao != null) {
			conjunction = cb.and(conjunction, cb.equal(movimentacao.<TipoMovimentacao>get("tipoMovimentacao"), tipoMovimentacao));
		}
		
		criterio.where(conjunction);
		return this.em.createQuery(criterio).getResultList();
	}
	
}
