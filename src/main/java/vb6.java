import java.io.File;
import java.io.IOException;
import java.lang.Math;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

public class vb6 {

	/*
	 * Abs - Returns the absolute value of a specified number.
	 * https://msdn.microsoft.com/en-us/library/system.math.abs.aspx
	 */

	public static double Abs(double d) {
		return Math.abs(d);
	}

	public static float Abs(float f) {
		return Math.abs(f);
	}

	public static int Abs(int i) {
		return Math.abs(i);
	}

	public static long Abs(long lng) {
		return Math.abs(lng);
	}

	/*
	 * Asc - Devuelve un valor Integer que representa el código de carácter
	 * correspondiente a un carácter.
	 * https://msdn.microsoft.com/library/zew1e4wc(v=vs.90).aspx
	 */

	public static int Asc(char chr) {
		return Asc(String.valueOf(chr));
	}

	public static int Asc(String str) {
		// Cp1252 es el codepage 1252 de Windows
		byte[] bytes = str.getBytes(Charset.forName("Cp1252"));
		return bytes[0] & 255;
	}

	/*
	 * CBool - Returns an expression that has been converted to a Variant of
	 * subtype Boolean.
	 * https://msdn.microsoft.com/library/2k9sfx3c(v=vs.84).aspx
	 */

	public static boolean CBool(char chr) {
		return CBool(String.valueOf(chr));
	}

	public static boolean CBool(String str) {
		return Boolean.valueOf(str);
	}

	public static boolean CBool(int i) {
		return i != 0;
	}

	public static boolean CBool(long lng) {
		return lng != 0;
	}

	/*
	 * CByte - Returns an expression that has been converted to a Variant of
	 * subtype Byte.
	 * https://msdn.microsoft.com/en-us/library/2ssb79wt(v=vs.84).aspx
	 */

	public static byte CByte(char chr) {
		return CByte(String.valueOf(chr));
	}

	public static byte CByte(String str) {
		return Byte.valueOf(str);
	}

	public static byte CByte(double d) {
		return (byte) d;
	}

	public static byte CByte(float f) {
		return (byte) f;
	}

	public static byte CByte(int i) {
		return (byte) i;
	}

	public static byte CByte(long lng) {
		return (byte) lng;
	}

	/*
	 * CDbl - Returns an expression that has been converted to a Variant of
	 * subtype Double.
	 * https://msdn.microsoft.com/en-us/library/ftekwwt0(v=vs.84).aspx
	 */

	public static double CDbl(char chr) {
		return CDbl(String.valueOf(chr));
	}

	public static double CDbl(String str) {
		return Double.valueOf(str);
	}

	public static double CDbl(float f) {
		return (double) f;
	}

	public static double CDbl(int i) {
		return (double) i;
	}

	public static double CDbl(long lng) {
		return (double) lng;
	}

	/*
	 * Chr - Returns the character associated with the specified character code.
	 * https://msdn.microsoft.com/en-us/library/613dxh46(v=vs.90).aspx
	 */

	public static char Chr(int charCode) {
		return (char) charCode;
	}

	/*
	 * CInt - Returns an expression that has been converted to a Variant of
	 * subtype Integer.
	 * https://msdn.microsoft.com/en-us/library/fctcwhw9(v=vs.84).aspx
	 */

	public static int CInt(char chr) {
		return CInt(String.valueOf(chr));
	}

	public static int CInt(String str) {
		return Integer.valueOf(str);
	}

	public static int CInt(double d) {
		return (int) d;
	}

	public static int CInt(float f) {
		return (int) f;
	}

	public static int CInt(long lng) {
		return (int) lng;
	}

	/*
	 * CLng - Returns an expression that has been converted to a Variant of
	 * subtype Long.
	 * https://msdn.microsoft.com/en-us/library/ck4c5842(v=vs.84).aspx
	 */

	public static long CLng(char chr) {
		return CLng(String.valueOf(chr));
	}

	public static long CLng(String str) {
		return Long.valueOf(str);
	}

	public static long CLng(double d) {
		return (long) d;
	}

	public static long CLng(float f) {
		return (long) f;
	}

	public static long CLng(int i) {
		return (long) i;
	}

	/*
	 * CStr - Returns an expression that has been converted to a Variant of
	 * subtype String.
	 * https://msdn.microsoft.com/en-us/library/0zk841e9(v=vs.84).aspx
	 */

	public static String CStr(char chr) {
		return String.valueOf(chr);
	}

	public static String CStr(double d) {
		return String.valueOf(d);
	}

	public static String CStr(float f) {
		return String.valueOf(f);
	}

	public static String CStr(int i) {
		return String.valueOf(i);
	}

	public static String CStr(long lng) {
		return String.valueOf(lng);
	}

	/*
	 * DateAdd - Returns a Date value containing a date and time value to which
	 * a specified time interval has been added.
	 * https://msdn.microsoft.com/en-us/library/hcxe65wz(v=vs.90).aspx
	 * http://www.csidata.com/custserv/onlinehelp/vbsdocs/vbs92.htm
	 */

	public static java.util.Date DateAdd(String interval, Double number, java.util.Date date) {

		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		Calendar c = Calendar.getInstance();
		c.setTime(date);

		// Parseo el intervalo a formato vb6 y hago la suma
		c.add(Vb6DateInterval.fromValue(interval).getCalendarValue(), number.intValue()); // Adding
																							// 5
																							// days

		return c.getTime();

	}

	/*
	 * Day - Returns an Integer value from 1 through 31 representing the day of
	 * the month.
	 * https://msdn.microsoft.com/en-us/library/9b4h8sxy(v=vs.90).aspx
	 */

	public static int Day() {
		return Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
	}

	/*
	 * FileCopy - Copies a file.
	 * https://msdn.microsoft.com/en-us/library/2s1c774y(v=vs.90).aspx
	 */

	public static void FileCopy(String source, String destination) throws IOException {
		File sourceFile = new File(source);
		File destFile = new File(destination);

		FileCopy(sourceFile, destFile);

	}

