/*  AUTOMATICALLY CONVERTED FILE  */
/* [(0, 'ATTRIBUTE'), (1, 'VB_Name'), (5, '='), (4, '"wskapiAO"')] */
/* '************************************************************** */
/* ' wskapiAO.bas */
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

public class wskapiAO {

	/* '' */
	/* ' Modulo para manejar Winsock */
	/* ' */

	/* # IF UsarQueSocket = 1 THEN */

	/* 'Si la variable esta en TRUE , al iniciar el WsApi se crea */
	/* 'una ventana LABEL para recibir los mensajes. Al detenerlo, */
	/* 'se destruye. */
	/* 'Si es FALSE, los mensajes se envian al form frmMain (o el */
	/* 'que sea). */
	/* # CONST WSAPI_CREAR_LABEL = True */
	static final int WSAPI_CREAR_LABEL = true;

	static final int SD_BOTH = 0x2;

	static final int WS_CHILD = 0x40000000;
	static final int GWL_WNDPROC = /* FANCY EXPRESSION */ (-4);

	static final int SIZE_RCVBUF = 8192;
	static final int SIZE_SNDBUF = 8192;

	/* '' */
	/*
	 * 'Esto es para agilizar la busqueda del slot a partir de un socket dado,
	 */
	/* 'sino, la funcion BuscaSlotSock se nos come todo el uso del CPU. */
	/* ' */
	/* ' @param Sock sock */
	/* ' @param slot slot */
	/* ' */
	static public class tSockCache {
		public int Sock;
		public int Slot;
	}

	public static vb6.Collection WSAPISock2Usr;

	/*
	 * '
	 * =========================================================================
	 * ===========
	 */
	/*
	 * '
	 * =========================================================================
	 * ===========
	 */

	public static int OldWProc;
	public static int ActualWProc;
	public static int hWndMsg;

	/*
	 * '
	 * =========================================================================
	 * ===========
	 */
	/*
	 * '
	 * =========================================================================
	 * ===========
	 */

	public static int SockListen;

	/* # END IF */

	/*
	 * '
	 * =========================================================================
	 * ===========
	 */
	/*
	 * '
	 * =========================================================================
	 * ===========
	 */

	static void IniciaWsApi(int hwndParent) {
 /* # IF UsarQueSocket = 1 THEN */
 
 LogApiSock("IniciaWsApi");
 Debug.PRINT"IniciaWsApi"();
 
 /* # IF WSAPI_CREAR_LABEL THEN */
 /* # ELSE */
 wskapiAO.hWndMsg = hwndParent;
 /* 'WSAPI_CREAR_LABEL */
 /* # END IF */
 
 wskapiAO.OldWProc = SetWindowLong(wskapiAO.hWndMsg, wskapiAO.GWL_WNDPROC, AddressOfWndProc());
 wskapiAO.ActualWProc = GetWindowLong(wskapiAO.hWndMsg, wskapiAO.GWL_WNDPROC);
 
 String desc;
 StartWinsock[desc];
 
 /* # END IF */
}

	static void LimpiaWsApi() {
 /* # IF UsarQueSocket = 1 THEN */
 
 LogApiSock("LimpiaWsApi");
 
  if (WSAStartedUp) {
  EndWinsock;
 }
 
  if (wskapiAO.OldWProc != 0) {
  SetWindowLong(wskapiAO.hWndMsg, wskapiAO.GWL_WNDPROC, wskapiAO.OldWProc);
  wskapiAO.OldWProc = 0;
 }
 
 /* # IF WSAPI_CREAR_LABEL THEN */
 /* # END IF */
 
 /* # END IF */
}

	static int BuscaSlotSock(int S) {
		int retval = 0;
		/* # IF UsarQueSocket = 1 THEN */

		/* FIXME: ON ERROR GOTO hayerror */

		retval = wskapiAO.WSAPISock2Usr.Item[vb6.CStr(S)];
		return retval;

		/* FIXME: hayerror : */
		retval = -1;
		/* # END IF */

		return retval;
	}

