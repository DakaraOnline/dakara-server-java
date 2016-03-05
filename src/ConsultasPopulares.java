/*  AUTOMATICALLY CONVERTED FILE  */
/* [(0, 'ATTRIBUTE'), (1, 'VB_Name'), (5, '='), (4, '"ConsultasPopulares"')] */
/* [(0, 'ATTRIBUTE'), (1, 'VB_GlobalNameSpace'), (5, '='), (1, 'False')] */
/* [(0, 'ATTRIBUTE'), (1, 'VB_Creatable'), (5, '='), (1, 'True')] */
/* [(0, 'ATTRIBUTE'), (1, 'VB_PredeclaredId'), (5, '='), (1, 'False')] */
/* [(0, 'ATTRIBUTE'), (1, 'VB_Exposed'), (5, '='), (1, 'False')] */
/* '************************************************************** */
/* ' ConsultasPopulares.cls */
/* ' */
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

import enums.*;

public class ConsultasPopulares {
	/* 'Modulo de consultas popluares */
	/* 'En realidad el modulo inicial guardaba los datos de las votaciones */
	/*
	 * 'en memoria, pero no le vi el punto, las votaciones son de vez en cuando
	 * nomas
	 */
	/*
	 * 'y el query a un .ini que registra todos los mails q ya votaron, es
	 * soportable
	 */
	/*
	 * 'si cuando uno vota y averiguo que el mail ya voto pero el PJ no,
	 * entonces seteo
	 */
	/* 'el flag de yavoto en el charfile ese tambien, */
	/*
	 * 'por lo que la busqueda secuencial en el .dat que tiene todos los mails q
	 * ya votaron
	 */
	/* 'se hara .. 1 vez por PJ nomas. */
	/* ' */
	/* 'Hecha por el oso */

	static final int ARCHIVOMAILS = "\\logs\\votaron.dat";
	static final int ARCHIVOCONFIG = "\\dat\\consultas.dat";

	private int pEncuestaActualNum;
	private String pEncuestaActualTex;
	private int pNivelRequerido;
	private int[] pOpciones = new int[0]; /* XXX */

	void Numero(int NumEncuesta) {
		pEncuestaActualNum = NumEncuesta;
	}

	int Numero() {
		int retval;
		retval = pEncuestaActualNum;
		return retval;
	}

	void texto(String Descripcion) {
		pEncuestaActualTex = Descripcion;
	}

	String texto() {
		String retval;
		retval = pEncuestaActualTex;
		return retval;
	}

	void LoadData() {
		int CantOpciones;
		int i;

		pEncuestaActualNum = vb6.val(ES.GetVar(vb6.App.Instance().Path + ARCHIVOCONFIG, "INIT", "ConsultaActual"));
		pEncuestaActualTex = ES.GetVar(vb6.App.Instance().Path + ARCHIVOCONFIG, "INIT", "ConsultaActualTexto");
		pNivelRequerido = ES.GetVar(vb6.App.Instance().Path + ARCHIVOCONFIG, "INIT", "NivelRequerido");

		if (pEncuestaActualNum > 0) {
			/* 'cargo todas las opciones */
			CantOpciones = vb6.val(ES.GetVar(vb6.App.Instance().Path + ARCHIVOCONFIG, "ENCUESTA" + pEncuestaActualNum,
					"CANTOPCIONES"));
			pOpciones = new Integer[0];
			pOpciones = (pOpciones == null) ? new Integer[1 + CantOpciones]
					: java.util.Arrays.copyOf(pOpciones, 1 + CantOpciones);
			for (i = (1); i <= (CantOpciones); i++) {
				pOpciones[i] = vb6.val(ES.GetVar(vb6.App.Instance().Path + ARCHIVOCONFIG,
						"ENCUESTA" + pEncuestaActualNum, "OPCION" + i));
			}
		}
	}

