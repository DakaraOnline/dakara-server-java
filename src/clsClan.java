/*  AUTOMATICALLY CONVERTED FILE  */
/* [(0, 'ATTRIBUTE'), (1, 'VB_Name'), (5, '='), (4, '"clsClan"')] */
/* [(0, 'ATTRIBUTE'), (1, 'VB_GlobalNameSpace'), (5, '='), (1, 'False')] */
/* [(0, 'ATTRIBUTE'), (1, 'VB_Creatable'), (5, '='), (1, 'True')] */
/* [(0, 'ATTRIBUTE'), (1, 'VB_PredeclaredId'), (5, '='), (1, 'False')] */
/* [(0, 'ATTRIBUTE'), (1, 'VB_Exposed'), (5, '='), (1, 'False')] */
/* '************************************************************** */
/* ' clsClan.cls */
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

public class clsClan {

	/* '' */
	/* ' clase clan */
	/* ' */
	/* ' Es el "ADO" de los clanes. La interfaz entre el disco y */
	/* ' el juego. Los datos no se guardan en memoria */
	/* ' para evitar problemas de sincronizacion, y considerando */
	/* ' que la performance de estas rutinas NO es critica. */
	/* ' by el oso :p */

	private String p_GuildName;
	private ALINEACION_GUILD p_Alineacion;
	/* 'Array de UserIndexes! */
	private vb6.Collection p_OnlineMembers;
	private vb6.Collection p_GMsOnline;
	private vb6.Collection p_PropuestasDePaz;
	private vb6.Collection p_PropuestasDeAlianza;
	private int p_IteradorRelaciones;
	private int p_IteradorOnlineMembers;
	private int p_IteradorPropuesta;
	private int p_IteradorOnlineGMs;
	/* 'Numero de guild en el mundo */
	private int p_GuildNumber;
	/* 'array de relaciones con los otros clanes */
	private RELACIONES_GUILD[] p_Relaciones = new RELACIONES_GUILD[0];
	private String GUILDINFOFILE;
	/* 'aca pq me es mas comodo setearlo y pq en ningun disenio */
	private String GUILDPATH;
	/* 'decente la capa de arriba se entera donde estan */
	private String MEMBERSFILE;
	/* 'los datos fisicamente */
	private String SOLICITUDESFILE;
	private String PROPUESTASFILE;
	private String RELACIONESFILE;
	private String VOTACIONESFILE;

	static final int NEWSLENGTH = 1024;
	static final int DESCLENGTH = 256;
	static final int CODEXLENGTH = 256;

	String GuildName() {
		String retval;
		retval = p_GuildName;
		return retval;
	}

	/* ' */
	/* 'ALINEACION Y ANTIFACCION */
	/* ' */

	ALINEACION_GUILD Alineacion() {
		ALINEACION_GUILD retval;
		retval = p_Alineacion;
		return retval;
	}

	int PuntosAntifaccion() {
		int retval = 0;
		retval = vb6.val(ES.GetVar(Declaraciones.GUILDINFOFILE, "GUILD" + p_GuildNumber, "Antifaccion"));
		return retval;
	}

	void PuntosAntifaccion(int p) {
		ES.WriteVar(Declaraciones.GUILDINFOFILE, "GUILD" + p_GuildNumber, "Antifaccion", vb6.CStr(p));
	}

	boolean CambiarAlineacion(ALINEACION_GUILD NuevaAlineacion) {
		boolean retval = false;
		p_Alineacion = NuevaAlineacion;
		ES.WriteVar(Declaraciones.GUILDINFOFILE, "GUILD" + p_GuildNumber, "Alineacion",
				modGuilds.Alineacion2String(p_Alineacion));

		if (p_Alineacion == ALINEACION_NEUTRO) {
			retval = true;
		}
		return retval;
	}

	/* ' */
	/* 'INICIALIZADORES */
	/* ' */

	void Class_Initialize() {
		Declaraciones.GUILDPATH = vb6.App.Instance().Path + "\\GUILDS\\";
		Declaraciones.GUILDINFOFILE = Declaraciones.GUILDPATH + "guildsinfo.inf";
	}

	void Class_Terminate() {
		p_OnlineMembers = null;
		p_GMsOnline = null;
		p_PropuestasDePaz = null;
		p_PropuestasDeAlianza = null;
	}