	static void AgregaSlotSock(int Sock, int Slot) {
 Debug.PRINT"AgregaSockSlot"();
 /* # IF ( UsarQueSocket = 1 ) THEN */
 
  if (wskapiAO.WSAPISock2Usr.Count>Declaraciones.MaxUsers) {
  TCP.CloseSocket(Slot);
  return;
 }
 
 wskapiAO.WSAPISock2Usr.Add(vb6.CStr(Slot), vb6.CStr(Sock));
 
 /* 'Dim Pri As Long, Ult As Long, Med As Long */
 /* 'Dim LoopC As Long */
 /* ' */
 /* 'If WSAPISockChacheCant > 0 Then */
 /* '    Pri = 1 */
 /* '    Ult = WSAPISockChacheCant */
 /* '    Med = Int((Pri + Ult) / 2) */
 /* ' */
 /* '    Do While (Pri <= Ult) And (Ult > 1) */
 /* '        If Sock < WSAPISockChache(Med).Sock Then */
 /* '            Ult = Med - 1 */
 /* '        Else */
 /* '            Pri = Med + 1 */
 /* '        End If */
 /* '        Med = Int((Pri + Ult) / 2) */
 /* '    Loop */
 /* ' */
 /* '    Pri = IIf(Sock < WSAPISockChache(Med).Sock, Med, Med + 1) */
 /* '    Ult = WSAPISockChacheCant */
 /* '    For LoopC = Ult To Pri Step -1 */
 /* '        WSAPISockChache(LoopC + 1) = WSAPISockChache(LoopC) */
 /* '    Next LoopC */
 /* '    Med = Pri */
 /* 'Else */
 /* '    Med = 1 */
 /* 'End If */
 /* 'WSAPISockChache(Med).Slot = Slot */
 /* 'WSAPISockChache(Med).Sock = Sock */
 /* 'WSAPISockChacheCant = WSAPISockChacheCant + 1 */
 
 /* # END IF */
}

	static void BorraSlotSock(int Sock) {
 /* # IF ( UsarQueSocket = 1 ) THEN */
 int cant = 0;
 
 cant = wskapiAO.WSAPISock2Usr.Count;
 /* FIXME: ON ERROR RESUME NEXT */
 wskapiAO.WSAPISock2Usr.Remove(vb6.CStr(Sock));
 
 Debug.PRINT"BorraSockSlot " + cant + " -> " + wskapiAO.WSAPISock2Usr.Count();
 
 /* # END IF */
}

