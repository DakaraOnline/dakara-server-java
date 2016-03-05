
/*  AUTOMATICALLY CONVERTED FILE  */

package enums;

public enum e_SecurityIpTabla {
	IP_INTERVALOS, IP_LIMITECONEXIONES;
	public static e_SecurityIpTabla fromInteger(int x) {
		switch (x) {
		case 1:
			return IP_INTERVALOS;
		case 2:
			return IP_LIMITECONEXIONES;
		}
		return null;
	}

	public static int toInteger(e_SecurityIpTabla x) {
		switch (x) {
		case IP_INTERVALOS:
			return 1;
		case IP_LIMITECONEXIONES:
			return 2;
		}
		return 0;
	}
}