	void Inicializar(String GuildName, int GuildNumber, ALINEACION_GUILD Alineacion) {
		int i = 0;

		p_GuildName = GuildName;
		p_GuildNumber = GuildNumber;
		p_Alineacion = Alineacion;
		p_OnlineMembers = new Collection();
		p_GMsOnline = new Collection();
		p_PropuestasDePaz = new Collection();
		p_PropuestasDeAlianza = new Collection();
		/* 'ALLIESFILE = GUILDPATH & p_GuildName & "-Allied.all" */
		/* 'ENEMIESFILE = GUILDPATH & p_GuildName & "-enemys.ene" */
		RELACIONESFILE = Declaraciones.GUILDPATH + p_GuildName + "-relaciones.rel";
		MEMBERSFILE = Declaraciones.GUILDPATH + p_GuildName + "-members.mem";
		PROPUESTASFILE = Declaraciones.GUILDPATH + p_GuildName + "-propositions.pro";
		SOLICITUDESFILE = Declaraciones.GUILDPATH + p_GuildName + "-solicitudes.sol";
		VOTACIONESFILE = Declaraciones.GUILDPATH + p_GuildName + "-votaciones.vot";
		p_IteradorOnlineMembers = 0;
		p_IteradorPropuesta = 0;
		p_IteradorOnlineGMs = 0;
		p_IteradorRelaciones = 0;
		p_Relaciones = (p_Relaciones == null) ? new RELACIONES_GUILD[1 + modGuilds.CANTIDADDECLANES]
				: java.util.Arrays.copyOf(p_Relaciones, 1 + modGuilds.CANTIDADDECLANES);
		for (i = (1); i <= (modGuilds.CANTIDADDECLANES); i++) {
			p_Relaciones[i] = modGuilds.String2Relacion(ES.GetVar(RELACIONESFILE, "RELACIONES", vb6.CStr(i)));
		}
		for (i = (1); i <= (modGuilds.CANTIDADDECLANES); i++) {
			if (vb6.Trim(ES.GetVar(PROPUESTASFILE, vb6.CStr(i), "Pendiente")) == "1") {
				switch (modGuilds.String2Relacion(vb6.Trim(ES.GetVar(PROPUESTASFILE, vb6.CStr(i), "Tipo")))) {
				case ALIADOS:
					p_PropuestasDeAlianza.Add(i);
					break;

				case PAZ:
					p_PropuestasDePaz.Add(i);
					break;
				}
			}
		}
	}

	/* '' */
	/* ' esta TIENE QUE LLAMARSE LUEGO DE INICIALIZAR() */
	/* ' */
	/* ' @param Fundador Nombre del fundador del clan */
	/* ' */
	void InicializarNuevoClan(String /* FIXME BYREF!! */ Fundador) {
		/*
		 * 'string pq al comienzo quizas no hay archivo guildinfo.ini y oldq es
		 * ""
		 */
		String OldQ;
		int NewQ = 0;
		/* 'para que genere los archivos */
		ES.WriteVar(MEMBERSFILE, "INIT", "NroMembers", "0");
		ES.WriteVar(SOLICITUDESFILE, "INIT", "CantSolicitudes", "0");

		OldQ = ES.GetVar(Declaraciones.GUILDINFOFILE, "INIT", "nroguilds");
		if (vb6.IsNumeric(OldQ)) {
			NewQ = vb6.CInt(vb6.Trim(OldQ)) + 1;
		} else {
			NewQ = 1;
		}

		ES.WriteVar(Declaraciones.GUILDINFOFILE, "INIT", "NroGuilds", NewQ);

		ES.WriteVar(Declaraciones.GUILDINFOFILE, "GUILD" + NewQ, "Founder", Fundador);
		ES.WriteVar(Declaraciones.GUILDINFOFILE, "GUILD" + NewQ, "GuildName", p_GuildName);
		ES.WriteVar(Declaraciones.GUILDINFOFILE, "GUILD" + NewQ, "Date", Date);
		ES.WriteVar(Declaraciones.GUILDINFOFILE, "GUILD" + NewQ, "Antifaccion", "0");
		ES.WriteVar(Declaraciones.GUILDINFOFILE, "GUILD" + NewQ, "Alineacion",
				modGuilds.Alineacion2String(p_Alineacion));

	}

	void ProcesarFundacionDeOtroClan() {
		p_Relaciones = (p_Relaciones == null) ? new RELACIONES_GUILD[1 + modGuilds.CANTIDADDECLANES]
				: java.util.Arrays.copyOf(p_Relaciones, 1 + modGuilds.CANTIDADDECLANES);
		p_Relaciones[modGuilds.CANTIDADDECLANES] = PAZ;
	}

	/* ' */
	/* 'MEMBRESIAS */
	/* ' */

	String Fundador() {
		String retval;
		retval = ES.GetVar(Declaraciones.GUILDINFOFILE, "GUILD" + p_GuildNumber, "Founder");
		return retval;
	}

	/* 'Public Property Get JugadoresOnline() As String */
	/* 'Dim i As Integer */
	/* ' 'leve violacion de capas x aqui, je */
	/* ' For i = 1 To p_OnlineMembers.Count */
	/*
	 * ' JugadoresOnline = UserList(p_OnlineMembers.Item(i)).Name & "," &
	 * JugadoresOnline
	 */
	/* ' Next i */
	/* 'End Property */

	int CantidadDeMiembros() {
		int retval = 0;
		String OldQ;
		OldQ = ES.GetVar(MEMBERSFILE, "INIT", "NroMembers");
		retval = vb6.IIf(vb6.IsNumeric(OldQ), vb6.CInt(OldQ), 0);
		return retval;
	}

	void SetLeader(String /* FIXME BYREF!! */ leader) {
		ES.WriteVar(Declaraciones.GUILDINFOFILE, "GUILD" + p_GuildNumber, "Leader", leader);
	}

