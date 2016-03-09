
/*  AUTOMATICALLY CONVERTED FILE  */
/* [(0, 'ATTRIBUTE'), (1, 'VB_Name'), (5, '='), (4, '"clsByteQueue"')] */
/* [(0, 'ATTRIBUTE'), (1, 'VB_GlobalNameSpace'), (5, '='), (1, 'False')] */
/* [(0, 'ATTRIBUTE'), (1, 'VB_Creatable'), (5, '='), (1, 'True')] */
/* [(0, 'ATTRIBUTE'), (1, 'VB_PredeclaredId'), (5, '='), (1, 'False')] */
/* [(0, 'ATTRIBUTE'), (1, 'VB_Exposed'), (5, '='), (1, 'False')] */
/* '************************************************************** */
/* ' clsByteQueue.cls - FIFO list of bytes. */
/* ' Creates and manipulates byte arrays to be sent and received by both client and server */
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
/* ' FIFO list of bytes */
/* ' Used to create and manipulate the byte arrays to be sent and received by both client and server */
/* ' */
/* ' @author Juan Martín Sotuyo Dodero (Maraxus) juansotuyo@gmail.com */
/* ' @version 1.1.0 */
/* ' @date 20060427 */

/* '************************************************************************** */
/* ' - HISTORY */
/* '       v1.0.0  -   Initial release ( 2006/04/27 - Juan Martín Sotuyo Dodero ) */
/* '       v1.1.0  -   Added Single and Double support ( 2007/10/28 - Juan Martín Sotuyo Dodero ) */
/* '************************************************************************** */

import enums.*;

public class clsByteQueue {
	/* 'It's the default, but we make it explicit just in case... */

	/* '' */
	/* ' The error number thrown when there is not enough data in */
	/* ' the buffer to read the specified data type. */
	/* ' It's 9 (subscript out of range) + the object error constant */
	static final int NOT_ENOUGH_DATA = 9;

	/* '' */
	/* ' The error number thrown when there is not enough space in */
	/* ' the buffer to write. */
	static final int NOT_ENOUGH_SPACE = 10;

	/* '' */
	/* ' Default size of a data buffer (10 Kbs) */
	/* ' */
	/* ' @see Class_Initialize */
	static final int DATA_BUFFER = 10240;

	/* '' */
	/* ' The byte data */
	private int[] data = new int[0];

	/* '' */
	/* ' How big the data array is */
	private int queueCapacity;

	/* '' */
	/* ' How far into the data array have we written */
	private int queueLength;

	/* '' */
	/*
	 * ' CopyMemory is the fastest way to copy memory blocks, so we abuse of it
	 */
	/* ' */
	/* ' @param destination Where the data will be copied. */
	/* ' @param source The data to be copied. */
	/* ' @param length Number of bytes to be copied. */

	/* '' */
	/* ' Initializes the queue with the default queueCapacity */
	/* ' */
	/* ' @see DATA_BUFFER */

	void Class_Initialize() {
		/* '*************************************************** */
		/* 'Autor: Juan Martín Sotuyo Dodero (Maraxus) */
		/* 'Last Modification: 04/27/06 */
		/* 'Initializes the queue with the default queueCapacity */
		/* '*************************************************** */
		data = new Byte[0];
		data = (data == null) ? new Byte[DATA_BUFFER - 1] : java.util.Arrays.copyOf(data, DATA_BUFFER - 1);

		queueCapacity = DATA_BUFFER;
	}

	/* '' */
	/* ' Clean up and release resources */

	void Class_Terminate() {
		/* '*************************************************** */
		/* 'Autor: Juan Martín Sotuyo Dodero (Maraxus) */
		/* 'Last Modification: 04/27/06 */
		/* 'Clean up */
		/* '*************************************************** */
		/* FIXME: ERASE data */
	}

	/* '' */
	/* ' Copies another ByteQueue's data into this object. */
	/* ' */
	/* ' @param source The ByteQueue whose buffer will eb copied. */
	/* ' @remarks This method will resize the ByteQueue's buffer to match */
	/* ' the source. All previous data on this object will be lost. */

	void CopyBuffer(clsByteQueue /* FIXME BYREF!! */ source) {
		/* '*************************************************** */
		/* 'Autor: Juan Martín Sotuyo Dodero (Maraxus) */
		/* 'Last Modification: 04/27/06 */
		/* 'A Visual Basic equivalent of a Copy Contructor */
		/* '*************************************************** */
		if (source.length == 0) {
			/* 'Clear the list and exit */
			RemoveData(length);
			return;
		}

		/* ' Set capacity and resize array - make sure all data is lost */
		queueCapacity = source.Capacity;

		data = new Byte[0];
		data = (data == null) ? new Byte[queueCapacity - 1] : java.util.Arrays.copyOf(data, queueCapacity - 1);

		/* ' Read buffer */
		int[] buf;
		buf = new Byte[0];
		buf = (buf == null) ? new Byte[source.length - 1] : java.util.Arrays.copyOf(buf, source.length - 1);

		source.PeekBlock(buf, source.length);

		queueLength = 0;

		/* ' Write buffer */
		WriteBlock(buf, source.length);
	}

	/* '' */
	/* ' Returns the smaller of val1 and val2 */
	/* ' */
	/* ' @param val1 First value to compare */
	/* ' @param val2 Second Value to compare */
	/* ' @return The smaller of val1 and val2 */
	/*
	 * ' @remarks This method is faster than Iif() and cleaner, therefore it's
	 * used instead of it
	 */

	int min(int val1, int val2) {
		int retval = 0;
		/* '*************************************************** */
		/* 'Autor: Juan Martín Sotuyo Dodero (Maraxus) */
		/* 'Last Modification: 04/27/06 */
		/* 'It's faster than iif and I like it better */
		/* '*************************************************** */
		if (val1 < val2) {
			retval = val1;
		} else {
			retval = val2;
		}
		return retval;
	}

