package org.powerTools.engine;


/**
 * A symbol is a named data item appearing in a test.
 * Its name is a simple identifier, like 'url' or 'cityOfBirth'.<BR/>
 * Some types of symbols contains a single item of data.
 * Others are structures, containing multiple fields, possibly nested.
 * Each field in a structure also has a simple identifier.
 * A structure field is referenced by prefixing it with its parent field names,
 * separated by periods, like 'client.address.zipCode'.<BR/>
 * A (field in a) structure can be cleared, removing all the fields it contains. 
 */
public interface Symbol {
	String PERIOD = "\\.";

	
	String getName ();

	void setValue (String value);
	void setValue (String name, String value);

	String getValue ();
	String getValue (String name);

	void clear (String[] names);
	void clear (String name);
}