	String GetLeader() {
		String retval;
		retval = ES.GetVar(Declaraciones.GUILDINFOFILE, "GUILD" + p_GuildNumber, "Leader");
		return retval;
	}

	String[] GetMemberList() {
		String[] retval;
		int OldQ = 0;
		String[] list;
		int i = 0;

		OldQ = Me.CantidadDeMiembros;

		list = new String[0];
		list = (list == null) ? new String[OldQ - 1] : java.util.Arrays.copyOf(list, OldQ - 1);

		for (i = (1); i <= (OldQ); i++) {
			list[i - 1] = vb6.UCase(ES.GetVar(MEMBERSFILE, "Members", "Member" + i));
		}

		retval = list;
		return retval;
	}

	void ConectarMiembro(int UserIndex) {
		/* '*************************************************** */
		/* 'Autor: Unknown (orginal version) */
		/* 'Last Modification: 28/05/2010 */
		/* '28/05/2010: ZaMa - No avisa cuando loguea un dios o admin. */
		/* '*************************************************** */
		p_OnlineMembers.Add(UserIndex);

		/* ' No avisa cuando loguea un dios */
		if ((Declaraciones.UserList[UserIndex].flags.Privilegios && (PlayerType.Admin || PlayerType.Dios)) == 0) {
			modSendData.SendData(SendTarget.ToDiosesYclan, Declaraciones.UserList[UserIndex].GuildIndex,
					Protocol.PrepareMessageGuildChat(Declaraciones.UserList[UserIndex].Name + " se ha conectado."));
		}
	}

	void DesConectarMiembro(int UserIndex) {
		/* '*************************************************** */
		/* 'Autor: Unknown (orginal version) */
		/* 'Last Modification: 28/05/2010 */
		/* '28/05/2010: ZaMa - No avisa cuando desloguea un dios o admin. */
		/* '*************************************************** */
		int i = 0;
		for (i = (1); i <= (p_OnlineMembers.Count); i++) {
			if (p_OnlineMembers.Item[i] == UserIndex) {
				p_OnlineMembers.Remove(i);

				/* ' No avisa cuando se desconecta un dios */
				if ((Declaraciones.UserList[UserIndex].flags.Privilegios
						&& (PlayerType.Admin || PlayerType.Dios)) == 0) {
					modSendData.SendData(SendTarget.ToDiosesYclan, Declaraciones.UserList[UserIndex].GuildIndex,
							Protocol.PrepareMessageGuildChat(
									Declaraciones.UserList[UserIndex].Name + " se ha desconectado."));
				}

				return;
			}
		}

	}

	void AceptarNuevoMiembro(String /* FIXME BYREF!! */ Nombre) {
		int OldQ = 0;
		String OldQs;
		String ruta;

		ruta = Declaraciones.CharPath + Nombre + ".chr";
		if (General.FileExist(ruta)) {
			ES.WriteVar(ruta, "GUILD", "GUILDINDEX", p_GuildNumber);
			ES.WriteVar(ruta, "GUILD", "AspiranteA", "0");
			/*
			 * 'CantPs = GetVar(CharPath & Nombre & ".chr", "GUILD",
			 * "ClanesParticipo")
			 */
			/* 'If IsNumeric(CantPs) Then */
			/* ' CantP = CInt(CantPs) */
			/* 'Else */
			/* ' CantP = 0 */
			/* 'End If */
			/*
			 * 'Call WriteVar(CharPath & Nombre & ".chr", "GUILD",
			 * "ClanesParticipo", CantP + 1)
			 */
			OldQs = ES.GetVar(MEMBERSFILE, "INIT", "NroMembers");
			if (vb6.IsNumeric(OldQs)) {
				OldQ = vb6.CInt(OldQs);
			} else {
				OldQ = 0;
			}
			ES.WriteVar(MEMBERSFILE, "INIT", "NroMembers", OldQ + 1);
			ES.WriteVar(MEMBERSFILE, "Members", "Member" + OldQ + 1, Nombre);
		}

	}

	void ExpulsarMiembro(String /* FIXME BYREF!! */ Nombre) {
		int OldQ = 0;
		String Temps;
		int i = 0;
		boolean EsMiembro = false;
		String MiembroDe;

		if (vb6.LenB(vb6.dir(Declaraciones.CharPath + Nombre + ".chr")) != 0) {
			OldQ = vb6.CInt(ES.GetVar(MEMBERSFILE, "INIT", "NroMembers"));
			i = 1;
			Nombre = vb6.UCase(Nombre);
			while (i <= OldQ && vb6.UCase(vb6.Trim(ES.GetVar(MEMBERSFILE, "Members", "Member" + i))) != Nombre) {
				i = i + 1;
			}
			EsMiembro = i <= OldQ;

			if (EsMiembro) {
				ES.WriteVar(Declaraciones.CharPath + Nombre + ".chr", "GUILD", "GuildIndex", "");
				while (i < OldQ) {
					Temps = ES.GetVar(MEMBERSFILE, "Members", "Member" + i + 1);
					ES.WriteVar(MEMBERSFILE, "Members", "Member" + i, Temps);
					i = i + 1;
				}
				ES.WriteVar(MEMBERSFILE, "Members", "Member" + OldQ, "");
				/* 'seteo la cantidad de miembros nueva */
				ES.WriteVar(MEMBERSFILE, "INIT", "NroMembers", OldQ - 1);
				/* 'lo echo a el */
				MiembroDe = ES.GetVar(Declaraciones.CharPath + Nombre + ".chr", "GUILD", "Miembro");
				if (!vb6.InStr(1, MiembroDe, p_GuildName, vbTextCompare) > 0) {
					if (vb6.LenB(MiembroDe) != 0) {
						MiembroDe = MiembroDe + ",";
					}
					MiembroDe = MiembroDe + p_GuildName;
					ES.WriteVar(Declaraciones.CharPath + Nombre + ".chr", "GUILD", "Miembro", MiembroDe);
				}
			}

		}

	}

