

/*  AUTOMATICALLY CONVERTED FILE  */
/* [(0, 'ATTRIBUTE'), (1, 'VB_Name'), (5, '='), (4, '"Acciones"')] */
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

public class Acciones {

	/* '' */
	/*
	 * ' Modulo para manejar las acciones (doble click) de los carteles, foro,
	 * puerta, ramitas
	 */
	/* ' */

	/* '' */
	/* ' Ejecuta la accion del doble click */
	/* ' */
	/* ' @param UserIndex UserIndex */
	/* ' @param Map Numero de mapa */
	/* ' @param X X */
	/* ' @param Y Y */

	static void Accion(int UserIndex, int Map, int X, int Y) {
		/* '*************************************************** */
		/* 'Author: Unknown */
		/* 'Last Modification: - */
		/* ' */
		/* '*************************************************** */

		int tempIndex = 0;

		/* FIXME: ON ERROR RESUME NEXT */
		/* '¿Rango Visión? (ToxicWaste) */
		if ((vb6.Abs(Declaraciones.UserList[UserIndex].Pos.Y - Y) > AI.RANGO_VISION_Y)
				|| (vb6.Abs(Declaraciones.UserList[UserIndex].Pos.X - X) > AI.RANGO_VISION_X)) {
			return;
		}

		/* '¿Posicion valida? */
		if (Extra.InMapBounds(Map, X, Y)) {
			/* 'Acciones NPCs */
			if (Declaraciones.MapData[Map][X][Y].NpcIndex > 0) {
				tempIndex = Declaraciones.MapData[Map][X][Y].NpcIndex;

				/* 'Set the target NPC */
				Declaraciones.UserList[UserIndex].flags.TargetNPC = tempIndex;

				if (Declaraciones.Npclist[tempIndex].Comercia == 1) {
					/* '¿Esta el user muerto? Si es asi no puede comerciar */
					if (Declaraciones.UserList[UserIndex].flags.Muerto == 1) {
						Protocol.WriteConsoleMsg(UserIndex, "¡¡Estás muerto!!", FontTypeNames.FONTTYPE_INFO);
						return;
					}

					/* 'Is it already in commerce mode?? */
					if (Declaraciones.UserList[UserIndex].flags.Comerciando) {
						return;
					}

					if (Matematicas.Distancia(Declaraciones.Npclist[tempIndex].Pos,
							Declaraciones.UserList[UserIndex].Pos) > 3) {
						Protocol.WriteConsoleMsg(UserIndex, "Estás demasiado lejos del vendedor.",
								FontTypeNames.FONTTYPE_INFO);
						return;
					}

					/* 'Iniciamos la rutina pa' comerciar. */
					modSistemaComercio.IniciarComercioNPC(UserIndex);

				} else if (Declaraciones.Npclist[tempIndex].NPCtype == eNPCType.Banquero) {
					/* '¿Esta el user muerto? Si es asi no puede comerciar */
					if (Declaraciones.UserList[UserIndex].flags.Muerto == 1) {
						Protocol.WriteConsoleMsg(UserIndex, "¡¡Estás muerto!!", FontTypeNames.FONTTYPE_INFO);
						return;
					}

					/* 'Is it already in commerce mode?? */
					if (Declaraciones.UserList[UserIndex].flags.Comerciando) {
						return;
					}

					if (Matematicas.Distancia(Declaraciones.Npclist[tempIndex].Pos,
							Declaraciones.UserList[UserIndex].Pos) > 3) {
						Protocol.WriteConsoleMsg(UserIndex, "Estás demasiado lejos del vendedor.",
								FontTypeNames.FONTTYPE_INFO);
						return;
					}

					/* 'A depositar de una */
					modBanco.IniciarDeposito(UserIndex);

				} else if (Declaraciones.Npclist[tempIndex].NPCtype == eNPCType.Revividor
						|| Declaraciones.Npclist[tempIndex].NPCtype == eNPCType.ResucitadorNewbie) {
					if (Matematicas.Distancia(Declaraciones.UserList[UserIndex].Pos,
							Declaraciones.Npclist[tempIndex].Pos) > 10) {
						Protocol.WriteConsoleMsg(UserIndex,
								"El sacerdote no puede curarte debido a que estás demasiado lejos.",
								FontTypeNames.FONTTYPE_INFO);
						return;
					}

					/* 'Revivimos si es necesario */
					if (Declaraciones.UserList[UserIndex].flags.Muerto == 1
							&& (Declaraciones.Npclist[tempIndex].NPCtype == eNPCType.Revividor
									|| Extra.EsNewbie(UserIndex))) {
						UsUaRiOs.RevivirUsuario(UserIndex);
					}

					if (Declaraciones.Npclist[tempIndex].NPCtype == eNPCType.Revividor || Extra.EsNewbie(UserIndex)) {
						/* 'curamos totalmente */
						Declaraciones.UserList[UserIndex].Stats.MinHp = Declaraciones.UserList[UserIndex].Stats.MaxHp;
						Protocol.WriteUpdateUserStats(UserIndex);
					}
				}

				/* '¿Es un obj? */
			} else if (Declaraciones.MapData[Map][X][Y].ObjInfo.ObjIndex > 0) {
				tempIndex = Declaraciones.MapData[Map][X][Y].ObjInfo.ObjIndex;

				Declaraciones.UserList[UserIndex].flags.TargetObj = tempIndex;

				switch (Declaraciones.ObjData[tempIndex].OBJType) {
				/* 'Es una puerta */
				case otPuertas:
					AccionParaPuerta(Map, X, Y, UserIndex);
					/* 'Es un cartel */
					break;

				case otCarteles:
					AccionParaCartel(Map, X, Y, UserIndex);
					/* 'Foro */
					break;

				case otForos:
					AccionParaForo(Map, X, Y, UserIndex);
					/* 'Leña */
					break;

				case otLena:
					if (tempIndex == Declaraciones.FOGATA_APAG && Declaraciones.UserList[UserIndex].flags.Muerto == 0) {
						AccionParaRamita(Map, X, Y, UserIndex);
					}
					break;
				}
				/* '>>>>>>>>>>>OBJETOS QUE OCUPAM MAS DE UN TILE<<<<<<<<<<<<< */
			} else if (Declaraciones.MapData[Map][X + 1][Y].ObjInfo.ObjIndex > 0) {
				tempIndex = Declaraciones.MapData[Map][X + 1][Y].ObjInfo.ObjIndex;
				Declaraciones.UserList[UserIndex].flags.TargetObj = tempIndex;

				switch (Declaraciones.ObjData[tempIndex].OBJType) {

				/* 'Es una puerta */
				case otPuertas:
					AccionParaPuerta(Map, X + 1, Y, UserIndex);

					break;
				}

			} else if (Declaraciones.MapData[Map][X + 1][Y + 1].ObjInfo.ObjIndex > 0) {
				tempIndex = Declaraciones.MapData[Map][X + 1][Y + 1].ObjInfo.ObjIndex;
				Declaraciones.UserList[UserIndex].flags.TargetObj = tempIndex;

				switch (Declaraciones.ObjData[tempIndex].OBJType) {
				/* 'Es una puerta */
				case otPuertas:
					AccionParaPuerta(Map, X + 1, Y + 1, UserIndex);
					break;
				}

			} else if (Declaraciones.MapData[Map][X][Y + 1].ObjInfo.ObjIndex > 0) {
				tempIndex = Declaraciones.MapData[Map][X][Y + 1].ObjInfo.ObjIndex;
				Declaraciones.UserList[UserIndex].flags.TargetObj = tempIndex;

				switch (Declaraciones.ObjData[tempIndex].OBJType) {
				/* 'Es una puerta */
				case otPuertas:
					AccionParaPuerta(Map, X, Y + 1, UserIndex);
					break;
				}
			}
		}
	}