	/* '' */
	/*
	 * ' Writes a byte array at the end of the byte queue if there is enough
	 * space.
	 */
	/* ' Otherwise it throws NOT_ENOUGH_DATA. */
	/* ' */
	/*
	 * ' @param buf Byte array containing the data to be copied. MUST have 0 as
	 * the first index.
	 */
	/* ' @param datalength Total number of elements in the array */
	/* ' @return The actual number of bytes copied */
	/* ' @remarks buf MUST be Base 0 */
	/* ' @see RemoveData */
	/* ' @see ReadData */
	/* ' @see NOT_ENOUGH_DATA */

	int WriteData(int[] /* FIXME BYREF!! */ buf, int dataLength) {
 int retval = 0;
 /* '*************************************************** */
 /* 'Autor: Juan Martín Sotuyo Dodero (Maraxus) */
 /* 'Last Modification: 04/27/06 */
 /* 'If the queueCapacity allows it copyes a byte buffer to the queue, if not it throws NOT_ENOUGH_DATA */
 /* '*************************************************** */
 /* 'Check if there is enough free space */
  if (queueCapacity-queueLength-dataLength<0) {
  Err.raise[NOT_ENOUGH_SPACE];
  return retval;
 }
 
 /* 'Copy data from buffer */
 SysTray.CopyMemory(data[queueLength], buf[0], dataLength);
 
 /* 'Update length of data */
 queueLength = queueLength+dataLength;
 retval = dataLength;
return retval;
}

	/* '' */
	/*
	 * ' Reads a byte array from the beginning of the byte queue if there is
	 * enough data available.
	 */
	/* ' Otherwise it throws NOT_ENOUGH_DATA. */
	/* ' */
	/*
	 * ' @param buf Byte array where to copy the data. MUST have 0 as the first
	 * index and already be sized properly.
	 */
	/* ' @param datalength Total number of elements in the array */
	/* ' @return The actual number of bytes copied */
	/*
	 * ' @remarks buf MUST be Base 0 and be already resized to be able to
	 * contain the requested bytes.
	 */
	/*
	 * ' This method performs no checks of such things as being a private method
	 * it's supposed that the consistency of the module is to be kept.
	 */
	/*
	 * ' If there is not enough data available it will read all available data.
	 */
	/* ' @see WriteData */
	/* ' @see RemoveData */
	/* ' @see NOT_ENOUGH_DATA */

	int ReadData(int[] /* FIXME BYREF!! */ buf, int dataLength) {
 int retval = 0;
 /* '*************************************************** */
 /* 'Autor: Juan Martín Sotuyo Dodero (Maraxus) */
 /* 'Last Modification: 04/27/06 */
 /* 'If enough memory is available, it copies the requested number of bytes to the buffer */
 /* '*************************************************** */
 /* 'Check if we can read the number of bytes requested */
  if (dataLength>queueLength) {
  Err.raise[NOT_ENOUGH_DATA];
  return retval;
 }
 
 /* 'Copy data to buffer */
 SysTray.CopyMemory(buf[0], data[0], dataLength);
 retval = dataLength;
return retval;
}

	/* '' */
	/*
	 * ' Removes a given number of bytes from the beginning of the byte queue.
	 */
	/*
	 * ' If there is less data available than the requested amount it removes
	 * all data.
	 */
	/* ' */
	/* ' @param datalength Total number of bytes to remove */
	/* ' @return The actual number of bytes removed */
	/* ' @see WriteData */
	/* ' @see ReadData */

	int RemoveData(int dataLength) {
		int retval = 0;
		/* '*************************************************** */
		/* 'Autor: Juan Martín Sotuyo Dodero (Maraxus) */
		/* 'Last Modification: 04/27/06 */
		/* 'Moves forward the queue overwriting the first dataLength bytes */
		/* '*************************************************** */
		/* 'Figure out how many bytes we can remove */
		retval = min(dataLength, queueLength);

		/* 'Remove data - prevent rt9 when cleaning a full queue */
		if (retval != queueCapacity) {
			SysTray.CopyMemory(data[0], data[retval], queueLength - retval);
		}

		/* 'Update length */
		queueLength = queueLength - retval;
		return retval;
	}

	/* '' */
	/* ' Writes a single byte at the end of the queue */
	/* ' */
	/* ' @param value The value to be written */
	/* ' @return The number of bytes written */
	/* ' @see PeekByte */
	/* ' @see ReadByte */

	int WriteByte(int value) {
		int retval = 0;
		/* '*************************************************** */
		/* 'Autor: Juan Martín Sotuyo Dodero (Maraxus) */
		/* 'Last Modification: 04/27/06 */
		/* 'Writes a byte to the queue */
		/* '*************************************************** */
		int[] buf;

		buf[0] = value;

		retval = WriteData(buf, 1);
		return retval;
	}

	/* '' */
	/* ' Writes an integer at the end of the queue */
	/* ' */
	/* ' @param value The value to be written */
	/* ' @return The number of bytes written */
	/* ' @see PeekInteger */
	/* ' @see ReadInteger */

	int WriteInteger(int value) {
		int retval = 0;
		/* '*************************************************** */
		/* 'Autor: Juan Martín Sotuyo Dodero (Maraxus) */
		/* 'Last Modification: 04/27/06 */
		/* 'Writes an integer to the queue */
		/* '*************************************************** */
		int[] buf;

		/* 'Copy data to temp buffer */
		SysTray.CopyMemory(buf[0], value, 2);

		retval = WriteData(buf, 2);
		return retval;
	}

	/* '' */
	/* ' Writes a long at the end of the queue */
	/* ' */
	/* ' @param value The value to be written */
	/* ' @return The number of bytes written */
	/* ' @see PeekLong */
	/* ' @see ReadLong */

	int WriteLong(int value) {
		int retval = 0;
		/* '*************************************************** */
		/* 'Autor: Juan Martín Sotuyo Dodero (Maraxus) */
		/* 'Last Modification: 04/27/06 */
		/* 'Writes a long to the queue */
		/* '*************************************************** */
		int[] buf;

		/* 'Copy data to temp buffer */
		SysTray.CopyMemory(buf[0], value, 4);

		retval = WriteData(buf, 4);
		return retval;
	}

