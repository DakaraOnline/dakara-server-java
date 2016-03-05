/*  AUTOMATICALLY CONVERTED FILE  */
/* [(0, 'ATTRIBUTE'), (1, 'VB_Name'), (5, '='), (4, '"Protocol"')] */
/* '************************************************************** */
/* ' Protocol.bas - Handles all incoming / outgoing messages for client-server communications. */
/* ' Uses a binary protocol designed by myself. */
/* ' */
/* ' Designed and implemented by Juan Martín Sotuyo Dodero (Maraxus) */
/* ' (juansotuyo@gmail.com) */
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

/* '' */
/* 'Handles all incoming / outgoing packets for client - server communications */
/* 'The binary prtocol here used was designed by Juan Martín Sotuyo Dodero. */
/* 'This is the first time it's used in Alkon, though the second time it's coded. */
/* 'This implementation has several enhacements from the first design. */
/* ' */
/* ' @author Juan Martín Sotuyo Dodero (Maraxus) juansotuyo@gmail.com */
/* ' @version 1.0.0 */
/* ' @date 20060517 */

import enums.*;

public class Protocol {

	/* '' */
	/*
	 * 'When we have a list of strings, we use this to separate them and prevent
	 */
	/*
	 * 'having too many string lengths in the queue. Yes, each string is
	 * NULL-terminated :P
	 */
	/* FIXME: FIXED DATATYPE SIZE: CONST SEPARATOR AS String * 1 = vbNullChar */
	static final String SEPARATOR = "\0";

	/* '' */
	/*
	 * 'Auxiliar ByteQueue used as buffer to generate messages not intended to
	 * be sent right away.
	 */
	/*
	 * 'Specially usefull to create a message once and send it over to several
	 * clients.
	 */
	private static clsByteQueue auxiliarBuffer;

	/* # IF SeguridadAlkon = 0 THEN */

	/* # END IF */
	/* '' */
	/* 'The last existing client packet id. */
	static final int LAST_CLIENT_PACKET_ID = 129;

	static void InitAuxiliarBuffer() {
		/* '*************************************************** */
		/* 'Author: ZaMa */
		/* 'Last Modification: 15/03/2011 */
		/* 'Initializaes Auxiliar Buffer */
		/* '*************************************************** */
		auxiliarBuffer = new clsByteQueue();
	}

	/* '' */
	/* ' Handles incoming data. */
	/* ' */
	/* ' @param userIndex The index of the user sending the message. */

	static void HandleIncomingData(int UserIndex) {
		/* '*************************************************** */
		/* 'Author: Juan Martín Sotuyo Dodero (Maraxus) */
		/* 'Last Modification: 01/09/07 */
		/* ' */
		/* '*************************************************** */
		/* FIXME: ON ERROR RESUME NEXT */
		int packetID;

		packetID = Declaraciones.UserList[UserIndex].incomingData.PeekByte();

		/* 'Does the packet requires a logged user?? */
		if (!(packetID == ClientPacketID.ThrowDices || packetID == ClientPacketID.LoginExistingChar
				|| packetID == ClientPacketID.LoginNewChar)) {

			/* 'Is the user actually logged? */
			if (!Declaraciones.UserList[UserIndex].flags.UserLogged) {
				TCP.CloseSocket(UserIndex);
				return;

				/* 'He is logged. Reset idle counter if id is valid. */
			} else if (packetID <= Protocol.LAST_CLIENT_PACKET_ID) {
				Declaraciones.UserList[UserIndex].Counters.IdleCount = 0;
			}
		} else if (packetID <= Protocol.LAST_CLIENT_PACKET_ID) {
			Declaraciones.UserList[UserIndex].Counters.IdleCount = 0;

			/* 'Is the user logged? */
			if (Declaraciones.UserList[UserIndex].flags.UserLogged) {
				TCP.CloseSocket(UserIndex);
				return;
			}
		}

		/* ' Ante cualquier paquete, pierde la proteccion de ser atacado. */
		Declaraciones.UserList[UserIndex].flags.NoPuedeSerAtacado = false;

		switch (packetID) {
		/* 'OLOGIN */
		case LoginExistingChar:
			HandleLoginExistingChar(UserIndex);

			/* 'TIRDAD */
			break;

		case ThrowDices:
			HandleThrowDices(UserIndex);

			/* 'NLOGIN */
			break;

		case LoginNewChar:
			HandleLoginNewChar(UserIndex);

			/* '; */
			break;

		case Talk:
			HandleTalk(UserIndex);

			/* '- */
			break;

		case Yell:
			HandleYell(UserIndex);

			/* '\ */
			break;

		case Whisper:
			HandleWhisper(UserIndex);

			/* 'M */
			break;

		case Walk:
			HandleWalk(UserIndex);

			/* 'RPU */
			break;

		case RequestPositionUpdate:
			HandleRequestPositionUpdate(UserIndex);

			/* 'AT */
			break;

		case Attack:
			HandleAttack(UserIndex);

			/* 'AG */
			break;

		case PickUp:
			HandlePickUp(UserIndex);

			/* '/SEG & SEG (SEG's behaviour has to be coded in the client) */
			break;

		case SafeToggle:
			HandleSafeToggle(UserIndex);

			break;

		case ResuscitationSafeToggle:
			HandleResuscitationToggle(UserIndex);

			/* 'GLINFO */
			break;

		case RequestGuildLeaderInfo:
			HandleRequestGuildLeaderInfo(UserIndex);

			/* 'ATR */
			break;

		case RequestAtributes:
			HandleRequestAtributes(UserIndex);

			/* 'FAMA */
			break;

		case RequestFame:
			HandleRequestFame(UserIndex);

			/* 'ESKI */
			break;

		case RequestSkills:
			HandleRequestSkills(UserIndex);

			/* 'FEST */
			break;

		case RequestMiniStats:
			HandleRequestMiniStats(UserIndex);

			/* 'FINCOM */
			break;

		case CommerceEnd:
			HandleCommerceEnd(UserIndex);

			break;

		case CommerceChat:
			HandleCommerceChat(UserIndex);

			/* 'FINCOMUSU */
			break;

		case UserCommerceEnd:
			HandleUserCommerceEnd(UserIndex);

			break;

		case UserCommerceConfirm:
			HandleUserCommerceConfirm(UserIndex);

			/* 'FINBAN */
			break;

		case BankEnd:
			HandleBankEnd(UserIndex);

			/* 'COMUSUOK */
			break;

		case UserCommerceOk:
			HandleUserCommerceOk(UserIndex);

			/* 'COMUSUNO */
			break;

		case UserCommerceReject:
			HandleUserCommerceReject(UserIndex);

			/* 'TI */
			break;

		case Drop:
			HandleDrop(UserIndex);

			/* 'LH */
			break;

		case CastSpell:
			HandleCastSpell(UserIndex);

			/* 'LC */
			break;

		case LeftClick:
			HandleLeftClick(UserIndex);

			/* 'RC */
			break;

		case DoubleClick:
			HandleDoubleClick(UserIndex);

			/* 'UK */
			break;

		case Work:
			HandleWork(UserIndex);

			/* 'UMH */
			break;

		case UseSpellMacro:
			HandleUseSpellMacro(UserIndex);

			/* 'USA */
			break;

		case UseItem:
			HandleUseItem(UserIndex);

			/* 'CNS */
			break;

		case CraftBlacksmith:
			HandleCraftBlacksmith(UserIndex);

			/* 'CNC */
			break;

		case CraftCarpenter:
			HandleCraftCarpenter(UserIndex);

			/* 'WLC */
			break;

		case WorkLeftClick:
			HandleWorkLeftClick(UserIndex);

			/* 'CIG */
			break;

		case CreateNewGuild:
			HandleCreateNewGuild(UserIndex);

			/* 'INFS */
			break;

		case SpellInfo:
			HandleSpellInfo(UserIndex);

			/* 'EQUI */
			break;

		case EquipItem:
			HandleEquipItem(UserIndex);

			/* 'CHEA */
			break;

		case ChangeHeading:
			HandleChangeHeading(UserIndex);

			/* 'SKSE */
			break;

		case ModifySkills:
			HandleModifySkills(UserIndex);

			/* 'ENTR */
			break;

		case Train:
			HandleTrain(UserIndex);

			/* 'COMP */
			break;

		case CommerceBuy:
			HandleCommerceBuy(UserIndex);

			/* 'RETI */
			break;

		case BankExtractItem:
			HandleBankExtractItem(UserIndex);

			/* 'VEND */
			break;

		case CommerceSell:
			HandleCommerceSell(UserIndex);

			/* 'DEPO */
			break;

		case BankDeposit:
			HandleBankDeposit(UserIndex);

			/* 'DEMSG */
			break;

		case ForumPost:
			HandleForumPost(UserIndex);

			/* 'DESPHE */
			break;

		case MoveSpell:
			HandleMoveSpell(UserIndex);

			break;

		case MoveBank:
			HandleMoveBank(UserIndex);

			/* 'DESCOD */
			break;

		case ClanCodexUpdate:
			HandleClanCodexUpdate(UserIndex);

			/* 'OFRECER */
			break;

		case UserCommerceOffer:
			HandleUserCommerceOffer(UserIndex);

			/* 'ACEPPEAT */
			break;

		case GuildAcceptPeace:
			HandleGuildAcceptPeace(UserIndex);

			/* 'RECPALIA */
			break;

		case GuildRejectAlliance:
			HandleGuildRejectAlliance(UserIndex);

			/* 'RECPPEAT */
			break;

		case GuildRejectPeace:
			HandleGuildRejectPeace(UserIndex);

			/* 'ACEPALIA */
			break;

		case GuildAcceptAlliance:
			HandleGuildAcceptAlliance(UserIndex);

			/* 'PEACEOFF */
			break;

		case GuildOfferPeace:
			HandleGuildOfferPeace(UserIndex);

			/* 'ALLIEOFF */
			break;

		case GuildOfferAlliance:
			HandleGuildOfferAlliance(UserIndex);

			/* 'ALLIEDET */
			break;

		case GuildAllianceDetails:
			HandleGuildAllianceDetails(UserIndex);

			/* 'PEACEDET */
			break;

		case GuildPeaceDetails:
			HandleGuildPeaceDetails(UserIndex);

			/* 'ENVCOMEN */
			break;

		case GuildRequestJoinerInfo:
			HandleGuildRequestJoinerInfo(UserIndex);

			/* 'ENVALPRO */
			break;

		case GuildAlliancePropList:
			HandleGuildAlliancePropList(UserIndex);

			/* 'ENVPROPP */
			break;

		case GuildPeacePropList:
			HandleGuildPeacePropList(UserIndex);

			/* 'DECGUERR */
			break;

		case GuildDeclareWar:
			HandleGuildDeclareWar(UserIndex);

			/* 'NEWWEBSI */
			break;

		case GuildNewWebsite:
			HandleGuildNewWebsite(UserIndex);

			/* 'ACEPTARI */
			break;

		case GuildAcceptNewMember:
			HandleGuildAcceptNewMember(UserIndex);

			/* 'RECHAZAR */
			break;

		case GuildRejectNewMember:
			HandleGuildRejectNewMember(UserIndex);

			/* 'ECHARCLA */
			break;

		case GuildKickMember:
			HandleGuildKickMember(UserIndex);

			/* 'ACTGNEWS */
			break;

		case GuildUpdateNews:
			HandleGuildUpdateNews(UserIndex);

			/* '1HRINFO< */
			break;

		case GuildMemberInfo:
			HandleGuildMemberInfo(UserIndex);

			/* 'ABREELEC */
			break;

		case GuildOpenElections:
			HandleGuildOpenElections(UserIndex);

			/* 'SOLICITUD */
			break;

		case GuildRequestMembership:
			HandleGuildRequestMembership(UserIndex);

			/* 'CLANDETAILS */
			break;

		case GuildRequestDetails:
			HandleGuildRequestDetails(UserIndex);

			/* '/ONLINE */
			break;

		case Online:
			HandleOnline(UserIndex);

			/* '/SALIR */
			break;

		case Quit:
			HandleQuit(UserIndex);

			/* '/SALIRCLAN */
			break;

		case GuildLeave:
			HandleGuildLeave(UserIndex);

			/* '/BALANCE */
			break;

		case RequestAccountState:
			HandleRequestAccountState(UserIndex);

			/* '/QUIETO */
			break;

		case PetStand:
			HandlePetStand(UserIndex);

			/* '/ACOMPANAR */
			break;

		case PetFollow:
			HandlePetFollow(UserIndex);

			/* '/LIBERAR */
			break;

		case ReleasePet:
			HandleReleasePet(UserIndex);

			/* '/ENTRENAR */
			break;

		case TrainList:
			HandleTrainList(UserIndex);

			/* '/DESCANSAR */
			break;

		case Rest:
			HandleRest(UserIndex);

			/* '/MEDITAR */
			break;

		case Meditate:
			HandleMeditate(UserIndex);

			/* '/RESUCITAR */
			break;

		case Resucitate:
			HandleResucitate(UserIndex);

			/* '/CURAR */
			break;

		case Heal:
			HandleHeal(UserIndex);

			/* '/AYUDA */
			break;

		case Help:
			HandleHelp(UserIndex);

			/* '/EST */
			break;

		case RequestStats:
			HandleRequestStats(UserIndex);

			/* '/COMERCIAR */
			break;

		case CommerceStart:
			HandleCommerceStart(UserIndex);

			/* '/BOVEDA */
			break;

		case BankStart:
			HandleBankStart(UserIndex);

			/* '/ENLISTAR */
			break;

		case Enlist:
			HandleEnlist(UserIndex);

			/* '/INFORMACION */
			break;

		case Information:
			HandleInformation(UserIndex);

			/* '/RECOMPENSA */
			break;

		case Reward:
			HandleReward(UserIndex);

			/* '/MOTD */
			break;

		case RequestMOTD:
			HandleRequestMOTD(UserIndex);

			/* '/UPTIME */
			break;

		case UpTime:
			HandleUpTime(UserIndex);

			/* '/SALIRPARTY */
			break;

		case PartyLeave:
			HandlePartyLeave(UserIndex);

			/* '/CREARPARTY */
			break;

		case PartyCreate:
			HandlePartyCreate(UserIndex);

			/* '/PARTY */
			break;

		case PartyJoin:
			HandlePartyJoin(UserIndex);

			/* '/ENCUESTA ( with no params ) */
			break;

		case Inquiry:
			HandleInquiry(UserIndex);

			/* '/CMSG */
			break;

		case GuildMessage:
			HandleGuildMessage(UserIndex);

			/* '/PMSG */
			break;

		case PartyMessage:
			HandlePartyMessage(UserIndex);

			/* '/CENTINELA */
			break;

		case CentinelReport:
			HandleCentinelReport(UserIndex);

			/* '/ONLINECLAN */
			break;

		case GuildOnline:
			HandleGuildOnline(UserIndex);

			/* '/ONLINEPARTY */
			break;

		case PartyOnline:
			HandlePartyOnline(UserIndex);

			/* '/BMSG */
			break;

		case CouncilMessage:
			HandleCouncilMessage(UserIndex);

			/* '/ROL */
			break;

		case RoleMasterRequest:
			HandleRoleMasterRequest(UserIndex);

			/* '/GM */
			break;

		case GMRequest:
			HandleGMRequest(UserIndex);

			/* '/_BUG */
			break;

		case bugReport:
			HandleBugReport(UserIndex);

			/* '/DESC */
			break;

		case ChangeDescription:
			HandleChangeDescription(UserIndex);

			/* '/VOTO */
			break;

		case GuildVote:
			HandleGuildVote(UserIndex);

			/* '/PENAS */
			break;

		case Punishments:
			HandlePunishments(UserIndex);

			/* '/CONTRASENA */
			break;

		case ChangePassword:
			HandleChangePassword(UserIndex);

			/* '/APOSTAR */
			break;

		case Gamble:
			HandleGamble(UserIndex);

			/* '/ENCUESTA ( with parameters ) */
			break;

		case InquiryVote:
			HandleInquiryVote(UserIndex);

			/* '/RETIRAR ( with no arguments ) */
			break;

		case LeaveFaction:
			HandleLeaveFaction(UserIndex);

			/* '/RETIRAR ( with arguments ) */
			break;

		case BankExtractGold:
			HandleBankExtractGold(UserIndex);

			/* '/DEPOSITAR */
			break;

		case BankDepositGold:
			HandleBankDepositGold(UserIndex);

			/* '/DENUNCIAR */
			break;

		case Denounce:
			HandleDenounce(UserIndex);

			/* '/FUNDARCLAN */
			break;

		case GuildFundate:
			HandleGuildFundate(UserIndex);

			break;

		case GuildFundation:
			HandleGuildFundation(UserIndex);

			/* '/ECHARPARTY */
			break;

		case PartyKick:
			HandlePartyKick(UserIndex);

			/* '/PARTYLIDER */
			break;

		case PartySetLeader:
			HandlePartySetLeader(UserIndex);

			/* '/ACCEPTPARTY */
			break;

		case PartyAcceptMember:
			HandlePartyAcceptMember(UserIndex);

			/* '/PING */
			break;

		case Ping:
			HandlePing(UserIndex);

			break;

		case RequestPartyForm:
			HandlePartyForm(UserIndex);

			break;

		case ItemUpgrade:
			HandleItemUpgrade(UserIndex);

			/* 'GM Messages */
			break;

		case GMCommands:
			HandleGMCommands(UserIndex);

			break;

		case InitCrafting:
			HandleInitCrafting(UserIndex);

			break;

		case Home:
			HandleHome(UserIndex);

			break;

		case ShowGuildNews:
			HandleShowGuildNews(UserIndex);

			break;

		case ShareNpc:
			HandleShareNpc(UserIndex);

			break;

		case StopSharingNpc:
			HandleStopSharingNpc(UserIndex);

			break;

		case Consultation:
			HandleConsultation(UserIndex);

			break;

		case moveItem:
			HandleMoveItem(UserIndex);

			/* # IF SeguridadAlkon THEN */
			/* # ELSE */
			break;

		default:
			/* 'ERROR : Abort! */
			TCP.CloseSocket(UserIndex);
			/* # END IF */
			break;
		}

		/*
		 * 'Done with this packet, move on to next one or send everything if no
		 * more packets found
		 */
		if (Declaraciones.UserList[UserIndex].incomingData.length > 0 && Err.Number == 0) {
			/* FIXME: Err . Clear */
			HandleIncomingData(UserIndex);

		} else if (Err.Number != 0
				&& !Err.Number == Declaraciones.UserList[UserIndex].incomingData.NotEnoughDataErrCode) {
			/* 'An error ocurred, log it and kick player. */
			General.LogError("Error: " + Err.Number + " [" + Err.description + "] " + " Source: " + Err.source + vbTab
					+ " HelpFile: " + Err.HelpFile + vbTab + " HelpContext: " + Err.HelpContext + vbTab
					+ " LastDllError: " + Err.LastDllError + vbTab + " - UserIndex: " + UserIndex + "("
					+ Declaraciones.UserList[UserIndex].Name + ") - producido al manejar el paquete: "
					+ vb6.CStr(packetID));
			TCP.CloseSocket(UserIndex);

		} else {
			/* 'Flush buffer - send everything that has been written */
			FlushBuffer(UserIndex);
		}
	}

	static void WriteMultiMessage(int UserIndex, int MessageIndex) {
 WriteMultiMessage(UserIndex, MessageIndex, int(), int(), int(), String());
 }

	static void WriteMultiMessage(int UserIndex, int MessageIndex, int Arg1, int Arg2, int Arg3, String StringArg1) {
		/* '*************************************************** */
		/* 'Author: Unknown */
		/* 'Last Modification: - */
		/* ' */
		/* '*************************************************** */

		/* FIXME: ON ERROR GOTO ErrHandler */
		Declaraciones.UserList[UserIndex].outgoingData.WriteByte(ServerPacketID.MultiMessage);
		Declaraciones.UserList[UserIndex].outgoingData.WriteByte(MessageIndex);

		switch (MessageIndex) {
		case DontSeeAnything:
		case eMessages.NPCSwing:
		case eMessages.NPCKillUser:
		case eMessages.BlockedWithShieldUser:
		case eMessages.BlockedWithShieldother:
		case eMessages.UserSwing:
		case eMessages.SafeModeOn:
		case eMessages.SafeModeOff:
		case eMessages.ResuscitationSafeOff:
		case eMessages.ResuscitationSafeOn:
		case eMessages.NobilityLost:
		case eMessages.CantUseWhileMeditating:
		case eMessages.CancelHome:
		case eMessages.FinishHome:

			break;

		case NPCHitUser:
			/* 'Target */
			Declaraciones.UserList[UserIndex].outgoingData.WriteByte(Arg1);
			/* 'damage */
			Declaraciones.UserList[UserIndex].outgoingData.WriteInteger(Arg2);

			break;

		case UserHitNPC:
			/* 'damage */
			Declaraciones.UserList[UserIndex].outgoingData.WriteLong(Arg1);

			break;

		case UserAttackedSwing:
			Declaraciones.UserList[UserIndex].outgoingData.WriteInteger(Declaraciones.UserList[Arg1].Char.CharIndex);

			break;

		case UserHittedByUser:
			/* 'AttackerIndex */
			Declaraciones.UserList[UserIndex].outgoingData.WriteInteger(Arg1);
			/* 'Target */
			Declaraciones.UserList[UserIndex].outgoingData.WriteByte(Arg2);
			/* 'damage */
			Declaraciones.UserList[UserIndex].outgoingData.WriteInteger(Arg3);

			break;

		case UserHittedUser:
			/* 'AttackerIndex */
			Declaraciones.UserList[UserIndex].outgoingData.WriteInteger(Arg1);
			/* 'Target */
			Declaraciones.UserList[UserIndex].outgoingData.WriteByte(Arg2);
			/* 'damage */
			Declaraciones.UserList[UserIndex].outgoingData.WriteInteger(Arg3);

			break;

		case WorkRequestTarget:
			/* 'skill */
			Declaraciones.UserList[UserIndex].outgoingData.WriteByte(Arg1);

			/*
			 * '"Has matado a " & UserList(VictimIndex).name & "!" "Has ganado "
			 * & DaExp & " puntos de experiencia."
			 */
			break;

		case HaveKilledUser:
			/* 'VictimIndex */
			Declaraciones.UserList[UserIndex].outgoingData.WriteInteger(Declaraciones.UserList[Arg1].Char.CharIndex);
			/* 'Expe */
			Declaraciones.UserList[UserIndex].outgoingData.WriteLong(Arg2);

			/* '"¡" & .name & " te ha matado!" */
			break;

		case UserKill:
			/* 'AttackerIndex */
			Declaraciones.UserList[UserIndex].outgoingData.WriteInteger(Declaraciones.UserList[Arg1].Char.CharIndex);

			break;

		case EarnExp:

			break;

		case Home:
			Declaraciones.UserList[UserIndex].outgoingData.WriteByte(vb6.CByte(Arg1));
			Declaraciones.UserList[UserIndex].outgoingData.WriteInteger(vb6.CInt(Arg2));
			/*
			 * 'El cliente no conoce nada sobre nombre de mapas y hogares, por
			 * lo tanto _ hasta que no se pasen los dats e .INFs al cliente,
			 * esto queda así.
			 */
			/* 'Call .WriteByte(CByte(Arg2)) */
			Declaraciones.UserList[UserIndex].outgoingData.WriteASCIIString(StringArg1);

			break;
		}
		/* '' */
		return;

		/* FIXME: ErrHandler : */
		if (Err.Number == Declaraciones.UserList[UserIndex].outgoingData.NotEnoughSpaceErrCode) {
			FlushBuffer(UserIndex);
			/* FIXME: RESUME */
		}
	}

	static void HandleGMCommands(int UserIndex) {
		/* '*************************************************** */
		/* 'Author: Unknown */
		/* 'Last Modification: - */
		/* ' */
		/* '*************************************************** */

		/* FIXME: ON ERROR GOTO ErrHandler */

		int Command;

		Declaraciones.UserList[UserIndex].incomingData.ReadByte();

		Command = Declaraciones.UserList[UserIndex].incomingData.PeekByte();

		switch (Command) {
		/* '/GMSG */
		case GMMessage:
			HandleGMMessage(UserIndex);

			/* '/SHOWNAME */
			break;

		case showName:
			HandleShowName(UserIndex);

			break;

		case OnlineRoyalArmy:
			HandleOnlineRoyalArmy(UserIndex);

			/* '/ONLINECAOS */
			break;

		case OnlineChaosLegion:
			HandleOnlineChaosLegion(UserIndex);

			/* '/IRCERCA */
			break;

		case GoNearby:
			HandleGoNearby(UserIndex);

			/* '/REM */
			break;

		case comment:
			HandleComment(UserIndex);

			/* '/HORA */
			break;

		case serverTime:
			HandleServerTime(UserIndex);

			/* '/DONDE */
			break;

		case Where:
			HandleWhere(UserIndex);

			/* '/NENE */
			break;

		case CreaturesInMap:
			HandleCreaturesInMap(UserIndex);

			/* '/TELEPLOC */
			break;

		case WarpMeToTarget:
			HandleWarpMeToTarget(UserIndex);

			/* '/TELEP */
			break;

		case WarpChar:
			HandleWarpChar(UserIndex);

			/* '/SILENCIAR */
			break;

		case Silence:
			HandleSilence(UserIndex);

			/* '/SHOW SOS */
			break;

		case SOSShowList:
			HandleSOSShowList(UserIndex);

			/* 'SOSDONE */
			break;

		case SOSRemove:
			HandleSOSRemove(UserIndex);

			/* '/IRA */
			break;

		case GoToChar:
			HandleGoToChar(UserIndex);

			/* '/INVISIBLE */
			break;

		case invisible:
			HandleInvisible(UserIndex);

			/* '/PANELGM */
			break;

		case GMPanel:
			HandleGMPanel(UserIndex);

			/* 'LISTUSU */
			break;

		case RequestUserList:
			HandleRequestUserList(UserIndex);

			/* '/TRABAJANDO */
			break;

		case Working:
			HandleWorking(UserIndex);

			/* '/OCULTANDO */
			break;

		case Hiding:
			HandleHiding(UserIndex);

			/* '/CARCEL */
			break;

		case Jail:
			HandleJail(UserIndex);

			/* '/RMATA */
			break;

		case KillNPC:
			HandleKillNPC(UserIndex);

			/* '/ADVERTENCIA */
			break;

		case WarnUser:
			HandleWarnUser(UserIndex);

			/* '/MOD */
			break;

		case EditChar:
			HandleEditChar(UserIndex);

			/* '/INFO */
			break;

		case RequestCharInfo:
			HandleRequestCharInfo(UserIndex);

			/* '/STAT */
			break;

		case RequestCharStats:
			HandleRequestCharStats(UserIndex);

			/* '/BAL */
			break;

		case RequestCharGold:
			HandleRequestCharGold(UserIndex);

			/* '/INV */
			break;

		case RequestCharInventory:
			HandleRequestCharInventory(UserIndex);

			/* '/BOV */
			break;

		case RequestCharBank:
			HandleRequestCharBank(UserIndex);

			/* '/SKILLS */
			break;

		case RequestCharSkills:
			HandleRequestCharSkills(UserIndex);

			/* '/REVIVIR */
			break;

		case ReviveChar:
			HandleReviveChar(UserIndex);

			/* '/ONLINEGM */
			break;

		case OnlineGM:
			HandleOnlineGM(UserIndex);

			/* '/ONLINEMAP */
			break;

		case OnlineMap:
			HandleOnlineMap(UserIndex);

			/* '/PERDON */
			break;

		case Forgive:
			HandleForgive(UserIndex);

			/* '/ECHAR */
			break;

		case Kick:
			HandleKick(UserIndex);

			/* '/EJECUTAR */
			break;

		case Execute:
			HandleExecute(UserIndex);

			/* '/BAN */
			break;

		case BanChar:
			HandleBanChar(UserIndex);

			/* '/UNBAN */
			break;

		case UnbanChar:
			HandleUnbanChar(UserIndex);

			/* '/SEGUIR */
			break;

		case NPCFollow:
			HandleNPCFollow(UserIndex);

			/* '/SUM */
			break;

		case SummonChar:
			HandleSummonChar(UserIndex);

			/* '/CC */
			break;

		case SpawnListRequest:
			HandleSpawnListRequest(UserIndex);

			/* 'SPA */
			break;

		case SpawnCreature:
			HandleSpawnCreature(UserIndex);

			/* '/RESETINV */
			break;

		case ResetNPCInventory:
			HandleResetNPCInventory(UserIndex);

			/* '/LIMPIAR */
			break;

		case CleanWorld:
			HandleCleanWorld(UserIndex);

			/* '/RMSG */
			break;

		case ServerMessage:
			HandleServerMessage(UserIndex);

			/* '/MAPMSG */
			break;

		case MapMessage:
			HandleMapMessage(UserIndex);

			/* '/NICK2IP */
			break;

		case NickToIP:
			HandleNickToIP(UserIndex);

			/* '/IP2NICK */
			break;

		case IPToNick:
			HandleIPToNick(UserIndex);

			/* '/ONCLAN */
			break;

		case GuildOnlineMembers:
			HandleGuildOnlineMembers(UserIndex);

			/* '/CT */
			break;

		case TeleportCreate:
			HandleTeleportCreate(UserIndex);

			/* '/DT */
			break;

		case TeleportDestroy:
			HandleTeleportDestroy(UserIndex);

			/* '/LLUVIA */
			break;

		case RainToggle:
			HandleRainToggle(UserIndex);

			/* '/SETDESC */
			break;

		case SetCharDescription:
			HandleSetCharDescription(UserIndex);

			/* '/FORCEMIDIMAP */
			break;

		case ForceMIDIToMap:
			HanldeForceMIDIToMap(UserIndex);

			/* '/FORCEWAVMAP */
			break;

		case ForceWAVEToMap:
			HandleForceWAVEToMap(UserIndex);

			/* '/REALMSG */
			break;

		case RoyalArmyMessage:
			HandleRoyalArmyMessage(UserIndex);

			/* '/CAOSMSG */
			break;

		case ChaosLegionMessage:
			HandleChaosLegionMessage(UserIndex);

			/* '/CIUMSG */
			break;

		case CitizenMessage:
			HandleCitizenMessage(UserIndex);

			/* '/CRIMSG */
			break;

		case CriminalMessage:
			HandleCriminalMessage(UserIndex);

			/* '/TALKAS */
			break;

		case TalkAsNPC:
			HandleTalkAsNPC(UserIndex);

			/* '/MASSDEST */
			break;

		case DestroyAllItemsInArea:
			HandleDestroyAllItemsInArea(UserIndex);

			/* '/ACEPTCONSE */
			break;

		case AcceptRoyalCouncilMember:
			HandleAcceptRoyalCouncilMember(UserIndex);

			/* '/ACEPTCONSECAOS */
			break;

		case AcceptChaosCouncilMember:
			HandleAcceptChaosCouncilMember(UserIndex);

			/* '/PISO */
			break;

		case ItemsInTheFloor:
			HandleItemsInTheFloor(UserIndex);

			/* '/ESTUPIDO */
			break;

		case MakeDumb:
			HandleMakeDumb(UserIndex);

			/* '/NOESTUPIDO */
			break;

		case MakeDumbNoMore:
			HandleMakeDumbNoMore(UserIndex);

			/* '/DUMPSECURITY */
			break;

		case DumpIPTables:
			HandleDumpIPTables(UserIndex);

			/* '/KICKCONSE */
			break;

		case CouncilKick:
			HandleCouncilKick(UserIndex);

			/* '/TRIGGER */
			break;

		case SetTrigger:
			HandleSetTrigger(UserIndex);

			/* '/TRIGGER with no args */
			break;

		case AskTrigger:
			HandleAskTrigger(UserIndex);

			/* '/BANIPLIST */
			break;

		case BannedIPList:
			HandleBannedIPList(UserIndex);

			/* '/BANIPRELOAD */
			break;

		case BannedIPReload:
			HandleBannedIPReload(UserIndex);

			/* '/MIEMBROSCLAN */
			break;

		case GuildMemberList:
			HandleGuildMemberList(UserIndex);

			/* '/BANCLAN */
			break;

		case GuildBan:
			HandleGuildBan(UserIndex);

			/* '/BANIP */
			break;

		case BanIP:
			HandleBanIP(UserIndex);

			/* '/UNBANIP */
			break;

		case UnbanIP:
			HandleUnbanIP(UserIndex);

			/* '/CI */
			break;

		case CreateItem:
			HandleCreateItem(UserIndex);

			/* '/DEST */
			break;

		case DestroyItems:
			HandleDestroyItems(UserIndex);

			/* '/NOCAOS */
			break;

		case ChaosLegionKick:
			HandleChaosLegionKick(UserIndex);

			/* '/NOREAL */
			break;

		case RoyalArmyKick:
			HandleRoyalArmyKick(UserIndex);

			/* '/FORCEMIDI */
			break;

		case ForceMIDIAll:
			HandleForceMIDIAll(UserIndex);

			/* '/FORCEWAV */
			break;

		case ForceWAVEAll:
			HandleForceWAVEAll(UserIndex);

			/* '/BORRARPENA */
			break;

		case RemovePunishment:
			HandleRemovePunishment(UserIndex);

			/* '/BLOQ */
			break;

		case TileBlockedToggle:
			HandleTileBlockedToggle(UserIndex);

			/* '/MATA */
			break;

		case KillNPCNoRespawn:
			HandleKillNPCNoRespawn(UserIndex);

			/* '/MASSKILL */
			break;

		case KillAllNearbyNPCs:
			HandleKillAllNearbyNPCs(UserIndex);

			/* '/LASTIP */
			break;

		case LastIP:
			HandleLastIP(UserIndex);

			/* '/MOTDCAMBIA */
			break;

		case ChangeMOTD:
			HandleChangeMOTD(UserIndex);

			/* 'ZMOTD */
			break;

		case SetMOTD:
			HandleSetMOTD(UserIndex);

			/* '/SMSG */
			break;

		case SystemMessage:
			HandleSystemMessage(UserIndex);

			/* '/ACC */
			break;

		case CreateNPC:
			HandleCreateNPC(UserIndex);

			/* '/RACC */
			break;

		case CreateNPCWithRespawn:
			HandleCreateNPCWithRespawn(UserIndex);

			/* '/AI1 - 4 */
			break;

		case ImperialArmour:
			HandleImperialArmour(UserIndex);

			/* '/AC1 - 4 */
			break;

		case ChaosArmour:
			HandleChaosArmour(UserIndex);

			/* '/NAVE */
			break;

		case NavigateToggle:
			HandleNavigateToggle(UserIndex);

			/* '/HABILITAR */
			break;

		case ServerOpenToUsersToggle:
			HandleServerOpenToUsersToggle(UserIndex);

			/* '/APAGAR */
			break;

		case TurnOffServer:
			HandleTurnOffServer(UserIndex);

			/* '/CONDEN */
			break;

		case TurnCriminal:
			HandleTurnCriminal(UserIndex);

			/* '/RAJAR */
			break;

		case ResetFactions:
			HandleResetFactions(UserIndex);

			/* '/RAJARCLAN */
			break;

		case RemoveCharFromGuild:
			HandleRemoveCharFromGuild(UserIndex);

			/* '/LASTEMAIL */
			break;

		case RequestCharMail:
			HandleRequestCharMail(UserIndex);

			/* '/APASS */
			break;

		case AlterPassword:
			HandleAlterPassword(UserIndex);

			/* '/AEMAIL */
			break;

		case AlterMail:
			HandleAlterMail(UserIndex);

			/* '/ANAME */
			break;

		case AlterName:
			HandleAlterName(UserIndex);

			/* '/CENTINELAACTIVADO */
			break;

		case ToggleCentinelActivated:
			HandleToggleCentinelActivated(UserIndex);

			/* '/DOBACKUP */
			break;

		case Declaraciones.eGMCommands.DoBackUp:
			HandleDoBackUp(UserIndex);

			/* '/SHOWCMSG */
			break;

		case ShowGuildMessages:
			HandleShowGuildMessages(UserIndex);

			/* '/GUARDAMAPA */
			break;

		case SaveMap:
			HandleSaveMap(UserIndex);

			/* '/MODMAPINFO PK */
			break;

		case ChangeMapInfoPK:
			HandleChangeMapInfoPK(UserIndex);

			/* '/MODMAPINFO BACKUP */
			break;

		case ChangeMapInfoBackup:
			HandleChangeMapInfoBackup(UserIndex);

			/* '/MODMAPINFO RESTRINGIR */
			break;

		case ChangeMapInfoRestricted:
			HandleChangeMapInfoRestricted(UserIndex);

			/* '/MODMAPINFO MAGIASINEFECTO */
			break;

		case ChangeMapInfoNoMagic:
			HandleChangeMapInfoNoMagic(UserIndex);

			/* '/MODMAPINFO INVISINEFECTO */
			break;

		case ChangeMapInfoNoInvi:
			HandleChangeMapInfoNoInvi(UserIndex);

			/* '/MODMAPINFO RESUSINEFECTO */
			break;

		case ChangeMapInfoNoResu:
			HandleChangeMapInfoNoResu(UserIndex);

			/* '/MODMAPINFO TERRENO */
			break;

		case ChangeMapInfoLand:
			HandleChangeMapInfoLand(UserIndex);

			/* '/MODMAPINFO ZONA */
			break;

		case ChangeMapInfoZone:
			HandleChangeMapInfoZone(UserIndex);

			/* '/MODMAPINFO ROBONPC */
			break;

		case ChangeMapInfoStealNpc:
			HandleChangeMapInfoStealNpc(UserIndex);

			/* '/MODMAPINFO OCULTARSINEFECTO */
			break;

		case ChangeMapInfoNoOcultar:
			HandleChangeMapInfoNoOcultar(UserIndex);

			/* '/MODMAPINFO INVOCARSINEFECTO */
			break;

		case ChangeMapInfoNoInvocar:
			HandleChangeMapInfoNoInvocar(UserIndex);

			/* '/GRABAR */
			break;

		case SaveChars:
			HandleSaveChars(UserIndex);

			/* '/BORRAR SOS */
			break;

		case CleanSOS:
			HandleCleanSOS(UserIndex);

			/* '/SHOW INT */
			break;

		case ShowServerForm:
			HandleShowServerForm(UserIndex);

			/* '/NOCHE */
			break;

		case night:
			HandleNight(UserIndex);

			/* '/ECHARTODOSPJS */
			break;

		case KickAllChars:
			HandleKickAllChars(UserIndex);

			/* '/RELOADNPCS */
			break;

		case ReloadNPCs:
			HandleReloadNPCs(UserIndex);

			/* '/RELOADSINI */
			break;

		case ReloadServerIni:
			HandleReloadServerIni(UserIndex);

			/* '/RELOADHECHIZOS */
			break;

		case ReloadSpells:
			HandleReloadSpells(UserIndex);

			/* '/RELOADOBJ */
			break;

		case ReloadObjects:
			HandleReloadObjects(UserIndex);

			/* '/REINICIAR */
			break;

		case Restart:
			HandleRestart(UserIndex);

			/* '/AUTOUPDATE */
			break;

		case ResetAutoUpdate:
			HandleResetAutoUpdate(UserIndex);

			/* '/CHATCOLOR */
			break;

		case ChatColor:
			HandleChatColor(UserIndex);

			/* '/IGNORADO */
			break;

		case Ignored:
			HandleIgnored(UserIndex);

			/* '/SLOT */
			break;

		case CheckSlot:
			HandleCheckSlot(UserIndex);

			/* '/SETINIVAR LLAVE CLAVE VALOR */
			break;

		case SetIniVar:
			HandleSetIniVar(UserIndex);

			/* '/CREARPRETORIANOS */
			break;

		case CreatePretorianClan:
			HandleCreatePretorianClan(UserIndex);

			/* '/ELIMINARPRETORIANOS */
			break;

		case RemovePretorianClan:
			HandleDeletePretorianClan(UserIndex);

			/* '/DENUNCIAS */
			break;

		case EnableDenounces:
			HandleEnableDenounces(UserIndex);

			/* '/SHOW DENUNCIAS */
			break;

		case ShowDenouncesList:
			HandleShowDenouncesList(UserIndex);

			/* '/SETDIALOG */
			break;

		case SetDialog:
			HandleSetDialog(UserIndex);

			/* '/IMPERSONAR */
			break;

		case Impersonate:
			HandleImpersonate(UserIndex);

			/* '/MIMETIZAR */
			break;

		case Imitate:
			HandleImitate(UserIndex);

			break;

		case RecordAdd:
			HandleRecordAdd(UserIndex);

			break;

		case RecordAddObs:
			HandleRecordAddObs(UserIndex);

			break;

		case RecordRemove:
			HandleRecordRemove(UserIndex);

			break;

		case RecordListRequest:
			HandleRecordListRequest(UserIndex);

			break;

		case RecordDetailsRequest:
			HandleRecordDetailsRequest(UserIndex);

			break;

		case HigherAdminsMessage:
			HandleHigherAdminsMessage(UserIndex);

			/* '/ACLAN */
			break;

		case AlterGuildName:
			HandleAlterGuildName(UserIndex);
			break;
		}

		return;

		/* FIXME: ErrHandler : */
		General.LogError(
				"Error en GmCommands. Error: " + Err.Number + " - " + Err.description + ". Paquete: " + Command);

	}

	/* '' */
	/* ' Handles the "Home" message. */
	/* ' */
	/* ' @param userIndex The index of the user sending the message. */
	static void HandleHome(int UserIndex) {
		/* '*************************************************** */
		/* 'Author: Budi */
		/* 'Creation Date: 06/01/2010 */
		/* 'Last Modification: 05/06/10 */
		/* 'Pato - 05/06/10: Add the Ucase$ to prevent problems. */
		/* '*************************************************** */
		Declaraciones.UserList[UserIndex].incomingData.ReadByte();
		if (Declaraciones.UserList[UserIndex].flags.TargetNpcTipo == eNPCType.Gobernador) {
			UsUaRiOs.setHome(UserIndex, Declaraciones.Npclist[Declaraciones.UserList[UserIndex].flags.TargetNPC].Ciudad,
					Declaraciones.UserList[UserIndex].flags.TargetNPC);
		} else {
			if (Declaraciones.UserList[UserIndex].flags.Muerto == 1) {
				/* 'Si es un mapa común y no está en cana */
				if ((Declaraciones.MapInfo[Declaraciones.UserList[UserIndex].Pos.Map].Restringir == eRestrict.restrict_no)
						&& (Declaraciones.UserList[UserIndex].Counters.Pena == 0)) {
					if (Declaraciones.UserList[UserIndex].flags.Traveling == 0) {
						if (Declaraciones.Ciudades[Declaraciones.UserList[UserIndex].Hogar].Map != Declaraciones.UserList[UserIndex].Pos.Map) {
							UsUaRiOs.goHome(UserIndex);
						} else {
							WriteConsoleMsg(UserIndex, "Ya te encuentras en tu hogar.", FontTypeNames.FONTTYPE_INFO);
						}
					} else {
						WriteMultiMessage(UserIndex, eMessages.CancelHome);
						Declaraciones.UserList[UserIndex].flags.Traveling = 0;
						Declaraciones.UserList[UserIndex].Counters.goHome = 0;
					}
				} else {
					WriteConsoleMsg(UserIndex, "No puedes usar este comando aquí.", FontTypeNames.FONTTYPE_FIGHT);
				}
			} else {
				WriteConsoleMsg(UserIndex, "Debes estar muerto para utilizar este comando.",
						FontTypeNames.FONTTYPE_INFO);
			}
		}
	}

	/* '' */
	/* ' Handles the "LoginExistingChar" message. */
	/* ' */
	/* ' @param userIndex The index of the user sending the message. */

	static void HandleLoginExistingChar(int UserIndex) {
		/* '*************************************************** */
		/* 'Author: Juan Martín Sotuyo Dodero (Maraxus) */
		/* 'Last Modification: 05/17/06 */
		/* ' */
		/* '*************************************************** */
		/* # IF SeguridadAlkon THEN */
		/* # ELSE */
		if (Declaraciones.UserList[UserIndex].incomingData.length < 6) {
			throw new RuntimeException("Err . Raise UserList ( UserIndex ) . incomingData . NotEnoughDataErrCode");
			return;
		}
		/* # END IF */

		/* FIXME: ON ERROR GOTO ErrHandler */
		/*
		 * 'This packet contains strings, make a copy of the data to prevent
		 * losses if it's not complete yet...
		 */
		clsByteQueue buffer;
		buffer = new clsByteQueue();
		buffer.CopyBuffer(Declaraciones.UserList[UserIndex].incomingData);

		/* 'Remove packet ID */
		buffer.ReadByte();

		String UserName;
		String Password;
		String version;

		UserName = buffer.ReadASCIIString();

		/* # IF SeguridadAlkon THEN */
		/* # ELSE */
		Password = buffer.ReadASCIIString();
		/* # END IF */

		/* 'Convert version number to string */
		version = vb6.CStr(buffer.ReadByte()) + "." + vb6.CStr(buffer.ReadByte()) + "." + vb6.CStr(buffer.ReadByte());

		if (!TCP.AsciiValidos(UserName)) {
			WriteErrorMsg(UserIndex, "Nombre inválido.");
			FlushBuffer(UserIndex);
			TCP.CloseSocket(UserIndex);

			return;
		}

		if (!Admin.PersonajeExiste(UserName)) {
			WriteErrorMsg(UserIndex, "El personaje no existe.");
			FlushBuffer(UserIndex);
			TCP.CloseSocket(UserIndex);

			return;
		}

		boolean bConFailed;

		/* # IF SeguridadAlkon THEN */
		/* # END IF */

		if (Admin.BANCheck(UserName)) {
			WriteErrorMsg(UserIndex,
					"Se te ha prohibido la entrada a Argentum Online debido a tu mal comportamiento. Puedes consultar el reglamento y el sistema de soporte desde www.argentumonline.com.ar");
		} else if (!Admin.VersionOK(version)) {
			WriteErrorMsg(UserIndex, "Esta versión del juego es obsoleta, la versión correcta es la "
					+ Declaraciones.ULTIMAVERSION + ". La misma se encuentra disponible en www.argentumonline.com.ar");
		} else {
			bConFailed = !TCP.ConnectUser(UserIndex, UserName, Password);
		}
		/* # IF SeguridadAlkon THEN */
		/* # END IF */

		/*
		 * 'If we got here then packet is complete, copy data back to original
		 * queue
		 */
		if (!bConFailed) {
			Declaraciones.UserList[UserIndex].incomingData.CopyBuffer(buffer);
		}

		/* FIXME: ErrHandler : */
		int ERROR;
		/* ERROR = Err . Number */
		/* FIXME: ON ERROR GOTO 0 */

		/* 'Destroy auxiliar buffer */
		buffer = null;

		if (ERROR != 0) {
			General.LogError("Error en HandleLoginExistingChar: " + Err.description + "(" + ERROR + "). UserName:"
					+ UserName + ". UserIndex: " + UserIndex);

			throw new RuntimeException("Err . Raise ERROR");
		}
	}

	/* '' */
	/* ' Handles the "ThrowDices" message. */
	/* ' */
	/* ' @param userIndex The index of the user sending the message. */

	static void HandleThrowDices(int UserIndex) {
		/* '*************************************************** */
		/* 'Author: Juan Martín Sotuyo Dodero (Maraxus) */
		/* 'Last Modification: 05/17/06 */
		/* ' */
		/* '*************************************************** */
		/* 'Remove packet ID */
		Declaraciones.UserList[UserIndex].incomingData.ReadByte();

		Declaraciones.UserList[UserIndex].Stats.UserAtributos[eAtributos.Fuerza] = SistemaCombate.MaximoInt(15,
				13 + Matematicas.RandomNumber(0, 3) + Matematicas.RandomNumber(0, 2));
		Declaraciones.UserList[UserIndex].Stats.UserAtributos[eAtributos.Agilidad] = SistemaCombate.MaximoInt(15,
				12 + Matematicas.RandomNumber(0, 3) + Matematicas.RandomNumber(0, 3));
		Declaraciones.UserList[UserIndex].Stats.UserAtributos[eAtributos.Inteligencia] = SistemaCombate.MaximoInt(16,
				13 + Matematicas.RandomNumber(0, 3) + Matematicas.RandomNumber(0, 2));
		Declaraciones.UserList[UserIndex].Stats.UserAtributos[eAtributos.Carisma] = SistemaCombate.MaximoInt(15,
				12 + Matematicas.RandomNumber(0, 3) + Matematicas.RandomNumber(0, 3));
		/* ' [TEMPORAL] 16 + RandomNumber(0, 1) + RandomNumber(0, 1) */
		Declaraciones.UserList[UserIndex].Stats.UserAtributos[eAtributos.Constitucion] = 18;

		WriteDiceRoll(UserIndex);
	}

	/* '' */
	/* ' Handles the "LoginNewChar" message. */
	/* ' */
	/* ' @param userIndex The index of the user sending the message. */

	static void HandleLoginNewChar(int UserIndex) {
		/* '*************************************************** */
		/* 'Author: Juan Martín Sotuyo Dodero (Maraxus) */
		/* 'Last Modification: 05/17/06 */
		/* ' */
		/* '*************************************************** */
		/* # IF SeguridadAlkon THEN */
		/* # ELSE */
		if (Declaraciones.UserList[UserIndex].incomingData.length < 15) {
			throw new RuntimeException("Err . Raise UserList ( UserIndex ) . incomingData . NotEnoughDataErrCode");
			return;
		}
		/* # END IF */

		/* FIXME: ON ERROR GOTO ErrHandler */
		/*
		 * 'This packet contains strings, make a copy of the data to prevent
		 * losses if it's not complete yet...
		 */
		clsByteQueue buffer;
		buffer = new clsByteQueue();
		buffer.CopyBuffer(Declaraciones.UserList[UserIndex].incomingData);

		/* 'Remove packet ID */
		buffer.ReadByte();

		String UserName;
		String Password;
		String version;
		eRaza race;
		eGenero gender;
		eCiudad homeland;
		eClass Class;
		int Head;
		String mail;

		/* # IF SeguridadAlkon THEN */
		/* # END IF */

		if (Declaraciones.PuedeCrearPersonajes == 0) {
			WriteErrorMsg(UserIndex, "La creación de personajes en este servidor se ha deshabilitado.");
			FlushBuffer(UserIndex);
			TCP.CloseSocket(UserIndex);

			return;
		}

		if (Declaraciones.ServerSoloGMs != 0) {
			WriteErrorMsg(UserIndex,
					"Servidor restringido a administradores. Consulte la página oficial o el foro oficial para más información.");
			FlushBuffer(UserIndex);
			TCP.CloseSocket(UserIndex);

			return;
		}

		if (Declaraciones.aClon.MaxPersonajes(Declaraciones.UserList[UserIndex].ip)) {
			WriteErrorMsg(UserIndex, "Has creado demasiados personajes.");
			FlushBuffer(UserIndex);
			TCP.CloseSocket(UserIndex);

			return;
		}

		UserName = buffer.ReadASCIIString();

		/* # IF SeguridadAlkon THEN */
		/* # ELSE */
		Password = buffer.ReadASCIIString();
		/* # END IF */

		/* 'Convert version number to string */
		version = vb6.CStr(buffer.ReadByte()) + "." + vb6.CStr(buffer.ReadByte()) + "." + vb6.CStr(buffer.ReadByte());

		/* # IF SeguridadAlkon THEN */
		/* # END IF */

		race = buffer.ReadByte();
		gender = buffer.ReadByte();
		Class = buffer.ReadByte();
		Head = buffer.ReadInteger();
		mail = buffer.ReadASCIIString();
		homeland = buffer.ReadByte();

		/* # IF SeguridadAlkon THEN */
		/* # END IF */

		if (!Admin.VersionOK(version)) {
			WriteErrorMsg(UserIndex, "Esta versión del juego es obsoleta, la versión correcta es la "
					+ Declaraciones.ULTIMAVERSION + ". La misma se encuentra disponible en www.argentumonline.com.ar");
		} else {
			TCP.ConnectNewUser(UserIndex, UserName, Password, race, gender, Class, mail, homeland, Head);
		}
		/* # IF SeguridadAlkon THEN */
		/* # END IF */

		/*
		 * 'If we got here then packet is complete, copy data back to original
		 * queue
		 */
		Declaraciones.UserList[UserIndex].incomingData.CopyBuffer(buffer);

		/* FIXME: ErrHandler : */
		int ERROR;
		/* ERROR = Err . Number */
		/* FIXME: ON ERROR GOTO 0 */

		/* 'Destroy auxiliar buffer */
		buffer = null;

		if (ERROR != 0) {
			throw new RuntimeException("Err . Raise ERROR");
		}
	}

	/* '' */
	/* ' Handles the "Talk" message. */
	/* ' */
	/* ' @param userIndex The index of the user sending the message. */

	static void HandleTalk(int UserIndex) {
		/* '*************************************************** */
		/* 'Author: Juan Martín Sotuyo Dodero (Maraxus) */
		/* 'Last Modification: 13/01/2010 */
		/* '15/07/2009: ZaMa - Now invisible admins talk by console. */
		/* '23/09/2009: ZaMa - Now invisible admins can't send empty chat. */
		/*
		 * '13/01/2010: ZaMa - Now hidden on boat pirats recover the proper boat
		 * body.
		 */
		/* '*************************************************** */
		if (Declaraciones.UserList[UserIndex].incomingData.length < 3) {
			throw new RuntimeException("Err . Raise UserList ( UserIndex ) . incomingData . NotEnoughDataErrCode");
			return;
		}

		/* FIXME: ON ERROR GOTO ErrHandler */

		/*
		 * 'This packet contains strings, make a copy of the data to prevent
		 * losses if it's not complete yet...
		 */
		clsByteQueue buffer;
		buffer = new clsByteQueue();
		buffer.CopyBuffer(Declaraciones.UserList[UserIndex].incomingData);

		/* 'Remove packet ID */
		buffer.ReadByte();

		String Chat;

		Chat = buffer.ReadASCIIString();

		/* '[Consejeros & GMs] */
		if (Declaraciones.UserList[UserIndex].flags.Privilegios && (PlayerType.Consejero || PlayerType.SemiDios)) {
			General.LogGM(Declaraciones.UserList[UserIndex].Name, "Dijo: " + Chat);
		}

		/* 'I see you.... */
		if (Declaraciones.UserList[UserIndex].flags.Oculto > 0) {
			Declaraciones.UserList[UserIndex].flags.Oculto = 0;
			Declaraciones.UserList[UserIndex].Counters.TiempoOculto = 0;

			if (Declaraciones.UserList[UserIndex].flags.Navegando == 1) {
				if (Declaraciones.UserList[UserIndex].clase == eClass.Pirat) {
					/* ' Pierde la apariencia de fragata fantasmal */
					UsUaRiOs.ToggleBoatBody(UserIndex);
					WriteConsoleMsg(UserIndex, "¡Has recuperado tu apariencia normal!", FontTypeNames.FONTTYPE_INFO);
					UsUaRiOs.ChangeUserChar(UserIndex, Declaraciones.UserList[UserIndex].Char.body,
							Declaraciones.UserList[UserIndex].Char.Head, Declaraciones.UserList[UserIndex].Char.heading,
							Declaraciones.NingunArma, Declaraciones.NingunEscudo, Declaraciones.NingunCasco);
				}
			} else {
				if (Declaraciones.UserList[UserIndex].flags.invisible == 0) {
					UsUaRiOs.SetInvisible(UserIndex, Declaraciones.UserList[UserIndex].Char.CharIndex, false);
					WriteConsoleMsg(UserIndex, "¡Has vuelto a ser visible!", FontTypeNames.FONTTYPE_INFO);
				}
			}
		}

		if (vb6.LenB(Chat) != 0) {
			/* 'Analize chat... */
			Statistics.ParseChat(Chat);

			if (!(Declaraciones.UserList[UserIndex].flags.AdminInvisible == 1)) {
				if (Declaraciones.UserList[UserIndex].flags.Muerto == 1) {
					modSendData.SendData(SendTarget.ToDeadArea, UserIndex, PrepareMessageChatOverHead(Chat,
							Declaraciones.UserList[UserIndex].Char.CharIndex, Declaraciones.CHAT_COLOR_DEAD_CHAR));
				} else {
					modSendData.SendData(SendTarget.ToPCArea, UserIndex,
							PrepareMessageChatOverHead(Chat, Declaraciones.UserList[UserIndex].Char.CharIndex,
									Declaraciones.UserList[UserIndex].flags.ChatColor));
				}
			} else {
				if (vb6.RTrim(Chat) != "") {
					modSendData.SendData(SendTarget.ToPCArea, UserIndex,
							PrepareMessageConsoleMsg("Gm> " + Chat, FontTypeNames.FONTTYPE_GM));
				}
			}
		}

		/*
		 * 'If we got here then packet is complete, copy data back to original
		 * queue
		 */
		Declaraciones.UserList[UserIndex].incomingData.CopyBuffer(buffer);

		/* FIXME: ErrHandler : */
		int ERROR;
		/* ERROR = Err . Number */
		/* FIXME: ON ERROR GOTO 0 */

		/* 'Destroy auxiliar buffer */
		buffer = null;

		if (ERROR != 0) {
			throw new RuntimeException("Err . Raise ERROR");
		}
	}

	/* '' */
	/* ' Handles the "Yell" message. */
	/* ' */
	/* ' @param userIndex The index of the user sending the message. */

	static void HandleYell(int UserIndex) {
		/* '*************************************************** */
		/* 'Author: Juan Martín Sotuyo Dodero (Maraxus) */
		/* 'Last Modification: 13/01/2010 (ZaMa) */
		/* '15/07/2009: ZaMa - Now invisible admins yell by console. */
		/*
		 * '13/01/2010: ZaMa - Now hidden on boat pirats recover the proper boat
		 * body.
		 */
		/* '*************************************************** */
		if (Declaraciones.UserList[UserIndex].incomingData.length < 3) {
			throw new RuntimeException("Err . Raise UserList ( UserIndex ) . incomingData . NotEnoughDataErrCode");
			return;
		}

		/* FIXME: ON ERROR GOTO ErrHandler */

		/*
		 * 'This packet contains strings, make a copy of the data to prevent
		 * losses if it's not complete yet...
		 */
		clsByteQueue buffer;
		buffer = new clsByteQueue();
		buffer.CopyBuffer(Declaraciones.UserList[UserIndex].incomingData);

		/* 'Remove packet ID */
		buffer.ReadByte();

		String Chat;

		Chat = buffer.ReadASCIIString();

		/* '[Consejeros & GMs] */
		if (Declaraciones.UserList[UserIndex].flags.Privilegios && (PlayerType.Consejero || PlayerType.SemiDios)) {
			General.LogGM(Declaraciones.UserList[UserIndex].Name, "Grito: " + Chat);
		}

		/* 'I see you.... */
		if (Declaraciones.UserList[UserIndex].flags.Oculto > 0) {
			Declaraciones.UserList[UserIndex].flags.Oculto = 0;
			Declaraciones.UserList[UserIndex].Counters.TiempoOculto = 0;

			if (Declaraciones.UserList[UserIndex].flags.Navegando == 1) {
				if (Declaraciones.UserList[UserIndex].clase == eClass.Pirat) {
					/* ' Pierde la apariencia de fragata fantasmal */
					UsUaRiOs.ToggleBoatBody(UserIndex);
					WriteConsoleMsg(UserIndex, "¡Has recuperado tu apariencia normal!", FontTypeNames.FONTTYPE_INFO);
					UsUaRiOs.ChangeUserChar(UserIndex, Declaraciones.UserList[UserIndex].Char.body,
							Declaraciones.UserList[UserIndex].Char.Head, Declaraciones.UserList[UserIndex].Char.heading,
							Declaraciones.NingunArma, Declaraciones.NingunEscudo, Declaraciones.NingunCasco);
				}
			} else {
				if (Declaraciones.UserList[UserIndex].flags.invisible == 0) {
					UsUaRiOs.SetInvisible(UserIndex, Declaraciones.UserList[UserIndex].Char.CharIndex, false);
					WriteConsoleMsg(UserIndex, "¡Has vuelto a ser visible!", FontTypeNames.FONTTYPE_INFO);
				}
			}
		}

		if (vb6.LenB(Chat) != 0) {
			/* 'Analize chat... */
			Statistics.ParseChat(Chat);

			if (Declaraciones.UserList[UserIndex].flags.Privilegios && PlayerType.User) {
				if (Declaraciones.UserList[UserIndex].flags.Muerto == 1) {
					modSendData.SendData(SendTarget.ToDeadArea, UserIndex, PrepareMessageChatOverHead(Chat,
							Declaraciones.UserList[UserIndex].Char.CharIndex, Declaraciones.CHAT_COLOR_DEAD_CHAR));
				} else {
					modSendData.SendData(SendTarget.ToPCArea, UserIndex,
							PrepareMessageChatOverHead(Chat, Declaraciones.UserList[UserIndex].Char.CharIndex, vbRed));
				}
			} else {
				if (!(Declaraciones.UserList[UserIndex].flags.AdminInvisible == 1)) {
					modSendData.SendData(SendTarget.ToPCArea, UserIndex, PrepareMessageChatOverHead(Chat,
							Declaraciones.UserList[UserIndex].Char.CharIndex, Declaraciones.CHAT_COLOR_GM_YELL));
				} else {
					modSendData.SendData(SendTarget.ToPCArea, UserIndex,
							PrepareMessageConsoleMsg("Gm> " + Chat, FontTypeNames.FONTTYPE_GM));
				}
			}
		}

		/*
		 * 'If we got here then packet is complete, copy data back to original
		 * queue
		 */
		Declaraciones.UserList[UserIndex].incomingData.CopyBuffer(buffer);

		/* FIXME: ErrHandler : */
		int ERROR;
		/* ERROR = Err . Number */
		/* FIXME: ON ERROR GOTO 0 */

		/* 'Destroy auxiliar buffer */
		buffer = null;

		if (ERROR != 0) {
			throw new RuntimeException("Err . Raise ERROR");
		}
	}

	/* '' */
	/* ' Handles the "Whisper" message. */
	/* ' */
	/* ' @param userIndex The index of the user sending the message. */

	static void HandleWhisper(int UserIndex) {
		/* '*************************************************** */
		/* 'Author: Juan Martín Sotuyo Dodero (Maraxus) */
		/* 'Last Modification: 03/12/2010 */
		/*
		 * '28/05/2009: ZaMa - Now it doesn't appear any message when private
		 * talking to an invisible admin
		 */
		/* '15/07/2009: ZaMa - Now invisible admins wisper by console. */
		/*
		 * '03/12/2010: Enanoh - Agregué susurro a Admins en modo consulta y Los
		 * Dioses pueden susurrar en ciertos casos.
		 */
		/* '*************************************************** */
		if (Declaraciones.UserList[UserIndex].incomingData.length < 5) {
			throw new RuntimeException("Err . Raise UserList ( UserIndex ) . incomingData . NotEnoughDataErrCode");
			return;
		}

		/* FIXME: ON ERROR GOTO ErrHandler */

		/*
		 * 'This packet contains strings, make a copy of the data to prevent
		 * losses if it's not complete yet...
		 */
		clsByteQueue buffer;
		buffer = new clsByteQueue();
		buffer.CopyBuffer(Declaraciones.UserList[UserIndex].incomingData);

		/* 'Remove packet ID */
		buffer.ReadByte();

		String Chat;
		int TargetUserIndex;
		PlayerType TargetPriv;
		PlayerType UserPriv;
		String TargetName;

		TargetName = buffer.ReadASCIIString();
		Chat = buffer.ReadASCIIString();

		UserPriv = Declaraciones.UserList[UserIndex].flags.Privilegios;

		if (Declaraciones.UserList[UserIndex].flags.Muerto) {
			WriteConsoleMsg(UserIndex, "¡¡Estás muerto!! Los muertos no pueden comunicarse con el mundo de los vivos. ",
					FontTypeNames.FONTTYPE_INFO);
		} else {
			/* ' Offline? */
			TargetUserIndex = Extra.NameIndex(TargetName);
			if (TargetUserIndex == Characters.INVALID_INDEX) {
				/* ' Admin? */
				if (ES.EsGmChar(TargetName)) {
					WriteConsoleMsg(UserIndex, "No puedes susurrarle a los Administradores.",
							FontTypeNames.FONTTYPE_INFO);
					/* ' Whisperer admin? (Else say nothing) */
				} else if ((UserPriv && (PlayerType.Dios || PlayerType.Admin)) != 0) {
					WriteConsoleMsg(UserIndex, "Usuario inexistente.", FontTypeNames.FONTTYPE_INFO);
				}

				/* ' Online */
			} else {
				/* ' Privilegios */
				TargetPriv = Declaraciones.UserList[TargetUserIndex].flags.Privilegios;

				/*
				 * ' Consejeros, semis y usuarios no pueden susurrar a dioses
				 * (Salvo en consulta)
				 */
				if ((TargetPriv && (PlayerType.Dios || PlayerType.Admin)) != 0
						&& (UserPriv && (PlayerType.User || PlayerType.Consejero || PlayerType.SemiDios)) != 0
						&& !Declaraciones.UserList[UserIndex].flags.EnConsulta) {

					/* ' No puede */
					WriteConsoleMsg(UserIndex, "No puedes susurrarle a los Administradores.",
							FontTypeNames.FONTTYPE_INFO);

					/*
					 * ' Usuarios no pueden susurrar a semis o conses (Salvo en
					 * consulta)
					 */
				} else if ((UserPriv && PlayerType.User) != 0 && (!TargetPriv && PlayerType.User) != 0
						&& !Declaraciones.UserList[UserIndex].flags.EnConsulta) {

					/* ' No puede */
					WriteConsoleMsg(UserIndex, "No puedes susurrarle a los Administradores.",
							FontTypeNames.FONTTYPE_INFO);

					/* ' En rango? (Los dioses pueden susurrar a distancia) */
				} else if (!TCP.EstaPCarea(UserIndex, TargetUserIndex)
						&& (UserPriv && (PlayerType.Dios || PlayerType.Admin)) == 0) {

					/* ' No se puede susurrar a admins fuera de su rango */
					if ((TargetPriv && (PlayerType.User)) == 0
							&& (UserPriv && (PlayerType.Dios || PlayerType.Admin)) == 0) {
						WriteConsoleMsg(UserIndex, "No puedes susurrarle a los Administradores.",
								FontTypeNames.FONTTYPE_INFO);

						/* ' Whisperer admin? (Else say nothing) */
					} else if ((UserPriv && (PlayerType.Dios || PlayerType.Admin)) != 0) {
						WriteConsoleMsg(UserIndex, "Estás muy lejos del usuario.", FontTypeNames.FONTTYPE_INFO);
					}
				} else {
					/* '[Consejeros & GMs] */
					if (UserPriv && (PlayerType.Consejero || PlayerType.SemiDios)) {
						General.LogGM(Declaraciones.UserList[UserIndex].Name,
								"Le susurro a '" + Declaraciones.UserList[TargetUserIndex].Name + "' " + Chat);

						/* ' Usuarios a administradores */
					} else if ((UserPriv && PlayerType.User) != 0 && (TargetPriv && PlayerType.User) == 0) {
						General.LogGM(Declaraciones.UserList[TargetUserIndex].Name,
								Declaraciones.UserList[UserIndex].Name + " le susurro en consulta: " + Chat);
					}

					if (vb6.LenB(Chat) != 0) {
						/* 'Analize chat... */
						Statistics.ParseChat(Chat);

						/* ' Dios susurrando a distancia */
						if (!TCP.EstaPCarea(UserIndex, TargetUserIndex)
								&& (UserPriv && (PlayerType.Dios || PlayerType.Admin)) != 0) {

							WriteConsoleMsg(UserIndex, "Susurraste> " + Chat, FontTypeNames.FONTTYPE_GM);
							WriteConsoleMsg(TargetUserIndex, "Gm susurra> " + Chat, FontTypeNames.FONTTYPE_GM);

						} else if (!(Declaraciones.UserList[UserIndex].flags.AdminInvisible == 1)) {
							WriteChatOverHead(UserIndex, Chat, Declaraciones.UserList[UserIndex].Char.CharIndex,
									vbBlue);
							WriteChatOverHead(TargetUserIndex, Chat, Declaraciones.UserList[UserIndex].Char.CharIndex,
									vbBlue);
							FlushBuffer(TargetUserIndex);

							/* '[CDT 17-02-2004] */
							if (Declaraciones.UserList[UserIndex].flags.Privilegios
									&& (PlayerType.User || PlayerType.Consejero)) {
								modSendData.SendData(SendTarget.ToAdminsAreaButConsejeros, UserIndex,
										PrepareMessageChatOverHead(
												"A " + Declaraciones.UserList[TargetUserIndex].Name + "> " + Chat,
												Declaraciones.UserList[UserIndex].Char.CharIndex, vbYellow));
							}
						} else {
							WriteConsoleMsg(UserIndex, "Susurraste> " + Chat, FontTypeNames.FONTTYPE_GM);
							if (UserIndex != TargetUserIndex) {
								WriteConsoleMsg(TargetUserIndex, "Gm susurra> " + Chat, FontTypeNames.FONTTYPE_GM);
							}

							if (Declaraciones.UserList[UserIndex].flags.Privilegios
									&& (PlayerType.User || PlayerType.Consejero)) {
								modSendData.SendData(SendTarget.ToAdminsAreaButConsejeros, UserIndex,
										PrepareMessageConsoleMsg("Gm dijo a "
												+ Declaraciones.UserList[TargetUserIndex].Name + "> " + Chat,
												FontTypeNames.FONTTYPE_GM));
							}
						}
					}
				}
			}
		}

		/*
		 * 'If we got here then packet is complete, copy data back to original
		 * queue
		 */
		Declaraciones.UserList[UserIndex].incomingData.CopyBuffer(buffer);

		/* FIXME: ErrHandler : */
		int ERROR;
		/* ERROR = Err . Number */
		/* FIXME: ON ERROR GOTO 0 */

		/* 'Destroy auxiliar buffer */
		buffer = null;

		if (ERROR != 0) {
			throw new RuntimeException("Err . Raise ERROR");
		}
	}

	/* '' */
	/* ' Handles the "Walk" message. */
	/* ' */
	/* ' @param userIndex The index of the user sending the message. */

	static void HandleWalk(int UserIndex) {
		/* '*************************************************** */
		/* 'Author: Juan Martín Sotuyo Dodero (Maraxus) */
		/* 'Last Modification: 13/01/2010 (ZaMa) */
		/* '11/19/09 Pato - Now the class bandit can walk hidden. */
		/*
		 * '13/01/2010: ZaMa - Now hidden on boat pirats recover the proper boat
		 * body.
		 */
		/* '*************************************************** */
		if (Declaraciones.UserList[UserIndex].incomingData.length < 2) {
			throw new RuntimeException("Err . Raise UserList ( UserIndex ) . incomingData . NotEnoughDataErrCode");
			return;
		}

		/* FIXME: ON ERROR GOTO ErrHandler */

		int dummy;
		int TempTick;
		eHeading heading;

		/* 'Remove packet ID */
		Declaraciones.UserList[UserIndex].incomingData.ReadByte();

		heading = Declaraciones.UserList[UserIndex].incomingData.ReadByte();

		/* 'Prevent SpeedHack */
		if (Declaraciones.UserList[UserIndex].flags.TimesWalk >= 30) {
			TempTick = Declaraciones.GetTickCount() && 0x7FFFFFFF;
			dummy = modNuevoTimer.getInterval(TempTick, Declaraciones.UserList[UserIndex].flags.StartWalk);

			/*
			 * ' 5800 is actually less than what would be needed in perfect
			 * conditions to take 30 steps
			 */
			/*
			 * '(it's about 193 ms per step against the over 200 needed in
			 * perfect conditions)
			 */
			if (dummy < 5800) {
				if (modNuevoTimer.getInterval(TempTick, Declaraciones.UserList[UserIndex].flags.CountSH) > 30000) {
					Declaraciones.UserList[UserIndex].flags.CountSH = 0;
				}

				if (!Declaraciones.UserList[UserIndex].flags.CountSH == 0) {
					if (dummy != 0) {
						dummy = 126000 / dummy;
					}

					General.LogHackAttemp("Tramposo SH: " + Declaraciones.UserList[UserIndex].Name + " , " + dummy);
					modSendData.SendData(SendTarget.ToAdmins, 0,
							PrepareMessageConsoleMsg(
									"Servidor> " + Declaraciones.UserList[UserIndex].Name
											+ " ha sido echado por el servidor por posible uso de SH.",
									FontTypeNames.FONTTYPE_SERVER));
					TCP.CloseSocket(UserIndex);

					return;
				} else {
					Declaraciones.UserList[UserIndex].flags.CountSH = TempTick;
				}
			}
			Declaraciones.UserList[UserIndex].flags.StartWalk = TempTick;
			Declaraciones.UserList[UserIndex].flags.TimesWalk = 0;
		}

		Declaraciones.UserList[UserIndex].flags.TimesWalk = Declaraciones.UserList[UserIndex].flags.TimesWalk + 1;

		/* 'If exiting, cancel */
		UsUaRiOs.CancelExit(UserIndex);

		/* 'TODO: Debería decirle por consola que no puede? */
		/* 'Esta usando el /HOGAR, no se puede mover */
		if (Declaraciones.UserList[UserIndex].flags.Traveling == 1) {
			return;
		}

		if (Declaraciones.UserList[UserIndex].flags.Paralizado == 0) {
			if (Declaraciones.UserList[UserIndex].flags.Meditando) {
				/* 'Stop meditating, next action will start movement. */
				Declaraciones.UserList[UserIndex].flags.Meditando = false;
				Declaraciones.UserList[UserIndex].Char.FX = 0;
				Declaraciones.UserList[UserIndex].Char.loops = 0;

				WriteMeditateToggle(UserIndex);
				WriteConsoleMsg(UserIndex, "Dejas de meditar.", FontTypeNames.FONTTYPE_INFO);

				modSendData.SendData(SendTarget.ToPCArea, UserIndex,
						PrepareMessageCreateFX(Declaraciones.UserList[UserIndex].Char.CharIndex, 0, 0));
			} else {
				/* 'Move user */
				UsUaRiOs.MoveUserChar(UserIndex, heading);

				/* 'Stop resting if needed */
				if (Declaraciones.UserList[UserIndex].flags.Descansar) {
					Declaraciones.UserList[UserIndex].flags.Descansar = false;

					WriteRestOK(UserIndex);
					WriteConsoleMsg(UserIndex, "Has dejado de descansar.", FontTypeNames.FONTTYPE_INFO);
				}
			}
			/* 'paralized */
		} else {
			if (!Declaraciones.UserList[UserIndex].flags.UltimoMensaje == 1) {
				Declaraciones.UserList[UserIndex].flags.UltimoMensaje = 1;

				WriteConsoleMsg(UserIndex, "No puedes moverte porque estás paralizado.", FontTypeNames.FONTTYPE_INFO);
			}

			Declaraciones.UserList[UserIndex].flags.CountSH = 0;
		}

		/* 'Can't move while hidden except he is a thief */
		if (Declaraciones.UserList[UserIndex].flags.Oculto == 1
				&& Declaraciones.UserList[UserIndex].flags.AdminInvisible == 0) {
			if (Declaraciones.UserList[UserIndex].clase != eClass.Thief
					&& Declaraciones.UserList[UserIndex].clase != eClass.Bandit) {
				Declaraciones.UserList[UserIndex].flags.Oculto = 0;
				Declaraciones.UserList[UserIndex].Counters.TiempoOculto = 0;

				if (Declaraciones.UserList[UserIndex].flags.Navegando == 1) {
					if (Declaraciones.UserList[UserIndex].clase == eClass.Pirat) {
						/* ' Pierde la apariencia de fragata fantasmal */
						UsUaRiOs.ToggleBoatBody(UserIndex);
						WriteConsoleMsg(UserIndex, "¡Has recuperado tu apariencia normal!",
								FontTypeNames.FONTTYPE_INFO);
						UsUaRiOs.ChangeUserChar(UserIndex, Declaraciones.UserList[UserIndex].Char.body,
								Declaraciones.UserList[UserIndex].Char.Head,
								Declaraciones.UserList[UserIndex].Char.heading, Declaraciones.NingunArma,
								Declaraciones.NingunEscudo, Declaraciones.NingunCasco);
					}
				} else {
					/* 'If not under a spell effect, show char */
					if (Declaraciones.UserList[UserIndex].flags.invisible == 0) {
						WriteConsoleMsg(UserIndex, "Has vuelto a ser visible.", FontTypeNames.FONTTYPE_INFO);
						UsUaRiOs.SetInvisible(UserIndex, Declaraciones.UserList[UserIndex].Char.CharIndex, false);
					}
				}
			}
		}

		return;
		/* FIXME: ErrHandler : */
		String UserName;
		if (UserIndex > 0) {
			UserName = Declaraciones.UserList[UserIndex].Name;
		}

		General.LogError(
				"Error en HandleWalk. Error: " + Err.description + ". User: " + UserName + "(" + UserIndex + ")");

	}

	/* '' */
	/* ' Handles the "RequestPositionUpdate" message. */
	/* ' */
	/* ' @param userIndex The index of the user sending the message. */

	static void HandleRequestPositionUpdate(int UserIndex) {
		/* '*************************************************** */
		/* 'Author: Juan Martín Sotuyo Dodero (Maraxus) */
		/* 'Last Modification: 05/17/06 */
		/* ' */
		/* '*************************************************** */
		/* 'Remove packet ID */
		Declaraciones.UserList[UserIndex].incomingData.ReadByte();

		WritePosUpdate(UserIndex);
	}

	/* '' */
	/* ' Handles the "Attack" message. */
	/* ' */
	/* ' @param userIndex The index of the user sending the message. */

	static void HandleAttack(int UserIndex) {
		/* '*************************************************** */
		/* 'Author: Juan Martín Sotuyo Dodero (Maraxus) */
		/* 'Last Modification: 13/01/2010 */
		/* 'Last Modified By: ZaMa */
		/*
		 * '10/01/2008: Tavo - Se cancela la salida del juego si el user esta
		 * saliendo.
		 */
		/* '13/11/2009: ZaMa - Se cancela el estado no atacable al atcar. */
		/*
		 * '13/01/2010: ZaMa - Now hidden on boat pirats recover the proper boat
		 * body.
		 */
		/* '*************************************************** */
		/* 'Remove packet ID */
		Declaraciones.UserList[UserIndex].incomingData.ReadByte();

		/* 'If dead, can't attack */
		if (Declaraciones.UserList[UserIndex].flags.Muerto == 1) {
			WriteConsoleMsg(UserIndex, "¡¡Estás muerto!!", FontTypeNames.FONTTYPE_INFO);
			return;
		}

		/* 'If user meditates, can't attack */
		if (Declaraciones.UserList[UserIndex].flags.Meditando) {
			return;
		}

		/* 'If equiped weapon is ranged, can't attack this way */
		if (Declaraciones.UserList[UserIndex].Invent.WeaponEqpObjIndex > 0) {
			if (Declaraciones.ObjData[Declaraciones.UserList[UserIndex].Invent.WeaponEqpObjIndex].proyectil == 1) {
				WriteConsoleMsg(UserIndex, "No puedes usar así este arma.", FontTypeNames.FONTTYPE_INFO);
				return;
			}
		}

		/* 'Admins can't attack. */
		if ((Declaraciones.UserList[UserIndex].flags.Privilegios && PlayerType.User) == 0) {
			return;
		}

		/* 'If exiting, cancel */
		UsUaRiOs.CancelExit(UserIndex);

		/* 'Attack! */
		SistemaCombate.UsuarioAtaca(UserIndex);

		/* 'Now you can be atacked */
		Declaraciones.UserList[UserIndex].flags.NoPuedeSerAtacado = false;

		/* 'I see you... */
		if (Declaraciones.UserList[UserIndex].flags.Oculto > 0
				&& Declaraciones.UserList[UserIndex].flags.AdminInvisible == 0) {
			Declaraciones.UserList[UserIndex].flags.Oculto = 0;
			Declaraciones.UserList[UserIndex].Counters.TiempoOculto = 0;

			if (Declaraciones.UserList[UserIndex].flags.Navegando == 1) {
				if (Declaraciones.UserList[UserIndex].clase == eClass.Pirat) {
					/* ' Pierde la apariencia de fragata fantasmal */
					UsUaRiOs.ToggleBoatBody(UserIndex);
					WriteConsoleMsg(UserIndex, "¡Has recuperado tu apariencia normal!", FontTypeNames.FONTTYPE_INFO);
					UsUaRiOs.ChangeUserChar(UserIndex, Declaraciones.UserList[UserIndex].Char.body,
							Declaraciones.UserList[UserIndex].Char.Head, Declaraciones.UserList[UserIndex].Char.heading,
							Declaraciones.NingunArma, Declaraciones.NingunEscudo, Declaraciones.NingunCasco);
				}
			} else {
				if (Declaraciones.UserList[UserIndex].flags.invisible == 0) {
					UsUaRiOs.SetInvisible(UserIndex, Declaraciones.UserList[UserIndex].Char.CharIndex, false);
					WriteConsoleMsg(UserIndex, "¡Has vuelto a ser visible!", FontTypeNames.FONTTYPE_INFO);
				}
			}
		}
	}

	/* '' */
	/* ' Handles the "PickUp" message. */
	/* ' */
	/* ' @param userIndex The index of the user sending the message. */

	static void HandlePickUp(int UserIndex) {
		/* '*************************************************** */
		/* 'Author: Juan Martín Sotuyo Dodero (Maraxus) */
		/* 'Last Modification: 07/25/09 */
		/*
		 * '02/26/2006: Marco - Agregué un checkeo por si el usuario trata de
		 * agarrar un item mientras comercia.
		 */
		/* '*************************************************** */
		/* 'Remove packet ID */
		Declaraciones.UserList[UserIndex].incomingData.ReadByte();

		/* 'If dead, it can't pick up objects */
		if (Declaraciones.UserList[UserIndex].flags.Muerto == 1) {
			return;
		}

		/*
		 * 'If user is trading items and attempts to pickup an item, he's
		 * cheating, so we kick him.
		 */
		if (Declaraciones.UserList[UserIndex].flags.Comerciando) {
			return;
		}

		/* 'Lower rank administrators can't pick up items */
		if (Declaraciones.UserList[UserIndex].flags.Privilegios && PlayerType.Consejero) {
			if (!Declaraciones.UserList[UserIndex].flags.Privilegios && PlayerType.RoleMaster) {
				WriteConsoleMsg(UserIndex, "No puedes tomar ningún objeto.", FontTypeNames.FONTTYPE_INFO);
				return;
			}
		}

		InvUsuario.GetObj(UserIndex);
	}

	/* '' */
	/* ' Handles the "SafeToggle" message. */
	/* ' */
	/* ' @param userIndex The index of the user sending the message. */

	static void HandleSafeToggle(int UserIndex) {
		/* '*************************************************** */
		/* 'Author: Juan Martín Sotuyo Dodero (Maraxus) */
		/* 'Last Modification: 05/17/06 */
		/* ' */
		/* '*************************************************** */
		/* 'Remove packet ID */
		Declaraciones.UserList[UserIndex].incomingData.ReadByte();

		if (Declaraciones.UserList[UserIndex].flags.Seguro) {
			/* 'Call WriteSafeModeOff(UserIndex) */
			WriteMultiMessage(UserIndex, eMessages.SafeModeOff);
		} else {
			/* 'Call WriteSafeModeOn(UserIndex) */
			WriteMultiMessage(UserIndex, eMessages.SafeModeOn);
		}

		Declaraciones.UserList[UserIndex].flags.Seguro = !Declaraciones.UserList[UserIndex].flags.Seguro;
	}

	/* '' */
	/* ' Handles the "ResuscitationSafeToggle" message. */
	/* ' */
	/* ' @param userIndex The index of the user sending the message. */

	static void HandleResuscitationToggle(int UserIndex) {
		/* '*************************************************** */
		/* 'Author: Rapsodius */
		/* 'Creation Date: 10/10/07 */
		/* '*************************************************** */
		Declaraciones.UserList[UserIndex].incomingData.ReadByte();

		Declaraciones.UserList[UserIndex].flags.SeguroResu = !Declaraciones.UserList[UserIndex].flags.SeguroResu;

		if (Declaraciones.UserList[UserIndex].flags.SeguroResu) {
			/* 'Call WriteResuscitationSafeOn(UserIndex) */
			WriteMultiMessage(UserIndex, eMessages.ResuscitationSafeOn);
		} else {
			/* 'Call WriteResuscitationSafeOff(UserIndex) */
			WriteMultiMessage(UserIndex, eMessages.ResuscitationSafeOff);
		}
	}

	/* '' */
	/* ' Handles the "RequestGuildLeaderInfo" message. */
	/* ' */
	/* ' @param userIndex The index of the user sending the message. */

	static void HandleRequestGuildLeaderInfo(int UserIndex) {
		/* '*************************************************** */
		/* 'Author: Juan Martín Sotuyo Dodero (Maraxus) */
		/* 'Last Modification: 05/17/06 */
		/* ' */
		/* '*************************************************** */
		/* 'Remove packet ID */
		Declaraciones.UserList[UserIndex].incomingData.ReadByte();

		modGuilds.SendGuildLeaderInfo(UserIndex);
	}

	/* '' */
	/* ' Handles the "RequestAtributes" message. */
	/* ' */
	/* ' @param userIndex The index of the user sending the message. */

	static void HandleRequestAtributes(int UserIndex) {
		/* '*************************************************** */
		/* 'Author: Juan Martín Sotuyo Dodero (Maraxus) */
		/* 'Last Modification: 05/17/06 */
		/* ' */
		/* '*************************************************** */
		/* 'Remove packet ID */
		Declaraciones.UserList[UserIndex].incomingData.ReadByte();

		WriteAttributes(UserIndex);
	}

	/* '' */
	/* ' Handles the "RequestFame" message. */
	/* ' */
	/* ' @param userIndex The index of the user sending the message. */

	static void HandleRequestFame(int UserIndex) {
		/* '*************************************************** */
		/* 'Author: Juan Martín Sotuyo Dodero (Maraxus) */
		/* 'Last Modification: 05/17/06 */
		/* ' */
		/* '*************************************************** */
		/* 'Remove packet ID */
		Declaraciones.UserList[UserIndex].incomingData.ReadByte();

		UsUaRiOs.EnviarFama(UserIndex);
	}

	/* '' */
	/* ' Handles the "RequestSkills" message. */
	/* ' */
	/* ' @param userIndex The index of the user sending the message. */

	static void HandleRequestSkills(int UserIndex) {
		/* '*************************************************** */
		/* 'Author: Juan Martín Sotuyo Dodero (Maraxus) */
		/* 'Last Modification: 05/17/06 */
		/* ' */
		/* '*************************************************** */
		/* 'Remove packet ID */
		Declaraciones.UserList[UserIndex].incomingData.ReadByte();

		WriteSendSkills(UserIndex);
	}

	/* '' */
	/* ' Handles the "RequestMiniStats" message. */
	/* ' */
	/* ' @param userIndex The index of the user sending the message. */

	static void HandleRequestMiniStats(int UserIndex) {
		/* '*************************************************** */
		/* 'Author: Juan Martín Sotuyo Dodero (Maraxus) */
		/* 'Last Modification: 05/17/06 */
		/* ' */
		/* '*************************************************** */
		/* 'Remove packet ID */
		Declaraciones.UserList[UserIndex].incomingData.ReadByte();

		WriteMiniStats(UserIndex);
	}

	/* '' */
	/* ' Handles the "CommerceEnd" message. */
	/* ' */
	/* ' @param userIndex The index of the user sending the message. */

	static void HandleCommerceEnd(int UserIndex) {
		/* '*************************************************** */
		/* 'Author: Juan Martín Sotuyo Dodero (Maraxus) */
		/* 'Last Modification: 05/17/06 */
		/* ' */
		/* '*************************************************** */
		/* 'Remove packet ID */
		Declaraciones.UserList[UserIndex].incomingData.ReadByte();

		/* 'User quits commerce mode */
		Declaraciones.UserList[UserIndex].flags.Comerciando = false;
		WriteCommerceEnd(UserIndex);
	}

	/* '' */
	/* ' Handles the "UserCommerceEnd" message. */
	/* ' */
	/* ' @param userIndex The index of the user sending the message. */

	static void HandleUserCommerceEnd(int UserIndex) {
		/* '*************************************************** */
		/* 'Author: Juan Martín Sotuyo Dodero (Maraxus) */
		/* 'Last Modification: 11/03/2010 */
		/*
		 * '11/03/2010: ZaMa - Le avisa por consola al que cencela que dejo de
		 * comerciar.
		 */
		/* '*************************************************** */
		/* 'Remove packet ID */
		Declaraciones.UserList[UserIndex].incomingData.ReadByte();

		/* 'Quits commerce mode with user */
		if (Declaraciones.UserList[UserIndex].ComUsu.DestUsu > 0) {
			if (Declaraciones.UserList[Declaraciones.UserList[UserIndex].ComUsu.DestUsu].ComUsu.DestUsu == UserIndex) {
				WriteConsoleMsg(Declaraciones.UserList[UserIndex].ComUsu.DestUsu,
						Declaraciones.UserList[UserIndex].Name + " ha dejado de comerciar con vos.",
						FontTypeNames.FONTTYPE_TALK);
				mdlCOmercioConUsuario.FinComerciarUsu(Declaraciones.UserList[UserIndex].ComUsu.DestUsu);

				/* 'Send data in the outgoing buffer of the other user */
				FlushBuffer(Declaraciones.UserList[UserIndex].ComUsu.DestUsu);
			}
		}

		mdlCOmercioConUsuario.FinComerciarUsu(UserIndex);
		WriteConsoleMsg(UserIndex, "Has dejado de comerciar.", FontTypeNames.FONTTYPE_TALK);
	}

	/* '' */
	/* ' Handles the "UserCommerceConfirm" message. */
	/* ' */
	/* ' @param userIndex The index of the user sending the message. */
	static void HandleUserCommerceConfirm(int UserIndex) {
		/* '*************************************************** */
		/* 'Author: ZaMa */
		/* 'Last Modification: 14/12/2009 */
		/* ' */
		/* '*************************************************** */

		/* 'Remove packet ID */
		Declaraciones.UserList[UserIndex].incomingData.ReadByte();

		/* 'Validate the commerce */
		if (mdlCOmercioConUsuario.PuedeSeguirComerciando(UserIndex)) {
			/* 'Tell the other user the confirmation of the offer */
			WriteUserOfferConfirm(Declaraciones.UserList[UserIndex].ComUsu.DestUsu);
			Declaraciones.UserList[UserIndex].ComUsu.Confirmo = true;
		}

	}

	static void HandleCommerceChat(int UserIndex) {
		/* '*************************************************** */
		/* 'Author: ZaMa */
		/* 'Last Modification: 03/12/2009 */
		/* ' */
		/* '*************************************************** */
		if (Declaraciones.UserList[UserIndex].incomingData.length < 3) {
			throw new RuntimeException("Err . Raise UserList ( UserIndex ) . incomingData . NotEnoughDataErrCode");
			return;
		}

		/* FIXME: ON ERROR GOTO ErrHandler */

		/*
		 * 'This packet contains strings, make a copy of the data to prevent
		 * losses if it's not complete yet...
		 */
		clsByteQueue buffer;
		buffer = new clsByteQueue();
		buffer.CopyBuffer(Declaraciones.UserList[UserIndex].incomingData);

		/* 'Remove packet ID */
		buffer.ReadByte();

		String Chat;

		Chat = buffer.ReadASCIIString();

		if (vb6.LenB(Chat) != 0) {
			if (mdlCOmercioConUsuario.PuedeSeguirComerciando(UserIndex)) {
				/* 'Analize chat... */
				Statistics.ParseChat(Chat);

				Chat = Declaraciones.UserList[UserIndex].Name + "> " + Chat;
				WriteCommerceChat(UserIndex, Chat, FontTypeNames.FONTTYPE_PARTY);
				WriteCommerceChat(Declaraciones.UserList[UserIndex].ComUsu.DestUsu, Chat, FontTypeNames.FONTTYPE_PARTY);
			}
		}

		/*
		 * 'If we got here then packet is complete, copy data back to original
		 * queue
		 */
		Declaraciones.UserList[UserIndex].incomingData.CopyBuffer(buffer);

		/* FIXME: ErrHandler : */
		int ERROR;
		/* ERROR = Err . Number */
		/* FIXME: ON ERROR GOTO 0 */

		/* 'Destroy auxiliar buffer */
		buffer = null;

		if (ERROR != 0) {
			throw new RuntimeException("Err . Raise ERROR");
		}
	}

	/* '' */
	/* ' Handles the "BankEnd" message. */
	/* ' */
	/* ' @param userIndex The index of the user sending the message. */

	static void HandleBankEnd(int UserIndex) {
		/* '*************************************************** */
		/* 'Author: Juan Martín Sotuyo Dodero (Maraxus) */
		/* 'Last Modification: 05/17/06 */
		/* ' */
		/* '*************************************************** */
		/* 'Remove packet ID */
		Declaraciones.UserList[UserIndex].incomingData.ReadByte();

		/* 'User exits banking mode */
		Declaraciones.UserList[UserIndex].flags.Comerciando = false;
		WriteBankEnd(UserIndex);
	}

	/* '' */
	/* ' Handles the "UserCommerceOk" message. */
	/* ' */
	/* ' @param userIndex The index of the user sending the message. */

	static void HandleUserCommerceOk(int UserIndex) {
		/* '*************************************************** */
		/* 'Author: Juan Martín Sotuyo Dodero (Maraxus) */
		/* 'Last Modification: 05/17/06 */
		/* ' */
		/* '*************************************************** */
		/* 'Remove packet ID */
		Declaraciones.UserList[UserIndex].incomingData.ReadByte();

		/* 'Trade accepted */
		mdlCOmercioConUsuario.AceptarComercioUsu(UserIndex);
	}

	/* '' */
	/* ' Handles the "UserCommerceReject" message. */
	/* ' */
	/* ' @param userIndex The index of the user sending the message. */

	static void HandleUserCommerceReject(int UserIndex) {
		/* '*************************************************** */
		/* 'Author: Juan Martín Sotuyo Dodero (Maraxus) */
		/* 'Last Modification: 05/17/06 */
		/* ' */
		/* '*************************************************** */
		int otherUser;

		/* 'Remove packet ID */
		Declaraciones.UserList[UserIndex].incomingData.ReadByte();

		otherUser = Declaraciones.UserList[UserIndex].ComUsu.DestUsu;

		/* 'Offer rejected */
		if (otherUser > 0) {
			if (Declaraciones.UserList[otherUser].flags.UserLogged) {
				WriteConsoleMsg(otherUser, Declaraciones.UserList[UserIndex].Name + " ha rechazado tu oferta.",
						FontTypeNames.FONTTYPE_TALK);
				mdlCOmercioConUsuario.FinComerciarUsu(otherUser);

				/* 'Send data in the outgoing buffer of the other user */
				FlushBuffer(otherUser);
			}
		}

		WriteConsoleMsg(UserIndex, "Has rechazado la oferta del otro usuario.", FontTypeNames.FONTTYPE_TALK);
		mdlCOmercioConUsuario.FinComerciarUsu(UserIndex);
	}

	/* '' */
	/* ' Handles the "Drop" message. */
	/* ' */
	/* ' @param userIndex The index of the user sending the message. */

	static void HandleDrop(int UserIndex) {
		/* '*************************************************** */
		/* 'Author: Juan Martín Sotuyo Dodero (Maraxus) */
		/* 'Last Modification: 07/25/09 */
		/*
		 * '07/25/09: Marco - Agregué un checkeo para patear a los usuarios que
		 * tiran items mientras comercian.
		 */
		/* '*************************************************** */
		if (Declaraciones.UserList[UserIndex].incomingData.length < 4) {
			throw new RuntimeException("Err . Raise UserList ( UserIndex ) . incomingData . NotEnoughDataErrCode");
			return;
		}

		int Slot;
		int Amount;

		/* 'Remove packet ID */
		Declaraciones.UserList[UserIndex].incomingData.ReadByte();

		Slot = Declaraciones.UserList[UserIndex].incomingData.ReadByte();
		Amount = Declaraciones.UserList[UserIndex].incomingData.ReadInteger();

		/*
		 * 'low rank admins can't drop item. Neither can the dead nor those
		 * sailing.
		 */
		if (Declaraciones.UserList[UserIndex].flags.Navegando == 1
				|| Declaraciones.UserList[UserIndex].flags.Muerto == 1
				|| ((Declaraciones.UserList[UserIndex].flags.Privilegios && PlayerType.Consejero) != 0
						&& (!Declaraciones.UserList[UserIndex].flags.Privilegios && PlayerType.RoleMaster) != 0)) {
			return;
		}

		/*
		 * 'If the user is trading, he can't drop items => He's cheating, we
		 * kick him.
		 */
		if (Declaraciones.UserList[UserIndex].flags.Comerciando) {
			return;
		}

		/* 'Are we dropping gold or other items?? */
		if (Slot == Declaraciones.FLAGORO) {
			/* 'Don't drop too much gold */
			if (Amount > 10000) {
				return;
			}

			InvUsuario.TirarOro(Amount, UserIndex);

			WriteUpdateGold(UserIndex);
		} else {
			/* 'Only drop valid slots */
			if (Slot <= Declaraciones.MAX_INVENTORY_SLOTS && Slot > 0) {
				if (Declaraciones.UserList[UserIndex].Invent.Object[Slot].ObjIndex == 0) {
					return;
				}

				InvUsuario.DropObj(UserIndex, Slot, Amount, Declaraciones.UserList[UserIndex].Pos.Map,
						Declaraciones.UserList[UserIndex].Pos.X, Declaraciones.UserList[UserIndex].Pos.Y, true);
			}
		}
	}

	/* '' */
	/* ' Handles the "CastSpell" message. */
	/* ' */
	/* ' @param userIndex The index of the user sending the message. */

	static void HandleCastSpell(int UserIndex) {
		/* '*************************************************** */
		/* 'Author: Juan Martín Sotuyo Dodero (Maraxus) */
		/* 'Last Modification: 05/17/06 */
		/*
		 * '13/11/2009: ZaMa - Ahora los npcs pueden atacar al usuario si quizo
		 * castear un hechizo
		 */
		/* '*************************************************** */
		if (Declaraciones.UserList[UserIndex].incomingData.length < 2) {
			throw new RuntimeException("Err . Raise UserList ( UserIndex ) . incomingData . NotEnoughDataErrCode");
			return;
		}

		/* 'Remove packet ID */
		Declaraciones.UserList[UserIndex].incomingData.ReadByte();

		int Spell;

		Spell = Declaraciones.UserList[UserIndex].incomingData.ReadByte();

		if (Declaraciones.UserList[UserIndex].flags.Muerto == 1) {
			WriteConsoleMsg(UserIndex, "¡¡Estás muerto!!", FontTypeNames.FONTTYPE_INFO);
			return;
		}

		/* 'Now you can be atacked */
		Declaraciones.UserList[UserIndex].flags.NoPuedeSerAtacado = false;

		if (Spell < 1) {
			Declaraciones.UserList[UserIndex].flags.Hechizo = 0;
			return;
		} else if (Spell > Declaraciones.MAXUSERHECHIZOS) {
			Declaraciones.UserList[UserIndex].flags.Hechizo = 0;
			return;
		}

		Declaraciones.UserList[UserIndex].flags.Hechizo = Declaraciones.UserList[UserIndex].Stats.UserHechizos[Spell];
	}

	/* '' */
	/* ' Handles the "LeftClick" message. */
	/* ' */
	/* ' @param userIndex The index of the user sending the message. */

	static void HandleLeftClick(int UserIndex) {
		/* '*************************************************** */
		/* 'Author: Juan Martín Sotuyo Dodero (Maraxus) */
		/* 'Last Modification: 05/17/06 */
		/* ' */
		/* '*************************************************** */
		if (Declaraciones.UserList[UserIndex].incomingData.length < 3) {
			throw new RuntimeException("Err . Raise UserList ( UserIndex ) . incomingData . NotEnoughDataErrCode");
			return;
		}

		/* 'Remove packet ID */
		Declaraciones.UserList[UserIndex].incomingData.ReadByte();

		int X;
		int Y;

		X = Declaraciones.UserList[UserIndex].incomingData.ReadByte();
		Y = Declaraciones.UserList[UserIndex].incomingData.ReadByte();

		Extra.LookatTile(UserIndex, Declaraciones.UserList[UserIndex].Pos.Map, X, Y);
	}

	/* '' */
	/* ' Handles the "DoubleClick" message. */
	/* ' */
	/* ' @param userIndex The index of the user sending the message. */

	static void HandleDoubleClick(int UserIndex) {
		/* '*************************************************** */
		/* 'Author: Juan Martín Sotuyo Dodero (Maraxus) */
		/* 'Last Modification: 05/17/06 */
		/* ' */
		/* '*************************************************** */
		if (Declaraciones.UserList[UserIndex].incomingData.length < 3) {
			throw new RuntimeException("Err . Raise UserList ( UserIndex ) . incomingData . NotEnoughDataErrCode");
			return;
		}

		/* 'Remove packet ID */
		Declaraciones.UserList[UserIndex].incomingData.ReadByte();

		int X;
		int Y;

		X = Declaraciones.UserList[UserIndex].incomingData.ReadByte();
		Y = Declaraciones.UserList[UserIndex].incomingData.ReadByte();

		Acciones.Accion(UserIndex, Declaraciones.UserList[UserIndex].Pos.Map, X, Y);
	}

	/* '' */
	/* ' Handles the "Work" message. */
	/* ' */
	/* ' @param userIndex The index of the user sending the message. */

	static void HandleWork(int UserIndex) {
		/* '*************************************************** */
		/* 'Author: Juan Martín Sotuyo Dodero (Maraxus) */
		/* 'Last Modification: 13/01/2010 (ZaMa) */
		/* '13/01/2010: ZaMa - El pirata se puede ocultar en barca */
		/* '*************************************************** */
		if (Declaraciones.UserList[UserIndex].incomingData.length < 2) {
			throw new RuntimeException("Err . Raise UserList ( UserIndex ) . incomingData . NotEnoughDataErrCode");
			return;
		}

		/* 'Remove packet ID */
		Declaraciones.UserList[UserIndex].incomingData.ReadByte();

		eSkill Skill;

		Skill = Declaraciones.UserList[UserIndex].incomingData.ReadByte();

		if (Declaraciones.UserList[UserIndex].flags.Muerto == 1) {
			return;
		}

		/* 'If exiting, cancel */
		UsUaRiOs.CancelExit(UserIndex);

		switch (Skill) {

		case Robar:
		case Magia:
		case Domar:
			WriteMultiMessage(UserIndex, eMessages.WorkRequestTarget, Skill);

			break;

		case Ocultarse:

			/* ' Verifico si se peude ocultar en este mapa */
			if (Declaraciones.MapInfo[Declaraciones.UserList[UserIndex].Pos.Map].OcultarSinEfecto == 1) {
				WriteConsoleMsg(UserIndex, "¡Ocultarse no funciona aquí!", FontTypeNames.FONTTYPE_INFO);
				return;
			}

			if (Declaraciones.UserList[UserIndex].flags.EnConsulta) {
				WriteConsoleMsg(UserIndex, "No puedes ocultarte si estás en consulta.", FontTypeNames.FONTTYPE_INFO);
				return;
			}

			if (Declaraciones.UserList[UserIndex].flags.Navegando == 1) {
				if (Declaraciones.UserList[UserIndex].clase != eClass.Pirat) {
					/* '[CDT 17-02-2004] */
					if (!Declaraciones.UserList[UserIndex].flags.UltimoMensaje == 3) {
						WriteConsoleMsg(UserIndex, "No puedes ocultarte si estás navegando.",
								FontTypeNames.FONTTYPE_INFO);
						Declaraciones.UserList[UserIndex].flags.UltimoMensaje = 3;
					}
					/* '[/CDT] */
					return;
				}
			}

			if (Declaraciones.UserList[UserIndex].flags.Oculto == 1) {
				/* '[CDT 17-02-2004] */
				if (!Declaraciones.UserList[UserIndex].flags.UltimoMensaje == 2) {
					WriteConsoleMsg(UserIndex, "Ya estás oculto.", FontTypeNames.FONTTYPE_INFO);
					Declaraciones.UserList[UserIndex].flags.UltimoMensaje = 2;
				}
				/* '[/CDT] */
				return;
			}

			Trabajo.DoOcultarse(UserIndex);

			break;
		}

	}

	/* '' */
	/* ' Handles the "InitCrafting" message. */
	/* ' */
	/* ' @param userIndex The index of the user sending the message. */

	static void HandleInitCrafting(int UserIndex) {
		/* '*************************************************** */
		/* 'Author: ZaMa */
		/* 'Last Modification: 29/01/2010 */
		/* ' */
		/* '*************************************************** */
		int TotalItems;
		int ItemsPorCiclo;

		/* 'Remove packet ID */
		Declaraciones.UserList[UserIndex].incomingData.ReadByte();

		TotalItems = Declaraciones.UserList[UserIndex].incomingData.ReadLong();
		ItemsPorCiclo = Declaraciones.UserList[UserIndex].incomingData.ReadInteger();

		if (TotalItems > 0) {

			Declaraciones.UserList[UserIndex].Construir.Cantidad = TotalItems;
			Declaraciones.UserList[UserIndex].Construir.PorCiclo = SistemaCombate
					.MinimoInt(Trabajo.MaxItemsConstruibles(UserIndex), ItemsPorCiclo);
		}
	}

	/* '' */
	/* ' Handles the "UseSpellMacro" message. */
	/* ' */
	/* ' @param userIndex The index of the user sending the message. */

	static void HandleUseSpellMacro(int UserIndex) {
		/* '*************************************************** */
		/* 'Author: Juan Martín Sotuyo Dodero (Maraxus) */
		/* 'Last Modification: 05/17/06 */
		/* ' */
		/* '*************************************************** */
		/* 'Remove packet ID */
		Declaraciones.UserList[UserIndex].incomingData.ReadByte();

		modSendData.SendData(SendTarget.ToAdmins, UserIndex,
				PrepareMessageConsoleMsg(
						Declaraciones.UserList[UserIndex].Name + " fue expulsado por Anti-macro de hechizos.",
						FontTypeNames.FONTTYPE_VENENO));
		WriteErrorMsg(UserIndex,
				"Has sido expulsado por usar macro de hechizos. Recomendamos leer el reglamento sobre el tema macros.");
		FlushBuffer(UserIndex);
		TCP.CloseSocket(UserIndex);
	}

	/* '' */
	/* ' Handles the "UseItem" message. */
	/* ' */
	/* ' @param userIndex The index of the user sending the message. */

	static void HandleUseItem(int UserIndex) {
		/* '*************************************************** */
		/* 'Author: Juan Martín Sotuyo Dodero (Maraxus) */
		/* 'Last Modification: 05/17/06 */
		/* ' */
		/* '*************************************************** */
		if (Declaraciones.UserList[UserIndex].incomingData.length < 2) {
			throw new RuntimeException("Err . Raise UserList ( UserIndex ) . incomingData . NotEnoughDataErrCode");
			return;
		}

		/* 'Remove packet ID */
		Declaraciones.UserList[UserIndex].incomingData.ReadByte();

		int Slot;

		Slot = Declaraciones.UserList[UserIndex].incomingData.ReadByte();

		if (Slot <= Declaraciones.UserList[UserIndex].CurrentInventorySlots && Slot > 0) {
			if (Declaraciones.UserList[UserIndex].Invent.Object[Slot].ObjIndex == 0) {
				return;
			}
		}

		if (Declaraciones.UserList[UserIndex].flags.Meditando) {
			/* 'The error message should have been provided by the client. */
			return;
		}

		/* # IF SeguridadAlkon THEN */
		/* # END IF */

		InvUsuario.UseInvItem(UserIndex, Slot);
	}

	/* '' */
	/* ' Handles the "CraftBlacksmith" message. */
	/* ' */
	/* ' @param userIndex The index of the user sending the message. */

	static void HandleCraftBlacksmith(int UserIndex) {
		/* '*************************************************** */
		/* 'Author: Juan Martín Sotuyo Dodero (Maraxus) */
		/* 'Last Modification: 05/17/06 */
		/* ' */
		/* '*************************************************** */
		if (Declaraciones.UserList[UserIndex].incomingData.length < 3) {
			throw new RuntimeException("Err . Raise UserList ( UserIndex ) . incomingData . NotEnoughDataErrCode");
			return;
		}

		/* 'Remove packet ID */
		Declaraciones.UserList[UserIndex].incomingData.ReadByte();

		int Item;

		Item = Declaraciones.UserList[UserIndex].incomingData.ReadInteger();

		if (Item < 1) {
			return;
		}

		if (Declaraciones.ObjData[Item].SkHerreria == 0) {
			return;
		}

		if (!modNuevoTimer.IntervaloPermiteTrabajar(UserIndex)) {
			return;
		}
		Trabajo.HerreroConstruirItem(UserIndex, Item);
	}

	/* '' */
	/* ' Handles the "CraftCarpenter" message. */
	/* ' */
	/* ' @param userIndex The index of the user sending the message. */

	static void HandleCraftCarpenter(int UserIndex) {
		/* '*************************************************** */
		/* 'Author: Juan Martín Sotuyo Dodero (Maraxus) */
		/* 'Last Modification: 05/17/06 */
		/* ' */
		/* '*************************************************** */
		if (Declaraciones.UserList[UserIndex].incomingData.length < 3) {
			throw new RuntimeException("Err . Raise UserList ( UserIndex ) . incomingData . NotEnoughDataErrCode");
			return;
		}

		/* 'Remove packet ID */
		Declaraciones.UserList[UserIndex].incomingData.ReadByte();

		int Item;

		Item = Declaraciones.UserList[UserIndex].incomingData.ReadInteger();

		if (Item < 1) {
			return;
		}

		if (Declaraciones.ObjData[Item].SkCarpinteria == 0) {
			return;
		}

		if (!modNuevoTimer.IntervaloPermiteTrabajar(UserIndex)) {
			return;
		}
		Trabajo.CarpinteroConstruirItem(UserIndex, Item);
	}

	/* '' */
	/* ' Handles the "WorkLeftClick" message. */
	/* ' */
	/* ' @param userIndex The index of the user sending the message. */

	static void HandleWorkLeftClick(int UserIndex) {
		/* '*************************************************** */
		/* 'Author: Juan Martín Sotuyo Dodero (Maraxus) */
		/* 'Last Modification: 14/01/2010 (ZaMa) */
		/*
		 * '16/11/2009: ZaMa - Agregada la posibilidad de extraer madera elfica.
		 */
		/*
		 * '12/01/2010: ZaMa - Ahora se admiten armas arrojadizas (proyectiles
		 * sin municiones).
		 */
		/*
		 * '14/01/2010: ZaMa - Ya no se pierden municiones al atacar npcs con
		 * dueno.
		 */
		/* '*************************************************** */
		if (Declaraciones.UserList[UserIndex].incomingData.length < 4) {
			throw new RuntimeException("Err . Raise UserList ( UserIndex ) . incomingData . NotEnoughDataErrCode");
			return;
		}

		/* 'Remove packet ID */
		Declaraciones.UserList[UserIndex].incomingData.ReadByte();

		int X;
		int Y;
		eSkill Skill;
		int DummyInt;
		/* 'Target user */
		int tU;
		/* 'Target NPC */
		int tN;

		int WeaponIndex;

		X = Declaraciones.UserList[UserIndex].incomingData.ReadByte();
		Y = Declaraciones.UserList[UserIndex].incomingData.ReadByte();

		Skill = Declaraciones.UserList[UserIndex].incomingData.ReadByte();

		if (Declaraciones.UserList[UserIndex].flags.Muerto == 1 || Declaraciones.UserList[UserIndex].flags.Descansar
				|| Declaraciones.UserList[UserIndex].flags.Meditando
				|| !Extra.InMapBounds(Declaraciones.UserList[UserIndex].Pos.Map, X, Y)) {
			return;
		}

		if (!Extra.InRangoVision(UserIndex, X, Y)) {
			WritePosUpdate(UserIndex);
			return;
		}

		/* 'If exiting, cancel */
		UsUaRiOs.CancelExit(UserIndex);

		switch (Skill) {
		case Proyectiles:

			/* 'Check attack interval */
			if (!modNuevoTimer.IntervaloPermiteAtacar(UserIndex, false)) {
				return;
			}
			/* 'Check Magic interval */
			if (!modNuevoTimer.IntervaloPermiteLanzarSpell(UserIndex, false)) {
				return;
			}
			/* 'Check bow's interval */
			if (!modNuevoTimer.IntervaloPermiteUsarArcos(UserIndex)) {
				return;
			}

			SistemaCombate.LanzarProyectil(UserIndex, X, Y);

			break;

		case Magia:
			/* 'Check the map allows spells to be casted. */
			if (Declaraciones.MapInfo[Declaraciones.UserList[UserIndex].Pos.Map].MagiaSinEfecto > 0) {
				WriteConsoleMsg(UserIndex, "Una fuerza oscura te impide canalizar tu energía.",
						FontTypeNames.FONTTYPE_FIGHT);
				return;
			}

			/* 'Target whatever is in that tile */
			Extra.LookatTile(UserIndex, Declaraciones.UserList[UserIndex].Pos.Map, X, Y);

			/* 'If it's outside range log it and exit */
			if (vb6.Abs(Declaraciones.UserList[UserIndex].Pos.X - X) > AI.RANGO_VISION_X
					|| vb6.Abs(Declaraciones.UserList[UserIndex].Pos.Y - Y) > AI.RANGO_VISION_Y) {
				General.LogCheating("Ataque fuera de rango de " + Declaraciones.UserList[UserIndex].Name + "("
						+ Declaraciones.UserList[UserIndex].Pos.Map + "/" + Declaraciones.UserList[UserIndex].Pos.X
						+ "/" + Declaraciones.UserList[UserIndex].Pos.Y + ") ip: "
						+ Declaraciones.UserList[UserIndex].ip + " a la posición ("
						+ Declaraciones.UserList[UserIndex].Pos.Map + "/" + X + "/" + Y + ")");
				return;
			}

			/* 'Check bow's interval */
			if (!modNuevoTimer.IntervaloPermiteUsarArcos(UserIndex, false)) {
				return;
			}

			/* 'Check Spell-Hit interval */
			if (!modNuevoTimer.IntervaloPermiteGolpeMagia(UserIndex)) {
				/* 'Check Magic interval */
				if (!modNuevoTimer.IntervaloPermiteLanzarSpell(UserIndex)) {
					return;
				}
			}

			/* 'Check intervals and cast */
			if (Declaraciones.UserList[UserIndex].flags.Hechizo > 0) {
				modHechizos.LanzarHechizo(Declaraciones.UserList[UserIndex].flags.Hechizo, UserIndex);
				Declaraciones.UserList[UserIndex].flags.Hechizo = 0;
			} else {
				WriteConsoleMsg(UserIndex, "¡Primero selecciona el hechizo que quieres lanzar!",
						FontTypeNames.FONTTYPE_INFO);
			}

			break;

		case Pesca:
			WeaponIndex = Declaraciones.UserList[UserIndex].Invent.WeaponEqpObjIndex;
			if (WeaponIndex == 0) {
				return;
			}

			/* 'Check interval */
			if (!modNuevoTimer.IntervaloPermiteTrabajar(UserIndex)) {
				return;
			}

			/* 'Basado en la idea de Barrin */
			/* 'Comentario por Barrin: jah, "basado", caradura ! ^^ */
			if (Declaraciones.MapData[Declaraciones.UserList[UserIndex].Pos.Map][Declaraciones.UserList[UserIndex].Pos.X][Declaraciones.UserList[UserIndex].Pos.Y].trigger == 1) {
				WriteConsoleMsg(UserIndex, "No puedes pescar desde donde te encuentras.", FontTypeNames.FONTTYPE_INFO);
				return;
			}

			if (General.HayAgua(Declaraciones.UserList[UserIndex].Pos.Map, X, Y)) {
				switch (WeaponIndex) {
				case Declaraciones.CANA_PESCA:
				case Declaraciones.CANA_PESCA_NEWBIE:
					Trabajo.DoPescar(UserIndex);

					break;

				case Declaraciones.RED_PESCA:

					DummyInt = Declaraciones.MapData[Declaraciones.UserList[UserIndex].Pos.Map][X][Y].ObjInfo.ObjIndex;

					if (DummyInt == 0) {
						WriteConsoleMsg(UserIndex, "No hay un yacimiento de peces donde pescar.",
								FontTypeNames.FONTTYPE_INFO);
						return;
					}

					if (vb6.Abs(Declaraciones.UserList[UserIndex].Pos.X - X)
							+ vb6.Abs(Declaraciones.UserList[UserIndex].Pos.Y - Y) > 2) {
						WriteConsoleMsg(UserIndex, "Estás demasiado lejos para pescar.", FontTypeNames.FONTTYPE_INFO);
						return;
					}

					if (Declaraciones.UserList[UserIndex].Pos.X == X && Declaraciones.UserList[UserIndex].Pos.Y == Y) {
						WriteConsoleMsg(UserIndex, "No puedes pescar desde allí.", FontTypeNames.FONTTYPE_INFO);
						return;
					}

					/* '¿Hay un arbol normal donde clickeo? */
					if (Declaraciones.ObjData[DummyInt].OBJType == eOBJType.otYacimientoPez) {
						Trabajo.DoPescarRed(UserIndex);
					} else {
						WriteConsoleMsg(UserIndex, "No hay un yacimiento de peces donde pescar.",
								FontTypeNames.FONTTYPE_INFO);
						return;
					}

					break;

				default:
					/* 'Invalid item! */
					return;
					break;
				}

				/* 'Play sound! */
				modSendData.SendData(SendTarget.ToPCArea, UserIndex, PrepareMessagePlayWave(Declaraciones.SND_PESCAR,
						Declaraciones.UserList[UserIndex].Pos.X, Declaraciones.UserList[UserIndex].Pos.Y));
			} else {
				WriteConsoleMsg(UserIndex, "No hay agua donde pescar. Busca un lago, río o mar.",
						FontTypeNames.FONTTYPE_INFO);
			}

		case Robar:
			/* 'Does the map allow us to steal here? */
			if (Declaraciones.MapInfo[Declaraciones.UserList[UserIndex].Pos.Map].Pk) {

				/* 'Check interval */
				if (!modNuevoTimer.IntervaloPermiteTrabajar(UserIndex)) {
					return;
				}

				/* 'Target whatever is in that tile */
				Extra.LookatTile(UserIndex, Declaraciones.UserList[UserIndex].Pos.Map, X, Y);

				tU = Declaraciones.UserList[UserIndex].flags.TargetUser;

				if (tU > 0 && tU != UserIndex) {
					/* 'Can't steal administrative players */
					if (Declaraciones.UserList[tU].flags.Privilegios && PlayerType.User) {
						if (Declaraciones.UserList[tU].flags.Muerto == 0) {
							if (vb6.Abs(Declaraciones.UserList[UserIndex].Pos.X - X)
									+ vb6.Abs(Declaraciones.UserList[UserIndex].Pos.Y - Y) > 2) {
								WriteConsoleMsg(UserIndex, "Estás demasiado lejos.", FontTypeNames.FONTTYPE_INFO);
								return;
							}

							/* '17/09/02 */
							/* 'Check the trigger */
							if (Declaraciones.MapData[Declaraciones.UserList[tU].Pos.Map][X][Y].trigger == eTrigger.ZONASEGURA) {
								WriteConsoleMsg(UserIndex, "No puedes robar aquí.", FontTypeNames.FONTTYPE_WARNING);
								return;
							}

							if (Declaraciones.MapData[Declaraciones.UserList[UserIndex].Pos.Map][Declaraciones.UserList[UserIndex].Pos.X][Declaraciones.UserList[UserIndex].Pos.Y].trigger == eTrigger.ZONASEGURA) {
								WriteConsoleMsg(UserIndex, "No puedes robar aquí.", FontTypeNames.FONTTYPE_WARNING);
								return;
							}

							Trabajo.DoRobar(UserIndex, tU);
						}
					}
				} else {
					WriteConsoleMsg(UserIndex, "¡No hay a quien robarle!", FontTypeNames.FONTTYPE_INFO);
				}
			} else {
				WriteConsoleMsg(UserIndex, "¡No puedes robar en zonas seguras!", FontTypeNames.FONTTYPE_INFO);
			}

			break;

		case Talar:
			/* 'Check interval */
			if (!modNuevoTimer.IntervaloPermiteTrabajar(UserIndex)) {
				return;
			}

			WeaponIndex = Declaraciones.UserList[UserIndex].Invent.WeaponEqpObjIndex;

			if (WeaponIndex == 0) {
				WriteConsoleMsg(UserIndex, "Deberías equiparte el hacha.", FontTypeNames.FONTTYPE_INFO);
				return;
			}

			if (WeaponIndex != Declaraciones.HACHA_LENADOR && WeaponIndex != Declaraciones.HACHA_LENA_ELFICA
					&& WeaponIndex != Declaraciones.HACHA_LENADOR_NEWBIE) {
				/*
				 * ' Podemos llegar acá si el user equipó el anillo dsp de la U
				 * y antes del click
				 */
				return;
			}

			DummyInt = Declaraciones.MapData[Declaraciones.UserList[UserIndex].Pos.Map][X][Y].ObjInfo.ObjIndex;

			if (DummyInt > 0) {
				if (vb6.Abs(Declaraciones.UserList[UserIndex].Pos.X - X)
						+ vb6.Abs(Declaraciones.UserList[UserIndex].Pos.Y - Y) > 2) {
					WriteConsoleMsg(UserIndex, "Estás demasiado lejos.", FontTypeNames.FONTTYPE_INFO);
					return;
				}

				/* 'Barrin 29/9/03 */
				if (Declaraciones.UserList[UserIndex].Pos.X == X && Declaraciones.UserList[UserIndex].Pos.Y == Y) {
					WriteConsoleMsg(UserIndex, "No puedes talar desde allí.", FontTypeNames.FONTTYPE_INFO);
					return;
				}

				/* '¿Hay un arbol normal donde clickeo? */
				if (Declaraciones.ObjData[DummyInt].OBJType == eOBJType.otArboles) {
					if (WeaponIndex == Declaraciones.HACHA_LENADOR
							|| WeaponIndex == Declaraciones.HACHA_LENADOR_NEWBIE) {
						modSendData.SendData(SendTarget.ToPCArea, UserIndex,
								PrepareMessagePlayWave(Declaraciones.SND_TALAR, Declaraciones.UserList[UserIndex].Pos.X,
										Declaraciones.UserList[UserIndex].Pos.Y));
						Trabajo.DoTalar(UserIndex);
					} else {
						WriteConsoleMsg(UserIndex, "No puedes extraer lena de éste árbol con éste hacha.",
								FontTypeNames.FONTTYPE_INFO);
					}

					/* ' Arbol Elfico? */
				} else if (Declaraciones.ObjData[DummyInt].OBJType == eOBJType.otArbolElfico) {

					if (WeaponIndex == Declaraciones.HACHA_LENA_ELFICA) {
						modSendData.SendData(SendTarget.ToPCArea, UserIndex,
								PrepareMessagePlayWave(Declaraciones.SND_TALAR, Declaraciones.UserList[UserIndex].Pos.X,
										Declaraciones.UserList[UserIndex].Pos.Y));
						Trabajo.DoTalar(UserIndex, true);
					} else {
						WriteConsoleMsg(UserIndex, "El hacha utilizado no es suficientemente poderosa.",
								FontTypeNames.FONTTYPE_INFO);
					}
				}
			} else {
				WriteConsoleMsg(UserIndex, "No hay ningún árbol ahí.", FontTypeNames.FONTTYPE_INFO);
			}

			break;

		case Mineria:
			if (!modNuevoTimer.IntervaloPermiteTrabajar(UserIndex)) {
				return;
			}

			WeaponIndex = Declaraciones.UserList[UserIndex].Invent.WeaponEqpObjIndex;

			if (WeaponIndex == 0) {
				return;
			}

			if (WeaponIndex != Declaraciones.PIQUETE_MINERO && WeaponIndex != Declaraciones.PIQUETE_MINERO_NEWBIE) {
				/*
				 * ' Podemos llegar acá si el user equipó el anillo dsp de la U
				 * y antes del click
				 */
				return;
			}

			/* 'Target whatever is in the tile */
			Extra.LookatTile(UserIndex, Declaraciones.UserList[UserIndex].Pos.Map, X, Y);

			DummyInt = Declaraciones.MapData[Declaraciones.UserList[UserIndex].Pos.Map][X][Y].ObjInfo.ObjIndex;

			if (DummyInt > 0) {
				/* 'Check distance */
				if (vb6.Abs(Declaraciones.UserList[UserIndex].Pos.X - X)
						+ vb6.Abs(Declaraciones.UserList[UserIndex].Pos.Y - Y) > 2) {
					WriteConsoleMsg(UserIndex, "Estás demasiado lejos.", FontTypeNames.FONTTYPE_INFO);
					return;
				}

				/* '¿Hay un yacimiento donde clickeo? */
				if (Declaraciones.ObjData[DummyInt].OBJType == eOBJType.otYacimiento) {
					Trabajo.DoMineria(UserIndex);
				} else {
					WriteConsoleMsg(UserIndex, "Ahí no hay ningún yacimiento.", FontTypeNames.FONTTYPE_INFO);
				}
			} else {
				WriteConsoleMsg(UserIndex, "Ahí no hay ningún yacimiento.", FontTypeNames.FONTTYPE_INFO);
			}

			break;

		case Domar:
			/* 'Modificado 25/11/02 */
			/* 'Optimizado y solucionado el bug de la doma de */
			/* 'criaturas hostiles. */

			/* 'Target whatever is that tile */
			Extra.LookatTile(UserIndex, Declaraciones.UserList[UserIndex].Pos.Map, X, Y);
			tN = Declaraciones.UserList[UserIndex].flags.TargetNPC;

			if (tN > 0) {
				if (Declaraciones.Npclist[tN].flags.Domable > 0) {
					if (vb6.Abs(Declaraciones.UserList[UserIndex].Pos.X - X)
							+ vb6.Abs(Declaraciones.UserList[UserIndex].Pos.Y - Y) > 2) {
						WriteConsoleMsg(UserIndex, "Estás demasiado lejos.", FontTypeNames.FONTTYPE_INFO);
						return;
					}

					if (vb6.LenB(Declaraciones.Npclist[tN].flags.AttackedBy) != 0) {
						WriteConsoleMsg(UserIndex, "No puedes domar una criatura que está luchando con un jugador.",
								FontTypeNames.FONTTYPE_INFO);
						return;
					}

					Trabajo.DoDomar(UserIndex, tN);
				} else {
					WriteConsoleMsg(UserIndex, "No puedes domar a esa criatura.", FontTypeNames.FONTTYPE_INFO);
				}
			} else {
				WriteConsoleMsg(UserIndex, "¡No hay ninguna criatura allí!", FontTypeNames.FONTTYPE_INFO);
			}

			/* 'UGLY!!! This is a constant, not a skill!! */
			break;

		case Declaraciones.FundirMetal:
			/* 'Check interval */
			if (!modNuevoTimer.IntervaloPermiteTrabajar(UserIndex)) {
				return;
			}

			/* 'Check there is a proper item there */
			if (Declaraciones.UserList[UserIndex].flags.TargetObj > 0) {
				if (Declaraciones.ObjData[Declaraciones.UserList[UserIndex].flags.TargetObj].OBJType == eOBJType.otFragua) {
					/* 'Validate other items */
					if (Declaraciones.UserList[UserIndex].flags.TargetObjInvSlot < 1
							|| Declaraciones.UserList[UserIndex].flags.TargetObjInvSlot > Declaraciones.UserList[UserIndex].CurrentInventorySlots) {
						return;
					}

					/* ''chequeamos que no se zarpe duplicando oro */
					if (Declaraciones.UserList[UserIndex].Invent.Object[Declaraciones.UserList[UserIndex].flags.TargetObjInvSlot].ObjIndex != Declaraciones.UserList[UserIndex].flags.TargetObjInvIndex) {
						if (Declaraciones.UserList[UserIndex].Invent.Object[Declaraciones.UserList[UserIndex].flags.TargetObjInvSlot].ObjIndex == 0
								|| Declaraciones.UserList[UserIndex].Invent.Object[Declaraciones.UserList[UserIndex].flags.TargetObjInvSlot].Amount == 0) {
							WriteConsoleMsg(UserIndex, "No tienes más minerales.", FontTypeNames.FONTTYPE_INFO);
							return;
						}

						/* ''FUISTE */
						WriteErrorMsg(UserIndex, "Has sido expulsado por el sistema anti cheats.");
						FlushBuffer(UserIndex);
						TCP.CloseSocket(UserIndex);
						return;
					}
					if (Declaraciones.ObjData[Declaraciones.UserList[UserIndex].flags.TargetObjInvIndex].OBJType == eOBJType.otMinerales) {
						Trabajo.FundirMineral(UserIndex);
					} else if (Declaraciones.ObjData[Declaraciones.UserList[UserIndex].flags.TargetObjInvIndex].OBJType == eOBJType.otWeapon) {
						Trabajo.FundirArmas(UserIndex);
					}
				} else {
					WriteConsoleMsg(UserIndex, "Ahí no hay ninguna fragua.", FontTypeNames.FONTTYPE_INFO);
				}
			} else {
				WriteConsoleMsg(UserIndex, "Ahí no hay ninguna fragua.", FontTypeNames.FONTTYPE_INFO);
			}

			break;

		case Herreria:
			/* 'Target wehatever is in that tile */
			Extra.LookatTile(UserIndex, Declaraciones.UserList[UserIndex].Pos.Map, X, Y);

			if (Declaraciones.UserList[UserIndex].flags.TargetObj > 0) {
				if (Declaraciones.ObjData[Declaraciones.UserList[UserIndex].flags.TargetObj].OBJType == eOBJType.otYunque) {
					InvUsuario.EnivarArmasConstruibles(UserIndex);
					InvUsuario.EnivarArmadurasConstruibles(UserIndex);
					WriteShowBlacksmithForm(UserIndex);
				} else {
					WriteConsoleMsg(UserIndex, "Ahí no hay ningún yunque.", FontTypeNames.FONTTYPE_INFO);
				}
			} else {
				WriteConsoleMsg(UserIndex, "Ahí no hay ningún yunque.", FontTypeNames.FONTTYPE_INFO);
			}
			break;
		}
	}

	/* '' */
	/* ' Handles the "CreateNewGuild" message. */
	/* ' */
	/* ' @param userIndex The index of the user sending the message. */

	static void HandleCreateNewGuild(int UserIndex) {
		/* '*************************************************** */
		/* 'Author: Juan Martín Sotuyo Dodero (Maraxus) */
		/* 'Last Modification: 05/11/09 */
		/*
		 * '05/11/09: Pato - Ahora se quitan los espacios del principio y del
		 * fin del nombre del clan
		 */
		/* '*************************************************** */
		if (Declaraciones.UserList[UserIndex].incomingData.length < 9) {
			throw new RuntimeException("Err . Raise UserList ( UserIndex ) . incomingData . NotEnoughDataErrCode");
			return;
		}

		/* FIXME: ON ERROR GOTO ErrHandler */
		/*
		 * 'This packet contains strings, make a copy of the data to prevent
		 * losses if it's not complete yet...
		 */
		clsByteQueue buffer;
		buffer = new clsByteQueue();
		buffer.CopyBuffer(Declaraciones.UserList[UserIndex].incomingData);

		/* 'Remove packet ID */
		buffer.ReadByte();

		String desc;
		String GuildName;
		String site;
		String[] codex;
		String errorStr;

		desc = buffer.ReadASCIIString();
		GuildName = vb6.Trim(buffer.ReadASCIIString());
		site = buffer.ReadASCIIString();
		codex = vb6.Split(buffer.ReadASCIIString(), Protocol.SEPARATOR);

		if (modGuilds.CrearNuevoClan(UserIndex, desc, GuildName, site, codex,
				Declaraciones.UserList[UserIndex].FundandoGuildAlineacion, errorStr)) {
			modSendData.SendData(SendTarget.ToAll, UserIndex,
					PrepareMessageConsoleMsg(
							Declaraciones.UserList[UserIndex].Name + " fundó el clan " + GuildName + " de alineación "
									+ modGuilds.GuildAlignment(Declaraciones.UserList[UserIndex].GuildIndex) + ".",
							FontTypeNames.FONTTYPE_GUILD));
			modSendData.SendData(SendTarget.ToAll, 0,
					PrepareMessagePlayWave(44, Declaraciones.NO_3D_SOUND, Declaraciones.NO_3D_SOUND));

			/* 'Update tag */
			UsUaRiOs.RefreshCharStatus(UserIndex);
		} else {
			WriteConsoleMsg(UserIndex, errorStr, FontTypeNames.FONTTYPE_GUILD);
		}

		/*
		 * 'If we got here then packet is complete, copy data back to original
		 * queue
		 */
		Declaraciones.UserList[UserIndex].incomingData.CopyBuffer(buffer);

		/* FIXME: ErrHandler : */
		int ERROR;
		/* ERROR = Err . Number */
		/* FIXME: ON ERROR GOTO 0 */

		/* 'Destroy auxiliar buffer */
		buffer = null;

		if (ERROR != 0) {
			throw new RuntimeException("Err . Raise ERROR");
		}
	}

	/* '' */
	/* ' Handles the "SpellInfo" message. */
	/* ' */
	/* ' @param userIndex The index of the user sending the message. */

	static void HandleSpellInfo(int UserIndex) {
		/* '*************************************************** */
		/* 'Author: Juan Martín Sotuyo Dodero (Maraxus) */
		/* 'Last Modification: 05/17/06 */
		/* ' */
		/* '*************************************************** */
		if (Declaraciones.UserList[UserIndex].incomingData.length < 2) {
			throw new RuntimeException("Err . Raise UserList ( UserIndex ) . incomingData . NotEnoughDataErrCode");
			return;
		}

		/* 'Remove packet ID */
		Declaraciones.UserList[UserIndex].incomingData.ReadByte();

		int spellSlot;
		int Spell;

		spellSlot = Declaraciones.UserList[UserIndex].incomingData.ReadByte();

		/* 'Validate slot */
		if (spellSlot < 1 || spellSlot > Declaraciones.MAXUSERHECHIZOS) {
			WriteConsoleMsg(UserIndex, "¡Primero selecciona el hechizo!", FontTypeNames.FONTTYPE_INFO);
			return;
		}

		/* 'Validate spell in the slot */
		Spell = Declaraciones.UserList[UserIndex].Stats.UserHechizos[spellSlot];
		if (Spell > 0 && Spell < Declaraciones.NumeroHechizos + 1) {
			/* 'Send information */
			WriteConsoleMsg(UserIndex,
					"%%%%%%%%%%%% INFO DEL HECHIZO %%%%%%%%%%%%" + vbCrLf + "Nombre:"
							+ Declaraciones.Hechizos[Spell].Nombre + vbCrLf + "Descripción:"
							+ Declaraciones.Hechizos[Spell].desc + vbCrLf + "Skill requerido: "
							+ Declaraciones.Hechizos[Spell].MinSkill + " de magia." + vbCrLf + "Maná necesario: "
							+ Declaraciones.Hechizos[Spell].ManaRequerido + vbCrLf + "Energía necesaria: "
							+ Declaraciones.Hechizos[Spell].StaRequerido + vbCrLf + "%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%",
					FontTypeNames.FONTTYPE_INFO);
		}
	}

	/* '' */
	/* ' Handles the "EquipItem" message. */
	/* ' */
	/* ' @param userIndex The index of the user sending the message. */

	static void HandleEquipItem(int UserIndex) {
		/* '*************************************************** */
		/* 'Author: Juan Martín Sotuyo Dodero (Maraxus) */
		/* 'Last Modification: 05/17/06 */
		/* ' */
		/* '*************************************************** */
		if (Declaraciones.UserList[UserIndex].incomingData.length < 2) {
			throw new RuntimeException("Err . Raise UserList ( UserIndex ) . incomingData . NotEnoughDataErrCode");
			return;
		}

		/* 'Remove packet ID */
		Declaraciones.UserList[UserIndex].incomingData.ReadByte();

		int itemSlot;

		itemSlot = Declaraciones.UserList[UserIndex].incomingData.ReadByte();

		/* 'Dead users can't equip items */
		if (Declaraciones.UserList[UserIndex].flags.Muerto == 1) {
			return;
		}

		/* 'Validate item slot */
		if (itemSlot > Declaraciones.UserList[UserIndex].CurrentInventorySlots || itemSlot < 1) {
			return;
		}

		if (Declaraciones.UserList[UserIndex].Invent.Object[itemSlot].ObjIndex == 0) {
			return;
		}

		InvUsuario.EquiparInvItem(UserIndex, itemSlot);
	}

	/* '' */
	/* ' Handles the "ChangeHeading" message. */
	/* ' */
	/* ' @param userIndex The index of the user sending the message. */

	static void HandleChangeHeading(int UserIndex) {
		/* '*************************************************** */
		/* 'Author: Juan Martín Sotuyo Dodero (Maraxus) */
		/* 'Last Modification: 06/28/2008 */
		/* 'Last Modified By: NicoNZ */
		/*
		 * ' 10/01/2008: Tavo - Se cancela la salida del juego si el user esta
		 * saliendo
		 */
		/* ' 06/28/2008: NicoNZ - Sólo se puede cambiar si está inmovilizado. */
		/* '*************************************************** */
		if (Declaraciones.UserList[UserIndex].incomingData.length < 2) {
			throw new RuntimeException("Err . Raise UserList ( UserIndex ) . incomingData . NotEnoughDataErrCode");
			return;
		}

		/* 'Remove packet ID */
		Declaraciones.UserList[UserIndex].incomingData.ReadByte();

		eHeading heading;
		int posX;
		int posY;

		heading = Declaraciones.UserList[UserIndex].incomingData.ReadByte();

		if (Declaraciones.UserList[UserIndex].flags.Paralizado == 1
				&& Declaraciones.UserList[UserIndex].flags.Inmovilizado == 0) {
			switch (heading) {
			case NORTH:
				posY = -1;
				break;

			case EAST:
				posX = 1;
				break;

			case SOUTH:
				posY = 1;
				break;

			case WEST:
				posX = -1;
				break;
			}

			if (Extra.LegalPos(Declaraciones.UserList[UserIndex].Pos.Map,
					Declaraciones.UserList[UserIndex].Pos.X + posX, Declaraciones.UserList[UserIndex].Pos.Y + posY,
					vb6.CBool(Declaraciones.UserList[UserIndex].flags.Navegando),
					!vb6.CBool(Declaraciones.UserList[UserIndex].flags.Navegando))) {
				return;
			}
		}

		/*
		 * 'Validate heading (VB won't say invalid cast if not a valid index
		 * like .Net languages would do... *sigh*)
		 */
		if (heading > 0 && heading < 5) {
			Declaraciones.UserList[UserIndex].Char.heading = heading;
			UsUaRiOs.ChangeUserChar(UserIndex, Declaraciones.UserList[UserIndex].Char.body,
					Declaraciones.UserList[UserIndex].Char.Head, Declaraciones.UserList[UserIndex].Char.heading,
					Declaraciones.UserList[UserIndex].Char.WeaponAnim,
					Declaraciones.UserList[UserIndex].Char.ShieldAnim,
					Declaraciones.UserList[UserIndex].Char.CascoAnim);
		}
	}

	/* '' */
	/* ' Handles the "ModifySkills" message. */
	/* ' */
	/* ' @param userIndex The index of the user sending the message. */

	static void HandleModifySkills(int UserIndex) {
		/* '*************************************************** */
		/* 'Author: Juan Martín Sotuyo Dodero (Maraxus) */
		/* 'Last Modification: 11/19/09 */
		/* '11/19/09: Pato - Adapting to new skills system. */
		/* '*************************************************** */
		if (Declaraciones.UserList[UserIndex].incomingData.length < 1 + Declaraciones.NUMSKILLS) {
			throw new RuntimeException("Err . Raise UserList ( UserIndex ) . incomingData . NotEnoughDataErrCode");
			return;
		}

		/* 'Remove packet ID */
		Declaraciones.UserList[UserIndex].incomingData.ReadByte();

		int i;
		int Count;
		int[] points;

		/* 'Codigo para prevenir el hackeo de los skills */
		/* '<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< */
		for (i = (1); i <= (Declaraciones.NUMSKILLS); i++) {
			points[i] = Declaraciones.UserList[UserIndex].incomingData.ReadByte();

			if (points[i] < 0) {
				General.LogHackAttemp(Declaraciones.UserList[UserIndex].Name + " IP:"
						+ Declaraciones.UserList[UserIndex].ip + " trató de hackear los skills.");
				Declaraciones.UserList[UserIndex].Stats.SkillPts = 0;
				TCP.CloseSocket(UserIndex);
				return;
			}

			Count = Count + points[i];
		}

		if (Count > Declaraciones.UserList[UserIndex].Stats.SkillPts) {
			General.LogHackAttemp(Declaraciones.UserList[UserIndex].Name + " IP:" + Declaraciones.UserList[UserIndex].ip
					+ " trató de hackear los skills.");
			TCP.CloseSocket(UserIndex);
			return;
		}
		/* '<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< */

		Declaraciones.UserList[UserIndex].Counters.AsignedSkills = SistemaCombate.MinimoInt(10,
				Declaraciones.UserList[UserIndex].Counters.AsignedSkills + Count);

		for (i = (1); i <= (Declaraciones.NUMSKILLS); i++) {
			if (points[i] > 0) {
				Declaraciones.UserList[UserIndex].Stats.SkillPts = Declaraciones.UserList[UserIndex].Stats.SkillPts
						- points[i];
				Declaraciones.UserList[UserIndex].Stats.UserSkills[i] = Declaraciones.UserList[UserIndex].Stats.UserSkills[i]
						+ points[i];

				/* 'Client should prevent this, but just in case... */
				if (Declaraciones.UserList[UserIndex].Stats.UserSkills[i] > 100) {
					Declaraciones.UserList[UserIndex].Stats.SkillPts = Declaraciones.UserList[UserIndex].Stats.SkillPts
							+ Declaraciones.UserList[UserIndex].Stats.UserSkills[i] - 100;
					Declaraciones.UserList[UserIndex].Stats.UserSkills[i] = 100;
				}

				UsUaRiOs.CheckEluSkill(UserIndex, i, true);
			}
		}
	}

	/* '' */
	/* ' Handles the "Train" message. */
	/* ' */
	/* ' @param userIndex The index of the user sending the message. */

	static void HandleTrain(int UserIndex) {
		/* '*************************************************** */
		/* 'Author: Juan Martín Sotuyo Dodero (Maraxus) */
		/* 'Last Modification: 05/17/06 */
		/* ' */
		/* '*************************************************** */
		if (Declaraciones.UserList[UserIndex].incomingData.length < 2) {
			throw new RuntimeException("Err . Raise UserList ( UserIndex ) . incomingData . NotEnoughDataErrCode");
			return;
		}

		/* 'Remove packet ID */
		Declaraciones.UserList[UserIndex].incomingData.ReadByte();

		int SpawnedNpc;
		int PetIndex;

		PetIndex = Declaraciones.UserList[UserIndex].incomingData.ReadByte();

		if (Declaraciones.UserList[UserIndex].flags.TargetNPC == 0) {
			return;
		}

		if (Declaraciones.Npclist[Declaraciones.UserList[UserIndex].flags.TargetNPC].NPCtype != eNPCType.Entrenador) {
			return;
		}

		if (Declaraciones.Npclist[Declaraciones.UserList[UserIndex].flags.TargetNPC].Mascotas < Declaraciones.MAXMASCOTASENTRENADOR) {
			if (PetIndex > 0
					&& PetIndex < Declaraciones.Npclist[Declaraciones.UserList[UserIndex].flags.TargetNPC].NroCriaturas
							+ 1) {
				/* 'Create the creature */
				SpawnedNpc = NPCs.SpawnNpc(
						Declaraciones.Npclist[Declaraciones.UserList[UserIndex].flags.TargetNPC].Criaturas[PetIndex].NpcIndex,
						Declaraciones.Npclist[Declaraciones.UserList[UserIndex].flags.TargetNPC].Pos, true, false);

				if (SpawnedNpc > 0) {
					Declaraciones.Npclist[SpawnedNpc].MaestroNpc = Declaraciones.UserList[UserIndex].flags.TargetNPC;
					Declaraciones.Npclist[Declaraciones.UserList[UserIndex].flags.TargetNPC].Mascotas = Declaraciones.Npclist[Declaraciones.UserList[UserIndex].flags.TargetNPC].Mascotas
							+ 1;
				}
			}
		} else {
			modSendData.SendData(SendTarget.ToPCArea, UserIndex,
					PrepareMessageChatOverHead("No puedo traer más criaturas, mata las existentes.",
							Declaraciones.Npclist[Declaraciones.UserList[UserIndex].flags.TargetNPC].Char.CharIndex,
							0x00ffffff));
		}
	}

	/* '' */
	/* ' Handles the "CommerceBuy" message. */
	/* ' */
	/* ' @param userIndex The index of the user sending the message. */

	static void HandleCommerceBuy(int UserIndex) {
		/* '*************************************************** */
		/* 'Author: Juan Martín Sotuyo Dodero (Maraxus) */
		/* 'Last Modification: 05/17/06 */
		/* ' */
		/* '*************************************************** */
		if (Declaraciones.UserList[UserIndex].incomingData.length < 4) {
			throw new RuntimeException("Err . Raise UserList ( UserIndex ) . incomingData . NotEnoughDataErrCode");
			return;
		}

		/* 'Remove packet ID */
		Declaraciones.UserList[UserIndex].incomingData.ReadByte();

		int Slot;
		int Amount;

		Slot = Declaraciones.UserList[UserIndex].incomingData.ReadByte();
		Amount = Declaraciones.UserList[UserIndex].incomingData.ReadInteger();

		/* 'Dead people can't commerce... */
		if (Declaraciones.UserList[UserIndex].flags.Muerto == 1) {
			WriteConsoleMsg(UserIndex, "¡¡Estás muerto!!", FontTypeNames.FONTTYPE_INFO);
			return;
		}

		/* '¿El target es un NPC valido? */
		if (Declaraciones.UserList[UserIndex].flags.TargetNPC < 1) {
			return;
		}

		/* '¿El NPC puede comerciar? */
		if (Declaraciones.Npclist[Declaraciones.UserList[UserIndex].flags.TargetNPC].Comercia == 0) {
			modSendData.SendData(SendTarget.ToPCArea, UserIndex,
					PrepareMessageChatOverHead("No tengo ningún interés en comerciar.",
							Declaraciones.Npclist[Declaraciones.UserList[UserIndex].flags.TargetNPC].Char.CharIndex,
							0x00ffffff));
			return;
		}

		/* 'Only if in commerce mode.... */
		if (!Declaraciones.UserList[UserIndex].flags.Comerciando) {
			WriteConsoleMsg(UserIndex, "No estás comerciando.", FontTypeNames.FONTTYPE_INFO);
			return;
		}

		/* 'User compra el item */
		modSistemaComercio.Comercio(eModoComercio.Compra, UserIndex, Declaraciones.UserList[UserIndex].flags.TargetNPC,
				Slot, Amount);
	}

	/* '' */
	/* ' Handles the "BankExtractItem" message. */
	/* ' */
	/* ' @param userIndex The index of the user sending the message. */

	static void HandleBankExtractItem(int UserIndex) {
		/* '*************************************************** */
		/* 'Author: Juan Martín Sotuyo Dodero (Maraxus) */
		/* 'Last Modification: 05/17/06 */
		/* ' */
		/* '*************************************************** */
		if (Declaraciones.UserList[UserIndex].incomingData.length < 4) {
			throw new RuntimeException("Err . Raise UserList ( UserIndex ) . incomingData . NotEnoughDataErrCode");
			return;
		}

		/* 'Remove packet ID */
		Declaraciones.UserList[UserIndex].incomingData.ReadByte();

		int Slot;
		int Amount;

		Slot = Declaraciones.UserList[UserIndex].incomingData.ReadByte();
		Amount = Declaraciones.UserList[UserIndex].incomingData.ReadInteger();

		/* 'Dead people can't commerce */
		if (Declaraciones.UserList[UserIndex].flags.Muerto == 1) {
			WriteConsoleMsg(UserIndex, "¡¡Estás muerto!!", FontTypeNames.FONTTYPE_INFO);
			return;
		}

		/* '¿El target es un NPC valido? */
		if (Declaraciones.UserList[UserIndex].flags.TargetNPC < 1) {
			return;
		}

		/* '¿Es el banquero? */
		if (Declaraciones.Npclist[Declaraciones.UserList[UserIndex].flags.TargetNPC].NPCtype != eNPCType.Banquero) {
			return;
		}

		/* 'User retira el item del slot */
		modBanco.UserRetiraItem(UserIndex, Slot, Amount);
	}

	/* '' */
	/* ' Handles the "CommerceSell" message. */
	/* ' */
	/* ' @param userIndex The index of the user sending the message. */

	static void HandleCommerceSell(int UserIndex) {
		/* '*************************************************** */
		/* 'Author: Juan Martín Sotuyo Dodero (Maraxus) */
		/* 'Last Modification: 05/17/06 */
		/* ' */
		/* '*************************************************** */
		if (Declaraciones.UserList[UserIndex].incomingData.length < 4) {
			throw new RuntimeException("Err . Raise UserList ( UserIndex ) . incomingData . NotEnoughDataErrCode");
			return;
		}

		/* 'Remove packet ID */
		Declaraciones.UserList[UserIndex].incomingData.ReadByte();

		int Slot;
		int Amount;

		Slot = Declaraciones.UserList[UserIndex].incomingData.ReadByte();
		Amount = Declaraciones.UserList[UserIndex].incomingData.ReadInteger();

		/* 'Dead people can't commerce... */
		if (Declaraciones.UserList[UserIndex].flags.Muerto == 1) {
			WriteConsoleMsg(UserIndex, "¡¡Estás muerto!!", FontTypeNames.FONTTYPE_INFO);
			return;
		}

		/* '¿El target es un NPC valido? */
		if (Declaraciones.UserList[UserIndex].flags.TargetNPC < 1) {
			return;
		}

		/* '¿El NPC puede comerciar? */
		if (Declaraciones.Npclist[Declaraciones.UserList[UserIndex].flags.TargetNPC].Comercia == 0) {
			modSendData.SendData(SendTarget.ToPCArea, UserIndex,
					PrepareMessageChatOverHead("No tengo ningún interés en comerciar.",
							Declaraciones.Npclist[Declaraciones.UserList[UserIndex].flags.TargetNPC].Char.CharIndex,
							0x00ffffff));
			return;
		}

		/* 'User compra el item del slot */
		modSistemaComercio.Comercio(eModoComercio.Venta, UserIndex, Declaraciones.UserList[UserIndex].flags.TargetNPC,
				Slot, Amount);
	}

	/* '' */
	/* ' Handles the "BankDeposit" message. */
	/* ' */
	/* ' @param userIndex The index of the user sending the message. */

	static void HandleBankDeposit(int UserIndex) {
		/* '*************************************************** */
		/* 'Author: Juan Martín Sotuyo Dodero (Maraxus) */
		/* 'Last Modification: 05/17/06 */
		/* ' */
		/* '*************************************************** */
		if (Declaraciones.UserList[UserIndex].incomingData.length < 4) {
			throw new RuntimeException("Err . Raise UserList ( UserIndex ) . incomingData . NotEnoughDataErrCode");
			return;
		}

		/* 'Remove packet ID */
		Declaraciones.UserList[UserIndex].incomingData.ReadByte();

		int Slot;
		int Amount;

		Slot = Declaraciones.UserList[UserIndex].incomingData.ReadByte();
		Amount = Declaraciones.UserList[UserIndex].incomingData.ReadInteger();

		/* 'Dead people can't commerce... */
		if (Declaraciones.UserList[UserIndex].flags.Muerto == 1) {
			WriteConsoleMsg(UserIndex, "¡¡Estás muerto!!", FontTypeNames.FONTTYPE_INFO);
			return;
		}

		/* '¿El target es un NPC valido? */
		if (Declaraciones.UserList[UserIndex].flags.TargetNPC < 1) {
			return;
		}

		/* '¿El NPC puede comerciar? */
		if (Declaraciones.Npclist[Declaraciones.UserList[UserIndex].flags.TargetNPC].NPCtype != eNPCType.Banquero) {
			return;
		}

		/* 'User deposita el item del slot rdata */
		modBanco.UserDepositaItem(UserIndex, Slot, Amount);
	}

	/* '' */
	/* ' Handles the "ForumPost" message. */
	/* ' */
	/* ' @param userIndex The index of the user sending the message. */

	static void HandleForumPost(int UserIndex) {
		/* '*************************************************** */
		/* 'Author: Juan Martín Sotuyo Dodero (Maraxus) */
		/* 'Last Modification: 02/01/2010 */
		/* '02/01/2010: ZaMa - Implemento nuevo sistema de foros */
		/* '*************************************************** */
		if (Declaraciones.UserList[UserIndex].incomingData.length < 6) {
			throw new RuntimeException("Err . Raise UserList ( UserIndex ) . incomingData . NotEnoughDataErrCode");
			return;
		}

		/* FIXME: ON ERROR GOTO ErrHandler */
		/*
		 * 'This packet contains strings, make a copy of the data to prevent
		 * losses if it's not complete yet...
		 */
		clsByteQueue buffer;
		buffer = new clsByteQueue();
		buffer.CopyBuffer(Declaraciones.UserList[UserIndex].incomingData);

		/* 'Remove packet ID */
		buffer.ReadByte();

		eForumMsgType ForumMsgType;

		String File;
		String Title;
		String Post;
		int ForumIndex;
		String postFile;
		int ForumType;

		ForumMsgType = buffer.ReadByte();

		Title = buffer.ReadASCIIString();
		Post = buffer.ReadASCIIString();

		if (Declaraciones.UserList[UserIndex].flags.TargetObj > 0) {
			ForumType = modForum.ForumAlignment(ForumMsgType);

			switch (ForumType) {

			case ieGeneral:
				ForumIndex = modForum
						.GetForumIndex(Declaraciones.ObjData[Declaraciones.UserList[UserIndex].flags.TargetObj].ForoID);

				break;

			case ieREAL:
				ForumIndex = modForum.GetForumIndex(modForum.FORO_REAL_ID);

				break;

			case ieCAOS:
				ForumIndex = modForum.GetForumIndex(modForum.FORO_CAOS_ID);

				break;
			}

			modForum.AddPost(ForumIndex, Post, Declaraciones.UserList[UserIndex].Name, Title,
					modForum.EsAnuncio(ForumMsgType));
		}

		/*
		 * 'If we got here then packet is complete, copy data back to original
		 * queue
		 */
		Declaraciones.UserList[UserIndex].incomingData.CopyBuffer(buffer);

		/* FIXME: ErrHandler : */
		int ERROR;
		/* ERROR = Err . Number */
		/* FIXME: ON ERROR GOTO 0 */

		/* 'Destroy auxiliar buffer */
		buffer = null;

		if (ERROR != 0) {
			throw new RuntimeException("Err . Raise ERROR");
		}
	}

	/* '' */
	/* ' Handles the "MoveSpell" message. */
	/* ' */
	/* ' @param userIndex The index of the user sending the message. */

	static void HandleMoveSpell(int UserIndex) {
		/* '*************************************************** */
		/* 'Author: Juan Martín Sotuyo Dodero (Maraxus) */
		/* 'Last Modification: 05/17/06 */
		/* ' */
		/* '*************************************************** */
		if (Declaraciones.UserList[UserIndex].incomingData.length < 3) {
			throw new RuntimeException("Err . Raise UserList ( UserIndex ) . incomingData . NotEnoughDataErrCode");
			return;
		}

		/* 'Remove packet ID */
		Declaraciones.UserList[UserIndex].incomingData.ReadByte();

		int dir;

		if (Declaraciones.UserList[UserIndex].incomingData.ReadBoolean()) {
			dir = 1;
		} else {
			dir = -1;
		}

		modHechizos.DesplazarHechizo(UserIndex, dir, Declaraciones.UserList[UserIndex].incomingData.ReadByte());
	}

	/* '' */
	/* ' Handles the "MoveBank" message. */
	/* ' */
	/* ' @param userIndex The index of the user sending the message. */

	static void HandleMoveBank(int UserIndex) {
		/* '*************************************************** */
		/* 'Author: Torres Patricio (Pato) */
		/* 'Last Modification: 06/14/09 */
		/* ' */
		/* '*************************************************** */
		if (Declaraciones.UserList[UserIndex].incomingData.length < 3) {
			throw new RuntimeException("Err . Raise UserList ( UserIndex ) . incomingData . NotEnoughDataErrCode");
			return;
		}

		/* 'Remove packet ID */
		Declaraciones.UserList[UserIndex].incomingData.ReadByte();

		int dir;
		int Slot;
		Declaraciones.Obj TempItem;

		if (Declaraciones.UserList[UserIndex].incomingData.ReadBoolean()) {
			dir = 1;
		} else {
			dir = -1;
		}

		Slot = Declaraciones.UserList[UserIndex].incomingData.ReadByte();

		TempItem.ObjIndex = Declaraciones.UserList[UserIndex].BancoInvent.Object[Slot].ObjIndex;
		TempItem.Amount = Declaraciones.UserList[UserIndex].BancoInvent.Object[Slot].Amount;

		/* 'Mover arriba */
		if (dir == 1) {
			Declaraciones.UserList[UserIndex].BancoInvent.Object[Slot] = Declaraciones.UserList[UserIndex].BancoInvent.Object[Slot
					- 1];
			Declaraciones.UserList[UserIndex].BancoInvent.Object[Slot - 1].ObjIndex = TempItem.ObjIndex;
			Declaraciones.UserList[UserIndex].BancoInvent.Object[Slot - 1].Amount = TempItem.Amount;
			/* 'mover abajo */
		} else {
			Declaraciones.UserList[UserIndex].BancoInvent.Object[Slot] = Declaraciones.UserList[UserIndex].BancoInvent.Object[Slot
					+ 1];
			Declaraciones.UserList[UserIndex].BancoInvent.Object[Slot + 1].ObjIndex = TempItem.ObjIndex;
			Declaraciones.UserList[UserIndex].BancoInvent.Object[Slot + 1].Amount = TempItem.Amount;
		}

		modBanco.UpdateBanUserInv(true, UserIndex, 0);
		modBanco.UpdateVentanaBanco(UserIndex);

	}

	/* '' */
	/* ' Handles the "ClanCodexUpdate" message. */
	/* ' */
	/* ' @param userIndex The index of the user sending the message. */

	static void HandleClanCodexUpdate(int UserIndex) {
		/* '*************************************************** */
		/* 'Author: Juan Martín Sotuyo Dodero (Maraxus) */
		/* 'Last Modification: 05/17/06 */
		/* ' */
		/* '*************************************************** */
		if (Declaraciones.UserList[UserIndex].incomingData.length < 5) {
			throw new RuntimeException("Err . Raise UserList ( UserIndex ) . incomingData . NotEnoughDataErrCode");
			return;
		}

		/* FIXME: ON ERROR GOTO ErrHandler */
		/*
		 * 'This packet contains strings, make a copy of the data to prevent
		 * losses if it's not complete yet...
		 */
		clsByteQueue buffer;
		buffer = new clsByteQueue();
		buffer.CopyBuffer(Declaraciones.UserList[UserIndex].incomingData);

		/* 'Remove packet ID */
		buffer.ReadByte();

		String desc;
		String[] codex;

		desc = buffer.ReadASCIIString();
		codex = vb6.Split(buffer.ReadASCIIString(), Protocol.SEPARATOR);

		modGuilds.ChangeCodexAndDesc(desc, codex, Declaraciones.UserList[UserIndex].GuildIndex);

		/*
		 * 'If we got here then packet is complete, copy data back to original
		 * queue
		 */
		Declaraciones.UserList[UserIndex].incomingData.CopyBuffer(buffer);

		/* FIXME: ErrHandler : */
		int ERROR;
		/* ERROR = Err . Number */
		/* FIXME: ON ERROR GOTO 0 */

		/* 'Destroy auxiliar buffer */
		buffer = null;

		if (ERROR != 0) {
			throw new RuntimeException("Err . Raise ERROR");
		}
	}

	/* '' */
	/* ' Handles the "UserCommerceOffer" message. */
	/* ' */
	/* ' @param userIndex The index of the user sending the message. */

	static void HandleUserCommerceOffer(int UserIndex) {
		/* '*************************************************** */
		/* 'Author: Juan Martín Sotuyo Dodero (Maraxus) */
		/* 'Last Modification: 24/11/2009 */
		/* '24/11/2009: ZaMa - Nuevo sistema de comercio */
		/* '*************************************************** */
		if (Declaraciones.UserList[UserIndex].incomingData.length < 7) {
			throw new RuntimeException("Err . Raise UserList ( UserIndex ) . incomingData . NotEnoughDataErrCode");
			return;
		}

		/* FIXME: ON ERROR GOTO ErrHandler */

		/* 'Remove packet ID */
		Declaraciones.UserList[UserIndex].incomingData.ReadByte();

		int Amount;
		int Slot;
		int tUser;
		int OfferSlot;
		int ObjIndex;

		Slot = Declaraciones.UserList[UserIndex].incomingData.ReadByte();
		Amount = Declaraciones.UserList[UserIndex].incomingData.ReadLong();
		OfferSlot = Declaraciones.UserList[UserIndex].incomingData.ReadByte();

		if (!mdlCOmercioConUsuario.PuedeSeguirComerciando(UserIndex)) {
			return;
		}

		/* 'Get the other player */
		tUser = Declaraciones.UserList[UserIndex].ComUsu.DestUsu;

		/*
		 * ' If he's already confirmed his offer, but now tries to change it,
		 * then he's cheating
		 */
		if (Declaraciones.UserList[UserIndex].ComUsu.Confirmo == true) {

			/* ' Finish the trade */
			mdlCOmercioConUsuario.FinComerciarUsu(UserIndex);
			mdlCOmercioConUsuario.FinComerciarUsu(tUser);
			Protocol.FlushBuffer(tUser);

			return;
		}

		/*
		 * 'If slot is invalid and it's not gold or it's not 0 (Substracting),
		 * then ignore it.
		 */
		if (((Slot < 0 || Slot > Declaraciones.UserList[UserIndex].CurrentInventorySlots)
				&& Slot != Declaraciones.FLAGORO)) {
			return;
		}

		/* 'If OfferSlot is invalid, then ignore it. */
		if (OfferSlot < 1 || OfferSlot > mdlCOmercioConUsuario.MAX_OFFER_SLOTS + 1) {
			return;
		}

		/* ' Can be negative if substracted from the offer, but never 0. */
		if (Amount == 0) {
			return;
		}

		/* 'Has he got enough?? */
		if (Slot == Declaraciones.FLAGORO) {
			/* ' Can't offer more than he has */
			if (Amount > Declaraciones.UserList[UserIndex].Stats.GLD
					- Declaraciones.UserList[UserIndex].ComUsu.GoldAmount) {
				WriteCommerceChat(UserIndex, "No tienes esa cantidad de oro para agregar a la oferta.",
						FontTypeNames.FONTTYPE_TALK);
				return;
			}

			if (Amount < 0) {
				if (vb6.Abs(Amount) > Declaraciones.UserList[UserIndex].ComUsu.GoldAmount) {
					Amount = Declaraciones.UserList[UserIndex].ComUsu.GoldAmount * (-1);
				}
			}
		} else {
			/*
			 * 'If modifing a filled offerSlot, we already got the objIndex,
			 * then we don't need to know it
			 */
			if (Slot != 0) {
				ObjIndex = Declaraciones.UserList[UserIndex].Invent.Object[Slot].ObjIndex;
			}

			/* ' Non-Transferible or commerciable? */
			if (ObjIndex != 0) {
				if ((Declaraciones.ObjData[ObjIndex].Intransferible == 1
						|| Declaraciones.ObjData[ObjIndex].NoComerciable == 1)) {
					WriteCommerceChat(UserIndex, "No puedes comerciar este ítem.", FontTypeNames.FONTTYPE_TALK);
					return;
				}
			}

			/* ' Can't offer more than he has */
			if (!UsUaRiOs.HasEnoughItems(UserIndex, ObjIndex, UsUaRiOs.TotalOfferItems(ObjIndex, UserIndex) + Amount)) {

				WriteCommerceChat(UserIndex, "No tienes esa cantidad.", FontTypeNames.FONTTYPE_TALK);
				return;
			}

			if (Amount < 0) {
				if (vb6.Abs(Amount) > Declaraciones.UserList[UserIndex].ComUsu.cant[OfferSlot]) {
					Amount = Declaraciones.UserList[UserIndex].ComUsu.cant[OfferSlot] * (-1);
				}
			}

			if (InvUsuario.ItemNewbie(ObjIndex)) {
				WriteCancelOfferItem(UserIndex, OfferSlot);
				return;
			}

			/*
			 * 'Don't allow to sell boats if they are equipped (you can't take
			 * them off in the water and causes trouble)
			 */
			if (Declaraciones.UserList[UserIndex].flags.Navegando == 1) {
				if (Declaraciones.UserList[UserIndex].Invent.BarcoSlot == Slot) {
					WriteCommerceChat(UserIndex, "No puedes vender tu barco mientras lo estés usando.",
							FontTypeNames.FONTTYPE_TALK);
					return;
				}
			}

			if (Declaraciones.UserList[UserIndex].Invent.MochilaEqpSlot > 0) {
				if (Declaraciones.UserList[UserIndex].Invent.MochilaEqpSlot == Slot) {
					WriteCommerceChat(UserIndex, "No puedes vender tu mochila mientras la estés usando.",
							FontTypeNames.FONTTYPE_TALK);
					return;
				}
			}
		}

		mdlCOmercioConUsuario.AgregarOferta(UserIndex, OfferSlot, ObjIndex, Amount, Slot == Declaraciones.FLAGORO);
		mdlCOmercioConUsuario.EnviarOferta(tUser, OfferSlot);

		return;

		/* FIXME: ErrHandler : */
		General.LogError("Error en HandleUserCommerceOffer. Error: " + Err.description + ". User: "
				+ Declaraciones.UserList[UserIndex].Name + "(" + UserIndex + ")" + ". tUser: " + tUser + ". Slot: "
				+ Slot + ". Amount: " + Amount + ". OfferSlot: " + OfferSlot);
	}

	/* '' */
	/* ' Handles the "GuildAcceptPeace" message. */
	/* ' */
	/* ' @param userIndex The index of the user sending the message. */

	static void HandleGuildAcceptPeace(int UserIndex) {
		/* '*************************************************** */
		/* 'Author: Juan Martín Sotuyo Dodero (Maraxus) */
		/* 'Last Modification: 05/17/06 */
		/* ' */
		/* '*************************************************** */
		if (Declaraciones.UserList[UserIndex].incomingData.length < 3) {
			throw new RuntimeException("Err . Raise UserList ( UserIndex ) . incomingData . NotEnoughDataErrCode");
			return;
		}

		/* FIXME: ON ERROR GOTO ErrHandler */
		/*
		 * 'This packet contains strings, make a copy of the data to prevent
		 * losses if it's not complete yet...
		 */
		clsByteQueue buffer;
		buffer = new clsByteQueue();
		buffer.CopyBuffer(Declaraciones.UserList[UserIndex].incomingData);

		/* 'Remove packet ID */
		buffer.ReadByte();

		String guild;
		String errorStr;
		String otherClanIndex;

		guild = buffer.ReadASCIIString();

		otherClanIndex = modGuilds.r_AceptarPropuestaDePaz(UserIndex, guild, errorStr);

		if (otherClanIndex == 0) {
			WriteConsoleMsg(UserIndex, errorStr, FontTypeNames.FONTTYPE_GUILD);
		} else {
			modSendData.SendData(SendTarget.ToGuildMembers, Declaraciones.UserList[UserIndex].GuildIndex,
					PrepareMessageConsoleMsg("Tu clan ha firmado la paz con " + guild + ".",
							FontTypeNames.FONTTYPE_GUILD));
			modSendData.SendData(SendTarget.ToGuildMembers, otherClanIndex,
					PrepareMessageConsoleMsg(
							"Tu clan ha firmado la paz con "
									+ modGuilds.GuildName(Declaraciones.UserList[UserIndex].GuildIndex) + ".",
							FontTypeNames.FONTTYPE_GUILD));
		}

		/*
		 * 'If we got here then packet is complete, copy data back to original
		 * queue
		 */
		Declaraciones.UserList[UserIndex].incomingData.CopyBuffer(buffer);

		/* FIXME: ErrHandler : */
		int ERROR;
		/* ERROR = Err . Number */
		/* FIXME: ON ERROR GOTO 0 */

		/* 'Destroy auxiliar buffer */
		buffer = null;

		if (ERROR != 0) {
			throw new RuntimeException("Err . Raise ERROR");
		}
	}

	/* '' */
	/* ' Handles the "GuildRejectAlliance" message. */
	/* ' */
	/* ' @param userIndex The index of the user sending the message. */

	static void HandleGuildRejectAlliance(int UserIndex) {
		/* '*************************************************** */
		/* 'Author: Juan Martín Sotuyo Dodero (Maraxus) */
		/* 'Last Modification: 05/17/06 */
		/* ' */
		/* '*************************************************** */
		if (Declaraciones.UserList[UserIndex].incomingData.length < 3) {
			throw new RuntimeException("Err . Raise UserList ( UserIndex ) . incomingData . NotEnoughDataErrCode");
			return;
		}

		/* FIXME: ON ERROR GOTO ErrHandler */
		/*
		 * 'This packet contains strings, make a copy of the data to prevent
		 * losses if it's not complete yet...
		 */
		clsByteQueue buffer;
		buffer = new clsByteQueue();
		buffer.CopyBuffer(Declaraciones.UserList[UserIndex].incomingData);

		/* 'Remove packet ID */
		buffer.ReadByte();

		String guild;
		String errorStr;
		String otherClanIndex;

		guild = buffer.ReadASCIIString();

		otherClanIndex = modGuilds.r_RechazarPropuestaDeAlianza(UserIndex, guild, errorStr);

		if (otherClanIndex == 0) {
			WriteConsoleMsg(UserIndex, errorStr, FontTypeNames.FONTTYPE_GUILD);
		} else {
			modSendData.SendData(SendTarget.ToGuildMembers, Declaraciones.UserList[UserIndex].GuildIndex,
					PrepareMessageConsoleMsg("Tu clan rechazado la propuesta de alianza de " + guild,
							FontTypeNames.FONTTYPE_GUILD));
			modSendData.SendData(SendTarget.ToGuildMembers, otherClanIndex,
					PrepareMessageConsoleMsg(
							modGuilds.GuildName(Declaraciones.UserList[UserIndex].GuildIndex)
									+ " ha rechazado nuestra propuesta de alianza con su clan.",
							FontTypeNames.FONTTYPE_GUILD));
		}

		/*
		 * 'If we got here then packet is complete, copy data back to original
		 * queue
		 */
		Declaraciones.UserList[UserIndex].incomingData.CopyBuffer(buffer);

		/* FIXME: ErrHandler : */
		int ERROR;
		/* ERROR = Err . Number */
		/* FIXME: ON ERROR GOTO 0 */

		/* 'Destroy auxiliar buffer */
		buffer = null;

		if (ERROR != 0) {
			throw new RuntimeException("Err . Raise ERROR");
		}
	}

	/* '' */
	/* ' Handles the "GuildRejectPeace" message. */
	/* ' */
	/* ' @param userIndex The index of the user sending the message. */

	static void HandleGuildRejectPeace(int UserIndex) {
		/* '*************************************************** */
		/* 'Author: Juan Martín Sotuyo Dodero (Maraxus) */
		/* 'Last Modification: 05/17/06 */
		/* ' */
		/* '*************************************************** */
		if (Declaraciones.UserList[UserIndex].incomingData.length < 3) {
			throw new RuntimeException("Err . Raise UserList ( UserIndex ) . incomingData . NotEnoughDataErrCode");
			return;
		}

		/* FIXME: ON ERROR GOTO ErrHandler */
		/*
		 * 'This packet contains strings, make a copy of the data to prevent
		 * losses if it's not complete yet...
		 */
		clsByteQueue buffer;
		buffer = new clsByteQueue();
		buffer.CopyBuffer(Declaraciones.UserList[UserIndex].incomingData);

		/* 'Remove packet ID */
		buffer.ReadByte();

		String guild;
		String errorStr;
		String otherClanIndex;

		guild = buffer.ReadASCIIString();

		otherClanIndex = modGuilds.r_RechazarPropuestaDePaz(UserIndex, guild, errorStr);

		if (otherClanIndex == 0) {
			WriteConsoleMsg(UserIndex, errorStr, FontTypeNames.FONTTYPE_GUILD);
		} else {
			modSendData.SendData(SendTarget.ToGuildMembers, Declaraciones.UserList[UserIndex].GuildIndex,
					PrepareMessageConsoleMsg("Tu clan rechazado la propuesta de paz de " + guild + ".",
							FontTypeNames.FONTTYPE_GUILD));
			modSendData.SendData(SendTarget.ToGuildMembers, otherClanIndex,
					PrepareMessageConsoleMsg(
							modGuilds.GuildName(Declaraciones.UserList[UserIndex].GuildIndex)
									+ " ha rechazado nuestra propuesta de paz con su clan.",
							FontTypeNames.FONTTYPE_GUILD));
		}

		/*
		 * 'If we got here then packet is complete, copy data back to original
		 * queue
		 */
		Declaraciones.UserList[UserIndex].incomingData.CopyBuffer(buffer);

		/* FIXME: ErrHandler : */
		int ERROR;
		/* ERROR = Err . Number */
		/* FIXME: ON ERROR GOTO 0 */

		/* 'Destroy auxiliar buffer */
		buffer = null;

		if (ERROR != 0) {
			throw new RuntimeException("Err . Raise ERROR");
		}
	}

	/* '' */
	/* ' Handles the "GuildAcceptAlliance" message. */
	/* ' */
	/* ' @param userIndex The index of the user sending the message. */

	static void HandleGuildAcceptAlliance(int UserIndex) {
		/* '*************************************************** */
		/* 'Author: Juan Martín Sotuyo Dodero (Maraxus) */
		/* 'Last Modification: 05/17/06 */
		/* ' */
		/* '*************************************************** */
		if (Declaraciones.UserList[UserIndex].incomingData.length < 3) {
			throw new RuntimeException("Err . Raise UserList ( UserIndex ) . incomingData . NotEnoughDataErrCode");
			return;
		}

		/* FIXME: ON ERROR GOTO ErrHandler */
		/*
		 * 'This packet contains strings, make a copy of the data to prevent
		 * losses if it's not complete yet...
		 */
		clsByteQueue buffer;
		buffer = new clsByteQueue();
		buffer.CopyBuffer(Declaraciones.UserList[UserIndex].incomingData);

		/* 'Remove packet ID */
		buffer.ReadByte();

		String guild;
		String errorStr;
		String otherClanIndex;

		guild = buffer.ReadASCIIString();

		otherClanIndex = modGuilds.r_AceptarPropuestaDeAlianza(UserIndex, guild, errorStr);

		if (otherClanIndex == 0) {
			WriteConsoleMsg(UserIndex, errorStr, FontTypeNames.FONTTYPE_GUILD);
		} else {
			modSendData.SendData(SendTarget.ToGuildMembers, Declaraciones.UserList[UserIndex].GuildIndex,
					PrepareMessageConsoleMsg("Tu clan ha firmado la alianza con " + guild + ".",
							FontTypeNames.FONTTYPE_GUILD));
			modSendData.SendData(SendTarget.ToGuildMembers, otherClanIndex,
					PrepareMessageConsoleMsg(
							"Tu clan ha firmado la paz con "
									+ modGuilds.GuildName(Declaraciones.UserList[UserIndex].GuildIndex) + ".",
							FontTypeNames.FONTTYPE_GUILD));
		}

		/*
		 * 'If we got here then packet is complete, copy data back to original
		 * queue
		 */
		Declaraciones.UserList[UserIndex].incomingData.CopyBuffer(buffer);

		/* FIXME: ErrHandler : */
		int ERROR;
		/* ERROR = Err . Number */
		/* FIXME: ON ERROR GOTO 0 */

		/* 'Destroy auxiliar buffer */
		buffer = null;

		if (ERROR != 0) {
			throw new RuntimeException("Err . Raise ERROR");
		}
	}

	/* '' */
	/* ' Handles the "GuildOfferPeace" message. */
	/* ' */
	/* ' @param userIndex The index of the user sending the message. */

	static void HandleGuildOfferPeace(int UserIndex) {
		/* '*************************************************** */
		/* 'Author: Juan Martín Sotuyo Dodero (Maraxus) */
		/* 'Last Modification: 05/17/06 */
		/* ' */
		/* '*************************************************** */
		if (Declaraciones.UserList[UserIndex].incomingData.length < 5) {
			throw new RuntimeException("Err . Raise UserList ( UserIndex ) . incomingData . NotEnoughDataErrCode");
			return;
		}

		/* FIXME: ON ERROR GOTO ErrHandler */
		/*
		 * 'This packet contains strings, make a copy of the data to prevent
		 * losses if it's not complete yet...
		 */
		clsByteQueue buffer;
		buffer = new clsByteQueue();
		buffer.CopyBuffer(Declaraciones.UserList[UserIndex].incomingData);

		/* 'Remove packet ID */
		buffer.ReadByte();

		String guild;
		String proposal;
		String errorStr;

		guild = buffer.ReadASCIIString();
		proposal = buffer.ReadASCIIString();

		if (modGuilds.r_ClanGeneraPropuesta(UserIndex, guild, RELACIONES_GUILD.PAZ, proposal, errorStr)) {
			WriteConsoleMsg(UserIndex, "Propuesta de paz enviada.", FontTypeNames.FONTTYPE_GUILD);
		} else {
			WriteConsoleMsg(UserIndex, errorStr, FontTypeNames.FONTTYPE_GUILD);
		}

		/*
		 * 'If we got here then packet is complete, copy data back to original
		 * queue
		 */
		Declaraciones.UserList[UserIndex].incomingData.CopyBuffer(buffer);

		/* FIXME: ErrHandler : */
		int ERROR;
		/* ERROR = Err . Number */
		/* FIXME: ON ERROR GOTO 0 */

		/* 'Destroy auxiliar buffer */
		buffer = null;

		if (ERROR != 0) {
			throw new RuntimeException("Err . Raise ERROR");
		}
	}

	/* '' */
	/* ' Handles the "GuildOfferAlliance" message. */
	/* ' */
	/* ' @param userIndex The index of the user sending the message. */

	static void HandleGuildOfferAlliance(int UserIndex) {
		/* '*************************************************** */
		/* 'Author: Juan Martín Sotuyo Dodero (Maraxus) */
		/* 'Last Modification: 05/17/06 */
		/* ' */
		/* '*************************************************** */
		if (Declaraciones.UserList[UserIndex].incomingData.length < 5) {
			throw new RuntimeException("Err . Raise UserList ( UserIndex ) . incomingData . NotEnoughDataErrCode");
			return;
		}

		/* FIXME: ON ERROR GOTO ErrHandler */
		/*
		 * 'This packet contains strings, make a copy of the data to prevent
		 * losses if it's not complete yet...
		 */
		clsByteQueue buffer;
		buffer = new clsByteQueue();
		buffer.CopyBuffer(Declaraciones.UserList[UserIndex].incomingData);

		/* 'Remove packet ID */
		buffer.ReadByte();

		String guild;
		String proposal;
		String errorStr;

		guild = buffer.ReadASCIIString();
		proposal = buffer.ReadASCIIString();

		if (modGuilds.r_ClanGeneraPropuesta(UserIndex, guild, RELACIONES_GUILD.ALIADOS, proposal, errorStr)) {
			WriteConsoleMsg(UserIndex, "Propuesta de alianza enviada.", FontTypeNames.FONTTYPE_GUILD);
		} else {
			WriteConsoleMsg(UserIndex, errorStr, FontTypeNames.FONTTYPE_GUILD);
		}

		/*
		 * 'If we got here then packet is complete, copy data back to original
		 * queue
		 */
		Declaraciones.UserList[UserIndex].incomingData.CopyBuffer(buffer);

		/* FIXME: ErrHandler : */
		int ERROR;
		/* ERROR = Err . Number */
		/* FIXME: ON ERROR GOTO 0 */

		/* 'Destroy auxiliar buffer */
		buffer = null;

		if (ERROR != 0) {
			throw new RuntimeException("Err . Raise ERROR");
		}
	}

	/* '' */
	/* ' Handles the "GuildAllianceDetails" message. */
	/* ' */
	/* ' @param userIndex The index of the user sending the message. */

	static void HandleGuildAllianceDetails(int UserIndex) {
		/* '*************************************************** */
		/* 'Author: Juan Martín Sotuyo Dodero (Maraxus) */
		/* 'Last Modification: 05/17/06 */
		/* ' */
		/* '*************************************************** */
		if (Declaraciones.UserList[UserIndex].incomingData.length < 3) {
			throw new RuntimeException("Err . Raise UserList ( UserIndex ) . incomingData . NotEnoughDataErrCode");
			return;
		}

		/* FIXME: ON ERROR GOTO ErrHandler */
		/*
		 * 'This packet contains strings, make a copy of the data to prevent
		 * losses if it's not complete yet...
		 */
		clsByteQueue buffer;
		buffer = new clsByteQueue();
		buffer.CopyBuffer(Declaraciones.UserList[UserIndex].incomingData);

		/* 'Remove packet ID */
		buffer.ReadByte();

		String guild;
		String errorStr;
		String details;

		guild = buffer.ReadASCIIString();

		details = modGuilds.r_VerPropuesta(UserIndex, guild, RELACIONES_GUILD.ALIADOS, errorStr);

		if (vb6.LenB(details) == 0) {
			WriteConsoleMsg(UserIndex, errorStr, FontTypeNames.FONTTYPE_GUILD);
		} else {
			WriteOfferDetails(UserIndex, details);
		}

		/*
		 * 'If we got here then packet is complete, copy data back to original
		 * queue
		 */
		Declaraciones.UserList[UserIndex].incomingData.CopyBuffer(buffer);

		/* FIXME: ErrHandler : */
		int ERROR;
		/* ERROR = Err . Number */
		/* FIXME: ON ERROR GOTO 0 */

		/* 'Destroy auxiliar buffer */
		buffer = null;

		if (ERROR != 0) {
			throw new RuntimeException("Err . Raise ERROR");
		}
	}

	/* '' */
	/* ' Handles the "GuildPeaceDetails" message. */
	/* ' */
	/* ' @param userIndex The index of the user sending the message. */

	static void HandleGuildPeaceDetails(int UserIndex) {
		/* '*************************************************** */
		/* 'Author: Juan Martín Sotuyo Dodero (Maraxus) */
		/* 'Last Modification: 05/17/06 */
		/* ' */
		/* '*************************************************** */
		if (Declaraciones.UserList[UserIndex].incomingData.length < 3) {
			throw new RuntimeException("Err . Raise UserList ( UserIndex ) . incomingData . NotEnoughDataErrCode");
			return;
		}

		/* FIXME: ON ERROR GOTO ErrHandler */
		/*
		 * 'This packet contains strings, make a copy of the data to prevent
		 * losses if it's not complete yet...
		 */
		clsByteQueue buffer;
		buffer = new clsByteQueue();
		buffer.CopyBuffer(Declaraciones.UserList[UserIndex].incomingData);

		/* 'Remove packet ID */
		buffer.ReadByte();

		String guild;
		String errorStr;
		String details;

		guild = buffer.ReadASCIIString();

		details = modGuilds.r_VerPropuesta(UserIndex, guild, RELACIONES_GUILD.PAZ, errorStr);

		if (vb6.LenB(details) == 0) {
			WriteConsoleMsg(UserIndex, errorStr, FontTypeNames.FONTTYPE_GUILD);
		} else {
			WriteOfferDetails(UserIndex, details);
		}

		/*
		 * 'If we got here then packet is complete, copy data back to original
		 * queue
		 */
		Declaraciones.UserList[UserIndex].incomingData.CopyBuffer(buffer);

		/* FIXME: ErrHandler : */
		int ERROR;
		/* ERROR = Err . Number */
		/* FIXME: ON ERROR GOTO 0 */

		/* 'Destroy auxiliar buffer */
		buffer = null;

		if (ERROR != 0) {
			throw new RuntimeException("Err . Raise ERROR");
		}
	}

	/* '' */
	/* ' Handles the "GuildRequestJoinerInfo" message. */
	/* ' */
	/* ' @param userIndex The index of the user sending the message. */

	static void HandleGuildRequestJoinerInfo(int UserIndex) {
		/* '*************************************************** */
		/* 'Author: Juan Martín Sotuyo Dodero (Maraxus) */
		/* 'Last Modification: 05/17/06 */
		/* ' */
		/* '*************************************************** */
		if (Declaraciones.UserList[UserIndex].incomingData.length < 3) {
			throw new RuntimeException("Err . Raise UserList ( UserIndex ) . incomingData . NotEnoughDataErrCode");
			return;
		}

		/* FIXME: ON ERROR GOTO ErrHandler */
		/*
		 * 'This packet contains strings, make a copy of the data to prevent
		 * losses if it's not complete yet...
		 */
		clsByteQueue buffer;
		buffer = new clsByteQueue();
		buffer.CopyBuffer(Declaraciones.UserList[UserIndex].incomingData);

		/* 'Remove packet ID */
		buffer.ReadByte();

		String User;
		String details;

		User = buffer.ReadASCIIString();

		details = modGuilds.a_DetallesAspirante(UserIndex, User);

		if (vb6.LenB(details) == 0) {
			WriteConsoleMsg(UserIndex, "El personaje no ha mandado solicitud, o no estás habilitado para verla.",
					FontTypeNames.FONTTYPE_GUILD);
		} else {
			WriteShowUserRequest(UserIndex, details);
		}

		/*
		 * 'If we got here then packet is complete, copy data back to original
		 * queue
		 */
		Declaraciones.UserList[UserIndex].incomingData.CopyBuffer(buffer);

		/* FIXME: ErrHandler : */
		int ERROR;
		/* ERROR = Err . Number */
		/* FIXME: ON ERROR GOTO 0 */

		/* 'Destroy auxiliar buffer */
		buffer = null;

		if (ERROR != 0) {
			throw new RuntimeException("Err . Raise ERROR");
		}
	}

	/* '' */
	/* ' Handles the "GuildAlliancePropList" message. */
	/* ' */
	/* ' @param userIndex The index of the user sending the message. */

	static void HandleGuildAlliancePropList(int UserIndex) {
		/* '*************************************************** */
		/* 'Author: Juan Martín Sotuyo Dodero (Maraxus) */
		/* 'Last Modification: 05/17/06 */
		/* ' */
		/* '*************************************************** */
		/* 'Remove packet ID */
		Declaraciones.UserList[UserIndex].incomingData.ReadByte();

		WriteAlianceProposalsList(UserIndex, modGuilds.r_ListaDePropuestas(UserIndex, RELACIONES_GUILD.ALIADOS));
	}

	/* '' */
	/* ' Handles the "GuildPeacePropList" message. */
	/* ' */
	/* ' @param userIndex The index of the user sending the message. */

	static void HandleGuildPeacePropList(int UserIndex) {
		/* '*************************************************** */
		/* 'Author: Juan Martín Sotuyo Dodero (Maraxus) */
		/* 'Last Modification: 05/17/06 */
		/* ' */
		/* '*************************************************** */
		/* 'Remove packet ID */
		Declaraciones.UserList[UserIndex].incomingData.ReadByte();

		WritePeaceProposalsList(UserIndex, modGuilds.r_ListaDePropuestas(UserIndex, RELACIONES_GUILD.PAZ));
	}

	/* '' */
	/* ' Handles the "GuildDeclareWar" message. */
	/* ' */
	/* ' @param userIndex The index of the user sending the message. */

	static void HandleGuildDeclareWar(int UserIndex) {
		/* '*************************************************** */
		/* 'Author: Juan Martín Sotuyo Dodero (Maraxus) */
		/* 'Last Modification: 05/17/06 */
		/* ' */
		/* '*************************************************** */
		if (Declaraciones.UserList[UserIndex].incomingData.length < 3) {
			throw new RuntimeException("Err . Raise UserList ( UserIndex ) . incomingData . NotEnoughDataErrCode");
			return;
		}

		/* FIXME: ON ERROR GOTO ErrHandler */
		/*
		 * 'This packet contains strings, make a copy of the data to prevent
		 * losses if it's not complete yet...
		 */
		clsByteQueue buffer;
		buffer = new clsByteQueue();
		buffer.CopyBuffer(Declaraciones.UserList[UserIndex].incomingData);

		/* 'Remove packet ID */
		buffer.ReadByte();

		String guild;
		String errorStr;
		int otherGuildIndex;

		guild = buffer.ReadASCIIString();

		otherGuildIndex = modGuilds.r_DeclararGuerra(UserIndex, guild, errorStr);

		if (otherGuildIndex == 0) {
			WriteConsoleMsg(UserIndex, errorStr, FontTypeNames.FONTTYPE_GUILD);
		} else {
			/* 'WAR shall be! */
			modSendData.SendData(SendTarget.ToGuildMembers, Declaraciones.UserList[UserIndex].GuildIndex,
					PrepareMessageConsoleMsg("TU CLAN HA ENTRADO EN GUERRA CON " + guild + ".",
							FontTypeNames.FONTTYPE_GUILD));
			modSendData.SendData(SendTarget.ToGuildMembers, otherGuildIndex,
					PrepareMessageConsoleMsg(modGuilds.GuildName(Declaraciones.UserList[UserIndex].GuildIndex)
							+ " LE DECLARA LA GUERRA A TU CLAN.", FontTypeNames.FONTTYPE_GUILD));
			modSendData.SendData(SendTarget.ToGuildMembers, Declaraciones.UserList[UserIndex].GuildIndex,
					PrepareMessagePlayWave(45, Declaraciones.NO_3D_SOUND, Declaraciones.NO_3D_SOUND));
			modSendData.SendData(SendTarget.ToGuildMembers, otherGuildIndex,
					PrepareMessagePlayWave(45, Declaraciones.NO_3D_SOUND, Declaraciones.NO_3D_SOUND));
		}

		/*
		 * 'If we got here then packet is complete, copy data back to original
		 * queue
		 */
		Declaraciones.UserList[UserIndex].incomingData.CopyBuffer(buffer);

		/* FIXME: ErrHandler : */
		int ERROR;
		/* ERROR = Err . Number */
		/* FIXME: ON ERROR GOTO 0 */

		/* 'Destroy auxiliar buffer */
		buffer = null;

		if (ERROR != 0) {
			throw new RuntimeException("Err . Raise ERROR");
		}
	}

	/* '' */
	/* ' Handles the "GuildNewWebsite" message. */
	/* ' */
	/* ' @param userIndex The index of the user sending the message. */

	static void HandleGuildNewWebsite(int UserIndex) {
		/* '*************************************************** */
		/* 'Author: Juan Martín Sotuyo Dodero (Maraxus) */
		/* 'Last Modification: 05/17/06 */
		/* ' */
		/* '*************************************************** */
		if (Declaraciones.UserList[UserIndex].incomingData.length < 3) {
			throw new RuntimeException("Err . Raise UserList ( UserIndex ) . incomingData . NotEnoughDataErrCode");
			return;
		}

		/* FIXME: ON ERROR GOTO ErrHandler */
		/*
		 * 'This packet contains strings, make a copy of the data to prevent
		 * losses if it's not complete yet...
		 */
		clsByteQueue buffer;
		buffer = new clsByteQueue();
		buffer.CopyBuffer(Declaraciones.UserList[UserIndex].incomingData);

		/* 'Remove packet ID */
		buffer.ReadByte();

		modGuilds.ActualizarWebSite(UserIndex, buffer.ReadASCIIString());

		/*
		 * 'If we got here then packet is complete, copy data back to original
		 * queue
		 */
		Declaraciones.UserList[UserIndex].incomingData.CopyBuffer(buffer);

		/* FIXME: ErrHandler : */
		int ERROR;
		/* ERROR = Err . Number */
		/* FIXME: ON ERROR GOTO 0 */

		/* 'Destroy auxiliar buffer */
		buffer = null;

		if (ERROR != 0) {
			throw new RuntimeException("Err . Raise ERROR");
		}
	}

	/* '' */
	/* ' Handles the "GuildAcceptNewMember" message. */
	/* ' */
	/* ' @param userIndex The index of the user sending the message. */

	static void HandleGuildAcceptNewMember(int UserIndex) {
		/* '*************************************************** */
		/* 'Author: Juan Martín Sotuyo Dodero (Maraxus) */
		/* 'Last Modification: 05/17/06 */
		/* ' */
		/* '*************************************************** */
		if (Declaraciones.UserList[UserIndex].incomingData.length < 3) {
			throw new RuntimeException("Err . Raise UserList ( UserIndex ) . incomingData . NotEnoughDataErrCode");
			return;
		}

		/* FIXME: ON ERROR GOTO ErrHandler */
		/*
		 * 'This packet contains strings, make a copy of the data to prevent
		 * losses if it's not complete yet...
		 */
		clsByteQueue buffer;
		buffer = new clsByteQueue();
		buffer.CopyBuffer(Declaraciones.UserList[UserIndex].incomingData);

		/* 'Remove packet ID */
		buffer.ReadByte();

		String errorStr;
		String UserName;
		int tUser;

		UserName = buffer.ReadASCIIString();

		if (!modGuilds.a_AceptarAspirante(UserIndex, UserName, errorStr)) {
			WriteConsoleMsg(UserIndex, errorStr, FontTypeNames.FONTTYPE_GUILD);
		} else {
			tUser = Extra.NameIndex(UserName);
			if (tUser > 0) {
				modGuilds.m_ConectarMiembroAClan(tUser, Declaraciones.UserList[UserIndex].GuildIndex);
				UsUaRiOs.RefreshCharStatus(tUser);
			}

			modSendData.SendData(SendTarget.ToGuildMembers, Declaraciones.UserList[UserIndex].GuildIndex,
					PrepareMessageConsoleMsg(UserName + " ha sido aceptado como miembro del clan.",
							FontTypeNames.FONTTYPE_GUILD));
			modSendData.SendData(SendTarget.ToGuildMembers, Declaraciones.UserList[UserIndex].GuildIndex,
					PrepareMessagePlayWave(43, Declaraciones.NO_3D_SOUND, Declaraciones.NO_3D_SOUND));
		}

		/*
		 * 'If we got here then packet is complete, copy data back to original
		 * queue
		 */
		Declaraciones.UserList[UserIndex].incomingData.CopyBuffer(buffer);

		/* FIXME: ErrHandler : */
		int ERROR;
		/* ERROR = Err . Number */
		/* FIXME: ON ERROR GOTO 0 */

		/* 'Destroy auxiliar buffer */
		buffer = null;

		if (ERROR != 0) {
			throw new RuntimeException("Err . Raise ERROR");
		}
	}

	/* '' */
	/* ' Handles the "GuildRejectNewMember" message. */
	/* ' */
	/* ' @param userIndex The index of the user sending the message. */

	static void HandleGuildRejectNewMember(int UserIndex) {
		/* '*************************************************** */
		/* 'Author: Juan Martín Sotuyo Dodero (Maraxus) */
		/* 'Last Modification: 01/08/07 */
		/* 'Last Modification by: (liquid) */
		/* ' */
		/* '*************************************************** */
		if (Declaraciones.UserList[UserIndex].incomingData.length < 5) {
			throw new RuntimeException("Err . Raise UserList ( UserIndex ) . incomingData . NotEnoughDataErrCode");
			return;
		}

		/* FIXME: ON ERROR GOTO ErrHandler */
		/*
		 * 'This packet contains strings, make a copy of the data to prevent
		 * losses if it's not complete yet...
		 */
		clsByteQueue buffer;
		buffer = new clsByteQueue();
		buffer.CopyBuffer(Declaraciones.UserList[UserIndex].incomingData);

		/* 'Remove packet ID */
		buffer.ReadByte();

		String errorStr;
		String UserName;
		String Reason;
		int tUser;

		UserName = buffer.ReadASCIIString();
		Reason = buffer.ReadASCIIString();

		if (!modGuilds.a_RechazarAspirante(UserIndex, UserName, errorStr)) {
			WriteConsoleMsg(UserIndex, errorStr, FontTypeNames.FONTTYPE_GUILD);
		} else {
			tUser = Extra.NameIndex(UserName);

			if (tUser > 0) {
				WriteConsoleMsg(tUser, errorStr + " : " + Reason, FontTypeNames.FONTTYPE_GUILD);
			} else {
				/* 'hay que grabar en el char su rechazo */
				modGuilds.a_RechazarAspiranteChar(UserName, Declaraciones.UserList[UserIndex].GuildIndex, Reason);
			}
		}

		/*
		 * 'If we got here then packet is complete, copy data back to original
		 * queue
		 */
		Declaraciones.UserList[UserIndex].incomingData.CopyBuffer(buffer);

		/* FIXME: ErrHandler : */
		int ERROR;
		/* ERROR = Err . Number */
		/* FIXME: ON ERROR GOTO 0 */

		/* 'Destroy auxiliar buffer */
		buffer = null;

		if (ERROR != 0) {
			throw new RuntimeException("Err . Raise ERROR");
		}
	}

	/* '' */
	/* ' Handles the "GuildKickMember" message. */
	/* ' */
	/* ' @param userIndex The index of the user sending the message. */

	static void HandleGuildKickMember(int UserIndex) {
		/* '*************************************************** */
		/* 'Author: Juan Martín Sotuyo Dodero (Maraxus) */
		/* 'Last Modification: 05/17/06 */
		/* ' */
		/* '*************************************************** */
		if (Declaraciones.UserList[UserIndex].incomingData.length < 3) {
			throw new RuntimeException("Err . Raise UserList ( UserIndex ) . incomingData . NotEnoughDataErrCode");
			return;
		}

		/* FIXME: ON ERROR GOTO ErrHandler */
		/*
		 * 'This packet contains strings, make a copy of the data to prevent
		 * losses if it's not complete yet...
		 */
		clsByteQueue buffer;
		buffer = new clsByteQueue();
		buffer.CopyBuffer(Declaraciones.UserList[UserIndex].incomingData);

		/* 'Remove packet ID */
		buffer.ReadByte();

		String UserName;
		int GuildIndex;

		UserName = buffer.ReadASCIIString();

		GuildIndex = modGuilds.m_EcharMiembroDeClan(UserIndex, UserName);

		if (GuildIndex > 0) {
			modSendData.SendData(SendTarget.ToGuildMembers, GuildIndex,
					PrepareMessageConsoleMsg(UserName + " fue expulsado del clan.", FontTypeNames.FONTTYPE_GUILD));
			modSendData.SendData(SendTarget.ToGuildMembers, GuildIndex,
					PrepareMessagePlayWave(45, Declaraciones.NO_3D_SOUND, Declaraciones.NO_3D_SOUND));
		} else {
			WriteConsoleMsg(UserIndex, "No puedes expulsar ese personaje del clan.", FontTypeNames.FONTTYPE_GUILD);
		}

		/*
		 * 'If we got here then packet is complete, copy data back to original
		 * queue
		 */
		Declaraciones.UserList[UserIndex].incomingData.CopyBuffer(buffer);

		/* FIXME: ErrHandler : */
		int ERROR;
		/* ERROR = Err . Number */
		/* FIXME: ON ERROR GOTO 0 */

		/* 'Destroy auxiliar buffer */
		buffer = null;

		if (ERROR != 0) {
			throw new RuntimeException("Err . Raise ERROR");
		}
	}

	/* '' */
	/* ' Handles the "GuildUpdateNews" message. */
	/* ' */
	/* ' @param userIndex The index of the user sending the message. */

	static void HandleGuildUpdateNews(int UserIndex) {
		/* '*************************************************** */
		/* 'Author: Juan Martín Sotuyo Dodero (Maraxus) */
		/* 'Last Modification: 05/17/06 */
		/* ' */
		/* '*************************************************** */
		if (Declaraciones.UserList[UserIndex].incomingData.length < 3) {
			throw new RuntimeException("Err . Raise UserList ( UserIndex ) . incomingData . NotEnoughDataErrCode");
			return;
		}

		/* FIXME: ON ERROR GOTO ErrHandler */
		/*
		 * 'This packet contains strings, make a copy of the data to prevent
		 * losses if it's not complete yet...
		 */
		clsByteQueue buffer;
		buffer = new clsByteQueue();
		buffer.CopyBuffer(Declaraciones.UserList[UserIndex].incomingData);

		/* 'Remove packet ID */
		buffer.ReadByte();

		modGuilds.ActualizarNoticias(UserIndex, buffer.ReadASCIIString());

		/*
		 * 'If we got here then packet is complete, copy data back to original
		 * queue
		 */
		Declaraciones.UserList[UserIndex].incomingData.CopyBuffer(buffer);

		/* FIXME: ErrHandler : */
		int ERROR;
		/* ERROR = Err . Number */
		/* FIXME: ON ERROR GOTO 0 */

		/* 'Destroy auxiliar buffer */
		buffer = null;

		if (ERROR != 0) {
			throw new RuntimeException("Err . Raise ERROR");
		}
	}

	/* '' */
	/* ' Handles the "GuildMemberInfo" message. */
	/* ' */
	/* ' @param userIndex The index of the user sending the message. */

	static void HandleGuildMemberInfo(int UserIndex) {
		/* '*************************************************** */
		/* 'Author: Juan Martín Sotuyo Dodero (Maraxus) */
		/* 'Last Modification: 05/17/06 */
		/* ' */
		/* '*************************************************** */
		if (Declaraciones.UserList[UserIndex].incomingData.length < 3) {
			throw new RuntimeException("Err . Raise UserList ( UserIndex ) . incomingData . NotEnoughDataErrCode");
			return;
		}

		/* FIXME: ON ERROR GOTO ErrHandler */
		/*
		 * 'This packet contains strings, make a copy of the data to prevent
		 * losses if it's not complete yet...
		 */
		clsByteQueue buffer;
		buffer = new clsByteQueue();
		buffer.CopyBuffer(Declaraciones.UserList[UserIndex].incomingData);

		/* 'Remove packet ID */
		buffer.ReadByte();

		modGuilds.SendDetallesPersonaje(UserIndex, buffer.ReadASCIIString());

		/*
		 * 'If we got here then packet is complete, copy data back to original
		 * queue
		 */
		Declaraciones.UserList[UserIndex].incomingData.CopyBuffer(buffer);

		/* FIXME: ErrHandler : */
		int ERROR;
		/* ERROR = Err . Number */
		/* FIXME: ON ERROR GOTO 0 */

		/* 'Destroy auxiliar buffer */
		buffer = null;

		if (ERROR != 0) {
			throw new RuntimeException("Err . Raise ERROR");
		}
	}

	/* '' */
	/* ' Handles the "GuildOpenElections" message. */
	/* ' */
	/* ' @param userIndex The index of the user sending the message. */

	static void HandleGuildOpenElections(int UserIndex) {
		/* '*************************************************** */
		/* 'Author: Juan Martín Sotuyo Dodero (Maraxus) */
		/* 'Last Modification: 05/17/06 */
		/* ' */
		/* '*************************************************** */
		/* 'Remove packet ID */
		Declaraciones.UserList[UserIndex].incomingData.ReadByte();

		String ERROR;

		if (!modGuilds.v_AbrirElecciones(UserIndex, ERROR)) {
			WriteConsoleMsg(UserIndex, ERROR, FontTypeNames.FONTTYPE_GUILD);
		} else {
			modSendData.SendData(SendTarget.ToGuildMembers, Declaraciones.UserList[UserIndex].GuildIndex,
					PrepareMessageConsoleMsg(
							"¡Han comenzado las elecciones del clan! Puedes votar escribiendo /VOTO seguido del nombre del personaje, por ejemplo: /VOTO "
									+ Declaraciones.UserList[UserIndex].Name,
							FontTypeNames.FONTTYPE_GUILD));
		}
	}

	/* '' */
	/* ' Handles the "GuildRequestMembership" message. */
	/* ' */
	/* ' @param userIndex The index of the user sending the message. */

	static void HandleGuildRequestMembership(int UserIndex) {
		/* '*************************************************** */
		/* 'Author: Juan Martín Sotuyo Dodero (Maraxus) */
		/* 'Last Modification: 05/17/06 */
		/* ' */
		/* '*************************************************** */
		if (Declaraciones.UserList[UserIndex].incomingData.length < 5) {
			throw new RuntimeException("Err . Raise UserList ( UserIndex ) . incomingData . NotEnoughDataErrCode");
			return;
		}

		/* FIXME: ON ERROR GOTO ErrHandler */
		/*
		 * 'This packet contains strings, make a copy of the data to prevent
		 * losses if it's not complete yet...
		 */
		clsByteQueue buffer;
		buffer = new clsByteQueue();
		buffer.CopyBuffer(Declaraciones.UserList[UserIndex].incomingData);

		/* 'Remove packet ID */
		buffer.ReadByte();

		String guild;
		String application;
		String errorStr;

		guild = buffer.ReadASCIIString();
		application = buffer.ReadASCIIString();

		if (!modGuilds.a_NuevoAspirante(UserIndex, guild, application, errorStr)) {
			WriteConsoleMsg(UserIndex, errorStr, FontTypeNames.FONTTYPE_GUILD);
		} else {
			WriteConsoleMsg(UserIndex,
					"Tu solicitud ha sido enviada. Espera prontas noticias del líder de " + guild + ".",
					FontTypeNames.FONTTYPE_GUILD);
		}

		/*
		 * 'If we got here then packet is complete, copy data back to original
		 * queue
		 */
		Declaraciones.UserList[UserIndex].incomingData.CopyBuffer(buffer);

		/* FIXME: ErrHandler : */
		int ERROR;
		/* ERROR = Err . Number */
		/* FIXME: ON ERROR GOTO 0 */

		/* 'Destroy auxiliar buffer */
		buffer = null;

		if (ERROR != 0) {
			throw new RuntimeException("Err . Raise ERROR");
		}
	}

	/* '' */
	/* ' Handles the "GuildRequestDetails" message. */
	/* ' */
	/* ' @param userIndex The index of the user sending the message. */

	static void HandleGuildRequestDetails(int UserIndex) {
		/* '*************************************************** */
		/* 'Author: Juan Martín Sotuyo Dodero (Maraxus) */
		/* 'Last Modification: 05/17/06 */
		/* ' */
		/* '*************************************************** */
		if (Declaraciones.UserList[UserIndex].incomingData.length < 3) {
			throw new RuntimeException("Err . Raise UserList ( UserIndex ) . incomingData . NotEnoughDataErrCode");
			return;
		}

		/* FIXME: ON ERROR GOTO ErrHandler */
		/*
		 * 'This packet contains strings, make a copy of the data to prevent
		 * losses if it's not complete yet...
		 */
		clsByteQueue buffer;
		buffer = new clsByteQueue();
		buffer.CopyBuffer(Declaraciones.UserList[UserIndex].incomingData);

		/* 'Remove packet ID */
		buffer.ReadByte();

		modGuilds.SendGuildDetails(UserIndex, buffer.ReadASCIIString());

		/*
		 * 'If we got here then packet is complete, copy data back to original
		 * queue
		 */
		Declaraciones.UserList[UserIndex].incomingData.CopyBuffer(buffer);

		/* FIXME: ErrHandler : */
		int ERROR;
		/* ERROR = Err . Number */
		/* FIXME: ON ERROR GOTO 0 */

		/* 'Destroy auxiliar buffer */
		buffer = null;

		if (ERROR != 0) {
			throw new RuntimeException("Err . Raise ERROR");
		}
	}

	/* '' */
	/* ' Handles the "Online" message. */
	/* ' */
	/* ' @param userIndex The index of the user sending the message. */

	static void HandleOnline(int UserIndex) {
		/* '*************************************************** */
		/* 'Author: Juan Martín Sotuyo Dodero (Maraxus) */
		/* 'Last Modification: 05/17/06 */
		/* ' */
		/* '*************************************************** */
		int i;
		int Count;

		/* 'Remove packet ID */
		Declaraciones.UserList[UserIndex].incomingData.ReadByte();

		for (i = (1); i <= (Declaraciones.LastUser); i++) {
			if (vb6.LenB(Declaraciones.UserList[i].Name) != 0) {
				if (Declaraciones.UserList[i].flags.Privilegios && (PlayerType.User || PlayerType.Consejero)) {
					Count = Count + 1;
				}
			}
		}

		WriteConsoleMsg(UserIndex, "Número de usuarios: " + vb6.CStr(Count), FontTypeNames.FONTTYPE_INFO);
	}

	/* '' */
	/* ' Handles the "Quit" message. */
	/* ' */
	/* ' @param userIndex The index of the user sending the message. */

	static void HandleQuit(int UserIndex) {
		/* '*************************************************** */
		/* 'Author: Juan Martín Sotuyo Dodero (Maraxus) */
		/* 'Last Modification: 04/15/2008 (NicoNZ) */
		/* 'If user is invisible, it automatically becomes */
		/* 'visible before doing the countdown to exit */
		/*
		 * '04/15/2008 - No se reseteaban lso contadores de invi ni de ocultar.
		 * (NicoNZ)
		 */
		/* '*************************************************** */
		int tUser;
		boolean isNotVisible;

		/* 'Remove packet ID */
		Declaraciones.UserList[UserIndex].incomingData.ReadByte();

		if (Declaraciones.UserList[UserIndex].flags.Paralizado == 1) {
			WriteConsoleMsg(UserIndex, "No puedes salir estando paralizado.", FontTypeNames.FONTTYPE_WARNING);
			return;
		}

		/* 'exit secure commerce */
		if (Declaraciones.UserList[UserIndex].ComUsu.DestUsu > 0) {
			tUser = Declaraciones.UserList[UserIndex].ComUsu.DestUsu;

			if (Declaraciones.UserList[tUser].flags.UserLogged) {
				if (Declaraciones.UserList[tUser].ComUsu.DestUsu == UserIndex) {
					WriteConsoleMsg(tUser, "Comercio cancelado por el otro usuario.", FontTypeNames.FONTTYPE_TALK);
					mdlCOmercioConUsuario.FinComerciarUsu(tUser);
				}
			}

			WriteConsoleMsg(UserIndex, "Comercio cancelado.", FontTypeNames.FONTTYPE_TALK);
			mdlCOmercioConUsuario.FinComerciarUsu(UserIndex);
		}

		UsUaRiOs.Cerrar_Usuario(UserIndex);
	}

	/* '' */
	/* ' Handles the "GuildLeave" message. */
	/* ' */
	/* ' @param userIndex The index of the user sending the message. */

	static void HandleGuildLeave(int UserIndex) {
		/* '*************************************************** */
		/* 'Author: Juan Martín Sotuyo Dodero (Maraxus) */
		/* 'Last Modification: 05/17/06 */
		/* ' */
		/* '*************************************************** */
		int GuildIndex;

		/* 'Remove packet ID */
		Declaraciones.UserList[UserIndex].incomingData.ReadByte();

		/* 'obtengo el guildindex */
		GuildIndex = modGuilds.m_EcharMiembroDeClan(UserIndex, Declaraciones.UserList[UserIndex].Name);

		if (GuildIndex > 0) {
			WriteConsoleMsg(UserIndex, "Dejas el clan.", FontTypeNames.FONTTYPE_GUILD);
			modSendData.SendData(SendTarget.ToGuildMembers, GuildIndex, PrepareMessageConsoleMsg(
					Declaraciones.UserList[UserIndex].Name + " deja el clan.", FontTypeNames.FONTTYPE_GUILD));
		} else {
			WriteConsoleMsg(UserIndex, "Tú no puedes salir de este clan.", FontTypeNames.FONTTYPE_GUILD);
		}
	}

	/* '' */
	/* ' Handles the "RequestAccountState" message. */
	/* ' */
	/* ' @param userIndex The index of the user sending the message. */

	static void HandleRequestAccountState(int UserIndex) {
		/* '*************************************************** */
		/* 'Author: Juan Martín Sotuyo Dodero (Maraxus) */
		/* 'Last Modification: 05/17/06 */
		/* ' */
		/* '*************************************************** */
		int earnings;
		int Percentage;

		/* 'Remove packet ID */
		Declaraciones.UserList[UserIndex].incomingData.ReadByte();

		/* 'Dead people can't check their accounts */
		if (Declaraciones.UserList[UserIndex].flags.Muerto == 1) {
			WriteConsoleMsg(UserIndex, "¡¡Estás muerto!!", FontTypeNames.FONTTYPE_INFO);
			return;
		}

		/* 'Validate target NPC */
		if (Declaraciones.UserList[UserIndex].flags.TargetNPC == 0) {
			WriteConsoleMsg(UserIndex, "Primero tienes que seleccionar un personaje, haz click izquierdo sobre él.",
					FontTypeNames.FONTTYPE_INFO);
			return;
		}

		if (Matematicas.Distancia(Declaraciones.Npclist[Declaraciones.UserList[UserIndex].flags.TargetNPC].Pos,
				Declaraciones.UserList[UserIndex].Pos) > 3) {
			WriteConsoleMsg(UserIndex, "Estás demasiado lejos del vendedor.", FontTypeNames.FONTTYPE_INFO);
			return;
		}

		switch (Declaraciones.Npclist[Declaraciones.UserList[UserIndex].flags.TargetNPC].NPCtype) {
		case Banquero:
			WriteChatOverHead(UserIndex,
					"Tienes " + Declaraciones.UserList[UserIndex].Stats.Banco + " monedas de oro en tu cuenta.",
					Declaraciones.Npclist[Declaraciones.UserList[UserIndex].flags.TargetNPC].Char.CharIndex,
					0x00ffffff);

			break;

		case Timbero:
			if (!Declaraciones.UserList[UserIndex].flags.Privilegios && PlayerType.User) {
				earnings = Admin.Apuestas.Ganancias - Admin.Apuestas.Perdidas;

				if (earnings >= 0 && Admin.Apuestas.Ganancias != 0) {
					Percentage = vb6.Int(earnings * 100 / Admin.Apuestas.Ganancias);
				}

				if (earnings < 0 && Admin.Apuestas.Perdidas != 0) {
					Percentage = vb6.Int(earnings * 100 / Admin.Apuestas.Perdidas);
				}

				WriteConsoleMsg(UserIndex,
						"Entradas: " + Admin.Apuestas.Ganancias + " Salida: " + Admin.Apuestas.Perdidas
								+ " Ganancia Neta: " + earnings + " (" + Percentage + "%) Jugadas: "
								+ Admin.Apuestas.Jugadas,
						FontTypeNames.FONTTYPE_INFO);
			}
			break;
		}
	}

	/* '' */
	/* ' Handles the "PetStand" message. */
	/* ' */
	/* ' @param userIndex The index of the user sending the message. */

	static void HandlePetStand(int UserIndex) {
		/* '*************************************************** */
		/* 'Author: Juan Martín Sotuyo Dodero (Maraxus) */
		/* 'Last Modification: 05/17/06 */
		/* ' */
		/* '*************************************************** */
		/* 'Remove packet ID */
		Declaraciones.UserList[UserIndex].incomingData.ReadByte();

		/* 'Dead people can't use pets */
		if (Declaraciones.UserList[UserIndex].flags.Muerto == 1) {
			WriteConsoleMsg(UserIndex, "¡¡Estás muerto!!", FontTypeNames.FONTTYPE_INFO);
			return;
		}

		/* 'Validate target NPC */
		if (Declaraciones.UserList[UserIndex].flags.TargetNPC == 0) {
			WriteConsoleMsg(UserIndex, "Primero tienes que seleccionar un personaje, haz click izquierdo sobre él.",
					FontTypeNames.FONTTYPE_INFO);
			return;
		}

		/* 'Make sure it's close enough */
		if (Matematicas.Distancia(Declaraciones.Npclist[Declaraciones.UserList[UserIndex].flags.TargetNPC].Pos,
				Declaraciones.UserList[UserIndex].Pos) > 10) {
			WriteConsoleMsg(UserIndex, "Estás demasiado lejos.", FontTypeNames.FONTTYPE_INFO);
			return;
		}

		/* 'Make sure it's his pet */
		if (Declaraciones.Npclist[Declaraciones.UserList[UserIndex].flags.TargetNPC].MaestroUser != UserIndex) {
			return;
		}

		/* 'Do it! */
		Declaraciones.Npclist[Declaraciones.UserList[UserIndex].flags.TargetNPC].Movement = TipoAI.ESTATICO;

		Extra.Expresar(Declaraciones.UserList[UserIndex].flags.TargetNPC, UserIndex);
	}

	/* '' */
	/* ' Handles the "PetFollow" message. */
	/* ' */
	/* ' @param userIndex The index of the user sending the message. */

	static void HandlePetFollow(int UserIndex) {
		/* '*************************************************** */
		/* 'Author: Juan Martín Sotuyo Dodero (Maraxus) */
		/* 'Last Modification: 05/17/06 */
		/* ' */
		/* '*************************************************** */
		/* 'Remove packet ID */
		Declaraciones.UserList[UserIndex].incomingData.ReadByte();

		/* 'Dead users can't use pets */
		if (Declaraciones.UserList[UserIndex].flags.Muerto == 1) {
			WriteConsoleMsg(UserIndex, "¡¡Estás muerto!!", FontTypeNames.FONTTYPE_INFO);
			return;
		}

		/* 'Validate target NPC */
		if (Declaraciones.UserList[UserIndex].flags.TargetNPC == 0) {
			WriteConsoleMsg(UserIndex, "Primero tienes que seleccionar un personaje, haz click izquierdo sobre él.",
					FontTypeNames.FONTTYPE_INFO);
			return;
		}

		/* 'Make sure it's close enough */
		if (Matematicas.Distancia(Declaraciones.Npclist[Declaraciones.UserList[UserIndex].flags.TargetNPC].Pos,
				Declaraciones.UserList[UserIndex].Pos) > 10) {
			WriteConsoleMsg(UserIndex, "Estás demasiado lejos.", FontTypeNames.FONTTYPE_INFO);
			return;
		}

		/* 'Make usre it's the user's pet */
		if (Declaraciones.Npclist[Declaraciones.UserList[UserIndex].flags.TargetNPC].MaestroUser != UserIndex) {
			return;
		}

		/* 'Do it */
		NPCs.FollowAmo(Declaraciones.UserList[UserIndex].flags.TargetNPC);

		Extra.Expresar(Declaraciones.UserList[UserIndex].flags.TargetNPC, UserIndex);
	}

	/* '' */
	/* ' Handles the "ReleasePet" message. */
	/* ' */
	/* ' @param userIndex The index of the user sending the message. */

	static void HandleReleasePet(int UserIndex) {
		/* '*************************************************** */
		/* 'Author: ZaMa */
		/* 'Last Modification: 18/11/2009 */
		/* ' */
		/* '*************************************************** */
		/* 'Remove packet ID */
		Declaraciones.UserList[UserIndex].incomingData.ReadByte();

		/* 'Dead users can't use pets */
		if (Declaraciones.UserList[UserIndex].flags.Muerto == 1) {
			WriteConsoleMsg(UserIndex, "¡¡Estás muerto!!", FontTypeNames.FONTTYPE_INFO);
			return;
		}

		/* 'Validate target NPC */
		if (Declaraciones.UserList[UserIndex].flags.TargetNPC == 0) {
			WriteConsoleMsg(UserIndex, "Primero tienes que seleccionar una mascota, haz click izquierdo sobre ella.",
					FontTypeNames.FONTTYPE_INFO);
			return;
		}

		/* 'Make usre it's the user's pet */
		if (Declaraciones.Npclist[Declaraciones.UserList[UserIndex].flags.TargetNPC].MaestroUser != UserIndex) {
			return;
		}

		/* 'Make sure it's close enough */
		if (Matematicas.Distancia(Declaraciones.Npclist[Declaraciones.UserList[UserIndex].flags.TargetNPC].Pos,
				Declaraciones.UserList[UserIndex].Pos) > 10) {
			WriteConsoleMsg(UserIndex, "Estás demasiado lejos.", FontTypeNames.FONTTYPE_INFO);
			return;
		}

		/* 'Do it */
		NPCs.QuitarPet(UserIndex, Declaraciones.UserList[UserIndex].flags.TargetNPC);

	}

	/* '' */
	/* ' Handles the "TrainList" message. */
	/* ' */
	/* ' @param userIndex The index of the user sending the message. */

	static void HandleTrainList(int UserIndex) {
		/* '*************************************************** */
		/* 'Author: Juan Martín Sotuyo Dodero (Maraxus) */
		/* 'Last Modification: 05/17/06 */
		/* ' */
		/* '*************************************************** */
		/* 'Remove packet ID */
		Declaraciones.UserList[UserIndex].incomingData.ReadByte();

		/* 'Dead users can't use pets */
		if (Declaraciones.UserList[UserIndex].flags.Muerto == 1) {
			WriteConsoleMsg(UserIndex, "¡¡Estás muerto!!", FontTypeNames.FONTTYPE_INFO);
			return;
		}

		/* 'Validate target NPC */
		if (Declaraciones.UserList[UserIndex].flags.TargetNPC == 0) {
			WriteConsoleMsg(UserIndex, "Primero tienes que seleccionar un personaje, haz click izquierdo sobre él.",
					FontTypeNames.FONTTYPE_INFO);
			return;
		}

		/* 'Make sure it's close enough */
		if (Matematicas.Distancia(Declaraciones.Npclist[Declaraciones.UserList[UserIndex].flags.TargetNPC].Pos,
				Declaraciones.UserList[UserIndex].Pos) > 10) {
			WriteConsoleMsg(UserIndex, "Estás demasiado lejos.", FontTypeNames.FONTTYPE_INFO);
			return;
		}

		/* 'Make sure it's the trainer */
		if (Declaraciones.Npclist[Declaraciones.UserList[UserIndex].flags.TargetNPC].NPCtype != eNPCType.Entrenador) {
			return;
		}

		WriteTrainerCreatureList(UserIndex, Declaraciones.UserList[UserIndex].flags.TargetNPC);
	}

	/* '' */
	/* ' Handles the "Rest" message. */
	/* ' */
	/* ' @param userIndex The index of the user sending the message. */

	static void HandleRest(int UserIndex) {
		/* '*************************************************** */
		/* 'Author: Juan Martín Sotuyo Dodero (Maraxus) */
		/* 'Last Modification: 05/17/06 */
		/* ' */
		/* '*************************************************** */
		/* 'Remove packet ID */
		Declaraciones.UserList[UserIndex].incomingData.ReadByte();

		/* 'Dead users can't use pets */
		if (Declaraciones.UserList[UserIndex].flags.Muerto == 1) {
			WriteConsoleMsg(UserIndex, "¡¡Estás muerto!! Solo puedes usar ítems cuando estás vivo.",
					FontTypeNames.FONTTYPE_INFO);
			return;
		}

		if (TCP.HayOBJarea(Declaraciones.UserList[UserIndex].Pos, Declaraciones.FOGATA)) {
			WriteRestOK(UserIndex);

			if (!Declaraciones.UserList[UserIndex].flags.Descansar) {
				WriteConsoleMsg(UserIndex, "Te acomodás junto a la fogata y comienzas a descansar.",
						FontTypeNames.FONTTYPE_INFO);
			} else {
				WriteConsoleMsg(UserIndex, "Te levantas.", FontTypeNames.FONTTYPE_INFO);
			}

			Declaraciones.UserList[UserIndex].flags.Descansar = !Declaraciones.UserList[UserIndex].flags.Descansar;
		} else {
			if (Declaraciones.UserList[UserIndex].flags.Descansar) {
				WriteRestOK(UserIndex);
				WriteConsoleMsg(UserIndex, "Te levantas.", FontTypeNames.FONTTYPE_INFO);

				Declaraciones.UserList[UserIndex].flags.Descansar = false;
				return;
			}

			WriteConsoleMsg(UserIndex, "No hay ninguna fogata junto a la cual descansar.", FontTypeNames.FONTTYPE_INFO);
		}
	}

	/* '' */
	/* ' Handles the "Meditate" message. */
	/* ' */
	/* ' @param userIndex The index of the user sending the message. */

	static void HandleMeditate(int UserIndex) {
		/* '*************************************************** */
		/* 'Author: Juan Martín Sotuyo Dodero (Maraxus) */
		/* 'Last Modification: 04/15/08 (NicoNZ) */
		/* 'Arreglé un bug que mandaba un index de la meditacion diferente */
		/* 'al que decia el server. */
		/* '*************************************************** */
		/* 'Remove packet ID */
		Declaraciones.UserList[UserIndex].incomingData.ReadByte();

		/* 'Dead users can't use pets */
		if (Declaraciones.UserList[UserIndex].flags.Muerto == 1) {
			WriteConsoleMsg(UserIndex, "¡¡Estás muerto!! Sólo puedes meditar cuando estás vivo.",
					FontTypeNames.FONTTYPE_INFO);
			return;
		}

		/* 'Can he meditate? */
		if (Declaraciones.UserList[UserIndex].Stats.MaxMAN == 0) {
			WriteConsoleMsg(UserIndex, "Sólo las clases mágicas conocen el arte de la meditación.",
					FontTypeNames.FONTTYPE_INFO);
			return;
		}

		/* 'Admins don't have to wait :D */
		if (!Declaraciones.UserList[UserIndex].flags.Privilegios && PlayerType.User) {
			Declaraciones.UserList[UserIndex].Stats.MinMAN = Declaraciones.UserList[UserIndex].Stats.MaxMAN;
			WriteConsoleMsg(UserIndex, "Maná restaurado.", FontTypeNames.FONTTYPE_VENENO);
			WriteUpdateMana(UserIndex);
			return;
		}

		WriteMeditateToggle(UserIndex);

		if (Declaraciones.UserList[UserIndex].flags.Meditando) {
			WriteConsoleMsg(UserIndex, "Dejas de meditar.", FontTypeNames.FONTTYPE_INFO);
		}

		Declaraciones.UserList[UserIndex].flags.Meditando = !Declaraciones.UserList[UserIndex].flags.Meditando;

		/* 'Barrin 3/10/03 Tiempo de inicio al meditar */
		if (Declaraciones.UserList[UserIndex].flags.Meditando) {
			Declaraciones.UserList[UserIndex].Counters.tInicioMeditar = Declaraciones.GetTickCount() && 0x7FFFFFFF;

			/*
			 * 'Call WriteConsoleMsg(UserIndex, "Te estás concentrando. En " &
			 * Fix(TIEMPO_INICIOMEDITAR / 1000) &
			 * " segundos comenzarás a meditar.", FontTypeNames.FONTTYPE_INFO)
			 */
			/* ' [TEMPORAL] */
			WriteConsoleMsg(UserIndex, "Te estás concentrando. En "
					+ vb6.Int(Declaraciones.UserList[UserIndex].Stats.ELV / 17) + " segundos comenzarás a meditar.",
					FontTypeNames.FONTTYPE_INFO);

			Declaraciones.UserList[UserIndex].Char.loops = Declaraciones.INFINITE_LOOPS;

			/* 'Show proper FX according to level */
			if (Declaraciones.UserList[UserIndex].Stats.ELV < 13) {
				Declaraciones.UserList[UserIndex].Char.FX = FXIDs.FXMEDITARCHICO;

			} else if (Declaraciones.UserList[UserIndex].Stats.ELV < 25) {
				Declaraciones.UserList[UserIndex].Char.FX = FXIDs.FXMEDITARMEDIANO;

			} else if (Declaraciones.UserList[UserIndex].Stats.ELV < 35) {
				Declaraciones.UserList[UserIndex].Char.FX = FXIDs.FXMEDITARGRANDE;

			} else if (Declaraciones.UserList[UserIndex].Stats.ELV < 42) {
				Declaraciones.UserList[UserIndex].Char.FX = FXIDs.FXMEDITARXGRANDE;

			} else {
				Declaraciones.UserList[UserIndex].Char.FX = FXIDs.FXMEDITARXXGRANDE;
			}

			modSendData.SendData(SendTarget.ToPCArea, UserIndex,
					PrepareMessageCreateFX(Declaraciones.UserList[UserIndex].Char.CharIndex,
							Declaraciones.UserList[UserIndex].Char.FX, Declaraciones.INFINITE_LOOPS));
		} else {
			Declaraciones.UserList[UserIndex].Counters.bPuedeMeditar = false;

			Declaraciones.UserList[UserIndex].Char.FX = 0;
			Declaraciones.UserList[UserIndex].Char.loops = 0;
			modSendData.SendData(SendTarget.ToPCArea, UserIndex,
					PrepareMessageCreateFX(Declaraciones.UserList[UserIndex].Char.CharIndex, 0, 0));
		}
	}

	/* '' */
	/* ' Handles the "Resucitate" message. */
	/* ' */
	/* ' @param userIndex The index of the user sending the message. */

	static void HandleResucitate(int UserIndex) {
		/* '*************************************************** */
		/* 'Author: Juan Martín Sotuyo Dodero (Maraxus) */
		/* 'Last Modification: 05/17/06 */
		/* ' */
		/* '*************************************************** */
		/* FIXME: ON ERROR GOTO ErrHandler */
		/* 'Remove packet ID */
		Declaraciones.UserList[UserIndex].incomingData.ReadByte();

		/* 'Se asegura que el target es un npc */
		if (Declaraciones.UserList[UserIndex].flags.TargetNPC == 0) {
			WriteConsoleMsg(UserIndex, "Primero tienes que seleccionar un personaje, haz click izquierdo sobre él.",
					FontTypeNames.FONTTYPE_INFO);
			return;
		}

		/* 'Validate NPC and make sure player is dead */
		if ((Declaraciones.Npclist[Declaraciones.UserList[UserIndex].flags.TargetNPC].NPCtype != eNPCType.Revividor
				&& (Declaraciones.Npclist[Declaraciones.UserList[UserIndex].flags.TargetNPC].NPCtype != eNPCType.ResucitadorNewbie
						|| !Extra.EsNewbie(UserIndex)))
				|| Declaraciones.UserList[UserIndex].flags.Muerto == 0) {
			return;
		}

		/* 'Make sure it's close enough */
		if (Matematicas.Distancia(Declaraciones.UserList[UserIndex].Pos,
				Declaraciones.Npclist[Declaraciones.UserList[UserIndex].flags.TargetNPC].Pos) > 10) {
			WriteConsoleMsg(UserIndex, "El sacerdote no puede resucitarte debido a que estás demasiado lejos.",
					FontTypeNames.FONTTYPE_INFO);
			return;
		}

		UsUaRiOs.RevivirUsuario(UserIndex);
		WriteConsoleMsg(UserIndex, "¡¡Has sido resucitado!!", FontTypeNames.FONTTYPE_INFO);

		return;

		/* FIXME: ErrHandler : */

		General.LogError("Error en HandleResucitate. Error: " + Err.Number + " - " + Err.description + ". Usuario: "
				+ Declaraciones.UserList[UserIndex].Name + "(" + UserIndex + ")");
	}

	/* '' */
	/* ' Handles the "Consultation" message. */
	/* ' */
	/* ' @param userIndex The index of the user sending the message. */

	static void HandleConsultation(String UserIndex) {
		/* '*************************************************** */
		/* 'Author: ZaMa */
		/* 'Last Modification: 01/05/2010 */
		/* 'Habilita/Deshabilita el modo consulta. */
		/* '01/05/2010: ZaMa - Agrego validaciones. */
		/*
		 * '16/09/2010: ZaMa - No se hace visible en los clientes si estaba
		 * navegando (porque ya lo estaba).
		 */
		/* '*************************************************** */

		int UserConsulta;

		/* 'Remove packet ID */
		Declaraciones.UserList[UserIndex].incomingData.ReadByte();

		/* ' Comando exclusivo para gms */
		if (!Extra.EsGm(UserIndex)) {
			return;
		}

		UserConsulta = Declaraciones.UserList[UserIndex].flags.TargetUser;

		/* 'Se asegura que el target es un usuario */
		if (UserConsulta == 0) {
			WriteConsoleMsg(UserIndex, "Primero tienes que seleccionar un usuario, haz click izquierdo sobre él.",
					FontTypeNames.FONTTYPE_INFO);
			return;
		}

		/* ' No podes ponerte a vos mismo en modo consulta. */
		if (UserConsulta == UserIndex) {
			return;
		}

		/* ' No podes estra en consulta con otro gm */
		if (Extra.EsGm(UserConsulta)) {
			WriteConsoleMsg(UserIndex, "No puedes iniciar el modo consulta con otro administrador.",
					FontTypeNames.FONTTYPE_INFO);
			return;
		}

		String UserName;
		UserName = Declaraciones.UserList[UserConsulta].Name;

		/* ' Si ya estaba en consulta, termina la consulta */
		if (Declaraciones.UserList[UserConsulta].flags.EnConsulta) {
			WriteConsoleMsg(UserIndex, "Has terminado el modo consulta con " + UserName + ".",
					FontTypeNames.FONTTYPE_INFOBOLD);
			WriteConsoleMsg(UserConsulta, "Has terminado el modo consulta.", FontTypeNames.FONTTYPE_INFOBOLD);
			General.LogGM(Declaraciones.UserList[UserIndex].Name, "Termino consulta con " + UserName);

			Declaraciones.UserList[UserConsulta].flags.EnConsulta = false;

			/* ' Sino la inicia */
		} else {
			WriteConsoleMsg(UserIndex, "Has iniciado el modo consulta con " + UserName + ".",
					FontTypeNames.FONTTYPE_INFOBOLD);
			WriteConsoleMsg(UserConsulta, "Has iniciado el modo consulta.", FontTypeNames.FONTTYPE_INFOBOLD);
			General.LogGM(Declaraciones.UserList[UserIndex].Name, "Inicio consulta con " + UserName);

			Declaraciones.UserList[UserConsulta].flags.EnConsulta = true;

			/* ' Pierde invi u ocu */
			if (Declaraciones.UserList[UserConsulta].flags.invisible == 1
					|| Declaraciones.UserList[UserConsulta].flags.Oculto == 1) {
				Declaraciones.UserList[UserConsulta].flags.Oculto = 0;
				Declaraciones.UserList[UserConsulta].flags.invisible = 0;
				Declaraciones.UserList[UserConsulta].Counters.TiempoOculto = 0;
				Declaraciones.UserList[UserConsulta].Counters.Invisibilidad = 0;

				if (Declaraciones.UserList[UserConsulta].flags.Navegando == 0) {
					UsUaRiOs.SetInvisible(UserConsulta, Declaraciones.UserList[UserConsulta].Char.CharIndex, false);
				}
			}
		}

		UsUaRiOs.SetConsulatMode(UserConsulta);

	}

	/* '' */
	/* ' Handles the "Heal" message. */
	/* ' */
	/* ' @param userIndex The index of the user sending the message. */

	static void HandleHeal(int UserIndex) {
		/* '*************************************************** */
		/* 'Author: Juan Martín Sotuyo Dodero (Maraxus) */
		/* 'Last Modification: 05/17/06 */
		/* ' */
		/* '*************************************************** */
		/* 'Remove packet ID */
		Declaraciones.UserList[UserIndex].incomingData.ReadByte();

		/* 'Se asegura que el target es un npc */
		if (Declaraciones.UserList[UserIndex].flags.TargetNPC == 0) {
			WriteConsoleMsg(UserIndex, "Primero tienes que seleccionar un personaje, haz click izquierdo sobre él.",
					FontTypeNames.FONTTYPE_INFO);
			return;
		}

		if ((Declaraciones.Npclist[Declaraciones.UserList[UserIndex].flags.TargetNPC].NPCtype != eNPCType.Revividor
				&& Declaraciones.Npclist[Declaraciones.UserList[UserIndex].flags.TargetNPC].NPCtype != eNPCType.ResucitadorNewbie)
				|| Declaraciones.UserList[UserIndex].flags.Muerto != 0) {
			return;
		}

		if (Matematicas.Distancia(Declaraciones.UserList[UserIndex].Pos,
				Declaraciones.Npclist[Declaraciones.UserList[UserIndex].flags.TargetNPC].Pos) > 10) {
			WriteConsoleMsg(UserIndex, "El sacerdote no puede curarte debido a que estás demasiado lejos.",
					FontTypeNames.FONTTYPE_INFO);
			return;
		}

		Declaraciones.UserList[UserIndex].Stats.MinHp = Declaraciones.UserList[UserIndex].Stats.MaxHp;

		WriteUpdateHP(UserIndex);

		WriteConsoleMsg(UserIndex, "¡¡Has sido curado!!", FontTypeNames.FONTTYPE_INFO);
	}

	/* '' */
	/* ' Handles the "RequestStats" message. */
	/* ' */
	/* ' @param userIndex The index of the user sending the message. */

	static void HandleRequestStats(int UserIndex) {
		/* '*************************************************** */
		/* 'Author: Juan Martín Sotuyo Dodero (Maraxus) */
		/* 'Last Modification: 05/17/06 */
		/* ' */
		/* '*************************************************** */
		/* 'Remove packet ID */
		Declaraciones.UserList[UserIndex].incomingData.ReadByte();

		UsUaRiOs.SendUserStatsTxt(UserIndex, UserIndex);
	}

	/* '' */
	/* ' Handles the "Help" message. */
	/* ' */
	/* ' @param userIndex The index of the user sending the message. */

	static void HandleHelp(int UserIndex) {
		/* '*************************************************** */
		/* 'Author: Juan Martín Sotuyo Dodero (Maraxus) */
		/* 'Last Modification: 05/17/06 */
		/* ' */
		/* '*************************************************** */
		/* 'Remove packet ID */
		Declaraciones.UserList[UserIndex].incomingData.ReadByte();

		Extra.SendHelp(UserIndex);
	}

	/* '' */
	/* ' Handles the "CommerceStart" message. */
	/* ' */
	/* ' @param userIndex The index of the user sending the message. */

	static void HandleCommerceStart(int UserIndex) {
		/* '*************************************************** */
		/* 'Author: Juan Martín Sotuyo Dodero (Maraxus) */
		/* 'Last Modification: 05/17/06 */
		/* ' */
		/* '*************************************************** */
		int i;
		/* 'Remove packet ID */
		Declaraciones.UserList[UserIndex].incomingData.ReadByte();

		/* 'Dead people can't commerce */
		if (Declaraciones.UserList[UserIndex].flags.Muerto == 1) {
			WriteConsoleMsg(UserIndex, "¡¡Estás muerto!!", FontTypeNames.FONTTYPE_INFO);
			return;
		}

		/* 'Is it already in commerce mode?? */
		if (Declaraciones.UserList[UserIndex].flags.Comerciando) {
			WriteConsoleMsg(UserIndex, "Ya estás comerciando.", FontTypeNames.FONTTYPE_INFO);
			return;
		}

		/* 'Validate target NPC */
		if (Declaraciones.UserList[UserIndex].flags.TargetNPC > 0) {
			/* 'Does the NPC want to trade?? */
			if (Declaraciones.Npclist[Declaraciones.UserList[UserIndex].flags.TargetNPC].Comercia == 0) {
				if (vb6.LenB(Declaraciones.Npclist[Declaraciones.UserList[UserIndex].flags.TargetNPC].desc) != 0) {
					WriteChatOverHead(UserIndex, "No tengo ningún interés en comerciar.",
							Declaraciones.Npclist[Declaraciones.UserList[UserIndex].flags.TargetNPC].Char.CharIndex,
							0x00ffffff);
				}

				return;
			}

			if (Matematicas.Distancia(Declaraciones.Npclist[Declaraciones.UserList[UserIndex].flags.TargetNPC].Pos,
					Declaraciones.UserList[UserIndex].Pos) > 3) {
				WriteConsoleMsg(UserIndex, "Estás demasiado lejos del vendedor.", FontTypeNames.FONTTYPE_INFO);
				return;
			}

			/* 'Start commerce.... */
			modSistemaComercio.IniciarComercioNPC(UserIndex);
			/* '[Alejo] */
		} else if (Declaraciones.UserList[UserIndex].flags.TargetUser > 0) {
			/* 'User commerce... */
			/* 'Can he commerce?? */
			if (Declaraciones.UserList[UserIndex].flags.Privilegios && PlayerType.Consejero) {
				WriteConsoleMsg(UserIndex, "No puedes vender ítems.", FontTypeNames.FONTTYPE_WARNING);
				return;
			}

			/* 'Is the other one dead?? */
			if (Declaraciones.UserList[Declaraciones.UserList[UserIndex].flags.TargetUser].flags.Muerto == 1) {
				WriteConsoleMsg(UserIndex, "¡¡No puedes comerciar con los muertos!!", FontTypeNames.FONTTYPE_INFO);
				return;
			}

			/* 'Is it me?? */
			if (Declaraciones.UserList[UserIndex].flags.TargetUser == UserIndex) {
				WriteConsoleMsg(UserIndex, "¡¡No puedes comerciar con vos mismo!!", FontTypeNames.FONTTYPE_INFO);
				return;
			}

			/* 'Check distance */
			if (Matematicas.Distancia(Declaraciones.UserList[Declaraciones.UserList[UserIndex].flags.TargetUser].Pos,
					Declaraciones.UserList[UserIndex].Pos) > 3) {
				WriteConsoleMsg(UserIndex, "Estás demasiado lejos del usuario.", FontTypeNames.FONTTYPE_INFO);
				return;
			}

			/* 'Is he already trading?? is it with me or someone else?? */
			if (Declaraciones.UserList[Declaraciones.UserList[UserIndex].flags.TargetUser].flags.Comerciando == true
					&& Declaraciones.UserList[Declaraciones.UserList[UserIndex].flags.TargetUser].ComUsu.DestUsu != UserIndex) {
				WriteConsoleMsg(UserIndex, "No puedes comerciar con el usuario en este momento.",
						FontTypeNames.FONTTYPE_INFO);
				return;
			}

			/* 'Initialize some variables... */
			Declaraciones.UserList[UserIndex].ComUsu.DestUsu = Declaraciones.UserList[UserIndex].flags.TargetUser;
			Declaraciones.UserList[UserIndex].ComUsu.DestNick = Declaraciones.UserList[Declaraciones.UserList[UserIndex].flags.TargetUser].Name;
			for (i = (1); i <= (mdlCOmercioConUsuario.MAX_OFFER_SLOTS); i++) {
				Declaraciones.UserList[UserIndex].ComUsu.cant[i] = 0;
				Declaraciones.UserList[UserIndex].ComUsu.Objeto[i] = 0;
			}
			Declaraciones.UserList[UserIndex].ComUsu.GoldAmount = 0;

			Declaraciones.UserList[UserIndex].ComUsu.Acepto = false;
			Declaraciones.UserList[UserIndex].ComUsu.Confirmo = false;

			/* 'Rutina para comerciar con otro usuario */
			mdlCOmercioConUsuario.IniciarComercioConUsuario(UserIndex,
					Declaraciones.UserList[UserIndex].flags.TargetUser);
		} else {
			WriteConsoleMsg(UserIndex, "Primero haz click izquierdo sobre el personaje.", FontTypeNames.FONTTYPE_INFO);
		}
	}

	/* '' */
	/* ' Handles the "BankStart" message. */
	/* ' */
	/* ' @param userIndex The index of the user sending the message. */

	static void HandleBankStart(int UserIndex) {
		/* '*************************************************** */
		/* 'Author: Juan Martín Sotuyo Dodero (Maraxus) */
		/* 'Last Modification: 05/17/06 */
		/* ' */
		/* '*************************************************** */
		/* 'Remove packet ID */
		Declaraciones.UserList[UserIndex].incomingData.ReadByte();

		/* 'Dead people can't commerce */
		if (Declaraciones.UserList[UserIndex].flags.Muerto == 1) {
			WriteConsoleMsg(UserIndex, "¡¡Estás muerto!!", FontTypeNames.FONTTYPE_INFO);
			return;
		}

		if (Declaraciones.UserList[UserIndex].flags.Comerciando) {
			WriteConsoleMsg(UserIndex, "Ya estás comerciando.", FontTypeNames.FONTTYPE_INFO);
			return;
		}

		/* 'Validate target NPC */
		if (Declaraciones.UserList[UserIndex].flags.TargetNPC > 0) {
			if (Matematicas.Distancia(Declaraciones.Npclist[Declaraciones.UserList[UserIndex].flags.TargetNPC].Pos,
					Declaraciones.UserList[UserIndex].Pos) > 3) {
				WriteConsoleMsg(UserIndex, "Estás demasiado lejos del vendedor.", FontTypeNames.FONTTYPE_INFO);
				return;
			}

			/* 'If it's the banker.... */
			if (Declaraciones.Npclist[Declaraciones.UserList[UserIndex].flags.TargetNPC].NPCtype == eNPCType.Banquero) {
				modBanco.IniciarDeposito(UserIndex);
			}
		} else {
			WriteConsoleMsg(UserIndex, "Primero haz click izquierdo sobre el personaje.", FontTypeNames.FONTTYPE_INFO);
		}
	}

	/* '' */
	/* ' Handles the "Enlist" message. */
	/* ' */
	/* ' @param userIndex The index of the user sending the message. */

	static void HandleEnlist(int UserIndex) {
		/* '*************************************************** */
		/* 'Author: Juan Martín Sotuyo Dodero (Maraxus) */
		/* 'Last Modification: 05/17/06 */
		/* ' */
		/* '*************************************************** */
		/* 'Remove packet ID */
		Declaraciones.UserList[UserIndex].incomingData.ReadByte();

		/* 'Validate target NPC */
		if (Declaraciones.UserList[UserIndex].flags.TargetNPC == 0) {
			WriteConsoleMsg(UserIndex, "Primero tienes que seleccionar un personaje, haz click izquierdo sobre él.",
					FontTypeNames.FONTTYPE_INFO);
			return;
		}

		if (Declaraciones.Npclist[Declaraciones.UserList[UserIndex].flags.TargetNPC].NPCtype != eNPCType.Noble
				|| Declaraciones.UserList[UserIndex].flags.Muerto != 0) {
			return;
		}

		if (Matematicas.Distancia(Declaraciones.UserList[UserIndex].Pos,
				Declaraciones.Npclist[Declaraciones.UserList[UserIndex].flags.TargetNPC].Pos) > 4) {
			WriteConsoleMsg(UserIndex, "Debes acercarte más.", FontTypeNames.FONTTYPE_INFO);
			return;
		}

		if (Declaraciones.Npclist[Declaraciones.UserList[UserIndex].flags.TargetNPC].flags.Faccion == 0) {
			ModFacciones.EnlistarArmadaReal(UserIndex);
		} else {
			ModFacciones.EnlistarCaos(UserIndex);
		}
	}

	/* '' */
	/* ' Handles the "Information" message. */
	/* ' */
	/* ' @param userIndex The index of the user sending the message. */

	static void HandleInformation(int UserIndex) {
		/* '*************************************************** */
		/* 'Author: Juan Martín Sotuyo Dodero (Maraxus) */
		/* 'Last Modification: 05/17/06 */
		/* ' */
		/* '*************************************************** */
		int Matados;
		int NextRecom;
		int Diferencia;

		/* 'Remove packet ID */
		Declaraciones.UserList[UserIndex].incomingData.ReadByte();

		/* 'Validate target NPC */
		if (Declaraciones.UserList[UserIndex].flags.TargetNPC == 0) {
			WriteConsoleMsg(UserIndex, "Primero tienes que seleccionar un personaje, haz click izquierdo sobre él.",
					FontTypeNames.FONTTYPE_INFO);
			return;
		}

		if (Declaraciones.Npclist[Declaraciones.UserList[UserIndex].flags.TargetNPC].NPCtype != eNPCType.Noble
				|| Declaraciones.UserList[UserIndex].flags.Muerto != 0) {
			return;
		}

		if (Matematicas.Distancia(Declaraciones.UserList[UserIndex].Pos,
				Declaraciones.Npclist[Declaraciones.UserList[UserIndex].flags.TargetNPC].Pos) > 4) {
			WriteConsoleMsg(UserIndex, "Estás demasiado lejos.", FontTypeNames.FONTTYPE_INFO);
			return;
		}

		NextRecom = Declaraciones.UserList[UserIndex].Faccion.NextRecompensa;

		if (Declaraciones.Npclist[Declaraciones.UserList[UserIndex].flags.TargetNPC].flags.Faccion == 0) {
			if (Declaraciones.UserList[UserIndex].Faccion.ArmadaReal == 0) {
				WriteChatOverHead(UserIndex, "¡¡No perteneces a las tropas reales!!",
						Declaraciones.Npclist[Declaraciones.UserList[UserIndex].flags.TargetNPC].Char.CharIndex,
						0x00ffffff);
				return;
			}

			Matados = Declaraciones.UserList[UserIndex].Faccion.CriminalesMatados;
			Diferencia = NextRecom - Matados;

			if (Diferencia > 0) {
				WriteChatOverHead(UserIndex,
						"Tu deber es combatir criminales, mata " + Diferencia
								+ " criminales más y te daré una recompensa.",
						Declaraciones.Npclist[Declaraciones.UserList[UserIndex].flags.TargetNPC].Char.CharIndex,
						0x00ffffff);
			} else {
				WriteChatOverHead(UserIndex,
						"Tu deber es combatir criminales, y ya has matado los suficientes como para merecerte una recompensa.",
						Declaraciones.Npclist[Declaraciones.UserList[UserIndex].flags.TargetNPC].Char.CharIndex,
						0x00ffffff);
			}
		} else {
			if (Declaraciones.UserList[UserIndex].Faccion.FuerzasCaos == 0) {
				WriteChatOverHead(UserIndex, "¡¡No perteneces a la legión oscura!!",
						Declaraciones.Npclist[Declaraciones.UserList[UserIndex].flags.TargetNPC].Char.CharIndex,
						0x00ffffff);
				return;
			}

			Matados = Declaraciones.UserList[UserIndex].Faccion.CiudadanosMatados;
			Diferencia = NextRecom - Matados;

			if (Diferencia > 0) {
				WriteChatOverHead(UserIndex,
						"Tu deber es sembrar el caos y la desesperanza, mata " + Diferencia
								+ " ciudadanos más y te daré una recompensa.",
						Declaraciones.Npclist[Declaraciones.UserList[UserIndex].flags.TargetNPC].Char.CharIndex,
						0x00ffffff);
			} else {
				WriteChatOverHead(UserIndex,
						"Tu deber es sembrar el caos y la desesperanza, y creo que estás en condiciones de merecer una recompensa.",
						Declaraciones.Npclist[Declaraciones.UserList[UserIndex].flags.TargetNPC].Char.CharIndex,
						0x00ffffff);
			}
		}
	}

	/* '' */
	/* ' Handles the "Reward" message. */
	/* ' */
	/* ' @param userIndex The index of the user sending the message. */

	static void HandleReward(int UserIndex) {
		/* '*************************************************** */
		/* 'Author: Juan Martín Sotuyo Dodero (Maraxus) */
		/* 'Last Modification: 05/17/06 */
		/* ' */
		/* '*************************************************** */
		/* 'Remove packet ID */
		Declaraciones.UserList[UserIndex].incomingData.ReadByte();

		/* 'Validate target NPC */
		if (Declaraciones.UserList[UserIndex].flags.TargetNPC == 0) {
			WriteConsoleMsg(UserIndex, "Primero tienes que seleccionar un personaje, haz click izquierdo sobre él.",
					FontTypeNames.FONTTYPE_INFO);
			return;
		}

		if (Declaraciones.Npclist[Declaraciones.UserList[UserIndex].flags.TargetNPC].NPCtype != eNPCType.Noble
				|| Declaraciones.UserList[UserIndex].flags.Muerto != 0) {
			return;
		}

		if (Matematicas.Distancia(Declaraciones.UserList[UserIndex].Pos,
				Declaraciones.Npclist[Declaraciones.UserList[UserIndex].flags.TargetNPC].Pos) > 4) {
			WriteConsoleMsg(UserIndex, "Estás demasiado lejos.", FontTypeNames.FONTTYPE_INFO);
			return;
		}

		if (Declaraciones.Npclist[Declaraciones.UserList[UserIndex].flags.TargetNPC].flags.Faccion == 0) {
			if (Declaraciones.UserList[UserIndex].Faccion.ArmadaReal == 0) {
				WriteChatOverHead(UserIndex, "¡¡No perteneces a las tropas reales!!",
						Declaraciones.Npclist[Declaraciones.UserList[UserIndex].flags.TargetNPC].Char.CharIndex,
						0x00ffffff);
				return;
			}
			ModFacciones.RecompensaArmadaReal(UserIndex);
		} else {
			if (Declaraciones.UserList[UserIndex].Faccion.FuerzasCaos == 0) {
				WriteChatOverHead(UserIndex, "¡¡No perteneces a la legión oscura!!",
						Declaraciones.Npclist[Declaraciones.UserList[UserIndex].flags.TargetNPC].Char.CharIndex,
						0x00ffffff);
				return;
			}
			ModFacciones.RecompensaCaos(UserIndex);
		}
	}

	/* '' */
	/* ' Handles the "RequestMOTD" message. */
	/* ' */
	/* ' @param userIndex The index of the user sending the message. */

	static void HandleRequestMOTD(int UserIndex) {
		/* '*************************************************** */
		/* 'Author: Juan Martín Sotuyo Dodero (Maraxus) */
		/* 'Last Modification: 05/17/06 */
		/* ' */
		/* '*************************************************** */
		/* 'Remove packet ID */
		Declaraciones.UserList[UserIndex].incomingData.ReadByte();

		TCP.SendMOTD(UserIndex);
	}

	/* '' */
	/* ' Handles the "UpTime" message. */
	/* ' */
	/* ' @param userIndex The index of the user sending the message. */

	static void HandleUpTime(int UserIndex) {
		/* '*************************************************** */
		/* 'Author: Juan Martín Sotuyo Dodero (Maraxus) */
		/* 'Last Modification: 01/10/08 */
		/*
		 * '01/10/2008 - Marcos Martinez (ByVal) - Automatic restart removed
		 * from the server along with all their assignments and varibles
		 */
		/* '*************************************************** */
		/* 'Remove packet ID */
		Declaraciones.UserList[UserIndex].incomingData.ReadByte();

		int time;
		String UpTimeStr;

		/* 'Get total time in seconds */
		time = modNuevoTimer.getInterval((Declaraciones.GetTickCount() && 0x7FFFFFFF), Admin.tInicioServer) / 1000;

		/* 'Get times in dd:hh:mm:ss format */
		UpTimeStr = (time % 60) + " segundos.";
		time = time / 60;

		UpTimeStr = (time % 60) + " minutos, " + UpTimeStr;
		time = time / 60;

		UpTimeStr = (time % 24) + " horas, " + UpTimeStr;
		time = time / 24;

		if (time == 1) {
			UpTimeStr = time + " día, " + UpTimeStr;
		} else {
			UpTimeStr = time + " días, " + UpTimeStr;
		}

		WriteConsoleMsg(UserIndex, "Server Online: " + UpTimeStr, FontTypeNames.FONTTYPE_INFO);
	}

	/* '' */
	/* ' Handles the "PartyLeave" message. */
	/* ' */
	/* ' @param userIndex The index of the user sending the message. */

	static void HandlePartyLeave(int UserIndex) {
		/* '*************************************************** */
		/* 'Author: Juan Martín Sotuyo Dodero (Maraxus) */
		/* 'Last Modification: 05/17/06 */
		/* ' */
		/* '*************************************************** */
		/* 'Remove packet ID */
		Declaraciones.UserList[UserIndex].incomingData.ReadByte();

		mdParty.SalirDeParty(UserIndex);
	}

	/* '' */
	/* ' Handles the "PartyCreate" message. */
	/* ' */
	/* ' @param userIndex The index of the user sending the message. */

	static void HandlePartyCreate(int UserIndex) {
		/* '*************************************************** */
		/* 'Author: Juan Martín Sotuyo Dodero (Maraxus) */
		/* 'Last Modification: 05/17/06 */
		/* ' */
		/* '*************************************************** */
		/* 'Remove packet ID */
		Declaraciones.UserList[UserIndex].incomingData.ReadByte();

		if (!mdParty.PuedeCrearParty(UserIndex)) {
			return;
		}

		mdParty.CrearParty(UserIndex);
	}

	/* '' */
	/* ' Handles the "PartyJoin" message. */
	/* ' */
	/* ' @param userIndex The index of the user sending the message. */

	static void HandlePartyJoin(int UserIndex) {
		/* '*************************************************** */
		/* 'Author: Juan Martín Sotuyo Dodero (Maraxus) */
		/* 'Last Modification: 05/17/06 */
		/* ' */
		/* '*************************************************** */
		/* 'Remove packet ID */
		Declaraciones.UserList[UserIndex].incomingData.ReadByte();

		mdParty.SolicitarIngresoAParty(UserIndex);
	}

	/* '' */
	/* ' Handles the "ShareNpc" message. */
	/* ' */
	/* ' @param userIndex The index of the user sending the message. */

	static void HandleShareNpc(int UserIndex) {
		/* '*************************************************** */
		/* 'Author: ZaMa */
		/* 'Last Modification: 15/04/2010 */
		/* 'Shares owned npcs with other user */
		/* '*************************************************** */

		int TargetUserIndex;
		int SharingUserIndex;

		/* 'Remove packet ID */
		Declaraciones.UserList[UserIndex].incomingData.ReadByte();

		/* ' Didn't target any user */
		TargetUserIndex = Declaraciones.UserList[UserIndex].flags.TargetUser;
		if (TargetUserIndex == 0) {
			return;
		}

		/* ' Can't share with admins */
		if (Extra.EsGm(TargetUserIndex)) {
			WriteConsoleMsg(UserIndex, "No puedes compartir npcs con administradores!!", FontTypeNames.FONTTYPE_INFO);
			return;
		}

		/* ' Pk or Caos? */
		if (ES.criminal(UserIndex)) {
			/* ' Caos can only share with other caos */
			if (Extra.esCaos(UserIndex)) {
				if (!Extra.esCaos(TargetUserIndex)) {
					WriteConsoleMsg(UserIndex, "Solo puedes compartir npcs con miembros de tu misma facción!!",
							FontTypeNames.FONTTYPE_INFO);
					return;
				}

				/* ' Pks don't need to share with anyone */
			} else {
				return;
			}

			/* ' Ciuda or Army? */
		} else {
			/* ' Can't share */
			if (ES.criminal(TargetUserIndex)) {
				WriteConsoleMsg(UserIndex, "No puedes compartir npcs con criminales!!", FontTypeNames.FONTTYPE_INFO);
				return;
			}
		}

		/* ' Already sharing with target */
		SharingUserIndex = Declaraciones.UserList[UserIndex].flags.ShareNpcWith;
		if (SharingUserIndex == TargetUserIndex) {
			return;
		}

		/* ' Aviso al usuario anterior que dejo de compartir */
		if (SharingUserIndex != 0) {
			WriteConsoleMsg(SharingUserIndex,
					Declaraciones.UserList[UserIndex].Name + " ha dejado de compartir sus npcs contigo.",
					FontTypeNames.FONTTYPE_INFO);
			WriteConsoleMsg(UserIndex,
					"Has dejado de compartir tus npcs con " + Declaraciones.UserList[SharingUserIndex].Name + ".",
					FontTypeNames.FONTTYPE_INFO);
		}

		Declaraciones.UserList[UserIndex].flags.ShareNpcWith = TargetUserIndex;

		WriteConsoleMsg(TargetUserIndex, Declaraciones.UserList[UserIndex].Name + " ahora comparte sus npcs contigo.",
				FontTypeNames.FONTTYPE_INFO);
		WriteConsoleMsg(UserIndex, "Ahora compartes tus npcs con " + Declaraciones.UserList[TargetUserIndex].Name + ".",
				FontTypeNames.FONTTYPE_INFO);

	}

	/* '' */
	/* ' Handles the "StopSharingNpc" message. */
	/* ' */
	/* ' @param userIndex The index of the user sending the message. */

	static void HandleStopSharingNpc(int UserIndex) {
		/* '*************************************************** */
		/* 'Author: ZaMa */
		/* 'Last Modification: 15/04/2010 */
		/* 'Stop Sharing owned npcs with other user */
		/* '*************************************************** */

		int SharingUserIndex;

		/* 'Remove packet ID */
		Declaraciones.UserList[UserIndex].incomingData.ReadByte();

		SharingUserIndex = Declaraciones.UserList[UserIndex].flags.ShareNpcWith;

		if (SharingUserIndex != 0) {

			/* ' Aviso al que compartia y al que le compartia. */
			WriteConsoleMsg(SharingUserIndex,
					Declaraciones.UserList[UserIndex].Name + " ha dejado de compartir sus npcs contigo.",
					FontTypeNames.FONTTYPE_INFO);
			WriteConsoleMsg(SharingUserIndex,
					"Has dejado de compartir tus npcs con " + Declaraciones.UserList[SharingUserIndex].Name + ".",
					FontTypeNames.FONTTYPE_INFO);

			Declaraciones.UserList[UserIndex].flags.ShareNpcWith = 0;
		}

	}

	/* '' */
	/* ' Handles the "Inquiry" message. */
	/* ' */
	/* ' @param userIndex The index of the user sending the message. */

	static void HandleInquiry(int UserIndex) {
		/* '*************************************************** */
		/* 'Author: Juan Martín Sotuyo Dodero (Maraxus) */
		/* 'Last Modification: 05/17/06 */
		/* ' */
		/* '*************************************************** */
		/* 'Remove packet ID */
		Declaraciones.UserList[UserIndex].incomingData.ReadByte();

		Declaraciones.ConsultaPopular.SendInfoEncuesta(UserIndex);
	}

	/* '' */
	/* ' Handles the "GuildMessage" message. */
	/* ' */
	/* ' @param userIndex The index of the user sending the message. */

	static void HandleGuildMessage(int UserIndex) {
		/* '*************************************************** */
		/* 'Author: Juan Martín Sotuyo Dodero (Maraxus) */
		/* 'Last Modification: 15/07/2009 */
		/*
		 * '02/03/2009: ZaMa - Arreglado un indice mal pasado a la funcion de
		 * cartel de clanes overhead.
		 */
		/* '15/07/2009: ZaMa - Now invisible admins only speak by console */
		/* '*************************************************** */
		if (Declaraciones.UserList[UserIndex].incomingData.length < 3) {
			throw new RuntimeException("Err . Raise UserList ( UserIndex ) . incomingData . NotEnoughDataErrCode");
			return;
		}

		/* FIXME: ON ERROR GOTO ErrHandler */
		/*
		 * 'This packet contains strings, make a copy of the data to prevent
		 * losses if it's not complete yet...
		 */
		clsByteQueue buffer;
		buffer = new clsByteQueue();
		buffer.CopyBuffer(Declaraciones.UserList[UserIndex].incomingData);

		/* 'Remove packet ID */
		buffer.ReadByte();

		String Chat;

		Chat = buffer.ReadASCIIString();

		if (vb6.LenB(Chat) != 0) {
			/* 'Analize chat... */
			Statistics.ParseChat(Chat);

			if (Declaraciones.UserList[UserIndex].GuildIndex > 0) {
				modSendData.SendData(SendTarget.ToDiosesYclan, Declaraciones.UserList[UserIndex].GuildIndex,
						PrepareMessageGuildChat(Declaraciones.UserList[UserIndex].Name + "> " + Chat));

				if (!(Declaraciones.UserList[UserIndex].flags.AdminInvisible == 1)) {
					modSendData.SendData(SendTarget.ToClanArea, UserIndex, PrepareMessageChatOverHead(
							"< " + Chat + " >", Declaraciones.UserList[UserIndex].Char.CharIndex, vbYellow));
				}
			}
		}

		/*
		 * 'If we got here then packet is complete, copy data back to original
		 * queue
		 */
		Declaraciones.UserList[UserIndex].incomingData.CopyBuffer(buffer);

		/* FIXME: ErrHandler : */
		int ERROR;
		/* ERROR = Err . Number */
		/* FIXME: ON ERROR GOTO 0 */

		/* 'Destroy auxiliar buffer */
		buffer = null;

		if (ERROR != 0) {
			throw new RuntimeException("Err . Raise ERROR");
		}
	}

	/* '' */
	/* ' Handles the "PartyMessage" message. */
	/* ' */
	/* ' @param userIndex The index of the user sending the message. */

	static void HandlePartyMessage(int UserIndex) {
		/* '*************************************************** */
		/* 'Author: Juan Martín Sotuyo Dodero (Maraxus) */
		/* 'Last Modification: 05/17/06 */
		/* ' */
		/* '*************************************************** */
		if (Declaraciones.UserList[UserIndex].incomingData.length < 3) {
			throw new RuntimeException("Err . Raise UserList ( UserIndex ) . incomingData . NotEnoughDataErrCode");
			return;
		}

		/* FIXME: ON ERROR GOTO ErrHandler */
		/*
		 * 'This packet contains strings, make a copy of the data to prevent
		 * losses if it's not complete yet...
		 */
		clsByteQueue buffer;
		buffer = new clsByteQueue();
		buffer.CopyBuffer(Declaraciones.UserList[UserIndex].incomingData);

		/* 'Remove packet ID */
		buffer.ReadByte();

		String Chat;

		Chat = buffer.ReadASCIIString();

		if (vb6.LenB(Chat) != 0) {
			/* 'Analize chat... */
			Statistics.ParseChat(Chat);

			mdParty.BroadCastParty(UserIndex, Chat);
			/*
			 * 'TODO : Con la 0.12.1 se debe definir si esto vuelve o se borra
			 * (/CMSG overhead)
			 */
			/*
			 * 'Call SendData(SendTarget.ToPartyArea, UserIndex,
			 * UserList(UserIndex).Pos.map, "||" & vbYellow & "°< " &
			 * mid$(rData, 7) & " >°" &
			 * CStr(UserList(UserIndex).Char.CharIndex))
			 */
		}

		/*
		 * 'If we got here then packet is complete, copy data back to original
		 * queue
		 */
		Declaraciones.UserList[UserIndex].incomingData.CopyBuffer(buffer);

		/* FIXME: ErrHandler : */
		int ERROR;
		/* ERROR = Err . Number */
		/* FIXME: ON ERROR GOTO 0 */

		/* 'Destroy auxiliar buffer */
		buffer = null;

		if (ERROR != 0) {
			throw new RuntimeException("Err . Raise ERROR");
		}
	}

	/* '' */
	/* ' Handles the "CentinelReport" message. */
	/* ' */
	/* ' @param userIndex The index of the user sending the message. */

	static void HandleCentinelReport(int UserIndex) {
		/* '*************************************************** */
		/* 'Author: Juan Martín Sotuyo Dodero (Maraxus) */
		/* 'Last Modification: 05/17/06 */
		/* ' */
		/* '*************************************************** */
		if (Declaraciones.UserList[UserIndex].incomingData.length < 3) {
			throw new RuntimeException("Err . Raise UserList ( UserIndex ) . incomingData . NotEnoughDataErrCode");
			return;
		}

		/* 'Remove packet ID */
		Declaraciones.UserList[UserIndex].incomingData.ReadByte();

		modCentinela.CentinelaCheckClave(UserIndex, Declaraciones.UserList[UserIndex].incomingData.ReadInteger());
	}

	/* '' */
	/* ' Handles the "GuildOnline" message. */
	/* ' */
	/* ' @param userIndex The index of the user sending the message. */

	static void HandleGuildOnline(int UserIndex) {
		/* '*************************************************** */
		/* 'Author: Juan Martín Sotuyo Dodero (Maraxus) */
		/* 'Last Modification: 05/17/06 */
		/* ' */
		/* '*************************************************** */
		/* 'Remove packet ID */
		Declaraciones.UserList[UserIndex].incomingData.ReadByte();

		String onlineList;

		onlineList = modGuilds.m_ListaDeMiembrosOnline(UserIndex, Declaraciones.UserList[UserIndex].GuildIndex);

		if (Declaraciones.UserList[UserIndex].GuildIndex != 0) {
			WriteConsoleMsg(UserIndex, "Companeros de tu clan conectados: " + onlineList,
					FontTypeNames.FONTTYPE_GUILDMSG);
		} else {
			WriteConsoleMsg(UserIndex, "No pertences a ningún clan.", FontTypeNames.FONTTYPE_GUILDMSG);
		}
	}

	/* '' */
	/* ' Handles the "PartyOnline" message. */
	/* ' */
	/* ' @param userIndex The index of the user sending the message. */

	static void HandlePartyOnline(int UserIndex) {
		/* '*************************************************** */
		/* 'Author: Juan Martín Sotuyo Dodero (Maraxus) */
		/* 'Last Modification: 05/17/06 */
		/* ' */
		/* '*************************************************** */
		/* 'Remove packet ID */
		Declaraciones.UserList[UserIndex].incomingData.ReadByte();

		mdParty.OnlineParty(UserIndex);
	}

	/* '' */
	/* ' Handles the "CouncilMessage" message. */
	/* ' */
	/* ' @param userIndex The index of the user sending the message. */

	static void HandleCouncilMessage(int UserIndex) {
		/* '*************************************************** */
		/* 'Author: Juan Martín Sotuyo Dodero (Maraxus) */
		/* 'Last Modification: 05/17/06 */
		/* ' */
		/* '*************************************************** */
		if (Declaraciones.UserList[UserIndex].incomingData.length < 3) {
			throw new RuntimeException("Err . Raise UserList ( UserIndex ) . incomingData . NotEnoughDataErrCode");
			return;
		}

		/* FIXME: ON ERROR GOTO ErrHandler */
		/*
		 * 'This packet contains strings, make a copy of the data to prevent
		 * losses if it's not complete yet...
		 */
		clsByteQueue buffer;
		buffer = new clsByteQueue();
		buffer.CopyBuffer(Declaraciones.UserList[UserIndex].incomingData);

		/* 'Remove packet ID */
		buffer.ReadByte();

		String Chat;

		Chat = buffer.ReadASCIIString();

		if (vb6.LenB(Chat) != 0) {
			/* 'Analize chat... */
			Statistics.ParseChat(Chat);

			if (Declaraciones.UserList[UserIndex].flags.Privilegios && PlayerType.RoyalCouncil) {
				modSendData.SendData(SendTarget.ToConsejo, UserIndex,
						PrepareMessageConsoleMsg("(Consejero) " + Declaraciones.UserList[UserIndex].Name + "> " + Chat,
								FontTypeNames.FONTTYPE_CONSEJO));
			} else if (Declaraciones.UserList[UserIndex].flags.Privilegios && PlayerType.ChaosCouncil) {
				modSendData.SendData(SendTarget.ToConsejoCaos, UserIndex,
						PrepareMessageConsoleMsg("(Consejero) " + Declaraciones.UserList[UserIndex].Name + "> " + Chat,
								FontTypeNames.FONTTYPE_CONSEJOCAOS));
			}
		}

		/*
		 * 'If we got here then packet is complete, copy data back to original
		 * queue
		 */
		Declaraciones.UserList[UserIndex].incomingData.CopyBuffer(buffer);

		/* FIXME: ErrHandler : */
		int ERROR;
		/* ERROR = Err . Number */
		/* FIXME: ON ERROR GOTO 0 */

		/* 'Destroy auxiliar buffer */
		buffer = null;

		if (ERROR != 0) {
			throw new RuntimeException("Err . Raise ERROR");
		}
	}

	/* '' */
	/* ' Handles the "RoleMasterRequest" message. */
	/* ' */
	/* ' @param userIndex The index of the user sending the message. */

	static void HandleRoleMasterRequest(int UserIndex) {
		/* '*************************************************** */
		/* 'Author: Juan Martín Sotuyo Dodero (Maraxus) */
		/* 'Last Modification: 05/17/06 */
		/* ' */
		/* '*************************************************** */
		if (Declaraciones.UserList[UserIndex].incomingData.length < 3) {
			throw new RuntimeException("Err . Raise UserList ( UserIndex ) . incomingData . NotEnoughDataErrCode");
			return;
		}

		/* FIXME: ON ERROR GOTO ErrHandler */
		/*
		 * 'This packet contains strings, make a copy of the data to prevent
		 * losses if it's not complete yet...
		 */
		clsByteQueue buffer;
		buffer = new clsByteQueue();
		buffer.CopyBuffer(Declaraciones.UserList[UserIndex].incomingData);

		/* 'Remove packet ID */
		buffer.ReadByte();

		String request;

		request = buffer.ReadASCIIString();

		if (vb6.LenB(request) != 0) {
			WriteConsoleMsg(UserIndex, "Su solicitud ha sido enviada.", FontTypeNames.FONTTYPE_INFO);
			modSendData.SendData(SendTarget.ToRMsAndHigherAdmins, 0,
					PrepareMessageConsoleMsg(Declaraciones.UserList[UserIndex].Name + " PREGUNTA ROL: " + request,
							FontTypeNames.FONTTYPE_GUILDMSG));
		}

		/*
		 * 'If we got here then packet is complete, copy data back to original
		 * queue
		 */
		Declaraciones.UserList[UserIndex].incomingData.CopyBuffer(buffer);

		/* FIXME: ErrHandler : */
		int ERROR;
		/* ERROR = Err . Number */
		/* FIXME: ON ERROR GOTO 0 */

		/* 'Destroy auxiliar buffer */
		buffer = null;

		if (ERROR != 0) {
			throw new RuntimeException("Err . Raise ERROR");
		}
	}

	/* '' */
	/* ' Handles the "GMRequest" message. */
	/* ' */
	/* ' @param userIndex The index of the user sending the message. */

	static void HandleGMRequest(int UserIndex) {
		/* '*************************************************** */
		/* 'Author: Juan Martín Sotuyo Dodero (Maraxus) */
		/* 'Last Modification: 05/17/06 */
		/* ' */
		/* '*************************************************** */
		/* 'Remove packet ID */
		Declaraciones.UserList[UserIndex].incomingData.ReadByte();

		if (!Declaraciones.Ayuda.Existe(Declaraciones.UserList[UserIndex].Name)) {
			WriteConsoleMsg(UserIndex,
					"El mensaje ha sido entregado, ahora sólo debes esperar que se desocupe algún GM.",
					FontTypeNames.FONTTYPE_INFO);
			Declaraciones.Ayuda.Push(Declaraciones.UserList[UserIndex].Name);
		} else {
			Declaraciones.Ayuda.Quitar(Declaraciones.UserList[UserIndex].Name);
			Declaraciones.Ayuda.Push(Declaraciones.UserList[UserIndex].Name);
			WriteConsoleMsg(UserIndex,
					"Ya habías mandado un mensaje, tu mensaje ha sido movido al final de la cola de mensajes.",
					FontTypeNames.FONTTYPE_INFO);
		}
	}

	/* '' */
	/* ' Handles the "BugReport" message. */
	/* ' */
	/* ' @param userIndex The index of the user sending the message. */

	static void HandleBugReport(int UserIndex) {
		/* '*************************************************** */
		/* 'Author: Juan Martín Sotuyo Dodero (Maraxus) */
		/* 'Last Modification: 05/17/06 */
		/* ' */
		/* '*************************************************** */
		if (Declaraciones.UserList[UserIndex].incomingData.length < 3) {
			throw new RuntimeException("Err . Raise UserList ( UserIndex ) . incomingData . NotEnoughDataErrCode");
			return;
		}

		/* FIXME: ON ERROR GOTO ErrHandler */
		/*
		 * 'This packet contains strings, make a copy of the data to prevent
		 * losses if it's not complete yet...
		 */
		clsByteQueue buffer;
		buffer = new clsByteQueue();
		int N;

		buffer.CopyBuffer(Declaraciones.UserList[UserIndex].incomingData);

		/* 'Remove packet ID */
		buffer.ReadByte();

		String bugReport;

		bugReport = buffer.ReadASCIIString();

		N = vb6.FreeFile();
		/* FIXME: OPEN App . Path & "\\LOGS\\BUGs.log" FOR Append Shared AS N */
		/*
		 * FIXME: PRINT # N , "Usuario:" & . Name & "  Fecha:" & Date &
		 * "    Hora:" & time
		 */
		/* FIXME: PRINT # N , "BUG:" */
		/* FIXME: PRINT # N , bugReport */
		/*
		 * FIXME: PRINT # N ,
		 * "########################################################################"
		 */
		/* FIXME: CLOSE # N */

		/*
		 * 'If we got here then packet is complete, copy data back to original
		 * queue
		 */
		Declaraciones.UserList[UserIndex].incomingData.CopyBuffer(buffer);

		/* FIXME: ErrHandler : */
		int ERROR;
		/* ERROR = Err . Number */
		/* FIXME: ON ERROR GOTO 0 */

		/* 'Destroy auxiliar buffer */
		buffer = null;

		if (ERROR != 0) {
			throw new RuntimeException("Err . Raise ERROR");
		}
	}

	/* '' */
	/* ' Handles the "ChangeDescription" message. */
	/* ' */
	/* ' @param userIndex The index of the user sending the message. */

	static void HandleChangeDescription(int UserIndex) {
		/* '*************************************************** */
		/* 'Author: Juan Martín Sotuyo Dodero (Maraxus) */
		/* 'Last Modification: 05/17/06 */
		/* ' */
		/* '*************************************************** */
		if (Declaraciones.UserList[UserIndex].incomingData.length < 3) {
			throw new RuntimeException("Err . Raise UserList ( UserIndex ) . incomingData . NotEnoughDataErrCode");
			return;
		}

		/* FIXME: ON ERROR GOTO ErrHandler */
		/*
		 * 'This packet contains strings, make a copy of the data to prevent
		 * losses if it's not complete yet...
		 */
		clsByteQueue buffer;
		buffer = new clsByteQueue();
		buffer.CopyBuffer(Declaraciones.UserList[UserIndex].incomingData);

		/* 'Remove packet ID */
		buffer.ReadByte();

		String description;

		description = buffer.ReadASCIIString();

		if (Declaraciones.UserList[UserIndex].flags.Muerto == 1) {
			WriteConsoleMsg(UserIndex, "No puedes cambiar la descripción estando muerto.", FontTypeNames.FONTTYPE_INFO);
		} else {
			if (!TCP.AsciiValidos(description)) {
				WriteConsoleMsg(UserIndex, "La descripción tiene caracteres inválidos.", FontTypeNames.FONTTYPE_INFO);
			} else {
				Declaraciones.UserList[UserIndex].desc = vb6.Trim(description);
				WriteConsoleMsg(UserIndex, "La descripción ha cambiado.", FontTypeNames.FONTTYPE_INFO);
			}
		}

		/*
		 * 'If we got here then packet is complete, copy data back to original
		 * queue
		 */
		Declaraciones.UserList[UserIndex].incomingData.CopyBuffer(buffer);

		/* FIXME: ErrHandler : */
		int ERROR;
		/* ERROR = Err . Number */
		/* FIXME: ON ERROR GOTO 0 */

		/* 'Destroy auxiliar buffer */
		buffer = null;

		if (ERROR != 0) {
			throw new RuntimeException("Err . Raise ERROR");
		}
	}

	/* '' */
	/* ' Handles the "GuildVote" message. */
	/* ' */
	/* ' @param userIndex The index of the user sending the message. */

	static void HandleGuildVote(int UserIndex) {
		/* '*************************************************** */
		/* 'Author: Juan Martín Sotuyo Dodero (Maraxus) */
		/* 'Last Modification: 05/17/06 */
		/* ' */
		/* '*************************************************** */
		if (Declaraciones.UserList[UserIndex].incomingData.length < 3) {
			throw new RuntimeException("Err . Raise UserList ( UserIndex ) . incomingData . NotEnoughDataErrCode");
			return;
		}

		/* FIXME: ON ERROR GOTO ErrHandler */
		/*
		 * 'This packet contains strings, make a copy of the data to prevent
		 * losses if it's not complete yet...
		 */
		clsByteQueue buffer;
		buffer = new clsByteQueue();
		buffer.CopyBuffer(Declaraciones.UserList[UserIndex].incomingData);

		/* 'Remove packet ID */
		buffer.ReadByte();

		String vote;
		String errorStr;

		vote = buffer.ReadASCIIString();

		if (!modGuilds.v_UsuarioVota(UserIndex, vote, errorStr)) {
			WriteConsoleMsg(UserIndex, "Voto NO contabilizado: " + errorStr, FontTypeNames.FONTTYPE_GUILD);
		} else {
			WriteConsoleMsg(UserIndex, "Voto contabilizado.", FontTypeNames.FONTTYPE_GUILD);
		}

		/*
		 * 'If we got here then packet is complete, copy data back to original
		 * queue
		 */
		Declaraciones.UserList[UserIndex].incomingData.CopyBuffer(buffer);

		/* FIXME: ErrHandler : */
		int ERROR;
		/* ERROR = Err . Number */
		/* FIXME: ON ERROR GOTO 0 */

		/* 'Destroy auxiliar buffer */
		buffer = null;

		if (ERROR != 0) {
			throw new RuntimeException("Err . Raise ERROR");
		}
	}

	/* '' */
	/* ' Handles the "ShowGuildNews" message. */
	/* ' */
	/* ' @param userIndex The index of the user sending the message. */

	static void HandleShowGuildNews(int UserIndex) {
		/* '*************************************************** */
		/* 'Author: ZaMA */
		/* 'Last Modification: 05/17/06 */
		/* ' */
		/* '*************************************************** */

		/* 'Remove packet ID */
		Declaraciones.UserList[UserIndex].incomingData.ReadByte();

		modGuilds.SendGuildNews(UserIndex);
	}

	/* '' */
	/* ' Handles the "Punishments" message. */
	/* ' */
	/* ' @param userIndex The index of the user sending the message. */

	static void HandlePunishments(int UserIndex) {
 /* '*************************************************** */
 /* 'Author: Juan Martín Sotuyo Dodero (Maraxus) */
 /* 'Last Modification: 25/08/2009 */
 /* '25/08/2009: ZaMa - Now only admins can see other admins' punishment list */
 /* '*************************************************** */
  if (Declaraciones.UserList[UserIndex].incomingData.length<3) {
  throw new RuntimeException("Err . Raise UserList ( UserIndex ) . incomingData . NotEnoughDataErrCode");
  return;
 }
 
 /* FIXME: ON ERROR GOTO ErrHandler */
  /* 'This packet contains strings, make a copy of the data to prevent losses if it's not complete yet... */
  clsByteQueue buffer;
  buffer = new clsByteQueue();
  buffer.CopyBuffer(Declaraciones.UserList[UserIndex].incomingData);
  
  /* 'Remove packet ID */
  buffer.ReadByte();
  
  String Name;
  int Count;
  
  Name = buffer.ReadASCIIString();
  
   if (vb6.LenB(Name) != 0) {
    if ((vb6.InStrB(Name, "\\") != 0)) {
    Name = vb6.Replace(Name, "\\", "");
   }
    if ((vb6.InStrB(Name, "/") != 0)) {
    Name = vb6.Replace(Name, "/", "");
   }
    if ((vb6.InStrB(Name, ":") != 0)) {
    Name = vb6.Replace(Name, ":", "");
   }
    if ((vb6.InStrB(Name, "|") != 0)) {
    Name = vb6.Replace(Name, "|", "");
   }
   
    if ((ES.EsAdmin(Name) || ES.EsDios(Name) || ES.EsSemiDios(Name) || ES.EsConsejero(Name) || ES.EsRolesMaster(Name)) && (Declaraciones.UserList[UserIndex].flags.Privilegios && PlayerType.User)) {
    WriteConsoleMsg(UserIndex, "No puedes ver las penas de los administradores.", FontTypeNames.FONTTYPE_INFO);
    } else {
     if (General.FileExist(Declaraciones.CharPath + Name + ".chr", 0)) {
     Count = vb6.val(ES.GetVar(Declaraciones.CharPath + Name + ".chr", "PENAS", "Cant"));
      if (Count == 0) {
      WriteConsoleMsg(UserIndex, "Sin prontuario..", FontTypeNames.FONTTYPE_INFO);
      } else {
       while (Count>0) {
       WriteConsoleMsg(UserIndex, Count + " - " + ES.GetVar(Declaraciones.CharPath + Name + ".chr", "PENAS", "P" + Count), FontTypeNames.FONTTYPE_INFO);
       Count = Count-1;
      }
     }
     } else {
     WriteConsoleMsg(UserIndex, "Personaje """ + Name + """ inexistente.", FontTypeNames.FONTTYPE_INFO);
    }
   }
  }
  
  /* 'If we got here then packet is complete, copy data back to original queue */
  Declaraciones.UserList[UserIndex].incomingData.CopyBuffer(buffer);
 
 /* FIXME: ErrHandler : */
 int ERROR;
 /* ERROR = Err . Number */
 /* FIXME: ON ERROR GOTO 0 */
 
 /* 'Destroy auxiliar buffer */
 buffer = null;
 
 if (ERROR != 0) {
 throw new RuntimeException("Err . Raise ERROR");
 }
}

	/* '' */
	/* ' Handles the "ChangePassword" message. */
	/* ' */
	/* ' @param userIndex The index of the user sending the message. */

	static void HandleChangePassword(int UserIndex) {
		/* '*************************************************** */
		/* 'Author: Juan Martín Sotuyo Dodero (Maraxus) */
		/* 'Creation Date: 10/10/07 */
		/* 'Last Modified By: Rapsodius */
		/* '*************************************************** */
		/* # IF SeguridadAlkon THEN */
		/* # ELSE */
		if (Declaraciones.UserList[UserIndex].incomingData.length < 5) {
			throw new RuntimeException("Err . Raise UserList ( UserIndex ) . incomingData . NotEnoughDataErrCode");
			return;
		}
		/* # END IF */

		/* FIXME: ON ERROR GOTO ErrHandler */
		/*
		 * 'This packet contains strings, make a copy of the data to prevent
		 * losses if it's not complete yet...
		 */
		clsByteQueue buffer;
		buffer = new clsByteQueue();
		buffer.CopyBuffer(Declaraciones.UserList[UserIndex].incomingData);

		String oldPass;
		String newPass;
		String oldPass2;

		/* 'Remove packet ID */
		buffer.ReadByte();

		/* # IF SeguridadAlkon THEN */
		/* # ELSE */
		oldPass = vb6.UCase(buffer.ReadASCIIString());
		newPass = vb6.UCase(buffer.ReadASCIIString());
		/* # END IF */

		if (vb6.LenB(newPass) == 0) {
			WriteConsoleMsg(UserIndex, "Debes especificar una contrasena nueva, inténtalo de nuevo.",
					FontTypeNames.FONTTYPE_INFO);
		} else {
			oldPass2 = vb6.UCase(ES.GetVar(Declaraciones.CharPath + Declaraciones.UserList[UserIndex].Name + ".chr",
					"INIT", "Password"));

			if (oldPass2 != oldPass) {
				WriteConsoleMsg(UserIndex,
						"La contrasena actual proporcionada no es correcta. La contrasena no ha sido cambiada, inténtalo de nuevo.",
						FontTypeNames.FONTTYPE_INFO);
			} else {
				ES.WriteVar(Declaraciones.CharPath + Declaraciones.UserList[UserIndex].Name + ".chr", "INIT",
						"Password", newPass);
				WriteConsoleMsg(UserIndex, "La contrasena fue cambiada con éxito.", FontTypeNames.FONTTYPE_INFO);
			}
		}

		/*
		 * 'If we got here then packet is complete, copy data back to original
		 * queue
		 */
		Declaraciones.UserList[UserIndex].incomingData.CopyBuffer(buffer);

		/* FIXME: ErrHandler : */
		int ERROR;
		/* ERROR = Err . Number */
		/* FIXME: ON ERROR GOTO 0 */

		/* 'Destroy auxiliar buffer */
		buffer = null;

		if (ERROR != 0) {
			throw new RuntimeException("Err . Raise ERROR");
		}
	}

	/* '' */
	/* ' Handles the "Gamble" message. */
	/* ' */
	/* ' @param userIndex The index of the user sending the message. */

	static void HandleGamble(int UserIndex) {
		/* '*************************************************** */
		/* 'Author: Juan Martín Sotuyo Dodero (Maraxus) */
		/* 'Last Modification: 05/17/06 */
		/*
		 * '10/07/2010: ZaMa - Now normal npcs don't answer if asked to gamble.
		 */
		/* '*************************************************** */
		if (Declaraciones.UserList[UserIndex].incomingData.length < 3) {
			throw new RuntimeException("Err . Raise UserList ( UserIndex ) . incomingData . NotEnoughDataErrCode");
			return;
		}

		/* 'Remove packet ID */
		Declaraciones.UserList[UserIndex].incomingData.ReadByte();

		int Amount;
		eNPCType TypeNpc;

		Amount = Declaraciones.UserList[UserIndex].incomingData.ReadInteger();

		/* ' Dead? */
		if (Declaraciones.UserList[UserIndex].flags.Muerto == 1) {
			WriteConsoleMsg(UserIndex, "¡¡Estás muerto!!", FontTypeNames.FONTTYPE_INFO);

			/* 'Validate target NPC */
		} else if (Declaraciones.UserList[UserIndex].flags.TargetNPC == 0) {
			WriteConsoleMsg(UserIndex, "Primero tienes que seleccionar un personaje, haz click izquierdo sobre él.",
					FontTypeNames.FONTTYPE_INFO);

			/* ' Validate Distance */
		} else if (Matematicas.Distancia(Declaraciones.Npclist[Declaraciones.UserList[UserIndex].flags.TargetNPC].Pos,
				Declaraciones.UserList[UserIndex].Pos) > 10) {
			WriteConsoleMsg(UserIndex, "Estás demasiado lejos.", FontTypeNames.FONTTYPE_INFO);

			/* ' Validate NpcType */
		} else if (Declaraciones.Npclist[Declaraciones.UserList[UserIndex].flags.TargetNPC].NPCtype != eNPCType.Timbero) {

			eNPCType TargetNpcType;
			TargetNpcType = Declaraciones.Npclist[Declaraciones.UserList[UserIndex].flags.TargetNPC].NPCtype;

			/* ' Normal npcs don't speak */
			if (TargetNpcType != eNPCType.Comun && TargetNpcType != eNPCType.DRAGON
					&& TargetNpcType != eNPCType.Pretoriano) {
				WriteChatOverHead(UserIndex, "No tengo ningún interés en apostar.",
						Declaraciones.Npclist[Declaraciones.UserList[UserIndex].flags.TargetNPC].Char.CharIndex,
						0x00ffffff);
			}

			/* ' Validate amount */
		} else if (Amount < 1) {
			WriteChatOverHead(UserIndex, "El mínimo de apuesta es 1 moneda.",
					Declaraciones.Npclist[Declaraciones.UserList[UserIndex].flags.TargetNPC].Char.CharIndex,
					0x00ffffff);

			/* ' Validate amount */
		} else if (Amount > 5000) {
			WriteChatOverHead(UserIndex, "El máximo de apuesta es 5000 monedas.",
					Declaraciones.Npclist[Declaraciones.UserList[UserIndex].flags.TargetNPC].Char.CharIndex,
					0x00ffffff);

			/* ' Validate user gold */
		} else if (Declaraciones.UserList[UserIndex].Stats.GLD < Amount) {
			WriteChatOverHead(UserIndex, "No tienes esa cantidad.",
					Declaraciones.Npclist[Declaraciones.UserList[UserIndex].flags.TargetNPC].Char.CharIndex,
					0x00ffffff);

		} else {
			if (Matematicas.RandomNumber(1, 100) <= 47) {
				Declaraciones.UserList[UserIndex].Stats.GLD = Declaraciones.UserList[UserIndex].Stats.GLD + Amount;
				WriteChatOverHead(UserIndex, "¡Felicidades! Has ganado " + vb6.CStr(Amount) + " monedas de oro.",
						Declaraciones.Npclist[Declaraciones.UserList[UserIndex].flags.TargetNPC].Char.CharIndex,
						0x00ffffff);

				Admin.Apuestas.Perdidas = Admin.Apuestas.Perdidas + Amount;
				ES.WriteVar(Declaraciones.DatPath + "apuestas.dat", "Main", "Perdidas",
						vb6.CStr(Admin.Apuestas.Perdidas));
			} else {
				Declaraciones.UserList[UserIndex].Stats.GLD = Declaraciones.UserList[UserIndex].Stats.GLD - Amount;
				WriteChatOverHead(UserIndex, "Lo siento, has perdido " + vb6.CStr(Amount) + " monedas de oro.",
						Declaraciones.Npclist[Declaraciones.UserList[UserIndex].flags.TargetNPC].Char.CharIndex,
						0x00ffffff);

				Admin.Apuestas.Ganancias = Admin.Apuestas.Ganancias + Amount;
				ES.WriteVar(Declaraciones.DatPath + "apuestas.dat", "Main", "Ganancias",
						vb6.CStr(Admin.Apuestas.Ganancias));
			}

			Admin.Apuestas.Jugadas = Admin.Apuestas.Jugadas + 1;

			ES.WriteVar(Declaraciones.DatPath + "apuestas.dat", "Main", "Jugadas", vb6.CStr(Admin.Apuestas.Jugadas));

			WriteUpdateGold(UserIndex);
		}
	}

	/* '' */
	/* ' Handles the "InquiryVote" message. */
	/* ' */
	/* ' @param userIndex The index of the user sending the message. */

	static void HandleInquiryVote(int UserIndex) {
		/* '*************************************************** */
		/* 'Author: Juan Martín Sotuyo Dodero (Maraxus) */
		/* 'Last Modification: 05/17/06 */
		/* ' */
		/* '*************************************************** */
		if (Declaraciones.UserList[UserIndex].incomingData.length < 2) {
			throw new RuntimeException("Err . Raise UserList ( UserIndex ) . incomingData . NotEnoughDataErrCode");
			return;
		}

		/* 'Remove packet ID */
		Declaraciones.UserList[UserIndex].incomingData.ReadByte();

		int opt;

		opt = Declaraciones.UserList[UserIndex].incomingData.ReadByte();

		WriteConsoleMsg(UserIndex, Declaraciones.ConsultaPopular.doVotar(UserIndex, opt), FontTypeNames.FONTTYPE_GUILD);
	}

	/* '' */
	/* ' Handles the "BankExtractGold" message. */
	/* ' */
	/* ' @param userIndex The index of the user sending the message. */

	static void HandleBankExtractGold(int UserIndex) {
		/* '*************************************************** */
		/* 'Author: Juan Martín Sotuyo Dodero (Maraxus) */
		/* 'Last Modification: 05/17/06 */
		/* ' */
		/* '*************************************************** */
		if (Declaraciones.UserList[UserIndex].incomingData.length < 5) {
			throw new RuntimeException("Err . Raise UserList ( UserIndex ) . incomingData . NotEnoughDataErrCode");
			return;
		}

		/* 'Remove packet ID */
		Declaraciones.UserList[UserIndex].incomingData.ReadByte();

		int Amount;

		Amount = Declaraciones.UserList[UserIndex].incomingData.ReadLong();

		/* 'Dead people can't leave a faction.. they can't talk... */
		if (Declaraciones.UserList[UserIndex].flags.Muerto == 1) {
			WriteConsoleMsg(UserIndex, "¡¡Estás muerto!!", FontTypeNames.FONTTYPE_INFO);
			return;
		}

		/* 'Validate target NPC */
		if (Declaraciones.UserList[UserIndex].flags.TargetNPC == 0) {
			WriteConsoleMsg(UserIndex, "Primero tienes que seleccionar un personaje, haz click izquierdo sobre él.",
					FontTypeNames.FONTTYPE_INFO);
			return;
		}

		if (Declaraciones.Npclist[Declaraciones.UserList[UserIndex].flags.TargetNPC].NPCtype != eNPCType.Banquero) {
			return;
		}

		if (Matematicas.Distancia(Declaraciones.UserList[UserIndex].Pos,
				Declaraciones.Npclist[Declaraciones.UserList[UserIndex].flags.TargetNPC].Pos) > 10) {
			WriteConsoleMsg(UserIndex, "Estás demasiado lejos.", FontTypeNames.FONTTYPE_INFO);
			return;
		}

		if (Amount > 0 && Amount <= Declaraciones.UserList[UserIndex].Stats.Banco) {
			Declaraciones.UserList[UserIndex].Stats.Banco = Declaraciones.UserList[UserIndex].Stats.Banco - Amount;
			Declaraciones.UserList[UserIndex].Stats.GLD = Declaraciones.UserList[UserIndex].Stats.GLD + Amount;
			WriteChatOverHead(UserIndex,
					"Tenés " + Declaraciones.UserList[UserIndex].Stats.Banco + " monedas de oro en tu cuenta.",
					Declaraciones.Npclist[Declaraciones.UserList[UserIndex].flags.TargetNPC].Char.CharIndex,
					0x00ffffff);
		} else {
			WriteChatOverHead(UserIndex, "No tienes esa cantidad.",
					Declaraciones.Npclist[Declaraciones.UserList[UserIndex].flags.TargetNPC].Char.CharIndex,
					0x00ffffff);
		}

		WriteUpdateGold(UserIndex);
		WriteUpdateBankGold(UserIndex);
	}

	/* '' */
	/* ' Handles the "LeaveFaction" message. */
	/* ' */
	/* ' @param userIndex The index of the user sending the message. */

	static void HandleLeaveFaction(int UserIndex) {
		/* '*************************************************** */
		/* 'Author: Juan Martín Sotuyo Dodero (Maraxus) */
		/* 'Last Modification: 09/28/2010 */
		/*
		 * ' 09/28/2010 C4b3z0n - Ahora la respuesta de los NPCs sino perteneces
		 * a ninguna facción solo la hacen el Rey o el Demonio
		 */
		/* ' 05/17/06 - Maraxus */
		/* '*************************************************** */

		boolean TalkToKing;
		boolean TalkToDemon;
		int NpcIndex;

		/* 'Remove packet ID */
		Declaraciones.UserList[UserIndex].incomingData.ReadByte();

		/* 'Dead people can't leave a faction.. they can't talk... */
		if (Declaraciones.UserList[UserIndex].flags.Muerto == 1) {
			WriteConsoleMsg(UserIndex, "¡¡Estás muerto!!", FontTypeNames.FONTTYPE_INFO);
			return;
		}

		/*
		 * ' Chequea si habla con el rey o el demonio. Puede salir sin hacerlo,
		 * pero si lo hace le reponden los npcs
		 */
		NpcIndex = Declaraciones.UserList[UserIndex].flags.TargetNPC;
		if (NpcIndex != 0) {
			/* ' Es rey o domonio? */
			if (Declaraciones.Npclist[NpcIndex].NPCtype == eNPCType.Noble) {
				/* 'Rey? */
				if (Declaraciones.Npclist[NpcIndex].flags.Faccion == 0) {
					TalkToKing = true;
					/* ' Demonio */
				} else {
					TalkToDemon = true;
				}
			}
		}

		/* 'Quit the Royal Army? */
		if (Declaraciones.UserList[UserIndex].Faccion.ArmadaReal == 1) {
			/* ' Si le pidio al demonio salir de la armada, este le responde. */
			if (TalkToDemon) {
				WriteChatOverHead(UserIndex, "¡¡¡Sal de aquí bufón!!!", Declaraciones.Npclist[NpcIndex].Char.CharIndex,
						0x00ffffff);

			} else {
				/* ' Si le pidio al rey salir de la armada, le responde. */
				if (TalkToKing) {
					WriteChatOverHead(UserIndex, "Serás bienvenido a las fuerzas imperiales si deseas regresar.",
							Declaraciones.Npclist[NpcIndex].Char.CharIndex, 0x00ffffff);
				}

				ModFacciones.ExpulsarFaccionReal(UserIndex, false);

			}

			/* 'Quit the Chaos Legion? */
		} else if (Declaraciones.UserList[UserIndex].Faccion.FuerzasCaos == 1) {
			/* ' Si le pidio al rey salir del caos, le responde. */
			if (TalkToKing) {
				WriteChatOverHead(UserIndex, "¡¡¡Sal de aquí maldito criminal!!!",
						Declaraciones.Npclist[NpcIndex].Char.CharIndex, 0x00ffffff);
			} else {
				/* ' Si le pidio al demonio salir del caos, este le responde. */
				if (TalkToDemon) {
					WriteChatOverHead(UserIndex, "Ya volverás arrastrandote.",
							Declaraciones.Npclist[NpcIndex].Char.CharIndex, 0x00ffffff);
				}

				ModFacciones.ExpulsarFaccionCaos(UserIndex, false);
			}
			/* ' No es faccionario */
		} else {

			/* ' Si le hablaba al rey o demonio, le repsonden ellos */
			/*
			 * 'Corregido, solo si son en efecto el rey o el demonio, no
			 * cualquier NPC (C4b3z0n)
			 */
			/* 'Si se pueden unir a la facción (status), son invitados */
			if ((TalkToDemon && ES.criminal(UserIndex)) || (TalkToKing && !ES.criminal(UserIndex))) {
				WriteChatOverHead(UserIndex, "No perteneces a nuestra facción. Si deseas unirte, di /ENLISTAR",
						Declaraciones.Npclist[NpcIndex].Char.CharIndex, 0x00ffffff);
			} else if ((TalkToDemon && !ES.criminal(UserIndex))) {
				WriteChatOverHead(UserIndex, "¡¡¡Sal de aquí bufón!!!", Declaraciones.Npclist[NpcIndex].Char.CharIndex,
						0x00ffffff);
			} else if ((TalkToKing && ES.criminal(UserIndex))) {
				WriteChatOverHead(UserIndex, "¡¡¡Sal de aquí maldito criminal!!!",
						Declaraciones.Npclist[NpcIndex].Char.CharIndex, 0x00ffffff);
			} else {
				WriteConsoleMsg(UserIndex, "¡No perteneces a ninguna facción!", FontTypeNames.FONTTYPE_FIGHT);
			}

		}

	}

	/* '' */
	/* ' Handles the "BankDepositGold" message. */
	/* ' */
	/* ' @param userIndex The index of the user sending the message. */

	static void HandleBankDepositGold(int UserIndex) {
		/* '*************************************************** */
		/* 'Author: Juan Martín Sotuyo Dodero (Maraxus) */
		/* 'Last Modification: 05/17/06 */
		/* ' */
		/* '*************************************************** */
		if (Declaraciones.UserList[UserIndex].incomingData.length < 5) {
			throw new RuntimeException("Err . Raise UserList ( UserIndex ) . incomingData . NotEnoughDataErrCode");
			return;
		}

		/* 'Remove packet ID */
		Declaraciones.UserList[UserIndex].incomingData.ReadByte();

		int Amount;

		Amount = Declaraciones.UserList[UserIndex].incomingData.ReadLong();

		/* 'Dead people can't leave a faction.. they can't talk... */
		if (Declaraciones.UserList[UserIndex].flags.Muerto == 1) {
			WriteConsoleMsg(UserIndex, "¡¡Estás muerto!!", FontTypeNames.FONTTYPE_INFO);
			return;
		}

		/* 'Validate target NPC */
		if (Declaraciones.UserList[UserIndex].flags.TargetNPC == 0) {
			WriteConsoleMsg(UserIndex, "Primero tienes que seleccionar un personaje, haz click izquierdo sobre él.",
					FontTypeNames.FONTTYPE_INFO);
			return;
		}

		if (Matematicas.Distancia(Declaraciones.Npclist[Declaraciones.UserList[UserIndex].flags.TargetNPC].Pos,
				Declaraciones.UserList[UserIndex].Pos) > 10) {
			WriteConsoleMsg(UserIndex, "Estás demasiado lejos.", FontTypeNames.FONTTYPE_INFO);
			return;
		}

		if (Declaraciones.Npclist[Declaraciones.UserList[UserIndex].flags.TargetNPC].NPCtype != eNPCType.Banquero) {
			return;
		}

		if (Amount > 0 && Amount <= Declaraciones.UserList[UserIndex].Stats.GLD) {
			Declaraciones.UserList[UserIndex].Stats.Banco = Declaraciones.UserList[UserIndex].Stats.Banco + Amount;
			Declaraciones.UserList[UserIndex].Stats.GLD = Declaraciones.UserList[UserIndex].Stats.GLD - Amount;
			WriteChatOverHead(UserIndex,
					"Tenés " + Declaraciones.UserList[UserIndex].Stats.Banco + " monedas de oro en tu cuenta.",
					Declaraciones.Npclist[Declaraciones.UserList[UserIndex].flags.TargetNPC].Char.CharIndex,
					0x00ffffff);

			WriteUpdateGold(UserIndex);
			WriteUpdateBankGold(UserIndex);
		} else {
			WriteChatOverHead(UserIndex, "No tenés esa cantidad.",
					Declaraciones.Npclist[Declaraciones.UserList[UserIndex].flags.TargetNPC].Char.CharIndex,
					0x00ffffff);
		}
	}

	/* '' */
	/* ' Handles the "Denounce" message. */
	/* ' */
	/* ' @param userIndex The index of the user sending the message. */

	static void HandleDenounce(int UserIndex) {
		/* '*************************************************** */
		/* 'Author: Juan Martín Sotuyo Dodero (Maraxus) */
		/* 'Last Modification: 14/11/2010 */
		/* '14/11/2010: ZaMa - Now denounces can be desactivated. */
		/* '*************************************************** */
		if (Declaraciones.UserList[UserIndex].incomingData.length < 3) {
			throw new RuntimeException("Err . Raise UserList ( UserIndex ) . incomingData . NotEnoughDataErrCode");
			return;
		}

		/* FIXME: ON ERROR GOTO ErrHandler */
		/*
		 * 'This packet contains strings, make a copy of the data to prevent
		 * losses if it's not complete yet...
		 */
		clsByteQueue buffer;
		buffer = new clsByteQueue();
		buffer.CopyBuffer(Declaraciones.UserList[UserIndex].incomingData);

		/* 'Remove packet ID */
		buffer.ReadByte();

		String Text;
		String msg;

		Text = buffer.ReadASCIIString();

		if (Declaraciones.UserList[UserIndex].flags.Silenciado == 0) {
			/* 'Analize chat... */
			Statistics.ParseChat(Text);

			msg = vb6.LCase(Declaraciones.UserList[UserIndex].Name) + " DENUNCIA: " + Text;

			modSendData.SendData(SendTarget.ToAdmins, 0, PrepareMessageConsoleMsg(msg, FontTypeNames.FONTTYPE_GUILDMSG),
					true);

			Declaraciones.Denuncias.Push(msg, false);

			WriteConsoleMsg(UserIndex, "Denuncia enviada, espere..", FontTypeNames.FONTTYPE_INFO);
		}

		/*
		 * 'If we got here then packet is complete, copy data back to original
		 * queue
		 */
		Declaraciones.UserList[UserIndex].incomingData.CopyBuffer(buffer);

		/* FIXME: ErrHandler : */
		int ERROR;
		/* ERROR = Err . Number */
		/* FIXME: ON ERROR GOTO 0 */

		/* 'Destroy auxiliar buffer */
		buffer = null;

		if (ERROR != 0) {
			throw new RuntimeException("Err . Raise ERROR");
		}
	}

	/* '' */
	/* ' Handles the "GuildFundate" message. */
	/* ' */
	/* ' @param userIndex The index of the user sending the message. */

	static void HandleGuildFundate(int UserIndex) {
		/* '*************************************************** */
		/* 'Author: ZaMa */
		/* 'Last Modification: 14/12/2009 */
		/* ' */
		/* '*************************************************** */
		if (Declaraciones.UserList[UserIndex].incomingData.length < 1) {
			throw new RuntimeException("Err . Raise UserList ( UserIndex ) . incomingData . NotEnoughDataErrCode");
			return;
		}

		Declaraciones.UserList[UserIndex].incomingData.ReadByte();

		if (modGuilds.HasFound(Declaraciones.UserList[UserIndex].Name)) {
			WriteConsoleMsg(UserIndex, "¡Ya has fundado un clan, no puedes fundar otro!",
					FontTypeNames.FONTTYPE_INFOBOLD);
			return;
		}

		WriteShowGuildAlign(UserIndex);
	}

	/* '' */
	/* ' Handles the "GuildFundation" message. */
	/* ' */
	/* ' @param userIndex The index of the user sending the message. */

	static void HandleGuildFundation(int UserIndex) {
		/* '*************************************************** */
		/* 'Author: ZaMa */
		/* 'Last Modification: 14/12/2009 */
		/* ' */
		/* '*************************************************** */
		if (Declaraciones.UserList[UserIndex].incomingData.length < 2) {
			throw new RuntimeException("Err . Raise UserList ( UserIndex ) . incomingData . NotEnoughDataErrCode");
			return;
		}

		/* 'Remove packet ID */
		Declaraciones.UserList[UserIndex].incomingData.ReadByte();

		eClanType clanType;
		String ERROR;

		clanType = Declaraciones.UserList[UserIndex].incomingData.ReadByte();

		if (modGuilds.HasFound(Declaraciones.UserList[UserIndex].Name)) {
			WriteConsoleMsg(UserIndex, "¡Ya has fundado un clan, no puedes fundar otro!",
					FontTypeNames.FONTTYPE_INFOBOLD);
			General.LogCheating("El usuario " + Declaraciones.UserList[UserIndex].Name
					+ " ha intentado fundar un clan ya habiendo fundado otro desde la IP "
					+ Declaraciones.UserList[UserIndex].ip);
			return;
		}

		switch (vb6.UCase(vb6.Trim(clanType))) {
		case ct_RoyalArmy:
			Declaraciones.UserList[UserIndex].FundandoGuildAlineacion = ALINEACION_ARMADA;
			break;

		case ct_Evil:
			Declaraciones.UserList[UserIndex].FundandoGuildAlineacion = ALINEACION_LEGION;
			break;

		case ct_Neutral:
			Declaraciones.UserList[UserIndex].FundandoGuildAlineacion = ALINEACION_NEUTRO;
			break;

		case ct_GM:
			Declaraciones.UserList[UserIndex].FundandoGuildAlineacion = ALINEACION_MASTER;
			break;

		case ct_Legal:
			Declaraciones.UserList[UserIndex].FundandoGuildAlineacion = ALINEACION_CIUDA;
			break;

		case ct_Criminal:
			Declaraciones.UserList[UserIndex].FundandoGuildAlineacion = ALINEACION_CRIMINAL;
			break;

		default:
			WriteConsoleMsg(UserIndex, "Alineación inválida.", FontTypeNames.FONTTYPE_GUILD);
			return;
			break;
		}

		if (modGuilds.PuedeFundarUnClan(UserIndex, Declaraciones.UserList[UserIndex].FundandoGuildAlineacion, ERROR)) {
			WriteShowGuildFundationForm(UserIndex);
		} else {
			Declaraciones.UserList[UserIndex].FundandoGuildAlineacion = 0;
			WriteConsoleMsg(UserIndex, ERROR, FontTypeNames.FONTTYPE_GUILD);
		}
	}

	/* '' */
	/* ' Handles the "PartyKick" message. */
	/* ' */
	/* ' @param userIndex The index of the user sending the message. */

	static void HandlePartyKick(int UserIndex) {
		/* '*************************************************** */
		/* 'Author: Juan Martín Sotuyo Dodero (Maraxus) */
		/* 'Last Modification: 05/05/09 */
		/* 'Last Modification by: Marco Vanotti (Marco) */
		/*
		 * '- 05/05/09: Now it uses "UserPuedeEjecutarComandos" to check if the
		 * user can use party commands
		 */
		/* '*************************************************** */
		if (Declaraciones.UserList[UserIndex].incomingData.length < 3) {
			throw new RuntimeException("Err . Raise UserList ( UserIndex ) . incomingData . NotEnoughDataErrCode");
			return;
		}

		/* FIXME: ON ERROR GOTO ErrHandler */
		/*
		 * 'This packet contains strings, make a copy of the data to prevent
		 * losses if it's not complete yet...
		 */
		clsByteQueue buffer;
		buffer = new clsByteQueue();
		buffer.CopyBuffer(Declaraciones.UserList[UserIndex].incomingData);

		/* 'Remove packet ID */
		buffer.ReadByte();

		String UserName;
		int tUser;

		UserName = buffer.ReadASCIIString();

		if (mdParty.UserPuedeEjecutarComandos(UserIndex)) {
			tUser = Extra.NameIndex(UserName);

			if (tUser > 0) {
				mdParty.ExpulsarDeParty(UserIndex, tUser);
			} else {
				if (vb6.InStr(UserName, "+")) {
					UserName = vb6.Replace(UserName, "+", " ");
				}

				WriteConsoleMsg(UserIndex, vb6.LCase(UserName) + " no pertenece a tu party.",
						FontTypeNames.FONTTYPE_INFO);
			}
		}

		/*
		 * 'If we got here then packet is complete, copy data back to original
		 * queue
		 */
		Declaraciones.UserList[UserIndex].incomingData.CopyBuffer(buffer);

		/* FIXME: ErrHandler : */
		int ERROR;
		/* ERROR = Err . Number */
		/* FIXME: ON ERROR GOTO 0 */

		/* 'Destroy auxiliar buffer */
		buffer = null;

		if (ERROR != 0) {
			throw new RuntimeException("Err . Raise ERROR");
		}
	}

	/* '' */
	/* ' Handles the "PartySetLeader" message. */
	/* ' */
	/* ' @param userIndex The index of the user sending the message. */

	static void HandlePartySetLeader(int UserIndex) {
		/* '*************************************************** */
		/* 'Author: Juan Martín Sotuyo Dodero (Maraxus) */
		/* 'Last Modification: 05/05/09 */
		/* 'Last Modification by: Marco Vanotti (MarKoxX) */
		/*
		 * '- 05/05/09: Now it uses "UserPuedeEjecutarComandos" to check if the
		 * user can use party commands
		 */
		/* '*************************************************** */
		if (Declaraciones.UserList[UserIndex].incomingData.length < 3) {
			throw new RuntimeException("Err . Raise UserList ( UserIndex ) . incomingData . NotEnoughDataErrCode");
			return;
		}

		/* 'On Error GoTo ErrHandler */
		/*
		 * 'This packet contains strings, make a copy of the data to prevent
		 * losses if it's not complete yet...
		 */
		clsByteQueue buffer;
		buffer = new clsByteQueue();
		buffer.CopyBuffer(Declaraciones.UserList[UserIndex].incomingData);

		/* 'Remove packet ID */
		buffer.ReadByte();

		String UserName;
		int tUser;
		int rank;
		rank = PlayerType.Admin || PlayerType.Dios || PlayerType.SemiDios || PlayerType.Consejero;

		UserName = buffer.ReadASCIIString();
		if (mdParty.UserPuedeEjecutarComandos(UserIndex)) {
			tUser = Extra.NameIndex(UserName);
			if (tUser > 0) {
				/* 'Don't allow users to spoof online GMs */
				if ((Admin.UserDarPrivilegioLevel(UserName)
						&& rank) <= (Declaraciones.UserList[UserIndex].flags.Privilegios && rank)) {
					mdParty.TransformarEnLider(UserIndex, tUser);
				} else {
					WriteConsoleMsg(UserIndex,
							vb6.LCase(Declaraciones.UserList[tUser].Name) + " no pertenece a tu party.",
							FontTypeNames.FONTTYPE_INFO);
				}

			} else {
				if (vb6.InStr(UserName, "+")) {
					UserName = vb6.Replace(UserName, "+", " ");
				}
				WriteConsoleMsg(UserIndex, vb6.LCase(UserName) + " no pertenece a tu party.",
						FontTypeNames.FONTTYPE_INFO);
			}
		}

		/*
		 * 'If we got here then packet is complete, copy data back to original
		 * queue
		 */
		Declaraciones.UserList[UserIndex].incomingData.CopyBuffer(buffer);

		/* FIXME: ErrHandler : */
		int ERROR;
		/* ERROR = Err . Number */
		/* FIXME: ON ERROR GOTO 0 */

		/* 'Destroy auxiliar buffer */
		buffer = null;

		if (ERROR != 0) {
			throw new RuntimeException("Err . Raise ERROR");
		}
	}

	/* '' */
	/* ' Handles the "PartyAcceptMember" message. */
	/* ' */
	/* ' @param userIndex The index of the user sending the message. */

	static void HandlePartyAcceptMember(int UserIndex) {
		/* '*************************************************** */
		/* 'Author: Juan Martín Sotuyo Dodero (Maraxus) */
		/* 'Last Modification: 05/05/09 */
		/* 'Last Modification by: Marco Vanotti (Marco) */
		/*
		 * '- 05/05/09: Now it uses "UserPuedeEjecutarComandos" to check if the
		 * user can use party commands
		 */
		/* '*************************************************** */
		if (Declaraciones.UserList[UserIndex].incomingData.length < 3) {
			throw new RuntimeException("Err . Raise UserList ( UserIndex ) . incomingData . NotEnoughDataErrCode");
			return;
		}

		/* FIXME: ON ERROR GOTO ErrHandler */
		/*
		 * 'This packet contains strings, make a copy of the data to prevent
		 * losses if it's not complete yet...
		 */
		clsByteQueue buffer;
		buffer = new clsByteQueue();
		buffer.CopyBuffer(Declaraciones.UserList[UserIndex].incomingData);

		/* 'Remove packet ID */
		buffer.ReadByte();

		String UserName;
		int tUser;
		int rank;
		boolean bUserVivo;

		rank = PlayerType.Admin || PlayerType.Dios || PlayerType.SemiDios || PlayerType.Consejero;

		UserName = buffer.ReadASCIIString();
		if (Declaraciones.UserList[UserIndex].flags.Muerto) {
			WriteConsoleMsg(UserIndex, "¡¡Estás muerto!!", FontTypeNames.FONTTYPE_PARTY);
		} else {
			bUserVivo = true;
		}

		if (mdParty.UserPuedeEjecutarComandos(UserIndex) && bUserVivo) {
			tUser = Extra.NameIndex(UserName);
			if (tUser > 0) {
				/*
				 * 'Validate administrative ranks - don't allow users to spoof
				 * online GMs
				 */
				if ((Declaraciones.UserList[tUser].flags.Privilegios
						&& rank) <= (Declaraciones.UserList[UserIndex].flags.Privilegios && rank)) {
					mdParty.AprobarIngresoAParty(UserIndex, tUser);
				} else {
					WriteConsoleMsg(UserIndex, "No puedes incorporar a tu party a personajes de mayor jerarquía.",
							FontTypeNames.FONTTYPE_INFO);
				}
			} else {
				if (vb6.InStr(UserName, "+")) {
					UserName = vb6.Replace(UserName, "+", " ");
				}

				/* 'Don't allow users to spoof online GMs */
				if ((Admin.UserDarPrivilegioLevel(UserName)
						&& rank) <= (Declaraciones.UserList[UserIndex].flags.Privilegios && rank)) {
					WriteConsoleMsg(UserIndex, vb6.LCase(UserName) + " no ha solicitado ingresar a tu party.",
							FontTypeNames.FONTTYPE_PARTY);
				} else {
					WriteConsoleMsg(UserIndex, "No puedes incorporar a tu party a personajes de mayor jerarquía.",
							FontTypeNames.FONTTYPE_INFO);
				}
			}
		}

		/*
		 * 'If we got here then packet is complete, copy data back to original
		 * queue
		 */
		Declaraciones.UserList[UserIndex].incomingData.CopyBuffer(buffer);

		/* FIXME: ErrHandler : */
		int ERROR;
		/* ERROR = Err . Number */
		/* FIXME: ON ERROR GOTO 0 */

		/* 'Destroy auxiliar buffer */
		buffer = null;

		if (ERROR != 0) {
			throw new RuntimeException("Err . Raise ERROR");
		}
	}

	/* '' */
	/* ' Handles the "GuildMemberList" message. */
	/* ' */
	/* ' @param userIndex The index of the user sending the message. */

	static void HandleGuildMemberList(int UserIndex) {
		/* '*************************************************** */
		/* 'Author: Juan Martín Sotuyo Dodero (Maraxus) */
		/* 'Last Modification: 05/17/06 */
		/* ' */
		/* '*************************************************** */
		if (Declaraciones.UserList[UserIndex].incomingData.length < 3) {
			throw new RuntimeException("Err . Raise UserList ( UserIndex ) . incomingData . NotEnoughDataErrCode");
			return;
		}

		/* FIXME: ON ERROR GOTO ErrHandler */
		/*
		 * 'This packet contains strings, make a copy of the data to prevent
		 * losses if it's not complete yet...
		 */
		clsByteQueue buffer;
		buffer = new clsByteQueue();
		buffer.CopyBuffer(Declaraciones.UserList[UserIndex].incomingData);

		/* 'Remove packet ID */
		buffer.ReadByte();

		String guild;
		int memberCount;
		int i;
		String UserName;

		guild = buffer.ReadASCIIString();

		if (Declaraciones.UserList[UserIndex].flags.Privilegios && (PlayerType.Admin || PlayerType.Dios)) {
			if ((vb6.InStrB(guild, "\\") != 0)) {
				guild = vb6.Replace(guild, "\\", "");
			}
			if ((vb6.InStrB(guild, "/") != 0)) {
				guild = vb6.Replace(guild, "/", "");
			}

			if (!General.FileExist(vb6.App.Instance().Path + "\\guilds\\" + guild + "-members.mem")) {
				WriteConsoleMsg(UserIndex, "No existe el clan: " + guild, FontTypeNames.FONTTYPE_INFO);
			} else {
				memberCount = vb6.val(ES.GetVar(vb6.App.Instance().Path + "\\Guilds\\" + guild + "-Members" + ".mem",
						"INIT", "NroMembers"));

				for (i = (1); i <= (memberCount); i++) {
					UserName = ES.GetVar(vb6.App.Instance().Path + "\\Guilds\\" + guild + "-Members" + ".mem",
							"Members", "Member" + i);

					WriteConsoleMsg(UserIndex, UserName + "<" + guild + ">", FontTypeNames.FONTTYPE_INFO);
				}
			}
		}

		/*
		 * 'If we got here then packet is complete, copy data back to original
		 * queue
		 */
		Declaraciones.UserList[UserIndex].incomingData.CopyBuffer(buffer);

		/* FIXME: ErrHandler : */
		int ERROR;
		/* ERROR = Err . Number */
		/* FIXME: ON ERROR GOTO 0 */

		/* 'Destroy auxiliar buffer */
		buffer = null;

		if (ERROR != 0) {
			throw new RuntimeException("Err . Raise ERROR");
		}
	}

	/* '' */
	/* ' Handles the "GMMessage" message. */
	/* ' */
	/* ' @param userIndex The index of the user sending the message. */

	static void HandleGMMessage(int UserIndex) {
		/* '*************************************************** */
		/* 'Author: Juan Martín Sotuyo Dodero (Maraxus) */
		/* 'Last Modification: 01/08/07 */
		/* 'Last Modification by: (liquid) */
		/* '*************************************************** */
		if (Declaraciones.UserList[UserIndex].incomingData.length < 3) {
			throw new RuntimeException("Err . Raise UserList ( UserIndex ) . incomingData . NotEnoughDataErrCode");
			return;
		}

		/* FIXME: ON ERROR GOTO ErrHandler */
		/*
		 * 'This packet contains strings, make a copy of the data to prevent
		 * losses if it's not complete yet...
		 */
		clsByteQueue buffer;
		buffer = new clsByteQueue();
		buffer.CopyBuffer(Declaraciones.UserList[UserIndex].incomingData);

		/* 'Remove packet ID */
		buffer.ReadByte();

		String message;

		message = buffer.ReadASCIIString();

		if (!Declaraciones.UserList[UserIndex].flags.Privilegios && PlayerType.User) {
			General.LogGM(Declaraciones.UserList[UserIndex].Name, "Mensaje a Gms:" + message);

			if (vb6.LenB(message) != 0) {
				/* 'Analize chat... */
				Statistics.ParseChat(message);

				modSendData.SendData(SendTarget.ToAdmins, 0, PrepareMessageConsoleMsg(
						Declaraciones.UserList[UserIndex].Name + "> " + message, FontTypeNames.FONTTYPE_GMMSG));
			}
		}

		/*
		 * 'If we got here then packet is complete, copy data back to original
		 * queue
		 */
		Declaraciones.UserList[UserIndex].incomingData.CopyBuffer(buffer);

		/* FIXME: ErrHandler : */
		int ERROR;
		/* ERROR = Err . Number */
		/* FIXME: ON ERROR GOTO 0 */

		/* 'Destroy auxiliar buffer */
		buffer = null;

		if (ERROR != 0) {
			throw new RuntimeException("Err . Raise ERROR");
		}
	}

	/* '' */
	/* ' Handles the "ShowName" message. */
	/* ' */
	/* ' @param userIndex The index of the user sending the message. */

	static void HandleShowName(int UserIndex) {
		/* '*************************************************** */
		/* 'Author: Juan Martín Sotuyo Dodero (Maraxus) */
		/* 'Last Modification: 05/17/06 */
		/* ' */
		/* '*************************************************** */
		/* 'Remove packet ID */
		Declaraciones.UserList[UserIndex].incomingData.ReadByte();

		if (Declaraciones.UserList[UserIndex].flags.Privilegios
				&& (PlayerType.Dios || PlayerType.Admin || PlayerType.RoleMaster)) {
			/* 'Show / Hide the name */
			Declaraciones.UserList[UserIndex].showName = !Declaraciones.UserList[UserIndex].showName;

			UsUaRiOs.RefreshCharStatus(UserIndex);
		}
	}

	/* '' */
	/* ' Handles the "OnlineRoyalArmy" message. */
	/* ' */
	/* ' @param userIndex The index of the user sending the message. */

	static void HandleOnlineRoyalArmy(int UserIndex) {
		/* '*************************************************** */
		/* 'Author: Juan Martín Sotuyo Dodero (Maraxus) */
		/* 'Last Modification: 28/05/2010 */
		/*
		 * '28/05/2010: ZaMa - Ahora solo dioses pueden ver otros dioses online.
		 */
		/* '*************************************************** */
		/* 'Remove packet ID */
		Declaraciones.UserList[UserIndex].incomingData.ReadByte();

		if (Declaraciones.UserList[UserIndex].flags.Privilegios && PlayerType.User) {
			return;
		}

		int i;
		String list;
		PlayerType priv;

		priv = PlayerType.User || PlayerType.Consejero || PlayerType.SemiDios;

		/* ' Solo dioses pueden ver otros dioses online */
		if (Declaraciones.UserList[UserIndex].flags.Privilegios && (PlayerType.Dios || PlayerType.Admin)) {
			priv = priv || PlayerType.Dios || PlayerType.Admin;
		}

		for (i = (1); i <= (Declaraciones.LastUser); i++) {
			if (Declaraciones.UserList[i].ConnID != -1) {
				if (Declaraciones.UserList[i].Faccion.ArmadaReal == 1) {
					if (Declaraciones.UserList[i].flags.Privilegios && priv) {
						list = list + Declaraciones.UserList[i].Name + ", ";
					}
				}
			}
		}

		if (vb6.Len(list) > 0) {
			WriteConsoleMsg(UserIndex, "Reales conectados: " + vb6.Left(list, vb6.Len(list) - 2),
					FontTypeNames.FONTTYPE_INFO);
		} else {
			WriteConsoleMsg(UserIndex, "No hay reales conectados.", FontTypeNames.FONTTYPE_INFO);
		}
	}

	/* '' */
	/* ' Handles the "OnlineChaosLegion" message. */
	/* ' */
	/* ' @param userIndex The index of the user sending the message. */

	static void HandleOnlineChaosLegion(int UserIndex) {
		/* '*************************************************** */
		/* 'Author: Juan Martín Sotuyo Dodero (Maraxus) */
		/* 'Last Modification: 28/05/2010 */
		/*
		 * '28/05/2010: ZaMa - Ahora solo dioses pueden ver otros dioses online.
		 */
		/* '*************************************************** */
		/* 'Remove packet ID */
		Declaraciones.UserList[UserIndex].incomingData.ReadByte();

		if (Declaraciones.UserList[UserIndex].flags.Privilegios && PlayerType.User) {
			return;
		}

		int i;
		String list;
		PlayerType priv;

		priv = PlayerType.User || PlayerType.Consejero || PlayerType.SemiDios;

		/* ' Solo dioses pueden ver otros dioses online */
		if (Declaraciones.UserList[UserIndex].flags.Privilegios && (PlayerType.Dios || PlayerType.Admin)) {
			priv = priv || PlayerType.Dios || PlayerType.Admin;
		}

		for (i = (1); i <= (Declaraciones.LastUser); i++) {
			if (Declaraciones.UserList[i].ConnID != -1) {
				if (Declaraciones.UserList[i].Faccion.FuerzasCaos == 1) {
					if (Declaraciones.UserList[i].flags.Privilegios && priv) {
						list = list + Declaraciones.UserList[i].Name + ", ";
					}
				}
			}
		}

		if (vb6.Len(list) > 0) {
			WriteConsoleMsg(UserIndex, "Caos conectados: " + vb6.Left(list, vb6.Len(list) - 2),
					FontTypeNames.FONTTYPE_INFO);
		} else {
			WriteConsoleMsg(UserIndex, "No hay Caos conectados.", FontTypeNames.FONTTYPE_INFO);
		}
	}

	/* '' */
	/* ' Handles the "GoNearby" message. */
	/* ' */
	/* ' @param userIndex The index of the user sending the message. */

	static void HandleGoNearby(int UserIndex) {
		/* '*************************************************** */
		/* 'Author: Juan Martín Sotuyo Dodero (Maraxus) */
		/* 'Last Modification: 01/10/07 */
		/* ' */
		/* '*************************************************** */
		if (Declaraciones.UserList[UserIndex].incomingData.length < 3) {
			throw new RuntimeException("Err . Raise UserList ( UserIndex ) . incomingData . NotEnoughDataErrCode");
			return;
		}

		/* FIXME: ON ERROR GOTO ErrHandler */
		/*
		 * 'This packet contains strings, make a copy of the data to prevent
		 * losses if it's not complete yet...
		 */
		clsByteQueue buffer;
		buffer = new clsByteQueue();
		buffer.CopyBuffer(Declaraciones.UserList[UserIndex].incomingData);

		/* 'Remove packet ID */
		buffer.ReadByte();

		String UserName;

		UserName = buffer.ReadASCIIString();

		int tIndex;
		int X;
		int Y;
		int i;
		boolean Found;

		tIndex = Extra.NameIndex(UserName);

		/* 'Check the user has enough powers */
		if (Declaraciones.UserList[UserIndex].flags.Privilegios
				&& (PlayerType.Admin || PlayerType.Dios || PlayerType.SemiDios || PlayerType.Consejero)) {
			/*
			 * 'Si es dios o Admins no podemos salvo que nosotros también lo
			 * seamos
			 */
			if (!(ES.EsDios(UserName) || ES.EsAdmin(UserName))
					|| (Declaraciones.UserList[UserIndex].flags.Privilegios && (PlayerType.Dios || PlayerType.Admin))) {
				/* 'existe el usuario destino? */
				if (tIndex <= 0) {
					WriteConsoleMsg(UserIndex, "Usuario offline.", FontTypeNames.FONTTYPE_INFO);
				} else {
					/* 'esto for sirve ir cambiando la distancia destino */
					for (i = (2); i <= (5); i++) {
						for (X = (Declaraciones.UserList[tIndex].Pos.X - i); X <= (Declaraciones.UserList[tIndex].Pos.X
								+ i); X++) {
							for (Y = (Declaraciones.UserList[tIndex].Pos.Y
									- i); Y <= (Declaraciones.UserList[tIndex].Pos.Y + i); Y++) {
								if (Declaraciones.MapData[Declaraciones.UserList[tIndex].Pos.Map][X][Y].UserIndex == 0) {
									if (Extra.LegalPos(Declaraciones.UserList[tIndex].Pos.Map, X, Y, true, true)) {
										UsUaRiOs.WarpUserChar(UserIndex, Declaraciones.UserList[tIndex].Pos.Map, X, Y,
												true);
										General.LogGM(Declaraciones.UserList[UserIndex].Name,
												"/IRCERCA " + UserName + " Mapa:"
														+ Declaraciones.UserList[tIndex].Pos.Map + " X:"
														+ Declaraciones.UserList[tIndex].Pos.X + " Y:"
														+ Declaraciones.UserList[tIndex].Pos.Y);
										Found = true;
										break; /* FIXME: EXIT FOR */
									}
								}
							}

							/*
							 * ' Feo, pero hay que abortar 3 fors sin usar GoTo
							 */
							if (Found) {
								break; /* FIXME: EXIT FOR */
							}
						}

						/* ' Feo, pero hay que abortar 3 fors sin usar GoTo */
						if (Found) {
							break; /* FIXME: EXIT FOR */
						}
					}

					/* 'No space found?? */
					if (!Found) {
						WriteConsoleMsg(UserIndex, "Todos los lugares están ocupados.", FontTypeNames.FONTTYPE_INFO);
					}
				}
			}
		}

		/*
		 * 'If we got here then packet is complete, copy data back to original
		 * queue
		 */
		Declaraciones.UserList[UserIndex].incomingData.CopyBuffer(buffer);

		/* FIXME: ErrHandler : */
		int ERROR;
		/* ERROR = Err . Number */
		/* FIXME: ON ERROR GOTO 0 */

		/* 'Destroy auxiliar buffer */
		buffer = null;

		if (ERROR != 0) {
			throw new RuntimeException("Err . Raise ERROR");
		}
	}

	/* '' */
	/* ' Handles the "Comment" message. */
	/* ' */
	/* ' @param userIndex The index of the user sending the message. */

	static void HandleComment(int UserIndex) {
		/* '*************************************************** */
		/* 'Author: Juan Martín Sotuyo Dodero (Maraxus) */
		/* 'Last Modification: 05/17/06 */
		/* ' */
		/* '*************************************************** */
		if (Declaraciones.UserList[UserIndex].incomingData.length < 3) {
			throw new RuntimeException("Err . Raise UserList ( UserIndex ) . incomingData . NotEnoughDataErrCode");
			return;
		}

		/* FIXME: ON ERROR GOTO ErrHandler */
		/*
		 * 'This packet contains strings, make a copy of the data to prevent
		 * losses if it's not complete yet...
		 */
		clsByteQueue buffer;
		buffer = new clsByteQueue();
		buffer.CopyBuffer(Declaraciones.UserList[UserIndex].incomingData);

		/* 'Remove packet ID */
		buffer.ReadByte();

		String comment;
		comment = buffer.ReadASCIIString();

		if (!Declaraciones.UserList[UserIndex].flags.Privilegios && PlayerType.User) {
			General.LogGM(Declaraciones.UserList[UserIndex].Name, "Comentario: " + comment);
			WriteConsoleMsg(UserIndex, "Comentario salvado...", FontTypeNames.FONTTYPE_INFO);
		}

		/*
		 * 'If we got here then packet is complete, copy data back to original
		 * queue
		 */
		Declaraciones.UserList[UserIndex].incomingData.CopyBuffer(buffer);

		/* FIXME: ErrHandler : */
		int ERROR;
		/* ERROR = Err . Number */
		/* FIXME: ON ERROR GOTO 0 */

		/* 'Destroy auxiliar buffer */
		buffer = null;

		if (ERROR != 0) {
			throw new RuntimeException("Err . Raise ERROR");
		}
	}

	/* '' */
	/* ' Handles the "ServerTime" message. */
	/* ' */
	/* ' @param userIndex The index of the user sending the message. */

	static void HandleServerTime(int UserIndex) {
		/* '*************************************************** */
		/* 'Author: Juan Martín Sotuyo Dodero (Maraxus) */
		/* 'Last Modification: 01/08/07 */
		/* 'Last Modification by: (liquid) */
		/* '*************************************************** */
		/* 'Remove packet ID */
		Declaraciones.UserList[UserIndex].incomingData.ReadByte();

		if (Declaraciones.UserList[UserIndex].flags.Privilegios && PlayerType.User) {
			return;
		}

		General.LogGM(Declaraciones.UserList[UserIndex].Name, "Hora.");

		modSendData.SendData(SendTarget.ToAll, 0,
				PrepareMessageConsoleMsg("Hora: " + vb6.time + " " + Date, FontTypeNames.FONTTYPE_INFO));
	}

	/* '' */
	/* ' Handles the "Where" message. */
	/* ' */
	/* ' @param userIndex The index of the user sending the message. */

	static void HandleWhere(int UserIndex) {
		/* '*************************************************** */
		/* 'Author: Juan Martín Sotuyo Dodero (Maraxus) */
		/* 'Last Modification: 18/11/2010 */
		/*
		 * '07/06/2010: ZaMa - Ahora no se puede usar para saber si hay
		 * dioses/admins online.
		 */
		/*
		 * '18/11/2010: ZaMa - Obtengo los privs del charfile antes de mostrar
		 * la posicion de un usuario offline.
		 */
		/* '*************************************************** */
		if (Declaraciones.UserList[UserIndex].incomingData.length < 3) {
			throw new RuntimeException("Err . Raise UserList ( UserIndex ) . incomingData . NotEnoughDataErrCode");
			return;
		}

		/* FIXME: ON ERROR GOTO ErrHandler */
		/*
		 * 'This packet contains strings, make a copy of the data to prevent
		 * losses if it's not complete yet...
		 */
		clsByteQueue buffer;
		buffer = new clsByteQueue();
		buffer.CopyBuffer(Declaraciones.UserList[UserIndex].incomingData);

		/* 'Remove packet ID */
		buffer.ReadByte();

		String UserName;
		int tUser;
		String miPos;

		UserName = buffer.ReadASCIIString();

		if (!Declaraciones.UserList[UserIndex].flags.Privilegios && PlayerType.User) {

			tUser = Extra.NameIndex(UserName);
			if (tUser <= 0) {

				if (General.FileExist(Declaraciones.CharPath + UserName + ".chr", 0)) {

					PlayerType CharPrivs;
					CharPrivs = ES.GetCharPrivs(UserName);

					if ((CharPrivs && (PlayerType.User || PlayerType.Consejero || PlayerType.SemiDios)) != 0
							|| ((CharPrivs && (PlayerType.Dios || PlayerType.Admin) != 0)
									&& (Declaraciones.UserList[UserIndex].flags.Privilegios
											&& (PlayerType.Dios || PlayerType.Admin)) != 0)) {
						miPos = ES.GetVar(Declaraciones.CharPath + UserName + ".chr", "INIT", "POSITION");
						WriteConsoleMsg(UserIndex,
								"Ubicación  " + UserName + " (Offline): " + General.ReadField(1, miPos, 45) + ", "
										+ General.ReadField(2, miPos, 45) + ", " + General.ReadField(3, miPos, 45)
										+ ".",
								FontTypeNames.FONTTYPE_INFO);
					}
				} else {
					if (!(ES.EsDios(UserName) || ES.EsAdmin(UserName))) {
						WriteConsoleMsg(UserIndex, "Usuario inexistente.", FontTypeNames.FONTTYPE_INFO);
					} else if (Declaraciones.UserList[UserIndex].flags.Privilegios
							&& (PlayerType.Dios || PlayerType.Admin)) {
						WriteConsoleMsg(UserIndex, "Usuario inexistente.", FontTypeNames.FONTTYPE_INFO);
					}
				}
			} else {
				if ((Declaraciones.UserList[tUser].flags.Privilegios
						&& (PlayerType.User || PlayerType.Consejero || PlayerType.SemiDios)) != 0
						|| ((Declaraciones.UserList[tUser].flags.Privilegios
								&& (PlayerType.Dios || PlayerType.Admin) != 0)
								&& (Declaraciones.UserList[UserIndex].flags.Privilegios
										&& (PlayerType.Dios || PlayerType.Admin)) != 0)) {
					WriteConsoleMsg(UserIndex,
							"Ubicación  " + UserName + ": " + Declaraciones.UserList[tUser].Pos.Map + ", "
									+ Declaraciones.UserList[tUser].Pos.X + ", " + Declaraciones.UserList[tUser].Pos.Y
									+ ".",
							FontTypeNames.FONTTYPE_INFO);
				}
			}

			General.LogGM(Declaraciones.UserList[UserIndex].Name, "/Donde " + UserName);
		}

		/*
		 * 'If we got here then packet is complete, copy data back to original
		 * queue
		 */
		Declaraciones.UserList[UserIndex].incomingData.CopyBuffer(buffer);

		/* FIXME: ErrHandler : */
		int ERROR;
		/* ERROR = Err . Number */
		/* FIXME: ON ERROR GOTO 0 */

		/* 'Destroy auxiliar buffer */
		buffer = null;

		if (ERROR != 0) {
			throw new RuntimeException("Err . Raise ERROR");
		}
	}

	/* '' */
	/* ' Handles the "CreaturesInMap" message. */
	/* ' */
	/* ' @param userIndex The index of the user sending the message. */

	static void HandleCreaturesInMap(int UserIndex) {
		/* '*************************************************** */
		/* 'Author: Juan Martín Sotuyo Dodero (Maraxus) */
		/* 'Last Modification: 30/07/06 */
		/*
		 * 'Pablo (ToxicWaste): modificaciones generales para simplificar la
		 * visualización.
		 */
		/* '*************************************************** */
		if (Declaraciones.UserList[UserIndex].incomingData.length < 3) {
			throw new RuntimeException("Err . Raise UserList ( UserIndex ) . incomingData . NotEnoughDataErrCode");
			return;
		}

		/* 'Remove packet ID */
		Declaraciones.UserList[UserIndex].incomingData.ReadByte();

		int Map;
		Object i;
		int j;
		Object NPCcount1;
		int NPCcount2;
		int[] NPCcant1;
		int[] NPCcant2;
		String[] List1;
		String[] List2;

		Map = Declaraciones.UserList[UserIndex].incomingData.ReadInteger();

		if (Declaraciones.UserList[UserIndex].flags.Privilegios && PlayerType.User) {
			return;
		}

		if (General.MapaValido(Map)) {
			for (i = (1); i <= (Declaraciones.LastNPC); i++) {
				/*
				 * 'VB isn't lazzy, so we put more restrictive condition first
				 * to speed up the process
				 */
				if (Declaraciones.Npclist[i].Pos.Map == Map) {
					/* '¿esta vivo? */
					if (Declaraciones.Npclist[i].flags.NPCActive && Declaraciones.Npclist[i].Hostile == 1
							&& Declaraciones.Npclist[i].Stats.Alineacion == 2) {
						if (NPCcount1 == 0) {
							List1 = new String[0];
							List1 = (List1 == null) ? new String[0] : java.util.Arrays.copyOf(List1, 0);
							NPCcant1 = new Integer[0];
							NPCcant1 = (NPCcant1 == null) ? new Integer[0] : java.util.Arrays.copyOf(NPCcant1, 0);
							NPCcount1 = 1;
							List1[0] = Declaraciones.Npclist[i].Name + ": (" + Declaraciones.Npclist[i].Pos.X + ","
									+ Declaraciones.Npclist[i].Pos.Y + ")";
							NPCcant1[0] = 1;
						} else {
							for (j = (0); j <= (NPCcount1 - 1); j++) {
								if (vb6.Left(List1[j],
										vb6.Len(Declaraciones.Npclist[i].Name)) == Declaraciones.Npclist[i].Name) {
									List1[j] = List1[j] + ", (" + Declaraciones.Npclist[i].Pos.X + ","
											+ Declaraciones.Npclist[i].Pos.Y + ")";
									NPCcant1[j] = NPCcant1[j] + 1;
									break; /* FIXME: EXIT FOR */
								}
							}
							if (j == NPCcount1) {
								List1 = (List1 == null) ? new String[NPCcount1]
										: java.util.Arrays.copyOf(List1, NPCcount1);
								NPCcant1 = (NPCcant1 == null) ? new Integer[NPCcount1]
										: java.util.Arrays.copyOf(NPCcant1, NPCcount1);
								NPCcount1 = NPCcount1 + 1;
								List1[j] = Declaraciones.Npclist[i].Name + ": (" + Declaraciones.Npclist[i].Pos.X + ","
										+ Declaraciones.Npclist[i].Pos.Y + ")";
								NPCcant1[j] = 1;
							}
						}
					} else {
						if (NPCcount2 == 0) {
							List2 = new String[0];
							List2 = (List2 == null) ? new String[0] : java.util.Arrays.copyOf(List2, 0);
							NPCcant2 = new Integer[0];
							NPCcant2 = (NPCcant2 == null) ? new Integer[0] : java.util.Arrays.copyOf(NPCcant2, 0);
							NPCcount2 = 1;
							List2[0] = Declaraciones.Npclist[i].Name + ": (" + Declaraciones.Npclist[i].Pos.X + ","
									+ Declaraciones.Npclist[i].Pos.Y + ")";
							NPCcant2[0] = 1;
						} else {
							for (j = (0); j <= (NPCcount2 - 1); j++) {
								if (vb6.Left(List2[j],
										vb6.Len(Declaraciones.Npclist[i].Name)) == Declaraciones.Npclist[i].Name) {
									List2[j] = List2[j] + ", (" + Declaraciones.Npclist[i].Pos.X + ","
											+ Declaraciones.Npclist[i].Pos.Y + ")";
									NPCcant2[j] = NPCcant2[j] + 1;
									break; /* FIXME: EXIT FOR */
								}
							}
							if (j == NPCcount2) {
								List2 = (List2 == null) ? new String[NPCcount2]
										: java.util.Arrays.copyOf(List2, NPCcount2);
								NPCcant2 = (NPCcant2 == null) ? new Integer[NPCcount2]
										: java.util.Arrays.copyOf(NPCcant2, NPCcount2);
								NPCcount2 = NPCcount2 + 1;
								List2[j] = Declaraciones.Npclist[i].Name + ": (" + Declaraciones.Npclist[i].Pos.X + ","
										+ Declaraciones.Npclist[i].Pos.Y + ")";
								NPCcant2[j] = 1;
							}
						}
					}
				}
			}

			WriteConsoleMsg(UserIndex, "Npcs Hostiles en mapa: ", FontTypeNames.FONTTYPE_WARNING);
			if (NPCcount1 == 0) {
				WriteConsoleMsg(UserIndex, "No hay NPCS Hostiles.", FontTypeNames.FONTTYPE_INFO);
			} else {
				for (j = (0); j <= (NPCcount1 - 1); j++) {
					WriteConsoleMsg(UserIndex, NPCcant1[j] + " " + List1[j], FontTypeNames.FONTTYPE_INFO);
				}
			}
			WriteConsoleMsg(UserIndex, "Otros Npcs en mapa: ", FontTypeNames.FONTTYPE_WARNING);
			if (NPCcount2 == 0) {
				WriteConsoleMsg(UserIndex, "No hay más NPCS.", FontTypeNames.FONTTYPE_INFO);
			} else {
				for (j = (0); j <= (NPCcount2 - 1); j++) {
					WriteConsoleMsg(UserIndex, NPCcant2[j] + " " + List2[j], FontTypeNames.FONTTYPE_INFO);
				}
			}
			General.LogGM(Declaraciones.UserList[UserIndex].Name, "Numero enemigos en mapa " + Map);
		}
	}

	/* '' */
	/* ' Handles the "WarpMeToTarget" message. */
	/* ' */
	/* ' @param userIndex The index of the user sending the message. */

	static void HandleWarpMeToTarget(int UserIndex) {
		/* '*************************************************** */
		/* 'Author: Juan Martín Sotuyo Dodero (Maraxus) */
		/* 'Last Modification: 26/03/09 */
		/*
		 * '26/03/06: ZaMa - Chequeo que no se teletransporte donde haya un char
		 * o npc
		 */
		/* '*************************************************** */
		/* 'Remove packet ID */
		Declaraciones.UserList[UserIndex].incomingData.ReadByte();

		int X;
		int Y;

		if (Declaraciones.UserList[UserIndex].flags.Privilegios && PlayerType.User) {
			return;
		}

		X = Declaraciones.UserList[UserIndex].flags.TargetX;
		Y = Declaraciones.UserList[UserIndex].flags.TargetY;

		Extra.FindLegalPos(UserIndex, Declaraciones.UserList[UserIndex].flags.TargetMap, X, Y);
		UsUaRiOs.WarpUserChar(UserIndex, Declaraciones.UserList[UserIndex].flags.TargetMap, X, Y, true);
		General.LogGM(Declaraciones.UserList[UserIndex].Name,
				"/TELEPLOC a x:" + Declaraciones.UserList[UserIndex].flags.TargetX + " Y:"
						+ Declaraciones.UserList[UserIndex].flags.TargetY + " Map:"
						+ Declaraciones.UserList[UserIndex].Pos.Map);
	}

	/* '' */
	/* ' Handles the "WarpChar" message. */
	/* ' */
	/* ' @param userIndex The index of the user sending the message. */

	static void HandleWarpChar(int UserIndex) {
		/* '*************************************************** */
		/* 'Author: Juan Martín Sotuyo Dodero (Maraxus) */
		/* 'Last Modification: 26/03/2009 */
		/*
		 * '26/03/2009: ZaMa - Chequeo que no se teletransporte a un tile donde
		 * haya un char o npc.
		 */
		/* '*************************************************** */
		if (Declaraciones.UserList[UserIndex].incomingData.length < 7) {
			throw new RuntimeException("Err . Raise UserList ( UserIndex ) . incomingData . NotEnoughDataErrCode");
			return;
		}

		/* FIXME: ON ERROR GOTO ErrHandler */
		/*
		 * 'This packet contains strings, make a copy of the data to prevent
		 * losses if it's not complete yet...
		 */
		clsByteQueue buffer;
		buffer = new clsByteQueue();
		buffer.CopyBuffer(Declaraciones.UserList[UserIndex].incomingData);

		/* 'Remove packet ID */
		buffer.ReadByte();

		String UserName;
		int Map;
		int X;
		int Y;
		int tUser;

		UserName = buffer.ReadASCIIString();
		Map = buffer.ReadInteger();
		X = buffer.ReadByte();
		Y = buffer.ReadByte();

		if (!Declaraciones.UserList[UserIndex].flags.Privilegios && PlayerType.User) {
			if (General.MapaValido(Map) && vb6.LenB(UserName) != 0) {
				if (vb6.UCase(UserName) != "YO") {
					if (!Declaraciones.UserList[UserIndex].flags.Privilegios && PlayerType.Consejero) {
						tUser = Extra.NameIndex(UserName);
					}
				} else {
					tUser = UserIndex;
				}

				if (tUser <= 0) {
					if (!(ES.EsDios(UserName) || ES.EsAdmin(UserName))) {
						WriteConsoleMsg(UserIndex, "Usuario offline.", FontTypeNames.FONTTYPE_INFO);
					} else {
						WriteConsoleMsg(UserIndex, "No puedes transportar dioses o admins.",
								FontTypeNames.FONTTYPE_INFO);
					}

				} else if (!((Declaraciones.UserList[tUser].flags.Privilegios && PlayerType.Dios) != 0
						|| (Declaraciones.UserList[tUser].flags.Privilegios && PlayerType.Admin) != 0)
						|| tUser == UserIndex) {

					if (Extra.InMapBounds(Map, X, Y)) {
						Extra.FindLegalPos(tUser, Map, X, Y);
						UsUaRiOs.WarpUserChar(tUser, Map, X, Y, true, true);
						WriteConsoleMsg(UserIndex, Declaraciones.UserList[tUser].Name + " transportado.",
								FontTypeNames.FONTTYPE_INFO);
						General.LogGM(Declaraciones.UserList[UserIndex].Name,
								"Transportó a " + Declaraciones.UserList[tUser].Name + " hacia " + "Mapa" + Map + " X:"
										+ X + " Y:" + Y);
					}
				} else {
					WriteConsoleMsg(UserIndex, "No puedes transportar dioses o admins.", FontTypeNames.FONTTYPE_INFO);
				}
			}
		}

		/*
		 * 'If we got here then packet is complete, copy data back to original
		 * queue
		 */
		Declaraciones.UserList[UserIndex].incomingData.CopyBuffer(buffer);

		/* FIXME: ErrHandler : */
		int ERROR;
		/* ERROR = Err . Number */
		/* FIXME: ON ERROR GOTO 0 */

		/* 'Destroy auxiliar buffer */
		buffer = null;

		if (ERROR != 0) {
			throw new RuntimeException("Err . Raise ERROR");
		}
	}

	/* '' */
	/* ' Handles the "Silence" message. */
	/* ' */
	/* ' @param userIndex The index of the user sending the message. */

	static void HandleSilence(int UserIndex) {
		/* '*************************************************** */
		/* 'Author: Juan Martín Sotuyo Dodero (Maraxus) */
		/* 'Last Modification: 05/17/06 */
		/* ' */
		/* '*************************************************** */
		if (Declaraciones.UserList[UserIndex].incomingData.length < 3) {
			throw new RuntimeException("Err . Raise UserList ( UserIndex ) . incomingData . NotEnoughDataErrCode");
			return;
		}

		/* FIXME: ON ERROR GOTO ErrHandler */
		/*
		 * 'This packet contains strings, make a copy of the data to prevent
		 * losses if it's not complete yet...
		 */
		clsByteQueue buffer;
		buffer = new clsByteQueue();
		buffer.CopyBuffer(Declaraciones.UserList[UserIndex].incomingData);

		/* 'Remove packet ID */
		buffer.ReadByte();

		String UserName;
		int tUser;

		UserName = buffer.ReadASCIIString();

		if (!Declaraciones.UserList[UserIndex].flags.Privilegios && PlayerType.User) {
			tUser = Extra.NameIndex(UserName);

			if (tUser <= 0) {
				WriteConsoleMsg(UserIndex, "Usuario offline.", FontTypeNames.FONTTYPE_INFO);
			} else {
				if (Declaraciones.UserList[tUser].flags.Silenciado == 0) {
					Declaraciones.UserList[tUser].flags.Silenciado = 1;
					WriteConsoleMsg(UserIndex, "Usuario silenciado.", FontTypeNames.FONTTYPE_INFO);
					WriteShowMessageBox(tUser,
							"Estimado usuario, ud. ha sido silenciado por los administradores. Sus denuncias serán ignoradas por el servidor de aquí en más. Utilice /GM para contactar un administrador.");
					General.LogGM(Declaraciones.UserList[UserIndex].Name,
							"/silenciar " + Declaraciones.UserList[tUser].Name);

					/* 'Flush the other user's buffer */
					FlushBuffer(tUser);
				} else {
					Declaraciones.UserList[tUser].flags.Silenciado = 0;
					WriteConsoleMsg(UserIndex, "Usuario des silenciado.", FontTypeNames.FONTTYPE_INFO);
					General.LogGM(Declaraciones.UserList[UserIndex].Name,
							"/DESsilenciar " + Declaraciones.UserList[tUser].Name);
				}
			}
		}

		/*
		 * 'If we got here then packet is complete, copy data back to original
		 * queue
		 */
		Declaraciones.UserList[UserIndex].incomingData.CopyBuffer(buffer);

		/* FIXME: ErrHandler : */
		int ERROR;
		/* ERROR = Err . Number */
		/* FIXME: ON ERROR GOTO 0 */

		/* 'Destroy auxiliar buffer */
		buffer = null;

		if (ERROR != 0) {
			throw new RuntimeException("Err . Raise ERROR");
		}
	}

	/* '' */
	/* ' Handles the "SOSShowList" message. */
	/* ' */
	/* ' @param userIndex The index of the user sending the message. */

	static void HandleSOSShowList(int UserIndex) {
		/* '*************************************************** */
		/* 'Author: Juan Martín Sotuyo Dodero (Maraxus) */
		/* 'Last Modification: 05/17/06 */
		/* ' */
		/* '*************************************************** */
		/* 'Remove packet ID */
		Declaraciones.UserList[UserIndex].incomingData.ReadByte();

		if (Declaraciones.UserList[UserIndex].flags.Privilegios && PlayerType.User) {
			return;
		}
		WriteShowSOSForm(UserIndex);
	}

	/* '' */
	/* ' Handles the "RequestPartyForm" message. */
	/* ' */
	/* ' @param userIndex The index of the user sending the message. */

	static void HandlePartyForm(int UserIndex) {
		/* '*************************************************** */
		/* 'Author: Budi */
		/* 'Last Modification: 11/26/09 */
		/* ' */
		/* '*************************************************** */
		/* 'Remove packet ID */
		Declaraciones.UserList[UserIndex].incomingData.ReadByte();
		if (Declaraciones.UserList[UserIndex].PartyIndex > 0) {
			WriteShowPartyForm(UserIndex);

		} else {
			WriteConsoleMsg(UserIndex, "No perteneces a ningún grupo!", FontTypeNames.FONTTYPE_INFOBOLD);
		}
	}

	/* '' */
	/* ' Handles the "ItemUpgrade" message. */
	/* ' */
	/* ' @param UserIndex The index of the user sending the message. */

	static void HandleItemUpgrade(int UserIndex) {
		/* '*************************************************** */
		/* 'Author: Torres Patricio */
		/* 'Last Modification: 12/09/09 */
		/* ' */
		/* '*************************************************** */
		int ItemIndex;

		/* 'Remove packet ID */
		Declaraciones.UserList[UserIndex].incomingData.ReadByte();

		ItemIndex = Declaraciones.UserList[UserIndex].incomingData.ReadInteger();

		if (ItemIndex <= 0) {
			return;
		}
		if (!Trabajo.TieneObjetos(ItemIndex, 1, UserIndex)) {
			return;
		}

		if (!modNuevoTimer.IntervaloPermiteTrabajar(UserIndex)) {
			return;
		}
		Trabajo.DoUpgrade(UserIndex, ItemIndex);
	}

	/* '' */
	/* ' Handles the "SOSRemove" message. */
	/* ' */
	/* ' @param userIndex The index of the user sending the message. */

	static void HandleSOSRemove(int UserIndex) {
		/* '*************************************************** */
		/* 'Author: Juan Martín Sotuyo Dodero (Maraxus) */
		/* 'Last Modification: 05/17/06 */
		/* ' */
		/* '*************************************************** */
		if (Declaraciones.UserList[UserIndex].incomingData.length < 3) {
			throw new RuntimeException("Err . Raise UserList ( UserIndex ) . incomingData . NotEnoughDataErrCode");
			return;
		}

		/* FIXME: ON ERROR GOTO ErrHandler */
		/*
		 * 'This packet contains strings, make a copy of the data to prevent
		 * losses if it's not complete yet...
		 */
		clsByteQueue buffer;
		buffer = new clsByteQueue();
		buffer.CopyBuffer(Declaraciones.UserList[UserIndex].incomingData);

		/* 'Remove packet ID */
		buffer.ReadByte();

		String UserName;
		UserName = buffer.ReadASCIIString();

		if (!Declaraciones.UserList[UserIndex].flags.Privilegios && PlayerType.User) {
			Declaraciones.Ayuda.Quitar(UserName);
		}

		/*
		 * 'If we got here then packet is complete, copy data back to original
		 * queue
		 */
		Declaraciones.UserList[UserIndex].incomingData.CopyBuffer(buffer);

		/* FIXME: ErrHandler : */
		int ERROR;
		/* ERROR = Err . Number */
		/* FIXME: ON ERROR GOTO 0 */

		/* 'Destroy auxiliar buffer */
		buffer = null;

		if (ERROR != 0) {
			throw new RuntimeException("Err . Raise ERROR");
		}
	}

	/* '' */
	/* ' Handles the "GoToChar" message. */
	/* ' */
	/* ' @param userIndex The index of the user sending the message. */

	static void HandleGoToChar(int UserIndex) {
		/* '*************************************************** */
		/* 'Author: Juan Martín Sotuyo Dodero (Maraxus) */
		/* 'Last Modification: 26/03/2009 */
		/*
		 * '26/03/2009: ZaMa - Chequeo que no se teletransporte a un tile donde
		 * haya un char o npc.
		 */
		/* '*************************************************** */
		if (Declaraciones.UserList[UserIndex].incomingData.length < 3) {
			throw new RuntimeException("Err . Raise UserList ( UserIndex ) . incomingData . NotEnoughDataErrCode");
			return;
		}

		/* FIXME: ON ERROR GOTO ErrHandler */
		/*
		 * 'This packet contains strings, make a copy of the data to prevent
		 * losses if it's not complete yet...
		 */
		clsByteQueue buffer;
		buffer = new clsByteQueue();
		buffer.CopyBuffer(Declaraciones.UserList[UserIndex].incomingData);

		/* 'Remove packet ID */
		buffer.ReadByte();

		String UserName;
		int tUser;
		int X;
		int Y;

		UserName = buffer.ReadASCIIString();
		tUser = Extra.NameIndex(UserName);

		if (Declaraciones.UserList[UserIndex].flags.Privilegios
				&& (PlayerType.Dios || PlayerType.Admin || PlayerType.SemiDios || PlayerType.Consejero)) {
			/*
			 * 'Si es dios o Admins no podemos salvo que nosotros también lo
			 * seamos
			 */
			if ((!(ES.EsDios(UserName) || ES.EsAdmin(UserName)))
					|| (((Declaraciones.UserList[UserIndex].flags.Privilegios
							&& (PlayerType.Dios || PlayerType.Admin)) != 0)
							&& ((Declaraciones.UserList[UserIndex].flags.Privilegios && PlayerType.RoleMaster) == 0))) {
				if (tUser <= 0) {
					WriteConsoleMsg(UserIndex, "Usuario offline.", FontTypeNames.FONTTYPE_INFO);
				} else {
					X = Declaraciones.UserList[tUser].Pos.X;
					Y = Declaraciones.UserList[tUser].Pos.Y + 1;
					Extra.FindLegalPos(UserIndex, Declaraciones.UserList[tUser].Pos.Map, X, Y);

					UsUaRiOs.WarpUserChar(UserIndex, Declaraciones.UserList[tUser].Pos.Map, X, Y, true);

					if (Declaraciones.UserList[UserIndex].flags.AdminInvisible == 0) {
						WriteConsoleMsg(tUser, Declaraciones.UserList[UserIndex].Name
								+ " se ha trasportado hacia donde te encuentras.", FontTypeNames.FONTTYPE_INFO);
						FlushBuffer(tUser);
					}

					General.LogGM(Declaraciones.UserList[UserIndex].Name,
							"/IRA " + UserName + " Mapa:" + Declaraciones.UserList[tUser].Pos.Map + " X:"
									+ Declaraciones.UserList[tUser].Pos.X + " Y:"
									+ Declaraciones.UserList[tUser].Pos.Y);
				}
			}
		}

		/*
		 * 'If we got here then packet is complete, copy data back to original
		 * queue
		 */
		Declaraciones.UserList[UserIndex].incomingData.CopyBuffer(buffer);

		/* FIXME: ErrHandler : */
		int ERROR;
		/* ERROR = Err . Number */
		/* FIXME: ON ERROR GOTO 0 */

		/* 'Destroy auxiliar buffer */
		buffer = null;

		if (ERROR != 0) {
			throw new RuntimeException("Err . Raise ERROR");
		}
	}

	/* '' */
	/* ' Handles the "Invisible" message. */
	/* ' */
	/* ' @param userIndex The index of the user sending the message. */

	static void HandleInvisible(int UserIndex) {
		/* '*************************************************** */
		/* 'Author: Juan Martín Sotuyo Dodero (Maraxus) */
		/* 'Last Modification: 05/17/06 */
		/* ' */
		/* '*************************************************** */
		/* 'Remove packet ID */
		Declaraciones.UserList[UserIndex].incomingData.ReadByte();

		if (Declaraciones.UserList[UserIndex].flags.Privilegios && PlayerType.User) {
			return;
		}

		Trabajo.DoAdminInvisible(UserIndex);
		General.LogGM(Declaraciones.UserList[UserIndex].Name, "/INVISIBLE");
	}

	/* '' */
	/* ' Handles the "GMPanel" message. */
	/* ' */
	/* ' @param userIndex The index of the user sending the message. */

	static void HandleGMPanel(int UserIndex) {
		/* '*************************************************** */
		/* 'Author: Juan Martín Sotuyo Dodero (Maraxus) */
		/* 'Last Modification: 05/17/06 */
		/* ' */
		/* '*************************************************** */
		/* 'Remove packet ID */
		Declaraciones.UserList[UserIndex].incomingData.ReadByte();

		if (Declaraciones.UserList[UserIndex].flags.Privilegios && PlayerType.User) {
			return;
		}

		WriteShowGMPanelForm(UserIndex);
	}

	/* '' */
	/* ' Handles the "GMPanel" message. */
	/* ' */
	/* ' @param userIndex The index of the user sending the message. */

	static void HandleRequestUserList(int UserIndex) {
 /* '*************************************************** */
 /* 'Author: Juan Martín Sotuyo Dodero (Maraxus) */
 /* 'Last Modification: 01/09/07 */
 /* 'Last modified by: Lucas Tavolaro Ortiz (Tavo) */
 /* 'I haven`t found a solution to split, so i make an array of names */
 /* '*************************************************** */
 int i;
 String[] names;
 int Count;
 
  /* 'Remove packet ID */
  Declaraciones.UserList[UserIndex].incomingData.ReadByte();
  
  if (Declaraciones.UserList[UserIndex].flags.Privilegios && (PlayerType.User || PlayerType.RoleMaster)) {
  return;
  }
  
  names = new String[0];
  names = (names == null) ? new String[1 + Declaraciones.LastUser] : java.util.Arrays.copyOf(names, 1 + Declaraciones.LastUser);
  Count = 1;
  
   for (i = (1); i <= (Declaraciones.LastUser); i++) {
    if ((vb6.LenB(Declaraciones.UserList[i].Name) != 0)) {
     if (Declaraciones.UserList[i].flags.Privilegios && PlayerType.User) {
     names[Count] = Declaraciones.UserList[i].Name;
     Count = Count+1;
    }
   }
  }
  
  if (Count>1) {
  WriteUserNameList(UserIndex, names[], Count-1);
  }
}

	/* '' */
	/* ' Handles the "Working" message. */
	/* ' */
	/* ' @param userIndex The index of the user sending the message. */

	static void HandleWorking(int UserIndex) {
		/* '*************************************************** */
		/* 'Author: Juan Martín Sotuyo Dodero (Maraxus) */
		/* 'Last Modification: 07/10/2010 */
		/*
		 * '07/10/2010: ZaMa - Adaptado para que funcione mas de un centinela en
		 * paralelo.
		 */
		/* '*************************************************** */
		int i;
		String users;

		/* 'Remove packet ID */
		Declaraciones.UserList[UserIndex].incomingData.ReadByte();

		if (Declaraciones.UserList[UserIndex].flags.Privilegios && (PlayerType.User || PlayerType.RoleMaster)) {
			return;
		}

		for (i = (1); i <= (Declaraciones.LastUser); i++) {
			if (Declaraciones.UserList[i].flags.UserLogged && Declaraciones.UserList[i].Counters.Trabajando > 0) {
				users = users + ", " + Declaraciones.UserList[i].Name;

				/* ' Display the user being checked by the centinel */
				if (Declaraciones.UserList[i].flags.CentinelaIndex != 0) {
					users = users + " (*)";
				}
			}
		}

		if (vb6.LenB(users) != 0) {
			users = vb6.Right(users, vb6.Len(users) - 2);
			WriteConsoleMsg(UserIndex, "Usuarios trabajando: " + users, FontTypeNames.FONTTYPE_INFO);
		} else {
			WriteConsoleMsg(UserIndex, "No hay usuarios trabajando.", FontTypeNames.FONTTYPE_INFO);
		}
	}

	/* '' */
	/* ' Handles the "Hiding" message. */
	/* ' */
	/* ' @param userIndex The index of the user sending the message. */

	static void HandleHiding(int UserIndex) {
		/* '*************************************************** */
		/* 'Author: Juan Martín Sotuyo Dodero (Maraxus) */
		/* 'Last Modification: 05/17/06 */
		/* ' */
		/* '*************************************************** */
		int i;
		String users;

		/* 'Remove packet ID */
		Declaraciones.UserList[UserIndex].incomingData.ReadByte();

		if (Declaraciones.UserList[UserIndex].flags.Privilegios && (PlayerType.User || PlayerType.RoleMaster)) {
			return;
		}

		for (i = (1); i <= (Declaraciones.LastUser); i++) {
			if ((vb6.LenB(Declaraciones.UserList[i].Name) != 0) && Declaraciones.UserList[i].Counters.Ocultando > 0) {
				users = users + Declaraciones.UserList[i].Name + ", ";
			}
		}

		if (vb6.LenB(users) != 0) {
			users = vb6.Left(users, vb6.Len(users) - 2);
			WriteConsoleMsg(UserIndex, "Usuarios ocultandose: " + users, FontTypeNames.FONTTYPE_INFO);
		} else {
			WriteConsoleMsg(UserIndex, "No hay usuarios ocultandose.", FontTypeNames.FONTTYPE_INFO);
		}
	}

	/* '' */
	/* ' Handles the "Jail" message. */
	/* ' */
	/* ' @param userIndex The index of the user sending the message. */

	static void HandleJail(int UserIndex) {
		/* '*************************************************** */
		/* 'Author: Juan Martín Sotuyo Dodero (Maraxus) */
		/* 'Last Modification: 07/06/2010 */
		/*
		 * '07/06/2010: ZaMa - Ahora no se puede usar para saber si hay
		 * dioses/admins online.
		 */
		/* '*************************************************** */
		if (Declaraciones.UserList[UserIndex].incomingData.length < 6) {
			throw new RuntimeException("Err . Raise UserList ( UserIndex ) . incomingData . NotEnoughDataErrCode");
			return;
		}

		/* FIXME: ON ERROR GOTO ErrHandler */
		/*
		 * 'This packet contains strings, make a copy of the data to prevent
		 * losses if it's not complete yet...
		 */
		clsByteQueue buffer;
		buffer = new clsByteQueue();
		buffer.CopyBuffer(Declaraciones.UserList[UserIndex].incomingData);

		/* 'Remove packet ID */
		buffer.ReadByte();

		String UserName;
		String Reason;
		int jailTime;
		int Count;
		int tUser;

		UserName = buffer.ReadASCIIString();
		Reason = buffer.ReadASCIIString();
		jailTime = buffer.ReadByte();

		if (vb6.InStr(1, UserName, "+")) {
			UserName = vb6.Replace(UserName, "+", " ");
		}

		/* '/carcel nick@motivo@<tiempo> */
		if ((!Declaraciones.UserList[UserIndex].flags.Privilegios && PlayerType.RoleMaster) != 0
				&& (!Declaraciones.UserList[UserIndex].flags.Privilegios && PlayerType.User) != 0) {
			if (vb6.LenB(UserName) == 0 || vb6.LenB(Reason) == 0) {
				WriteConsoleMsg(UserIndex, "Utilice /carcel nick@motivo@tiempo", FontTypeNames.FONTTYPE_INFO);
			} else {
				tUser = Extra.NameIndex(UserName);

				if (tUser <= 0) {
					if ((ES.EsDios(UserName) || ES.EsAdmin(UserName))) {
						WriteConsoleMsg(UserIndex, "No puedes encarcelar a administradores.",
								FontTypeNames.FONTTYPE_INFO);
					} else {
						WriteConsoleMsg(UserIndex, "El usuario no está online.", FontTypeNames.FONTTYPE_INFO);
					}
				} else {
					if (!Declaraciones.UserList[tUser].flags.Privilegios && PlayerType.User) {
						WriteConsoleMsg(UserIndex, "No puedes encarcelar a administradores.",
								FontTypeNames.FONTTYPE_INFO);
					} else if (jailTime > 60) {
						WriteConsoleMsg(UserIndex, "No puedés encarcelar por más de 60 minutos.",
								FontTypeNames.FONTTYPE_INFO);
					} else {
						if ((vb6.InStrB(UserName, "\\") != 0)) {
							UserName = vb6.Replace(UserName, "\\", "");
						}
						if ((vb6.InStrB(UserName, "/") != 0)) {
							UserName = vb6.Replace(UserName, "/", "");
						}

						if (General.FileExist(Declaraciones.CharPath + UserName + ".chr", 0)) {
							Count = vb6.val(ES.GetVar(Declaraciones.CharPath + UserName + ".chr", "PENAS", "Cant"));
							ES.WriteVar(Declaraciones.CharPath + UserName + ".chr", "PENAS", "Cant", Count + 1);
							ES.WriteVar(Declaraciones.CharPath + UserName + ".chr", "PENAS", "P" + Count + 1,
									vb6.LCase(Declaraciones.UserList[UserIndex].Name) + ": CARCEL " + jailTime
											+ "m, MOTIVO: " + vb6.LCase(Reason) + " " + Date + " " + vb6.time());
						}

						Admin.Encarcelar(tUser, jailTime, Declaraciones.UserList[UserIndex].Name);
						General.LogGM(Declaraciones.UserList[UserIndex].Name, " encarceló a " + UserName);
					}
				}
			}
		}

		/*
		 * 'If we got here then packet is complete, copy data back to original
		 * queue
		 */
		Declaraciones.UserList[UserIndex].incomingData.CopyBuffer(buffer);

		/* FIXME: ErrHandler : */
		int ERROR;
		/* ERROR = Err . Number */
		/* FIXME: ON ERROR GOTO 0 */

		/* 'Destroy auxiliar buffer */
		buffer = null;

		if (ERROR != 0) {
			throw new RuntimeException("Err . Raise ERROR");
		}
	}

	/* '' */
	/* ' Handles the "KillNPC" message. */
	/* ' */
	/* ' @param userIndex The index of the user sending the message. */

	static void HandleKillNPC(int UserIndex) {
		/* '*************************************************** */
		/* 'Author: Juan Martín Sotuyo Dodero (Maraxus) */
		/* 'Last Modification: 04/22/08 (NicoNZ) */
		/* ' */
		/* '*************************************************** */
		/* 'Remove packet ID */
		Declaraciones.UserList[UserIndex].incomingData.ReadByte();

		if (Declaraciones.UserList[UserIndex].flags.Privilegios && PlayerType.User) {
			return;
		}

		int tNPC;
		Declaraciones.npc auxNPC;

		/* 'Los consejeros no pueden RMATAr a nada en el mapa pretoriano */
		if (Declaraciones.UserList[UserIndex].flags.Privilegios && PlayerType.Consejero) {
			if (Declaraciones.UserList[UserIndex].Pos.Map == PraetoriansCoopNPC.MAPA_PRETORIANO) {
				WriteConsoleMsg(UserIndex, "Los consejeros no pueden usar este comando en el mapa pretoriano.",
						FontTypeNames.FONTTYPE_INFO);
				return;
			}
		}

		tNPC = Declaraciones.UserList[UserIndex].flags.TargetNPC;

		if (tNPC > 0) {
			WriteConsoleMsg(UserIndex, "RMatas (con posible respawn) a: " + Declaraciones.Npclist[tNPC].Name,
					FontTypeNames.FONTTYPE_INFO);

			auxNPC = Declaraciones.Npclist[tNPC];
			NPCs.QuitarNPC(tNPC);
			NPCs.ReSpawnNpc(auxNPC);

			Declaraciones.UserList[UserIndex].flags.TargetNPC = 0;
		} else {
			WriteConsoleMsg(UserIndex, "Antes debes hacer click sobre el NPC.", FontTypeNames.FONTTYPE_INFO);
		}
	}

	/* '' */
	/* ' Handles the "WarnUser" message. */
	/* ' */
	/* ' @param userIndex The index of the user sending the message. */

	static void HandleWarnUser(int UserIndex) {
		/* '*************************************************** */
		/* 'Author: Nicolas Matias Gonzalez (NIGO) */
		/* 'Last Modification: 12/26/06 */
		/* ' */
		/* '*************************************************** */
		if (Declaraciones.UserList[UserIndex].incomingData.length < 5) {
			throw new RuntimeException("Err . Raise UserList ( UserIndex ) . incomingData . NotEnoughDataErrCode");
			return;
		}

		/* FIXME: ON ERROR GOTO ErrHandler */
		/*
		 * 'This packet contains strings, make a copy of the data to prevent
		 * losses if it's not complete yet...
		 */
		clsByteQueue buffer;
		buffer = new clsByteQueue();
		buffer.CopyBuffer(Declaraciones.UserList[UserIndex].incomingData);

		/* 'Remove packet ID */
		buffer.ReadByte();

		String UserName;
		String Reason;
		PlayerType Privs;
		int Count;

		UserName = buffer.ReadASCIIString();
		Reason = buffer.ReadASCIIString();

		if ((!Declaraciones.UserList[UserIndex].flags.Privilegios && PlayerType.RoleMaster) != 0
				&& (!Declaraciones.UserList[UserIndex].flags.Privilegios && PlayerType.User) != 0) {
			if (vb6.LenB(UserName) == 0 || vb6.LenB(Reason) == 0) {
				WriteConsoleMsg(UserIndex, "Utilice /advertencia nick@motivo", FontTypeNames.FONTTYPE_INFO);
			} else {
				Privs = Admin.UserDarPrivilegioLevel(UserName);

				if (!Privs && PlayerType.User) {
					WriteConsoleMsg(UserIndex, "No puedes advertir a administradores.", FontTypeNames.FONTTYPE_INFO);
				} else {
					if ((vb6.InStrB(UserName, "\\") != 0)) {
						UserName = vb6.Replace(UserName, "\\", "");
					}
					if ((vb6.InStrB(UserName, "/") != 0)) {
						UserName = vb6.Replace(UserName, "/", "");
					}

					if (General.FileExist(Declaraciones.CharPath + UserName + ".chr", 0)) {
						Count = vb6.val(ES.GetVar(Declaraciones.CharPath + UserName + ".chr", "PENAS", "Cant"));
						ES.WriteVar(Declaraciones.CharPath + UserName + ".chr", "PENAS", "Cant", Count + 1);
						ES.WriteVar(Declaraciones.CharPath + UserName + ".chr", "PENAS", "P" + Count + 1,
								vb6.LCase(Declaraciones.UserList[UserIndex].Name) + ": ADVERTENCIA por: "
										+ vb6.LCase(Reason) + " " + Date + " " + vb6.time());

						WriteConsoleMsg(UserIndex, "Has advertido a " + vb6.UCase(UserName) + ".",
								FontTypeNames.FONTTYPE_INFO);
						General.LogGM(Declaraciones.UserList[UserIndex].Name, " advirtio a " + UserName);
					}
				}
			}
		}

		/*
		 * 'If we got here then packet is complete, copy data back to original
		 * queue
		 */
		Declaraciones.UserList[UserIndex].incomingData.CopyBuffer(buffer);

		/* FIXME: ErrHandler : */
		int ERROR;
		/* ERROR = Err . Number */
		/* FIXME: ON ERROR GOTO 0 */

		/* 'Destroy auxiliar buffer */
		buffer = null;

		if (ERROR != 0) {
			throw new RuntimeException("Err . Raise ERROR");
		}
	}

	/* '' */
	/* ' Handles the "EditChar" message. */
	/* ' */
	/* ' @param userIndex The index of the user sending the message. */

	static void HandleEditChar(int UserIndex) {
 /* '*************************************************** */
 /* 'Author: Nicolas Matias Gonzalez (NIGO) */
 /* 'Last Modification: 18/09/2010 */
 /* '02/03/2009: ZaMa - Cuando editas nivel, chequea si el pj puede permanecer en clan faccionario */
 /* '11/06/2009: ZaMa - Todos los comandos se pueden usar aunque el pj este offline */
 /* '18/09/2010: ZaMa - Ahora se puede editar la vida del propio pj (cualquier rm o dios). */
 /* '*************************************************** */
  if (Declaraciones.UserList[UserIndex].incomingData.length<8) {
  throw new RuntimeException("Err . Raise UserList ( UserIndex ) . incomingData . NotEnoughDataErrCode");
  return;
 }
 
 /* FIXME: ON ERROR GOTO ErrHandler */
  /* 'This packet contains strings, make a copy of the data to prevent losses if it's not complete yet... */
  clsByteQueue buffer;
  buffer = new clsByteQueue();
  buffer.CopyBuffer(Declaraciones.UserList[UserIndex].incomingData);
  
  /* 'Remove packet ID */
  buffer.ReadByte();
  
  String UserName;
  int tUser;
  int opcion;
  String Arg1;
  String Arg2;
  boolean valido;
  int LoopC;
  String CommandString;
  int N;
  String UserCharPath;
  int Var;
  
  UserName = vb6.Replace(buffer.ReadASCIIString(), "+", " ");
  
   if (vb6.UCase(UserName) == "YO") {
   tUser = UserIndex;
   } else {
   tUser = Extra.NameIndex(UserName);
  }
  
  opcion = buffer.ReadByte();
  Arg1 = buffer.ReadASCIIString();
  Arg2 = buffer.ReadASCIIString();
  
   if (Declaraciones.UserList[UserIndex].flags.Privilegios && PlayerType.RoleMaster) {
   switch (Declaraciones.UserList[UserIndex].flags.Privilegios && (PlayerType.Dios || PlayerType.SemiDios || PlayerType.Consejero)) {
    case Consejero:
    /* ' Los RMs consejeros sólo se pueden editar su head, body, level y vida */
    valido = tUser == UserIndex && (opcion == eEditOptions.eo_Body || opcion == eEditOptions.eo_Head || opcion == eEditOptions.eo_Level || opcion == eEditOptions.eo_Vida);
    
    break;
    
    case SemiDios:
    /* ' Los RMs sólo se pueden editar su level o vida y el head y body de cualquiera */
    valido = ((opcion == eEditOptions.eo_Level || opcion == eEditOptions.eo_Vida) && tUser == UserIndex) || opcion == eEditOptions.eo_Body || opcion == eEditOptions.eo_Head;
    
    break;
    
    case Dios:
    /* ' Los DRMs pueden aplicar los siguientes comandos sobre cualquiera */
    /* ' pero si quiere modificar el level o vida sólo lo puede hacer sobre sí mismo */
    valido = ((opcion == eEditOptions.eo_Level || opcion == eEditOptions.eo_Vida) && tUser == UserIndex) || opcion == eEditOptions.eo_Body || opcion == eEditOptions.eo_Head || opcion == eEditOptions.eo_CiticensKilled || opcion == eEditOptions.eo_CriminalsKilled || opcion == eEditOptions.eo_Class || opcion == eEditOptions.eo_Skills || opcion == eEditOptions.eo_addGold;
   break;
   }
   
   /* 'Si no es RM debe ser dios para poder usar este comando */
   } else if (Declaraciones.UserList[UserIndex].flags.Privilegios && (PlayerType.Admin || PlayerType.Dios)) {
   
    if (opcion == eEditOptions.eo_Vida) {
    /* '  Por ahora dejo para que los dioses no puedan editar la vida de otros */
    valido = (tUser == UserIndex);
    } else {
    valido = true;
   }
   
   } else if ((Declaraciones.UserList[UserIndex].flags.Privilegios && PlayerType.SemiDios)) {
   
   valido = (opcion == eEditOptions.eo_Poss || ((opcion == eEditOptions.eo_Vida) && (tUser == UserIndex)));
   
    if (Declaraciones.UserList[UserIndex].flags.PrivEspecial) {
    valido = valido || (opcion == eEditOptions.eo_CiticensKilled) || (opcion == eEditOptions.eo_CriminalsKilled);
   }
   
   } else if ((Declaraciones.UserList[UserIndex].flags.Privilegios && PlayerType.Consejero)) {
   valido = ((opcion == eEditOptions.eo_Vida) && (tUser == UserIndex));
  }
  
   if (valido) {
   UserCharPath = Declaraciones.CharPath + UserName + ".chr";
    if (tUser<=0 && !General.FileExist(UserCharPath)) {
    WriteConsoleMsg(UserIndex, "Estás intentando editar un usuario inexistente.", FontTypeNames.FONTTYPE_INFO);
    General.LogGM(Declaraciones.UserList[UserIndex].Name, "Intentó editar un usuario inexistente.");
    } else {
    /* 'For making the Log */
    CommandString = "/MOD ";
    
    switch (opcion) {
     case eo_Gold:
      if (vb6.val(Arg1)<=Declaraciones.MAX_ORO_EDIT) {
      /* ' Esta offline? */
       if (tUser<=0) {
       ES.WriteVar(UserCharPath, "STATS", "GLD", vb6.val(Arg1));
       WriteConsoleMsg(UserIndex, "Charfile Alterado: " + UserName, FontTypeNames.FONTTYPE_INFO);
       /* ' Online */
       } else {
       Declaraciones.UserList[tUser].Stats.GLD = vb6.val(Arg1);
       WriteUpdateGold(tUser);
      }
      } else {
      WriteConsoleMsg(UserIndex, "No está permitido utilizar valores mayores a " + Declaraciones.MAX_ORO_EDIT + ". Su comando ha quedado en los logs del juego.", FontTypeNames.FONTTYPE_INFO);
     }
     
     /* ' Log it */
     CommandString = CommandString + "ORO ";
     
     break;
     
     case eo_Experience:
      if (vb6.val(Arg1)>20000000) {
      Arg1 = 20000000;
     }
     
     /* ' Offline */
      if (tUser<=0) {
      Var = ES.GetVar(UserCharPath, "STATS", "EXP");
      ES.WriteVar(UserCharPath, "STATS", "EXP", Var+vb6.val(Arg1));
      WriteConsoleMsg(UserIndex, "Charfile Alterado: " + UserName, FontTypeNames.FONTTYPE_INFO);
      /* ' Online */
      } else {
      Declaraciones.UserList[tUser].Stats.Exp = Declaraciones.UserList[tUser].Stats.Exp+vb6.val(Arg1);
      UsUaRiOs.CheckUserLevel(tUser);
      WriteUpdateExp(tUser);
     }
     
     /* ' Log it */
     CommandString = CommandString + "EXP ";
     
     break;
     
     case eo_Body:
      if (tUser<=0) {
      ES.WriteVar(UserCharPath, "INIT", "Body", Arg1);
      WriteConsoleMsg(UserIndex, "Charfile Alterado: " + UserName, FontTypeNames.FONTTYPE_INFO);
      } else {
      UsUaRiOs.ChangeUserChar(tUser, vb6.val(Arg1), Declaraciones.UserList[tUser].Char.Head, Declaraciones.UserList[tUser].Char.heading, Declaraciones.UserList[tUser].Char.WeaponAnim, Declaraciones.UserList[tUser].Char.ShieldAnim, Declaraciones.UserList[tUser].Char.CascoAnim);
     }
     
     /* ' Log it */
     CommandString = CommandString + "BODY ";
     
     break;
     
     case eo_Head:
      if (tUser<=0) {
      ES.WriteVar(UserCharPath, "INIT", "Head", Arg1);
      WriteConsoleMsg(UserIndex, "Charfile Alterado: " + UserName, FontTypeNames.FONTTYPE_INFO);
      } else {
      UsUaRiOs.ChangeUserChar(tUser, Declaraciones.UserList[tUser].Char.body, vb6.val(Arg1), Declaraciones.UserList[tUser].Char.heading, Declaraciones.UserList[tUser].Char.WeaponAnim, Declaraciones.UserList[tUser].Char.ShieldAnim, Declaraciones.UserList[tUser].Char.CascoAnim);
     }
     
     /* ' Log it */
     CommandString = CommandString + "HEAD ";
     
     break;
     
     case eo_CriminalsKilled:
     Var = vb6.IIf(vb6.val(Arg1)>Declaraciones.MAXUSERMATADOS, Declaraciones.MAXUSERMATADOS, vb6.val(Arg1));
     
     /* ' Offline */
      if (tUser<=0) {
      ES.WriteVar(UserCharPath, "FACCIONES", "CrimMatados", Var);
      WriteConsoleMsg(UserIndex, "Charfile Alterado: " + UserName, FontTypeNames.FONTTYPE_INFO);
      /* ' Online */
      } else {
      Declaraciones.UserList[tUser].Faccion.CriminalesMatados = Var;
     }
     
     /* ' Log it */
     CommandString = CommandString + "CRI ";
     
     break;
     
     case eo_CiticensKilled:
     Var = vb6.IIf(vb6.val(Arg1)>Declaraciones.MAXUSERMATADOS, Declaraciones.MAXUSERMATADOS, vb6.val(Arg1));
     
     /* ' Offline */
      if (tUser<=0) {
      ES.WriteVar(UserCharPath, "FACCIONES", "CiudMatados", Var);
      WriteConsoleMsg(UserIndex, "Charfile Alterado: " + UserName, FontTypeNames.FONTTYPE_INFO);
      /* ' Online */
      } else {
      Declaraciones.UserList[tUser].Faccion.CiudadanosMatados = Var;
     }
     
     /* ' Log it */
     CommandString = CommandString + "CIU ";
     
     break;
     
     case eo_Level:
      if (vb6.val(Arg1)>Declaraciones.STAT_MAXELV) {
      Arg1 = vb6.CStr(Declaraciones.STAT_MAXELV);
      WriteConsoleMsg(UserIndex, "No puedes tener un nivel superior a " + Declaraciones.STAT_MAXELV + ".", Declaraciones.FONTTYPE_INFO);
     }
     
     /* ' Chequeamos si puede permanecer en el clan */
      if (vb6.val(Arg1)>=25) {
      
      int GI;
       if (tUser<=0) {
       GI = ES.GetVar(UserCharPath, "GUILD", "GUILDINDEX");
       } else {
       GI = Declaraciones.UserList[tUser].GuildIndex;
      }
      
       if (GI>0) {
        if (modGuilds.GuildAlignment(GI) == "Del Mal" || modGuilds.GuildAlignment(GI) == "Real") {
        /* 'We get here, so guild has factionary alignment, we have to expulse the user */
        modGuilds.m_EcharMiembroDeClan(-1, UserName);
        
        modSendData.SendData(SendTarget.ToGuildMembers, GI, PrepareMessageConsoleMsg(UserName + " deja el clan.", FontTypeNames.FONTTYPE_GUILD));
        /* ' Si esta online le avisamos */
        if (tUser>0) {
        WriteConsoleMsg(tUser, "¡Ya tienes la madurez suficiente como para decidir bajo que estandarte pelearás! Por esta razón, hasta tanto no te enlistes en la facción bajo la cual tu clan está alineado, estarás excluído del mismo.", FontTypeNames.FONTTYPE_GUILD);
        }
       }
      }
     }
     
     /* ' Offline */
      if (tUser<=0) {
      ES.WriteVar(UserCharPath, "STATS", "ELV", vb6.val(Arg1));
      WriteConsoleMsg(UserIndex, "Charfile Alterado: " + UserName, FontTypeNames.FONTTYPE_INFO);
      /* ' Online */
      } else {
      Declaraciones.UserList[tUser].Stats.ELV = vb6.val(Arg1);
      WriteUpdateUserStats(tUser);
     }
     
     /* ' Log it */
     CommandString = CommandString + "LEVEL ";
     
     break;
     
     case eo_Class:
      for (LoopC = (1); LoopC <= (Declaraciones.NUMCLASES); LoopC++) {
      if (vb6.UCase(Declaraciones.ListaClases[LoopC]) == vb6.UCase(Arg1)) {
      break; /* FIXME: EXIT FOR */
      }
     }
     
      if (LoopC>Declaraciones.NUMCLASES) {
      WriteConsoleMsg(UserIndex, "Clase desconocida. Intente nuevamente.", FontTypeNames.FONTTYPE_INFO);
      } else {
      /* ' Offline */
       if (tUser<=0) {
       ES.WriteVar(UserCharPath, "INIT", "Clase", LoopC);
       WriteConsoleMsg(UserIndex, "Charfile Alterado: " + UserName, FontTypeNames.FONTTYPE_INFO);
       /* ' Online */
       } else {
       Declaraciones.UserList[tUser].clase = LoopC;
      }
     }
     
     /* ' Log it */
     CommandString = CommandString + "CLASE ";
     
     break;
     
     case eo_Skills:
      for (LoopC = (1); LoopC <= (Declaraciones.NUMSKILLS); LoopC++) {
      if (vb6.UCase(vb6.Replace(Declaraciones.SkillsNames[LoopC], " ", "+")) == vb6.UCase(Arg1)) {
      break; /* FIXME: EXIT FOR */
      }
     }
     
      if (LoopC>Declaraciones.NUMSKILLS) {
      WriteConsoleMsg(UserIndex, "Skill Inexistente!", FontTypeNames.FONTTYPE_INFO);
      } else {
      /* ' Offline */
       if (tUser<=0) {
       ES.WriteVar(UserCharPath, "Skills", "SK" + LoopC, Arg2);
       ES.WriteVar(UserCharPath, "Skills", "EXPSK" + LoopC, 0);
       
        if (Arg2<Declaraciones.MAXSKILLPOINTS) {
        ES.WriteVar(UserCharPath, "Skills", "ELUSK" + LoopC, Declaraciones.ELU_SKILL_INICIAL*1.05 $ Arg2);
        } else {
        ES.WriteVar(UserCharPath, "Skills", "ELUSK" + LoopC, 0);
       }
       
       WriteConsoleMsg(UserIndex, "Charfile Alterado: " + UserName, FontTypeNames.FONTTYPE_INFO);
       /* ' Online */
       } else {
       Declaraciones.UserList[tUser].Stats.UserSkills[LoopC] = vb6.val(Arg2);
       UsUaRiOs.CheckEluSkill(tUser, LoopC, true);
      }
     }
     
     /* ' Log it */
     CommandString = CommandString + "SKILLS ";
     
     break;
     
     case eo_SkillPointsLeft:
     /* ' Offline */
      if (tUser<=0) {
      ES.WriteVar(UserCharPath, "STATS", "SkillPtsLibres", Arg1);
      WriteConsoleMsg(UserIndex, "Charfile Alterado: " + UserName, FontTypeNames.FONTTYPE_INFO);
      /* ' Online */
      } else {
      Declaraciones.UserList[tUser].Stats.SkillPts = vb6.val(Arg1);
     }
     
     /* ' Log it */
     CommandString = CommandString + "SKILLSLIBRES ";
     
     break;
     
     case eo_Nobleza:
     Var = vb6.IIf(vb6.val(Arg1)>Declaraciones.MAXREP, Declaraciones.MAXREP, vb6.val(Arg1));
     
     /* ' Offline */
      if (tUser<=0) {
      ES.WriteVar(UserCharPath, "REP", "Nobles", Var);
      WriteConsoleMsg(UserIndex, "Charfile Alterado: " + UserName, FontTypeNames.FONTTYPE_INFO);
      /* ' Online */
      } else {
      Declaraciones.UserList[tUser].Reputacion.NobleRep = Var;
     }
     
     /* ' Log it */
     CommandString = CommandString + "NOB ";
     
     break;
     
     case eo_Asesino:
     Var = vb6.IIf(vb6.val(Arg1)>Declaraciones.MAXREP, Declaraciones.MAXREP, vb6.val(Arg1));
     
     /* ' Offline */
      if (tUser<=0) {
      ES.WriteVar(UserCharPath, "REP", "Asesino", Var);
      WriteConsoleMsg(UserIndex, "Charfile Alterado: " + UserName, FontTypeNames.FONTTYPE_INFO);
      /* ' Online */
      } else {
      Declaraciones.UserList[tUser].Reputacion.AsesinoRep = Var;
     }
     
     /* ' Log it */
     CommandString = CommandString + "ASE ";
     
     break;
     
     case eo_Sex:
     int Sex;
     /* ' Mujer? */
     Sex = vb6.IIf(vb6.UCase(Arg1) == "MUJER", eGenero.Mujer, 0);
     /* ' Hombre? */
     Sex = vb6.IIf(vb6.UCase(Arg1) == "HOMBRE", eGenero.Hombre, Sex);
     
     /* ' Es Hombre o mujer? */
      if (Sex != 0) {
      /* ' OffLine */
       if (tUser<=0) {
       ES.WriteVar(UserCharPath, "INIT", "Genero", Sex);
       WriteConsoleMsg(UserIndex, "Charfile Alterado: " + UserName, FontTypeNames.FONTTYPE_INFO);
       /* ' Online */
       } else {
       Declaraciones.UserList[tUser].Genero = Sex;
      }
      } else {
      WriteConsoleMsg(UserIndex, "Genero desconocido. Intente nuevamente.", FontTypeNames.FONTTYPE_INFO);
     }
     
     /* ' Log it */
     CommandString = CommandString + "SEX ";
     
     break;
     
     case eo_Raza:
     int raza;
     
     Arg1 = vb6.UCase(Arg1);
     switch (Arg1) {
      case "HUMANO":
      raza = eRaza.Humano;
      break;
      
      case "ELFO":
      raza = eRaza.Elfo;
      break;
      
      case "DROW":
      raza = eRaza.Drow;
      break;
      
      case "ENANO":
      raza = eRaza.Enano;
      break;
      
      case "GNOMO":
      raza = eRaza.Gnomo;
      break;
      
      default:
      raza = 0;
     break;
     }
     
      if (raza == 0) {
      WriteConsoleMsg(UserIndex, "Raza desconocida. Intente nuevamente.", FontTypeNames.FONTTYPE_INFO);
      } else {
       if (tUser<=0) {
       ES.WriteVar(UserCharPath, "INIT", "Raza", raza);
       WriteConsoleMsg(UserIndex, "Charfile Alterado: " + UserName, FontTypeNames.FONTTYPE_INFO);
       } else {
       Declaraciones.UserList[tUser].raza = raza;
      }
     }
     
     /* ' Log it */
     CommandString = CommandString + "RAZA ";
     
     case eo_addGold:
     
     int bankGold;
     
      if (vb6.Abs(Arg1)>Declaraciones.MAX_ORO_EDIT) {
      WriteConsoleMsg(UserIndex, "No está permitido utilizar valores mayores a " + Declaraciones.MAX_ORO_EDIT + ".", FontTypeNames.FONTTYPE_INFO);
      } else {
       if (tUser<=0) {
       bankGold = ES.GetVar(Declaraciones.CharPath + UserName + ".chr", "STATS", "BANCO");
       ES.WriteVar(UserCharPath, "STATS", "BANCO", vb6.IIf(bankGold+vb6.val(Arg1)<=0, 0, bankGold+vb6.val(Arg1)));
       WriteConsoleMsg(UserIndex, "Se le ha agregado " + Arg1 + " monedas de oro a " + UserName + ".", Declaraciones.FONTTYPE_TALK);
       } else {
       Declaraciones.UserList[tUser].Stats.Banco = vb6.IIf(Declaraciones.UserList[tUser].Stats.Banco+vb6.val(Arg1)<=0, 0, Declaraciones.UserList[tUser].Stats.Banco+vb6.val(Arg1));
       WriteConsoleMsg(tUser, Declaraciones.STANDARD_BOUNTY_HUNTER_MESSAGE, Declaraciones.FONTTYPE_TALK);
      }
     }
     
     /* ' Log it */
     CommandString = CommandString + "AGREGAR ";
     
     break;
     
     case eo_Vida:
     
      if (vb6.val(Arg1)>Declaraciones.MAX_VIDA_EDIT) {
      Arg1 = vb6.CStr(Declaraciones.MAX_VIDA_EDIT);
      WriteConsoleMsg(UserIndex, "No puedes tener vida superior a " + Declaraciones.MAX_VIDA_EDIT + ".", Declaraciones.FONTTYPE_INFO);
     }
     
     /* ' No valido si esta offline, porque solo se puede editar a si mismo */
     Declaraciones.UserList[tUser].Stats.MaxHp = vb6.val(Arg1);
     Declaraciones.UserList[tUser].Stats.MinHp = vb6.val(Arg1);
     
     WriteUpdateUserStats(tUser);
     
     /* ' Log it */
     CommandString = CommandString + "VIDA ";
     
     break;
     
     case eo_Poss:
     
     int Map;
     int X;
     int Y;
     
     Map = vb6.val(General.ReadField(1, Arg1, 45));
     X = vb6.val(General.ReadField(2, Arg1, 45));
     Y = vb6.val(General.ReadField(3, Arg1, 45));
     
      if (Extra.InMapBounds(Map, X, Y)) {
      
       if (tUser<=0) {
       ES.WriteVar(UserCharPath, "INIT", "POSITION", Map + "-" + X + "-" + Y);
       WriteConsoleMsg(UserIndex, "Charfile Alterado: " + UserName, FontTypeNames.FONTTYPE_INFO);
       } else {
       UsUaRiOs.WarpUserChar(tUser, Map, X, Y, true, true);
       WriteConsoleMsg(UserIndex, "Usuario teletransportado: " + UserName, FontTypeNames.FONTTYPE_INFO);
      }
      } else {
      WriteConsoleMsg(UserIndex, "Posición inválida", Declaraciones.FONTTYPE_INFO);
     }
     
     /* ' Log it */
     CommandString = CommandString + "POSS ";
     
     break;
     
     default:
     WriteConsoleMsg(UserIndex, "Comando no permitido.", FontTypeNames.FONTTYPE_INFO);
     CommandString = CommandString + "UNKOWN ";
     
    break;
    }
    
    CommandString = CommandString + Arg1 + " " + Arg2;
    General.LogGM(Declaraciones.UserList[UserIndex].Name, CommandString + " " + UserName);
    
   }
  }
  
  /* 'If we got here then packet is complete, copy data back to original queue */
  Declaraciones.UserList[UserIndex].incomingData.CopyBuffer(buffer);
  
 /* FIXME: ErrHandler : */
 int ERROR;
 /* ERROR = Err . Number */
 /* FIXME: ON ERROR GOTO 0 */
 
 /* 'Destroy auxiliar buffer */
 buffer = null;
 
 if (ERROR != 0) {
 throw new RuntimeException("Err . Raise ERROR");
 }
}

	/* '' */
	/* ' Handles the "RequestCharInfo" message. */
	/* ' */
	/* ' @param userIndex The index of the user sending the message. */

	static void HandleRequestCharInfo(int UserIndex) {
		/* '*************************************************** */
		/* 'Author: Fredy Horacio Treboux (liquid) */
		/* 'Last Modification: 01/08/07 */
		/* 'Last Modification by: (liquid).. alto bug zapallo.. */
		/* '*************************************************** */
		if (Declaraciones.UserList[UserIndex].incomingData.length < 3) {
			throw new RuntimeException("Err . Raise UserList ( UserIndex ) . incomingData . NotEnoughDataErrCode");
			return;
		}

		/* FIXME: ON ERROR GOTO ErrHandler */
		/*
		 * 'This packet contains strings, make a copy of the data to prevent
		 * losses if it's not complete yet...
		 */
		clsByteQueue buffer;
		buffer = new clsByteQueue();
		buffer.CopyBuffer(Declaraciones.UserList[UserIndex].incomingData);

		/* 'Remove packet ID */
		buffer.ReadByte();

		String TargetName;
		int TargetIndex;

		TargetName = vb6.Replace(buffer.ReadASCIIString(), "+", " ");
		TargetIndex = Extra.NameIndex(TargetName);

		if (Declaraciones.UserList[UserIndex].flags.Privilegios
				&& (PlayerType.Admin || PlayerType.Dios || PlayerType.SemiDios)) {
			/* 'is the player offline? */
			if (TargetIndex <= 0) {
				/* 'don't allow to retrieve administrator's info */
				if (!(ES.EsDios(TargetName) || ES.EsAdmin(TargetName))) {
					WriteConsoleMsg(UserIndex, "Usuario offline, buscando en charfile.", FontTypeNames.FONTTYPE_INFO);
					UsUaRiOs.SendUserStatsTxtOFF(UserIndex, TargetName);
				}
			} else {
				/* 'don't allow to retrieve administrator's info */
				if (Declaraciones.UserList[TargetIndex].flags.Privilegios
						&& (PlayerType.User || PlayerType.Consejero || PlayerType.SemiDios)) {
					UsUaRiOs.SendUserStatsTxt(UserIndex, TargetIndex);
				}
			}
		}

		/*
		 * 'If we got here then packet is complete, copy data back to original
		 * queue
		 */
		Declaraciones.UserList[UserIndex].incomingData.CopyBuffer(buffer);

		/* FIXME: ErrHandler : */
		int ERROR;
		/* ERROR = Err . Number */
		/* FIXME: ON ERROR GOTO 0 */

		/* 'Destroy auxiliar buffer */
		buffer = null;

		if (ERROR != 0) {
			throw new RuntimeException("Err . Raise ERROR");
		}
	}

	/* '' */
	/* ' Handles the "RequestCharStats" message. */
	/* ' */
	/* ' @param userIndex The index of the user sending the message. */

	static void HandleRequestCharStats(int UserIndex) {
		/* '*************************************************** */
		/* 'Author: Nicolas Matias Gonzalez (NIGO) */
		/* 'Last Modification: 07/06/2010 */
		/*
		 * '07/06/2010: ZaMa - Ahora no se puede usar para saber si hay
		 * dioses/admins online.
		 */
		/* '*************************************************** */
		if (Declaraciones.UserList[UserIndex].incomingData.length < 3) {
			throw new RuntimeException("Err . Raise UserList ( UserIndex ) . incomingData . NotEnoughDataErrCode");
			return;
		}

		/* FIXME: ON ERROR GOTO ErrHandler */
		/*
		 * 'This packet contains strings, make a copy of the data to prevent
		 * losses if it's not complete yet...
		 */
		clsByteQueue buffer;
		buffer = new clsByteQueue();
		buffer.CopyBuffer(Declaraciones.UserList[UserIndex].incomingData);

		/* 'Remove packet ID */
		buffer.ReadByte();

		String UserName;
		int tUser;

		boolean UserIsAdmin;
		boolean OtherUserIsAdmin;

		UserName = buffer.ReadASCIIString();

		UserIsAdmin = (Declaraciones.UserList[UserIndex].flags.Privilegios
				&& (PlayerType.Admin || PlayerType.Dios)) != 0;

		if ((!Declaraciones.UserList[UserIndex].flags.Privilegios && PlayerType.RoleMaster) != 0
				&& ((Declaraciones.UserList[UserIndex].flags.Privilegios && PlayerType.SemiDios) != 0 || UserIsAdmin)) {
			General.LogGM(Declaraciones.UserList[UserIndex].Name, "/STAT " + UserName);

			tUser = Extra.NameIndex(UserName);

			OtherUserIsAdmin = ES.EsDios(UserName) || ES.EsAdmin(UserName);

			if (tUser <= 0) {
				if (UserIsAdmin || !OtherUserIsAdmin) {
					WriteConsoleMsg(UserIndex, "Usuario offline. Leyendo charfile... ", FontTypeNames.FONTTYPE_INFO);

					UsUaRiOs.SendUserMiniStatsTxtFromChar(UserIndex, UserName);
				} else {
					WriteConsoleMsg(UserIndex, "No puedes ver los stats de un dios o admin.",
							FontTypeNames.FONTTYPE_INFO);
				}
			} else {
				if (UserIsAdmin || !OtherUserIsAdmin) {
					UsUaRiOs.SendUserMiniStatsTxt(UserIndex, tUser);
				} else {
					WriteConsoleMsg(UserIndex, "No puedes ver los stats de un dios o admin.",
							FontTypeNames.FONTTYPE_INFO);
				}
			}
		}

		/*
		 * 'If we got here then packet is complete, copy data back to original
		 * queue
		 */
		Declaraciones.UserList[UserIndex].incomingData.CopyBuffer(buffer);

		/* FIXME: ErrHandler : */
		int ERROR;
		/* ERROR = Err . Number */
		/* FIXME: ON ERROR GOTO 0 */

		/* 'Destroy auxiliar buffer */
		buffer = null;

		if (ERROR != 0) {
			throw new RuntimeException("Err . Raise ERROR");
		}
	}

	/* '' */
	/* ' Handles the "RequestCharGold" message. */
	/* ' */
	/* ' @param userIndex The index of the user sending the message. */

	static void HandleRequestCharGold(int UserIndex) {
		/* '*************************************************** */
		/* 'Author: Nicolas Matias Gonzalez (NIGO) */
		/* 'Last Modification: 07/06/2010 */
		/*
		 * '07/06/2010: ZaMa - Ahora no se puede usar para saber si hay
		 * dioses/admins online.
		 */
		/* '*************************************************** */
		if (Declaraciones.UserList[UserIndex].incomingData.length < 3) {
			throw new RuntimeException("Err . Raise UserList ( UserIndex ) . incomingData . NotEnoughDataErrCode");
			return;
		}

		/* FIXME: ON ERROR GOTO ErrHandler */
		/*
		 * 'This packet contains strings, make a copy of the data to prevent
		 * losses if it's not complete yet...
		 */
		clsByteQueue buffer;
		buffer = new clsByteQueue();
		buffer.CopyBuffer(Declaraciones.UserList[UserIndex].incomingData);

		/* 'Remove packet ID */
		buffer.ReadByte();

		String UserName;
		int tUser;

		boolean UserIsAdmin;
		boolean OtherUserIsAdmin;

		UserName = buffer.ReadASCIIString();

		UserIsAdmin = (Declaraciones.UserList[UserIndex].flags.Privilegios
				&& (PlayerType.Admin || PlayerType.Dios)) != 0;

		if ((Declaraciones.UserList[UserIndex].flags.Privilegios && PlayerType.SemiDios) || UserIsAdmin) {

			General.LogGM(Declaraciones.UserList[UserIndex].Name, "/BAL " + UserName);

			tUser = Extra.NameIndex(UserName);
			OtherUserIsAdmin = ES.EsDios(UserName) || ES.EsAdmin(UserName);

			tUser = Extra.NameIndex(UserName);
			OtherUserIsAdmin = ES.EsDios(UserName) || ES.EsAdmin(UserName);

			if (tUser <= 0) {
				if (UserIsAdmin || !OtherUserIsAdmin) {
					WriteConsoleMsg(UserIndex, "Usuario offline. Leyendo charfile... ", FontTypeNames.FONTTYPE_TALK);

					UsUaRiOs.SendUserOROTxtFromChar(UserIndex, UserName);
				} else {
					WriteConsoleMsg(UserIndex, "No puedes ver el oro de un dios o admin.", FontTypeNames.FONTTYPE_INFO);
				}
			} else {
				if (UserIsAdmin || !OtherUserIsAdmin) {
					WriteConsoleMsg(UserIndex, "El usuario " + UserName + " tiene "
							+ Declaraciones.UserList[tUser].Stats.Banco + " en el banco.", FontTypeNames.FONTTYPE_TALK);
				} else {
					WriteConsoleMsg(UserIndex, "No puedes ver el oro de un dios o admin.", FontTypeNames.FONTTYPE_INFO);
				}
			}
		}

		/*
		 * 'If we got here then packet is complete, copy data back to original
		 * queue
		 */
		Declaraciones.UserList[UserIndex].incomingData.CopyBuffer(buffer);

		/* FIXME: ErrHandler : */
		int ERROR;
		/* ERROR = Err . Number */
		/* FIXME: ON ERROR GOTO 0 */

		/* 'Destroy auxiliar buffer */
		buffer = null;

		if (ERROR != 0) {
			throw new RuntimeException("Err . Raise ERROR");
		}
	}

	/* '' */
	/* ' Handles the "RequestCharInventory" message. */
	/* ' */
	/* ' @param userIndex The index of the user sending the message. */

	static void HandleRequestCharInventory(int UserIndex) {
		/* '*************************************************** */
		/* 'Author: Nicolas Matias Gonzalez (NIGO) */
		/* 'Last Modification: 07/06/2010 */
		/*
		 * '07/06/2010: ZaMa - Ahora no se puede usar para saber si hay
		 * dioses/admins online.
		 */
		/* '*************************************************** */
		if (Declaraciones.UserList[UserIndex].incomingData.length < 3) {
			throw new RuntimeException("Err . Raise UserList ( UserIndex ) . incomingData . NotEnoughDataErrCode");
			return;
		}

		/* FIXME: ON ERROR GOTO ErrHandler */
		/*
		 * 'This packet contains strings, make a copy of the data to prevent
		 * losses if it's not complete yet...
		 */
		clsByteQueue buffer;
		buffer = new clsByteQueue();
		buffer.CopyBuffer(Declaraciones.UserList[UserIndex].incomingData);

		/* 'Remove packet ID */
		buffer.ReadByte();

		String UserName;
		int tUser;

		boolean UserIsAdmin;
		boolean OtherUserIsAdmin;

		UserName = buffer.ReadASCIIString();

		UserIsAdmin = (Declaraciones.UserList[UserIndex].flags.Privilegios
				&& (PlayerType.Admin || PlayerType.Dios)) != 0;

		if ((Declaraciones.UserList[UserIndex].flags.Privilegios
				&& (PlayerType.Admin || PlayerType.Dios || PlayerType.SemiDios))) {
			General.LogGM(Declaraciones.UserList[UserIndex].Name, "/INV " + UserName);

			tUser = Extra.NameIndex(UserName);
			OtherUserIsAdmin = ES.EsDios(UserName) || ES.EsAdmin(UserName);

			tUser = Extra.NameIndex(UserName);
			OtherUserIsAdmin = ES.EsDios(UserName) || ES.EsAdmin(UserName);

			if (tUser <= 0) {
				if (UserIsAdmin || !OtherUserIsAdmin) {
					WriteConsoleMsg(UserIndex, "Usuario offline. Leyendo del charfile...", FontTypeNames.FONTTYPE_TALK);

					UsUaRiOs.SendUserInvTxtFromChar(UserIndex, UserName);
				} else {
					WriteConsoleMsg(UserIndex, "No puedes ver el inventario de un dios o admin.",
							FontTypeNames.FONTTYPE_INFO);
				}
			} else {
				if (UserIsAdmin || !OtherUserIsAdmin) {
					UsUaRiOs.SendUserInvTxt(UserIndex, tUser);
				} else {
					WriteConsoleMsg(UserIndex, "No puedes ver el inventario de un dios o admin.",
							FontTypeNames.FONTTYPE_INFO);
				}
			}
		}

		/*
		 * 'If we got here then packet is complete, copy data back to original
		 * queue
		 */
		Declaraciones.UserList[UserIndex].incomingData.CopyBuffer(buffer);

		/* FIXME: ErrHandler : */
		int ERROR;
		/* ERROR = Err . Number */
		/* FIXME: ON ERROR GOTO 0 */

		/* 'Destroy auxiliar buffer */
		buffer = null;

		if (ERROR != 0) {
			throw new RuntimeException("Err . Raise ERROR");
		}
	}

	/* '' */
	/* ' Handles the "RequestCharBank" message. */
	/* ' */
	/* ' @param userIndex The index of the user sending the message. */

	static void HandleRequestCharBank(int UserIndex) {
		/* '*************************************************** */
		/* 'Author: Nicolas Matias Gonzalez (NIGO) */
		/* 'Last Modification: 07/06/2010 */
		/*
		 * '07/06/2010: ZaMa - Ahora no se puede usar para saber si hay
		 * dioses/admins online.
		 */
		/* '*************************************************** */
		if (Declaraciones.UserList[UserIndex].incomingData.length < 3) {
			throw new RuntimeException("Err . Raise UserList ( UserIndex ) . incomingData . NotEnoughDataErrCode");
			return;
		}

		/* FIXME: ON ERROR GOTO ErrHandler */
		/*
		 * 'This packet contains strings, make a copy of the data to prevent
		 * losses if it's not complete yet...
		 */
		clsByteQueue buffer;
		buffer = new clsByteQueue();
		buffer.CopyBuffer(Declaraciones.UserList[UserIndex].incomingData);

		/* 'Remove packet ID */
		buffer.ReadByte();

		String UserName;
		int tUser;

		boolean UserIsAdmin;
		boolean OtherUserIsAdmin;

		UserName = buffer.ReadASCIIString();

		UserIsAdmin = (Declaraciones.UserList[UserIndex].flags.Privilegios
				&& (PlayerType.Admin || PlayerType.Dios)) != 0;

		if ((Declaraciones.UserList[UserIndex].flags.Privilegios && PlayerType.SemiDios) != 0 || UserIsAdmin) {
			General.LogGM(Declaraciones.UserList[UserIndex].Name, "/BOV " + UserName);

			tUser = Extra.NameIndex(UserName);
			OtherUserIsAdmin = ES.EsDios(UserName) || ES.EsAdmin(UserName);

			tUser = Extra.NameIndex(UserName);
			OtherUserIsAdmin = ES.EsDios(UserName) || ES.EsAdmin(UserName);

			if (tUser <= 0) {
				if (UserIsAdmin || !OtherUserIsAdmin) {
					WriteConsoleMsg(UserIndex, "Usuario offline. Leyendo charfile... ", FontTypeNames.FONTTYPE_TALK);

					modBanco.SendUserBovedaTxtFromChar(UserIndex, UserName);
				} else {
					WriteConsoleMsg(UserIndex, "No puedes ver la bóveda de un dios o admin.",
							FontTypeNames.FONTTYPE_INFO);
				}
			} else {
				if (UserIsAdmin || !OtherUserIsAdmin) {
					modBanco.SendUserBovedaTxt(UserIndex, tUser);
				} else {
					WriteConsoleMsg(UserIndex, "No puedes ver la bóveda de un dios o admin.",
							FontTypeNames.FONTTYPE_INFO);
				}
			}
		}

		/*
		 * 'If we got here then packet is complete, copy data back to original
		 * queue
		 */
		Declaraciones.UserList[UserIndex].incomingData.CopyBuffer(buffer);

		/* FIXME: ErrHandler : */
		int ERROR;
		/* ERROR = Err . Number */
		/* FIXME: ON ERROR GOTO 0 */

		/* 'Destroy auxiliar buffer */
		buffer = null;

		if (ERROR != 0) {
			throw new RuntimeException("Err . Raise ERROR");
		}
	}

	/* '' */
	/* ' Handles the "RequestCharSkills" message. */
	/* ' */
	/* ' @param userIndex The index of the user sending the message. */

	static void HandleRequestCharSkills(int UserIndex) {
		/* '*************************************************** */
		/* 'Author: Nicolas Matias Gonzalez (NIGO) */
		/* 'Last Modification: 12/29/06 */
		/* ' */
		/* '*************************************************** */
		if (Declaraciones.UserList[UserIndex].incomingData.length < 3) {
			throw new RuntimeException("Err . Raise UserList ( UserIndex ) . incomingData . NotEnoughDataErrCode");
			return;
		}

		/* FIXME: ON ERROR GOTO ErrHandler */
		/*
		 * 'This packet contains strings, make a copy of the data to prevent
		 * losses if it's not complete yet...
		 */
		clsByteQueue buffer;
		buffer = new clsByteQueue();
		buffer.CopyBuffer(Declaraciones.UserList[UserIndex].incomingData);

		/* 'Remove packet ID */
		buffer.ReadByte();

		String UserName;
		int tUser;
		int LoopC;
		String message;

		UserName = buffer.ReadASCIIString();
		tUser = Extra.NameIndex(UserName);

		if ((Declaraciones.UserList[UserIndex].flags.Privilegios
				&& (PlayerType.Admin || PlayerType.Dios || PlayerType.SemiDios))) {
			General.LogGM(Declaraciones.UserList[UserIndex].Name, "/STATS " + UserName);

			if (tUser <= 0) {
				if ((vb6.InStrB(UserName, "\\") != 0)) {
					UserName = vb6.Replace(UserName, "\\", "");
				}
				if ((vb6.InStrB(UserName, "/") != 0)) {
					UserName = vb6.Replace(UserName, "/", "");
				}

				for (LoopC = (1); LoopC <= (Declaraciones.NUMSKILLS); LoopC++) {
					message = message + "CHAR>" + Declaraciones.SkillsNames[LoopC] + " = "
							+ ES.GetVar(Declaraciones.CharPath + UserName + ".chr", "SKILLS", "SK" + LoopC) + vbCrLf;
				}

				WriteConsoleMsg(UserIndex,
						message + "CHAR> Libres:"
								+ ES.GetVar(Declaraciones.CharPath + UserName + ".chr", "STATS", "SKILLPTSLIBRES"),
						FontTypeNames.FONTTYPE_INFO);
			} else {
				UsUaRiOs.SendUserSkillsTxt(UserIndex, tUser);
			}
		}

		/*
		 * 'If we got here then packet is complete, copy data back to original
		 * queue
		 */
		Declaraciones.UserList[UserIndex].incomingData.CopyBuffer(buffer);

		/* FIXME: ErrHandler : */
		int ERROR;
		/* ERROR = Err . Number */
		/* FIXME: ON ERROR GOTO 0 */

		/* 'Destroy auxiliar buffer */
		buffer = null;

		if (ERROR != 0) {
			throw new RuntimeException("Err . Raise ERROR");
		}
	}

	/* '' */
	/* ' Handles the "ReviveChar" message. */
	/* ' */
	/* ' @param userIndex The index of the user sending the message. */

	static void HandleReviveChar(int UserIndex) {
		/* '*************************************************** */
		/* 'Author: Nicolas Matias Gonzalez (NIGO) */
		/* 'Last Modification: 11/03/2010 */
		/*
		 * '11/03/2010: ZaMa - Al revivir con el comando, si esta navegando le
		 * da cuerpo e barca.
		 */
		/* '*************************************************** */
		if (Declaraciones.UserList[UserIndex].incomingData.length < 3) {
			throw new RuntimeException("Err . Raise UserList ( UserIndex ) . incomingData . NotEnoughDataErrCode");
			return;
		}

		/* FIXME: ON ERROR GOTO ErrHandler */
		/*
		 * 'This packet contains strings, make a copy of the data to prevent
		 * losses if it's not complete yet...
		 */
		clsByteQueue buffer;
		buffer = new clsByteQueue();
		buffer.CopyBuffer(Declaraciones.UserList[UserIndex].incomingData);

		/* 'Remove packet ID */
		buffer.ReadByte();

		String UserName;
		int tUser;
		int LoopC;

		UserName = buffer.ReadASCIIString();

		if ((Declaraciones.UserList[UserIndex].flags.Privilegios
				&& (PlayerType.Admin || PlayerType.Dios || PlayerType.SemiDios))) {
			if (vb6.UCase(UserName) != "YO") {
				tUser = Extra.NameIndex(UserName);
			} else {
				tUser = UserIndex;
			}

			if (tUser <= 0) {
				WriteConsoleMsg(UserIndex, "Usuario offline.", FontTypeNames.FONTTYPE_INFO);
			} else {
				/* 'If dead, show him alive (naked). */
				if (Declaraciones.UserList[tUser].flags.Muerto == 1) {
					Declaraciones.UserList[tUser].flags.Muerto = 0;

					if (Declaraciones.UserList[tUser].flags.Navegando == 1) {
						UsUaRiOs.ToggleBoatBody(tUser);
					} else {
						General.DarCuerpoDesnudo(tUser);
					}

					if (Declaraciones.UserList[tUser].flags.Traveling == 1) {
						Declaraciones.UserList[tUser].flags.Traveling = 0;
						Declaraciones.UserList[tUser].Counters.goHome = 0;
						WriteMultiMessage(tUser, eMessages.CancelHome);
					}

					UsUaRiOs.ChangeUserChar(tUser, Declaraciones.UserList[tUser].Char.body,
							Declaraciones.UserList[tUser].OrigChar.Head, Declaraciones.UserList[tUser].Char.heading,
							Declaraciones.UserList[tUser].Char.WeaponAnim,
							Declaraciones.UserList[tUser].Char.ShieldAnim,
							Declaraciones.UserList[tUser].Char.CascoAnim);

					WriteConsoleMsg(tUser, Declaraciones.UserList[UserIndex].Name + " te ha resucitado.",
							FontTypeNames.FONTTYPE_INFO);
				} else {
					WriteConsoleMsg(tUser, Declaraciones.UserList[UserIndex].Name + " te ha curado.",
							FontTypeNames.FONTTYPE_INFO);
				}

				Declaraciones.UserList[tUser].Stats.MinHp = Declaraciones.UserList[tUser].Stats.MaxHp;

				if (Declaraciones.UserList[tUser].flags.Traveling == 1) {
					Declaraciones.UserList[tUser].Counters.goHome = 0;
					Declaraciones.UserList[tUser].flags.Traveling = 0;
					WriteMultiMessage(tUser, eMessages.CancelHome);
				}

				WriteUpdateHP(tUser);

				FlushBuffer(tUser);

				General.LogGM(Declaraciones.UserList[UserIndex].Name, "Resucito a " + UserName);
			}
		}

		/*
		 * 'If we got here then packet is complete, copy data back to original
		 * queue
		 */
		Declaraciones.UserList[UserIndex].incomingData.CopyBuffer(buffer);

		/* FIXME: ErrHandler : */
		int ERROR;
		/* ERROR = Err . Number */
		/* FIXME: ON ERROR GOTO 0 */

		/* 'Destroy auxiliar buffer */
		buffer = null;

		if (ERROR != 0) {
			throw new RuntimeException("Err . Raise ERROR");
		}
	}

	/* '' */
	/* ' Handles the "OnlineGM" message. */
	/* ' */
	/* ' @param userIndex The index of the user sending the message. */

	static void HandleOnlineGM(int UserIndex) {
		/* '*************************************************** */
		/* 'Author: Fredy Horacio Treboux (liquid) */
		/* 'Last Modification: 12/28/06 */
		/* ' */
		/* '*************************************************** */
		int i;
		String list;
		PlayerType priv;
		boolean isRM;

		/* 'Remove packet ID */
		Declaraciones.UserList[UserIndex].incomingData.ReadByte();

		if (Declaraciones.UserList[UserIndex].flags.Privilegios && (PlayerType.User || PlayerType.Consejero)) {
			return;
		}

		priv = PlayerType.Consejero || PlayerType.SemiDios;
		if (Declaraciones.UserList[UserIndex].flags.Privilegios && (PlayerType.Dios || PlayerType.Admin)) {
			priv = priv || PlayerType.Dios || PlayerType.Admin;
		}

		isRM = ((Declaraciones.UserList[UserIndex].flags.Privilegios && PlayerType.RoleMaster) != 0);

		for (i = (1); i <= (Declaraciones.LastUser); i++) {
			if (Declaraciones.UserList[i].flags.UserLogged) {
				if (((Declaraciones.UserList[i].flags.Privilegios && priv) != 0)) {
					if (!(isRM
							&& (((Declaraciones.UserList[i].flags.Privilegios
									&& (PlayerType.Admin || PlayerType.Dios)) != 0))
							&& (Declaraciones.UserList[i].flags.Privilegios && PlayerType.RoleMaster) == 0)) {
						list = list + Declaraciones.UserList[i].Name + ", ";
					}
				}
			}
		}

		if (vb6.LenB(list) != 0) {
			list = vb6.Left(list, vb6.Len(list) - 2);
			WriteConsoleMsg(UserIndex, list + ".", FontTypeNames.FONTTYPE_INFO);
		} else {
			WriteConsoleMsg(UserIndex, "No hay GMs Online.", FontTypeNames.FONTTYPE_INFO);
		}
	}

	/* '' */
	/* ' Handles the "OnlineMap" message. */
	/* ' */
	/* ' @param userIndex The index of the user sending the message. */

	static void HandleOnlineMap(int UserIndex) {
		/* '*************************************************** */
		/* 'Author: Nicolas Matias Gonzalez (NIGO) */
		/* 'Last Modification: 23/03/2009 */
		/*
		 * '23/03/2009: ZaMa - Ahora no requiere estar en el mapa, sino que por
		 * defecto se toma en el que esta, pero se puede especificar otro
		 */
		/* '*************************************************** */
		/* 'Remove packet ID */
		Declaraciones.UserList[UserIndex].incomingData.ReadByte();

		int Map;
		Map = Declaraciones.UserList[UserIndex].incomingData.ReadInteger();

		if (Declaraciones.UserList[UserIndex].flags.Privilegios && (PlayerType.User || PlayerType.Consejero)) {
			return;
		}

		int LoopC;
		String list;
		PlayerType priv;

		priv = PlayerType.User || PlayerType.Consejero || PlayerType.SemiDios;
		if (Declaraciones.UserList[UserIndex].flags.Privilegios && (PlayerType.Dios || PlayerType.Admin)) {
			priv = priv + (PlayerType.Dios || PlayerType.Admin);
		}

		for (LoopC = (1); LoopC <= (Declaraciones.LastUser); LoopC++) {
			if (vb6.LenB(Declaraciones.UserList[LoopC].Name) != 0 && Declaraciones.UserList[LoopC].Pos.Map == Map) {
				if (Declaraciones.UserList[LoopC].flags.Privilegios && priv) {
					list = list + Declaraciones.UserList[LoopC].Name + ", ";
				}
			}
		}

		if (vb6.Len(list) > 2) {
			list = vb6.Left(list, vb6.Len(list) - 2);
		}

		WriteConsoleMsg(UserIndex, "Usuarios en el mapa: " + list, FontTypeNames.FONTTYPE_INFO);
		General.LogGM(Declaraciones.UserList[UserIndex].Name, "/ONLINEMAP " + Map);
	}

	/* '' */
	/* ' Handles the "Forgive" message. */
	/* ' */
	/* ' @param userIndex The index of the user sending the message. */

	static void HandleForgive(int UserIndex) {
		/* '*************************************************** */
		/* 'Author: Nicolas Matias Gonzalez (NIGO) */
		/* 'Last Modification: 07/06/2010 */
		/*
		 * '07/06/2010: ZaMa - Ahora no se puede usar para saber si hay
		 * dioses/admins online.
		 */
		/* '*************************************************** */
		if (Declaraciones.UserList[UserIndex].incomingData.length < 3) {
			throw new RuntimeException("Err . Raise UserList ( UserIndex ) . incomingData . NotEnoughDataErrCode");
			return;
		}

		/* FIXME: ON ERROR GOTO ErrHandler */
		/*
		 * 'This packet contains strings, make a copy of the data to prevent
		 * losses if it's not complete yet...
		 */
		clsByteQueue buffer;
		buffer = new clsByteQueue();
		buffer.CopyBuffer(Declaraciones.UserList[UserIndex].incomingData);

		/* 'Remove packet ID */
		buffer.ReadByte();

		String UserName;
		int tUser;

		UserName = buffer.ReadASCIIString();

		if ((!Declaraciones.UserList[UserIndex].flags.Privilegios && PlayerType.RoleMaster) != 0
				&& (Declaraciones.UserList[UserIndex].flags.Privilegios
						&& (PlayerType.Admin || PlayerType.Dios || PlayerType.SemiDios)) != 0) {
			tUser = Extra.NameIndex(UserName);

			if (tUser > 0) {
				if (Extra.EsNewbie(tUser)) {
					UsUaRiOs.VolverCiudadano(tUser);
				} else {
					General.LogGM(Declaraciones.UserList[UserIndex].Name,
							"Intento perdonar un personaje de nivel avanzado.");

					if (!(ES.EsDios(UserName) || ES.EsAdmin(UserName))) {
						WriteConsoleMsg(UserIndex, "Sólo se permite perdonar newbies.", FontTypeNames.FONTTYPE_INFO);
					}
				}
			}
		}

		/*
		 * 'If we got here then packet is complete, copy data back to original
		 * queue
		 */
		Declaraciones.UserList[UserIndex].incomingData.CopyBuffer(buffer);

		/* FIXME: ErrHandler : */
		int ERROR;
		/* ERROR = Err . Number */
		/* FIXME: ON ERROR GOTO 0 */

		/* 'Destroy auxiliar buffer */
		buffer = null;

		if (ERROR != 0) {
			throw new RuntimeException("Err . Raise ERROR");
		}
	}

	/* '' */
	/* ' Handles the "Kick" message. */
	/* ' */
	/* ' @param userIndex The index of the user sending the message. */

	static void HandleKick(int UserIndex) {
		/* '*************************************************** */
		/* 'Author: Nicolas Matias Gonzalez (NIGO) */
		/* 'Last Modification: 07/06/2010 */
		/*
		 * '07/06/2010: ZaMa - Ahora no se puede usar para saber si hay
		 * dioses/admins online.
		 */
		/* '*************************************************** */
		if (Declaraciones.UserList[UserIndex].incomingData.length < 3) {
			throw new RuntimeException("Err . Raise UserList ( UserIndex ) . incomingData . NotEnoughDataErrCode");
			return;
		}

		/* FIXME: ON ERROR GOTO ErrHandler */
		/*
		 * 'This packet contains strings, make a copy of the data to prevent
		 * losses if it's not complete yet...
		 */
		clsByteQueue buffer;
		buffer = new clsByteQueue();
		buffer.CopyBuffer(Declaraciones.UserList[UserIndex].incomingData);

		/* 'Remove packet ID */
		buffer.ReadByte();

		String UserName;
		int tUser;
		int rank;
		boolean IsAdmin;

		rank = PlayerType.Admin || PlayerType.Dios || PlayerType.SemiDios || PlayerType.Consejero;

		UserName = buffer.ReadASCIIString();
		IsAdmin = (Declaraciones.UserList[UserIndex].flags.Privilegios && (PlayerType.Admin || PlayerType.Dios)) != 0;

		if ((Declaraciones.UserList[UserIndex].flags.Privilegios && PlayerType.SemiDios) || IsAdmin) {
			tUser = Extra.NameIndex(UserName);

			if (tUser <= 0) {
				if (!(ES.EsDios(UserName) || ES.EsAdmin(UserName)) || IsAdmin) {
					WriteConsoleMsg(UserIndex, "El usuario no está online.", FontTypeNames.FONTTYPE_INFO);
				} else {
					WriteConsoleMsg(UserIndex, "No puedes echar a alguien con jerarquía mayor a la tuya.",
							FontTypeNames.FONTTYPE_INFO);
				}
			} else {
				if ((Declaraciones.UserList[tUser].flags.Privilegios
						&& rank) > (Declaraciones.UserList[UserIndex].flags.Privilegios && rank)) {
					WriteConsoleMsg(UserIndex, "No puedes echar a alguien con jerarquía mayor a la tuya.",
							FontTypeNames.FONTTYPE_INFO);
				} else {
					TCP.CloseSocket(tUser);
					General.LogGM(Declaraciones.UserList[UserIndex].Name, "Echó a " + UserName);
				}
			}
		}

		/*
		 * 'If we got here then packet is complete, copy data back to original
		 * queue
		 */
		Declaraciones.UserList[UserIndex].incomingData.CopyBuffer(buffer);

		/* FIXME: ErrHandler : */
		int ERROR;
		/* ERROR = Err . Number */
		/* FIXME: ON ERROR GOTO 0 */

		/* 'Destroy auxiliar buffer */
		buffer = null;

		if (ERROR != 0) {
			throw new RuntimeException("Err . Raise ERROR");
		}
	}

	/* '' */
	/* ' Handles the "Execute" message. */
	/* ' */
	/* ' @param userIndex The index of the user sending the message. */

	static void HandleExecute(int UserIndex) {
		/* '*************************************************** */
		/* 'Author: Nicolas Matias Gonzalez (NIGO) */
		/* 'Last Modification: 07/06/2010 */
		/*
		 * '07/06/2010: ZaMa - Ahora no se puede usar para saber si hay
		 * dioses/admins online.
		 */
		/* '*************************************************** */
		if (Declaraciones.UserList[UserIndex].incomingData.length < 3) {
			throw new RuntimeException("Err . Raise UserList ( UserIndex ) . incomingData . NotEnoughDataErrCode");
			return;
		}

		/* FIXME: ON ERROR GOTO ErrHandler */
		/*
		 * 'This packet contains strings, make a copy of the data to prevent
		 * losses if it's not complete yet...
		 */
		clsByteQueue buffer;
		buffer = new clsByteQueue();
		buffer.CopyBuffer(Declaraciones.UserList[UserIndex].incomingData);

		/* 'Remove packet ID */
		buffer.ReadByte();

		String UserName;
		int tUser;

		UserName = buffer.ReadASCIIString();

		if ((!Declaraciones.UserList[UserIndex].flags.Privilegios && PlayerType.RoleMaster) != 0
				&& (Declaraciones.UserList[UserIndex].flags.Privilegios
						&& (PlayerType.Admin || PlayerType.Dios || PlayerType.SemiDios)) != 0) {
			tUser = Extra.NameIndex(UserName);

			if (tUser > 0) {
				if (!Declaraciones.UserList[tUser].flags.Privilegios && PlayerType.User) {
					WriteConsoleMsg(UserIndex, "¿¿Estás loco?? ¿¿Cómo vas a pinatear un gm?? :@",
							FontTypeNames.FONTTYPE_INFO);
				} else {
					UsUaRiOs.UserDie(tUser);
					modSendData.SendData(SendTarget.ToAll, 0,
							PrepareMessageConsoleMsg(
									Declaraciones.UserList[UserIndex].Name + " ha ejecutado a " + UserName + ".",
									FontTypeNames.FONTTYPE_EJECUCION));
					General.LogGM(Declaraciones.UserList[UserIndex].Name, " ejecuto a " + UserName);
				}
			} else {
				if (!(ES.EsDios(UserName) || ES.EsAdmin(UserName))) {
					WriteConsoleMsg(UserIndex, "No está online.", FontTypeNames.FONTTYPE_INFO);
				} else {
					WriteConsoleMsg(UserIndex, "¿¿Estás loco?? ¿¿Cómo vas a pinatear un gm?? :@",
							FontTypeNames.FONTTYPE_INFO);
				}
			}
		}

		/*
		 * 'If we got here then packet is complete, copy data back to original
		 * queue
		 */
		Declaraciones.UserList[UserIndex].incomingData.CopyBuffer(buffer);

		/* FIXME: ErrHandler : */
		int ERROR;
		/* ERROR = Err . Number */
		/* FIXME: ON ERROR GOTO 0 */

		/* 'Destroy auxiliar buffer */
		buffer = null;

		if (ERROR != 0) {
			throw new RuntimeException("Err . Raise ERROR");
		}
	}

	/* '' */
	/* ' Handles the "BanChar" message. */
	/* ' */
	/* ' @param userIndex The index of the user sending the message. */

	static void HandleBanChar(int UserIndex) {
		/* '*************************************************** */
		/* 'Author: Nicolas Matias Gonzalez (NIGO) */
		/* 'Last Modification: 12/29/06 */
		/* ' */
		/* '*************************************************** */
		if (Declaraciones.UserList[UserIndex].incomingData.length < 5) {
			throw new RuntimeException("Err . Raise UserList ( UserIndex ) . incomingData . NotEnoughDataErrCode");
			return;
		}

		/* FIXME: ON ERROR GOTO ErrHandler */
		/*
		 * 'This packet contains strings, make a copy of the data to prevent
		 * losses if it's not complete yet...
		 */
		clsByteQueue buffer;
		buffer = new clsByteQueue();
		buffer.CopyBuffer(Declaraciones.UserList[UserIndex].incomingData);

		/* 'Remove packet ID */
		buffer.ReadByte();

		String UserName;
		String Reason;

		UserName = buffer.ReadASCIIString();
		Reason = buffer.ReadASCIIString();

		if ((!Declaraciones.UserList[UserIndex].flags.Privilegios && PlayerType.RoleMaster) != 0
				&& (Declaraciones.UserList[UserIndex].flags.Privilegios
						&& (PlayerType.Admin || PlayerType.Dios || PlayerType.SemiDios)) != 0) {
			Admin.BanCharacter(UserIndex, UserName, Reason);
		}

		/*
		 * 'If we got here then packet is complete, copy data back to original
		 * queue
		 */
		Declaraciones.UserList[UserIndex].incomingData.CopyBuffer(buffer);

		/* FIXME: ErrHandler : */
		int ERROR;
		/* ERROR = Err . Number */
		/* FIXME: ON ERROR GOTO 0 */

		/* 'Destroy auxiliar buffer */
		buffer = null;

		if (ERROR != 0) {
			throw new RuntimeException("Err . Raise ERROR");
		}
	}

	/* '' */
	/* ' Handles the "UnbanChar" message. */
	/* ' */
	/* ' @param userIndex The index of the user sending the message. */

	static void HandleUnbanChar(int UserIndex) {
		/* '*************************************************** */
		/* 'Author: Nicolas Matias Gonzalez (NIGO) */
		/* 'Last Modification: 12/29/06 */
		/* ' */
		/* '*************************************************** */
		if (Declaraciones.UserList[UserIndex].incomingData.length < 3) {
			throw new RuntimeException("Err . Raise UserList ( UserIndex ) . incomingData . NotEnoughDataErrCode");
			return;
		}

		/* FIXME: ON ERROR GOTO ErrHandler */
		/*
		 * 'This packet contains strings, make a copy of the data to prevent
		 * losses if it's not complete yet...
		 */
		clsByteQueue buffer;
		buffer = new clsByteQueue();
		buffer.CopyBuffer(Declaraciones.UserList[UserIndex].incomingData);

		/* 'Remove packet ID */
		buffer.ReadByte();

		String UserName;
		int cantPenas;

		UserName = buffer.ReadASCIIString();

		if ((!Declaraciones.UserList[UserIndex].flags.Privilegios && PlayerType.RoleMaster) != 0
				&& (Declaraciones.UserList[UserIndex].flags.Privilegios
						&& (PlayerType.Admin || PlayerType.Dios || PlayerType.SemiDios)) != 0) {
			if ((vb6.InStrB(UserName, "\\") != 0)) {
				UserName = vb6.Replace(UserName, "\\", "");
			}
			if ((vb6.InStrB(UserName, "/") != 0)) {
				UserName = vb6.Replace(UserName, "/", "");
			}

			if (!General.FileExist(Declaraciones.CharPath + UserName + ".chr", 0)) {
				WriteConsoleMsg(UserIndex, "Charfile inexistente (no use +).", FontTypeNames.FONTTYPE_INFO);
			} else {
				if ((vb6.val(ES.GetVar(Declaraciones.CharPath + UserName + ".chr", "FLAGS", "Ban")) == 1)) {
					Admin.UnBan(UserName);

					/* 'penas */
					cantPenas = vb6.val(ES.GetVar(Declaraciones.CharPath + UserName + ".chr", "PENAS", "Cant"));
					ES.WriteVar(Declaraciones.CharPath + UserName + ".chr", "PENAS", "Cant", cantPenas + 1);
					ES.WriteVar(Declaraciones.CharPath + UserName + ".chr", "PENAS", "P" + cantPenas + 1,
							vb6.LCase(Declaraciones.UserList[UserIndex].Name) + ": UNBAN. " + Date + " " + vb6.time());

					General.LogGM(Declaraciones.UserList[UserIndex].Name, "/UNBAN a " + UserName);
					WriteConsoleMsg(UserIndex, UserName + " unbanned.", FontTypeNames.FONTTYPE_INFO);
				} else {
					WriteConsoleMsg(UserIndex, UserName + " no está baneado. Imposible unbanear.",
							FontTypeNames.FONTTYPE_INFO);
				}
			}
		}

		/*
		 * 'If we got here then packet is complete, copy data back to original
		 * queue
		 */
		Declaraciones.UserList[UserIndex].incomingData.CopyBuffer(buffer);

		/* FIXME: ErrHandler : */
		int ERROR;
		/* ERROR = Err . Number */
		/* FIXME: ON ERROR GOTO 0 */

		/* 'Destroy auxiliar buffer */
		buffer = null;

		if (ERROR != 0) {
			throw new RuntimeException("Err . Raise ERROR");
		}
	}

	/* '' */
	/* ' Handles the "NPCFollow" message. */
	/* ' */
	/* ' @param userIndex The index of the user sending the message. */

	static void HandleNPCFollow(int UserIndex) {
		/* '*************************************************** */
		/* 'Author: Nicolas Matias Gonzalez (NIGO) */
		/* 'Last Modification: 12/29/06 */
		/* ' */
		/* '*************************************************** */
		/* 'Remove packet ID */
		Declaraciones.UserList[UserIndex].incomingData.ReadByte();

		if (Declaraciones.UserList[UserIndex].flags.Privilegios && (PlayerType.User || PlayerType.Consejero)) {
			return;
		}

		if (Declaraciones.UserList[UserIndex].flags.TargetNPC > 0) {
			NPCs.DoFollow(Declaraciones.UserList[UserIndex].flags.TargetNPC, Declaraciones.UserList[UserIndex].Name);
			Declaraciones.Npclist[Declaraciones.UserList[UserIndex].flags.TargetNPC].flags.Inmovilizado = 0;
			Declaraciones.Npclist[Declaraciones.UserList[UserIndex].flags.TargetNPC].flags.Paralizado = 0;
			Declaraciones.Npclist[Declaraciones.UserList[UserIndex].flags.TargetNPC].Contadores.Paralisis = 0;
		}
	}

	/* '' */
	/* ' Handles the "SummonChar" message. */
	/* ' */
	/* ' @param userIndex The index of the user sending the message. */

	static void HandleSummonChar(int UserIndex) {
		/* '*************************************************** */
		/* 'Author: Nicolas Matias Gonzalez (NIGO) */
		/* 'Last Modification: 26/03/2009 */
		/*
		 * '26/03/2009: ZaMa - Chequeo que no se teletransporte donde haya un
		 * char o npc
		 */
		/* '*************************************************** */
		if (Declaraciones.UserList[UserIndex].incomingData.length < 3) {
			throw new RuntimeException("Err . Raise UserList ( UserIndex ) . incomingData . NotEnoughDataErrCode");
			return;
		}

		/* FIXME: ON ERROR GOTO ErrHandler */
		/*
		 * 'This packet contains strings, make a copy of the data to prevent
		 * losses if it's not complete yet...
		 */
		clsByteQueue buffer;
		buffer = new clsByteQueue();
		buffer.CopyBuffer(Declaraciones.UserList[UserIndex].incomingData);

		/* 'Remove packet ID */
		buffer.ReadByte();

		String UserName;
		int tUser;
		int X;
		int Y;

		UserName = buffer.ReadASCIIString();

		if ((Declaraciones.UserList[UserIndex].flags.Privilegios
				&& (PlayerType.Admin || PlayerType.Dios || PlayerType.SemiDios))) {
			tUser = Extra.NameIndex(UserName);

			if (tUser <= 0) {
				if (ES.EsDios(UserName) || ES.EsAdmin(UserName)) {
					WriteConsoleMsg(UserIndex, "No puedes invocar a dioses y admins.", FontTypeNames.FONTTYPE_INFO);
				} else {
					WriteConsoleMsg(UserIndex, "El jugador no está online.", FontTypeNames.FONTTYPE_INFO);
				}

			} else {
				if ((Declaraciones.UserList[UserIndex].flags.Privilegios && (PlayerType.Dios || PlayerType.Admin)) != 0
						|| (Declaraciones.UserList[tUser].flags.Privilegios
								&& (PlayerType.Consejero || PlayerType.User)) != 0) {
					WriteConsoleMsg(tUser, Declaraciones.UserList[UserIndex].Name + " te ha trasportado.",
							FontTypeNames.FONTTYPE_INFO);
					X = Declaraciones.UserList[UserIndex].Pos.X;
					Y = Declaraciones.UserList[UserIndex].Pos.Y + 1;
					Extra.FindLegalPos(tUser, Declaraciones.UserList[UserIndex].Pos.Map, X, Y);
					UsUaRiOs.WarpUserChar(tUser, Declaraciones.UserList[UserIndex].Pos.Map, X, Y, true, true);
					General.LogGM(Declaraciones.UserList[UserIndex].Name,
							"/SUM " + UserName + " Map:" + Declaraciones.UserList[UserIndex].Pos.Map + " X:"
									+ Declaraciones.UserList[UserIndex].Pos.X + " Y:"
									+ Declaraciones.UserList[UserIndex].Pos.Y);
				} else {
					WriteConsoleMsg(UserIndex, "No puedes invocar a dioses y admins.", FontTypeNames.FONTTYPE_INFO);
				}
			}
		}

		/*
		 * 'If we got here then packet is complete, copy data back to original
		 * queue
		 */
		Declaraciones.UserList[UserIndex].incomingData.CopyBuffer(buffer);

		/* FIXME: ErrHandler : */
		int ERROR;
		/* ERROR = Err . Number */
		/* FIXME: ON ERROR GOTO 0 */

		/* 'Destroy auxiliar buffer */
		buffer = null;

		if (ERROR != 0) {
			throw new RuntimeException("Err . Raise ERROR");
		}
	}

	/* '' */
	/* ' Handles the "SpawnListRequest" message. */
	/* ' */
	/* ' @param userIndex The index of the user sending the message. */

	static void HandleSpawnListRequest(int UserIndex) {
		/* '*************************************************** */
		/* 'Author: Nicolas Matias Gonzalez (NIGO) */
		/* 'Last Modification: 12/29/06 */
		/* ' */
		/* '*************************************************** */
		/* 'Remove packet ID */
		Declaraciones.UserList[UserIndex].incomingData.ReadByte();

		if (Declaraciones.UserList[UserIndex].flags.Privilegios && (PlayerType.User || PlayerType.Consejero)) {
			return;
		}

		General.EnviarSpawnList(UserIndex);
	}

	/* '' */
	/* ' Handles the "SpawnCreature" message. */
	/* ' */
	/* ' @param userIndex The index of the user sending the message. */

	static void HandleSpawnCreature(int UserIndex) {
 /* '*************************************************** */
 /* 'Author: Nicolas Matias Gonzalez (NIGO) */
 /* 'Last Modification: 12/29/06 */
 /* ' */
 /* '*************************************************** */
  if (Declaraciones.UserList[UserIndex].incomingData.length<3) {
  throw new RuntimeException("Err . Raise UserList ( UserIndex ) . incomingData . NotEnoughDataErrCode");
  return;
 }
 
  /* 'Remove packet ID */
  Declaraciones.UserList[UserIndex].incomingData.ReadByte();
  
  int npc;
  npc = Declaraciones.UserList[UserIndex].incomingData.ReadInteger();
  
   if ((Declaraciones.UserList[UserIndex].flags.Privilegios && (PlayerType.Admin || PlayerType.Dios || PlayerType.SemiDios))) {
   if (npc>0 && npc<=vb6.UBound(Declaraciones.SpawnList[])) {
   NPCs.SpawnNpc(Declaraciones.SpawnList[npc].NpcIndex, Declaraciones.UserList[UserIndex].Pos, true, false);
   }
   
   General.LogGM(Declaraciones.UserList[UserIndex].Name, "Sumoneo " + Declaraciones.SpawnList[npc].NpcName);
  }
}

	/* '' */
	/* ' Handles the "ResetNPCInventory" message. */
	/* ' */
	/* ' @param userIndex The index of the user sending the message. */

	static void HandleResetNPCInventory(int UserIndex) {
		/* '*************************************************** */
		/* 'Author: Nicolas Matias Gonzalez (NIGO) */
		/* 'Last Modification: 12/29/06 */
		/* ' */
		/* '*************************************************** */
		/* 'Remove packet ID */
		Declaraciones.UserList[UserIndex].incomingData.ReadByte();

		if (Declaraciones.UserList[UserIndex].flags.Privilegios
				&& (PlayerType.User || PlayerType.Consejero || PlayerType.RoleMaster)) {
			return;
		}
		if (Declaraciones.UserList[UserIndex].flags.TargetNPC == 0) {
			return;
		}

		InvNpc.ResetNpcInv(Declaraciones.UserList[UserIndex].flags.TargetNPC);
		General.LogGM(Declaraciones.UserList[UserIndex].Name,
				"/RESETINV " + Declaraciones.Npclist[Declaraciones.UserList[UserIndex].flags.TargetNPC].Name);
	}

	/* '' */
	/* ' Handles the "CleanWorld" message. */
	/* ' */
	/* ' @param userIndex The index of the user sending the message. */

	static void HandleCleanWorld(int UserIndex) {
		/* '*************************************************** */
		/* 'Author: Nicolas Matias Gonzalez (NIGO) */
		/* 'Last Modification: 12/29/06 */
		/* ' */
		/* '*************************************************** */
		/* 'Remove packet ID */
		Declaraciones.UserList[UserIndex].incomingData.ReadByte();

		if (Declaraciones.UserList[UserIndex].flags.Privilegios
				&& (PlayerType.User || PlayerType.Consejero || PlayerType.RoleMaster)) {
			return;
		}

		General.LimpiarMundo();
	}

	/* '' */
	/* ' Handles the "ServerMessage" message. */
	/* ' */
	/* ' @param userIndex The index of the user sending the message. */

	static void HandleServerMessage(int UserIndex) {
		/* '*************************************************** */
		/* 'Author: Nicolas Matias Gonzalez (NIGO) */
		/* 'Last Modification: 28/05/2010 */
		/* '28/05/2010: ZaMa - Ahora no dice el nombre del gm que lo dice. */
		/* '*************************************************** */
		if (Declaraciones.UserList[UserIndex].incomingData.length < 3) {
			throw new RuntimeException("Err . Raise UserList ( UserIndex ) . incomingData . NotEnoughDataErrCode");
			return;
		}

		/* FIXME: ON ERROR GOTO ErrHandler */
		/*
		 * 'This packet contains strings, make a copy of the data to prevent
		 * losses if it's not complete yet...
		 */
		clsByteQueue buffer;
		buffer = new clsByteQueue();
		buffer.CopyBuffer(Declaraciones.UserList[UserIndex].incomingData);

		/* 'Remove packet ID */
		buffer.ReadByte();

		String message;
		message = buffer.ReadASCIIString();

		if ((Declaraciones.UserList[UserIndex].flags.Privilegios
				&& (PlayerType.Admin || PlayerType.Dios || PlayerType.SemiDios))) {
			if (vb6.LenB(message) != 0) {
				General.LogGM(Declaraciones.UserList[UserIndex].Name, "Mensaje Broadcast:" + message);
				modSendData.SendData(SendTarget.ToAll, 0,
						PrepareMessageConsoleMsg(message, FontTypeNames.FONTTYPE_TALK));
				/* ''''''''''''''''SOLO PARA EL TESTEO''''''' */
				/* ''''''''''SE USA PARA COMUNICARSE CON EL SERVER''''''''''' */
				/*
				 * 'frmMain.txtChat.Text = frmMain.txtChat.Text & vbNewLine &
				 * UserList(UserIndex).name & " > " & message
				 */
			}
		}

		/*
		 * 'If we got here then packet is complete, copy data back to original
		 * queue
		 */
		Declaraciones.UserList[UserIndex].incomingData.CopyBuffer(buffer);

		/* FIXME: ErrHandler : */
		int ERROR;
		/* ERROR = Err . Number */
		/* FIXME: ON ERROR GOTO 0 */

		/* 'Destroy auxiliar buffer */
		buffer = null;

		if (ERROR != 0) {
			throw new RuntimeException("Err . Raise ERROR");
		}
	}

	/* '' */
	/* ' Handles the "MapMessage" message. */
	/* ' */
	/* ' @param userIndex The index of the user sending the message. */

	static void HandleMapMessage(int UserIndex) {
		/* '*************************************************** */
		/* 'Author: ZaMa */
		/* 'Last Modification: 14/11/2010 */
		/* '*************************************************** */
		if (Declaraciones.UserList[UserIndex].incomingData.length < 3) {
			throw new RuntimeException("Err . Raise UserList ( UserIndex ) . incomingData . NotEnoughDataErrCode");
			return;
		}

		/* FIXME: ON ERROR GOTO ErrHandler */
		/*
		 * 'This packet contains strings, make a copy of the data to prevent
		 * losses if it's not complete yet...
		 */
		clsByteQueue buffer;
		buffer = new clsByteQueue();
		buffer.CopyBuffer(Declaraciones.UserList[UserIndex].incomingData);

		/* 'Remove packet ID */
		buffer.ReadByte();

		String message;
		message = buffer.ReadASCIIString();

		if ((Declaraciones.UserList[UserIndex].flags.Privilegios
				&& (PlayerType.Admin || PlayerType.Dios || PlayerType.SemiDios) != 0)
				|| ((Declaraciones.UserList[UserIndex].flags.Privilegios && PlayerType.Consejero) != 0
						&& (Declaraciones.UserList[UserIndex].flags.Privilegios && PlayerType.RoleMaster) != 0)) {

			if (vb6.LenB(message) != 0) {

				int mapa;
				mapa = Declaraciones.UserList[UserIndex].Pos.Map;

				General.LogGM(Declaraciones.UserList[UserIndex].Name, "Mensaje a mapa " + mapa + ":" + message);
				modSendData.SendData(SendTarget.toMap, mapa,
						PrepareMessageConsoleMsg(message, FontTypeNames.FONTTYPE_TALK));
			}
		}

		/*
		 * 'If we got here then packet is complete, copy data back to original
		 * queue
		 */
		Declaraciones.UserList[UserIndex].incomingData.CopyBuffer(buffer);

		/* FIXME: ErrHandler : */
		int ERROR;
		/* ERROR = Err . Number */
		/* FIXME: ON ERROR GOTO 0 */

		/* 'Destroy auxiliar buffer */
		buffer = null;

		if (ERROR != 0) {
			throw new RuntimeException("Err . Raise ERROR");
		}
	}

	/* '' */
	/* ' Handles the "NickToIP" message. */
	/* ' */
	/* ' @param userIndex The index of the user sending the message. */

	static void HandleNickToIP(int UserIndex) {
		/* '*************************************************** */
		/* 'Author: Nicolas Matias Gonzalez (NIGO) */
		/* 'Last Modification: 07/06/2010 */
		/*
		 * 'Pablo (ToxicWaste): Agrego para que el /nick2ip tambien diga los
		 * nicks en esa ip por pedido de la DGM.
		 */
		/*
		 * '07/06/2010: ZaMa - Ahora no se puede usar para saber si hay
		 * dioses/admins online.
		 */
		/* '*************************************************** */
		if (Declaraciones.UserList[UserIndex].incomingData.length < 3) {
			throw new RuntimeException("Err . Raise UserList ( UserIndex ) . incomingData . NotEnoughDataErrCode");
			return;
		}

		/* FIXME: ON ERROR GOTO ErrHandler */
		/*
		 * 'This packet contains strings, make a copy of the data to prevent
		 * losses if it's not complete yet...
		 */
		clsByteQueue buffer;
		buffer = new clsByteQueue();
		buffer.CopyBuffer(Declaraciones.UserList[UserIndex].incomingData);

		/* 'Remove packet ID */
		buffer.ReadByte();

		String UserName;
		int tUser;
		PlayerType priv;
		boolean IsAdmin;

		UserName = buffer.ReadASCIIString();

		if ((!Declaraciones.UserList[UserIndex].flags.Privilegios && PlayerType.RoleMaster) != 0
				&& (Declaraciones.UserList[UserIndex].flags.Privilegios
						&& (PlayerType.Admin || PlayerType.Dios || PlayerType.SemiDios)) != 0) {
			tUser = Extra.NameIndex(UserName);
			General.LogGM(Declaraciones.UserList[UserIndex].Name, "NICK2IP Solicito la IP de " + UserName);

			IsAdmin = (Declaraciones.UserList[UserIndex].flags.Privilegios
					&& (PlayerType.Dios || PlayerType.Admin)) != 0;
			if (IsAdmin) {
				priv = PlayerType.User || PlayerType.Consejero || PlayerType.SemiDios || PlayerType.Dios
						|| PlayerType.Admin;
			} else {
				priv = PlayerType.User;
			}

			if (tUser > 0) {
				if (Declaraciones.UserList[tUser].flags.Privilegios && priv) {
					WriteConsoleMsg(UserIndex, "El ip de " + UserName + " es " + Declaraciones.UserList[tUser].ip,
							FontTypeNames.FONTTYPE_INFO);
					String ip;
					String lista;
					int LoopC;
					ip = Declaraciones.UserList[tUser].ip;
					for (LoopC = (1); LoopC <= (Declaraciones.LastUser); LoopC++) {
						if (Declaraciones.UserList[LoopC].ip == ip) {
							if (vb6.LenB(Declaraciones.UserList[LoopC].Name) != 0
									&& Declaraciones.UserList[LoopC].flags.UserLogged) {
								if (Declaraciones.UserList[LoopC].flags.Privilegios && priv) {
									lista = lista + Declaraciones.UserList[LoopC].Name + ", ";
								}
							}
						}
					}
					if (vb6.LenB(lista) != 0) {
						lista = vb6.Left(lista, vb6.Len(lista) - 2);
					}
					WriteConsoleMsg(UserIndex, "Los personajes con ip " + ip + " son: " + lista,
							FontTypeNames.FONTTYPE_INFO);
				}
			} else {
				if (!(ES.EsDios(UserName) || ES.EsAdmin(UserName)) || IsAdmin) {
					WriteConsoleMsg(UserIndex, "No hay ningún personaje con ese nick.", FontTypeNames.FONTTYPE_INFO);
				}
			}
		}

		/*
		 * 'If we got here then packet is complete, copy data back to original
		 * queue
		 */
		Declaraciones.UserList[UserIndex].incomingData.CopyBuffer(buffer);

		/* FIXME: ErrHandler : */
		int ERROR;
		/* ERROR = Err . Number */
		/* FIXME: ON ERROR GOTO 0 */

		/* 'Destroy auxiliar buffer */
		buffer = null;

		if (ERROR != 0) {
			throw new RuntimeException("Err . Raise ERROR");
		}
	}

	/* '' */
	/* ' Handles the "IPToNick" message. */
	/* ' */
	/* ' @param userIndex The index of the user sending the message. */

	static void HandleIPToNick(int UserIndex) {
		/* '*************************************************** */
		/* 'Author: Nicolas Matias Gonzalez (NIGO) */
		/* 'Last Modification: 12/29/06 */
		/* ' */
		/* '*************************************************** */
		if (Declaraciones.UserList[UserIndex].incomingData.length < 5) {
			throw new RuntimeException("Err . Raise UserList ( UserIndex ) . incomingData . NotEnoughDataErrCode");
			return;
		}

		/* 'Remove packet ID */
		Declaraciones.UserList[UserIndex].incomingData.ReadByte();

		String ip;
		int LoopC;
		String lista;
		PlayerType priv;

		ip = Declaraciones.UserList[UserIndex].incomingData.ReadByte() + ".";
		ip = ip + Declaraciones.UserList[UserIndex].incomingData.ReadByte() + ".";
		ip = ip + Declaraciones.UserList[UserIndex].incomingData.ReadByte() + ".";
		ip = ip + Declaraciones.UserList[UserIndex].incomingData.ReadByte();

		if (Declaraciones.UserList[UserIndex].flags.Privilegios
				&& (PlayerType.User || PlayerType.Consejero || PlayerType.RoleMaster)) {
			return;
		}

		General.LogGM(Declaraciones.UserList[UserIndex].Name, "IP2NICK Solicito los Nicks de IP " + ip);

		if (Declaraciones.UserList[UserIndex].flags.Privilegios && (PlayerType.Dios || PlayerType.Admin)) {
			priv = PlayerType.User || PlayerType.Consejero || PlayerType.SemiDios || PlayerType.Dios
					|| PlayerType.Admin;
		} else {
			priv = PlayerType.User;
		}

		for (LoopC = (1); LoopC <= (Declaraciones.LastUser); LoopC++) {
			if (Declaraciones.UserList[LoopC].ip == ip) {
				if (vb6.LenB(Declaraciones.UserList[LoopC].Name) != 0
						&& Declaraciones.UserList[LoopC].flags.UserLogged) {
					if (Declaraciones.UserList[LoopC].flags.Privilegios && priv) {
						lista = lista + Declaraciones.UserList[LoopC].Name + ", ";
					}
				}
			}
		}

		if (vb6.LenB(lista) != 0) {
			lista = vb6.Left(lista, vb6.Len(lista) - 2);
		}
		WriteConsoleMsg(UserIndex, "Los personajes con ip " + ip + " son: " + lista, FontTypeNames.FONTTYPE_INFO);
	}

	/* '' */
	/* ' Handles the "GuildOnlineMembers" message. */
	/* ' */
	/* ' @param userIndex The index of the user sending the message. */

	static void HandleGuildOnlineMembers(int UserIndex) {
		/* '*************************************************** */
		/* 'Author: Nicolas Matias Gonzalez (NIGO) */
		/* 'Last Modification: 12/29/06 */
		/* ' */
		/* '*************************************************** */
		if (Declaraciones.UserList[UserIndex].incomingData.length < 3) {
			throw new RuntimeException("Err . Raise UserList ( UserIndex ) . incomingData . NotEnoughDataErrCode");
			return;
		}

		/* FIXME: ON ERROR GOTO ErrHandler */
		/*
		 * 'This packet contains strings, make a copy of the data to prevent
		 * losses if it's not complete yet...
		 */
		clsByteQueue buffer;
		buffer = new clsByteQueue();
		buffer.CopyBuffer(Declaraciones.UserList[UserIndex].incomingData);

		/* 'Remove packet ID */
		buffer.ReadByte();

		String GuildName;
		int tGuild;

		GuildName = buffer.ReadASCIIString();

		if ((vb6.InStrB(GuildName, "+") != 0)) {
			GuildName = vb6.Replace(GuildName, "+", " ");
		}

		if ((!Declaraciones.UserList[UserIndex].flags.Privilegios && PlayerType.RoleMaster) != 0
				&& (Declaraciones.UserList[UserIndex].flags.Privilegios
						&& (PlayerType.Admin || PlayerType.Dios || PlayerType.SemiDios)) != 0) {
			tGuild = modGuilds.GetGuildIndex(GuildName);

			if (tGuild > 0) {
				WriteConsoleMsg(UserIndex,
						"Clan " + vb6.UCase(GuildName) + ": " + modGuilds.m_ListaDeMiembrosOnline(UserIndex, tGuild),
						FontTypeNames.FONTTYPE_GUILDMSG);
			}
		}

		/*
		 * 'If we got here then packet is complete, copy data back to original
		 * queue
		 */
		Declaraciones.UserList[UserIndex].incomingData.CopyBuffer(buffer);

		/* FIXME: ErrHandler : */
		int ERROR;
		/* ERROR = Err . Number */
		/* FIXME: ON ERROR GOTO 0 */

		/* 'Destroy auxiliar buffer */
		buffer = null;

		if (ERROR != 0) {
			throw new RuntimeException("Err . Raise ERROR");
		}
	}

	/* '' */
	/* ' Handles the "TeleportCreate" message. */
	/* ' */
	/* ' @param userIndex The index of the user sending the message. */

	static void HandleTeleportCreate(int UserIndex) {
		/* '*************************************************** */
		/* 'Author: Nicolas Matias Gonzalez (NIGO) */
		/* 'Last Modification: 22/03/2010 */
		/*
		 * '15/11/2009: ZaMa - Ahora se crea un teleport con un radio
		 * especificado.
		 */
		/*
		 * '22/03/2010: ZaMa - Harcodeo los teleps y radios en el dat, para
		 * evitar mapas bugueados.
		 */
		/* '*************************************************** */
		if (Declaraciones.UserList[UserIndex].incomingData.length < 6) {
			throw new RuntimeException("Err . Raise UserList ( UserIndex ) . incomingData . NotEnoughDataErrCode");
			return;
		}

		/* 'Remove packet ID */
		Declaraciones.UserList[UserIndex].incomingData.ReadByte();

		int mapa;
		int X;
		int Y;
		int Radio;

		mapa = Declaraciones.UserList[UserIndex].incomingData.ReadInteger();
		X = Declaraciones.UserList[UserIndex].incomingData.ReadByte();
		Y = Declaraciones.UserList[UserIndex].incomingData.ReadByte();
		Radio = Declaraciones.UserList[UserIndex].incomingData.ReadByte();

		Radio = SistemaCombate.MinimoInt(Radio, 6);

		if (Declaraciones.UserList[UserIndex].flags.Privilegios
				&& (PlayerType.User || PlayerType.Consejero || PlayerType.SemiDios)) {
			return;
		}

		General.LogGM(Declaraciones.UserList[UserIndex].Name, "/CT " + mapa + "," + X + "," + Y + "," + Radio);

		if (!General.MapaValido(mapa) || !Extra.InMapBounds(mapa, X, Y)) {
			return;
		}

		if (Declaraciones.MapData[Declaraciones.UserList[UserIndex].Pos.Map][Declaraciones.UserList[UserIndex].Pos.X][Declaraciones.UserList[UserIndex].Pos.Y
				- 1].ObjInfo.ObjIndex > 0) {
			return;
		}

		if (Declaraciones.MapData[Declaraciones.UserList[UserIndex].Pos.Map][Declaraciones.UserList[UserIndex].Pos.X][Declaraciones.UserList[UserIndex].Pos.Y
				- 1].TileExit.Map > 0) {
			return;
		}

		if (Declaraciones.MapData[mapa][X][Y].ObjInfo.ObjIndex > 0) {
			WriteConsoleMsg(UserIndex, "Hay un objeto en el piso en ese lugar.", FontTypeNames.FONTTYPE_INFO);
			return;
		}

		if (Declaraciones.MapData[mapa][X][Y].TileExit.Map > 0) {
			WriteConsoleMsg(UserIndex, "No puedes crear un teleport que apunte a la entrada de otro.",
					FontTypeNames.FONTTYPE_INFO);
			return;
		}

		Declaraciones.Obj ET;
		ET.Amount = 1;
		/*
		 * ' Es el numero en el dat. El indice es el comienzo + el radio, todo
		 * harcodeado :(.
		 */
		ET.ObjIndex = Declaraciones.TELEP_OBJ_INDEX + Radio;

		Declaraciones.MapData[Declaraciones.UserList[UserIndex].Pos.Map][Declaraciones.UserList[UserIndex].Pos.X][Declaraciones.UserList[UserIndex].Pos.Y
				- 1].TileExit.Map = mapa;
		Declaraciones.MapData[Declaraciones.UserList[UserIndex].Pos.Map][Declaraciones.UserList[UserIndex].Pos.X][Declaraciones.UserList[UserIndex].Pos.Y
				- 1].TileExit.X = X;
		Declaraciones.MapData[Declaraciones.UserList[UserIndex].Pos.Map][Declaraciones.UserList[UserIndex].Pos.X][Declaraciones.UserList[UserIndex].Pos.Y
				- 1].TileExit.Y = Y;

		InvUsuario.MakeObj(ET, Declaraciones.UserList[UserIndex].Pos.Map, Declaraciones.UserList[UserIndex].Pos.X,
				Declaraciones.UserList[UserIndex].Pos.Y - 1);
	}

	/* '' */
	/* ' Handles the "TeleportDestroy" message. */
	/* ' */
	/* ' @param userIndex The index of the user sending the message. */

	static void HandleTeleportDestroy(int UserIndex) {
		/* '*************************************************** */
		/* 'Author: Nicolas Matias Gonzalez (NIGO) */
		/* 'Last Modification: 12/29/06 */
		/* ' */
		/* '*************************************************** */
		int mapa;
		int X;
		int Y;

		/* 'Remove packet ID */
		Declaraciones.UserList[UserIndex].incomingData.ReadByte();

		/* '/dt */
		if (Declaraciones.UserList[UserIndex].flags.Privilegios
				&& (PlayerType.User || PlayerType.Consejero || PlayerType.SemiDios)) {
			return;
		}

		mapa = Declaraciones.UserList[UserIndex].flags.TargetMap;
		X = Declaraciones.UserList[UserIndex].flags.TargetX;
		Y = Declaraciones.UserList[UserIndex].flags.TargetY;

		if (!Extra.InMapBounds(mapa, X, Y)) {
			return;
		}

		if (Declaraciones.MapData[mapa][X][Y].ObjInfo.ObjIndex == 0) {
			return;
		}

		if (Declaraciones.ObjData[Declaraciones.MapData[mapa][X][Y].ObjInfo.ObjIndex].OBJType == eOBJType.otTeleport
				&& Declaraciones.MapData[mapa][X][Y].TileExit.Map > 0) {
			General.LogGM(Declaraciones.UserList[UserIndex].Name, "/DT: " + mapa + "," + X + "," + Y);

			InvUsuario.EraseObj(Declaraciones.MapData[mapa][X][Y].ObjInfo.Amount, mapa, X, Y);

			if (Declaraciones.MapData[Declaraciones.MapData[mapa][X][Y].TileExit.Map][Declaraciones.MapData[mapa][X][Y].TileExit.X][Declaraciones.MapData[mapa][X][Y].TileExit.Y].ObjInfo.ObjIndex == 651) {
				InvUsuario.EraseObj(1, Declaraciones.MapData[mapa][X][Y].TileExit.Map,
						Declaraciones.MapData[mapa][X][Y].TileExit.X, Declaraciones.MapData[mapa][X][Y].TileExit.Y);
			}

			Declaraciones.MapData[mapa][X][Y].TileExit.Map = 0;
			Declaraciones.MapData[mapa][X][Y].TileExit.X = 0;
			Declaraciones.MapData[mapa][X][Y].TileExit.Y = 0;
		}
	}

	/* '' */
	/* ' Handles the "RainToggle" message. */
	/* ' */
	/* ' @param userIndex The index of the user sending the message. */

	static void HandleRainToggle(int UserIndex) {
		/* '*************************************************** */
		/* 'Author: Nicolas Matias Gonzalez (NIGO) */
		/* 'Last Modification: 12/29/06 */
		/* ' */
		/* '*************************************************** */
		/* 'Remove packet ID */
		Declaraciones.UserList[UserIndex].incomingData.ReadByte();

		if (Declaraciones.UserList[UserIndex].flags.Privilegios && (PlayerType.User || PlayerType.Consejero)) {
			return;
		}

		General.LogGM(Declaraciones.UserList[UserIndex].Name, "/LLUVIA");
		Admin.Lloviendo = !Admin.Lloviendo;

		modSendData.SendData(SendTarget.ToAll, 0, PrepareMessageRainToggle());
	}

	/* '' */
	/* ' Handles the "EnableDenounces" message. */
	/* ' */
	/* ' @param userIndex The index of the user sending the message. */

	static void HandleEnableDenounces(int UserIndex) {
		/* '*************************************************** */
		/* 'Author: ZaMa */
		/* 'Last Modification: 14/11/2010 */
		/* 'Enables/Disables */
		/* '*************************************************** */

		/* 'Remove packet ID */
		Declaraciones.UserList[UserIndex].incomingData.ReadByte();

		/* ' Gm? */
		if (!Extra.EsGm(UserIndex)) {
			return;
		}
		/* ' Rm? */
		if ((Declaraciones.UserList[UserIndex].flags.Privilegios && PlayerType.RoleMaster) != 0) {
			return;
		}

		boolean Activado;
		String msg;

		Activado = !Declaraciones.UserList[UserIndex].flags.SendDenounces;
		Declaraciones.UserList[UserIndex].flags.SendDenounces = Activado;

		msg = "Denuncias por consola " + vb6.IIf(Activado, "ativadas", "desactivadas") + ".";

		General.LogGM(Declaraciones.UserList[UserIndex].Name, msg);

		WriteConsoleMsg(UserIndex, msg, FontTypeNames.FONTTYPE_INFO);

	}

	/* '' */
	/* ' Handles the "ShowDenouncesList" message. */
	/* ' */
	/* ' @param userIndex The index of the user sending the message. */

	static void HandleShowDenouncesList(int UserIndex) {
		/* '*************************************************** */
		/* 'Author: ZaMa */
		/* 'Last Modification: 14/11/2010 */
		/* ' */
		/* '*************************************************** */
		/* 'Remove packet ID */
		Declaraciones.UserList[UserIndex].incomingData.ReadByte();

		if ((Declaraciones.UserList[UserIndex].flags.Privilegios && (PlayerType.User || PlayerType.RoleMaster)) != 0) {
			return;
		}
		WriteShowDenounces(UserIndex);
	}

	/* '' */
	/* ' Handles the "SetCharDescription" message. */
	/* ' */
	/* ' @param userIndex The index of the user sending the message. */

	static void HandleSetCharDescription(int UserIndex) {
		/* '*************************************************** */
		/* 'Author: Nicolas Matias Gonzalez (NIGO) */
		/* 'Last Modification: 12/29/06 */
		/* ' */
		/* '*************************************************** */
		if (Declaraciones.UserList[UserIndex].incomingData.length < 3) {
			throw new RuntimeException("Err . Raise UserList ( UserIndex ) . incomingData . NotEnoughDataErrCode");
			return;
		}

		/* FIXME: ON ERROR GOTO ErrHandler */
		/*
		 * 'This packet contains strings, make a copy of the data to prevent
		 * losses if it's not complete yet...
		 */
		clsByteQueue buffer;
		buffer = new clsByteQueue();
		buffer.CopyBuffer(Declaraciones.UserList[UserIndex].incomingData);

		/* 'Remove packet ID */
		buffer.ReadByte();

		int tUser;
		String desc;

		desc = buffer.ReadASCIIString();

		if ((Declaraciones.UserList[UserIndex].flags.Privilegios && (PlayerType.Dios || PlayerType.Admin)) != 0
				|| (Declaraciones.UserList[UserIndex].flags.Privilegios && PlayerType.RoleMaster) != 0) {
			tUser = Declaraciones.UserList[UserIndex].flags.TargetUser;
			if (tUser > 0) {
				Declaraciones.UserList[tUser].DescRM = desc;
			} else {
				WriteConsoleMsg(UserIndex, "Haz click sobre un personaje antes.", FontTypeNames.FONTTYPE_INFO);
			}
		}

		/*
		 * 'If we got here then packet is complete, copy data back to original
		 * queue
		 */
		Declaraciones.UserList[UserIndex].incomingData.CopyBuffer(buffer);

		/* FIXME: ErrHandler : */
		int ERROR;
		/* ERROR = Err . Number */
		/* FIXME: ON ERROR GOTO 0 */

		/* 'Destroy auxiliar buffer */
		buffer = null;

		if (ERROR != 0) {
			throw new RuntimeException("Err . Raise ERROR");
		}
	}

	/* '' */
	/* ' Handles the "ForceMIDIToMap" message. */
	/* ' */
	/* ' @param userIndex The index of the user sending the message. */

	static void HanldeForceMIDIToMap(int UserIndex) {
		/* '*************************************************** */
		/* 'Author: Nicolas Matias Gonzalez (NIGO) */
		/* 'Last Modification: 12/29/06 */
		/* ' */
		/* '*************************************************** */
		if (Declaraciones.UserList[UserIndex].incomingData.length < 4) {
			throw new RuntimeException("Err . Raise UserList ( UserIndex ) . incomingData . NotEnoughDataErrCode");
			return;
		}

		/* 'Remove packet ID */
		Declaraciones.UserList[UserIndex].incomingData.ReadByte();

		int midiID;
		int mapa;

		midiID = Declaraciones.UserList[UserIndex].incomingData.ReadByte();
		mapa = Declaraciones.UserList[UserIndex].incomingData.ReadInteger();

		/* 'Solo dioses, admins y RMS */
		if (Declaraciones.UserList[UserIndex].flags.Privilegios
				&& (PlayerType.Dios || PlayerType.Admin || PlayerType.RoleMaster)) {
			/* 'Si el mapa no fue enviado tomo el actual */
			if (!Extra.InMapBounds(mapa, 50, 50)) {
				mapa = Declaraciones.UserList[UserIndex].Pos.Map;
			}

			if (midiID == 0) {
				/* 'Ponemos el default del mapa */
				modSendData.SendData(SendTarget.toMap, mapa,
						PrepareMessagePlayMidi(Declaraciones.MapInfo[Declaraciones.UserList[UserIndex].Pos.Map].Music));
			} else {
				/* 'Ponemos el pedido por el GM */
				modSendData.SendData(SendTarget.toMap, mapa, PrepareMessagePlayMidi(midiID));
			}
		}
	}

	/* '' */
	/* ' Handles the "ForceWAVEToMap" message. */
	/* ' */
	/* ' @param userIndex The index of the user sending the message. */

	static void HandleForceWAVEToMap(int UserIndex) {
		/* '*************************************************** */
		/* 'Author: Nicolas Matias Gonzalez (NIGO) */
		/* 'Last Modification: 12/29/06 */
		/* ' */
		/* '*************************************************** */
		if (Declaraciones.UserList[UserIndex].incomingData.length < 6) {
			throw new RuntimeException("Err . Raise UserList ( UserIndex ) . incomingData . NotEnoughDataErrCode");
			return;
		}

		/* 'Remove packet ID */
		Declaraciones.UserList[UserIndex].incomingData.ReadByte();

		int waveID;
		int mapa;
		int X;
		int Y;

		waveID = Declaraciones.UserList[UserIndex].incomingData.ReadByte();
		mapa = Declaraciones.UserList[UserIndex].incomingData.ReadInteger();
		X = Declaraciones.UserList[UserIndex].incomingData.ReadByte();
		Y = Declaraciones.UserList[UserIndex].incomingData.ReadByte();

		/* 'Solo dioses, admins y RMS */
		if (Declaraciones.UserList[UserIndex].flags.Privilegios
				&& (PlayerType.Dios || PlayerType.Admin || PlayerType.RoleMaster)) {
			/* 'Si el mapa no fue enviado tomo el actual */
			if (!Extra.InMapBounds(mapa, X, Y)) {
				mapa = Declaraciones.UserList[UserIndex].Pos.Map;
				X = Declaraciones.UserList[UserIndex].Pos.X;
				Y = Declaraciones.UserList[UserIndex].Pos.Y;
			}

			/* 'Ponemos el pedido por el GM */
			modSendData.SendData(SendTarget.toMap, mapa, PrepareMessagePlayWave(waveID, X, Y));
		}
	}

	/* '' */
	/* ' Handles the "RoyalArmyMessage" message. */
	/* ' */
	/* ' @param userIndex The index of the user sending the message. */

	static void HandleRoyalArmyMessage(int UserIndex) {
		/* '*************************************************** */
		/* 'Author: Nicolas Matias Gonzalez (NIGO) */
		/* 'Last Modification: 12/29/06 */
		/* ' */
		/* '*************************************************** */
		if (Declaraciones.UserList[UserIndex].incomingData.length < 3) {
			throw new RuntimeException("Err . Raise UserList ( UserIndex ) . incomingData . NotEnoughDataErrCode");
			return;
		}

		/* FIXME: ON ERROR GOTO ErrHandler */
		/*
		 * 'This packet contains strings, make a copy of the data to prevent
		 * losses if it's not complete yet...
		 */
		clsByteQueue buffer;
		buffer = new clsByteQueue();
		buffer.CopyBuffer(Declaraciones.UserList[UserIndex].incomingData);

		/* 'Remove packet ID */
		buffer.ReadByte();

		String message;
		message = buffer.ReadASCIIString();

		/* 'Solo dioses, admins, semis y RMS */
		if (Declaraciones.UserList[UserIndex].flags.Privilegios
				&& (PlayerType.Dios || PlayerType.Admin || PlayerType.SemiDios || PlayerType.RoleMaster)) {
			modSendData.SendData(SendTarget.ToRealYRMs, 0,
					PrepareMessageConsoleMsg("EJÉRCITO REAL> " + message, FontTypeNames.FONTTYPE_TALK));
		}

		/*
		 * 'If we got here then packet is complete, copy data back to original
		 * queue
		 */
		Declaraciones.UserList[UserIndex].incomingData.CopyBuffer(buffer);

		/* FIXME: ErrHandler : */
		int ERROR;
		/* ERROR = Err . Number */
		/* FIXME: ON ERROR GOTO 0 */

		/* 'Destroy auxiliar buffer */
		buffer = null;

		if (ERROR != 0) {
			throw new RuntimeException("Err . Raise ERROR");
		}
	}

	/* '' */
	/* ' Handles the "ChaosLegionMessage" message. */
	/* ' */
	/* ' @param userIndex The index of the user sending the message. */

	static void HandleChaosLegionMessage(int UserIndex) {
		/* '*************************************************** */
		/* 'Author: Nicolas Matias Gonzalez (NIGO) */
		/* 'Last Modification: 12/29/06 */
		/* ' */
		/* '*************************************************** */
		if (Declaraciones.UserList[UserIndex].incomingData.length < 3) {
			throw new RuntimeException("Err . Raise UserList ( UserIndex ) . incomingData . NotEnoughDataErrCode");
			return;
		}

		/* FIXME: ON ERROR GOTO ErrHandler */
		/*
		 * 'This packet contains strings, make a copy of the data to prevent
		 * losses if it's not complete yet...
		 */
		clsByteQueue buffer;
		buffer = new clsByteQueue();
		buffer.CopyBuffer(Declaraciones.UserList[UserIndex].incomingData);

		/* 'Remove packet ID */
		buffer.ReadByte();

		String message;
		message = buffer.ReadASCIIString();

		/* 'Solo dioses, admins, semis y RMS */
		if (Declaraciones.UserList[UserIndex].flags.Privilegios
				&& (PlayerType.Dios || PlayerType.Admin || PlayerType.SemiDios || PlayerType.RoleMaster)) {
			modSendData.SendData(SendTarget.ToCaosYRMs, 0,
					PrepareMessageConsoleMsg("FUERZAS DEL CAOS> " + message, FontTypeNames.FONTTYPE_TALK));
		}

		/*
		 * 'If we got here then packet is complete, copy data back to original
		 * queue
		 */
		Declaraciones.UserList[UserIndex].incomingData.CopyBuffer(buffer);

		/* FIXME: ErrHandler : */
		int ERROR;
		/* ERROR = Err . Number */
		/* FIXME: ON ERROR GOTO 0 */

		/* 'Destroy auxiliar buffer */
		buffer = null;

		if (ERROR != 0) {
			throw new RuntimeException("Err . Raise ERROR");
		}
	}

	/* '' */
	/* ' Handles the "CitizenMessage" message. */
	/* ' */
	/* ' @param userIndex The index of the user sending the message. */

	static void HandleCitizenMessage(int UserIndex) {
		/* '*************************************************** */
		/* 'Author: Nicolas Matias Gonzalez (NIGO) */
		/* 'Last Modification: 12/29/06 */
		/* ' */
		/* '*************************************************** */
		if (Declaraciones.UserList[UserIndex].incomingData.length < 3) {
			throw new RuntimeException("Err . Raise UserList ( UserIndex ) . incomingData . NotEnoughDataErrCode");
			return;
		}

		/* FIXME: ON ERROR GOTO ErrHandler */
		/*
		 * 'This packet contains strings, make a copy of the data to prevent
		 * losses if it's not complete yet...
		 */
		clsByteQueue buffer;
		buffer = new clsByteQueue();
		buffer.CopyBuffer(Declaraciones.UserList[UserIndex].incomingData);

		/* 'Remove packet ID */
		buffer.ReadByte();

		String message;
		message = buffer.ReadASCIIString();

		/* 'Solo dioses, admins, semis y RMS */
		if (Declaraciones.UserList[UserIndex].flags.Privilegios
				&& (PlayerType.Dios || PlayerType.Admin || PlayerType.SemiDios || PlayerType.RoleMaster)) {
			modSendData.SendData(SendTarget.ToCiudadanosYRMs, 0,
					PrepareMessageConsoleMsg("CIUDADANOS> " + message, FontTypeNames.FONTTYPE_TALK));
		}

		/*
		 * 'If we got here then packet is complete, copy data back to original
		 * queue
		 */
		Declaraciones.UserList[UserIndex].incomingData.CopyBuffer(buffer);

		/* FIXME: ErrHandler : */
		int ERROR;
		/* ERROR = Err . Number */
		/* FIXME: ON ERROR GOTO 0 */

		/* 'Destroy auxiliar buffer */
		buffer = null;

		if (ERROR != 0) {
			throw new RuntimeException("Err . Raise ERROR");
		}
	}

	/* '' */
	/* ' Handles the "CriminalMessage" message. */
	/* ' */
	/* ' @param userIndex The index of the user sending the message. */

	static void HandleCriminalMessage(int UserIndex) {
		/* '*************************************************** */
		/* 'Author: Nicolas Matias Gonzalez (NIGO) */
		/* 'Last Modification: 12/29/06 */
		/* ' */
		/* '*************************************************** */
		if (Declaraciones.UserList[UserIndex].incomingData.length < 3) {
			throw new RuntimeException("Err . Raise UserList ( UserIndex ) . incomingData . NotEnoughDataErrCode");
			return;
		}

		/* FIXME: ON ERROR GOTO ErrHandler */
		/*
		 * 'This packet contains strings, make a copy of the data to prevent
		 * losses if it's not complete yet...
		 */
		clsByteQueue buffer;
		buffer = new clsByteQueue();
		buffer.CopyBuffer(Declaraciones.UserList[UserIndex].incomingData);

		/* 'Remove packet ID */
		buffer.ReadByte();

		String message;
		message = buffer.ReadASCIIString();

		/* 'Solo dioses, admins y RMS */
		if (Declaraciones.UserList[UserIndex].flags.Privilegios
				&& (PlayerType.Dios || PlayerType.Admin || PlayerType.RoleMaster)) {
			modSendData.SendData(SendTarget.ToCriminalesYRMs, 0,
					PrepareMessageConsoleMsg("CRIMINALES> " + message, FontTypeNames.FONTTYPE_TALK));
		}

		/*
		 * 'If we got here then packet is complete, copy data back to original
		 * queue
		 */
		Declaraciones.UserList[UserIndex].incomingData.CopyBuffer(buffer);

		/* FIXME: ErrHandler : */
		int ERROR;
		/* ERROR = Err . Number */
		/* FIXME: ON ERROR GOTO 0 */

		/* 'Destroy auxiliar buffer */
		buffer = null;

		if (ERROR != 0) {
			throw new RuntimeException("Err . Raise ERROR");
		}
	}

	/* '' */
	/* ' Handles the "TalkAsNPC" message. */
	/* ' */
	/* ' @param userIndex The index of the user sending the message. */

	static void HandleTalkAsNPC(int UserIndex) {
		/* '*************************************************** */
		/* 'Author: Nicolas Matias Gonzalez (NIGO) */
		/* 'Last Modification: 12/29/06 */
		/* ' */
		/* '*************************************************** */
		if (Declaraciones.UserList[UserIndex].incomingData.length < 3) {
			throw new RuntimeException("Err . Raise UserList ( UserIndex ) . incomingData . NotEnoughDataErrCode");
			return;
		}

		/* FIXME: ON ERROR GOTO ErrHandler */
		/*
		 * 'This packet contains strings, make a copy of the data to prevent
		 * losses if it's not complete yet...
		 */
		clsByteQueue buffer;
		buffer = new clsByteQueue();
		buffer.CopyBuffer(Declaraciones.UserList[UserIndex].incomingData);

		/* 'Remove packet ID */
		buffer.ReadByte();

		String message;
		message = buffer.ReadASCIIString();

		/* 'Solo dioses, admins y RMS */
		if (Declaraciones.UserList[UserIndex].flags.Privilegios
				&& (PlayerType.Dios || PlayerType.Admin || PlayerType.RoleMaster)) {
			/* 'Asegurarse haya un NPC seleccionado */
			if (Declaraciones.UserList[UserIndex].flags.TargetNPC > 0) {
				modSendData.SendData(SendTarget.ToNPCArea, Declaraciones.UserList[UserIndex].flags.TargetNPC,
						PrepareMessageChatOverHead(message,
								Declaraciones.Npclist[Declaraciones.UserList[UserIndex].flags.TargetNPC].Char.CharIndex,
								0x00ffffff));
			} else {
				WriteConsoleMsg(UserIndex,
						"Debes seleccionar el NPC por el que quieres hablar antes de usar este comando.",
						FontTypeNames.FONTTYPE_INFO);
			}
		}

		/*
		 * 'If we got here then packet is complete, copy data back to original
		 * queue
		 */
		Declaraciones.UserList[UserIndex].incomingData.CopyBuffer(buffer);

		/* FIXME: ErrHandler : */
		int ERROR;
		/* ERROR = Err . Number */
		/* FIXME: ON ERROR GOTO 0 */

		/* 'Destroy auxiliar buffer */
		buffer = null;

		if (ERROR != 0) {
			throw new RuntimeException("Err . Raise ERROR");
		}
	}

	/* '' */
	/* ' Handles the "DestroyAllItemsInArea" message. */
	/* ' */
	/* ' @param userIndex The index of the user sending the message. */

	static void HandleDestroyAllItemsInArea(int UserIndex) {
		/* '*************************************************** */
		/* 'Author: Nicolas Matias Gonzalez (NIGO) */
		/* 'Last Modification: 12/30/06 */
		/* ' */
		/* '*************************************************** */
		/* 'Remove packet ID */
		Declaraciones.UserList[UserIndex].incomingData.ReadByte();

		if (Declaraciones.UserList[UserIndex].flags.Privilegios
				&& (PlayerType.User || PlayerType.Consejero || PlayerType.SemiDios)) {
			return;
		}

		int X;
		int Y;
		boolean bIsExit;

		for (Y = (Declaraciones.UserList[UserIndex].Pos.Y - Declaraciones.MinYBorder
				+ 1); Y <= (Declaraciones.UserList[UserIndex].Pos.Y + Declaraciones.MinYBorder - 1); Y++) {
			for (X = (Declaraciones.UserList[UserIndex].Pos.X - Declaraciones.MinXBorder
					+ 1); X <= (Declaraciones.UserList[UserIndex].Pos.X + Declaraciones.MinXBorder - 1); X++) {
				if (X > 0 && Y > 0 && X < 101 && Y < 101) {
					if (Declaraciones.MapData[Declaraciones.UserList[UserIndex].Pos.Map][X][Y].ObjInfo.ObjIndex > 0) {
						bIsExit = Declaraciones.MapData[Declaraciones.UserList[UserIndex].Pos.Map][X][Y].TileExit.Map > 0;
						if (Extra.ItemNoEsDeMapa(
								Declaraciones.MapData[Declaraciones.UserList[UserIndex].Pos.Map][X][Y].ObjInfo.ObjIndex,
								bIsExit)) {
							InvUsuario.EraseObj(Declaraciones.MAX_INVENTORY_OBJS,
									Declaraciones.UserList[UserIndex].Pos.Map, X, Y);
						}
					}
				}
			}
		}

		General.LogGM(Declaraciones.UserList[UserIndex].Name, "/MASSDEST");
	}

	/* '' */
	/* ' Handles the "AcceptRoyalCouncilMember" message. */
	/* ' */
	/* ' @param userIndex The index of the user sending the message. */

	static void HandleAcceptRoyalCouncilMember(int UserIndex) {
		/* '*************************************************** */
		/* 'Author: Nicolas Matias Gonzalez (NIGO) */
		/* 'Last Modification: 12/30/06 */
		/* ' */
		/* '*************************************************** */
		if (Declaraciones.UserList[UserIndex].incomingData.length < 3) {
			throw new RuntimeException("Err . Raise UserList ( UserIndex ) . incomingData . NotEnoughDataErrCode");
			return;
		}

		/* FIXME: ON ERROR GOTO ErrHandler */
		/*
		 * 'This packet contains strings, make a copy of the data to prevent
		 * losses if it's not complete yet...
		 */
		clsByteQueue buffer;
		buffer = new clsByteQueue();
		buffer.CopyBuffer(Declaraciones.UserList[UserIndex].incomingData);

		/* 'Remove packet ID */
		buffer.ReadByte();

		String UserName;
		int tUser;
		int LoopC;

		UserName = buffer.ReadASCIIString();

		if ((!Declaraciones.UserList[UserIndex].flags.Privilegios && PlayerType.RoleMaster) != 0
				&& (Declaraciones.UserList[UserIndex].flags.Privilegios && (PlayerType.Admin || PlayerType.Dios))) {
			tUser = Extra.NameIndex(UserName);
			if (tUser <= 0) {
				WriteConsoleMsg(UserIndex, "Usuario offline", FontTypeNames.FONTTYPE_INFO);
			} else {
				modSendData.SendData(SendTarget.ToAll, 0,
						PrepareMessageConsoleMsg(UserName + " fue aceptado en el honorable Consejo Real de Banderbill.",
								FontTypeNames.FONTTYPE_CONSEJO));
				if (Declaraciones.UserList[tUser].flags.Privilegios && PlayerType.ChaosCouncil) {
					Declaraciones.UserList[tUser].flags.Privilegios = Declaraciones.UserList[tUser].flags.Privilegios
							- PlayerType.ChaosCouncil;
				}
				if (!Declaraciones.UserList[tUser].flags.Privilegios && PlayerType.RoyalCouncil) {
					Declaraciones.UserList[tUser].flags.Privilegios = Declaraciones.UserList[tUser].flags.Privilegios
							+ PlayerType.RoyalCouncil;
				}

				UsUaRiOs.WarpUserChar(tUser, Declaraciones.UserList[tUser].Pos.Map, Declaraciones.UserList[tUser].Pos.X,
						Declaraciones.UserList[tUser].Pos.Y, false);
			}
		}

		/*
		 * 'If we got here then packet is complete, copy data back to original
		 * queue
		 */
		Declaraciones.UserList[UserIndex].incomingData.CopyBuffer(buffer);

		/* FIXME: ErrHandler : */
		int ERROR;
		/* ERROR = Err . Number */
		/* FIXME: ON ERROR GOTO 0 */

		/* 'Destroy auxiliar buffer */
		buffer = null;

		if (ERROR != 0) {
			throw new RuntimeException("Err . Raise ERROR");
		}
	}

	/* '' */
	/* ' Handles the "ChaosCouncilMember" message. */
	/* ' */
	/* ' @param userIndex The index of the user sending the message. */

	static void HandleAcceptChaosCouncilMember(int UserIndex) {
		/* '*************************************************** */
		/* 'Author: Nicolas Matias Gonzalez (NIGO) */
		/* 'Last Modification: 12/30/06 */
		/* ' */
		/* '*************************************************** */
		if (Declaraciones.UserList[UserIndex].incomingData.length < 3) {
			throw new RuntimeException("Err . Raise UserList ( UserIndex ) . incomingData . NotEnoughDataErrCode");
			return;
		}

		/* FIXME: ON ERROR GOTO ErrHandler */
		/*
		 * 'This packet contains strings, make a copy of the data to prevent
		 * losses if it's not complete yet...
		 */
		clsByteQueue buffer;
		buffer = new clsByteQueue();
		buffer.CopyBuffer(Declaraciones.UserList[UserIndex].incomingData);

		/* 'Remove packet ID */
		buffer.ReadByte();

		String UserName;
		int tUser;
		int LoopC;

		UserName = buffer.ReadASCIIString();

		if ((!Declaraciones.UserList[UserIndex].flags.Privilegios && PlayerType.RoleMaster) != 0
				&& (Declaraciones.UserList[UserIndex].flags.Privilegios && (PlayerType.Admin || PlayerType.Dios))) {
			tUser = Extra.NameIndex(UserName);
			if (tUser <= 0) {
				WriteConsoleMsg(UserIndex, "Usuario offline", FontTypeNames.FONTTYPE_INFO);
			} else {
				modSendData.SendData(SendTarget.ToAll, 0, PrepareMessageConsoleMsg(
						UserName + " fue aceptado en el Concilio de las Sombras.", FontTypeNames.FONTTYPE_CONSEJO));

				if (Declaraciones.UserList[tUser].flags.Privilegios && PlayerType.RoyalCouncil) {
					Declaraciones.UserList[tUser].flags.Privilegios = Declaraciones.UserList[tUser].flags.Privilegios
							- PlayerType.RoyalCouncil;
				}
				if (!Declaraciones.UserList[tUser].flags.Privilegios && PlayerType.ChaosCouncil) {
					Declaraciones.UserList[tUser].flags.Privilegios = Declaraciones.UserList[tUser].flags.Privilegios
							+ PlayerType.ChaosCouncil;
				}

				UsUaRiOs.WarpUserChar(tUser, Declaraciones.UserList[tUser].Pos.Map, Declaraciones.UserList[tUser].Pos.X,
						Declaraciones.UserList[tUser].Pos.Y, false);
			}
		}

		/*
		 * 'If we got here then packet is complete, copy data back to original
		 * queue
		 */
		Declaraciones.UserList[UserIndex].incomingData.CopyBuffer(buffer);

		/* FIXME: ErrHandler : */
		int ERROR;
		/* ERROR = Err . Number */
		/* FIXME: ON ERROR GOTO 0 */

		/* 'Destroy auxiliar buffer */
		buffer = null;

		if (ERROR != 0) {
			throw new RuntimeException("Err . Raise ERROR");
		}
	}

	/* '' */
	/* ' Handles the "ItemsInTheFloor" message. */
	/* ' */
	/* ' @param userIndex The index of the user sending the message. */

	static void HandleItemsInTheFloor(int UserIndex) {
		/* '*************************************************** */
		/* 'Author: Nicolas Matias Gonzalez (NIGO) */
		/* 'Last Modification: 12/30/06 */
		/* ' */
		/* '*************************************************** */
		/* 'Remove packet ID */
		Declaraciones.UserList[UserIndex].incomingData.ReadByte();

		if (Declaraciones.UserList[UserIndex].flags.Privilegios
				&& (PlayerType.User || PlayerType.Consejero || PlayerType.SemiDios)) {
			return;
		}

		int tObj;
		String lista;
		int X;
		int Y;

		for (X = (5); X <= (95); X++) {
			for (Y = (5); Y <= (95); Y++) {
				tObj = Declaraciones.MapData[Declaraciones.UserList[UserIndex].Pos.Map][X][Y].ObjInfo.ObjIndex;
				if (tObj > 0) {
					if (Declaraciones.ObjData[tObj].OBJType != eOBJType.otArboles) {
						WriteConsoleMsg(UserIndex, "(" + X + "," + Y + ") " + Declaraciones.ObjData[tObj].Name,
								FontTypeNames.FONTTYPE_INFO);
					}
				}
			}
		}
	}

	/* '' */
	/* ' Handles the "MakeDumb" message. */
	/* ' */
	/* ' @param userIndex The index of the user sending the message. */

	static void HandleMakeDumb(int UserIndex) {
		/* '*************************************************** */
		/* 'Author: Nicolas Matias Gonzalez (NIGO) */
		/* 'Last Modification: 12/30/06 */
		/* ' */
		/* '*************************************************** */
		if (Declaraciones.UserList[UserIndex].incomingData.length < 3) {
			throw new RuntimeException("Err . Raise UserList ( UserIndex ) . incomingData . NotEnoughDataErrCode");
			return;
		}

		/* FIXME: ON ERROR GOTO ErrHandler */
		/*
		 * 'This packet contains strings, make a copy of the data to prevent
		 * losses if it's not complete yet...
		 */
		clsByteQueue buffer;
		buffer = new clsByteQueue();
		buffer.CopyBuffer(Declaraciones.UserList[UserIndex].incomingData);

		/* 'Remove packet ID */
		buffer.ReadByte();

		String UserName;
		int tUser;

		UserName = buffer.ReadASCIIString();

		if (((Declaraciones.UserList[UserIndex].flags.Privilegios && (PlayerType.Admin || PlayerType.Dios)) != 0
				|| ((Declaraciones.UserList[UserIndex].flags.Privilegios
						&& (PlayerType.SemiDios || PlayerType.RoleMaster)) == (PlayerType.SemiDios
								|| PlayerType.RoleMaster)))) {
			tUser = Extra.NameIndex(UserName);
			/* 'para deteccion de aoice */
			if (tUser <= 0) {
				WriteConsoleMsg(UserIndex, "Usuario offline.", FontTypeNames.FONTTYPE_INFO);
			} else {
				WriteDumb(tUser);
			}
		}

		/*
		 * 'If we got here then packet is complete, copy data back to original
		 * queue
		 */
		Declaraciones.UserList[UserIndex].incomingData.CopyBuffer(buffer);

		/* FIXME: ErrHandler : */
		int ERROR;
		/* ERROR = Err . Number */
		/* FIXME: ON ERROR GOTO 0 */

		/* 'Destroy auxiliar buffer */
		buffer = null;

		if (ERROR != 0) {
			throw new RuntimeException("Err . Raise ERROR");
		}
	}

	/* '' */
	/* ' Handles the "MakeDumbNoMore" message. */
	/* ' */
	/* ' @param userIndex The index of the user sending the message. */

	static void HandleMakeDumbNoMore(int UserIndex) {
		/* '*************************************************** */
		/* 'Author: Nicolas Matias Gonzalez (NIGO) */
		/* 'Last Modification: 12/30/06 */
		/* ' */
		/* '*************************************************** */
		if (Declaraciones.UserList[UserIndex].incomingData.length < 3) {
			throw new RuntimeException("Err . Raise UserList ( UserIndex ) . incomingData . NotEnoughDataErrCode");
			return;
		}

		/* FIXME: ON ERROR GOTO ErrHandler */
		/*
		 * 'This packet contains strings, make a copy of the data to prevent
		 * losses if it's not complete yet...
		 */
		clsByteQueue buffer;
		buffer = new clsByteQueue();
		buffer.CopyBuffer(Declaraciones.UserList[UserIndex].incomingData);

		/* 'Remove packet ID */
		buffer.ReadByte();

		String UserName;
		int tUser;

		UserName = buffer.ReadASCIIString();

		if (((Declaraciones.UserList[UserIndex].flags.Privilegios && (PlayerType.Admin || PlayerType.Dios)) != 0
				|| ((Declaraciones.UserList[UserIndex].flags.Privilegios
						&& (PlayerType.SemiDios || PlayerType.RoleMaster)) == (PlayerType.SemiDios
								|| PlayerType.RoleMaster)))) {
			tUser = Extra.NameIndex(UserName);
			/* 'para deteccion de aoice */
			if (tUser <= 0) {
				WriteConsoleMsg(UserIndex, "Usuario offline.", FontTypeNames.FONTTYPE_INFO);
			} else {
				WriteDumbNoMore(tUser);
				FlushBuffer(tUser);
			}
		}

		/*
		 * 'If we got here then packet is complete, copy data back to original
		 * queue
		 */
		Declaraciones.UserList[UserIndex].incomingData.CopyBuffer(buffer);

		/* FIXME: ErrHandler : */
		int ERROR;
		/* ERROR = Err . Number */
		/* FIXME: ON ERROR GOTO 0 */

		/* 'Destroy auxiliar buffer */
		buffer = null;

		if (ERROR != 0) {
			throw new RuntimeException("Err . Raise ERROR");
		}
	}

	/* '' */
	/* ' Handles the "DumpIPTables" message. */
	/* ' */
	/* ' @param userIndex The index of the user sending the message. */

	static void HandleDumpIPTables(int UserIndex) {
 /* '*************************************************** */
 /* 'Author: Nicolas Matias Gonzalez (NIGO) */
 /* 'Last Modification: 12/30/06 */
 /* ' */
 /* '*************************************************** */
  /* 'Remove packet ID */
  Declaraciones.UserList[UserIndex].incomingData.ReadByte();
  
  if (Declaraciones.UserList[UserIndex].flags.Privilegios && (PlayerType.User || PlayerType.Consejero || PlayerType.SemiDios)) {
  return;
  }
  
  SecurityIp.DumpTables;
}

	/* '' */
	/* ' Handles the "CouncilKick" message. */
	/* ' */
	/* ' @param userIndex The index of the user sending the message. */

	static void HandleCouncilKick(int UserIndex) {
		/* '*************************************************** */
		/* 'Author: Nicolas Matias Gonzalez (NIGO) */
		/* 'Last Modification: 12/30/06 */
		/* ' */
		/* '*************************************************** */
		if (Declaraciones.UserList[UserIndex].incomingData.length < 3) {
			throw new RuntimeException("Err . Raise UserList ( UserIndex ) . incomingData . NotEnoughDataErrCode");
			return;
		}

		/* FIXME: ON ERROR GOTO ErrHandler */
		/*
		 * 'This packet contains strings, make a copy of the data to prevent
		 * losses if it's not complete yet...
		 */
		clsByteQueue buffer;
		buffer = new clsByteQueue();
		buffer.CopyBuffer(Declaraciones.UserList[UserIndex].incomingData);

		/* 'Remove packet ID */
		buffer.ReadByte();

		String UserName;
		int tUser;

		UserName = buffer.ReadASCIIString();

		if ((!Declaraciones.UserList[UserIndex].flags.Privilegios && PlayerType.RoleMaster) != 0
				&& (Declaraciones.UserList[UserIndex].flags.Privilegios
						&& (PlayerType.Admin || PlayerType.Dios)) != 0) {
			tUser = Extra.NameIndex(UserName);
			if (tUser <= 0) {
				if (General.FileExist(Declaraciones.CharPath + UserName + ".chr")) {
					WriteConsoleMsg(UserIndex, "Usuario offline, echando de los consejos.",
							FontTypeNames.FONTTYPE_INFO);
					ES.WriteVar(Declaraciones.CharPath + UserName + ".chr", "CONSEJO", "PERTENECE", 0);
					ES.WriteVar(Declaraciones.CharPath + UserName + ".chr", "CONSEJO", "PERTENECECAOS", 0);
				} else {
					WriteConsoleMsg(UserIndex,
							"No se encuentra el charfile " + Declaraciones.CharPath + UserName + ".chr",
							FontTypeNames.FONTTYPE_INFO);
				}
			} else {
				if (Declaraciones.UserList[tUser].flags.Privilegios && PlayerType.RoyalCouncil) {
					WriteConsoleMsg(tUser, "Has sido echado del consejo de Banderbill.", FontTypeNames.FONTTYPE_TALK);
					Declaraciones.UserList[tUser].flags.Privilegios = Declaraciones.UserList[tUser].flags.Privilegios
							- PlayerType.RoyalCouncil;

					UsUaRiOs.WarpUserChar(tUser, Declaraciones.UserList[tUser].Pos.Map,
							Declaraciones.UserList[tUser].Pos.X, Declaraciones.UserList[tUser].Pos.Y, false);
					modSendData.SendData(SendTarget.ToAll, 0, PrepareMessageConsoleMsg(
							UserName + " fue expulsado del consejo de Banderbill.", FontTypeNames.FONTTYPE_CONSEJO));
				}

				if (Declaraciones.UserList[tUser].flags.Privilegios && PlayerType.ChaosCouncil) {
					WriteConsoleMsg(tUser, "Has sido echado del Concilio de las Sombras.", FontTypeNames.FONTTYPE_TALK);
					Declaraciones.UserList[tUser].flags.Privilegios = Declaraciones.UserList[tUser].flags.Privilegios
							- PlayerType.ChaosCouncil;

					UsUaRiOs.WarpUserChar(tUser, Declaraciones.UserList[tUser].Pos.Map,
							Declaraciones.UserList[tUser].Pos.X, Declaraciones.UserList[tUser].Pos.Y, false);
					modSendData.SendData(SendTarget.ToAll, 0, PrepareMessageConsoleMsg(
							UserName + " fue expulsado del Concilio de las Sombras.", FontTypeNames.FONTTYPE_CONSEJO));
				}
			}
		}

		/*
		 * 'If we got here then packet is complete, copy data back to original
		 * queue
		 */
		Declaraciones.UserList[UserIndex].incomingData.CopyBuffer(buffer);

		/* FIXME: ErrHandler : */
		int ERROR;
		/* ERROR = Err . Number */
		/* FIXME: ON ERROR GOTO 0 */

		/* 'Destroy auxiliar buffer */
		buffer = null;

		if (ERROR != 0) {
			throw new RuntimeException("Err . Raise ERROR");
		}
	}

	/* '' */
	/* ' Handles the "SetTrigger" message. */
	/* ' */
	/* ' @param userIndex The index of the user sending the message. */

	static void HandleSetTrigger(int UserIndex) {
		/* '*************************************************** */
		/* 'Author: Nicolas Matias Gonzalez (NIGO) */
		/* 'Last Modification: 12/30/06 */
		/* ' */
		/* '*************************************************** */
		if (Declaraciones.UserList[UserIndex].incomingData.length < 2) {
			throw new RuntimeException("Err . Raise UserList ( UserIndex ) . incomingData . NotEnoughDataErrCode");
			return;
		}

		/* 'Remove packet ID */
		Declaraciones.UserList[UserIndex].incomingData.ReadByte();

		int tTrigger;
		String tLog;

		tTrigger = Declaraciones.UserList[UserIndex].incomingData.ReadByte();

		if (Declaraciones.UserList[UserIndex].flags.Privilegios
				&& (PlayerType.User || PlayerType.Consejero || PlayerType.SemiDios || PlayerType.RoleMaster)) {
			return;
		}

		if (tTrigger >= 0) {
			Declaraciones.MapData[Declaraciones.UserList[UserIndex].Pos.Map][Declaraciones.UserList[UserIndex].Pos.X][Declaraciones.UserList[UserIndex].Pos.Y].trigger = tTrigger;
			tLog = "Trigger " + tTrigger + " en mapa " + Declaraciones.UserList[UserIndex].Pos.Map + " "
					+ Declaraciones.UserList[UserIndex].Pos.X + "," + Declaraciones.UserList[UserIndex].Pos.Y;

			General.LogGM(Declaraciones.UserList[UserIndex].Name, tLog);
			WriteConsoleMsg(UserIndex, tLog, FontTypeNames.FONTTYPE_INFO);
		}
	}

	/* '' */
	/* ' Handles the "AskTrigger" message. */
	/* ' */
	/* ' @param userIndex The index of the user sending the message. */

	static void HandleAskTrigger(int UserIndex) {
		/* '*************************************************** */
		/* 'Author: Nicolas Matias Gonzalez (NIGO) */
		/* 'Last Modification: 04/13/07 */
		/* ' */
		/* '*************************************************** */
		int tTrigger;

		/* 'Remove packet ID */
		Declaraciones.UserList[UserIndex].incomingData.ReadByte();

		if (Declaraciones.UserList[UserIndex].flags.Privilegios
				&& (PlayerType.User || PlayerType.Consejero || PlayerType.SemiDios || PlayerType.RoleMaster)) {
			return;
		}

		tTrigger = Declaraciones.MapData[Declaraciones.UserList[UserIndex].Pos.Map][Declaraciones.UserList[UserIndex].Pos.X][Declaraciones.UserList[UserIndex].Pos.Y].trigger;

		General.LogGM(Declaraciones.UserList[UserIndex].Name,
				"Miro el trigger en " + Declaraciones.UserList[UserIndex].Pos.Map + ","
						+ Declaraciones.UserList[UserIndex].Pos.X + "," + Declaraciones.UserList[UserIndex].Pos.Y
						+ ". Era " + tTrigger);

		WriteConsoleMsg(UserIndex,
				"Trigger " + tTrigger + " en mapa " + Declaraciones.UserList[UserIndex].Pos.Map + " "
						+ Declaraciones.UserList[UserIndex].Pos.X + ", " + Declaraciones.UserList[UserIndex].Pos.Y,
				FontTypeNames.FONTTYPE_INFO);
	}

	/* '' */
	/* ' Handles the "BannedIPList" message. */
	/* ' */
	/* ' @param userIndex The index of the user sending the message. */

	static void HandleBannedIPList(int UserIndex) {
		/* '*************************************************** */
		/* 'Author: Nicolas Matias Gonzalez (NIGO) */
		/* 'Last Modification: 12/30/06 */
		/* ' */
		/* '*************************************************** */
		/* 'Remove packet ID */
		Declaraciones.UserList[UserIndex].incomingData.ReadByte();

		if (Declaraciones.UserList[UserIndex].flags.Privilegios
				&& (PlayerType.User || PlayerType.Consejero || PlayerType.SemiDios || PlayerType.RoleMaster)) {
			return;
		}

		String lista;
		int LoopC;

		General.LogGM(Declaraciones.UserList[UserIndex].Name, "/BANIPLIST");

		for (LoopC = (1); LoopC <= (Declaraciones.BanIps.Count); LoopC++) {
			lista = lista + Declaraciones.BanIps.Item[LoopC] + ", ";
		}

		if (vb6.LenB(lista) != 0) {
			lista = vb6.Left(lista, vb6.Len(lista) - 2);
		}

		WriteConsoleMsg(UserIndex, lista, FontTypeNames.FONTTYPE_INFO);
	}

	/* '' */
	/* ' Handles the "BannedIPReload" message. */
	/* ' */
	/* ' @param userIndex The index of the user sending the message. */

	static void HandleBannedIPReload(int UserIndex) {
		/* '*************************************************** */
		/* 'Author: Nicolas Matias Gonzalez (NIGO) */
		/* 'Last Modification: 12/30/06 */
		/* ' */
		/* '*************************************************** */
		/* 'Remove packet ID */
		Declaraciones.UserList[UserIndex].incomingData.ReadByte();

		if (Declaraciones.UserList[UserIndex].flags.Privilegios
				&& (PlayerType.User || PlayerType.Consejero || PlayerType.SemiDios || PlayerType.RoleMaster)) {
			return;
		}

		Admin.BanIpGuardar();
		Admin.BanIpCargar();
	}

	/* '' */
	/* ' Handles the "GuildBan" message. */
	/* ' */
	/* ' @param userIndex The index of the user sending the message. */

	static void HandleGuildBan(int UserIndex) {
		/* '*************************************************** */
		/* 'Author: Nicolas Matias Gonzalez (NIGO) */
		/* 'Last Modification: 12/30/06 */
		/* ' */
		/* '*************************************************** */
		if (Declaraciones.UserList[UserIndex].incomingData.length < 3) {
			throw new RuntimeException("Err . Raise UserList ( UserIndex ) . incomingData . NotEnoughDataErrCode");
			return;
		}

		/* FIXME: ON ERROR GOTO ErrHandler */
		/*
		 * 'This packet contains strings, make a copy of the data to prevent
		 * losses if it's not complete yet...
		 */
		clsByteQueue buffer;
		buffer = new clsByteQueue();
		buffer.CopyBuffer(Declaraciones.UserList[UserIndex].incomingData);

		/* 'Remove packet ID */
		buffer.ReadByte();

		String GuildName;
		int cantMembers;
		int LoopC;
		String member;
		int Count;
		int tIndex;
		String tFile;

		GuildName = buffer.ReadASCIIString();

		if ((!Declaraciones.UserList[UserIndex].flags.Privilegios && PlayerType.RoleMaster) != 0
				&& (Declaraciones.UserList[UserIndex].flags.Privilegios && (PlayerType.Admin || PlayerType.Dios))) {
			tFile = vb6.App.Instance().Path + "\\guilds\\" + GuildName + "-members.mem";

			if (!General.FileExist(tFile)) {
				WriteConsoleMsg(UserIndex, "No existe el clan: " + GuildName, FontTypeNames.FONTTYPE_INFO);
			} else {
				modSendData.SendData(SendTarget.ToAll, 0,
						PrepareMessageConsoleMsg(
								Declaraciones.UserList[UserIndex].Name + " baneó al clan " + vb6.UCase(GuildName),
								FontTypeNames.FONTTYPE_FIGHT));

				/* 'baneamos a los miembros */
				General.LogGM(Declaraciones.UserList[UserIndex].Name, "BANCLAN a " + vb6.UCase(GuildName));

				cantMembers = vb6.val(ES.GetVar(tFile, "INIT", "NroMembers"));

				for (LoopC = (1); LoopC <= (cantMembers); LoopC++) {
					member = ES.GetVar(tFile, "Members", "Member" + LoopC);
					/* 'member es la victima */
					ES.Ban(member, "Administracion del servidor", "Clan Banned");

					modSendData.SendData(SendTarget.ToAll, 0,
							PrepareMessageConsoleMsg(
									"   " + member + "<" + GuildName + "> ha sido expulsado del servidor.",
									FontTypeNames.FONTTYPE_FIGHT));

					tIndex = Extra.NameIndex(member);
					if (tIndex > 0) {
						/* 'esta online */
						Declaraciones.UserList[tIndex].flags.Ban = 1;
						TCP.CloseSocket(tIndex);
					}

					/* 'ponemos el flag de ban a 1 */
					ES.WriteVar(Declaraciones.CharPath + member + ".chr", "FLAGS", "Ban", "1");
					/* 'ponemos la pena */
					Count = vb6.val(ES.GetVar(Declaraciones.CharPath + member + ".chr", "PENAS", "Cant"));
					ES.WriteVar(Declaraciones.CharPath + member + ".chr", "PENAS", "Cant", Count + 1);
					ES.WriteVar(Declaraciones.CharPath + member + ".chr", "PENAS", "P" + Count + 1,
							vb6.LCase(Declaraciones.UserList[UserIndex].Name) + ": BAN AL CLAN: " + GuildName + " "
									+ Date + " " + vb6.time());
				}
			}
		}

		/*
		 * 'If we got here then packet is complete, copy data back to original
		 * queue
		 */
		Declaraciones.UserList[UserIndex].incomingData.CopyBuffer(buffer);

		/* FIXME: ErrHandler : */
		int ERROR;
		/* ERROR = Err . Number */
		/* FIXME: ON ERROR GOTO 0 */

		/* 'Destroy auxiliar buffer */
		buffer = null;

		if (ERROR != 0) {
			throw new RuntimeException("Err . Raise ERROR");
		}
	}

	/* '' */
	/* ' Handles the "BanIP" message. */
	/* ' */
	/* ' @param userIndex The index of the user sending the message. */

	static void HandleBanIP(int UserIndex) {
		/* '*************************************************** */
		/* 'Author: Juan Martín Sotuyo Dodero (Maraxus) */
		/* 'Last Modification: 07/02/09 */
		/* 'Agregado un CopyBuffer porque se producia un bucle */
		/* 'inifito al intentar banear una ip ya baneada. (NicoNZ) */
		/*
		 * '07/02/09 Pato - Ahora no es posible saber si un gm está o no online.
		 */
		/* '*************************************************** */
		if (Declaraciones.UserList[UserIndex].incomingData.length < 6) {
			throw new RuntimeException("Err . Raise UserList ( UserIndex ) . incomingData . NotEnoughDataErrCode");
			return;
		}

		/* FIXME: ON ERROR GOTO ErrHandler */
		/*
		 * 'This packet contains strings, make a copy of the data to prevent
		 * losses if it's not complete yet...
		 */
		clsByteQueue buffer;
		buffer = new clsByteQueue();
		buffer.CopyBuffer(Declaraciones.UserList[UserIndex].incomingData);

		/* 'Remove packet ID */
		buffer.ReadByte();

		String bannedIP;
		int tUser;
		String Reason;
		int i;

		/* ' Is it by ip?? */
		if (buffer.ReadBoolean()) {
			bannedIP = buffer.ReadByte() + ".";
			bannedIP = bannedIP + buffer.ReadByte() + ".";
			bannedIP = bannedIP + buffer.ReadByte() + ".";
			bannedIP = bannedIP + buffer.ReadByte();
		} else {
			tUser = Extra.NameIndex(buffer.ReadASCIIString());

			if (tUser > 0) {
				bannedIP = Declaraciones.UserList[tUser].ip;
			}
		}

		Reason = buffer.ReadASCIIString();

		if (Declaraciones.UserList[UserIndex].flags.Privilegios && (PlayerType.Admin || PlayerType.Dios)) {
			if (vb6.LenB(bannedIP) > 0) {
				General.LogGM(Declaraciones.UserList[UserIndex].Name, "/BanIP " + bannedIP + " por " + Reason);

				if (Admin.BanIpBuscar(bannedIP) > 0) {
					WriteConsoleMsg(UserIndex, "La IP " + bannedIP + " ya se encuentra en la lista de bans.",
							FontTypeNames.FONTTYPE_INFO);
				} else {
					Admin.BanIpAgrega(bannedIP);
					modSendData.SendData(SendTarget.ToAdmins, 0, PrepareMessageConsoleMsg(
							Declaraciones.UserList[UserIndex].Name + " baneó la IP " + bannedIP + " por " + Reason,
							FontTypeNames.FONTTYPE_FIGHT));

					/* 'Find every player with that ip and ban him! */
					for (i = (1); i <= (Declaraciones.LastUser); i++) {
						if (Declaraciones.UserList[i].ConnIDValida) {
							if (Declaraciones.UserList[i].ip == bannedIP) {
								Admin.BanCharacter(UserIndex, Declaraciones.UserList[i].Name, "IP POR " + Reason);
							}
						}
					}
				}
			} else if (tUser <= 0) {
				WriteConsoleMsg(UserIndex, "El personaje no está online.", FontTypeNames.FONTTYPE_INFO);
			}
		}

		/*
		 * 'If we got here then packet is complete, copy data back to original
		 * queue
		 */
		Declaraciones.UserList[UserIndex].incomingData.CopyBuffer(buffer);

		/* FIXME: ErrHandler : */
		int ERROR;
		/* ERROR = Err . Number */
		/* FIXME: ON ERROR GOTO 0 */

		/* 'Destroy auxiliar buffer */
		buffer = null;

		if (ERROR != 0) {
			throw new RuntimeException("Err . Raise ERROR");
		}
	}

	/* '' */
	/* ' Handles the "UnbanIP" message. */
	/* ' */
	/* ' @param userIndex The index of the user sending the message. */

	static void HandleUnbanIP(int UserIndex) {
 /* '*************************************************** */
 /* 'Author: Juan Martín Sotuyo Dodero (Maraxus) */
 /* 'Last Modification: 12/30/06 */
 /* ' */
 /* '*************************************************** */
  if (Declaraciones.UserList[UserIndex].incomingData.length<5) {
  throw new RuntimeException("Err . Raise UserList ( UserIndex ) . incomingData . NotEnoughDataErrCode");
  return;
 }
 
  /* 'Remove packet ID */
  Declaraciones.UserList[UserIndex].incomingData.ReadByte();
  
  String bannedIP;
  
  bannedIP = Declaraciones.UserList[UserIndex].incomingData.ReadByte() + ".";
  bannedIP = bannedIP + Declaraciones.UserList[UserIndex].incomingData.ReadByte() + ".";
  bannedIP = bannedIP + Declaraciones.UserList[UserIndex].incomingData.ReadByte() + ".";
  bannedIP = bannedIP + Declaraciones.UserList[UserIndex].incomingData.ReadByte();
  
  if (Declaraciones.UserList[UserIndex].flags.Privilegios && (PlayerType.User || PlayerType.Consejero || PlayerType.SemiDios || PlayerType.RoleMaster)) {
  return;
  }
  
   if (Admin.BanIpQuita(bannedIP)) {
   WriteConsoleMsg(UserIndex, "La IP """ + bannedIP + """ se ha quitado de la lista de bans.", FontTypeNames.FONTTYPE_INFO);
   } else {
   WriteConsoleMsg(UserIndex, "La IP """ + bannedIP + """ NO se encuentra en la lista de bans.", FontTypeNames.FONTTYPE_INFO);
  }
}

	/* '' */
	/* ' Handles the "CreateItem" message. */
	/* ' */
	/* ' @param userIndex The index of the user sending the message. */

	static void HandleCreateItem(int UserIndex) {
		/* '*************************************************** */
		/* 'Author: Nicolas Matias Gonzalez (NIGO) */
		/* 'Last Modification: 12/30/06 */
		/* ' */
		/* '*************************************************** */
		if (Declaraciones.UserList[UserIndex].incomingData.length < 3) {
			throw new RuntimeException("Err . Raise UserList ( UserIndex ) . incomingData . NotEnoughDataErrCode");
			return;
		}

		/* 'Remove packet ID */
		Declaraciones.UserList[UserIndex].incomingData.ReadByte();

		int tObj;
		String tStr;
		tObj = Declaraciones.UserList[UserIndex].incomingData.ReadInteger();

		if (Declaraciones.UserList[UserIndex].flags.Privilegios
				&& (PlayerType.User || PlayerType.Consejero || PlayerType.SemiDios)) {
			return;
		}

		int mapa;
		int X;
		int Y;

		mapa = Declaraciones.UserList[UserIndex].Pos.Map;
		X = Declaraciones.UserList[UserIndex].Pos.X;
		Y = Declaraciones.UserList[UserIndex].Pos.Y;

		General.LogGM(Declaraciones.UserList[UserIndex].Name,
				"/CI: " + tObj + " en mapa " + mapa + " (" + X + "," + Y + ")");

		if (Declaraciones.MapData[mapa][X][Y - 1].ObjInfo.ObjIndex > 0) {
			return;
		}

		if (Declaraciones.MapData[mapa][X][Y - 1].TileExit.Map > 0) {
			return;
		}

		if (tObj < 1 || tObj > Declaraciones.NumObjDatas) {
			return;
		}

		/* 'Is the object not null? */
		if (vb6.LenB(Declaraciones.ObjData[tObj].Name) == 0) {
			return;
		}

		Declaraciones.Obj Objeto;
		WriteConsoleMsg(UserIndex, "¡¡ATENCIÓN: FUERON CREADOS ***100*** ÍTEMS, TIRE Y /DEST LOS QUE NO NECESITE!!",
				FontTypeNames.FONTTYPE_GUILD);

		Objeto.Amount = 100;
		Objeto.ObjIndex = tObj;
		InvUsuario.MakeObj(Objeto, mapa, X, Y - 1);

		if (Declaraciones.ObjData[tObj].Log == 1) {
			General.LogDesarrollo(Declaraciones.UserList[UserIndex].Name + " /CI: [" + tObj + "]"
					+ Declaraciones.ObjData[tObj].Name + " en mapa " + mapa + " (" + X + "," + Y + ")");
		}

	}

	/* '' */
	/* ' Handles the "DestroyItems" message. */
	/* ' */
	/* ' @param userIndex The index of the user sending the message. */

	static void HandleDestroyItems(int UserIndex) {
		/* '*************************************************** */
		/* 'Author: Nicolas Matias Gonzalez (NIGO) */
		/* 'Last Modification: 12/30/06 */
		/* ' */
		/* '*************************************************** */
		/* 'Remove packet ID */
		Declaraciones.UserList[UserIndex].incomingData.ReadByte();

		if (Declaraciones.UserList[UserIndex].flags.Privilegios
				&& (PlayerType.User || PlayerType.Consejero || PlayerType.SemiDios)) {
			return;
		}

		int mapa;
		int X;
		int Y;

		mapa = Declaraciones.UserList[UserIndex].Pos.Map;
		X = Declaraciones.UserList[UserIndex].Pos.X;
		Y = Declaraciones.UserList[UserIndex].Pos.Y;

		int ObjIndex;
		ObjIndex = Declaraciones.MapData[mapa][X][Y].ObjInfo.ObjIndex;

		if (ObjIndex == 0) {
			return;
		}

		General.LogGM(Declaraciones.UserList[UserIndex].Name, "/DEST " + ObjIndex + " en mapa " + mapa + " (" + X + ","
				+ Y + "). Cantidad: " + Declaraciones.MapData[mapa][X][Y].ObjInfo.Amount);

		if (Declaraciones.ObjData[ObjIndex].OBJType == eOBJType.otTeleport
				&& Declaraciones.MapData[mapa][X][Y].TileExit.Map > 0) {

			WriteConsoleMsg(UserIndex, "No puede destruir teleports así. Utilice /DT.", FontTypeNames.FONTTYPE_INFO);
			return;
		}

		InvUsuario.EraseObj(10000, mapa, X, Y);
	}

	/* '' */
	/* ' Handles the "ChaosLegionKick" message. */
	/* ' */
	/* ' @param userIndex The index of the user sending the message. */

	static void HandleChaosLegionKick(int UserIndex) {
		/* '*************************************************** */
		/* 'Author: Nicolas Matias Gonzalez (NIGO) */
		/* 'Last Modification: 12/30/06 */
		/* ' */
		/* '*************************************************** */
		if (Declaraciones.UserList[UserIndex].incomingData.length < 3) {
			throw new RuntimeException("Err . Raise UserList ( UserIndex ) . incomingData . NotEnoughDataErrCode");
			return;
		}

		/* FIXME: ON ERROR GOTO ErrHandler */
		/*
		 * 'This packet contains strings, make a copy of the data to prevent
		 * losses if it's not complete yet...
		 */
		clsByteQueue buffer;
		buffer = new clsByteQueue();
		buffer.CopyBuffer(Declaraciones.UserList[UserIndex].incomingData);

		/* 'Remove packet ID */
		buffer.ReadByte();

		String UserName;
		String Reason;
		int tUser;
		int cantPenas;

		UserName = buffer.ReadASCIIString();
		Reason = buffer.ReadASCIIString();

		if ((!Declaraciones.UserList[UserIndex].flags.Privilegios && PlayerType.RoleMaster) != 0
				&& (Declaraciones.UserList[UserIndex].flags.Privilegios && (PlayerType.Admin || PlayerType.Dios)) != 0
				|| Declaraciones.UserList[UserIndex].flags.PrivEspecial) {

			if ((vb6.InStrB(UserName, "\\") != 0)) {
				UserName = vb6.Replace(UserName, "\\", "");
			}
			if ((vb6.InStrB(UserName, "/") != 0)) {
				UserName = vb6.Replace(UserName, "/", "");
			}
			tUser = Extra.NameIndex(UserName);

			General.LogGM(Declaraciones.UserList[UserIndex].Name, "ECHO DEL CAOS A: " + UserName);

			if (tUser > 0) {
				ModFacciones.ExpulsarFaccionCaos(tUser, true);
				Declaraciones.UserList[tUser].Faccion.Reenlistadas = 200;
				WriteConsoleMsg(UserIndex, UserName + " expulsado de las fuerzas del caos y prohibida la reenlistada.",
						FontTypeNames.FONTTYPE_INFO);
				WriteConsoleMsg(tUser,
						Declaraciones.UserList[UserIndex].Name
								+ " te ha expulsado en forma definitiva de las fuerzas del caos.",
						FontTypeNames.FONTTYPE_FIGHT);
				FlushBuffer(tUser);

				cantPenas = vb6.val(ES.GetVar(Declaraciones.CharPath + UserName + ".chr", "PENAS", "Cant"));
				ES.WriteVar(Declaraciones.CharPath + UserName + ".chr", "PENAS", "Cant", cantPenas + 1);
				ES.WriteVar(Declaraciones.CharPath + UserName + ".chr", "PENAS", "P" + cantPenas + 1,
						vb6.LCase(Declaraciones.UserList[UserIndex].Name) + ": EXPULSADO de la Legión Oscura por: "
								+ vb6.LCase(Reason) + " " + Date + " " + vb6.time());
			} else {
				if (General.FileExist(Declaraciones.CharPath + UserName + ".chr")) {
					ES.WriteVar(Declaraciones.CharPath + UserName + ".chr", "FACCIONES", "EjercitoCaos", 0);
					ES.WriteVar(Declaraciones.CharPath + UserName + ".chr", "FACCIONES", "Reenlistadas", 200);
					ES.WriteVar(Declaraciones.CharPath + UserName + ".chr", "FACCIONES", "Extra",
							"Expulsado por " + Declaraciones.UserList[UserIndex].Name);

					cantPenas = vb6.val(ES.GetVar(Declaraciones.CharPath + UserName + ".chr", "PENAS", "Cant"));
					ES.WriteVar(Declaraciones.CharPath + UserName + ".chr", "PENAS", "Cant", cantPenas + 1);
					ES.WriteVar(Declaraciones.CharPath + UserName + ".chr", "PENAS", "P" + cantPenas + 1,
							vb6.LCase(Declaraciones.UserList[UserIndex].Name) + ": EXPULSADO de la Legión Oscura por: "
									+ vb6.LCase(Reason) + " " + Date + " " + vb6.time());

					WriteConsoleMsg(UserIndex,
							UserName + " expulsado de las fuerzas del caos y prohibida la reenlistada.",
							FontTypeNames.FONTTYPE_INFO);
				} else {
					WriteConsoleMsg(UserIndex, UserName + ".chr inexistente.", FontTypeNames.FONTTYPE_INFO);
				}
			}
		}

		/*
		 * 'If we got here then packet is complete, copy data back to original
		 * queue
		 */
		Declaraciones.UserList[UserIndex].incomingData.CopyBuffer(buffer);

		/* FIXME: ErrHandler : */
		int ERROR;
		/* ERROR = Err . Number */
		/* FIXME: ON ERROR GOTO 0 */

		/* 'Destroy auxiliar buffer */
		buffer = null;

		if (ERROR != 0) {
			throw new RuntimeException("Err . Raise ERROR");
		}
	}

	/* '' */
	/* ' Handles the "RoyalArmyKick" message. */
	/* ' */
	/* ' @param userIndex The index of the user sending the message. */

	static void HandleRoyalArmyKick(int UserIndex) {
		/* '*************************************************** */
		/* 'Author: Nicolas Matias Gonzalez (NIGO) */
		/* 'Last Modification: 12/30/06 */
		/* ' */
		/* '*************************************************** */
		if (Declaraciones.UserList[UserIndex].incomingData.length < 3) {
			throw new RuntimeException("Err . Raise UserList ( UserIndex ) . incomingData . NotEnoughDataErrCode");
			return;
		}

		/* FIXME: ON ERROR GOTO ErrHandler */
		/*
		 * 'This packet contains strings, make a copy of the data to prevent
		 * losses if it's not complete yet...
		 */
		clsByteQueue buffer;
		buffer = new clsByteQueue();
		buffer.CopyBuffer(Declaraciones.UserList[UserIndex].incomingData);

		/* 'Remove packet ID */
		buffer.ReadByte();

		String UserName;
		String Reason;
		int tUser;
		int cantPenas;

		UserName = buffer.ReadASCIIString();
		Reason = buffer.ReadASCIIString();

		if ((!Declaraciones.UserList[UserIndex].flags.Privilegios && PlayerType.RoleMaster) != 0
				&& (Declaraciones.UserList[UserIndex].flags.Privilegios && (PlayerType.Admin || PlayerType.Dios)) != 0
				|| Declaraciones.UserList[UserIndex].flags.PrivEspecial) {

			if ((vb6.InStrB(UserName, "\\") != 0)) {
				UserName = vb6.Replace(UserName, "\\", "");
			}
			if ((vb6.InStrB(UserName, "/") != 0)) {
				UserName = vb6.Replace(UserName, "/", "");
			}
			tUser = Extra.NameIndex(UserName);

			General.LogGM(Declaraciones.UserList[UserIndex].Name, "ECHÓ DE LA REAL A: " + UserName);

			if (tUser > 0) {
				ModFacciones.ExpulsarFaccionReal(tUser, true);
				Declaraciones.UserList[tUser].Faccion.Reenlistadas = 200;
				WriteConsoleMsg(UserIndex, UserName + " expulsado de las fuerzas reales y prohibida la reenlistada.",
						FontTypeNames.FONTTYPE_INFO);
				WriteConsoleMsg(tUser,
						Declaraciones.UserList[UserIndex].Name
								+ " te ha expulsado en forma definitiva de las fuerzas reales.",
						FontTypeNames.FONTTYPE_FIGHT);
				FlushBuffer(tUser);

				cantPenas = vb6.val(ES.GetVar(Declaraciones.CharPath + UserName + ".chr", "PENAS", "Cant"));
				ES.WriteVar(Declaraciones.CharPath + UserName + ".chr", "PENAS", "Cant", cantPenas + 1);
				ES.WriteVar(Declaraciones.CharPath + UserName + ".chr", "PENAS", "P" + cantPenas + 1,
						vb6.LCase(Declaraciones.UserList[UserIndex].Name) + ": EXPULSADO del Ejército Real por: "
								+ vb6.LCase(Reason) + " " + Date + " " + vb6.time());
			} else {
				if (General.FileExist(Declaraciones.CharPath + UserName + ".chr")) {
					ES.WriteVar(Declaraciones.CharPath + UserName + ".chr", "FACCIONES", "EjercitoReal", 0);
					ES.WriteVar(Declaraciones.CharPath + UserName + ".chr", "FACCIONES", "Reenlistadas", 200);
					ES.WriteVar(Declaraciones.CharPath + UserName + ".chr", "FACCIONES", "Extra",
							"Expulsado por " + Declaraciones.UserList[UserIndex].Name);
					WriteConsoleMsg(UserIndex,
							UserName + " expulsado de las fuerzas reales y prohibida la reenlistada.",
							FontTypeNames.FONTTYPE_INFO);

					cantPenas = vb6.val(ES.GetVar(Declaraciones.CharPath + UserName + ".chr", "PENAS", "Cant"));
					ES.WriteVar(Declaraciones.CharPath + UserName + ".chr", "PENAS", "Cant", cantPenas + 1);
					ES.WriteVar(Declaraciones.CharPath + UserName + ".chr", "PENAS", "P" + cantPenas + 1,
							vb6.LCase(Declaraciones.UserList[UserIndex].Name) + ": EXPULSADO del Ejército Real por: "
									+ vb6.LCase(Reason) + " " + Date + " " + vb6.time());
				} else {
					WriteConsoleMsg(UserIndex, UserName + ".chr inexistente.", FontTypeNames.FONTTYPE_INFO);
				}
			}
		}

		/*
		 * 'If we got here then packet is complete, copy data back to original
		 * queue
		 */
		Declaraciones.UserList[UserIndex].incomingData.CopyBuffer(buffer);

		/* FIXME: ErrHandler : */
		int ERROR;
		/* ERROR = Err . Number */
		/* FIXME: ON ERROR GOTO 0 */

		/* 'Destroy auxiliar buffer */
		buffer = null;

		if (ERROR != 0) {
			throw new RuntimeException("Err . Raise ERROR");
		}
	}

	/* '' */
	/* ' Handles the "ForceMIDIAll" message. */
	/* ' */
	/* ' @param userIndex The index of the user sending the message. */

	static void HandleForceMIDIAll(int UserIndex) {
		/* '*************************************************** */
		/* 'Author: Nicolas Matias Gonzalez (NIGO) */
		/* 'Last Modification: 12/30/06 */
		/* ' */
		/* '*************************************************** */
		if (Declaraciones.UserList[UserIndex].incomingData.length < 2) {
			throw new RuntimeException("Err . Raise UserList ( UserIndex ) . incomingData . NotEnoughDataErrCode");
			return;
		}

		/* 'Remove packet ID */
		Declaraciones.UserList[UserIndex].incomingData.ReadByte();

		int midiID;
		midiID = Declaraciones.UserList[UserIndex].incomingData.ReadByte();

		if (Declaraciones.UserList[UserIndex].flags.Privilegios
				&& (PlayerType.User || PlayerType.Consejero || PlayerType.SemiDios)) {
			return;
		}

		modSendData.SendData(SendTarget.ToAll, 0,
				PrepareMessageConsoleMsg(Declaraciones.UserList[UserIndex].Name + " broadcast música: " + midiID,
						FontTypeNames.FONTTYPE_SERVER));

		modSendData.SendData(SendTarget.ToAll, 0, PrepareMessagePlayMidi(midiID));
	}

	/* '' */
	/* ' Handles the "ForceWAVEAll" message. */
	/* ' */
	/* ' @param userIndex The index of the user sending the message. */

	static void HandleForceWAVEAll(int UserIndex) {
		/* '*************************************************** */
		/* 'Author: Nicolas Matias Gonzalez (NIGO) */
		/* 'Last Modification: 12/30/06 */
		/* ' */
		/* '*************************************************** */
		if (Declaraciones.UserList[UserIndex].incomingData.length < 2) {
			throw new RuntimeException("Err . Raise UserList ( UserIndex ) . incomingData . NotEnoughDataErrCode");
			return;
		}

		/* 'Remove packet ID */
		Declaraciones.UserList[UserIndex].incomingData.ReadByte();

		int waveID;
		waveID = Declaraciones.UserList[UserIndex].incomingData.ReadByte();

		if (Declaraciones.UserList[UserIndex].flags.Privilegios
				&& (PlayerType.User || PlayerType.Consejero || PlayerType.SemiDios)) {
			return;
		}

		modSendData.SendData(SendTarget.ToAll, 0,
				PrepareMessagePlayWave(waveID, Declaraciones.NO_3D_SOUND, Declaraciones.NO_3D_SOUND));
	}

	/* '' */
	/* ' Handles the "RemovePunishment" message. */
	/* ' */
	/* ' @param userIndex The index of the user sending the message. */

	static void HandleRemovePunishment(int UserIndex) {
		/* '*************************************************** */
		/* 'Author: Nicolas Matias Gonzalez (NIGO) */
		/* 'Last Modification: 1/05/07 */
		/* 'Pablo (ToxicWaste): 1/05/07, You can now edit the punishment. */
		/* '*************************************************** */
		if (Declaraciones.UserList[UserIndex].incomingData.length < 6) {
			throw new RuntimeException("Err . Raise UserList ( UserIndex ) . incomingData . NotEnoughDataErrCode");
			return;
		}

		/* FIXME: ON ERROR GOTO ErrHandler */
		/*
		 * 'This packet contains strings, make a copy of the data to prevent
		 * losses if it's not complete yet...
		 */
		clsByteQueue buffer;
		buffer = new clsByteQueue();
		buffer.CopyBuffer(Declaraciones.UserList[UserIndex].incomingData);

		/* 'Remove packet ID */
		buffer.ReadByte();

		String UserName;
		int punishment;
		String NewText;

		UserName = buffer.ReadASCIIString();
		punishment = buffer.ReadByte();
		NewText = buffer.ReadASCIIString();

		if ((!Declaraciones.UserList[UserIndex].flags.Privilegios && PlayerType.RoleMaster) != 0
				&& (Declaraciones.UserList[UserIndex].flags.Privilegios && (PlayerType.Admin || PlayerType.Dios))) {
			if (vb6.LenB(UserName) == 0) {
				WriteConsoleMsg(UserIndex, "Utilice /borrarpena Nick@NumeroDePena@NuevaPena",
						FontTypeNames.FONTTYPE_INFO);
			} else {
				if ((vb6.InStrB(UserName, "\\") != 0)) {
					UserName = vb6.Replace(UserName, "\\", "");
				}
				if ((vb6.InStrB(UserName, "/") != 0)) {
					UserName = vb6.Replace(UserName, "/", "");
				}

				if (General.FileExist(Declaraciones.CharPath + UserName + ".chr", 0)) {
					General.LogGM(Declaraciones.UserList[UserIndex].Name,
							" borro la pena: " + punishment + "-"
									+ ES.GetVar(Declaraciones.CharPath + UserName + ".chr", "PENAS", "P" + punishment)
									+ " de " + UserName + " y la cambió por: " + NewText);

					ES.WriteVar(Declaraciones.CharPath + UserName + ".chr", "PENAS", "P" + punishment,
							vb6.LCase(Declaraciones.UserList[UserIndex].Name) + ": <" + NewText + "> " + Date + " "
									+ vb6.time());

					WriteConsoleMsg(UserIndex, "Pena modificada.", FontTypeNames.FONTTYPE_INFO);
				}
			}
		}

		/*
		 * 'If we got here then packet is complete, copy data back to original
		 * queue
		 */
		Declaraciones.UserList[UserIndex].incomingData.CopyBuffer(buffer);

		/* FIXME: ErrHandler : */
		int ERROR;
		/* ERROR = Err . Number */
		/* FIXME: ON ERROR GOTO 0 */

		/* 'Destroy auxiliar buffer */
		buffer = null;

		if (ERROR != 0) {
			throw new RuntimeException("Err . Raise ERROR");
		}
	}

	/* '' */
	/* ' Handles the "TileBlockedToggle" message. */
	/* ' */
	/* ' @param userIndex The index of the user sending the message. */

	static void HandleTileBlockedToggle(int UserIndex) {
		/* '*************************************************** */
		/* 'Author: Nicolas Matias Gonzalez (NIGO) */
		/* 'Last Modification: 12/30/06 */
		/* ' */
		/* '*************************************************** */
		/* 'Remove packet ID */
		Declaraciones.UserList[UserIndex].incomingData.ReadByte();

		if (Declaraciones.UserList[UserIndex].flags.Privilegios
				&& (PlayerType.User || PlayerType.Consejero || PlayerType.SemiDios)) {
			return;
		}

		General.LogGM(Declaraciones.UserList[UserIndex].Name, "/BLOQ");

		if (Declaraciones.MapData[Declaraciones.UserList[UserIndex].Pos.Map][Declaraciones.UserList[UserIndex].Pos.X][Declaraciones.UserList[UserIndex].Pos.Y].Blocked == 0) {
			Declaraciones.MapData[Declaraciones.UserList[UserIndex].Pos.Map][Declaraciones.UserList[UserIndex].Pos.X][Declaraciones.UserList[UserIndex].Pos.Y].Blocked = 1;
		} else {
			Declaraciones.MapData[Declaraciones.UserList[UserIndex].Pos.Map][Declaraciones.UserList[UserIndex].Pos.X][Declaraciones.UserList[UserIndex].Pos.Y].Blocked = 0;
		}

		General.Bloquear(true, Declaraciones.UserList[UserIndex].Pos.Map, Declaraciones.UserList[UserIndex].Pos.X,
				Declaraciones.UserList[UserIndex].Pos.Y,
				Declaraciones.MapData[Declaraciones.UserList[UserIndex].Pos.Map][Declaraciones.UserList[UserIndex].Pos.X][Declaraciones.UserList[UserIndex].Pos.Y].Blocked);
	}

	/* '' */
	/* ' Handles the "KillNPCNoRespawn" message. */
	/* ' */
	/* ' @param userIndex The index of the user sending the message. */

	static void HandleKillNPCNoRespawn(int UserIndex) {
		/* '*************************************************** */
		/* 'Author: Nicolas Matias Gonzalez (NIGO) */
		/* 'Last Modification: 12/30/06 */
		/* ' */
		/* '*************************************************** */
		/* 'Remove packet ID */
		Declaraciones.UserList[UserIndex].incomingData.ReadByte();

		if (Declaraciones.UserList[UserIndex].flags.Privilegios
				&& (PlayerType.User || PlayerType.Consejero || PlayerType.SemiDios)) {
			return;
		}

		if (Declaraciones.UserList[UserIndex].flags.TargetNPC == 0) {
			return;
		}

		NPCs.QuitarNPC(Declaraciones.UserList[UserIndex].flags.TargetNPC);
		General.LogGM(Declaraciones.UserList[UserIndex].Name,
				"/MATA " + Declaraciones.Npclist[Declaraciones.UserList[UserIndex].flags.TargetNPC].Name);
	}

	/* '' */
	/* ' Handles the "KillAllNearbyNPCs" message. */
	/* ' */
	/* ' @param userIndex The index of the user sending the message. */

	static void HandleKillAllNearbyNPCs(int UserIndex) {
		/* '*************************************************** */
		/* 'Author: Nicolas Matias Gonzalez (NIGO) */
		/* 'Last Modification: 12/30/06 */
		/* ' */
		/* '*************************************************** */
		/* 'Remove packet ID */
		Declaraciones.UserList[UserIndex].incomingData.ReadByte();

		if (Declaraciones.UserList[UserIndex].flags.Privilegios
				&& (PlayerType.User || PlayerType.Consejero || PlayerType.SemiDios)) {
			return;
		}

		int X;
		int Y;

		for (Y = (Declaraciones.UserList[UserIndex].Pos.Y - Declaraciones.MinYBorder
				+ 1); Y <= (Declaraciones.UserList[UserIndex].Pos.Y + Declaraciones.MinYBorder - 1); Y++) {
			for (X = (Declaraciones.UserList[UserIndex].Pos.X - Declaraciones.MinXBorder
					+ 1); X <= (Declaraciones.UserList[UserIndex].Pos.X + Declaraciones.MinXBorder - 1); X++) {
				if (X > 0 && Y > 0 && X < 101 && Y < 101) {
					if (Declaraciones.MapData[Declaraciones.UserList[UserIndex].Pos.Map][X][Y].NpcIndex > 0) {
						NPCs.QuitarNPC(Declaraciones.MapData[Declaraciones.UserList[UserIndex].Pos.Map][X][Y].NpcIndex);
					}
				}
			}
		}
		General.LogGM(Declaraciones.UserList[UserIndex].Name, "/MASSKILL");
	}

	/* '' */
	/* ' Handles the "LastIP" message. */
	/* ' */
	/* ' @param userIndex The index of the user sending the message. */

	static void HandleLastIP(int UserIndex) {
 /* '*************************************************** */
 /* 'Author: Nicolas Matias Gonzalez (NIGO) */
 /* 'Last Modification: 12/30/06 */
 /* ' */
 /* '*************************************************** */
  if (Declaraciones.UserList[UserIndex].incomingData.length<3) {
  throw new RuntimeException("Err . Raise UserList ( UserIndex ) . incomingData . NotEnoughDataErrCode");
  return;
 }
 
 /* FIXME: ON ERROR GOTO ErrHandler */
  /* 'This packet contains strings, make a copy of the data to prevent losses if it's not complete yet... */
  clsByteQueue buffer;
  buffer = new clsByteQueue();
  buffer.CopyBuffer(Declaraciones.UserList[UserIndex].incomingData);
  
  /* 'Remove packet ID */
  buffer.ReadByte();
  
  String UserName;
  String lista;
  int LoopC;
  int priv;
  boolean validCheck;
  
  priv = PlayerType.Admin || PlayerType.Dios || PlayerType.SemiDios || PlayerType.Consejero;
  UserName = buffer.ReadASCIIString();
  
   if ((!Declaraciones.UserList[UserIndex].flags.Privilegios && PlayerType.RoleMaster) != 0 && (Declaraciones.UserList[UserIndex].flags.Privilegios && (PlayerType.Admin || PlayerType.Dios || PlayerType.SemiDios)) != 0) {
   /* 'Handle special chars */
    if ((vb6.InStrB(UserName, "\\") != 0)) {
    UserName = vb6.Replace(UserName, "\\", "");
   }
    if ((vb6.InStrB(UserName, "\\") != 0)) {
    UserName = vb6.Replace(UserName, "/", "");
   }
    if ((vb6.InStrB(UserName, "+") != 0)) {
    UserName = vb6.Replace(UserName, "+", " ");
   }
   
   /* 'Only Gods and Admins can see the ips of adminsitrative characters. All others can be seen by every adminsitrative char. */
    if (Extra.NameIndex(UserName)>0) {
    validCheck = (Declaraciones.UserList[Extra.NameIndex(UserName)].flags.Privilegios && priv) == 0 || (Declaraciones.UserList[UserIndex].flags.Privilegios && (PlayerType.Admin || PlayerType.Dios)) != 0;
    } else {
    validCheck = (Admin.UserDarPrivilegioLevel(UserName) && priv) == 0 || (Declaraciones.UserList[UserIndex].flags.Privilegios && (PlayerType.Admin || PlayerType.Dios)) != 0;
   }
   
    if (validCheck) {
    General.LogGM(Declaraciones.UserList[UserIndex].Name, "/LASTIP " + UserName);
    
     if (General.FileExist(Declaraciones.CharPath + UserName + ".chr", 0)) {
     lista = "Las ultimas IPs con las que " + UserName + " se conectó son:";
      for (LoopC = (1); LoopC <= (5); LoopC++) {
      lista = lista + vbCrLf + LoopC + " - " + ES.GetVar(Declaraciones.CharPath + UserName + ".chr", "INIT", "LastIP" + LoopC);
     }
     WriteConsoleMsg(UserIndex, lista, FontTypeNames.FONTTYPE_INFO);
     } else {
     WriteConsoleMsg(UserIndex, "Charfile """ + UserName + """ inexistente.", FontTypeNames.FONTTYPE_INFO);
    }
    } else {
    WriteConsoleMsg(UserIndex, UserName + " es de mayor jerarquía que vos.", FontTypeNames.FONTTYPE_INFO);
   }
  }
  
  /* 'If we got here then packet is complete, copy data back to original queue */
  Declaraciones.UserList[UserIndex].incomingData.CopyBuffer(buffer);
 
 /* FIXME: ErrHandler : */
 int ERROR;
 /* ERROR = Err . Number */
 /* FIXME: ON ERROR GOTO 0 */
 
 /* 'Destroy auxiliar buffer */
 buffer = null;
 
 if (ERROR != 0) {
 throw new RuntimeException("Err . Raise ERROR");
 }
}

	/* '' */
	/* ' Handles the "ChatColor" message. */
	/* ' */
	/* ' @param userIndex The index of the user sending the message. */

	static void HandleChatColor(int UserIndex) {
		/* '*************************************************** */
		/* 'Author: Lucas Tavolaro Ortiz (Tavo) */
		/* 'Last Modification: 12/23/06 */
		/* 'Last modified by: Juan Martín Sotuyo Dodero (Maraxus) */
		/* 'Change the user`s chat color */
		/* '*************************************************** */
		if (Declaraciones.UserList[UserIndex].incomingData.length < 4) {
			throw new RuntimeException("Err . Raise UserList ( UserIndex ) . incomingData . NotEnoughDataErrCode");
			return;
		}

		/* 'Remove packet ID */
		Declaraciones.UserList[UserIndex].incomingData.ReadByte();

		int color;

		color = vb6.RGB(Declaraciones.UserList[UserIndex].incomingData.ReadByte(),
				Declaraciones.UserList[UserIndex].incomingData.ReadByte(),
				Declaraciones.UserList[UserIndex].incomingData.ReadByte());

		if ((Declaraciones.UserList[UserIndex].flags.Privilegios
				&& (PlayerType.Admin || PlayerType.Dios || PlayerType.RoleMaster))) {
			Declaraciones.UserList[UserIndex].flags.ChatColor = color;
		}
	}

	/* '' */
	/* ' Handles the "Ignored" message. */
	/* ' */
	/* ' @param userIndex The index of the user sending the message. */

	static void HandleIgnored(int UserIndex) {
		/* '*************************************************** */
		/* 'Author: Lucas Tavolaro Ortiz (Tavo) */
		/* 'Last Modification: 12/23/06 */
		/* 'Ignore the user */
		/* '*************************************************** */
		/* 'Remove packet ID */
		Declaraciones.UserList[UserIndex].incomingData.ReadByte();

		if (Declaraciones.UserList[UserIndex].flags.Privilegios
				&& (PlayerType.Admin || PlayerType.Dios || PlayerType.SemiDios || PlayerType.Consejero)) {
			Declaraciones.UserList[UserIndex].flags.AdminPerseguible = !Declaraciones.UserList[UserIndex].flags.AdminPerseguible;
		}
	}

	/* '' */
	/* ' Handles the "CheckSlot" message. */
	/* ' */
	/* ' @param userIndex The index of the user sending the message. */

	static void HandleCheckSlot(int UserIndex) {
		/* '*************************************************** */
		/* 'Author: Pablo (ToxicWaste) */
		/* 'Last Modification: 07/06/2010 */
		/* 'Check one Users Slot in Particular from Inventory */
		/*
		 * '07/06/2010: ZaMa - Ahora no se puede usar para saber si hay
		 * dioses/admins online.
		 */
		/* '*************************************************** */
		if (Declaraciones.UserList[UserIndex].incomingData.length < 4) {
			throw new RuntimeException("Err . Raise UserList ( UserIndex ) . incomingData . NotEnoughDataErrCode");
			return;
		}

		/* FIXME: ON ERROR GOTO ErrHandler */
		/*
		 * 'This packet contains strings, make a copy of the data to prevent
		 * losses if it's not complete yet...
		 */
		clsByteQueue buffer;
		buffer = new clsByteQueue();
		buffer.CopyBuffer(Declaraciones.UserList[UserIndex].incomingData);

		/* 'Remove packet ID */
		buffer.ReadByte();

		/* 'Reads the UserName and Slot Packets */
		String UserName;
		int Slot;
		int tIndex;

		boolean UserIsAdmin;
		boolean OtherUserIsAdmin;

		/* 'Que UserName? */
		UserName = buffer.ReadASCIIString();
		/* 'Que Slot? */
		Slot = buffer.ReadByte();

		UserIsAdmin = (Declaraciones.UserList[UserIndex].flags.Privilegios
				&& (PlayerType.Admin || PlayerType.Dios)) != 0;

		if ((Declaraciones.UserList[UserIndex].flags.Privilegios && PlayerType.SemiDios) != 0 || UserIsAdmin) {

			General.LogGM(Declaraciones.UserList[UserIndex].Name,
					Declaraciones.UserList[UserIndex].Name + " Checkeó el slot " + Slot + " de " + UserName);

			/* 'Que user index? */
			tIndex = Extra.NameIndex(UserName);
			OtherUserIsAdmin = ES.EsDios(UserName) || ES.EsAdmin(UserName);

			if (tIndex > 0) {
				if (UserIsAdmin || !OtherUserIsAdmin) {
					if (Slot > 0 && Slot <= Declaraciones.UserList[tIndex].CurrentInventorySlots) {
						if (Declaraciones.UserList[tIndex].Invent.Object[Slot].ObjIndex > 0) {
							WriteConsoleMsg(UserIndex,
									" Objeto " + Slot + ") "
											+ Declaraciones.ObjData[Declaraciones.UserList[tIndex].Invent.Object[Slot].ObjIndex].Name
											+ " Cantidad:" + Declaraciones.UserList[tIndex].Invent.Object[Slot].Amount,
									FontTypeNames.FONTTYPE_INFO);
						} else {
							WriteConsoleMsg(UserIndex, "No hay ningún objeto en slot seleccionado.",
									FontTypeNames.FONTTYPE_INFO);
						}
					} else {
						WriteConsoleMsg(UserIndex, "Slot Inválido.", FontTypeNames.FONTTYPE_TALK);
					}
				} else {
					WriteConsoleMsg(UserIndex, "No puedes ver slots de un dios o admin.", FontTypeNames.FONTTYPE_INFO);
				}
			} else {
				if (UserIsAdmin || !OtherUserIsAdmin) {
					WriteConsoleMsg(UserIndex, "Usuario offline.", FontTypeNames.FONTTYPE_TALK);
				} else {
					WriteConsoleMsg(UserIndex, "No puedes ver slots de un dios o admin.", FontTypeNames.FONTTYPE_INFO);
				}
			}
		}

		/*
		 * 'If we got here then packet is complete, copy data back to original
		 * queue
		 */
		Declaraciones.UserList[UserIndex].incomingData.CopyBuffer(buffer);

		/* FIXME: ErrHandler : */
		int ERROR;
		/* ERROR = Err . Number */
		/* FIXME: ON ERROR GOTO 0 */

		/* 'Destroy auxiliar buffer */
		buffer = null;

		if (ERROR != 0) {
			throw new RuntimeException("Err . Raise ERROR");
		}
	}

	/* '' */
	/* ' Handles the "ResetAutoUpdate" message. */
	/* ' */
	/* ' @param userIndex The index of the user sending the message. */

	static void HandleResetAutoUpdate(int UserIndex) {
		/* '*************************************************** */
		/* 'Author: Lucas Tavolaro Ortiz (Tavo) */
		/* 'Last Modification: 12/23/06 */
		/* 'Reset the AutoUpdate */
		/* '*************************************************** */
		/* 'Remove packet ID */
		Declaraciones.UserList[UserIndex].incomingData.ReadByte();

		if (Declaraciones.UserList[UserIndex].flags.Privilegios
				&& (PlayerType.User || PlayerType.Consejero || PlayerType.SemiDios)) {
			return;
		}
		if (vb6.UCase(Declaraciones.UserList[UserIndex].Name) != "MARAXUS") {
			return;
		}

		WriteConsoleMsg(UserIndex, "TID: " + vb6.CStr(General.ReiniciarAutoUpdate()), FontTypeNames.FONTTYPE_INFO);
	}

	/* '' */
	/* ' Handles the "Restart" message. */
	/* ' */
	/* ' @param userIndex The index of the user sending the message. */

	static void HandleRestart(int UserIndex) {
		/* '*************************************************** */
		/* 'Author: Lucas Tavolaro Ortiz (Tavo) */
		/* 'Last Modification: 12/23/06 */
		/* 'Restart the game */
		/* '*************************************************** */
		/* 'Remove Packet ID */
		Declaraciones.UserList[UserIndex].incomingData.ReadByte();

		if (Declaraciones.UserList[UserIndex].flags.Privilegios
				&& (PlayerType.User || PlayerType.Consejero || PlayerType.SemiDios)) {
			return;
		}
		if (vb6.UCase(Declaraciones.UserList[UserIndex].Name) != "MARAXUS") {
			return;
		}

		/* 'time and Time BUG! */
		General.LogGM(Declaraciones.UserList[UserIndex].Name,
				Declaraciones.UserList[UserIndex].Name + " reinició el mundo.");

		General.ReiniciarServidor(true);
	}

	/* '' */
	/* ' Handles the "ReloadObjects" message. */
	/* ' */
	/* ' @param userIndex The index of the user sending the message. */

	static void HandleReloadObjects(int UserIndex) {
		/* '*************************************************** */
		/* 'Author: Lucas Tavolaro Ortiz (Tavo) */
		/* 'Last Modification: 12/23/06 */
		/* 'Reload the objects */
		/* '*************************************************** */
		/* 'Remove Packet ID */
		Declaraciones.UserList[UserIndex].incomingData.ReadByte();

		if (Declaraciones.UserList[UserIndex].flags.Privilegios
				&& (PlayerType.User || PlayerType.Consejero || PlayerType.SemiDios || PlayerType.RoleMaster)) {
			return;
		}

		General.LogGM(Declaraciones.UserList[UserIndex].Name,
				Declaraciones.UserList[UserIndex].Name + " ha recargado los objetos.");

		ES.LoadOBJData();
	}

	/* '' */
	/* ' Handles the "ReloadSpells" message. */
	/* ' */
	/* ' @param userIndex The index of the user sending the message. */

	static void HandleReloadSpells(int UserIndex) {
		/* '*************************************************** */
		/* 'Author: Lucas Tavolaro Ortiz (Tavo) */
		/* 'Last Modification: 12/23/06 */
		/* 'Reload the spells */
		/* '*************************************************** */
		/* 'Remove Packet ID */
		Declaraciones.UserList[UserIndex].incomingData.ReadByte();

		if (Declaraciones.UserList[UserIndex].flags.Privilegios
				&& (PlayerType.User || PlayerType.Consejero || PlayerType.SemiDios || PlayerType.RoleMaster)) {
			return;
		}

		General.LogGM(Declaraciones.UserList[UserIndex].Name,
				Declaraciones.UserList[UserIndex].Name + " ha recargado los hechizos.");

		ES.CargarHechizos();
	}

	/* '' */
	/* ' Handle the "ReloadServerIni" message. */
	/* ' */
	/* ' @param userIndex The index of the user sending the message */

	static void HandleReloadServerIni(int UserIndex) {
		/* '*************************************************** */
		/* 'Author: Lucas Tavolaro Ortiz (Tavo) */
		/* 'Last Modification: 12/23/06 */
		/* 'Reload the Server`s INI */
		/* '*************************************************** */
		/* 'Remove Packet ID */
		Declaraciones.UserList[UserIndex].incomingData.ReadByte();

		if (Declaraciones.UserList[UserIndex].flags.Privilegios
				&& (PlayerType.User || PlayerType.Consejero || PlayerType.SemiDios || PlayerType.RoleMaster)) {
			return;
		}

		General.LogGM(Declaraciones.UserList[UserIndex].Name,
				Declaraciones.UserList[UserIndex].Name + " ha recargado los INITs.");

		ES.LoadSini();

		WriteConsoleMsg(UserIndex, "Server.ini actualizado correctamente", FontTypeNames.FONTTYPE_INFO);
	}

	/* '' */
	/* ' Handle the "ReloadNPCs" message */
	/* ' */
	/* ' @param userIndex The index of the user sending the message */

	static void HandleReloadNPCs(int UserIndex) {
		/* '*************************************************** */
		/* 'Author: Lucas Tavolaro Ortiz (Tavo) */
		/* 'Last Modification: 12/23/06 */
		/* 'Reload the Server`s NPC */
		/* '*************************************************** */
		/* 'Remove Packet ID */
		Declaraciones.UserList[UserIndex].incomingData.ReadByte();

		if (Declaraciones.UserList[UserIndex].flags.Privilegios
				&& (PlayerType.User || PlayerType.Consejero || PlayerType.SemiDios || PlayerType.RoleMaster)) {
			return;
		}

		General.LogGM(Declaraciones.UserList[UserIndex].Name,
				Declaraciones.UserList[UserIndex].Name + " ha recargado los NPCs.");

		General.CargaNpcsDat();

		WriteConsoleMsg(UserIndex, "Npcs.dat recargado.", FontTypeNames.FONTTYPE_INFO);
	}

	/* '' */
	/* ' Handle the "KickAllChars" message */
	/* ' */
	/* ' @param userIndex The index of the user sending the message */

	static void HandleKickAllChars(int UserIndex) {
		/* '*************************************************** */
		/* 'Author: Lucas Tavolaro Ortiz (Tavo) */
		/* 'Last Modification: 12/23/06 */
		/* 'Kick all the chars that are online */
		/* '*************************************************** */
		/* 'Remove Packet ID */
		Declaraciones.UserList[UserIndex].incomingData.ReadByte();

		if (Declaraciones.UserList[UserIndex].flags.Privilegios
				&& (PlayerType.User || PlayerType.Consejero || PlayerType.SemiDios || PlayerType.RoleMaster)) {
			return;
		}

		General.LogGM(Declaraciones.UserList[UserIndex].Name,
				Declaraciones.UserList[UserIndex].Name + " ha echado a todos los personajes.");

		TCP.EcharPjsNoPrivilegiados();
	}

	/* '' */
	/* ' Handle the "Night" message */
	/* ' */
	/* ' @param userIndex The index of the user sending the message */

	static void HandleNight(int UserIndex) {
		/* '*************************************************** */
		/* 'Author: Lucas Tavolaro Ortiz (Tavo) */
		/* 'Last Modification: 12/23/06 */
		/* 'Last modified by: Juan Martín Sotuyo Dodero (Maraxus) */
		/* ' */
		/* '*************************************************** */
		/* 'Remove Packet ID */
		Declaraciones.UserList[UserIndex].incomingData.ReadByte();

		if (Declaraciones.UserList[UserIndex].flags.Privilegios
				&& (PlayerType.User || PlayerType.Consejero || PlayerType.SemiDios)) {
			return;
		}
		if (vb6.UCase(Declaraciones.UserList[UserIndex].Name) != "MARAXUS") {
			return;
		}

		Admin.DeNoche = !Admin.DeNoche;

		int i;

		for (i = (1); i <= (Declaraciones.NumUsers); i++) {
			if (Declaraciones.UserList[i].flags.UserLogged && Declaraciones.UserList[i].ConnID > -1) {
				TCP.EnviarNoche(i);
			}
		}
	}

	/* '' */
	/* ' Handle the "ShowServerForm" message */
	/* ' */
	/* ' @param userIndex The index of the user sending the message */

	static void HandleShowServerForm(int UserIndex) {
 /* '*************************************************** */
 /* 'Author: Lucas Tavolaro Ortiz (Tavo) */
 /* 'Last Modification: 12/23/06 */
 /* 'Show the server form */
 /* '*************************************************** */
  /* 'Remove Packet ID */
  Declaraciones.UserList[UserIndex].incomingData.ReadByte();
  
  if (Declaraciones.UserList[UserIndex].flags.Privilegios && (PlayerType.User || PlayerType.Consejero || PlayerType.SemiDios || PlayerType.RoleMaster)) {
  return;
  }
  
  General.LogGM(Declaraciones.UserList[UserIndex].Name, Declaraciones.UserList[UserIndex].Name + " ha solicitado mostrar el formulario del servidor.");
  frmMain.mnuMostrar_Click;
}

	/* '' */
	/* ' Handle the "CleanSOS" message */
	/* ' */
	/* ' @param userIndex The index of the user sending the message */

	static void HandleCleanSOS(int UserIndex) {
		/* '*************************************************** */
		/* 'Author: Lucas Tavolaro Ortiz (Tavo) */
		/* 'Last Modification: 12/23/06 */
		/* 'Clean the SOS */
		/* '*************************************************** */
		/* 'Remove Packet ID */
		Declaraciones.UserList[UserIndex].incomingData.ReadByte();

		if (Declaraciones.UserList[UserIndex].flags.Privilegios
				&& (PlayerType.User || PlayerType.Consejero || PlayerType.SemiDios || PlayerType.RoleMaster)) {
			return;
		}

		General.LogGM(Declaraciones.UserList[UserIndex].Name,
				Declaraciones.UserList[UserIndex].Name + " ha borrado los SOS.");

		Declaraciones.Ayuda.Reset();
	}

	/* '' */
	/* ' Handle the "SaveChars" message */
	/* ' */
	/* ' @param userIndex The index of the user sending the message */

	static void HandleSaveChars(int UserIndex) {
 /* '*************************************************** */
 /* 'Author: Lucas Tavolaro Ortiz (Tavo) */
 /* 'Last Modification: 12/23/06 */
 /* 'Save the characters */
 /* '*************************************************** */
  /* 'Remove Packet ID */
  Declaraciones.UserList[UserIndex].incomingData.ReadByte();
  
  if (Declaraciones.UserList[UserIndex].flags.Privilegios && (PlayerType.User || PlayerType.Consejero || PlayerType.SemiDios || PlayerType.RoleMaster)) {
  return;
  }
  
  General.LogGM(Declaraciones.UserList[UserIndex].Name, Declaraciones.UserList[UserIndex].Name + " ha guardado todos los chars.");
  
  mdParty.ActualizaExperiencias;
  General.GuardarUsuarios();
}

	/* '' */
	/* ' Handle the "ChangeMapInfoBackup" message */
	/* ' */
	/* ' @param userIndex The index of the user sending the message */

	static void HandleChangeMapInfoBackup(int UserIndex) {
		/* '*************************************************** */
		/* 'Author: Lucas Tavolaro Ortiz (Tavo) */
		/* 'Last Modification: 12/24/06 */
		/* 'Last modified by: Juan Martín Sotuyo Dodero (Maraxus) */
		/* 'Change the backup`s info of the map */
		/* '*************************************************** */
		if (Declaraciones.UserList[UserIndex].incomingData.length < 2) {
			throw new RuntimeException("Err . Raise UserList ( UserIndex ) . incomingData . NotEnoughDataErrCode");
			return;
		}

		/* 'Remove Packet ID */
		Declaraciones.UserList[UserIndex].incomingData.ReadByte();

		boolean doTheBackUp;

		doTheBackUp = Declaraciones.UserList[UserIndex].incomingData.ReadBoolean();

		if ((Declaraciones.UserList[UserIndex].flags.Privilegios && (PlayerType.Admin || PlayerType.Dios)) == 0) {
			return;
		}

		General.LogGM(Declaraciones.UserList[UserIndex].Name,
				Declaraciones.UserList[UserIndex].Name + " ha cambiado la información sobre el BackUp.");

		/* 'Change the boolean to byte in a fast way */
		if (doTheBackUp) {
			Declaraciones.MapInfo[Declaraciones.UserList[UserIndex].Pos.Map].BackUp = 1;
		} else {
			Declaraciones.MapInfo[Declaraciones.UserList[UserIndex].Pos.Map].BackUp = 0;
		}

		/* 'Change the boolean to string in a fast way */
		ES.WriteVar(
				vb6.App.Instance().Path + Declaraciones.MapPath + "mapa" + Declaraciones.UserList[UserIndex].Pos.Map
						+ ".dat",
				"Mapa" + Declaraciones.UserList[UserIndex].Pos.Map, "backup",
				Declaraciones.MapInfo[Declaraciones.UserList[UserIndex].Pos.Map].BackUp);

		WriteConsoleMsg(UserIndex,
				"Mapa " + Declaraciones.UserList[UserIndex].Pos.Map + " Backup: "
						+ Declaraciones.MapInfo[Declaraciones.UserList[UserIndex].Pos.Map].BackUp,
				FontTypeNames.FONTTYPE_INFO);
	}

	/* '' */
	/* ' Handle the "ChangeMapInfoPK" message */
	/* ' */
	/* ' @param userIndex The index of the user sending the message */

	static void HandleChangeMapInfoPK(int UserIndex) {
		/* '*************************************************** */
		/* 'Author: Lucas Tavolaro Ortiz (Tavo) */
		/* 'Last Modification: 12/24/06 */
		/* 'Last modified by: Juan Martín Sotuyo Dodero (Maraxus) */
		/* 'Change the pk`s info of the map */
		/* '*************************************************** */
		if (Declaraciones.UserList[UserIndex].incomingData.length < 2) {
			throw new RuntimeException("Err . Raise UserList ( UserIndex ) . incomingData . NotEnoughDataErrCode");
			return;
		}

		/* 'Remove Packet ID */
		Declaraciones.UserList[UserIndex].incomingData.ReadByte();

		boolean isMapPk;

		isMapPk = Declaraciones.UserList[UserIndex].incomingData.ReadBoolean();

		if ((Declaraciones.UserList[UserIndex].flags.Privilegios && (PlayerType.Admin || PlayerType.Dios)) == 0) {
			return;
		}

		General.LogGM(Declaraciones.UserList[UserIndex].Name,
				Declaraciones.UserList[UserIndex].Name + " ha cambiado la información sobre si es PK el mapa.");

		Declaraciones.MapInfo[Declaraciones.UserList[UserIndex].Pos.Map].Pk = isMapPk;

		/* 'Change the boolean to string in a fast way */
		ES.WriteVar(vb6.App.Instance().Path + Declaraciones.MapPath + "mapa" + Declaraciones.UserList[UserIndex].Pos.Map
				+ ".dat", "Mapa" + Declaraciones.UserList[UserIndex].Pos.Map, "Pk", vb6.IIf(isMapPk, "1", "0"));

		WriteConsoleMsg(UserIndex,
				"Mapa " + Declaraciones.UserList[UserIndex].Pos.Map + " PK: "
						+ Declaraciones.MapInfo[Declaraciones.UserList[UserIndex].Pos.Map].Pk,
				FontTypeNames.FONTTYPE_INFO);
	}

	/* '' */
	/* ' Handle the "ChangeMapInfoRestricted" message */
	/* ' */
	/* ' @param userIndex The index of the user sending the message */

	static void HandleChangeMapInfoRestricted(int UserIndex) {
		/* '*************************************************** */
		/* 'Author: Pablo (ToxicWaste) */
		/* 'Last Modification: 26/01/2007 */
		/*
		 * 'Restringido -> Options: "NEWBIE", "NO", "ARMADA", "CAOS", "FACCION".
		 */
		/* '*************************************************** */
		if (Declaraciones.UserList[UserIndex].incomingData.length < 3) {
			throw new RuntimeException("Err . Raise UserList ( UserIndex ) . incomingData . NotEnoughDataErrCode");
			return;
		}

		/* FIXME: ON ERROR GOTO ErrHandler */
		String tStr;

		/*
		 * 'This packet contains strings, make a copy of the data to prevent
		 * losses if it's not complete yet...
		 */
		clsByteQueue buffer;
		buffer = new clsByteQueue();
		buffer.CopyBuffer(Declaraciones.UserList[UserIndex].incomingData);

		/* 'Remove Packet ID */
		buffer.ReadByte();

		tStr = buffer.ReadASCIIString();

		if ((Declaraciones.UserList[UserIndex].flags.Privilegios && (PlayerType.Admin || PlayerType.Dios)) != 0) {
			if (tStr == "NEWBIE" || tStr == "NO" || tStr == "ARMADA" || tStr == "CAOS" || tStr == "FACCION") {
				General.LogGM(Declaraciones.UserList[UserIndex].Name, Declaraciones.UserList[UserIndex].Name
						+ " ha cambiado la información sobre si es restringido el mapa.");

				Declaraciones.MapInfo[Declaraciones.UserList[UserIndex].Pos.Map].Restringir = Extra
						.RestrictStringToByte(tStr);

				ES.WriteVar(
						vb6.App.Instance().Path + Declaraciones.MapPath + "mapa"
								+ Declaraciones.UserList[UserIndex].Pos.Map + ".dat",
						"Mapa" + Declaraciones.UserList[UserIndex].Pos.Map, "Restringir", tStr);
				WriteConsoleMsg(UserIndex,
						"Mapa " + Declaraciones.UserList[UserIndex].Pos.Map + " Restringido: "
								+ Extra.RestrictByteToString(
										Declaraciones.MapInfo[Declaraciones.UserList[UserIndex].Pos.Map].Restringir),
						FontTypeNames.FONTTYPE_INFO);
			} else {
				WriteConsoleMsg(UserIndex, "Opciones para restringir: 'NEWBIE', 'NO', 'ARMADA', 'CAOS', 'FACCION'",
						FontTypeNames.FONTTYPE_INFO);
			}
		}

		/*
		 * 'If we got here then packet is complete, copy data back to original
		 * queue
		 */
		Declaraciones.UserList[UserIndex].incomingData.CopyBuffer(buffer);

		/* FIXME: ErrHandler : */
		int ERROR;
		/* ERROR = Err . Number */
		/* FIXME: ON ERROR GOTO 0 */

		/* 'Destroy auxiliar buffer */
		buffer = null;

		if (ERROR != 0) {
			throw new RuntimeException("Err . Raise ERROR");
		}
	}

	/* '' */
	/* ' Handle the "ChangeMapInfoNoMagic" message */
	/* ' */
	/* ' @param userIndex The index of the user sending the message */

	static void HandleChangeMapInfoNoMagic(int UserIndex) {
		/* '*************************************************** */
		/* 'Author: Pablo (ToxicWaste) */
		/* 'Last Modification: 26/01/2007 */
		/* 'MagiaSinEfecto -> Options: "1" , "0". */
		/* '*************************************************** */
		if (Declaraciones.UserList[UserIndex].incomingData.length < 2) {
			throw new RuntimeException("Err . Raise UserList ( UserIndex ) . incomingData . NotEnoughDataErrCode");
			return;
		}

		boolean nomagic;

		/* 'Remove Packet ID */
		Declaraciones.UserList[UserIndex].incomingData.ReadByte();

		nomagic = Declaraciones.UserList[UserIndex].incomingData.ReadBoolean();

		if ((Declaraciones.UserList[UserIndex].flags.Privilegios && (PlayerType.Admin || PlayerType.Dios)) != 0) {
			General.LogGM(Declaraciones.UserList[UserIndex].Name, Declaraciones.UserList[UserIndex].Name
					+ " ha cambiado la información sobre si está permitido usar la magia el mapa.");
			Declaraciones.MapInfo[Declaraciones.UserList[UserIndex].Pos.Map].MagiaSinEfecto = nomagic;
			ES.WriteVar(vb6.App.Instance().Path + Declaraciones.MapPath + "mapa"
					+ Declaraciones.UserList[UserIndex].Pos.Map + ".dat",
					"Mapa" + Declaraciones.UserList[UserIndex].Pos.Map, "MagiaSinEfecto", nomagic);
			WriteConsoleMsg(UserIndex,
					"Mapa " + Declaraciones.UserList[UserIndex].Pos.Map + " MagiaSinEfecto: "
							+ Declaraciones.MapInfo[Declaraciones.UserList[UserIndex].Pos.Map].MagiaSinEfecto,
					FontTypeNames.FONTTYPE_INFO);
		}
	}

	/* '' */
	/* ' Handle the "ChangeMapInfoNoInvi" message */
	/* ' */
	/* ' @param userIndex The index of the user sending the message */

	static void HandleChangeMapInfoNoInvi(int UserIndex) {
		/* '*************************************************** */
		/* 'Author: Pablo (ToxicWaste) */
		/* 'Last Modification: 26/01/2007 */
		/* 'InviSinEfecto -> Options: "1", "0" */
		/* '*************************************************** */
		if (Declaraciones.UserList[UserIndex].incomingData.length < 2) {
			throw new RuntimeException("Err . Raise UserList ( UserIndex ) . incomingData . NotEnoughDataErrCode");
			return;
		}

		boolean noinvi;

		/* 'Remove Packet ID */
		Declaraciones.UserList[UserIndex].incomingData.ReadByte();

		noinvi = Declaraciones.UserList[UserIndex].incomingData.ReadBoolean();

		if ((Declaraciones.UserList[UserIndex].flags.Privilegios && (PlayerType.Admin || PlayerType.Dios)) != 0) {
			General.LogGM(Declaraciones.UserList[UserIndex].Name, Declaraciones.UserList[UserIndex].Name
					+ " ha cambiado la información sobre si está permitido usar la invisibilidad en el mapa.");
			Declaraciones.MapInfo[Declaraciones.UserList[UserIndex].Pos.Map].InviSinEfecto = noinvi;
			ES.WriteVar(vb6.App.Instance().Path + Declaraciones.MapPath + "mapa"
					+ Declaraciones.UserList[UserIndex].Pos.Map + ".dat",
					"Mapa" + Declaraciones.UserList[UserIndex].Pos.Map, "InviSinEfecto", noinvi);
			WriteConsoleMsg(UserIndex,
					"Mapa " + Declaraciones.UserList[UserIndex].Pos.Map + " InviSinEfecto: "
							+ Declaraciones.MapInfo[Declaraciones.UserList[UserIndex].Pos.Map].InviSinEfecto,
					FontTypeNames.FONTTYPE_INFO);
		}
	}

	/* '' */
	/* ' Handle the "ChangeMapInfoNoResu" message */
	/* ' */
	/* ' @param userIndex The index of the user sending the message */

	static void HandleChangeMapInfoNoResu(int UserIndex) {
		/* '*************************************************** */
		/* 'Author: Pablo (ToxicWaste) */
		/* 'Last Modification: 26/01/2007 */
		/* 'ResuSinEfecto -> Options: "1", "0" */
		/* '*************************************************** */
		if (Declaraciones.UserList[UserIndex].incomingData.length < 2) {
			throw new RuntimeException("Err . Raise UserList ( UserIndex ) . incomingData . NotEnoughDataErrCode");
			return;
		}

		boolean noresu;

		/* 'Remove Packet ID */
		Declaraciones.UserList[UserIndex].incomingData.ReadByte();

		noresu = Declaraciones.UserList[UserIndex].incomingData.ReadBoolean();

		if ((Declaraciones.UserList[UserIndex].flags.Privilegios && (PlayerType.Admin || PlayerType.Dios)) != 0) {
			General.LogGM(Declaraciones.UserList[UserIndex].Name, Declaraciones.UserList[UserIndex].Name
					+ " ha cambiado la información sobre si está permitido usar el resucitar en el mapa.");
			Declaraciones.MapInfo[Declaraciones.UserList[UserIndex].Pos.Map].ResuSinEfecto = noresu;
			ES.WriteVar(vb6.App.Instance().Path + Declaraciones.MapPath + "mapa"
					+ Declaraciones.UserList[UserIndex].Pos.Map + ".dat",
					"Mapa" + Declaraciones.UserList[UserIndex].Pos.Map, "ResuSinEfecto", noresu);
			WriteConsoleMsg(UserIndex,
					"Mapa " + Declaraciones.UserList[UserIndex].Pos.Map + " ResuSinEfecto: "
							+ Declaraciones.MapInfo[Declaraciones.UserList[UserIndex].Pos.Map].ResuSinEfecto,
					FontTypeNames.FONTTYPE_INFO);
		}
	}

	/* '' */
	/* ' Handle the "ChangeMapInfoLand" message */
	/* ' */
	/* ' @param userIndex The index of the user sending the message */

	static void HandleChangeMapInfoLand(int UserIndex) {
		/* '*************************************************** */
		/* 'Author: Pablo (ToxicWaste) */
		/* 'Last Modification: 26/01/2007 */
		/*
		 * 'Terreno -> Opciones: "BOSQUE", "NIEVE", "DESIERTO", "CIUDAD",
		 * "CAMPO", "DUNGEON".
		 */
		/* '*************************************************** */
		if (Declaraciones.UserList[UserIndex].incomingData.length < 3) {
			throw new RuntimeException("Err . Raise UserList ( UserIndex ) . incomingData . NotEnoughDataErrCode");
			return;
		}

		/* FIXME: ON ERROR GOTO ErrHandler */
		String tStr;

		/*
		 * 'This packet contains strings, make a copy of the data to prevent
		 * losses if it's not complete yet...
		 */
		clsByteQueue buffer;
		buffer = new clsByteQueue();
		buffer.CopyBuffer(Declaraciones.UserList[UserIndex].incomingData);

		/* 'Remove Packet ID */
		buffer.ReadByte();

		tStr = buffer.ReadASCIIString();

		if ((Declaraciones.UserList[UserIndex].flags.Privilegios && (PlayerType.Admin || PlayerType.Dios)) != 0) {
			if (tStr == "BOSQUE" || tStr == "NIEVE" || tStr == "DESIERTO" || tStr == "CIUDAD" || tStr == "CAMPO"
					|| tStr == "DUNGEON") {
				General.LogGM(Declaraciones.UserList[UserIndex].Name,
						Declaraciones.UserList[UserIndex].Name + " ha cambiado la información del terreno del mapa.");

				Declaraciones.MapInfo[Declaraciones.UserList[UserIndex].Pos.Map].Terreno = Extra
						.TerrainStringToByte(tStr);

				ES.WriteVar(
						vb6.App.Instance().Path + Declaraciones.MapPath + "mapa"
								+ Declaraciones.UserList[UserIndex].Pos.Map + ".dat",
						"Mapa" + Declaraciones.UserList[UserIndex].Pos.Map, "Terreno", tStr);
				WriteConsoleMsg(UserIndex,
						"Mapa " + Declaraciones.UserList[UserIndex].Pos.Map + " Terreno: "
								+ Extra.TerrainByteToString(
										Declaraciones.MapInfo[Declaraciones.UserList[UserIndex].Pos.Map].Terreno),
						FontTypeNames.FONTTYPE_INFO);
			} else {
				WriteConsoleMsg(UserIndex,
						"Opciones para terreno: 'BOSQUE', 'NIEVE', 'DESIERTO', 'CIUDAD', 'CAMPO', 'DUNGEON'",
						FontTypeNames.FONTTYPE_INFO);
				WriteConsoleMsg(UserIndex,
						"Igualmente, el único útil es 'NIEVE' ya que al ingresarlo, la gente muere de frío en el mapa.",
						FontTypeNames.FONTTYPE_INFO);
			}
		}

		/*
		 * 'If we got here then packet is complete, copy data back to original
		 * queue
		 */
		Declaraciones.UserList[UserIndex].incomingData.CopyBuffer(buffer);

		/* FIXME: ErrHandler : */
		int ERROR;
		/* ERROR = Err . Number */
		/* FIXME: ON ERROR GOTO 0 */

		/* 'Destroy auxiliar buffer */
		buffer = null;

		if (ERROR != 0) {
			throw new RuntimeException("Err . Raise ERROR");
		}
	}

	/* '' */
	/* ' Handle the "ChangeMapInfoZone" message */
	/* ' */
	/* ' @param userIndex The index of the user sending the message */

	static void HandleChangeMapInfoZone(int UserIndex) {
		/* '*************************************************** */
		/* 'Author: Pablo (ToxicWaste) */
		/* 'Last Modification: 26/01/2007 */
		/*
		 * 'Zona -> Opciones: "BOSQUE", "NIEVE", "DESIERTO", "CIUDAD", "CAMPO",
		 * "DUNGEON".
		 */
		/* '*************************************************** */
		if (Declaraciones.UserList[UserIndex].incomingData.length < 3) {
			throw new RuntimeException("Err . Raise UserList ( UserIndex ) . incomingData . NotEnoughDataErrCode");
			return;
		}

		/* FIXME: ON ERROR GOTO ErrHandler */
		String tStr;

		/*
		 * 'This packet contains strings, make a copy of the data to prevent
		 * losses if it's not complete yet...
		 */
		clsByteQueue buffer;
		buffer = new clsByteQueue();
		buffer.CopyBuffer(Declaraciones.UserList[UserIndex].incomingData);

		/* 'Remove Packet ID */
		buffer.ReadByte();

		tStr = buffer.ReadASCIIString();

		if ((Declaraciones.UserList[UserIndex].flags.Privilegios && (PlayerType.Admin || PlayerType.Dios)) != 0) {
			if (tStr == "BOSQUE" || tStr == "NIEVE" || tStr == "DESIERTO" || tStr == "CIUDAD" || tStr == "CAMPO"
					|| tStr == "DUNGEON") {
				General.LogGM(Declaraciones.UserList[UserIndex].Name,
						Declaraciones.UserList[UserIndex].Name + " ha cambiado la información de la zona del mapa.");
				Declaraciones.MapInfo[Declaraciones.UserList[UserIndex].Pos.Map].Zona = tStr;
				ES.WriteVar(
						vb6.App.Instance().Path + Declaraciones.MapPath + "mapa"
								+ Declaraciones.UserList[UserIndex].Pos.Map + ".dat",
						"Mapa" + Declaraciones.UserList[UserIndex].Pos.Map, "Zona", tStr);
				WriteConsoleMsg(UserIndex,
						"Mapa " + Declaraciones.UserList[UserIndex].Pos.Map + " Zona: "
								+ Declaraciones.MapInfo[Declaraciones.UserList[UserIndex].Pos.Map].Zona,
						FontTypeNames.FONTTYPE_INFO);
			} else {
				WriteConsoleMsg(UserIndex,
						"Opciones para terreno: 'BOSQUE', 'NIEVE', 'DESIERTO', 'CIUDAD', 'CAMPO', 'DUNGEON'",
						FontTypeNames.FONTTYPE_INFO);
				WriteConsoleMsg(UserIndex,
						"Igualmente, el único útil es 'DUNGEON' ya que al ingresarlo, NO se sentirá el efecto de la lluvia en este mapa.",
						FontTypeNames.FONTTYPE_INFO);
			}
		}

		/*
		 * 'If we got here then packet is complete, copy data back to original
		 * queue
		 */
		Declaraciones.UserList[UserIndex].incomingData.CopyBuffer(buffer);

		/* FIXME: ErrHandler : */
		int ERROR;
		/* ERROR = Err . Number */
		/* FIXME: ON ERROR GOTO 0 */

		/* 'Destroy auxiliar buffer */
		buffer = null;

		if (ERROR != 0) {
			throw new RuntimeException("Err . Raise ERROR");
		}
	}

	/* '' */
	/* ' Handle the "ChangeMapInfoStealNp" message */
	/* ' */
	/* ' @param userIndex The index of the user sending the message */

	static void HandleChangeMapInfoStealNpc(int UserIndex) {
		/* '*************************************************** */
		/* 'Author: ZaMa */
		/* 'Last Modification: 25/07/2010 */
		/* 'RoboNpcsPermitido -> Options: "1", "0" */
		/* '*************************************************** */
		if (Declaraciones.UserList[UserIndex].incomingData.length < 2) {
			throw new RuntimeException("Err . Raise UserList ( UserIndex ) . incomingData . NotEnoughDataErrCode");
			return;
		}

		int RoboNpc;

		/* 'Remove Packet ID */
		Declaraciones.UserList[UserIndex].incomingData.ReadByte();

		RoboNpc = vb6.val(vb6.IIf(Declaraciones.UserList[UserIndex].incomingData.ReadBoolean(), 1, 0));

		if ((Declaraciones.UserList[UserIndex].flags.Privilegios && (PlayerType.Admin || PlayerType.Dios)) != 0) {
			General.LogGM(Declaraciones.UserList[UserIndex].Name, Declaraciones.UserList[UserIndex].Name
					+ " ha cambiado la información sobre si está permitido robar npcs en el mapa.");

			Declaraciones.MapInfo[Declaraciones.UserList[UserIndex].Pos.Map].RoboNpcsPermitido = RoboNpc;

			ES.WriteVar(vb6.App.Instance().Path + Declaraciones.MapPath + "mapa"
					+ Declaraciones.UserList[UserIndex].Pos.Map + ".dat",
					"Mapa" + Declaraciones.UserList[UserIndex].Pos.Map, "RoboNpcsPermitido", RoboNpc);
			WriteConsoleMsg(UserIndex,
					"Mapa " + Declaraciones.UserList[UserIndex].Pos.Map + " RoboNpcsPermitido: "
							+ Declaraciones.MapInfo[Declaraciones.UserList[UserIndex].Pos.Map].RoboNpcsPermitido,
					FontTypeNames.FONTTYPE_INFO);
		}
	}

	/* '' */
	/* ' Handle the "ChangeMapInfoNoOcultar" message */
	/* ' */
	/* ' @param userIndex The index of the user sending the message */

	static void HandleChangeMapInfoNoOcultar(int UserIndex) {
		/* '*************************************************** */
		/* 'Author: ZaMa */
		/* 'Last Modification: 18/09/2010 */
		/* 'OcultarSinEfecto -> Options: "1", "0" */
		/* '*************************************************** */
		if (Declaraciones.UserList[UserIndex].incomingData.length < 2) {
			throw new RuntimeException("Err . Raise UserList ( UserIndex ) . incomingData . NotEnoughDataErrCode");
			return;
		}

		int NoOcultar;
		int mapa;

		/* 'Remove Packet ID */
		Declaraciones.UserList[UserIndex].incomingData.ReadByte();

		NoOcultar = vb6.val(vb6.IIf(Declaraciones.UserList[UserIndex].incomingData.ReadBoolean(), 1, 0));

		if ((Declaraciones.UserList[UserIndex].flags.Privilegios && (PlayerType.Admin || PlayerType.Dios)) != 0) {

			mapa = Declaraciones.UserList[UserIndex].Pos.Map;

			General.LogGM(Declaraciones.UserList[UserIndex].Name, Declaraciones.UserList[UserIndex].Name
					+ " ha cambiado la información sobre si está permitido ocultarse en el mapa " + mapa + ".");

			Declaraciones.MapInfo[mapa].OcultarSinEfecto = NoOcultar;

			ES.WriteVar(vb6.App.Instance().Path + Declaraciones.MapPath + "mapa" + mapa + ".dat", "Mapa" + mapa,
					"OcultarSinEfecto", NoOcultar);
			WriteConsoleMsg(UserIndex, "Mapa " + mapa + " OcultarSinEfecto: " + NoOcultar, FontTypeNames.FONTTYPE_INFO);
		}

	}

	/* '' */
	/* ' Handle the "ChangeMapInfoNoInvocar" message */
	/* ' */
	/* ' @param userIndex The index of the user sending the message */

	static void HandleChangeMapInfoNoInvocar(int UserIndex) {
		/* '*************************************************** */
		/* 'Author: ZaMa */
		/* 'Last Modification: 18/09/2010 */
		/* 'InvocarSinEfecto -> Options: "1", "0" */
		/* '*************************************************** */
		if (Declaraciones.UserList[UserIndex].incomingData.length < 2) {
			throw new RuntimeException("Err . Raise UserList ( UserIndex ) . incomingData . NotEnoughDataErrCode");
			return;
		}

		int NoInvocar;
		int mapa;

		/* 'Remove Packet ID */
		Declaraciones.UserList[UserIndex].incomingData.ReadByte();

		NoInvocar = vb6.val(vb6.IIf(Declaraciones.UserList[UserIndex].incomingData.ReadBoolean(), 1, 0));

		if ((Declaraciones.UserList[UserIndex].flags.Privilegios && (PlayerType.Admin || PlayerType.Dios)) != 0) {

			mapa = Declaraciones.UserList[UserIndex].Pos.Map;

			General.LogGM(Declaraciones.UserList[UserIndex].Name, Declaraciones.UserList[UserIndex].Name
					+ " ha cambiado la información sobre si está permitido invocar en el mapa " + mapa + ".");

			Declaraciones.MapInfo[mapa].InvocarSinEfecto = NoInvocar;

			ES.WriteVar(vb6.App.Instance().Path + Declaraciones.MapPath + "mapa" + mapa + ".dat", "Mapa" + mapa,
					"InvocarSinEfecto", NoInvocar);
			WriteConsoleMsg(UserIndex, "Mapa " + mapa + " InvocarSinEfecto: " + NoInvocar, FontTypeNames.FONTTYPE_INFO);
		}

	}

	/* '' */
	/* ' Handle the "SaveMap" message */
	/* ' */
	/* ' @param userIndex The index of the user sending the message */

	static void HandleSaveMap(int UserIndex) {
		/* '*************************************************** */
		/* 'Author: Lucas Tavolaro Ortiz (Tavo) */
		/* 'Last Modification: 12/24/06 */
		/* 'Saves the map */
		/* '*************************************************** */
		/* 'Remove Packet ID */
		Declaraciones.UserList[UserIndex].incomingData.ReadByte();

		if (Declaraciones.UserList[UserIndex].flags.Privilegios
				&& (PlayerType.User || PlayerType.Consejero || PlayerType.SemiDios || PlayerType.RoleMaster)) {
			return;
		}

		General.LogGM(Declaraciones.UserList[UserIndex].Name, Declaraciones.UserList[UserIndex].Name
				+ " ha guardado el mapa " + vb6.CStr(Declaraciones.UserList[UserIndex].Pos.Map));

		ES.GrabarMapa(Declaraciones.UserList[UserIndex].Pos.Map,
				vb6.App.Instance().Path + "\\WorldBackUp\\Mapa" + Declaraciones.UserList[UserIndex].Pos.Map);

		WriteConsoleMsg(UserIndex, "Mapa Guardado.", FontTypeNames.FONTTYPE_INFO);
	}

	/* '' */
	/* ' Handle the "ShowGuildMessages" message */
	/* ' */
	/* ' @param userIndex The index of the user sending the message */

	static void HandleShowGuildMessages(int UserIndex) {
		/* '*************************************************** */
		/* 'Author: Lucas Tavolaro Ortiz (Tavo) */
		/* 'Last Modification: 12/24/06 */
		/* 'Last modified by: Juan Martín Sotuyo Dodero (Maraxus) */
		/* 'Allows admins to read guild messages */
		/* '*************************************************** */
		if (Declaraciones.UserList[UserIndex].incomingData.length < 3) {
			throw new RuntimeException("Err . Raise UserList ( UserIndex ) . incomingData . NotEnoughDataErrCode");
			return;
		}

		/* FIXME: ON ERROR GOTO ErrHandler */
		/*
		 * 'This packet contains strings, make a copy of the data to prevent
		 * losses if it's not complete yet...
		 */
		clsByteQueue buffer;
		buffer = new clsByteQueue();
		buffer.CopyBuffer(Declaraciones.UserList[UserIndex].incomingData);

		/* 'Remove packet ID */
		buffer.ReadByte();

		String guild;

		guild = buffer.ReadASCIIString();

		if ((!Declaraciones.UserList[UserIndex].flags.Privilegios && PlayerType.RoleMaster) != 0
				&& (Declaraciones.UserList[UserIndex].flags.Privilegios && (PlayerType.Admin || PlayerType.Dios))) {
			modGuilds.GMEscuchaClan(UserIndex, guild);
		}

		/*
		 * 'If we got here then packet is complete, copy data back to original
		 * queue
		 */
		Declaraciones.UserList[UserIndex].incomingData.CopyBuffer(buffer);

		/* FIXME: ErrHandler : */
		int ERROR;
		/* ERROR = Err . Number */
		/* FIXME: ON ERROR GOTO 0 */

		/* 'Destroy auxiliar buffer */
		buffer = null;

		if (ERROR != 0) {
			throw new RuntimeException("Err . Raise ERROR");
		}
	}

	/* '' */
	/* ' Handle the "DoBackUp" message */
	/* ' */
	/* ' @param userIndex The index of the user sending the message */

	static void HandleDoBackUp(int UserIndex) {
 /* '*************************************************** */
 /* 'Author: Lucas Tavolaro Ortiz (Tavo) */
 /* 'Last Modification: 12/24/06 */
 /* 'Show guilds messages */
 /* '*************************************************** */
  /* 'Remove Packet ID */
  Declaraciones.UserList[UserIndex].incomingData.ReadByte();
  
  if (Declaraciones.UserList[UserIndex].flags.Privilegios && (PlayerType.User || PlayerType.Consejero || PlayerType.SemiDios || PlayerType.RoleMaster)) {
  return;
  }
  
  General.LogGM(Declaraciones.UserList[UserIndex].Name, Declaraciones.UserList[UserIndex].Name + " ha hecho un backup.");
  
  /* 'Sino lo confunde con la id del paquete */
  ES.DoBackUp;
}

	/* '' */
	/* ' Handle the "ToggleCentinelActivated" message */
	/* ' */
	/* ' @param userIndex The index of the user sending the message */

	static void HandleToggleCentinelActivated(int UserIndex) {
		/* '*************************************************** */
		/* 'Author: Lucas Tavolaro Ortiz (Tavo) */
		/* 'Last Modification: 12/26/06 */
		/* 'Last modified by: Juan Martín Sotuyo Dodero (Maraxus) */
		/* 'Activate or desactivate the Centinel */
		/* '*************************************************** */
		/* 'Remove Packet ID */
		Declaraciones.UserList[UserIndex].incomingData.ReadByte();

		if (Declaraciones.UserList[UserIndex].flags.Privilegios
				&& (PlayerType.User || PlayerType.Consejero || PlayerType.SemiDios)) {
			return;
		}

		modCentinela.centinelaActivado = !modCentinela.centinelaActivado;

		modCentinela.ResetCentinelas();

		if (modCentinela.centinelaActivado) {
			modSendData.SendData(SendTarget.ToAdmins, 0,
					PrepareMessageConsoleMsg("El centinela ha sido activado.", FontTypeNames.FONTTYPE_SERVER));
		} else {
			modSendData.SendData(SendTarget.ToAdmins, 0,
					PrepareMessageConsoleMsg("El centinela ha sido desactivado.", FontTypeNames.FONTTYPE_SERVER));
		}
	}

	/* '' */
	/* ' Handle the "AlterName" message */
	/* ' */
	/* ' @param userIndex The index of the user sending the message */

	static void HandleAlterName(int UserIndex) {
		/* '*************************************************** */
		/* 'Author: Juan Martín Sotuyo Dodero (Maraxus) */
		/* 'Last Modification: 12/26/06 */
		/* 'Change user name */
		/* '*************************************************** */
		if (Declaraciones.UserList[UserIndex].incomingData.length < 5) {
			throw new RuntimeException("Err . Raise UserList ( UserIndex ) . incomingData . NotEnoughDataErrCode");
			return;
		}

		/* FIXME: ON ERROR GOTO ErrHandler */
		/*
		 * 'This packet contains strings, make a copy of the data to prevent
		 * losses if it's not complete yet...
		 */
		clsByteQueue buffer;
		buffer = new clsByteQueue();
		buffer.CopyBuffer(Declaraciones.UserList[UserIndex].incomingData);

		/* 'Remove packet ID */
		buffer.ReadByte();

		/* 'Reads the userName and newUser Packets */
		String UserName;
		String newName;
		int changeNameUI;
		int GuildIndex;

		UserName = buffer.ReadASCIIString();
		newName = buffer.ReadASCIIString();

		if ((!Declaraciones.UserList[UserIndex].flags.Privilegios && PlayerType.RoleMaster) != 0
				&& (Declaraciones.UserList[UserIndex].flags.Privilegios && (PlayerType.Admin || PlayerType.Dios))
				|| Declaraciones.UserList[UserIndex].flags.PrivEspecial) {
			if (vb6.LenB(UserName) == 0 || vb6.LenB(newName) == 0) {
				WriteConsoleMsg(UserIndex, "Usar: /ANAME origen@destino", FontTypeNames.FONTTYPE_INFO);
			} else {
				changeNameUI = Extra.NameIndex(UserName);

				if (changeNameUI > 0) {
					WriteConsoleMsg(UserIndex, "El Pj está online, debe salir para hacer el cambio.",
							FontTypeNames.FONTTYPE_WARNING);
				} else {
					if (!General.FileExist(Declaraciones.CharPath + UserName + ".chr")) {
						WriteConsoleMsg(UserIndex, "El pj " + UserName + " es inexistente.",
								FontTypeNames.FONTTYPE_INFO);
					} else {
						GuildIndex = vb6
								.val(ES.GetVar(Declaraciones.CharPath + UserName + ".chr", "GUILD", "GUILDINDEX"));

						if (GuildIndex > 0) {
							WriteConsoleMsg(UserIndex,
									"El pj " + UserName
											+ " pertenece a un clan, debe salir del mismo con /salirclan para ser transferido.",
									FontTypeNames.FONTTYPE_INFO);
						} else {
							if (!General.FileExist(Declaraciones.CharPath + newName + ".chr")) {
								vb6.FileCopy(Declaraciones.CharPath + UserName + ".chr",
										Declaraciones.CharPath + vb6.UCase(newName) + ".chr");

								WriteConsoleMsg(UserIndex, "Transferencia exitosa.", FontTypeNames.FONTTYPE_INFO);

								ES.WriteVar(Declaraciones.CharPath + UserName + ".chr", "FLAGS", "Ban", "1");

								int cantPenas;

								cantPenas = vb6
										.val(ES.GetVar(Declaraciones.CharPath + UserName + ".chr", "PENAS", "Cant"));

								ES.WriteVar(Declaraciones.CharPath + UserName + ".chr", "PENAS", "Cant",
										vb6.CStr(cantPenas + 1));

								ES.WriteVar(Declaraciones.CharPath + UserName + ".chr", "PENAS",
										"P" + vb6.CStr(cantPenas + 1),
										vb6.LCase(Declaraciones.UserList[UserIndex].Name)
												+ ": BAN POR Cambio de nick a " + vb6.UCase(newName) + " " + Date + " "
												+ vb6.time());

								General.LogGM(Declaraciones.UserList[UserIndex].Name,
										"Ha cambiado de nombre al usuario " + UserName + ". Ahora se llama " + newName);
							} else {
								WriteConsoleMsg(UserIndex, "El nick solicitado ya existe.",
										FontTypeNames.FONTTYPE_INFO);
							}
						}
					}
				}
			}
		}

		/*
		 * 'If we got here then packet is complete, copy data back to original
		 * queue
		 */
		Declaraciones.UserList[UserIndex].incomingData.CopyBuffer(buffer);

		/* FIXME: ErrHandler : */
		int ERROR;
		/* ERROR = Err . Number */
		/* FIXME: ON ERROR GOTO 0 */

		/* 'Destroy auxiliar buffer */
		buffer = null;

		if (ERROR != 0) {
			throw new RuntimeException("Err . Raise ERROR");
		}
	}

	/* '' */
	/* ' Handle the "AlterName" message */
	/* ' */
	/* ' @param userIndex The index of the user sending the message */

	static void HandleAlterMail(int UserIndex) {
		/* '*************************************************** */
		/* 'Author: Juan Martín Sotuyo Dodero (Maraxus) */
		/* 'Last Modification: 12/26/06 */
		/* 'Change user password */
		/* '*************************************************** */
		if (Declaraciones.UserList[UserIndex].incomingData.length < 5) {
			throw new RuntimeException("Err . Raise UserList ( UserIndex ) . incomingData . NotEnoughDataErrCode");
			return;
		}

		/* FIXME: ON ERROR GOTO ErrHandler */
		/*
		 * 'This packet contains strings, make a copy of the data to prevent
		 * losses if it's not complete yet...
		 */
		clsByteQueue buffer;
		buffer = new clsByteQueue();
		buffer.CopyBuffer(Declaraciones.UserList[UserIndex].incomingData);

		/* 'Remove packet ID */
		buffer.ReadByte();

		String UserName;
		String newMail;

		UserName = buffer.ReadASCIIString();
		newMail = buffer.ReadASCIIString();

		if ((!Declaraciones.UserList[UserIndex].flags.Privilegios && PlayerType.RoleMaster) != 0
				&& (Declaraciones.UserList[UserIndex].flags.Privilegios && (PlayerType.Admin || PlayerType.Dios))) {
			if (vb6.LenB(UserName) == 0 || vb6.LenB(newMail) == 0) {
				WriteConsoleMsg(UserIndex, "usar /AEMAIL <pj>-<nuevomail>", FontTypeNames.FONTTYPE_INFO);
			} else {
				if (!General.FileExist(Declaraciones.CharPath + UserName + ".chr")) {
					WriteConsoleMsg(UserIndex, "No existe el charfile " + UserName + ".chr",
							FontTypeNames.FONTTYPE_INFO);
				} else {
					ES.WriteVar(Declaraciones.CharPath + UserName + ".chr", "CONTACTO", "Email", newMail);
					WriteConsoleMsg(UserIndex, "Email de " + UserName + " cambiado a: " + newMail,
							FontTypeNames.FONTTYPE_INFO);
				}

				General.LogGM(Declaraciones.UserList[UserIndex].Name, "Le ha cambiado el mail a " + UserName);
			}
		}

		/*
		 * 'If we got here then packet is complete, copy data back to original
		 * queue
		 */
		Declaraciones.UserList[UserIndex].incomingData.CopyBuffer(buffer);

		/* FIXME: ErrHandler : */
		int ERROR;
		/* ERROR = Err . Number */
		/* FIXME: ON ERROR GOTO 0 */

		/* 'Destroy auxiliar buffer */
		buffer = null;

		if (ERROR != 0) {
			throw new RuntimeException("Err . Raise ERROR");
		}
	}

	/* '' */
	/* ' Handle the "AlterPassword" message */
	/* ' */
	/* ' @param userIndex The index of the user sending the message */

	static void HandleAlterPassword(int UserIndex) {
		/* '*************************************************** */
		/* 'Author: Juan Martín Sotuyo Dodero (Maraxus) */
		/* 'Last Modification: 12/26/06 */
		/* 'Change user password */
		/* '*************************************************** */
		if (Declaraciones.UserList[UserIndex].incomingData.length < 5) {
			throw new RuntimeException("Err . Raise UserList ( UserIndex ) . incomingData . NotEnoughDataErrCode");
			return;
		}

		/* FIXME: ON ERROR GOTO ErrHandler */
		/*
		 * 'This packet contains strings, make a copy of the data to prevent
		 * losses if it's not complete yet...
		 */
		clsByteQueue buffer;
		buffer = new clsByteQueue();
		buffer.CopyBuffer(Declaraciones.UserList[UserIndex].incomingData);

		/* 'Remove packet ID */
		buffer.ReadByte();

		String UserName;
		String copyFrom;
		String Password;

		UserName = vb6.Replace(buffer.ReadASCIIString(), "+", " ");
		copyFrom = vb6.Replace(buffer.ReadASCIIString(), "+", " ");

		if ((!Declaraciones.UserList[UserIndex].flags.Privilegios && PlayerType.RoleMaster) != 0
				&& (Declaraciones.UserList[UserIndex].flags.Privilegios && (PlayerType.Admin || PlayerType.Dios))) {
			General.LogGM(Declaraciones.UserList[UserIndex].Name, "Ha alterado la contrasena de " + UserName);

			if (vb6.LenB(UserName) == 0 || vb6.LenB(copyFrom) == 0) {
				WriteConsoleMsg(UserIndex, "usar /APASS <pjsinpass>@<pjconpass>", FontTypeNames.FONTTYPE_INFO);
			} else {
				if (!General.FileExist(Declaraciones.CharPath + UserName + ".chr")
						|| !General.FileExist(Declaraciones.CharPath + copyFrom + ".chr")) {
					WriteConsoleMsg(UserIndex, "Alguno de los PJs no existe " + UserName + "@" + copyFrom,
							FontTypeNames.FONTTYPE_INFO);
				} else {
					Password = ES.GetVar(Declaraciones.CharPath + copyFrom + ".chr", "INIT", "Password");
					ES.WriteVar(Declaraciones.CharPath + UserName + ".chr", "INIT", "Password", Password);

					WriteConsoleMsg(UserIndex, "Password de " + UserName + " ha cambiado por la de " + copyFrom,
							FontTypeNames.FONTTYPE_INFO);
				}
			}
		}

		/*
		 * 'If we got here then packet is complete, copy data back to original
		 * queue
		 */
		Declaraciones.UserList[UserIndex].incomingData.CopyBuffer(buffer);

		/* FIXME: ErrHandler : */
		int ERROR;
		/* ERROR = Err . Number */
		/* FIXME: ON ERROR GOTO 0 */

		/* 'Destroy auxiliar buffer */
		buffer = null;

		if (ERROR != 0) {
			throw new RuntimeException("Err . Raise ERROR");
		}
	}

	/* '' */
	/* ' Handle the "HandleCreateNPC" message */
	/* ' */
	/* ' @param userIndex The index of the user sending the message */

	static void HandleCreateNPC(int UserIndex) {
		/* '*************************************************** */
		/* 'Author: Juan Martín Sotuyo Dodero (Maraxus) */
		/* 'Last Modification: 26/09/2010 */
		/* '26/09/2010: ZaMa - Ya no se pueden crear npcs pretorianos. */
		/* '*************************************************** */
		if (Declaraciones.UserList[UserIndex].incomingData.length < 3) {
			throw new RuntimeException("Err . Raise UserList ( UserIndex ) . incomingData . NotEnoughDataErrCode");
			return;
		}

		/* 'Remove Packet ID */
		Declaraciones.UserList[UserIndex].incomingData.ReadByte();

		int NpcIndex;

		NpcIndex = Declaraciones.UserList[UserIndex].incomingData.ReadInteger();

		if (Declaraciones.UserList[UserIndex].flags.Privilegios
				&& (PlayerType.User || PlayerType.Consejero || PlayerType.SemiDios)) {
			return;
		}

		if (NpcIndex >= 900) {
			WriteConsoleMsg(UserIndex,
					"No puedes sumonear miembros del clan pretoriano de esta forma, utiliza /CrearClanPretoriano.",
					FontTypeNames.FONTTYPE_WARNING);
			return;
		}

		NpcIndex = NPCs.SpawnNpc(NpcIndex, Declaraciones.UserList[UserIndex].Pos, true, false);

		if (NpcIndex != 0) {
			General.LogGM(Declaraciones.UserList[UserIndex].Name, "Sumoneó a " + Declaraciones.Npclist[NpcIndex].Name
					+ " en mapa " + Declaraciones.UserList[UserIndex].Pos.Map);
		}
	}

	/* '' */
	/* ' Handle the "CreateNPCWithRespawn" message */
	/* ' */
	/* ' @param userIndex The index of the user sending the message */

	static void HandleCreateNPCWithRespawn(int UserIndex) {
		/* '*************************************************** */
		/* 'Author: Juan Martín Sotuyo Dodero (Maraxus) */
		/* 'Last Modification: 26/09/2010 */
		/* '26/09/2010: ZaMa - Ya no se pueden crear npcs pretorianos. */
		/* '*************************************************** */
		if (Declaraciones.UserList[UserIndex].incomingData.length < 3) {
			throw new RuntimeException("Err . Raise UserList ( UserIndex ) . incomingData . NotEnoughDataErrCode");
			return;
		}

		/* 'Remove Packet ID */
		Declaraciones.UserList[UserIndex].incomingData.ReadByte();

		int NpcIndex;

		NpcIndex = Declaraciones.UserList[UserIndex].incomingData.ReadInteger();

		if (NpcIndex >= 900) {
			WriteConsoleMsg(UserIndex,
					"No puedes sumonear miembros del clan pretoriano de esta forma, utiliza /CrearClanPretoriano.",
					FontTypeNames.FONTTYPE_WARNING);
			return;
		}

		if (Declaraciones.UserList[UserIndex].flags.Privilegios
				&& (PlayerType.User || PlayerType.Consejero || PlayerType.SemiDios)) {
			return;
		}

		NpcIndex = NPCs.SpawnNpc(NpcIndex, Declaraciones.UserList[UserIndex].Pos, true, true);

		if (NpcIndex != 0) {
			General.LogGM(Declaraciones.UserList[UserIndex].Name, "Sumoneó con respawn "
					+ Declaraciones.Npclist[NpcIndex].Name + " en mapa " + Declaraciones.UserList[UserIndex].Pos.Map);
		}
	}

	/* '' */
	/* ' Handle the "ImperialArmour" message */
	/* ' */
	/* ' @param userIndex The index of the user sending the message */

	static void HandleImperialArmour(int UserIndex) {
		/* '*************************************************** */
		/* 'Author: Juan Martín Sotuyo Dodero (Maraxus) */
		/* 'Last Modification: 12/24/06 */
		/* ' */
		/* '*************************************************** */
		if (Declaraciones.UserList[UserIndex].incomingData.length < 4) {
			throw new RuntimeException("Err . Raise UserList ( UserIndex ) . incomingData . NotEnoughDataErrCode");
			return;
		}

		/* 'Remove Packet ID */
		Declaraciones.UserList[UserIndex].incomingData.ReadByte();

		int index;
		int ObjIndex;

		index = Declaraciones.UserList[UserIndex].incomingData.ReadByte();
		ObjIndex = Declaraciones.UserList[UserIndex].incomingData.ReadInteger();

		if (Declaraciones.UserList[UserIndex].flags.Privilegios
				&& (PlayerType.User || PlayerType.Consejero || PlayerType.SemiDios || PlayerType.RoleMaster)) {
			return;
		}

		switch (index) {
		case 1:
			ModFacciones.ArmaduraImperial1 = ObjIndex;

			break;

		case 2:
			ModFacciones.ArmaduraImperial2 = ObjIndex;

			break;

		case 3:
			ModFacciones.ArmaduraImperial3 = ObjIndex;

			break;

		case 4:
			ModFacciones.TunicaMagoImperial = ObjIndex;
			break;
		}
	}

	/* '' */
	/* ' Handle the "ChaosArmour" message */
	/* ' */
	/* ' @param userIndex The index of the user sending the message */

	static void HandleChaosArmour(int UserIndex) {
		/* '*************************************************** */
		/* 'Author: Juan Martín Sotuyo Dodero (Maraxus) */
		/* 'Last Modification: 12/24/06 */
		/* ' */
		/* '*************************************************** */
		if (Declaraciones.UserList[UserIndex].incomingData.length < 4) {
			throw new RuntimeException("Err . Raise UserList ( UserIndex ) . incomingData . NotEnoughDataErrCode");
			return;
		}

		/* 'Remove Packet ID */
		Declaraciones.UserList[UserIndex].incomingData.ReadByte();

		int index;
		int ObjIndex;

		index = Declaraciones.UserList[UserIndex].incomingData.ReadByte();
		ObjIndex = Declaraciones.UserList[UserIndex].incomingData.ReadInteger();

		if (Declaraciones.UserList[UserIndex].flags.Privilegios
				&& (PlayerType.User || PlayerType.Consejero || PlayerType.SemiDios || PlayerType.RoleMaster)) {
			return;
		}

		switch (index) {
		case 1:
			ModFacciones.ArmaduraCaos1 = ObjIndex;

			break;

		case 2:
			ModFacciones.ArmaduraCaos2 = ObjIndex;

			break;

		case 3:
			ModFacciones.ArmaduraCaos3 = ObjIndex;

			break;

		case 4:
			ModFacciones.TunicaMagoCaos = ObjIndex;
			break;
		}
	}

	/* '' */
	/* ' Handle the "NavigateToggle" message */
	/* ' */
	/* ' @param userIndex The index of the user sending the message */

	static void HandleNavigateToggle(int UserIndex) {
		/* '*************************************************** */
		/* 'Author: Juan Martín Sotuyo Dodero (Maraxus) */
		/* 'Last Modification: 01/12/07 */
		/* ' */
		/* '*************************************************** */
		/* 'Remove Packet ID */
		Declaraciones.UserList[UserIndex].incomingData.ReadByte();

		if (Declaraciones.UserList[UserIndex].flags.Privilegios && (PlayerType.User || PlayerType.Consejero)) {
			return;
		}

		if (Declaraciones.UserList[UserIndex].flags.Navegando == 1) {
			Declaraciones.UserList[UserIndex].flags.Navegando = 0;
		} else {
			Declaraciones.UserList[UserIndex].flags.Navegando = 1;
		}

		/* 'Tell the client that we are navigating. */
		WriteNavigateToggle(UserIndex);
	}

	/* '' */
	/* ' Handle the "ServerOpenToUsersToggle" message */
	/* ' */
	/* ' @param userIndex The index of the user sending the message */

	static void HandleServerOpenToUsersToggle(int UserIndex) {
		/* '*************************************************** */
		/* 'Author: Juan Martín Sotuyo Dodero (Maraxus) */
		/* 'Last Modification: 12/24/06 */
		/* ' */
		/* '*************************************************** */
		/* 'Remove Packet ID */
		Declaraciones.UserList[UserIndex].incomingData.ReadByte();

		if (Declaraciones.UserList[UserIndex].flags.Privilegios
				&& (PlayerType.User || PlayerType.Consejero || PlayerType.SemiDios || PlayerType.RoleMaster)) {
			return;
		}

		if (Declaraciones.ServerSoloGMs > 0) {
			WriteConsoleMsg(UserIndex, "Servidor habilitado para todos.", FontTypeNames.FONTTYPE_INFO);
			Declaraciones.ServerSoloGMs = 0;
			frmMain.chkServerHabilitado.value = vbUnchecked;
		} else {
			WriteConsoleMsg(UserIndex, "Servidor restringido a administradores.", FontTypeNames.FONTTYPE_INFO);
			Declaraciones.ServerSoloGMs = 1;
			frmMain.chkServerHabilitado.value = vbChecked;
		}
	}

	/* '' */
	/* ' Handle the "TurnOffServer" message */
	/* ' */
	/* ' @param userIndex The index of the user sending the message */

	static void HandleTurnOffServer(int UserIndex) {
		/* '*************************************************** */
		/* 'Author: Juan Martín Sotuyo Dodero (Maraxus) */
		/* 'Last Modification: 12/24/06 */
		/* 'Turns off the server */
		/* '*************************************************** */
		int handle;

		/* 'Remove Packet ID */
		Declaraciones.UserList[UserIndex].incomingData.ReadByte();

		if (Declaraciones.UserList[UserIndex].flags.Privilegios
				&& (PlayerType.User || PlayerType.Consejero || PlayerType.SemiDios || PlayerType.RoleMaster)) {
			return;
		}

		General.LogGM(Declaraciones.UserList[UserIndex].Name, "/APAGAR");
		modSendData.SendData(SendTarget.ToAll, 0,
				PrepareMessageConsoleMsg("¡¡¡" + Declaraciones.UserList[UserIndex].Name + " VA A APAGAR EL SERVIDOR!!!",
						FontTypeNames.FONTTYPE_FIGHT));

		/* 'Log */
		handle = vb6.FreeFile();
		/*
		 * FIXME: OPEN App . Path & "\\logs\\Main.log" FOR Append Shared AS #
		 * handle
		 */

		/*
		 * FIXME: PRINT # handle , Date & " " & time & " server apagado por " &
		 * . Name & ". "
		 */

		/* FIXME: CLOSE # handle */

		Unload(frmMain);
	}

	/* '' */
	/* ' Handle the "TurnCriminal" message */
	/* ' */
	/* ' @param userIndex The index of the user sending the message */

	static void HandleTurnCriminal(int UserIndex) {
		/* '*************************************************** */
		/* 'Author: Juan Martín Sotuyo Dodero (Maraxus) */
		/* 'Last Modification: 12/26/06 */
		/* ' */
		/* '*************************************************** */
		if (Declaraciones.UserList[UserIndex].incomingData.length < 3) {
			throw new RuntimeException("Err . Raise UserList ( UserIndex ) . incomingData . NotEnoughDataErrCode");
			return;
		}

		/* FIXME: ON ERROR GOTO ErrHandler */
		/*
		 * 'This packet contains strings, make a copy of the data to prevent
		 * losses if it's not complete yet...
		 */
		clsByteQueue buffer;
		buffer = new clsByteQueue();
		buffer.CopyBuffer(Declaraciones.UserList[UserIndex].incomingData);

		/* 'Remove packet ID */
		buffer.ReadByte();

		String UserName;
		int tUser;

		UserName = buffer.ReadASCIIString();

		if ((!Declaraciones.UserList[UserIndex].flags.Privilegios && PlayerType.RoleMaster) != 0
				&& (Declaraciones.UserList[UserIndex].flags.Privilegios && (PlayerType.Admin || PlayerType.Dios))) {
			General.LogGM(Declaraciones.UserList[UserIndex].Name, "/CONDEN " + UserName);

			tUser = Extra.NameIndex(UserName);
			if (tUser > 0) {
				UsUaRiOs.VolverCriminal(tUser);
			}
		}

		/*
		 * 'If we got here then packet is complete, copy data back to original
		 * queue
		 */
		Declaraciones.UserList[UserIndex].incomingData.CopyBuffer(buffer);

		/* FIXME: ErrHandler : */
		int ERROR;
		/* ERROR = Err . Number */
		/* FIXME: ON ERROR GOTO 0 */

		/* 'Destroy auxiliar buffer */
		buffer = null;

		if (ERROR != 0) {
			throw new RuntimeException("Err . Raise ERROR");
		}
	}

	/* '' */
	/* ' Handle the "ResetFactions" message */
	/* ' */
	/* ' @param userIndex The index of the user sending the message */

	static void HandleResetFactions(int UserIndex) {
		/* '*************************************************** */
		/* 'Author: Juan Martín Sotuyo Dodero (Maraxus) */
		/* 'Last Modification: 06/09/09 */
		/* ' */
		/* '*************************************************** */
		if (Declaraciones.UserList[UserIndex].incomingData.length < 3) {
			throw new RuntimeException("Err . Raise UserList ( UserIndex ) . incomingData . NotEnoughDataErrCode");
			return;
		}

		/* FIXME: ON ERROR GOTO ErrHandler */
		/*
		 * 'This packet contains strings, make a copy of the data to prevent
		 * losses if it's not complete yet...
		 */
		clsByteQueue buffer;
		buffer = new clsByteQueue();
		buffer.CopyBuffer(Declaraciones.UserList[UserIndex].incomingData);

		/* 'Remove packet ID */
		buffer.ReadByte();

		String UserName;
		String Reason;
		int tUser;
		String Char;
		int cantPenas;

		UserName = buffer.ReadASCIIString();

		if ((!Declaraciones.UserList[UserIndex].flags.Privilegios && PlayerType.RoleMaster) != 0
				&& (Declaraciones.UserList[UserIndex].flags.Privilegios && (PlayerType.Admin || PlayerType.Dios))
				|| Declaraciones.UserList[UserIndex].flags.PrivEspecial) {
			General.LogGM(Declaraciones.UserList[UserIndex].Name, "/RAJAR " + UserName);

			tUser = Extra.NameIndex(UserName);

			if (tUser > 0) {
				TCP.ResetFacciones(tUser);

				cantPenas = vb6.val(ES.GetVar(Declaraciones.CharPath + UserName + ".chr", "PENAS", "Cant"));
				ES.WriteVar(Declaraciones.CharPath + UserName + ".chr", "PENAS", "Cant", cantPenas + 1);
				ES.WriteVar(Declaraciones.CharPath + UserName + ".chr", "PENAS", "P" + cantPenas + 1,
						vb6.LCase(Declaraciones.UserList[UserIndex].Name) + ": Personaje reincorporado a la facción. "
								+ Date + " " + vb6.time());
			} else {
				Char = Declaraciones.CharPath + UserName + ".chr";

				if (General.FileExist(Char, 0)) {
					ES.WriteVar(Char, "FACCIONES", "EjercitoReal", 0);
					ES.WriteVar(Char, "FACCIONES", "CiudMatados", 0);
					ES.WriteVar(Char, "FACCIONES", "CrimMatados", 0);
					ES.WriteVar(Char, "FACCIONES", "EjercitoCaos", 0);
					ES.WriteVar(Char, "FACCIONES", "FechaIngreso", "No ingresó a ninguna Facción");
					ES.WriteVar(Char, "FACCIONES", "rArCaos", 0);
					ES.WriteVar(Char, "FACCIONES", "rArReal", 0);
					ES.WriteVar(Char, "FACCIONES", "rExCaos", 0);
					ES.WriteVar(Char, "FACCIONES", "rExReal", 0);
					ES.WriteVar(Char, "FACCIONES", "recCaos", 0);
					ES.WriteVar(Char, "FACCIONES", "recReal", 0);
					ES.WriteVar(Char, "FACCIONES", "Reenlistadas", 0);
					ES.WriteVar(Char, "FACCIONES", "NivelIngreso", 0);
					ES.WriteVar(Char, "FACCIONES", "MatadosIngreso", 0);
					ES.WriteVar(Char, "FACCIONES", "NextRecompensa", 0);

					cantPenas = vb6.val(ES.GetVar(Declaraciones.CharPath + UserName + ".chr", "PENAS", "Cant"));
					ES.WriteVar(Declaraciones.CharPath + UserName + ".chr", "PENAS", "Cant", cantPenas + 1);
					ES.WriteVar(Declaraciones.CharPath + UserName + ".chr", "PENAS", "P" + cantPenas + 1,
							vb6.LCase(Declaraciones.UserList[UserIndex].Name)
									+ ": Personaje reincorporado a la facción. " + Date + " " + vb6.time());
				} else {
					WriteConsoleMsg(UserIndex, "El personaje " + UserName + " no existe.", FontTypeNames.FONTTYPE_INFO);
				}
			}
		}

		/*
		 * 'If we got here then packet is complete, copy data back to original
		 * queue
		 */
		Declaraciones.UserList[UserIndex].incomingData.CopyBuffer(buffer);

		/* FIXME: ErrHandler : */
		int ERROR;
		/* ERROR = Err . Number */
		/* FIXME: ON ERROR GOTO 0 */

		/* 'Destroy auxiliar buffer */
		buffer = null;

		if (ERROR != 0) {
			throw new RuntimeException("Err . Raise ERROR");
		}
	}

	/* '' */
	/* ' Handle the "RemoveCharFromGuild" message */
	/* ' */
	/* ' @param userIndex The index of the user sending the message */

	static void HandleRemoveCharFromGuild(int UserIndex) {
		/* '*************************************************** */
		/* 'Author: Juan Martín Sotuyo Dodero (Maraxus) */
		/* 'Last Modification: 12/26/06 */
		/* ' */
		/* '*************************************************** */
		if (Declaraciones.UserList[UserIndex].incomingData.length < 3) {
			throw new RuntimeException("Err . Raise UserList ( UserIndex ) . incomingData . NotEnoughDataErrCode");
			return;
		}

		/* FIXME: ON ERROR GOTO ErrHandler */
		/*
		 * 'This packet contains strings, make a copy of the data to prevent
		 * losses if it's not complete yet...
		 */
		clsByteQueue buffer;
		buffer = new clsByteQueue();
		buffer.CopyBuffer(Declaraciones.UserList[UserIndex].incomingData);

		/* 'Remove packet ID */
		buffer.ReadByte();

		String UserName;
		int GuildIndex;

		UserName = buffer.ReadASCIIString();

		if ((!Declaraciones.UserList[UserIndex].flags.Privilegios && PlayerType.RoleMaster) != 0
				&& (Declaraciones.UserList[UserIndex].flags.Privilegios && (PlayerType.Admin || PlayerType.Dios))) {
			General.LogGM(Declaraciones.UserList[UserIndex].Name, "/RAJARCLAN " + UserName);

			GuildIndex = modGuilds.m_EcharMiembroDeClan(UserIndex, UserName);

			if (GuildIndex == 0) {
				WriteConsoleMsg(UserIndex, "No pertenece a ningún clan o es fundador.", FontTypeNames.FONTTYPE_INFO);
			} else {
				WriteConsoleMsg(UserIndex, "Expulsado.", FontTypeNames.FONTTYPE_INFO);
				modSendData.SendData(SendTarget.ToGuildMembers, GuildIndex,
						PrepareMessageConsoleMsg(
								UserName + " ha sido expulsado del clan por los administradores del servidor.",
								FontTypeNames.FONTTYPE_GUILD));
			}
		}

		/*
		 * 'If we got here then packet is complete, copy data back to original
		 * queue
		 */
		Declaraciones.UserList[UserIndex].incomingData.CopyBuffer(buffer);

		/* FIXME: ErrHandler : */
		int ERROR;
		/* ERROR = Err . Number */
		/* FIXME: ON ERROR GOTO 0 */

		/* 'Destroy auxiliar buffer */
		buffer = null;

		if (ERROR != 0) {
			throw new RuntimeException("Err . Raise ERROR");
		}
	}

	/* '' */
	/* ' Handle the "RequestCharMail" message */
	/* ' */
	/* ' @param userIndex The index of the user sending the message */

	static void HandleRequestCharMail(int UserIndex) {
		/* '*************************************************** */
		/* 'Author: Juan Martín Sotuyo Dodero (Maraxus) */
		/* 'Last Modification: 12/26/06 */
		/* 'Request user mail */
		/* '*************************************************** */
		if (Declaraciones.UserList[UserIndex].incomingData.length < 3) {
			throw new RuntimeException("Err . Raise UserList ( UserIndex ) . incomingData . NotEnoughDataErrCode");
			return;
		}

		/* FIXME: ON ERROR GOTO ErrHandler */
		/*
		 * 'This packet contains strings, make a copy of the data to prevent
		 * losses if it's not complete yet...
		 */
		clsByteQueue buffer;
		buffer = new clsByteQueue();
		buffer.CopyBuffer(Declaraciones.UserList[UserIndex].incomingData);

		/* 'Remove packet ID */
		buffer.ReadByte();

		String UserName;
		String mail;

		UserName = buffer.ReadASCIIString();

		if ((!Declaraciones.UserList[UserIndex].flags.Privilegios && PlayerType.RoleMaster) != 0
				&& (Declaraciones.UserList[UserIndex].flags.Privilegios && (PlayerType.Admin || PlayerType.Dios))
				|| Declaraciones.UserList[UserIndex].flags.PrivEspecial) {
			if (General.FileExist(Declaraciones.CharPath + UserName + ".chr")) {
				mail = ES.GetVar(Declaraciones.CharPath + UserName + ".chr", "CONTACTO", "email");

				WriteConsoleMsg(UserIndex, "Last email de " + UserName + ":" + mail, FontTypeNames.FONTTYPE_INFO);
			}
		}

		/*
		 * 'If we got here then packet is complete, copy data back to original
		 * queue
		 */
		Declaraciones.UserList[UserIndex].incomingData.CopyBuffer(buffer);

		/* FIXME: ErrHandler : */
		int ERROR;
		/* ERROR = Err . Number */
		/* FIXME: ON ERROR GOTO 0 */

		/* 'Destroy auxiliar buffer */
		buffer = null;

		if (ERROR != 0) {
			throw new RuntimeException("Err . Raise ERROR");
		}
	}

	/* '' */
	/* ' Handle the "SystemMessage" message */
	/* ' */
	/* ' @param userIndex The index of the user sending the message */

	static void HandleSystemMessage(int UserIndex) {
		/* '*************************************************** */
		/* 'Author: Lucas Tavolaro Ortiz (Tavo) */
		/* 'Last Modification: 12/29/06 */
		/* 'Send a message to all the users */
		/* '*************************************************** */
		if (Declaraciones.UserList[UserIndex].incomingData.length < 3) {
			throw new RuntimeException("Err . Raise UserList ( UserIndex ) . incomingData . NotEnoughDataErrCode");
			return;
		}

		/* FIXME: ON ERROR GOTO ErrHandler */
		/*
		 * 'This packet contains strings, make a copy of the data to prevent
		 * losses if it's not complete yet...
		 */
		clsByteQueue buffer;
		buffer = new clsByteQueue();
		buffer.CopyBuffer(Declaraciones.UserList[UserIndex].incomingData);

		/* 'Remove packet ID */
		buffer.ReadByte();

		String message;
		message = buffer.ReadASCIIString();

		if ((!Declaraciones.UserList[UserIndex].flags.Privilegios && PlayerType.RoleMaster) != 0
				&& (Declaraciones.UserList[UserIndex].flags.Privilegios && (PlayerType.Admin || PlayerType.Dios))) {
			General.LogGM(Declaraciones.UserList[UserIndex].Name, "Mensaje de sistema:" + message);

			modSendData.SendData(SendTarget.ToAll, 0, PrepareMessageShowMessageBox(message));
		}

		/*
		 * 'If we got here then packet is complete, copy data back to original
		 * queue
		 */
		Declaraciones.UserList[UserIndex].incomingData.CopyBuffer(buffer);

		/* FIXME: ErrHandler : */
		int ERROR;
		/* ERROR = Err . Number */
		/* FIXME: ON ERROR GOTO 0 */

		/* 'Destroy auxiliar buffer */
		buffer = null;

		if (ERROR != 0) {
			throw new RuntimeException("Err . Raise ERROR");
		}
	}

	/* '' */
	/* ' Handle the "SetMOTD" message */
	/* ' */
	/* ' @param userIndex The index of the user sending the message */

	static void HandleSetMOTD(int UserIndex) {
 /* '*************************************************** */
 /* 'Author: Lucas Tavolaro Ortiz (Tavo) */
 /* 'Last Modification: 03/31/07 */
 /* 'Set the MOTD */
 /* 'Modified by: Juan Martín Sotuyo Dodero (Maraxus) */
 /* '   - Fixed a bug that prevented from properly setting the new number of lines. */
 /* '   - Fixed a bug that caused the player to be kicked. */
 /* '*************************************************** */
  if (Declaraciones.UserList[UserIndex].incomingData.length<3) {
  throw new RuntimeException("Err . Raise UserList ( UserIndex ) . incomingData . NotEnoughDataErrCode");
  return;
 }
 
 /* FIXME: ON ERROR GOTO ErrHandler */
  /* 'This packet contains strings, make a copy of the data to prevent losses if it's not complete yet... */
  clsByteQueue buffer;
  buffer = new clsByteQueue();
  buffer.CopyBuffer(Declaraciones.UserList[UserIndex].incomingData);
  
  /* 'Remove packet ID */
  buffer.ReadByte();
  
  String newMOTD;
  String[] auxiliaryString;
  int LoopC;
  
  newMOTD = buffer.ReadASCIIString();
  auxiliaryString = vb6.Split(newMOTD, vbCrLf);
  
   if (((!Declaraciones.UserList[UserIndex].flags.Privilegios && PlayerType.RoleMaster) != 0 && (Declaraciones.UserList[UserIndex].flags.Privilegios && (PlayerType.Admin || PlayerType.Dios))) || Declaraciones.UserList[UserIndex].flags.PrivEspecial) {
   
   General.LogGM(Declaraciones.UserList[UserIndex].Name, "Ha fijado un nuevo MOTD");
   
   Admin.MaxLines = vb6.UBound(auxiliaryString[])+1;
   
   Admin.MOTD = new None[0];
   Admin.MOTD = (Admin.MOTD == null) ? new None[1 + Admin.MaxLines] : java.util.Arrays.copyOf(Admin.MOTD, 1 + Admin.MaxLines);
   
   ES.WriteVar(vb6.App.Instance().Path + "\\Dat\\Motd.ini", "INIT", "NumLines", vb6.CStr(Admin.MaxLines));
   
    for (LoopC = (1); LoopC <= (Admin.MaxLines); LoopC++) {
    ES.WriteVar(vb6.App.Instance().Path + "\\Dat\\Motd.ini", "Motd", "Line" + vb6.CStr(LoopC), auxiliaryString[LoopC-1]);
    
    Admin.MOTD[LoopC].texto = auxiliaryString[LoopC-1];
   }
   
   WriteConsoleMsg(UserIndex, "Se ha cambiado el MOTD con éxito.", FontTypeNames.FONTTYPE_INFO);
  }
  
  /* 'If we got here then packet is complete, copy data back to original queue */
  Declaraciones.UserList[UserIndex].incomingData.CopyBuffer(buffer);
 
 /* FIXME: ErrHandler : */
 int ERROR;
 /* ERROR = Err . Number */
 /* FIXME: ON ERROR GOTO 0 */
 
 /* 'Destroy auxiliar buffer */
 buffer = null;
 
 if (ERROR != 0) {
 throw new RuntimeException("Err . Raise ERROR");
 }
}

	/* '' */
	/* ' Handle the "ChangeMOTD" message */
	/* ' */
	/* ' @param userIndex The index of the user sending the message */

	static void HandleChangeMOTD(int UserIndex) {
 /* '*************************************************** */
 /* 'Author: Juan Martín sotuyo Dodero (Maraxus) */
 /* 'Last Modification: 12/29/06 */
 /* 'Change the MOTD */
 /* '*************************************************** */
  /* 'Remove Packet ID */
  Declaraciones.UserList[UserIndex].incomingData.ReadByte();
  
   if ((Declaraciones.UserList[UserIndex].flags.Privilegios && (PlayerType.RoleMaster || PlayerType.User || PlayerType.Consejero || PlayerType.SemiDios))) {
   return;
  }
  
  String auxiliaryString;
  int LoopC;
  
   for (LoopC = (vb6.LBound(Admin.MOTD[])); LoopC <= (vb6.UBound(Admin.MOTD[])); LoopC++) {
   auxiliaryString = auxiliaryString + Admin.MOTD[LoopC].texto + vbCrLf;
  }
  
   if (vb6.Len(auxiliaryString)>=2) {
    if (vb6.Right(auxiliaryString, 2) == vbCrLf) {
    auxiliaryString = vb6.Left(auxiliaryString, vb6.Len(auxiliaryString)-2);
   }
  }
  
  WriteShowMOTDEditionForm(UserIndex, auxiliaryString);
}

	/* '' */
	/* ' Handle the "Ping" message */
	/* ' */
	/* ' @param userIndex The index of the user sending the message */

	static void HandlePing(int UserIndex) {
		/* '*************************************************** */
		/* 'Author: Lucas Tavolaro Ortiz (Tavo) */
		/* 'Last Modification: 12/24/06 */
		/* 'Show guilds messages */
		/* '*************************************************** */
		/* 'Remove Packet ID */
		Declaraciones.UserList[UserIndex].incomingData.ReadByte();

		WritePong(UserIndex);
	}

	/* '' */
	/* ' Handle the "SetIniVar" message */
	/* ' */
	/* ' @param userIndex The index of the user sending the message */

	static void HandleSetIniVar(int UserIndex) {
		/* '*************************************************** */
		/* 'Author: Brian Chaia (BrianPr) */
		/* 'Last Modification: 01/23/10 (Marco) */
		/* 'Modify server.ini */
		/* '*************************************************** */
		if (Declaraciones.UserList[UserIndex].incomingData.length < 6) {
			throw new RuntimeException("Err . Raise UserList ( UserIndex ) . incomingData . NotEnoughDataErrCode");
			return;
		}

		/* FIXME: ON ERROR GOTO ErrHandler */

		/*
		 * 'This packet contains strings, make a copy of the data to prevent
		 * losses if it's not complete yet...
		 */
		clsByteQueue buffer;
		buffer = new clsByteQueue();

		buffer.CopyBuffer(Declaraciones.UserList[UserIndex].incomingData);

		/* 'Remove packet ID */
		buffer.ReadByte();

		String sLlave;
		String sClave;
		String sValor;

		/* 'Obtengo los parámetros */
		sLlave = buffer.ReadASCIIString();
		sClave = buffer.ReadASCIIString();
		sValor = buffer.ReadASCIIString();

		if (Declaraciones.UserList[UserIndex].flags.Privilegios && (PlayerType.Admin || PlayerType.Dios)) {
			String sTmp;

			/* 'No podemos modificar [INIT]Dioses ni [Dioses]* */
			if ((vb6.UCase(sLlave) == "INIT" && vb6.UCase(sClave) == "DIOSES") || vb6.UCase(sLlave) == "DIOSES") {
				WriteConsoleMsg(UserIndex, "¡No puedes modificar esa información desde aquí!",
						FontTypeNames.FONTTYPE_INFO);
			} else {
				/* 'Obtengo el valor según llave y clave */
				sTmp = ES.GetVar(Declaraciones.IniPath + "Server.ini", sLlave, sClave);

				/* 'Si obtengo un valor escribo en el server.ini */
				if (vb6.LenB(sTmp)) {
					ES.WriteVar(Declaraciones.IniPath + "Server.ini", sLlave, sClave, sValor);
					General.LogGM(Declaraciones.UserList[UserIndex].Name, "Modificó en server.ini (" + sLlave + " "
							+ sClave + ") el valor " + sTmp + " por " + sValor);
					WriteConsoleMsg(UserIndex,
							"Modificó " + sLlave + " " + sClave + " a " + sValor + ". Valor anterior " + sTmp,
							FontTypeNames.FONTTYPE_INFO);
				} else {
					WriteConsoleMsg(UserIndex, "No existe la llave y/o clave", FontTypeNames.FONTTYPE_INFO);
				}
			}
		}

		/*
		 * 'If we got here then packet is complete, copy data back to original
		 * queue
		 */
		Declaraciones.UserList[UserIndex].incomingData.CopyBuffer(buffer);

		/* FIXME: ErrHandler : */
		int ERROR;

		/* ERROR = Err . Number */

		/* FIXME: ON ERROR GOTO 0 */
		/* 'Destroy auxiliar buffer */
		buffer = null;

		if (ERROR != 0) {
			throw new RuntimeException("Err . Raise ERROR");
		}
	}

	/* '' */
	/* ' Handle the "CreatePretorianClan" message */
	/* ' */
	/* ' @param userIndex The index of the user sending the message */

	static void HandleCreatePretorianClan(int UserIndex) {
		/* '*************************************************** */
		/* 'Author: ZaMa */
		/* 'Last Modification: 29/10/2010 */
		/* '*************************************************** */

		/* FIXME: ON ERROR GOTO ErrHandler */

		int Map;
		int X;
		int Y;
		int index;

		/* 'Remove packet ID */
		Declaraciones.UserList[UserIndex].incomingData.ReadByte();

		Map = Declaraciones.UserList[UserIndex].incomingData.ReadInteger();
		X = Declaraciones.UserList[UserIndex].incomingData.ReadByte();
		Y = Declaraciones.UserList[UserIndex].incomingData.ReadByte();

		/* ' User Admin? */
		if (((Declaraciones.UserList[UserIndex].flags.Privilegios && (PlayerType.Admin || PlayerType.Dios)) == 0)
				|| ((Declaraciones.UserList[UserIndex].flags.Privilegios && PlayerType.RoleMaster) != 0)) {
			return;
		}

		/* ' Valid pos? */
		if (!Extra.InMapBounds(Map, X, Y)) {
			WriteConsoleMsg(UserIndex, "Posición inválida.", FontTypeNames.FONTTYPE_INFO);
			return;
		}

		/* ' Choose pretorian clan index */
		if (Map == PraetoriansCoopNPC.MAPA_PRETORIANO) {
			/* ' Default clan */
			index = 1;
		} else {
			/* ' Custom Clan */
			index = 2;
		}

		/* ' Is already active any clan? */
		if (!Declaraciones.ClanPretoriano[index].Active) {

			if (!Declaraciones.ClanPretoriano[index].SpawnClan(Map, X, Y, index)) {
				WriteConsoleMsg(UserIndex, "La posición no es apropiada para crear el clan",
						FontTypeNames.FONTTYPE_INFO);
			}

		} else {
			WriteConsoleMsg(UserIndex, "El clan pretoriano se encuentra activo en el mapa "
					+ Declaraciones.ClanPretoriano[index].ClanMap + ". Utilice /EliminarPretorianos MAPA y reintente.",
					FontTypeNames.FONTTYPE_INFO);
		}

		General.LogGM(Declaraciones.UserList[UserIndex].Name,
				"Utilizó el comando /CREARPRETORIANOS " + Map + " " + X + " " + Y);

		return;

		/* FIXME: ErrHandler : */
		General.LogError("Error en HandleCreatePretorianClan. Error: " + Err.Number + " - " + Err.description);
	}

	/* '' */
	/* ' Handle the "CreatePretorianClan" message */
	/* ' */
	/* ' @param userIndex The index of the user sending the message */

	static void HandleDeletePretorianClan(int UserIndex) {
		/* '*************************************************** */
		/* 'Author: ZaMa */
		/* 'Last Modification: 29/10/2010 */
		/* '*************************************************** */

		/* FIXME: ON ERROR GOTO ErrHandler */

		int Map;
		int index;

		/* 'Remove packet ID */
		Declaraciones.UserList[UserIndex].incomingData.ReadByte();

		Map = Declaraciones.UserList[UserIndex].incomingData.ReadInteger();

		/* ' User Admin? */
		if (((Declaraciones.UserList[UserIndex].flags.Privilegios && (PlayerType.Admin || PlayerType.Dios)) == 0)
				|| ((Declaraciones.UserList[UserIndex].flags.Privilegios && PlayerType.RoleMaster) != 0)) {
			return;
		}

		/* ' Valid map? */
		if (Map < 1 || Map > Declaraciones.NumMaps) {
			WriteConsoleMsg(UserIndex, "Mapa inválido.", FontTypeNames.FONTTYPE_INFO);
			return;
		}

		for (index = (1); index <= (vb6.UBound(Declaraciones.ClanPretoriano)); index++) {

			/* ' Search for the clan to be deleted */
			if (Declaraciones.ClanPretoriano[index].ClanMap == Map) {
				Declaraciones.ClanPretoriano[index].DeleteClan();
				break; /* FIXME: EXIT FOR */
			}

		}

		General.LogGM(Declaraciones.UserList[UserIndex].Name, "Utilizó el comando /ELIMINARPRETORIANOS " + Map);

		return;

		/* FIXME: ErrHandler : */
		General.LogError("Error en HandleDeletePretorianClan. Error: " + Err.Number + " - " + Err.description);
	}

	/* '' */
	/*
	 * ' Writes the "Logged" message to the given user's outgoing data buffer.
	 */
	/* ' */
	/* ' @param UserIndex User to which the message is intended. */
	/*
	 * ' @remarks The data is not actually sent until the buffer is properly
	 * flushed.
	 */

	static void WriteLoggedMessage(int UserIndex) {
		/* '*************************************************** */
		/* 'Author: Juan Martín Sotuyo Dodero (Maraxus) */
		/* 'Last Modification: 05/17/06 */
		/*
		 * 'Writes the "Logged" message to the given user's outgoing data buffer
		 */
		/* '*************************************************** */
		/* FIXME: ON ERROR GOTO ErrHandler */
		Declaraciones.UserList[UserIndex].outgoingData.WriteByte(ServerPacketID.Logged);

		Declaraciones.UserList[UserIndex].outgoingData.WriteByte(Declaraciones.UserList[UserIndex].clase);
		return;

		/* FIXME: ErrHandler : */
		if (Err.Number == Declaraciones.UserList[UserIndex].outgoingData.NotEnoughSpaceErrCode) {
			FlushBuffer(UserIndex);
			/* FIXME: RESUME */
		}
	}

	/* '' */
	/*
	 * ' Writes the "RemoveDialogs" message to the given user's outgoing data
	 * buffer.
	 */
	/* ' */
	/* ' @param UserIndex User to which the message is intended. */
	/*
	 * ' @remarks The data is not actually sent until the buffer is properly
	 * flushed.
	 */

	static void WriteRemoveAllDialogs(int UserIndex) {
		/* '*************************************************** */
		/* 'Author: Juan Martín Sotuyo Dodero (Maraxus) */
		/* 'Last Modification: 05/17/06 */
		/*
		 * 'Writes the "RemoveDialogs" message to the given user's outgoing data
		 * buffer
		 */
		/* '*************************************************** */
		/* FIXME: ON ERROR GOTO ErrHandler */
		Declaraciones.UserList[UserIndex].outgoingData.WriteByte(ServerPacketID.RemoveDialogs);
		return;

		/* FIXME: ErrHandler : */
		if (Err.Number == Declaraciones.UserList[UserIndex].outgoingData.NotEnoughSpaceErrCode) {
			FlushBuffer(UserIndex);
			/* FIXME: RESUME */
		}
	}

	/* '' */
	/*
	 * ' Writes the "RemoveCharDialog" message to the given user's outgoing data
	 * buffer.
	 */
	/* ' */
	/* ' @param UserIndex User to which the message is intended. */
	/* ' @param CharIndex Character whose dialog will be removed. */
	/*
	 * ' @remarks The data is not actually sent until the buffer is properly
	 * flushed.
	 */

	static void WriteRemoveCharDialog(int UserIndex, int CharIndex) {
		/* '*************************************************** */
		/* 'Author: Juan Martín Sotuyo Dodero (Maraxus) */
		/* 'Last Modification: 05/17/06 */
		/*
		 * 'Writes the "RemoveCharDialog" message to the given user's outgoing
		 * data buffer
		 */
		/* '*************************************************** */
		/* FIXME: ON ERROR GOTO ErrHandler */
		Declaraciones.UserList[UserIndex].outgoingData.WriteASCIIStringFixed(PrepareMessageRemoveCharDialog(CharIndex));
		return;

		/* FIXME: ErrHandler : */
		if (Err.Number == Declaraciones.UserList[UserIndex].outgoingData.NotEnoughSpaceErrCode) {
			FlushBuffer(UserIndex);
			/* FIXME: RESUME */
		}
	}

	/* '' */
	/*
	 * ' Writes the "NavigateToggle" message to the given user's outgoing data
	 * buffer.
	 */
	/* ' */
	/* ' @param UserIndex User to which the message is intended. */
	/*
	 * ' @remarks The data is not actually sent until the buffer is properly
	 * flushed.
	 */

	static void WriteNavigateToggle(int UserIndex) {
		/* '*************************************************** */
		/* 'Author: Juan Martín Sotuyo Dodero (Maraxus) */
		/* 'Last Modification: 05/17/06 */
		/*
		 * 'Writes the "NavigateToggle" message to the given user's outgoing
		 * data buffer
		 */
		/* '*************************************************** */
		/* FIXME: ON ERROR GOTO ErrHandler */
		Declaraciones.UserList[UserIndex].outgoingData.WriteByte(ServerPacketID.NavigateToggle);
		return;

		/* FIXME: ErrHandler : */
		if (Err.Number == Declaraciones.UserList[UserIndex].outgoingData.NotEnoughSpaceErrCode) {
			FlushBuffer(UserIndex);
			/* FIXME: RESUME */
		}
	}

	/* '' */
	/*
	 * ' Writes the "Disconnect" message to the given user's outgoing data
	 * buffer.
	 */
	/* ' */
	/* ' @param UserIndex User to which the message is intended. */
	/*
	 * ' @remarks The data is not actually sent until the buffer is properly
	 * flushed.
	 */

	static void WriteDisconnect(int UserIndex) {
		/* '*************************************************** */
		/* 'Author: Juan Martín Sotuyo Dodero (Maraxus) */
		/* 'Last Modification: 05/17/06 */
		/*
		 * 'Writes the "Disconnect" message to the given user's outgoing data
		 * buffer
		 */
		/* '*************************************************** */
		/* FIXME: ON ERROR GOTO ErrHandler */
		Declaraciones.UserList[UserIndex].outgoingData.WriteByte(ServerPacketID.Disconnect);
		return;

		/* FIXME: ErrHandler : */
		if (Err.Number == Declaraciones.UserList[UserIndex].outgoingData.NotEnoughSpaceErrCode) {
			FlushBuffer(UserIndex);
			/* FIXME: RESUME */
		}
	}

	/* '' */
	/*
	 * ' Writes the "UserOfferConfirm" message to the given user's outgoing data
	 * buffer.
	 */
	/* ' */
	/* ' @param UserIndex User to which the message is intended. */
	/*
	 * ' @remarks The data is not actually sent until the buffer is properly
	 * flushed.
	 */

	static void WriteUserOfferConfirm(int UserIndex) {
		/* '*************************************************** */
		/* 'Author: ZaMa */
		/* 'Last Modification: 14/12/2009 */
		/*
		 * 'Writes the "UserOfferConfirm" message to the given user's outgoing
		 * data buffer
		 */
		/* '*************************************************** */
		/* FIXME: ON ERROR GOTO ErrHandler */
		Declaraciones.UserList[UserIndex].outgoingData.WriteByte(ServerPacketID.UserOfferConfirm);
		return;

		/* FIXME: ErrHandler : */
		if (Err.Number == Declaraciones.UserList[UserIndex].outgoingData.NotEnoughSpaceErrCode) {
			FlushBuffer(UserIndex);
			/* FIXME: RESUME */
		}
	}

	/* '' */
	/*
	 * ' Writes the "CommerceEnd" message to the given user's outgoing data
	 * buffer.
	 */
	/* ' */
	/* ' @param UserIndex User to which the message is intended. */
	/*
	 * ' @remarks The data is not actually sent until the buffer is properly
	 * flushed.
	 */

	static void WriteCommerceEnd(int UserIndex) {
		/* '*************************************************** */
		/* 'Author: Juan Martín Sotuyo Dodero (Maraxus) */
		/* 'Last Modification: 05/17/06 */
		/*
		 * 'Writes the "CommerceEnd" message to the given user's outgoing data
		 * buffer
		 */
		/* '*************************************************** */
		/* FIXME: ON ERROR GOTO ErrHandler */
		Declaraciones.UserList[UserIndex].outgoingData.WriteByte(ServerPacketID.CommerceEnd);
		return;

		/* FIXME: ErrHandler : */
		if (Err.Number == Declaraciones.UserList[UserIndex].outgoingData.NotEnoughSpaceErrCode) {
			FlushBuffer(UserIndex);
			/* FIXME: RESUME */
		}
	}

	/* '' */
	/*
	 * ' Writes the "BankEnd" message to the given user's outgoing data buffer.
	 */
	/* ' */
	/* ' @param UserIndex User to which the message is intended. */
	/*
	 * ' @remarks The data is not actually sent until the buffer is properly
	 * flushed.
	 */

	static void WriteBankEnd(int UserIndex) {
		/* '*************************************************** */
		/* 'Author: Juan Martín Sotuyo Dodero (Maraxus) */
		/* 'Last Modification: 05/17/06 */
		/*
		 * 'Writes the "BankEnd" message to the given user's outgoing data
		 * buffer
		 */
		/* '*************************************************** */
		/* FIXME: ON ERROR GOTO ErrHandler */
		Declaraciones.UserList[UserIndex].outgoingData.WriteByte(ServerPacketID.BankEnd);
		return;

		/* FIXME: ErrHandler : */
		if (Err.Number == Declaraciones.UserList[UserIndex].outgoingData.NotEnoughSpaceErrCode) {
			FlushBuffer(UserIndex);
			/* FIXME: RESUME */
		}
	}

	/* '' */
	/*
	 * ' Writes the "CommerceInit" message to the given user's outgoing data
	 * buffer.
	 */
	/* ' */
	/* ' @param UserIndex User to which the message is intended. */
	/*
	 * ' @remarks The data is not actually sent until the buffer is properly
	 * flushed.
	 */

	static void WriteCommerceInit(int UserIndex) {
		/* '*************************************************** */
		/* 'Author: Juan Martín Sotuyo Dodero (Maraxus) */
		/* 'Last Modification: 05/17/06 */
		/*
		 * 'Writes the "CommerceInit" message to the given user's outgoing data
		 * buffer
		 */
		/* '*************************************************** */
		/* FIXME: ON ERROR GOTO ErrHandler */
		Declaraciones.UserList[UserIndex].outgoingData.WriteByte(ServerPacketID.CommerceInit);
		return;

		/* FIXME: ErrHandler : */
		if (Err.Number == Declaraciones.UserList[UserIndex].outgoingData.NotEnoughSpaceErrCode) {
			FlushBuffer(UserIndex);
			/* FIXME: RESUME */
		}
	}

	/* '' */
	/*
	 * ' Writes the "BankInit" message to the given user's outgoing data buffer.
	 */
	/* ' */
	/* ' @param UserIndex User to which the message is intended. */
	/*
	 * ' @remarks The data is not actually sent until the buffer is properly
	 * flushed.
	 */

	static void WriteBankInit(int UserIndex) {
		/* '*************************************************** */
		/* 'Author: Juan Martín Sotuyo Dodero (Maraxus) */
		/* 'Last Modification: 05/17/06 */
		/*
		 * 'Writes the "BankInit" message to the given user's outgoing data
		 * buffer
		 */
		/* '*************************************************** */
		/* FIXME: ON ERROR GOTO ErrHandler */
		Declaraciones.UserList[UserIndex].outgoingData.WriteByte(ServerPacketID.BankInit);
		Declaraciones.UserList[UserIndex].outgoingData.WriteLong(Declaraciones.UserList[UserIndex].Stats.Banco);
		return;

		/* FIXME: ErrHandler : */
		if (Err.Number == Declaraciones.UserList[UserIndex].outgoingData.NotEnoughSpaceErrCode) {
			FlushBuffer(UserIndex);
			/* FIXME: RESUME */
		}
	}

	/* '' */
	/*
	 * ' Writes the "UserCommerceInit" message to the given user's outgoing data
	 * buffer.
	 */
	/* ' */
	/* ' @param UserIndex User to which the message is intended. */
	/*
	 * ' @remarks The data is not actually sent until the buffer is properly
	 * flushed.
	 */

	static void WriteUserCommerceInit(int UserIndex) {
		/* '*************************************************** */
		/* 'Author: Juan Martín Sotuyo Dodero (Maraxus) */
		/* 'Last Modification: 05/17/06 */
		/*
		 * 'Writes the "UserCommerceInit" message to the given user's outgoing
		 * data buffer
		 */
		/* '*************************************************** */
		/* FIXME: ON ERROR GOTO ErrHandler */
		Declaraciones.UserList[UserIndex].outgoingData.WriteByte(ServerPacketID.UserCommerceInit);
		Declaraciones.UserList[UserIndex].outgoingData
				.WriteASCIIString(Declaraciones.UserList[UserIndex].ComUsu.DestNick);
		return;

		/* FIXME: ErrHandler : */
		if (Err.Number == Declaraciones.UserList[UserIndex].outgoingData.NotEnoughSpaceErrCode) {
			FlushBuffer(UserIndex);
			/* FIXME: RESUME */
		}
	}

	/* '' */
	/*
	 * ' Writes the "UserCommerceEnd" message to the given user's outgoing data
	 * buffer.
	 */
	/* ' */
	/* ' @param UserIndex User to which the message is intended. */
	/*
	 * ' @remarks The data is not actually sent until the buffer is properly
	 * flushed.
	 */

	static void WriteUserCommerceEnd(int UserIndex) {
		/* '*************************************************** */
		/* 'Author: Juan Martín Sotuyo Dodero (Maraxus) */
		/* 'Last Modification: 05/17/06 */
		/*
		 * 'Writes the "UserCommerceEnd" message to the given user's outgoing
		 * data buffer
		 */
		/* '*************************************************** */
		/* FIXME: ON ERROR GOTO ErrHandler */
		Declaraciones.UserList[UserIndex].outgoingData.WriteByte(ServerPacketID.UserCommerceEnd);
		return;

		/* FIXME: ErrHandler : */
		if (Err.Number == Declaraciones.UserList[UserIndex].outgoingData.NotEnoughSpaceErrCode) {
			FlushBuffer(UserIndex);
			/* FIXME: RESUME */
		}
	}

	/* '' */
	/*
	 * ' Writes the "ShowBlacksmithForm" message to the given user's outgoing
	 * data buffer.
	 */
	/* ' */
	/* ' @param UserIndex User to which the message is intended. */
	/*
	 * ' @remarks The data is not actually sent until the buffer is properly
	 * flushed.
	 */

	static void WriteShowBlacksmithForm(int UserIndex) {
		/* '*************************************************** */
		/* 'Author: Juan Martín Sotuyo Dodero (Maraxus) */
		/* 'Last Modification: 05/17/06 */
		/*
		 * 'Writes the "ShowBlacksmithForm" message to the given user's outgoing
		 * data buffer
		 */
		/* '*************************************************** */
		/* FIXME: ON ERROR GOTO ErrHandler */
		Declaraciones.UserList[UserIndex].outgoingData.WriteByte(ServerPacketID.ShowBlacksmithForm);
		return;

		/* FIXME: ErrHandler : */
		if (Err.Number == Declaraciones.UserList[UserIndex].outgoingData.NotEnoughSpaceErrCode) {
			FlushBuffer(UserIndex);
			/* FIXME: RESUME */
		}
	}

	/* '' */
	/*
	 * ' Writes the "ShowCarpenterForm" message to the given user's outgoing
	 * data buffer.
	 */
	/* ' */
	/* ' @param UserIndex User to which the message is intended. */
	/*
	 * ' @remarks The data is not actually sent until the buffer is properly
	 * flushed.
	 */

	static void WriteShowCarpenterForm(int UserIndex) {
		/* '*************************************************** */
		/* 'Author: Juan Martín Sotuyo Dodero (Maraxus) */
		/* 'Last Modification: 05/17/06 */
		/*
		 * 'Writes the "ShowCarpenterForm" message to the given user's outgoing
		 * data buffer
		 */
		/* '*************************************************** */
		/* FIXME: ON ERROR GOTO ErrHandler */
		Declaraciones.UserList[UserIndex].outgoingData.WriteByte(ServerPacketID.ShowCarpenterForm);
		return;

		/* FIXME: ErrHandler : */
		if (Err.Number == Declaraciones.UserList[UserIndex].outgoingData.NotEnoughSpaceErrCode) {
			FlushBuffer(UserIndex);
			/* FIXME: RESUME */
		}
	}

	/* '' */
	/*
	 * ' Writes the "UpdateSta" message to the given user's outgoing data
	 * buffer.
	 */
	/* ' */
	/* ' @param UserIndex User to which the message is intended. */
	/*
	 * ' @remarks The data is not actually sent until the buffer is properly
	 * flushed.
	 */

	static void WriteUpdateSta(int UserIndex) {
		/* '*************************************************** */
		/* 'Author: Juan Martín Sotuyo Dodero (Maraxus) */
		/* 'Last Modification: 05/17/06 */
		/*
		 * 'Writes the "UpdateMana" message to the given user's outgoing data
		 * buffer
		 */
		/* '*************************************************** */
		/* FIXME: ON ERROR GOTO ErrHandler */
		Declaraciones.UserList[UserIndex].outgoingData.WriteByte(ServerPacketID.UpdateSta);
		Declaraciones.UserList[UserIndex].outgoingData.WriteInteger(Declaraciones.UserList[UserIndex].Stats.MinSta);
		return;

		/* FIXME: ErrHandler : */
		if (Err.Number == Declaraciones.UserList[UserIndex].outgoingData.NotEnoughSpaceErrCode) {
			FlushBuffer(UserIndex);
			/* FIXME: RESUME */
		}
	}

	/* '' */
	/*
	 * ' Writes the "UpdateMana" message to the given user's outgoing data
	 * buffer.
	 */
	/* ' */
	/* ' @param UserIndex User to which the message is intended. */
	/*
	 * ' @remarks The data is not actually sent until the buffer is properly
	 * flushed.
	 */

	static void WriteUpdateMana(int UserIndex) {
		/* '*************************************************** */
		/* 'Author: Juan Martín Sotuyo Dodero (Maraxus) */
		/* 'Last Modification: 05/17/06 */
		/*
		 * 'Writes the "UpdateMana" message to the given user's outgoing data
		 * buffer
		 */
		/* '*************************************************** */
		/* FIXME: ON ERROR GOTO ErrHandler */
		Declaraciones.UserList[UserIndex].outgoingData.WriteByte(ServerPacketID.UpdateMana);
		Declaraciones.UserList[UserIndex].outgoingData.WriteInteger(Declaraciones.UserList[UserIndex].Stats.MinMAN);
		return;

		/* FIXME: ErrHandler : */
		if (Err.Number == Declaraciones.UserList[UserIndex].outgoingData.NotEnoughSpaceErrCode) {
			FlushBuffer(UserIndex);
			/* FIXME: RESUME */
		}
	}

	/* '' */
	/*
	 * ' Writes the "UpdateHP" message to the given user's outgoing data buffer.
	 */
	/* ' */
	/* ' @param UserIndex User to which the message is intended. */
	/*
	 * ' @remarks The data is not actually sent until the buffer is properly
	 * flushed.
	 */

	static void WriteUpdateHP(int UserIndex) {
		/* '*************************************************** */
		/* 'Author: Juan Martín Sotuyo Dodero (Maraxus) */
		/* 'Last Modification: 05/17/06 */
		/*
		 * 'Writes the "UpdateMana" message to the given user's outgoing data
		 * buffer
		 */
		/* '*************************************************** */
		/* FIXME: ON ERROR GOTO ErrHandler */
		Declaraciones.UserList[UserIndex].outgoingData.WriteByte(ServerPacketID.UpdateHP);
		Declaraciones.UserList[UserIndex].outgoingData.WriteInteger(Declaraciones.UserList[UserIndex].Stats.MinHp);
		return;

		/* FIXME: ErrHandler : */
		if (Err.Number == Declaraciones.UserList[UserIndex].outgoingData.NotEnoughSpaceErrCode) {
			FlushBuffer(UserIndex);
			/* FIXME: RESUME */
		}
	}

	/* '' */
	/*
	 * ' Writes the "UpdateGold" message to the given user's outgoing data
	 * buffer.
	 */
	/* ' */
	/* ' @param UserIndex User to which the message is intended. */
	/*
	 * ' @remarks The data is not actually sent until the buffer is properly
	 * flushed.
	 */

	static void WriteUpdateGold(int UserIndex) {
		/* '*************************************************** */
		/* 'Author: Juan Martín Sotuyo Dodero (Maraxus) */
		/* 'Last Modification: 05/17/06 */
		/*
		 * 'Writes the "UpdateGold" message to the given user's outgoing data
		 * buffer
		 */
		/* '*************************************************** */
		/* FIXME: ON ERROR GOTO ErrHandler */
		Declaraciones.UserList[UserIndex].outgoingData.WriteByte(ServerPacketID.UpdateGold);
		Declaraciones.UserList[UserIndex].outgoingData.WriteLong(Declaraciones.UserList[UserIndex].Stats.GLD);
		return;

		/* FIXME: ErrHandler : */
		if (Err.Number == Declaraciones.UserList[UserIndex].outgoingData.NotEnoughSpaceErrCode) {
			FlushBuffer(UserIndex);
			/* FIXME: RESUME */
		}
	}

	/* '' */
	/*
	 * ' Writes the "UpdateBankGold" message to the given user's outgoing data
	 * buffer.
	 */
	/* ' */
	/* ' @param UserIndex User to which the message is intended. */
	/*
	 * ' @remarks The data is not actually sent until the buffer is properly
	 * flushed.
	 */

	static void WriteUpdateBankGold(int UserIndex) {
		/* '*************************************************** */
		/* 'Author: ZaMa */
		/* 'Last Modification: 14/12/2009 */
		/*
		 * 'Writes the "UpdateBankGold" message to the given user's outgoing
		 * data buffer
		 */
		/* '*************************************************** */
		/* FIXME: ON ERROR GOTO ErrHandler */
		Declaraciones.UserList[UserIndex].outgoingData.WriteByte(ServerPacketID.UpdateBankGold);
		Declaraciones.UserList[UserIndex].outgoingData.WriteLong(Declaraciones.UserList[UserIndex].Stats.Banco);
		return;

		/* FIXME: ErrHandler : */
		if (Err.Number == Declaraciones.UserList[UserIndex].outgoingData.NotEnoughSpaceErrCode) {
			FlushBuffer(UserIndex);
			/* FIXME: RESUME */
		}
	}

	/* '' */
	/*
	 * ' Writes the "UpdateExp" message to the given user's outgoing data
	 * buffer.
	 */
	/* ' */
	/* ' @param UserIndex User to which the message is intended. */
	/*
	 * ' @remarks The data is not actually sent until the buffer is properly
	 * flushed.
	 */

	static void WriteUpdateExp(int UserIndex) {
		/* '*************************************************** */
		/* 'Author: Juan Martín Sotuyo Dodero (Maraxus) */
		/* 'Last Modification: 05/17/06 */
		/*
		 * 'Writes the "UpdateExp" message to the given user's outgoing data
		 * buffer
		 */
		/* '*************************************************** */
		/* FIXME: ON ERROR GOTO ErrHandler */
		Declaraciones.UserList[UserIndex].outgoingData.WriteByte(ServerPacketID.UpdateExp);
		Declaraciones.UserList[UserIndex].outgoingData.WriteLong(Declaraciones.UserList[UserIndex].Stats.Exp);
		return;

		/* FIXME: ErrHandler : */
		if (Err.Number == Declaraciones.UserList[UserIndex].outgoingData.NotEnoughSpaceErrCode) {
			FlushBuffer(UserIndex);
			/* FIXME: RESUME */
		}
	}

	/* '' */
	/*
	 * ' Writes the "UpdateStrenghtAndDexterity" message to the given user's
	 * outgoing data buffer.
	 */
	/* ' */
	/* ' @param UserIndex User to which the message is intended. */
	/*
	 * ' @remarks The data is not actually sent until the buffer is properly
	 * flushed.
	 */

	static void WriteUpdateStrenghtAndDexterity(int UserIndex) {
		/* '*************************************************** */
		/* 'Author: Budi */
		/* 'Last Modification: 11/26/09 */
		/*
		 * 'Writes the "UpdateStrenghtAndDexterity" message to the given user's
		 * outgoing data buffer
		 */
		/* '*************************************************** */
		/* FIXME: ON ERROR GOTO ErrHandler */
		Declaraciones.UserList[UserIndex].outgoingData.WriteByte(ServerPacketID.UpdateStrenghtAndDexterity);
		Declaraciones.UserList[UserIndex].outgoingData
				.WriteByte(Declaraciones.UserList[UserIndex].Stats.UserAtributos[eAtributos.Fuerza]);
		Declaraciones.UserList[UserIndex].outgoingData
				.WriteByte(Declaraciones.UserList[UserIndex].Stats.UserAtributos[eAtributos.Agilidad]);
		return;

		/* FIXME: ErrHandler : */
		if (Err.Number == Declaraciones.UserList[UserIndex].outgoingData.NotEnoughSpaceErrCode) {
			FlushBuffer(UserIndex);
			/* FIXME: RESUME */
		}
	}

	/*
	 * ' Writes the "UpdateStrenghtAndDexterity" message to the given user's
	 * outgoing data buffer.
	 */
	/* ' */
	/* ' @param UserIndex User to which the message is intended. */
	/*
	 * ' @remarks The data is not actually sent until the buffer is properly
	 * flushed.
	 */

	static void WriteUpdateDexterity(int UserIndex) {
		/* '*************************************************** */
		/* 'Author: Budi */
		/* 'Last Modification: 11/26/09 */
		/*
		 * 'Writes the "UpdateStrenghtAndDexterity" message to the given user's
		 * outgoing data buffer
		 */
		/* '*************************************************** */
		/* FIXME: ON ERROR GOTO ErrHandler */
		Declaraciones.UserList[UserIndex].outgoingData.WriteByte(ServerPacketID.UpdateDexterity);
		Declaraciones.UserList[UserIndex].outgoingData
				.WriteByte(Declaraciones.UserList[UserIndex].Stats.UserAtributos[eAtributos.Agilidad]);
		return;

		/* FIXME: ErrHandler : */
		if (Err.Number == Declaraciones.UserList[UserIndex].outgoingData.NotEnoughSpaceErrCode) {
			FlushBuffer(UserIndex);
			/* FIXME: RESUME */
		}
	}

	/*
	 * ' Writes the "UpdateStrenghtAndDexterity" message to the given user's
	 * outgoing data buffer.
	 */
	/* ' */
	/* ' @param UserIndex User to which the message is intended. */
	/*
	 * ' @remarks The data is not actually sent until the buffer is properly
	 * flushed.
	 */

	static void WriteUpdateStrenght(int UserIndex) {
		/* '*************************************************** */
		/* 'Author: Budi */
		/* 'Last Modification: 11/26/09 */
		/*
		 * 'Writes the "UpdateStrenghtAndDexterity" message to the given user's
		 * outgoing data buffer
		 */
		/* '*************************************************** */
		/* FIXME: ON ERROR GOTO ErrHandler */
		Declaraciones.UserList[UserIndex].outgoingData.WriteByte(ServerPacketID.UpdateStrenght);
		Declaraciones.UserList[UserIndex].outgoingData
				.WriteByte(Declaraciones.UserList[UserIndex].Stats.UserAtributos[eAtributos.Fuerza]);
		return;

		/* FIXME: ErrHandler : */
		if (Err.Number == Declaraciones.UserList[UserIndex].outgoingData.NotEnoughSpaceErrCode) {
			FlushBuffer(UserIndex);
			/* FIXME: RESUME */
		}
	}

	/* '' */
	/*
	 * ' Writes the "ChangeMap" message to the given user's outgoing data
	 * buffer.
	 */
	/* ' */
	/* ' @param UserIndex User to which the message is intended. */
	/* ' @param map The new map to load. */
	/*
	 * ' @param version The version of the map in the server to check if client
	 * is properly updated.
	 */
	/*
	 * ' @remarks The data is not actually sent until the buffer is properly
	 * flushed.
	 */

	static void WriteChangeMap(int UserIndex, int Map, int version) {
		/* '*************************************************** */
		/* 'Author: Juan Martín Sotuyo Dodero (Maraxus) */
		/* 'Last Modification: 05/17/06 */
		/*
		 * 'Writes the "ChangeMap" message to the given user's outgoing data
		 * buffer
		 */
		/* '*************************************************** */
		/* FIXME: ON ERROR GOTO ErrHandler */
		Declaraciones.UserList[UserIndex].outgoingData.WriteByte(ServerPacketID.ChangeMap);
		Declaraciones.UserList[UserIndex].outgoingData.WriteInteger(Map);
		Declaraciones.UserList[UserIndex].outgoingData.WriteInteger(version);
		return;

		/* FIXME: ErrHandler : */
		if (Err.Number == Declaraciones.UserList[UserIndex].outgoingData.NotEnoughSpaceErrCode) {
			FlushBuffer(UserIndex);
			/* FIXME: RESUME */
		}
	}

	/* '' */
	/*
	 * ' Writes the "PosUpdate" message to the given user's outgoing data
	 * buffer.
	 */
	/* ' */
	/* ' @param UserIndex User to which the message is intended. */
	/*
	 * ' @remarks The data is not actually sent until the buffer is properly
	 * flushed.
	 */

	static void WritePosUpdate(int UserIndex) {
		/* '*************************************************** */
		/* 'Author: Juan Martín Sotuyo Dodero (Maraxus) */
		/* 'Last Modification: 05/17/06 */
		/*
		 * 'Writes the "PosUpdate" message to the given user's outgoing data
		 * buffer
		 */
		/* '*************************************************** */
		/* FIXME: ON ERROR GOTO ErrHandler */
		Declaraciones.UserList[UserIndex].outgoingData.WriteByte(ServerPacketID.PosUpdate);
		Declaraciones.UserList[UserIndex].outgoingData.WriteByte(Declaraciones.UserList[UserIndex].Pos.X);
		Declaraciones.UserList[UserIndex].outgoingData.WriteByte(Declaraciones.UserList[UserIndex].Pos.Y);
		return;

		/* FIXME: ErrHandler : */
		if (Err.Number == Declaraciones.UserList[UserIndex].outgoingData.NotEnoughSpaceErrCode) {
			FlushBuffer(UserIndex);
			/* FIXME: RESUME */
		}
	}

	/* '' */
	/*
	 * ' Writes the "ChatOverHead" message to the given user's outgoing data
	 * buffer.
	 */
	/* ' */
	/* ' @param UserIndex User to which the message is intended. */
	/* ' @param Chat Text to be displayed over the char's head. */
	/*
	 * ' @param CharIndex The character uppon which the chat will be displayed.
	 */
	/* ' @param Color The color to be used when displaying the chat. */
	/*
	 * ' @remarks The data is not actually sent until the buffer is properly
	 * flushed.
	 */

	static void WriteChatOverHead(int UserIndex, String Chat, int CharIndex, int color) {
		/* '*************************************************** */
		/* 'Author: Juan Martín Sotuyo Dodero (Maraxus) */
		/* 'Last Modification: 05/17/06 */
		/*
		 * 'Writes the "ChatOverHead" message to the given user's outgoing data
		 * buffer
		 */
		/* '*************************************************** */
		/* FIXME: ON ERROR GOTO ErrHandler */
		Declaraciones.UserList[UserIndex].outgoingData
				.WriteASCIIStringFixed(PrepareMessageChatOverHead(Chat, CharIndex, color));
		return;

		/* FIXME: ErrHandler : */
		if (Err.Number == Declaraciones.UserList[UserIndex].outgoingData.NotEnoughSpaceErrCode) {
			FlushBuffer(UserIndex);
			/* FIXME: RESUME */
		}
	}

	/* '' */
	/*
	 * ' Writes the "ConsoleMsg" message to the given user's outgoing data
	 * buffer.
	 */
	/* ' */
	/* ' @param UserIndex User to which the message is intended. */
	/* ' @param Chat Text to be displayed over the char's head. */
	/* ' @param FontIndex Index of the FONTTYPE structure to use. */
	/*
	 * ' @remarks The data is not actually sent until the buffer is properly
	 * flushed.
	 */

	static void WriteConsoleMsg(int UserIndex, String Chat, FontTypeNames FontIndex) {
		/* '*************************************************** */
		/* 'Author: Juan Martín Sotuyo Dodero (Maraxus) */
		/* 'Last Modification: 05/17/06 */
		/*
		 * 'Writes the "ConsoleMsg" message to the given user's outgoing data
		 * buffer
		 */
		/* '*************************************************** */
		/* FIXME: ON ERROR GOTO ErrHandler */
		Declaraciones.UserList[UserIndex].outgoingData.WriteASCIIStringFixed(PrepareMessageConsoleMsg(Chat, FontIndex));
		return;

		/* FIXME: ErrHandler : */
		if (Err.Number == Declaraciones.UserList[UserIndex].outgoingData.NotEnoughSpaceErrCode) {
			FlushBuffer(UserIndex);
			/* FIXME: RESUME */
		}
	}

	static void WriteCommerceChat(int UserIndex, String Chat, FontTypeNames FontIndex) {
		/* '*************************************************** */
		/* 'Author: ZaMa */
		/* 'Last Modification: 05/17/06 */
		/*
		 * 'Writes the "ConsoleMsg" message to the given user's outgoing data
		 * buffer
		 */
		/* '*************************************************** */
		/* FIXME: ON ERROR GOTO ErrHandler */
		Declaraciones.UserList[UserIndex].outgoingData
				.WriteASCIIStringFixed(PrepareCommerceConsoleMsg(Chat, FontIndex));
		return;

		/* FIXME: ErrHandler : */
		if (Err.Number == Declaraciones.UserList[UserIndex].outgoingData.NotEnoughSpaceErrCode) {
			FlushBuffer(UserIndex);
			/* FIXME: RESUME */
		}
	}

	/* '' */
	/*
	 * ' Writes the "GuildChat" message to the given user's outgoing data
	 * buffer.
	 */
	/* ' */
	/* ' @param UserIndex User to which the message is intended. */
	/* ' @param Chat Text to be displayed over the char's head. */
	/*
	 * ' @remarks The data is not actually sent until the buffer is properly
	 * flushed.
	 */

	static void WriteGuildChat(int UserIndex, String Chat) {
		/* '*************************************************** */
		/* 'Author: Juan Martín Sotuyo Dodero (Maraxus) */
		/* 'Last Modification: 05/17/06 */
		/*
		 * 'Writes the "GuildChat" message to the given user's outgoing data
		 * buffer
		 */
		/* '*************************************************** */
		/* FIXME: ON ERROR GOTO ErrHandler */
		Declaraciones.UserList[UserIndex].outgoingData.WriteASCIIStringFixed(PrepareMessageGuildChat(Chat));
		return;

		/* FIXME: ErrHandler : */
		if (Err.Number == Declaraciones.UserList[UserIndex].outgoingData.NotEnoughSpaceErrCode) {
			FlushBuffer(UserIndex);
			/* FIXME: RESUME */
		}
	}

	/* '' */
	/*
	 * ' Writes the "ShowMessageBox" message to the given user's outgoing data
	 * buffer.
	 */
	/* ' */
	/* ' @param UserIndex User to which the message is intended. */
	/* ' @param Message Text to be displayed in the message box. */
	/*
	 * ' @remarks The data is not actually sent until the buffer is properly
	 * flushed.
	 */

	static void WriteShowMessageBox(int UserIndex, String message) {
		/* '*************************************************** */
		/* 'Author: Juan Martín Sotuyo Dodero (Maraxus) */
		/* 'Last Modification: 05/17/06 */
		/*
		 * 'Writes the "ShowMessageBox" message to the given user's outgoing
		 * data buffer
		 */
		/* '*************************************************** */
		/* FIXME: ON ERROR GOTO ErrHandler */
		Declaraciones.UserList[UserIndex].outgoingData.WriteByte(ServerPacketID.ShowMessageBox);
		Declaraciones.UserList[UserIndex].outgoingData.WriteASCIIString(message);
		return;

		/* FIXME: ErrHandler : */
		if (Err.Number == Declaraciones.UserList[UserIndex].outgoingData.NotEnoughSpaceErrCode) {
			FlushBuffer(UserIndex);
			/* FIXME: RESUME */
		}
	}

	/* '' */
	/*
	 * ' Writes the "UserIndexInServer" message to the given user's outgoing
	 * data buffer.
	 */
	/* ' */
	/* ' @param UserIndex User to which the message is intended. */
	/*
	 * ' @remarks The data is not actually sent until the buffer is properly
	 * flushed.
	 */

	static void WriteUserIndexInServer(int UserIndex) {
		/* '*************************************************** */
		/* 'Author: Juan Martín Sotuyo Dodero (Maraxus) */
		/* 'Last Modification: 05/17/06 */
		/*
		 * 'Writes the "UserIndexInServer" message to the given user's outgoing
		 * data buffer
		 */
		/* '*************************************************** */
		/* FIXME: ON ERROR GOTO ErrHandler */
		Declaraciones.UserList[UserIndex].outgoingData.WriteByte(ServerPacketID.UserIndexInServer);
		Declaraciones.UserList[UserIndex].outgoingData.WriteInteger(UserIndex);
		return;

		/* FIXME: ErrHandler : */
		if (Err.Number == Declaraciones.UserList[UserIndex].outgoingData.NotEnoughSpaceErrCode) {
			FlushBuffer(UserIndex);
			/* FIXME: RESUME */
		}
	}

	/* '' */
	/*
	 * ' Writes the "UserCharIndexInServer" message to the given user's outgoing
	 * data buffer.
	 */
	/* ' */
	/* ' @param UserIndex User to which the message is intended. */
	/*
	 * ' @remarks The data is not actually sent until the buffer is properly
	 * flushed.
	 */

	static void WriteUserCharIndexInServer(int UserIndex) {
		/* '*************************************************** */
		/* 'Author: Juan Martín Sotuyo Dodero (Maraxus) */
		/* 'Last Modification: 05/17/06 */
		/*
		 * 'Writes the "UserIndexInServer" message to the given user's outgoing
		 * data buffer
		 */
		/* '*************************************************** */
		/* FIXME: ON ERROR GOTO ErrHandler */
		Declaraciones.UserList[UserIndex].outgoingData.WriteByte(ServerPacketID.UserCharIndexInServer);
		Declaraciones.UserList[UserIndex].outgoingData.WriteInteger(Declaraciones.UserList[UserIndex].Char.CharIndex);
		return;

		/* FIXME: ErrHandler : */
		if (Err.Number == Declaraciones.UserList[UserIndex].outgoingData.NotEnoughSpaceErrCode) {
			FlushBuffer(UserIndex);
			/* FIXME: RESUME */
		}
	}

	/* '' */
	/*
	 * ' Writes the "CharacterCreate" message to the given user's outgoing data
	 * buffer.
	 */
	/* ' */
	/* ' @param UserIndex User to which the message is intended. */
	/* ' @param body Body index of the new character. */
	/* ' @param head Head index of the new character. */
	/* ' @param heading Heading in which the new character is looking. */
	/* ' @param CharIndex The index of the new character. */
	/* ' @param X X coord of the new character's position. */
	/* ' @param Y Y coord of the new character's position. */
	/* ' @param weapon Weapon index of the new character. */
	/* ' @param shield Shield index of the new character. */
	/* ' @param FX FX index to be displayed over the new character. */
	/* ' @param FXLoops Number of times the FX should be rendered. */
	/* ' @param helmet Helmet index of the new character. */
	/* ' @param name Name of the new character. */
	/* ' @param criminal Determines if the character is a criminal or not. */
	/*
	 * ' @param privileges Sets if the character is a normal one or any kind of
	 * administrative character.
	 */
	/*
	 * ' @remarks The data is not actually sent until the buffer is properly
	 * flushed.
	 */

	static void WriteCharacterCreate(int UserIndex, int body, int Head, eHeading heading, int CharIndex, int X, int Y,
			int weapon, int shield, int FX, int FXLoops, int helmet, String Name, int NickColor, int Privileges) {
		/* '*************************************************** */
		/* 'Author: Juan Martín Sotuyo Dodero (Maraxus) */
		/* 'Last Modification: 05/17/06 */
		/*
		 * 'Writes the "CharacterCreate" message to the given user's outgoing
		 * data buffer
		 */
		/* '*************************************************** */
		/* FIXME: ON ERROR GOTO ErrHandler */
		Declaraciones.UserList[UserIndex].outgoingData.WriteASCIIStringFixed(PrepareMessageCharacterCreate(body, Head,
				heading, CharIndex, X, Y, weapon, shield, FX, FXLoops, helmet, Name, NickColor, Privileges));
		return;

		/* FIXME: ErrHandler : */
		if (Err.Number == Declaraciones.UserList[UserIndex].outgoingData.NotEnoughSpaceErrCode) {
			FlushBuffer(UserIndex);
			/* FIXME: RESUME */
		}
	}

	/* '' */
	/*
	 * ' Writes the "CharacterRemove" message to the given user's outgoing data
	 * buffer.
	 */
	/* ' */
	/* ' @param UserIndex User to which the message is intended. */
	/* ' @param CharIndex Character to be removed. */
	/*
	 * ' @remarks The data is not actually sent until the buffer is properly
	 * flushed.
	 */

	static void WriteCharacterRemove(int UserIndex, int CharIndex) {
		/* '*************************************************** */
		/* 'Author: Juan Martín Sotuyo Dodero (Maraxus) */
		/* 'Last Modification: 05/17/06 */
		/*
		 * 'Writes the "CharacterRemove" message to the given user's outgoing
		 * data buffer
		 */
		/* '*************************************************** */
		/* FIXME: ON ERROR GOTO ErrHandler */
		Declaraciones.UserList[UserIndex].outgoingData.WriteASCIIStringFixed(PrepareMessageCharacterRemove(CharIndex));
		return;

		/* FIXME: ErrHandler : */
		if (Err.Number == Declaraciones.UserList[UserIndex].outgoingData.NotEnoughSpaceErrCode) {
			FlushBuffer(UserIndex);
			/* FIXME: RESUME */
		}
	}

	/* '' */
	/*
	 * ' Writes the "CharacterMove" message to the given user's outgoing data
	 * buffer.
	 */
	/* ' */
	/* ' @param UserIndex User to which the message is intended. */
	/* ' @param CharIndex Character which is moving. */
	/* ' @param X X coord of the character's new position. */
	/* ' @param Y Y coord of the character's new position. */
	/*
	 * ' @remarks The data is not actually sent until the buffer is properly
	 * flushed.
	 */

	static void WriteCharacterMove(int UserIndex, int CharIndex, int X, int Y) {
		/* '*************************************************** */
		/* 'Author: Juan Martín Sotuyo Dodero (Maraxus) */
		/* 'Last Modification: 05/17/06 */
		/*
		 * 'Writes the "CharacterMove" message to the given user's outgoing data
		 * buffer
		 */
		/* '*************************************************** */
		/* FIXME: ON ERROR GOTO ErrHandler */
		Declaraciones.UserList[UserIndex].outgoingData
				.WriteASCIIStringFixed(PrepareMessageCharacterMove(CharIndex, X, Y));
		return;

		/* FIXME: ErrHandler : */
		if (Err.Number == Declaraciones.UserList[UserIndex].outgoingData.NotEnoughSpaceErrCode) {
			FlushBuffer(UserIndex);
			/* FIXME: RESUME */
		}
	}

	static void WriteForceCharMove(Object UserIndex, eHeading Direccion) {
		/* '*************************************************** */
		/* 'Author: ZaMa */
		/* 'Last Modification: 26/03/2009 */
		/*
		 * 'Writes the "ForceCharMove" message to the given user's outgoing data
		 * buffer
		 */
		/* '*************************************************** */
		/* FIXME: ON ERROR GOTO ErrHandler */
		Declaraciones.UserList[UserIndex].outgoingData.WriteASCIIStringFixed(PrepareMessageForceCharMove(Direccion));
		return;

		/* FIXME: ErrHandler : */
		if (Err.Number == Declaraciones.UserList[UserIndex].outgoingData.NotEnoughSpaceErrCode) {
			FlushBuffer(UserIndex);
			/* FIXME: RESUME */
		}
	}

	/* '' */
	/*
	 * ' Writes the "CharacterChange" message to the given user's outgoing data
	 * buffer.
	 */
	/* ' */
	/* ' @param UserIndex User to which the message is intended. */
	/* ' @param body Body index of the new character. */
	/* ' @param head Head index of the new character. */
	/* ' @param heading Heading in which the new character is looking. */
	/* ' @param CharIndex The index of the new character. */
	/* ' @param weapon Weapon index of the new character. */
	/* ' @param shield Shield index of the new character. */
	/* ' @param FX FX index to be displayed over the new character. */
	/* ' @param FXLoops Number of times the FX should be rendered. */
	/* ' @param helmet Helmet index of the new character. */
	/*
	 * ' @remarks The data is not actually sent until the buffer is properly
	 * flushed.
	 */

	static void WriteCharacterChange(int UserIndex, int body, int Head, eHeading heading, int CharIndex, int weapon,
			int shield, int FX, int FXLoops, int helmet) {
		/* '*************************************************** */
		/* 'Author: Juan Martín Sotuyo Dodero (Maraxus) */
		/* 'Last Modification: 05/17/06 */
		/*
		 * 'Writes the "CharacterChange" message to the given user's outgoing
		 * data buffer
		 */
		/* '*************************************************** */
		/* FIXME: ON ERROR GOTO ErrHandler */
		Declaraciones.UserList[UserIndex].outgoingData.WriteASCIIStringFixed(
				PrepareMessageCharacterChange(body, Head, heading, CharIndex, weapon, shield, FX, FXLoops, helmet));
		return;

		/* FIXME: ErrHandler : */
		if (Err.Number == Declaraciones.UserList[UserIndex].outgoingData.NotEnoughSpaceErrCode) {
			FlushBuffer(UserIndex);
			/* FIXME: RESUME */
		}
	}

	/* '' */
	/*
	 * ' Writes the "ObjectCreate" message to the given user's outgoing data
	 * buffer.
	 */
	/* ' */
	/* ' @param UserIndex User to which the message is intended. */
	/* ' @param GrhIndex Grh of the object. */
	/* ' @param X X coord of the character's new position. */
	/* ' @param Y Y coord of the character's new position. */
	/*
	 * ' @remarks The data is not actually sent until the buffer is properly
	 * flushed.
	 */

	static void WriteObjectCreate(int UserIndex, int GrhIndex, int X, int Y) {
		/* '*************************************************** */
		/* 'Author: Juan Martín Sotuyo Dodero (Maraxus) */
		/* 'Last Modification: 05/17/06 */
		/*
		 * 'Writes the "ObjectCreate" message to the given user's outgoing data
		 * buffer
		 */
		/* '*************************************************** */
		/* FIXME: ON ERROR GOTO ErrHandler */
		Declaraciones.UserList[UserIndex].outgoingData
				.WriteASCIIStringFixed(PrepareMessageObjectCreate(GrhIndex, X, Y));
		return;

		/* FIXME: ErrHandler : */
		if (Err.Number == Declaraciones.UserList[UserIndex].outgoingData.NotEnoughSpaceErrCode) {
			FlushBuffer(UserIndex);
			/* FIXME: RESUME */
		}
	}

	/* '' */
	/*
	 * ' Writes the "ObjectDelete" message to the given user's outgoing data
	 * buffer.
	 */
	/* ' */
	/* ' @param UserIndex User to which the message is intended. */
	/* ' @param X X coord of the character's new position. */
	/* ' @param Y Y coord of the character's new position. */
	/*
	 * ' @remarks The data is not actually sent until the buffer is properly
	 * flushed.
	 */

	static void WriteObjectDelete(int UserIndex, int X, int Y) {
		/* '*************************************************** */
		/* 'Author: Juan Martín Sotuyo Dodero (Maraxus) */
		/* 'Last Modification: 05/17/06 */
		/*
		 * 'Writes the "ObjectDelete" message to the given user's outgoing data
		 * buffer
		 */
		/* '*************************************************** */
		/* FIXME: ON ERROR GOTO ErrHandler */
		Declaraciones.UserList[UserIndex].outgoingData.WriteASCIIStringFixed(PrepareMessageObjectDelete(X, Y));
		return;

		/* FIXME: ErrHandler : */
		if (Err.Number == Declaraciones.UserList[UserIndex].outgoingData.NotEnoughSpaceErrCode) {
			FlushBuffer(UserIndex);
			/* FIXME: RESUME */
		}
	}

	/* '' */
	/*
	 * ' Writes the "BlockPosition" message to the given user's outgoing data
	 * buffer.
	 */
	/* ' */
	/* ' @param UserIndex User to which the message is intended. */
	/* ' @param X X coord of the character's new position. */
	/* ' @param Y Y coord of the character's new position. */
	/* ' @param Blocked True if the position is blocked. */
	/*
	 * ' @remarks The data is not actually sent until the buffer is properly
	 * flushed.
	 */

	static void WriteBlockPosition(int UserIndex, int X, int Y, boolean Blocked) {
		/* '*************************************************** */
		/* 'Author: Juan Martín Sotuyo Dodero (Maraxus) */
		/* 'Last Modification: 05/17/06 */
		/*
		 * 'Writes the "BlockPosition" message to the given user's outgoing data
		 * buffer
		 */
		/* '*************************************************** */
		/* FIXME: ON ERROR GOTO ErrHandler */
		Declaraciones.UserList[UserIndex].outgoingData.WriteByte(ServerPacketID.BlockPosition);
		Declaraciones.UserList[UserIndex].outgoingData.WriteByte(X);
		Declaraciones.UserList[UserIndex].outgoingData.WriteByte(Y);
		Declaraciones.UserList[UserIndex].outgoingData.WriteBoolean(Blocked);
		return;

		/* FIXME: ErrHandler : */
		if (Err.Number == Declaraciones.UserList[UserIndex].outgoingData.NotEnoughSpaceErrCode) {
			FlushBuffer(UserIndex);
			/* FIXME: RESUME */
		}
	}

	/* '' */
	/*
	 * ' Writes the "PlayMidi" message to the given user's outgoing data buffer.
	 */
	/* ' */
	/* ' @param UserIndex User to which the message is intended. */
	/* ' @param midi The midi to be played. */
	/* ' @param loops Number of repets for the midi. */
	/*
	 * ' @remarks The data is not actually sent until the buffer is properly
	 * flushed.
	 */

	static void WritePlayMidi(int UserIndex, int midi) {
		WritePlayMidi(UserIndex, midi, -1);
	}

	static void WritePlayMidi(int UserIndex, int midi, int loops) {
		/* '*************************************************** */
		/* 'Author: Juan Martín Sotuyo Dodero (Maraxus) */
		/* 'Last Modification: 05/17/06 */
		/*
		 * 'Writes the "PlayMidi" message to the given user's outgoing data
		 * buffer
		 */
		/* '*************************************************** */
		/* FIXME: ON ERROR GOTO ErrHandler */
		Declaraciones.UserList[UserIndex].outgoingData.WriteASCIIStringFixed(PrepareMessagePlayMidi(midi, loops));
		return;

		/* FIXME: ErrHandler : */
		if (Err.Number == Declaraciones.UserList[UserIndex].outgoingData.NotEnoughSpaceErrCode) {
			FlushBuffer(UserIndex);
			/* FIXME: RESUME */
		}
	}

	/* '' */
	/*
	 * ' Writes the "PlayWave" message to the given user's outgoing data buffer.
	 */
	/* ' */
	/* ' @param UserIndex User to which the message is intended. */
	/* ' @param wave The wave to be played. */
	/*
	 * ' @param X The X position in map coordinates from where the sound comes.
	 */
	/*
	 * ' @param Y The Y position in map coordinates from where the sound comes.
	 */
	/*
	 * ' @remarks The data is not actually sent until the buffer is properly
	 * flushed.
	 */

	static void WritePlayWave(int UserIndex, int wave, int X, int Y) {
		/* '*************************************************** */
		/* 'Author: Juan Martín Sotuyo Dodero (Maraxus) */
		/* 'Last Modification: 08/08/07 */
		/* 'Last Modified by: Rapsodius */
		/* 'Added X and Y positions for 3D Sounds */
		/* '*************************************************** */
		/* FIXME: ON ERROR GOTO ErrHandler */
		Declaraciones.UserList[UserIndex].outgoingData.WriteASCIIStringFixed(PrepareMessagePlayWave(wave, X, Y));
		return;

		/* FIXME: ErrHandler : */
		if (Err.Number == Declaraciones.UserList[UserIndex].outgoingData.NotEnoughSpaceErrCode) {
			FlushBuffer(UserIndex);
			/* FIXME: RESUME */
		}
	}

	/* '' */
	/*
	 * ' Writes the "GuildList" message to the given user's outgoing data
	 * buffer.
	 */
	/* ' */
	/* ' @param UserIndex User to which the message is intended. */
	/* ' @param GuildList List of guilds to be sent. */
	/*
	 * ' @remarks The data is not actually sent until the buffer is properly
	 * flushed.
	 */

	static void WriteGuildList(int UserIndex, String[] /* FIXME BYREF!! */ guildList) {
 /* '*************************************************** */
 /* 'Author: Juan Martín Sotuyo Dodero (Maraxus) */
 /* 'Last Modification: 05/17/06 */
 /* 'Writes the "GuildList" message to the given user's outgoing data buffer */
 /* '*************************************************** */
 /* FIXME: ON ERROR GOTO ErrHandler */
 String Tmp;
 int i;
 
  Declaraciones.UserList[UserIndex].outgoingData.WriteByte(ServerPacketID.guildList);
  
  /* ' Prepare guild name's list */
   for (i = (vb6.LBound(guildList[])); i <= (vb6.UBound(guildList[])); i++) {
   Tmp = Tmp + guildList[i] + Protocol.SEPARATOR;
  }
  
  if (vb6.Len(Tmp)) {
  Tmp = vb6.Left(Tmp, vb6.Len(Tmp)-1);
  }
  
  Declaraciones.UserList[UserIndex].outgoingData.WriteASCIIString(Tmp);
 return;
 
 /* FIXME: ErrHandler : */
  if (Err.Number == Declaraciones.UserList[UserIndex].outgoingData.NotEnoughSpaceErrCode) {
  FlushBuffer(UserIndex);
  /* FIXME: RESUME */
 }
}

	/* '' */
	/*
	 * ' Writes the "AreaChanged" message to the given user's outgoing data
	 * buffer.
	 */
	/* ' */
	/* ' @param UserIndex User to which the message is intended. */
	/*
	 * ' @remarks The data is not actually sent until the buffer is properly
	 * flushed.
	 */

	static void WriteAreaChanged(int UserIndex) {
		/* '*************************************************** */
		/* 'Author: Juan Martín Sotuyo Dodero (Maraxus) */
		/* 'Last Modification: 05/17/06 */
		/*
		 * 'Writes the "AreaChanged" message to the given user's outgoing data
		 * buffer
		 */
		/* '*************************************************** */
		/* FIXME: ON ERROR GOTO ErrHandler */
		Declaraciones.UserList[UserIndex].outgoingData.WriteByte(ServerPacketID.AreaChanged);
		Declaraciones.UserList[UserIndex].outgoingData.WriteByte(Declaraciones.UserList[UserIndex].Pos.X);
		Declaraciones.UserList[UserIndex].outgoingData.WriteByte(Declaraciones.UserList[UserIndex].Pos.Y);
		return;

		/* FIXME: ErrHandler : */
		if (Err.Number == Declaraciones.UserList[UserIndex].outgoingData.NotEnoughSpaceErrCode) {
			FlushBuffer(UserIndex);
			/* FIXME: RESUME */
		}
	}

	/* '' */
	/*
	 * ' Writes the "PauseToggle" message to the given user's outgoing data
	 * buffer.
	 */
	/* ' */
	/* ' @param UserIndex User to which the message is intended. */
	/*
	 * ' @remarks The data is not actually sent until the buffer is properly
	 * flushed.
	 */

	static void WritePauseToggle(int UserIndex) {
		/* '*************************************************** */
		/* 'Author: Juan Martín Sotuyo Dodero (Maraxus) */
		/* 'Last Modification: 05/17/06 */
		/*
		 * 'Writes the "PauseToggle" message to the given user's outgoing data
		 * buffer
		 */
		/* '*************************************************** */
		/* FIXME: ON ERROR GOTO ErrHandler */
		Declaraciones.UserList[UserIndex].outgoingData.WriteASCIIStringFixed(PrepareMessagePauseToggle());
		return;

		/* FIXME: ErrHandler : */
		if (Err.Number == Declaraciones.UserList[UserIndex].outgoingData.NotEnoughSpaceErrCode) {
			FlushBuffer(UserIndex);
			/* FIXME: RESUME */
		}
	}

	/* '' */
	/*
	 * ' Writes the "RainToggle" message to the given user's outgoing data
	 * buffer.
	 */
	/* ' */
	/* ' @param UserIndex User to which the message is intended. */
	/*
	 * ' @remarks The data is not actually sent until the buffer is properly
	 * flushed.
	 */

	static void WriteRainToggle(int UserIndex) {
		/* '*************************************************** */
		/* 'Author: Juan Martín Sotuyo Dodero (Maraxus) */
		/* 'Last Modification: 05/17/06 */
		/*
		 * 'Writes the "RainToggle" message to the given user's outgoing data
		 * buffer
		 */
		/* '*************************************************** */
		/* FIXME: ON ERROR GOTO ErrHandler */
		Declaraciones.UserList[UserIndex].outgoingData.WriteASCIIStringFixed(PrepareMessageRainToggle());
		return;

		/* FIXME: ErrHandler : */
		if (Err.Number == Declaraciones.UserList[UserIndex].outgoingData.NotEnoughSpaceErrCode) {
			FlushBuffer(UserIndex);
			/* FIXME: RESUME */
		}
	}

	/* '' */
	/*
	 * ' Writes the "CreateFX" message to the given user's outgoing data buffer.
	 */
	/* ' */
	/* ' @param UserIndex User to which the message is intended. */
	/* ' @param CharIndex Character upon which the FX will be created. */
	/* ' @param FX FX index to be displayed over the new character. */
	/* ' @param FXLoops Number of times the FX should be rendered. */
	/*
	 * ' @remarks The data is not actually sent until the buffer is properly
	 * flushed.
	 */

	static void WriteCreateFX(int UserIndex, int CharIndex, int FX, int FXLoops) {
		/* '*************************************************** */
		/* 'Author: Juan Martín Sotuyo Dodero (Maraxus) */
		/* 'Last Modification: 05/17/06 */
		/*
		 * 'Writes the "CreateFX" message to the given user's outgoing data
		 * buffer
		 */
		/* '*************************************************** */
		/* FIXME: ON ERROR GOTO ErrHandler */
		Declaraciones.UserList[UserIndex].outgoingData
				.WriteASCIIStringFixed(PrepareMessageCreateFX(CharIndex, FX, FXLoops));
		return;

		/* FIXME: ErrHandler : */
		if (Err.Number == Declaraciones.UserList[UserIndex].outgoingData.NotEnoughSpaceErrCode) {
			FlushBuffer(UserIndex);
			/* FIXME: RESUME */
		}
	}

	/* '' */
	/*
	 * ' Writes the "UpdateUserStats" message to the given user's outgoing data
	 * buffer.
	 */
	/* ' */
	/* ' @param UserIndex User to which the message is intended. */
	/*
	 * ' @remarks The data is not actually sent until the buffer is properly
	 * flushed.
	 */

	static void WriteUpdateUserStats(int UserIndex) {
		/* '*************************************************** */
		/* 'Author: Juan Martín Sotuyo Dodero (Maraxus) */
		/* 'Last Modification: 05/17/06 */
		/*
		 * 'Writes the "UpdateUserStats" message to the given user's outgoing
		 * data buffer
		 */
		/* '*************************************************** */
		/* FIXME: ON ERROR GOTO ErrHandler */
		Declaraciones.UserList[UserIndex].outgoingData.WriteByte(ServerPacketID.UpdateUserStats);
		Declaraciones.UserList[UserIndex].outgoingData.WriteInteger(Declaraciones.UserList[UserIndex].Stats.MaxHp);
		Declaraciones.UserList[UserIndex].outgoingData.WriteInteger(Declaraciones.UserList[UserIndex].Stats.MinHp);
		Declaraciones.UserList[UserIndex].outgoingData.WriteInteger(Declaraciones.UserList[UserIndex].Stats.MaxMAN);
		Declaraciones.UserList[UserIndex].outgoingData.WriteInteger(Declaraciones.UserList[UserIndex].Stats.MinMAN);
		Declaraciones.UserList[UserIndex].outgoingData.WriteInteger(Declaraciones.UserList[UserIndex].Stats.MaxSta);
		Declaraciones.UserList[UserIndex].outgoingData.WriteInteger(Declaraciones.UserList[UserIndex].Stats.MinSta);
		Declaraciones.UserList[UserIndex].outgoingData.WriteLong(Declaraciones.UserList[UserIndex].Stats.GLD);
		Declaraciones.UserList[UserIndex].outgoingData.WriteByte(Declaraciones.UserList[UserIndex].Stats.ELV);
		Declaraciones.UserList[UserIndex].outgoingData.WriteLong(Declaraciones.UserList[UserIndex].Stats.ELU);
		Declaraciones.UserList[UserIndex].outgoingData.WriteLong(Declaraciones.UserList[UserIndex].Stats.Exp);
		return;

		/* FIXME: ErrHandler : */
		if (Err.Number == Declaraciones.UserList[UserIndex].outgoingData.NotEnoughSpaceErrCode) {
			FlushBuffer(UserIndex);
			/* FIXME: RESUME */
		}
	}

	/* '' */
	/*
	 * ' Writes the "WorkRequestTarget" message to the given user's outgoing
	 * data buffer.
	 */
	/* ' */
	/* ' @param UserIndex User to which the message is intended. */
	/* ' @param Skill The skill for which we request a target. */
	/*
	 * ' @remarks The data is not actually sent until the buffer is properly
	 * flushed.
	 */

	static void WriteWorkRequestTarget(int UserIndex, eSkill Skill) {
		/* '*************************************************** */
		/* 'Author: Juan Martín Sotuyo Dodero (Maraxus) */
		/* 'Last Modification: 05/17/06 */
		/*
		 * 'Writes the "WorkRequestTarget" message to the given user's outgoing
		 * data buffer
		 */
		/* '*************************************************** */
		/* FIXME: ON ERROR GOTO ErrHandler */
		Declaraciones.UserList[UserIndex].outgoingData.WriteByte(ServerPacketID.WorkRequestTarget);
		Declaraciones.UserList[UserIndex].outgoingData.WriteByte(Skill);
		return;

		/* FIXME: ErrHandler : */
		if (Err.Number == Declaraciones.UserList[UserIndex].outgoingData.NotEnoughSpaceErrCode) {
			FlushBuffer(UserIndex);
			/* FIXME: RESUME */
		}
	}

	/* '' */
	/*
	 * ' Writes the "ChangeInventorySlot" message to the given user's outgoing
	 * data buffer.
	 */
	/* ' */
	/* ' @param UserIndex User to which the message is intended. */
	/* ' @param slot Inventory slot which needs to be updated. */
	/*
	 * ' @remarks The data is not actually sent until the buffer is properly
	 * flushed.
	 */

	static void WriteChangeInventorySlot(int UserIndex, int Slot) {
		/* '*************************************************** */
		/* 'Author: Juan Martín Sotuyo Dodero (Maraxus) */
		/* 'Last Modification: 3/12/09 */
		/*
		 * 'Writes the "ChangeInventorySlot" message to the given user's
		 * outgoing data buffer
		 */
		/* '3/12/09: Budi - Ahora se envia MaxDef y MinDef en lugar de Def */
		/* '*************************************************** */
		/* FIXME: ON ERROR GOTO ErrHandler */
		Declaraciones.UserList[UserIndex].outgoingData.WriteByte(ServerPacketID.ChangeInventorySlot);
		Declaraciones.UserList[UserIndex].outgoingData.WriteByte(Slot);

		int ObjIndex;
		Declaraciones.ObjData obData;

		ObjIndex = Declaraciones.UserList[UserIndex].Invent.Object[Slot].ObjIndex;

		if (ObjIndex > 0) {
			obData = Declaraciones.ObjData[ObjIndex];
		}

		Declaraciones.UserList[UserIndex].outgoingData.WriteInteger(ObjIndex);
		Declaraciones.UserList[UserIndex].outgoingData.WriteASCIIString(obData.Name);
		Declaraciones.UserList[UserIndex].outgoingData
				.WriteInteger(Declaraciones.UserList[UserIndex].Invent.Object[Slot].Amount);
		Declaraciones.UserList[UserIndex].outgoingData
				.WriteBoolean(Declaraciones.UserList[UserIndex].Invent.Object[Slot].Equipped);
		Declaraciones.UserList[UserIndex].outgoingData.WriteInteger(obData.GrhIndex);
		Declaraciones.UserList[UserIndex].outgoingData.WriteByte(obData.OBJType);
		Declaraciones.UserList[UserIndex].outgoingData.WriteInteger(obData.MaxHIT);
		Declaraciones.UserList[UserIndex].outgoingData.WriteInteger(obData.MinHIT);
		Declaraciones.UserList[UserIndex].outgoingData.WriteInteger(obData.MaxDef);
		Declaraciones.UserList[UserIndex].outgoingData.WriteInteger(obData.MinDef);
		Declaraciones.UserList[UserIndex].outgoingData.WriteSingle(modSistemaComercio.SalePrice(ObjIndex));
		return;

		/* FIXME: ErrHandler : */
		if (Err.Number == Declaraciones.UserList[UserIndex].outgoingData.NotEnoughSpaceErrCode) {
			FlushBuffer(UserIndex);
			/* FIXME: RESUME */
		}
	}

	static void WriteAddSlots(int UserIndex, eMochilas Mochila) {
		/* '*************************************************** */
		/* 'Author: Budi */
		/* 'Last Modification: 01/12/09 */
		/*
		 * 'Writes the "AddSlots" message to the given user's outgoing data
		 * buffer
		 */
		/* '*************************************************** */
		Declaraciones.UserList[UserIndex].outgoingData.WriteByte(ServerPacketID.AddSlots);
		Declaraciones.UserList[UserIndex].outgoingData.WriteByte(Mochila);
	}

	/* '' */
	/*
	 * ' Writes the "ChangeBankSlot" message to the given user's outgoing data
	 * buffer.
	 */
	/* ' */
	/* ' @param UserIndex User to which the message is intended. */
	/* ' @param slot Inventory slot which needs to be updated. */
	/*
	 * ' @remarks The data is not actually sent until the buffer is properly
	 * flushed.
	 */

	static void WriteChangeBankSlot(int UserIndex, int Slot) {
		/* '*************************************************** */
		/* 'Author: Juan Martín Sotuyo Dodero (Maraxus) */
		/* 'Last Modification: 12/03/09 */
		/*
		 * 'Writes the "ChangeBankSlot" message to the given user's outgoing
		 * data buffer
		 */
		/*
		 * '12/03/09: Budi - Ahora se envia MaxDef y MinDef en lugar de sólo Def
		 */
		/* '*************************************************** */
		/* FIXME: ON ERROR GOTO ErrHandler */
		Declaraciones.UserList[UserIndex].outgoingData.WriteByte(ServerPacketID.ChangeBankSlot);
		Declaraciones.UserList[UserIndex].outgoingData.WriteByte(Slot);

		int ObjIndex;
		Declaraciones.ObjData obData;

		ObjIndex = Declaraciones.UserList[UserIndex].BancoInvent.Object[Slot].ObjIndex;

		Declaraciones.UserList[UserIndex].outgoingData.WriteInteger(ObjIndex);

		if (ObjIndex > 0) {
			obData = Declaraciones.ObjData[ObjIndex];
		}

		Declaraciones.UserList[UserIndex].outgoingData.WriteASCIIString(obData.Name);
		Declaraciones.UserList[UserIndex].outgoingData
				.WriteInteger(Declaraciones.UserList[UserIndex].BancoInvent.Object[Slot].Amount);
		Declaraciones.UserList[UserIndex].outgoingData.WriteInteger(obData.GrhIndex);
		Declaraciones.UserList[UserIndex].outgoingData.WriteByte(obData.OBJType);
		Declaraciones.UserList[UserIndex].outgoingData.WriteInteger(obData.MaxHIT);
		Declaraciones.UserList[UserIndex].outgoingData.WriteInteger(obData.MinHIT);
		Declaraciones.UserList[UserIndex].outgoingData.WriteInteger(obData.MaxDef);
		Declaraciones.UserList[UserIndex].outgoingData.WriteInteger(obData.MinDef);
		Declaraciones.UserList[UserIndex].outgoingData.WriteLong(obData.Valor);
		return;

		/* FIXME: ErrHandler : */
		if (Err.Number == Declaraciones.UserList[UserIndex].outgoingData.NotEnoughSpaceErrCode) {
			FlushBuffer(UserIndex);
			/* FIXME: RESUME */
		}
	}

	/* '' */
	/*
	 * ' Writes the "ChangeSpellSlot" message to the given user's outgoing data
	 * buffer.
	 */
	/* ' */
	/* ' @param UserIndex User to which the message is intended. */
	/* ' @param slot Spell slot to update. */
	/*
	 * ' @remarks The data is not actually sent until the buffer is properly
	 * flushed.
	 */

	static void WriteChangeSpellSlot(int UserIndex, int Slot) {
		/* '*************************************************** */
		/* 'Author: Juan Martín Sotuyo Dodero (Maraxus) */
		/* 'Last Modification: 05/17/06 */
		/*
		 * 'Writes the "ChangeSpellSlot" message to the given user's outgoing
		 * data buffer
		 */
		/* '*************************************************** */
		/* FIXME: ON ERROR GOTO ErrHandler */
		Declaraciones.UserList[UserIndex].outgoingData.WriteByte(ServerPacketID.ChangeSpellSlot);
		Declaraciones.UserList[UserIndex].outgoingData.WriteByte(Slot);
		Declaraciones.UserList[UserIndex].outgoingData
				.WriteInteger(Declaraciones.UserList[UserIndex].Stats.UserHechizos[Slot]);

		if (Declaraciones.UserList[UserIndex].Stats.UserHechizos[Slot] > 0) {
			Declaraciones.UserList[UserIndex].outgoingData.WriteASCIIString(
					Declaraciones.Hechizos[Declaraciones.UserList[UserIndex].Stats.UserHechizos[Slot]].Nombre);
		} else {
			Declaraciones.UserList[UserIndex].outgoingData.WriteASCIIString("(None)");
		}
		return;

		/* FIXME: ErrHandler : */
		if (Err.Number == Declaraciones.UserList[UserIndex].outgoingData.NotEnoughSpaceErrCode) {
			FlushBuffer(UserIndex);
			/* FIXME: RESUME */
		}
	}

	/* '' */
	/*
	 * ' Writes the "Atributes" message to the given user's outgoing data
	 * buffer.
	 */
	/* ' */
	/* ' @param UserIndex User to which the message is intended. */
	/*
	 * ' @remarks The data is not actually sent until the buffer is properly
	 * flushed.
	 */

	static void WriteAttributes(int UserIndex) {
		/* '*************************************************** */
		/* 'Author: Juan Martín Sotuyo Dodero (Maraxus) */
		/* 'Last Modification: 05/17/06 */
		/*
		 * 'Writes the "Atributes" message to the given user's outgoing data
		 * buffer
		 */
		/* '*************************************************** */
		/* FIXME: ON ERROR GOTO ErrHandler */
		Declaraciones.UserList[UserIndex].outgoingData.WriteByte(ServerPacketID.Atributes);
		Declaraciones.UserList[UserIndex].outgoingData
				.WriteByte(Declaraciones.UserList[UserIndex].Stats.UserAtributos[eAtributos.Fuerza]);
		Declaraciones.UserList[UserIndex].outgoingData
				.WriteByte(Declaraciones.UserList[UserIndex].Stats.UserAtributos[eAtributos.Agilidad]);
		Declaraciones.UserList[UserIndex].outgoingData
				.WriteByte(Declaraciones.UserList[UserIndex].Stats.UserAtributos[eAtributos.Inteligencia]);
		Declaraciones.UserList[UserIndex].outgoingData
				.WriteByte(Declaraciones.UserList[UserIndex].Stats.UserAtributos[eAtributos.Carisma]);
		Declaraciones.UserList[UserIndex].outgoingData
				.WriteByte(Declaraciones.UserList[UserIndex].Stats.UserAtributos[eAtributos.Constitucion]);
		return;

		/* FIXME: ErrHandler : */
		if (Err.Number == Declaraciones.UserList[UserIndex].outgoingData.NotEnoughSpaceErrCode) {
			FlushBuffer(UserIndex);
			/* FIXME: RESUME */
		}
	}

	/* '' */
	/*
	 * ' Writes the "BlacksmithWeapons" message to the given user's outgoing
	 * data buffer.
	 */
	/* ' */
	/* ' @param UserIndex User to which the message is intended. */
	/*
	 * ' @remarks The data is not actually sent until the buffer is properly
	 * flushed.
	 */

	static void WriteBlacksmithWeapons(int UserIndex) {
 /* '*************************************************** */
 /* 'Author: Juan Martín Sotuyo Dodero (Maraxus) */
 /* 'Last Modification: 04/15/2008 (NicoNZ) Habia un error al fijarse los skills del personaje */
 /* 'Writes the "BlacksmithWeapons" message to the given user's outgoing data buffer */
 /* '*************************************************** */
 /* FIXME: ON ERROR GOTO ErrHandler */
 int i;
 Declaraciones.ObjData Obj;
 int[] validIndexes;
 int Count;
 
 validIndexes = new Integer[0];
 validIndexes = (validIndexes == null) ? new Integer[1 + vb6.UBound(Declaraciones.ArmasHerrero[])] : java.util.Arrays.copyOf(validIndexes, 1 + vb6.UBound(Declaraciones.ArmasHerrero[]));
 
  Declaraciones.UserList[UserIndex].outgoingData.WriteByte(ServerPacketID.BlacksmithWeapons);
  
   for (i = (1); i <= (vb6.UBound(Declaraciones.ArmasHerrero[])); i++) {
   /* ' Can the user create this object? If so add it to the list.... */
    if (Declaraciones.ObjData[Declaraciones.ArmasHerrero[i]].SkHerreria<=vb6.Round(Declaraciones.UserList[UserIndex].Stats.UserSkills[eSkill.Herreria]/Trabajo.ModHerreriA(Declaraciones.UserList[UserIndex].clase), 0)) {
    Count = Count+1;
    validIndexes[Count] = i;
   }
  }
  
  /* ' Write the number of objects in the list */
  Declaraciones.UserList[UserIndex].outgoingData.WriteInteger(Count);
  
  /* ' Write the needed data of each object */
   for (i = (1); i <= (Count); i++) {
   Obj = Declaraciones.ObjData[Declaraciones.ArmasHerrero[validIndexes[i]]];
   Declaraciones.UserList[UserIndex].outgoingData.WriteASCIIString(Obj.Name);
   Declaraciones.UserList[UserIndex].outgoingData.WriteInteger(Obj.GrhIndex);
   Declaraciones.UserList[UserIndex].outgoingData.WriteInteger(Obj.LingH);
   Declaraciones.UserList[UserIndex].outgoingData.WriteInteger(Obj.LingP);
   Declaraciones.UserList[UserIndex].outgoingData.WriteInteger(Obj.LingO);
   Declaraciones.UserList[UserIndex].outgoingData.WriteInteger(Declaraciones.ArmasHerrero[validIndexes[i]]);
   Declaraciones.UserList[UserIndex].outgoingData.WriteInteger(Obj.Upgrade);
  }
 return;
 
 /* FIXME: ErrHandler : */
  if (Err.Number == Declaraciones.UserList[UserIndex].outgoingData.NotEnoughSpaceErrCode) {
  FlushBuffer(UserIndex);
  /* FIXME: RESUME */
 }
}

	/* '' */
	/*
	 * ' Writes the "BlacksmithArmors" message to the given user's outgoing data
	 * buffer.
	 */
	/* ' */
	/* ' @param UserIndex User to which the message is intended. */
	/*
	 * ' @remarks The data is not actually sent until the buffer is properly
	 * flushed.
	 */

	static void WriteBlacksmithArmors(int UserIndex) {
 /* '*************************************************** */
 /* 'Author: Juan Martín Sotuyo Dodero (Maraxus) */
 /* 'Last Modification: 04/15/2008 (NicoNZ) Habia un error al fijarse los skills del personaje */
 /* 'Writes the "BlacksmithArmors" message to the given user's outgoing data buffer */
 /* '*************************************************** */
 /* FIXME: ON ERROR GOTO ErrHandler */
 int i;
 Declaraciones.ObjData Obj;
 int[] validIndexes;
 int Count;
 
 validIndexes = new Integer[0];
 validIndexes = (validIndexes == null) ? new Integer[1 + vb6.UBound(Declaraciones.ArmadurasHerrero[])] : java.util.Arrays.copyOf(validIndexes, 1 + vb6.UBound(Declaraciones.ArmadurasHerrero[]));
 
  Declaraciones.UserList[UserIndex].outgoingData.WriteByte(ServerPacketID.BlacksmithArmors);
  
   for (i = (1); i <= (vb6.UBound(Declaraciones.ArmadurasHerrero[])); i++) {
   /* ' Can the user create this object? If so add it to the list.... */
    if (Declaraciones.ObjData[Declaraciones.ArmadurasHerrero[i]].SkHerreria<=vb6.Round(Declaraciones.UserList[UserIndex].Stats.UserSkills[eSkill.Herreria]/Trabajo.ModHerreriA(Declaraciones.UserList[UserIndex].clase), 0)) {
    Count = Count+1;
    validIndexes[Count] = i;
   }
  }
  
  /* ' Write the number of objects in the list */
  Declaraciones.UserList[UserIndex].outgoingData.WriteInteger(Count);
  
  /* ' Write the needed data of each object */
   for (i = (1); i <= (Count); i++) {
   Obj = Declaraciones.ObjData[Declaraciones.ArmadurasHerrero[validIndexes[i]]];
   Declaraciones.UserList[UserIndex].outgoingData.WriteASCIIString(Obj.Name);
   Declaraciones.UserList[UserIndex].outgoingData.WriteInteger(Obj.GrhIndex);
   Declaraciones.UserList[UserIndex].outgoingData.WriteInteger(Obj.LingH);
   Declaraciones.UserList[UserIndex].outgoingData.WriteInteger(Obj.LingP);
   Declaraciones.UserList[UserIndex].outgoingData.WriteInteger(Obj.LingO);
   Declaraciones.UserList[UserIndex].outgoingData.WriteInteger(Declaraciones.ArmadurasHerrero[validIndexes[i]]);
   Declaraciones.UserList[UserIndex].outgoingData.WriteInteger(Obj.Upgrade);
  }
 return;
 
 /* FIXME: ErrHandler : */
  if (Err.Number == Declaraciones.UserList[UserIndex].outgoingData.NotEnoughSpaceErrCode) {
  FlushBuffer(UserIndex);
  /* FIXME: RESUME */
 }
}

	/* '' */
	/*
	 * ' Writes the "CarpenterObjects" message to the given user's outgoing data
	 * buffer.
	 */
	/* ' */
	/* ' @param UserIndex User to which the message is intended. */
	/*
	 * ' @remarks The data is not actually sent until the buffer is properly
	 * flushed.
	 */

	static void WriteCarpenterObjects(int UserIndex) {
 /* '*************************************************** */
 /* 'Author: Juan Martín Sotuyo Dodero (Maraxus) */
 /* 'Last Modification: 05/17/06 */
 /* 'Writes the "CarpenterObjects" message to the given user's outgoing data buffer */
 /* '*************************************************** */
 /* FIXME: ON ERROR GOTO ErrHandler */
 int i;
 Declaraciones.ObjData Obj;
 int[] validIndexes;
 int Count;
 
 validIndexes = new Integer[0];
 validIndexes = (validIndexes == null) ? new Integer[1 + vb6.UBound(Declaraciones.ObjCarpintero[])] : java.util.Arrays.copyOf(validIndexes, 1 + vb6.UBound(Declaraciones.ObjCarpintero[]));
 
  Declaraciones.UserList[UserIndex].outgoingData.WriteByte(ServerPacketID.CarpenterObjects);
  
   for (i = (1); i <= (vb6.UBound(Declaraciones.ObjCarpintero[])); i++) {
   /* ' Can the user create this object? If so add it to the list.... */
    if (Declaraciones.ObjData[Declaraciones.ObjCarpintero[i]].SkCarpinteria<=Declaraciones.UserList[UserIndex].Stats.UserSkills[eSkill.Carpinteria]/Trabajo.ModCarpinteria(Declaraciones.UserList[UserIndex].clase)) {
    Count = Count+1;
    validIndexes[Count] = i;
   }
  }
  
  /* ' Write the number of objects in the list */
  Declaraciones.UserList[UserIndex].outgoingData.WriteInteger(Count);
  
  /* ' Write the needed data of each object */
   for (i = (1); i <= (Count); i++) {
   Obj = Declaraciones.ObjData[Declaraciones.ObjCarpintero[validIndexes[i]]];
   Declaraciones.UserList[UserIndex].outgoingData.WriteASCIIString(Obj.Name);
   Declaraciones.UserList[UserIndex].outgoingData.WriteInteger(Obj.GrhIndex);
   Declaraciones.UserList[UserIndex].outgoingData.WriteInteger(Obj.Madera);
   Declaraciones.UserList[UserIndex].outgoingData.WriteInteger(Obj.MaderaElfica);
   Declaraciones.UserList[UserIndex].outgoingData.WriteInteger(Declaraciones.ObjCarpintero[validIndexes[i]]);
   Declaraciones.UserList[UserIndex].outgoingData.WriteInteger(Obj.Upgrade);
  }
 return;
 
 /* FIXME: ErrHandler : */
  if (Err.Number == Declaraciones.UserList[UserIndex].outgoingData.NotEnoughSpaceErrCode) {
  FlushBuffer(UserIndex);
  /* FIXME: RESUME */
 }
}

	/* '' */
	/*
	 * ' Writes the "RestOK" message to the given user's outgoing data buffer.
	 */
	/* ' */
	/* ' @param UserIndex User to which the message is intended. */
	/*
	 * ' @remarks The data is not actually sent until the buffer is properly
	 * flushed.
	 */

	static void WriteRestOK(int UserIndex) {
		/* '*************************************************** */
		/* 'Author: Juan Martín Sotuyo Dodero (Maraxus) */
		/* 'Last Modification: 05/17/06 */
		/*
		 * 'Writes the "RestOK" message to the given user's outgoing data buffer
		 */
		/* '*************************************************** */
		/* FIXME: ON ERROR GOTO ErrHandler */
		Declaraciones.UserList[UserIndex].outgoingData.WriteByte(ServerPacketID.RestOK);
		return;

		/* FIXME: ErrHandler : */
		if (Err.Number == Declaraciones.UserList[UserIndex].outgoingData.NotEnoughSpaceErrCode) {
			FlushBuffer(UserIndex);
			/* FIXME: RESUME */
		}
	}

	/* '' */
	/*
	 * ' Writes the "ErrorMsg" message to the given user's outgoing data buffer.
	 */
	/* ' */
	/* ' @param UserIndex User to which the message is intended. */
	/* ' @param message The error message to be displayed. */
	/*
	 * ' @remarks The data is not actually sent until the buffer is properly
	 * flushed.
	 */

	static void WriteErrorMsg(int UserIndex, String message) {
		/* '*************************************************** */
		/* 'Author: Juan Martín Sotuyo Dodero (Maraxus) */
		/* 'Last Modification: 05/17/06 */
		/*
		 * 'Writes the "ErrorMsg" message to the given user's outgoing data
		 * buffer
		 */
		/* '*************************************************** */
		/* FIXME: ON ERROR GOTO ErrHandler */
		Declaraciones.UserList[UserIndex].outgoingData.WriteASCIIStringFixed(PrepareMessageErrorMsg(message));
		return;

		/* FIXME: ErrHandler : */
		if (Err.Number == Declaraciones.UserList[UserIndex].outgoingData.NotEnoughSpaceErrCode) {
			FlushBuffer(UserIndex);
			/* FIXME: RESUME */
		}
	}

	/* '' */
	/* ' Writes the "Blind" message to the given user's outgoing data buffer. */
	/* ' */
	/* ' @param UserIndex User to which the message is intended. */
	/*
	 * ' @remarks The data is not actually sent until the buffer is properly
	 * flushed.
	 */

	static void WriteBlind(int UserIndex) {
		/* '*************************************************** */
		/* 'Author: Juan Martín Sotuyo Dodero (Maraxus) */
		/* 'Last Modification: 05/17/06 */
		/*
		 * 'Writes the "Blind" message to the given user's outgoing data buffer
		 */
		/* '*************************************************** */
		/* FIXME: ON ERROR GOTO ErrHandler */
		Declaraciones.UserList[UserIndex].outgoingData.WriteByte(ServerPacketID.Blind);
		return;

		/* FIXME: ErrHandler : */
		if (Err.Number == Declaraciones.UserList[UserIndex].outgoingData.NotEnoughSpaceErrCode) {
			FlushBuffer(UserIndex);
			/* FIXME: RESUME */
		}
	}

	/* '' */
	/* ' Writes the "Dumb" message to the given user's outgoing data buffer. */
	/* ' */
	/* ' @param UserIndex User to which the message is intended. */
	/*
	 * ' @remarks The data is not actually sent until the buffer is properly
	 * flushed.
	 */

	static void WriteDumb(int UserIndex) {
		/* '*************************************************** */
		/* 'Author: Juan Martín Sotuyo Dodero (Maraxus) */
		/* 'Last Modification: 05/17/06 */
		/*
		 * 'Writes the "Dumb" message to the given user's outgoing data buffer
		 */
		/* '*************************************************** */
		/* FIXME: ON ERROR GOTO ErrHandler */
		Declaraciones.UserList[UserIndex].outgoingData.WriteByte(ServerPacketID.Dumb);
		return;

		/* FIXME: ErrHandler : */
		if (Err.Number == Declaraciones.UserList[UserIndex].outgoingData.NotEnoughSpaceErrCode) {
			FlushBuffer(UserIndex);
			/* FIXME: RESUME */
		}
	}

	/* '' */
	/*
	 * ' Writes the "ShowSignal" message to the given user's outgoing data
	 * buffer.
	 */
	/* ' */
	/* ' @param UserIndex User to which the message is intended. */
	/* ' @param objIndex Index of the signal to be displayed. */
	/*
	 * ' @remarks The data is not actually sent until the buffer is properly
	 * flushed.
	 */

	static void WriteShowSignal(int UserIndex, int ObjIndex) {
		/* '*************************************************** */
		/* 'Author: Juan Martín Sotuyo Dodero (Maraxus) */
		/* 'Last Modification: 05/17/06 */
		/*
		 * 'Writes the "ShowSignal" message to the given user's outgoing data
		 * buffer
		 */
		/* '*************************************************** */
		/* FIXME: ON ERROR GOTO ErrHandler */
		Declaraciones.UserList[UserIndex].outgoingData.WriteByte(ServerPacketID.ShowSignal);
		Declaraciones.UserList[UserIndex].outgoingData.WriteASCIIString(Declaraciones.ObjData[ObjIndex].texto);
		Declaraciones.UserList[UserIndex].outgoingData.WriteInteger(Declaraciones.ObjData[ObjIndex].GrhSecundario);
		return;

		/* FIXME: ErrHandler : */
		if (Err.Number == Declaraciones.UserList[UserIndex].outgoingData.NotEnoughSpaceErrCode) {
			FlushBuffer(UserIndex);
			/* FIXME: RESUME */
		}
	}

	/* '' */
	/*
	 * ' Writes the "ChangeNPCInventorySlot" message to the given user's
	 * outgoing data buffer.
	 */
	/* ' */
	/* ' @param UserIndex User to which the message is intended. */
	/* ' @param slot The inventory slot in which this item is to be placed. */
	/* ' @param obj The object to be set in the NPC's inventory window. */
	/* ' @param price The value the NPC asks for the object. */
	/*
	 * ' @remarks The data is not actually sent until the buffer is properly
	 * flushed.
	 */

	static void WriteChangeNPCInventorySlot(int UserIndex, int Slot, Declaraciones.Obj /* FIXME BYREF!! */ Obj, float price) {
 /* '*************************************************** */
 /* 'Author: Juan Martín Sotuyo Dodero (Maraxus) */
 /* 'Last Modification: 12/03/09 */
 /* 'Last Modified by: Budi */
 /* 'Writes the "ChangeNPCInventorySlot" message to the given user's outgoing data buffer */
 /* '12/03/09: Budi - Ahora se envia MaxDef y MinDef en lugar de sólo Def */
 /* '*************************************************** */
 /* FIXME: ON ERROR GOTO ErrHandler */
 Declaraciones.ObjData ObjInfo;
 
  if (Obj.ObjIndex>=vb6.LBound(Declaraciones.ObjData[]) && Obj.ObjIndex<=vb6.UBound(Declaraciones.ObjData[])) {
  ObjInfo = Declaraciones.ObjData[Obj.ObjIndex];
 }
 
  Declaraciones.UserList[UserIndex].outgoingData.WriteByte(ServerPacketID.ChangeNPCInventorySlot);
  Declaraciones.UserList[UserIndex].outgoingData.WriteByte(Slot);
  Declaraciones.UserList[UserIndex].outgoingData.WriteASCIIString(ObjInfo.Name);
  Declaraciones.UserList[UserIndex].outgoingData.WriteInteger(Obj.Amount);
  Declaraciones.UserList[UserIndex].outgoingData.WriteSingle(price);
  Declaraciones.UserList[UserIndex].outgoingData.WriteInteger(ObjInfo.GrhIndex);
  Declaraciones.UserList[UserIndex].outgoingData.WriteInteger(Obj.ObjIndex);
  Declaraciones.UserList[UserIndex].outgoingData.WriteByte(ObjInfo.OBJType);
  Declaraciones.UserList[UserIndex].outgoingData.WriteInteger(ObjInfo.MaxHIT);
  Declaraciones.UserList[UserIndex].outgoingData.WriteInteger(ObjInfo.MinHIT);
  Declaraciones.UserList[UserIndex].outgoingData.WriteInteger(ObjInfo.MaxDef);
  Declaraciones.UserList[UserIndex].outgoingData.WriteInteger(ObjInfo.MinDef);
 return;
 
 /* FIXME: ErrHandler : */
  if (Err.Number == Declaraciones.UserList[UserIndex].outgoingData.NotEnoughSpaceErrCode) {
  FlushBuffer(UserIndex);
  /* FIXME: RESUME */
 }
}

	/* '' */
	/*
	 * ' Writes the "UpdateHungerAndThirst" message to the given user's outgoing
	 * data buffer.
	 */
	/* ' */
	/* ' @param UserIndex User to which the message is intended. */
	/*
	 * ' @remarks The data is not actually sent until the buffer is properly
	 * flushed.
	 */

	static void WriteUpdateHungerAndThirst(int UserIndex) {
		/* '*************************************************** */
		/* 'Author: Juan Martín Sotuyo Dodero (Maraxus) */
		/* 'Last Modification: 05/17/06 */
		/*
		 * 'Writes the "UpdateHungerAndThirst" message to the given user's
		 * outgoing data buffer
		 */
		/* '*************************************************** */
		/* FIXME: ON ERROR GOTO ErrHandler */
		Declaraciones.UserList[UserIndex].outgoingData.WriteByte(ServerPacketID.UpdateHungerAndThirst);
		Declaraciones.UserList[UserIndex].outgoingData.WriteByte(Declaraciones.UserList[UserIndex].Stats.MaxAGU);
		Declaraciones.UserList[UserIndex].outgoingData.WriteByte(Declaraciones.UserList[UserIndex].Stats.MinAGU);
		Declaraciones.UserList[UserIndex].outgoingData.WriteByte(Declaraciones.UserList[UserIndex].Stats.MaxHam);
		Declaraciones.UserList[UserIndex].outgoingData.WriteByte(Declaraciones.UserList[UserIndex].Stats.MinHam);
		return;

		/* FIXME: ErrHandler : */
		if (Err.Number == Declaraciones.UserList[UserIndex].outgoingData.NotEnoughSpaceErrCode) {
			FlushBuffer(UserIndex);
			/* FIXME: RESUME */
		}
	}

	/* '' */
	/* ' Writes the "Fame" message to the given user's outgoing data buffer. */
	/* ' */
	/* ' @param UserIndex User to which the message is intended. */
	/*
	 * ' @remarks The data is not actually sent until the buffer is properly
	 * flushed.
	 */

	static void WriteFame(int UserIndex) {
		/* '*************************************************** */
		/* 'Author: Juan Martín Sotuyo Dodero (Maraxus) */
		/* 'Last Modification: 05/17/06 */
		/*
		 * 'Writes the "Fame" message to the given user's outgoing data buffer
		 */
		/* '*************************************************** */
		/* FIXME: ON ERROR GOTO ErrHandler */
		Declaraciones.UserList[UserIndex].outgoingData.WriteByte(ServerPacketID.Fame);

		Declaraciones.UserList[UserIndex].outgoingData
				.WriteLong(Declaraciones.UserList[UserIndex].Reputacion.AsesinoRep);
		Declaraciones.UserList[UserIndex].outgoingData
				.WriteLong(Declaraciones.UserList[UserIndex].Reputacion.BandidoRep);
		Declaraciones.UserList[UserIndex].outgoingData
				.WriteLong(Declaraciones.UserList[UserIndex].Reputacion.BurguesRep);
		Declaraciones.UserList[UserIndex].outgoingData
				.WriteLong(Declaraciones.UserList[UserIndex].Reputacion.LadronesRep);
		Declaraciones.UserList[UserIndex].outgoingData.WriteLong(Declaraciones.UserList[UserIndex].Reputacion.NobleRep);
		Declaraciones.UserList[UserIndex].outgoingData.WriteLong(Declaraciones.UserList[UserIndex].Reputacion.PlebeRep);
		Declaraciones.UserList[UserIndex].outgoingData.WriteLong(Declaraciones.UserList[UserIndex].Reputacion.Promedio);
		return;

		/* FIXME: ErrHandler : */
		if (Err.Number == Declaraciones.UserList[UserIndex].outgoingData.NotEnoughSpaceErrCode) {
			FlushBuffer(UserIndex);
			/* FIXME: RESUME */
		}
	}

	/* '' */
	/*
	 * ' Writes the "MiniStats" message to the given user's outgoing data
	 * buffer.
	 */
	/* ' */
	/* ' @param UserIndex User to which the message is intended. */
	/*
	 * ' @remarks The data is not actually sent until the buffer is properly
	 * flushed.
	 */

	static void WriteMiniStats(int UserIndex) {
		/* '*************************************************** */
		/* 'Author: Juan Martín Sotuyo Dodero (Maraxus) */
		/* 'Last Modification: 05/17/06 */
		/*
		 * 'Writes the "MiniStats" message to the given user's outgoing data
		 * buffer
		 */
		/* '*************************************************** */
		/* FIXME: ON ERROR GOTO ErrHandler */
		Declaraciones.UserList[UserIndex].outgoingData.WriteByte(ServerPacketID.MiniStats);

		Declaraciones.UserList[UserIndex].outgoingData
				.WriteLong(Declaraciones.UserList[UserIndex].Faccion.CiudadanosMatados);
		Declaraciones.UserList[UserIndex].outgoingData
				.WriteLong(Declaraciones.UserList[UserIndex].Faccion.CriminalesMatados);

		/*
		 * 'TODO : Este valor es calculable, no debería NI EXISTIR, ya sea en el
		 * servidor ni en el cliente!!!
		 */
		Declaraciones.UserList[UserIndex].outgoingData
				.WriteLong(Declaraciones.UserList[UserIndex].Stats.UsuariosMatados);

		Declaraciones.UserList[UserIndex].outgoingData
				.WriteInteger(Declaraciones.UserList[UserIndex].Stats.NPCsMuertos);

		Declaraciones.UserList[UserIndex].outgoingData.WriteByte(Declaraciones.UserList[UserIndex].clase);
		Declaraciones.UserList[UserIndex].outgoingData.WriteLong(Declaraciones.UserList[UserIndex].Counters.Pena);
		return;

		/* FIXME: ErrHandler : */
		if (Err.Number == Declaraciones.UserList[UserIndex].outgoingData.NotEnoughSpaceErrCode) {
			FlushBuffer(UserIndex);
			/* FIXME: RESUME */
		}
	}

	/* '' */
	/*
	 * ' Writes the "LevelUp" message to the given user's outgoing data buffer.
	 */
	/* ' */
	/* ' @param skillPoints The number of free skill points the player has. */
	/*
	 * ' @remarks The data is not actually sent until the buffer is properly
	 * flushed.
	 */

	static void WriteLevelUp(int UserIndex, int skillPoints) {
		/* '*************************************************** */
		/* 'Author: Juan Martín Sotuyo Dodero (Maraxus) */
		/* 'Last Modification: 05/17/06 */
		/*
		 * 'Writes the "LevelUp" message to the given user's outgoing data
		 * buffer
		 */
		/* '*************************************************** */
		/* FIXME: ON ERROR GOTO ErrHandler */
		Declaraciones.UserList[UserIndex].outgoingData.WriteByte(ServerPacketID.LevelUp);
		Declaraciones.UserList[UserIndex].outgoingData.WriteInteger(skillPoints);
		return;

		/* FIXME: ErrHandler : */
		if (Err.Number == Declaraciones.UserList[UserIndex].outgoingData.NotEnoughSpaceErrCode) {
			FlushBuffer(UserIndex);
			/* FIXME: RESUME */
		}
	}

	/* '' */
	/*
	 * ' Writes the "AddForumMsg" message to the given user's outgoing data
	 * buffer.
	 */
	/* ' */
	/* ' @param title The title of the message to display. */
	/* ' @param message The message to be displayed. */
	/*
	 * ' @remarks The data is not actually sent until the buffer is properly
	 * flushed.
	 */

	static void WriteAddForumMsg(int UserIndex, eForumType ForumType,
			String /* FIXME BYREF!! */ Title, String /* FIXME BYREF!! */ Author,
			String /* FIXME BYREF!! */ message) {
		/* '*************************************************** */
		/* 'Author: Juan Martín Sotuyo Dodero (Maraxus) */
		/* 'Last Modification: 02/01/2010 */
		/*
		 * 'Writes the "AddForumMsg" message to the given user's outgoing data
		 * buffer
		 */
		/* '02/01/2010: ZaMa - Now sends Author and forum type */
		/* '*************************************************** */
		/* FIXME: ON ERROR GOTO ErrHandler */
		Declaraciones.UserList[UserIndex].outgoingData.WriteByte(ServerPacketID.AddForumMsg);
		Declaraciones.UserList[UserIndex].outgoingData.WriteByte(ForumType);
		Declaraciones.UserList[UserIndex].outgoingData.WriteASCIIString(Title);
		Declaraciones.UserList[UserIndex].outgoingData.WriteASCIIString(Author);
		Declaraciones.UserList[UserIndex].outgoingData.WriteASCIIString(message);
		return;

		/* FIXME: ErrHandler : */
		if (Err.Number == Declaraciones.UserList[UserIndex].outgoingData.NotEnoughSpaceErrCode) {
			FlushBuffer(UserIndex);
			/* FIXME: RESUME */
		}
	}

	/* '' */
	/*
	 * ' Writes the "ShowForumForm" message to the given user's outgoing data
	 * buffer.
	 */
	/* ' */
	/* ' @param UserIndex User to which the message is intended. */
	/*
	 * ' @remarks The data is not actually sent until the buffer is properly
	 * flushed.
	 */

	static void WriteShowForumForm(int UserIndex) {
		/* '*************************************************** */
		/* 'Author: Juan Martín Sotuyo Dodero (Maraxus) */
		/* 'Last Modification: 05/17/06 */
		/*
		 * 'Writes the "ShowForumForm" message to the given user's outgoing data
		 * buffer
		 */
		/* '*************************************************** */
		/* FIXME: ON ERROR GOTO ErrHandler */

		int Visibilidad;
		int CanMakeSticky;

		Declaraciones.UserList[UserIndex].outgoingData.WriteByte(ServerPacketID.ShowForumForm);

		Visibilidad = eForumVisibility.ieGENERAL_MEMBER;

		if (Extra.esCaos(UserIndex) || Extra.EsGm(UserIndex)) {
			Visibilidad = Visibilidad || eForumVisibility.ieCAOS_MEMBER;
		}

		if (Extra.esArmada(UserIndex) || Extra.EsGm(UserIndex)) {
			Visibilidad = Visibilidad || eForumVisibility.ieREAL_MEMBER;
		}

		Declaraciones.UserList[UserIndex].outgoingData.WriteByte(Visibilidad);

		/* ' Pueden mandar sticky los gms o los del consejo de armada/caos */
		if (Extra.EsGm(UserIndex)) {
			CanMakeSticky = 2;
		} else if ((Declaraciones.UserList[UserIndex].flags.Privilegios && PlayerType.ChaosCouncil) != 0) {
			CanMakeSticky = 1;
		} else if ((Declaraciones.UserList[UserIndex].flags.Privilegios && PlayerType.RoyalCouncil) != 0) {
			CanMakeSticky = 1;
		}

		Declaraciones.UserList[UserIndex].outgoingData.WriteByte(CanMakeSticky);
		return;

		/* FIXME: ErrHandler : */
		if (Err.Number == Declaraciones.UserList[UserIndex].outgoingData.NotEnoughSpaceErrCode) {
			FlushBuffer(UserIndex);
			/* FIXME: RESUME */
		}
	}

	/* '' */
	/*
	 * ' Writes the "SetInvisible" message to the given user's outgoing data
	 * buffer.
	 */
	/* ' */
	/* ' @param UserIndex User to which the message is intended. */
	/* ' @param CharIndex The char turning visible / invisible. */
	/*
	 * ' @param invisible True if the char is no longer visible, False
	 * otherwise.
	 */
	/*
	 * ' @remarks The data is not actually sent until the buffer is properly
	 * flushed.
	 */

	static void WriteSetInvisible(int UserIndex, int CharIndex, boolean invisible) {
		/* '*************************************************** */
		/* 'Author: Juan Martín Sotuyo Dodero (Maraxus) */
		/* 'Last Modification: 05/17/06 */
		/*
		 * 'Writes the "SetInvisible" message to the given user's outgoing data
		 * buffer
		 */
		/* '*************************************************** */
		/* FIXME: ON ERROR GOTO ErrHandler */
		Declaraciones.UserList[UserIndex].outgoingData
				.WriteASCIIStringFixed(PrepareMessageSetInvisible(CharIndex, invisible));
		return;

		/* FIXME: ErrHandler : */
		if (Err.Number == Declaraciones.UserList[UserIndex].outgoingData.NotEnoughSpaceErrCode) {
			FlushBuffer(UserIndex);
			/* FIXME: RESUME */
		}
	}

	/* '' */
	/*
	 * ' Writes the "DiceRoll" message to the given user's outgoing data buffer.
	 */
	/* ' */
	/* ' @param UserIndex User to which the message is intended. */
	/*
	 * ' @remarks The data is not actually sent until the buffer is properly
	 * flushed.
	 */

	static void WriteDiceRoll(int UserIndex) {
		/* '*************************************************** */
		/* 'Author: Juan Martín Sotuyo Dodero (Maraxus) */
		/* 'Last Modification: 05/17/06 */
		/*
		 * 'Writes the "DiceRoll" message to the given user's outgoing data
		 * buffer
		 */
		/* '*************************************************** */
		/* FIXME: ON ERROR GOTO ErrHandler */
		Declaraciones.UserList[UserIndex].outgoingData.WriteByte(ServerPacketID.DiceRoll);

		Declaraciones.UserList[UserIndex].outgoingData
				.WriteByte(Declaraciones.UserList[UserIndex].Stats.UserAtributos[eAtributos.Fuerza]);
		Declaraciones.UserList[UserIndex].outgoingData
				.WriteByte(Declaraciones.UserList[UserIndex].Stats.UserAtributos[eAtributos.Agilidad]);
		Declaraciones.UserList[UserIndex].outgoingData
				.WriteByte(Declaraciones.UserList[UserIndex].Stats.UserAtributos[eAtributos.Inteligencia]);
		Declaraciones.UserList[UserIndex].outgoingData
				.WriteByte(Declaraciones.UserList[UserIndex].Stats.UserAtributos[eAtributos.Carisma]);
		Declaraciones.UserList[UserIndex].outgoingData
				.WriteByte(Declaraciones.UserList[UserIndex].Stats.UserAtributos[eAtributos.Constitucion]);
		return;

		/* FIXME: ErrHandler : */
		if (Err.Number == Declaraciones.UserList[UserIndex].outgoingData.NotEnoughSpaceErrCode) {
			FlushBuffer(UserIndex);
			/* FIXME: RESUME */
		}
	}

	/* '' */
	/*
	 * ' Writes the "MeditateToggle" message to the given user's outgoing data
	 * buffer.
	 */
	/* ' */
	/* ' @param UserIndex User to which the message is intended. */
	/*
	 * ' @remarks The data is not actually sent until the buffer is properly
	 * flushed.
	 */

	static void WriteMeditateToggle(int UserIndex) {
		/* '*************************************************** */
		/* 'Author: Juan Martín Sotuyo Dodero (Maraxus) */
		/* 'Last Modification: 05/17/06 */
		/*
		 * 'Writes the "MeditateToggle" message to the given user's outgoing
		 * data buffer
		 */
		/* '*************************************************** */
		/* FIXME: ON ERROR GOTO ErrHandler */
		Declaraciones.UserList[UserIndex].outgoingData.WriteByte(ServerPacketID.MeditateToggle);
		return;

		/* FIXME: ErrHandler : */
		if (Err.Number == Declaraciones.UserList[UserIndex].outgoingData.NotEnoughSpaceErrCode) {
			FlushBuffer(UserIndex);
			/* FIXME: RESUME */
		}
	}

	/* '' */
	/*
	 * ' Writes the "BlindNoMore" message to the given user's outgoing data
	 * buffer.
	 */
	/* ' */
	/* ' @param UserIndex User to which the message is intended. */
	/*
	 * ' @remarks The data is not actually sent until the buffer is properly
	 * flushed.
	 */

	static void WriteBlindNoMore(int UserIndex) {
		/* '*************************************************** */
		/* 'Author: Juan Martín Sotuyo Dodero (Maraxus) */
		/* 'Last Modification: 05/17/06 */
		/*
		 * 'Writes the "BlindNoMore" message to the given user's outgoing data
		 * buffer
		 */
		/* '*************************************************** */
		/* FIXME: ON ERROR GOTO ErrHandler */
		Declaraciones.UserList[UserIndex].outgoingData.WriteByte(ServerPacketID.BlindNoMore);
		return;

		/* FIXME: ErrHandler : */
		if (Err.Number == Declaraciones.UserList[UserIndex].outgoingData.NotEnoughSpaceErrCode) {
			FlushBuffer(UserIndex);
			/* FIXME: RESUME */
		}
	}

	/* '' */
	/*
	 * ' Writes the "DumbNoMore" message to the given user's outgoing data
	 * buffer.
	 */
	/* ' */
	/* ' @param UserIndex User to which the message is intended. */
	/*
	 * ' @remarks The data is not actually sent until the buffer is properly
	 * flushed.
	 */

	static void WriteDumbNoMore(int UserIndex) {
		/* '*************************************************** */
		/* 'Author: Juan Martín Sotuyo Dodero (Maraxus) */
		/* 'Last Modification: 05/17/06 */
		/*
		 * 'Writes the "DumbNoMore" message to the given user's outgoing data
		 * buffer
		 */
		/* '*************************************************** */
		/* FIXME: ON ERROR GOTO ErrHandler */
		Declaraciones.UserList[UserIndex].outgoingData.WriteByte(ServerPacketID.DumbNoMore);
		return;

		/* FIXME: ErrHandler : */
		if (Err.Number == Declaraciones.UserList[UserIndex].outgoingData.NotEnoughSpaceErrCode) {
			FlushBuffer(UserIndex);
			/* FIXME: RESUME */
		}
	}

	/* '' */
	/*
	 * ' Writes the "SendSkills" message to the given user's outgoing data
	 * buffer.
	 */
	/* ' */
	/* ' @param UserIndex User to which the message is intended. */
	/*
	 * ' @remarks The data is not actually sent until the buffer is properly
	 * flushed.
	 */

	static void WriteSendSkills(int UserIndex) {
		/* '*************************************************** */
		/* 'Author: Juan Martín Sotuyo Dodero (Maraxus) */
		/* 'Last Modification: 11/19/09 */
		/*
		 * 'Writes the "SendSkills" message to the given user's outgoing data
		 * buffer
		 */
		/*
		 * '11/19/09: Pato - Now send the percentage of progress of the skills.
		 */
		/* '*************************************************** */
		/* FIXME: ON ERROR GOTO ErrHandler */
		int i;

		Declaraciones.UserList[UserIndex].outgoingData.WriteByte(ServerPacketID.SendSkills);

		for (i = (1); i <= (Declaraciones.NUMSKILLS); i++) {
			Declaraciones.UserList[UserIndex].outgoingData
					.WriteByte(Declaraciones.UserList[UserIndex].Stats.UserSkills[i]);
			if (Declaraciones.UserList[UserIndex].Stats.UserSkills[i] < Declaraciones.MAXSKILLPOINTS) {
				Declaraciones.UserList[UserIndex].outgoingData
						.WriteByte(vb6.Int(Declaraciones.UserList[UserIndex].Stats.ExpSkills[i] * 100
								/ Declaraciones.UserList[UserIndex].Stats.EluSkills[i]));
			} else {
				Declaraciones.UserList[UserIndex].outgoingData.WriteByte(0);
			}
		}
		return;

		/* FIXME: ErrHandler : */
		if (Err.Number == Declaraciones.UserList[UserIndex].outgoingData.NotEnoughSpaceErrCode) {
			FlushBuffer(UserIndex);
			/* FIXME: RESUME */
		}
	}

	/* '' */
	/*
	 * ' Writes the "TrainerCreatureList" message to the given user's outgoing
	 * data buffer.
	 */
	/* ' */
	/* ' @param UserIndex User to which the message is intended. */
	/* ' @param npcIndex The index of the requested trainer. */
	/*
	 * ' @remarks The data is not actually sent until the buffer is properly
	 * flushed.
	 */

	static void WriteTrainerCreatureList(int UserIndex, int NpcIndex) {
		/* '*************************************************** */
		/* 'Author: Juan Martín Sotuyo Dodero (Maraxus) */
		/* 'Last Modification: 05/17/06 */
		/*
		 * 'Writes the "TrainerCreatureList" message to the given user's
		 * outgoing data buffer
		 */
		/* '*************************************************** */
		/* FIXME: ON ERROR GOTO ErrHandler */
		int i;
		String str;

		Declaraciones.UserList[UserIndex].outgoingData.WriteByte(ServerPacketID.TrainerCreatureList);

		for (i = (1); i <= (Declaraciones.Npclist[NpcIndex].NroCriaturas); i++) {
			str = str + Declaraciones.Npclist[NpcIndex].Criaturas[i].NpcName + Protocol.SEPARATOR;
		}

		if (vb6.LenB(str) > 0) {
			str = vb6.Left(str, vb6.Len(str) - 1);
		}

		Declaraciones.UserList[UserIndex].outgoingData.WriteASCIIString(str);
		return;

		/* FIXME: ErrHandler : */
		if (Err.Number == Declaraciones.UserList[UserIndex].outgoingData.NotEnoughSpaceErrCode) {
			FlushBuffer(UserIndex);
			/* FIXME: RESUME */
		}
	}

	/* '' */
	/*
	 * ' Writes the "GuildNews" message to the given user's outgoing data
	 * buffer.
	 */
	/* ' */
	/* ' @param UserIndex User to which the message is intended. */
	/* ' @param guildNews The guild's news. */
	/* ' @param enemies The list of the guild's enemies. */
	/* ' @param allies The list of the guild's allies. */
	/*
	 * ' @remarks The data is not actually sent until the buffer is properly
	 * flushed.
	 */

	static void WriteGuildNews(int UserIndex, String guildNews, String[] /* FIXME BYREF!! */ enemies, String[] /* FIXME BYREF!! */ allies) {
 /* '*************************************************** */
 /* 'Author: Juan Martín Sotuyo Dodero (Maraxus) */
 /* 'Last Modification: 05/17/06 */
 /* 'Writes the "GuildNews" message to the given user's outgoing data buffer */
 /* '*************************************************** */
 /* FIXME: ON ERROR GOTO ErrHandler */
 int i;
 String Tmp;
 
  Declaraciones.UserList[UserIndex].outgoingData.WriteByte(ServerPacketID.guildNews);
  
  Declaraciones.UserList[UserIndex].outgoingData.WriteASCIIString(guildNews);
  
  /* 'Prepare enemies' list */
   for (i = (vb6.LBound(enemies[])); i <= (vb6.UBound(enemies[])); i++) {
   Tmp = Tmp + enemies[i] + Protocol.SEPARATOR;
  }
  
  if (vb6.Len(Tmp)) {
  Tmp = vb6.Left(Tmp, vb6.Len(Tmp)-1);
  }
  
  Declaraciones.UserList[UserIndex].outgoingData.WriteASCIIString(Tmp);
  
  Tmp = "";
  /* 'Prepare allies' list */
   for (i = (vb6.LBound(allies[])); i <= (vb6.UBound(allies[])); i++) {
   Tmp = Tmp + allies[i] + Protocol.SEPARATOR;
  }
  
  if (vb6.Len(Tmp)) {
  Tmp = vb6.Left(Tmp, vb6.Len(Tmp)-1);
  }
  
  Declaraciones.UserList[UserIndex].outgoingData.WriteASCIIString(Tmp);
 return;
 
 /* FIXME: ErrHandler : */
  if (Err.Number == Declaraciones.UserList[UserIndex].outgoingData.NotEnoughSpaceErrCode) {
  FlushBuffer(UserIndex);
  /* FIXME: RESUME */
 }
}

	/* '' */
	/*
	 * ' Writes the "OfferDetails" message to the given user's outgoing data
	 * buffer.
	 */
	/* ' */
	/* ' @param UserIndex User to which the message is intended. */
	/* ' @param details Th details of the Peace proposition. */
	/*
	 * ' @remarks The data is not actually sent until the buffer is properly
	 * flushed.
	 */

	static void WriteOfferDetails(int UserIndex, String details) {
		/* '*************************************************** */
		/* 'Author: Juan Martín Sotuyo Dodero (Maraxus) */
		/* 'Last Modification: 05/17/06 */
		/*
		 * 'Writes the "OfferDetails" message to the given user's outgoing data
		 * buffer
		 */
		/* '*************************************************** */
		/* FIXME: ON ERROR GOTO ErrHandler */
		int i;

		Declaraciones.UserList[UserIndex].outgoingData.WriteByte(ServerPacketID.OfferDetails);

		Declaraciones.UserList[UserIndex].outgoingData.WriteASCIIString(details);
		return;

		/* FIXME: ErrHandler : */
		if (Err.Number == Declaraciones.UserList[UserIndex].outgoingData.NotEnoughSpaceErrCode) {
			FlushBuffer(UserIndex);
			/* FIXME: RESUME */
		}
	}

	/* '' */
	/*
	 * ' Writes the "AlianceProposalsList" message to the given user's outgoing
	 * data buffer.
	 */
	/* ' */
	/* ' @param UserIndex User to which the message is intended. */
	/* ' @param guilds The list of guilds which propossed an alliance. */
	/*
	 * ' @remarks The data is not actually sent until the buffer is properly
	 * flushed.
	 */

	static void WriteAlianceProposalsList(int UserIndex, String[] /* FIXME BYREF!! */ guilds) {
 /* '*************************************************** */
 /* 'Author: Juan Martín Sotuyo Dodero (Maraxus) */
 /* 'Last Modification: 05/17/06 */
 /* 'Writes the "AlianceProposalsList" message to the given user's outgoing data buffer */
 /* '*************************************************** */
 /* FIXME: ON ERROR GOTO ErrHandler */
 int i;
 String Tmp;
 
  Declaraciones.UserList[UserIndex].outgoingData.WriteByte(ServerPacketID.AlianceProposalsList);
  
  /* ' Prepare guild's list */
   for (i = (vb6.LBound(guilds[])); i <= (vb6.UBound(guilds[])); i++) {
   Tmp = Tmp + guilds[i] + Protocol.SEPARATOR;
  }
  
  if (vb6.Len(Tmp)) {
  Tmp = vb6.Left(Tmp, vb6.Len(Tmp)-1);
  }
  
  Declaraciones.UserList[UserIndex].outgoingData.WriteASCIIString(Tmp);
 return;
 
 /* FIXME: ErrHandler : */
  if (Err.Number == Declaraciones.UserList[UserIndex].outgoingData.NotEnoughSpaceErrCode) {
  FlushBuffer(UserIndex);
  /* FIXME: RESUME */
 }
}

	/* '' */
	/*
	 * ' Writes the "PeaceProposalsList" message to the given user's outgoing
	 * data buffer.
	 */
	/* ' */
	/* ' @param UserIndex User to which the message is intended. */
	/* ' @param guilds The list of guilds which propossed peace. */
	/*
	 * ' @remarks The data is not actually sent until the buffer is properly
	 * flushed.
	 */

	static void WritePeaceProposalsList(int UserIndex, String[] /* FIXME BYREF!! */ guilds) {
 /* '*************************************************** */
 /* 'Author: Juan Martín Sotuyo Dodero (Maraxus) */
 /* 'Last Modification: 05/17/06 */
 /* 'Writes the "PeaceProposalsList" message to the given user's outgoing data buffer */
 /* '*************************************************** */
 /* FIXME: ON ERROR GOTO ErrHandler */
 int i;
 String Tmp;
 
  Declaraciones.UserList[UserIndex].outgoingData.WriteByte(ServerPacketID.PeaceProposalsList);
  
  /* ' Prepare guilds' list */
   for (i = (vb6.LBound(guilds[])); i <= (vb6.UBound(guilds[])); i++) {
   Tmp = Tmp + guilds[i] + Protocol.SEPARATOR;
  }
  
  if (vb6.Len(Tmp)) {
  Tmp = vb6.Left(Tmp, vb6.Len(Tmp)-1);
  }
  
  Declaraciones.UserList[UserIndex].outgoingData.WriteASCIIString(Tmp);
 return;
 
 /* FIXME: ErrHandler : */
  if (Err.Number == Declaraciones.UserList[UserIndex].outgoingData.NotEnoughSpaceErrCode) {
  FlushBuffer(UserIndex);
  /* FIXME: RESUME */
 }
}

	/* '' */
	/*
	 * ' Writes the "CharacterInfo" message to the given user's outgoing data
	 * buffer.
	 */
	/* ' */
	/* ' @param UserIndex User to which the message is intended. */
	/* ' @param charName The requested char's name. */
	/* ' @param race The requested char's race. */
	/* ' @param class The requested char's class. */
	/* ' @param gender The requested char's gender. */
	/* ' @param level The requested char's level. */
	/* ' @param gold The requested char's gold. */
	/* ' @param reputation The requested char's reputation. */
	/*
	 * ' @param previousPetitions The requested char's previous petitions to
	 * enter guilds.
	 */
	/* ' @param currentGuild The requested char's current guild. */
	/* ' @param previousGuilds The requested char's previous guilds. */
	/* ' @param RoyalArmy True if tha char belongs to the Royal Army. */
	/* ' @param CaosLegion True if tha char belongs to the Caos Legion. */
	/*
	 * ' @param citicensKilled The number of citicens killed by the requested
	 * char.
	 */
	/*
	 * ' @param criminalsKilled The number of criminals killed by the requested
	 * char.
	 */
	/*
	 * ' @remarks The data is not actually sent until the buffer is properly
	 * flushed.
	 */

	static void WriteCharacterInfo(int UserIndex, String charName, eRaza race, eClass Class, eGenero gender, int level,
			int gold, int bank, int reputation, String previousPetitions, String currentGuild, String previousGuilds,
			boolean RoyalArmy, boolean CaosLegion, int citicensKilled, int criminalsKilled) {
		/* '*************************************************** */
		/* 'Author: Juan Martín Sotuyo Dodero (Maraxus) */
		/* 'Last Modification: 05/17/06 */
		/*
		 * 'Writes the "CharacterInfo" message to the given user's outgoing data
		 * buffer
		 */
		/* '*************************************************** */
		/* FIXME: ON ERROR GOTO ErrHandler */
		Declaraciones.UserList[UserIndex].outgoingData.WriteByte(ServerPacketID.CharacterInfo);

		Declaraciones.UserList[UserIndex].outgoingData.WriteASCIIString(charName);
		Declaraciones.UserList[UserIndex].outgoingData.WriteByte(race);
		Declaraciones.UserList[UserIndex].outgoingData.WriteByte(Class);
		Declaraciones.UserList[UserIndex].outgoingData.WriteByte(gender);

		Declaraciones.UserList[UserIndex].outgoingData.WriteByte(level);
		Declaraciones.UserList[UserIndex].outgoingData.WriteLong(gold);
		Declaraciones.UserList[UserIndex].outgoingData.WriteLong(bank);
		Declaraciones.UserList[UserIndex].outgoingData.WriteLong(reputation);

		Declaraciones.UserList[UserIndex].outgoingData.WriteASCIIString(previousPetitions);
		Declaraciones.UserList[UserIndex].outgoingData.WriteASCIIString(currentGuild);
		Declaraciones.UserList[UserIndex].outgoingData.WriteASCIIString(previousGuilds);

		Declaraciones.UserList[UserIndex].outgoingData.WriteBoolean(RoyalArmy);
		Declaraciones.UserList[UserIndex].outgoingData.WriteBoolean(CaosLegion);

		Declaraciones.UserList[UserIndex].outgoingData.WriteLong(citicensKilled);
		Declaraciones.UserList[UserIndex].outgoingData.WriteLong(criminalsKilled);
		return;

		/* FIXME: ErrHandler : */
		if (Err.Number == Declaraciones.UserList[UserIndex].outgoingData.NotEnoughSpaceErrCode) {
			FlushBuffer(UserIndex);
			/* FIXME: RESUME */
		}
	}

	/* '' */
	/*
	 * ' Writes the "GuildLeaderInfo" message to the given user's outgoing data
	 * buffer.
	 */
	/* ' */
	/* ' @param UserIndex User to which the message is intended. */
	/* ' @param guildList The list of guild names. */
	/* ' @param memberList The list of the guild's members. */
	/* ' @param guildNews The guild's news. */
	/*
	 * ' @param joinRequests The list of chars which requested to join the clan.
	 */
	/*
	 * ' @remarks The data is not actually sent until the buffer is properly
	 * flushed.
	 */

	static void WriteGuildLeaderInfo(int UserIndex, String[] /* FIXME BYREF!! */ guildList, String[] /* FIXME BYREF!! */ MemberList, String guildNews, String[] /* FIXME BYREF!! */ joinRequests) {
 /* '*************************************************** */
 /* 'Author: Juan Martín Sotuyo Dodero (Maraxus) */
 /* 'Last Modification: 05/17/06 */
 /* 'Writes the "GuildLeaderInfo" message to the given user's outgoing data buffer */
 /* '*************************************************** */
 /* FIXME: ON ERROR GOTO ErrHandler */
 int i;
 String Tmp;
 
  Declaraciones.UserList[UserIndex].outgoingData.WriteByte(ServerPacketID.GuildLeaderInfo);
  
  /* ' Prepare guild name's list */
   for (i = (vb6.LBound(guildList[])); i <= (vb6.UBound(guildList[])); i++) {
   Tmp = Tmp + guildList[i] + Protocol.SEPARATOR;
  }
  
  if (vb6.Len(Tmp)) {
  Tmp = vb6.Left(Tmp, vb6.Len(Tmp)-1);
  }
  
  Declaraciones.UserList[UserIndex].outgoingData.WriteASCIIString(Tmp);
  
  /* ' Prepare guild member's list */
  Tmp = "";
   for (i = (vb6.LBound(MemberList[])); i <= (vb6.UBound(MemberList[])); i++) {
   Tmp = Tmp + MemberList[i] + Protocol.SEPARATOR;
  }
  
  if (vb6.Len(Tmp)) {
  Tmp = vb6.Left(Tmp, vb6.Len(Tmp)-1);
  }
  
  Declaraciones.UserList[UserIndex].outgoingData.WriteASCIIString(Tmp);
  
  /* ' Store guild news */
  Declaraciones.UserList[UserIndex].outgoingData.WriteASCIIString(guildNews);
  
  /* ' Prepare the join request's list */
  Tmp = "";
   for (i = (vb6.LBound(joinRequests[])); i <= (vb6.UBound(joinRequests[])); i++) {
   Tmp = Tmp + joinRequests[i] + Protocol.SEPARATOR;
  }
  
  if (vb6.Len(Tmp)) {
  Tmp = vb6.Left(Tmp, vb6.Len(Tmp)-1);
  }
  
  Declaraciones.UserList[UserIndex].outgoingData.WriteASCIIString(Tmp);
 return;
 
 /* FIXME: ErrHandler : */
  if (Err.Number == Declaraciones.UserList[UserIndex].outgoingData.NotEnoughSpaceErrCode) {
  FlushBuffer(UserIndex);
  /* FIXME: RESUME */
 }
}

	/* '' */
	/*
	 * ' Writes the "GuildLeaderInfo" message to the given user's outgoing data
	 * buffer.
	 */
	/* ' */
	/* ' @param UserIndex User to which the message is intended. */
	/* ' @param guildList The list of guild names. */
	/* ' @param memberList The list of the guild's members. */
	/*
	 * ' @remarks The data is not actually sent until the buffer is properly
	 * flushed.
	 */

	static void WriteGuildMemberInfo(int UserIndex, String[] /* FIXME BYREF!! */ guildList, String[] /* FIXME BYREF!! */ MemberList) {
 /* '*************************************************** */
 /* 'Author: ZaMa */
 /* 'Last Modification: 21/02/2010 */
 /* 'Writes the "GuildMemberInfo" message to the given user's outgoing data buffer */
 /* '*************************************************** */
 /* FIXME: ON ERROR GOTO ErrHandler */
 int i;
 String Tmp;
 
  Declaraciones.UserList[UserIndex].outgoingData.WriteByte(ServerPacketID.GuildMemberInfo);
  
  /* ' Prepare guild name's list */
   for (i = (vb6.LBound(guildList[])); i <= (vb6.UBound(guildList[])); i++) {
   Tmp = Tmp + guildList[i] + Protocol.SEPARATOR;
  }
  
  if (vb6.Len(Tmp)) {
  Tmp = vb6.Left(Tmp, vb6.Len(Tmp)-1);
  }
  
  Declaraciones.UserList[UserIndex].outgoingData.WriteASCIIString(Tmp);
  
  /* ' Prepare guild member's list */
  Tmp = "";
   for (i = (vb6.LBound(MemberList[])); i <= (vb6.UBound(MemberList[])); i++) {
   Tmp = Tmp + MemberList[i] + Protocol.SEPARATOR;
  }
  
  if (vb6.Len(Tmp)) {
  Tmp = vb6.Left(Tmp, vb6.Len(Tmp)-1);
  }
  
  Declaraciones.UserList[UserIndex].outgoingData.WriteASCIIString(Tmp);
 return;
 
 /* FIXME: ErrHandler : */
  if (Err.Number == Declaraciones.UserList[UserIndex].outgoingData.NotEnoughSpaceErrCode) {
  FlushBuffer(UserIndex);
  /* FIXME: RESUME */
 }
}

	/* '' */
	/*
	 * ' Writes the "GuildDetails" message to the given user's outgoing data
	 * buffer.
	 */
	/* ' */
	/* ' @param UserIndex User to which the message is intended. */
	/* ' @param guildName The requested guild's name. */
	/* ' @param founder The requested guild's founder. */
	/* ' @param foundationDate The requested guild's foundation date. */
	/* ' @param leader The requested guild's current leader. */
	/* ' @param URL The requested guild's website. */
	/* ' @param memberCount The requested guild's member count. */
	/* ' @param electionsOpen True if the clan is electing it's new leader. */
	/* ' @param alignment The requested guild's alignment. */
	/* ' @param enemiesCount The requested guild's enemy count. */
	/* ' @param alliesCount The requested guild's ally count. */
	/*
	 * ' @param antifactionPoints The requested guild's number of antifaction
	 * acts commited.
	 */
	/* ' @param codex The requested guild's codex. */
	/* ' @param guildDesc The requested guild's description. */
	/*
	 * ' @remarks The data is not actually sent until the buffer is properly
	 * flushed.
	 */

	static void WriteGuildDetails(int UserIndex, String GuildName, String founder, String foundationDate, String leader, String URL, int memberCount, boolean electionsOpen, String alignment, int enemiesCount, int AlliesCount, String antifactionPoints, String[] /* FIXME BYREF!! */ codex, String guildDesc) {
 /* '*************************************************** */
 /* 'Author: Juan Martín Sotuyo Dodero (Maraxus) */
 /* 'Last Modification: 05/17/06 */
 /* 'Writes the "GuildDetails" message to the given user's outgoing data buffer */
 /* '*************************************************** */
 /* FIXME: ON ERROR GOTO ErrHandler */
 int i;
 String temp;
 
  Declaraciones.UserList[UserIndex].outgoingData.WriteByte(ServerPacketID.GuildDetails);
  
  Declaraciones.UserList[UserIndex].outgoingData.WriteASCIIString(GuildName);
  Declaraciones.UserList[UserIndex].outgoingData.WriteASCIIString(founder);
  Declaraciones.UserList[UserIndex].outgoingData.WriteASCIIString(foundationDate);
  Declaraciones.UserList[UserIndex].outgoingData.WriteASCIIString(leader);
  Declaraciones.UserList[UserIndex].outgoingData.WriteASCIIString(URL);
  
  Declaraciones.UserList[UserIndex].outgoingData.WriteInteger(memberCount);
  Declaraciones.UserList[UserIndex].outgoingData.WriteBoolean(electionsOpen);
  
  Declaraciones.UserList[UserIndex].outgoingData.WriteASCIIString(alignment);
  
  Declaraciones.UserList[UserIndex].outgoingData.WriteInteger(enemiesCount);
  Declaraciones.UserList[UserIndex].outgoingData.WriteInteger(AlliesCount);
  
  Declaraciones.UserList[UserIndex].outgoingData.WriteASCIIString(antifactionPoints);
  
   for (i = (vb6.LBound(codex[])); i <= (vb6.UBound(codex[])); i++) {
   temp = temp + codex[i] + Protocol.SEPARATOR;
  }
  
  if (vb6.Len(temp)>1) {
  temp = vb6.Left(temp, vb6.Len(temp)-1);
  }
  
  Declaraciones.UserList[UserIndex].outgoingData.WriteASCIIString(temp);
  
  Declaraciones.UserList[UserIndex].outgoingData.WriteASCIIString(guildDesc);
 return;
 
 /* FIXME: ErrHandler : */
  if (Err.Number == Declaraciones.UserList[UserIndex].outgoingData.NotEnoughSpaceErrCode) {
  FlushBuffer(UserIndex);
  /* FIXME: RESUME */
 }
}

	/* '' */
	/*
	 * ' Writes the "ShowGuildAlign" message to the given user's outgoing data
	 * buffer.
	 */
	/* ' */
	/* ' @param UserIndex User to which the message is intended. */
	/*
	 * ' @remarks The data is not actually sent until the buffer is properly
	 * flushed.
	 */

	static void WriteShowGuildAlign(int UserIndex) {
		/* '*************************************************** */
		/* 'Author: ZaMa */
		/* 'Last Modification: 14/12/2009 */
		/*
		 * 'Writes the "ShowGuildAlign" message to the given user's outgoing
		 * data buffer
		 */
		/* '*************************************************** */
		/* FIXME: ON ERROR GOTO ErrHandler */
		Declaraciones.UserList[UserIndex].outgoingData.WriteByte(ServerPacketID.ShowGuildAlign);
		return;

		/* FIXME: ErrHandler : */
		if (Err.Number == Declaraciones.UserList[UserIndex].outgoingData.NotEnoughSpaceErrCode) {
			FlushBuffer(UserIndex);
			/* FIXME: RESUME */
		}
	}

	/* '' */
	/*
	 * ' Writes the "ShowGuildFundationForm" message to the given user's
	 * outgoing data buffer.
	 */
	/* ' */
	/* ' @param UserIndex User to which the message is intended. */
	/*
	 * ' @remarks The data is not actually sent until the buffer is properly
	 * flushed.
	 */

	static void WriteShowGuildFundationForm(int UserIndex) {
		/* '*************************************************** */
		/* 'Author: Juan Martín Sotuyo Dodero (Maraxus) */
		/* 'Last Modification: 05/17/06 */
		/*
		 * 'Writes the "ShowGuildFundationForm" message to the given user's
		 * outgoing data buffer
		 */
		/* '*************************************************** */
		/* FIXME: ON ERROR GOTO ErrHandler */
		Declaraciones.UserList[UserIndex].outgoingData.WriteByte(ServerPacketID.ShowGuildFundationForm);
		return;

		/* FIXME: ErrHandler : */
		if (Err.Number == Declaraciones.UserList[UserIndex].outgoingData.NotEnoughSpaceErrCode) {
			FlushBuffer(UserIndex);
			/* FIXME: RESUME */
		}
	}

	/* '' */
	/*
	 * ' Writes the "ParalizeOK" message to the given user's outgoing data
	 * buffer.
	 */
	/* ' */
	/* ' @param UserIndex User to which the message is intended. */
	/*
	 * ' @remarks The data is not actually sent until the buffer is properly
	 * flushed.
	 */

	static void WriteParalizeOK(int UserIndex) {
		/* '*************************************************** */
		/* 'Author: Juan Martín Sotuyo Dodero (Maraxus) */
		/* 'Last Modification: 08/12/07 */
		/* 'Last Modified By: Lucas Tavolaro Ortiz (Tavo) */
		/*
		 * 'Writes the "ParalizeOK" message to the given user's outgoing data
		 * buffer
		 */
		/* 'And updates user position */
		/* '*************************************************** */
		/* FIXME: ON ERROR GOTO ErrHandler */
		Declaraciones.UserList[UserIndex].outgoingData.WriteByte(ServerPacketID.ParalizeOK);
		WritePosUpdate(UserIndex);
		return;

		/* FIXME: ErrHandler : */
		if (Err.Number == Declaraciones.UserList[UserIndex].outgoingData.NotEnoughSpaceErrCode) {
			FlushBuffer(UserIndex);
			/* FIXME: RESUME */
		}
	}

	/* '' */
	/*
	 * ' Writes the "ShowUserRequest" message to the given user's outgoing data
	 * buffer.
	 */
	/* ' */
	/* ' @param UserIndex User to which the message is intended. */
	/* ' @param details DEtails of the char's request. */
	/*
	 * ' @remarks The data is not actually sent until the buffer is properly
	 * flushed.
	 */

	static void WriteShowUserRequest(int UserIndex, String details) {
		/* '*************************************************** */
		/* 'Author: Juan Martín Sotuyo Dodero (Maraxus) */
		/* 'Last Modification: 05/17/06 */
		/*
		 * 'Writes the "ShowUserRequest" message to the given user's outgoing
		 * data buffer
		 */
		/* '*************************************************** */
		/* FIXME: ON ERROR GOTO ErrHandler */
		Declaraciones.UserList[UserIndex].outgoingData.WriteByte(ServerPacketID.ShowUserRequest);

		Declaraciones.UserList[UserIndex].outgoingData.WriteASCIIString(details);
		return;

		/* FIXME: ErrHandler : */
		if (Err.Number == Declaraciones.UserList[UserIndex].outgoingData.NotEnoughSpaceErrCode) {
			FlushBuffer(UserIndex);
			/* FIXME: RESUME */
		}
	}

	/* '' */
	/*
	 * ' Writes the "TradeOK" message to the given user's outgoing data buffer.
	 */
	/* ' */
	/* ' @param UserIndex User to which the message is intended. */
	/*
	 * ' @remarks The data is not actually sent until the buffer is properly
	 * flushed.
	 */

	static void WriteTradeOK(int UserIndex) {
		/* '*************************************************** */
		/* 'Author: Juan Martín Sotuyo Dodero (Maraxus) */
		/* 'Last Modification: 05/17/06 */
		/*
		 * 'Writes the "TradeOK" message to the given user's outgoing data
		 * buffer
		 */
		/* '*************************************************** */
		/* FIXME: ON ERROR GOTO ErrHandler */
		Declaraciones.UserList[UserIndex].outgoingData.WriteByte(ServerPacketID.TradeOK);
		return;

		/* FIXME: ErrHandler : */
		if (Err.Number == Declaraciones.UserList[UserIndex].outgoingData.NotEnoughSpaceErrCode) {
			FlushBuffer(UserIndex);
			/* FIXME: RESUME */
		}
	}

	/* '' */
	/*
	 * ' Writes the "BankOK" message to the given user's outgoing data buffer.
	 */
	/* ' */
	/* ' @param UserIndex User to which the message is intended. */
	/*
	 * ' @remarks The data is not actually sent until the buffer is properly
	 * flushed.
	 */

	static void WriteBankOK(int UserIndex) {
		/* '*************************************************** */
		/* 'Author: Juan Martín Sotuyo Dodero (Maraxus) */
		/* 'Last Modification: 05/17/06 */
		/*
		 * 'Writes the "BankOK" message to the given user's outgoing data buffer
		 */
		/* '*************************************************** */
		/* FIXME: ON ERROR GOTO ErrHandler */
		Declaraciones.UserList[UserIndex].outgoingData.WriteByte(ServerPacketID.BankOK);
		return;

		/* FIXME: ErrHandler : */
		if (Err.Number == Declaraciones.UserList[UserIndex].outgoingData.NotEnoughSpaceErrCode) {
			FlushBuffer(UserIndex);
			/* FIXME: RESUME */
		}
	}

	/* '' */
	/*
	 * ' Writes the "ChangeUserTradeSlot" message to the given user's outgoing
	 * data buffer.
	 */
	/* ' */
	/* ' @param UserIndex User to which the message is intended. */
	/* ' @param ObjIndex The object's index. */
	/* ' @param amount The number of objects offered. */
	/*
	 * ' @remarks The data is not actually sent until the buffer is properly
	 * flushed.
	 */

	static void WriteChangeUserTradeSlot(int UserIndex, int OfferSlot, int ObjIndex, int Amount) {
		/* '*************************************************** */
		/* 'Author: Juan Martín Sotuyo Dodero (Maraxus) */
		/* 'Last Modification: 12/03/09 */
		/*
		 * 'Writes the "ChangeUserTradeSlot" message to the given user's
		 * outgoing data buffer
		 */
		/*
		 * '25/11/2009: ZaMa - Now sends the specific offer slot to be modified.
		 */
		/*
		 * '12/03/09: Budi - Ahora se envia MaxDef y MinDef en lugar de sólo Def
		 */
		/* '*************************************************** */
		/* FIXME: ON ERROR GOTO ErrHandler */
		Declaraciones.UserList[UserIndex].outgoingData.WriteByte(ServerPacketID.ChangeUserTradeSlot);

		Declaraciones.UserList[UserIndex].outgoingData.WriteByte(OfferSlot);
		Declaraciones.UserList[UserIndex].outgoingData.WriteInteger(ObjIndex);
		Declaraciones.UserList[UserIndex].outgoingData.WriteLong(Amount);

		if (ObjIndex > 0) {
			Declaraciones.UserList[UserIndex].outgoingData.WriteInteger(Declaraciones.ObjData[ObjIndex].GrhIndex);
			Declaraciones.UserList[UserIndex].outgoingData.WriteByte(Declaraciones.ObjData[ObjIndex].OBJType);
			Declaraciones.UserList[UserIndex].outgoingData.WriteInteger(Declaraciones.ObjData[ObjIndex].MaxHIT);
			Declaraciones.UserList[UserIndex].outgoingData.WriteInteger(Declaraciones.ObjData[ObjIndex].MinHIT);
			Declaraciones.UserList[UserIndex].outgoingData.WriteInteger(Declaraciones.ObjData[ObjIndex].MaxDef);
			Declaraciones.UserList[UserIndex].outgoingData.WriteInteger(Declaraciones.ObjData[ObjIndex].MinDef);
			Declaraciones.UserList[UserIndex].outgoingData.WriteLong(modSistemaComercio.SalePrice(ObjIndex));
			Declaraciones.UserList[UserIndex].outgoingData.WriteASCIIString(Declaraciones.ObjData[ObjIndex].Name);
			/* ' Borra el item */
		} else {
			Declaraciones.UserList[UserIndex].outgoingData.WriteInteger(0);
			Declaraciones.UserList[UserIndex].outgoingData.WriteByte(0);
			Declaraciones.UserList[UserIndex].outgoingData.WriteInteger(0);
			Declaraciones.UserList[UserIndex].outgoingData.WriteInteger(0);
			Declaraciones.UserList[UserIndex].outgoingData.WriteInteger(0);
			Declaraciones.UserList[UserIndex].outgoingData.WriteInteger(0);
			Declaraciones.UserList[UserIndex].outgoingData.WriteLong(0);
			Declaraciones.UserList[UserIndex].outgoingData.WriteASCIIString("");
		}
		return;

		/* FIXME: ErrHandler : */
		if (Err.Number == Declaraciones.UserList[UserIndex].outgoingData.NotEnoughSpaceErrCode) {
			FlushBuffer(UserIndex);
			/* FIXME: RESUME */
		}
	}

	/* '' */
	/*
	 * ' Writes the "SendNight" message to the given user's outgoing data
	 * buffer.
	 */
	/* ' */
	/* ' @param UserIndex User to which the message is intended. */
	/*
	 * ' @remarks The data is not actually sent until the buffer is properly
	 * flushed.
	 */

	static void WriteSendNight(int UserIndex, boolean night) {
		/* '*************************************************** */
		/* 'Author: Fredy Horacio Treboux (liquid) */
		/* 'Last Modification: 01/08/07 */
		/*
		 * 'Writes the "SendNight" message to the given user's outgoing data
		 * buffer
		 */
		/* '*************************************************** */
		/* FIXME: ON ERROR GOTO ErrHandler */
		Declaraciones.UserList[UserIndex].outgoingData.WriteByte(ServerPacketID.SendNight);
		Declaraciones.UserList[UserIndex].outgoingData.WriteBoolean(night);
		return;

		/* FIXME: ErrHandler : */
		if (Err.Number == Declaraciones.UserList[UserIndex].outgoingData.NotEnoughSpaceErrCode) {
			FlushBuffer(UserIndex);
			/* FIXME: RESUME */
		}
	}

	/* '' */
	/*
	 * ' Writes the "SpawnList" message to the given user's outgoing data
	 * buffer.
	 */
	/* ' */
	/* ' @param UserIndex User to which the message is intended. */
	/* ' @param npcNames The names of the creatures that can be spawned. */
	/*
	 * ' @remarks The data is not actually sent until the buffer is properly
	 * flushed.
	 */

	static void WriteSpawnList(int UserIndex, String[] /* FIXME BYREF!! */ npcNames) {
 /* '*************************************************** */
 /* 'Author: Juan Martín Sotuyo Dodero (Maraxus) */
 /* 'Last Modification: 05/17/06 */
 /* 'Writes the "SpawnList" message to the given user's outgoing data buffer */
 /* '*************************************************** */
 /* FIXME: ON ERROR GOTO ErrHandler */
 int i;
 String Tmp;
 
  Declaraciones.UserList[UserIndex].outgoingData.WriteByte(ServerPacketID.SpawnList);
  
   for (i = (vb6.LBound(npcNames[])); i <= (vb6.UBound(npcNames[])); i++) {
   Tmp = Tmp + npcNames[i] + Protocol.SEPARATOR;
  }
  
  if (vb6.Len(Tmp)) {
  Tmp = vb6.Left(Tmp, vb6.Len(Tmp)-1);
  }
  
  Declaraciones.UserList[UserIndex].outgoingData.WriteASCIIString(Tmp);
 return;
 
 /* FIXME: ErrHandler : */
  if (Err.Number == Declaraciones.UserList[UserIndex].outgoingData.NotEnoughSpaceErrCode) {
  FlushBuffer(UserIndex);
  /* FIXME: RESUME */
 }
}

	/* '' */
	/*
	 * ' Writes the "ShowSOSForm" message to the given user's outgoing data
	 * buffer.
	 */
	/* ' */
	/* ' @param UserIndex User to which the message is intended. */
	/*
	 * ' @remarks The data is not actually sent until the buffer is properly
	 * flushed.
	 */

	static void WriteShowSOSForm(int UserIndex) {
		/* '*************************************************** */
		/* 'Author: Juan Martín Sotuyo Dodero (Maraxus) */
		/* 'Last Modification: 05/17/06 */
		/*
		 * 'Writes the "ShowSOSForm" message to the given user's outgoing data
		 * buffer
		 */
		/* '*************************************************** */
		/* FIXME: ON ERROR GOTO ErrHandler */
		int i;
		String Tmp;

		Declaraciones.UserList[UserIndex].outgoingData.WriteByte(ServerPacketID.ShowSOSForm);

		for (i = (1); i <= (Declaraciones.Ayuda.Longitud); i++) {
			Tmp = Tmp + Declaraciones.Ayuda.VerElemento(i) + Protocol.SEPARATOR;
		}

		if (vb6.LenB(Tmp) != 0) {
			Tmp = vb6.Left(Tmp, vb6.Len(Tmp) - 1);
		}

		Declaraciones.UserList[UserIndex].outgoingData.WriteASCIIString(Tmp);
		return;

		/* FIXME: ErrHandler : */
		if (Err.Number == Declaraciones.UserList[UserIndex].outgoingData.NotEnoughSpaceErrCode) {
			FlushBuffer(UserIndex);
			/* FIXME: RESUME */
		}
	}

	/* '' */
	/*
	 * ' Writes the "ShowDenounces" message to the given user's outgoing data
	 * buffer.
	 */
	/* ' */
	/* ' @param UserIndex User to which the message is intended. */
	/*
	 * ' @remarks The data is not actually sent until the buffer is properly
	 * flushed.
	 */

	static void WriteShowDenounces(int UserIndex) {
		/* '*************************************************** */
		/* 'Author: ZaMa */
		/* 'Last Modification: 14/11/2010 */
		/*
		 * 'Writes the "ShowDenounces" message to the given user's outgoing data
		 * buffer
		 */
		/* '*************************************************** */
		/* FIXME: ON ERROR GOTO ErrHandler */

		int DenounceIndex;
		String DenounceList;

		Declaraciones.UserList[UserIndex].outgoingData.WriteByte(ServerPacketID.ShowDenounces);

		for (DenounceIndex = (1); DenounceIndex <= (Declaraciones.Denuncias.Longitud); DenounceIndex++) {
			DenounceList = DenounceList + Declaraciones.Denuncias.VerElemento(DenounceIndex, false)
					+ Protocol.SEPARATOR;
		}

		if (vb6.LenB(DenounceList) != 0) {
			DenounceList = vb6.Left(DenounceList, vb6.Len(DenounceList) - 1);
		}

		Declaraciones.UserList[UserIndex].outgoingData.WriteASCIIString(DenounceList);
		return;

		/* FIXME: ErrHandler : */
		if (Err.Number == Declaraciones.UserList[UserIndex].outgoingData.NotEnoughSpaceErrCode) {
			FlushBuffer(UserIndex);
			/* FIXME: RESUME */
		}
	}

	/* '' */
	/*
	 * ' Writes the "ShowSOSForm" message to the given user's outgoing data
	 * buffer.
	 */
	/* ' */
	/* ' @param UserIndex User to which the message is intended. */
	/*
	 * ' @remarks The data is not actually sent until the buffer is properly
	 * flushed.
	 */

	static void WriteShowPartyForm(int UserIndex) {
 /* '*************************************************** */
 /* 'Author: Budi */
 /* 'Last Modification: 11/26/09 */
 /* 'Writes the "ShowPartyForm" message to the given user's outgoing data buffer */
 /* '*************************************************** */
 /* FIXME: ON ERROR GOTO ErrHandler */
 int i;
 String Tmp;
 int PI;
 int[] members;
 
  Declaraciones.UserList[UserIndex].outgoingData.WriteByte(ServerPacketID.ShowPartyForm);
  
  PI = Declaraciones.UserList[UserIndex].PartyIndex;
  Declaraciones.UserList[UserIndex].outgoingData.WriteByte(vb6.CByte(Declaraciones.Parties[PI].EsPartyLeader(UserIndex)));
  
   if (PI>0) {
   Declaraciones.Parties[PI].ObtenerMiembrosOnline(members[]);
    for (i = (1); i <= (mdParty.PARTY_MAXMEMBERS); i++) {
     if (members[i]>0) {
     Tmp = Tmp + Declaraciones.UserList[members[i]].Name + " (" + vb6.Fix(Declaraciones.Parties[PI].MiExperiencia(members[i])) + ")" + Protocol.SEPARATOR;
    }
   }
  }
  
  if (vb6.LenB(Tmp) != 0) {
  Tmp = vb6.Left(Tmp, vb6.Len(Tmp)-1);
  }
  
  Declaraciones.UserList[UserIndex].outgoingData.WriteASCIIString(Tmp);
  Declaraciones.UserList[UserIndex].outgoingData.WriteLong(Declaraciones.Parties[PI].ObtenerExperienciaTotal());
 return;
 
 /* FIXME: ErrHandler : */
  if (Err.Number == Declaraciones.UserList[UserIndex].outgoingData.NotEnoughSpaceErrCode) {
  FlushBuffer(UserIndex);
  /* FIXME: RESUME */
 }
}

	/* '' */
	/*
	 * ' Writes the "ShowMOTDEditionForm" message to the given user's outgoing
	 * data buffer.
	 */
	/* ' */
	/* ' @param UserIndex User to which the message is intended. */
	/* ' @param currentMOTD The current Message Of The Day. */
	/*
	 * ' @remarks The data is not actually sent until the buffer is properly
	 * flushed.
	 */

	static void WriteShowMOTDEditionForm(int UserIndex, String currentMOTD) {
		/* '*************************************************** */
		/* 'Author: Juan Martín Sotuyo Dodero (Maraxus) */
		/* 'Last Modification: 05/17/06 */
		/*
		 * 'Writes the "ShowMOTDEditionForm" message to the given user's
		 * outgoing data buffer
		 */
		/* '*************************************************** */
		/* FIXME: ON ERROR GOTO ErrHandler */
		Declaraciones.UserList[UserIndex].outgoingData.WriteByte(ServerPacketID.ShowMOTDEditionForm);

		Declaraciones.UserList[UserIndex].outgoingData.WriteASCIIString(currentMOTD);
		return;

		/* FIXME: ErrHandler : */
		if (Err.Number == Declaraciones.UserList[UserIndex].outgoingData.NotEnoughSpaceErrCode) {
			FlushBuffer(UserIndex);
			/* FIXME: RESUME */
		}
	}

	/* '' */
	/*
	 * ' Writes the "ShowGMPanelForm" message to the given user's outgoing data
	 * buffer.
	 */
	/* ' */
	/* ' @param UserIndex User to which the message is intended. */
	/*
	 * ' @remarks The data is not actually sent until the buffer is properly
	 * flushed.
	 */

	static void WriteShowGMPanelForm(int UserIndex) {
		/* '*************************************************** */
		/* 'Author: Juan Martín Sotuyo Dodero (Maraxus) */
		/* 'Last Modification: 05/17/06 */
		/*
		 * 'Writes the "ShowGMPanelForm" message to the given user's outgoing
		 * data buffer
		 */
		/* '*************************************************** */
		/* FIXME: ON ERROR GOTO ErrHandler */
		Declaraciones.UserList[UserIndex].outgoingData.WriteByte(ServerPacketID.ShowGMPanelForm);
		return;

		/* FIXME: ErrHandler : */
		if (Err.Number == Declaraciones.UserList[UserIndex].outgoingData.NotEnoughSpaceErrCode) {
			FlushBuffer(UserIndex);
			/* FIXME: RESUME */
		}
	}

	/* '' */
	/*
	 * ' Writes the "UserNameList" message to the given user's outgoing data
	 * buffer.
	 */
	/* ' */
	/* ' @param UserIndex User to which the message is intended. */
	/* ' @param userNameList List of user names. */
	/* ' @param Cant Number of names to send. */
	/*
	 * ' @remarks The data is not actually sent until the buffer is properly
	 * flushed.
	 */

	static void WriteUserNameList(int UserIndex, String[] /* FIXME BYREF!! */ userNamesList, int cant) {
		/* '*************************************************** */
		/* 'Author: Juan Martín Sotuyo Dodero (Maraxus) */
		/* 'Last Modification: 05/17/06 NIGO: */
		/*
		 * 'Writes the "UserNameList" message to the given user's outgoing data
		 * buffer
		 */
		/* '*************************************************** */
		/* FIXME: ON ERROR GOTO ErrHandler */
		int i;
		String Tmp;

		Declaraciones.UserList[UserIndex].outgoingData.WriteByte(ServerPacketID.UserNameList);

		/* ' Prepare user's names list */
		for (i = (1); i <= (cant); i++) {
			Tmp = Tmp + userNamesList[i] + Protocol.SEPARATOR;
		}

		if (vb6.Len(Tmp)) {
			Tmp = vb6.Left(Tmp, vb6.Len(Tmp) - 1);
		}

		Declaraciones.UserList[UserIndex].outgoingData.WriteASCIIString(Tmp);
		return;

		/* FIXME: ErrHandler : */
		if (Err.Number == Declaraciones.UserList[UserIndex].outgoingData.NotEnoughSpaceErrCode) {
			FlushBuffer(UserIndex);
			/* FIXME: RESUME */
		}
	}

	/* '' */
	/* ' Writes the "Pong" message to the given user's outgoing data buffer. */
	/* ' */
	/* ' @param UserIndex User to which the message is intended. */
	/*
	 * ' @remarks The data is not actually sent until the buffer is properly
	 * flushed.
	 */

	static void WritePong(int UserIndex) {
		/* '*************************************************** */
		/* 'Author: Juan Martín Sotuyo Dodero (Maraxus) */
		/* 'Last Modification: 05/17/06 */
		/*
		 * 'Writes the "Pong" message to the given user's outgoing data buffer
		 */
		/* '*************************************************** */
		/* FIXME: ON ERROR GOTO ErrHandler */
		Declaraciones.UserList[UserIndex].outgoingData.WriteByte(ServerPacketID.Pong);
		return;

		/* FIXME: ErrHandler : */
		if (Err.Number == Declaraciones.UserList[UserIndex].outgoingData.NotEnoughSpaceErrCode) {
			FlushBuffer(UserIndex);
			/* FIXME: RESUME */
		}
	}

	/* '' */
	/* ' Flushes the outgoing data buffer of the user. */
	/* ' */
	/* ' @param UserIndex User whose outgoing data buffer will be flushed. */

	static void FlushBuffer(int UserIndex) {
		/* '*************************************************** */
		/* 'Author: Juan Martín Sotuyo Dodero (Maraxus) */
		/* 'Last Modification: 05/17/06 */
		/* 'Sends all data existing in the buffer */
		/* '*************************************************** */
		String sndData;

		if (Declaraciones.UserList[UserIndex].outgoingData.length == 0) {
			return;
		}

		sndData = Declaraciones.UserList[UserIndex].outgoingData
				.ReadASCIIStringFixed(Declaraciones.UserList[UserIndex].outgoingData.length);

		TCP.EnviarDatosASlot(UserIndex, sndData);
	}

	/* '' */
	/* ' Prepares the "SetInvisible" message and returns it. */
	/* ' */
	/* ' @param CharIndex The char turning visible / invisible. */
	/*
	 * ' @param invisible True if the char is no longer visible, False
	 * otherwise.
	 */
	/*
	 * ' @return The formated message ready to be writen as is on outgoing
	 * buffers.
	 */
	/*
	 * ' @remarks The message is written to no outgoing buffer, but only
	 * prepared in a single string to be easily sent to several clients.
	 */

	static String PrepareMessageSetInvisible(int CharIndex, boolean invisible) {
		String retval;
		/* '*************************************************** */
		/* 'Author: Juan Martín Sotuyo Dodero (Maraxus) */
		/* 'Last Modification: 05/17/06 */
		/* 'Prepares the "SetInvisible" message and returns it. */
		/* '*************************************************** */
		auxiliarBuffer.WriteByte(ServerPacketID.SetInvisible);

		auxiliarBuffer.WriteInteger(CharIndex);
		auxiliarBuffer.WriteBoolean(invisible);

		retval = auxiliarBuffer.ReadASCIIStringFixed(auxiliarBuffer.length);
		return retval;
	}

	static String PrepareMessageCharacterChangeNick(int CharIndex, String newNick) {
		String retval;
		/* '*************************************************** */
		/* 'Author: Budi */
		/* 'Last Modification: 07/23/09 */
		/* 'Prepares the "Change Nick" message and returns it. */
		/* '*************************************************** */
		auxiliarBuffer.WriteByte(ServerPacketID.CharacterChangeNick);

		auxiliarBuffer.WriteInteger(CharIndex);
		auxiliarBuffer.WriteASCIIString(newNick);

		retval = auxiliarBuffer.ReadASCIIStringFixed(auxiliarBuffer.length);
		return retval;
	}

	/* '' */
	/* ' Prepares the "ChatOverHead" message and returns it. */
	/* ' */
	/* ' @param Chat Text to be displayed over the char's head. */
	/*
	 * ' @param CharIndex The character uppon which the chat will be displayed.
	 */
	/* ' @param Color The color to be used when displaying the chat. */
	/*
	 * ' @return The formated message ready to be writen as is on outgoing
	 * buffers.
	 */
	/*
	 * ' @remarks The message is written to no outgoing buffer, but only
	 * prepared in a single string to be easily sent to several clients.
	 */

	static String PrepareMessageChatOverHead(String Chat, int CharIndex, int color) {
 String retval;
 /* '*************************************************** */
 /* 'Author: Juan Martín Sotuyo Dodero (Maraxus) */
 /* 'Last Modification: 05/17/06 */
 /* 'Prepares the "ChatOverHead" message and returns it. */
 /* '*************************************************** */
  auxiliarBuffer.WriteByte(ServerPacketID.ChatOverHead);
  auxiliarBuffer.WriteASCIIString(Chat);
  auxiliarBuffer.WriteInteger(CharIndex);
  
  /* ' Write rgb channels and save one byte from long :D */
  auxiliarBuffer.WriteByte(color && 0xFF);
  auxiliarBuffer.WriteByte((color && 0xFF00 + )/0x100 + );
  auxiliarBuffer.WriteByte((color && 0xFF0000)/0x10000);
  
  retval = auxiliarBuffer.ReadASCIIStringFixed(auxiliarBuffer.length);
return retval;
}

	/* '' */
	/* ' Prepares the "ConsoleMsg" message and returns it. */
	/* ' */
	/* ' @param Chat Text to be displayed over the char's head. */
	/* ' @param FontIndex Index of the FONTTYPE structure to use. */
	/*
	 * ' @return The formated message ready to be writen as is on outgoing
	 * buffers.
	 */
	/*
	 * ' @remarks The data is not actually sent until the buffer is properly
	 * flushed.
	 */

	static String PrepareMessageConsoleMsg(String Chat, FontTypeNames FontIndex) {
		String retval;
		/* '*************************************************** */
		/* 'Author: Juan Martín Sotuyo Dodero (Maraxus) */
		/* 'Last Modification: 05/17/06 */
		/* 'Prepares the "ConsoleMsg" message and returns it. */
		/* '*************************************************** */
		auxiliarBuffer.WriteByte(ServerPacketID.ConsoleMsg);
		auxiliarBuffer.WriteASCIIString(Chat);
		auxiliarBuffer.WriteByte(FontIndex);

		retval = auxiliarBuffer.ReadASCIIStringFixed(auxiliarBuffer.length);
		return retval;
	}

	static String PrepareCommerceConsoleMsg(String /* FIXME BYREF!! */ Chat, FontTypeNames FontIndex) {
		String retval;
		/* '*************************************************** */
		/* 'Author: ZaMa */
		/* 'Last Modification: 03/12/2009 */
		/* 'Prepares the "CommerceConsoleMsg" message and returns it. */
		/* '*************************************************** */
		auxiliarBuffer.WriteByte(ServerPacketID.CommerceChat);
		auxiliarBuffer.WriteASCIIString(Chat);
		auxiliarBuffer.WriteByte(FontIndex);

		retval = auxiliarBuffer.ReadASCIIStringFixed(auxiliarBuffer.length);
		return retval;
	}

	/* '' */
	/* ' Prepares the "CreateFX" message and returns it. */
	/* ' */
	/* ' @param UserIndex User to which the message is intended. */
	/* ' @param CharIndex Character upon which the FX will be created. */
	/* ' @param FX FX index to be displayed over the new character. */
	/* ' @param FXLoops Number of times the FX should be rendered. */
	/*
	 * ' @return The formated message ready to be writen as is on outgoing
	 * buffers.
	 */
	/*
	 * ' @remarks The data is not actually sent until the buffer is properly
	 * flushed.
	 */

	static String PrepareMessageCreateFX(int CharIndex, int FX, int FXLoops) {
		String retval;
		/* '*************************************************** */
		/* 'Author: Juan Martín Sotuyo Dodero (Maraxus) */
		/* 'Last Modification: 05/17/06 */
		/* 'Prepares the "CreateFX" message and returns it */
		/* '*************************************************** */
		auxiliarBuffer.WriteByte(ServerPacketID.CreateFX);
		auxiliarBuffer.WriteInteger(CharIndex);
		auxiliarBuffer.WriteInteger(FX);
		auxiliarBuffer.WriteInteger(FXLoops);

		retval = auxiliarBuffer.ReadASCIIStringFixed(auxiliarBuffer.length);
		return retval;
	}

	/* '' */
	/* ' Prepares the "PlayWave" message and returns it. */
	/* ' */
	/* ' @param wave The wave to be played. */
	/*
	 * ' @param X The X position in map coordinates from where the sound comes.
	 */
	/*
	 * ' @param Y The Y position in map coordinates from where the sound comes.
	 */
	/*
	 * ' @return The formated message ready to be writen as is on outgoing
	 * buffers.
	 */
	/*
	 * ' @remarks The data is not actually sent until the buffer is properly
	 * flushed.
	 */

	static String PrepareMessagePlayWave(int wave, int X, int Y) {
		String retval;
		/* '*************************************************** */
		/* 'Author: Juan Martín Sotuyo Dodero (Maraxus) */
		/* 'Last Modification: 08/08/07 */
		/* 'Last Modified by: Rapsodius */
		/* 'Added X and Y positions for 3D Sounds */
		/* '*************************************************** */
		auxiliarBuffer.WriteByte(ServerPacketID.PlayWave);
		auxiliarBuffer.WriteByte(wave);
		auxiliarBuffer.WriteByte(X);
		auxiliarBuffer.WriteByte(Y);

		retval = auxiliarBuffer.ReadASCIIStringFixed(auxiliarBuffer.length);
		return retval;
	}

	/* '' */
	/* ' Prepares the "GuildChat" message and returns it. */
	/* ' */
	/* ' @param Chat Text to be displayed over the char's head. */
	/*
	 * ' @return The formated message ready to be writen as is on outgoing
	 * buffers.
	 */
	/*
	 * ' @remarks The data is not actually sent until the buffer is properly
	 * flushed.
	 */

	static String PrepareMessageGuildChat(String Chat) {
		String retval;
		/* '*************************************************** */
		/* 'Author: Juan Martín Sotuyo Dodero (Maraxus) */
		/* 'Last Modification: 05/17/06 */
		/* 'Prepares the "GuildChat" message and returns it */
		/* '*************************************************** */
		auxiliarBuffer.WriteByte(ServerPacketID.GuildChat);
		auxiliarBuffer.WriteASCIIString(Chat);

		retval = auxiliarBuffer.ReadASCIIStringFixed(auxiliarBuffer.length);
		return retval;
	}

	/* '' */
	/* ' Prepares the "ShowMessageBox" message and returns it. */
	/* ' */
	/* ' @param Message Text to be displayed in the message box. */
	/*
	 * ' @return The formated message ready to be writen as is on outgoing
	 * buffers.
	 */
	/*
	 * ' @remarks The data is not actually sent until the buffer is properly
	 * flushed.
	 */

	static String PrepareMessageShowMessageBox(String Chat) {
		String retval;
		/* '*************************************************** */
		/* 'Author: Fredy Horacio Treboux (liquid) */
		/* 'Last Modification: 01/08/07 */
		/* 'Prepares the "ShowMessageBox" message and returns it */
		/* '*************************************************** */
		auxiliarBuffer.WriteByte(ServerPacketID.ShowMessageBox);
		auxiliarBuffer.WriteASCIIString(Chat);

		retval = auxiliarBuffer.ReadASCIIStringFixed(auxiliarBuffer.length);
		return retval;
	}

	/* '' */
	/* ' Prepares the "PlayMidi" message and returns it. */
	/* ' */
	/* ' @param midi The midi to be played. */
	/* ' @param loops Number of repets for the midi. */
	/*
	 * ' @return The formated message ready to be writen as is on outgoing
	 * buffers.
	 */
	/*
	 * ' @remarks The data is not actually sent until the buffer is properly
	 * flushed.
	 */

	static String PrepareMessagePlayMidi(int midi) {
		return PrepareMessagePlayMidi(midi, -1);
	}

	static String PrepareMessagePlayMidi(int midi, int loops) {
		String retval;
		/* '*************************************************** */
		/* 'Author: Juan Martín Sotuyo Dodero (Maraxus) */
		/* 'Last Modification: 05/17/06 */
		/* 'Prepares the "GuildChat" message and returns it */
		/* '*************************************************** */
		auxiliarBuffer.WriteByte(ServerPacketID.PlayMidi);
		auxiliarBuffer.WriteInteger(midi);
		auxiliarBuffer.WriteInteger(loops);

		retval = auxiliarBuffer.ReadASCIIStringFixed(auxiliarBuffer.length);
		return retval;
	}

	/* '' */
	/* ' Prepares the "PauseToggle" message and returns it. */
	/* ' */
	/*
	 * ' @return The formated message ready to be writen as is on outgoing
	 * buffers.
	 */
	/*
	 * ' @remarks The data is not actually sent until the buffer is properly
	 * flushed.
	 */

	static String PrepareMessagePauseToggle() {
		String retval;
		/* '*************************************************** */
		/* 'Author: Juan Martín Sotuyo Dodero (Maraxus) */
		/* 'Last Modification: 05/17/06 */
		/* 'Prepares the "PauseToggle" message and returns it */
		/* '*************************************************** */
		auxiliarBuffer.WriteByte(ServerPacketID.PauseToggle);
		retval = auxiliarBuffer.ReadASCIIStringFixed(auxiliarBuffer.length);
		return retval;
	}

	/* '' */
	/* ' Prepares the "RainToggle" message and returns it. */
	/* ' */
	/*
	 * ' @return The formated message ready to be writen as is on outgoing
	 * buffers.
	 */
	/*
	 * ' @remarks The data is not actually sent until the buffer is properly
	 * flushed.
	 */

	static String PrepareMessageRainToggle() {
		String retval;
		/* '*************************************************** */
		/* 'Author: Juan Martín Sotuyo Dodero (Maraxus) */
		/* 'Last Modification: 05/17/06 */
		/* 'Prepares the "RainToggle" message and returns it */
		/* '*************************************************** */
		auxiliarBuffer.WriteByte(ServerPacketID.RainToggle);

		retval = auxiliarBuffer.ReadASCIIStringFixed(auxiliarBuffer.length);
		return retval;
	}

	/* '' */
	/* ' Prepares the "ObjectDelete" message and returns it. */
	/* ' */
	/* ' @param X X coord of the character's new position. */
	/* ' @param Y Y coord of the character's new position. */
	/*
	 * ' @return The formated message ready to be writen as is on outgoing
	 * buffers.
	 */
	/*
	 * ' @remarks The data is not actually sent until the buffer is properly
	 * flushed.
	 */

	static String PrepareMessageObjectDelete(int X, int Y) {
		String retval;
		/* '*************************************************** */
		/* 'Author: Juan Martín Sotuyo Dodero (Maraxus) */
		/* 'Last Modification: 05/17/06 */
		/* 'Prepares the "ObjectDelete" message and returns it */
		/* '*************************************************** */
		auxiliarBuffer.WriteByte(ServerPacketID.ObjectDelete);
		auxiliarBuffer.WriteByte(X);
		auxiliarBuffer.WriteByte(Y);

		retval = auxiliarBuffer.ReadASCIIStringFixed(auxiliarBuffer.length);
		return retval;
	}

	/* '' */
	/* ' Prepares the "BlockPosition" message and returns it. */
	/* ' */
	/* ' @param X X coord of the tile to block/unblock. */
	/* ' @param Y Y coord of the tile to block/unblock. */
	/* ' @param Blocked Blocked status of the tile */
	/*
	 * ' @return The formated message ready to be writen as is on outgoing
	 * buffers.
	 */
	/*
	 * ' @remarks The data is not actually sent until the buffer is properly
	 * flushed.
	 */

	static String PrepareMessageBlockPosition(int X, int Y, boolean Blocked) {
		String retval;
		/* '*************************************************** */
		/* 'Author: Fredy Horacio Treboux (liquid) */
		/* 'Last Modification: 01/08/07 */
		/* 'Prepares the "BlockPosition" message and returns it */
		/* '*************************************************** */
		auxiliarBuffer.WriteByte(ServerPacketID.BlockPosition);
		auxiliarBuffer.WriteByte(X);
		auxiliarBuffer.WriteByte(Y);
		auxiliarBuffer.WriteBoolean(Blocked);

		retval = auxiliarBuffer.ReadASCIIStringFixed(auxiliarBuffer.length);

		return retval;
	}

	/* '' */
	/* ' Prepares the "ObjectCreate" message and returns it. */
	/* ' */
	/* ' @param GrhIndex Grh of the object. */
	/* ' @param X X coord of the character's new position. */
	/* ' @param Y Y coord of the character's new position. */
	/*
	 * ' @return The formated message ready to be writen as is on outgoing
	 * buffers.
	 */
	/*
	 * ' @remarks The data is not actually sent until the buffer is properly
	 * flushed.
	 */

	static String PrepareMessageObjectCreate(int GrhIndex, int X, int Y) {
		String retval;
		/* '*************************************************** */
		/* 'Author: Juan Martín Sotuyo Dodero (Maraxus) */
		/* 'Last Modification: 05/17/06 */
		/* 'prepares the "ObjectCreate" message and returns it */
		/* '*************************************************** */
		auxiliarBuffer.WriteByte(ServerPacketID.ObjectCreate);
		auxiliarBuffer.WriteByte(X);
		auxiliarBuffer.WriteByte(Y);
		auxiliarBuffer.WriteInteger(GrhIndex);

		retval = auxiliarBuffer.ReadASCIIStringFixed(auxiliarBuffer.length);
		return retval;
	}

	/* '' */
	/* ' Prepares the "CharacterRemove" message and returns it. */
	/* ' */
	/* ' @param CharIndex Character to be removed. */
	/*
	 * ' @return The formated message ready to be writen as is on outgoing
	 * buffers.
	 */
	/*
	 * ' @remarks The data is not actually sent until the buffer is properly
	 * flushed.
	 */

	static String PrepareMessageCharacterRemove(int CharIndex) {
		String retval;
		/* '*************************************************** */
		/* 'Author: Juan Martín Sotuyo Dodero (Maraxus) */
		/* 'Last Modification: 05/17/06 */
		/* 'Prepares the "CharacterRemove" message and returns it */
		/* '*************************************************** */
		auxiliarBuffer.WriteByte(ServerPacketID.CharacterRemove);
		auxiliarBuffer.WriteInteger(CharIndex);

		retval = auxiliarBuffer.ReadASCIIStringFixed(auxiliarBuffer.length);
		return retval;
	}

	/* '' */
	/* ' Prepares the "RemoveCharDialog" message and returns it. */
	/* ' */
	/* ' @param CharIndex Character whose dialog will be removed. */
	/*
	 * ' @return The formated message ready to be writen as is on outgoing
	 * buffers.
	 */
	/*
	 * ' @remarks The data is not actually sent until the buffer is properly
	 * flushed.
	 */

	static String PrepareMessageRemoveCharDialog(int CharIndex) {
		String retval;
		/* '*************************************************** */
		/* 'Author: Juan Martín Sotuyo Dodero (Maraxus) */
		/* 'Last Modification: 05/17/06 */
		/*
		 * 'Writes the "RemoveCharDialog" message to the given user's outgoing
		 * data buffer
		 */
		/* '*************************************************** */
		auxiliarBuffer.WriteByte(ServerPacketID.RemoveCharDialog);
		auxiliarBuffer.WriteInteger(CharIndex);

		retval = auxiliarBuffer.ReadASCIIStringFixed(auxiliarBuffer.length);
		return retval;
	}

	/* '' */
	/*
	 * ' Writes the "CharacterCreate" message to the given user's outgoing data
	 * buffer.
	 */
	/* ' */
	/* ' @param body Body index of the new character. */
	/* ' @param head Head index of the new character. */
	/* ' @param heading Heading in which the new character is looking. */
	/* ' @param CharIndex The index of the new character. */
	/* ' @param X X coord of the new character's position. */
	/* ' @param Y Y coord of the new character's position. */
	/* ' @param weapon Weapon index of the new character. */
	/* ' @param shield Shield index of the new character. */
	/* ' @param FX FX index to be displayed over the new character. */
	/* ' @param FXLoops Number of times the FX should be rendered. */
	/* ' @param helmet Helmet index of the new character. */
	/* ' @param name Name of the new character. */
	/*
	 * ' @param NickColor Determines if the character is a criminal or not, and
	 * if can be atacked by someone
	 */
	/*
	 * ' @param privileges Sets if the character is a normal one or any kind of
	 * administrative character.
	 */
	/*
	 * ' @return The formated message ready to be writen as is on outgoing
	 * buffers.
	 */
	/*
	 * ' @remarks The data is not actually sent until the buffer is properly
	 * flushed.
	 */

	static String PrepareMessageCharacterCreate(int body, int Head, eHeading heading, int CharIndex, int X, int Y,
			int weapon, int shield, int FX, int FXLoops, int helmet, String Name, int NickColor, int Privileges) {
		String retval;
		/* '*************************************************** */
		/* 'Author: Juan Martín Sotuyo Dodero (Maraxus) */
		/* 'Last Modification: 05/17/06 */
		/* 'Prepares the "CharacterCreate" message and returns it */
		/* '*************************************************** */
		auxiliarBuffer.WriteByte(ServerPacketID.CharacterCreate);

		auxiliarBuffer.WriteInteger(CharIndex);
		auxiliarBuffer.WriteInteger(body);
		auxiliarBuffer.WriteInteger(Head);
		auxiliarBuffer.WriteByte(heading);
		auxiliarBuffer.WriteByte(X);
		auxiliarBuffer.WriteByte(Y);
		auxiliarBuffer.WriteInteger(weapon);
		auxiliarBuffer.WriteInteger(shield);
		auxiliarBuffer.WriteInteger(helmet);
		auxiliarBuffer.WriteInteger(FX);
		auxiliarBuffer.WriteInteger(FXLoops);
		auxiliarBuffer.WriteASCIIString(Name);
		auxiliarBuffer.WriteByte(NickColor);
		auxiliarBuffer.WriteByte(Privileges);

		retval = auxiliarBuffer.ReadASCIIStringFixed(auxiliarBuffer.length);
		return retval;
	}

	/* '' */
	/* ' Prepares the "CharacterChange" message and returns it. */
	/* ' */
	/* ' @param body Body index of the new character. */
	/* ' @param head Head index of the new character. */
	/* ' @param heading Heading in which the new character is looking. */
	/* ' @param CharIndex The index of the new character. */
	/* ' @param weapon Weapon index of the new character. */
	/* ' @param shield Shield index of the new character. */
	/* ' @param FX FX index to be displayed over the new character. */
	/* ' @param FXLoops Number of times the FX should be rendered. */
	/* ' @param helmet Helmet index of the new character. */
	/*
	 * ' @return The formated message ready to be writen as is on outgoing
	 * buffers.
	 */
	/*
	 * ' @remarks The data is not actually sent until the buffer is properly
	 * flushed.
	 */

	static String PrepareMessageCharacterChange(int body, int Head, eHeading heading, int CharIndex, int weapon,
			int shield, int FX, int FXLoops, int helmet) {
		String retval;
		/* '*************************************************** */
		/* 'Author: Juan Martín Sotuyo Dodero (Maraxus) */
		/* 'Last Modification: 05/17/06 */
		/* 'Prepares the "CharacterChange" message and returns it */
		/* '*************************************************** */
		auxiliarBuffer.WriteByte(ServerPacketID.CharacterChange);

		auxiliarBuffer.WriteInteger(CharIndex);
		auxiliarBuffer.WriteInteger(body);
		auxiliarBuffer.WriteInteger(Head);
		auxiliarBuffer.WriteByte(heading);
		auxiliarBuffer.WriteInteger(weapon);
		auxiliarBuffer.WriteInteger(shield);
		auxiliarBuffer.WriteInteger(helmet);
		auxiliarBuffer.WriteInteger(FX);
		auxiliarBuffer.WriteInteger(FXLoops);

		retval = auxiliarBuffer.ReadASCIIStringFixed(auxiliarBuffer.length);
		return retval;
	}

	/* '' */
	/* ' Prepares the "CharacterMove" message and returns it. */
	/* ' */
	/* ' @param CharIndex Character which is moving. */
	/* ' @param X X coord of the character's new position. */
	/* ' @param Y Y coord of the character's new position. */
	/*
	 * ' @return The formated message ready to be writen as is on outgoing
	 * buffers.
	 */
	/*
	 * ' @remarks The data is not actually sent until the buffer is properly
	 * flushed.
	 */

	static String PrepareMessageCharacterMove(int CharIndex, int X, int Y) {
		String retval;
		/* '*************************************************** */
		/* 'Author: Juan Martín Sotuyo Dodero (Maraxus) */
		/* 'Last Modification: 05/17/06 */
		/* 'Prepares the "CharacterMove" message and returns it */
		/* '*************************************************** */
		auxiliarBuffer.WriteByte(ServerPacketID.CharacterMove);
		auxiliarBuffer.WriteInteger(CharIndex);
		auxiliarBuffer.WriteByte(X);
		auxiliarBuffer.WriteByte(Y);

		retval = auxiliarBuffer.ReadASCIIStringFixed(auxiliarBuffer.length);
		return retval;
	}

	static String PrepareMessageForceCharMove(eHeading Direccion) {
		String retval;
		/* '*************************************************** */
		/* 'Author: ZaMa */
		/* 'Last Modification: 26/03/2009 */
		/* 'Prepares the "ForceCharMove" message and returns it */
		/* '*************************************************** */
		auxiliarBuffer.WriteByte(ServerPacketID.ForceCharMove);
		auxiliarBuffer.WriteByte(Direccion);

		retval = auxiliarBuffer.ReadASCIIStringFixed(auxiliarBuffer.length);
		return retval;
	}

	/* '' */
	/* ' Prepares the "UpdateTagAndStatus" message and returns it. */
	/* ' */
	/* ' @param CharIndex Character which is moving. */
	/* ' @param X X coord of the character's new position. */
	/* ' @param Y Y coord of the character's new position. */
	/*
	 * ' @return The formated message ready to be writen as is on outgoing
	 * buffers.
	 */
	/*
	 * ' @remarks The data is not actually sent until the buffer is properly
	 * flushed.
	 */

	static String PrepareMessageUpdateTagAndStatus(int UserIndex, int NickColor,
			String /* FIXME BYREF!! */ Tag) {
		String retval;
		/* '*************************************************** */
		/* 'Author: Alejandro Salvo (Salvito) */
		/* 'Last Modification: 04/07/07 */
		/* 'Last Modified By: Juan Martín Sotuyo Dodero (Maraxus) */
		/* 'Prepares the "UpdateTagAndStatus" message and returns it */
		/*
		 * '15/01/2010: ZaMa - Now sends the nick color instead of the status.
		 */
		/* '*************************************************** */
		auxiliarBuffer.WriteByte(ServerPacketID.UpdateTagAndStatus);

		auxiliarBuffer.WriteInteger(Declaraciones.UserList[UserIndex].Char.CharIndex);
		auxiliarBuffer.WriteByte(NickColor);
		auxiliarBuffer.WriteASCIIString(Tag);

		retval = auxiliarBuffer.ReadASCIIStringFixed(auxiliarBuffer.length);
		return retval;
	}

	/* '' */
	/* ' Prepares the "ErrorMsg" message and returns it. */
	/* ' */
	/* ' @param message The error message to be displayed. */
	/*
	 * ' @remarks The data is not actually sent until the buffer is properly
	 * flushed.
	 */

	static String PrepareMessageErrorMsg(String message) {
		String retval;
		/* '*************************************************** */
		/* 'Author: Juan Martín Sotuyo Dodero (Maraxus) */
		/* 'Last Modification: 05/17/06 */
		/* 'Prepares the "ErrorMsg" message and returns it */
		/* '*************************************************** */
		auxiliarBuffer.WriteByte(ServerPacketID.ErrorMsg);
		auxiliarBuffer.WriteASCIIString(message);

		retval = auxiliarBuffer.ReadASCIIStringFixed(auxiliarBuffer.length);
		return retval;
	}

	/* '' */
	/*
	 * ' Writes the "StopWorking" message to the given user's outgoing data
	 * buffer.
	 */
	/* ' */
	/* ' @param UserIndex User to which the message is intended. */

	static void WriteStopWorking(int UserIndex) {
		/* '*************************************************** */
		/* 'Author: ZaMa */
		/* 'Last Modification: 21/02/2010 */
		/* ' */
		/* '*************************************************** */
		/* FIXME: ON ERROR GOTO ErrHandler */

		Declaraciones.UserList[UserIndex].outgoingData.WriteByte(ServerPacketID.StopWorking);

		return;

		/* FIXME: ErrHandler : */
		if (Err.Number == Declaraciones.UserList[UserIndex].outgoingData.NotEnoughSpaceErrCode) {
			FlushBuffer(UserIndex);
			/* FIXME: RESUME */
		}
	}

	/* '' */
	/*
	 * ' Writes the "CancelOfferItem" message to the given user's outgoing data
	 * buffer.
	 */
	/* ' */
	/* ' @param UserIndex User to which the message is intended. */
	/* ' @param Slot The slot to cancel. */

	static void WriteCancelOfferItem(int UserIndex, int Slot) {
		/* '*************************************************** */
		/* 'Author: Torres Patricio (Pato) */
		/* 'Last Modification: 05/03/2010 */
		/* ' */
		/* '*************************************************** */
		/* FIXME: ON ERROR GOTO ErrHandler */
		Declaraciones.UserList[UserIndex].outgoingData.WriteByte(ServerPacketID.CancelOfferItem);
		Declaraciones.UserList[UserIndex].outgoingData.WriteByte(Slot);

		return;

		/* FIXME: ErrHandler : */
		if (Err.Number == Declaraciones.UserList[UserIndex].outgoingData.NotEnoughSpaceErrCode) {
			FlushBuffer(UserIndex);
			/* FIXME: RESUME */
		}
	}

	/* '' */
	/* ' Handles the "SetDialog" message. */
	/* ' */
	/* ' @param UserIndex The index of the user sending the message */

	static void HandleSetDialog(int UserIndex) {
		/* '*************************************************** */
		/* 'Author: Amraphen */
		/* 'Last Modification: 18/11/2010 */
		/* '20/11/2010: ZaMa - Arreglo privilegios. */
		/* '*************************************************** */
		if (Declaraciones.UserList[UserIndex].incomingData.length < 2) {
			throw new RuntimeException("Err . Raise UserList ( UserIndex ) . incomingData . NotEnoughDataErrCode");
			return;
		}

		/* FIXME: ON ERROR GOTO ErrHandler */

		/*
		 * 'This packet contains strings, make a copy of the data to prevent
		 * losses if it's not complete yet...
		 */
		clsByteQueue buffer;
		buffer = new clsByteQueue();
		buffer.CopyBuffer(Declaraciones.UserList[UserIndex].incomingData);

		/* 'Remove packet id */
		buffer.ReadByte();

		String NewDialog;
		NewDialog = buffer.ReadASCIIString();

		Declaraciones.UserList[UserIndex].incomingData.CopyBuffer(buffer);

		if (Declaraciones.UserList[UserIndex].flags.TargetNPC > 0) {
			/* ' Dsgm/Dsrm/Rm */
			if (!((Declaraciones.UserList[UserIndex].flags.Privilegios && PlayerType.Dios) == 0
					&& (Declaraciones.UserList[UserIndex].flags.Privilegios
							&& (PlayerType.SemiDios || PlayerType.RoleMaster)) != (PlayerType.SemiDios
									|| PlayerType.RoleMaster))) {
				/* 'Replace the NPC's dialog. */
				Declaraciones.Npclist[Declaraciones.UserList[UserIndex].flags.TargetNPC].desc = NewDialog;
			}
		}

		/* FIXME: ErrHandler : */
		int ERROR;
		/* ERROR = Err . Number */
		/* FIXME: ON ERROR GOTO 0 */

		/* 'Destroy auxiliar buffer */
		buffer = null;

		if (ERROR != 0) {
			throw new RuntimeException("Err . Raise ERROR");
		}
	}

	/* '' */
	/* ' Handles the "Impersonate" message. */
	/* ' */
	/* ' @param userIndex The index of the user sending the message. */

	static void HandleImpersonate(int UserIndex) {
		/* '*************************************************** */
		/* 'Author: ZaMa */
		/* 'Last Modification: 20/11/2010 */
		/* ' */
		/* '*************************************************** */

		/* 'Remove packet ID */
		Declaraciones.UserList[UserIndex].incomingData.ReadByte();

		/* ' Dsgm/Dsrm/Rm */
		if ((Declaraciones.UserList[UserIndex].flags.Privilegios && PlayerType.Dios) == 0
				&& (Declaraciones.UserList[UserIndex].flags.Privilegios
						&& (PlayerType.SemiDios || PlayerType.RoleMaster)) != (PlayerType.SemiDios
								|| PlayerType.RoleMaster)) {
			return;
		}

		int NpcIndex;
		NpcIndex = Declaraciones.UserList[UserIndex].flags.TargetNPC;

		if (NpcIndex == 0) {
			return;
		}

		/* ' Copy head, body and desc */
		Trabajo.ImitateNpc(UserIndex, NpcIndex);

		/* ' Teleports user to npc's coords */
		UsUaRiOs.WarpUserChar(UserIndex, Declaraciones.Npclist[NpcIndex].Pos.Map, Declaraciones.Npclist[NpcIndex].Pos.X,
				Declaraciones.Npclist[NpcIndex].Pos.Y, false, true);

		/* ' Log gm */
		General.LogGM(Declaraciones.UserList[UserIndex].Name, "/IMPERSONAR con " + Declaraciones.Npclist[NpcIndex].Name
				+ " en mapa " + Declaraciones.UserList[UserIndex].Pos.Map);

		/* ' Remove npc */
		NPCs.QuitarNPC(NpcIndex);

	}

	/* '' */
	/* ' Handles the "Imitate" message. */
	/* ' */
	/* ' @param userIndex The index of the user sending the message. */

	static void HandleImitate(int UserIndex) {
		/* '*************************************************** */
		/* 'Author: ZaMa */
		/* 'Last Modification: 20/11/2010 */
		/* ' */
		/* '*************************************************** */

		/* 'Remove packet ID */
		Declaraciones.UserList[UserIndex].incomingData.ReadByte();

		/* ' Dsgm/Dsrm/Rm/ConseRm */
		if ((Declaraciones.UserList[UserIndex].flags.Privilegios && PlayerType.Dios) == 0
				&& (Declaraciones.UserList[UserIndex].flags.Privilegios
						&& (PlayerType.SemiDios || PlayerType.RoleMaster)) != (PlayerType.SemiDios
								|| PlayerType.RoleMaster)
				&& (Declaraciones.UserList[UserIndex].flags.Privilegios
						&& (PlayerType.Consejero || PlayerType.RoleMaster)) != (PlayerType.Consejero
								|| PlayerType.RoleMaster)) {
			return;
		}

		int NpcIndex;
		NpcIndex = Declaraciones.UserList[UserIndex].flags.TargetNPC;

		if (NpcIndex == 0) {
			return;
		}

		/* ' Copy head, body and desc */
		Trabajo.ImitateNpc(UserIndex, NpcIndex);
		General.LogGM(Declaraciones.UserList[UserIndex].Name, "/MIMETIZAR con " + Declaraciones.Npclist[NpcIndex].Name
				+ " en mapa " + Declaraciones.UserList[UserIndex].Pos.Map);

	}

	/* '' */
	/* ' Handles the "RecordAdd" message. */
	/* ' */
	/* ' @param UserIndex The index of the user sending the message */

	static void HandleRecordAdd(int UserIndex) {
		/* '************************************************************** */
		/* 'Author: Amraphen */
		/* 'Last Modify Date: 29/11/2010 */
		/* ' */
		/* '************************************************************** */
		if (Declaraciones.UserList[UserIndex].incomingData.length < 2) {
			throw new RuntimeException("Err . Raise UserList ( UserIndex ) . incomingData . NotEnoughDataErrCode");
			return;
		}

		/* FIXME: ON ERROR GOTO ErrHandler */

		/*
		 * 'This packet contains strings, make a copy of the data to prevent
		 * losses if it's not complete yet...
		 */
		clsByteQueue buffer;
		buffer = new clsByteQueue();
		buffer.CopyBuffer(Declaraciones.UserList[UserIndex].incomingData);

		/* 'Remove packet id */
		buffer.ReadByte();

		String UserName;
		String Reason;

		UserName = buffer.ReadASCIIString();
		Reason = buffer.ReadASCIIString();

		if (!(Declaraciones.UserList[UserIndex].flags.Privilegios
				&& (PlayerType.User || PlayerType.Consejero || PlayerType.RoleMaster))) {
			/* 'Verificamos que exista el personaje */
			if (!General.FileExist(Declaraciones.CharPath + vb6.UCase(UserName) + ".chr")) {
				WriteShowMessageBox(UserIndex, "El personaje no existe");
			} else {
				/* 'Agregamos el seguimiento */
				modUserRecords.AddRecord(UserIndex, UserName, Reason);

				/* 'Enviamos la nueva lista de personajes */
				WriteRecordList(UserIndex);
			}
		}

		Declaraciones.UserList[UserIndex].incomingData.CopyBuffer(buffer);

		/* FIXME: ErrHandler : */
		int ERROR;
		/* ERROR = Err . Number */
		/* FIXME: ON ERROR GOTO 0 */

		/* 'Destroy auxiliar buffer */
		buffer = null;

		if (ERROR != 0) {
			throw new RuntimeException("Err . Raise ERROR");
		}
	}

	/* '' */
	/* ' Handles the "RecordAddObs" message. */
	/* ' */
	/* ' @param UserIndex The index of the user sending the message. */

	static void HandleRecordAddObs(int UserIndex) {
		/* '************************************************************** */
		/* 'Author: Amraphen */
		/* 'Last Modify Date: 29/11/2010 */
		/* ' */
		/* '************************************************************** */
		if (Declaraciones.UserList[UserIndex].incomingData.length < 3) {
			throw new RuntimeException("Err . Raise UserList ( UserIndex ) . incomingData . NotEnoughDataErrCode");
			return;
		}

		/* FIXME: ON ERROR GOTO ErrHandler */

		/*
		 * 'This packet contains strings, make a copy of the data to prevent
		 * losses if it's not complete yet...
		 */
		clsByteQueue buffer;
		buffer = new clsByteQueue();
		buffer.CopyBuffer(Declaraciones.UserList[UserIndex].incomingData);

		/* 'Remove packet id */
		buffer.ReadByte();

		int RecordIndex;
		String Obs;

		RecordIndex = buffer.ReadByte();
		Obs = buffer.ReadASCIIString();

		if (!(Declaraciones.UserList[UserIndex].flags.Privilegios
				&& (PlayerType.User || PlayerType.Consejero || PlayerType.RoleMaster))) {
			/* 'Agregamos la observación */
			modUserRecords.AddObs(UserIndex, RecordIndex, Obs);

			/* 'Actualizamos la información */
			WriteRecordDetails(UserIndex, RecordIndex);
		}

		Declaraciones.UserList[UserIndex].incomingData.CopyBuffer(buffer);

		/* FIXME: ErrHandler : */
		int ERROR;
		/* ERROR = Err . Number */
		/* FIXME: ON ERROR GOTO 0 */

		/* 'Destroy auxiliar buffer */
		buffer = null;

		if (ERROR != 0) {
			throw new RuntimeException("Err . Raise ERROR");
		}
	}

	/* '' */
	/* ' Handles the "RecordRemove" message. */
	/* ' */
	/* ' @param UserIndex The index of the user sending the message. */

	static void HandleRecordRemove(int UserIndex) {
		/* '*************************************************** */
		/* 'Author: Amraphen */
		/* 'Last Modification: 29/11/2010 */
		/* ' */
		/* '*************************************************** */
		int RecordIndex;

		/* 'Remove packet ID */
		Declaraciones.UserList[UserIndex].incomingData.ReadByte();

		RecordIndex = Declaraciones.UserList[UserIndex].incomingData.ReadByte();

		if (Declaraciones.UserList[UserIndex].flags.Privilegios
				&& (PlayerType.User || PlayerType.Consejero || PlayerType.RoleMaster)) {
			return;
		}

		/*
		 * 'Sólo dioses pueden remover los seguimientos, los otros reciben una
		 * advertencia:
		 */
		if ((Declaraciones.UserList[UserIndex].flags.Privilegios && PlayerType.Dios)) {
			modUserRecords.RemoveRecord(RecordIndex);
			WriteShowMessageBox(UserIndex, "Se ha eliminado el seguimiento.");
			WriteRecordList(UserIndex);
		} else {
			WriteShowMessageBox(UserIndex, "Sólo los dioses pueden eliminar seguimientos.");
		}
	}

	/* '' */
	/* ' Handles the "RecordListRequest" message. */
	/* ' */
	/* ' @param UserIndex The index of the user sending the message. */

	static void HandleRecordListRequest(int UserIndex) {
		/* '*************************************************** */
		/* 'Author: Amraphen */
		/* 'Last Modification: 29/11/2010 */
		/* ' */
		/* '*************************************************** */
		/* 'Remove packet ID */
		Declaraciones.UserList[UserIndex].incomingData.ReadByte();

		if (Declaraciones.UserList[UserIndex].flags.Privilegios
				&& (PlayerType.User || PlayerType.Consejero || PlayerType.RoleMaster)) {
			return;
		}

		WriteRecordList(UserIndex);
	}

	/* '' */
	/*
	 * ' Writes the "RecordDetails" message to the given user's outgoing data
	 * buffer.
	 */
	/* ' */
	/* ' @param UserIndex User to which the message is intended. */
	/*
	 * ' @remarks The data is not actually sent until the buffer is properly
	 * flushed.
	 */

	static void WriteRecordDetails(int UserIndex, int RecordIndex) {
		/* '*************************************************** */
		/* 'Author: Amraphen */
		/* 'Last Modification: 29/11/2010 */
		/*
		 * 'Writes the "RecordDetails" message to the given user's outgoing data
		 * buffer
		 */
		/* '*************************************************** */
		int i;
		int tIndex;
		String tmpStr;
		vb6.Date TempDate;
		/* FIXME: ON ERROR GOTO ErrHandler */

		Declaraciones.UserList[UserIndex].outgoingData.WriteByte(ServerPacketID.RecordDetails);

		/* 'Creador y motivo */
		Declaraciones.UserList[UserIndex].outgoingData.WriteASCIIString(Declaraciones.Records[RecordIndex].Creador);
		Declaraciones.UserList[UserIndex].outgoingData.WriteASCIIString(Declaraciones.Records[RecordIndex].Motivo);

		tIndex = Extra.NameIndex(Declaraciones.Records[RecordIndex].Usuario);

		/* 'Status del pj (online?) */
		Declaraciones.UserList[UserIndex].outgoingData.WriteBoolean(tIndex > 0);

		/* 'Escribo la IP según el estado del personaje */
		if (tIndex > 0) {
			/* 'La IP Actual */
			tmpStr = Declaraciones.UserList[tIndex].ip;
			/* 'String nulo */
		} else {
			tmpStr = "";
		}
		Declaraciones.UserList[UserIndex].outgoingData.WriteASCIIString(tmpStr);

		/* 'Escribo tiempo online según el estado del personaje */
		if (tIndex > 0) {
			/* 'Tiempo logueado. */
			TempDate = vb6.Now() - Declaraciones.UserList[tIndex].LogOnTime;
			tmpStr = vb6.Hour(TempDate) + ":" + vb6.Minute(TempDate) + ":" + vb6.Second(TempDate);
		} else {
			/* 'Envío string nulo. */
			tmpStr = "";
		}
		Declaraciones.UserList[UserIndex].outgoingData.WriteASCIIString(tmpStr);

		/* 'Escribo observaciones: */
		tmpStr = "";
		if (Declaraciones.Records[RecordIndex].NumObs) {
			for (i = (1); i <= (Declaraciones.Records[RecordIndex].NumObs); i++) {
				tmpStr = tmpStr + Declaraciones.Records[RecordIndex].Obs[i].Creador + "> "
						+ Declaraciones.Records[RecordIndex].Obs[i].Detalles + vbCrLf;
			}

			tmpStr = vb6.Left(tmpStr, vb6.Len(tmpStr) - 1);
		}
		Declaraciones.UserList[UserIndex].outgoingData.WriteASCIIString(tmpStr);
		return;

		/* FIXME: ErrHandler : */
		if (Err.Number == Declaraciones.UserList[UserIndex].outgoingData.NotEnoughSpaceErrCode) {
			FlushBuffer(UserIndex);
			/* FIXME: RESUME */
		}
	}

	/* '' */
	/*
	 * ' Writes the "RecordList" message to the given user's outgoing data
	 * buffer.
	 */
	/* ' */
	/* ' @param UserIndex User to which the message is intended. */
	/*
	 * ' @remarks The data is not actually sent until the buffer is properly
	 * flushed.
	 */

	static void WriteRecordList(int UserIndex) {
		/* '*************************************************** */
		/* 'Author: Amraphen */
		/* 'Last Modification: 29/11/2010 */
		/*
		 * 'Writes the "RecordList" message to the given user's outgoing data
		 * buffer
		 */
		/* '*************************************************** */
		int i;

		/* FIXME: ON ERROR GOTO ErrHandler */

		Declaraciones.UserList[UserIndex].outgoingData.WriteByte(ServerPacketID.RecordList);

		Declaraciones.UserList[UserIndex].outgoingData.WriteByte(Declaraciones.NumRecords);
		for (i = (1); i <= (Declaraciones.NumRecords); i++) {
			Declaraciones.UserList[UserIndex].outgoingData.WriteASCIIString(Declaraciones.Records[i].Usuario);
		}
		return;

		/* FIXME: ErrHandler : */
		if (Err.Number == Declaraciones.UserList[UserIndex].outgoingData.NotEnoughSpaceErrCode) {
			FlushBuffer(UserIndex);
			/* FIXME: RESUME */
		}
	}

	/* '' */
	/* ' Handles the "RecordDetailsRequest" message. */
	/* ' */
	/* ' @param UserIndex The index of the user sending the message. */

	static void HandleRecordDetailsRequest(int UserIndex) {
		/* '*************************************************** */
		/* 'Author: Amraphen */
		/* 'Last Modification: 07/04/2011 */
		/* 'Handles the "RecordListRequest" message */
		/* '*************************************************** */
		int RecordIndex;

		/* 'Remove packet ID */
		Declaraciones.UserList[UserIndex].incomingData.ReadByte();

		RecordIndex = Declaraciones.UserList[UserIndex].incomingData.ReadByte();

		if (Declaraciones.UserList[UserIndex].flags.Privilegios
				&& (PlayerType.User || PlayerType.Consejero || PlayerType.RoleMaster)) {
			return;
		}

		WriteRecordDetails(UserIndex, RecordIndex);
	}

	static void HandleMoveItem(int UserIndex) {
		/* '*************************************************** */
		/* 'Author: Ignacio Mariano Tirabasso (Budi) */
		/* 'Last Modification: 01/01/2011 */
		/* ' */
		/* '*************************************************** */

		int originalSlot;
		int newSlot;

		Declaraciones.UserList[UserIndex].incomingData.ReadByte();

		originalSlot = Declaraciones.UserList[UserIndex].incomingData.ReadByte();
		newSlot = Declaraciones.UserList[UserIndex].incomingData.ReadByte();
		Declaraciones.UserList[UserIndex].incomingData.ReadByte();

		InvUsuario.moveItem(UserIndex, originalSlot, newSlot);

	}

	/* '' */
	/* ' Handles the "HigherAdminsMessage" message. */
	/* ' */
	/* ' @param userIndex The index of the user sending the message. */

	static void HandleHigherAdminsMessage(int UserIndex) {
		/* '*************************************************** */
		/* 'Author: Torres Patricio (Pato) */
		/* 'Last Modification: 03/30/12 */
		/* ' */
		/* '*************************************************** */
		if (Declaraciones.UserList[UserIndex].incomingData.length < 3) {
			throw new RuntimeException("Err . Raise UserList ( UserIndex ) . incomingData . NotEnoughDataErrCode");
			return;
		}

		/* FIXME: ON ERROR GOTO ErrHandler */
		/*
		 * 'This packet contains strings, make a copy of the data to prevent
		 * losses if it's not complete yet...
		 */
		clsByteQueue buffer;
		buffer = new clsByteQueue();
		buffer.CopyBuffer(Declaraciones.UserList[UserIndex].incomingData);

		/* 'Remove packet ID */
		buffer.ReadByte();

		String message;

		message = buffer.ReadASCIIString();

		if (((Declaraciones.UserList[UserIndex].flags.Privilegios && (PlayerType.Admin || PlayerType.Dios)) != 0)
				&& ((Declaraciones.UserList[UserIndex].flags.Privilegios && PlayerType.RoleMaster) == 0)) {
			General.LogGM(Declaraciones.UserList[UserIndex].Name, "Mensaje a Dioses:" + message);

			if (vb6.LenB(message) != 0) {
				/* 'Analize chat... */
				Statistics.ParseChat(message);

				modSendData.SendData(SendTarget.ToHigherAdminsButRMs, 0,
						PrepareMessageConsoleMsg(Declaraciones.UserList[UserIndex].Name + "(Sólo Dioses)> " + message,
								FontTypeNames.FONTTYPE_GMMSG));
			}
		}

		/*
		 * 'If we got here then packet is complete, copy data back to original
		 * queue
		 */
		Declaraciones.UserList[UserIndex].incomingData.CopyBuffer(buffer);

		/* FIXME: ErrHandler : */
		int ERROR;
		/* ERROR = Err . Number */
		/* FIXME: ON ERROR GOTO 0 */

		/* 'Destroy auxiliar buffer */
		buffer = null;

		if (ERROR != 0) {
			throw new RuntimeException("Err . Raise ERROR");
		}
	}
	/* '' */
	/* ' Handle the "AlterName" message */
	/* ' */
	/* ' @param userIndex The index of the user sending the message */

	static void HandleAlterGuildName(int UserIndex) {
		/* '*************************************************** */
		/* 'Author: Lex! */
		/* 'Last Modification: 14/05/12 */
		/* 'Change guild name */
		/* '*************************************************** */
		if (Declaraciones.UserList[UserIndex].incomingData.length < 5) {
			throw new RuntimeException("Err . Raise UserList ( UserIndex ) . incomingData . NotEnoughDataErrCode");
			return;
		}

		/* FIXME: ON ERROR GOTO ErrHandler */

		/*
		 * 'This packet contains strings, make a copy of the data to prevent
		 * losses if it's not complete yet...
		 */
		clsByteQueue buffer;
		buffer = new clsByteQueue();
		buffer.CopyBuffer(Declaraciones.UserList[UserIndex].incomingData);

		/* 'Remove packet ID */
		buffer.ReadByte();

		/* 'Reads the userName and newUser Packets */
		String GuildName;
		String newGuildName;
		int GuildIndex;

		GuildName = buffer.ReadASCIIString();
		newGuildName = buffer.ReadASCIIString();
		GuildName = vb6.Trim(GuildName);
		newGuildName = vb6.Trim(newGuildName);

		if (((Declaraciones.UserList[UserIndex].flags.Privilegios && PlayerType.RoleMaster) == 0)
				&& ((Declaraciones.UserList[UserIndex].flags.Privilegios
						&& (PlayerType.Admin || PlayerType.Dios)) != 0)) {
			if (vb6.LenB(GuildName) == 0 || vb6.LenB(newGuildName) == 0) {
				WriteConsoleMsg(UserIndex, "Usar: /ACLAN origen@destino", FontTypeNames.FONTTYPE_INFO);
			} else {
				/* 'Revisa si el nombre nuevo del clan existe */
				if ((vb6.InStrB(newGuildName, "+") != 0)) {
					GuildName = vb6.Replace(newGuildName, "+", " ");
				}

				GuildIndex = modGuilds.GetGuildIndex(newGuildName);
				if (GuildIndex > 0) {
					WriteConsoleMsg(UserIndex, "El clan destino ya existe.", FontTypeNames.FONTTYPE_INFO);
				} else {
					/* 'Revisa si el nombre del clan existe */
					if ((vb6.InStrB(GuildName, "+") != 0)) {
						GuildName = vb6.Replace(GuildName, "+", " ");
					}

					GuildIndex = modGuilds.GetGuildIndex(GuildName);
					if (GuildIndex > 0) {
						/* ' Existe clan origen y no el de destino */
						/*
						 * ' Verifica si existen archivos del clan, los crea con
						 * nombre nuevo y borra los viejos
						 */
						if (General.FileExist(Declaraciones.GUILDPATH + GuildName + "-members.mem")) {
							vb6.FileCopy(Declaraciones.GUILDPATH + GuildName + "-members.mem",
									Declaraciones.GUILDPATH + vb6.UCase(newGuildName) + "-members.mem");
							/*
							 * FIXME: KILL ( GUILDPATH & GuildName &
							 * "-members.mem" )
							 */
						}

						if (General.FileExist(Declaraciones.GUILDPATH + GuildName + "-relaciones.rel")) {
							vb6.FileCopy(Declaraciones.GUILDPATH + GuildName + "-relaciones.rel",
									Declaraciones.GUILDPATH + vb6.UCase(newGuildName) + "-relaciones.rel");
							/*
							 * FIXME: KILL ( GUILDPATH & GuildName &
							 * "-relaciones.rel" )
							 */
						}

						if (General.FileExist(Declaraciones.GUILDPATH + GuildName + "-Propositions.pro")) {
							vb6.FileCopy(Declaraciones.GUILDPATH + GuildName + "-Propositions.pro",
									Declaraciones.GUILDPATH + vb6.UCase(newGuildName) + "-Propositions.pro");
							/*
							 * FIXME: KILL ( GUILDPATH & GuildName &
							 * "-Propositions.pro" )
							 */
						}

						if (General.FileExist(Declaraciones.GUILDPATH + GuildName + "-solicitudes.sol")) {
							vb6.FileCopy(Declaraciones.GUILDPATH + GuildName + "-solicitudes.sol",
									Declaraciones.GUILDPATH + vb6.UCase(newGuildName) + "-solicitudes.sol");
							/*
							 * FIXME: KILL ( GUILDPATH & GuildName &
							 * "-solicitudes.sol" )
							 */
						}

						if (General.FileExist(Declaraciones.GUILDPATH + GuildName + "-votaciones.vot")) {
							vb6.FileCopy(Declaraciones.GUILDPATH + GuildName + "-votaciones.vot",
									Declaraciones.GUILDPATH + vb6.UCase(newGuildName) + "-votaciones.vot");
							/*
							 * FIXME: KILL ( GUILDPATH & GuildName &
							 * "-votaciones.vot" )
							 */
						}

						/* ' Actualiza nombre del clan en guildsinfo y server */
						ES.WriteVar(Declaraciones.GUILDINFOFILE, "GUILD" + GuildIndex, "GuildName", newGuildName);
						modGuilds.SetNewGuildName(GuildIndex, newGuildName);

						/* ' Actualiza todos los online del clan */
						int index;
						int NumOnline;
						String MemberList;
						int MemberIndex;

						MemberIndex = modGuilds.m_Iterador_ProximoUserIndex(GuildIndex);
						while (MemberIndex > 0) {
							if ((Declaraciones.UserList[MemberIndex].ConnID != -1)) {
								UsUaRiOs.RefreshCharStatus(MemberIndex);
							}

							MemberIndex = modGuilds.m_Iterador_ProximoUserIndex(GuildIndex);
						}

						/* ' Avisa que sali? todo OK y guarda en log del GM */
						WriteConsoleMsg(UserIndex, "El clan " + GuildName + " fue renombrado como " + newGuildName,
								FontTypeNames.FONTTYPE_INFO);
						General.LogGM(Declaraciones.UserList[UserIndex].Name,
								"Ha cambiado el nombre del clan " + GuildName + ". Ahora se llama " + newGuildName);
					} else {
						WriteConsoleMsg(UserIndex, "El clan origen no existe.", FontTypeNames.FONTTYPE_INFO);
					}
				}
			}
		}

		/*
		 * 'If we got here then packet is complete, copy data back to original
		 * queue
		 */
		Declaraciones.UserList[UserIndex].incomingData.CopyBuffer(buffer);

		/* FIXME: ErrHandler : */
		int ERROR;
		/* ERROR = Err . Number */
		/* FIXME: ON ERROR GOTO 0 */

		/* 'Destroy auxiliar buffer */
		buffer = null;

		if (ERROR != 0) {
			throw new RuntimeException("Err . Raise ERROR");
		}
	}

}