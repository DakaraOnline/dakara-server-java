/*  AUTOMATICALLY CONVERTED FILE  */
/* [(0, 'ATTRIBUTE'), (1, 'VB_Name'), (5, '='), (4, '"mdParty"')] */
/* '************************************************************** */
/* ' mdParty.bas - Library of functions to manipulate parties. */
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

public class mdParty {

	/* '' */
	/* ' SOPORTES PARA LAS PARTIES */
	/* ' (Ver este modulo como una clase abstracta "PartyManager") */
	/* ' */

	/* '' */
	/* 'cantidad maxima de parties en el servidor */
	static final int MAX_PARTIES = 300;

	/* '' */
	/* 'nivel minimo para crear party */
	static final int MINPARTYLEVEL = 15;

	/* '' */
	/* 'Cantidad maxima de gente en la party */
	static final int PARTY_MAXMEMBERS = 5;

	/* '' */
	/* 'Si esto esta en True, la exp sale por cada golpe que le da */
	/*
	 * 'Si no, la exp la recibe al salirse de la party (pq las partys, floodean)
	 */
	static final boolean PARTY_EXPERIENCIAPORGOLPE = false;

	/* '' */
	/* 'maxima diferencia de niveles permitida en una party */
	static final int MAXPARTYDELTALEVEL = 7;

	/* '' */
	/* 'distancia al leader para que este acepte el ingreso */
	static final int MAXDISTANCIAINGRESOPARTY = 2;

	/* '' */
	/* 'maxima distancia a un exito para obtener su experiencia */
	static final int PARTY_MAXDISTANCIA = 18;

	/* '' */
	/* 'restan las muertes de los miembros? */
	static final boolean CASTIGOS = false;

	/* '' */
	/* 'Numero al que elevamos el nivel de cada miembro de la party */
	/*
	 * 'Esto es usado para calcular la distribución de la experiencia entre los
	 * miembros
	 */
	/* 'Se lee del archivo de balance */
	public static float ExponenteNivelParty;

	/* '' */
	/* 'tPartyMember */
	/* ' */
	/* ' @param UserIndex UserIndex */
	/* ' @param Experiencia Experiencia */
	/* ' */
	static public class tPartyMember {
		int UserIndex;
		double Experiencia;
	}

	static int NextParty() {
 int retval;
 /* '*************************************************** */
 /* 'Author: Unknown */
 /* 'Last Modification: - */
 /* ' */
 /* '*************************************************** */
 
 int i;
 retval = -1;
  for (i = (1); i <= (mdParty.MAX_PARTIES); i++) {
   if (Declaraciones.Parties[i]Is null ) {
   retval = i;
   return retval;
  }
 }
return retval;
}

	static boolean PuedeCrearParty(int UserIndex) {
		boolean retval;
		/* '*************************************************** */
		/* 'Author: Unknown */
		/* 'Last Modification: 05/22/2010 (Marco) */
		/*
		 * ' - 05/22/2010 : staff members aren't allowed to party anyone.
		 * (Marco)
		 */
		/* '*************************************************** */

		retval = true;

		if ((Declaraciones.UserList[UserIndex].flags.Privilegios && PlayerType.User) == 0) {
			/* 'staff members aren't allowed to party anyone. */
			Protocol.WriteConsoleMsg(UserIndex, "¡Los miembros del staff no pueden crear partys!",
					FontTypeNames.FONTTYPE_PARTY);
			retval = false;
		} else if (vb6.CInt(Declaraciones.UserList[UserIndex].Stats.UserAtributos[eAtributos.Carisma])
				* Declaraciones.UserList[UserIndex].Stats.UserSkills[eSkill.Liderazgo] < 100) {
			Protocol.WriteConsoleMsg(UserIndex, "Tu carisma y liderazgo no son suficientes para liderar una party.",
					FontTypeNames.FONTTYPE_PARTY);
			retval = false;
		} else if (Declaraciones.UserList[UserIndex].flags.Muerto == 1) {
			Protocol.WriteConsoleMsg(UserIndex, "¡¡Estás muerto!!", FontTypeNames.FONTTYPE_PARTY);
			retval = false;
		}
		return retval;
	}

