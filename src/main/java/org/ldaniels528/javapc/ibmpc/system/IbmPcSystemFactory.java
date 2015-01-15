package org.ldaniels528.javapc.ibmpc.system;

import org.ldaniels528.javapc.ibmpc.devices.display.IbmPcDisplayFrame;

/**
 * IBM PC System Factory
 *
 * @author lawrence.daniels@gmail.com
 */
public class IbmPcSystemFactory {

    /**
     * Creates an instance of an IBM PC jr system
     *
     * @param frame the given {@link IbmPcDisplayFrame display frame}
     * @return a new {@link IbmPcSystem IBM PC jr system}
     */
    public static IbmPcSystem getIBMPCjr(final IbmPcDisplayFrame frame) {
        return new IbmPcSystemImpl(frame, IbmPcSystem.IBM_PCjr);
    }

    /**
     * Creates an instance of an IBM PC system
     *
     * @param frame the given {@link IbmPcDisplayFrame display frame}
     * @return a new {@link IbmPcSystem IBM PC system}
     */
    public static IbmPcSystem getIBMPC(final IbmPcDisplayFrame frame) {
        return new IbmPcSystemImpl(frame, IbmPcSystem.IBM_PC);
    }

}
