/*  AUTOMATICALLY CONVERTED FILE  */

/* 
 * Este archivo fue convertido automaticamente, por un script, desde el 
 * código fuente original de Visual Basic 6.
 */

/* [(0, 'ATTRIBUTE'), (1, 'VB_Name'), (5, '='), (4, '"modForum"')] */
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

public class modForum {

	static final int MAX_MENSAJES_FORO = 30;
	static final int MAX_ANUNCIOS_FORO = 5;

	static final String FORO_REAL_ID = "REAL";
	static final String FORO_CAOS_ID = "CAOS";

	static public class tPost {
		public String sTitulo;
		public String sPost;
		public String Autor;
	}

	static public class tForo {
		public tPost[] vsPost;
		public tPost[] vsAnuncio;
		public int CantPosts;
		public int CantAnuncios;
		public String ID;
	}

	private static int NumForos;
	private static tForo[] Foros = new tForo[0];

	public static void AddForum(String sForoID) {
		/* '*************************************************** */
		/* 'Author: ZaMa */
		/* 'Last Modification: 22/02/2010 */
		/* 'Adds a forum to the list and fills it. */
		/* '*************************************************** */
		String ForumPath;
		String PostPath;
		int PostIndex = 0;
		int FileIndex = 0;

		NumForos = NumForos + 1;
		Foros = (Foros == null) ? new tForo[1 + NumForos] : java.util.Arrays.copyOf(Foros, 1 + NumForos);

		ForumPath = vb6.App.Instance().Path + "\\foros\\" + sForoID + ".for";

		Foros[NumForos].ID = sForoID;

		if (General.FileExist(ForumPath, 0)) {
			Foros[NumForos].CantPosts = vb6.val(ES.GetVar(ForumPath, "INFO", "CantMSG"));
			Foros[NumForos].CantAnuncios = vb6.val(ES.GetVar(ForumPath, "INFO", "CantAnuncios"));

			/* ' Cargo posts */
			for (PostIndex = (1); PostIndex <= (Foros[NumForos].CantPosts); PostIndex++) {
				FileIndex = vb6.FreeFile();
				PostPath = vb6.App.Instance().Path + "\\foros\\" + sForoID + PostIndex + ".for";

				/* FIXME: OPEN PostPath FOR INPUT Shared AS # FileIndex */

				/* ' Titulo */
				/*
				 * FIXME: INPUT # FileIndex , . vsPost ( PostIndex ) . sTitulo
				 */
				/* ' Autor */
				/* FIXME: INPUT # FileIndex , . vsPost ( PostIndex ) . Autor */
				/* ' Mensaje */
				/* FIXME: INPUT # FileIndex , . vsPost ( PostIndex ) . sPost */

				/* FIXME: CLOSE # FileIndex */
			}

			/* ' Cargo anuncios */
			for (PostIndex = (1); PostIndex <= (Foros[NumForos].CantAnuncios); PostIndex++) {
				FileIndex = vb6.FreeFile();
				PostPath = vb6.App.Instance().Path + "\\foros\\" + sForoID + PostIndex + "a.for";

				/* FIXME: OPEN PostPath FOR INPUT Shared AS # FileIndex */

				/* ' Titulo */
				/*
				 * FIXME: INPUT # FileIndex , . vsAnuncio ( PostIndex ) .
				 * sTitulo
				 */
				/* ' Autor */
				/*
				 * FIXME: INPUT # FileIndex , . vsAnuncio ( PostIndex ) . Autor
				 */
				/* ' Mensaje */
				/*
				 * FIXME: INPUT # FileIndex , . vsAnuncio ( PostIndex ) . sPost
				 */

				/* FIXME: CLOSE # FileIndex */
			}
		}

	}

	public static int GetForumIndex(String /* FIXME BYREF!! */ sForoID) {
		int retval = 0;
		/* '*************************************************** */
		/* 'Author: ZaMa */
		/* 'Last Modification: 22/02/2010 */
		/* 'Returns the forum index. */
		/* '*************************************************** */

		int ForumIndex = 0;

		for (ForumIndex = (1); ForumIndex <= (NumForos); ForumIndex++) {
			if (Foros[ForumIndex].ID == sForoID) {
				retval = ForumIndex;
				return retval;
			}
		}

		return retval;
	}

