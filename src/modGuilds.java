/*  AUTOMATICALLY CONVERTED FILE  */
/* [(0, 'ATTRIBUTE'), (1, 'VB_Name'), (5, '='), (4, '"modGuilds"')] */
/* '************************************************************** */
/* ' modGuilds.bas - Module to allow the usage of areas instead of maps. */
/* ' Saves a lot of bandwidth. */
/* ' */
/* ' Implemented by Mariano Barrou (El Oso) */
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

public class modGuilds {

	/* 'guilds nueva version. Hecho por el oso, eliminando los problemas */
	/* 'de sincronizacion con los datos en el HD... entre varios otros */
	/* 'º¬ */

	/* '''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''' */
	/* '''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''' */
	/* 'DECLARACIOENS PUBLICAS CONCERNIENTES AL JUEGO */
	/* 'Y CONFIGURACION DEL SISTEMA DE CLANES */
	/* '''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''' */
	private static String GUILDINFOFILE;
	/* 'archivo .\guilds\guildinfo.ini o similar */

	static final int MAX_GUILDS = 1000;
	/* 'cantidad maxima de guilds en el servidor */

	public static int CANTIDADDECLANES;
	/* 'cantidad actual de clanes en el servidor */

	private static clsClan[] guilds = new clsClan[1 + modGuilds.MAX_GUILDS];
	/* 'array global de guilds, se indexa por userlist().guildindex */

	static final int CANTIDADMAXIMACODEX = 8;
	/* 'cantidad maxima de codecs que se pueden definir */

	static final int MAXASPIRANTES = 10;
	/*
	 * 'cantidad maxima de aspirantes que puede tener un clan acumulados a la
	 * vez
	 */

	static final int MAXANTIFACCION = 5;
	/*
	 * 'puntos maximos de antifaccion que un clan tolera antes de ser cambiada
	 * su alineacion
	 */

	/* 'alineaciones permitidas */

	/* 'numero de .wav del cliente */

	/* 'estado entre clanes */
	/* '''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''' */
	/* '''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''' */
	/* '''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''' */

	static void LoadGuildsDB() {
		/* '*************************************************** */
		/* 'Author: Unknown */
		/* 'Last Modification: - */
		/* ' */
		/* '*************************************************** */

		String CantClanes;
		int i = 0;
		String TempStr;
		ALINEACION_GUILD Alin;

		Declaraciones.GUILDINFOFILE = vb6.App.Instance().Path + "\\guilds\\guildsinfo.inf";

		CantClanes = ES.GetVar(Declaraciones.GUILDINFOFILE, "INIT", "nroGuilds");

		if (vb6.IsNumeric(CantClanes)) {
			modGuilds.CANTIDADDECLANES = vb6.CInt(CantClanes);
		} else {
			modGuilds.CANTIDADDECLANES = 0;
		}

		for (i = (1); i <= (modGuilds.CANTIDADDECLANES); i++) {
			guilds[i] = new clsClan();
			TempStr = ES.GetVar(Declaraciones.GUILDINFOFILE, "GUILD" + i, "GUILDNAME");
			Alin = String2Alineacion(ES.GetVar(Declaraciones.GUILDINFOFILE, "GUILD" + i, "Alineacion"));
			guilds[i].Inicializar(TempStr, i, Alin);
		}

	}

	static boolean m_ConectarMiembroAClan(int UserIndex, int GuildIndex) {
		boolean retval = false;
		/* '*************************************************** */
		/* 'Author: Unknown */
		/* 'Last Modification: - */
		/* ' */
		/* '*************************************************** */

		boolean NuevaA = false;
		String News;

		/* 'x las dudas... */
		if (GuildIndex > modGuilds.CANTIDADDECLANES || GuildIndex <= 0) {
			return retval;
		}
		if (m_EstadoPermiteEntrar(UserIndex, GuildIndex)) {
			guilds[GuildIndex].ConectarMiembro(UserIndex);
			Declaraciones.UserList[UserIndex].GuildIndex = GuildIndex;
			retval = true;
		} else {
			retval = m_ValidarPermanencia(UserIndex, true, NuevaA);
			if (NuevaA) {
				News = News + "El clan tiene nueva alineación.";
			}
			/*
			 * 'If NuevoL Or NuevaA Then Call
			 * guilds(GuildIndex).SetGuildNews(News)
			 */
		}

		return retval;
	}

	static boolean m_ValidarPermanencia(int UserIndex, boolean SumaAntifaccion,
			boolean /* FIXME BYREF!! */ CambioAlineacion) {
		boolean retval = false;
		/* '*************************************************** */
		/* 'Autor: Unknown (orginal version) */
		/* 'Last Modification: 14/12/2009 */
		/*
		 * '25/03/2009: ZaMa - Desequipo los items faccionarios que tenga el
		 * funda al abandonar la faccion
		 */
		/* '14/12/2009: ZaMa - La alineacion del clan depende del lider */
		/*
		 * '14/02/2010: ZaMa - Ya no es necesario saber si el lider cambia, ya
		 * que no puede cambiar.
		 */
		/* '*************************************************** */

		int GuildIndex = 0;

		retval = true;

		GuildIndex = Declaraciones.UserList[UserIndex].GuildIndex;
		if (GuildIndex > modGuilds.CANTIDADDECLANES && GuildIndex <= 0) {
			return retval;
		}

		if (!m_EstadoPermiteEntrar(UserIndex, GuildIndex)) {

			/* ' Es el lider, bajamos 1 rango de alineacion */
			if (m_EsGuildLeader(Declaraciones.UserList[UserIndex].Name, GuildIndex)) {
				General.LogClanes(Declaraciones.UserList[UserIndex].Name + ", líder de " + guilds[GuildIndex].GuildName
						+ " hizo bajar la alienación de su clan.");

				CambioAlineacion = true;

				/*
				 * ' Por si paso de ser armada/legion a pk/ciuda, chequeo de
				 * nuevo
				 */
				do {
					UpdateGuildMembers(GuildIndex);
				} while (!(m_EstadoPermiteEntrar(UserIndex, GuildIndex)));
			} else {
				General.LogClanes(Declaraciones.UserList[UserIndex].Name + " de " + guilds[GuildIndex].GuildName
						+ " es expulsado en validar permanencia.");

				retval = false;
				if (SumaAntifaccion) {
					guilds[GuildIndex].PuntosAntifaccion = guilds[GuildIndex].PuntosAntifaccion + 1;
				}

				CambioAlineacion = guilds[GuildIndex].PuntosAntifaccion == modGuilds.MAXANTIFACCION;

				General.LogClanes(Declaraciones.UserList[UserIndex].Name + " de " + guilds[GuildIndex].GuildName
						+ vb6.IIf(CambioAlineacion, " SI ", " NO ") + "provoca cambio de alineación. MAXANT:"
						+ CambioAlineacion);

				m_EcharMiembroDeClan(-1, Declaraciones.UserList[UserIndex].Name);

				/*
				 * ' Llegamos a la maxima cantidad de antifacciones permitidas,
				 * bajamos un grado de alineación
				 */
				if (CambioAlineacion) {
					UpdateGuildMembers(GuildIndex);
				}
			}
		}
		return retval;
	}

	static void UpdateGuildMembers(int GuildIndex) {
		/* '*************************************************** */
		/* 'Autor: ZaMa */
		/* 'Last Modification: 14/01/2010 (ZaMa) */
		/* '14/01/2010: ZaMa - Pulo detalles en el funcionamiento general. */
		/* '*************************************************** */
		String[] GuildMembers;
		int TotalMembers = 0;
		int MemberIndex = 0;
		boolean Sale = false;
		String MemberName;
		int UserIndex = 0;
		int Reenlistadas = 0;

		/*
		 * ' Si devuelve true, cambio a neutro y echamos a todos los que estén
		 * de mas, sino no echamos a nadie
		 */
		/* 'ALINEACION_NEUTRO) */
		if (guilds[GuildIndex].CambiarAlineacion(BajarGrado(GuildIndex))) {

			/*
			 * 'uso GetMemberList y no los iteradores pq voy a rajar gente y
			 * puedo alterar
			 */
			/* 'internamente al iterador en el proceso */
			GuildMembers = guilds[GuildIndex].GetMemberList();
			TotalMembers = vb6.UBound(GuildMembers);

			for (MemberIndex = (0); MemberIndex <= (TotalMembers); MemberIndex++) {
				MemberName = GuildMembers[MemberIndex];

				/* 'vamos a violar un poco de capas.. */
				UserIndex = Extra.NameIndex(MemberName);
				if (UserIndex > 0) {
					Sale = !m_EstadoPermiteEntrar(UserIndex, GuildIndex);
				} else {
					Sale = !m_EstadoPermiteEntrarChar(MemberName, GuildIndex);
				}

				if (Sale) {
					/* 'hay que sacarlo de las facciones */
					if (m_EsGuildLeader(MemberName, GuildIndex)) {

						if (UserIndex > 0) {
							if (Declaraciones.UserList[UserIndex].Faccion.ArmadaReal != 0) {
								ModFacciones.ExpulsarFaccionReal(UserIndex);
								/* ' No cuenta como reenlistada :p. */
								Declaraciones.UserList[UserIndex].Faccion.Reenlistadas = Declaraciones.UserList[UserIndex].Faccion.Reenlistadas
										- 1;
							} else if (Declaraciones.UserList[UserIndex].Faccion.FuerzasCaos != 0) {
								ModFacciones.ExpulsarFaccionCaos(UserIndex);
								/* ' No cuenta como reenlistada :p. */
								Declaraciones.UserList[UserIndex].Faccion.Reenlistadas = Declaraciones.UserList[UserIndex].Faccion.Reenlistadas
										- 1;
							}
						} else {
							if (General.FileExist(Declaraciones.CharPath + MemberName + ".chr")) {
								ES.WriteVar(Declaraciones.CharPath + MemberName + ".chr", "FACCIONES", "EjercitoCaos",
										0);
								ES.WriteVar(Declaraciones.CharPath + MemberName + ".chr", "FACCIONES", "EjercitoReal",
										0);
								Reenlistadas = ES.GetVar(Declaraciones.CharPath + MemberName + ".chr", "FACCIONES",
										"Reenlistadas");
								ES.WriteVar(Declaraciones.CharPath + MemberName + ".chr", "FACCIONES", "Reenlistadas",
										vb6.IIf(Reenlistadas > 1, Reenlistadas - 1, Reenlistadas));
							}
						}
						/* 'sale si no es guildLeader */
					} else {
						m_EcharMiembroDeClan(-1, MemberName);
					}
				}
			}
		} else {
			/* ' Resetea los puntos de antifacción */
			guilds[GuildIndex].PuntosAntifaccion = 0;
		}
	}

