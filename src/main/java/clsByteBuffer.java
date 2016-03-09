

/*  AUTOMATICALLY CONVERTED FILE  */
/* [(0, 'ATTRIBUTE'), (1, 'VB_Name'), (5, '='), (4, '"clsByteBuffer"')] */
/* [(0, 'ATTRIBUTE'), (1, 'VB_GlobalNameSpace'), (5, '='), (1, 'False')] */
/* [(0, 'ATTRIBUTE'), (1, 'VB_Creatable'), (5, '='), (1, 'True')] */
/* [(0, 'ATTRIBUTE'), (1, 'VB_PredeclaredId'), (5, '='), (1, 'False')] */
/* [(0, 'ATTRIBUTE'), (1, 'VB_Exposed'), (5, '='), (1, 'False')] */
import enums.*;

public class clsByteBuffer {

	private int[] data = new int[0];

	private int CurrentPos;
	private int lastPos;
	private int hFile;

	static final int INVALID_FILE_HANDLER = 1;
	static final int DEFAULT_MAX_SIZE_FILE = 65535;
	static final int BYTE_SIZE = 1;
	static final int BOOL_SIZE = 2;
	static final int INTEGER_SIZE = 2;
	static final int LONG_SIZE = 4;
	static final int SINGLE_SIZE = 4;
	static final int DOUBLE_SIZE = 8;
	static final int STRING_LENGTH_SIZE = 2;

	void initializeReader(int[] /* FIXME BYREF!! */ arrayByte) {
		lastPos = vb6.UBound(arrayByte);
		data = new Byte[0];
		data = (data == null) ? new Byte[lastPos] : java.util.Arrays.copyOf(data, lastPos);

		SysTray.CopyMemory(data[0], arrayByte[0], lastPos + 1);
		CurrentPos = 0;
	}

	void initializeWriter(int fileHandler) {
		data = new Byte[0];
		data = (data == null) ? new Byte[DEFAULT_MAX_SIZE_FILE * 20]
				: java.util.Arrays.copyOf(data, DEFAULT_MAX_SIZE_FILE * 20);

		hFile = fileHandler;
		CurrentPos = 0;
		lastPos = -1;
	}

	void getBytes(int[] /* FIXME BYREF!! */ destination) {
		getBytes(destination, -1);
	}

	void getBytes(int[] /* FIXME BYREF!! */ destination, int length) {
		if (length >= 0) {
			SysTray.CopyMemory(destination[0], data[CurrentPos], length);
		} else {
			SysTray.CopyMemory(destination[0], data[0], lastPos + 1);
		}
	}

	int getByte() {
		int retval = 0;
		retval = data[CurrentPos];
		CurrentPos = CurrentPos + BYTE_SIZE;
		return retval;
	}

	boolean getBoolean() {
		boolean retval = false;
		SysTray.CopyMemory(retval, data[CurrentPos], BOOL_SIZE);
		CurrentPos = CurrentPos + BOOL_SIZE;
		return retval;
	}

	int getInteger() {
		int retval = 0;
		SysTray.CopyMemory(retval, data[CurrentPos], INTEGER_SIZE);
		CurrentPos = CurrentPos + INTEGER_SIZE;
		return retval;
	}

	int getLong() {
		int retval = 0;
		SysTray.CopyMemory(retval, data[CurrentPos], LONG_SIZE);
		CurrentPos = CurrentPos + LONG_SIZE;
		return retval;
	}

	float getSingle() {
		float retval = 0.0f;
		SysTray.CopyMemory(retval, data[CurrentPos], SINGLE_SIZE);
		CurrentPos = CurrentPos + SINGLE_SIZE;
		return retval;
	}

	double getDouble() {
		double retval = 0.0;
		SysTray.CopyMemory(retval, data[CurrentPos], DOUBLE_SIZE);
		CurrentPos = CurrentPos + DOUBLE_SIZE;
		return retval;
	}

	String getString() {
		return getString(-1);
	}

	String getString(int length) {
		String retval;
		int[] ret;

		if (length < 0) {
			length = getInteger();
			retval = getString(length);
		} else {
			if (length > 0) {
				ret = new Byte[0];
				ret = (ret == null) ? new Byte[length - 1] : java.util.Arrays.copyOf(ret, length - 1);

				SysTray.CopyMemory(ret[0], data[CurrentPos], length);

				retval = vb6.StrConv(ret, vbUnicode);
				CurrentPos = CurrentPos + length;
			}
		}

		return retval;
	}

	void putByte(int value) {
		data[lastPos + 1] = value;
		lastPos = lastPos + BYTE_SIZE;
	}

	void putBoolean(boolean value) {
		SysTray.CopyMemory(data[lastPos + 1], value, BOOL_SIZE);
		lastPos = lastPos + BOOL_SIZE;
	}

	void putInteger(int value) {
		SysTray.CopyMemory(data[lastPos + 1], value, INTEGER_SIZE);
		lastPos = lastPos + INTEGER_SIZE;
	}

	void putLong(int value) {
		SysTray.CopyMemory(data[lastPos + 1], value, LONG_SIZE);
		lastPos = lastPos + LONG_SIZE;
	}

	void putSingle(float value) {
		SysTray.CopyMemory(data[lastPos + 1], value, SINGLE_SIZE);
		lastPos = lastPos + SINGLE_SIZE;
	}

	void putDouble(double value) {
		SysTray.CopyMemory(data[lastPos + 1], value, DOUBLE_SIZE);
		lastPos = lastPos + DOUBLE_SIZE;
	}

	void putString(String /* FIXME BYREF!! */ str) {
		putString(str, true);
	}

	void putString(String /* FIXME BYREF!! */ str, boolean withLength) {
		int length = 0;

		length = vb6.Len(str);

		if (withLength) {
			putInteger(length);
			putString(str, false);
		} else {
			if (length > 0) {
				SysTray.CopyMemory(data[lastPos + 1], BYVALStrPtr[vb6.StrConv(str, vbFromUnicode)], length);

				lastPos = lastPos + length;
			}
		}
	}

	void getVoid(int length) {
		CurrentPos = CurrentPos + length;
	}

	void putVoid(int length) {
		lastPos = lastPos + length;
	}

	void clearData() {
		data = new Byte[0];
		data = (data == null) ? new Byte[DEFAULT_MAX_SIZE_FILE] : java.util.Arrays.copyOf(data, DEFAULT_MAX_SIZE_FILE);

		CurrentPos = 0;
		lastPos = -1;
		hFile = -1;
	}

	int getLastPos() {
		int retval = 0;
		retval = lastPos;
		return retval;
	}

	int getCurrentPos() {
		int retval = 0;
		retval = CurrentPos;
		return retval;
	}

	boolean Eof() {
		boolean retval = false;
		retval = (CurrentPos > vb6.UBound(data));
		return retval;
	}

	void saveBuffer() {
		int[] buf;

		if (hFile > 0) {
			buf = new Byte[0];
			buf = (buf == null) ? new Byte[lastPos] : java.util.Arrays.copyOf(buf, lastPos);

			SysTray.CopyMemory(buf[0], data[0], lastPos + 1);
			/* FIXME: PUT hFile , , buf */
		}
	}

	void Class_Initialize() {
		hFile = INVALID_FILE_HANDLER;
	}

	void Class_Terminate() {
		/* FIXME: ERASE data ( ) */
	}

}