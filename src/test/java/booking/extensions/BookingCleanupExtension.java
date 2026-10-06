package booking.extensions;

import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolver;

public class BookingCleanupExtension implements ParameterResolver, AfterEachCallback {

    private static final ExtensionContext.Namespace NAMESPACE =
            ExtensionContext.Namespace.create(BookingCleanupExtension.class);

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) {
        return parameterContext.getParameter().getType() == BookingCleaner.class;
    }

    @Override
    public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) {
        return extensionContext.getStore(NAMESPACE)
                .getOrComputeIfAbsent(BookingCleaner.class, key -> new BookingCleaner(), BookingCleaner.class);
    }

    @Override
    public void afterEach(ExtensionContext context) {
        BookingCleaner cleaner = context.getStore(NAMESPACE).remove(BookingCleaner.class, BookingCleaner.class);

        if (cleaner != null) {
            cleaner.deleteAll();
        }
    }
}