	public static void FileCopy(File from, File to) throws IOException {
		Files.copy(from.toPath(), to.toPath());
	}

	/*
	 * Fix - function returns the first negative integer greater than or equal
	 * to the number.
	 * https://msdn.microsoft.com/en-us/library/xh29swte(v=vs.90).aspx
	 */

	public static double Fix(double d) {
		return Math.ceil(d);
	}

	public static int Fix(int i) {
		return (int) Math.ceil(i);
	}

	public static long Fix(long lng) {
		return (long) Math.ceil(lng);
	}

	/*
	 * Int - returns the first negative integer less than or equal to Number.
	 * https://msdn.microsoft.com/en-us/library/xh29swte(v=vs.90).aspx
	 */

	public static double Int(double d) {
		return Math.floor(d);
	}

	public static int Int(int i) {
		return (int) Math.floor(i);
	}

	public static long Int(long lng) {
		return (long) Math.floor(lng);
	}

	/*
	 * Now - A vb6 date function that returns the current date and time together
	 * http://www.vb6.us/tutorials/date-time-functions-visual-basic
	 */
	public static java.util.Date Now() {
		return new java.util.Date();
	}

	/*
	 * Format - Returns a Variant (String) containing an expression formatted
	 * according to instructions contained in a format expression.
	 * https://msdn.microsoft.com/en-us/library/aa262745(v=vs.60).aspx
	 */

	/*
	 * FIXME? Los potenciales casos de uso de la funcion original de VB6 de
	 * Format() son muchisimos. Por ende, aca solo se implementan aquellos que
	 * se usan en el resto de la aplicacion:
	 *
	 * /src/frmUserList.java: 41: List1.AddItem(vb6.Format(LoopC, "000") + " "
	 *
	 * /src/General.java: 366: Declaraciones.LastBackup = vb6.Format(vb6.Now(),
	 * "Short Time"); 367: Declaraciones.Minutos = vb6.Format(vb6.Now(),
	 * "Short Time");
	 *
	 * /src/modUserRecords.java: 130:
	 * Declaraciones.Records[Declaraciones.NumRecords].Fecha =
	 * vb6.Format(vb6.Now(), "DD/MM/YYYY hh:mm:ss");
	 */
	public static String Format(Object obj, String format) {

		String resultado = "";

		if (format.compareTo("000") == 0) {
			// 0 = Digit placeholder; prints a trailing or a leading zero in
			// this position, if appropriate.
			resultado = String.format("%03d", (int) obj);
			return resultado;
		}

		java.util.Date date = (java.util.Date) obj;
		SimpleDateFormat sdf;

		if (format.compareTo("DD/MM/YYYY hh:mm:ss") == 0) {

			sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss.SS");
			resultado = sdf.format(date);

		} else if (format.compareTo("Short Time") == 0) {

			sdf = new SimpleDateFormat("HH:mm");
			resultado = sdf.format(date);

		}

		return resultado;
	}

	/*
	 * Hour - Devuelve un valor Integer entre 0 y 23 que representa la hora del
	 * día. https://msdn.microsoft.com/es-es/library/se56s6ky%28v=vs.90%29.aspx
	 */

	public static int Hour() {
		return Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
	}

	/*
	 * Weekday - Devuelve un valor Integer que contiene un número que representa
	 * el día de la semana.
	 * https://msdn.microsoft.com/es-es/library/82yfs2zh%28v=vs.90%29.aspx
	 */

	public static int Weekday() {
		return Calendar.getInstance().get(Calendar.DAY_OF_WEEK);
	}

	/*
	 * Second - Returns an Integer value from 0 through 59 that represents the
	 * second of the minute.
	 * https://msdn.microsoft.com/es-es/library/82yfs2zh%28v=vs.90%29.aspx
	 */

	public static int Second() {
		return Calendar.getInstance().get(Calendar.SECOND);
	}

	/*
	 * Minute - Devuelve un valor Integer entre 0 y 59 que representa el minuto
	 * de la hora.
	 * https://msdn.microsoft.com/es-es/library/7eayd8ts%28v=vs.90%29.aspx
	 */

	public static int Minute() {
		return Calendar.getInstance().get(Calendar.MINUTE);
	}

	/*
	 * IsNull - Returns a Boolean value that indicates whether an expression
	 * contains no valid data
	 * https://msdn.microsoft.com/en-us/library/office/gg278616.aspx
	 */

	public static boolean IsNull(Object obj) {
		return obj == null;
	}

	/*
	 * IsNumeric - Devuelve un valor Boolean que indica si una expresión puede
	 * evaluarse como un número.
	 * 
	 * IsNumeric devuelve True si el tipo de datos de Expression es Boolean,
	 * Byte, Decimal, Double, Integer, Long, SByte, Short, Single, UInteger,
	 * ULong o UShort o un Object que contiene uno de esos tipos numéricos.
	 * También devuelve True si Expression es Char o String, que puede convertir
	 * correctamente a un número.
	 * 
	 * IsNumeric devuelve False si Expression es un dato de tipo Date o un dato
	 * de tipo Object y no contiene ningún tipo numérico. IsNumeric devuelve
	 * False si Expression es Char o String que no se puede convertir en un
	 * número.
	 *
	 * https://msdn.microsoft.com/es-es/library/6cd3f6w1%28v=vs.90%29.aspx
	 */

	public static boolean IsNumeric(Object obj) {
		return obj instanceof Integer || obj instanceof Double || obj instanceof Long || obj instanceof Float
				|| obj instanceof Short || obj instanceof Short || (obj instanceof Character && IsNumeric((char) obj))
				|| (obj instanceof String && IsNumeric((String) obj));
	}

	public static boolean IsNumeric(char c) {
		return IsNumeric(Character.toString(c));
	}

	public static boolean IsNumeric(String s) {
		return s.matches("[-+]?\\d*\\.?\\d+"); // match a number, with optional
												// '-' and/or decimal.
	}

	/*
	 * IsDate - Devuelve un valor Boolean que indica si una expresión representa
	 * un valor Date válido.
	 * https://msdn.microsoft.com/es-es/library/00wf8zk9%28v=vs.90%29.aspx
	 */

