package raykernel.lang.dom.statement;

import java.io.Serializable;
import java.util.Collection;
import java.util.Set;

import raykernel.lang.dom.expression.Expression;

import com.google.common.collect.Sets;

public abstract class Statement implements Serializable {
	int charIndex = -1; // useful for ordering later

	/**
	 * get the expressions that directly make up this statements
	 */
	public abstract Collection<Expression> getSubExpressions();

	/**
	 * Gather up all sub expressions down to the leaves
	 */
	public Collection<Expression> getAllSubExpressions() {

		Set<Expression> allExpressions = Sets.newHashSet();

		for (Expression e : getSubExpressions()) {
			allExpressions.addAll(Expression.getAllSubExpressions(e));
		}

		return allExpressions;

	}

	public abstract void substitute(Expression oldExp, Expression newExp);

	@Override
	public abstract Statement clone();

	@Override
	public boolean equals(Object o) {
		boolean result = false;

		result = toString().equals(o.toString());

		// System.out.println(this + " ?= " + o + " : " + result);

		return result;
	}

	@Override
	public int hashCode() {
		System.out.println(this + " hash= " + toString().hashCode());

		return toString().hashCode();
	}

	public void setCharIndex(int charIndex) {
		this.charIndex = charIndex;
	}

	public int getCharIndex() {
		return charIndex;
	}
}
