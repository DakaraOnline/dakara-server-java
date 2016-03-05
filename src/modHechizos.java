
/*  AUTOMATICALLY CONVERTED FILE  */
/* [(0, 'ATTRIBUTE'), (1, 'VB_Name'), (5, '='), (4, '"modHechizos"')] */
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

public class modHechizos {

	static final int HELEMENTAL_FUEGO = 26;
	static final int HELEMENTAL_TIERRA = 28;

	static void NpcLanzaSpellSobreUser(int NpcIndex, int UserIndex, int Spell) {
		NpcLanzaSpellSobreUser(NpcIndex, UserIndex, Spell, false, false);
	}

	static void NpcLanzaSpellSobreUser(int NpcIndex, int UserIndex, int Spell, boolean DecirPalabras,
			boolean IgnoreVisibilityCheck) {
		/* '*************************************************** */
		/* 'Autor: Unknown (orginal version) */
		/* 'Last Modification: 11/11/2010 */
		/*
		 * '13/02/2009: ZaMa - Los npcs que tiren magias, no podran hacerlo en
		 * mapas donde no se permita usarla.
		 */
		/*
		 * '13/07/2010: ZaMa - Ahora no se contabiliza la muerte de un atacable.
		 */
		/*
		 * '21/09/2010: ZaMa - Amplio los tipos de hechizos que pueden lanzar
		 * los npcs.
		 */
		/*
		 * '21/09/2010: ZaMa - Permito que se ignore el chequeo de visibilidad
		 * (pueden atacar a invis u ocultos).
		 */
		/*
		 * '11/11/2010: ZaMa - No se envian los efectos del hechizo si no lo
		 * castea.
		 */
		/* '*************************************************** */

		if (Declaraciones.Npclist[NpcIndex].CanAttack == 0) {
			return;
		}

		/* ' Doesn't consider if the user is hidden/invisible or not. */
		if (!IgnoreVisibilityCheck) {
			if (Declaraciones.UserList[UserIndex].flags.invisible == 1
					|| Declaraciones.UserList[UserIndex].flags.Oculto == 1) {
				return;
			}
		}

		/* ' Si no se peude usar magia en el mapa, no le deja hacerlo. */
		if (Declaraciones.MapInfo[Declaraciones.UserList[UserIndex].Pos.Map].MagiaSinEfecto > 0) {
			return;
		}

		Declaraciones.Npclist[NpcIndex].CanAttack = 0;

		int dano;
		int AnilloObjIndex;
		AnilloObjIndex = Declaraciones.UserList[UserIndex].Invent.AnilloEqpObjIndex;

		/* ' Heal HP */
		if (Declaraciones.Hechizos[Spell].SubeHP == 1) {

			SendSpellEffects(UserIndex, NpcIndex, Spell, DecirPalabras);

			dano = Matematicas.RandomNumber(Declaraciones.Hechizos[Spell].MinHp, Declaraciones.Hechizos[Spell].MaxHp);

			Declaraciones.UserList[UserIndex].Stats.MinHp = Declaraciones.UserList[UserIndex].Stats.MinHp + dano;
			if (Declaraciones.UserList[UserIndex].Stats.MinHp > Declaraciones.UserList[UserIndex].Stats.MaxHp) {
				Declaraciones.UserList[UserIndex].Stats.MinHp = Declaraciones.UserList[UserIndex].Stats.MaxHp;
			}

			Protocol.WriteConsoleMsg(UserIndex,
					Declaraciones.Npclist[NpcIndex].Name + " te ha quitado " + dano + " puntos de vida.",
					FontTypeNames.FONTTYPE_FIGHT);
			Protocol.WriteUpdateUserStats(UserIndex);

			/* ' Damage */
		} else if (Declaraciones.Hechizos[Spell].SubeHP == 2) {

			if (Declaraciones.UserList[UserIndex].flags.Privilegios && PlayerType.User) {

				SendSpellEffects(UserIndex, NpcIndex, Spell, DecirPalabras);

				dano = Matematicas.RandomNumber(Declaraciones.Hechizos[Spell].MinHp,
						Declaraciones.Hechizos[Spell].MaxHp);

				if (Declaraciones.UserList[UserIndex].Invent.CascoEqpObjIndex > 0) {
					dano = dano - Matematicas.RandomNumber(
							Declaraciones.ObjData[Declaraciones.UserList[UserIndex].Invent.CascoEqpObjIndex].DefensaMagicaMin,
							Declaraciones.ObjData[Declaraciones.UserList[UserIndex].Invent.CascoEqpObjIndex].DefensaMagicaMax);
				}

				if (Declaraciones.UserList[UserIndex].Invent.AnilloEqpObjIndex > 0) {
					dano = dano - Matematicas.RandomNumber(
							Declaraciones.ObjData[Declaraciones.UserList[UserIndex].Invent.AnilloEqpObjIndex].DefensaMagicaMin,
							Declaraciones.ObjData[Declaraciones.UserList[UserIndex].Invent.AnilloEqpObjIndex].DefensaMagicaMax);
				}

				if (dano < 0) {
					dano = 0;
				}

				Declaraciones.UserList[UserIndex].Stats.MinHp = Declaraciones.UserList[UserIndex].Stats.MinHp - dano;

				Protocol.WriteConsoleMsg(UserIndex,
						Declaraciones.Npclist[NpcIndex].Name + " te ha quitado " + dano + " puntos de vida.",
						FontTypeNames.FONTTYPE_FIGHT);
				Protocol.WriteUpdateUserStats(UserIndex);

				/* 'Muere */
				if (Declaraciones.UserList[UserIndex].Stats.MinHp < 1) {
					Declaraciones.UserList[UserIndex].Stats.MinHp = 0;
					if (Declaraciones.Npclist[NpcIndex].NPCtype == eNPCType.GuardiaReal) {
						SistemaCombate.RestarCriminalidad(UserIndex);
					}

					int MasterIndex;
					MasterIndex = Declaraciones.Npclist[NpcIndex].MaestroUser;

					/* '[Barrin 1-12-03] */
					if (MasterIndex > 0) {

						/* ' No son frags los muertos atacables */
						if (Declaraciones.UserList[UserIndex].flags.AtacablePor != MasterIndex) {
							/* 'Store it! */
							Statistics.StoreFrag(MasterIndex, UserIndex);

							UsUaRiOs.ContarMuerte(UserIndex, MasterIndex);
						}

						UsUaRiOs.ActStats(UserIndex, MasterIndex);
					}
					/* '[/Barrin] */

					UsUaRiOs.UserDie(UserIndex);

				}

			}

		}

		/* ' Paralisis/Inmobilize */
		if (Declaraciones.Hechizos[Spell].Paraliza == 1 || Declaraciones.Hechizos[Spell].Inmoviliza == 1) {

			if (Declaraciones.UserList[UserIndex].flags.Paralizado == 0) {

				SendSpellEffects(UserIndex, NpcIndex, Spell, DecirPalabras);

				if (AnilloObjIndex > 0) {
					if (Declaraciones.ObjData[AnilloObjIndex].ImpideParalizar != 0) {
						Protocol.WriteConsoleMsg(UserIndex, "Tu anillo rechaza los efectos de la paralisis.",
								FontTypeNames.FONTTYPE_FIGHT);
						return;
					}
				}

				if (Declaraciones.Hechizos[Spell].Inmoviliza == 1) {

					Declaraciones.UserList[UserIndex].flags.Inmovilizado = 1;

					if (AnilloObjIndex > 0) {
						if (Declaraciones.ObjData[AnilloObjIndex].ImpideInmobilizar != 0) {
							Declaraciones.UserList[UserIndex].flags.Inmovilizado = 0;
							Protocol.WriteConsoleMsg(UserIndex,
									"Tu anillo rechaza los efectos del hechizo inmobilizar.",
									FontTypeNames.FONTTYPE_FIGHT);
						}
					}
				}

				Declaraciones.UserList[UserIndex].flags.Paralizado = 1;
				Declaraciones.UserList[UserIndex].Counters.Paralisis = Admin.IntervaloParalizado;

				Protocol.WriteParalizeOK(UserIndex);

			}

		}

		/* ' Stupidity */
		if (Declaraciones.Hechizos[Spell].Estupidez == 1) {

			if (Declaraciones.UserList[UserIndex].flags.Estupidez == 0) {

				SendSpellEffects(UserIndex, NpcIndex, Spell, DecirPalabras);

				if (AnilloObjIndex > 0) {
					if (Declaraciones.ObjData[AnilloObjIndex].ImpideAturdir != 0) {
						Protocol.WriteConsoleMsg(UserIndex, "Tu anillo rechaza los efectos de la turbación.",
								FontTypeNames.FONTTYPE_FIGHT);
						return;
					}
				}

				Declaraciones.UserList[UserIndex].flags.Estupidez = 1;
				Declaraciones.UserList[UserIndex].Counters.Ceguera = Admin.IntervaloInvisible;

				Protocol.WriteDumb(UserIndex);

			}
		}

		/* ' Blind */
		if (Declaraciones.Hechizos[Spell].Ceguera == 1) {

			if (Declaraciones.UserList[UserIndex].flags.Ceguera == 0) {

				SendSpellEffects(UserIndex, NpcIndex, Spell, DecirPalabras);

				if (AnilloObjIndex > 0) {
					if (Declaraciones.ObjData[AnilloObjIndex].ImpideCegar != 0) {
						Protocol.WriteConsoleMsg(UserIndex, "Tu anillo rechaza los efectos de la ceguera.",
								FontTypeNames.FONTTYPE_FIGHT);
						return;
					}
				}

				Declaraciones.UserList[UserIndex].flags.Ceguera = 1;
				Declaraciones.UserList[UserIndex].Counters.Ceguera = Admin.IntervaloInvisible;

				Protocol.WriteBlind(UserIndex);

			}
		}

		/* ' Remove Invisibility/Hidden */
		if (Declaraciones.Hechizos[Spell].RemueveInvisibilidadParcial == 1) {

			SendSpellEffects(UserIndex, NpcIndex, Spell, DecirPalabras);

			/* 'Sacamos el efecto de ocultarse */
			if (Declaraciones.UserList[UserIndex].flags.Oculto == 1) {
				Declaraciones.UserList[UserIndex].Counters.TiempoOculto = 0;
				Declaraciones.UserList[UserIndex].flags.Oculto = 0;
				UsUaRiOs.SetInvisible(UserIndex, Declaraciones.UserList[UserIndex].Char.CharIndex, false);
				Protocol.WriteConsoleMsg(UserIndex, "¡Has sido detectado!", FontTypeNames.FONTTYPE_VENENO);
			} else {
				/* 'sino, solo lo "iniciamos" en la sacada de invisibilidad. */
				Protocol.WriteConsoleMsg(UserIndex, "Comienzas a hacerte visible.", FontTypeNames.FONTTYPE_VENENO);
				Declaraciones.UserList[UserIndex].Counters.Invisibilidad = Admin.IntervaloInvisible - 1;
			}

		}

	}

	static void SendSpellEffects(int UserIndex, int NpcIndex, int Spell, boolean DecirPalabras) {
		/* '*************************************************** */
		/* 'Author: ZaMa */
		/* 'Last Modification: 11/11/2010 */
		/* 'Sends spell's wav, fx and mgic words to users. */
		/* '*************************************************** */
		/* ' Spell Wav */
		modSendData.SendData(SendTarget.ToPCArea, UserIndex,
				Protocol.PrepareMessagePlayWave(Declaraciones.Hechizos[Spell].WAV,
						Declaraciones.UserList[UserIndex].Pos.X, Declaraciones.UserList[UserIndex].Pos.Y));

		/* ' Spell FX */
		modSendData.SendData(SendTarget.ToPCArea, UserIndex,
				Protocol.PrepareMessageCreateFX(Declaraciones.UserList[UserIndex].Char.CharIndex,
						Declaraciones.Hechizos[Spell].FXgrh, Declaraciones.Hechizos[Spell].loops));

		/* ' Spell Words */
		if (DecirPalabras) {
			modSendData.SendData(SendTarget.ToNPCArea, NpcIndex,
					Protocol.PrepareMessageChatOverHead(Declaraciones.Hechizos[Spell].PalabrasMagicas,
							Declaraciones.Npclist[NpcIndex].Char.CharIndex, vbCyan));
		}
	}

	static void NpcLanzaSpellSobreNpc(int NpcIndex, int TargetNPC, int SpellIndex) {
		NpcLanzaSpellSobreNpc(NpcIndex, TargetNPC, SpellIndex, false);
	}

	static void NpcLanzaSpellSobreNpc(int NpcIndex, int TargetNPC, int SpellIndex, boolean DecirPalabras) {
		/* '*************************************************** */
		/* 'Author: Unknown */
		/* 'Last Modification: 21/09/2010 */
		/* '21/09/2010: ZaMa - Now npcs can cast a wider range of spells. */
		/* '*************************************************** */

		if (Declaraciones.Npclist[NpcIndex].CanAttack == 0) {
			return;
		}
		Declaraciones.Npclist[NpcIndex].CanAttack = 0;

		int Danio;

		/* ' Spell sound and FX */
		modSendData.SendData(SendTarget.ToNPCArea, TargetNPC,
				Protocol.PrepareMessagePlayWave(Declaraciones.Hechizos[SpellIndex].WAV,
						Declaraciones.Npclist[TargetNPC].Pos.X, Declaraciones.Npclist[TargetNPC].Pos.Y));

		modSendData.SendData(SendTarget.ToNPCArea, TargetNPC,
				Protocol.PrepareMessageCreateFX(Declaraciones.Npclist[TargetNPC].Char.CharIndex,
						Declaraciones.Hechizos[SpellIndex].FXgrh, Declaraciones.Hechizos[SpellIndex].loops));

		/* ' Decir las palabras magicas? */
		if (DecirPalabras) {
			modSendData.SendData(SendTarget.ToNPCArea, NpcIndex,
					Protocol.PrepareMessageChatOverHead(Declaraciones.Hechizos[SpellIndex].PalabrasMagicas,
							Declaraciones.Npclist[NpcIndex].Char.CharIndex, vbCyan));
		}

		/* ' Spell deals damage?? */
		if (Declaraciones.Hechizos[SpellIndex].SubeHP == 2) {

			Danio = Matematicas.RandomNumber(Declaraciones.Hechizos[SpellIndex].MinHp,
					Declaraciones.Hechizos[SpellIndex].MaxHp);

			/* ' Deal damage */
			Declaraciones.Npclist[TargetNPC].Stats.MinHp = Declaraciones.Npclist[TargetNPC].Stats.MinHp - Danio;

			/* 'Muere? */
			if (Declaraciones.Npclist[TargetNPC].Stats.MinHp < 1) {
				Declaraciones.Npclist[TargetNPC].Stats.MinHp = 0;
				if (Declaraciones.Npclist[NpcIndex].MaestroUser > 0) {
					NPCs.MuereNpc(TargetNPC, Declaraciones.Npclist[NpcIndex].MaestroUser);
				} else {
					NPCs.MuereNpc(TargetNPC, 0);
				}
			}

			/* ' Spell recovers health?? */
		} else if (Declaraciones.Hechizos[SpellIndex].SubeHP == 1) {

			Danio = Matematicas.RandomNumber(Declaraciones.Hechizos[SpellIndex].MinHp,
					Declaraciones.Hechizos[SpellIndex].MaxHp);

			/* ' Recovers health */
			Declaraciones.Npclist[TargetNPC].Stats.MinHp = Declaraciones.Npclist[TargetNPC].Stats.MinHp + Danio;

			if (Declaraciones.Npclist[TargetNPC].Stats.MinHp > Declaraciones.Npclist[TargetNPC].Stats.MaxHp) {
				Declaraciones.Npclist[TargetNPC].Stats.MinHp = Declaraciones.Npclist[TargetNPC].Stats.MaxHp;
			}

		}

		/* ' Spell Adds/Removes poison? */
		if (Declaraciones.Hechizos[SpellIndex].Envenena == 1) {
			Declaraciones.Npclist[TargetNPC].flags.Envenenado = 1;
		} else if (Declaraciones.Hechizos[SpellIndex].CuraVeneno == 1) {
			Declaraciones.Npclist[TargetNPC].flags.Envenenado = 0;
		}

		/* ' Spells Adds/Removes Paralisis/Inmobility? */
		if (Declaraciones.Hechizos[SpellIndex].Paraliza == 1) {
			Declaraciones.Npclist[TargetNPC].flags.Paralizado = 1;
			Declaraciones.Npclist[TargetNPC].flags.Inmovilizado = 0;
			Declaraciones.Npclist[TargetNPC].Contadores.Paralisis = Admin.IntervaloParalizado;

		} else if (Declaraciones.Hechizos[SpellIndex].Inmoviliza == 1) {
			Declaraciones.Npclist[TargetNPC].flags.Inmovilizado = 1;
			Declaraciones.Npclist[TargetNPC].flags.Paralizado = 0;
			Declaraciones.Npclist[TargetNPC].Contadores.Paralisis = Admin.IntervaloParalizado;

		} else if (Declaraciones.Hechizos[SpellIndex].RemoverParalisis == 1) {
			if (Declaraciones.Npclist[TargetNPC].flags.Paralizado == 1
					|| Declaraciones.Npclist[TargetNPC].flags.Inmovilizado == 1) {
				Declaraciones.Npclist[TargetNPC].flags.Paralizado = 0;
				Declaraciones.Npclist[TargetNPC].flags.Inmovilizado = 0;
				Declaraciones.Npclist[TargetNPC].Contadores.Paralisis = 0;
			}
		}

	}