	static ALINEACION_GUILD BajarGrado(int GuildIndex) {
		ALINEACION_GUILD retval;
		/* '*************************************************** */
		/* 'Autor: ZaMa */
		/* 'Last Modification: 27/11/2009 */
		/* 'Reduce el grado de la alineacion a partir de la alineacion dada */
		/* '*************************************************** */

		switch (guilds[GuildIndex].Alineacion) {
		case ALINEACION_ARMADA:
			retval = ALINEACION_CIUDA;
			break;

		case ALINEACION_LEGION:
			retval = ALINEACION_CRIMINAL;
			break;

		default:
			retval = ALINEACION_NEUTRO;
			break;
		}

		return retval;
	}

	static void m_DesconectarMiembroDelClan(int UserIndex, int GuildIndex) {
		/* '*************************************************** */
		/* 'Author: Unknown */
		/* 'Last Modification: - */
		/* ' */
		/* '*************************************************** */

		if (Declaraciones.UserList[UserIndex].GuildIndex > modGuilds.CANTIDADDECLANES) {
			return;
		}
		guilds[GuildIndex].DesConectarMiembro(UserIndex);
	}

	static boolean m_EsGuildLeader(String /* FIXME BYREF!! */ PJ, int GuildIndex) {
		boolean retval = false;
		/* '*************************************************** */
		/* 'Author: Unknown */
		/* 'Last Modification: - */
		/* ' */
		/* '*************************************************** */

		retval = (vb6.UCase(PJ) == vb6.UCase(vb6.Trim(guilds[GuildIndex].GetLeader())));
		return retval;
	}

	static boolean m_EsGuildFounder(String /* FIXME BYREF!! */ PJ, int GuildIndex) {
		boolean retval = false;
		/* '*************************************************** */
		/* 'Author: Unknown */
		/* 'Last Modification: - */
		/* ' */
		/* '*************************************************** */

		retval = (vb6.UCase(PJ) == vb6.UCase(vb6.Trim(guilds[GuildIndex].Fundador)));
		return retval;
	}

	static int m_EcharMiembroDeClan(int Expulsador, String Expulsado) {
		int retval = 0;
		/* '*************************************************** */
		/* 'Author: Unknown */
		/* 'Last Modification: - */
		/* ' */
		/* '*************************************************** */

		/* 'UI echa a Expulsado del clan de Expulsado */
		int UserIndex = 0;
		int GI = 0;

		retval = 0;

		UserIndex = Extra.NameIndex(Expulsado);
		if (UserIndex > 0) {
			/* 'pj online */
			GI = Declaraciones.UserList[UserIndex].GuildIndex;
			if (GI > 0) {
				if (m_PuedeSalirDeClan(Expulsado, GI, Expulsador)) {
					guilds[GI].DesConectarMiembro(UserIndex);
					guilds[GI].ExpulsarMiembro(Expulsado);
					General.LogClanes(Expulsado + " ha sido expulsado de " + guilds[GI].GuildName + " Expulsador = "
							+ Expulsador);
					Declaraciones.UserList[UserIndex].GuildIndex = 0;
					UsUaRiOs.RefreshCharStatus(UserIndex);
					retval = GI;
				} else {
					retval = 0;
				}
			} else {
				retval = 0;
			}
		} else {
			/* 'pj offline */
			GI = GetGuildIndexFromChar(Expulsado);
			if (GI > 0) {
				if (m_PuedeSalirDeClan(Expulsado, GI, Expulsador)) {
					guilds[GI].ExpulsarMiembro(Expulsado);
					General.LogClanes(Expulsado + " ha sido expulsado de " + guilds[GI].GuildName + " Expulsador = "
							+ Expulsador);
					retval = GI;
				} else {
					retval = 0;
				}
			} else {
				retval = 0;
			}
		}

		return retval;
	}

	static void ActualizarWebSite(int UserIndex, String /* FIXME BYREF!! */ Web) {
		/* '*************************************************** */
		/* 'Author: Unknown */
		/* 'Last Modification: - */
		/* ' */
		/* '*************************************************** */

		int GI = 0;

		GI = Declaraciones.UserList[UserIndex].GuildIndex;
		if (GI <= 0 || GI > modGuilds.CANTIDADDECLANES) {
			return;
		}

		if (!m_EsGuildLeader(Declaraciones.UserList[UserIndex].Name, GI)) {
			return;
		}

		guilds[GI].SetURL(Web);

	}

	static void ChangeCodexAndDesc(String /* FIXME BYREF!! */ desc, String[] /* FIXME BYREF!! */ codex, int GuildIndex) {
 /* '*************************************************** */
 /* 'Author: Unknown */
 /* 'Last Modification: - */
 /* ' */
 /* '*************************************************** */
 
 int i = 0;
 
 if (GuildIndex<1 || GuildIndex>modGuilds.CANTIDADDECLANES) {
 return;
 }
 
  guilds[GuildIndex].SetDesc(desc);
  
   for (i = (0); i <= (vb6.UBound(codex[])); i++) {
   guilds[GuildIndex].SetCodex(i, codex[i]);
  }
  
   for (i = (i); i <= (modGuilds.CANTIDADMAXIMACODEX); i++) {
   guilds[GuildIndex].SetCodex(i, "");
  }
}

	static void ActualizarNoticias(int UserIndex, String /* FIXME BYREF!! */ Datos) {
		/* '*************************************************** */
		/* 'Author: Unknown */
		/* 'Last Modification: 21/02/2010 */
		/*
		 * '21/02/2010: ZaMa - Ahora le avisa a los miembros que cambio el
		 * guildnews.
		 */
		/* '*************************************************** */

		int GI = 0;

		GI = Declaraciones.UserList[UserIndex].GuildIndex;

		if (GI <= 0 || GI > modGuilds.CANTIDADDECLANES) {
			return;
		}

		if (!m_EsGuildLeader(Declaraciones.UserList[UserIndex].Name, GI)) {
			return;
		}

		guilds[GI].SetGuildNews(Datos);

		modSendData.SendData(SendTarget.ToDiosesYclan, Declaraciones.UserList[UserIndex].GuildIndex,
				Protocol.PrepareMessageGuildChat(
						Declaraciones.UserList[UserIndex].Name + " ha actualizado las noticias del clan!"));
	}

	static boolean CrearNuevoClan(int FundadorIndex, String /* FIXME BYREF!! */ desc, String /* FIXME BYREF!! */ GuildName, String /* FIXME BYREF!! */ URL, String[] /* FIXME BYREF!! */ codex, ALINEACION_GUILD Alineacion, String /* FIXME BYREF!! */ refError) {
 boolean retval = false;
 /* '*************************************************** */
 /* 'Author: Unknown */
 /* 'Last Modification: - */
 /* ' */
 /* '*************************************************** */
 
 int CantCodex = 0;
 int i = 0;
 String DummyString;
 
 retval = false;
  if (!PuedeFundarUnClan(FundadorIndex, Alineacion, DummyString)) {
  refError = DummyString;
  return retval;
 }
 
  if (GuildName == "" || !GuildNameValido(GuildName)) {
  refError = "Nombre de clan inválido.";
  return retval;
 }
 
  if (YaExiste(GuildName)) {
  refError = "Ya existe un clan con ese nombre.";
  return retval;
 }
 
 CantCodex = vb6.UBound(codex[])+1;
 
 /* 'tenemos todo para fundar ya */
  if (modGuilds.CANTIDADDECLANES<vb6.UBound(guilds)) {
  modGuilds.CANTIDADDECLANES = modGuilds.CANTIDADDECLANES+1;
  /* 'ReDim Preserve Guilds(1 To CANTIDADDECLANES) As clsClan */
  
  /* 'constructor custom de la clase clan */
  guilds[modGuilds.CANTIDADDECLANES] = new clsClan();
  
   guilds[modGuilds.CANTIDADDECLANES].Inicializar(GuildName, modGuilds.CANTIDADDECLANES, Alineacion);
   
   /* 'Damos de alta al clan como nuevo inicializando sus archivos */
   guilds[modGuilds.CANTIDADDECLANES].InicializarNuevoClan(Declaraciones.UserList[FundadorIndex].Name);
   
   /* 'seteamos codex y descripcion */
    for (i = (1); i <= (CantCodex); i++) {
    guilds[modGuilds.CANTIDADDECLANES].SetCodex(i, codex[i-1]);
   }
   guilds[modGuilds.CANTIDADDECLANES].SetDesc(desc);
   guilds[modGuilds.CANTIDADDECLANES].SetGuildNews("Clan creado con alineación: " + Alineacion2String(Alineacion));
   guilds[modGuilds.CANTIDADDECLANES].SetLeader(Declaraciones.UserList[FundadorIndex].Name);
   guilds[modGuilds.CANTIDADDECLANES].SetURL(URL);
   
   /* '"conectamos" al nuevo miembro a la lista de la clase */
   guilds[modGuilds.CANTIDADDECLANES].AceptarNuevoMiembro(Declaraciones.UserList[FundadorIndex].Name);
   guilds[modGuilds.CANTIDADDECLANES].ConectarMiembro(FundadorIndex);
  
  Declaraciones.UserList[FundadorIndex].GuildIndex = modGuilds.CANTIDADDECLANES;
  UsUaRiOs.RefreshCharStatus(FundadorIndex);
  
   for (i = (1); i <= (modGuilds.CANTIDADDECLANES-1); i++) {
   guilds[i].ProcesarFundacionDeOtroClan();
  }
  } else {
  refError = "No hay más slots para fundar clanes. Consulte a un administrador.";
  return retval;
 }
 
 retval = true;
return retval;
}

