/*  AUTOMATICALLY CONVERTED FILE  */

/* 
 * Este archivo fue convertido automaticamente, por un script, desde el 
 * código fuente original de Visual Basic 6.
 */

/* [(0, 'ATTRIBUTE'), (1, 'VB_Name'), (5, '='), (4, '"modInvisibles"')] */
import enums.*;

public class modInvisibles {

	/* ' 0 = viejo */
	/* ' 1 = nuevo */
	/* # CONST MODO_INVISIBILIDAD = 0 */
	static final int MODO_INVISIBILIDAD = 0;

	/*
	 * ' cambia el estado de invisibilidad a 1 o 0 dependiendo del modo: true o
	 * false
	 */
	/* ' */
	public static void PonerInvisible(int UserIndex, boolean estado) {
		/* # IF MODO_INVISIBILIDAD = 0 THEN */

		Declaraciones.UserList[UserIndex].flags.invisible = vb6.IIf(estado, 1, 0);
		Declaraciones.UserList[UserIndex].flags.Oculto = vb6.IIf(estado, 1, 0);
		Declaraciones.UserList[UserIndex].Counters.Invisibilidad = 0;

		UsUaRiOs.SetInvisible(UserIndex, Declaraciones.UserList[UserIndex].Char.CharIndex, ! /* FIXME */estado);
		/*
		 * 'Call SendData(SendTarget.ToPCArea, UserIndex,
		 * PrepareMessageSetInvisible(UserList(UserIndex).Char.CharIndex, Not
		 * estado))
		 */

		/* # ELSE */

		/* # END IF */
	}

}