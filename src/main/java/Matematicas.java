

/*  AUTOMATICALLY CONVERTED FILE  */
/* [(0, 'ATTRIBUTE'), (1, 'VB_Name'), (5, '='), (4, '"Matematicas"')] */
/* 'Argentum Online 0.12.2 */
/* 'Copyright (C) 2002 Márquez Pablo Ignacio */
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

import enums.*;

public class Matematicas {

	static int Porcentaje(int Total, int Porc) {
		int retval = 0;
		/* '*************************************************** */
		/* 'Author: Unknown */
		/* 'Last Modification: - */
		/* ' */
		/* '*************************************************** */

		retval = (Total * Porc) / (double) 100;
		return retval;
	}

	static int Distancia(Declaraciones.WorldPos /* FIXME BYREF!! */ wp1,
			Declaraciones.WorldPos /* FIXME BYREF!! */ wp2) {
		int retval = 0;
		/* '*************************************************** */
		/* 'Author: Unknown */
		/* 'Last Modification: - */
		/* ' */
		/* '*************************************************** */

		/* 'Encuentra la distancia entre dos WorldPos */
		retval = vb6.Abs(wp1.X - wp2.X) + vb6.Abs(wp1.Y - wp2.Y) + (vb6.Abs(wp1.map - wp2.map) * 100);
		return retval;
	}

	static double Distance(int X1, int Y1, int X2, int Y2) {
 double retval = 0.0;
 /* '*************************************************** */
 /* 'Author: Unknown */
 /* 'Last Modification: - */
 /* ' */
 /* '*************************************************** */
 
 /* 'Encuentra la distancia entre dos puntos */
 
 retval = vb6.Sqr(((Y1-Y2) $ 2+(X1-X2) $ 2));
 
return retval;
}

	static int RandomNumber(int LowerBound, int UpperBound) {
		int retval = 0;
		/* '************************************************************** */
		/* 'Author: Juan Martín Sotuyo Dodero */
		/* 'Last Modify Date: 3/06/2006 */
		/*
		 * 'Generates a random number in the range given - recoded to use longs
		 * and work properly with ranges
		 */
		/* '************************************************************** */
		retval = vb6.Fix(vb6.Rnd() * (UpperBound - LowerBound + 1)) + LowerBound;
		return retval;
	}

}