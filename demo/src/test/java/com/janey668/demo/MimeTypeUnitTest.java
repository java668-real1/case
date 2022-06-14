package com.janey668.demo;

import net.sf.jmimemagic.*;
import org.apache.tika.Tika;
import org.junit.Test;

import javax.activation.MimetypesFileTypeMap;
import java.io.File;
import java.io.IOException;
import java.net.FileNameMap;
import java.net.MalformedURLException;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.Assert.assertEquals;

/**
 * Test class demonstrating various strategies to resolve MIME type of a file.
 * @author tritty
 *
 */
public class MimeTypeUnitTest {
    /**
     * Expected Ouput.
     */
    public static final String PNG_EXT = "image/png";

    /**
     * The location of the file.
     */
//    public static final String FILE_LOC = "D:\\work\\project\\github\\case\\demo\\file\\2237c4df-12b4-497a-83c7-e5bf95c6fc7f";
    public static final String FILE_LOC = "D:\\work\\project\\github\\case\\demo\\file\\998864ec-5894-47df-ba55-a346718382d0.png";

    /**
     * Test method, demonstrating usage in Java 7.
     * 
     * @throws IOException
     */
    @Test
    public void whenUsingJava7_thenSuccess() throws IOException {
        final Path path = new File(FILE_LOC).toPath();
        final String mimeType = Files.probeContentType(path);
//        assertEquals(mimeType, PNG_EXT);
        System.out.println(mimeType);
    }

    /**
     * Test method demonstrating the usage of URLConnection to resolve MIME type.
     * 
     * @throws MalformedURLException
     * @throws IOException
     */
    @Test
    public void whenUsingGetContentType_thenSuccess() throws MalformedURLException, IOException {
        final File file = new File(FILE_LOC);
        final URLConnection connection = file.toURL()
            .openConnection();
        final String mimeType = connection.getContentType();
//        assertEquals(mimeType, PNG_EXT);
        System.out.println(mimeType);
    }

    /**
     * Test method demonstrating the usage of URLConnection to resolve MIME type.
     * 
     */
    @Test
    public void whenUsingGuessContentTypeFromName_thenSuccess() {
        final File file = new File(FILE_LOC);
        final String mimeType = URLConnection.guessContentTypeFromName(file.getName());
//        assertEquals(mimeType, PNG_EXT);
        System.out.println(mimeType);
    }

    /**
     * Test method demonstrating the usage of FileNameMap from URLConnection
     * to resolve MIME type of a file.
     * 
     */
    @Test
    public void whenUsingGetFileNameMap_thenSuccess() {
        final File file = new File(FILE_LOC);
        final FileNameMap fileNameMap = URLConnection.getFileNameMap();
        final String mimeType = fileNameMap.getContentTypeFor(file.getName());
//        assertEquals(mimeType, PNG_EXT);
        System.out.println(mimeType);
    }


    /**
     * Test method demonstrating the usage of MimeTypesFileTypeMap for resolution of 
     * MIME type.
     * 
     */
    @Test
    public void whenUsingMimeTypesFileTypeMap_thenSuccess() {
        final File file = new File(FILE_LOC);
        final MimetypesFileTypeMap fileTypeMap = new MimetypesFileTypeMap();
        final String mimeType = fileTypeMap.getContentType(file.getName());
        assertEquals(mimeType, PNG_EXT);
    }

    /**
     * Test method demonstrating usage of jMimeMagic.
     * 
     * @throws MagicParseException
     * @throws MagicMatchNotFoundException
     * @throws MagicException
     */
    @Test
    public void whenUsingJmimeMagic_thenSuccess() throws MagicParseException, MagicMatchNotFoundException, MagicException {
        final File file = new File(FILE_LOC);
        final Magic magic = new Magic();
        final MagicMatch match = magic.getMagicMatch(file, false);
        assertEquals(match.getMimeType(), PNG_EXT);
    }

    /**
     * Test method demonstrating usage of Apache Tika.
     * 
     * @throws IOException
     */
    @Test
    public void whenUsingTika_thenSuccess() throws IOException {
        final File file = new File(FILE_LOC);
        final Tika tika = new Tika();
        final String mimeType = tika.detect(file);
        System.out.println(mimeType);
//        assertEquals(mimeType, PNG_EXT);
    }
}
