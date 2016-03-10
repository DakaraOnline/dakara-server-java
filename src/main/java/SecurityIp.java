
/*  AUTOMATICALLY CONVERTED FILE  */
/* [(0, 'ATTRIBUTE'), (1, 'VB_Name'), (5, '='), (4, '"SecurityIp"')] */
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

/* '************************************************************** */
/* ' General_IpSecurity.Bas - Maneja la seguridad de las IPs */
/* ' */
/* ' Escrito y diseñado por DuNga (ltourrilhes@gmail.com) */
/* '************************************************************** */
import enums.*;

public class SecurityIp {

	/* '************************************************* ************* */
	/* ' General_IpSecurity.Bas - Maneja la seguridad de las IPs */
	/* ' */
	/* ' Escrito y diseñado por DuNga (ltourrilhes@gmail.com) */
	/* '************************************************* ************* */

	/* 'USAMOS 2 LONGS: UNO DE LA IP, SEGUIDO DE UNO DE LA INFO */
	private static int[] IpTables = new int[0];
	private static int EntrysCounter;
	private static int MaxValue;
	/* 'Cuantas veces multiplike el EntrysCounter para que me entren? */
	private static int Multiplicado;
	static final int IntervaloEntreConexiones = 5000;

	/* '''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''' */
	/* 'Declaraciones para maximas conexiones por usuario */
	/* 'Agregado por EL OSO */
	private static int[] MaxConTables = new int[0];
	/* 'puntero a la ultima insertada */
	private static int MaxConTablesEntry;

	static final int LIMITECONEXIONESxIP = 10;

	static void InitIpTables(int OptCountersValue) {
		/* '************************************************* ************* */
		/* 'Author: Lucio N. Tourrilhes (DuNga) */
		/* 'Last Modify Date: EL OSO 21/01/06. Soporte para MaxConTables */
		/* ' */
		/* '************************************************* ************* */
		EntrysCounter = OptCountersValue;
		Multiplicado = 1;

		IpTables = new Long[0];
		IpTables = (IpTables == null) ? new Long[EntrysCounter * 2 - 1]
				: java.util.Arrays.copyOf(IpTables, EntrysCounter * 2 - 1);
		MaxValue = 0;

		MaxConTables = new Long[0];
		MaxConTables = (MaxConTables == null) ? new Long[Declaraciones.MaxUsers * 2 - 1]
				: java.util.Arrays.copyOf(MaxConTables, Declaraciones.MaxUsers * 2 - 1);
		MaxConTablesEntry = 0;

	}

	/*
	 * '''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''
	 * '''''''
	 */
	/*
	 * '''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''
	 * '''''''
	 */
	/*
	 * ''''''''''''''''''''''FUNCIONES PARA
	 * INTERVALOS'''''''''''''''''''''''''''''''''
	 */
	/*
	 * '''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''
	 * '''''''
	 */
	/*
	 * '''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''
	 * '''''''
	 */

	static void IpSecurityMantenimientoLista() {
		/* '************************************************* ************* */
		/* 'Author: Lucio N. Tourrilhes (DuNga) */
		/* 'Last Modify Date: Unknow */
		/* ' */
		/* '************************************************* ************* */
		/* 'Las borro todas cada 1 hora, asi se "renuevan" */
		EntrysCounter = EntrysCounter / Multiplicado;
		Multiplicado = 1;
		IpTables = new Long[0];
		IpTables = (IpTables == null) ? new Long[EntrysCounter * 2 - 1]
				: java.util.Arrays.copyOf(IpTables, EntrysCounter * 2 - 1);
		MaxValue = 0;
	}

	static boolean IpSecurityAceptarNuevaConexion(int ip) {
 boolean retval = false;
 /* '*************************************************  ************* */
 /* 'Author: Lucio N. Tourrilhes (DuNga) */
 /* 'Last Modify Date: Unknow */
 /* ' */
 /* '*************************************************  ************* */
 int IpTableIndex = 0;
 int tmpTime = 0;
 
 IpTableIndex = FindTableIp(ip, IP_INTERVALOS);
 
 tmpTime = (Declaraciones.GetTickCount() && 0x7FFFFFFF);
 
  if (IpTableIndex>=0) {
  /* 'No está saturando de connects? */
   if (IpTables[IpTableIndex+1]+SecurityIp.IntervaloEntreConexiones<=tmpTime) {
   IpTables[IpTableIndex+1] = (tmpTime && 0x3FFFFFFF);
   retval = true;
   Debug.PRINT"CONEXION ACEPTADA"();
   
   return retval;
   } else {
   retval = false;
   Debug.PRINT"CONEXION NO ACEPTADA"();
   
   return retval;
  }
  } else {
  IpTableIndex = !IpTableIndex;
  AddNewIpIntervalo(ip, IpTableIndex);
  IpTables[IpTableIndex+1] = (tmpTime && 0x3FFFFFFF);
  retval = true;
  
  return retval;
 }
 
return retval;
}