	void RemoveMemberName(String /* FIXME BYREF!! */ Nombre) {
		int OldQ = 0;
		String Temps;
		int i = 0;
		boolean EsMiembro = false;

		OldQ = vb6.CInt(ES.GetVar(MEMBERSFILE, "INIT", "NroMembers"));
		i = 1;
		Nombre = vb6.UCase(Nombre);
		while (i <= OldQ && vb6.UCase(vb6.Trim(ES.GetVar(MEMBERSFILE, "Members", "Member" + i))) != Nombre) {
			i = i + 1;
		}
		EsMiembro = i <= OldQ;

		if (EsMiembro) {
			while (i < OldQ) {
				Temps = ES.GetVar(MEMBERSFILE, "Members", "Member" + i + 1);
				ES.WriteVar(MEMBERSFILE, "Members", "Member" + i, Temps);
				i = i + 1;
			}

			ES.WriteVar(MEMBERSFILE, "Members", "Member" + OldQ, "");
			/* 'seteo la cantidad de miembros nueva */
			ES.WriteVar(MEMBERSFILE, "INIT", "NroMembers", OldQ - 1);
		}

	}

	/* ' */
	/* 'ASPIRANTES */
	/* ' */

	String[] GetAspirantes() {
		String[] retval;
		int OldQ = 0;
		String[] list;
		int i = 0;

		OldQ = Me.CantidadAspirantes();

		if (OldQ > 1) {
			list = new String[0];
			list = (list == null) ? new String[OldQ - 1] : java.util.Arrays.copyOf(list, OldQ - 1);
		} else {
			list = new String[0];
			list = (list == null) ? new String[0] : java.util.Arrays.copyOf(list, 0);
		}

		for (i = (1); i <= (OldQ); i++) {
			list[i - 1] = ES.GetVar(SOLICITUDESFILE, "SOLICITUD" + i, "Nombre");
		}

		retval = list;
		return retval;
	}

	int CantidadAspirantes() {
		int retval = 0;
		String Temps;

		retval = 0;
		Temps = ES.GetVar(SOLICITUDESFILE, "INIT", "CantSolicitudes");
		if (!vb6.IsNumeric(Temps)) {
			return retval;
		}
		retval = vb6.CInt(Temps);

		return retval;
	}

	String DetallesSolicitudAspirante(int NroAspirante) {
		String retval;
		retval = ES.GetVar(SOLICITUDESFILE, "SOLICITUD" + NroAspirante, "Detalle");
		return retval;
	}

	int NumeroDeAspirante(String /* FIXME BYREF!! */ Nombre) {
		int retval = 0;
		int i = 0;

		retval = 0;

		for (i = (1); i <= (modGuilds.MAXASPIRANTES); i++) {
			if (vb6.UCase(vb6.Trim(ES.GetVar(SOLICITUDESFILE, "SOLICITUD" + i, "Nombre"))) == vb6.UCase(Nombre)) {
				retval = i;
				return retval;
			}
		}
		return retval;
	}

	void NuevoAspirante(String /* FIXME BYREF!! */ Nombre,
			String /* FIXME BYREF!! */ Peticion) {
		int i = 0;
		String OldQ;
		int OldQI = 0;

		OldQ = ES.GetVar(SOLICITUDESFILE, "INIT", "CantSolicitudes");
		if (vb6.IsNumeric(OldQ)) {
			OldQI = vb6.CInt(OldQ);
		} else {
			OldQI = 0;
		}
		for (i = (1); i <= (modGuilds.MAXASPIRANTES); i++) {
			if (ES.GetVar(SOLICITUDESFILE, "SOLICITUD" + i, "Nombre") == "") {
				ES.WriteVar(SOLICITUDESFILE, "SOLICITUD" + i, "Nombre", Nombre);
				ES.WriteVar(SOLICITUDESFILE, "SOLICITUD" + i, "Detalle",
						vb6.IIf(vb6.Trim(Peticion) == "", "Peticion vacia", Peticion));
				ES.WriteVar(SOLICITUDESFILE, "INIT", "CantSolicitudes", OldQI + 1);
				ES.WriteVar(Declaraciones.CharPath + Nombre + ".chr", "GUILD", "ASPIRANTEA", p_GuildNumber);
				return;
			}
		}
	}