	String doVotar(int UserIndex, int opcion) {
		String retval;
		/* FIXME: ON ERROR GOTO errorh */

		boolean YaVoto;
		String CharFile;
		int sufragio;

		/* 'revisar q no haya votado */
		/* 'grabar en el charfile el numero de encuesta */
		/* 'actualizar resultados encuesta */
		if (pEncuestaActualNum == 0) {
			retval = "No hay consultas populares abiertas";
			return retval;
		}

		CharFile = Declaraciones.CharPath + Declaraciones.UserList[UserIndex].Name + ".chr";

		if ((Declaraciones.UserList[UserIndex].Stats.ELV >= pNivelRequerido)) {
			if ((OpcionValida(opcion))) {
				YaVoto = vb6.val(ES.GetVar(CharFile, "CONSULTAS", "Voto")) >= pEncuestaActualNum;
				if (!YaVoto) {
					if (!MailYaVoto(Declaraciones.UserList[UserIndex].email)) {
						/* 'pj apto para votar */
						sufragio = vb6.CLng(vb6.val(ES.GetVar(vb6.App.Instance().Path + ARCHIVOCONFIG,
								"RESULTADOS" + pEncuestaActualNum, "V" + opcion)));
						sufragio = sufragio + 1;
						ES.WriteVar(vb6.App.Instance().Path + ARCHIVOCONFIG, "RESULTADOS" + pEncuestaActualNum,
								"V" + opcion, vb6.str(sufragio));
						retval = "Tu voto ha sido computado. Opcion: " + opcion;
						MarcarPjComoQueYaVoto(UserIndex);
						MarcarMailComoQueYaVoto(Declaraciones.UserList[UserIndex].email);
					} else {
						MarcarPjComoQueYaVoto(UserIndex);
						retval = "Este email ya voto en la consulta: " + pEncuestaActualTex;
					}
				} else {
					retval = "Este personaje ya voto en la consulta: " + pEncuestaActualTex;
				}
			} else {
				retval = "Esa no es una opcion para votar";
			}
		} else {
			retval = "Para votar en esta consulta debes ser nivel " + pNivelRequerido + " o superior";
		}

		return retval;
		/* FIXME: errorh : */
		General.LogError("Error en ConsultasPopularse.doVotar: " + Err.description);

		return retval;
	}

	String SendInfoEncuesta(int UserIndex) {
		String retval;
		int i;
		Protocol.WriteConsoleMsg(UserIndex, "CONSULTA POPULAR NUMERO " + pEncuestaActualNum,
				FontTypeNames.FONTTYPE_GUILD);
		Protocol.WriteConsoleMsg(UserIndex, pEncuestaActualTex, FontTypeNames.FONTTYPE_GUILD);
		Protocol.WriteConsoleMsg(UserIndex, " Opciones de voto: ", FontTypeNames.FONTTYPE_GUILDMSG);
		for (i = (1); i <= (vb6.UBound(pOpciones)); i++) {
			Protocol.WriteConsoleMsg(UserIndex, "(Opcion " + i + "): "
					+ ES.GetVar(vb6.App.Instance().Path + ARCHIVOCONFIG, "ENCUESTA" + pEncuestaActualNum, "OPCION" + i),
					FontTypeNames.FONTTYPE_GUILDMSG);
		}
		Protocol.WriteConsoleMsg(UserIndex,
				" Para votar una opcion, escribe /encuesta NUMERODEOPCION, por ejemplo para votar la opcion 1, escribe /encuesta 1. Tu voto no podra ser cambiado.",
				FontTypeNames.FONTTYPE_VENENO);
		return retval;
	}

	void MarcarPjComoQueYaVoto(int UserIndex) {
		ES.WriteVar(Declaraciones.CharPath + Declaraciones.UserList[UserIndex].Name + ".chr", "CONSULTAS", "Voto",
				vb6.str(pEncuestaActualNum));
	}

	boolean MailYaVoto(String email) {
		boolean retval;
		/*
		 * 'abro el archivo, while not eof levnato 1 linea y comparo. Si da
		 * true, cierro
		 */
		int ArchN;
		String Tmp;

		retval = false;

		/* ' Si no existe no voto. */
		if (!General.FileExist(vb6.App.Instance().Path + ARCHIVOMAILS)) {
			return retval;
		}

		ArchN = vb6.FreeFile();

		/* FIXME: OPEN App . Path & ARCHIVOMAILS FOR INPUT AS # ArchN */

		while (!clsByteBuffer.Eof(ArchN)) {
			/* FIXME: LINE INPUT # ArchN , Tmp */
			if (email == Tmp) {
				retval = true;
				/* FIXME: CLOSE # ArchN */
				return retval;
			}
		}

		/* FIXME: CLOSE # ArchN */
		return retval;
	}

	void MarcarMailComoQueYaVoto(String email) {
		int ArchN;

		ArchN = vb6.FreeFile();

		/* FIXME: OPEN App . Path & ARCHIVOMAILS FOR Append AS # ArchN */
		/* FIXME: PRINT # ArchN , email */

		/* FIXME: CLOSE # ArchN */

	}

	boolean OpcionValida(int opcion) {
		boolean retval;
		retval = opcion > 0 && opcion <= vb6.UBound(pOpciones);
		return retval;
	}

}