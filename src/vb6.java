
import java.io.File;
import java.io.IOException;
import java.lang.Math;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class vb6 {

    // FIXME: Editar las siguientes funciones con los parametros adecuados
    //        Hay que sobrecargar varias de las funciones, una por cada tipo.

    public static class App {
        // Esto tiene que volar del codigo
    }

    public static class Date {
        // Esto tiene que volar del codigo
    }

    public static class Collection {
        // Esto tiene que volar del codigo
    }

    /* Abs - Returns the absolute value of a specified number.
     * https://msdn.microsoft.com/en-us/library/system.math.abs.aspx */

    public static double Abs(double d){
        return Math.abs(d);
    }

    public static float Abs(float f){
        return Math.abs(f);
    }

    public static int Abs(int i){
        return Math.abs(i);
    }

    public static long Abs(long lng){
        return Math.abs(lng);
    }


    /* Asc - Devuelve un valor Integer que representa el código de carácter correspondiente a un carácter.
     * https://msdn.microsoft.com/library/zew1e4wc(v=vs.90).aspx */

    public static int Asc(char chr){
        return Asc(String.valueOf(chr));
    }

    public static int Asc(String str){
        // Cp1252 es el codepage 1252 de Windows
        byte[] bytes = str.getBytes(Charset.forName("Cp1252"));
        return bytes[0] & 255;
    }


    /* CBool - Returns an expression that has been converted to a Variant of subtype Boolean.
     * https://msdn.microsoft.com/library/2k9sfx3c(v=vs.84).aspx */

    public static boolean CBool(char chr){
        return CBool(String.valueOf(chr));
    }

    public static boolean CBool(String str){
        return Boolean.valueOf(str);
    }

    public static boolean CBool(double d){
        return d!=0;
    }

    public static boolean CBool(float f){
        return f!=0;
    }

    public static boolean CBool(int i){
        return i!=0;
    }

    public static boolean CBool(long lng){
        return lng!=0;
    }


    /* CByte - Returns an expression that has been converted to a Variant of subtype Byte.
     * https://msdn.microsoft.com/en-us/library/2ssb79wt(v=vs.84).aspx */

    public static byte CByte(char chr){
        return CByte(String.valueOf(chr));
    }

    public static byte CByte(String str){
        return Byte.valueOf(str);
    }

    public static byte CByte(double d){
        return (byte) d;
    }

    public static byte CByte(float f){
        return (byte) f;
    }

    public static byte CByte(int i){
        return (byte) i;
    }

    public static byte CByte(long lng){
        return (byte) lng;
    }


    /* CDbl - Returns an expression that has been converted to a Variant of subtype Double.
     * https://msdn.microsoft.com/en-us/library/ftekwwt0(v=vs.84).aspx */

    public static double CDbl(char chr){
        return CDbl(String.valueOf(chr));
    }

    public static double CDbl(String str){
        return Double.valueOf(str);
    }

    public static double CDbl(float f){
        return (double) f;
    }

    public static double CDbl(int i){
        return (double) i;
    }

    public static double CDbl(long lng){
        return (double) lng;
    }


    /* Chr - Returns the character associated with the specified character code.
     * https://msdn.microsoft.com/en-us/library/613dxh46(v=vs.90).aspx */

    public static char Chr(int charCode){
        return (char) charCode;
    }


    /* CInt - Returns an expression that has been converted to a Variant of subtype Integer.
     * https://msdn.microsoft.com/en-us/library/fctcwhw9(v=vs.84).aspx */

    public static int CInt(char chr){
        return CInt(String.valueOf(chr));
    }

    public static int CInt(String str){
        return Integer.valueOf(str);
    }

    public static int CInt(double d){
        return (int) d;
    }

    public static int CInt(float f){
        return (int) f;
    }

    public static int CInt(long lng){
        return (int) lng;
    }


    /* CLng - Returns an expression that has been converted to a Variant of subtype Long.
     * https://msdn.microsoft.com/en-us/library/ck4c5842(v=vs.84).aspx */

    public static long CLng(char chr){
        return CLng(String.valueOf(chr));
    }

    public static long CLng(String str){
        return Long.valueOf(str);
    }

    public static long CLng(double d){
        return (long) d;
    }

    public static long CLng(float f){
        return (long) f;
    }

    public static long CLng(int i){
        return (long) i;
    }


    /* CStr - Returns an expression that has been converted to a Variant of subtype String.
     * https://msdn.microsoft.com/en-us/library/0zk841e9(v=vs.84).aspx */

    public static String CStr(char chr){
        return String.valueOf(chr);
    }

    public static String CStr(double d){
        return String.valueOf(d);
    }

    public static String CStr(float f){
        return String.valueOf(f);
    }

    public static String CStr(int i){
        return String.valueOf(i);
    }

    public static String CStr(long lng){
        return String.valueOf(lng);
    }


    /* DateAdd - Returns a Date value containing a date and time value to which a specified time interval has been added.
     * https://msdn.microsoft.com/en-us/library/hcxe65wz(v=vs.90).aspx
     * http://www.csidata.com/custserv/onlinehelp/vbsdocs/vbs92.htm */

    public static java.util.Date DateAdd(String interval, Double number, java.util.Date date){

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Calendar c = Calendar.getInstance();
        c.setTime(date);

        //Parseo el intervalo a formato vb6 y hago la suma
        c.add(Vb6DateInterval.fromValue(interval).getCalendarValue(), number.intValue()); // Adding 5 days

        return c.getTime();

    }


    /* Day - Returns an Integer value from 1 through 31 representing the day of the month.
     * https://msdn.microsoft.com/en-us/library/9b4h8sxy(v=vs.90).aspx */

    public static int Day(){
        return Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
    }
    
    
    /* FileCopy - Copies a file.
     * https://msdn.microsoft.com/en-us/library/2s1c774y(v=vs.90).aspx */

    public static void FileCopy(String source, String destination){
        File sourceFile = new File(source);
        File destFile = new File(destination);

        try {
            FileCopy(sourceFile, destFile);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void FileCopy(File from, File to ) throws IOException {
        Files.copy(from.toPath(), to.toPath());
    }


    /* Fix - function returns the first negative integer greater than or equal to the number.
     * https://msdn.microsoft.com/en-us/library/xh29swte(v=vs.90).aspx */

    public static double Fix(double d){
        return Math.ceil(d);
    }

    public static int Fix(int i){
        return (int) Math.ceil(i);
    }

    public static long Fix(long lng){
        return (long) Math.ceil(lng);
    }


    /* Int - returns the first negative integer less than or equal to Number.
     * https://msdn.microsoft.com/en-us/library/xh29swte(v=vs.90).aspx */

    public static double Int(double d){
        return Math.floor(d);
    }

    public static int Int(int i){
        return (int) Math.floor(i);
    }

    public static long Int(long lng){
        return (long) Math.floor(lng);
    }


    /* Now - A vb6 date function that returns the current date and time together
     * http://www.vb6.us/tutorials/date-time-functions-visual-basic */
    public static java.util.Date Now(){
        return new java.util.Date();
    }

    
    /* Format - Returns a Variant (String) containing an expression formatted according to instructions contained in a format expression.
     * https://msdn.microsoft.com/en-us/library/aa262745(v=vs.60).aspx */

    /* FIXME?
     * Los potenciales casos de uso de la funcion original de VB6 de Format() son muchisimos. Por ende, aca solo se implementan aquellos que
     * se usan en el resto de la aplicacion:
     *
     * /src/frmUserList.java:
     * 	41: 			List1.AddItem(vb6.Format(LoopC, "000") + " "
     *
     * /src/General.java:
     * 	366: 		Declaraciones.LastBackup = vb6.Format(vb6.Now(), "Short Time");
     * 	367: 		Declaraciones.Minutos = vb6.Format(vb6.Now(), "Short Time");
     *
     * /src/modUserRecords.java:
     * 	130: 		Declaraciones.Records[Declaraciones.NumRecords].Fecha = vb6.Format(vb6.Now(), "DD/MM/YYYY hh:mm:ss");
     * */
    public static String Format(Object obj, String format){

        String resultado = "";

        if(format.compareTo("000") == 0 ){
            // 0 = Digit placeholder; prints a trailing or a leading zero in this position, if appropriate.
            resultado = String.format("%03d", (int) obj);
            return resultado;
        }

        java.util.Date date = (java.util.Date) obj;
        SimpleDateFormat sdf;

        if (format.compareTo("DD/MM/YYYY hh:mm:ss") == 0 ){

            sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss.SS");
            resultado = sdf.format(date);

        } else if (format.compareTo("Short Time") == 0 ){

            sdf = new SimpleDateFormat("HH:mm");
            resultado = sdf.format(date);

        }

        return resultado;
    }
    
        
    /* Hour - Devuelve un valor Integer entre 0 y 23 que representa la hora del día.
     * https://msdn.microsoft.com/es-es/library/se56s6ky%28v=vs.90%29.aspx */

    public static int Hour(){
        return Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
    }

    
    /* Weekday - Devuelve un valor Integer que contiene un número que representa el día de la semana.
     * https://msdn.microsoft.com/es-es/library/82yfs2zh%28v=vs.90%29.aspx */

    public static int Weekday(){
        return Calendar.getInstance().get(Calendar.DAY_OF_WEEK);
    }
    
    
    /* Second - Returns an Integer value from 0 through 59 that represents the second of the minute.
     * https://msdn.microsoft.com/es-es/library/82yfs2zh%28v=vs.90%29.aspx */

    public static int Second(){
        return Calendar.getInstance().get(Calendar.SECOND);
    }
    
    
    /* Minute - Devuelve un valor Integer entre 0 y 59 que representa el minuto de la hora.
     * https://msdn.microsoft.com/es-es/library/7eayd8ts%28v=vs.90%29.aspx */

    public static int Minute(){
        return Calendar.getInstance().get(Calendar.MINUTE);
    }
    
    
    /* IsNull - Returns a Boolean value that indicates whether an expression contains no valid data
     * https://msdn.microsoft.com/en-us/library/office/gg278616.aspx */

    public static boolean IsNull(Object obj){
        return obj == null;
    }

    
    /* IsNumeric - Devuelve un valor Boolean que indica si una expresión puede evaluarse como un número. 
     * 
     * IsNumeric devuelve True si el tipo de datos de Expression es Boolean, Byte, Decimal, Double, Integer, 
     * Long, SByte, Short, Single, UInteger, ULong o UShort o un Object que contiene uno de esos tipos numéricos. 
     * También devuelve True si Expression es Char o String, que puede convertir correctamente a un número.
     * 
     * IsNumeric devuelve False si Expression es un dato de tipo Date o un dato de tipo Object y no contiene ningún 
     * tipo numérico. IsNumeric devuelve False si Expression es Char o String que no se puede convertir en un número.
     *
     * https://msdn.microsoft.com/es-es/library/6cd3f6w1%28v=vs.90%29.aspx */

    public static boolean IsNumeric(Object obj){
        return  obj instanceof Integer ||
                obj instanceof Double ||
                obj instanceof Long ||
                obj instanceof Float ||
                obj instanceof Short ||
                obj instanceof Short ||
                (obj instanceof Character && IsNumeric((char) obj)) ||
                (obj instanceof String && IsNumeric((String) obj));
    }

    public static boolean IsNumeric(char c) {
        return IsNumeric(Character.toString(c));
    }

    public static boolean IsNumeric(String s) {
        return s.matches("[-+]?\\d*\\.?\\d+");  //match a number, with optional '-' and/or decimal.
    }  

    
    /* IsDate - Devuelve un valor Boolean que indica si una expresión representa un valor Date válido.
     * https://msdn.microsoft.com/es-es/library/00wf8zk9%28v=vs.90%29.aspx */

    public static boolean IsDate(Object obj){
        return obj instanceof java.util.Date;
    }


    /* InStr - Returns an integer specifying the start position of the first occurrence of one string within another.
     * https://msdn.microsoft.com/en-us/library/8460tsh1(v=vs.90).aspx */

    public static int InStr(String str1, String str2){
        return InStr(1, str1, str2);
    }

    public static int InStr(String str1, String str2, int compare){
        return InStr(1, str1, str2, compare);
    }

    public static int InStr(int start, String str1, String str2){
        return InStr(start, str1, str2, Vb6CompareType.BINARY.getVb6InStrCompareType()); //Si no se especifica, se asume binario
    }
    
    /**
     * @param start
     *   Optional. Numeric expression that sets the starting position for each search. If omitted, search begins at the first character position. The start index is 1-based.
     * @param str1
     *   Required. String expression being searched.
     * @param str2
     *   Required. String expression sought.
     * @param compare
     *   Optional. Specifies the type of string comparison. If Compare is omitted, the Option Compare setting determines the type of comparison.
     *
     * @return
     *  <b>0</b> - If {@code str1} is zero length or Nothing <br>
     *  <b>0</b> - If {@code start} > {@code str2} <br>
     *  <b>0</b> - If {@code str2} is not found <br>
     *  <b>{@code start}</b> - {@code str2} is zero length or Nothing <br>
     *  <b><i>Position where match begins</i></b> - {@code str2} is found within {@code str1}
     *
     * */
    public static int InStr(int start, String str1, String str2, int compare){

        if(str1 == null || str1.length() == 0 || (str2 != null && start > str2.length() ) ) return 0; //Primeros dos casos de retorno
        if(str2 == null || str2.length() == 0) return start; //Cuarto caso de retorno

        if(start >= 1) str1 = str1.substring(start-1); //Porque el 'start' empieza en 1 //(Dios, que mal diseñado esta VB6, peor que la API Date de Java)

        Vb6CompareType comparisonType = Vb6CompareType.fromValue(compare);

        /* La diferencia entre la comparacion TEXT y BINARY no es literal como uno podria imaginarse. BINARY hace referencia a una busqueda case-sensitive,
         * mientras que TEXT es una busqueda case-insensitive
         * */

        int resultSearch = 0;

        if(comparisonType == Vb6CompareType.BINARY){ //case-sensitive
            resultSearch = str1.indexOf(str2);
        } else if (comparisonType == Vb6CompareType.TEXT){ //case-insensitive
            resultSearch = str1.toLowerCase().indexOf(str2.toLowerCase());
        }

        if(resultSearch == -1) return 0; //Si es -1 no hubo ocurrencia; VB6 devuelve 0
        else return resultSearch;

    }


    /*TODO from here*/

    public static void IIf(){
    }

    public static void InStrB(){
    }

    public static void LBound(){
    }

    public static void LCase(){
    }

    public static void Left(){
    }

    public static void Len(){
    }

    public static void LenB(){
    }

    public static void LOF(){
    }

    public static void mid(){
    }

    public static void MsgBox(){
    }

    public static void Replace(){
    }

    public static void RGB(){
    }

    public static void Right(){
    }

    public static void Rnd(){
    }

    public static void Round(){
    }

    public static void RTrim(){
    }

    public static void Sgn(){
    }

    public static void Shell(){
    }

    public static void Space(){
    }

    public static void Split(){
    }

    public static void Sqr(){
    }

    public static void str(){
    }

    public static void StrConv(){
    }

    public static void time(){
    }

    public static void Trim(){
    }

    public static void UBound(){
    }

    public static void UCase(){
    }

    public static void dir(){
    }

    public static void FreeFile(){
    }

    public static void val(){
        // FIXME: la funcion val es una cosa fea de VB6, hay que volarla.
        //        El problema es que se usa en demasiados lugares del codigo
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

        DAY("d"),
        DAYOFYEAR("y"),
        HOUR("h"),
        MINUTE("n"),
        MONTH("m"),
        // QUARTER("q"), --> No tiene un equivalente en Calendar
        SECOND("s"),
        WEEKDAY("w"),
        WEEKOFYEAR("ww"),
        YEAR("yyyy");

        private String vb6StringInterval;
        private int calendarValue;

        Vb6DateInterval(String str){
            vb6StringInterval = str;

            switch (str){
                case "d": calendarValue = Calendar.DAY_OF_MONTH;
                case "y": calendarValue = Calendar.DAY_OF_YEAR;
                case "h": calendarValue = Calendar.HOUR;
                case "n": calendarValue = Calendar.MINUTE;
                case "m": calendarValue = Calendar.MONTH;
                case "s": calendarValue = Calendar.SECOND;
                case "w": calendarValue = Calendar.DAY_OF_WEEK;
                case "ww": calendarValue = Calendar.WEEK_OF_YEAR;
                case "yyyy": calendarValue = Calendar.YEAR;
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
            switch (str){
                case "d": return Vb6DateInterval.DAY;
                case "y": return Vb6DateInterval.DAYOFYEAR;
                case "h": return Vb6DateInterval.HOUR;
                case "n": return Vb6DateInterval.MINUTE;
                case "m": return Vb6DateInterval.MONTH;
                case "s": return Vb6DateInterval.SECOND;
                case "w": return Vb6DateInterval.WEEKDAY;
                case "ww": return Vb6DateInterval.WEEKOFYEAR;
                case "yyyy": return Vb6DateInterval.YEAR;
                default: return Vb6DateInterval.DAY; //Se asume dia si no existe coincidencia
            }
        }

    }

    /**
     * <pre>
     *     <table responsive="true"><tbody><tr><th><p>Constant</p></th><th><p>Value</p></th><th><p>Description</p></th></tr><tr><td data-th="Constant"><p><span><span class="input">Binary</span></span></p></td><td data-th="Value"><p>0</p></td><td data-th="Description"><p>Performs a binary comparison</p></td></tr><tr><td data-th="Constant"><p><span><span class="input">Text</span></span></p></td><td data-th="Value"><p>1</p></td><td data-th="Description"><p>Performs a text comparison</p></td></tr></tbody></table>
     * </pre>
     * */
    public static enum Vb6CompareType {

        BINARY(0),
        TEXT(1);

        private int vb6InStrCompareType;

        Vb6CompareType(int i){
            vb6InStrCompareType = i;
        }

        public int getVb6InStrCompareType() {
            return vb6InStrCompareType;
        }

        public String value() {
            return name();
        }

        public static Vb6CompareType fromValue(int i) {
            switch (i){
                case 0: return Vb6CompareType.BINARY;
                case 1: return Vb6CompareType.TEXT;
                default: return Vb6CompareType.BINARY; //Se asume binario si no existe coincidencia
            }
        }

    }

}