	public static boolean IsDate(Object obj) {
		return obj instanceof java.util.Date;
	}

	/*
	 * InStr - Returns an integer specifying the start position of the first
	 * occurrence of one string within another.
	 * https://msdn.microsoft.com/en-us/library/8460tsh1(v=vs.90).aspx
	 */

	public static int InStr(String str1, String str2) {
		return InStr(1, str1, str2);
	}

	public static int InStr(String str1, String str2, int compare) {
		return InStr(1, str1, str2, compare);
	}

	public static int InStr(int start, String str1, String str2) {
		return InStr(start, str1, str2, Vb6CompareType.BINARY.getVb6InStrCompareType()); // Si
																							// no
																							// se
																							// especifica,
																							// se
																							// asume
																							// binario
	}

	/**
	 * @param start
	 *            Optional. Numeric expression that sets the starting position
	 *            for each search. If omitted, search begins at the first
	 *            character position. The start index is 1-based.
	 * @param str1
	 *            Required. String expression being searched.
	 * @param str2
	 *            Required. String expression sought.
	 * @param compare
	 *            Optional. Specifies the type of string comparison. If Compare
	 *            is omitted, the Option Compare setting determines the type of
	 *            comparison.
	 *
	 * @return <b>0</b> - If {@code str1} is zero length or Nothing <br>
	 *         <b>0</b> - If {@code start} > {@code str2} <br>
	 *         <b>0</b> - If {@code str2} is not found <br>
	 *         <b>{@code start}</b> - {@code str2} is zero length or Nothing
	 *         <br>
	 *         <b><i>Position where match begins</i></b> - {@code str2} is found
	 *         within {@code str1}
	 *
	 */
	public static int InStr(int start, String str1, String str2, int compare) {

		if (str1 == null || str1.length() == 0 || (str2 != null && start > str2.length()))
			return 0; // Primeros dos casos de retorno
		if (str2 == null || str2.length() == 0)
			return start; // Cuarto caso de retorno

		if (start >= 1)
			str1 = str1.substring(start - 1); // Porque el 'start' empieza en 1
												// //(Dios, que mal diseñado
												// esta VB6, peor que la API
												// Date de Java)

		Vb6CompareType comparisonType = Vb6CompareType.fromValue(compare);

		/*
		 * La diferencia entre la comparacion TEXT y BINARY no es literal como
		 * uno podria imaginarse. BINARY hace referencia a una busqueda
		 * case-sensitive, mientras que TEXT es una busqueda case-insensitive
		 */

		int resultSearch = 0;

		if (comparisonType == Vb6CompareType.BINARY) { // case-sensitive
			resultSearch = str1.indexOf(str2);
		} else if (comparisonType == Vb6CompareType.TEXT) { // case-insensitive
			resultSearch = str1.toLowerCase().indexOf(str2.toLowerCase());
		}

		if (resultSearch == -1)
			return 0; // Si es -1 no hubo ocurrencia; VB6 devuelve 0
		else
			return resultSearch;

	}

	/*
	 * IIf - Returns one of two objects, depending on the evaluation of an
	 * expression.
	 * https://msdn.microsoft.com/en-us/library/27ydhh0d(v=vs.90).aspx
	 */

	/**
	 * @param expression
	 *            Required. Boolean. The expression you want to evaluate.
	 * @param truePart
	 *            Required. Object. Returned if Expression evaluates to True.
	 * @param falsePart
	 *            Required. Object. Returned if Expression evaluates to False.
	 */
	public static <T> T IIf(boolean expression, T truePart, T falsePart) {
		if (expression)
			return truePart;
		else
			return falsePart;
	}

	/*
	 * LBound - Returns the lowest available subscript for the indicated
	 * dimension of an array.
	 * https://msdn.microsoft.com/en-us/library/t9a7w1ac(v=vs.90).aspx
	 */

	// Se implementa la version mas sencilla de LBound (que es, ademas, la que
	// se usa en casi todos lados): verificacion de un array unidimensional sin
	// parametro opcional
	public static int LBound(Object[] array) throws ArgumentNullException {
		if (array != null)
			return 0;
		else
			throw new ArgumentNullException(
					"Array is Nothing. - Error code 9 (see: https://msdn.microsoft.com/en-us/library/t9a7w1ac(v=vs.90).aspx)");
	}

	/*
	 * UBound - Returns the highest available subscript for the indicated
	 * dimension of an array.
	 * https://msdn.microsoft.com/en-us/library/95b8f22f(v=vs.90).aspx
	 */

	/**
	 * @return Integer. The highest value the subscript for the specified
	 *         dimension can contain. If Array has only one element, UBound
	 *         returns 0. If Array has no elements, for example if it is a
	 *         zero-length string, UBound returns -1.
	 */
	public static int UBound(Object[] array) throws ArgumentNullException {
		if (array != null) {
			return array.length - 1;
		} else
			throw new ArgumentNullException(
					"Array is Nothing. - Error code 9 (see: https://msdn.microsoft.com/en-us/library/95b8f22f(v=vs.90).aspx)");
	}

	/*
	 * LCase - Returns a string or character converted to lowercase.
	 * https://msdn.microsoft.com/en-us/library/7789633z(v=vs.90).aspx
	 */

	public static char LCase(char c) {
		return Character.toLowerCase(c);
	}

	public static String LCase(String s) {
		return s.toLowerCase();
	}

	/*
	 * UCase - Returns a string or character containing the specified string
	 * converted to uppercase.
	 * https://msdn.microsoft.com/en-us/library/53e2ew8a(v=vs.90).aspx
	 */

	public static char UCase(char c) {
		return Character.toUpperCase(c);
	}

	public static String UCase(String s) {
		return s.toUpperCase();
	}

	/*
	 * time - Returns the current time
	 * http://www.vb6.us/tutorials/date-time-functions-visual-basic
	 */

	public static java.util.Date time() {
		return new java.util.Date();
	}

