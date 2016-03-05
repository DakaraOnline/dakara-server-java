/*  AUTOMATICALLY CONVERTED FILE  */
/* [(0, 'ATTRIBUTE'), (1, 'VB_Name'), (5, '='), (4, '"clsIniManager"')] */
/* [(0, 'ATTRIBUTE'), (1, 'VB_GlobalNameSpace'), (5, '='), (1, 'False')] */
/* [(0, 'ATTRIBUTE'), (1, 'VB_Creatable'), (5, '='), (1, 'True')] */
/* [(0, 'ATTRIBUTE'), (1, 'VB_PredeclaredId'), (5, '='), (1, 'False')] */
/* [(0, 'ATTRIBUTE'), (1, 'VB_Exposed'), (5, '='), (1, 'False')] */
/* '************************************************************** */
/* ' clsIniManager.cls - Loads INI files into memory and applies Binary Search to get values at high speed. */
/* ' Use it instead of GetVar when reading several values form the same file at once, otherwise it's not usefull. */
/* ' Based in the idea of AlejoLP and his clsLeerInis. */
/* ' */
/* ' Designed and implemented by Juan Martín Sotuyo Dodero (Maraxus) */
/* ' (juansotuyo@gmail.com) */
/* '************************************************************** */

/* '************************************************************************** */
/* 'This program is free software; you can redistribute it and/or modify */
/* 'it under the terms of the Affero General Public License; */
/* 'either version 1 of the License, or any later version. */
/* ' */
/* 'This program is distributed in the hope that it will be useful, */
/* 'but WITHOUT ANY WARRANTY; without even the implied warranty of */
/* 'MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the */
/* 'Affero General Public License for more details. */
/* ' */
/* 'You should have received a copy of the Affero General Public License */
/* 'along with this program; if not, you can find it at http://www.affero.org/oagpl.html */
/* '************************************************************************** */

/* '' */
/* 'Loads a complete INI file into memory and sorts it's data and keys for faster searches. */
/* 'It is MUCH faster than GetPrivateProfileStringA if you search for several values within a file, otherwise stick to the API. */
/* 'It's particularly usefull when you can keep a whole file in memory such as NPCs.dat' */
/* ' Based in the idea of AlejoLP and his clsLeerInis. */
/* ' */
/* ' @author Juan Martín Sotuyo Dodero (Maraxus) juansotuyo@gmail.com */
/* ' @version 1.1.0 */
/* ' @date 20060501 */

/* '01/05/2006 - Juan Martín Sotuyo Dodero (Maraxus) - (juansotuyo@gmail.com) */
/* '   - First Release */
/* ' */
/* '01/04/2008 - Juan Martín Sotuyo Dodero (Maraxus) - (juansotuyo@gmail.com) */
/* '   - Add: KeyExists method allows to check for valid section keys. */
/* ' */
/* '10/10/2010 - Torres Patricio(Pato) */
/* '   - Add:  AddNode method to add a main node. */
/* '           AddKey method to add key into the refered main node. */
/* '           DumpFile method to dump the data into a file. */

import enums.*;

public class clsIniManager {

	/* '' */
	/* 'Structure that contains a value and it's key in a INI file */
	/* ' */
	/* ' @param key String containing the key associated to the value. */
	/* ' @param value String containing the value of the INI entry. */
	/* ' @see MainNode */
	/* ' */

	static public class ChildNode {
		public String key;
		public String value;
	}

	/* '' */
	/* 'Structure that contains all info under a tag in a INI file. */
	/* 'Such tags are indicated with the "[" and "]" characters. */
	/* ' */
	/*
	 * ' @param name String containing the text within the "[" and "]"
	 * characters.
	 */
	/* 'It's the key used when searching for a main section of the INI data. */
	/*
	 * ' @param values Array of ChildNodes, each containing a value entry along
	 * with it's key.
	 */
	/* ' @param numValues Number of entrys in the main node. */

	static public class MainNode {
		public String Name;
		public ChildNode[] values;
		public int numValues;
	}

	/* '' */
	/* 'Containts all Main sections of the loaded INI file */
	private MainNode[] fileData = new MainNode[0];

