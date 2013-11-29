package edu.wcu.cs.agora.allen.DnsResolver;


/**
 * This program attempts to make DNS resolution requests to a DNS server
 * specified in command line arguments. It will attempt a UDP request up to
 * 3 times, and then exit. If a DNS response packet is truncated, this program
 * will attempt a TCP connection instead. However, some servers do not implement
 * or respond to TCP DNS requests, so this is a last resort.
 *
 * @author Tyler Allen
 * @version 10/7/2013
 */

import java.io.IOException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.List;


public class Main
{

    /** The correct number of arguments for this program.*/
    public static final int CORRECT_ARGS = 3;

    /** Exit code for Invalid Arguments. */
    public static final int ERROR_INVALID_ARGS       = 1;
    /** Exit code for Invalid Hostname. */
    public static final int ERROR_INVALID_HOSTNAME   = 2;
    /** Exit code for Unexpected Socket Timeout. */
    public static final int ERROR_SOCKET_TIMEOUT     = 3;
    /** Exit code for Generic IO Exception. (See error message.) */
    public static final int GENERIC_IO_EXCEPTION     = 4;
    /** Exit code for Socket related exceptions.*/
    public static final int GENERIC_SOCKET_EXCEPTION = 5;
    /** Exit code for errors in DNS packets returned.*/
    public static final int GENERIC_DNS_EXCEPTION    = 6;

    /** Argument in ARGS array where the DNS IP is expected. */
    public static final int DNS_IP = 0;
    /** Argument in ARGS array containing the hostname to look up. */
    public static final int HOSTNAME_IP = 1;
    /** Argument in ARGS array containing the type of record to request. */
    public static final int LOOKUP_TYPE = 2;

    /** The number of attempts that a UDP request will be tried.*/
    public final static int UDP_ATTEMPTS = 3;

    /**
     * Main primarily contains argument checking, and final printing of results.
     * All requests are made in the helper method makeRequest().
     * @param args Needs to contain the DNS server to contact, the address to
     *             lookup, and the type of record to request.
     */
    public static void main(String[] args)
    {
	    if (args.length != CORRECT_ARGS)
            usage(ERROR_INVALID_ARGS, "Invalid Number of Arguments: " +
                                       args.length);
        if (RecordType.typeLookup(args[LOOKUP_TYPE]) == null)
            usage(ERROR_INVALID_ARGS, "Unsupported Record Type");
        Request request = null;
        try
        {
            request = new Request(args[DNS_IP], args[HOSTNAME_IP],
                                  args[LOOKUP_TYPE]);
        }
        catch (UnknownHostException uhe)
        {
            System.err.println(uhe.getMessage());
            usage(ERROR_INVALID_HOSTNAME, "Unknown Host");
        }
        Resolver.Response response = makeRequest(request);
        System.out.println("Queries: ");
        printRecordList(response.getQueries());
        System.out.println("\nAnswers: ");
        printRecordList(response.getAnswers());
        System.out.println("\nAuthority: ");
        printRecordList(response.getAuthority());
        System.out.println("\nAdditional: ");
        printRecordList(response.getAdditional());
    }

    private static void printRecordList(List<Record> list)
    {
        for (Record record : list)
        {
            System.out.println(record);
        }
    }