	static void CrearParty(int UserIndex) {
		/* '*************************************************** */
		/* 'Author: Unknown */
		/* 'Last Modification: - */
		/* ' */
		/* '*************************************************** */

		int tInt;

		if (Declaraciones.UserList[UserIndex].PartyIndex == 0) {
			if (Declaraciones.UserList[UserIndex].flags.Muerto == 0) {
				if (Declaraciones.UserList[UserIndex].Stats.UserSkills[eSkill.Liderazgo] >= 5) {
					tInt = mdParty.NextParty;
					if (tInt == -1) {
						Protocol.WriteConsoleMsg(UserIndex, "Por el momento no se pueden crear más parties.",
								FontTypeNames.FONTTYPE_PARTY);
						return;
					} else {
						Declaraciones.Parties[tInt] = new clsParty();
						if (!Declaraciones.Parties[tInt].NuevoMiembro(UserIndex)) {
							Protocol.WriteConsoleMsg(UserIndex, "La party está llena, no puedes entrar.",
									FontTypeNames.FONTTYPE_PARTY);
							Declaraciones.Parties[tInt] = null;
							return;
						} else {
							Protocol.WriteConsoleMsg(UserIndex, "¡Has formado una party!",
									FontTypeNames.FONTTYPE_PARTY);
							Declaraciones.UserList[UserIndex].PartyIndex = tInt;
							Declaraciones.UserList[UserIndex].PartySolicitud = 0;
							if (!Declaraciones.Parties[tInt].HacerLeader(UserIndex)) {
								Protocol.WriteConsoleMsg(UserIndex, "No puedes hacerte líder.",
										FontTypeNames.FONTTYPE_PARTY);
							} else {
								Protocol.WriteConsoleMsg(UserIndex, "¡Te has convertido en líder de la party!",
										FontTypeNames.FONTTYPE_PARTY);
							}
						}
					}
				} else {
					Protocol.WriteConsoleMsg(UserIndex,
							"No tienes suficientes puntos de liderazgo para liderar una party.",
							FontTypeNames.FONTTYPE_PARTY);
				}
			} else {
				Protocol.WriteConsoleMsg(UserIndex, "¡¡Estás muerto!!", FontTypeNames.FONTTYPE_PARTY);
			}
		} else {
			Protocol.WriteConsoleMsg(UserIndex, "Ya perteneces a una party.", FontTypeNames.FONTTYPE_PARTY);
		}
	}

	static void SolicitarIngresoAParty(int UserIndex) {
		/* '*************************************************** */
		/* 'Author: Unknown */
		/* 'Last Modification: 05/22/2010 (Marco) */
		/*
		 * ' - 05/22/2010 : staff members aren't allowed to party anyone.
		 * (Marco)
		 */
		/*
		 * '18/09/2010: ZaMa - Ahora le avisa al funda de la party cuando
		 * alguien quiere ingresar a la misma.
		 */
		/*
		 * '18/09/2010: ZaMa - Contemple mas ecepciones (solo se le puede mandar
		 * party al lider)
		 */
		/* '*************************************************** */

		/* 'ESTO ES enviado por el PJ para solicitar el ingreso a la party */
		int TargetUserIndex;
		int PartyIndex;

		/* 'staff members aren't allowed to party anyone */
		if ((Declaraciones.UserList[UserIndex].flags.Privilegios && PlayerType.User) == 0) {
			Protocol.WriteConsoleMsg(UserIndex, "¡Los miembros del staff no pueden unirse a partys!",
					FontTypeNames.FONTTYPE_PARTY);
			return;
		}

		if (Declaraciones.UserList[UserIndex].PartyIndex > 0) {
			/* 'si ya esta en una party */
			Protocol.WriteConsoleMsg(UserIndex, "Ya perteneces a una party, escribe /SALIRPARTY para abandonarla",
					FontTypeNames.FONTTYPE_PARTY);
			Declaraciones.UserList[UserIndex].PartySolicitud = 0;
			return;
		}

		/* ' Muerto? */
		if (Declaraciones.UserList[UserIndex].flags.Muerto == 1) {
			Protocol.WriteConsoleMsg(UserIndex, "¡¡Estás muerto!!", FontTypeNames.FONTTYPE_INFO);
			Declaraciones.UserList[UserIndex].PartySolicitud = 0;
			return;
		}

		TargetUserIndex = Declaraciones.UserList[UserIndex].flags.TargetUser;
		/* ' Target valido? */
		if (TargetUserIndex > 0) {

			PartyIndex = Declaraciones.UserList[TargetUserIndex].PartyIndex;
			/* ' Tiene party? */
			if (PartyIndex > 0) {

				/* ' Es el lider? */
				if (Declaraciones.Parties[PartyIndex].EsPartyLeader(TargetUserIndex)) {
					Declaraciones.UserList[UserIndex].PartySolicitud = PartyIndex;
					Protocol.WriteConsoleMsg(UserIndex, "El lider decidirá si te acepta en la party.",
							FontTypeNames.FONTTYPE_PARTY);
					Protocol.WriteConsoleMsg(TargetUserIndex,
							Declaraciones.UserList[UserIndex].name + " solicita ingresar a tu party.",
							FontTypeNames.FONTTYPE_PARTY);

					/* ' No es lider */
				} else {
					Protocol.WriteConsoleMsg(UserIndex,
							Declaraciones.UserList[TargetUserIndex].name + " no es lider de la party.",
							FontTypeNames.FONTTYPE_PARTY);
				}

				/* ' No tiene party */
			} else {
				Protocol.WriteConsoleMsg(UserIndex,
						Declaraciones.UserList[TargetUserIndex].name + " no pertenece a ninguna party.",
						FontTypeNames.FONTTYPE_PARTY);
				Declaraciones.UserList[UserIndex].PartySolicitud = 0;
				return;
			}

			/* ' Target inválido */
		} else {
			Protocol.WriteConsoleMsg(UserIndex,
					"Para ingresar a una party debes hacer click sobre el fundador y luego escribir /PARTY",
					FontTypeNames.FONTTYPE_PARTY);
			Declaraciones.UserList[UserIndex].PartySolicitud = 0;
		}

	}