	/* '' */
	/* 'Stores the total number of main sections in the loaded INI file */
	private int MainNodes;

	/* '' */
	/* 'Default constructor. Does nothing. */

	void Class_Initialize() {
		/* '************************************************************** */
		/* 'Author: Juan Martín Sotuyo Dodero */
		/* 'Last Modify Date: 5/01/2006 */
		/* ' */
		/* '************************************************************** */
	}

	/* '' */
	/* 'Destroy every array and deallocates al memory. */
	/* ' */

	void Class_Terminate() {
		/* '************************************************************** */
		/* 'Author: Juan Martín Sotuyo Dodero */
		/* 'Last Modify Date: 5/01/2006 */
		/* ' */
		/* '************************************************************** */
		int i = 0;

		/* 'Clean up */
		if (MainNodes) {
			for (i = (1); i <= (MainNodes - 1); i++) {
				/* FIXME: ERASE fileData ( i ) . values */
			}

			/* FIXME: ERASE fileData */
		}

		MainNodes = 0;
	}

	/* '' */
	/*
	 * 'Loads a INI file so it's values can be read. Must be called before being
	 * able to use GetValue.
	 */
	/* ' */
	/* ' @param file Complete path of the INI file to be loaded. */
	/* ' @see GetValue */

	void Initialize(String File) {
		/* '************************************************************** */
		/* 'Author: Juan Martín Sotuyo Dodero */
		/* 'Last Modify Date: 27/07/2006 */
		/* 'Opens the requested file and loads it's data into memory */
		/* '************************************************************** */
		int handle = 0;
		String Text;
		int Pos = 0;

		/* 'Prevent memory losses if we are attempting to reload a file.... */
		diccionario.Class_Terminate();

		/* 'Get a free handle and start reading line by line until the end */
		handle = vb6.FreeFile();

		/* FIXME: OPEN File FOR INPUT AS handle */

		while (!(clsByteBuffer.Eof(handle))) {
			/* FIXME: LINE INPUT # handle , Text */

			/* 'Is it null?? */
			if (vb6.Len(Text)) {
				/*
				 * 'If it starts with '[' it is a main node or nothing
				 * (GetPrivateProfileStringA works this way), otherwise it's a
				 * value
				 */
				if (vb6.Left(Text, 1) == "[") {
					/*
					 * 'If it has an ending ']' it's a main node, otherwise it's
					 * nothing
					 */
					Pos = vb6.InStr(2, Text, "]");
					if (Pos) {
						/* 'Add a main node */
						fileData = (fileData == null) ? new MainNode[MainNodes]
								: java.util.Arrays.copyOf(fileData, MainNodes);

						fileData[MainNodes].Name = vb6.UCase(vb6.Trim(vb6.mid(Text, 2, Pos - 2)));

						MainNodes = MainNodes + 1;
					}
				} else {
					/*
					 * 'So it's a value. Check if it has a '=', otherwise it's
					 * nothing
					 */
					Pos = vb6.InStr(2, Text, "=");
					if (Pos) {
						/* 'Is it under any main node?? */
						if (MainNodes) {
							/* 'Add it to the main node's value */
							fileData[MainNodes - 1].values = (fileData[MainNodes - 1].values == null)
									? new ChildNode[fileData[MainNodes - 1].numValues]
									: java.util.Arrays.copyOf(fileData[MainNodes - 1].values,
											fileData[MainNodes - 1].numValues);

							fileData[MainNodes - 1].values[fileData[MainNodes - 1].numValues].value = vb6.Right(Text,
									vb6.Len(Text) - Pos);
							fileData[MainNodes - 1].values[fileData[MainNodes - 1].numValues].key = vb6
									.UCase(vb6.Left(Text, Pos - 1));

							fileData[MainNodes - 1].numValues = fileData[MainNodes - 1].numValues + 1;
						}
					}
				}
			}
		}

		/* FIXME: CLOSE handle */

		int i = 0;

		if (MainNodes) {
			/* 'Sort main nodes to allow binary search */
			SortMainNodes(0, MainNodes - 1);

			/* 'Sort values of each node to allow binary search */
			for (i = (0); i <= (MainNodes - 1); i++) {
				if (fileData[i].numValues) {
					SortChildNodes(fileData[i], 0, fileData[i].numValues - 1);
				}
			}
		}
	}