    /**
     * Helper method for main. Calls the helper method tryUDP.
     * In the event that tryUDP throws a TruncatedPacketException, we will call
     * tryTCP as an attempt to recover. This method also handles all exceptions
     * related to user error and socket exceptions, and exits accordingly.
     *
     * @param request The request we will be sending to Resolver to resolve.
     * @return The interpreted Response received from the server.
     */
    private static Resolver.Response makeRequest(Request request)
    {
        Resolver resolver = Resolver.getInstance();
        Resolver.Response response = null;
        try
        {
            try
            {
                response = tryUDP(resolver, request);
            }
            catch (TruncatedUdpPacketException tupe)
            {
                System.err.println("Truncated UDP Packet Exception:\n" +
                                   tupe.getMessage() +
                                   "\nAttempting TCP Connection...");
                response = tryTCP(resolver, request);
            }
        }
        catch (SocketTimeoutException ste)
        {
            System.err.println("Response timed out. UDP Request likely lost " +
                               "or DNS port is closed.");
            System.exit(ERROR_SOCKET_TIMEOUT);
        }
        catch (SocketException se)
        {
            System.err.println("Socket Exception Information:\n" +
                    se.getMessage());
            System.exit(GENERIC_SOCKET_EXCEPTION);
        }
        catch (DnsException de)
        {
            System.err.println("Error in DNS Response from server:\n" +
                    de.getMessage());
            System.exit(GENERIC_DNS_EXCEPTION);
        }
        catch (IOException ioe)
        {
            System.err.println("IO Exception Information:\n" +
                    ioe.getMessage());
            System.exit(GENERIC_IO_EXCEPTION);
        }
        return response;
    }

    /**
     * This method is a helper method for main. It calls resolver to make a
     * UDP request. It will try 3 times to safeguard against UDP packet loss.
     * In the event that the 3rd attempt fails, the exception will be thrown up
     * to be handled in main.
     * @param resolver The resolver object to process the DNS resolution.
     * @param request The request to send to the Resolver.
     * @return The response received back from Resolver.
     * @throws IOException Various Socket, IO, and DNS exceptions. See
     *                     the exceptions that can be thrown by
     *                     the makeUdpRequest in Resolver.
     */
    private static Resolver.Response tryUDP(Resolver resolver, Request request)
                                   throws IOException
    {
        Resolver.Response response = null;
            for(int i = 1; i < UDP_ATTEMPTS + 1 && response == null; i++)
            {
                try
                {
                    response = resolver.makeUdpRequest(request);
                }
                catch (SocketTimeoutException ste)
                {
                    System.err.println("Socket timed out on attempt: " + i +
                                       ".\nSocket Message: " +
                                       ste.getMessage() + "\nRetrying...");
                    if(i == UDP_ATTEMPTS)
                    {
                        throw ste;
                    }
                }
            }
        return response;
    }

    /**
     * This helper method will attempt a TCP request. This is primarily to be
     * used as a fallback, in the event that UDP responses are too large.
     * @param resolver The resolver to make the TCP request through.
     * @param request The DNS request to be resolved.
     * @return The Response to the DNS request.
     * @throws IOException Various socket and DNS exceptions. See the exceptions
     *                     thrown in the Resolver class from the makeTcpRequest
     *                     method.
     */
    private static Resolver.Response tryTCP(Resolver resolver, Request request)
                                            throws IOException
    {
        Resolver.Response response = null;
        for(int i = 0; i < 2 && response == null; i++)
        {
            try
            {
                System.out.println("Answers: ");
                response = resolver.makeTcpRequest(request);
            }
            catch (SocketTimeoutException ste)
            {
                System.err.println("Socket timed out on attempt: " + i +
                        ".\nSocket Message: " +
                        ste.getMessage() + "\nRetrying...");
            }
        }
        if(response == null)
        {
            throw new SocketTimeoutException();
        }
        return response;
    }

    /**
     * Displays the String contained in output (if output is null, this line
     * is not displayed). Prints a usage messaged for proper usage of this
     * program. Exits with the supplied error code.
     * @param error The error code to exit on.
     * @param output The output string to print. Does not print if null.
     */
    private static void usage(int error, String output)
    {
        if (output != null)
        {
            System.err.println(output);
        }
        String program = System.getProperty("sun.java.command");
        if (program.contains(" "))
            program = program.substring(0, program.indexOf(" "));
        System.err.println("Usage: java " + program + " <DNS IP> <HOSTNAME> " +
                           "<RECORD TYPE>");
        System.err.println("Supported Record Types: A, CNAME, MX, PTR");
        System.exit(error);
    }
}
