
/*  AUTOMATICALLY CONVERTED FILE  */
/* [(0, 'ATTRIBUTE'), (1, 'VB_Name'), (5, '='), (4, '"Declaraciones"')] */
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

public class Declaraciones {

	/* '' */
	/* ' Modulo de declaraciones. Aca hay de todo. */
	/* ' */
	/* # IF SeguridadAlkon THEN */
	/* # END IF */

	public static clsAntiMassClon aClon;
	public static vb6.Collection TrashCollector;

	static final int MAXSPAWNATTEMPS = 60;
	static final int INFINITE_LOOPS = /* FANCY EXPRESSION */ -1;
	static final int FXSANGRE = 14;

	/* '' */
	/* ' The color of chats over head of dead characters. */
	static final int CHAT_COLOR_DEAD_CHAR = 0xC0C0C0;

	/* '' */
	/* ' The color of yells made by any kind of game administrator. */
	static final int CHAT_COLOR_GM_YELL = 0xF82FF;

	/* '' */
	/* ' Coordinates for normal sounds (not 3D, like rain) */
	static final int NO_3D_SOUND = 0;

	static final int iFragataFantasmal = 87;
	static final int iFragataReal = 190;
	static final int iFragataCaos = 189;
	static final int iBarca = 84;
	static final int iGalera = 85;
	static final int iGaleon = 86;

	/* ' Embarcaciones ciudas */
	static final int iBarcaCiuda = 395;
	static final int iBarcaCiudaAtacable = 552;
	static final int iGaleraCiuda = 397;
	static final int iGaleraCiudaAtacable = 560;
	static final int iGaleonCiuda = 399;
	static final int iGaleonCiudaAtacable = 556;

	/* ' Embarcaciones reales */
	static final int iBarcaReal = 550;
	static final int iBarcaRealAtacable = 553;
	static final int iGaleraReal = 558;
	static final int iGaleraRealAtacable = 561;
	static final int iGaleonReal = 554;
	static final int iGaleonRealAtacable = 557;

	/* ' Embarcaciones pk */
	static final int iBarcaPk = 396;
	static final int iGaleraPk = 398;
	static final int iGaleonPk = 400;

	/* ' Embarcaciones caos */
	static final int iBarcaCaos = 551;
	static final int iGaleraCaos = 559;
	static final int iGaleonCaos = 555;

	static final int LimiteNewbie = 12;

	/* 'Cabecera de los con */
	static public class tCabecera {
		String desc;
		int crc;
		int MagicWord;
	}

	public static tCabecera MiCabecera;

	/* 'Barrin 3/10/03 */
	/* 'Cambiado a 2 segundos el 30/11/07 */
	static final int TIEMPO_INICIOMEDITAR = 2000;

	static final int NingunEscudo = 2;
	static final int NingunCasco = 2;
	static final int NingunArma = 2;

	static final int EspadaMataDragonesIndex = 402;
	static final int LAUDMAGICO = 696;
	static final int FLAUTAMAGICA = 208;

	static final int LAUDELFICO = 1049;
	static final int FLAUTAELFICA = 1050;

	static final int APOCALIPSIS_SPELL_INDEX = 25;
	static final int DESCARGA_SPELL_INDEX = 23;

	static final int SLOTS_POR_FILA = 5;

	static final int PROB_ACUCHILLAR = 20;
	static final float DANO_ACUCHILLAR = 0.2;

	static final int MAXMASCOTASENTRENADOR = 7;

	static final int TIEMPO_CARCEL_PIQUETE = 10;

	/* '' */
	/* ' TRIGGERS */
	/* ' */
	/* ' @param NADA nada */
	/* ' @param BAJOTECHO bajo techo */
	/* ' @param trigger_2 ??? */
	/* ' @param POSINVALIDA los npcs no pueden pisar tiles con este trigger */
	/* ' @param ZONASEGURA no se puede robar o pelear desde este trigger */
	/* ' @param ANTIPIQUETE */
	/*
	 * ' @param ZONAPELEA al pelear en este trigger no se caen las cosas y no
	 * cambia el estado de ciuda o crimi
	 */
	/* ' */

	/* '' */
	/* ' constantes para el trigger 6 */
	/* ' */
	/* ' @see eTrigger */
	/* ' @param TRIGGER6_PERMITE TRIGGER6_PERMITE */
	/* ' @param TRIGGER6_PROHIBE TRIGGER6_PROHIBE */
	/* ' @param TRIGGER6_AUSENTE El trigger no aparece */
	/* ' */

	/* 'TODO : Reemplazar por un enum */
	static final String Bosque = "BOSQUE";
	static final String Nieve = "NIEVE";
	static final String Desierto = "DESIERTO";
	static final String Ciudad = "CIUDAD";
	static final String Campo = "CAMPO";
	static final String Dungeon = "DUNGEON";

	/* ' <<<<<< Targets >>>>>> */

	/* ' <<<<<< Acciona sobre >>>>>> */

	static final int MAXUSERHECHIZOS = 35;

	/* ' TODO: Y ESTO ? LO CONOCE GD ? */
	static final int EsfuerzoTalarGeneral = 4;
	static final int EsfuerzoTalarLenador = 2;