	/* '' */
	/*
	 * 'Sorts all child nodes within the given MainNode alphabetically by their
	 * keys. Uses quicksort.
	 */
	/* ' */
	/* ' @param Node The MainNode whose values are to be sorted. */
	/* ' @param first The first index to consider when sorting. */
	/* ' @param last The last index to be considered when sorting. */

	void SortChildNodes(MainNode /* FIXME BYREF!! */ Node, int First, int Last) {
		/* '************************************************************** */
		/* 'Author: Juan Martín Sotuyo Dodero */
		/* 'Last Modify Date: 5/01/2006 */
		/* 'Sorts the list of values in a given MainNode using quicksort, */
		/* 'this allows the use of Binary Search for faster searches */
		/* '************************************************************** */
		/* 'First item in the list */
		int min = 0;
		/* 'Last item in the list */
		int max = 0;
		/* 'Item used to compare */
		String comp;
		ChildNode temp;

		min = First;
		max = Last;

		comp = Node.values[(min + max) / 2].key;

		while (min <= max) {
			while (Node.values[min].key < comp && min < Last) {
				min = min + 1;
			}
			while (Node.values[max].key > comp && max > First) {
				max = max - 1;
			}
			if (min <= max) {
				temp = Node.values[min];
				Node.values[min] = Node.values[max];
				Node.values[max] = temp;
				min = min + 1;
				max = max - 1;
			}
		}

		if (First < max) {
			SortChildNodes(Node, First, max);
		}
		if (min < Last) {
			SortChildNodes(Node, min, Last);
		}
	}

	/* '' */
	/*
	 * 'Sorts all main nodes in the loaded INI file alphabetically by their
	 * names. Uses quicksort.
	 */
	/* ' */
	/* ' @param first The first index to consider when sorting. */
	/* ' @param last The last index to be considered when sorting. */

	void SortMainNodes(int First, int Last) {
		/* '************************************************************** */
		/* 'Author: Juan Martín Sotuyo Dodero */
		/* 'Last Modify Date: 5/01/2006 */
		/* 'Sorts the MainNodes list using quicksort, */
		/* 'this allows the use of Binary Search for faster searches */
		/* '************************************************************** */
		/* 'First item in the list */
		int min = 0;
		/* 'Last item in the list */
		int max = 0;
		/* 'Item used to compare */
		String comp;
		MainNode temp;

		min = First;
		max = Last;

		comp = fileData[(min + max) / 2].Name;

		while (min <= max) {
			while (fileData[min].Name < comp && min < Last) {
				min = min + 1;
			}
			while (fileData[max].Name > comp && max > First) {
				max = max - 1;
			}
			if (min <= max) {
				temp = fileData[min];
				fileData[min] = fileData[max];
				fileData[max] = temp;
				min = min + 1;
				max = max - 1;
			}
		}

		if (First < max) {
			SortMainNodes(First, max);
		}
		if (min < Last) {
			SortMainNodes(min, Last);
		}
	}

	/* '' */
	/*
	 * 'Searches for a given key within a given main section and if it exists
	 * retrieves it's value, otherwise a null string
	 */
	/* ' */
	/*
	 * ' @param Main The name of the main section in which we will be searching.
	 */
	/* ' @param key The key of the value we are looking for. */
	/*
	 * ' @returns The value asociated with the given key under the requeted main
	 * section of the INI file or a null string if it's not found.
	 */

	String GetValue(String Main, String key) {
		String retval;
		/* '************************************************************** */
		/* 'Author: Juan Martín Sotuyo Dodero */
		/* 'Last Modify Date: 5/01/2006 */
		/*
		 * 'Returns a value if the key and main node exist, or a nullstring
		 * otherwise
		 */
		/* '************************************************************** */
		int i = 0;
		int j = 0;

		/* 'Search for the main node */
		i = FindMain(vb6.UCase(Main));

		if (i >= 0) {
			/* 'If valid, binary search among keys */
			j = FindKey(fileData[i], vb6.UCase(key));

			/* 'If we found it we return it */
			if (j >= 0) {
				retval = fileData[i].values[j].value;
			}
		}
		return retval;
	}

