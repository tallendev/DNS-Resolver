package edu.wcu.cs.agora.allen.DnsResolver;

/**
 * This Exception is a more specific DNS exception. It is created when a UDP
 * DNS packet has the truncated bit flipped true. This means that the full
 * packet could not be received. The recommended course of action in this case
 * is to attempt a TCP Connection.
 *
 * @author Tyler Allen
 * @version 10/08/2013
 */

public class TruncatedUdpPacketException extends DnsException
{
    /**
     * Ensures that children of this class can be created with no arguments.
     */
    public TruncatedUdpPacketException()
    {
        super();
    }

    /**
     * Ensures that children of this class can be created with a message arg.
     * @param reason The reason this exception was thrown.
     */
    public TruncatedUdpPacketException(String reason)
    {
        super(reason);
    }
}
