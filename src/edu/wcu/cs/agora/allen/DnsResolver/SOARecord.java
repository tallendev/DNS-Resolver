package edu.wcu.cs.agora.allen.DnsResolver;

import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * Created by tyler on 10/24/13.
 */
public class SOARecord extends Record
{
    /** The number of ints contained in Rdata in SOA. */
    public static final int NUM_INTS_IN_SOA = 5;
    /** The number of bytes in an int. */
    public static final int INT_SIZE        = 4;
    /** One byte index. */
    public static final int ONE_BYTE        = 1;
    /** Two byte indexes. */
    public static final int TWO_BYTES       = 2;
    /** Three byte indexes. */
    public static final int THREE_BYTES     = 3;

    /**
     * Constructor simply calls super.
     * @param name Associated with the name field in Record.
     * @param type Associated with the type field in Record.
     * @param classType Associated with the classType field in Record.
     * @param ttl Associated with the ttl field in Record.
     * @param rdata Associated with the RDATA field in Record.
     */
    protected SOARecord(String name, RecordType type, String classType, int ttl,
                       List<Byte> rdata)
    {
        super(name, type, classType, ttl, rdata);
    }

    /**
     * Adds the rData for this record to super.toString().
     * @return The newly created toString.
     */
    @Override
    public String toString()
    {
        return super.toString() + formatRData();
    }

    /**
     * Formats the RDATA in a readable format. Because this contains domain
     * addresses, it is already formatted. We simply transform the bytes into
     * String form.
     * @return The string form of the RDATA.
     */
    private String formatRData()
    {
        List<Byte> rdata = super.getRdata();
        List<Byte> sublist = rdata.subList(0, rdata.size() -
                                              NUM_INTS_IN_SOA * INT_SIZE);
        String ret = new String(Util.unboxBytes(sublist),
                                StandardCharsets.UTF_8) + "\t";
        for (int i = 0; i < NUM_INTS_IN_SOA * INT_SIZE; i += INT_SIZE)
        {
            ret += Util.bytesToInt(sublist.get(i), sublist.get(i + ONE_BYTE),
                                   sublist.get(i + TWO_BYTES),
                                   sublist.get(i + THREE_BYTES)) + "\t";
        }
        return ret;
    }
}