	void RetirarAspirante(String /* FIXME BYREF!! */ Nombre,
			int /* FIXME BYREF!! */ NroAspirante) {
		String OldQ;
		String OldQI;
		String Pedidos;
		int i = 0;

		OldQ = ES.GetVar(SOLICITUDESFILE, "INIT", "CantSolicitudes");
		if (vb6.IsNumeric(OldQ)) {
			OldQI = vb6.CInt(OldQ);
		} else {
			OldQI = 1;
		}
		/*
		 * 'Call WriteVar(SOLICITUDESFILE, "SOLICITUD" & NroAspirante, "Nombre",
		 * vbNullString)
		 */
		/*
		 * 'Call WriteVar(SOLICITUDESFILE, "SOLICITUD" & NroAspirante,
		 * "Detalle", vbNullString)
		 */
		ES.WriteVar(Declaraciones.CharPath + Nombre + ".chr", "GUILD", "ASPIRANTEA", "0");
		Pedidos = ES.GetVar(Declaraciones.CharPath + Nombre + ".chr", "GUILD", "Pedidos");
		if (!vb6.InStr(1, Pedidos, p_GuildName, vbTextCompare) > 0) {
			if (vb6.LenB(Pedidos) != 0) {
				Pedidos = Pedidos + ",";
			}
			Pedidos = Pedidos + p_GuildName;
			ES.WriteVar(Declaraciones.CharPath + Nombre + ".chr", "GUILD", "Pedidos", Pedidos);
		}

		ES.WriteVar(SOLICITUDESFILE, "INIT", "CantSolicitudes", OldQI - 1);
		for (i = (NroAspirante); i <= (modGuilds.MAXASPIRANTES - 1); i++) {
			ES.WriteVar(SOLICITUDESFILE, "SOLICITUD" + i, "Nombre",
					ES.GetVar(SOLICITUDESFILE, "SOLICITUD" + (i + 1), "Nombre"));
			ES.WriteVar(SOLICITUDESFILE, "SOLICITUD" + i, "Detalle",
					ES.GetVar(SOLICITUDESFILE, "SOLICITUD" + (i + 1), "Detalle"));
		}

		ES.WriteVar(SOLICITUDESFILE, "SOLICITUD" + modGuilds.MAXASPIRANTES, "Nombre", "");
		ES.WriteVar(SOLICITUDESFILE, "SOLICITUD" + modGuilds.MAXASPIRANTES, "Detalle", "");

	}

	void InformarRechazoEnChar(String /* FIXME BYREF!! */ Nombre,
			String /* FIXME BYREF!! */ Detalles) {
		ES.WriteVar(Declaraciones.CharPath + Nombre + ".chr", "GUILD", "MotivoRechazo", Detalles);
	}

	/* ' */
	/* 'DEFINICION DEL CLAN (CODEX Y NOTICIAS) */
	/* ' */

	String GetFechaFundacion() {
		String retval;
		retval = ES.GetVar(Declaraciones.GUILDINFOFILE, "GUILD" + p_GuildNumber, "Date");
		return retval;
	}

	void SetCodex(int CodexNumber, String /* FIXME BYREF!! */ codex) {
		ReplaceInvalidChars(codex);
		codex = vb6.Left(codex, CODEXLENGTH);
		ES.WriteVar(Declaraciones.GUILDINFOFILE, "GUILD" + p_GuildNumber, "Codex" + CodexNumber, codex);
	}

	String GetCodex(int CodexNumber) {
		String retval;
		retval = ES.GetVar(Declaraciones.GUILDINFOFILE, "GUILD" + p_GuildNumber, "Codex" + CodexNumber);
		return retval;
	}

	void SetURL(String /* FIXME BYREF!! */ URL) {
		ES.WriteVar(Declaraciones.GUILDINFOFILE, "GUILD" + p_GuildNumber, "URL", vb6.Left(URL, 40));
	}

	String GetURL() {
		String retval;
		retval = ES.GetVar(Declaraciones.GUILDINFOFILE, "GUILD" + p_GuildNumber, "URL");
		return retval;
	}

	void SetGuildNews(String /* FIXME BYREF!! */ News) {
		ReplaceInvalidChars(News);

		News = vb6.Left(News, NEWSLENGTH);

		ES.WriteVar(Declaraciones.GUILDINFOFILE, "GUILD" + p_GuildNumber, "GuildNews", News);
	}

	String GetGuildNews() {
		String retval;
		retval = ES.GetVar(Declaraciones.GUILDINFOFILE, "GUILD" + p_GuildNumber, "GuildNews");
		return retval;
	}

	void SetDesc(String /* FIXME BYREF!! */ desc) {
		ReplaceInvalidChars(desc);
		desc = vb6.Left(desc, DESCLENGTH);

		ES.WriteVar(Declaraciones.GUILDINFOFILE, "GUILD" + p_GuildNumber, "Desc", desc);
	}