	/* '' */
	/* ' Writes a single at the end of the queue */
	/* ' */
	/* ' @param value The value to be written */
	/* ' @return The number of bytes written */
	/* ' @see PeekSingle */
	/* ' @see ReadSingle */

	int WriteSingle(float value) {
		int retval = 0;
		/* '*************************************************** */
		/* 'Autor: Juan Martín Sotuyo Dodero (Maraxus) */
		/* 'Last Modification: 10/28/07 */
		/* 'Writes a single to the queue */
		/* '*************************************************** */
		int[] buf;

		/* 'Copy data to temp buffer */
		SysTray.CopyMemory(buf[0], value, 4);

		retval = WriteData(buf, 4);
		return retval;
	}

	/* '' */
	/* ' Writes a double at the end of the queue */
	/* ' */
	/* ' @param value The value to be written */
	/* ' @return The number of bytes written */
	/* ' @see PeekDouble */
	/* ' @see ReadDouble */

	int WriteDouble(double value) {
		int retval = 0;
		/* '*************************************************** */
		/* 'Autor: Juan Martín Sotuyo Dodero (Maraxus) */
		/* 'Last Modification: 10/28/07 */
		/* 'Writes a double to the queue */
		/* '*************************************************** */
		int[] buf;

		/* 'Copy data to temp buffer */
		SysTray.CopyMemory(buf[0], value, 8);

		retval = WriteData(buf, 8);
		return retval;
	}

	/* '' */
	/* ' Writes a boolean value at the end of the queue */
	/* ' */
	/* ' @param value The value to be written */
	/* ' @return The number of bytes written */
	/* ' @see PeekBoolean */
	/* ' @see ReadBoolean */

	int WriteBoolean(boolean value) {
		int retval = 0;
		/* '*************************************************** */
		/* 'Autor: Juan Martín Sotuyo Dodero (Maraxus) */
		/* 'Last Modification: 04/27/06 */
		/* 'Writes a byte to the queue */
		/* '*************************************************** */
		int[] buf;

		if (value) {
			buf[0] = 1;
		}

		retval = WriteData(buf, 1);
		return retval;
	}

	/* '' */
	/* ' Writes a fixed length ASCII string at the end of the queue */
	/* ' */
	/* ' @param value The string to be written */
	/* ' @return The number of bytes written */
	/* ' @see PeekASCIIStringFixed */
	/* ' @see ReadASCIIStringFixed */

	int WriteASCIIStringFixed(String value) {
		int retval = 0;
		/* '*************************************************** */
		/* 'Autor: Juan Martín Sotuyo Dodero (Maraxus) */
		/* 'Last Modification: 04/27/06 */
		/* 'Writes a fixed length ASCII string to the queue */
		/* '*************************************************** */
		int[] buf;
		buf = new Byte[0];
		buf = (buf == null) ? new Byte[vb6.Len(value) - 1] : java.util.Arrays.copyOf(buf, vb6.Len(value) - 1);

		/* 'Copy data to temp buffer */
		SysTray.CopyMemory(buf[0], BYVALStrPtr[vb6.StrConv(value, vbFromUnicode)], vb6.Len(value));

		retval = WriteData(buf, vb6.Len(value));
		return retval;
	}

	/* '' */
	/* ' Writes a fixed length unicode string at the end of the queue */
	/* ' */
	/* ' @param value The string to be written */
	/* ' @return The number of bytes written */
	/* ' @see PeekUnicodeStringFixed */
	/* ' @see ReadUnicodeStringFixed */

	int WriteUnicodeStringFixed(String value) {
		int retval = 0;
		/* '*************************************************** */
		/* 'Autor: Juan Martín Sotuyo Dodero (Maraxus) */
		/* 'Last Modification: 04/27/06 */
		/* 'Writes a fixed length UNICODE string to the queue */
		/* '*************************************************** */
		int[] buf;
		buf = new Byte[0];
		buf = (buf == null) ? new Byte[vb6.LenB(value)] : java.util.Arrays.copyOf(buf, vb6.LenB(value));

		/* 'Copy data to temp buffer */
		SysTray.CopyMemory(buf[0], BYVALStrPtr[value], vb6.LenB(value));

		retval = WriteData(buf, vb6.LenB(value));
		return retval;
	}

	/* '' */
	/* ' Writes a variable length ASCII string at the end of the queue */
	/* ' */
	/* ' @param value The string to be written */
	/* ' @return The number of bytes written */
	/* ' @see PeekASCIIString */
	/* ' @see ReadASCIIString */

	int WriteASCIIString(String value) {
		int retval = 0;
		/* '*************************************************** */
		/* 'Autor: Juan Martín Sotuyo Dodero (Maraxus) */
		/* 'Last Modification: 04/27/06 */
		/* 'Writes a variable length ASCII string to the queue */
		/* '*************************************************** */
		int[] buf;
		buf = new Byte[0];
		buf = (buf == null) ? new Byte[vb6.Len(value) + 1] : java.util.Arrays.copyOf(buf, vb6.Len(value) + 1);

		/* 'Copy length to temp buffer */
		SysTray.CopyMemory(buf[0], vb6.CInt(vb6.Len(value)), 2);

		if (vb6.Len(value) > 0) {
			/* 'Copy data to temp buffer */
			SysTray.CopyMemory(buf[2], BYVALStrPtr[vb6.StrConv(value, vbFromUnicode)], vb6.Len(value));
		}

		retval = WriteData(buf, vb6.Len(value) + 2);
		return retval;
	}

	/* '' */
	/* ' Writes a variable length unicode string at the end of the queue */
	/* ' */
	/* ' @param value The string to be written */
	/* ' @return The number of bytes written */
	/* ' @see PeekUnicodeString */
	/* ' @see ReadUnicodeString */

