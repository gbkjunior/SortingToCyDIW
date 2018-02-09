package adapter;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.HashSet;
import java.util.List;
import java.util.StringJoiner;

import com.github.javafaker.Faker;

import java.util.Random;
import java.util.Set;

import cyutils.btree.Tuple;
import cyutils.btree.TupleAttribute;
// class variable



public class BTreePrepareSortedData {
	final static String lexicon = "ABCDEFGHIJKLMNOPQRSTUVWXYZ12345674890";
	static Faker faker = new Faker();
	final static java.util.Random rand = new java.util.Random();

	// consider using a Map<String,Boolean> to say whether the identifier is being used or not 
	final static Set<String> identifiers = new HashSet<String>();
	private static final int DATA_SIZE = 1000;
	
	public static void prepare(String tupleConfigXmlFile, String tupleTxtFile) throws FileNotFoundException {
		Tuple tuple = new Tuple(tupleConfigXmlFile);
		List<TupleAttribute> attributes = tuple.getTupleAttributes();
		PrintWriter writer = new PrintWriter(tupleTxtFile);
		
		for (int ii = 0; ii < DATA_SIZE; ii++) {
			StringJoiner joiner = new StringJoiner(",");
			for (TupleAttribute attribute : attributes) {
				if (attribute.getType().equals("Integer")) {
					joiner.add(String.valueOf(increasingInt(ii)));
				} else if (attribute.getType().equals("String")) {
					joiner.add(faker.name().fullName());
				}
			}
			String joined = "[" + joiner.toString() + "]";
			writer.println(joined);
		}
		writer.close();
	}
	
	private static int increasingInt(int i) {
		return i;
	}
	
	private static String increasingString(int i) {
		return String.format("%05d", i);	
	}

	public static String randomIdentifier() {
    StringBuilder builder = new StringBuilder();
    while(builder.toString().length() == 0) {
        int length = rand.nextInt(5)+5;
        for(int i = 0; i < length; i++) {
            builder.append(lexicon.charAt(rand.nextInt(lexicon.length())));
        }
        if(identifiers.contains(builder.toString())) {
            builder = new StringBuilder();
        }
    }
    return builder.toString();
}
}