	static void AddNewIpIntervalo(int ip, int index) {
		/* '************************************************* ************* */
		/* 'Author: Lucio N. Tourrilhes (DuNga) */
		/* 'Last Modify Date: Unknow */
		/* ' */
		/* '************************************************* ************* */
		/* '2) Pruebo si hay espacio, sino agrando la lista */
		if (MaxValue + 1 > EntrysCounter) {
			EntrysCounter = EntrysCounter / Multiplicado;
			Multiplicado = Multiplicado + 1;
			EntrysCounter = EntrysCounter * Multiplicado;

			IpTables = (IpTables == null) ? new Long[EntrysCounter * 2 - 1]
					: java.util.Arrays.copyOf(IpTables, EntrysCounter * 2 - 1);
		}

		/* '4) Corro todo el array para arriba */
		/* '*4 (peso del long) * 2(cantidad de elementos por c/u) */
		SysTray.CopyMemory(IpTables[index + 2], IpTables[index], (MaxValue - index / 2) * 8);
		IpTables[index] = ip;

		/* '3) Subo el indicador de el maximo valor almacenado y listo :) */
		MaxValue = MaxValue + 1;
	}

	/*
	 * '
	 * '''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''
	 * ''''''
	 */
	/*
	 * '
	 * '''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''
	 * ''''''
	 */
	/*
	 * ' ''''''''''''''''''''FUNCIONES PARA LIMITES X
	 * IP''''''''''''''''''''''''''''''''
	 */
	/*
	 * '
	 * '''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''
	 * ''''''
	 */
	/*
	 * '
	 * '''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''
	 * ''''''
	 */

	static boolean IPSecuritySuperaLimiteConexiones(int ip) {
 boolean retval = false;
 int IpTableIndex = 0;
 
 IpTableIndex = FindTableIp(ip, IP_LIMITECONEXIONES);
 
  if (IpTableIndex>=0) {
  
   if (MaxConTables[IpTableIndex+1]<SecurityIp.LIMITECONEXIONESxIP) {
   General.LogIP("Agregamos conexion a " + GetAscIP[ip] + " iptableindex=" + IpTableIndex + ". Conexiones: " + MaxConTables[IpTableIndex+1]);
   Debug.PRINT"suma conexion a " + GetAscIP[ip] + " total " + MaxConTables[IpTableIndex+1]+1();
   MaxConTables[IpTableIndex+1] = MaxConTables[IpTableIndex+1]+1;
   retval = false;
   } else {
   General.LogIP("rechazamos conexion de " + GetAscIP[ip] + " iptableindex=" + IpTableIndex + ". Conexiones: " + MaxConTables[IpTableIndex+1]);
   Debug.PRINT"rechaza conexion a " + GetAscIP[ip];
   retval = true;
  }
  } else {
  retval = false;
  /* 'si hay espacio.. */
   if (MaxConTablesEntry<Declaraciones.MaxUsers) {
   IpTableIndex = !IpTableIndex;
   /* 'iptableindex es donde lo agrego */
   AddNewIpLimiteConexiones(ip, IpTableIndex);
   MaxConTables[IpTableIndex+1] = 1;
   } else {
   General.LogCriticEvent("SecurityIP.IPSecuritySuperaLimiteConexiones: Se supero la disponibilidad de slots.");
  }
 }
 
return retval;
}

	static void AddNewIpLimiteConexiones(int ip, int index) {
		/* '************************************************* ************* */
		/* 'Author: (EL OSO) */
		/* 'Last Modify Date: 16/2/2006 */
		/* '05/21/10 - Pato: Saco el uso de buffer auxiliar */
		/* '************************************************* ************* */
		/* 'Debug.Print "agrega conexion a " & ip */
		/*
		 * 'Debug.Print "(Declaraciones.MaxUsers - index) = " &
		 * (Declaraciones.MaxUsers - Index)
		 */
		/* '4) Corro todo el array para arriba */
		/* '*4 (peso del long) * 2(cantidad de elementos por c/u) */
		SysTray.CopyMemory(MaxConTables[index + 2], MaxConTables[index], (MaxConTablesEntry - index / 2) * 8);
		MaxConTables[index] = ip;

		/* '3) Subo el indicador de el maximo valor almacenado y listo :) */
		MaxConTablesEntry = MaxConTablesEntry + 1;
	}