	static int WndProc(int hWnd, int msg, int wParam, int lParam) {
 int retval = 0;
 /* # IF UsarQueSocket = 1 THEN */
 
 /* FIXME: ON ERROR RESUME NEXT */
 
 int ret = 0;
 int[] Tmp;
 int S = 0;
 int E = 0;
 int N = 0;
 int UltError = 0;
 
 switch (msg) {
  case 1025:
  S = wParam;
  E = WSAGetSelectEvent[lParam];
  
  switch (E) {
   case FD_ACCEPT:
    if (S == wskapiAO.SockListen) {
    EventoSockAccept(S);
   }
   
   /* '    Case FD_WRITE */
   /* '        N = BuscaSlotSock(s) */
   /* '        If N < 0 And s <> SockListen Then */
   /* '            'Call apiclosesocket(s) */
   /* '            call WSApiCloseSocket(s) */
   /* '            Exit Function */
   /* '        End If */
   /* ' */
   
   /* '        Call IntentarEnviarDatosEncolados(N) */
   /* ' */
   /* '        Dale = UserList(N).ColaSalida.Count > 0 */
   /* '        Do While Dale */
   /* '            Ret = WsApiEnviar(N, UserList(N).ColaSalida.Item(1), False) */
   /* '            If Ret <> 0 Then */
   /* '                If Ret = WSAEWOULDBLOCK Then */
   /* '                    Dale = False */
   /* '                Else */
   /* '                    'y aca que hacemo' ?? help! i need somebody, help! */
   /* '                    Dale = False */
   /* '                    Debug.Print "ERROR AL ENVIAR EL DATO DESDE LA COLA " & Ret & ": " & GetWSAErrorString(Ret) */
   /* '                End If */
   /* '            Else */
   /* '            '    Debug.Print "Dato de la cola enviado" */
   /* '                UserList(N).ColaSalida.Remove 1 */
   /* '                Dale = (UserList(N).ColaSalida.Count > 0) */
   /* '            End If */
   /* '        Loop */
   
   break;
   
   case FD_READ:
   N = BuscaSlotSock(S);
    if (N<0 && S != wskapiAO.SockListen) {
    /* 'Call apiclosesocket(s) */
    WSApiCloseSocket(S);
    return retval;
   }
   
   /* 'create appropiate sized buffer */
   Tmp = (Tmp == null) ? new Byte[wskapiAO.SIZE_RCVBUF-1] : java.util.Arrays.copyOf(Tmp, wskapiAO.SIZE_RCVBUF-1);
   
   ret = recv[S][Tmp[0]][wskapiAO.SIZE_RCVBUF][0];
   /* ' Comparo por = 0 ya que esto es cuando se cierra */
   /* ' "gracefully". (mas abajo) */
    if (ret<0) {
    UltError = Err.LastDllError;
     if (UltError == TCP.WSAEMSGSIZE) {
     Debug.PRINT"WSAEMSGSIZE"();
     ret = wskapiAO.SIZE_RCVBUF;
     } else {
     Debug.PRINT"Error en Recv: " + GetWSAErrorString[UltError];
     LogApiSock("Error en Recv: N=" + N + " S=" + S + " Str=" + GetWSAErrorString[UltError]);
     
     /* 'no hay q llamar a CloseSocket() directamente, */
     /* 'ya q pueden abusar de algun error para */
     /* 'desconectarse sin los 10segs. CREEME. */
     TCP.CloseSocketSL(N);
     UsUaRiOs.Cerrar_Usuario(N);
     return retval;
    }
    } else if (ret == 0) {
    TCP.CloseSocketSL(N);
    UsUaRiOs.Cerrar_Usuario(N);
   }
   
   Tmp = (Tmp == null) ? new Byte[ret-1] : java.util.Arrays.copyOf(Tmp, ret-1);
   
   EventoSockRead(N, Tmp);
   
   break;
   
   case FD_CLOSE:
   N = BuscaSlotSock(S);
   if (S != wskapiAO.SockListen) {
   apiclosesocket[S];
   }
   
    if (N>0) {
    BorraSlotSock(S);
    Declaraciones.UserList[N].ConnID = -1;
    Declaraciones.UserList[N].ConnIDValida = false;
    EventoSockClose(N);
   }
  break;
  }
  
  default:
  retval = CallWindowProc(wskapiAO.OldWProc, hWnd, msg, wParam, lParam);
 break;
 }
 /* # END IF */
return retval;
}

	/* 'Retorna 0 cuando se envió o se metio en la cola, */
	/* 'retorna <> 0 cuando no se pudo enviar o no se pudo meter en la cola */
	static int WsApiEnviar(int Slot, String /* FIXME BYREF!! */ str) {
 int retval = 0;
 /* # IF UsarQueSocket = 1 THEN */
 String ret;
 int Retorno = 0;
 int[] data;
 
 data = (data == null) ? new Byte[vb6.Len(str)-1] : java.util.Arrays.copyOf(data, vb6.Len(str)-1);
 
 data = vb6.StrConv(str, vbFromUnicode);
 
 /* # IF SeguridadAlkon THEN */
 /* # END IF */
 
 Retorno = 0;
 
  if (Declaraciones.UserList[Slot].ConnID != -1 && Declaraciones.UserList[Slot].ConnIDValida) {
  ret = send[BYVALDeclaraciones.UserList[Slot].ConnID][data[0]][BYVALvb6.UBound(data[])+1][BYVAL0];
   if (ret<0) {
   ret = Err.LastDllError;
    if (ret == TCP.WSAEWOULDBLOCK) {
    
    /* # IF SeguridadAlkon THEN */
    /* # END IF */
    
    /* ' WSAEWOULDBLOCK, put the data again in the outgoingData Buffer */
    Declaraciones.UserList[Slot].outgoingData.WriteASCIIStringFixed(str);
   }
  }
  } else if (Declaraciones.UserList[Slot].ConnID != -1 && !Declaraciones.UserList[Slot].ConnIDValida) {
   if (!Declaraciones.UserList[Slot].Counters.Saliendo) {
   Retorno = -1;
  }
 }
 
 retval = Retorno;
 /* # END IF */
return retval;
}

