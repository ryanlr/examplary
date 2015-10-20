package raykernel.lang.dom.expression;

import java.util.LinkedList;
import java.util.List;

import org.eclipse.jdt.core.dom.InstanceofExpression;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;

import raykernel.lang.dom.condition.AndCondition;
import raykernel.lang.dom.condition.Condition;
import raykernel.lang.dom.condition.OrCondition;
import raykernel.lang.dom.naming.Declaration;
import raykernel.lang.dom.naming.Type;

public class EclipseExpressionAdapter {

	public static Expression translate(org.eclipse.jdt.core.dom.Expression expression)
			throws UnknownExpressionException {
		if (expression == null)
			return null;

		// System.out.println("Translating " + expression + " : " +
		// expression.getClass());

		if (expression instanceof org.eclipse.jdt.core.dom.NullLiteral)
			return new NullExpression();
		else if (expression instanceof org.eclipse.jdt.core.dom.ThisExpression)
			return new ThisExpression();
		else if (expression instanceof org.eclipse.jdt.core.dom.BooleanLiteral) {
			if (((org.eclipse.jdt.core.dom.BooleanLiteral) expression).booleanValue())
				return new True();
			return new False();
		} else if (expression instanceof org.eclipse.jdt.core.dom.NumberLiteral) {
			org.eclipse.jdt.core.dom.NumberLiteral numlit = (org.eclipse.jdt.core.dom.NumberLiteral) expression;
			return new ConstantExpression(numlit.getToken());
		} else if (expression instanceof org.eclipse.jdt.core.dom.CharacterLiteral) {
			org.eclipse.jdt.core.dom.CharacterLiteral charlit = (org.eclipse.jdt.core.dom.CharacterLiteral) expression;
			return new CharacterLiteralExpression(charlit.getEscapedValue());
		} else if (expression instanceof org.eclipse.jdt.core.dom.SimpleName)
			return new Variable(
					((org.eclipse.jdt.core.dom.SimpleName) expression).getFullyQualifiedName());
		else if (expression instanceof org.eclipse.jdt.core.dom.QualifiedName)
			return new Variable(((org.eclipse.jdt.core.dom.QualifiedName) expression).toString());
		else if (expression instanceof org.eclipse.jdt.core.dom.StringLiteral)
			return new StringLiteralExpression(
					((org.eclipse.jdt.core.dom.StringLiteral) expression).getLiteralValue());
		else if (expression instanceof org.eclipse.jdt.core.dom.ParenthesizedExpression) {
			Expression exp = translate(((org.eclipse.jdt.core.dom.ParenthesizedExpression) expression)
					.getExpression());

			return new ParenthesizedExpression(exp);
		} else if (expression instanceof org.eclipse.jdt.core.dom.Assignment) {
			org.eclipse.jdt.core.dom.Assignment assign = (org.eclipse.jdt.core.dom.Assignment) expression;
			Expression left = translate(assign.getLeftHandSide());
			Expression right = translate(assign.getRightHandSide());
			// String op = assign.getOperator().toString();

			if (!(left instanceof Variable))
				throw new IllegalStateException("Expected a Variable.  Got a " + left.getClass());

			return new AssignmentExpression((Variable) left, right);
		} else if (expression instanceof org.eclipse.jdt.core.dom.MethodInvocation) {
			org.eclipse.jdt.core.dom.MethodInvocation inv = (org.eclipse.jdt.core.dom.MethodInvocation) expression;

			Expression target = translate(inv.getExpression());
			List<Expression> arguments = translateArguments(inv.arguments());

			String method = inv.getName().toString();
			InvocationExpression invokeExpr = new InvocationExpression(target, method, arguments);

			return invokeExpr;
		} else if (expression instanceof org.eclipse.jdt.core.dom.SuperMethodInvocation) {
			org.eclipse.jdt.core.dom.SuperMethodInvocation inv = (org.eclipse.jdt.core.dom.SuperMethodInvocation) expression;

			List<Expression> arguments = translateArguments(inv.arguments());
			String method = inv.getName().toString();

			InvocationExpression invokeExpr = new InvocationExpression(new SuperExpression(),
					method, arguments);

			return invokeExpr;
		} else if (expression instanceof org.eclipse.jdt.core.dom.ArrayInitializer) {
			org.eclipse.jdt.core.dom.ArrayInitializer init = (org.eclipse.jdt.core.dom.ArrayInitializer) expression;
			List<Expression> expressions = translateArguments(init.expressions());
			return new ArrayInitExpression(expressions);
		} else if (expression instanceof org.eclipse.jdt.core.dom.ArrayCreation) {
			org.eclipse.jdt.core.dom.ArrayCreation arrayC = (org.eclipse.jdt.core.dom.ArrayCreation) expression;
			Type t = new Type(arrayC.getType().toString());
			List<Expression> dims = translateArguments(arrayC.dimensions());
			ArrayInitExpression init = (ArrayInitExpression) translate(arrayC.getInitializer());

			return new ArrayCreationExpression(t, dims, init);
		} else if (expression instanceof org.eclipse.jdt.core.dom.ArrayAccess) {
			org.eclipse.jdt.core.dom.ArrayAccess arrayA = (org.eclipse.jdt.core.dom.ArrayAccess) expression;

			Expression array = translate(arrayA.getArray());
			Expression index = translate(arrayA.getIndex());

			return new ArrayAccessExpression(array, index);
		} else if (expression instanceof org.eclipse.jdt.core.dom.FieldAccess) {
			org.eclipse.jdt.core.dom.FieldAccess acc = (org.eclipse.jdt.core.dom.FieldAccess) expression;

			Expression exp = translate(acc.getExpression());
			String field = acc.getName().toString();

			return new FieldRefExpression(exp, field);
		} else if (expression instanceof org.eclipse.jdt.core.dom.ClassInstanceCreation) {
			org.eclipse.jdt.core.dom.ClassInstanceCreation cic = (org.eclipse.jdt.core.dom.ClassInstanceCreation) expression;

			List<Expression> arguments = translateArguments(cic.arguments());

			Type t = new Type(cic.getType().toString());

			Expression target = translate(cic.getExpression());

			return new NewExpression(target, t, arguments);

		} else if (expression instanceof org.eclipse.jdt.core.dom.CastExpression) {
			org.eclipse.jdt.core.dom.CastExpression cast = (org.eclipse.jdt.core.dom.CastExpression) expression;

			Expression e = translate(cast.getExpression());
			Type type = new Type(cast.getType().toString());

			return new CastExpression(e, type);
		} else if (expression instanceof InstanceofExpression) {
			org.eclipse.jdt.core.dom.InstanceofExpression inst = (org.eclipse.jdt.core.dom.InstanceofExpression) expression;
			Expression exp = translate(inst.getLeftOperand());
			Type type = new Type(inst.getRightOperand().toString());

			return new InstanceOfExpression(exp, type);
		} else if (expression instanceof org.eclipse.jdt.core.dom.PostfixExpression) {
			org.eclipse.jdt.core.dom.PostfixExpression pfx = (org.eclipse.jdt.core.dom.PostfixExpression) expression;
			Expression exp = translate(pfx.getOperand());

			return new PostfixExpression(exp, pfx.getOperator().toString());
		} else if (expression instanceof org.eclipse.jdt.core.dom.PrefixExpression) {
			org.eclipse.jdt.core.dom.PrefixExpression pfx = (org.eclipse.jdt.core.dom.PrefixExpression) expression;
			Expression exp = translate(pfx.getOperand());

			return new PrefixExpression(exp, pfx.getOperator().toString());
		} else if (expression instanceof org.eclipse.jdt.core.dom.TypeLiteral) {
			org.eclipse.jdt.core.dom.TypeLiteral exp = (org.eclipse.jdt.core.dom.TypeLiteral) expression;
			return new TypeLiteralExpression(exp.getType().toString());
		} else if (expression instanceof org.eclipse.jdt.core.dom.InfixExpression) {
			org.eclipse.jdt.core.dom.InfixExpression inf = (org.eclipse.jdt.core.dom.InfixExpression) expression;

			org.eclipse.jdt.core.dom.InfixExpression.Operator op = inf.getOperator();

			Expression left = translate(inf.getLeftOperand());
			Expression right = translate(inf.getRightOperand());

			// Condition Chains
			if (op == org.eclipse.jdt.core.dom.InfixExpression.Operator.CONDITIONAL_AND) {
				AndCondition c = new AndCondition();

				if (!(left instanceof Condition) || !(right instanceof Condition))
					throw new IllegalArgumentException("These need to be conditions: " + left
							+ ", " + right + "\n" + "left type = " + left.getClass() + "\n"
							+ "right type = " + right.getClass());

				c.addCondition((Condition) left);
				c.addCondition((Condition) right);

				return c;
			} else if (op == org.eclipse.jdt.core.dom.InfixExpression.Operator.CONDITIONAL_OR) {
				OrCondition c = new OrCondition();

				if (!(left instanceof Condition) || !(right instanceof Condition))
					throw new IllegalArgumentException("These need to be conditions: " + left
							+ ", " + right);

				c.addCondition((Condition) left);
				c.addCondition((Condition) right);

				return c;
			}

			// Comparison
			else if (op == org.eclipse.jdt.core.dom.InfixExpression.Operator.EQUALS
					|| op == org.eclipse.jdt.core.dom.InfixExpression.Operator.NOT_EQUALS
					|| op == org.eclipse.jdt.core.dom.InfixExpression.Operator.LESS
					|| op == org.eclipse.jdt.core.dom.InfixExpression.Operator.LESS_EQUALS
					|| op == org.eclipse.jdt.core.dom.InfixExpression.Operator.GREATER
					|| op == org.eclipse.jdt.core.dom.InfixExpression.Operator.GREATER_EQUALS)
				return new ComparasonExpression(left, right, op.toString());

			// Arithmetic
			else if (op == org.eclipse.jdt.core.dom.InfixExpression.Operator.MINUS
					|| op == org.eclipse.jdt.core.dom.InfixExpression.Operator.PLUS
					|| op == org.eclipse.jdt.core.dom.InfixExpression.Operator.TIMES
					|| op == org.eclipse.jdt.core.dom.InfixExpression.Operator.DIVIDE)
				return new ArithmeticExpression(left, right, op.toString());

			// Other like arithmetic
			else if (op == org.eclipse.jdt.core.dom.InfixExpression.Operator.LEFT_SHIFT
					|| op == org.eclipse.jdt.core.dom.InfixExpression.Operator.RIGHT_SHIFT_SIGNED
					|| op == org.eclipse.jdt.core.dom.InfixExpression.Operator.RIGHT_SHIFT_UNSIGNED
					|| op == org.eclipse.jdt.core.dom.InfixExpression.Operator.OR
					|| op == org.eclipse.jdt.core.dom.InfixExpression.Operator.AND
					|| op == org.eclipse.jdt.core.dom.InfixExpression.Operator.XOR
					|| op == org.eclipse.jdt.core.dom.InfixExpression.Operator.REMAINDER)
				return new ArithmeticExpression(left, right, op.toString());

			else {
				System.out.println("Unhandled infix op = " + op + " : " + op.getClass());
			}

		}

		throw new UnknownExpressionException(expression);

	}

	private static List<Expression> translateArguments(List arguments)
			throws UnknownExpressionException {
		List<Expression> ret = new LinkedList<>();

		for (Object o : arguments) {
			org.eclipse.jdt.core.dom.Expression arg = (org.eclipse.jdt.core.dom.Expression) o;
			ret.add(translate(arg));
		}

		return ret;
	}

	public static Condition translateCondition(org.eclipse.jdt.core.dom.Expression expression)
			throws UnknownExpressionException {
		
		Expression e;

		try {
			e = translate(expression);
		} catch (UnknownExpressionException exp) {
			return new True();
		}

		if (e == null)
			return new True();

		if (e instanceof Condition)
			return (Condition) e;

		throw new IllegalArgumentException(e + " not recognised as a condition.  Type is "
				+ e.getClass());
	}

	public static Declaration translateDeclaration(SingleVariableDeclaration dec) {
		return new Declaration(new Type(dec.getType().toString()), new Variable(dec.getName()
				.toString()));

	}

	public static Type translateType(org.eclipse.jdt.core.dom.Type type) {
		return new Type(type.toString());
	}
}