	static final int EsfuerzoPescarPescador = 1;
	static final int EsfuerzoPescarGeneral = 3;

	static final int EsfuerzoExcavarMinero = 2;
	static final int EsfuerzoExcavarGeneral = 5;

	static final int FX_TELEPORT_INDEX = 1;

	static final float PORCENTAJE_MATERIALES_UPGRADE = 0.85;

	/*
	 * ' La utilidad de esto es casi nula, sólo se revisa si fue a la cabeza...
	 */

	static final int Guardias = 6;

	static final int MAX_ORO_EDIT = 5000000;
	static final int MAX_VIDA_EDIT = 30000;

	static final String STANDARD_BOUNTY_HUNTER_MESSAGE = "Se te ha otorgado un premio por ayudar al proyecto reportando bugs, el mismo está disponible en tu bóveda.";
	static final String TAG_USER_INVISIBLE = "[INVISIBLE]";
	static final String TAG_CONSULT_MODE = "[CONSULTA]";

	static final int MAXREP = 6000000;
	static final int MAXORO = 90000000;
	static final int MAXEXP = 99999999;

	static final int MAXUSERMATADOS = 65000;

	static final int MAXATRIBUTOS = 40;
	static final int MINATRIBUTOS = 6;

	static final int LingoteHierro = 386;
	static final int LingotePlata = 387;
	static final int LingoteOro = 388;
	static final int Lena = 58;
	static final int LenaElfica = 1006;

	static final int MAXNPCS = 10000;
	static final int MAXCHARS = 10000;

	static final int HACHA_LENADOR = 127;
	static final int HACHA_LENA_ELFICA = 1005;
	static final int PIQUETE_MINERO = 187;

	static final int HACHA_LENADOR_NEWBIE = 561;
	static final int PIQUETE_MINERO_NEWBIE = 562;
	static final int CANA_PESCA_NEWBIE = 563;
	static final int SERRUCHO_CARPINTERO_NEWBIE = 564;
	static final int MARTILLO_HERRERO_NEWBIE = 565;

	static final int DAGA = 15;
	static final int FOGATA_APAG = 136;
	static final int FOGATA = 63;
	static final int ORO_MINA = 194;
	static final int PLATA_MINA = 193;
	static final int HIERRO_MINA = 192;
	static final int MARTILLO_HERRERO = 389;
	static final int SERRUCHO_CARPINTERO = 198;
	static final int ObjArboles = 4;
	static final int RED_PESCA = 543;
	static final int CANA_PESCA = 138;

	static final int MIN_APUNALAR = 10;

	/* '********** CONSTANTANTES *********** */

	/* '' */
	/* ' Cantidad de skills */
	static final int NUMSKILLS = 20;

	/* '' */
	/* ' Cantidad de Atributos */
	static final int NUMATRIBUTOS = 5;

	/* '' */
	/* ' Cantidad de Clases */
	static final int NUMCLASES = 12;

	/* '' */
	/* ' Cantidad de Razas */
	static final int NUMRAZAS = 5;

	/* '' */
	/* ' Valor maximo de cada skill */
	static final int MAXSKILLPOINTS = 100;

	/* '' */
	/* ' Cantidad de Ciudades */
	static final int NUMCIUDADES = 6;

	/* '' */
	/* 'Direccion */
	/* ' */
	/* ' @param NORTH Norte */
	/* ' @param EAST Este */
	/* ' @param SOUTH Sur */
	/* ' @param WEST Oeste */
	/* ' */

	/* '' */
	/* ' Cantidad maxima de mascotas */
	static final int MAXMASCOTAS = 3;

	/* '%%%%%%%%%% CONSTANTES DE INDICES %%%%%%%%%%%%%%% */
	static final int vlASALTO = 100;
	static final int vlASESINO = 1000;
	static final int vlCAZADOR = 5;
	static final int vlNoble = 5;
	static final int vlLadron = 25;
	static final int vlProleta = 2;

	/* '%%%%%%%%%% CONSTANTES DE INDICES %%%%%%%%%%%%%%% */
	static final int iCuerpoMuerto = 8;
	static final int iCabezaMuerto = 500;

	static final int iORO = 12;
	static final int Pescado = 139;

	static final int NUM_PECES = 4;
	public static int[] ListaPeces = new int[1 + Declaraciones.NUM_PECES]; /* XXX */

	/* '%%%%%%%%%% CONSTANTES DE INDICES %%%%%%%%%%%%%%% */

	static final int FundirMetal = 88;

	/* 'HP adicionales cuando sube de nivel */
	static final int AdicionalHPGuerrero = 2;
	/* 'HP adicionales cuando sube de nivel */
	static final int AdicionalHPCazador = 1;

	static final int AumentoSTDef = 15;
	static final int AumentoStBandido = /* FANCY EXPRESSION */ AumentoSTDef + 3;
	static final int AumentoSTLadron = /* FANCY EXPRESSION */ AumentoSTDef + 3;
	static final int AumentoSTMago = /* FANCY EXPRESSION */ AumentoSTDef - 1;
	static final int AumentoSTTrabajador = /* FANCY EXPRESSION */ AumentoSTDef + 25;