	String GetDesc() {
		String retval;
		retval = ES.GetVar(Declaraciones.GUILDINFOFILE, "GUILD" + p_GuildNumber, "Desc");
		return retval;
	}

	/* ' */
	/* ' */
	/* 'ELECCIONES */
	/* ' */
	/* ' */

	boolean EleccionesAbiertas() {
		boolean retval = false;
		String ee;
		ee = ES.GetVar(Declaraciones.GUILDINFOFILE, "GUILD" + p_GuildNumber, "EleccionesAbiertas");
		/* 'cualquier otra cosa da falso */
		retval = (ee == "1");
		return retval;
	}

	void AbrirElecciones() {
		ES.WriteVar(Declaraciones.GUILDINFOFILE, "GUILD" + p_GuildNumber, "EleccionesAbiertas", "1");
		ES.WriteVar(Declaraciones.GUILDINFOFILE, "GUILD" + p_GuildNumber, "EleccionesFinalizan",
				vb6.DateAdd("d", 1, vb6.Now()));
		ES.WriteVar(VOTACIONESFILE, "INIT", "NumVotos", "0");
	}

	/* 'solo pueden cerrarse mediante recuento de votos */
	void CerrarElecciones() {
		ES.WriteVar(Declaraciones.GUILDINFOFILE, "GUILD" + p_GuildNumber, "EleccionesAbiertas", "0");
		ES.WriteVar(Declaraciones.GUILDINFOFILE, "GUILD" + p_GuildNumber, "EleccionesFinalizan", "");
		/* 'borramos toda la evidencia ;-) */
		KILL(VOTACIONESFILE);
	}

	void ContabilizarVoto(String /* FIXME BYREF!! */ Votante,
			String /* FIXME BYREF!! */ Votado) {
		int q = 0;
		String Temps;

		Temps = ES.GetVar(VOTACIONESFILE, "INIT", "NumVotos");
		q = vb6.IIf(vb6.IsNumeric(Temps), vb6.CInt(Temps), 0);
		ES.WriteVar(VOTACIONESFILE, "VOTOS", Votante, Votado);
		ES.WriteVar(VOTACIONESFILE, "INIT", "NumVotos", vb6.CStr(q + 1));
	}

	boolean YaVoto(Object /* FIXME BYREF!! */ Votante) {
		boolean retval = false;
		retval = ((vb6.LenB(vb6.Trim(ES.GetVar(VOTACIONESFILE, "VOTOS", Votante)))) != 0);
		return retval;
	}

	String ContarVotos(int /* FIXME BYREF!! */ CantGanadores) {
 String retval;
 int q = 0;
 int i = 0;
 String Temps;
 String tempV;
 diccionario d;
 
 /* FIXME: ON ERROR GOTO errh */
 retval = "";
 CantGanadores = 0;
 Temps = ES.GetVar(MEMBERSFILE, "INIT", "NroMembers");
 q = vb6.IIf(vb6.IsNumeric(Temps), vb6.CInt(Temps), 0);
  if (q>0) {
  /* 'el diccionario tiene clave el elegido y valor la #votos */
  d = new diccionario();
  
   for (i = (1); i <= (q); i++) {
   /* 'miembro del clan */
   Temps = ES.GetVar(MEMBERSFILE, "MEMBERS", "Member" + i);
   
   /* 'a quienvoto */
   tempV = ES.GetVar(VOTACIONESFILE, "VOTOS", Temps);
   
   /* 'si voto a alguien contabilizamos el voto */
    if (vb6.LenB(tempV) != 0) {
    /* 'cuantos votos tiene? */
     if (!vb6.IsNull(d.At(tempV))) {
     d.AtPut(tempV, vb6.CInt(d.At(tempV))+1);
     } else {
     d.AtPut(tempV, 1);
    }
   }
  }
  
  /* 'quien quedo con mas votos, y cuantos tuvieron esos votos? */
  retval = d.MayorValor(CantGanadores);
  
  d = null;
 }
 
 return retval;
 /* FIXME: errh : */
 General.LogError("clsClan.Contarvotos: " + Err.description);
 if (!dIs null ) {
 d = null;
 }
 retval = "";
return retval;
}

	boolean RevisarElecciones() {
 boolean retval = false;
 vb6.Date FechaSufragio;
 String Temps;
 String Ganador;
 int CantGanadores = 0;
 String[] list;
 int i = 0;
 
 retval = false;
 Temps = vb6.Trim(ES.GetVar(Declaraciones.GUILDINFOFILE, "GUILD" + p_GuildNumber, "EleccionesFinalizan"));
 
 if (Temps == "") {
 return retval;
 }
 
  if (vb6.IsDate(Temps)) {
  FechaSufragio = CDate[Temps];
  /* 'toca! */
   if (FechaSufragio<vb6.Now()) {
   Ganador = ContarVotos(CantGanadores);
   
    if (CantGanadores>1) {
    /* 'empate en la votacion */
    SetGuildNews("*Empate en la votación. " + Ganador + " con " + CantGanadores + " votos ganaron las elecciones del clan.");
    } else if (CantGanadores == 1) {
    list = Me.GetMemberList();
    
     for (i = (0); i <= (vb6.UBound(list[])); i++) {
     if (Ganador == list[i]) {
     break; /* FIXME: EXIT FOR */
     }
    }
    
     if (i<=vb6.UBound(list[])) {
     SetGuildNews("*" + Ganador + " ganó la elección del clan*");
     Me.SetLeader(Ganador);
     retval = true;
     } else {
     SetGuildNews("*" + Ganador + " ganó la elección del clan pero abandonó las filas por lo que la votación queda desierta*");
    }
    } else {
    SetGuildNews("*El período de votación se cerró sin votos*");
   }
   
   CerrarElecciones();
   
  }
  } else {
  General.LogError("clsClan.RevisarElecciones: tempS is not Date");
 }
 
return retval;
}