	int WriteUnicodeString(String value) {
		int retval = 0;
		/* '*************************************************** */
		/* 'Autor: Juan Martín Sotuyo Dodero (Maraxus) */
		/* 'Last Modification: 04/27/06 */
		/* 'Writes a variable length UNICODE string to the queue */
		/* '*************************************************** */
		int[] buf;
		buf = new Byte[0];
		buf = (buf == null) ? new Byte[vb6.LenB(value) + 1] : java.util.Arrays.copyOf(buf, vb6.LenB(value) + 1);

		/* 'Copy length to temp buffer */
		SysTray.CopyMemory(buf[0], vb6.CInt(vb6.Len(value)), 2);

		if (vb6.Len(value) > 0) {
			/* 'Copy data to temp buffer */
			SysTray.CopyMemory(buf[2], BYVALStrPtr[value], vb6.LenB(value));
		}

		retval = WriteData(buf, vb6.LenB(value) + 2);
		return retval;
	}

	/* '' */
	/* ' Writes a byte array at the end of the queue */
	/* ' */
	/* ' @param value The byte array to be written. MUST be Base 0. */
	/*
	 * ' @param length The number of elements to copy from the byte array. If
	 * less than 0 it will copy the whole array.
	 */
	/* ' @return The number of bytes written */
	/* ' @remarks value() MUST be Base 0. */
	/* ' @see PeekBlock */
	/* ' @see ReadBlock */

	int WriteBlock(int[] /* FIXME BYREF!! */ value) {
		return WriteBlock(value, -1);
	}

	int WriteBlock(int[] /* FIXME BYREF!! */ value, int length) {
 int retval = 0;
 /* '*************************************************** */
 /* 'Autor: Juan Martín Sotuyo Dodero (Maraxus) */
 /* 'Last Modification: 04/27/06 */
 /* 'Writes a byte array to the queue */
 /* '*************************************************** */
 /* 'Prevent from copying memory outside the array */
 if (length>vb6.UBound(value[])+1 || length<0) {
 length = vb6.UBound(value[])+1;
 }
 
 retval = WriteData(value, length);
return retval;
}

	/* '' */
	/* ' Reads a single byte from the begining of the queue and removes it */
	/* ' */
	/* ' @return The read value */
	/* ' @remarks Read methods removes the data from the queue. */
	/* ' Data removed can't be recovered by the queue in any way */
	/* ' @see PeekByte */
	/* ' @see WriteByte */

	int ReadByte() {
		int retval = 0;
		/* '*************************************************** */
		/* 'Autor: Juan Martín Sotuyo Dodero (Maraxus) */
		/* 'Last Modification: 04/27/06 */
		/* 'Reads a byte from the queue and removes it */
		/* '*************************************************** */
		int[] buf;

		/* 'Read the data and remove it */
		RemoveData(ReadData(buf, 1));

		retval = buf[0];
		return retval;
	}

	/* '' */
	/* ' Reads an integer from the begining of the queue and removes it */
	/* ' */
	/* ' @return The read value */
	/* ' @remarks Read methods removes the data from the queue. */
	/* ' Data removed can't be recovered by the queue in any way */
	/* ' @see PeekInteger */
	/* ' @see WriteInteger */

	int ReadInteger() {
		int retval = 0;
		/* '*************************************************** */
		/* 'Autor: Juan Martín Sotuyo Dodero (Maraxus) */
		/* 'Last Modification: 04/27/06 */
		/* 'Reads an integer from the queue and removes it */
		/* '*************************************************** */
		int[] buf;

		/* 'Read the data and remove it */
		RemoveData(ReadData(buf, 2));

		/* 'Copy data to temp buffer */
		SysTray.CopyMemory(retval, buf[0], 2);
		return retval;
	}

	/* '' */
	/* ' Reads a long from the begining of the queue and removes it */
	/* ' */
	/* ' @return The read value */
	/* ' @remarks Read methods removes the data from the queue. */
	/* ' Data removed can't be recovered by the queue in any way */
	/* ' @see PeekLong */
	/* ' @see WriteLong */

	int ReadLong() {
		int retval = 0;
		/* '*************************************************** */
		/* 'Autor: Juan Martín Sotuyo Dodero (Maraxus) */
		/* 'Last Modification: 04/27/06 */
		/* 'Reads a long from the queue and removes it */
		/* '*************************************************** */
		int[] buf;

		/* 'Read the data and remove it */
		RemoveData(ReadData(buf, 4));

		/* 'Copy data to temp buffer */
		SysTray.CopyMemory(retval, buf[0], 4);
		return retval;
	}

	/* '' */
	/* ' Reads a single from the begining of the queue and removes it */
	/* ' */
	/* ' @return The read value */
	/* ' @remarks Read methods removes the data from the queue. */
	/* ' Data removed can't be recovered by the queue in any way */
	/* ' @see PeekSingle */
	/* ' @see WriteSingle */

	float ReadSingle() {
		float retval = 0.0f;
		/* '*************************************************** */
		/* 'Autor: Juan Martín Sotuyo Dodero (Maraxus) */
		/* 'Last Modification: 10/28/07 */
		/* 'Reads a single from the queue and removes it */
		/* '*************************************************** */
		int[] buf;

		/* 'Read the data and remove it */
		RemoveData(ReadData(buf, 4));

		/* 'Copy data to temp buffer */
		SysTray.CopyMemory(retval, buf[0], 4);
		return retval;
	}

	/* '' */
	/* ' Reads a double from the begining of the queue and removes it */
	/* ' */
	/* ' @return The read value */
	/* ' @remarks Read methods removes the data from the queue. */
	/* ' Data removed can't be recovered by the queue in any way */
	/* ' @see PeekDouble */
	/* ' @see WriteDouble */

	double ReadDouble() {
		double retval = 0.0;
		/* '*************************************************** */
		/* 'Autor: Juan Martín Sotuyo Dodero (Maraxus) */
		/* 'Last Modification: 10/28/07 */
		/* 'Reads a double from the queue and removes it */
		/* '*************************************************** */
		int[] buf;

		/* 'Read the data and remove it */
		RemoveData(ReadData(buf, 8));

		/* 'Copy data to temp buffer */
		SysTray.CopyMemory(retval, buf[0], 8);
		return retval;
	}

	/* '' */
	/* ' Reads a Boolean from the begining of the queue and removes it */
	/* ' */
	/* ' @return The read value */
	/* ' @remarks Read methods removes the data from the queue. */
	/* ' Data removed can't be recovered by the queue in any way */
	/* ' @see PeekBoolean */
	/* ' @see WriteBoolean */

