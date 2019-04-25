package werkzeuge.graphwerkzeug.presentation.toolbaractions;


import org.junit.Test;
import werkzeuge.graphwerkzeug.presentation.TraceabilityLayouter;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class TraceabilityLayouterTest {

    @Test
    public void isGapBigEnoughTest() {
        TraceabilityLayouter layouter = new TraceabilityLayouter();

        double gap = 200;

        assertThat(layouter.isGapBigEnough(100, 300, gap), equalTo(true));
        assertThat(layouter.isGapBigEnough(101, 300, gap), equalTo(false));
        assertThat(layouter.isGapBigEnough(-100, 100, gap), equalTo(true));
        assertThat(layouter.isGapBigEnough(-99, 100, gap), equalTo(false));

        assertThat(layouter.isGapBigEnough(300, 100, gap), equalTo(false));
        assertThat(layouter.isGapBigEnough(100, -100, gap), equalTo(false));
        assertThat(layouter.isGapBigEnough(99, -100, gap), equalTo(false));
        assertThat(layouter.isGapBigEnough(-300, -100, gap), equalTo(true));
        assertThat(layouter.isGapBigEnough(0, -100, gap), equalTo(false));

    }

    @Test
    public void calculateDistanceForSwiftNodeTest() {
        TraceabilityLayouter layouter = new TraceabilityLayouter();

        double gap = 200;

        assertThat(layouter.calculateDistanceForSwiftNode(100, 299, gap), equalTo(1.0));
        assertThat(layouter.calculateDistanceForSwiftNode(100, 300, gap), equalTo(0.0));
        assertThat(layouter.calculateDistanceForSwiftNode(-100, 100, gap), equalTo(0.0));
        assertThat(layouter.calculateDistanceForSwiftNode(-99, 100, gap), equalTo(1.0));

        assertThat(layouter.calculateDistanceForSwiftNode(300, 100, gap), equalTo(400.0));
        assertThat(layouter.calculateDistanceForSwiftNode(100, -100, gap), equalTo(400.0));
        assertThat(layouter.calculateDistanceForSwiftNode(99, -100, gap), equalTo(399.0));
        assertThat(layouter.calculateDistanceForSwiftNode(-300, -100, gap), equalTo(0.0));
        assertThat(layouter.calculateDistanceForSwiftNode(0, -100, gap), equalTo(300.0));

        assertThat(layouter.calculateDistanceForSwiftNode(100, 301, gap), equalTo(-1.0));
        assertThat(layouter.calculateDistanceForSwiftNode(99, 300, gap), equalTo(-1.0));
        assertThat(layouter.calculateDistanceForSwiftNode(-100, 101, gap), equalTo(-1.0));
        assertThat(layouter.calculateDistanceForSwiftNode(-101, 101, gap), equalTo(-2.0));

    }
}
