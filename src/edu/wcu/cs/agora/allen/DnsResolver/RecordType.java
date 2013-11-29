package edu.wcu.cs.agora.allen.DnsResolver;

import java.util.HashMap;

/**
 * This enum contains all the Record Types that we support. It contains the
 * various forms that the "Record Type" literal appears in a DNS packet. It
 * also contains information on if that Record Type has R data that is
 * expandable, so that the Resolver can expand it if necessary.
 * @author Tyler Allen
 * @version 10/23/13
 */
public enum RecordType
{
    /** The A type record. */
    A("A", (short) 1, false, (byte) 0b00000000, (byte) 0b00000001),

    /** The CNAME type record. */
    CNAME("CNAME", (short) 0b00000101, true,
          (byte) 0b00000000, (byte) 0b00000101),

    /** The MX type record. */
    MX("MX", (short) 0b00001111, true, (byte) 0b00000000, (byte) 0b00001111),

    /** The NS type record. */
    NS("NS", (short) 0b00000010, true, (byte) 0b00000000, (byte) 0b00000010),

    /** The PTR type record. */
    PTR("PTR", (short) 0b00001100, true, (byte) 0b00000000, (byte) 0b00001100),

    /** The SOA type record. */
    SOA("SOA", (short) 6, true, (byte) 0b00000000, (byte) 0b00000110);

    /** The String version of the A record type. */
    public static final String A_STR     = "A";
    /** The String version of the CNAME record type. */
    public static final String CNAME_STR = "CNAME";
    /** The String version of the MX record type. */
    public static final String MX_STR    = "MX";
    /** The String version of the NS record type. */
    public static final String NS_STR    = "NS";
    /** The String version of the PTR record type. */
    public static final String PTR_STR   = "PTR";
    /** The String version of the NS record type. */
    public static final String SOA_STR    = "SOA";

    /** The String version of the IN record class. */
    public final static String IN_STR = "IN";
    /** The String version of the CS record class. */
    public final static String CS_STR = "CS";
    /** The String version of the CH record class. */
    public final static String CH_STR = "CH";
    /** The String version of the HS record class. */
    public final static String HS_STR = "HS";



    /** Table that takes the String representation of a record type, and returns
     *  the enum representation. */
    private static final HashMap<String, RecordType> tTable = new HashMap<>();
    /** Table that contains the binary representations, and returns the enum
     * representation. */
    private static final HashMap<Short, RecordType> reverseTTable =
                                                                new HashMap<>();
    /** A table that takes the byte representation of the record class, and
     *  returns the enum representation. */
    private static final HashMap<Byte, String> classTable = new HashMap<>();

    /** Fill up our lookup HashTables. */
    static
    {
        /** Byte representation of A record. */
        final byte[] A_BYTE     = {0b00000000, (byte) 0b00000001};
        /** Byte representation of CNAME record. */
        final byte[] CNAME_BYTE = {0b00000000, (byte) 0b00000101};
        /** Byte representation of MX record. */
        final byte[] MX_BYTE    = {0b00000000, (byte) 0b00001111};
        /** Byte representation of NS record. */
        final byte[] NS_BYTE    = {0b00000000, (byte) 0b00000010};
        /** Byte representation of PTR record. */
        final byte[] PTR_BYTE   = {0b00000000, (byte) 0b00001100};
        /** Byte representation of SOA record. */
        final byte[] SOA_BYTE   = {0b0, (byte) 6};

        /** Short representation of A type record. */
        final short A_SHORT     = Util.bytesToShort(A_BYTE[0],
                                                    A_BYTE[1]);
        /** Short representation of CNAME type record. */
        final short CNAME_SHORT = Util.bytesToShort(CNAME_BYTE[0],
                                                    CNAME_BYTE[1]);
        /** Short representation of MX type record. */
        final short MX_SHORT    = Util.bytesToShort(MX_BYTE[0],
                                                    MX_BYTE[1]);
        /** Short representation of NS type record. */
        final short NS_SHORT    = Util.bytesToShort(NS_BYTE[0],
                                                    NS_BYTE[1]);
        /** Short representation of PTR type record. */
        final short PTR_SHORT   = Util.bytesToShort(PTR_BYTE[0],
                                                    PTR_BYTE[1]);
        /** Short representation of SOA record. */
        final short SOA_SHORT   = (short) 6;

        tTable.put(A_STR, A);
        tTable.put(CNAME_STR, CNAME);
        tTable.put(MX_STR, MX);
        tTable.put(NS_STR, NS);
        tTable.put(PTR_STR, PTR);
        tTable.put(SOA_STR, SOA);

        reverseTTable.put(A_SHORT, A);
        reverseTTable.put(CNAME_SHORT, CNAME);
        reverseTTable.put(MX_SHORT, MX);
        reverseTTable.put(NS_SHORT, NS);
        reverseTTable.put(PTR_SHORT, PTR);
        reverseTTable.put(SOA_SHORT, SOA);

        final byte IN = (byte) 0b00000001;
        final byte CS = (byte) 0b00000010;
        final byte CH = (byte) 0b00000011;
        final byte HS = (byte) 0b00000100;

        classTable.put(IN, IN_STR);
        classTable.put(CS, CS_STR);
        classTable.put(CH, CH_STR);
        classTable.put(HS, HS_STR);
    }

    /** The String representation of this RecordType. */
    private String recordStr;
    /** The short representation of this RecordType. */
    private short recordShrt;
    /** The byte array representation of this record type. */
    private byte[] recordBytes;
    /** Is the Rdata of this record type expandable? */
    private boolean isExpandable;

    private RecordType(String recordStr, short recordShrt, boolean isExpandable,
                       byte... recordBytes)
    {
        this.recordShrt = recordShrt;
        this.recordBytes = recordBytes;
        this.recordStr = recordStr;
        this.isExpandable = isExpandable;
    }

    /**
     * Returns the String representation of this Record Type.
     * @return recordStr The String Representation of this record type.
     */
    public String toString()
    {
        return recordStr;
    }

    /**
     * Returns the short representation of this record type.
     * @return recordShrt The short representation of this record type.
     */
    public short toShort()
    {
        return recordShrt;
    }

    /**
     * Returns the ByteArray representation of this Record Type.
     * @return recordBytes The byte array representation of this record type.
     */
    public byte[] toByteArray()
    {
        return recordBytes;
    }

    /**
     * Returns a boolean result depending on if the RData in this type of
     * record is expandable from the DNS packet.
     * @return isExpandable Boolean representing if the Rdata of this record
     *                      type in the DNS packet is expandable.
     */
    public boolean isRdataExpandable()
    {
        return isExpandable;
    }

    /**
     * Does a lookup for String-Binary classes.
     * @param key The class in byte form.
     * @return The String form of the DNS class.
     */
    public static String classLookup(byte key)
    {
        assert classTable.get(key) != null : "Key: " + key;
        return classTable.get(key);
    }

    /**
     * Does a lookup for Binary-String DNS types.
     * @param key The type, in short form.
     * @return The type, in String form.
     */
    public static RecordType reverseTypeLookup(short key)
    {
        assert reverseTTable.get(key) != null : "key: " + key;
        return reverseTTable.get(key);
    }

    /**
     * Does a lookup on the String form of the type, returning the byte form.
     * @param key The String form of the type.
     * @return The byte form of the type.
     */
    public static RecordType typeLookup(String key)
    {
        assert tTable.get(key.toUpperCase()) != null : "key: " + key;
        return tTable.get(key.toUpperCase());
    }
}
