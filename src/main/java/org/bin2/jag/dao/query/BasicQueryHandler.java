package org.bin2.jag.dao.query;

import org.hibernate.Query;
import org.hibernate.Session;

public class BasicQueryHandler implements  QueryHandler {
	private final String query;
	public BasicQueryHandler(String query) {
		this.query=query;
	}   
	public Query getQuery(Session session) {
		return session.createQuery(query);
	}
}
