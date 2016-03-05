
/*  AUTOMATICALLY CONVERTED FILE  */
/* [(0, 'ATTRIBUTE'), (1, 'VB_Name'), (5, '='), (4, '"cCola"')] */
/* [(0, 'ATTRIBUTE'), (1, 'VB_GlobalNameSpace'), (5, '='), (1, 'False')] */
/* [(0, 'ATTRIBUTE'), (1, 'VB_Creatable'), (5, '='), (1, 'True')] */
/* [(0, 'ATTRIBUTE'), (1, 'VB_PredeclaredId'), (5, '='), (1, 'False')] */
/* [(0, 'ATTRIBUTE'), (1, 'VB_Exposed'), (5, '='), (1, 'False')] */
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

public class cCola {
	/* ' Metodos publicos */
	/* ' */
	/* ' Public sub Push(byval i as variant) mete el elemento i */
	/* ' al final de la cola. */
	/* ' */
	/* ' Public Function Pop As Variant: quita de la cola el primer elem */
	/* ' y lo devuelve */
	/* ' */
	/* ' Public Function VerElemento(ByVal Index As Integer) As Variant */
	/* ' muestra el elemento numero Index de la cola sin quitarlo */
	/* ' */
	/* ' Public Function PopByVal() As Variant: muestra el primer */
	/* ' elemento de la cola sin quitarlo */
	/* ' */
	/* ' Public Property Get Longitud() As Integer: devuelve la */
	/* ' cantidad de elementos que tiene la cola. */

	static final int FRENTE = 1;

	private int MaxElem;

	private vb6.Collection Cola;

	void MaxLenght(int Max) {
		/* '*************************************************** */
		/* 'Author: ZaMa */
		/* 'Last Modification: 14/11/2010 */
		/* 'Sets the max queue lenght. */
		/* '*************************************************** */
		MaxElem = Max;
	}

	void Reset() {
		/* FIXME: ON ERROR RESUME NEXT */

		int i = 0;
		for (i = (1); i <= (Me.Longitud); i++) {
			Cola.Remove(FRENTE);
		}

	}

	int Longitud() {
		int retval = 0;
		retval = Cola.Count;
		return retval;
	}

	boolean IndexValido(int i) {
		boolean retval = false;
		retval = i >= 1 && i <= Me.Longitud;
		return retval;
	}

	void Class_Initialize() {
		Cola = new Collection();
	}

	String VerElemento(int index) {
		return VerElemento(index, true);
	}

	String VerElemento(int index, boolean ConvertUpper) {
		String retval;
		/* FIXME: ON ERROR RESUME NEXT */
		if (IndexValido(index)) {
			/* 'Pablo */
			if (ConvertUpper) {
				retval = vb6.UCase(Cola.Item[index]);
			} else {
				retval = Cola.Item[index];
			}

			/* '/Pablo */
			/* 'VerElemento = Cola(Index) */
		} else {
			retval = 0;
		}
		return retval;
	}

	void Push(String Detalle) {
		Push(Detalle, true);
	}

	void Push(String Detalle, boolean ConvertUpper) {
 /* '*************************************************** */
 /* 'Author: Unknown */
 /* 'Last Modification: 14/11/2010 */
 /* '14/11/2010: ZaMa - Ahora se valida si supera un maximo previamente establecido. */
 /* '*************************************************** */
 /* FIXME: ON ERROR RESUME NEXT */
 /* 'Mete elemento en la cola */
 
 String aux;
 aux = vb6.time + " ";
 
  if (ConvertUpper) {
  aux = aux + vb6.UCase(Detalle);
  } else {
  aux = aux + Detalle;
 }
 
 /* ' Validate that not exceed max lenght */
  if (MaxElem != 0) {
   if (Cola.Count == MaxElem) {
   /* ' Quito el primer elemento */
   Cola.Remove[FRENTE];
  }
 }
 
 Cola.Add[aux];
 
}

	String Pop() {
 String retval;
 /* FIXME: ON ERROR RESUME NEXT */
 /* 'Quita elemento de la cola */
  if (Cola.Count>0) {
  retval = Cola[FRENTE];
  Cola.Remove[FRENTE];
  } else {
  retval = 0;
 }
return retval;
}

	String PopByVal() {
		String retval;
		/* FIXME: ON ERROR RESUME NEXT */
		/* 'Call LogTarea("PopByVal SOS") */

		/* 'Quita elemento de la cola */
		if (Cola.Count > 0) {
			retval = Cola.Item[1];
		} else {
			retval = 0;
		}

		return retval;
	}

	boolean Existe(String Nombre) {
		boolean retval = false;
		/* FIXME: ON ERROR RESUME NEXT */

		String V;
		int i = 0;
		String NombreEnMayusculas;
		NombreEnMayusculas = vb6.UCase(Nombre);

		for (i = (1); i <= (Me.Longitud); i++) {
			/* 'Pablo */
			V = vb6.mid(Me.VerElemento(i), 10, vb6.Len(Me.VerElemento(i)));
			/* '/Pablo */
			/* 'V = Me.VerElemento(i) */
			if (V == NombreEnMayusculas) {
				retval = true;
				return retval;
			}
		}
		retval = false;

		return retval;
	}

	void Quitar(String Nombre) {
 /* FIXME: ON ERROR RESUME NEXT */
 String V;
 int i = 0;
 String NombreEnMayusculas;
 
 NombreEnMayusculas = vb6.UCase(Nombre);
 
  for (i = (1); i <= (Me.Longitud); i++) {
  /* 'Pablo */
  V = vb6.mid(Me.VerElemento(i), 10, vb6.Len(Me.VerElemento(i)));
  /* '/Pablo */
  /* 'V = Me.VerElemento(i) */
   if (V == NombreEnMayusculas) {
   Cola.Remove[i];
   return;
  }
 }
 
}

	void QuitarIndex(int index) {
 /* FIXME: ON ERROR RESUME NEXT */
 if (IndexValido(index)) {
 Cola.Remove[index];
 }
}

	void Class_Terminate() {
		/* 'Destruimos el objeto Cola */
		Cola = null;
	}

}