	static void SendGuildNews(int UserIndex) {
		/* '*************************************************** */
		/* 'Author: Unknown */
		/* 'Last Modification: - */
		/* ' */
		/* '*************************************************** */

		int GuildIndex = 0;
		int i = 0;
		int go = 0;

		GuildIndex = Declaraciones.UserList[UserIndex].GuildIndex;
		if (GuildIndex == 0) {
			return;
		}

		String[] enemies;

		if (guilds[GuildIndex].CantidadEnemys) {
			enemies = new String[0];
			enemies = (enemies == null) ? new String[guilds[GuildIndex].CantidadEnemys - 1]
					: java.util.Arrays.copyOf(enemies, guilds[GuildIndex].CantidadEnemys - 1);
		} else {
			enemies = new String[0];
			enemies = (enemies == null) ? new String[0] : java.util.Arrays.copyOf(enemies, 0);
		}

		String[] allies;

		if (guilds[GuildIndex].CantidadAllies) {
			allies = new String[0];
			allies = (allies == null) ? new String[guilds[GuildIndex].CantidadAllies - 1]
					: java.util.Arrays.copyOf(allies, guilds[GuildIndex].CantidadAllies - 1);
		} else {
			allies = new String[0];
			allies = (allies == null) ? new String[0] : java.util.Arrays.copyOf(allies, 0);
		}

		i = guilds[GuildIndex].Iterador_ProximaRelacion(RELACIONES_GUILD.GUERRA);
		go = 0;

		while (i > 0) {
			enemies[go] = guilds[i].GuildName;
			i = guilds[GuildIndex].Iterador_ProximaRelacion(RELACIONES_GUILD.GUERRA);
			go = go + 1;
		}

		i = guilds[GuildIndex].Iterador_ProximaRelacion(RELACIONES_GUILD.ALIADOS);
		go = 0;

		while (i > 0) {
			allies[go] = guilds[i].GuildName;
			i = guilds[GuildIndex].Iterador_ProximaRelacion(RELACIONES_GUILD.ALIADOS);
		}

		Protocol.WriteGuildNews(UserIndex, guilds[GuildIndex].GetGuildNews(), enemies, allies);

		if (guilds[GuildIndex].EleccionesAbiertas()) {
			Protocol.WriteConsoleMsg(UserIndex, "Hoy es la votación para elegir un nuevo líder para el clan.",
					FontTypeNames.FONTTYPE_GUILD);
			Protocol.WriteConsoleMsg(UserIndex,
					"La elección durará 24 horas, se puede votar a cualquier miembro del clan.",
					FontTypeNames.FONTTYPE_GUILD);
			Protocol.WriteConsoleMsg(UserIndex, "Para votar escribe /VOTO NICKNAME.", FontTypeNames.FONTTYPE_GUILD);
			Protocol.WriteConsoleMsg(UserIndex, "Sólo se computará un voto por miembro. Tu voto no puede ser cambiado.",
					FontTypeNames.FONTTYPE_GUILD);
		}

	}

	static boolean m_PuedeSalirDeClan(String /* FIXME BYREF!! */ Nombre, int GuildIndex, int QuienLoEchaUI) {
		boolean retval = false;
		/* '*************************************************** */
		/* 'Author: Unknown */
		/* 'Last Modification: - */
		/* ' */
		/* '*************************************************** */

		/* 'sale solo si no es fundador del clan. */

		retval = false;
		if (GuildIndex == 0) {
			return retval;
		}

		/*
		 * 'esto es un parche, si viene en -1 es porque la invoca la rutina de
		 * expulsion automatica de clanes x antifacciones
		 */
		if (QuienLoEchaUI == -1) {
			retval = true;
			return retval;
		}

		/* 'cuando UI no puede echar a nombre? */
		/*
		 * 'si no es gm Y no es lider del clan del pj Y no es el mismo que se va
		 * voluntariamente
		 */
		if (Declaraciones.UserList[QuienLoEchaUI].flags.Privilegios && PlayerType.User) {
			if (!m_EsGuildLeader(vb6.UCase(Declaraciones.UserList[QuienLoEchaUI].Name), GuildIndex)) {
				/* 'si no sale voluntariamente... */
				if (vb6.UCase(Declaraciones.UserList[QuienLoEchaUI].Name) != vb6.UCase(Nombre)) {
					return retval;
				}
			}
		}

		/* ' Ahora el lider es el unico que no puede salir del clan */
		retval = vb6.UCase(guilds[GuildIndex].GetLeader()) != vb6.UCase(Nombre);

		return retval;
	}

	static boolean PuedeFundarUnClan(int UserIndex, ALINEACION_GUILD Alineacion,
			String /* FIXME BYREF!! */ refError) {
		boolean retval = false;
		/* '*************************************************** */
		/* 'Autor: Unknown */
		/* 'Last Modification: 27/11/2009 */
		/* 'Returns true if can Found a guild */
		/* '27/11/2009: ZaMa - Ahora valida si ya fundo clan o no. */
		/* '*************************************************** */

		if (Declaraciones.UserList[UserIndex].GuildIndex > 0) {
			refError = "Ya perteneces a un clan, no puedes fundar otro";
			return retval;
		}

		if (Declaraciones.UserList[UserIndex].Stats.ELV < 25
				|| Declaraciones.UserList[UserIndex].Stats.UserSkills[eSkill.Liderazgo] < 90) {
			refError = "Para fundar un clan debes ser nivel 25 y tener 90 skills en liderazgo.";
			return retval;
		}

		switch (Alineacion) {
		case ALINEACION_ARMADA:
			if (Declaraciones.UserList[UserIndex].Faccion.ArmadaReal != 1) {
				refError = "Para fundar un clan real debes ser miembro del ejército real.";
				return retval;
			}
			break;

		case ALINEACION_CIUDA:
			if (ES.criminal(UserIndex)) {
				refError = "Para fundar un clan de ciudadanos no debes ser criminal.";
				return retval;
			}
			break;

		case ALINEACION_CRIMINAL:
			if (!ES.criminal(UserIndex)) {
				refError = "Para fundar un clan de criminales no debes ser ciudadano.";
				return retval;
			}
			break;

		case ALINEACION_LEGION:
			if (Declaraciones.UserList[UserIndex].Faccion.FuerzasCaos != 1) {
				refError = "Para fundar un clan del mal debes pertenecer a la legión oscura.";
				return retval;
			}
			break;

		case ALINEACION_MASTER:
			if (Declaraciones.UserList[UserIndex].flags.Privilegios
					&& (PlayerType.User || PlayerType.Consejero || PlayerType.SemiDios)) {
				refError = "Para fundar un clan sin alineación debes ser un dios.";
				return retval;
			}
			break;

		case ALINEACION_NEUTRO:
			if (Declaraciones.UserList[UserIndex].Faccion.ArmadaReal != 0
					|| Declaraciones.UserList[UserIndex].Faccion.FuerzasCaos != 0) {
				refError = "Para fundar un clan neutro no debes pertenecer a ninguna facción.";
				return retval;
			}
			break;
		}

		retval = true;

		return retval;
	}

	static boolean m_EstadoPermiteEntrarChar(String /* FIXME BYREF!! */ Personaje, int GuildIndex) {
		boolean retval = false;
		/* '*************************************************** */
		/* 'Author: Unknown */
		/* 'Last Modification: - */
		/* ' */
		/* '*************************************************** */

		int Promedio = 0;
		int ELV = 0;
		int f = 0;

		retval = false;

		if (vb6.InStrB(Personaje, "\\") != 0) {
			Personaje = vb6.Replace(Personaje, "\\", "");
		}
		if (vb6.InStrB(Personaje, "/") != 0) {
			Personaje = vb6.Replace(Personaje, "/", "");
		}
		if (vb6.InStrB(Personaje, ".") != 0) {
			Personaje = vb6.Replace(Personaje, ".", "");
		}

		if (General.FileExist(Declaraciones.CharPath + Personaje + ".chr")) {
			Promedio = vb6.CLng(ES.GetVar(Declaraciones.CharPath + Personaje + ".chr", "REP", "Promedio"));
			switch (guilds[GuildIndex].Alineacion) {
			case ALINEACION_ARMADA:
				if (Promedio >= 0) {
					ELV = vb6.CInt(ES.GetVar(Declaraciones.CharPath + Personaje + ".chr", "Stats", "ELV"));
					if (ELV >= 25) {
						f = vb6.CByte(
								ES.GetVar(Declaraciones.CharPath + Personaje + ".chr", "Facciones", "EjercitoReal"));
					}
					retval = vb6.IIf(ELV >= 25, f != 0, true);
				}
				break;

			case ALINEACION_CIUDA:
				retval = Promedio >= 0;
				break;

			case ALINEACION_CRIMINAL:
				retval = Promedio < 0;
				break;

			case ALINEACION_NEUTRO:
				retval = vb6.CByte(
						ES.GetVar(Declaraciones.CharPath + Personaje + ".chr", "Facciones", "EjercitoReal")) == 0;
				retval = retval && (vb6.CByte(
						ES.GetVar(Declaraciones.CharPath + Personaje + ".chr", "Facciones", "EjercitoCaos")) == 0);
				break;

			case ALINEACION_LEGION:
				if (Promedio < 0) {
					ELV = vb6.CInt(ES.GetVar(Declaraciones.CharPath + Personaje + ".chr", "Stats", "ELV"));
					if (ELV >= 25) {
						f = vb6.CByte(
								ES.GetVar(Declaraciones.CharPath + Personaje + ".chr", "Facciones", "EjercitoCaos"));
					}
					retval = vb6.IIf(ELV >= 25, f != 0, true);
				}
				break;

			default:
				retval = true;
				break;
			}
		}
		return retval;
	}