	static boolean TieneHechizo(int i, int UserIndex) {
		boolean retval;
		/* '*************************************************** */
		/* 'Author: Unknown */
		/* 'Last Modification: - */
		/* ' */
		/* '*************************************************** */

		/* FIXME: ON ERROR GOTO ErrHandler */

		int j;
		for (j = (1); j <= (Declaraciones.MAXUSERHECHIZOS); j++) {
			if (Declaraciones.UserList[UserIndex].Stats.UserHechizos[j] == i) {
				retval = true;
				return retval;
			}
		}

		return retval;
		/* FIXME: ErrHandler : */

		return retval;
	}

	static void AgregarHechizo(int UserIndex, int Slot) {
		/* '*************************************************** */
		/* 'Author: Unknown */
		/* 'Last Modification: - */
		/* ' */
		/* '*************************************************** */

		int hIndex;
		int j;

		hIndex = Declaraciones.ObjData[Declaraciones.UserList[UserIndex].Invent.Object[Slot].ObjIndex].HechizoIndex;

		if (!TieneHechizo(hIndex, UserIndex)) {
			/* 'Buscamos un slot vacio */
			for (j = (1); j <= (Declaraciones.MAXUSERHECHIZOS); j++) {
				if (Declaraciones.UserList[UserIndex].Stats.UserHechizos[j] == 0) {
					break; /* FIXME: EXIT FOR */
				}
			}

			if (Declaraciones.UserList[UserIndex].Stats.UserHechizos[j] != 0) {
				Protocol.WriteConsoleMsg(UserIndex, "No tienes espacio para más hechizos.",
						FontTypeNames.FONTTYPE_INFO);
			} else {
				Declaraciones.UserList[UserIndex].Stats.UserHechizos[j] = hIndex;
				UpdateUserHechizos(false, UserIndex, vb6.CByte(j));
				/* 'Quitamos del inv el item */
				InvUsuario.QuitarUserInvItem(UserIndex, vb6.CByte(Slot), 1);
			}
		} else {
			Protocol.WriteConsoleMsg(UserIndex, "Ya tienes ese hechizo.", FontTypeNames.FONTTYPE_INFO);
		}

	}

	static void DecirPalabrasMagicas(String SpellWords, int UserIndex) {
		/* '*************************************************** */
		/* 'Author: Unknown */
		/* 'Last Modification: 17/11/2009 */
		/*
		 * '25/07/2009: ZaMa - Invisible admins don't say any word when casting
		 * a spell
		 */
		/*
		 * '17/11/2009: ZaMa - Now the user become visible when casting a spell,
		 * if it is hidden
		 */
		/* '*************************************************** */
		/* FIXME: ON ERROR GOTO ErrHandler */

		if (Declaraciones.UserList[UserIndex].flags.AdminInvisible != 1) {
			modSendData.SendData(SendTarget.ToPCArea, UserIndex, Protocol.PrepareMessageChatOverHead(SpellWords,
					Declaraciones.UserList[UserIndex].Char.CharIndex, vbCyan));

			/* ' Si estaba oculto, se vuelve visible */
			if (Declaraciones.UserList[UserIndex].flags.Oculto == 1) {
				Declaraciones.UserList[UserIndex].flags.Oculto = 0;
				Declaraciones.UserList[UserIndex].Counters.TiempoOculto = 0;

				if (Declaraciones.UserList[UserIndex].flags.invisible == 0) {
					Protocol.WriteConsoleMsg(UserIndex, "Has vuelto a ser visible.", FontTypeNames.FONTTYPE_INFO);
					UsUaRiOs.SetInvisible(UserIndex, Declaraciones.UserList[UserIndex].Char.CharIndex, false);
				}
			}
		}

		return;

		/* FIXME: ErrHandler : */
		General.LogError("Error en DecirPalabrasMagicas. Error: " + Err.Number + " - " + Err.description);
	}

	/* '' */
	/* ' Check if an user can cast a certain spell */
	/* ' */
	/* ' @param UserIndex Specifies reference to user */
	/* ' @param HechizoIndex Specifies reference to spell */
	/* ' @return True if the user can cast the spell, otherwise returns false */
	static boolean PuedeLanzar(int UserIndex, int HechizoIndex) {
		boolean retval;
		/* '*************************************************** */
		/* 'Author: Unknown */
		/* 'Last Modification: 12/01/2010 */
		/* 'Last Modification By: ZaMa */
		/*
		 * '06/11/09 - Corregida la bonificación de maná del mimetismo en el
		 * druida con flauta mágica equipada.
		 */
		/* '19/11/2009: ZaMa - Validacion de mana para el Invocar Mascotas */
		/*
		 * '12/01/2010: ZaMa - Validacion de mana para hechizos lanzados por
		 * druida.
		 */
		/* '*************************************************** */
		float DruidManaBonus;

		if (Declaraciones.UserList[UserIndex].flags.Muerto) {
			Protocol.WriteConsoleMsg(UserIndex, "No puedes lanzar hechizos estando muerto.",
					FontTypeNames.FONTTYPE_INFO);
			return retval;
		}

		if (Declaraciones.Hechizos[HechizoIndex].NeedStaff > 0) {
			if (Declaraciones.UserList[UserIndex].clase == eClass.Mage) {
				if (Declaraciones.UserList[UserIndex].Invent.WeaponEqpObjIndex > 0) {
					if (Declaraciones.ObjData[Declaraciones.UserList[UserIndex].Invent.WeaponEqpObjIndex].StaffPower < Declaraciones.Hechizos[HechizoIndex].NeedStaff) {
						Protocol.WriteConsoleMsg(UserIndex,
								"No posees un báculo lo suficientemente poderoso para poder lanzar el conjuro.",
								FontTypeNames.FONTTYPE_INFO);
						return retval;
					}
				} else {
					Protocol.WriteConsoleMsg(UserIndex, "No puedes lanzar este conjuro sin la ayuda de un báculo.",
							FontTypeNames.FONTTYPE_INFO);
					return retval;
				}
			}
		}

		if (Declaraciones.UserList[UserIndex].Stats.UserSkills[eSkill.Magia] < Declaraciones.Hechizos[HechizoIndex].MinSkill) {
			Protocol.WriteConsoleMsg(UserIndex, "No tienes suficientes puntos de magia para lanzar este hechizo.",
					FontTypeNames.FONTTYPE_INFO);
			return retval;
		}

		if (Declaraciones.UserList[UserIndex].Stats.MinSta < Declaraciones.Hechizos[HechizoIndex].StaRequerido) {
			if (Declaraciones.UserList[UserIndex].Genero == eGenero.Hombre) {
				Protocol.WriteConsoleMsg(UserIndex, "Estás muy cansado para lanzar este hechizo.",
						FontTypeNames.FONTTYPE_INFO);
			} else {
				Protocol.WriteConsoleMsg(UserIndex, "Estás muy cansada para lanzar este hechizo.",
						FontTypeNames.FONTTYPE_INFO);
			}
			return retval;
		}

		DruidManaBonus = 1;
		if (Declaraciones.UserList[UserIndex].clase == eClass.Druid) {
			if (Declaraciones.UserList[UserIndex].Invent.AnilloEqpObjIndex == Declaraciones.FLAUTAELFICA) {
				/* ' 50% menos de mana requerido para mimetismo */
				if (Declaraciones.Hechizos[HechizoIndex].Mimetiza == 1) {
					DruidManaBonus = 0.5;

					/* ' 30% menos de mana requerido para invocaciones */
				} else if (Declaraciones.Hechizos[HechizoIndex].Tipo == uInvocacion) {
					DruidManaBonus = 0.7;

					/*
					 * ' 10% menos de mana requerido para las demas magias,
					 * excepto apoca
					 */
				} else if (HechizoIndex != Declaraciones.APOCALIPSIS_SPELL_INDEX) {
					DruidManaBonus = 0.9;
				}
			}

			/*
			 * ' Necesita tener la barra de mana completa para invocar una
			 * mascota
			 */
			if (Declaraciones.Hechizos[HechizoIndex].Warp == 1) {
				if (Declaraciones.UserList[UserIndex].Stats.MinMAN != Declaraciones.UserList[UserIndex].Stats.MaxMAN) {
					Protocol.WriteConsoleMsg(UserIndex, "Debes poseer toda tu maná para poder lanzar este hechizo.",
							FontTypeNames.FONTTYPE_INFO);
					return retval;
					/* ' Si no tiene mascotas, no tiene sentido que lo use */
				} else if (Declaraciones.UserList[UserIndex].NroMascotas == 0) {
					Protocol.WriteConsoleMsg(UserIndex, "Debes poseer alguna mascota para poder lanzar este hechizo.",
							FontTypeNames.FONTTYPE_INFO);
					return retval;
				}
			}
		}

		if (Declaraciones.UserList[UserIndex].Stats.MinMAN < Declaraciones.Hechizos[HechizoIndex].ManaRequerido
				* DruidManaBonus) {
			Protocol.WriteConsoleMsg(UserIndex, "No tienes suficiente maná.", FontTypeNames.FONTTYPE_INFO);
			return retval;
		}

		retval = true;
		return retval;
	}

	static void HechizoTerrenoEstado(int UserIndex, boolean /* FIXME BYREF!! */ b) {
		/* '*************************************************** */
		/* 'Author: Unknown */
		/* 'Last Modification: - */
		/* ' */
		/* '*************************************************** */

		int PosCasteadaX;
		int PosCasteadaY;
		int PosCasteadaM;
		int H;
		int TempX;
		int TempY;

		PosCasteadaX = Declaraciones.UserList[UserIndex].flags.TargetX;
		PosCasteadaY = Declaraciones.UserList[UserIndex].flags.TargetY;
		PosCasteadaM = Declaraciones.UserList[UserIndex].flags.TargetMap;

		H = Declaraciones.UserList[UserIndex].flags.Hechizo;

		if (Declaraciones.Hechizos[H].RemueveInvisibilidadParcial == 1) {
			b = true;
			for (TempX = (PosCasteadaX - 8); TempX <= (PosCasteadaX + 8); TempX++) {
				for (TempY = (PosCasteadaY - 8); TempY <= (PosCasteadaY + 8); TempY++) {
					if (Extra.InMapBounds(PosCasteadaM, TempX, TempY)) {
						if (Declaraciones.MapData[PosCasteadaM][TempX][TempY].UserIndex > 0) {
							/* 'hay un user */
							if (Declaraciones.UserList[Declaraciones.MapData[PosCasteadaM][TempX][TempY].UserIndex].flags.invisible == 1
									&& Declaraciones.UserList[Declaraciones.MapData[PosCasteadaM][TempX][TempY].UserIndex].flags.AdminInvisible == 0) {
								modSendData.SendData(SendTarget.ToPCArea, UserIndex,
										Protocol.PrepareMessageCreateFX(
												Declaraciones.UserList[Declaraciones.MapData[PosCasteadaM][TempX][TempY].UserIndex].Char.CharIndex,
												Declaraciones.Hechizos[H].FXgrh, Declaraciones.Hechizos[H].loops));
							}
						}
					}
				}
			}

			InfoHechizo(UserIndex);
		}
	}

	/* '' */
	/* ' Le da propiedades al nuevo npc */
	/* ' */
	/* ' @param UserIndex Indice del usuario que invoca. */
	/* ' @param b Indica si se termino la operación. */

	static void HechizoInvocacion(int UserIndex, boolean /* FIXME BYREF!! */ HechizoCasteado) {
		/* '*************************************************** */
		/* 'Author: Uknown */
		/* 'Last modification: 18/09/2010 */
		/* 'Sale del sub si no hay una posición valida. */
		/* '18/11/2009: Optimizacion de codigo. */
		/*
		 * '18/09/2010: ZaMa - No se permite invocar en mapas con
		 * InvocarSinEfecto.
		 */
		/* '*************************************************** */

		/* FIXME: ON ERROR GOTO ERROR */

		int mapa;
		mapa = Declaraciones.UserList[UserIndex].Pos.Map;

		/* 'No permitimos se invoquen criaturas en zonas seguras */
		if (Declaraciones.MapInfo[mapa].Pk == false
				|| Declaraciones.MapData[mapa][Declaraciones.UserList[UserIndex].Pos.X][Declaraciones.UserList[UserIndex].Pos.Y].trigger == eTrigger.ZONASEGURA) {
			Protocol.WriteConsoleMsg(UserIndex, "No puedes invocar criaturas en zona segura.",
					FontTypeNames.FONTTYPE_INFO);
			return;
		}

		/*
		 * 'No permitimos se invoquen criaturas en mapas donde esta prohibido
		 * hacerlo
		 */
		if (Declaraciones.MapInfo[mapa].InvocarSinEfecto == 1) {
			Protocol.WriteConsoleMsg(UserIndex,
					"Invocar no está permitido aquí! Retirate de la Zona si deseas utilizar el Hechizo.",
					FontTypeNames.FONTTYPE_INFO);
			return;
		}

		int SpellIndex;
		int NroNpcs;
		int NpcIndex;
		int PetIndex;
		Declaraciones.WorldPos TargetPos;

		TargetPos.Map = Declaraciones.UserList[UserIndex].flags.TargetMap;
		TargetPos.X = Declaraciones.UserList[UserIndex].flags.TargetX;
		TargetPos.Y = Declaraciones.UserList[UserIndex].flags.TargetY;

		SpellIndex = Declaraciones.UserList[UserIndex].flags.Hechizo;

		/* ' Warp de mascotas */
		if (Declaraciones.Hechizos[SpellIndex].Warp == 1) {
			PetIndex = UsUaRiOs.FarthestPet(UserIndex);

			/* ' La invoco cerca mio */
			if (PetIndex > 0) {
				UsUaRiOs.WarpMascota(UserIndex, PetIndex);
			}

			/* ' Invocacion normal */
		} else {
			if (Declaraciones.UserList[UserIndex].NroMascotas >= Declaraciones.MAXMASCOTAS) {
				return;
			}

			for (NroNpcs = (1); NroNpcs <= (Declaraciones.Hechizos[SpellIndex].cant); NroNpcs++) {

				if (Declaraciones.UserList[UserIndex].NroMascotas < Declaraciones.MAXMASCOTAS) {
					NpcIndex = NPCs.SpawnNpc(Declaraciones.Hechizos[SpellIndex].NumNpc, TargetPos, true, false);
					if (NpcIndex > 0) {
						Declaraciones.UserList[UserIndex].NroMascotas = Declaraciones.UserList[UserIndex].NroMascotas
								+ 1;

						PetIndex = Trabajo.FreeMascotaIndex(UserIndex);

						Declaraciones.UserList[UserIndex].MascotasIndex[PetIndex] = NpcIndex;
						Declaraciones.UserList[UserIndex].MascotasType[PetIndex] = Declaraciones.Npclist[NpcIndex].Numero;

						Declaraciones.Npclist[NpcIndex].MaestroUser = UserIndex;
						Declaraciones.Npclist[NpcIndex].Contadores.TiempoExistencia = Admin.IntervaloInvocacion;
						Declaraciones.Npclist[NpcIndex].GiveGLD = 0;

						NPCs.FollowAmo(NpcIndex);
					} else {
						return;
					}
				} else {
					break; /* FIXME: EXIT FOR */
				}

			}
		}

		InfoHechizo(UserIndex);
		HechizoCasteado = true;

		return;

		/* ERROR : */
		General.LogError(
				"[" + Err.Number + "] " + Err.description + " por el usuario " + Declaraciones.UserList[UserIndex].Name
						+ "(" + UserIndex + ") en (" + Declaraciones.UserList[UserIndex].Pos.Map + ", "
						+ Declaraciones.UserList[UserIndex].Pos.X + ", " + Declaraciones.UserList[UserIndex].Pos.Y
						+ "). Tratando de tirar el hechizo " + Declaraciones.Hechizos[SpellIndex].Nombre + "("
						+ SpellIndex + ") en la posicion ( " + Declaraciones.UserList[UserIndex].flags.TargetX + ", "
						+ Declaraciones.UserList[UserIndex].flags.TargetY + ")");

	}