	static void LogApiSock(String str) {
		/* # IF ( UsarQueSocket = 1 ) THEN */

		/* FIXME: ON ERROR GOTO ErrHandler */

		int nfile = 0;
		/* ' obtenemos un canal */
		nfile = vb6.FreeFile();
		/*
		 * FIXME: OPEN App . Path & "\\logs\\wsapi.log" FOR Append Shared AS #
		 * nfile
		 */
		/* FIXME: PRINT # nfile , Date & " " & time & " " & str */
		/* FIXME: CLOSE # nfile */

		return;

		/* FIXME: ErrHandler : */

		/* # END IF */
	}

	static void EventoSockAccept(int SockID) {
 /* # IF UsarQueSocket = 1 THEN */
 /* '========================================================== */
 /* 'USO DE LA API DE WINSOCK */
 /* '======================== */
 
 int NewIndex = 0;
 int ret = 0;
 int Tam = 0;
 sockaddr sa;
 int NuevoSock = 0;
 int i = 0;
 String str;
 int[] data;
 
 Tam = sockaddr_size;
 
 /* '============================================= */
 /* 'SockID es en este caso es el socket de escucha, */
 /* 'a diferencia de socketwrench que es el nuevo */
 /* 'socket de la nueva conn */
 
 /* 'Modificado por Maraxus */
 /* 'ret = WSAAccept(SockID, sa, Tam, AddressOf CondicionSocket, 0) */
 ret = accept[SockID][sa][Tam];
 
  if (ret == INVALID_SOCKET) {
  i = Err.LastDllError;
  General.LogCriticEvent("Error en Accept() API " + i + ": " + GetWSAErrorString[i]);
  return;
 }
 
 /* 'If ret = INVALID_SOCKET Then */
 /* '    If Err.LastDllError = 11002 Then */
 /* '        ' We couldn't decide if to accept or reject the connection */
 /* '        'Force reject so we can get it out of the queue */
 /* '        ret = WSAAccept(SockID, sa, Tam, AddressOf CondicionSocket, 1) */
 /* '        Call LogCriticEvent("Error en WSAAccept() API 11002: No se pudo decidir si aceptar o rechazar la conexión.") */
 /* '    Else */
 /* '        i = Err.LastDllError */
 /* '        Call LogCriticEvent("Error en WSAAccept() API " & i & ": " & GetWSAErrorString(i)) */
 /* '        Exit Sub */
 /* '    End If */
 /* 'End If */
 
 NuevoSock = ret;
 
  if (setsockopt[NuevoSock][SOL_SOCKET][SO_LINGER][0][4] != 0) {
  i = Err.LastDllError;
  General.LogCriticEvent("Error al setear lingers." + i + ": " + GetWSAErrorString[i]);
 }
 
  if (!SecurityIp.IpSecurityAceptarNuevaConexion(sa.sin_addr)) {
  WSApiCloseSocket(NuevoSock);
  return;
 }
 
  if (SecurityIp.IPSecuritySuperaLimiteConexiones(sa.sin_addr)) {
  str = Protocol.PrepareMessageErrorMsg("Limite de conexiones para su IP alcanzado.");
  
  data = (data == null) ? new Byte[vb6.Len(str)-1] : java.util.Arrays.copyOf(data, vb6.Len(str)-1);
  
  data = vb6.StrConv(str, vbFromUnicode);
  
  /* # IF SeguridadAlkon THEN */
  /* # END IF */
  
  send[BYVALNuevoSock][data[0]][BYVALvb6.UBound(data[])+1][BYVAL0];
  WSApiCloseSocket(NuevoSock);
  return;
 }
 
 /* 'Seteamos el tamaño del buffer de entrada */
  if (setsockopt[NuevoSock][SOL_SOCKET][SO_RCVBUFFER][wskapiAO.SIZE_RCVBUF][4] != 0) {
  i = Err.LastDllError;
  General.LogCriticEvent("Error al setear el tamaño del buffer de entrada " + i + ": " + GetWSAErrorString[i]);
 }
 /* 'Seteamos el tamaño del buffer de salida */
  if (setsockopt[NuevoSock][SOL_SOCKET][SO_SNDBUFFER][wskapiAO.SIZE_SNDBUF][4] != 0) {
  i = Err.LastDllError;
  General.LogCriticEvent("Error al setear el tamaño del buffer de salida " + i + ": " + GetWSAErrorString[i]);
 }
 
 /* ''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''' */
 /* ''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''' */
 /* '   BIENVENIDO AL SERVIDOR!!!!!!!! */
 /* ''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''' */
 /* ''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''' */
 
 /* 'Mariano: Baje la busqueda de slot abajo de CondicionSocket y limite x ip */
 /* ' Nuevo indice */
 NewIndex = UsUaRiOs.NextOpenUser();
 
  if (NewIndex<=Declaraciones.MaxUsers) {
  
  /* 'Make sure both outgoing and incoming data buffers are clean */
  Declaraciones.UserList[NewIndex].incomingData.ReadASCIIStringFixed(Declaraciones.UserList[NewIndex].incomingData.length);
  Declaraciones.UserList[NewIndex].outgoingData.ReadASCIIStringFixed(Declaraciones.UserList[NewIndex].outgoingData.length);
  
  /* # IF SeguridadAlkon THEN */
  /* # END IF */
  
  Declaraciones.UserList[NewIndex].ip = GetAscIP[sa.sin_addr];
  /* 'Busca si esta banneada la ip */
   for (i = (1); i <= (Declaraciones.BanIps.Count); i++) {
    if (Declaraciones.BanIps.Item[i] == Declaraciones.UserList[NewIndex].ip) {
    /* 'Call apiclosesocket(NuevoSock) */
    Protocol.WriteErrorMsg(NewIndex, "Su IP se encuentra bloqueada en este servidor.");
    Protocol.FlushBuffer(NewIndex);
    SecurityIp.IpRestarConexion(sa.sin_addr);
    WSApiCloseSocket(NuevoSock);
    return;
   }
  }
  
  if (NewIndex>Declaraciones.LastUser) {
  Declaraciones.LastUser = NewIndex;
  }
  
  Declaraciones.UserList[NewIndex].ConnID = NuevoSock;
  Declaraciones.UserList[NewIndex].ConnIDValida = true;
  
  AgregaSlotSock(NuevoSock, NewIndex);
  } else {
  str = Protocol.PrepareMessageErrorMsg("El servidor se encuentra lleno en este momento. Disculpe las molestias ocasionadas.");
  
  data = (data == null) ? new Byte[vb6.Len(str)-1] : java.util.Arrays.copyOf(data, vb6.Len(str)-1);
  
  data = vb6.StrConv(str, vbFromUnicode);
  
  /* # IF SeguridadAlkon THEN */
  /* # END IF */
  
  send[BYVALNuevoSock][data[0]][BYVALvb6.UBound(data[])+1][BYVAL0];
  WSApiCloseSocket(NuevoSock);
 }
 
 /* # END IF */
}