	boolean ReadBoolean() {
		boolean retval = false;
		/* '*************************************************** */
		/* 'Autor: Juan Martín Sotuyo Dodero (Maraxus) */
		/* 'Last Modification: 04/27/06 */
		/* 'Reads a Boolean from the queue and removes it */
		/* '*************************************************** */
		int[] buf;

		/* 'Read the data and remove it */
		RemoveData(ReadData(buf, 1));

		if (buf[0] == 1) {
			retval = true;
		}
		return retval;
	}

	/* '' */
	/*
	 * ' Reads a fixed length ASCII string from the begining of the queue and
	 * removes it
	 */
	/* ' */
	/* ' @param length The length of the string to be read */
	/* ' @return The read string */
	/* ' @remarks Read methods removes the data from the queue. */
	/* ' Data removed can't be recovered by the queue in any way */
	/*
	 * ' If there is not enough data to read the complete string then nothing is
	 * removed and an empty string is returned
	 */
	/* ' @see PeekASCIIStringFixed */
	/* ' @see WriteUnicodeStringFixed */

	String ReadASCIIStringFixed(int length) {
 String retval;
 /* '*************************************************** */
 /* 'Autor: Juan Martín Sotuyo Dodero (Maraxus) */
 /* 'Last Modification: 04/27/06 */
 /* 'Reads a fixed length ASCII string from the queue and removes it */
 /* '*************************************************** */
 if (length<=0) {
 return retval;
 }
 
  if (queueLength>=length) {
  int[] buf;
  buf = new Byte[0];
  buf = (buf == null) ? new Byte[length-1] : java.util.Arrays.copyOf(buf, length-1);
  
  /* 'Read the data and remove it */
  RemoveData(ReadData(buf, length));
  
  retval = vb6.StrConv(buf, vbUnicode);
  } else {
  Err.raise[NOT_ENOUGH_DATA];
 }
return retval;
}

	/* '' */
	/*
	 * ' Reads a fixed length unicode string from the begining of the queue and
	 * removes it
	 */
	/* ' */
	/* ' @param length The length of the string to be read. */
	/*
	 * ' @return The read string if enough data is available, an empty string
	 * otherwise.
	 */
	/* ' @remarks Read methods removes the data from the queue. */
	/* ' Data removed can't be recovered by the queue in any way. */
	/*
	 * ' If there is not enough data to read the complete string then nothing is
	 * removed and an empty string is returned
	 */
	/* ' @see PeekUnicodeStringFixed */
	/* ' @see WriteUnicodeStringFixed */

	String ReadUnicodeStringFixed(int length) {
 String retval;
 /* '*************************************************** */
 /* 'Autor: Juan Martín Sotuyo Dodero (Maraxus) */
 /* 'Last Modification: 04/27/06 */
 /* 'Reads a fixed length UNICODE string from the queue and removes it */
 /* '*************************************************** */
 if (length<=0) {
 return retval;
 }
 
  if (queueLength>=length*2) {
  int[] buf;
  buf = new Byte[0];
  buf = (buf == null) ? new Byte[length*2-1] : java.util.Arrays.copyOf(buf, length*2-1);
  
  /* 'Read the data and remove it */
  RemoveData(ReadData(buf, length*2));
  
  retval = buf;
  } else {
  Err.raise[NOT_ENOUGH_DATA];
 }
return retval;
}

	/* '' */
	/*
	 * ' Reads a variable length ASCII string from the begining of the queue and
	 * removes it
	 */
	/* ' */
	/* ' @return The read string */
	/* ' @remarks Read methods removes the data from the queue. */
	/* ' Data removed can't be recovered by the queue in any way */
	/*
	 * ' If there is not enough data to read the complete string then nothing is
	 * removed and an empty string is returned
	 */
	/* ' @see PeekASCIIString */
	/* ' @see WriteASCIIString */

	String ReadASCIIString() {
 String retval;
 /* '*************************************************** */
 /* 'Autor: Juan Martín Sotuyo Dodero (Maraxus) */
 /* 'Last Modification: 04/27/06 */
 /* 'Reads a variable length ASCII string from the queue and removes it */
 /* '*************************************************** */
 int[] buf;
 int length = 0;
 
 /* 'Make sure we can read a valid length */
  if (queueLength>1) {
  /* 'Read the length */
  ReadData(buf, 2);
  SysTray.CopyMemory(length, buf[0], 2);
  
  /* 'Make sure there are enough bytes */
   if (queueLength>=length+2) {
   /* 'Remove the length */
   RemoveData(2);
   
    if (length>0) {
    int[] buf2;
    buf2 = new Byte[0];
    buf2 = (buf2 == null) ? new Byte[length-1] : java.util.Arrays.copyOf(buf2, length-1);
    
    /* 'Read the data and remove it */
    RemoveData(ReadData(buf2, length));
    
    retval = vb6.StrConv(buf2, vbUnicode);
   }
   } else {
   Err.raise[NOT_ENOUGH_DATA];
  }
  } else {
  Err.raise[NOT_ENOUGH_DATA];
 }
return retval;
}

	/* '' */
	/*
	 * ' Reads a variable length unicode string from the begining of the queue
	 * and removes it
	 */
	/* ' */
	/*
	 * ' @return The read string if enough data is available, an empty string
	 * otherwise.
	 */
	/* ' @remarks Read methods removes the data from the queue. */
	/* ' Data removed can't be recovered by the queue in any way */
	/*
	 * ' If there is not enough data to read the complete string then nothing is
	 * removed and an empty string is returned
	 */
	/* ' @see PeekUnicodeString */
	/* ' @see WriteUnicodeString */