	/*
	 * Left - https://msdn.microsoft.com/en-us/library/y050k1wb(v=vs.90).aspx
	 * https://msdn.microsoft.com/en-us/library/y050k1wb(v=vs.90).aspx
	 */

	/**
	 * @param str
	 *            Required. String expression from which the leftmost characters
	 *            are returned.
	 * @param length
	 *            Required. Integer expression. Numeric expression indicating
	 *            how many characters to return. If zero, a zero-length string
	 *            ("") is returned. If greater than or equal to the number of
	 *            characters in str, the complete string is returned.
	 */
	public static String Left(String str, int length) {
		if (length > str.length())
			return str; // Sin esta verificacion, se obtiene un
						// StringIndexOutOfBoundsException
		return str.substring(0, length);
	}

	/*
	 * Len - Returns an integer containing either the number of characters in a
	 * string or the nominal number of bytes required to store a variable.
	 * https://msdn.microsoft.com/en-us/library/dxsw58z6(v=vs.90).aspx
	 */

	/**
	 * @param expression
	 *            Any valid String expression or variable name. If Expression is
	 *            of type Object, the Len function returns the size as it will
	 *            be written to the file by the FilePut function.
	 *
	 * @return -1 in case of error
	 */
	public static int Len(String expression) {
		return expression.length();
	}

	/* LenB - Len de los bytes codificados en cp1252. */

	public static int LenB(String str) {
		byte[] bytes = str.getBytes(Charset.forName("Cp1252"));
		return bytes.length;
	}

	/*
	 * La función InStrB de versiones anteriores de Visual Basic devuelve un
	 * número de bytes, en lugar de la posición de un carácter.
	 * 
	 * The InStrB function is used with byte data contained in a string. Instead
	 * of returning the character position of the first occurrence of one string
	 * within another, InStrB returns the byte position.
	 *
	 * https://msdn.microsoft.com/es-es/library/8460tsh1%28v=vs.90%29.aspx
	 * http://www.experts-exchange.com/questions/20625413/InstrB-and-Instr.html
	 */

	// Segun Google, InStrB es practicamente igual a hacer un InStr en Binary
	// (osea, una comparacion case-sensitive)

	public static int InStrB(String str1, String str2) {
		return InStrB(1, str1, str2);
	}

	public static int InStrB(int start, String str1, String str2) {
		return InStr(start, str1, str2, Vb6CompareType.BINARY.getVb6InStrCompareType());
	}

	/*
	 * Right - Devuelve una cadena que contiene un número especificado de
	 * caracteres desde el lado derecho de una cadena.
	 * https://msdn.microsoft.com/es-es/library/dxs6hz0a%28v=vs.90%29.aspx
	 */

	public static String Right(String str, int length) {
		if (length > str.length())
			return str; // Sin esta verificacion, se obtiene un
						// StringIndexOutOfBoundsException
		return str.substring(str.length() - length);
	}

	/*
	 * Rnd - Devuelve un número aleatorio de tipo Single.
	 *
	 * [Single data type]: Holds signed IEEE 32-bit (4-byte) single-precision
	 * floating-point numbers ranging in value from -3.4028235E+38 through
	 * -1.401298E-45 for negative values and from 1.401298E-45 through
	 * 3.4028235E+38 for positive values. Single-precision numbers store an
	 * approximation of a real number.
	 *
	 *
	 * La función Rnd devuelve un valor menor que 1, pero mayor o igual a cero.
	 * El valor de Number determina la forma en que Rnd genera un número
	 * aleatorio.
	 *
	 * Para cualquier valor de inicialización dado, se genera la misma secuencia
	 * de números ya que cada llamada sucesiva que se hace a la función Rnd
	 * utiliza el número anteriormente generado como inicialización para el
	 * siguiente número de la secuencia.
	 *
	 * Antes de llamar a la función Rnd, utilice la instrucción Randomize sin
	 * argumento para inicializar el generador de números aleatorios con un
	 * valor de inicialización basado en el temporizador del sistema.
	 *
	 * Ejemplo de uso VB6: randomValue = CInt(Math.Floor((upperbound -
	 * lowerbound + 1) * Rnd())) + lowerbound
	 *
	 * https://msdn.microsoft.com/es-es/library/f7s023d2%28v=vs.90%29.aspx
	 * https://msdn.microsoft.com/en-us/library/xay7978z.aspx
	 */

	/**
	 * @return Depende de {@code number}; si es:
	 *         <ul>
	 *         <li>Menor que cero - Siempre el mismo número, utilizando
	 *         {@code number} como valor de inicialización.</li>
	 *         <li>Mayor que cero - El siguiente número aleatorio en la
	 *         secuencia.</li>
	 *         <li>Igual a cero - El número generado por última vez.</li>
	 *         <li>Omitido - El siguiente número aleatorio en la secuencia.</li>
	 *         </ul>
	 */

	private static Random rnd;
	private static long lastRandomNumber = 0;

	public static long Rnd(int number) {

		if (number < 0) {
			rnd = new Random(number);
			lastRandomNumber = rnd.nextLong();
		} else if (number > 0) {
			return Rnd();
		}

		return lastRandomNumber;
	}

	public static long Rnd() {

		if (rnd == null)
			rnd = new Random();

		lastRandomNumber = rnd.nextLong();
		return lastRandomNumber;
	}

	/*
	 * Round - Returns a number rounded to a specified number of decimal places.
	 * https://msdn.microsoft.com/en-us/library/aa242034(v=vs.60).aspx
	 */

	/**
	 * @param expression
	 *            Required. Numeric expression being rounded.
	 * @param numdecimalplaces
	 *            Optional. Number indicating how many places to the right of
	 *            the decimal are included in the rounding. If omitted, integers
	 *            are returned by the Round function.
	 */
	public static double Round(double expression, int numdecimalplaces) {
		if (numdecimalplaces < 0)
			throw new IllegalArgumentException();

		BigDecimal bd = new BigDecimal(expression);
		bd = bd.setScale(numdecimalplaces, RoundingMode.HALF_UP);
		return bd.doubleValue();
	}

	public static long Round(double expression) {
		return Math.round(expression);
	}

