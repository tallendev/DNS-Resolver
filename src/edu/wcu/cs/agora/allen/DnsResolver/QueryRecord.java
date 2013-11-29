package edu.wcu.cs.agora.allen.DnsResolver;

import java.util.List;

/**
 * Created by tyler on 10/24/13.
 */
public class QueryRecord extends Record
{
    private final static int USED_FIELDS = 3;

    /**
     * Protected, as these should be created using hte Factory method.
     *
     * @param name      Value to be put in this.name
     * @param type      Value to be put in this.type
     * @param classType Value to be put in this.classType
     * @param ttl       Value to be put in this.TTL
     * @param rdata     Value to be put in this.rdata
     */
    protected QueryRecord(String name, RecordType type, String classType,
                          int ttl, List<Byte> rdata)
    {
        super(name, type, classType, ttl, rdata);
    }

    /**
     * Constructor omitting values we don't care about for Queries. An empty
     * linked list is passed in to avoid a null pointer exception in generating
     * the toString.
     * @param name
     * @param type
     * @param classType
     */
    protected QueryRecord(String name, RecordType type, String classType)
    {
        super(name, type, classType, -1, null);
    }

    @Override
    public String toString()
    {
        return String.format("%-30s\t%6S\t%-2S", getName(), getType(),
                             getClassType());
    }
}
