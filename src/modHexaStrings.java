/*  AUTOMATICALLY CONVERTED FILE  */
/* [(0, 'ATTRIBUTE'), (1, 'VB_Name'), (5, '='), (4, '"modHexaStrings"')] */
/* 'Argentum Online 0.12.2 */
/* ' */
/* 'Copyright (C) 2002 Márquez Pablo Ignacio */
/* 'Copyright (C) 2002 Otto Perez */
/* 'Copyright (C) 2002 Aaron Perkins */
/* ' */
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
/* ' */
/* 'Argentum Online is based on Baronsoft's VB6 Online RPG */
/* 'You can contact the original creator of ORE at aaron@baronsoft.com */
/* 'for more information about ORE please visit http://www.baronsoft.com/ */
/* ' */
/* ' */
/* 'You can contact me at: */
/* 'morgolock@speedy.com.ar */
/* 'www.geocities.com/gmorgolock */
/* 'Calle 3 número 983 piso 7 dto A */
/* 'La Plata - Pcia, Buenos Aires - Republica Argentina */
/* 'Código Postal 1900 */
/* 'Pablo Ignacio Márquez */

/* 'Modulo realizado por Gonzalo Larralde(CDT) <gonzalolarralde@yahoo.com.ar> */
/* 'Para la conversion a caracteres de cadenas MD5 y de */
/* 'semi encriptación de cadenas por ascii table offset */

import enums.*;

public class modHexaStrings {

	static String hexMd52Asc(String MD5) {
		String retval;
		/* '*************************************************** */
		/* 'Author: Unknown */
		/* 'Last Modification: - */
		/* ' */
		/* '*************************************************** */

		int i;
		String L;

		if (vb6.Len(MD5) && 0x1) {
			MD5 = "0" + MD5;
		}

		for (i = (1); i <= (vb6.Len(MD5) / 2); i++) {
			L = vb6.mid(MD5, (2 * i) - 1, 2);
			retval = retval + vb6.Chr(hexHex2Dec(L));
		}
		return retval;
	}

	static int hexHex2Dec(String hex) {
		int retval;
		/* '*************************************************** */
		/* 'Author: Unknown */
		/* 'Last Modification: - */
		/* ' */
		/* '*************************************************** */

		retval = vb6.val("&H" + hex);
		return retval;
	}

	static String txtOffset(String Text, int off) {
		String retval;
		/* '*************************************************** */
		/* 'Author: Unknown */
		/* 'Last Modification: - */
		/* ' */
		/* '*************************************************** */

		int i;
		String L;

		for (i = (1); i <= (vb6.Len(Text)); i++) {
			L = vb6.mid(Text, i, 1);
			retval = retval + vb6.Chr((vb6.Asc(L) + off) && 0xFF);
		}
		return retval;
	}

}