	static void HandleHechizoTerreno(int UserIndex, int SpellIndex) {
		/* '*************************************************** */
		/* 'Author: Unknown */
		/* 'Last Modification: 18/11/2009 */
		/* '18/11/2009: ZaMa - Optimizacion de codigo. */
		/* '*************************************************** */

		boolean HechizoCasteado;
		int ManaRequerida;

		switch (Declaraciones.Hechizos[SpellIndex].Tipo) {
		case uInvocacion:
			HechizoInvocacion(UserIndex, HechizoCasteado);

			break;

		case uEstado:
			HechizoTerrenoEstado(UserIndex, HechizoCasteado);
			break;
		}

		if (HechizoCasteado) {
			UsUaRiOs.SubirSkill(UserIndex, eSkill.Magia, true);

			ManaRequerida = Declaraciones.Hechizos[SpellIndex].ManaRequerido;

			/* ' Invocó una mascota */
			if (Declaraciones.Hechizos[SpellIndex].Warp == 1) {
				/* ' Consume toda la mana */
				ManaRequerida = Declaraciones.UserList[UserIndex].Stats.MinMAN;
			} else {
				/* ' Bonificaciones en hechizos */
				if (Declaraciones.UserList[UserIndex].clase == eClass.Druid) {
					/* ' Solo con flauta equipada */
					if (Declaraciones.UserList[UserIndex].Invent.AnilloEqpObjIndex == Declaraciones.FLAUTAELFICA) {
						/* ' 30% menos de mana para invocaciones */
						ManaRequerida = ManaRequerida * 0.7;
					}
				}
			}

			/* ' Quito la mana requerida */
			Declaraciones.UserList[UserIndex].Stats.MinMAN = Declaraciones.UserList[UserIndex].Stats.MinMAN
					- ManaRequerida;
			if (Declaraciones.UserList[UserIndex].Stats.MinMAN < 0) {
				Declaraciones.UserList[UserIndex].Stats.MinMAN = 0;
			}

			/* ' Quito la estamina requerida */
			Declaraciones.UserList[UserIndex].Stats.MinSta = Declaraciones.UserList[UserIndex].Stats.MinSta
					- Declaraciones.Hechizos[SpellIndex].StaRequerido;
			if (Declaraciones.UserList[UserIndex].Stats.MinSta < 0) {
				Declaraciones.UserList[UserIndex].Stats.MinSta = 0;
			}

			/* ' Update user stats */
			Protocol.WriteUpdateUserStats(UserIndex);
		}

	}

	static void HandleHechizoUsuario(int UserIndex, int SpellIndex) {
		/* '*************************************************** */
		/* 'Author: Unknown */
		/* 'Last Modification: 12/01/2010 */
		/* '18/11/2009: ZaMa - Optimizacion de codigo. */
		/*
		 * '12/01/2010: ZaMa - Optimizacion y agrego bonificaciones al druida.
		 */
		/* '*************************************************** */

		boolean HechizoCasteado;
		int ManaRequerida;

		switch (Declaraciones.Hechizos[SpellIndex].Tipo) {
		case uEstado:
			/* ' Afectan estados (por ejem : Envenenamiento) */
			HechizoEstadoUsuario(UserIndex, HechizoCasteado);

			break;

		case uPropiedades:
			/* ' Afectan HP,MANA,STAMINA,ETC */
			HechizoCasteado = HechizoPropUsuario(UserIndex);
			break;
		}

		if (HechizoCasteado) {
			UsUaRiOs.SubirSkill(UserIndex, eSkill.Magia, true);

			ManaRequerida = Declaraciones.Hechizos[SpellIndex].ManaRequerido;

			/* ' Bonificaciones para druida */
			if (Declaraciones.UserList[UserIndex].clase == eClass.Druid) {
				/* ' Solo con flauta magica */
				if (Declaraciones.UserList[UserIndex].Invent.AnilloEqpObjIndex == Declaraciones.FLAUTAELFICA) {
					if (Declaraciones.Hechizos[SpellIndex].Mimetiza == 1) {
						/* ' 50% menos de mana para mimetismo */
						ManaRequerida = ManaRequerida * 0.5;

					} else if (SpellIndex != Declaraciones.APOCALIPSIS_SPELL_INDEX) {
						/*
						 * ' 10% menos de mana para todo menos apoca y descarga
						 */
						ManaRequerida = ManaRequerida * 0.9;
					}
				}
			}

			/* ' Quito la mana requerida */
			Declaraciones.UserList[UserIndex].Stats.MinMAN = Declaraciones.UserList[UserIndex].Stats.MinMAN
					- ManaRequerida;
			if (Declaraciones.UserList[UserIndex].Stats.MinMAN < 0) {
				Declaraciones.UserList[UserIndex].Stats.MinMAN = 0;
			}

			/* ' Quito la estamina requerida */
			Declaraciones.UserList[UserIndex].Stats.MinSta = Declaraciones.UserList[UserIndex].Stats.MinSta
					- Declaraciones.Hechizos[SpellIndex].StaRequerido;
			if (Declaraciones.UserList[UserIndex].Stats.MinSta < 0) {
				Declaraciones.UserList[UserIndex].Stats.MinSta = 0;
			}

			/* ' Update user stats */
			Protocol.WriteUpdateUserStats(UserIndex);
			Protocol.WriteUpdateUserStats(Declaraciones.UserList[UserIndex].flags.TargetUser);
			Declaraciones.UserList[UserIndex].flags.TargetUser = 0;
		}

	}

	static void HandleHechizoNPC(int UserIndex, int HechizoIndex) {
		/* '*************************************************** */
		/* 'Author: Unknown */
		/* 'Last Modification: 12/01/2010 */
		/*
		 * '13/02/2009: ZaMa - Agregada 50% bonificacion en coste de mana a
		 * mimetismo para druidas
		 */
		/* '17/11/2009: ZaMa - Optimizacion de codigo. */
		/*
		 * '12/01/2010: ZaMa - Bonificacion para druidas de 10% para todos
		 * hechizos excepto apoca y descarga.
		 */
		/*
		 * '12/01/2010: ZaMa - Los druidas mimetizados con npcs ahora son
		 * ignorados.
		 */
		/* '*************************************************** */
		boolean HechizoCasteado;
		int ManaRequerida;

		switch (Declaraciones.Hechizos[HechizoIndex].Tipo) {
		case uEstado:
			/* ' Afectan estados (por ejem : Envenenamiento) */
			HechizoEstadoNPC(Declaraciones.UserList[UserIndex].flags.TargetNPC, HechizoIndex, HechizoCasteado,
					UserIndex);

			break;

		case uPropiedades:
			/* ' Afectan HP,MANA,STAMINA,ETC */
			HechizoPropNPC(HechizoIndex, Declaraciones.UserList[UserIndex].flags.TargetNPC, UserIndex, HechizoCasteado);
			break;
		}

		if (HechizoCasteado) {
			UsUaRiOs.SubirSkill(UserIndex, eSkill.Magia, true);

			ManaRequerida = Declaraciones.Hechizos[HechizoIndex].ManaRequerido;

			/* ' Bonificación para druidas. */
			if (Declaraciones.UserList[UserIndex].clase == eClass.Druid) {
				/* ' Se mostró como usuario, puede ser atacado por npcs */
				Declaraciones.UserList[UserIndex].flags.Ignorado = false;

				/* ' Solo con flauta equipada */
				if (Declaraciones.UserList[UserIndex].Invent.AnilloEqpObjIndex == Declaraciones.FLAUTAELFICA) {
					if (Declaraciones.Hechizos[HechizoIndex].Mimetiza == 1) {
						/* ' 50% menos de mana para mimetismo */
						ManaRequerida = ManaRequerida * 0.5;
						/*
						 * ' Será ignorado hasta que pierda el efecto del
						 * mimetismo o ataque un npc
						 */
						Declaraciones.UserList[UserIndex].flags.Ignorado = true;
					} else {
						/* ' 10% menos de mana para hechizos */
						if (HechizoIndex != Declaraciones.APOCALIPSIS_SPELL_INDEX) {
							ManaRequerida = ManaRequerida * 0.9;
						}
					}
				}
			}

			/* ' Quito la mana requerida */
			Declaraciones.UserList[UserIndex].Stats.MinMAN = Declaraciones.UserList[UserIndex].Stats.MinMAN
					- ManaRequerida;
			if (Declaraciones.UserList[UserIndex].Stats.MinMAN < 0) {
				Declaraciones.UserList[UserIndex].Stats.MinMAN = 0;
			}

			/* ' Quito la estamina requerida */
			Declaraciones.UserList[UserIndex].Stats.MinSta = Declaraciones.UserList[UserIndex].Stats.MinSta
					- Declaraciones.Hechizos[HechizoIndex].StaRequerido;
			if (Declaraciones.UserList[UserIndex].Stats.MinSta < 0) {
				Declaraciones.UserList[UserIndex].Stats.MinSta = 0;
			}

			/* ' Update user stats */
			Protocol.WriteUpdateUserStats(UserIndex);
			Declaraciones.UserList[UserIndex].flags.TargetNPC = 0;
		}
	}

	static void LanzarHechizo(int SpellIndex, int UserIndex) {
		/* '*************************************************** */
		/* 'Autor: Unknown (orginal version) */
		/* 'Last Modification: 02/16/2010 */
		/* '24/01/2007 ZaMa - Optimizacion de codigo. */
		/*
		 * '02/16/2010: Marco - Now .flags.hechizo makes reference to global
		 * spell index instead of user's spell index
		 */
		/* '*************************************************** */
		/* FIXME: ON ERROR GOTO ErrHandler */

		if (Declaraciones.UserList[UserIndex].flags.EnConsulta) {
			Protocol.WriteConsoleMsg(UserIndex, "No puedes lanzar hechizos si estás en consulta.",
					FontTypeNames.FONTTYPE_INFO);
			return;
		}

		if (PuedeLanzar(UserIndex, SpellIndex)) {
			switch (Declaraciones.Hechizos[SpellIndex].Target) {
			case uUsuarios:
				if (Declaraciones.UserList[UserIndex].flags.TargetUser > 0) {
					if (vb6.Abs(Declaraciones.UserList[Declaraciones.UserList[UserIndex].flags.TargetUser].Pos.Y
							- Declaraciones.UserList[UserIndex].Pos.Y) <= AI.RANGO_VISION_Y) {
						HandleHechizoUsuario(UserIndex, SpellIndex);
					} else {
						Protocol.WriteConsoleMsg(UserIndex, "Estás demasiado lejos para lanzar este hechizo.",
								FontTypeNames.FONTTYPE_WARNING);
					}
				} else {
					Protocol.WriteConsoleMsg(UserIndex, "Este hechizo actúa sólo sobre usuarios.",
							FontTypeNames.FONTTYPE_INFO);
				}

				break;

			case uNPC:
				if (Declaraciones.UserList[UserIndex].flags.TargetNPC > 0) {
					if (vb6.Abs(Declaraciones.Npclist[Declaraciones.UserList[UserIndex].flags.TargetNPC].Pos.Y
							- Declaraciones.UserList[UserIndex].Pos.Y) <= AI.RANGO_VISION_Y) {
						HandleHechizoNPC(UserIndex, SpellIndex);
					} else {
						Protocol.WriteConsoleMsg(UserIndex, "Estás demasiado lejos para lanzar este hechizo.",
								FontTypeNames.FONTTYPE_WARNING);
					}
				} else {
					Protocol.WriteConsoleMsg(UserIndex, "Este hechizo sólo afecta a los npcs.",
							FontTypeNames.FONTTYPE_INFO);
				}

				break;

			case uUsuariosYnpc:
				if (Declaraciones.UserList[UserIndex].flags.TargetUser > 0) {
					if (vb6.Abs(Declaraciones.UserList[Declaraciones.UserList[UserIndex].flags.TargetUser].Pos.Y
							- Declaraciones.UserList[UserIndex].Pos.Y) <= AI.RANGO_VISION_Y) {
						HandleHechizoUsuario(UserIndex, SpellIndex);
					} else {
						Protocol.WriteConsoleMsg(UserIndex, "Estás demasiado lejos para lanzar este hechizo.",
								FontTypeNames.FONTTYPE_WARNING);
					}
				} else if (Declaraciones.UserList[UserIndex].flags.TargetNPC > 0) {
					if (vb6.Abs(Declaraciones.Npclist[Declaraciones.UserList[UserIndex].flags.TargetNPC].Pos.Y
							- Declaraciones.UserList[UserIndex].Pos.Y) <= AI.RANGO_VISION_Y) {
						HandleHechizoNPC(UserIndex, SpellIndex);
					} else {
						Protocol.WriteConsoleMsg(UserIndex, "Estás demasiado lejos para lanzar este hechizo.",
								FontTypeNames.FONTTYPE_WARNING);
					}
				} else {
					Protocol.WriteConsoleMsg(UserIndex, "Target inválido.", FontTypeNames.FONTTYPE_INFO);
				}

				break;

			case uTerreno:
				HandleHechizoTerreno(UserIndex, SpellIndex);
				break;
			}

		}

		if (Declaraciones.UserList[UserIndex].Counters.Trabajando) {
			Declaraciones.UserList[UserIndex].Counters.Trabajando = Declaraciones.UserList[UserIndex].Counters.Trabajando
					- 1;
		}

		if (Declaraciones.UserList[UserIndex].Counters.Ocultando) {
			Declaraciones.UserList[UserIndex].Counters.Ocultando = Declaraciones.UserList[UserIndex].Counters.Ocultando
					- 1;
		}

		return;

		/* FIXME: ErrHandler : */
		General.LogError("Error en LanzarHechizo. Error " + Err.Number + " : " + Err.description + " Hechizo: "
				+ Declaraciones.Hechizos[SpellIndex].Nombre + "(" + SpellIndex + "). Casteado por: "
				+ Declaraciones.UserList[UserIndex].Name + "(" + UserIndex + ").");

	}