	/* '/VOTACIONES */

	/* ' */
	/* 'RELACIONES */
	/* ' */

	int CantidadPropuestas(RELACIONES_GUILD Tipo) {
		int retval = 0;
		switch (Tipo) {
		case ALIADOS:
			retval = p_PropuestasDeAlianza.Count;
			break;

		case GUERRA:

			break;

		case PAZ:
			retval = p_PropuestasDePaz.Count;
			break;
		}
		return retval;
	}

	int CantidadEnemys() {
		int retval = 0;
		int i = 0;
		for (i = (1); i <= (modGuilds.CANTIDADDECLANES); i++) {
			retval = retval + vb6.IIf(p_Relaciones[i] == GUERRA, 1, 0);
		}
		return retval;
	}

	int CantidadAllies() {
		int retval = 0;
		int i = 0;
		for (i = (1); i <= (modGuilds.CANTIDADDECLANES); i++) {
			retval = retval + vb6.IIf(p_Relaciones[i] == ALIADOS, 1, 0);
		}
		return retval;
	}

	RELACIONES_GUILD GetRelacion(int OtroGuild) {
		RELACIONES_GUILD retval;
		retval = p_Relaciones[OtroGuild];
		return retval;
	}

	void SetRelacion(int GuildIndex, RELACIONES_GUILD Relacion) {
		p_Relaciones[GuildIndex] = Relacion;
		ES.WriteVar(RELACIONESFILE, "RELACIONES", GuildIndex, modGuilds.Relacion2String(Relacion));
	}

	void SetPropuesta(RELACIONES_GUILD Tipo, int OtroGuild,
			String /* FIXME BYREF!! */ Detalle) {
		ES.WriteVar(PROPUESTASFILE, vb6.CStr(OtroGuild), "Detalle", Detalle);
		ES.WriteVar(PROPUESTASFILE, vb6.CStr(OtroGuild), "Tipo", modGuilds.Relacion2String(Tipo));
		ES.WriteVar(PROPUESTASFILE, vb6.CStr(OtroGuild), "Pendiente", "1");
		switch (Tipo) {
		case ALIADOS:
			p_PropuestasDeAlianza.Add(OtroGuild);
			break;

		case PAZ:
			p_PropuestasDePaz.Add(OtroGuild);
			break;
		}
	}

	void AnularPropuestas(int OtroGuild) {
 int i = 0;
 
 ES.WriteVar(PROPUESTASFILE, vb6.CStr(OtroGuild), "Detalle", "");
 ES.WriteVar(PROPUESTASFILE, vb6.CStr(OtroGuild), "Pendiente", "0");
  for (i = (1); i <= (p_PropuestasDePaz.Count); i++) {
  if (p_PropuestasDePaz.Item[i] == OtroGuild) {
  p_PropuestasDePaz.Remove[i];
  }
  return;
 }
  for (i = (1); i <= (p_PropuestasDeAlianza.Count); i++) {
  if (p_PropuestasDeAlianza.Item[i] == OtroGuild) {
  p_PropuestasDeAlianza.Remove[i];
  }
  return;
 }
 
}

	String GetPropuesta(int OtroGuild, RELACIONES_GUILD /* FIXME BYREF!! */ Tipo) {
		String retval;
		/*
		 * 'trae la solicitd que haya, no valida si es actual o de que tipo es
		 */
		retval = ES.GetVar(PROPUESTASFILE, vb6.CStr(OtroGuild), "Detalle");
		Tipo = modGuilds.String2Relacion(ES.GetVar(PROPUESTASFILE, vb6.CStr(OtroGuild), "Tipo"));
		return retval;
	}

	boolean HayPropuesta(int OtroGuild, RELACIONES_GUILD /* FIXME BYREF!! */ Tipo) {
		boolean retval = false;
		int i = 0;

		retval = false;
		switch (Tipo) {
		case ALIADOS:
			for (i = (1); i <= (p_PropuestasDeAlianza.Count); i++) {
				if (p_PropuestasDeAlianza.Item[i] == OtroGuild) {
					retval = true;
				}
			}
			break;

		case PAZ:
			for (i = (1); i <= (p_PropuestasDePaz.Count); i++) {
				if (p_PropuestasDePaz.Item[i] == OtroGuild) {
					retval = true;
				}
			}
			break;

		case GUERRA:

			break;
		}

		return retval;
	}

