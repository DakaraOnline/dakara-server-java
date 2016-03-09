

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
		public String desc;
		public int crc;
		public int MagicWord;
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
	public static int[] ListaPeces = new int[1 + Declaraciones.NUM_PECES];

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

	/* 'Tamaño del mapa */
	static final int XMaxMapSize = 100;
	static final int XMinMapSize = 1;
	static final int YMaxMapSize = 100;
	static final int YMinMapSize = 1;

	/* 'Tamaño del tileset */
	static final int TileSizeX = 32;
	static final int TileSizeY = 32;

	/* 'Tamaño en Tiles de la pantalla de visualizacion */
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
		public String Creador;
		public vb6.Date Fecha;

		public String Detalles;
	}

	static public class tRecord {
		public String Usuario;
		public String Motivo;
		public String Creador;
		public vb6.Date Fecha;

		public int NumObs;
		public tObservacion[] Obs;
	}

	static public class tHechizo {
		public String Nombre;
		public String desc;
		public String PalabrasMagicas;

		public String HechizeroMsg;
		public String TargetMsg;
		public String PropioMsg;

		public TipoHechizo Tipo;

		public int WAV;
		public int FXgrh;
		public int loops;

		public int SubeHP;
		public int MinHp;
		public int MaxHp;

		public int SubeMana;
		public int MiMana;
		public int MaMana;

		public int SubeSta;
		public int MinSta;
		public int MaxSta;

		public int SubeHam;
		public int MinHam;
		public int MaxHam;

		public int SubeSed;
		public int MinSed;
		public int MaxSed;

		public int SubeAgilidad;
		public int MinAgilidad;
		public int MaxAgilidad;

		public int SubeFuerza;
		public int MinFuerza;
		public int MaxFuerza;

		public int SubeCarisma;
		public int MinCarisma;
		public int MaxCarisma;

		public int Invisibilidad;
		public int Paraliza;
		public int Inmoviliza;
		public int RemoverParalisis;
		public int RemoverEstupidez;
		public int CuraVeneno;
		public int Envenena;
		public int Maldicion;
		public int RemoverMaldicion;
		public int Bendicion;
		public int Estupidez;
		public int Ceguera;
		public int Revivir;
		public int Morph;
		public int Mimetiza;
		public int RemueveInvisibilidadParcial;

		public int Warp;
		public int Invoca;
		public int NumNpc;
		public int cant;

		public int MinSkill;
		public int ManaRequerido;

		public int StaRequerido;

		public TargetType Target;

		public int NeedStaff;
		public boolean StaffAffected;
	}

	static public class LevelSkill {
		public int LevelValue;
	}

	static public class UserOBJ {
		public int ObjIndex;
		public int Amount;
		public int Equipped;
	}

	static public class Inventario {
		public UserOBJ[] Object;
		public int WeaponEqpObjIndex;
		public int WeaponEqpSlot;
		public int ArmourEqpObjIndex;
		public int ArmourEqpSlot;
		public int EscudoEqpObjIndex;
		public int EscudoEqpSlot;
		public int CascoEqpObjIndex;
		public int CascoEqpSlot;
		public int MunicionEqpObjIndex;
		public int MunicionEqpSlot;
		public int AnilloEqpObjIndex;
		public int AnilloEqpSlot;
		public int BarcoObjIndex;
		public int BarcoSlot;
		public int MochilaEqpObjIndex;
		public int MochilaEqpSlot;
		public int NroItems;
	}

	static public class tPartyData {
		public int PIndex;
		public double RemXP;
		public int TargetUser;
	}

	static public class Position {
		public int X;
		public int Y;
	}

	static public class WorldPos {
		public int Map;
		public int X;
		public int Y;
	}

	static public class FXdata {
		public String Nombre;
		public int GrhIndex;
		public int Delay;
	}

	/* 'Datos de user o npc */
	static public class Char {
		public int CharIndex;
		public int Head;
		public int body;

		public int WeaponAnim;
		public int ShieldAnim;
		public int CascoAnim;

		public int FX;
		public int loops;

		public eHeading heading;
	}

	/* 'Tipos de objetos */
	static public class ObjData {
		public String Name;

		public eOBJType OBJType;

		public int GrhIndex;
		public int GrhSecundario;

		public int MaxItems;
		public Inventario Conte;
		public int Apunala;
		public int Acuchilla;

		public int HechizoIndex;

		public String ForoID;

		public int MinHp;
		public int MaxHp;

		public int MineralIndex;
		public int LingoteInex;

		public int proyectil;
		public int Municion;

		public int Crucial;
		public int Newbie;

		public int MinSta;

		public int TipoPocion;
		public int MaxModificador;
		public int MinModificador;
		public int DuracionEfecto;
		public int MinSkill;
		public int LingoteIndex;

		public int MinHIT;
		public int MaxHIT;

		public int MinHam;
		public int MinSed;

		public int def;
		public int MinDef;
		public int MaxDef;

		public int Ropaje;

		public int WeaponAnim;
		public int WeaponRazaEnanaAnim;
		public int ShieldAnim;
		public int CascoAnim;

		public int Valor;

		public int Cerrada;
		public int Llave;
		public int clave;

		public int Radio;

		public int MochilaType;

		public int Guante;

		public int IndexAbierta;
		public int IndexCerrada;
		public int IndexCerradaLlave;

		public int RazaEnana;
		public int RazaDrow;
		public int RazaElfa;
		public int RazaGnoma;
		public int RazaHumana;

		public int Mujer;
		public int Hombre;

		public int Envenena;
		public int Paraliza;

		public int Agarrable;

		public int LingH;
		public int LingO;
		public int LingP;
		public int Madera;
		public int MaderaElfica;

		public int SkHerreria;
		public int SkCarpinteria;

		public String texto;

		public eClass[] ClaseProhibida;

		public int Snd1;
		public int Snd2;
		public int Snd3;

		public int Real;
		public int Caos;

		public int NoSeCae;
		public int NoSeTira;
		public int NoRobable;
		public int NoComerciable;
		public int Intransferible;

		public int StaffPower;
		public int StaffDamageBonus;
		public int DefensaMagicaMax;
		public int DefensaMagicaMin;
		public int Refuerzo;

		public int ImpideParalizar;
		public int ImpideInmobilizar;
		public int ImpideAturdir;
		public int ImpideCegar;

		public int Log;
		public int NoLog;

		public int Upgrade;
	}

	static public class Obj {
		public int ObjIndex;
		public int Amount;
	}

	/* '[Pablo ToxicWaste] */
	static public class ModClase {
		public double Evasion;
		public double AtaqueArmas;
		public double AtaqueProyectiles;
		public double AtaqueWrestling;
		public double DanoArmas;
		public double DanoProyectiles;
		public double DanoWrestling;
		public double Escudo;
	}

	static public class ModRaza {
		public float Fuerza;
		public float Agilidad;
		public float Inteligencia;
		public float Carisma;
		public float Constitucion;
	}
	/* '[/Pablo ToxicWaste] */

	/* '[KEVIN] */
	/* 'Banco Objs */
	static final int MAX_BANCOINVENTORY_SLOTS = 40;
	/* '[/KEVIN] */

	/* '[KEVIN] */
	static public class BancoInventario {
		public UserOBJ[] Object;
		public int NroItems;
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
		public int NobleRep;
		public int BurguesRep;
		public int PlebeRep;
		public int LadronesRep;
		public int BandidoRep;
		public int AsesinoRep;
		public int Promedio;
	}

	/* 'Estadisticas de los usuarios */
	static public class UserStats {
		public int GLD;
		public int Banco;

		public int MaxHp;
		public int MinHp;

		public int MaxSta;
		public int MinSta;
		public int MaxMAN;
		public int MinMAN;
		public int MaxHIT;
		public int MinHIT;

		public int MaxHam;
		public int MinHam;

		public int MaxAGU;
		public int MinAGU;

		public int def;
		public double Exp;
		public int ELV;
		public int ELU;
		public int[] UserSkills;
		public int[] UserAtributos;
		public int[] UserAtributosBackUP;
		public int[] UserHechizos;
		public int UsuariosMatados;
		public int CriminalesMatados;
		public int NPCsMuertos;

		public int SkillPts;

		public int[] ExpSkills;
		public int[] EluSkills;

	}

	/* 'Flags */
	static public class UserFlags {
		public int Muerto;
		public int Escondido;
		public boolean Comerciando;
		public boolean UserLogged;
		public boolean Meditando;
		public String Descuento;
		public int Hambre;
		public int Sed;
		public int PuedeMoverse;
		public int TimerLanzarSpell;
		public int PuedeTrabajar;
		public int Envenenado;
		public int Paralizado;
		public int Inmovilizado;
		public int Estupidez;
		public int Ceguera;
		public int invisible;
		public int Maldicion;
		public int Bendicion;
		public int Oculto;
		public int Desnudo;
		public boolean Descansar;
		public int Hechizo;
		public boolean TomoPocion;
		public int TipoPocion;

		public boolean NoPuedeSerAtacado;
		public int AtacablePor;
		public int ShareNpcWith;

		public int Vuela;
		public int Navegando;
		public boolean Seguro;
		public boolean SeguroResu;

		public int DuracionEfecto;
		public int TargetNPC;
		public eNPCType TargetNpcTipo;
		public int OwnedNpc;
		public int NpcInv;

		public int Ban;
		public int AdministrativeBan;

		public int TargetUser;

		public int TargetObj;
		public int TargetObjMap;
		public int TargetObjX;
		public int TargetObjY;

		public int TargetMap;
		public int TargetX;
		public int TargetY;

		public int TargetObjInvIndex;
		public int TargetObjInvSlot;

		public int AtacadoPorNpc;
		public int AtacadoPorUser;
		public int NPCAtacado;
		public boolean Ignorado;

		public boolean EnConsulta;
		public boolean SendDenounces;

		public int StatsChanged;
		public PlayerType Privilegios;
		public boolean PrivEspecial;

		public int ValCoDe;

		public String LastCrimMatado;
		public String LastCiudMatado;

		public int OldBody;
		public int OldHead;
		public int AdminInvisible;
		public boolean AdminPerseguible;

		public int ChatColor;

		public String MD5Reportado;

		public int TimesWalk;
		public int StartWalk;
		public int CountSH;

		public int UltimoMensaje;

		public int Silenciado;

		public int Mimetizado;

		public int CentinelaIndex;
		public boolean CentinelaOK;

		public int lastMap;
		public int Traveling;

		public String ParalizedBy;
		public int ParalizedByIndex;
		public int ParalizedByNpcIndex;

		/* # IF SeguridadAlkon THEN */
		/* # END IF */
	}

	static public class UserCounters {
		public int IdleCount;
		public int AttackCounter;
		public int HPCounter;
		public int STACounter;
		public int Frio;
		public int Lava;
		public int COMCounter;
		public int AGUACounter;
		public int Veneno;
		public int Paralisis;
		public int Ceguera;
		public int Estupidez;

		public int Invisibilidad;
		public int TiempoOculto;

		public int Mimetismo;
		public int PiqueteC;
		public int Pena;
		public WorldPos SendMapCounter;
		public boolean Saliendo;
		public int Salir;

		public int tInicioMeditar;
		public boolean bPuedeMeditar;

		public int TimerLanzarSpell;
		public int TimerPuedeAtacar;
		public int TimerPuedeUsarArco;
		public int TimerPuedeTrabajar;
		public int TimerUsar;
		public int TimerMagiaGolpe;
		public int TimerGolpeMagia;
		public int TimerGolpeUsar;
		public int TimerPuedeSerAtacado;
		public int TimerPerteneceNpc;
		public int TimerEstadoAtacable;

		public int Trabajando;
		public int Ocultando;

		public int failedUsageAttempts;

		public int goHome;
		public int AsignedSkills;
		/* # IF SeguridadAlkon THEN */
		/* # END IF */
	}

	/* 'Cosas faccionarias. */
	static public class tFacciones {
		public int ArmadaReal;
		public int FuerzasCaos;
		public int CriminalesMatados;
		public int CiudadanosMatados;
		public int RecompensasReal;
		public int RecompensasCaos;
		public int RecibioExpInicialReal;
		public int RecibioExpInicialCaos;
		public int RecibioArmaduraReal;
		public int RecibioArmaduraCaos;
		public int Reenlistadas;
		public int NivelIngreso;
		public String FechaIngreso;
		public int MatadosIngreso;
		public int NextRecompensa;
	}

	static public class tCrafting {
		public int Cantidad;
		public int PorCiclo;
	}

	/* 'Tipo de los Usuarios */
	static public class User {
		public String Name;
		public int ID;

		public boolean showName;

		public Char Char;
		public Char CharMimetizado;
		public Char OrigChar;

		public String desc;
		public String DescRM;

		public eClass clase;
		public eRaza raza;
		public eGenero Genero;
		public String email;
		public eCiudad Hogar;

		public Inventario Invent;

		public WorldPos Pos;

		public boolean ConnIDValida;
		public int ConnID;

		public BancoInventario BancoInvent;

		public UserCounters Counters;

		public tCrafting Construir;

		public int[] MascotasIndex;
		public int[] MascotasType;
		public int NroMascotas;

		public UserStats Stats;
		public UserFlags flags;

		public tReputacion Reputacion;

		public tFacciones Faccion;

		/* # IF SeguridadAlkon THEN */
		/* # END IF */

		/* # IF ConUpTime THEN */
		public vb6.Date LogOnTime;
		public int UpTime;
		/* # END IF */

		public String ip;

		public mdlCOmercioConUsuario.tCOmercioUsuario ComUsu;

		public int GuildIndex;
		public ALINEACION_GUILD FundandoGuildAlineacion;
		public int EscucheClan;

		public int PartyIndex;
		public int PartySolicitud;

		public int KeyCrypt;

		public ModAreas.AreaInfo AreasInfo;

		public clsByteQueue outgoingData;
		public clsByteQueue incomingData;

		public int CurrentInventorySlots;
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
		public int Alineacion;
		public int MaxHp;
		public int MinHp;
		public int MaxHIT;
		public int MinHIT;
		public int def;
		public int defM;
	}

	static public class NpcCounters {
		public int Paralisis;
		public int TiempoExistencia;
	}

	static public class NPCFlags {
		public int AfectaParalisis;
		public int Domable;
		public int Respawn;
		public boolean NPCActive;
		public boolean Follow;
		public int Faccion;
		public int AtacaDoble;
		public int LanzaSpells;

		public int ExpCount;

		public TipoAI OldMovement;
		public int OldHostil;

		public int AguaValida;
		public int TierraInvalida;

		public int Sound;
		public String AttackedBy;
		public String AttackedFirstBy;
		public int BackUp;
		public int RespawnOrigPos;

		public int Envenenado;
		public int Paralizado;
		public int Inmovilizado;
		public int invisible;
		public int Maldicion;
		public int Bendicion;

		public int Snd1;
		public int Snd2;
		public int Snd3;
	}

	static public class tCriaturasEntrenador {
		public int NpcIndex;
		public String NpcName;
		public int tmpIndex;
	}

	/* ' New type for holding the pathfinding info */
	static public class NpcPathFindingInfo {
		public Queue.tVertice[] Path;
		public Position Target;
		public int PathLenght;
		public int CurPos;
		public int TargetUser;
		public boolean NoPath;

	}
	/* ' New type for holding the pathfinding info */

	static public class tDrops {
		public int ObjIndex;
		public int Amount;
	}

	static final int MAX_NPC_DROPS = 5;

	static public class npc {
		public String Name;
		public Char Char;
		public String desc;

		public eNPCType NPCtype;
		public int Numero;

		public int InvReSpawn;

		public int Comercia;
		public int Target;
		public int TargetNPC;
		public int TipoItems;

		public int Veneno;

		public WorldPos Pos;
		public WorldPos Orig;
		public int SkillDomar;

		public TipoAI Movement;
		public int Attackable;
		public int Hostile;
		public int PoderAtaque;
		public int PoderEvasion;

		public int Owner;

		public int GiveEXP;
		public int GiveGLD;
		public tDrops[] Drop;

		public NPCStats Stats;
		public NPCFlags flags;
		public NpcCounters Contadores;

		public Inventario Invent;
		public int CanAttack;

		public int NroExpresiones;
		public String[] Expresiones;

		public int NroSpells;
		public int[] Spells;

		public int NroCriaturas;
		public tCriaturasEntrenador[] Criaturas;
		public int MaestroUser;
		public int MaestroNpc;
		public int Mascotas;

		public NpcPathFindingInfo PFINFO;
		public ModAreas.AreaInfo AreasInfo;

		public int Ciudad;

		public int ClanIndex;
	}

	/* '********************************************************** */
	/* '********************************************************** */
	/* '******************** Tipos del mapa ********************** */
	/* '********************************************************** */
	/* '********************************************************** */
	/* 'Tile */
	static public class MapBlock {
		public int Blocked;
		public int[] Graphic;
		public int UserIndex;
		public int NpcIndex;
		public Obj ObjInfo;
		public WorldPos TileExit;
		public eTrigger trigger;
	}

	/* 'Info del mapa */
	static public class MapInfo {
		public int NumUsers;
		public String Music;
		public String Name;
		public WorldPos StartPos;
		public WorldPos OnDeathGoTo;

		public int MapVersion;
		public boolean Pk;
		public int MagiaSinEfecto;
		public int NoEncriptarMP;

		public int InviSinEfecto;
		public int ResuSinEfecto;
		public int OcultarSinEfecto;
		public int InvocarSinEfecto;

		public int RoboNpcsPermitido;

		public String Terreno;
		public String Zona;
		public int Restringir;
		public int BackUp;
	}

	/* '********** V A R I A B L E S P U B L I C A S *********** */

	public static boolean SERVERONLINE;
	public static String ULTIMAVERSION;
	/* ' TODO: Se usa esta variable ? */
	public static boolean BackUp;

	public static String[] ListaRazas = new String[1 + Declaraciones.NUMRAZAS];
	public static String[] SkillsNames = new String[1 + Declaraciones.NUMSKILLS];
	public static String[] ListaClases = new String[1 + Declaraciones.NUMCLASES];
	public static String[] ListaAtributos = new String[1 + Declaraciones.NUMATRIBUTOS];

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
	public static float[] HappyHourDays = new float[1 + 7];

	/* '' */
	/* 'Esta activada la verificacion MD5 ? */
	public static int MD5ClientesActivado;

	public static boolean EnPausa;
	public static boolean EnTesting;

	/* '*****************ARRAYS PUBLICOS************************* */
	/* 'USUARIOS */
	public static User[] UserList = new User[0];
	/* 'NPCS */
	public static npc[] Npclist = new npc[1 + Declaraciones.MAXNPCS];
	public static MapBlock[][][] MapData = new MapBlock[0][0][0];
	public static MapInfo[] MapInfo = new MapInfo[0];
	public static tHechizo[] Hechizos = new tHechizo[0];
	public static int[] CharList = new int[1 + Declaraciones.MAXCHARS];
	public static ObjData[] ObjData = new ObjData[0];
	public static FXdata[] FX = new FXdata[0];
	public static tCriaturasEntrenador[] SpawnList = new tCriaturasEntrenador[0];
	public static LevelSkill[] LevelSkill = new LevelSkill[1 + 50];
	public static String[] ForbidenNames = new String[0];
	public static int[] ArmasHerrero = new int[0];
	public static int[] ArmadurasHerrero = new int[0];
	public static int[] ObjCarpintero = new int[0];
	public static String[] MD5s = new String[vb6.nline.dakaraserver.Collection BanIps;
	public static clsParty[] Parties = new clsParty[1 + mdParty.MAX_PARTIES];
	public static ModClase[] ModClase = new ModClase[1 + Declaraciones.NUMCLASES];
	public static ModRaza[] ModRaza = new ModRaza[1 + Declaraciones.NUMRAZAS];
	public static double[] ModVida = new double[1 + Declaraciones.NUMCLASES];
	public static int[] DistribucionEnteraVida = new int[1 + 5];
	public static int[] DistribucionSemienteraVida = new int[1 + 4];
	public static WorldPos[] Ciudades = new WorldPos[1 + Declaraciones.NUMCIUDADES];
	public static HomeDistance[] distanceToCities = new HomeDistance[0];
	public static tRecord[] Records = new tRecord[0];
	/* '********************************************************* */

	static public class HomeDistance {
		public int[] distanceToCity;
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
	public static clsClanPretoriano[] ClanPretoriano = new clsClanPretoriano[0];

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