	static void SalirDeParty(int UserIndex) {
		/* '*************************************************** */
		/* 'Author: Unknown */
		/* 'Last Modification: - */
		/* ' */
		/* '*************************************************** */

		int PI;
		PI = Declaraciones.UserList[UserIndex].PartyIndex;
		if (PI > 0) {
			if (Declaraciones.Parties[PI].SaleMiembro(UserIndex)) {
				/* 'sale el leader */
				Declaraciones.Parties[PI] = null;
			} else {
				Declaraciones.UserList[UserIndex].PartyIndex = 0;
			}
		} else {
			Protocol.WriteConsoleMsg(UserIndex, "No eres miembro de ninguna party.", FontTypeNames.FONTTYPE_INFO);
		}

	}

	static void ExpulsarDeParty(int leader, int OldMember) {
		/* '*************************************************** */
		/* 'Author: Unknown */
		/* 'Last Modification: - */
		/* ' */
		/* '*************************************************** */

		int PI;
		PI = Declaraciones.UserList[leader].PartyIndex;

		if (PI == Declaraciones.UserList[OldMember].PartyIndex) {
			if (Declaraciones.Parties[PI].SaleMiembro(OldMember)) {
				/* 'si la funcion me da true, entonces la party se disolvio */
				/* 'y los partyindex fueron reseteados a 0 */
				Declaraciones.Parties[PI] = null;
			} else {
				Declaraciones.UserList[OldMember].PartyIndex = 0;
			}
		} else {
			Protocol.WriteConsoleMsg(leader,
					vb6.LCase(Declaraciones.UserList[OldMember].name) + " no pertenece a tu party.",
					FontTypeNames.FONTTYPE_INFO);
		}

	}

	/* '' */
	/*
	 * ' Determines if a user can use party commands like /acceptparty or not.
	 */
	/* ' */
	/* ' @param User Specifies reference to user */
	/* ' @return True if the user can use party commands, false if not. */
	static boolean UserPuedeEjecutarComandos(int User) {
		boolean retval;
		/* '************************************************* */
		/* 'Author: Marco Vanotti(Marco) */
		/* 'Last modified: 05/05/09 */
		/* ' */
		/* '************************************************* */
		int PI;

		PI = Declaraciones.UserList[User].PartyIndex;

		if (PI > 0) {
			if (Declaraciones.Parties[PI].EsPartyLeader(User)) {
				retval = true;
			} else {
				Protocol.WriteConsoleMsg(User, "¡No eres el líder de tu party!", FontTypeNames.FONTTYPE_PARTY);
				return retval;
			}
		} else {
			Protocol.WriteConsoleMsg(User, "No eres miembro de ninguna party.", FontTypeNames.FONTTYPE_INFO);
			return retval;
		}
		return retval;
	}

