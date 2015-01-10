package ibmpc.devices.cpu.x86.decoder;

import ibmpc.devices.cpu.OpCode;

import java.util.HashMap;
import java.util.Map;

/**
 * CPU Machine Code Decode Cache
 *
 * @author lawrence.daniels@gmail.com
 */
public class DecodeCache {
    private final Map<Long, OpCode> cache;

    /**
     * Default constructor
     */
    public DecodeCache(final int capacity) {
        this.cache = new HashMap<Long, OpCode>(capacity);
    }

    /**
     * Caches the given opCode using the byte code as the key
     *
     * @param byteCode the given machine code
     * @param opCode   the given {@link OpCode opCode}
     */
    public void cache(final long byteCode, final OpCode opCode) {
        cache.put(byteCode, opCode);
    }

    /**
     * Retrieves from cache the opCode corresponds to the given byte code.
     *
     * @param byteCode the given machine code
     * @return the resultant @link OpCode opCode} or <tt>null</tt> if not found
     */
    public OpCode getOpCode(final long byteCode) {
        return cache.get(byteCode);
    }

}
