package com.booking.app.utils;

import lombok.experimental.UtilityClass;
import lombok.extern.log4j.Log4j2;

@Log4j2
@UtilityClass
public class ExceptionUtils {

    public static void logException(Throwable e) {
        log.error("Exception occurred", e);

        StackTraceElement[] stackTraceElements = e.getStackTrace();
        if (stackTraceElements.length > 0) {
            StackTraceElement firstElement = stackTraceElements[0];
            String className = firstElement.getClassName();
            String methodName = firstElement.getMethodName();
            int lineNumber = firstElement.getLineNumber();

            log.error("Exception occurred in method: {}.{} at line: {}", className, methodName, lineNumber);
        }
    }

}
