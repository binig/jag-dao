package org.bin2.jag.dao.query;

import org.hibernate.Query;

/**
 * set the firstResult of the query using the param value
 * 
 * @see Query#setFirstResult
 **/
public class MaxResultParameterHandler implements ParameterHandler {

	/**
	 * set the firstResult of the query using the param value
	 * 
	 * @see Query#setMaxResult
	 * @param param use this value as firstResult
	 * @throws ClassCastException if param is not an instance of Number
	 **/
	@Override
	public void proceedParameter(final Query query, final Object param) {
		query.setMaxResults(((Number) param).intValue());
	}
}