	static void HechizoEstadoUsuario(int UserIndex, boolean /* FIXME BYREF!! */ HechizoCasteado) {
		/* '*************************************************** */
		/* 'Autor: Unknown (orginal version) */
		/* 'Last Modification: 28/04/2010 */
		/* 'Handles the Spells that afect the Stats of an User */
		/*
		 * '24/01/2007 Pablo (ToxicWaste) - Invisibilidad no permitida en Mapas
		 * con InviSinEfecto
		 */
		/*
		 * '26/01/2007 Pablo (ToxicWaste) - Cambios que permiten mejor manejo de
		 * ataques en los rings.
		 */
		/*
		 * '26/01/2007 Pablo (ToxicWaste) - Revivir no permitido en Mapas con
		 * ResuSinEfecto
		 */
		/*
		 * '02/01/2008 Marcos (ByVal) - Curar Veneno no permitido en usuarios
		 * muertos.
		 */
		/*
		 * '06/28/2008 NicoNZ - Agregué que se le de valor al flag Inmovilizado.
		 */
		/*
		 * '17/11/2008: NicoNZ - Agregado para quitar la penalización de vida en
		 * el ring y cambio de ecuacion.
		 */
		/*
		 * '13/02/2009: ZaMa - Arreglada ecuacion para quitar vida tras
		 * resucitar en rings.
		 */
		/* '23/11/2009: ZaMa - Optimizacion de codigo. */
		/*
		 * '28/04/2010: ZaMa - Agrego Restricciones para ciudas respecto al
		 * estado atacable.
		 */
		/*
		 * '16/09/2010: ZaMa - Solo se hace invi para los clientes si no esta
		 * navegando.
		 */
		/* '*************************************************** */

		int HechizoIndex;
		int TargetIndex;

		HechizoIndex = Declaraciones.UserList[UserIndex].flags.Hechizo;
		TargetIndex = Declaraciones.UserList[UserIndex].flags.TargetUser;

		int AnilloObjIndex;
		AnilloObjIndex = Declaraciones.UserList[TargetIndex].Invent.AnilloEqpObjIndex;

		/* ' <-------- Agrega Invisibilidad ----------> */
		if (Declaraciones.Hechizos[HechizoIndex].Invisibilidad == 1) {
			if (Declaraciones.UserList[TargetIndex].flags.Muerto == 1) {
				Protocol.WriteConsoleMsg(UserIndex, "¡El usuario está muerto!", FontTypeNames.FONTTYPE_INFO);
				HechizoCasteado = false;
				return;
			}

			if (Declaraciones.UserList[TargetIndex].Counters.Saliendo) {
				if (UserIndex != TargetIndex) {
					Protocol.WriteConsoleMsg(UserIndex, "¡El hechizo no tiene efecto!", FontTypeNames.FONTTYPE_INFO);
					HechizoCasteado = false;
					return;
				} else {
					Protocol.WriteConsoleMsg(UserIndex, "¡No puedes hacerte invisible mientras te encuentras saliendo!",
							FontTypeNames.FONTTYPE_WARNING);
					HechizoCasteado = false;
					return;
				}
			}

			/* 'No usar invi mapas InviSinEfecto */
			if (Declaraciones.MapInfo[Declaraciones.UserList[TargetIndex].Pos.Map].InviSinEfecto > 0) {
				Protocol.WriteConsoleMsg(UserIndex, "¡La invisibilidad no funciona aquí!", FontTypeNames.FONTTYPE_INFO);
				HechizoCasteado = false;
				return;
			}

			/* ' Chequea si el status permite ayudar al otro usuario */
			HechizoCasteado = CanSupportUser(UserIndex, TargetIndex, true);
			if (!HechizoCasteado) {
				return;
			}

			/* 'Si sos user, no uses este hechizo con GMS. */
			if (Declaraciones.UserList[UserIndex].flags.Privilegios && PlayerType.User) {
				if (!Declaraciones.UserList[TargetIndex].flags.Privilegios && PlayerType.User) {
					HechizoCasteado = false;
					return;
				}
			}

			Declaraciones.UserList[TargetIndex].flags.invisible = 1;

			/* ' Solo se hace invi para los clientes si no esta navegando */
			if (Declaraciones.UserList[TargetIndex].flags.Navegando == 0) {
				UsUaRiOs.SetInvisible(TargetIndex, Declaraciones.UserList[TargetIndex].Char.CharIndex, true);
			}

			InfoHechizo(UserIndex);
			HechizoCasteado = true;
		}

		/* ' <-------- Agrega Mimetismo ----------> */
		if (Declaraciones.Hechizos[HechizoIndex].Mimetiza == 1) {
			if (Declaraciones.UserList[TargetIndex].flags.Muerto == 1) {
				return;
			}

			if (Declaraciones.UserList[TargetIndex].flags.Navegando == 1) {
				return;
			}
			if (Declaraciones.UserList[UserIndex].flags.Navegando == 1) {
				return;
			}

			/* 'Si sos user, no uses este hechizo con GMS. */
			if (Declaraciones.UserList[UserIndex].flags.Privilegios && PlayerType.User) {
				if (!Declaraciones.UserList[TargetIndex].flags.Privilegios && PlayerType.User) {
					return;
				}
			}

			if (Declaraciones.UserList[UserIndex].flags.Mimetizado == 1) {
				Protocol.WriteConsoleMsg(UserIndex, "Ya te encuentras mimetizado. El hechizo no ha tenido efecto.",
						FontTypeNames.FONTTYPE_INFO);
				return;
			}

			if (Declaraciones.UserList[UserIndex].flags.AdminInvisible == 1) {
				return;
			}

			/* 'copio el char original al mimetizado */

			Declaraciones.UserList[UserIndex].CharMimetizado.body = Declaraciones.UserList[UserIndex].Char.body;
			Declaraciones.UserList[UserIndex].CharMimetizado.Head = Declaraciones.UserList[UserIndex].Char.Head;
			Declaraciones.UserList[UserIndex].CharMimetizado.CascoAnim = Declaraciones.UserList[UserIndex].Char.CascoAnim;
			Declaraciones.UserList[UserIndex].CharMimetizado.ShieldAnim = Declaraciones.UserList[UserIndex].Char.ShieldAnim;
			Declaraciones.UserList[UserIndex].CharMimetizado.WeaponAnim = Declaraciones.UserList[UserIndex].Char.WeaponAnim;

			Declaraciones.UserList[UserIndex].flags.Mimetizado = 1;

			/* 'ahora pongo local el del enemigo */
			Declaraciones.UserList[UserIndex].Char.body = Declaraciones.UserList[TargetIndex].Char.body;
			Declaraciones.UserList[UserIndex].Char.Head = Declaraciones.UserList[TargetIndex].Char.Head;
			Declaraciones.UserList[UserIndex].Char.CascoAnim = Declaraciones.UserList[TargetIndex].Char.CascoAnim;
			Declaraciones.UserList[UserIndex].Char.ShieldAnim = Declaraciones.UserList[TargetIndex].Char.ShieldAnim;
			Declaraciones.UserList[UserIndex].Char.WeaponAnim = Declaraciones.UserList[TargetIndex].Char.WeaponAnim;

			UsUaRiOs.ChangeUserChar(UserIndex, Declaraciones.UserList[UserIndex].Char.body,
					Declaraciones.UserList[UserIndex].Char.Head, Declaraciones.UserList[UserIndex].Char.heading,
					Declaraciones.UserList[UserIndex].Char.WeaponAnim,
					Declaraciones.UserList[UserIndex].Char.ShieldAnim,
					Declaraciones.UserList[UserIndex].Char.CascoAnim);

			InfoHechizo(UserIndex);
			HechizoCasteado = true;
		}

		/* ' <-------- Agrega Envenenamiento ----------> */
		if (Declaraciones.Hechizos[HechizoIndex].Envenena == 1) {
			if (UserIndex == TargetIndex) {
				Protocol.WriteConsoleMsg(UserIndex, "No puedes atacarte a vos mismo.", FontTypeNames.FONTTYPE_FIGHT);
				return;
			}

			if (!SistemaCombate.PuedeAtacar(UserIndex, TargetIndex)) {
				return;
			}
			if (UserIndex != TargetIndex) {
				SistemaCombate.UsuarioAtacadoPorUsuario(UserIndex, TargetIndex);
			}
			Declaraciones.UserList[TargetIndex].flags.Envenenado = 1;
			InfoHechizo(UserIndex);
			HechizoCasteado = true;
		}

		/* ' <-------- Cura Envenenamiento ----------> */
		if (Declaraciones.Hechizos[HechizoIndex].CuraVeneno == 1) {

			/* 'Verificamos que el usuario no este muerto */
			if (Declaraciones.UserList[TargetIndex].flags.Muerto == 1) {
				Protocol.WriteConsoleMsg(UserIndex, "¡El usuario está muerto!", FontTypeNames.FONTTYPE_INFO);
				HechizoCasteado = false;
				return;
			}

			/* ' Chequea si el status permite ayudar al otro usuario */
			HechizoCasteado = CanSupportUser(UserIndex, TargetIndex);
			if (!HechizoCasteado) {
				return;
			}

			/* 'Si sos user, no uses este hechizo con GMS. */
			if (Declaraciones.UserList[UserIndex].flags.Privilegios && PlayerType.User) {
				if (!Declaraciones.UserList[TargetIndex].flags.Privilegios && PlayerType.User) {
					return;
				}
			}

			Declaraciones.UserList[TargetIndex].flags.Envenenado = 0;
			InfoHechizo(UserIndex);
			HechizoCasteado = true;
		}

		/* ' <-------- Agrega Maldicion ----------> */
		if (Declaraciones.Hechizos[HechizoIndex].Maldicion == 1) {
			if (UserIndex == TargetIndex) {
				Protocol.WriteConsoleMsg(UserIndex, "No puedes atacarte a vos mismo.", FontTypeNames.FONTTYPE_FIGHT);
				return;
			}

			if (!SistemaCombate.PuedeAtacar(UserIndex, TargetIndex)) {
				return;
			}
			if (UserIndex != TargetIndex) {
				SistemaCombate.UsuarioAtacadoPorUsuario(UserIndex, TargetIndex);
			}
			Declaraciones.UserList[TargetIndex].flags.Maldicion = 1;
			InfoHechizo(UserIndex);
			HechizoCasteado = true;
		}

		/* ' <-------- Remueve Maldicion ----------> */
		if (Declaraciones.Hechizos[HechizoIndex].RemoverMaldicion == 1) {
			Declaraciones.UserList[TargetIndex].flags.Maldicion = 0;
			InfoHechizo(UserIndex);
			HechizoCasteado = true;
		}

		/* ' <-------- Agrega Bendicion ----------> */
		if (Declaraciones.Hechizos[HechizoIndex].Bendicion == 1) {
			Declaraciones.UserList[TargetIndex].flags.Bendicion = 1;
			InfoHechizo(UserIndex);
			HechizoCasteado = true;
		}

		/* ' <-------- Agrega Paralisis/Inmobilidad ----------> */
		if (Declaraciones.Hechizos[HechizoIndex].Paraliza == 1
				|| Declaraciones.Hechizos[HechizoIndex].Inmoviliza == 1) {
			if (UserIndex == TargetIndex) {
				Protocol.WriteConsoleMsg(UserIndex, "No puedes atacarte a vos mismo.", FontTypeNames.FONTTYPE_FIGHT);
				return;
			}

			if (Declaraciones.UserList[TargetIndex].flags.Paralizado == 0) {
				if (!SistemaCombate.PuedeAtacar(UserIndex, TargetIndex)) {
					return;
				}

				if (UserIndex != TargetIndex) {
					SistemaCombate.UsuarioAtacadoPorUsuario(UserIndex, TargetIndex);
				}

				InfoHechizo(UserIndex);
				HechizoCasteado = true;

				if (AnilloObjIndex > 0) {
					if (Declaraciones.ObjData[AnilloObjIndex].ImpideParalizar != 0) {
						Protocol.WriteConsoleMsg(UserIndex, "Tu anillo rechaza los efectos de la paralisis.",
								FontTypeNames.FONTTYPE_FIGHT);
						Protocol.WriteConsoleMsg(UserIndex, " ¡El hechizo no tiene efecto!",
								FontTypeNames.FONTTYPE_FIGHT);
						Protocol.FlushBuffer(TargetIndex);
						return;
					}
				}

				if (Declaraciones.Hechizos[HechizoIndex].Inmoviliza == 1) {
					Declaraciones.UserList[TargetIndex].flags.Inmovilizado = 1;

					if (AnilloObjIndex > 0) {
						if (Declaraciones.ObjData[AnilloObjIndex].ImpideInmobilizar != 0) {
							Declaraciones.UserList[TargetIndex].flags.Inmovilizado = 0;
							Protocol.WriteConsoleMsg(UserIndex,
									"Tu anillo rechaza los efectos del hechizo inmobilizar.",
									FontTypeNames.FONTTYPE_FIGHT);
							Protocol.WriteConsoleMsg(UserIndex, " ¡El hechizo no tiene efecto!",
									FontTypeNames.FONTTYPE_FIGHT);
						}
					}
				}

				Declaraciones.UserList[TargetIndex].flags.Paralizado = 1;
				Declaraciones.UserList[TargetIndex].Counters.Paralisis = Admin.IntervaloParalizado;

				Declaraciones.UserList[TargetIndex].flags.ParalizedByIndex = UserIndex;
				Declaraciones.UserList[TargetIndex].flags.ParalizedBy = Declaraciones.UserList[UserIndex].Name;

				Protocol.WriteParalizeOK(TargetIndex);
				Protocol.FlushBuffer(TargetIndex);
			}
		}

		/* ' <-------- Remueve Paralisis/Inmobilidad ----------> */
		if (Declaraciones.Hechizos[HechizoIndex].RemoverParalisis == 1) {

			/* ' Remueve si esta en ese estado */
			if (Declaraciones.UserList[TargetIndex].flags.Paralizado == 1) {

				/* ' Chequea si el status permite ayudar al otro usuario */
				HechizoCasteado = CanSupportUser(UserIndex, TargetIndex, true);
				if (!HechizoCasteado) {
					return;
				}

				General.RemoveParalisis(TargetIndex);
				InfoHechizo(UserIndex);

			}
		}

		/* ' <-------- Remueve Estupidez (Aturdimiento) ----------> */
		if (Declaraciones.Hechizos[HechizoIndex].RemoverEstupidez == 1) {

			/* ' Remueve si esta en ese estado */
			if (Declaraciones.UserList[TargetIndex].flags.Estupidez == 1) {

				/* ' Chequea si el status permite ayudar al otro usuario */
				HechizoCasteado = CanSupportUser(UserIndex, TargetIndex);
				if (!HechizoCasteado) {
					return;
				}

				Declaraciones.UserList[TargetIndex].flags.Estupidez = 0;

				/* 'no need to crypt this */
				Protocol.WriteDumbNoMore(TargetIndex);
				Protocol.FlushBuffer(TargetIndex);
				InfoHechizo(UserIndex);

			}
		}

		/* ' <-------- Revive ----------> */
		if (Declaraciones.Hechizos[HechizoIndex].Revivir == 1) {
			if (Declaraciones.UserList[TargetIndex].flags.Muerto == 1) {

				/*
				 * 'Seguro de resurreccion (solo afecta a los hechizos, no al
				 * sacerdote ni al comando de GM)
				 */
				if (Declaraciones.UserList[TargetIndex].flags.SeguroResu) {
					Protocol.WriteConsoleMsg(UserIndex,
							"¡El espíritu no tiene intenciones de regresar al mundo de los vivos!",
							FontTypeNames.FONTTYPE_INFO);
					HechizoCasteado = false;
					return;
				}

				/* 'No usar resu en mapas con ResuSinEfecto */
				if (Declaraciones.MapInfo[Declaraciones.UserList[TargetIndex].Pos.Map].ResuSinEfecto > 0) {
					Protocol.WriteConsoleMsg(UserIndex,
							"¡Revivir no está permitido aquí! Retirate de la Zona si deseas utilizar el Hechizo.",
							FontTypeNames.FONTTYPE_INFO);
					HechizoCasteado = false;
					return;
				}

				/*
				 * 'No podemos resucitar si nuestra barra de energía no está
				 * llena. (GD: 29/04/07)
				 */
				if (Declaraciones.UserList[UserIndex].Stats.MaxSta != Declaraciones.UserList[UserIndex].Stats.MinSta) {
					Protocol.WriteConsoleMsg(UserIndex, "No puedes resucitar si no tienes tu barra de energía llena.",
							FontTypeNames.FONTTYPE_INFO);
					HechizoCasteado = false;
					return;
				}

				/* 'revisamos si necesita vara */
				if (Declaraciones.UserList[UserIndex].clase == eClass.Mage) {
					if (Declaraciones.UserList[UserIndex].Invent.WeaponEqpObjIndex > 0) {
						if (Declaraciones.ObjData[Declaraciones.UserList[UserIndex].Invent.WeaponEqpObjIndex].StaffPower < Declaraciones.Hechizos[HechizoIndex].NeedStaff) {
							Protocol.WriteConsoleMsg(UserIndex, "Necesitas un báculo mejor para lanzar este hechizo.",
									FontTypeNames.FONTTYPE_INFO);
							HechizoCasteado = false;
							return;
						}
					}
				} else if (Declaraciones.UserList[UserIndex].clase == eClass.Bard) {
					if (Declaraciones.UserList[UserIndex].Invent.AnilloEqpObjIndex != Declaraciones.LAUDELFICO
							&& Declaraciones.UserList[UserIndex].Invent.AnilloEqpObjIndex != Declaraciones.LAUDMAGICO) {
						Protocol.WriteConsoleMsg(UserIndex, "Necesitas un instrumento mágico para devolver la vida.",
								FontTypeNames.FONTTYPE_INFO);
						HechizoCasteado = false;
						return;
					}
				} else if (Declaraciones.UserList[UserIndex].clase == eClass.Druid) {
					if (Declaraciones.UserList[UserIndex].Invent.AnilloEqpObjIndex != Declaraciones.FLAUTAELFICA
							&& Declaraciones.UserList[UserIndex].Invent.AnilloEqpObjIndex != Declaraciones.FLAUTAMAGICA) {
						Protocol.WriteConsoleMsg(UserIndex, "Necesitas un instrumento mágico para devolver la vida.",
								FontTypeNames.FONTTYPE_INFO);
						HechizoCasteado = false;
						return;
					}
				}

				/* ' Chequea si el status permite ayudar al otro usuario */
				HechizoCasteado = CanSupportUser(UserIndex, TargetIndex, true);
				if (!HechizoCasteado) {
					return;
				}

				boolean EraCriminal;
				EraCriminal = ES.criminal(UserIndex);

				if (!ES.criminal(TargetIndex)) {
					if (TargetIndex != UserIndex) {
						Declaraciones.UserList[UserIndex].Reputacion.NobleRep = Declaraciones.UserList[UserIndex].Reputacion.NobleRep
								+ 500;
						if (Declaraciones.UserList[UserIndex].Reputacion.NobleRep > Declaraciones.MAXREP) {
							Declaraciones.UserList[UserIndex].Reputacion.NobleRep = Declaraciones.MAXREP;
						}
						Protocol.WriteConsoleMsg(UserIndex, "¡Los Dioses te sonríen, has ganado 500 puntos de nobleza!",
								FontTypeNames.FONTTYPE_INFO);
					}
				}

				if (EraCriminal && !ES.criminal(UserIndex)) {
					UsUaRiOs.RefreshCharStatus(UserIndex);
				}

				/* 'Pablo Toxic Waste (GD: 29/04/07) */
				Declaraciones.UserList[TargetIndex].Stats.MinAGU = 0;
				Declaraciones.UserList[TargetIndex].flags.Sed = 1;
				Declaraciones.UserList[TargetIndex].Stats.MinHam = 0;
				Declaraciones.UserList[TargetIndex].flags.Hambre = 1;
				Protocol.WriteUpdateHungerAndThirst(TargetIndex);
				InfoHechizo(UserIndex);
				Declaraciones.UserList[TargetIndex].Stats.MinMAN = 0;
				Declaraciones.UserList[TargetIndex].Stats.MinSta = 0;

				/*
				 * 'Agregado para quitar la penalización de vida en el ring y
				 * cambio de ecuacion. (NicoNZ)
				 */
				if ((SistemaCombate.TriggerZonaPelea(UserIndex, TargetIndex) != TRIGGER6_PERMITE)) {
					/*
					 * 'Solo saco vida si es User. no quiero que exploten GMs
					 * por ahi.
					 */
					if (Declaraciones.UserList[UserIndex].flags.Privilegios && PlayerType.User) {
						Declaraciones.UserList[UserIndex].Stats.MinHp = Declaraciones.UserList[UserIndex].Stats.MinHp
								* (1 - Declaraciones.UserList[TargetIndex].Stats.ELV * 0.015);
					}
				}

				if ((Declaraciones.UserList[UserIndex].Stats.MinHp <= 0)) {
					UsUaRiOs.UserDie(UserIndex);
					Protocol.WriteConsoleMsg(UserIndex, "El esfuerzo de resucitar fue demasiado grande.",
							FontTypeNames.FONTTYPE_INFO);
					HechizoCasteado = false;
				} else {
					Protocol.WriteConsoleMsg(UserIndex, "El esfuerzo de resucitar te ha debilitado.",
							FontTypeNames.FONTTYPE_INFO);
					HechizoCasteado = true;
				}

				if (Declaraciones.UserList[TargetIndex].flags.Traveling == 1) {
					Declaraciones.UserList[TargetIndex].Counters.goHome = 0;
					Declaraciones.UserList[TargetIndex].flags.Traveling = 0;
					/*
					 * 'Call WriteConsoleMsg(TargetIndex,
					 * "Tu viaje ha sido cancelado.",
					 * FontTypeNames.FONTTYPE_FIGHT)
					 */
					Protocol.WriteMultiMessage(TargetIndex, eMessages.CancelHome);
				}

				UsUaRiOs.RevivirUsuario(TargetIndex);
			} else {
				HechizoCasteado = false;
			}

		}

		/* ' <-------- Agrega Ceguera ----------> */
		if (Declaraciones.Hechizos[HechizoIndex].Ceguera == 1) {
			if (UserIndex == TargetIndex) {
				Protocol.WriteConsoleMsg(UserIndex, "No puedes atacarte a vos mismo.", FontTypeNames.FONTTYPE_FIGHT);
				return;
			}

			if (!SistemaCombate.PuedeAtacar(UserIndex, TargetIndex)) {
				return;
			}
			if (UserIndex != TargetIndex) {
				SistemaCombate.UsuarioAtacadoPorUsuario(UserIndex, TargetIndex);
			}

			InfoHechizo(UserIndex);
			HechizoCasteado = true;

			if (AnilloObjIndex > 0) {
				if (Declaraciones.ObjData[AnilloObjIndex].ImpideCegar != 0) {
					Protocol.WriteConsoleMsg(UserIndex, "Tu anillo rechaza los efectos de la ceguera.",
							FontTypeNames.FONTTYPE_FIGHT);
					Protocol.WriteConsoleMsg(UserIndex, " ¡El hechizo no tiene efecto!", FontTypeNames.FONTTYPE_FIGHT);
					Protocol.FlushBuffer(TargetIndex);
					return;
				}
			}

			Declaraciones.UserList[TargetIndex].flags.Ceguera = 1;
			Declaraciones.UserList[TargetIndex].Counters.Ceguera = Admin.IntervaloParalizado / 3;

			Protocol.WriteBlind(TargetIndex);
			Protocol.FlushBuffer(TargetIndex);

		}

		/* ' <-------- Agrega Estupidez (Aturdimiento) ----------> */
		if (Declaraciones.Hechizos[HechizoIndex].Estupidez == 1) {
			if (UserIndex == TargetIndex) {
				Protocol.WriteConsoleMsg(UserIndex, "No puedes atacarte a vos mismo.", FontTypeNames.FONTTYPE_FIGHT);
				return;
			}

			if (!SistemaCombate.PuedeAtacar(UserIndex, TargetIndex)) {
				return;
			}

			if (UserIndex != TargetIndex) {
				SistemaCombate.UsuarioAtacadoPorUsuario(UserIndex, TargetIndex);
			}

			InfoHechizo(UserIndex);
			HechizoCasteado = true;

			if (AnilloObjIndex > 0) {
				if (Declaraciones.ObjData[AnilloObjIndex].ImpideAturdir != 0) {
					Protocol.WriteConsoleMsg(UserIndex, "Tu anillo rechaza los efectos de la turbación.",
							FontTypeNames.FONTTYPE_FIGHT);
					Protocol.WriteConsoleMsg(UserIndex, " ¡El hechizo no tiene efecto!", FontTypeNames.FONTTYPE_FIGHT);
					Protocol.FlushBuffer(TargetIndex);
					return;
				}
			}

			if (Declaraciones.UserList[TargetIndex].flags.Estupidez == 0) {
				Declaraciones.UserList[TargetIndex].flags.Estupidez = 1;
				Declaraciones.UserList[TargetIndex].Counters.Ceguera = Admin.IntervaloParalizado;
			}

			Protocol.WriteDumb(TargetIndex);
			Protocol.FlushBuffer(TargetIndex);

		}

	}