	/* 'Tamano del mapa */
	static final int XMaxMapSize = 100;
	static final int XMinMapSize = 1;
	static final int YMaxMapSize = 100;
	static final int YMinMapSize = 1;

	/* 'Tamano del tileset */
	static final int TileSizeX = 32;
	static final int TileSizeY = 32;

	/* 'Tamano en Tiles de la pantalla de visualizacion */
	static final int XWindow = 17;
	static final int YWindow = 13;

	/* 'Sonidos */
	static final int SND_SWING = 2;
	static final int SND_TALAR = 13;
	static final int SND_PESCAR = 14;
	static final int SND_MINERO = 15;
	static final int SND_WARP = 3;
	static final int SND_PUERTA = 5;
	static final int SND_NIVEL = 6;

	static final int SND_USERMUERTE = 11;
	static final int SND_IMPACTO = 10;
	static final int SND_IMPACTO2 = 12;
	static final int SND_LENADOR = 13;
	static final int SND_FOGATA = 14;
	static final int SND_AVE = 21;
	static final int SND_AVE2 = 22;
	static final int SND_AVE3 = 34;
	static final int SND_GRILLO = 28;
	static final int SND_GRILLO2 = 29;
	static final int SND_SACARARMA = 25;
	static final int SND_ESCUDO = 37;
	static final int MARTILLOHERRERO = 41;
	static final int LABUROCARPINTERO = 42;
	static final int SND_BEBER = 46;

	/* '' */
	/* ' Cantidad maxima de objetos por slot de inventario */
	static final int MAX_INVENTORY_OBJS = 10000;

	/* '' */
	/* ' Cantidad de "slots" en el inventario con mochila */
	static final int MAX_INVENTORY_SLOTS = 30;

	/* '' */
	/* ' Cantidad de "slots" en el inventario sin mochila */
	static final int MAX_NORMAL_INVENTORY_SLOTS = 20;

	/* '' */
	/* ' Constante para indicar que se esta usando ORO */
	static final int FLAGORO = /* FANCY EXPRESSION */ MAX_INVENTORY_SLOTS + 1;

	/* ' CATEGORIAS PRINCIPALES */

	/* 'Texto */
	static final String FONTTYPE_TALK = "~255~255~255~0~0";
	static final String FONTTYPE_FIGHT = "~255~0~0~1~0";
	static final String FONTTYPE_WARNING = "~32~51~223~1~1";
	static final String FONTTYPE_INFO = "~65~190~156~0~0";
	static final String FONTTYPE_INFOBOLD = "~65~190~156~1~0";
	static final String FONTTYPE_EJECUCION = "~130~130~130~1~0";
	static final String FONTTYPE_PARTY = "~255~180~255~0~0";
	static final String FONTTYPE_VENENO = "~0~255~0~0~0";
	static final String FONTTYPE_GUILD = "~255~255~255~1~0";
	static final String FONTTYPE_SERVER = "~0~185~0~0~0";
	static final String FONTTYPE_GUILDMSG = "~228~199~27~0~0";
	static final String FONTTYPE_CONSEJO = "~130~130~255~1~0";
	static final String FONTTYPE_CONSEJOCAOS = "~255~60~00~1~0";
	static final String FONTTYPE_CONSEJOVesA = "~0~200~255~1~0";
	static final String FONTTYPE_CONSEJOCAOSVesA = "~255~50~0~1~0";
	static final String FONTTYPE_CENTINELA = "~0~255~0~1~0";

	/* 'Estadisticas */
	static final int STAT_MAXELV = 255;
	static final int STAT_MAXHP = 999;
	static final int STAT_MAXSTA = 999;
	static final int STAT_MAXMAN = 9999;
	static final int STAT_MAXHIT_UNDER36 = 99;
	static final int STAT_MAXHIT_OVER36 = 999;
	static final int STAT_MAXDEF = 99;

	static final int ELU_SKILL_INICIAL = 200;
	static final int EXP_ACIERTO_SKILL = 50;
	static final int EXP_FALLO_SKILL = 20;

	/* ' ************************************************************** */
	/* ' ************************************************************** */
	/* ' ************************ TIPOS ******************************* */
	/* ' ************************************************************** */
	/* ' ************************************************************** */

	static public class tObservacion {
		String Creador;
		vb6.Date Fecha;

		String Detalles;
	}

	static public class tRecord {
		String Usuario;
		String Motivo;
		String Creador;
		vb6.Date Fecha;

		int NumObs;
		tObservacion[] Obs;
	}

	static public class tHechizo {
		String Nombre;
		String desc;
		String PalabrasMagicas;

		String HechizeroMsg;
		String TargetMsg;
		String PropioMsg;

		TipoHechizo Tipo;

		int WAV;
		int FXgrh;
		int loops;

		int SubeHP;
		int MinHp;
		int MaxHp;

		int SubeMana;
		int MiMana;
		int MaMana;

		int SubeSta;
		int MinSta;
		int MaxSta;

		int SubeHam;
		int MinHam;
		int MaxHam;

		int SubeSed;
		int MinSed;
		int MaxSed;

		int SubeAgilidad;
		int MinAgilidad;
		int MaxAgilidad;