	static void EventoSockRead(int Slot, int[] /* FIXME BYREF!! */ Datos) {
		/* # IF UsarQueSocket = 1 THEN */

		/* # IF SeguridadAlkon THEN */
		/* # END IF */

		Declaraciones.UserList[Slot].incomingData.WriteBlock(Datos);

		if (Declaraciones.UserList[Slot].ConnID != -1) {
			Protocol.HandleIncomingData(Slot);
		} else {
			return;
		}

		/* # END IF */
	}

	static void EventoSockClose(int Slot) {
		/* # IF UsarQueSocket = 1 THEN */

		/* 'Es el mismo user al que está revisando el centinela?? */
		/*
		 * 'Si estamos acá es porque se cerró la conexión, no es un /salir, y no
		 * queremos banearlo....
		 */
		int CentinelaIndex = 0;
		CentinelaIndex = Declaraciones.UserList[Slot].flags.CentinelaIndex;

		if (CentinelaIndex != 0) {
			modCentinela.CentinelaUserLogout(CentinelaIndex);
		}

		/* # IF SeguridadAlkon THEN */
		/* # END IF */

		if (Declaraciones.UserList[Slot].flags.UserLogged) {
			TCP.CloseSocketSL(Slot);
			UsUaRiOs.Cerrar_Usuario(Slot);
		} else {
			TCP.CloseSocket(Slot);
		}
		/* # END IF */
	}

