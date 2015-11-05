package uk.co.ribot.androidboilerplate;

import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import rx.Observable;
import uk.co.ribot.androidboilerplate.data.model.Ribot;
import uk.co.ribot.androidboilerplate.injection.TestComponentRule;
import uk.co.ribot.androidboilerplate.ui.activity.MainActivity;
import uk.co.ribot.androidboilerplate.util.ClearDataRule;
import uk.co.ribot.androidboilerplate.util.MockModelsUtil;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.mockito.Mockito.when;

@RunWith(AndroidJUnit4.class)
public class MainActivityTest {

    @Rule
    public final ActivityTestRule<MainActivity> main =
            new ActivityTestRule<>(MainActivity.class, false, false);
    @Rule
    public final TestComponentRule component = new TestComponentRule();
    @Rule
    public final ClearDataRule clearDataRule = new ClearDataRule(component);

    @Test
    public void listOfRibotsShows() {
        List<Ribot> mockRibots = MockModelsUtil.createListRibots(20);
        when(component.getMockRibotsService().getRibots())
                .thenReturn(Observable.just(mockRibots));

        main.launchActivity(null);

        int position = 0;
        for (Ribot mockRibot : mockRibots) {
            onView(withId(R.id.recycler_view))
                    .perform(RecyclerViewActions.scrollToPosition(position));
            String name = String.format("%s %s", mockRibot.profile.name.first,
                    mockRibot.profile.name.last);
            onView(withText(name))
                    .check(matches(isDisplayed()));
            onView(withText(mockRibot.profile.email))
                    .check(matches(isDisplayed()));
            position++;
        }
    }

}