	static void AccionParaForo(int Map, int X, int Y, int UserIndex) {
		/* '*************************************************** */
		/* 'Author: Unknown */
		/* 'Last Modification: 02/01/2010 */
		/* '02/01/2010: ZaMa - Agrego foros faccionarios */
		/* '*************************************************** */

		/* FIXME: ON ERROR RESUME NEXT */

		Declaraciones.WorldPos Pos;

		Pos.Map = Map;
		Pos.X = X;
		Pos.Y = Y;

		if (Matematicas.Distancia(Pos, Declaraciones.UserList[UserIndex].Pos) > 2) {
			Protocol.WriteConsoleMsg(UserIndex, "Estas demasiado lejos.", FontTypeNames.FONTTYPE_INFO);
			return;
		}

		if (modForum.SendPosts(UserIndex,
				Declaraciones.ObjData[Declaraciones.MapData[Map][X][Y].ObjInfo.ObjIndex].ForoID)) {
			Protocol.WriteShowForumForm(UserIndex);
		}

	}

	static void AccionParaPuerta(int Map, int X, int Y, int UserIndex) {
		/* '*************************************************** */
		/* 'Author: Unknown */
		/* 'Last Modification: - */
		/* ' */
		/* '*************************************************** */

		/* FIXME: ON ERROR RESUME NEXT */

		if (!(Matematicas.Distance(Declaraciones.UserList[UserIndex].Pos.X, Declaraciones.UserList[UserIndex].Pos.Y, X,
				Y) > 2)) {
			if (Declaraciones.ObjData[Declaraciones.MapData[Map][X][Y].ObjInfo.ObjIndex].Llave == 0) {
				if (Declaraciones.ObjData[Declaraciones.MapData[Map][X][Y].ObjInfo.ObjIndex].Cerrada == 1) {
					/* 'Abre la puerta */
					if (Declaraciones.ObjData[Declaraciones.MapData[Map][X][Y].ObjInfo.ObjIndex].Llave == 0) {

						Declaraciones.MapData[Map][X][Y].ObjInfo.ObjIndex = Declaraciones.ObjData[Declaraciones.MapData[Map][X][Y].ObjInfo.ObjIndex].IndexAbierta;

						modSendData.SendToAreaByPos(Map, X, Y,
								Protocol.PrepareMessageObjectCreate(
										Declaraciones.ObjData[Declaraciones.MapData[Map][X][Y].ObjInfo.ObjIndex].GrhIndex,
										X, Y));

						/* 'Desbloquea */
						Declaraciones.MapData[Map][X][Y].Blocked = 0;
						Declaraciones.MapData[Map][X - 1][Y].Blocked = 0;

						/* 'Bloquea todos los mapas */
						General.Bloquear(true, Map, X, Y, 0);
						General.Bloquear(true, Map, X - 1, Y, 0);

						/* 'Sonido */
						modSendData.SendData(SendTarget.ToPCArea, UserIndex,
								Protocol.PrepareMessagePlayWave(Declaraciones.SND_PUERTA, X, Y));

					} else {
						Protocol.WriteConsoleMsg(UserIndex, "La puerta esta cerrada con llave.",
								FontTypeNames.FONTTYPE_INFO);
					}
				} else {
					/* 'Cierra puerta */
					Declaraciones.MapData[Map][X][Y].ObjInfo.ObjIndex = Declaraciones.ObjData[Declaraciones.MapData[Map][X][Y].ObjInfo.ObjIndex].IndexCerrada;

					modSendData.SendToAreaByPos(Map, X, Y, Protocol.PrepareMessageObjectCreate(
							Declaraciones.ObjData[Declaraciones.MapData[Map][X][Y].ObjInfo.ObjIndex].GrhIndex, X, Y));

					Declaraciones.MapData[Map][X][Y].Blocked = 1;
					Declaraciones.MapData[Map][X - 1][Y].Blocked = 1;

					General.Bloquear(true, Map, X - 1, Y, 1);
					General.Bloquear(true, Map, X, Y, 1);

					modSendData.SendData(SendTarget.ToPCArea, UserIndex,
							Protocol.PrepareMessagePlayWave(Declaraciones.SND_PUERTA, X, Y));
				}

				Declaraciones.UserList[UserIndex].flags.TargetObj = Declaraciones.MapData[Map][X][Y].ObjInfo.ObjIndex;
			} else {
				Protocol.WriteConsoleMsg(UserIndex, "La puerta está cerrada con llave.", FontTypeNames.FONTTYPE_INFO);
			}
		} else {
			Protocol.WriteConsoleMsg(UserIndex, "Estás demasiado lejos.", FontTypeNames.FONTTYPE_INFO);
		}

	}

