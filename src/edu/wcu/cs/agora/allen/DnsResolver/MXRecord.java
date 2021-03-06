package edu.wcu.cs.agora.allen.DnsResolver;

import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * Record for the CNAME Record type.
 * @author Tyler Allen
 * @version 10/07/2013
 */
public class MXRecord extends Record
{
    /** Number of bytes representing "priority" before MX record. */
    public static final int PRIORITY_SIZE = 2;

    /**
     * Constructor simply calls super.
     * @param name Associated with the name field in Record.
     * @param type Associated with the type field in Record.
     * @param classType Associated with the classType field in Record.
     * @param ttl Associated with the ttl field in Record.
     * @param rdata Associated with the RDATA field in Record.
     */
    protected MXRecord(String name, RecordType type, String classType,
                       int ttl, List<Byte> rdata)
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
        return Util.bytesToShort(rdata.get(0), rdata.get(1)) + "\t" +
               new String(Util.unboxBytes(rdata.
                               subList(PRIORITY_SIZE, rdata.size())),
                               StandardCharsets.UTF_8);
    }
}