	String ReadUnicodeString() {
 String retval;
 /* '*************************************************** */
 /* 'Autor: Juan Martín Sotuyo Dodero (Maraxus) */
 /* 'Last Modification: 04/27/06 */
 /* 'Reads a variable length UNICODE string from the queue and removes it */
 /* '*************************************************** */
 int[] buf;
 int length = 0;
 
 /* 'Make sure we can read a valid length */
  if (queueLength>1) {
  /* 'Read the length */
  ReadData(buf, 2);
  SysTray.CopyMemory(length, buf[0], 2);
  
  /* 'Make sure there are enough bytes */
   if (queueLength>=length*2+2) {
   /* 'Remove the length */
   RemoveData(2);
   
   int[] buf2;
   buf2 = new Byte[0];
   buf2 = (buf2 == null) ? new Byte[length*2-1] : java.util.Arrays.copyOf(buf2, length*2-1);
   
   /* 'Read the data and remove it */
   RemoveData(ReadData(buf2, length*2));
   
   retval = buf2;
   } else {
   Err.raise[NOT_ENOUGH_DATA];
  }
  } else {
  Err.raise[NOT_ENOUGH_DATA];
 }
return retval;
}

	/* '' */
	/* ' Reads a byte array from the begining of the queue and removes it */
	/* ' */
	/*
	 * ' @param block Byte array which will contain the read data. MUST be Base
	 * 0 and previously resized to contain the requested amount of bytes.
	 */
	/* ' @param dataLength Number of bytes to retrieve from the queue. */
	/* ' @return The number of read bytes. */
	/*
	 * ' @remarks The block() array MUST be Base 0 and previously resized to be
	 * able to contain the requested bytes.
	 */
	/* ' Read methods removes the data from the queue. */
	/* ' Data removed can't be recovered by the queue in any way */
	/* ' @see PeekBlock */
	/* ' @see WriteBlock */

	int ReadBlock(int[] /* FIXME BYREF!! */ block, int dataLength) {
 int retval = 0;
 /* '*************************************************** */
 /* 'Autor: Juan Martín Sotuyo Dodero (Maraxus) */
 /* 'Last Modification: 04/27/06 */
 /* 'Reads a byte array from the queue and removes it */
 /* '*************************************************** */
 /* 'Read the data and remove it */
 if (dataLength>0) {
 retval = RemoveData(ReadData(block[], dataLength));
 }
return retval;
}

	/* '' */
	/*
	 * ' Reads a single byte from the begining of the queue but DOES NOT remove
	 * it.
	 */
	/* ' */
	/* ' @return The read value. */
	/*
	 * ' @remarks Peek methods, unlike Read methods, don't remove the data from
	 * the queue.
	 */
	/* ' @see ReadByte */
	/* ' @see WriteByte */

	int PeekByte() {
		int retval = 0;
		/* '*************************************************** */
		/* 'Autor: Juan Martín Sotuyo Dodero (Maraxus) */
		/* 'Last Modification: 04/27/06 */
		/* 'Reads a byte from the queue but doesn't removes it */
		/* '*************************************************** */
		int[] buf;

		/* 'Read the data and remove it */
		ReadData(buf, 1);

		retval = buf[0];
		return retval;
	}

	/* '' */
	/*
	 * ' Reads an integer from the begining of the queue but DOES NOT remove it.
	 */
	/* ' */
	/* ' @return The read value. */
	/*
	 * ' @remarks Peek methods, unlike Read methods, don't remove the data from
	 * the queue.
	 */
	/* ' @see ReadInteger */
	/* ' @see WriteInteger */

	int PeekInteger() {
		int retval = 0;
		/* '*************************************************** */
		/* 'Autor: Juan Martín Sotuyo Dodero (Maraxus) */
		/* 'Last Modification: 04/27/06 */
		/* 'Reads an integer from the queue but doesn't removes it */
		/* '*************************************************** */
		int[] buf;

		/* 'Read the data and remove it */
		ReadData(buf, 2);

		/* 'Copy data to temp buffer */
		SysTray.CopyMemory(retval, buf[0], 2);
		return retval;
	}

	/* '' */
	/* ' Reads a long from the begining of the queue but DOES NOT remove it. */
	/* ' */
	/* ' @return The read value. */
	/*
	 * ' @remarks Peek methods, unlike Read methods, don't remove the data from
	 * the queue.
	 */
	/* ' @see ReadLong */
	/* ' @see WriteLong */

	int PeekLong() {
		int retval = 0;
		/* '*************************************************** */
		/* 'Autor: Juan Martín Sotuyo Dodero (Maraxus) */
		/* 'Last Modification: 04/27/06 */
		/* 'Reads a long from the queue but doesn't removes it */
		/* '*************************************************** */
		int[] buf;

		/* 'Read the data and remove it */
		ReadData(buf, 4);

		/* 'Copy data to temp buffer */
		SysTray.CopyMemory(retval, buf[0], 4);
		return retval;
	}

	/* '' */
	/*
	 * ' Reads a single from the begining of the queue but DOES NOT remove it.
	 */
	/* ' */
	/* ' @return The read value. */
	/*
	 * ' @remarks Peek methods, unlike Read methods, don't remove the data from
	 * the queue.
	 */
	/* ' @see ReadSingle */
	/* ' @see WriteSingle */

	float PeekSingle() {
		float retval = 0.0f;
		/* '*************************************************** */
		/* 'Autor: Juan Martín Sotuyo Dodero (Maraxus) */
		/* 'Last Modification: 10/28/07 */
		/* 'Reads a single from the queue but doesn't removes it */
		/* '*************************************************** */
		int[] buf;

		/* 'Read the data and remove it */
		ReadData(buf, 4);

		/* 'Copy data to temp buffer */
		SysTray.CopyMemory(retval, buf[0], 4);
		return retval;
	}

	/* '' */
	/*
	 * ' Reads a double from the begining of the queue but DOES NOT remove it.
	 */
	/* ' */
	/* ' @return The read value. */
	/*
	 * ' @remarks Peek methods, unlike Read methods, don't remove the data from
	 * the queue.
	 */
	/* ' @see ReadDouble */
	/* ' @see WriteDouble */

	double PeekDouble() {
		double retval = 0.0;
		/* '*************************************************** */
		/* 'Autor: Juan Martín Sotuyo Dodero (Maraxus) */
		/* 'Last Modification: 10/28/07 */
		/* 'Reads a double from the queue but doesn't removes it */
		/* '*************************************************** */
		int[] buf;

		/* 'Read the data and remove it */
		ReadData(buf, 8);

		/* 'Copy data to temp buffer */
		SysTray.CopyMemory(retval, buf[0], 8);
		return retval;
	}