	/* '' */
	/*
	 * 'Searches for a given key within a given main section and if it exists
	 * retrieves it's value, otherwise a null string
	 */
	/* ' */
	/*
	 * ' @param Main The name of the main section in which we will be searching.
	 */
	/* ' @param key The key of the value we are looking for. */
	/*
	 * ' @returns The value asociated with the given key under the requeted main
	 * section of the INI file or a null string if it's not found.
	 */

	void ChangeValue(String Main, String key, String value) {
		/* '************************************************************** */
		/* 'Author: ZaMa */
		/* 'Last Modify Date: 27/05/2009 */
		/* 'If the key and main node exist, changes the value */
		/* '************************************************************** */
		int i = 0;
		int j = 0;

		/* 'Search for the main node */
		i = FindMain(vb6.UCase(Main));

		if (i < 0) {
			i = AddNode(Main);
		}

		/* 'If valid, binary search among keys */
		j = FindKey(fileData[i], vb6.UCase(key));

		if (j < 0) {
			AddKey(Main, key, value);
		} else {
			/* 'If we found it we change it */
			fileData[i].values[j].value = value;
		}
	}

	/* '' */
	/*
	 * 'Searches for a given key within a given main node and returns the index
	 * in which it's stored or the negation of the index in which it should be
	 * if not found.
	 */
	/* ' */
	/*
	 * ' @param Node The MainNode among whose value entries we will be
	 * searching.
	 */
	/* ' @param key The key of the value we are looking for. */
	/*
	 * ' @returns The index in which the value with the key we are looking for
	 * is stored or the negation of the index in which it should be if not
	 * found.
	 */

	int FindKey(MainNode /* FIXME BYREF!! */ Node, String key) {
		int retval = 0;
		/* '************************************************************** */
		/* 'Author: Juan Martín Sotuyo Dodero */
		/* 'Last Modify Date: 5/01/2006 */
		/*
		 * 'Returns the index of the value which key matches the requested one,
		 */
		/* 'or the negation of the position were it should be if not found */
		/* '************************************************************** */
		int min = 0;
		int max = 0;
		int mid = 0;

		min = 0;
		max = Node.numValues - 1;

		while (min <= max) {
			mid = (min + max) / 2;

			if (Node.values[mid].key < key) {
				min = mid + 1;
			} else if (Node.values[mid].key > key) {
				max = mid - 1;
			} else {
				/* 'We found it */
				retval = mid;
				return retval;
			}
		}

		/* 'Not found, return the negation of the position where it should be */
		/*
		 * '(all higher values are to the right of the list and lower values are
		 * to the left)
		 */
		retval = !min;
		return retval;
	}

	/* '' */
	/*
	 * 'Searches for a main section with the given name within the loaded INI
	 * file and returns the index in which it's stored or the negation of the
	 * index in which it should be if not found.
	 */
	/* ' */
	/* ' @param name The name of the MainNode we are looking for. */
	/*
	 * ' @returns The index in which the main section we are looking for is
	 * stored or the negation of the index in which it should be if not found.
	 */

	int FindMain(String Name) {
		int retval = 0;
		/* '************************************************************** */
		/* 'Author: Juan Martín Sotuyo Dodero */
		/* 'Last Modify Date: 5/01/2006 */
		/*
		 * 'Returns the index of the MainNode which name matches the requested
		 * one,
		 */
		/* 'or the negation of the position were it should be if not found */
		/* '************************************************************** */
		int min = 0;
		int max = 0;
		int mid = 0;

		min = 0;
		max = MainNodes - 1;

		while (min <= max) {
			mid = (min + max) / 2;

			if (fileData[mid].Name < Name) {
				min = mid + 1;
			} else if (fileData[mid].Name > Name) {
				max = mid - 1;
			} else {
				/* 'We found it */
				retval = mid;
				return retval;
			}
		}

		/* 'Not found, return the negation of the position where it should be */
		/*
		 * '(all higher values are to the right of the list and lower values are
		 * to the left)
		 */
		retval = !min;
		return retval;
	}

