package com.emi.log;

import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.pattern.ConverterKeys;
import org.apache.logging.log4j.core.pattern.LogEventPatternConverter;
import org.apache.logging.log4j.core.pattern.PatternConverter;

import com.emi.base.BeneformRuntime;

@Plugin(name = "MessageIdPatternConverter", category = PatternConverter.CATEGORY)
@ConverterKeys({ "Id", "messageId" })
public class MessageIdPatternConverter
    extends LogEventPatternConverter
{

    private static final String key_id = "msgId";
    
    private static final String pre_msg = "msgId:";

    /**
     * Singleton.
     */
    private static final MessageIdPatternConverter INSTANCE = new MessageIdPatternConverter();

    private MessageIdPatternConverter()
    {
        super("Id", "messageid");
    }

    /**
     * Obtains an instance of MessageIdPatternConverter.
     *
     * @param options
     *            options, currently ignored, may be null.
     * @return instance of MessageIdPatternConverter.
     */
    public static MessageIdPatternConverter newInstance(final String[] options)
    {
        return INSTANCE;
    }

    @Override
    public void format(LogEvent event, StringBuilder toAppendTo)
    {
        HttpServletRequest requtest = BeneformRuntime.getRequest();
        if (requtest != null)
        {
            toAppendTo.append(pre_msg);
            toAppendTo.append(requtest.getParameter(key_id));
        } else
        {
            toAppendTo.append("SYSTEM");
        }
    }
}
