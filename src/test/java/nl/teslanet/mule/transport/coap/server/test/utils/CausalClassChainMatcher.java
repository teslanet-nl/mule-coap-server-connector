package nl.teslanet.mule.transport.coap.server.test.utils;

import static org.hamcrest.Matchers.contains;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

public class CausalClassChainMatcher extends TypeSafeMatcher<Throwable>
{
        private final Class<? extends Throwable>[] expectedClasses;
        private List<Class<? extends Throwable>> causualClasses;
        private Matcher<Iterable<? extends Class<? extends Throwable>>> matcher;

        @SafeVarargs
        public CausalClassChainMatcher(Class<? extends Throwable>... classes) {
            this.expectedClasses = classes;
        }

        @Override
        public void describeTo(Description description) {
            // copy of MatcherAssert.assertThat()
            description.appendText("")
                    .appendText("\nExpected: ")
                    .appendDescriptionOf(matcher)
                    .appendText("\n     but: ");
            this.describeMismatch(causualClasses, description);
        }

        @Override
        protected boolean matchesSafely(Throwable item) {

            List<Class<? extends Throwable>> causes = new ArrayList<Class<? extends Throwable>>();
            while (item != null) {
                causes.add(item.getClass());
                item = item.getCause();
            }
            causualClasses = Collections.unmodifiableList(causes);

            // ordered test
            matcher = contains(expectedClasses);
            return matcher.matches(causualClasses);
        }
}