	/* '' */
	/* 'Checks wether a given key exists or not. */
	/* ' */
	/*
	 * ' @param name The name of the element whose existance is being checked.
	 */
	/* ' @returns True if the key exists, false otherwise. */

	boolean KeyExists(String Name) {
		boolean retval = false;
		/* '************************************************************** */
		/* 'Author: Juan Martín Sotuyo Dodero */
		/* 'Last Modify Date: 04/01/2008 */
		/* 'Returns true of the key exists, false otherwise. */
		/* '************************************************************** */
		retval = FindMain(vb6.UCase(Name)) >= 0;
		return retval;
	}

	int AddNode(String /* FIXME BYREF!! */ Name) {
		int retval = 0;
		/* '************************************************************** */
		/* 'Author: Torres Patricio (Pato) */
		/* 'Last Modify Date: 10/10/2010 */
		/* ' */
		/* '************************************************************** */
		int i = 0;
		int MainPos = 0;

		Name = vb6.UCase(Name);

		/* 'Add a main node */
		fileData = (fileData == null) ? new MainNode[MainNodes] : java.util.Arrays.copyOf(fileData, MainNodes);

		if (MainNodes) {
			i = MainNodes - 1;
			MainPos = !FindMain(Name);

			while (i >= MainPos) {
				fileData[i + 1] = fileData[i];
				i = i - 1;
			}

			fileData[MainPos].numValues = 0;
			/* FIXME: ERASE fileData ( MainPos ) . values */
		}

		fileData[MainPos].Name = Name;

		MainNodes = MainNodes + 1;

		retval = MainPos;
		return retval;
	}

	int AddKey(String /* FIXME BYREF!! */ Main, String /* FIXME BYREF!! */ key) {
		return AddKey(Main, key, "");
	}

	int AddKey(String /* FIXME BYREF!! */ Main, String /* FIXME BYREF!! */ key, String value) {
		int retval = 0;
		/* '************************************************************** */
		/* 'Author: Torres Patricio (Pato) */
		/* 'Last Modify Date: 10/10/2010 */
		/* ' */
		/* '************************************************************** */
		int MainPos = 0;
		int KeyPos = 0;
		int i = 0;

		Main = vb6.UCase(Main);
		key = vb6.UCase(key);

		MainPos = FindMain(Main);

		if (MainPos < 0) {
			MainPos = AddNode(Main);
		}

		/* 'Add it to the main node's value */
		fileData[MainPos].values = (fileData[MainPos].values == null) ? new ChildNode[fileData[MainPos].numValues]
				: java.util.Arrays.copyOf(fileData[MainPos].values, fileData[MainPos].numValues);

		if (fileData[MainPos].numValues > 0) {
			i = fileData[MainPos].numValues - 1;
			KeyPos = !FindKey(fileData[MainPos], key);

			while (i >= KeyPos) {
				fileData[MainPos].values[i + 1] = fileData[MainPos].values[i];
				i = i - 1;
			}
		}

		fileData[MainPos].values[KeyPos].key = key;
		fileData[MainPos].values[KeyPos].value = value;

		fileData[MainPos].numValues = fileData[MainPos].numValues + 1;

		retval = KeyPos;
		return retval;
	}

	void DumpFile(String /* FIXME BYREF!! */ File) {
		/* '************************************************************** */
		/* 'Author: Torres Patricio (Pato) */
		/* 'Last Modify Date: 10/10/2010 */
		/* ' */
		/* '************************************************************** */
		int hFile = 0;
		int i = 0;
		int j = 0;

		hFile = vb6.FreeFile();

		/* FIXME: OPEN File FOR OUTPUT AS hFile */

		for (i = (0); i <= (MainNodes - 1); i++) {
			/* FIXME: PRINT # hFile , "[" & . Name & "]" */

			for (j = (0); j <= (fileData[i].numValues - 1); j++) {
				/*
				 * FIXME: PRINT # hFile , . values ( j ) . key & "=" & . values
				 * ( j ) . value
				 */
			}

			/* FIXME: PRINT # hFile , "" */
		}

		/* FIXME: CLOSE hFile */
	}

}