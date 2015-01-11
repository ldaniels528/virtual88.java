package org.ldaniels528.javapc.util;

import org.apache.commons.io.IOUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * Resource Helper
 *
 * @author lawrence.daniels@gmail.com
 */
public class ResourceHelper {

    /**
     * Reads the file from the disk
     *
     * @param file the given {@link java.io.File file}
     * @return the binary data
     * @throws java.io.IOException
     */
    public static byte[] getBinaryContents(final File file) throws IOException {
        final ByteArrayOutputStream out = new ByteArrayOutputStream((int) file.length());
        try (final FileInputStream in = new FileInputStream(file)) {
            IOUtils.copy(in, out);
            return out.toByteArray();
        }
    }

}
