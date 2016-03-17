/*  AUTOMATICALLY CONVERTED FILE  */

/* 
 * Este archivo fue convertido automaticamente, por un script, desde el 
 * código fuente original de Visual Basic 6.
 */

/* [(0, 'ATTRIBUTE'), (1, 'VB_Name'), (5, '='), (4, '"Characters"')] */
/* '************************************************************** */
/* ' Characters.bas - library of functions to manipulate characters. */
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

import enums.*;

public class Characters {

	/* '' */
	/* ' Value representing invalid indexes. */
	static final int INVALID_INDEX = 0;

	/* '' */
	/* ' Retrieves the UserList index of the user with the give char index. */
	/* ' */
	/*
	 * ' @param CharIndex The char index being used by the user to be retrieved.
	 */
	/*
	 * ' @return The index of the user with the char placed in CharIndex or
	 * INVALID_INDEX if it's not a user or valid char index.
	 */
	/* ' @see INVALID_INDEX */

	public static int CharIndexToUserIndex(int CharIndex) {
		int retval = 0;
		/* '*************************************************** */
		/* 'Autor: Juan Martín Sotuyo Dodero (Maraxus) */
		/* 'Last Modification: 05/17/06 */
		/*
		 * 'Takes a CharIndex and transforms it into a UserIndex. Returns
		 * INVALID_INDEX in case of error.
		 */
		/* '*************************************************** */
		retval = Declaraciones.CharList[CharIndex];

		if (retval < 1 || retval > Declaraciones.MaxUsers) {
			retval = Characters.INVALID_INDEX;
			return retval;
		}

		if (Declaraciones.UserList[retval].Char.CharIndex != CharIndex) {
			retval = Characters.INVALID_INDEX;
			return retval;
		}
		return retval;
	}

}