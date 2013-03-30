package br.com.caelum.financas.transaction;

import javax.inject.Inject;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;
import javax.persistence.EntityManager;

@Interceptor
@Transactional
public class TransactionInterceptor {

	@Inject
	EntityManager em;

	@AroundInvoke
	public Object intercept(InvocationContext ctx) throws Exception {
		em.getTransaction().begin();

		Object resultado = ctx.proceed();

		em.getTransaction().commit();
		return resultado;
	}

}