		int SubeFuerza;
		int MinFuerza;
		int MaxFuerza;

		int SubeCarisma;
		int MinCarisma;
		int MaxCarisma;

		int Invisibilidad;
		int Paraliza;
		int Inmoviliza;
		int RemoverParalisis;
		int RemoverEstupidez;
		int CuraVeneno;
		int Envenena;
		int Maldicion;
		int RemoverMaldicion;
		int Bendicion;
		int Estupidez;
		int Ceguera;
		int Revivir;
		int Morph;
		int Mimetiza;
		int RemueveInvisibilidadParcial;

		int Warp;
		int Invoca;
		int NumNpc;
		int cant;

		int MinSkill;
		int ManaRequerido;

		int StaRequerido;

		TargetType Target;

		int NeedStaff;
		boolean StaffAffected;
	}

	static public class LevelSkill {
		int LevelValue;
	}

	static public class UserOBJ {
		int ObjIndex;
		int Amount;
		int Equipped;
	}

	static public class Inventario {
		UserOBJ[] Object;
		int WeaponEqpObjIndex;
		int WeaponEqpSlot;
		int ArmourEqpObjIndex;
		int ArmourEqpSlot;
		int EscudoEqpObjIndex;
		int EscudoEqpSlot;
		int CascoEqpObjIndex;
		int CascoEqpSlot;
		int MunicionEqpObjIndex;
		int MunicionEqpSlot;
		int AnilloEqpObjIndex;
		int AnilloEqpSlot;
		int BarcoObjIndex;
		int BarcoSlot;
		int MochilaEqpObjIndex;
		int MochilaEqpSlot;
		int NroItems;
	}

	static public class tPartyData {
		int PIndex;
		double RemXP;
		int TargetUser;
	}

	static public class Position {
		int X;
		int Y;
	}

	static public class WorldPos {
		int Map;
		int X;
		int Y;
	}

	static public class FXdata {
		String Nombre;
		int GrhIndex;
		int Delay;
	}

	/* 'Datos de user o npc */
	static public class Char {
		int CharIndex;
		int Head;
		int body;

		int WeaponAnim;
		int ShieldAnim;
		int CascoAnim;

		int FX;
		int loops;

		eHeading heading;
	}

	/* 'Tipos de objetos */
	static public class ObjData {
		String Name;

		eOBJType OBJType;

		int GrhIndex;
		int GrhSecundario;

		int MaxItems;
		Inventario Conte;
		int Apunala;
		int Acuchilla;

		int HechizoIndex;

		String ForoID;

		int MinHp;
		int MaxHp;

		int MineralIndex;
		int LingoteInex;

		int proyectil;
		int Municion;

		int Crucial;
		int Newbie;

		int MinSta;

		int TipoPocion;
		int MaxModificador;
		int MinModificador;
		int DuracionEfecto;
		int MinSkill;
		int LingoteIndex;

		int MinHIT;
		int MaxHIT;

		int MinHam;
		int MinSed;

		int def;
		int MinDef;
		int MaxDef;

		int Ropaje;

		int WeaponAnim;
		int WeaponRazaEnanaAnim;
		int ShieldAnim;
		int CascoAnim;

		int Valor;

		int Cerrada;
		int Llave;
		int clave;

		int Radio;

		int MochilaType;

		int Guante;

		int IndexAbierta;
		int IndexCerrada;
		int IndexCerradaLlave;

		int RazaEnana;
		int RazaDrow;
		int RazaElfa;
		int RazaGnoma;
		int RazaHumana;

		int Mujer;
		int Hombre;

		int Envenena;
		int Paraliza;

		int Agarrable;

		int LingH;
		int LingO;
		int LingP;
		int Madera;
		int MaderaElfica;

		int SkHerreria;
		int SkCarpinteria;

		String texto;

		eClass[] ClaseProhibida;

		int Snd1;
		int Snd2;
		int Snd3;

		int Real;
		int Caos;

		int NoSeCae;
		int NoSeTira;
		int NoRobable;
		int NoComerciable;
		int Intransferible;

		int StaffPower;
		int StaffDamageBonus;
		int DefensaMagicaMax;
		int DefensaMagicaMin;
		int Refuerzo;

		int ImpideParalizar;
		int ImpideInmobilizar;
		int ImpideAturdir;
		int ImpideCegar;

		int Log;
		int NoLog;

		int Upgrade;
	}

	static public class Obj {
		int ObjIndex;
		int Amount;
	}

	/* '[Pablo ToxicWaste] */
	static public class ModClase {
		double Evasion;
		double AtaqueArmas;
		double AtaqueProyectiles;
		double AtaqueWrestling;
		double DanoArmas;
		double DanoProyectiles;
		double DanoWrestling;
		double Escudo;
	}

	static public class ModRaza {
		float Fuerza;
		float Agilidad;
		float Inteligencia;
		float Carisma;
		float Constitucion;
	}
	/* '[/Pablo ToxicWaste] */

	/* '[KEVIN] */
	/* 'Banco Objs */
	static final int MAX_BANCOINVENTORY_SLOTS = 40;
	/* '[/KEVIN] */

