package edu.wcu.cs.agora.allen.DnsResolver;

import java.util.List;

/**
 * @author Tyler Allen
 * @version 10/07/2013
 */
public abstract class Record
{
    /** The domain name of this record. */
    private String name;
    /** The type of this record. */
    private RecordType type;
    /** The class of this record/*/
    private String classType;
    /** The time this record had to live when requested. */
    private int ttl;
    /** The variable RDATA field containing the lookup information. */
    private List<Byte> rdata;

    /**
     * Protected, as these should be created using hte Factory method.
     * @param name Value to be put in this.name
     * @param type Value to be put in this.type
     * @param classType Value to be put in this.classType
     * @param ttl Value to be put in this.TTL
     * @param rdata Value to be put in this.rdata
     */
    protected Record(String name, RecordType type, String classType, int ttl,
                     List<Byte> rdata)
    {
        this.name = name;
        this.type = type;
        this.classType = classType;
        this.ttl = ttl;
        this.rdata = rdata;
    }

    /**
     * Getter for name.
     * @return The name field
     */
    public String getName()
    {
        return name;
    }

    /**
     * Getter for type.
     * @return The type field.
     */
    public RecordType getType()
    {
        return type;
    }

    /**
     * Getter for the class.
     * @return The class field.
     */
    public String getClassType()
    {
        return classType;
    }

    /**
     * Getter for the ttl of this record.
     * @return The ttl field.
     */
    public int getTtl()
    {
        return ttl;
    }

    /**
     * Getter for the rdata.
     * @return The rdata field.
     */
    public List<Byte> getRdata()
    {
        return rdata;
    }

    /**
     * Factory method for creation of records. This is how records should be
     * created. It is protected because outside classes could not create this
     * type of object. This method will decide which implementation of Record
     * to return.
     * @param name The name associated with the name field.
     * @param type The type associated with the type field.
     * @param classType The classType associated with the classType field.
     * @param ttl The ttl associated with the TTL field.
     * @param rdata The rdata associated with the rdata field. May be
     *              uncompressed before a Record object is created.
     * @return The newly created record. Null will be returned and error message
     *         will be printed in the event that the record type is not
     *         supported.
     */
    protected static Record recordFactory(String name, RecordType type,
                                          short classType, int ttl,
                                          List<Byte> rdata)
    {
        Record record = null;
        String classT = RecordType.classLookup((byte) classType);
        // Selects the proper record, and creates it. Will also call for the
        // RDATA field to be decompressed, if necessary.
        if (rdata != null)
            switch(type)
            {
                case A:
                    record = new ARecord(name, type, classT, ttl, rdata);
                    break;
                case CNAME:
                    record = new CNAMERecord(name, type, classT, ttl, rdata);
                    break;
                case MX:
                    record = new MXRecord(name, type, classT, ttl, rdata);
                    break;
                case NS:
                    record = new NSRecord(name, type, classT, ttl, rdata);
                    break;
                case PTR:
                    record = new PTRRecord(name, type, classT, ttl, rdata);
                    break;
                case SOA:
                    record = new SOARecord(name, type, classT, ttl, rdata);
                    break;
                default :
                    System.err.println("Record type \"" + type + "\" is not " +
                            "supported. This record will be ignored.\n" +
                            "Record Type: " + type.toShort());
            }
        else
            record = new QueryRecord(name, type, classT);
        return record;
    }

    /**
     * ToString for this record. All children should add their own RDATA
     * section.
     * @return The String Representation of this class.
     */
    @Override
    public String toString()
    {
        return String.format("%-30s\t%6S\t%-2S\t%-20s\t", getName(), getType(),
                             getClassType(), Util.readableTime(getTtl()));
    }
}
