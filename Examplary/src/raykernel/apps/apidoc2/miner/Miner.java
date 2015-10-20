package raykernel.apps.apidoc2.miner;

import java.io.File;
import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import raykernel.io.FileReader;
import raykernel.lang.dom.expression.UnknownExpressionException;
import raykernel.lang.dom.naming.Type;
import raykernel.lang.parse.ClassDeclaration;
import raykernel.lang.parse.EclipseCFGParser;

public class Miner implements Serializable {
	Map<Type, Set<ClassDeclaration>> typeToClassMap = new HashMap<>();

	EclipseCFGParser parser = new EclipseCFGParser();

	public void mine(File rootFile) throws UnknownExpressionException {
		List<File> sourceFiles = raykernel.io.FileFinder.findAll(rootFile, "java");
		System.out.println(sourceFiles.size() + " java source files found in "
				+ rootFile.getAbsolutePath());

		for (File srcFile : sourceFiles) {
			String source = FileReader.readFile(srcFile);
			List<ClassDeclaration> classes = parser.parse(source);

			for (ClassDeclaration dec : classes) {
				Set<Type> usedTypes = Util.getTypesUsed(dec);

				for (Type type : usedTypes) {
					Set<ClassDeclaration> decs = typeToClassMap.get(type);

					if (decs == null) {
						decs = new HashSet<ClassDeclaration>();
						typeToClassMap.put(type, decs);
					}

					decs.add(dec);
				}
			}
		}

		// write index to a file

		System.out.println("Type\tNumberOfClasses");
		for (Type key : typeToClassMap.keySet()) {
			System.out.println(key + "\t" + typeToClassMap.get(key).size());
		}
	}

	public Set<ClassDeclaration> getClassDecs(Type type) {
		return typeToClassMap.get(type);
	}

	public Collection<Type> getMinedTypes() {
		return typeToClassMap.keySet();
	}
}
