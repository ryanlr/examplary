package raykernel.apps.apidoc2.miner;

import java.io.File;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import raykernel.apps.apidoc2.model.AbstractUse;
import raykernel.apps.apidoc2.model.ConcreteUse;
import raykernel.apps.apidoc2.model.ConcreteUseClusterer;
import raykernel.lang.dom.expression.UnknownExpressionException;
import raykernel.lang.dom.naming.Type;
import raykernel.lang.parse.ClassDeclaration;

/**
 * The Miner takes as input a directory it recursively explores all directories
 * from that root locating java files those files are parsed using the eclipse
 * jdt they are then converted into a CFG representation independent of eclipse
 * Finally, all object creation sites (usage seeds) are located and an index is
 * created mapping API Names (Class names) to the cfgs that use them the index
 * is then serialized and written to disk
 * 
 * @author buse
 * 
 */
public class Main {

	public static void main(String[] args) throws UnknownExpressionException {
		String rootPath = args[0];
		File rootFile = new File(rootPath);

		Miner miner = new Miner();
		miner.mine(rootFile);

		for (Type type : miner.getMinedTypes()) {
			System.out.println("Analyzing uses of: " + type);

			Set<ClassDeclaration> decs = miner.getClassDecs(type);
			List<ConcreteUse> concreteUses = new LinkedList<>();

			for (ClassDeclaration dec : decs) {
				List<ConcreteUse> uses = ConcreteUse.extractUses(type, dec);
				concreteUses.addAll(uses);
			}

			System.out.println("Found " + concreteUses.size()
					+ " concrete uses");

			for (ConcreteUse use : concreteUses) {
				System.out.println(use);
			}

			// if more than n, then cluster them
			List<List<ConcreteUse>> clusters = ConcreteUseClusterer
					.cluster(concreteUses);

			// abstract each cluster, then print it
			List<AbstractUse> abstractUses = new LinkedList<>();
			for (List<ConcreteUse> cluster : clusters) {
				AbstractUse use = AbstractUse.abstractUse(cluster);
				System.out.println(use);
			}
		}
	}
}