	static void IpRestarConexion(int ip) {
		/* '*************************************************** */
		/* 'Author: Unknown */
		/* 'Last Modification: - */
		/* ' */
		/* '*************************************************** */

		/* FIXME: ON ERROR GOTO ErrHandler */

		int key = 0;
		/* 'Debug.Print "resta conexion a " & ip */

		key = FindTableIp(ip, IP_LIMITECONEXIONES);

		if (key >= 0) {
			if (MaxConTables[key + 1] > 0) {
				MaxConTables[key + 1] = MaxConTables[key + 1] - 1;
			}
			/*
			 * 'Call LogIP("restamos conexion a " & ip & " key=" & key &
			 * ". Conexiones: " & MaxConTables(key + 1))
			 */
			/* 'Comento esto, sino se nos va el HD en logs, jaja */
			if (MaxConTables[key + 1] <= 0) {
				/* 'la limpiamos */
				MaxConTablesEntry = MaxConTablesEntry - 1;

				if (key + 2 < vb6.UBound(MaxConTables)) {
					SysTray.CopyMemory(MaxConTables[key], MaxConTables[key + 2], (MaxConTablesEntry - (key / 2)) * 8);
				}
			}
			/* 'Key < 0 */
		} else {
			General.LogIP("restamos conexion a " + GetAscIP[ip] + " key=" + key + ". NEGATIVO!!");
			/*
			 * 'LogCriticEvent
			 * "SecurityIp.IpRestarconexion obtuvo un valor negativo en key"
			 */
		}

		return;

		/* FIXME: ErrHandler : */
		General.LogError("Error en IpRestarConexion. Error: " + Err.Number + " - " + Err.description + ". Ip: "
				+ GetAscIP[ip] + " Key:" + key);
	}

	/*
	 * '
	 * '''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''
	 * ''''''
	 */
	/*
	 * '
	 * '''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''
	 * ''''''
	 */
	/*
	 * ' ''''''''''''''''''''''''FUNCIONES
	 * GENERALES''''''''''''''''''''''''''''''''''''
	 */
	/*
	 * '
	 * '''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''
	 * ''''''
	 */
	/*
	 * '
	 * '''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''
	 * ''''''
	 */

	static int FindTableIp(int ip, e_SecurityIpTabla Tabla) {
		int retval = 0;
		/* '************************************************* ************* */
		/* 'Author: Lucio N. Tourrilhes (DuNga) */
		/* 'Last Modify Date: Unknow */
		/*
		 * 'Modified by Juan Martín Sotuyo Dodero (Maraxus) to use Binary
		 * Insertion
		 */
		/* '************************************************* ************* */
		int First = 0;
		int Last = 0;
		int Middle = 0;

		switch (Tabla) {
		case IP_INTERVALOS:
			First = 0;
			Last = MaxValue - 1;
			while (First <= Last) {
				Middle = (First + Last) / 2;

				if ((IpTables[Middle * 2] < ip)) {
					First = Middle + 1;
				} else if ((IpTables[Middle * 2] > ip)) {
					Last = Middle - 1;
				} else {
					retval = Middle * 2;
					return retval;
				}
			}
			retval = !(First * 2);

			break;

		case IP_LIMITECONEXIONES:

			First = 0;
			Last = MaxConTablesEntry - 1;

			while (First <= Last) {
				Middle = (First + Last) / 2;

				if (MaxConTables[Middle * 2] < ip) {
					First = Middle + 1;
				} else if (MaxConTables[Middle * 2] > ip) {
					Last = Middle - 1;
				} else {
					retval = Middle * 2;
					return retval;
				}
			}
			retval = !(First * 2);
			break;
		}
		return retval;
	}

	static int DumpTables() {
		int retval = 0;
		/* '*************************************************** */
		/* 'Author: Unknown */
		/* 'Last Modification: - */
		/* ' */
		/* '*************************************************** */

		int i = 0;

		/* FIXME WEIRD FOR */
		for (i = (0); ((2) > 0) ? (i <= (MaxConTablesEntry * 2 - 1))
				: (i >= (MaxConTablesEntry * 2 - 1)); i = i + (2)) {
			General.LogCriticEvent(GetAscIP[MaxConTables[i]] + " > " + MaxConTables[i + 1]);
		}

		return retval;
	}

}