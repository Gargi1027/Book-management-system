import java.io.Serializable;

/**
 * Defines the common methods/behaviors of every type of "thing" we can store in the database.
 */
public interface BusinessObject extends Serializable
{
    /**
     * @return The unique identifier of the record
     */
    String getId();

    /**
     * Applies the unique id to the record
     * @param id The unique id
     */
    void setId(String id);
}