	static boolean m_EstadoPermiteEntrar(int UserIndex, int GuildIndex) {
		boolean retval = false;
		/* '*************************************************** */
		/* 'Author: Unknown */
		/* 'Last Modification: - */
		/* ' */
		/* '*************************************************** */

		switch (guilds[GuildIndex].Alineacion) {
		case ALINEACION_ARMADA:
			retval = !ES.criminal(UserIndex) && vb6.IIf(Declaraciones.UserList[UserIndex].Stats.ELV >= 25,
					Declaraciones.UserList[UserIndex].Faccion.ArmadaReal != 0, true);

			break;

		case ALINEACION_LEGION:
			retval = ES.criminal(UserIndex) && vb6.IIf(Declaraciones.UserList[UserIndex].Stats.ELV >= 25,
					Declaraciones.UserList[UserIndex].Faccion.FuerzasCaos != 0, true);

			break;

		case ALINEACION_NEUTRO:
			retval = Declaraciones.UserList[UserIndex].Faccion.ArmadaReal == 0
					&& Declaraciones.UserList[UserIndex].Faccion.FuerzasCaos == 0;

			break;

		case ALINEACION_CIUDA:
			retval = !ES.criminal(UserIndex);

			break;

		case ALINEACION_CRIMINAL:
			retval = ES.criminal(UserIndex);

			/* 'game masters */
			break;

		default:
			retval = true;
			break;
		}
		return retval;
	}

	static ALINEACION_GUILD String2Alineacion(String /* FIXME BYREF!! */ S) {
		ALINEACION_GUILD retval;
		/* '*************************************************** */
		/* 'Author: Unknown */
		/* 'Last Modification: - */
		/* ' */
		/* '*************************************************** */

		switch (S) {
		case "Neutral":
			retval = ALINEACION_NEUTRO;
			break;

		case "Del Mal":
			retval = ALINEACION_LEGION;
			break;

		case "Real":
			retval = ALINEACION_ARMADA;
			break;

		case "Game Masters":
			retval = ALINEACION_MASTER;
			break;

		case "Legal":
			retval = ALINEACION_CIUDA;
			break;

		case "Criminal":
			retval = ALINEACION_CRIMINAL;
			break;
		}
		return retval;
	}

	static String Alineacion2String(ALINEACION_GUILD Alineacion) {
		String retval;
		/* '*************************************************** */
		/* 'Author: Unknown */
		/* 'Last Modification: - */
		/* ' */
		/* '*************************************************** */

		switch (Alineacion) {
		case ALINEACION_NEUTRO:
			retval = "Neutral";
			break;

		case ALINEACION_LEGION:
			retval = "Del Mal";
			break;

		case ALINEACION_ARMADA:
			retval = "Real";
			break;

		case ALINEACION_MASTER:
			retval = "Game Masters";
			break;

		case ALINEACION_CIUDA:
			retval = "Legal";
			break;

		case ALINEACION_CRIMINAL:
			retval = "Criminal";
			break;
		}
		return retval;
	}

	static String Relacion2String(RELACIONES_GUILD Relacion) {
		String retval;
		/* '*************************************************** */
		/* 'Author: Unknown */
		/* 'Last Modification: - */
		/* ' */
		/* '*************************************************** */

		switch (Relacion) {
		case ALIADOS:
			retval = "A";
			break;

		case GUERRA:
			retval = "G";
			break;

		case PAZ:
			retval = "P";
			break;

		case ALIADOS:
			retval = "?";
			break;
		}
		return retval;
	}

	static RELACIONES_GUILD String2Relacion(String S) {
		RELACIONES_GUILD retval;
		/* '*************************************************** */
		/* 'Author: Unknown */
		/* 'Last Modification: - */
		/* ' */
		/* '*************************************************** */

		switch (vb6.UCase(vb6.Trim(S))) {
		case "":
		case "P":
			retval = RELACIONES_GUILD.PAZ;
			break;

		case "G":
			retval = RELACIONES_GUILD.GUERRA;
			break;

		case "A":
			retval = RELACIONES_GUILD.ALIADOS;
			break;

		default:
			retval = RELACIONES_GUILD.PAZ;
			break;
		}
		return retval;
	}

	static boolean GuildNameValido(String cad) {
		boolean retval = false;
		/* '*************************************************** */
		/* 'Author: Unknown */
		/* 'Last Modification: - */
		/* ' */
		/* '*************************************************** */

		int car = 0;
		int i = 0;

		/* 'old function by morgo */

		cad = vb6.LCase(cad);

		for (i = (1); i <= (vb6.Len(cad)); i++) {
			car = vb6.Asc(vb6.mid(cad, i, 1));

			if ((car < 97 || car > 122) && (car != 255) && (car != 32)) {
				retval = false;
				return retval;
			}

		}

		retval = true;

		return retval;
	}

	static boolean YaExiste(String GuildName) {
		boolean retval = false;
		/* '*************************************************** */
		/* 'Author: Unknown */
		/* 'Last Modification: - */
		/* ' */
		/* '*************************************************** */

		int i = 0;

		retval = false;
		GuildName = vb6.UCase(GuildName);

		for (i = (1); i <= (modGuilds.CANTIDADDECLANES); i++) {
			retval = (vb6.UCase(guilds[i].GuildName) == GuildName);
			if (retval) {
				return retval;
			}
		}

		return retval;
	}

	static boolean HasFound(String /* FIXME BYREF!! */ UserName) {
		boolean retval = false;
		/* '*************************************************** */
		/* 'Autor: ZaMa */
		/* 'Last Modification: 27/11/2009 */
		/* 'Returns true if it's already the founder of other guild */
		/* '*************************************************** */
		int i = 0;
		String Name;

		Name = vb6.UCase(UserName);

		for (i = (1); i <= (modGuilds.CANTIDADDECLANES); i++) {
			retval = (vb6.UCase(guilds[i].Fundador) == Name);
			if (retval) {
				return retval;
			}
		}

		return retval;
	}

	static boolean v_AbrirElecciones(int UserIndex, String /* FIXME BYREF!! */ refError) {
		boolean retval = false;
		/* '*************************************************** */
		/* 'Author: Unknown */
		/* 'Last Modification: - */
		/* ' */
		/* '*************************************************** */

		int GuildIndex = 0;

		retval = false;
		GuildIndex = Declaraciones.UserList[UserIndex].GuildIndex;

		if (GuildIndex == 0 || GuildIndex > modGuilds.CANTIDADDECLANES) {
			refError = "Tú no perteneces a ningún clan.";
			return retval;
		}

		if (!m_EsGuildLeader(Declaraciones.UserList[UserIndex].Name, GuildIndex)) {
			refError = "No eres el líder de tu clan";
			return retval;
		}

		if (guilds[GuildIndex].EleccionesAbiertas()) {
			refError = "Las elecciones ya están abiertas.";
			return retval;
		}

		retval = true;
		guilds[GuildIndex].AbrirElecciones();

		return retval;
	}

	static boolean v_UsuarioVota(int UserIndex, String /* FIXME BYREF!! */ Votado, String /* FIXME BYREF!! */ refError) {
 boolean retval = false;
 /* '*************************************************** */
 /* 'Author: Unknown */
 /* 'Last Modification: - */
 /* ' */
 /* '*************************************************** */
 
 int GuildIndex = 0;
 String[] list;
 int i = 0;
 
 retval = false;
 GuildIndex = Declaraciones.UserList[UserIndex].GuildIndex;
 
  if (GuildIndex == 0 || GuildIndex>modGuilds.CANTIDADDECLANES) {
  refError = "Tú no perteneces a ningún clan.";
  return retval;
 }
 
   if (!guilds[GuildIndex].EleccionesAbiertas()) {
   refError = "No hay elecciones abiertas en tu clan.";
   return retval;
  }
  
  list = guilds[GuildIndex].GetMemberList();
   for (i = (0); i <= (vb6.UBound(list[])); i++) {
   if (vb6.UCase(Votado) == list[i]) {
   break; /* FIXME: EXIT FOR */
   }
  }
  
   if (i>vb6.UBound(list[])) {
   refError = Votado + " no pertenece al clan.";
   return retval;
  }
  
   if (guilds[GuildIndex].YaVoto(Declaraciones.UserList[UserIndex].Name)) {
   refError = "Ya has votado, no puedes cambiar tu voto.";
   return retval;
  }
  
  guilds[GuildIndex].ContabilizarVoto(Declaraciones.UserList[UserIndex].Name, Votado);
  retval = true;
 
return retval;
}

	static void v_RutinaElecciones() {
 /* '*************************************************** */
 /* 'Author: Unknown */
 /* 'Last Modification: - */
 /* ' */
 /* '*************************************************** */
 
 int i = 0;
 
 /* FIXME: ON ERROR GOTO errh */
 modSendData.SendData(SendTarget.ToAll, 0, Protocol.PrepareMessageConsoleMsg("Servidor> Revisando elecciones", FontTypeNames.FONTTYPE_SERVER));
  for (i = (1); i <= (modGuilds.CANTIDADDECLANES); i++) {
   if (!guilds[i]Is null ) {
    if (guilds[i].RevisarElecciones()) {
    modSendData.SendData(SendTarget.ToAll, 0, Protocol.PrepareMessageConsoleMsg("Servidor> " + guilds[i].GetLeader + " es el nuevo líder de " + guilds[i].GuildName + ".", FontTypeNames.FONTTYPE_SERVER));
   }
  }
  /* FIXME: proximo : */
 }
 modSendData.SendData(SendTarget.ToAll, 0, Protocol.PrepareMessageConsoleMsg("Servidor> Elecciones revisadas.", FontTypeNames.FONTTYPE_SERVER));
 return;
 /* FIXME: errh : */
 General.LogError("modGuilds.v_RutinaElecciones():" + Err.description);
 /* FIXME: RESUME proximo */
}

	static int GetGuildIndexFromChar(String /* FIXME BYREF!! */ PlayerName) {
		int retval = 0;
		/* '*************************************************** */
		/* 'Author: Unknown */
		/* 'Last Modification: - */
		/* ' */
		/* '*************************************************** */

		/* 'aca si que vamos a violar las capas deliveradamente ya que */
		/* 'visual basic no permite declarar metodos de clase */
		String Temps;
		if (vb6.InStrB(PlayerName, "\\") != 0) {
			PlayerName = vb6.Replace(PlayerName, "\\", "");
		}
		if (vb6.InStrB(PlayerName, "/") != 0) {
			PlayerName = vb6.Replace(PlayerName, "/", "");
		}
		if (vb6.InStrB(PlayerName, ".") != 0) {
			PlayerName = vb6.Replace(PlayerName, ".", "");
		}
		Temps = ES.GetVar(Declaraciones.CharPath + PlayerName + ".chr", "GUILD", "GUILDINDEX");
		if (vb6.IsNumeric(Temps)) {
			retval = vb6.CInt(Temps);
		} else {
			retval = 0;
		}
		return retval;
	}

