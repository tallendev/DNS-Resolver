package edu.wcu.cs.agora.allen.DnsResolver;

import java.util.List;

/**
 * Record for the ARecord type.
 * @author Tyler Allen
 * @version 10/07/2013
 */
public class ARecord extends Record
{
    /**
     * Constructor simply calls super.
     * @param name Associated with the name field in Record.
     * @param type Associated with the type field in Record.
     * @param classType Associated with the classType field in Record.
     * @param ttl Associated with the ttl field in Record.
     * @param rdata Associated with the RDATA field in Record.
     */
    protected ARecord(String name, RecordType type, String classType, int ttl,
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
     * Formats the Rdata in a readable format. Because this one contains raw
     * IP Addresses, we add a '.' between each element.
     * @return String form of the formatted Rdata.
     */
    private String formatRData()
    {
        String returnVal = "";
        for (Byte bite : getRdata())
        {
            returnVal += Util.unsignByteToShort(bite) + ".";
        }
        return returnVal.substring(0, returnVal.length() - 1);
    }
}