	static void HechizoEstadoNPC(int NpcIndex, int SpellIndex,
			boolean /* FIXME BYREF!! */ HechizoCasteado, int UserIndex) {
		/* '*************************************************** */
		/* 'Autor: Unknown (orginal version) */
		/* 'Last Modification: 07/07/2008 */
		/* 'Handles the Spells that afect the Stats of an NPC */
		/* '04/13/2008 NicoNZ - Guardias Faccionarios pueden ser */
		/* 'removidos por users de su misma faccion. */
		/*
		 * '07/07/2008: NicoNZ - Solo se puede mimetizar con npcs si es druida
		 */
		/* '*************************************************** */

		if (Declaraciones.Hechizos[SpellIndex].Invisibilidad == 1) {
			InfoHechizo(UserIndex);
			Declaraciones.Npclist[NpcIndex].flags.invisible = 1;
			HechizoCasteado = true;
		}

		if (Declaraciones.Hechizos[SpellIndex].Envenena == 1) {
			if (!SistemaCombate.PuedeAtacarNPC(UserIndex, NpcIndex)) {
				HechizoCasteado = false;
				return;
			}
			UsUaRiOs.NPCAtacado(NpcIndex, UserIndex);
			InfoHechizo(UserIndex);
			Declaraciones.Npclist[NpcIndex].flags.Envenenado = 1;
			HechizoCasteado = true;
		}

		if (Declaraciones.Hechizos[SpellIndex].CuraVeneno == 1) {
			InfoHechizo(UserIndex);
			Declaraciones.Npclist[NpcIndex].flags.Envenenado = 0;
			HechizoCasteado = true;
		}

		if (Declaraciones.Hechizos[SpellIndex].Maldicion == 1) {
			if (!SistemaCombate.PuedeAtacarNPC(UserIndex, NpcIndex)) {
				HechizoCasteado = false;
				return;
			}
			UsUaRiOs.NPCAtacado(NpcIndex, UserIndex);
			InfoHechizo(UserIndex);
			Declaraciones.Npclist[NpcIndex].flags.Maldicion = 1;
			HechizoCasteado = true;
		}

		if (Declaraciones.Hechizos[SpellIndex].RemoverMaldicion == 1) {
			InfoHechizo(UserIndex);
			Declaraciones.Npclist[NpcIndex].flags.Maldicion = 0;
			HechizoCasteado = true;
		}

		if (Declaraciones.Hechizos[SpellIndex].Bendicion == 1) {
			InfoHechizo(UserIndex);
			Declaraciones.Npclist[NpcIndex].flags.Bendicion = 1;
			HechizoCasteado = true;
		}

		if (Declaraciones.Hechizos[SpellIndex].Paraliza == 1) {
			if (Declaraciones.Npclist[NpcIndex].flags.AfectaParalisis == 0) {
				if (!SistemaCombate.PuedeAtacarNPC(UserIndex, NpcIndex, true)) {
					HechizoCasteado = false;
					return;
				}
				UsUaRiOs.NPCAtacado(NpcIndex, UserIndex);
				InfoHechizo(UserIndex);
				Declaraciones.Npclist[NpcIndex].flags.Paralizado = 1;
				Declaraciones.Npclist[NpcIndex].flags.Inmovilizado = 0;
				Declaraciones.Npclist[NpcIndex].Contadores.Paralisis = Admin.IntervaloParalizado;
				HechizoCasteado = true;
			} else {
				Protocol.WriteConsoleMsg(UserIndex, "El NPC es inmune a este hechizo.", FontTypeNames.FONTTYPE_INFO);
				HechizoCasteado = false;
				return;
			}
		}

		if (Declaraciones.Hechizos[SpellIndex].RemoverParalisis == 1) {
			if (Declaraciones.Npclist[NpcIndex].flags.Paralizado == 1
					|| Declaraciones.Npclist[NpcIndex].flags.Inmovilizado == 1) {
				if (Declaraciones.Npclist[NpcIndex].MaestroUser == UserIndex) {
					InfoHechizo(UserIndex);
					Declaraciones.Npclist[NpcIndex].flags.Paralizado = 0;
					Declaraciones.Npclist[NpcIndex].Contadores.Paralisis = 0;
					HechizoCasteado = true;
				} else {
					if (Declaraciones.Npclist[NpcIndex].NPCtype == eNPCType.GuardiaReal) {
						if (Extra.esArmada(UserIndex)) {
							InfoHechizo(UserIndex);
							Declaraciones.Npclist[NpcIndex].flags.Paralizado = 0;
							Declaraciones.Npclist[NpcIndex].Contadores.Paralisis = 0;
							HechizoCasteado = true;
							return;
						} else {
							Protocol.WriteConsoleMsg(UserIndex,
									"Sólo puedes remover la parálisis de los Guardias si perteneces a su facción.",
									FontTypeNames.FONTTYPE_INFO);
							HechizoCasteado = false;
							return;
						}

						Protocol.WriteConsoleMsg(UserIndex,
								"Solo puedes remover la parálisis de los NPCs que te consideren su amo.",
								FontTypeNames.FONTTYPE_INFO);
						HechizoCasteado = false;
						return;
					} else {
						if (Declaraciones.Npclist[NpcIndex].NPCtype == eNPCType.Guardiascaos) {
							if (Extra.esCaos(UserIndex)) {
								InfoHechizo(UserIndex);
								Declaraciones.Npclist[NpcIndex].flags.Paralizado = 0;
								Declaraciones.Npclist[NpcIndex].Contadores.Paralisis = 0;
								HechizoCasteado = true;
								return;
							} else {
								Protocol.WriteConsoleMsg(UserIndex,
										"Solo puedes remover la parálisis de los Guardias si perteneces a su facción.",
										FontTypeNames.FONTTYPE_INFO);
								HechizoCasteado = false;
								return;
							}
						}
					}
				}
			} else {
				Protocol.WriteConsoleMsg(UserIndex, "Este NPC no está paralizado", FontTypeNames.FONTTYPE_INFO);
				HechizoCasteado = false;
				return;
			}
		}

		if (Declaraciones.Hechizos[SpellIndex].Inmoviliza == 1) {
			if (Declaraciones.Npclist[NpcIndex].flags.AfectaParalisis == 0) {
				if (!SistemaCombate.PuedeAtacarNPC(UserIndex, NpcIndex, true)) {
					HechizoCasteado = false;
					return;
				}
				UsUaRiOs.NPCAtacado(NpcIndex, UserIndex);
				Declaraciones.Npclist[NpcIndex].flags.Inmovilizado = 1;
				Declaraciones.Npclist[NpcIndex].flags.Paralizado = 0;
				Declaraciones.Npclist[NpcIndex].Contadores.Paralisis = Admin.IntervaloParalizado;
				InfoHechizo(UserIndex);
				HechizoCasteado = true;
			} else {
				Protocol.WriteConsoleMsg(UserIndex, "El NPC es inmune al hechizo.", FontTypeNames.FONTTYPE_INFO);
			}
		}

		if (Declaraciones.Hechizos[SpellIndex].Mimetiza == 1) {
			if (Declaraciones.UserList[UserIndex].flags.Mimetizado == 1) {
				Protocol.WriteConsoleMsg(UserIndex, "Ya te encuentras mimetizado. El hechizo no ha tenido efecto.",
						FontTypeNames.FONTTYPE_INFO);
				return;
			}

			if (Declaraciones.UserList[UserIndex].flags.AdminInvisible == 1) {
				return;
			}

			if (Declaraciones.UserList[UserIndex].clase == eClass.Druid) {
				/* 'copio el char original al mimetizado */

				Declaraciones.UserList[UserIndex].CharMimetizado.body = Declaraciones.UserList[UserIndex].Char.body;
				Declaraciones.UserList[UserIndex].CharMimetizado.Head = Declaraciones.UserList[UserIndex].Char.Head;
				Declaraciones.UserList[UserIndex].CharMimetizado.CascoAnim = Declaraciones.UserList[UserIndex].Char.CascoAnim;
				Declaraciones.UserList[UserIndex].CharMimetizado.ShieldAnim = Declaraciones.UserList[UserIndex].Char.ShieldAnim;
				Declaraciones.UserList[UserIndex].CharMimetizado.WeaponAnim = Declaraciones.UserList[UserIndex].Char.WeaponAnim;

				Declaraciones.UserList[UserIndex].flags.Mimetizado = 1;

				/* 'ahora pongo lo del NPC. */
				Declaraciones.UserList[UserIndex].Char.body = Declaraciones.Npclist[NpcIndex].Char.body;
				Declaraciones.UserList[UserIndex].Char.Head = Declaraciones.Npclist[NpcIndex].Char.Head;
				Declaraciones.UserList[UserIndex].Char.CascoAnim = Declaraciones.NingunCasco;
				Declaraciones.UserList[UserIndex].Char.ShieldAnim = Declaraciones.NingunEscudo;
				Declaraciones.UserList[UserIndex].Char.WeaponAnim = Declaraciones.NingunArma;

				UsUaRiOs.ChangeUserChar(UserIndex, Declaraciones.UserList[UserIndex].Char.body,
						Declaraciones.UserList[UserIndex].Char.Head, Declaraciones.UserList[UserIndex].Char.heading,
						Declaraciones.UserList[UserIndex].Char.WeaponAnim,
						Declaraciones.UserList[UserIndex].Char.ShieldAnim,
						Declaraciones.UserList[UserIndex].Char.CascoAnim);

			} else {
				Protocol.WriteConsoleMsg(UserIndex, "Sólo los druidas pueden mimetizarse con criaturas.",
						FontTypeNames.FONTTYPE_INFO);
				return;
			}

			InfoHechizo(UserIndex);
			HechizoCasteado = true;
		}

	}

