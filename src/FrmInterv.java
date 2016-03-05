
/*  AUTOMATICALLY CONVERTED FILE  */
/* [(0, 'ATTRIBUTE'), (1, 'VB_Name'), (5, '='), (4, '"FrmInterv"')] */
/* [(0, 'ATTRIBUTE'), (1, 'VB_GlobalNameSpace'), (5, '='), (1, 'False')] */
/* [(0, 'ATTRIBUTE'), (1, 'VB_Creatable'), (5, '='), (1, 'False')] */
/* [(0, 'ATTRIBUTE'), (1, 'VB_PredeclaredId'), (5, '='), (1, 'True')] */
/* [(0, 'ATTRIBUTE'), (1, 'VB_Exposed'), (5, '='), (1, 'False')] */
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

public class FrmInterv {

	static void AplicarIntervalos() {

		/* '¿?¿?¿?¿?¿?¿?¿?¿?¿?¿?¿ Intervalos del main loop ¿?¿?¿?¿?¿?¿?¿?¿?¿ */
		Admin.SanaIntervaloSinDescansar = vb6.val(txtSanaIntervaloSinDescansar.Text);
		Admin.StaminaIntervaloSinDescansar = vb6.val(txtStaminaIntervaloSinDescansar.Text);
		Admin.SanaIntervaloDescansar = vb6.val(txtSanaIntervaloDescansar.Text);
		Admin.StaminaIntervaloDescansar = vb6.val(txtStaminaIntervaloDescansar.Text);
		Admin.IntervaloSed = vb6.val(txtIntervaloSed.Text);
		Admin.IntervaloHambre = vb6.val(txtIntervaloHambre.Text);
		Admin.IntervaloVeneno = vb6.val(txtIntervaloVeneno.Text);
		Admin.IntervaloParalizado = vb6.val(txtIntervaloParalizado.Text);
		Admin.IntervaloInvisible = vb6.val(txtIntervaloInvisible.Text);
		Admin.IntervaloFrio = vb6.val(txtIntervaloFrio.Text);
		Admin.IntervaloWavFx = vb6.val(txtIntervaloWAVFX.Text);
		Admin.IntervaloInvocacion = vb6.val(txtInvocacion.Text);
		Admin.IntervaloParaConexion = vb6.val(txtIntervaloParaConexion.Text);

		/* '///////////////// TIMERS \\\\\\\\\\\\\\\\\\\ */

		Admin.IntervaloUserPuedeCastear = vb6.val(txtIntervaloLanzaHechizo.Text);
		frmMain.npcataca.Interval = vb6.val(txtNPCPuedeAtacar.Text);
		frmMain.TIMER_AI.Interval = vb6.val(txtAI.Text);
		Admin.IntervaloUserPuedeTrabajar = vb6.val(txtTrabajo.Text);
		Admin.IntervaloUserPuedeAtacar = vb6.val(txtPuedeAtacar.Text);
		frmMain.tLluvia.Interval = vb6.val(txtIntervaloPerdidaStaminaLluvia.Text);

	}

	static void Command1_Click() {
		/* FIXME: ON ERROR RESUME NEXT */
		AplicarIntervalos();

	}

	static void Command2_Click() {

		/* FIXME: ON ERROR GOTO Err */

		/* 'Intervalos */
		ES.WriteVar(Declaraciones.IniPath + "Server.ini", "INTERVALOS", "SanaIntervaloSinDescansar",
				vb6.str(Admin.SanaIntervaloSinDescansar));
		ES.WriteVar(Declaraciones.IniPath + "Server.ini", "INTERVALOS", "StaminaIntervaloSinDescansar",
				vb6.str(Admin.StaminaIntervaloSinDescansar));
		ES.WriteVar(Declaraciones.IniPath + "Server.ini", "INTERVALOS", "SanaIntervaloDescansar",
				vb6.str(Admin.SanaIntervaloDescansar));
		ES.WriteVar(Declaraciones.IniPath + "Server.ini", "INTERVALOS", "StaminaIntervaloDescansar",
				vb6.str(Admin.StaminaIntervaloDescansar));
		ES.WriteVar(Declaraciones.IniPath + "Server.ini", "INTERVALOS", "IntervaloSed", vb6.str(Admin.IntervaloSed));
		ES.WriteVar(Declaraciones.IniPath + "Server.ini", "INTERVALOS", "IntervaloHambre",
				vb6.str(Admin.IntervaloHambre));
		ES.WriteVar(Declaraciones.IniPath + "Server.ini", "INTERVALOS", "IntervaloVeneno",
				vb6.str(Admin.IntervaloVeneno));
		ES.WriteVar(Declaraciones.IniPath + "Server.ini", "INTERVALOS", "IntervaloParalizado",
				vb6.str(Admin.IntervaloParalizado));
		ES.WriteVar(Declaraciones.IniPath + "Server.ini", "INTERVALOS", "IntervaloInvisible",
				vb6.str(Admin.IntervaloInvisible));
		ES.WriteVar(Declaraciones.IniPath + "Server.ini", "INTERVALOS", "IntervaloFrio", vb6.str(Admin.IntervaloFrio));
		ES.WriteVar(Declaraciones.IniPath + "Server.ini", "INTERVALOS", "IntervaloWAVFX",
				vb6.str(Admin.IntervaloWavFx));
		ES.WriteVar(Declaraciones.IniPath + "Server.ini", "INTERVALOS", "IntervaloInvocacion",
				vb6.str(Admin.IntervaloInvocacion));
		ES.WriteVar(Declaraciones.IniPath + "Server.ini", "INTERVALOS", "IntervaloParaConexion",
				vb6.str(Admin.IntervaloParaConexion));

		/* '&&&&&&&&&&&&&&&&&&&&& TIMERS &&&&&&&&&&&&&&&&&&&&&&& */

		ES.WriteVar(Declaraciones.IniPath + "Server.ini", "INTERVALOS", "IntervaloLanzaHechizo",
				vb6.str(Admin.IntervaloUserPuedeCastear));
		ES.WriteVar(Declaraciones.IniPath + "Server.ini", "INTERVALOS", "IntervaloNpcAI", frmMain.TIMER_AI.Interval);
		ES.WriteVar(Declaraciones.IniPath + "Server.ini", "INTERVALOS", "IntervaloNpcPuedeAtacar",
				frmMain.npcataca.Interval);
		ES.WriteVar(Declaraciones.IniPath + "Server.ini", "INTERVALOS", "IntervaloTrabajo",
				vb6.str(Admin.IntervaloUserPuedeTrabajar));
		ES.WriteVar(Declaraciones.IniPath + "Server.ini", "INTERVALOS", "IntervaloUserPuedeAtacar",
				vb6.str(Admin.IntervaloUserPuedeAtacar));
		ES.WriteVar(Declaraciones.IniPath + "Server.ini", "INTERVALOS", "IntervaloPerdidaStaminaLluvia",
				frmMain.tLluvia.Interval);

		vb6.MsgBox("Los intervalos se han guardado sin problemas.");

		return;
		/* FIXME: Err : */
		vb6.MsgBox("Error al intentar grabar los intervalos");
	}

	static void ok_Click() {
		Me.Visible = false;
	}

}