	public static void AddPost(int ForumIndex, String /* FIXME BYREF!! */ Post,
			String /* FIXME BYREF!! */ Autor, String /* FIXME BYREF!! */ Titulo, boolean bAnuncio) {
		/* '*************************************************** */
		/* 'Author: ZaMa */
		/* 'Last Modification: 22/02/2010 */
		/* 'Saves a new post into the forum. */
		/* '*************************************************** */

		if (bAnuncio) {
			if (Foros[ForumIndex].CantAnuncios < modForum.MAX_ANUNCIOS_FORO) {
				Foros[ForumIndex].CantAnuncios = Foros[ForumIndex].CantAnuncios + 1;
			}

			MoveArray(ForumIndex, bAnuncio);

			/* ' Agrego el anuncio */
			Foros[ForumIndex].vsAnuncio[1].sTitulo = Titulo;
			Foros[ForumIndex].vsAnuncio[1].Autor = Autor;
			Foros[ForumIndex].vsAnuncio[1].sPost = Post;

		} else {
			if (Foros[ForumIndex].CantPosts < modForum.MAX_MENSAJES_FORO) {
				Foros[ForumIndex].CantPosts = Foros[ForumIndex].CantPosts + 1;
			}

			MoveArray(ForumIndex, bAnuncio);

			/* ' Agrego el post */
			Foros[ForumIndex].vsPost[1].sTitulo = Titulo;
			Foros[ForumIndex].vsPost[1].Autor = Autor;
			Foros[ForumIndex].vsPost[1].sPost = Post;

		}
	}

	public static void SaveForums() {
		/* '*************************************************** */
		/* 'Author: ZaMa */
		/* 'Last Modification: 22/02/2010 */
		/* 'Saves all forums into disk. */
		/* '*************************************************** */
		int ForumIndex = 0;

		for (ForumIndex = (1); ForumIndex <= (NumForos); ForumIndex++) {
			SaveForum(ForumIndex);
		}
	}

	public static void SaveForum(int ForumIndex) {
		/* '*************************************************** */
		/* 'Author: ZaMa */
		/* 'Last Modification: 22/02/2010 */
		/* 'Saves a forum into disk. */
		/* '*************************************************** */

		int PostIndex = 0;
		int FileIndex = 0;
		String PostPath;

		CleanForum(ForumIndex);

		/* ' Guardo info del foro */
		ES.WriteVar(vb6.App.Instance().Path + "\\Foros\\" + Foros[ForumIndex].ID + ".for", "INFO", "CantMSG",
				Foros[ForumIndex].CantPosts);
		ES.WriteVar(vb6.App.Instance().Path + "\\Foros\\" + Foros[ForumIndex].ID + ".for", "INFO", "CantAnuncios",
				Foros[ForumIndex].CantAnuncios);

		/* ' Guardo posts */
		for (PostIndex = (1); PostIndex <= (Foros[ForumIndex].CantPosts); PostIndex++) {

			PostPath = vb6.App.Instance().Path + "\\Foros\\" + Foros[ForumIndex].ID + PostIndex + ".for";
			FileIndex = vb6.FreeFile();
			/* FIXME: OPEN PostPath FOR OUTPUT AS FileIndex */

			/* FIXME: PRINT # FileIndex , . sTitulo */
			/* FIXME: PRINT # FileIndex , . Autor */
			/* FIXME: PRINT # FileIndex , . sPost */

			/* FIXME: CLOSE # FileIndex */

		}

		/* ' Guardo Anuncios */
		for (PostIndex = (1); PostIndex <= (Foros[ForumIndex].CantAnuncios); PostIndex++) {

			PostPath = vb6.App.Instance().Path + "\\Foros\\" + Foros[ForumIndex].ID + PostIndex + "a.for";
			FileIndex = vb6.FreeFile();
			/* FIXME: OPEN PostPath FOR OUTPUT AS FileIndex */

			/* FIXME: PRINT # FileIndex , . sTitulo */
			/* FIXME: PRINT # FileIndex , . Autor */
			/* FIXME: PRINT # FileIndex , . sPost */

			/* FIXME: CLOSE # FileIndex */

		}

	}