	static void AprobarIngresoAParty(int leader, int NewMember) {
		/* '*************************************************** */
		/* 'Author: Unknown */
		/* 'Last Modification: 11/03/2010 */
		/*
		 * '11/03/2010: ZaMa - Le avisa al lider si intenta aceptar a alguien
		 * que sea mimebro de su propia party.
		 */
		/* '*************************************************** */

		/* 'el UI es el leader */
		int PI;
		String razon;

		PI = Declaraciones.UserList[leader].PartyIndex;

		if (Declaraciones.UserList[NewMember].PartySolicitud == PI) {
			if (!Declaraciones.UserList[NewMember].flags.Muerto == 1) {
				if (Declaraciones.UserList[NewMember].PartyIndex == 0) {
					if (Declaraciones.Parties[PI].PuedeEntrar(NewMember, razon)) {
						if (Declaraciones.Parties[PI].NuevoMiembro(NewMember)) {
							Declaraciones.Parties[PI]
									.MandarMensajeAConsola(
											Declaraciones.UserList[leader].name + " ha aceptado a "
													+ Declaraciones.UserList[NewMember].name + " en la party.",
											"Servidor");
							Declaraciones.UserList[NewMember].PartyIndex = PI;
							Declaraciones.UserList[NewMember].PartySolicitud = 0;
						} else {
							/* 'no pudo entrar */
							/*
							 * 'ACA UNO PUEDE CODIFICAR OTRO TIPO DE ERRORES...
							 */
							modSendData.SendData(SendTarget.ToAdmins, leader,
									Protocol.PrepareMessageConsoleMsg(
											" Servidor> CATÁSTROFE EN PARTIES, NUEVOMIEMBRO DIO FALSE! :S ",
											FontTypeNames.FONTTYPE_PARTY));
						}
					} else {
						/* 'no debe entrar */
						Protocol.WriteConsoleMsg(leader, razon, FontTypeNames.FONTTYPE_PARTY);
					}
				} else {
					if (Declaraciones.UserList[NewMember].PartyIndex == PI) {
						Protocol.WriteConsoleMsg(leader,
								vb6.LCase(Declaraciones.UserList[NewMember].name) + " ya es miembro de la party.",
								FontTypeNames.FONTTYPE_PARTY);
					} else {
						Protocol.WriteConsoleMsg(leader,
								Declaraciones.UserList[NewMember].name + " ya es miembro de otra party.",
								FontTypeNames.FONTTYPE_PARTY);
					}

					return;
				}
			} else {
				Protocol.WriteConsoleMsg(leader, "¡Está muerto, no puedes aceptar miembros en ese estado!",
						FontTypeNames.FONTTYPE_PARTY);
				return;
			}
		} else {
			if (Declaraciones.UserList[NewMember].PartyIndex == PI) {
				Protocol.WriteConsoleMsg(leader,
						vb6.LCase(Declaraciones.UserList[NewMember].name) + " ya es miembro de la party.",
						FontTypeNames.FONTTYPE_PARTY);
			} else {
				Protocol.WriteConsoleMsg(leader,
						vb6.LCase(Declaraciones.UserList[NewMember].name) + " no ha solicitado ingresar a tu party.",
						FontTypeNames.FONTTYPE_PARTY);
			}

			return;
		}

	}

	static int IsPartyMember(int UserIndex, int PartyIndex) {
		int retval;
		int MemberIndex;

		for (MemberIndex = (1); MemberIndex <= (mdParty.PARTY_MAXMEMBERS); MemberIndex++) {

		}
		return retval;
	}

	static void BroadCastParty(int UserIndex, String /* FIXME BYREF!! */ texto) {
		/* '*************************************************** */
		/* 'Author: Unknown */
		/* 'Last Modification: - */
		/* ' */
		/* '*************************************************** */

		int PI;

		PI = Declaraciones.UserList[UserIndex].PartyIndex;

		if (PI > 0) {
			Declaraciones.Parties[PI].MandarMensajeAConsola(texto, Declaraciones.UserList[UserIndex].name);
		}

	}

	static void OnlineParty(int UserIndex) {
 /* '************************************************* */
 /* 'Author: Unknown */
 /* 'Last modified: 11/27/09 (Budi) */
 /* 'Adapte la función a los nuevos métodos de clsParty */
 /* '************************************************* */
 int i;
 int PI;
 String Text;
 int[] MembersOnline;
 
 PI = Declaraciones.UserList[UserIndex].PartyIndex;
 
  if (PI>0) {
  Declaraciones.Parties[PI].ObtenerMiembrosOnline(MembersOnline[]);
  Text = "Nombre(Exp): ";
   for (i = (1); i <= (mdParty.PARTY_MAXMEMBERS); i++) {
    if (MembersOnline[i]>0) {
    Text = Text + " - " + Declaraciones.UserList[MembersOnline[i]].name + " (" + vb6.Fix(Declaraciones.Parties[PI].MiExperiencia(MembersOnline[i])) + ")";
   }
  }
  Text = Text + ". Experiencia total: " + Declaraciones.Parties[PI].ObtenerExperienciaTotal();
  Protocol.WriteConsoleMsg(UserIndex, Text, FontTypeNames.FONTTYPE_PARTY);
 }
 
}