	/* '' */
	/*
	 * ' Reads a Bollean from the begining of the queue but DOES NOT remove it.
	 */
	/* ' */
	/* ' @return The read value. */
	/*
	 * ' @remarks Peek methods, unlike Read methods, don't remove the data from
	 * the queue.
	 */
	/* ' @see ReadBoolean */
	/* ' @see WriteBoolean */

	boolean PeekBoolean() {
		boolean retval = false;
		/* '*************************************************** */
		/* 'Autor: Juan Martín Sotuyo Dodero (Maraxus) */
		/* 'Last Modification: 04/27/06 */
		/* 'Reads a Boolean from the queue but doesn't removes it */
		/* '*************************************************** */
		int[] buf;

		/* 'Read the data and remove it */
		ReadData(buf, 1);

		if (buf[0] == 1) {
			retval = true;
		}
		return retval;
	}

	/* '' */
	/*
	 * ' Reads a fixed length ASCII string from the begining of the queue but
	 * DOES NOT remove it.
	 */
	/* ' */
	/* ' @param length The length of the string to be read */
	/*
	 * ' @return The read string if enough data is available, an empty string
	 * otherwise.
	 */
	/*
	 * ' @remarks Peek methods, unlike Read methods, don't remove the data from
	 * the queue.
	 */
	/*
	 * ' If there is not enough data to read the complete string then an empty
	 * string is returned
	 */
	/* ' @see ReadASCIIStringFixed */
	/* ' @see WriteASCIIStringFixed */

	String PeekASCIIStringFixed(int length) {
 String retval;
 /* '*************************************************** */
 /* 'Autor: Juan Martín Sotuyo Dodero (Maraxus) */
 /* 'Last Modification: 04/27/06 */
 /* 'Reads a fixed length ASCII string from the queue but doesn't removes it */
 /* '*************************************************** */
 if (length<=0) {
 return retval;
 }
 
  if (queueLength>=length) {
  int[] buf;
  buf = new Byte[0];
  buf = (buf == null) ? new Byte[length-1] : java.util.Arrays.copyOf(buf, length-1);
  
  /* 'Read the data and remove it */
  ReadData(buf, length);
  
  retval = vb6.StrConv(buf, vbUnicode);
  } else {
  Err.raise[NOT_ENOUGH_DATA];
 }
return retval;
}

	/* '' */
	/*
	 * ' Reads a fixed length unicode string from the begining of the queue but
	 * DOES NOT remove it.
	 */
	/* ' */
	/* ' @param length The length of the string to be read */
	/*
	 * ' @return The read string if enough data is available, an empty string
	 * otherwise.
	 */
	/*
	 * ' @remarks Peek methods, unlike Read methods, don't remove the data from
	 * the queue.
	 */
	/*
	 * ' If there is not enough data to read the complete string then an empty
	 * string is returned
	 */
	/* ' @see ReadUnicodeStringFixed */
	/* ' @see WriteUnicodeStringFixed */

	String PeekUnicodeStringFixed(int length) {
 String retval;
 /* '*************************************************** */
 /* 'Autor: Juan Martín Sotuyo Dodero (Maraxus) */
 /* 'Last Modification: 04/27/06 */
 /* 'Reads a fixed length UNICODE string from the queue but doesn't removes it */
 /* '*************************************************** */
 if (length<=0) {
 return retval;
 }
 
  if (queueLength>=length*2) {
  int[] buf;
  buf = new Byte[0];
  buf = (buf == null) ? new Byte[length*2-1] : java.util.Arrays.copyOf(buf, length*2-1);
  
  /* 'Read the data and remove it */
  ReadData(buf, length*2);
  
  retval = buf;
  } else {
  Err.raise[NOT_ENOUGH_DATA];
 }
return retval;
}

	/* '' */
	/*
	 * ' Reads a variable length ASCII string from the begining of the queue but
	 * DOES NOT remove it.
	 */
	/* ' */
	/*
	 * ' @return The read string if enough data is available, an empty string
	 * otherwise.
	 */
	/*
	 * ' @remarks Peek methods, unlike Read methods, don't remove the data from
	 * the queue.
	 */
	/*
	 * ' If there is not enough data to read the complete string then an empty
	 * string is returned
	 */
	/* ' @see ReadASCIIString */
	/* ' @see WriteASCIIString */

	String PeekASCIIString() {
 String retval;
 /* '*************************************************** */
 /* 'Autor: Juan Martín Sotuyo Dodero (Maraxus) */
 /* 'Last Modification: 04/27/06 */
 /* 'Reads a variable length ASCII string from the queue but doesn't removes it */
 /* '*************************************************** */
 int[] buf;
 int length = 0;
 
 /* 'Make sure we can read a valid length */
  if (queueLength>1) {
  /* 'Read the length */
  ReadData(buf, 2);
  SysTray.CopyMemory(length, buf[0], 2);
  
  /* 'Make sure there are enough bytes */
   if (queueLength>=length+2) {
   int[] buf2;
   buf2 = new Byte[0];
   buf2 = (buf2 == null) ? new Byte[length+1] : java.util.Arrays.copyOf(buf2, length+1);
   
   /* 'Read the data (we have to read the length again) */
   ReadData(buf2, length+2);
   
    if (length>0) {
    /* 'Get rid of the length */
    int[] buf3;
    buf3 = new Byte[0];
    buf3 = (buf3 == null) ? new Byte[length-1] : java.util.Arrays.copyOf(buf3, length-1);
    SysTray.CopyMemory(buf3[0], buf2[2], length);
    
    retval = vb6.StrConv(buf3, vbUnicode);
   }
   } else {
   Err.raise[NOT_ENOUGH_DATA];
  }
  } else {
  Err.raise[NOT_ENOUGH_DATA];
 }
return retval;
}