	public static void CleanForum(int ForumIndex) {
		/* '*************************************************** */
		/* 'Author: ZaMa */
		/* 'Last Modification: 22/02/2010 */
		/* 'Cleans a forum from disk. */
		/* '*************************************************** */
		int PostIndex = 0;
		int NumPost = 0;
		String ForumPath;

		/* ' Elimino todo */
		ForumPath = vb6.App.Instance().Path + "\\Foros\\" + Foros[ForumIndex].ID + ".for";
		if (General.FileExist(ForumPath, 0)) {

			NumPost = vb6.val(ES.GetVar(ForumPath, "INFO", "CantMSG"));

			/* ' Elimino los post viejos */
			for (PostIndex = (1); PostIndex <= (NumPost); PostIndex++) {
				/*
				 * FIXME: KILL App . Path & "\\Foros\\" & . ID & PostIndex & "
				 * .for"
				 */
			}

			NumPost = vb6.val(ES.GetVar(ForumPath, "INFO", "CantAnuncios"));

			/* ' Elimino los post viejos */
			for (PostIndex = (1); PostIndex <= (NumPost); PostIndex++) {
				/*
				 * FIXME: KILL App . Path & "\\Foros\\" & . ID & PostIndex & "
				 * a.for"
				 */
			}

			/* ' Elimino el foro */
			/* FIXME: KILL App . Path & "\\Foros\\" & . ID & ".for" */

		}

	}

	public static boolean SendPosts(int UserIndex, String /* FIXME BYREF!! */ ForoID) {
		boolean retval = false;
		/* '*************************************************** */
		/* 'Author: ZaMa */
		/* 'Last Modification: 22/02/2010 */
		/* 'Sends all the posts of a required forum */
		/* '*************************************************** */

		int ForumIndex = 0;
		int PostIndex = 0;
		boolean bEsGm = false;

		ForumIndex = GetForumIndex(ForoID);

		if (ForumIndex > 0) {

			/* ' Send General posts */
			for (PostIndex = (1); PostIndex <= (Foros[ForumIndex].CantPosts); PostIndex++) {
				Protocol.WriteAddForumMsg(UserIndex, eForumMsgType.ieGeneral,
						Foros[ForumIndex].vsPost[PostIndex].sTitulo, Foros[ForumIndex].vsPost[PostIndex].Autor,
						Foros[ForumIndex].vsPost[PostIndex].sPost);
			}

			/* ' Send Sticky posts */
			for (PostIndex = (1); PostIndex <= (Foros[ForumIndex].CantAnuncios); PostIndex++) {
				Protocol.WriteAddForumMsg(UserIndex, eForumMsgType.ieGENERAL_STICKY,
						Foros[ForumIndex].vsAnuncio[PostIndex].sTitulo, Foros[ForumIndex].vsAnuncio[PostIndex].Autor,
						Foros[ForumIndex].vsAnuncio[PostIndex].sPost);
			}

			bEsGm = EsGM[UserIndex];

			/* ' Caos? */
			if (Extra.esCaos(UserIndex) || bEsGm) {

				ForumIndex = GetForumIndex(modForum.FORO_CAOS_ID);

				/* ' Send General Caos posts */
				for (PostIndex = (1); PostIndex <= (Foros[ForumIndex].CantPosts); PostIndex++) {

					Protocol.WriteAddForumMsg(UserIndex, eForumMsgType.ieCAOS,
							Foros[ForumIndex].vsPost[PostIndex].sTitulo, Foros[ForumIndex].vsPost[PostIndex].Autor,
							Foros[ForumIndex].vsPost[PostIndex].sPost);

				}

				/* ' Send Sticky posts */
				for (PostIndex = (1); PostIndex <= (Foros[ForumIndex].CantAnuncios); PostIndex++) {
					Protocol.WriteAddForumMsg(UserIndex, eForumMsgType.ieCAOS_STICKY,
							Foros[ForumIndex].vsAnuncio[PostIndex].sTitulo,
							Foros[ForumIndex].vsAnuncio[PostIndex].Autor, Foros[ForumIndex].vsAnuncio[PostIndex].sPost);
				}

			}

			/* ' Caos? */
			if (Extra.esArmada(UserIndex) || bEsGm) {

				ForumIndex = GetForumIndex(modForum.FORO_REAL_ID);

				/* ' Send General Real posts */
				for (PostIndex = (1); PostIndex <= (Foros[ForumIndex].CantPosts); PostIndex++) {

					Protocol.WriteAddForumMsg(UserIndex, eForumMsgType.ieREAL,
							Foros[ForumIndex].vsPost[PostIndex].sTitulo, Foros[ForumIndex].vsPost[PostIndex].Autor,
							Foros[ForumIndex].vsPost[PostIndex].sPost);

				}

				/* ' Send Sticky posts */
				for (PostIndex = (1); PostIndex <= (Foros[ForumIndex].CantAnuncios); PostIndex++) {
					Protocol.WriteAddForumMsg(UserIndex, eForumMsgType.ieREAL_STICKY,
							Foros[ForumIndex].vsAnuncio[PostIndex].sTitulo,
							Foros[ForumIndex].vsAnuncio[PostIndex].Autor, Foros[ForumIndex].vsAnuncio[PostIndex].sPost);
				}

			}

			retval = true;
		}

		return retval;
	}