	static int GetGuildIndex(String GuildName) {
		int retval = 0;
		/* '*************************************************** */
		/* 'Author: Unknown */
		/* 'Last Modification: - */
		/* ' */
		/* '*************************************************** */

		/* 'me da el indice del guildname */
		int i = 0;

		retval = 0;
		GuildName = vb6.UCase(GuildName);
		for (i = (1); i <= (modGuilds.CANTIDADDECLANES); i++) {
			if (vb6.UCase(guilds[i].GuildName) == GuildName) {
				retval = i;
				return retval;
			}
		}
		return retval;
	}

	static String m_ListaDeMiembrosOnline(int UserIndex, int GuildIndex) {
		String retval;
		/* '*************************************************** */
		/* 'Author: Unknown */
		/* 'Last Modification: 28/05/2010 */
		/* '28/05/2010: ZaMa - Solo dioses pueden ver otros dioses online. */
		/* '*************************************************** */

		int i = 0;
		PlayerType priv;

		priv = PlayerType.User || PlayerType.Consejero || PlayerType.SemiDios;

		/* ' Solo dioses pueden ver otros dioses online */
		if (Declaraciones.UserList[UserIndex].flags.Privilegios && (PlayerType.Dios || PlayerType.Admin)) {
			priv = priv || PlayerType.Dios || PlayerType.Admin;
		}

		if (GuildIndex > 0 && GuildIndex <= modGuilds.CANTIDADDECLANES) {

			/* ' Horrible, tengo que decirlo.. */
			i = guilds[GuildIndex].m_Iterador_ProximoUserIndex;

			while (i > 0) {

				/* 'No mostramos dioses y admins */
				if (i != UserIndex && (Declaraciones.UserList[i].flags.Privilegios && priv)) {
					retval = retval + Declaraciones.UserList[i].Name + ",";
				}

				i = guilds[GuildIndex].m_Iterador_ProximoUserIndex;
			}
		}

		if (vb6.Len(retval) > 0) {
			retval = vb6.Left(retval, vb6.Len(retval) - 1);
		}
		return retval;
	}

	static String[] PrepareGuildsList() {
		String[] retval;
		/* '*************************************************** */
		/* 'Author: Unknown */
		/* 'Last Modification: - */
		/* ' */
		/* '*************************************************** */

		String[] tStr;
		int i = 0;

		if (modGuilds.CANTIDADDECLANES == 0) {
			tStr = new String[0];
			tStr = (tStr == null) ? new String[0] : java.util.Arrays.copyOf(tStr, 0);
		} else {
			tStr = new String[0];
			tStr = (tStr == null) ? new String[modGuilds.CANTIDADDECLANES - 1]
					: java.util.Arrays.copyOf(tStr, modGuilds.CANTIDADDECLANES - 1);

			for (i = (1); i <= (modGuilds.CANTIDADDECLANES); i++) {
				tStr[i - 1] = guilds[i].GuildName;
			}
		}

		retval = tStr;
		return retval;
	}

	static void SendGuildDetails(int UserIndex, String /* FIXME BYREF!! */ GuildName) {
		/* '*************************************************** */
		/* 'Author: Unknown */
		/* 'Last Modification: - */
		/* ' */
		/* '*************************************************** */

		String[] codex;
		int GI = 0;
		int i = 0;

		GI = GetGuildIndex(GuildName);
		if (GI == 0) {
			return;
		}

		for (i = (1); i <= (modGuilds.CANTIDADMAXIMACODEX); i++) {
			codex[i - 1] = guilds[GI].GetCodex(i);
		}

		Protocol.WriteGuildDetails(UserIndex, GuildName, guilds[GI].Fundador, guilds[GI].GetFechaFundacion(),
				guilds[GI].GetLeader(), guilds[GI].GetURL(), guilds[GI].CantidadDeMiembros,
				guilds[GI].EleccionesAbiertas(), Alineacion2String(guilds[GI].Alineacion), guilds[GI].CantidadEnemys,
				guilds[GI].CantidadAllies, guilds[GI].PuntosAntifaccion + "/" + vb6.CStr(modGuilds.MAXANTIFACCION),
				codex, guilds[GI].GetDesc());
	}

	static void SendGuildLeaderInfo(int UserIndex) {
		/* '*************************************************** */
		/* 'Autor: Mariano Barrou (El Oso) */
		/* 'Last Modification: 12/10/06 */
		/* 'Las Modified By: Juan Martín Sotuyo Dodero (Maraxus) */
		/* '*************************************************** */
		int GI = 0;
		String[] guildList;
		String[] MemberList;
		String[] aspirantsList;

		GI = Declaraciones.UserList[UserIndex].GuildIndex;

		guildList = PrepareGuildsList();

		if (GI <= 0 || GI > modGuilds.CANTIDADDECLANES) {
			/* 'Send the guild list instead */
			Protocol.WriteGuildList(UserIndex, guildList);
			return;
		}

		MemberList = guilds[GI].GetMemberList();

		if (!m_EsGuildLeader(Declaraciones.UserList[UserIndex].Name, GI)) {
			/* 'Send the guild list instead */
			Protocol.WriteGuildMemberInfo(UserIndex, guildList, MemberList);
			return;
		}

		aspirantsList = guilds[GI].GetAspirantes();

		Protocol.WriteGuildLeaderInfo(UserIndex, guildList, MemberList, guilds[GI].GetGuildNews(), aspirantsList);
	}

	static int m_Iterador_ProximoUserIndex(int GuildIndex) {
		int retval = 0;
		/* '*************************************************** */
		/* 'Author: Unknown */
		/* 'Last Modification: - */
		/* ' */
		/* '*************************************************** */

		/* 'itera sobre los onlinemembers */
		retval = 0;
		if (GuildIndex > 0 && GuildIndex <= modGuilds.CANTIDADDECLANES) {
			retval = guilds[GuildIndex].m_Iterador_ProximoUserIndex();
		}
		return retval;
	}

	static int Iterador_ProximoGM(int GuildIndex) {
		int retval = 0;
		/* '*************************************************** */
		/* 'Author: Unknown */
		/* 'Last Modification: - */
		/* ' */
		/* '*************************************************** */

		/* 'itera sobre los gms escuchando este clan */
		retval = 0;
		if (GuildIndex > 0 && GuildIndex <= modGuilds.CANTIDADDECLANES) {
			retval = guilds[GuildIndex].Iterador_ProximoGM();
		}
		return retval;
	}

	static int r_Iterador_ProximaPropuesta(int GuildIndex, RELACIONES_GUILD Tipo) {
		int retval = 0;
		/* '*************************************************** */
		/* 'Author: Unknown */
		/* 'Last Modification: - */
		/* ' */
		/* '*************************************************** */

		/* 'itera sobre las propuestas */
		retval = 0;
		if (GuildIndex > 0 && GuildIndex <= modGuilds.CANTIDADDECLANES) {
			retval = guilds[GuildIndex].Iterador_ProximaPropuesta(Tipo);
		}
		return retval;
	}

	static int GMEscuchaClan(int UserIndex, String GuildName) {
		int retval = 0;
		/* '*************************************************** */
		/* 'Author: Unknown */
		/* 'Last Modification: - */
		/* ' */
		/* '*************************************************** */

		int GI = 0;

		/* 'listen to no guild at all */
		if (vb6.LenB(GuildName) == 0 && Declaraciones.UserList[UserIndex].EscucheClan != 0) {
			/* 'Quit listening to previous guild!! */
			Protocol.WriteConsoleMsg(UserIndex,
					"Dejas de escuchar a : " + guilds[Declaraciones.UserList[UserIndex].EscucheClan].GuildName,
					FontTypeNames.FONTTYPE_GUILD);
			guilds[Declaraciones.UserList[UserIndex].EscucheClan].DesconectarGM(UserIndex);
			return retval;
		}

		/* 'devuelve el guildindex */
		GI = GetGuildIndex(GuildName);
		if (GI > 0) {
			if (Declaraciones.UserList[UserIndex].EscucheClan != 0) {
				if (Declaraciones.UserList[UserIndex].EscucheClan == GI) {
					/* 'Already listening to them... */
					Protocol.WriteConsoleMsg(UserIndex, "Conectado a : " + GuildName, FontTypeNames.FONTTYPE_GUILD);
					retval = GI;
					return retval;
				} else {
					/* 'Quit listening to previous guild!! */
					Protocol.WriteConsoleMsg(UserIndex,
							"Dejas de escuchar a : " + guilds[Declaraciones.UserList[UserIndex].EscucheClan].GuildName,
							FontTypeNames.FONTTYPE_GUILD);
					guilds[Declaraciones.UserList[UserIndex].EscucheClan].DesconectarGM(UserIndex);
				}
			}

			guilds[GI].ConectarGM(UserIndex);
			Protocol.WriteConsoleMsg(UserIndex, "Conectado a : " + GuildName, FontTypeNames.FONTTYPE_GUILD);
			retval = GI;
			Declaraciones.UserList[UserIndex].EscucheClan = GI;
		} else {
			Protocol.WriteConsoleMsg(UserIndex, "Error, el clan no existe.", FontTypeNames.FONTTYPE_GUILD);
			retval = 0;
		}

		return retval;
	}

	static void GMDejaDeEscucharClan(int UserIndex, int GuildIndex) {
		/* '*************************************************** */
		/* 'Author: Unknown */
		/* 'Last Modification: - */
		/* ' */
		/* '*************************************************** */

		/* 'el index lo tengo que tener de cuando me puse a escuchar */
		Declaraciones.UserList[UserIndex].EscucheClan = 0;
		guilds[GuildIndex].DesconectarGM(UserIndex);
	}