	public static float Round(float expression, int numdecimalplaces) {
		return (float) Round((double) expression, numdecimalplaces);
	}

	public static int Round(float expression) {
		return Math.round(expression);
	}

	/*
	 * Sqr - Returns a Double specifying the square root of a number.
	 * https://msdn.microsoft.com/en-us/library/aa263367(v=vs.60).aspx
	 */

	public static double Sqr(double number) {
		return Math.sqrt(number);
	}

	/*
	 * Mid - Returns a string containing a specified number of characters from a
	 * string. https://msdn.microsoft.com/en-us/library/05e63829(v=vs.90).aspx
	 */

	/**
	 * @param str
	 *            Required. String expression from which characters are
	 *            returned.
	 * @param start
	 *            Required. Integer expression. Starting position of the
	 *            characters to return. If Start is greater than the number of
	 *            characters in str, the Mid function returns a zero-length
	 *            string (""). Start is one based.
	 * @param length
	 *            Optional. Integer expression. Number of characters to return.
	 *            If omitted or if there are fewer than Length characters in the
	 *            text (including the character at position Start), all
	 *            characters from the start position to the end of the string
	 *            are returned.
	 */
	public static String mid(String str, int start, int length) {
		return Right(str.substring(start - 1), length);
	}

	/*
	 * Replace - Returns a string in which a specified substring has been
	 * replaced with another substring a specified number of times.
	 * http://www.vb6.us/tutorials/vb6-string-functions
	 */

	public static String Replace(String expression, String find, String replaceWith) {
		// Todos los usos de vb6.Replace() involucran buscar y reemplazar un
		// String en otro String objetivo.
		return expression.replace(find, replaceWith);
	}

	/*
	 * RTrim - Removes trailing blank spaces from a string.
	 * http://www.vb6.us/tutorials/vb6-string-functions
	 */

	public static String RTrim(String str) {
		int i = str.length() - 1;
		while (i >= 0 && Character.isWhitespace(str.charAt(i))) {
			i--;
		}
		return str.substring(0, i + 1);
	}

	/*
	 * Trim - Removes both leading and trailing blank spaces from a string.
	 * http://www.vb6.us/tutorials/vb6-string-functions
	 */

	public static String Trim(String str) {
		return str.trim();
	}

	/*
	 * LTrim - Removes both leading and trailing blank spaces from a string.
	 *
	 * Este metodo tecnicamente no era parte del modulo VB6 original, pero es
	 * parte de la funcionalidad de Trim la cual combina ambas funcionalidades
	 * de LTrim() y RTrim()
	 *
	 * http://www.vb6.us/tutorials/vb6-string-functions
	 */

	public static String LTrim(String str) {
		int i = 0;
		while (i < str.length() && Character.isWhitespace(str.charAt(i))) {
			i++;
		}
		return str.substring(i);
	}

	/*
	 * Sgn - Returns an integer indicating the sign of a number.
	 * http://www.chennaiiq.com/developers/reference/visual_basic/functions/sgn.
	 * asp
	 */

	/**
	 * <table width="90%" border="0" align="center" cellpadding="1" cellspacing=
	 * "1" class="TBox">
	 * <tbody>
	 * <tr class="tabH1">
	 * <td>Statement</td>
	 * <td>Result</td>
	 * <td>Remark</td>
	 * </tr>
	 * <tr class="ver12">
	 * <td>Sgn(1)</td>
	 * <td>1</td>
	 * <td>&nbsp;</td>
	 * </tr>
	 * <tr class="ver12">
	 * <td height="10" colspan="3"></td>
	 * </tr>
	 * <tr class="ver12">
	 * <td>Sgn(533)</td>
	 * <td>1</td>
	 * <td>&nbsp;</td>
	 * </tr>
	 * <tr class="ver12">
	 * <td height="10" colspan="3"></td>
	 * </tr>
	 * <tr class="ver12">
	 * <td>Sgn(33.57)</td>
	 * <td>1</td>
	 * <td>&nbsp;</td>
	 * </tr>
	 * <tr class="ver12">
	 * <td height="10" colspan="3"></td>
	 * </tr>
	 * <tr class="ver12">
	 * <td>Sgn(-1)</td>
	 * <td>-1</td>
	 * <td>&nbsp;</td>
	 * </tr>
	 * <tr class="ver12">
	 * <td height="10" colspan="3"></td>
	 * </tr>
	 * <tr class="ver12">
	 * <td>Sgn(-533)</td>
	 * <td>-1</td>
	 * <td>&nbsp;</td>
	 * </tr>
	 * <tr class="ver12">
	 * <td height="10" colspan="3"></td>
	 * </tr>
	 * <tr class="ver12">
	 * <td>Sgn(-33.57)</td>
	 * <td>-1</td>
	 * <td>&nbsp;</td>
	 * </tr>
	 * <tr class="ver12">
	 * <td height="10" colspan="3"></td>
	 * </tr>
	 * <tr class="ver12">
	 * <td>Sgn(0)</td>
	 * <td>0</td>
	 * <td>&nbsp;</td>
	 * </tr>
	 * <tr class="ver12">
	 * <td height="10" colspan="3"></td>
	 * </tr>
	 * <tr class="ver12">
	 * <td>Sgn(unknown)</td>
	 * <td>0</td>
	 * <td>uninitialized Variable</td>
	 * </tr>
	 * <tr class="ver12">
	 * <td height="10" colspan="3"></td>
	 * </tr>
	 * <tr class="ver12">
	 * <td>Sgn(Null)</td>
	 * <td>Error</td>
	 * <td>Invalid use of Null</td>
	 * </tr>
	 * <tr class="ver12">
	 * <td height="10" colspan="3"></td>
	 * </tr>
	 * <tr class="ver12">
	 * <td>Sgn("abcd")</td>
	 * <td>Error</td>
	 * <td>Type mismatch</td>
	 * </tr>
	 * <tr class="ver12">
	 * <td height="10" colspan="3"></td>
	 * </tr>
	 * <tr class="ver12">
	 * <td>Sgn(True)</td>
	 * <td>-1</td>
	 * <td>True - Non Zero</td>
	 * </tr>
	 * <tr class="ver12">
	 * <td height="10" colspan="3"></td>
	 * </tr>
	 * <tr class="ver12">
	 * <td>Sgn(False)</td>
	 * <td>0</td>
	 * <td>False - Zero</td>
	 * </tr>
	 * </tbody>
	 * </table>
	 */
	public static int Sgn(double number) {
		if (number > 0)
			return 1;
		else if (number < 0)
			return -1;

		return 0;
	}

