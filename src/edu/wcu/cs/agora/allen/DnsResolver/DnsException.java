package edu.wcu.cs.agora.allen.DnsResolver;

import java.io.IOException;

/**
 * This exception is to be used when errors from DNS servers are received. For
 * example, a Malformed DNS Packet would result in a DNS exception.
 *
 * @author Tyler Allen
 * @version 10/9/2013
 */

public class DnsException extends IOException
{
    /**
     * Constructor ensures that children can be created with no args.
     */
    public DnsException()
    {
        super();
    }

    /**
     * Constructor ensures that children can be created with a Reason arg.
     * @param reason The message for this exception.
     */
    public DnsException(String reason)
    {
        super(reason);
    }
}