	static int r_DeclararGuerra(int UserIndex, String /* FIXME BYREF!! */ GuildGuerra,
			String /* FIXME BYREF!! */ refError) {
		int retval = 0;
		/* '*************************************************** */
		/* 'Author: Unknown */
		/* 'Last Modification: - */
		/* ' */
		/* '*************************************************** */

		int GI = 0;
		int GIG = 0;

		retval = 0;
		GI = Declaraciones.UserList[UserIndex].GuildIndex;
		if (GI <= 0 || GI > modGuilds.CANTIDADDECLANES) {
			refError = "No eres miembro de ningún clan.";
			return retval;
		}

		if (!m_EsGuildLeader(Declaraciones.UserList[UserIndex].Name, GI)) {
			refError = "No eres el líder de tu clan.";
			return retval;
		}

		if (vb6.Trim(GuildGuerra) == "") {
			refError = "No has seleccionado ningún clan.";
			return retval;
		}

		GIG = GetGuildIndex(GuildGuerra);
		if (guilds[GI].GetRelacion(GIG) == GUERRA) {
			refError = "Tu clan ya está en guerra con " + GuildGuerra + ".";
			return retval;
		}

		if (GI == GIG) {
			refError = "No puedes declarar la guerra a tu mismo clan.";
			return retval;
		}

		if (GIG < 1 || GIG > modGuilds.CANTIDADDECLANES) {
			General.LogError("ModGuilds.r_DeclararGuerra: " + GI + " declara a " + GuildGuerra);
			refError = "Inconsistencia en el sistema de clanes. Avise a un administrador (GIG fuera de rango)";
			return retval;
		}

		guilds[GI].AnularPropuestas(GIG);
		guilds[GIG].AnularPropuestas(GI);
		guilds[GI].SetRelacion(GIG, RELACIONES_GUILD.GUERRA);
		guilds[GIG].SetRelacion(GI, RELACIONES_GUILD.GUERRA);

		retval = GIG;

		return retval;
	}

	static int r_AceptarPropuestaDePaz(int UserIndex, String /* FIXME BYREF!! */ GuildPaz,
			String /* FIXME BYREF!! */ refError) {
		int retval = 0;
		/* '*************************************************** */
		/* 'Author: Unknown */
		/* 'Last Modification: - */
		/* ' */
		/* '*************************************************** */

		/*
		 * 'el clan de userindex acepta la propuesta de paz de guildpaz, con
		 * quien esta en guerra
		 */
		int GI = 0;
		int GIG = 0;

		GI = Declaraciones.UserList[UserIndex].GuildIndex;
		if (GI <= 0 || GI > modGuilds.CANTIDADDECLANES) {
			refError = "No eres miembro de ningún clan.";
			return retval;
		}

		if (!m_EsGuildLeader(Declaraciones.UserList[UserIndex].Name, GI)) {
			refError = "No eres el líder de tu clan.";
			return retval;
		}

		if (vb6.Trim(GuildPaz) == "") {
			refError = "No has seleccionado ningún clan.";
			return retval;
		}

		GIG = GetGuildIndex(GuildPaz);

		if (GIG < 1 || GIG > modGuilds.CANTIDADDECLANES) {
			General.LogError("ModGuilds.r_AceptarPropuestaDePaz: " + GI + " acepta de " + GuildPaz);
			refError = "Inconsistencia en el sistema de clanes. Avise a un administrador (GIG fuera de rango).";
			return retval;
		}

		if (guilds[GI].GetRelacion(GIG) != RELACIONES_GUILD.GUERRA) {
			refError = "No estás en guerra con ese clan.";
			return retval;
		}

		if (!guilds[GI].HayPropuesta(GIG, RELACIONES_GUILD.PAZ)) {
			refError = "No hay ninguna propuesta de paz para aceptar.";
			return retval;
		}

		guilds[GI].AnularPropuestas(GIG);
		guilds[GIG].AnularPropuestas(GI);
		guilds[GI].SetRelacion(GIG, RELACIONES_GUILD.PAZ);
		guilds[GIG].SetRelacion(GI, RELACIONES_GUILD.PAZ);

		retval = GIG;
		return retval;
	}

	static int r_RechazarPropuestaDeAlianza(int UserIndex,
			String /* FIXME BYREF!! */ GuildPro, String /* FIXME BYREF!! */ refError) {
		int retval = 0;
		/* '*************************************************** */
		/* 'Author: Unknown */
		/* 'Last Modification: - */
		/* ' */
		/* '*************************************************** */

		/* 'devuelve el index al clan guildPro */
		int GI = 0;
		int GIG = 0;

		retval = 0;
		GI = Declaraciones.UserList[UserIndex].GuildIndex;

		if (GI <= 0 || GI > modGuilds.CANTIDADDECLANES) {
			refError = "No eres miembro de ningún clan.";
			return retval;
		}

		if (!m_EsGuildLeader(Declaraciones.UserList[UserIndex].Name, GI)) {
			refError = "No eres el líder de tu clan.";
			return retval;
		}

		if (vb6.Trim(GuildPro) == "") {
			refError = "No has seleccionado ningún clan.";
			return retval;
		}

		GIG = GetGuildIndex(GuildPro);

		if (GIG < 1 || GIG > modGuilds.CANTIDADDECLANES) {
			General.LogError("ModGuilds.r_RechazarPropuestaDeAlianza: " + GI + " acepta de " + GuildPro);
			refError = "Inconsistencia en el sistema de clanes. Avise a un administrador (GIG fuera de rango).";
			return retval;
		}

		if (!guilds[GI].HayPropuesta(GIG, ALIADOS)) {
			refError = "No hay propuesta de alianza del clan " + GuildPro;
			return retval;
		}

		guilds[GI].AnularPropuestas(GIG);
		/* 'avisamos al otro clan */
		guilds[GIG].SetGuildNews(
				guilds[GI].GuildName + " ha rechazado nuestra propuesta de alianza. " + guilds[GIG].GetGuildNews());
		retval = GIG;

		return retval;
	}

	static int r_RechazarPropuestaDePaz(int UserIndex,
			String /* FIXME BYREF!! */ GuildPro, String /* FIXME BYREF!! */ refError) {
		int retval = 0;
		/* '*************************************************** */
		/* 'Author: Unknown */
		/* 'Last Modification: - */
		/* ' */
		/* '*************************************************** */

		/* 'devuelve el index al clan guildPro */
		int GI = 0;
		int GIG = 0;

		retval = 0;
		GI = Declaraciones.UserList[UserIndex].GuildIndex;

		if (GI <= 0 || GI > modGuilds.CANTIDADDECLANES) {
			refError = "No eres miembro de ningún clan.";
			return retval;
		}

		if (!m_EsGuildLeader(Declaraciones.UserList[UserIndex].Name, GI)) {
			refError = "No eres el líder de tu clan.";
			return retval;
		}

		if (vb6.Trim(GuildPro) == "") {
			refError = "No has seleccionado ningún clan.";
			return retval;
		}

		GIG = GetGuildIndex(GuildPro);

		if (GIG < 1 || GIG > modGuilds.CANTIDADDECLANES) {
			General.LogError("ModGuilds.r_RechazarPropuestaDePaz: " + GI + " acepta de " + GuildPro);
			refError = "Inconsistencia en el sistema de clanes. Avise a un administrador (GIG fuera de rango).";
			return retval;
		}

		if (!guilds[GI].HayPropuesta(GIG, RELACIONES_GUILD.PAZ)) {
			refError = "No hay propuesta de paz del clan " + GuildPro;
			return retval;
		}

		guilds[GI].AnularPropuestas(GIG);
		/* 'avisamos al otro clan */
		guilds[GIG].SetGuildNews(
				guilds[GI].GuildName + " ha rechazado nuestra propuesta de paz. " + guilds[GIG].GetGuildNews());
		retval = GIG;

		return retval;
	}

	static int r_AceptarPropuestaDeAlianza(int UserIndex,
			String /* FIXME BYREF!! */ GuildAllie, String /* FIXME BYREF!! */ refError) {
		int retval = 0;
		/* '*************************************************** */
		/* 'Author: Unknown */
		/* 'Last Modification: - */
		/* ' */
		/* '*************************************************** */

		/*
		 * 'el clan de userindex acepta la propuesta de paz de guildpaz, con
		 * quien esta en guerra
		 */
		int GI = 0;
		int GIG = 0;

		retval = 0;
		GI = Declaraciones.UserList[UserIndex].GuildIndex;
		if (GI <= 0 || GI > modGuilds.CANTIDADDECLANES) {
			refError = "No eres miembro de ningún clan.";
			return retval;
		}

		if (!m_EsGuildLeader(Declaraciones.UserList[UserIndex].Name, GI)) {
			refError = "No eres el líder de tu clan.";
			return retval;
		}

		if (vb6.Trim(GuildAllie) == "") {
			refError = "No has seleccionado ningún clan.";
			return retval;
		}

		GIG = GetGuildIndex(GuildAllie);

		if (GIG < 1 || GIG > modGuilds.CANTIDADDECLANES) {
			General.LogError("ModGuilds.r_AceptarPropuestaDeAlianza: " + GI + " acepta de " + GuildAllie);
			refError = "Inconsistencia en el sistema de clanes. Avise a un administrador (GIG fuera de rango).";
			return retval;
		}

		if (guilds[GI].GetRelacion(GIG) != RELACIONES_GUILD.PAZ) {
			refError = "No estás en paz con el clan, solo puedes aceptar propuesas de alianzas con alguien que estes en paz.";
			return retval;
		}

		if (!guilds[GI].HayPropuesta(GIG, RELACIONES_GUILD.ALIADOS)) {
			refError = "No hay ninguna propuesta de alianza para aceptar.";
			return retval;
		}

		guilds[GI].AnularPropuestas(GIG);
		guilds[GIG].AnularPropuestas(GI);
		guilds[GI].SetRelacion(GIG, RELACIONES_GUILD.ALIADOS);
		guilds[GIG].SetRelacion(GI, RELACIONES_GUILD.ALIADOS);

		retval = GIG;

		return retval;
	}