	public static int Sgn(boolean bool) {
		if (bool)
			return -1; // Esto la verdad que es horrible, que un true devuelva
						// -1, pero asi estaba en el ejemplo :(
		else
			return 0;
	}

	/*
	 * Space - Returns a string consisting of the specified number of spaces.
	 * https://msdn.microsoft.com/en-us/library/k6ethaxs(v=vs.90).aspx
	 */

	public static String Space(int numberOfSpaces) {
		String resultado = "";
		for (int e = numberOfSpaces; e > 0; e--)
			resultado = resultado + " ";
		return resultado;
	}

	/*
	 * str - Returns a String representation of a number.
	 * https://msdn.microsoft.com/en-us/library/4y6a1sx7(v=vs.90).aspx
	 */

	public static String str(int i) {
		return String.valueOf(i);
	}

	public static String str(double d) {
		return String.valueOf(d);
	}

	public static String str(long l) {
		return String.valueOf(l);
	}

	public static String str(float f) {
		return String.valueOf(f);
	}

	/*
	 * StrConv - Returns a Variant (String) converted as specified.
	 *
	 * https://msdn.microsoft.com/en-us/library/aa263373(v=vs.60).aspx
	 * http://stackoverflow.com/questions/655891/converting-utf-8-to-iso-8859-1-
	 * in-java-how-to-keep-it-as-single-byte
	 */

	public static String StrConv(String string, int conversion) {
		return StrConv(string, Vb6EncodingType.fromValue(conversion));
	}

	public static String StrConv(String string, Vb6EncodingType conversion) {
		// Siempre se usa de la siguiente forma: vb6.StrConv(str, vbFromUnicode)
		// Por lo tanto, solo se implementara este escenario

		Charset originCharset = StandardCharsets.UTF_16;
		Charset systemCharset = Charset.defaultCharset();

		ByteBuffer inputBuffer = ByteBuffer.wrap(string.getBytes());

		// decode UTF-8
		CharBuffer data = originCharset.decode(inputBuffer);

		// encode ISO-8559-1
		ByteBuffer outputBuffer = systemCharset.encode(data);
		byte[] outputData = outputBuffer.array();

		return new String(outputData);
	}

	/* ************************* */
	/* TODO - METODOS A ELIMINAR */
	/* ************************* */

	public static class App {
		// TODO Esto tiene que volar del codigo
	}

	public static class Date {
		// TODO Esto tiene que volar del codigo
	}

	public static class Collection {
		// TODO Esto tiene que volar del codigo
	}

	public static void dir() {
		// TODO - Esta funcion se usa solo en dos lados, no vale la pena
		// implementarla. Directamente toquemos los metodos que la llaman
		/*
		 * /src/main/java/clsClan.java: 344: if
		 * (vb6.LenB(vb6.dir(Declaraciones.CharPath + Nombre + ".chr")) != 0) {
		 * 
		 * /src/main/java/General.java: 630: retval = vb6.LenB(vb6.dir(File,
		 * FileType)) != 0;
		 */
	}

	public static void FreeFile() {
		// FIXME Esta es una funcion relacionada con el manejo de archivos en
		// VB6. Habria que volarla y cambiar todos los metodos que hacen uso de
		// ella.
		/*
		 * /src/main/java/Admin.java: 197: hFile = vb6.FreeFile(); 458: ArchN =
		 * vb6.FreeFile(); 484: ArchN = vb6.FreeFile();
		 * 
		 * /src/main/java/clsIniManager.java: 156: handle = vb6.FreeFile(); 645:
		 * hFile = vb6.FreeFile();
		 * 
		 * /src/main/java/ConsultasPopulares.java: 195: ArchN = vb6.FreeFile();
		 * 215: ArchN = vb6.FreeFile();
		 * 
		 * /src/main/java/ES.java: 313: N = vb6.FreeFile(1); 339: N =
		 * vb6.FreeFile(1); 564: nfile = vb6.FreeFile(); 610: FreeFileMap =
		 * vb6.FreeFile(); 616: FreeFileInf = vb6.FreeFile(); 1692: hFile =
		 * vb6.FreeFile(); 2686: mifile = vb6.FreeFile(); 2712: mifile =
		 * vb6.FreeFile(); 2737: mifile = vb6.FreeFile();
		 * 
		 * /src/main/java/frmMain.java: 254: N = vb6.FreeFile(); 392: N =
		 * vb6.FreeFile();
		 * 
		 * /src/main/java/frmServidor.java: 272: N = vb6.FreeFile();
		 * 
		 * /src/main/java/General.java: 608: N = vb6.FreeFile(); 696: nfile =
		 * vb6.FreeFile(); 721: nfile = vb6.FreeFile(); 746: nfile =
		 * vb6.FreeFile(); 771: nfile = vb6.FreeFile(); 796: nfile =
		 * vb6.FreeFile(); 821: nfile = vb6.FreeFile(); 846: nfile =
		 * vb6.FreeFile(1); 869: nfile = vb6.FreeFile(); 888: nfile =
		 * vb6.FreeFile(); 907: nfile = vb6.FreeFile(); 928: nfile =
		 * vb6.FreeFile(); 954: nfile = vb6.FreeFile(); 980: nfile =
		 * vb6.FreeFile(); 1014: nfile = vb6.FreeFile(); 1047: nfile =
		 * vb6.FreeFile(); 1072: nfile = vb6.FreeFile(); 1105: nfile =
		 * vb6.FreeFile(); 1229: N = vb6.FreeFile();
		 * 
		 * /src/main/java/modCentinela.java: 643: nfile = vb6.FreeFile();
		 * 
		 * /src/main/java/modForum.java: 84: FileIndex = vb6.FreeFile(); 103:
		 * FileIndex = vb6.FreeFile(); 219: FileIndex = vb6.FreeFile(); 234:
		 * FileIndex = vb6.FreeFile();
		 * 
		 * /src/main/java/modUserRecords.java: 188: intFile = vb6.FreeFile();
		 * 
		 * /src/main/java/Protocol.java: 7886: N = vb6.FreeFile(); 16574: handle
		 * = vb6.FreeFile();
		 * 
		 * /src/main/java/Statistics.java: 105: handle = vb6.FreeFile(); 246:
		 * handle = vb6.FreeFile(); 617: handle = vb6.FreeFile();
		 * 
		 * /src/main/java/TCP.java: 1347: N = vb6.FreeFile(); 1352: N =
		 * vb6.FreeFile(); 1850: N = vb6.FreeFile(1);
		 * 
		 * /src/main/java/wskapiAO.java: 388: nfile = vb6.FreeFile();
		 */
	}