	/* '' */
	/*
	 * ' Reads a variable length unicode string from the begining of the queue
	 * but DOES NOT remove it.
	 */
	/* ' */
	/*
	 * ' @return The read string if enough data is available, an empty string
	 * otherwise.
	 */
	/*
	 * ' @remarks Peek methods, unlike Read methods, don't remove the data from
	 * the queue.
	 */
	/*
	 * ' If there is not enough data to read the complete string then an empty
	 * string is returned
	 */
	/* ' @see ReadUnicodeString */
	/* ' @see WriteUnicodeString */

	String PeekUnicodeString() {
 String retval;
 /* '*************************************************** */
 /* 'Autor: Juan Martín Sotuyo Dodero (Maraxus) */
 /* 'Last Modification: 04/27/06 */
 /* 'Reads a variable length UNICODE string from the queue but doesn't removes it */
 /* '*************************************************** */
 int[] buf;
 int length = 0;
 
 /* 'Make sure we can read a valid length */
  if (queueLength>1) {
  /* 'Read the length */
  ReadData(buf, 2);
  SysTray.CopyMemory(length, buf[0], 2);
  
  /* 'Make sure there are enough bytes */
   if (queueLength>=length*2+2) {
   int[] buf2;
   buf2 = new Byte[0];
   buf2 = (buf2 == null) ? new Byte[length*2+1] : java.util.Arrays.copyOf(buf2, length*2+1);
   
   /* 'Read the data (we need to read the length again) */
   ReadData(buf2, length*2+2);
   
   /* 'Get rid of the length bytes */
   int[] buf3;
   buf3 = new Byte[0];
   buf3 = (buf3 == null) ? new Byte[length*2-1] : java.util.Arrays.copyOf(buf3, length*2-1);
   SysTray.CopyMemory(buf3[0], buf2[2], length*2);
   
   retval = buf3;
   } else {
   Err.raise[NOT_ENOUGH_DATA];
  }
  } else {
  Err.raise[NOT_ENOUGH_DATA];
 }
return retval;
}

	/* '' */
	/*
	 * ' Reads a byte array from the begining of the queue but DOES NOT remove
	 * it.
	 */
	/* ' */
	/*
	 * ' @param block() Byte array that will contain the read data. MUST be Base
	 * 0 and previously resized to contain the requested amount of bytes.
	 */
	/* ' @param dataLength Number of bytes to be read */
	/* ' @return The actual number of read bytes. */
	/*
	 * ' @remarks Peek methods, unlike Read methods, don't remove the data from
	 * the queue.
	 */
	/* ' @see ReadBlock */
	/* ' @see WriteBlock */

	int PeekBlock(int[] /* FIXME BYREF!! */ block, int dataLength) {
 int retval = 0;
 /* '*************************************************** */
 /* 'Autor: Juan Martín Sotuyo Dodero (Maraxus) */
 /* 'Last Modification: 04/27/06 */
 /* 'Reads a byte array from the queue but doesn't removes it */
 /* '*************************************************** */
 /* 'Read the data */
 if (dataLength>0) {
 retval = ReadData(block[], dataLength);
 }
return retval;
}

	/* '' */
	/* ' Retrieves the current capacity of the queue. */
	/* ' */
	/* ' @return The current capacity of the queue. */

	int Capacity() {
		int retval = 0;
		/* '*************************************************** */
		/* 'Autor: Juan Martín Sotuyo Dodero (Maraxus) */
		/* 'Last Modification: 04/27/06 */
		/* 'Retrieves the current capacity of the queue */
		/* '*************************************************** */
		retval = queueCapacity;
		return retval;
	}

	/* '' */
	/* ' Sets the capacity of the queue. */
	/* ' */
	/* ' @param value The new capacity of the queue. */
	/*
	 * ' @remarks If the new capacity is smaller than the current Length, all
	 * exceeding data is lost.
	 */
	/* ' @see Length */

	void Capacity(int value) {
		/* '*************************************************** */
		/* 'Autor: Juan Martín Sotuyo Dodero (Maraxus) */
		/* 'Last Modification: 04/27/06 */
		/* 'Sets the current capacity of the queue. */
		/* 'All data in the queue exceeding the new capacity is lost */
		/* '*************************************************** */
		/* 'Upate capacity */
		queueCapacity = value;

		/* 'All extra data is lost */
		if (length > value) {
			queueLength = value;
		}

		/* 'Resize the queue */
		data = (data == null) ? new Byte[queueCapacity - 1] : java.util.Arrays.copyOf(data, queueCapacity - 1);
	}

	/* '' */
	/* ' Retrieves the length of the total data in the queue. */
	/* ' */
	/* ' @return The length of the total data in the queue. */

	int length() {
		int retval = 0;
		/* '*************************************************** */
		/* 'Autor: Juan Martín Sotuyo Dodero (Maraxus) */
		/* 'Last Modification: 04/27/06 */
		/* 'Retrieves the current number of bytes in the queue */
		/* '*************************************************** */
		retval = queueLength;
		return retval;
	}

	/* '' */
	/* ' Retrieves the NOT_ENOUGH_DATA error code. */
	/* ' */
	/* ' @return NOT_ENOUGH_DATA. */

	int NotEnoughDataErrCode() {
		int retval = 0;
		/* '*************************************************** */
		/* 'Autor: Juan Martín Sotuyo Dodero (Maraxus) */
		/* 'Last Modification: 04/27/06 */
		/* 'Retrieves the NOT_ENOUGH_DATA error code */
		/* '*************************************************** */
		retval = NOT_ENOUGH_DATA;
		return retval;
	}

	/* '' */
	/* ' Retrieves the NOT_ENOUGH_SPACE error code. */
	/* ' */
	/* ' @return NOT_ENOUGH_SPACE. */

	int NotEnoughSpaceErrCode() {
		int retval = 0;
		/* '*************************************************** */
		/* 'Autor: Juan Martín Sotuyo Dodero (Maraxus) */
		/* 'Last Modification: 04/27/06 */
		/* 'Retrieves the NOT_ENOUGH_SPACE error code */
		/* '*************************************************** */
		retval = NOT_ENOUGH_SPACE;
		return retval;
	}

}