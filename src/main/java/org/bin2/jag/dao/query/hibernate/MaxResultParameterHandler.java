package org.bin2.jag.dao.query.hibernate;

import org.bin2.jag.dao.query.ParameterHandler;
import org.hibernate.Query;

/**
 * set the firstResult of the query using the param value
 * 
 * @see Query#setFirstResult
 **/
public class MaxResultParameterHandler implements ParameterHandler<Query> {

	/**
	 * set the firstResult of the query using the param value
	 * 
	 * @see Query#setMaxResults(int)
	 * @param param use this value as firstResult
	 * @throws ClassCastException if param is not an instance of Number
	 **/
	@Override
	public void proceedParameter(final Query query, final Object param) {
		query.setMaxResults(((Number) param).intValue());
	}
}
