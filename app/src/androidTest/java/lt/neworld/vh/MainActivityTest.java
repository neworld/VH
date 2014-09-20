package lt.neworld.vh;

import android.test.ActivityInstrumentationTestCase2;
import android.view.View;

import com.google.android.apps.common.testing.ui.espresso.action.CoordinatesProvider;
import com.google.android.apps.common.testing.ui.espresso.action.GeneralClickAction;
import com.google.android.apps.common.testing.ui.espresso.action.Press;
import com.google.android.apps.common.testing.ui.espresso.action.Tap;
import com.squareup.spoon.Spoon;

import static com.google.android.apps.common.testing.ui.espresso.Espresso.onView;
import static com.google.android.apps.common.testing.ui.espresso.assertion.ViewAssertions.matches;
import static com.google.android.apps.common.testing.ui.espresso.matcher.ViewMatchers.isDisplayed;
import static com.google.android.apps.common.testing.ui.espresso.matcher.ViewMatchers.withId;

public class MainActivityTest extends ActivityInstrumentationTestCase2<MainActivity> {
    private MainActivity mainActivity;

    public MainActivityTest() {
        super(MainActivity.class);
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();

        mainActivity = getActivity();
    }

    public void testMain() throws Exception {
        onView(withId(R.id.fr_photo_set_list_container))
                .check(matches(isDisplayed()));

        Spoon.screenshot(mainActivity, "main");
    }

    public void testTapOnListContainer() throws Exception {
        onView(withId(R.id.fr_photo_set_list_container))
                .perform(new GeneralClickAction(
                        Tap.SINGLE,
                        new CoordinatesProvider() {
                            @Override
                            public float[] calculateCoordinates(View view) {
                                int[] position = new int[2];
                                view.getLocationOnScreen(position);

                                return new float[] {position[0], position[1]};
                            }
                        },
                        Press.FINGER
                ));

        onView(withId(R.id.fr_photo_preview_image))
                .check(matches(isDisplayed()));

        Spoon.screenshot(mainActivity, "concrete_photo");
    }
}