	/* '[KEVIN] */
	static public class BancoInventario {
		UserOBJ[] Object;
		int NroItems;
	}
	/* '[/KEVIN] */

	/* ' Determina el color del nick */

	/* '******* */
	/* 'FOROS * */
	/* '******* */

	/* ' Tipos de mensajes */

	/* ' Indica los privilegios para visualizar los diferentes foros */

	/* ' Indica el tipo de foro */

	/* ' Limite de posts */
	static final int MAX_STICKY_POST = 10;
	static final int MAX_GENERAL_POST = 35;

	/* ' Estructura contenedora de mensajes */
	/* ' Public Type tForo */
	/* ' StickyTitle(1 To MAX_STICKY_POST) As String */
	/* ' StickyPost(1 To MAX_STICKY_POST) As String */
	/* ' GeneralTitle(1 To MAX_GENERAL_POST) As String */
	/* ' GeneralPost(1 To MAX_GENERAL_POST) As String */
	/* ' End Type */

	/* '********************************************************* */
	/* '********************************************************* */
	/* '********************************************************* */
	/* '********************************************************* */
	/* '******* T I P O S D E U S U A R I O S ************** */
	/* '********************************************************* */
	/* '********************************************************* */
	/* '********************************************************* */
	/* '********************************************************* */

	/* 'Fama del usuario */
	static public class tReputacion {
		int NobleRep;
		int BurguesRep;
		int PlebeRep;
		int LadronesRep;
		int BandidoRep;
		int AsesinoRep;
		int Promedio;
	}

	/* 'Estadisticas de los usuarios */
	static public class UserStats {
		int GLD;
		int Banco;

		int MaxHp;
		int MinHp;

		int MaxSta;
		int MinSta;
		int MaxMAN;
		int MinMAN;
		int MaxHIT;
		int MinHIT;

		int MaxHam;
		int MinHam;

		int MaxAGU;
		int MinAGU;

		int def;
		double Exp;
		int ELV;
		int ELU;
		int[] UserSkills;
		int[] UserAtributos;
		int[] UserAtributosBackUP;
		int[] UserHechizos;
		int UsuariosMatados;
		int CriminalesMatados;
		int NPCsMuertos;

		int SkillPts;

		int[] ExpSkills;
		int[] EluSkills;

	}

	/* 'Flags */
	static public class UserFlags {
		int Muerto;
		int Escondido;
		boolean Comerciando;
		boolean UserLogged;
		boolean Meditando;
		String Descuento;
		int Hambre;
		int Sed;
		int PuedeMoverse;
		int TimerLanzarSpell;
		int PuedeTrabajar;
		int Envenenado;
		int Paralizado;
		int Inmovilizado;
		int Estupidez;
		int Ceguera;
		int invisible;
		int Maldicion;
		int Bendicion;
		int Oculto;
		int Desnudo;
		boolean Descansar;
		int Hechizo;
		boolean TomoPocion;
		int TipoPocion;

		boolean NoPuedeSerAtacado;
		int AtacablePor;
		int ShareNpcWith;

		int Vuela;
		int Navegando;
		boolean Seguro;
		boolean SeguroResu;

		int DuracionEfecto;
		int TargetNPC;
		eNPCType TargetNpcTipo;
		int OwnedNpc;
		int NpcInv;

		int Ban;
		int AdministrativeBan;

		int TargetUser;

		int TargetObj;
		int TargetObjMap;
		int TargetObjX;
		int TargetObjY;

		int TargetMap;
		int TargetX;
		int TargetY;

		int TargetObjInvIndex;
		int TargetObjInvSlot;

		int AtacadoPorNpc;
		int AtacadoPorUser;
		int NPCAtacado;
		boolean Ignorado;

		boolean EnConsulta;
		boolean SendDenounces;

		int StatsChanged;
		PlayerType Privilegios;
		boolean PrivEspecial;

		int ValCoDe;

		String LastCrimMatado;
		String LastCiudMatado;

		int OldBody;
		int OldHead;
		int AdminInvisible;
		boolean AdminPerseguible;

		int ChatColor;

		String MD5Reportado;

		int TimesWalk;
		int StartWalk;
		int CountSH;

		int UltimoMensaje;

		int Silenciado;

		int Mimetizado;

		int CentinelaIndex;
		boolean CentinelaOK;

		int lastMap;
		int Traveling;

		String ParalizedBy;
		int ParalizedByIndex;
		int ParalizedByNpcIndex;

		/* # IF SeguridadAlkon THEN */
		/* # END IF */
	}

	static public class UserCounters {
		int IdleCount;
		int AttackCounter;
		int HPCounter;
		int STACounter;
		int Frio;
		int Lava;
		int COMCounter;
		int AGUACounter;
		int Veneno;
		int Paralisis;
		int Ceguera;
		int Estupidez;

		int Invisibilidad;
		int TiempoOculto;

		int Mimetismo;
		int PiqueteC;
		int Pena;
		WorldPos SendMapCounter;
		boolean Saliendo;
		int Salir;

		int tInicioMeditar;
		boolean bPuedeMeditar;