	public static boolean EsAnuncio(int ForumType) {
		boolean retval = false;
		/* '*************************************************** */
		/* 'Author: ZaMa */
		/* 'Last Modification: 22/02/2010 */
		/* 'Returns true if the post is sticky. */
		/* '*************************************************** */
		switch (ForumType) {
		case ieCAOS_STICKY:
			retval = true;

			break;

		case ieGENERAL_STICKY:
			retval = true;

			break;

		case ieREAL_STICKY:
			retval = true;

			break;
		}

		return retval;
	}

	public static int ForumAlignment(int yForumType) {
		int retval = 0;
		/* '*************************************************** */
		/* 'Author: ZaMa */
		/* 'Last Modification: 01/03/2010 */
		/* 'Returns the forum alignment. */
		/* '*************************************************** */
		switch (yForumType) {
		case ieCAOS:
		case eForumMsgType.ieCAOS_STICKY:
			retval = eForumType.ieCAOS;

			break;

		case ieGeneral:
		case eForumMsgType.ieGENERAL_STICKY:
			retval = eForumType.ieGeneral;

			break;

		case ieREAL:
		case eForumMsgType.ieREAL_STICKY:
			retval = eForumType.ieREAL;

			break;
		}

		return retval;
	}

	public static void ResetForums() {
		/* '*************************************************** */
		/* 'Author: ZaMa */
		/* 'Last Modification: 22/02/2010 */
		/* 'Resets forum info */
		/* '*************************************************** */
		Foros = new tForo[0];
		Foros = (Foros == null) ? new tForo[1 + 1] : java.util.Arrays.copyOf(Foros, 1 + 1);
		NumForos = 0;
	}

	public static void MoveArray(int ForumIndex, boolean Sticky) {
		int i = 0;

		if (Sticky) {
			/* FIXME WEIRD FOR */
			for (i = (Foros[ForumIndex].CantAnuncios); ((-1) > 0) ? (i <= (2)) : (i >= (2)); i = i + (-1)) {
				Foros[ForumIndex].vsAnuncio[i].sTitulo = Foros[ForumIndex].vsAnuncio[i - 1].sTitulo;
				Foros[ForumIndex].vsAnuncio[i].sPost = Foros[ForumIndex].vsAnuncio[i - 1].sPost;
				Foros[ForumIndex].vsAnuncio[i].Autor = Foros[ForumIndex].vsAnuncio[i - 1].Autor;
			}
		} else {
			/* FIXME WEIRD FOR */
			for (i = (Foros[ForumIndex].CantPosts); ((-1) > 0) ? (i <= (2)) : (i >= (2)); i = i + (-1)) {
				Foros[ForumIndex].vsPost[i].sTitulo = Foros[ForumIndex].vsPost[i - 1].sTitulo;
				Foros[ForumIndex].vsPost[i].sPost = Foros[ForumIndex].vsPost[i - 1].sPost;
				Foros[ForumIndex].vsPost[i].Autor = Foros[ForumIndex].vsPost[i - 1].Autor;
			}
		}
	}

}