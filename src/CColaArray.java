
/*  AUTOMATICALLY CONVERTED FILE  */
/* [(0, 'ATTRIBUTE'), (1, 'VB_Name'), (5, '='), (4, '"CColaArray"')] */
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

public class CColaArray {

	private int m_maxelem;

	private String[] m_array = new String[0]; /* XXX */
	private int m_lastelem;
	private int m_firstelem;
	private int m_size;

	boolean IsEmpty() {
		boolean retval;
		retval = m_size == 0;
		return retval;
	}

	boolean IsFull() {
		boolean retval;
		/* 'IsFull = m_lastelem = m_maxelem */
		retval = m_size == m_maxelem;
		return retval;
	}

	boolean Push(String aString) {
		boolean retval;

		if (!Me.IsFull) {

			if (Me.IsEmpty) {
				m_firstelem = 1;
			}

			m_lastelem = m_lastelem + 1;
			if ((m_lastelem > m_maxelem)) {
				m_lastelem = m_lastelem - m_maxelem;
			}
			m_size = m_size + 1;
			m_array[m_lastelem] = aString;

			retval = true;
		} else {
			retval = false;
		}

		return retval;
	}

	String Pop() {
		String retval;

		if (!Me.IsEmpty) {

			retval = m_array[m_firstelem];
			m_firstelem = m_firstelem + 1;
			if ((m_firstelem > m_maxelem)) {
				m_firstelem = m_firstelem - m_maxelem;
			}
			m_size = m_size - 1;

			/* 'If m_firstelem > m_lastelem And m_size = 0 Then */
			if (m_size == 0) {
				m_lastelem = 0;
				m_firstelem = 0;
				m_size = 0;
			}
		} else {
			retval = "";

		}

		return retval;
	}

	void Class_Initialize() {
		m_lastelem = 0;
		m_firstelem = 0;
		m_size = 0;
		m_maxelem = 300;

		m_array = new None[0];
		m_array = (m_array == null) ? new None[1 + m_maxelem] : java.util.Arrays.copyOf(m_array, 1 + m_maxelem);
	}

	int MaxElems() {
		int retval;
		retval = m_maxelem;
		return retval;
	}

	void MaxElems(int lNewValue) {
		m_maxelem = lNewValue;
		m_array = (m_array == null) ? new None[1 + m_maxelem] : java.util.Arrays.copyOf(m_array, 1 + m_maxelem);

	}

}