		int TimerLanzarSpell;
		int TimerPuedeAtacar;
		int TimerPuedeUsarArco;
		int TimerPuedeTrabajar;
		int TimerUsar;
		int TimerMagiaGolpe;
		int TimerGolpeMagia;
		int TimerGolpeUsar;
		int TimerPuedeSerAtacado;
		int TimerPerteneceNpc;
		int TimerEstadoAtacable;

		int Trabajando;
		int Ocultando;

		int failedUsageAttempts;

		int goHome;
		int AsignedSkills;
		/* # IF SeguridadAlkon THEN */
		/* # END IF */
	}

	/* 'Cosas faccionarias. */
	static public class tFacciones {
		int ArmadaReal;
		int FuerzasCaos;
		int CriminalesMatados;
		int CiudadanosMatados;
		int RecompensasReal;
		int RecompensasCaos;
		int RecibioExpInicialReal;
		int RecibioExpInicialCaos;
		int RecibioArmaduraReal;
		int RecibioArmaduraCaos;
		int Reenlistadas;
		int NivelIngreso;
		String FechaIngreso;
		int MatadosIngreso;
		int NextRecompensa;
	}

	static public class tCrafting {
		int Cantidad;
		int PorCiclo;
	}

	/* 'Tipo de los Usuarios */
	static public class User {
		String Name;
		int ID;

		boolean showName;

		Char Char;
		Char CharMimetizado;
		Char OrigChar;

		String desc;
		String DescRM;

		eClass clase;
		eRaza raza;
		eGenero Genero;
		String email;
		eCiudad Hogar;

		Inventario Invent;

		WorldPos Pos;

		boolean ConnIDValida;
		int ConnID;

		BancoInventario BancoInvent;

		UserCounters Counters;

		tCrafting Construir;

		int[] MascotasIndex;
		int[] MascotasType;
		int NroMascotas;

		UserStats Stats;
		UserFlags flags;

		tReputacion Reputacion;

		tFacciones Faccion;

		/* # IF SeguridadAlkon THEN */
		/* # END IF */

		/* # IF ConUpTime THEN */
		vb6.Date LogOnTime;
		int UpTime;
		/* # END IF */

		String ip;

		mdlCOmercioConUsuario.tCOmercioUsuario ComUsu;

		int GuildIndex;
		ALINEACION_GUILD FundandoGuildAlineacion;
		int EscucheClan;

		int PartyIndex;
		int PartySolicitud;

		int KeyCrypt;

		ModAreas.AreaInfo AreasInfo;

		clsByteQueue outgoingData;
		clsByteQueue incomingData;

		int CurrentInventorySlots;
	}

	/* '********************************************************* */
	/* '********************************************************* */
	/* '********************************************************* */
	/* '********************************************************* */
	/* '** T I P O S D E N P C S ************************** */
	/* '********************************************************* */
	/* '********************************************************* */
	/* '********************************************************* */
	/* '********************************************************* */

	static public class NPCStats {
		int Alineacion;
		int MaxHp;
		int MinHp;
		int MaxHIT;
		int MinHIT;
		int def;
		int defM;
	}

	static public class NpcCounters {
		int Paralisis;
		int TiempoExistencia;
	}

	static public class NPCFlags {
		int AfectaParalisis;
		int Domable;
		int Respawn;
		boolean NPCActive;
		boolean Follow;
		int Faccion;
		int AtacaDoble;
		int LanzaSpells;

		int ExpCount;

		TipoAI OldMovement;
		int OldHostil;

		int AguaValida;
		int TierraInvalida;

		int Sound;
		String AttackedBy;
		String AttackedFirstBy;
		int BackUp;
		int RespawnOrigPos;

		int Envenenado;
		int Paralizado;
		int Inmovilizado;
		int invisible;
		int Maldicion;
		int Bendicion;

		int Snd1;
		int Snd2;
		int Snd3;
	}

	static public class tCriaturasEntrenador {
		int NpcIndex;
		String NpcName;
		int tmpIndex;
	}

	/* ' New type for holding the pathfinding info */
	static public class NpcPathFindingInfo {
		Queue.tVertice[] Path;
		Position Target;
		int PathLenght;
		int CurPos;
		int TargetUser;
		boolean NoPath;

	}
	/* ' New type for holding the pathfinding info */

	static public class tDrops {
		int ObjIndex;
		int Amount;
	}

	static final int MAX_NPC_DROPS = 5;

	static public class npc {
		String Name;
		Char Char;
		String desc;

		eNPCType NPCtype;
		int Numero;

		int InvReSpawn;

		int Comercia;
		int Target;
		int TargetNPC;
		int TipoItems;

		int Veneno;

		WorldPos Pos;
		WorldPos Orig;
		int SkillDomar;

		TipoAI Movement;
		int Attackable;
		int Hostile;
		int PoderAtaque;
		int PoderEvasion;

		int Owner;

		int GiveEXP;
		int GiveGLD;
		tDrops[] Drop;

		NPCStats Stats;
		NPCFlags flags;
		NpcCounters Contadores;

		Inventario Invent;
		int CanAttack;

		int NroExpresiones;
		String[] Expresiones;

		int NroSpells;
		int[] Spells;

		int NroCriaturas;
		tCriaturasEntrenador[] Criaturas;
		int MaestroUser;
		int MaestroNpc;
		int Mascotas;