	/* 'Public Function GetEnemy(ByVal EnemyIndex As Integer) As String */
	/* ' GetEnemy = GetVar(ENEMIESFILE, "ENEMYS", "ENEMY" & EnemyIndex) */
	/* 'End Function */

	/* 'Public Function GetAllie(ByVal AllieIndex As Integer) As String */
	/* ' GetAllie = GetVar(ALLIESFILE, "ALLIES", "ALLIE" & AllieIndex) */
	/* 'End Function */

	/* ' */
	/* 'ITERADORES */
	/* ' */

	int Iterador_ProximaPropuesta(RELACIONES_GUILD Tipo) {
		int retval = 0;

		retval = 0;
		switch (Tipo) {
		case ALIADOS:
			if (p_IteradorPropuesta < p_PropuestasDeAlianza.Count) {
				p_IteradorPropuesta = p_IteradorPropuesta + 1;
				retval = p_PropuestasDeAlianza.Item[p_IteradorPropuesta];
			}

			if (p_IteradorPropuesta >= p_PropuestasDeAlianza.Count) {
				p_IteradorPropuesta = 0;
			}
			break;

		case PAZ:
			if (p_IteradorPropuesta < p_PropuestasDePaz.Count) {
				p_IteradorPropuesta = p_IteradorPropuesta + 1;
				retval = p_PropuestasDePaz.Item[p_IteradorPropuesta];
			}

			if (p_IteradorPropuesta >= p_PropuestasDePaz.Count) {
				p_IteradorPropuesta = 0;
			}
			break;
		}

		return retval;
	}

	int m_Iterador_ProximoUserIndex() {
		int retval = 0;

		if (p_IteradorOnlineMembers < p_OnlineMembers.Count) {
			p_IteradorOnlineMembers = p_IteradorOnlineMembers + 1;
			retval = p_OnlineMembers.Item[p_IteradorOnlineMembers];
		} else {
			p_IteradorOnlineMembers = 0;
			retval = 0;
		}
		return retval;
	}

	int Iterador_ProximoGM() {
		int retval = 0;

		if (p_IteradorOnlineGMs < p_GMsOnline.Count) {
			p_IteradorOnlineGMs = p_IteradorOnlineGMs + 1;
			retval = p_GMsOnline.Item[p_IteradorOnlineGMs];
		} else {
			p_IteradorOnlineGMs = 0;
			retval = 0;
		}
		return retval;
	}

	int Iterador_ProximaRelacion(RELACIONES_GUILD r) {
		int retval = 0;

		while (p_IteradorRelaciones < vb6.UBound(p_Relaciones)) {

			p_IteradorRelaciones = p_IteradorRelaciones + 1;
			if (p_Relaciones[p_IteradorRelaciones] == r) {
				retval = p_IteradorRelaciones;
				return retval;
			}
		}

		if (p_IteradorRelaciones >= vb6.UBound(p_Relaciones)) {
			p_IteradorRelaciones = 0;
		}
		return retval;
	}
	/* ' */
	/* ' */
	/* ' */

	/* ' */
	/* 'ADMINISTRATIVAS */
	/* ' */

	void ConectarGM(int UserIndex) {
		p_GMsOnline.Add(UserIndex);
	}

	void DesconectarGM(int UserIndex) {
 int i = 0;
  for (i = (1); i <= (p_GMsOnline.Count); i++) {
   if (p_GMsOnline.Item[i] == UserIndex) {
   p_GMsOnline.Remove[i];
  }
 }
}

	/* ' */
	/* 'VARIAS, EXTRAS Y DEMASES */
	/* ' */

	void ReplaceInvalidChars(String /* FIXME BYREF!! */ S) {
		if (vb6.InStrB(S, vb6.Chr(13)) != 0) {
			S = vb6.Replace(S, vb6.Chr(13), "");
		}
		if (vb6.InStrB(S, vb6.Chr(10)) != 0) {
			S = vb6.Replace(S, vb6.Chr(10), "");
		}
		if (vb6.InStrB(S, "¬") != 0) {
			/* 'morgo usaba esto como "separador" */
			S = vb6.Replace(S, "¬", "");
		}
	}

	void SetGuildName(String /* FIXME BYREF!! */ newGuildName) {
		/* '*************************************************** */
		/* 'Author: Lex! */
		/* 'Last Modification: 14/05/2012 */
		/* 'Setea GuildName */
		/* '*************************************************** */
		p_GuildName = newGuildName;

		RELACIONESFILE = Declaraciones.GUILDPATH + p_GuildName + "-relaciones.rel";
		MEMBERSFILE = Declaraciones.GUILDPATH + p_GuildName + "-members.mem";
		PROPUESTASFILE = Declaraciones.GUILDPATH + p_GuildName + "-propositions.pro";
		SOLICITUDESFILE = Declaraciones.GUILDPATH + p_GuildName + "-solicitudes.sol";
		VOTACIONESFILE = Declaraciones.GUILDPATH + p_GuildName + "-votaciones.vot";
	}

}