package edu.wcu.cs.agora.allen.DnsResolver;

import java.util.Collection;
import java.util.List;

/**
 * This class contains some common functionality that is used to handle
 * Byte lists and byte arrays, but is not necessarily class specific.
 *
 * @author Tyler Allen
 * @version 10/07/2013
 */

public class Util
{
    /** The length of a byte, in bits. */
    public static final int BYTE_LEN = 8;
    /** The length of a short, in bits. */
    public static final int SHORT_LEN = 16;
    /** The maximum value of a byte. */
    public static final byte MAX_BYTE = 127;
    /** The number of seconds in a minute. */
    public static final int SEC_IN_MIN = 60;

    /**
     * This method takes all of the bytes in a byte array, and adds them to a
     * provided Byte List, in order.
     * @param array The array to copy.
     * @param list The list being copied to.
     * @return The list that was copied to, for convenience.
     */
    public static List<Byte> byteArrayToList(byte[] array, List<Byte> list)
    {
        for (byte anArray : array)
            list.add(anArray);
        return list;
    }

    /**
     * Combines two byte arrays into one.
     * @param one The first byte array.
     * @param two The second byte array.
     * @return The combined byte array.
     */
    public static byte[] combineByteArray(byte[] one, byte[] two)
    {
        byte[] three = new byte[one.length + two.length];
        System.arraycopy(one, 0, three, 0, one.length);
        System.arraycopy(two, one.length - one.length, three,
                         one.length, three.length - one.length);
        return three;
    }

    /**
     * Convenience method for turning two bytes into a short, using bit
     * shifting.
     * @param byte1 The left byte.
     * @param byte2 The right byte.
     * @return The combined short.
     */
    public static short bytesToShort(byte byte1, byte byte2)
    {
        return (short) ((unsignByteToShort(byte1) << BYTE_LEN) |
                         unsignByteToShort(byte2));
    }

    /**
     * Combines 4 bytes into an int. Uses bytesToShort to group them together.
     * @param byte1 The farthest right byte.
     * @param byte2 The second farthest right byte.
     * @param byte3 The 3rd farthest right byte.
     * @param byte4 The last byte.
     * @return The int created by the 4 bytes.
     */
    public static int bytesToInt(byte byte1, byte byte2, byte byte3, byte byte4)
    {
        return ((int)bytesToShort(byte1, byte2) << SHORT_LEN) |
               (int)bytesToShort(byte3, byte4);
    }

    /**
     * Returns copy of primitive byte array that was passed in.
     * @param t The array to be copied.
     * @return The newly created array.
     */
    public static  byte[] byteArrayCopy(byte[] t)
    {
        return byteArrayCopy(t, t.length);
    }

    /**
     * Creates a copy of the byteArray that was passed in, starting at 0 and
     * going to length len.
     * @param t The array to copy.
     * @param len The number of elements to copy.
     * @return The new array with len elements in it.
     */
    public static byte[] byteArrayCopy(byte[] t, int len)
    {
        byte[] newT = new byte[len];
        System.arraycopy(t, 0, newT, 0, len);
        return newT;
    }

    /**
     * Adds all bytes from an array to a List of Bytes.
     * @param list The list of bytes to be added to.
     * @param array The array to copy bytes from.
     */
    public static void addBytesToList(List<Byte> list, byte[] array)
    {
        addBytesToList(list, array, 0, array.length);
    }

    /**
     * This method adds len bytes to list, starting at pos in the array.
     * @param list The list to be added to.
     * @param array The array to be copied from.
     * @param pos The position in the array to start copying from.
     * @param len The number of elements to copy.
     */
    public static void addBytesToList(List<Byte> list, byte[] array,
                                      int pos, int len)
    {
        for (int i = 0; i < len; i++)
        {
            list.add(array[pos + i]);
        }
    }

    /**
     * Unboxes bytes from a collection into a new array.
     * @param bytes The list to unbox.
     * @return The newly created array.
     */
    public static byte[] unboxBytes(Collection<Byte> bytes)
    {
        byte[] newBytes = new byte[bytes.size()];
        int i = 0;
        for (Byte aByte : bytes)
        {
            newBytes[i] = aByte;
            i++;
        }
        return newBytes;
    }

    /**
     * This method "unsigns" a byte. Due to Java not supporting unsigned
     * Bytes, we use a Short instead.
     * @param in The byte to unsign.
     * @return The unsigned byte.
     */
    public static short unsignByteToShort(byte in)
    {
        return in < 0 ? (short) (MAX_BYTE + (MAX_BYTE + 2 - Math.abs(in))) :
                                (short) in;
    }

    /**
     * This method transforms seconds into a readable minute/second output, of
     * the form:
     * 3 Minutes, 53 Seconds
     * @param seconds The time to be formatted.
     * @return The newly formatted string.
     */
    public static String readableTime(int seconds)
    {
        int min = seconds / SEC_IN_MIN;
        int sec = seconds % SEC_IN_MIN;
        String strMin = " Minute" + ((min != 1) ? "s" : "");
        String strSec = " Second" + ((sec != 1) ? "s" : "");
        return min + strMin + ", " + sec + strSec;
    }

}