	public static void LOF() {
		// TODO volar del codigo; es una funcion de file
	}

	public static void val() {
		// FIXME: la funcion val es una cosa fea de VB6, hay que volarla.
		// El problema es que se usa en demasiados lugares del codigo
	}

	public static void MsgBox() {
		// TODO funcion de capa de presentacion; volar del codigo
	}

	public static void RGB() {
		// TODO funcion de capa de presentacion; volar del codigo
	}

	public static void Shell() {
		// TODO Reemplazar en el resto del codigo esta porqueria de VB6
	}

	public static void Split() {
		// TODO Esta funcion se usa una sola vez en una sola clase. A volarla...
		/*
		 * /src/main/java/Protocol.java: 4179: codex =
		 * vb6.Split(buffer.ReadASCIIString(), Protocol.SEPARATOR); 4895: codex
		 * = vb6.Split(buffer.ReadASCIIString(), Protocol.SEPARATOR); 16974:
		 * auxiliaryString = vb6.Split(newMOTD, vbCrLf);
		 */
	}

	/* ************************* */
	/* CLASES & ENUMS AUXILIARES */
	/* ************************* */

	/**
	 * <pre>
	 * <table responsive="true"><tbody><tr><th><p>Enumeration value </p></th><th><p>String </p></th><th><p>Unit of time interval to add </p></th></tr><tr><td data-th="Enumeration value "><p><span><span class="input">DateInterval.Day</span></span></p></td><td data-th="String "><p>d </p></td><td data-th="Unit of time interval to add "><p>Day; truncated to integral value </p></td></tr><tr><td data-th="Enumeration value "><p><span><span class="input">DateInterval.DayOfYear</span></span></p></td><td data-th="String "><p>y </p></td><td data-th="Unit of time interval to add "><p>Day; truncated to integral value </p></td></tr><tr><td data-th="Enumeration value "><p><span><span class="input">DateInterval.Hour</span></span></p></td><td data-th="String "><p>h </p></td><td data-th="Unit of time interval to add "><p>Hour; truncated to integral value</p></td></tr><tr><td data-th="Enumeration value "><p><span><span class="input">DateInterval.Minute</span></span></p></td><td data-th="String "><p>n </p></td><td data-th="Unit of time interval to add "><p>Minute; truncated to integral value</p></td></tr><tr><td data-th="Enumeration value "><p><span><span class="input">DateInterval.Month</span></span></p></td><td data-th="String "><p>m </p></td><td data-th="Unit of time interval to add "><p>Month; truncated to integral value </p></td></tr><tr><td data-th="Enumeration value "><p><span><span class="input">DateInterval.Quarter</span></span></p></td><td data-th="String "><p>q </p></td><td data-th="Unit of time interval to add "><p>Quarter; truncated to integral value </p></td></tr><tr><td data-th="Enumeration value "><p><span><span class="input">DateInterval.Second</span></span></p></td><td data-th="String "><p>s </p></td><td data-th="Unit of time interval to add "><p>Second; truncated to integral value</p></td></tr><tr><td data-th="Enumeration value "><p><span><span class="input">DateInterval.Weekday</span></span></p></td><td data-th="String "><p>w </p></td><td data-th="Unit of time interval to add "><p>Day; truncated to integral value </p></td></tr><tr><td data-th="Enumeration value "><p><span><span class="input">DateInterval.WeekOfYear</span></span></p></td><td data-th="String "><p>ww </p></td><td data-th="Unit of time interval to add "><p>Week; truncated to integral value </p></td></tr><tr><td data-th="Enumeration value "><p><span><span class="input">DateInterval.Year</span></span></p></td><td data-th="String "><p>yyyy </p></td><td data-th="Unit of time interval to add "><p>Year; truncated to integral value </p></td></tr></tbody></table>
	 * </pre>
	 */
	public static enum Vb6DateInterval {

		DAY("d"), DAYOFYEAR("y"), HOUR("h"), MINUTE("n"), MONTH("m"),
		// QUARTER("q"), --> No tiene un equivalente en Calendar
		SECOND("s"), WEEKDAY("w"), WEEKOFYEAR("ww"), YEAR("yyyy");

		private String vb6StringInterval;
		private int calendarValue;

		Vb6DateInterval(String str) {
			vb6StringInterval = str;

			switch (str) {
			case "d":
				calendarValue = Calendar.DAY_OF_MONTH;
			case "y":
				calendarValue = Calendar.DAY_OF_YEAR;
			case "h":
				calendarValue = Calendar.HOUR;
			case "n":
				calendarValue = Calendar.MINUTE;
			case "m":
				calendarValue = Calendar.MONTH;
			case "s":
				calendarValue = Calendar.SECOND;
			case "w":
				calendarValue = Calendar.DAY_OF_WEEK;
			case "ww":
				calendarValue = Calendar.WEEK_OF_YEAR;
			case "yyyy":
				calendarValue = Calendar.YEAR;
			}

		}

