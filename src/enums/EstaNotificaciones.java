
/*  AUTOMATICALLY CONVERTED FILE  */

package enums;

public enum EstaNotificaciones {
	CANTIDAD_ONLINE, RECORD_USUARIOS, UPTIME_SERVER, CANTIDAD_MAPAS, EVENTO_NUEVO_CLAN, HANDLE_WND_SERVER;
	public static EstaNotificaciones fromInteger(int x) {
		switch (x) {
		case 1:
			return CANTIDAD_ONLINE;
		case 2:
			return RECORD_USUARIOS;
		case 3:
			return UPTIME_SERVER;
		case 4:
			return CANTIDAD_MAPAS;
		case 5:
			return EVENTO_NUEVO_CLAN;
		case 100:
			return HANDLE_WND_SERVER;
		}
		return null;
	}

	public static int toInteger(EstaNotificaciones x) {
		switch (x) {
		case CANTIDAD_ONLINE:
			return 1;
		case RECORD_USUARIOS:
			return 2;
		case UPTIME_SERVER:
			return 3;
		case CANTIDAD_MAPAS:
			return 4;
		case EVENTO_NUEVO_CLAN:
			return 5;
		case HANDLE_WND_SERVER:
			return 100;
		}
		return 0;
	}

	public int toInteger() {
		return toInteger(this);
	}
}