	static void HechizoPropNPC(int SpellIndex, int NpcIndex, int UserIndex,
			boolean /* FIXME BYREF!! */ HechizoCasteado) {
		/* '*************************************************** */
		/* 'Autor: Unknown (orginal version) */
		/* 'Last Modification: 18/09/2010 */
		/* 'Handles the Spells that afect the Life NPC */
		/* '14/08/2007 Pablo (ToxicWaste) - Orden general. */
		/* '18/09/2010: ZaMa - Ahora valida si podes ayudar a un npc. */
		/* '*************************************************** */

		int dano;

		/* 'Salud */
		if (Declaraciones.Hechizos[SpellIndex].SubeHP == 1) {

			HechizoCasteado = CanSupportNpc(UserIndex, NpcIndex);

			if (HechizoCasteado) {
				dano = Matematicas.RandomNumber(Declaraciones.Hechizos[SpellIndex].MinHp,
						Declaraciones.Hechizos[SpellIndex].MaxHp);
				dano = dano + Matematicas.Porcentaje(dano, 3 * Declaraciones.UserList[UserIndex].Stats.ELV);

				InfoHechizo(UserIndex);
				Declaraciones.Npclist[NpcIndex].Stats.MinHp = Declaraciones.Npclist[NpcIndex].Stats.MinHp + dano;
				if (Declaraciones.Npclist[NpcIndex].Stats.MinHp > Declaraciones.Npclist[NpcIndex].Stats.MaxHp) {
					Declaraciones.Npclist[NpcIndex].Stats.MinHp = Declaraciones.Npclist[NpcIndex].Stats.MaxHp;
				}
				Protocol.WriteConsoleMsg(UserIndex, "Has curado " + dano + " puntos de vida a la criatura.",
						FontTypeNames.FONTTYPE_FIGHT);
			}

		} else if (Declaraciones.Hechizos[SpellIndex].SubeHP == 2) {
			if (!SistemaCombate.PuedeAtacarNPC(UserIndex, NpcIndex)) {
				HechizoCasteado = false;
				return;
			}
			UsUaRiOs.NPCAtacado(NpcIndex, UserIndex);
			dano = Matematicas.RandomNumber(Declaraciones.Hechizos[SpellIndex].MinHp,
					Declaraciones.Hechizos[SpellIndex].MaxHp);
			dano = dano + Matematicas.Porcentaje(dano, 3 * Declaraciones.UserList[UserIndex].Stats.ELV);

			if (Declaraciones.Hechizos[SpellIndex].StaffAffected) {
				if (Declaraciones.UserList[UserIndex].clase == eClass.Mage) {
					if (Declaraciones.UserList[UserIndex].Invent.WeaponEqpObjIndex > 0) {
						dano = (dano
								* (Declaraciones.ObjData[Declaraciones.UserList[UserIndex].Invent.WeaponEqpObjIndex].StaffDamageBonus
										+ 70))
								/ 100;
						/* 'Aumenta dano segun el staff- */
						/* 'Dano = (Dano* (70 + BonifBáculo)) / 100 */
					} else {
						/* 'Baja dano a 70% del original */
						dano = dano * 0.7;
					}
				}
			}
			if (Declaraciones.UserList[UserIndex].Invent.AnilloEqpObjIndex == Declaraciones.LAUDELFICO
					|| Declaraciones.UserList[UserIndex].Invent.AnilloEqpObjIndex == Declaraciones.FLAUTAELFICA) {
				/* 'laud magico de los bardos */
				dano = dano * 1.04;
			}

			InfoHechizo(UserIndex);
			HechizoCasteado = true;

			if (Declaraciones.Npclist[NpcIndex].flags.Snd2 > 0) {
				modSendData.SendData(SendTarget.ToNPCArea, NpcIndex,
						Protocol.PrepareMessagePlayWave(Declaraciones.Npclist[NpcIndex].flags.Snd2,
								Declaraciones.Npclist[NpcIndex].Pos.X, Declaraciones.Npclist[NpcIndex].Pos.Y));
			}

			/* 'Quizas tenga defenza magica el NPC. Pablo (ToxicWaste) */
			dano = dano - Declaraciones.Npclist[NpcIndex].Stats.defM;
			if (dano < 0) {
				dano = 0;
			}

			Declaraciones.Npclist[NpcIndex].Stats.MinHp = Declaraciones.Npclist[NpcIndex].Stats.MinHp - dano;
			Protocol.WriteConsoleMsg(UserIndex, "¡Le has quitado " + dano + " puntos de vida a la criatura!",
					FontTypeNames.FONTTYPE_FIGHT);
			SistemaCombate.CalcularDarExp(UserIndex, NpcIndex, dano);

			if (Declaraciones.Npclist[NpcIndex].Stats.MinHp < 1) {
				Declaraciones.Npclist[NpcIndex].Stats.MinHp = 0;
				NPCs.MuereNpc(NpcIndex, UserIndex);
			}
		}

	}

	static void InfoHechizo(int UserIndex) {
		/* '*************************************************** */
		/* 'Autor: Unknown (orginal version) */
		/* 'Last Modification: 25/07/2009 */
		/* '25/07/2009: ZaMa - Code improvements. */
		/*
		 * '25/07/2009: ZaMa - Now invisible admins magic sounds are not sent to
		 * anyone but themselves
		 */
		/* '*************************************************** */
		int SpellIndex;
		int tUser;
		int tNPC;

		SpellIndex = Declaraciones.UserList[UserIndex].flags.Hechizo;
		tUser = Declaraciones.UserList[UserIndex].flags.TargetUser;
		tNPC = Declaraciones.UserList[UserIndex].flags.TargetNPC;

		DecirPalabrasMagicas(Declaraciones.Hechizos[SpellIndex].PalabrasMagicas, UserIndex);

		if (tUser > 0) {
			/* ' Los admins invisibles no producen sonidos ni fx's */
			if (Declaraciones.UserList[UserIndex].flags.AdminInvisible == 1 && UserIndex == tUser) {
				TCP.EnviarDatosASlot(UserIndex,
						Protocol.PrepareMessageCreateFX(Declaraciones.UserList[tUser].Char.CharIndex,
								Declaraciones.Hechizos[SpellIndex].FXgrh, Declaraciones.Hechizos[SpellIndex].loops));
				TCP.EnviarDatosASlot(UserIndex, Protocol.PrepareMessagePlayWave(Declaraciones.Hechizos[SpellIndex].WAV,
						Declaraciones.UserList[tUser].Pos.X, Declaraciones.UserList[tUser].Pos.Y));
			} else {
				modSendData.SendData(SendTarget.ToPCArea, tUser,
						Protocol.PrepareMessageCreateFX(Declaraciones.UserList[tUser].Char.CharIndex,
								Declaraciones.Hechizos[SpellIndex].FXgrh, Declaraciones.Hechizos[SpellIndex].loops));
				/* 'Esta linea faltaba. Pablo (ToxicWaste) */
				modSendData.SendData(SendTarget.ToPCArea, tUser,
						Protocol.PrepareMessagePlayWave(Declaraciones.Hechizos[SpellIndex].WAV,
								Declaraciones.UserList[tUser].Pos.X, Declaraciones.UserList[tUser].Pos.Y));
			}
		} else if (tNPC > 0) {
			modSendData.SendData(SendTarget.ToNPCArea, tNPC,
					Protocol.PrepareMessageCreateFX(Declaraciones.Npclist[tNPC].Char.CharIndex,
							Declaraciones.Hechizos[SpellIndex].FXgrh, Declaraciones.Hechizos[SpellIndex].loops));
			modSendData.SendData(SendTarget.ToNPCArea, tNPC,
					Protocol.PrepareMessagePlayWave(Declaraciones.Hechizos[SpellIndex].WAV,
							Declaraciones.Npclist[tNPC].Pos.X, Declaraciones.Npclist[tNPC].Pos.Y));
		}

		if (tUser > 0) {
			if (UserIndex != tUser) {
				if (Declaraciones.UserList[UserIndex].showName) {
					Protocol.WriteConsoleMsg(UserIndex,
							Declaraciones.Hechizos[SpellIndex].HechizeroMsg + " " + Declaraciones.UserList[tUser].Name,
							FontTypeNames.FONTTYPE_FIGHT);
				} else {
					Protocol.WriteConsoleMsg(UserIndex, Declaraciones.Hechizos[SpellIndex].HechizeroMsg + " alguien.",
							FontTypeNames.FONTTYPE_FIGHT);
				}
				Protocol.WriteConsoleMsg(tUser,
						Declaraciones.UserList[UserIndex].Name + " " + Declaraciones.Hechizos[SpellIndex].TargetMsg,
						FontTypeNames.FONTTYPE_FIGHT);
			} else {
				Protocol.WriteConsoleMsg(UserIndex, Declaraciones.Hechizos[SpellIndex].PropioMsg,
						FontTypeNames.FONTTYPE_FIGHT);
			}
		} else if (tNPC > 0) {
			Protocol.WriteConsoleMsg(UserIndex, Declaraciones.Hechizos[SpellIndex].HechizeroMsg + " " + "la criatura.",
					FontTypeNames.FONTTYPE_FIGHT);
		}

	}

