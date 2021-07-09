package android.content.res;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class AssetManager {
    /**
     * Mode for {@link #open(String, int)}: no specific information about how
     * data will be accessed.
     */
    public static final int ACCESS_UNKNOWN = 0;
    /**
     * Mode for {@link #open(String, int)}: Read chunks, and seek forward and
     * backward.
     */
    public static final int ACCESS_RANDOM = 1;
    /**
     * Mode for {@link #open(String, int)}: Read sequentially, with an
     * occasional forward seek.
     */
    public static final int ACCESS_STREAMING = 2;
    /**
     * Mode for {@link #open(String, int)}: Attempt to load contents into
     * memory, for fast small reads.
     */
    public static final int ACCESS_BUFFER = 3;
    
    public  InputStream open( String fileName) throws IOException {
        return open(fileName, ACCESS_STREAMING);
    }
    
    public  InputStream open( String fileName, int accessMode) throws IOException {
        File file = new File("assets");
        if(file.exists()){
        	FileInputStream inputStream = new FileInputStream(new File(file, fileName));
        	return inputStream;
        }
        return null;
    }
}