	static void TransformarEnLider(int OldLeader, int NewLeader) {
		/* '*************************************************** */
		/* 'Author: Unknown */
		/* 'Last Modification: - */
		/* ' */
		/* '*************************************************** */

		int PI;

		if (OldLeader == NewLeader) {
			return;
		}

		PI = Declaraciones.UserList[OldLeader].PartyIndex;

		if (PI == Declaraciones.UserList[NewLeader].PartyIndex) {
			if (Declaraciones.UserList[NewLeader].flags.Muerto == 0) {
				if (Declaraciones.Parties[PI].HacerLeader(NewLeader)) {
					Declaraciones.Parties[PI].MandarMensajeAConsola(
							"El nuevo líder de la party es " + Declaraciones.UserList[NewLeader].name,
							Declaraciones.UserList[OldLeader].name);
				} else {
					Protocol.WriteConsoleMsg(OldLeader, "¡No se ha hecho el cambio de mando!",
							FontTypeNames.FONTTYPE_PARTY);
				}
			} else {
				Protocol.WriteConsoleMsg(OldLeader, "¡Está muerto!", FontTypeNames.FONTTYPE_INFO);
			}
		} else {
			Protocol.WriteConsoleMsg(OldLeader,
					vb6.LCase(Declaraciones.UserList[NewLeader].name) + " no pertenece a tu party.",
					FontTypeNames.FONTTYPE_INFO);
		}

	}

	static void ActualizaExperiencias() {
 /* '*************************************************** */
 /* 'Author: Unknown */
 /* 'Last Modification: - */
 /* ' */
 /* '*************************************************** */
 
 /* 'esta funcion se invoca antes de worlsaves, y apagar servidores */
 /* 'en caso que la experiencia sea acumulada y no por golpe */
 /* 'para que grabe los datos en los charfiles */
 int i;
 
  if (!mdParty.PARTY_EXPERIENCIAPORGOLPE) {
  
  Declaraciones.haciendoBK = true;
  modSendData.SendData(SendTarget.ToAll, 0, Protocol.PrepareMessagePauseToggle());
  
  modSendData.SendData(SendTarget.ToAll, 0, Protocol.PrepareMessageConsoleMsg("Servidor> Distribuyendo experiencia en parties.", FontTypeNames.FONTTYPE_SERVER));
   for (i = (1); i <= (mdParty.MAX_PARTIES); i++) {
    if (!Declaraciones.Parties[i]Is null ) {
    Declaraciones.Parties[i].FlushExperiencia();
   }
  }
  modSendData.SendData(SendTarget.ToAll, 0, Protocol.PrepareMessageConsoleMsg("Servidor> Experiencia distribuida.", FontTypeNames.FONTTYPE_SERVER));
  modSendData.SendData(SendTarget.ToAll, 0, Protocol.PrepareMessagePauseToggle());
  Declaraciones.haciendoBK = false;
  
 }
 
}

	static void ObtenerExito(int UserIndex, int Exp, int mapa, int X, int Y) {
		/* '*************************************************** */
		/* 'Author: Unknown */
		/* 'Last Modification: - */
		/* ' */
		/* '*************************************************** */

		if (Exp <= 0) {
			if (!mdParty.CASTIGOS) {
				return;
			}
		}

		Declaraciones.Parties[Declaraciones.UserList[UserIndex].PartyIndex].ObtenerExito(Exp, mapa, X, Y);

	}

	static int CantMiembros(int UserIndex) {
		int retval;
		/* '*************************************************** */
		/* 'Author: Unknown */
		/* 'Last Modification: - */
		/* ' */
		/* '*************************************************** */

		retval = 0;
		if (Declaraciones.UserList[UserIndex].PartyIndex > 0) {
			retval = Declaraciones.Parties[Declaraciones.UserList[UserIndex].PartyIndex].retval;
		}

		return retval;
	}

	/* '' */
	/* ' Sets the new p_sumaniveleselevados to the party. */
	/* ' */
	/* ' @param UserInidex Specifies reference to user */
	/*
	 * ' @remarks When a user level up and he is in a party, we call this sub to
	 * don't desestabilice the party exp formula
	 */
	static void ActualizarSumaNivelesElevados(int UserIndex) {
		/* '************************************************* */
		/* 'Author: Marco Vanotti (MarKoxX) */
		/* 'Last modified: 28/10/08 */
		/* ' */
		/* '************************************************* */
		if (Declaraciones.UserList[UserIndex].PartyIndex > 0) {
			Declaraciones.Parties[Declaraciones.UserList[UserIndex].PartyIndex]
					.UpdateSumaNivelesElevados(Declaraciones.UserList[UserIndex].Stats.ELV);
		}
	}

}