	static boolean HechizoPropUsuario(int UserIndex) {
		boolean retval;
		/* '*************************************************** */
		/* 'Autor: Unknown (orginal version) */
		/* 'Last Modification: 28/04/2010 */
		/*
		 * '02/01/2008 Marcos (ByVal) - No permite tirar curar heridas a
		 * usuarios muertos.
		 */
		/*
		 * '28/04/2010: ZaMa - Agrego Restricciones para ciudas respecto al
		 * estado atacable.
		 */
		/* '*************************************************** */

		int SpellIndex;
		int dano;
		int TargetIndex;

		SpellIndex = Declaraciones.UserList[UserIndex].flags.Hechizo;
		TargetIndex = Declaraciones.UserList[UserIndex].flags.TargetUser;

		if (Declaraciones.UserList[TargetIndex].flags.Muerto) {
			Protocol.WriteConsoleMsg(UserIndex, "No puedes lanzar este hechizo a un muerto.",
					FontTypeNames.FONTTYPE_INFO);
			return retval;
		}

		/* ' <-------- Aumenta Hambre ----------> */
		if (Declaraciones.Hechizos[SpellIndex].SubeHam == 1) {

			InfoHechizo(UserIndex);

			dano = Matematicas.RandomNumber(Declaraciones.Hechizos[SpellIndex].MinHam,
					Declaraciones.Hechizos[SpellIndex].MaxHam);

			Declaraciones.UserList[TargetIndex].Stats.MinHam = Declaraciones.UserList[TargetIndex].Stats.MinHam + dano;
			if (Declaraciones.UserList[TargetIndex].Stats.MinHam > Declaraciones.UserList[TargetIndex].Stats.MaxHam) {
				Declaraciones.UserList[TargetIndex].Stats.MinHam = Declaraciones.UserList[TargetIndex].Stats.MaxHam;
			}

			if (UserIndex != TargetIndex) {
				Protocol.WriteConsoleMsg(UserIndex, "Le has restaurado " + dano + " puntos de hambre a "
						+ Declaraciones.UserList[TargetIndex].Name + ".", FontTypeNames.FONTTYPE_FIGHT);
				Protocol.WriteConsoleMsg(TargetIndex,
						Declaraciones.UserList[UserIndex].Name + " te ha restaurado " + dano + " puntos de hambre.",
						FontTypeNames.FONTTYPE_FIGHT);
			} else {
				Protocol.WriteConsoleMsg(UserIndex, "Te has restaurado " + dano + " puntos de hambre.",
						FontTypeNames.FONTTYPE_FIGHT);
			}

			Protocol.WriteUpdateHungerAndThirst(TargetIndex);

			/* ' <-------- Quita Hambre ----------> */
		} else if (Declaraciones.Hechizos[SpellIndex].SubeHam == 2) {
			if (!SistemaCombate.PuedeAtacar(UserIndex, TargetIndex)) {
				return retval;
			}

			if (UserIndex != TargetIndex) {
				SistemaCombate.UsuarioAtacadoPorUsuario(UserIndex, TargetIndex);
			} else {
				return retval;
			}

			InfoHechizo(UserIndex);

			dano = Matematicas.RandomNumber(Declaraciones.Hechizos[SpellIndex].MinHam,
					Declaraciones.Hechizos[SpellIndex].MaxHam);

			Declaraciones.UserList[TargetIndex].Stats.MinHam = Declaraciones.UserList[TargetIndex].Stats.MinHam - dano;

			if (UserIndex != TargetIndex) {
				Protocol.WriteConsoleMsg(UserIndex, "Le has quitado " + dano + " puntos de hambre a "
						+ Declaraciones.UserList[TargetIndex].Name + ".", FontTypeNames.FONTTYPE_FIGHT);
				Protocol.WriteConsoleMsg(TargetIndex,
						Declaraciones.UserList[UserIndex].Name + " te ha quitado " + dano + " puntos de hambre.",
						FontTypeNames.FONTTYPE_FIGHT);
			} else {
				Protocol.WriteConsoleMsg(UserIndex, "Te has quitado " + dano + " puntos de hambre.",
						FontTypeNames.FONTTYPE_FIGHT);
			}

			if (Declaraciones.UserList[TargetIndex].Stats.MinHam < 1) {
				Declaraciones.UserList[TargetIndex].Stats.MinHam = 0;
				Declaraciones.UserList[TargetIndex].flags.Hambre = 1;
			}

			Protocol.WriteUpdateHungerAndThirst(TargetIndex);
		}

		/* ' <-------- Aumenta Sed ----------> */
		if (Declaraciones.Hechizos[SpellIndex].SubeSed == 1) {

			InfoHechizo(UserIndex);

			dano = Matematicas.RandomNumber(Declaraciones.Hechizos[SpellIndex].MinSed,
					Declaraciones.Hechizos[SpellIndex].MaxSed);

			Declaraciones.UserList[TargetIndex].Stats.MinAGU = Declaraciones.UserList[TargetIndex].Stats.MinAGU + dano;
			if (Declaraciones.UserList[TargetIndex].Stats.MinAGU > Declaraciones.UserList[TargetIndex].Stats.MaxAGU) {
				Declaraciones.UserList[TargetIndex].Stats.MinAGU = Declaraciones.UserList[TargetIndex].Stats.MaxAGU;
			}

			Protocol.WriteUpdateHungerAndThirst(TargetIndex);

			if (UserIndex != TargetIndex) {
				Protocol.WriteConsoleMsg(UserIndex, "Le has restaurado " + dano + " puntos de sed a "
						+ Declaraciones.UserList[TargetIndex].Name + ".", FontTypeNames.FONTTYPE_FIGHT);
				Protocol.WriteConsoleMsg(TargetIndex,
						Declaraciones.UserList[UserIndex].Name + " te ha restaurado " + dano + " puntos de sed.",
						FontTypeNames.FONTTYPE_FIGHT);
			} else {
				Protocol.WriteConsoleMsg(UserIndex, "Te has restaurado " + dano + " puntos de sed.",
						FontTypeNames.FONTTYPE_FIGHT);
			}

			/* ' <-------- Quita Sed ----------> */
		} else if (Declaraciones.Hechizos[SpellIndex].SubeSed == 2) {

			if (!SistemaCombate.PuedeAtacar(UserIndex, TargetIndex)) {
				return retval;
			}

			if (UserIndex != TargetIndex) {
				SistemaCombate.UsuarioAtacadoPorUsuario(UserIndex, TargetIndex);
			}

			InfoHechizo(UserIndex);

			dano = Matematicas.RandomNumber(Declaraciones.Hechizos[SpellIndex].MinSed,
					Declaraciones.Hechizos[SpellIndex].MaxSed);

			Declaraciones.UserList[TargetIndex].Stats.MinAGU = Declaraciones.UserList[TargetIndex].Stats.MinAGU - dano;

			if (UserIndex != TargetIndex) {
				Protocol.WriteConsoleMsg(UserIndex,
						"Le has quitado " + dano + " puntos de sed a " + Declaraciones.UserList[TargetIndex].Name + ".",
						FontTypeNames.FONTTYPE_FIGHT);
				Protocol.WriteConsoleMsg(TargetIndex,
						Declaraciones.UserList[UserIndex].Name + " te ha quitado " + dano + " puntos de sed.",
						FontTypeNames.FONTTYPE_FIGHT);
			} else {
				Protocol.WriteConsoleMsg(UserIndex, "Te has quitado " + dano + " puntos de sed.",
						FontTypeNames.FONTTYPE_FIGHT);
			}

			if (Declaraciones.UserList[TargetIndex].Stats.MinAGU < 1) {
				Declaraciones.UserList[TargetIndex].Stats.MinAGU = 0;
				Declaraciones.UserList[TargetIndex].flags.Sed = 1;
			}

			Protocol.WriteUpdateHungerAndThirst(TargetIndex);

		}

		/* ' <-------- Aumenta Agilidad ----------> */
		if (Declaraciones.Hechizos[SpellIndex].SubeAgilidad == 1) {

			/* ' Chequea si el status permite ayudar al otro usuario */
			if (!CanSupportUser(UserIndex, TargetIndex)) {
				return retval;
			}

			InfoHechizo(UserIndex);
			dano = Matematicas.RandomNumber(Declaraciones.Hechizos[SpellIndex].MinAgilidad,
					Declaraciones.Hechizos[SpellIndex].MaxAgilidad);

			Declaraciones.UserList[TargetIndex].flags.DuracionEfecto = 1200;
			Declaraciones.UserList[TargetIndex].Stats.UserAtributos[eAtributos.Agilidad] = Declaraciones.UserList[TargetIndex].Stats.UserAtributos[eAtributos.Agilidad]
					+ dano;
			if (Declaraciones.UserList[TargetIndex].Stats.UserAtributos[eAtributos.Agilidad] > SistemaCombate.MinimoInt(
					Declaraciones.MAXATRIBUTOS,
					Declaraciones.UserList[TargetIndex].Stats.UserAtributosBackUP[Agilidad] * 2)) {
				Declaraciones.UserList[TargetIndex].Stats.UserAtributos[eAtributos.Agilidad] = SistemaCombate.MinimoInt(
						Declaraciones.MAXATRIBUTOS,
						Declaraciones.UserList[TargetIndex].Stats.UserAtributosBackUP[Agilidad] * 2);
			}

			Declaraciones.UserList[TargetIndex].flags.TomoPocion = true;
			Protocol.WriteUpdateDexterity(TargetIndex);

			/* ' <-------- Quita Agilidad ----------> */
		} else if (Declaraciones.Hechizos[SpellIndex].SubeAgilidad == 2) {

			if (!SistemaCombate.PuedeAtacar(UserIndex, TargetIndex)) {
				return retval;
			}

			if (UserIndex != TargetIndex) {
				SistemaCombate.UsuarioAtacadoPorUsuario(UserIndex, TargetIndex);
			}

			InfoHechizo(UserIndex);

			Declaraciones.UserList[TargetIndex].flags.TomoPocion = true;
			dano = Matematicas.RandomNumber(Declaraciones.Hechizos[SpellIndex].MinAgilidad,
					Declaraciones.Hechizos[SpellIndex].MaxAgilidad);
			Declaraciones.UserList[TargetIndex].flags.DuracionEfecto = 700;
			Declaraciones.UserList[TargetIndex].Stats.UserAtributos[eAtributos.Agilidad] = Declaraciones.UserList[TargetIndex].Stats.UserAtributos[eAtributos.Agilidad]
					- dano;
			if (Declaraciones.UserList[TargetIndex].Stats.UserAtributos[eAtributos.Agilidad] < Declaraciones.MINATRIBUTOS) {
				Declaraciones.UserList[TargetIndex].Stats.UserAtributos[eAtributos.Agilidad] = Declaraciones.MINATRIBUTOS;
			}

			Protocol.WriteUpdateDexterity(TargetIndex);
		}

		/* ' <-------- Aumenta Fuerza ----------> */
		if (Declaraciones.Hechizos[SpellIndex].SubeFuerza == 1) {

			/* ' Chequea si el status permite ayudar al otro usuario */
			if (!CanSupportUser(UserIndex, TargetIndex)) {
				return retval;
			}

			InfoHechizo(UserIndex);
			dano = Matematicas.RandomNumber(Declaraciones.Hechizos[SpellIndex].MinFuerza,
					Declaraciones.Hechizos[SpellIndex].MaxFuerza);

			Declaraciones.UserList[TargetIndex].flags.DuracionEfecto = 1200;

			Declaraciones.UserList[TargetIndex].Stats.UserAtributos[eAtributos.Fuerza] = Declaraciones.UserList[TargetIndex].Stats.UserAtributos[eAtributos.Fuerza]
					+ dano;
			if (Declaraciones.UserList[TargetIndex].Stats.UserAtributos[eAtributos.Fuerza] > SistemaCombate.MinimoInt(
					Declaraciones.MAXATRIBUTOS,
					Declaraciones.UserList[TargetIndex].Stats.UserAtributosBackUP[Fuerza] * 2)) {
				Declaraciones.UserList[TargetIndex].Stats.UserAtributos[eAtributos.Fuerza] = SistemaCombate.MinimoInt(
						Declaraciones.MAXATRIBUTOS,
						Declaraciones.UserList[TargetIndex].Stats.UserAtributosBackUP[Fuerza] * 2);
			}

			Declaraciones.UserList[TargetIndex].flags.TomoPocion = true;
			Protocol.WriteUpdateStrenght(TargetIndex);

			/* ' <-------- Quita Fuerza ----------> */
		} else if (Declaraciones.Hechizos[SpellIndex].SubeFuerza == 2) {

			if (!SistemaCombate.PuedeAtacar(UserIndex, TargetIndex)) {
				return retval;
			}

			if (UserIndex != TargetIndex) {
				SistemaCombate.UsuarioAtacadoPorUsuario(UserIndex, TargetIndex);
			}

			InfoHechizo(UserIndex);

			Declaraciones.UserList[TargetIndex].flags.TomoPocion = true;

			dano = Matematicas.RandomNumber(Declaraciones.Hechizos[SpellIndex].MinFuerza,
					Declaraciones.Hechizos[SpellIndex].MaxFuerza);
			Declaraciones.UserList[TargetIndex].flags.DuracionEfecto = 700;
			Declaraciones.UserList[TargetIndex].Stats.UserAtributos[eAtributos.Fuerza] = Declaraciones.UserList[TargetIndex].Stats.UserAtributos[eAtributos.Fuerza]
					- dano;
			if (Declaraciones.UserList[TargetIndex].Stats.UserAtributos[eAtributos.Fuerza] < Declaraciones.MINATRIBUTOS) {
				Declaraciones.UserList[TargetIndex].Stats.UserAtributos[eAtributos.Fuerza] = Declaraciones.MINATRIBUTOS;
			}

			Protocol.WriteUpdateStrenght(TargetIndex);
		}

		/* ' <-------- Cura salud ----------> */
		if (Declaraciones.Hechizos[SpellIndex].SubeHP == 1) {

			/* 'Verifica que el usuario no este muerto */
			if (Declaraciones.UserList[TargetIndex].flags.Muerto == 1) {
				Protocol.WriteConsoleMsg(UserIndex, "¡El usuario está muerto!", FontTypeNames.FONTTYPE_INFO);
				return retval;
			}

			/* ' Chequea si el status permite ayudar al otro usuario */
			if (!CanSupportUser(UserIndex, TargetIndex)) {
				return retval;
			}

			dano = Matematicas.RandomNumber(Declaraciones.Hechizos[SpellIndex].MinHp,
					Declaraciones.Hechizos[SpellIndex].MaxHp);
			dano = dano + Matematicas.Porcentaje(dano, 3 * Declaraciones.UserList[UserIndex].Stats.ELV);

			InfoHechizo(UserIndex);

			Declaraciones.UserList[TargetIndex].Stats.MinHp = Declaraciones.UserList[TargetIndex].Stats.MinHp + dano;
			if (Declaraciones.UserList[TargetIndex].Stats.MinHp > Declaraciones.UserList[TargetIndex].Stats.MaxHp) {
				Declaraciones.UserList[TargetIndex].Stats.MinHp = Declaraciones.UserList[TargetIndex].Stats.MaxHp;
			}

			Protocol.WriteUpdateHP(TargetIndex);

			if (UserIndex != TargetIndex) {
				Protocol.WriteConsoleMsg(UserIndex, "Le has restaurado " + dano + " puntos de vida a "
						+ Declaraciones.UserList[TargetIndex].Name + ".", FontTypeNames.FONTTYPE_FIGHT);
				Protocol.WriteConsoleMsg(TargetIndex,
						Declaraciones.UserList[UserIndex].Name + " te ha restaurado " + dano + " puntos de vida.",
						FontTypeNames.FONTTYPE_FIGHT);
			} else {
				Protocol.WriteConsoleMsg(UserIndex, "Te has restaurado " + dano + " puntos de vida.",
						FontTypeNames.FONTTYPE_FIGHT);
			}

			/* ' <-------- Quita salud (Dana) ----------> */
		} else if (Declaraciones.Hechizos[SpellIndex].SubeHP == 2) {

			if (UserIndex == TargetIndex) {
				Protocol.WriteConsoleMsg(UserIndex, "No puedes atacarte a vos mismo.", FontTypeNames.FONTTYPE_FIGHT);
				return retval;
			}

			dano = Matematicas.RandomNumber(Declaraciones.Hechizos[SpellIndex].MinHp,
					Declaraciones.Hechizos[SpellIndex].MaxHp);

			dano = dano + Matematicas.Porcentaje(dano, 3 * Declaraciones.UserList[UserIndex].Stats.ELV);

			if (Declaraciones.Hechizos[SpellIndex].StaffAffected) {
				if (Declaraciones.UserList[UserIndex].clase == eClass.Mage) {
					if (Declaraciones.UserList[UserIndex].Invent.WeaponEqpObjIndex > 0) {
						dano = (dano
								* (Declaraciones.ObjData[Declaraciones.UserList[UserIndex].Invent.WeaponEqpObjIndex].StaffDamageBonus
										+ 70))
								/ 100;
					} else {
						/* 'Baja dano a 70% del original */
						dano = dano * 0.7;
					}
				}
			}

			if (Declaraciones.UserList[UserIndex].Invent.AnilloEqpObjIndex == Declaraciones.LAUDELFICO
					|| Declaraciones.UserList[UserIndex].Invent.AnilloEqpObjIndex == Declaraciones.FLAUTAELFICA) {
				/* 'laud magico de los bardos */
				dano = dano * 1.04;
			}

			/* 'cascos antimagia */
			if ((Declaraciones.UserList[TargetIndex].Invent.CascoEqpObjIndex > 0)) {
				dano = dano - Matematicas.RandomNumber(
						Declaraciones.ObjData[Declaraciones.UserList[TargetIndex].Invent.CascoEqpObjIndex].DefensaMagicaMin,
						Declaraciones.ObjData[Declaraciones.UserList[TargetIndex].Invent.CascoEqpObjIndex].DefensaMagicaMax);
			}

			/* 'anillos */
			if ((Declaraciones.UserList[TargetIndex].Invent.AnilloEqpObjIndex > 0)) {
				dano = dano - Matematicas.RandomNumber(
						Declaraciones.ObjData[Declaraciones.UserList[TargetIndex].Invent.AnilloEqpObjIndex].DefensaMagicaMin,
						Declaraciones.ObjData[Declaraciones.UserList[TargetIndex].Invent.AnilloEqpObjIndex].DefensaMagicaMax);
			}

			if (dano < 0) {
				dano = 0;
			}

			if (!SistemaCombate.PuedeAtacar(UserIndex, TargetIndex)) {
				return retval;
			}

			if (UserIndex != TargetIndex) {
				SistemaCombate.UsuarioAtacadoPorUsuario(UserIndex, TargetIndex);
			}

			InfoHechizo(UserIndex);

			Declaraciones.UserList[TargetIndex].Stats.MinHp = Declaraciones.UserList[TargetIndex].Stats.MinHp - dano;

			Protocol.WriteUpdateHP(TargetIndex);

			Protocol.WriteConsoleMsg(UserIndex,
					"Le has quitado " + dano + " puntos de vida a " + Declaraciones.UserList[TargetIndex].Name + ".",
					FontTypeNames.FONTTYPE_FIGHT);
			Protocol.WriteConsoleMsg(TargetIndex,
					Declaraciones.UserList[UserIndex].Name + " te ha quitado " + dano + " puntos de vida.",
					FontTypeNames.FONTTYPE_FIGHT);

			/* 'Muere */
			if (Declaraciones.UserList[TargetIndex].Stats.MinHp < 1) {

				if (Declaraciones.UserList[TargetIndex].flags.AtacablePor != UserIndex) {
					/* 'Store it! */
					Statistics.StoreFrag(UserIndex, TargetIndex);
					UsUaRiOs.ContarMuerte(TargetIndex, UserIndex);
				}

				Declaraciones.UserList[TargetIndex].Stats.MinHp = 0;
				UsUaRiOs.ActStats(TargetIndex, UserIndex);
				UsUaRiOs.UserDie(TargetIndex);
			}

		}

		/* ' <-------- Aumenta Mana ----------> */
		if (Declaraciones.Hechizos[SpellIndex].SubeMana == 1) {

			InfoHechizo(UserIndex);
			Declaraciones.UserList[TargetIndex].Stats.MinMAN = Declaraciones.UserList[TargetIndex].Stats.MinMAN + dano;
			if (Declaraciones.UserList[TargetIndex].Stats.MinMAN > Declaraciones.UserList[TargetIndex].Stats.MaxMAN) {
				Declaraciones.UserList[TargetIndex].Stats.MinMAN = Declaraciones.UserList[TargetIndex].Stats.MaxMAN;
			}

			Protocol.WriteUpdateMana(TargetIndex);

			if (UserIndex != TargetIndex) {
				Protocol.WriteConsoleMsg(UserIndex, "Le has restaurado " + dano + " puntos de maná a "
						+ Declaraciones.UserList[TargetIndex].Name + ".", FontTypeNames.FONTTYPE_FIGHT);
				Protocol.WriteConsoleMsg(TargetIndex,
						Declaraciones.UserList[UserIndex].Name + " te ha restaurado " + dano + " puntos de maná.",
						FontTypeNames.FONTTYPE_FIGHT);
			} else {
				Protocol.WriteConsoleMsg(UserIndex, "Te has restaurado " + dano + " puntos de maná.",
						FontTypeNames.FONTTYPE_FIGHT);
			}

			/* ' <-------- Quita Mana ----------> */
		} else if (Declaraciones.Hechizos[SpellIndex].SubeMana == 2) {
			if (!SistemaCombate.PuedeAtacar(UserIndex, TargetIndex)) {
				return retval;
			}

			if (UserIndex != TargetIndex) {
				SistemaCombate.UsuarioAtacadoPorUsuario(UserIndex, TargetIndex);
			}

			InfoHechizo(UserIndex);

			if (UserIndex != TargetIndex) {
				Protocol.WriteConsoleMsg(UserIndex, "Le has quitado " + dano + " puntos de maná a "
						+ Declaraciones.UserList[TargetIndex].Name + ".", FontTypeNames.FONTTYPE_FIGHT);
				Protocol.WriteConsoleMsg(TargetIndex,
						Declaraciones.UserList[UserIndex].Name + " te ha quitado " + dano + " puntos de maná.",
						FontTypeNames.FONTTYPE_FIGHT);
			} else {
				Protocol.WriteConsoleMsg(UserIndex, "Te has quitado " + dano + " puntos de maná.",
						FontTypeNames.FONTTYPE_FIGHT);
			}

			Declaraciones.UserList[TargetIndex].Stats.MinMAN = Declaraciones.UserList[TargetIndex].Stats.MinMAN - dano;
			if (Declaraciones.UserList[TargetIndex].Stats.MinMAN < 1) {
				Declaraciones.UserList[TargetIndex].Stats.MinMAN = 0;
			}

			Protocol.WriteUpdateMana(TargetIndex);

		}

		/* ' <-------- Aumenta Stamina ----------> */
		if (Declaraciones.Hechizos[SpellIndex].SubeSta == 1) {
			InfoHechizo(UserIndex);
			Declaraciones.UserList[TargetIndex].Stats.MinSta = Declaraciones.UserList[TargetIndex].Stats.MinSta + dano;
			if (Declaraciones.UserList[TargetIndex].Stats.MinSta > Declaraciones.UserList[TargetIndex].Stats.MaxSta) {
				Declaraciones.UserList[TargetIndex].Stats.MinSta = Declaraciones.UserList[TargetIndex].Stats.MaxSta;
			}

			Protocol.WriteUpdateSta(TargetIndex);

			if (UserIndex != TargetIndex) {
				Protocol.WriteConsoleMsg(UserIndex, "Le has restaurado " + dano + " puntos de energía a "
						+ Declaraciones.UserList[TargetIndex].Name + ".", FontTypeNames.FONTTYPE_FIGHT);
				Protocol.WriteConsoleMsg(TargetIndex,
						Declaraciones.UserList[UserIndex].Name + " te ha restaurado " + dano + " puntos de energía.",
						FontTypeNames.FONTTYPE_FIGHT);
			} else {
				Protocol.WriteConsoleMsg(UserIndex, "Te has restaurado " + dano + " puntos de energía.",
						FontTypeNames.FONTTYPE_FIGHT);
			}

			/* ' <-------- Quita Stamina ----------> */
		} else if (Declaraciones.Hechizos[SpellIndex].SubeSta == 2) {
			if (!SistemaCombate.PuedeAtacar(UserIndex, TargetIndex)) {
				return retval;
			}

			if (UserIndex != TargetIndex) {
				SistemaCombate.UsuarioAtacadoPorUsuario(UserIndex, TargetIndex);
			}

			InfoHechizo(UserIndex);

			if (UserIndex != TargetIndex) {
				Protocol.WriteConsoleMsg(UserIndex, "Le has quitado " + dano + " puntos de energía a "
						+ Declaraciones.UserList[TargetIndex].Name + ".", FontTypeNames.FONTTYPE_FIGHT);
				Protocol.WriteConsoleMsg(TargetIndex,
						Declaraciones.UserList[UserIndex].Name + " te ha quitado " + dano + " puntos de energía.",
						FontTypeNames.FONTTYPE_FIGHT);
			} else {
				Protocol.WriteConsoleMsg(UserIndex, "Te has quitado " + dano + " puntos de energía.",
						FontTypeNames.FONTTYPE_FIGHT);
			}

			Declaraciones.UserList[TargetIndex].Stats.MinSta = Declaraciones.UserList[TargetIndex].Stats.MinSta - dano;

			if (Declaraciones.UserList[TargetIndex].Stats.MinSta < 1) {
				Declaraciones.UserList[TargetIndex].Stats.MinSta = 0;
			}

			Protocol.WriteUpdateSta(TargetIndex);

		}

		retval = true;

		Protocol.FlushBuffer(TargetIndex);

		return retval;
	}