	static void AccionParaCartel(int Map, int X, int Y, int UserIndex) {
		/* '*************************************************** */
		/* 'Author: Unknown */
		/* 'Last Modification: - */
		/* ' */
		/* '*************************************************** */

		/* FIXME: ON ERROR RESUME NEXT */

		if (Declaraciones.ObjData[Declaraciones.MapData[Map][X][Y].ObjInfo.ObjIndex].OBJType == 8) {

			if (vb6.Len(Declaraciones.ObjData[Declaraciones.MapData[Map][X][Y].ObjInfo.ObjIndex].texto) > 0) {
				Protocol.WriteShowSignal(UserIndex, Declaraciones.MapData[Map][X][Y].ObjInfo.ObjIndex);
			}

		}

	}

	static void AccionParaRamita(int Map, int X, int Y, int UserIndex) {
 /* '*************************************************** */
 /* 'Author: Unknown */
 /* 'Last Modification: - */
 /* ' */
 /* '*************************************************** */
 
 /* FIXME: ON ERROR RESUME NEXT */
 
 int Suerte = 0;
 int exito = 0;
 Declaraciones.Obj Obj;
 
 int SkillSupervivencia = 0;
 
 Declaraciones.WorldPos Pos;
 Pos.Map = Map;
 Pos.X = X;
 Pos.Y = Y;
 
   if (Matematicas.Distancia(Pos, Declaraciones.UserList[UserIndex].Pos)>2) {
   Protocol.WriteConsoleMsg(UserIndex, "Estás demasiado lejos.", FontTypeNames.FONTTYPE_INFO);
   return;
  }
  
   if (Declaraciones.MapData[Map][X][Y].trigger == eTrigger.ZONASEGURA || Declaraciones.MapInfo[Map].Pk == false) {
   Protocol.WriteConsoleMsg(UserIndex, "No puedes hacer fogatas en zona segura.", FontTypeNames.FONTTYPE_INFO);
   return;
  }
  
  SkillSupervivencia = Declaraciones.UserList[UserIndex].Stats.UserSkills[eSkill.Supervivencia];
  
   if (SkillSupervivencia<6) {
   Suerte = 3;
   
   } else if (SkillSupervivencia<=10) {
   Suerte = 2;
   
   } else {
   Suerte = 1;
  }
  
  exito = Matematicas.RandomNumber(1, Suerte);
  
   if (exito == 1) {
    if (Declaraciones.MapInfo[Declaraciones.UserList[UserIndex].Pos.Map].Zona != Declaraciones.Ciudad) {
    Obj.ObjIndex = Declaraciones.FOGATA;
    Obj.Amount = 1;
    
    Protocol.WriteConsoleMsg(UserIndex, "Has prendido la fogata.", FontTypeNames.FONTTYPE_INFO);
    
    InvUsuario.MakeObj(Obj, Map, X, Y);
    
    /* 'Las fogatas prendidas se deben eliminar */
    cGarbage Fogatita;
    Fogatita = new cGarbage();
    Fogatita.Map = Map;
    Fogatita.X = X;
    Fogatita.Y = Y;
    Declaraciones.TrashCollector.Add[Fogatita];
    
    UsUaRiOs.SubirSkill(UserIndex, eSkill.Supervivencia, true);
    } else {
    Protocol.WriteConsoleMsg(UserIndex, "La ley impide realizar fogatas en las ciudades.", FontTypeNames.FONTTYPE_INFO);
    return;
   }
   } else {
   Protocol.WriteConsoleMsg(UserIndex, "No has podido hacer fuego.", FontTypeNames.FONTTYPE_INFO);
   UsUaRiOs.SubirSkill(UserIndex, eSkill.Supervivencia, false);
  }
  
}

}