	static void WSApiReiniciarSockets() {
 /* # IF UsarQueSocket = 1 THEN */
 int i = 0;
 /* 'Cierra el socket de escucha */
 if (wskapiAO.SockListen>=0) {
 apiclosesocket[wskapiAO.SockListen];
 }
 
 /* 'Cierra todas las conexiones */
  for (i = (1); i <= (Declaraciones.MaxUsers); i++) {
   if (Declaraciones.UserList[i].ConnID != -1 && Declaraciones.UserList[i].ConnIDValida) {
   TCP.CloseSocket(i);
  }
  
  /* 'Call ResetUserSlot(i) */
 }
 
  for (i = (1); i <= (Declaraciones.MaxUsers); i++) {
  Declaraciones.UserList[i].incomingData = null;
  Declaraciones.UserList[i].outgoingData = null;
 }
 
 /* ' No 'ta el PRESERVE :p */
 Declaraciones.UserList = new None[0];
 Declaraciones.UserList = (Declaraciones.UserList == null) ? new None[1 + Declaraciones.MaxUsers] : java.util.Arrays.copyOf(Declaraciones.UserList, 1 + Declaraciones.MaxUsers);
  for (i = (1); i <= (Declaraciones.MaxUsers); i++) {
  Declaraciones.UserList[i].ConnID = -1;
  Declaraciones.UserList[i].ConnIDValida = false;
  
  Declaraciones.UserList[i].incomingData = new clsByteQueue();
  Declaraciones.UserList[i].outgoingData = new clsByteQueue();
 }
 
 Declaraciones.LastUser = 1;
 Declaraciones.NumUsers = 0;
 
 LimpiaWsApi();
 Sleep(100);
 IniciaWsApi(frmMain.hWnd);
 wskapiAO.SockListen = ListenForConnect[Admin.Puerto][wskapiAO.hWndMsg][""];
 
 /* # END IF */
}

	static void WSApiCloseSocket(int Socket) {
 /* # IF UsarQueSocket = 1 THEN */
 WSAAsyncSelect[Socket][wskapiAO.hWndMsg][BYVAL1025][BYVAL(FD_CLOSE)];
 ShutDown[Socket][wskapiAO.SD_BOTH];
 /* # END IF */
}

	static int CondicionSocket(WSABUF /* FIXME BYREF!! */ lpCallerId,
			WSABUF /* FIXME BYREF!! */ lpCallerData,
			FLOWSPEC /* FIXME BYREF!! */ lpSQOS, int Reserved,
			WSABUF /* FIXME BYREF!! */ lpCalleeId, WSABUF /* FIXME BYREF!! */ lpCalleeData,
			int /* FIXME BYREF!! */ Group, int dwCallbackData) {
		int retval = 0;
		/* # IF UsarQueSocket = 1 THEN */
		sockaddr sa;

		/* 'Check if we were requested to force reject */

		if (dwCallbackData == 1) {
			retval = CF_REJECT;
			return retval;
		}

		/* 'Get the address */

		SysTray.CopyMemory(sa, BYVALlpCallerId.lpBuffer, lpCallerId.dwBufferLen);

		if (!SecurityIp.IpSecurityAceptarNuevaConexion(sa.sin_addr)) {
			retval = CF_REJECT;
			return retval;
		}

		/*
		 * 'En realdiad es al pedo, porque CondicionSocket se inicializa a 0,
		 * pero así es más claro....
		 */
		retval = CF_ACCEPT;
		/* # END IF */
		return retval;
	}

}