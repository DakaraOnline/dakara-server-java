/*  AUTOMATICALLY CONVERTED FILE  */

/* 
 * Este archivo fue convertido automaticamente, por un script, desde el 
 * código fuente original de Visual Basic 6.
 */

/* [(0, 'ATTRIBUTE'), (1, 'VB_Name'), (5, '='), (4, '"diccionario"')] */
/* [(0, 'ATTRIBUTE'), (1, 'VB_GlobalNameSpace'), (5, '='), (1, 'False')] */
/* [(0, 'ATTRIBUTE'), (1, 'VB_Creatable'), (5, '='), (1, 'True')] */
/* [(0, 'ATTRIBUTE'), (1, 'VB_PredeclaredId'), (5, '='), (1, 'False')] */
/* [(0, 'ATTRIBUTE'), (1, 'VB_Exposed'), (5, '='), (1, 'False')] */
/* '************************************************************** */
/* ' diccionario.cls */
/* ' */
/* ' Designed and implemented by Mariono Barrou (El Oso) */
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

/* 'clase diccionario */
/* 'basico, plain sin queso ni papa fritas */

/* 'mi idea cuando hice esto, lo encontre en el rigido :p. Hecha por el oso */

import enums.*;

public class diccionario {

	static final int MAX_ELEM = 100;

	static public class diccElem {
		public String clave;
		public Object def;
	}

	/*
	 * 'visual basic es una mierda para usar memoria dinamica, asi que uso esto
	 */
	private diccElem[] p_elementos = new diccElem[1 + MAX_ELEM];
	private int p_cant;

	public void Class_Initialize() {
		/* 'constructor */
		p_cant = 0;
	}

	public void Class_Terminate() {
		/* 'destructor */
		/* 'destruir los variants????? */
	}

	public int CantElem() {
		int retval = 0;
		retval = p_cant;
		return retval;
	}

	public boolean AtPut(String clave, Object /* FIXME BYREF!! */ elem) {
		boolean retval = false;
		int i = 0;

		retval = false;

		if (vb6.LenB(clave) == 0) {
			return retval;
		}

		clave = vb6.UCase(clave);

		if (p_cant == MAX_ELEM) {
			retval = false;
		} else {
			for (i = (1); i <= (p_cant); i++) {
				if (clave == p_elementos[i].clave) {
					p_elementos[i].def = elem;
					retval = true;
					/* ' epa ;) */
					break; /* FIXME: EXIT FOR */
				}
			}
			if (! /* FIXME */retval) {
				p_cant = p_cant + 1;
				p_elementos[p_cant].def = elem;
				p_elementos[p_cant].clave = clave;
				retval = true;
			}

		}
		return retval;
	}

	public Object At(String clave) {
		Object retval;
		int i = 0;

		clave = vb6.UCase(clave);

		retval = nullptr;
		for (i = (1); i <= (p_cant); i++) {
			if (clave == p_elementos[i].clave) {
				retval = p_elementos[i].def;
				return retval;
			}
		}

		return retval;
	}

	public String AtIndex(int i) {
		String retval;
		retval = p_elementos[i].clave;
		return retval;
	}

	public String MayorValor(int /* FIXME BYREF!! */ cant) {
		String retval;
		/* 'parchecito para el AO, me da la clave con mayor valor en valor */
		/* 'y la cantidad de claves con ese valor (por si hay empate) */
		int i = 0;
		int max = 0;
		String clave;
		max = -1;
		cant = 0;
		clave = "";
		for (i = (1); i <= (p_cant); i++) {
			if (max <= vb6.CInt(p_elementos[i].def)) {
				cant = vb6.IIf(max == vb6.CInt(p_elementos[i].def), cant + 1, 1);
				clave = vb6.IIf(max == vb6.CInt(p_elementos[i].def), clave + "," + p_elementos[i].clave,
						p_elementos[i].clave);
				max = vb6.CInt(p_elementos[i].def);
			}
		}

		retval = clave;

		return retval;
	}

	public void DumpAll() {
		int i = 0;

		for (i = (1); i <= (MAX_ELEM); i++) {
			p_elementos[i].clave = "";
			p_elementos[i].def = nullptr;
		}
		p_cant = 0;

	}

}