	static boolean r_ClanGeneraPropuesta(int UserIndex,
			String /* FIXME BYREF!! */ OtroClan, RELACIONES_GUILD Tipo,
			String /* FIXME BYREF!! */ Detalle, String /* FIXME BYREF!! */ refError) {
		boolean retval = false;
		/* '*************************************************** */
		/* 'Author: Unknown */
		/* 'Last Modification: - */
		/* ' */
		/* '*************************************************** */

		int OtroClanGI = 0;
		int GI = 0;

		retval = false;

		GI = Declaraciones.UserList[UserIndex].GuildIndex;
		if (GI <= 0 || GI > modGuilds.CANTIDADDECLANES) {
			refError = "No eres miembro de ningún clan.";
			return retval;
		}

		OtroClanGI = GetGuildIndex(OtroClan);

		if (OtroClanGI == GI) {
			refError = "No puedes declarar relaciones con tu propio clan.";
			return retval;
		}

		if (OtroClanGI <= 0 || OtroClanGI > modGuilds.CANTIDADDECLANES) {
			refError = "El sistema de clanes esta inconsistente, el otro clan no existe.";
			return retval;
		}

		if (guilds[OtroClanGI].HayPropuesta(GI, Tipo)) {
			refError = "Ya hay propuesta de " + Relacion2String(Tipo) + " con " + OtroClan;
			return retval;
		}

		if (!m_EsGuildLeader(Declaraciones.UserList[UserIndex].Name, GI)) {
			refError = "No eres el líder de tu clan.";
			return retval;
		}

		/* 'de acuerdo al tipo procedemos validando las transiciones */
		if (Tipo == RELACIONES_GUILD.PAZ) {
			if (guilds[GI].GetRelacion(OtroClanGI) != RELACIONES_GUILD.GUERRA) {
				refError = "No estás en guerra con " + OtroClan;
				return retval;
			}
		} else if (Tipo == RELACIONES_GUILD.GUERRA) {
			/* 'por ahora no hay propuestas de guerra */
		} else if (Tipo == RELACIONES_GUILD.ALIADOS) {
			if (guilds[GI].GetRelacion(OtroClanGI) != RELACIONES_GUILD.PAZ) {
				refError = "Para solicitar alianza no debes estar ni aliado ni en guerra con " + OtroClan;
				return retval;
			}
		}

		guilds[OtroClanGI].SetPropuesta(Tipo, GI, Detalle);
		retval = true;

		return retval;
	}

	static String r_VerPropuesta(int UserIndex, String /* FIXME BYREF!! */ OtroGuild, RELACIONES_GUILD Tipo,
			String /* FIXME BYREF!! */ refError) {
		String retval;
		/* '*************************************************** */
		/* 'Author: Unknown */
		/* 'Last Modification: - */
		/* ' */
		/* '*************************************************** */

		int OtroClanGI = 0;
		int GI = 0;

		retval = "";
		refError = "";

		GI = Declaraciones.UserList[UserIndex].GuildIndex;
		if (GI <= 0 || GI > modGuilds.CANTIDADDECLANES) {
			refError = "No eres miembro de ningún clan.";
			return retval;
		}

		if (!m_EsGuildLeader(Declaraciones.UserList[UserIndex].Name, GI)) {
			refError = "No eres el líder de tu clan.";
			return retval;
		}

		OtroClanGI = GetGuildIndex(OtroGuild);

		if (!guilds[GI].HayPropuesta(OtroClanGI, Tipo)) {
			refError = "No existe la propuesta solicitada.";
			return retval;
		}

		retval = guilds[GI].GetPropuesta(OtroClanGI, Tipo);

		return retval;
	}

	static String[] r_ListaDePropuestas(int UserIndex, RELACIONES_GUILD Tipo) {
		String[] retval;
		/* '*************************************************** */
		/* 'Author: Unknown */
		/* 'Last Modification: - */
		/* ' */
		/* '*************************************************** */

		int GI = 0;
		int i = 0;
		int proposalCount = 0;
		String[] proposals;

		GI = Declaraciones.UserList[UserIndex].GuildIndex;

		if (GI > 0 && GI <= modGuilds.CANTIDADDECLANES) {
			proposalCount = guilds[GI].CantidadPropuestas[Tipo];

			/* 'Resize array to contain all proposals */
			if (proposalCount > 0) {
				proposals = new String[0];
				proposals = (proposals == null) ? new String[proposalCount - 1]
						: java.util.Arrays.copyOf(proposals, proposalCount - 1);
			} else {
				proposals = new String[0];
				proposals = (proposals == null) ? new String[0] : java.util.Arrays.copyOf(proposals, 0);
			}

			/* 'Store each guild name */
			for (i = (0); i <= (proposalCount - 1); i++) {
				proposals[i] = guilds[guilds[GI].Iterador_ProximaPropuesta(Tipo)].GuildName;
			}
		}

		retval = proposals;
		return retval;
	}

	static void a_RechazarAspiranteChar(String /* FIXME BYREF!! */ Aspirante, int guild,
			String /* FIXME BYREF!! */ Detalles) {
		/* '*************************************************** */
		/* 'Author: Unknown */
		/* 'Last Modification: - */
		/* ' */
		/* '*************************************************** */

		if (vb6.InStrB(Aspirante, "\\") != 0) {
			Aspirante = vb6.Replace(Aspirante, "\\", "");
		}
		if (vb6.InStrB(Aspirante, "/") != 0) {
			Aspirante = vb6.Replace(Aspirante, "/", "");
		}
		if (vb6.InStrB(Aspirante, ".") != 0) {
			Aspirante = vb6.Replace(Aspirante, ".", "");
		}
		guilds[guild].InformarRechazoEnChar(Aspirante, Detalles);
	}

	static String a_ObtenerRechazoDeChar(String /* FIXME BYREF!! */ Aspirante) {
		String retval;
		/* '*************************************************** */
		/* 'Author: Unknown */
		/* 'Last Modification: - */
		/* ' */
		/* '*************************************************** */

		if (vb6.InStrB(Aspirante, "\\") != 0) {
			Aspirante = vb6.Replace(Aspirante, "\\", "");
		}
		if (vb6.InStrB(Aspirante, "/") != 0) {
			Aspirante = vb6.Replace(Aspirante, "/", "");
		}
		if (vb6.InStrB(Aspirante, ".") != 0) {
			Aspirante = vb6.Replace(Aspirante, ".", "");
		}
		retval = ES.GetVar(Declaraciones.CharPath + Aspirante + ".chr", "GUILD", "MotivoRechazo");
		ES.WriteVar(Declaraciones.CharPath + Aspirante + ".chr", "GUILD", "MotivoRechazo", "");
		return retval;
	}

	static boolean a_RechazarAspirante(int UserIndex, String /* FIXME BYREF!! */ Nombre,
			String /* FIXME BYREF!! */ refError) {
		boolean retval = false;
		/* '*************************************************** */
		/* 'Author: Unknown */
		/* 'Last Modification: - */
		/* ' */
		/* '*************************************************** */

		int GI = 0;
		int NroAspirante = 0;

		retval = false;
		GI = Declaraciones.UserList[UserIndex].GuildIndex;
		if (GI <= 0 || GI > modGuilds.CANTIDADDECLANES) {
			refError = "No perteneces a ningún clan";
			return retval;
		}

		NroAspirante = guilds[GI].NumeroDeAspirante(Nombre);

		if (NroAspirante == 0) {
			refError = Nombre + " no es aspirante a tu clan.";
			return retval;
		}

		guilds[GI].RetirarAspirante(Nombre, NroAspirante);
		refError = "Fue rechazada tu solicitud de ingreso a " + guilds[GI].GuildName;
		retval = true;

		return retval;
	}

	static String a_DetallesAspirante(int UserIndex, String /* FIXME BYREF!! */ Nombre) {
		String retval;
		/* '*************************************************** */
		/* 'Author: Unknown */
		/* 'Last Modification: - */
		/* ' */
		/* '*************************************************** */

		int GI = 0;
		int NroAspirante = 0;

		GI = Declaraciones.UserList[UserIndex].GuildIndex;
		if (GI <= 0 || GI > modGuilds.CANTIDADDECLANES) {
			return retval;
		}

		if (!m_EsGuildLeader(Declaraciones.UserList[UserIndex].Name, GI)) {
			return retval;
		}

		NroAspirante = guilds[GI].NumeroDeAspirante(Nombre);
		if (NroAspirante > 0) {
			retval = guilds[GI].DetallesSolicitudAspirante(NroAspirante);
		}

		return retval;
	}