		public String getVb6StringInterval() {
			return vb6StringInterval;
		}

		public int getCalendarValue() {
			return calendarValue;
		}

		public String value() {
			return name();
		}

		public static Vb6DateInterval fromValue(String str) {
			switch (str) {
			case "d":
				return Vb6DateInterval.DAY;
			case "y":
				return Vb6DateInterval.DAYOFYEAR;
			case "h":
				return Vb6DateInterval.HOUR;
			case "n":
				return Vb6DateInterval.MINUTE;
			case "m":
				return Vb6DateInterval.MONTH;
			case "s":
				return Vb6DateInterval.SECOND;
			case "w":
				return Vb6DateInterval.WEEKDAY;
			case "ww":
				return Vb6DateInterval.WEEKOFYEAR;
			case "yyyy":
				return Vb6DateInterval.YEAR;
			default:
				return Vb6DateInterval.DAY; // Se asume dia si no existe
											// coincidencia
			}
		}

	}

	/**
	 * <pre>
	 *     <table responsive="true"><tbody><tr><th><p>Constant</p></th><th><p>Value</p></th><th><p>Description</p></th></tr><tr><td data-th="Constant"><p><span><span class="input">Binary</span></span></p></td><td data-th="Value"><p>0</p></td><td data-th="Description"><p>Performs a binary comparison</p></td></tr><tr><td data-th="Constant"><p><span><span class="input">Text</span></span></p></td><td data-th="Value"><p>1</p></td><td data-th="Description"><p>Performs a text comparison</p></td></tr></tbody></table>
	 * </pre>
	 */
	public static enum Vb6CompareType {

		BINARY(0), TEXT(1);

		private int vb6InStrCompareType;

		Vb6CompareType(int i) {
			vb6InStrCompareType = i;
		}

		public int getVb6InStrCompareType() {
			return vb6InStrCompareType;
		}

		public String value() {
			return name();
		}

		public static Vb6CompareType fromValue(int i) {
			switch (i) {
			case 0:
				return Vb6CompareType.BINARY;
			case 1:
				return Vb6CompareType.TEXT;
			default:
				return Vb6CompareType.BINARY; // Se asume binario si no existe
												// coincidencia
			}
		}

	}

	/**
	 * <table cols="3">
	 * <tbody>
	 * <tr valign="top">
	 * <td class="label"><b>Constant</b></td>
	 * <td class="label"><b>Value</b></td>
	 * <td class="label"><b>Description</b></td>
	 * </tr>
	 * <tr valign="top">
	 * <td><b>vbUpperCase</b></td>
	 * <td>1</td>
	 * <td>Converts the string to uppercase characters.</td>
	 * </tr>
	 * <tr valign="top">
	 * <td><b>vbLowerCase</b></td>
	 * <td>2</td>
	 * <td>Converts the string to lowercase characters.</td>
	 * </tr>
	 * <tr valign="top">
	 * <td><b>vbProperCase</b></td>
	 * <td>3</td>
	 * <td>Converts the first letter of every word in string to uppercase.</td>
	 * </tr>
	 * <tr valign="top">
	 * <td><b>vbWide*</b></td>
	 * <td>4*</td>
	 * <td>Converts narrow (single-byte) characters in string to wide
	 * (double-byte) characters.</td>
	 * </tr>
	 * <tr valign="top">
	 * <td><b>vbNarrow*</b></td>
	 * <td>8*</td>
	 * <td>Converts wide (double-byte) characters in string to narrow
	 * (single-byte) characters.</td>
	 * </tr>
	 * <tr valign="top">
	 * <td><b>vbKatakana**</b></td>
	 * <td>16**</td>
	 * <td>Converts Hiragana characters in string to Katakana characters.</td>
	 * </tr>
	 * <tr valign="top">
	 * <td><b>vbHiragana**</b></td>
	 * <td>32**</td>
	 * <td>Converts Katakana characters in string to Hiragana characters.</td>
	 * </tr>
	 * <tr valign="top">
	 * <td><b>vbUnicode</b></td>
	 * <td>64</td>
	 * <td>Converts the string to
	 * <a href="https://msdn.microsoft.com/en-us/library/aa220300.aspx">Unicode
	 * </a> using the default code page of the system.</td>
	 * </tr>
	 * <tr valign="top">
	 * <td><b>vbFromUnicode</b></td>
	 * <td>128</td>
	 * <td>Converts the string from Unicode to the default code page of the
	 * system.</td>
	 * </tr>
	 * </tbody>
	 * </table>
	 */
	public static enum Vb6EncodingType {

		VBUPPERCASE(1), VBLOWERCASE(2), VBPROPERCASE(3), VBWIDE(4), VBNARROW(8), VBKATAKANA(16), VBHIRAGANA(
				32), VBUNICODE(64), VBFROMUNICODE(128);

		private int vb6StrConvType;

		Vb6EncodingType(int i) {
			vb6StrConvType = i;
		}

		public int getVb6StrConvType() {
			return vb6StrConvType;
		}

		public String value() {
			return name();
		}

		public static Vb6EncodingType fromValue(int i) {
			switch (i) {
			case 1:
				return VBUPPERCASE;
			case 2:
				return VBLOWERCASE;
			case 3:
				return VBPROPERCASE;
			case 4:
				return VBWIDE;
			case 8:
				return VBNARROW;
			case 16:
				return VBKATAKANA;
			case 32:
				return VBHIRAGANA;
			case 64:
				return VBUNICODE;
			case 128:
				return VBFROMUNICODE;
			default:
				return VBFROMUNICODE;
			}
		}
	}

	public static class ArgumentNullException extends Exception {
		public ArgumentNullException() {
			super();
		}

		public ArgumentNullException(String message) {
			super(message);
		}

		public ArgumentNullException(String message, Throwable cause) {
			super(message, cause);
		}

		public ArgumentNullException(Throwable cause) {
			super(cause);
		}

		protected ArgumentNullException(String message, Throwable cause, boolean enableSuppression,
				boolean writableStackTrace) {
			super(message, cause, enableSuppression, writableStackTrace);
		}
	}

}
