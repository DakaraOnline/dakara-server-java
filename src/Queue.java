
/*  AUTOMATICALLY CONVERTED FILE  */
/* [(0, 'ATTRIBUTE'), (1, 'VB_Name'), (5, '='), (4, '"Queue"')] */
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

public class Queue {

	static public class tVertice {
		public int X;
		public int Y;
	}

	static final int MAXELEM = 1000;

	private static tVertice[] m_array = new tVertice[0];
	private static int m_lastelem;
	private static int m_firstelem;
	private static int m_size;

	static boolean IsEmpty() {
		boolean retval = false;
		retval = m_size == 0;
		return retval;
	}

	static boolean IsFull() {
		boolean retval = false;
		retval = m_lastelem == Queue.MAXELEM;
		return retval;
	}

	static boolean Push(tVertice /* FIXME BYREF!! */ Vertice) {
		boolean retval = false;

		if (!IsFull()) {

			if (IsEmpty()) {
				m_firstelem = 1;
			}

			m_lastelem = m_lastelem + 1;
			m_size = m_size + 1;
			m_array[m_lastelem] = Vertice;

			retval = true;
		} else {
			retval = false;
		}

		return retval;
	}

	static tVertice Pop() {
		tVertice retval;

		if (!IsEmpty()) {

			retval = m_array[m_firstelem];
			m_firstelem = m_firstelem + 1;
			m_size = m_size - 1;

			if (m_firstelem > m_lastelem && m_size == 0) {
				m_lastelem = 0;
				m_firstelem = 0;
				m_size = 0;
			}

		}

		return retval;
	}

	static void InitQueue() {
		m_array = new tVertice[0];
		m_array = (m_array == null) ? new tVertice[Queue.MAXELEM] : java.util.Arrays.copyOf(m_array, Queue.MAXELEM);
		m_lastelem = 0;
		m_firstelem = 0;
		m_size = 0;
	}

}