		NpcPathFindingInfo PFINFO;
		ModAreas.AreaInfo AreasInfo;

		int Ciudad;

		int ClanIndex;
	}

	/* '********************************************************** */
	/* '********************************************************** */
	/* '******************** Tipos del mapa ********************** */
	/* '********************************************************** */
	/* '********************************************************** */
	/* 'Tile */
	static public class MapBlock {
		int Blocked;
		int[] Graphic;
		int UserIndex;
		int NpcIndex;
		Obj ObjInfo;
		WorldPos TileExit;
		eTrigger trigger;
	}

	/* 'Info del mapa */
	static public class MapInfo {
		int NumUsers;
		String Music;
		String Name;
		WorldPos StartPos;
		WorldPos OnDeathGoTo;

		int MapVersion;
		boolean Pk;
		int MagiaSinEfecto;
		int NoEncriptarMP;

		int InviSinEfecto;
		int ResuSinEfecto;
		int OcultarSinEfecto;
		int InvocarSinEfecto;

		int RoboNpcsPermitido;

		String Terreno;
		String Zona;
		int Restringir;
		int BackUp;
	}

	/* '********** V A R I A B L E S P U B L I C A S *********** */

	public static boolean SERVERONLINE;
	public static String ULTIMAVERSION;
	/* ' TODO: Se usa esta variable ? */
	public static boolean BackUp;

	public static String[] ListaRazas = new String[1 + Declaraciones.NUMRAZAS]; /* XXX */
	public static String[] SkillsNames = new String[1 + Declaraciones.NUMSKILLS]; /* XXX */
	public static String[] ListaClases = new String[1 + Declaraciones.NUMCLASES]; /* XXX */
	public static String[] ListaAtributos = new String[1 + Declaraciones.NUMATRIBUTOS]; /* XXX */

	public static int RECORDusuarios;

	/* ' */
	/* 'Directorios */
	/* ' */

	/* '' */
	/* 'Ruta base del server, en donde esta el "server.ini" */
	public static String IniPath;

	/* '' */
	/* 'Ruta base para guardar los chars */
	public static String CharPath;

	/* '' */
	/* 'Ruta base para los archivos de mapas */
	public static String MapPath;

	/* '' */
	/* 'Ruta base para los DATs */
	public static String DatPath;

	/* '' */
	/* 'Bordes del mapa */
	public static int MinXBorder;
	public static int MaxXBorder;
	public static int MinYBorder;
	public static int MaxYBorder;

	/* '' */
	/* 'Numero de usuarios actual */
	public static int NumUsers;
	public static int LastUser;
	public static int LastChar;
	public static int NumChars;
	public static int LastNPC;
	public static int NumNPCs;
	public static int NumFX;
	public static int NumMaps;
	public static int NumObjDatas;
	public static int NumeroHechizos;
	public static int AllowMultiLogins;
	public static int IdleLimit;
	public static int MaxUsers;
	public static int HideMe;
	public static String LastBackup;
	public static String Minutos;
	public static boolean haciendoBK;
	public static int PuedeCrearPersonajes;
	public static int ServerSoloGMs;
	public static int NumRecords;
	public static float HappyHour;
	public static boolean HappyHourActivated;
	public static float[] HappyHourDays = new float[1 + 7]; /* XXX */

	/* '' */
	/* 'Esta activada la verificacion MD5 ? */
	public static int MD5ClientesActivado;

	public static boolean EnPausa;
	public static boolean EnTesting;

	/* '*****************ARRAYS PUBLICOS************************* */
	/* 'USUARIOS */
	public static User[] UserList = new User[0]; /* XXX */
	/* 'NPCS */
	public static npc[] Npclist = new npc[1 + Declaraciones.MAXNPCS]; /* XXX */
	public static MapBlock[] MapData = new MapBlock[0]; /* XXX */
	public static MapInfo[] MapInfo = new MapInfo[0]; /* XXX */
	public static tHechizo[] Hechizos = new tHechizo[0]; /* XXX */
	public static int[] CharList = new int[1 + Declaraciones.MAXCHARS]; /* XXX */
	public static ObjData[] ObjData = new ObjData[0]; /* XXX */
	public static FXdata[] FX = new FXdata[0]; /* XXX */
	public static tCriaturasEntrenador[] SpawnList = new tCriaturasEntrenador[0]; /* XXX */
	public static LevelSkill[] LevelSkill = new LevelSkill[1 + 50]; /* XXX */
	public static String[] ForbidenNames = new String[0]; /* XXX */
	public static int[] ArmasHerrero = new int[0]; /* XXX */
	public static int[] ArmadurasHerrero = new int[0]; /* XXX */
	public static int[] ObjCarpintero = new int[0]; /* XXX */
	public static String[] MD5s = new String[0]; /* XXX */
	public static vb6.Collection BanIps;
	public static clsParty[] Parties = new clsParty[1 + mdParty.MAX_PARTIES]; /* XXX */
	public static ModClase[] ModClase = new ModClase[1 + Declaraciones.NUMCLASES]; /* XXX */
	public static ModRaza[] ModRaza = new ModRaza[1 + Declaraciones.NUMRAZAS]; /* XXX */
	public static double[] ModVida = new double[1 + Declaraciones.NUMCLASES]; /* XXX */
	public static int[] DistribucionEnteraVida = new int[1 + 5]; /* XXX */
	public static int[] DistribucionSemienteraVida = new int[1 + 4]; /* XXX */
	public static WorldPos[] Ciudades = new WorldPos[1 + Declaraciones.NUMCIUDADES]; /* XXX */
	public static HomeDistance[] distanceToCities = new HomeDistance[0]; /* XXX */
	public static tRecord[] Records = new tRecord[0]; /* XXX */
	/* '********************************************************* */