	static boolean CanSupportUser(int CasterIndex, int TargetIndex) {
		return CanSupportUser(CasterIndex, TargetIndex, false);
	}

	static boolean CanSupportUser(int CasterIndex, int TargetIndex, boolean DoCriminal) {
		boolean retval;
		/* '*************************************************** */
		/* 'Author: ZaMa */
		/* 'Last Modification: 28/04/2010 */
		/* 'Checks if caster can cast support magic on target user. */
		/* '*************************************************** */

		/* FIXME: ON ERROR GOTO ErrHandler */

		/* ' Te podes curar a vos mismo */
		if (CasterIndex == TargetIndex) {
			retval = true;
			return retval;
		}

		/* ' No podes ayudar si estas en consulta */
		if (Declaraciones.UserList[CasterIndex].flags.EnConsulta) {
			Protocol.WriteConsoleMsg(CasterIndex, "No puedes ayudar usuarios mientras estas en consulta.",
					FontTypeNames.FONTTYPE_INFO);
			return retval;
		}

		/* ' Si estas en la arena, esta todo permitido */
		if (SistemaCombate.TriggerZonaPelea(CasterIndex, TargetIndex) == TRIGGER6_PERMITE) {
			retval = true;
			return retval;
		}

		/* ' Victima criminal? */
		if (ES.criminal(TargetIndex)) {

			/* ' Casteador Ciuda? */
			if (!ES.criminal(CasterIndex)) {

				/* ' Armadas no pueden ayudar */
				if (Extra.esArmada(CasterIndex)) {
					Protocol.WriteConsoleMsg(CasterIndex,
							"Los miembros del ejército real no pueden ayudar a los criminales.",
							FontTypeNames.FONTTYPE_INFO);
					return retval;
				}

				/* ' Si el ciuda tiene el seguro puesto no puede ayudar */
				if (Declaraciones.UserList[CasterIndex].flags.Seguro) {
					Protocol.WriteConsoleMsg(CasterIndex,
							"Para ayudar criminales debes sacarte el seguro ya que te volverás criminal como ellos.",
							FontTypeNames.FONTTYPE_INFO);
					return retval;
				} else {
					/* ' Penalizacion */
					if (DoCriminal) {
						UsUaRiOs.VolverCriminal(CasterIndex);
					} else {
						DisNobAuBan(CasterIndex, Declaraciones.UserList[CasterIndex].Reputacion.NobleRep * 0.5, 10000);
					}
				}
			}

			/* ' Victima ciuda o army */
		} else {
			/* ' Casteador es caos? => No Pueden ayudar ciudas */
			if (Extra.esCaos(CasterIndex)) {
				Protocol.WriteConsoleMsg(CasterIndex,
						"Los miembros de la legión oscura no pueden ayudar a los ciudadanos.",
						FontTypeNames.FONTTYPE_INFO);
				return retval;

				/* ' Casteador ciuda/army? */
			} else if (!ES.criminal(CasterIndex)) {

				/* ' Esta en estado atacable? */
				if (Declaraciones.UserList[TargetIndex].flags.AtacablePor > 0) {

					/* ' No esta atacable por el casteador? */
					if (Declaraciones.UserList[TargetIndex].flags.AtacablePor != CasterIndex) {

						/* ' Si es armada no puede ayudar */
						if (Extra.esArmada(CasterIndex)) {
							Protocol.WriteConsoleMsg(CasterIndex,
									"Los miembros del ejército real no pueden ayudar a ciudadanos en estado atacable.",
									FontTypeNames.FONTTYPE_INFO);
							return retval;
						}

						/* ' Seguro puesto? */
						if (Declaraciones.UserList[CasterIndex].flags.Seguro) {
							Protocol.WriteConsoleMsg(CasterIndex,
									"Para ayudar ciudadanos en estado atacable debes sacarte el seguro, pero te puedes volver criminal.",
									FontTypeNames.FONTTYPE_INFO);
							return retval;
						} else {
							DisNobAuBan(CasterIndex, Declaraciones.UserList[CasterIndex].Reputacion.NobleRep * 0.5,
									10000);
						}
					}
				}

			}
		}

		retval = true;

		return retval;

		/* FIXME: ErrHandler : */
		General.LogError("Error en CanSupportUser, Error: " + Err.Number + " - " + Err.description + " CasterIndex: "
				+ CasterIndex + ", TargetIndex: " + TargetIndex);

		return retval;
	}

	static void UpdateUserHechizos(boolean UpdateAll, int UserIndex, int Slot) {
		/* '*************************************************** */
		/* 'Author: Unknown */
		/* 'Last Modification: - */
		/* ' */
		/* '*************************************************** */

		int LoopC;

		/* 'Actualiza un solo slot */
		if (!UpdateAll) {
			/* 'Actualiza el inventario */
			if (Declaraciones.UserList[UserIndex].Stats.UserHechizos[Slot] > 0) {
				ChangeUserHechizo(UserIndex, Slot, Declaraciones.UserList[UserIndex].Stats.UserHechizos[Slot]);
			} else {
				ChangeUserHechizo(UserIndex, Slot, 0);
			}
		} else {
			/* 'Actualiza todos los slots */
			for (LoopC = (1); LoopC <= (Declaraciones.MAXUSERHECHIZOS); LoopC++) {
				/* 'Actualiza el inventario */
				if (Declaraciones.UserList[UserIndex].Stats.UserHechizos[LoopC] > 0) {
					ChangeUserHechizo(UserIndex, LoopC, Declaraciones.UserList[UserIndex].Stats.UserHechizos[LoopC]);
				} else {
					ChangeUserHechizo(UserIndex, LoopC, 0);
				}
			}
		}

	}

	static boolean CanSupportNpc(int CasterIndex, int TargetIndex) {
		boolean retval;
		/* '*************************************************** */
		/* 'Author: ZaMa */
		/* 'Last Modification: 18/09/2010 */
		/* 'Checks if caster can cast support magic on target Npc. */
		/* '*************************************************** */

		/* FIXME: ON ERROR GOTO ErrHandler */

		int OwnerIndex;

		OwnerIndex = Declaraciones.Npclist[TargetIndex].Owner;

		/* ' Si no tiene dueno puede */
		if (OwnerIndex == 0) {
			retval = true;
			return retval;
		}

		/* ' Puede hacerlo si es su propio npc */
		if (CasterIndex == OwnerIndex) {
			retval = true;
			return retval;
		}

		/* ' No podes ayudar si estas en consulta */
		if (Declaraciones.UserList[CasterIndex].flags.EnConsulta) {
			Protocol.WriteConsoleMsg(CasterIndex, "No puedes ayudar npcs mientras estas en consulta.",
					FontTypeNames.FONTTYPE_INFO);
			return retval;
		}

		/* ' Si estas en la arena, esta todo permitido */
		if (SistemaCombate.TriggerZonaPelea(CasterIndex, OwnerIndex) == TRIGGER6_PERMITE) {
			retval = true;
			return retval;
		}

		/* ' Victima criminal? */
		if (ES.criminal(OwnerIndex)) {
			/* ' Victima caos? */
			if (Extra.esCaos(OwnerIndex)) {
				/* ' Atacante caos? */
				if (Extra.esCaos(CasterIndex)) {
					/* ' No podes ayudar a un npc de un caos si sos caos */
					Protocol.WriteConsoleMsg(CasterIndex,
							"No puedes ayudar npcs que están luchando contra un miembro de tu facción.",
							FontTypeNames.FONTTYPE_INFO);
					return retval;
				}
			}

			/*
			 * ' Uno es caos y el otro no, o la victima es pk, entonces puede
			 * ayudar al npc
			 */
			retval = true;
			return retval;

			/* ' Victima ciuda */
		} else {
			/* ' Atacante ciuda? */
			if (!ES.criminal(CasterIndex)) {
				/* ' Atacante armada? */
				if (Extra.esArmada(CasterIndex)) {
					/* ' Victima armada? */
					if (Extra.esArmada(OwnerIndex)) {
						/*
						 * ' No podes ayudar a un npc de un armada si sos armada
						 */
						Protocol.WriteConsoleMsg(CasterIndex,
								"No puedes ayudar npcs que están luchando contra un miembro de tu facción.",
								FontTypeNames.FONTTYPE_INFO);
						return retval;
					}
				}

				/*
				 * ' Uno es armada y el otro ciuda, o los dos ciudas, puede
				 * atacar si no tiene seguro
				 */
				if (Declaraciones.UserList[CasterIndex].flags.Seguro) {
					Protocol.WriteConsoleMsg(CasterIndex,
							"Para ayudar a criaturas que luchan contra ciudadanos debes sacarte el seguro.",
							FontTypeNames.FONTTYPE_INFO);
					return retval;

					/* ' ayudo al npc sin seguro, se convierte en atacable */
				} else {
					UsUaRiOs.ToogleToAtackable(CasterIndex, OwnerIndex, true);
					retval = true;
					return retval;
				}

			}

			/*
			 * ' Atacante criminal y victima ciuda, entonces puede ayudar al npc
			 */
			retval = true;
			return retval;

		}

		retval = true;

		return retval;

		/* FIXME: ErrHandler : */
		General.LogError("Error en CanSupportNpc, Error: " + Err.Number + " - " + Err.description + " CasterIndex: "
				+ CasterIndex + ", OwnerIndex: " + OwnerIndex);

		return retval;
	}

	static void ChangeUserHechizo(int UserIndex, int Slot, int Hechizo) {
		/* '*************************************************** */
		/* 'Author: Unknown */
		/* 'Last Modification: - */
		/* ' */
		/* '*************************************************** */

		Declaraciones.UserList[UserIndex].Stats.UserHechizos[Slot] = Hechizo;

		if (Hechizo > 0 && Hechizo < Declaraciones.NumeroHechizos + 1) {
			Protocol.WriteChangeSpellSlot(UserIndex, Slot);
		} else {
			Protocol.WriteChangeSpellSlot(UserIndex, Slot);
		}

	}

	static void DesplazarHechizo(int UserIndex, int Dire, int HechizoDesplazado) {
		/* '*************************************************** */
		/* 'Author: Unknown */
		/* 'Last Modification: - */
		/* ' */
		/* '*************************************************** */

		if ((Dire != 1 && Dire != -1)) {
			return;
		}
		if (!(HechizoDesplazado >= 1 && HechizoDesplazado <= Declaraciones.MAXUSERHECHIZOS)) {
			return;
		}

		int TempHechizo;

		/* 'Mover arriba */
		if (Dire == 1) {
			if (HechizoDesplazado == 1) {
				Protocol.WriteConsoleMsg(UserIndex, "No puedes mover el hechizo en esa dirección.",
						FontTypeNames.FONTTYPE_INFO);
				return;
			} else {
				TempHechizo = Declaraciones.UserList[UserIndex].Stats.UserHechizos[HechizoDesplazado];
				Declaraciones.UserList[UserIndex].Stats.UserHechizos[HechizoDesplazado] = Declaraciones.UserList[UserIndex].Stats.UserHechizos[HechizoDesplazado
						- 1];
				Declaraciones.UserList[UserIndex].Stats.UserHechizos[HechizoDesplazado - 1] = TempHechizo;
			}
			/* 'mover abajo */
		} else {
			if (HechizoDesplazado == Declaraciones.MAXUSERHECHIZOS) {
				Protocol.WriteConsoleMsg(UserIndex, "No puedes mover el hechizo en esa dirección.",
						FontTypeNames.FONTTYPE_INFO);
				return;
			} else {
				TempHechizo = Declaraciones.UserList[UserIndex].Stats.UserHechizos[HechizoDesplazado];
				Declaraciones.UserList[UserIndex].Stats.UserHechizos[HechizoDesplazado] = Declaraciones.UserList[UserIndex].Stats.UserHechizos[HechizoDesplazado
						+ 1];
				Declaraciones.UserList[UserIndex].Stats.UserHechizos[HechizoDesplazado + 1] = TempHechizo;
			}
		}

	}

	static void DisNobAuBan(int UserIndex, int NoblePts, int BandidoPts) {
		/* '*************************************************** */
		/* 'Author: Unknown */
		/* 'Last Modification: - */
		/* ' */
		/* '*************************************************** */

		/*
		 * 'disminuye la nobleza NoblePts puntos y aumenta el bandido BandidoPts
		 * puntos
		 */
		boolean EraCriminal;
		EraCriminal = ES.criminal(UserIndex);

		/* 'Si estamos en la arena no hacemos nada */
		if (Declaraciones.MapData[Declaraciones.UserList[UserIndex].Pos.Map][Declaraciones.UserList[UserIndex].Pos.X][Declaraciones.UserList[UserIndex].Pos.Y].trigger == 6) {
			return;
		}

		if (Declaraciones.UserList[UserIndex].flags.Privilegios && (PlayerType.User || PlayerType.Consejero)) {
			/* 'pierdo nobleza... */
			Declaraciones.UserList[UserIndex].Reputacion.NobleRep = Declaraciones.UserList[UserIndex].Reputacion.NobleRep
					- NoblePts;
			if (Declaraciones.UserList[UserIndex].Reputacion.NobleRep < 0) {
				Declaraciones.UserList[UserIndex].Reputacion.NobleRep = 0;
			}

			/* 'gano bandido... */
			Declaraciones.UserList[UserIndex].Reputacion.BandidoRep = Declaraciones.UserList[UserIndex].Reputacion.BandidoRep
					+ BandidoPts;
			if (Declaraciones.UserList[UserIndex].Reputacion.BandidoRep > Declaraciones.MAXREP) {
				Declaraciones.UserList[UserIndex].Reputacion.BandidoRep = Declaraciones.MAXREP;
			}
			/* 'Call WriteNobilityLost(UserIndex) */
			Protocol.WriteMultiMessage(UserIndex, eMessages.NobilityLost);
			if (ES.criminal(UserIndex)) {
				if (Declaraciones.UserList[UserIndex].Faccion.ArmadaReal == 1) {
					ModFacciones.ExpulsarFaccionReal(UserIndex);
				}
			}
		}

		if (!EraCriminal && ES.criminal(UserIndex)) {
			UsUaRiOs.RefreshCharStatus(UserIndex);
		}
	}

}