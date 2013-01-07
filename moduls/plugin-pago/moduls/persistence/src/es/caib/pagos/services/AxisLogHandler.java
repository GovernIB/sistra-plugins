package es.caib.pagos.services;

import org.apache.axis.AxisFault;
import org.apache.axis.MessageContext;
import org.apache.axis.handlers.BasicHandler;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Log handler para axis.
 * @author rsanz
 */
public class AxisLogHandler extends BasicHandler {
	
	protected static Log LOG = LogFactory.getLog(AxisLogHandler.class);
	
    public void invoke(final MessageContext msgContext) throws AxisFault {
        if ((msgContext.getResponseMessage() != null)
                && (msgContext.getResponseMessage().getSOAPPart() != null)) {
            LOG.info("Pagos response: \n "
                    + msgContext.getResponseMessage().getSOAPPartAsString());
        } else {
            if ((msgContext.getRequestMessage() != null)
                    && (msgContext.getRequestMessage().getSOAPPartAsString() != null)) {
                LOG.info("Pagos request: \n "
                        + msgContext.getRequestMessage().getSOAPPartAsString());
            }
        }
    }
}