	static public class HomeDistance {
		int[] distanceToCity;
	}

	public static WorldPos Nix;
	public static WorldPos Ullathorpe;
	public static WorldPos Banderbill;
	public static WorldPos Lindos;
	public static WorldPos Arghal;
	public static WorldPos Arkhein;
	public static WorldPos Nemahuak;

	public static WorldPos Prision;
	public static WorldPos Libertad;

	public static cCola Ayuda;
	public static cCola Denuncias;
	public static ConsultasPopulares ConsultaPopular;
	public static SoundMapInfo SonidosMapas;

	static final int MATRIX_INITIAL_MAP = 1;

	static final int GOHOME_PENALTY = 5;
	static final int GM_MAP = 49;

	static final int TELEP_OBJ_INDEX = 1012;

	static final int HUMANO_H_PRIMER_CABEZA = 1;
	/*
	 * 'En verdad es hasta la 51, pero como son muchas estas las dejamos no
	 * seleccionables
	 */
	static final int HUMANO_H_ULTIMA_CABEZA = 40;

	static final int ELFO_H_PRIMER_CABEZA = 101;
	static final int ELFO_H_ULTIMA_CABEZA = 122;

	static final int DROW_H_PRIMER_CABEZA = 201;
	static final int DROW_H_ULTIMA_CABEZA = 221;

	static final int ENANO_H_PRIMER_CABEZA = 301;
	static final int ENANO_H_ULTIMA_CABEZA = 319;

	static final int GNOMO_H_PRIMER_CABEZA = 401;
	static final int GNOMO_H_ULTIMA_CABEZA = 416;
	/* '************************************************** */
	static final int HUMANO_M_PRIMER_CABEZA = 70;
	static final int HUMANO_M_ULTIMA_CABEZA = 89;

	static final int ELFO_M_PRIMER_CABEZA = 170;
	static final int ELFO_M_ULTIMA_CABEZA = 188;

	static final int DROW_M_PRIMER_CABEZA = 270;
	static final int DROW_M_ULTIMA_CABEZA = 288;

	static final int ENANO_M_PRIMER_CABEZA = 370;
	static final int ENANO_M_ULTIMA_CABEZA = 384;

	static final int GNOMO_M_PRIMER_CABEZA = 470;
	static final int GNOMO_M_ULTIMA_CABEZA = 484;

	/*
	 * ' Por ahora la dejo constante.. SI se quisiera extender la propiedad de
	 * paralziar, se podria hacer
	 */
	/* ' una nueva variable en el dat. */
	static final int GUANTE_HURTO = 873;

	static final int ESPADA_VIKINGA = 123;
	/* ''''''' */
	/* '' Pretorianos */
	/* ''''''' */
	public static clsClanPretoriano[] ClanPretoriano = new clsClanPretoriano[0]; /* XXX */

	static final int MAX_DENOUNCES = 20;

	/* 'Mensajes de los NPCs enlistadores (Nobles): */
	static final String MENSAJE_REY_CAOS = "¿Esperabas pasar desapercibido, intruso? Los servidores del Demonio no son bienvenidos, ¡Guardias, a él!";
	static final String MENSAJE_REY_CRIMINAL_NOENLISTABLE = "Tus pecados son grandes, pero aún así puedes redimirte. El pasado deja huellas, pero aún puedes limpiar tu alma.";
	static final String MENSAJE_REY_CRIMINAL_ENLISTABLE = "Limpia tu reputación y paga por los delitos cometidos. Un miembro de la Armada Real debe tener un comportamiento ejemplar.";

	static final String MENSAJE_DEMONIO_REAL = "Lacayo de Tancredo, ve y dile a tu gente que nadie pisará estas tierras si no se arrodilla ante mi.";
	static final String MENSAJE_DEMONIO_CIUDADANO_NOENLISTABLE = "Tu indecisión te ha condenado a una vida sin sentido, aún tienes elección... Pero ten mucho cuidado, mis hordas nunca descansan.";
	static final String MENSAJE_DEMONIO_CIUDADANO_ENLISTABLE = "Siento el miedo por tus venas. Deja de ser escoria y únete a mis filas, sabrás que es el mejor camino.";

	public static clsIniManager Administradores;

	static final int MIN_AMOUNT_LOG = 1000;
	static final int MIN_VALUE_LOG = 50000;
	static final int MIN_GOLD_AMOUNT_LOG = 10000;

	/* 'TODO:Esto hay que volarlo, es temporal! */
	public static String GUILDPATH;
	public static String GUILDINFOFILE;

}