import java.io.*;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

/**
 * You'll need to use your imagination here. For simplicity, we're not going to bother connecting to a "real" database.
 * This facade class could theoretically hide the details for connecting to and interacting with a SQL, Mongo,
 * in-memory, or any other kind of database. This provides the common interface for reading/writing to the database
 * of our choice.
 *
 * For this project the data store will simply be a directory of files on the local machine. It won't allow for any
 * actual "query" capability, but gives us the basics of reading, writing, and deleting records given their
 * unique ids.
 */
public class DataStore
{
    /** The root directory for record files */
    private Path dataFilesLocation;

    public DataStore()
    {
        // Just write all record ".obj" files to the "db" directory of the app's execution directory.
        dataFilesLocation = Paths.get("./db");
        dataFilesLocation.toFile().mkdirs();
    }

    /**
     * Saves a new or existing record to the "database". New/existing is based on whether or not there is already an
     * "id" property applied to the record, and if it's a new record, we'll auto-generate and assign the id to the record.
     * @param record The record to save
     * @return A future that resolves w/ the 'id' once the record has been written to the database
     */
    public <T extends BusinessObject> CompletableFuture<String> save(T record)
    {
        CompletableFuture<String> result = new CompletableFuture<>();
        try
        {
            // Make sure that we auto-assign ids to new records. Preserve the id for existing records.
            if (record.getId() == null || record.getId().isEmpty())
            {
                record.setId(UUID.randomUUID().toString());
                System.out.println("[DataStore] Save new: " + record.getClass().getSimpleName() + " : " + record.getId());
            }
            else
            {
                System.out.println("[DataStore] Save existing: " + record.getClass().getSimpleName() + " : " + record.getId());
            }

            // In a production environment we'd just pipe the ObjectOutputStream to the file writer rather than
            // capturing the entire byte array in memory, but for simplicity this will do just fine here.
            Files.write(findRecordPath(record.getClass(), record.getId()), serialize(record));
            result.complete(record.getId());
        }
        catch (Throwable t)
        {
            t.printStackTrace();
            result.completeExceptionally(t);
        }
        return result;
    }

    /**
     * Retrieves the given record from the database (assuming it's actually there).
     * @param recordType The type of record that you want to look up (i.e. the "table")
     * @param id The id of the record to look up
     * @return A future that resolves w/ the populated business object if it exists
     */
    public <T extends BusinessObject> CompletableFuture<T> read(Class<T> recordType, String id)
    {
        CompletableFuture<T> result = new CompletableFuture<>();
        try
        {
            System.out.println("[DataStore] Reading: " + recordType.getSimpleName() + " : " + id);
            Path recordPath = findRecordPath(recordType, id);
            if (recordPath.toFile().exists())
            {
                byte[] serializedBytes = Files.readAllBytes(recordPath);
                result.complete(deserialize(serializedBytes));
            }
            else
            {
                result.completeExceptionally(new IllegalArgumentException("Record does not exist " + id));
            }
        }
        catch (Throwable t)
        {
            t.printStackTrace();
            result.completeExceptionally(t);
        }
        return result;
    }

    /**
     * Retrieves ALL instances of the given type of record. In a real DBMS this would be a nice efficient query, but
     * in this mock database, it reads each record from its individual file and returns a stream of the resulting
     * record instances.
     * @param recordType The type of record that you want to look up (i.e. the "table")
     * @return A future that resolves w/ the populated business objects
     */
    public <T extends BusinessObject> CompletableFuture<Collection<T>> readAll(Class<T> recordType)
    {
        CompletableFuture<Collection<T>> result = new CompletableFuture<>();
        try (DirectoryStream<Path> fileStream = Files.newDirectoryStream(findTablePath(recordType), "*.obj"))
        {
            System.out.println("[DataStore] Reading all: " + recordType.getSimpleName());
            Collection<T> records = new ArrayList<>();
            for (Path recordPath : fileStream)
            {
                records.add(deserialize(Files.readAllBytes(recordPath)));
            }
            result.complete(records);
        }
        catch (Throwable t)
        {
            t.printStackTrace();
            result.completeExceptionally(t);
        }
        return result;
    }

    /**
     * Deletes the given record from the database if it exists.
     * @param recordType The type of record that you want to remove (i.e. the "table")
     * @param id The id of the record to remove
     * @return A future that completes when the delete has finished (or errors if record doesn't exists in the first place)
     */
    public <T extends BusinessObject> CompletableFuture<T> delete(Class<T> recordType, String id)
    {
        CompletableFuture<T> result = new CompletableFuture<>();
        try
        {
            System.out.println("[DataStore] Deleting: " + recordType.getSimpleName() + " : " + id);
            File file = findRecordPath(recordType, id).toFile();
            if (file.exists())
            {
                file.delete();
            }
            else
            {
                result.completeExceptionally(new IllegalArgumentException("Record does not exist " + id));
            }
        }
        catch (Throwable t)
        {
            t.printStackTrace();
            result.completeExceptionally(t);
        }
        return result;
    }



    // -------- Private Helpers ----------------------------------------------

    /**
     * Returns the absolute path to the file that contains the data files for all 
     * all records in the table.
     * @param type The type of business object (i.e. the "table")
     * @return The path to the table's data
     */
    private Path findTablePath(Class<?> type)
    {
        Path path = dataFilesLocation.resolve(type.getSimpleName());
	path.toFile().mkdirs();   // make sure it's actually there for when we try to write it
	return path;
    }

    /**
     * Returns the absolute path to the file that contains the data for this record.
     * @param type The type of business object (i.e. the "table")
     * @param id The id of the record whose data you want
     * @return The path to the record's data
     */
    private Path findRecordPath(Class<?> type, String id)
    {
        return findTablePath(type).resolve(id + ".obj");
    }

    /**
     * Uses standard Java serialization to convert the given object into a raw byte array
     * @param obj The object to serialize
     * @return The serialized bytes
     */
    private byte[] serialize(Object obj) throws IOException
    {
        if (obj != null)
        {
            try (ByteArrayOutputStream bytes = new ByteArrayOutputStream(4096);
                 ObjectOutputStream out = new ObjectOutputStream(bytes))
            {
                out.writeObject(obj);
                return bytes.toByteArray();
            }
        }
        return new byte[0];
    }

    /**
     * Deserializes the data into an actual object of type T
     * @param data The data array to deserialize
     * @param <T> The type of the resulting object
     * @return The deserialized object
     */
    @SuppressWarnings("unchecked")
    private <T> T deserialize(byte[] data) throws ClassNotFoundException, IOException
    {
        if (data != null && data.length > 0)
        {
            try (ByteArrayInputStream bytes = new ByteArrayInputStream(data);
                 ObjectInputStream in = new ObjectInputStream(bytes))
            {
                return (T)in.readObject();
            }
        }
        return null;
    }

}