	static void SendDetallesPersonaje(int UserIndex, String Personaje) {
 /* '*************************************************** */
 /* 'Author: Unknown */
 /* 'Last Modification: - */
 /* ' */
 /* '*************************************************** */
 
 int GI = 0;
 int NroAsp = 0;
 String GuildName;
 clsIniManager UserFile;
 String Miembro;
 int GuildActual = 0;
 String[] list;
 int i = 0;
 
 /* FIXME: ON ERROR GOTO ERROR */
 GI = Declaraciones.UserList[UserIndex].GuildIndex;
 
 Personaje = vb6.UCase(Personaje);
 
  if (GI<=0 || GI>modGuilds.CANTIDADDECLANES) {
  Protocol.WriteConsoleMsg(UserIndex, "No perteneces a ningún clan.", FontTypeNames.FONTTYPE_INFO);
  return;
 }
 
  if (!m_EsGuildLeader(Declaraciones.UserList[UserIndex].Name, GI)) {
  Protocol.WriteConsoleMsg(UserIndex, "No eres el líder de tu clan.", FontTypeNames.FONTTYPE_INFO);
  return;
 }
 
  if (vb6.InStrB(Personaje, "\\") != 0) {
  Personaje = vb6.Replace(Personaje, "\\", "");
 }
  if (vb6.InStrB(Personaje, "/") != 0) {
  Personaje = vb6.Replace(Personaje, "/", "");
 }
  if (vb6.InStrB(Personaje, ".") != 0) {
  Personaje = vb6.Replace(Personaje, ".", "");
 }
 
 NroAsp = guilds[GI].NumeroDeAspirante(Personaje);
 
  if (NroAsp == 0) {
  list = guilds[GI].GetMemberList();
  
   for (i = (0); i <= (vb6.UBound(list[])); i++) {
   if (Personaje == list[i]) {
   break; /* FIXME: EXIT FOR */
   }
  }
  
   if (i>vb6.UBound(list[])) {
   Protocol.WriteConsoleMsg(UserIndex, "El personaje no es ni aspirante ni miembro del clan.", FontTypeNames.FONTTYPE_INFO);
   return;
  }
 }
 
 /* 'ahora traemos la info */
 
 UserFile = new clsIniManager();
 
  UserFile.Initialize(Declaraciones.CharPath + Personaje + ".chr");
  
  /* ' Get the character's current guild */
  GuildActual = vb6.val(UserFile.GetValue("GUILD", "GuildIndex"));
   if (GuildActual>0 && GuildActual<=modGuilds.CANTIDADDECLANES) {
   GuildName = "<" + guilds[GuildActual].GuildName + ">";
   } else {
   GuildName = "Ninguno";
  }
  
  /* 'Get previous guilds */
  Miembro = UserFile.GetValue("GUILD", "Miembro");
   if (vb6.Len(Miembro)>400) {
   Miembro = ".." + vb6.Right(Miembro, 400);
  }
  
  Protocol.WriteCharacterInfo(UserIndex, Personaje, UserFile.GetValue("INIT", "Raza"), UserFile.GetValue("INIT", "Clase"), UserFile.GetValue("INIT", "Genero"), UserFile.GetValue("STATS", "ELV"), UserFile.GetValue("STATS", "GLD"), UserFile.GetValue("STATS", "Banco"), UserFile.GetValue("REP", "Promedio"), UserFile.GetValue("GUILD", "Pedidos"), GuildName, Miembro, UserFile.GetValue("FACCIONES", "EjercitoReal"), UserFile.GetValue("FACCIONES", "EjercitoCaos"), UserFile.GetValue("FACCIONES", "CiudMatados"), UserFile.GetValue("FACCIONES", "CrimMatados"));
 
 UserFile = null;
 
 return;
 /* ERROR : */
 UserFile = null;
  if (!(General.FileExist(Declaraciones.CharPath + Personaje + ".chr", vbArchive))) {
  General.LogError("El usuario " + Declaraciones.UserList[UserIndex].Name + " (" + UserIndex + " ) ha pedido los detalles del personaje " + Personaje + " que no se encuentra.");
  
  /* FIXME: ON ERROR RESUME NEXT */
  /* ' Fuerzo el borrado de la lista, lo deberia hacer el programa que borra pjs.. */
  guilds[GI].RemoveMemberName(Personaje);
  } else {
  General.LogError("[" + Err.Number + "] " + Err.description + " En la rutina SendDetallesPersonaje, por el usuario " + Declaraciones.UserList[UserIndex].Name + " (" + UserIndex + " ), pidiendo información sobre el personaje " + Personaje);
 }
}

	static boolean a_NuevoAspirante(int UserIndex, String /* FIXME BYREF!! */ clan,
			String /* FIXME BYREF!! */ Solicitud, String /* FIXME BYREF!! */ refError) {
		boolean retval = false;
		/* '*************************************************** */
		/* 'Author: Unknown */
		/* 'Last Modification: - */
		/* ' */
		/* '*************************************************** */

		String ViejoSolicitado;
		int ViejoGuildINdex = 0;
		int ViejoNroAspirante = 0;
		int NuevoGuildIndex = 0;

		retval = false;

		if (Declaraciones.UserList[UserIndex].GuildIndex > 0) {
			refError = "Ya perteneces a un clan, debes salir del mismo antes de solicitar ingresar a otro.";
			return retval;
		}

		if (Extra.EsNewbie(UserIndex)) {
			refError = "Los newbies no tienen derecho a entrar a un clan.";
			return retval;
		}

		NuevoGuildIndex = GetGuildIndex(clan);
		if (NuevoGuildIndex == 0) {
			refError = "Ese clan no existe, avise a un administrador.";
			return retval;
		}

		if (!m_EstadoPermiteEntrar(UserIndex, NuevoGuildIndex)) {
			refError = "Tú no puedes entrar a un clan de alineación "
					+ Alineacion2String(guilds[NuevoGuildIndex].Alineacion);
			return retval;
		}

		if (guilds[NuevoGuildIndex].CantidadAspirantes() >= modGuilds.MAXASPIRANTES) {
			refError = "El clan tiene demasiados aspirantes. Contáctate con un miembro para que procese las solicitudes.";
			return retval;
		}

		ViejoSolicitado = ES.GetVar(Declaraciones.CharPath + Declaraciones.UserList[UserIndex].Name + ".chr", "GUILD",
				"ASPIRANTEA");

		if (vb6.LenB(ViejoSolicitado) != 0) {
			/* 'borramos la vieja solicitud */
			ViejoGuildINdex = vb6.CInt(ViejoSolicitado);
			if (ViejoGuildINdex != 0) {
				ViejoNroAspirante = guilds[ViejoGuildINdex].NumeroDeAspirante(Declaraciones.UserList[UserIndex].Name);
				if (ViejoNroAspirante > 0) {
					guilds[ViejoGuildINdex].RetirarAspirante(Declaraciones.UserList[UserIndex].Name, ViejoNroAspirante);
				}
			} else {
				/*
				 * 'RefError =
				 * "Inconsistencia en los clanes, avise a un administrador"
				 */
				/* 'Exit Function */
			}
		}

		guilds[NuevoGuildIndex].NuevoAspirante(Declaraciones.UserList[UserIndex].Name, Solicitud);
		retval = true;
		return retval;
	}

	static boolean a_AceptarAspirante(int UserIndex, String /* FIXME BYREF!! */ Aspirante,
			String /* FIXME BYREF!! */ refError) {
		boolean retval = false;
		/* '*************************************************** */
		/* 'Author: Unknown */
		/* 'Last Modification: - */
		/* ' */
		/* '*************************************************** */

		int GI = 0;
		int NroAspirante = 0;
		int AspiranteUI = 0;

		/* 'un pj ingresa al clan :D */

		retval = false;

		GI = Declaraciones.UserList[UserIndex].GuildIndex;
		if (GI <= 0 || GI > modGuilds.CANTIDADDECLANES) {
			refError = "No perteneces a ningún clan";
			return retval;
		}

		if (!m_EsGuildLeader(Declaraciones.UserList[UserIndex].Name, GI)) {
			refError = "No eres el líder de tu clan";
			return retval;
		}

		NroAspirante = guilds[GI].NumeroDeAspirante(Aspirante);

		if (NroAspirante == 0) {
			refError = "El Pj no es aspirante al clan.";
			return retval;
		}

		AspiranteUI = Extra.NameIndex(Aspirante);
		if (AspiranteUI > 0) {
			/* 'pj Online */
			if (!m_EstadoPermiteEntrar(AspiranteUI, GI)) {
				refError = Aspirante + " no puede entrar a un clan de alineación "
						+ Alineacion2String(guilds[GI].Alineacion);
				guilds[GI].RetirarAspirante(Aspirante, NroAspirante);
				return retval;
			} else if (!Declaraciones.UserList[AspiranteUI].GuildIndex == 0) {
				refError = Aspirante + " ya es parte de otro clan.";
				guilds[GI].RetirarAspirante(Aspirante, NroAspirante);
				return retval;
			}
		} else {
			if (!m_EstadoPermiteEntrarChar(Aspirante, GI)) {
				refError = Aspirante + " no puede entrar a un clan de alineación "
						+ Alineacion2String(guilds[GI].Alineacion);
				guilds[GI].RetirarAspirante(Aspirante, NroAspirante);
				return retval;
			} else if (GetGuildIndexFromChar(Aspirante)) {
				refError = Aspirante + " ya es parte de otro clan.";
				guilds[GI].RetirarAspirante(Aspirante, NroAspirante);
				return retval;
			}
		}
		/* 'el pj es aspirante al clan y puede entrar */

		guilds[GI].RetirarAspirante(Aspirante, NroAspirante);
		guilds[GI].AceptarNuevoMiembro(Aspirante);

		/* ' If player is online, update tag */
		if (AspiranteUI > 0) {
			UsUaRiOs.RefreshCharStatus(AspiranteUI);
		}

		retval = true;
		return retval;
	}

	static String GuildName(int GuildIndex) {
		String retval;
		/* '*************************************************** */
		/* 'Author: Unknown */
		/* 'Last Modification: - */
		/* ' */
		/* '*************************************************** */

		if (GuildIndex <= 0 || GuildIndex > modGuilds.CANTIDADDECLANES) {
			return retval;
		}

		retval = guilds[GuildIndex].retval;
		return retval;
	}

	static String GuildLeader(int GuildIndex) {
		String retval;
		/* '*************************************************** */
		/* 'Author: Unknown */
		/* 'Last Modification: - */
		/* ' */
		/* '*************************************************** */

		if (GuildIndex <= 0 || GuildIndex > modGuilds.CANTIDADDECLANES) {
			return retval;
		}

		retval = guilds[GuildIndex].GetLeader();
		return retval;
	}

	static String GuildAlignment(int GuildIndex) {
		String retval;
		/* '*************************************************** */
		/* 'Author: Unknown */
		/* 'Last Modification: - */
		/* ' */
		/* '*************************************************** */

		if (GuildIndex <= 0 || GuildIndex > modGuilds.CANTIDADDECLANES) {
			return retval;
		}

		retval = Alineacion2String(guilds[GuildIndex].Alineacion);
		return retval;
	}

	static String GuildFounder(int GuildIndex) {
		String retval;
		/* '*************************************************** */
		/* 'Autor: ZaMa */
		/* 'Returns the guild founder's name */
		/* 'Last Modification: 25/03/2009 */
		/* '*************************************************** */
		if (GuildIndex <= 0 || GuildIndex > modGuilds.CANTIDADDECLANES) {
			return retval;
		}

		retval = guilds[GuildIndex].Fundador;
		return retval;
	}

	static void SetNewGuildName(int GuildIndex, String /* FIXME BYREF!! */ newGuildName) {
		/* '*************************************************** */
		/* 'Author: Lex! */
		/* 'Last Modification: 15/05/2012 */
		/* 'Va a la clase de guilds para setear GuildName nuevo */
		/* '*************************************************** */

		guilds[GuildIndex].SetGuildName(newGuildName);
	}

}